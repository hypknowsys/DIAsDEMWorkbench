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

import java.lang.reflect.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.tasks.project.openProject.*;
import org.hypknowsys.wum.tasks.project.closeProject.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class ExecuteBatchScriptControlPanel extends WumActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private ExecuteBatchScriptParameter Parameter = null;
  private Script WumScript = null;
  private ScriptTask WumScriptTask = null;
  private int WumScriptTaskIndex = 0;
  private boolean ErrorsDuringScriptExecution = false;
  
  protected TaskProgress CurrentScriptTaskProgress = null;
  protected ScriptableTaskResult CurrentScriptTaskResult = null;
  
  private transient String CurrentBlockingTaskClassName = null;
  private transient String CurrentNonBlockingTaskClassName = null;
  
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

  public ExecuteBatchScriptControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public ExecuteBatchScriptControlPanel(Server pWumServer, Project pWumProject,
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
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

    if (ActionSource == GuiTimer) {
      this.timerEvent();
    } else if ( ActionCommand.equals("OK") ) {
      this.ok();
    } else if ( ActionCommand.equals("TaskFinished") ) {
      this.taskFinished();
    } else if ( ActionCommand.equals("Cancel") ) {
      WumServer.stopNonBlockingTask();
      this.setControlPanelContainerClosed(true);
    } else if (ActionCommand.equals("KInternalFrame:EscapePressed")) {
      if (CloseIfEscapeIsPressed)
        this.setControlPanelContainerClosed(true);   
    } 
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskControlPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
  
    super.initialize();
    WumGui.setGuiStatus(GuiClient.PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING);

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    Parameter_Panel = new ExecuteBatchScriptParameterPanel(WumServer, 
    WumProject, WumGui, WumGuiPreferences);
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void launchCurrentWumScriptTask() {
    
    WumScriptTaskIndex++;
    
    // script execution has terminated
    if (WumScriptTask == null) {
      WumScript.setEndTimeStamp(Tools.getSystemDate());
      if (ErrorsDuringScriptExecution == false) {
        WumScript.setStatus(Script.SCRIPT_EXECUTED_WITHOUT_ERRORS);
      }
      WumScript.addLog(WumGui.logInfoMessage(
      "Task finished: Execute Batch Script"));
      WumScript.writeXmlDocument(Parameter.getBatchScriptFileName());
      GuiInternalProgressMonitor.close();     
      GuiInternalProgressMonitor = null;
      JOptionPane.showInternalMessageDialog(WumGui.getKDesktopPane(), 
      "The batch script has been\nexecuted successfully!",
      "Execute Batch Script", JOptionPane.INFORMATION_MESSAGE);
      if (Parameter.getUnclosedProjectRemainsOpen() && WumProject != null) {
        WumGui.setProject((Project)WumProject);
        PriorWumGuiStatus = GuiClient.PROJECT_OPENED_NO_TASK_RUNNING;
        WumGui.getJFrame().setTitle(WumGuiPreferences.getProperty(
        "WUM_WORKBENCH_TITLE") + " [" + WumProject.getProjectName() + "]");
        WumGui.logInfoMessage("Unclosed batch script project " 
        + WumProject.getProjectName() + " remains open");
      }
      this.setControlPanelContainerClosed(true); 
      return;
    }
    
    Progress.update(WumScriptTaskIndex - 1, "Task "
    + WumScriptTaskIndex + "/" + WumScript.countScriptTasks() +  ": "
    + WumScript.getScriptTask(WumScriptTaskIndex - 1).getLabel());
    GuiInternalProgressMonitor.setStep( Progress.getValue() );
    GuiInternalProgressMonitor.setStepNote( Progress.getNote() );
    if (WumScriptTask.execute()) {
      WumScript.addLog(KLogPanel.getLogMessage("Starting execution of task "
      + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
      + " (" + WumScriptTask.getLabel() + ")"));
      WumScriptTask.addLog(KLogPanel.getLogMessage("Starting execution of task "
      + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
      + " (" + WumScriptTask.getLabel() + ")"));
      WumScriptTask.setStartTimeStamp(Tools.getSystemDate());
      
      CurrentScriptTaskProgress = null;
      CurrentScriptTaskResult = null;
      
      CurrentBlockingTaskClassName = null;
      CurrentNonBlockingTaskClassName = null;
      Task currentTask = null;
      
      try {
        currentTask = (Task)Class.forName(WumScriptTask.getClassName())
        .getConstructor(null).newInstance(null);
        if (currentTask instanceof BlockingTask) {
          CurrentBlockingTaskClassName = WumScriptTask.getClassName();
        }
        else {
          if (currentTask instanceof NonBlockingTask) {
            CurrentNonBlockingTaskClassName = WumScriptTask.getClassName();
          }
          else
            System.err.println(WumScriptTask.getClassName()
            + " cannot be started!");
        }
      }
      catch(Exception e2) {
        e2.printStackTrace();
      }
      
      if (CurrentBlockingTaskClassName != null) {
        GuiInternalProgressMonitor.setProgress( Progress.INDETERMINATE );
        CurrentScriptTaskResult = (ScriptableTaskResult)WumServer
        .executeBlockingTask((Project)WumProject, WumScriptTask
        .getClassName(), WumScriptTask.getParameter());
        this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
        "TaskFinished"));
      }
      else {
        if (CurrentNonBlockingTaskClassName != null) {
          WumServer.startNonBlockingTask((Project)WumProject,
          WumScriptTask.getClassName(), WumScriptTask.getParameter());
        }
      }
    }
    else {
      this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
      "TaskFinished"));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void timerEvent() {
    
    // System.out.print("."); System.out.flush();
    if ( GuiInternalProgressMonitor != null
    && GuiInternalProgressMonitor.isCanceled() ) {
      if (CurrentNonBlockingTaskClassName != null) {
        WumServer.stopNonBlockingTask();
      }
      WumScript.addLog(WumGui.logWarningMessage(
      "Task stopped: Execute Batch Script"));
      this.setControlPanelContainerClosed(true);
    }
    CurrentScriptTaskProgress = WumServer.getTaskProgress();
    if (GuiInternalProgressMonitor != null) {
      //System.out.println(":" + CurrentScriptTaskProgress.getValue());
      //System.out.flush();
      if (CurrentScriptTaskProgress.getValue() == TaskProgress.INDETERMINATE)
        GuiInternalProgressMonitor.setIndeterminate(true);
      else
        GuiInternalProgressMonitor.setProgress( CurrentScriptTaskProgress.getValue() );
      GuiInternalProgressMonitor.setNote( CurrentScriptTaskProgress.getNote() );
    }
    if (WumServer.getTaskStatus() == Task.TASK_FINISHED) {
      GuiInternalProgressMonitor.setProgress( CurrentScriptTaskProgress
      .getValue() );
      GuiInternalProgressMonitor.setNote( CurrentScriptTaskProgress
      .getNote() );
      CurrentScriptTaskResult = (ScriptableTaskResult)WumServer
      .getTaskResult();
      this.actionPerformed(new ActionEvent(this,
      ActionEvent.ACTION_PERFORMED, "TaskFinished"));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void ok() {
    
    Parameter = (ExecuteBatchScriptParameter)Parameter_Panel
    .getTaskParameter();
    AbstractValidatedTaskParameter validatedParameter =
    WumServer.validateTaskParameter((Project)WumProject,
    "org.hypknowsys.wum.client.gui.solutions.batch.executeBatchScript" +
    ".ExecuteBatchScriptTask",
    Parameter);
    
    boolean commit = this.isValidParameter(validatedParameter);
    if (commit) {
      CloseIfEscapeIsPressed = false;
      Parameter_Panel.saveCurrentParameterSettingsAsDefaults();
      if (ControlPanelContainer != null) {
        ControlPanelContainer.setVisible(false);
      }
      GuiTimer.start();
      
      //WumScript = WUMscript.getTestWumScript();
      try {
        WumScript = new DefaultWUMscript(
        Parameter.getBatchScriptFileName());
      }
      catch (WumException e2) {
        WumGui.logWarningMessage(e2.getMessage());
        JOptionPane.showMessageDialog(WumGui.getJFrame(),
        "Error: The chosen batch script\n"
        + Tools.shortenFileName(Parameter.getBatchScriptFileName(), 30) + "\n"
        + "cannot be opened. It might not\n"
        + "be a valid WUM batch script.",
        this.getPreferredTitle(), JOptionPane.ERROR_MESSAGE);
        this.setControlPanelContainerClosed(true);
        return;
      }
      WumScriptTask = (WUMscriptTask)WumScript.getFirstScriptTask();
      
      Progress = new AbstractTaskProgress(Progress.INDETERMINATE, "Initial Preparations");
      Result = new AbstractTaskResult(TaskResult.NO_RESULT, "");
      GuiInternalProgressMonitor = new KInternalProgressMonitor(
      WumGui.getJFrame(),
      "Please Wait: Executing Batch Script ...",
      "Initial Preparations", 0, 100, true, "Execute Batch Script",
      "Stop", true, WumGui.getKDesktopPane(),
      "Task 0/"+WumScript.countScriptTasks(),
      0, WumScript.countScriptTasks());
      WumScript.resetScript();
      WumScript.addLog(WumGui.logInfoMessage(
      "Task started: Execute Batch Script"));
      WumScript.setStartTimeStamp(Tools.getSystemDate());
      ErrorsDuringScriptExecution = false;
      this.launchCurrentWumScriptTask();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void taskFinished() {
    
    if (WumScriptTask.execute()) {
      WumScriptTask.setResult((ScriptableTaskResult)CurrentScriptTaskResult);
      WumScriptTask.setEndTimeStamp(Tools.getSystemDate());
      
      if (CurrentScriptTaskResult.getStatus() == TaskResult.NO_RESULT) {
        WumScriptTask.addLog(WumGui.logWarningMessage(
        CurrentScriptTaskResult.getLogMessage()));
        GuiInternalProgressMonitor.close();
        WumScriptTask.setStatus(ScriptTask.TASK_EXECUTED_WITH_ERRORS);
        ErrorsDuringScriptExecution = true;
        JOptionPane.showInternalMessageDialog(WumGui.getKDesktopPane(),
        "The batch script terminated unsuccessfully:\n"
        + CurrentScriptTaskResult.getDescription(),
        "Execute Batch Script", JOptionPane.ERROR_MESSAGE);
        WumScript.addLog(KLogPanel.getLogMessage("Execution of task "
        + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
        + " (" + WumScriptTask.getLabel() + ")"
        + " has terminated unsuccessfully"));
        WumScript.addLog(WumGui.logInfoMessage(
        "Task finished: Execute Batch Script"));
        WumScriptTask.addLog(KLogPanel.getLogMessage("Execution of task "
        + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
        + " (" + WumScriptTask.getLabel() + ")"
        + " has terminated unsuccessfully"));
        WumScript.setStatus(Script.SCRIPT_EXECUTED_WITH_ERRORS);
        WumScript.writeXmlDocument(Parameter.getBatchScriptFileName());
        this.setControlPanelContainerClosed(true);
        return;
      }
      if (CurrentScriptTaskResult.getStatus() == TaskResult.FINAL_RESULT) {
        WumScript.addLog(KLogPanel.getLogMessage("Execution of task "
        + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
        + " (" + WumScriptTask.getLabel() + ")"
        + " has terminated successfully"));
        WumScriptTask.addLog(KLogPanel.getLogMessage("Execution of task "
        + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
        + " (" + WumScriptTask.getLabel() + ")"
        + " has terminated successfully"));
        WumScriptTask.setStatus(ScriptTask.TASK_EXECUTED_WITHOUT_ERRORS);
        if (CurrentScriptTaskResult instanceof OpenProjectResult) {
          WumProject = ((OpenProjectResult)CurrentScriptTaskResult)
          .getWumProject();
        }
        if (CurrentScriptTaskResult instanceof CloseProjectResult) {
          WumProject = null;
        }
      }
    }
    WumScriptTask = (WUMscriptTask)WumScript.getNextScriptTask();
    this.launchCurrentWumScriptTask();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}