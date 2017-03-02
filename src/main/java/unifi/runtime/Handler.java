package unifi.runtime;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Defines the responsibilities of a class which can handle {@link Message}
 * callbacks.
 */
public interface Handler {
  /**
   * Called when a message has been posted and handles the message.
   */
  void handleMessage(@NonNull Message msg);

  @NonNull
  String getMessageName(@NonNull Message msg);

  @NonNull
  Message obtainMessage();

  @NonNull
  Message obtainMessage(int what);

  @NonNull
  Message obtainMessage(int what, @Nullable Object obj);

  @NonNull
  Message obtainMessage(int what, int arg1, int arg2);

  @NonNull
  Message obtainMessage(int what, int arg1, int arg2, @Nullable Object obj);

  boolean sendMessage(@NonNull Message msg);

  boolean sendMessageDelayed(@NonNull Message msg, long delayMillis);

  void removeMessages(int what);

  void removeMessages(int what, @Nullable Object token);

  void removeMessages(@NonNull Runnable r, @Nullable Object token);

  void removeCallbacksAndMessages(@Nullable Object token);

  boolean hasMessages(int what);

  boolean hasMessages(int what, @Nullable Object token);

  boolean hasCallbacks(@Nullable Runnable r);

  void removeCallbacks(@NonNull Runnable r);

  void removeCallbacks(@NonNull Runnable r, @Nullable Object token);

  boolean post(@NonNull Runnable r);

  boolean postDelayed(@NonNull Runnable r, long delayMillis);

  boolean postAtTime(@NonNull Runnable r, long whenMillis);

  boolean postAtTime(@NonNull Runnable r, @Nullable Object token, long whenMillis);
}
