package deprecated.unifi2.graphics;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Rect holds four integer coordinates for a rectangle. The rectangle is represented by the
 * coordinates of its 4 edges ({@link #left}, {@link #top}, {@link #right} and {@link #bottom}).
 * These fields can be accessed directly. Use {@link #width} and {@link #height} to retrieve
 * the rectangle's width and height. Note: most methods do not check to see that the coordinates
 * are sorted correctly (i.e. {@code left <= right} and {@code top <= bottom}).
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
   * Checks whether or not this rectangle is empty, i.e.,
   * {@link #left}<code> >= </code>{@link #right} or {@link #top}<code> >= </code>{@link #bottom}.
   *
   * @return {@code true} if this rectangle is empty, otherwise {@code false}
   */
  public final boolean isEmpty() {
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
   * Note: This method does not validate the invariants for the sides of the rectangle,
   *       i.e., {@link #left}<code> <= </code>{@link #right}, so the result may be negative.
   *
   * @return The width of this rectangle
   */
  public final int width() {
    return right - left;
  }

  /**
   * Returns the height of this rectangle.
   * <p>
   * Note: This method does not validate the invariants for the sides of the rectangle,
   *       i.e., {@link #top}<code> <= </code>{@link #bottom}, so the result may be negative.
   *
   * @return The height of this rectangle
   */
  public final int height() {
    return bottom - top;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == this) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (obj.getClass() != Rect.class) {
      return false;
    }

    Rect other = (Rect) obj;
    return left == other.left
        && top == other.top
        && right == other.right
        && bottom == other.bottom;
  }

  @Override
  public int hashCode() {
    int result = left;
    result = 31 * result + top;
    result = 31 * result + right;
    result = 31 * result + bottom;
    return result;
  }

  @Override
  @NonNull
  public String toString() {
    return "Rect(" + left + ", " + top + ", " + right + ", " + bottom + ")";
  }

  @NonNull
  public JsonObject toJsonObject() {
    return Json.createObjectBuilder()
        .add("left", left)
        .add("top", top)
        .add("right", right)
        .add("bottom", bottom)
        .build();
  }

}
