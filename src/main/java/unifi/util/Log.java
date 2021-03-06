package unifi.util;

import com.badlogic.gdx.Gdx;

public class Log {

  private Log() {}

  public static void v(String tag, String msg) {
    Gdx.app.debug(tag, msg);
  }

  public static void v(String tag, String msg, Throwable tr) {
    Gdx.app.debug(tag, msg, tr);
  }

  public static void d(String tag, String msg) {
    Gdx.app.debug(tag, msg);
  }

  public static void d(String tag, String msg, Throwable tr) {
    Gdx.app.debug(tag, msg, tr);
  }

  public static void i(String tag, String msg) {
    Gdx.app.log(tag, msg);
  }

  public static void i(String tag, String msg, Throwable tr) {
    Gdx.app.log(tag, msg, tr);
  }

  public static void w(String tag, String msg) {
    Gdx.app.log(tag, msg);
  }

  public static void w(String tag, String msg, Throwable tr) {
    Gdx.app.log(tag, msg, tr);
  }

  public static void e(String tag, String msg) {
    Gdx.app.error(tag, msg);
  }

  public static void e(String tag, String msg, Throwable tr) {
    Gdx.app.error(tag, msg, tr);
  }

  // TODO: Implement this more completely: What a Terrible Failure
  public static void wtf(String tag, String msg, Throwable tr) {
    Gdx.app.error(tag, msg, tr);
  }

  public static boolean isLoggable(String tag, int level) {
    return Gdx.app.getLogLevel() >= level;
  }

}
