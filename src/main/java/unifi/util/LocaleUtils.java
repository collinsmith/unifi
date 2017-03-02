package unifi.util;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Utility functions for dealing with locales.
 */
public class LocaleUtils {

  private LocaleUtils() {}

  private static final Set<String> RTL;
  static {
    Set<String> lang = new HashSet<>();
    lang.add("ar");
    lang.add("dv");
    lang.add("fa");
    lang.add("ha");
    lang.add("he");
    lang.add("iw");
    lang.add("ji");
    lang.add("ps");
    lang.add("ur");
    lang.add("yi");
    RTL = Collections.unmodifiableSet(lang);
  }

  /**
   * Returns the layout direction for a given locale.
   *
   * @return {@link LayoutDirection#RTL} or {@link LayoutDirection#LTR}
   */
  @LayoutDirection.Resolved
  public static int getLayoutDirection(@Nullable Locale locale) {
    if (locale != null && !locale.equals(Locale.ROOT)) {
      /** <a href="http://stackoverflow.com/a/20821395/3134241">source</a> */
      return RTL.contains(locale) ? LayoutDirection.RTL : LayoutDirection.LTR;
    }

    // TODO: Force RTL if development property set, else default to LTR
    return LayoutDirection.LTR;
  }

}
