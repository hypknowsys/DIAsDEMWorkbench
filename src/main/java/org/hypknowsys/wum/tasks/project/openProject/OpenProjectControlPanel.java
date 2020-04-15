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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;
import org.hypknowsys.wum.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class OpenProjectControlPanel extends WumActionsControlPanel {
  
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
  
  public OpenProjectControlPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public OpenProjectControlPanel(Server pWumServer, Project pWumProject,
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
    
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
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
  
  public void actionPerformed(ActionEvent e) {
    
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();
    
    if ( ActionCommand.equals("OK") ) {
      this.ok();
    } else if ( ActionCommand.equals("Cancel") ) {
      this.setControlPanelContainerClosed(true);
    } if (ActionCommand.equals("KInternalFrame:EscapePressed")) {
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
    WumGui.setGuiStatus(GuiClient.PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    Parameter_Panel = new OpenProjectParameterPanel(WumServer,
    WumProject, WumGui, WumGuiPreferences);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void ok() {
    
    OpenProjectParameter parameter = (OpenProjectParameter)Parameter_Panel
    .getTaskParameter();
    AbstractValidatedTaskParameter validatedParameter =
    WumServer.validateTaskParameter((Project)WumProject,
    "org.hypknowsys.wum.tasks.project.openProject.OpenProjectTask", parameter);
    
    boolean commit = this.isValidParameter(validatedParameter);
    if (commit) {
      CloseIfEscapeIsPressed = false;
      if (ControlPanelContainer != null) {
        ControlPanelContainer.setVisible(false);
      }
      Result = WumServer.executeBlockingTask((Project)WumProject,
      "org.hypknowsys.wum.tasks.project.openProject.OpenProjectTask", parameter);
      if (Result.getStatus() == TaskResult.NO_RESULT) {
        WumGui.logWarningMessage(Result.getLogMessage());
      }
      if (Result.getStatus() == TaskResult.FINAL_RESULT) {
        if (Parameter_Panel != null) {
          Parameter_Panel.saveCurrentParameterSettingsAsDefaults();
        }
        WumGui.logInfoMessage(Result.getLogMessage());
        WumProject = ( (OpenProjectResult)Result ).getWumProject();
        WumGui.setProject((Project)WumProject);
        PriorWumGuiStatus = GuiClient.PROJECT_OPENED_NO_TASK_RUNNING;
        WumGui.getJFrame().setTitle(WumGuiPreferences
        .getProperty("WUM_WORKBENCH_TITLE") + " [" +
        WumProject.getProjectName() + "]");
        WumGuiPreferences.setMruProjectFileName( 
        WumProject.getProjectFileName());
        WumGuiPreferences.setMruProjectDirectory( 
        WumProject.getProjectDirectory());
        WumGuiPreferences.setMruParameterDirectory(
        WumProject.getParameterDirectory());
        WumGuiPreferences.quickSave();
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
  
  public static void main(String args[]) {}
  
}