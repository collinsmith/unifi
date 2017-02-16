package deprecated.unifi3.graphics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.PrintWriter;

/**
 * Rect holds four integer coordinates for a rectangle. The rectangle is represented by the
 * coordinates of its 4 edges ({@link #left}, {@link #top}, {@link #right} and {@link #bottom}).
 * These fields can be accessed directly. Use {@link #width} and {@link #height} to retrieve the
 * rectangle's width and height. Note: most methods do not check to see that the coordinates are
 * sorted correctly (i.e. {@code left <= right} and {@code top <= bottom}).
 */
public class Rect {
  /**
   * X-coordinate of the left side of this rectangle.
   * <p>
   * Invariant: {@code left <= }{@link #right}
   */
  public int left;

  /**
   * Y-coordinate of the top of this rectangle
   * <p>
   * Invariant: {@code top <= }{@link #bottom}
   */
  public int top;

  /**
   * X-coordinate of the right side of this rectangle.
   * <p>
   * Invariant: {@link #left}<code> <= right</code>
   */
  public int right;

  /**
   * Y-coordinate of the bottom of this rectangle
   * <p>
   * Invariant: {@link #top}<code> <= bottom</code>
   */
  public int bottom;

  /**
   * Constructs an empty rectangle. All coordinates are initialized to {@code 0}.
   */
  public Rect() {}

  /**
   * Constructs a rectangle with the specified coordinates.
   * <p>
   * Note: No side invariant validation is performed, so the caller must ensure that
   *       {@code left <= right} and {@code top <= bottom}.
   *
   * @param left   The x-coordinate of the {@link #left} side of the rectangle
   * @param top    The y-coordinate of the {@link #top} of the rectangle
   * @param right  The x-coordinate of the {@link #right} side of the rectangle
   * @param bottom The y-coordinate of the {@link #bottom} of the rectangle
   */
  public Rect(int left, int top, int right, int bottom) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  /**
   * Constructs a rectangle initialized with the values in the specified source rectangle {@code r}
   * (which is left unmodified). If the source rectangle is {@code null}, then all coordinates are
   * initialized to {@code 0}.
   *
   * @param r The rectangle whose coordinates are copied into the new rectangle
   */
  public Rect(@Nullable Rect r) {
    if (r == null) {
      left = top = right = bottom = 0;
    } else {
      left = r.left;
      top = r.top;
      right = r.right;
      bottom = r.bottom;
    }
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == this) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (getClass() != obj.getClass()) {
      return false;
    }

    Rect other = (Rect) obj;
    return left == other.left && top == other.top && right == other.right && bottom == other.bottom;
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
    StringBuilder sb = new StringBuilder(32);
    sb.append("Rect(");
    sb.append(left);
    sb.append(", ");
    sb.append(top);
    sb.append(" - ");
    sb.append(right);
    sb.append(", ");
    sb.append(bottom);
    sb.append(")");
    return sb.toString();
  }

  /**
   * Return a string representation of the rectangle in a compact form.
   */
  @NonNull
  public String toShortString() {
    return toShortString(new StringBuilder(32));
  }

  /**
   * Return a string representation of the rectangle in a compact form.
   *
   * @param sb String builder to write to
   *
   * @throws NullPointerException if the builder {@code sb} is {@code null}
   */
  @NonNull
  public String toShortString(@NonNull StringBuilder sb) {
    sb.setLength(0);
    sb.append('[');
    sb.append(left);
    sb.append(',');
    sb.append(top);
    sb.append("][");
    sb.append(right);
    sb.append(',');
    sb.append(bottom);
    sb.append(']');
    return sb.toString();
  }

  /**
   * Print short representation to given writer.
   *
   * @param pw Print writer to print to
   *
   * @throws NullPointerException if the writer {@code pw} is {@code null}
   */
  public void printShortString(@NonNull PrintWriter pw) {
    pw.print('[');
    pw.print(left);
    pw.print(',');
    pw.print(top);
    pw.print("][");
    pw.print(right);
    pw.print(',');
    pw.print(bottom);
    pw.print(']');
  }

  /**
   * Checks whether or not this rectangle is empty, i.e., {@link #left}<code> >= </code>{@link
   * #right} or {@link #top}<code> >= </code>{@link #bottom}.
   *
   * @return {@code true} if this rectangle is empty, otherwise {@code false}
   */
  public boolean isEmpty() {
    return left >= right || top >= bottom;
  }

  /**
   * Sets all coordinates of this rectangle to {@code 0}.
   */
  public void setEmpty() {
    left = right = top = bottom = 0;
  }

  /**
   * Returns the width of this rectangle.
   * <p>
   * Note: This method does not validate the invariants for the sides of the rectangle, i.e.,
   *       {@link #left}<code> <= </code>{@link #right}, so the result may be negative.
   *
   * @return The width of this rectangle
   */
  public final int width() {
    return right - left;
  }

  /**
   * Returns the height of this rectangle.
   * <p>
   * Note: This method does not validate the invariants for the sides of the rectangle, i.e.,
   *       {@link #top}<code> <= </code>{@link #bottom}, so the result may be negative.
   *
   * @return The height of this rectangle
   */
  public final int height() {
    return bottom - top;
  }

  /**
   * Returns the horizontal center of the rectangle. If the computed value is fractional, this
   * method returns the largest integer that is less than the computed value.
   *
   * @return The horizontal center of the rectangle
   */
  public final int centerX() {
    return (left + right) >> 1;
  }

  /**
   * The vertical center of the rectangle. If the computed value is fractional, this method
   * returns the largest integer that is less than the computed value.
   *
   * @return The vertical center of the rectangle
   */
  public final int centerY() {
    return (top + bottom) >> 1;
  }

  /**
   * The exact horizontal center of the rectangle as a float.
   *
   * @return The horizontal center of the rectangle
   */
  public final float exactCenterX() {
    return (left + right) * 0.5f;
  }

  /**
   * The exact vertical center of the rectangle as a float.
   *
   * @return The vertical center of the rectangle
   */
  public final float exactCenterY() {
    return (top + bottom) * 0.5f;
  }

  /**
   * Sets this rectangle's coordinates to the specified values.
   * <p>
   * Note: No side invariant validation is performed, so the caller must ensure that
   *       {@code left <= right} and {@code top <= bottom}.
   *
   * @param left   The x-coordinate of the {@link #left} side of the rectangle
   * @param top    The y-coordinate of the {@link #top} of the rectangle
   * @param right  The x-coordinate of the {@link #right} side of the rectangle
   * @param bottom The y-coordinate of the {@link #bottom} of the rectangle
   */
  public void set(int left, int top, int right, int bottom) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  /**
   * Sets this rectangle's coordinates to those of the specified {@code src} rectangle.
   *
   * @param src The rectangle whose coordinates are copied
   *
   * @throws NullPointerException if the {@code src} rectangle is {@code null}
   */
  public void set(@NonNull Rect src) {
    this.left = src.left;
    this.top = src.top;
    this.right = src.right;
    this.bottom = src.bottom;
  }

  /**
   * Offsets this rectangle by adding {@code dx} to its {@link #left} and {@link #right}
   * coordinates, and {@code dy} to its {@link #top} and {@link #bottom} coordinates.
   *
   * @param dx The amount to add to the rectangle's {@code left} and {@code right} coordinates
   * @param dy The amount to add to the rectangle's {@code top} and {@code bottom} coordinates
   */
  public void offset(int dx, int dy) {
    left += dx;
    top += dy;
    right += dx;
    bottom += dy;
  }

  /**
   * Offset this rectangle to a specific {@code (left, top)} coordinate, keeping its {@link #width}
   * and {@link #height} the same.
   *
   * @param left The new {@link #left} coordinate for this rectangle
   * @param top  The new {@link #top} coordinate for this rectangle
   */
  public void offsetTo(int left, int top) {
    right += left - this.left;
    bottom += top - this.top;
    this.left = left;
    this.top = top;
  }

  /**
   * Insets this rectangle by {@code (dx, dy)}. If {@code dx} is positive, then the sides are moved
   * inwards (i.e., making the rectangle narrower), otherwise the sides are moved outwards (i.e.,
   * making the rectangle wider). The same holds true for {@code dy} and the {@link #height}
   * coordinates.
   *
   * @param dx The amount to add (or subtract) from this rectangle's {@link #width}
   * @param dy The amount to add (or subtract) from this rectangle's {@link #height}
   */
  public void inset(int dx, int dy) {
    left += dx;
    top += dy;
    right -= dx;
    bottom -= dy;
  }

  /**
   * Checks whether or not the specified {@code (x, y)} coordinates lie within this rectangle's
   * bounds.
   * <p>
   * Note: Coordinates on the {@link #left} and {@link #top} sides are considered to be inside,
   *       while coordinates on the {@link #right} and {@link #bottom} sides are not. This means
   *       that contains is represented invariably as {@code left <= x < right and
   *       top <= y < bottom}.
   * <p>
   * Note: An empty rectangle never contains any point.
   *
   * @param x The x-coordinate of the point being tested for containment
   * @param y The y-coordinate of the point being tested for containment
   *
   * @return {@code true} if and only if {@code (x, y)} are contained within the rectangle, where
   *         containment means {@code left <= x < right and top <= y < bottom}
   */
  public boolean contains(int x, int y) {
    // check for empty first
    return left < right && top < bottom
        // now check for containment
        && x >= left && x < right
        && y >= top && y < bottom;
  }

  /**
   * Checks whether or not the specified bounds of a rectangle are inside or equal to this
   * rectangle's bounds. I.e., if this rectangle is a superset of the specified rectangle.
   * <p>
   * Note: An empty rectangle never contains another rectangle.
   *
   * @param left   The left side of the rectangle being tested for containment
   * @param top    The top of the rectangle being tested for containment
   * @param right  The right side of the rectangle being tested for containment
   * @param bottom The bottom of the rectangle being tested for containment
   *
   * @return {@code true} if the bounds of a rectangle are inside or equal to this rectangle's
   *         bounds, otherwise {@code false}
   */
  public boolean contains(int left, int top, int right, int bottom) {
    // check for empty first
    return this.left < this.right && this.top < this.bottom
        // now check for containment
        && this.left <= left && this.top <= top
        && this.right >= right && this.bottom >= bottom;
  }

  /**
   * Checks whether or not the bounds of a specified rectangle {@code r} are inside or equal to this
   * rectangle's bounds. I.e., if this rectangle is a superset of the specified rectangle.
   * <p>
   * Note: An empty rectangle never contains another rectangle.
   *
   * @param r The rectangle being tested for containment
   *
   * @return {@code true} if the bounds of rectangle {@code r} are inside or equal to this
   *         rectangle's bounds, otherwise {@code false}
   *
   * @throws NullPointerException if {@code r} is {@code null}
   */
  public boolean contains(@NonNull Rect r) {
    // check for empty first
    return this.left < this.right && this.top < this.bottom
        // now check for containment
        && left <= r.left && top <= r.top
        && right >= r.right && bottom >= r.bottom;
  }

  /**
   * Checks whether or not the rectangle specified by {@code (left, top, right, bottom)} intersects
   * this rectangle. If it does, then this rectangle is set to that intersection, otherwise this
   * rectangle is left unmodified.
   * <p>
   * Note: No check is performed to test if either rectangle is {@linkplain #isEmpty() empty}.
   * <p>
   * Note: To only test for intersection, use {@link #intersects(Rect, Rect)}.
   *
   * @param left   The left side of the rectangle being intersected with this rectangle
   * @param top    The top of the rectangle being intersected with this rectangle
   * @param right  The right side of the rectangle being intersected with this rectangle
   * @param bottom The bottom of the rectangle being intersected with this rectangle
   *
   * @return {@code true} if the specified bounds intersect with the bounds of this rectangle (in
   *         which case this rectangle is then set to that intersection), otherwise {@code false}
   *         and this rectangle is left unmodified
   *
   * @see #intersects(Rect, Rect)
   */
  public boolean intersect(int left, int top, int right, int bottom) {
    if (this.left < right && left < this.right && this.top < bottom && top < this.bottom) {
      if (this.left < left) this.left = left;
      if (this.top < top) this.top = top;
      if (this.right > right) this.right = right;
      if (this.bottom > bottom) this.bottom = bottom;
      return true;
    }

    return false;
  }

  /**
   * Checks whether or not the specified rectangle {code r} intersects with this rectangle. If it
   * does, then this rectangle is set to that intersection, otherwise this rectangle is left
   * unmodified.
   * <p>
   * Note: No check is performed to test if either rectangle is {@linkplain #isEmpty() empty}.
   * <p>
   * Note: To only test for intersection, use {@link #intersects(Rect, Rect)}.
   *
   * @param r The rectangle being intersected with this rectangle
   *
   * @return {@code true} if the bounds of the specified rectangle {@code r} intersect with the
   *         bounds of this rectangle (in which case this rectangle is then set to that
   *         intersection), otherwise {@code false} and this rectangle is left unmodified
   *
   * @throws NullPointerException if {@code r} is {@code null}
   *
   * @see #intersects(Rect, Rect)
   */
  public boolean intersect(@NonNull Rect r) {
    return intersect(r.left, r.top, r.right, r.bottom);
  }

  /**
   * Checks whether or not rectangles {@code a} and {@code b} intersect. If they do, then this
   * rectangle is set to the bounds of that intersection, otherwise this rectangle is left
   * unmodified.
   * <p>
   * Note: No check is performed to test if either rectangle is {@linkplain #isEmpty() empty}.
   * <p>
   * Note: To only test for intersection, use {@link #intersects(Rect, Rect)}.
   *
   * @param a The rectangle being intersected with rectangle {@code b}
   * @param b The rectangle being intersected with rectangle {@code a}
   *
   * @return {@code true} if the bounds of the specified rectangle {@code a} intersect with the
   *         bounds of the specified rectangle {@code b} (in which case this rectangle is then set
   *         to that intersection), otherwise {@code false} and this rectangle is left unmodified
   *
   * @throws NullPointerException if either {@code a} or {@code b} is {@code null}
   *
   * @see #intersects(Rect, Rect)
   */
  public boolean setIntersect(Rect a, Rect b) {
    if (a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom) {
      left = Math.max(a.left, b.left);
      top = Math.max(a.top, b.top);
      right = Math.min(a.right, b.right);
      bottom = Math.min(a.bottom, b.bottom);
      return true;
    }
    return false;
  }

  /**
   * Checks whether or not the specified bounds of a rectangle intersect with the bounds of this
   * rectangle. In either case, this rectangle is left unmodified.
   * <p>
   * Note: No check is performed to test if either rectangle is {@linkplain #isEmpty() empty}.
   * <p>
   * Note: To retrieve the bounds of this intersection, use {@link #intersect(int, int, int, int)},
   *       {@link #intersect(Rect)}, or {@link #setIntersect(Rect, Rect)}.
   *
   * @param left   The left side of the rectangle being tested for intersection
   * @param top    The top of the rectangle being tested for intersection
   * @param right  The right side of the rectangle being tested for intersection
   * @param bottom The bottom of the rectangle being tested for intersection
   *
   * @return {@code true} if the specified bounds of a rectangle intersect with this rectangle,
   *         otherwise {@code false}
   *
   * @see #intersect(int, int, int, int)
   * @see #intersect(Rect)
   * @see #setIntersect(Rect, Rect)
   */
  public boolean intersects(int left, int top, int right, int bottom) {
    return this.left < right && left < this.right && this.top < bottom && top < this.bottom;
  }

  /**
   * Checks whether or not the specified rectangles {@code a} and {@code b} intersect. In either
   * case, both rectangles are left unmodified.
   * <p>
   * Note: To retrieve the bounds of this intersection, use {@link #intersect(int, int, int, int)},
   *       {@link #intersect(Rect)}, or {@link #setIntersect(Rect, Rect)}.
   *
   * @param a The first rectangle being tested for intersection
   * @param b The second rectangle being tested for intersection
   *
   * @return {@code true} if the bounds of the specified rectangles {@code a} and {@code b}
   *         intersect with this rectangle, otherwise {@code false}
   *
   * @throws NullPointerException if either {@code a} or {@code b} is {@code null}
   *
   * @see #intersect(int, int, int, int)
   * @see #intersect(Rect)
   * @see #setIntersect(Rect, Rect)
   */
  public static boolean intersects(@NonNull Rect a, @NonNull Rect b) {
    return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
  }

  /**
   * Updates this rectangle's bounds to enclose itself and those of a specified rectangle. <p> Note:
   * If the specified bounds are {@linkplain #isEmpty() empty}, then this rectangle is left
   * unmodified.
   * <p>
   * Note: If this rectangle is {@linkplain #isEmpty() empty}, then it is set to the bounds of
   *       the specified rectangle.
   *
   * @param left   The left edge being combined with this rectangle
   * @param top    The top edge being combined with this rectangle
   * @param right  The right edge being combined with this rectangle
   * @param bottom The bottom edge being combined with this rectangle
   *
   * @see #union(Rect)
   */
  public void union(int left, int top, int right, int bottom) {
    if ((left < right) && (top < bottom)) {
      if ((this.left < this.right) && (this.top < this.bottom)) {
        if (this.left > left) this.left = left;
        if (this.top > top) this.top = top;
        if (this.right < right) this.right = right;
        if (this.bottom < bottom) this.bottom = bottom;
      } else {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
      }
    }
  }

  /**
   * Updates this rectangle's bounds to enclose itself and those of the specified rectangle {@code
   * r}.
   * <p>
   * Note: If the specified rectangle is {@linkplain #isEmpty() empty}, then this rectangle
   *       is left unmodified. <p> Note: If this rectangle is {@linkplain #isEmpty() empty}, then
   *       it is set to the bounds of the specified rectangle.
   *
   * @param r The rectangle being combined with this rectangle
   *
   * @throws NullPointerException if {@code r} is {@code null}
   *
   * @see #union(int, int, int, int)
   */
  public void union(@NonNull Rect r) {
    union(r.left, r.top, r.right, r.bottom);
  }

  /**
   * Updates this rectangle's bounds to enclose itself and the specified {@code (x, y)} coordinate.
   * <p>
   * Note: There is no check to test that this rectangle is {@linkplain #isEmpty() non-empty}.
   *
   * @param x The x-coordinate of the point to add to the rectangle
   * @param y The y-coordinate of the point to add to the rectangle
   */
  public void union(int x, int y) {
    if (x < left) {
      left = x;
    } else if (x > right) {
      right = x;
    }

    if (y < top) {
      top = y;
    } else if (y > bottom) {
      bottom = y;
    }
  }

  /**
   * Checks whether or not the invariants for the sides of this rectangle holds (i.e., {@link
   * #left}<code> <= </code>{@link #right} and {@link #top}<code> <= </code>{@link #bottom}), and
   * swaps them (left with right and/or top with bottom) in the case that they do not. This method
   * can be called if the edges are computed separately, and may have crossed over each other. If
   * the edges are already correct, then this rectangle is left unmodified.
   */
  public void sort() {
    if (left > right) {
      int temp = left;
      left = right;
      right = temp;
    }
    if (top > bottom) {
      int temp = top;
      top = bottom;
      bottom = temp;
    }
  }

  /**
   * Scales this rectangle by the specified factor.
   */
  public void scale(float scale) {
    if (scale != 1.0f) {
      left = (int) (left * scale + 0.5f);
      top = (int) (top * scale + 0.5f);
      right = (int) (right * scale + 0.5f);
      bottom = (int) (bottom * scale + 0.5f);
    }
  }

  /**
   * Scales this rectangle by the specified factor, rounding values towards the inside.
   */
  public void scaleRoundIn(float scale) {
    if (scale != 1.0f) {
      left = (int) Math.ceil(left * scale);
      top = (int) Math.ceil(top * scale);
      right = (int) Math.floor(right * scale);
      bottom = (int) Math.floor(bottom * scale);
    }
  }

}
