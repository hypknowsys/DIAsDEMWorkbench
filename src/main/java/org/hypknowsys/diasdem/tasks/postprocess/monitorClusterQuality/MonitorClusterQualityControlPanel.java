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

package org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality;

import java.lang.reflect.*;
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
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.client.gui.*;
import org.hypknowsys.diasdem.client.gui.tools.clusterLabelEditor.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class MonitorClusterQualityControlPanel extends DiasdemActionsControlPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String TASK_LABEL =
  "Monitor Cluster Quality";
  private String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality.MonitorClusterQualityTask";
  private String INITIAL_PROGRESS_MESSAGE =
  "Please Wait: Monitoring Cluster Quality ...";
  private String INITIAL_PROGRESS_NOTE =
  "Initial Preparations";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityControlPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityControlPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
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
    
    if (ActionSource == GuiTimer) {
      this.timerEvent(TASK_LABEL);
    } else if (ActionCommand.equals("OK")) {
      super.startNonBlockingTask(TASK_CLASS_NAME, TASK_LABEL, 
      INITIAL_PROGRESS_MESSAGE, INITIAL_PROGRESS_NOTE, 0, 100, true, "Stop");
    } else if (ActionCommand.equals("Cancel")) {
      DiasdemServer.stopNonBlockingTask();
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
    DiasdemGui.setGuiStatus(GuiClient.PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    Parameter_Panel = new MonitorClusterQualityParameterPanel(DiasdemServer,
    DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void timerEvent(String pTaskLabel) {
    
    if (GuiInternalProgressMonitor != null
    && GuiInternalProgressMonitor.isCanceled()) {
      DiasdemServer.stopNonBlockingTask();
      DiasdemGui.logWarningMessage("Task stopped: " + pTaskLabel);
      this.setControlPanelContainerClosed(true);
    }
    Progress = DiasdemServer.getTaskProgress();
    if (GuiInternalProgressMonitor != null) {
      if (Progress.getValue() == TaskProgress.INDETERMINATE)
        GuiInternalProgressMonitor.setIndeterminate(true);
      else
        GuiInternalProgressMonitor.setProgress(Progress.getValue());
      GuiInternalProgressMonitor.setNote(Progress.getNote());
    }
    if (DiasdemServer.getTaskStatus() == Task.TASK_FINISHED) {
      DiasdemGui.logInfoMessage("Task finished: " + pTaskLabel);
      GuiInternalProgressMonitor.setProgress(Progress.getValue());
      GuiInternalProgressMonitor.setNote(Progress.getNote());
      GuiInternalProgressMonitor.close();
      Result = DiasdemServer.getTaskResult();
      if (Result.getStatus() == TaskResult.NO_RESULT) {
        JOptionPane.showInternalMessageDialog(
        DiasdemGui.getKDesktopPane(), Result.getDescription(),
        pTaskLabel, JOptionPane.WARNING_MESSAGE);
      }
      if (Result.getStatus() == TaskResult.FINAL_RESULT) {
        if (Parameter_Panel.getTaskParameter() != null  
        && ((MonitorClusterQualityParameter)Parameter_Panel.getTaskParameter())
        .launchHtmlBrowser()) {
          try {
            Runtime.getRuntime().exec(DiasdemGuiPreferences.getStringProperty(
            "EXTERNAL_BROWSER") + " " + Tools.ensureTrailingSlash(
            ((MonitorClusterQualityParameter)Parameter_Panel.getTaskParameter())
            .getClusterDirectory()) + "index.html");
          } catch (java.io.IOException e1) {
            JOptionPane.showMessageDialog(this,
            "Your preferred Web browser cannot be launched!" +
            "\nPlease check Tools -> Options -> External Programs.",
            pTaskLabel, JOptionPane.WARNING_MESSAGE);
          }
        }
        if (Parameter_Panel.getTaskParameter() != null
        && ((MonitorClusterQualityParameter)Parameter_Panel.getTaskParameter())
        .launchClusterLabelEditor()) {
          ClusterLabelEditorControlPanel panel = new
          ClusterLabelEditorControlPanel();
          panel.setContext(DiasdemServer, DiasdemProject, DiasdemGui,
          DiasdemGuiPreferences);
          panel.setClusterLabelFile(new File (((MonitorClusterQualityParameter)
          Parameter_Panel.getTaskParameter()).getClusterLabelFileName()));
          panel.initialize();
          KInternalFrame editor = DiasdemGui
          .launchTaskControlPanelAsInternalFrame(panel);
          if (editor != null) {
            editor.setTitle(panel.getPreferredTitle() + " [" + Tools
            .shortenFileName(panel.getClusterLabelFile().getAbsolutePath(), 50)
            + "]");
          }
        }
        JOptionPane.showInternalMessageDialog(
        DiasdemGui.getKDesktopPane(), Result.getDescription(),
        pTaskLabel, JOptionPane.INFORMATION_MESSAGE);
      }
      this.setControlPanelContainerClosed(true);
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