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

package org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile;

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

public class ConvertParameterTextFileParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ConvertParameterTextFileParameter CastParameter = null;
  
  private KTextField SourceFileName_Text = null;
  private KButtonPanel SourceFileName_Button = null;
  private KTextField TargetFileName_Text = null;
  private KButtonPanel TargetFileName_Button = null;
  private KComboBox ConversionType_Combo = null;
  private KTextField RegularExpression_Text = null;
  private KTextField ReplacementString_Text = null;
  
  private boolean ConversionType_FirstAction = true;
  
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
  
  public ConvertParameterTextFileParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertParameterTextFileParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if (ActionCommand.equals("SourceFileNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(SourceFileName_Text, 
      CurrentParameterDirectory, "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S, 
      null, "Select Existing Source Parameter File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("TargetFileNameButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(TargetFileName_Text, 
      CurrentParameterDirectory, "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S, 
      null, "Select Target Parameter File to be Created",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("ConversionTypeCombo")) {

      this.setComponentStatus();
         
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
    
    return "Convert Parameter Text File";
    
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
    
    ConvertParameterTextFileParameter parameter = new ConvertParameterTextFileParameter(
    SourceFileName_Text.getText(), TargetFileName_Text.getText(),
    ConversionType_Combo.getSelectedString(), RegularExpression_Text.getText(), 
    ReplacementString_Text.getText());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ConvertParameterTextFileParameter) {
      CastParameter = (ConvertParameterTextFileParameter)pTaskParameter;
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
    "CONVERT_PARAMETER_FILE:_MRU_SOURCE_FILE_NAME",
    SourceFileName_Text.getText());
    DiasdemProject.setProperty(
    "CONVERT_PARAMETER_FILE:_MRU_TARGET_FILE_NAME",
    TargetFileName_Text.getText());
    DiasdemProject.setIntProperty("DEFAULT_CONVERSION_TYPE_INDEX",
    ConversionType_Combo.getSelectedIndex());
    if (RegularExpression_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_CONVERSION_REGULAR_EXPRESSION",
      RegularExpression_Text.getText());
    }
    if (ReplacementString_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_CONVERSION_REPLACEMENT_STRING",
      ReplacementString_Text.getText());
    }
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (SourceFileName_Text != null) {
      return SourceFileName_Text;
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
    
    ConversionType_Combo = new KComboBox(ConvertParameterTextFileParameter
    .CONVERSION_TYPES.length, true, "ConversionTypeCombo", this);
    for (int i = 0; i < ConvertParameterTextFileParameter.CONVERSION_TYPES.length; i++)
      ConversionType_Combo.addItem(ConvertParameterTextFileParameter
      .CONVERSION_TYPES[i], false);

    RegularExpression_Text = new KTextField("", 30, false);
    ReplacementString_Text = new KTextField("", 30, false);
    if (CastParameter != null) {
      SourceFileName_Text = new KTextField(CastParameter
      .getSourceParameterFileName(), 30);
      TargetFileName_Text = new KTextField(CastParameter
      .getTargetParameterFileName(), 30);
      ConversionType_Combo.setSelectedIndex(CastParameter.getConversionType());
      this.setComponentStatus();
    }
    else {
      SourceFileName_Text = new KTextField(DiasdemProject.getProperty(
      "CONVERT_PARAMETER_FILE:_MRU_SOURCE_FILE_NAME"), 30);
      TargetFileName_Text = new KTextField(DiasdemProject.getProperty(
      "CONVERT_PARAMETER_FILE:_MRU_TARGET_FILE_NAME"), 30);
      if (DiasdemProject.getIntProperty("DEFAULT_CONVERSION_TYPE_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_CONVERSION_TYPE_INDEX")
      < ConvertParameterTextFileParameter.CONVERSION_TYPES.length) {
        ConversionType_Combo.setSelectedIndex(DiasdemProject
        .getIntProperty("DEFAULT_CONVERSION_TYPE_INDEX"));
      }
      else {
        ConversionType_Combo.setSelectedIndex(ConvertParameterTextFileParameter
        .CONVERT_PARAMETER_FILE_LINES_TO_LOWER_CASE);
      }
      this.setComponentStatus();
    }
    SourceFileName_Text.setCaretAtEnding();
    TargetFileName_Text.setCaretAtEnding();
    RegularExpression_Text.setCaretAtEnding();
    ReplacementString_Text.setCaretAtEnding();
    
    SourceFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    SourceFileName_Button.addSingleButton("...", 
      KeyEvent.VK_S, true, true, "SourceFileNameButton", this,
    "Click this button to select the source parameter file.");
    
    TargetFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TargetFileName_Button.addSingleButton("...", 
      KeyEvent.VK_G, true, true, "TargetFileNameButton", this,
    "Click this button to select the target parameter file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(SourceFileName_Text);

    Parameter_Panel.addLabel("Source Parameter File:", 0, 0, KeyEvent.VK_S,
      SourceFileName_Button.getDefaultButton(), true,
    "Task input: Select the source parameter file.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(SourceFileName_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(SourceFileName_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Target Parameter File:", 0, 2, KeyEvent.VK_G,
    TargetFileName_Button, true,
    "Task output: Select the parameter file for storing the results.");
    Parameter_Panel.addComponent(TargetFileName_Text, 2, 2);
   Parameter_Panel.addKButtonPanel(TargetFileName_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Conversion Type:", 0, 4, KeyEvent.VK_T,
    ConversionType_Combo, true,
    "Task input: Choose the type of conversion to be carried out.");
    Parameter_Panel.addComponent(ConversionType_Combo, 2, 4, 
    new Insets(0, 0, 0, 0), 3, 1);

    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Regular Expression:", 0, 6, KeyEvent.VK_R,
    RegularExpression_Text, true, "Optional task input: Input the regular "
    + "expression that must match processed text units.");
    Parameter_Panel.addComponent(RegularExpression_Text, 2, 6, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Replacement String:", 0, 8, KeyEvent.VK_P,
    ReplacementString_Text, true,
    "Optional task input: Input the regex replacement string.");
    Parameter_Panel.addComponent(ReplacementString_Text, 2, 8, 
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
  
  private void setComponentStatus() {
    
    if (ConversionType_FirstAction) {
      ConversionType_FirstAction = false;
      return;
    }
    
    if (ConversionType_Combo != null && ConversionType_Combo.getSelectedIndex()
    == ConvertParameterTextFileParameter.APPLY_REGULAR_EXPRESSION_TO_PARAMETER_FILE_LINES) {
      if (CastParameter != null) {
        if (RegularExpression_Text != null) {
          RegularExpression_Text.setText(CastParameter.getRegularExpression());
          RegularExpression_Text.setEnabled(true);
        }
        if (ReplacementString_Text != null) {
          ReplacementString_Text.setText(CastParameter.getReplacementString());
          ReplacementString_Text.setEnabled(true);
        }
      }
      else {
        if (RegularExpression_Text != null) {
          RegularExpression_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_CONVERSION_REGULAR_EXPRESSION"));
          RegularExpression_Text.setEnabled(true);
        }
        if (ReplacementString_Text != null) {
          ReplacementString_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_CONVERSION_REPLACEMENT_STRING"));
          ReplacementString_Text.setEnabled(true);
        }
      }
    }
    else if (ConversionType_Combo != null && ConversionType_Combo.getSelectedIndex()
    != ConvertParameterTextFileParameter.APPLY_REGULAR_EXPRESSION_TO_PARAMETER_FILE_LINES) {
      if (RegularExpression_Text != null) {
        RegularExpression_Text.setText("");
        RegularExpression_Text.setEnabled(false);
      }
      if (ReplacementString_Text != null) {
        ReplacementString_Text.setText("");
        ReplacementString_Text.setEnabled(false);
      }
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