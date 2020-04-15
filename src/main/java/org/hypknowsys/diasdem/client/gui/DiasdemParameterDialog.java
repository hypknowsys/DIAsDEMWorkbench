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

import java.io.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class DiasdemParameterDialog extends KDialog implements TaskDialog {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Server DiasdemServer = null;
  protected Project DiasdemProject = null;
  protected GuiClient DiasdemGui = null;
  protected GuiClientPreferences DiasdemGuiPreferences = null;

  private TaskParameterControlPanel Control_Panel = null;  
  private TaskParameterPanel Parameter_Panel = null;  
  private TaskParameter Parameter = null;  

  protected Component MostRecentFocusComponent = null;
  protected boolean InitialFocusSet = false;
  
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
  
  public DiasdemParameterDialog(JFrame pParent_Frame) {
  
    super(pParent_Frame);
    
  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public DiasdemParameterDialog(Server pDiasdemServer, Project pDiasdemProject,
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences, 
  TaskParameter pParameter, TaskParameterPanel pParameterPanel) {
  
    super(pDiasdemGui.getJFrame(), pParameterPanel.getPreferredTitle(), true);

    this.setContext(pDiasdemGui.getJFrame(), pParameterPanel
    .getPreferredTitle(), true, pDiasdemServer, pDiasdemProject, pDiasdemGui,
    pDiasdemGuiPreferences, pParameter, pParameterPanel);
    this.setOpened(true);

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

    if (ActionCommand.equals("KDialog:EscapePressed")) {
      // System.out.println("EscapePressed");
      this.setClosed(true); 
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface WindowListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void windowOpened(WindowEvent e) {
  
    if (InitialFocusSet == false) {
      if (this.getDefaultButton() != null) {
        this.getRootPane().setDefaultButton(this.getDefaultButton());
      }
      MostRecentFocusComponent = this.getInitialFocusComponent();
      InitialFocusSet = true;
      if (MostRecentFocusComponent != null) {
        MostRecentFocusComponent.requestFocus();
      }    
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void windowClosing(WindowEvent e) { this.setClosed(true); }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskDialog methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContext(Server pDiasdemServer, Project pDiasdemProject,
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
  
    DiasdemServer = pDiasdemServer;
    DiasdemProject = pDiasdemProject;
    DiasdemGui = pDiasdemGui;
    DiasdemGuiPreferences = pDiasdemGuiPreferences;
    
  }
  
  public void setContext(JFrame pParent_Frame, String pTitle, boolean pModal,
  Server pDiasdemServer, Project pDiasdemProject,
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences, 
  TaskParameter pParameter, TaskParameterPanel pParameterPanel) {
    
    super.setContext(pParent_Frame, pTitle, pModal);
    this.setContext(pDiasdemServer, pDiasdemProject, pDiasdemGui, 
    pDiasdemGuiPreferences);
    Parameter = pParameter;
    Parameter_Panel = pParameterPanel;
    
  }

  public void initialize() {

    super.initialize();
    
    Control_Panel = new DiasdemParameterControlPanel(DiasdemServer, 
    DiasdemProject, DiasdemGui, DiasdemGuiPreferences, Parameter, 
    Parameter_Panel);
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {

    if (Control_Panel != null) {
      Control_Panel.finalize();
    }
    super.finalize();

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void setVisible(boolean pVisible) {
  
    if (pVisible == true) {
      this.getContentPane().add((JPanel)Control_Panel);
      Control_Panel.setControlPanelContainer(this);
      this.pack();
      if (Control_Panel!= null) {
        super.setVisible(Control_Panel.controlPanelContainerIsVisible());
      }
      else {
        super.setVisible(true);
      }
    }
    else {
      super.setVisible(false);
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    
    if (Control_Panel != null) {
      return Control_Panel.getPreferredTitle();
    }
    else {
      return "DIAsDEM Parameter Dialog";
    }
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    if (Control_Panel != null)
      return Control_Panel.getTaskParameter();
    else
      return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public boolean isCanceled() {
   
    if (Control_Panel != null)
      return Control_Panel.isCanceled();
    else
      return true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getInitialFocusComponent() {
    
    if (Control_Panel != null)
      InitialFocusComponent = Control_Panel.getInitialFocusComponent(); 
    return InitialFocusComponent; 
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public JButton getDefaultButton() {
    
    if (Control_Panel != null)
      return Control_Panel.getDefaultButton(); 
    else
      return null; 
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public boolean controlPanelContainerIsVisible() {
    
    if (Control_Panel != null)
      return Control_Panel.controlPanelContainerIsVisible();
    else
      return true;
    
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
  
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static TaskParameter showDiasdemParameterDialog(
  Server pDiasdemServer, Project pDiasdemProject,
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences, 
  TaskParameter pParameter, TaskParameterPanel pParameterPanel) {
    
    DiasdemParameterDialog dialog = new DiasdemParameterDialog(pDiasdemServer,
    pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences, pParameter, 
    pParameterPanel);
    if (dialog.isCanceled()) {
      dialog = null;
      return null;
    }
    else {
      pParameter = dialog.getTaskParameter();
      dialog = null;
      return pParameter;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}