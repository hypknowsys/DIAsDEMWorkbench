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

package org.hypknowsys.wum.tasks.prepare.sessionizeLogFiles;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class SessionizeLogFilesParameterPanel extends WumParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private SessionizeLogFilesParameter CastParameter = null;
  
  private KRadioButtonGroup Criteria_Group = null;
  private KTextField Threshold_Text = null;
  
  private String CurrentThreshold = "0/00:30:00";
  
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
  
  public SessionizeLogFilesParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public SessionizeLogFilesParameterPanel(Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
    
    super();
    
    this.setContext(pWumServer, pWumProject, pWumGui,
    pWumGuiPreferences);
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
    
    if (ActionCommand.equals("Session"))  {
    
      this.Threshold_Text.setText(CurrentThreshold);
      this.Threshold_Text.setEnabled(true);
      
    } else if (ActionCommand.equals("PageView"))  {
    
      this.Threshold_Text.setText(CurrentThreshold);
      this.Threshold_Text.setEnabled(true);
      
    } else if (ActionCommand.equals("WUMprep"))  {
    
      CurrentThreshold = this.Threshold_Text.getText();
      this.Threshold_Text.setText("none");
      this.Threshold_Text.setEnabled(false);
      
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
    
    return "Sessionize Log Files";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogSSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    SessionizeLogFilesParameter parameter = new SessionizeLogFilesParameter();
    
    if (Criteria_Group.getSelectedActionCommand().equals("Session")) {
      parameter.setSessionThreshold(Threshold_Text.getText().trim());
      parameter.setSessionCriterion(parameter.MAXIMUM_SESSION_DURATION);
    }
    else if (Criteria_Group.getSelectedActionCommand().equals("PageView")) {
      parameter.setSessionThreshold(Threshold_Text.getText().trim());
      parameter.setSessionCriterion(parameter.MAXIMUM_PAGE_VIEW_TIME);
    }
    else if (Criteria_Group.getSelectedActionCommand().equals("WUMprep")) {
      parameter.setSessionThreshold("");
      parameter.setSessionCriterion(parameter.WUMPREP_SESSION_ID);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof SessionizeLogFilesParameter) {
      CastParameter = (SessionizeLogFilesParameter)pTaskParameter;
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
    
    if (Criteria_Group.getSelectedActionCommand().equals("Session")) {
      WumProject.setIntProperty(
      "SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_CRITERION",
      CastParameter.MAXIMUM_SESSION_DURATION);
      WumProject.setProperty(
      "SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_THRESHOLD",
      Threshold_Text.getText().trim());
    }
    else if (Criteria_Group.getSelectedActionCommand().equals("PageView")) {
      WumProject.setIntProperty(
      "SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_CRITERION",
      CastParameter.MAXIMUM_PAGE_VIEW_TIME);
      WumProject.setProperty(
      "SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_THRESHOLD",
      Threshold_Text.getText().trim());
    }
    else if (Criteria_Group.getSelectedActionCommand().equals("WUMprep")) {
      WumProject.setIntProperty(
      "SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_CRITERION",
      CastParameter.WUMPREP_SESSION_ID);
    }
    WumProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Threshold_Text != null) {
      return Threshold_Text;
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
    
    Criteria_Group = new KRadioButtonGroup(3, 1);
    Criteria_Group.addFirstButton("Maximum Session Duration",
      KeyEvent.VK_S, true, true, "Session", this);
    Criteria_Group.addNextButton("Maximum Page View Time",
      KeyEvent.VK_P, true, false, "PageView", this);
    Criteria_Group.addLastButton("Use WUMprep Session ID",
      KeyEvent.VK_W, true, false, "WUMprep", this);
    Threshold_Text = new KTextField(CurrentThreshold); 

    int enableCriterionButton = 0;
    if (CastParameter != null) {
      enableCriterionButton = CastParameter.getSessionCriterion();
      Threshold_Text.setText(CastParameter.getSessionThreshold());
    }
    else {
      enableCriterionButton = WumProject.getIntProperty(
      "SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_CRITERION");
      Threshold_Text.setText(WumProject.getProperty(
      "SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_THRESHOLD"));
    }
    if (SessionizeLogFilesParameter.isValidSessionCriterion(
    enableCriterionButton)) {
      Criteria_Group.setSelected(enableCriterionButton, true);
    }
    if (Criteria_Group.getSelectedActionCommand().equals("WUMprep")) {
      this.Threshold_Text.setText("none");
      this.Threshold_Text.setEnabled(false);
    }
    Threshold_Text.setCaretAtEnding();    
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Threshold_Text);

    Parameter_Panel.setLabelAnchor( GridBagConstraints.NORTHWEST );
    Parameter_Panel.addLabel("Criterion:", 0, 0);
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Criteria_Group, 2, 0, new Insets(0, 0, 0, 0), 
      3, 1);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.setLabelAnchor( GridBagConstraints.WEST );
    Parameter_Panel.addLabel("Threshold:", 0, 2, KeyEvent.VK_T, Threshold_Text);
    Parameter_Panel.addComponent(Threshold_Text, 2, 2);
    Parameter_Panel.addBlankColumn(3, 2, 12);
    Parameter_Panel.addLabel("(D/HH:MM:SS)", 4, 2);
    
    this.removeAll();
    this.validate();
    this.addNorth(Parameter_Panel);
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
  
  public static void main(String args[]) {}
  
}