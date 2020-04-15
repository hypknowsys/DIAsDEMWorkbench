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

import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class DiasdemNonBlockingTask extends DiasdemTask 
implements NonBlockingTask, Runnable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Thread TaskThread = null;  
  protected TaskProgress Progress = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected transient DIAsDEMcollection DiasdemCollection = null;
  protected transient DIAsDEMdocument DiasdemDocument = null;
  protected transient DIAsDEMtextUnit DiasdemTextUnit = null;
  protected transient StringBuffer TextUnitContents = null;
  protected transient String TextUnitContentsAsString = null;
  
  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public DiasdemNonBlockingTask() { 

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
  
    DiasdemServer = pServer;
    if (pProject instanceof DIAsDEMproject) {
      DiasdemProject = (DIAsDEMproject)pProject;
    }
    else {
      DiasdemProject = null;
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
      if (DiasdemCollection != null) {
        this.closeDiasdemCollection();
      }
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
    
    DiasdemServer.setTaskStatus(Task.TASK_ACCEPTED);
    Progress = new DiasdemTaskProgress(pMinProgress, pProgressNote);
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    Result = new DiasdemScriptableTaskResult(TaskResult.NO_RESULT, "", "");
    DiasdemServer.setTaskResult(Result);    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void validateParameter(TaskParameter pParameter,
  String pErrorShortMessage) {
    
    AbstractValidatedTaskParameter validatedParameter =
    this.validateTaskParameter(DiasdemProject, pParameter);
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
  
  protected void openDiasdemCollection(String pDiasdemCollectionFileName) {
    
    try {
      DiasdemCollection = DiasdemProject.instantiateDefaultDiasdemCollection();
      DiasdemCollection.open(pDiasdemCollectionFileName);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.setErrorTaskResult(100, 
      "Error: DIAsDEM Collection cannot be opened!",
      "Error: The DIAsDEM document collection cannnot be opened!\n"
      + "File: " + Tools.shortenFileName(pDiasdemCollectionFileName, 50));
      this.stop();
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void checkPrerequisitesAndSetDefaultTextUnitsLayer(
  String pProgressNote) {
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    if (DiasdemDocument == null) {
      this.setErrorTaskResult(100, pProgressNote,
      "Error: The document collection does\n" +
      "not contain any documents at all!");
      this.stop();
    }
    DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
    .getActiveTextUnitsLayerIndex());
    if (DiasdemDocument.getActiveTextUnitsLayer() == null) {
      this.setErrorTaskResult(100, pProgressNote,
      "Error: The first document does not contain\n" +
      "the default active text units layer " + 
      DiasdemProject.getActiveTextUnitsLayerIndex() + "!");
      this.stop();
    }
    if (DiasdemDocument.getNumberOfOriginalTextUnits() == 0) {
      this.setErrorTaskResult(100, pProgressNote,
      "Error: The default active text units\n" +
      "layer " + DiasdemProject.getActiveTextUnitsLayerIndex() + 
      " of the first document does not\n" +
      "contain any original text units at all!");
      this.stop();
    }
    if (DiasdemDocument.getNumberOfProcessedTextUnits() == 0) {
      this.setErrorTaskResult(100, pProgressNote,
      "Error: The default active text units\n" +
      "layer " + DiasdemProject.getActiveTextUnitsLayerIndex() + 
      " of the first document does not\n" +
      "contain any processed text units at all!");
      this.stop();
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void closeDiasdemCollection() {
    
    try {
      DiasdemCollection.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      this.setErrorTaskResult(100, 
      "Error: DIAsDEM Collection cannot be closed!",
      "Error: The DIAsDEM document collection cannnot be closed!");
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
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    pFinalResult.setStatus(pResultStatus);
    DiasdemServer.setTaskResult(pFinalResult);
    DiasdemServer.setTaskStatus(pTaskStatus);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setErrorTaskResult(int pMaxProgress, String pProgressNote, 
  String pErrorResultNote) {
    
    TaskResult errorResult =  new DiasdemScriptableTaskResult(
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