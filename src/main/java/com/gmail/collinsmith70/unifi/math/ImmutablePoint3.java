package com.gmail.collinsmith70.unifi.math;

import android.support.annotation.NonNull;

public final class ImmutablePoint3 extends Point3 {

  public static ImmutablePoint3 newImmutablePoint() {
    return new ImmutablePoint3();
  }

  public static ImmutablePoint3 newImmutablePoint(int x, int y, int z) {
    return new ImmutablePoint3(x, y, z);
  }

  public static ImmutablePoint3 copyOf(@NonNull IPoint3 src) {
    return new ImmutablePoint3(src);
  }

  private ImmutablePoint3() {
    super();
  }

  private ImmutablePoint3(int x, int y, int z) {
    super(x, y, z);
  }

  private ImmutablePoint3(@NonNull IPoint3 src) {
    super(src);
  }

  @Deprecated
  @Override
  public void setX(int x) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setY(int y) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setZ(int z) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(int x, int y) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(int x, int y, int z) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull IPoint2 src) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull IPoint3 src) {
    throw new UnsupportedOperationException();
  }

}
