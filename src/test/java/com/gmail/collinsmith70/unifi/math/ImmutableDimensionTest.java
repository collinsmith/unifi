package com.gmail.collinsmith70.unifi.math;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
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
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

  @Test
  public void testSetHeight() {
    Dimension dim = ImmutableDimension.ZERO;
    for (int testCase : data) {
      try {
        dim.setHeight(testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

  @Test
  public void testSet_IntInt() {
    Dimension dim = ImmutableDimension.ZERO;
    int width, height;
    for (int i = 0; i < data.length - 1; i++) {
      try {
        width = data[i];
        height = data[i + 1];
        dim.set(width, height);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

  @Test
  public void testSet_Dimension() {
    Dimension src;
    Dimension dim = ImmutableDimension.ZERO;
    int width, height;
    for (int i = 0; i < data.length - 1; i++) {
      try {
        width = data[i];
        height = data[i + 1];
        src = new Dimension(width, height);
        dim.set(src);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

  @Test
  public void testEquals_IntInt() {
    int width, height;
    for (int i = 0; i < data.length - 1; i++) {
      width = data[i];
      height = data[i + 1];
      assertTrue(ImmutableDimension.newImmutableDimension(width, height).equals(width, height));
    }
  }

  @Test
  public void testEquals_Object() {
    Dimension dim1 = new Dimension(0, 0);
    Dimension dim2 = new Dimension(1, 1);
    Dimension dim11 = ImmutableDimension.newImmutableDimension(0, 0);
    Dimension dim22 = ImmutableDimension.newImmutableDimension(1, 1);

    assertTrue(dim1.equals(dim1));
    assertTrue(dim2.equals(dim2));
    assertFalse(dim1.equals(dim2));
    assertFalse(dim2.equals(dim1));
    assertFalse(dim1.equals(null));
    assertFalse(dim2.equals(null));

    assertTrue(dim11.equals(dim11));
    assertTrue(dim1.equals(dim11));
    assertTrue(dim11.equals(dim1));
    assertTrue(dim22.equals(dim22));
    assertTrue(dim2.equals(dim22));
    assertTrue(dim22.equals(dim2));
    assertFalse(dim1.equals(dim22));
    assertFalse(dim2.equals(dim11));
    assertFalse(dim11.equals(null));
    assertFalse(dim22.equals(null));
  }

  @Test
  public void testHashCode() {
    Dimension dim1 = new Dimension(0, 0);
    Dimension dim2 = new Dimension(1, 1);
    Dimension dim3 = new Dimension(0, 0);
    Dimension dim11 = ImmutableDimension.newImmutableDimension(0, 0);
    Dimension dim22 = ImmutableDimension.newImmutableDimension(1, 1);
    Dimension dim33 = ImmutableDimension.newImmutableDimension(0, 0);

    assertTrue(dim11.hashCode() == dim11.hashCode());
    assertTrue(dim11.hashCode() == dim33.hashCode());
    assertTrue(dim11.hashCode() == dim1.hashCode());
    assertTrue(dim11.hashCode() == dim3.hashCode());
    assertFalse(dim1.hashCode() == dim22.hashCode());
    assertFalse(dim3.hashCode() == dim22.hashCode());
    assertFalse(dim11.hashCode() == dim22.hashCode());
    assertFalse(dim33.hashCode() == dim22.hashCode());
  }

}