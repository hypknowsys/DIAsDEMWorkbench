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

package org.hypknowsys.misc.swing;

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class KFrame extends JFrame implements WindowListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected KGuiPreferences GuiPreferences = null;

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

  public KFrame() {

    super();

    this.setContext("", new KDefaultGuiPreferences());

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KFrame(String pTitle, KGuiPreferences pGuiPreferences) {

    super();

    this.setContext(pTitle, pGuiPreferences);
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
    TmpStringBuffer.append("; super.toString()=");
    TmpStringBuffer.append(super.toString());
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface WindowListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void windowActivated(WindowEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void windowClosed(WindowEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void windowClosing(WindowEvent e) { this.setClosed(true); }

  /* ########## ########## ########## ########## ########## ######### */  

  public void windowDeactivated(WindowEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void windowDeiconified(WindowEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void windowIconified(WindowEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void windowOpened(WindowEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContext(String pTitle, KGuiPreferences pGuiPreferences) {
    
    super.setTitle(pTitle);
    GuiPreferences = pGuiPreferences;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOpened(boolean pOpened) {
  
    if (pOpened) {
      this.initialize();
      this.setVisible(true);
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setClosed(boolean pClosed) {
  
    if (pClosed) {
      try {
        this.finalize();
        this.setVisible(false);
        this.dispose();
      }
      catch (PropertyVetoException e) {
        System.out.println("Warning: KFrame cannot be closed!");
      }
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void initialize() {
  
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {}
  
  /* ########## ########## ########## ########## ########## ######### */  
  
  public void setVisible(boolean pVisible) {
  
    if (pVisible == true) {
      this.removeWindowListener(this);
      this.addWindowListener(this); 
      if (GuiPreferences != null) {
        this.setSize(this.getInsets().left + this.getInsets().right
        + GuiPreferences.getFrameSizeX(), this.getInsets().top
        + this.getInsets().bottom + GuiPreferences.getFrameSizeY());
        this.setLocation(new Point(GuiPreferences.getFramePositionX(),
        GuiPreferences.getFramePositionY()));
      }
      super.setVisible(true);
    }
    else {
      super.setVisible(false);
    }
  
  }

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
  
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}