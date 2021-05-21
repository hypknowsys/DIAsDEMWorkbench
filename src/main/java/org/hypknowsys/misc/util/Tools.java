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
import java.util.regex.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.hypknowsys.misc.io.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class Tools {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static transient Integer TmpInteger = null;
  public static transient Long TmpLong = null;
  public static transient Boolean TmpBoolean = null;
  public static transient Double TmpDouble = null;
  public static transient String TmpString = null;
  public static transient Pattern TmpRegexPattern = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public Tools() {}

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

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String getSystemDate() { 

    return (new Date()).toString(); 

  }

  /* ########## ########## ########## ########## ########## ######### */

  public static String getFileSeparator() { 

    return System.getProperty("file.separator"); 

  }

  /* ########## ########## ########## ########## ########## ######### */

  public static String getLineSeparator() { 

    return System.getProperty("line.separator"); 

  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isInt(String pTestString) {
  
    TmpInteger = null;
    try {
      TmpInteger = new Integer(pTestString);
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;  

  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isDouble(String pTestString) {
  
    TmpDouble = null;
    try {
      TmpDouble = new Double(pTestString);
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;  

  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isLong(String pTestString) {
  
    TmpLong = null;
    try {
      TmpLong = new Long(pTestString);
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;  

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isBoolean(String pTestString) {
  
   if (pTestString.toLowerCase().startsWith("t")
   || pTestString.toLowerCase().startsWith("y")
   || pTestString.startsWith("1")
   || pTestString.toLowerCase().startsWith("j")
   || pTestString.toLowerCase().startsWith("f")
   || pTestString.toLowerCase().startsWith("n")
   || pTestString.startsWith("0")) {
     return true;
   }
   else {
     return false;  
   }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public static int string2Int(String pIntString) {

    TmpInteger = null;
    try {
      TmpInteger = new Integer(pIntString);
    }
    catch (NumberFormatException e) {
      return 0;
    }
    return TmpInteger.intValue();  

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public static String int2String(int pInt) {

   return Integer.toString(pInt); 

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public static long string2Long(String pLongString) {

    TmpLong = null;
    try {
      TmpLong = new Long(pLongString);
    }
    catch (NumberFormatException e) {
      return 0L;
    }
    return TmpLong.longValue();  

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public static String long2String(long pLong) {

   return Long.toString(pLong); 

  }

  /* ########## ########## ########## ########## ########## ######### */

  public static double string2Double(String pDoubleString) {

    TmpDouble = null;
    try {
      TmpDouble = new Double(pDoubleString);
    }
    catch (NumberFormatException e) {
      return 0.0d;
    }
    return TmpDouble.doubleValue();  

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public static String double2String(double pDouble) {

   return Double.toString(pDouble); 

  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean string2Boolean(String pBooleanString) {

   if (pBooleanString.toLowerCase().startsWith("t")
   || pBooleanString.toLowerCase().startsWith("y")
   || pBooleanString.startsWith("1")
   || pBooleanString.toLowerCase().startsWith("j")) {
     return true;
   }
   else {
     return false;  
   }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public static String boolean2String(boolean pBoolean) {

   return Boolean.toString(pBoolean); 

  } 

  /* ########## ########## ########## ########## ########## ######### */

   public static void printPrompt(String pPrompt) {  

     System.out.print(pPrompt + " ");
     System.out.flush();

   }

   /* ########## ########## ########## ########## ########## ######### */
   
   public static String readString() {
     
     int ch;
     String r = "";
     boolean done = false;
     while (!done) {
       try {
         ch = System.in.read();
         if (ch < 0 || (char)ch == '\n')
           done = true;
         else
           r = r + (char) ch;
       }
       catch(java.io.IOException e) {
         done = true;
       }
     }
     return r;
     
   }
   
  /* ########## ########## ########## ########## ########## ######### */

   public static String readString(String prompt) { 

      printPrompt(prompt);
      return readString();

   }

  /* ########## ########## ########## ########## ########## ######### */

   public static String readWord() {
 
     int ch;
     String r = "";
     boolean done = false;
     while (!done) {
       try {
         ch = System.in.read();
         if (ch < 0 || java.lang.Character.isSpace((char)ch))
           done = true;
         else
           r = r + (char) ch;
       }
       catch(java.io.IOException e) {
         done = true;
       }
    }
    return r;

   }

  /* ########## ########## ########## ########## ########## ######### */

   public static int readInt(String prompt) {

     while(true) {
       printPrompt(prompt);
       try {
         return Integer.valueOf(readString().trim()).intValue();
       } 
       catch(NumberFormatException e) {
         System.out.println("Error: Please enter an Integer value! ");
       }
     }

   }

  /* ########## ########## ########## ########## ########## ######### */

   public static long readLong(String prompt) {

     while(true) {  

       printPrompt(prompt);
       try {
         return Long.valueOf(readString().trim()).longValue();
       } 
       catch(NumberFormatException e) {  
         System.out.println("Error: Please enter a Long value! ");
       }
     }

   }

  /* ########## ########## ########## ########## ########## ######### */

   public static double readDouble(String prompt) {  

     while(true) {
       printPrompt(prompt);
       try {
         return Double.valueOf(readString().trim()).doubleValue();
       } 
       catch(NumberFormatException e) {
         System.out.println("Error: Please enter a Double value! ");
       }
     }
   }

  /* ########## ########## ########## ########## ########## ######### */

   public static String shortenFileName(String pFileName, int pMaxLength) {  

     String result = null;
     if (pFileName.length() <= pMaxLength)
       result = pFileName;
     else {
       int removeIndexStart = 10;
       int removeIndexStop = pFileName.length() - pMaxLength 
         + removeIndexStart + 3;
       if (removeIndexStart > pFileName.length() 
       || removeIndexStop > pFileName.length() )
         result = pFileName;
       else
         result = pFileName.substring(0, removeIndexStart) + "..."
           + pFileName.substring(removeIndexStop);   
     }
     return result; 
     
  }
   
  /* ########## ########## ########## ########## ########## ######### */

  public static String ensureTrailingSlash(String pDirectory) {
    
    if (pDirectory.endsWith(File.separator))
        return pDirectory;
      else
        return pDirectory + File.separator;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

   public static String getFileExtension(String pFileName) {  

     String result = "";
     int index = pFileName.lastIndexOf('.');
     if (index >= 0)
       result = pFileName.substring(index);

     return result; 
     
  }
   
  /* ########## ########## ########## ########## ########## ######### */

   public static String removeFileExtension(String pFileName) {  

     String result = pFileName;
     int index = pFileName.lastIndexOf('.');
     if (index >= 0)
       result = pFileName.substring(0, index);

     return result; 
     
  }
   
  /* ########## ########## ########## ########## ########## ######### */

   public static String replaceFileExtension(String pFileName, 
   String pNewFileExtension) {  

     return Tools.removeFileExtension(pFileName) + pNewFileExtension; 
     
  }
   
  /* ########## ########## ########## ########## ########## ######### */

   public static String removeDirectory(String pAbsoluteFileName) {  

     String result = pAbsoluteFileName;
     int index = pAbsoluteFileName.lastIndexOf( Tools.getFileSeparator() );
     if (index >= 0 && ( (index + 1) <= pAbsoluteFileName.length() ) )
       result = pAbsoluteFileName.substring(index + 1);

     return result; 
     
  }
   
  /* ########## ########## ########## ########## ########## ######### */

   public static String extractDirectory(String pAbsoluteFileName) {  

     String result = pAbsoluteFileName;
     int index = pAbsoluteFileName.lastIndexOf( Tools.getFileSeparator() );
     if (index >= 0 && ( (index + 1) <= pAbsoluteFileName.length() ) )
       result = pAbsoluteFileName.substring(0, index + 1);

     return result; 
     
  }
   
  /* ########## ########## ########## ########## ########## ######### */

   public static String removeQuotesAndNewLines(String pText) {
     
     if (pText != null) {
       return pText.replace('\"', ' ').replace('\n', ' ');
     }
     else {
       return pText;
     }
     
  }
   
  /* ########## ########## ########## ########## ########## ######### */

   public static String replaceDoubleQuotesWithSingleQuotes(String pText) {
     
     if (pText != null) {
       return pText.replace('\"', '\'');
     }
     else {
       return pText;
     }
     
  }
   
  /* ########## ########## ########## ########## ########## ######### */

  public static String lpad(String pString, int pMaxNumber) {

    String maxString = pMaxNumber + "";
    StringBuffer buffer = new StringBuffer( maxString.length() );
    if ( pString.length() >= maxString.length() ) {
      return pString.substring( 0, maxString.length() );
    }
    else {
      for (int i = 0; i < ( maxString.length() - pString.length() ); i++)
        buffer.append(" ");
      buffer.append(pString);
      return buffer.toString();
    }

  }  // lpad()
  
  /* ########## ########## ########## ########## ########## ######### */

  public static String fixStringSize(String pString, int pSize, String pAlign) {

    StringBuffer vBuffer = new StringBuffer(pSize);
    if (pString.length() >= pSize) 
      return pString.substring(0, pSize);
    else {
      if (pAlign.equals("left") ) {
        vBuffer.append(pString);
        for (int i = 0; i < ( pSize - pString.length() ); i++)
          vBuffer.append(" ");
        }
      else {
        for (int i = 0; i < ( pSize - pString.length() ); i++)
          vBuffer.append(" ");
          vBuffer.append(pString);
        }
        return vBuffer.toString();
      } 

  }  // fixStringSize()

  /* ########## ########## ########## ########## ########## ######### */

  public static String createAsciiAttributeName(String pAttributeName, 
  int pMaxSize, String pPrefix, String pSuffix) {

    return Tools.createAsciiAttributeName(pAttributeName, pMaxSize,
    pPrefix, pSuffix, "");
    
  }  // createAsciiAttributeName()

  /* ########## ########## ########## ########## ########## ######### */

  public static String createAsciiAttributeName(String pAttributeName, 
  int pMaxSize, String pPrefix, String pSuffix, 
  String pAdditionalValidChars) {

    String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    + "1234567890_" + pAdditionalValidChars;
    StringBuffer result = new StringBuffer(pAttributeName.length());
    int prefixLength = (pPrefix == null ? 0 : pPrefix.length());
    int suffixLength = (pSuffix == null ? 0 : pSuffix.length());
    String prefix = (pPrefix == null ? "" : pPrefix);
    String suffix = (pSuffix == null ? "" : pSuffix);
    int attributeNameLength = Math.min(pMaxSize - prefixLength - suffixLength,
    pAttributeName.length());
    if (attributeNameLength <= 0) {
      result.append(prefix).append(suffix);
      return result.substring(0, Math.min(pMaxSize, pAttributeName.length()));
    }
    else {
      result.append(prefix);
      for (int i = 0; i < attributeNameLength; i++) {
        if (validChars.indexOf( pAttributeName.charAt(i) ) >= 0)
          result.append( pAttributeName.charAt(i) );
        if  (pAttributeName.charAt(i) == 'ü')
          result.append( (result.toString().length() + suffixLength + 2 
          <= pMaxSize ? "ue" : "u") );
        if  (pAttributeName.charAt(i) == 'ö')
          result.append( (result.toString().length() + suffixLength + 2 
          <= pMaxSize ? "oe" : "o") );
        if  (pAttributeName.charAt(i) == 'ä')
          result.append( (result.toString().length() + suffixLength + 2 
          <= pMaxSize ? "ae" : "a") );
        if  (pAttributeName.charAt(i) == 'Ü')
          result.append( (result.toString().length() + suffixLength + 2 
          <= pMaxSize ? "Ue" : "U") );
        if  (pAttributeName.charAt(i) == 'Ö')
          result.append( (result.toString().length() + suffixLength + 2 
          <= pMaxSize ? "Oe" : "O") );
        if  (pAttributeName.charAt(i) == 'Ä')
          result.append( (result.toString().length() + suffixLength + 2 
          <= pMaxSize ? "Ae" : "A") );
        if  (pAttributeName.charAt(i) == 'ß')
          result.append( (result.toString().length() + suffixLength + 2 
          <= pMaxSize ? "sz" : "s") );
        if (result.toString().length() + suffixLength > pMaxSize)
          break;
        // System.out.println(pAttributeName + " ... " + result.toString());
      }
      result.append(suffix);
      // System.out.println(pAttributeName + " = " + result.toString());
      
      return result.toString();
    }
    
  }  // createAsciiAttributeName()

  /* ########## ########## ########## ########## ########## ######### */

  public static String insertHtmlEntityReferences(String pSource) {

    StringBuffer result = new StringBuffer(pSource.length() + 1000);
    for (int i = 0; i < pSource.length(); i++)
      switch ( pSource.charAt(i) ) {
        // & must be replaced first, because it is part of entities
        case '&': { result.append("&amp;"); break; }
        case 'ü': { result.append("&uuml;"); break; }
        case 'Ü': { result.append("&Uuml;"); break; }
        case 'ö': { result.append("&ouml;"); break; }
        case 'Ö': { result.append("&Ouml;"); break; }
        case 'ä': { result.append("&auml;"); break; }
        case 'Ä': { result.append("&Auml;"); break; }
        case 'ß': { result.append("&szlig;"); break; }
        case '<': { result.append("&lt;"); break; }
        case '>': { result.append("&gt;"); break; }
        case '\"': { result.append("&quot;"); break; }
        case '\'': { result.append("&apos;"); break; }
        case '`': { result.append("&apos;"); break; }
        default: result.append( pSource.charAt(i) );
      }
      
    return result.toString();
   
  }  // insertHtmlEntityReferences()

  /* ########## ########## ########## ########## ########## ######### */

  public static String replaceNewlineWithHtmlBrTag(String pSource) {

    StringBuffer result = new StringBuffer(pSource.length() + 1000);
    for (int i = 0; i < pSource.length(); i++)
      switch ( pSource.charAt(i) ) {
        case '\n': { result.append("<br>"); break; }
        default: result.append( pSource.charAt(i) );
      }
      
    return result.toString();
   
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static String insertUTF8EntityReferences(String pSource) {

    StringBuffer result = new StringBuffer( pSource.length() + 1000);
    for (int i = 0; i < pSource.length(); i++)
      switch ( pSource.charAt(i) ) {
        // & must be replaced first, because it is part of entities
        case '&': { result.append("&amp;"); break; }
        case '<': { result.append("&lt;"); break; }
        case '>': { result.append("&gt;"); break; }
        case '\"': { result.append("&quot;"); break; }
        case '\'': { result.append("&apos;"); break; }
        case '`': { result.append("&apos;"); break; }
        default: result.append( pSource.charAt(i) );
      }
      
    return result.toString();
   
  }  // insertHtmlEntityReferences()

  /* ########## ########## ########## ########## ########## ######### */

  public static String escapeRegexSpecialChars(String pSource) {

    StringBuffer result = new StringBuffer( pSource.length() + 1000);
    for (int i = 0; i < pSource.length(); i++)
      switch ( pSource.charAt(i) ) {
        case ' ': { result.append("\\ "); break; }
        case '.': { result.append("\\."); break; }
        case '(': { result.append("\\("); break; }
        case ')': { result.append("\\)"); break; }
        case '[': { result.append("\\["); break; }
        case ']': { result.append("\\]"); break; }
        case '\'': { result.append("\\\\"); break; }
        case '/': { result.append("\\/"); break; }
        case '-': { result.append("\\-"); break; }
        case '*': { result.append("\\*"); break; }
        case '+': { result.append("\\+"); break; }
        case '?': { result.append("\\?"); break; }
        default: result.append( pSource.charAt(i) );
      }
      
    return result.toString();
   
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static String removeSgmlHtmlXmlMarkup(String pSource,
  boolean pInsertBlankSpaceInsteadOfMarkup) {

    StringBuffer result = new StringBuffer(pSource.length() + 1000);
    boolean skipChars = false;
    for (int i = 0; i < pSource.length(); i++) {
      if (pSource.charAt(i) == '<') {
        skipChars = true;
      }
      if (!skipChars) {
        result.append( pSource.charAt(i) );
      }
      if (pSource.charAt(i) == '>') {
        skipChars = false;
        if (pInsertBlankSpaceInsteadOfMarkup) {
          result.append(" ");
        }
      }
    }
      
    return result.toString();
   
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static String wrapString(String pString, int pMaxLineLength) {

    StringTokenizer tokenizer = new StringTokenizer(pString);
    int lineLength = 0;
    StringBuffer result = new StringBuffer(pString.length() + 100);
    String token = null;
    
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      if (lineLength + token.length() > pMaxLineLength) {
        result.append("\n");
        lineLength = 0;
      }
      else {
        if (lineLength > 0) { 
          result.append(" ");
          lineLength++;
        }
      }
      result.append(token);
      lineLength += token.length();
    }

    return result.toString();

  }

  /* ########## ########## ########## ########## ########## ######### */

  public static String ensureMaxLength(String pString, int pMaxLength) {

    if (pString == null) {
      return null;
    }
    else if (pString.length() >= pMaxLength) {
      return pString.substring(0, pMaxLength);
    }
    else {
      return pString;
    }

  } 
  
  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isValidandWriteableFileOrDirectoryName(String pFileName) {
    
    if (pFileName == null) {
      return false;
    }
      
    boolean result = false;
    File file = new File(pFileName);
    if (file.exists())
      result = true; 
    else
      try {
        file.createNewFile();
        file.delete();
        result = true;
      }
      catch (Exception e) {}

    return result;    
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isValidandWriteableFileOrDirectoryName(String pFileName,
  String pFileExtention) {
    
    if (pFileName == null) {
      return false;
    }
      
    if (!pFileName.endsWith(pFileExtention))
      return false;
    else
      return Tools.isValidandWriteableFileOrDirectoryName(pFileName);
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isValidandWriteableDirectoryName(String pFileName) {
    
    if (pFileName == null) {
      return false;
    }
      
    boolean result = false;
    File file = new File(Tools.ensureTrailingSlash(pFileName));
    if (file.exists() && file.isDirectory()) {
      result = true; 
    }
    else {
      try {
        file.createNewFile();
        if (file.isDirectory())
          result = true;
        file.delete();
      }
      catch (Exception e) { 
        result = false; 
      }
    }

    return result;    
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isExistingDirectory(String pFileName) {
    
    if (pFileName == null) {
      return false;
    }
      
    File file = new File(Tools.ensureTrailingSlash(pFileName));
    if (file.exists() && file.isDirectory())
      return true; 
    else
      return false;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isValidandWriteableFileName(String pFileName) {
    
    if (pFileName == null) {
      return false;
    }
      
    boolean result = false;
    File file = new File(pFileName);
    if (file.exists() && file.isFile())
      result = true; 
    else
      try {
        file.createNewFile();
        if (file.isFile())
          result = true;
        file.delete();
      }
      catch (Exception e) { result = false; }

    return result;    
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isValidandWriteableFileName(String pFileName,
  String pFileExtention) {
    
    if (pFileName == null) {
      return false;
    }
      
    if (!pFileName.endsWith(pFileExtention))
      return false;
    else
      return Tools.isValidandWriteableFileName(pFileName);
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isExistingFile(String pFileName,
  String pFileExtention) {
    
    if (pFileName == null) {
      return false;
    }
      
    if (!pFileName.endsWith(pFileExtention))
      return false;
    File file = new File(pFileName);
    if (file.exists() && file.isFile())
      return true; 
    else
      return false;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isExistingFile(String pFileName) {
    
    if (pFileName == null) {
      return false;
    }
      
    File file = new File(pFileName);
    if (file.exists() && file.isFile())
      return true; 
    else
      return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public static void requestFocus(Component pComponent) {
    
    if (pComponent != null) {
      FocusEvent lost = new FocusEvent(pComponent, 
      FocusEvent.FOCUS_LOST);
      FocusEvent gained = new FocusEvent(pComponent, 
      FocusEvent.FOCUS_GAINED);
      pComponent.dispatchEvent(lost);
      pComponent.dispatchEvent(gained);
      pComponent.dispatchEvent(lost);
      pComponent.requestFocus();
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static void requestFocus(Component pFirstComponent,
  Component pSecondComponent) {
    
    Tools.requestFocus(pFirstComponent);
    Tools.requestFocus(pSecondComponent);
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static void requestFocus(Component pFirstComponent,
  Component pSecondComponent, Component pThirdComponent) {
    
    Tools.requestFocus(pFirstComponent);
    Tools.requestFocus(pSecondComponent);
    Tools.requestFocus(pThirdComponent);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public static String getRelativeFileName(String pBaseDirectory, 
  String pFileName) {
    
   int index = pFileName.indexOf(Tools.ensureTrailingSlash(pBaseDirectory));
   if (index >= 0 && (pBaseDirectory.length() + 1) < pFileName.length()) {
     return pFileName.substring(pBaseDirectory.length() + 1);
   }
   else {
     return pFileName;
   }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static int getLengthOfLineFeedChars() {
    
   long result = 1;
   try {
     File file = File.createTempFile("Java_", ".tmp");
     TextFile text = new TextFile(file);
     text.open();
     text.setFirstLine("");
     text.close();
     result = file.length();
     file.delete();
   }
   catch (IOException e) {
     e.printStackTrace();
   }
   
   return (int)result;  
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public static StringBuffer stringBufferFromTextualSystemResource(
  String pSystemResourceName) {
    
    // Example: SystemResourceName = "org/hypknowsys/diasdem/resources/txt/CONTACT.txt"
    
    String line = null;
    boolean firstLine = true;
    try {
      BufferedReader reader = new BufferedReader( new InputStreamReader(
      ClassLoader.getSystemResourceAsStream(pSystemResourceName)));
      StringBuffer result = new StringBuffer(100000);
      line = reader.readLine();
      while (line != null) {
        if (firstLine) {
          firstLine = false;
        }
        else {
          result.append("\n");
        }
        result.append(line);
        line = reader.readLine();
      }
      return result;
    }
    catch (IOException e) {
      System.err.println("Error: System resource " + pSystemResourceName
      + " cannot be opened!");
      return new StringBuffer("");
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static String stringFromTextualSystemResource(
  String pSystemResourceName) {
    
    // Example: SystemResourceName = "org/hypknowsys/diasdem/resources/txt/CONTACT.txt"
    
    return Tools.stringBufferFromTextualSystemResource(pSystemResourceName)
    .toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static void copyTextualSystemResource(
  String pSystemResourceName, String pDestinationFileName) {
    
    // Example: SystemResourceName = "org/hypknowsys/diasdem/resources/txt/CONTACT.txt"
    
    if (!Tools.isValidandWriteableFileName(pDestinationFileName)) {
      System.err.println("Error: System resource " + pSystemResourceName
      + " cannot be copied to destination file " + pDestinationFileName + "!");
      return;
    }
    try {
      TextFile destination = new TextFile(new File(pDestinationFileName));
      destination.empty();
      destination.open();
      BufferedReader reader = new BufferedReader( new InputStreamReader(
      ClassLoader.getSystemResourceAsStream(pSystemResourceName)));
      String line = reader.readLine();
      while (line != null) {
        destination.setNextLine(line);
        line = reader.readLine();
      }
      destination.close();
    }
    catch (IOException e) {
      System.err.println("Error: System resource " + pSystemResourceName
      + " cannot be copied to destination file " + pDestinationFileName + "!");
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static StringBuffer stringBufferFromTextFileContents(
  String pTextFileName) {
    
    if (!Tools.isExistingFile(pTextFileName)) {
      return new StringBuffer("");
    }
    else {
      boolean firstLine = true;
      File file = new File(pTextFileName);
      StringBuffer result = new StringBuffer((int)file.length() + 1000);
      TextFile textFile = new TextFile(file);
      textFile.openReadOnly();
      for (String line = textFile.getFirstLine(); line != null;
      line = textFile.getNextLine()) {
        if (firstLine) {
          firstLine = false;
        }
        else {
          result.append("\n");
        }
        result.append(line);
      }
      textFile.close();
      return result;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static String stringFromTextFileContents(String pTextFileName) {
    
    return Tools.stringBufferFromTextFileContents(pTextFileName).toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static final byte[] getStringAsArrayOfByte(String pString) {
    
    int length = pString.length();
    byte[] result = new byte[length];
    pString.getBytes(0, length, result, 0);
    
    return result;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static double log(int pBase, double pX) {
    
    return Math.log(pX) / Math.log((double)pBase);
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public static boolean isSyntacticallyCorrectRegex(String pRegex) {

    TmpRegexPattern = null;
    try {
      TmpRegexPattern = Pattern.compile(pRegex);
    }
    catch (Exception e) {
      return false;
    }
    return true;  

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public static boolean stringContainsLetter(String pString) {
    
    if (pString == null || pString.length() == 0) {
      return false;
    }
    else {    
      for (int i = 0; i < pString.length(); i++) {
        if (Character.isLetter(pString.charAt(i))) {
          return true;
        }
      }
      return false;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static boolean stringIsNullOrEmpty(String pString) {
    
    if (pString == null || pString.length() == 0) {
      return true;
    }
    else {    
      return false;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String insertLineBreaks(int pMaxCharactersPerLine,
  String pString) {
    
    if (pString != null || pString.length() >= 0) {
      String rest = new String(pString);
      StringBuffer result = new StringBuffer(pString.length() + 1000);
      int breakIndex = pMaxCharactersPerLine;
      char breakChar = ' ';
      while (rest.length() > 0) {
        breakIndex = pMaxCharactersPerLine;
        if (result.toString().length() > 0) {
          result.append('\n');
        }
        if (breakIndex >= rest.length()) {
          result.append(rest);
          rest = "";
        }
        else {
          breakChar = rest.charAt(breakIndex);
          int tries = 0;
          while (!(breakChar == ' ' || breakChar == '-' || breakChar == '/'
          || breakChar == '\\' || breakChar == ':' || breakChar == '@'
          || breakChar == ',' || breakChar == ';' || breakChar == '_'
          || breakChar == '.' || breakChar == '!' || breakChar == '?'
          || breakChar == '~' || breakChar == '=' || breakChar == ')'
          || breakChar == '(' || breakChar == '&' || breakChar == '%'
          || breakChar == '$' || breakChar == '�' || breakChar == '"'
          || breakChar == '\'' || breakChar == '\t' || breakChar == '\n'
          || breakChar == '>' || breakChar == '<' || breakChar == '#')
          && tries++ < 20 && (breakIndex - 1) >= 0) {
            breakIndex--;
            breakChar = rest.charAt(breakIndex);
          }
          if (breakChar == '/' && (breakIndex - 1) >= 0
          && rest.charAt(breakIndex - 1) == '<') {
            breakIndex--;
          }
          if (tries >= 20 || breakIndex == 0) {
            breakIndex = pMaxCharactersPerLine;
          }
          result.append(rest.substring(0, breakIndex));
          rest = rest.substring(breakIndex).trim();
        }
      }
      return result.toString();
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String insertHtmlLineBreaks(int pMaxCharactersPerLine,
  String pString) {
    
    if (pString != null || pString.length() >= 0) {
      String rest = new String(pString);
      StringBuffer result = new StringBuffer(pString.length() + 1000);
      int breakIndex = pMaxCharactersPerLine;
      char breakChar = ' ';
      while (rest.length() > 0) {
        breakIndex = pMaxCharactersPerLine;
        if (result.toString().length() > 0) {
          result.append("<br>");
        }
        if (breakIndex >= rest.length()) {
          result.append(rest);
          rest = "";
        }
        else {
          breakChar = rest.charAt(breakIndex);
          int tries = 0;
          while (!(breakChar == ' ' || breakChar == '-' || breakChar == '/'
          || breakChar == '\\' || breakChar == ':' || breakChar == '@'
          || breakChar == ',' || breakChar == ';' || breakChar == '_'
          || breakChar == '.' || breakChar == '!' || breakChar == '?'
          || breakChar == '~' || breakChar == '=' || breakChar == ')'
          || breakChar == '(' || breakChar == '&' || breakChar == '%'
          || breakChar == '$' || breakChar == '�' || breakChar == '"'
          || breakChar == '\'' || breakChar == '\t' || breakChar == '\n'
          || breakChar == '>' || breakChar == '<' || breakChar == '#')
          && tries++ < 20 && (breakIndex - 1) >= 0) {
            breakIndex--;
            breakChar = rest.charAt(breakIndex);
          }
          if (tries >= 20 || breakIndex == 0) {
            breakIndex = pMaxCharactersPerLine;
          }
          result.append(rest.substring(0, breakIndex));
          rest = rest.substring(breakIndex).trim();
        }
      }
      return result.toString();
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String intArray2String(int[] pArray) {
    
    StringBuffer result = new StringBuffer(pArray.length * 15);
    result.append('[');
    for (int i = 0; i < pArray.length; i++) {
      if (i > 0) { result.append(','); }
      result.append(Integer.toString(pArray[i]));
    }
    result.append(']');
    
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static boolean isIntArray(String pString) {

    if (pString == null || pString.length() < 2 || !pString.startsWith("[")
    || !pString.endsWith("]")) {
      return false;
    }
    String tmpString = pString.substring(1, pString.length() - 1);
    if (tmpString.length() == 0) {
      return true;
    }
    else {
      String[] array = tmpString.split(",");
      for (int i = 0; i < array.length; i++) {
        if (!Tools.isInt(array[i])) {
          return false;
        }
      }
    }
    
    return true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static int[] string2IntArray(String pString) {

    int[] result = null;
    if (Tools.isIntArray(pString)) {
      String[] array = (pString.substring(1, pString.length() - 1)).split(",");
      result = new int[array.length];
      for (int i = 0; i < array.length; i++) {
        result[i] = Tools.string2Int(array[i]);
      }
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}