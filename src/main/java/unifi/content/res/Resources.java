package unifi.content.res;

import android.support.annotation.NonNull;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

public class Resources implements Disposable {

  @NonNull static final String TAG = "Resources";

  @NonNull final AssetManager mAssets;

  private Resources() {
    mAssets = new AssetManager();
  }

  @Override
  public void dispose() {
    mAssets.dispose();
  }

  /**
   * Returns the asset manager of these resources.
   */
  @NonNull
  public final AssetManager getAssets() {
    return mAssets;
  }
}
