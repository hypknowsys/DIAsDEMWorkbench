/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.client.gui.solutions.batch.executeBatchScript;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class ExecuteBatchScriptParameterPanel extends WumParameterPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private KTextField BatchScriptFileName_Text = null;
  private KButtonPanel BatchScriptFileName_Button = null;
  private KCheckBox UnclosedProjectRemainsOpen_CheckBox = null;
   
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

  public ExecuteBatchScriptParameterPanel() {
  
    super();

  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public ExecuteBatchScriptParameterPanel(Server pWumServer, 
  Project pWumProject, GuiClient pWumGui, 
  GuiClientPreferences pWumGuiPreferences) {
  
    super();
    
    this.setContext(pWumServer, pWumProject, pWumGui,
    pWumGuiPreferences);
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
 
    if (ActionCommand.equals("BatchScriptFileName_Button")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      BatchScriptFileName_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Batch Script File",
      WUMproject.SCRIPT_FILE_FILTER, false, true);
      
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
    
    return "Execute Batch Script";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogXsSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    ExecuteBatchScriptParameter parameter = new ExecuteBatchScriptParameter(
    BatchScriptFileName_Text.getText());
    if (UnclosedProjectRemainsOpen_CheckBox.isSelected()) {
      parameter.setUnclosedProjectRemainsOpen(true);
    }
    else {
      parameter.setUnclosedProjectRemainsOpen(false);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
   
    ExecuteBatchScriptParameter parameter = null;
    if (pTaskParameter instanceof ExecuteBatchScriptParameter) {
      parameter = (ExecuteBatchScriptParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    BatchScriptFileName_Text.setText(parameter.getBatchScriptFileName()); 
    BatchScriptFileName_Text.setCaretAtEnding();    
    UnclosedProjectRemainsOpen_CheckBox.setSelected(parameter
    .getUnclosedProjectRemainsOpen());
    
    if (Parameter_Panel != null) {
      Parameter_Panel.validate();
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {
    
    WumProject.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
    BatchScriptFileName_Text.getText());
    WumProject.quickSave();
    WumGuiPreferences.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
    BatchScriptFileName_Text.getText());
    if (UnclosedProjectRemainsOpen_CheckBox.isSelected()) {    
      WumGuiPreferences.setBooleanProperty(
      "EXECUTE_BATCH_SCRIPT:_UNCLOSED_PROJECT_REMAINS_OPEN", true);
    }
    else {
      WumGuiPreferences.setBooleanProperty(
      "EXECUTE_BATCH_SCRIPT:_UNCLOSED_PROJECT_REMAINS_OPEN", false);
    }    
    
    WumGuiPreferences.quickSave();
   
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (BatchScriptFileName_Text == null) {
      return null;
    }
    else {
      if (BatchScriptFileName_Text.getText().trim().length() == 0) {
        return BatchScriptFileName_Text;
      }
      else {
        return null;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createParameterPanel() { 
    
    BatchScriptFileName_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    BatchScriptFileName_Button.addSingleButton("...",
    KeyEvent.VK_F, true, true, "BatchScriptFileName_Button", this,
    "Click this button to select the script file.");
    
    UnclosedProjectRemainsOpen_CheckBox = new KCheckBox(
    "Unless Explicitly Closed, Batch Script Project Remains Open", false, true, 
    "UnclosedProjectRemainsOpen", this, KeyEvent.VK_I, 
    "Check this box, if the unclosed batch script project should" +
    "remain open in the GUI.");
    
    BatchScriptFileName_Text = new KTextField(WumProject
    .getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME"), 30);
    if (BatchScriptFileName_Text.getText().length() == 0) {
      BatchScriptFileName_Text.setText(WumGuiPreferences
      .getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME"));
    }
    if (WumGuiPreferences.getBooleanProperty(
    "EXECUTE_BATCH_SCRIPT:_UNCLOSED_PROJECT_REMAINS_OPEN")) {
      UnclosedProjectRemainsOpen_CheckBox.setSelected(true);
    }
    else {
      UnclosedProjectRemainsOpen_CheckBox.setSelected(false);
    }
    BatchScriptFileName_Text.setCaretAtEnding();
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(BatchScriptFileName_Text);
    
    Parameter_Panel.addLabel("Script File Name:", 0, 0, KeyEvent.VK_F, 
    BatchScriptFileName_Button.getDefaultButton(), true,
    "Required input: This WUM batch script will be executed.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(BatchScriptFileName_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(BatchScriptFileName_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Advanced Options:", 0, 2);
    Parameter_Panel.addComponent(UnclosedProjectRemainsOpen_CheckBox, 
      2, 2, new Insets(0, 0, 0, 0), 3, 1);

    this.addNorth(Parameter_Panel);
    
  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}