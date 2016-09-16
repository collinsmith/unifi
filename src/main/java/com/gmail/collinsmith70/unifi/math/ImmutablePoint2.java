package com.gmail.collinsmith70.unifi.math;

import android.support.annotation.NonNull;

public final class ImmutablePoint2 extends Point2 {

  public static ImmutablePoint2 newImmutablePoint2() {
    return new ImmutablePoint2();
  }

  public static ImmutablePoint2 newImmutablePoint2(int x, int y) {
    return new ImmutablePoint2(x, y);
  }

  public static ImmutablePoint2 copyOf(@NonNull Point2 src) {
    return new ImmutablePoint2(src);
  }

  private ImmutablePoint2() {
    super();
  }

  private ImmutablePoint2(int x, int y) {
    super(x, y);
  }

  private ImmutablePoint2(@NonNull Point2 src) {
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
  public void set(int x, int y) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull Point2 src) {
    throw new UnsupportedOperationException();
  }

}
