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

package org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeParameterTextFile;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class TokenizeParameterTextFileParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String SourceParameterFileName = null;
  protected String TargetParameterFileName = null;
  protected String AbbreviationsFileName = null;
  protected String RegexFileName = null;
  protected String TokenizeRegexFileName = null;
  protected String NormalizeRegexFileName = null;
  protected String MultiTokenFileName = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeParameterTextFile"
  + ".TokenizeParameterTextFileTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeParameterTextFile"
  + ".TokenizeParameterTextFileParameterPanel";
  
  private final static String SOURCE_PARAMETER_FILE_NAME =
  "SourceParameterFileName";
  private final static String TARGET_PARAMETER_FILE_NAME =
  "TargetParameterFileName";
  private final static String ABBREVIATIONS_FILE_NAME =
  "AbbreviationsFileName";
  private final static String REGEX_FILE_NAME =
  "RegexFileName";
  private final static String TOKENIZE_REGEX_FILE_NAME =
  "TokenizeRegexFileName";
  private final static String NORMALIZE_REGEX_FILE_NAME =
  "NormalizeRegexFileName";
  private final static String MULTI_TOKEN_FILE_NAME =
  "MultiTokenFileName";

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TokenizeParameterTextFileParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    SourceParameterFileName = null;
    TargetParameterFileName = null;
    AbbreviationsFileName = null;
    RegexFileName = null;
    TokenizeRegexFileName = null;
    NormalizeRegexFileName = null;
    MultiTokenFileName = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TokenizeParameterTextFileParameter(String pSourceParameterFileName, 
  String pTargetParameterFileName, String pAbbreviationsFileName, 
  String pRegexFileName, String pTokenizeRegexFileName, 
  String pNormalizeRegexFileName, String pMultiTokenFileName) {
    
    this();
    
    SourceParameterFileName = pSourceParameterFileName;
    TargetParameterFileName = pTargetParameterFileName;
    AbbreviationsFileName = pAbbreviationsFileName;
    RegexFileName = pRegexFileName;
    TokenizeRegexFileName = pTokenizeRegexFileName;
    NormalizeRegexFileName = pNormalizeRegexFileName;
    MultiTokenFileName = pMultiTokenFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getSourceParameterFileName() {
    return SourceParameterFileName; }
  public String getTargetParameterFileName() {
    return TargetParameterFileName; }
  public String getAbbreviationsFileName() {
    return AbbreviationsFileName; }
  public String getRegexFileName() {
    return RegexFileName; }
  public String getTokenizeRegexFileName() { 
    return TokenizeRegexFileName; }
  public String getNormalizeRegexFileName() { 
    return NormalizeRegexFileName; }
  public String getMultiTokenFileName() { 
    return MultiTokenFileName; }
  
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
    ParameterAttributes.put(SOURCE_PARAMETER_FILE_NAME, SourceParameterFileName);
    ParameterAttributes.put(TARGET_PARAMETER_FILE_NAME, TargetParameterFileName);
    ParameterAttributes.put(ABBREVIATIONS_FILE_NAME, AbbreviationsFileName);
    ParameterAttributes.put(REGEX_FILE_NAME, RegexFileName);
    ParameterAttributes.put(TOKENIZE_REGEX_FILE_NAME, TokenizeRegexFileName);
    ParameterAttributes.put(NORMALIZE_REGEX_FILE_NAME, NormalizeRegexFileName);
    ParameterAttributes.put(MULTI_TOKEN_FILE_NAME, MultiTokenFileName);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    SourceParameterFileName = (String)ParameterAttributes
    .get(SOURCE_PARAMETER_FILE_NAME);
    TargetParameterFileName = (String)ParameterAttributes
    .get(TARGET_PARAMETER_FILE_NAME);
    AbbreviationsFileName = (String)ParameterAttributes
    .get(ABBREVIATIONS_FILE_NAME);
    RegexFileName = (String)ParameterAttributes
    .get(REGEX_FILE_NAME);
    TokenizeRegexFileName = (String)ParameterAttributes
    .get(TOKENIZE_REGEX_FILE_NAME);
    NormalizeRegexFileName = (String)ParameterAttributes
    .get(NORMALIZE_REGEX_FILE_NAME);
    MultiTokenFileName = (String)ParameterAttributes
    .get(MULTI_TOKEN_FILE_NAME);
    
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