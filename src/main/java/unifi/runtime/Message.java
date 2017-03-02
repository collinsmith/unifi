package unifi.runtime;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Defines a message containing a description and arbitrary data object that can
 * be sent to a {@link Handler}. This object contains two extra {@code int}
 * fields and an extra {@code Object} field that allow you to not do allocations
 * in many cases.
 *
 * <p>While the constructor of this class is public, the best way to get one of
 * these instances is to call {@link #obtain Message.obtain()} or one of the
 * {@link Handler#obtainMessage Handler.obtainMessage()} methods, which pulls
 * them from a pool of recycled objects.
 */
public final class Message {
  /**
   * User-defined message code so that the recipient can identify what this
   * message is about. Each {@link Handler} has its own namespace for message
   * codes, so you do not need to worry about yours conflicting with other
   * handlers.
   */
  public int what;

  /**
   * Extra {@code int} field.
   */
  public int arg1;

  /**
   * Extra {@code int} field.
   */
  public int arg2;

  /**
   * Extra {@code Object} field. Typically this is used to reference an
   * arbitrary object to send to the recipient.
   */
  @Nullable public Object obj;

  //region Flags
  static final int FLAG_IN_USE = 1 << 0;
  @Deprecated static final int FLAG_ASYNCHRONOUS = 1 << 1;
  static final int FLAGS_TO_CLEAR_ON_COPY_FROM = FLAG_IN_USE;
  //endregion

  //region Internal Fields
  int flags;
  long when;
  @Nullable Handler target;
  @Nullable Runnable callback;

  /** Link to other message, used for pooling and message queues */
  @Nullable Message next;
  //endregion

  //region Pooling
  private static final int MAX_POOL_SIZE = 50;

  @NonNull private static final Object sPoolSync = new Object();
  @Nullable private static Message sPool;
  private static int sPoolSize = 0;
  //endregion

  //region Obtaining
  /**
   * Obtains a recycled message instance, or creates a new one if none are
   * available.
   */
  @NonNull
  public static Message obtain() {
    synchronized (sPoolSync) {
      if (sPool != null) {
        Message m = sPool;
        sPool = m.next;
        m.next = null;
        m.flags = 0; // clear in-use flag
        sPoolSize--;
        return m;
      }
    }

    return new Message();
  }

  /**
   * Same as {@link #obtain()}, but copies the values of an existing message
   * (including its target) into the new one.
   *
   * @param orig Original message to copy.
   *
   * @return A Message object from the global pool.
   */
  public static Message obtain(@NonNull Message orig) {
    Message m = obtain();
    m.what = orig.what;
    m.arg1 = orig.arg1;
    m.arg2 = orig.arg2;
    m.obj = orig.obj;
    m.target = orig.target;
    m.callback = orig.callback;
    return m;
  }

  /**
   * @see #obtain()
   */
  @NonNull
  public static Message obtain(@Nullable Handler h) {
    Message m = obtain();
    m.target = h;
    return m;
  }

  /**
   * @see #obtain()
   */
  @NonNull
  public static Message obtain(@Nullable Handler h, @Nullable Runnable callback) {
    Message m = obtain();
    m.target = h;
    m.callback = callback;
    return m;
  }

  /**
   * Same as {@link #obtain()}, but sets the values for both <em>target</em> and
   * <em>what</em> members on the Message.
   *
   * @param h    Value to assign to the <em>target</em> member.
   * @param what Value to assign to the <em>what</em> member.
   *
   * @return A Message object from the global pool.
   */
  public static Message obtain(@Nullable Handler h, int what) {
    Message m = obtain();
    m.target = h;
    m.what = what;

    return m;
  }

  /**
   * Same as {@link #obtain()}, but sets the values of the <em>target</em>,
   * <em>what</em>, and <em>obj</em> members.
   *
   * @param h    The <em>target</em> value to set.
   * @param what The <em>what</em> value to set.
   * @param obj  The <em>object</em> method to set.
   *
   * @return A Message object from the global pool.
   */
  public static Message obtain(@Nullable Handler h, int what, @Nullable Object obj) {
    Message m = obtain();
    m.target = h;
    m.what = what;
    m.obj = obj;

    return m;
  }

  /**
   * Same as {@link #obtain()}, but sets the values of the <em>target</em>,
   * <em>what</em>, <em>arg1</em>, and <em>arg2</em> members.
   *
   * @param h    The <em>target</em> value to set.
   * @param what The <em>what</em> value to set.
   * @param arg1 The <em>arg1</em> value to set.
   * @param arg2 The <em>arg2</em> value to set.
   *
   * @return A Message object from the global pool.
   */
  public static Message obtain(@Nullable Handler h, int what, int arg1, int arg2) {
    Message m = obtain();
    m.target = h;
    m.what = what;
    m.arg1 = arg1;
    m.arg2 = arg2;

    return m;
  }

  /**
   * Same as {@link #obtain()}, but sets the values of the <em>target</em>,
   * <em>what</em>, <em>arg1</em>, <em>arg2</em>, and <em>obj</em> members.
   *
   * @param h    The <em>target</em> value to set.
   * @param what The <em>what</em> value to set.
   * @param arg1 The <em>arg1</em> value to set.
   * @param arg2 The <em>arg2</em> value to set.
   * @param obj  The <em>obj</em> value to set.
   *
   * @return A Message object from the global pool.
   */
  public static Message obtain(@Nullable Handler h, int what,
                               int arg1, int arg2, @Nullable Object obj) {
    Message m = obtain();
    m.target = h;
    m.what = what;
    m.arg1 = arg1;
    m.arg2 = arg2;
    m.obj = obj;

    return m;
  }
  //endregion

  //region Recycling
  /**
   * Recycled this message back into the pool and clears all details.
   *
   * <p>Note: You <strong>MUST NOT</strong> touch the message after calling this
   * function, because it has effectively been freed. It is an error to recycle
   * a message that is currently enqueued or that is in the process of being
   * delivered to a {@link Handler}.
   */
  public void recycle() {
    checkRecycled();
    recycleUnchecked();
  }

  private void checkRecycled() {
    if (isInUse()) {
      throw new IllegalStateException(
          "This message cannot be recycled because it is still in use.");
    }
  }

  /**
   * Recycles this message back into the pool and clears all details.
   *
   * <p>Note: Recycled messages are marked as {@link #FLAG_IN_USE in use}.
   */
  void recycleUnchecked() {
    flags = FLAG_IN_USE;
    what = 0;
    arg1 = 0;
    arg2 = 0;
    obj = null;
    when = 0;
    target = null;
    callback = null;

    synchronized (sPoolSync) {
      if (sPoolSize < MAX_POOL_SIZE) {
        next = sPool;
        sPool = this;
        sPoolSize++;
      }
    }
  }
  //endregion

  /**
   * Indicates whether or not this message is currently in use. A message which
   * is "in use" is enqueued in a queue, or recycled.
   */
  boolean isInUse() {
    return (flags & FLAG_IN_USE) == FLAG_IN_USE;
  }

  void markInUse() {
    flags |= FLAG_IN_USE;
  }

  /**
   * Copies the state of the {@code src} message into this one, clearing the
   * specified {@linkplain #FLAGS_TO_CLEAR_ON_COPY_FROM flags}.
   *
   * <p>Note: This is a shallow copy, and does not copy the linked list fields,
   * nor the timestamp or target/callback of the original message.
   */
  public void copyFrom(@NonNull Message src) {
    this.flags = src.flags &= ~FLAGS_TO_CLEAR_ON_COPY_FROM;
    this.what = src.what;
    this.arg1 = src.arg1;
    this.arg2 = src.arg2;
    this.obj = src.obj;
  }

  /**
   * Returns the targeted delivery time of this message, in milliseconds.
   */
  public long getWhen() {
    return when;
  }

  /**
   * @see #getTarget()
   */
  public void setTarget(@Nullable Handler target) {
    this.target = target;
  }

  /**
   * Returns the target {@link Handler} implementation that will receive this
   * message. The object must implement {@link Handler#handleMessage(Message)}.
   * Each handler has its own namespace or message codes, so you don't need to
   * worry about yours conflicting with other handlers.
   */
  @Nullable
  public Handler getTarget() {
    return target;
  }

  /**
   * Returns the callback object that will execute once this message is handled.
   * This object must implement {@link Runnable}. This is called by the
   * {@link #getTarget() target} handler that is receiving this message to
   * dispatch it. If not set, the message will be dispatched to the receiving
   * handler's {@link Handler#handleMessage(Message)} method.
   */
  @Nullable
  public Runnable getCallback() {
    return callback;
  }

  /**
   * Sends this message to the handler specified by {@link #getTarget()}.
   *
   * @throws NullPointerException if this field has no handler.
   */
  public void sendToTarget() {
    target.sendMessage(this);
  }

  /**
   * Indicates whether or not this message is asynchronous, meaning that it is
   * not subject to synchronization barriers.
   *
   * @see #setAsynchronous(boolean)
   */
  @Deprecated
  public boolean isAsynchronous() {
    return (flags & FLAG_ASYNCHRONOUS) == FLAG_ASYNCHRONOUS;
  }

  /**
   * Sets whether or not this message is asynchronous, meaning that is is not
   * subject to synchronization barriers.
   *
   * <p>Note: Asynchronous messages may be delivered out of order with respect
   * to synchronous messages, although they are always delivered in order among
   * themselves. If the relative order of these messages matters, then they
   * probably should not be asynchronous in the first place. Use with caution.
   *
   * @param async {@code true} if the message should be asynchronous,
   *              {@code false} otherwise
   *
   * @see #isAsynchronous()
   */
  @Deprecated
  public void setAsynchronous(boolean async) {
    if (async) {
      flags |= FLAG_ASYNCHRONOUS;
    } else {
      flags &= ~FLAG_ASYNCHRONOUS;
    }
  }

  /**
   * Constructs an empty message, however the preferred way to obtain a message
   * instance is with {@link #obtain() Message.obtain()}.
   */
  public Message() {}

  @NonNull
  @Override
  public String toString() {
    return toString(SystemClock.millisTime());
  }

  @NonNull
  String toString(long now) {
    StringBuilder b = new StringBuilder(64);
    b.append("{ when=");
    b.append(when - now);// TODO: This could be formatted to improve readability
    if (target != null) {
      if (callback != null) {
        b.append(" callback=");
        b.append(callback.getClass().getName());
      } else {
        b.append(" what=");
        b.append(what);
      }

      if (arg1 != 0) {
        b.append(" arg1=");
        b.append(arg1);
      }
      if (arg2 != 0) {
        b.append(" arg2=");
        b.append(arg2);
      }
      if (obj != null) {
        b.append(" obj=");
        b.append(obj);
      }

      b.append(" target=");
      b.append(target.getClass().getName());
    } else {
      b.append(" barrier=");
      b.append(arg1);
    }

    b.append(" }");
    return b.toString();
  }
}
