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

package org.hypknowsys.diasdem.client.gui.solutions.batch.executeBatchScript;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.diasdem.core.DIAsDEMproject;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class ExecuteBatchScriptParameterPanel extends DiasdemParameterPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private KTextField BatchScriptFileName_Text = null;
  private KButtonPanel BatchScriptFileName_Button = null;
   
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

  public ExecuteBatchScriptParameterPanel(Server pDiasdemServer, 
  Project pDiasdemProject, GuiClient pDiasdemGui, 
  GuiClientPreferences pDiasdemGuiPreferences) {
  
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
 
    if (ActionCommand.equals("BatchScriptFileName_Button")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      BatchScriptFileName_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Batch Script File",
      DIAsDEMproject.SCRIPT_FILE_FILTER, false, true);
      
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
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogXsSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    ExecuteBatchScriptParameter parameter = new ExecuteBatchScriptParameter(
    BatchScriptFileName_Text.getText());
    
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
    
    if (Parameter_Panel != null) {
      Parameter_Panel.validate();
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {
    
    DiasdemProject.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
    BatchScriptFileName_Text.getText());
    DiasdemProject.quickSave();
    DiasdemGuiPreferences.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
    BatchScriptFileName_Text.getText());
    DiasdemGuiPreferences.quickSave();
   
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
    
    BatchScriptFileName_Text = new KTextField(DiasdemProject
    .getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME"), 30);
    if (BatchScriptFileName_Text.getText().length() == 0) {
      BatchScriptFileName_Text.setText(DiasdemGuiPreferences
      .getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME"));
    }
    BatchScriptFileName_Text.setCaretAtEnding();
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(BatchScriptFileName_Text);
    
    Parameter_Panel.addLabel("Script File Name:", 0, 0, KeyEvent.VK_F, 
    BatchScriptFileName_Button.getDefaultButton(), true,
    "Required input: This DIAsDEM batch script will be executed.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(BatchScriptFileName_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(BatchScriptFileName_Button, 4, 0);

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