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
  @IntDef({
      FOCUS_BACKWARD,
      FOCUS_FORWARD,
      FOCUS_LEFT,
      FOCUS_UP,
      FOCUS_RIGHT,
      FOCUS_DOWN
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Any {}

  @IntDef({
      FOCUS_LEFT,
      FOCUS_UP,
      FOCUS_RIGHT,
      FOCUS_DOWN
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Real {} // Like @Any, but without forward/backward

  /**
   * Move focus to the previous selectable item.
   */
  public static final int FOCUS_BACKWARD = 0x00000001;

  /**
   * Move focus to the next selectable item.
   */
  public static final int FOCUS_FORWARD = 0x00000002;

  /**
   * Move focus to the left.
   */
  public static final int FOCUS_LEFT = 0x00000011;

  /**
   * Move focus up.
   */
  public static final int FOCUS_UP = 0x00000021;

  /**
   * Move focus to the right.
   */
  public static final int FOCUS_RIGHT = 0x00000042;

  /**
   * Move focus down.
   */
  public static final int FOCUS_DOWN = 0x00000082;

  private FocusDirection() {}

}
