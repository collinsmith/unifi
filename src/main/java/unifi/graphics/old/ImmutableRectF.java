package unifi.graphics.old;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class ImmutableRectF extends RectF {

  public static ImmutableRectF newImmutableRectF() {
    return new ImmutableRectF();
  }

  @NonNull
  public static ImmutableRectF newImmutableRectF(float left, float top, float right, float bottom) {
    return new ImmutableRectF(left, top, right, bottom);
  }

  @NonNull
  public static ImmutableRectF copyOf(@NonNull RectF src) {
    return new ImmutableRectF(src); // Validates @NonNull
  }

  @NonNull
  public static ImmutableRectF copyOf(@NonNull Rect src) {
    return new ImmutableRectF(src); // Validates @NonNull
  }

  private ImmutableRectF() {
    super();
  }

  private ImmutableRectF(float left, float top, float right, float bottom) {
    super(left, top, right, bottom);
  }

  private ImmutableRectF(@NonNull RectF src) {
    super(src); // Validates @NonNull
  }

  private ImmutableRectF(@NonNull Rect src) {
    super(src); // Validates @NonNull
  }

  @Deprecated
  @Override
  public void setLeft(float left) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setTop(float top) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setRight(float right) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setBottom(float bottom) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(float left, float top, float right, float bottom) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull RectF src) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void set(@NonNull Rect src) {
    throw new UnsupportedOperationException();
  }

}
