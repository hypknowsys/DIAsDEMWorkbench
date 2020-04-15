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
 * A MintPredicate represents one attribute in the MINT query's order 
 * byclause. Currently only used to sort MintPatternOutput. Syntax:
 * variable.attribute [asc|desc]
 * 
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MintOrderByAttribute implements Serializable {

  // ########## attributes and constants ##########

  private String OrderByString = null;
  private String Variable = null;
  private int VariableIndex = 0;
  private int Attribute = ATTR_UNKNOWN;
  private int AttributeType = TYPE_UNKNOWN;
  private int OrderByType = ORDER_UNKNOWN;
  
  public static final int ORDER_UNKNOWN = MintOrderByKey.UNKNOWN;
  public static final int ORDER_ASC = MintOrderByKey.ASC;
  public static final int ORDER_DESC = MintOrderByKey.DESC;      
  
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
      TYPE_DOUBLE, TYPE_STRING, TYPE_DOUBLE, TYPE_DOUBLE};

  // ########## constructors ##########

/**
 * @exception MintSyntaxErrorException if pPredicateString doesn't
 * conform to the MINT syntax
 */  

  public MintOrderByAttribute(String pOrderByString, Vector pValidVariables)
    throws MintSyntaxErrorException {

    OrderByString = pOrderByString.trim();   
    Variable = ""; VariableIndex = 0;
    Attribute = ATTR_UNKNOWN;   AttributeType = TYPE_UNKNOWN;
    
    this.splitOrderByString(pValidVariables);       
    
  }  
  
  // ########## accessor methods ##########
  
    public int getOrderByType() { return OrderByType; }

  // ########## mutator methods ##########
  

  // ########## standard methods ##########
  

/**
 * example of pValidVariables: "a.support desc" or "b.url"
 */  
  
  private void splitOrderByString(Vector pValidVariables) 
    throws MintSyntaxErrorException {
  
    String orderByStringTemp = OrderByString;
    String attributeString = "";

    try {
      Variable = OrderByString.substring( 
        0, OrderByString.indexOf(".") ).trim();
      OrderByString = OrderByString.substring( 
        OrderByString.indexOf(".") + 1 ).trim();
      if ( OrderByString.indexOf(" ") > 0) {
        attributeString = OrderByString.substring( 
          0, OrderByString.indexOf(" ") ).trim();
        OrderByString = OrderByString.substring( 
          OrderByString.indexOf(" ") + 1 ).trim();
      }
      else {
        attributeString = OrderByString.trim();
        OrderByString = "";
      }
    }
    catch (IndexOutOfBoundsException e1) {
      throw new MintSyntaxErrorException(
        "Order attribute \"" + orderByStringTemp + 
        "\" is not valid!");   
    }
      
    if ( ! pValidVariables.contains(Variable) )  
      throw new MintSyntaxErrorException(
        "Variable " + Variable + " in order attribute \"" + 
        orderByStringTemp + "\" is not valid!");
    else 
      VariableIndex = pValidVariables.indexOf(Variable);
    
    boolean validAttribute = false;
    for (int i = 0; i < AttributeStrings.length; i++)
      if ( attributeString.equals( AttributeStrings[i] ) ) {
        Attribute = Attributes[i];
        AttributeType = AttributeTypes[i];
        validAttribute = true;
        break;
      }
    if ( ! validAttribute )  
      throw new MintSyntaxErrorException(
        "Attribute " + attributeString + " in order attribute \"" + 
        orderByStringTemp + "\" is not valid!"); 
    
    String orderByTypeString = OrderByString.trim();
    if (orderByTypeString.length() == 0)  
      OrderByType = ORDER_ASC;
    else {
      if ( orderByTypeString.toLowerCase().equals("asc") )
        OrderByType = ORDER_ASC;
       else
         if ( orderByTypeString.toLowerCase().equals("desc") )
            OrderByType = ORDER_DESC;
         else
           throw new MintSyntaxErrorException(
           "Ordering in order attribute \"" + orderByStringTemp + 
           "\" is not valid!");
    }
  
    OrderByString = orderByStringTemp;      
  
  }  // splitOrderByString()

  /**
   * tests whether this MintOrderByAttribute is matched by the given 
   * MintPatternOutput 
   * @return true if MintPatternOutput matches this MintOrderByAttribute
   */
  
  public Object getOrderByValue(MintPatternOutput pPatternOutput) {
  
    if ( (VariableIndex < 0) || (VariableIndex >= pPatternOutput.getLength()) )
      // given variable does not exist in the given pattern 
      return null; 

    switch (Attribute) {
      case ATTR_URL: {
        return new String( pPatternOutput.getPageUrl(VariableIndex) );
      }
      case ATTR_ID: {
        return new Long( pPatternOutput.getPageID(VariableIndex) );
      }
      case ATTR_ACCESSES: {
        return new Integer( pPatternOutput.getAccesses(VariableIndex) );
      }
      case ATTR_OCCURRENCE: {
        return new Integer( pPatternOutput.getOccurrence(VariableIndex) );
      }      
      case ATTR_SUPPORT: {
        return new Long( pPatternOutput.getSupport(VariableIndex) );
      }      
      case ATTR_CONFIDENCE: {
        return new Double( pPatternOutput.getConfidence(VariableIndex) );
      }      
      case ATTR_TITLE: {
        return new String( pPatternOutput.getPageTitle(VariableIndex) );
      }
      case ATTR_RELSUPPORT: {
        return new Double( pPatternOutput.getRelativeSupport(VariableIndex) );
      }      
      case ATTR_ROOTCONFIDENCE: {
        return new Double( pPatternOutput.getRootConfidence(VariableIndex) );
      }      
    }
    
    return null;
  
  }  // isMatchedByPatternOutput()
  
}  // class MintOrderByAttribute
