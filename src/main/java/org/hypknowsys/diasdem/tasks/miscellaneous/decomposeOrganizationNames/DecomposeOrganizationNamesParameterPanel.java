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

package org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames;

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
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class DecomposeOrganizationNamesParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DecomposeOrganizationNamesParameter CastParameter = null;
  
  private KTextField TokenizedOrganizationsFileName_Text = null;
  private KButtonPanel TokenizedOrganizationsFileName_Button = null;
  private KTextField SuffixesFileName_Text = null;
  private KButtonPanel SuffixesFileName_Button = null;
  private KTextField BlacklistFileName_Text = null;
  private KButtonPanel BlacklistFileName_Button = null;
  private KTextField ShortenedOrganizationsFileName_Text = null;
  private KButtonPanel ShortenedOrganizationsFileName_Button = null;
  private KCheckBox ExtractTokensOfDecomposedCompanyNames_CheckBox = null;
  
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
  
  public DecomposeOrganizationNamesParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DecomposeOrganizationNamesParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if (ActionCommand.equals("TokenizedOrganizationsButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      TokenizedOrganizationsFileName_Text, CurrentParameterDirectory, 
      "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S, 
      null, "Select Existing Tokenized Organizations File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("SuffixesButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      SuffixesFileName_Text, CurrentParameterDirectory, 
      "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S, 
      null, "Select Existing Organization Suffixes File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("BlacklistButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      BlacklistFileName_Text, CurrentParameterDirectory, 
      "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing Blacklist of Organizations File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("ShortenedOrganizationsButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      ShortenedOrganizationsFileName_Text, CurrentParameterDirectory, 
      "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select File of Shortened Organizations to be Created",
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
    
    return "Decompose Organization Names";
    
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
    
    DecomposeOrganizationNamesParameter parameter = 
    new DecomposeOrganizationNamesParameter(
    TokenizedOrganizationsFileName_Text.getText(),
    SuffixesFileName_Text.getText(),
    BlacklistFileName_Text.getText(),
    ShortenedOrganizationsFileName_Text.getText(), false);
    
    if (ExtractTokensOfDecomposedCompanyNames_CheckBox.isSelected()) {
      parameter.setExtractTokensOfDecomposedCompanyNames(true);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof DecomposeOrganizationNamesParameter) {
      CastParameter = (DecomposeOrganizationNamesParameter)pTaskParameter;
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
    
    DiasdemProject.setProperty("TOKENIZE_PARAMETER_FILE:_MRU_SOURCE_FILE_NAME",
    TokenizedOrganizationsFileName_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_ORGANIZATIONS_END_FILE",
    SuffixesFileName_Text.getText());
    DiasdemProject.setProperty("DECOMPOSE_ORG_NAMES:_MRU_BLACKLIST_FILE_NAME",
    BlacklistFileName_Text.getText());
    DiasdemProject.setProperty("DECOMPOSE_ORG_NAMES:_MRU_SHORTENED_ORG_FILE_NAME",
    ShortenedOrganizationsFileName_Text.getText());
    if (ExtractTokensOfDecomposedCompanyNames_CheckBox.isSelected()) {
      DiasdemProject.setBooleanProperty(
      "DECOMPOSE_ORG_NAMES:_MRU_EXTRACT_TOKENS", true);
    }
    else {
      DiasdemProject.setBooleanProperty(
      "DECOMPOSE_ORG_NAMES:_MRU_EXTRACT_TOKENS", false);
    }
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (TokenizedOrganizationsFileName_Text != null) {
      return TokenizedOrganizationsFileName_Text;
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
    
    ExtractTokensOfDecomposedCompanyNames_CheckBox = new KCheckBox(
    "Extract Tokens of Decomposed Company Names", true, true,
    "ComputeConditionalFrequencies", this, KeyEvent.VK_E, 
    "If this box is checked, tokens of decomposed company names "
    + "might serve as company names."); 

    if (CastParameter != null) {
      TokenizedOrganizationsFileName_Text = new KTextField(CastParameter
      .getTokenizedOrganizationsFileName(), 30);
      SuffixesFileName_Text = new KTextField(CastParameter
      .getOrganizationSuffixesFileName(), 30);
      BlacklistFileName_Text = new KTextField(CastParameter
      .getBlacklistOfOrganizationsFileName(), 30);
      ShortenedOrganizationsFileName_Text = new KTextField(CastParameter
      .getShortenedOrganizationsFileName(), 30);
      if (CastParameter.extractTokensOfDecomposedCompanyNames()) {
        ExtractTokensOfDecomposedCompanyNames_CheckBox.setSelected(true);
      }
      else {
        ExtractTokensOfDecomposedCompanyNames_CheckBox.setSelected(false);
      }
    }
    else {
      TokenizedOrganizationsFileName_Text = new KTextField(
      DiasdemProject.getProperty(
      "TOKENIZE_PARAMETER_FILE:_MRU_SOURCE_FILE_NAME"), 30);
      SuffixesFileName_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_ORGANIZATIONS_END_FILE"), 30);
      BlacklistFileName_Text = new KTextField(DiasdemProject.getProperty(
      "DECOMPOSE_ORG_NAMES:_MRU_BLACKLIST_FILE_NAME"), 30);
      ShortenedOrganizationsFileName_Text = new KTextField(
      DiasdemProject.getProperty(
      "DECOMPOSE_ORG_NAMES:_MRU_SHORTENED_ORG_FILE_NAME"), 30);
      if (DiasdemProject.getBooleanProperty(
      "DECOMPOSE_ORG_NAMES:_MRU_EXTRACT_TOKENS")) {
        ExtractTokensOfDecomposedCompanyNames_CheckBox.setSelected(true);
      }
      else {
        ExtractTokensOfDecomposedCompanyNames_CheckBox.setSelected(false);
      }
    }
    TokenizedOrganizationsFileName_Text.setCaretAtEnding();
    SuffixesFileName_Text.setCaretAtEnding();
    BlacklistFileName_Text.setCaretAtEnding();
    ShortenedOrganizationsFileName_Text.setCaretAtEnding();
    
    TokenizedOrganizationsFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TokenizedOrganizationsFileName_Button.addSingleButton("...", 
      KeyEvent.VK_T, true, true, "TokenizedOrganizationsButton", this,
    "Click this button to select the tokenized organizations file.");
    
    SuffixesFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    SuffixesFileName_Button.addSingleButton("...", 
      KeyEvent.VK_O, true, true, "SuffixesButton", this,
    "Click this button to select the organization suffixes file.");
    
    BlacklistFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    BlacklistFileName_Button.addSingleButton("...", 
      KeyEvent.VK_B, true, true, "BlacklistButton", this,
    "Click this button to select the blacklist of organizations file.");   
    
    ShortenedOrganizationsFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    ShortenedOrganizationsFileName_Button.addSingleButton("...", 
      KeyEvent.VK_S, true, true, "ShortenedOrganizationsButton", this,
    "Click this button to select the shortened organization file.");    

    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(TokenizedOrganizationsFileName_Text);

    Parameter_Panel.addLabel("Tokenized Organizations File:", 0, 0, KeyEvent.VK_T,
    TokenizedOrganizationsFileName_Button.getDefaultButton(), true,
    "Task input: Select the file containing tokenized organizations to be "
    + "decomposed.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(TokenizedOrganizationsFileName_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(TokenizedOrganizationsFileName_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Organization Suffixes File:", 0, 2, KeyEvent.VK_O,
    SuffixesFileName_Button, true, "Task input: This file contains "
    + "language-specific abbreviations of organizations.");
    Parameter_Panel.addComponent(SuffixesFileName_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(SuffixesFileName_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Blacklist of Organizations File:", 0, 4, 
    KeyEvent.VK_B, BlacklistFileName_Button.getDefaultButton(), true,
    "Task input: This file contains potential names of organizations " +
    "that are in fact no organizations.");
    Parameter_Panel.addComponent(BlacklistFileName_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(BlacklistFileName_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Shortened Organizations File:", 0, 6, 
    KeyEvent.VK_S, ShortenedOrganizationsFileName_Button.getDefaultButton(), 
    true, "Task output: This file will contain shortened names of organizations " +
    "preceding organization suffixes.");
    Parameter_Panel.addComponent(ShortenedOrganizationsFileName_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(ShortenedOrganizationsFileName_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 8);
    Parameter_Panel.addComponent(ExtractTokensOfDecomposedCompanyNames_CheckBox,
    2, 8, new Insets(0, 0, 0, 0), 3, 1);

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