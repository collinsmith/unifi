package unifi.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A class for defining layout directions. A layout direction can be
 * left-to-right (LTR) or right-to-left (RTL). It can also be inherited (from a
 * parent) or deduced from the default language script of a locale.
 */
public class LayoutDirection {
  /**
   * @hide
   */
  @IntDef({ LTR, RTL, INHERIT, LOCALE })
  @Retention(RetentionPolicy.SOURCE)
  // Not called LayoutDirection to avoid conflict with android.util.LayoutDirection
  public @interface Any {}

  /**
   * @hide
   */
  @IntDef({ LTR, RTL })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Resolved {}

  /**
   * Horizontal layout direction of this view is from Left to Right.
   */
  public static final int LTR = 0;

  /**
   * Horizontal layout direction of this view is from Right to Left.
   */
  public static final int RTL = 1;

  /**
   * Horizontal layout direction of this view is inherited from its parent.
   */
  public static final int INHERIT = 2;

  /**
   * Horizontal layout direction of this view is from deduced from the default
   * language script for the locale.
   */
  public static final int LOCALE = 3;

  private LayoutDirection() {}

}
