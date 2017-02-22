package unifi.view;


import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import unifi.util.Log;

/**
 * A measure spec encapsulates the layout requirements passed from parent to a
 * child. Each measure spec represents a requirement for either the width or the
 * height. A measure spec is comprised of a size and a mode. There are three
 * possible modes: <dl> <dt>{@link MeasureSpec#UNSPECIFIED UNSPECIFIED}</dt>
 * <dd>The parent has not imposed any constraint on the child. It can be
 * whatever size it wants.</dd>
 * <p>
 * <dt>{@link MeasureSpec#EXACTLY EXACTLY}</dt> <dd>The parent has determined an
 * exact size for the child. The child is going to be given those bounds
 * regardless of how big it wants to be.</dd>
 * <p>
 * <dt>{@link MeasureSpec#AT_MOST AT_MOST}</dt> <dd>The child can be as large as
 * it wants, up to the specified size.</dd> </dl>
 * <p>
 * Measure specs are encoded as {@code int} values to reduce object allocation.
 * This class is provided to encode/decode the {@literal <size, mode>} tuple
 * into the {@code int}.
 */
public class MeasureSpec {
  /**
   * Offset of the {@link #getMode mode}
   */
  private static final int MODE_SHIFT = 30;

  /**
   * Bit mask for the {@link #getMode mode}
   */
  private static final int MODE_MASK = 0x3 << MODE_SHIFT;

  /**
   * Measurement mode specifying that the parent has not imposed and constraint
   * on the child, so it can be whatever size it wants.
   */
  public static final int UNSPECIFIED = 0 << MODE_SHIFT;

  /**
   * Measurement mode specifying that the parent has determined an exact size
   * for the child. The child is going to be given those bounds regardless of
   * how big it wants to be.
   */
  public static final int EXACTLY = 1 << MODE_SHIFT;

  /**
   * Measurement mode specifying that the child can be as large as it wants, up
   * to the specified size.
   */
  public static final int AT_MOST = 2 << MODE_SHIFT;

  @IntDef({ UNSPECIFIED, EXACTLY, AT_MOST })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {}

  /**
   * @see #create(int, int)
   */
  public static int makeMeasureSpec(int size, @Mode int mode) {
    return create(size, mode);
  }

  /**
   * Creates a measure specification based on the supplied size and mode.
   *
   * <p>The mode must always be one of: {@link MeasureSpec#UNSPECIFIED}, {@link
   * MeasureSpec#EXACTLY}, or {@link MeasureSpec#AT_MOST}.
   *
   * @param size The size of the measure specification
   * @param mode One of: {@link MeasureSpec#UNSPECIFIED}, {@link
   *             MeasureSpec#EXACTLY}, or {@link MeasureSpec#AT_MOST}.
   *
   * @return The measurement specification ({@code int}-encoded {@literal <size,
   * mode>} tuple)
   */
  public static int create(int size, @Mode int mode) {
    return (size & ~MODE_MASK) | (mode & MODE_MASK);
  }

  /**
   * Extracts the mode from the supplied measurement specification.
   *
   * @param measureSpec The measurement specification to extract the mode from
   *
   * @return One of: {@link MeasureSpec#UNSPECIFIED}, {@link
   * MeasureSpec#EXACTLY}, or {@link MeasureSpec#AT_MOST}.
   */
  @Mode
  public static int getMode(int measureSpec) {
    return (measureSpec & MODE_MASK);
  }

  /**
   * Extracts the size from the supplied measurement specification.
   *
   * @param measureSpec The measurement specification to extract the size from
   *
   * @return The size, in pixels, defined in the supplied measurement
   * specification
   */
  public static int getSize(int measureSpec) {
    return (measureSpec & ~MODE_MASK);
  }

  static int adjust(int measureSpec, int delta) {
    final int mode = getMode(measureSpec);
    if (mode == UNSPECIFIED) {
      /** No need to adjust size for {@link #UNSPECIFIED} mode */
      return create(0, UNSPECIFIED);
    }

    int size = getSize(measureSpec) + delta;
    if (size < 0) {
      Log.e(View.VIEW_LOG_TAG,
          "MeasureSpec.adjust: new size would be negative! (" + size + ") " +
          "spec: " + toString(measureSpec) + " delta: " + delta);
      size = 0;
    }

    return create(size, mode);
  }

  @NonNull
  public static String toString(int measureSpec) {
    int mode = getMode(measureSpec);
    int size = getSize(measureSpec);
    StringBuilder sb = new StringBuilder("MeasureSpec: ");
    switch (mode) {
      case UNSPECIFIED:
        sb.append("UNSPECIFIED ");
        break;
      case EXACTLY:
        sb.append("EXACTLY ");
        break;
      case AT_MOST:
        sb.append("AT_MOST ");
        break;
      default:
        sb.append(mode).append(' ');
    }

    sb.append(size);
    return sb.toString();
  }

  private MeasureSpec() {}
}
