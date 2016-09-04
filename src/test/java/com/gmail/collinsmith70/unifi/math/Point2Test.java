package com.gmail.collinsmith70.unifi.math;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class Point2Test {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };

  @Test
  public void testSetX() {
    Point2 pt = new Point2();
    for (int testCase : data) {
      pt.setX(testCase);
      assertTrue(pt.getX() == testCase);
    }
  }

  @Test
  public void testSetY() {
    Point2 pt = new Point2();
    for (int testCase : data) {
      pt.setY(testCase);
      assertTrue(pt.getY() == testCase);
    }
  }

  @Test
  public void testSet_IntInt() {
    Point2 pt = new Point2();
    int x, y;
    for (int i = 0; i < data.length - 1; i++) {
      x = data[i];
      y = data[i + 1];
      pt.set(x, y);
      assertTrue(pt.getX() == x);
      assertTrue(pt.getY() == y);
    }
  }

  @Test
  public void testSet_Point2() {
    Point2 pt = new Point2();
    Point2 pt2;
    int x, y;
    for (int i = 0; i < data.length - 1; i++) {
      x = data[i];
      y = data[i + 1];
      pt2 = new Point2(x, y);
      pt.set(pt2);
      assertTrue(pt.equals(pt2));
    }
  }

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void testOnChange() {
    final String expectMessage = "Point2#onChange() called";
    exception.expect(RuntimeException.class);
    exception.expectMessage(expectMessage);
    new Point2() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.setX(1);
    new Point2() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.setY(1);
    new Point2() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.set(1, 1);
    new Point2() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.set(new Point2(1, 1));
  }

  @Test
  public void testEquals_IntInt() {
    int x, y;
    for (int i = 0; i < data.length - 1; i++) {
      x = data[i];
      y = data[i + 1];
      assertTrue(new Point2(x, y).equals(x, y));
    }
  }

  @Test
  public void testEquals_Object() {
    Point2 pt1 = new Point2(0, 0);
    Point2 pt2 = new Point2(1, 1);

    assertTrue(pt1.equals(pt1));
    assertTrue(pt2.equals(pt2));
    assertFalse(pt1.equals(pt2));
    assertFalse(pt2.equals(pt1));
    assertFalse(pt1.equals(null));
    assertFalse(pt2.equals(null));
  }

  @Test
  public void testHashCode() {
    Point2 pt1 = new Point2(0, 0);
    Point2 pt2 = new Point2(1, 1);
    Point2 pt3 = new Point2(0, 0);

    assertTrue(pt1.hashCode() == pt1.hashCode());
    assertTrue(pt1.hashCode() == pt3.hashCode());
    assertFalse(pt1.hashCode() == pt2.hashCode());
    assertFalse(pt3.hashCode() == pt2.hashCode());
  }

}