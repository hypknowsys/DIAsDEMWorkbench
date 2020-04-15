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

package org.hypknowsys.diasdem.tasks.project.openProject;

import java.awt.event.KeyEvent;
import java.io.IOException;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar;
import org.hypknowsys.diasdem.core.DIAsDEMproject;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMproject;
import org.hypknowsys.diasdem.server.DiasdemScriptableBlockingTask;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;
import org.hypknowsys.server.TaskResult;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class OpenProjectTask extends DiasdemScriptableBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private OpenProjectParameter CastParameter = null;
  private OpenProjectResult CastResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String LABEL =
  "Open Project";
  private static final String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.project.openProject"
  + ".OpenProjectParameter";
  private static final String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.project.openProject"
  + ".OpenProjectResult";
  private static final String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.project.openProject"
  + ".OpenProjectControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public OpenProjectTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    
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
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    
    Parameter = pParameter;
    CastParameter = (OpenProjectParameter)pParameter;
    
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(Parameter);
    
    
    if (!Tools.isExistingFile(CastParameter.getProjectFileName(),
    DIAsDEMproject.PROJECT_FILE_EXTENSION)) {
      result.addError(
      "Error: The parameter 'Project File Name' is incorrect.\n"
      + "It must contain the valid local file name of an existing\n"
      + "DIAsDEM project file whose file extension is '"
      + DIAsDEMproject.PROJECT_FILE_EXTENSION + "'.");
    }
    
    return result;
    
  }
  
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
    CastParameter = (OpenProjectParameter)pParameter;
    
    this.acceptTask();
    if (!this.isValidParameter(Parameter,
    "Error: Project cannot be opened!")) {
      return Result;
    }
    
    try {
      DiasdemProject = new DefaultDIAsDEMproject(
      CastParameter.getProjectFileName(), DefaultDIAsDEMproject.LOAD);
      CastResult = new OpenProjectResult(TaskResult.FINAL_RESULT,
      "The DIAsDEM project " + DiasdemProject.getProjectName()
      + " has been successfully opened.", "Project "
      + DiasdemProject.getProjectName() + " opened", DiasdemProject);
    }
    catch (IOException e) {
      CastResult = new OpenProjectResult(TaskResult.NO_RESULT,
      "Error: The DIAsDEM project " + DiasdemProject.getProjectName()
      + " cannot be opened due to an internal error.", "Project "
      + DiasdemProject.getProjectName() + " cannot be opened", null);
    }
    
    return CastResult;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new OpenProjectParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new OpenProjectResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem("Open Project", KeyEvent.VK_O,
    DIAsDEMguiMenuBar.ACTIONS_PROJECT_MANAGEMENT,
    null, 0, 0,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
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