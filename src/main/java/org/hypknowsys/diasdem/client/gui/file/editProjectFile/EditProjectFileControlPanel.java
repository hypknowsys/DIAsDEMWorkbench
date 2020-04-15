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

package org.hypknowsys.diasdem.client.gui.file.editProjectFile;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskControlPanelContainer;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.server.Server;

/**
 * @version 2.2, 31 December 2005
 * @author Karsten Winkler
 */

public class EditProjectFileControlPanel extends DiasdemActionsControlPanel {
  
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
  
  public EditProjectFileControlPanel() {
    
    super();
    
    ControlPanelContainerIsVisible = false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public EditProjectFileControlPanel(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences) {
    
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
    ControlPanelContainerIsVisible = false;
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
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setControlPanelContainer(TaskControlPanelContainer
  pControlPanelContainer) {
    
    ControlPanelContainer = pControlPanelContainer;
    this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
    "OK"));
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    Parameter_Panel = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void ok() {
    
    JFileChooser fileChooser = new JFileChooser(
    DiasdemProject.getProperty("PROJECT_DIRECTORY"));
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setDialogTitle("Edit Project File");
    int result = fileChooser.showOpenDialog(ControlPanelContainer
    .getParentJFrame());
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      String filename = selectedFile.getAbsolutePath();
      try {
        if (filename != null) {
          Runtime.getRuntime().exec(DiasdemGuiPreferences.getStringProperty(
          "EXTERNAL_EDITOR") + " " + filename);
        }
        else {
          Runtime.getRuntime().exec(DiasdemGuiPreferences.getStringProperty(
          "EXTERNAL_EDITOR"));
        }
      }
      catch (IOException e1) {
        JOptionPane.showMessageDialog(ControlPanelContainer.getParentJFrame(),
        "Your preferred text editor cannot be launched!"
        + "\nPlease check your DIAsDEMgui preferences.",
        "Edit Parameter File", JOptionPane.WARNING_MESSAGE);
      }
    }
    PriorDiasdemGuiStatus = DO_NOT_MODIFY_DIASDEM_GUI_STATUS;
    this.setControlPanelContainerClosed(true);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}