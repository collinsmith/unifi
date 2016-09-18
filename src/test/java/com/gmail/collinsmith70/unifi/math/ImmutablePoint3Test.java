package com.gmail.collinsmith70.unifi.math;

import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

public class ImmutablePoint3Test {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testImmutablePoint3() {
    Point3 pt = ImmutablePoint3.newImmutablePoint3();
    assertTrue(pt.equals(0, 0, 0));
  }
  
  @Test
  public void testImmutablePoint3_IntIntInt() {
    int x, y, z;
    Point3 pt;
    for (int i = 0; i < data.length - 2; i++) {
      x = data[i];
      y = data[i + 1];
      z = data[i + 2];
      pt = ImmutablePoint3.newImmutablePoint3(x, y, z);
      assertTrue(pt.equals(x, y, z));
    }
  }
  
  @Test
  public void testImmutablePoint3_Point3() {
    int x, y, z;
    Point3 pt1, pt2;
    for (int i = 0; i < data.length - 2; i++) {
      x = data[i];
      y = data[i + 1];
      z = data[i + 2];
      pt1 = new Point3(x, y, z);
      pt2 = ImmutablePoint3.copyOf(pt1);
      assertTrue(pt1.equals(pt2));
    }
  }

  @Test
  public void testSetX() {
    Point3 pt = ImmutablePoint3.ZERO;
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
    Point3 pt = ImmutablePoint3.ZERO;
    for (int testCase : data) {
      try {
        pt.setY(testCase);
        assertTrue(pt.getY() == testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch(UnsupportedOperationException e){
        // expected
      }
    }
  }

  @Test
  public void testSetZ() {
    Point3 pt = ImmutablePoint3.ZERO;
    for (int testCase : data) {
      try {
        pt.setZ(testCase);
        assertTrue(pt.getZ() == testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch(UnsupportedOperationException e){
        // expected
      }
    }
  }

}
