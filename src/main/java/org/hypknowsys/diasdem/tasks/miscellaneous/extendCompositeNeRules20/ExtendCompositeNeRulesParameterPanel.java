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

package org.hypknowsys.diasdem.tasks.miscellaneous.extendCompositeNeRules20;

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

public class ExtendCompositeNeRulesParameterPanel
extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ExtendCompositeNeRulesParameter CastParameter = null;
  
  private KTextField BasicNeFile_Text = null;
  private KButtonPanel BasicNeFile_Button = null;
  private KTextField InitialCompositeNeFileName_Text = null;
  private KButtonPanel InitialCompositeNeFileName_Button = null;
  private KTextField ExtendedCompositeNeFileName_Text = null;
  private KButtonPanel ExtendedCompositeNeFileName_Button = null;
   
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
  
  public ExtendCompositeNeRulesParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ExtendCompositeNeRulesParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if (ActionCommand.equals("BasicNeFileButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      BasicNeFile_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Basic NE File",
      DIAsDEMguiPreferences.CSV_FILE_FILTER, false, true);
    
    } else if (ActionCommand.equals("InitialCompositeNeFileNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      InitialCompositeNeFileName_Text, CurrentParameterDirectory, 
      "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S, null, 
      "Select Existing Initial Composite NE File",
      DIAsDEMguiPreferences.CSV_FILE_FILTER, false, true);
    
    } else if (ActionCommand.equals("ExtendedCompositeNeFileNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      ExtendedCompositeNeFileName_Text, CurrentParameterDirectory, 
      "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S, null, 
      "Select Extended Composite NE File to be Created",
      DIAsDEMguiPreferences.CSV_FILE_FILTER, false, true);
    
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
    
    return "Extend Composite NE Rules 2.0";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogSSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    ExtendCompositeNeRulesParameter parameter = 
    new ExtendCompositeNeRulesParameter(
    BasicNeFile_Text.getText(),
    InitialCompositeNeFileName_Text.getText(),
    ExtendedCompositeNeFileName_Text.getText());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ExtendCompositeNeRulesParameter) {
      CastParameter = (ExtendCompositeNeRulesParameter)pTaskParameter;
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
    
    DiasdemProject.setProperty("NEEX20_DEFAULT_BASIC_NE_FILE",
    BasicNeFile_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_INITIAL_COMPOSITE_NE_FILE",
    InitialCompositeNeFileName_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_COMPOSITE_NE_FILE",
    ExtendedCompositeNeFileName_Text.getText());
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (BasicNeFile_Text != null) {
      return BasicNeFile_Text;
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
      BasicNeFile_Text = new KTextField(CastParameter
      .getBasicNeFileName(), 30);
      InitialCompositeNeFileName_Text = new KTextField(CastParameter
      .getInitialCompositeNeFileName(), 30);
      ExtendedCompositeNeFileName_Text = new KTextField(CastParameter
      .getExtendedCompositeNeFileName(), 30);
    }
    else {
      BasicNeFile_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_BASIC_NE_FILE"), 30);
      InitialCompositeNeFileName_Text = new KTextField(
      DiasdemProject.getProperty("NEEX20_DEFAULT_INITIAL_COMPOSITE_NE_FILE"), 30);
      ExtendedCompositeNeFileName_Text = new KTextField(
      DiasdemProject.getProperty("NEEX20_DEFAULT_COMPOSITE_NE_FILE"), 30);
    }
    BasicNeFile_Text.setCaretAtEnding();
    InitialCompositeNeFileName_Text.setCaretAtEnding();
    ExtendedCompositeNeFileName_Text.setCaretAtEnding();
    
    BasicNeFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    BasicNeFile_Button.addSingleButton("...", 
      KeyEvent.VK_B, true, true, "BasicNeFileButton", this,
    "Click this button to select the basic NE file.");   
    InitialCompositeNeFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    InitialCompositeNeFileName_Button.addSingleButton("...", 
      KeyEvent.VK_I, true, true, "InitialCompositeNeFileNameButton", this,
    "Click this button to select the initial composite NE file.");    
    ExtendedCompositeNeFileName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    ExtendedCompositeNeFileName_Button.addSingleButton("...", 
      KeyEvent.VK_E, true, true, "ExtendedCompositeNeFileNameButton", this,
    "Click this button to select the extended composite NE file.");    

    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(BasicNeFile_Text);

    Parameter_Panel.addLabel("Basic NE File:", 0, 0, KeyEvent.VK_B,
      BasicNeFile_Button.getDefaultButton(), true,
    "Task input: This file contains a fixed list of basic NEs such as Surname.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent( BasicNeFile_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel( BasicNeFile_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Initial Composite NE File:", 0, 2, KeyEvent.VK_I,
      InitialCompositeNeFileName_Button.getDefaultButton(), true,
    "Task input: This file contains rules for constructing " +
    "composite NEs such as Person.");
    Parameter_Panel.addComponent( InitialCompositeNeFileName_Text, 2, 2);
    Parameter_Panel.addKButtonPanel( InitialCompositeNeFileName_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Extended Composite NE File:", 0, 4, KeyEvent.VK_E,
      ExtendedCompositeNeFileName_Button.getDefaultButton(), true,
    "Task output: This file will contain extended rules for constructing " +
    "composite NEs such as Person.");
    Parameter_Panel.addComponent( ExtendedCompositeNeFileName_Text, 2, 4);
    Parameter_Panel.addKButtonPanel( ExtendedCompositeNeFileName_Button, 4, 4);

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