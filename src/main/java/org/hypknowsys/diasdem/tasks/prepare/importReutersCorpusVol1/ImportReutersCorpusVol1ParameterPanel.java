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

package org.hypknowsys.diasdem.tasks.prepare.importReutersCorpusVol1;

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
 * @version 2.1.0.5, 1 January 2004
 * @author Karsten Winkler
 */

public class ImportReutersCorpusVol1ParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportReutersCorpusVol1Parameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField SourceDirectory_Text = null;
  private KButtonPanel SourceDirectory_Button = null;
  private KTextField FileNameFilter_Text = null;
  private JCheckBox IncludeSubdirectories_CheckBox = null;
  private JCheckBox IncludeZipFileContents_CheckBox = null;
  private KTextField TopicOfCollection_Text = null;
  private KTextField RegionOfCollection_Text = null;
  
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
  
  public ImportReutersCorpusVol1ParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportReutersCorpusVol1ParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
      "DEFAULT_REUTERS_CORPUS_VOL1_FILE_DIRECTORY", "Select", KeyEvent.VK_S, null, 
      "Select Existing Reuters Corpus Directory");
      
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
    
    return "Import Reuters Corpus Vol. 1";
    
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
    
    ImportReutersCorpusVol1Parameter parameter = new ImportReutersCorpusVol1Parameter(
    Collection_Text.getText(),
    SourceDirectory_Text.getText(),
    FileNameFilter_Text.getText(), false,
    TopicOfCollection_Text.getText(),
    RegionOfCollection_Text.getText());
    if (IncludeSubdirectories_CheckBox.isSelected()) {
      parameter.setIncludeSubdirectories(true);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ImportReutersCorpusVol1Parameter) {
      CastParameter = (ImportReutersCorpusVol1Parameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_REUTERS_CORPUS_VOL1_FILE_DIRECTORY",
    SourceDirectory_Text.getText());
    DiasdemProject.setProperty("DEFAULT_REUTERS_CORPUS_VOL1_COLLECTION_TOPIC",
    TopicOfCollection_Text.getText());
    DiasdemProject.setProperty("DEFAULT_REUTERS_CORPUS_VOL1_COLLECTION_REGION",
    RegionOfCollection_Text.getText());
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
    KeyEvent.VK_D, true, true, "SourceDirectoryButton", this,
    "Click this button to select the Reuters Corpus directory.");
    
    IncludeSubdirectories_CheckBox = new KCheckBox(
    "Include Subdirectories", false, true, 
    "IncludeSubdirectories", this, KeyEvent.VK_I, 
    "If this box is checked, files will be search in subdirectories "
    + "of Reuters Corpus directory.");

    IncludeZipFileContents_CheckBox = new KCheckBox(
    "Include Contents of ZIP Archives", true, false, 
    "IncludeZipFileContents", this, KeyEvent.VK_Z, 
    "If this box is checked, files will be searched in ZIP archives "
    + "in Reuters Corpus directory.");

    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      SourceDirectory_Text = new KTextField(CastParameter
      .getSourceDirectory(), 30);
      FileNameFilter_Text = new KTextField(CastParameter
      .getFileNameFilter(), 30);
      TopicOfCollection_Text = new KTextField(CastParameter
      .getTopicOfCollection(), 30);
      RegionOfCollection_Text = new KTextField(CastParameter
      .getRegionOfCollection(), 30);
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
      "DEFAULT_REUTERS_CORPUS_VOL1_FILE_DIRECTORY"), 30);
      FileNameFilter_Text = new KTextField(".xml", 30);
      TopicOfCollection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_REUTERS_CORPUS_VOL1_COLLECTION_TOPIC"), 30);
      RegionOfCollection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_REUTERS_CORPUS_VOL1_COLLECTION_REGION"), 30);
    }
    Collection_Text.setCaretAtEnding();
    SourceDirectory_Text.setCaretAtEnding();
    FileNameFilter_Text.setCaretAtEnding();
    TopicOfCollection_Text.setCaretAtEnding();
    RegionOfCollection_Text.setCaretAtEnding();
    
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
    Parameter_Panel.addLabel("Reuters Corpus Directory:", 0, 2, KeyEvent.VK_D,
    SourceDirectory_Button.getDefaultButton(), true,
    "Task input: This directory must contain " +
    "original Reuters Corpus files to be imported.");
    Parameter_Panel.addComponent(SourceDirectory_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(SourceDirectory_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addComponent( IncludeSubdirectories_CheckBox, 2, 4,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addComponent( IncludeZipFileContents_CheckBox, 2, 5,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 6, 11);
    Parameter_Panel.addLabel("File Name Extension:", 0, 7, KeyEvent.VK_N,
    FileNameFilter_Text, true,
    "Task input: Reuters Corpus files to be imported " +
    "must have this file name extension.");
    Parameter_Panel.addComponent( FileNameFilter_Text, 2, 7,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 8, 11);
    Parameter_Panel.addLabel("Topic of Collection:", 0, 9, KeyEvent.VK_T,
    TopicOfCollection_Text, true,
    "Optional task input: Reuters Corpus news to be imported " +
    "must have this topic code (e.g., C181).");
    Parameter_Panel.addComponent( TopicOfCollection_Text, 2, 9,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 10, 11);
    Parameter_Panel.addLabel("Region of Collection:", 0, 11, KeyEvent.VK_R,
    RegionOfCollection_Text, true,
    "Optional task input: Reuters Corpus news to be imported " +
    "must have this region code (e.g., USA).");
    Parameter_Panel.addComponent( RegionOfCollection_Text, 2, 11,
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