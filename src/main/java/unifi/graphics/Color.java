package unifi.graphics;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.collections4.trie.UnmodifiableTrie;
import org.apache.commons.lang.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.signedness.qual.Unsigned;

import java.util.Locale;

public final class Color {

  private Color() {}

  private static final int ALPHA_OFFSET = 24;
  private static final int RED_OFFSET = 16;
  private static final int GREEN_OFFSET = 8;
  private static final int BLUE_OFFSET = 0;

  @Unsigned
  private static final int ALPHA_MASK = 0xFF << ALPHA_OFFSET;

  @Unsigned
  private static final int RED_MASK = 0xFF << RED_OFFSET;

  @Unsigned
  private static final int GREEN_MASK = 0xFF << GREEN_OFFSET;

  @Unsigned
  private static final int BLUE_MASK = 0xFF << BLUE_OFFSET;

  @Unsigned
  private static final int CLEAR = 0xFF000000;

  @Unsigned
  private static final int DARK_GRAY = 0xFF444444;

  @Unsigned
  private static final int GRAY = 0xFF888888;

  @Unsigned
  private static final int LIGHT_GRAY = 0xFFCCCCCC;

  private static final Trie<String, Integer> colorNames;
  static {
    PatriciaTrie<Integer> names = new PatriciaTrie<Integer>();
    names.put("black", 0xFF000000);
    names.put("white", 0xFFFFFFFF);
    names.put("darkgray", DARK_GRAY);
    names.put("darkgrey", DARK_GRAY);
    names.put("gray", GRAY);
    names.put("grey", GRAY);
    names.put("lightgray", LIGHT_GRAY);
    names.put("lightgrey", LIGHT_GRAY);
    names.put("red", 0xFFFF0000);
    names.put("green", 0xFF00FF00);
    names.put("blue", 0xFF0000FF);
    names.put("yellow", 0xFFFFFF00);
    names.put("cyan", 0xFF00FFFF);
    names.put("magenta", 0xFFFF00FF);
    names.put("aqua", 0xFF00FFFF);
    names.put("fuchsia", 0xFFFF00FF);
    names.put("lime", 0xFF00FF00);
    names.put("maroon", 0xFF800000);
    names.put("navy", 0xFF000080);
    names.put("olive", 0xFF808000);
    names.put("purple", 0xFF800080);
    names.put("silver", 0xFFC0C0C0);
    names.put("teal", 0xFF008080);
    colorNames = UnmodifiableTrie.unmodifiableTrie(names);
  }

  @Unsigned
  public static int parseColor(@NonNull String str) {
    Validate.isTrue(str != null, "str cannot be null");
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

  @Unsigned
  public static int rgb(int r, int g, int b) {
    return argb(0xFF, r, g, b);
  }

  @Unsigned
  public static int argb(int a, int r, int g, int b) {
    Validate.isTrue(0 <= a && a < 256, "a (%d) must be between 0 and 255 (inclusive)", a);
    Validate.isTrue(0 <= r && r < 256, "r (%d) must be between 0 and 255 (inclusive)", r);
    Validate.isTrue(0 <= g && g < 256, "g (%d) must be between 0 and 255 (inclusive)", g);
    Validate.isTrue(0 <= b && b < 256, "b (%d) must be between 0 and 255 (inclusive)", b);
    return a << ALPHA_OFFSET | r << RED_OFFSET | g << GREEN_OFFSET | b << BLUE_OFFSET;
  }

  public static int alpha(@Unsigned int color) {
    return (color & ALPHA_MASK) >>> ALPHA_OFFSET;
  }

  public static int red(@Unsigned int color) {
    return (color & RED_MASK) >>> RED_OFFSET;
  }

  public static int green(@Unsigned int color) {
    return (color & GREEN_MASK) >>> GREEN_OFFSET;
  }

  public static int blue(@Unsigned int color) {
    return (color & BLUE_MASK) >>> BLUE_OFFSET;
  }

  // TODO: Replace with org.checkerframework annotations, however not working with qualified
  @android.annotation.NonNull
  public static com.badlogic.gdx.graphics.Color toGdxColor(@Unsigned int color) {
    return toGdxColor(color, new com.badlogic.gdx.graphics.Color());
  }

  // TODO: Replace with org.checkerframework annotations, however not working with qualified
  @android.annotation.NonNull
  public static com.badlogic.gdx.graphics.Color toGdxColor(@Unsigned int color,
      // TODO: Replace with org.checkerframework annotations, however not working with qualified
      @android.annotation.NonNull com.badlogic.gdx.graphics.Color dst) {
    Validate.isTrue(dst != null, "dst Color cannot be null");
    com.badlogic.gdx.graphics.Color.argb8888ToColor(dst, color);
    return new com.badlogic.gdx.graphics.Color(dst);
  }

  public static boolean equalsGdxColor(@Unsigned int color,
      // TODO: Replace with org.checkerframework annotations, however not working with qualified
      @android.annotation.Nullable com.badlogic.gdx.graphics.Color c) {
    // TODO: Check if cleaner method. Unfortunately LibGDX depends on direct field access to get
    //       the color components, so this method should be more interoperable.
    com.badlogic.gdx.graphics.Color asGdxColor = toGdxColor(color);
    return asGdxColor.equals(c);
  }

  @NonNull
  public static String toHexString(@Unsigned int color) {
    return String.format("#%08x", color);
  }

}
