package unifi.graphics;

import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.NumberUtils;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;

public class Canvas implements Disposable {

  private static final float NO_TINT = convertColor(Color.WHITE);

  private static final int MAX_RECT_POOL_SIZE = 32;

  private static float convertColor(@ColorInt int color) {
    return NumberUtils.intToFloatColor(Color.abgr(color));
  }

  private final Vector2 tmp = new Vector2();

  @NonNull private final Batch mBatch;
  private final boolean mOwnsBatch;
  @NonNull private Viewport mViewport;

  @NonNull private final Pixmap mPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
  @Nullable private Texture mTexture;

  private static final int INITIAL_SAVE_COUNT = 16;
  private static final int SAVE_COUNT_GROW_SIZE = 16;
  @NonNull RectF[] mSaves;
  int mSaveCount;
  @Nullable RectF mClip;

  public Canvas() {
    mBatch = new SpriteBatch();
    mOwnsBatch = true;

    OrthographicCamera camera = new OrthographicCamera();
    camera.setToOrtho(true);

    mViewport = new ScalingViewport(Scaling.fit,
        Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

    mSaves = new RectF[INITIAL_SAVE_COUNT];
    mSaveCount = 0;
  }

  public final void resize(int width, int height) {
    mViewport.update(width, height, true);
  }

  /**
   * Disposes the canvas and releases and resources it holds. If the canvas does
   * not manage the {@link Batch} instance, then it is not disposed.
   */
  @Override
  @CallSuper
  public final void dispose() {
    if (mOwnsBatch) mBatch.dispose();
    mPixmap.dispose();
    if (mTexture != null) mTexture.dispose();
    onDispose();
  }

  /**
   * Called when the canvas is disposed.
   */
  protected void onDispose() {}

  /**
   * Prepares the canvas for drawing. This must preceded any draw calls and
   * terminate with a call to {@link #end()}.
   */
  public final void begin() {
    mBatch.begin();
    mBatch.enableBlending();
    mBatch.setProjectionMatrix(mViewport.getCamera().combined);
    onBegin();
  }

  /**
   * Called when the canvas is prepared for drawing.
   */
  protected final void onBegin() {}

  /**
   * Indicates whether or not this canvas is drawing. A canvas is considered to
   * be drawing in-between a call to {@link #begin()} and {@link #end()}.
   */
  @CallSuper
  public boolean isDrawing() {
    return mBatch.isDrawing();
  }

  /**
   * Finishes drawing and resets the state to the initial one, that is, disables
   * clipping.
   */
  public final void end() {
    mBatch.end();
    restoreToCount(0);
    onEnd();
  }

  /**
   * Called when the canvas finished drawing.
   */
  protected final void onEnd() {}

  /**
   * Renders the pending draw commands to the canvas without calling {@link #end()}.
   */
  public final void flush() {
    mBatch.flush();
    onFlush();
  }

  /**
   * Called when the canvas is flushed.
   */
  protected void onFlush() {}

  /**
   * Clips the subsequent draw commands to the intersection of the bounds of the
   * specified rectangle and the current clip bounds. If there are no clip
   * bounds set yet, then this will set the clip bounds to the specified bounds.
   *
   * <p>Clipping bounds can be saved with a call to {@link #save()} and restored
   * later on with a call to {@link #restore()}.
   *
   * <p>Note: The bounds are specified assuming the y-axis is pointing down,
   * that is, {@code (0, 0)} is located in the top left of the screen.
   *
   * @param left   The left edge of the clip bounds, in pixels
   * @param top    The top edge of the clip bounds, in pixels
   * @param right  The right edge of the clip bounds, in pixels
   * @param bottom The bottom edge of the clip bounds, in pixels
   *
   * @return {@code true} if the clip bounds are non-empty, {@code false} otherwise
   *
   * @see #clipRect(RectF)
   * @see #clipRect(Rect)
   */
  public boolean clipRect(float left, float top, float right, float bottom) {
    if (mClip == null) {
      assert mSaves.length > 0 : "mSaves initial size should be > 0";
      if (mSaves[0] != null) {
        mClip = mSaves[0];
        mClip.set(left, top, right, bottom);
      } else {
        mClip = new RectF(left, top, right, bottom);
      }

      Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
    } else {
      mClip.intersect(left, top, right, bottom);
    }

    flush();
    int width = (int) (right - left);
    int height = (int) (bottom - top);
    tmp.set(left, bottom);
    mViewport.project(tmp);
    left = tmp.x;
    bottom = tmp.y;
    HdpiUtils.glScissor((int) left, (int) bottom, width, height);
    return width > 0 && height > 0;
  }

  /**
   * Clips the subsequent draw commands to the intersection of the bounds of the
   * specified rectangle and the current clip bounds. If there are no clip
   * bounds set yet, then this will set the clip bounds to the specified
   * bounds.
   *
   * <p>Clipping bounds can be saved with a call to {@link #save()} and restored
   * later on with a call to {@link #restore()}.
   *
   * <p>Note: The bounds are specified assuming the y-axis is pointing down,
   * that is, {@code (0, 0)} is located in the top left of the screen.
   *
   * @param clip The clipping bounds to use, this is intersected with the
   *             bounds of the current clip rectangle
   *
   * @return {@code true} if the clip bounds are non-empty, {@code false} otherwise
   *
   * @see #clipRect(float, float, float, float)
   * @see #clipRect(Rect)
   */
  public boolean clipRect(@NonNull RectF clip) {
    return clipRect(clip.left, clip.top, clip.right, clip.bottom);
  }

  /**
   * Clips the subsequent draw commands to the intersection of the bounds of the
   * specified rectangle and the current clip bounds. If there are no clip
   * bounds set yet, then this will set the clip bounds to the specified
   * bounds.
   *
   * <p>Clipping bounds can be saved with a call to {@link #save()} and restored
   * later on with a call to {@link #restore()}.
   *
   * <p>Note: The bounds are specified assuming the y-axis is pointing down,
   * that is, {@code (0, 0)} is located in the top left of the screen.
   *
   * @param clip The clipping bounds to use, this is intersected with the
   *             bounds of the current clip rectangle
   *
   * @return {@code true} if the clip bounds are non-empty, {@code false} otherwise
   *
   * @see #clipRect(float, float, float, float)
   * @see #clipRect(RectF)
   */
  public boolean clipRect(@NonNull Rect clip) {
    return clipRect(clip.left, clip.top, clip.right, clip.bottom);
  }

  /**
   * Saves the current state of the canvas for restoration at a later time. This
   * is useful for when you have applied clipping that you want to modify, but
   * want to restore that clipping at a later time.
   *
   * <p>Currently this only saves the current clip bounds.
   *
   * <p>Note: This does not modify the rendered canvas content.
   *
   * @return An identifier which can be used with {@link #restoreToCount(int)}
   *         to restore the state of the canvas at the time of this call.
   *
   * @see #restore()
   * @see #restoreToCount(int)
   */
  public final int save() {
    assert mClip != null || mSaveCount == 0 : "mClip should not be null if saves > 0";
    if (mClip == null) {
      return 0;
    }

    if (mSaveCount == mSaves.length) {
      mSaves = Arrays.copyOf(mSaves, mSaveCount + SAVE_COUNT_GROW_SIZE);
    }

    RectF top = mSaves[mSaveCount] = mClip;

    mClip = mSaveCount < mSaves.length - 1 ? mSaves[mSaveCount + 1] : null;
    if (mClip != null) {
      mClip.set(top);
    } else {
      mClip = new RectF(top);
    }

    return mSaveCount++;
  }

  /**
   * Restores the state of the canvas to the previously saved one. If no
   * {@link #save()} calls precede this call and the current state is non-null,
   * then the state is reset to the initial one, that is clipping becomes
   * disabled.
   *
   * <p>Currently this only restores the clip bounds.
   *
   * <p>Note: This does not modify the rendered canvas content.
   *
   * @see #save()
   * @see #restoreToCount(int)
   */
  public final void restore() {
    if (mSaveCount == 0) {
      mClip = null;
      return;
    }

    mClip = mSaves[mSaveCount - 1];
    if (mSaveCount > MAX_RECT_POOL_SIZE) {
      mSaves[mSaveCount] = null;
    }

    mSaveCount--;
    flush();
    if (mClip == null) {
      Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    } else {
      HdpiUtils.glScissor((int) mClip.left, (int) mClip.top,
          (int) (mClip.right - mClip.left), (int) (mClip.bottom - mClip.top));
    }
  }

  /**
   * Restores the state of the canvas to the specified {@code saveCount}
   * previously returned by {@link #save()}.
   *
   * <p>Note: This does not modify the rendered canvas content.
   *
   * @param saveCount The save count to restore to
   *
   * @throws IllegalArgumentException if {@code saveCount} is invalid.
   *
   * @see #save()
   * @see #restoreToCount(int)
   */
  public void restoreToCount(int saveCount) {
    if (saveCount > mSaveCount) {
      throw new IllegalArgumentException("save exceeds saves: " + saveCount);
    } else if (saveCount < 0) {
      throw new IllegalArgumentException("save cannot be negative: " + saveCount);
    }

    mClip = saveCount == 0 ? null : mSaves[saveCount - 1];
    if (mSaveCount > MAX_RECT_POOL_SIZE) {
      Arrays.fill(mSaves, MAX_RECT_POOL_SIZE, mSaveCount, null);
    }

    mSaveCount = saveCount;
    if (mClip == null) {
      Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }
  }

  /**
   * Prepares the specified paint to be used as a tint for the batch.
   */
  private void prepareTint(@Nullable Paint paint) {
    if (paint == null) {
      mBatch.setColor(NO_TINT);
      return;
    }

    mBatch.setColor(convertColor(paint.mColor));
  }

  /**
   * Prepares the specified paint to be used as a solid color texture.
   */
  private void preparePixmap(@NonNull Paint paint) {
    if (paint == null) throw new IllegalArgumentException("paint cannot be null");
    mPixmap.drawPixel(0, 0, Color.rgba(paint.mColor));
    mTexture = new Texture(mPixmap);
  }

  /**
   * Fills this canvas with the specified paint.
   */
  public void fill(@NonNull Paint paint) {
    preparePixmap(paint);
    mBatch.draw(mTexture, 0, 0, mViewport.getWorldWidth(), mViewport.getWorldHeight());
  }

  /**
   * Fills the specified bounds of this canvas with the given paint.
   *
   * <p>Note: The bounds are specified assuming the y-axis is pointing down,
   * that is, {@code (0, 0)} is located in the top left of the screen.
   *
   * @param l     The left edge, in pixels
   * @param t     The top edge, in pixels
   * @param r     The right edge, in pixels
   * @param b     The bottom edge, in pixels
   * @param paint The paint to fill the bounds with
   */
  public void drawRect(float l, float t, float r, float b, @NonNull Paint paint) {
    preparePixmap(paint);
    mBatch.draw(mTexture, l, t, r - l, b - t);
  }

  /**
   * Draws the specified texture, stretching it to fill the given bounds.
   *
   * @param texture The texture to render
   * @param l       The left edge, in pixels
   * @param t       The top edge, in pixels
   * @param r       The right edge, in pixels
   * @param b       The bottom edge, in pixels
   * @param paint   The paint used to tint the texture, or {@code null} for no tint
   */
  public void draw(@NonNull Texture texture, float l, float t, float r, float b,
                   @Nullable Paint paint) {
    prepareTint(paint);
    mBatch.draw(texture, l, t, r - l, b - t, 0, 0, 1, 1, false, true);
  }
}
