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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KInternalFrame extends JInternalFrame 
implements InternalFrameListener, ActionListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected JFrame Parent_Frame = null;
  protected KProgressMonitor GuiProgressMonitor = null;
  protected KInternalProgressMonitor GuiInternalProgressMonitor = null;
  protected JFileChooser GuiFileChooser = null;
  protected Timer GuiTimer = null;
  protected Component InitialFocusComponent = null;
  protected Component MostRecentFocusComponent = null;
  protected JButton DefaultButton = null;
  protected JFrame ParentJFrame = null;
  protected JDesktopPane ParentJDesktopPane = null;
  protected boolean InitialFocusSet = false;
  protected Component MyTopLevelComponent = null;
  
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

  public KInternalFrame() {

    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public KInternalFrame(String pTitle, boolean pResizable, 
    boolean pCloseable, boolean pMaximizable, boolean pIconifiable) {

    this(pTitle, pResizable, pCloseable, pMaximizable, pIconifiable,
    200, 100, null);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public KInternalFrame(String pTitle, boolean pResizable, 
    boolean pCloseable, boolean pMaximizable, boolean pIconifiable,
    int pPreferredSizeX, int pPreferredSizeY) {

    this(pTitle, pResizable, pCloseable, pMaximizable, pIconifiable,
    pPreferredSizeX, pPreferredSizeY, null);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public KInternalFrame(String pTitle, boolean pResizable, 
    boolean pCloseable, boolean pMaximizable, boolean pIconifiable,
    int pPreferredSizeX, int pPreferredSizeY, Component pComponent) {

    super();
    
    this.setContext(pTitle, pResizable, pCloseable, pMaximizable, pIconifiable,
    pPreferredSizeX, pPreferredSizeY, pComponent);
    this.setOpened(true);
      
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
    return ParentJFrame; }
  public JDesktopPane getParentJDesktopPane() { 
    return ParentJDesktopPane; }
  public Component getMostRecentFocusComponent() { 
    return MostRecentFocusComponent; }

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
  public void setParentJFrame(JFrame pParentJFrame) { 
    ParentJFrame = pParentJFrame; }
  public void setParentJDesktopPane(JDesktopPane pParentJDesktopPane) { 
    ParentJDesktopPane = pParentJDesktopPane; }

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
      // System.out.println(".");
    } else if (ActionCommand.equals("KInternalFrame:EscapePressed")) {
      // System.out.println("EscapePressed");
      this.setClosed(true); 
    }
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface InternalFrameListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void internalFrameActivated(InternalFrameEvent e) {
  
    // work around for setting JINternalFrame focus correctly
    if (this.getDefaultButton() != null) {
      this.getRootPane().setDefaultButton(this.getDefaultButton());
    }
    if (InitialFocusSet == false) {
      MostRecentFocusComponent = this.getInitialFocusComponent();
      InitialFocusSet = true;
    }
    if (MostRecentFocusComponent != null) {
      FocusEvent lost = new FocusEvent(MostRecentFocusComponent, 
      FocusEvent.FOCUS_LOST);
      FocusEvent gained = new FocusEvent(MostRecentFocusComponent, 
      FocusEvent.FOCUS_GAINED);
      dispatchEvent(lost);
      dispatchEvent(gained);
      dispatchEvent(lost);
      MostRecentFocusComponent.requestFocus();
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void internalFrameClosed(InternalFrameEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void internalFrameClosing(InternalFrameEvent e) {
    this.setClosed(true); 
  } 

  /* ########## ########## ########## ########## ########## ######### */  

  public void internalFrameDeactivated(InternalFrameEvent e) {
    MostRecentFocusComponent = this.getMostRecentFocusOwner();  
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void internalFrameDeiconified(InternalFrameEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void internalFrameIconified(InternalFrameEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */  

  public void internalFrameOpened(InternalFrameEvent e) {}

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setContext(String pTitle, boolean pResizable, 
  boolean pCloseable, boolean pMaximizable, boolean pIconifiable,
  int pPreferredSizeX, int pPreferredSizeY, Component pMyTopLevelComponent) {
    
    super.setTitle(pTitle);
    super.setResizable(pResizable);
    super.setClosable(pCloseable);
    super.setMaximizable(pMaximizable);
    super.setIconifiable(pIconifiable);
    
    PreferredSizeX = pPreferredSizeX;
    PreferredSizeY = pPreferredSizeY;
    MyTopLevelComponent = pMyTopLevelComponent;
    
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
  
    if (super.isClosed == pClosed) {
      return;
    }
    if (pClosed) {
      try {
        this.finalize();
        if (this.isClosable()) {
          super.isClosed = pClosed;
          this.dispose();
         this.fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING);
        }
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
      this.removeInternalFrameListener(this);
      this.addInternalFrameListener(this);
      this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
      this.setPreferredSize(new Dimension(
      PreferredSizeX + this.getInsets().left + this.getInsets().right,
      PreferredSizeY + this.getInsets().top + this.getInsets().bottom));
      this.setSize(this.getPreferredSize());
      if (MyTopLevelComponent != null) {
//        final JDialog dialog4Size = new JDialog(Paren);
//        Container contentPane = dialog4Size.getContentPane();
//        contentPane.add(MyTopLevelComponent, BorderLayout.CENTER);
//        dialog4Size.pack();
//        this.setPreferredSize(dialog4Size.getPreferredSize());
//        this.setSize(this.getPreferredSize());
        Container guiContentPane = super.getContentPane();
        guiContentPane.add(MyTopLevelComponent, BorderLayout.CENTER);
      }
      if (this.getDefaultButton() != null) {
        this.getRootPane().setDefaultButton(this.getDefaultButton());
      }
      super.setVisible(true);
    }
    else {
      super.setVisible(false);
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredKDesktopLayout() {
    return KDesktopPane.CENTER;
  }
  
  /* ########## ########## ########## ########## ########## ######### */  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  // close internal frame when escape key is pressed: work around
  // http://www.javaworld.com/javaworld/javatips/jw-javatip72.html
  
  protected JRootPane createRootPane() {
    
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    JRootPane rootPane = new JRootPane();
    rootPane.registerKeyboardAction(this, "KInternalFrame:EscapePressed", 
    stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    return rootPane;
  
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