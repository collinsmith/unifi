package com.gmail.collinsmith70.unifi.math;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;

public class RectTest {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testRect() {
    Rect rect = new Rect();
    assertTrue(rect.equals(0, 0, 0, 0));
  }
  
  @Test
  public void testRect_IntIntIntInt() {
    Rect rect = new Rect();
    int left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      rect = new Rect(left, top, right, bottom);
      assertTrue(rect.equals(left, top, right, bottom));
    }
  }
  
  @Test
  public void testRect_Rect() {
    Rect rect = new Rect();
    Rect src;
    int left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      src = new Rect(left, top, right, bottom);
      rect = new Rect(src);
      assertTrue(rect.equals(src));
    }
  }
  
  @Test
  public void testSetLeft() {
    Rect rect = new Rect();
    for (int testCase : data) {
      rect.setLeft(testCase);
      assertTrue(rect.getLeft() == testCase);
    }
  }

  @Test
  public void testSetTop() {
    Rect rect = new Rect();
    for (int testCase : data) {
      rect.setTop(testCase);
      assertTrue(rect.getTop() == testCase);
    }
  }

  @Test
  public void testSetRight() {
    Rect rect = new Rect();
    for (int testCase : data) {
      rect.setRight(testCase);
      assertTrue(rect.getRight() == testCase);
    }
  }

  @Test
  public void testSetBottom() {
    Rect rect = new Rect();
    for (int testCase : data) {
      rect.setBottom(testCase);
      assertTrue(rect.getBottom() == testCase);
    }
  }

  @Test
  public void testSet_IntIntIntInt() {
    Rect rect = new Rect();
    int left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 1];
      bottom = data[i + 1];
      rect.set(left, top, right, bottom);
      assertTrue(rect.getLeft() == left);
      assertTrue(rect.getTop() == top);
      assertTrue(rect.getRight() == right);
      assertTrue(rect.getBottom() == bottom);
    }
  }

  @Test
  public void testSet_Rect() {
    Rect rect = new Rect();
    Rect rect2;
    int left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      rect2 = new Rect(left, top, right, bottom);
      rect.set(rect2);
      assertTrue(rect.equals(rect2));
    }
  }

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void testOnChange() {
    final String expectMessage = "Rect#onChange() called";
    exception.expect(RuntimeException.class);
    exception.expectMessage(expectMessage);
    new Rect() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.setLeft(1);
    new Rect() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.setTop(1);
    new Rect() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.setRight(1);
    new Rect() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.setBottom(1);
    new Rect() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.set(1, 1, 1, 1);
    new Rect() {
      @Override
      protected void onChange() {
        throw new RuntimeException(expectMessage);
      }
    }.set(new Rect(1, 1, 1, 1));
  }

  @Test
  public void testEquals_IntIntIntInt() {
    int left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      assertTrue(new Rect(left, top, right, bottom).equals(left, top, right, bottom));
    }
  }

  @Test
  public void testEquals_Object() {
    Rect rect1 = new Rect(0, 0, 0, 0);
    Rect rect2 = new Rect(1, 1, 1, 1);

    assertTrue(rect1.equals(rect1));
    assertTrue(rect2.equals(rect2));
    assertFalse(rect1.equals(rect2));
    assertFalse(rect2.equals(rect1));
    assertFalse(rect1.equals(null));
    assertFalse(rect2.equals(null));
  }

  @Test
  public void testHashCode() {
    Rect rect1 = new Rect(0, 0, 0, 0);
    Rect rect2 = new Rect(1, 1, 1, 1);
    Rect rect3 = new Rect(0, 0, 0, 0);

    assertTrue(rect1.hashCode() == rect1.hashCode());
    assertTrue(rect1.hashCode() == rect3.hashCode());
    assertFalse(rect1.hashCode() == rect2.hashCode());
    assertFalse(rect3.hashCode() == rect2.hashCode());
  }

  @Test
  public void testContains_Point() {
    Point2 p1 = new Point2(-1, -1);
    Point2 p2 = new Point2(0, 0);
    Point2 p3 = new Point2(1, 1);
    Point2 p4 = new Point2(9, 9);
    Point2 p5 = new Point2(10, 10);
    Point2 p6 = new Point2(11, 11);
    Rect rect = new Rect(0, 10, 10, 0);

    assertFalse(rect.contains(p1));
    assertTrue(rect.contains(p2));
    assertTrue(rect.contains(p3));
    assertTrue(rect.contains(p4));
    assertTrue(rect.contains(p5));
    assertFalse(rect.contains(p6));
  }

}
