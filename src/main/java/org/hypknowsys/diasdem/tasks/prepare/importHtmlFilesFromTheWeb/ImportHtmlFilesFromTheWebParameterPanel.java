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

package org.hypknowsys.diasdem.tasks.prepare.importHtmlFilesFromTheWeb;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Heiko Scharff
 */

public class ImportHtmlFilesFromTheWebParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportHtmlFilesFromTheWebParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField Url_Text = null;
  private KButtonPanel Url_Button = null;
  private KTextField SourceDirectory_Text = null;
  private KButtonPanel SourceDirectory_Button = null;
  private KTextField FileNameFilter_Text = null;

  private File CurrentUrlFileDirectory = null;
  private File CurrentTextFileDirectory = null;

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
  
  public ImportHtmlFilesFromTheWebParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportHtmlFilesFromTheWebParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    } else if (ActionCommand.equals("UrlButton")) {

      CurrentUrlFileDirectory = this.fileNameButtonClicked(
      Url_Text, CurrentUrlFileDirectory,
      "IMPORT_HTML_FILES_FROM_THE_WEB:_DEFAULT_URL_FILE", "Select", 
      KeyEvent.VK_U, null, "Select Existing Download URL File",
      DIAsDEMguiPreferences.URL_FILE_FILTER, false, true);

    } else if (ActionCommand.equals("SourceDirectoryButton")) {
      
      CurrentTextFileDirectory = this.directoryButtonClicked(
      SourceDirectory_Text, CurrentTextFileDirectory, 
      "DEFAULT_TEXT_FILE_DIRECTORY", "Select", KeyEvent.VK_S, null, 
      "Select Existing Text File Directory");
      
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
    
    return "Import HTML Files from the Web";

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
    
    ImportHtmlFilesFromTheWebParameter parameter = new ImportHtmlFilesFromTheWebParameter(
    Collection_Text.getText(),
    Url_Text.getText(),
    SourceDirectory_Text.getText(),
    FileNameFilter_Text.getText());

    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ImportHtmlFilesFromTheWebParameter) {
      CastParameter = (ImportHtmlFilesFromTheWebParameter)pTaskParameter;
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
    
    DiasdemProject.setProperty(
    "DEFAULT_COLLECTION_FILE", Collection_Text.getText());
    DiasdemProject.setProperty(
    "IMPORT_HTML_FILES_FROM_THE_WEB:_DEFAULT_URL_FILE", Url_Text.getText());
    DiasdemProject.setProperty(
    "DEFAULT_TEXT_FILE_DIRECTORY", SourceDirectory_Text.getText());
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

    Url_Button = new KButtonPanel(0, 0, 0, 0, 1,
      KButtonPanel.HORIZONTAL_RIGHT);
    Url_Button.addSingleButton("...",
     KeyEvent.VK_U, true, true, "UrlButton", this,
    "Click this button to select the download URL file.");

    SourceDirectory_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    SourceDirectory_Button.addSingleButton("...",
    KeyEvent.VK_T, true, true, "SourceDirectoryButton", this,
    "Click this button to select the text file directory.");
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      Url_Text = new KTextField(CastParameter
      .getUrlFileName(), 30);
      SourceDirectory_Text = new KTextField(CastParameter
      .getSourceDirectory(), 30);
      FileNameFilter_Text = new KTextField(CastParameter
      .getFileNameFilter(), 30);
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      Url_Text = new KTextField(DiasdemProject.getProperty(
      "IMPORT_HTML_FILES_FROM_THE_WEB:_DEFAULT_URL_FILE"), 30);
      SourceDirectory_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_TEXT_FILE_DIRECTORY"), 30);
      FileNameFilter_Text = new KTextField(".txt", 30);
    }
    Collection_Text.setCaretAtEnding();
    Url_Text.setCaretAtEnding();
    SourceDirectory_Text.setCaretAtEnding();
    FileNameFilter_Text.setCaretAtEnding();
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Collection_Text);

    // collection file
    Parameter_Panel.addLabel("Collection File:", 0, 0, KeyEvent.VK_C,
      Collection_Button.getDefaultButton(), true,
    "Task input: This collection file contains references " +
    "to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());

    // url file
    Parameter_Panel.addLabel("Download URL File:", 0, 2, KeyEvent.VK_U,
      Url_Button.getDefaultButton(), true,
    "Task input: This download URL file contains references " +
    "to all documents to be downloaded.");
    Parameter_Panel.addComponent(Url_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(Url_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);

    // text file directory (= download directory)
    Parameter_Panel.addLabel("Text File Directory:", 0, 4, KeyEvent.VK_T,
    SourceDirectory_Button.getDefaultButton(), true,
    "Task output: This directory will contain all imported HTML files.");
    Parameter_Panel.addComponent(SourceDirectory_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(SourceDirectory_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    // extension
    Parameter_Panel.addLabel("File Name Extension:", 0, 6, KeyEvent.VK_N,
    FileNameFilter_Text, true,
    "Task input: Imported HTML files will be stored as text files "
    + "having this file name extension.");
    Parameter_Panel.addComponent( FileNameFilter_Text, 2, 6,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 4, 11);

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
