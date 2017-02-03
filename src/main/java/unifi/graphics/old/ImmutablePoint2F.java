package unifi.graphics.old;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class ImmutablePoint2F extends Point2F {

  @NonNull
  public static ImmutablePoint2F newImmutablePoint2F() {
    return new ImmutablePoint2F();
  }

  @NonNull
  public static ImmutablePoint2F newImmutablePoint2F(float x, float y) {
    return new ImmutablePoint2F(x, y);
  }

  @NonNull
  public static ImmutablePoint2F copyOf(@NonNull Point2F src) {
    return new ImmutablePoint2F(src); // Validates @NonNull
  }

  @NonNull
  public static ImmutablePoint2F copyOf(@NonNull Point2 src) {
    return new ImmutablePoint2F(src); // Validates @NonNull
  }

  private ImmutablePoint2F() {
    super();
  }

  private ImmutablePoint2F(float x, float y) {
    super(x, y);
  }

  private ImmutablePoint2F(@NonNull Point2F src) {
    super(src); // Validates @NonNull
  }

  private ImmutablePoint2F(@NonNull Point2 src) {
    super(src); // Validates @NonNull
  }

  @Deprecated
  @Override
  public void setX(float x) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setY(float y) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(float x, float y) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull Point2F src) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull Point2 src) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public @NonNull Point2F add(@NonNull Point2F src) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NonNull Point2F add(@NonNull Point2F src, @NonNull Point2F dst) {
    if (dst == this) {
      throw new UnsupportedOperationException();
    }

    return super.add(src, dst);
  }

  @Deprecated
  @Override
  public @NonNull Point2F add(@NonNull Point2 src) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NonNull Point2F add(@NonNull Point2 src, @NonNull Point2F dst) {
    if (dst == this) {
      throw new UnsupportedOperationException();
    }

    return super.add(src, dst);
  }

  @Deprecated
  @Override
  public @NonNull Point2F subtract(@NonNull Point2F src) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NonNull Point2F subtract(@NonNull Point2F src, @NonNull Point2F dst) {
    if (dst == this) {
      throw new UnsupportedOperationException();
    }

    return super.subtract(src, dst);
  }

  @Deprecated
  @Override
  public @NonNull Point2F subtract(@NonNull Point2 src) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NonNull Point2F subtract(@NonNull Point2 src, @NonNull Point2F dst) {
    if (dst == this) {
      throw new UnsupportedOperationException();
    }

    return super.subtract(src, dst);
  }

  @Deprecated
  @Override
  public @NonNull Point2F scale(double scalar) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NonNull Point2F scale(double scalar, @NonNull Point2F dst) {
    if (dst == this) {
      throw new UnsupportedOperationException();
    }

    return super.scale(scalar, dst);
  }

}
