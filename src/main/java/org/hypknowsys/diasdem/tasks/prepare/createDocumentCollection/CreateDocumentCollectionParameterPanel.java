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

public class CreateDocumentCollectionParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private CreateDocumentCollectionParameter CastParameter = null;
  
  private KTextField Name_Text = null;
  private KTextField FileName_Text = null;
  private KButtonPanel FileName_Button = null;
  private KTextField Directory_Text = null;
  private KButtonPanel Directory_Button = null;
  private KTextField DocumentsPerVolume_Text = null;
  
  private KBorderPanel Notes_Panel = null;
  private KScrollTextArea Notes_Text = null;
  
  private File CurrentCollectionDirectory = null;
  
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
  
  public CreateDocumentCollectionParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateDocumentCollectionParameterPanel(Server pDiasdemServer,
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
    
    if (ActionCommand.equals("DirectoryButton")) {
      
      CurrentCollectionDirectory = this.directoryButtonClicked(Directory_Text,
      CurrentCollectionDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Collection Directory");

    }
    else if (ActionCommand.equals("FileNameButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(FileName_Text, 
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Collection File to be Created",
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
    
    return "Create Document Collection";
    
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
    
    CreateDocumentCollectionParameter parameter =
    new CreateDocumentCollectionParameter(
    Name_Text.getText(),
    FileName_Text.getText(),
    Directory_Text.getText(),
    Notes_Text.getText(),
    Tools.string2Int(DocumentsPerVolume_Text.getText()));
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof CreateDocumentCollectionParameter) {
      CastParameter = (CreateDocumentCollectionParameter)pTaskParameter;
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
    
    DiasdemProject.setProperty("DEFAULT_COLLECTION_DIRECTORY",
    Directory_Text.getText());
    DiasdemProject.setProperty("DEFAULT_COLLECTION_FILE",
    FileName_Text.getText());
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Name_Text != null) {
      return Name_Text;
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
      Name_Text = new KTextField(CastParameter
      .getCollectionName(), 30);
      FileName_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      Directory_Text = new KTextField(CastParameter
      .getCollectionDirectory(), 30);
      Notes_Text = new KScrollTextArea(CastParameter
      .getCollectionNotes());
      DocumentsPerVolume_Text = new KTextField(CastParameter
      .getDocumentsPerVolume() + "", 30);
    }
    else {
      Name_Text = new KTextField("<DefaultCollectionName>", 30);
      FileName_Text = new KTextField(Tools.ensureTrailingSlash(DiasdemProject
      .getProperty("PROJECT_DIRECTORY")) + "collection"
      + DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION, 30);
      Directory_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_DIRECTORY"), 30);
      Notes_Text = new KScrollTextArea("");
      DocumentsPerVolume_Text = new KTextField("10", 30);
    }
    Name_Text.setCaretAtEnding();
    FileName_Text.setCaretAtEnding();
    Directory_Text.setCaretAtEnding();
    Notes_Text.setCaretAtEnding();
    DocumentsPerVolume_Text.setCaretAtEnding();
    
    Directory_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    Directory_Button.addSingleButton("...",
    KeyEvent.VK_D, true, true, "DirectoryButton", this,
    "Click this button to select the collection directory.");
    
    FileName_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    FileName_Button.addSingleButton("...",
    KeyEvent.VK_F, true, true, "FileNameButton", this,
    "Click this button to select the collection file.");
    
    Parameter_Panel = new KGridBagPanel(12, 12, 11, 11);
    Parameter_Panel.startFocusForwarding(Name_Text);
    
    Parameter_Panel.addLabel("Collection Name:", 0, 0, KeyEvent.VK_C,
    Name_Text, true,
    "Task input: Select a concise name for the new DIAsDEM collection.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Name_Text, 2, 0,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Collection File:", 0, 2, KeyEvent.VK_F,
    FileName_Button.getDefaultButton(), true,
    "Task output: This collection file will contain collection-specific " +
    "meta-data.");
    Parameter_Panel.addComponent(FileName_Text, 2, 2);
    Parameter_Panel.addBlankColumn(3, 2, 12);
    Parameter_Panel.addKButtonPanel(FileName_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Collection Directory:", 0, 4, KeyEvent.VK_D,
    Directory_Button.getDefaultButton(), true,
    "Task output: This directory will subsequently contain DIAsDEM documents.");
    Parameter_Panel.addComponent(Directory_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(Directory_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Documents Per Volume:", 0, 6, KeyEvent.VK_V,
    Name_Text, true,
    "Task input: Input an appropriate parameter value such as 10.");
    Parameter_Panel.addComponent(DocumentsPerVolume_Text, 2, 6,
    new Insets(0, 0, 0, 0), 3, 1);
    
    KBorderPanel ParameterNorth_Panel = new KBorderPanel(0, 0, 0, 0);
    ParameterNorth_Panel.startFocusForwarding(Parameter_Panel);
    ParameterNorth_Panel.addNorth(Parameter_Panel);
    
    Notes_Panel = new KBorderPanel(12, 12, 11, 11);
    Notes_Panel.startFocusForwarding(Notes_Text);
    Notes_Panel.addCenter(Notes_Text);
    
    KTabbedPane Tabbed_Pane = new KTabbedPane();
    Tabbed_Pane.addTab("Properties", ParameterNorth_Panel, KeyEvent.VK_P, 
    true, true);
    Tabbed_Pane.addTab("Notes", Notes_Panel, KeyEvent.VK_N, 
    true, false);
    Tabbed_Pane.startFocusForwardingToSelectedTab();
    
    this.removeAll();
    this.validate();
    this.addCenter(Tabbed_Pane);
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