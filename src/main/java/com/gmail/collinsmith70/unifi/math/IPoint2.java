package com.gmail.collinsmith70.unifi.math;

import android.support.annotation.NonNull;

public interface IPoint2 {
  
  @NonNull
  static final IPoint2 ZERO = ImmutablePoint2.newImmutablePoint();

  int getX();
  
  void setX(int x);

  int getY();

  void setY(int y);

  void set(int x, int y);

  void set(@NonNull IPoint2 src);

  boolean equals(int x, int y);
  
}
