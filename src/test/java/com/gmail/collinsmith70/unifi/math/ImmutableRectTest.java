package com.gmail.collinsmith70.unifi.math;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ImmutableRectTest {

  private static final int[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testImmutableRect() {
    Rect rect = ImmutableRect.newImmutableRect();
    assertTrue(rect.getLeft() == 0);
    assertTrue(rect.getTop() == 0);
    assertTrue(rect.getRight() == 0);
    assertTrue(rect.getBottom() == 0);
  }
  
  @Test
  public void testImmutableRect_IntIntIntInt() {
    int left, top, right, bottom;
    Rect rect;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      rect = ImmutableRect.newImmutableRect(left, top, right, bottom);
      assertTrue(rect.getLeft() == left);
      assertTrue(rect.getTop() == top);
      assertTrue(rect.getRight() == right);
      assertTrue(rect.getBottom() == bottom);
    }
  }
  
  @Test
  public void testImmutableRect_Rect() {
    int left, top, right, bottom;
    Rect src, rect;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      src = new Rect(left, top, right, bottom);
      rect = ImmutableRect.copyOf(src);
      assertTrue(rect.getLeft() == src.getLeft());
      assertTrue(rect.getTop() == src.getTop());
      assertTrue(rect.getRight() == src.getRight());
      assertTrue(rect.getBottom() == src.getBottom());
    }
  }

  @Test
  public void testSetLeft() {
    Rect rect = ImmutableRect.ZERO;
    for (int testCase : data) {
      try {
        rect.setLeft(testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

  @Test
  public void testSetTop() {
    Rect rect = ImmutableRect.ZERO;
    for (int testCase : data) {
      try {
        rect.setTop(testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

  @Test
  public void testSetRight() {
    Rect rect = ImmutableRect.ZERO;
    for (int testCase : data) {
      try {
        rect.setRight(testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

  @Test
  public void testSetBottom() {
    Rect rect = ImmutableRect.ZERO;
    for (int testCase : data) {
      try {
        rect.setBottom(testCase);
        fail("UnsupportedOperationException was not thrown!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }
  
}
