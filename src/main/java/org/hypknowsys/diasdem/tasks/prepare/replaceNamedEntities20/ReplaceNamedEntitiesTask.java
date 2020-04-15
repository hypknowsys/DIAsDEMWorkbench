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

package org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities20;

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
import org.hypknowsys.diasdem.core.neex.*;
import org.hypknowsys.diasdem.client.gui.*;
import gnu.regexp.*;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class ReplaceNamedEntitiesTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ReplaceNamedEntitiesParameter CastParameter = null;
  
  private NamedEntityExtractor20 CurrentExtractor = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Replace Named Entities 2.0";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities20"
  + ".ReplaceNamedEntitiesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities20"
  + ".ReplaceNamedEntitiesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities20"
  + ".ReplaceNamedEntitiesControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceNamedEntitiesTask() {
    
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
    
    ReplaceNamedEntitiesParameter parameter = null;
    if (pParameter instanceof ReplaceNamedEntitiesParameter) {
      parameter = (ReplaceNamedEntitiesParameter)pParameter;
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
    
    file = new File(parameter.getForenamesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Forenames File'!");
    }
    file = new File(parameter.getSurnamesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Surnames File'!");
    }
    file = new File(parameter.getSurnameSuffixesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Surname Suffixes File'!");
    }
    file = new File(parameter.getMiddleInitialsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Middle Initials File'!");
    }
    file = new File(parameter.getTitlesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Titles File'!");
    }
    file = new File(parameter.getPlacesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Places File'!");
    }
    file = new File(parameter.getOrganizationsStartFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Organizations Start File'!");
    }
    file = new File(parameter.getOrganizationsEndFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Organizations End File'!");
    }
    file = new File(parameter.getCompositeFeaturesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.CSV_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.CSV_FILE_EXTENSION +
      "-file in the field 'Composite NE File'!");
    }
    file = new File(parameter.getRegularExpressionsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.CSV_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.CSV_FILE_EXTENSION +
      "-file in the field 'Regex NE File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ReplaceNamedEntitiesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ReplaceNamedEntitiesResult();
    
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
    
    if (Parameter != null && Parameter instanceof ReplaceNamedEntitiesParameter) {
      CastParameter = (ReplaceNamedEntitiesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    String shortErrorMessage = "Error: Named entities cannot be replaced!";
    this.acceptTask(TaskProgress.INDETERMINATE, 
    "Loading Lists of Known Named Entities");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);        
    
    CurrentExtractor = new NamedEntityExtractor20(CastParameter
    .getNeexParameter());
    
    int counterProgress = 1;
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
      
      DiasdemDocument.getActiveTextUnitsLayer().resetNamedEntities();
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits(); 
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
        if (TextUnitContentsAsString.indexOf(NamedEntity
        .PLACEHOLDER_PREFIX) >= 0) {
          this.setErrorTaskResult(100, shortErrorMessage,
          "Error: The default active text units " +
          "layer " + DiasdemProject.getActiveTextUnitsLayerIndex() +
          "\ncomprises processed text units that contain\n" +
          "placeholders of previously extracted named\n" +
          "entities. Rollback this text units layer to a\n" +
          "savepoint whose processed text units do\n" +
          "not contain named entity placeholders!");
          this.stop();
        }
        DiasdemTextUnit.setContentsFromString(CurrentExtractor
        .extractNamedEntities(TextUnitContentsAsString, DiasdemDocument
        .getActiveTextUnitsLayer()));
        DiasdemDocument.replaceProcessedTextUnit(i, DiasdemTextUnit);
      }      
      CurrentExtractor.createCanonicalNEs(DiasdemDocument
      .getActiveTextUnitsLayer());
      
      DiasdemCollection.replaceDocument(DiasdemDocument.getDiasdemDocumentID(),
      DiasdemDocument);
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT,
    "All identified named entities have been replaced\n" +
    "in DIAsDEM documents contained in the collection\n" +
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}