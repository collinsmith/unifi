package com.gmail.collinsmith70.unifi.content.res;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class AttributeDeclTest {

  @Test(expected = IllegalArgumentException.class)
  public void testAttributeDecl_invalidIndex() {
    new AttributeDecl<String>(-1, "", "", String.class);
  }

  @Test
  public void testAttributeDecl_getIndex() {
    int[] data = {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
        43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
    };
    
    for (int point : data) {
      AttributeDecl<String> decl = new AttributeDecl<String>(point, "", "", String.class);
      assertTrue(decl.getIndex() == point);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAttributeDecl_nullNamespace() {
    new AttributeDecl<String>(0, null, "", String.class);
  }

  @Test
  public void testAttributeDecl_getNamespace() {
    String[] data = {
        "foo", "bar", "unifi", "android"
    };
    
    for (String point : data) {
      AttributeDecl<String> decl = new AttributeDecl<String>(0, point, "", String.class);
      assertTrue(decl.getNamespace().equals(point));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAttributeDecl_nullName() {
    new AttributeDecl<String>(0, "", null, String.class);
  }

  @Test
  public void testAttributeDecl_getName() {
    String[] data = {
        "foo", "bar", "unifi", "android"
    };
    
    for (String point : data) {
      AttributeDecl<String> decl = new AttributeDecl<String>(0, "", point, String.class);
      assertTrue(decl.getName().equals(point));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAttributeDecl_nullType() {
    new AttributeDecl<String>(0, "", "", null);
  }

  @Test
  public void testAttributeDecl_getType() {
    Class<?>[] data = {
        Integer.class, Byte.class, Long.class, String.class, Object.class, Date.class
    };
    
    for (Class<?> point : data) {
      @SuppressWarnings({"rawtypes", "unchecked"})
      AttributeDecl<?> decl = new AttributeDecl(0, "", "", point);
      assertTrue(decl.getType().equals(point));
    }
  }
  
}
