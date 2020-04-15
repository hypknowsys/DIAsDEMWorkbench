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
 * includes a taxonomy hierarchy (concept hierarchy) that currently
 * supports only one level (i.e. Page.TaxonomyLevel1) of generalization;
 * each Page.Url can be allocated to exactly one item of the 1st
 * level in this taxonomy; the allocation of pages to items in the 
 * taxonomy is simply performed by substring matching; all substrings
 * and their corresponding items of the 1st taxonomy level are stored
 * in a text file according to the following format: <p>
 * "SubstringInToPage NameOfTaxonomyItem". If a Page.Url is not allocated
 * to any taxonomy item, its Page.TaxonomyLevel1 is set to Page.Url
 */

public class TaxonomyHierarchy {

  // ########## attributes and constants ##########
  
  private MiningBase CurrentMiningBase = null;
  private String CurrentUrl = null;
  private String[] AllUrlSubstrings = null;
  private String[] AllTaxonomyItems = null;  
  
  private final static String TAXONOMY_LIST_FILE = "WUM.ImportTaxonomy.txt";

  /**
   * constructs a new instance of this code class and retrieves
   * the taxonomy from the corresponding text files
   */

  public TaxonomyHierarchy(MiningBase pCurrentMiningBase) {
  
    CurrentMiningBase = pCurrentMiningBase;
    Itemizer itemizer = null;
    String item = null;
    
    // retrieve replace list from corresponding file
    File file = new File ( CurrentMiningBase.getMiningBaseDirectory() + 
      TAXONOMY_LIST_FILE );
    if ( file.exists() ) {
      TextFile textFile = new TextFile(file);
      textFile.open();
      Vector urlSubstrings = new Vector();
      Vector taxonomyItems = new Vector();
      String itemFrom = null;
      String itemTo = null;
      String vReplacementsLine = textFile.getFirstLine();
      while (vReplacementsLine != null) {
        itemizer = new Itemizer(vReplacementsLine);
        itemFrom = itemizer.getNextItemOrNull();
        if (itemFrom != null) {
          itemTo = itemizer.getNextItemOrNull();
          if (itemTo != null) {
            urlSubstrings.add( itemFrom.trim() );
            taxonomyItems.add( itemTo.trim() );
          }
        }
        vReplacementsLine = textFile.getNextLine(); 
      }
      textFile.close();
      if ( urlSubstrings.size() > 0) {
        AllUrlSubstrings = new String[ urlSubstrings.size() ];
        for (int i = 0; i < AllUrlSubstrings.length; i++)
          AllUrlSubstrings[i] = (String)urlSubstrings.elementAt(i);  
      }
      else
        AllUrlSubstrings = null;
      if ( taxonomyItems.size() > 0) {
        AllTaxonomyItems = new String[ taxonomyItems.size() ];
        for (int i = 0; i < AllTaxonomyItems.length; i++)
          AllTaxonomyItems[i] = (String)taxonomyItems.elementAt(i);  
      }
      else
        AllTaxonomyItems = null;
      
    }
    else {            
      AllUrlSubstrings = null;   
      AllTaxonomyItems = null;
    }    
  
  }
  
  /**
   * @return a String representation of TaxonomyList or "" 
   * if there is no TaxonomyList
   */
  
  public String getTaxonomyList() {
  
    String vResult = "";
    if ( (AllUrlSubstrings != null) && (AllTaxonomyItems != null) )
      for (int i = 0; i < AllUrlSubstrings.length; i++) 
        vResult += AllUrlSubstrings[i] + " " + AllTaxonomyItems[i] + "\n";
    else
      vResult = " ";
      
    return vResult;
  
  }  // getReplaceList()
  
  /**
   * initializes the Vectors AllUrlSubstrings and AllTaxonomyItems with the 
   * contents 
   * of the given string, each pair of items must be separated by "\n";
   * the first item is the substring to be replaced by the second item
   * @param pTaxonomyList String containing Items of TaxonomyList
   */
  
  public void setTaxonomyList(String pTaxonomyList) {
  
    Itemizer itemizer = null;
    String item = null;
    
    File file = new File ( CurrentMiningBase.getMiningBaseDirectory() + 
      TAXONOMY_LIST_FILE );
    TextFile textFile = new TextFile(file);
    textFile.empty();
    textFile.open();
    
    itemizer = new Itemizer(pTaxonomyList);
    Vector urlSubstrings = new Vector();
    Vector taxonomyItems = new Vector();
    String itemFrom = null;
    String itemTo = null;
    itemFrom = itemizer.getNextItemOrNull();
    while (itemFrom != null) {      
      itemTo = itemizer.getNextItemOrNull();
      if (itemTo != null) {
        urlSubstrings.add( itemFrom.trim() );
        taxonomyItems.add( itemTo.trim() );
        textFile.setNextLine(itemFrom + " " + itemTo);
      }      
      itemFrom = itemizer.getNextItemOrNull();
    }
    
    textFile.close();    
    
    if ( urlSubstrings.size() > 0) {
      AllUrlSubstrings = new String[ urlSubstrings.size() ];
      for (int i = 0; i < AllUrlSubstrings.length; i++) 
        AllUrlSubstrings[i] = (String)urlSubstrings.elementAt(i);  
    }
    else
      AllUrlSubstrings = null;
    if ( taxonomyItems.size() > 0) {
      AllTaxonomyItems = new String[ taxonomyItems.size() ];
      for (int i = 0; i < AllTaxonomyItems.length; i++) 
        AllTaxonomyItems[i] = (String)taxonomyItems.elementAt(i);  
    }
    else
      AllTaxonomyItems = null;
  
 }  // setTaxonomyList()  
      
  /**
   * determines the (level 1) taxonomy item of the given URL according
   * to this concept hierarchy; 
   * If a Page.Url is not allocated to any taxonomy item, its 
   * Page.TaxonomyLevel1
   * is set to Page.Url
   * @param pUrl URL of which the taxonomy item has to be determined
   * @return String containing the taxonomy item of pUrl according
   *         to this taxonomy hierarchy
   */
  
  public String getTaxonomyItem1(String pNewUrl) {

    CurrentUrl = pNewUrl;
    String taxonomyItem = "";
    int replaceID = isContainedInUrlSubstrings(CurrentUrl);
    if ( replaceID >= 0 )
      taxonomyItem = AllTaxonomyItems[replaceID];
    else
      taxonomyItem = pNewUrl;
      
    return taxonomyItem;
 
  }  // getTaxonomyItem1
    
  /**
   * tests whether or not any item of AllUrlSubstrings is contained in pLine
   * @param pLine String to be tested
   * @return -1 if nothing is to be replaced or the index in AllUrlSubstrings
   * of the substring to be replaced 
   */
  
  private int isContainedInUrlSubstrings(String pLine) {
  
    if ( (AllUrlSubstrings != null) && (AllTaxonomyItems != null) ) {
      for (int i = 0; i < AllUrlSubstrings.length; i++)
        if ( pLine.indexOf( AllUrlSubstrings[i] ) >= 0) return i;
      return -1;
    }
    else    
      return -1;
  
  }  // isToReplace()
      
}