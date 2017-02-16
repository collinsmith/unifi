package deprecated.unifi3.res;

import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.Arrays;

import deprecated.unifi3.util.DisplayMetrics;
import deprecated.unifi3.util.Log;
import deprecated.unifi3.util.TypedValue;

public class TypedArray {

  static TypedArray obtain(@NonNull Resources res, @IntRange(from = 0) int len) {
    final TypedArray attrs = res.mTypedArrayPool.acquire();
    if (attrs != null) {
      attrs.mRecycled = false;
      attrs.mLength = len;

      final int fullLen = len * AssetManager.STYLE_NUM_ENTRIES;
      if (attrs.mData.length >= fullLen) {
        return attrs;
      }

      attrs.mData = new int[fullLen];
      attrs.mIndices = new int[len + 1];
      return attrs;
    }

    return new TypedArray(res,
        new int[len * AssetManager.STYLE_NUM_ENTRIES],
        new int[len + 1], len);
  }

  @NonNull
  private final Resources mResources;

  @NonNull
  private final DisplayMetrics mMetrics;

  @NonNull
  private final AssetManager mAssets;


  @NonNull
  @Size(min = 0)
  /*package*/ int[] mData;

  @NonNull
  @Size(min = 1)
  /*package*/ int[] mIndices;

  @NonNull
  /*package*/ int mLength;

  @NonNull
  /*package*/ TypedValue mValue = new TypedValue();

  private boolean mRecycled;

  /*package*/ TypedArray(@NonNull Resources res, @NonNull @Size(min = 0) int[] data,
                         @NonNull @Size(min = 1) int[] indices, @IntRange(from = 0) int len) {
    this.mResources = res;
    this.mMetrics = mResources.mMetrics;
    this.mAssets = mResources.mAssets;
    this.mData = data;
    this.mIndices = indices;
    this.mLength = len;
  }

  @NonNull
  @Override
  public String toString() {
    return Arrays.toString(mData);
  }

  private void checkRecycled() {
    if (mRecycled) {
      throw new IllegalStateException("Cannot make calls to a recycled instance!");
    }
  }

  @IntRange(from = 0)
  public int length() {
    checkRecycled();
    return mLength;
  }

  public int getIndexCount() {
    checkRecycled();
    return mIndices[0];
  }

  public int getIndex(int index) {
    checkRecycled();
    return mIndices[index + 1];
  }

  @NonNull
  public Resources getResources() {
    checkRecycled();
    return mResources;
  }

  private boolean getValueAt(@IntRange(from = 0) int index, @NonNull TypedValue outValue) {
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return false;
    }

    outValue.type = type;
    outValue.data = data[index + AssetManager.STYLE_DATA];
    outValue.assetCookie = data[index + AssetManager.STYLE_ASSET_COOKIE];
    outValue.resourceId = data[index + AssetManager.STYLE_RESOURCE_ID];
    outValue.changingConfigurations = data[index + AssetManager.STYLE_CHANGING_CONFIGURATIONS];
    outValue.density = data[index + AssetManager.STYLE_DENSITY];
    outValue.string = (type == TypedValue.TYPE_STRING) ? loadStringValueAt(index) : null;
    return true;
  }

  private CharSequence loadStringValueAt(int index) {
    final int[] data = mData;
    final int cookie = data[index + AssetManager.STYLE_ASSET_COOKIE];
    if (cookie < 0) {
      if (mXml != null) {
        return mXml.getPooledString(
            data[index + AssetManager.STYLE_DATA]);
      }
      return null;
    }
    return mAssets.getPooledStringForCookie(cookie, data[index + AssetManager.STYLE_DATA]);
  }

  @Nullable
  public CharSequence getText(@IntRange(from = 0) int index) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return null;
    } else if (type == TypedValue.TYPE_STRING) {
      return loadStringValueAt(index);
    }

    TypedValue v = mValue;
    if (getValueAt(index, v)) {
      Log.w(Resources.TAG, "Converting to string: " + v);
      CharSequence cs = v.coerceToString();
      return cs != null ? cs.toString() : null;
    }

    Log.w(Resources.TAG, "getString of bad type: 0x" + Integer.toHexString(type));
    return null;
  }

  @Nullable
  public String getString(@IntRange(from = 0) int index) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return null;
    } else if (type == TypedValue.TYPE_STRING) {
      return loadStringValueAt(index).toString();
    }

    TypedValue v = mValue;
    if (getValueAt(index, v)) {
      Log.w(Resources.TAG, "Converting to string: " + v);
      CharSequence cs = v.coerceToString();
      return cs != null ? cs.toString() : null;
    }

    Log.w(Resources.TAG, "getString of bad type: 0x" + Integer.toHexString(type));
    return null;
  }

  @Nullable
  public String getNonResourceString(@IntRange(from = 0) int index) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_STRING) {
      final int cookie = data[index + AssetManager.STYLE_ASSET_COOKIE];
      if (cookie < 0) {
        return mXml.getPooledString(
            data[index + AssetManager.STYLE_DATA]).toString();
      }
    }

    return null;
  }

  @Nullable
  public String getNonConfigurationString(@IntRange(from = 0) int index,
                                          int allowedChangingConfigs) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if ((data[index + AssetManager.STYLE_CHANGING_CONFIGURATIONS] & ~allowedChangingConfigs) != 0) {
      return null;
    }

    if (type == TypedValue.TYPE_NULL) {
      return null;
    } else if (type == TypedValue.TYPE_STRING) {
      return loadStringValueAt(index).toString();
    }

    TypedValue v = mValue;
    if (getValueAt(index, v)) {
      Log.w(Resources.TAG, "Converting to string: " + v);
      CharSequence cs = v.coerceToString();
      return cs != null ? cs.toString() : null;
    }

    Log.w(Resources.TAG, "getString of bad type: 0x" + Integer.toHexString(type));
    return null;
  }

  public boolean getBoolean(@IntRange(from = 0) int index, boolean defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
      return data[index + AssetManager.STYLE_DATA] != 0;
    }

    TypedValue v = mValue;
    if (getValueAt(index, v)) {
      Log.w(Resources.TAG, "Converting to boolean: " + v);
      return XmlUtils.convertValueToBoolean(
          v.coerceToString(), defValue);
    }
    Log.w(Resources.TAG, "getBoolean of bad type: 0x"
        + Integer.toHexString(type));
    return defValue;
  }

  public int getInt(@IntRange(from = 0) int index, int defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
      return data[index + AssetManager.STYLE_DATA];
    }

    TypedValue v = mValue;
    if (getValueAt(index, v)) {
      Log.w(Resources.TAG, "Converting to int: " + v);
      return XmlUtils.convertValueToInt(
          v.coerceToString(), defValue);
    }

    Log.w(Resources.TAG, "getInt of bad type: 0x" + Integer.toHexString(type));
    return defValue;
  }

  public float getFloat(@IntRange(from = 0) int index, float defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type == TypedValue.TYPE_FLOAT) {
      return Float.intBitsToFloat(data[index + AssetManager.STYLE_DATA]);
    } else if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
      return data[index + AssetManager.STYLE_DATA];
    }

    TypedValue v = mValue;
    if (getValueAt(index, v)) {
      Log.w(Resources.TAG, "Converting to float: " + v);
      CharSequence str = v.coerceToString();
      if (str != null) {
        return Float.parseFloat(str.toString());
      }
    }

    Log.w(Resources.TAG, "getFloat of bad type: 0x" + Integer.toHexString(type));
    return defValue;
  }

  @ColorInt
  public int getColor(@IntRange(from = 0) int index, @ColorInt int defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
      return data[index + AssetManager.STYLE_DATA];
    } else if (type == TypedValue.TYPE_STRING) {
      final TypedValue value = mValue;
      if (getValueAt(index, value)) {
        ColorStateList csl = mResources.loadColorStateList(value, value.resourceId);
        return csl.getDefaultColor();
      }

      return defValue;
    } else if (type == TypedValue.TYPE_ATTRIBUTE) {
      throw new RuntimeException("Failed to resolve attribute at index " + index);
    }

    throw new UnsupportedOperationException("Can't convert to color: type=0x"
        + Integer.toHexString(type));
  }

  @Nullable
  public ColorStateList getColorStateList(@IntRange(from = 0) int index) {
    checkRecycled();
    final TypedValue value = mValue;
    if (getValueAt(index * AssetManager.STYLE_NUM_ENTRIES, value)) {
      if (value.type == TypedValue.TYPE_ATTRIBUTE) {
        throw new RuntimeException("Failed to resolve attribute at index " + index);
      }

      return mResources.loadColorStateList(value, value.resourceId);
    }

    return null;
  }

  public int getInteger(@IntRange(from = 0) int index, int defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type >= TypedValue.TYPE_FIRST_INT
        && type <= TypedValue.TYPE_LAST_INT) {
      return data[index + AssetManager.STYLE_DATA];
    } else if (type == TypedValue.TYPE_ATTRIBUTE) {
      throw new RuntimeException("Failed to resolve attribute at index " + index);
    }

    throw new UnsupportedOperationException("Can't convert to integer: type=0x"
        + Integer.toHexString(type));
  }

  public float getDimension(@IntRange(from = 0) int index, float defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type == TypedValue.TYPE_DIMENSION) {
      return TypedValue.complexToDimension(
          data[index + AssetManager.STYLE_DATA], mMetrics);
    } else if (type == TypedValue.TYPE_ATTRIBUTE) {
      throw new RuntimeException("Failed to resolve attribute at index " + index);
    }

    throw new UnsupportedOperationException("Can't convert to dimension: type=0x"
        + Integer.toHexString(type));
  }

  public int getDimensionPixelOffset(@IntRange(from = 0) int index, int defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type == TypedValue.TYPE_DIMENSION) {
      return TypedValue.complexToDimensionPixelOffset(
          data[index + AssetManager.STYLE_DATA], mMetrics);
    } else if (type == TypedValue.TYPE_ATTRIBUTE) {
      throw new RuntimeException("Failed to resolve attribute at index " + index);
    }

    throw new UnsupportedOperationException("Can't convert to dimension: type=0x"
        + Integer.toHexString(type));
  }

  public int getDimensionPixelSize(@IntRange(from = 0) int index, int defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type == TypedValue.TYPE_DIMENSION) {
      return TypedValue.complexToDimensionPixelSize(
          data[index + AssetManager.STYLE_DATA], mMetrics);
    } else if (type == TypedValue.TYPE_ATTRIBUTE) {
      throw new RuntimeException("Failed to resolve attribute at index " + index);
    }

    throw new UnsupportedOperationException("Can't convert to dimension: type=0x"
        + Integer.toHexString(type));
  }

  public int getLayoutDimension(@IntRange(from = 0) int index, String name) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type >= TypedValue.TYPE_FIRST_INT
        && type <= TypedValue.TYPE_LAST_INT) {
      return data[index + AssetManager.STYLE_DATA];
    } else if (type == TypedValue.TYPE_DIMENSION) {
      return TypedValue.complexToDimensionPixelSize(
          data[index + AssetManager.STYLE_DATA], mMetrics);
    } else if (type == TypedValue.TYPE_ATTRIBUTE) {
      throw new RuntimeException("Failed to resolve attribute at index " + index);
    }

    throw new RuntimeException(getPositionDescription()
        + ": You must supply a " + name + " attribute.");
  }

  public int getLayoutDimension(@IntRange(from = 0) int index, int defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type >= TypedValue.TYPE_FIRST_INT
        && type <= TypedValue.TYPE_LAST_INT) {
      return data[index + AssetManager.STYLE_DATA];
    } else if (type == TypedValue.TYPE_DIMENSION) {
      return TypedValue.complexToDimensionPixelSize(
          data[index + AssetManager.STYLE_DATA], mMetrics);
    }

    return defValue;
  }

  public float getFraction(@IntRange(from = 0) int index, int base, int pbase, float defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    if (type == TypedValue.TYPE_NULL) {
      return defValue;
    } else if (type == TypedValue.TYPE_FRACTION) {
      return TypedValue.complexToFraction(
          data[index + AssetManager.STYLE_DATA], base, pbase);
    } else if (type == TypedValue.TYPE_ATTRIBUTE) {
      throw new RuntimeException("Failed to resolve attribute at index " + index);
    }

    throw new UnsupportedOperationException("Can't convert to fraction: type=0x"
        + Integer.toHexString(type));
  }

  public int getResourceId(@IntRange(from = 0) int index, int defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    if (data[index + AssetManager.STYLE_TYPE] != TypedValue.TYPE_NULL) {
      final int resid = data[index + AssetManager.STYLE_RESOURCE_ID];
      if (resid != 0) {
        return resid;
      }
    }
    return defValue;
  }

  public int getThemeAttributeId(@IntRange(from = 0) int index, int defValue) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    if (data[index + AssetManager.STYLE_TYPE] == TypedValue.TYPE_ATTRIBUTE) {
      return data[index + AssetManager.STYLE_DATA];
    }
    return defValue;
  }

  @Nullable
  public Drawable getDrawable(@IntRange(from = 0) int index) {
    checkRecycled();
    final TypedValue value = mValue;
    if (getValueAt(index * AssetManager.STYLE_NUM_ENTRIES, value)) {
      if (value.type == TypedValue.TYPE_ATTRIBUTE) {
        throw new RuntimeException("Failed to resolve attribute at index " + index);
      }
      return mResources.loadDrawable(value, value.resourceId, mTheme);
    }
    return null;
  }

  @Nullable
  public CharSequence[] getTextArray(@IntRange(from = 0) int index) {
    checkRecycled();
    final TypedValue value = mValue;
    if (getValueAt(index * AssetManager.STYLE_NUM_ENTRIES, value)) {
      return mResources.getTextArray(value.resourceId);
    }
    return null;
  }

  public boolean getValue(@IntRange(from = 0) int index, TypedValue outValue) {
    checkRecycled();
    return getValueAt(index * AssetManager.STYLE_NUM_ENTRIES, outValue);
  }

  public int getType(@IntRange(from = 0) int index) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    return mData[index + AssetManager.STYLE_TYPE];
  }

  public boolean hasValue(@IntRange(from = 0) int index) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    return type != TypedValue.TYPE_NULL;
  }

  public boolean hasValueOrEmpty(@IntRange(from = 0) int index) {
    checkRecycled();
    index *= AssetManager.STYLE_NUM_ENTRIES;
    final int[] data = mData;
    final int type = data[index + AssetManager.STYLE_TYPE];
    return type != TypedValue.TYPE_NULL
        || data[index + AssetManager.STYLE_DATA] == TypedValue.DATA_NULL_EMPTY;
  }

  @Nullable
  public TypedValue peekValue(@IntRange(from = 0) int index) {
    checkRecycled();
    final TypedValue value = mValue;
    if (getValueAt(index * AssetManager.STYLE_NUM_ENTRIES, value)) {
      return value;
    }
    return null;
  }

  @NonNull
  public String getPositionDescription() {
    checkRecycled();
    return mXml != null ? mXml.getPositionDescription() : "<internal>";
  }

  public void recycle() {
    if (mRecycled) {
      throw new RuntimeException(toString() + " recycled twice!");
    }

    mRecycled = true;

    // These may have been set by the client.
    mXml = null;
    mTheme = null;

    mResources.mTypedArrayPool.release(this);
  }

  @Nullable
  public int[] extractThemeAttrs() {
    checkRecycled();
    int[] attrs = null;

    final int[] data = mData;
    final int N = length();
    for (int i = 0; i < N; i++) {
      final int index = i * AssetManager.STYLE_NUM_ENTRIES;
      if (data[index + AssetManager.STYLE_TYPE] != TypedValue.TYPE_ATTRIBUTE) {
        continue;
      }

      // Null the entry so that we can safely call getZzz().
      data[index + AssetManager.STYLE_TYPE] = TypedValue.TYPE_NULL;

      final int attr = data[index + AssetManager.STYLE_DATA];
      if (attr == 0) {
        // This attribute is useless!
        continue;
      }

      if (attrs == null) {
        attrs = new int[N];
      }
      attrs[i] = attr;
    }

    return attrs;
  }
}
