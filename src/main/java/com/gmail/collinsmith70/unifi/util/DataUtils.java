package com.gmail.collinsmith70.unifi.util;

import com.gmail.collinsmith70.unifi.annotation.Unsigned;

import android.support.annotation.Nullable;

/**
 * Class containing static utility methods which can be used when parsing data.
 */
public class DataUtils {

  /**
   * Enforces the uninstantiability of {@link DataUtils}.
   */
  private DataUtils() {
  }

  /**
   * Attempts to parse the specified {@code String} as a boolean value, returning
   * {@code defaultValue} in the case which the passed {@code String} is {@code null}.
   * 
   * A {@code String} is considered equal to boolean {@code true} if it matches:
   * <ul>
   *    <li>{@code 1}</li>
   *    <li>{@code true} (case-insensitive)</li>
   * </ul>
   * 
   * @param str          {@code String} to parse
   * @param defaultValue Value to return in the case which the passed {@code String} is {@code null}
   * 
   * @return {@code true} if the passed {@code String} is equal to logical {@code true}, otherwise
   *         {@code false}
   */
  public static final boolean parseBoolean(@Nullable String str, boolean defaultValue) {
    if (str == null) {
      return defaultValue;
    }

    if (str.equals("1")) {
      return true;
    }

    return str.toString().equalsIgnoreCase("true");
  }

  /**
   * Attempts to parse the specified {@code String} as an integer value, returning
   * {@code defaultValue} in the cases which the passed {@code String} is {@code null} or empty.
   * The {@code String} will be parsed in a similar manner to that of
   * {@link Integer#decode(String)}.
   * 
   * @param str          {@code String} to parse
   * @param defaultValue Value to return in the case which the passed {@code String} is {@code null}
   *                         or empty.
   * 
   * @return Decoded integer value of the {@code String}.
   * 
   * @see Integer#decode(String)
   */
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
  
  /**
   * Attempts to parse the specified {@code String} as an encoded {@code ARGB8888} color, returning
   * {@code defaultValue} in the cases which the passed {@code String} is {@code null} or not a
   * valid parseable color.
   * 
   * @param str          {@code String} to parse
   * @param defaultValue Value to return in the cases which the passed {@code String} is
   *                         {@code null} or not a valid parseable color.
   * 
   * @return Decoded unsigned integer representation of the passed {@code String} encoded in
   *             {@code ARGB8888} format
   * 
   * {@see ColorUtils#parseColor}
   */
  @Nullable
  @Unsigned
  public static final int parseColor(@Nullable String str, @Unsigned int defaultValue) {
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
