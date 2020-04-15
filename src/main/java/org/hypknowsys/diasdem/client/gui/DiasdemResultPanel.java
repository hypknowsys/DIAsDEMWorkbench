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
  
public abstract class DiasdemResultPanel extends KBorderPanel 
implements TaskResultPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Server DiasdemServer = null;
  protected Project DiasdemProject = null;
  protected GuiClient DiasdemGui = null;
  protected GuiClientPreferences DiasdemGuiPreferences = null;

  protected File CurrentProjectDirectory = null;
  protected File CurrentParameterDirectory = null;
  
  protected KGridBagPanel Result_Panel = null;
  
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

  public DiasdemResultPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public DiasdemResultPanel(Server pDiasdemServer, Project pDiasdemProject,
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
  /* ########## interface TaskResultPanel methods */
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
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {}
  
  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    return "DIAsDEM Result Panel";
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public TaskResult getTaskResult() {
    return null; }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setTaskResult(TaskResult pTaskResult) {}
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getInitialFocusComponent() { 
    return null; }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getAsComponent() { 
    return (Component)this; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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