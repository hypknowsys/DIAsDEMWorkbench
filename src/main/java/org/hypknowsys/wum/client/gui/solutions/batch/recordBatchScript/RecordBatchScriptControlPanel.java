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

package org.hypknowsys.wum.client.gui.solutions.batch.recordBatchScript;

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
  
public class RecordBatchScriptControlPanel extends WumActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private WUMscript WumScript = null;
  private boolean IsRecordingBatchScript = false;
  private boolean BatchScriptHasBeenSaved = false;
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

  public RecordBatchScriptControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public RecordBatchScriptControlPanel(Server pWumServer, Project pWumProject,
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
    WumGui.getGuiMenuBar().getRecordBatchScriptKMenuItem()
    .setEnabled(false);
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

    if ( ActionCommand.equals("Start") ) { 
      this.start();
    } else if ( ActionCommand.equals("Stop") ) {
      this.stop();
    } else if ( ActionCommand.equals("Save") ) {
        this.save();
    } else if ( ActionCommand.equals("Open") ) {
        this.open();
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
  
  public void finalize() throws PropertyVetoException {

    boolean commit = true;
    if ((WumScript != null && WumScript.countScriptTasks() > 0
    && !BatchScriptHasBeenSaved) || IsRecordingBatchScript) {
      int input = JOptionPane.showConfirmDialog(
      ControlPanelContainer.getParentJFrame(), 
      "Warning: The current batch script has\n"
      + "not been saved yet. Do you really want\n"
      + "to close the batch script recorder?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION, 
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION) {
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
  
  public String getPreferredTitle() {
    
    return "Record Batch Script";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogXxsSizeY();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredKDesktopLayout() {
    
    return KDesktopPane.SOUTH_EAST;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Button_Panel != null) {
      return Button_Panel.getButton(1);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createButtonPanel() {  

    Button_Panel = this.getButtonPanel();
    
  }

  public void createParameterPanel() {  

    Parameter_Panel = null;
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void start() {
    
    boolean commit = true;
    if (WumScript != null && WumScript.countScriptTasks() > 0
    && !BatchScriptHasBeenSaved ) {
      int input = JOptionPane.showConfirmDialog(
      ControlPanelContainer.getParentJFrame(), 
      "Warning: The current batch script has\n"
      + "not been saved yet. Do you really want\n"
      + "to start recording a new batch script?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION, 
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION) {
        commit = false;
      }
    }
    if (commit) {
      Button_Panel.setEnabled(0, false);  // Open
      Button_Panel.setEnabled(1, false);  // Start
      Button_Panel.setEnabled(2, true);  // Stop
      Button_Panel.setEnabled(3, false);  // Save
      WumGui.logInfoMessage("Recording of new batch script started");
      WumScript = new DefaultWUMscript();
      IsRecordingBatchScript = true;
      BatchScriptHasBeenSaved = false;
      WumServer.startRecordingScript(WumScript);
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void stop() {
    
    Button_Panel.setEnabled(0, true);  // Open
    Button_Panel.setEnabled(1, true);  // Start
    Button_Panel.setEnabled(2, false); // Stop
    Button_Panel.setEnabled(3, true);  // Save
    WumGui.logInfoMessage("Recording of new batch script stopped");
    IsRecordingBatchScript = false;
    BatchScriptHasBeenSaved = false;
    WumScript = (WUMscript)WumServer.stopRecordingScript();
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void save() {
    
    if (WumScript == null || WumScript.countScriptTasks() == 0) {
      JOptionPane.showMessageDialog(
      ControlPanelContainer.getParentJFrame(), "The current batch script does\n"
      + "not contain any tasks at all!\nEmpty scripts cannot be saved.",
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
    int result = GuiFileChooser.showSaveDialog(WumGui.getJFrame());
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      WUMproject.SCRIPT_FILE_EXTENSION);
      BatchScriptDirectory = GuiFileChooser.getCurrentDirectory();
      boolean commit = true;
      if (Tools.isExistingFile(selectedFile.getAbsolutePath())) {
        int input = JOptionPane.showConfirmDialog(
        ControlPanelContainer.getParentJFrame(), 
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
        BatchScriptHasBeenSaved = true;
        this.setControlPanelContainerTitle(this.getPreferredTitle() + " [" 
        + Tools.shortenFileName(selectedFile.getAbsolutePath(), 40)  + "]");
        WumProject.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
        selectedFile.getAbsolutePath());
        WumProject.quickSave();
        WumGuiPreferences.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
        selectedFile.getAbsolutePath());
        WumGuiPreferences.quickSave();
      }
    }  
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void open() {
    
    if ((WumScript != null && WumScript.countScriptTasks() > 0
    && !BatchScriptHasBeenSaved) || IsRecordingBatchScript) {
      int input = JOptionPane.showConfirmDialog(
      ControlPanelContainer.getParentJFrame(), 
      "Warning: The current batch script has\n"
      + "not been saved yet. Do you really want\n"
      + "to close the batch script recorder?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION, 
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION) {
        return;
      }
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
      GuiFileChooser = new JFileChooser(WumProject
      .getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME"));
    }
    else {
      GuiFileChooser = new JFileChooser(WumGuiPreferences
      .getMruBatchScriptFileName());
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Batch Script File to be Extended");
    GuiFileChooser.setFileFilter(WUMproject.SCRIPT_FILE_FILTER);
    int result = GuiFileChooser.showOpenDialog(WumGui.getJFrame());
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      WUMproject.SCRIPT_FILE_EXTENSION);
      BatchScriptDirectory = GuiFileChooser.getCurrentDirectory();
      if (Tools.isExistingFile(selectedFile.getAbsolutePath())) {
        try {
          WumScript = new DefaultWUMscript(selectedFile
          .getAbsolutePath());
          this.setControlPanelContainerTitle(this.getPreferredTitle() + " ["
          + Tools.shortenFileName(selectedFile.getAbsolutePath(), 40)  + "]");
          Button_Panel.setEnabled(0, false);  // Open
          Button_Panel.setEnabled(1, false);  // Start
          Button_Panel.setEnabled(2, true);   // Stop
          Button_Panel.setEnabled(3, false);  // Save
          IsRecordingBatchScript = true;
          BatchScriptHasBeenSaved = false;
          WumServer.startRecordingScript(WumScript);
        }
        catch (Exception e) {
          JOptionPane.showMessageDialog(WumGui.getJFrame(),
          "Error: The chosen batch script\n"
          + Tools.shortenFileName(selectedFile.getAbsolutePath(), 30) + "\n"
          + "cannot be opened. It might not\n"
          + "be a valid DIAsDEM batch script.",
          this.getPreferredTitle(), JOptionPane.ERROR_MESSAGE);
        }
      }
    }  
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
    private KButtonPanel getButtonPanel() {  

    KButtonPanel button_Panel = new KButtonPanel(17, 0, 0, 0, 6, 
      KButtonPanel.HORIZONTAL_RIGHT);
    button_Panel.addFirstButton("Open", 
      KeyEvent.VK_O, true, false, "Open", this);
    button_Panel.addNextButton("Start", 
      KeyEvent.VK_T, true, false, "Start", this);
    button_Panel.addNextButton("Stop", 
      KeyEvent.VK_P, false, false, "Stop", this);
    button_Panel.addNextButton("Save", 
      KeyEvent.VK_S, false, false, "Save", this);
    button_Panel.addNextButton("Exit", 
      KeyEvent.VK_X, true, false, "Exit", this);
    button_Panel.addLastButton("Help", 
      KeyEvent.VK_H, false, false, "Help", this);
    
    return button_Panel;
    
  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}