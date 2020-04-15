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

package org.hypknowsys.diasdem.tasks.prepare.disambiguateWordSenses;

import java.io.*;
import java.util.*;
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

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class DisambiguateWordSensesTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DisambiguateWordSensesParameter CastParameter = null;
  private DisambiguateWordSensesResult CastResult = null;
  private WordSenseDisambiguator MyWordSenseDisambiguator = null;
  
  private TextFile DebuggingHtmlFile = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Disambiguate Word Senses";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.disambiguateWordSenses"
  + ".DisambiguateWordSensesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.disambiguateWordSenses"
  + ".DisambiguateWordSensesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.disambiguateWordSenses"
  + ".DisambiguateWordSensesControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DISAMBIGUATE_WORD_SENSES:_DEFAULT_WORD_SENSES_FILE", 
    "Diambiguate Word Senses: Default Word Senses File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DISAMBIGUATE_WORD_SENSES:_DEFAULT_DEBUGGING_HTML_FILE", 
    "Diambiguate Word Senses: Default Debugging HTML File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DisambiguateWordSensesTask() {
    
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
    
    DisambiguateWordSensesParameter parameter = null;
    if (pParameter instanceof DisambiguateWordSensesParameter) {
      parameter = (DisambiguateWordSensesParameter)pParameter;
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
    file = new File(parameter.getWordSensesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing\nlocal " +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Word Senses File'!");
    }
    if (!Tools.stringIsNullOrEmpty(parameter.getDebuggingHtmlFileName())) {
      if (!Tools.isValidandWriteableFileName(parameter
      .getDebuggingHtmlFileName(), DIAsDEMguiPreferences.HTML_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter a valid local " +
        DIAsDEMguiPreferences.HTML_FILE_EXTENSION +
        "-file\nname in the field 'Debugging HTML File'!");
      }
      result.addWarning(
      "Warning: Disambiguating word senses involves\n"
      + "many write operations on HTML files, which are\n"
      + "often monitored by virus scanners. Hence, virus\n"
      + "scanners should be temporarily shut down to en-\n"
      + "sure a high performance quality evaluation. Note:\n"
      + "Warnings like this one can be disabled in the\n"
      + "Tools -> Options dialog. Do you want to proceed?");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new DisambiguateWordSensesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new DisambiguateWordSensesResult();
    
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
    
    if (Parameter != null && Parameter instanceof DisambiguateWordSensesParameter) {
      CastParameter = (DisambiguateWordSensesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Word senses cannot be disambiguated!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
        
    DebuggingHtmlFile = null;
    if (!Tools.stringIsNullOrEmpty(CastParameter.getDebuggingHtmlFileName())) {
      Template DebuggingOutputHeader = new Template(Tools
      .stringFromTextualSystemResource("org/hypknowsys/diasdem/resources/html/"
      + "HtmlFile_HeaderTemplate.html"));
      DebuggingOutputHeader.addValue("${Title}", "Disambiguate Word Senses");
      DebuggingHtmlFile = new TextFile(new File(CastParameter
      .getDebuggingHtmlFileName()));
      DebuggingHtmlFile.open();
      DebuggingHtmlFile.setFirstLine(DebuggingOutputHeader.insertValues());
      DebuggingHtmlFile.setNextLine(
      "<p>Created by Tasks &gt; Prepare Data Set " +
      "&gt; Disambiguate Word Senses on " + Tools.getSystemDate() +"</p>");
      DebuggingHtmlFile.setNextLine(
      "<h3>Processed Text Units with Tokens listed in Word Senses File</h3>");
    }
    
    MyWordSenseDisambiguator = new WordSenseDisambiguatorBasedOnContextWords(
    CastParameter.getWordSensesFileName(), DebuggingHtmlFile);
    
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
        TextUnitContentsAsString = MyWordSenseDisambiguator
        .disambiguateWordSenses(DiasdemTextUnit.getContentsAsString(),
        DiasdemDocument.getOriginalTextUnit(i).getContentsAsString(),
        DiasdemDocument);
        DiasdemTextUnit.setContentsFromString(TextUnitContentsAsString);
        DiasdemDocument.replaceProcessedTextUnit(i, DiasdemTextUnit);
      }

      DiasdemCollection.replaceDocument(DiasdemDocument
      .getDiasdemDocumentID(), DiasdemDocument);
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents
    
    if (!Tools.stringIsNullOrEmpty(CastParameter.getDebuggingHtmlFileName())) {
      DebuggingHtmlFile.setNextLine(Tools.stringFromTextualSystemResource(
      "org/hypknowsys/diasdem/resources/html/HtmlFile_FooterTemplate.html"));
      DebuggingHtmlFile.close();
    }    
    super.closeDiasdemCollection();
    
    CastResult = new DisambiguateWordSensesResult(TaskResult.FINAL_RESULT,
    "The word senses of tokens in the DIAsDEM collection\n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) +
    "\nhave been successfully disambiguated", 
    "Word senses disambiguated");
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}