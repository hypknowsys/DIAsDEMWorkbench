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

package org.hypknowsys.diasdem.tasks.miscellaneous.rollbackProcessedTextUnits;

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
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class RollbackProcessedTextUnitsParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private RollbackProcessedTextUnitsParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField RollbackTextUnitsID_Text = null;
  
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
  
  public RollbackProcessedTextUnitsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public RollbackProcessedTextUnitsParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
  
  public void actionPerformed(ActionEvent e) {
    
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();
    
    if (ActionCommand.equals("CollectionFileName_Button")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
      
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
    
    return "Rollback Processed Text Units";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogSSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    RollbackProcessedTextUnitsParameter parameter = new RollbackProcessedTextUnitsParameter(
    Collection_Text.getText(),
    Tools.string2Int(RollbackTextUnitsID_Text.getText()));
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof RollbackProcessedTextUnitsParameter) {
      CastParameter = (RollbackProcessedTextUnitsParameter)pTaskParameter;
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
    
    DiasdemProject.setProperty("DEFAULT_COLLECTION_FILE",
    Collection_Text.getText());
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Collection_Text != null) {
      return Collection_Text;
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
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      RollbackTextUnitsID_Text = new KTextField(CastParameter
      .getRollbackTextUnitsID() + "", 30);
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      RollbackTextUnitsID_Text = new KTextField("0", 30);
    }
    Collection_Text.setCaretAtEnding();    
    RollbackTextUnitsID_Text.setCaretAtEnding();    
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "CollectionFileName_Button", this,
    "Click this button to select the collection file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Collection_Text);

    Parameter_Panel.addLabel("Collection File:", 0, 0, KeyEvent.VK_C,
    Collection_Button.getDefaultButton(), true,
    "Task input: This collection file contains references " +
    "to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Text Units Layer:", 0, 2);
    Parameter_Panel.addComponent(new JLabel(
    "Default Text Units Layer (Project Property)"),
    2, 2, new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 4, 11);
    Parameter_Panel.addLabel("Rollback ID:", 0, 5, KeyEvent.VK_R,
    RollbackTextUnitsID_Text, true,
    "Task input: This ID uniquely identifies the RollbackTextUnits " +
    "that should replace ProcessedTextUnits.");
    Parameter_Panel.addComponent(RollbackTextUnitsID_Text, 2, 5,
    new Insets(0, 0, 0, 0), 3, 1);

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