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

package org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ExportOracle8iSqlScriptsParameterPanel
extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ExportOracle8iSqlScriptsParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField DtdFile_Text = null;
  private KButtonPanel DtdFile_Button = null;
  private KComboBox DatabaseType_Combo = null;
  private KTextField DatabaseUser_Text = null;
  private KTextField DatabasePassword_Text = null;
  private KTextField ScriptDirectory_Text = null;
  private KButtonPanel ScriptDirectory_Button = null;
  
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
  
  public ExportOracle8iSqlScriptsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ExportOracle8iSqlScriptsParameterPanel(Server pDiasdemServer,
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
  
  public void actionPerformed(ActionEvent e) {
    
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();
    
    if (ActionCommand.equals("CollectionButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("DtdFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      DtdFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Conceptual DTD File",
      DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("ScriptDirectoryButton")) {

      CurrentProjectDirectory = this.directoryButtonClicked(
      ScriptDirectory_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing SQL Script Directory");
      
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
    
    return "Export Oracle 8i SQL Scripts";
    
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
    
    ExportOracle8iSqlScriptsParameter parameter = new ExportOracle8iSqlScriptsParameter(
    Collection_Text.getText(),
    DtdFile_Text.getText(),
    DatabaseType_Combo.getSelectedString(),
    DatabaseUser_Text.getText(),
    DatabasePassword_Text.getText(),
    ScriptDirectory_Text.getText());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ExportOracle8iSqlScriptsParameter) {
      CastParameter = (ExportOracle8iSqlScriptsParameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_CONCEPTUAL_DTD_FILE",
    DtdFile_Text.getText());
    if (DatabaseUser_Text.getText().length() > 0)
      DiasdemProject.setProperty("DEFAULT_DATABASE_USER",
      DatabaseUser_Text.getText());
    if (DatabasePassword_Text.getText().length() > 0)
      DiasdemProject.setProperty("DEFAULT_DATABASE_PASSWORD",
      DatabasePassword_Text.getText());
    DiasdemProject.setProperty("DEFAULT_SQL_SCRIPT_DIRECTORY",
    ScriptDirectory_Text.getText());
    DiasdemProject.setProperty("DEFAULT_DATABASE_TYPE_INDEX",
    String.valueOf(DatabaseType_Combo.getSelectedIndex()));
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
    
    DatabaseType_Combo = new KComboBox(ExportOracle8iSqlScriptsParameter
    .DATABASE_TYPES.length, true, "DatabaseTypeCombo", this);
    for (int i = 0; i < ExportOracle8iSqlScriptsParameter.DATABASE_TYPES
    .length; i++) {
      DatabaseType_Combo.addItem(ExportOracle8iSqlScriptsParameter
      .DATABASE_TYPES[i], false);
    }

    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      DtdFile_Text = new KTextField(CastParameter
      .getDtdFileName(), 30);
      DatabaseUser_Text = new KTextField(CastParameter
      .getDatabaseUser(), 30);
      DatabasePassword_Text = new KTextField(CastParameter
      .getDatabasePassword(), 30);
      ScriptDirectory_Text = new KTextField(CastParameter
      .getScriptDirectory(), 30);
      if (CastParameter.getDatabaseType() >= 0 
      && CastParameter.getDatabaseType()
      < ExportOracle8iSqlScriptsParameter.DATABASE_TYPES.length) {
        DatabaseType_Combo.setSelectedIndex(CastParameter.getDatabaseType());
      }
      else {
        DatabaseType_Combo.setSelectedIndex(ExportOracle8iSqlScriptsParameter
        .ORACLE_8I);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      DtdFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CONCEPTUAL_DTD_FILE"), 30);
      DatabaseUser_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_DATABASE_USER"), 30);
      DatabasePassword_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_DATABASE_PASSWORD"), 30);
      ScriptDirectory_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_SQL_SCRIPT_DIRECTORY"), 30);
      if (DiasdemProject.getIntProperty("DEFAULT_DATABASE_TYPE_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_DATABASE_TYPE_INDEX")
      < ExportOracle8iSqlScriptsParameter.DATABASE_TYPES.length) {
        DatabaseType_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_DATABASE_TYPE_INDEX"));
      }
      else {
        DatabaseType_Combo.setSelectedIndex(ExportOracle8iSqlScriptsParameter
        .ORACLE_8I);
      }
    }
    Collection_Text.setCaretAtEnding();    
    DtdFile_Text.setCaretAtEnding();  
    DatabaseUser_Text.setCaretAtEnding();  
    DatabasePassword_Text.setCaretAtEnding();  
    ScriptDirectory_Text.setCaretAtEnding();  
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
      KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");
    
    DtdFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    DtdFile_Button.addSingleButton("...", 
      KeyEvent.VK_P, true, true, "DtdFileButton", this,
    "Click this button to select the conceptual DTD file."); 
    
    ScriptDirectory_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    ScriptDirectory_Button.addSingleButton("...", 
      KeyEvent.VK_S, true, true, "ScriptDirectoryButton", this,
    "Click this button to select the local script directory.");

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
    Parameter_Panel.addLabel("Conceptual DTD File:", 0, 2, KeyEvent.VK_P,
      DtdFile_Button.getDefaultButton(), true,
    "Task input: This file contains meta-data about the conceptual" +
    "XML DTD");
    Parameter_Panel.addComponent(DtdFile_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(DtdFile_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Database Type:", 0, 4, KeyEvent.VK_T,
      DatabaseType_Combo, true,
    "Task input: Select the appropriate database type.");
    Parameter_Panel.addComponent(DatabaseType_Combo, 2, 4, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Database User:", 0, 6, KeyEvent.VK_U,
      DatabaseUser_Text, true,
    "Task input: Type in the database user name to access the database.");
    Parameter_Panel.addComponent(DatabaseUser_Text, 2, 6, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Database Password:", 0, 8, KeyEvent.VK_D,
      DatabasePassword_Text, true,
    "Task input: Type in the database user's password to access the database.");
    Parameter_Panel.addComponent(DatabasePassword_Text, 2, 8, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("SQL Script Directory:", 0, 10, KeyEvent.VK_S,
      ScriptDirectory_Button.getDefaultButton(), true,
    "Task output: All SQL scripts will copied into this local directory.");
    Parameter_Panel.addComponent(ScriptDirectory_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(ScriptDirectory_Button, 4, 10);

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