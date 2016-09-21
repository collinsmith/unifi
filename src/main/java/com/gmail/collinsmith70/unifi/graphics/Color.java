package com.gmail.collinsmith70.unifi.graphics;

import android.support.annotation.NonNull;

import com.gmail.collinsmith70.unifi.annotation.Unsigned;

public class Color {

  @NonNull
  public static final Color BLACK = new Color(0xFF000000);

  @Unsigned
  private final int argb8888;

  @NonNull
  private final com.badlogic.gdx.graphics.Color color;

  public Color() {
    this(0);
  }

  public Color(@Unsigned int argb8888) {
    this.argb8888 = argb8888;
    this.color = new com.badlogic.gdx.graphics.Color();
    com.badlogic.gdx.graphics.Color.argb8888ToColor(color, argb8888);
  }

  @NonNull
  public com.badlogic.gdx.graphics.Color getGdxColor() {
    com.badlogic.gdx.graphics.Color.argb8888ToColor(color, argb8888);
    return new com.badlogic.gdx.graphics.Color(color);
  }

}
