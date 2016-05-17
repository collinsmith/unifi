package com.gmail.collinsmith70.unifi.content.res;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.badlogic.gdx.graphics.Color;
import com.gmail.collinsmith70.unifi.content.res.Resources.Theme;
import com.gmail.collinsmith70.unifi.math.Dimension;

public class TypedArrayTest {

  private final Resources RES = new Resources();

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void testObtain() {
    final int len = 0;
    TypedArray array = TypedArray.obtain(RES, len);
    assertTrue(array != null);
    assertTrue(!array.isRecycled());
    assertTrue(array.getResources() == RES);
    assertTrue(array.getData() != null);
    assertTrue(array.getData().length == len);
    assertTrue(array.getTheme() == null);
    array.recycle();
  }

  @Test
  public void testObtainParams() {
    exception.expect(IllegalArgumentException.class);
    TypedArray.obtain(null, 0);
    TypedArray.obtain(RES, -1);
  }

  @Test
  public void testRecycle() {
    final int len = 0;
    TypedArray array = TypedArray.obtain(RES, len);
    array.recycle();
    assertTrue(array.isRecycled());
    exception.expect(IllegalStateException.class);
    array.getResources();
    array.getData();
    array.getTheme();
  }

  @Test
  public void testSetData() {
    TypedArray array = TypedArray.obtain(RES, 0);
    final Object[] data = new Object[0];
    array.setData(data);
    assertTrue(array.getData() == data);
    exception.expect(IllegalArgumentException.class);
    array.setData(null);
    array.recycle();
  }

  @Test
  public void testSetTheme() {
    TypedArray array = TypedArray.obtain(RES, 0);
    final Theme theme = RES.new Theme();
    array.setTheme(theme);
    assertTrue(array.getTheme() == theme);
    exception.expect(IllegalArgumentException.class);
    array.setTheme(null);
    array.recycle();
  }

  private static final int[] data = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59,
      61, 67, 71, 73, 79, 83, 89, 97};

  @Test
  public void testGetInt() {
    TypedArray array = TypedArray.obtain(RES, 1);
    for (int value : data) {
      array.getData()[0] = value;
      assertTrue(array.getInt(0, -1) == value);
      array.getData()[0] = -value;
      assertTrue(array.getInt(0, -1) == -value);
      array.getData()[0] = Integer.toString(value);
      assertTrue(array.getInt(0, -1) == value);
      array.getData()[0] = Integer.toString(-value);
      assertTrue(array.getInt(0, -1) == -value);
      array.getData()[0] = null;
      assertTrue(array.getInt(0, value) == value);
    }

    array.recycle();
  }

  @Test
  public void testGetFloat() {
    TypedArray array = TypedArray.obtain(RES, 1);
    for (float value : data) {
      array.getData()[0] = value;
      assertTrue(array.getFloat(0, -1) == value);
      array.getData()[0] = -value;
      assertTrue(array.getFloat(0, -1) == -value);
      array.getData()[0] = Float.toString(value);
      assertTrue(array.getFloat(0, -1) == value);
      array.getData()[0] = Float.toString(-value);
      assertTrue(array.getFloat(0, -1) == -value);
      array.getData()[0] = null;
      assertTrue(array.getFloat(0, value) == value);
    }

    array.recycle();
  }

  @Test
  public void testGetString() {
    TypedArray array = TypedArray.obtain(RES, 1);
    String test = "foo";
    array.getData()[0] = test;
    assertTrue(array.getString(0, null) == test);
    test = "bar";
    array.getData()[0] = test;
    assertTrue(array.getString(0, null) == test);
    array.getData()[0] = null;
    assertTrue(array.getString(0, test) == test);
    array.recycle();
  }

  @Test
  public void testGetBoolean() {
    TypedArray array = TypedArray.obtain(RES, 1);
    boolean test = true;
    array.getData()[0] = test;
    assertTrue(array.getBoolean(0, !test) == test);
    test = false;
    array.getData()[0] = test;
    assertTrue(array.getBoolean(0, !test) == test);
    test = true;
    array.getData()[0] = Boolean.toString(test);
    assertTrue(array.getBoolean(0, !test) == test);
    test = false;
    array.getData()[0] = Boolean.toString(test);
    assertTrue(array.getBoolean(0, !test) == test);
    array.getData()[0] = "True";
    assertTrue(array.getBoolean(0, false) == true);
    array.getData()[0] = "False";
    assertTrue(array.getBoolean(0, true) == false);
    array.getData()[0] = null;
    assertTrue(array.getBoolean(0, true) == true);
    array.recycle();
  }

  @Test
  public void testGetColor() {
    TypedArray array = TypedArray.obtain(RES, 1);
    Color color = Color.WHITE;
    array.getData()[0] = color;
    assertTrue(array.getColor(0, null) == color);
    array.getData()[0] = 0xFFFFFFFF;
    assertTrue(array.getColor(0, null).equals(color));
    array.getData()[0] = "#FFFFFFFF";
    assertTrue(array.getColor(0, null).equals(color));
    array.getData()[0] = "white";
    assertTrue(array.getColor(0, null).equals(color));
    array.getData()[0] = null;
    assertTrue(array.getColor(0, color) == color);
    array.recycle();
  }

  @Test
  public void testGetDimension() {
    TypedArray array = TypedArray.obtain(RES, 1);
    Dimension test = new Dimension();
    array.getData()[0] = test;
    assertTrue(array.getDimension(0, null) == test);
    test = new Dimension();
    array.getData()[0] = test;
    assertTrue(array.getDimension(0, null) == test);
    array.getData()[0] = null;
    assertTrue(array.getDimension(0, test) == test);
    array.recycle();
  }

}
