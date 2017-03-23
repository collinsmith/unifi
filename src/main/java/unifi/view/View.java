package unifi.view;

import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

import unifi.content.Context;
import unifi.graphics.Canvas;
import unifi.graphics.Color;
import unifi.graphics.Insets;
import unifi.graphics.PixelFormat;
import unifi.graphics.Rect;
import unifi.graphics.drawable.ColorDrawable;
import unifi.graphics.drawable.Drawable;
import unifi.runtime.Message;
import unifi.util.FocusDirection;
import unifi.util.LayoutDirection;
import unifi.util.LocaleUtils;
import unifi.util.Log;
import unifi.util.LongSparseLongArray;
import unifi.util.UnifiRuntimeException;

public class View implements Drawable.Callback {
  private static final boolean DBG = true;

  @NonNull
  protected static final String VIEW_LOG_TAG = "View";

  private static boolean sIgnoreMeasureCache = false;

  /**
   * Indicates that a view has no ID.
   *
   * @see #getId()
   */
  public static final int NO_ID = -1;

  /**
   * Default horizontal layout direction.
   */
  static final int LAYOUT_DIRECTION_RESOLVED_DEFAULT = LayoutDirection.LTR;

  //region @Visibility
  @IntDef({ VISIBLE, INVISIBLE, GONE })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Visibility {}
  //endregion

  //region mViewFlags Flags
  //region VISIBILITY_MASK
  /**
   * This view is visible. Use with {@link #setVisibility}.
   */
  // TODO: Reference unifi:visibility annotation
  public static final int VISIBLE = 0;

  /**
   * This view is invisible, but it still takes up space for layout purposes.
   * Use with {@link #setVisibility}.
   */
  // TODO: Reference unifi:visibility annotation
  public static final int INVISIBLE = 0x00000001;

  /**
   * This view is invisible, and it doesn't take any space for layout purposes.
   * Use with {@link #setVisibility}.
   */
  // TODO: Reference unifi:visibility annotation
  public static final int GONE = 0x00000002;

  /**
   * Mask for use with {@link #setFlags} indicating bits used for visibility.
   */
  static final int VISIBILITY_MASK = VISIBLE | INVISIBLE | GONE;
  //endregion

  //region FOCUSABLE_MASK
  /**
   * This view does not want keystrokes. Use with {@link #FOCUSABLE_MASK} when
   * calling {@link #setFlags}.
   */
  private static final int NOT_FOCUSABLE = 0;

  /**
   * This view wants keystrokes. Use with {@link #FOCUSABLE_MASK} when calling
   * {@link #setFlags}.
   */
  private static final int FOCUSABLE = 0x00000004;

  /**
   * Indicates that this view can take/keep focus when in touch mode.
   */
  static final int FOCUSABLE_IN_TOUCH_MODE = 0x00000008;

  /**
   * Mask for use with {@link #setFlags} indicating bits used for focus.
   */
  private static final int FOCUSABLE_MASK = NOT_FOCUSABLE | FOCUSABLE;
  //endregion

  //region DRAW_MASK
  /**
   * This view won't draw. {@link #onDraw} won't be called and further optimizations
   * will be performed. It is okay to have this flag set and a background.
   * Use with {@link #DRAW_MASK} when calling setFlags.
   */
  static final int WILL_NOT_DRAW = 0x00000080;

  /**
   * Mask for use with {@link #setFlags} indicating bits used for indicating
   * whether this view is will draw.
   */
  static final int DRAW_MASK = 0x00000080;
  //endregion

  /**
   * Indicates that this view gets its drawable states from its direct parent
   * and ignores its original internal states.
   */
  static final int DUPLICATE_PARENT_STATE = 0x00400000;
  //endregion

  //region mPrivateFlags Flags
  static final int PFLAG_PADDING_RESOLVED             = 0x00000001;
  static final int PFLAG_HAS_BOUNDS                   = 0x00000002;
  static final int PFLAG_DRAWN                        = 0x00000004;
  static final int PFLAG_INVALIDATED                  = 0x00000008;
  static final int PFLAG_WILL_LAYOUT                  = 0x00000010;
  static final int PFLAG_MEASURED_DIMENSION_SET       = 0x00000020;
  static final int PFLAG_LAYOUT_REQUIRED              = 0x00000040;
  static final int PFLAG_MEASURE_NEEDED_BEFORE_LAYOUT = 0x00000080;
  static final int PFLAG_OPAQUE_BACKGROUND            = 0x00000100;
  static final int PFLAG_OPAQUE_SCROLLBARS            = 0x00000200;
  static final int PFLAG_OPAQUE_MASK = PFLAG_OPAQUE_BACKGROUND | PFLAG_OPAQUE_SCROLLBARS;
  static final int PFLAG_SKIP_DRAW                    = 0x00000400;
  static final int PFLAG_ONLY_DRAWS_BACKGROUND        = 0x00000800;
  static final int PFLAG_DRAWABLE_RESOLVED            = 0x00001000;
  static final int PFLAG_DRAWABLE_STATE_DIRTY         = 0x00002000;
  static final int PFLAG_IS_LAID_OUT                  = 0x00004000;
  static final int PFLAG_DIRTY                        = 0x00008000;
  static final int PFLAG_DIRTY_OPAQUE                 = 0x00010000;
  static final int PFLAG_DIRTY_MASK = PFLAG_DIRTY | PFLAG_DIRTY_OPAQUE;
  static final int PFLAG_WANTS_FOCUS                  = 0x00020000;
  static final int PFLAG_FOCUSED                      = 0x00040000;
  static final int PFLAG_OVER                         = 0x00080000;


  static final int PFLAG_LAYOUT_DIRECTION_MASK_SHIFT = 24;
  static final int PFLAG_LAYOUT_DIRECTION_MASK = 0x3 << PFLAG_LAYOUT_DIRECTION_MASK_SHIFT;
  static final int PFLAG_LAYOUT_DIRECTION_RESOLVED_RTL = 4 << PFLAG_LAYOUT_DIRECTION_MASK_SHIFT;
  static final int PFLAG_LAYOUT_DIRECTION_RESOLVED = 8 << PFLAG_LAYOUT_DIRECTION_MASK_SHIFT;
  static final int PFLAG_LAYOUT_DIRECTION_RESOLVED_MASK = 0xC << PFLAG_LAYOUT_DIRECTION_MASK_SHIFT;

  /**
   * Group of bits indicating that RTL properties resolution is done.
   */
  static final int ALL_RTL_PROPERTIES_RESOLVED =
      PFLAG_LAYOUT_DIRECTION_RESOLVED |
      PFLAG_PADDING_RESOLVED;
  //endregion

  //region MEASURED_STATE
  /**
   * Bit mask of {@link #getMeasuredWidthAndState()} and {@link
   * #getMeasuredWidthAndState()} that provide the actual measured size.
   */
  public static final int MEASURED_SIZE_MASK = 0x00ffffff;

  /**
   * Bit mask of {@link #getMeasuredWidthAndState()} and {@link
   * #getMeasuredWidthAndState()} that provide the additional state bits.
   */
  public static final int MEASURED_STATE_MASK = 0xff000000;

  /**
   * Bit shift of {@link #MEASURED_STATE_MASK} to get to the height bits for
   * functions that combine both width and height into a single int, such as
   * {@link #getMeasuredState()} and the childState argument of {@link
   * #resolveSizeAndState(int, int, int)}.
   */
  public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;

  /**
   * Bit of {@link #getMeasuredWidthAndState()} and {@link
   * #getMeasuredWidthAndState()} that indicates the measured size is smaller
   * that the space the view would like to have.
   */
  public static final int MEASURED_STATE_TOO_SMALL = 0x01000000;
  //endregion

  /**
   * Temporary Rect currently for use with {@link #mBackground}.
   */
  static final ThreadLocal<Rect> sThreadLocal = new ThreadLocal<>();

  int mID = NO_ID;

  /**
   * The application environment this view lives in.
   */
  @Nullable Context mContext;

  /**
   * The parent this view is attached to.
   */
  @Nullable ViewParent mParent;

  /**
   * Attachment information for this view.
   */
  @Nullable AttachInfo mAttachInfo;

  /**
   * The layout parameters associated with this view and used by the parent
   * {@link ViewGroup} to determine how this view should be laid out.
   *
   * <p>This field should not be modified directly, and {@link #setLayoutParams}
   * should be used instead.
   *
   * @see #setLayoutParams(ViewGroup.LayoutParams)
   * @see #getLayoutParams()
   */
  @Nullable protected ViewGroup.LayoutParams mLayoutParams;

  /**
   * Flags holding various states of the view. Do not modify outside of
   * {@link #setFlags}
   */
  int mViewFlags;

  /**
   * Flags holding various private states of the view.
   */
  int mPrivateFlags;

  //region Bounds
  /**
   * The distance, in pixels, from the left edge of this view's
   * {@link #mParent} to the left edge of this view.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #setLeft(int)
   * @see #getLeft()
   */
  protected int mLeft;
  /**
   * The distance, in pixels, from the left edge of this view's
   * {@link #mParent} to the right edge of this view.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #setRight(int)
   * @see #getRight()
   */
  protected int mRight;
  /**
   * The distance, in pixels, from the top edge of this view's
   * {@link #mParent} to the top edge of this view.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #setTop(int)
   * @see #getTop()
   */
  protected int mTop;
  /**
   * The distance, in pixels, from the top edge of this view's
   * {@link #mParent} to the bottom edge of this view.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #setBottom(int)
   * @see #getBottom()
   */
  protected int mBottom;
  //endregion

  //region Scrolling
  /**
   * The offset, in pixels, by which the content of this view is scrolled
   * horizontally.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #getScrollX
   * @see #setScrollX
   */
  protected int mScrollX;
  /**
   * The offset, in pixels, by which the content of this view is scrolled
   * vertically.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #getScrollY
   * @see #setScrollY
   */
  protected int mScrollY;
  //endregion

  //region Padding
  /**
   * The left padding of this view, in pixels. That is, the distance between
   * the left edge of this view and the left edge of its content.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #setPadding(int, int, int, int)
   * @see #getPaddingLeft()
   */
  protected int mPaddingLeft;
  /**
   * The right padding of this view, in pixels. That is, the distance between
   * the right edge of this view and the right edge of its content.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #setPadding(int, int, int, int)
   * @see #getPaddingRight()
   */
  protected int mPaddingRight;
  /**
   * The top padding of this view, in pixels. That is, the distance between
   * the top edge of this view and the top edge of its content.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #setPadding(int, int, int, int)
   * @see #getPaddingTop()
   */
  protected int mPaddingTop;
  /**
   * The bottom padding of this view, in pixels. That is, the distance between
   * the bottom edge of this view and the bottom edge of its content.
   *
   * <p>Note: This field should not be modified, and the getter/setter methods
   * should be used instead so that the correct callbacks are sent.
   *
   * @see #setPadding(int, int, int, int)
   * @see #getPaddingBottom()
   */
  protected int mPaddingBottom;

  /**
   * Cache for paddingRight set by the user to append to the scrollbar's size.
   *
   * <p>Note: This field is volatile should not be modified.
   */
  protected int mUserPaddingRight;
  /**
   * Cache for paddingBottom set by the user to append to the scrollbar's size.
   *
   * <p>Note: This field is volatile should not be modified.
   */
  protected int mUserPaddingBottom;
  /**
   * Cache for paddingRight set by the user to append to the scrollbar's size.
   *
   * <p>Note: This field is volatile should not be modified.
   */
  protected int mUserPaddingLeft;

  /**
   * Cache the paddingStart set by the user to append to the scrollbar's size.
   *
   * <p>Note: This field is volatile should not be modified.
   */
  int mUserPaddingStart;

  /**
   * Cache the paddingEnd set by the user to append to the scrollbar's size.
   *
   * <p>Note: This field is volatile should not be modified.
   */
  int mUserPaddingEnd;

  /**
   * Cache initial left padding.
   *
   * <p>Note: This field is volatile should not be modified.
   */
  int mUserPaddingLeftInitial;

  /**
   * Cache initial right padding.
   *
   * <p>Note: This field is volatile should not be modified.
   */
  int mUserPaddingRightInitial;

  /**
   * Default undefined padding
   */
  private static final int UNDEFINED_PADDING = Integer.MIN_VALUE;

  /**
   * Cache if a left padding has been defined
   */
  private boolean mLeftPaddingDefined = false;

  /**
   * Cache if a right padding has been defined
   */
  private boolean mRightPaddingDefined = false;
  //endregion

  //region Measuring
  int mOldWidthMeasureSpec = Integer.MIN_VALUE;
  int mOldHeightMeasureSpec = Integer.MIN_VALUE;
  @Nullable private LongSparseLongArray mMeasureCache;

  /**
   * Width, in pixels, as measured during measure pass.
   */
  int mMeasuredWidth;

  /**
   * Height, in pixels, as measured during measure pass
   */
  int mMeasuredHeight;

  /**
   * The minimum width of this view. We'll try our best to have the {@linkplain
   * #getWidth width} of this view be at least this amount.
   */
  private int mMinWidth;

  /**
   * The minimum height of this view. We'll try our best to have the {@linkplain
   * #getHeight height} of this view be at least this amount.
   */
  private int mMinHeight;
  //endregion

  /**
   * The layout insets, in pixels, that is the distance in pixels between the
   * visible edges of this view and its bounds.
   */
  @Nullable private Insets mLayoutInsets;

  @Nullable private Drawable mBackground;
  private boolean mBackgroundSizeChanged;

  @Nullable
  private int[] mDrawableState = null;

  public View(@Nullable Context context) {
    mContext = context;
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder(128);
    out.append(getClass().getName());
    out.append('{');
    out.append(Integer.toHexString(System.identityHashCode(this)));
    out.append(' ');
    switch (mViewFlags & VISIBILITY_MASK) {
      case VISIBLE:   out.append('V'); break;
      case INVISIBLE: out.append('I'); break;
      case GONE:      out.append('G'); break;
      default:        out.append('.');
    }

    out.append((mViewFlags & FOCUSABLE_MASK) == FOCUSABLE ? 'F' : '.');
    out.append((mViewFlags & DRAW_MASK) == WILL_NOT_DRAW ? '.' : 'D');
    out.append((mPrivateFlags & PFLAG_INVALIDATED) != 0 ? 'I' : '.');
    out.append((mPrivateFlags & PFLAG_DIRTY_MASK) != 0 ? 'D' : '.');
    out.append(' ');
    out.append((mPrivateFlags & PFLAG_FOCUSED) != 0 ? 'F' : '.');
    out.append(' ');
    out.append(mLeft);
    out.append(',');
    out.append(mTop);
    out.append('~');
    out.append(mRight);
    out.append(',');
    out.append(mBottom);
    final int id = getId();
    if (id != NO_ID) {
      out.append(" #");
      out.append(Integer.toHexString(id));
    }

    out.append('}');
    return out.toString();
  }

  //region ID
  /**
   * Returns this view's identifier, or {@value #NO_ID} ({@link #NO_ID}) if the
   * view has no ID.
   */
  public int getId() {
    return mID;
  }

  /**
   * Sets the identifier for this view. The identifier does not have to be
   * unique in this view's hierarchy, however it should be a positive number.
   *
   * @param id A number used to identify this view
   *
   * @see #NO_ID
   * @see #getId()
   * @see #findViewById(int)
   */
  public void setId(int id) {
    mID = id;
  }

  /**
   * Searches for a child view with the specified {@link #getId() id}. If this
   * view's id is the same as {@code id}, then it is returned.
   *
   * @param id The id to search for
   *
   * @return A view with the specified {@code id} in the hierarchy,
   *         or {@code null}
   */
  @Nullable
  public final View findViewById(int id) {
    if (id < 0) {
      return null;
    }

    return findViewByIdTraversal(id);
  }

  /**
   * Searches for a child view with the specified {@link #getId() id}. If this
   * view's id is the same as {@code id}, then it is returned. This method
   * can be overridden to provide custom traversals.
   *
   * @see #findViewById(int)
   */
  @Nullable
  protected View findViewByIdTraversal(int id) {
    if (id == mID) {
      return this;
    }

    return null;
  }
  //endregion

  //region Hierarchy Attachments
  /**
   * Returns the application environment this view is running in, through which
   * it can access the current theme, resources, etc. If this view is currently
   * detached, that is, does not belong to any application, then {@code null}
   * is returned.
   */
  @Nullable
  public final Context getContext() {
    return mContext;
  }

  /**
   * Returns the view root associated with this view, or {@code null} if this
   * view is not attached to any.
   */
  @Nullable
  public ViewRoot getViewRoot() {
    if (mAttachInfo != null) {
      return mAttachInfo.mViewRoot;
    }

    return null;
  }

  /**
   * Returns the parent of this view, or {@code null} if this view has no parent.
   *
   * <p>Note: The parent of this view is not necessarily a {@code View}.
   */
  @Nullable
  public final ViewParent getParent() {
    return mParent;
  }

  /**
   * Reassigns the parent of this view to the specified view parent.
   *
   * <p>Note: Caller is responsible for calling {@link #requestLayout()} if necessary.
   */
  void assignParent(@Nullable ViewParent parent) {
    if (mParent == null) {
      mParent = parent;
    } else if (parent == null) {
      mParent = null;
    } else {
      throw new UnifiRuntimeException(
          "View " + this + " being added, but it already has a parent");
    }
  }

  /**
   * Returns the topmost view in the current view hierarchy, or {@code null} if
   * this view does not belong to any hierarchy.
   */
  @Nullable
  public View getRootView() {
    if (mAttachInfo != null) {
      final View v = mAttachInfo.mRootView;
      if (v != null) {
        return v;
      }
    }

    View parent = this;
    while (mParent != null && parent.mParent instanceof View) {
      parent = (View) parent.mParent;
    }

    return parent;
  }

  void dispatchAttachedToWindow(@NonNull AttachInfo info, @Visibility int visibility) {
    if (DBG) {
      Log.d(VIEW_LOG_TAG, "Attached! " + this);
    }

    mAttachInfo = info;
    mPrivateFlags |= PFLAG_DRAWABLE_STATE_DIRTY;
    //onAttachedToWindow();

    int vis = info.mWindowVisibility;
    if (vis != GONE) {
      onWindowVisibilityChanged(vis);
    }

    if ((mPrivateFlags & PFLAG_DRAWABLE_STATE_DIRTY) != 0) {
      refreshDrawableState();
    }
  }

  void dispatchDetachedFromWindow() {
    AttachInfo info = mAttachInfo;
    if (info != null) {
      int vis = info.mWindowVisibility;
      if (vis != GONE) {
        onWindowVisibilityChanged(GONE);
      }
    }

    //onDetachedFromWindow();
    onDetachedFromWindowInternal();

    mAttachInfo = null;
  }

  protected void onDetachedFromWindowInternal() {
    mPrivateFlags &= ~PFLAG_IS_LAID_OUT;
    jumpDrawablesToCurrentState();
  }

  /**
   * Called when the window containing this view has changed its visibility.
   *
   * <p>Note: This tells you whether or not your window is being made visible to
   * the window manager; this does <em>not</em> tell you whether or not your
   * window is obscured by other windows on the screen if it is itself visible.
   *
   * @param visibility The new visibility of the window
   */
  protected void onWindowVisibilityChanged(@Visibility int visibility) {}

  /**
   * Indicates whether or not the device is currently in touch mode. Touch mode
   * is entered once the used begins interacting with the device by touch, and
   * affects various things like whether focus is always visible to the user.
   */
  public boolean isInTouchMode() {
    if (mAttachInfo != null) {
      return mAttachInfo.mInTouchMode;
    }

    return false;
  }
  //endregion

  /**
   * Sets flags controlling the behavior of this view.
   *
   * @param flags Constant indicating the value which should be set
   * @param mask  Constant indicating the bit range that should be changed
   */
  void setFlags(int flags, int mask) {
    int old = mViewFlags;
    mViewFlags = (mViewFlags & ~mask) | (flags & mask);

    int changed = mViewFlags ^ old;
    if (changed == 0) {
      return;
    }

    int privateFlags = mPrivateFlags;

    /* Check if the FOCUSABLE bit has changed */
    if (((changed & FOCUSABLE_MASK) != 0)
        && ((privateFlags & PFLAG_HAS_BOUNDS) !=0)) {
      if (((old & FOCUSABLE_MASK) == FOCUSABLE)
          && ((privateFlags & PFLAG_FOCUSED) != 0)) {
        /* Give up focus if we are no longer focusable */
      } else if (((old & FOCUSABLE_MASK) == NOT_FOCUSABLE)
          && ((privateFlags & PFLAG_FOCUSED) == 0)) {
        /* Tell the view system that we are now available to take focus
         * if no one else already has it. */
        if (mParent != null) mParent.focusableViewAvailable(this);
      }
    }

    final int newVisibility = flags & VISIBILITY_MASK;
    if (newVisibility == VISIBLE) {
      if ((changed & VISIBILITY_MASK) != 0) {
        invalidate();

        // a view becoming visible is worth notifying the parent
        // about in case nothing has focus.  even if this specific view
        // isn't focusable, it may contain something that is, so let
        // the root view try to give this focus if nothing else does.
        if ((mParent != null) && (mBottom > mTop) && (mRight > mLeft)) {
          mParent.focusableViewAvailable(this);
        }
      }
    }

    /* Check if the GONE bit has changed */
    if ((changed & GONE) != 0) {
      requestLayout();

      if (((mViewFlags & VISIBILITY_MASK) == GONE)) {
        if (hasFocus()) clearFocus();
        if (mParent instanceof View) {
          // GONE views no-op invalidation, so invalidate the parent
          ((View) mParent).invalidate();
        }
      }
      if (mAttachInfo != null) {
        mAttachInfo.mViewVisibilityChanged = true;
      }
    }

    /* Check if the VISIBLE bit has changed */
    if ((changed & INVISIBLE) != 0) {
      /* If this view is becoming invisible, set the PFLAG_DRAWN flag so that
       * the next invalidate() will not be skipped. */
      mPrivateFlags |= PFLAG_DRAWN;
      if ((mViewFlags & VISIBILITY_MASK) == INVISIBLE) {
        if (getRootView() != this) {
          if (hasFocus()) clearFocus();
        }
      }

      if (mAttachInfo != null) {
        mAttachInfo.mViewVisibilityChanged = true;
      }
    }

    if ((changed & VISIBILITY_MASK) != 0) {
      if (mParent instanceof ViewGroup) {
        ((ViewGroup) mParent).onChildVisibilityChanged(this,
            (changed & VISIBILITY_MASK), newVisibility);
        ((View) mParent).invalidate();
      } else if (mParent != null) {
        mParent.invalidateChild(this, null);
      }
      dispatchVisibilityChanged(this, newVisibility);
    }

    if ((changed & DRAW_MASK) != 0) {
      if ((mViewFlags & WILL_NOT_DRAW) != 0) {
        if (mBackground != null) {
          mPrivateFlags &= ~PFLAG_SKIP_DRAW;
          mPrivateFlags |=  PFLAG_ONLY_DRAWS_BACKGROUND;
        } else {
          mPrivateFlags |= PFLAG_SKIP_DRAW;
        }
      } else {
        mPrivateFlags &= ~PFLAG_SKIP_DRAW;
      }

      requestLayout();
      invalidate();
    }
  }

  //region Visibility
  /**
   * Returns the visibility status for this view.
   *
   * @return One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}
   */
  @Visibility
  public int getVisibility() {
    return mViewFlags & VISIBILITY_MASK;
  }

  /**
   * Sets the enabled state of this view.
   *
   * @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}
   */
  public void setVisibility(@Visibility int visibility) {
    setFlags(visibility, VISIBILITY_MASK);
    if (mBackground != null) {
      mBackground.setVisible(visibility == VISIBLE, false);
    }
  }

  /**
   * Dispatches a view visibility change down the view hierarchy.
   *
   * @param view       The view whose visibility has changed. Could be <i>this</i>
   *                   view, or an ancestor
   * @param visibility The new visibility: {@link #VISIBLE}, {@link #INVISIBLE}
   *                   or {@link #GONE}
   */
  protected void dispatchVisibilityChanged(@NonNull View view, @Visibility int visibility) {
    onVisibilityChanged(view, visibility);
  }

  /**
   * Called when the visibility of the view or an ancestor of the view has
   * changed.
   *
   * @param view       The view whose visibility has changed. Could be
   *                   <i>this</i>
   *                   view, or an ancestor
   * @param visibility The new visibility: {@link #VISIBLE}, {@link #INVISIBLE}
   *                   or {@link #GONE}
   */
  protected void onVisibilityChanged(@NonNull View view, @Visibility int visibility) {
    // TODO: awaken scrollbars
  }
  //endregion

  //region Opacity
  /**
   * Indicates whether or not this view is opaque. An opaque view guarantees that
   * it will draw all the pixels overlapping its bounds using fully opaque color.
   *
   * Subclasses should override this method whenever possible to indicate whether
   * an instance is opaque. Opaque views are treated in a special way by the view
   * hierarchy, possible allowing it to perform optimizations during invalidate/
   * draw passes.
   *
   * @return {@code true} if this view is guaranteed to be fully-opaque,
   *         {@code false} otherwise.
   */
  public boolean isOpqaue() {
    return (mPrivateFlags & PFLAG_OPAQUE_MASK) == PFLAG_OPAQUE_MASK
        && getFinalAlpha() >= 1.0f;
  }

  protected void computeOpaqueFlags() {
    if (mBackground != null && mBackground.getOpacity() == PixelFormat.OPAQUE) {
      mPrivateFlags |= PFLAG_OPAQUE_BACKGROUND;
    } else {
      mPrivateFlags &= ~PFLAG_OPAQUE_BACKGROUND;
    }

    // TODO: Implement support for scrollbars opacity
  }

  /**
   * Calculates the visual alpha of this view, which is a combination of the
   * actual alpha and the transitionAlpha (if set).
   */
  private float getFinalAlpha() {
    // TODO: Add support for mTransformationInfo
    return 1;
  }
  //endregion

  //region Background
  /**
   * Sets the background {@link Color} for this view.
   */
  public void setBackgroundColor(@ColorInt int color) {
    if (mBackground instanceof ColorDrawable) {
      ((ColorDrawable)mBackground.mutate()).setColor(color);
      computeOpaqueFlags();
    } else {
      setBackground(new ColorDrawable(color));
    }
  }

  /**
   * Returns the drawable being used as the background of this view,
   * or {@code null} if this view has no background.
   *
   * @see #setBackground(Drawable)
   */
  @Nullable
  public Drawable getBackground() {
    return mBackground;
  }

  /**
   * Sets the background to a given drawable, or removes the background if
   * {@code null}. If the background has padding, this view's padding is set to
   * the background's padding. However, when a background is removed, this
   * view's padding is unchanged. If setting the padding is desired, please use
   * {@link #setPadding(int, int, int, int)}.
   *
   * @param background The drawable to use as the background, or {@code null} to
   *                   remove the background
   */
  public void setBackground(@Nullable Drawable background) {
    computeOpaqueFlags();
    if (background == mBackground) {
      return;
    }

    boolean requestLayout = false;

    if (mBackground != null) {
      mBackground.setCallback(null);
      unscheduleDrawable(mBackground);
    }

    if (background != null) {
      Rect padding = sThreadLocal.get();
      if (padding == null) {
        padding = new Rect();
        sThreadLocal.set(padding);
      }

      resetResolvedDrawablesInternal();
      background.setLayoutDirection(getLayoutDirection());
      if (background.getPadding(padding)) {
        resetResolvedPaddingInternal();
        switch (background.getLayoutDirection()) {
          case LayoutDirection.RTL:
            mUserPaddingLeftInitial = padding.right;
            mUserPaddingRightInitial = padding.left;
            internalSetPadding(padding.right, padding.top, padding.left,
                padding.bottom);
            break;
          case LayoutDirection.LTR:
          default:
            mUserPaddingLeftInitial = padding.left;
            mUserPaddingRightInitial = padding.right;
            internalSetPadding(padding.left, padding.top, padding.right,
                padding.bottom);
        }
        mLeftPaddingDefined = false;
        mRightPaddingDefined = false;
      }

      // Compare the minimum sizes of the old Drawable and the new.  If there
      // isn't an old or
      // if it has a different minimum size, we should layout again
      if (mBackground == null
          || mBackground.getMinimumHeight() != background.getMinimumHeight()
          || mBackground.getMinimumWidth() != background.getMinimumWidth()) {
        requestLayout = true;
      }

      background.setCallback(this);
      if (background.isStateful()) {
        background.setState(getDrawableState());
      }
      background.setVisible(getVisibility() == VISIBLE, false);
      mBackground = background;

      //applyBackgroundTint(); TODO: Add support for background tint?

      if ((mPrivateFlags & PFLAG_SKIP_DRAW) != 0) {
        mPrivateFlags &= ~PFLAG_SKIP_DRAW;
        mPrivateFlags |= PFLAG_ONLY_DRAWS_BACKGROUND;
        requestLayout = true;
      }
    } else {
      /* Remove the background */
      mBackground = null;

      if ((mPrivateFlags & PFLAG_ONLY_DRAWS_BACKGROUND) != 0) {
        /*
         * This view ONLY drew the background before and we're removing
         * the background, so now it won't draw anything
         * (hence we SKIP_DRAW)
         */
        mPrivateFlags &= ~PFLAG_ONLY_DRAWS_BACKGROUND;
        mPrivateFlags |= PFLAG_SKIP_DRAW;
      }

      /*
       * When the background is set, we try to apply its padding to this
       * View. When the background is removed, we don't touch this View's
       * padding. This is noted in the Javadocs. Hence, we don't need to
       * requestLayout(), the invalidate() below is sufficient.
       */

      // The old background's minimum size could have affected this
      // View's layout, so let's requestLayout
      requestLayout = true;
    }

    computeOpaqueFlags();

    if (requestLayout) {
      requestLayout();
    }

    mBackgroundSizeChanged = true;
    invalidate();
  }
  //endregion

  //region Insets
  /**
   * Returns the optical insets of this view, and computes them in the case that
   * there are no optical insets set.
   *
   * @see #setOpticalInsets(Insets)
   * @see #computeOpticalInsets()
   */
  @NonNull
  public Insets getOpticalInsets() {
    if (mLayoutInsets == null) {
      mLayoutInsets = computeOpticalInsets();
    }

    return mLayoutInsets;
  }

  /**
   * Calculates the optical insets of this view when there are none to be either
   * the insets of the background drawable, or {@link Insets#NONE}.
   */
  @NonNull
  Insets computeOpticalInsets() {
    return (mBackground == null) ? Insets.NONE : mBackground.getOpticalInsets();
  }

  /**
   * Sets this view's optical insets.
   *
   * <p>This method should be treated similarly to {@link #setMeasuredDimension}
   * and not as a general property. Views that compute their own optical insets
   * should call it as part of measurement.
   *
   * <p>Note: This method does not request layout. If you are setting optical
   * insets outside of measure/layout itself, you will want to call
   * {@link #requestLayout()} yourself.
   *
   * @see #getOpticalInsets()
   */
  public void setOpticalInsets(@Nullable Insets insets) {
    mLayoutInsets = insets;
  }
  //endregion

  //region Bounds
  /**
   * Returns the left position of this view, in pixels, relative to its parent.
   */
  public final int getLeft() {
    return mLeft;
  }

  /**
   * Sets the left position of this view relative to its parent. This method is
   * meant to be called by the layout system and should not generally be called
   * otherwise, because the property is volatile may be changed at anytime by
   * the layout system.
   *
   * @param left The new left position of the view, in pixels
   */
  public final void setLeft(int left) {
    if (left != mLeft) {
      invalidate();

      int oldWidth = mRight - mLeft;
      int height = mBottom - mTop;

      mLeft = left;
      sizeChange(mRight - mLeft, height, oldWidth, height);
      mBackgroundSizeChanged = true;
    }
  }

  /**
   * Returns the left position of this view, in pixels, relative to its parent.
   */
  public final int getRight() {
    return mRight;
  }

  /**
   * Sets the right position of this view relative to its parent. This method is
   * meant to be called by the layout system and should not generally be called
   * otherwise, because the property is volatile may be changed at anytime by
   * the layout system.
   *
   * @param right The new right position of the view, in pixels
   */
  public final void setRight(int right) {
    if (right != mRight) {
      invalidate();

      int oldWidth = mRight - mLeft;
      int height = mBottom - mTop;

      mRight = right;
      sizeChange(mRight - mLeft, height, oldWidth, height);
      mBackgroundSizeChanged = true;
    }
  }

  /**
   * Returns the left position of this view, in pixels, relative to its parent.
   */
  public final int getTop() {
    return mTop;
  }

  /**
   * Sets the top position of this view relative to its parent. This method is
   * meant to be called by the layout system and should not generally be called
   * otherwise, because the property is volatile may be changed at anytime by
   * the layout system.
   *
   * @param top The new top position of the view, in pixels
   */
  public final void setTop(int top) {
    if (top != mTop) {
      invalidate();

      int oldHeight = mBottom - mTop;
      int width = mRight - mLeft;

      mTop = top;
      sizeChange(width, mBottom - mTop, width, oldHeight);
      mBackgroundSizeChanged = true;
    }
  }

  /**
   * Returns the left position of this view, in pixels, relative to its parent.
   */
  public final int getBottom() {
    return mBottom;
  }

  /**
   * Sets the bottom position of this view relative to its parent. This method
   * is meant to be called by the layout system and should not generally be
   * called otherwise, because the property is volatile may be changed at
   * anytime by the layout system.
   *
   * @param bottom The new bottom position of the view, in pixels
   */
  public final void setBottom(int bottom) {
    if (bottom != mBottom) {
      invalidate();

      int oldHeight = mBottom - mTop;
      int width = mRight - mLeft;

      mBottom = bottom;
      sizeChange(width, mBottom - mTop, width, oldHeight);
      mBackgroundSizeChanged = true;
    }
  }

  private void sizeChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
    onSizeChange(newWidth, newHeight, oldWidth, oldHeight);
  }

  /**
   * Called when the bounds (size or position) of the view has changed. If this
   * method has not been called before, for example, when first being added to
   * the view hierarchy, then both old values will be {@code 0}.
   *
   * @param w    The current width of this view
   * @param h    The current height of this view
   * @param oldw The previous width of this view
   * @param oldh The previous height of this view
   */
  protected void onSizeChange(int w, int h, int oldw, int oldh) {}

  /**
   * Returns the width of this view, in pixels
   */
  public final int getWidth() {
    return mRight - mLeft;
  }

  /**
   * Returns the height of this view, in pixels
   */
  public final int getHeight() {
    return mBottom - mTop;
  }
  //endregion

  //region Padding
  /**
   * Returns the top padding of this view, in pixels.
   */
  public int getPaddingTop() {
    return mPaddingTop;
  }

  /**
   * Returns the bottom padding of this view, in pixels.
   */
  public int getPaddingBottom() {
    return mPaddingBottom;
  }

  /**
   * Returns the left padding of this view, in pixels.
   */
  public int getPaddingLeft() {
    if (!isPaddingResolved()) {
      resolvePadding();
    }

    return mPaddingLeft;
  }

  /**
   * Returns the right padding of this view, in pixels.
   */
  public int getPaddingRight() {
    if (!isPaddingResolved()) {
      resolvePadding();
    }

    return mPaddingRight;
  }

  /**
   * Returns the start padding of this view depending on its resolved layout
   * direction.
   *
   * @return The start padding of this view, in pixels
   */
  public int getPaddingStart() {
    if (!isPaddingResolved()) {
      resolvePadding();
    }

    return getLayoutDirection() == LayoutDirection.RTL
        ? mPaddingRight : mPaddingLeft;
  }

  /**
   * Returns the end padding of this view depending on its resolved layout
   * direction.
   *
   * @return The end padding of this view, in pixels
   */
  public int getPaddingEnd() {
    if (!isPaddingResolved()) {
      resolvePadding();
    }

    return getLayoutDirection() == LayoutDirection.RTL
        ? mPaddingLeft : mPaddingRight;
  }

  /**
   * Sets the padding of this view.
   *
   * @param left   The left padding, in pixels
   * @param top    The top padding, in pixels
   * @param right  The right padding, in pixels
   * @param bottom The bottom padding, in pixels
   */
  public void setPadding(int left, int top, int right, int bottom) {
    resetResolvedPaddingInternal();

    mUserPaddingStart = UNDEFINED_PADDING;
    mUserPaddingEnd = UNDEFINED_PADDING;

    mUserPaddingLeftInitial = left;
    mUserPaddingRightInitial = right;

    mLeftPaddingDefined = true;
    mRightPaddingDefined = true;

    internalSetPadding(left, top, right, bottom);
  }

  /**
   * This method should normally not be called by subclasses.
   * Use {@link #setPadding(int, int, int, int)} instead.
   *
   * @see #setPadding(int, int, int, int)
   */
  protected void internalSetPadding(int left, int top, int right, int bottom) {
    mUserPaddingLeft = left;
    mUserPaddingRight = right;
    mUserPaddingBottom = bottom;

    // TODO: Support scrollbars with insets
    boolean changed = false;
    if (mPaddingLeft != left) {
      changed = true;
      mPaddingLeft = left;
    }

    if (mPaddingTop != top) {
      changed = true;
      mPaddingTop = top;
    }

    if (mPaddingRight != right) {
      changed = true;
      mPaddingRight = right;
    }

    if (mPaddingBottom != bottom) {
      changed = true;
      mPaddingBottom = bottom;
    }

    if (changed) {
      requestLayout();
      invalidate();
    }
  }

  /**
   * Indicates if the padding has been resolved.
   */
  boolean isPaddingResolved() {
    return (mPrivateFlags & PFLAG_PADDING_RESOLVED) == PFLAG_PADDING_RESOLVED;
  }

  /**
   * Resets the resolved layout direction.
   */
  public void resetResolvedPadding() {
    resetResolvedPaddingInternal();
  }

  /**
   * Used when we only want to reset <i>this<i> view's padding and not trigger
   * overrides in subclasses with children that might reset the children as well.
   */
  void resetResolvedPaddingInternal() {
    mPrivateFlags &= ~PFLAG_PADDING_RESOLVED;
  }

  /**
   * Resolves the padding depending on the layout direction, is applicable.
   */
  public void resolvePadding() {
    final int resolvedLayoutDirection = getLayoutDirection();
    if (mBackground != null && (!mLeftPaddingDefined || !mRightPaddingDefined)) {
      Rect padding = sThreadLocal.get();
      if (padding == null) {
        padding = new Rect();
        sThreadLocal.set(padding);
      }

      mBackground.getPadding(padding);
      if (!mLeftPaddingDefined) {
        mUserPaddingLeftInitial = padding.left;
      }

      if (!mRightPaddingDefined) {
        mUserPaddingRightInitial = padding.right;
      }
    }

    switch (resolvedLayoutDirection) {
      case LayoutDirection.RTL:
        if (mUserPaddingStart != UNDEFINED_PADDING) {
          mUserPaddingRight = mUserPaddingStart;
        } else {
          mUserPaddingRight = mUserPaddingRightInitial;
        }

        if (mUserPaddingEnd != UNDEFINED_PADDING) {
          mUserPaddingLeft = mUserPaddingEnd;
        } else {
          mUserPaddingLeft = mUserPaddingLeftInitial;
        }
        break;

      case LayoutDirection.LTR:
      default:
        if (mUserPaddingStart != UNDEFINED_PADDING) {
          mUserPaddingLeft = mUserPaddingStart;
        } else {
          mUserPaddingLeft = mUserPaddingLeftInitial;
        }

        if (mUserPaddingEnd != UNDEFINED_PADDING) {
          mUserPaddingRight = mUserPaddingEnd;
        } else {
          mUserPaddingRight = mUserPaddingRightInitial;
        }
    }

    mUserPaddingBottom = (mUserPaddingBottom >= 0) ? mUserPaddingBottom : mPaddingBottom;

    internalSetPadding(mUserPaddingLeft, mPaddingTop, mUserPaddingRight, mUserPaddingBottom);
    onRtlPropertiesChanged(resolvedLayoutDirection);

    mPrivateFlags |= PFLAG_PADDING_RESOLVED;
  }
  //endregion

  //region Baseline
  /**
   * Returns the offset of the view's text baseline from the view's top
   * boundary. If this view does not support baseline alignment, this method
   * should return {@code -1}.
   */
  public int getBaseline() {
    return -1;
  }
  //endregion

  //region LayoutDirection
  /**
   * Returns the unresolved layout direction for this view.
   *
   * @return One of {@link LayoutDirection#RTL}, {@link LayoutDirection#LTR},
   *         {@link LayoutDirection#INHERIT}, {@link LayoutDirection#LOCALE}
   */
  @LayoutDirection.Any
  public int getRawLayoutDirection() {
    return (mPrivateFlags & PFLAG_LAYOUT_DIRECTION_MASK) >> PFLAG_LAYOUT_DIRECTION_MASK_SHIFT;
  }

  /**
   * Returns the resolved layout direction of this view.
   *
   * @return {@link LayoutDirection#RTL} if the layout direction is RTL,
   *         otherwise {@link LayoutDirection#LTR}
   */
  @LayoutDirection.Resolved
  public int getLayoutDirection() {
    return (mPrivateFlags & PFLAG_LAYOUT_DIRECTION_RESOLVED_RTL)
        == PFLAG_LAYOUT_DIRECTION_RESOLVED_RTL ? LayoutDirection.RTL : LayoutDirection.LTR;
  }

  /**
   * Sets the layout direction for this view. This will propagate a reset of
   * layout direction resolution to the view's children and resolve layout
   * direction for this view.
   *
   * Resolution will be done if the value is set to {@link LayoutDirection#INHERIT}.
   * The resolution proceeds up the hierarchy of the view to get the value. If
   * there is no parent, then it will return the default {@link LayoutDirection#LTR}.
   *
   * @param direction The layout direction to set. Should be one of:
   *     {@link LayoutDirection#RTL}, {@link LayoutDirection#LTR},
   *     {@link LayoutDirection#INHERIT}, {@link LayoutDirection#LOCALE}
   */
  public void setLayoutDirection(@LayoutDirection.Any int direction) {
    if (getRawLayoutDirection() != direction) {
      mPrivateFlags &= ~PFLAG_LAYOUT_DIRECTION_MASK;
      resetRtlProperties();
      mPrivateFlags |= (direction << PFLAG_LAYOUT_DIRECTION_MASK_SHIFT)
          & PFLAG_LAYOUT_DIRECTION_MASK;
      resolveRtlPropertiesIfNeeded();
      requestLayout();
      invalidate();
    }
  }

  /**
   * Indicates whether or not this view's layout is right-to-left. This is
   * resolved from layout attribute and/or the inherited value from the parent.
   */
  public boolean isLayoutRtl() {
    return getLayoutDirection() == LayoutDirection.RTL;
  }

  /**
   * Resets the resolved layout direction. Layout direction will be resolved
   * during a call to {@link #onMeasure(int, int)}.
   */
  public void resetResolvedLayoutDirection() {
    mPrivateFlags &= ~PFLAG_LAYOUT_DIRECTION_RESOLVED_MASK;
  }

  /**
   * Resolves and caches the layout direction.
   *
   * <p>Precondition: This is implicitly supposing that the parent directionally
   * can and will be resolved before its children. If the parent has not
   * resolved its layout direction yet, this method will return {@code false}.
   *
   * <p>Note: Layout direction is initially set to LTR.
   *
   * @return {@code true} if the layout was resolved, {@code false} otherwise
   */
  public boolean resolveLayoutDirection() {
    mPrivateFlags &= ~PFLAG_LAYOUT_DIRECTION_RESOLVED_MASK;
    switch ((mPrivateFlags & PFLAG_LAYOUT_DIRECTION_MASK) << PFLAG_LAYOUT_DIRECTION_MASK_SHIFT) {
      case LayoutDirection.INHERIT:
        if (!canResolveLayoutDirection()) {
          return false;
        }

        try {
          if (!mParent.isLayoutDirectionResolved()) {
            return false;
          }

          if (mParent.getLayoutDirection() == LayoutDirection.RTL) {
            mPrivateFlags |= PFLAG_LAYOUT_DIRECTION_RESOLVED_RTL;
          }
        } catch (AbstractMethodError e) {
          Log.e(VIEW_LOG_TAG, mParent.getClass().getSimpleName()
              + " does not fully implement ViewParent", e);
        }
        break;
      case LayoutDirection.RTL:
        mPrivateFlags |= PFLAG_LAYOUT_DIRECTION_RESOLVED_RTL;
        break;
      case LayoutDirection.LOCALE:
        if (LocaleUtils.getLayoutDirection(Locale.getDefault()) == LayoutDirection.RTL) {
          mPrivateFlags |= PFLAG_LAYOUT_DIRECTION_RESOLVED_RTL;
        }
        break;
      case LayoutDirection.LTR:
      default:
        // Do nothing. LTR by default
    }

    mPrivateFlags |= PFLAG_LAYOUT_DIRECTION_RESOLVED;
    return true;
  }

  /**
   * Indicates if this layout can resolve its layout direction.
   */
  public boolean canResolveLayoutDirection() {
    switch (getRawLayoutDirection()) {
      case LayoutDirection.INHERIT:
        if (mParent != null) {
          try {
            return mParent.canResolveLayoutDirection();
          } catch (AbstractMethodError e) {
            Log.e(VIEW_LOG_TAG, mParent.getClass().getSimpleName()
                + " does not fully implement ViewParent", e);
          }
        }

        return false;
      default:
        return true;
    }
  }

  /**
   * Indicates if the layout direction is inherited, that is,
   * {@link #getRawLayoutDirection()} returns {@link LayoutDirection#INHERIT}.
   */
  public boolean isLayoutDirectionInherited() {
    return getRawLayoutDirection() == LayoutDirection.INHERIT;
  }

  /**
   * Indicates whether or not the layout direction has been resolved.
   */
  public boolean isLayoutDirectionResolved() {
    return (mPrivateFlags & PFLAG_LAYOUT_DIRECTION_RESOLVED) == PFLAG_LAYOUT_DIRECTION_RESOLVED;
  }
  //endregion

  //region RTL Layout Resolution
  /**
   * Resets resolution of all RTL-related properties.
   */
  public void resetRtlProperties() {
    resetResolvedLayoutDirection();
    resetResolvedPadding();
  }

  /**
   * Resolves all RTL-related properties.
   *
   * @return {@code true} if resolution of RTL properties was needed,
   *         {@code false} otherwise
   */
  public boolean resolveRtlPropertiesIfNeeded() {
    if (!needRtlPropertiesResolution()) {
      return false;
    }

    if (!isLayoutDirectionResolved()) {
      resolveLayoutDirection();
      resolveLayoutParams();
    }

    if (!isPaddingResolved()) {
      resolvePadding();
    }

    onRtlPropertiesChanged(getLayoutDirection());
    return true;
  }

  /**
   * Indicates if RTL properties need resolution.
   */
  private boolean needRtlPropertiesResolution() {
    return (mPrivateFlags & ALL_RTL_PROPERTIES_RESOLVED) != ALL_RTL_PROPERTIES_RESOLVED;
  }

  /**
   * Called when any RTL property (layout direction) has been changed.
   *
   * <p>Override this to take care of cached information that depends on the
   * resolved layout direction, or to inform child views that inherit their
   * layout direction.
   *
   * <p>The default implementation does nothing.
   *
   * @param layoutDirection The new direction of the layout
   *
   * @see LayoutDirection#RTL
   * @see LayoutDirection#LTR
   */
  public void onRtlPropertiesChanged(@LayoutDirection.Resolved int layoutDirection) {}
  //endregion

  //region LayoutParams
  /**
   * Returns the layout parameters association with this view. All views should
   * have layout parameters. These supply parameters to the parent of this view
   * specifying how it should be arranged.
   *
   * This method may return {@code null} if this view is not attached to a
   * parent, or {@link #setLayoutParams} was not invoked successfully. When a
   * view is attached to a parent {@link ViewGroup}, this method must not return
   * {@code null}.
   *
   * @return The layout parameters associated with this view, or {@code null}
   *         if no parameters have been set yet.
   */
  @Nullable
  public ViewGroup.LayoutParams getLayoutParams() {
    return mLayoutParams;
  }

  public void setLayoutParams(@NonNull ViewGroup.LayoutParams params) {
    if (params == null) {
      throw new IllegalArgumentException("Layout parameters cannot be null");
    }

    mLayoutParams = params;
    resolveLayoutParams();
    if (mParent instanceof ViewGroup) {
      ((ViewGroup) mParent).onSetLayoutParams(this, params);
    }

    requestLayout();
  }

  public void resolveLayoutParams() {
    if (mLayoutParams != null) {
      mLayoutParams.resolveLayoutDirection(getLayoutDirection());
    }
  }
  //endregion

  //region Invalidation
  /**
   * Indicates if this view should be invalidated or not. Views which are not
   * visible and which are not running animations should not be invalidated --
   * they will not be drawn and they should not set dirty flags as if they will
   * be drawn.
   */
  private boolean shouldSkipInvalidate() {
    // TODO: Support mCurrentAnimation
    return (mViewFlags & VISIBILITY_MASK) != VISIBLE
        && (!(mParent instanceof ViewGroup)
            || !((ViewGroup) mParent).isViewTransitioning(this));
  }

  /**
   * Invalidates this whole view. If the view is visible, {@link #onDraw} will
   * be called at some point in the future.
   *
   * <p>This must be called from a UI thread. To call from a non-UI thread, use
   * {@link #postInvalidate}.
   */
  public void invalidate() {
    invalidateInternal(0, 0, mRight - mLeft, mBottom - mTop);
  }

  /**
   * Marks the area defined by {@code dirty} as needing to be redrawn. If the
   * view is visible, {@link #onDraw} will be called at some point in the future.
   *
   * <p>This must be called from a UI thread. To call from a non-UI thread, use
   * {@link #postInvalidate}.
   *
   * @param dirty The rectangle representing the bounds of the dirty region
   */
  public void invalidate(@NonNull Rect dirty) {
    final int scrollX = mScrollX;
    final int scrollY = mScrollY;
    invalidateInternal(dirty.left - scrollX, dirty.top - scrollY,
        dirty.right - scrollX, dirty.bottom - scrollY);
  }

  /**
   * Marks the area defined by the rectangle {@code (l,t,r,b)} as needing to
   * be redrawn. The coordinates of the dirty rectangle are relative to the view.
   * If the view is visible, {@link #onDraw} will be called at some point in the
   * future.
   *
   * <p>This must be called from a UI thread. To call from a non-UI thread, use
   * {@link #postInvalidate(int, int, int, int)}.
   *
   * @param l The left position of the dirty region
   * @param t The top position of the dirty region
   * @param r The right position of the dirty region
   * @param b The bottom position of the dirty region
   */
  public void invalidate(int l, int t, int r, int b) {
    final int scrollX = mScrollX;
    final int scrollY = mScrollY;
    invalidateInternal(l - scrollX, t - scrollY, r - scrollX, b - scrollY);
  }

  /**
   * Invalidates the specified region given as {@code (l,t,r,b)}.
   */
  void invalidateInternal(int l, int t, int r, int b) {
    if (shouldSkipInvalidate()) {
      return;
    }

    if ((mPrivateFlags & (PFLAG_DRAWN | PFLAG_HAS_BOUNDS)) == (PFLAG_DRAWN | PFLAG_HAS_BOUNDS)
        || (mPrivateFlags & PFLAG_INVALIDATED) != PFLAG_INVALIDATED) {
      mPrivateFlags &= ~PFLAG_DRAWN;

      mPrivateFlags |= PFLAG_DIRTY;
      mPrivateFlags |= PFLAG_INVALIDATED;

      final AttachInfo ai = mAttachInfo;
      final ViewParent p = mParent;
      if (p != null && ai != null && l < r && t < b) {
        final Rect damage = ai.mTmpInvalRect;
        damage.set(l, t, r, b);
        p.invalidateChild(this, damage);
      }
    }
  }

  /**
   * Invalidates this view on a subsequent cycle through the event loop.
   * Call this to invalidate the view from a non-UI thread.
   *
   * <p>This method can be invoked from outside of the UI thread only when this
   * view is attached to a window.
   *
   * @see #invalidate()
   */
  public void postInvalidate() {
    throw new UnsupportedOperationException();
  }

  /**
   * Invalidates the specified area of this view on a subsequent cycle through
   * the event loop. Call this to invalidate the view from a non-UI thread.
   *
   * <p>This method can be invoked from outside of the UI thread only when this
   * view is attached to a window.
   *
   * @param l The left coordinate of the rectangle to invalidate
   * @param t The top coordinate of the rectangle to invalidate
   * @param r The right coordinate of the rectangle to invalidate
   * @param b The bottom coordinate of the rectangle to invalidate
   *
   * @see #invalidate(int, int, int, int)
   * @see #invalidate(Rect)
   */
  public void postInvalidate(int l, int t, int r, int b) {
    throw new UnsupportedOperationException();
  }
  //endregion

  //region Layout
  /**
   * Indicates whether the view hierarchy is currently undergoing a layout pass.
   * This information is useful to avoid situations such as calling
   * {@link #requestLayout()} during a layout pass.
   */
  public boolean isInLayout() {
    ViewRoot viewRoot = getViewRoot();
    return viewRoot != null && viewRoot.isInLayout();
  }

  /**
   * Requests this view perform a layout. This will schedule a layout pass of
   * the view tree. This should not be called while the view hierarchy is
   * currently in a layout pass {@link #isInLayout}. If a layout is happening,
   * the request may be honored at the end of the current layout pass (and then
   * layout will run again) or after the current frame is drawn and the next
   * layout occurs.
   *
   * <p>Subclasses which override this method should call the superclass method
   * to handle possible request-during-layout errors correctly.
   */
  @CallSuper
  public void requestLayout() {
    if (mMeasureCache != null) {
      mMeasureCache.clear();
    }

    if (mAttachInfo != null && mAttachInfo.mViewRequestingLayout == null) {
      // Only trigger request-during-layout logic if this view is requesting it,
      // not the views in its parent hierarchy
      ViewRoot viewRoot = getViewRoot();
      if (viewRoot != null && viewRoot.isInLayout()) {
        if (!viewRoot.requestLayoutDuringLayout(this)) {
          return;
        }
      }

      mAttachInfo.mViewRequestingLayout = this;
    }

    mPrivateFlags |= PFLAG_WILL_LAYOUT;
    mPrivateFlags |= PFLAG_INVALIDATED;
    if (mParent != null && !mParent.isLayoutRequested()) {
      mParent.requestLayout();
    }

    if (mAttachInfo != null && mAttachInfo.mViewRequestingLayout == this) {
      mAttachInfo.mViewRequestingLayout = this;
    }
  }

  public void forceLayout() {
    if (mMeasureCache != null) {
      mMeasureCache.clear();
    }

    mPrivateFlags |= PFLAG_WILL_LAYOUT;
    mPrivateFlags |= PFLAG_INVALIDATED;
  }

  /**
   * Indicates whether or not this view's layout will be requested during the
   * next hierarchy layout pass.
   *
   * @return {@code true} if a layout will be performed during the next layout pass
   */
  public boolean isLayoutRequested() {
    return (mPrivateFlags & PFLAG_WILL_LAYOUT) == PFLAG_WILL_LAYOUT;
  }

  /**
   * Indicates whether or not {@code o} is a {@link ViewGroup} that is laying
   * out using optical bounds.
   */
  public static boolean isLayoutModeOptical(Object o) {
    return o instanceof ViewGroup && ((ViewGroup) o).isLayoutModeOptical();
  }

  private boolean setOpticalFrame(int left, int top, int right, int bottom) {
    Insets parentInsets = mParent instanceof View
        ? ((View) mParent).getOpticalInsets() : Insets.NONE;
    Insets childInsets = getOpticalInsets();
    return setFrame(
        left   + parentInsets.left - childInsets.left,
        top    + parentInsets.top  - childInsets.top,
        right  + parentInsets.left + childInsets.right,
        bottom + parentInsets.top  + childInsets.bottom);
  }

  public void layout(int l, int t, int r, int b) {
    if ((mPrivateFlags & PFLAG_MEASURE_NEEDED_BEFORE_LAYOUT) != 0) {
      onMeasure(mOldWidthMeasureSpec, mOldHeightMeasureSpec);
      mPrivateFlags &= ~PFLAG_MEASURE_NEEDED_BEFORE_LAYOUT;
    }

    int oldL = mLeft;
    int oldT = mTop;
    int oldR = mRight;
    int oldB = mBottom;

    boolean changed = isLayoutModeOptical(mParent)
        ? setOpticalFrame(l, t, r, b) : setFrame(l, t, r, b);
    if (changed || (mPrivateFlags & PFLAG_LAYOUT_REQUIRED) == PFLAG_LAYOUT_REQUIRED) {
      onLayout(changed, l, t, r, b);
      mPrivateFlags &= ~PFLAG_LAYOUT_REQUIRED;
      // TODO: Notify listeners of change
    }

    mPrivateFlags &= ~PFLAG_WILL_LAYOUT;
    mPrivateFlags |= PFLAG_IS_LAID_OUT;
  }

  /**
   * Called by {@link #layout} when this view should assign a size and position
   * to each of its children.
   *
   * <p>Subclasses classes with children should override this method and call
   * {@link #layout} on each of their children.
   *
   * @param changed {@code true} if this is a new size or position for this view
   * @param left    Left position, relative to parent
   * @param top     Top position, relative to parent
   * @param right   Right position, relative to parent
   * @param bottom  Bottom position, relative to parent
   */
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {}

  /**
   * Assigns a size and position to this view.
   *
   * @param left   Left position, relative to parent
   * @param top    Top position, relative to parent
   * @param right  Right position, relative to parent
   * @param bottom Bottom position, relative to parent
   *
   * @return {@code true} if the new size and/or position is different than the
   *         previous ones, {@code false} otherwise
   */
  protected boolean setFrame(int left, int top, int right, int bottom) {
    boolean changed = false;
    if (DBG) {
      Log.d(VIEW_LOG_TAG,
          this + " View.setFrame(" + left + "," + top + "," + right + "," + bottom + ")");
    }

    if (mLeft != left || mRight != right || mTop != top || mBottom != bottom) {
      changed = true;

      // Remember our drawn bit
      int drawn = mPrivateFlags & PFLAG_DRAWN;

      int oldWidth = mRight - mLeft;
      int oldHeight = mBottom - mTop;
      int newWidth = right - left;
      int newHeight = bottom - top;
      boolean sizeChanged = (newWidth != oldWidth) || (newHeight != oldHeight);

      // Invalidate our old position
      invalidate();

      mLeft = left;
      mTop = top;
      mRight = right;
      mBottom = bottom;

      mPrivateFlags |= PFLAG_HAS_BOUNDS;


      if (sizeChanged) {
        sizeChange(newWidth, newHeight, oldWidth, oldHeight);
      }

      if ((mViewFlags & VISIBILITY_MASK) == VISIBLE) {
        // If we are visible, force the DRAWN bit to on so that
        // this invalidate will go through (at least to our parent).
        // This is because someone may have invalidated this view
        // before this call to setFrame came in, thereby clearing
        // the DRAWN bit.
        mPrivateFlags |= PFLAG_DRAWN;
        invalidate();
      }

      // Reset drawn bit to original value (invalidate turns it off)
      mPrivateFlags |= drawn;

      mBackgroundSizeChanged = true;
    }
    return changed;
  }
  //endregion

  //region Measure
  /**
   * Determines how large this view should be, given the specified constraints.
   *
   * <p>The actual measurement work of a view is performed in {@link #onMeasure},
   * called by this method. Therefore, only {@link #onMeasure} can be overridden
   * by subclasses.
   *
   * @param widthMeasureSpec  Horizontal space requirements, as imposed by the parent
   * @param heightMeasureSpec Vertical space requirements, as imposed by the parent
   */
  public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
    boolean optical = isLayoutModeOptical(this);
    if (optical != isLayoutModeOptical(mParent)) {
      Insets insets = getOpticalInsets();
      int oWidth = insets.left + insets.right;
      int oHeight = insets.top + insets.bottom;
      widthMeasureSpec = MeasureSpec.adjust(widthMeasureSpec, optical ? -oWidth : oWidth);
      heightMeasureSpec = MeasureSpec.adjust(heightMeasureSpec, optical ? -oHeight : oHeight);
    }

    // Suppress sign extension for the low bytes
    long key = (long) widthMeasureSpec << 32 | (long) heightMeasureSpec & 0xffffffffL;
    if (mMeasureCache == null) mMeasureCache = new LongSparseLongArray(2);

    final boolean forceLayout = (mPrivateFlags & PFLAG_WILL_LAYOUT) == PFLAG_WILL_LAYOUT;
    final boolean isExactly = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY &&
        MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY;
    final boolean matchingSize = isExactly &&
        getMeasuredWidth() == MeasureSpec.getSize(widthMeasureSpec) &&
        getMeasuredHeight() == MeasureSpec.getSize(heightMeasureSpec);
    if (forceLayout || !matchingSize &&
        (widthMeasureSpec != mOldWidthMeasureSpec ||
            heightMeasureSpec != mOldHeightMeasureSpec)) {

      // first clears the measured dimension flag
      mPrivateFlags &= ~PFLAG_MEASURED_DIMENSION_SET;

      resolveRtlPropertiesIfNeeded();

      int cacheIndex = forceLayout ? -1 : mMeasureCache.indexOfKey(key);
      if (cacheIndex < 0 || sIgnoreMeasureCache) {
        // measure ourselves, this should set the measured dimension flag back
        onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPrivateFlags &= ~PFLAG_MEASURE_NEEDED_BEFORE_LAYOUT;
      } else {
        long value = mMeasureCache.valueAt(cacheIndex);
        // Casting a long to int drops the high 32 bits, no mask needed
        setMeasuredDimensionRaw((int) (value >> 32), (int) value);
        mPrivateFlags |= PFLAG_MEASURE_NEEDED_BEFORE_LAYOUT;
      }

      // flag not set, setMeasuredDimension() was not invoked, we raise
      // an exception to warn the developer
      if ((mPrivateFlags & PFLAG_MEASURED_DIMENSION_SET) != PFLAG_MEASURED_DIMENSION_SET) {
        throw new IllegalStateException("onMeasure() did not set the measured " +
            "dimension by calling setMeasuredDimension()");
      }

      mPrivateFlags |= PFLAG_LAYOUT_REQUIRED;
    }

    mOldWidthMeasureSpec = widthMeasureSpec;
    mOldHeightMeasureSpec = heightMeasureSpec;

    mMeasureCache.put(key, ((long) mMeasuredWidth) << 32 |
        (long) mMeasuredHeight & 0xffffffffL); // suppress sign extension
  }

  /**
   * Measures the view and its contents to determine the measured width and
   * height. This method is invoked by {@link #measure(int, int)} and should be
   * overridden by subclasses to provide accurate and efficient measurement of
   * their contents.
   *
   * <p><strong>CONTRACT:</strong> When overriding this method, you
   * <em>must</em> call {@link #setMeasuredDimension(int, int)} to store the
   * measured width and height of this view. Failure to do so will trigger an
   * {@link IllegalStateException} to be thrown by {@link #measure(int, int)}.
   * Calling superclass' {@code onMeasure} is a valid use.
   *
   * <p>The base implementation of {@code measure} defaults to the background
   * size, unless a larger size is allowed by the measure spec. Subclasses
   * should override {@code #onMeasure} to provide better measurements of their
   * content.
   *
   * <p>If this method is overridden, it is the subclass's responsibility to
   * make sure the measured width and height are at least the view's minimum
   * {@linkplain #getSuggestedMinimumWidth() width} and {@linkplain
   * #getSuggestedMinimumHeight() height}.
   *
   * @param widthMeasureSpec  Horizontal space requirements, as imposed by the
   *                          parent and encoded using {@link MeasureSpec}
   * @param heightMeasureSpec Vertical space requirements, as imposed by the
   *                          parent and encoded using {@link MeasureSpec}
   *
   * @throws IllegalStateException if this method does not call {@link
   * #setMeasuredDimension(int, int)}
   * @see #getMeasuredWidth()
   * @see #getMeasuredHeight()
   * @see #setMeasuredDimension(int, int)
   * @see #getSuggestedMinimumHeight()
   * @see #getSuggestedMinimumWidth()
   * @see MeasureSpec#getMode(int)
   * @see MeasureSpec#getSize(int)
   */
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(
        getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
        getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
  }

  /**
   * This method must be called by {@link #onMeasure(int, int)} to store the
   * measured width and height. Failing to do so will trigger an exception at
   * measurement time.
   *
   * @param measuredWidth  The measured width of this view. May be a complex bit
   *                       mask as defined by {@link #MEASURED_SIZE_MASK} and
   *                       {@link #MEASURED_STATE_TOO_SMALL}
   * @param measuredHeight The measured height of this view. May be a complex
   *                       bit mask as defined by {@link #MEASURED_SIZE_MASK}
   *                       and {@link #MEASURED_STATE_TOO_SMALL}
   */
  protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
    boolean optical = isLayoutModeOptical(this);
    if (optical != isLayoutModeOptical(mParent)) {
      Insets insets = getOpticalInsets();
      int opticalWidth = insets.left + insets.right;
      int opticalHeight = insets.top + insets.bottom;

      measuredWidth += optical ? opticalWidth : -opticalWidth;
      measuredHeight += optical ? opticalHeight : -opticalHeight;
    }

    setMeasuredDimensionRaw(measuredWidth, measuredHeight);
  }

  /**
   * Sets the measured dimension without extra processing for things like
   * optical bounds. Useful for reapplying consistent values that have already
   * been cooked with adjustments for optical bounds, etc. such as those from
   * the measurement cache.
   *
   * @param measuredWidth  The measured width of this view. May be a complex bit
   *                       mask as defined by {@link #MEASURED_SIZE_MASK} and
   *                       {@link #MEASURED_STATE_TOO_SMALL}
   * @param measuredHeight The measured height of this view. May be a complex
   *                       bit mask as defined by {@link #MEASURED_SIZE_MASK}
   *                       and {@link #MEASURED_STATE_TOO_SMALL}
   */
  private void setMeasuredDimensionRaw(int measuredWidth, int measuredHeight) {
    mMeasuredWidth = measuredWidth;
    mMeasuredHeight = measuredHeight;

    mPrivateFlags |= PFLAG_MEASURED_DIMENSION_SET;
  }

  /**
   * Merges the specified states as returned by {@link #getMeasuredState()}.
   *
   * @param curState The current state as returned from a view or the result of
   *                 combining multiple views
   * @param newState The new view state to combine with {@code curState}
   *
   * @return A new integer reflecting the combination of the two states
   */
  public static int combineMeasuredStates(int curState, int newState) {
    return curState | newState;
  }

  /**
   * Version of {@link #resolveSizeAndState(int, int, int)} returning only the
   * {@link #MEASURED_SIZE_MASK} bits of the result.
   *
   * @param size        How big the view wants to be
   * @param measureSpec The constraints imposed by the parent
   *
   * @return The size information bit mask, as defined by {@link #MEASURED_SIZE_MASK}
   *
   * @see #resolveSizeAndState(int, int, int)
   */
  public static int resolveSize(int size, int measureSpec) {
    return resolveSizeAndState(size, measureSpec, 0) & MEASURED_SIZE_MASK;
  }

  /**
   * Reconciles a desired size and state, with constraints imposed by a
   * {@link MeasureSpec}. Will take the desired size, unless a different size is
   * imposed by the constraints. The returned value is a compound {@link int}
   * with the resolved size in the {@link #MEASURED_SIZE_MASK} bits and
   * optionally the bit {@link #MEASURED_STATE_TOO_SMALL} set if the resulting
   * size is smaller than the size the view wants to be.
   *
   * @param size               How big the view wants to be
   * @param measureSpec        The constraints imposed by the parent
   * @param childMeasuredState The measured state of the child to combine back
   *                           into the result
   *
   * @return The size information bit mask as defined by
   *         {@link #MEASURED_STATE_MASK} and {@link #MEASURED_STATE_TOO_SMALL}
   */
  public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
    int result;
    @MeasureSpec.Mode
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);
    switch (specMode) {
      case MeasureSpec.UNSPECIFIED:
        result = size;
        break;
      case MeasureSpec.AT_MOST:
        result = specSize < size
            ? specSize | MEASURED_STATE_TOO_SMALL
            : size;
        break;
      case MeasureSpec.EXACTLY:
        result = specSize;
        break;
      default:
        result = size;
    }

    return result | (childMeasuredState & MEASURED_STATE_MASK);
  }

  /**
   * Returns a default size for this view using the supplied size if the {@link
   * MeasureSpec} imposed no constraints. Will increase in size if allowed by
   * the measure spec.
   *
   * @param size        The default size for this view
   * @param measureSpec The constraints imposed by the parent
   *
   * @return The size this view should be
   */
  public static int getDefaultSize(int size, int measureSpec) {
    int result;
    @MeasureSpec.Mode
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);
    switch (specMode) {
      case MeasureSpec.UNSPECIFIED:
        result = size;
        break;
      case MeasureSpec.AT_MOST:
      case MeasureSpec.EXACTLY:
        result = specSize;
        break;
      default:
        result = size;
    }

    return result;
  }

  /**
   * Returns the suggested minimum width that this view should use. This returns
   * the maximum of the view's minimum width and the background's {@link
   * Drawable#getMinimumWidth() minimum width}.
   *
   * <p>When being used in {@link #onMeasure(int, int)}, the caller should still
   * ensure the returned width is within the requirements of the parent.
   *
   * @return The suggested minimum width of the view
   */
  protected int getSuggestedMinimumWidth() {
    return (mBackground == null)
        ? mMinWidth
        : Math.max(mMinWidth, mBackground.getMinimumWidth());
  }

  /**
   * Returns the suggested minimum height that this view should use. This
   * returns the maximum of the view's minimum height and the background's
   * {@link Drawable#getMinimumHeight() minimum height}.
   *
   * <p>When being used in {@link #onMeasure(int, int)}, the caller should
   * still ensure the returned height is within the requirements of the parent.
   *
   * @return The suggested minimum height of the view
   */
  protected int getSuggestedMinimumHeight() {
    return (mBackground == null)
        ? mMinHeight
        : Math.max(mMinHeight, mBackground.getMinimumHeight());
  }

  /**
   * Like {@link #getMeasuredWidthAndState()}, but only returns the raw width
   * component (that is, the result is masked by {@link #MEASURED_SIZE_MASK}).
   *
   * @return The raw measured width of this view
   */
  public final int getMeasuredWidth() {
    return mMeasuredWidth & MEASURED_SIZE_MASK;
  }

  /**
   * Returns the full width measurement information for this view as computed by
   * the most recent call to {@link #measure(int, int)}. This result is a bit
   * mask as defined by {@link #MEASURED_SIZE_MASK} and {@link
   * #MEASURED_STATE_TOO_SMALL}. This should be used during measurement and
   * layout calculations only. Use {@link #getWidth()} to see how wide a view is
   * after layout.
   *
   * @return The measured width of this view as a bit mask
   */
  public final int getMeasuredWidthAndState() {
    return mMeasuredWidth;
  }

  /**
   * Like {@link #getMeasuredHeightAndState()}, but only returns the raw height
   * component (that is, the result is masked by {@link #MEASURED_SIZE_MASK}).
   *
   * @return The raw measured height of this view
   */
  public final int getMeasuredHeight() {
    return mMeasuredHeight & MEASURED_SIZE_MASK;
  }

  /**
   * Returns the full height measurement information for this view as computed
   * by the most recent call to {@link #measure(int, int)}. This result is a bit
   * mask as defined by {@link #MEASURED_SIZE_MASK} and {@link
   * #MEASURED_STATE_TOO_SMALL}. This should be used during measurement and
   * layout calculations only. Use {@link #getHeight()} to see how tall a view
   * is after layout.
   *
   * @return The measured height of this view as a bit mask
   */
  public final int getMeasuredHeightAndState() {
    return mMeasuredHeight;
  }

  /**
   * Returns only the state bits of {@link #getMeasuredWidthAndState()} and
   * {@link #getMeasuredHeightAndState()}, combined into one {@code int}. the
   * width component is in the bits defined by {@link #MEASURED_STATE_MASK}, and
   * the height component is at the shifted bits <code>{@link
   * #MEASURED_HEIGHT_STATE_SHIFT} >> {@link #MEASURED_STATE_TOO_SMALL}</code>.
   *
   * @return The state bits of {@link #getMeasuredWidthAndState()} and {@link
   * #getMeasuredHeightAndState()}
   */
  public final int getMeasuredState() {
    return (mMeasuredWidth & MEASURED_STATE_MASK)
        | ((mMeasuredHeight >> MEASURED_HEIGHT_STATE_SHIFT)
        & (MEASURED_STATE_MASK >> MEASURED_HEIGHT_STATE_SHIFT));
  }
  //endregion

  //region Draw
  /**
   * Sets whether or not this view does not draw anything. This setting allows
   * optimizations to be made if this view does not draw anything on its own. By
   * default, this flag is not set on a view, but could be set on some view
   * subclasses, such as {@link ViewGroup}.
   *
   * Typically, if you override {@link #onDraw}, you should clear this flag.
   *
   * @param willNotDraw {@code true} if this view will draw on its own,
   *                    {@code false} otherwise
   */
  public void setWillNotDraw(boolean willNotDraw) {
    setFlags(willNotDraw ? WILL_NOT_DRAW : 0, DRAW_MASK);
  }

  /**
   * Indicates whether or not this view draws on its own.
   */
  public boolean willNotDraw() {
    return (mViewFlags & DRAW_MASK) == WILL_NOT_DRAW;
  }

  /**
   * Manually renders this view (and all of its children) to the given canvas.
   * The view must have already done a full layout before this function is
   * called. When implementing a view, implement {@link #onDraw(Canvas)} instead
   * of overriding this method. If you need to override this method, make sure
   * to call the superclass version.
   *
   * @param canvas The canvas on which to draw the content of this view
   */
  @CallSuper
  public void draw(@NonNull Canvas canvas) {
    final int privateFlags = mPrivateFlags;
    final boolean dirtyOpaque = (privateFlags & PFLAG_DIRTY_MASK) == PFLAG_DIRTY_OPAQUE
        && (mAttachInfo == null || !mAttachInfo.mIgnoreDirtyState);
    mPrivateFlags = (privateFlags & ~PFLAG_DIRTY_MASK) | PFLAG_DRAWN;

    /**
     * Draw traversal performs several drawing steps which must be executed
     * in the appropriate order:
     *
     *   1. Draw the background
     *   2. If necessary, save the canvas' layers to prepare for fading
     *   3. Draw view's content
     *   4. Draw children
     *   5. If necessary, draw the fading edges and restore layers
     *   6. Draw decorations (scrollbars for instance)
     */
    if (!dirtyOpaque) {
      drawBackground(canvas);
    }

    if (!dirtyOpaque) {
      onDraw(canvas);
    }

    dispatchDraw(canvas);
  }

  /**
   * Called when the view should draw its content.
   *
   * @param canvas The canvas on which to draw the content
   */
  protected void onDraw(@NonNull Canvas canvas) {}

  /**
   * Called by {@link #draw} when this view should draw its children. This
   * should be overridden by subclasses to gain control just before its children
   * are drawn (but after its own view has been drawn).
   *
   * @param canvas The canvas on which to draw the view
   */
  protected void dispatchDraw(@NonNull Canvas canvas) {}

  private void drawBackground(@NonNull Canvas canvas) {
    final Drawable background = mBackground;
    if (background == null) {
      return;
    }

    if (mBackgroundSizeChanged) {
      background.setBounds(0, 0, mRight - mLeft, mBottom - mTop);
      mBackgroundSizeChanged = false;
    }

    final int scrollX = mScrollX;
    final int scrollY = mScrollY;
    if ((scrollX | scrollY) == 0) {
      background.draw(canvas);
    } else {
      canvas.translate(scrollX, scrollY);
      background.draw(canvas);
      canvas.translate(-scrollX, -scrollY);
    }
  }

  protected void drawDebug(@NonNull Canvas canvas) {}
  //endregion

  //region Drawables
  //region Drawable.Callback
  @Override
  public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
    if (verifyDrawable(who) && what != null) {
      if (mAttachInfo != null) {
        Message msg = mAttachInfo.mViewRoot.mHandler.obtainMessage(
            ViewRoot.MSG_DO_CALLBACK);
        mAttachInfo.mViewRoot.mHandler.sendMessageAtTime(msg, when);
      } else {
        // FIXME: schedule drawable with view that isn't parented
      }
    }
  }

  @Override
  public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
    if (verifyDrawable(who) && what != null) {
      if (mAttachInfo != null) {
        // TODO: This will not work the same as in Android due to lack of a Choreographer
        mAttachInfo.mViewRoot.mHandler.removeMessages(what, who);
      }
    }
  }

  public void unscheduleDrawable(@Nullable Drawable who) {
    if (mAttachInfo != null && who != null) {
      // FIXME: This will not work correctly, as action == null
      mAttachInfo.mViewRoot.mHandler.removeCallbacks(null, who);
    }
  }

  @Override
  public void invalidateDrawable(@NonNull Drawable drawable) {
    if (verifyDrawable(drawable)) {
      final Rect dirty = drawable.getDirtyBounds();
      final int scrollX = mScrollX;
      final int scrollY = mScrollY;
      invalidate(dirty.left + scrollX, dirty.top + scrollY,
          dirty.right + scrollX, dirty.bottom + scrollY);
    }
  }
  //endregion

  /**
   * Resolves the drawables depending on the layout direction.
   *
   * <p>Precondition: This is implicitly supposing that the view directionally
   * can and will be resolved before its drawables.
   *
   * <p>Note: This method will call {@link View#onResolveDrawables(int)} when
   * resolution is done.
   */
  protected void resolveDrawables() {
    if (!isLayoutDirectionResolved() && getRawLayoutDirection() == LayoutDirection.INHERIT) {
      return;
    }

    final int layoutDirection = isLayoutDirectionResolved()
        ? getLayoutDirection() : getRawLayoutDirection();
    if (mBackground != null) {
      mBackground.setLayoutDirection(layoutDirection);
    }

    mPrivateFlags |= PFLAG_DRAWABLE_RESOLVED;
    onResolveDrawables(layoutDirection);
  }

  /**
   * Indicates whether or not the drawables are resolved.
   */
  boolean areDrawablesResolved() {
    return (mPrivateFlags & PFLAG_DRAWABLE_RESOLVED) == PFLAG_DRAWABLE_RESOLVED;
  }

  /**
   * Called when layout direction has been resolved.
   *
   * <p>Note: The default implementation does nothing.
   *
   * @param layoutDirection The resolved layout direction, either
   *                        {@link LayoutDirection#LTR} or {@link LayoutDirection#RTL}
   *
   * @see LayoutDirection#LTR
   * @see LayoutDirection#RTL
   */
  public void onResolveDrawables(@LayoutDirection.Resolved int layoutDirection) {}

  /**
   * Resets the resolved drawable state.
   */
  protected void resetResolvedDrawables() {
    resetResolvedDrawablesInternal();
  }

  /**
   * Resets the resolved drawable state.
   */
  void resetResolvedDrawablesInternal() {
    mPrivateFlags &= ~PFLAG_DRAWABLE_RESOLVED;
  }

  /**
   * Indicates whether or not this view displays the specified drawable.
   * Subclasses of {@code View} that display their own drawables should override
   * this method and return {@code true} for any drawable it is displaying. This
   * allows animations for those drawables to be scheduled.
   *
   * <p>Be sure to call through to the super class when overriding this method.
   *
   * @return {@code true} if the drawable is being displayed in this view,
   * {@code false} otherwise and it is not allowed to animate.
   *
   * @see #unscheduleDrawable(Drawable, Runnable)
   * @see #drawableStateChanged()
   */
  @CallSuper
  protected boolean verifyDrawable(@NonNull Drawable who) {
    return who == mBackground;
  }

  /**
   * Called whenever the state of the view changes in such a way that it impacts
   * the state of drawables being shown.
   *
   * TODO: Add/document support for StateListAnimator
   *
   * <p>Be sure to call through to super class when overriding this method.
   *
   * @see Drawable#setState(int[])
   */
  @CallSuper
  protected void drawableStateChanged() {
    final Drawable d = mBackground;
    if (d != null && d.isStateful()) {
      d.setState(getDrawableState());
    }

    // TODO: Implement support for StateListAnimator
  }

  /**
   * Forces this widget to update its drawable state. This will cause .. to be
   * called on this widget. Widgets that are interested in the new state should
   * call {@link #getDrawableState()}.
   *
   * @see #drawableStateChanged
   * @see #getDrawableState
   */
  public void refreshDrawableState() {
    mPrivateFlags |= PFLAG_DRAWABLE_STATE_DIRTY;
    drawableStateChanged();

    ViewParent parent = mParent;
    if (parent != null) {
      parent.childDrawableStateChanged(this);
    }
  }

  /**
   * Returns an array of resource IDs of the drawable states representing the
   * current state of this widget.
   *
   * @return The current drawable state
   *
   * @see Drawable#setState(int[])
   * @see #drawableStateChanged
   * @see #onCreateDrawableState(int)
   */
  @NonNull
  public final int[] getDrawableState() {
    if (mDrawableState != null && (mPrivateFlags & PFLAG_DRAWABLE_STATE_DIRTY) == 0) {
      return mDrawableState;
    } else {
      mDrawableState = onCreateDrawableState(0);
      mPrivateFlags &= ~PFLAG_DRAWABLE_STATE_DIRTY;
      return mDrawableState;
    }
  }

  /**
   * Generates the new {@link Drawable} state for this widget. This is called by
   * the widget system when the cached drawable state is invalidated. To
   * retrieve the current state, you should use {@link #getDrawableState()}.
   *
   * @param extraSpace If non-zero, this number will indicate the number of
   *                   extra entries you would like in the returned array, in
   *                   which you can place your own states
   *
   * @return An array holding the current {@link Drawable} state of this widget
   *
   * @see #mergeDrawableStates(int[], int[])
   */
  @NonNull
  protected int[] onCreateDrawableState(int extraSpace) {
    if ((mViewFlags & DUPLICATE_PARENT_STATE) == DUPLICATE_PARENT_STATE
        && mParent instanceof View) {
      return ((View) mParent).onCreateDrawableState(extraSpace);
    }

    int privateFlags = mPrivateFlags;

    int viewStateIndex = 0;
    // TODO: Implement support for all the copied states
    int[] drawableState = null;

    if (DBG) {
      Log.i(VIEW_LOG_TAG, "drawableStateIndex=" + viewStateIndex);
    }

    if (extraSpace == 0) {
      return drawableState;
    }

    final int[] fullState;
    if (drawableState != null) {
      fullState = new int[drawableState.length + extraSpace];
      System.arraycopy(drawableState, 0, fullState, 0, drawableState.length);
    } else {
      fullState = new int[extraSpace];
    }

    return fullState;
  }

  /**
   * Merges your own state values in {@code additionalState} into the base state
   * values {@code baseState} that were returned by {@link
   * #onCreateDrawableState(int)}.
   *
   * @param baseState       The base state values returned by {@link
   *                        #onCreateDrawableState(int)}, which will be modified
   *                        to also hold your own additional state values
   * @param additionalState The additional state values you would like added to
   *                        {@code baseState}; this array is not modified
   *
   * @return The initial {@code baseState} array you originally passed in
   *
   * @see #onCreateDrawableState(int)
   */
  @NonNull
  protected static int[] mergeDrawableStates(@NonNull int[] baseState, @NonNull int[] additionalState) {
    final int N = baseState.length;
    int i = N - 1;
    while (i >= 0 && baseState[i] == 0) {
      i--;
    }

    System.arraycopy(additionalState, 0, baseState, i + 1, additionalState.length);
    return baseState;
  }

  /**
   * Calls {@link Drawable#jumpToCurrentState() Drawable.jumpToCurrentState()}
   * on all drawables associated with this view.
   *
   * TODO: Add/document support for StateListAnimator
   */
  public void jumpDrawablesToCurrentState() {
    if (mBackground != null) {
      mBackground.jumpToCurrentState();
    }
  }
  //endregion

  //region Focus
  /**
   * Indicates whether or not this view is able to take focus.
   */
  public final boolean isFocusable() {
    return (mViewFlags & FOCUSABLE_MASK) == FOCUSABLE;
  }

  /**
   * Sets whether or not this view can receive focus.
   *
   * <p>Note: Setting this to {@code false} will ensure that this view is not
   * focusable in touch mode.
   *
   * @see #setFocusableInTouchMode(boolean)
   */
  public void setFocusable(boolean focusable) {
    if (!focusable) {
      setFlags(0, FOCUSABLE_IN_TOUCH_MODE);
    }

    setFlags(focusable ? FOCUSABLE : NOT_FOCUSABLE, FOCUSABLE_MASK);
  }

  /**
   * Indicates whether or not this view is focusable when in touch mode.
   *
   * <p>This is useful for cases such as a button, which would like focus when
   * the user is navigating via a directional pad so that the user can select
   * it, but once the user starts touching the screen, the button shouldn't take
   * focus.
   */
  public final boolean isFocusableInTouchMode() {
    return (mViewFlags & FOCUSABLE_IN_TOUCH_MODE) == FOCUSABLE_IN_TOUCH_MODE;
  }

  /**
   * Sets whether or not this view can receive focus while in touch mode.
   *
   * <p>Note: Setting this to {@code true} will also ensure this view is focusable.
   */
  public void setFocusableInTouchMode(boolean focusableInTouchMode) {
    setFlags(focusableInTouchMode ? FOCUSABLE_IN_TOUCH_MODE : 0, FOCUSABLE_IN_TOUCH_MODE);
    if (focusableInTouchMode) {
      setFlags(FOCUSABLE, FOCUSABLE_MASK);
    }
  }

  /**
   * Indicates whether or not this view has focus itself, or is the ancestor of
   * the view that has focus.
   */
  public boolean hasFocus() {
    return (mPrivateFlags & PFLAG_FOCUSED) != 0;
  }

  /**
   * Indicates whether or not this view is focusable, or if it contains a
   * reachable view for this method returns {@code true}. A reachable view that
   * is focusable is a view whose parents do not block descendants' focus.
   */
  public boolean hasFocusable() {
    if (!isFocusableInTouchMode()) {
      for (ViewParent p = mParent; p instanceof ViewGroup; p = p.getParent()) {
        ViewGroup g = (ViewGroup) p;
        if (g.shouldBlockFocusForTouchscreen()) {
          return false;
        }
      }
    }

    return (mViewFlags & VISIBILITY_MASK) == VISIBLE && isFocusable();
  }

  /**
   * Called when this view wants to give up focus. If focus is cleared,
   * {@link #onFocusChanged} is called.
   *
   * <p>Note: When a view clears focus, the framework is trying to give focus to
   * the first focusable view from the top. Hence, if this view is the top-level
   * view that can take focus, then all callbacks related to clearing focus will
   * be invoked after which the framework will give focus to the view.
   */
  public void clearFocus() {
    if (DBG) {
      Log.d(VIEW_LOG_TAG, this + " clearFocus()");
    }

    clearFocusInternal(null, true, true);
  }

  /**
   * Called internally by the view system when a new view is getting focus. This
   * method clears the old focus.
   *
   * <p><b>Note:</b> The parent view's focused child must be updated manually
   * after calling this method. Failing to do so may leave the view hierarchy in
   * an inconsistent state.
   *
   * @param focused The currently focused view, or {@code null} if no view is
   *                focused (that is, focus is being cleared)
   */
  void unFocus(@Nullable View focused) {
    if (DBG) {
      Log.d(VIEW_LOG_TAG, this + " unFocus()");
    }

    clearFocusInternal(focused, false, false);
  }

  /**
   * Clears focus from the view, optionally propagating the change up through
   * the parent hierarchy and requesting that the root view place new focus.
   *
   * @param focused   The currently focused view, or {@code null} if no view is
   *                  focused (that is, focus is being cleared)
   * @param propagate {@code true} to propagate the change up through the parent
   *                  hierarchy, {@code false} otherwise
   * @param refocus   If {@code propagate} is {@code true}, {@code true} to
   *                  request that the root view place new focus, {@code false}
   *                  otherwise
   */
  void clearFocusInternal(@Nullable View focused, boolean propagate, boolean refocus) {
    if ((mPrivateFlags & PFLAG_FOCUSED) != 0) {
      mPrivateFlags &= ~PFLAG_FOCUSED;
      if (propagate && mParent != null) {
        mParent.clearChildFocus(this);
      }

      onFocusChanged(false, 0, null);
      refreshDrawableState();
      if (propagate && (!refocus || !rootViewRequestFocus())) {
        notifyGlobalFocusCleared(this);
      }
    }
  }

  void notifyGlobalFocusCleared(@Nullable View oldFocus) {
    if (oldFocus != null && mAttachInfo != null) {
      mAttachInfo.mTreeObserver.dispatchOnGlobalFocusChange(oldFocus, null);
    }
  }

  boolean rootViewRequestFocus() {
    final View root = getRootView();
    return root != null && root.requestFocus();
  }

  /**
   * Called by the view system when the focus state of this view changes.
   *
   * <p>When the focus change event is caused by directional navigation, {@code
   * direction} and {@code previouslyFocusedRect} provide insight into where the
   * focus is coming from. When overriding this method, be sure to call up
   * through to the super class method so that the standard focus handling will
   * occur.
   *
   * @param gainFocus {@code true} if the view has focus, {@code false} otherwise
   * @param direction The direction focus has moved when {@code #requestFocus}
   *                  is called to give this view focus. Values are
   *                  {@link FocusDirection#UP}, {@link FocusDirection#DOWN},
   *                  {@link FocusDirection#LEFT}, {@link FocusDirection#RIGHT},
   *                  {@link FocusDirection#FORWARD}, or {@link FocusDirection#BACKWARD}.
   *                  It may not always apply, in which case use the default.
   * @param previouslyFocusedRect The rectangle, in which this view's coordinate
   *                  system of the previously focused view. If applicable, this
   *                  will be passed in as finer-grained information about where
   *                  the focus is coming from (in addition to direction),
   *                  otherwise {@code null}.
   */
  @CallSuper
  protected void onFocusChanged(boolean gainFocus, @FocusDirection.Any int direction,
                               @Nullable Rect previouslyFocusedRect) {} //TODO: impl

  /**
   * Requests that this view, or one of its descendants, takes focus.
   *
   * <p>A view will not actually take focus if it is not {@linkplain #isFocusable
   * focusable}, or if it is focusable and it is not {@linkplain #isFocusableInTouchMode
   * focusable in touch mode} while in touch mode.
   *
   * <p>See {@link #focusSearch}, which is a variation of this method to say
   * that you have focus and want your parent to look for the next one.
   *
   * <p>This is equivalent to calling {@link #requestFocus(int, Rect)} a {@code
   * null} previously focused rectangle.
   *
   * @return {@code true} if this view or one of its descendants took focus as a
   *         result of this call, {@code false} otherwise
   */
  public final boolean requestFocus() {
    return requestFocus(FocusDirection.DOWN);
  }

  /**
   * Requests that this view, or one of its descendants, takes focus and
   * provides it with a hint about what direction focus is heading.
   *
   * <p>A view will not actually take focus if it is not {@linkplain #isFocusable
   * focusable}, or if it is focusable and it is not {@linkplain #isFocusableInTouchMode
   * focusable in touch mode} while in touch mode.
   *
   * <p>See {@link #focusSearch}, which is a variation of this method to say
   * that you have focus and want your parent to look for the next one.
   *
   * <p>This is equivalent to calling {@link #requestFocus(int, Rect)} a {@code
   * null} previously focused rectangle.
   *
   * @param direction One of {@link FocusDirection#UP}, {@link FocusDirection#DOWN},
   *                  {@link FocusDirection#LEFT}, or {@link FocusDirection#RIGHT}
   *
   * @return {@code true} if this view or one of its descendants took focus as a
   *         result of this call, {@code false} otherwise
   */
  public final boolean requestFocus(@FocusDirection.Real int direction) {
    return requestFocus(direction, null);
  }

  /**
   * Requests that this view, or one of its descendants, takes focus and gives
   * it hints about the direction and a specific rectangle that the focus is
   * coming from. The rectangle can help give larger views a finer-grained hint
   * about where focus is coming from, and therefore, where to show selection,
   * or forward focus change internally.
   *
   * <p>A view will not actually take focus if it is not {@linkplain #isFocusable
   * focusable}, or if it is focusable and it is not {@linkplain #isFocusableInTouchMode
   * focusable in touch mode} while in touch mode.
   *
   * <p>A view will not take focus if it is not visible.
   *
   * <p>A view will not take focus if one of its parents has
   * {@link unifi.view.ViewGroup#getDescendantFocusability()} equal to
   * {@link ViewGroup#FOCUS_BLOCK_DESCENDANTS}.
   *
   * <p>This is equivalent to calling {@link #requestFocus(int, Rect)} a {@code
   * null} previously focused rectangle.
   *
   * <p>You may wish to override this method if your custom view has an internal
   * view that it wishes to forward the request to.
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
  public boolean requestFocus(@FocusDirection.Real int direction,
                              @Nullable Rect previouslyFocusedRect) {
    return requestFocusNoSearch(direction, previouslyFocusedRect);
  }

  private boolean requestFocusNoSearch(@FocusDirection.Real int direction,
                                       @Nullable Rect previouslyFocusedRect) {
    if ((mViewFlags & FOCUSABLE_MASK) != FOCUSABLE
        || (mViewFlags & VISIBILITY_MASK) != VISIBLE) {
      return false;
    }

    if (isInTouchMode() && (mViewFlags & FOCUSABLE_IN_TOUCH_MODE) != FOCUSABLE_IN_TOUCH_MODE) {
      return false;
    }

    if (hasAncestorThatBlocksDescendantFocus()) {
      return false;
    }

    handleFocusGainInternal(direction, previouslyFocusedRect);
    return true;
  }

  /**
   * Indicates whether or not and ancestor of this view blocks descendant focus.
   */
  private boolean hasAncestorThatBlocksDescendantFocus() {
    final boolean focusableInTouchMode = isFocusableInTouchMode();
    ViewParent ancestor = mParent;
    while (ancestor instanceof ViewGroup) {
      final ViewGroup vgAncestor = (ViewGroup) ancestor;
      if (vgAncestor.getDescendantFocusability() == ViewGroup.FOCUS_BLOCK_DESCENDANTS
          || (!focusableInTouchMode && vgAncestor.shouldBlockFocusForTouchscreen())) {
        return true;
      } else {
        ancestor = vgAncestor.getParent();
      }
    }

    return false;
  }

  /**
   * Requests that this view, or one of its descendants, takes focus in touch
   * mode. This is a special variant of {@link #requestFocus} that will allow
   * views that are not focusable in touch mode to request focus when they are
   * touched.
   *
   * @return {@code true} if this view, or one of its descendants, took focus as
   *         a result of this operation, {@code false} otherwise
   *
   * @see #isInTouchMode()
   */
  public final boolean requestFocusFromTouch() {
    if (isInTouchMode()) {
      ViewRoot viewRoot = getViewRoot();
      if (viewRoot != null) {
        viewRoot.ensureTouchMode(false);
      }
    }

    return requestFocus(FocusDirection.DOWN);
  }

  void handleFocusGainInternal(@FocusDirection.Real int direction,
                               @Nullable Rect previouslyFocusedRect) {
    if (DBG) {
      Log.d(VIEW_LOG_TAG, this + " requestFocus()");
    }

    if ((mPrivateFlags & PFLAG_FOCUSED) == 0) {
      mPrivateFlags |= PFLAG_FOCUSED;

      View oldFocus = mAttachInfo != null ? getRootView().findFocus() : null;

      if (mParent != null) {
        mParent.requestChildFocus(this, this);
      }

      if (mAttachInfo != null) {
        mAttachInfo.mTreeObserver.dispatchOnGlobalFocusChange(oldFocus, this);
      }

      onFocusChanged(true, direction, previouslyFocusedRect);
      refreshDrawableState();
    }
  }

  /**
   * Finds the nearest view in the specified direction that can take focus.
   * This does not actually give focus to that view.
   *
   * @param direction One of {@link FocusDirection#UP}, {@link FocusDirection#DOWN},
   *                  {@link FocusDirection#LEFT}, or {@link FocusDirection#RIGHT}
   *
   * @return The nearest focusable in the specified direction, or {@code null}
   *         if none can be found
   */
  @Nullable
  public View focusSearch(@FocusDirection.Real int direction) {
    if (mParent != null) {
      return mParent.focusSearch(this, direction);
    }

    return null;
  }

  /**
   * Indicates whether or not this view has focus.
   */
  public boolean isFocused() {
    return (mPrivateFlags & PFLAG_FOCUSED) != 0;
  }

  /**
   * Finds the view in the hierarchy rooted at this view that currently has
   * focus, or {@code null} if no focused view can be found.
   */
  @Nullable
  public View findFocus() {
    return (mPrivateFlags & PFLAG_FOCUSED) != 0 ? this : null;
  }
  //endregion

  //region Hit
  /**
   * Indicates whether or not the cursor is over this view.
   */
  public boolean isOver() {
    return (mPrivateFlags & PFLAG_OVER) == PFLAG_OVER;
  }

  /**
   * Sets whether or not this view has the cursor over it.
   */
  protected void setOver(boolean over) {
    if (over != isOver()) {
      if (over) {
        mPrivateFlags |= PFLAG_OVER;
      } else {
        mPrivateFlags &= ~PFLAG_OVER;
      }
    }
  }

  /**
   * Indicates whether or not the specified coordinates lie within the bounds
   * of this view.
   */
  final boolean pointInView(float x, float y) {
    return mLeft <= x && x <= mRight
        && mBottom <= y && y <= mTop;
  }

  /**
   * Returns the deepest view in this view at the specified coordinates, or
   * {@code null} if none is found.
   */
  @Nullable
  View hitDeepestFocusableView(float x, float y) {
    if (pointInView(x, y) && hasFocusable()) {
      return this;
    }

    return null;
  }
  //endregion

  /**
   * A set of information given to a view when it is attached to a parent window.
   */
  static final class AttachInfo {
    /**
     * Root view of the window. This will be the Unifi container.
     *
     * <p>Note: Do not confuse this with {@link #mRootView}, as they are two
     * distinct things.
     */
    @NonNull
    final ViewRoot mViewRoot;

    @NonNull
    final Rect mTmpInvalRect = new Rect();

    /**
     * The view tree observer used to dispatch global events like layout,
     * pre-draw, touch mode changes, etc.
     */
    @NonNull
    final ViewTreeObserver mTreeObserver = new ViewTreeObserver();

    /**
     * The top view in the hierarchy. This will be the top-level view ancestor
     * of this view.
     *
     * <p>Note: Do not confuse this with {@link #mViewRoot}, as they are two
     * distinct things.
     */
    @Nullable
    View mRootView;

    /**
     * Indicates if the visibility of any views have changed. Used to determine
     * if redraw is needed.
     */
    boolean mViewVisibilityChanged;

    /**
     * Indicates whether or not ignoring the {@link #PFLAG_DIRTY_MASK} flags.
     */
    boolean mIgnoreDirtyState;

    /**
     * Used to track which view originated a {@link #requestLayout()} call, used
     * when {@link #requestLayout()} is called during layout.
     */
    @Nullable
    View mViewRequestingLayout;

    /**
     * The current visibility of the window.
     */
    @Visibility int mWindowVisibility;

    /**
     * Show where the margins, bounds and layout bounds are for each view.
     */
    boolean mDebugLayout = true;

    /**
     * Indicates whether or not the view's window is currently in touch mode.
     */
    boolean mInTouchMode;

    AttachInfo(@NonNull ViewRoot viewRoot) {
      mViewRoot = viewRoot;
    }

  }
}
