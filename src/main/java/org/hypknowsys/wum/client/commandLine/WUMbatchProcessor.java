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

package org.hypknowsys.wum.client.commandLine;

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
import org.hypknowsys.wum.tasks.project.newProject.*;
import org.hypknowsys.wum.tasks.project.openProject.*;
import org.hypknowsys.wum.tasks.project.closeProject.*;
import org.hypknowsys.wum.client.gui.solutions.batch.executeBatchScript.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

// command line version of org.hypknowsys.wum.client.gui.solutions.batch.executeBatchScript
// return code = 0: batch script finished successfully
// return code = 1: batch script has not been loaded 
// return code = 2: batch script finished unsuccessfully
  
public class WUMbatchProcessor extends WumActionsControlPanel implements Runnable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private Thread TaskThread = null;  

  private Server WumServer = null; 
  private Script WumScript = null;
  private ScriptTask WumScriptTask = null;
  private int WumScriptTaskIndex = 0;
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

  public WUMbatchProcessor(String pWumBatchScriptFileName, boolean pVerbose) {
  
    Parameter = new ExecuteBatchScriptParameter(pWumBatchScriptFileName);
    Verbose = pVerbose;

    if (!Tools.isExistingFile(Parameter.getBatchScriptFileName(),
    WUMproject.SCRIPT_FILE_EXTENSION)) {
      System.out.println(
      "Error: The <WUM Batch Script File Name> is incorrect.\n" +
      "It must correspond to a valid local file name of an existing\n" +
      "WUM script file whose file extension is '" + 
      WUMproject.SCRIPT_FILE_EXTENSION + "'.");
      System.exit(1);
    }
    else {
      WUMguiPreferences WumGuiPreferences = new WUMguiPreferences();
      System.out.println("*");
      System.out.println("* " + WumGuiPreferences.getProperty(
      "WUM_WORKBENCH_TITLE") + " " + WumGuiPreferences
      .getProperty("WUM_VERSION") + " for Java " + WumGuiPreferences
      .getProperty("JAVA_VERSION") + " Released " + WumGuiPreferences
      .getProperty("RELEASE_DATE"));
      System.out.println("* Executing Batch Script: " + Parameter
      .getBatchScriptFileName() + " ...");
      System.out.println("*");
      WumServer = new WUMserver();
      WumServer.setTaskThreadPriority(10);  // maximum value
      this.executeWumBatchScript();        
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
      WumScript = new DefaultWUMscript(
      Parameter.getBatchScriptFileName());
    }
    catch (WumException e2) {
      System.out.println(
      "Error: The <WUM Batch Script File Name> "
      + Tools.shortenFileName(Parameter.getBatchScriptFileName(), 30) + "\n"
      + "cannot be opened. It might not be a valid WUM batch script.");
      System.exit(1);
    }
    
    Result = new AbstractTaskResult(TaskResult.NO_RESULT, "");
    LogMessage = KLogPanel.getLogMessage("Task started: Execute Batch Script");
    WumScript.resetScript();
    WumScript.addLog(LogMessage);
    System.out.println(LogMessage);
    WumScript.setStartTimeStamp(Tools.getSystemDate());
    ErrorsDuringScriptExecution = false;

    WumScriptTask = (WUMscriptTask)WumScript.getFirstScriptTask();
    while (WumScriptTask != null) {
      this.launchCurrentWumScriptTask();
      WumScriptTask = (WUMscriptTask)WumScript.getNextScriptTask();
    }
    
    // script execution has terminated
    WumScript.setEndTimeStamp(Tools.getSystemDate());
    if (ErrorsDuringScriptExecution == false) {
      WumScript.setStatus(Script.SCRIPT_EXECUTED_WITHOUT_ERRORS);
    }
    LogMessage = KLogPanel.getLogMessage(
    "Task successfully finished: Execute Batch Script");
    WumScript.addLog(LogMessage);
    System.out.println(LogMessage);
    WumScript.writeXmlDocument(Parameter.getBatchScriptFileName());
    System.exit(0);
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  private void executeWumBatchScript() {
   
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
  
  private void launchCurrentWumScriptTask() {
    
    WumScriptTaskIndex++;
    
    if (WumScriptTask.execute()) {
      
      LogMessage = KLogPanel.getLogMessage("Starting execution of task "
      + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
      + " (" + WumScriptTask.getLabel() + ")");
      WumScript.addLog(LogMessage);
      WumScriptTask.addLog(LogMessage);
      System.out.println(LogMessage);
      WumScriptTask.setStartTimeStamp(Tools.getSystemDate());
      
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
        else if (currentTask instanceof NonBlockingTask) {
          CurrentNonBlockingTaskClassName = WumScriptTask.getClassName();
        }
        else {
          System.out.println(KLogPanel.getLogMessage("Error: " 
          + WumScriptTask.getClassName() + " cannot be started!"));
        }
      }
      catch(Exception e2) {
        System.out.println(KLogPanel.getLogMessage("Error: " 
        + WumScriptTask.getClassName() + " cannot be started!"));
        e2.printStackTrace();
        System.exit(2);
      }
      
      if (CurrentBlockingTaskClassName != null) {
        CurrentScriptTaskResult = (ScriptableTaskResult)WumServer
        .executeBlockingTask((Project)WumProject, WumScriptTask
        .getClassName(), WumScriptTask.getParameter());
        this.taskFinished();
      }
      else if (CurrentNonBlockingTaskClassName != null) {
        WumServer.startNonBlockingTask((Project)WumProject,
        WumScriptTask.getClassName(), WumScriptTask.getParameter());
        boolean TaskRunning = true;
        while (TaskRunning) { // wait for server to finish the task
          try {
            TaskThread.sleep(10 * ONE_SECOND);
          }
          catch(InterruptedException e) {}
          this.timerEvent();
          if (WumServer.getTaskStatus() == Task.TASK_FINISHED) {
            CurrentScriptTaskResult = (ScriptableTaskResult)WumServer
            .getTaskResult();
            TaskRunning = false;
          }
        }
        this.taskFinished();
      }     
      
    }  // if (WumScriptTask.execute())
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void timerEvent() {
    
    if (Verbose) {
      // System.out.print("."); System.out.flush();
      CurrentScriptTaskProgress = WumServer.getTaskProgress();
      System.out.println("Progress: " + CurrentScriptTaskProgress.getValue()
      + "%, " + CurrentScriptTaskProgress.getNote());
      System.out.flush();
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void taskFinished() {
    
    WumScriptTask.setResult((ScriptableTaskResult)CurrentScriptTaskResult);
    WumScriptTask.setEndTimeStamp(Tools.getSystemDate());
    
    if (CurrentScriptTaskResult.getStatus() == TaskResult.NO_RESULT) {
      LogMessage = KLogPanel.getLogMessage(CurrentScriptTaskResult
      .getLogMessage());
      WumScript.addLog(LogMessage);
      System.out.println(LogMessage);
      WumScriptTask.setStatus(ScriptTask.TASK_EXECUTED_WITH_ERRORS);
      ErrorsDuringScriptExecution = true;
      LogMessage = KLogPanel.getLogMessage("Execution of task "
      + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
      + " (" + WumScriptTask.getLabel() + ")"
      + " has terminated unsuccessfully");
      WumScript.addLog(LogMessage);
      WumScriptTask.addLog(LogMessage);
      System.out.println(LogMessage);
      LogMessage = KLogPanel.getLogMessage(
      "Task unsuccessfully finished: Execute Batch Script");
      WumScript.addLog(LogMessage);
      System.out.println(LogMessage);
      WumScript.setStatus(Script.SCRIPT_EXECUTED_WITH_ERRORS);
      WumScript.writeXmlDocument(Parameter.getBatchScriptFileName());
      System.exit(2);
    }
    if (CurrentScriptTaskResult.getStatus() == TaskResult.FINAL_RESULT) {
      LogMessage = KLogPanel.getLogMessage("Execution of task "
      + WumScriptTaskIndex + "/" + WumScript.countScriptTasks()
      + " (" + WumScriptTask.getLabel() + ")"
      + " has terminated successfully");
      WumScript.addLog(LogMessage);
      System.out.println(LogMessage);
      WumScriptTask.addLog(LogMessage);
      WumScriptTask.setStatus(ScriptTask.TASK_EXECUTED_WITHOUT_ERRORS);
      if (CurrentScriptTaskResult instanceof OpenProjectResult) {
        WumProject = ((OpenProjectResult)CurrentScriptTaskResult)
        .getWumProject();
      }
      else if (CurrentScriptTaskResult instanceof NewProjectResult) {
        WumProject = ((NewProjectResult)CurrentScriptTaskResult)
        .getWumProject();
      }
      else if (CurrentScriptTaskResult instanceof CloseProjectResult) {
        WumProject = null;
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
    args[0], WUMproject.SCRIPT_FILE_EXTENSION)) {
      if (args.length >= 2 && args[1] != null && args[1].equals("verbose")) {
        WUMbatchProcessor batchProcessor =
        new WUMbatchProcessor(args[0], true);
      }
      else {
        WUMbatchProcessor batchProcessor =
        new WUMbatchProcessor(args[0], false);
      }
    }
    else {
      System.out.println("Usage: " 
      + "java wum.client.commandLine.WUMbatchProcessor " 
      + "<WUM Batch Script File Name> [verbose]");
      System.exit(1);
    }
    
  }
  
}