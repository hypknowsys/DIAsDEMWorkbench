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

package org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits;

import java.io.File;
import java.util.TreeSet;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.server.DiasdemScriptableNonBlockingTask;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.KProperty;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;
import org.hypknowsys.server.TaskParameter;
import org.hypknowsys.server.TaskProgress;
import org.hypknowsys.server.TaskResult;

/**
 * @version 2.2, 7 May 2006
 * @author Karsten Winkler
 */

public class ReplaceLabelsOfTextUnitsTask
extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ReplaceLabelsOfTextUnitsParameter CastParameter = null;
  private ReplaceLabelsOfTextUnitsResult CastResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String LABEL =
  "Replace Labels of Text Units";
  private static final String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits"
  + ".ReplaceLabelsOfTextUnitsParameter";
  private static final String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits"
  + ".ReplaceLabelsOfTextUnitsResult";
  private static final String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits"
  + ".ReplaceLabelsOfTextUnitsControlPanel";
  
  private static final KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("REPLACE_LABELS_OF_TEXT_UNITS:_FIND_LABEL", 
    "Replace Labels of Text Units: Default Find Label",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("REPLACE_LABELS_OF_TEXT_UNITS:_REPLACE_WITH", 
    "Replace Labels of Text Units: Default Replace With",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("REPLACE_LABELS_OF_TEXT_UNITS:_LOG_FILE_NAME", 
    "Replace Labels of Text Units: Default Log File Name",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceLabelsOfTextUnitsTask() {
    
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
    
    ReplaceLabelsOfTextUnitsParameter parameter = null;
    if (pParameter instanceof ReplaceLabelsOfTextUnitsParameter) {
      parameter = (ReplaceLabelsOfTextUnitsParameter)pParameter;
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
      "Error: Please enter the name of an existing local\n"
      + DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION
      + "-file in the field 'Collection File'!");
    }
    if (parameter.getCollectionFileName().trim().length() <= 0
    || !parameter.getCollectionFileName().trim().endsWith(
    DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local "
      + DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION
      + "-file name\nin the field 'Collection File'!");
    }

    if (parameter.getFindLabel().trim().length() <= 0) {
      result.addError(
      "Error: Please enter a text unit label\n"
      + "to find in the field 'Find Label'!");
    }
    if (parameter.getReplaceWith().trim().length() <= 0) {
      result.addError(
      "Error: Please enter a text unit label\n"
      + "to replace in the field 'Replace With'!");
    }
    
    if (parameter.getLogFileName().trim().length() <= 0
    || !parameter.getLogFileName().trim().endsWith(
    DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local "
      + DIAsDEMguiPreferences.TEXT_FILE_EXTENSION
      + "-file name\nin the field 'Log File'!");
    }
    file = new File(parameter.getLogFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n"
      + "'Log File' currently exists. Do you really\n"
      + "want to replace this file?");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ReplaceLabelsOfTextUnitsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ReplaceLabelsOfTextUnitsResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    DIAsDEMguiMenuBar.ACTIONS_MISCELLANEOUS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter 
    instanceof ReplaceLabelsOfTextUnitsParameter) {
      CastParameter = (ReplaceLabelsOfTextUnitsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    String shortErrorMessage = "Error: !";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    int numberOfReplacements = 0;
    TreeSet numberOfDistinctTextUnitClusters = new TreeSet();
    TextFile logFile = new TextFile(new File (CastParameter.getLogFileName()));
    logFile.open();
    logFile.setFirstLine("DiasdemDocumentID,ProcessedTextUnitID,Iteration,"
    + "ClusterID,ClusterLabelOld,ClusterLabelNew,"
    + "NumberOfDistinctTextUnitClusters");
    
    int counterProgress = 0;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update((int)(counterProgress * 100 / maxProgress),
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
        if (DiasdemTextUnit.getClusterLabel().equals(CastParameter
        .getFindLabel())) {
          numberOfDistinctTextUnitClusters.add(DiasdemTextUnit.getIteration()
          + ":" + DiasdemTextUnit.getClusterID());
          logFile.setNextLine(DiasdemDocument.getDiasdemDocumentID()
          + "," + i + "," + DiasdemTextUnit.getIteration() + ","
          + DiasdemTextUnit.getClusterID() + "," + DiasdemTextUnit
          .getClusterLabel() + "," + CastParameter.getReplaceWith()
          + "," + numberOfDistinctTextUnitClusters.size());
          DiasdemTextUnit.setClusterLabel(CastParameter.getReplaceWith());
          numberOfReplacements++;
        }
        DiasdemDocument.replaceProcessedTextUnit(i, DiasdemTextUnit);
      }
      
      DiasdemCollection.replaceDocument(DiasdemDocument
      .getDiasdemDocumentID(), DiasdemDocument);
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents
    
    super.closeDiasdemCollection();
    logFile.close();
    
    CastResult = new ReplaceLabelsOfTextUnitsResult(TaskResult.FINAL_RESULT,
    Tools.insertLineBreaks(60, numberOfReplacements + " labels of text units "
    + "have been altered from " + CastParameter.getFindLabel() + " to " 
    + CastParameter.getReplaceWith() + ". See the log file for details."),
    numberOfReplacements + " labels of text units have been replaced.");
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
  
  public static void main(String pOptions[]) {}
  
}