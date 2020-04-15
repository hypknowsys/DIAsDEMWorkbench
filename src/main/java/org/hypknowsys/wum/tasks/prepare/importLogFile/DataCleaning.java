/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.tasks.prepare.importLogFile;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;

/**
 * This class contains code necessary to perform the data cleaning of an 
 * AccessLogLine, child of WUMoodb to access class attributes, manages three 
 * text files in the local directory of the corresponding Database containing 
 * the ExcludeList, the IncludeList and the ReplaceList <p>
 *
 * data cleaning currently means substring comparisons and replacements
 * in the following string: CurrentLogLine.getHost() + " " + 
 * CurrentLogLine.getToPage() + " " + CurrentLogLine.getStatus() <p>
 *
 * In order to import a certain AccessLogLine at least one substring of 
 * IncludeList must be contained in this string and all substrings of 
 * ExcludeList must not be contained in this string. In this String a 
 * replacement of the substrings contained in ReplaceList is performed.
 *
 * @version 0.9.0, 20 May 2004
 * @author Karsten Winkler
 */

public class DataCleaning {

  // ########## attributes and constants ##########
  
  private MiningBase CurrentMiningBase = null;
  private AccessLogLine CurrentLogLine = null;

  private String[] IncludeList = null;  
  private String[] ReplaceFrom = null;
  private String[] ReplaceTo = null;  
  private String[] ExcludeList = null;

  private String LocalHostToPageCode = null;
  private Itemizer MyItemizer = null;
  
  private final static String EXCLUDE_LIST_FILE = 
    "WUM.ImportExclude.txt";
  private final static String INCLUDE_LIST_FILE = 
    "WUM.ImportInclude.txt";
  private final static String REPLACE_LIST_FILE = 
    "WUM.ImportReplace.txt";

  /**
   * constructs a new instance of this code class and retrieves
   * IncludeList, ExcludeList and ReplaceList from the corresponding
   * text files
   */

  public DataCleaning(MiningBase pCurrentMiningBase) {
  
    CurrentMiningBase = pCurrentMiningBase;
    MyItemizer = new Itemizer("Dummy String");
    String item = null;
    
    // retrieve exclude list from corresponding file
    File file = new File ( CurrentMiningBase.getMiningBaseDirectory() + 
      EXCLUDE_LIST_FILE );
    if ( file.exists() ) {
      TextFile textFile = new TextFile(file);
      textFile.open();
      Vector excludeList = new Vector();
      String excludeListLine = textFile.getFirstLine();
      while (excludeListLine != null) {
        MyItemizer.setNewItemString(excludeListLine);
        item = MyItemizer.getNextItemOrNull();
        while (item != null) {
          excludeList.add( item.trim() );
          item = MyItemizer.getNextItemOrNull();          
        }
        excludeListLine = textFile.getNextLine(); 
      }
      textFile.close();
      if ( excludeList.size() > 0) {
        ExcludeList = new String[ excludeList.size() ];
        for (int i = 0; i < ExcludeList.length; i++)
          ExcludeList[i] = (String)excludeList.elementAt(i);  
      }
      else
        ExcludeList = null;
    }
    else
      ExcludeList = null;
    
    // retrieve include list from corresponding file
    file = new File ( CurrentMiningBase.getMiningBaseDirectory() + 
      INCLUDE_LIST_FILE );
    if ( file.exists() ) {
      TextFile textFile = new TextFile(file);
      textFile.open();
      Vector includeList = new Vector();
      String validDirLine = textFile.getFirstLine();
      while (validDirLine != null) {
        MyItemizer.setNewItemString(validDirLine);
        item = MyItemizer.getNextItemOrNull();
        while (item != null) {
          includeList.add( item.trim() );
          item = MyItemizer.getNextItemOrNull();                    
        }
        validDirLine = textFile.getNextLine(); 
      }
      textFile.close();
      IncludeList = new String[ includeList.size() ];
      if ( includeList.size() > 0) {
        IncludeList = new String[ includeList.size() ];
        for (int i = 0; i < IncludeList.length; i++)
          IncludeList[i] = (String)includeList.elementAt(i);  
      }
      else
        IncludeList = null;
    }
    else           
      IncludeList = null;
  
    // retrieve replace list from corresponding file
    file = new File ( CurrentMiningBase.getMiningBaseDirectory() +  
      REPLACE_LIST_FILE );
    if ( file.exists() ) {
      TextFile textFile = new TextFile(file);
      textFile.open();
      Vector replaceFrom = new Vector();
      Vector replaceTo = new Vector();
      String itemFrom = null;
      String itemTo = null;
      String vReplacementsLine = textFile.getFirstLine();
      while (vReplacementsLine != null) {
        MyItemizer.setNewItemString(vReplacementsLine);
        itemFrom = MyItemizer.getNextItemOrNull();
        if (itemFrom != null) {
          itemTo = MyItemizer.getNextItemOrNull();
          if (itemTo != null) {
            replaceFrom.add( itemFrom.trim() );
            replaceTo.add( itemTo.trim() );
          }
        }
        vReplacementsLine = textFile.getNextLine(); 
      }
      textFile.close();
      if ( replaceFrom.size() > 0) {
        ReplaceFrom = new String[ replaceFrom.size() ];
        for (int i = 0; i < ReplaceFrom.length; i++)
          ReplaceFrom[i] = (String)replaceFrom.elementAt(i);  
      }
      else
        ReplaceFrom = null;
      if ( replaceTo.size() > 0) {
        ReplaceTo = new String[ replaceTo.size() ];
        for (int i = 0; i < ReplaceTo.length; i++)
          ReplaceTo[i] = (String)replaceTo.elementAt(i);  
      }
      else
        ReplaceTo = null;
      
    }
    else {            
      ReplaceFrom = null;   
      ReplaceTo = null;
    }    
  
  }
  
  /**
   * @return a String representation of IncludeList or a proposal "201" for a
   * new IncludeList if there is no IncludeList
   */
  
  public String getIncludeList() {
  
    StringBuffer result = new StringBuffer("");
    if (IncludeList != null)
      for (int i = 0; i < IncludeList.length; i++) 
        result.append( IncludeList[i] ).append("\n");
    else
        result.append(" ");
          
    return result.toString();
  
  }  // getIncludeList()
  
  /**
   * @return a String representation of ExcludeList or a proposal 
   * ".gif\n.jpg\n.jpeg" for a new ExcludeList if there is no ExcludeList
   */
  
  public String getExcludeList() {
  
    StringBuffer result = new StringBuffer("");;
    if (ExcludeList != null)
      for (int i = 0; i < ExcludeList.length; i++) 
        result.append( ExcludeList[i] ).append("\n");
    else
      result.append(".gif\n.jpg\n.jpeg");  // proposal
      
    return result.toString();
  
  }  // getExcludeList()
    
  /**
   * @return a String representation of ReplaceList or "" 
   * if there is no ExcludeList
   */
  
  public String getReplaceList() {
  
    StringBuffer result = new StringBuffer("");
    if ( (ReplaceFrom != null) && (ReplaceTo != null) )
      for (int i = 0; i < ReplaceFrom.length; i++) 
        result.append( ReplaceFrom[i] ).append(" ")
          .append( ReplaceTo[i] ).append("\n");
    else
        result.append(" ");
      
    return result.toString();
  
  }  // getReplaceList()
  
  /**
   * initializes the Array IncludeList with the contents of the given
   * string, items must be separated by blank spaces or "\n"
   * @param pIncludeList String containing Items of IncludeList
   */
  
  public void setIncludeList(String pIncludeList) {
  
    Itemizer itemizer = null;
    String item = null;
    
    File file = new File ( CurrentMiningBase.getMiningBaseDirectory() + 
      INCLUDE_LIST_FILE );
    TextFile textFile = new TextFile(file);
    textFile.empty();
    textFile.open();
    
    Vector includeList = new Vector();
    itemizer = new Itemizer(pIncludeList);
    item = itemizer.getNextItemOrNull();
    while (item != null) {      
      includeList.add( item.trim() );
      textFile.setNextLine(item);
      item = itemizer.getNextItemOrNull();
    }
    
    textFile.close();    
    
    if ( includeList.size() > 0) {
      IncludeList = new String[ includeList.size() ];
      for (int i = 0; i < IncludeList.length; i++) 
        IncludeList[i] = (String)includeList.elementAt(i);
    }
    else
      IncludeList = null;
  
  }  // setIncludeList()
  
  /**
   * initializes the Array ExcludeList with the contents of the given
   * string, items must be separated by blank spaces or "\n"
   * @param pExcludeList String containing Items of ExcludeList
   */
  
  public void setExcludeList(String pExcludeList) {
  
    Itemizer itemizer = null;
    String item = null;
    
    File file = new File ( CurrentMiningBase.getMiningBaseDirectory() + 
      EXCLUDE_LIST_FILE );
    TextFile textFile = new TextFile(file);
    textFile.empty();
    textFile.open();
    
    Vector excludeList = new Vector();
    itemizer = new Itemizer(pExcludeList);
    item = itemizer.getNextItemOrNull();
    while (item != null) {      
      excludeList.add( item.trim() );
      textFile.setNextLine(item);
      item = itemizer.getNextItemOrNull();
    }
    
    textFile.close();    
    
    if ( excludeList.size() > 0) {
      ExcludeList = new String[ excludeList.size() ];
      for (int i = 0; i < ExcludeList.length; i++) 
        ExcludeList[i] = (String)excludeList.elementAt(i);
    }
    else
      ExcludeList = null;
  
  }  // setExcludeList()
    
  /**
   * initializes the Arrays ReplaceFrom and ReplaceTo with the contents 
   * of the given string, each pair of items must be separated by "\n";
   * the first item is the substring to be replaced by the second item
   * @param pReplaceList String containing Items of ReplaceList
   */
  
  public void setReplaceList(String pReplaceList) {
  
    Itemizer itemizer = null;
    String item = null;
    
    File file = new File ( CurrentMiningBase.getMiningBaseDirectory() + 
      REPLACE_LIST_FILE );
    TextFile textFile = new TextFile(file);
    textFile.empty();
    textFile.open();
    
    itemizer = new Itemizer(pReplaceList);
    Vector replaceFrom = new Vector();
    Vector replaceTo = new Vector();
    String itemFrom = null;
    String itemTo = null;
    itemFrom = itemizer.getNextItemOrNull();
    while (itemFrom != null) {      
      itemTo = itemizer.getNextItemOrNull();
      if (itemTo != null) {
        replaceFrom.add( itemFrom.trim() );
        replaceTo.add( itemTo.trim() );
        textFile.setNextLine(itemFrom + " " + itemTo);
      }      
      itemFrom = itemizer.getNextItemOrNull();
    }
    
    textFile.close();    
    
    if ( replaceFrom.size() > 0) {
      ReplaceFrom = new String[ replaceFrom.size() ];
      for (int i = 0; i < ReplaceFrom.length; i++) 
        ReplaceFrom[i] = (String)replaceFrom.elementAt(i);  
    }
    else
      ReplaceFrom = null;
    if ( replaceTo.size() > 0) {
      ReplaceTo = new String[ replaceTo.size() ];
      for (int i = 0; i < ReplaceTo.length; i++) 
        ReplaceTo[i] = (String)replaceTo.elementAt(i);  
    }
    else
      ReplaceTo = null;
  
 }  // setReplaceList()  
      
  /**
   * checks whether or not the given pLogLine can be imported;
   * marks the given AccessLogLine 'valid' or 'not valid'
   * @param pLogLine AccessLogLine to check for validity
   * @return same AccessLogLine, has to be tested using isValid()
   */
  
  public AccessLogLine checkAccessLogLine(AccessLogLine pLogLine) {

    CurrentLogLine = pLogLine;
    LocalHostToPageCode = CurrentLogLine.getHost() + " " +
      CurrentLogLine.getToPage() + " " + CurrentLogLine.getStatus();
    
    if ( matchesIncludeList(LocalHostToPageCode) ) {
    
      if ( ! matchesExcludeList(LocalHostToPageCode) ) {
        
        int vReplaceID = isToReplace( CurrentLogLine.getToPage() );
        while ( vReplaceID >= 0 ) {
          CurrentLogLine.setToPage( 
            replace(CurrentLogLine.getToPage(), vReplaceID) );
          vReplaceID = isToReplace( CurrentLogLine.getToPage() );
        }
            
      }
      else
        CurrentLogLine.setValid(false);
        
    }
    else
      CurrentLogLine.setValid(false);
      
    return CurrentLogLine;
 
  }  // checkAccessLogLine
    
  /**
   * @param pLine String to be tested
   * @return true if at least one item of IncludeList is contained in pLine
   * otherwise false
   */
  
  private boolean matchesIncludeList(String pLine) {
  
    if (IncludeList != null) {
      for (int i = 0; i < IncludeList.length; i++)
        if ( pLine.indexOf( IncludeList[i] ) >= 0) 
          return true;
      return false;
    }
    else      
      return true;
  
  }  // isValidDir()
  
  /**
   * @param pLine String to be tested
   * @return false if any item of ExcludeList is contained in pLine, 
   * otherwise true
   */
  
  private boolean matchesExcludeList(String pLine) {
  
    if (ExcludeList != null) {
      for (int i = 0; i < ExcludeList.length; i++)
        if ( pLine.indexOf( ExcludeList[i] ) >= 0) 
          return true;
      return false;
    }
    else
      return false;
    
  }  // isValidLine()
  
  /**
   * tests whether or not any item of ReplaceFrom is contained in pLine
   * @param pLine String to be tested
   * @return -1 if nothing is to be replaced or the index in ReplaceFrom
   * of the substring to be replaced 
   */
  
  private int isToReplace(String pLine) {
  
    if ( (ReplaceFrom != null) && (ReplaceTo != null) ) {
      for (int i = 0; i < ReplaceFrom.length; i++)
        if ( pLine.indexOf( ReplaceFrom[i] ) >= 0) return i;
      return -1;
    }
    else    
      return -1;
  
  }  // isToReplace()
  
  /**
   * @param pLine String to be changed
   * @param pReplaceID index in ReplaceFrom of the substring to be replaced 
   * @return pLine with the substring ReplaceFrom.at(pReplaceID) replaced
   * by ReplaceTo.at(pReplaceID)
   */
  
  private String replace(String pLine, int pReplaceID) {
  
    int vIndexOf = pLine.indexOf( ReplaceFrom[pReplaceID] );
    String pNewLine = pLine.substring(0, vIndexOf) + ReplaceTo[pReplaceID] +
      pLine.substring( vIndexOf + ReplaceFrom[pReplaceID].length() );      
   
    return pNewLine;
  
  }
    
}