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

package org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter;

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
 * @version 2.1.0.2, 18 October 2003
 * @author Karsten Winkler
 */

public class FindCollocationsFrequencyPosFilterParameterPanel 
extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private FindCollocationsFrequencyPosFilterParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField MinTokensInCollocation_Text = null;
  private KTextField MaxTokensInCollocation_Text = null;
  private KTextField MinCollocationFrequency_Text = null;
  private KTextField CollocationPosFilterRegex_Text = null;
  private KTextField CollocationsFileName_Text = null;
  private KButtonPanel CollocationsFileName_Button = null;
  private KTextField TreeTaggerInput_Text = null;
  private KButtonPanel TreeTaggerInput_Button = null;
  private KTextField TreeTaggerOutput_Text = null;
  private KButtonPanel TreeTaggerOutput_Button = null;
  private KTextField TreeTaggerCommand_Text = null;
  private KCheckBox ExportCollocationsInTxt_CheckBox = null;
  private KCheckBox ExportCollocationsInHtml_CheckBox = null;
  
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
  
  public FindCollocationsFrequencyPosFilterParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public FindCollocationsFrequencyPosFilterParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    } else if (ActionCommand.equals("CollocationsFileNameButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      CollocationsFileName_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Collocations File to be Created",
      DIAsDEMguiPreferences.TF_STATISTICS_FILE_FILTER, false, true);
    
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
    
    return "Find Collocations: Freq./POS-Filter";
    
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
    
    FindCollocationsFrequencyPosFilterParameter parameter = new 
    FindCollocationsFrequencyPosFilterParameter(
    Collection_Text.getText(),
    Tools.string2Int(MinTokensInCollocation_Text.getText()),
    Tools.string2Int(MaxTokensInCollocation_Text.getText()),
    Tools.string2Int(MinCollocationFrequency_Text.getText()),
    CollocationPosFilterRegex_Text.getText(),
    CollocationsFileName_Text.getText(),
    TreeTaggerInput_Text.getText(),
    TreeTaggerOutput_Text.getText(),
    TreeTaggerCommand_Text.getText(), false, false);
    if (ExportCollocationsInTxt_CheckBox.isSelected()) {
      parameter.setExportCollocationsInTxtFormat(true);
    }
    if (ExportCollocationsInHtml_CheckBox.isSelected()) {
      parameter.setExportCollocationsInHtmlFormat(true);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof FindCollocationsFrequencyPosFilterParameter) {
      CastParameter = (FindCollocationsFrequencyPosFilterParameter)pTaskParameter;
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
    DiasdemProject.setProperty("MIN_TOKENS_IN_COLLOCATION",
    MinTokensInCollocation_Text.getText());
    DiasdemProject.setProperty("MAX_TOKENS_IN_COLLOCATION",
    MaxTokensInCollocation_Text.getText());
    DiasdemProject.setProperty("MIN_COLLOCATION_FREQUENCY",
    MinCollocationFrequency_Text.getText());
    DiasdemProject.setProperty("COLLOCATION_POS_FILTER_REGEX",
    CollocationPosFilterRegex_Text.getText());
    DiasdemProject.setProperty("COLLOCATIONS_FILE_NAME",
    CollocationsFileName_Text.getText());
    if (TreeTaggerInput_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_TREETAGGER_INPUT_FILE",
      TreeTaggerInput_Text.getText());
    }
    if (TreeTaggerOutput_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_TREETAGGER_OUTPUT_FILE",
      TreeTaggerOutput_Text.getText());
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
    
    ExportCollocationsInTxt_CheckBox = new KCheckBox(
    "Export Frequent Collocations in TXT Format", false, true,
    "ExportCollocationsInTxt", this, KeyEvent.VK_X, 
    "If this box is checked, collocations will be saved as a multi-token word file."); 
    ExportCollocationsInHtml_CheckBox = new KCheckBox(
    "Export Frequent Collocations in HTML Format", false, true,
    "ExportCollocationsInHtml", this, KeyEvent.VK_H, 
    "If this box is checked, collocations will be exported as an HTML file."); 

    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      MinTokensInCollocation_Text = new KTextField(Tools.int2String(
      CastParameter.getMinTokensInCollocation()), 30);
      MaxTokensInCollocation_Text = new KTextField(Tools.int2String(
      CastParameter.getMaxTokensInCollocation()), 30);
      MinCollocationFrequency_Text = new KTextField(Tools.int2String(
      CastParameter.getMinCollocationFrequency()), 30);
      CollocationPosFilterRegex_Text = new KTextField(CastParameter
      .getCollocationPosFilterRegex(), 30);
      CollocationsFileName_Text = new KTextField(CastParameter
      .getCollocationsFileName(), 30);
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
      if (CastParameter.getExportCollocationsInTxtFormat()) {
        ExportCollocationsInTxt_CheckBox.setSelected(true);
      }
      else {
        ExportCollocationsInTxt_CheckBox.setSelected(false);
      }
      if (CastParameter.getExportCollocationsInHtmlFormat()) {
        ExportCollocationsInHtml_CheckBox.setSelected(true);
      }
      else {
        ExportCollocationsInHtml_CheckBox.setSelected(false);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      MinTokensInCollocation_Text = new KTextField(DiasdemProject
      .getProperty("MIN_TOKENS_IN_COLLOCATION"), 30);
      MaxTokensInCollocation_Text = new KTextField(DiasdemProject
      .getProperty("MAX_TOKENS_IN_COLLOCATION"), 30);
      MinCollocationFrequency_Text = new KTextField(DiasdemProject
      .getProperty("MIN_COLLOCATION_FREQUENCY"), 30);
      CollocationPosFilterRegex_Text = new KTextField(DiasdemProject
      .getProperty("COLLOCATION_POS_FILTER_REGEX"), 30);
      CollocationsFileName_Text = new KTextField(DiasdemProject
      .getProperty("COLLOCATIONS_FILE_NAME"), 30);
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
    }
    if (TreeTaggerCommand_Text.getText().length() == 0) {
      TreeTaggerCommand_Text.setText(DiasdemGuiPreferences.getProperty(
      "DEFAULT_TREE_TAGGER_COMMAND"));
    }
    Collection_Text.setCaretAtEnding();
    MinTokensInCollocation_Text.setCaretAtEnding();
    MaxTokensInCollocation_Text.setCaretAtEnding();
    MinCollocationFrequency_Text.setCaretAtEnding();
    CollocationPosFilterRegex_Text.setCaretAtEnding();
    CollocationsFileName_Text.setCaretAtEnding();
    TreeTaggerInput_Text.setCaretAtEnding();
    TreeTaggerOutput_Text.setCaretAtEnding();
    TreeTaggerCommand_Text.setEnabled(false);
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
     KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");    
    
    TreeTaggerInput_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TreeTaggerInput_Button.addSingleButton("...", 
      KeyEvent.VK_R, true, true, "TreeTaggerInputButton", this,
    "Click this button to select the TreeTagger input file.");  
    
    TreeTaggerOutput_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TreeTaggerOutput_Button.addSingleButton("...", 
      KeyEvent.VK_E, true, true, "TreeTaggerOutputButton", this,
    "Click this button to select the TreeTagger output file.");    
    
    CollocationsFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    CollocationsFileName_Button.addSingleButton("...", 
      KeyEvent.VK_L, true, true, "CollocationsFileNameButton", this,
    "Click this button to select the collocations file.");   
    
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
    Parameter_Panel.addLabel("Min. Tokens in Collocation:", 0, 2, KeyEvent.VK_I,
    MinTokensInCollocation_Text, true, "Task input: Select the minimum number" +
    " (0, 1, ...) of tokens in collocations.");
    Parameter_Panel.addComponent(MinTokensInCollocation_Text, 2, 2, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Max. Tokens in Collocation:", 0, 4, KeyEvent.VK_A,
    MaxTokensInCollocation_Text, true, "Task input: Select the maximum number" +
    " (1, 2,...) of tokens in collocations.");
    Parameter_Panel.addComponent(MaxTokensInCollocation_Text, 2, 4, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Min. Collocation Frequency:", 0, 6, KeyEvent.VK_F,
    MinCollocationFrequency_Text, true, "Task input: Select the minimum" +
    " frequency of valid collocations.");
    Parameter_Panel.addComponent(MinCollocationFrequency_Text, 2, 6, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Collocation POS Tag Filter:", 0, 8, KeyEvent.VK_P,
    CollocationPosFilterRegex_Text, true, "Task input: Enter a valid regular" +
    " expression matching POS tags, e.g. '(NN NN|NN IN NN)'.");
    Parameter_Panel.addComponent(CollocationPosFilterRegex_Text, 2, 8, 
    new Insets(0, 0, 0, 0), 3, 1, 1.0);
    Parameter_Panel.addBlankRow(0, 9, 11);   
    Parameter_Panel.addLabel("Collocations File:", 0, 10, KeyEvent.VK_L,
    CollocationsFileName_Button.getDefaultButton(), true,
    "Task output: This file will contain all extracted collocations.");
    Parameter_Panel.addComponent(CollocationsFileName_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(CollocationsFileName_Button, 4, 10);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("TreeTagger Input File:", 0, 12, KeyEvent.VK_R,
      TreeTaggerInput_Button.getDefaultButton(), true,
    "Task output: This temporary file will contain TreeTagger input.");
    Parameter_Panel.addComponent(TreeTaggerInput_Text, 2, 12);
    Parameter_Panel.addKButtonPanel(TreeTaggerInput_Button, 4, 12);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("TreeTagger Output File:", 0, 14, KeyEvent.VK_E,
      TreeTaggerOutput_Button.getDefaultButton(), true,
    "Task output: This temporary file will contain TreeTagger output.");
    Parameter_Panel.addComponent(TreeTaggerOutput_Text, 2, 14);
    Parameter_Panel.addKButtonPanel(TreeTaggerOutput_Button, 4, 14);
    Parameter_Panel.addBlankRow(0, 15, 11);
    Parameter_Panel.addLabel("TreeTagger Command:", 0, 16);
    Parameter_Panel.addComponent( TreeTaggerCommand_Text, 2, 16, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 17, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 18);
    Parameter_Panel.addComponent(ExportCollocationsInTxt_CheckBox, 2, 18,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 19);
    Parameter_Panel.addComponent(ExportCollocationsInHtml_CheckBox, 2, 19,
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