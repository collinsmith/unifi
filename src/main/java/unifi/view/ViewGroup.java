package unifi.view;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.badlogic.gdx.Input;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import unifi.content.Context;
import unifi.graphics.Canvas;
import unifi.graphics.Color;
import unifi.graphics.Insets;
import unifi.graphics.Paint;
import unifi.graphics.Rect;
import unifi.util.FocusDirection;
import unifi.util.LayoutDirection;
import unifi.util.Log;

public abstract class ViewGroup extends View implements ViewManager, ViewParent {
  private static final boolean DBG = true;

  @NonNull
  protected static final String TAG = "ViewGroup";

  protected static boolean DEBUG_DRAW = true;

  @Nullable private static Paint sDebugPaint;

  //region @Focusability
  @IntDef(flag = true,
      value = {
          FOCUS_BEFORE_DESCENDANTS,
          FOCUS_AFTER_DESCENDANTS
      })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Focusability {}
  //endregion

  //region LayoutModes
  private static final int LAYOUT_MODE_UNDEFINED = -1;

  /**
   * This constant is a {@link #setLayoutMode(int) layoutMode}. Clip bounds are
   * the raw values of {@link #getLeft() left}, {@link #getTop() top}, {@link
   * #getRight() right} and {@link #getBottom() bottom}.
   */
  public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;

  /**
   * This constant is a {@link #setLayoutMode(int) layoutMode}. Optical bounds
   * describe where a widget appears to be. They sit inside the clip bounds
   * which need to cover a larger area to allow other effects, such as shadows
   * and glows, to be drawn.
   */
  public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;

  public static int LAYOUT_MODE_DEFAULT = LAYOUT_MODE_CLIP_BOUNDS;
  //endregion

  /**
   * Either {@link #LAYOUT_MODE_CLIP_BOUNDS} or {@link #LAYOUT_MODE_OPTICAL_BOUNDS}.
   */
  private int mLayoutMode = LAYOUT_MODE_UNDEFINED;

  //region mGroupFlags Flags
  /**
   * Indicates that the view group invalidates only the child's rectangle.
   * Set by default
   */
  static final int FLAG_CLIP_CHILDREN = 0x00000001;

  /**
   * Indicates that the view group excludes the padding area from the invalidate
   * rectangle. Set by default
   */
  private static final int FLAG_CLIP_TO_PADDING = 0x00000002;

  /**
   * Indicates that this view group has padding; if unset there is no padding and
   * we don't need to clip to it, even if {@link #FLAG_CLIP_TO_PADDING} is set
   */
  private static final int FLAG_PADDING_NOT_NULL = 0x00000004;

  /**
   * We clip to padding when {#link #FLAG_CLIP_TO_PADDING} and
   * {@link #FLAG_PADDING_NOT_NULL} are set at the same time.
   */
  protected static final int CLIP_TO_PADDING_MASK = FLAG_CLIP_TO_PADDING | FLAG_PADDING_NOT_NULL;

  /**
   * Indicates whether or not {@link #dispatchDraw} will invoke {@link #invalidate};
   * This is set by {@link #drawChild} when a child needs to be invalidated and
   * {@link #FLAG_OPTIMIZE_INVALIDATE} is set.
   */
  static final int FLAG_INVALIDATE_REQUIRED = 0x00000008;

  //region FLAG_MASK_FOCUSABILITY
  /**
   * Mask of all focus flags
   */
  private static final int FLAG_MASK_FOCUSABILITY = 0x00060000;

  /**
   * This view will get focus before any of its descendants.
   */
  public static final int FOCUS_BEFORE_DESCENDANTS = 0x00020000;

  /**
   * This view will get focus only if none of its descendants want it.
   */
  public static final int FOCUS_AFTER_DESCENDANTS = 0x00040000;

  /**
   * This view will block any of its descendants from getting focus, even if
   * they are focusable.
   */
  public static final int FOCUS_BLOCK_DESCENDANTS = 0x00060000;
  //endregion

  /**
   * This view will not permit focus to enter this group if a touchscreen is present.
   */
  static final int FLAG_TOUCHSCREEN_BLOCKS_FOCUS = 0x04000000;
  //endregion

  /**
   * Internal flags representing part of the state of a view group.
   */
  int mGroupFlags;

  //region mChildren
  private static final int ARRAY_INITIAL_CAPACITY = 12;
  private static final int ARRAY_CAPACITY_INCREMENT = 12;

  /**
   * Child views of this view group
   */
  private View[] mChildren;

  /**
   * Number of valid children in {@link #mChildren} array, the rest should be\
   * {@code null} or not considered as children
   */
  private int mChildrenCount;
  //endregion

  /**
   * The view in this group that has focus, or {@code null} if none does
   */
  @Nullable private View mFocused;

  public ViewGroup(@Nullable Context context) {
    super(context);
    initViewGroup();
  }

  private boolean debugDraw() {
    return DEBUG_DRAW || mAttachInfo != null && mAttachInfo.mDebugLayout;
  }

  private void initViewGroup() {
    if (!debugDraw()) {
      setFlags(WILL_NOT_DRAW, DRAW_MASK);
    }

    mGroupFlags |= FLAG_CLIP_CHILDREN;
    mGroupFlags |= FLAG_CLIP_TO_PADDING;

    setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);

    mChildren = new View[ARRAY_INITIAL_CAPACITY];
    mChildrenCount = 0;
  }

  private void setBooleanFlag(int flag, boolean value) {
    if (value) {
      mGroupFlags |= flag;
    } else {
      mGroupFlags &= ~flag;
    }
  }

  //region Children
  /**
   * Returns the position in the group of the specified child view, or {@code -1}
   * if the view does not belong the group.
   */
  public int indexOfChild(@Nullable View child) {
    final int count = mChildrenCount;
    final View[] children = mChildren;
    for (int i = 0; i < count; i++) {
      if (children[i] == child) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Returns the number of children in this group.
   */
  public int getChildCount() {
    return mChildrenCount;
  }

  /**
   * Returns the view at the specified position in the group, or {@code null} if
   * the position does not exist within the group.
   */
  @Nullable
  public View getChildAt(int index) {
    if (index < 0 || index >= mChildrenCount) {
      return null;
    }

    return mChildren[index];
  }

  private void addInArray(@NonNull View child, int index) {
    View[] children = mChildren;
    final int count = mChildrenCount;
    final int size = children.length;
    if (index == count) {
      if (size == count) {
        mChildren = new View[size + ARRAY_CAPACITY_INCREMENT];
        System.arraycopy(children, 0, mChildren, 0, size);
        children = mChildren;
      }

      children[mChildrenCount++] = child;
    } else if (index < count) {
      if (size == count) {
        mChildren = new View[size + ARRAY_CAPACITY_INCREMENT];
        System.arraycopy(children, 0, mChildren, 0, index);
        System.arraycopy(children, index, mChildren, index + 1, count - index);
        children = mChildren;
      } else {
        System.arraycopy(children, index, children, index + 1, count - index);
      }

      children[index] = child;
      mChildrenCount++;
    } else {
      throw new IndexOutOfBoundsException("index=" + index + " count=" + count);
    }
  }

  /**
   * <p>Note: This method also sets the child's parent to {@code null}.
   */
  private void removeFromArray(int index) {
    final View[] children = mChildren;
    children[index].mParent = null;
    final int count = mChildrenCount;
    if (index == count - 1) {
      children[--mChildrenCount] = null;
    } else if (index >= 0 && index < count) {
      System.arraycopy(children, index + 1, children, index, count - index - 1);
      children[--mChildrenCount] = null;
    } else {
      throw new IndexOutOfBoundsException();
    }
  }
  //endregion

  //region addView
  /**
   * Adds a view to this group with the specified layout parameters.
   *
   * <p><strong>Note:</strong> Do not invoke this method from {@link #draw},
   * {@link #onDraw} or any related method.
   *
   * @param child  The view to add to this group
   * @param params The layout parameters used to layout {@code view}, or {@code
   *               null} if no layout params can be provided
   */
  @Override
  public void addView(@NonNull View child, @Nullable LayoutParams params) {
    addView(child, -1, params);
  }

  /**
   * Adds a view to this group. If no layout parameters are already set on the
   * child, the default parameters for this view group are applied.
   *
   * <p><strong>Note:</strong> do not invoke this method from {@link #draw},
   * {@link #onDraw}, {@link #dispatchDraw} or any related method.
   *
   * @param child The view to add
   *
   * @see #generateDefaultLayoutParams()
   */
  public void addView(@NonNull View child) {
    addView(child, -1);
  }

  /**
   * Adds a view to this group. If no layout parameters are already set on the
   * child, the default parameters for this view group are applied.
   *
   * <p><strong>Note:</strong> do not invoke this method from {@link #draw},
   * {@link #onDraw}, {@link #dispatchDraw} or any related method.
   *
   * @param child The view to add
   * @param index The position at which to add the child
   *
   * @see #generateDefaultLayoutParams()
   */
  public void addView(@NonNull View child, int index) {
    if (child == null) {
      throw new IllegalArgumentException("Cannot add a null child to a ViewGroup");
    }

    LayoutParams params = child.getLayoutParams();
    if (params == null) {
      params = generateDefaultLayoutParams();
      if (params == null) {
        throw new AssertionError("generateDefaultLayoutParams() cannot return null");
      }
    }

    addView(child, index, params);
  }

  /**
   * Adds a view with this view's default layout parameters and the specified
   * {@link LayoutParams#width width} and {@link LayoutParams#height height}.
   *
   * <p><strong>Note:</strong> do not invoke this method from {@link #draw},
   * {@link #onDraw}, {@link #dispatchDraw} or any related method.
   *
   * @param child  The view to add
   * @param width  How wide the view wants to be, either {@link LayoutParams#MATCH_PARENT}
   *               or {@link LayoutParams#WRAP_CONTENT}, or a fixed size, in pixels
   * @param height How wide the view wants to be, either {@link LayoutParams#MATCH_PARENT}
   *               or {@link LayoutParams#WRAP_CONTENT}, or a fixed size, in pixels
   */
  public void addView(@NonNull View child, int width, int height) {
    final LayoutParams params = generateDefaultLayoutParams();
    params.width = width;
    params.height = height;
    addView(child, -1, params);
  }

  /**
   * Adds a view to this group with the specified layout parameters.
   *
   * <p><strong>Note:</strong> do not invoke this method from {@link #draw},
   * {@link #onDraw}, {@link #dispatchDraw} or any related method.
   *
   * @param child  The view to add
   * @param index  The position at which to add the view
   * @param params The layout parameters to set on the view
   */
  public void addView(@NonNull View child, int index, @NonNull LayoutParams params) {
    if (DBG) {
      Log.d(TAG, this + " addView");
    }

    if (child == null) {
      throw new IllegalArgumentException("Cannot add a null child to a ViewGroup");
    }

    /**
     * {@link #addViewInner} will call {@link #requestLayout()} on {@param child}
     * when setting the {@code params}, therefore we call {@link #requestLayout()}
     * on ourselves before, so that the child's request will be blocked at our level.
     */
    requestLayout();
    invalidate();
    addViewInner(child, index, params, false);
  }

  private void addViewInner(@NonNull View child, int index, @NonNull LayoutParams params,
                            boolean preventLayoutRequest) {
    if (child.getParent() != null) {
      throw new IllegalStateException("The specified child already has a parent. " +
          "You must call removeView() on the child's parent first.");
    }

    if (!checkLayoutParams(params)) {
      params = generateLayoutParams(params);
    }

    if (preventLayoutRequest) {
      child.mLayoutParams = params;
    } else {
      child.setLayoutParams(params);
    }

    if (index < 0) {
      index = mChildrenCount;
    }

    addInArray(child, index);

    if (preventLayoutRequest) {
      child.assignParent(this);
    } else {
      child.mParent = this;
    }

    if (child.hasFocus()) {
      requestChildFocus(child, child.findFocus());
    }

    if (child.isLayoutDirectionInherited()) {
      child.resetRtlProperties();
    }

    onViewAdded(child);

    // TODO: duplicate parent state
  }

  /**
   * Called when a child view is added to this view group.
   */
  protected void onViewAdded(@NonNull View child) {}
  //endregion

  //region removeView
  /**
   * {@inheritDoc}
   *
   * <p><strong>Note:</strong> Do not invoke this method from {@link #draw},
   * {@link #onDraw} or {@link #dispatchDraw} or any related method.
   *
   * @param view {@inheritDoc}
   *
   * @return {@inheritDoc}
   */
  @Override
  public boolean removeView(@Nullable View view) {
    if (removeViewInternal(view)) {
      requestLayout();
      invalidate();
      return true;
    }

    return false;
  }

  private boolean removeViewInternal(@NonNull View view) {
    final int index = indexOfChild(view);
    if (index >= 0) {
      removeViewInternal(index, view);
      return true;
    }

    return false;
  }

  private void removeViewInternal(int index, @NonNull View view) {
    boolean clearChildFocus = false;
    if (view == mFocused) {
      view.unFocus(null);
      clearChildFocus = true;
    }

    if (view.mAttachInfo != null) {
      view.dispatchDetachedFromWindow();
    }

    removeFromArray(index);

    if (clearChildFocus) {
      clearChildFocus(view);
      if (!rootViewRequestFocus()) {
        notifyGlobalFocusCleared(this);
      }
    }

    onViewRemoved(view);
  }

  /**
   * Called when a child view is removed from this view group.
   */
  protected void onViewRemoved(@NonNull View child) {}
  //endregion

  //region Draw
  /**
   * {@inheritDoc}
   */
  @Override
  protected void dispatchDraw(@NonNull Canvas canvas) {
    final int count = mChildrenCount;
    final View[] children = mChildren;
    int flags = mGroupFlags;

    int clipSaveCount = 0;
    final boolean clipToPadding = (flags & CLIP_TO_PADDING_MASK) == FLAG_CLIP_TO_PADDING;
    if (clipToPadding) {
      clipSaveCount = canvas.save();
      canvas.clipRect(mScrollX + mPaddingLeft, mScrollY + mPaddingTop,
          mScrollX + mRight - mLeft - mPaddingRight,
          mScrollY + mBottom - mTop - mPaddingBottom);
    }

    mGroupFlags &= ~FLAG_INVALIDATE_REQUIRED;

    boolean more = false;
    for (int i = 0; i < count; i++) {
      int childIndex = i;
      final View child = children[childIndex];
      if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE) {
        more |= drawChild(canvas, child);
      }
    }

    if (debugDraw()) {
      drawDebug(canvas);
    }

    if (clipToPadding) {
      canvas.restoreToCount(clipSaveCount);
    }

    flags = mGroupFlags;
    if ((flags & FLAG_INVALIDATE_REQUIRED) == FLAG_INVALIDATE_REQUIRED) {
      invalidate();
    }
  }

  protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child) {
    // TODO: Support boolean draw(Canvas, ViewGroup, long) ?
    child.draw(canvas);
    return false;
  }

  @Override
  protected void drawDebug(@NonNull Canvas canvas) {
    Paint paint = getDebugPaint();

    // Draw optical bounds
    {
      paint.setColor(Color.RED);
      paint.setStyle(Paint.Style.STROKE);
      for (int i = 0; i < getChildCount(); i++) {
        View c = getChildAt(i);
        Insets insets = c.getOpticalInsets();
        drawRect(
            canvas, paint,
            c.getLeft()   + insets.left,
            c.getTop()    + insets.top,
            c.getRight()  - insets.right - 1,
            c.getBottom() - insets.bottom - 1);
      }
    }

    // Draw margins
    {
      paint.setColor(Color.argb(63, 255, 0, 255));
      paint.setStyle(Paint.Style.FILL);
      onDebugDrawMargins(canvas, paint);
    }

    // Draw clip bounds
    {
      paint.setColor(Color.rgb(63, 127, 255));
      paint.setStyle(Paint.Style.FILL);
      int lineLength = 16;
      int lineWidth = 1;
      for (int i = 0; i < getChildCount(); i++) {
        View c = getChildAt(i);
        drawRectCorners(canvas, paint,
            c.getLeft(), c.getTop(), c.getRight(), c.getBottom(),
            lineLength, lineWidth);
      }
    }
  }

  protected void onDebugDrawMargins(@NonNull Canvas canvas, @NonNull Paint paint) {
    for (int i = 0; i < getChildCount(); i++) {
      View c = getChildAt(i);
      c.getLayoutParams().onDrawDebug(c, canvas, paint);
    }
  }

  @NonNull
  private static Paint getDebugPaint() {
    if (sDebugPaint == null) {
      sDebugPaint = new Paint();
    }

    return sDebugPaint;
  }

  private static void drawRect(@NonNull Canvas canvas, @NonNull Paint paint,
                               int x1, int y1, int x2, int y2) {
    canvas.drawRect(x1, y1, x2, y2, paint);
  }

  private static void fillRect(@NonNull Canvas canvas, @NonNull Paint paint,
                               int x1, int y1, int x2, int y2) {
    if (x1 != x2 && y1 != y2) {
      if (x1 > x2) {
        int tmp = x1;
        x1 = x2;
        x2 = tmp;
      }

      if (y1 > y2) {
        int tmp = y1;
        y1 = y2;
        y2 = tmp;
      }

      canvas.drawRect(x1, y1, x2, y2, paint);
    }
  }

  private static int sign(int x) {
    return (x >= 0) ? 1 : -1;
  }

  private static void drawCorner(@NonNull Canvas c, @NonNull Paint paint,
                                 int x1, int y1, int dx, int dy, int lw) {
    fillRect(c, paint, x1, y1, x1 + dx, y1 + lw * sign(dy));
    fillRect(c, paint, x1, y1, x1 + lw * sign(dx), y1 + dy);
  }

  private static void drawRectCorners(@NonNull Canvas canvas, @NonNull Paint paint,
                                      int x1, int y1, int x2, int y2,
                                      int lineLength, int lineWidth) {
    drawCorner(canvas, paint, x1, y1, lineLength, lineLength, lineWidth);
    drawCorner(canvas, paint, x1, y2, lineLength, -lineLength, lineWidth);
    drawCorner(canvas, paint, x2, y1, -lineLength, lineLength, lineWidth);
    drawCorner(canvas, paint, x2, y2, -lineLength, -lineLength, lineWidth);
  }

  private static void fillDifference(@NonNull Canvas canvas, @NonNull Paint paint,
                                     int x2, int y2, int x3, int y3,
                                     int dx1, int dy1, int dx2, int dy2) {
    int x1 = x2 - dx1;
    int y1 = y2 - dy1;

    int x4 = x3 + dx2;
    int y4 = y3 + dy2;

    fillRect(canvas, paint, x1, y1, x4, y2);
    fillRect(canvas, paint, x1, y2, x2, y3);
    fillRect(canvas, paint, x3, y2, x4, y3);
    fillRect(canvas, paint, x1, y3, x4, y4);
  }
  //endregion

  //region Measure
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // TODO: This override isn't present in android.view.ViewGroup, but seems
    //       necessary in this implementation
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    measureChildren(widthMeasureSpec, heightMeasureSpec);
  }

  /**
   * Measures the children of this view group, taking into account the
   * {@link MeasureSpec} requirements for this view and its padding. Children
   * whose {@link View#getVisibility() visibility} are {@link View#GONE} are not
   * measured.
   *
   * @param widthMeasureSpec  The width requirements for this view
   * @param heightMeasureSpec The height requirements for this view
   */
  protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
    final int size = mChildrenCount;
    final View[] children = mChildren;
    for (int i = 0; i < size; i++) {
      View child = children[i];
      if ((child.mViewFlags & VISIBILITY_MASK) != GONE) {
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
      }
    }
  }

  /**
   * Measures the specified child view of this view group, taking into account
   * the {@link MeasureSpec} requirements for this view and its padding.
   *
   * @param child The child to measure
   * @param parentWidthMeasureSpec  The width requirements for this view
   * @param parentHeightMeasureSpec The height requirements for this view
   */
  protected void measureChild(@NonNull View child,
                              int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
    final LayoutParams params = child.getLayoutParams();
    final int childWidthMeasureSpec = getChildMeasureSpec(
        parentWidthMeasureSpec, mPaddingLeft + mPaddingRight, params.width);
    final int childHeightMeasureSpec = getChildMeasureSpec(
        parentHeightMeasureSpec, mPaddingTop + mPaddingBottom, params.height);
    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
  }

  /**
   * Measures the specified child view of this view group, taking into account
   * the {@link MeasureSpec} requirements for this view and its padding. The
   * child must have {@link MarginLayoutParams}.
   *
   * @param child      The child to measure
   * @param parentWidthMeasureSpec The width requirements for this view
   * @param widthUsed  Extra space that has been used up by the parent
   *                   horizontally, (possibly by other children)
   * @param parentHeightMeasureSpec The height requirements for this view
   * @param heightUsed Extra space that has been used up by the parent
   *                   vertically, (possibly by other children)
   */
  protected void measureChildWithMargins(@NonNull View child,
                                         int parentWidthMeasureSpec, int widthUsed,
                                         int parentHeightMeasureSpec, int heightUsed) {
    final MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
    final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
        mPaddingLeft + mPaddingRight + params.leftMargin + params.rightMargin + widthUsed,
        params.width);
    final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
        mPaddingTop + mPaddingBottom + params.topMargin + params.bottomMargin + heightUsed,
        params.height);
    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
  }

  /**
   * Calculates the {@link MeasureSpec} to assign to a child view in a single
   * dimension (width or height).
   *
   * <p>The goal is to combine information from our measure specification with
   * the layout parameters of the child to get the best possible results. For
   * example, if this view knows its size (Because its measure spec mode is
   * {@link MeasureSpec#EXACTLY}), and the child has indicated in its layout
   * parameters that it wants to be the same size of its parent, then the parent
   * should ask the child to layout given an exact size.
   *
   * @param spec           The requirements for this view
   * @param padding        The padding of this view, for the current dimension
   *                       and margins, if applicable
   * @param childDimension How big the child wants to be in the current dimension
   *
   * @return A {@linkplain MeasureSpec MeasureSpec-encoded int} for the child
   */
  public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
    @MeasureSpec.Mode
    int specMode = MeasureSpec.getMode(spec);
    int specSize = MeasureSpec.getSize(spec);

    int size = Math.max(0, specSize - padding);

    int resultSize = 0;
    int resultMode = 0;

    switch (specMode) {
      // Parent has imposed an exact size on us
      case MeasureSpec.EXACTLY:
        if (childDimension >= 0) {
          resultSize = childDimension;
          resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
          // Child wants to be our size. So be it.
          resultSize = size;
          resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
          // Child wants to determine its own size. It can't be
          // bigger than us.
          resultSize = size;
          resultMode = MeasureSpec.AT_MOST;
        }
        break;

      // Parent has imposed a maximum size on us
      case MeasureSpec.AT_MOST:
        if (childDimension >= 0) {
          // Child wants a specific size... so be it
          resultSize = childDimension;
          resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
          // Child wants to be our size, but our size is not fixed.
          // Constrain child to not be bigger than us.
          resultSize = size;
          resultMode = MeasureSpec.AT_MOST;
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
          // Child wants to determine its own size. It can't be
          // bigger than us.
          resultSize = size;
          resultMode = MeasureSpec.AT_MOST;
        }
        break;

      // Parent asked to see how big we want to be
      case MeasureSpec.UNSPECIFIED:
        if (childDimension >= 0) {
          // Child wants a specific size... let him have it
          resultSize = childDimension;
          resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
          // Child wants to be our size... find out how big it should
          // be
          resultSize = 0;
          resultMode = MeasureSpec.UNSPECIFIED;
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
          // Child wants to determine its own size.... find out how
          // big it should be
          resultSize = 0;
          resultMode = MeasureSpec.UNSPECIFIED;
        }
        break;
    }

    return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
  }
  //endregion

  //region Layout
  /**
   * {@inheritDoc}
   */
  @Override
  public final void layout(int l, int t, int r, int b) {
    super.layout(l, t, r, b);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected abstract void onLayout(boolean changed, int left, int top, int right, int bottom);

  /**
   * Indicates if this view group is laying out using optical bounds.
   */
  boolean isLayoutModeOptical() {
    return mLayoutMode == LAYOUT_MODE_OPTICAL_BOUNDS;
  }

  @NonNull
  @Override
  Insets computeOpticalInsets() {
    if (isLayoutModeOptical()) {
      int left = 0;
      int top = 0;
      int right = 0;
      int bottom = 0;
      for (int i = 0; i < mChildrenCount; i++) {
        View child = mChildren[i];
        if (child.getVisibility() == VISIBLE) {
          Insets insets = child.getOpticalInsets();
          left = Math.max(left, insets.left);
          top = Math.max(top, insets.top);
          right = Math.max(right, insets.right);
          bottom = Math.max(bottom, insets.bottom);
        }
      }

      return Insets.of(left, top, right, bottom);
    }

    return Insets.NONE;
  }

  @Override
  public void updateViewLayout(@NonNull View view, @NonNull LayoutParams params) {
    if (!checkLayoutParams(params)) {
      throw new IllegalArgumentException("Invalid LayoutParams supplied to " + this);
    }

    if (view.mParent != this) {
      throw new IllegalArgumentException("Given view not a child of " + this);
    }

    view.setLayoutParams(params);
  }
  //endregion

  //region Invalidate
  /**
   * Don't call or override this method. It is used for the implementation of
   * the view hierarchy.
   */
  @Override
  public void invalidateChild(@NonNull View child, @Nullable Rect r) {//TODO: Implement
  }

  /**
   * Don't call or override this method. It is used for the implementation of
   * the view hierarchy.
   *
   * This implementation returns {@code null} if this view group does not have a
   * parent, if this view group is already fully invalidated or if the dirty
   * rectangle does not intersect with this view group's bounds.
   */
  @Nullable
  @Override
  public ViewParent invalidateChildInParent(@NonNull @Size(value = 2) int[] location, @NonNull Rect dirty) {//TODO: Implement
    return null;
  }
  //endregion

  //region Focus
  /**
   * Indicates whether or not this view group should ignore focus requests for
   * itself and its children.
   */
  public boolean getTouchscreenBlocksFocus() {
    return (mGroupFlags & FLAG_TOUCHSCREEN_BLOCKS_FOCUS) != 0;
  }

  /**
   * Sets whether or not this view group should ignore focus requests for itself
   * and its children.
   *
   * <p>If this option is enabled and the view group or a descendant currently
   * has focus, then focus will proceed {@linkplain FocusDirection#FORWARD forward}.
   */
  public void setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus) {
    if (touchscreenBlocksFocus) {
      mGroupFlags |= FLAG_TOUCHSCREEN_BLOCKS_FOCUS;
      if (hasFocus()) {
        View focusedChild = getDeepestFocusedChild();
        if (!focusedChild.isFocusableInTouchMode()) {
          View newFocus = focusSearch(FocusDirection.DOWN);
          if (newFocus != null) {
            newFocus.requestFocus();
          }
        }
      }
    } else {
      mGroupFlags &= ~FLAG_TOUCHSCREEN_BLOCKS_FOCUS;
    }
  }

  boolean shouldBlockFocusForTouchscreen() {
    //FIXME: This needs further validation, MultitouchScreen is subset of Touchscreen
    return getTouchscreenBlocksFocus()
        && mContext.getInput().isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
  }

  /**
   * Indicates the descendant focusability of this view group. The descendant
   * focusability defines the relationship between this view group and its
   * descendants when looking for a view to focus in {@link #requestFocus(int, Rect)}
   */
  @Focusability
  public int getDescendantFocusability() {
    return mGroupFlags & FLAG_MASK_FOCUSABILITY;
  }

  /**
   * Sets the descendant focusability of this view group. This defines the
   * relationship between this view group and its descendants when looking for a
   * view to take focus in {@link #requestFocus(int, Rect)}.
   *
   * @param focusability One of {@link #FOCUS_BEFORE_DESCENDANTS}, {@link #FOCUS_AFTER_DESCENDANTS},
   *                     {@link #FOCUS_BLOCK_DESCENDANTS}
   */
  public void setDescendantFocusability(@Focusability int focusability) {
    switch (focusability) {
      case FOCUS_BEFORE_DESCENDANTS:
      case FOCUS_AFTER_DESCENDANTS:
      case FOCUS_BLOCK_DESCENDANTS:
        mGroupFlags &= ~FLAG_MASK_FOCUSABILITY;
        mGroupFlags |= (focusability & FLAG_MASK_FOCUSABILITY);
        break;
      default:
        throw new IllegalArgumentException("Must be one of FOCUS_BEFORE_DESCENDANTS, " +
            "FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS");
    }
  }

  @Override
  void handleFocusGainInternal(@FocusDirection.Real int direction,
                               @Nullable Rect previouslyFocusedRect) {
    if (mFocused != null) {
      mFocused.unFocus(this);
      mFocused = null;
    }

    super.handleFocusGainInternal(direction, previouslyFocusedRect);
  }

  @Override
  public void requestChildFocus(@NonNull View child, @NonNull View focused) {
    if (DBG) {
      Log.d(TAG, this + " requestChildFocus()");
    }

    if ((mGroupFlags & FLAG_MASK_FOCUSABILITY) == FOCUS_BLOCK_DESCENDANTS) {
      return;
    }

    super.unFocus(focused);
    if (mFocused != child) {
      if (mFocused != null) {
        mFocused.unFocus(focused);
      }

      mFocused = child;
    }

    if (mParent != null) {
      mParent.requestChildFocus(this, focused);
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>Looks for a view to give focus to resprcting the setting specified by
   * {@link #getDescendantFocusability()}.
   *
   * <p>Uses {@link #onRequestFocusInDescendants} to find focus within the
   * children of this group when appropriate.
   *
   * @see #FOCUS_BLOCK_DESCENDANTS
   * @see #FOCUS_BEFORE_DESCENDANTS
   * @see #FOCUS_AFTER_DESCENDANTS
   * @see #onRequestFocusInDescendants(int, Rect)
   */
  @Override
  public boolean requestFocus(@FocusDirection.Real int direction,
                              @Nullable Rect previouslyFocusedRect) {
    if (DBG) {
      Log.d(TAG, this + " ViewGroup.requestFocus direction=" + direction);
    }

    int descendantFocusability = mGroupFlags & FLAG_MASK_FOCUSABILITY;
    switch (descendantFocusability) {
      case FOCUS_BLOCK_DESCENDANTS:
        return super.requestFocus(direction, previouslyFocusedRect);
      case FOCUS_BEFORE_DESCENDANTS: {
        final boolean took = super.requestFocus(direction, previouslyFocusedRect);
        return took ? took : onRequestFocusInDescendants(direction, previouslyFocusedRect);
      }
      case FOCUS_AFTER_DESCENDANTS: {
        final boolean took = super.requestFocus(direction, previouslyFocusedRect);
        return took ? took : onRequestFocusInDescendants(direction, previouslyFocusedRect);
      }
      default:
        throw new IllegalStateException("descendant focusability must be one of " +
            "FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS " +
            "but is " + descendantFocusability);
    }
  }

  /**
   * Looks for a descendant to call {@link View#requestFocus} on.
   *
   * <p>Called by {@link ViewGroup#requestFocus} when it wants to request focus
   * within its children. Override this to customize how you view group requests
   * focus within its children.
   *
   * @param direction One of {@link FocusDirection#UP}, {@link FocusDirection#DOWN},
   *                  {@link FocusDirection#LEFT}, or {@link FocusDirection#RIGHT}
   * @param previouslyFocusedRect The rectangle, in this view's coordinate system,
   *                  to give a finer-grained hint about where focus is coming from.
   *                  May be {@code null} if there is no hint.
   *
   * @return {@code true} if this view or one of its descendants took focus as a
   *         result of this call, {@code false} otherwise
   */
  protected boolean onRequestFocusInDescendants(@FocusDirection.Real int direction,
                                                @Nullable Rect previouslyFocusedRect) {
    int index, increment, end;
    final int count = mChildrenCount;
    final View[] children = mChildren;
    if ((direction & FocusDirection.FORWARD) != 0) {
      index = 0;
      increment = 1;
      end = count;
    } else {
      index = count - 1;
      increment = -1;
      end = -1;
    }

    for (int i = index; i != end; i += increment) {
      View child = children[i];
      if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE) {
        if (child.requestFocus(direction, previouslyFocusedRect)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public void focusableViewAvailable(@NonNull View view) {
    if (mParent != null && (mGroupFlags & FLAG_MASK_FOCUSABILITY) != FOCUS_BLOCK_DESCENDANTS
        && (isFocusableInTouchMode() || !shouldBlockFocusForTouchscreen())
        && !(isFocused() && (mGroupFlags & FLAG_MASK_FOCUSABILITY) != FOCUS_AFTER_DESCENDANTS)) {
      mParent.focusableViewAvailable(view);
    }
  }

  @Override
  public void clearChildFocus(@NonNull View child) {
    if (DBG) {
      Log.d(TAG, this + " clearChildFocus()");
    }

    mFocused = null;
    if (mParent != null) {
      mParent.clearChildFocus(this);
    }
  }

  @Override
  public void clearFocus() {
    if (DBG) {
      Log.d(TAG, this + " clearFocus()");
    }

    if (mFocused == null) {
      super.clearFocus();
    } else {
      View focused = mFocused;
      mFocused = null;
      focused.clearFocus();
    }
  }

  @Override
  void unFocus(@Nullable View focused) {
    if (DBG) {
      Log.d(TAG, this + " unFocus()");
    }

    if (mFocused == null) {
      super.unFocus(focused);
    } else {
      mFocused.unFocus(focused);
      mFocused = null;
    }
  }

  /**
   * Returns the focused child of this view, or {@code null} if no view in this
   * group has focus.
   */
  @Nullable
  public View getFocusedChild() {
    return mFocused;
  }

  /**
   * Returns the deepest focused child of this view, or {@code null} if no view
   * in this group has focus.
   */
  @Nullable
  View getDeepestFocusedChild() {
    View v = this;
    while (v != null) {
      if (v.isFocused()) {
        return v;
      }

      v = v instanceof ViewGroup ? ((ViewGroup) v).getFocusedChild() : null;
    }

    return null;
  }

  /**
   * Indicates whether or not this view has or contains focus.
   */
  @Override
  public boolean hasFocus() {
    return (mPrivateFlags & PFLAG_FOCUSED) != 0 || mFocused != null;
  }

  @Nullable
  @Override
  public View findFocus() {
    if (DBG) {
      Log.d(TAG, "Find focus in " + this + ": flags=" + isFocused() + ", child=" + mFocused);
    }

    if (isFocused()) {
      return this;
    }

    if (mFocused != null) {
      return mFocused.findFocus();
    }

    return null;
  }

  @Override
  public boolean hasFocusable() {
    if ((mViewFlags & VISIBILITY_MASK) != VISIBLE) {
      return false;
    }

    if (isFocusable()) {
      return true;
    }

    @Focusability
    final int descendantFocusability = getDescendantFocusability();
    if (descendantFocusability != FOCUS_BLOCK_DESCENDANTS) {
      final int count = mChildrenCount;
      final View[] children = mChildren;
      for (int i = 0; i < count; i++) {
        View child = children[i];
        if (child.hasFocusable()) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @param focused   {@inheritDoc}
   * @param direction {@inheritDoc}, or {@code 0} if not applicable.
   *
   * @return
   */
  @Nullable
  @Override
  public View focusSearch(@Nullable View focused, @FocusDirection.Real int direction) {
    if (mParent != null) {
      return mParent.focusSearch(focused, direction);
    }

    return null;
  }
  //endregion

  //region Drawable
  @Override
  public void childDrawableStateChanged(@NonNull View child) {//TODO: Implement
  }
  //endregion

  //region Hit
  @Nullable
  @Override
  View hitDeepestFocusableView(float x, float y) {
    if (DBG) {
      Log.d(TAG, this + " ViewGroup.hitDeepestFocusableView x=" + x + "; y=" + y);
    }

    switch (mGroupFlags & FLAG_MASK_FOCUSABILITY) {
      case FOCUS_BEFORE_DESCENDANTS: {
        final View hit = super.hitDeepestFocusableView(x, y);
        return hit != null ? hit : onHitInDescendants(x, y);
      }
      case FOCUS_AFTER_DESCENDANTS: {
        final View hit = super.hitDeepestFocusableView(x, y);
        return hit != null ? hit : onHitInDescendants(x, y);
      }
      case FOCUS_BLOCK_DESCENDANTS:
      default:
        return super.hitDeepestFocusableView(x, y);
    }
  }

  @Nullable
  protected View onHitInDescendants(float x, float y) {
    final int count = mChildrenCount;
    final View[] children = mChildren;
    for (int i = 0; i  < count; i++) {
      View child = children[i];
      if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE) {
        View hit = child.hitDeepestFocusableView(x, y);
        if (hit != null) {
          return hit;
        }
      }
    }

    return null;
  }
  //endregion

  /**
   * Indicates if the specified view is transitioning. This method is used to
   * determine if a view which is {@link #INVISIBLE} or {@link #GONE} should
   * still be invalidated because it is being transitioned (and therefore still
   * needs to be redrawn).
   */
  boolean isViewTransitioning(@NonNull View view) {
    return false;
  }

  /**
   * Called when a view's visibility has changed. Notifies the parent to take any
   * appropriate action. Visibilities will be one of {@link #GONE},
   * {@link #INVISIBLE}, or {@link #VISIBLE}
   *
   * @param child         The view whose visibility has changed
   * @param oldVisibility The previous visibility
   * @param newVisibility The new visibility
   */
  protected void onChildVisibilityChanged(@NonNull View child,
                                          @Visibility int oldVisibility,
                                          @Visibility int newVisibility) {
    // TODO: Implement
  }

  //region LayoutParams
  /**
   * Indicates whether or not the passed layout parameters are suitable for this
   * view group.
   */
  protected boolean checkLayoutParams(@Nullable LayoutParams params) {
    return params != null;
  }

  /**
   * Returns a safe set of layout parameters based on the supplied layout params.
   * When a view group is passed a view whose layout params do not pass
   * {@link #checkLayoutParams}, this method is invoked. This method should
   * return a new set of layout params suitable for this view group, possibly by
   * copying the appropriate attributes from {@code params}.
   *
   * @param params The layout parameters to convert into a suitable set of
   *               layout parameters for this view group
   *
   * @return A suitable set of layout parameters for this view group
   */
  @Nullable
  protected LayoutParams generateLayoutParams(LayoutParams params) {
    return params;
  }

  /**
   * Returns a set of default layout parameters. These parameters are requested
   * when the view passed to {@link #addView(View)} has no layout parameters
   * already set. If {@code null} is returned, an exception is thrown by
   * {@code addView()}.
   */
  @Nullable
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
  }

  /**
   * Called when the layout parameters of a child have been changed or updated.
   * Override this to update the layout.
   *
   * @param child  The child originating the event
   * @param params The params assigned to the child
   */
  protected void onSetLayoutParams(@NonNull View child, @NonNull LayoutParams params) {}

  /**
   * LayoutParams are used by {@link View} instances to tell their parents how
   * they want to be laid out.
   *
   * <p>The base layout parameters just describe how big the view wants to be
   * for both width and height. For each dimension, it can specify one of:
   *
   * <ul>
   *   <li>{@link #MATCH_PARENT}, which indicates that the view wants to be as
   *   big as its parent (minus padding)
   *   <li>{@link #WRAP_CONTENT}, which means that the view wants to be just big
   *   enough to enclose its content (plus padding)
   *   <li>an exact number
   * </ul>
   *
   * <p>Subclasses of {@code LayoutParams} may add additional parameters to use
   * when laying out subclasses of {@code View}, such as {@link MarginLayoutParams}
   */
  public static class LayoutParams {
    /**
     * Special value for the height or width requested by a {@link View}. {@code
     * FILL_PARENT} indicates that the view wants to be as big as its parent,
     * minus the parent's padding, if any. This value was deprecated starting in
     * API Level 8 in Android and replaced by {@link #MATCH_PARENT}. It is
     * provided only for ease-of-use.
     *
     * @see #MATCH_PARENT
     */
    @Deprecated
    @SuppressWarnings("UnusedDeclaration")
    public static final int FILL_PARENT = -1;

    /**
     * Special value for the height or width requested by a {@link View}. {@code
     * MATCH_PARENT} indicates that the view wants to be as big as its parent,
     * minus the parent's padding, if any.
     */
    public static final int MATCH_PARENT = -1;

    /**
     * Special value for the height or width requested by a {@link View}. {@code
     * WRAP_CONTENT} indicates that the view wants to be just large enough to
     * fit its own internal content, taking its own padding into account.
     */
    public static final int WRAP_CONTENT = -2;

    /**
     * Information about how wide the view wants to be. Can be one of the
     * constants {@link #MATCH_PARENT} or {@link #WRAP_CONTENT}, otherwise an
     * exact size.
     */
    public int width;

    /**
     * Information about how tall the view wants to be. Can be one of the
     * constants {@link #MATCH_PARENT} or {@link #WRAP_CONTENT}, otherwise an
     * exact size.
     */
    public int height;

    /**
     * Constructs a new set of layout parameters with the specified {@code
     * width} and {@code height}.
     *
     * @param width  How wide the view wants to be, either {@link #MATCH_PARENT}
     *               or {@link #WRAP_CONTENT}, or a fixed size, in pixels
     * @param height How tall the view wants to be, either {@link #MATCH_PARENT}
     *               or {@link #WRAP_CONTENT}, or a fixed size, in pixels
     */
    public LayoutParams(int width, int height) {
      this.width = width;
      this.height = height;
    }

    /**
     * Constructs a new set of layout parameters, copying the {@link #width} and
     * {@link #height} from the specified {@code src} parameters.
     *
     * @param src The layout parameters to copy
     */
    public LayoutParams(@NonNull ViewGroup.LayoutParams src) {
      this.width = src.width;
      this.height = src.height;
    }

    /**
     * Used internally by {@link ViewGroup.MarginLayoutParams}.
     */
    LayoutParams() {}

    public void resolveLayoutDirection(@LayoutDirection.Resolved int layoutDirection) {}

    /**
     * Draws the debugging annotations on the specified {@code canvas} for these
     * layout parameters.
     *
     * @param view   The view that contains these layout parameters
     * @param canvas The canvas on which to draw
     * @param paint  The paint to use
     */
    public void onDrawDebug(@NonNull View view, @NonNull Canvas canvas, @NonNull Paint paint) {}

    @Override
    public String toString() {
      return "ViewManager.LayoutParams={ width=" + sizeToString(width)
          + ", height=" + sizeToString(height) + " }";
    }

    protected static String sizeToString(int size) {
      switch (size) {
        case WRAP_CONTENT:
          return "wrap-content";
        case MATCH_PARENT:
          return "match-parent";
        default:
          return String.valueOf(size);
      }
    }
  }

  /**
   * Implementation of {@link LayoutParams} which adds support for views with
   * margins.
   */
  public static class MarginLayoutParams extends LayoutParams {
    /**
     * The left margin, in pixels, of this child. Margin values should be
     * positive. Call {@link ViewGroup#setLayoutParams} after reassigning a new
     * value to this field.
     */
    @IntRange(from = 0)
    public int leftMargin;

    /**
     * The top margin, in pixels, of this child. Margin values should be
     * positive. Call {@link ViewGroup#setLayoutParams} after reassigning a new
     * value to this field.
     */
    @IntRange(from = 0)
    public int topMargin;

    /**
     * The right margin, in pixels, of this child. Margin values should be
     * positive. Call {@link ViewGroup#setLayoutParams} after reassigning a new
     * value to this field.
     */
    @IntRange(from = 0)
    public int rightMargin;

    /**
     * The bottom margin, in pixels, of this child. Margin values should be
     * positive. Call {@link ViewGroup#setLayoutParams} after reassigning a new
     * value to this field.
     */
    @IntRange(from = 0)
    public int bottomMargin;

    /**
     * The start margin, in pixels, of the child. Margin values should be
     * positive. Call {@link ViewGroup#setLayoutParams(LayoutParams)} after
     * reassigning a new value to this field.
     */
    private int startMargin = DEFAULT_MARGIN_RELATIVE;

    /**
     * The end margin, in pixels, of the child. Margin values should be
     * positive. Call {@link ViewGroup#setLayoutParams(LayoutParams)} after
     * reassigning a new value to this field.
     */
    private int endMargin = DEFAULT_MARGIN_RELATIVE;

    /**
     * The default start and end margin.
     */
    public static final int DEFAULT_MARGIN_RELATIVE = Integer.MIN_VALUE;

    /**
     * Flags for the margin state, do not modify this unless you know what you
     * are doing.
     *
     * Bit  0: layout direction
     * Bit  1: layout direction
     * Bit  2: left margin undefined
     * Bit  3: right margin undefined
     * Bit  4: need resolution
     *
     * Bit 5 to 7 not used
     */
    byte mMarginFlags;

    private static final int LAYOUT_DIRECTION_MASK       = 0x00000003;
    private static final int LEFT_MARGIN_UNDEFINED_MASK  = 0x00000004;
    private static final int RIGHT_MARGIN_UNDEFINED_MASK = 0x00000008;
    private static final int NEED_RESOLUTION_MASK        = 0x00000010;

    private static final int DEFAULT_MARGIN_RESOLVED = 0;
    private static final int UNDEFINED_MARGIN = DEFAULT_MARGIN_RELATIVE;

    /**
     * {@inheritDoc}
     */
    public MarginLayoutParams(int width, int height) {
      super(width, height);

      mMarginFlags |= LEFT_MARGIN_UNDEFINED_MASK;
      mMarginFlags |= RIGHT_MARGIN_UNDEFINED_MASK;

      mMarginFlags &= ~NEED_RESOLUTION_MASK;
    }

    /**
     * {inheritDoc}
     */
    public MarginLayoutParams(@NonNull LayoutParams src) {
      super(src);

      mMarginFlags |= LEFT_MARGIN_UNDEFINED_MASK;
      mMarginFlags |= RIGHT_MARGIN_UNDEFINED_MASK;

      mMarginFlags &= ~NEED_RESOLUTION_MASK;
    }

    /**
     * Used internally.
     */
    public final void copyMarginsFrom(@NonNull MarginLayoutParams src) {
      this.leftMargin = src.leftMargin;
      this.topMargin = src.topMargin;
      this.rightMargin = src.rightMargin;
      this.bottomMargin = src.bottomMargin;
      this.startMargin = src.startMargin;
      this.endMargin = src.endMargin;

      this.mMarginFlags = src.mMarginFlags;
    }

    /**
     * Sets the margins, in pixels. A call to {@link View#requestLayout} will
     * need to be performed after this call so that the new margins are taken
     * into account. Margin values should be positive (i.e., {@code margin >=
     * 0}).
     *
     * @param left   The left margin size
     * @param top    The top margin size
     * @param right  The right margin size
     * @param bottom The bottom margin size
     */
    public void setMargins(@IntRange(from = 0) int left, @IntRange(from = 0) int top,
                           @IntRange(from = 0) int right, @IntRange(from = 0) int bottom) {
      leftMargin = left;
      topMargin = top;
      rightMargin = right;
      bottomMargin = bottom;
      mMarginFlags &= ~LEFT_MARGIN_UNDEFINED_MASK;
      mMarginFlags &= ~RIGHT_MARGIN_UNDEFINED_MASK;
      if (isMarginRelative()) {
        mMarginFlags |= NEED_RESOLUTION_MASK;
      } else {
        mMarginFlags &= ~NEED_RESOLUTION_MASK;
      }
    }

    /**
     * Sets the relative margins, in pixels. A call to {@link View#requestLayout()}
     * needs to be done so that the new relative margins are taken into account.
     * Left and right margins may be overridden by {@link View#requestLayout()}
     * depending on layout direction. Margin values should be positive.
     *
     * @param start  The start margin size
     * @param top    The top margin size
     * @param end    The end margin size
     * @param bottom The bottom margin size
     */
    public void setMarginsRelative(int start, int top, int end, int bottom) {
        startMargin = start;
        topMargin = top;
        endMargin = end;
        bottomMargin = bottom;
        mMarginFlags |= NEED_RESOLUTION_MASK;
    }

    /**
     * Sets the relative start margin. Margin values should be positive.
     *
     * @param start The start margin size
     */
    public void setMarginStart(int start) {
      startMargin = start;
      mMarginFlags |= NEED_RESOLUTION_MASK;
    }

    /**
     * Returns the start margin, in pixels.
     */
    public int getMarginStart() {
      if (startMargin != DEFAULT_MARGIN_RELATIVE) {
        return startMargin;
      }

      if ((mMarginFlags & NEED_RESOLUTION_MASK) == NEED_RESOLUTION_MASK) {
        resolveMargins();
      }

      switch (mMarginFlags & LAYOUT_DIRECTION_MASK) {
        case LayoutDirection.RTL:
          return rightMargin;
        case LayoutDirection.LTR:
        default:
          return leftMargin;
      }
    }

    /**
     * Sets the relative end margin. Margin values should be positive.
     *
     * @param end The end margin size
     */
    public void setMarginEnd(int end) {
      endMargin = end;
      mMarginFlags |= NEED_RESOLUTION_MASK;
    }

    /**
     * Returns the end margin, in pixels.
     */
    public int getMarginEnd() {
      if (endMargin != DEFAULT_MARGIN_RELATIVE) {
        return endMargin;
      }

      if ((mMarginFlags & NEED_RESOLUTION_MASK) == NEED_RESOLUTION_MASK) {
        resolveMargins();
      }

      switch (mMarginFlags & LAYOUT_DIRECTION_MASK) {
        case LayoutDirection.RTL:
          return leftMargin;
        case LayoutDirection.LTR:
        default:
          return rightMargin;
      }
    }

    /**
     * Indicates if the margins are relative.
     */
    public boolean isMarginRelative() {
      return startMargin != DEFAULT_MARGIN_RELATIVE
          || endMargin != DEFAULT_MARGIN_RELATIVE;
    }

    /**
     * Sets the layout direction.
     *
     * @param layoutDirection Should be either {@link LayoutDirection#LTR} or
     *                        {@link LayoutDirection#RTL}
     */
    public void setLayoutDirection(@LayoutDirection.Resolved int layoutDirection) {
      if (layoutDirection != LayoutDirection.LTR
          && layoutDirection != LayoutDirection.RTL) {
        return;
      }

      if (layoutDirection != (mMarginFlags & LAYOUT_DIRECTION_MASK)) {
        mMarginFlags &= ~ LAYOUT_DIRECTION_MASK;
        mMarginFlags |= (layoutDirection & LAYOUT_DIRECTION_MASK);
        if (isMarginRelative()) {
          mMarginFlags |= NEED_RESOLUTION_MASK;
        } else {
          mMarginFlags &= ~NEED_RESOLUTION_MASK;
        }
      }
    }

    /**
     * Returns the resolved layout direction.
     *
     * @return Either {@link LayoutDirection#LTR} or {@link LayoutDirection#RTL}
     */
    @LayoutDirection.Resolved
    public int getLayoutDirection() {
      return (mMarginFlags & LAYOUT_DIRECTION_MASK);
    }

    @Override
    public void resolveLayoutDirection(@LayoutDirection.Resolved int layoutDirection) {
      setLayoutDirection(layoutDirection);
      if (!isMarginRelative()
          || (mMarginFlags & NEED_RESOLUTION_MASK) == NEED_RESOLUTION_MASK) {
        return;
      }

      resolveMargins();
    }

    private void resolveMargins() {
      switch (mMarginFlags & LAYOUT_DIRECTION_MASK) {
        case LayoutDirection.RTL:
          leftMargin = (endMargin > DEFAULT_MARGIN_RELATIVE)
              ? endMargin : DEFAULT_MARGIN_RESOLVED;
          rightMargin = (startMargin > DEFAULT_MARGIN_RELATIVE)
              ? startMargin : DEFAULT_MARGIN_RESOLVED;
          break;
        case LayoutDirection.LTR:
        default:
          leftMargin = (startMargin > DEFAULT_MARGIN_RELATIVE)
              ? startMargin : DEFAULT_MARGIN_RESOLVED;
          rightMargin = (endMargin > DEFAULT_MARGIN_RELATIVE)
              ? endMargin : DEFAULT_MARGIN_RESOLVED;
      }

      mMarginFlags &= ~NEED_RESOLUTION_MASK;
    }

    /**
     * Indicates if the layout margin is right-to-left.
     */
    public boolean isLayoutRtl() {
      return (mMarginFlags & LAYOUT_DIRECTION_MASK) == LayoutDirection.RTL;
    }

    @Override
    public void onDrawDebug(@NonNull View view, @NonNull Canvas canvas, @NonNull Paint paint) {
      Insets oi = isLayoutModeOptical(view.mParent) ? view.getOpticalInsets() : Insets.NONE;
      fillDifference(canvas, paint,
          view.getLeft() + oi.left,
          view.getTop() + oi.top,
          view.getRight() - oi.right,
          view.getBottom() - oi.bottom,
          leftMargin,
          topMargin,
          rightMargin,
          bottomMargin);
    }

    @Override
    public String toString() {
      return "ViewGroup.MarginLayoutParams={ width=" + sizeToString(width)
          + ", height=" + sizeToString(height)
          + ", leftMargin=" + String.valueOf(leftMargin)
          + ", topMargin=" + String.valueOf(topMargin)
          + ", rightMargin=" + String.valueOf(rightMargin)
          + ", bottomMargin=" + String.valueOf(bottomMargin) + " }";
    }
  }
  //endregion
}
