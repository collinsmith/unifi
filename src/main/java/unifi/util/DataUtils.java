package unifi.util;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.signedness.qual.Unsigned;

import unifi.graphics.Color;

public final class DataUtils {

  private DataUtils() {}

  /**
   * Attempts to parse the specified {@code String} as a {@code boolean} value, returning
   * {@code defaultValue} in the case in which the passed {@code String} is {@code null}.
   *
   * A {@code String} is considered equal to boolean {@code true} if it matches:
   * <ul>
   * <li>{@code 1}</li>
   * <li>{@code true} (case-insensitive)</li>
   * </ul>
   *
   * @param str          {@code String} to parse
   * @param defaultValue value to return in the case which {@code str} is {@code null}
   *
   * @return {@code true} if {@code str} is equal to logical {@code true}, otherwise {@code false}
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
   * Attempts to parse the specified {@code String} as an {@code int} value, returning
   * {@code defaultValue} in the cases which the passed {@code String} is {@code null} or empty.
   * {@code str} will be parsed in a similar manner to that of {@link Integer#decode(String)}.
   *
   * @param str          {@code String} to parse
   * @param defaultValue value to return in the case which {@code str} is {@code null} or empty
   *
   * @return Decoded {@code int} value of {@code str}.
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
   * valid {@link Color parseable color}.
   *
   * @param str          {@code String} to parse
   * @param defaultValue value to return in the cases which {@code str} is {@code null} or not a
   *                     valid parseable color.
   *
   * @return Decoded unsigned {@code int} representation of {@code str} encoded in {@code ARGB8888}
   *         format
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
      return Color.parseColor(str);
    } catch (IllegalArgumentException e) {
      return defaultValue;
    }
  }

}
