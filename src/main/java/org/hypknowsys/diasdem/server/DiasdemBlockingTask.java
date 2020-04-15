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

package org.hypknowsys.diasdem.server;

import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class DiasdemBlockingTask extends DiasdemTask 
implements BlockingTask {

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

  public DiasdemBlockingTask() { 

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
    
    DiasdemServer = pServer;
    if (pProject instanceof DIAsDEMproject) {
      DiasdemProject = (DIAsDEMproject)pProject;
    }
    else {
      DiasdemProject = null;
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
    
    Result = new DiasdemTaskResult(TaskResult.NO_RESULT, "", "");
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected boolean isValidParameter(
  TaskParameter pParameter, String pErrorShortMessage) {
    
    AbstractValidatedTaskParameter validatedParameter =
    this.validateTaskParameter(DiasdemProject, pParameter);
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
    DiasdemServer.setTaskResult(pFinalResult);
    DiasdemServer.setTaskStatus(pTaskStatus);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setErrorTaskResult(String pErrorResultNote) {
    
    TaskResult errorResult =  new DiasdemScriptableTaskResult(
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