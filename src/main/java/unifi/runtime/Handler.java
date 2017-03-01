package unifi.runtime;

import android.support.annotation.NonNull;

/**
 * Defines the respondibilities of a class which can handle {@link Message}
 * callbacks.
 */
public interface Handler {
  /**
   * Called when a message has been posted and handles the message.
   */
  void handleMessage(@NonNull Message msg);
}
