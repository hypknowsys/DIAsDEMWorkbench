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

package org.hypknowsys.misc.io;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class Itemizer {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */


  private StringTokenizer MyStringTokenizer = null;
  private StringBuffer LocalStringBuffer = null;
  private String LocalString = null;
  private String LocalStringResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public static final String ITEM_SEPARATOR = " ";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public Itemizer(String pItemString) {
  
    MyStringTokenizer = new StringTokenizer(pItemString);
    
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
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setNewItemString(String pItemString) {
  
    MyStringTokenizer = new StringTokenizer(pItemString);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean hasMoreItems() {
  
    if (MyStringTokenizer != null) {
      return MyStringTokenizer.hasMoreTokens();
    }
    else {
      return false;
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getNextItem()
  throws NoSuchElementException {

    LocalString = MyStringTokenizer.nextToken();
    LocalStringBuffer = new StringBuffer();

    if (LocalString.startsWith("\"")) {
      // LocalString is first part of String
      LocalStringBuffer.append(LocalString);
      while (!(LocalString.endsWith("\""))) {
        LocalString = MyStringTokenizer.nextToken();
        LocalStringBuffer.append(" ").append(LocalString);
      }
    }
    else {
      // boolean, int, long or double
      LocalStringBuffer.append(LocalString);
    }

    return LocalStringBuffer.toString();

  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getNextItemOrNull() {

    if (this.hasMoreItems()) {
      return MyStringTokenizer.nextToken();
    }
    else {
      return null;
    }

  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getEndOfLine()
  throws NoSuchElementException {

    LocalStringBuffer = new StringBuffer(); 
    
    if (MyStringTokenizer.hasMoreElements()) {
      LocalStringBuffer.append(MyStringTokenizer.nextToken());
    }
    while (MyStringTokenizer.hasMoreElements()) {
      LocalStringBuffer.append(ITEM_SEPARATOR).append(
      MyStringTokenizer.nextToken());
    }

    return LocalStringBuffer.toString();

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String booleanToItem(boolean pBoolean) {

    return String.valueOf(pBoolean);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String intToItem(int pInt) {

    return String.valueOf(pInt);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String longToItem(long pLong) {

    return String.valueOf(pLong);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String doubleToItem(double pDouble) {

    return String.valueOf(pDouble);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String stringToItem(String pString) {

    return (new StringBuffer(pString.length() + 2)).append("\"")
    .append(pString).append("\"").toString();

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static boolean itemToBoolean(String pBoolean)
  throws NumberFormatException {

    if (pBoolean.equals("true")) {
      return true;
    }
    else {
      if (pBoolean.equals("false")) {
        return false;
      }
      else {
        throw new NumberFormatException();
      }
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static int itemToInt(String pInt)
  throws NumberFormatException {

    return Integer.parseInt(pInt);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static long itemToLong(String pLong)
  throws NumberFormatException {

    return Long.parseLong(pLong);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static double itemToDouble(String pDouble)
  throws NumberFormatException {

    return Double.parseDouble(pDouble);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String itemToString(String pString)
  throws StringIndexOutOfBoundsException, NumberFormatException {

    if (pString.startsWith("\"") && pString.endsWith("\"")) {
      return pString.substring(1, pString.length() - 1);
    }
    else {
      throw new NumberFormatException();
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

  public static void main(String pOptions[]) {}
  
}