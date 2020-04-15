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
import org.hypknowsys.core.Script;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface Server {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getTaskThreadPriority();
  
  public int getTaskStatus();
  
  public TaskProgress getTaskProgress();
  
  public TaskResult getTaskResult();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskThreadPriority(int pTaskThreadPriority);
  
  public void setTaskStatus(int pTaskStatus);
  
  public void setTaskProgress(TaskProgress pTaskProgress);
  
  public void setTaskResult(TaskResult pTaskResult);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void terminateSession();
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, String pTaskClassName, TaskParameter pTaskParameter);
  
  public TaskResult executeBlockingTask(Project pProject,
  String pTaskClassName, TaskParameter pTaskParameter);
  
  public void startNonBlockingTask(Project pProject, String pTaskClassName,
  TaskParameter pTaskParameter);
  
  public void stopNonBlockingTask();
  
  public void setTaskProgress(TaskProgress pTaskProgress, Thread pTaskThread);
  
  public void appendTaskProgressNote(String pNote);
  
  public void appendTaskProgressNote(String pNote, Thread pTaskThread);
  
  public TaskProgress clearTaskProgress();
  
  public void startRecordingScript(Script pNewScript);
  
  public Script stopRecordingScript();
  
  public Task instantiateTask(String pTaskClassName, Server pDiasdemServer);
  
  public BlockingTask instantiateBlockingTask(String pTaskClassName,
  Server pDiasdemServer);
  
  public NonBlockingTask instantiateNonBlockingTask(
  String pTaskClassName, Server pDiasdemServer);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}