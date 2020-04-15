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

package org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits;

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

public class LemmatizeTextUnitsParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private LemmatizeTextUnitsParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField TreeTaggerInput_Text = null;
  private KButtonPanel TreeTaggerInput_Button = null;
  private KTextField TreeTaggerOutput_Text = null;
  private KButtonPanel TreeTaggerOutput_Button = null;
  private KTextField TreeTaggerCommand_Text = null;
  private KComboBox Algorithm_Combo = null;
  private KTextField LemmaForms_Text = null;
  private KButtonPanel LemmaForms_Button = null;
  private KTextField UnknownLemmaForms_Text = null;
  private KButtonPanel UnknownLemmaForms_Button = null;
  private JCheckBox CreateNewLemmaFormsFile_CheckBox = null;
  private JCheckBox AppendPosTagToEachToken_CheckBox = null;
  
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
  
  public LemmatizeTextUnitsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public LemmatizeTextUnitsParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if ( ActionCommand.equals("CollectionButton") ) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
    
    } else if (ActionCommand.equals("TreeTaggerInputButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      TreeTaggerInput_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select TreeTagger Input File to be Created",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
    
    } else if (ActionCommand.equals("TreeTaggerOutputButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      TreeTaggerOutput_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select TreeTagger Output File to be Created",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
    
    } else if (ActionCommand.equals("LemmaFormsButton")) {
      
      if (CreateNewLemmaFormsFile_CheckBox.isSelected()) {
        CurrentParameterDirectory = this.fileNameButtonClicked(
        LemmaForms_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY",
        "Select", KeyEvent.VK_S, null, "Select Known Lemma Forms File to be Created",
        DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      }
      else {
        CurrentParameterDirectory = this.fileNameButtonClicked(
        LemmaForms_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY",
        "Select", KeyEvent.VK_S, null, "Select Existing Known Lemma Forms File",
        DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      }
    
    } else if (ActionCommand.equals("UnknownLemmaFormsButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      UnknownLemmaForms_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing or New Unknown Lemma Forms File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
    
    } else if (ActionCommand.equals("CreateNewLemmaFormsFile")) {
      this.createNewLemmaFormsFile();
    } else if (ActionCommand.equals("AlgorithmCombo")) {
      this.doAlgorithmSpecificSettings();
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
    
    return "Lemmatize Text Units";
    
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
    
    LemmatizeTextUnitsParameter parameter = new LemmatizeTextUnitsParameter(
    Collection_Text.getText(),
    TreeTaggerInput_Text.getText(),
    TreeTaggerOutput_Text.getText(),
    TreeTaggerCommand_Text.getText(),
    Algorithm_Combo.getSelectedString(),
    LemmaForms_Text.getText(),
    UnknownLemmaForms_Text.getText(), false, false );
    if (CreateNewLemmaFormsFile_CheckBox.isSelected()) {
      parameter.setCreateKnownLemmaFormsFile(true);
    }
    else {
      parameter.setCreateKnownLemmaFormsFile(false);
    }
    if (AppendPosTagToEachToken_CheckBox.isSelected()) {
      parameter.setAppendPosTagToEachToken(true);
    }
    else {
      parameter.setAppendPosTagToEachToken(false);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof LemmatizeTextUnitsParameter) {
      CastParameter = (LemmatizeTextUnitsParameter)pTaskParameter;
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
    if (TreeTaggerInput_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_TREETAGGER_INPUT_FILE",
      TreeTaggerInput_Text.getText());
    }
    if (TreeTaggerOutput_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_TREETAGGER_OUTPUT_FILE",
      TreeTaggerOutput_Text.getText());
    }
    if ( LemmaForms_Text.getText().length() > 0 &&
    ! CreateNewLemmaFormsFile_CheckBox.isSelected() ) {
      DiasdemProject.setProperty("DEFAULT_KNOWN_LEMMA_FORMS_FILE",
      LemmaForms_Text.getText());
    }
    if (UnknownLemmaForms_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_UNKNOWN_LEMMA_FORMS_FILE",
      UnknownLemmaForms_Text.getText());
    }
    DiasdemProject.setProperty("DEFAULT_LEMMATIZATION_ALGORITHM_INDEX",
    String.valueOf(Algorithm_Combo.getSelectedIndex()));
    if (AppendPosTagToEachToken_CheckBox != null
    && AppendPosTagToEachToken_CheckBox.isEnabled())  {
      DiasdemProject.setBooleanProperty(
      "LEMMATIZE_TEXT_UNITS:_DEFAULT_APPEND_POS_TAG_TO_TOKENS",
      AppendPosTagToEachToken_CheckBox.isSelected());
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
    
    Algorithm_Combo = new KComboBox(LemmatizeTextUnitsParameter
    .ALGORITHMS.length, true, "AlgorithmCombo", this);
    for (int i = 0; i < LemmatizeTextUnitsParameter.ALGORITHMS.length; i++) {
      Algorithm_Combo.addItem(LemmatizeTextUnitsParameter.ALGORITHMS[i], false);
    }

    CreateNewLemmaFormsFile_CheckBox = new KCheckBox(
      "Create New Known Lemma Forms File", false, true, 
      "CreateNewLemmaFormsFile", this, KeyEvent.VK_N, 
    "If this box is checked, running TreeTagger will create a list " +
    "of lemma forms.");
    
    AppendPosTagToEachToken_CheckBox = new KCheckBox(
      "Append Part of Speech Tag to Each Token", false, true, 
      "AppendPosTagToEachToken", this, KeyEvent.VK_A, 
    "If this box is checked, the POS tag will be appended to each token"
    + "(e.g., test/p:n).");
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      TreeTaggerInput_Text = new KTextField(CastParameter
      .getParserFileName(), 30);
      TreeTaggerOutput_Text = new KTextField(CastParameter
      .getTaggedFileName(), 30);
      if (CastParameter.getTreeTaggerCommand() != null
      && CastParameter.getTreeTaggerCommand().length() >= 0) {
        TreeTaggerCommand_Text = new KTextField(CastParameter
        .getTreeTaggerCommand(), 30);
      }
      else {
        TreeTaggerCommand_Text = new KTextField(DiasdemProject.getProperty(
        "TREE_TAGGER_COMMAND"), 30);
      }
      LemmaForms_Text = new KTextField(CastParameter
      .getLemmaFormListFileName(), 30);
      UnknownLemmaForms_Text = new KTextField(CastParameter
      .getUnknownLemmaFormsFileName(), 30);
      Algorithm_Combo.setSelectedIndex(CastParameter.getAlgorithm());
      if (CastParameter.createKnownLemmaFormsFile()) {
        CreateNewLemmaFormsFile_CheckBox.setSelected(true);
      }
      else {
        CreateNewLemmaFormsFile_CheckBox.setSelected(false);
      }
      if (CastParameter.appendPosTagToEachToken()) {
        AppendPosTagToEachToken_CheckBox.setSelected(true);
      }
      else {
        AppendPosTagToEachToken_CheckBox.setSelected(false);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      if (DiasdemProject.getProperty("DEFAULT_TREETAGGER_INPUT_FILE")
      .length() == 0) {
        TreeTaggerInput_Text = new KTextField(Tools.ensureTrailingSlash(
        DiasdemProject.getProperty("PROJECT_DIRECTORY") )
        + "treetagger.input" + GuiClientPreferences.TEXT_FILE_EXTENSION, 30);
      }
      else {
        TreeTaggerInput_Text = new KTextField(DiasdemProject.getProperty(
        "DEFAULT_TREETAGGER_INPUT_FILE"), 30);
      }      
      if (DiasdemProject.getProperty("DEFAULT_TREETAGGER_OUTPUT_FILE")
      .length() == 0) {
        TreeTaggerOutput_Text = new KTextField(Tools.ensureTrailingSlash(
        DiasdemProject.getProperty("PROJECT_DIRECTORY") )
        + "treetagger.output" + DiasdemGuiPreferences.TEXT_FILE_EXTENSION, 30);
      }
      else {
        TreeTaggerOutput_Text = new KTextField(DiasdemProject.getProperty(
        "DEFAULT_TREETAGGER_OUTPUT_FILE"), 30);
      }
      TreeTaggerCommand_Text = new KTextField( DiasdemProject.getProperty(
      "TREE_TAGGER_COMMAND"), 30);
      LemmaForms_Text = new KTextField(30);
      UnknownLemmaForms_Text = new KTextField(30);
      if (DiasdemProject.getIntProperty("DEFAULT_LEMMATIZATION_ALGORITHM_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_LEMMATIZATION_ALGORITHM_INDEX")
      < LemmatizeTextUnitsParameter.ALGORITHMS.length) {
        Algorithm_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_LEMMATIZATION_ALGORITHM_INDEX"));
      }
      else {
        Algorithm_Combo.setSelectedIndex(LemmatizeTextUnitsParameter
        .USE_TREETAGGER_TO_DETERMINE_LEMMA_FORM);
      }
      if (DiasdemProject.getBooleanProperty(
      "LEMMATIZE_TEXT_UNITS:_DEFAULT_APPEND_POS_TAG_TO_TOKENS")) {
        AppendPosTagToEachToken_CheckBox.setSelected(true);
      }
      else {
        AppendPosTagToEachToken_CheckBox.setSelected(false);
      }
    }
    if (TreeTaggerCommand_Text.getText().length() == 0) {
      TreeTaggerCommand_Text.setText(DiasdemGuiPreferences.getProperty(
      "DEFAULT_TREE_TAGGER_COMMAND"));
    }
    Collection_Text.setCaretAtEnding();
    TreeTaggerInput_Text.setCaretAtEnding();
    TreeTaggerOutput_Text.setCaretAtEnding();
    TreeTaggerCommand_Text.setEnabled(false);
    LemmaForms_Text.setEnabled(false);
    UnknownLemmaForms_Text.setEnabled(false);
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
     KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");    
    
    TreeTaggerInput_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TreeTaggerInput_Button.addSingleButton("...", 
      KeyEvent.VK_I, true, true, "TreeTaggerInputButton", this,
    "Click this button to select the TreeTagger input file.");  
    
    TreeTaggerOutput_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TreeTaggerOutput_Button.addSingleButton("...", 
      KeyEvent.VK_O, true, true, "TreeTaggerOutputButton", this,
    "Click this button to select the TreeTagger output file.");    
    
    LemmaForms_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    LemmaForms_Button.addSingleButton("...", 
      KeyEvent.VK_K, false, true, "LemmaFormsButton", this,
    "Click this button to select the lemma forms file.");   
    
    UnknownLemmaForms_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    UnknownLemmaForms_Button.addSingleButton("...", 
      KeyEvent.VK_U, false, true, "UnknownLemmaFormsButton", this,
    "Click this button to select the unknown lemma forms file.");   

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
    Parameter_Panel.addLabel("Lemmatization Algorithm:", 0, 2, KeyEvent.VK_L,
      Algorithm_Combo, true,
    "Task input: Select the algorithm to be employed for lemmatization.");
    Parameter_Panel.addComponent(Algorithm_Combo, 2, 2, new Insets(0, 0, 0, 0), 
      3, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("TreeTagger Input File:", 0, 4, KeyEvent.VK_I,
      TreeTaggerInput_Button.getDefaultButton(), true,
    "Task output: This temporary file will contain TreeTagger input.");
    Parameter_Panel.addComponent(TreeTaggerInput_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(TreeTaggerInput_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("TreeTagger Output File:", 0, 6, KeyEvent.VK_O,
      TreeTaggerOutput_Button.getDefaultButton(), true,
    "Task output: This temporary file will contain TreeTagger output.");
    Parameter_Panel.addComponent(TreeTaggerOutput_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(TreeTaggerOutput_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("TreeTagger Command:", 0, 8);
    Parameter_Panel.addComponent( TreeTaggerCommand_Text, 2, 8, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Known Lemma Forms:", 0, 10, KeyEvent.VK_K,
      LemmaForms_Button.getDefaultButton(), true,
    "Task input: This file contains a list of known terms and their " +
    "lemma forms.");
    Parameter_Panel.addComponent(LemmaForms_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(LemmaForms_Button, 4, 10);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("Unknown Lemma Forms:", 0, 12, KeyEvent.VK_U,
      UnknownLemmaForms_Button.getDefaultButton(), true,
    "Task output: This file will contain terms whose lemma forms " +
    "are not listed.");
    Parameter_Panel.addComponent(UnknownLemmaForms_Text, 2, 12);
    Parameter_Panel.addKButtonPanel(UnknownLemmaForms_Button, 4, 12);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 14);
    Parameter_Panel.addComponent(CreateNewLemmaFormsFile_CheckBox, 2, 14, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 15);
    Parameter_Panel.addComponent(AppendPosTagToEachToken_CheckBox, 2, 15, 
      new Insets(0, 0, 0, 0), 3, 1);

    Parameter_Panel.setPreferredSize(Parameter_Panel.getMinimumSize());    
    KScrollBorderPanel Parameter_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    Parameter_ScrollPanel.addNorth(Parameter_Panel);
    Parameter_ScrollPanel.startFocusForwarding(Collection_Text);
    
    this.removeAll();
    this.validate();
    this.addCenter(Parameter_ScrollPanel);
    this.validate();
    this.doAlgorithmSpecificSettings();
    this.setComponentStatus();    
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createNewLemmaFormsFile() {
    
    if ( CreateNewLemmaFormsFile_CheckBox.isSelected() ) {
      if (LemmaForms_Text != null) {
        LemmaForms_Text.setText("");
        LemmaForms_Text.setCaretAtEnding();
        LemmaForms_Text.setEnabled(true);
      }
      if (LemmaForms_Button != null)
        LemmaForms_Button.setAllEnabled(true);
    }
    else {
      if (LemmaForms_Text != null) {
        LemmaForms_Text.setText("");
        LemmaForms_Text.setEnabled(false);
      }
      if (LemmaForms_Button != null)
        LemmaForms_Button.setAllEnabled(false);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void doAlgorithmSpecificSettings() {
    
    if (Algorithm_Combo.getSelectedString().equals(
    LemmatizeTextUnitsParameter.ALGORITHMS[
    LemmatizeTextUnitsParameter.USE_TREETAGGER_TO_DETERMINE_LEMMA_FORM])) {
      if (CreateNewLemmaFormsFile_CheckBox != null)  {
        CreateNewLemmaFormsFile_CheckBox.setSelected(false);
        CreateNewLemmaFormsFile_CheckBox.setEnabled(true);
      }
      if (AppendPosTagToEachToken_CheckBox != null)  {
        AppendPosTagToEachToken_CheckBox.setSelected(false);
        AppendPosTagToEachToken_CheckBox.setEnabled(true);
        if (CastParameter != null) {
          AppendPosTagToEachToken_CheckBox.setSelected(CastParameter
          .appendPosTagToEachToken());
        }
        else {
          AppendPosTagToEachToken_CheckBox.setSelected(DiasdemProject
          .getBooleanProperty(
          "LEMMATIZE_TEXT_UNITS:_DEFAULT_APPEND_POS_TAG_TO_TOKENS"));
        }
      }
      if (LemmaForms_Text != null) {
        LemmaForms_Text.setText("");
        LemmaForms_Text.setEnabled(false);
      }
      if (LemmaForms_Button != null)
        LemmaForms_Button.setAllEnabled(false);
      if (UnknownLemmaForms_Text != null) {
        UnknownLemmaForms_Text.setText("");
        UnknownLemmaForms_Text.setEnabled(false);
      }
      if (UnknownLemmaForms_Button != null)
        UnknownLemmaForms_Button.setAllEnabled(false);
      if (TreeTaggerOutput_Text != null) {
        TreeTaggerOutput_Text.setEnabled(true);
        if (CastParameter != null) {
          TreeTaggerOutput_Text.setText(CastParameter
          .getTaggedFileName());
        }
        else {
          if (DiasdemProject.getProperty("DEFAULT_TREETAGGER_OUTPUT_FILE")
          .length() == 0) {
            TreeTaggerOutput_Text.setText(Tools.ensureTrailingSlash(
            DiasdemProject.getProperty("PROJECT_DIRECTORY"))
            + "treetagger.output" + DiasdemGuiPreferences.TEXT_FILE_EXTENSION);
          }
          else {
            TreeTaggerOutput_Text.setText(DiasdemProject.getProperty(
            "DEFAULT_TREETAGGER_OUTPUT_FILE"));
          }
        }
        TreeTaggerInput_Text.setCaretAtEnding();
      }
      if (TreeTaggerOutput_Button != null)
        TreeTaggerOutput_Button.setAllEnabled(true);
      if (TreeTaggerInput_Text != null) {
        TreeTaggerInput_Text.setEnabled(true);
        if (CastParameter != null) {
          TreeTaggerInput_Text.setText(CastParameter
          .getParserFileName());
        }
        else {
          if (DiasdemProject.getProperty("DEFAULT_TREETAGGER_INPUT_FILE")
          .length() == 0) {
            TreeTaggerInput_Text.setText(Tools.ensureTrailingSlash(
            DiasdemProject.getProperty("PROJECT_DIRECTORY") )
            + "treetagger.input" + DiasdemGuiPreferences.TEXT_FILE_EXTENSION);
          }
          else {
            TreeTaggerInput_Text.setText(DiasdemProject.getProperty(
            "DEFAULT_TREETAGGER_INPUT_FILE"));
          }
          TreeTaggerInput_Text.setCaretAtEnding();
        }
      }
      if (TreeTaggerInput_Button != null)
        TreeTaggerInput_Button.setAllEnabled(true);
    }
    if (Algorithm_Combo.getSelectedString().equals(
    LemmatizeTextUnitsParameter.ALGORITHMS[
    LemmatizeTextUnitsParameter.LOOK_UP_LEMMA_FORM_IN_LIST])) {
      if (CreateNewLemmaFormsFile_CheckBox != null)  {
        CreateNewLemmaFormsFile_CheckBox.setSelected(false);
        CreateNewLemmaFormsFile_CheckBox.setEnabled(false);
      }
      if (AppendPosTagToEachToken_CheckBox != null)  {
        AppendPosTagToEachToken_CheckBox.setSelected(false);
        AppendPosTagToEachToken_CheckBox.setEnabled(false);
      }
      if (LemmaForms_Text != null) {
        LemmaForms_Text.setEnabled(true);
        if (CastParameter != null) {
          LemmaForms_Text.setText(CastParameter
          .getLemmaFormListFileName());
        }
        else {
          LemmaForms_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_KNOWN_LEMMA_FORMS_FILE"));
        }
        LemmaForms_Text.setCaretAtEnding();
      }
      if (LemmaForms_Button != null)
        LemmaForms_Button.setAllEnabled(true);
      if (UnknownLemmaForms_Text != null) {
        UnknownLemmaForms_Text.setEnabled(true);
        if (CastParameter != null) {
          UnknownLemmaForms_Text.setText(CastParameter
          .getUnknownLemmaFormsFileName());
        }
        else {
          UnknownLemmaForms_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_UNKNOWN_LEMMA_FORMS_FILE"));
        }
        UnknownLemmaForms_Text.setCaretAtEnding();
      }
      if (UnknownLemmaForms_Button != null)
        UnknownLemmaForms_Button.setAllEnabled(true);
      if (TreeTaggerOutput_Text != null) {
        TreeTaggerOutput_Text.setText("");
        TreeTaggerOutput_Text.setEnabled(false);
      }
      if (TreeTaggerOutput_Button != null)
        TreeTaggerOutput_Button.setAllEnabled(false);
      if (TreeTaggerInput_Text != null) {
        TreeTaggerInput_Text.setText("");
        TreeTaggerInput_Text.setEnabled(false);
      }
      if (TreeTaggerInput_Button != null)
        TreeTaggerInput_Button.setAllEnabled(false);
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