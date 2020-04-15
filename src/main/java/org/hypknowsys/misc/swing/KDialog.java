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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KDialog extends JDialog implements WindowListener, ActionListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected JFrame Parent_Frame = null;
  protected KProgressMonitor GuiProgressMonitor = null;
  protected JFileChooser GuiFileChooser = null;
  protected Timer GuiTimer = null;
  protected Component InitialFocusComponent = null;
  protected JButton DefaultButton = null;

  protected int PreferredSizeX = 0;
  protected int PreferredSizeY = 0;
  
  protected String ActionCommand = null;
  protected Object ActionSource = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  protected final static int ONE_SECOND = 1000;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KDialog(JFrame pParent_Frame) {

    this(pParent_Frame, "", true);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KDialog(JFrame pParent_Frame, String pTitle, boolean pModal) {
  
    super(pParent_Frame);

    this.setContext(pParent_Frame, pTitle, pModal);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public int getPreferredSizeX() { 
    return PreferredSizeX; }
  public int getPreferredSizeY() {
    return PreferredSizeY; }
  public Component getInitialFocusComponent() { 
    return InitialFocusComponent; } 
  public JButton getDefaultButton() { 
    return DefaultButton; } 
  public JFrame getParentJFrame() {
    return Parent_Frame; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setPreferredSizeX(int pPreferredSizeX) { 
    PreferredSizeX = pPreferredSizeX; }
  public void setPreferredSizeY(int pPreferredSizeY) { 
    PreferredSizeY = pPreferredSizeY; }
  public void setInitialFocusComponent(Component pInitialFocusComponent) { 
    InitialFocusComponent = pInitialFocusComponent; }
  public void setDefaultButton(JButton pDefaultButton) { 
    DefaultButton = pDefaultButton; } 
  public void setParentJFrame(JFrame pParent_Frame) { 
    Parent_Frame = pParent_Frame; }

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
  /* ########## interface ActionListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void actionPerformed(ActionEvent e) {
  
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();

    if (ActionSource == GuiTimer) {
      // System.out.println(".");
    } else if (ActionCommand.equals("KDialog:EscapePressed")) {
      // System.out.println("EscapePressed");
      this.setClosed(true);
    }
        
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

  public void setContext(JFrame pParent_Frame, String pTitle, boolean pModal) {
    
    if (pParent_Frame != null) {
      Parent_Frame = pParent_Frame;
    }
    super.setTitle(pTitle);
    super.setModal(pModal);
    
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
      catch (PropertyVetoException e) {}
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void initialize() {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {
    
    if (GuiTimer != null && GuiTimer.isRunning()) {
      GuiTimer.stop();
    }
    GuiTimer = null;
    
  }
       
  /* ########## ########## ########## ########## ########## ######### */  
  
  public void setVisible(boolean pVisible) {
  
    if (pVisible == true) {
      this.removeWindowListener(this);
      this.addWindowListener(this); 
      Rectangle bounds = this.getParent().getBounds();
      Rectangle abounds = this.getBounds();
      this.setLocation(bounds.x + (bounds.width - abounds.width) / 2, 
      bounds.y + (bounds.height - abounds.height) / 2);
      super.setVisible(true);
    }
    else {
      super.setVisible(false);
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void setLocationAndAddListener() {
  
    this.removeWindowListener(this);
    this.addWindowListener(this); 
    this.pack();
    Rectangle bounds = this.getParent().getBounds();
    Rectangle abounds = this.getBounds();
    this.setLocation(bounds.x + (bounds.width - abounds.width) / 2,
    bounds.y + (bounds.height - abounds.height) / 2);
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  // close dialog when escape key is pressed: work around
  // http://www.javaworld.com/javaworld/javatips/jw-javatip72.html

  protected JRootPane createRootPane() {
    
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    JRootPane rootPane = new JRootPane();
    rootPane.registerKeyboardAction(this, "KDialog:EscapePressed", 
    stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    return rootPane;
  
  }

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