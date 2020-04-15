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

package org.hypknowsys.diasdem.client.gui.solutions.batch.recordBatchScript;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.diasdem.core.DIAsDEMproject;
import org.hypknowsys.diasdem.core.DIAsDEMscript;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMscript;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KDesktopPane;
import org.hypknowsys.misc.swing.KFileFilter;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class RecordBatchScriptControlPanel extends DiasdemActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private DIAsDEMscript DiasdemScript = null;
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

  public RecordBatchScriptControlPanel(Server pDiasdemServer, 
  Project pDiasdemProject, GuiClient pDiasdemGui, 
  GuiClientPreferences pDiasdemGuiPreferences) {
  
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
    DiasdemGui.getGuiMenuBar().getRecordBatchScriptKMenuItem()
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
    if ((DiasdemScript != null && DiasdemScript.countScriptTasks() > 0
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
      DiasdemGui.getGuiMenuBar().getRecordBatchScriptKMenuItem()
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
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogXxsSizeY();
    
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
    if (DiasdemScript != null && DiasdemScript.countScriptTasks() > 0
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
      DiasdemGui.logInfoMessage("Recording of new batch script started");
      DiasdemScript = new DefaultDIAsDEMscript();
      IsRecordingBatchScript = true;
      BatchScriptHasBeenSaved = false;
      DiasdemServer.startRecordingScript(DiasdemScript);
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void stop() {
    
    Button_Panel.setEnabled(0, true);  // Open
    Button_Panel.setEnabled(1, true);  // Start
    Button_Panel.setEnabled(2, false); // Stop
    Button_Panel.setEnabled(3, true);  // Save
    DiasdemGui.logInfoMessage("Recording of new batch script stopped");
    IsRecordingBatchScript = false;
    BatchScriptHasBeenSaved = false;
    DiasdemScript = (DIAsDEMscript)DiasdemServer.stopRecordingScript();
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void save() {
    
    if (DiasdemScript == null || DiasdemScript.countScriptTasks() == 0) {
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
    else if (DiasdemProject != null && DiasdemProject.getStringProperty(
      "MRU_BATCH_SCRIPT_FILE_NAME").length() > 0) {
      GuiFileChooser = new JFileChooser(Tools.extractDirectory(
      DiasdemProject.getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME")));
    }
    else {
      GuiFileChooser = new JFileChooser(Tools.extractDirectory(
      DiasdemGuiPreferences.getMruBatchScriptFileName()));
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Batch Script File to be Created");
    GuiFileChooser.setFileFilter(DIAsDEMproject.SCRIPT_FILE_FILTER);
    int result = GuiFileChooser.showSaveDialog(DiasdemGui.getJFrame());
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      DIAsDEMproject.SCRIPT_FILE_EXTENSION);
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
        DiasdemScript.writeXmlDocument(selectedFile.getAbsolutePath());
        BatchScriptHasBeenSaved = true;
        this.setControlPanelContainerTitle(this.getPreferredTitle() + " [" 
        + Tools.shortenFileName(selectedFile.getAbsolutePath(), 40)  + "]");
        DiasdemProject.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
        selectedFile.getAbsolutePath());
        DiasdemProject.quickSave();
        DiasdemGuiPreferences.setProperty("MRU_BATCH_SCRIPT_FILE_NAME",
        selectedFile.getAbsolutePath());
        DiasdemGuiPreferences.quickSave();
      }
    }  
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void open() {
    
    if ((DiasdemScript != null && DiasdemScript.countScriptTasks() > 0
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
    else if (DiasdemProject != null && DiasdemProject.getStringProperty(
      "MRU_BATCH_SCRIPT_FILE_NAME").length() > 0) {
      GuiFileChooser = new JFileChooser(DiasdemProject
      .getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME"));
    }
    else {
      GuiFileChooser = new JFileChooser(DiasdemGuiPreferences
      .getMruBatchScriptFileName());
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Batch Script File to be Extended");
    GuiFileChooser.setFileFilter(DIAsDEMproject.SCRIPT_FILE_FILTER);
    int result = GuiFileChooser.showOpenDialog(DiasdemGui.getJFrame());
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      DIAsDEMproject.SCRIPT_FILE_EXTENSION);
      BatchScriptDirectory = GuiFileChooser.getCurrentDirectory();
      if (Tools.isExistingFile(selectedFile.getAbsolutePath())) {
        try {
          DiasdemScript = new DefaultDIAsDEMscript(selectedFile
          .getAbsolutePath());
          this.setControlPanelContainerTitle(this.getPreferredTitle() + " ["
          + Tools.shortenFileName(selectedFile.getAbsolutePath(), 40)  + "]");
          Button_Panel.setEnabled(0, false);  // Open
          Button_Panel.setEnabled(1, false);  // Start
          Button_Panel.setEnabled(2, true);   // Stop
          Button_Panel.setEnabled(3, false);  // Save
          IsRecordingBatchScript = true;
          BatchScriptHasBeenSaved = false;
          DiasdemServer.startRecordingScript(DiasdemScript);
        }
        catch (Exception e) {
          JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
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