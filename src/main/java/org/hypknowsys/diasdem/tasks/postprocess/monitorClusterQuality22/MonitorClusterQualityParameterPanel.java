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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits
.VectorizeTextUnitsParameter;
import org.hypknowsys.misc.swing.KBorderPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KCheckBox;
import org.hypknowsys.misc.swing.KComboBox;
import org.hypknowsys.misc.swing.KFileFilter;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KScrollBorderPanel;
import org.hypknowsys.misc.swing.KTabbedPane;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Karsten Winkler
 */

public class MonitorClusterQualityParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private MonitorClusterQualityParameter CastParameter = null;
  
  private KGridBagPanel QualityParameters_Panel = null;
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField ThesaurusName_Text = null;
  private KButtonPanel ThesaurusName_Button = null;
  private KTextField ClusterResultFile_Text = null;
  private KButtonPanel ClusterResultFile_Button = null;
  private KTextField ClusterDir_Text = null;
  private KButtonPanel ClusterDir_Button = null;
  private KTextField MaxClusterID_Text = null;
  private KComboBox VectorDimensions_Combo = null;
  private KTextField ScopeNotesContain_Text = null;
  private KTextField Iteration_Text = null;
  private KComboBox ResultFileFormat_Combo = null;
  private KTextField MinClusterSize_Text = null;
  private KTextField MaxDescriptorCoverage_Text = null;
  private KTextField MinDescriptorDominance_Text = null;
  private KTextField DominantDescriptorThreshold_Text = null;
  private KTextField RareDescriptorThreshold_Text = null;
  private KTextField FrequentNonDescriptorThreshold_Text = null;
  private KTextField MaxNumberOfOutputTextUnits_Text = null;
  private KTextField ClusterLabelFile_Text = null;
  private KButtonPanel ClusterLabelFile_Button = null;
  private KCheckBox IgnoreFirstLine_CheckBox = null;
  private KCheckBox IgnoreEmptyClusters_CheckBox = null;
  private KCheckBox RankClustersByQuality_CheckBox = null;
  private KCheckBox LaunchHtmlBrowser_CheckBox = null;
  private KCheckBox LaunchClusterLabelEditor_CheckBox = null;
  private KCheckBox DumpDocumentsForVisualization_CheckBox = null;
  private KCheckBox IgnoreOutlierCluster_CheckBox = null;
  private KTextField OutlierClusterID_Text = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityParameterPanel(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences) {
    
    super();
    
    this.setContext(pDiasdemServer, pDiasdemProject, pDiasdemGui,
    pDiasdemGuiPreferences);
    this.initialize();
    
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
  /* ########## interface ActionListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void actionPerformed(ActionEvent pActionEvent) {
    
    ActionCommand = pActionEvent.getActionCommand();
    ActionSource = pActionEvent.getSource();
    
    if (ActionCommand.equals("CollectionButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("ThesaurusNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      ThesaurusName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Thesaurus File",
      DIAsDEMguiPreferences.THESAURUS_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("ClusterFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ClusterResultFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Clustering Results File",
      this.getRequiredFileFilter(), false, true);
      
    }
    else if (ActionCommand.equals("ClusterLabelButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ClusterLabelFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Cluster Label File to be Created",
      DIAsDEMguiPreferences.CLUSTER_LABEL_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("ClusterDirButton")) {
      
      CurrentProjectDirectory = this.directoryButtonClicked(ClusterDir_Text,
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing and Empty Cluster Directory");
      
    }
    else if (ActionCommand.equals("VectorDimensionsCombo")) {
      
      if (VectorDimensions_Combo.getSelectedString().equals(
      VectorizeTextUnitsParameter.VECTOR_DIMENSIONS_OPTIONS[
      VectorizeTextUnitsParameter.ALL_DESCRIPTORS])) {
        if (ScopeNotesContain_Text != null) {
          ScopeNotesContain_Text.setText("");
          ScopeNotesContain_Text.setEnabled(false);
        }
      }
      else {
        if (ScopeNotesContain_Text != null) {
          ScopeNotesContain_Text.setEnabled(true);
          ScopeNotesContain_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_TEXT_UNIT_DESCRIPTORS_CONTAIN"));
          ScopeNotesContain_Text.setCaretAtEnding();
        }
      }
      
    }
    else if (ActionCommand.equals("ResultFileFormat_Combo")) {
      
      if (ClusterResultFile_Text != null
      && ClusterResultFile_Text.getText().length() > 0) {
        ClusterResultFile_Text.setText(Tools.removeFileExtension(
        ClusterResultFile_Text.getText()) + this.getRequiredFileExtension());
      }
      
    }
    else if (ActionCommand.equals("IgnoreOutlierCluster")) {
      
      if (!IgnoreOutlierCluster_CheckBox.isSelected()) {
        OutlierClusterID_Text.setText("");
        OutlierClusterID_Text.setEnabled(false);
      }
      else {
        if (CastParameter != null) {
          OutlierClusterID_Text.setText(Tools.int2String(CastParameter
          .getOutlierClusterID()));
        }
        else {
          OutlierClusterID_Text.setText(DiasdemProject.getProperty(
          "MONITOR_CLUSTER_QUALITY_22:_OUTLIER_CLUSTER_ID"));
        }
        OutlierClusterID_Text.setEnabled(true);
      }
      this.validate();
      
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskParameterPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
    
    super.initialize();
    
    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY));
    
    this.createParameterPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Monitor Cluster Quality 2.2";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogMSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    int clusterNumber = Tools.string2Int(
    MaxClusterID_Text.getText().trim());
    int minClusterSize = Tools.string2Int(
    MinClusterSize_Text.getText().trim());
    double maxDescriptorCoverage = Tools.string2Double(
    MaxDescriptorCoverage_Text.getText().trim());
    double minDescriptorDominance = Tools.string2Double(
    MinDescriptorDominance_Text.getText().trim());
    double dominantDescriptorThreshold = Tools.string2Double(
    DominantDescriptorThreshold_Text.getText().trim());
    double rareDescriptorThreshold = Tools.string2Double(
    RareDescriptorThreshold_Text.getText().trim());
    double frequentNonDescriptorThreshold = Tools.string2Double(
    FrequentNonDescriptorThreshold_Text.getText().trim());
    int maxNumberOfOutputTextUnits = Tools.string2Int(
    MaxNumberOfOutputTextUnits_Text.getText().trim());
    int outlierClusterID = Tools.string2Int(
    OutlierClusterID_Text.getText().trim());
    
    MonitorClusterQualityParameter parameter =
    new MonitorClusterQualityParameter(
    Collection_Text.getText(),
    ThesaurusName_Text.getText(),
    ClusterResultFile_Text.getText(),
    ResultFileFormat_Combo.getSelectedString(),
    ClusterDir_Text.getText(),
    clusterNumber,
    Tools.string2Int(Iteration_Text.getText()),
    minClusterSize, maxDescriptorCoverage,
    minDescriptorDominance,
    VectorDimensions_Combo.getSelectedString(),
    ScopeNotesContain_Text.getText(),
    ClusterLabelFile_Text.getText(), false, false, false, false, false, false,
    dominantDescriptorThreshold, rareDescriptorThreshold, 
    frequentNonDescriptorThreshold, maxNumberOfOutputTextUnits,
    false, outlierClusterID);
    
    if (IgnoreFirstLine_CheckBox.isSelected()) {
      parameter.setIgnoreFirstResultFileLine(true);
    }
    if (IgnoreEmptyClusters_CheckBox.isSelected()) {
      parameter.setIgnoreEmptyClusters(true);
    }
    if (RankClustersByQuality_CheckBox.isSelected()) {
      parameter.setRankClustersByQuality(true);
    }
    if (LaunchHtmlBrowser_CheckBox.isSelected()) {
      parameter.setLaunchHtmlBrowser(true);
    }
    if (LaunchClusterLabelEditor_CheckBox.isSelected()) {
      parameter.setLaunchClusterLabelEditor(true);
    }
    if (DumpDocumentsForVisualization_CheckBox.isSelected()) {
      parameter.setDumpDocumentsForVisualization(true);
    }
    if (IgnoreOutlierCluster_CheckBox.isSelected()) {
      parameter.setIgnoreTextUnitsInOutlierCluster(true);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof MonitorClusterQualityParameter) {
      CastParameter = (MonitorClusterQualityParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    IsEnabled = true;
    this.createParameterPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {
    
    DiasdemProject.setProperty("DEFAULT_COLLECTION_FILE",
    Collection_Text.getText());
    DiasdemProject.setProperty("DEFAULT_CLUSTER_RESULT_FILE",
    ClusterResultFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_CLUSTER_DIRECTORY",
    ClusterDir_Text.getText());
    DiasdemProject.setProperty("DEFAULT_CLUSTER_LABEL_FILE",
    ClusterLabelFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_THESAURUS_FILE",
    ThesaurusName_Text.getText());
    if (MaxClusterID_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_MAX_CLUSTER_ID",
      MaxClusterID_Text.getText());
    }
    if (ScopeNotesContain_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_CONTAIN",
      ScopeNotesContain_Text.getText());
    }
    if (MinClusterSize_Text.getText().length() > 0) {
      DiasdemProject.setProperty(
      "MONITOR_CLUSTER_QUALITY_22:_MIN_CLUSTER_SIZE",
      MinClusterSize_Text.getText());
    }
    if (MaxDescriptorCoverage_Text.getText().length() > 0) {
      DiasdemProject.setProperty(
      "MONITOR_CLUSTER_QUALITY_22:_MAX_DESCRIPTOR_COVERAGE",
      MaxDescriptorCoverage_Text.getText());
    }
    if (MinDescriptorDominance_Text.getText().length() > 0) {
      DiasdemProject.setProperty(
      "MONITOR_CLUSTER_QUALITY_22:_MIN_DESCRIPTOR_DOMINANCE",
      MinDescriptorDominance_Text.getText());
    }
    if (DominantDescriptorThreshold_Text.getText().length() > 0) {
      DiasdemProject.setProperty(
      "MONITOR_CLUSTER_QUALITY_22:_DOMINANT_DESCRIPTOR_THRESHOLD",
      DominantDescriptorThreshold_Text.getText());
    }
    if (RareDescriptorThreshold_Text.getText().length() > 0) {
      DiasdemProject.setProperty(
      "MONITOR_CLUSTER_QUALITY_22:_RARE_DESCRIPTOR_THRESHOLD",
      RareDescriptorThreshold_Text.getText());
    }
    if (FrequentNonDescriptorThreshold_Text.getText().length() > 0) {
      DiasdemProject.setProperty(
      "MONITOR_CLUSTER_QUALITY_22:_FREQUENT_NONDESCRIPTOR_THRESHOLD",
      FrequentNonDescriptorThreshold_Text.getText());
    }
    if (MaxNumberOfOutputTextUnits_Text.getText().length() > 0) {
      DiasdemProject.setProperty(
      "MONITOR_CLUSTER_QUALITY_22:_MAX_NUMBER_OF_OUTPUT_TEXT_UNITS",
      MaxNumberOfOutputTextUnits_Text.getText());
    }
    if (OutlierClusterID_Text.getText().length() > 0) {
      DiasdemProject.setProperty(
      "MONITOR_CLUSTER_QUALITY_22:_OUTLIER_CLUSTER_ID",
      OutlierClusterID_Text.getText());
    }
    DiasdemProject.setProperty("DEFAULT_ITERATION",
    Iteration_Text.getText());
    DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX",
    String.valueOf(VectorDimensions_Combo.getSelectedIndex()));
    DiasdemProject.setProperty("DEFAULT_RESULT_FILE_FORMAT_INDEX",
    String.valueOf(ResultFileFormat_Combo.getSelectedIndex()));
    DiasdemProject.setProperty("MONITOR_CLUSTER_QUALITY_22:_IGNORE_FIRST_LINE",
    String.valueOf(IgnoreFirstLine_CheckBox.isSelected()));
    DiasdemProject.setProperty(
    "MONITOR_CLUSTER_QUALITY_22:_IGNORE_EMPTY_CLUSTERS",
    String.valueOf(IgnoreEmptyClusters_CheckBox.isSelected()));
    DiasdemProject.setProperty(
    "MONITOR_CLUSTER_QUALITY_22:_RANK_CLUSTERS_BY_QUALITY",
    String.valueOf(RankClustersByQuality_CheckBox.isSelected()));
    DiasdemProject.setProperty("MONITOR_CLUSTER_QUALITY_22:_LAUNCH_WEB_BROWSER",
    String.valueOf(LaunchHtmlBrowser_CheckBox.isSelected()));
    DiasdemProject.setProperty(
    "MONITOR_CLUSTER_QUALITY_22:_LAUNCH_CLUSTER_LABEL_EDITOR",
    String.valueOf(LaunchClusterLabelEditor_CheckBox.isSelected()));
    DiasdemProject.setProperty(
    "MONITOR_CLUSTER_QUALITY_22:_DUMP_DIASDEM_DOCUMENTS",
    String.valueOf(DumpDocumentsForVisualization_CheckBox.isSelected()));
    DiasdemProject.setProperty(
    "MONITOR_CLUSTER_QUALITY_22:_IGNORE_TEXT_UNITS_IN_OUTLIER_CLUSTER",
    String.valueOf(IgnoreOutlierCluster_CheckBox.isSelected()));
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Collection_Text != null) {
      return Collection_Text;
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setEnabled(boolean pEnabled) {
    
    IsEnabled = pEnabled;
    this.setComponentStatus();
    super.setEnabled(IsEnabled);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createParameterPanel() {
    
    VectorDimensions_Combo = new KComboBox(MonitorClusterQualityParameter
    .VECTOR_DIMENSIONS_OPTIONS.length, true, "VectorDimensionsCombo", this);
    for (int i = 0; i < MonitorClusterQualityParameter.VECTOR_DIMENSIONS_OPTIONS
    .length; i++) {
      VectorDimensions_Combo.addItem(MonitorClusterQualityParameter
      .VECTOR_DIMENSIONS_OPTIONS[i], false);
    }
    
    ResultFileFormat_Combo = new KComboBox(MonitorClusterQualityParameter
    .RESULT_FILE_FORMAT.length, true, "ResultFileFormat_Combo", this);
    for (int i = 0; i < MonitorClusterQualityParameter.RESULT_FILE_FORMAT
    .length; i++) {
      ResultFileFormat_Combo.addItem(MonitorClusterQualityParameter
      .RESULT_FILE_FORMAT[i], false);
    }
    
    IgnoreFirstLine_CheckBox = new KCheckBox(
    "Ignore First Line of Cluster Result File", false, true,
    "IgnoreFirstLine", this, KeyEvent.VK_G,
    "Check this box, if the first line of cluster result file contains "
    + "attribute names.");
    
    IgnoreEmptyClusters_CheckBox = new KCheckBox(
    "Ignore Empty Clusters in Cluster Index HTML File", false, true,
    "IgnoreEmptyClusters", this, KeyEvent.VK_H,
    "If this box is checked, empty cluster files will not be referenced.");
    
    RankClustersByQuality_CheckBox = new KCheckBox(
    "Rank Clusters by Quality in Cluster Index HTML File", false, true,
    "RankClustersByQuality", this, KeyEvent.VK_H,
    "If this box is checked, clusters will be ranked by quality index.");
    
    LaunchHtmlBrowser_CheckBox = new KCheckBox(
    "Launch Web Browser with Cluster Index HTML File", false, true,
    "LaunchHtmlBrowser", this, KeyEvent.VK_W,
    "If this box is checked, a Web browser will be launched.");
    
    LaunchClusterLabelEditor_CheckBox = new KCheckBox(
    "Launch Cluster Label Editor with Cluster Label File", false, true,
    "LaunchClusterLabelEditor", this, KeyEvent.VK_N,
    "If this box is checked, a Cluster Label Editor will be launched.");
    
    DumpDocumentsForVisualization_CheckBox = new KCheckBox(
    "Dump DIAsDEM Documents for Visualization", false, true,
    "DumpDocumentsForVisualization", this, KeyEvent.VK_D,
    "If this box is checked, documents will be dumped for visualization.");
    
    IgnoreOutlierCluster_CheckBox = new KCheckBox(
    "Ignore Text Units in Outlier Cluster, Outlier Cluster ID:", false, false,
    "IgnoreOutlierCluster", this, KeyEvent.VK_U,
    "If this box is checked, the outlier cluster will be ignored.");
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      Iteration_Text = new KTextField(CastParameter
      .getIteration() + "", 30);
      ThesaurusName_Text = new KTextField(CastParameter
      .getThesaurusFileName(), 30);
      ClusterResultFile_Text = new KTextField(CastParameter
      .getClusterResultFileName(), 30);
      ClusterLabelFile_Text = new KTextField(CastParameter
      .getClusterLabelFileName(), 30);
      ClusterDir_Text = new KTextField(CastParameter
      .getClusterDirectory(), 30);
      MaxClusterID_Text = new KTextField(CastParameter
      .getNumberOfClusters() + "", 30);
      MinClusterSize_Text = new KTextField(CastParameter
      .getMinClusterSize() + "", 30);
      MaxDescriptorCoverage_Text = new KTextField(CastParameter
      .getMaxDescriptorCoverage() + "", 30);
      MinDescriptorDominance_Text = new KTextField(CastParameter
      .getMinDescriptorDominance() + "", 30);
      DominantDescriptorThreshold_Text = new KTextField(CastParameter
      .getDominantDescriptorThreshold() + "", 30);
      RareDescriptorThreshold_Text = new KTextField(CastParameter
      .getRareDescriptorThreshold() + "", 30);
      FrequentNonDescriptorThreshold_Text = new KTextField(CastParameter
      .getFrequentNonDescriptorThreshold() + "", 30);
      MaxNumberOfOutputTextUnits_Text = new KTextField(CastParameter
      .getMaxNumberOfOutputTextUnits() + "", 30);
      
      if (CastParameter.getVectorDimensions() >= 0
      && CastParameter.getVectorDimensions()
      < MonitorClusterQualityParameter.VECTOR_DIMENSIONS_OPTIONS.length) {
        VectorDimensions_Combo.setSelectedIndex(CastParameter
        .getVectorDimensions());
      }
      else {
        VectorDimensions_Combo.setSelectedIndex(MonitorClusterQualityParameter
        .SPECIFIED_DESCRIPTORS);
      }
      if (CastParameter.getVectorDimensions()
      == VectorizeTextUnitsParameter.SPECIFIED_DESCRIPTORS
      || CastParameter.getVectorDimensions()
      == VectorizeTextUnitsParameter.NOT_SPECIFIED_DESCRIPTORS) {
        ScopeNotesContain_Text = new KTextField(CastParameter.
        getDescriptorsScopeNotesContain(), 30);
        ScopeNotesContain_Text.setEnabled(true);
      }
      else {
        ScopeNotesContain_Text = new KTextField("");
        ScopeNotesContain_Text.setEnabled(false);
      }
      
      if (CastParameter.getClusterResultFileFormat() >= 0
      && CastParameter.getClusterResultFileFormat()
      < MonitorClusterQualityParameter.RESULT_FILE_FORMAT.length) {
        ResultFileFormat_Combo.setSelectedIndex(CastParameter
        .getClusterResultFileFormat());
      }
      else {
        ResultFileFormat_Combo.setSelectedIndex(MonitorClusterQualityParameter
        .CSV_FILE);
      }
      
      if (CastParameter.ignoreFirstResultFileLine()) {
        IgnoreFirstLine_CheckBox.setSelected(true);
      }
      else {
        IgnoreFirstLine_CheckBox.setSelected(false);
      }
      if (CastParameter.ignoreEmptyClusters()) {
        IgnoreEmptyClusters_CheckBox.setSelected(true);
      }
      else {
        IgnoreEmptyClusters_CheckBox.setSelected(false);
      }
      if (CastParameter.rankClustersByQuality()) {
        RankClustersByQuality_CheckBox.setSelected(true);
      }
      else {
        RankClustersByQuality_CheckBox.setSelected(false);
      }
      if (CastParameter.launchHtmlBrowser()) {
        LaunchHtmlBrowser_CheckBox.setSelected(true);
      }
      else {
        LaunchHtmlBrowser_CheckBox.setSelected(false);
      }
      if (CastParameter.launchClusterLabelEditor()) {
        LaunchClusterLabelEditor_CheckBox.setSelected(true);
      }
      else {
        LaunchClusterLabelEditor_CheckBox.setSelected(false);
      }
      if (CastParameter.dumpDocumentsForVisualization()) {
        DumpDocumentsForVisualization_CheckBox.setSelected(true);
      }
      else {
        DumpDocumentsForVisualization_CheckBox.setSelected(false);
      }
      if (CastParameter.ignoreTextUnitsInOutlierCluster()) {
        IgnoreOutlierCluster_CheckBox.setSelected(true);
        OutlierClusterID_Text = new KTextField(CastParameter
        .getOutlierClusterID() + "", 30);
        OutlierClusterID_Text.setEnabled(true);
      }
      else {
        IgnoreOutlierCluster_CheckBox.setSelected(false);
        OutlierClusterID_Text = new KTextField("", 30);
        OutlierClusterID_Text.setEnabled(false);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      Iteration_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_ITERATION"), 30);
      ThesaurusName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_THESAURUS_FILE"), 30);
      ClusterResultFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CLUSTER_RESULT_FILE"), 30);
      ClusterLabelFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CLUSTER_LABEL_FILE"), 30);
      ClusterDir_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CLUSTER_DIRECTORY"), 30);
      MaxClusterID_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_MAX_CLUSTER_ID"), 30);
      MinClusterSize_Text = new KTextField(DiasdemProject.getProperty(
      "MONITOR_CLUSTER_QUALITY_22:_MIN_CLUSTER_SIZE"), 30);
      MaxDescriptorCoverage_Text = new KTextField(DiasdemProject
      .getProperty("MONITOR_CLUSTER_QUALITY_22:_MAX_DESCRIPTOR_COVERAGE"), 30);
      MinDescriptorDominance_Text = new KTextField(DiasdemProject
      .getProperty("MONITOR_CLUSTER_QUALITY_22:_MIN_DESCRIPTOR_DOMINANCE"), 30);
      DominantDescriptorThreshold_Text = new KTextField(DiasdemProject
      .getProperty(
      "MONITOR_CLUSTER_QUALITY_22:_DOMINANT_DESCRIPTOR_THRESHOLD"), 30);
      RareDescriptorThreshold_Text = new KTextField(DiasdemProject
      .getProperty(
      "MONITOR_CLUSTER_QUALITY_22:_RARE_DESCRIPTOR_THRESHOLD"), 30);
      FrequentNonDescriptorThreshold_Text = new KTextField(DiasdemProject
      .getProperty(
      "MONITOR_CLUSTER_QUALITY_22:_FREQUENT_NONDESCRIPTOR_THRESHOLD"), 30);
      MaxNumberOfOutputTextUnits_Text = new KTextField(DiasdemProject
      .getProperty(
      "MONITOR_CLUSTER_QUALITY_22:_MAX_NUMBER_OF_OUTPUT_TEXT_UNITS"), 30);
      
      if (DiasdemProject.getIntProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX")
      >= 0 && DiasdemProject.getIntProperty(
      "DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX") < MonitorClusterQualityParameter
      .VECTOR_DIMENSIONS_OPTIONS.length) {
        VectorDimensions_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX"));
      }
      else {
        VectorDimensions_Combo.setSelectedIndex(MonitorClusterQualityParameter
        .SPECIFIED_DESCRIPTORS);
      }
      if (VectorDimensions_Combo.getSelectedIndex()
      == VectorizeTextUnitsParameter.SPECIFIED_DESCRIPTORS
      || VectorDimensions_Combo.getSelectedIndex()
      == VectorizeTextUnitsParameter.NOT_SPECIFIED_DESCRIPTORS) {
        ScopeNotesContain_Text = new KTextField(DiasdemProject.getProperty(
        "DEFAULT_TEXT_UNIT_DESCRIPTORS_CONTAIN"), 30);
        ScopeNotesContain_Text.setEnabled(true);
      }
      else {
        ScopeNotesContain_Text = new KTextField("");
        ScopeNotesContain_Text.setEnabled(false);
      }
      
      if (DiasdemProject.getIntProperty("DEFAULT_RESULT_FILE_FORMAT_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_RESULT_FILE_FORMAT_INDEX")
      < MonitorClusterQualityParameter.RESULT_FILE_FORMAT.length) {
        ResultFileFormat_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_RESULT_FILE_FORMAT_INDEX"));
      }
      else {
        ResultFileFormat_Combo.setSelectedIndex(MonitorClusterQualityParameter
        .CSV_FILE);
      }
      
      IgnoreFirstLine_CheckBox.setSelected(DiasdemProject
      .getBooleanProperty("MONITOR_CLUSTER_QUALITY_22:_IGNORE_FIRST_LINE"));
      IgnoreEmptyClusters_CheckBox.setSelected(DiasdemProject
      .getBooleanProperty("MONITOR_CLUSTER_QUALITY_22:_IGNORE_EMPTY_CLUSTERS"));
      RankClustersByQuality_CheckBox.setSelected(DiasdemProject
      .getBooleanProperty(
      "MONITOR_CLUSTER_QUALITY_22:_RANK_CLUSTERS_BY_QUALITY"));
      LaunchHtmlBrowser_CheckBox.setSelected(DiasdemProject
      .getBooleanProperty("MONITOR_CLUSTER_QUALITY_22:_LAUNCH_WEB_BROWSER"));
      LaunchClusterLabelEditor_CheckBox.setSelected(DiasdemProject
      .getBooleanProperty(
      "MONITOR_CLUSTER_QUALITY_22:_LAUNCH_CLUSTER_LABEL_EDITOR"));
      DumpDocumentsForVisualization_CheckBox.setSelected(DiasdemProject
      .getBooleanProperty(
      "MONITOR_CLUSTER_QUALITY_22:_DUMP_DIASDEM_DOCUMENTS"));
      IgnoreOutlierCluster_CheckBox.setSelected(DiasdemProject
      .getBooleanProperty(
      "MONITOR_CLUSTER_QUALITY_22:_IGNORE_TEXT_UNITS_IN_OUTLIER_CLUSTER"));
      
      if (IgnoreOutlierCluster_CheckBox.isSelected()) {
        OutlierClusterID_Text = new KTextField(DiasdemProject
        .getProperty("MONITOR_CLUSTER_QUALITY_22:_OUTLIER_CLUSTER_ID"), 30);
        OutlierClusterID_Text.setEnabled(true);
      }
      else {
        OutlierClusterID_Text = new KTextField("", 30);
        OutlierClusterID_Text.setEnabled(false);
      }
      
    }
    Collection_Text.setCaretAtEnding();
    ThesaurusName_Text.setCaretAtEnding();
    ClusterResultFile_Text.setCaretAtEnding();
    ClusterLabelFile_Text.setCaretAtEnding();
    ClusterDir_Text.setCaretAtEnding();
    MaxClusterID_Text.setCaretAtEnding();
    MinClusterSize_Text.setCaretAtEnding();
    MaxDescriptorCoverage_Text.setCaretAtEnding();
    MinDescriptorDominance_Text.setCaretAtEnding();
    DominantDescriptorThreshold_Text.setCaretAtEnding();
    RareDescriptorThreshold_Text.setCaretAtEnding();
    FrequentNonDescriptorThreshold_Text.setCaretAtEnding();
    MaxNumberOfOutputTextUnits_Text.setCaretAtEnding();
    OutlierClusterID_Text.setCaretAtEnding();
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");
    
    ThesaurusName_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ThesaurusName_Button.addSingleButton("...",
    KeyEvent.VK_T, true, true, "ThesaurusNameButton", this,
    "Click this button to select the thesaurus file.");
    
    ClusterDir_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ClusterDir_Button.addSingleButton("...",
    KeyEvent.VK_D, true, true, "ClusterDirButton", this,
    "Click this button to select the cluster directory.");
    
    ClusterResultFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ClusterResultFile_Button.addSingleButton("...",
    KeyEvent.VK_R, true, true, "ClusterFileButton", this,
    "Click this button to select the cluster result file.");
    
    ClusterLabelFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ClusterLabelFile_Button.addSingleButton("...",
    KeyEvent.VK_L, true, true, "ClusterLabelButton", this,
    "Click this button to select the cluster label file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Collection_Text);
    
    Parameter_Panel.addLabel("Collection File:", 0, 0, KeyEvent.VK_C,
    Collection_Button.getDefaultButton(), true,
    "Task input: This collection file contains references "
    + "to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankColumn(5, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 6, 0);
    Parameter_Panel.addBlankRow(0, 1, 11);
    Parameter_Panel.addLabel("KDT Process Iteration:", 0, 2, KeyEvent.VK_I,
    Iteration_Text, true,
    "Task input: Enter the current iteration of the DIAsDEM KDT process.");
    Parameter_Panel.addComponent(Iteration_Text,
    2, 2, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Result File Format:", 0, 4, KeyEvent.VK_F,
    ResultFileFormat_Combo, true,
    "Task input: Select the correct format of cluster result file.");
    Parameter_Panel.addComponent(ResultFileFormat_Combo,
    2, 4, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Cluster Result File:", 0, 6, KeyEvent.VK_R,
    ClusterResultFile_Button.getDefaultButton(), true, "Task input: This "
    + "file contains the results of the current clustering run.");
    Parameter_Panel.addComponent(ClusterResultFile_Text, 2, 6,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ClusterResultFile_Button, 6, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Cluster Directory:", 0, 8, KeyEvent.VK_D,
    ClusterDir_Button.getDefaultButton(), true,
    "Task input: HTML files describing each cluster "
    + "will be copied into this directory.");
    Parameter_Panel.addComponent(ClusterDir_Text, 2, 8,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ClusterDir_Button, 6, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Cluster Label File:", 0, 10, KeyEvent.VK_L,
    ClusterLabelFile_Button.getDefaultButton(), true,
    "Task output: This file will contain default labels for high quality "
    + "text unit vector clusters.");
    Parameter_Panel.addComponent(ClusterLabelFile_Text, 2, 10,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ClusterLabelFile_Button, 6, 10);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("Max. Cluster ID:", 0, 12, KeyEvent.VK_X,
    MaxClusterID_Text, true,
    "Task input: Enter the max. cluster ID which not necessarily "
    + "equals the number of clusters.");
    Parameter_Panel.addComponent(MaxClusterID_Text,
    2, 12, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("Thesaurus File:", 0, 14, KeyEvent.VK_T,
    ThesaurusName_Button.getDefaultButton(), true,
    "Task input: This DIAsDEM-specific thesaurus file contains "
    + "text unit descriptors.");
    Parameter_Panel.addComponent(ThesaurusName_Text, 2, 14,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ThesaurusName_Button, 6, 14);
    Parameter_Panel.addBlankRow(0, 15, 11);
    Parameter_Panel.addLabel("Text Unit Descriptors:", 0, 16, KeyEvent.VK_U,
    VectorDimensions_Combo, true,
    "Task input: Only matching thesaurus descriptors will correspond to "
    + "vector dimensions.");
    Parameter_Panel.addComponent(VectorDimensions_Combo,
    2, 16, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 17, 11);
    Parameter_Panel.addLabel("", 0, 18);
    Parameter_Panel.addComponent(ScopeNotesContain_Text, 2, 18,
    new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 19, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 32);
    Parameter_Panel.addComponent(IgnoreFirstLine_CheckBox,
    2, 32, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 33);
    Parameter_Panel.addComponent(IgnoreEmptyClusters_CheckBox,
    2, 33, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 34);
    Parameter_Panel.addComponent(RankClustersByQuality_CheckBox,
    2, 34, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 35);
    Parameter_Panel.addComponent(LaunchHtmlBrowser_CheckBox,
    2, 35, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 36);
    Parameter_Panel.addComponent(LaunchClusterLabelEditor_CheckBox,
    2, 36, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 37);
    Parameter_Panel.addComponent(DumpDocumentsForVisualization_CheckBox,
    2, 37, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 38);
    Parameter_Panel.addComponent(IgnoreOutlierCluster_CheckBox,
    2, 38, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addComponent(OutlierClusterID_Text,
    2, 39, new Insets(0, 0, 0, 0), 5, 1);
    
    Parameter_Panel.setPreferredSize(Parameter_Panel.getMinimumSize());
    KScrollBorderPanel parameter_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    parameter_ScrollPanel.addCenter(Parameter_Panel);
    parameter_ScrollPanel.startFocusForwarding(Collection_Text);
    
    QualityParameters_Panel = new KGridBagPanel(0, 0, 0, 0);
    QualityParameters_Panel.startFocusForwarding(
    DominantDescriptorThreshold_Text);
    
    QualityParameters_Panel.addLabel("1) Dominant Descriptor Threshold =",
    0, 0, KeyEvent.VK_1, DominantDescriptorThreshold_Text, true,
    "Task input: Select an appropriate parameter value [0.0; 1.0].");
    QualityParameters_Panel.addBlankColumn(1, 0, 12);
    QualityParameters_Panel.addComponent(DominantDescriptorThreshold_Text,
    2, 0, new Insets(0, 0, 0, 0), 3, 1);
    QualityParameters_Panel.addBlankRow(0, 1, 11);
    QualityParameters_Panel.addLabel("2) Rare Descriptor Threshold =",
    0, 2, KeyEvent.VK_2, RareDescriptorThreshold_Text, true,
    "Task input: Select an appropriate parameter value [0.0; 1.0].");
    QualityParameters_Panel.addComponent(RareDescriptorThreshold_Text,
    2, 2, new Insets(0, 0, 0, 0), 3, 1);
    QualityParameters_Panel.addBlankRow(0, 3, 11);
    QualityParameters_Panel.addBlankRow(0, 4, 11);
    QualityParameters_Panel.addLabel("3) Max. Descriptor Coverage =",
    0, 5, KeyEvent.VK_3, MaxDescriptorCoverage_Text, true,
    "Task input: Select an appropriate parameter value [0.0; 1.0].");
    QualityParameters_Panel.addComponent(MaxDescriptorCoverage_Text,
    2, 5, new Insets(0, 0, 0, 0), 3, 1);
    QualityParameters_Panel.addBlankRow(0, 6, 11);
    QualityParameters_Panel.addLabel("4) Min. Descriptor Dominance =",
    0, 7, KeyEvent.VK_4, MinDescriptorDominance_Text, true,
    "Task input: Select an appropriate parameter value [0.0; 1.0].");
    QualityParameters_Panel.addComponent(MinDescriptorDominance_Text,
    2, 7, new Insets(0, 0, 0, 0), 3, 1);
    QualityParameters_Panel.addBlankRow(0, 8, 11);
    QualityParameters_Panel.addLabel("5) Min. Cluster Size =", 
    0, 9, KeyEvent.VK_5, MinClusterSize_Text, true,
    "Task input: Select an appropriate parameter value (non-negative "
    + "integer).");
    QualityParameters_Panel.addComponent(MinClusterSize_Text,
    2, 9, new Insets(0, 0, 0, 0), 3, 1);
    QualityParameters_Panel.addBlankRow(0, 10, 11);
    QualityParameters_Panel.addBlankRow(0, 11, 11);
    QualityParameters_Panel.addLabel("6) Frequent Non-Descriptor Threshold =",
    0, 12, KeyEvent.VK_6, FrequentNonDescriptorThreshold_Text, true,
    "Task input: Select an appropriate parameter value [0.0; 1.0].");
    QualityParameters_Panel.addComponent(FrequentNonDescriptorThreshold_Text,
    2, 12, new Insets(0, 0, 0, 0), 3, 1);
    QualityParameters_Panel.addBlankRow(0, 13, 11);
    QualityParameters_Panel.addLabel("7) Max. Number of Output Text Units =",
    0, 14, KeyEvent.VK_7, MaxNumberOfOutputTextUnits_Text, true,
    "Task input: Select an appropriate parameter value >= 0.");
    QualityParameters_Panel.addComponent(MaxNumberOfOutputTextUnits_Text,
    2, 14, new Insets(0, 0, 0, 0), 3, 1);
    
    QualityParameters_Panel.setPreferredSize(QualityParameters_Panel
    .getMinimumSize());
    KScrollBorderPanel qualityParameters_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    qualityParameters_ScrollPanel.addNorth(QualityParameters_Panel);
    qualityParameters_ScrollPanel.startFocusForwarding(
    DominantDescriptorThreshold_Text);
    
    
    KBorderPanel parameterNorth_Panel = new KBorderPanel(12, 12, 11, 11);
    parameterNorth_Panel.startFocusForwarding(parameter_ScrollPanel);
    parameterNorth_Panel.addCenter(parameter_ScrollPanel);
    
    KBorderPanel qualityParameterNorth_Panel = new KBorderPanel(12, 12, 11, 11);
    qualityParameterNorth_Panel.startFocusForwarding(
    QualityParameters_Panel);
    qualityParameterNorth_Panel.addCenter(qualityParameters_ScrollPanel);
    
    KTabbedPane tabbed_Pane = new KTabbedPane();
    tabbed_Pane.addTab("Main Settings", parameterNorth_Panel, KeyEvent.VK_S,
    true, true);
    tabbed_Pane.addTab("Quality Parameters", qualityParameterNorth_Panel,
    KeyEvent.VK_Q, true, false);
    tabbed_Pane.startFocusForwardingToSelectedTab();
    
    this.removeAll();
    this.validate();
    this.addCenter(tabbed_Pane);
    this.validate();
    this.setComponentStatus();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getRequiredFileExtension() {
    
    if (ResultFileFormat_Combo != null
    && ResultFileFormat_Combo.getSelectedString().equals(
    MonitorClusterQualityParameter.RESULT_FILE_FORMAT[
    MonitorClusterQualityParameter.CSV_FILE])) {
      return DiasdemGuiPreferences.CSV_FILE_EXTENSION;
    }
    if (ResultFileFormat_Combo != null
    && ResultFileFormat_Combo.getSelectedString().equals(
    MonitorClusterQualityParameter.RESULT_FILE_FORMAT[
    MonitorClusterQualityParameter.TXT_FILE])) {
      return DiasdemGuiPreferences.TEXT_FILE_EXTENSION;
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private KFileFilter getRequiredFileFilter() {
    
    if (ResultFileFormat_Combo != null
    && ResultFileFormat_Combo.getSelectedString().equals(
    MonitorClusterQualityParameter.RESULT_FILE_FORMAT[
    MonitorClusterQualityParameter.CSV_FILE])) {
      return DiasdemGuiPreferences.CSV_FILE_FILTER;
    }
    if (ResultFileFormat_Combo != null
    && ResultFileFormat_Combo.getSelectedString().equals(
    MonitorClusterQualityParameter.RESULT_FILE_FORMAT[
    MonitorClusterQualityParameter.TXT_FILE])) {
      return DiasdemGuiPreferences.TEXT_FILE_FILTER;
    }
    
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}