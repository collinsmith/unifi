package unifi.graphics;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class ImmutablePoint2 extends Point2 {

  @NonNull
  public static ImmutablePoint2 newImmutablePoint2() {
    return new ImmutablePoint2();
  }

  @NonNull
  public static ImmutablePoint2 newImmutablePoint2(int x, int y) {
    return new ImmutablePoint2(x, y);
  }

  @NonNull
  public static ImmutablePoint2 copyOf(@NonNull Point2 src) {
    return new ImmutablePoint2(src);
  }

  private ImmutablePoint2() {
    super();
  }

  private ImmutablePoint2(int x, int y) {
    super(x, y);
  }

  private ImmutablePoint2(@NonNull Point2 src) {
    super(src);
  }

  @Deprecated
  @Override
  public void setX(int x) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setY(int y) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(int x, int y) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull Point2 src) {
    throw new UnsupportedOperationException();
  }

}
