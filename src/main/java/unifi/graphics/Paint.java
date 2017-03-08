package unifi.graphics;

import android.support.annotation.ColorInt;

public class Paint {

  static final int DEFAULT_PAINT_FLAGS = 0;

  private int mFlags;

  @ColorInt int mColor;

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
}
