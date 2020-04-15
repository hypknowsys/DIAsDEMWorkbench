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
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KCheckBox;
import org.hypknowsys.misc.swing.KComboBox;
import org.hypknowsys.misc.swing.KFileFilter;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class TagTextUnitsParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TagTextUnitsParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField ClusterLabelFile_Text = null;
  private KButtonPanel ClusterLabelFile_Button = null;
  private KTextField ClusterResultFile_Text = null;
  private KButtonPanel ClusterResultFile_Button = null;
  private KTextField Iteration_Text = null;
  private KComboBox ResultFileFormat_Combo = null;
  private KCheckBox IgnoreFirstLine_CheckBox = null;
  
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
  
  public TagTextUnitsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagTextUnitsParameterPanel(Server pDiasdemServer,
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
    else if (ActionCommand.equals("ClusterLabelFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ClusterLabelFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Cluster Label File to be Created",
      DIAsDEMguiPreferences.CLUSTER_LABEL_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("ClusterResultFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ClusterResultFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Clustering Results File",
      this.getRequiredFileFilter(), false, true);
      
    }
    else if (ActionCommand.equals("ResultFileFormat_Combo")) {
      
      if (ClusterResultFile_Text != null
      && ClusterResultFile_Text.getText().length() > 0) {
        ClusterResultFile_Text.setText(Tools.removeFileExtension(
        ClusterResultFile_Text.getText()) + this.getRequiredFileExtension());
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
    
    return "Tag Text Units";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    TagTextUnitsParameter parameter = new TagTextUnitsParameter(
    Collection_Text.getText(),
    ClusterLabelFile_Text.getText(),
    ClusterResultFile_Text.getText(),
    ResultFileFormat_Combo.getSelectedString(),
    Tools.string2Int(Iteration_Text.getText()), false);
    if (IgnoreFirstLine_CheckBox.isSelected()) {
      parameter.setIgnoreFirstResultFileLine(true);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof TagTextUnitsParameter) {
      CastParameter = (TagTextUnitsParameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_CLUSTER_LABEL_FILE",
    ClusterLabelFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_ITERATION",
    Iteration_Text.getText());
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
    
    ResultFileFormat_Combo = new KComboBox(TagTextUnitsParameter
    .RESULT_FILE_FORMAT.length, true, "ResultFileFormat_Combo", this);
    for (int i = 0; i < TagTextUnitsParameter.RESULT_FILE_FORMAT
    .length; i++) {
      ResultFileFormat_Combo.addItem(TagTextUnitsParameter
      .RESULT_FILE_FORMAT[i], false);
    }
    
    IgnoreFirstLine_CheckBox = new KCheckBox(
    "Ignore First Line of Cluster Result File", false, true,
    "IgnoreFirstLine", this, KeyEvent.VK_G, "Check this box, if the first "
    + "line of cluster result file contains attribute names.");
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      ClusterLabelFile_Text = new KTextField(CastParameter
      .getClusterLabelFileName(), 30);
      ClusterResultFile_Text = new KTextField(CastParameter
      .getClusterResultFileName(), 30);
      Iteration_Text = new KTextField(CastParameter
      .getIteration() + "", 30);
      
      if (CastParameter.getClusterResultFileFormat() >= 0
      && CastParameter.getClusterResultFileFormat()
      < TagTextUnitsParameter.RESULT_FILE_FORMAT.length) {
        ResultFileFormat_Combo.setSelectedIndex(CastParameter
        .getClusterResultFileFormat());
      }
      else {
        ResultFileFormat_Combo.setSelectedIndex(TagTextUnitsParameter
        .CSV_FILE);
      }
      if (CastParameter.ignoreFirstResultFileLine()) {
        IgnoreFirstLine_CheckBox.setSelected(true);
      }
      else {
        IgnoreFirstLine_CheckBox.setSelected(false);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      ClusterLabelFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CLUSTER_LABEL_FILE"), 30);
      ClusterResultFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CLUSTER_RESULT_FILE"), 30);
      Iteration_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_ITERATION"), 30);
      
      if (DiasdemProject.getIntProperty("DEFAULT_RESULT_FILE_FORMAT_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_RESULT_FILE_FORMAT_INDEX")
      < TagTextUnitsParameter.RESULT_FILE_FORMAT.length) {
        ResultFileFormat_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_RESULT_FILE_FORMAT_INDEX"));
      }
      else {
        ResultFileFormat_Combo.setSelectedIndex(TagTextUnitsParameter
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
    ClusterLabelFile_Text.setCaretAtEnding();
    ClusterResultFile_Text.setCaretAtEnding();
    Iteration_Text.setCaretAtEnding();
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");
    
    ClusterResultFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ClusterResultFile_Button.addSingleButton("...",
    KeyEvent.VK_R, true, true, "ClusterResultFileButton", this,
    "Click this button to select the cluster result file.");
    
    ClusterLabelFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ClusterLabelFile_Button.addSingleButton("...",
    KeyEvent.VK_L, true, true, "ClusterLabelFileButton", this,
    "Click this button to select the cluster label file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Collection_Text);
    
    Parameter_Panel.addLabel("Collection File:", 0, 0, KeyEvent.VK_C,
    Collection_Button.getDefaultButton(), true, "Task input: This collection "
    + "file contains references to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("KDT Process Iteration:", 0, 2, KeyEvent.VK_I,
    Iteration_Text, true, "Task input: nter the current iteration of "
    + "the DIAsDEM KDT process.");
    Parameter_Panel.addComponent(Iteration_Text, 2, 2,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Result File Format:", 0, 4, KeyEvent.VK_F,
    ResultFileFormat_Combo, true, "Task input: Select the correct format "
    + "of cluster result file.");
    Parameter_Panel.addComponent(ResultFileFormat_Combo,
    2, 4, new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Cluster Result File:", 0, 6, KeyEvent.VK_R,
    ClusterResultFile_Button.getDefaultButton());
    Parameter_Panel.addComponent(ClusterResultFile_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(ClusterResultFile_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Cluster Label File:", 0, 8, KeyEvent.VK_L,
    ClusterLabelFile_Button.getDefaultButton(), true, "Task input: This "
    + "file contains the results of the current clustering run.");
    Parameter_Panel.addComponent(ClusterLabelFile_Text, 2, 8);
    Parameter_Panel.addKButtonPanel(ClusterLabelFile_Button, 4, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 10);
    Parameter_Panel.addComponent(IgnoreFirstLine_CheckBox,
    2, 10, new Insets(0, 0, 0, 0), 5, 1);
    
    this.removeAll();
    this.validate();
    this.addNorth(Parameter_Panel);
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
    TagTextUnitsParameter.RESULT_FILE_FORMAT[
    TagTextUnitsParameter.CSV_FILE])) {
      return DiasdemGuiPreferences.CSV_FILE_EXTENSION;
    }
    if (ResultFileFormat_Combo != null
    && ResultFileFormat_Combo.getSelectedString().equals(
    TagTextUnitsParameter.RESULT_FILE_FORMAT[
    TagTextUnitsParameter.TXT_FILE])) {
      return DiasdemGuiPreferences.TEXT_FILE_EXTENSION;
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private KFileFilter getRequiredFileFilter() {
    
    if (ResultFileFormat_Combo != null
    && ResultFileFormat_Combo.getSelectedString().equals(
    TagTextUnitsParameter.RESULT_FILE_FORMAT[
    TagTextUnitsParameter.CSV_FILE])) {
      return DiasdemGuiPreferences.CSV_FILE_FILTER;
    }
    if (ResultFileFormat_Combo != null
    && ResultFileFormat_Combo.getSelectedString().equals(
    TagTextUnitsParameter.RESULT_FILE_FORMAT[
    TagTextUnitsParameter.TXT_FILE])) {
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