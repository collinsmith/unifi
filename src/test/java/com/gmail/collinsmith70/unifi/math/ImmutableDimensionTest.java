package com.gmail.collinsmith70.unifi.math;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ImmutableDimensionTest {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testImmutableDimension() {
    Dimension dim = ImmutableDimension.newImmutableDimension();
    assertTrue(dim.getWidth() == 0);
    assertTrue(dim.getHeight() == 0);
  }
  
  @Test
  public void testImmutableDimension_IntInt() {
    int width, height;
    ImmutableDimension dim;
    for (int i = 0; i < data.length - 1; i++) {
      width = data[i];
      height = data[i + 1];
      dim = ImmutableDimension.newImmutableDimension(width, height);
      assertTrue(dim.getWidth() == width);
      assertTrue(dim.getHeight() == height);
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
      assertTrue(dim.getWidth() == src.getWidth());
      assertTrue(dim.getHeight() == src.getHeight());
    }
  }

  @Test
  public void testSetWidth() {
    Dimension dim = ImmutableDimension.ZERO;
    for (int testCase : data) {
      try {
        dim.setWidth(testCase);
        assertTrue(dim.getWidth() == testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
      }
    }
  }

  @Test
  public void testSetHeight() {
    Dimension dim = ImmutableDimension.ZERO;
    for (int testCase : data) {
      try {
        dim.setHeight(testCase);
        assertTrue(dim.getHeight() == testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
      }
    }
  }

}