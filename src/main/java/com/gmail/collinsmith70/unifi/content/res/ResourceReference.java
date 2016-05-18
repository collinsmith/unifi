package com.gmail.collinsmith70.unifi.content.res;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ResourceReference implements Poolable {

  private static final String TAG = ResourceReference.class.getSimpleName();
  
  private boolean recycled;
  
  private boolean isStyleAttributeReference;
  
  @Nullable
  private String packageName;
  
  @Nullable
  private Type resourceType;
  
  @Nullable
  private String resourceName;
  
  @NonNull
  static ResourceReference parse(@NonNull String resourceId) throws ParseException {
    Validate.isTrue(resourceId != null, "resourceId cannot be null");
    boolean isStyleAttributeReference = false;
    String packageName = null;
    Type resourceType = null;
    String resourceName = null;
    try {
      int pos = 0;
      Reader r = new StringReader(resourceId);
      int ch = r.read();
      pos++;
      if (ch == -1) {
        throw new ParseException(
            "Unable to parse resource identifier: Unexpected end of string: "
            + "Resource identifiers should be formatted like: "
            + "@[package:]type/name", pos);
      } else if (ch != '@' && ch != '?') {
        throw new ParseException(
            "Unable to parse resource identifier: Missing reference type: "
            + "Resource identifiers should be formatted like: "
            + "@[package:]type/name", pos);
      }

      isStyleAttributeReference = ch == '?';

      String tmp;
      StringBuilder sb = new StringBuilder();

      ch = r.read();
      pos++;
      while (ch != -1 && ch != ':' && ch != '/') {
        sb.append((char) ch);
        ch = r.read();
        pos++;
      }

      switch (ch) {
        case -1:
          throw new ParseException(
              "Unable to parse resource identifier: Unexpected end of string: "
              + "Resource identifiers should be formatted like: "
              + "@[package:]type/name",
              pos);
        case ':':
          if (sb.length() == 0) {
            throw new ParseException(
                "Unable to parse resource identifier: Invalid format: "
                + "':' given without package string preceding: "
                + "Resource identifiers should be formatted like: "
                + "@[package:]type/name",
                pos);
          }

          packageName = sb.toString();

          sb = new StringBuilder();
          ch = r.read();
          pos++;
          while (ch != -1 && ch != '/') {
            sb.append((char) ch);
            ch = r.read();
            pos++;
          }

          if (ch == -1) {
            throw new ParseException(
                "Unable to parse resource identifier: Unexpected end of string: "
                    + "Resource identifiers should be formatted like: "
                    + "@[package:]type/name",
                pos);
          }

        case '/':
          if (sb.length() == 0) {
            throw new ParseException(
                "Unable to parse resource identifier: Invalid format: "
                    + "'/' given without type string preceding: "
                    + "Resource identifiers should be formatted like: "
                    + "@[package:]type/name",
                pos);
          }

          tmp = null;
          try {
            resourceType = Type.valueOf(tmp = sb.toString());
          } catch (IllegalArgumentException e) {
            throw new ParseException(
                "Unable to parse resource identifier: Unidentifiable type: " + tmp, pos);
          }

          break;
      }

      sb = new StringBuilder();
      ch = r.read();
      pos++;
      while (ch != -1) {
        sb.append((char) ch);
        ch = r.read();
        pos++;
      }

      if (sb.length() == 0) {
        throw new ParseException("Unable to parse resource identifier: Unexpected end of string: "
            + "Resource identifiers should be formatted like: "
            + "@[package:]type/name", pos);
      }

      resourceName = sb.toString();
    } catch (IOException e) {
      Gdx.app.error(TAG, e.getMessage(), e);
    }
    
    return ResourceReference.obtain(
        isStyleAttributeReference, packageName, resourceType, resourceName);
  }
  
  @NonNull
  static ResourceReference obtain(boolean styleAttributeReference,
                                  @Nullable String packageName,
                                  @NonNull Type resourceType,
                                  @NonNull String resourceName) {
    Validate.isTrue(resourceType != null, "resourceType cannot be null");
    Validate.isTrue(resourceName != null, "resourceName cannot be null");
    final ResourceReference ref = Pools.obtain(ResourceReference.class);
    ref.setRecycled(false);
    ref.setStyleAttributeReference(styleAttributeReference);
    ref.setPackageName(packageName);
    ref.setResourceType(resourceType);
    ref.setResourceName(resourceName);
    return ref;
  }
  
  public void recycle() {
    setRecycled(true);
  }
  
  @Override
  public void reset() {
    setRecycled(true);
  }
  
  private void checkRecycled() {
    if (recycled) {
      throw new IllegalStateException(toString() + " has been recycled!");
    }
  }
  
  ResourceReference() {
    setRecycled(true);
  }
  
  ResourceReference(boolean styleAttributeReference,
                    @Nullable String packageName,
                    @NonNull Type resourceType,
                    @NonNull String resourceName) {
    Validate.isTrue(resourceType != null, "resourceType cannot be null");
    Validate.isTrue(resourceName != null, "resourceName cannot be null");
    setRecycled(false);
    setStyleAttributeReference(styleAttributeReference);
    setPackageName(packageName);
    setResourceType(resourceType);
    setResourceName(resourceName);
  }
  
  public boolean isRecycled() {
    return recycled;
  }
  
  private void setRecycled(boolean recycled) {
    this.recycled = recycled;
  }
  
  public boolean isStyleAttributeReference() {
    checkRecycled();
    return isStyleAttributeReference;
  }
  
  private void setStyleAttributeReference(boolean isStyleAttributeReference) {
    this.isStyleAttributeReference = isStyleAttributeReference;
  }
  
  @Nullable
  public String getPackageName() {
    checkRecycled();
    return packageName;
  }
  
  private void setPackageName(@Nullable String packageName) {
    this.packageName = packageName;
  }
  
  @NonNull
  public Type getResourceType() {
    checkRecycled();
    return resourceType;
  }
  
  private void setResourceType(@Nullable Type resourceType) {
    this.resourceType = resourceType;
  }
  
  @NonNull
  public String getResourceName() {
    checkRecycled();
    return resourceName;
  }
  
  private void setResourceName(@Nullable String resourceName) {
    this.resourceName = resourceName;
  }
  
  @Override
  public String toString() {
    if (getPackageName() == null) {
      return String.format(Locale.ROOT, "@%s/%s", getResourceType(), getResourceName());
    }

    return String.format(Locale.ROOT, "@%s:%s/%s",
        getPackageName(), getResourceType(), getResourceName());
  }
  
  public enum Type {
      color(),
      string();

      @Nullable
      private final String tag;

      Type() {
          this.tag = name().toLowerCase(Locale.ROOT);
      }

      Type(@NonNull String tag) {
          Validate.isTrue(tag != null, "tag cannot null");
          Validate.isTrue(!tag.isEmpty(), "tag cannot be empty");
          this.tag = tag;
      }

      @NonNull
      public String getTag() {
          return tag;
      }

  }
  
}
