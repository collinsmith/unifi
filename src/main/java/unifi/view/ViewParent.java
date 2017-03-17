package unifi.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import unifi.graphics.Rect;
import unifi.util.FocusDirection;
import unifi.util.LayoutDirection;

/**
 * Defines the responsibilities for a class that will be a parent of a {@link
 * View}. This is the API that a view sees when it wants to interact with its
 * parent.
 */
public interface ViewParent {
  /**
   * Called when something has changed which has invalidated the layout of a
   * child of this view parent. This will schedule a layout pass of the view
   * tree.
   */
  void requestLayout();

  /**
   * Indicates whether or not a layout was {@linkplain #requestLayout requested}
   * on this view parent.
   */
  boolean isLayoutRequested();

  /**
   * Returns the parent of this view parent, if it exists, or {@code null} if
   * parent does not have a parent.
   */
  @Nullable
  ViewParent getParent();

  /**
   * Indicates that all or part of a child is dirty and needs to be redrawn.
   *
   * @param child The child which is dirty
   * @param r     The area within the child that is invalid
   */
  void invalidateChild(@NonNull View child, @Nullable Rect r);

  /**
   * Index of the left position in the location array
   */
  int CHILD_LEFT_INDEX = 0;
  /**
   * Index of the top position in the location array
   */
  int CHILD_TOP_INDEX = 1;

  /**
   * Invalidates the child at the specified {@code location} of this parent and
   * marks the given region {@code r} as invalid and needing to be redrawn.
   * {@code location} should be an array of two {@code int} values which
   * respectively represent the left and top position of the invalid child.
   *
   * <p>If the specified region {@code r} does not require invalidation in the
   * parent, or if the parent does not exist, this method must return {@code
   * null}. When this method returns a non-null value, {@code location} must
   * have been updated with the left and top coordinates of this parent.
   *
   * @param location An array of 2 ints containing the left and top coordinates
   *                 of the child to invalidate
   * @param r        The area within the child that is invalid
   *
   * @return The {@linkplain #getParent parent} of this parent, or {@code null}
   */
  @Nullable
  ViewParent invalidateChildInParent(@NonNull @Size(value = 2) int[] location, @NonNull Rect r);

  /**
   * Finds the nearest view in the specified direction that wants to take focus.
   *
   * @param v         The view that currently has focus
   * @param direction One of {@link FocusDirection#UP}, {@link FocusDirection#DOWN},
   *                  {@link FocusDirection#LEFT}, or {@link FocusDirection#RIGHT}
   *
   * @return The nearest focusable in the specified direction, or {@code null}
   *         if none could be found
   */
  @Nullable
  View focusSearch(@Nullable View v, @FocusDirection.Real int direction);

  /**
   * Notifies this parent that the specified child has become available for
   * focus. This method is intended to be used to handle state transitions in
   * this parent from the case where there are no focusable child views to the
   * case where the first focusable child view becomes available.
   *
   * @param view The view who has become newly focusable
   */
  void focusableViewAvailable(@NonNull View view);

  /**
   * Called when a child of this parent wants focus.
   *
   * @param child   The child of this view parent that wants focus. This view
   *                will contain the focused view. It is not necessarily the
   *                view that actually has focus
   * @param focused The view that is a descendant of {@code child} that actually
   *                has focus
   */
  void requestChildFocus(@NonNull View child, @NonNull View focused);

  /**
   * Called when a child of this parent is giving up focus.
   *
   * @param child The child that is giving up focus
   */
  void clearChildFocus(@NonNull View child);

  /**
   * Indicates if this view parent can resolve the layout direction.
   *
   * @see View#setLayoutDirection(int)
   */
  boolean canResolveLayoutDirection();

  /**
   * Indicates if this view parent's layout direction is resolved.
   *
   * @see View#getLayoutDirection()
   */
  boolean isLayoutDirectionResolved();

  /**
   * Returns the resolved layout direction of this view parent.
   *
   * @return {@link LayoutDirection#RTL} if the layout direction is RTL,
   *         otherwise {@link LayoutDirection#LTR}
   */
  @LayoutDirection.Resolved
  int getLayoutDirection();

  /**
   * This method is called on the parent when a child's drawable state has
   * changed.
   *
   * @param child The child whose drawable state has changed
   */
  void childDrawableStateChanged(@NonNull View child);
}
