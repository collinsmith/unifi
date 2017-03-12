package unifi.content;

import com.badlogic.gdx.utils.Disposable;

import unifi.content.res.Resources;

public class Context implements Disposable {

  public Context() {}

  @Override
  public void dispose() {
    // TODO: Dispose resources instance
  }

  public Resources getResources() {
    throw new UnsupportedOperationException();
  }
}
