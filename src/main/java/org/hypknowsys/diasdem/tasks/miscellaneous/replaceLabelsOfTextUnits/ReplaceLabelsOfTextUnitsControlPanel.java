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

package org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits;

import java.awt.event.ActionEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.server.Server;

/**
 * @version 2.2, 7 May 2006
 * @author Karsten Winkler
 */

public class ReplaceLabelsOfTextUnitsControlPanel
extends DiasdemActionsControlPanel {
  
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
  "Replace Labels of Text Units";
  private String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits"
  + ".ReplaceLabelsOfTextUnitsTask";
  private String INITIAL_PROGRESS_MESSAGE =
  "Please Wait: Replacing Labels of Text Units ...";
  private String INITIAL_PROGRESS_NOTE =
  "Initial Preparations";
  
  private String DIASDEM_TASK_HELP_RESOURCE =
  "org/hypknowsys/diasdem/tasks/miscellaneous"
  + "/replaceLabelsOfTextUnits/ReplaceLabelsOfTextUnits.html";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceLabelsOfTextUnitsControlPanel() {
    
    super();
    
    DiasdemTaskHelpResource = DIASDEM_TASK_HELP_RESOURCE;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceLabelsOfTextUnitsControlPanel(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences) {
    
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
    DiasdemTaskHelpResource = DIASDEM_TASK_HELP_RESOURCE;
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
      DiasdemServer.stopNonBlockingTask();
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
    DiasdemGui.setGuiStatus(GuiClient.PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    Parameter_Panel = new ReplaceLabelsOfTextUnitsParameterPanel(DiasdemServer,
    DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
    
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