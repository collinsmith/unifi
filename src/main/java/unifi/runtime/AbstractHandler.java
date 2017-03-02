package unifi.runtime;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;

public abstract class AbstractHandler implements Handler {
  @NonNull final MessageQueue mQueue;

  public AbstractHandler() {
    mQueue = new MessageQueue();
  }

  public AbstractHandler(@NonNull MessageQueue queue) {
    if (queue == null) throw new IllegalArgumentException("Associated message queue cannot be null");
    mQueue = queue;
  }

  @NonNull
  @Override
  public String getMessageName(@NonNull Message msg) {
    if (msg.callback != null) {
      return msg.callback.getClass().getName();
    }

    return "0x" + Integer.toHexString(msg.what);
  }

  @NonNull
  @Override
  public final Message obtainMessage() {
    return Message.obtain(this);
  }

  @NonNull
  @Override
  public final Message obtainMessage(int what) {
    return Message.obtain(this, what);
  }

  @NonNull
  @Override
  public final Message obtainMessage(int what, @Nullable Object obj) {
    return Message.obtain(this, what, obj);
  }

  @NonNull
  @Override
  public final Message obtainMessage(int what, int arg1, int arg2) {
    return Message.obtain(this, what, arg1, arg2);
  }

  @NonNull
  @Override
  public final Message obtainMessage(int what, int arg1, int arg2, @Nullable Object obj) {
    return Message.obtain(this, what, arg1, arg2, obj);
  }

  @NonNull
  private static Message getPostMessage(@NonNull Runnable r) {
    Message m = Message.obtain();
    m.callback = r;
    return m;
  }

  @NonNull
  private static Message getPostMessage(@NonNull Runnable r, @Nullable Object token) {
    Message m = Message.obtain();
    m.callback = r;
    m.obj = token;
    return m;
  }

  private boolean enqueueMessage(@NonNull Message msg, long whenMillis) {
    msg.target = this;
    msg.when = whenMillis;
    return mQueue.offer(msg);
  }

  @Override
  public final boolean sendMessage(@NonNull Message msg) {
    return sendMessageDelayed(msg, 0);
  }

  @Override
  public final boolean sendMessageDelayed(@NonNull Message msg, long delayMillis) {
    if (delayMillis < 0) {
      delayMillis = 0;
    }

    return sendMessageAtTime(msg, SystemClock.millisTime() + delayMillis);
  }

  public final boolean sendMessageAtTime(@NonNull Message msg, long whenMillis) {
    return enqueueMessage(msg, whenMillis);
  }

  @Override
  public final void removeMessages(int what) {
    removeMessages(what, null);
  }

  @Override
  public final void removeMessages(int what, @Nullable Object token) {
    for (Iterator<Message> it = mQueue.iterator(); it.hasNext();) {
      Message msg = it.next();
      if (msg.what == what && (token == null || msg.obj == token)) {
        it.remove();
        msg.recycleUnchecked();
      }
    }
  }

  @Override
  public final void removeMessages(@Nullable Runnable r, @Nullable Object token) {
    if (r == null) {
      return;
    }

    for (Iterator<Message> it = mQueue.iterator(); it.hasNext(); ) {
      Message msg = it.next();
      if (msg.callback == r && (token == null || msg.obj == token)) {
        it.remove();
        msg.recycleUnchecked();
      }
    }
  }

  @Override
  public final void removeCallbacksAndMessages(@Nullable Object token) {
    for (Iterator<Message> it = mQueue.iterator(); it.hasNext();) {
      Message msg = it.next();
      if (token == null || msg.obj == token) {
        it.remove();
        msg.recycleUnchecked();
      }
    }
  }

  @Override
  public final boolean hasMessages(int what) {
    return hasMessages(what, null);
  }

  @Override
  public final boolean hasMessages(int what, @Nullable Object token) {
    for (Message msg : mQueue) {
      if (msg.what == what && (token == null || msg.obj == token)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public final boolean hasCallbacks(@Nullable Runnable r) {
    for (Message msg : mQueue) {
      if (msg.callback == r) {
        return true;
      }
    }

    return false;
  }

  @Override
  public final void removeCallbacks(@Nullable Runnable r) {
    removeMessages(r, null);
  }

  @Override
  public final void removeCallbacks(@Nullable Runnable r, @Nullable Object token) {
    removeMessages(r, token);
  }

  @Override
  public final boolean post(@NonNull Runnable r) {
    return sendMessageDelayed(getPostMessage(r), 0);
  }

  @Override
  public final boolean postDelayed(@NonNull Runnable r, long delayMillis) {
    return sendMessageDelayed(getPostMessage(r), delayMillis);
  }

  @Override
  public final boolean postAtTime(@NonNull Runnable r, long whenMillis) {
    return sendMessageAtTime(getPostMessage(r), whenMillis);
  }

  @Override
  public final boolean postAtTime(@NonNull Runnable r, @Nullable Object token, long whenMillis) {
    return sendMessageAtTime(getPostMessage(r, token), whenMillis);
  }
}
