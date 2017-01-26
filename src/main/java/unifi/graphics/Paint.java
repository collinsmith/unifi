package unifi.graphics;

import com.gmail.collinsmith70.unifi.annotation.Unsigned;

import org.apache.commons.lang.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.Json;

public class Paint {

  @Unsigned
  private int flags;

  @Unsigned
  private int color;

  public Paint() {}

  public Paint(@Unsigned int flags) {
    _setFlags(flags);
  }

  public Paint(@NonNull Paint src) {
    Validate.isTrue(src != null, "source Paint cannot be null");
    _set(src);
  }

  private void _set(@NonNull Paint src) {
    assert src != null : "source Paint cannot be null";
    _setFlags(src.getFlags());
    _setColor(src.getColor());
  }

  @Unsigned
  public int getFlags() {
    return flags;
  }

  private void _setFlags(@Unsigned int flags) {
    this.flags = flags;
  }

  @NonNull
  public Paint setFlags(@Unsigned int flags) {
    _setFlags(flags);
    return this;
  }

  @Unsigned
  public int getColor() {
    return color;
  }

  private void _setColor(@Unsigned int color) {
    this.color = color;
  }

  @NonNull
  public Paint setColor(@Unsigned int color) {
    _setColor(color);
    return this;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (!(obj instanceof Paint)) {
      return false;
    }

    Paint other = (Paint) obj;
    return getFlags() == other.getFlags()
        && getColor() == other.getColor();
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + getFlags();
    result = 31 * result + getColor();
    return result;
  }

  @Override
  public String toString() {
    return Json.createObjectBuilder()
        .add("flags", Integer.toBinaryString(getFlags()))
        .add("color", Color.toHexString(getColor()))
        .build().toString();
  }

}
