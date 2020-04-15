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

package org.hypknowsys.wum.tasks.project.newProject;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;
import org.hypknowsys.wum.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class NewProjectTask extends WumScriptableBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NewProjectParameter CastParameter = null;
  private NewProjectResult CastResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "New Project";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.wum.tasks.project.newProject.NewProjectParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.wum.tasks.project.newProject.NewProjectResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.project.newProject.NewProjectControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public NewProjectTask() {
    
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
  /* ########## interface ScriptableTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    
    Parameter = pParameter;
    CastParameter = (NewProjectParameter)pParameter;
    
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(Parameter);
    
    
    if (CastParameter.getProjectName() == null
    || CastParameter.getProjectName().length() == 0) {
      result.addError(
      "Error: The parameter 'Project Name' is incorrect. It must\n" +
      "contain a non-empty String that labels the new project.");
    }
    
    if (!Tools.isValidandWriteableFileOrDirectoryName(CastParameter
    .getProjectFileName(), WUMproject.PROJECT_FILE_EXTENSION)) {
      result.addError(
      "Error: The parameter 'Project File Name' is incorrect.\n" +
      "It must contain a valid local file name whose file\n" +
      "extension is '" + WUMproject.PROJECT_FILE_EXTENSION +
      "'. Additionally, you must have\n" +
      "write permission for the specified file.");
    }
    
    if (Tools.isExistingFile(CastParameter.getProjectFileName())) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Project File Name' currently exists.\n" +
      "Do you really want to replace this file?");
    }
    
    if (!Tools.isValidandWriteableDirectoryName(CastParameter
    .getProjectDirectory()) || CastParameter.getProjectDirectory()
    .length() == 0) {
      result.addError(
      "Error: The parameter 'Project Directory' is incorrect.\n" +
      "It must contain a valid local directory name. Additionally,\n" +
      "you must have write permission for the specified directory.");
    }
    
    if (!Tools.isValidandWriteableDirectoryName(CastParameter
    .getParameterDirectory()) || CastParameter.getParameterDirectory()
    .length() == 0) {
      result.addError(
      "Error: The parameter 'Parameter Directory' is incorrect.\n" +
      "It must contain a valid local directory name. Additionally,\n" +
      "you must have write permission for the specified directory.");
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
    CastParameter = (NewProjectParameter)pParameter;
    
    this.acceptTask();
    if (!this.isValidParameter(Parameter, 
    "Error: New project cannot be created!")) {
      return Result; 
    }
    
    try {
      WumProject = new DefaultWUMproject(
      CastParameter.getProjectFileName(), DefaultWUMproject.CREATE);
      WumProject.setProjectName(CastParameter.getProjectName());
      WumProject.setProjectNotes(CastParameter.getProjectNotes());
      WumProject.setProjectFileName(CastParameter.getProjectFileName());
      WumProject.setProjectDirectory(CastParameter.getProjectDirectory());
      WumProject.setParameterDirectory(CastParameter.getParameterDirectory());
      WumProject.quickSave();
      CastResult = new NewProjectResult(TaskResult.FINAL_RESULT,
      "The new WUM project " + CastParameter.getProjectName() +
      " has successfully been created and opened.", "Project " +
      CastParameter.getProjectName() + " created and opened", WumProject);
    }
    catch (IOException e) {
      CastResult = new NewProjectResult(TaskResult.NO_RESULT,
      "Error: The new WUM project " + CastParameter.getProjectName() +
      " has not been created due to an internal error.", "Project " +
      CastParameter.getProjectName() + " cannot be created", null);
    }
    
    return CastResult;
    
  } 
   
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new NewProjectParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new NewProjectResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem("New Project", KeyEvent.VK_N,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_PROJECT_MANAGEMENT,
    null, 0, 0, GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
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