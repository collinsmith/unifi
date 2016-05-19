package com.gmail.collinsmith70.unifi.content.res;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

public class ResourceReferenceTest {

  @Test
  public void testParse_Color() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@unifi:color/white");
    assertTrue(ref.isStyleAttributeReference() == false);
    assertTrue(ref.getPackageName().equals("unifi"));
    assertTrue(ref.getResourceType() == ResourceReference.Type.color);
    assertTrue(ref.getResourceName().equals("white"));
    ref.recycle();
    ref = ResourceReference.parse("@color/white");
    assertTrue(ref.isStyleAttributeReference() == false);
    assertTrue(ref.getPackageName() == null);
    assertTrue(ref.getResourceType() == ResourceReference.Type.color);
    assertTrue(ref.getResourceName().equals("white"));
    ref.recycle();
  }

  @Test
  public void testParse_String() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@unifi:string/test");
    assertTrue(ref.isStyleAttributeReference() == false);
    assertTrue(ref.getPackageName().equals("unifi"));
    assertTrue(ref.getResourceType() == ResourceReference.Type.string);
    assertTrue(ref.getResourceName().equals("test"));
    ref.recycle();
    ref = ResourceReference.parse("@string/test");
    assertTrue(ref.isStyleAttributeReference() == false);
    assertTrue(ref.getPackageName() == null);
    assertTrue(ref.getResourceType() == ResourceReference.Type.string);
    assertTrue(ref.getResourceName().equals("test"));
    ref.recycle();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParse_null() throws ParseException {
    ResourceReference ref = ResourceReference.parse(null);
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_empty() throws ParseException {
    ResourceReference ref = ResourceReference.parse("");
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_missingReferenceTypeWithPackageTypeName() throws ParseException {
    ResourceReference ref = ResourceReference.parse("package:type/name");
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_missingReferenceTypeWithTypeName() throws ParseException {
    ResourceReference ref = ResourceReference.parse("type/name");
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_missingPackageWithTypeName() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@:type/name");
    ref.recycle();
  }

  @Test(expected = ParseException.class)
  public void testParse_missingTypeWithoutPackage() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@/name");
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_missingTypeAfterPackage() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@package:/name");
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_missingNameAfterPackageType() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@package:type/");
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_missingNameAfterPackageTypeWithoutSlash() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@package:type");
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_missingNameAfterType() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@type/");
    ref.recycle();
  }
  
  @Test(expected = ParseException.class)
  public void testParse_missingNameAfterTypeWithoutSlash() throws ParseException {
    ResourceReference ref = ResourceReference.parse("@type");
    ref.recycle();
  }
  
}
