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

package org.hypknowsys.wum.client.gui.solutions.batch.editBatchScript;

import java.lang.reflect.*;
import java.io.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.tasks.project.openProject.*;
import org.hypknowsys.wum.tasks.project.closeProject.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class EditBatchScriptControlPanel extends WumActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private Script WumScript = null;
  private String BatchScriptFileName = null;
  private File BatchScriptDirectory = null;
      
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

  public EditBatchScriptControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public EditBatchScriptControlPanel(Server pWumServer, Project pWumProject,
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
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

    if ( ActionCommand.equals("New") ) { 
      this.newScript();
    } else if ( ActionCommand.equals("Open") ) {
      this.openScript();
    } else if ( ActionCommand.equals("Save") ) {
      this.save();
    } else if ( ActionCommand.equals("SaveAs") ) {
      this.saveAs();
    } else if ( ActionCommand.equals("ResetScript") ) {
      this.resetScript();
    } else if ( ActionCommand.equals("Exit") ) {
      this.setControlPanelContainerClosed(true);
    } else if (ActionCommand.equals("KInternalFrame:EscapePressed")) {      
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
    
    PriorWumGuiStatus = DO_NOT_MODIFY_WUM_GUI_STATUS;    

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Edit Batch Script";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Button_Panel != null) {
      return Button_Panel.getButton(0);
    }
    else {
      return null;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void finalize() throws PropertyVetoException {

    boolean commit = true;
    if (WumScript != null && WumScript.countScriptTasks() > 0
    && Parameter_Panel != null && Parameter_Panel.getSaveRequired()) {
      int input = JOptionPane.showConfirmDialog(
      WumGui.getJFrame(), 
      "Warning: The current batch script has\n"
      + "not been saved yet. Do you really want\n"
      + "to close the batch script editor?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION, 
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
    }
    if (commit) {
      WumGui.getGuiMenuBar().getRecordBatchScriptKMenuItem()
      .setEnabled(true);
      super.finalize();
    }
    else {
      throw new PropertyVetoException("Close operation canceled!", 
      new PropertyChangeEvent(this, "close", null, null) ); 
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createButtonPanel() {  

    Button_Panel = this.getButtonPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    Parameter_Panel = new EditBatchScriptParameterPanel(WumServer, 
    WumProject, WumGui, WumGuiPreferences);
    Parameter_Panel.setEnabled(false);
  
  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void newScript() {
    
    boolean commit = true;
    if (WumScript != null && WumScript.countScriptTasks() > 0
    && Parameter_Panel != null && Parameter_Panel.getSaveRequired()) {
      int input = JOptionPane.showConfirmDialog(
      WumGui.getJFrame(), 
      "Warning: The current batch script has\n"
      + "not been saved yet. Do you really\n"
      + "want to create a new batch script?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION, 
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
    }
    if (commit) {
      Button_Panel.setEnabled(0, true);  // New
      Button_Panel.setEnabled(1, true);  // Open
      Button_Panel.setEnabled(2, true);  // Save
      Button_Panel.setEnabled(3, true);  // SaveAs
      Button_Panel.setEnabled(4, true);  // Reset
      WumScript = new DefaultWUMscript();
      BatchScriptFileName = null;
      Parameter_Panel.setSaveRequired(false);
      this.setControlPanelContainerTitle(this.getPreferredTitle());
      Parameter_Panel.setTaskParameter(new EditBatchScriptParameter(
      WumScript));
      Tools.requestFocus(Parameter_Panel.getAsComponent(),
      Parameter_Panel.getInitialFocusComponent());
    }
  
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private void openScript() {
    
    boolean commit = true;
    if (WumScript != null && WumScript.countScriptTasks() > 0
    && Parameter_Panel != null && Parameter_Panel.getSaveRequired() ) {
      int input = JOptionPane.showConfirmDialog(
      WumGui.getJFrame(),
      "Warning: The current batch script has\n"
      + "not been saved yet. Do you really\n"
      + "want to open a new batch script?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION,
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
    }
    if (commit) {
      File file = null;
      if (BatchScriptFileName != null) {
        file = new File(BatchScriptFileName);
      }
      if (file != null && file.exists() && file.isFile()) {
        GuiFileChooser = new JFileChooser(file);
        GuiFileChooser.setSelectedFile(file);
      }
      else if (BatchScriptDirectory != null) {
        GuiFileChooser = new JFileChooser(BatchScriptDirectory);
      }
      else if (WumProject != null && WumProject.getStringProperty(
      "MRU_BATCH_SCRIPT_FILE_NAME").length() > 0) {
        GuiFileChooser = new JFileChooser(WumProject
        .getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME"));
      }
      else if (WumProject != null && WumProject.getStringProperty(
      "PROJECT_DIRECTORY").length() > 0) {
        GuiFileChooser = new JFileChooser(WumProject
        .getStringProperty("PROJECT_DIRECTORY"));
      }
      else {
        GuiFileChooser = new JFileChooser(WumGuiPreferences
        .getMruBatchScriptFileName());
      }
      GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      GuiFileChooser.setDialogTitle("Select Existing Batch Script File");
      GuiFileChooser.setFileFilter(WUMproject.SCRIPT_FILE_FILTER);
      int result = GuiFileChooser.showSaveDialog(this);
      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = KFileFilter.ensureFileExtension(
        GuiFileChooser.getSelectedFile(),
        WUMproject.SCRIPT_FILE_EXTENSION);
        try {
          WumScript = new DefaultWUMscript(
          selectedFile.getAbsolutePath());
          WumScript.setTransientFileName(selectedFile.getAbsolutePath());
          BatchScriptDirectory = GuiFileChooser.getCurrentDirectory();
          this.setControlPanelContainerTitle(this.getPreferredTitle() + " ["
          + Tools.shortenFileName(selectedFile.getAbsolutePath(), 40)  + "]");
          Button_Panel.setEnabled(0, true);  // New
          Button_Panel.setEnabled(1, true);  // Open
          Button_Panel.setEnabled(2, true);  // Save
          Button_Panel.setEnabled(3, true);  // SaveAs
          Button_Panel.setEnabled(4, true);  // Reset
          BatchScriptFileName = selectedFile.getAbsolutePath();
          Parameter_Panel.setSaveRequired(false);
          Parameter_Panel.setTaskParameter(new EditBatchScriptParameter(
          WumScript));
          Tools.requestFocus(Parameter_Panel.getAsComponent(),
          Parameter_Panel.getInitialFocusComponent());
        }
        catch (WumException e) {
          WumGui.logWarningMessage(e.getMessage());
          JOptionPane.showMessageDialog(WumGui.getJFrame(),
          "Error: The chosen batch script\n"
          + Tools.shortenFileName(selectedFile.getAbsolutePath(), 30) + "\n"
          + "cannot be opened. It might not\n"
          + "be a valid WUM batch script.",
          this.getPreferredTitle(), JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private void save() {
    
    WumScript = ( (EditBatchScriptParameter)Parameter_Panel
    .getTaskParameter() ).getWumScript();

    if (BatchScriptFileName != null) {
      File file = new File(BatchScriptFileName);
      if ( file.exists() && file.isFile() ) {
        boolean commit = this.isValidEditBatchScriptParameter();
        if (!commit) {
          JOptionPane.showMessageDialog(
          WumGui.getJFrame(), "The current batch script has not\n"
          + "been saved due to indicated errors.",
          this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);
          return;
        }
        else {
          WumScript.writeXmlDocument(BatchScriptFileName);
          Parameter_Panel.setSaveRequired(false);
        }
      }
      else {
        this.saveAs();
      }
    }
    else {
      this.saveAs();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void saveAs() {
    
    WumScript = ( (EditBatchScriptParameter)Parameter_Panel
    .getTaskParameter() ).getWumScript();
    
    boolean commit = this.isValidEditBatchScriptParameter();
    if (!commit) {
      JOptionPane.showMessageDialog(
      WumGui.getJFrame(), "The current batch script has not\n"
      + "been saved due to indicated errors.",
      this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);      
      return;
    }
    
    File file = null;
    if (BatchScriptFileName != null) {
      file = new File(BatchScriptFileName);
    }
    if (file != null && file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else if (BatchScriptDirectory != null) {
      GuiFileChooser = new JFileChooser(BatchScriptDirectory);
    }
    else if (WumProject != null && WumProject.getStringProperty(
      "MRU_BATCH_SCRIPT_FILE_NAME").length() > 0) {
      GuiFileChooser = new JFileChooser(Tools.extractDirectory(
      WumProject.getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME")));
    }
    else {
      GuiFileChooser = new JFileChooser(Tools.extractDirectory(
      WumGuiPreferences.getMruBatchScriptFileName()));
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Batch Script File to be Created");
    GuiFileChooser.setFileFilter(WUMproject.SCRIPT_FILE_FILTER);
    int result = GuiFileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      WUMproject.SCRIPT_FILE_EXTENSION);
      BatchScriptDirectory = GuiFileChooser.getCurrentDirectory();
      if (Tools.isExistingFile(selectedFile.getAbsolutePath())) {
        int input = JOptionPane.showConfirmDialog(
        WumGui.getJFrame(), 
        "Warning: The specified batch script currently\n" +
        "exists. Do you really want to replace this file?",
        this.getPreferredTitle(), JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
        if (input == JOptionPane.NO_OPTION) {
          commit = true;
        }
      }  
      if (commit) {
        WumScript.writeXmlDocument(selectedFile.getAbsolutePath());
        BatchScriptFileName = selectedFile.getAbsolutePath();
        WumScript.setTransientFileName(BatchScriptFileName);
        Parameter_Panel.setTaskParameter(new EditBatchScriptParameter(
        WumScript));
        Tools.requestFocus(Parameter_Panel.getInitialFocusComponent());
        Parameter_Panel.setSaveRequired(false);
        this.setControlPanelContainerTitle(this.getPreferredTitle() + " [" 
        + Tools.shortenFileName(selectedFile.getAbsolutePath(), 40)  + "]");
        WumProject.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
        BatchScriptFileName);
        WumProject.quickSave();
        WumGuiPreferences.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
        BatchScriptFileName);
        WumGuiPreferences.quickSave();
      }
    }  
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void resetScript() {
    
    WumScript = ( (EditBatchScriptParameter)Parameter_Panel
    .getTaskParameter() ).getWumScript();
    if (WumScript != null) {
      boolean commit = true;
      int input = JOptionPane.showConfirmDialog(WumGui.getJFrame(),
      "Warning: Do you really want to reset\n"
      + "the entire batch script by deleting\nb"
      + "the results of all tasks?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION,
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
      if (commit) {
        WumScript.resetScript();
        Parameter_Panel.setTaskParameter(new EditBatchScriptParameter(
        WumScript));
        Parameter_Panel.setSaveRequired(true);
        Tools.requestFocus(Parameter_Panel.getAsComponent(),
        Parameter_Panel.getInitialFocusComponent());
      }
    }

  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private KButtonPanel getButtonPanel() {  

    KButtonPanel button_Panel = new KButtonPanel(17, 0, 0, 0, 7, 
      KButtonPanel.HORIZONTAL_RIGHT);
    button_Panel.addFirstButton("New", 
      KeyEvent.VK_N, true, false, "New", this);
    button_Panel.addNextButton("Open", 
      KeyEvent.VK_O, true, false, "Open", this);
    button_Panel.addNextButton("Save", 
      KeyEvent.VK_S, false, false, "Save", this);
    button_Panel.addNextButton("Save As", 
      KeyEvent.VK_A, false, false, "SaveAs", this);
    button_Panel.addNextButton("Reset", 
      KeyEvent.VK_R, false, false, "ResetScript", this);
    button_Panel.addNextButton("Exit", 
      KeyEvent.VK_X, true, false, "Exit", this);
    button_Panel.addLastButton("Help", 
      KeyEvent.VK_H, false, false, "Help", this);
    
    return button_Panel;
    
  } 
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isValidEditBatchScriptParameter() {

    TaskParameter parameter = Parameter_Panel.getTaskParameter();
    AbstractValidatedTaskParameter validatedParameter =
    WumServer.validateTaskParameter((Project)WumProject,
    parameter.getTaskClassName(), parameter);
    return this.isValidParameter(validatedParameter);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}