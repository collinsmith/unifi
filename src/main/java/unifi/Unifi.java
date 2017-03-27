package unifi;

import unifi.util.Log;

public class Unifi {
  private static final String TAG = "Unifi";

  private Unifi() {}

  public static void init() {
    Log.i(TAG, "Initializing Unifi");
  }

}
