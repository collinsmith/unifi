package com.gmail.collinsmith70.unifi.content.res;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.badlogic.gdx.graphics.Color;
import com.gmail.collinsmith70.unifi.content.Context;
import com.google.common.primitives.UnsignedInteger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Resources {

  private static Map<ResourceReference, ?> RESOURCES;
  
  @Nullable
  public Object getValue(@NonNull ResourceReference ref) {
    if (!RESOURCES.containsKey(ref)) {
      throw new RuntimeException(String.format(Locale.ROOT,
          "Resource reference \"%s\" not found", ref));
    }
    
    return RESOURCES.get(ref);
  }
  
  @NonNull
  public TypedArray obtainAttributes(@NonNull Context context,
                                     @NonNull AttributeSet attrSet,
                                     @NonNull AttributeDecl<?>[] attrs) {
    Validate.isTrue(context != null, "context cannot be null");
    Validate.isTrue(attrSet != null, "attrSet cannot be null");
    Validate.isTrue(attrs != null, "attrs cannot be null");
    final TypedArray array = TypedArray.obtain(this, attrs.length);
    retrieveAttributes(context, attrSet, attrs, array.getData());
    return array;
  }

  public void retrieveAttributes(@NonNull Context context,
                                 @NonNull AttributeSet attrSet,
                                 @NonNull AttributeDecl<?>[] attrs,
                                 @NonNull Object[] data) {
    Validate.isTrue(context != null, "context cannot be null");
    Validate.isTrue(attrSet != null, "attrSet cannot be null");
    Validate.isTrue(attrs != null, "attrs cannot be null");
    Validate.isTrue(data != null, "data cannot be null");
    Class<?> type;
    ResourceReference ref;
    for (AttributeDecl<?> attr : attrs) {
      try {
        ref = ResourceReference.parse(context.getPackageName(),
            attrSet.getAttributeValue(attr.getNamespace(), attr.getName()));
        data[attr.getIndex()] = getValue(ref);
        ref.recycle();
      } catch (ParseException e) {
        type = attr.getType();
        if (type == Integer.class) {
          data[attr.getIndex()] =
              attrSet.getAttributeIntValue(attr.getNamespace(), attr.getName(), 0);
        } else if (type == UnsignedInteger.class || type == Color.class) {
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
  }

  public final class Theme {

  }

}
