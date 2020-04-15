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

package org.hypknowsys.diasdem.tasks.prepare.createTextUnits;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class CreateTextUnitsParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String AbbreviationsFileName = null;
  protected String RegexFileName = null;
  protected int Algorithm = HEURISTIC_SENTENCE_IDENTIFIER;
  protected boolean KeepAsterisks = true;
  protected int TextUnitsLayer = CREATE_OR_REPLACE_DEFAULT_TEXT_UNITS_LAYER;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createTextUnits.CreateTextUnitsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createTextUnits.CreateTextUnitsParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String ABBREVIATIONS_FILE_NAME =
  "AbbreviationsFileName";
  private final static String REGEX_FILE_NAME =
  "RegexFileName";
  private final static String ALGORITHM =
  "Algorithm";
  private final static String KEEP_ASTERISKS =
  "KeepAsterisks";
  private final static String TEXT_UNITS_LAYER =
  "TextUnitsLayer";
  
  public final static int HEURISTIC_SENTENCE_IDENTIFIER = 0;
  public final static int TEXT_AS_A_SINGLE_TEXT_UNIT = 1;
  public final static String[] ALGORITHMS = {
    "Heuristic Sentence Identifier",
    "Text as a Single Text Unit"
  };
  
  public final static int KEEP_ASTERISKS_FOR_TOKENIZATION = 0;
  public final static int DO_NOT_KEEP_ASTERISKS_FOR_TOKENIZATION = 1;
  public final static String[] KEEP_ASTERISKS_OPTIONS = {
    "Keep Asterisks for Tokenization",
    "Do Not Keep Asterisks for Tokenization"
  };
  
  public final static int CREATE_OR_REPLACE_DEFAULT_TEXT_UNITS_LAYER = 0;
  public final static int CREATE_NEW_TEXT_UNITS_LAYER = 1;
  public final static String[] TEXT_UNITS_LAYER_OPTIONS = {
    "Create or Replace Default Text Units Layer",
    "Create New Text Units Layer"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateTextUnitsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    AbbreviationsFileName = null;
    RegexFileName = null;
    Algorithm = HEURISTIC_SENTENCE_IDENTIFIER;
    KeepAsterisks = true;
    TextUnitsLayer = CREATE_OR_REPLACE_DEFAULT_TEXT_UNITS_LAYER;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateTextUnitsParameter(String pCollectionFileName,
  String pAbbreviationsFileName, String pRegexFileName, String pAlgorithm,
  String pKeepAsterisks, String pTextUnitsLayer) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    AbbreviationsFileName = pAbbreviationsFileName;
    RegexFileName = pRegexFileName;
    
    for (int i = 0; i < ALGORITHMS.length; i++)
      if (ALGORITHMS[i].equals(pAlgorithm)) {
        Algorithm = i;
        break;
      }
    if (pKeepAsterisks.indexOf("Not") >= 0) {
      KeepAsterisks = false;
    }
    else {
      KeepAsterisks = true;
    }
    for (int i = 0; i < TEXT_UNITS_LAYER_OPTIONS.length; i++)
      if (TEXT_UNITS_LAYER_OPTIONS[i].equals(pTextUnitsLayer)) {
        TextUnitsLayer = i;
        break;
      }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateTextUnitsParameter(String pCollectionFileName,
  String pAbbreviationsFileName, String pRegexFileName,
  int pAlgorithm, boolean pKeepAsterisks, int pTextUnitsLayer) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    AbbreviationsFileName = pAbbreviationsFileName;
    RegexFileName = pRegexFileName;
    Algorithm = pAlgorithm;
    KeepAsterisks = pKeepAsterisks;
    TextUnitsLayer = pTextUnitsLayer;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getAbbreviationsFileName() {
    return AbbreviationsFileName; }
  public String getRegexFileName() {
    return RegexFileName; }
  public int getAlgorithm() {
    return Algorithm; }
  public boolean getKeepAsterisks() {
    return KeepAsterisks; }
  public int getTextUnitsLayer() {
    return TextUnitsLayer; }
  
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
    ParameterAttributes.put(ABBREVIATIONS_FILE_NAME, AbbreviationsFileName);
    ParameterAttributes.put(REGEX_FILE_NAME, RegexFileName);
    ParameterAttributes.put(ALGORITHM, Tools
    .int2String(Algorithm));
    ParameterAttributes.put(KEEP_ASTERISKS, Tools
    .boolean2String(KeepAsterisks));
    ParameterAttributes.put(TEXT_UNITS_LAYER, Tools
    .int2String(TextUnitsLayer));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    AbbreviationsFileName = (String)ParameterAttributes
    .get(ABBREVIATIONS_FILE_NAME);
    RegexFileName = (String)ParameterAttributes
    .get(REGEX_FILE_NAME);
    Algorithm = Tools.string2Int((String)ParameterAttributes
    .get(ALGORITHM));
    KeepAsterisks = Tools.string2Boolean((String)ParameterAttributes
    .get(KEEP_ASTERISKS));
    TextUnitsLayer = Tools.string2Int((String)ParameterAttributes
    .get(TEXT_UNITS_LAYER));
    
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