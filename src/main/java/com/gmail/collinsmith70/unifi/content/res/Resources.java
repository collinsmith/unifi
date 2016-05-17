package com.gmail.collinsmith70.unifi.content.res;

import org.apache.commons.lang3.Validate;

import com.google.common.primitives.UnsignedInteger;

import android.support.annotation.NonNull;

public class Resources {

  @NonNull
  public TypedArray obtainAttributes(@NonNull AttributeSet attrSet,
                                     @NonNull AttributeDecl<?>[] attrs) {
    final TypedArray array = TypedArray.obtain(this, attrs.length);
    retrieveAttributes(attrSet, attrs, array.getData());
    return array;
  }

  public void retrieveAttributes(@NonNull AttributeSet attrSet,
                                 @NonNull AttributeDecl<?>[] attrs,
                                 @NonNull Object[] data) {
    Validate.isTrue(attrSet != null, "attrSet cannot be null");
    Validate.isTrue(attrs != null, "attrs cannot be null");
    Validate.isTrue(data != null, "data cannot be null");
    Class<?> type;
    for (AttributeDecl<?> attr : attrs) {
      type = attr.getType();
      if (type == Integer.class) {
        data[attr.getIndex()] =
            attrSet.getAttributeIntValue(attr.getNamespace(), attr.getName(), 0);
      } else if (type == UnsignedInteger.class) {
        data[attr.getIndex()] =
            attrSet.getAttributeUnsignedIntValue(attr.getNamespace(), attr.getName(), 0);
      } else if (type == Float.class) {
        data[attr.getIndex()] =
            attrSet.getAttributeFloatValue(attr.getNamespace(), attr.getName(), 0);
      } else if (type == Boolean.class) {
        data[attr.getIndex()] =
            attrSet.getAttributeBooleanValue(attr.getNamespace(), attr.getName(), false);
      } else if (type == String.class) {
        data[attr.getIndex()] = attrSet.getAttributeValue(attr.getNamespace(), attr.getName());
      }
    }
  }

  public final class Theme {

  }

}
