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

package org.hypknowsys.diasdem.tasks.prepare.importPlainTextFiles;

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
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ImportPlainTextFilesTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportPlainTextFilesParameter CastParameter = null;
  private ImportPlainTextFilesResult CastResult = null;
  
  private ArrayList AllFileNamesInDirectory = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Import Plain Text Files";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importPlainTextFiles"
  + ".ImportPlainTextFilesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importPlainTextFiles"
  + ".ImportPlainTextFilesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importPlainTextFiles"
  + ".ImportPlainTextFilesControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportPlainTextFilesTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    
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
    
    ImportPlainTextFilesParameter parameter = null;
    if (pParameter instanceof ImportPlainTextFilesParameter) {
      parameter = (ImportPlainTextFilesParameter)pParameter;
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
    
    return new ImportPlainTextFilesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ImportPlainTextFilesResult();
    
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
    
    if (Parameter != null && Parameter instanceof ImportPlainTextFilesParameter) {
      CastParameter = (ImportPlainTextFilesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, "Error: Text files cannot be imported!");
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

    HashMap metaData = null;
    TextBufferedReader sourceFile = null;
    String sourceFileName = null;
    String targetFileName = null;
    StringBuffer text = null;
    String currentLine = null;
    
    int counterProgress = 0;
    int maxProgress = AllFileNamesInDirectory.size();
    int collectionFiles = 0;
    
    for (int i = 0; i < AllFileNamesInDirectory.size(); i++) {
      
      counterProgress = i;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Text File " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      if ( ( (String)AllFileNamesInDirectory.get(i) )
      .endsWith(CastParameter.getFileNameFilter())) {
        collectionFiles++;
        sourceFileName = (String)AllFileNamesInDirectory.get(i);
        sourceFile = new TextBufferedReader( new File(sourceFileName) );
        sourceFile.open();
        text = new StringBuffer();
        currentLine = sourceFile.getFirstLine();
        while (currentLine != null) {
          text.append(currentLine);
          text.append(" ");
          currentLine = sourceFile.getNextLine();
        }
        sourceFile.close();
        metaData = new HashMap();
        metaData.put("SourceFile", sourceFileName);
        DiasdemDocument = DiasdemCollection.instantiateDefaultDiasdemDocument();
        DiasdemDocument.setOriginalText(text.toString().trim());
        DiasdemDocument.setMetaData(metaData);
        DiasdemCollection.addDocument(DiasdemDocument);
      }  // if: current file meets criterion
      
    }  // for: process all files in directory
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT,
    collectionFiles + " matching plain text files have been found in\n"
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