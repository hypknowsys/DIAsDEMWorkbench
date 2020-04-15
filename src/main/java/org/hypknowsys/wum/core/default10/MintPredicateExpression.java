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
 * A MintPredicateExpression represents an expression that may occur on the
 * lefthand side of a MintPredicate. It's enclosed by two brackets and 
 * contains two operands and one operator. The expression's result will be 
 * calculated and then compared with Value in the corresponding 
 * MintPredicates. Syntax: <p>
 * "VariableName.Attribute Operator VariableName.Attribute" <p>
 * Attributes = ("accesses" = total integer number of a Page's accesses;
 * "occurrence" = page occurrence integer number in a Trail; "support" = 
 * support value of a PageOccurrence in an observation as a long number).
 * Operators = ("+"; "-"; "*"; "/"). Currently, each MintPredicateExpression 
 * is of Type STATISTICS_PREDICATE and the ResultType is always TYPE_DOUBLE. 
 * <p>
 *
 * Modified by tveit 5/99: accessor methods <p>
 *
 * Modified by kwinkler 6/99: ( a.url - b.url ) = 0 feature added
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MintPredicateExpression implements Serializable {

  // ########## attributes and constants ##########

  private String PredicateExpressionString = null;
  private int Type = UNKNOWN_PREDICATE_EXPRESSION;
  private String VariableLeft = null;
  private int AttributeLeft = ATTR_UNKNOWN;
  private int AttributeTypeLeft = TYPE_UNKNOWN;
  private Object AttributeValueLeft = null;
  private int Operator = OP_UNKNOWN;
  private String VariableRight = null;
  private int AttributeRight = ATTR_UNKNOWN;
  private int AttributeTypeRight = TYPE_UNKNOWN;
  private Object AttributeValueRight = null;
  private Object Result = null;
  private int ResultType = TYPE_UNKNOWN;
  
  private static final int ATTR_UNKNOWN = 0;
  private static final int ATTR_ACCESSES = 3;
  private static final int ATTR_OCCURRENCE = 4;
  private static final int ATTR_SUPPORT = 5;
  private static final int ATTR_URL = 6;
  
  private static final int TYPE_UNKNOWN = 0;
  private static final int TYPE_INT = 1;
  private static final int TYPE_LONG = 2;
  private static final int TYPE_DOUBLE = 3;
  
  public static final int UNKNOWN_PREDICATE_EXPRESSION = 0;
  public static final int CONTENT_PREDICATE_EXPRESSION = 1;
  public static final int STATISTICS_PREDICATE_EXPRESSION = 2;
  
  private static final String[] AttributeStrings = 
    { "accesses", "occurrence", "support", "url" };
  private static final int[] Attributes =
    { ATTR_ACCESSES, ATTR_OCCURRENCE, ATTR_SUPPORT, ATTR_URL };
  private static final int[] AttributeTypes =
    { TYPE_INT, TYPE_INT, TYPE_LONG, TYPE_INT };
    
  private static final int OP_UNKNOWN = -100;  
  private static final int OP_PLUS = 0;
  private static final int OP_MINUS = 1;
  private static final int OP_DIVIDED_BY = 2;
  private static final int OP_MULTIPLIED_BY = 3;
  private static final String[] OperatorStrings = 
    { "+", "-", "/", "*" };
  private static final int[] Operators = 
    { OP_PLUS, OP_MINUS, OP_DIVIDED_BY, OP_MULTIPLIED_BY };

  // ########## constructors ##########

/**
 * constructs an empty MintPredicateExpression, type is unknown
 */  
  
  public MintPredicateExpression() {

    PredicateExpressionString = "";
    Type = UNKNOWN_PREDICATE_EXPRESSION;
    VariableLeft = "";
    AttributeLeft = ATTR_UNKNOWN;
    AttributeTypeLeft = TYPE_UNKNOWN;
    Operator = OP_UNKNOWN;
    VariableRight = "";
    AttributeRight = ATTR_UNKNOWN;
    AttributeTypeRight = TYPE_UNKNOWN;
    Result = null;
    ResultType = TYPE_UNKNOWN;
    
  }  
  
/**
 * constructs an MintPredicateExpression, all attributes are set
 * @param pPredicateExpressionString String containing one predicate expression
 * @param pValidVariables Array containing all valid variable names in the
 * query context, used for syntax check
 * @exception MintSyntaxErrorException if pPredicateExpressionString doesn't
 * conform to the MINT syntax
 */  

  public MintPredicateExpression(String pPredicateExpressionString,
    Vector pValidVariables) throws MintSyntaxErrorException {

    PredicateExpressionString = pPredicateExpressionString;
    Type = UNKNOWN_PREDICATE_EXPRESSION;
    VariableLeft = "";
    AttributeLeft = ATTR_UNKNOWN;
    AttributeTypeLeft = TYPE_UNKNOWN;
    AttributeValueLeft = null;
    Operator = OP_UNKNOWN;
    VariableRight = "";
    AttributeRight = ATTR_UNKNOWN;
    AttributeTypeRight = TYPE_UNKNOWN;
    AttributeValueRight = null;
    Result = null;
    ResultType = TYPE_UNKNOWN;
    
    this.splitPredicateExpressionString(pValidVariables); 
    
  }  
  
  // ########## accessor methods ##########
  
  public int getResultType() { return ResultType; }
  public int getExpressionType() { return Type; }
  //########## tveit #######
  public String getLeftVariableName() { return VariableLeft; }
  public String getRightVariableName() { return VariableRight; }
  public int getLeftAttribute() { return AttributeLeft; }
  public int getRightAttribute() { return AttributeRight; }
  public int getOperatorOfExpression() { return Operator; }

 
  // ########## standard methods ##########
  
  public String toString() {
  
    String vResult = "MintPredicateExpression: Type=" + Type + ", " + 
      PredicateExpressionString + 
      ", (" + AttributeLeft + " " + AttributeTypeLeft + ") " + 
      Operator + 
      ", (" + AttributeRight + " " + AttributeTypeRight +
      ")";
    if (Result != null) 
      vResult += ", " + Result.toString();
      
    return vResult;
  
  }  // toString()
 
/**
 * sets all attributes of MintPredicateExpression by parsing 
 * PredicateExpressionString
 * @param pValidVariables Array containing all valid variable names in the
 * query context, used for syntax check
 * @exception MintSyntaxErrorException if PredicateExpressionString doesn't
 * conform to the MINT syntax
 */  
  
  private void splitPredicateExpressionString(Vector pValidVariables) 
    throws MintSyntaxErrorException {
    
    // examples: "a.support / b.support" or "a.accesses + b.accesses"
    // "a.url - b.url = 0 => test on equality
  
    String vPredicateExpressionStringTemp = PredicateExpressionString.trim();
        
    Itemizer oItemizer = new Itemizer(PredicateExpressionString);
    String vOperandLeftString = oItemizer.getNextItem().trim();
    String vOperatorString = oItemizer.getNextItem().trim();
    String vOperandRightString = oItemizer.getNextItem().trim();
    if ( (vOperandLeftString == null) || (vOperatorString == null) ||
      (vOperandRightString == null) )
      throw new MintSyntaxErrorException(
         "Predicate expression \"" + vPredicateExpressionStringTemp +
         "\" is not valid!");      
    
    String vAttributeLeftString = "";
    String vAttributeRightString = "";
    try {
      VariableLeft = vOperandLeftString.substring(0,
        vOperandLeftString.indexOf(".") ).trim();
      VariableRight = vOperandRightString.substring(0,
        vOperandRightString.indexOf(".") ).trim();
      vAttributeLeftString = vOperandLeftString.substring(
        vOperandLeftString.indexOf(".") + 1 ).trim();
      vAttributeRightString = vOperandRightString.substring(
        vOperandRightString.indexOf(".") + 1 ).trim();      
    }
    catch (IndexOutOfBoundsException e1) {
      throw new MintSyntaxErrorException(
       "1Predicate expression \"" + vPredicateExpressionStringTemp +
       "\" is not valid!");      
    }      
    
    boolean vValidAttribute = false;
    for (int i = 0; i < AttributeStrings.length; i++)
      if ( vAttributeLeftString.equals( AttributeStrings[i] ) ) {
        AttributeLeft = Attributes[i];
        AttributeTypeLeft = AttributeTypes[i];
        vValidAttribute = true;
        break;
      }
    if ( ! vValidAttribute )  
      throw new MintSyntaxErrorException(
        "2Atribute " + vAttributeLeftString + " in predicate expression \"" + 
        vPredicateExpressionStringTemp + "\" is not valid!"); 
    vValidAttribute = false;
    for (int i = 0; i < AttributeStrings.length; i++)
      if ( vAttributeRightString.equals( AttributeStrings[i] ) ) {
        AttributeRight = Attributes[i];
        AttributeTypeRight = AttributeTypes[i];
        vValidAttribute = true;
        break;
      }
    if ( ! vValidAttribute )  
      throw new MintSyntaxErrorException(
        "Attribute " + vAttributeRightString + " in predicate expression \"" + 
        vPredicateExpressionStringTemp + "\" is not valid!"); 
   
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
        vPredicateExpressionStringTemp + "\" is not valid!");         

    // can't really calculate with URLs, therefore checking syntax
    if ( ( ( AttributeLeft == ATTR_URL ) || ( AttributeRight == ATTR_URL ) ) &&
       ( ! ( AttributeLeft == AttributeRight ) ) )
      throw new MintSyntaxErrorException("Attribute url can only be used " +
        "for a test on equality: ( variable.url - variable.url ) = 0"); 
      
    // restriction can be relaxed later
    ResultType = TYPE_DOUBLE;
    // restriction can be relaxed later
    Type = STATISTICS_PREDICATE_EXPRESSION;  
    
    PredicateExpressionString = vPredicateExpressionStringTemp;      
  
  }  // splitPredicateExpressionString()

  /**
   * calculates the MintPredicatExpression's Result, must be called before 
   * calling this.getResultDouble();
   * @param pWUM WUM context for the WUM query and its template
   * @param pPatternDescriptor MintPatternDescriptor that is to be tested 
   * for match with this MintPredicate
   * @param pTemplate MintTemplate context for this MintPredicate
   */
  
  public void calculateResult(MiningBase pMiningBase, 
    MintPatternDescriptor pPatternDescriptor, MintTemplate pTemplate) {
  
    if (Type != STATISTICS_PREDICATE_EXPRESSION) {
      Result = null;
      return;
    }
  
    int vPositionLeft = pTemplate.getPositionOfVariable(VariableLeft);
    if (vPositionLeft == -1)  {  // variable doesn't exist
      Result = null;
      return;
    }

    switch (AttributeLeft) {
      case ATTR_ACCESSES: {
        PageOccurrence oPageOccurrenceLeft = 
          pPatternDescriptor.getPageOccurrence(vPositionLeft);
        Page oPageLeft = pMiningBase.getPage( 
          oPageOccurrenceLeft.getPageID() );
        AttributeValueLeft = new Integer( oPageLeft.getAccesses() );     
        break;
      }
      case ATTR_OCCURRENCE: {
        PageOccurrence oPageOccurrenceLeft = 
          pPatternDescriptor.getPageOccurrence(vPositionLeft);
        AttributeValueLeft = new Integer( 
          oPageOccurrenceLeft.getOccurrence() );
        break;
      }      
      case ATTR_SUPPORT: {
        AttributeValueLeft = new Long(       
          pPatternDescriptor.getSupport(vPositionLeft) );
        break;
      }
      case ATTR_URL: {
        PageOccurrence oPageOccurrenceLeft = 
          pPatternDescriptor.getPageOccurrence(vPositionLeft);
        Page oPageLeft = pMiningBase.getPage( 
          oPageOccurrenceLeft.getPageID() );
        AttributeValueLeft = new String( oPageLeft.getUrl() );     
        break;
      }      
    }
    
    int vPositionRight = pTemplate.getPositionOfVariable(VariableRight);
    if (vPositionRight == -1)  {  // variable doesn't exist
      Result = null;
      return;
    }
    
    switch (AttributeRight) {
      case ATTR_ACCESSES: {
        PageOccurrence oPageOccurrenceRight = 
          pPatternDescriptor.getPageOccurrence(vPositionRight);
        Page oPageRight = pMiningBase.getPage( 
          oPageOccurrenceRight.getPageID() );
        AttributeValueRight = new Integer( oPageRight.getAccesses() );
        break;
      }
      case ATTR_OCCURRENCE: {
        PageOccurrence oPageOccurrenceRight = 
          pPatternDescriptor.getPageOccurrence(vPositionRight);
        AttributeValueRight = new Integer( 
          oPageOccurrenceRight.getOccurrence() );
        break;
      }      
      case ATTR_SUPPORT: {
        AttributeValueRight = new Long(       
          pPatternDescriptor.getSupport(vPositionRight) );
        break;
      }      
      case ATTR_URL: {
        PageOccurrence oPageOccurrenceRight = 
          pPatternDescriptor.getPageOccurrence(vPositionRight);
        Page oPageRight = pMiningBase.getPage( 
          oPageOccurrenceRight.getPageID() );
        AttributeValueRight = new String( oPageRight.getUrl() );     
        break;
      }      
    }

    // Modified by kwinkler 6/99: ( left.url - right.url ) = 0, Due to 
    // previous algorithm, the result cannot be of type String,
    // therefore integer numbers are assigned according to string test on 
    // equality:
    // left.url == right.url: AttributeValueLeft=1 and AttributeValueRight=1
    // left.url == right.url: AttributeValueLeft=1 and AttributeValueRight=100
    
    if ( (AttributeLeft == ATTR_URL) && (AttributeRight == ATTR_URL) ) {
      if ( ( (String)AttributeValueRight ).compareTo( 
        (String)AttributeValueLeft  ) == 0  ) {
        AttributeValueLeft = new Integer(1);
        AttributeValueRight = new Integer(1);
      }
      else {
        AttributeValueLeft = new Integer(1);
        AttributeValueRight = new Integer(100);
      }
    }
    
    // restrictions can be relaxed later
    double vAttributeValueLeft = 0.0;
    double vAttributeValueRight = 0.0;
    double vResult = 0.0;
    
    switch (AttributeTypeLeft) {
      case TYPE_INT: {
        vAttributeValueLeft = ( (Integer)AttributeValueLeft ).doubleValue();
        break;
      }
      case TYPE_LONG: {
        vAttributeValueLeft = ( (Long)AttributeValueLeft ).doubleValue();
        break;
      }
      case TYPE_DOUBLE: {
        vAttributeValueLeft = ( (Double)AttributeValueLeft ).doubleValue();
        break;
      }
    }
    switch (AttributeTypeRight) {
      case TYPE_INT: {
        vAttributeValueRight = ( (Integer)AttributeValueRight ).doubleValue();
        break;
      }
      case TYPE_LONG: {
        vAttributeValueRight = ( (Long)AttributeValueRight ).doubleValue();
        break;
      }
      case TYPE_DOUBLE: {
        vAttributeValueRight = ( (Double)AttributeValueRight ).doubleValue();
        break;
      }
    }
    
    switch (Operator) {
      case OP_PLUS: {
        vResult = vAttributeValueLeft + vAttributeValueRight;
        break;
      }
      case OP_MINUS: {
        vResult = vAttributeValueLeft - vAttributeValueRight;
        break;
      }
      case OP_DIVIDED_BY: {
        vResult = vAttributeValueLeft / vAttributeValueRight;
        break;
      }
      case OP_MULTIPLIED_BY: {
        vResult = vAttributeValueLeft * vAttributeValueRight;
        break;
      }      
    }
    
    Result = new Double(vResult);  // restriction can be relaxed later
    
  }  // calculateResult()

  /**
   * @return result of this predicate expression or null if Type != TYPE_INT,
   * currently no useable: Type = TYPE_DOUBLE always
   */  
  
  public Integer getResultInteger() {
  
    if ( (Result != null) && (ResultType == TYPE_INT) )
      return (Integer)Result;
    else
      return null;
  
  }  // getResultInteger()
  
  /**
   * @return result of this predicate expression or null if Type != TYPE_LONG,
   * currently no useable: Type = TYPE_DOUBLE always
   */    
  
  public Long getResultLong() {
  
    if ( (Result != null) && (ResultType == TYPE_LONG) )
      return (Long)Result;
    else
      return null;
  
  }  // getResultLong()

  /**
   * @return result of this predicate expression or null if 
   * Type != TYPE_DOUBLE,
   */  
  
  public Double getResultDouble() {
  
    if ( (Result != null) && (ResultType == TYPE_DOUBLE) )
      return (Double)Result;
    else
      return null;
  
  }  // getResultDouble()
  
}  // class MintPredicateExpression

