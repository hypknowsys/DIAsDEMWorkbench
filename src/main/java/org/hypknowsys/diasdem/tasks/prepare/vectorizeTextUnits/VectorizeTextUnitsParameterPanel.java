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

package org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits;

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
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class VectorizeTextUnitsParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private VectorizeTextUnitsParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField Iteration_Text = null;
  private KComboBox VectorDimensions_Combo = null;
  private boolean VectorDimensions_FirstAction = true;
  private KTextField ScopeNotesContain_Text = null;
  private KTextField ThesaurusName_Text = null;
  private KButtonPanel ThesaurusName_Button = null;
  private KTextField VectorName_Text = null;
  private KButtonPanel VectorName_Button = null;
  private KComboBox Format_Combo = null;
  private KComboBox DescriptorFrequency_Combo = null;
  private KCheckBox TermAssocFile_CheckBox = null;
  private KCheckBox MetaDataFile_CheckBox = null;
  private KComboBox WeightsMode_Combo = null;
  private boolean WeightsMode_FirstAction = true;
  private KTextField WeightsName_Text = null;
  private KButtonPanel WeightsName_Button = null;

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
  
  public VectorizeTextUnitsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public VectorizeTextUnitsParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
      
    } else if (ActionCommand.equals("ThesaurusNameButton") ) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      ThesaurusName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Thesaurus File",
      DIAsDEMguiPreferences.THESAURUS_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("VectorNameButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      VectorName_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Text Unit Vectors File to be Created",
      this.getRequiredFileFilter(), false, true);
      
    } else if (ActionCommand.equals("WeightsNameButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      WeightsName_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Descriptor Weights File",
      DIAsDEMguiPreferences.DESCRIPTOR_WEIGHTS_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("VectorDimensionsCombo")) {
      
      this.vectorDimensionsCombo();
      
    } else if (ActionCommand.equals("Format_Combo")) {
      
      if (VectorName_Text != null && VectorName_Text.getText().length() > 0) {
        VectorName_Text.setText(Tools.removeFileExtension(VectorName_Text
        .getText()) + this.getRequiredFileExtension() );
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
    
    return "Vectorize Text Units";
    
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
    
    VectorizeTextUnitsParameter parameter = new VectorizeTextUnitsParameter(
    Collection_Text.getText(),
    Tools.string2Int(Iteration_Text.getText()),
    ThesaurusName_Text.getText(),
    VectorName_Text.getText(),
    Format_Combo.getSelectedString(),
    DescriptorFrequency_Combo.getSelectedString(),
    VectorDimensions_Combo.getSelectedString(),
    ScopeNotesContain_Text.getText(), false, false,
    WeightsMode_Combo.getSelectedString(),
    WeightsName_Text.getText());
    if (TermAssocFile_CheckBox.isSelected()) {
      parameter.setCreateFileForTermAssociationDiscovery(true);
    }
    else {
      parameter.setCreateFileForTermAssociationDiscovery(false);
    }
    if (MetaDataFile_CheckBox.isSelected()) {
      parameter.setCreateMetaDataFile(true);
    }
    else {
      parameter.setCreateMetaDataFile(false);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof VectorizeTextUnitsParameter) {
      CastParameter = (VectorizeTextUnitsParameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_THESAURUS_FILE",
    ThesaurusName_Text.getText());
    DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_VECTORS_FILE",
    VectorName_Text.getText());
    DiasdemProject.setProperty("DEFAULT_DESCRIPTOR_WEIGHTS_FILE",
    WeightsName_Text.getText());
    DiasdemProject.setProperty("DEFAULT_VECTOR_FILE_FORMAT_INDEX",
    String.valueOf(Format_Combo.getSelectedIndex()));
    DiasdemProject.setProperty("DEFAULT_ITERATION",
    Iteration_Text.getText());
    DiasdemProject.setProperty("DEFAULT_DESCRIPTOR_FREQUENCY_INDEX",
    String.valueOf(DescriptorFrequency_Combo.getSelectedIndex()));
    DiasdemProject.setProperty("DEFAULT_DESCRIPTOR_WEIGHTS_INDEX",
    String.valueOf(WeightsMode_Combo.getSelectedIndex()));
    DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX",
    String.valueOf(VectorDimensions_Combo.getSelectedIndex()));
    if (ScopeNotesContain_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_CONTAIN",
      ScopeNotesContain_Text.getText());
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
    
    VectorDimensions_Combo = new KComboBox(VectorizeTextUnitsParameter
    .VECTOR_DIMENSIONS_OPTIONS.length, true, "VectorDimensionsCombo", this);
    for (int i = 0; i < VectorizeTextUnitsParameter.VECTOR_DIMENSIONS_OPTIONS
    .length; i++)
      VectorDimensions_Combo.addItem(VectorizeTextUnitsParameter
      .VECTOR_DIMENSIONS_OPTIONS[i], false);
    
    DescriptorFrequency_Combo = new KComboBox(VectorizeTextUnitsParameter
    .DESCRIPTOR_FREQUENCY_OPTIONS.length, true, "DescriptorFrequencyCombo", this);
    for (int i = 0; i < VectorizeTextUnitsParameter
    .DESCRIPTOR_FREQUENCY_OPTIONS.length; i++)
      DescriptorFrequency_Combo.addItem(VectorizeTextUnitsParameter
      .DESCRIPTOR_FREQUENCY_OPTIONS[i], false);
    DescriptorFrequency_Combo.setSelectedIndex(0);
    
    Format_Combo = new KComboBox(VectorizeTextUnitsParameter
    .VECTOR_FILE_FORMAT_OPTIONS.length, true, "Format_Combo", this);
    for (int i = 0; i < VectorizeTextUnitsParameter
    .VECTOR_FILE_FORMAT_OPTIONS.length; i++)
      Format_Combo.addItem(VectorizeTextUnitsParameter
      .VECTOR_FILE_FORMAT_OPTIONS[i], false);
    
    WeightsMode_Combo = new KComboBox(VectorizeTextUnitsParameter
    .WEIGHTS_MODES.length, true, "WeightsMode_Combo", this);
    for (int i = 0; i < VectorizeTextUnitsParameter
    .WEIGHTS_MODES.length; i++)
      WeightsMode_Combo.addItem(VectorizeTextUnitsParameter
      .WEIGHTS_MODES[i], false);
    
    TermAssocFile_CheckBox = new KCheckBox(
      "Create File for Mining Descriptor Association Rules", false, true, 
      "TermAssocFile", this, KeyEvent.VK_A, 
    "If this box is checked, a special text unit vector file will be created.");

    MetaDataFile_CheckBox = new KCheckBox(
      "Create Meta-Data File for Text Unit Vectors File", false, true, 
      "MetaDataFile", this, KeyEvent.VK_M, 
    "If this box is checked, a descriptive meta-data file will be created.");

    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      Iteration_Text = new KTextField(CastParameter
      .getIteration() + "", 30);
      ThesaurusName_Text = new KTextField(CastParameter
      .getThesaurusFileName(), 30);
      VectorName_Text = new KTextField(CastParameter
      .getVectorFileName(), 30);
      WeightsName_Text = new KTextField(CastParameter
      .getWeightsFileName(), 30);
      
      if (CastParameter.getVectorDimensions() >= 0 
      && CastParameter.getVectorDimensions() < 
      VectorizeTextUnitsParameter.VECTOR_DIMENSIONS_OPTIONS.length) {
        VectorDimensions_Combo.setSelectedIndex(
        CastParameter.getVectorDimensions());
      }
      else {
        VectorDimensions_Combo.setSelectedIndex(
        VectorizeTextUnitsParameter.SPECIFIED_DESCRIPTORS);
      }
      
      if (CastParameter.getVectorDimensions()
      == VectorizeTextUnitsParameter.SPECIFIED_DESCRIPTORS ||
      CastParameter.getVectorDimensions()
      == VectorizeTextUnitsParameter.NOT_SPECIFIED_DESCRIPTORS) {
        ScopeNotesContain_Text = new KTextField(CastParameter
        .getDescriptorsScopeNotesContain(), 30);
        ScopeNotesContain_Text.setEnabled(true);
      }
      else {
        ScopeNotesContain_Text = new KTextField("", 30);
        ScopeNotesContain_Text.setEnabled(false);
      }
      
      if (CastParameter.getDescriptorFrequency() >= 0 && CastParameter.getDescriptorFrequency() 
      < VectorizeTextUnitsParameter.DESCRIPTOR_FREQUENCY_OPTIONS.length) {
        DescriptorFrequency_Combo.setSelectedIndex(CastParameter.getDescriptorFrequency());
      }
      else {
        DescriptorFrequency_Combo.setSelectedIndex(0);
      }
      if (CastParameter.getWeightsMode() >= 0 && CastParameter.getWeightsMode() 
      < VectorizeTextUnitsParameter.WEIGHTS_MODES.length) {
        WeightsMode_Combo.setSelectedIndex(CastParameter.getWeightsMode());
      }
      else {
        WeightsMode_Combo.setSelectedIndex(0);
      }
      if (CastParameter.getFileType() >= 0 && CastParameter.getFileType() 
      < VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS.length) {
        Format_Combo.setSelectedIndex(CastParameter.getFileType());
      }
      else {
        Format_Combo.setSelectedIndex(0);
      }      
      if (CastParameter.createFileForTermAssociationDiscovery()) {
        TermAssocFile_CheckBox.setSelected(true);
      }
      else {
        TermAssocFile_CheckBox.setSelected(false);
      }
      if (CastParameter.createMetaDataFile()) {
        MetaDataFile_CheckBox.setSelected(true);
      }
      else {
        MetaDataFile_CheckBox.setSelected(false);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      Iteration_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_ITERATION"), 30);
      ThesaurusName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_THESAURUS_FILE"), 30);
      VectorName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_TEXT_UNIT_VECTORS_FILE"), 30);
      WeightsName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_DESCRIPTOR_WEIGHTS_FILE"), 30);
      
      if (DiasdemProject.getIntProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX")
      < VectorizeTextUnitsParameter.VECTOR_DIMENSIONS_OPTIONS.length) {
        VectorDimensions_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX"));
      }
      else {
        VectorDimensions_Combo.setSelectedIndex(VectorizeTextUnitsParameter
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
        ScopeNotesContain_Text = new KTextField("", 30);
        ScopeNotesContain_Text.setEnabled(false);
      }
      
      if (DiasdemProject.getIntProperty("DEFAULT_DESCRIPTOR_FREQUENCY_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_DESCRIPTOR_FREQUENCY_INDEX")
      < VectorizeTextUnitsParameter.DESCRIPTOR_FREQUENCY_OPTIONS.length) {
        DescriptorFrequency_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_DESCRIPTOR_FREQUENCY_INDEX"));
      }
      else {
        DescriptorFrequency_Combo.setSelectedIndex(0);
      }
      if (DiasdemProject.getIntProperty("DEFAULT_DESCRIPTOR_WEIGHTS_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_DESCRIPTOR_WEIGHTS_INDEX")
      < VectorizeTextUnitsParameter.WEIGHTS_MODES.length) {
        WeightsMode_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_DESCRIPTOR_WEIGHTS_INDEX"));
      }
      else {
        WeightsMode_Combo.setSelectedIndex(0);
      }
      if (DiasdemProject.getIntProperty("DEFAULT_VECTOR_FILE_FORMAT_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_VECTOR_FILE_FORMAT_INDEX")
      < VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS.length) {
        Format_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_VECTOR_FILE_FORMAT_INDEX"));
      }
      else {
        Format_Combo.setSelectedIndex(0);
      }      
    }
    Collection_Text.setCaretAtEnding();
    ThesaurusName_Text.setCaretAtEnding();
    VectorName_Text.setCaretAtEnding();
    WeightsName_Text.setCaretAtEnding();
    
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
    
    VectorName_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    VectorName_Button.addSingleButton("...",
    KeyEvent.VK_V, true, true, "VectorNameButton", this,
    "Click this button to select the vector file.");
    
    WeightsName_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    WeightsName_Button.addSingleButton("...",
    KeyEvent.VK_D, true, true, "WeightsNameButton", this,
    "Click this button to select the descriptor weights file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Collection_Text);

    Parameter_Panel.addLabel("Collection File:", 0, 0, KeyEvent.VK_C,
      Collection_Button.getDefaultButton(), true,
    "Task input: This collection file contains references " +
    "to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11);
    Parameter_Panel.addLabel("KDD Process Iteration:", 0, 2, KeyEvent.VK_I,
      Iteration_Text, true,
    "Task input: Enter the current iteration of the DIAsDEM KDD process.");
    Parameter_Panel.addComponent(Iteration_Text, 2, 2, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Text Unit Vectors Format:", 0, 4, KeyEvent.VK_R,
      Format_Combo, true,
    "Task input: Select the requested vector file format");
    Parameter_Panel.addComponent(Format_Combo, 2, 4,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Text Unit Vectors File:", 0, 6, KeyEvent.VK_V,
      VectorName_Button.getDefaultButton(), true,
    "Task output: This file will contain text unit vectors in " +
    "the requested format.");
    Parameter_Panel.addComponent(VectorName_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(VectorName_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Thesaurus File:", 0, 8, KeyEvent.VK_T,
      ThesaurusName_Button.getDefaultButton(), true,
    "Task input: This DIAsDEM-specific thesaurus file contains " +
    "text unit descriptors.");
    Parameter_Panel.addComponent(ThesaurusName_Text, 2, 8);
    Parameter_Panel.addKButtonPanel(ThesaurusName_Button, 4, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Text Unit Descriptors:", 0, 10, KeyEvent.VK_U,
      VectorDimensions_Combo, true,
    "Task input: Only matching thesaurus descriptors will correspond to " +
    "vector dimensions.");
    Parameter_Panel.addComponent(VectorDimensions_Combo, 2, 10,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("", 0, 12);
    Parameter_Panel.addComponent(ScopeNotesContain_Text, 2, 12,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("Descriptor Frequency:", 0, 14, KeyEvent.VK_F,
      DescriptorFrequency_Combo, true,
    "Task input: Select an appropriate descriptor frequeny measure.");
    Parameter_Panel.addComponent(DescriptorFrequency_Combo, 2, 14,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 15, 11);
    Parameter_Panel.addLabel("Descriptor Weights:", 0, 16, KeyEvent.VK_W,
      WeightsMode_Combo, true,
    "Task input: Select an appropriate descriptor weighting mode.");
    Parameter_Panel.addComponent(WeightsMode_Combo, 2, 16,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 17, 11);
    Parameter_Panel.addLabel("Descriptor Weights File:", 0, 18, KeyEvent.VK_D,
      WeightsName_Button.getDefaultButton(), true,
    "Task in-/output: This file contains/will contain descriptor weights.");
    Parameter_Panel.addComponent(WeightsName_Text, 2, 18);
    Parameter_Panel.addKButtonPanel(WeightsName_Button, 4, 18);
    Parameter_Panel.addBlankRow(0, 19, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 20);
    Parameter_Panel.addComponent(TermAssocFile_CheckBox, 2, 20, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 21);
    Parameter_Panel.addComponent(MetaDataFile_CheckBox, 2, 21, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addComponent(new JLabel(""), 0, 22, 
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
    this.setComponentStatus();    
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {}

  /* ########## ########## ########## ########## ########## ######### */
  
  private void vectorDimensionsCombo() {
    
    if (VectorDimensions_FirstAction) {
      VectorDimensions_FirstAction = false;
      return;
    }
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
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getRequiredFileExtension() {
    
    if (Format_Combo != null && Format_Combo.getSelectedString().equals(
    VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS[
    VectorizeTextUnitsParameter.CSV_FILE]))
      return DiasdemGuiPreferences.CSV_FILE_EXTENSION;
    if (Format_Combo != null && Format_Combo.getSelectedString().equals(
    VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS[
    VectorizeTextUnitsParameter.ARFF_FILE]))
      return DIAsDEMguiPreferences.ARFF_FILE_EXTENSION;
    if (Format_Combo != null && Format_Combo.getSelectedString().equals(
    VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS[
    VectorizeTextUnitsParameter.SPARSE_ARFF_FILE]))
      return DIAsDEMguiPreferences.ARFF_FILE_EXTENSION;
    if (Format_Combo != null && Format_Combo.getSelectedString().equals(
    VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS[
    VectorizeTextUnitsParameter.TXT_FILE]))
      return DiasdemGuiPreferences.TEXT_FILE_EXTENSION;
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private KFileFilter getRequiredFileFilter() {
   
    if (Format_Combo != null && Format_Combo.getSelectedString().equals(
    VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS[
    VectorizeTextUnitsParameter.CSV_FILE]))
      return DiasdemGuiPreferences.CSV_FILE_FILTER;
    if (Format_Combo != null && Format_Combo.getSelectedString().equals(
    VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS[
    VectorizeTextUnitsParameter.ARFF_FILE]))
      return DIAsDEMguiPreferences.ARFF_FILE_FILTER;
    if (Format_Combo != null && Format_Combo.getSelectedString().equals(
    VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS[
    VectorizeTextUnitsParameter.SPARSE_ARFF_FILE]))
      return DIAsDEMguiPreferences.ARFF_FILE_FILTER;
    if (Format_Combo != null && Format_Combo.getSelectedString().equals(
    VectorizeTextUnitsParameter.VECTOR_FILE_FORMAT_OPTIONS[
    VectorizeTextUnitsParameter.TXT_FILE]))
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