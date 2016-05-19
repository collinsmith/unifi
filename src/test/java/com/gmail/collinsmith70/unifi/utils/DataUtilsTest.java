package com.gmail.collinsmith70.unifi.utils;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gmail.collinsmith70.unifi.util.ColorUtils;
import com.gmail.collinsmith70.unifi.util.DataUtils;

public class DataUtilsTest {

  private static final Object[][] data = {{"0", false}, {"1", true}, {"false", false},
      {"true", true}, {"FALSE", false}, {"TRUE", true}, {"False", false}, {"True", true}};

  private static final int[] data2 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53,
      59, 61, 67, 71, 73, 79, 83, 89, 97};

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void testParseBoolean() {
    boolean test, validator;
    for (Object[] row : data) {
      validator = (Boolean) row[1];
      test = DataUtils.parseBoolean((String) row[0], !validator);
      assertTrue(test == validator);
    }
  }
  
  @Test
  public void testParseBoolean_null() {
    boolean validator = true;
    boolean test = DataUtils.parseBoolean(null, validator);
    assertTrue(test == validator);
  }

  @Test
  public void testParseBoolean_empty() {
    boolean validator = false;
    boolean test = DataUtils.parseBoolean("", !validator);
    assertTrue(test == validator);
  }

  @Test
  public void testParseBoolean_negativeTest() {
    boolean validator = false;
    boolean test = DataUtils.parseBoolean("not_a_boolean", !validator);
    assertTrue(test == validator);
  }

  @Test
  public void testParseInt() {
    int test;
    int negValidator;
    for (int validator : data2) {
      negValidator = -validator;
      test = DataUtils.parseInt(Integer.toString(validator), ~validator);
      assertTrue(test == validator);
      test = DataUtils.parseInt(Integer.toString(negValidator), ~negValidator);
      assertTrue(test == negValidator);
      test = DataUtils.parseInt("0x" + Integer.toString(validator, 16), ~validator);
      assertTrue(test == validator);
      test = DataUtils.parseInt("#" + Integer.toString(validator, 16), ~validator);
      assertTrue(test == validator);
      test = DataUtils.parseInt("0" + Integer.toString(validator, 8), ~validator);
      assertTrue(test == validator);
      test = DataUtils.parseInt("-0x" + Integer.toString(negValidator, 16).substring(1),
          ~negValidator);
      assertTrue(test == negValidator);
      test = DataUtils.parseInt("-#" + Integer.toString(negValidator, 16).substring(1),
          ~negValidator);
      assertTrue(test == negValidator);
      test = DataUtils.parseInt("-0" + Integer.toString(negValidator, 8).substring(1),
          ~negValidator);
      assertTrue(test == negValidator);
    }
  }

  @Test
  public void testParseInt_null() {
    int validator = Integer.MAX_VALUE;
    int test = DataUtils.parseInt(null, validator);
    assertTrue(test == validator);
  }

  @Test
  public void testParseInt_empty() {
    int validator = Integer.MAX_VALUE;
    int test = DataUtils.parseInt("", validator);
    assertTrue(test == validator);
  }
  
  @Test
  public void testParseInt_negativeTest() {
    int validator = Integer.MAX_VALUE;
    int test = DataUtils.parseInt("not_an_int", validator);
    assertTrue(test == validator);
  }
  
  @Test
  public void testParseColor_null() {
    int validator = ColorUtils.WHITE;
    int test = DataUtils.parseColor(null, validator);
    assertTrue(test == validator);
  }

  @Test
  public void testParseColor_empty() {
    int validator = ColorUtils.WHITE;
    int test = DataUtils.parseColor("", validator);
    assertTrue(test == validator);
  }
  
  @Test
  public void testParseColor_negativeTest() {
    int validator = ColorUtils.WHITE;
    int test = DataUtils.parseColor("not_a_color", validator);
    assertTrue(test == validator);
  }
  
  @Test
  public void testParseColor_incorrectColorString() {
    int validator = ColorUtils.WHITE;
    int test = DataUtils.parseColor("black1", validator);
    assertTrue(test == validator);
  }
  
  @Test
  public void testParseColor_hexStringWithoutPrefix() {
    int validator = ColorUtils.WHITE;
    int test = DataUtils.parseColor(
        String.format(Locale.ROOT, "%08X", ColorUtils.BLACK), validator);
    assertTrue(test == validator);
  }

  @Test
  public void testParseColor_hexStringWithInvalid() {
    int validator = ColorUtils.WHITE;
    int test = DataUtils.parseColor(
        String.format(Locale.ROOT, "0x%08X", ColorUtils.BLACK), validator);
    assertTrue(test == validator);
  }
  
  @Test
  public void testParseColor_lengths() {
    int validator = ColorUtils.WHITE;
    int test = DataUtils.parseColor("#0", validator);
    assertTrue(test == validator);
    test = DataUtils.parseColor("#00000", validator);
    assertTrue(test == validator);
    test = DataUtils.parseColor("#0000000", validator);
    assertTrue(test == validator);
    test = DataUtils.parseColor("#000000000", validator);
    assertTrue(test == validator);
    test = DataUtils.parseColor(String.format(Locale.ROOT, "#%X", ColorUtils.CLEAR), validator);
    assertTrue(test == validator);
  }

}
