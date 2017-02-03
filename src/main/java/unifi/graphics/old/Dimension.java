package unifi.graphics.old;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.Json;

public class Dimension {

  public static final Dimension ZERO = ImmutableDimension.newImmutableDimension();

  private int width;
  private int height;

  public Dimension() {
    this(0, 0);
  }

  public Dimension(int width, int height) {
    Validate.isTrue(width >= 0, "width (%d) must be greater than or equal to 0", width);
    Validate.isTrue(height >= 0, "height (%d) must be greater than or equal to 0", height);
    _set(width, height);
  }

  public Dimension(@NonNull Dimension src) {
    Validate.isTrue(src != null, "source Dimension cannot be null");
    _set(src);
  }

  protected void onChange() {}

  public int getWidth() {
    return width;
  }

  private void _setWidth(int width) {
    assert width >= 0 : "width must be greater than or equal to 0";
    this.width = width;
  }

  public void setWidth(int width) {
    Validate.isTrue(width >= 0, "width (%d) must be greater than or equal to 0", width);
    if (getWidth() != width) {
      _setWidth(width);
      onChange();
    }
  }

  public int getHeight() {
    return height;
  }

  private void _setHeight(int height) {
    assert height >= 0 : "height must be greater than or equal to 0";
    this.height = height;
  }

  public void setHeight(int height) {
    Validate.isTrue(height >= 0, "height (%d) must be greater than or equal to 0", height);
    if (getHeight() != height) {
      _setHeight(height);
      onChange();
    }
  }

  private void _set(int width, int height) {
    assert width >= 0 : "width must be greater than or equal to 0";
    assert height >= 0 : "height must be greater than or equal to 0";
    _setWidth(width);
    _setHeight(height);
  }

  public void set(int width, int height) {
    Validate.isTrue(width >= 0, "width (%d) must be greater than or equal to 0", width);
    Validate.isTrue(height >= 0, "height (%d) must be greater than or equal to 0", height);
    if (!equals(width, height)) {
      _set(width, height);
      onChange();
    }
  }

  private void _set(@NonNull Dimension src) {
    assert src != null : "source Dimension cannot be null";
    _setWidth(src.getWidth());
    _setHeight(src.getHeight());
  }

  public void set(@NonNull Dimension src) {
    Validate.isTrue(src != null, "source Dimension cannot be null");
    if (!equals(src.getWidth(), src.getHeight())) {
      _set(src);
      onChange();
    }
  }

  public final boolean equals(int width, int height) {
    return getWidth() == width && getHeight() == height;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (!(obj instanceof Dimension)) {
      return false;
    }

    Dimension other = (Dimension) obj;
    return equals(other.getWidth(), other.getHeight());
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + getWidth();
    result = 31 * result + getHeight();
    return result;
  }

  @Override
  public String toString() {
    return Json.createObjectBuilder()
        .add("width", getWidth())
        .add("height", getHeight())
        .build().toString();
  }

}
