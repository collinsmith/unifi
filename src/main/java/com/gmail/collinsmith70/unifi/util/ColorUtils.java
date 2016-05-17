package com.gmail.collinsmith70.unifi.util;

import java.util.Locale;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.lang3.Validate;

import com.badlogic.gdx.graphics.Color;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ColorUtils {

  @Nullable
  private static Color tmp = new Color();

  private ColorUtils() {}

  @NonNull
  public static Color parseColor(@NonNull String colorString) {
    Validate.isTrue(colorString != null, "colorString cannot be null");
    if (colorString.charAt(0) == '#') {
      // Use a long to avoid rollovers on #ffXXXXXX
      long color = Long.parseLong(colorString.substring(1), 16);
      if (colorString.length() == 7) {
        // Set the alpha value
        color |= 0x00000000ff000000;
      } else if (colorString.length() != 9) {
        throw new IllegalArgumentException("Unknown color: " + colorString);
      }

      Color.argb8888ToColor(tmp, (int) color);
      return new Color(tmp);
    } else {
      Integer color = colorNames.get(colorString.toLowerCase(Locale.ROOT));
      if (color != null) {
        Color.argb8888ToColor(tmp, color);
        return new Color(tmp);
      }
    }

    throw new IllegalArgumentException("Unknown color: " + colorString);
  }

  private static final Trie<String, Integer> colorNames;

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

  static {
    colorNames = new PatriciaTrie<Integer>();
    colorNames.put("clear", CLEAR);
    colorNames.put("transparent", TRANSPARENT);
    colorNames.put("black", BLACK);
    colorNames.put("white", WHITE);
    colorNames.put("darkgray", DARK_GRAY);
    colorNames.put("darkgrey", DARK_GRAY);
    colorNames.put("gray", GRAY);
    colorNames.put("grey", GRAY);
    colorNames.put("lightgray", LIGHT_GRAY);
    colorNames.put("lightgrey", LIGHT_GRAY);
    colorNames.put("red", RED);
    colorNames.put("green", GREEN);
    colorNames.put("blue", BLUE);
    colorNames.put("yellow", YELLOW);
    colorNames.put("cyan", CYAN);
    colorNames.put("magenta", MAGENTA);
    colorNames.put("aqua", 0xFF00FFFF);
    colorNames.put("fuchsia", 0xFFFF00FF);
    colorNames.put("lime", 0xFF00FF00);
    colorNames.put("maroon", 0xFF800000);
    colorNames.put("navy", 0xFF000080);
    colorNames.put("olive", 0xFF808000);
    colorNames.put("purple", 0xFF800080);
    colorNames.put("silver", 0xFFC0C0C0);
    colorNames.put("teal", 0xFF008080);
  }

}
