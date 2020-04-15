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
 * A MintTemplate corresponds to one template in the MINT query'a from-clause.
 * It contains variable names and search expressions. A valid template must 
 * either begin with the roor variable name (currently "#") or a valid 
 * variable name. It must end with a valid variable name. Two variable names 
 * r the root variable name and the first variable name van be separated from
 * each other by a SearchExpression. A MintTemplate contains its Name, its 
 * TemplateString and an Array of statistics 
 * predidicates allocated to this MintTemplate. The Arrays VariableNames and
 * SearchExpression contain the corresponding items in correct order of their
 * appearance in the TemplateString. Each variable is allowed to appear once in
 * the TemplateString. <p>
 *
 * Modified by tveit 5/1999: getStatisticsPredicate() <p>
 *
 * Modified by kwinkler 8/1999: SearchSuffix in last SearchExpression of 
 * Template <p>
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MintTemplate implements Serializable {

  // ########## attributes and constants ##########

  private String Name = null;                    
  private String TemplateString = null;          
  private int CounterVariableNames = 0;
  private int CounterSearchExpressions = 0;
  private Vector StatisticsPredicates = null;
  private transient Iterator StatisticsPredicatesIterator = null;
  private Vector VariableNames = null;     
  private Vector SearchExpressions = null;  

  // added by kwinkler 11/99
  private boolean vMatch = false;
  
  public final static String ROOT_VARIABLE_NAME = "#";

  // ########## constructors ##########
  
/**
 * constructs an empty MintTemplate
 */  

  public MintTemplate() {

    Name = "";   TemplateString = "";        
    StatisticsPredicates = new Vector(); StatisticsPredicatesIterator = null;
    VariableNames = new Vector();   SearchExpressions = new Vector();
    
  }  
  
/**
 * constructs an MintTemplate, all attributes are set
 * @param pName the template's name
 * @param pTemplateString String containing one template
 * @param pValidVariables Array containing all valid variable names in the
 * query context, used for syntax check
 * @exception MintSyntaxErrorException if pTemplateString doesn't
 * conform to the MINT syntax
 */  

  public MintTemplate(String pName, String pTemplateString, 
    Vector pValidVariableNames) throws MintSyntaxErrorException  {

    Name = pName;   TemplateString = pTemplateString.trim();        
    StatisticsPredicates = new Vector();   StatisticsPredicatesIterator = null;
    VariableNames = new Vector();   SearchExpressions = new Vector();
    
    this.splitTemplateString(pValidVariableNames);
    
  }  
  
  // ########## accessor methods ##########
  
  public String getName() { return Name; }
  public int countVariableNames() { return CounterVariableNames; }
  public int countSearchExpressions() { return CounterSearchExpressions; }
  //######### tveit
  public MintPredicate getStatisticsPredicate(int pPredicateID) {
    if ((pPredicateID >= 0) && 
      (pPredicateID < this.countStatisticsPredicates())) 
      return (MintPredicate)StatisticsPredicates.elementAt(pPredicateID);
     else return null;
  }
  public boolean startsWithRoot() {
    if ( TemplateString.trim().startsWith(ROOT_VARIABLE_NAME) )
      return true;
    else
      return false;
  }

    
  // ########## standard methods ##########
  
  public String toString() {
  
    String result = "MintTemplate: " + Name + " = " + TemplateString;    
    StatisticsPredicatesIterator = StatisticsPredicates.iterator();
    while ( StatisticsPredicatesIterator.hasNext() )
      result += "\n   " + 
        ( (MintPredicate)StatisticsPredicatesIterator.next() ).toString();
    StatisticsPredicatesIterator = null;    
    result += "\n   VariableNames: " + VariableNames;
    result += "\n   SearchExpressions: " + SearchExpressions;
      
    return result;
    
  }  // toString()

/**
 * sets all attributes of MintTemplate by parsing TemplateString
 * @param pValidVariables Array containing all valid variable names in the
 * query context, used for syntax check
 * @exception MintSyntaxErrorException if PredicateString doesn't
 * conform to the MINT syntax
 */  
  
  public void splitTemplateString(Vector pValidVariableNames) 
    throws MintSyntaxErrorException {

    Itemizer oItemizer = new Itemizer(TemplateString);
    String vNextSearchExpression = "";
    String vNextVariable = "";
    int vCounterItems = 1;
    int vSearchExpressionType = SearchExpression.TYPE_UNKNOWN;
    boolean vStartWithRoot = false;
    
    String vNextItem = oItemizer.getNextItemOrNull();
    while (vNextItem != null) {
      
      vNextItem = vNextItem.trim();
      
      // template starts with variable name, 1st search expression is dummy '*'
      if ( (vCounterItems == 1) && 
        ( ! vNextItem.startsWith(ROOT_VARIABLE_NAME) ) ) {
        vStartWithRoot = false;  
        vNextSearchExpression = "*";  // TYPE_WUM_GIVEN
        vSearchExpressionType = SearchExpression.TYPE_WUM_GIVEN;
      } // variable follows
      
      // template starts with root variable name, 1st search expression is 
      // valid
      if ( (vCounterItems == 1) && 
        ( vNextItem.startsWith(ROOT_VARIABLE_NAME) ) ) {
        vStartWithRoot = true;  
        if (vNextItem.length() > 1)  // TYPE_USER_GIVEN
          throw new MintSyntaxErrorException(
          ROOT_VARIABLE_NAME + 
          " must be separated by a blank space from the next item!");
        else
          vNextSearchExpression = "";  
        vSearchExpressionType = SearchExpression.TYPE_USER_GIVEN;
      } else // variable or search expression follows
      
      if ( (vNextItem.indexOf("*") >= 0) || (vNextItem.indexOf("_") >= 0) || 
         (vNextItem.indexOf("[") >= 0) || (vNextItem.indexOf("]") >= 0)  ||
         ( vNextItem.startsWith(ROOT_VARIABLE_NAME) ) ) { 
        // found search expression: perform various syntax checks
        if ( (vCounterItems == 1) && ( ! vStartWithRoot) )
          throw new MintSyntaxErrorException(
            "Template expressions must begin with " + ROOT_VARIABLE_NAME +
            " or with a valid variable name!");
        else          
        if ( vNextItem.equals("*") && (vNextSearchExpression.length() > 0) )
          throw new MintSyntaxErrorException(
            "Search expression in template expression is not valid!");
        else
        if ( vNextItem.equals("_") && 
          (vNextSearchExpression.indexOf("*") >= 0) )
          throw new MintSyntaxErrorException(
            vNextSearchExpression + 
            " Search expression in template expression is not valid!");
        else
        if ( vNextItem.equals("_") && 
          (vNextSearchExpression.indexOf("[") >= 0) )
          throw new MintSyntaxErrorException(
            "Search expression in template expression is not valid!");
        else
        if ( vNextItem.startsWith("[") && 
          (vNextSearchExpression.length() > 0) )
          throw new MintSyntaxErrorException(
            "Search expression in template expression is not valid!");
        else
        if ( vNextItem.startsWith("[") && (vNextItem.indexOf("]") < 0) )
          throw new MintSyntaxErrorException(
            "Search expression in template expression is not valid!");
        else
        if ( vNextItem.startsWith("[") && (vNextItem.indexOf(";") < 0) )
          throw new MintSyntaxErrorException(
            "Search expression in template expression is not valid!");
        vSearchExpressionType = SearchExpression.TYPE_USER_GIVEN;
        // save this part of search expression
        vNextSearchExpression += vNextItem;  
      }
      else { 
        // found variable: save previous search expression
        SearchExpressions.add(
          new SearchExpression(vNextSearchExpression, vNextItem, 
          vSearchExpressionType) );
        vNextSearchExpression = "";
        CounterSearchExpressions++;
        if ( ( pValidVariableNames.contains(vNextItem) ) && 
          ( ! VariableNames.contains(vNextItem) ) ) {
          VariableNames.add(vNextItem);        
          CounterVariableNames++;
        }
        else
          throw new MintSyntaxErrorException(
            "Variable " + vNextItem + " in template expression is not valid!");
      }
      vCounterItems++;
      vNextItem = oItemizer.getNextItemOrNull();  
    }
    
    // template ends with search expression
    if (vNextSearchExpression.length() > 0) {
      // old version
      // throw new MintSyntaxErrorException(
      //  "Template expressions must end with a valid variable name!");
      // modified by kwinkler 8/99
      SearchExpression oLastSearchExpression = 
        (SearchExpression)SearchExpressions.remove(
        SearchExpressions.size() - 1 );
      oLastSearchExpression.setSearchSuffix(vNextSearchExpression);
      SearchExpressions.add(oLastSearchExpression);
      // System.out.println( "Last SearchExpression with SearchSuffix = " + 
      //   oLastSearchExpression.toString() );
    }
          
  }  // splitTemplateString()()

  /**
   * @return all variable names as an array of String in order of their
   * occurrence in the template string
   */

  public String[] getVariableNames() {  
  
    // in order according to template
    String[] aResult = new String[ VariableNames.size() ];
    for (int i = 0; i < aResult.length; i++) 
      aResult[i] = (String)VariableNames.elementAt(i);
    
    return aResult;
  
  }  // getVariableNames()
  
  /**
   * returns the MintVariable's name specified by its position in the array 
   * Variables
   * @param pIndex 0 <= pIndex < VariablesNames.length
   * @return corresponding MintVariable's name or "" if pIndex is out of 
   * valid range
   */  

  public String getVariableName(int pIndex) {  
  
    if ( (pIndex >= 0) && (pIndex < VariableNames.size()) )
      return (String)VariableNames.elementAt(pIndex);
    else
      return "";
  
  }  // getVariableName()
  
  /**
   * @return all Searchexpressions as an array of SearchExpressions in order of
   * their occurrence in the template string, its first SearchExpression 
   * (index: 0) 
   * occurs before the first MintVariable in the template
   */

  public SearchExpression[] getSearchExpressions() {
  
    // in order according to template
    SearchExpression[] aResult = 
      new SearchExpression[ SearchExpressions.size() ];
    for (int i = 0; i < aResult.length; i++) 
      aResult[i] = (SearchExpression)SearchExpressions.elementAt(i);
    
    return aResult;
  
  }  // getSearchExpressions()

  /**
   * returns the SearchExpression specified by its position in the array 
   * SearchExpressions, exmaple: this.getSearchExpression(1) returns the
   * SearchExpression occuring before the 2nd MintVariable in the template
   * @param pIndex 0 <= pIndex < SearchExpressions.length
   * @return corresponding SDearchExpression or null if pIndex is out of 
   * valid range
   */  
  
  public SearchExpression getSearchExpression(int pIndex) {  
  
    if ( (pIndex >= 0) && (pIndex < SearchExpressions.size()) )
      return (SearchExpression)SearchExpressions.elementAt(pIndex);
    else
      return null;
  
  }  // getSearchExpression()
  
  
  /**
   * return the given variable's position in TemplateString and therfore its 
   * index in the Arrays StatisticsPredicates and SearchExpressions
   * @param pVariable the variable's name
   * @return index of pVariable or -1 if pVariable doesn't exist in 
   * MintTempplate
   */

  public int getPositionOfVariable(String pVariable) {
  
    // returns position of pVariable in array VariableNames
    // or -1 if pVariable isn't contained in VariableNames
  
    for (int i = 0; i < VariableNames.size(); i++)
      if ( VariableNames.elementAt(i).equals(pVariable) ) return i;
   
    return -1;
    
  }  // getPositionOfVariable()  

  
  /**
   * adds the given MintPredicate to the MintTemplate's Array 
   * StatisticsPredicates
   * @param pStatisticsPredicate StatisticsPredicate to add
   */
  
  public void addStatisticsPredicate(MintPredicate pStatisticsPredicate) {
  
    StatisticsPredicates.add(pStatisticsPredicate);
  
  }  // addStatisticsPredicate()    

  /**
    * added by kwinkler 11/99
    * adds the given MintPredicate to the SearchExpression of the appropriate 
    * variable
    * @param pWildcardPredicate WildcardPredicate (= ContentPredicate) to be 
    * added
    */
  
  public void addWildcardPredicate(MintPredicate pWildcardPredicate) 
    throws MintSyntaxErrorException {
  
    int vVariableIndex = getPositionOfVariable( 
      pWildcardPredicate.getVariable() );
    if (vVariableIndex != -1)
      ( (SearchExpression)(SearchExpressions.elementAt(vVariableIndex) ) )
        .addContentPredicate(pWildcardPredicate);
    else
      throw new MintSyntaxErrorException(
        "Variable in wildcard expression does not exist!"); 

  }  // addStatisticsPredicate()    

  
  /**
   * @return the number of statistics predicates contained in Array
   * StatisticsPredicates
   */
  
  public int countStatisticsPredicates() { 
  
    if (StatisticsPredicates == null) 
      return 0;
    else
      return StatisticsPredicates.size(); 
      
  }  // countStatisticsPredicates()  

  /**
   * tests whether this MintTemplate (better: its statistics predicates and 
   * predicates
   * containing expressions) is matched by the given MintPatternDescriptor;
   * @param pWUM WUM context for the WUM query
   * @param pPatternDescriptor MintPatternDescriptor that is to be tested for 
   * match 
   * with this MintTemplate
   * @return true if pPatternDescriptor match this MintTemplate or false if 
   * there
   * is no match 
   */

  public boolean isMatchedByPatternDescriptor(MiningBase pMiningBase,
    MintPatternDescriptor pPatternDescriptor) {
    
    vMatch = false;
  
    if ( (StatisticsPredicates == null) || 
      (StatisticsPredicates.size() == 0) ) 
      // no predicate = all pattern descriptors match
      return true;
    else {
      // predicates = all predicates must be matched by pattern descriptor
      for (int i = 0; i < StatisticsPredicates.size(); i++) {
        vMatch = ( (MintPredicate)StatisticsPredicates.elementAt(i) )
          .isMatchedByPatternDescriptor(pMiningBase, pPatternDescriptor, 
          this);
        if ( ! vMatch ) return false;              
      } 
    }
    
    return vMatch;
  
  }  // isMatchedByPatternDescriptor()

}

