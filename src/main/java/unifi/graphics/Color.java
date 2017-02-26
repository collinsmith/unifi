package unifi.graphics;

import com.google.common.collect.ImmutableMap;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.Map;

/**
 * The Color class defines methods for creating and converting color ints. Colors
 * are represented as packed ints, made up of 4 bytes: alpha, red, green, blue.
 * The values are unpremultiplied, meaning any transparency is stored solely in
 * the alpha component, and not in the color components. The components are stored
 * as follows {@code (alpha << 24) | (red << 16) | (green << 8) | blue}. Each
 * component ranges between {@code [0..255]} with {@code 0} meaning no contribution
 * for that component, and {@code 255} meaning 100% contribution. Thus opaque-black
 * would be {@code 0xFF000000} (100% opaque but no contributions from red, green,
 * or blue), and opaque-white would be {@code 0xFFFFFFFF}
 */
public final class Color {

  private Color() {}

  // Note: ALPHA_OFFSET and BLUE_OFFSET are optimized, if changed, make sure to
  //       change their impls.
  //
  //       Also, all of the below color definitions are defined in ARGB, so they
  //       will need to be changed as well (or a static method to convert at
  //       runtime)
  private static final int ALPHA_OFFSET = 24;
  private static final int RED_OFFSET = 16;
  private static final int GREEN_OFFSET = 8;
  private static final int BLUE_OFFSET = 0;

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
  public static int alpha(@ColorInt int color) {
    return color >>> ALPHA_OFFSET;
  }

  /**
   * Returns the red component of a color int.
   * This is the same as saying {@code (color >> 16) & 0xFF}
   */
  public static int red(@ColorInt int color) {
    return (color >> RED_OFFSET) & 0xFF;
  }

  /**
   * Returns the green component of a color int.
   * This is the same as saying {@code (color >> 8) & 0xFF}
   */
  public static int green(@ColorInt int color) {
    return (color >> GREEN_OFFSET) & 0xFF;
  }

  /**
   * Returns the blue component of a color int.
   * This is the same as saying {@code color & 0xFF}
   */
  public static int blue(@ColorInt int color) {
    return color & 0xFF;
  }

  /**
   * Returns a color-int from the specified red, green, and blue components. The
   * alpha component is implicitly {@code 255} (fully opaque). These component
   * values should be {@code [0..255]}, but there is no range check performed,
   * so if they are out of range, the returned color is undefined.
   *
   * @param red   Red component {@code [0..255]} of the color
   * @param green Green component {@code [0..255]} of the color
   * @param blue  Blue component {@code [0..255]} of the color
   *
   * @return The color-int defined by the specified {@code red}, {@code green}
   *         and {@code blue} values
   */
  @ColorInt
  public static int rgb(int red, int green, int blue) {
    return (0xFF << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
  }

  /**
   * Return a color-int from alpha, red, green, blue components. These component
   * values should be {@code [0..255]}, but there is no range check performed,
   * so if they are out of range, the returned color is undefined.
   *
   * @param alpha Alpha component {@code [0..255]} of the color
   * @param red   Red component {@code [0..255]} of the color
   * @param green Green component {@code [0..255]} of the color
   * @param blue  Blue component {@code [0..255]} of the color
   *
   * @return The color-int defined by the specified {@code red}, {@code green}
   *         and {@code blue} values with the given {@code alpha} transparency
   */
  @ColorInt
  public static int argb(int alpha, int red, int green, int blue) {
    return (alpha << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
  }

  /**
   * Converts the packed color-int from the default format into {@code rgba} by
   * shifting the values.
   *
   * @param color The color-int to convert
   *
   * @return A color-int encoded as {@code rgba}
   */
  public static int rgba(@ColorInt int color) {
    return (color << 8) | (color >>> ALPHA_OFFSET);
  }

  /**
   * Converts the packed color-int from the default format into {@code abgr} by
   * shifting the values.
   *
   * @param color The color-int to convert
   *
   * @return A color-int encoded as {@code abgr}
   */
  public static int abgr(@ColorInt int color) {
    return (color & 0xFF00FF00) | ((color & 0xFF) << 16) | ((color & 0xFF0000) >> 16);
  }

  /**
   * Parse the color string, and return the corresponding color-int. Supported
   * formats are:
   * <ul>
   *   <li>{@code #RRGGBB}
   *
   *   <li>{@code #AARRGGBB}
   *
   *   <li>'red', 'blue', 'green', 'black', 'white', 'gray', 'cyan', 'magenta',
   *   'yellow', 'lightgray', 'darkgray', 'grey', 'lightgrey', 'darkgrey',
   *   'aqua', 'fuschia', 'lime', 'maroon', 'navy', 'olive', 'purple', 'silver',
   *   'teal'
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

  private static final Map<String, Integer> colorNames
      = ImmutableMap.<String, Integer>builder()
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
