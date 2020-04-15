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

package org.hypknowsys.wum.tasks.project.openProject;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class OpenProjectTask extends WumScriptableBlockingTask {
  
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
  
  private final static String LABEL =
  "Open Project";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.wum.tasks.project.openProject.OpenProjectParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.wum.tasks.project.openProject.OpenProjectResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.project.openProject.OpenProjectControlPanel";
  
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
    WUMproject.PROJECT_FILE_EXTENSION)) {
      result.addError(
      "Error: The parameter 'Project File Name' is incorrect.\n" +
      "It must contain the valid local file name of an existing\n" +
      "DIAsDEM project file whose file extension is '" +
      WUMproject.PROJECT_FILE_EXTENSION + "'.");
    }
    
    return result;
    
  }
  
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
    CastParameter = (OpenProjectParameter)pParameter;
    
    this.acceptTask();
    if (!this.isValidParameter(Parameter, 
    "Error: Project cannot be opened!")) {
      return Result; 
    }
    
    try {
      WumProject = new DefaultWUMproject(
      CastParameter.getProjectFileName(), DefaultWUMproject.LOAD);
      CastResult = new OpenProjectResult(TaskResult.FINAL_RESULT,
      "The WUM project " + WumProject.getProjectName() +
      " has been successfully opened.", "Project " +
      WumProject.getProjectName() + " opened", WumProject);
    }
    catch (IOException e) {
      CastResult = new OpenProjectResult(TaskResult.NO_RESULT,
      "Error: The WUM project " + WumProject.getProjectName() +
      " cannot be opened due to an internal error.", "Project " +
      WumProject.getProjectName() + " cannot be opened", null);
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
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_PROJECT_MANAGEMENT,
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
  
  public static void main(String args[]) {}
  
}