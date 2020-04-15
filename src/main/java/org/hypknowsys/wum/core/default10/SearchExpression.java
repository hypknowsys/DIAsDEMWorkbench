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

package org.hypknowsys.wum.core.default10;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;

/**
 * Each SearchExpression is a part of a MINT query's template clause.
 * A SearchExpression contains valid wildcarts. The type of wildcart
 * determines of possible size of subtrails before the following
 * template variable is matched. The attribute LookForPageOccurrence
 * contains next current binding of the following template variable. Syntax:<p>
 * "*" stands for 0, 1, 2, ..., Integer.MAX_VALUE PageOccurrences before 
 * LookForPageOccurrence<p>
 * "_" stands for exactly 1 PageOccurrence before LookForPageOccurrence<p>
 * "__" or "_ _" stands for exactly 2 PageOccurrences before 
 * LookForPageOccurrence<p> "" stands for 0 PageOccurrences before 
 * LookForPageOccurrence (exact match)<p>
 * "[2;4]" stands for min. 2 and max. 4 PageOccurrences before 
 * LookForPageOccurrence<p>
 *
 * A SearchExpression can either be given by the user (i.g. it's contained
 * in the template) or it can automatically be added by WUM (i.g. there is
 * no placeholder before the first variable name in teh template). During
 * the process of generating and evaluating MintPatternDescriptor in the
 * AggregatedLog, the SearchExpression's counter is used to determine 
 * valid (i.g. matching the placeholders) pattern descriptors. <p>
 *
 * Modified by kwinkler 8/1999: search suffix after last variable of template
 * String SearchSuffix added; contains suffix in case of last variable, 
 * otherwise empty <p>
 * 
 * Mofidied by kwinkler 11/1999: various changes to include predicates of 
 * wildcards, LAST_VALID = current node is valid but doesn't match content 
 * predicates of this  SearchExpression; therefore do not continue traversing
 * the tree afterwards 
 * 
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class SearchExpression implements Serializable, Cloneable {

  // ########## attributes and constants ##########
  
  private String SearchPrefix = "";
  private int Type = TYPE_UNKNOWN;
  private int MinPageOccurrences = 0;
  private int MaxPageOccurrences = 0;  
  private int Counter = 0;   
  private PageOccurrence LookForPageOccurrence = null;
  private String LookForVariableName = null;

  // 9/99 modified by kwinkler: change not completed yet
  private String SearchSuffix = "";  
  private int SuffixMinPageOccurrences = 0;
  private int SuffixMaxPageOccurrences = 0;  
  private int SuffixCounter = 0;
  
  // 11/99 mofified by kwinkler
  private Vector ContentPredicates = null;
  private Iterator ContentPredicatesIterator = null; 
  private boolean vMatch = false;
  private Page oPage = null;

  public static final int TYPE_UNKNOWN = 0;  
  public static final int TYPE_USER_GIVEN = 1;
  public static final int TYPE_WUM_GIVEN = 2;
  public static final int TYPE_WUM_OUTPUT = 3;
  
  public static final int SMALLER_MIN = -1;  
  public static final int VALID = 0;
  public static final int GREATER_MAX = 1;
  // 11/99 mofified by kwinkler
  public static final int LAST_VALID = 2;
  
  public static final int UNKNOWN = Integer.MIN_VALUE;
  public static final int START = -1;
  public static final int MIN = 0;
  public static final int MAX = Integer.MAX_VALUE;
  public static final int MIN_STAR = 0;
  public static final int MAX_STAR = Integer.MAX_VALUE;
  public static final int MIN_UNDERSCORE = 1;
  public static final int MAX_UNDERSCORE = 1;

  // ########## constructors ##########
  
/**
 * constructs an emtpy SearchExpression
 */  

  public SearchExpression() {

    SearchPrefix = "";   Type = TYPE_UNKNOWN;
    MinPageOccurrences = UNKNOWN;   MaxPageOccurrences = UNKNOWN; 
    Counter = START;    
    LookForPageOccurrence = null;    LookForVariableName = null; 
    // 9/99 modified by kwinkler    
    SearchSuffix = "";
    // 11/99 modified by kwinkler
    ContentPredicates = new Vector();   ContentPredicatesIterator = null;
    
  }  
  
/**
 * constructs an SearchExpression with the given attributes
 * @param pMinPageOccurrences value of MinPageOccurrences 
 * @param pMaxPageOccurrences value of MaxPageOccurrences
 */

  public SearchExpression(int pMinPageOccurrences, int pMaxPageOccurrences) {

    SearchPrefix = "";   Type = TYPE_UNKNOWN;
    MinPageOccurrences = pMinPageOccurrences;   
    MaxPageOccurrences = pMaxPageOccurrences; 
    Counter = START;    
    LookForPageOccurrence = null;    LookForVariableName = null;
    // 9/99 modified by kwinkler
    SearchSuffix = "";
    // 11/99 modified by kwinkler
    ContentPredicates = new Vector();   ContentPredicatesIterator = null;
    
  }  
  
/**
 * constructs an SearchExpression with the given attributes, decomposes
 * given given placeholder string
 * @param pSearchPrefix contains a valid placeholder string (
 * e.g. "*", "_", "__", "____" or "[0;1]", [2;5])
 * @param pLookForVariableName variable name following in the template 
 * after pSearchPrefix 
 * @param pType Type of the SearchExpression: this.TYPE_USER_GIVEN, 
 * this.TYPE_WUM_GIVEN or this.TYPE_WUM_OUTPUT
 */

  public SearchExpression(String pSearchPrefix, String pLookForVariableName,
    int pType) {

    LookForVariableName = pLookForVariableName.trim();
    SearchPrefix = pSearchPrefix.trim();

    // 9/99 modified by kwinkler
    SearchSuffix = "";
    // 11/99 modified by kwinkler
    ContentPredicates = new Vector();   ContentPredicatesIterator = null;

    Type = pType;
    
    if (pSearchPrefix.indexOf("[") == 0) {
     
      int vPosition = pSearchPrefix.indexOf(";");
      int vLength = pSearchPrefix.length();
      try {
        MinPageOccurrences = 
          ( new Integer( pSearchPrefix.substring(1, vPosition) ) ).intValue();
        MaxPageOccurrences = ( new Integer( pSearchPrefix.substring(
          vPosition + 1, vLength - 1) ) ).intValue();
      }
      catch (NumberFormatException e) {
        MinPageOccurrences = 0;
        MaxPageOccurrences = 0;       
      }
     
    }
    else {
    
      if ( SearchPrefix.equals("") ) {
        MinPageOccurrences = 0;
        MaxPageOccurrences = 0; 
      } else
      if ( SearchPrefix.equals("*") ) {
        MinPageOccurrences = MIN_STAR;
        MaxPageOccurrences = MAX_STAR;  
      } 
      else {
        MinPageOccurrences = 0;
        MaxPageOccurrences = 0;
        for (int i = 0; i < SearchPrefix.length(); i++)
          if (SearchPrefix.charAt(i) == '_') {
            MaxPageOccurrences++;  
            MinPageOccurrences++;
          }
      }
      
    }
    Counter = UNKNOWN;
    
    LookForPageOccurrence = null;   
    
  }       
  
  // ########## mutator methodes ##########
  
  public void setSearchPrefix(String pSearchPrefix) 
    { SearchPrefix = pSearchPrefix; }
  public void setType(int pType) 
    { Type = pType; }
  public void setMinPageOccurrences(int pMinPageOccurrences) 
    { MinPageOccurrences = pMinPageOccurrences; }
  public void setMaxPageOccurrences(int pMaxPageOccurrences)
    { MaxPageOccurrences = pMaxPageOccurrences; }
  public void setCounter(int pCounter) 
    { Counter = pCounter; }
  public void resetCounter() 
    { Counter = START; }
  public void setLookForPageOccurrence(PageOccurrence pLookForPageOccurrence)
    { LookForPageOccurrence = pLookForPageOccurrence; }
  public void setLookForVariableName(String pLookForVariableName)
    { LookForVariableName = pLookForVariableName; }  
  // 9/99 modified by kwinkler
  public void setSearchSuffix(String pSearchSuffix)
    { SearchSuffix = pSearchSuffix; this.splitSuffixString(pSearchSuffix); }
  public void setSuffixMinPageOccurrences(int pSuffixMinPageOccurrences) 
    { SuffixMinPageOccurrences = pSuffixMinPageOccurrences; }
  public void setSuffixMaxPageOccurrences(int pSuffixMaxPageOccurrences)
    { SuffixMaxPageOccurrences = pSuffixMaxPageOccurrences; }
  public void setSuffixCounter(int pSuffixCounter) 
    { SuffixCounter = pSuffixCounter; }
  public void resetSuffixCounter() 
    { SuffixCounter = START; }

  
  // ########## accessor methods ##########
  
  public String getSearchPrefix() 
    { return SearchPrefix; }
  public int getType() 
    { return Type; }
  public int getMinPageOccurrences() 
    { return MinPageOccurrences; }
  public int getMaxPageOccurrences() 
    { return MaxPageOccurrences; }
  public int getCounter() 
    { return Counter; }
  public long getLookForPageID() 
    { return LookForPageOccurrence.getPageID(); }
  public int getLookForOccurrence() 
    { return LookForPageOccurrence.getOccurrence(); }
  public PageOccurrence getLookForPageOccurrence() 
    { return LookForPageOccurrence; }
  public String getLookForVariableName() 
    { return LookForVariableName; }
  // 9/99 modified by kwinkler
  public String getSearchSuffix() 
    { return SearchSuffix; }
  public boolean containsSearchSuffix() 
    { if (SearchSuffix.length() == 0) return false; else return true; }
  public int getSuffixMinPageOccurrences() 
    { return SuffixMinPageOccurrences; }
  public int getSuffixMaxPageOccurrences() 
    { return SuffixMaxPageOccurrences; }
  public int getSuffixCounter() 
    { return SuffixCounter; }

  
  // ########## standard methods ##########
  
  public String toString() { 
  
    String vResult = "P=[" + SearchPrefix + ";" + MinPageOccurrences + ";" +
      MaxPageOccurrences + "] S=[" + SearchSuffix + ";" + 
      SuffixMinPageOccurrences + ";" + SuffixMaxPageOccurrences + "] PC=" + 
      Counter + " SC=" + SuffixCounter + " T=" + Type;
    if (LookForPageOccurrence != null) 
      vResult += " LookFor=" + LookForPageOccurrence.toString();
 
    if ( ContentPredicates != null) {
      ContentPredicatesIterator = ContentPredicates.iterator();
      while ( ContentPredicatesIterator.hasNext() )
        vResult += "\n   " + 
          ( (MintPredicate)ContentPredicatesIterator.next() ).toString();
      ContentPredicatesIterator = null;
    }
      
    return vResult;
    
  }  // toString() 
  
  public Object clone() { 
  
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException e) {
      return null;
    }
    
  }  // clone() 
  
  /**
    * added by kwinkler, 11/99
    * increments the counter in this SearchExpression that 
    * is used whether or not the placeholder is match and checks whether
    * the given PageOccurrence matches all ContentPredicates of this
    * SearchExpression
    * @param pWUM WUM context for the WUM query
    * @param pPageOccurrence PageOccurrence that is to be tested for match 
    * @return int value (this.VALID, this.LAST_VALID, this.SMALLER_MIN or 
    * this.GREATER_MAX) indicating whether or not the incremented counter is 
    * in its valid range [MinPageOccurrences; MaxPageOccurrences]
    */
  
  public int incrementCounter(MiningBase pMiningBase, 
    PageOccurrence pPageOccurrence) { 
  
    // increments Counter; 
    // returns VALID: Counter >= min and Counter <= max, 
    // all ContentPredicates match
    // return LAST_VALID: Counter >= min and Counter <= max, 
    // ContentPredicates don't match
    
    Counter++;
    
    if ( (Counter >= MinPageOccurrences) && (Counter <= MaxPageOccurrences) )
      if ( this.isMatchedByPageOccurrence(pMiningBase, pPageOccurrence) )
        return VALID;
      else
        return LAST_VALID;
    else
      if ( (Counter < MinPageOccurrences) )
        return SMALLER_MIN;
      else
        return GREATER_MAX;      
    
  }  // incrementCounter() 

  /**
   * increments the counter in this SearchExpression that 
   * is used whether or not the placeholder is match 
   * @return int value (this.VALID, this.SMALLER_MIN or this.GREATER_MAX)
   * indicating whether or not the incremented counter isin its valid
   * range [MinPageOccurrences; MaxPageOccurrences]
   */
  
  public int incrementCounter() { 
  
    // increments Counter; 
    // returns VALID: Counter >= min and Counter <= max
    
    Counter++;
    
    if ( (Counter >= MinPageOccurrences) && (Counter <= MaxPageOccurrences) )
      return VALID;
    else
      if ( (Counter < MinPageOccurrences) )
        return SMALLER_MIN;
      else
        return GREATER_MAX;      
    
  }  // incrementCounter() 
  
  /**
   * decrements the counter in this SearchExpression
   */
  
  public void decrementCounter() { Counter--; }
  
  /**
   * decreases the counter in this SearchExpression by the given value
   * @param pDecrease value by which SearchExpression has to be decreased
   */
  
  public void decreaseCounter(int pDecrease) { Counter -= pDecrease; }

  /**
   * added by kwinkler 8/99, increments the SuffixCounter in this 
   * SearchExpression that is used whether or not the placeholder is match 
   * @return int value (this.VALID, this.SMALLER_MIN or this.GREATER_MAX)
   * indicating whether or not the incremented counter is in its valid
   * range [SuffixMinPageOccurrences; SuffixMaxPageOccurrences]
   */
  
  public int incrementSuffixCounter() { 
  
    // increments SuffixCounter; 
    // returns VALID: SuffixCounter >= min and SuffixCounter <= max
    
    Counter++;
    
    if ( (SuffixCounter >= SuffixMinPageOccurrences) && 
      (SuffixCounter <= SuffixMaxPageOccurrences) )
      return VALID;
    else
      if ( (SuffixCounter < SuffixMinPageOccurrences) )
        return SMALLER_MIN;
      else
        return GREATER_MAX;      
    
  }  // incrementSuffixCounter() 

  /**
   * added by kwinkler 8/99, decrements the SuffixCounter in this 
   * SearchExpression
   */
  
  public void decrementSuffixCounter() { Counter--; }
  
  /**
   * added by kwinkler 8/99, decreases the SuffixCounter in this 
   * SearchExpression by the given value
   * @param pDecrease value by which SearchExpression has to be decreased
   */
  
  public void decreaseSuffixCounter(int pDecrease) 
    { SuffixCounter -= pDecrease; }

  /**
    * added by kwinkler 11/99
    * adds the given MintPredicate to the SearchExpression's Array 
    * ContentPredicates
    * @param pContentPredicate ContentPredicate to add
    */
  
  public void addContentPredicate(MintPredicate pContentPredicate)  
    throws MintSyntaxErrorException {
  
    // rule: no wildcard, no wildcard predicate
    if ( ( (MinPageOccurrences == 0) && (MaxPageOccurrences == 0) ) ||
       ( Type == TYPE_WUM_GIVEN ) )
      throw new MintSyntaxErrorException("Wildcard predicate without " +
        "corresponding wildcard!");

    ContentPredicates.add(pContentPredicate);
  
  }  

  /**
    * added by kwinkler 11/99
    * @return the number of content predicates contained in Array
    * ContentPredicates
    */
  
  public int countContentPredicates() { 
  
    if (ContentPredicates == null) 
      return 0;
    else
      return ContentPredicates.size(); 
      
  }  // countContentPredicates()

    
  /**
   * added by kwinkler 8/99, splits pSearchSuffix into its itmes and sets
   * SuffixMinPageOccurrences, SuffixMaxPageOccurrences and SuffixCounter
   * @param pSearchSuffix String containing the search suffix that appears 
   * after the last variable in the template
   */

  private void splitSuffixString(String pSearchSuffix) {


    if (pSearchSuffix.indexOf("[") == 0) {
     
      int vPosition = pSearchSuffix.indexOf(";");
      int vLength = pSearchSuffix.length();
      try {
        SuffixMinPageOccurrences = 
          ( new Integer( pSearchSuffix.substring(1, vPosition) ) ).intValue();
        SuffixMaxPageOccurrences = ( new Integer( pSearchSuffix.substring(
          vPosition + 1, vLength - 1) ) ).intValue();
      }
      catch (NumberFormatException e) {
        SuffixMinPageOccurrences = 0;
        SuffixMaxPageOccurrences = 0;       
      }
     
    }
    else {
    
      if ( SearchSuffix.equals("") ) {
        SuffixMinPageOccurrences = 0;
        SuffixMaxPageOccurrences = 0; 
      } else
      if ( SearchSuffix.equals("*") ) {
        SuffixMinPageOccurrences = MIN_STAR;
        SuffixMaxPageOccurrences = MAX_STAR;  
      } 
      else {
        SuffixMinPageOccurrences = 0;
        SuffixMaxPageOccurrences = 0;
        for (int i = 0; i < SearchSuffix.length(); i++)
          if (SearchSuffix.charAt(i) == '_') {
            SuffixMaxPageOccurrences++;  
            SuffixMinPageOccurrences++;
          }
      }
      
    }
    SuffixCounter = UNKNOWN;

  }  // splitSuffixString()

  /**
   * tests whether this SearchExpression (better: its content predicates) is 
   * matched by the given PageOccurrence;
   * @param pWUM WUM context for the WUM query
   * @param pPageOccurrence PageOccurrence that is to be tested for match 
   * with the content predicates of this SearchExpression
   * @return true if pPageOccurrence match this SearchExpression or false if 
   * there is no match 
   */
  
  private boolean isMatchedByPageOccurrence(MiningBase pMiningBase, 
    PageOccurrence pPageOccurrence) {
   
    if ( (ContentPredicates == null) || (ContentPredicates.size() == 0) ) 
      return true;  // no predicate = all nodes match

    vMatch = false;    
    oPage = pMiningBase.getPage( pPageOccurrence.getPageID() );
    if (oPage == null) return false;
   
    // predicates = all predicates must be matched by node
    for (int i = 0; i < ContentPredicates.size(); i++) {
      vMatch = ( (MintPredicate)ContentPredicates.elementAt(i) ).
        isMatchedByPage(oPage, pPageOccurrence.getOccurrence() );
      if ( !vMatch ) return false;              
      }
    
    return vMatch;
  
  }  // isMatchedByObservationNode()

}