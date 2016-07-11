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
  
  /**
   * @see #isRecycled
   */
  private boolean recycled;
  
  /**
   * @see #isStyleAttributeReference()
   */
  private boolean isStyleAttributeReference;
  
  /**
   * @see #getPackageName
   */
  @Nullable
  private String packageName;
  
  
  /**
   * @see #getResourceType
   */
  @Nullable
  private Type resourceType;

  /**
   * @see #getResourceName
   */
  @Nullable
  private String resourceName;
  
  /**
   * Parses the specified {@code resourceId} and returns a pooled {@link ResourceReference}
   * instance.
   * 
   * @param defaultPackage Default resource package to use if {@code resourceId} did not specify one
   * @param resourceId     Resource ID {@code String} to parse
   * 
   * @return {@code ResourceReference} representing the parsed {@code resourceId}.
   * 
   * @throws ParseException when there was a problem parsing the given {@code resourceId}. This is
   *                        is usually do to a formatting issue.
   * 
   * @see #obtain
   */
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
  
  /**
   * Obtains a pooled {@link ResourceReference} programmatically. 
   * 
   * @param styleAttributeReference {@code true} if this is a reference to a styled
   *                                {@code ResourceReference}, otherwise {@code false}
   * @param packageName             Package which the {@code ResourceReference} belongs to
   * @param resourceType            Type which the {@code ResourceReference} value represents
   * @param resourceName            Name of the {@code ResourceReference}
   * 
   * @return {@code ResourceReference} representing the passed parameters.
   * 
   * @see #parse
   */
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
  
  /**
   * Recycles this {@code ResourceReference} instance and places it back into the pool.
   * {@code ResourceReference} instances should always be recycled after use.
   */
  public void recycle() {
    setRecycled(true);
  }

  /**
   * Resets the state of this {@code ResourceReference} instance to not recycled.
   * {@code ResourceReference} instances will be in this state when they are created.
   */
  @Override
  public void reset() {
    setRecycled(true);
  }
  
  /**
   * Checks whether or not this {@code ResourceReference} has been recycled, and throws an
   * {@link IllegalStateException} if it has.
   */
  private void checkRecycled() {
    if (recycled) {
      throw new IllegalStateException(toString() + " has been recycled!");
    }
  }
  
  /**
   * Constructs an empty {@link ResourceReference} instance which is not recycled.
   */
  ResourceReference() {
    setRecycled(true);
  }

  /**
   * Constructs a {@link ResourceReference} instance with the specified parameters.
   * 
   * @param styleAttributeReference {@code true} if this is a reference to a styled
   *                                {@code ResourceReference}, otherwise {@code false}
   * @param packageName             Package which the {@code ResourceReference} belongs to
   * @param resourceType            Type which the {@code ResourceReference} value represents
   * @param resourceName            Name of the {@code ResourceReference}
   */
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
  
  /**
   * Checks whether or not this {@code ResourceReference} is marked as recycled. Recycled
   * {@code ResourceReference} instances are invalidated and should not be operated on.
   * 
   * @return {@code true} if this {@code ResourceReference} has been recycled,
   *         otherwise {@code false}
   */
  public boolean isRecycled() {
    return recycled;
  }
  
  /**
   * Sets this {@code ResourceReference} to the specified {@linkplain #isRecycled() recycled} state.
   * 
   * @param recycled {@code true} if this {@code ResourceReference} has been recycled,
   *                 otherwise {@code false}
   */
  private void setRecycled(boolean recycled) {
    this.recycled = recycled;
  }
  
  /**
   * Checks whether or not this {@code ResourceReference} is to a styled attribute (i.e., an
   * attribute which is based on the current {@linkplain Resources.Theme theme}, and changes
   * dynamically to reflect that).
   * 
   * @return {@code true} if this {@code ResourceReference} is for a styled attribute,
   *         otherwise {@code false}
   */
  public boolean isStyleAttributeReference() {
    checkRecycled();
    return isStyleAttributeReference;
  }
  
  /**
   * Sets this {@code ResourceReference} to the specified
   * {@linkplain #isStyleAttributeReference() isStyleAttributeReference} state.
   * 
   * @param isStyleAttributeReference {@code true} if this {@code ResourceReference} is for a styled
   *                                  attribute, otherwise {@code false}
   */
  private void setStyleAttributeReference(boolean isStyleAttributeReference) {
    this.isStyleAttributeReference = isStyleAttributeReference;
  }
  
  /**
   * Package which this {@code ResourceReference} belongs to.
   * 
   * @return Package assigned to this {@code ResourceReference}
   */
  @NonNull
  public String getPackageName() {
    checkRecycled();
    return packageName;
  }
  
  /**
   * Changes the package of this {@code ResourceReference} to the specified one.
   * 
   * @param packageName Package to assign to this {@code ResourceReference}
   */
  private void setPackageName(@Nullable String packageName) {
    this.packageName = packageName;
  }
  
  /**
   * {@link Type} which the value of this {@code ResourceReference} represents.
   * 
   * @return {@code Type} that this {@code ResourceReference} corresponds to
   */
  @NonNull
  public Type getResourceType() {
    checkRecycled();
    return resourceType;
  }
  
  /**
   * Changes the type of this {@code ResourceReference} to the specified one.
   * 
   * @param resourceType {@code Type} that this {@code ResourceReference} should correspond with
   */
  private void setResourceType(@Nullable Type resourceType) {
    this.resourceType = resourceType;
  }
  
  /**
   * Name assigned to this {@code ResourceReference}.
   * 
   * @return Name assigned to this {@code ResourceReference}
   */
  @NonNull
  public String getResourceName() {
    checkRecycled();
    return resourceName;
  }
  
  /**
   * Changes the name of this {@code ResourceReference} to the specified one.
   * 
   * @param resourceName Name to assign to this {@code ResourceReference}
   */
  private void setResourceName(@Nullable String resourceName) {
    this.resourceName = resourceName;
  }
  
  /**
   * {@code String} representation of this {@code ResourceReference} using the
   * {@linkplain #parse parseable} format.
   * 
   * @return {@code String} representation of this {@code ResourceReference}
   */
  @Override
  public String toString() {
    if (getPackageName() == null) {
      return String.format(Locale.ROOT, "@%s/%s", getResourceType(), getResourceName());
    }

    return String.format(Locale.ROOT, "@%s:%s/%s",
        getPackageName(), getResourceType(), getResourceName());
  }
  
  /**
   * Checks whether or not this {@code ResourceReference} instance has fields equalling the passed
   * parameters.
   * 
   * @param isStyleAttributeReference {@code true} if this is a reference to a styled
   *                                  {@code ResourceReference}, otherwise {@code false}
   * @param packageName               Package which the {@code ResourceReference} belongs to
   * @param resourceType              Type which the {@code ResourceReference} value represents
   * @param resourceName              Name of the {@code ResourceReference}
   * 
   * @return {@code true} if it does, otherwise {@code false}
   */
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
   
  /**
   * Enumeration of all {@link ResourceReference} types. {@code ResourceReference} types are
   * {@linkplain ResourceReference#parse parseable} types.
   */
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
