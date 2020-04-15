/*
 * Copyright (C) 2000-2005, Henner Graubitz, Myra Spiliopoulou, Karsten 
 * Winkler. All rights reserved.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.hypknowsys.misc.util;

import java.util.*;
import java.io.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KProperties implements Serializable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Properties MyProperties = null;
  protected Properties MyDefaultProperties = null;
  protected TreeMap KPropertyIndex = null;
  protected String FileName = null;
  
  // subclasses must define an initial array for details of properties
  protected KProperty[] PropertyDetails = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected transient StringBuffer TmpStringBuffer = null;
  protected transient String TmpString = null;
  protected transient File TmpFile = null;
  protected transient Object TmpObject = null;
  protected transient Integer TmpInteger = null;
  protected transient int TmpInt = 0;
  protected transient boolean TmpBoolean = false;
  protected transient KProperty TmpKProperty = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static KProperty[] EXAMPLE_PROPERTY_DATA = {
    new KProperty("STRING_PROPERTY", "Description of Property",
     "Default Value as a String", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("INTEGER_PROPERTY", "Description of Property",
     "1", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("LONG_PROPERTY", "Description of Property",
     "123456789123456789", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("DOUBLE_PROPERTY", "Description of Property",
     "1.0", KProperty.DOUBLE, KProperty.NOT_EDITABLE),
    new KProperty("BOOLEAN_PROPERTY", "Description of Property",
     "true", KProperty.BOOLEAN, KProperty.NOT_EDITABLE)
  };

  public final static int LOAD = 0;
  public final static int CREATE = 1;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KProperties() {

    this(null);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KProperties(KProperty[] pPropertyDetails) {

    MyDefaultProperties = new Properties(); 
    KPropertyIndex = new TreeMap();

    if (pPropertyDetails != null) {
      PropertyDetails = pPropertyDetails;
      for (int i = 0; i < PropertyDetails.length; i++) {
        KPropertyIndex.put(PropertyDetails[i].getKey(), PropertyDetails[i]);
        MyDefaultProperties.put(pPropertyDetails[i].getKey(), 
        pPropertyDetails[i].getDefaultValue());
      }
    }
    
    MyProperties = new Properties(MyDefaultProperties);
    FileName = null;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KProperties(String pFileName, KProperty[] pPropertyDetails) 
  throws IOException {

    this(pPropertyDetails);
    this.load(pFileName);

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() {

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    TmpStringBuffer.append(" [NumberOfProperties=");
    TmpStringBuffer.append(this.countProperties());
    TmpStringBuffer.append("; FileName=");
    TmpStringBuffer.append(FileName);
    TmpStringBuffer.append("]");
    
    return TmpStringBuffer.toString();

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void save(String pFileName) throws IOException {

    if (pFileName == null) {
      FileName = null;
      throw new IOException("[KProperties] Error: Property file " 
      + pFileName + " cannot be saved!");
    }
    
    try {
      FileOutputStream fileOut = new FileOutputStream(pFileName);
      MyProperties.store(fileOut, 
        "This is an automatically created file: Please do not edit " +
        "this file manually!");
      FileName = pFileName;
    }
    catch (IOException e) {
      FileName = null;
      throw new IOException("[KProperties] Error: Property file " 
      + pFileName + " cannot be saved!");
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void save() throws IOException {

    this.save(FileName);
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public void quickSave() {

    try {
      this.save(FileName);
    }
    catch (IOException e) {
      System.err.println("[KProperties] Warning: Property file " 
      + FileName + " cannot be saved!");
      System.err.flush();
      FileName = null;    
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public void load(String pFileName) throws IOException {

    if (pFileName == null) {
      MyProperties = new Properties(MyDefaultProperties);
      FileName = null;
      throw new IOException("[KProperties] Warning: Property file " +
      pFileName + " cannot be opened! Using defaults instead ...");
    }
    
    try {
      FileInputStream fileIn = new FileInputStream(pFileName);
      MyProperties.load(fileIn);
      FileName = pFileName;
    }
    catch (IOException e) {
      MyProperties = new Properties(MyDefaultProperties);
      FileName = null;
      throw new IOException("[KProperties] Warning: Property file " +
      pFileName + " cannot be opened! Using defaults instead ...");
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void quickLoad(String pFileName) {

    try {
      this.load(FileName);
    }
    catch (IOException e) {
      System.err.println("[KProperties] Warning: Property file " +
      pFileName + " cannot be opened! Using defaults instead ...");
      System.err.flush();
      FileName = null;    
    }
    
  }  // quickLoad()

  /* ########## ########## ########## ########## ########## ######### */

  public void registerButDontReplaceKProperties(KProperty[] pPropertyDetails) {

    if (pPropertyDetails != null) {
      if (MyDefaultProperties == null) {
        MyDefaultProperties = new Properties();
      }
      if (MyProperties == null) {
        MyProperties = new Properties(MyDefaultProperties);
      }
      if (KPropertyIndex == null) {
        KPropertyIndex = new TreeMap();
      }
      for (int i = 0; i < pPropertyDetails.length; i++) {
        if (KPropertyIndex.get(pPropertyDetails[i].getKey()) == null) {
          KPropertyIndex.put(pPropertyDetails[i].getKey(), pPropertyDetails[i]);
          MyDefaultProperties.put(pPropertyDetails[i].getKey(), 
          pPropertyDetails[i].getDefaultValue());
        }
      }
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public String[] getPropertyKeys() {

    int propertyCounter = 0;
    Object dummy = null;
    Enumeration propertyKeyEnum = MyProperties.propertyNames();
    while (propertyKeyEnum.hasMoreElements()) {
      dummy = propertyKeyEnum.nextElement();     
      propertyCounter++;
    }
    String[] result = new String[propertyCounter];
    propertyCounter = 0;
    propertyKeyEnum = MyProperties.propertyNames();
    while (propertyKeyEnum.hasMoreElements()) {
      result[propertyCounter++] = (String)propertyKeyEnum.nextElement();     
    }

    return result;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public String getProperty(String pPropertyKey) {

    TmpObject = MyProperties.getProperty(pPropertyKey);
    if (TmpObject != null) {
      TmpString = (String)TmpObject;
    }
    else {
      TmpString = "";
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " does not exist! Returning empty String instead ...");
      System.err.flush();
    }

    return TmpString;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public String getPropertyDescription(String pPropertyKey) {

    if (this.isRegisteredKey(pPropertyKey)) {
      TmpKProperty = (KProperty)KPropertyIndex.get(pPropertyKey);
      TmpString = TmpKProperty.getDescription();
    }
    else {
      TmpString = "";
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " is not registered! Returning empty String instead ...");
      System.err.flush();
    }

    return TmpString;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public int getPropertyType(String pPropertyKey) {

    if (this.isRegisteredKey(pPropertyKey)) {
      TmpKProperty = (KProperty)KPropertyIndex.get(pPropertyKey);
      TmpInt = TmpKProperty.getType();
    }
    else {
      TmpInt = KProperty.STRING;
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " is not registered! Returning type KProperty.STRING instead ...");
      System.err.flush();
    }

    return TmpInt;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public boolean isEditableInTable(String pPropertyKey) {

    if (this.isRegisteredKey(pPropertyKey)) {
      TmpKProperty = (KProperty)KPropertyIndex.get(pPropertyKey);
      TmpBoolean = TmpKProperty.getEditable();
    }
    else {
      TmpBoolean = KProperty.EDITABLE;
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " is not registered! Returning type KProperty.EDITABLE instead ...");
      System.err.flush();
    }

    return TmpBoolean;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public int countProperties() {

    return MyProperties.size();

  }

  /* ########## ########## ########## ########## ########## ######### */

  public String getStringProperty(String pPropertyKey) {

    return this.getProperty(pPropertyKey);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public int getIntProperty(String pPropertyKey) {

    TmpObject = MyProperties.getProperty(pPropertyKey);
    if (TmpObject != null) {
      return Tools.string2Int((String)TmpObject);
    }
    else {
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " does not exist! Returning 0 instead ...");
      System.err.flush();
      return 0;
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public long getLongProperty(String pPropertyKey) {

    TmpObject = MyProperties.getProperty(pPropertyKey);
    if (TmpObject != null) {
      return Tools.string2Long((String)TmpObject);
    }
    else {
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " does not exist! Returning 0L instead ...");
      System.err.flush();
      return 0L;
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public double getDoubleProperty(String pPropertyKey) {

    TmpObject = MyProperties.getProperty(pPropertyKey);
    if (TmpObject != null) {
      return Tools.string2Double((String)TmpObject);
    }
    else {
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " does not exist! Returning 0.0D instead ...");
      System.err.flush();
      return 0.0D;
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public boolean getBooleanProperty(String pPropertyKey) {

    TmpObject = MyProperties.getProperty(pPropertyKey);
    if (TmpObject != null) {
      return Tools.string2Boolean((String)TmpObject);
    }
    else {
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " does not exist! Returning false instead ...");
      System.err.flush();
      return false;
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public String getTimeStringProperty(String pPropertyKey) {

    TmpObject = MyProperties.getProperty(pPropertyKey);
    if (TmpObject != null) {
      return (String)TmpObject;
    }
    else {
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " does not exist! Returning \"0/00:00:00\" instead ...");
      System.err.flush();
      return "0/00:00:00";
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setProperty(String pPropertyKey, String pValue) {

    MyProperties.put(pPropertyKey, pValue);
    if (!this.isRegisteredKey(pPropertyKey)) {
      System.err.println("[KProperties] Warning: Property " + pPropertyKey 
      + " is not registered! Register properties before setting them ...");
      System.err.flush();
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setStringProperty(String pPropertyKey, String pValue) {

    this.setProperty(pPropertyKey, pValue);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setIntProperty(String pPropertyKey, int pValue) {

    this.setProperty(pPropertyKey, ( new Integer(pValue) ).toString());

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setLongProperty(String pPropertyKey, long pValue) {

    this.setProperty(pPropertyKey, ( new Long(pValue) ).toString());

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setDoubleProperty(String pPropertyKey, double pValue) {

    this.setProperty(pPropertyKey, ( new Double(pValue) ).toString());

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setBooleanProperty(String pPropertyKey, boolean pValue) {

    this.setProperty(pPropertyKey, ( new Boolean(pValue) ).toString());

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setTimeStringProperty(String pPropertyKey, String pValue) {

    this.setProperty(pPropertyKey, pValue);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public boolean isValidProperty(String pPropertyKey, String pValue) {

    boolean result = true;
    if (this.isRegisteredKey(pPropertyKey)) {
      TmpKProperty = (KProperty)KPropertyIndex.get(pPropertyKey);
      if (TmpKProperty.getType() == KProperty.INTEGER)
        result = Tools.isInt(pValue);
      else if (TmpKProperty.getType() == KProperty.LONG)
        result = Tools.isLong(pValue);
      else if (TmpKProperty.getType() == KProperty.DOUBLE)
        result = Tools.isDouble(pValue);
      else if (TmpKProperty.getType() == KProperty.BOOLEAN)
        result = Tools.isBoolean(pValue);
      else if (TmpKProperty.getType() == KProperty.TIMESTRING)
        result = TimeStamp.isValidTime(pValue);
    }
    else {
      result = false;
    }

    return result;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public boolean isRegisteredKey(String pPropertyKey) {

    boolean result = false;
    if (KPropertyIndex.get(pPropertyKey) != null) {
      result = true;
    }

    return result;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public boolean isValidKey(String pPropertyKey) {

    boolean result = false;
    if (MyDefaultProperties.get(pPropertyKey) != null) {
      result = true;
    }

    return result;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setDefaultsAndKey2KPropertyIndex() {
  
    for (int i = 0; i < PropertyDetails.length; i++) {
      MyDefaultProperties.put(PropertyDetails[i].getKey(), 
        PropertyDetails[i].getDefaultValue());
      KPropertyIndex.put(PropertyDetails[i].getKey(), new Integer(i));
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String[] args) {

    KProperties kProperties = new KProperties(
    KProperties.EXAMPLE_PROPERTY_DATA);   
    
    String stringProp = kProperties.getProperty("STRING_PROPERTY");
    stringProp = kProperties.getStringProperty("STRING_PROPERTY");
    int intProp = kProperties.getIntProperty("INTEGER_PROPERTY");
    long longProp = kProperties.getLongProperty("LONG_PROPERTY");
    double doubleProp = kProperties.getDoubleProperty("DOUBLE_PROPERTY");
    boolean booleanProp = kProperties.getBooleanProperty("BOOLEAN_PROPERTY");
    stringProp = kProperties.getProperty("NONEXISTING_PROPERTY");
    
  }  
  
}