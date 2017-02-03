package unifi.graphics.old;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ImmutableDimension extends Dimension {

  @NonNull
  public static ImmutableDimension newImmutableDimension() {
    return new ImmutableDimension();
  }

  @NonNull
  public static ImmutableDimension newImmutableDimension(int width, int height) {
    return new ImmutableDimension(width, height);
  }

  @NonNull
  public static ImmutableDimension copyOf(@NonNull Dimension src) {
    return new ImmutableDimension(src); // Validates @NonNull
  }

  private ImmutableDimension() {
    super();
  }

  private ImmutableDimension(int width, int height) {
    super(width, height);
  }

  private ImmutableDimension(@NonNull Dimension src) {
    super(src); // Validates @NonNull
  }

  @Deprecated
  @Override
  public void setWidth(int width) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setHeight(int height) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(int width, int height) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull Dimension src) {
    throw new UnsupportedOperationException();
  }

}
