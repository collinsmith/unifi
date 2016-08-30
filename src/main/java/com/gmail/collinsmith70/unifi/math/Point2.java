package com.gmail.collinsmith70.unifi.math;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.Validate;

public class Point2 {
  
  @NonNull
  static final Point2 ZERO = ImmutablePoint2.newImmutablePoint();
  
  private int x;
  private int y;

  public Point2() {
    this(0, 0);
  }

  public Point2(int x, int y) {
    _set(x, y);
  }

  public Point2(@NonNull Point2 src) {
    _set(src);
  }

  protected void onChange() {}

  public int getX() {
    return x;
  }

  protected final void _setX(int x) {
    this.x = x;
  }

  public void setX(int x) {
    if (getX() != x) {
      _setX(x);
      onChange();
    }
  }

  public int getY() {
    return y;
  }

  protected final void _setY(int y) {
    this.y = y;
  }

  public void setY(int y) {
    if (getY() != y) {
      _setY(y);
      onChange();
    }
  }

  protected final void _set(int x, int y) {
    _setX(x);
    _setY(y);
  }

  public void set(int x, int y) {
    if (!equals(x, y)) {
      _set(x, y);
      onChange();
    }
  }

  protected final void _set(@NonNull Point2 src) {
    Validate.isTrue(src != null, "source Point2 cannot be null");
    _setX(src.getX());
    _setY(src.getY());
  }

  public void set(@NonNull Point2 src) {
    Validate.isTrue(src != null, "source Point2 cannot be null");
    if (!equals(src.getX(), src.getY())) {
      _set(src);
      onChange();
    }
  }

  public final boolean equals(int x, int y) {
    return getX() == x && getY() == y;
  }

  @Override
  @CallSuper
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (!(obj instanceof Point2)) {
      return false;
    }

    Point2 other = (Point2) obj;
    return equals(other.getX(), other.getY());
  }

  @Override
  @CallSuper
  public int hashCode() {
    int result = 17;
    result = 31 * result + getX();
    result = 31 * result + getY();
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s: { x=%d, y=%d }", getClass().getSimpleName(), getX(), getY());
  }

}
