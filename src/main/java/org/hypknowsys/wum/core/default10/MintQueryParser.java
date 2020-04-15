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
 * The MintQueryParser parses a String containing a MINT query and creates
 * a corresponding instance of the class MintQuery. A MintSyntaxErrorException
 * will be trown, if the query String doesn't conform to the MINT syntax.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MintQueryParser implements Serializable {
  
  private MintQuery Query = null;
  
  private String QueryString = null;
  private String QueryStringLowerCase = null;
  
  // general parts of query
  private String SelectClause = null;
  private String FromClause = null;
  private String FromClauseLowerCase = null;
  private String WhereClause = null;
  private String WhereClauseLowerCase = null;
    
  // specific parts of query
  private String VariableClause = null;
  private String TemplateClause = null;
  private String TemplateClauseLowerCase = null;
  private int CounterVariables = 0;
  private Vector VariableNames = null;
  private String TemplateName = null;
  private String TemplateString = null;
  private MintTemplate oTemplate = null;
  private int CounterPredicates = 0;
  private Vector Predicates = null;
  
  private Itemizer oItemizer = null;
  private String vBuffer = null;
  
  private final static String ERROR = ""; // "MINT syntax error:\n";

  // ########## comments will follow ##########


  // ########## comments will follow ##########

  public MintQueryParser() {}  
  
  // ########## comments will follow ##########

  public MintQuery parseQueryString(CheckMintSyntaxParameter pParameter)
    throws MintSyntaxErrorException {

    return this.parseQueryString( pParameter.getQueryString() );
 
  }

  // ########## comments will follow ##########

  public MintQuery parseQueryString(String pQueryString)
    throws MintSyntaxErrorException {
    
    if (pQueryString == null) 
      throw new MintSyntaxErrorException(ERROR + "Query String is null!");
    
    QueryString = pQueryString.trim();
    if (QueryString.length() == 0) 
      throw new MintSyntaxErrorException(ERROR + "Query String is empty!");
    
    QueryStringLowerCase = pQueryString.trim().toLowerCase();
    
    this.splitIntoMainParts();
    this.splitFromClause();
    this.splitSelectClause();
    this.splitWhereClause();
    
    Query = new MintQuery(pQueryString);
    Query.setTemplate(oTemplate);
    
    MintVariable[] oVariables = new MintVariable[ VariableNames.size() ];
    for (int i = 0; i < oVariables.length; i++)
      oVariables[i] = new MintVariable( (String)VariableNames.elementAt(i) );
    Query.setVariables(oVariables);
    
    if (Predicates != null) {
      MintPredicate[] oPredicates = new MintPredicate[ Predicates.size() ];
      for (int i = 0; i < oPredicates.length; i++)
        oPredicates[i] = (MintPredicate)Predicates.elementAt(i);
      Query.setPredicates(oPredicates);        
      Query.allocatePredicates();
    }
    
    return Query;
    
  }  // parseQueryString()
  
  // ########## comments will follow ##########
  
  private void splitIntoMainParts() throws MintSyntaxErrorException {
  
    int vSelectIndex = QueryStringLowerCase.indexOf("select");
    if (vSelectIndex < 0)
      throw new MintSyntaxErrorException(ERROR + 
        "\"select\" statement is missing!");   
    
    int vFromIndex = QueryStringLowerCase.indexOf("from");
    if (vFromIndex < 0)
      throw new MintSyntaxErrorException(ERROR + 
        "\"from\" statement is missing!");
      
    try {
      SelectClause = QueryString.substring(vSelectIndex + 6, 
        vFromIndex).trim();      
    }
    catch (IndexOutOfBoundsException e2) {
      throw new MintSyntaxErrorException(ERROR + 
        "Select expression is missing!");
    }    
      
    int vWhereIndex = QueryStringLowerCase.indexOf("where");
    if (vWhereIndex < 0) {
      // no predicates at all
      try {
        FromClause = 
          QueryString.substring(vFromIndex + 4).trim();
      }
      catch (IndexOutOfBoundsException e2) {
        throw new MintSyntaxErrorException(ERROR + 
          "From expression is missing!");
      } 
      WhereClause = null;         
      WhereClauseLowerCase = null;
    }
    else {      
      try {
        FromClause = 
          QueryString.substring(vFromIndex + 4, vWhereIndex).trim();
      }
      catch (IndexOutOfBoundsException e2) {
        throw new MintSyntaxErrorException(ERROR + 
          "From expression is missing!");
      } 
      try {
        WhereClause = 
          QueryString.substring(vWhereIndex + 5).trim();
        WhereClauseLowerCase = WhereClause.toLowerCase();      
      }
      catch (IndexOutOfBoundsException e2) {
        throw new MintSyntaxErrorException(ERROR + 
          "Where expression is missing!");
      } 
      
    }
    FromClauseLowerCase = FromClause.toLowerCase();      
  
  }  // splitIntoMainParts()
  
  // ########## comments will follow ##########
  
  private void splitFromClause() throws MintSyntaxErrorException {
  
    int vNodeIndex = FromClauseLowerCase.indexOf("node");
    if (vNodeIndex < 0)
      throw new MintSyntaxErrorException(ERROR + 
        "\"node\" statement after \"from\" in from-clause is missing!");   
    try {
      FromClause = FromClause.substring(vNodeIndex + 4).trim();
      FromClauseLowerCase = FromClause.toLowerCase();
    }
    catch (IndexOutOfBoundsException e2) {
      throw new MintSyntaxErrorException(ERROR + 
        "Variable name is missing!");
    }
    
    int vAsIndex = FromClauseLowerCase.indexOf("as");
    if (vAsIndex < 0)
      throw new MintSyntaxErrorException(ERROR + 
        "\"as\" statement after \"node\" in from-clause is missing!");   
    try {
      FromClause = FromClause.substring(vAsIndex + 2).trim();
      FromClauseLowerCase = FromClause.toLowerCase();
    }
    catch (IndexOutOfBoundsException e2) {
      throw new MintSyntaxErrorException(ERROR + 
        "Variable name is missing!");
    }

    int vTemplateIndex = FromClauseLowerCase.indexOf("template");
    if (vTemplateIndex < 0)
      throw new MintSyntaxErrorException(ERROR + 
        "\"template\" statement in from-clause is missing!"); 
      
    VariableClause = FromClause.substring(0, vTemplateIndex).trim(); 
    try {
      TemplateClause = FromClause.substring(vTemplateIndex + 8).trim();   
      TemplateClauseLowerCase = TemplateClause.toLowerCase();
    }
    catch (IndexOutOfBoundsException e2) {
      throw new MintSyntaxErrorException(ERROR + 
        "Template expression is missing!");
    }    
    
    int vFirstCommaIndex = VariableClause.lastIndexOf(",");
    int vLastCommaIndex = VariableClause.lastIndexOf(",");
    if (vFirstCommaIndex < vLastCommaIndex)
      throw new MintSyntaxErrorException(ERROR + 
        "Names of variables are not separated by commas! Use blank spaces!");
    
    if (vLastCommaIndex < 0)
      throw new MintSyntaxErrorException(ERROR + 
        "Comma after list of variable names is missing!");
    VariableClause = VariableClause.substring(0, vLastCommaIndex).trim();
        
    VariableNames = new Vector();
    oItemizer = new Itemizer(VariableClause);
    vBuffer = oItemizer.getNextItemOrNull();
    while (vBuffer != null) {
      vBuffer = vBuffer.trim();
      if ( ! VariableNames.contains(vBuffer) )
        VariableNames.add(vBuffer);
      else
        throw new MintSyntaxErrorException(ERROR + "Variable name " + 
          vBuffer + " occurs more than once in the list of variables!");  
      vBuffer = oItemizer.getNextItemOrNull();
    }
    CounterVariables = VariableNames.size();
    
    int vTemplateAsIndex = TemplateClauseLowerCase.indexOf("as");
    if (vTemplateAsIndex < 0)
      throw new MintSyntaxErrorException(ERROR + 
        "\"as\" statement before template variable is missing!");         
    
    TemplateString = TemplateClause.substring(0, vTemplateAsIndex).trim();
    try {
      TemplateName = TemplateClause.substring(vTemplateAsIndex + 2).trim();
    }
    catch (IndexOutOfBoundsException e1) {
      throw new MintSyntaxErrorException(ERROR + 
        "Template variable is missing!");
    }
    oTemplate = new MintTemplate(TemplateName, TemplateString, VariableNames);
  
  }  // splitFromClause()

  // ########## comments will follow ##########
  
  public void splitSelectClause() {
  
    if ( ! SelectClause.equals( oTemplate.getName() ) )
      throw new MintSyntaxErrorException(ERROR + 
        "Select expression does not contain the template variable!");  
  
  }  // splitSelectClause()  
  
  // ########## comments will follow ##########
  
  public void splitWhereClause() {
  
    CounterPredicates = 0;
    String vCurrentPredicateString = "";
    if (WhereClause == null) {        
      Predicates = null;  
    }
    else {
      Predicates = new Vector();      
      oItemizer = new Itemizer(WhereClause);
      vBuffer = oItemizer.getNextItemOrNull();
      while (vBuffer != null) {
        vBuffer = vBuffer.trim();
        if ( vBuffer.toLowerCase().equals("and") ) {  
          // false =  No filter predicate: This is a MINT query predicate!
          Predicates.add( new MintPredicate(
              vCurrentPredicateString.trim(), VariableNames, false ) );
          CounterPredicates++;
          vCurrentPredicateString = "";
          }
        else {
          vCurrentPredicateString += vBuffer + " ";
        }         
        vBuffer = oItemizer.getNextItemOrNull();
      }     
      // first or last predicate
      // false =  No filter predicate: This is a MINT query predicate!
      Predicates.add( new MintPredicate(vCurrentPredicateString.trim(), 
        VariableNames, false ) );
      CounterPredicates++;        
    }
  
  }  // splitWhereClause()  
  
}  // class MintQueryParser
