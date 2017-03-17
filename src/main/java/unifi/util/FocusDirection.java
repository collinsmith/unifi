package unifi.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A class for defining focus directions. A focus direction can be a real
 * direction (up, down, left, or right), or an abstract direction (forward or
 * backward).
 */
public class FocusDirection {
  @IntDef({ BACKWARD, FORWARD, LEFT, UP, RIGHT, DOWN })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Any {}

  @IntDef({ LEFT, UP, RIGHT, DOWN })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Real {} // Like @Any, but without forward/backward

  /**
   * Move focus to the previous selectable item.
   */
  public static final int BACKWARD = 0x00000001;

  /**
   * Move focus to the next selectable item.
   */
  public static final int FORWARD = 0x00000002;

  /**
   * Move focus to the left.
   */
  public static final int LEFT = 0x00000011;

  /**
   * Move focus up.
   */
  public static final int UP = 0x00000021;

  /**
   * Move focus to the right.
   */
  public static final int RIGHT = 0x00000042;

  /**
   * Move focus down.
   */
  public static final int DOWN = 0x00000082;

  private FocusDirection() {}

}
