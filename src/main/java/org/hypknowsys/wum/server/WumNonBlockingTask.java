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

import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.core.*;
import org.hypknowsys.wum.core.default10.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class WumNonBlockingTask extends WumTask implements NonBlockingTask, Runnable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Thread TaskThread = null;  
  protected TaskProgress Progress = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected transient StringBuffer TextUnitContents = null;
  protected transient String TextUnitContentsAsString = null;
  
  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public WumNonBlockingTask() { 

    super();

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
  /* ########## interface NonBlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void start(Server pServer, Project pProject, 
  TaskParameter pParameter, int pTaskThreadPriority) {
  
    WumServer = pServer;
    if (pProject instanceof WUMproject) {
      WumProject = (WUMproject)pProject;
    }
    else {
      WumProject = null;
    }
    Parameter = pParameter;
    if (TaskThread == null) {
      TaskThread = new Thread(this);
    }
    TaskThread.setPriority(pTaskThreadPriority);
    TaskThread.start();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void stop() {
  
    if (TaskThread != null && TaskThread.isAlive()) { 
      TaskThread.stop();
    }
    TaskThread = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {}

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void acceptTask(int pMinProgress, String pProgressNote) {
    
    WumServer.setTaskStatus(Task.TASK_ACCEPTED);
    Progress = new WumTaskProgress(pMinProgress, pProgressNote);
    WumServer.setTaskProgress(Progress, TaskThread);
    Result = new WumScriptableTaskResult(TaskResult.NO_RESULT, "", "");
    WumServer.setTaskResult(Result);    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void validateParameter(TaskParameter pParameter,
  String pErrorShortMessage) {
    
    AbstractValidatedTaskParameter validatedParameter =
    this.validateTaskParameter(WumProject, pParameter);
    if (validatedParameter.numberOfErrors() > 0) {
      String errorLongMessage = "";
      for (int i = 0; i < validatedParameter.numberOfErrors(); i++) {
        errorLongMessage += "\n" + validatedParameter.getError(i);
      }
      this.setErrorTaskResult(100, pErrorShortMessage,
      pErrorShortMessage + errorLongMessage);
      this.stop();      
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setTaskResult(int pMaxProgress, String pProgressNote, 
  TaskResult pFinalResult, int pResultStatus) {
    
    this.setTaskResult(pMaxProgress, pProgressNote, pFinalResult, 
    pResultStatus, Task.TASK_FINISHED);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setTaskResult(int pMaxProgress, String pProgressNote, 
  TaskResult pFinalResult, int pResultStatus, int pTaskStatus) {
    
    Progress.update(pMaxProgress, pProgressNote); 
    WumServer.setTaskProgress(Progress, TaskThread);
    pFinalResult.setStatus(pResultStatus);
    WumServer.setTaskResult(pFinalResult);
    WumServer.setTaskStatus(pTaskStatus);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setErrorTaskResult(int pMaxProgress, String pProgressNote, 
  String pErrorResultNote) {
    
    TaskResult errorResult =  new WumScriptableTaskResult(
    TaskResult.NO_RESULT, pErrorResultNote, pProgressNote);
    this.setTaskResult(pMaxProgress, pProgressNote, errorResult,
    TaskResult.NO_RESULT, Task.TASK_FINISHED);
        
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