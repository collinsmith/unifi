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

  @NonNull
  private ConstantState mColorState;
  private boolean mMutated;

  /**
   * Constructs a color drawable that will draw in black.
   */
  ColorDrawable() {
    mColorState = new ConstantState();
  }

  /**
   * Constructs a color drawable that will draw the specified {@code Color}.
   *
   * @param color The color to draw
   */
  public ColorDrawable(@ColorInt int color) {
    this();
    setColor(color);
  }

  /**
   * Returns the {@link Color} of this drawable.
   */
  @ColorInt
  public int getColor() {
    return mColorState.mUseColor;
  }

  /**
   * Sets the color of this drawable to the specified {@link Color}.
   */
  public void setColor(@ColorInt int color) {
    if (mColorState.mBaseColor != color || mColorState.mUseColor != color) {
      mColorState.mBaseColor = mColorState.mUseColor = color;
      invalidateSelf();
    }
  }

  @NonNull
  @Override
  public Drawable mutate() {
    if (!mMutated && super.mutate() == this) {
      mColorState = new ConstantState(mColorState);
      mMutated = true;
    }

    return this;
  }

  @Override
  public void clearMutated() {
    super.clearMutated();
    mMutated = false;
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    if (Color.alpha(mColorState.mUseColor) != 0) {
      sPaint.setColor(mColorState.mUseColor);
      canvas.drawRect(getBounds(), sPaint);
    }
  }

  @Override
  public int getOpacity() {
    switch (Color.alpha(mColorState.mUseColor)) {
      case 0:   return PixelFormat.TRANSPARENT;
      case 255: return PixelFormat.OPAQUE;
      default:  return PixelFormat.TRANSLUCENT;
    }
  }

  @Override
  public int getAlpha() {
    return Color.alpha(mColorState.mUseColor);
  }

  @Override
  public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    alpha += alpha >> 7; // make it 0..256
    final int baseAlpha = Color.alpha(mColorState.mBaseColor);
    final int useAlpha = baseAlpha * alpha >> 8;
    final int useColor = Color.setAlpha(mColorState.mBaseColor, useAlpha);
    if (mColorState.mUseColor != useColor) {
      mColorState.mUseColor = useColor;
      invalidateSelf();
    }
  }

  @Override
  protected boolean onStateChange(@NonNull int[] stateSet) {
    return false;
  }

  @Override
  public boolean isStateful() {
    return false;
  }

  private ColorDrawable(@NonNull ConstantState state) {
    assert state != null;
    mColorState = state;
  }

  final static class ConstantState implements Drawable.ConstantState {
    /** Base color, independent of  {@link #setAlpha(int)} */
    @ColorInt int mBaseColor;
    /** Base color, modulated by {@link #setAlpha(int)} */
    @ColorInt int mUseColor;

    ConstantState() {}

    ConstantState(@NonNull ConstantState state) {
      mBaseColor = state.mBaseColor;
      mUseColor = state.mUseColor;
    }
  }
}
