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

package org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeDocumentMetaData;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.event.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;
import org.hypknowsys.diasdem.tasks.prepare.tokenizeTextUnits.*;
import org.hypknowsys.diasdem.tasks.prepare.createTextUnits.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class TokenizeDocumentMetaDataTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TokenizeDocumentMetaDataParameter CastParameter = null;
  
  private DIAsDEMtextUnit[] TmpTextUnits = null;
  private TextUnitCreator MyTextUnitCreator = null;
  private TextTokenizer MyTextUnitTokenizer = null;
  private TextNormalizer MyTextUnitNormalizer = null;
  private MultiTokenWordIdentifier MyMultiTokenWordIdentifier = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient Pattern TmpPattern = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Tokenize Document Meta Data";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeDocumentMetaData"
  + ".TokenizeDocumentMetaDataUnitsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeDocumentMetaData"
  + ".TokenizeDocumentMetaDataResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeDocumentMetaData"
  + ".TokenizeDocumentMetaDataControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("TOKENIZE_META_DATA:_MRU_SOURCE_META_DATA_ATTRIBUTE", 
    "Task: Tokenize Meta Data; MRU Source Meta Data Attribute",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("TOKENIZE_META_DATA:_MRU_TARGET_META_DATA_ATTRIBUTE", 
    "Task: Tokenize Meta Data; MRU Target Meta Data Attribute",
    "", KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TokenizeDocumentMetaDataTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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
  /* ########## interface NonBlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    
    TokenizeDocumentMetaDataParameter parameter = null;
    if (pParameter instanceof TokenizeDocumentMetaDataParameter) {
      parameter = (TokenizeDocumentMetaDataParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    File file = new File(parameter.getCollectionFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION +
      "-file in the field 'Collection File'!");
    }
    if (parameter.getSourceMetaDataAttribute() == null
    || parameter.getSourceMetaDataAttribute().length() == 0) {
      result.addError(
      "Error: Please enter the source meta data\n" +
      "attribute in the field 'Source Attribute'!");
    }
    if (parameter.getTargetMetaDataAttribute() == null
    || parameter.getTargetMetaDataAttribute().length() == 0) {
      result.addError(
      "Error: Please enter the target meta data\n" +
      "attribute in the field 'Target Attribute'!");
    }
    file = new File(parameter.getAbbreviationsFileName());
    if (!file.exists() || !file.isFile()) {
      result.addError(
      "Error: Please enter the name of an existing\n" +
      "local file in the field 'Abbreviations File'!");
    }
    file = new File(parameter.getRegexFileName());
    if (!file.exists() || !file.isFile()) {
      result.addError(
      "Error: Please enter the name of an existing\n" +
      "local file in the field 'Full Stop Regex File'!");
    }
    file = new File(parameter.getTokenizeRegexFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Tokenize Regex File'!");
    }
    file = new File(parameter.getNormalizeRegexFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Normalize Regex File'!");
    }
    file = new File(parameter.getMultiTokenFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Multi Token Word File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new TokenizeDocumentMetaDataParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new TokenizeDocumentMetaDataResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_MISCELLANEOUS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof TokenizeDocumentMetaDataParameter) {
      CastParameter = (TokenizeDocumentMetaDataParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Meta data cannot be tokenized!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
        
    // read abbreviations: each line contains one abbreviation;
    // comment lines start with '#'
    TextFile abbreviationsFile = new TextFile(
    new File(CastParameter.getAbbreviationsFileName()));
    abbreviationsFile.open();
    String line = abbreviationsFile
    .getFirstLineButIgnoreCommentsAndEmptyLines();
    ArrayList list = new ArrayList();
    while (line != null) {
      list.add(line.trim());
      line = abbreviationsFile.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    // sort abbreviations by decreasing length
    Collections.sort(list, new SortStringsByDecreasingLength());
    // for (int i = 0; i < list.size(); i++) {
    //   System.out.println((String)list.get(i));
    // }
    // System.out.println(list.size());
    String[] abbreviations = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      abbreviations[i] = (String)list.get(i);
    }
    abbreviationsFile.close();
    
    // read regex file: each line contains the regex to search for
    // and the replacement string separated by a tab stop;
    // comment lines start with '#'
    TextFile regexFile = new TextFile(new File(CastParameter
    .getRegexFileName()));
    regexFile.open();
    line = regexFile.getFirstLineButIgnoreCommentsAndEmptyLines();
    ArrayList searchList = new ArrayList();
    ArrayList replaceList = new ArrayList();
    String[] contents = null;
    while (line != null) {
      contents = line.split("\t");
      if (contents.length == 2 && contents[1].trim().length() > 0) {
        try {
          TmpPattern = Pattern.compile(contents[0].trim());
          searchList.add(contents[0].trim());
          replaceList.add(contents[1].trim());
        }
        catch (PatternSyntaxException e) {
          System.out.println("[TokenizeDocumentMetaDataTask] Regex syntax error in "
          + " file " + CastParameter.getRegexFileName() + ": "
          + " Line \"" + line + "\"; error message: " + e.getMessage());
        }
      }
      else {
        System.out.println("[TokenizeDocumentMetaDataTask] Error in file "
        + CastParameter.getRegexFileName() + ": Line \""
        + line + "\" does not conform to syntax!");
      }
      line = regexFile.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    String[] regexSearch = new String[searchList.size()];
    for (int i = 0; i < searchList.size(); i++)
      regexSearch[i] = (String)searchList.get(i);
    String[] regexReplace = new String[replaceList.size()];
    for (int i = 0; i < replaceList.size(); i++)
      regexReplace[i] = (String)replaceList.get(i);
    regexFile.close();
    MyTextUnitCreator = new TextAsSingleTextUnitIdentifier(abbreviations,
    regexSearch, regexReplace, true);

    // read tokenizer regex file: each line contains the regex to search for
    // and the replacement string separated by a tab stop;
    // comment lines start with '#'
    TextFile tokenizeRegexFile = new TextFile(
    new File(CastParameter.getTokenizeRegexFileName()));
    tokenizeRegexFile.open();
    line = tokenizeRegexFile
    .getFirstLineButIgnoreCommentsAndEmptyLines();
    ArrayList tokenizeSearchList = new ArrayList();
    ArrayList tokenizeReplaceList = new ArrayList();
    String[] tokenizeContents = null;
    while (line != null) {
      tokenizeContents = line.split("\t");
      if (tokenizeContents.length == 2 
      && tokenizeContents[1].trim().length() > 0) {
        try {
          TmpPattern = Pattern.compile(tokenizeContents[0].trim());
          tokenizeSearchList.add(tokenizeContents[0].trim());
          tokenizeReplaceList.add(tokenizeContents[1].trim());
        }
        catch (PatternSyntaxException e) {
          System.out.println("[TokenizeDocumentMetaDataTask] Regex syntax error in "
          + " file " + CastParameter.getTokenizeRegexFileName() + ": "
          + " Line \"" + line + "\"; error message: " + e.getMessage());
        }
      }
      else {
        System.out.println("[TokenizeDocumentMetaDataTask] Error in file "
        + CastParameter.getTokenizeRegexFileName() + ": Line \""
        + line + "\" does not conform to syntax!");
      }
      line = tokenizeRegexFile.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    String[] tokenizeSearch = new String[tokenizeSearchList.size()];
    for (int i = 0; i < tokenizeSearchList.size(); i++)
      tokenizeSearch[i] = (String)tokenizeSearchList.get(i);
    String[] tokenizeReplace = new String[tokenizeReplaceList.size()];
    for (int i = 0; i < tokenizeReplaceList.size(); i++)
      tokenizeReplace[i] = (String)tokenizeReplaceList.get(i);
    tokenizeRegexFile.close();
    MyTextUnitTokenizer = new HeuristicTextTokenizer(tokenizeSearch,
    tokenizeReplace);
    // read normalizer regex file: each line contains the regex to search for
    // and the replacement string separated by a tab stop;
    // comment lines start with '#'
    TextFile normalizeRegexFile = new TextFile(
    new File(CastParameter.getNormalizeRegexFileName()));
    normalizeRegexFile.open();
    line = normalizeRegexFile.getFirstLineButIgnoreCommentsAndEmptyLines();
    ArrayList normalizeSearchList = new ArrayList();
    ArrayList normalizeReplaceList = new ArrayList();
    String[] normalizeContents = null;
    while (line != null) {
      normalizeContents = line.split("\t");
      if (normalizeContents.length == 2 
      && normalizeContents[1].trim().length() > 0) {
        try {
          TmpPattern = Pattern.compile(normalizeContents[0].trim());
          normalizeSearchList.add(normalizeContents[0].trim());
          normalizeReplaceList.add(normalizeContents[1].trim());
        }
        catch (PatternSyntaxException e) {
          System.out.println("[TokenizeDocumentMetaDataTask] Regex syntax error in "
          + " file " + CastParameter.getNormalizeRegexFileName() + ": "
          + " Line \"" + line + "\"; error message: " + e.getMessage());
        }
      }
      else {
        System.out.println("[TokenizeDocumentMetaDataTask] Error in file "
        + CastParameter.getNormalizeRegexFileName() + ": Line \""
        + line + "\" does not conform to syntax!");
      }
      line = normalizeRegexFile.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    String[] normalizeSearch = new String[normalizeSearchList.size()];
    for (int i = 0; i < normalizeSearchList.size(); i++)
      normalizeSearch[i] = (String)normalizeSearchList.get(i);
    String[] normalizeReplace = new String[normalizeReplaceList.size()];
    for (int i = 0; i < normalizeReplaceList.size(); i++)
      normalizeReplace[i] = (String)normalizeReplaceList.get(i);
    normalizeRegexFile.close();
    MyTextUnitNormalizer = new HeuristicTextNormalizer(normalizeSearch,
    normalizeReplace);
    // read multi token word: each line contains one multi token word;
    // comment lines start with '#'
    TextFile multiTokenFile = new TextFile(
    new File(CastParameter.getMultiTokenFileName()));
    multiTokenFile.open();
    line = multiTokenFile.getFirstLineButIgnoreCommentsAndEmptyLines();
    list = new ArrayList();
    while (line != null) {
      list.add(line.trim());
      line = multiTokenFile.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    // sort multi token terms by decreasing length
    Collections.sort(list, new SortStringsByDecreasingLength());
    // for (int i = 0; i < list.size(); i++) {
    //   System.out.println((String)list.get(i));
    // }
    // System.out.println(list.size());
    String[] multiToken = new String[list.size()];
    for (int i = 0; i < list.size(); i++)
      multiToken[i] = (String)list.get(i);
    multiTokenFile.close();
    MyMultiTokenWordIdentifier = new HeuristicMultiTokenWordIdentifier(
    multiToken);
    
    String metaDataSourceValue = null;
    String metaDataTargetValue = null;
    int counterSuccess = 0;
    int counterProgress = 0;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();    
    while (DiasdemDocument != null) {
            
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      metaDataSourceValue = (String)DiasdemDocument.getMetaData().get(
      CastParameter.getSourceMetaDataAttribute());
      if (metaDataSourceValue != null) {
        counterSuccess++;
        TmpTextUnits = MyTextUnitCreator.createTextUnits(metaDataSourceValue);
        metaDataTargetValue = MyTextUnitTokenizer
        .tokenizeText(TmpTextUnits[0].getContentsAsString());
        metaDataTargetValue = metaDataTargetValue.replace('*', '.').trim();
        metaDataTargetValue = MyTextUnitNormalizer
        .normalizeText(metaDataTargetValue);
        metaDataTargetValue = MyMultiTokenWordIdentifier
        .identifyMultiTokenWords(metaDataTargetValue);
        DiasdemDocument.getMetaData().put(CastParameter
        .getTargetMetaDataAttribute(), metaDataTargetValue);
      }

      DiasdemCollection.replaceDocument(DiasdemDocument
      .getDiasdemDocumentID(), DiasdemDocument);
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT,
    "The source meta data attribute has been successfully tokenized\n" + 
    "in " + counterSuccess + " of " + counterProgress + 
    " documents contained in the collection\n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) + "!");
    this.setTaskResult(100, "All Documents Processed ...", Result,
    TaskResult.FINAL_RESULT, Task.TASK_FINISHED);
    
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
  
  class SortStringsByDecreasingLength implements Comparator {
    
    public int compare(Object pObject1, Object pObject2) {
      String string1 = (String)pObject1;
      String string2 = (String)pObject2;
      if (string1.length() < string2.length()) {
        return 1;
      }
      else if (string1.length() > string2.length()) {
        return -1;
      }
      else {
        return 0;
      }
    }
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}