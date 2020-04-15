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

package org.hypknowsys.diasdem.tasks.prepare.tokenizeTextUnits;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class TokenizeTextUnitsParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String TokenizeRegexFileName = null;
  protected String NormalizeRegexFileName = null;
  protected String MultiTokenFileName = null;
  protected String TokenReplacementFileName = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.tokenizeTextUnits.TokenizeTextUnitsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.tokenizeTextUnits.TokenizeTextUnitsParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String TOKENIZE_REGEX_FILE_NAME =
  "TokenizeRegexFileName";
  private final static String NORMALIZE_REGEX_FILE_NAME =
  "NormalizeRegexFileName";
  private final static String MULTI_TOKEN_FILE_NAME =
  "MultiTokenFileName";
  private final static String TOKEN_REPLACEMENT_FILE_NAME =
  "TokenReplacementFileName";

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TokenizeTextUnitsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    TokenizeRegexFileName = null;
    NormalizeRegexFileName = null;
    MultiTokenFileName = null;
    TokenReplacementFileName = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TokenizeTextUnitsParameter(String pCollectionFileName, 
  String pTokenizeRegexFileName, String pNormalizeRegexFileName, 
  String pMultiTokenFileName, String pTokenReplacementFileName) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    TokenizeRegexFileName = pTokenizeRegexFileName;
    NormalizeRegexFileName = pNormalizeRegexFileName;
    MultiTokenFileName = pMultiTokenFileName;
    TokenReplacementFileName = pTokenReplacementFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() { 
    return CollectionFileName; }
  public String getTokenizeRegexFileName() { 
    return TokenizeRegexFileName; }
  public String getNormalizeRegexFileName() { 
    return NormalizeRegexFileName; }
  public String getMultiTokenFileName() { 
    return MultiTokenFileName; }
  public String getTokenReplacementFileName() { 
    return TokenReplacementFileName; }
  
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
  /* ########## interface ScriptableTaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public org.jdom.Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(TOKENIZE_REGEX_FILE_NAME, TokenizeRegexFileName);
    ParameterAttributes.put(NORMALIZE_REGEX_FILE_NAME, NormalizeRegexFileName);
    ParameterAttributes.put(MULTI_TOKEN_FILE_NAME, MultiTokenFileName);
    ParameterAttributes.put(TOKEN_REPLACEMENT_FILE_NAME, TokenReplacementFileName);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    TokenizeRegexFileName = (String)ParameterAttributes
    .get(TOKENIZE_REGEX_FILE_NAME);
    NormalizeRegexFileName = (String)ParameterAttributes
    .get(NORMALIZE_REGEX_FILE_NAME);
    MultiTokenFileName = (String)ParameterAttributes
    .get(MULTI_TOKEN_FILE_NAME);
    TokenReplacementFileName = (String)ParameterAttributes
    .get(TOKEN_REPLACEMENT_FILE_NAME);
    
  }
  
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}