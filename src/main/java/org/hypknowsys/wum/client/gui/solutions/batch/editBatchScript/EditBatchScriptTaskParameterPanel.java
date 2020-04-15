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

package org.hypknowsys.wum.client.gui.solutions.batch.editBatchScript;

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

public class EditBatchScriptTaskParameterPanel extends WumParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ScriptTask WumScriptTask = null;
  private boolean IsEnabled = true;
  
  private KBorderPanel SettingsContainer_Panel = null;
  private KTextField ScriptLabel_Text = null;
  private KTextField Status_Text = null;
  private KTextField Label_Text = null;
  private KTextField ClassName_Text = null;
  private KButtonPanel ClassName_Button = null;
  private KCheckBox Execute_CheckBox = null;
  
  private ScriptableTaskParameter TaskParameter = null;
  private TaskParameterPanel TaskParameter_Panel = null;
  private KScrollBorderPanel TaskParameterContainer_Panel = null;
  
  private ScriptableTaskResult TaskResult = null;
  private TaskResultPanel TaskResult_Panel = null;
  private KScrollBorderPanel TaskResultContainer_Panel = null;
  
  private KBorderPanel Notes_Panel = null;
  private KScrollTextArea Notes_Text = null;
  
  private KScrollBorderPanel ExecutionContainer_Panel = null;
  private KGridBagPanel Execution_Panel = null;
  private KTextField StartTimeStamp_Text = null;
  private KTextField EndTimeStamp_Text = null;
  private KScrollTextArea Log_Text = null;
  
  private KTabbedPane Tabbed_Pane = null;
  
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
  
  public EditBatchScriptTaskParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public EditBatchScriptTaskParameterPanel(Server pWumServer,
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
    
    if ( ActionCommand.equals("ClassNameButton") ) {
      this.actionClassNameButton();
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
    
    return "Edit Batch Script Task";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogLSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    WumScriptTask.setLabel(Label_Text.getText());
    WumScriptTask.setClassName(ClassName_Text.getText());
    WumScriptTask.setNotes(Notes_Text.getText());
    if (TaskParameter_Panel != null && TaskParameter_Panel.getTaskParameter()
    instanceof ScriptableTaskParameter) {
      WumScriptTask.setParameter(
      (ScriptableTaskParameter)TaskParameter_Panel.getTaskParameter());
    }
    if (TaskResult_Panel != null && TaskResult_Panel.getTaskResult()
    instanceof ScriptableTaskResult) {
      WumScriptTask.setResult(
      (ScriptableTaskResult)TaskResult_Panel.getTaskResult());
    }
    if (Execute_CheckBox.isSelected()) {
      WumScriptTask.setExecute(true);
    }
    else {
      WumScriptTask.setExecute(false);
    }
    
    EditBatchScriptTaskParameter parameter = new EditBatchScriptTaskParameter(
    WumScriptTask);
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    EditBatchScriptTaskParameter parameter = null;
    if (pTaskParameter instanceof EditBatchScriptTaskParameter) {
      parameter = (EditBatchScriptTaskParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    WumScriptTask = parameter.getWumScriptTask();
    this.createParameterPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Label_Text != null) {
      return Label_Text;
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
    
    ClassName_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ClassName_Button.addSingleButton("...",
    KeyEvent.VK_N, false, true, "ClassNameButton", this,
    "Click this button to select a batch script task.");
    
    Execute_CheckBox = new KCheckBox(
    "Execute WUM Batch Script Task During Next Batch Run", true, true,
    "Execute", this, KeyEvent.VK_E,
    "If this box is checked, this batch script task will be executed " +
    "during the next batch run.");

    if (WumScriptTask != null) {
      ScriptLabel_Text = new KTextField(
      WumScriptTask.getTransientScriptLabel(), 30, true, false);
      ScriptLabel_Text.setCaretAtEnding();
      Status_Text = new KTextField("Task Number in Script: " +
      WumScriptTask.getTransientTaskNumber() + ", " +
      WumScriptTask.getStatusString(), 30, true, false);
      Status_Text.setCaretAtEnding();
      Label_Text = new KTextField(
      WumScriptTask.getLabel(), 30, true, true);
      Label_Text.setCaretAtEnding();
      ClassName_Text = new KTextField(
      WumScriptTask.getClassName(), 30, true, false);
      ClassName_Text.setCaretAtEnding();
      Notes_Text = new KScrollTextArea(WumScriptTask.getNotes(), true);
      StartTimeStamp_Text = new KTextField(
      WumScriptTask.getStartTimeStamp(), 30, true, false);
      StartTimeStamp_Text.setCaretAtEnding();
      EndTimeStamp_Text = new KTextField(
      WumScriptTask.getEndTimeStamp(), 30, true, false);
      EndTimeStamp_Text.setCaretAtEnding();
      Log_Text = new KScrollTextArea(WumScriptTask.getLog(), true, false);
      
      if (WumScriptTask.getParameter() != null && WumScriptTask
      .getParameter() instanceof ScriptableTaskParameter) {
        TaskParameter = (ScriptableTaskParameter)WumScriptTask.getParameter();
        TaskParameter_Panel = TaskParameter.getTaskParameterPanel(WumServer,
        WumProject, WumGui, WumGuiPreferences);
      }
      else {
        TaskParameter_Panel = null;
      }
      
      if (WumScriptTask.getResult() != null && WumScriptTask
      .getResult() instanceof ScriptableTaskResult) {
        TaskResult = (ScriptableTaskResult)WumScriptTask.getResult();
        TaskResult_Panel = TaskResult.getTaskResultPanel(WumServer,
        WumProject, WumGui, WumGuiPreferences);
      }
      else {
        TaskResult_Panel = null;
      }
      
      if (WumScriptTask.execute()) {
        Execute_CheckBox.setSelected(true);
      }
      else {
        Execute_CheckBox.setSelected(false);
      }
      
    }
    else {
      ScriptLabel_Text = new KTextField("", 30, false);
      Status_Text = new KTextField("", 30, false);
      Label_Text = new KTextField("", 30, false);
      ClassName_Text = new KTextField("", 30, false);
      Notes_Text = new KScrollTextArea("", 30, 10, false);
      StartTimeStamp_Text = new KTextField("", 30, false);
      EndTimeStamp_Text = new KTextField("", 30, false);
      Log_Text = new KScrollTextArea("", false, false);
      Execute_CheckBox.setSelected(true);
    }
    
    Parameter_Panel = new KGridBagPanel(12, 12, 11, 11);
    Parameter_Panel.startFocusForwarding(Label_Text);
    SettingsContainer_Panel = new KBorderPanel();
    SettingsContainer_Panel.startFocusForwarding(Label_Text);
    
    Parameter_Panel.addLabel("Task Label:", 0, 0, KeyEvent.VK_L,
    Label_Text, true, 
    "Optional input: Concise name of this WUM batch script task");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Label_Text, 2, 0, new Insets(0, 0, 0, 0),
    3, 1);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Class Name:", 0, 2, KeyEvent.VK_N,
    ClassName_Text);
    Parameter_Panel.addComponent(ClassName_Text, 2, 2);
    Parameter_Panel.addBlankColumn(3, 2, 12);
    Parameter_Panel.addKButtonPanel(ClassName_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Execution:", 0, 4);   
    Parameter_Panel.addComponent(Execute_CheckBox, 2, 4, new Insets(0, 0, 0, 0),
    3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Script Label:", 0, 6);
    Parameter_Panel.addComponent(ScriptLabel_Text, 2, 6, new Insets(0, 0, 0, 0),
    3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Task Status:", 0, 8);
    Parameter_Panel.addComponent(Status_Text, 2, 8, new Insets(0, 0, 0, 0),
    3, 1);
    
    SettingsContainer_Panel.addNorth(Parameter_Panel);
    
    
    TaskParameterContainer_Panel = new KScrollBorderPanel(12, 12, 11, 11,
    12, 12, 11, 11);
    if (TaskParameter_Panel != null) {
      TaskParameterContainer_Panel.addCenter(TaskParameter_Panel
      .getAsComponent());
      TaskParameterContainer_Panel.startFocusForwarding(TaskParameter_Panel
      .getInitialFocusComponent());
    }
    
    
    TaskResultContainer_Panel = new KScrollBorderPanel(12, 12, 11, 11,
    12, 12, 11, 11);
    if (TaskResult_Panel != null) {
      TaskResultContainer_Panel.addCenter(TaskResult_Panel
      .getAsComponent());
      TaskResultContainer_Panel.startFocusForwarding(TaskResult_Panel
      .getInitialFocusComponent());
    }
    
    
    Execution_Panel = new KGridBagPanel();
    Execution_Panel.startFocusForwarding(Log_Text);
    Execution_Panel.setPreferredSize(new Dimension(
    WumGuiPreferences.getIntProperty("DIALOG_S_SIZE_X"),
    WumGuiPreferences.getIntProperty("DIALOG_M_SIZE_Y")));
    ExecutionContainer_Panel = new KScrollBorderPanel(12, 12, 11, 11,
    12, 12, 11, 11);
    ExecutionContainer_Panel.startFocusForwarding(Log_Text);
    
    Execution_Panel.addLabel("Start Time Stamp:", 0, 0);
    Execution_Panel.addBlankColumn(1, 0, 12);
    Execution_Panel.addComponent(StartTimeStamp_Text, 2, 0);
    Execution_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Execution_Panel.addLabel("End Time Stamp:", 0, 2);
    Execution_Panel.addComponent(EndTimeStamp_Text, 2, 2);
    Execution_Panel.addBlankRow(0, 3, 11);
    Execution_Panel.setLabelAnchor(GridBagConstraints.NORTHWEST);
    Execution_Panel.addLabel("Log Messages:", 0, 4);
    Execution_Panel.setLabelAnchor(GridBagConstraints.WEST);
    Execution_Panel.addComponent(Log_Text, 2, 4, new Insets(0, 0, 0, 0),
    3, 1, 1.0);
    
    ExecutionContainer_Panel.addCenter(Execution_Panel);
    
    
    Notes_Panel = new KBorderPanel(12, 12, 11, 11);
    Notes_Panel.addCenter(Notes_Text);
    Notes_Panel.startFocusForwarding(Notes_Text);
    
    
    Tabbed_Pane = new KTabbedPane();
    Tabbed_Pane.addTab("1. Settings", SettingsContainer_Panel,
    KeyEvent.VK_1, true, true);
    Tabbed_Pane.addTab("2. Notes", Notes_Panel,
    KeyEvent.VK_2, true, false);
    Tabbed_Pane.addTab("3. Parameters", TaskParameterContainer_Panel,
    KeyEvent.VK_3, false, false);
    Tabbed_Pane.addTab("4. Execution", ExecutionContainer_Panel,
    KeyEvent.VK_4, true, false);
    Tabbed_Pane.addTab("5. Results", TaskResultContainer_Panel,
    KeyEvent.VK_5, false, false);
    Tabbed_Pane.startFocusForwardingToSelectedTab();
    
    
    this.removeAll();
    this.validate();
    this.addCenter(Tabbed_Pane);
    this.validate();
    this.setComponentStatus();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {
    
    if (WumScriptTask != null && (WumScriptTask.getClassName() == null
    || WumScriptTask.getClassName().length() == 0)) {
      ClassName_Button.setAllEnabled(true);
    }
    else {
      ClassName_Button.setAllEnabled(false);
    }
    if (TaskParameter_Panel != null && IsEnabled) {
      Tabbed_Pane.setEnabledAt("3. Parameters", true);
    }
    else {
      Tabbed_Pane.setEnabledAt("3. Parameters", false);
    }
    if (Log_Text != null && Log_Text.getText().length() > 0 && IsEnabled) {
      Tabbed_Pane.setEnabledAt("4. Execution", true);
    }
    else {
      Tabbed_Pane.setEnabledAt("4. Execution", false);
    }
    if (TaskResult_Panel != null && IsEnabled) {
      Tabbed_Pane.setEnabledAt("5. Results", true);
    }
    else {
      Tabbed_Pane.setEnabledAt("5. Results", false);
    }
    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void actionClassNameButton() {
    
    boolean commit = true;
    String className = null;
    
    SelectScriptableTaskParameter classParameter = new
    SelectScriptableTaskParameter();
    classParameter.setRootOfScriptableTasks(WumGui.getGuiMenuBar()
    .getRootOfScriptableTasksJTree());
    TaskParameterPanel panel = classParameter.getTaskParameterPanel(
    WumServer, WumProject, WumGui, WumGuiPreferences);
    classParameter = (SelectScriptableTaskParameter)WumParameterDialog
    .showWumParameterDialog(WumServer, WumProject, WumGui,
    WumGuiPreferences, (WumTaskParameter)classParameter, panel);
    Tools.requestFocus(this, ClassName_Button);
    
    if (classParameter == null) {
      commit = false;
    }
    else {
      className = classParameter.getSelectedClassName();
    }
    
    if (commit) {
      if (WumScriptTask != null) {
        WumScriptTask.setClassName(className);
        Task task = WumServer.instantiateTask(className, WumServer);
        TaskParameter parameter = null;
        if (task != null) {
          parameter = task.getDefaultTaskParameter(WumServer,
          WumProject);
          if (parameter instanceof ScriptableTaskParameter) {
            WumScriptTask.setParameter((ScriptableTaskParameter)parameter);
            WumScriptTask.setLabel(task.getLabel());
          }
        }
        if (parameter != null) {
          TaskParameter_Panel = parameter.getTaskParameterPanel(WumServer,
          WumProject, WumGui, WumGuiPreferences);
        }
      }
      this.createParameterPanel();
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