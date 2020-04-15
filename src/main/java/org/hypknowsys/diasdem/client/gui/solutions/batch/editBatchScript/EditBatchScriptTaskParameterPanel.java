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

package org.hypknowsys.diasdem.client.gui.solutions.batch.editBatchScript;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskParameterPanel;
import org.hypknowsys.client.gui.TaskResultPanel;
import org.hypknowsys.core.Project;
import org.hypknowsys.core.ScriptTask;
import org.hypknowsys.core.ScriptableTaskParameter;
import org.hypknowsys.core.ScriptableTaskResult;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterDialog;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.diasdem.server.DiasdemTaskParameter;
import org.hypknowsys.misc.swing.KBorderPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KCheckBox;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KScrollBorderPanel;
import org.hypknowsys.misc.swing.KScrollTextArea;
import org.hypknowsys.misc.swing.KTabbedPane;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class EditBatchScriptTaskParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ScriptTask DiasdemScriptTask = null;
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
  
  public EditBatchScriptTaskParameterPanel(Server pDiasdemServer,
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
    
    return DiasdemGuiPreferences.getDialogLSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    DiasdemScriptTask.setLabel(Label_Text.getText());
    DiasdemScriptTask.setClassName(ClassName_Text.getText());
    DiasdemScriptTask.setNotes(Notes_Text.getText());
    if (TaskParameter_Panel != null && TaskParameter_Panel.getTaskParameter()
    instanceof ScriptableTaskParameter) {
      DiasdemScriptTask.setParameter(
      (ScriptableTaskParameter)TaskParameter_Panel.getTaskParameter());
    }
    if (TaskResult_Panel != null && TaskResult_Panel.getTaskResult()
    instanceof ScriptableTaskResult) {
      DiasdemScriptTask.setResult(
      (ScriptableTaskResult)TaskResult_Panel.getTaskResult());
    }
    if (Execute_CheckBox.isSelected()) {
      DiasdemScriptTask.setExecute(true);
    }
    else {
      DiasdemScriptTask.setExecute(false);
    }
    
    EditBatchScriptTaskParameter parameter = new EditBatchScriptTaskParameter(
    DiasdemScriptTask);
    
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
    
    DiasdemScriptTask = parameter.getDiasdemScriptTask();
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
    "Execute DIAsDEM Batch Script Task During Next Batch Run", true, true,
    "Execute", this, KeyEvent.VK_E,
    "If this box is checked, this batch script task will be executed " +
    "during the next batch run.");

    if (DiasdemScriptTask != null) {
      ScriptLabel_Text = new KTextField(
      DiasdemScriptTask.getTransientScriptLabel(), 30, true, false);
      ScriptLabel_Text.setCaretAtEnding();
      Status_Text = new KTextField("Task Number in Script: " +
      DiasdemScriptTask.getTransientTaskNumber() + ", " +
      DiasdemScriptTask.getStatusString(), 30, true, false);
      Status_Text.setCaretAtEnding();
      Label_Text = new KTextField(
      DiasdemScriptTask.getLabel(), 30, true, true);
      Label_Text.setCaretAtEnding();
      ClassName_Text = new KTextField(
      DiasdemScriptTask.getClassName(), 30, true, false);
      ClassName_Text.setCaretAtEnding();
      Notes_Text = new KScrollTextArea(DiasdemScriptTask.getNotes(), true);
      StartTimeStamp_Text = new KTextField(
      DiasdemScriptTask.getStartTimeStamp(), 30, true, false);
      StartTimeStamp_Text.setCaretAtEnding();
      EndTimeStamp_Text = new KTextField(
      DiasdemScriptTask.getEndTimeStamp(), 30, true, false);
      EndTimeStamp_Text.setCaretAtEnding();
      Log_Text = new KScrollTextArea(DiasdemScriptTask.getLog(), true, false);
      
      if (DiasdemScriptTask.getParameter() != null && DiasdemScriptTask
      .getParameter() instanceof ScriptableTaskParameter) {
        TaskParameter = (ScriptableTaskParameter)DiasdemScriptTask
        .getParameter();
        TaskParameter_Panel = TaskParameter.getTaskParameterPanel(DiasdemServer,
        DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
      }
      else {
        TaskParameter_Panel = null;
      }
      
      if (DiasdemScriptTask.getResult() != null && DiasdemScriptTask
      .getResult() instanceof ScriptableTaskResult) {
        TaskResult = (ScriptableTaskResult)DiasdemScriptTask.getResult();
        TaskResult_Panel = TaskResult.getTaskResultPanel(DiasdemServer,
        DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
      }
      else {
        TaskResult_Panel = null;
      }
      
      if (DiasdemScriptTask.execute()) {
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
    "Optional input: Concise name of this DIAsDEM batch script task");
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
    DiasdemGuiPreferences.getIntProperty("DIALOG_S_SIZE_X"),
    DiasdemGuiPreferences.getIntProperty("DIALOG_M_SIZE_Y")));
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
    
    if (DiasdemScriptTask != null && (DiasdemScriptTask.getClassName() == null
    || DiasdemScriptTask.getClassName().length() == 0)) {
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
    classParameter.setRootOfScriptableTasks(DiasdemGui.getGuiMenuBar()
    .getRootOfScriptableTasksJTree());
    TaskParameterPanel panel = classParameter.getTaskParameterPanel(
    DiasdemServer, DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
    classParameter = (SelectScriptableTaskParameter)DiasdemParameterDialog
    .showDiasdemParameterDialog(DiasdemServer, DiasdemProject, DiasdemGui,
    DiasdemGuiPreferences, (DiasdemTaskParameter)classParameter, panel);
    Tools.requestFocus(this, ClassName_Button);
    
    if (classParameter == null) {
      commit = false;
    }
    else {
      className = classParameter.getSelectedClassName();
    }
    
    if (commit) {
      if (DiasdemScriptTask != null) {
        DiasdemScriptTask.setClassName(className);
        Task task = DiasdemServer.instantiateTask(className, DiasdemServer);
        TaskParameter parameter = null;
        if (task != null) {
          parameter = task.getDefaultTaskParameter(DiasdemServer,
          DiasdemProject);
          if (parameter instanceof ScriptableTaskParameter) {
            DiasdemScriptTask.setParameter((ScriptableTaskParameter)parameter);
            DiasdemScriptTask.setLabel(task.getLabel());
          }
        }
        if (parameter != null) {
          TaskParameter_Panel = parameter.getTaskParameterPanel(DiasdemServer,
          DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
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