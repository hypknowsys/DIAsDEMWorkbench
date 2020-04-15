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

package org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22;

import java.io.File;
import java.util.StringTokenizer;
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
 * @version 2.1.2.0, 13 May 2004
 * @author Karsten Winkler
 */

public class MonitorClusterQualityTask
extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private MonitorClusterQualityParameter CastParameter = null;
  
  private TextUnitClusterSet MyTextUnitClusterSet = null;
  private String CurrentXmlFileName = "something";
  private String NewXmlFileName = null;
  private int NewSentenceID = 0;
  private int PreviousSentenceID = 0;
  private int SentenceCounter = 0;
  private int NewClusterID = -1;
  private File DumpDirectoryFile = null;
  private String DumpDirectoryFileName = null;
  private int DumpDirectoryIndex = 1;
  private int FilesInDumpDirectory = 0;
  private int MaxFilesInDumpDirectory = 0;
  
  private StringTokenizer Tokenizer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String LABEL =
  "Monitor Cluster Quality 2.2";
  private static final String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22"
  + ".MonitorClusterQualityParameter";
  private static final String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22"
  + ".MonitorClusterQualityResult";
  private static final String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22"
  + ".MonitorClusterQualityControlPanel";
  
  // the filename is restricted to 25 characters in output file
  private static final String FILE_EXTENSION = "";
  
  private static final KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_MIN_CLUSTER_SIZE",
    "Monitor Cluster Quality 2.2: Min. Cluster Size",
    "50", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_MAX_DESCRIPTOR_COVERAGE",
    "Monitor Cluster Quality 2.2: Max. Descriptor Coverage [0; 1]",
    "0.25", KProperty.DOUBLE, KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_MIN_DESCRIPTOR_DOMINANCE",
    "Monitor Cluster Quality 2.2: Min. Descriptor Dominance [0; 1]",
    "0.25", KProperty.DOUBLE, KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_DOMINANT_DESCRIPTOR_THRESHOLD",
    "Monitor Cluster Quality 2.2: Dominant Descriptor Threshold [0; 1]",
    "0.8", KProperty.DOUBLE, KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_RARE_DESCRIPTOR_THRESHOLD",
    "Monitor Cluster Quality 2.2: Rare Descriptor Threshold [0; 1]",
    "0.005", KProperty.DOUBLE, KProperty.NOT_EDITABLE),
    new KProperty(
    "MONITOR_CLUSTER_QUALITY_22:_FREQUENT_NONDESCRIPTOR_THRESHOLD",
    "Monitor Cluster Quality 2.2: Frequent Non-Descriptor Threshold [0; 1]",
    "0.2", KProperty.DOUBLE, KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_MAX_NUMBER_OF_OUTPUT_TEXT_UNITS",
    "Monitor Cluster Quality 2.2: Max. Number of Output Text Units",
    "1000", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_IGNORE_FIRST_LINE",
    "Monitor Cluster Quality 2.2: Default Setting of Ignore First Line of "
    + "Cluster File", "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_IGNORE_EMPTY_CLUSTERS",
    "Monitor Cluster Quality 2.2: Default Setting of Ignore Empty Clusters "
    + "in Cluster Index HTML File", "false", KProperty.BOOLEAN,
    KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_RANK_CLUSTERS_BY_QUALITY",
    "Monitor Cluster Quality 2.2: Default Setting of Rank Clusters by Quality "
    + "in Cluster Index HTML File", "false", KProperty.BOOLEAN,
    KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_LAUNCH_WEB_BROWSER",
    "Monitor Cluster Quality 2.2: Default Setting of Launch Web Browser with "
    + "Cluster Index HTML File", "false", KProperty.BOOLEAN,
    KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_LAUNCH_CLUSTER_LABEL_EDITOR",
    "Monitor Cluster Quality 2.2: Default Setting of Lauch Cluster Label "
    + "Editor with Cluster Label File", "false", KProperty.BOOLEAN,
    KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_DUMP_DIASDEM_DOCUMENTS",
    "Monitor Cluster Quality 2.2: Default Setting of Dump DIAsDEM Documents "
    + "for Visualization", "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty(
    "MONITOR_CLUSTER_QUALITY_22:_IGNORE_TEXT_UNITS_IN_OUTLIER_CLUSTER",
    "Monitor Cluster Quality 2.2: Default Setting of Ignore Text Units In "
    + "Outlier Cluster", "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("MONITOR_CLUSTER_QUALITY_22:_OUTLIER_CLUSTER_ID",
    "Monitor Cluster Quality 2.2: Default Setting of Outlier Cluster ID",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE)
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityTask() {
    
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
    
    MonitorClusterQualityParameter parameter = null;
    if (pParameter instanceof MonitorClusterQualityParameter) {
      parameter = (MonitorClusterQualityParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    result.addWarning(
    "Warning: Monitoring the cluster quality involves\n"
    + "many write operations on HTML files, which are\n"
    + "often monitored by virus scanners. Hence, virus\n"
    + "scanners should be temporarily shut down to en-\n"
    + "sure a high performance quality evaluation. Note:\n"
    + "Warnings like this one can be disabled in the\n"
    + "Tools -> Options dialog. Do you want to proceed?");
    
    File file = new File(parameter.getCollectionFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n"
      + DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION
      + "-file in the field 'Collection File'!");
    }
    file = new File(parameter.getThesaurusFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n"
      + DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION
      + "-file in the field 'Thesaurus File'!");
    }
    file = new File(parameter.getClusterResultFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(this.getRequiredFileExtension(parameter))) {
      result.addError(
      "Error: Please enter the name of an existing local\n"
      + this.getRequiredFileExtension(parameter)
      + "-file in the field 'Cluster Result File'!");
    }
    file = new File(parameter.getClusterDirectory());
    if (file.exists() && file.isDirectory() && file.list() != null
    && file.list().length > 0) {
      result.addWarning(
      "Warning: The directory specified in the field\n"
      + "'Cluster Directory' is not empty. Do you really\n"
      + "want to select this directory?");
    }
    file = new File(parameter.getClusterDirectory());
    if (!file.exists()) {
      try {
        boolean success = file.mkdirs();
      }
      catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    if (!file.exists() || !file.isDirectory()) {
      result.addError(
      "Please enter the name of an existing local\n"
      + "directory in the field 'Cluster Directory'!");
    }
    if (parameter.getClusterLabelFileName().trim().length() <= 0
    || !parameter.getClusterLabelFileName().trim().endsWith(
    DIAsDEMguiPreferences.CLUSTER_LABEL_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local "
      + DIAsDEMguiPreferences.CLUSTER_LABEL_FILE_EXTENSION
      + "-file name\nin the field 'Cluster Labels File'!");
    }
    file = new File(parameter.getClusterLabelFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n"
      + "'Cluster Labels File' currently exists.\n"
      + "Do you really want to replace this file?");
    }
    
    if (parameter.getNumberOfClusters() <= 0) {
      result.addError(
      "Please enter a non-negative integer\n"
      + "in the field 'Max. Cluster ID'!");
    }
    if (parameter.getMinClusterSize() <= 0) {
      result.addError(
      "Please enter a non-negative integer\n"
      + "in the field 'Min. Cluster Size'!");
    }
    if (parameter.getMaxDescriptorCoverage() < 0.0D
    || parameter.getMaxDescriptorCoverage() > 1.0D) {
      result.addError(
      "Please enter a double [0.0; 1.0] in\n"
      + "the field 'Max. Descriptor Coverage'!");
    }
    if (parameter.getMinDescriptorDominance() < 0.0D
    || parameter.getMinDescriptorDominance() > 1.0D) {
      result.addError(
      "Please enter a double [0.0; 1.0] in\n"
      + "the field 'Min. DescriptorDominance'!");
    }
    if (parameter.getIteration() < 1) {
      result.addError(
      "Please enter a non-negative integer\n"
      + "in the field 'KDD Process Iteration'!");
    }
    if (parameter.getDominantDescriptorThreshold() < 0.0D
    || parameter.getDominantDescriptorThreshold() > 1.0D) {
      result.addError(
      "Please enter a double [0.0; 1.0] in the\n"
      + "field 'Dominant Descriptor Threshold'!");
    }
    if (parameter.getRareDescriptorThreshold() < 0.0D
    || parameter.getRareDescriptorThreshold() > 1.0D) {
      result.addError(
      "Please enter a double [0.0; 1.0] in the\n"
      + "field 'Rare Descriptor Threshold'!");
    }
    if (parameter.getFrequentNonDescriptorThreshold() < 0.0D
    || parameter.getFrequentNonDescriptorThreshold() > 1.0D) {
      result.addError(
      "Please enter a double [0.0; 1.0] in the\n"
      + "field 'Frequent Non-Descriptor Threshold'!");
    }
    if (parameter.getMaxNumberOfOutputTextUnits() < 0) {
      result.addError(
      "Please enter an integer >= 0 in the field\n"
      + "'Max. Number of Output Text Units'!");
    }
    if (parameter.ignoreTextUnitsInOutlierCluster()
    && parameter.getOutlierClusterID() < 0) {
      result.addError(
      "Please enter an integer >= 0 in\n"
      + "the field 'Outlier Cluster ID'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new MonitorClusterQualityParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new MonitorClusterQualityResult();
    
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
    
    if (Parameter != null
    && Parameter instanceof MonitorClusterQualityParameter) {
      CastParameter = (MonitorClusterQualityParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    String shortErrorMessage = "Error: Cluster quality cannot be monitored!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    MyTextUnitClusterSet = new TextUnitClusterSet(
    DiasdemProject.getProjectName(),
    DiasdemProject.getProjectFileName(),
    CastParameter.getCollectionFileName(),
    CastParameter.getIteration(),
    CastParameter.getThesaurusFileName(),
    CastParameter.getNumberOfClusters(),
    CastParameter.getClusterDirectory(),
    CastParameter.getMinClusterSize(),
    CastParameter.getMaxDescriptorCoverage(),
    CastParameter.getMinDescriptorDominance(),
    CastParameter.getVectorDimensions(),
    CastParameter.getDescriptorsScopeNotesContain(),
    CastParameter.getClusterLabelFileName(),
    CastParameter.ignoreEmptyClusters(),
    CastParameter.rankClustersByQuality(),
    CastParameter.getDominantDescriptorThreshold(),
    CastParameter.getRareDescriptorThreshold(),
    CastParameter.getFrequentNonDescriptorThreshold(),
    CastParameter.getMaxNumberOfOutputTextUnits(),
    CastParameter.getClusterResultFileName(),
    CastParameter.ignoreTextUnitsInOutlierCluster(),
    CastParameter.getOutlierClusterID());
    
    String line = null;
    String lineGrundformen = null;
    String lineZerlegt = null;
    
    TextFile clusterResultFile = new TextFile(
    new File(CastParameter.getClusterResultFileName()));
    clusterResultFile.open();
    int counterProgress = 0;
    long maxProgress = Math.max(1, DiasdemCollection
    .getNumberOfUntaggedTextUnits());  // avoid division by zero
    
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
      
      if (!NewXmlFileName.equals(CurrentXmlFileName)) {
        newFile = true;
        CurrentXmlFileName = NewXmlFileName;
        DiasdemDocument = DiasdemCollection.getDocument(
        CurrentXmlFileName.trim());
        if (DiasdemDocument != null) {
          DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
          .getActiveTextUnitsLayerIndex());
          // read-only task, backup is not necessary
          if (CastParameter.dumpDocumentsForVisualization()) {
            DumpDirectoryFileName = this.getDumpDirectory();
          }
        }
      }
      else {
        newFile = false;
      }
      
      if (DiasdemDocument != null) {
        lineZerlegt = DiasdemDocument.getOriginalTextUnit(NewSentenceID)
        .getContentsAsString();
        lineGrundformen = DiasdemDocument.getProcessedTextUnit(NewSentenceID)
        .getContentsAsString();
      }
      
      MyTextUnitClusterSet.addTextUnit(NewClusterID, lineZerlegt,
      lineGrundformen, CurrentXmlFileName, NewSentenceID, DiasdemDocument,
      DumpDirectoryFileName, CastParameter.getClusterDirectory(),
      CastParameter.dumpDocumentsForVisualization(),
      CastParameter.getMaxNumberOfOutputTextUnits());
      
      line = clusterResultFile.getNextLine();
      
    }  // read all result file lines
    
    Progress.update(TaskProgress.INDETERMINATE,
    "Computing Quality of Clusters");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    clusterResultFile.close();
    MyTextUnitClusterSet.assessClusterQuality();
    super.closeDiasdemCollection();
    
    Result.update(TaskResult.FINAL_RESULT,
    "There are HTML files visualizing all clusters in the directory\n"
    + Tools.shortenFileName(CastParameter.getClusterDirectory(), 50) + "!");
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
      
      case MonitorClusterQualityParameter.CSV_FILE: {
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
      case MonitorClusterQualityParameter.TXT_FILE: {
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
  MonitorClusterQualityParameter pParameter) {
    
    if (pParameter != null && pParameter.getClusterResultFileFormat()
    == MonitorClusterQualityParameter.CSV_FILE) {
      return DIAsDEMguiPreferences.CSV_FILE_EXTENSION;
    }
    else if (pParameter != null && pParameter.getClusterResultFileFormat()
    == MonitorClusterQualityParameter.TXT_FILE) {
      return DIAsDEMguiPreferences.TEXT_FILE_EXTENSION;
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getDumpDirectory() {
    
    if (DumpDirectoryFile == null) {
      MaxFilesInDumpDirectory = DiasdemProject.getIntProperty(
      "MAX_FILES_PER_DIRECTORY");
      DumpDirectoryIndex = 0;
      FilesInDumpDirectory = MaxFilesInDumpDirectory;
    }
    if (FilesInDumpDirectory == MaxFilesInDumpDirectory) {
      DumpDirectoryIndex++;
      FilesInDumpDirectory = 0;
      DumpDirectoryFileName = Tools.ensureTrailingSlash(CastParameter
      .getClusterDirectory()) + "documents" + File.separator
      + "part" + DumpDirectoryIndex + File.separator;
      DumpDirectoryFile = new File(DumpDirectoryFileName);
      if (!DumpDirectoryFile.exists()) {
        DumpDirectoryFile.mkdirs();
      }
    }
    FilesInDumpDirectory++;
    
    return DumpDirectoryFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}