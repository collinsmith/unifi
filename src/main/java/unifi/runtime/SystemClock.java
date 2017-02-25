package unifi.runtime;

/**
 * A provider of clock values with varying precision. This class intended to provide some
 * abstraction for how those clock values are retrieved.
 */
public class SystemClock {

  private SystemClock() {}

  /**
   * @see System#currentTimeMillis()
   */
  public static long currentTimeMillis() {
    return System.currentTimeMillis();
  }

  /**
   * @see System#nanoTime()
   */
  public static long nanoTime() {
    return System.nanoTime();
  }

  /**
   * Returns {@link #nanoTime()} in the millisecond precision.
   *
   * @return {@link #nanoTime()} in the millisecond precision
   */
  public static long millisTime() {
    return System.nanoTime() / 1000000;
  }

  /**
   * Indicates whether or not the time specified by {@code to} comes after the time specified by
   * {@code from}. This method can handle both nanosecond and millisecond precision, however both
   * arguments must be of the same precision to retrieve any meaningful result.
   *
   * @param from The initial time {@code t0}
   * @param to   The current time {@code t}
   *
   * @return {@code true} if the different between the two times {@code t0 - t} is negative,
   *         otherwise {@code false}
   */
  public static boolean hasElapsed(long from, long to) {
    return to - from < 0;
  }

}
