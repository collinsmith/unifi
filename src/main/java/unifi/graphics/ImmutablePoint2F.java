package unifi.graphics;

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
    return new ImmutablePoint2F(src);
  }

  @NonNull
  public static ImmutablePoint2F copyOf(@NonNull Point2 src) {
    return new ImmutablePoint2F(src);
  }

  private ImmutablePoint2F() {
    super();
  }

  private ImmutablePoint2F(float x, float y) {
    super(x, y);
  }

  private ImmutablePoint2F(@NonNull Point2F src) {
    super(src);
  }

  private ImmutablePoint2F(@NonNull Point2 src) {
    super(src);
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

}
