package com.gmail.collinsmith70.unifi.math;

import android.support.annotation.NonNull;

public interface IPoint3 extends IPoint2 {
  
  @NonNull
  static final IPoint3 ZERO = ImmutablePoint3.newImmutablePoint();

  int getX();
  
  void setX(int x);

  int getY();

  void setY(int y);

  int getZ();

  void setZ(int z);

  void set(int x, int y, int z);

  void set(@NonNull IPoint3 src);

  boolean equals(int x, int y, int z);
  
}
