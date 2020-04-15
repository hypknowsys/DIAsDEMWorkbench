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

package org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys;

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
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ClusterTextUnitVectorsHypknowsysParameterPanel
extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ClusterTextUnitVectorsHypknowsysParameter CastParameter = null;
  
  private KGridBagPanel ClusterParameters_Panel = null;
  private KTabbedPane Tabbed_Pane = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField InputFile_Text = null;
  private KButtonPanel InputFile_Button = null;
  private KTextField OutputFile_Text = null;
  private KButtonPanel OutputFile_Button = null;
  private KTextField ModelFile_Text = null;
  private KButtonPanel ModelFile_Button = null;
  private KComboBox Algorithm_Combo = null;
  private boolean Algorithm_FirstAction = true;
  private KComboBox Mode_Combo = null;
  private boolean Mode_FirstAction = true;
  private KComboBox Distance_Combo = null;
  private boolean Distance_FirstAction = true;
  private KCheckBox ClusterValidityAssessment_CheckBox = null;
  private KCheckBox VerboseMode_CheckBox = null;
  private KTextField HtmlReportFile_Text = null;
  private KButtonPanel HtmlReportFile_Button = null;
  private KCheckBox LaunchHtmlBrowser_CheckBox = null;
  private KTextField NumberOfClusters_Text = null;
  private KTextField RandomNumberSeed_Text = null;
  private KTextField MaxIterations_Text = null;
  private KTextField MinClusterCardinality_Text = null;
  private KTextField MaxRetriesPerBisectingPass_Text = null;
  private KTextField NumberOfRows_Text = null;
  private KTextField NumberOfColumns_Text = null;
  private KTextField LatticeType_Text = null;
  private KTextField NeighborhoodRadii_Text = null;
  private KCheckBox DrawRandomSample_CheckBox = null;
  private KCheckBox SequentialAccess_CheckBox = null;
  private KTextField RandomSampleSize_Text = null;
  private KTextField SizeOfNearestNeighborsList_Text = null;
  private KTextField NumberOfSharedNearestNeighbors_Text = null;
  private KTextField StrongLinkThreshold_Text = null;
  private KTextField LabelingThreshold_Text = null;
  private KTextField MergeThreshold_Text = null;
  private KTextField NoiseThreshold_Text = null;
  private KTextField TopicThreshold_Text = null;
  
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
  
  public ClusterTextUnitVectorsHypknowsysParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsHypknowsysParameterPanel(Server pDiasdemServer,
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
    else if (ActionCommand.equals("InputFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      InputFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null,
      "Select Existing Text Unit Vectors File",
      DIAsDEMguiPreferences.ARFF_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("OutputFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      OutputFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null,
      "Select Clustering Results File to be Created",
      DIAsDEMguiPreferences.CSV_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("ModelFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ModelFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null,
      "Select New or Existing Text Unit Clusterer File",
      this.getRequiredFileFilter(), false, true);
      
    }
    else if (ActionCommand.equals("HtmlReportFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      HtmlReportFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select New HTML Report File",
      DIAsDEMguiPreferences.HTML_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("AlgorithmCombo")) {
      
      if (Algorithm_FirstAction) {
        Algorithm_FirstAction = false;
        return;
      }
      if (Mode_Combo != null && Mode_Combo.getSelectedString().equals(
      ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_MODES[
      ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE])) {
        this.enableParameterFields();
      }
      if (ModelFile_Text != null && ModelFile_Text.getText().length() > 0) {
        ModelFile_Text.setText(Tools.removeFileExtension(ModelFile_Text
        .getText()) + this.getRequiredFileExtension());
      }
      
    }
    else if (ActionCommand.equals("ModeCombo")) {
      
      if (Mode_FirstAction) {
        Mode_FirstAction = false;
        return;
      }
      if (Mode_Combo != null && Mode_Combo.getSelectedString().equals(
      ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_MODES[
      ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE])) {
        this.enableParameterFields();
      }
      else {
        this.disableParameterFields();
      }
      
    }
    else if (ActionCommand.equals("DrawRandomSample")) {
      
      if (DrawRandomSample_CheckBox != null
      && DrawRandomSample_CheckBox.isSelected()) {
        if (RandomSampleSize_Text != null) {
          RandomSampleSize_Text.setEnabled(true);
          if (CastParameter != null) {
            RandomSampleSize_Text.setText(CastParameter.getRandomSampleSize());
          }
          else {
            RandomSampleSize_Text.setText(DiasdemProject.getProperty(
            "DEFAULT_HYPKNOWSYS_RANDOM_SAMPLE_SIZE"));
          }
        }
      }
      else {
        if (RandomSampleSize_Text != null) {
          RandomSampleSize_Text.setEnabled(false);
          RandomSampleSize_Text.setText("");
        }
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
    
    return "Cluster Text Unit Vectors (hypKNOWsys)";
    
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
    
    ClusterTextUnitVectorsHypknowsysParameter parameter =
    new ClusterTextUnitVectorsHypknowsysParameter(
    Collection_Text.getText(),
    InputFile_Text.getText(),
    OutputFile_Text.getText(),
    ModelFile_Text.getText(),
    Algorithm_Combo.getSelectedString(),
    Mode_Combo.getSelectedString(),
    Distance_Combo.getSelectedString(),
    false, false,
    HtmlReportFile_Text.getText(),
    NumberOfClusters_Text.getText(), false,
    RandomNumberSeed_Text.getText(),
    MaxIterations_Text.getText(),
    MinClusterCardinality_Text.getText(),
    MaxRetriesPerBisectingPass_Text.getText(),
    NumberOfRows_Text.getText(),
    NumberOfColumns_Text.getText(),
    LatticeType_Text.getText(),
    NeighborhoodRadii_Text.getText(),
    false, false, RandomSampleSize_Text.getText(),
    SizeOfNearestNeighborsList_Text.getText(),
    NumberOfSharedNearestNeighbors_Text.getText(),
    StrongLinkThreshold_Text.getText(),
    LabelingThreshold_Text.getText(), MergeThreshold_Text.getText(),
    NoiseThreshold_Text.getText(), TopicThreshold_Text.getText());
    
    if (ClusterValidityAssessment_CheckBox.isSelected()) {
      parameter.setClusterValidityAssessment(true);
    }
    if (VerboseMode_CheckBox.isSelected()) {
      parameter.setVerboseMode(true);
    }
    if (LaunchHtmlBrowser_CheckBox.isSelected()) {
      parameter.setLaunchHtmlBrowser(true);
    }
    if (DrawRandomSample_CheckBox.isSelected()) {
      parameter.setDrawRandomSample(true);
    }
    if (SequentialAccess_CheckBox.isSelected()) {
      parameter.setSequentialAccess(true);
    }
    
    // this parameter cannot be set via the parameter panel
    parameter.setCompactifyTrainingInstances(DiasdemProject.getBooleanProperty(
    "DEFAULT_HYPKNOWSYS_COMPACTIFY_TRAINING_INSTANCES"));
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ClusterTextUnitVectorsHypknowsysParameter) {
      CastParameter = (ClusterTextUnitVectorsHypknowsysParameter)pTaskParameter;
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
    
    if (!Tools.stringIsNullOrEmpty(Collection_Text.getText())) {
      DiasdemProject.setProperty("DEFAULT_COLLECTION_FILE",
      Collection_Text.getText());
    }
    DiasdemProject.setProperty("DEFAULT_CLUSTER_RESULT_FILE",
    OutputFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_VECTORS_FILE",
    InputFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_CLUSTERER_FILE_NAME",
    ModelFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_CLUSTERING_ALGORITHM_INDEX",
    String.valueOf(Algorithm_Combo.getSelectedIndex()));
    DiasdemProject.setProperty("DEFAULT_CLUSTERING_MODE_INDEX",
    String.valueOf(Mode_Combo.getSelectedIndex()));
    if (Mode_Combo.getSelectedIndex()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_DISTANCE_MEASURE_INDEX",
      String.valueOf(Distance_Combo.getSelectedIndex()));
      DiasdemProject.setProperty(
      "DEFAULT_HYPKNOWSYS_CLUSTER_VALIDITY_ASSESSMENT",
      String.valueOf(ClusterValidityAssessment_CheckBox.isSelected()));
      DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_VERBOSE_MODE",
      String.valueOf(VerboseMode_CheckBox.isSelected()));
      DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_HTML_REPORT_FILE_NAME",
      HtmlReportFile_Text.getText());
      DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_LAUNCH_HTML_BROWSER",
      String.valueOf(LaunchHtmlBrowser_CheckBox.isSelected()));
      if (NumberOfClusters_Text.getText().length() > 0) {
        DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_NUMBER_OF_CLUSTERS",
        NumberOfClusters_Text.getText());
      }
      if (RandomNumberSeed_Text.getText().length() > 0) {
        DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_RANDOM_NUMBER_SEED",
        RandomNumberSeed_Text.getText());
      }
      if (MaxIterations_Text.getText().length() > 0) {
        DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_MAX_KMEANS_ITERATIONS",
        MaxIterations_Text.getText());
      }
      if (MinClusterCardinality_Text.getText().length() > 0) {
        DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_MIN_CLUSTER_CARDINALITY",
        MinClusterCardinality_Text.getText());
      }
      if (MaxRetriesPerBisectingPass_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_MAX_RETRIES_PER_BISECTING_PASS",
        MaxRetriesPerBisectingPass_Text.getText());
      }
      if (NumberOfRows_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_NUMBER_OF_ROWS",
        NumberOfRows_Text.getText());
      }
      if (NumberOfColumns_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_NUMBER_OF_COLUMNS",
        NumberOfColumns_Text.getText());
      }
      if (LatticeType_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_LATTICE_TYPE",
        LatticeType_Text.getText());
      }
      if (NeighborhoodRadii_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_NEIGHBORHOOD_RADII",
        NeighborhoodRadii_Text.getText());
      }
      if (SizeOfNearestNeighborsList_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_SIZE_OF_NEAREST_NEIGHBORS_LIST",
        SizeOfNearestNeighborsList_Text.getText());
      }
      if (NumberOfSharedNearestNeighbors_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_NUMBER_OF_SHARED_NEAREST_NEIGHBORS",
        NumberOfSharedNearestNeighbors_Text.getText());
      }
      if (StrongLinkThreshold_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_STRONG_LINK_THRESHOLD",
        StrongLinkThreshold_Text.getText());
      }
      if (LabelingThreshold_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_LABELING_THRESHOLD",
        LabelingThreshold_Text.getText());
      }
      if (MergeThreshold_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_MERGE_THRESHOLD",
        MergeThreshold_Text.getText());
      }
      if (NoiseThreshold_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_NOISE_THRESHOLD",
        NoiseThreshold_Text.getText());
      }
      if (TopicThreshold_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_TOPIC_THRESHOLD",
        TopicThreshold_Text.getText());
      }
      DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_DRAW_RANDOM_SAMPLE",
      String.valueOf(DrawRandomSample_CheckBox.isSelected()));
      if (DrawRandomSample_CheckBox.isSelected()
      && RandomSampleSize_Text.getText().length() > 0) {
        DiasdemProject.setProperty(
        "DEFAULT_HYPKNOWSYS_RANDOM_SAMPLE_SIZE",
        RandomSampleSize_Text.getText());
      }
      DiasdemProject.setProperty("DEFAULT_HYPKNOWSYS_SEQUENTIAL_ACCESS",
      String.valueOf(SequentialAccess_CheckBox.isSelected()));
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
    
    Algorithm_Combo = new KComboBox(ClusterTextUnitVectorsHypknowsysParameter
    .CLUSTERING_ALGORITHMS.length, true, "AlgorithmCombo", this);
    for (int i = 0; i < ClusterTextUnitVectorsHypknowsysParameter
    .CLUSTERING_ALGORITHMS.length; i++) {
      Algorithm_Combo.addItem(ClusterTextUnitVectorsHypknowsysParameter
      .CLUSTERING_ALGORITHMS[i], false);
    }
    
    Mode_Combo = new KComboBox(ClusterTextUnitVectorsHypknowsysParameter
    .CLUSTERING_MODES.length, true, "ModeCombo", this);
    for (int i = 0; i < ClusterTextUnitVectorsHypknowsysParameter
    .CLUSTERING_MODES.length; i++) {
      Mode_Combo.addItem(ClusterTextUnitVectorsHypknowsysParameter
      .CLUSTERING_MODES[i], false);
    }
    
    Distance_Combo = new KComboBox(ClusterTextUnitVectorsHypknowsysParameter
    .DISTANCE_MEASURES.length, true, "DistanceCombo", this);
    for (int i = 0; i < ClusterTextUnitVectorsHypknowsysParameter
    .DISTANCE_MEASURES.length; i++) {
      Distance_Combo.addItem(ClusterTextUnitVectorsHypknowsysParameter
      .DISTANCE_MEASURES[i], false);
    }
    
    ClusterValidityAssessment_CheckBox = new KCheckBox(
    "Perform Assessment of Cluster Validity", false, true,
    "ClusterValidityAssessment", this, KeyEvent.VK_E, "If this box is "
    + "checked, an assessment of cluster validity is performed.");
    
    VerboseMode_CheckBox = new KCheckBox(
    "Execute Clustering Algorithm in Verbose Mode", false, true,
    "VerboseMode", this, KeyEvent.VK_V, "If this box is checked, the "
    + "clustering algorithm is executed in verbose mode.");
    
    LaunchHtmlBrowser_CheckBox = new KCheckBox(
    "Launch Web Browser with HTML Report File", false, true,
    "LaunchHtmlBrowser", this, KeyEvent.VK_L, "If this box is checked, a Web "
    + "browser will be launched.");
    
    DrawRandomSample_CheckBox = new KCheckBox(
    "Create Clustering Model: Draw Random Text Unit Vector Sample",
    false, true, "DrawRandomSample", this, KeyEvent.VK_T, "If this box is "
    + "checked, a random sample will be drawn to reduce memory requirements.");
    
    SequentialAccess_CheckBox = new KCheckBox(
    "Apply Clustering Model: Access Text Unit Vectors Sequentially",
    false, true, "SequentialAccess", this, KeyEvent.VK_Y, "If this box is "
    + "checked, text unit vectors will be sequentially accessed to reduce "
    + "memory requirements.");
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      InputFile_Text = new KTextField(CastParameter
      .getInputVectorsFileName(), 30);
      OutputFile_Text = new KTextField(CastParameter
      .getOutputVectorsFileName(), 30);
      ModelFile_Text = new KTextField(CastParameter
      .getClusterModelFileName(), 30);
      HtmlReportFile_Text = new KTextField(CastParameter
      .getHtmlReportFileName(), 10);
      NumberOfClusters_Text = new KTextField(CastParameter
      .getNumberOfClusters(), 10);
      RandomNumberSeed_Text = new KTextField(CastParameter
      .getRandomNumberSeed(), 10);
      MaxIterations_Text = new KTextField(CastParameter
      .getMaxIterations(), 10);
      MinClusterCardinality_Text = new KTextField(CastParameter
      .getMinClusterCardinality(), 10);
      MaxRetriesPerBisectingPass_Text = new KTextField(CastParameter
      .getMaxRetriesPerBisectingPass(), 10);
      NumberOfRows_Text = new KTextField(CastParameter
      .getNumberOfRows(), 10);
      NumberOfColumns_Text = new KTextField(CastParameter
      .getNumberOfColumns(), 10);
      LatticeType_Text = new KTextField(CastParameter
      .getLatticeType(), 10);
      NeighborhoodRadii_Text = new KTextField(CastParameter
      .getNeighborhoodRadii(), 10);
      RandomSampleSize_Text = new KTextField(CastParameter
      .getRandomSampleSize(), 10);
      SizeOfNearestNeighborsList_Text = new KTextField(CastParameter
      .getSizeOfNearestNeighborsList(), 10);
      NumberOfSharedNearestNeighbors_Text = new KTextField(CastParameter
      .getNumberOfSharedNearestNeighbors(), 10);
      StrongLinkThreshold_Text = new KTextField(CastParameter
      .getStrongLinkThreshold(), 10);
      LabelingThreshold_Text = new KTextField(CastParameter
      .getLabelingThreshold(), 10);
      MergeThreshold_Text = new KTextField(CastParameter
      .getMergeThreshold(), 10);
      NoiseThreshold_Text = new KTextField(CastParameter
      .getNoiseThreshold(), 10);
      TopicThreshold_Text = new KTextField(CastParameter
      .getTopicThreshold(), 10);
      
      if (CastParameter.getClusteringAlgorithm() >= 0
      && CastParameter.getClusteringAlgorithm()
      < ClusterTextUnitVectorsHypknowsysParameter
      .CLUSTERING_ALGORITHMS.length) {
        Algorithm_Combo.setSelectedIndex(CastParameter
        .getClusteringAlgorithm());
      }
      else {
        Algorithm_Combo.setSelectedIndex(
        ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_SIMPLE_KMEANS);
      }
      
      if (CastParameter.getClusteringMode() >= 0
      && CastParameter.getClusteringMode()
      < ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_MODES.length) {
        Mode_Combo.setSelectedIndex(CastParameter.getClusteringMode());
      }
      else {
        Mode_Combo.setSelectedIndex(ClusterTextUnitVectorsHypknowsysParameter
        .CLUSTERING_PHASE);
      }
      
      if (CastParameter.getDistanceMeasure() >= 0
      && CastParameter.getDistanceMeasure()
      < ClusterTextUnitVectorsHypknowsysParameter.DISTANCE_MEASURES.length) {
        Distance_Combo.setSelectedIndex(CastParameter.getDistanceMeasure());
      }
      else {
        Distance_Combo.setSelectedIndex(
        ClusterTextUnitVectorsHypknowsysParameter
        .HYPKNOWSYS_EUCLIDEAN_DISTANCE);
      }
      
      ClusterValidityAssessment_CheckBox.setSelected(CastParameter
      .getClusterValidityAssessment());
      VerboseMode_CheckBox.setSelected(CastParameter
      .getVerboseMode());
      LaunchHtmlBrowser_CheckBox.setSelected(CastParameter
      .launchHtmlBrowser());
      DrawRandomSample_CheckBox.setSelected(CastParameter
      .drawRandomSample());
      SequentialAccess_CheckBox.setSelected(CastParameter
      .sequentialAccess());
      
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      InputFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_TEXT_UNIT_VECTORS_FILE"), 30);
      OutputFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CLUSTER_RESULT_FILE"), 30);
      ModelFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_CLUSTERER_FILE_NAME"), 30);
      HtmlReportFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_HTML_REPORT_FILE_NAME"), 10);
      NumberOfClusters_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_NUMBER_OF_CLUSTERS"), 10);
      RandomNumberSeed_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_RANDOM_NUMBER_SEED"), 10);
      MaxIterations_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_MAX_KMEANS_ITERATIONS"), 10);
      MinClusterCardinality_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_MIN_CLUSTER_CARDINALITY"), 10);
      MaxRetriesPerBisectingPass_Text = new KTextField(DiasdemProject
      .getProperty("DEFAULT_HYPKNOWSYS_MAX_RETRIES_PER_BISECTING_PASS"), 10);
      NumberOfRows_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_NUMBER_OF_ROWS"), 10);
      NumberOfColumns_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_NUMBER_OF_COLUMNS"), 10);
      LatticeType_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_LATTICE_TYPE"), 10);
      NeighborhoodRadii_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_NEIGHBORHOOD_RADII"), 10);
      RandomSampleSize_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_HYPKNOWSYS_RANDOM_SAMPLE_SIZE"), 10);
      SizeOfNearestNeighborsList_Text = new KTextField(DiasdemProject
      .getProperty("DEFAULT_HYPKNOWSYS_SIZE_OF_NEAREST_NEIGHBORS_LIST"), 10);
      NumberOfSharedNearestNeighbors_Text = new KTextField(DiasdemProject
      .getProperty("DEFAULT_HYPKNOWSYS_NUMBER_OF_SHARED_NEAREST_NEIGHBORS"),
      10);
      StrongLinkThreshold_Text = new KTextField(DiasdemProject
      .getProperty("DEFAULT_HYPKNOWSYS_STRONG_LINK_THRESHOLD"), 10);
      LabelingThreshold_Text = new KTextField(DiasdemProject
      .getProperty("DEFAULT_HYPKNOWSYS_LABELING_THRESHOLD"), 10);
      MergeThreshold_Text = new KTextField(DiasdemProject
      .getProperty("DEFAULT_HYPKNOWSYS_MERGE_THRESHOLD"), 10);
      NoiseThreshold_Text = new KTextField(DiasdemProject
      .getProperty("DEFAULT_HYPKNOWSYS_NOISE_THRESHOLD"), 10);
      TopicThreshold_Text = new KTextField(DiasdemProject
      .getProperty("DEFAULT_HYPKNOWSYS_TOPIC_THRESHOLD"), 10);
      
      if (DiasdemProject.getIntProperty("DEFAULT_CLUSTERING_ALGORITHM_INDEX")
      >= 0 && DiasdemProject.getIntProperty(
      "DEFAULT_CLUSTERING_ALGORITHM_INDEX")
      < ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS
      .length) {
        Algorithm_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_CLUSTERING_ALGORITHM_INDEX"));
      }
      else {
        Algorithm_Combo.setSelectedIndex(
        ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_SIMPLE_KMEANS);
      }
      
      if (DiasdemProject.getIntProperty("DEFAULT_CLUSTERING_MODE_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_CLUSTERING_MODE_INDEX")
      < ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_MODES.length) {
        Mode_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_CLUSTERING_MODE_INDEX"));
      }
      else {
        Mode_Combo.setSelectedIndex(ClusterTextUnitVectorsHypknowsysParameter
        .CLUSTERING_PHASE);
      }
      
      if (DiasdemProject.getIntProperty(
      "DEFAULT_HYPKNOWSYS_DISTANCE_MEASURE_INDEX")
      >= 0 && DiasdemProject.getIntProperty(
      "DEFAULT_HYPKNOWSYS_DISTANCE_MEASURE_INDEX")
      < ClusterTextUnitVectorsHypknowsysParameter.DISTANCE_MEASURES.length) {
        Distance_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_HYPKNOWSYS_DISTANCE_MEASURE_INDEX"));
      }
      else {
        Distance_Combo.setSelectedIndex(
        ClusterTextUnitVectorsHypknowsysParameter
        .HYPKNOWSYS_EUCLIDEAN_DISTANCE);
      }
      
      if (DiasdemProject.getBooleanProperty(
      "DEFAULT_HYPKNOWSYS_CLUSTER_VALIDITY_ASSESSMENT")) {
        ClusterValidityAssessment_CheckBox.setSelected(true);
      }
      else {
        ClusterValidityAssessment_CheckBox.setSelected(false);
      }
      if (DiasdemProject.getBooleanProperty(
      "DEFAULT_HYPKNOWSYS_VERBOSE_MODE")) {
        VerboseMode_CheckBox.setSelected(true);
      }
      else {
        VerboseMode_CheckBox.setSelected(false);
      }
      if (DiasdemProject.getBooleanProperty(
      "DEFAULT_HYPKNOWSYS_LAUNCH_HTML_BROWSER")) {
        LaunchHtmlBrowser_CheckBox.setSelected(true);
      }
      else {
        LaunchHtmlBrowser_CheckBox.setSelected(false);
      }
      if (DiasdemProject.getBooleanProperty(
      "DEFAULT_HYPKNOWSYS_DRAW_RANDOM_SAMPLE")) {
        DrawRandomSample_CheckBox.setSelected(true);
      }
      else {
        DrawRandomSample_CheckBox.setSelected(false);
      }
      if (DiasdemProject.getBooleanProperty(
      "DEFAULT_HYPKNOWSYS_SEQUENTIAL_ACCESS")) {
        SequentialAccess_CheckBox.setSelected(true);
      }
      else {
        SequentialAccess_CheckBox.setSelected(false);
      }
      
    }
    Collection_Text.setCaretAtEnding();
    InputFile_Text.setCaretAtEnding();
    OutputFile_Text.setCaretAtEnding();
    ModelFile_Text.setCaretAtEnding();
    HtmlReportFile_Text.setCaretAtEnding();
    NumberOfClusters_Text.setCaretAtEnding();
    RandomNumberSeed_Text.setCaretAtEnding();
    MaxIterations_Text.setCaretAtEnding();
    MinClusterCardinality_Text.setCaretAtEnding();
    MaxRetriesPerBisectingPass_Text.setCaretAtEnding();
    NumberOfRows_Text.setCaretAtEnding();
    NumberOfColumns_Text.setCaretAtEnding();
    LatticeType_Text.setCaretAtEnding();
    NeighborhoodRadii_Text.setCaretAtEnding();
    RandomSampleSize_Text.setCaretAtEnding();
    SizeOfNearestNeighborsList_Text.setCaretAtEnding();
    NumberOfSharedNearestNeighbors_Text.setCaretAtEnding();
    StrongLinkThreshold_Text.setCaretAtEnding();
    LabelingThreshold_Text.setCaretAtEnding();
    MergeThreshold_Text.setCaretAtEnding();
    NoiseThreshold_Text.setCaretAtEnding();
    TopicThreshold_Text.setCaretAtEnding();
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");
    
    InputFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    InputFile_Button.addSingleButton("...",
    KeyEvent.VK_F, true, true, "InputFileButton", this,
    "Click this button to select the text unit vector file.");
    
    OutputFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    OutputFile_Button.addSingleButton("...",
    KeyEvent.VK_R, true, true, "OutputFileButton", this,
    "Click this button to select the clustering result file.");
    
    ModelFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ModelFile_Button.addSingleButton("...",
    KeyEvent.VK_U, true, true, "ModelFileButton", this,
    "Click this button to select the clustering model file.");
    
    HtmlReportFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    HtmlReportFile_Button.addSingleButton("...",
    KeyEvent.VK_H, true, true, "HtmlReportFileButton", this,
    "Click this button to select the HTML report file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Collection_Text);
    
    Parameter_Panel.addLabel("Clustering Mode:", 0, 0, KeyEvent.VK_M,
    Mode_Combo, true,
    "Task input: Select an appropriate clustering mode.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Mode_Combo, 2, 0,
    new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 1, 11);
    Parameter_Panel.addLabel("Collection File:", 0, 2, KeyEvent.VK_C,
    Collection_Button.getDefaultButton(), true,
    "Task input: This collection file contains references "
    + "to all DIAsDEM documents.");
    Parameter_Panel.addComponent(Collection_Text, 2, 2,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankColumn(5, 2, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 6, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Text Unit Vectors File:", 0, 4, KeyEvent.VK_F,
    InputFile_Button.getDefaultButton(), true,
    "Task input: This file contains text unit vectors in ARFF format.");
    Parameter_Panel.addComponent(InputFile_Text, 2, 4,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(InputFile_Button, 6, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Clustering Algorithm:", 0, 6, KeyEvent.VK_A,
    Algorithm_Combo, true,
    "Task input: Select an appropriate hypKNOWsys clustering algorithm.");
    Parameter_Panel.addComponent(Algorithm_Combo, 2, 6,
    new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Distance Measure:", 0, 8, KeyEvent.VK_D,
    Distance_Combo, true,
    "Task input: Select an appropriate hypKNOWsys distance measure.");
    Parameter_Panel.addComponent(Distance_Combo, 2, 8,
    new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Clustering Results File:", 0, 10, KeyEvent.VK_R,
    OutputFile_Button.getDefaultButton(), true,
    "Task output: This file will contain the results of this clustering run.");
    Parameter_Panel.addComponent(OutputFile_Text, 2, 10,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(OutputFile_Button, 6, 10);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("Text Unit Clusterer File:", 0, 12, KeyEvent.VK_U,
    ModelFile_Button.getDefaultButton(), true, "Task output: This file will "
    + "contain the clustering model for usage in application mode.");
    Parameter_Panel.addComponent(ModelFile_Text, 2, 12,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ModelFile_Button, 6, 12);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("HTML Report File:", 0, 14, KeyEvent.VK_H,
    HtmlReportFile_Button.getDefaultButton(), true, "Optional task output: "
    + "This file will contain an HTML report about the clustering.");
    Parameter_Panel.addComponent(HtmlReportFile_Text, 2, 14,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(HtmlReportFile_Button, 6, 14);
    Parameter_Panel.addBlankRow(0, 15, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 16);
    Parameter_Panel.addComponent(ClusterValidityAssessment_CheckBox, 2, 16,
    new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 17);
    Parameter_Panel.addComponent(VerboseMode_CheckBox, 2, 17,
    new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 18);
    Parameter_Panel.addComponent(LaunchHtmlBrowser_CheckBox, 2, 18,
    new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 19);
    Parameter_Panel.addComponent(DrawRandomSample_CheckBox, 2, 19,
    new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addLabel("", 0, 20);
    Parameter_Panel.addComponent(SequentialAccess_CheckBox, 2, 20,
    new Insets(0, 0, 0, 0), 5, 1);
    
    Parameter_Panel.setPreferredSize(Parameter_Panel.getMinimumSize());
    KScrollBorderPanel parameter_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    parameter_ScrollPanel.addNorth(Parameter_Panel);
    parameter_ScrollPanel.startFocusForwarding(Collection_Text);
    
    ClusterParameters_Panel = new KGridBagPanel(0, 0, 0, 0);
    ClusterParameters_Panel.startFocusForwarding(NumberOfClusters_Text);
    
    ClusterParameters_Panel.addLabel("1) Number of Clusters:", 0, 0,
    KeyEvent.VK_1, NumberOfClusters_Text, true,
    "Task input: Specify the number of clusters (e.g., 10) or specify a loop "
    + "over this parameter (e.g., 10/5/100).");
    ClusterParameters_Panel.addBlankColumn(1, 0, 11);
    ClusterParameters_Panel.addComponent(NumberOfClusters_Text, 2, 0,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 1, 11);
    
    ClusterParameters_Panel.addLabel("2) Random Number Seed:", 0, 2,
    KeyEvent.VK_2, RandomNumberSeed_Text, true,
    "Task input: Select an appropriate parameter value (e.g., 12345).");
    ClusterParameters_Panel.addComponent(RandomNumberSeed_Text, 2, 2,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 3, 11);
    
    ClusterParameters_Panel.addLabel("3) Max. Iterations:", 0, 4,
    KeyEvent.VK_3, MaxIterations_Text, true, "Task input: Select an "
    + "appropriate, algorithm-specific stopping criterion (e.g., 20).");
    ClusterParameters_Panel.addComponent(MaxIterations_Text, 2, 4,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 5, 11);
    
    ClusterParameters_Panel.addLabel("4) Min. Cluster Cardinality:", 0, 6,
    KeyEvent.VK_4, MinClusterCardinality_Text, true, "Task input: Select an "
    + "appropriate stopping criterion (e.g., 5).");
    ClusterParameters_Panel.addComponent(MinClusterCardinality_Text, 2, 6,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 7, 11);
    
    ClusterParameters_Panel.addLabel("5) Max. Retries per Bisecting Pass:",
    0, 8, KeyEvent.VK_5, MaxRetriesPerBisectingPass_Text, true, "Task input: "
    + "Select an appropriate stopping criterion (e.g., 20).");
    ClusterParameters_Panel.addComponent(MaxRetriesPerBisectingPass_Text, 2, 8,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 9, 11);
    
    ClusterParameters_Panel.addLabel("6) Number of SOM Rows:", 0, 10,
    KeyEvent.VK_6, NumberOfRows_Text, true,
    "Task input: Select the number of SOM rows (e.g., 15).");
    ClusterParameters_Panel.addComponent(NumberOfRows_Text, 2, 10,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 11, 11);
    
    ClusterParameters_Panel.addLabel("7) Number of SOM Columns:", 0, 12,
    KeyEvent.VK_7, NumberOfColumns_Text, true,
    "Task input: Select the number of SOM columns (e.g., 15).");
    ClusterParameters_Panel.addComponent(NumberOfColumns_Text, 2, 12,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 13, 11);
    
    ClusterParameters_Panel.addLabel("8) Lattice Type of SOM Array:", 0, 14,
    KeyEvent.VK_8, LatticeType_Text, true, "Task input: Select the lattice "
    + "type (i.e., 'rectangular' or 'hexagonal').");
    ClusterParameters_Panel.addComponent(LatticeType_Text, 2, 14,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 15, 11);
    
    ClusterParameters_Panel.addLabel("9) Neighborhood Radii:", 0, 16,
    KeyEvent.VK_9, NeighborhoodRadii_Text, true, "Task input: Specify an array"
    + " of integers (e.g., '[3,2,2,2,1,1,1,1,0]').");
    ClusterParameters_Panel.addComponent(NeighborhoodRadii_Text, 2, 16,
    new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 17, 11);
    
    ClusterParameters_Panel.addLabel("10) Size of Nearest Neighbors List:",
    0, 18, 0, SizeOfNearestNeighborsList_Text, true, "Task input: Specify the "
    + " size of nearest neigbors list (e.g., 50).");
    ClusterParameters_Panel.addComponent(SizeOfNearestNeighborsList_Text,
    2, 18, new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 19, 11);
    
    ClusterParameters_Panel.addLabel("11) Number of Shared Nearest Neighbors:",
    0, 20, 0, NumberOfSharedNearestNeighbors_Text, true, "Task input: Specify "
    + " the number of shared nearest neighbors (e.g., 20).");
    ClusterParameters_Panel.addComponent(NumberOfSharedNearestNeighbors_Text,
    2, 20, new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 21, 11);
    
    ClusterParameters_Panel.addLabel("12) Strong Link Threshold:",
    0, 22, 0, StrongLinkThreshold_Text, true, "Task input: Specify "
    + " the strong link threshold (e.g., 20).");
    ClusterParameters_Panel.addComponent(StrongLinkThreshold_Text,
    2, 22, new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 23, 11);
    
    ClusterParameters_Panel.addLabel("13) Labeling Threshold:",
    0, 24, 0, LabelingThreshold_Text, true, "Task input: Specify "
    + " the labeling threshold (e.g., 25).");
    ClusterParameters_Panel.addComponent(LabelingThreshold_Text,
    2, 24, new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 25, 11);
    
    ClusterParameters_Panel.addLabel("14) Merge Threshold:",
    0, 26, 0, MergeThreshold_Text, true, "Task input: Specify "
    + " the merge threshold (e.g., 40).");
    ClusterParameters_Panel.addComponent(MergeThreshold_Text,
    2, 26, new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 27, 11);
    
    ClusterParameters_Panel.addLabel("15) Noise Threshold:",
    0, 28, 0, NoiseThreshold_Text, true, "Task input: Specify "
    + " the noise threshold (e.g., 5).");
    ClusterParameters_Panel.addComponent(NoiseThreshold_Text,
    2, 28, new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 29, 11);
    
    ClusterParameters_Panel.addLabel("16) Topic Threshold:",
    0, 30, 0, TopicThreshold_Text, true, "Task input: Specify "
    + " the topic threshold (e.g., 40).");
    ClusterParameters_Panel.addComponent(TopicThreshold_Text,
    2, 30, new Insets(0, 0, 0, 0), 3, 1);
    ClusterParameters_Panel.addBlankRow(0, 31, 11);
    
    ClusterParameters_Panel.addLabel("Random Sample Size:", 0, 32,
    KeyEvent.VK_9, NeighborhoodRadii_Text, true, "Task input: Specify the"
    + " absolute (e.g., 1000) or relative (e.g., 0.25) random sample size.");
    ClusterParameters_Panel.addComponent(RandomSampleSize_Text, 2, 32,
    new Insets(0, 0, 0, 0), 3, 1);
    
    ClusterParameters_Panel.setPreferredSize(ClusterParameters_Panel
    .getMinimumSize());
    KScrollBorderPanel clusterParameters_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    clusterParameters_ScrollPanel.addNorth(ClusterParameters_Panel);
    clusterParameters_ScrollPanel.startFocusForwarding(NumberOfClusters_Text);
    
    KBorderPanel parameterNorth_Panel = new KBorderPanel(12, 12, 11, 11);
    parameterNorth_Panel.startFocusForwarding(parameter_ScrollPanel);
    parameterNorth_Panel.addCenter(parameter_ScrollPanel);
    
    KBorderPanel clusterParameterNorth_Panel = new KBorderPanel(12, 12, 11, 11);
    clusterParameterNorth_Panel.startFocusForwarding(
    clusterParameters_ScrollPanel);
    clusterParameterNorth_Panel.addCenter(clusterParameters_ScrollPanel);
    
    Tabbed_Pane = new KTabbedPane();
    Tabbed_Pane.addTab("Main Settings", parameterNorth_Panel, KeyEvent.VK_S,
    true, true);
    Tabbed_Pane.addTab("Clustering Parameters", clusterParameterNorth_Panel,
    KeyEvent.VK_P, true, false);
    Tabbed_Pane.startFocusForwardingToSelectedTab();
    
    this.removeAll();
    this.validate();
    this.addCenter(Tabbed_Pane);
    this.validate();
    this.enableParameterFields();
    if (Mode_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_MODES[
    ClusterTextUnitVectorsHypknowsysParameter.APPLICATION_PHASE])) {
      this.disableParameterFields();
    }
    this.setComponentStatus();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getRequiredFileExtension() {
    
    if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_SIMPLE_KMEANS])) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_SIMPLE_KMEANS_FILE_EXTENSION;
    }
    else if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BISECTING_KMEANS])) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_BISECTING_KMEANS_FILE_EXTENSION;
    }
    else if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BATCH_SOM])) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_BATCH_SOM_FILE_EXTENSION;
    }
    else if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_JARVIS_PATRICK_SNN])) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_JARVIS_PATRICK_SNN_FILE_EXTENSION;
    }
    else if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter
    .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN])) {
      return DIAsDEMguiPreferences
      .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN_FILE_EXTENSION;
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private KFileFilter getRequiredFileFilter() {
    
    if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_SIMPLE_KMEANS])) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_SIMPLE_KMEANS_FILE_FILTER;
    }
    else if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BISECTING_KMEANS])) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_BISECTING_KMEANS_FILE_FILTER;
    }
    else if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BATCH_SOM])) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_BATCH_SOM_FILE_FILTER;
    }
    else if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_JARVIS_PATRICK_SNN])) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_JARVIS_PATRICK_SNN_FILE_FILTER;
    }
    else if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString()
    .equals(ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter
    .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN])) {
      return DIAsDEMguiPreferences
      .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN_FILE_FILTER;
    }
    
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void enableParameterFields() {
    
    this.disableParameterFields();
    
    if (Distance_Combo != null) {
      Distance_Combo.setEnabled(true);
      if (CastParameter != null) {
        if (CastParameter.getDistanceMeasure() >= 0
        && CastParameter.getDistanceMeasure()
        < ClusterTextUnitVectorsHypknowsysParameter.DISTANCE_MEASURES.length) {
          Distance_Combo.setSelectedIndex(CastParameter.getDistanceMeasure());
        }
        else {
          Distance_Combo.setSelectedIndex(
          ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_EUCLIDEAN_DISTANCE);
        }
      }
      else {
        if (DiasdemProject.getIntProperty(
        "DEFAULT_HYPKNOWSYS_DISTANCE_MEASURE_INDEX")
        >= 0 && DiasdemProject.getIntProperty(
        "DEFAULT_HYPKNOWSYS_DISTANCE_MEASURE_INDEX")
        < ClusterTextUnitVectorsHypknowsysParameter.DISTANCE_MEASURES.length) {
          Distance_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
          "DEFAULT_HYPKNOWSYS_DISTANCE_MEASURE_INDEX"));
        }
        else {
          Distance_Combo.setSelectedIndex(
          ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_EUCLIDEAN_DISTANCE);
        }
      }
      if (Distance_Combo.getSelectedIndex()
      == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_NOT_APPLICABLE
      && Mode_Combo.getSelectedIndex()
      == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
        Distance_Combo.setSelectedIndex(
        ClusterTextUnitVectorsHypknowsysParameter
        .HYPKNOWSYS_EUCLIDEAN_DISTANCE);
      }
    }
    if (Tabbed_Pane != null && Mode_Combo.getSelectedIndex()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      Tabbed_Pane.setEnabledAt(1, true);
    }
    if (Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_SIMPLE_KMEANS])
    || Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BISECTING_KMEANS])) {
      if (NumberOfClusters_Text != null) {
        NumberOfClusters_Text.setEnabled(true);
        if (CastParameter != null) {
          NumberOfClusters_Text.setText(CastParameter.getNumberOfClusters());
        }
        else {
          NumberOfClusters_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_NUMBER_OF_CLUSTERS"));
        }
      }
      if (RandomNumberSeed_Text != null) {
        RandomNumberSeed_Text.setEnabled(true);
        if (CastParameter != null) {
          RandomNumberSeed_Text.setText(CastParameter.getRandomNumberSeed());
        }
        else {
          RandomNumberSeed_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_RANDOM_NUMBER_SEED"));
        }
      }
      if (MaxIterations_Text != null) {
        MaxIterations_Text.setEnabled(true);
        if (CastParameter != null) {
          MaxIterations_Text.setText(CastParameter
          .getMaxIterations());
        }
        else {
          MaxIterations_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_MAX_KMEANS_ITERATIONS"));
        }
      }
    }
    if (Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BISECTING_KMEANS])) {
      if (MinClusterCardinality_Text != null) {
        MinClusterCardinality_Text.setEnabled(true);
        if (CastParameter != null) {
          MinClusterCardinality_Text.setText(CastParameter
          .getMinClusterCardinality());
        }
        else {
          MinClusterCardinality_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_MIN_CLUSTER_CARDINALITY"));
        }
      }
      if (MaxRetriesPerBisectingPass_Text != null) {
        MaxRetriesPerBisectingPass_Text.setEnabled(true);
        if (CastParameter != null) {
          MaxRetriesPerBisectingPass_Text.setText(CastParameter
          .getMaxRetriesPerBisectingPass());
        }
        else {
          MaxRetriesPerBisectingPass_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_MAX_RETRIES_PER_BISECTING_PASS"));
        }
      }
    }
    
    if (Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BATCH_SOM])) {
      if (RandomNumberSeed_Text != null) {
        RandomNumberSeed_Text.setEnabled(true);
        if (CastParameter != null) {
          RandomNumberSeed_Text.setText(CastParameter.getRandomNumberSeed());
        }
        else {
          RandomNumberSeed_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_RANDOM_NUMBER_SEED"));
        }
      }
      if (MaxIterations_Text != null) {
        MaxIterations_Text.setEnabled(true);
        if (CastParameter != null) {
          MaxIterations_Text.setText(CastParameter
          .getMaxIterations());
        }
        else {
          MaxIterations_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_MAX_KMEANS_ITERATIONS"));
        }
      }
      if (NumberOfRows_Text != null) {
        NumberOfRows_Text.setEnabled(true);
        if (CastParameter != null) {
          NumberOfRows_Text.setText(CastParameter.getNumberOfRows());
        }
        else {
          NumberOfRows_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_NUMBER_OF_ROWS"));
        }
      }
      if (NumberOfColumns_Text != null) {
        NumberOfColumns_Text.setEnabled(true);
        if (CastParameter != null) {
          NumberOfColumns_Text.setText(CastParameter.getNumberOfColumns());
        }
        else {
          NumberOfColumns_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_NUMBER_OF_COLUMNS"));
        }
      }
      if (LatticeType_Text != null) {
        LatticeType_Text.setEnabled(true);
        if (CastParameter != null) {
          LatticeType_Text.setText(CastParameter.getLatticeType());
        }
        else {
          LatticeType_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_LATTICE_TYPE"));
        }
      }
      if (NeighborhoodRadii_Text != null) {
        NeighborhoodRadii_Text.setEnabled(true);
        if (CastParameter != null) {
          NeighborhoodRadii_Text.setText(CastParameter.getNeighborhoodRadii());
        }
        else {
          NeighborhoodRadii_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_NEIGHBORHOOD_RADII"));
        }
      }
    }
    
    if (Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_JARVIS_PATRICK_SNN])
    || Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter
    .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN])) {
      if (SizeOfNearestNeighborsList_Text != null) {
        SizeOfNearestNeighborsList_Text.setEnabled(true);
        if (CastParameter != null) {
          SizeOfNearestNeighborsList_Text.setText(CastParameter
          .getSizeOfNearestNeighborsList());
        }
        else {
          SizeOfNearestNeighborsList_Text.setText(DiasdemProject
          .getProperty("DEFAULT_HYPKNOWSYS_SIZE_OF_NEAREST_NEIGHBORS_LIST"));
        }
      }
    }
    
    if (Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_JARVIS_PATRICK_SNN])) {
      if (NumberOfSharedNearestNeighbors_Text != null) {
        NumberOfSharedNearestNeighbors_Text.setEnabled(true);
        if (CastParameter != null) {
          NumberOfSharedNearestNeighbors_Text.setText(CastParameter
          .getNumberOfSharedNearestNeighbors());
        }
        else {
          NumberOfSharedNearestNeighbors_Text.setText(
          DiasdemProject.getProperty(
          "DEFAULT_HYPKNOWSYS_NUMBER_OF_SHARED_NEAREST_NEIGHBORS"));
        }
      }
    }
    
    if (Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsHypknowsysParameter
    .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN])) {
      if (StrongLinkThreshold_Text != null) {
        StrongLinkThreshold_Text.setEnabled(true);
        if (CastParameter != null) {
          StrongLinkThreshold_Text.setText(CastParameter
          .getStrongLinkThreshold());
        }
        else {
          StrongLinkThreshold_Text.setText(DiasdemProject
          .getProperty("DEFAULT_HYPKNOWSYS_STRONG_LINK_THRESHOLD"));
        }
      }
      if (LabelingThreshold_Text != null) {
        LabelingThreshold_Text.setEnabled(true);
        if (CastParameter != null) {
          LabelingThreshold_Text.setText(CastParameter
          .getLabelingThreshold());
        }
        else {
          LabelingThreshold_Text.setText(DiasdemProject
          .getProperty("DEFAULT_HYPKNOWSYS_LABELING_THRESHOLD"));
        }
      }
      if (MergeThreshold_Text != null) {
        MergeThreshold_Text.setEnabled(true);
        if (CastParameter != null) {
          MergeThreshold_Text.setText(CastParameter
          .getMergeThreshold());
        }
        else {
          MergeThreshold_Text.setText(DiasdemProject
          .getProperty("DEFAULT_HYPKNOWSYS_MERGE_THRESHOLD"));
        }
      }
      if (NoiseThreshold_Text != null) {
        NoiseThreshold_Text.setEnabled(true);
        if (CastParameter != null) {
          NoiseThreshold_Text.setText(CastParameter
          .getNoiseThreshold());
        }
        else {
          NoiseThreshold_Text.setText(DiasdemProject
          .getProperty("DEFAULT_HYPKNOWSYS_NOISE_THRESHOLD"));
        }
      }
      if (TopicThreshold_Text != null) {
        TopicThreshold_Text.setEnabled(true);
        if (CastParameter != null) {
          TopicThreshold_Text.setText(CastParameter
          .getTopicThreshold());
        }
        else {
          TopicThreshold_Text.setText(DiasdemProject
          .getProperty("DEFAULT_HYPKNOWSYS_TOPIC_THRESHOLD"));
        }
      }
    }
    
    if (ClusterValidityAssessment_CheckBox != null) {
      ClusterValidityAssessment_CheckBox.setEnabled(true);
      if (CastParameter != null) {
        ClusterValidityAssessment_CheckBox.setSelected(CastParameter
        .getClusterValidityAssessment());
      }
      else {
        ClusterValidityAssessment_CheckBox.setSelected(DiasdemProject
        .getBooleanProperty("DEFAULT_HYPKNOWSYS_CLUSTER_VALIDITY_ASSESSMENT"));
      }
    }
    if (VerboseMode_CheckBox != null) {
      VerboseMode_CheckBox.setEnabled(true);
      if (CastParameter != null) {
        VerboseMode_CheckBox.setSelected(CastParameter
        .getVerboseMode());
      }
      else {
        VerboseMode_CheckBox.setSelected(DiasdemProject
        .getBooleanProperty("DEFAULT_HYPKNOWSYS_VERBOSE_MODE"));
      }
    }
    if (LaunchHtmlBrowser_CheckBox != null) {
      LaunchHtmlBrowser_CheckBox.setEnabled(true);
      if (CastParameter != null) {
        LaunchHtmlBrowser_CheckBox.setSelected(CastParameter
        .launchHtmlBrowser());
      }
      else {
        LaunchHtmlBrowser_CheckBox.setSelected(DiasdemProject
        .getBooleanProperty("DEFAULT_HYPKNOWSYS_LAUNCH_HTML_BROWSER"));
      }
    }
    if (HtmlReportFile_Text != null) {
      HtmlReportFile_Text.setEnabled(true);
      if (CastParameter != null) {
        HtmlReportFile_Text.setText(CastParameter
        .getHtmlReportFileName());
      }
      else {
        HtmlReportFile_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_HYPKNOWSYS_HTML_REPORT_FILE_NAME"));
      }
    }
    if (HtmlReportFile_Button != null) {
      HtmlReportFile_Button.setAllEnabled(true);
    }
    
    if (Mode_Combo.getSelectedIndex()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      if (DrawRandomSample_CheckBox != null) {
        DrawRandomSample_CheckBox.setEnabled(true);
        if (CastParameter != null) {
          DrawRandomSample_CheckBox.setSelected(CastParameter
          .drawRandomSample());
        }
        else {
          DrawRandomSample_CheckBox.setSelected(DiasdemProject
          .getBooleanProperty("DEFAULT_HYPKNOWSYS_DRAW_RANDOM_SAMPLE"));
        }
      }
      if (RandomSampleSize_Text != null) {
        if (DrawRandomSample_CheckBox.isSelected()) {
          RandomSampleSize_Text.setEnabled(true);
          if (CastParameter != null) {
            RandomSampleSize_Text.setText(CastParameter.getRandomSampleSize());
          }
          else {
            RandomSampleSize_Text.setText(DiasdemProject.getProperty(
            "DEFAULT_HYPKNOWSYS_RANDOM_SAMPLE_SIZE"));
          }
        }
        else {
          RandomSampleSize_Text.setEnabled(false);
          RandomSampleSize_Text.setText("");
        }
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void disableParameterFields() {
    
    if (Tabbed_Pane != null) {
      Tabbed_Pane.setEnabledAt(1, false);
    }
    if (Distance_Combo != null) {
      Distance_Combo.setEnabled(false);
      Distance_Combo.setSelectedIndex(ClusterTextUnitVectorsHypknowsysParameter
      .HYPKNOWSYS_NOT_APPLICABLE);
    }
    if (NumberOfClusters_Text != null) {
      NumberOfClusters_Text.setEnabled(false);
      NumberOfClusters_Text.setText("");
    }
    if (RandomNumberSeed_Text != null) {
      RandomNumberSeed_Text.setEnabled(false);
      RandomNumberSeed_Text.setText("");
    }
    if (MaxIterations_Text != null) {
      MaxIterations_Text.setEnabled(false);
      MaxIterations_Text.setText("");
    }
    if (MinClusterCardinality_Text != null) {
      MinClusterCardinality_Text.setEnabled(false);
      MinClusterCardinality_Text.setText("");
    }
    if (MaxRetriesPerBisectingPass_Text != null) {
      MaxRetriesPerBisectingPass_Text.setEnabled(false);
      MaxRetriesPerBisectingPass_Text.setText("");
    }
    if (NumberOfRows_Text != null) {
      NumberOfRows_Text.setEnabled(false);
      NumberOfRows_Text.setText("");
    }
    if (NumberOfColumns_Text != null) {
      NumberOfColumns_Text.setEnabled(false);
      NumberOfColumns_Text.setText("");
    }
    if (LatticeType_Text != null) {
      LatticeType_Text.setEnabled(false);
      LatticeType_Text.setText("");
    }
    if (NeighborhoodRadii_Text != null) {
      NeighborhoodRadii_Text.setEnabled(false);
      NeighborhoodRadii_Text.setText("");
    }
    if (ClusterValidityAssessment_CheckBox != null) {
      ClusterValidityAssessment_CheckBox.setSelected(false);
      ClusterValidityAssessment_CheckBox.setEnabled(false);
    }
    if (VerboseMode_CheckBox != null) {
      VerboseMode_CheckBox.setSelected(false);
      VerboseMode_CheckBox.setEnabled(false);
    }
    if (LaunchHtmlBrowser_CheckBox != null) {
      LaunchHtmlBrowser_CheckBox.setSelected(false);
      LaunchHtmlBrowser_CheckBox.setEnabled(false);
    }
    if (HtmlReportFile_Text != null) {
      HtmlReportFile_Text.setText("");
      HtmlReportFile_Text.setEnabled(false);
    }
    if (HtmlReportFile_Button != null) {
      HtmlReportFile_Button.setAllEnabled(false);
    }
    if (DrawRandomSample_CheckBox != null) {
      DrawRandomSample_CheckBox.setSelected(false);
      DrawRandomSample_CheckBox.setEnabled(false);
    }
    if (RandomSampleSize_Text != null) {
      RandomSampleSize_Text.setEnabled(false);
      RandomSampleSize_Text.setText("");
    }
    if (SizeOfNearestNeighborsList_Text != null) {
      SizeOfNearestNeighborsList_Text.setEnabled(false);
      SizeOfNearestNeighborsList_Text.setText("");
    }
    if (NumberOfSharedNearestNeighbors_Text != null) {
      NumberOfSharedNearestNeighbors_Text.setEnabled(false);
      NumberOfSharedNearestNeighbors_Text.setText("");
    }
    if (StrongLinkThreshold_Text != null) {
      StrongLinkThreshold_Text.setEnabled(false);
      StrongLinkThreshold_Text.setText("");
    }
    if (LabelingThreshold_Text != null) {
      LabelingThreshold_Text.setEnabled(false);
      LabelingThreshold_Text.setText("");
    }
    if (MergeThreshold_Text != null) {
      MergeThreshold_Text.setEnabled(false);
      MergeThreshold_Text.setText("");
    }
    if (NoiseThreshold_Text != null) {
      NoiseThreshold_Text.setEnabled(false);
      NoiseThreshold_Text.setText("");
    }
    if (TopicThreshold_Text != null) {
      TopicThreshold_Text.setEnabled(false);
      TopicThreshold_Text.setText("");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}