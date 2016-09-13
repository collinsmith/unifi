package com.gmail.collinsmith70.unifi.math;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

public class ImmutableDimensionTest {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testImmutableDimension() {
    Dimension dim = ImmutableDimension.newImmutableDimension();
    assertTrue(dim.equals(0, 0));
  }
  
  @Test
  public void testImmutableDimension_IntInt() {
    int width, height;
    ImmutableDimension dim;
    for (int i = 0; i < data.length - 1; i++) {
      width = data[i];
      height = data[i + 1];
      dim = ImmutableDimension.newImmutableDimension(width, height);
      assertTrue(dim.equals(width, height));
    }
  }
  
  @Test
  public void testImmutableDimension_Dimension() {
    int width, height;
    Dimension src;
    ImmutableDimension dim;
    for (int i = 0; i < data.length - 1; i++) {
      width = data[i];
      height = data[i + 1];
      src = new Dimension(width, height);
      dim = ImmutableDimension.copyOf(src);
      assertTrue(dim.equals(src));
    }
  }

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void testSetWidth() {
    exception.expect(UnsupportedOperationException.class);
    Dimension dim = ImmutableDimension.ZERO;
    for (int testCase : data) {
      dim.setWidth(testCase);
      assertTrue(dim.getWidth() == testCase);
    }
  }

  @Test
  public void testSetHeight() {
    exception.expect(UnsupportedOperationException.class);
    Dimension dim = ImmutableDimension.ZERO;
    for (int testCase : data) {
      dim.setHeight(testCase);
      assertTrue(dim.getHeight() == testCase);
    }
  }

}