package deprecated.unifi2.res;

import com.google.inject.Inject;

import org.apache.commons.lang.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;

import deprecated.unifi2.util.Pools;
import deprecated.unifi2.util.Pools.Pool;
import deprecated.unifi2.util.Pools.SimplePool;

public class Resources {

  @NonNull
  private final Pool<TypedArray> pool;

  public Resources() {
    this(Pools.synchronizedPool(new SimplePool<TypedArray>(5)));
  }

  @Inject
  public Resources(@NonNull Pool<TypedArray> pool) {
    Validate.isTrue(pool != null, "pool cannot be null");
    this.pool = pool;
  }

  @NonNull
  public Pool<TypedArray> getPool() {
    return pool;
  }

}
