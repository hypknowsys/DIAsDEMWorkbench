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

package org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1.0.2, 18 October 2003
 * @author Karsten Winkler
 */

public class FindCollocationsFrequencyPosFilterParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected int MinTokensInCollocation = 0;
  protected int MaxTokensInCollocation = 0;
  protected int MinCollocationFrequency = 0;
  protected String CollocationPosFilterRegex = null;
  protected String CollocationsFileName = null;
  protected String ParserFileName = null;
  protected String TaggedFileName = null;
  protected String TreeTaggerCommand = null;
  protected boolean ExportCollocationsInTxtFormat = false;
  protected boolean ExportCollocationsInHtmlFormat = false;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter" +
  ".FindCollocationsFrequencyPosFilterTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter" +
  ".FindCollocationsFrequencyPosFilterParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String MIN_TOKENS_IN_COLLOCATION =
  "MinTokensInCollocation";
  private final static String MAX_TOKENS_IN_COLLOCATION =
  "MaxTokensInCollocation";
  private final static String MIN_COLLOCATION_FREQUENCY =
  "MinCollocationFrequency";
  private final static String COLLOCATION_POS_FILTER_REGEX =
  "CollocationPosFilterRegex";
  private final static String COLLOCATIONS_FILE_NAME =
  "CollocationsFileName";
  private final static String PARSER_FILE_NAME =
  "ParserFileName";
  private final static String TAGGED_FILE_NAME =
  "TaggedFileName";
  private final static String TREE_TAGGER_COMMAND =
  "TreeTaggerCommand";
  private final static String EXPORT_COLLOCATIONS_IN_TXT_FORMAT =
  "ExportCollocationsInTxtFormat";
  private final static String EXPORT_COLLOCATIONS_IN_HTML_FORMAT =
  "ExportCollocationsInHtmlFormat";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
    public FindCollocationsFrequencyPosFilterParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    MinTokensInCollocation = 0;
    MaxTokensInCollocation = 0;
    MinCollocationFrequency = 0;
    CollocationPosFilterRegex = null;
    CollocationsFileName = null;
    ParserFileName = null;
    TaggedFileName = null;
    TreeTaggerCommand = null;
    ExportCollocationsInTxtFormat = false;
    ExportCollocationsInHtmlFormat = false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
    public FindCollocationsFrequencyPosFilterParameter(
    String pCollectionFileName, int pMinTokensInCollocation,
    int pMaxTokensInCollocation, int pMinCollocationFrequency,
    String pCollocationPosFilterRegex, String pCollocationsFileName,
    String pParserFileName, String pTaggedFileName, String pTreeTaggerCommand,
    boolean pExportCollocationsInTxtFormat, 
    boolean pExportCollocationsInHtmlFormat) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    MinTokensInCollocation = pMinTokensInCollocation;
    MaxTokensInCollocation = pMaxTokensInCollocation;
    MinCollocationFrequency = pMinCollocationFrequency;
    CollocationPosFilterRegex = pCollocationPosFilterRegex;
    CollocationsFileName = pCollocationsFileName;
    ParserFileName = pParserFileName;
    TaggedFileName = pTaggedFileName;
    TreeTaggerCommand = pTreeTaggerCommand;
    ExportCollocationsInTxtFormat = pExportCollocationsInTxtFormat;
    ExportCollocationsInHtmlFormat = pExportCollocationsInHtmlFormat;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() { 
    return CollectionFileName; }
  public int getMinTokensInCollocation() { 
    return MinTokensInCollocation; }
  public int getMaxTokensInCollocation() { 
    return MaxTokensInCollocation; }
  public int getMinCollocationFrequency() { 
    return MinCollocationFrequency; }
  public String getCollocationPosFilterRegex() { 
    return CollocationPosFilterRegex; }
  public String getCollocationsFileName() { 
    return CollocationsFileName; }
  public String getParserFileName() { 
    return ParserFileName; }
  public String getTaggedFileName() { 
    return TaggedFileName; }
  public String getTreeTaggerCommand() { 
    return TreeTaggerCommand; }
  public boolean getExportCollocationsInTxtFormat() {
    return ExportCollocationsInTxtFormat; }
  public boolean exportCollocationsInTxtFormat() {
    return ExportCollocationsInTxtFormat; }
  public boolean getExportCollocationsInHtmlFormat() {
    return ExportCollocationsInHtmlFormat; }
  public boolean exportCollocationsInHtmlFormat() {
    return ExportCollocationsInHtmlFormat; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setExportCollocationsInTxtFormat(
  boolean pExportCollocationsInTxtFormat) {
    ExportCollocationsInTxtFormat = pExportCollocationsInTxtFormat; }
  public void setExportCollocationsInHtmlFormat(
  boolean pExportCollocationsInHtmlFormat) {
    ExportCollocationsInHtmlFormat = pExportCollocationsInHtmlFormat; }

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
    ParameterAttributes.put(MIN_TOKENS_IN_COLLOCATION,
    Tools.int2String(MinTokensInCollocation));
    ParameterAttributes.put(MAX_TOKENS_IN_COLLOCATION,
    Tools.int2String(MaxTokensInCollocation));
    ParameterAttributes.put(MIN_COLLOCATION_FREQUENCY,
    Tools.int2String(MinCollocationFrequency));
    ParameterAttributes.put(COLLOCATION_POS_FILTER_REGEX,
    CollocationPosFilterRegex);
    ParameterAttributes.put(COLLOCATIONS_FILE_NAME,
    CollocationsFileName);
    ParameterAttributes.put(PARSER_FILE_NAME,
    ParserFileName);
    ParameterAttributes.put(TAGGED_FILE_NAME,
    TaggedFileName);
    ParameterAttributes.put(TREE_TAGGER_COMMAND,
    TreeTaggerCommand);
    ParameterAttributes.put(EXPORT_COLLOCATIONS_IN_TXT_FORMAT,
    Tools.boolean2String(ExportCollocationsInTxtFormat));
    ParameterAttributes.put(EXPORT_COLLOCATIONS_IN_HTML_FORMAT,
    Tools.boolean2String(ExportCollocationsInHtmlFormat));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
   
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    MinTokensInCollocation = Tools.string2Int(
    (String)ParameterAttributes.get(MIN_TOKENS_IN_COLLOCATION));
    MaxTokensInCollocation = Tools.string2Int(
    (String)ParameterAttributes.get(MAX_TOKENS_IN_COLLOCATION));
    MinCollocationFrequency = Tools.string2Int(
    (String)ParameterAttributes.get(MIN_COLLOCATION_FREQUENCY));
    CollocationPosFilterRegex = (String)ParameterAttributes
    .get(COLLOCATION_POS_FILTER_REGEX);
    CollocationsFileName = (String)ParameterAttributes
    .get(COLLOCATIONS_FILE_NAME);
    ParserFileName = (String)ParameterAttributes
    .get(PARSER_FILE_NAME);
    TaggedFileName = (String)ParameterAttributes
    .get(TAGGED_FILE_NAME);
    TreeTaggerCommand = (String)ParameterAttributes
    .get(TREE_TAGGER_COMMAND);
    ExportCollocationsInTxtFormat = Tools.string2Boolean(
    (String)ParameterAttributes.get(EXPORT_COLLOCATIONS_IN_TXT_FORMAT));
    ExportCollocationsInHtmlFormat = Tools.string2Boolean(
    (String)ParameterAttributes.get(EXPORT_COLLOCATIONS_IN_HTML_FORMAT));
    
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