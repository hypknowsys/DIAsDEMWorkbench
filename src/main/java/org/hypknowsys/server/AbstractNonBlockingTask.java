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

package org.hypknowsys.server;

import org.hypknowsys.core.Project;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public abstract class AbstractNonBlockingTask extends AbstractTask
implements Runnable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected Thread TaskThread = null;
  protected AbstractTaskProgress Progress = null;
  
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
  
  public AbstractNonBlockingTask() {
    
    super();
    TaskServer = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractNonBlockingTask(Server pTaskServer) {
    
    super(pTaskServer);
    TaskServer = pTaskServer;
    
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
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    TaskServer.setTaskStatus(Task.TASK_ACCEPTED);
    Progress = new AbstractTaskProgress(0, "Please wait!");
    TaskServer.setTaskProgress(Progress, TaskThread);
    Result = new AbstractTaskResult(TaskResult.NO_RESULT, "");
    TaskServer.setTaskResult(Result);
    
    int counterProgress = 0;
    int maxProgress = 100;  // must be determined according to the task
    
    // do something before the loop
    
    // do something in a loop ...
    
    // update the DiasdemServer's progress counter in the loop
    if ((counterProgress == 1) || ((counterProgress % 50) == 0)) {
      Progress.update(counterProgress, maxProgress,
      "CurrentProgress: " +  counterProgress);
      TaskServer.setTaskProgress(Progress, TaskThread);
    }
    
    // there may be an intermediate result to be shown
    Result.update(TaskResult.INTERMEDIATE_RESULT, "Some results ...");
    TaskServer.setTaskResult(Result);
    
    // continue doing something in a loop ...
    
    // do something after the loop
    
    Result.update(TaskResult.FINAL_RESULT, "Some results ...");
    TaskServer.setTaskResult(Result);
    TaskServer.setTaskStatus(Task.TASK_FINISHED);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface NonBlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void start(Project pProject, TaskParameter pParameter,
  int pTaskThreadPriority) {
    
    TaskProject = pProject;
    Parameter = (AbstractTaskParameter)pParameter;
    if (TaskThread == null) {
      TaskThread = new Thread(this);
    }
    TaskThread.setPriority(pTaskThreadPriority);
    TaskThread.start();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void stop() {
    
    if ((TaskThread != null) && TaskThread.isAlive()) {
      TaskThread.stop();
    }
    TaskThread = null;
    
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
  
  public static void main(String pOptions[]) {}
  
}