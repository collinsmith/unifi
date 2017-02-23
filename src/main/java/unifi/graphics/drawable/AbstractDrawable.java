package unifi.graphics.drawable;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import unifi.graphics.Insets;
import unifi.graphics.Rect;
import unifi.util.StateSet;
import unifi.view.View;

/**
 * Abstract implementation of a {@link Drawable}.
 *
 * @see Drawable
 */
public abstract class AbstractDrawable implements Drawable {
  private static final Rect ZERO_BOUNDS_RECT = new Rect();

  /**
   * Set of primitive {@code int} values representing the state of this drawable
   */
  @NonNull
  private int[] mStateSet = StateSet.WILD_CARD;

  /**
   * Indicates the level of this drawable.
   *
   * @see #setLevel(int)
   */
  @IntRange(from = 0, to = 10000)
  private int mLevel = 0;

  /**
   * Bounds of this drawable, lazily initialized as a new {@link Rect} when
   * required.
   */
  @NonNull
  private Rect mBounds = ZERO_BOUNDS_RECT;

  /**
   * Weak reference to the callback associated with this drawable.
   */
  private WeakReference<Callback> mCallback = null;

  /**
   * Indicates whether or not this drawable is visible.
   *
   * @see #setVisible(boolean, boolean)
   */
  private boolean mVisible = true;

  /**
   * The resolved layout direction of this drawable.
   */
  @View.ResolvedLayoutDir
  private int mLayoutDirection;

  @Override
  public void setCallback(@NonNull Callback callback) {
    mCallback = new WeakReference<>(callback);
  }

  @Nullable
  @Override
  public Callback getCallback() {
    if (mCallback != null) {
      return mCallback.get();
    }

    return null;
  }

  @Override
  public void invalidateSelf() {
    final Callback callback = getCallback();
    if (callback != null) {
      callback.invalidateDrawable(this);
    }
  }

  @Override
  public void scheduleSelf(@NonNull Runnable what, long when) {
    final Callback callback = getCallback();
    if (callback != null) {
      callback.scheduleDrawable(this, what, when);
    }
  }

  @Override
  public void unscheduleSelf(@NonNull Runnable what) {
    final Callback callback = getCallback();
    if (callback != null) {
      callback.unscheduleDrawable(this, what);
    }
  }

  @Override
  public void setBounds(int left, int top, int right, int bottom) {
    Rect oldBounds = mBounds;
    if (oldBounds == ZERO_BOUNDS_RECT) {
      oldBounds = mBounds = new Rect();
    }

    if (oldBounds.left != left || oldBounds.top != top
        || oldBounds.right != right || oldBounds.bottom != bottom) {
      if (!oldBounds.isEmpty()) {
        invalidateSelf();
      }

      mBounds.set(left, top, right, bottom);
      onBoundsChange(mBounds);
    }
  }

  @Override
  public void setBounds(@NonNull Rect bounds) {
    setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
  }

  @Override
  public final void copyBounds(@NonNull Rect dst) {
    dst.set(mBounds);
  }

  @Override
  public final Rect copyBounds() {
    return new Rect(mBounds);
  }

  @NonNull
  @Override
  public final Rect getBounds() {
    if (mBounds == ZERO_BOUNDS_RECT) {
      mBounds = new Rect();
    }

    return mBounds;
  }

  /**
   * Called when the bounds of this drawable has changed. Override this method
   * if the drawables appearance is based on the bounds.
   *
   * <p>Note: For efficiency, the passed {@code bounds} is the same object
   * stored in the drawable. <strong>You should not modify the object passed
   * into this method.</strong>
   *
   * @see #setBounds(int, int, int, int)
   * @see #setBounds(Rect)
   */
  protected void onBoundsChange(@NonNull Rect bounds) {}

  /**
   * {@inheritDoc}
   * <p>
   * <p>Note: By default, this returns the {@linkplain #getBounds full drawable
   * bounds}. Custom drawables may override this method to perform more precise
   * invalidation.
   */
  @NonNull
  @Override
  public Rect getDirtyBounds() {
    return getBounds();
  }

  @Override
  public boolean getPadding(@NonNull Rect dst) {
    dst.set(0, 0, 0, 0);
    return false;
  }

  @NonNull
  @Override
  public Insets getOpticalInsets() {
    return Insets.NONE;
  }

  @Override
  @View.ResolvedLayoutDir
  public int getLayoutDirection() {
    return mLayoutDirection;
  }

  @Override
  public void setLayoutDirection(@View.ResolvedLayoutDir int layoutDirection) {
    // TODO: This seems like an odd choice, may be due to better compat.
    if (getLayoutDirection() != layoutDirection) {
      mLayoutDirection = layoutDirection;
    }
  }

  @Override
  public int getAlpha() {
    return 0xFF;
  }

  @Override
  public boolean isStateful() {
    return false;
  }

  @Override
  public boolean setState(@NonNull int[] stateSet) {
    if (!Arrays.equals(mStateSet, stateSet)) {
      mStateSet = stateSet;
      return onStateChange(stateSet);
    }

    return false;
  }

  @NonNull
  @Override
  public int[] getState() {
    return mStateSet;
  }

  @Override
  public void jumpToCurrentState() {}

  /**
   * Called when the state of this drawable has changed. Override this method
   * if the drawable recognizes the specified state.
   *
   * @param state The new state of this drawable
   *
   * @return {@code true} if the appearance of the drawable has changed as a
   *         result of this new state set (that is, it needs to be drawn),
   *         {@code false} otherwise
   *
   * @see #setState(int[])
   */
  protected boolean onStateChange(@NonNull int[] state) {
    return false;
  }

  @NonNull
  @Override
  public Drawable getCurrent() {
    return this;
  }

  @Override
  public final int getLevel() {
    return mLevel;
  }

  @Override
  public final boolean setLevel(@IntRange(from = 0, to = 10000) int level) {
    // TODO: Validate the range? Android specifies [0..10000], but doesn't enforce
    if (mLevel != level) {
      mLevel = level;
      return onLevelChange(level);
    }

    return false;
  }

  /**
   * Called when the level of this drawable changed. Override this method if
   * the drawable changes appearance based on level.
   *
   * @return {@code true} if the appearance of the drawable has changed as a
   *         result of this new level (that is, it needs to be drawn),
   *         {@code false} otherwise
   *
   * @see #setLevel(int)
   */
  // TODO: level range is not currently enforced, so I don't want to place that restriction here
  protected boolean onLevelChange(int level) {
    return false;
  }

  @Override
  public boolean setVisible(boolean visible, boolean restart) {
    boolean changed = mVisible != visible;
    if (changed) {
      mVisible = visible;
      invalidateSelf();
    }

    return changed;
  }

  @Override
  public final boolean isVisible() {
    return mVisible;
  }

  @Override
  public int getIntrinsicWidth() {
    return -1;
  }

  @Override
  public int getIntrinsicHeight() {
    return -1;
  }

  @Override
  public int getMinimumWidth() {
    final int intrinsicWidth = getIntrinsicWidth();
    return intrinsicWidth > 0 ? intrinsicWidth : 0;
  }

  @Override
  public int getMinimumHeight() {
    final int intrinsicHeight = getIntrinsicHeight();
    return intrinsicHeight > 0 ? intrinsicHeight : 0;
  }
}
