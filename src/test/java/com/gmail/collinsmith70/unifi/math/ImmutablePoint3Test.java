package com.gmail.collinsmith70.unifi.math;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class ImmutablePoint3Test {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testImmutablePoint3() {
    Point3 pt = ImmutablePoint3.newImmutablePoint();
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
      pt = ImmutablePoint3.newImmutablePoint(x, y, z);
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

  @Rule
  public final ExpectedException exception = ExpectedException.none();
  
  @Test
  public void testSetX() {
    exception.expect(UnsupportedOperationException.class);
    Point3 pt = ImmutablePoint3.ZERO;
    for (int testCase : data) {
      pt.setX(testCase);
      assertTrue(pt.getX() == testCase);
    }
  }

  @Test
  public void testSetY() {
    exception.expect(UnsupportedOperationException.class);
    Point3 pt = ImmutablePoint3.ZERO;
    for (int testCase : data) {
      pt.setY(testCase);
      assertTrue(pt.getY() == testCase);
    }
  }

  @Test
  public void testSetZ() {
    exception.expect(UnsupportedOperationException.class);
    Point3 pt = ImmutablePoint3.ZERO;
    for (int testCase : data) {
      pt.setZ(testCase);
      assertTrue(pt.getZ() == testCase);
    }
  }

}
