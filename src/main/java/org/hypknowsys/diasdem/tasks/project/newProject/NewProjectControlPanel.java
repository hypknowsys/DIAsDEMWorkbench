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

package org.hypknowsys.diasdem.tasks.project.newProject;

import java.awt.event.ActionEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskResult;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class NewProjectControlPanel extends DiasdemActionsControlPanel {
  
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
  
  public NewProjectControlPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NewProjectControlPanel(Server pDiasdemServer, Project pDiasdemProject,
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
    this.initialize();
    
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
  /* ########## interface ActionListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void actionPerformed(ActionEvent pActionEvent) {
    
    ActionCommand = pActionEvent.getActionCommand();
    ActionSource = pActionEvent.getSource();
    
    if (ActionCommand.equals("OK")) {
      this.ok();
    }
    else if (ActionCommand.equals("Cancel")) {
      this.setControlPanelContainerClosed(true);
    }
    else if (ActionCommand.equals("KInternalFrame:EscapePressed")) {
      if (CloseIfEscapeIsPressed) {
        this.setControlPanelContainerClosed(true);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskControlPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
    
    super.initialize();
    DiasdemGui.setGuiStatus(GuiClient.PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    Parameter_Panel = new NewProjectParameterPanel(DiasdemServer,
    DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void ok() {
    
    NewProjectParameter parameter = (NewProjectParameter)Parameter_Panel
    .getTaskParameter();
    AbstractValidatedTaskParameter validatedParameter =
    DiasdemServer.validateTaskParameter((Project)DiasdemProject,
    "org.hypknowsys.diasdem.tasks.project.newProject.NewProjectTask",
    parameter);
    
    boolean commit = this.isValidParameter(validatedParameter);
    if (commit) {
      CloseIfEscapeIsPressed = false;
      if (ControlPanelContainer != null) {
        ControlPanelContainer.setVisible(false);
      }
      Result = DiasdemServer.executeBlockingTask((Project)DiasdemProject,
      "org.hypknowsys.diasdem.tasks.project.newProject.NewProjectTask",
      parameter);
      if (Result.getStatus() == TaskResult.NO_RESULT) {
        DiasdemGui.logWarningMessage(Result.getLogMessage());
      }
      if (Result.getStatus() == TaskResult.FINAL_RESULT) {
        if (Parameter_Panel != null) {
          Parameter_Panel.saveCurrentParameterSettingsAsDefaults();
        }
        DiasdemGui.logInfoMessage(Result.getLogMessage());
        DiasdemProject = ((NewProjectResult)Result).getDiasdemProject();
        DiasdemGui.setProject((Project)DiasdemProject);
        PriorDiasdemGuiStatus = GuiClient.PROJECT_OPENED_NO_TASK_RUNNING;
        DiasdemGui.getJFrame().setTitle(DiasdemGuiPreferences
        .getProperty("DIASDEM_WORKBENCH_TITLE") + " ["
        + parameter.getProjectName() + "]");
        DiasdemGuiPreferences.setMruProjectFileName(
        DiasdemProject.getProjectFileName());
        DiasdemGuiPreferences.setMruProjectDirectory(
        DiasdemProject.getProjectDirectory());
        DiasdemGuiPreferences.setMruParameterDirectory(
        DiasdemProject.getParameterDirectory());
        DiasdemGuiPreferences.quickSave();
      }
      this.setControlPanelContainerClosed(true);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}