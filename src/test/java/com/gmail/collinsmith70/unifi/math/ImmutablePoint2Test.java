package com.gmail.collinsmith70.unifi.math;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ImmutablePoint2Test {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testImmutablePoint2() {
    Point2 pt = ImmutablePoint2.newImmutablePoint2();
    assertTrue(pt.equals(0, 0));
  }
  
  @Test
  public void testImmutablePoint2_IntInt() {
    int x, y;
    Point2 pt;
    for (int i = 0; i < data.length - 1; i++) {
      x = data[i];
      y = data[i + 1];
      pt = ImmutablePoint2.newImmutablePoint2(x, y);
      assertTrue(pt.equals(x, y));
    }
  }
  
  @Test
  public void testImmutablePoint2_Point2() {
    int x, y;
    Point2 pt1, pt2;
    for (int i = 0; i < data.length - 1; i++) {
      x = data[i];
      y = data[i + 1];
      pt1 = new Point2(x, y);
      pt2 = ImmutablePoint2.copyOf(pt1);
      assertTrue(pt1.equals(pt2));
    }
  }

  @Test
  public void testSetX() {
    Point2 pt = ImmutablePoint2.ZERO;
    for (int testCase : data) {
      try {
        pt.setX(testCase);
        assertTrue(pt.getX() == testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch(UnsupportedOperationException e){
        // expected
      }
    }
  }

  @Test
  public void testSetY() {
    Point2 pt = ImmutablePoint2.ZERO;
    for (int testCase : data) {
      try {
        pt.setY(testCase);
        assertTrue(pt.getY() == testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

}
