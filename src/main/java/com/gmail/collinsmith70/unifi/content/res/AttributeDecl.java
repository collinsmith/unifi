package com.gmail.collinsmith70.unifi.content.res;

import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.gmail.collinsmith70.unifi.annotation.Index;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AttributeDecl<T> {

  @Nullable
  private final String namespace;

  @NonNull
  private final String name;

  @NonNull
  private final Class<T> type;

  @Index
  private final int index;

  public AttributeDecl(@Index int index,
                       @Nullable String namespace,
                       @NonNull String name,
                       @NonNull Class<T> type) {
    Validate.isTrue(index >= 0, "index cannot be less than 0");
    Validate.isTrue(name != null, "name cannot be null");
    Validate.isTrue(type != null, "type cannot be null");
    this.index = index;
    this.namespace = namespace;
    this.name = name;
    this.type = type;
  }

  @Index
  public int getIndex() {
    return index;
  }

  @Nullable
  public String getNamespace() {
    return namespace;
  }

  @NonNull
  public String getName() {
    return name;
  }

  @NonNull
  public Class<T> getType() {
    return type;
  }

  @Override
  public String toString() {
    return String.format(Locale.ROOT,
        "AttributeDecl: { id=%d namespace=\"%s\" name=\"%s\", type=\"%s\" }", index, namespace,
        name, type.getName());
  }

}
