package com.gmail.collinsmith70.unifi.math;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.Validate;

public class Point3 extends Point2 {

  @NonNull
  static final Point3 ZERO = ImmutablePoint3.newImmutablePoint();
  
  private int z;

  public Point3() {
    this(0, 0, 0);
  }

  public Point3(int x, int y, int z) {
    _set(x, y, z);
  }

  public Point3(@NonNull Point3 src) {
    _set(src);
  }

  public int getZ() {
    return z;
  }

  private void _setZ(int z) {
    this.z = z;
  }

  public void setZ(int z) {
    if (getZ() != z) {
      _setZ(z);
      onChange();
    }
  }

  private void _set(int x, int y, int z) {
    _setX(x);
    _setY(y);
    _setZ(z);
  }

  public void set(int x, int y, int z) {
    if (!equals(x, y, z)) {
      _set(x, y, z);
      onChange();
    }
  }

  private void _set(@NonNull Point3 src) {
    Validate.isTrue(src != null, "source Point3 cannot be null");
    super._set(src);
    _setZ(src.getZ());
    
  }

  public void set(@NonNull Point3 src) {
    Validate.isTrue(src != null, "source Point3 cannot be null");
    if (!equals(src.getX(), src.getY(), src.getZ())) {
      _set(src);
      onChange();
    }
  }

  public final boolean equals(int x, int y, int z) {
    return super.equals(x, y) && getZ() == z;
  }
  
  @Override
  @CallSuper
  public boolean equals(@Nullable Object obj) {
    if (!super.equals(obj)) {
      return false;
    }
    
//  It's safe to assert that all of these conditions have been checked by super.equals(Object)
//  if (obj == null) {
//    return false;
//  } else if (obj == this) {
//    return true;
//  } else
    if (!(obj instanceof Point3)) {
      return false;
    }

    Point3 other = (Point3) obj;
    return getZ() == other.getZ();
  }

  @Override
  @CallSuper
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + getZ();
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s: { x=%d, y=%d, z=%d }",
        getClass().getSimpleName(), getX(), getY(), getZ());
  }
  
  public Point3 add(@NonNull Point3 src) {
    return add(src, this);
  }

  public Point3 add(@NonNull Point3 src, @NonNull Point3 dst) {
    Validate.isTrue(src != null, "source Point3 cannot be null");
    Validate.isTrue(dst != null, "destination Point3 cannot be null");
    if (!equals(src.getX(), src.getY())) {
      dst._set(getX() + src.getX(), getY() + src.getY());
      dst.onChange();
      return dst;
    }
    
    return dst;
  }

  public Point3 subtract(@NonNull Point3 src) {
    return subtract(src, this);
  }

  public Point3 subtract(@NonNull Point3 src, @NonNull Point3 dst) {
    Validate.isTrue(src != null, "source Point3 cannot be null");
    Validate.isTrue(dst != null, "destination Point3 cannot be null");
    if (!equals(src.getX(), src.getY())) {
      dst._set(getX() - src.getX(), getY() - src.getY());
      dst.onChange();
      return dst;
    }
    
    return dst;
  }

  public Point3 scale(double scalar) {
    return scale(scalar, this);
  }

  public Point3 scale(double scalar, @NonNull Point3 dst) {
    Validate.isTrue(dst != null, "destination Point3 cannot be null");
    if (scalar != 1.0) {
      dst._set((int)(getX() * scalar), (int)(getY() * scalar));
      dst.onChange();
      return dst;
    }
    
    return dst;
  }

}
