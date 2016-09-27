package com.gmail.collinsmith70.unifi.graphics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gmail.collinsmith70.unifi.annotation.Unsigned;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.util.Locale;

public class Color {

  private static final Trie<String, Integer> colorNames;

  private static final int DARK_GRAY = 0xFF444444;
  private static final int GRAY = 0xFF888888;
  private static final int LIGHT_GRAY = 0xFFCCCCCC;

  static {
    colorNames = new PatriciaTrie<Integer>();
    colorNames.put("black", 0xFF000000);
    colorNames.put("white", 0xFFFFFFFF);
    colorNames.put("darkgray", DARK_GRAY);
    colorNames.put("darkgrey", DARK_GRAY);
    colorNames.put("gray", GRAY);
    colorNames.put("grey", GRAY);
    colorNames.put("lightgray", LIGHT_GRAY);
    colorNames.put("lightgrey", LIGHT_GRAY);
    colorNames.put("red", 0xFFFF0000);
    colorNames.put("green", 0xFF00FF00);
    colorNames.put("blue", 0xFF0000FF);
    colorNames.put("yellow", 0xFFFFFF00);
    colorNames.put("cyan", 0xFF00FFFF);
    colorNames.put("magenta", 0xFFFF00FF);
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

  @Unsigned
  private final int argb8888;

  public Color() {
    this(0);
  }

  public Color(@Unsigned int argb8888) {
    this.argb8888 = argb8888;
  }

  @NonNull
  public com.badlogic.gdx.graphics.Color getGdxColor() {
    return getGdxColor(new com.badlogic.gdx.graphics.Color());
  }

  @NonNull
  public com.badlogic.gdx.graphics.Color getGdxColor(@Nullable com.badlogic.gdx.graphics.Color color) {
    if (color == null) {
      color = new com.badlogic.gdx.graphics.Color();
    }

    com.badlogic.gdx.graphics.Color.argb8888ToColor(color, argb8888);
    return new com.badlogic.gdx.graphics.Color(color);
  }

  public static int parseColor(String str) {
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

}
