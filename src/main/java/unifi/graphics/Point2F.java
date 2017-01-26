package unifi.graphics;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.Json;

public class Point2F {

  private float x;
  private float y;

  public Point2F() {
    this(0, 0);
  }

  public Point2F(float x, float y) {
    _set(x, y);
  }

  public Point2F(@NonNull Point2F src) {
    _set(src); // Validates @NonNull
  }

  public Point2F(@NonNull Point2 src) {
    _set(src); // Validates @NonNull
  }

  protected void onChange() {
  }

  public float getX() {
    return x;
  }

  private final void _setX(float x) {
    this.x = x;
  }

  public void setX(float x) {
    if (getX() != x) {
      _setX(x);
      onChange();
    }
  }

  public float getY() {
    return y;
  }

  private final void _setY(float y) {
    this.y = y;
  }

  public void setY(float y) {
    if (getY() != y) {
      _setY(y);
      onChange();
    }
  }

  private final void _set(float x, float y) {
    _setX(x);
    _setY(y);
  }

  public void set(float x, float y) {
    if (!equals(x, y)) {
      _set(x, y);
      onChange();
    }
  }

  private final void _set(@NonNull Point2F src) {
    Validate.isTrue(src != null, "source Point2F cannot be null");
    _setX(src.getX());
    _setY(src.getY());
  }

  private final void _set(@NonNull Point2 src) {
    Validate.isTrue(src != null, "source Point2 cannot be null");
    _setX(src.getX());
    _setY(src.getY());
  }

  public void set(@NonNull Point2F src) {
    if (!equals(src)) { // Validates @NonNull
      _set(src);
      onChange();
    }
  }

  public void set(@NonNull Point2 src) {
    if (!equals(src)) { // Validates @NonNull
      _set(src);
      onChange();
    }
  }

  public final boolean equals(float x, float y) {
    return getX() == x && getY() == y;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (obj instanceof Point2) {
      Point2 other = (Point2) obj;
      return equals(other.getX(), other.getY());
    } else if (!(obj instanceof Point2F)) {
      return false;
    }

    Point2F other = (Point2F) obj;
    return equals(other.getX(), other.getY());
  }

  @Override
  public int hashCode() {
    final float x = getX();
    final float y = getY();
    if (x % 1.0f == 0.0f
      && y % 1.0f == 0.0f) {
      return Point2.hashCode((int) x, (int) y);
    }

    int result = 17;
    result = 31 * result + Float.floatToIntBits(x);
    result = 31 * result + Float.floatToIntBits(y);
    return result;
  }

  @Override
  public String toString() {
    return Json.createObjectBuilder()
        .add("x", getX())
        .add("y", getY())
        .build().toString();
  }
}
