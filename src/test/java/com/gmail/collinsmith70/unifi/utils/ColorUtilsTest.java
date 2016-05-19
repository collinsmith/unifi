package com.gmail.collinsmith70.unifi.utils;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gmail.collinsmith70.unifi.util.ColorUtils;

public class ColorUtilsTest {

  private static final Object[][] data = {{"clear", ColorUtils.CLEAR},
      {"transparent", ColorUtils.TRANSPARENT}, {"black", ColorUtils.BLACK},
      {"white", ColorUtils.WHITE}, {"darkgray", ColorUtils.DARK_GRAY},
      {"darkgrey", ColorUtils.DARK_GRAY}, {"gray", ColorUtils.GRAY}, {"grey", ColorUtils.GRAY},
      {"lightgray", ColorUtils.LIGHT_GRAY}, {"lightgrey", ColorUtils.LIGHT_GRAY},
      {"red", ColorUtils.RED}, {"green", ColorUtils.GREEN}, {"blue", ColorUtils.BLUE},
      {"yellow", ColorUtils.YELLOW}, {"cyan", ColorUtils.CYAN}, {"magenta", ColorUtils.MAGENTA}};

  @Test
  public void testParseColor() {
    int color;
    int validator;
    for (Object[] row : data) {
      color = ColorUtils.parseColor((String) row[0]);
      validator = (Integer) row[1];
      assertTrue(color == validator);
      color = ColorUtils.parseColor(String.format(Locale.ROOT, "#%08X", validator));
      assertTrue(color == validator);
    }
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParseColor_null() {
    ColorUtils.parseColor(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseColor_empty() {
    ColorUtils.parseColor("");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParseColor_negativeTest() {
    ColorUtils.parseColor("not_a_color");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParseColor_incorrectColorString() {
    ColorUtils.parseColor("black1");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParseColor_hexStringWithoutPrefix() {
    ColorUtils.parseColor(String.format(Locale.ROOT, "%08X", ColorUtils.BLACK));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseColor_hexStringWithInvalid() {
    ColorUtils.parseColor(String.format(Locale.ROOT, "0x%08X", ColorUtils.BLACK));
  }
  
  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void testParseColor_lengths() {
    exception.expect(IllegalArgumentException.class);
    ColorUtils.parseColor("#0");
    exception.expect(IllegalArgumentException.class);
    ColorUtils.parseColor("#00000");
    exception.expect(IllegalArgumentException.class);
    ColorUtils.parseColor("#0000000");
    exception.expect(IllegalArgumentException.class);
    ColorUtils.parseColor("#000000000");
    exception.expect(IllegalArgumentException.class);
    ColorUtils.parseColor(String.format(Locale.ROOT, "#%X", ColorUtils.CLEAR));
  }

}
