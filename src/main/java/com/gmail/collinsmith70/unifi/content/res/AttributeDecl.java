package com.gmail.collinsmith70.unifi.content.res;

import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.gmail.collinsmith70.unifi.Unifi;
import com.gmail.collinsmith70.unifi.annotation.Index;

import android.support.annotation.NonNull;

/**
 * An {@code AttributeDecl} defines a reference to a resource attribute which can be used within
 * resource files. {@code AttributeDecl} are used to create programmatic associations between
 * resource files and the code which would like to access them. Every {@code AttributeDecl} must
 * be associated with some namespace (typically, internal Unifi {@code AttributeDecl}s are
 * declared within the {@linkplain Unifi#NAMESPACE Unifi namespace}). Additionally, indexes should
 * be used to uniquely identify an {@code AttributeDecl} as part of a {@link TypedArray} aggregate.
 * 
 * <pre>
 * {@code
 *  public class ColorDrawable {
 *    public static final int color = 0;
 *    
 *    public static final AttributeDecl<?>[] attrs = new AttributeDecl<?>[] {
 *      new AttributeDecl<Color>(color, Unifi.NAMESPACE, "color", Color.class)
 *    };
 *  }
 * }
 * </pre>
 * 
 * @param <T> Type of the resource this resource reference references.
 */
public class AttributeDecl<T> {

  /**
   * @see #getNamespace
   */
  @NonNull
  private final String namespace;

  /**
   * @see #getName
   */
  @NonNull
  private final String name;

  /**
   * @see #getType
   */
  @NonNull
  private final Class<T> type;

  /**
   * @see #getIndex
   */
  @Index
  private final int index;

  /**
   * Constructs an {@link AttributeDecl} using the specified parameters.
   * 
   * @param index     Index used to identify the {@code AttributeDecl} value within the
   *                      {@linkplain TypedArray programmatic structure} of the resource.
   * @param namespace Namespace of the {@code AttributeDecl}
   * @param name      Name of the {@code AttributeDecl}
   * @param type      Type which the {@code AttributeDecl} represents (used when resolving a value
   *                      of the {@code AttributeDecl})
   */
  public AttributeDecl(@Index int index,
                       @NonNull String namespace,
                       @NonNull String name,
                       @NonNull Class<T> type) {
    Validate.isTrue(index >= 0, "index cannot be less than 0");
    Validate.isTrue(namespace != null, "namespace cannot be null");
    Validate.isTrue(name != null, "name cannot be null");
    Validate.isTrue(type != null, "type cannot be null");
    this.index = index;
    this.namespace = namespace;
    this.name = name;
    this.type = type;
  }

  /**
   * Index used to identify this {@code AttributeDecl} value within the
   * {@linkplain TypedArray programmatic structure} of the resource.
   * 
   * @return Index of this {@code AttributeDecl} 
   */
  @Index
  public int getIndex() {
    return index;
  }

  /**
   * Namespace of which this {@code AttributeDecl} belongs.
   * 
   * @return Namespace of this {@code AttributeDecl}
   */
  @NonNull
  public String getNamespace() {
    return namespace;
  }

  /**
   * Name of this {@code AttributeDecl}.
   * 
   * @return Name of this {@code AttributeDecl}
   */
  @NonNull
  public String getName() {
    return name;
  }

  /**
   * Type of the attribute which this {@code AttributeDecl} represents (used when resolving 
   * a value instance of this attribute).
   * 
   * @return Type of the attribute which this {@code AttributeDecl} represents
   */
  @NonNull
  public Class<T> getType() {
    return type;
  }

  @Override
  public String toString() {
    return String.format(Locale.ROOT,
        "AttributeDecl: { id=%d, namespace=\"%s\", name=\"%s\", type=\"%s\" }", index, namespace,
        name, type.getName());
  }

}
