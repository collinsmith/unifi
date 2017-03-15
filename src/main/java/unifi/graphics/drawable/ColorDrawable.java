package unifi.graphics.drawable;

import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import unifi.graphics.Canvas;
import unifi.graphics.Color;
import unifi.graphics.Paint;
import unifi.graphics.PixelFormat;

/**
 * A drawable that will fill its bounds with a specified {@link Color}.
 */
public class ColorDrawable extends AbstractDrawable {

  private static final Paint sPaint = new Paint();

  @ColorInt private int mColor;

  /**
   * Constructs a color drawable that will draw in black.
   */
  public ColorDrawable() {
    mColor = Color.BLACK;
  }

  /**
   * Constructs a color drawable that will draw the specified {@code Color}.
   *
   * @param color The color to draw
   */
  public ColorDrawable(@ColorInt int color) {
    mColor = color;
  }

  /**
   * Returns the {@link Color} of this drawable.
   */
  @ColorInt
  public int getColor() {
    return mColor;
  }

  /**
   * Sets the color of this drawable to the specified {@link Color}.
   */
  public void setColor(@ColorInt int color) {
    if (mColor != color) {
      mColor = color;
      invalidateSelf();
    }
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    sPaint.setColor(mColor);
    canvas.fill(sPaint);
  }

  @Override
  public int getOpacity() {
    return Color.alpha(mColor) == 255 ? PixelFormat.OPAQUE : PixelFormat.TRANSLUCENT;
  }

  @Override
  public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    mColor &= ~0xFF000000;
    mColor |= (alpha << 24);
  }
}
