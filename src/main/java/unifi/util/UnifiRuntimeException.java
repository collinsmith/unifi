package unifi.util;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class UnifiRuntimeException extends GdxRuntimeException {

  public UnifiRuntimeException(String message) {
    super(message);
  }

  public UnifiRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnifiRuntimeException(Throwable cause) {
    super(cause);
  }

}
