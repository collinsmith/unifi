package unifi.graphics;

import android.support.annotation.NonNull;

/**
 * Point holds two integer coordinates {@link #x} and {@link #y}
 */
public class Point {
  /**
   * X-coordinate of this point.
   */
  public int x;

  /**
   * Y-coordinate of this point.
   */
  public int y;

  public Point() {}

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point(@NonNull Point src) {
    this.x = src.x;
    this.y = src.y;
  }

  public void set(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public final void negate() {
    x = -x;
    y = -y;
  }

  public final void offset(int dx, int dy) {
    x += dx;
    y += dy;
  }

  public final boolean equals(int x, int y) {
    return this.x == x && this.y == y;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (getClass() != obj.getClass()) {
      return false;
    }

    Point other = (Point) obj;
    return x == other.x && y == other.y;
  }

  @Override
  public int hashCode() {
    int result = x;
    result = 31 * result + y;
    return result;
  }

  @Override
  public String toString() {
    return "Point(" + x + ", " + y + ")";
  }
}
