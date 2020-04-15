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
  
public class EditBatchScriptParameterPanel extends WumParameterPanel
implements TableModelListener, ListSelectionListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private Script WumScript = null;
  private ScriptTask WumScriptTask = null;
  private boolean IsEnabled = true;

  private KBorderPanel SettingsContainer_Panel = null;
  private KLabel Label_Label = null;
  private KTextField Label_Text = null;
  private KLabel Status_Label = null;
  private KTextField Status_Text = null;
  private KLabel FileName_Label = null;
  private KTextField FileName_Text = null;
 
  private KGridBagPanel TaskEditor_Panel = null;
  private JTable Tasks_Table = null;
  private ListSelectionModel Tasks_ListSelectionModel = null;
  private int SelectedTask = -1;
  private JScrollPane Tasks_ScrollPane = null;
  private KButtonPanel TasksButton_Panel = null;

  private KBorderPanel Notes_Panel = null;
  private KScrollTextArea Notes_Text = null;
   
  private KScrollBorderPanel ExecutionContainer_Panel = null;
  private KGridBagPanel Execution_Panel = null;  
  private KLabel StartTimeStamp_Label = null;
  private KTextField StartTimeStamp_Text = null;
  private KLabel EndTimeStamp_Label = null;
  private KTextField EndTimeStamp_Text = null;
  private KLabel Log_Label = null;
  private KScrollTextArea Log_Text = null;
   
  private KTabbedPane Tabbed_Pane = null;
  
  private Tasks_TablePopup Tasks_PopupMenu = null; 

  private ScriptTask ClipboardTask = null;

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

  public EditBatchScriptParameterPanel() {
  
    super();

  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public EditBatchScriptParameterPanel(Server pWumServer, 
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

    if ( ActionCommand.equals("Append") ) { 
      this.append();
    } else if ( ActionCommand.equals("Insert") ) { 
      this.insert();
    } else if ( ActionCommand.equals("Edit") ) { 
      this.edit();
    } else  if ( ActionCommand.equals("ResetTask") ) { 
      this.resetTask();
    } else if ( ActionCommand.equals("Delete") ) { 
      this.delete();
    } else if ( ActionCommand.equals("Cut") ) { 
      this.cut();
    } else if ( ActionCommand.equals("Copy") ) { 
      this.copy();
    } else if ( ActionCommand.equals("Paste") ) { 
      this.paste();
    } 
   
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TableModelListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void tableChanged(TableModelEvent pEvent) {
    
    int row = pEvent.getFirstRow();
    int column = pEvent.getColumn();
    String columnName = WumScript.getColumnName(column);
    Object data = WumScript.getValueAt(row, column);
   
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface ListSelectionListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void valueChanged(ListSelectionEvent pEvent) {
    
    if (pEvent.getValueIsAdjusting()) 
      return;
    ListSelectionModel model = (ListSelectionModel)pEvent.getSource();
    if (model.isSelectionEmpty()) {
      SelectedTask = -1;
      this.setComponentStatus();
    } else {
      SelectedTask = model.getMinSelectionIndex();
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
    
    return "Edit Batch Script";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogLSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    WumScript.setLabel(Label_Text.getText());
    WumScript.setNotes(Notes_Text.getText());
    
    EditBatchScriptParameter parameter = new EditBatchScriptParameter(
    WumScript);
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
   
    EditBatchScriptParameter parameter = null;
    if (pTaskParameter instanceof EditBatchScriptParameter) {
      parameter = (EditBatchScriptParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    WumScript = parameter.getWumScript(); 
    IsEnabled = true;
    SaveRequired = false;
    this.createParameterPanel();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Label_Text != null && Label_Text.isEnabled()) {
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
    
    if (WumScript != null) {
      Label_Text = new KTextField(WumScript.getLabel(), 30, true);
      Label_Text.setCaretAtEnding();
      Status_Text = new KTextField(
      WumScript.getStatusString(), 30, true, false);
      Status_Text.setCaretAtEnding();
      FileName_Text = new KTextField(
      WumScript.getTransientFileName(), 30, true, false);
      FileName_Text.setCaretAtEnding();
      Notes_Text = new KScrollTextArea(WumScript.getNotes(), true);
      StartTimeStamp_Text = new KTextField(
      WumScript.getStartTimeStamp(), 30, true, false);
      StartTimeStamp_Text.setCaretAtEnding();
      EndTimeStamp_Text = new KTextField(
      WumScript.getEndTimeStamp(), 30, true, false);
      EndTimeStamp_Text.setCaretAtEnding();
      Log_Text = new KScrollTextArea(WumScript.getLog(), true, false);
    }
    else {
      Label_Text = new KTextField("", 30, false);
      Status_Text = new KTextField("", 30, false);
      FileName_Text = new KTextField("", 30, false);
      Notes_Text = new KScrollTextArea("", 30, 10, false);
      StartTimeStamp_Text = new KTextField("", 30, false);
      EndTimeStamp_Text = new KTextField("", 30, false);
      Log_Text = new KScrollTextArea("", false, false);
    }
    
    Parameter_Panel = new KGridBagPanel(12, 12, 11, 11);
    Parameter_Panel.startFocusForwarding(Label_Text);
    SettingsContainer_Panel = new KBorderPanel();
    SettingsContainer_Panel.startFocusForwarding(Label_Text);
    
    Label_Label = Parameter_Panel.addLabel("Script Label:", 0, 0,
    KeyEvent.VK_L, Label_Text, true, 
    "Optional input: Concise name of this WUM batch script");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent( Label_Text, 2, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Status_Label = Parameter_Panel.addLabel("Script Status:", 0, 2);
    Parameter_Panel.addComponent(Status_Text, 2, 2, new Insets(0, 0, 0, 0),
    3, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    FileName_Label = Parameter_Panel.addLabel("File Name:", 0, 4);
    Parameter_Panel.addComponent(FileName_Text, 2, 4, new Insets(0, 0, 0, 0),
    3, 1);
    
    SettingsContainer_Panel.addNorth(Parameter_Panel);
    
    
    if (WumScript != null) {
      Tasks_Table = new JTable(WumScript);
      for (int i = 0; i < Tasks_Table.getColumnCount(); i++) {
        Tasks_Table.getColumnModel().getColumn(i).setPreferredWidth(
        WumScript.getPreferredColumnWidth(i) );
      }
    }
    else {
      Tasks_Table = new JTable();
    }
    Tasks_Table.setPreferredScrollableViewportSize( this.getSize() );
    Tasks_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    Tasks_Table.setRowSelectionAllowed(true);
    Tasks_Table.setColumnSelectionAllowed(false);
    Tasks_ListSelectionModel = Tasks_Table.getSelectionModel();
    Tasks_ListSelectionModel.addListSelectionListener(this);
    Tasks_ScrollPane = new JScrollPane(Tasks_Table);
    Tasks_Table.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
          actionPerformed(new ActionEvent(this,
          ActionEvent.ACTION_PERFORMED, "Edit"));
        }
      }
    });
    Tasks_PopupMenu = new Tasks_TablePopup(this);
    MouseListener popupListener = new PopupListener();
    Tasks_Table.addMouseListener(popupListener);
    Tasks_Table.getParent().addMouseListener(popupListener);
    
    TasksButton_Panel = new KButtonPanel(0, 0, 0, 0, 8,
    KButtonPanel.VERTICAL_TOP);
    TasksButton_Panel.addFirstButton("Append",
    KeyEvent.VK_P, false, false, "Append", this);
    TasksButton_Panel.addNextButton("Insert",
    KeyEvent.VK_I, false, false, "Insert", this);
    TasksButton_Panel.addNextButton("Edit",
    KeyEvent.VK_E, false, false, "Edit", this);
    TasksButton_Panel.addNextButton("Reset",
    KeyEvent.VK_T, false, false, "ResetTask", this);
    TasksButton_Panel.addLastButton("Delete",
    KeyEvent.VK_D, false, false, "Delete", this);
    TasksButton_Panel.addNextButton("Cut",
    0, false, false, "Cut", this);
    TasksButton_Panel.addNextButton("Copy",
    0, false, false, "Copy", this);
    TasksButton_Panel.addLastButton("Paste",
    0, false, false, "Paste", this);
    
    TaskEditor_Panel = new KGridBagPanel(12, 12, 11, 11);
    TaskEditor_Panel.addComponent(Tasks_ScrollPane, 0, 0,
    new Insets(0, 0, 0, 0), 1, 1, 1.0);
    TaskEditor_Panel.addBlankColumn(1, 0, 12);
    TaskEditor_Panel.addKButtonPanel(TasksButton_Panel, 2, 0,
    GridBagConstraints.NORTH);
    
    
    Execution_Panel = new KGridBagPanel();
    Execution_Panel.startFocusForwarding(Log_Text);
    Execution_Panel.setPreferredSize(new Dimension(
    WumGuiPreferences.getIntProperty("DIALOG_S_SIZE_X"),
    WumGuiPreferences.getIntProperty("DIALOG_M_SIZE_Y")));
    ExecutionContainer_Panel = new KScrollBorderPanel(12, 12, 11, 11,
    12, 12, 11, 11);
    ExecutionContainer_Panel.startFocusForwarding(Log_Text);
    
    StartTimeStamp_Label = Execution_Panel.addLabel("Start Time Stamp:", 0, 0);
    Execution_Panel.addBlankColumn(1, 0, 12);
    Execution_Panel.addComponent(StartTimeStamp_Text, 2, 0);
    Execution_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    EndTimeStamp_Label = Execution_Panel.addLabel("End Time Stamp:", 0, 2);
    Execution_Panel.addComponent(EndTimeStamp_Text, 2, 2);
    Execution_Panel.addBlankRow(0, 3, 11);
    Execution_Panel.setLabelAnchor(GridBagConstraints.NORTHWEST);
    Log_Label = Execution_Panel.addLabel("Log Messages:", 0, 4);
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
    Tabbed_Pane.addTab("3. Tasks", TaskEditor_Panel,
    KeyEvent.VK_3, true, false);
    Tabbed_Pane.addTab("4. Execution", ExecutionContainer_Panel,
    KeyEvent.VK_4, true, false);
    Tabbed_Pane.startFocusForwardingToSelectedTab();

    if (WumScript != null && WumScript.countScriptTasks() > 0) {
      SelectedTask = 0;
      Tasks_Table.setRowSelectionInterval(0, 0);
    }
    else {
      SelectedTask = -1;
    }
    
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

    boolean enableAppend = false;
    boolean enableInsert = false;
    boolean enableEdit = false;
    boolean enableReset= false;
    boolean enableDelete = false;
    boolean enableCut = false;
    boolean enableCopy = false;
    boolean enablePaste = false;
    
    if (WumScript != null && IsEnabled) {
      Tabbed_Pane.setEnabled(true);
      Label_Label.setEnabled(true);
      Label_Text.setEnabled(true);
      Status_Label.setEnabled(true);
      Status_Text.setEnabled(true);
      FileName_Label.setEnabled(true);
      FileName_Text.setEnabled(true);
      Notes_Text.setTextAreaEnabled(true);
      enableAppend = true;    
      if (WumScript.countScriptTasks() > 0 && SelectedTask >= 0) {
        enableInsert = true;
        enableEdit = true;
        enableReset = true;     
        enableDelete = true;        
        enableCut = true;        
        enableCopy = true;        
      }
      if (ClipboardTask != null) {
        enablePaste = true;        
      }
      if ((WumScript.getLog() != null 
      && WumScript.getLog().length() > 0)
      || (WumScript.getStartTimeStamp() != null
      && WumScript.getStartTimeStamp().length() > 0)
      || (WumScript.getEndTimeStamp() != null
      && WumScript.getEndTimeStamp().length() > 0)) {
        Tabbed_Pane.setEnabledAt("4. Execution", true);
        StartTimeStamp_Label.setEnabled(true);
        StartTimeStamp_Text.setEnabled(true);
        EndTimeStamp_Label.setEnabled(true);
        EndTimeStamp_Text.setEnabled(true);
        Log_Label.setEnabled(true);
        Log_Text.setEnabled(true);
      }
      else {
        Tabbed_Pane.setEnabledAt("4. Execution", false);
        StartTimeStamp_Label.setEnabled(false);
        StartTimeStamp_Text.setEnabled(false);
        EndTimeStamp_Label.setEnabled(false);
        EndTimeStamp_Text.setEnabled(false);
        Log_Label.setEnabled(false);
        Log_Text.setEnabled(false);        
      }
    }
    else {
      Tabbed_Pane.setEnabled(false);
      Label_Label.setEnabled(false);
      Label_Text.setEnabled(false);
      Status_Label.setEnabled(false);
      Status_Text.setEnabled(false);
      FileName_Label.setEnabled(false);
      FileName_Text.setEnabled(false);
      Notes_Text.setTextAreaEnabled(false);
    }
    
    TasksButton_Panel.setEnabled(0, enableAppend);
    TasksButton_Panel.setEnabled(1, enableInsert);
    TasksButton_Panel.setEnabled(2, enableEdit);
    TasksButton_Panel.setEnabled(3, enableReset);
    TasksButton_Panel.setEnabled(4, enableDelete);
    TasksButton_Panel.setEnabled(5, enableCut);
    TasksButton_Panel.setEnabled(6, enableCopy);
    TasksButton_Panel.setEnabled(7, enablePaste);
    if (Tasks_PopupMenu != null) {
      Tasks_PopupMenu.setEnabled(enableAppend, enableInsert, enableEdit,
      enableReset, enableDelete, enableCut, enableCopy, enablePaste); 
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void append() {
    
    if (SelectedTask >= 0  && SelectedTask < WumScript.countScriptTasks()) {
      int taskID = SelectedTask;
      WumScript.appendScriptTask(new DefaultWUMscriptTask(
      Tools.getSystemDate(), "", null, ""), taskID);
      SelectedTask = Math.min(taskID + 1, WumScript.countScriptTasks() - 1);
      Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
    }
    else {
      WumScript.addScriptTask(new DefaultWUMscriptTask(
      Tools.getSystemDate(), "", null, ""));     
      SelectedTask = Math.max(0, WumScript.countScriptTasks() - 1);
      Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
    }
    SaveRequired = true;
    this.setComponentStatus();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void insert() {
    
    if (SelectedTask >= 0 && SelectedTask < WumScript.countScriptTasks()) {
      int taskID = SelectedTask;
      WumScript.insertScriptTask(new DefaultWUMscriptTask(
      Tools.getSystemDate(), "", null, ""), taskID);
      SelectedTask = Math.max(0, SelectedTask - 1);
      Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
      SaveRequired = true;
      this.setComponentStatus();
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void edit() {
    
    if (SelectedTask >= 0 && SelectedTask < WumScript.countScriptTasks()) {
      int taskID = SelectedTask;
      ScriptTask task = WumScript.getScriptTask(taskID);
      task.setTransientScriptLabel(Label_Text.getText());
      task.setTransientTaskNumber(taskID + 1);
      
      EditBatchScriptTaskParameter parameter = new
      EditBatchScriptTaskParameter(task);
      TaskParameterPanel panel = parameter.getTaskParameterPanel(
      WumServer, WumProject, WumGui, WumGuiPreferences);    
      parameter = (EditBatchScriptTaskParameter)WumParameterDialog
      .showWumParameterDialog(WumServer, WumProject, WumGui, 
      WumGuiPreferences, (WumTaskParameter)parameter, panel);
      Tools.requestFocus(Tabbed_Pane, Tasks_Table);
      
      if (parameter != null) {      
        WumScript.replaceScriptTask(parameter.getWumScriptTask(),
        taskID);
        SaveRequired = true;
        this.setComponentStatus();  
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void resetTask() {
    
    if (SelectedTask >= 0 && SelectedTask < WumScript.countScriptTasks()) {
      boolean commit = true;
      int input = JOptionPane.showConfirmDialog(
      WumGui.getJFrame(), "Warning: Do you really want\n"
      + "to reset the selected task\nby deleting its results?", this.getPreferredTitle(),
      JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
      if (commit) {
        int taskID = SelectedTask;
        WumScript.resetScriptTask(taskID);
        SaveRequired = true;
        this.setComponentStatus();
      }
    }
    
  }   

  /* ########## ########## ########## ########## ########## ######### */
  
  private void delete() {
    
    if (SelectedTask >= 0 && SelectedTask < WumScript.countScriptTasks()) {
      boolean commit = true;
      int input = JOptionPane.showConfirmDialog(
      WumGui.getJFrame(), "Warning: Do you really want\n"
      + "to delete the selected task?", this.getPreferredTitle(),
      JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
      if (commit) {
        int taskID = SelectedTask;
        WumScript.deleteScriptTask(taskID);
        if (taskID < WumScript.countScriptTasks()) {
          SelectedTask = taskID;
          Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
        }
        else if (WumScript.countScriptTasks() > 0) {
          SelectedTask = taskID - 1;
          Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
        }
        else
          SelectedTask = -1;
        SaveRequired = true;
        this.setComponentStatus();
      }
    }
    
  }   

  /* ########## ########## ########## ########## ########## ######### */
  
  private void cut() {
    
    if (SelectedTask >= 0 && SelectedTask < WumScript.countScriptTasks()) {
      boolean commit = true;
      int input = JOptionPane.showConfirmDialog(
      WumGui.getJFrame(), "Warning: Do you really want\n"
      + "to delete the selected task\nand copy it to the clipboard?", 
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION, 
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
      if (commit) {
        int taskID = SelectedTask;
        if (WumScript.getScriptTask(taskID).clone() != null) {
          ClipboardTask = (ScriptTask)WumScript.getScriptTask(taskID)
          .clone();
          WumScript.deleteScriptTask(taskID);
          if (taskID < WumScript.countScriptTasks()) {
            SelectedTask = taskID;
            Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
          }
          else if (WumScript.countScriptTasks() > 0) {
            SelectedTask = taskID - 1;
            Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
          }
          else
            SelectedTask = -1;
          SaveRequired = true;
          this.setComponentStatus();
        }
        else {
          ClipboardTask = null;
        }
      }
    }
    
  }   

  /* ########## ########## ########## ########## ########## ######### */
  
  private void copy() {
    
    if (SelectedTask >= 0 && SelectedTask < WumScript.countScriptTasks()) {
      int taskID = SelectedTask;
      if (WumScript.getScriptTask(taskID).clone() != null) {
        ClipboardTask = (ScriptTask)WumScript.getScriptTask(taskID).clone();
      }
      else {
        ClipboardTask = null;
      }     
    }
    this.setComponentStatus();
    
  }   

  /* ########## ########## ########## ########## ########## ######### */
  
  private void paste() {

    if (ClipboardTask == null) {
      return;
    }
    // appends the clipboard task
    if (SelectedTask >= 0  && SelectedTask < WumScript.countScriptTasks()) {
      int taskID = SelectedTask;
      WumScript.appendScriptTask((ScriptTask)ClipboardTask.clone(), taskID);
      SelectedTask = Math.min(taskID + 1, WumScript.countScriptTasks() - 1);
      Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
    }
    else {
      WumScript.addScriptTask((ScriptTask)ClipboardTask.clone());     
      SelectedTask = Math.max(0, WumScript.countScriptTasks() - 1);
      Tasks_Table.setRowSelectionInterval(SelectedTask, SelectedTask);
    }
    SaveRequired = true;
    this.setComponentStatus();
    
  }   

  /* ########## ########## ########## ########## ########## ######### */
  
  private class PopupListener extends MouseAdapter {
    
    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }
    
    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }
    
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        if (Tasks_Table.isRowSelected(Tasks_Table.rowAtPoint(e.getPoint()))) {
          Tasks_PopupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
      }
    }
    
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private class Tasks_TablePopup extends JPopupMenu {
    
    KMenuItem AppendItem = null;
    KMenuItem InsertItem = null;
    KMenuItem EditItem = null;
    KMenuItem ResetItem = null;
    KMenuItem DeleteItem = null;
    KMenuItem CutItem = null;
    KMenuItem CopyItem = null;
    KMenuItem PasteItem = null;
    
    public Tasks_TablePopup(ActionListener pListener) {
      super();
      AppendItem = new KMenuItem("Append New Task", KeyEvent.VK_A,
      false, "Append", pListener);
      add(AppendItem);
      InsertItem = new KMenuItem("Insert New Task", KeyEvent.VK_I,
      false, "Insert", pListener);
      add(InsertItem);
      EditItem = new KMenuItem("Edit Selected Task", KeyEvent.VK_E,
      false, "Edit", pListener);
      add(EditItem);
      ResetItem = new KMenuItem("Reset Selected Task", KeyEvent.VK_T,
      false, "ResetTask", pListener);
      add(ResetItem);
      DeleteItem = new KMenuItem("Delete Selected Task", KeyEvent.VK_D,
      false, "Delete", pListener);
      add(DeleteItem);
      add(new JSeparator());
      CutItem = new KMenuItem("Cut Selected Task", 0,
      false, "Cut", pListener);
      add(CutItem);
      CopyItem = new KMenuItem("Copy Selected Task", 0,
      false, "Copy", pListener);
      add(CopyItem);
      PasteItem = new KMenuItem("Paste Clipboard Task", 0,
      false, "Paste", pListener);
      add(PasteItem);
    }
    
    public void setEnabled(boolean pAppend, boolean pInsert, boolean pEdit,
    boolean pReset, boolean pDelete, boolean pCut, boolean pCopy,
    boolean pPaste) {
      AppendItem.setEnabled(pAppend);
      InsertItem.setEnabled(pInsert);
      EditItem.setEnabled(pEdit);
      ResetItem.setEnabled(pReset);
      DeleteItem.setEnabled(pDelete);
      CutItem.setEnabled(pCut);
      CopyItem.setEnabled(pCopy);
      PasteItem.setEnabled(pPaste);
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