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

package org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles;

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

public class MergeParameterTextFilesParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private MergeParameterTextFilesParameter CastParameter = null;
  
  private KTextField SourceFileName1_Text = null;
  private KButtonPanel SourceFileName1_Button = null;
  private KTextField SourceFileName2_Text = null;
  private KButtonPanel SourceFileName2_Button = null;
  private KTextField SourceFileName3_Text = null;
  private KButtonPanel SourceFileName3_Button = null;
  private KTextField SourceFileName4_Text = null;
  private KButtonPanel SourceFileName4_Button = null;
  private KTextField TargetFileName_Text = null;
  private KButtonPanel TargetFileName_Button = null;
  private KComboBox MergeType_Combo = null;
  
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
  
  public MergeParameterTextFilesParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public MergeParameterTextFilesParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if (ActionCommand.equals("SourceFileName1Button")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      SourceFileName1_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Source Parameter File 1",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("SourceFileName2Button")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      SourceFileName2_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Source Parameter File 2",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("SourceFileName3Button")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      SourceFileName3_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Source Parameter File 3",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("SourceFileName4Button")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      SourceFileName4_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Source Parameter File 4",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("TargetFileNameButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      TargetFileName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Target Parameter File to be Created",
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
    
    return "Merge Parameter Text Files";
    
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
    
    MergeParameterTextFilesParameter parameter = new MergeParameterTextFilesParameter(
    SourceFileName1_Text.getText(), SourceFileName2_Text.getText(), 
    SourceFileName3_Text.getText(), SourceFileName4_Text.getText(), 
    TargetFileName_Text.getText(), MergeType_Combo.getSelectedString());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof MergeParameterTextFilesParameter) {
      CastParameter = (MergeParameterTextFilesParameter)pTaskParameter;
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
    
    DiasdemProject.setProperty(
    "MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME1",
    SourceFileName1_Text.getText());
    DiasdemProject.setProperty(
    "MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME2",
    SourceFileName2_Text.getText());
    DiasdemProject.setProperty(
    "MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME3",
    SourceFileName3_Text.getText());
    DiasdemProject.setProperty(
    "MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME4",
    SourceFileName4_Text.getText());
    DiasdemProject.setProperty(
    "MERGE_PARAMETER_FILES:_MRU_TARGET_FILE_NAME",
    TargetFileName_Text.getText());
    DiasdemProject.setIntProperty(
    "MERGE_PARAMETER_FILES:_DEFAULT_MERGE_TYPE_INDEX",
    MergeType_Combo.getSelectedIndex());
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (SourceFileName1_Text != null) {
      return SourceFileName1_Text;
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
    
    MergeType_Combo = new KComboBox(MergeParameterTextFilesParameter
    .MERGE_TYPES.length, true, "MergeTypeCombo", this);
    for (int i = 0; i < MergeParameterTextFilesParameter.MERGE_TYPES.length; i++)
      MergeType_Combo.addItem(MergeParameterTextFilesParameter
      .MERGE_TYPES[i], false);

    if (CastParameter != null) {
      SourceFileName1_Text = new KTextField(CastParameter
      .getSourceParameterFileName1(), 30);
      SourceFileName2_Text = new KTextField(CastParameter
      .getSourceParameterFileName2(), 30);
      SourceFileName3_Text = new KTextField(CastParameter
      .getSourceParameterFileName3(), 30);
      SourceFileName4_Text = new KTextField(CastParameter
      .getSourceParameterFileName4(), 30);
      TargetFileName_Text = new KTextField(CastParameter
      .getTargetParameterFileName(), 30);
      if (CastParameter.getMergeType() >= 0 && CastParameter.getMergeType()
      < MergeParameterTextFilesParameter.MERGE_TYPES.length) {
        MergeType_Combo.setSelectedIndex(CastParameter.getMergeType());
      }
      else {
        MergeType_Combo.setSelectedIndex(MergeParameterTextFilesParameter
        .CREATE_OR_REPLACE_TARGET);
      }
   }
    else {
      SourceFileName1_Text = new KTextField(DiasdemProject.getProperty(
      "MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME1"), 30);
      SourceFileName2_Text = new KTextField(DiasdemProject.getProperty(
      "MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME2"), 30);
      SourceFileName3_Text = new KTextField(DiasdemProject.getProperty(
      "MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME3"), 30);
      SourceFileName4_Text = new KTextField(DiasdemProject.getProperty(
      "MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME4"), 30);
      TargetFileName_Text = new KTextField(DiasdemProject.getProperty(
      "MERGE_PARAMETER_FILES:_MRU_TARGET_FILE_NAME"), 30);
      if (DiasdemProject.getIntProperty(
      "MERGE_PARAMETER_FILES:_DEFAULT_MERGE_TYPE_INDEX")
      >= 0 && DiasdemProject.getIntProperty(
      "MERGE_PARAMETER_FILES:_DEFAULT_MERGE_TYPE_INDEX")
      < MergeParameterTextFilesParameter.MERGE_TYPES.length) {
        MergeType_Combo.setSelectedIndex(DiasdemProject
        .getIntProperty("MERGE_PARAMETER_FILES:_DEFAULT_MERGE_TYPE_INDEX"));
      }
      else {
        MergeType_Combo.setSelectedIndex(MergeParameterTextFilesParameter
        .CREATE_OR_REPLACE_TARGET);
      }
    }
    SourceFileName1_Text.setCaretAtEnding();
    SourceFileName2_Text.setCaretAtEnding();
    SourceFileName3_Text.setCaretAtEnding();
    SourceFileName4_Text.setCaretAtEnding();
    TargetFileName_Text.setCaretAtEnding();
    
    SourceFileName1_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    SourceFileName1_Button.addSingleButton("...", 
      KeyEvent.VK_1, true, true, "SourceFileName1Button", this,
    "Click this button to select the source parameter file 1.");
    
    SourceFileName2_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    SourceFileName2_Button.addSingleButton("...", 
      KeyEvent.VK_2, true, true, "SourceFileName2Button", this,
    "Click this button to select the source parameter file 2.");

    SourceFileName3_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    SourceFileName3_Button.addSingleButton("...", 
      KeyEvent.VK_3, true, true, "SourceFileName3Button", this,
    "Click this button to select the source parameter file 3.");

    SourceFileName4_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    SourceFileName4_Button.addSingleButton("...", 
      KeyEvent.VK_4, true, true, "SourceFileName4Button", this,
    "Click this button to select the source parameter file 4.");

    TargetFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TargetFileName_Button.addSingleButton("...", 
      KeyEvent.VK_T, true, true, "TargetFileNameButton", this,
    "Click this button to select the target parameter file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(SourceFileName1_Text);

    Parameter_Panel.addLabel("Source Parameter File 1:", 0, 0, KeyEvent.VK_1,
      SourceFileName1_Button.getDefaultButton(), true,
    "Task input: Select the source parameter file 1.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(SourceFileName1_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(SourceFileName1_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Source Parameter File 2:", 0, 2, KeyEvent.VK_2,
      SourceFileName2_Button.getDefaultButton(), true,
    "Task input: Select the source parameter file 2.");
    Parameter_Panel.addComponent(SourceFileName2_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(SourceFileName2_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Source Parameter File 3:", 0, 4, KeyEvent.VK_3,
      SourceFileName3_Button.getDefaultButton(), true,
    "Optional task input: Select the source parameter file 3.");
    Parameter_Panel.addComponent(SourceFileName3_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(SourceFileName3_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Source Parameter File 4:", 0, 6, KeyEvent.VK_4,
      SourceFileName4_Button.getDefaultButton(), true,
    "Optional task input: Select the source parameter file 4.");
    Parameter_Panel.addComponent(SourceFileName4_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(SourceFileName4_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Merge Type:", 0, 8, KeyEvent.VK_M,
    MergeType_Combo, true,
    "Task input: Choose the type of file merge to be performed.");
    Parameter_Panel.addComponent(MergeType_Combo, 2, 8, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Target Parameter File:", 0, 10, KeyEvent.VK_T,
    TargetFileName_Button, true,
    "Task output: Select the parameter file for storing the results.");
    Parameter_Panel.addComponent(TargetFileName_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(TargetFileName_Button, 4, 10);
    
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