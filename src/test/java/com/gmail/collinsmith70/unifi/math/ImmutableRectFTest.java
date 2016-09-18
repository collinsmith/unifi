package com.gmail.collinsmith70.unifi.math;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ImmutableRectFTest {

  private static final float[] data = {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };
  
  @Test
  public void testImmutableRectF() {
    RectF rect = ImmutableRectF.newImmutableRectF();
    assertTrue(rect.getLeft() == 0f);
    assertTrue(rect.getTop() == 0f);
    assertTrue(rect.getRight() == 0f);
    assertTrue(rect.getBottom() == 0f);
  }
  
  @Test
  public void testImmutableRectF_IntIntIntInt() {
    float left, top, right, bottom;
    RectF rect;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      rect = ImmutableRectF.newImmutableRectF(left, top, right, bottom);
      assertTrue(rect.getLeft() == left);
      assertTrue(rect.getTop() == top);
      assertTrue(rect.getRight() == right);
      assertTrue(rect.getBottom() == bottom);
    }
  }
  
  @Test
  public void testImmutableRectF_Rect() {
    int left, top, right, bottom;
    Rect src;
    RectF rect;
    for (int i = 0; i < data.length - 3; i++) {
      left = (int)data[i];
      top = (int)data[i + 1];
      right = (int)data[i + 2];
      bottom = (int)data[i + 3];
      src = new Rect(left, top, right, bottom);
      rect = ImmutableRectF.copyOf(src);
      assertTrue(rect.getLeft() == src.getLeft());
      assertTrue(rect.getTop() == src.getTop());
      assertTrue(rect.getRight() == src.getRight());
      assertTrue(rect.getBottom() == src.getBottom());
    }
  }
  
  @Test
  public void testImmutableRectF_RectF() {
    float left, top, right, bottom;
    RectF src, rect;
    for (int i = 0; i < data.length - 3; i++) {
      left = data[i];
      top = data[i + 1];
      right = data[i + 2];
      bottom = data[i + 3];
      src = new RectF(left, top, right, bottom);
      rect = ImmutableRectF.copyOf(src);
      assertTrue(rect.getLeft() == src.getLeft());
      assertTrue(rect.getTop() == src.getTop());
      assertTrue(rect.getRight() == src.getRight());
      assertTrue(rect.getBottom() == src.getBottom());
    }
  }
  
}
