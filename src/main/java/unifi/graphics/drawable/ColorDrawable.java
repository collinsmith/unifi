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
    if (Color.alpha(sPaint.getColor()) != 0) {
      sPaint.setColor(mColor);
      canvas.drawRect(getBounds(), sPaint);
    }
  }

  @Override
  public int getOpacity() {
    switch (Color.alpha(mColor)) {
      case 0:   return PixelFormat.TRANSPARENT;
      case 255: return PixelFormat.OPAQUE;
      default:  return PixelFormat.TRANSLUCENT;
    }
  }

  @Override
  public int getAlpha() {
    return Color.alpha(mColor);
  }

  @Override
  public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    mColor = Color.setAlpha(mColor, alpha);
  }
}
