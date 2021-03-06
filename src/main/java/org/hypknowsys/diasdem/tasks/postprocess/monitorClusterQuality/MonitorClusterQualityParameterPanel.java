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

package org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;
import org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class MonitorClusterQualityParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private MonitorClusterQualityParameter CastParameter = null;
  
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
  private KTextField MinClusterCardinality_Text = null;
  private KTextField MaxDistinctDescriptorsRatio_Text = null;
  private KTextField MinFrequentDescriptorsRatio_Text = null;
  private KTextField ClusterLabelFile_Text = null;
  private KButtonPanel ClusterLabelFile_Button = null;
  private KCheckBox IgnoreFirstLine_CheckBox = null;
  private KCheckBox IgnoreEmptyClusters_CheckBox = null;
  private KCheckBox LaunchHtmlBrowser_CheckBox = null;
  private KCheckBox LaunchClusterLabelEditor_CheckBox = null;
  private KCheckBox DumpDocumentsForVisualization_CheckBox = null;

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
  
  public MonitorClusterQualityParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
  
  public void actionPerformed(ActionEvent e) {
    
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();
    
    if (ActionCommand.equals("CollectionButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("ThesaurusNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      ThesaurusName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Thesaurus File",
      DIAsDEMguiPreferences.THESAURUS_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("ClusterFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ClusterResultFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Clustering Results File",
      this.getRequiredFileFilter(), false, true);
      
    } else if (ActionCommand.equals("ClusterLabelButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ClusterLabelFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Cluster Label File to be Created",
      DIAsDEMguiPreferences.CLUSTER_LABEL_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("ClusterDirButton")) {
      
      CurrentProjectDirectory = this.directoryButtonClicked(ClusterDir_Text,
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing and Empty Cluster Directory");
      
    } else if (ActionCommand.equals("VectorDimensionsCombo")) {
      
      if ( VectorDimensions_Combo.getSelectedString().equals(
      VectorizeTextUnitsParameter.VECTOR_DIMENSIONS_OPTIONS[
      VectorizeTextUnitsParameter.ALL_DESCRIPTORS] ) ) {
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
      
    } else if (ActionCommand.equals("ResultFileFormat_Combo")) {
      
      if (ClusterResultFile_Text != null
      && ClusterResultFile_Text.getText().length() > 0) {
        ClusterResultFile_Text.setText( Tools.removeFileExtension(
        ClusterResultFile_Text.getText() ) + this.getRequiredFileExtension() );
      }
      
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
    
    return "Monitor Cluster Quality";
    
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
    int minClusterCardinality = Tools.string2Int(
    MinClusterCardinality_Text.getText().trim());
    double maxDistinctDescriptorsRatio = Tools.string2Double(
    MaxDistinctDescriptorsRatio_Text.getText().trim());
    double minFrequentDescriptorsRatio = Tools.string2Double(
    MinFrequentDescriptorsRatio_Text.getText().trim());
    
    MonitorClusterQualityParameter parameter =
    new MonitorClusterQualityParameter(
    Collection_Text.getText(), 
    ThesaurusName_Text.getText(),
    ClusterResultFile_Text.getText(),
    ResultFileFormat_Combo.getSelectedString(),
    ClusterDir_Text.getText(),
    clusterNumber, 
    Tools.string2Int(Iteration_Text.getText()),
    minClusterCardinality, maxDistinctDescriptorsRatio,
    minFrequentDescriptorsRatio,
    VectorDimensions_Combo.getSelectedString(),
    ScopeNotesContain_Text.getText(),
    ClusterLabelFile_Text.getText(), false, false, false, false, false );
    
    if (IgnoreFirstLine_CheckBox.isSelected())
      parameter.setIgnoreFirstResultFileLine(true);
    if (IgnoreEmptyClusters_CheckBox.isSelected())
      parameter.setIgnoreEmptyClusters(true);
    if (LaunchHtmlBrowser_CheckBox.isSelected())
      parameter.setLaunchHtmlBrowser(true);
    if (LaunchClusterLabelEditor_CheckBox.isSelected())
      parameter.setLaunchClusterLabelEditor(true);
    if (DumpDocumentsForVisualization_CheckBox.isSelected())
      parameter.setDumpDocumentsForVisualization(true);
    
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
    if (MinClusterCardinality_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_MIN_CARDINALITY",
      MinClusterCardinality_Text.getText());
    }
    if (MaxDistinctDescriptorsRatio_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_MAX_DISTINCT_RATIO",
      MaxDistinctDescriptorsRatio_Text.getText());
    }
    if (MinFrequentDescriptorsRatio_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_MIN_FREQUENT_RATIO",
      MinFrequentDescriptorsRatio_Text.getText());
    }
    DiasdemProject.setProperty("DEFAULT_ITERATION",
    Iteration_Text.getText());
    DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX",
    String.valueOf(VectorDimensions_Combo.getSelectedIndex()));
    DiasdemProject.setProperty("DEFAULT_RESULT_FILE_FORMAT_INDEX",
    String.valueOf(ResultFileFormat_Combo.getSelectedIndex()));
    if (IgnoreFirstLine_CheckBox.isSelected()) {    
      DiasdemProject.setBooleanProperty(
      "DEFAULT_IGNORE_FIRST_LINE_IN_CLUSTER_RESULT_FILE", true);
    }
    else {
      DiasdemProject.setBooleanProperty(
      "DEFAULT_IGNORE_FIRST_LINE_IN_CLUSTER_RESULT_FILE", false);
    }
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
    "Check this box, if the first line of cluster result file contains " +
    "attribute names.");

    IgnoreEmptyClusters_CheckBox = new KCheckBox(
      "Ignore Empty Clusters in Cluster Index HTML File", false, true, 
      "IgnoreEmptyClusters", this, KeyEvent.VK_H, 
    "If this box is checked, empty cluster files will not be referenced.");

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
      MinClusterCardinality_Text = new KTextField(CastParameter
      .getMinClusterCardinality() + "", 30);
      MaxDistinctDescriptorsRatio_Text = new KTextField(CastParameter
      .getMaxDistinctDescriptorsRatio() + "", 30);
      MinFrequentDescriptorsRatio_Text = new KTextField(CastParameter
      .getMinFrequentDescriptorsRatio() + "", 30);
      
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
      == VectorizeTextUnitsParameter.SPECIFIED_DESCRIPTORS ||
      CastParameter.getVectorDimensions()
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
      MinClusterCardinality_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_MIN_CARDINALITY"), 30);
      MaxDistinctDescriptorsRatio_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_MAX_DISTINCT_RATIO"), 30);
      MinFrequentDescriptorsRatio_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_MIN_FREQUENT_RATIO"), 30);
      
      if (DiasdemProject.getIntProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX")
      < MonitorClusterQualityParameter.VECTOR_DIMENSIONS_OPTIONS.length) {
        VectorDimensions_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX"));
      }
      else {
        VectorDimensions_Combo.setSelectedIndex(MonitorClusterQualityParameter
        .SPECIFIED_DESCRIPTORS);
      }
      if (VectorDimensions_Combo.getSelectedIndex()
      == VectorizeTextUnitsParameter.SPECIFIED_DESCRIPTORS ||
      VectorDimensions_Combo.getSelectedIndex()
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

      if (DiasdemProject.getBooleanProperty(
      "DEFAULT_IGNORE_FIRST_LINE_IN_CLUSTER_RESULT_FILE")) {
        IgnoreFirstLine_CheckBox.setSelected(true);
      }
      else {
        IgnoreFirstLine_CheckBox.setSelected(false);
      }

    }
    Collection_Text.setCaretAtEnding();
    ThesaurusName_Text.setCaretAtEnding();
    ClusterResultFile_Text.setCaretAtEnding();  
    ClusterLabelFile_Text.setCaretAtEnding();  
    ClusterDir_Text.setCaretAtEnding();  
    MaxClusterID_Text.setCaretAtEnding();  
    MinClusterCardinality_Text.setCaretAtEnding();
    MaxDistinctDescriptorsRatio_Text.setCaretAtEnding();
    MinFrequentDescriptorsRatio_Text.setCaretAtEnding();
    
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
    "Task input: This collection file contains references " +
    "to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankColumn(5, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 6, 0);
    Parameter_Panel.addBlankRow(0, 1, 11);
    Parameter_Panel.addLabel("KDD Process Iteration:", 0, 2, KeyEvent.VK_I,
      Iteration_Text, true,
    "Task input: nter the current iteration of the DIAsDEM KDD process.");
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
      ClusterResultFile_Button.getDefaultButton(), true,
    "Task input: This file contains the results of the current clustering run.");
    Parameter_Panel.addComponent(ClusterResultFile_Text, 2, 6, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ClusterResultFile_Button, 6, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Cluster Directory:", 0, 8, KeyEvent.VK_D,
      ClusterDir_Button.getDefaultButton(), true,
    "Task input: HTML files describing each cluster " +
    "will be copied into this directory.");
    Parameter_Panel.addComponent(ClusterDir_Text, 2, 8, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ClusterDir_Button, 6, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Cluster Label File:", 0, 10, KeyEvent.VK_L,
      ClusterLabelFile_Button.getDefaultButton(), true,
    "Task output: This file will contain default labels for high quality " +
    "text unit vector clusters.");
    Parameter_Panel.addComponent(ClusterLabelFile_Text, 2, 10, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ClusterLabelFile_Button, 6, 10);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("Max. Cluster ID:", 0, 12, KeyEvent.VK_X,
      MaxClusterID_Text, true,
    "Task input: Enter the max. cluster ID which not necessarily " +
    "equals the number of clusters.");
    Parameter_Panel.addComponent(MaxClusterID_Text, 
      2, 12, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("Thesaurus File:", 0, 14, KeyEvent.VK_T,
      ThesaurusName_Button.getDefaultButton(), true,
    "Task input: This DIAsDEM-specific thesaurus file contains " +
    "text unit descriptors.");
    Parameter_Panel.addComponent(ThesaurusName_Text, 2, 14, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ThesaurusName_Button, 6, 14);
    Parameter_Panel.addBlankRow(0, 15, 11);
    Parameter_Panel.addLabel("Text Unit Descriptors:", 0, 16, KeyEvent.VK_U,
      VectorDimensions_Combo, true,
    "Task input: Only matching thesaurus descriptors will correspond to " +
    "vector dimensions.");
    Parameter_Panel.addComponent(VectorDimensions_Combo, 
      2, 16, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 17, 11);
    Parameter_Panel.addLabel("", 0, 18);
    Parameter_Panel.addComponent(ScopeNotesContain_Text, 2, 18,
      new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 19, 11);
    Parameter_Panel.addLabel("Cluster Quality Criteria:", 0, 20);
    Parameter_Panel.addLabel("1) Min. Cardinality =", 2, 20, KeyEvent.VK_1,
      MinClusterCardinality_Text, true,
    "Task input: Select an appropriate parameter value (non-negative integer).");
    Parameter_Panel.addBlankColumn(3, 8, 12);
    Parameter_Panel.addComponent(MinClusterCardinality_Text, 
      4, 20, new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 21, 11);
    Parameter_Panel.addLabel("2) Max. Distinct Ratio =", 2, 22, KeyEvent.VK_2,
      MaxDistinctDescriptorsRatio_Text, true,
    "Task input: Select an appropriate parameter value [0.0; 1.0].");
    Parameter_Panel.addComponent(MaxDistinctDescriptorsRatio_Text, 
      4, 22, new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 23, 11);
    Parameter_Panel.addLabel("3) Min. Frequent Ratio =", 2, 24, KeyEvent.VK_3,
      MinFrequentDescriptorsRatio_Text, true,
    "Task input: Select an appropriate parameter value [0.0; 1.0].");
    Parameter_Panel.addComponent(MinFrequentDescriptorsRatio_Text, 
      4, 24, new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 25, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 26);
    Parameter_Panel.addComponent(IgnoreFirstLine_CheckBox, 
      2, 26, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 27);
    Parameter_Panel.addComponent(IgnoreEmptyClusters_CheckBox, 
      2, 27, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 28);
    Parameter_Panel.addComponent(LaunchHtmlBrowser_CheckBox, 
      2, 28, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 29);
    Parameter_Panel.addComponent(LaunchClusterLabelEditor_CheckBox, 
      2, 29, new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 29);
    Parameter_Panel.addLabel("", 0, 30);
    Parameter_Panel.addComponent(DumpDocumentsForVisualization_CheckBox, 
      2, 31, new Insets(0, 0, 0, 0), 5, 1);

    Parameter_Panel.setPreferredSize(Parameter_Panel.getMinimumSize());    
    KScrollBorderPanel Parameter_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    Parameter_ScrollPanel.addCenter(Parameter_Panel);
    Parameter_ScrollPanel.startFocusForwarding(Collection_Text);
    

    this.removeAll();
    this.validate();
    this.addCenter(Parameter_ScrollPanel);
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
    MonitorClusterQualityParameter.CSV_FILE]))
      return DiasdemGuiPreferences.CSV_FILE_EXTENSION;
    if (ResultFileFormat_Combo != null 
    && ResultFileFormat_Combo.getSelectedString().equals(
    MonitorClusterQualityParameter.RESULT_FILE_FORMAT[
    MonitorClusterQualityParameter.TXT_FILE]))
      return DiasdemGuiPreferences.TEXT_FILE_EXTENSION;
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  private KFileFilter getRequiredFileFilter() {
   
    if (ResultFileFormat_Combo != null 
    && ResultFileFormat_Combo.getSelectedString().equals(
    MonitorClusterQualityParameter.RESULT_FILE_FORMAT[
    MonitorClusterQualityParameter.CSV_FILE]))
      return DiasdemGuiPreferences.CSV_FILE_FILTER;
    if (ResultFileFormat_Combo != null 
    && ResultFileFormat_Combo.getSelectedString().equals(
    MonitorClusterQualityParameter.RESULT_FILE_FORMAT[
    MonitorClusterQualityParameter.TXT_FILE]))
      return DiasdemGuiPreferences.TEXT_FILE_FILTER;
    
    return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}