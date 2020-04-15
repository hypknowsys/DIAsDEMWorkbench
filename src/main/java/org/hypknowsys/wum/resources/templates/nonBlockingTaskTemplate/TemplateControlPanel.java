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

import java.awt.event.ActionEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.server.Server;
import org.hypknowsys.wum.client.gui.WumActionsControlPanel;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class TemplateControlPanel extends WumActionsControlPanel {
  
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
  
  private String TASK_LABEL =
  "Template Non-Blocking Task";
  private String TASK_CLASS_NAME =
  "org.hypknowsys.wum.resources.templates.nonBlockingTaskTemplate"
  + ".TemplateTask";
  private String INITIAL_PROGRESS_MESSAGE =
  "Please Wait: Executing Template Non-Blocking Task ...";
  private String INITIAL_PROGRESS_NOTE =
  "Initial Preparations";
  
  // comment out subsequent code, if there is no help available
  private String WUM_TASK_HELP_RESOURCE = 
  "org/hypknowsys/wum/resources/templates"
  + "/nonBlockingTaskTemplate/Template.html";
  // un-comment subsequent code, if there is no help available
  // private String WUM_TASK_HELP_RESOURCE = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TemplateControlPanel() {
    
    super();
    
    WumTaskHelpResource = WUM_TASK_HELP_RESOURCE;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TemplateControlPanel(Server pWumServer, Project pWumProject, 
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
    
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
    WumTaskHelpResource = WUM_TASK_HELP_RESOURCE;
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
    
    if (ActionSource == GuiTimer) {
      super.timerEvent(TASK_LABEL);
    }
    else if (ActionCommand.equals("OK")) {
      super.startNonBlockingTask(TASK_CLASS_NAME, TASK_LABEL, 
      INITIAL_PROGRESS_MESSAGE, INITIAL_PROGRESS_NOTE, 0, 100, true, "Stop");
    }
    else if (ActionCommand.equals("Cancel")) {
      WumServer.stopNonBlockingTask();
      this.setControlPanelContainerClosed(true);
    }
    else if (ActionCommand.equals("Help")) {
      this.help();
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
    WumGui.setGuiStatus(GuiClient.PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    Parameter_Panel = new TemplateParameterPanel(WumServer,
    WumProject, WumGui, WumGuiPreferences);
    
  }
  
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