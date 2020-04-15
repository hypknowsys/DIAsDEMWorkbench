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

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ConvertTextUnitsTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ConvertTextUnitsParameter CastParameter = null;
  private ConvertTextUnitsResult CastResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient Pattern RegexPattern = null;
  private transient Matcher RegexMatcher = null;  
  private MultiTokenWordIdentifier MyMultiTokenWordIdentifier = null;
  private TokenReplacer MyTokenReplacer = null;
  private transient int BeginTag = 0;
  private transient int EndTag = 0;
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient StringTokenizer TmpStringTokenizer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Convert Text Units";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.convertTextUnits"
  + ".ConvertTextUnitsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.convertTextUnits"
  + ".ConvertTextUnitsResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.convertTextUnits"
  + ".ConvertTextUnitsControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DEFAULT_CONVERSION_TYPE_INDEX", 
    "Convert Text Units: Default Conversion Type Index",
    "0", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_CONVERSION_REGULAR_EXPRESSION", 
    "Convert Text Units: Default Conversion Regular Expression",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_CONVERSION_REPLACEMENT_STRING", 
    "Convert Text Units: Default Conversion Replacement String",
    "", KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertTextUnitsTask() {
    
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
    
    ConvertTextUnitsParameter parameter = null;
    if (pParameter instanceof ConvertTextUnitsParameter) {
      parameter = (ConvertTextUnitsParameter)pParameter;
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
    
    if (parameter.getRegularExpression() != null && parameter
    .getRegularExpression().length() > 0 && !Tools.isSyntacticallyCorrectRegex(
    parameter.getRegularExpression())) {
      result.addError(
      "Error: Please enter a syntactically correct regular\n" +
      "expression in the field 'Regular Expression'!");
    }
    
    if (!Tools.stringIsNullOrEmpty(parameter.getMultiTokenFileName())) {
      file = new File(parameter.getMultiTokenFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the field 'Multi Token Words File'!");
      }
    }
    if (!Tools.stringIsNullOrEmpty(parameter.getTokenReplacementFileName())) {
      file = new File(parameter.getTokenReplacementFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the field 'Token Replacement File'!");
      }
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ConvertTextUnitsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ConvertTextUnitsResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_PREPARE_DATA_SET,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof ConvertTextUnitsParameter) {
      CastParameter = (ConvertTextUnitsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Text Units Cannot be Converted!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    if (CastParameter.getConversionType() == ConvertTextUnitsParameter
    .APPLY_REGULAR_EXPRESSION_TO_TEXT_UNITS) {
      RegexPattern = Pattern.compile(CastParameter.getRegularExpression());
    }
        
    if (CastParameter.getConversionType() == ConvertTextUnitsParameter
    .IDENTIFY_SPECIFIED_MULTI_TOKEN_TERMS) {
      // read multi token word: each line contains one multi token word;
      // comment lines start with '#'
      TextFile multiTokenFile = new TextFile(
      new File(CastParameter.getMultiTokenFileName()));
      multiTokenFile.open();
      String line = multiTokenFile.getFirstLineButIgnoreCommentsAndEmptyLines();
      ArrayList list = new ArrayList();
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
    }
    else {
      MyMultiTokenWordIdentifier = null;
    }
    
    if (CastParameter.getConversionType() == ConvertTextUnitsParameter
    .FIND_AND_REPLACE_SPECIFIED_TOKENS) {
      // read token replacement file: each line contains the tokens to search 
      // for and the replacement tokens separated by a tab stop;
      // comment lines start with '#'
      TextFile tokenReplacementFile = new TextFile(
      new File(CastParameter.getTokenReplacementFileName()));
      tokenReplacementFile.open();
      String line = tokenReplacementFile.getFirstLineButIgnoreCommentsAndEmptyLines();
      HashMap tokensSearchList = new HashMap();
      String[] tokensContents = null;
      while (line != null) {
        tokensContents = line.split("\t");
        if (tokensContents.length == 2
        && tokensContents[1].trim().length() > 0) {
          try {
            tokensSearchList.put(tokensContents[0].trim(), 
            tokensContents[1].trim());
          }
          catch (PatternSyntaxException e) {
            System.out.println("[TokenizeTextUnitsTask] Regex syntax error in "
            + " file " + CastParameter.getTokenReplacementFileName() + ": "
            + " Line \"" + line + "\"; error message: " + e.getMessage());
          }
        }
        else {
          System.out.println("[TokenizeTextUnitsTask] Error in file "
          + CastParameter.getTokenReplacementFileName() + ": Line \""
          + line + "\" does not conform to syntax!");
        }
        line = tokenReplacementFile.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      // sort multi token terms by decreasing length
      ArrayList list = new ArrayList(tokensSearchList.keySet());
      Collections.sort(list, new SortStringsByDecreasingLength());
      // create arrays for token replacement      
      String[] tokensSearch = new String[list.size()];
      String[] tokensReplace = new String[list.size()];
      Iterator iterator = list.iterator();
      int i = 0;
      while (iterator.hasNext()) {
        TmpString = (String)iterator.next();
        tokensSearch[i] = TmpString;
        tokensReplace[i++] = (String)tokensSearchList.get(TmpString);
      }
      tokenReplacementFile.close();
      MyTokenReplacer = new HeuristicTokenReplacer(tokensSearch, tokensReplace);
    }
    else {
      MyTokenReplacer = null;
    }
    
    int counterProgress = 0;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();    
    while (DiasdemDocument != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }

      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      DiasdemDocument.backupProcessedTextUnits(DiasdemProject
      .getProcessedTextUnitsRollbackOption());
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits(); 
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        switch (CastParameter.getConversionType()) {
          case ConvertTextUnitsParameter.CONVERT_TEXT_UNITS_TO_LOWER_CASE: {
            TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString()
            .toLowerCase();
            break;
          }
          case ConvertTextUnitsParameter.CONVERT_TEXT_UNITS_TO_UPPER_CASE: {
            TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString()
            .toUpperCase();
            break;
          }         
          case ConvertTextUnitsParameter.APPLY_REGULAR_EXPRESSION_TO_TEXT_UNITS: {
            RegexMatcher = RegexPattern.matcher(DiasdemTextUnit
            .getContentsAsString());
            TmpStringBuffer = new StringBuffer(DiasdemTextUnit
            .getContentsAsString().length() + 10000);
            while (RegexMatcher.find()) {
              RegexMatcher.appendReplacement(TmpStringBuffer,
              CastParameter.getReplacementString());
            }
            TextUnitContentsAsString = RegexMatcher.appendTail(TmpStringBuffer)
            .toString();
            break;
          }
          case ConvertTextUnitsParameter.IDENTIFY_SPECIFIED_MULTI_TOKEN_TERMS: {
            TextUnitContentsAsString = MyMultiTokenWordIdentifier
            .identifyMultiTokenWords(DiasdemTextUnit.getContentsAsString());
            break;
          }         
          case ConvertTextUnitsParameter.FIND_AND_REPLACE_SPECIFIED_TOKENS: {
            TextUnitContentsAsString = MyTokenReplacer
            .replaceTokens(DiasdemTextUnit.getContentsAsString());
            break;
          }         
          case ConvertTextUnitsParameter.REMOVE_PART_OF_SPEECH_TAGS_FROM_TOKENS: {
            TextUnitContentsAsString = this.removeTagsFromTokens(
            DiasdemTextUnit.getContentsAsString(), "/p:");
            break;
          }         
          case ConvertTextUnitsParameter.REMOVE_WORD_SENSE_TAGS_FROM_TOKENS: {
            TextUnitContentsAsString = this.removeTagsFromTokens(
            DiasdemTextUnit.getContentsAsString(), "/s:");
            break;
          }         
          default: {
            TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
          }
        }
        DiasdemTextUnit.setContentsFromString(TextUnitContentsAsString);
        DiasdemDocument.replaceProcessedTextUnit(i, DiasdemTextUnit);
      }

      DiasdemCollection.replaceDocument(DiasdemDocument
      .getDiasdemDocumentID(), DiasdemDocument);
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents
    
    super.closeDiasdemCollection();
    
    CastResult = new ConvertTextUnitsResult(TaskResult.FINAL_RESULT,
    "All processed text units have been converted in the DIAsDEM document\n" +
    " collection " +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 55) + "!", 
    "Processed text units have been converted.");
    this.setTaskResult(100, "All Documents Processed ...", CastResult,
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
  
  private String removeTagsFromTokens(String pTokens, String pTagPrefix) {
    
    TmpStringBuffer = new StringBuffer(pTokens.length() + 1000); 
    TmpStringTokenizer = new StringTokenizer(pTokens);
    while (TmpStringTokenizer.hasMoreTokens()) {
      TmpString = TmpStringTokenizer.nextToken();
      BeginTag = TmpString.indexOf(pTagPrefix);
      if (BeginTag >= 0) {
        TmpStringBuffer.append(new String(TmpString.substring(0, BeginTag)));
        EndTag = TmpString.indexOf("/", BeginTag + 1);
        if (EndTag > BeginTag && EndTag < TmpString.length()) {
          TmpStringBuffer.append(new String(TmpString.substring(EndTag)));
        }
      }
      else {
        TmpStringBuffer.append(TmpString);
      }
      TmpStringBuffer.append(" ");
    }    
    
    return TmpStringBuffer.toString().trim();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}