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

package org.hypknowsys.diasdem.tasks.prepare.convertTextUnits;

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

public class ConvertTextUnitsParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ConvertTextUnitsParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KComboBox ConversionType_Combo = null;
  private KTextField RegularExpression_Text = null;
  private KTextField ReplacementString_Text = null;
  private KTextField MultiTokenName_Text = null;
  private KButtonPanel MultiTokenName_Button = null;
  private KTextField TokenReplacementName_Text = null;
  private KButtonPanel TokenReplacementName_Button = null;
  
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
  
  public ConvertTextUnitsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertTextUnitsParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if (ActionCommand.equals("CollectionFileName_Button")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("ConversionTypeCombo")) {

      this.setComponentStatus();
         
    } else if (ActionCommand.equals("MultiTokenNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      MultiTokenName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Multi-Token Words File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("TokenReplacementNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      TokenReplacementName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Token Replacement File",
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
    
    return "Convert Text Units";
    
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
    
    ConvertTextUnitsParameter parameter = new ConvertTextUnitsParameter(
    Collection_Text.getText(), ConversionType_Combo.getSelectedString(), 
    RegularExpression_Text.getText(), ReplacementString_Text.getText(),
    MultiTokenName_Text.getText(), TokenReplacementName_Text.getText());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ConvertTextUnitsParameter) {
      CastParameter = (ConvertTextUnitsParameter)pTaskParameter;
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
    if (MultiTokenName_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_MULTI_TOKEN_WORDS_FILE",
      MultiTokenName_Text.getText());
    }
    if (TokenReplacementName_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_TOKEN_REPLACEMENT_FILE",
      TokenReplacementName_Text.getText());
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
    
    ConversionType_Combo = new KComboBox(ConvertTextUnitsParameter
    .CONVERSION_TYPES.length, true, "ConversionTypeCombo", this);
    for (int i = 0; i < ConvertTextUnitsParameter.CONVERSION_TYPES.length; i++)
      ConversionType_Combo.addItem(ConvertTextUnitsParameter
      .CONVERSION_TYPES[i], false);

    RegularExpression_Text = new KTextField("", 30, false);
    ReplacementString_Text = new KTextField("", 30, false);
    MultiTokenName_Text = new KTextField("", 30, false);
    TokenReplacementName_Text = new KTextField("", 30, false);

    MultiTokenName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    MultiTokenName_Button.addSingleButton("...", 
      KeyEvent.VK_M, false, true, "MultiTokenNameButton", this,
    "Click this button to select the multi-token words file.");    
    
    TokenReplacementName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TokenReplacementName_Button.addSingleButton("...", 
      KeyEvent.VK_R, false, true, "TokenReplacementNameButton", this,
    "Click this button to select the token replacement file.");    
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      ConversionType_Combo.setSelectedIndex(CastParameter.getConversionType());
      this.setComponentStatus();
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      if (DiasdemProject.getIntProperty("DEFAULT_CONVERSION_TYPE_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_CONVERSION_TYPE_INDEX")
      < ConvertTextUnitsParameter.CONVERSION_TYPES.length) {
        ConversionType_Combo.setSelectedIndex(DiasdemProject
        .getIntProperty("DEFAULT_CONVERSION_TYPE_INDEX"));
      }
      else {
        ConversionType_Combo.setSelectedIndex(ConvertTextUnitsParameter
        .CONVERT_TEXT_UNITS_TO_LOWER_CASE);
      }
      this.setComponentStatus();
    }
    Collection_Text.setCaretAtEnding();    
    RegularExpression_Text.setCaretAtEnding();
    ReplacementString_Text.setCaretAtEnding();
    MultiTokenName_Text.setCaretAtEnding();
    TokenReplacementName_Text.setCaretAtEnding();
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "CollectionFileName_Button", this,
    "Click this button to select the collection file.");
    
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
    Parameter_Panel.addLabel("Conversion Type:", 0, 2, KeyEvent.VK_T,
    ConversionType_Combo, true,
    "Task input: Choose the type of conversion to be carried out.");
    Parameter_Panel.addComponent(ConversionType_Combo, 2, 2, 
    new Insets(0, 0, 0, 0), 3, 1);

    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Regular Expression:", 0, 4, KeyEvent.VK_R,
    RegularExpression_Text, true, "Optional task input: Input the regular "
    + "expression that must match processed text units.");
    Parameter_Panel.addComponent(RegularExpression_Text, 2, 4, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Replacement String:", 0, 6, KeyEvent.VK_S,
    ReplacementString_Text, true,
    "Optional task input: Input the regex replacement string.");
    Parameter_Panel.addComponent(ReplacementString_Text, 2, 6, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Multi-Token Words File:", 0, 8, KeyEvent.VK_M,
    MultiTokenName_Button.getDefaultButton(), true,
    "Optional task input: This file contains multi-token words comprising blank spaces.");
    Parameter_Panel.addComponent(MultiTokenName_Text, 2, 8);
    Parameter_Panel.addKButtonPanel(MultiTokenName_Button, 4, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Token Replacement File:", 0, 10, KeyEvent.VK_R,
    TokenReplacementName_Button.getDefaultButton(), true,
    "Optional task input: This file contains tokens to be replaced by other tokens.");
    Parameter_Panel.addComponent(TokenReplacementName_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(TokenReplacementName_Button, 4, 10);
    
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
    
    if (ConversionType_Combo != null) {
      
      if (ConversionType_Combo.getSelectedIndex()
      == ConvertTextUnitsParameter.APPLY_REGULAR_EXPRESSION_TO_TEXT_UNITS) {
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
      else {
        if (RegularExpression_Text != null) {
          RegularExpression_Text.setText("");
          RegularExpression_Text.setEnabled(false);
        }
        if (ReplacementString_Text != null) {
          ReplacementString_Text.setText("");
          ReplacementString_Text.setEnabled(false);
        }
      }
      
      if (ConversionType_Combo.getSelectedIndex()
      == ConvertTextUnitsParameter.IDENTIFY_SPECIFIED_MULTI_TOKEN_TERMS) {
        if (CastParameter != null) {
          if (MultiTokenName_Text != null) {
            MultiTokenName_Text.setText(CastParameter.getMultiTokenFileName());
            MultiTokenName_Text.setEnabled(true);
          }
          if (MultiTokenName_Button != null) {
            MultiTokenName_Button.setAllEnabled(true);
          }
        }
        else {
          if (MultiTokenName_Text != null) {
            MultiTokenName_Text.setText(DiasdemProject.getProperty(
            "DEFAULT_MULTI_TOKEN_WORDS_FILE"));
            MultiTokenName_Text.setEnabled(true);
          }
          if (MultiTokenName_Button != null) {
            MultiTokenName_Button.setAllEnabled(true);
          }
        }
      }
      else {
        if (MultiTokenName_Text != null) {
          MultiTokenName_Text.setText("");
          MultiTokenName_Text.setEnabled(false);
        }
        if (MultiTokenName_Button != null) {
          MultiTokenName_Button.setAllEnabled(false);
        }
      }
      
      if (ConversionType_Combo.getSelectedIndex()
      == ConvertTextUnitsParameter.FIND_AND_REPLACE_SPECIFIED_TOKENS) {
        if (CastParameter != null) {
          if (TokenReplacementName_Text != null) {
            TokenReplacementName_Text.setText(CastParameter
            .getTokenReplacementFileName());
            TokenReplacementName_Text.setEnabled(true);
          }
          if (TokenReplacementName_Button != null) {
            TokenReplacementName_Button.setAllEnabled(true);
          }
        }
        else {
          if (TokenReplacementName_Text != null) {
            TokenReplacementName_Text.setText(DiasdemProject.getProperty(
            "DEFAULT_TOKEN_REPLACEMENT_FILE"));
            TokenReplacementName_Text.setEnabled(true);
          }
          if (TokenReplacementName_Button != null) {
            TokenReplacementName_Button.setAllEnabled(true);
          }
        }
      }
      else {
        if (TokenReplacementName_Text != null) {
          TokenReplacementName_Text.setText("");
          TokenReplacementName_Text.setEnabled(false);
        }
        if (TokenReplacementName_Button != null) {
          TokenReplacementName_Button.setAllEnabled(false);
        }
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