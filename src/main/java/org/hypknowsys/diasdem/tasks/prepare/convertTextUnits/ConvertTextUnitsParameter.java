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

package org.hypknowsys.diasdem.tasks.prepare.convertTextUnits;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ConvertTextUnitsParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected int ConversionType = CONVERT_TEXT_UNITS_TO_LOWER_CASE;
  protected String RegularExpression = null;
  protected String ReplacementString = null;
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
  "org.hypknowsys.diasdem.tasks.prepare.convertTextUnits"
  + ".ConvertTextUnitsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.convertTextUnits"
  + ".ConvertTextUnitsParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String CONVERSION_TYPE =
  "ConversionType";
  private final static String REGULAR_EXPRESSION =
  "RegularExpression";
  private final static String REPLACEMENT_STRING =
  "ReplacementString";
  private final static String MULTI_TOKEN_FILE_NAME =
  "MultiTokenFileName";
  private final static String TOKEN_REPLACEMENT_FILE_NAME =
  "TokenReplacementFileName";
  
  public final static int CONVERT_TEXT_UNITS_TO_LOWER_CASE = 0;
  public final static int CONVERT_TEXT_UNITS_TO_UPPER_CASE = 1;
  public final static int APPLY_REGULAR_EXPRESSION_TO_TEXT_UNITS = 2;
  public final static int IDENTIFY_SPECIFIED_MULTI_TOKEN_TERMS = 3;
  public final static int FIND_AND_REPLACE_SPECIFIED_TOKENS = 4;
  public final static int REMOVE_PART_OF_SPEECH_TAGS_FROM_TOKENS = 5;
  public final static int REMOVE_WORD_SENSE_TAGS_FROM_TOKENS = 6;
  public final static String[] CONVERSION_TYPES = {
    "Convert Text Units to Lower Case",
    "Convert Text Units to Upper Case",
    "Apply Regular Expression to Text Units",
    "Identifiy Specified Multi-Token Terms",
    "Find and Replace Specified Tokens",
    "Remove Part of Speech Tags from Tokens",
    "Remove Word Sense Tags from Tokens"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertTextUnitsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    CollectionFileName = null;
    ConversionType = CONVERT_TEXT_UNITS_TO_LOWER_CASE;
    MultiTokenFileName = null;
    TokenReplacementFileName = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertTextUnitsParameter(String pCollectionFileName,
  String pConversionType, String pRegularExpression,
  String pReplacementString, String pMultiTokenFileName, 
  String pTokenReplacementFileName) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ConversionType = CONVERT_TEXT_UNITS_TO_LOWER_CASE;
    RegularExpression = pRegularExpression;
    ReplacementString = pReplacementString;
    MultiTokenFileName = pMultiTokenFileName;
    TokenReplacementFileName = pTokenReplacementFileName;
    
    for (int i = 0; i < CONVERSION_TYPES.length; i++) {
      if (CONVERSION_TYPES[i].equals(pConversionType)) {
        ConversionType = i;
        break;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertTextUnitsParameter(String pCollectionFileName,
  int pConversionType, String pRegularExpression,
  String pReplacementString, String pMultiTokenFileName, 
  String pTokenReplacementFileName) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    RegularExpression = pRegularExpression;
    ReplacementString = pReplacementString;
    if (pConversionType >= 0 && pConversionType < CONVERSION_TYPES.length) {
      ConversionType = pConversionType;
    }
    MultiTokenFileName = pMultiTokenFileName;
    TokenReplacementFileName = pTokenReplacementFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public int getConversionType() {
    return ConversionType; }
  public String getRegularExpression() {
    return RegularExpression; }
  public String getReplacementString() {
    return ReplacementString; }
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
    ParameterAttributes.put(CONVERSION_TYPE, Tools.int2String(
    ConversionType));
    ParameterAttributes.put(REGULAR_EXPRESSION, RegularExpression);
    ParameterAttributes.put(REPLACEMENT_STRING, ReplacementString);
    ParameterAttributes.put(MULTI_TOKEN_FILE_NAME, MultiTokenFileName);
    ParameterAttributes.put(TOKEN_REPLACEMENT_FILE_NAME, 
    TokenReplacementFileName);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes.get(COLLECTION_FILE_NAME);
    ConversionType = Tools.string2Int((String)ParameterAttributes
    .get(CONVERSION_TYPE));
    RegularExpression = (String)ParameterAttributes.get(REGULAR_EXPRESSION);
    ReplacementString = (String)ParameterAttributes.get(REPLACEMENT_STRING);
    MultiTokenFileName = (String)ParameterAttributes.get(MULTI_TOKEN_FILE_NAME);
    TokenReplacementFileName = (String)ParameterAttributes.get(
    TOKEN_REPLACEMENT_FILE_NAME);
    
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