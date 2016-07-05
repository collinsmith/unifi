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

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Represents a reference to a resource attribute. Resource references consist of (at a minimum),
 * a package, type and name. Similar to a file system, a resource reference is used to locate a
 * a cached resource in memory. Additionally, there are two distinct types of
 * {@code ResourceReference} instances, one which is a raw resource reference, and another which
 * is a styled attribute. Styled attributes are references to resources which are dependent on
 * the current {@linkplain Resources.Theme theme}.
 * <p>
 * As many {@code ResourceReference} instances may exist at a time, {@code ResourceReference} is
 * designed with <a href="https://en.wikipedia.org/wiki/Object_pool_pattern">object pooling</a>
 * built in:
 * <pre>
 * {@code
 *  ResourceReference ref = ResourceReference.parse("@unifi:string/HelloWorld");
 *  ...
 *  ref.recycle();
 * }
 * </pre>
 */
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
  static ResourceReference parse(@NonNull String defaultPackage,
                                 @NonNull String resourceId) throws ParseException {
    Validate.isTrue(defaultPackage != null, "defaultPackage cannot be null");
    Validate.isTrue(resourceId != null, "resourceId cannot be null");
    boolean isStyleAttributeReference = false;
    String packageName = defaultPackage;
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
                                  @NonNull String packageName,
                                  @NonNull Type resourceType,
                                  @NonNull String resourceName) {
    Validate.isTrue(packageName != null, "packageName cannot be null");
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
                    @NonNull String packageName,
                    @NonNull Type resourceType,
                    @NonNull String resourceName) {
    Validate.isTrue(packageName != null, "packageName cannot be null");
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
  
  @NonNull
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
  
  public boolean equals(boolean isStyleAttributeReference,
                        @Nullable String packageName,
                        @Nullable Type resourceType,
                        @Nullable String resourceName) {
    return isStyleAttributeReference() == isStyleAttributeReference
        && getPackageName().equals(packageName)
        && getResourceType() == resourceType
        && getResourceName().equals(resourceName);
  }

  @Override
  @CallSuper
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (!(obj instanceof ResourceReference)) {
      return false;
    }
    
    ResourceReference other = (ResourceReference) obj;
    return equals(other);
  }

  @Override
  @CallSuper
  public int hashCode() {
    int result = 17;
    result = 31 * result + (isStyleAttributeReference ? 1231 : 1237);
    result = 31 * result + ((packageName == null) ? 0 : packageName.hashCode());
    result = 31 * result + ((resourceName == null) ? 0 : resourceName.hashCode());
    result = 31 * result + ((resourceType == null) ? 0 : resourceType.hashCode());
    return result;
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
