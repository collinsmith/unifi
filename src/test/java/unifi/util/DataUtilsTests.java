package unifi.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import unifi.graphics.Color;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class DataUtilsTests {

  private static final boolean output = true;

  @RunWith(Enclosed.class)
  public static class parseBoolean {

    public static class negative_tests {

      @Test
      public void null_string_true() {
        boolean expected = true;
        boolean parsed = DataUtils.parseBoolean(null, expected);
        Assert.assertEquals(expected, parsed);
      }

      @Test
      public void null_string_false() {
        boolean expected = true;
        boolean parsed = DataUtils.parseBoolean(null, expected);
        Assert.assertEquals(expected, parsed);
      }

      @Test
      public void string_is_empty() {
        boolean expected = false;
        boolean parsed = DataUtils.parseBoolean("", expected);
        Assert.assertEquals(expected, parsed);
      }

      @Test
      public void string_is_2() {
        boolean parsed = DataUtils.parseBoolean("2", true);
        Assert.assertFalse(parsed);
      }

    }

    public static class positive_tests {

      @Test
      public void string_is_0() {
        boolean parsed = DataUtils.parseBoolean("0", true);
        Assert.assertFalse(parsed);
      }

      @Test
      public void string_is_1() {
        boolean parsed = DataUtils.parseBoolean("1", false);
        Assert.assertTrue(parsed);
      }

      @Test
      public void string_is_true() {
        boolean parsed = DataUtils.parseBoolean("true", false);
        Assert.assertTrue(parsed);
      }

      @Test
      public void string_is_false() {
        boolean parsed = DataUtils.parseBoolean("false", true);
        Assert.assertFalse(parsed);
      }

      @Test
      public void string_is_TRue() {
        boolean parsed = DataUtils.parseBoolean("TRue", false);
        Assert.assertTrue(parsed);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class parseInteger {

    public static class negative_tests {

      @Test
      public void string_is_null() {
        final int expected = PRIMES[0];
        int parsed = DataUtils.parseInt(null, expected);
        Assert.assertEquals(expected, parsed);
      }

      @Test
      public void string_is_empty() {
        final int expected = PRIMES[0];
        int parsed = DataUtils.parseInt("", expected);
        Assert.assertEquals(expected, parsed);
      }

    }

    public static class positive_tests {

      @Test
      public void integer_base_10() {
        final int expected = PRIMES[0];
        int parsed = DataUtils.parseInt(Integer.toString(expected), PRIMES[1]);
        Assert.assertEquals(expected, parsed);
      }

      @Test
      public void integer_base_8() {
        final int expected = PRIMES[9];
        int parsed = DataUtils.parseInt("035", PRIMES[1]);
        Assert.assertEquals(expected, parsed);
      }

      @Test
      public void integer_base_16() {
        final int expected = PRIMES[9];
        int parsed = DataUtils.parseInt("0x1D", PRIMES[1]);
        Assert.assertEquals(expected, parsed);
      }

      @Test
      public void integer_base_16_hash() {
        final int expected = PRIMES[9];
        int parsed = DataUtils.parseInt("#1D", PRIMES[1]);
        Assert.assertEquals(expected, parsed);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class parseColor {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int RED = 0xFFFF0000;
    private static final int GREEN = 0xFF00FF00;
    private static final int BLUE = 0xFF0000FF;
    private static final int CLEAR = 0x00000000;

    public static class negative_tests {

      @Test
      public void string_is_null() {
        final int expected = PRIMES[0];
        final int color = DataUtils.parseColor(null, expected);
        Assert.assertEquals(expected, color);
      }

      @Test
      public void string_is_empty() {
        final int expected = PRIMES[0];
        final int color = DataUtils.parseColor("", expected);
        Assert.assertEquals(expected, color);
      }

      @Test
      public void string_is_invalid() {
        final int expected = PRIMES[0];
        final int color = DataUtils.parseColor("#0", expected);
        Assert.assertEquals(expected, color);
      }

      @Test
      public void invalid_name() {
        final int expected = PRIMES[0];
        final int color = DataUtils.parseColor("invalid_name", expected);
        Assert.assertEquals(expected, color);
      }

      @Test
      public void invalid_hex_5F() {
        final int expected = PRIMES[0];
        final int color = DataUtils.parseColor("#FFFFF", expected);
        Assert.assertEquals(expected, color);
      }

      @Test
      public void invalid_hex_7F() {
        final int expected = PRIMES[0];
        final int color = DataUtils.parseColor("#FFFFFFF", expected);
        Assert.assertEquals(expected, color);
      }

      @Test
      public void invalid_hex_9F() {
        final int expected = PRIMES[0];
        final int color = DataUtils.parseColor("#FFFFFFFFF", expected);
        Assert.assertEquals(expected, color);
      }

    }

    public static class positive_tests {

      @Test
      public void colorName_white() {
        final int color = DataUtils.parseColor("white", PRIMES[0]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(WHITE, color);
      }

      @Test
      public void colorName_white_in_hex() {
        final int color = DataUtils.parseColor(Color.toHexString(WHITE), PRIMES[0]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(WHITE, color);
      }

      @Test
      public void colorName_red_in_hex() {
        final int color = DataUtils.parseColor(Color.toHexString(RED), PRIMES[0]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(RED, color);
      }

      @Test
      public void colorName_green_in_hex() {
        final int color = DataUtils.parseColor(Color.toHexString(GREEN), PRIMES[0]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(GREEN, color);
      }

      @Test
      public void colorName_blue_in_hex() {
        final int color = DataUtils.parseColor(Color.toHexString(BLUE), PRIMES[0]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(BLUE, color);
      }

      @Test
      public void colorName_clear_in_hex() {
        final int color = DataUtils.parseColor(Color.toHexString(CLEAR), PRIMES[0]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(CLEAR, color);
      }

      @Test
      public void colorName_white_in_hex_6F() {
        final int color = DataUtils.parseColor("#FFFFFF", PRIMES[0]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(WHITE, color);
      }

    }

  }

}
