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

package org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class LemmatizeTextUnitsParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String ParserFileName = null;
  protected String TaggedFileName = null;
  protected String TreeTaggerCommand = null;
  protected int Algorithm = USE_TREETAGGER_TO_DETERMINE_LEMMA_FORM;
  protected String LemmaFormListFileName = null;
  protected String UnknownLemmaFormsFileName = null;
  protected boolean CreateKnownLemmaFormsFile = false;
  protected boolean AppendPosTagToEachToken = false;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits"
  + ".LemmatizeTextUnitsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits"
  + ".LemmatizeTextUnitsParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String PARSER_FILE_NAME =
  "ParserFileName";
  private final static String TAGGED_FILE_NAME =
  "TaggedFileName";
  private final static String TREE_TAGGER_COMMAND =
  "TreeTaggerCommand";
  private final static String ALGORITHM =
  "Algorithm";
  private final static String LEMMA_FORM_LIST_FILE_NAME =
  "LemmaFormListFileName";
  private final static String UNKNOWN_LEMMA_FORMS_FILE_NAME =
  "UnknownLemmaFormsFileName";
  private final static String CREATE_KNOWN_LEMMA_FORMS_FILE =
  "CreateKnownLemmaFormsFile";
  private final static String APPEND_POS_TAG_TO_EACH_TOKEN =
  "AppendPosTagToEachToken";
  
  public final static int USE_TREETAGGER_TO_DETERMINE_LEMMA_FORM = 0;
  public final static int LOOK_UP_LEMMA_FORM_IN_LIST = 1;
  public final static String[] ALGORITHMS = {
    "Use TreeTagger to Determine Lemma Form", 
    "Look Up Lemma Form in List" };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public LemmatizeTextUnitsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    ParserFileName = null;
    TaggedFileName = null;
    TreeTaggerCommand = null;
    Algorithm = USE_TREETAGGER_TO_DETERMINE_LEMMA_FORM;
    LemmaFormListFileName = null;
    UnknownLemmaFormsFileName = null;
    CreateKnownLemmaFormsFile = false;
    AppendPosTagToEachToken = false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public LemmatizeTextUnitsParameter(String pCollectionFileName, 
    String pParserFileName, String pTaggedFileName, 
    String pTreeTaggerCommand, int pAlgorithm, String pLemmaFormListFileName,
    String pUnknownLemmaFormsFileName, boolean pCreateKnownLemmaFormsFile,
    boolean pAppendPosTagToEachToken) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ParserFileName = pParserFileName;
    TaggedFileName = pTaggedFileName;
    TreeTaggerCommand = pTreeTaggerCommand;
    Algorithm = pAlgorithm;
    LemmaFormListFileName = pLemmaFormListFileName;
    UnknownLemmaFormsFileName = pUnknownLemmaFormsFileName;
    CreateKnownLemmaFormsFile = pCreateKnownLemmaFormsFile;
    AppendPosTagToEachToken = pAppendPosTagToEachToken;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public LemmatizeTextUnitsParameter(String pCollectionFileName, 
    String pParserFileName, String pTaggedFileName, 
    String pTreeTaggerCommand, String pAlgorithm, String pLemmaFormListFileName,
    String pUnknownLemmaFormsFileName, boolean pCreateKnownLemmaFormsFile,
    boolean pAppendPosTagToEachToken) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ParserFileName = pParserFileName;
    TaggedFileName = pTaggedFileName;
    TreeTaggerCommand = pTreeTaggerCommand;
    LemmaFormListFileName = pLemmaFormListFileName;
    UnknownLemmaFormsFileName = pUnknownLemmaFormsFileName;
    CreateKnownLemmaFormsFile = pCreateKnownLemmaFormsFile;
    AppendPosTagToEachToken = pAppendPosTagToEachToken;

    for (int i = 0; i < ALGORITHMS.length; i++) {
      if (ALGORITHMS[i].equals(pAlgorithm)) { 
        Algorithm = i;
        break;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() { 
    return CollectionFileName; }
  public String getParserFileName() { 
    return ParserFileName; }
  public String getTaggedFileName() { 
    return TaggedFileName; }
  public String getTreeTaggerCommand() { 
    return TreeTaggerCommand; }
  public String getLemmaFormListFileName() { 
    return LemmaFormListFileName; }
  public String getUnknownLemmaFormsFileName() { 
    return UnknownLemmaFormsFileName; }
  public int getAlgorithm() { 
    return Algorithm; }
  public boolean getCreateKnownLemmaFormsFile() { 
    return CreateKnownLemmaFormsFile; }
  public boolean createKnownLemmaFormsFile() { 
    return CreateKnownLemmaFormsFile; }
  public boolean getAppendPosTagToEachToken() { 
    return AppendPosTagToEachToken; }
  public boolean appendPosTagToEachToken() { 
    return AppendPosTagToEachToken; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCreateKnownLemmaFormsFile(boolean pCreateKnownLemmaFormsFile) { 
    CreateKnownLemmaFormsFile = pCreateKnownLemmaFormsFile; }
  public void setAppendPosTagToEachToken(boolean pAppendPosTagToEachToken) { 
    AppendPosTagToEachToken = pAppendPosTagToEachToken; }

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
    ParameterAttributes.put(COLLECTION_FILE_NAME,
    CollectionFileName);
    ParameterAttributes.put(PARSER_FILE_NAME,
    ParserFileName);
    ParameterAttributes.put(TAGGED_FILE_NAME,
    TaggedFileName);
    ParameterAttributes.put(TREE_TAGGER_COMMAND,
    TreeTaggerCommand);
    ParameterAttributes.put(ALGORITHM,
    Tools.int2String(Algorithm));
    ParameterAttributes.put(LEMMA_FORM_LIST_FILE_NAME,
    LemmaFormListFileName);
    ParameterAttributes.put(UNKNOWN_LEMMA_FORMS_FILE_NAME,
    UnknownLemmaFormsFileName);
    ParameterAttributes.put(CREATE_KNOWN_LEMMA_FORMS_FILE,
    Tools.boolean2String(CreateKnownLemmaFormsFile));
    ParameterAttributes.put(APPEND_POS_TAG_TO_EACH_TOKEN,
    Tools.boolean2String(AppendPosTagToEachToken));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
   
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    ParserFileName = (String)ParameterAttributes
    .get(PARSER_FILE_NAME);
    TaggedFileName = (String)ParameterAttributes
    .get(TAGGED_FILE_NAME);
    TreeTaggerCommand = (String)ParameterAttributes
    .get(TREE_TAGGER_COMMAND);
    Algorithm = Tools.string2Int(
    (String)ParameterAttributes.get(ALGORITHM));
    LemmaFormListFileName = (String)ParameterAttributes
    .get(LEMMA_FORM_LIST_FILE_NAME);
    UnknownLemmaFormsFileName = (String)ParameterAttributes
    .get(UNKNOWN_LEMMA_FORMS_FILE_NAME);
    CreateKnownLemmaFormsFile = Tools.string2Boolean(
    (String)ParameterAttributes.get(CREATE_KNOWN_LEMMA_FORMS_FILE));
    AppendPosTagToEachToken = Tools.string2Boolean(
    (String)ParameterAttributes.get(APPEND_POS_TAG_TO_EACH_TOKEN));
    
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