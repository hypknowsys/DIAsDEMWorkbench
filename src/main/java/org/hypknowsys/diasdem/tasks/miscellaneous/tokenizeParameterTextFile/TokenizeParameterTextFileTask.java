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

public class TokenizeParameterTextFileTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TokenizeParameterTextFileParameter CastParameter = null;
  
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
  "Tokenize Parameter Text File";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeParameterTextFile"
  + ".TokenizeParameterTextFileUnitsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeParameterTextFile"
  + ".TokenizeParameterTextFileResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeParameterTextFile"
  + ".TokenizeParameterTextFileControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("TOKENIZE_PARAMETER_FILE:_MRU_SOURCE_FILE_NAME", 
    "Task: Tokenize Parameter Text File; MRU Source Text File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("TOKENIZE_PARAMETER_FILE:_MRU_TARGET_FILE_NAME",
    "Task: Tokenize Parameter Text File; MRU Target Text File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TokenizeParameterTextFileTask() {
    
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
    
    TokenizeParameterTextFileParameter parameter = null;
    if (pParameter instanceof TokenizeParameterTextFileParameter) {
      parameter = (TokenizeParameterTextFileParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    if (parameter.getSourceParameterFileName().trim().equals(
    parameter.getTargetParameterFileName().trim())) {
      result.addError(
      "Error: 'Source Parameter File' must be\n" +
      "different from 'Target Parameter File'!");
    }
    File file = new File(parameter.getSourceParameterFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Source Parameter File'!");
    }
    if (parameter.getTargetParameterFileName().trim().length() <= 0
    || !parameter.getTargetParameterFileName().trim().endsWith(
    DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file name\nin the field 'Target Parameter File'!");
    }
    file = new File(parameter.getTargetParameterFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Target Parameter File' currently exists.\n" +
      "Do you really want to replace this file?");
    }
    file = new File(parameter.getSourceParameterFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION +
      "-file in the field 'Source Parameter File'!");
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
    
    return new TokenizeParameterTextFileParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new TokenizeParameterTextFileResult();
    
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
    
    if (Parameter != null && Parameter instanceof TokenizeParameterTextFileParameter) {
      CastParameter = (TokenizeParameterTextFileParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Parameter text file cannot be tokenized!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
        
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
          System.out.println("[TokenizeParameterTextFileTask] Regex syntax error in "
          + " file " + CastParameter.getRegexFileName() + ": "
          + " Line \"" + line + "\"; error message: " + e.getMessage());
        }
      }
      else {
        System.out.println("[TokenizeParameterTextFileTask] Error in file "
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
          System.out.println("[TokenizeParameterTextFileTask] Regex syntax error in "
          + " file " + CastParameter.getTokenizeRegexFileName() + ": "
          + " Line \"" + line + "\"; error message: " + e.getMessage());
        }
      }
      else {
        System.out.println("[TokenizeParameterTextFileTask] Error in file "
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
          System.out.println("[TokenizeParameterTextFileTask] Regex syntax error in "
          + " file " + CastParameter.getNormalizeRegexFileName() + ": "
          + " Line \"" + line + "\"; error message: " + e.getMessage());
        }
      }
      else {
        System.out.println("[TokenizeParameterTextFileTask] Error in file "
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
    
    TextFile sourceParameterFile = new TextFile(new File(CastParameter
    .getSourceParameterFileName()));
    sourceParameterFile.openReadOnly();
    
    TextFile targetParameterFile = new TextFile(new File(CastParameter
    .getTargetParameterFileName()));
    targetParameterFile.empty();
    targetParameterFile.open();    
    
    String sourceLine = null;
    String targetLine = null;
    int counterSuccess = 0;
    int counterProgress = 0;
    int counterProgressLines = 0;
    long maxProgress = ( new File(CastParameter
    .getSourceParameterFileName()) ).length();
    int lengthOfLineFeedChars = Tools.getLengthOfLineFeedChars();
    
    sourceLine = sourceParameterFile.getFirstLine();
    while (sourceLine != null) {
      
      if (counterProgressLines == 1 || (counterProgressLines % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Source Line " + counterProgressLines);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      if (!sourceLine.startsWith(sourceParameterFile.getCommentLinePrefix())) {
        counterSuccess++;
        TmpTextUnits = MyTextUnitCreator.createTextUnits(sourceLine);
        targetLine = MyTextUnitTokenizer
        .tokenizeText(TmpTextUnits[0].getContentsAsString());
        targetLine = targetLine.replace('*', '.').trim();
        targetLine = MyTextUnitNormalizer
        .normalizeText(targetLine);
        targetLine = MyMultiTokenWordIdentifier
        .identifyMultiTokenWords(targetLine);
        targetParameterFile.setNextLine(targetLine);
      }
      else {
        targetParameterFile.setNextLine(sourceLine);
      }

      counterProgress += sourceLine.length() + lengthOfLineFeedChars;
      counterProgressLines++;
      sourceLine = sourceParameterFile.getNextLine();
      
    }  // read all lines
    
    sourceParameterFile.close();
    targetParameterFile.close();

    Result.update(TaskResult.FINAL_RESULT,
    counterSuccess + " of " + counterProgressLines + 
    " lines in the source parameter text file have been\n" + 
    "tokenized. Results have been saved as target parameter text file\n" +
    Tools.shortenFileName(CastParameter.getTargetParameterFileName(), 60) + "!");
    this.setTaskResult(100, "Source Text File Processed ...", Result,
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