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

package org.hypknowsys.diasdem.tasks.postprocess.tagTextUnits;

import java.io.File;
import java.util.StringTokenizer;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.server.DiasdemScriptableNonBlockingTask;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;
import org.hypknowsys.server.TaskParameter;
import org.hypknowsys.server.TaskProgress;
import org.hypknowsys.server.TaskResult;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class TagTextUnitsTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TagTextUnitsParameter CastParameter = null;
  
  private String CurrentXmlFileName = "something";
  private String NewXmlFileName = null;
  private int NewSentenceID = 0;
  private int PreviousSentenceID = 0;
  private int SentenceCounter = 0;
  private int NewClusterID = -1;
  
  private StringTokenizer Tokenizer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String LABEL =
  "Tag Text Units";
  private static final String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagTextUnits"
  + ".TagTextUnitsParameter";
  private static final String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagTextUnits"
  + ".TagTextUnitsResult";
  private static final String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagTextUnits"
  + ".TagTextUnitsControlPanel";
  
  // the filename is restricted to 25 characters in output file
  private static final String FILE_EXTENSION = "";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagTextUnitsTask() {
    
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
    
    TagTextUnitsParameter parameter = null;
    if (pParameter instanceof TagTextUnitsParameter) {
      parameter = (TagTextUnitsParameter)pParameter;
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
    file = new File(parameter.getClusterLabelFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.CLUSTER_LABEL_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n"
      + DIAsDEMguiPreferences.CLUSTER_LABEL_FILE_EXTENSION
      + "-file in the field 'Cluster Label File'!");
    }
    file = new File(parameter.getClusterResultFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(this.getRequiredFileExtension(parameter))) {
      result.addError(
      "Error: Please enter the name of an existing local\n"
      + this.getRequiredFileExtension(parameter)
      + "-file in the field 'Cluster Result File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new TagTextUnitsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new TagTextUnitsResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    DIAsDEMguiMenuBar.ACTIONS_POSTPROCESS_PATTERNS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof TagTextUnitsParameter) {
      CastParameter = (TagTextUnitsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    String shortErrorMessage = "Error: Text units cannot be tagged!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    DIAsDEMthesaurus clusterDescriptions = new DefaultDIAsDEMthesaurus();
    clusterDescriptions.load(CastParameter.getClusterLabelFileName());
    DIAsDEMthesaurusTerm currentTerm = null;
    String line = null;
    int taggedTextUnits = 0;
    
    TextFile clusterResultFile = new TextFile(
    new File(CastParameter.getClusterResultFileName()));
    clusterResultFile.open();
    int counterProgress = 0;
    long maxProgress = Math.max(1, DiasdemCollection
    .getNumberOfUntaggedTextUnits());  // avoid division by zero
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    boolean newFile = false;
    line = clusterResultFile.getFirstLine();
    if (CastParameter.ignoreFirstResultFileLine()) {
      line = clusterResultFile.getNextLine();
    }
    while (line != null) {
      
      counterProgress++;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update((int)(counterProgress * 100 / maxProgress),
        "Processing Text Unit " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      this.extractResultsFromCurrentLine(line);
      
      if (! NewXmlFileName.equals(CurrentXmlFileName)) {
        newFile = true;
        CurrentXmlFileName = NewXmlFileName;
        // System.out.println(CurrentXmlFileName);
        
        // update previous XML file
        if (DiasdemDocument != null) {
          DiasdemCollection.replaceDocument(DiasdemDocument
          .getDiasdemDocumentID(), DiasdemDocument);
        }
        DiasdemDocument = DiasdemCollection.getDocument(
        CurrentXmlFileName.trim());
        if (DiasdemDocument != null) {
          DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
          .getActiveTextUnitsLayerIndex());
          DiasdemDocument.backupProcessedTextUnits(DiasdemProject
          .getProcessedTextUnitsRollbackOption());
        }
      }
      else {
        newFile = false;
      }
      
      if (NewClusterID == 0 
      && clusterDescriptions.get(NewClusterID + "") == null) {
        // Note: In releases prior to 2.2, cluster 0 is a garbage cluster that
        // is not contained in the cluster label file. Hence, cluster 0 must be
        // created on the fly when tagging text units. In the current release,
        // cluster 0 exists in the cluster label file as well.
        currentTerm = new DefaultDIAsDEMthesaurusTerm();
        currentTerm.setScopeNotes("-");
      }
      else {
        currentTerm = clusterDescriptions.get(NewClusterID + "");
      }
      
      if (DiasdemDocument != null) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(NewSentenceID);
        if (DiasdemTextUnit != null
        && (DiasdemTextUnit.getClusterLabel() == null
        || DiasdemTextUnit.getClusterLabel().equals("-"))) {
          if (!currentTerm.getScopeNotes().equals("-")) {
            taggedTextUnits++;
          }
          DiasdemTextUnit.setIteration(CastParameter.getIteration());
          DiasdemTextUnit.setClusterLabel(currentTerm.getScopeNotes());
          DiasdemTextUnit.setClusterID(NewClusterID);
          DiasdemDocument.replaceProcessedTextUnit(NewSentenceID,
          DiasdemTextUnit);
        }
      }
      
      line = clusterResultFile.getNextLine();
      
    }  // read all lines of result file
    
    clusterResultFile.close();
    super.closeDiasdemCollection();
    
    Result.update(TaskResult.FINAL_RESULT,
    taggedTextUnits + " of " + maxProgress
    + " untagged text units have been assigned\n"
    + "a semantic label in the DIAsDEM document collection\n"
    + Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) + "!");
    this.setTaskResult(100, "All Text Units Processed ...", Result,
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
  
  private void extractResultsFromCurrentLine(String pLine) {
    
    switch (CastParameter.getClusterResultFileFormat()) {
      
      case TagTextUnitsParameter.CSV_FILE: {
        // format is hard-coded
        Tokenizer = new StringTokenizer(pLine, ",");
        NewXmlFileName =  Tokenizer.nextToken();
        PreviousSentenceID = NewSentenceID;
        NewSentenceID = (new Integer(Tokenizer.nextToken().trim()))
        .intValue();
        NewClusterID = (new Integer(Tokenizer.nextToken().trim()))
        .intValue();
        break;
      }
      case TagTextUnitsParameter.TXT_FILE: {
        // format is hard-coded
        NewXmlFileName = pLine.substring(20, 44).trim() + FILE_EXTENSION;
        PreviousSentenceID = NewSentenceID;
        NewSentenceID = (new Integer(pLine.substring(45, 49).trim()))
        .intValue();
        // clusterID ist dreistellig
        if (pLine.substring(55, Math.min(58, pLine.length())).trim()
        .equals("")) {
          NewClusterID = 0;
        }
        else {
          NewClusterID = (new Integer(pLine.substring(55,
          Math.min(58, pLine.length())).trim())).intValue();
        }
        break;
      }
      
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getRequiredFileExtension(
  TagTextUnitsParameter pParameter) {
    
    if (pParameter != null && pParameter.getClusterResultFileFormat()
    == TagTextUnitsParameter.CSV_FILE) {
      return DIAsDEMguiPreferences.CSV_FILE_EXTENSION;
    }
    else if (pParameter != null && pParameter.getClusterResultFileFormat()
    == TagTextUnitsParameter.TXT_FILE) {
      return DIAsDEMguiPreferences.TEXT_FILE_EXTENSION;
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}