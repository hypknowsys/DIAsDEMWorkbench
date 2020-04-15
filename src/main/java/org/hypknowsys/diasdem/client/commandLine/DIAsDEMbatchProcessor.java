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

package org.hypknowsys.diasdem.client.commandLine;

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
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.tasks.project.newProject.*;
import org.hypknowsys.diasdem.tasks.project.openProject.*;
import org.hypknowsys.diasdem.tasks.project.closeProject.*;
import org.hypknowsys.diasdem.client.gui.solutions.batch.executeBatchScript.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

// command line version of org.hypknowsys.diasdem.client.gui.solutions.batch.executeBatchScript
// return code = 0: batch script finished successfully
// return code = 1: batch script has not been loaded 
// return code = 2: batch script finished unsuccessfully
  
public class DIAsDEMbatchProcessor extends DiasdemActionsControlPanel
implements Runnable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private Thread TaskThread = null;  

  private Server DiasdemServer = null; 
  private Script DiasdemScript = null;
  private ScriptTask DiasdemScriptTask = null;
  private int DiasdemScriptTaskIndex = 0;
  private boolean ErrorsDuringScriptExecution = false;
  private String LogMessage = null;
  private boolean Verbose = false;
  
  private ExecuteBatchScriptParameter Parameter = null;
  private TaskProgress CurrentScriptTaskProgress = null;
  private ScriptableTaskResult CurrentScriptTaskResult = null;
  
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

  public DIAsDEMbatchProcessor(String pDiasdemBatchScriptFileName,
  boolean pVerbose) {
  
    Parameter = new ExecuteBatchScriptParameter(pDiasdemBatchScriptFileName);
    Verbose = pVerbose;

    if (!Tools.isExistingFile(Parameter.getBatchScriptFileName(),
    DIAsDEMproject.SCRIPT_FILE_EXTENSION)) {
      System.out.println(
      "Error: The <DIAsDEM Batch Script File Name> is incorrect.\n" +
      "It must correspond to a valid local file name of an existing\n" +
      "DIAsDEM script file whose file extension is '" + 
      DIAsDEMproject.SCRIPT_FILE_EXTENSION + "'.");
      System.exit(1);
    }
    else {
      DIAsDEMguiPreferences DiasdemGuiPreferences = new DIAsDEMguiPreferences();
      System.out.println("*");
      System.out.println("* " + DiasdemGuiPreferences.getProperty(
      "DIASDEM_WORKBENCH_TITLE") + " " + DiasdemGuiPreferences
      .getProperty("DIASDEM_VERSION") + " for Java " + DiasdemGuiPreferences
      .getProperty("JAVA_VERSION") + " Released " + DiasdemGuiPreferences
      .getProperty("RELEASE_DATE"));
      System.out.println("* Executing Batch Script: " + Parameter
      .getBatchScriptFileName() + " ...");
      System.out.println("*");
      DiasdemServer = new DIAsDEMserver();
      DiasdemServer.setTaskThreadPriority(10);  // maximum value
      this.executeDiasdemBatchScript();        
    }
    
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
  
  public void actionPerformed(ActionEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {

    try {
      DiasdemScript = new DefaultDIAsDEMscript(
      Parameter.getBatchScriptFileName());
    }
    catch (DiasdemException e2) {
      System.out.println(
      "Error: The <DIAsDEM Batch Script File Name> "
      + Tools.shortenFileName(Parameter.getBatchScriptFileName(), 30) + "\n"
      + "cannot be opened. It might not be a valid DIAsDEM batch script.");
      System.exit(1);
    }
    
    Result = new AbstractTaskResult(TaskResult.NO_RESULT, "");
    LogMessage = KLogPanel.getLogMessage("Task started: Execute Batch Script");
    DiasdemScript.resetScript();
    DiasdemScript.addLog(LogMessage);
    System.out.println(LogMessage);
    DiasdemScript.setStartTimeStamp(Tools.getSystemDate());
    ErrorsDuringScriptExecution = false;

    DiasdemScriptTask = (DIAsDEMscriptTask)DiasdemScript.getFirstScriptTask();
    while (DiasdemScriptTask != null) {
      this.launchCurrentDiasdemScriptTask();
      DiasdemScriptTask = (DIAsDEMscriptTask)DiasdemScript.getNextScriptTask();
    }
    
    // script execution has terminated
    DiasdemScript.setEndTimeStamp(Tools.getSystemDate());
    if (ErrorsDuringScriptExecution == false) {
      DiasdemScript.setStatus(Script.SCRIPT_EXECUTED_WITHOUT_ERRORS);
    }
    LogMessage = KLogPanel.getLogMessage(
    "Task successfully finished: Execute Batch Script");
    DiasdemScript.addLog(LogMessage);
    System.out.println(LogMessage);
    DiasdemScript.writeXmlDocument(Parameter.getBatchScriptFileName());
    System.exit(0);
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  private void executeDiasdemBatchScript() {
   
    if (TaskThread == null) {
      TaskThread = new Thread(this);
    }
    TaskThread.setPriority(5);  // normal value
    TaskThread.start();

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void launchCurrentDiasdemScriptTask() {
    
    DiasdemScriptTaskIndex++;
    
    if (DiasdemScriptTask.execute()) {
      
      LogMessage = KLogPanel.getLogMessage("Starting execution of task "
      + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks()
      + " (" + DiasdemScriptTask.getLabel() + ")");
      DiasdemScript.addLog(LogMessage);
      DiasdemScriptTask.addLog(LogMessage);
      System.out.println(LogMessage);
      DiasdemScriptTask.setStartTimeStamp(Tools.getSystemDate());
      
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
        else if (currentTask instanceof NonBlockingTask) {
          CurrentNonBlockingTaskClassName = DiasdemScriptTask.getClassName();
        }
        else {
          System.out.println(KLogPanel.getLogMessage("Error: " 
          + DiasdemScriptTask.getClassName() + " cannot be started!"));
        }
      }
      catch(Exception e2) {
        System.out.println(KLogPanel.getLogMessage("Error: " 
        + DiasdemScriptTask.getClassName() + " cannot be started!"));
        e2.printStackTrace();
        System.exit(2);
      }
      
      if (CurrentBlockingTaskClassName != null) {
        CurrentScriptTaskResult = (ScriptableTaskResult)DiasdemServer
        .executeBlockingTask((Project)DiasdemProject, DiasdemScriptTask
        .getClassName(), DiasdemScriptTask.getParameter());
        this.taskFinished();
      }
      else if (CurrentNonBlockingTaskClassName != null) {
        DiasdemServer.startNonBlockingTask((Project)DiasdemProject,
        DiasdemScriptTask.getClassName(), DiasdemScriptTask.getParameter());
        boolean TaskRunning = true;
        while (TaskRunning) { // wait for server to finish the task
          try {
            TaskThread.sleep(10 * ONE_SECOND);
          }
          catch(InterruptedException e) {}
          this.timerEvent();
          if (DiasdemServer.getTaskStatus() == Task.TASK_FINISHED) {
            CurrentScriptTaskResult = (ScriptableTaskResult)DiasdemServer
            .getTaskResult();
            TaskRunning = false;
          }
        }
        this.taskFinished();
      }     
      
    }  // if (DiasdemScriptTask.execute())
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void timerEvent() {
    
    if (Verbose) {
      // System.out.print("."); System.out.flush();
      CurrentScriptTaskProgress = DiasdemServer.getTaskProgress();
      System.out.println("Progress: " + CurrentScriptTaskProgress.getValue()
      + "%, " + CurrentScriptTaskProgress.getNote());
      System.out.flush();
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void taskFinished() {
    
    DiasdemScriptTask.setResult((ScriptableTaskResult)CurrentScriptTaskResult);
    DiasdemScriptTask.setEndTimeStamp(Tools.getSystemDate());
    
    if (CurrentScriptTaskResult.getStatus() == TaskResult.NO_RESULT) {
      LogMessage = KLogPanel.getLogMessage(CurrentScriptTaskResult
      .getLogMessage());
      DiasdemScript.addLog(LogMessage);
      System.out.println(LogMessage);
      DiasdemScriptTask.setStatus(ScriptTask.TASK_EXECUTED_WITH_ERRORS);
      ErrorsDuringScriptExecution = true;
      LogMessage = KLogPanel.getLogMessage("Execution of task "
      + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks()
      + " (" + DiasdemScriptTask.getLabel() + ")"
      + " has terminated unsuccessfully");
      DiasdemScript.addLog(LogMessage);
      DiasdemScriptTask.addLog(LogMessage);
      System.out.println(LogMessage);
      LogMessage = KLogPanel.getLogMessage(
      "Task unsuccessfully finished: Execute Batch Script");
      DiasdemScript.addLog(LogMessage);
      System.out.println(LogMessage);
      DiasdemScript.setStatus(Script.SCRIPT_EXECUTED_WITH_ERRORS);
      DiasdemScript.writeXmlDocument(Parameter.getBatchScriptFileName());
      System.exit(2);
    }
    if (CurrentScriptTaskResult.getStatus() == TaskResult.FINAL_RESULT) {
      LogMessage = KLogPanel.getLogMessage("Execution of task "
      + DiasdemScriptTaskIndex + "/" + DiasdemScript.countScriptTasks()
      + " (" + DiasdemScriptTask.getLabel() + ")"
      + " has terminated successfully");
      DiasdemScript.addLog(LogMessage);
      System.out.println(LogMessage);
      DiasdemScriptTask.addLog(LogMessage);
      DiasdemScriptTask.setStatus(ScriptTask.TASK_EXECUTED_WITHOUT_ERRORS);
      if (CurrentScriptTaskResult instanceof OpenProjectResult) {
        DiasdemProject = ((OpenProjectResult)CurrentScriptTaskResult)
        .getDiasdemProject();
      }
      else if (CurrentScriptTaskResult instanceof NewProjectResult) {
        DiasdemProject = ((NewProjectResult)CurrentScriptTaskResult)
        .getDiasdemProject();
      }
      else if (CurrentScriptTaskResult instanceof CloseProjectResult) {
        DiasdemProject = null;
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {
  
    if (args.length > 0 && args[0] != null && Tools.isExistingFile(
    args[0], DIAsDEMproject.SCRIPT_FILE_EXTENSION)) {
      if (args.length >= 2 && args[1] != null && args[1].equals("verbose")) {
        DIAsDEMbatchProcessor batchProcessor =
        new DIAsDEMbatchProcessor(args[0], true);
      }
      else {
        DIAsDEMbatchProcessor batchProcessor =
        new DIAsDEMbatchProcessor(args[0], false);
      }
    }
    else {
      System.out.println("Usage: " 
      + "java diasdem.client.commandLine.DIAsDEMbatchProcessor " 
      + "<DIAsDEM Batch Script File Name> [verbose]");
      System.exit(1);
    }
    
  }
  
}