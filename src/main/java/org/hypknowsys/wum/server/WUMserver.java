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

package org.hypknowsys.wum.server;

import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class WUMserver implements Server {  

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private Task CurrentTask = null;
  private int TaskStatus = Task.TASK_UNKNOWN;
  private TaskProgress CurrentTaskProgress = null;
  private TaskResult CurrentTaskResult = null;
  private int TaskResultStatus = TaskResult.RESULT_UNKNOWN;
  private int TaskThreadPriority = Thread.NORM_PRIORITY;
  
  private Script CurrentScript = null;

  private Class[] ConstructorParameterClass = null;
  private Object[] ConstructorParameter = null;

  private String RunningTask = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static boolean GUI_PRIORITY = true;  

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public WUMserver() {

    ConstructorParameterClass = null;
    ConstructorParameter = null;
    
    CurrentScript = null;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public int getTaskThreadPriority() { 
    return TaskThreadPriority; }
  public int getTaskStatus() { 
    return TaskStatus; }

  /* ########## ########## ########## ########## ########## ######### */

  public TaskProgress getTaskProgress() {
    
    if (CurrentTaskProgress != null)
      return CurrentTaskProgress;
    else
      return new AbstractTaskProgress(0, "");
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public TaskResult getTaskResult() {
    
    if (CurrentTaskResult != null)
      return CurrentTaskResult;
    else
      return new AbstractTaskResult(TaskResult.NO_RESULT, "");
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setTaskProgress(TaskProgress pTaskProgress) {
    CurrentTaskProgress = pTaskProgress; }
  public void setTaskResult(TaskResult pTaskResult) { 
    CurrentTaskResult = pTaskResult; }

  /* ########## ########## ########## ########## ########## ######### */

  public void setTaskStatus(int pTaskStatus) { 
    
    TaskStatus = pTaskStatus; 
    if (TaskStatus == Task.TASK_FINISHED)
      RunningTask = null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setTaskThreadPriority(int pTaskThreadPriority) {
    
    if (pTaskThreadPriority < Thread.MIN_PRIORITY || 
      pTaskThreadPriority > Thread.MAX_PRIORITY)
      TaskThreadPriority = Thread.NORM_PRIORITY;
    else  
      TaskThreadPriority = pTaskThreadPriority; 
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Server methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void terminateSession() {}  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, String pTaskClassName, TaskParameter pTaskParameter) {
  
    // pTaskClassName = "org.hypknowsys.Wum.tasks.file.newProject.newProjectTask"

    AbstractValidatedTaskParameter validatedTaskParameter = null;
    // use other task than CurrentTask to avoid concurrency issues
    Task taskForParameterValidation = null;
    try {
      taskForParameterValidation = (Task)Class.forName(pTaskClassName)
      .getConstructor(null).newInstance(null);
      validatedTaskParameter = ((Task)taskForParameterValidation)
      .validateTaskParameter(pProject, pTaskParameter);  
    }
    catch(ClassNotFoundException e1) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: ClassNotFoundException, parameter cannot be validated!\n" 
      + pTaskClassName);
      e1.printStackTrace(); 
    }
    catch(NoSuchMethodException e2) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: NoSuchMethodException, parameter cannot be validated!\n" 
      + pTaskClassName);      
      e2.printStackTrace();
    }
    catch(InstantiationException e3) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: InstantiationException, parameter cannot be validated!\n" 
      + pTaskClassName);      
      e3.printStackTrace(); 
    }
    catch(IllegalAccessException e4) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: IllegalAccessException, parameter cannot be validated!\n" 
      + pTaskClassName);      
      e4.printStackTrace(); 
    }
    catch(InvocationTargetException e5) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: InvocationTargetException, parameter cannot be validated!\n" 
      + pTaskClassName);      
      e5.printStackTrace(); 
    }

    return validatedTaskParameter;
    
  }

  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult executeBlockingTask(Project pProject, String pTaskClassName, 
  TaskParameter pTaskParameter) {
    
   // pTaskClassName = "org.hypknowsys.Wum.tasks.projects.newProject.NewProjectTask"

    RunningTask = pTaskClassName;
    TaskStatus = Task.TASK_UNKNOWN;
    CurrentTaskResult = null;
    CurrentTaskProgress = null;
    try {
      CurrentTask = (BlockingTask)Class.forName(pTaskClassName)
        .getConstructor(ConstructorParameterClass)
        .newInstance(ConstructorParameter);
      CurrentTaskResult = ( (BlockingTask)CurrentTask )
      .execute((org.hypknowsys.server.Server)this, pProject, pTaskParameter);  

      if (CurrentTask instanceof ScriptableTask && CurrentScript != null) {
        CurrentScript.addScriptTask(new DefaultWUMscriptTask(CurrentTask
        .getLabel(), pTaskClassName, (ScriptableTaskParameter)pTaskParameter,
        "Java Class Name: " + pTaskClassName));
      }
    }
    catch(ClassNotFoundException e1) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: ClassNotFoundException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: ClassNotFoundException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e1.printStackTrace(); 
    }
    catch(NoSuchMethodException e2) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: NoSuchMethodException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: NoSuchMethodException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e2.printStackTrace(); 
    }
    catch(InstantiationException e3) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: InstantiationException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: InstantiationException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e3.printStackTrace(); 
    }
    catch(IllegalAccessException e4) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: IllegalAccessException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: IllegalAccessException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e4.printStackTrace(); 
    }
    catch(InvocationTargetException e5) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: InvocationTargetException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: InvocationTargetException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e5.printStackTrace(); 
    }

    return CurrentTaskResult; 
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void startNonBlockingTask(Project pProject, String pTaskClassName, 
  TaskParameter pTaskParameter) {
    
    // pTaskClassName = "org.hypknowsys.Wum.tasks.project.newProject.NewProjectTask"

    RunningTask = pTaskClassName;
    TaskStatus = Task.TASK_UNKNOWN;
    CurrentTaskResult = null;
    CurrentTaskProgress = null;
    try {
      CurrentTask = (NonBlockingTask)Class.forName(pTaskClassName)
        .getConstructor(ConstructorParameterClass)
        .newInstance(ConstructorParameter);
      ( (NonBlockingTask)CurrentTask ).start((org.hypknowsys.server.Server)this,
      pProject, pTaskParameter, TaskThreadPriority);  
      
      if (CurrentTask instanceof ScriptableTask && CurrentScript != null) {
        CurrentScript.addScriptTask(new DefaultWUMscriptTask(CurrentTask
        .getLabel(), pTaskClassName, (ScriptableTaskParameter)pTaskParameter,
        "Java Class Name: " + pTaskClassName));
      }
    }
    catch(ClassNotFoundException e1) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: ClassNotFoundException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: ClassNotFoundException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e1.printStackTrace(); 
    }
    catch(NoSuchMethodException e2) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: NoSuchMethodException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: NoSuchMethodException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e2.printStackTrace(); 
    }
    catch(InstantiationException e3) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: InstantiationException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: InstantiationException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e3.printStackTrace(); 
    }
    catch(IllegalAccessException e4) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: IllegalAccessException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: IllegalAccessException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e4.printStackTrace(); 
    }
    catch(InvocationTargetException e5) { 
      CurrentTaskProgress = new AbstractTaskProgress(100, 
      "Error: InvocationTargetException, task cannot be started!\n" + pTaskClassName); 
      CurrentTaskResult = new AbstractTaskResult(TaskResult.NO_RESULT, 
      "Error: InvocationTargetException, task cannot be started!\n" + pTaskClassName);
      TaskStatus = Task.TASK_FINISHED;
      e5.printStackTrace(); 
    }
    
  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void stopNonBlockingTask() {

    // task can't be stopped
    if (RunningTask == null)  
      return;

    if (CurrentTask != null && CurrentTask instanceof NonBlockingTask) { 
      ( (NonBlockingTask)CurrentTask ).stop();
      CurrentTask = null;
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskProgress(TaskProgress pTaskProgress, Thread pTaskThread) {

    CurrentTaskProgress = pTaskProgress; 

    // used to work around the ProgressMonitor/Timer bug in Linux:
    // TaskThread is suspended to activate the event dispatch thread
    // added by kwinkler, 10/2000
    if (GUI_PRIORITY)
      try {
        pTaskThread.sleep(0, 1);
      }
      catch (InterruptedException e) {}

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void appendTaskProgressNote(String pNote) {

    if (CurrentTaskProgress != null)
      CurrentTaskProgress.appendNote(pNote);
    else
      CurrentTaskProgress = new AbstractTaskProgress(0, pNote);
     
  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void appendTaskProgressNote(String pNote, Thread pTaskThread) {

    if (CurrentTaskProgress != null)
      CurrentTaskProgress.appendNote(pNote);
    else
      CurrentTaskProgress = new AbstractTaskProgress(0, pNote);
 
    // used to work around the ProgressMonitor/Timer bug in Linux:
    // TaskThread is suspended to activate the event dispatch thread
    // added by kwinkler, 10/2000
    if (GUI_PRIORITY)
      try {
        pTaskThread.sleep(0, 1);
      }
      catch (InterruptedException e) {}
    
  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskProgress clearTaskProgress() {

    TaskProgress result = null;
    if (CurrentTaskProgress != null) {
      result = new AbstractTaskProgress( CurrentTaskProgress.getValue(),
        CurrentTaskProgress.getNote() );
      CurrentTaskProgress = null;
    }
    else 
      result = new AbstractTaskProgress(0, "");

    return result;
     
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void startRecordingScript(Script pNewScript) {
  
    CurrentScript = (Script)( pNewScript.clone() );
    CurrentScript.setLabel("Script recorded by WUMserver; "
    + Tools.getSystemDate());
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Script stopRecordingScript() {
    
    Script tmpScript = (Script)( CurrentScript.clone() );
    CurrentScript = null;
    return tmpScript;
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Task instantiateTask(String pTaskClassName, 
  Server pWumServer) {
    
    // pTaskClassName = "org.hypknowsys.Wum.tasks.project.newProject.NewProjectTask"   
    // use other task than CurrentTask to avoid concurrency issues
    if (pTaskClassName != null) {
      Task task = null;
      try {
        task = (Task)Class.forName(pTaskClassName)
        .getConstructor(null).newInstance(null);
      }
      catch(ClassNotFoundException e1) { e1.printStackTrace(); }
      catch(NoSuchMethodException e2) { e2.printStackTrace(); }
      catch(InstantiationException e3) { e3.printStackTrace(); }
      catch(IllegalAccessException e4) { e4.printStackTrace(); }
      catch(InvocationTargetException e5) { e5.printStackTrace(); }
      
      return task;
    }
    else {
      return null;
    }
   
  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public BlockingTask instantiateBlockingTask(
  String pTaskClassName, Server pWumServer) {
    
    Task task = this.instantiateTask(pTaskClassName,
    pWumServer);
    
    if (task != null && task instanceof BlockingTask) {
      return (BlockingTask)task;
    }
    else {
      return null;
    }
    
  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public NonBlockingTask instantiateNonBlockingTask(
  String pTaskClassName, Server pWumServer) {
    
    Task task = this.instantiateTask(pTaskClassName,
    pWumServer);
    
    if (task != null && task instanceof NonBlockingTask) {
      return (NonBlockingTask)task;
    }
    else {
      return null;
    }
    
  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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