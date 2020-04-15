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

package org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection;

import java.lang.reflect.*;
import java.beans.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class CreateDocumentCollectionControlPanel extends DiasdemActionsControlPanel {
  
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
  "Create Document Collection";
  private String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection.CreateDocumentCollectionTask";
  private String INITIAL_PROGRESS_MESSAGE =
  "Please Wait: Creating Document Collection ...";
  private String INITIAL_PROGRESS_NOTE =
  "Initial Preparations";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateDocumentCollectionControlPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateDocumentCollectionControlPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
  
  public void actionPerformed(ActionEvent e) {
    
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();
    
    if (ActionSource == GuiTimer) {
      super.timerEvent(TASK_LABEL);
    } else if (ActionCommand.equals("OK")) {
      this.ok();
    } else if (ActionCommand.equals("Cancel")) {
      this.setControlPanelContainerClosed(true);
    } else if (ActionCommand.equals("KInternalFrame:EscapePressed")) {
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
    
    Parameter_Panel = new CreateDocumentCollectionParameterPanel(DiasdemServer,
    DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void ok() {
    
    CreateDocumentCollectionParameter parameter = 
    (CreateDocumentCollectionParameter)Parameter_Panel.getTaskParameter();
    AbstractValidatedTaskParameter validatedParameter =
    DiasdemServer.validateTaskParameter(DiasdemProject,
    TASK_CLASS_NAME, parameter);
    
    boolean commit = this.isValidParameter(validatedParameter);
    if (commit) {
      CloseIfEscapeIsPressed = false;
      if (ControlPanelContainer != null) {
        ControlPanelContainer.setVisible(false);
      }
      Result = DiasdemServer.executeBlockingTask(DiasdemProject,
      TASK_CLASS_NAME, parameter);
      if (Result.getStatus() == TaskResult.NO_RESULT) {
        DiasdemGui.logWarningMessage(Result.getLogMessage());
        JOptionPane.showInternalMessageDialog(
        DiasdemGui.getKDesktopPane(), Result.getDescription(),
        TASK_LABEL, JOptionPane.WARNING_MESSAGE);
      }
      if (Result.getStatus() == TaskResult.FINAL_RESULT) {
        if (Parameter_Panel != null) {
          Parameter_Panel.saveCurrentParameterSettingsAsDefaults();
        }
        DiasdemGui.logInfoMessage(Result.getLogMessage());
        JOptionPane.showInternalMessageDialog(
        DiasdemGui.getKDesktopPane(), Result.getDescription(),
        TASK_LABEL, JOptionPane.INFORMATION_MESSAGE);
        PriorDiasdemGuiStatus = GuiClient.PROJECT_OPENED_NO_TASK_RUNNING;
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