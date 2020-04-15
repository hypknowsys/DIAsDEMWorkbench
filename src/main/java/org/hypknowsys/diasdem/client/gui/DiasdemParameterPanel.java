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

package org.hypknowsys.diasdem.client.gui;

import java.beans.*;
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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class DiasdemParameterPanel extends KBorderPanel 
implements TaskParameterPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Server DiasdemServer = null;
  protected Project DiasdemProject = null;
  protected GuiClient DiasdemGui = null;
  protected GuiClientPreferences DiasdemGuiPreferences = null;

  protected File CurrentProjectDirectory = null;
  protected File CurrentParameterDirectory = null;
  protected boolean SaveRequired = false;
  protected boolean IsEnabled = true;
  
  protected KGridBagPanel Parameter_Panel = null;
  
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

  public DiasdemParameterPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public DiasdemParameterPanel(Server pDiasdemServer, Project pDiasdemProject,
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
  
    super();
    
    this.setContext(pDiasdemServer, pDiasdemProject, pDiasdemGui,
    pDiasdemGuiPreferences);
    
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
     
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskParameterPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContext(Server pDiasdemServer, Project pDiasdemProject,
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
  
    DiasdemServer = pDiasdemServer;
    DiasdemProject = pDiasdemProject;
    DiasdemGui = pDiasdemGui;
    DiasdemGuiPreferences = pDiasdemGuiPreferences;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
    
    if (Tools.isValidandWriteableDirectoryName(
    DiasdemGuiPreferences.getMruProjectDirectory())) {
      CurrentProjectDirectory = new File(
      DiasdemGuiPreferences.getMruProjectDirectory());
    }
    if (Tools.isValidandWriteableDirectoryName(
    DiasdemGuiPreferences.getMruParameterDirectory())) {
      CurrentParameterDirectory = new File(
      DiasdemGuiPreferences.getMruParameterDirectory());
    }
    if (DiasdemProject != null && Tools.isValidandWriteableDirectoryName(
    DiasdemProject.getProjectDirectory())) {
      CurrentProjectDirectory = new File(
      DiasdemProject.getProjectDirectory());
    }
    if (DiasdemProject != null && Tools.isValidandWriteableDirectoryName(
    DiasdemProject.getParameterDirectory())) {
      CurrentParameterDirectory = new File(
      DiasdemProject.getParameterDirectory());
    }
    
    SaveRequired = false;
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {}
  
  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    return "DIAsDEM Parameter Panel";
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public TaskParameter getTaskParameter() {
    return null; }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setTaskParameter(TaskParameter pTaskParameter) {}
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void saveCurrentParameterSettingsAsDefaults() {}
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getInitialFocusComponent() { 
    return null; }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getAsComponent() { 
    return (Component)this; }

  /* ########## ########## ########## ########## ########## ######### */  

  public boolean getSaveRequired() { 
    return SaveRequired; }

  /* ########## ########## ########## ########## ########## ######### */  

  public void setSaveRequired(boolean pSaveRequired) {
    SaveRequired = pSaveRequired; }

  /* ########## ########## ########## ########## ########## ######### */  

  public boolean getEnabled() { 
    return IsEnabled; }

  /* ########## ########## ########## ########## ########## ######### */  

  public void setEnabled(boolean pEnabled) {
    IsEnabled = pEnabled; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected File fileNameButtonClicked(KTextField pFileNameTextField,
  File pPrimaryDefaultDirectory, String pSecondaryDefaultProperty, 
  String pApproveButtonText, int pApproveButtonMnemonic, 
  String pApproveButtonToolTipText, String pDialogTitle) {
    
    KFileFilter[] nullKFileFilter = new KFileFilter[0];
    
    return this.fileNameButtonClicked(pFileNameTextField, 
    pPrimaryDefaultDirectory, pSecondaryDefaultProperty, pApproveButtonText, 
    pApproveButtonMnemonic, pApproveButtonToolTipText, pDialogTitle, 
    nullKFileFilter, true, false);
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  protected File fileNameButtonClicked(KTextField pFileNameTextField,
  File pPrimaryDefaultDirectory, String pSecondaryDefaultProperty, 
  String pApproveButtonText, int pApproveButtonMnemonic, 
  String pApproveButtonToolTipText, String pDialogTitle, 
  KFileFilter pFileFilter, boolean pAcceptAllFileFilterUsed,
  boolean pEnsureDefaultFileExtension) {
    
    KFileFilter[] fileFilters = null;
    if (pFileFilter != null) {
      fileFilters = new KFileFilter[1];
      fileFilters[0] = pFileFilter;
    }
    return this.fileNameButtonClicked(pFileNameTextField, 
    pPrimaryDefaultDirectory, pSecondaryDefaultProperty, pApproveButtonText, 
    pApproveButtonMnemonic, pApproveButtonToolTipText, pDialogTitle, 
    fileFilters, pAcceptAllFileFilterUsed, pEnsureDefaultFileExtension);
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  protected File fileNameButtonClicked(KTextField pFileNameTextField,
  File pPrimaryDefaultDirectory, String pSecondaryDefaultProperty, 
  String pApproveButtonText, int pApproveButtonMnemonic, 
  String pApproveButtonToolTipText, String pDialogTitle, 
  KFileFilter[] pFileFilters, boolean pAcceptAllFileFilterUsed,
  boolean pEnsureDefaultFileExtension) {
    
    if (pFileNameTextField == null) {
      return pPrimaryDefaultDirectory;
    }
    File file = new File(pFileNameTextField.getText());
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      if (pPrimaryDefaultDirectory != null) {
        GuiFileChooser = new JFileChooser(pPrimaryDefaultDirectory);
      }
      else {
        GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
        pSecondaryDefaultProperty));
      }
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setMultiSelectionEnabled(false);
    GuiFileChooser.setAcceptAllFileFilterUsed(pAcceptAllFileFilterUsed);
    if (pDialogTitle != null) {
      GuiFileChooser.setDialogTitle(pDialogTitle);
    }
    if (pFileFilters != null) {
      for (int i = 0; i < pFileFilters.length; i++) {
        GuiFileChooser.addChoosableFileFilter(pFileFilters[i]);
        if (i == 0) {
          GuiFileChooser.setFileFilter(pFileFilters[0]);
        }
      }      
    }
    int result = 0;
    if (pApproveButtonText != null) {
      GuiFileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
      GuiFileChooser.setApproveButtonText(pApproveButtonText);
      GuiFileChooser.setApproveButtonMnemonic(pApproveButtonMnemonic);
      if (pApproveButtonToolTipText != null) {
        GuiFileChooser.setApproveButtonToolTipText(pApproveButtonToolTipText);
      }
      result = GuiFileChooser.showDialog(this, null);
    }
    else {
      result = GuiFileChooser.showOpenDialog(this);
    }
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = null;
      if (pFileFilters != null && pEnsureDefaultFileExtension 
      && GuiFileChooser.getFileFilter() instanceof KFileFilter) {
        selectedFile = ( (KFileFilter)GuiFileChooser.getFileFilter() )
        .ensureDefaultFileExtension(GuiFileChooser.getSelectedFile());
      }
      else {
        selectedFile = GuiFileChooser.getSelectedFile();
      }
      if (pPrimaryDefaultDirectory != null) {
        pPrimaryDefaultDirectory = GuiFileChooser.getCurrentDirectory();
      }
      pFileNameTextField.setText(selectedFile.getAbsolutePath());
      pFileNameTextField.setCaretAtEnding();
    }
    
    return pPrimaryDefaultDirectory;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected File directoryButtonClicked(KTextField pFileNameTextField,
  File pPrimaryDefaultDirectory, String pSecondaryDefaultProperty, 
  String pApproveButtonText, int pApproveButtonMnemonic, 
  String pApproveButtonToolTipText, String pDialogTitle) {
    
    if (pFileNameTextField == null) {
      return pPrimaryDefaultDirectory;
    }
    File file = new File(pFileNameTextField.getText());
    if (file.exists() && file.isDirectory()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      if (pPrimaryDefaultDirectory != null) {
        GuiFileChooser = new JFileChooser(pPrimaryDefaultDirectory);
      }
      else {
        GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
        pSecondaryDefaultProperty));
      }
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    GuiFileChooser.setMultiSelectionEnabled(false);
    if (pDialogTitle != null) {
      GuiFileChooser.setDialogTitle(pDialogTitle);
    }
    int result = 0;
    if (pApproveButtonText != null) {
      GuiFileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
      GuiFileChooser.setApproveButtonText(pApproveButtonText);
      GuiFileChooser.setApproveButtonMnemonic(pApproveButtonMnemonic);
      if (pApproveButtonToolTipText != null) {
        GuiFileChooser.setApproveButtonToolTipText(pApproveButtonToolTipText);
      }
      result = GuiFileChooser.showDialog(this, null);
    }
    else {
      result = GuiFileChooser.showOpenDialog(this);
    }
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = GuiFileChooser.getSelectedFile();
      if (pPrimaryDefaultDirectory != null) {
        pPrimaryDefaultDirectory = GuiFileChooser.getCurrentDirectory();
      }
      pFileNameTextField.setText(selectedFile.getAbsolutePath());
      pFileNameTextField.setCaretAtEnding();
    }
    
    return pPrimaryDefaultDirectory;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}