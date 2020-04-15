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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.diasdem.core.DIAsDEMproject;
import org.hypknowsys.misc.swing.KBorderPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KScrollTextArea;
import org.hypknowsys.misc.swing.KTabbedPane;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class NewProjectParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NewProjectParameter CastParameter = null;
  
  private KTextField ProjectName_Text = null;
  private KTextField ProjectFileName_Text = null;
  private KButtonPanel ProjectFileName_Button = null;
  private KTextField ProjectDirectory_Text = null;
  private KButtonPanel ProjectDirectory_Button = null;
  private KTextField ParameterDirectory_Text = null;
  private KButtonPanel ParameterDirectory_Button = null;
  
  private KBorderPanel Notes_Panel = null;
  private KScrollTextArea ProjectNotes_Text = null;
  
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
  
  public NewProjectParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NewProjectParameterPanel(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences) {
    
    super();
    
    this.setContext(pDiasdemServer, pDiasdemProject, pDiasdemGui,
    pDiasdemGuiPreferences);
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
    
    if (ActionCommand.equals("ProjectFileName_Button")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(ProjectFileName_Text,
      CurrentProjectDirectory, "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Project File to be Created",
      DIAsDEMproject.PROJECT_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("ProjectDirectory_Button")) {
      
      CurrentProjectDirectory = this.directoryButtonClicked(
      ProjectDirectory_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null,
      "Select Existing Directory of Project Files");
      
    }
    else if (ActionCommand.equals("ParameterDirectory_Button")) {
      
      CurrentParameterDirectory = this.directoryButtonClicked(
      ParameterDirectory_Text, CurrentParameterDirectory,
      "PARAMETER_DIRECTORY", "Select", KeyEvent.VK_S, null,
      "Select Existing Directory of Parameter Files");
      
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskParameterPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
    
    super.initialize();
    
    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY));
    
    this.createParameterPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "New Project";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    NewProjectParameter parameter = new NewProjectParameter(
    ProjectName_Text.getText(), ProjectNotes_Text.getText(),
    ProjectFileName_Text.getText(), ProjectDirectory_Text.getText(),
    ParameterDirectory_Text.getText());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof NewProjectParameter) {
      CastParameter = (NewProjectParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    IsEnabled = true;
    this.createParameterPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {
    
    DiasdemGuiPreferences.setMruProjectFileName(
    ProjectFileName_Text.getText());
    DiasdemGuiPreferences.setMruProjectDirectory(
    ProjectDirectory_Text.getText());
    DiasdemGuiPreferences.setMruParameterDirectory(
    ParameterDirectory_Text.getText());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (ProjectName_Text != null) {
      return ProjectName_Text;
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setEnabled(boolean pEnabled) {
    
    IsEnabled = pEnabled;
    this.setComponentStatus();
    super.setEnabled(IsEnabled);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createParameterPanel() {
    
    ProjectFileName_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ProjectFileName_Button.addSingleButton("...",
    KeyEvent.VK_F, true, true, "ProjectFileName_Button", this,
    "Click this button to select the project file.");
    
    ProjectDirectory_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ProjectDirectory_Button.addSingleButton("...",
    KeyEvent.VK_D, true, true, "ProjectDirectory_Button", this,
    "Click this button to select the project directory.");
    
    ParameterDirectory_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ParameterDirectory_Button.addSingleButton("...",
    KeyEvent.VK_P, true, true, "ParameterDirectory_Button", this,
    "Click this button to select the parameter directory.");
    
    if (CastParameter != null) {
      ProjectName_Text = new KTextField(CastParameter
      .getProjectName(), 30);
      ProjectName_Text.setCaretAtEnding();
      ProjectFileName_Text = new KTextField(CastParameter
      .getProjectFileName(), 30);
      ProjectFileName_Text.setCaretAtEnding();
      ProjectDirectory_Text = new KTextField(CastParameter
      .getProjectDirectory(), 30);
      ProjectDirectory_Text.setCaretAtEnding();
      ParameterDirectory_Text = new KTextField(CastParameter
      .getParameterDirectory(), 30);
      ParameterDirectory_Text.setCaretAtEnding();
      ProjectNotes_Text = new KScrollTextArea(CastParameter
      .getProjectNotes());
      ProjectNotes_Text.setCaretAtEnding();
    }
    else {
      ProjectName_Text = new KTextField(DiasdemProject.getProperty(
      "PROJECT_NAME"), 30);
      ProjectName_Text.setCaretAtEnding();
      ProjectFileName_Text = new KTextField(DiasdemProject.getProperty(
      "PROJECT_FILE_NAME"), 30);
      ProjectFileName_Text.setCaretAtEnding();
      ProjectDirectory_Text = new KTextField(DiasdemProject.getProperty(
      "PROJECT_DIRECTORY"), 30);
      ProjectDirectory_Text.setCaretAtEnding();
      ParameterDirectory_Text = new KTextField(DiasdemProject.getProperty(
      "PARAMETER_DIRECTORY"), 30);
      ParameterDirectory_Text.setCaretAtEnding();
      ProjectNotes_Text = new KScrollTextArea(DiasdemProject.getProperty(
      "PROJECT_NOTES"));
      ProjectNotes_Text.setCaretAtEnding();
    }
    
    Parameter_Panel = new KGridBagPanel(12, 12, 11, 11);
    Parameter_Panel.startFocusForwarding(ProjectName_Text);
    
    Parameter_Panel.addLabel("Project Name:", 0, 0, KeyEvent.VK_N,
    ProjectName_Text, true, "Required input: Unique name of the new project");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(ProjectName_Text, 2, 0,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Project File Name:", 0, 2, KeyEvent.VK_F,
    ProjectFileName_Button.getDefaultButton(), true,
    "Task output: This file will contain project-related meta-data.");
    Parameter_Panel.addComponent(ProjectFileName_Text, 2, 2);
    Parameter_Panel.addBlankColumn(3, 2, 12);
    Parameter_Panel.addKButtonPanel(ProjectFileName_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Project Directory:", 0, 4, KeyEvent.VK_D,
    ProjectDirectory_Button.getDefaultButton(), true,
    "Task input: This directory will contain project-related files.");
    Parameter_Panel.addComponent(ProjectDirectory_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(ProjectDirectory_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Parameter Directory:", 0, 6, KeyEvent.VK_P,
    ParameterDirectory_Button.getDefaultButton(), true,
    "Task input: Default directory for selecting parameter files");
    Parameter_Panel.addComponent(ParameterDirectory_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(ParameterDirectory_Button, 4, 6);
    
    KBorderPanel parameterNorth_Panel = new KBorderPanel(0, 0, 0, 0);
    parameterNorth_Panel.startFocusForwarding(Parameter_Panel);
    parameterNorth_Panel.addNorth(Parameter_Panel);
    
    Notes_Panel = new KBorderPanel(12, 12, 11, 11);
    Notes_Panel.startFocusForwarding(ProjectNotes_Text);
    Notes_Panel.addCenter(ProjectNotes_Text);
    
    KTabbedPane tabbed_Pane = new KTabbedPane();
    tabbed_Pane.addTab("Properties", parameterNorth_Panel, KeyEvent.VK_R,
    true, true);
    tabbed_Pane.addTab("Notes", Notes_Panel, KeyEvent.VK_T,
    true, false);
    tabbed_Pane.startFocusForwardingToSelectedTab();
    
    this.removeAll();
    this.validate();
    this.addCenter(tabbed_Pane);
    this.validate();
    this.setComponentStatus();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}