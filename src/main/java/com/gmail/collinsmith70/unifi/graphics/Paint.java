package com.gmail.collinsmith70.unifi.graphics;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.Validate;

public class Paint {

  @NonNull
  private Color color;

  private Paint() {
  }

  @NonNull
  public Color getColor() {
    return color;
  }

  protected void _setColor(@NonNull Color color) {
    Validate.isTrue(color != null, "color cannot be null");
    this.color = color;
  }

  public void setColor(@NonNull Color color) {
    _setColor(color);
  }

}
