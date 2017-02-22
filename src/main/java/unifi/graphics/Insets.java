package unifi.graphics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Insets holds four integer offsets which describe changes to the four edges of a rectangle. By
 * convention, positive values move edges towards the center of the rectangle.
 * <p>
 * Note: <strong>Insets are immutable so may be treated as values.</strong>
 */
public class Insets {

  /**
   * Represents an insets with all values equal to {@code 0}.
   */
  public static final Insets NONE = new Insets(0, 0, 0, 0);

  /**
   * Return an insets instance with the appropriate values.
   *
   * @param left   The left inset, in pixels
   * @param top    The top inset, in pixels
   * @param right  The right inset, in pixels
   * @param bottom The bottom inset, in pixels
   *
   * @return An insets instance with the appropriate values
   */
  @NonNull
  public static Insets of(int left, int top, int right, int bottom) {
    if (left == 0 && top == 0 && right == 0 && bottom == 0) {
      return NONE;
    }

    return new Insets(left, top, right, bottom);
  }

  /**
   * Return an insets instance with the appropriate values, taken as the sides of the specified
   * {@code rect}.
   *
   * @param rect The rectangle from which to take the values
   *
   * @return An insets instance with the appropriate values
   */
  public static Insets of(@Nullable Rect rect) {
    return (rect == null) ? NONE : Insets.of(rect.left, rect.top, rect.right, rect.bottom);
  }

  /**
   * The left offset, in pixels, of the insets, positive values indicate towards the center,
   * negative indicate outwards.
   */
  public final int left;

  /**
   * The top offset, in pixels, of the insets, positive values indicate towards the center,
   * negative indicate outwards.
   */
  public final int top;

  /**
   * The right offset, in pixels, of the insets, positive values indicate towards the center,
   * negative indicate outwards.
   */
  public final int right;

  /**
   * The bottom offset, in pixels, of the insets, positive values indicate towards the center,
   * negative indicate outwards.
   */
  public final int bottom;

  /**
   * Constructs a new Insets instance with the specified values.
   *
   * @param left   The left inset, in pixels
   * @param top    The top inset, in pixels
   * @param right  The right inset, in pixels
   * @param bottom The bottom inset, in pixels
   */
  private Insets(int left, int top, int right, int bottom) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  /**
   * Two Insets instances are equal iff they belong to the same class and their fields are pairwise
   * equal.
   *
   * @param obj The object to compare this instance with
   *
   * @return {@code true} iff this object is equal {@code obj}
   */
  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (getClass() != obj.getClass()) {
      return false;
    }

    Insets insets = (Insets) obj;
    return bottom == insets.bottom
        && left == insets.left
        && right == insets.right
        && top == insets.top;
  }

  @Override
  public int hashCode() {
    int result = left;
    result = 31 * result + top;
    result = 31 * result + right;
    result = 31 * result + bottom;
    return result;
  }

  @NonNull
  @Override
  public String toString() {
    return "Insets{" +
        "left=" + left +
        ", top=" + top +
        ", right=" + right +
        ", bottom=" + bottom +
        '}';
  }
}
