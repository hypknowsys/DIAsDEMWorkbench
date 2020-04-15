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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class DiasdemParameterControlPanel extends DiasdemControlPanel 
implements TaskParameterControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private boolean IsCanceled = true;  
  protected TaskParameter Parameter = null;

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

  public DiasdemParameterControlPanel() {
  
    super();
    
    IsCanceled = true;
    Parameter = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public DiasdemParameterControlPanel(Server pDiasdemServer, 
  Project pDiasdemProject, GuiClient pDiasdemGui, 
  GuiClientPreferences pDiasdemGuiPreferences, 
  TaskParameter pParameter, TaskParameterPanel pParameterPanel) {
  
    super();
    
    this.setContext(pDiasdemServer, pDiasdemProject, pDiasdemGui,
    pDiasdemGuiPreferences, pParameter, pParameterPanel);
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

    if ( ActionCommand.equals("OK") ) {
      TaskParameter parameter = Parameter_Panel.getTaskParameter();
      AbstractValidatedTaskParameter validatedParameter =
      DiasdemServer.validateTaskParameter((Project)DiasdemProject,
      parameter.getTaskClassName(), parameter);
      boolean commit = this.isValidParameter(validatedParameter);
      if (commit) {   
        IsCanceled = false;
        ControlPanelContainer.setClosed(true);
      }
    } else
    if ( ActionCommand.equals("Cancel") ) { 
      ControlPanelContainer.setClosed(true);    
    } 

  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskParameterControlPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContext(Server pDiasdemServer, Project pDiasdemProject,
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences, 
  TaskParameter pParameter, TaskParameterPanel pParameterPanel) {
  
    DiasdemServer = pDiasdemServer;
    DiasdemProject = pDiasdemProject;
    DiasdemGui = pDiasdemGui;
    DiasdemGuiPreferences = pDiasdemGuiPreferences;
    
    Parameter_Panel = pParameterPanel;
    Parameter_Panel.setTaskParameter(pParameter);
    Parameter = pParameter;
    IsCanceled = true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void initialize() {
        
    super.initialize();
    
    GuiTimer = new javax.swing.Timer(ONE_SECOND, this);

    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 
    
    this.createButtonPanel();

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

  public String getPreferredTitle() {
    
    if (Parameter_Panel != null) {
      return Parameter_Panel.getPreferredTitle();
    }
    else {
      return "DIAsDEM Parameter Control Panel";
    }
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {   
    
    if (Parameter_Panel != null) {
      return Parameter_Panel.getTaskParameter(); 
    }
    else {
      return null;
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void setTaskParameter(TaskParameter pTaskParameter) {   
    
    if (Parameter_Panel != null) {
      Parameter_Panel.setTaskParameter(pTaskParameter); 
    }

  }
    
  /* ########## ########## ########## ########## ########## ######### */  

  public boolean isCanceled() {
   
    return IsCanceled;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createButtonPanel() {  

    Button_Panel = new KButtonPanel(17, 0, 0, 0, 3, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("OK", 
      KeyEvent.VK_O, true, true, "OK", this);
    Button_Panel.addNextButton("Cancel", 
      0, true, false, "Cancel", this);
    Button_Panel.addLastButton("Help", 
      KeyEvent.VK_H, false, false, "Help", this);
    
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