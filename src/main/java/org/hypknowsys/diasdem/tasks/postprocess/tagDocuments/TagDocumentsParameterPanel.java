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

package org.hypknowsys.diasdem.tasks.postprocess.tagDocuments;

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

public class TagDocumentsParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TagDocumentsParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField DtdFile_Text = null;
  private KButtonPanel DtdFile_Button = null;
  private KTextField RandomSampleFile_Text = null;
  private KButtonPanel RandomSampleFile_Button = null;
  private KTextField RandomSampleSize_Text = null;
  private KComboBox TextUnit_Combo = null;
  private KCheckBox CreateTagByDocumentFile_CheckBox = null;
  private KCheckBox CreateWumFiles_CheckBox = null;
  private KCheckBox CreateGateFiles_CheckBox = null;
  private KTextField GateDirectory_Text = null;
  private KButtonPanel GateDirectory_Button = null;
  private KTextField XmlDocumentsDirectory_Text = null;
  private KButtonPanel XmlDocumentsDirectory_Button = null;
  
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
  
  public TagDocumentsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagDocumentsParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
      "Select", KeyEvent.VK_S, null, "Select Existing Preliminary DTD File",
      DIAsDEMguiPreferences.PRELIMINARY_DTD_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("RandomSampleFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      RandomSampleFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Random Sample File to be Created",
      DIAsDEMguiPreferences.TEXT_UNIT_SAMPLE_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("GateDirectoryButton")) {

      CurrentProjectDirectory = this.directoryButtonClicked(GateDirectory_Text,
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing Directory of GATE Documents");
      
    } else if (ActionCommand.equals("XmlDocumentsButton")) {
      
      CurrentProjectDirectory = this.directoryButtonClicked(
      XmlDocumentsDirectory_Text,
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing Directory of XML Documents");
      
    }  else if (ActionCommand.equals("ExportGateFile")) {
      
      this.createGateFileButton();
      
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
    
    return "Tag Documents";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogMSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    TagDocumentsParameter parameter = new TagDocumentsParameter(
    Collection_Text.getText(),
    DtdFile_Text.getText(),
    Tools.string2Double(RandomSampleSize_Text.getText()),
    RandomSampleFile_Text.getText(), false, false, false,
    GateDirectory_Text.getText(), 
    XmlDocumentsDirectory_Text.getText());
    if (CreateTagByDocumentFile_CheckBox.isSelected()) {
      parameter.setCreateTagByDocumentFile(true);
    }
    if (CreateWumFiles_CheckBox.isSelected()) {
      parameter.setCreateWumFiles(true);
    }
    if (CreateGateFiles_CheckBox.isSelected()) {
      parameter.setCreateGateFile(true);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof TagDocumentsParameter) {
      CastParameter = (TagDocumentsParameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_PRELIMINARY_DTD_FILE",
    DtdFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_SAMPLE_FILE",
    RandomSampleFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_SAMPLE_SIZE",
    RandomSampleSize_Text.getText());
    if (CreateGateFiles_CheckBox.isSelected()) {
      DiasdemProject.setProperty("DEFAULT_EXPORT_GATE_DOCUMENTS",
      Tools.boolean2String(true));
      DiasdemProject.setProperty("DEFAULT_EXPORT_GATE_DIRECTORY",
      GateDirectory_Text.getText());
    }
    else {
      DiasdemProject.setProperty("DEFAULT_EXPORT_GATE_DOCUMENTS",
      Tools.boolean2String(false));
    }
    DiasdemProject.setProperty("DEFAULT_XML_DOCUMENTS_DIRECTORY",
    XmlDocumentsDirectory_Text.getText());
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
    
    CreateTagByDocumentFile_CheckBox = new KCheckBox(
    "Export Tag-by-Document-Matrix as CSV-File", false, true,
    "CreateTagByDocumentFile", this, KeyEvent.VK_F,
    "If this box is checked, a structured tag-by-document CVS-file " +
    "will be created.");

    CreateWumFiles_CheckBox = new KCheckBox(
    "Create Log Files for Tag Analysis with WUM", false, true,
    "CreateWumFiles", this, KeyEvent.VK_L,
    "If this box is checked, a sequential tag-by-document log file " +
    "will be created.");

    CreateGateFiles_CheckBox = new KCheckBox(
    "Export XML Documents as GATE Files. Directory:", false, true,
    "ExportGateFile", this, KeyEvent.VK_G,
    "If this box is checked, XML documents will be exported as structurally " +
    "and semantically annotated GATE files.");

    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      XmlDocumentsDirectory_Text = new KTextField(CastParameter
      .getXmlDocumentsDirectory(), 30);
      DtdFile_Text = new KTextField(CastParameter
      .getDtdFileName(), 30);
      RandomSampleFile_Text = new KTextField(CastParameter
      .getRandomSampleFileName(), 30);
      RandomSampleSize_Text = new KTextField(CastParameter
      .getRandomSampleSize() +"", 30);
      if (CastParameter.createTagByDocumentFile()) {
        CreateTagByDocumentFile_CheckBox.setSelected(true);
      }
      else {
        CreateTagByDocumentFile_CheckBox.setSelected(false);
      }
      if (CastParameter.createWumFiles()) {
        CreateWumFiles_CheckBox.setSelected(true);
      }
      else {
        CreateWumFiles_CheckBox.setSelected(false);
      }
      if (CastParameter.createGateFiles()) {
        CreateGateFiles_CheckBox.setSelected(true);
        GateDirectory_Text = new KTextField(CastParameter
        .getGateDirectory(), 30);
      }
      else {
        CreateGateFiles_CheckBox.setSelected(false);
        GateDirectory_Text = new KTextField("", 30);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      XmlDocumentsDirectory_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_XML_DOCUMENTS_DIRECTORY"), 30);
      DtdFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_PRELIMINARY_DTD_FILE"), 30);
      RandomSampleFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_TEXT_UNIT_SAMPLE_FILE"), 30);
      RandomSampleSize_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_TEXT_UNIT_SAMPLE_SIZE"), 30);
      if (DiasdemProject.getBooleanProperty(
      "DEFAULT_EXPORT_GATE_DOCUMENTS")) {
        CreateGateFiles_CheckBox.setSelected(true);
        GateDirectory_Text = new KTextField(DiasdemProject.getProperty(
        "DEFAULT_CREATE_HTML_SAMPLE_DIRECTORY"), 30);
      }
      else {
        CreateGateFiles_CheckBox.setSelected(false);
        GateDirectory_Text = new KTextField("", 30);
      }
    }
    Collection_Text.setCaretAtEnding();    
    XmlDocumentsDirectory_Text.setCaretAtEnding();    
    DtdFile_Text.setCaretAtEnding();  
    RandomSampleFile_Text.setCaretAtEnding();  
    RandomSampleSize_Text.setCaretAtEnding();  
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
      KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");    
    
    XmlDocumentsDirectory_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    XmlDocumentsDirectory_Button.addSingleButton("...", 
      KeyEvent.VK_X, true, true, "XmlDocumentsButton", this,
    "Click this button to select the directory of XML documents.");    
    
    DtdFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    DtdFile_Button.addSingleButton("...", 
      KeyEvent.VK_P, true, true, "DtdFileButton", this,
    "Click this button to select the DTD file.");    

    RandomSampleFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    RandomSampleFile_Button.addSingleButton("...", 
      KeyEvent.VK_R, true, true, "RandomSampleFileButton", this,
    "Click this button to select the random sample file.");    

    GateDirectory_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    GateDirectory_Button.addSingleButton("...", 
      0, true, true, "GateDirectoryButton", this,
    "Click this button to select the directory of GATE documents.");    
    
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
    Parameter_Panel.addBlankRow(0, 1, 11);
    Parameter_Panel.addLabel("XML Document Directory:", 0, 2, KeyEvent.VK_X,
      XmlDocumentsDirectory_Button.getDefaultButton(), true,
    "Task output: Sematically tagged XML documents will be copied into " +
    "this directory.");
    Parameter_Panel.addComponent(XmlDocumentsDirectory_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(XmlDocumentsDirectory_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Preliminary DTD File:", 0, 4, KeyEvent.VK_P,
      DtdFile_Button.getDefaultButton(), true,
    "Task input: This file contains meta-data about the preliminary " +
    "XML DTD");
    Parameter_Panel.addComponent(DtdFile_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(DtdFile_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Random Sample File:", 0, 6, KeyEvent.VK_R,
      RandomSampleFile_Button.getDefaultButton(), true,
    "Task output: This file will contain a random sample of text units.");
    Parameter_Panel.addComponent(RandomSampleFile_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(RandomSampleFile_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Random Sample Size:", 0, 8, KeyEvent.VK_S,
      RandomSampleSize_Text, true,
    "Task input: Choose an appropriate parameter value in [0.0; 1.0].");
    Parameter_Panel.addComponent(RandomSampleSize_Text, 2, 8, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 10);
    Parameter_Panel.addComponent( CreateTagByDocumentFile_CheckBox, 
      2, 10, new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 11);
    Parameter_Panel.addComponent(CreateWumFiles_CheckBox, 
      2, 11, new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 12);
    Parameter_Panel.addComponent(CreateGateFiles_CheckBox, 
      2, 12, new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 13);
    Parameter_Panel.addComponent(GateDirectory_Text, 2, 13, 
      new Insets(0, 0, 0, 0), 1, 1);
    Parameter_Panel.addKButtonPanel(GateDirectory_Button, 4, 13);

    Parameter_Panel.setPreferredSize(Parameter_Panel.getMinimumSize());    
    KScrollBorderPanel Parameter_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    Parameter_ScrollPanel.addNorth(Parameter_Panel);
    Parameter_ScrollPanel.startFocusForwarding(Collection_Text);
    
    this.removeAll();
    this.validate();
    this.addCenter(Parameter_ScrollPanel);
    this.validate();
    this.setComponentStatus();    
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {
  
    this.createGateFileButton();
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createGateFileButton() {
    
    if (!CreateGateFiles_CheckBox.isSelected()) {
      GateDirectory_Text.setText("");
      GateDirectory_Text.setEnabled(false);
      GateDirectory_Button.setAllEnabled(false);
    }
    else {
      if (CastParameter != null) {
        GateDirectory_Text.setText(CastParameter.getGateDirectory());
      }
      else {
        GateDirectory_Text.setText(DiasdemProject.getProperty(
        "DEFAULT_EXPORT_GATE_DIRECTORY"));
      }
      GateDirectory_Text.setEnabled(true);
      GateDirectory_Button.setAllEnabled(true);
    }
    this.validate();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}