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
  
public abstract class WumResultPanel extends KBorderPanel implements TaskResultPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Server WumServer = null;
  protected Project WumProject = null;
  protected GuiClient WumGui = null;
  protected GuiClientPreferences WumGuiPreferences = null;

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

  public WumResultPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public WumResultPanel(Server pWumServer, Project pWumProject, 
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    super();
    
    this.setContext(pWumServer, pWumProject, pWumGui,
    pWumGuiPreferences);
    
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
  
  public void setContext(Server pWumServer, Project pWumProject,
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    WumServer = pWumServer;
    WumProject = pWumProject;
    WumGui = pWumGui;
    WumGuiPreferences = pWumGuiPreferences;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void initialize() {
  
    if (Tools.isValidandWriteableDirectoryName(
    WumGuiPreferences.getMruProjectDirectory())) {
      CurrentProjectDirectory = new File(
      WumGuiPreferences.getMruProjectDirectory());
    }
    if (Tools.isValidandWriteableDirectoryName(
    WumGuiPreferences.getMruParameterDirectory())) {
      CurrentParameterDirectory = new File(
      WumGuiPreferences.getMruParameterDirectory());
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {}
  
  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    return "WUM Result Panel";
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