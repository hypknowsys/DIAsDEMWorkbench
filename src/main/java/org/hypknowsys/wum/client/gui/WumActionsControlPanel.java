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
import org.hypknowsys.misc.util.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.client.gui.tools.htmlDocumentViewer.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class WumActionsControlPanel extends WumControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected int PriorWumGuiStatus = 0;
  protected String WumTaskHelpResource = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  protected int DO_NOT_MODIFY_WUM_GUI_STATUS = -12345;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public WumActionsControlPanel() {
  
    super();
     
  }

  /* ########## ########## ########## ########## ########## ######### */

  public WumActionsControlPanel(Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
     
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

    if (ActionCommand.equals("KInternalFrame:EscapePressed")) {
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
    
    PriorWumGuiStatus = WumGui.getGuiStatus();    
    GuiTimer = new javax.swing.Timer(ONE_SECOND, this);

    // subclasses must override this.createParameterPanel()
    this.createButtonPanel();
    this.createParameterPanel();

    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 

    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));   
    if (Parameter_Panel != null) {
      this.setLayout( new BorderLayout() );
      this.add((KPanel)Parameter_Panel, BorderLayout.CENTER);
      this.add(Button_Panel, BorderLayout.SOUTH);
    }
    else {      
      this.setLayout( new BorderLayout() );
      this.add(Button_Panel, BorderLayout.SOUTH);
    }

  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {

    if (WumGui != null 
    && PriorWumGuiStatus != DO_NOT_MODIFY_WUM_GUI_STATUS) {
      WumGui.setGuiStatus(PriorWumGuiStatus);
    }
    super.finalize();
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    
    if (Parameter_Panel != null) {
      return Parameter_Panel.getPreferredTitle();
    }
    else {
      return "WUM Action Control Panel";
    }
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createButtonPanel() {  

    Button_Panel = new KButtonPanel(17, 0, 0, 0, 3, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("OK", 
      KeyEvent.VK_O, true, true, "OK", this);
    Button_Panel.addNextButton("Cancel", 
      0, true, false, "Cancel", this);
    Button_Panel.addLastButton("Help", 
      KeyEvent.VK_H, (WumTaskHelpResource == null ? false : true),
      false, "Help", this);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {  

    Parameter_Panel = null;
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void timerEvent(String pTaskLabel) {
    
    if (GuiInternalProgressMonitor != null
    && GuiInternalProgressMonitor.isCanceled()) {
      WumServer.stopNonBlockingTask();
      WumGui.logWarningMessage("Task stopped: " + pTaskLabel);
      this.setControlPanelContainerClosed(true);
    }
    Progress = WumServer.getTaskProgress();
    if (GuiInternalProgressMonitor != null) {
      if (Progress.getValue() == TaskProgress.INDETERMINATE)
        GuiInternalProgressMonitor.setIndeterminate(true);
      else
        GuiInternalProgressMonitor.setProgress(Progress.getValue());
      GuiInternalProgressMonitor.setNote(Progress.getNote());
    }
    if (WumServer.getTaskStatus() == Task.TASK_FINISHED) {
      WumGui.logInfoMessage("Task finished: " + pTaskLabel);
      GuiInternalProgressMonitor.setProgress(Progress.getValue());
      GuiInternalProgressMonitor.setNote(Progress.getNote());
      GuiInternalProgressMonitor.close();
      Result = WumServer.getTaskResult();
      if (Result.getStatus() == TaskResult.NO_RESULT) {
        JOptionPane.showInternalMessageDialog(
        WumGui.getKDesktopPane(), Result.getDescription(),
        pTaskLabel, JOptionPane.WARNING_MESSAGE);
      }
      if (Result.getStatus() == TaskResult.FINAL_RESULT) {
        JOptionPane.showInternalMessageDialog(
        WumGui.getKDesktopPane(), Result.getDescription(),
        pTaskLabel, JOptionPane.INFORMATION_MESSAGE);
      }
      this.setControlPanelContainerClosed(true);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void help() {
    
    if (WumTaskHelpResource != null) {
      try {
        File helpFile = File.createTempFile("wumHelp", ".html");
        Tools.copyTextualSystemResource(WumTaskHelpResource,
        helpFile.getAbsolutePath());
        HtmlDocumentViewerControlPanel panel = new
        HtmlDocumentViewerControlPanel();
        panel.setContext(WumServer, WumProject, WumGui,
        WumGuiPreferences);
        panel.setFileName(helpFile.getAbsolutePath());
        panel.setIsHelpDisplay(true);
        panel.initialize();
        KInternalFrame editor = WumGui
        .launchTaskControlPanelAsInternalFrame(panel);
        if (editor != null) {
          editor.setTitle("Help [" + this.getPreferredTitle() + "]");
        }
      }
      catch (IOException ignore) {
        JOptionPane.showInternalMessageDialog(
        WumGui.getKDesktopPane(), "Error: Help page cannot be displayed!",
        this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void startNonBlockingTask(String pTaskClassName, 
  String pTaskLabel, String pInitialProgressMessage, 
  String pInitialProgressNote, int pProgressMin, int pProgressMax, 
  boolean pIndeterminate, String pCancelOption) {
    
    AbstractValidatedTaskParameter validatedParameter =
    WumServer.validateTaskParameter(WumProject,
    pTaskClassName, Parameter_Panel.getTaskParameter());
    
    if (this.isValidParameter(validatedParameter)) {
      CloseIfEscapeIsPressed = false;
      Parameter_Panel.saveCurrentParameterSettingsAsDefaults();
      if (ControlPanelContainer != null) {
        ControlPanelContainer.setVisible(false);
      }
      WumGui.logInfoMessage("Task started: " + pTaskLabel);
      GuiTimer.start();
      
      GuiInternalProgressMonitor = new KInternalProgressMonitor(
      WumGui.getJFrame(), pInitialProgressMessage, pInitialProgressNote,
      pProgressMin, pProgressMax, pIndeterminate, pTaskLabel, pCancelOption,
      true, WumGui.getKDesktopPane());
      WumServer.startNonBlockingTask(WumProject, pTaskClassName,
      Parameter_Panel.getTaskParameter());
    }
    
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