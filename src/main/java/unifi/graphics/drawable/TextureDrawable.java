package unifi.graphics.drawable;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.badlogic.gdx.graphics.Texture;

import unifi.graphics.Canvas;
import unifi.graphics.Color;
import unifi.graphics.Paint;
import unifi.graphics.PixelFormat;

/**
 * A drawable that will draw a {@link Texture}.
 */
public class TextureDrawable extends AbstractDrawable {

  @NonNull private static final Paint sPaint = new Paint();

  @Nullable private ConstantState mTextureState;

  @IntRange(from = 0, to = 255) private int mAlpha;

  /**
   * Constructs a texture drawable with a {@code null} texture. This is intended
   * for internal use only when loading texture drawables from resources.
   */
  TextureDrawable() {
    mTextureState.mTexture = null;
    mAlpha = 255;
  }

  /**
   * Constructs a texture drawable with the specified texture and alpha set to
   * {@code 255}.
   */
  public TextureDrawable(@NonNull Texture texture) {
    this(texture, 255);
  }

  /**
   * Constructs a texture drawable with the specified texture and alpha.
   */
  public TextureDrawable(@NonNull Texture texture, @IntRange(from = 0, to = 255) int alpha) {
    if (texture == null) {
      throw new IllegalArgumentException("Cannot create a TextureDrawable with a null texture");
    } else if (alpha < 0 || alpha > 255) {
      throw new IllegalArgumentException("Alpha must be between [0..255] (inclusive)");
    }

    mTextureState.mTexture = texture;
    mAlpha = alpha;
  }

  /**
   * Returns the texture drawn by this drawable, or {@code null} if no texture
   * has been set yet, as is with the case where a texture is loaded from
   * resources and has not completed loading yet.
   */
  @Nullable
  public Texture getTexture() {
    return mTextureState.mTexture;
  }

  /**
   * Sets the texture drawn by this drawable.
   */
  public void setTexture(@NonNull Texture texture) {
    if (texture == null) {
      throw new IllegalArgumentException("Cannot create a TextureDrawable with a null texture");
    }

    if (mTextureState.mTexture != texture) {
      mTextureState.mTexture = texture;
      invalidateSelf();
    }
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    if (mTextureState.mTexture != null) {
      sPaint.setColor(mAlpha == 255 ? Color.WHITE : Color.setAlpha(Color.WHITE, mAlpha));
      canvas.draw(mTextureState.mTexture, getBounds(), sPaint);
    }
  }

  @Override
  public int getOpacity() {
    switch (mAlpha) {
      case 0:   return PixelFormat.TRANSPARENT;
      case 255: return PixelFormat.OPAQUE;
      default:  return PixelFormat.TRANSLUCENT;
    }
  }

  @Override
  public int getAlpha() {
    return mAlpha;
  }

  @Override
  public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    if (alpha < 0 || alpha > 255) {
      throw new IllegalArgumentException("Alpha must be between [0..255] (inclusive)");
    }

    mAlpha = alpha;
  }

  @Override
  public int getIntrinsicWidth() {
    if (mTextureState.mTexture == null) {
      return 0;
    }

    return mTextureState.mTexture.getWidth();
  }

  @Override
  public int getIntrinsicHeight() {
    if (mTextureState.mTexture == null) {
      return 0;
    }

    return mTextureState.mTexture.getHeight();
  }

  private TextureDrawable(ConstantState state) {
    mTextureState = state;
  }

  final static class ConstantState implements Drawable.ConstantState {

    @NonNull final Paint mPaint;

    @Nullable Texture mTexture;

    @IntRange(from = 0, to = 255) int mAlpha;

    ConstantState(@Nullable Texture texture) {
      mTexture = texture;
      mPaint = new Paint();
    }

    ConstantState(@NonNull ConstantState state) {
      mTexture = state.mTexture;
      mPaint = new Paint(state.mPaint);
    }

  }
}
