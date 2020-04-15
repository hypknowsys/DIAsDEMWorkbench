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

package org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files;

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

public class ImportReuters21578FilesParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportReuters21578FilesParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField SourceDirectory_Text = null;
  private KButtonPanel SourceDirectory_Button = null;
  private KTextField FileNameFilter_Text = null;
  private JCheckBox IncludeSubdirectories_CheckBox = null;
  private KTextField TopicOfCollection_Text = null;
  
  private File CurrentTextFileDirectory = null;
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
  
  public ImportReuters21578FilesParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportReuters21578FilesParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if ( ActionCommand.equals("CollectionButton") ) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
    
    } else if (ActionCommand.equals("SourceDirectoryButton")) {
      
      CurrentTextFileDirectory = this.directoryButtonClicked(
      SourceDirectory_Text, CurrentTextFileDirectory, 
      "DEFAULT_REUTERS_21578_FILE_DIRECTORY", "Select", KeyEvent.VK_S, null, 
      "Select Existing Reuters-21578 Directory");
      
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
    
    return "Import Reuters-21578 Files";
    
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
    
    ImportReuters21578FilesParameter parameter = new ImportReuters21578FilesParameter(
    Collection_Text.getText(),
    SourceDirectory_Text.getText(),
    FileNameFilter_Text.getText(), false,
    TopicOfCollection_Text.getText());
    if (IncludeSubdirectories_CheckBox.isSelected()) {
      parameter.setIncludeSubdirectories(true);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ImportReuters21578FilesParameter) {
      CastParameter = (ImportReuters21578FilesParameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_REUTERS_21578_FILE_DIRECTORY",
    SourceDirectory_Text.getText());
    DiasdemProject.setProperty("DEFAULT_REUTERS_21578_COLLECTION_TOPIC",
    TopicOfCollection_Text.getText());
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
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
     KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");    
    
    SourceDirectory_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    SourceDirectory_Button.addSingleButton("...",
    KeyEvent.VK_R, true, true, "SourceDirectoryButton", this,
    "Click this button to select the Reuters-21578 file directory.");
    
    IncludeSubdirectories_CheckBox = new KCheckBox(
    "Include Subdirectories", false, true, 
    "IncludeSubdirectories", this, KeyEvent.VK_I, 
    "If this box is checked, files will be search in subdirectories "
    + "of Reuters-21578 directory.");

    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      SourceDirectory_Text = new KTextField(CastParameter
      .getSourceDirectory(), 30);
      FileNameFilter_Text = new KTextField(CastParameter
      .getFileNameFilter(), 30);
      TopicOfCollection_Text = new KTextField(CastParameter
      .getTopicOfCollection(), 30);
      if (CastParameter.includeSubdirectories()) {
        IncludeSubdirectories_CheckBox.setSelected(true);
      }
      else {
        IncludeSubdirectories_CheckBox.setSelected(false);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      SourceDirectory_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_REUTERS_21578_FILE_DIRECTORY"), 30);
      FileNameFilter_Text = new KTextField(".sgm", 30);
      TopicOfCollection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_REUTERS_21578_COLLECTION_TOPIC"), 30);
    }
    Collection_Text.setCaretAtEnding();
    SourceDirectory_Text.setCaretAtEnding();
    FileNameFilter_Text.setCaretAtEnding();
    TopicOfCollection_Text.setCaretAtEnding();
    
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
    Parameter_Panel.addLabel("Reuters-21578 Directory:", 0, 2, KeyEvent.VK_R,
    SourceDirectory_Button.getDefaultButton(), true,
    "Task input: This directory must contain " +
    "original Reuters-21578 SGML files to be imported.");
    Parameter_Panel.addComponent(SourceDirectory_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(SourceDirectory_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addComponent( IncludeSubdirectories_CheckBox, 2, 4,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("File Name Extension:", 0, 6, KeyEvent.VK_N,
    FileNameFilter_Text, true,
    "Task input: Reuters-21578 files to be imported " +
    "must have this file name extension.");
    Parameter_Panel.addComponent( FileNameFilter_Text, 2, 6,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Topic of Collection:", 0, 8, KeyEvent.VK_T,
    TopicOfCollection_Text, true,
    "Task input: Reuters-21578 news to be imported " +
    "must have this topic (e.g., earn).");
    Parameter_Panel.addComponent( TopicOfCollection_Text, 2, 8,
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