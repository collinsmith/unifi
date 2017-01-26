package unifi.graphics;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.Json;

public class RectF {

  public static final RectF ZERO = ImmutableRectF.newImmutableRectF();

  private float left;
  private float top;
  private float right;
  private float bottom;

  public RectF() {
    this(0, 0, 0, 0);
  }

  public RectF(float left, float top, float right, float bottom) {
    _set(left, top, right, bottom);
  }

  public RectF(@NonNull RectF src) {
    Validate.isTrue(src != null, "source RectF cannot be null");
    _set(src);
  }

  public RectF(@NonNull Rect src) {
    Validate.isTrue(src != null, "source Rect cannot be null");
    _set(src);
  }

  protected void onChange() {}

  public float getLeft() {
    return left;
  }

  private void _setLeft(float left) {
    this.left = left;
  }

  public void setLeft(float left) {
    if (getLeft() != left) {
      _setLeft(left);
      if (getRight() < left) {
        _setRight(left);
      }

      onChange();
    }
  }

  public float getTop() {
    return top;
  }

  private void _setTop(float top) {
    this.top = top;
  }

  public void setTop(float top) {
    if (getTop() != top) {
      _setTop(top);
      if (getBottom() < top) {
        _setBottom(top);
      }

      onChange();
    }
  }

  public float getRight() {
    return right;
  }

  private void _setRight(float right) {
    this.right = right;
  }

  public void setRight(float right) {
    if (getRight() != right) {
      _setRight(right);
      if (right < getLeft()) {
        _setLeft(right);
      }

      onChange();
    }
  }

  public float getBottom() {
    return bottom;
  }

  private void _setBottom(float bottom) {
    this.bottom = bottom;
  }

  public void setBottom(float bottom) {
    if (getBottom() != bottom) {
      _setBottom(bottom);
      if (bottom < getTop()) {
        _setTop(bottom);
      }

      onChange();
    }
  }

  private void _set(float left, float top, float right, float bottom) {
    Validate.isTrue(left <= right, "left (%d) must be <= right (%d)", left, right);
    Validate.isTrue(top <= bottom, "top (%d) must be <= bottom (%d)", top, bottom);
    _setLeft(left);
    _setTop(top);
    _setRight(right);
    _setBottom(bottom);
  }

  public void set(float left, float top, float right, float bottom) {
    if (!equals(left, top, right, bottom)) {
      _set(left, top, right, bottom);
      onChange();
    }
  }

  private void _set(@NonNull RectF src) {
    assert src != null : "source RectF cannot be null";
    _setLeft(src.getLeft());
    _setTop(src.getTop());
    _setRight(src.getRight());
    _setBottom(src.getBottom());
  }

  private void _set(@NonNull Rect src) {
    assert src != null : "source Rect cannot be null";
    _setLeft(src.getLeft());
    _setTop(src.getTop());
    _setRight(src.getRight());
    _setBottom(src.getBottom());
  }

  public void set(@NonNull RectF src) {
    Validate.isTrue(src != null, "source RectF cannot be null");
    if (!equals(src)) {
      _set(src);
      onChange();
    }
  }

  public void set(@NonNull Rect src) {
    Validate.isTrue(src != null, "source Rect cannot be null");
    if (!equals(src)) {
      _set(src);
      onChange();
    }
  }

  public final void setEmpty(float length) {
    set(length, length, length, length);
  }

  public final void setEmpty() {
    setEmpty(0);
  }

  public boolean isEmpty() {
    return getLeft() >= getRight() && getTop() >= getBottom();
  }

  public float getWidth() {
    return getRight() - getLeft();
  }

  public float getHeight() {
    return getBottom() - getTop();
  }

  public final boolean equals(float left, float top, float right, float bottom) {
    return getLeft() == left
        && getTop() == top
        && getRight() == right
        && getBottom() == bottom;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (obj instanceof Rect) {
      Rect other = (Rect) obj;
      return equals(other.getLeft(), other.getTop(), other.getRight(), other.getBottom());
    } else if (!(obj instanceof RectF)) {
      return false;
    }

    RectF other = (RectF) obj;
    return equals(other.getLeft(), other.getTop(), other.getRight(), other.getBottom());
  }

  @Override
  public int hashCode() {
    final float left = getLeft();
    final float top = getTop();
    final float right = getRight();
    final float bottom = getBottom();
    if (left % 1.0f == 0.0f
        && top % 1.0f == 0.0f
        && right % 1.0f == 0.0f
        && bottom % 1.0f == 0.0f) {
      return Rect.hashCode((int) left, (int) top, (int) right, (int) bottom);
    }

    int result = 17;
    result = 31 * result + Float.floatToIntBits(left);
    result = 31 * result + Float.floatToIntBits(top);
    result = 31 * result + Float.floatToIntBits(right);
    result = 31 * result + Float.floatToIntBits(bottom);
    return result;
  }

  @Override
  @NonNull
  public String toString() {
    return Json.createObjectBuilder()
        .add("left", getLeft())
        .add("top", getTop())
        .add("right", getRight())
        .add("bottom", getBottom())
        .build().toString();
  }

}
