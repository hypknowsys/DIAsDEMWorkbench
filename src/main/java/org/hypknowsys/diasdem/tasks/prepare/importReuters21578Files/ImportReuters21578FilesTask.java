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

package org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files;

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

public class ImportReuters21578FilesTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportReuters21578FilesParameter CastParameter = null;
  private ImportReuters21578FilesResult CastResult = null;
  
  private ArrayList AllFileNamesInDirectory = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Import Reuters-21578 Files";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files"
  + ".ImportReuters21578FilesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files"
  + ".ImportReuters21578FilesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files"
  + ".ImportReuters21578FilesControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DEFAULT_REUTERS_21578_FILE_DIRECTORY", 
    "Default Directory of Source Reuters-21578 Files", 
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_REUTERS_21578_COLLECTION_TOPIC", 
    "Default Collection Topic of Source Reuters-21578 Files", 
    "", KProperty.STRING, KProperty.EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportReuters21578FilesTask() {
    
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
    
    ImportReuters21578FilesParameter parameter = null;
    if (pParameter instanceof ImportReuters21578FilesParameter) {
      parameter = (ImportReuters21578FilesParameter)pParameter;
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
    file = new File(parameter.getSourceDirectory());
    if (!file.exists() || !file.isDirectory()) {
      result.addError(
      "Error: Please enter the name of\n" +
      "an existing local directory in the\n" +
      "field 'Text File Directory'!");
    }
    if (parameter.getFileNameFilter().trim().length() <= 0) {
      result.addError(
      "Error: Please enter a valid file\n" +
      "name extension such as '.txt' in\n" +
      "the field 'File Name Extension'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ImportReuters21578FilesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ImportReuters21578FilesResult();
    
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
    
    if (Parameter != null && Parameter instanceof ImportReuters21578FilesParameter) {
      CastParameter = (ImportReuters21578FilesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    this.acceptTask(TaskProgress.INDETERMINATE, "Processing Reuters-21578 News");
    this.validateParameter(Parameter, "Error: Reuters-21578 files cannot be imported!");
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    
    File sourceDirectory = null;
    try {
      sourceDirectory = new File(CastParameter.getSourceDirectory());
    }
    catch(Exception e) { 
      e.printStackTrace(); 
      this.stop();
    }
    AllFileNamesInDirectory = new ArrayList();
    this.enumerateAllFileNames(sourceDirectory);

    // determine number of matching file for progress measurement
    int numberOfMatchingFiles = 0;
    for (int i = 0; i < AllFileNamesInDirectory.size(); i++) {
      if ( ( (String)AllFileNamesInDirectory.get(i) )
      .endsWith(CastParameter.getFileNameFilter())) {
        numberOfMatchingFiles++;
      } 
    }
    
    HashMap metaData = null;
    TextBufferedReader sourceFile = null;
    String sourceFileName = null;
    String targetFileName = null;
    StringBuffer text = null;
    StringBuffer topic = null;
    String currentLine = null;
    
    int counterProgress = 0;
    int maxProgress = numberOfMatchingFiles + 1;
    int collectionFiles = 0;
    
    for (int i = 0; i < AllFileNamesInDirectory.size(); i++) {
      
      if ( ( (String)AllFileNamesInDirectory.get(i) )
      .endsWith(CastParameter.getFileNameFilter())) {
        
        // Reuters-21578 corpus only consists of 22 files
        counterProgress++;
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Reuters-21578 File " + counterProgress + "/"
        + numberOfMatchingFiles);
        DiasdemServer.setTaskProgress(Progress, TaskThread);

        sourceFileName = (String)AllFileNamesInDirectory.get(i);
        sourceFile = new TextBufferedReader( new File(sourceFileName) );
        sourceFile.open();
        text = null;
        boolean collectNewsText = false;
        boolean collectTopic = false;
        currentLine = sourceFile.getFirstLine();
        while (currentLine != null) {
          
          if (currentLine.startsWith("<REUTERS")) {
            // save preceding document
            if (DiasdemDocument != null && text != null) {
              DiasdemDocument.setOriginalText(text.toString().trim());
              DiasdemDocument.setMetaData(metaData);
              if (((String)metaData.get("Topic")).indexOf(CastParameter
              .getTopicOfCollection()) >= 0
              || CastParameter.getTopicOfCollection() == null 
              || CastParameter.getTopicOfCollection().length() == 0) {
                DiasdemCollection.addDocument(DiasdemDocument);
                collectionFiles++;
              }
            }
            DiasdemDocument = DiasdemCollection
            .instantiateDefaultDiasdemDocument();
            metaData = new HashMap();
            metaData.put("SourceFile", sourceFileName);
            text = null;
            topic = null;
            collectNewsText = false;
            collectTopic = false;
          }
          
          if (currentLine.indexOf("<TITLE") >= 0) {
            collectNewsText = true;
            text = new StringBuffer(10000);
          }
          
          if (currentLine.indexOf("</TITLE") >= 0) {
            // ensure text unit split by creating artificial sentence
            currentLine = currentLine + " .";
          }
          
          if (currentLine.indexOf("<TOPICS") >= 0) {
            collectTopic = true;
            topic = new StringBuffer(10000);
          }
          
          if (collectNewsText) {
            // dateline is a stand-alone text unit
            currentLine = currentLine.replaceAll(
            "<DATELINE>.*</DATELINE>", "$0 .");
            currentLine = Tools.removeSgmlHtmlXmlMarkup(currentLine, true);
            // replace SGML-like certain <company names>, because of TreeTagger 
            currentLine = currentLine.replaceAll("&lt;","\\[");
            currentLine = currentLine.replaceAll(">", "\\]");
            currentLine = currentLine.replaceAll("&#3;", "");
            currentLine = currentLine.replaceAll("&apos;", "'");
            currentLine = currentLine.replaceAll("&amp;", "&");
            text.append(currentLine.trim());
            text.append(" ");
          }
          
          if (collectTopic) {
            topic.append(currentLine);
            topic.append(" ");
          }
          
          if (currentLine.indexOf("</BODY>") >= 0) {
            collectNewsText = false;
          }
          
          if (currentLine.indexOf("</TOPICS>") >= 0) {
            metaData.put("Topic", Tools.removeSgmlHtmlXmlMarkup(
            topic.toString(), true).trim());
            collectTopic = false;
          }
          
          currentLine = sourceFile.getNextLine();
          
        }
        // save last document
        if (DiasdemDocument != null && text != null) {
          DiasdemDocument.setOriginalText(text.toString().trim());
          DiasdemDocument.setMetaData(metaData);
          if (((String)metaData.get("Topic")).indexOf(CastParameter
          .getTopicOfCollection()) >= 0
          || CastParameter.getTopicOfCollection() == null
          || CastParameter.getTopicOfCollection().length() == 0) {
            DiasdemCollection.addDocument(DiasdemDocument);
            collectionFiles++;
          }
        }
        sourceFile.close();
      }  // if: current file meets criterion
      
    }  // for: process all files in directory
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT,
    collectionFiles + " news have been found in matching files in\n"
    + Tools.shortenFileName(CastParameter.getSourceDirectory(), 50) + ".\n" 
    + "They have been imported into the DIAsDEM collection\n" + Tools
    .shortenFileName(DiasdemCollection.getCollectionFileName(), 50) + ".\n"
    + "Currently, this collection comprises " + DiasdemCollection
    .getNumberOfDocuments() + " documents.");
    
    this.setTaskResult(100, collectionFiles + " Text Files Imported ...", 
    Result, TaskResult.FINAL_RESULT, Task.TASK_FINISHED);
    
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
  
  private void enumerateAllFileNames(File pInitialDirectory) {
  
    File[] allSourceFiles = pInitialDirectory.listFiles();
    for (int i = 0; i < allSourceFiles.length; i++) {
      if (allSourceFiles[i].isDirectory() 
      && CastParameter.includeSubdirectories()) {
        this.enumerateAllFileNames(allSourceFiles[i]);
      }
      else {
        AllFileNamesInDirectory.add(allSourceFiles[i].getAbsolutePath());
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