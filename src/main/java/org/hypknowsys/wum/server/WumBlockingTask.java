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

import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class WumBlockingTask extends WumTask implements BlockingTask {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

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

  public WumBlockingTask() { 

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
  /* ########## interface BlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult execute(Server pServer, Project pProject, 
  TaskParameter pParameter) {
    
    WumServer = pServer;
    if (pProject instanceof WUMproject) {
      WumProject = (WUMproject)pProject;
    }
    else {
      WumProject = null;
    }
    Parameter = pParameter;
    
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void acceptTask() {
    
    Result = new WumTaskResult(TaskResult.NO_RESULT, "", "");
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected boolean isValidParameter(
  TaskParameter pParameter, String pErrorShortMessage) {
    
    AbstractValidatedTaskParameter validatedParameter =
    this.validateTaskParameter(WumProject, pParameter);
    if (validatedParameter.numberOfErrors() > 0) {
      String errorLongMessage = "";
      for (int i = 0; i < validatedParameter.numberOfErrors(); i++) {
        errorLongMessage += "\n" + validatedParameter.getError(i);
      }
      this.setErrorTaskResult(pErrorShortMessage + errorLongMessage);
      return false;
    }
    else {
      return true;
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setTaskResult(TaskResult pFinalResult, int pResultStatus) {
    
    this.setTaskResult(pFinalResult, pResultStatus, Task.TASK_FINISHED);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setTaskResult(TaskResult pFinalResult, 
  int pResultStatus, int pTaskStatus) {
    
    pFinalResult.setStatus(pResultStatus);
    WumServer.setTaskResult(pFinalResult);
    WumServer.setTaskStatus(pTaskStatus);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setErrorTaskResult(String pErrorResultNote) {
    
    TaskResult errorResult =  new WumScriptableTaskResult(
    TaskResult.NO_RESULT, pErrorResultNote, pErrorResultNote);
    this.setTaskResult(errorResult, TaskResult.NO_RESULT, Task.TASK_FINISHED);
        
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