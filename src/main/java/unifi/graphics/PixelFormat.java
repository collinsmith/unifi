package unifi.graphics;

import android.support.annotation.IntDef;

import com.badlogic.gdx.graphics.Pixmap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Utility class which defines various pixel format special values.
 *
 * @see Pixmap.Format
 */
public final class PixelFormat {
  @IntDef({ UNKNOWN, TRANSLUCENT, TRANSPARENT, OPAQUE })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Opacity {}

  /**
   * Represents an unknown pixel format.
   */
  public static final int UNKNOWN = 0;

  /**
   * Represents a pixel format that supports translucency
   * (many alpha bits).
   */
  public static final int TRANSLUCENT = -3;

  /**
   * Represents a pixel format that supports transparency
   * (at least 1 alpha bit).
   */
  public static final int TRANSPARENT = -2;

  /**
   * Represents an opaque pixel format (no alpha bits required).
   */
  public static final int OPAQUE = -1;

  /**
   * Returns the appropriate opacity value for two source opacities. The return
   * value is determined in the order of <code>
   *   {@link PixelFormat#UNKNOWN UNKNOWN}
   * > {@link PixelFormat#TRANSLUCENT TRANSLUCENT}
   * > {@link PixelFormat#TRANSPARENT TRANSPARENT}
   * > {@link PixelFormat#OPAQUE OPAQUE}</code>.
   *
   * @param op1 One opacity value.
   * @param op2 Another opacity value.
   *
   * @return The resolved opacity of {@code opt1} and {@code opt2}
   */
  @Opacity
  public static int resolveOpacity(@Opacity int op1, @Opacity int op2) {
    if (op1 == op2) {
      return op1;
    }

    if (op1 == UNKNOWN || op2 == UNKNOWN) {
      return UNKNOWN;
    }

    if (op1 == TRANSLUCENT || op2 == TRANSLUCENT) {
      return TRANSLUCENT;
    }

    if (op1 == TRANSPARENT || op2 == TRANSPARENT) {
      return TRANSPARENT;
    }

    return OPAQUE;
  }

  private PixelFormat() {}
}
