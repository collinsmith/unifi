package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class ColorTests {

  private static final boolean output = true;

  private static final int WHITE = 0xFFFFFFFF;
  private static final int RED   = 0xFFFF0000;
  private static final int GREEN = 0xFF00FF00;
  private static final int BLUE  = 0xFF0000FF;
  private static final int CLEAR = 0x00000000;

  @RunWith(Enclosed.class)
  public static class Constructors {

    @RunWith(Enclosed.class)
    public static class Color_ {

      public static class positive_tests {

        @Test
        public void non_instantiability()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
                   InstantiationException {
          Constructor<Color> constructor = Color.class.getDeclaredConstructor();
          Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
          constructor.setAccessible(true);
          constructor.newInstance();
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class rgb {

    public static class negative_tests {

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_red() {
        final int color = Color.rgb(-1, 0, 0);
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_green() {
        final int color = Color.rgb(0, -1, 0);
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_blue() {
        final int color = Color.rgb(0, 0, -1);
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

    }

    public static class positive_tests {

      @Test
      public void primes() {
        final int color = Color.rgb(PRIMES[0], PRIMES[1], PRIMES[2]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(0xFF, Color.alpha(color));
        Assert.assertEquals(PRIMES[0], Color.red(color));
        Assert.assertEquals(PRIMES[1], Color.green(color));
        Assert.assertEquals(PRIMES[2], Color.blue(color));
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class argb {

    public static class negative_tests {

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_alpha() {
        final int color = Color.argb(-1, 0, 0, 0);
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_red() {
        final int color = Color.argb(0, -1, 0, 0);
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_green() {
        final int color = Color.argb(0, 0, -1, 0);
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_blue() {
        final int color = Color.argb(0, 0, 0, -1);
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

    }

    public static class positive_tests {

      @Test
      public void primes() {
        final int color = Color.argb(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(PRIMES[0], Color.alpha(color));
        Assert.assertEquals(PRIMES[1], Color.red(color));
        Assert.assertEquals(PRIMES[2], Color.green(color));
        Assert.assertEquals(PRIMES[3], Color.blue(color));
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class toGdxColor {

    @RunWith(Enclosed.class)
    public static class toGdxColor_uint {

      public static class positive_tests {

        @Test
        public void equality() {
          final int color = WHITE;
          if (output) System.out.println("color:" + Color.toHexString(color));
          com.badlogic.gdx.graphics.Color c = Color.toGdxColor(color);
          if (output) System.out.println("c:" + c);
          com.badlogic.gdx.graphics.Color expected = new com.badlogic.gdx.graphics.Color();
          com.badlogic.gdx.graphics.Color.argb8888ToColor(expected, color);
          if (output) System.out.println("expected:" + expected);
          Assert.assertEquals(expected, c);
          Assert.assertTrue(Color.equalsGdxColor(color, expected));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class toGdxColor_uint_GdxColor {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          final int color = WHITE;
          com.badlogic.gdx.graphics.Color c = Color.toGdxColor(color, null);
          if (output) System.out.println("c:" + c);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          final int color = WHITE;
          com.badlogic.gdx.graphics.Color dst = new com.badlogic.gdx.graphics.Color();
          if (output) System.out.println("dst:" + dst);
          com.badlogic.gdx.graphics.Color c = Color.toGdxColor(color);
          if (output) System.out.println("c:" + c);
          com.badlogic.gdx.graphics.Color.argb8888ToColor(dst, color);
          Assert.assertEquals(dst, c);
          Assert.assertTrue(Color.equalsGdxColor(color, c));
        }
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class parseColor {

    public static class negative_tests {

      @Test(expected = IllegalArgumentException.class)
      public void fails_null() {
        final int color = Color.parseColor(null);
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_name() {
        final int color = Color.parseColor("invalid_name");
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_hex_5F() {
        final int color = Color.parseColor("#FFFFF");
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_hex_7F() {
        final int color = Color.parseColor("#FFFFFFF");
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_hex_9F() {
        final int color = Color.parseColor("#FFFFFFFFF");
        if (output) System.out.println("color:" + Color.toHexString(color));
      }

    }

    public static class positive_tests {

      @Test
      public void colorName_white() {
        final int color = Color.parseColor("white");
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(WHITE, color);
      }

      @Test
      public void colorName_white_in_hex() {
        final int color = Color.parseColor(Color.toHexString(WHITE));
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(WHITE, color);
      }

      @Test
      public void colorName_red_in_hex() {
        final int color = Color.parseColor(Color.toHexString(RED));
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(RED, color);
      }

      @Test
      public void colorName_green_in_hex() {
        final int color = Color.parseColor(Color.toHexString(GREEN));
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(GREEN, color);
      }

      @Test
      public void colorName_blue_in_hex() {
        final int color = Color.parseColor(Color.toHexString(BLUE));
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(BLUE, color);
      }

      @Test
      public void colorName_clear_in_hex() {
        final int color = Color.parseColor(Color.toHexString(CLEAR));
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(CLEAR, color);
      }

      @Test
      public void colorName_white_in_hex_6F() {
        final int color = Color.parseColor("#FFFFFF");
        if (output) System.out.println("color:" + Color.toHexString(color));
        Assert.assertEquals(WHITE, color);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class toHexString {

    public static class positive_tests {

      @Test
      public void nullity() {
        final int color = CLEAR;
        String toString = Color.toHexString(color);
        if (output) System.out.println("color:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

    }

  }

}
