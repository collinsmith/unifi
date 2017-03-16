package unifi.graphics.drawable;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import unifi.graphics.Canvas;
import unifi.graphics.Insets;
import unifi.graphics.PixelFormat;
import unifi.graphics.Rect;
import unifi.util.LayoutDirection;
import unifi.view.View;

import static unifi.graphics.PixelFormat.Opacity;

/**
 * A {@code Drawable} is a general abstraction for "something that can be
 * drawn."  Most often you will deal with Drawable as the type of resource
 * retrieved for drawing things to the screen; Drawable provides an interface
 * for dealing with an underlying visual resource that may take a variety of
 * forms. Unlike a {@link unifi.view.View}, a Drawable does not have any
 * facility to receive events or otherwise interact with the user.
 *
 * <p>In addition to simple drawing, Drawable provides a number of generic
 * mechanisms for its client to interact with what is being drawn:
 *
 * <ul>
 *   <li> The {@link #setBounds} method <var>must</var> be called to tell the
 *   Drawable where it is drawn and how large it should be. All Drawables should
 *   respect the requested size, often simply by scaling their imagery. A client
 *   can find the preferred size for some Drawables with the
 *   {@link #getIntrinsicHeight} and {@link #getIntrinsicWidth} methods.
 *
 *   <li> The {@link #getPadding} method can return from some Drawables
 *   information about how to frame content that is placed inside of them. For
 *   example, a Drawable that is intended to be the frame for a button widget
 *   would need to return padding that correctly places the label inside of
 *   itself.
 *
 *   <li> The {@link #setState} method allows the client to tell the Drawable in
 *   which state it is to be drawn, such as "focused", "selected", etc. Some
 *   drawables may modify their imagery based on the selected state.
 *
 *   <li> The {@link #setLevel} method allows the client to supply a single
 *   continuous controller that can modify the Drawable is displayed, such as a
 *   battery level or progress level. Some drawables may modify their imagery
 *   based on the current level.
 *
 *   <li> A Drawable can perform animations by calling back to its client
 *   through the {@link Callback} interface.  All clients should support this
 *   interface (via {@link #setCallback}) so that animations will work. A simple
 *   way to do this is through the system facilities such as
 *   {@link unifi.view.View#setBackground(Drawable)} and
 *   {@link unifi.widget.ImageView}.
 * </ul>
 *
 * TODO: Much of this documentation is irrelevant, however it's here until it potentially is
 * Though usually not visible to the application, Drawables may take a variety
 * of forms:
 *
 * <ul>
 *   <li> <b>Bitmap</b>: the simplest Drawable, a PNG or JPEG image.
 *   <li> <b>Nine Patch</b>: an extension to the PNG format allows it to specify
 *   information about how to stretch it and place things inside of it.
 *   <li> <b>Shape</b>: contains simple drawing commands instead of a raw
 *   bitmap, allowing it to resize better in some cases.
 *   <li> <b>Layers</b>: a compound drawable, which draws multiple underlying
 *   drawables on top of each other.
 *   <li> <b>States</b>: a compound drawable that selects one of a set of
 *   drawables based on its state.
 *   <li> <b>Levels</b>: a compound drawable that selects one of a set of
 *   drawables based on its level.
 *   <li> <b>Scale</b>: a compound drawable with a single child drawable, whose
 *   overall size is modified based on the current level.
 * </ul>
 */
public interface Drawable {
  /**
   * Draws this drawable onto the passed {@code canvas}.
   */
  void draw(@NonNull Canvas canvas);

  /**
   * Makes this drawable mutable. This operation cannot be reversed. A mutable
   * drawable is guaranteed to not share its state with any other drawable.
   *
   * <p>This method can be used when it becomes necessary to modify the
   * properties of drawables loaded from resources. By default, all drawables
   * loaded from resources share a common state; if you modify the state of one
   * instance, the change will propagate to all other instances.
   *
   * <p>Note: Calling this method on a mutable drawable will have no effect.
   *
   * @see ConstantState
   * @see #getConstantState()
   */
  @NonNull
  Drawable mutate();

  /**
   * Clears the mutated state, allowing this drawable to be cached and mutated
   * again.
   *
   * <p>This method is inherited from <a href="http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.1.1_r1/android/graphics/drawable/Drawable.java#Drawable.clearMutated%28%29">
   * android.graphics.drawable.Drawable</a> and used internally, and should
   * generally not be used.
   */
  void clearMutated();

  /**
   * Binds the specified {@link Callback} to this drawable. This is required for
   * clients who want to support animated drawables.
   */
  void setCallback(@NonNull Callback callback);

  /**
   * Returns the current {@link Callback} implementation attached to this
   * drawable.
   */
  @Nullable
  Callback getCallback();

  /**
   * Invalidates this drawable using the current {@linkplain #getCallback
   * callback}. An invalid drawable will need to be redrawn. If there is no
   * current callback attached to this drawable (i.e., {@code getCallback()}
   * returns {@code null}), then this method does nothing.
   *
   * @see Callback#invalidateDrawable(Drawable)
   * @see #getCallback()
   * @see #setCallback(Callback)
   */
  void invalidateSelf();

  /**
   * Schedules this drawable using the current {@linkplain #getCallback
   * callback}. If there is no current callback attached to this drawable (i.e.,
   * {@code getCallback()} returns {@code null}), then this method does nothing.
   *
   * @param what The action being scheduled
   * @param when The time (in milliseconds) to run
   */
  void scheduleSelf(@NonNull Runnable what, long when);

  /**
   * Unschedules this drawable using the current {@linkplain #getCallback
   * callback}. If there is no current callback attached to this drawable (i.e.,
   * {@code getCallback()} returns {@code null}), then this method does nothing.
   *
   * @param what The runnable that you no longer want called
   */
  void unscheduleSelf(@NonNull Runnable what);

  /**
   * Specifies the bounding rectangle for this drawable. This is where the
   * drawable will draw when {@link #draw} is called. All values are relative to
   * the top left of the canvas this drawable will draw itself on.
   */
  void setBounds(int left, int top, int right, int bottom);

  /**
   * Specifies the bounding rectangle for this drawable. This is where the
   * drawable will draw when {@link #draw} is called.
   *
   * <p>Note: The bounds are only copied from {@code bounds}, so no reference is
   * kept.
   */
  void setBounds(@NonNull Rect bounds);

  /**
   * Copies the bounds of this drawable into the specified {@code dst} rectangle
   * (allocated by the caller). The bounds specify where this drawable will draw
   * when its {@link #draw} method is called.
   *
   * <p>Note: {@code dst} is only a copy and will not update as this drawable's
   * bounds change.
   *
   * @see #copyBounds()
   */
  void copyBounds(@NonNull Rect dst);

  /**
   * Returns a copy of the bounds of this drawable in a newly allocated
   * rectangle. This returns the same values as {@link #getBounds}, but the
   * returned object is guaranteed to not be changed later by the drawable
   * (i.e., it retains no reference to this drawable's bounding rectangle).
   * If the caller already has a {@link Rect} allocated, use
   * {@link #copyBounds(Rect)} instead.
   *
   * @see #copyBounds(Rect)
   */
  Rect copyBounds();

  /**
   * Returns the bounds of this drawable as a {@link Rect}.
   *
   * <p>Note: For efficiency, the returned object may be the same object stored
   * in the drawable (though this is not guaranteed), so if a persistent copy of
   * the bounds is needed, call {@link #copyBounds(Rect)} instead.
   *
   * <p>Note: <strong>You should not modify the object returned by this method,
   * as it may be the same object stored in the drawable.</strong>
   */
  @NonNull
  Rect getBounds();

  /**
   * Returns this drawable's dirty bounds as a {@link Rect}.
   *
   * <p>Note: For efficiency, the returned object may be the same object stored
   * in the drawable (though this is not guaranteed).
   */
  @NonNull
  Rect getDirtyBounds();

  /**
   * Populates the specified rectangle with the insets suggested by this
   * drawable for placing content inside the its bounds. Positive values move
   * toward the center of the drawable.
   *
   * <p>Note: If {@code true} is returned, then this drawable has no padding,
   * i.e., the padding on each side is equal to {@code 0}.
   *
   * @return {@code true} if this drawable actually has padding, {@code false}
   *         otherwise
   *
   * @see Rect#inset(int, int)
   */
  boolean getPadding(@NonNull Rect dst);

  /**
   * Returns the insets suggested by this drawable for use with alignment
   * operations during layout.
   */
  @NonNull
  Insets getOpticalInsets();

  /**
   * Gets the opacity/transparency of this drawable. The returned value is one
   * of the abstract format constants in {@link unifi.graphics.PixelFormat}:
   * {@link PixelFormat#UNKNOWN}, {@link PixelFormat#TRANSLUCENT},
   * {@link PixelFormat#TRANSPARENT}, or {@link PixelFormat#OPAQUE}.
   *
   * <p>Generally a drawable should be as conservative as possible with the
   * value it returns. For example, if it contains multiple child drawables and
   * only shows one of them at a time, if only one of the children is {@code
   * TRANSLUCENT} and the others are {@code OPAQUE} then {@code TRANSLUCENT}
   * should be returned. You can use {@link PixelFormat#resolveOpacity} to
   * perform a standard reduction of two opacities to the appropriate single
   * output.
   *
   * TODO: Document setColorFilter?:
   * <p>Note that the returned value does <em>not</em> take into account a
   * custom alpha or color filter that has been applied by the client through
   * the {@link #setAlpha} or {@link #setColorFilter} methods.
   *
   * @return The opacity pixel format of the drawable.
   *
   * @see unifi.graphics.PixelFormat
   */
  @Opacity
  int getOpacity();

  /**
   * Returns the resolved layout direction for this drawable.
   *
   * @return One of {@link LayoutDirection#LTR}, {@link LayoutDirection#RTL}
   */
  @LayoutDirection.Resolved
  int getLayoutDirection();

  /**
   * Sets the layout direction for this drawable. {@code layoutDirection} should
   * already be resolved -- it is not the drawables responsibility to perform
   * this resolution.
   *
   * @param layoutDirection One of {@link LayoutDirection#LTR},
   *                               {@link LayoutDirection#RTL}
   */
  void setLayoutDirection(@LayoutDirection.Resolved int layoutDirection);

  /**
   * Gets the alpha value for this drawable in the range {@code [0..255]}, where
   * {@code 0} indicates fully transparent and {@code 255} indicates fully
   * opaque.
   */
  @IntRange(from = 0, to = 255)
  int getAlpha();

  /**
   * Sets the alpha value for the drawable in the range {@code [0..255]}, where
   * {@code 0} indicates fully transparent and {@code 255} indicates fully
   * opaque.
   */
  void setAlpha(@IntRange(from = 0, to = 255) int alpha);

  /**
   * Indicates whether or not this drawable will change its appearance based on
   * its state. Clients can use this to determine whether it is necessary to
   * calculate their state and call {@link #setState(int[])}.
   */
  boolean isStateful();

  /**
   * Specifies a new set of states for the drawable. These are use-case
   * specific, so see the relevant documentation.
   * TODO: Document Button states as example
   *
   * <p>If the new {@code stateSet} being applied causes the appearance of the
   * drawable to change, then it is responsible for calling
   * {@link #invalidateSelf()} in order do have itself redrawn and this method
   * should return {@code true}.
   *
   * <p>Note: The drawable keeps a reference to {@code stateSet} until a new
   * state set is specified via another call to this method, so you <strong>must
   * not modify this array during that time</strong>.
   *
   * @param stateSet The new set of states to apply
   *
   * @return {@code true} if the appearance of the drawable changes as a result
   *         of this operation, {@code false} otherwise
   */
  boolean setState(@NonNull int[] stateSet);

  /**
   * Describes the current state, as a union of primitive states. Some drawables
   * may modify their imagery based on the selected state.
   *
   * @return An array of resource IDs describing the current state
   */
  @NonNull
  int[] getState();

  /**
   * Asks the drawable to immediately jump to the current state and skip any
   * active animations if it does transition animations between states.
   */
  void jumpToCurrentState();

  /**
   * TODO: Document StateListDrawable and LevelListDrawable
   * The current drawable that will be used by this drawable. For simple
   * drawables, this is just the drawable itself, however for drawable that
   * change state, this will be the child drawable currently in use.
   */
  @NonNull
  Drawable getCurrent();

  /**
   * Specifies the level for the drawable. This allows a drawable to vary its
   * imagery based on a continuous controller, for example to show progress or
   * volume level.
   *
   * <p>If the new level you are supplying causes the appearance of the drawable
   * to change, then it is responsible for calling {@link #invalidateSelf()} in
   * order to have itself redrawn, and this method should return {@code true}.
   *
   * @param level The new level, in the range {@code [0..10000]}
   *
   * @return {@code true} if the appearance of this drawable changed as a result
   *         of this method, {@code false} otherwise
   */
  boolean setLevel(@IntRange(from = 0, to = 10000) int level);

  /**
   * Returns the current level of the drawable in the range {@code [0..10000]}.
   *
   * @see #setLevel(int)
   */
  @IntRange(from = 0, to = 10000)
  int getLevel();

  /**
   * Sets whether or not this drawable is visible. This generally does not
   * impact the drawable's behavior, but is a hint that can be used by some
   * drawables, for example, to decide whether or not to run animations.
   *
   * @param visible {@code true} to set this drawable as visible
   * @param restart {@code true} to force the drawable to behave as if it has
   *                just become visible, even if it had last been visible. This
   *                can be used to force animations to restart.
   *
   * @return {@code true} if the new visibility is different from its previous
   *         state, {@code false} otherwise
   */
  boolean setVisible(boolean visible, boolean restart);

  /**
   * Indicates whether or not this drawable is visible.
   */
  boolean isVisible();

  /**
   * Returns the intrinsic width of the underlying drawable object, or {@code
   * -1} if it has no intrinsic width, such as with a solid color.
   */
  int getIntrinsicWidth();

  /**
   * Returns the intrinsic height of the underlying drawable object, or {@code
   * -1} if it has no intrinsic height, such as with a solid color.
   */
  int getIntrinsicHeight();

  /**
   * Returns the minimum width suggested by this drawable. If this drawable is
   * used as a background, it is suggested that the parent use at least this
   * value for its width.
   *
   * <p>Note: There will be some scenarios where this will not be possible.
   * <p>Note: This value should <em>include</em> any padding.
   *
   * @return The minimum width suggested by this drawable, or {@code 0} if this
   *         drawable doesn't have a suggested minimum width
   */
  int getMinimumWidth();

  /**
   * Returns the minimum height suggested by this drawable. If this drawable is
   * used as a background, it is suggested that the parent use at least this
   * value for its height.
   * <p>
   * <p>Note: There will be some scenarios where this will not be possible.
   * <p>Note: This value should <em>include</em> any padding.
   *
   * @return The minimum height suggested by this drawable, or {@code 0} if this
   *         drawable doesn't have a suggested minimum height
   */
  int getMinimumHeight();

  /**
   * Returns the constant state instance that holds the shared state of this
   * drawable.
   *
   * @see ConstantState
   * @see #mutate()
   */
  @Nullable
  ConstantState getConstantState();

  /**
   * Defines the behavior of an animated drawable. Upon retrieving a drawable,
   * use {@link Drawable#setCallback} to supply your implementation of this
   * interface to the drawable; it uses this interface to schedule and execute
   * animation changes.
   */
  interface Callback {
    /**
     * Called when the drawable needs to be redrawn. A parent should invalidate
     * itself at this point (or at least the part of itself where the drawable
     * appears).
     *
     * @param who The drawable initiating the request
     */
    void invalidateDrawable(@NonNull Drawable who);

    /**
     * Schedules the next frame of the drawable's animation. An implementation
     * can generally call {@link unifi.runtime.Timer#postAtTime(Runnable,
     * Object, long)} with the parameters {@code what, who, when} to perform the
     * scheduling.
     *
     * @param who  The drawable being scheduled
     * @param what The action to execute
     * @param when The time (in milliseconds) to run. Timebase is {@link
     *             unifi.runtime.SystemClock#millisTime()}
     */
    void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when);

    /**
     * Unschedules an action previously scheduled with
     * {@link #scheduleDrawable}. An implementation can generally call
     * {@link unifi.runtime.Timer#removeCallbacks(Runnable, Object)} with the
     * parameters {@code what, who} to unschedule the drawable.
     *
     * @param who  The drawable being unscheduled
     * @param what The action being unscheduled
     */
    void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what);
  }

  /**
   * Abstract class used by drawables to store shared constant state and data
   * between drawable instances.
   */
  interface ConstantState {}
}
