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

package org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka;

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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ClusterTextUnitVectorsWekaParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ClusterTextUnitVectorsWekaParameter CastParameter = null;
  
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
  private KTextField NumberOfClusters_Text = null;
  private KTextField Acuity_Text = null;
  private KTextField Cutoff_Text = null;
  private KTextField MaxIterations_Text = null;
  private KTextField RandomNumberSeed_Text = null;
  private KTextField MinStdDev_Text = null;
  
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
  
  public ClusterTextUnitVectorsWekaParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsWekaParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
      
    } else if (ActionCommand.equals("InputFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      InputFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Text Unit Vectors File",
      DIAsDEMguiPreferences.ARFF_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("OutputFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      OutputFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Clustering Results File to be Created",
      DIAsDEMguiPreferences.CSV_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("ModelFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ModelFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select New or Existing Text Unit Clusterer File",
      this.getRequiredFileFilter(), false, true);
      
    } else if (ActionCommand.equals("AlgorithmCombo")) {
      
      if (Algorithm_FirstAction) {
        Algorithm_FirstAction = false;
        return;
      }
      if ( Mode_Combo != null && Mode_Combo.getSelectedString().equals(
      ClusterTextUnitVectorsWekaParameter.CLUSTERING_MODES[
      ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE] ) ) {
        this.enableParameterFields();
      }
      if (ModelFile_Text != null && ModelFile_Text.getText().length() > 0) {
        ModelFile_Text.setText( Tools.removeFileExtension(ModelFile_Text
        .getText() ) + this.getRequiredFileExtension() );
      }
      
    } else if (ActionCommand.equals("ModeCombo")) {
      
      if (Mode_FirstAction) {
        Mode_FirstAction = false;
        return;
      }
      if ( Mode_Combo != null && Mode_Combo.getSelectedString().equals(
      ClusterTextUnitVectorsWekaParameter.CLUSTERING_MODES[
      ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE] ) ) {
        this.enableParameterFields();
      }
      else {
        this.disableParameterFields();
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
    
    return "Cluster Text Unit Vectors (Weka)";
    
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
    
    ClusterTextUnitVectorsWekaParameter parameter = new ClusterTextUnitVectorsWekaParameter(
    Collection_Text.getText(), 
    InputFile_Text.getText(),
    OutputFile_Text.getText(), 
    ModelFile_Text.getText(),
    Algorithm_Combo.getSelectedString(), 
    Mode_Combo.getSelectedString(),
    NumberOfClusters_Text.getText(), 
    Acuity_Text.getText(),
    Cutoff_Text.getText(), 
    MaxIterations_Text.getText(),
    RandomNumberSeed_Text.getText(), 
    MinStdDev_Text.getText() );
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ClusterTextUnitVectorsWekaParameter) {
      CastParameter = (ClusterTextUnitVectorsWekaParameter)pTaskParameter;
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
    OutputFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_VECTORS_FILE",
    InputFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_WEKA_SIMPLE_KMEANS_FILE",
    ModelFile_Text.getText());
    if (NumberOfClusters_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_NUMBER_OF_CLUSTERS",
      NumberOfClusters_Text.getText());
    }
    if (Acuity_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_ACUITY",
      Acuity_Text.getText());
    }
    if (Cutoff_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_CUTOFF",
      Cutoff_Text.getText());
    }
    if (MaxIterations_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_MAX_ITERATIONS",
      MaxIterations_Text.getText());
    }
    if (RandomNumberSeed_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_RANDOM_NUMBER_SEED",
      RandomNumberSeed_Text.getText());
    }
    if (MinStdDev_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_MIN_STD_DEVIATION",
      MinStdDev_Text.getText());
    }
    DiasdemProject.setProperty("DEFAULT_CLUSTERING_ALGORITHM_INDEX",
    String.valueOf(Algorithm_Combo.getSelectedIndex()));
    DiasdemProject.setProperty("DEFAULT_CLUSTERING_MODE_INDEX",
    String.valueOf(Mode_Combo.getSelectedIndex()));
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
    
    Algorithm_Combo = new KComboBox(ClusterTextUnitVectorsWekaParameter
    .CLUSTERING_ALGORITHMS.length, true, "AlgorithmCombo", this);
    for (int i = 0; i < ClusterTextUnitVectorsWekaParameter
    .CLUSTERING_ALGORITHMS.length; i++) {
      Algorithm_Combo.addItem(ClusterTextUnitVectorsWekaParameter
      .CLUSTERING_ALGORITHMS[i], false);
    }
    
    Mode_Combo = new KComboBox(ClusterTextUnitVectorsWekaParameter
    .CLUSTERING_MODES.length, true, "ModeCombo", this);
    for (int i = 0; i < ClusterTextUnitVectorsWekaParameter.CLUSTERING_MODES
    .length; i++) {
      Mode_Combo.addItem(ClusterTextUnitVectorsWekaParameter
      .CLUSTERING_MODES[i], false);
    }

   if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      InputFile_Text = new KTextField(CastParameter
      .getInputVectorsFileName(), 30);
      OutputFile_Text = new KTextField(CastParameter
      .getOutputVectorsFileName(), 30);
      ModelFile_Text = new KTextField(CastParameter
      .getClusterModelFileName(), 30);
      NumberOfClusters_Text = new KTextField(CastParameter
      .getNumberOfClusters(), 10);
      Acuity_Text = new KTextField(CastParameter
      .getAcuity(), 10);
      Cutoff_Text = new KTextField(CastParameter
      .getCutoff(), 10);
      MaxIterations_Text = new KTextField(CastParameter
      .getMaxIterations(), 10);
      RandomNumberSeed_Text = new KTextField(CastParameter
      .getRandomNumberSeed(), 10);
      MinStdDev_Text = new KTextField(CastParameter
      .getMinStdDeviation(), 10);
      
      if (CastParameter.getClusteringAlgorithm() >= 0 
      && CastParameter.getClusteringAlgorithm() < 
      ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS.length) {
        Algorithm_Combo.setSelectedIndex(CastParameter
        .getClusteringAlgorithm());
      }
      else {
        Algorithm_Combo.setSelectedIndex(ClusterTextUnitVectorsWekaParameter
        .WEKA_SIMPLE_KMEANS);
      }
      
      if (CastParameter.getClusteringMode() >= 0 
      && CastParameter.getClusteringMode() < 
      ClusterTextUnitVectorsWekaParameter.CLUSTERING_MODES.length) {
        Mode_Combo.setSelectedIndex(CastParameter.getClusteringMode());
      }
      else {
        Mode_Combo.setSelectedIndex(ClusterTextUnitVectorsWekaParameter
        .CLUSTERING_PHASE);
      }

   }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      InputFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_TEXT_UNIT_VECTORS_FILE"), 30);
      OutputFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CLUSTER_RESULT_FILE"), 30);
      ModelFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_WEKA_SIMPLE_KMEANS_FILE"), 30);
      NumberOfClusters_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_NUMBER_OF_CLUSTERS"), 10);
      Acuity_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_ACUITY"), 10);
      Cutoff_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CUTOFF"), 10);
      MaxIterations_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_MAX_ITERATIONS"), 10);
      RandomNumberSeed_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_RANDOM_NUMBER_SEED"), 10);
      MinStdDev_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_MIN_STD_DEVIATION"), 10);
      
      if (DiasdemProject.getIntProperty("DEFAULT_CLUSTERING_ALGORITHM_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_CLUSTERING_ALGORITHM_INDEX")
      < ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS.length) {
        Algorithm_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_CLUSTERING_ALGORITHM_INDEX"));
      }
      else {
        Algorithm_Combo.setSelectedIndex(ClusterTextUnitVectorsWekaParameter
        .WEKA_SIMPLE_KMEANS);
      }
      
      if (DiasdemProject.getIntProperty("DEFAULT_CLUSTERING_MODE_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_CLUSTERING_MODE_INDEX")
      < ClusterTextUnitVectorsWekaParameter.CLUSTERING_MODES.length) {
        Mode_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_CLUSTERING_MODE_INDEX"));
      }
      else {
        Mode_Combo.setSelectedIndex(ClusterTextUnitVectorsWekaParameter
        .CLUSTERING_PHASE);
      }
     
    }
    Collection_Text.setCaretAtEnding();    
    InputFile_Text.setCaretAtEnding();  
    OutputFile_Text.setCaretAtEnding();
    ModelFile_Text.setCaretAtEnding();  
    NumberOfClusters_Text.setCaretAtEnding();  
    Acuity_Text.setCaretAtEnding();  
    Cutoff_Text.setCaretAtEnding();  
    MaxIterations_Text.setCaretAtEnding();  
    RandomNumberSeed_Text.setCaretAtEnding();  
    MinStdDev_Text.setCaretAtEnding();  
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
      KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");    
    
    InputFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    InputFile_Button.addSingleButton("...", 
      KeyEvent.VK_V, true, true, "InputFileButton", this,
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
    "Task input: This collection file contains references " +
    "to all DIAsDEM documents.");
    Parameter_Panel.addComponent(Collection_Text, 2, 2, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankColumn(5, 2, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 6, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Text Unit Vectors File:", 0, 4, KeyEvent.VK_V,
      InputFile_Button.getDefaultButton(), true,
    "Task input: This file contains text unit vectors in ARFF format.");
    Parameter_Panel.addComponent(InputFile_Text, 2, 4, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(InputFile_Button, 6, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Clustering Algorithm:", 0, 6, KeyEvent.VK_A,
      Algorithm_Combo, true,
    "Task input: Select an appropriate Weka clustering algorithm.");
    Parameter_Panel.addComponent(Algorithm_Combo, 2, 6, 
      new Insets(0, 0, 0, 0), 5, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Clustering Parameters:", 0, 8);
    Parameter_Panel.addLabel("1) Number of Clusters = ", 2, 8, KeyEvent.VK_1,
      NumberOfClusters_Text, true,
    "Task input: Select an appropriate parameter value.");
    Parameter_Panel.addBlankColumn(3, 8, 12);
    Parameter_Panel.addComponent(NumberOfClusters_Text, 4, 8, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 9, 11);    
    Parameter_Panel.addLabel("", 0, 10);
    Parameter_Panel.addLabel("2) Acuity = ", 2, 10, KeyEvent.VK_2,
      Acuity_Text, true,
    "Task input: Select an appropriate parameter value.");
    Parameter_Panel.addComponent( Acuity_Text, 4, 10, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 11, 11);    
    Parameter_Panel.addLabel("", 0, 12);
    Parameter_Panel.addLabel("3) Cutoff = ", 2, 12, KeyEvent.VK_3,
      Cutoff_Text, true,
    "Task input: Select an appropriate parameter value.");
    Parameter_Panel.addComponent(Cutoff_Text, 4, 12, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 13, 11);    
    Parameter_Panel.addLabel("", 0, 14);
    Parameter_Panel.addLabel("4) Max. Iterations = ", 2, 14, KeyEvent.VK_4,
      MaxIterations_Text, true,
    "Task input: Select an appropriate parameter value.");
    Parameter_Panel.addComponent(MaxIterations_Text, 4, 14, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 15, 11);    
    Parameter_Panel.addLabel("", 0, 16);
    Parameter_Panel.addLabel("5) Random Number Seed = ", 2, 16, KeyEvent.VK_5,
      RandomNumberSeed_Text, true,
    "Task input: Select an appropriate parameter value.");
    Parameter_Panel.addComponent(RandomNumberSeed_Text, 4, 16, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 17, 11);    
    Parameter_Panel.addLabel("", 0, 18);
    Parameter_Panel.addLabel("6) Min. Std. Deviation = ", 2, 18, KeyEvent.VK_6,
      MinStdDev_Text, true,
    "Task input: Select an appropriate parameter value.");
    Parameter_Panel.addComponent(MinStdDev_Text, 4, 18, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 19, 11);    
    Parameter_Panel.addLabel("Clustering Results File:", 0, 20, KeyEvent.VK_R,
      OutputFile_Button.getDefaultButton(), true,
    "Task output: This file will contain the results of this clustering run.");
    Parameter_Panel.addComponent(OutputFile_Text, 2, 20, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(OutputFile_Button, 6, 20);
    Parameter_Panel.addBlankRow(0, 21, 11);    
    Parameter_Panel.addLabel("Text Unit Clusterer File:", 0, 22, KeyEvent.VK_U,
      ModelFile_Button.getDefaultButton(), true,
    "Task output: This file will contain the clustering model for usage " +
    "in application mode.");
    Parameter_Panel.addComponent(ModelFile_Text, 2, 22, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addKButtonPanel(ModelFile_Button, 6, 22);
    Parameter_Panel.addComponent(new JLabel(""), 0, 23, 
      new Insets(0, 0, 0, 0), 5, 1, 1.0);

    Parameter_Panel.setPreferredSize(Parameter_Panel.getMinimumSize());    
    KScrollBorderPanel Parameter_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    Parameter_ScrollPanel.addCenter(Parameter_Panel);
    Parameter_ScrollPanel.startFocusForwarding(Collection_Text);
    

    this.removeAll();
    this.validate();
    this.addCenter(Parameter_ScrollPanel);
    this.validate();
    this.enableParameterFields();
    if (Mode_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_MODES[
    ClusterTextUnitVectorsWekaParameter.APPLICATION_PHASE])) {
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
    
    if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_SIMPLE_KMEANS]))
      return DIAsDEMguiPreferences.WEKA_SIMPLE_KMEANS_FILE_EXTENSION;
    if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_COBWEB]))
      return DIAsDEMguiPreferences.WEKA_COBWEB_FILE_EXTENSION;
    if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_EM]))
      return DIAsDEMguiPreferences.WEKA_EM_FILE_EXTENSION;
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private KFileFilter getRequiredFileFilter() {
   
    if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_SIMPLE_KMEANS]))
      return DIAsDEMguiPreferences.WEKA_SIMPLE_KMEANS_FILE_FILTER;
    if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_COBWEB]))
      return DIAsDEMguiPreferences.WEKA_COBWEB_FILE_FILTER;
    if (Algorithm_Combo != null && Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_EM]))
      return DIAsDEMguiPreferences.WEKA_EM_FILE_FILTER;
    
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void enableParameterFields() {
    
    if ( Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_SIMPLE_KMEANS] ) ) {
      if (NumberOfClusters_Text != null) {
        NumberOfClusters_Text.setEnabled(true);
        NumberOfClusters_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_NUMBER_OF_CLUSTERS") );
      }
      if (Acuity_Text != null) {
        Acuity_Text.setEnabled(false);
        Acuity_Text.setText("");
      }
      if (Cutoff_Text != null) {
        Cutoff_Text.setEnabled(false);
        Cutoff_Text.setText("");
      }
      if (MaxIterations_Text != null) {
        MaxIterations_Text.setEnabled(false);
        MaxIterations_Text.setText("");
      }
      if (RandomNumberSeed_Text != null) {
        RandomNumberSeed_Text.setEnabled(false);
        RandomNumberSeed_Text.setText("");
      }
      if (MinStdDev_Text != null) {
        MinStdDev_Text.setEnabled(false);
        MinStdDev_Text.setText("");
      }
    }
    if ( Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_COBWEB] ) ) {
      if (NumberOfClusters_Text != null) {
        NumberOfClusters_Text.setEnabled(false);
        NumberOfClusters_Text.setText("");
      }
      if (Acuity_Text != null) {
        Acuity_Text.setEnabled(true);
        Acuity_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_ACUITY") );
      }
      if (Cutoff_Text != null) {
        Cutoff_Text.setEnabled(true);
        Cutoff_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_CUTOFF") );
      }
      if (MaxIterations_Text != null) {
        MaxIterations_Text.setEnabled(false);
        MaxIterations_Text.setText("");
      }
      if (RandomNumberSeed_Text != null) {
        RandomNumberSeed_Text.setEnabled(false);
        RandomNumberSeed_Text.setText("");
      }
      if (MinStdDev_Text != null) {
        MinStdDev_Text.setEnabled(false);
        MinStdDev_Text.setText("");
      }
    }    
    if ( Algorithm_Combo.getSelectedString().equals(
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_ALGORITHMS[
    ClusterTextUnitVectorsWekaParameter.WEKA_EM] ) ) {
      if (NumberOfClusters_Text != null) {
        NumberOfClusters_Text.setEnabled(true);
        NumberOfClusters_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_NUMBER_OF_CLUSTERS") );
      }
      if (Acuity_Text != null) {
        Acuity_Text.setEnabled(false);
        Acuity_Text.setText("");
      }
      if (Cutoff_Text != null) {
        Cutoff_Text.setEnabled(false);
        Cutoff_Text.setText("");
      }
      if (MaxIterations_Text != null) {
        MaxIterations_Text.setEnabled(true);
        MaxIterations_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_MAX_ITERATIONS") );
      }
      if (RandomNumberSeed_Text != null) {
        RandomNumberSeed_Text.setEnabled(true);
        RandomNumberSeed_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_RANDOM_NUMBER_SEED") );
      }
      if (MinStdDev_Text != null) {
        MinStdDev_Text.setEnabled(true);
        MinStdDev_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_MIN_STD_DEVIATION") );
      }
    }    
    
  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private void disableParameterFields() {
    
    if (NumberOfClusters_Text != null) {
      NumberOfClusters_Text.setEnabled(false);
      NumberOfClusters_Text.setText("");
    }
    if (Acuity_Text != null) {
      Acuity_Text.setEnabled(false);
      Acuity_Text.setText("");
    }
    if (Cutoff_Text != null) {
      Cutoff_Text.setEnabled(false);
      Cutoff_Text.setText("");
    }
    if (MaxIterations_Text != null) {
      MaxIterations_Text.setEnabled(false);
      MaxIterations_Text.setText("");
    }
    if (RandomNumberSeed_Text != null) {
      RandomNumberSeed_Text.setEnabled(false);
      RandomNumberSeed_Text.setText("");
    }
    if (MinStdDev_Text != null) {
      MinStdDev_Text.setEnabled(false);
      MinStdDev_Text.setText("");
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