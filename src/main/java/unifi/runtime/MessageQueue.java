package unifi.runtime;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.PrintWriter;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import unifi.util.Log;

/**
 * Low-level class consisting of a queue of messages to be dispatched. Messages
 * are not added directly to a {@code MessageQueue}, but rather through
 * {@link Handler} objects.
 */
public final class MessageQueue {
  private static final String TAG = "MessageQueue";
  private static final boolean DBG = true;

  private final boolean mQuitAllowed;
  private boolean mQuitting;

  @Nullable BlockingQueue mQueue;
  @Nullable Message mMessages;

  /**
   * Indicates whether {@link #next()} blocked waiting with a non-zero timeout.
   */
  private boolean mBlocked;

  private int mNextBarrierToken;

  //region IdleHandler
  private final List<IdleHandler> mIdleHandlers = new ArrayList<>();
  private IdleHandler[] mPendingIdleHandlers;

  /**
   * Adds a new {@link IdleHandler} to this message queue. This may be removed
   * automatically for you by returning false from {@link IdleHandler#queueIdle()}
   * when it is invoked, or explicitly removing it with (@link #removeIdleHandler}.
   *
   * <p>This method is safe to call from any thead.
   *
   * @param handler The idle handler to add
   *
   * @throws NullPointerException if {@code handler} is {@code null}.
   */
  public void addIdleHandler(@NonNull IdleHandler handler) {
    if (handler == null) {
      throw new NullPointerException("Can't add a null IdleHandler");
    }

    synchronized (this) {
      mIdleHandlers.add(handler);
    }
  }

  /**
   * Removes the specified idle handler from the queue it was previously added.
   * If the given object is not currently in the idle list, nothing is done.
   *
   * @param handler The idle handler to remove
   */
  public void removeIdleHandler(@Nullable IdleHandler handler) {
    if (handler == null) {
      return;
    }

    synchronized (this) {
      mIdleHandlers.remove(handler);
    }
  }

  /**
   * Defines the responsibilities of a class that deals with when a thread is
   * going to block as it waits for more messages.
   */
  public interface IdleHandler {
    /**
     * Called when the message queue has run out of messages and will not wait
     * for more. Return {@code true} to keep your idle handler active, otherwise
     * {@code false} to have it removed.
     *
     * <p>Note: This may be called if there are still messages pending in the
     * queue, but they are all scheduled to be dispatched after the current
     * time.
     */
    boolean queueIdle();
  }
  //endregion

  MessageQueue(boolean quitAllowed) {
    mQuitAllowed = quitAllowed;
    mQueue = new BlockingQueue();
  }

  @Nullable
  Message next() {
    final BlockingQueue queue = mQueue;
    if (queue == null) {
      return null;
    }

    int pendingIdleHandlerCount = -1;
    int nextPollTimeoutMillis = 0;
    for (;;) {
      try {
        queue.pollOnce(nextPollTimeoutMillis, TimeUnit.MILLISECONDS);
      } catch (InterruptedException e) {
        Log.w(TAG, e.getMessage(), e);
        Thread.currentThread().interrupt();
        continue;
      }

      synchronized (this) {
        // Try the receive the next message. Return if found.
        final long now = SystemClock.millisTime();
        Message prevMsg = null;
        Message msg = mMessages;
        if (msg != null && msg.target == null) {
          // Stalled by a barrier. Find the next asynchronous message in the queue.
          do {
            prevMsg = msg;
            msg = msg.next;
          } while (msg != null && !msg.isAsynchronous());
        }

        if (msg != null) {
          if (now < msg.when) {
            // Next message is not ready. Set a timeout to wake up when it is ready.
            nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
          } else {
            // Got a message
            mBlocked = false;
            if (prevMsg != null) {
              prevMsg.next = msg.next;
            } else {
              mQueue.removeFirst();
            }

            msg.next = null;
            if (DBG) {
              Log.v(TAG, "Returning message: " + msg);
            }

            return msg;
          }
        } else {
          // No more messages
          nextPollTimeoutMillis = -1;
        }

        // Process the quit message now that all pending messages have been handled.
        if (mQuitting) {
          return null;
        }

        // If first time idle, then get the number of idlers to run.
        // Idle handles only run if the queue is empty or if the first message
        // in the queue (possible a barrier) is due to be handled in the future.
        if (pendingIdleHandlerCount < 0
            && (mMessages == null || now < mMessages.when)) {
          pendingIdleHandlerCount = mIdleHandlers.size();
        }

        if (pendingIdleHandlerCount <= 0) {
          // No idle handlers to run. Loop and wait some more.
          mBlocked = true;
          continue;
        }

        if (mPendingIdleHandlers == null) {
          mPendingIdleHandlers = new IdleHandler[Math.max(pendingIdleHandlerCount, 4)];
        }

        mPendingIdleHandlers = mIdleHandlers.toArray(mPendingIdleHandlers);
      }

      // Run the idle handlers.
      // We only ever reach this code block during the first iteration.
      for (int i = 0; i < pendingIdleHandlerCount; i++) {
        final IdleHandler idler = mPendingIdleHandlers[i];
        mPendingIdleHandlers[i] = null; // Release the reference to the handler

        boolean keep = false;
        try {
          keep = idler.queueIdle();
        } catch (Throwable t) {
          Log.wtf(TAG, "IdleHandler threw exception", t);
        }

        if (!keep) {
          synchronized (this) {
            mIdleHandlers.remove(idler);
          }
        }
      }

      // Reset the idle handler count to 0 so we do not run them again.
      pendingIdleHandlerCount = 0;

      // While calling idle handler, a new message could have been delivered,
      // so go back and look again for a pending message without waiting.
      nextPollTimeoutMillis = 0;
    }
  }

  void quit(boolean safe) {
    if (!mQuitAllowed) {
      throw new IllegalStateException("Main thread not allowed to quit.");
    }

    synchronized (this) {
      if (mQuitting) {
        return;
      }

      mQuitting = true;
      if (safe) {
        removeAllFutureMessagesLocked();
      } else {
        removeAllMessagesLocked();
      }

      mQueue.notifyAll();
      mQueue = null;
    }
  }

  int enqueueSyncBarrier(long when) {
    // Enqueue a new sync barrier token.
    // We don't need to wake the queue because the purpose of a barrier is to
    // stall it.
    synchronized (this) {
      final int token = mNextBarrierToken++;
      final Message msg = Message.obtain();
      msg.markInUse();
      msg.when = when;
      msg.arg1 = token;
      mQueue.offer(msg);
      return token;
    }
  }

  void removeSyncBarrier(int token) {
    // Remove a sync barrier token from the queue.
    // If the queue is no longer stalled by a barrier then wake it.
    synchronized (this) {
      Message prev = null;
      Message p = mMessages;
      while (p != null && (p.target != null || p.arg1 != token)) {
        prev = p;
        p = p.next;
      }

      if (p == null) {
        throw new IllegalStateException(
            "The specified message queue synchronization barrier token has " +
            "not been posted or has already been removed.");
      }

      final boolean needWake;
      if (prev != null) {
        prev.next = p.next;
        needWake = false;
      } else {
        mMessages = p.next;
        needWake = mMessages == null || mMessages.target != null;
      }
      p.recycleUnchecked();

      // If the loop is quitting then it is already awake.
      // We can assume mPtr != 0 when mQuitting is false.
      if (needWake && !mQuitting) {
        mQueue.notifyAll();
      }
    }
  }

  boolean enqueue(@NonNull Message msg, long when) {
    if (msg.target == null) {
      throw new IllegalArgumentException("Messages must have a target to be queued.");
    } else if (msg.isInUse()) {
      throw new IllegalStateException("Message " + msg + " is already in use.");
    }

    synchronized (this) {
      if (mQuitting) {
        IllegalStateException e = new IllegalStateException(
            msg.target + " sending message to a Handler on a dead thread");
        Log.w(TAG, e.getMessage(), e);
        msg.recycle();
        return false;
      }

      msg.markInUse();
      msg.when = when;
      mQueue.offer(msg);
    }

    return true;
  }

  boolean hasMessages(@Nullable Handler h, int what, @Nullable Object object) {
    if (h == null) {
      return false;
    }

    synchronized (this) {
      Message p = mMessages;
      while (p != null) {
        if (p.target == h && p.what == what && (object == null || p.obj == object)) {
          return true;
        }

        p = p.next;
      }

      return false;
    }
  }

  boolean hasMessages(@Nullable Handler h, @Nullable Runnable r, @Nullable Object object) {
    if (h == null) {
      return false;
    }

    synchronized (this) {
      Message p = mMessages;
      while (p != null) {
        if (p.target == h && p.callback == r && (object == null || p.obj == object)) {
          return true;
        }

        p = p.next;
      }

      return false;
    }
  }

  boolean isIdling() {
    synchronized (this) {
      return isIdlingLocked();
    }
  }

  private boolean isIdlingLocked() {
    // If the loop is quitting, then it must not be idling.
    return !mQuitting && mQueue.lock.isLocked();
  }

  void removeMessages(@Nullable Handler h, int what, @Nullable Object object) {
    if (h == null) {
      return;
    }

    synchronized (this) {
      Message p = mMessages;

      // Remove all messages at front.
      while (p != null && p.target == h && p.what == what
          && (object == null || p.obj == object)) {
        Message n = p.next;
        mMessages = n;
        p.recycleUnchecked();
        p = n;
      }

      // Remove all messages after front.
      while (p != null) {
        Message n = p.next;
        if (n != null) {
          if (n.target == h && n.what == what
              && (object == null || n.obj == object)) {
            Message nn = n.next;
            n.recycleUnchecked();
            p.next = nn;
            continue;
          }
        }
        p = n;
      }
    }
  }

  void removeMessages(@Nullable Handler h, @Nullable Runnable r, @Nullable Object object) {
    if (h == null || r == null) {
      return;
    }

    synchronized (this) {
      Message p = mMessages;

      // Remove all messages at front.
      while (p != null && p.target == h && p.callback == r
          && (object == null || p.obj == object)) {
        Message n = p.next;
        mMessages = n;
        p.recycleUnchecked();
        p = n;
      }

      // Remove all messages after front.
      while (p != null) {
        Message n = p.next;
        if (n != null) {
          if (n.target == h && n.callback == r
              && (object == null || n.obj == object)) {
            Message nn = n.next;
            n.recycleUnchecked();
            p.next = nn;
            continue;
          }
        }
        p = n;
      }
    }
  }

  void removeCallbacksAndMessages(@Nullable Handler h, @Nullable Object object) {
    if (h == null) {
      return;
    }

    synchronized (this) {
      Message p = mMessages;

      // Remove all messages at front.
      while (p != null && p.target == h
          && (object == null || p.obj == object)) {
        Message n = p.next;
        mMessages = n;
        p.recycleUnchecked();
        p = n;
      }

      // Remove all messages after front.
      while (p != null) {
        Message n = p.next;
        if (n != null) {
          if (n.target == h && (object == null || n.obj == object)) {
            Message nn = n.next;
            n.recycleUnchecked();
            p.next = nn;
            continue;
          }
        }
        p = n;
      }
    }
  }

  private void removeAllMessagesLocked() {
    Message p = mMessages;
    while (p != null) {
      Message n = p.next;
      p.recycleUnchecked();
      p = n;
    }

    mMessages = null;
  }

  private void removeAllFutureMessagesLocked() {
    final long now = SystemClock.millisTime();
    Message p = mMessages;
    if (p != null) {
      if (p.when > now) {
        removeAllMessagesLocked();
      } else {
        Message n;
        for (;;) {
          n = p.next;
          if (n == null) {
            return;
          }
          if (n.when > now) {
            break;
          }
          p = n;
        }

        p.next = null;
        do {
          p = n;
          n = p.next;
          p.recycleUnchecked();
        } while (n != null);
      }
    }
  }

  void dump(PrintWriter pw, String prefix) {
    synchronized (this) {
      long now = SystemClock.millisTime();
      int n = 0;
      for (Message msg = mMessages; msg != null; msg = msg.next) {
        pw.println(prefix + "Message " + n + ": " + msg.toString(now));
        n++;
      }

      pw.println(prefix + "(Total messages: " + n + ", idling=" + isIdlingLocked()
          + ", quitting=" + mQuitting + ")");
    }
  }

  /**
   * Custom implementation of {@link java.util.concurrent.DelayQueue} optimized
   * for {@link Message} instances. All methods are copies of corresponding
   * {@code DelayQueue} methods, the {@link java.util.concurrent.Delayed}
   * dependency has been removed and comparisons improved.
   */
  private class BlockingQueue extends AbstractQueue<Message>
      implements java.util.concurrent.BlockingQueue<Message> {
    final transient ReentrantLock lock = new ReentrantLock();

    /**
     * Thread designated to wait for the element at the head of
     * the queue.  This variant of the Leader-Follower pattern
     * (http://www.cs.wustl.edu/~schmidt/POSA/POSA2/) serves to
     * minimize unnecessary timed waiting.  When a thread becomes
     * the leader, it waits only for the next delay to elapse, but
     * other threads await indefinitely.  The leader thread must
     * signal some other thread before returning from take() or
     * poll(...), unless some other thread becomes leader in the
     * interim.  Whenever the head of the queue is replaced with
     * an element with an earlier expiration time, the leader
     * field is invalidated by being reset to null, and some
     * waiting thread, but not necessarily the current leader, is
     * signalled.  So waiting threads must be prepared to acquire
     * and lose leadership while waiting.
     */
    @Nullable Thread leader = null;

    /**
     * Condition signalled when a newer element becomes available
     * at the head of the queue or a new thread may need to
     * become leader.
     */
    @Nullable final Condition available = lock.newCondition();

    @Override
    public int size() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int remainingCapacity() {
      return Integer.MAX_VALUE;
    }

    /**
     * Inserts the specified element into this message queue. As the queue is
     * unbounded this method will never block.
     *
     * @param message The message to add
     * @param timeout This parameter is ignored as the method never blocks
     * @param unit    This parameter is ignored as the method never blocks
     *
     * @return {@code true}
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean offer(@NonNull Message message, long timeout, @NonNull TimeUnit unit) {
      return offer(message);
    }

    /**
     * Inserts the specified element into this delay queue.
     *
     * @param message The message to add
     *
     * @return {@code true}
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean offer(@NonNull Message message) {
      if (message == null) {
        throw new NullPointerException("Cannot queue null messages.");
      }

      final ReentrantLock lock = this.lock;
      lock.lock();
      try {
        Message node = mMessages, prev = null;
        while (node != null && node.when <= message.when) {
          prev = node;
          node = node.next;
        }

        if (prev == null) {
          mMessages = message;
          message.next = null;

          leader = null;
          available.signal();
        } else {
          prev.next = message;
          message.next = node;
        }

        return true;
      } finally {
        lock.unlock();
      }
    }

    /**
     * Inserts the specified element into this message queue. As the queue is
     * unbounded this method will never block.
     *
     * @param message The message to add
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void put(@NonNull Message message) {
      offer(message);
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns
     * {@code null} if this queue is empty. Unlike {@code poll}, if no expired
     * elements are available in the queue, this method returns the element that
     * will expire next, if one exists.
     *
     * @return The head of this queue, or {@code null} if this queue is empty
     */
    @Nullable
    @Override
    public Message peek() {
      final ReentrantLock lock = this.lock;
      lock.lock();
      try {
        return mMessages;
      } finally {
        lock.unlock();
      }
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary until
     * an element with the expired delay is available in this queue.
     *
     * @return The head of this queue.
     *
     * @throws InterruptedException {@inheritDoc}
     */
    @Override
    public Message take() throws InterruptedException {
      final ReentrantLock lock = this.lock;
      lock.lockInterruptibly();
      try {
        for (;;) {
          Message first = mMessages;
          if (first == null) {
            available.await();
          } else {
            long delay = first.when - SystemClock.millisTime();
            if (delay <= 0) {
              return removeFirst();
            }

            first = null;
            if (leader != null) {
              available.await();
            } else {
              Thread thisThread = Thread.currentThread();
              leader = thisThread;
              try {
                available.awaitNanos(TimeUnit.MILLISECONDS.toNanos(delay));
              } finally {
                if (leader == thisThread) {
                  leader = null;
                }
              }
            }
          }
        }
      } finally {
        if (leader == null && mMessages != null) {
          available.signal();
        }

        lock.unlock();
      }
    }

    private Message removeFirst() {
      Message first = mMessages;
      mMessages = first.next;
      first.next = null;
      return first;
    }

    public void pollOnce(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      final ReentrantLock lock = this.lock;
      lock.lockInterruptibly();
      try {
        for (;;) {
          Message first = mMessages;
          if (first == null) {
            if (nanos <= 0) {
              return;
            } else {
              nanos = available.awaitNanos(nanos);
            }
          } else {
            long delay = first.when - SystemClock.millisTime();
            if (delay <= 0) {
              return;
            }

            if (nanos <= 0) {
              return;
            }

            first = null;
            delay = TimeUnit.MILLISECONDS.toNanos(delay);
            if (nanos < delay || leader != null) {
              nanos = available.awaitNanos(nanos);
            } else {
              Thread thisThread = Thread.currentThread();
              leader = thisThread;
              try {
                long timeLeft = available.awaitNanos(delay);
                nanos -= delay - timeLeft;
              } finally {
                if (leader == thisThread) {
                  leader = null;
                }
              }
            }
          }
        }
      } finally {
        if (leader == null && mMessages != null) {
          available.signal();
        }

        lock.unlock();
      }
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary until
     * an element with an expired delay is available on this queue, or the
     * specified wait time expires.
     *
     * @return The head of this queue, or {@code null} if the specified waiting
     *         time elapses before an element with an expired delay becomes
     *         available
     *
     * @throws InterruptedException {@inheritDoc}
     */
    @Override
    public Message poll(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      final ReentrantLock lock = this.lock;
      lock.lockInterruptibly();
      try {
        for (;;) {
          Message first = mMessages;
          if (first == null) {
            if (nanos <= 0) {
              return null;
            } else {
              nanos = available.awaitNanos(nanos);
            }
          } else {
            long delay = first.when - SystemClock.millisTime();
            if (delay <= 0) {
              return removeFirst();
            }

            if (nanos <= 0) {
              return null;
            }

            first = null;
            delay = TimeUnit.MILLISECONDS.toNanos(delay);
            if (nanos < delay || leader != null) {
              nanos = available.awaitNanos(nanos);
            } else {
              Thread thisThread = Thread.currentThread();
              leader = thisThread;
              try {
                long timeLeft = available.awaitNanos(delay);
                nanos -= delay - timeLeft;
              } finally {
                if (leader == thisThread) {
                  leader = null;
                }
              }
            }
          }
        }
      } finally {
        if (leader == null && mMessages != null) {
          available.signal();
        }

        lock.unlock();
      }
    }

    /**
     * Retrieves and removes the head of this queue, or returns {@code null} if
     * this queue has no elements with an expired delay.
     *
     * @return The head of this queue, or {@code null} if this queue has no
     *         elements with an expired delay
     */
    @Override
    public Message poll() {
      final ReentrantLock lock = this.lock;
      lock.lock();
      try {
        Message first = mMessages;
        if (first == null || first.when > SystemClock.millisTime()) {
          return null;
        } else {
          return removeFirst();
        }
      } finally {
        lock.unlock();
      }
    }

    /**
     * Returns the first element if it is expired. Used only by {@link #drainTo}
     * and {@link #drainTo(Collection, int)}. Call only when holding lock.
     *
     * @return
     */
    private Message peekExpired(long now) {
      // assert lock.isHeldByCurrentThread();
      Message first = mMessages;
      return (first == null || first.when <= now) ? null : first;
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    @Override
    public int drainTo(@NonNull Collection<? super Message> c) {
      if (c == null) {
        throw new NullPointerException();
      } if (c == this) {
        throw new IllegalArgumentException();
      }

      final ReentrantLock lock = this.lock;
      lock.lock();
      long now = SystemClock.millisTime();
      try {
        int n = 0;
        for (Message m; (m = peekExpired(now)) != null; ) {
          c.add(m);
          removeFirst();
          ++n;
        }
        return n;
      } finally {
        lock.unlock();
      }
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    @Override
    public int drainTo(@NonNull Collection<? super Message> c, int maxElements) {
      if (c == null) {
        throw new NullPointerException();
      } else if (c == this) {
        throw new IllegalArgumentException();
      } else if (maxElements <= 0) {
        return 0;
      }

      final ReentrantLock lock = this.lock;
      lock.lock();
      long now = SystemClock.millisTime();
      try {
        int n = 0;
        for (Message m; n < maxElements && (m = peekExpired(now)) != null; ) {
          c.add(m);
          removeFirst();
          ++n;
        }

        return n;
      } finally {
        lock.unlock();
      }
    }

    @Override
    public Iterator<Message> iterator() {
      throw new UnsupportedOperationException();
    }
  }
}
