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
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class WumInternalFrame extends KInternalFrame implements TaskInternalFrame {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected Server WumServer = null;
  protected Project WumProject = null;
  protected GuiClient WumGui = null;
  protected GuiClientPreferences WumGuiPreferences = null;

  private TaskControlPanel Control_Panel = null;  
  
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

  public WumInternalFrame() {
  
    super();
        
  }

  /* ########## ########## ########## ########## ########## ######### */

  public WumInternalFrame(boolean pResizable, boolean pClosable, boolean pMaximizable, boolean pIconifiable, Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences, TaskControlPanel pControl_Panel, JFrame pParentFrame, JDesktopPane pParentDesktop) {
  
    super();
    
    this.setContext(pResizable, pClosable, pMaximizable, pIconifiable, 
    pWumServer, pWumProject, pWumGui, pWumGuiPreferences, 
    pControl_Panel, pParentFrame, pParentDesktop);
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

    if (ActionCommand.equals("KInternalFrame:EscapePressed")) {
      if (Control_Panel != null) {
        Control_Panel.actionPerformed(e);
      }
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskInternalFrame methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContext(Server pWumServer, Project pWumProject,
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    WumServer = pWumServer;
    WumProject = pWumProject;
    WumGui = pWumGui;
    WumGuiPreferences = pWumGuiPreferences;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setContext(boolean pResizable, boolean pCloseable, 
  boolean pMaximizable, boolean pIconifiable, Server pWumServer, 
  Project pWumProject, GuiClient pWumGui, 
  GuiClientPreferences pWumGuiPreferences, TaskControlPanel pControl_Panel,
  JFrame pParentFrame, JDesktopPane pParentDesktop) {
  
    super.setContext(pControl_Panel.getPreferredTitle(), pResizable, pCloseable, 
    pMaximizable, pIconifiable, pControl_Panel.getPreferredSizeX(), 
    pControl_Panel.getPreferredSizeY(), pControl_Panel.getAsComponent());
    
    WumServer = pWumServer;
    WumProject = pWumProject;
    WumGui = pWumGui;
    WumGuiPreferences = pWumGuiPreferences;

    Control_Panel = pControl_Panel;
    super.setParentJFrame(pParentFrame);
    super.setParentJDesktopPane(pParentDesktop);
    
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
      return "DIAsDEM Internal Frame";
    }
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getPreferredKDesktopLayout() {
    
    if (Control_Panel != null)
      return Control_Panel.getPreferredKDesktopLayout(); 
    else
      return KDesktopPane.CENTER;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public Component getInitialFocusComponent() {
    
    if (Control_Panel != null)
      InitialFocusComponent = Control_Panel.getInitialFocusComponent(); 
    return InitialFocusComponent; 
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public JButton getDefaultButton() {
    
    if (Control_Panel != null) {
      return Control_Panel.getDefaultButton(); 
    }
    else {
      return null; 
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public boolean controlPanelContainerIsVisible() {
    
    if (Control_Panel != null) {
      return Control_Panel.controlPanelContainerIsVisible();
    }
    else {
      return true;
    }
    
  }
  
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