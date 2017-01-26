package unifi.graphics;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.Json;

public class Rect {

  public static final Rect ZERO = ImmutableRect.newImmutableRect();

  private int left;
  private int top;
  private int right;
  private int bottom;

  public Rect() {
    this(0, 0, 0, 0);
  }

  public Rect(int left, int top, int right, int bottom) {
    Validate.isTrue(left <= right, "left (%d) must be <= right (%d)", left, right);
    Validate.isTrue(top <= bottom, "top (%d) must be <= bottom (%d)", top, bottom);
    _set(left, top, right, bottom);
  }

  public Rect(@NonNull Rect src) {
    Validate.isTrue(src != null, "source Rect cannot be null");
    _set(src);
  }

  protected void onChange() {}

  public int getLeft() {
    return left;
  }

  private void _setLeft(int left) {
    this.left = left;
  }

  public void setLeft(int left) {
    if (getLeft() != left) {
      _setLeft(left);
      if (getRight() < left) {
        _setRight(left);
      }

      onChange();
    }
  }

  public int getTop() {
    return top;
  }

  private void _setTop(int top) {
    this.top = top;
  }

  public void setTop(int top) {
    if (getTop() != top) {
      _setTop(top);
      if (getBottom() < top) {
        _setBottom(top);
      }

      onChange();
    }
  }

  public int getRight() {
    return right;
  }

  private void _setRight(int right) {
    this.right = right;
  }

  public void setRight(int right) {
    if (getRight() != right) {
      _setRight(right);
      if (right < getLeft()) {
        _setLeft(right);
      }

      onChange();
    }
  }

  public int getBottom() {
    return bottom;
  }

  private void _setBottom(int bottom) {
    this.bottom = bottom;
  }

  public void setBottom(int bottom) {
    if (getBottom() != bottom) {
      _setBottom(bottom);
      if (bottom < getTop()) {
        _setTop(bottom);
      }

      onChange();
    }
  }

  private void _set(int left, int top, int right, int bottom) {
    assert left <= right : "left must be <= right";
    assert top <= bottom : "top must be <= bottom";
    _setLeft(left);
    _setTop(top);
    _setRight(right);
    _setBottom(bottom);
  }

  public void set(int left, int top, int right, int bottom) {
    Validate.isTrue(left <= right, "left (%d) must be <= right (%d)", left, right);
    Validate.isTrue(top <= bottom, "top (%d) must be <= bottom (%d)", top, bottom);
    if (!equals(left, top, right, bottom)) {
      _set(left, top, right, bottom);
      onChange();
    }
  }

  private void _set(@NonNull Rect src) {
    assert src != null : "source Rect cannot be null";
    _setLeft(src.getLeft());
    _setTop(src.getTop());
    _setRight(src.getRight());
    _setBottom(src.getBottom());
  }

  public void set(@NonNull Rect src) {
    Validate.isTrue(src != null, "source Rect cannot be null");
    if (!equals(src)) {
      _set(src);
      onChange();
    }
  }

  public final void setEmpty(int length) {
    set(length, length, length, length);
  }

  public final void setEmpty() {
    setEmpty(0);
  }

  public boolean isEmpty() {
    return getLeft() >= getRight() && getTop() >= getBottom();
  }

  public int getWidth() {
    return getRight() - getLeft();
  }

  public int getHeight() {
    return getBottom() - getTop();
  }

  public final boolean equals(int left, int top, int right, int bottom) {
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
    } else if (obj instanceof RectF) {
      RectF other = (RectF) obj;
      return other.equals(this);
    } else if (!(obj instanceof Rect)) {
      return false;
    }

    Rect other = (Rect) obj;
    return equals(other.getLeft(), other.getTop(), other.getRight(), other.getBottom());
  }

  static int hashCode(int left, int top, int right, int bottom) {
    int result = 17;
    result = 31 * result + left;
    result = 31 * result + top;
    result = 31 * result + right;
    result = 31 * result + bottom;
    return result;
  }

  @Override
  public int hashCode() {
    return hashCode(getLeft(), getTop(), getRight(), getBottom());
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

  @NonNull
  public final RectF toRectF() {
    return ImmutableRectF.copyOf(this);
  }

  @NonNull
  public final RectF toRectF(@NonNull RectF dst) {
    Validate.isTrue(dst != null, "destination RectF cannot be null");
    dst.set(this);
    return dst;
  }

}
