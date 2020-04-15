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

/**
 * A MintPredicate represents one predicate in the MINT query's where-clause.
 * It can either be a simple predicate or a complex predicate containing one 
 * MintPredicateExpression on the lefthand side. Syntax:<p>
 * (Simple MintPredicate) "VariableName.Attribute Operator Operand"
 * (Complex MintPredicate) "( MintPredicateExpression ) Operator Operand"<P>
 * Attributes = ("url" = URL as a String; "id" = ID of the URL in Pages as an 
 * long number; "accesses" = total integer number of a Page's accesses;
 * "occurrence" = page occurrence integer number in a Trail; 
 * "support" = support value of a PageOccurrence in an observation as a long 
 * number). Operators = ("<="; "<"; "="; ">"; ">="; "!="; only for Strings: 
 * "startswith"; 
 * "contains", "endswith", "!startswith", "!contains", "!endswith"). 
 * Operands can either be Strings ("SomeString") or numbers (123 or 0.5).
 * A MintPredicate is constructed by7 parsing a String containing exactly one
 * predicate. Each MintPredicate is either of Type CONTENT_PREDICATE 
 * (attributes: "url", "id" or "accesses") or of Type STATISTICS_PREDICATE 
 * (attribute "support" or MintPredicate contains a MintPredicateExpression).
 *
 * Modified by tveit 5/99: mutator methods
 * 
 * Modified by kwinkler 11/99: predicate can now be either a VARIABLE_PREDICATE
 * or a WILDCARD_PREDICATE (stored in PredicateClass), methods 
 * setPredicateClass(), getPredicateClass(), isVariablePredicate(), is 
 * WildcardPredicate() added new syntax of MintPredicate: a * b _ c, 
 * *.b.url != "...", wildcard.c.url != "..." predicate on wildcards can only 
 * be content predicates
 * 
 * Modified by kwinkler 8/2000: method isMatchedByMintPatternOutput added
 * to support the filtering of query results; attribute OutputPredicate
 * added to prevent output attributes to be used in mining queries;
 * problem: filter predicates include a wider choice of attribute that
 * MINT query predicates; filter vs. query predicates; filter predicates 
 * consist of content predicates, statistics predicates and predicates
 * using the attributes confidence and title; filter predicate does not
 * support expression predicates and predicates on wildcards
 */

public class MintPredicate implements Serializable {

  // ########## attributes and constants ##########

  private String PredicateString = null;
  private int Type = UNKNOWN_PREDICATE;
  private String Variable = null;
  private int Attribute = ATTR_UNKNOWN;
  private int AttributeType = TYPE_UNKNOWN;
  private int Operator = OP_UNKNOWN;
  private Object Value = null;
  private MintPredicateExpression Expression = null;
  // ######## attributes by kwinkler 11/99 #########
  private int PredicateClass = UNKNOWN_PREDICATE;
  // ######## attributes by kwinkler 11/99 #########
  // reason: improval of performance
  private boolean vResult = false;
  private Object oCompareValue = null;
  private boolean vResultComparison = false;
  private int vIntCompareValue = 0;
  private int vIntValue = 0;
  private long vLongCompareValue = 0l;
  private long vLongValue = 0l;
  private double vDoubleCompareValue = 0.0d;
  private double vDoubleValue = 0.0d;
  private String vStringCompareValue = null;  
  private String vStringValue = null;
  // ######## attributes by kwinkler 8/2000 #########
  // true = filtering of query; false = query predicate
  private boolean FilterPredicate = false;
  
  public static final int UNKNOWN_PREDICATE = 0;
  public static final int CONTENT_PREDICATE = 1;
  public static final int STATISTICS_PREDICATE = 2;  
  public static final int FILTER_PREDICATE = 3;  
  // ######## attributes by kwinkler 11/99 #########
  public static final int VARIABLE_PREDICATE = 1;
  public static final int WILDCARD_PREDICATE = 2;      
  
  private static final int ATTR_UNKNOWN = 0;
  private static final int ATTR_URL = 1;
  private static final int ATTR_ID = 2;
  private static final int ATTR_ACCESSES = 3;
  private static final int ATTR_OCCURRENCE = 4;
  private static final int ATTR_SUPPORT = 5;
  private static final int ATTR_CONFIDENCE = 6;
  private static final int ATTR_TITLE = 7;
  private static final int ATTR_RELSUPPORT = 8;
  private static final int ATTR_ROOTCONFIDENCE = 9;
  private static final int ATTR_EXPRESSION = 100;
  
  private static final int TYPE_UNKNOWN = 0;
  private static final int TYPE_INT = 1;
  private static final int TYPE_LONG = 2;
  private static final int TYPE_DOUBLE = 3;
  private static final int TYPE_STRING = 4;
  
  private static final String[] AttributeStrings =
    { "url", "id", "accesses", "occurrence", "support", "confidence", 
      "title", "relsupport", "rootconfidence" };
  private static final int[] Attributes =
    { ATTR_URL, ATTR_ID, ATTR_ACCESSES, ATTR_OCCURRENCE, ATTR_SUPPORT,
      ATTR_CONFIDENCE, ATTR_TITLE, ATTR_RELSUPPORT, ATTR_ROOTCONFIDENCE };
  private static final int[] AttributeTypes =
    { TYPE_STRING, TYPE_LONG, TYPE_INT, TYPE_INT, TYPE_LONG,
      TYPE_DOUBLE, TYPE_STRING, TYPE_DOUBLE, TYPE_DOUBLE };
  private static final int[] AttributePredicateTypes =
    { CONTENT_PREDICATE, CONTENT_PREDICATE, CONTENT_PREDICATE, 
      CONTENT_PREDICATE, STATISTICS_PREDICATE, FILTER_PREDICATE,
      FILTER_PREDICATE, FILTER_PREDICATE, FILTER_PREDICATE };
    
  private static final int OP_UNKNOWN = -100;  
  private static final int OP_LESS_EQUAL = -2;
  private static final int OP_LESS = -1;
  private static final int OP_EQUAL = 0;
  private static final int OP_MORE = 1;
  private static final int OP_MORE_EQUAL = 2;
  private static final int OP_NOT_EQUAL = 3;
  private static final int OP_STARTSWITH = 4;
  private static final int OP_CONTAINS = 5;
  private static final int OP_ENDSWITH = 6;
  private static final int OP_NOT_STARTSWITH = 7;
  private static final int OP_NOT_CONTAINS = 8;
  private static final int OP_NOT_ENDSWITH = 9;
  
  private static final String[] OperatorStrings =
    { "<=", "<", "=", ">", ">=", "!=", "startswith", "contains", "endswith",
    "!startswith", "!contains", "!endswith" };
  private static final int[] Operators =
    { OP_LESS_EQUAL, OP_LESS, OP_EQUAL, OP_MORE, OP_MORE_EQUAL, OP_NOT_EQUAL,
      OP_STARTSWITH, OP_CONTAINS, OP_ENDSWITH, OP_NOT_STARTSWITH, 
      OP_NOT_CONTAINS, OP_NOT_ENDSWITH};
      
  // ########## constructors ##########

/**
 * constructs an empty MintPredicate, type is unknown
 */  

  public MintPredicate() {

    PredicateString = "";   Type = UNKNOWN_PREDICATE;   Variable = ""; 
    PredicateClass = UNKNOWN_PREDICATE;  
    Attribute = ATTR_UNKNOWN;   AttributeType = TYPE_UNKNOWN;
    Operator = OP_UNKNOWN;   Value = null;   
    Expression = null;
    FilterPredicate = false;
        
  }  
  
/**
 * constructs an MintPredicate, all attributes are set
 * @param pPredicateString String containing one predicate
 * @param pValidVariables Array containing all valid variable names in the
 * query context, used for syntax check
 * @exception MintSyntaxErrorException if pPredicateString doesn't
 * conform to the MINT syntax
 */  

  public MintPredicate(String pPredicateString, Vector pValidVariables,
    boolean pFilterPredicate) throws MintSyntaxErrorException {

    PredicateString = pPredicateString.trim();   
    Type = UNKNOWN_PREDICATE;   Variable = ""; 
    PredicateClass = UNKNOWN_PREDICATE;
    Attribute = ATTR_UNKNOWN;   AttributeType = TYPE_UNKNOWN;
    Operator = OP_UNKNOWN;   Value = null;   
    Expression = null;
    FilterPredicate = pFilterPredicate;    

    this.splitPredicateString(pValidVariables, FilterPredicate);       
    
  }  
  
  // ########## mutator methods ##########
  
  public int getType() { return Type; }
  public String getVariable() { return Variable; }
  // ########## added by kwinkler 11/99 ###########
  public void setPredicateClass(int pPredicateClass) {
    if ( (pPredicateClass == VARIABLE_PREDICATE) || 
      (pPredicateClass == WILDCARD_PREDICATE) )
      PredicateClass = pPredicateClass;
    else
      PredicateClass = UNKNOWN_PREDICATE;
  }
  public int getPredicateClass() { return PredicateClass; }

  // ########## added by tveit 8/99 ######### //
  public int getAttribute() { return Attribute; }
  public int getOperator() { return Operator; }
  public Object getValue() { return Value; }
  public boolean existsExpression() { 
    if ( Expression != null ) 
      return true;
    else 
      return false;
  }
  public MintPredicateExpression getExpression() {
    if (Expression != null) 
      return Expression;
    else
      return null;
  }  

  // ########## standard methods ##########
  
  public String toString() {
  
    String vResult = "MintPredicate: Class=" + PredicateClass + 
      ", Type=" + Type + ", " + PredicateString + ", Attribute=" + 
      Attribute + ", AttributeType=" + AttributeType + ", Operator=" + 
      Operator + ", Value=" + Value.toString();
    if (Expression != null) 
      vResult += "\n   Expression: " + Expression.toString();
      
    return vResult;
  
  }  // toString()

/**
 * sets all attributes of MintPredicate by parsing PredicateString
 * @param pValidVariables Array containing all valid variable names in the
 * query context, used for syntax check
 * @exception MintSyntaxErrorException if PredicateString doesn't
 * conform to the MINT syntax
 */  
  
  private void splitPredicateString(Vector pValidVariables, 
    boolean pFilterPredicate) throws MintSyntaxErrorException {
  
    String vPredicateStringTemp = PredicateString;
    String vExpressionString = null;

    // wildcard predicate vs. variable predicate
    if ( ( PredicateString.startsWith("*.") ) || 
       ( PredicateString.toLowerCase().startsWith("wildcard.") ) ) {
      PredicateClass = WILDCARD_PREDICATE;
      if ( PredicateString.startsWith("*.") )
        PredicateString = PredicateString.substring(2).trim();
      else
        PredicateString = PredicateString.substring(9).trim();
    }
    else
      PredicateClass = VARIABLE_PREDICATE;
    
    if ( PredicateString.startsWith("(") ) {
    
      // (1) predicate with an expression on the lefthand side
      
      int vBracketIndex = PredicateString.indexOf(")");
      if (vBracketIndex < 0)
        throw new MintSyntaxErrorException( 
          "Missing closing bracket in predicate expression \"" + 
          vPredicateStringTemp + "\"!");
      else
        try {
          vExpressionString = 
            PredicateString.substring(1, vBracketIndex ).trim();
        }
        catch (IndexOutOfBoundsException e1) {
          throw new MintSyntaxErrorException(
            "Predicate expression \"" + PredicateString + "\" is not valid!");
        }
      
      Expression = new MintPredicateExpression(vExpressionString, 
      pValidVariables);
      Attribute = ATTR_EXPRESSION;
      AttributeType = Expression.getResultType();
      Type = Expression.getExpressionType();
        
      try {  
        PredicateString = PredicateString.substring( 
          PredicateString.indexOf(")") + 1 ).trim();
      }
      catch (IndexOutOfBoundsException e1) {
        throw new MintSyntaxErrorException( 
          "Predicate expression \"" + vPredicateStringTemp + 
          "\" is not valid!");
      }
      
    }
    else {
    
      // (2) predicate with an VariableName.Attribute on the lefthand side
      
      String vAttributeString = "";
      try {
        Variable = PredicateString.substring( 
          0, PredicateString.indexOf(".") ).trim();
        PredicateString = PredicateString.substring( 
          PredicateString.indexOf(".") + 1 ).trim();
        vAttributeString = PredicateString.substring( 
          0, PredicateString.indexOf(" ") ).trim();
        PredicateString = PredicateString.substring( 
          PredicateString.indexOf(" ") + 1 ).trim();
      }
      catch (IndexOutOfBoundsException e1) {
        throw new MintSyntaxErrorException(
          "Predicate expression \"" + vPredicateStringTemp + 
          "\" is not valid!");   
      }
      
      if ( ! pValidVariables.contains(Variable) )  
        throw new MintSyntaxErrorException(
          "Variable " + Variable + " in predicate expression \"" + 
          vPredicateStringTemp + "\" is not valid!"); 
      
      boolean vValidAttribute = false;
      for (int i = 0; i < AttributeStrings.length; i++)
        if ( vAttributeString.equals( AttributeStrings[i] ) ) {
          Attribute = Attributes[i];
          AttributeType = AttributeTypes[i];
          Type = AttributePredicateTypes[i];
          vValidAttribute = true;
          break;
        }
      if ( ! vValidAttribute )  
        throw new MintSyntaxErrorException(
          "Attribute " + vAttributeString + " in predicate expression \"" + 
          vPredicateStringTemp + "\" is not valid!"); 
      
    }  
    
    String vOperatorString = "";
    String vValueString = "";      
    try {
      vOperatorString = PredicateString.substring( 
        0, PredicateString.indexOf(" ") ).trim();
      PredicateString = PredicateString.substring( 
        PredicateString.indexOf(" ") + 1 ).trim();
      vValueString = PredicateString.substring( 
        PredicateString.indexOf(" ") + 1 ).trim();              
    }
    catch (IndexOutOfBoundsException e1) {
      throw new MintSyntaxErrorException(
        "Predicate expression \"" + vPredicateStringTemp + "\" is not valid!");
    }
    
    boolean vValidOperator = false;
    for (int i = 0; i < OperatorStrings.length; i++)
      if ( vOperatorString.equals( OperatorStrings[i] ) ) {
        Operator = Operators[i];
        vValidOperator = true;
        break;
      }
    if ( ! vValidOperator )  
      throw new MintSyntaxErrorException(
        "Operator " + vOperatorString + " in predicate expression \"" + 
        vPredicateStringTemp + "\" is not valid!");      
    
    try {
      switch (AttributeType) {
        case TYPE_INT: 
          Value = new Integer(vValueString); break;
        case TYPE_LONG: 
          Value = new Long(vValueString); break;
        case TYPE_DOUBLE: 
          Value = new Double(vValueString); break;
        case TYPE_STRING: // without ""
          Value = vValueString.substring(1, vValueString.length() - 1); break;
      }
    }
    catch (IndexOutOfBoundsException e1) {
      throw new MintSyntaxErrorException(
        "Predicate expression \"" + vPredicateStringTemp + "\" is not valid!");
    }
    catch (NumberFormatException e2) {
      throw new MintSyntaxErrorException(
        "Predicate expression \"" + vPredicateStringTemp + "\" is not valid!");
    }   
    
    // kwinkler 8/2000: check whether predicate contains correct attribute
    // according to its general type: filter vs. query predicate
    if ( (! pFilterPredicate) && (Type == FILTER_PREDICATE) )
    throw new MintSyntaxErrorException(
      "MINT query predicate expression \"" + vPredicateStringTemp + 
      "\" uses attributes only allowed in filter predicates!");

    // kwinkler 8/2000: filer predicates must not be expression predicates
    // or predicates on wildcards
    if ( (pFilterPredicate) && (AttributeType == ATTR_EXPRESSION) )
    throw new MintSyntaxErrorException(
      "The filter predicate \"" + vPredicateStringTemp + 
      "\" must not contain an expression!");
    if ( (pFilterPredicate) && (PredicateClass == WILDCARD_PREDICATE) )
    throw new MintSyntaxErrorException(
      "The filter predicate \"" + vPredicateStringTemp + 
      "\" must not be a predicate on wildcardsa!");

    PredicateString = vPredicateStringTemp;      
  
  }  // splitPredicateString()

  /**
   * tests whether this MintPredicate is matched by the given Page and 
   * Occurrence; works only if Type == CONTENT_PREDICATE 
   * @param pPage Page that is to be tested for match with this MintPredicate
   * @param pOccurrence occurrence number of pPage in a Trail
   * @return true if (Page, Occurrence) match this MintPredicate or null if 
   * thereis no match or TYPE == STATISTICS_PREDICATE
   */
  
  public boolean isMatchedByPage(Page pPage, int pOccurrence) {
  
    if (Type != CONTENT_PREDICATE) return false;
  
    vResult = false; oCompareValue = null;

    switch (Attribute) {
      case ATTR_URL: {
        oCompareValue = new String( pPage.getUrl() );
        vResult = CompareStringToValue( (String)oCompareValue );
        break;
      }
      case ATTR_ID: {
        oCompareValue = new Long( pPage.getID() );
        vResult = CompareLongToValue( (Long)oCompareValue );
        break;
      }
      case ATTR_ACCESSES: {
        oCompareValue = new Integer( pPage.getAccesses() );
        vResult = CompareIntegerToValue( (Integer)oCompareValue );
        break;
      }
      case ATTR_OCCURRENCE: {
        oCompareValue = new Integer( pOccurrence );
        vResult = CompareIntegerToValue( (Integer)oCompareValue );
        break;
      }      
    }
    
    return vResult;
  
  }  // isMatchedByPage()
  
  /**
   * tests whether this MintPredicate is matched by the given 
   * MintPatternOutput 
   * @return true if MintPatternOutput matches this MintPredicate
   */
  
  public boolean isMatchedByPatternOutput(MintPatternOutput pPatternOutput,
    MintQuery pQuery) {
  
    int variableIndex = pQuery.getVariableIndex(Variable);
    if ( (variableIndex < 0) || (variableIndex >= pPatternOutput.getLength()) )
      // given variable does not exist in the given pattern 
      return false; 

    vResult = false; oCompareValue = null;

    switch (Attribute) {
      case ATTR_URL: {
        oCompareValue = new String( pPatternOutput.getPageUrl(variableIndex) );
        vResult = CompareStringToValue( (String)oCompareValue );
        break;
      }
      case ATTR_ID: {
        oCompareValue = new Long( pPatternOutput.getPageID(variableIndex) );
        vResult = CompareLongToValue( (Long)oCompareValue );
        break;
      }
      case ATTR_ACCESSES: {
        oCompareValue = new Integer( 
          pPatternOutput.getAccesses(variableIndex) );
        vResult = CompareIntegerToValue( (Integer)oCompareValue );
        break;
      }
      case ATTR_OCCURRENCE: {
        oCompareValue = new Integer( 
          pPatternOutput.getOccurrence(variableIndex) );
        vResult = CompareIntegerToValue( (Integer)oCompareValue );
        break;
      }      
      case ATTR_SUPPORT: {
        oCompareValue = new Long( pPatternOutput.getSupport(variableIndex) );
        vResult = CompareLongToValue( (Long)oCompareValue );
        break;
      }      
      case ATTR_CONFIDENCE: {
        oCompareValue = new Double( 
          pPatternOutput.getConfidence(variableIndex) );
        vResult = CompareDoubleToValue( (Double)oCompareValue );
        break;
      }      
      case ATTR_TITLE: {
        oCompareValue = new String( 
          pPatternOutput.getPageTitle(variableIndex) );
        vResult = CompareStringToValue( (String)oCompareValue );
        break;
      }
      case ATTR_RELSUPPORT: {
        oCompareValue = new Double( 
          pPatternOutput.getRelativeSupport(variableIndex) );
        vResult = CompareDoubleToValue( (Double)oCompareValue );
        break;
      }      
      case ATTR_ROOTCONFIDENCE: {
        oCompareValue = new Double( 
          pPatternOutput.getRootConfidence(variableIndex) );
        vResult = CompareDoubleToValue( (Double)oCompareValue );
        break;
      }      
    }
    
    return vResult;
  
  }  // isMatchedByPatternOutput()
  
  /**
   * tests whether this MintPredicate is matched by the given 
   * MintPatternDescriptor; works only if Type == STATISTICS_PREDICATE; 
   * currently MintPredicateExpressions 
   * are only tested if pPatternDescriptor contains all template variables
   * @param pWUM WUM context for the WUM query and its template
   * @param pPatternDescriptor MintPatternDescriptor that is to be tested for 
   * match with this MintPredicate
   * @param pTemplate MintTemplate context for this MintPredicate
   * @return true if (pPatternDescriptor match this MintPredicate or null if 
   * there is no match or TYPE == CONTENT_PREDICATE
   */
  
  public boolean isMatchedByPatternDescriptor(MiningBase pMiningBase,
    MintPatternDescriptor pPatternDescriptor, MintTemplate pTemplate) {
  
    if (Type != STATISTICS_PREDICATE) return false;
    
    vResult = false; oCompareValue = null;
    
    switch (Attribute) {
      case ATTR_EXPRESSION: {
        if ( pPatternDescriptor.getLength() < pTemplate.countVariableNames() )
          return true;  // sub-patterns are not testet yet
          
        if (Expression != null)
          Expression.calculateResult(pMiningBase, pPatternDescriptor, 
            pTemplate);
        else
          return false;
        switch (AttributeType) {
          case TYPE_INT: 
            oCompareValue = Expression.getResultInteger();
            if (oCompareValue != null)
              vResult = CompareIntegerToValue( (Integer)oCompareValue ); 
            else
              vResult = false;
            break;
          case TYPE_LONG: 
            oCompareValue = Expression.getResultLong();
            if (oCompareValue != null)
              vResult = CompareLongToValue( (Long)oCompareValue ); 
             else
              vResult = false;
            break;
          case TYPE_DOUBLE: 
            oCompareValue = Expression.getResultDouble();
            if (oCompareValue != null)
              vResult = CompareDoubleToValue( (Double)oCompareValue ); 
            else
              vResult = false;
            break;
            
        }        
        break;
      }
      case ATTR_SUPPORT: {
        // position of required Variable in Template =
        // required position in pattern descriptor
        int vPosition = pTemplate.getPositionOfVariable(Variable);
        if (vPosition == -1) vResult = false;
        else
          if ( vPosition >= pPatternDescriptor.getLength() )
            // test not necessary, variable is not in descriptor
            vResult = true;  
          else {          
            oCompareValue = new Long( 
              pPatternDescriptor.getSupport(vPosition) );
            vResult = CompareLongToValue( (Long)oCompareValue );
            break;
          }
      }
    }
    
    return vResult;
  
  }  // isMatchedByPatternDescriptor()  

  // ########## methods comparing parameter with Value  ##########  

  private boolean CompareIntegerToValue(Integer pCompareInteger) {
  
    // returns: true if comparison (pInteger Operator Value) is true
    if (AttributeType != TYPE_INT) return false;
  
    vResultComparison = false;
    vIntCompareValue = pCompareInteger.intValue();
    vIntValue = ( (Integer)Value ).intValue();
    
    switch (Operator) {
      case OP_LESS_EQUAL: { 
        if (vIntCompareValue <= vIntValue) vResultComparison = true; break; } 
      case OP_LESS: { 
        if (vIntCompareValue < vIntValue) vResultComparison = true; break; }
      case OP_EQUAL: { 
        if (vIntCompareValue == vIntValue) vResultComparison = true; break; }
      case OP_MORE: { 
        if (vIntCompareValue > vIntValue) vResultComparison = true; break; }
      case OP_MORE_EQUAL: { 
        if (vIntCompareValue >= vIntValue) vResultComparison = true; break; }
      case OP_NOT_EQUAL: { 
        if (vIntCompareValue != vIntValue) vResultComparison = true; break; }
    }
  
    return vResultComparison;
  
  }  // CompareIntegerToValue()
  
  private boolean CompareLongToValue(Long pCompareLong) {
  
    // returns: true if comparison (pInteger Operator Value) is true
    if (AttributeType != TYPE_LONG) return false;
  
    vResultComparison = false;
    vLongCompareValue = pCompareLong.longValue();
    vLongValue = ( (Long)Value ).longValue();
    
    switch (Operator) { 
      case OP_LESS_EQUAL: { 
        if (vLongCompareValue <= vLongValue) vResultComparison = true; break; } 
      case OP_LESS: { 
        if (vLongCompareValue < vLongValue) vResultComparison = true; break; }
      case OP_EQUAL: { 
        if (vLongCompareValue == vLongValue) vResultComparison = true; break; }
      case OP_MORE: { 
        if (vLongCompareValue > vLongValue) vResultComparison = true; break; }
      case OP_MORE_EQUAL: { 
        if (vLongCompareValue >= vLongValue) vResultComparison = true; break; }
      case OP_NOT_EQUAL: { 
        if (vLongCompareValue != vLongValue) vResultComparison = true; break; }
    }
  
    return vResultComparison;
  
  }  // CompareLongToValue()

  private boolean CompareDoubleToValue(Double pCompareDouble) {
  
    // returns: true if comparison (pInteger Operator Value) is true
    if (AttributeType != TYPE_DOUBLE) return false;
  
    vResultComparison = false;
    vDoubleCompareValue = pCompareDouble.doubleValue();
    vDoubleValue = ( (Double)Value ).doubleValue();
    
    switch (Operator) {
      case OP_LESS_EQUAL: { 
        if (vDoubleCompareValue <= vDoubleValue) 
          vResultComparison = true; break; } 
      case OP_LESS: { 
        if (vDoubleCompareValue < vDoubleValue) 
          vResultComparison = true; break; }
      case OP_EQUAL: { 
        if (vDoubleCompareValue == vDoubleValue) 
          vResultComparison = true; break; }
      case OP_MORE: { 
        if (vDoubleCompareValue > vDoubleValue) 
          vResultComparison = true; break; }
      case OP_MORE_EQUAL: { 
        if (vDoubleCompareValue >= vDoubleValue) 
          vResultComparison = true; break; }
      case OP_NOT_EQUAL: { 
        if (vDoubleCompareValue != vDoubleValue) 
          vResultComparison = true; break; }
    }
  
    return vResultComparison;
  
  }  // CompareDoubleToValue()

  private boolean CompareStringToValue(String pCompareString) {
  
    // returns: true if comparison (pInteger Operator Value) is true
    if (AttributeType != TYPE_STRING) return false;
  
    vResultComparison = false;
    vStringCompareValue = pCompareString.trim();
    vStringValue = (String)Value;
    
    switch (Operator) {
      case OP_LESS_EQUAL: {
        if ( vStringCompareValue.compareTo(vStringValue) <= 0 ) 
          vResultComparison = true; break;        
      } 
      case OP_LESS: {
        if ( vStringCompareValue.compareTo(vStringValue) < 0 ) 
          vResultComparison = true; break;
      }
      case OP_EQUAL: {
        if ( vStringCompareValue.equals(vStringValue) ) 
          vResultComparison = true; break;
      }
      case OP_MORE: {
        if ( vStringCompareValue.compareTo(vStringValue) > 0 ) 
          vResultComparison = true; break;
      }
      case OP_MORE_EQUAL: {
        if ( vStringCompareValue.compareTo(vStringValue) >= 0 ) 
          vResultComparison = true; break;
      }
      case OP_NOT_EQUAL: {
        if ( ! vStringCompareValue.equals(vStringValue) ) 
          vResultComparison = true; break;
      }
      case OP_STARTSWITH: {
        if ( vStringCompareValue.startsWith(vStringValue) ) 
          vResultComparison = true; break;
      }
      case OP_CONTAINS: {
        if ( vStringCompareValue.indexOf(vStringValue) >= 0 ) 
          vResultComparison = true; break;
      }
      case OP_ENDSWITH: {
        if ( vStringCompareValue.endsWith(vStringValue) ) 
          vResultComparison = true; break;
      }
      case OP_NOT_STARTSWITH: {
        if ( ! vStringCompareValue.startsWith(vStringValue) ) 
          vResultComparison = true; break;
      }
      case OP_NOT_CONTAINS: {
        if ( vStringCompareValue.indexOf(vStringValue) < 0 ) 
          vResultComparison = true; break;
      }
      case OP_NOT_ENDSWITH: {
        if ( ! vStringCompareValue.endsWith(vStringValue) ) 
          vResultComparison = true; break;
      }
      
    }
  
    return vResultComparison;
  
  }  // CompareStringToValue()

}  // class MintPredicate
