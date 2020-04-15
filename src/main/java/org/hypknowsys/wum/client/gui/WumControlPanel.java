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

package org.hypknowsys.wum.client.gui;

import java.io.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.core.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class WumControlPanel extends KPanel implements TaskControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Server WumServer = null;
  protected Project WumProject = null;
  protected GuiClient WumGui = null;
  protected GuiClientPreferences WumGuiPreferences = null;
  protected TaskProgress Progress = null;
  protected TaskResult Result = null;
  
  protected TaskControlPanelContainer ControlPanelContainer = null;  
  protected KButtonPanel Button_Panel = null;
  protected TaskParameterPanel Parameter_Panel = null;
  
  protected boolean CloseIfEscapeIsPressed = true;
  protected boolean ControlPanelContainerIsVisible = true;

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

  public WumControlPanel() {
  
    super();

    CloseIfEscapeIsPressed = true;
    ControlPanelContainerIsVisible = true;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public WumControlPanel(Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    super();
    
    this.setContext(pWumServer, pWumProject, pWumGui,
    pWumGuiPreferences);
    CloseIfEscapeIsPressed = true;
    ControlPanelContainerIsVisible = true;

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
  /* ########## interface TaskControlPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContext(Server pWumServer, Project pWumProject,
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    WumServer = pWumServer;
    WumProject = pWumProject;
    WumGui = pWumGui;
    WumGuiPreferences = pWumGuiPreferences;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void initialize() {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {

    if (Parameter_Panel != null) {
      Parameter_Panel.finalize();
    }
    if ( GuiTimer != null && GuiTimer.isRunning() ) {
      GuiTimer.stop();
    }
    GuiTimer = null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void setControlPanelContainerClosed(boolean pClosed) {

    if (ControlPanelContainer != null) {
      ControlPanelContainer.setClosed(pClosed);
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    
    if (Parameter_Panel != null) {
      return Parameter_Panel.getPreferredTitle();
    }
    else {
      return "WUM Control Panel";
    }
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    if (Parameter_Panel != null) {
      return Parameter_Panel.getPreferredSizeX();
    }
    else {
      return WumGuiPreferences.getIntProperty("DIALOG_M_SIZE_X");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    if (Parameter_Panel != null) {
      return Parameter_Panel.getPreferredSizeY();
    }
    else {
      return WumGuiPreferences.getIntProperty("DIALOG_M_SIZE_Y");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredKDesktopLayout() {
    return KDesktopPane.CENTER;
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setControlPanelContainer(TaskControlPanelContainer 
  pControlPanelContainer) {    
    
    ControlPanelContainer = pControlPanelContainer;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setControlPanelContainerTitle(String pTitle) {    

    if (ControlPanelContainer != null) {
      ControlPanelContainer.setTitle(pTitle);
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public String getControlPanelContainerTitle() {    

    if (ControlPanelContainer != null) {
      return ControlPanelContainer.getTitle();
    }
    else {
      return "";
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getInitialFocusComponent() {
    
    if (Parameter_Panel != null 
    && Parameter_Panel.getInitialFocusComponent() != null)
      return Parameter_Panel.getInitialFocusComponent();
    else
      if (Button_Panel != null)
        return Button_Panel.getDefaultButton();
      else
        return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public JButton getDefaultButton() {
    
    if (Button_Panel != null)
      return Button_Panel.getDefaultButton(); 
    else
      return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public boolean controlPanelContainerIsVisible() {
    
    return ControlPanelContainerIsVisible;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getAsComponent() { 
    return (Component)this; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected boolean isValidParameter(
  AbstractValidatedTaskParameter pValidatedParameter) {
    
    boolean commit = true;
    String title = this.getPreferredTitle();
    
    if (pValidatedParameter.numberOfErrors() > 0) {
      commit = false;
      for (int i = 0; i < pValidatedParameter.numberOfErrors(); i++) {
        JOptionPane.showMessageDialog(ControlPanelContainer
        .getParentJFrame(), pValidatedParameter.getError(i), title,
        JOptionPane.ERROR_MESSAGE);
      }
    }
    if (pValidatedParameter.numberOfWarnings() > 0 && commit == true
    && WumGuiPreferences.getShowWarnings()) {
      for (int i = 0; i < pValidatedParameter.numberOfWarnings(); i++) {
        int input = JOptionPane.showConfirmDialog(ControlPanelContainer
        .getParentJFrame(), pValidatedParameter.getWarning(i), title,
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (input == JOptionPane.NO_OPTION
        || input == JOptionPane.CLOSED_OPTION) {
          commit = false;
        }
      }
    }
    return commit;
    
  }
  
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
        GuiFileChooser = new JFileChooser(WumProject.getProperty(
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
        GuiFileChooser = new JFileChooser(WumProject.getProperty(
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