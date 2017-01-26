package unifi.graphics;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.Json;

public class Point2 {

  public static final Point2 ZERO = ImmutablePoint2.newImmutablePoint2();

  private int x;
  private int y;

  public Point2() {
    this(0, 0);
  }

  public Point2(int x, int y) {
    _set(x, y);
  }

  public Point2(@NonNull Point2 src) {
    _set(src); // Validates @NonNull
  }

  protected void onChange() {
  }

  public int getX() {
    return x;
  }

  private final void _setX(int x) {
    this.x = x;
  }

  public void setX(int x) {
    if (getX() != x) {
      _setX(x);
      onChange();
    }
  }

  public int getY() {
    return y;
  }

  private final void _setY(int y) {
    this.y = y;
  }

  public void setY(int y) {
    if (getY() != y) {
      _setY(y);
      onChange();
    }
  }

  private final void _set(int x, int y) {
    _setX(x);
    _setY(y);
  }

  public void set(int x, int y) {
    if (!equals(x, y)) {
      _set(x, y);
      onChange();
    }
  }

  private final void _set(@NonNull Point2 src) {
    Validate.isTrue(src != null, "source Point2 cannot be null");
    _setX(src.getX());
    _setY(src.getY());
  }

  public void set(@NonNull Point2 src) {
    if (!equals(src)) { // Validates @NonNull
      _set(src);
      onChange();
    }
  }

  @NonNull
  public final Point2 add(@NonNull Point2 src) {
    return add(src, this); // Validates @NonNull
  }

  @NonNull
  public Point2 add(@NonNull Point2 src, @NonNull Point2 dst) {
    Validate.isTrue(src != null, "source Point2 cannot be null");
    Validate.isTrue(dst != null, "destination Point2 cannot be null");
    dst.set(getX() + src.getX(), getY() + src.getY());
    return dst;
  }

  @NonNull
  public final Point2 subtract(@NonNull Point2 src) {
    return subtract(src, this);
  }

  @NonNull
  public Point2 subtract(@NonNull Point2 src, @NonNull Point2 dst) {
    Validate.isTrue(src != null, "source Point2 cannot be null");
    Validate.isTrue(dst != null, "destination Point2 cannot be null");
    dst.set(getX() - src.getX(), getY() - src.getY());
    return dst;
  }

  @NonNull
  public final Point2 scale(double scalar) {
    return scale(scalar, this);
  }

  @NonNull
  public Point2 scale(double scalar, @NonNull Point2 dst) {
    Validate.isTrue(dst != null, "destination Point2 cannot be null");
    if (scalar != 1.0 || dst != this) {
      dst.set((int) (getX() * scalar), (int) (getY() * scalar));
    }

    return dst;
  }

  @NonNull
  public final Point2F toPoint2F() {
    return ImmutablePoint2F.copyOf(this);
  }

  @NonNull
  public final Point2F toPoint2F(@NonNull Point2F dst) {
    Validate.isTrue(dst != null, "destination Point2F cannot be null");
    dst.set(this);
    return dst;
  }

  public final boolean equals(int x, int y) {
    return getX() == x && getY() == y;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (obj instanceof Point2F) {
      Point2F other = (Point2F) obj;
      return other.equals(this);
    } else if (!(obj instanceof Point2)) {
      return false;
    }

    Point2 other = (Point2) obj;
    return equals(other.getX(), other.getY());
  }

  static int hashCode(int x, int y) {
    int result = 17;
    result = 31 * result + x;
    result = 31 * result + y;
    return result;
  }

  @Override
  public int hashCode() {
    return hashCode(getX(), getY());
  }

  @Override
  public String toString() {
    return Json.createObjectBuilder()
        .add("x", getX())
        .add("y", getY())
        .build().toString();
  }

}
