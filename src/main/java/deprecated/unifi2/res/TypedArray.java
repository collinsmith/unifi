package deprecated.unifi2.res;

import com.google.common.base.Preconditions;

import org.apache.commons.lang.Validate;

import java.util.Arrays;

import deprecated.unifi2.util.Pools;

public class TypedArray implements Pools.Poolable {

  static TypedArray obtain(Resources res, int len) {
    Preconditions.checkArgument(res != null, "res cannot be null");
    Validate.isTrue(len >= 0, "len (%d) must be >= 0", len);
    final TypedArray attrs = res.getPool().acquire();
    if (attrs != null) {
      return attrs;
    }

    return new TypedArray(res);
  }

  private boolean recycled;

  private Object[] data;

  TypedArray() {}

  TypedArray(Resources res) {
  }

  public boolean isRecycled() {
    return recycled;
  }

  @Override
  public void recycle() {
    checkRecycled();
    getResources().getPool().release(this);
    this.recycled = true;
  }

  private void checkRecycled() {
    if (isRecycled()) {
      throw new IllegalStateException(String.format("This %s has already been recycled!",
          getClass().getSimpleName()));
    }
  }

  public Resources getResources() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return Arrays.toString(data);
  }
}
