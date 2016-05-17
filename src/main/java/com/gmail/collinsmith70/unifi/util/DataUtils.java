package com.gmail.collinsmith70.unifi.util;

import android.support.annotation.Nullable;

public class DataUtils {

  private DataUtils() {}

  public static final boolean convertValueToBoolean(@Nullable CharSequence charSeq,
                                                    boolean defaultValue) {
    if (charSeq == null) {
      return defaultValue;
    }

    if (charSeq.equals("1")) {
      return true;
    }

    return charSeq.toString().equalsIgnoreCase("true");
  }

  public static final int convertValueToInt(@Nullable CharSequence charSeq,
                                            int defaultValue) {
    if (charSeq == null) {
      return defaultValue;
    }

    String nm = charSeq.toString();

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

    return Integer.parseInt(nm.substring(index), base) * sign;
  }

}
