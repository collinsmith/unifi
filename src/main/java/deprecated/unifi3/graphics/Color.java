package deprecated.unifi3.graphics;

import com.google.common.collect.ImmutableMap;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.Map;

public class Color {

  private Color() {}

  public static final int TRANSPARENT = 0x00000000;
  public static final int CLEAR = TRANSPARENT;

  public static final int BLACK = 0xFF000000;
  public static final int WHITE = 0xFFFFFFFF;
  public static final int DARK_GRAY = 0xFF444444;
  public static final int GRAY = 0xFF888888;
  public static final int LIGHT_GRAY = 0xFFCCCCCC;
  public static final int RED = 0xFFFF0000;
  public static final int GREEN = 0xFF00FF00;
  public static final int BLUE = 0xFF0000FF;
  public static final int YELLOW = 0xFFFFFF00;
  public static final int CYAN = 0xFF00FFFF;
  public static final int MAGENTA = 0xFFFF00FF;

  /**
   * Returns the alpha component of a color int.
   * This is the same as saying {@code color >>> 24}.
   */
  public static int alpha(int color) {
    return color >>> 24;
  }

  /**
   * Returns the red component of a color int.
   * This is the same as saying {@code (color >> 16) & 0xFF}
   */
  public static int red(int color) {
    return (color >> 16) & 0xFF;
  }

  /**
   * Returns the green component of a color int.
   * This is the same as saying {@code (color >> 8) & 0xFF}
   */
  public static int green(int color) {
    return (color >> 8) & 0xFF;
  }

  /**
   * Returns the blue component of a color int.
   * This is the same as saying {@code color & 0xFF}
   */
  public static int blue(int color) {
    return color & 0xFF;
  }

  /**
   * Returns a color-int from the specified red, green, and blue components. The alpha component is
   * implicitly {@code 255} (fully opaque). These component values should be {@code [0..255]}, but
   * there is no range check performed, so if they are out of range, the returned color is
   * undefined.
   *
   * @param red   Red component {@code [0..255]} of the color
   * @param green Green component {@code [0..255]} of the color
   * @param blue  Blue component {@code [0..255]} of the color
   *
   * @return The color-int defined by the specified {@code red}, {@code green} and {@code blue}
   *         values
   */
  public static int rgb(int red, int green, int blue) {
    return (0xFF << 24) | (red << 16) | (green << 8) | blue;
  }

  /**
   * Return a color-int from alpha, red, green, blue components. These component values should be
   * {@code [0..255]}, but there is no range check performed, so if they are out of range, the
   * returned color is undefined.
   *
   * @param alpha Alpha component {@code [0..255]} of the color
   * @param red   Red component {@code [0..255]} of the color
   * @param green Green component {@code [0..255]} of the color
   * @param blue  Blue component {@code [0..255]} of the color
   *
   * @return The color-int defined by the specified {@code red}, {@code green} and {@code blue}
   *         values with the given {@code alpha} transparency
   */
  public static int argb(int alpha, int red, int green, int blue) {
    return (alpha << 24) | (red << 16) | (green << 8) | blue;
  }

  /**
   * Parse the color string, and return the corresponding color-int. Supported formats are:
   * <ul>
   *   <li>{@code #RRGGBB}</li>
   *   <li>{@code #AARRGGBB}</li>
   *   <li>'red', 'blue', 'green', 'black', 'white', 'gray', 'cyan', 'magenta', 'yellow',
   *       'lightgray', 'darkgray', 'grey', 'lightgrey', 'darkgrey', 'aqua', 'fuschia', 'lime',
   *       'maroon', 'navy', 'olive', 'purple', 'silver', 'teal'</li>
   * </ul>
   *
   * @param str The color string to parse
   *
   * @return The corresponding color-int of {@code str}
   *
   * @throws IllegalArgumentException if {@code str} cannot be parsed.
   */
  @ColorInt
  public static int parseColor(@NonNull String str) {
    if (str.charAt(0) == '#') {
      // Use a long to avoid rollovers on #ffXXXXXX
      long color = Long.parseLong(str.substring(1), 16);
      if (str.length() == 7) {
        // Set the alpha value
        color |= 0x00000000ff000000;
      } else if (str.length() != 9) {
        throw new IllegalArgumentException("Unknown color: " + str);
      }

      return (int) color;
    } else {
      Integer color = colorNames.get(str.toLowerCase(Locale.ROOT));
      if (color != null) {
        return color;
      }
    }

    throw new IllegalArgumentException("Unknown color: " + str);
  }

  private static final Map<String, Integer> colorNames = ImmutableMap.<String, Integer>builder()
      .put("clear", CLEAR)
      .put("transparent", TRANSPARENT)
      .put("black", BLACK)
      .put("white", WHITE)
      .put("darkgray", DARK_GRAY)
      .put("darkgrey", DARK_GRAY)
      .put("gray", GRAY)
      .put("grey", GRAY)
      .put("lightgray", LIGHT_GRAY)
      .put("lightgrey", LIGHT_GRAY)
      .put("red", RED)
      .put("green", GREEN)
      .put("blue", BLUE)
      .put("yellow", YELLOW)
      .put("cyan", CYAN)
      .put("magenta", MAGENTA)
      .put("aqua", 0xFF00FFFF)
      .put("fuchsia", 0xFFFF00FF)
      .put("lime", 0xFF00FF00)
      .put("maroon", 0xFF800000)
      .put("navy", 0xFF000080)
      .put("olive", 0xFF808000)
      .put("purple", 0xFF800080)
      .put("silver", 0xFFC0C0C0)
      .put("teal", 0xFF008080)
      .build();

}
