package unifi.runtime;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueue extends AbstractQueue<Message> implements BlockingQueue<Message> {
  @NonNull private final ReentrantLock lock = new ReentrantLock();

  @Nullable private Thread leader = null;

  @NonNull private final Condition available = lock.newCondition();

  @NonNull private final PriorityQueue<Message> q;

  public MessageQueue() {
    q = new PriorityQueue<>(31, new Comparator<Message>() {
      @Override
      public int compare(Message o1, Message o2) {
        return Long.compare(o1.when, o2.when);
      }
    });
  }

  //region MessageQueue
  //endregion

  //region DelayQueue
  @Override
  public boolean add(@NonNull Message msg) {
    return offer(msg);
  }

  @Override
  public void put(Message msg) {
    offer(msg);
  }

  @Override
  public boolean offer(@NonNull Message msg, long timeout, TimeUnit unit) {
    return offer(msg);
  }

  @Override
  public boolean offer(@NonNull Message msg) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      q.offer(msg);
      if (q.peek() == msg) {
        leader = null;
        available.signal();
      }

      return true;
    } finally {
      lock.unlock();
    }
  }

  @Nullable
  public Message poll() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      Message first = q.peek();
      if (first == null || first.when > SystemClock.millisTime()) {
        return null;
      } else {
        return q.poll();
      }
    } finally {
      lock.unlock();
    }
  }

  @Nullable
  public Message poll(long timeout, @NonNull TimeUnit unit) throws InterruptedException {
    long nanos = unit.toNanos(timeout);
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
      for (;;) {
        Message first = q.peek();
        if (first == null) {
          if (nanos <= 0) {
            return null;
          } else {
            nanos = available.awaitNanos(nanos);
          }
        } else {
          long delay = TimeUnit.MILLISECONDS.toNanos(first.when - SystemClock.millisTime());
          if (delay <= 0) return q.poll();
          if (nanos <= 0) return null;
          first = null;
          if (nanos < delay || leader != null) {
            nanos = available.awaitNanos(nanos);
          } else {
            Thread thisThread = Thread.currentThread();
            leader = thisThread;
            try {
              long timeLeft = available.awaitNanos(delay);
              nanos -= delay - timeLeft;
            } finally {
              if (leader == thisThread) leader = null;
            }
          }
        }
      }
    } finally {
      if (leader == null && q.peek() != null) {
        available.signal();
      }

      lock.unlock();
    }
  }

  @NonNull
  public Message take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
      for (;;) {
        Message first = q.peek();
        if (first == null) {
          available.await();
        } else {
          long delay = TimeUnit.MILLISECONDS.toNanos(first.when - SystemClock.millisTime());
          if (delay <= 0) return q.poll();
          first = null;
          if (leader != null) {
            available.await();
          } else {
            Thread thisThread = Thread.currentThread();
            leader = thisThread;
            try {
              available.awaitNanos(delay);
            } finally {
              if (leader == thisThread) leader = null;
            }
          }
        }
      }
    } finally {
      if (leader == null && q.peek() != null) {
        available.signal();
      }

      lock.unlock();
    }
  }

  @Nullable
  public Message peek() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      return q.peek();
    } finally {
      lock.unlock();
    }
  }

  @Nullable
  private Message peekExpired(long now) {
    // assert lock.isHeldByCurrentThread();
    Message first = q.peek();
    return (first == null || now - first.when > 0) ? null : first;
  }

  @Override
  public int drainTo(Collection<? super Message> c) {
    if (c == null) throw new NullPointerException();
    if (c == this) throw new IllegalArgumentException();
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      int n = 0;
      long now = SystemClock.millisTime();
      for (Message msg; (msg = peekExpired(now)) != null; ) {
        c.add(msg);
        q.poll();
        ++n;
      }

      return n;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int drainTo(Collection<? super Message> c, int maxElements) {
    if (c == null) throw new NullPointerException();
    if (c == this) throw new IllegalArgumentException();
    if (maxElements <= 0) return 0;
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      int n = 0;
      long now = SystemClock.millisTime();
      for (Message msg; n < maxElements && (msg = peekExpired(now)) != null; ) {
        c.add(msg);
        q.poll();
        ++n;
      }

      return n;
    } finally {
      lock.unlock();
    }
  }

  public int size() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      return q.size();
    } finally {
      lock.unlock();
    }
  }

  public void clear() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      q.clear();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int remainingCapacity() {
    return Integer.MAX_VALUE;
  }

  @Override
  public Object[] toArray() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      return q.toArray();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public <T> T[] toArray(T[] a) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      return q.toArray(a);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean remove(@Nullable Object o) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      return q.remove(o);
    } finally {
      lock.unlock();
    }
  }

  void removeEQ(Object o) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      for (Iterator<Message> it = q.iterator(); it.hasNext();) {
        if (o == it.next()) {
          it.remove();
          break;
        }
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Iterator<Message> iterator() {
    return new Itr();
  }

  private class Itr implements Iterator<Message> {
    final Object[] array = toArray();
    int cursor = 0;
    int lastRet = -1;

    @Override
    public boolean hasNext() {
      return cursor < array.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Message next() {
      if (cursor >= array.length) throw new NoSuchElementException();
      lastRet = cursor;
      return (Message) array[cursor++];
    }

    @Override
    public void remove() {
      if (lastRet < 0) throw new IllegalStateException();
      removeEQ(array[lastRet]);
      lastRet = -1;
    }
  }
  //endregion
}
