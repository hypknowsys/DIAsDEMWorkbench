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

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.core.Script;
import org.hypknowsys.core.ScriptTask;
import org.hypknowsys.core.ScriptableTaskResult;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.diasdem.core.DIAsDEMscriptTask;
import org.hypknowsys.diasdem.core.DiasdemException;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMscript;
import org.hypknowsys.diasdem.tasks.project.closeProject.CloseProjectResult;
import org.hypknowsys.diasdem.tasks.project.openProject.OpenProjectResult;
import org.hypknowsys.misc.swing.KInternalProgressMonitor;
import org.hypknowsys.misc.swing.KLogPanel;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.AbstractTaskProgress;
import org.hypknowsys.server.AbstractTaskResult;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.BlockingTask;
import org.hypknowsys.server.NonBlockingTask;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;
import org.hypknowsys.server.TaskProgress;
import org.hypknowsys.server.TaskResult;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class ExecuteBatchScriptControlPanel extends DiasdemActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private ExecuteBatchScriptParameter Parameter = null;
  private Script DiasdemScript = null;
  private ScriptTask DiasdemScriptTask = null;
  private int DiasdemScriptTaskIndex = 0;
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

  public ExecuteBatchScriptControlPanel(Server pDiasdemServer, 
  Project pDiasdemProject, GuiClient pDiasdemGui, 
  GuiClientPreferences pDiasdemGuiPreferences) {
  
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
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
      DiasdemServer.stopNonBlockingTask();
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
    DiasdemGui.setGuiStatus(GuiClient.PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING);

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    Parameter_Panel = new ExecuteBatchScriptParameterPanel(DiasdemServer, 
    DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void launchCurrentDiasdemScriptTask() {
    
    DiasdemScriptTaskIndex++;
    
    // script execution has terminated
    if (DiasdemScriptTask == null) {
      DiasdemScript.setEndTimeStamp(Tools.getSystemDate());
      if (ErrorsDuringScriptExecution == false) {
        DiasdemScript.setStatus(Script.SCRIPT_EXECUTED_WITHOUT_ERRORS);
      }
      DiasdemScript.addLog(DiasdemGui.logInfoMessage(
      "Task finished: Execute Batch Script"));
      DiasdemScript.writeXmlDocument(Parameter.getBatchScriptFileName());
      GuiInternalProgressMonitor.close();     
      GuiInternalProgressMonitor = null;
      JOptionPane.showInternalMessageDialog(DiasdemGui.getKDesktopPane(), 
      "The batch script has been\nexecuted successfully!",
      "Execute Batch Script", JOptionPane.INFORMATION_MESSAGE);
      this.setControlPanelContainerClosed(true); 
      return;
    }
    
    Progress.update(DiasdemScriptTaskIndex - 1, "Task "
    + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks() +  ": "
    + DiasdemScript.getScriptTask(DiasdemScriptTaskIndex - 1).getLabel());
    GuiInternalProgressMonitor.setStep( Progress.getValue() );
    GuiInternalProgressMonitor.setStepNote( Progress.getNote() );
    if (DiasdemScriptTask.execute()) {
      DiasdemScript.addLog(KLogPanel.getLogMessage("Starting execution of task "
      + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks()
      + " (" + DiasdemScriptTask.getLabel() + ")"));
      DiasdemScriptTask.addLog(KLogPanel.getLogMessage("Starting execution " +
      "of task " + DiasdemScriptTaskIndex + "/" + DiasdemScript
      .countScriptTasks() + " (" + DiasdemScriptTask.getLabel() + ")"));
      DiasdemScriptTask.setStartTimeStamp(Tools.getSystemDate());
      
      CurrentScriptTaskProgress = null;
      CurrentScriptTaskResult = null;
      
      CurrentBlockingTaskClassName = null;
      CurrentNonBlockingTaskClassName = null;
      Task currentTask = null;
      
      try {
        currentTask = (Task)Class.forName(DiasdemScriptTask.getClassName())
        .getConstructor(null).newInstance(null);
        if (DiasdemProject != null && currentTask != null) {
          // register all properties of the current task to make sure they are
          // available even if they are not stored in the project file
          DiasdemProject.registerButDontReplaceKProperties(currentTask
          .getProjectPropertyData());
        }
        if (currentTask instanceof BlockingTask) {
          CurrentBlockingTaskClassName = DiasdemScriptTask.getClassName();
        }
        else {
          if (currentTask instanceof NonBlockingTask) {
            CurrentNonBlockingTaskClassName = DiasdemScriptTask.getClassName();
          }
          else
            System.err.println(DiasdemScriptTask.getClassName()
            + " cannot be started!");
        }
      }
      catch(Exception e2) {
        e2.printStackTrace();
      }
      
      if (CurrentBlockingTaskClassName != null) {
        GuiInternalProgressMonitor.setProgress( Progress.INDETERMINATE );
        CurrentScriptTaskResult = (ScriptableTaskResult)DiasdemServer
        .executeBlockingTask((Project)DiasdemProject, DiasdemScriptTask
        .getClassName(), DiasdemScriptTask.getParameter());
        this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
        "TaskFinished"));
      }
      else {
        if (CurrentNonBlockingTaskClassName != null) {
          DiasdemServer.startNonBlockingTask((Project)DiasdemProject,
          DiasdemScriptTask.getClassName(), DiasdemScriptTask.getParameter());
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
        DiasdemServer.stopNonBlockingTask();
      }
      DiasdemScript.addLog(DiasdemGui.logWarningMessage(
      "Task stopped: Execute Batch Script"));
      this.setControlPanelContainerClosed(true);
    }
    CurrentScriptTaskProgress = DiasdemServer.getTaskProgress();
    if (GuiInternalProgressMonitor != null) {
      //System.out.println(":" + CurrentScriptTaskProgress.getValue());
      //System.out.flush();
      if (CurrentScriptTaskProgress.getValue() == TaskProgress.INDETERMINATE) {
        GuiInternalProgressMonitor.setIndeterminate(true);
      }
      else {
        GuiInternalProgressMonitor.setProgress(CurrentScriptTaskProgress
        .getValue());
      }
      GuiInternalProgressMonitor.setNote( CurrentScriptTaskProgress.getNote() );
    }
    if (DiasdemServer.getTaskStatus() == Task.TASK_FINISHED) {
      GuiInternalProgressMonitor.setProgress( CurrentScriptTaskProgress
      .getValue() );
      GuiInternalProgressMonitor.setNote( CurrentScriptTaskProgress
      .getNote() );
      CurrentScriptTaskResult = (ScriptableTaskResult)DiasdemServer
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
    DiasdemServer.validateTaskParameter((Project)DiasdemProject,
    "org.hypknowsys.diasdem.client.gui.solutions.batch.executeBatchScript" +
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
      
      //DiasdemScript = DIAsDEMscript.getTestDiasdemScript();
      try {
        DiasdemScript = new DefaultDIAsDEMscript(
        Parameter.getBatchScriptFileName());
      }
      catch (DiasdemException e2) {
        DiasdemGui.logWarningMessage(e2.getMessage());
        JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
        "Error: The chosen batch script\n"
        + Tools.shortenFileName(Parameter.getBatchScriptFileName(), 30) + "\n"
        + "cannot be opened. It might not\n"
        + "be a valid DIAsDEM batch script.",
        this.getPreferredTitle(), JOptionPane.ERROR_MESSAGE);
        this.setControlPanelContainerClosed(true);
        return;
      }
      DiasdemScriptTask = (DIAsDEMscriptTask)DiasdemScript.getFirstScriptTask();
      
      Progress = new AbstractTaskProgress(Progress.INDETERMINATE, 
      "Initial Preparations");
      Result = new AbstractTaskResult(TaskResult.NO_RESULT, "");
      GuiInternalProgressMonitor = new KInternalProgressMonitor(
      DiasdemGui.getJFrame(),
      "Please Wait: Executing Batch Script ...",
      "Initial Preparations", 0, 100, true, "Execute Batch Script",
      "Stop", true, DiasdemGui.getKDesktopPane(),
      "Task 0/"+DiasdemScript.countScriptTasks(),
      0, DiasdemScript.countScriptTasks());
      DiasdemScript.resetScript();
      DiasdemScript.addLog(DiasdemGui.logInfoMessage(
      "Task started: Execute Batch Script"));
      DiasdemScript.setStartTimeStamp(Tools.getSystemDate());
      ErrorsDuringScriptExecution = false;
      this.launchCurrentDiasdemScriptTask();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void taskFinished() {
    
    if (DiasdemScriptTask.execute()) {
      DiasdemScriptTask.setResult(
      (ScriptableTaskResult)CurrentScriptTaskResult);
      DiasdemScriptTask.setEndTimeStamp(Tools.getSystemDate());
      
      if (CurrentScriptTaskResult.getStatus() == TaskResult.NO_RESULT) {
        DiasdemScriptTask.addLog(DiasdemGui.logWarningMessage(
        CurrentScriptTaskResult.getLogMessage()));
        GuiInternalProgressMonitor.close();
        DiasdemScriptTask.setStatus(ScriptTask.TASK_EXECUTED_WITH_ERRORS);
        ErrorsDuringScriptExecution = true;
        JOptionPane.showInternalMessageDialog(DiasdemGui.getKDesktopPane(),
        "The batch script terminated unsuccessfully:\n"
        + CurrentScriptTaskResult.getDescription(),
        "Execute Batch Script", JOptionPane.ERROR_MESSAGE);
        DiasdemScript.addLog(KLogPanel.getLogMessage("Execution of task "
        + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks()
        + " (" + DiasdemScriptTask.getLabel() + ")"
        + " has terminated unsuccessfully"));
        DiasdemScript.addLog(DiasdemGui.logInfoMessage(
        "Task finished: Execute Batch Script"));
        DiasdemScriptTask.addLog(KLogPanel.getLogMessage("Execution of task "
        + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks()
        + " (" + DiasdemScriptTask.getLabel() + ")"
        + " has terminated unsuccessfully"));
        DiasdemScript.setStatus(Script.SCRIPT_EXECUTED_WITH_ERRORS);
        DiasdemScript.writeXmlDocument(Parameter.getBatchScriptFileName());
        this.setControlPanelContainerClosed(true);
        return;
      }
      if (CurrentScriptTaskResult.getStatus() == TaskResult.FINAL_RESULT) {
        DiasdemScript.addLog(KLogPanel.getLogMessage("Execution of task "
        + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks()
        + " (" + DiasdemScriptTask.getLabel() + ")"
        + " has terminated successfully"));
        DiasdemScriptTask.addLog(KLogPanel.getLogMessage("Execution of task "
        + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks()
        + " (" + DiasdemScriptTask.getLabel() + ")"
        + " has terminated successfully"));
        DiasdemScriptTask.setStatus(ScriptTask.TASK_EXECUTED_WITHOUT_ERRORS);
        if (CurrentScriptTaskResult instanceof OpenProjectResult) {
          DiasdemProject = ((OpenProjectResult)CurrentScriptTaskResult)
          .getDiasdemProject();
        }
        if (CurrentScriptTaskResult instanceof CloseProjectResult) {
          DiasdemProject = null;
        }
      }
    }
    DiasdemScriptTask = (DIAsDEMscriptTask)DiasdemScript.getNextScriptTask();
    this.launchCurrentDiasdemScriptTask();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}