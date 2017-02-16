package deprecated.unifi3.res;

import android.support.annotation.NonNull;

import deprecated.unifi3.util.DisplayMetrics;
import deprecated.unifi3.util.Pools;

public class Resources {

  @NonNull
  public static final String TAG = "Resources";

  /*package*/ final Pools.Pool<TypedArray> mTypedArrayPool = new Pools.SynchronizedPool<>(5);

  @NonNull
  final AssetManager mAssets = null;

  @NonNull
  final DisplayMetrics mMetrics = null;

  private Resources() {}

}
