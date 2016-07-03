package com.gmail.collinsmith70.unifi;

/**
 * Class containing static members which are common across all modules of the Unifi platform.
 */
public class Unifi {

  /**
   * Enforces the uninstantiability of {@link Unifi}.
   */
  private Unifi() {
  }
  
  /**
   * Default Unifi namespace.
   */
  public static final String NAMESPACE = "http://schemas.unifi.com/res/unifi";

  /**
   * Default Unifi package.
   */
  public static final String PACKAGE = "unifi";
  
}
