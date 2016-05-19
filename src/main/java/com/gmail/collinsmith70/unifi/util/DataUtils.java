package com.gmail.collinsmith70.unifi.util;

import android.support.annotation.Nullable;

public class DataUtils {

  private DataUtils() {}

  public static final boolean parseBoolean(@Nullable String str, boolean defaultValue) {
    if (str == null) {
      return defaultValue;
    }

    if (str.equals("1")) {
      return true;
    }

    return str.toString().equalsIgnoreCase("true");
  }

  public static final int parseInt(@Nullable String str, int defaultValue) {
    if (str == null) {
      return defaultValue;
    } else if (str.isEmpty()) {
      return defaultValue;
    }

    String nm = str.toString();

    int sign = 1;
    int index = 0;
    int len = nm.length();
    int base = 10;

    if ('-' == nm.charAt(0)) {
      sign = -1;
      index++;
    }

    if ('0' == nm.charAt(index)) {
      if (index == (len - 1))
        return 0;

      char c = nm.charAt(index + 1);

      if ('x' == c || 'X' == c) {
        index += 2;
        base = 16;
      } else {
        index++;
        base = 8;
      }
    } else if ('#' == nm.charAt(index)) {
      index++;
      base = 16;
    }

    try {
      return Integer.parseInt(nm.substring(index), base) * sign;
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }
  
  @Nullable
  public static final int parseColor(@Nullable String str, @Nullable int defaultValue) {
    if (str == null) {
      return defaultValue;
    }
    
    try {
      return ColorUtils.parseColor(str);
    } catch (IllegalArgumentException e) {
      return defaultValue;
    }
  }

}
