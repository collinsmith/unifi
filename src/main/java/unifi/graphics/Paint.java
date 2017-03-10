package unifi.graphics;

import com.google.common.base.Preconditions;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public class Paint {

  static final int DEFAULT_PAINT_FLAGS = 0;

  private int mFlags;

  @ColorInt int mColor;

  @NonNull Style mStyle = Style.FILL;

  float mStrokeWidth = 1.0f;

  public Paint() {
    this(0);
  }

  public Paint(int flags) {
    setFlags(flags | DEFAULT_PAINT_FLAGS);
  }

  public int getFlags() {
    return mFlags;
  }

  public void setFlags(int flags) {
    mFlags = flags;
  }

  @ColorInt
  public int getColor() {
    return mColor;
  }

  public void setColor(@ColorInt int color) {
    mColor = color;
  }

  @NonNull
  public Style getStyle() {
    return mStyle;
  }

  public void setStyle(@NonNull Style style) {
    mStyle = Preconditions.checkNotNull(style);
  }

  public float getStrokeWidth() {
    return mStrokeWidth;
  }

  public void setStrokeWidth(float strokeWidth) {
    mStrokeWidth = Math.max(strokeWidth, 1.0f);
  }

  public enum Style {
    FILL,
    STROKE,
  }
}
