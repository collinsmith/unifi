package unifi.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Defines the responsibilities for a class that will be a manager of
 * {@link View} instances.
 */
public interface ViewManager {
  /**
   * Assigns the passed {@code params} to the specified {@code view} and adds
   * it to this view manager.
   *
   * @param view   The view to add to this view manager
   * @param params The layout params used to layout {@code view}, or
   *               {@code null} if no layout params can be provided
   */
  void addView(@NonNull View view, @Nullable ViewGroup.LayoutParams params);

  /**
   * Removes the specified {@code view} from this view manager.
   *
   * @param view The view to remove from this view manager
   *
   * @return {@code true} if the children of this view manager changed as a
   *         result of this operation (i.e., if the view was being managed by
   *         this view manager and is no longer being managed by it),
   *         {@code false} otherwise
   */
  boolean removeView(@Nullable View view);

  /**
   * Assigns the passed {@code params} to the specified {@code view} and
   * updates the layout hierarchy of this view manager.
   *
   * <p>Precondition: {@code view} should <em>already</em> be managed by this
   * view manager.
   *
   * @param view   The view whose layout to update
   * @param params The layout params used to layout {@code view}, or
   *               {@code null} if no layout params can be provided
   *
   * @throws IllegalArgumentException if {@code view} is not managed by this
   *     view manager or the supplied {@code params} are invalid.
   */
  void updateViewLayout(@NonNull View view, @NonNull ViewGroup.LayoutParams params);
}
