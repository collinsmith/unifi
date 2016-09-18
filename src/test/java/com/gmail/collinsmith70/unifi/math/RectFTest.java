package com.gmail.collinsmith70.unifi.math;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RectFTest {

  private static final float[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testRectF() {
    RectF rect = new RectF();
    assertTrue(rect.equals(0, 0, 0, 0));
  }
  
  @Test
  public void testRectF_FloatFloatFloatFloat() {
    RectF rect = new RectF();
    float left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      rect = new RectF(left, top, right, bottom);
      assertTrue(rect.equals(left, top, right, bottom));
    }
  }
  
  @Test
  public void testRectF_RectF() {
    RectF rect = new RectF();
    RectF src;
    float left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      src = new RectF(left, top, right, bottom);
      rect = new RectF(src);
      assertTrue(rect.equals(src));
    }
  }
  
  @Test
  public void testRectF_Rect() {
    RectF rect = new RectF();
    Rect src;
    int left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = (int)data[i];
      top = (int)data[i + 1];
      right = (int)data[i + 2];
      bottom = (int)data[i + 3];
      src = new Rect(left, top, right, bottom);
      rect = new RectF(src);
      assertTrue(rect.equals(src.getLeft(), src.getTop(), src.getRight(), src.getBottom()));
    }
  }

  @Test
  public void testSetLeft() {
    RectF rect = new RectF();
    for (float testCase : data) {
      rect.setLeft(testCase);
      assertTrue(rect.getLeft() == testCase);
    }
  }

  @Test
  public void testSetTop() {
    RectF rect = new RectF();
    for (float testCase : data) {
      rect.setTop(testCase);
      assertTrue(rect.getTop() == testCase);
    }
  }

  @Test
  public void testSetRight() {
    RectF rect = new RectF();
    for (float testCase : data) {
      rect.setRight(testCase);
      assertTrue(rect.getRight() == testCase);
    }
  }

  @Test
  public void testSetBottom() {
    RectF rect = new RectF();
    for (float testCase : data) {
      rect.setBottom(testCase);
      assertTrue(rect.getBottom() == testCase);
    }
  }

  @Test
  public void testSet_IntIntIntInt() {
    RectF rect = new RectF();
    float left, top, right, bottom;
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
  public void testSet_RectF() {
    RectF rect = new RectF();
    RectF rect2;
    float left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      rect2 = new RectF(left, top, right, bottom);
      rect.set(rect2);
      assertTrue(rect.equals(rect2));
    }
  }

  @Test
  public void testOnChange() {
    final String expectMessage = "RectF#onChange() called";
    try {
      new RectF() {
        @Override
        protected void onChange() {
          throw new RuntimeException(expectMessage);
        }
      }.setLeft(1);
      fail("RuntimeException was not thrown!");
    } catch (RuntimeException e) {
      // expected
    }

    try {
      new RectF() {
        @Override
        protected void onChange() {
          throw new RuntimeException(expectMessage);
        }
      }.setTop(1);
      fail("RuntimeException was not thrown!");
    } catch (RuntimeException e) {
      // expected
    }

    try {
      new RectF() {
        @Override
        protected void onChange() {
          throw new RuntimeException(expectMessage);
        }
      }.setRight(1);
      fail("RuntimeException was not thrown!");
    } catch (RuntimeException e) {
      // expected
    }

    try {
      new RectF() {
        @Override
        protected void onChange() {
          throw new RuntimeException(expectMessage);
        }
      }.setBottom(1);
      fail("RuntimeException was not thrown!");
    } catch (RuntimeException e) {
      // expected
    }

    try {
      new RectF() {
        @Override
        protected void onChange() {
          throw new RuntimeException(expectMessage);
        }
      }.set(1, 1, 1, 1);
      fail("RuntimeException was not thrown!");
    } catch (RuntimeException e) {
      // expected
    }

    try {
      new RectF() {
        @Override
        protected void onChange() {
          throw new RuntimeException(expectMessage);
        }
      }.set(new RectF(1, 1, 1, 1));
      fail("RuntimeException was not thrown!");
    } catch (RuntimeException e) {
      // expected
    }
  }

  @Test
  public void testEquals_IntIntIntInt() {
    float left, top, right, bottom;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      assertTrue(new RectF(left, top, right, bottom).equals(left, top, right, bottom));
    }
  }

  @Test
  public void testEquals_Object() {
    RectF rect1 = new RectF(0, 1, 2, 3);
    RectF rect2 = new RectF(4, 5, 6, 7);
    RectF rect3 = new RectF(4, 5, 6, 7);

    assertTrue(rect1.equals(rect1));
    assertTrue(rect2.equals(rect2));
    assertTrue(rect2.equals(rect3));
    assertFalse(rect1.equals(rect2));
    assertFalse(rect2.equals(rect1));
    assertFalse(rect1.equals(null));
    assertFalse(rect2.equals(null));
  }

  @Test
  public void testHashCode() {
    RectF rect1 = new RectF(0, 1, 2, 3);
    RectF rect2 = new RectF(4, 5, 6, 7);
    RectF rect3 = new RectF(0, 1, 2, 3);

    assertTrue(rect1.hashCode() == rect1.hashCode());
    assertTrue(rect1.hashCode() == rect3.hashCode());
    assertFalse(rect1.hashCode() == rect2.hashCode());
    assertFalse(rect3.hashCode() == rect2.hashCode());
  }

}
