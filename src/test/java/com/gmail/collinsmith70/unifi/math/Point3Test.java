package com.gmail.collinsmith70.unifi.math;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class Point3Test {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };

  @Test
  public void testPoint3() {
	Point3 pt = new Point3();
	assertTrue(pt.equals(0, 0, 0));
  }

  @Test
  public void testPoint3_IntIntInt() {
    int x, y, z;
    for (int i = 0; i < data.length - 2; i++) {
      x = data[i];
      y = data[i + 1];
      z = data[i + 2];
      Point3 pt = new Point3(x, y, z);
      assertTrue(pt.equals(x, y, z));
    }
  }

  @Test
  public void testPoint3_Point3() {
    int x, y, z;
    for (int i = 0; i < data.length - 2; i++) {
      x = data[i];
      y = data[i + 1];
      z = data[i + 2];
      Point3 src = new Point3(x, y, z);
      Point3 pt = new Point3(src);
      assertTrue(pt.equals(src));
    }
  }

  @Test
  public void testSetZ() {
    Point3 pt = new Point3();
    for (int testCase : data) {
      pt.setZ(testCase);
      assertTrue(pt.getZ() == testCase);
    }
  }

  @Test
  public void testSet_IntIntInt() {
    Point3 pt = new Point3();
    int x, y, z;
    for (int i = 0; i < data.length - 2; i++) {
      x = data[i];
      y = data[i + 1];
      z = data[i + 2];
      pt.set(x, y, z);
      assertTrue(pt.getX() == x);
      assertTrue(pt.getY() == y);
      assertTrue(pt.getZ() == z);
    }
  }

  @Test
  public void testSet_Point() {
    Point3 pt = new Point3();
    Point3 pt2;
    int x, y, z;
    for (int i = 0; i < data.length - 2; i++) {
      x = data[i];
      y = data[i + 1];
      z = data[i + 2];
      pt2 = new Point3(x, y, z);
      pt.set(pt2);
      assertTrue(pt.equals(pt2));
    }
  }

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void testOnChange() {
    final String expectMessage = "Point3#onChange() called";
    exception.expect(RuntimeException.class);
    exception.expectMessage(expectMessage);
    new Point3() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.setX(1);
    new Point3() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.setY(1);
    new Point3() {
	  @Override
	  protected void onChange() {
	    throw new RuntimeException(expectMessage);
	  }
    }.setZ(1);
    new Point3() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.set(1, 1, 1);
    new Point3() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.set(new Point3(1, 1, 1));
  }

  @Test
  public void testEquals_IntIntInt() {
    int x, y, z;
    for (int i = 0; i < data.length - 2; i++) {
      x = data[i];
      y = data[i + 1];
      z = data[i + 2];
      assertTrue(new Point3(x, y, z).equals(x, y, z));
    }
  }

  @Test
  public void testEquals_Object() {
    Point3 pt1 = new Point3(0, 0, 0);
    Point3 pt2 = new Point3(1, 1, 1);

    assertTrue(pt1.equals(pt1));
    assertTrue(pt2.equals(pt2));
    assertFalse(pt1.equals(pt2));
    assertFalse(pt2.equals(pt1));
    assertFalse(pt1.equals(null));
    assertFalse(pt2.equals(null));
  }

  @Test
  public void testHashCode() {
    Point3 pt1 = new Point3(0, 0, 0);
    Point3 pt2 = new Point3(1, 1, 1);
    Point3 pt3 = new Point3(0, 0, 0);

    assertTrue(pt1.hashCode() == pt1.hashCode());
    assertTrue(pt1.hashCode() == pt3.hashCode());
    assertFalse(pt1.hashCode() == pt2.hashCode());
    assertFalse(pt3.hashCode() == pt2.hashCode());
  }

}
