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

package org.hypknowsys.diasdem.tasks.prepare.createTextUnits;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
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

public class CreateTextUnitsParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private CreateTextUnitsParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KComboBox Algorithm_Combo = null;
  private KTextField AbbrevName_Text = null;
  private KButtonPanel AbbrevName_Button = null;
  private KTextField RegexName_Text = null;
  private KButtonPanel RegexName_Button = null;
  private KComboBox KeepStars_Combo = null;
  private KComboBox TextUnitsLayer_Combo = null;
   
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
  
  public CreateTextUnitsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateTextUnitsParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
      
      CurrentProjectDirectory = this.fileNameButtonClicked(Collection_Text, 
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S, 
      null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("AbbrevNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(AbbrevName_Text, 
      CurrentParameterDirectory, "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing Abbreviations File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("RegexNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(RegexName_Text, 
      CurrentParameterDirectory, "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing Full Stop Regex File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
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
    
    return "Create Text Units";
    
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
    
    CreateTextUnitsParameter parameter = new CreateTextUnitsParameter(
    Collection_Text.getText(),
    AbbrevName_Text.getText(),
    RegexName_Text.getText(),
    Algorithm_Combo.getSelectedString(),
    KeepStars_Combo.getSelectedString(),
    TextUnitsLayer_Combo.getSelectedString());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof CreateTextUnitsParameter) {
      CastParameter = (CreateTextUnitsParameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_ABBREVIATIONS_FILE",
    AbbrevName_Text.getText());
    DiasdemProject.setProperty("DEFAULT_FULL_STOP_REGEX_FILE",
    RegexName_Text.getText());
    DiasdemProject.quickSave();    
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
    
    Algorithm_Combo = new KComboBox(CreateTextUnitsParameter.ALGORITHMS.length, 
      true, "AlgorithmCombo", this);
    for (int i = 0; i < CreateTextUnitsParameter.ALGORITHMS.length; i++)
      Algorithm_Combo.addItem(CreateTextUnitsParameter.ALGORITHMS[i], false);

    KeepStars_Combo = new KComboBox(CreateTextUnitsParameter
    .KEEP_ASTERISKS_OPTIONS.length, true, "KeepStarsCombo", this);
    for (int i = 0; i < CreateTextUnitsParameter.KEEP_ASTERISKS_OPTIONS
    .length; i++)
      KeepStars_Combo.addItem(CreateTextUnitsParameter
      .KEEP_ASTERISKS_OPTIONS[i], false);

    TextUnitsLayer_Combo = new KComboBox(CreateTextUnitsParameter
    .TEXT_UNITS_LAYER_OPTIONS.length, true, "TextUnitLayerCombo", this);
    for (int i = 0; i < CreateTextUnitsParameter.TEXT_UNITS_LAYER_OPTIONS
    .length; i++)
      TextUnitsLayer_Combo.addItem(CreateTextUnitsParameter
      .TEXT_UNITS_LAYER_OPTIONS[i], false);

    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      Algorithm_Combo.setSelectedIndex(CastParameter.getAlgorithm());
      AbbrevName_Text = new KTextField(CastParameter
      .getAbbreviationsFileName(), 30);
      RegexName_Text = new KTextField(CastParameter
      .getRegexFileName(), 30);
      if (CastParameter.getKeepAsterisks()) {
        KeepStars_Combo.setSelectedIndex(CreateTextUnitsParameter
        .KEEP_ASTERISKS_FOR_TOKENIZATION);
      }
      else {
        KeepStars_Combo.setSelectedIndex(CreateTextUnitsParameter
        .DO_NOT_KEEP_ASTERISKS_FOR_TOKENIZATION);
      }
      if (CastParameter.getTextUnitsLayer() >= 0 
      && CastParameter.getTextUnitsLayer() < 
      CreateTextUnitsParameter.TEXT_UNITS_LAYER_OPTIONS.length) {
        TextUnitsLayer_Combo.setSelectedIndex(
        CastParameter.getTextUnitsLayer());
      }
      else {
        TextUnitsLayer_Combo.setSelectedIndex(
        CreateTextUnitsParameter.CREATE_OR_REPLACE_DEFAULT_TEXT_UNITS_LAYER);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      Algorithm_Combo.setSelectedIndex(CreateTextUnitsParameter
      .HEURISTIC_SENTENCE_IDENTIFIER);
      AbbrevName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_ABBREVIATIONS_FILE"), 30);
      RegexName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_FULL_STOP_REGEX_FILE"), 30);
      KeepStars_Combo.setSelectedIndex(CreateTextUnitsParameter
      .KEEP_ASTERISKS_FOR_TOKENIZATION);
      TextUnitsLayer_Combo.setSelectedIndex(
      CreateTextUnitsParameter.CREATE_OR_REPLACE_DEFAULT_TEXT_UNITS_LAYER);
    }
    Collection_Text.setCaretAtEnding();
    AbbrevName_Text.setCaretAtEnding();
    RegexName_Text.setCaretAtEnding();
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
      KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");
    
    AbbrevName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    AbbrevName_Button.addSingleButton("...", 
      KeyEvent.VK_A, true, true, "AbbrevNameButton", this,
    "Click this button to select the abbreviation file.");   
    
    RegexName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    RegexName_Button.addSingleButton("...", 
      KeyEvent.VK_F, true, true, "RegexNameButton", this,
    "Click this button to select the full stop regex file.");    

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
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Text Unit Algorithm:", 0, 2, KeyEvent.VK_T,
      Algorithm_Combo, true,
    "Task input: Select an algorithm for the creation of text units.");
    Parameter_Panel.addComponent(Algorithm_Combo, 2, 2, new Insets(0, 0, 0, 0), 
      3, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Abbreviations File:", 0, 4, KeyEvent.VK_A,
      AbbrevName_Button.getDefaultButton(), true,
    "Task input: This file contains language- and domain-specific " +
    "abbreviations.");
    Parameter_Panel.addComponent(AbbrevName_Text, 2, 4);
    Parameter_Panel.addBlankColumn(3, 4, 12);
    Parameter_Panel.addKButtonPanel(AbbrevName_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Full Stop Regex File:", 0, 6, KeyEvent.VK_F,
      RegexName_Button.getDefaultButton(), true,
    "Task input: This file contains language- and domain-specific " +
    "regular expressions.");
    Parameter_Panel.addComponent(RegexName_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(RegexName_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Replaced Full Stops:", 0, 8, KeyEvent.VK_R,
      KeepStars_Combo, true,
    "Task input: Should asterisks that replace full stops be " +
    "retained tor tokenization?");
    Parameter_Panel.addComponent(KeepStars_Combo, 2, 8, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Text Units Layer:", 0, 10, KeyEvent.VK_L,
      TextUnitsLayer_Combo, true,
    "Task input: Which text units layer should be associated with new text units?");
    Parameter_Panel.addComponent(TextUnitsLayer_Combo, 2, 10, 
    new Insets(0, 0, 0, 0), 3, 1);

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
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}