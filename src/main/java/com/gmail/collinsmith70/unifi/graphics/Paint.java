package com.gmail.collinsmith70.unifi.graphics;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.Validate;

public class Paint {

  @NonNull
  private Color color;

  public Paint() {
  }

  @NonNull
  public Color getColor() {
    return color;
  }

  protected final void _setColor(@NonNull Color color) {
    Validate.isTrue(color != null, "color cannot be null");
    this.color = color;
  }

  public void setColor(@NonNull Color color) {
    _setColor(color);
  }

  @Override
  @NonNull
  public String toString() {
    return String.format("Paint: { color=%s }", color);
  }

  public static final class Builder {

    @NonNull
    private final Paint paint;

    public Builder() {
      this.paint = new Paint();
    }

    public Paint build() {
      return paint;
    }

    @NonNull
    public Color getColor() {
      return paint.getColor();
    }

    public void setColor(@NonNull Color color) {
      Validate.isTrue(color != null, "color cannot be null");
      paint.setColor(color);
    }

  }

}
