package unifi.graphics;

import com.google.common.base.Preconditions;

import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public class Paint {

  static final int DEFAULT_PAINT_FLAGS = 0;

  private int mFlags;

  @ColorInt int mColor = Color.WHITE;

  @NonNull Style mStyle = Style.FILL;

  float mStrokeWidth = 1.0f;

  public Paint() {
    this(0);
  }

  public Paint(int flags) {
    setFlags(flags | DEFAULT_PAINT_FLAGS);
  }

  public Paint(@NonNull Paint src) {
    mFlags = src.mFlags;
    mColor = src.mColor;
    mStyle = src.mStyle;
    mStrokeWidth = src.mStrokeWidth;
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

  @IntRange(from = 0, to = 255)
  public int getAlpha() {
    return Color.alpha(mColor);
  }

  public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    if (alpha < 0 || alpha > 255) {
      throw new IllegalArgumentException("alpha must be in range [0..255] inclusive");
    }

    mColor = Color.setAlpha(mColor, alpha);
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
