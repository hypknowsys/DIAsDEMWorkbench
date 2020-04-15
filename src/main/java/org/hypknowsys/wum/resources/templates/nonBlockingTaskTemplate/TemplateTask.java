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

package org.hypknowsys.wum.resources.templates.nonBlockingTaskTemplate;

import java.io.File;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.core.Project;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.KProperty;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;
import org.hypknowsys.server.TaskParameter;
import org.hypknowsys.server.TaskProgress;
import org.hypknowsys.server.TaskResult;
import org.hypknowsys.wum.client.gui.WUMguiMenuBar;
import org.hypknowsys.wum.client.gui.WUMguiPreferences;
import org.hypknowsys.wum.core.WUMproject;
import org.hypknowsys.wum.core.default10.MiningBase;
import org.hypknowsys.wum.server.WumScriptableNonBlockingTask;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class TemplateTask extends WumScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TemplateParameter CastParameter = null;
  private TemplateResult CastResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String LABEL =
  "Template Non-Blocking Task";
  private static final String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.wum.resources.templates.nonBlockingTaskTemplate"
  + ".TemplateParameter";
  private static final String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.wum.resources.templates.nonBlockingTaskTemplate"
  + ".TemplateResult";
  private static final String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.resources.templates.nonBlockingTaskTemplate"
  + ".TemplateControlPanel";
  
  private static int MIN_REQUIRED_MINING_BASE_STATUS = MiningBase
  .MINING_BASE_IS_CLOSED;
  
  private static final KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DEFAULT_TEXT_COLLECTION_FILE", "Template file",
    "<DefaultValue>", KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TemplateTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
    
    MinRequiredMiningBaseStatus = MIN_REQUIRED_MINING_BASE_STATUS;
    
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
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    
    TemplateParameter parameter = null;
    if (pParameter instanceof TemplateParameter) {
      parameter = (TemplateParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    if (!(pProject instanceof WUMproject && ((WUMproject)pProject)
    .getMiningBaseStatus() >= MinRequiredMiningBaseStatus)) {
      result.addError(
      "Error: The project's mining base does not have\n"
      + "the minimum status code required by this task!");
    }
    
    File file = new File(parameter.getTextFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(WUMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n"
      + WUMguiPreferences.TEXT_FILE_EXTENSION
      + "-file in the field 'Text File'!");
    }
    if (parameter.getTextFileName().trim().length() <= 0
    || !parameter.getTextFileName().trim().endsWith(
    WUMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local "
      + WUMguiPreferences.TEXT_FILE_EXTENSION 
      + "-file name\nin the field 'Text File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new TemplateParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new TemplateResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    WUMguiMenuBar.ACTIONS_MISCELLANEOUS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof TemplateParameter) {
      CastParameter = (TemplateParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: !";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
        
    int counterProgress = 0;
    long maxProgress = 100;
    
    while (counterProgress < 100) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update((int)(counterProgress * 100 / maxProgress),
        "Processing Item " + counterProgress);
        WumServer.setTaskProgress(Progress, TaskThread);
      }

      counterProgress++;
      
    }  // do something
    
    CastResult = new TemplateResult(TaskResult.FINAL_RESULT,
    "Template Non-Blocking Task: Result", 
    "Template Non-Blocking Task: Log Message");
    this.setTaskResult(100, "All Items Processed ...", CastResult,
    TaskResult.FINAL_RESULT, Task.TASK_FINISHED);
    
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