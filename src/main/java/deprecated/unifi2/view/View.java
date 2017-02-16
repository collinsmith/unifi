package deprecated.unifi2.view;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class View {

  private static final boolean DBG = true;

  protected static final String VIEW_LOG_TAG = View.class.getSimpleName();

  public static final int NO_ID = -1;

  private static final int NOT_FOCUSABLE = 0x00000000;
  private static final int FOCUSABLE = 0x00000001;

  private static final int FOCUSABLE_MASK = 0x00000001;

  private static final int FITS_SYSTEM_WINDOWS = 0x00000002;

  @IntDef({VISIBLE, INVISIBLE, GONE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Visibility {
  }

  public static final int VISIBLE = 0x00000000;
  public static final int INVISIBLE = 0x00000004;
  public static final int GONE = 0x00000008;

  private static final int[] VISIBILITY_FLAGS = {VISIBLE, INVISIBLE, GONE};
  static final int VISIBILITY_MASK = 0x0000000C;

  static final int ENABLED = 0x00000000;
  static final int DISABLED = 0x00000020;

  static final int ENABLED_MASK = 0x00000020;

  private View() {
  }

  void setFlags(int flags, int mask) {
    final int old = mViewFlags;
  }

}
