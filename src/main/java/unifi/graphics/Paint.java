package unifi.graphics;

import com.gmail.collinsmith70.unifi.annotation.Unsigned;

import org.checkerframework.checker.nullness.qual.NonNull;

import javax.json.Json;

public class Paint {

  @Unsigned
  private int color;

  public Paint() {}

  @Unsigned
  public int getColor() {
    return color;
  }

  @NonNull
  public Paint setColor(@Unsigned int color) {
    this.color = color;
    return this;
  }

  @Override
  public String toString() {
    return Json.createObjectBuilder()
        .add("color", Color.toHexString(getColor()))
        .build().toString();
  }

}
