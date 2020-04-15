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

package org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeDocumentMetaData;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
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

public class TokenizeDocumentMetaDataParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TokenizeDocumentMetaDataParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField SourceMetaDataAttribute_Text = null;
  private KTextField TargetMetaDataAttribute_Text = null;
  private KTextField AbbrevName_Text = null;
  private KButtonPanel AbbrevName_Button = null;
  private KTextField RegexName_Text = null;
  private KButtonPanel RegexName_Button = null;
  private KTextField TokenizeRegexName_Text = null;
  private KButtonPanel TokenizeRegexName_Button = null;
  private KTextField NormalizeRegexName_Text = null;
  private KButtonPanel NormalizeRegexName_Button = null;
  private KTextField MultiTokenName_Text = null;
  private KButtonPanel MultiTokenName_Button = null;
  
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
  
  public TokenizeDocumentMetaDataParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TokenizeDocumentMetaDataParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
      
    } else if (ActionCommand.equals("TokenizeRegexNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      TokenizeRegexName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Tokenize Regex File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("NormalizeRegexNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      NormalizeRegexName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Normalize Regex File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("MultiTokenNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      MultiTokenName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Multi Token Word File",
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
    
    return "Tokenize Document Meta Data";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    TokenizeDocumentMetaDataParameter parameter = new TokenizeDocumentMetaDataParameter(
    Collection_Text.getText(),
    SourceMetaDataAttribute_Text.getText(),
    TargetMetaDataAttribute_Text.getText(), 
    AbbrevName_Text.getText(),
    RegexName_Text.getText(),
    TokenizeRegexName_Text.getText(),
    NormalizeRegexName_Text.getText(),
    MultiTokenName_Text.getText());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof TokenizeDocumentMetaDataParameter) {
      CastParameter = (TokenizeDocumentMetaDataParameter)pTaskParameter;
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
    DiasdemProject.setProperty(
    "TOKENIZE_META_DATA:_MRU_SOURCE_META_DATA_ATTRIBUTE",
    SourceMetaDataAttribute_Text.getText());
    DiasdemProject.setProperty(
    "TOKENIZE_META_DATA:_MRU_TARGET_META_DATA_ATTRIBUTE",
    TargetMetaDataAttribute_Text.getText());
    DiasdemProject.setProperty("DEFAULT_ABBREVIATIONS_FILE",
    AbbrevName_Text.getText());
    DiasdemProject.setProperty("DEFAULT_FULL_STOP_REGEX_FILE",
    RegexName_Text.getText());
    DiasdemProject.setProperty("DEFAULT_TOKENIZE_REGEX_FILE",
    TokenizeRegexName_Text.getText());
    DiasdemProject.setProperty("DEFAULT_NORMALIZE_REGEX_FILE",
    NormalizeRegexName_Text.getText());
    DiasdemProject.setProperty("DEFAULT_MULTI_TOKEN_WORDS_FILE",
    MultiTokenName_Text.getText());
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
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      SourceMetaDataAttribute_Text = new KTextField(CastParameter
      .getSourceMetaDataAttribute(), 30);
      TargetMetaDataAttribute_Text = new KTextField(CastParameter
      .getTargetMetaDataAttribute(), 30);
      AbbrevName_Text = new KTextField(CastParameter
      .getAbbreviationsFileName(), 30);
      RegexName_Text = new KTextField(CastParameter
      .getRegexFileName(), 30);
      TokenizeRegexName_Text = new KTextField(CastParameter
      .getTokenizeRegexFileName(), 30);
      NormalizeRegexName_Text = new KTextField(CastParameter
      .getNormalizeRegexFileName(), 30);
      MultiTokenName_Text = new KTextField(CastParameter
      .getMultiTokenFileName(), 30);  
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      SourceMetaDataAttribute_Text = new KTextField(DiasdemProject.getProperty(
      "TOKENIZE_META_DATA:_MRU_SOURCE_META_DATA_ATTRIBUTE"), 30);
      TargetMetaDataAttribute_Text = new KTextField(DiasdemProject.getProperty(
      "TOKENIZE_META_DATA:_MRU_TARGET_META_DATA_ATTRIBUTE"), 30);
      AbbrevName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_ABBREVIATIONS_FILE"), 30);
      RegexName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_FULL_STOP_REGEX_FILE"), 30);
      TokenizeRegexName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_TOKENIZE_REGEX_FILE"), 30);
      NormalizeRegexName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_NORMALIZE_REGEX_FILE"), 30);
      MultiTokenName_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_MULTI_TOKEN_WORDS_FILE"), 30);    
    }
    Collection_Text.setCaretAtEnding();
    SourceMetaDataAttribute_Text.setCaretAtEnding();
    TargetMetaDataAttribute_Text.setCaretAtEnding();
    AbbrevName_Text.setCaretAtEnding();
    RegexName_Text.setCaretAtEnding();
    TokenizeRegexName_Text.setCaretAtEnding();
    NormalizeRegexName_Text.setCaretAtEnding();
    MultiTokenName_Text.setCaretAtEnding();
    
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
    "Click this button to select the regex file.");    

    TokenizeRegexName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TokenizeRegexName_Button.addSingleButton("...", 
      KeyEvent.VK_T, true, true, "TokenizeRegexNameButton", this,
    "Click this button to select the tokenize regex file.");
    
    NormalizeRegexName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    NormalizeRegexName_Button.addSingleButton("...", 
      KeyEvent.VK_N, true, true, "NormalizeRegexNameButton", this,
    "Click this button to select the normalize regex file.");
    
    MultiTokenName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    MultiTokenName_Button.addSingleButton("...", 
      KeyEvent.VK_M, true, true, "MultiTokenNameButton", this,
    "Click this button to select the multi-token words file.");    
    
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
    Parameter_Panel.addLabel("Source Attribute:", 0, 2, KeyEvent.VK_S,
    SourceMetaDataAttribute_Text, true,
    "Task input: Select the meta data attribute to be tokenized.");
    Parameter_Panel.addComponent(SourceMetaDataAttribute_Text, 2, 2, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Target Attribute:", 0, 4, KeyEvent.VK_G,
    TargetMetaDataAttribute_Text, true,
    "Task output: Select the meta data attribute for storing the results.");
    Parameter_Panel.addComponent(TargetMetaDataAttribute_Text, 2, 4, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Abbreviations File:", 0, 6, KeyEvent.VK_A,
      AbbrevName_Button.getDefaultButton(), true,
    "Task input: This file contains language- and domain-specific " +
    "abbreviations.");
    Parameter_Panel.addComponent(AbbrevName_Text, 2, 6);
    Parameter_Panel.addBlankColumn(3, 6, 12);
    Parameter_Panel.addKButtonPanel(AbbrevName_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Full Stop Regex File:", 0, 8, KeyEvent.VK_F,
      RegexName_Button.getDefaultButton(), true,
    "Task input: This file contains language- and domain-specific " +
    "regular expressions.");
    Parameter_Panel.addComponent(RegexName_Text, 2, 8);
    Parameter_Panel.addKButtonPanel(RegexName_Button, 4, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Tokenize Regex File:", 0, 10, KeyEvent.VK_T,
      TokenizeRegexName_Button.getDefaultButton(), true,
    "Task input: This file contains regular expressions for tokenization.");
    Parameter_Panel.addComponent(TokenizeRegexName_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(TokenizeRegexName_Button, 4, 10);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("Normalize Regex File:", 0, 12, KeyEvent.VK_N,
      NormalizeRegexName_Button.getDefaultButton(), true,
    "Task input: This file contains regular expressions for normalization.");
    Parameter_Panel.addComponent(NormalizeRegexName_Text, 2, 12);
    Parameter_Panel.addKButtonPanel(NormalizeRegexName_Button, 4, 12);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("Multi Token Words File:", 0, 14, KeyEvent.VK_M,
      MultiTokenName_Button.getDefaultButton(), true,
    "Task input: This file contains known multi-token words.");
    Parameter_Panel.addComponent(MultiTokenName_Text, 2, 14);
    Parameter_Panel.addKButtonPanel(MultiTokenName_Button, 4, 14);

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