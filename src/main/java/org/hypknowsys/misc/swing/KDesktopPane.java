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
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.util.*;

/**
 * KDesktopPane manages a set of JInternalFrames, all internal frame
 * should have a valid preferred size that is used to cascade frames
 */


// _C_ascade
// Tile _V_ertically
// Tile _H_orizontally
// --
// _R_estore All
// Ma_ximize All
// Mi_n_imize All
// Close All
// ---
// 1 Title
// 2 Title

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KDesktopPane extends JDesktopPane
implements InternalFrameListener, ActionListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected HashMap InternalFrames = null;

  protected int NextInternalFrameID = 1; 
  protected int InternalFrameXOffset = 30;
  protected int InternalFrameYOffset = 30;

  protected Stack ActivityStack = null;
 
  protected JFrame ParentJFrame = null;
  
  protected JMenu Window_Menu = null;
  protected JMenuItem Cascade_MenuItem = null;
  protected JMenuItem TileVertically_MenuItem = null;
  protected JMenuItem TileHorizontally_MenuItem = null;
  protected JMenuItem RestoreAll_MenuItem = null;
  protected JMenuItem MaximizeAll_MenuItem = null;
  protected JMenuItem MinimizeAll_MenuItem = null;
  protected JMenuItem CloseAll_MenuItem = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public final static int CENTER = 0;
  public final static int SOUTH_EAST = 4;
  public final static int CASCADE = 100;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KDesktopPane() {

    super();

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KDesktopPane(JFrame pParentJFrame) {

    super();

    this.setContext(pParentJFrame);
    this.setOpened(true);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public JMenu getWindowMenu() { 
    return Window_Menu; }
  public JFrame getParentJFrame() { 
    return ParentJFrame; }
  
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
  /* ########## interface InternalFrameListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void internalFrameActivated(InternalFrameEvent e) {
    
    // System.out.println("internalFrameActivated #########");
    boolean dummy = ActivityStack.removeElement(
    (KInternalFrame)(e.getSource()));
    ActivityStack.push( (KInternalFrame)( e.getSource() ) );
    // System.out.println("ActivityStack: " + ActivityStack);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void internalFrameClosed(InternalFrameEvent e) {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void internalFrameClosing(InternalFrameEvent e) { 
    
    this.close(e); 
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void internalFrameDeactivated(InternalFrameEvent e) {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void internalFrameDeiconified(InternalFrameEvent e) {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void internalFrameIconified(InternalFrameEvent e) {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void internalFrameOpened(InternalFrameEvent e) {}
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface ActionListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void actionPerformed(ActionEvent e) {
  
    String actionCommand = e.getActionCommand();
    Object actionSource = e.getSource();
    
    if (actionSource == MinimizeAll_MenuItem)
      this.minimizeAll(); 
    else if (actionSource == MaximizeAll_MenuItem)
      this.maximizeAll();  
    else if (actionSource == Cascade_MenuItem)
      this.cascade();  
    else if (actionSource == CloseAll_MenuItem)
      this.closeAll();  
    else if (actionSource == RestoreAll_MenuItem)
      this.restoreAll();  
      
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setContext(JFrame pParentJFrame) {

    ParentJFrame = pParentJFrame;
    
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
      }
      catch (PropertyVetoException e) {
        System.out.println("Warning: KDesktopPane cannot be closed!");
      }
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void initialize() {
  
    //Make dragging faster:
    super.putClientProperty("JDesktopPane.dragMode", "outline");
    InternalFrames = new HashMap();
    ActivityStack = new Stack();
    this.createWindowMenu();

  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {}
  
  /* ########## ########## ########## ########## ########## ######### */  
  
  public void setVisible(boolean pVisible) {
  
    if (pVisible == true) {
      super.setVisible(true);
    }
    else {
      super.setVisible(false);
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public Component add(Component pInternalFrame, Integer pIndex) {

    // for proper rendering of JOptionPane.showInternal?Dialog
    super.add(pInternalFrame, pIndex);
    System.err.println("[KDesktopPane] JOptionPane.showInternal?Dialog");
    
    return pInternalFrame;

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void add(KInternalFrame pInternalFrame) {

    this.add(pInternalFrame, CASCADE);

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void addCascade(KInternalFrame pInternalFrame) {
    
    this.add(pInternalFrame, CASCADE);

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void addCenter(KInternalFrame pInternalFrame) {
    
    this.add(pInternalFrame, CENTER);

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void addSouthEast(KInternalFrame pInternalFrame) {
    
    this.add(pInternalFrame, SOUTH_EAST);

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void add(KInternalFrame pInternalFrame, int pLayout) {

    InternalFrames.put( new Integer(NextInternalFrameID), pInternalFrame );

    Dimension mySize = this.getSize();
    Dimension frameSize = pInternalFrame.getSize();
    Insets frameInsets = pInternalFrame.getInsets();
    int top = 0, bottom = 0, left = 0, right = 0;
    if (frameSize.width > mySize.width) {
      pInternalFrame.setPreferredSizeX(mySize.width - frameInsets.left 
      - frameInsets.right);
      pInternalFrame.setPreferredSize(new Dimension(pInternalFrame
      .getPreferredSizeX(), frameSize.height));
      pInternalFrame.setSize(pInternalFrame
      .getPreferredSizeX(), frameSize.height);
      left = frameInsets.left;
      right = frameInsets.right;
    }
    frameSize = pInternalFrame.getSize();
    if (frameSize.height > mySize.height) {
      pInternalFrame.setPreferredSizeY(mySize.height - frameInsets.top 
      - frameInsets.bottom);
      pInternalFrame.setPreferredSize(new Dimension(frameSize
      .width, pInternalFrame.getPreferredSizeX()));
      pInternalFrame.setSize(frameSize.width, pInternalFrame
      .getPreferredSizeY());
      top = frameInsets.top;
      bottom = frameInsets.bottom;
    }
    frameSize = pInternalFrame.getSize();
    
    switch (pLayout) {
      case CENTER: {
        pInternalFrame.setLocation(
        (mySize.width - frameSize.width - left - right) / 2,
        (mySize.height - frameSize.height - top - bottom) / 2);
        break;
      }
      case SOUTH_EAST: {
        pInternalFrame.setLocation(
        (mySize.width - frameSize.width - left - right),
        (mySize.height - frameSize.height - top - bottom));
        break;
      }
      case CASCADE: {
        if (frameSize.width > ( mySize.width - InternalFrameXOffset *
        (this.countInternalFrames() - 1))) {
          pInternalFrame.setSize(mySize.width - InternalFrameXOffset *
          (this.countInternalFrames() - 1), frameSize.height);
        }
        frameSize = pInternalFrame.getSize();
        if (frameSize.height > ( mySize.height  - InternalFrameYOffset *
        (this.countInternalFrames() - 1))) {
          pInternalFrame.setSize(frameSize.width, mySize.height -
          InternalFrameYOffset * (this.countInternalFrames() - 1) );
        }
        frameSize = pInternalFrame.getSize();
        pInternalFrame.setLocation(
        InternalFrameXOffset * (this.countInternalFrames() - 1),
        InternalFrameYOffset * (this.countInternalFrames() - 1) );
        break;
      }
      default: {
        pInternalFrame.setLocation(
        (mySize.width - frameSize.width) / 2,
        (mySize.height - frameSize.height) / 2);
        break;
      }      
    }

    super.add(pInternalFrame);
    try {
      pInternalFrame.setSelected(true);
    } catch (java.beans.PropertyVetoException e) {}
    pInternalFrame.setVisible(true);
    pInternalFrame.addInternalFrameListener(this);

    // System.out.println("ActivityStack@add-(): " + ActivityStack.size());
    // System.out.println("internalFrameAdded #########");
    boolean dummy = ActivityStack.removeElement(pInternalFrame);
    ActivityStack.push(pInternalFrame);
    // System.out.println("ActivityStack@add+(): " + ActivityStack.size());

    NextInternalFrameID++;

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void close(InternalFrameEvent e) {

    Iterator internalFramesIterator = InternalFrames.keySet().iterator();
    Integer currentInternalFrameID = null;
    while ( internalFramesIterator.hasNext() ) {
      currentInternalFrameID = (Integer)internalFramesIterator.next();
      if ( (JInternalFrame)InternalFrames.get(
        currentInternalFrameID) == e.getSource() ) {
        InternalFrames.remove(currentInternalFrameID);
        boolean dummy = ActivityStack.removeElement( 
          (KInternalFrame)( e.getSource() ) );
        break;
      }
    }

    // select previously active frame instead of selecting random frame
    // System.out.println("ActivityStack@close(): " + ActivityStack.size());
    if ( ! ActivityStack.empty() ) {
      KInternalFrame lastActiveFrame = (KInternalFrame)ActivityStack.peek();
      try {
        lastActiveFrame.moveToFront();
        lastActiveFrame.setIcon(false);
        lastActiveFrame.setMaximum(false);
        lastActiveFrame.setSelected(true);
        lastActiveFrame.requestFocus();
        // System.out.println("Now active Frame: " + lastActiveFrame.getTitle());
      }
      catch (java.beans.PropertyVetoException e2) {}
    }
     
  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public int countInternalFrames() {

    if (InternalFrames == null)
      return 0;
    else
      return InternalFrames.size();

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void minimizeAll() {

    JInternalFrame[] allFrames = super.getAllFrames();
    int frameCounter = allFrames.length;

    for (int i = 0; i < frameCounter; i++)
      try {
        allFrames[i].setIcon(true); 
      } catch (java.beans.PropertyVetoException e) {}

  } 

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void closeAll() {

    JInternalFrame[] allFrames = super.getAllFrames();
    int frameCounter = allFrames.length;

    for (int i = 0; i < frameCounter; i++)
      if (allFrames[i] instanceof KInternalFrame)
        ( (KInternalFrame)allFrames[i] ).setClosed(true); 
      else
        try {
          allFrames[i].setClosed(true); 
        } catch (java.beans.PropertyVetoException e) {}

  } 

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void cascade() {

    JInternalFrame[] allFrames = super.getAllFrames();
    int frameCounter = allFrames.length;

    for (int i = 0; i < frameCounter; i++) {
      try {
        allFrames[i].setIcon(false); 
        allFrames[i].setMaximum(false); 
        allFrames[i].moveToFront(); 
      } catch (java.beans.PropertyVetoException e) {}

      Dimension mySize = this.getSize();
      Dimension frameSize = allFrames[i].getSize();
      if ( frameSize.width > ( mySize.width - InternalFrameXOffset * i ) )
        allFrames[i].setSize(mySize.width - InternalFrameXOffset * i,
          frameSize.height);
      frameSize = allFrames[i].getSize();
      if ( frameSize.height > ( mySize.height  - InternalFrameYOffset * i ) )
        allFrames[i].setSize(frameSize.width, mySize.height - 
          InternalFrameYOffset * i );
      frameSize = allFrames[i].getSize();
      allFrames[i].setLocation(InternalFrameXOffset * i, 
        InternalFrameYOffset * i);
      // allFrames[i].setSize( allFrames[i].getPreferredSize() ); 
      // allFrames[i].setLocation(
      //   InternalFrameXOffset * i, InternalFrameYOffset * i);
    }      
    if ( frameCounter > 0 )
      try {
        allFrames[frameCounter - 1].setSelected(true);
      } catch (java.beans.PropertyVetoException e) {}
 
  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void maximizeAll() {

    JInternalFrame[] allFrames = super.getAllFrames();
    int frameCounter = allFrames.length;

    for (int i = 0; i < frameCounter; i++)
      try {
        allFrames[i].setMaximum(true); 
      } catch (java.beans.PropertyVetoException e) {}

  }

  /* ########## ########## ########## ########## ########## ######### */  
  
  public void restoreAll() {

    JInternalFrame[] allFrames = super.getAllFrames();
    int frameCounter = allFrames.length;

    for (int i = 0; i < frameCounter; i++)
      try {
        allFrames[i].setMaximum(false); 
        allFrames[i].setIcon(false); 
      } catch (java.beans.PropertyVetoException e) {}

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createWindowMenu() {

    Window_Menu = new JMenu("Window");  
    Window_Menu.setMnemonic(KeyEvent.VK_W);
    Cascade_MenuItem = new JMenuItem("Cascade", 
      KeyEvent.VK_C);
    Cascade_MenuItem.setActionCommand("Cascade");
    Cascade_MenuItem.addActionListener(this);
    Cascade_MenuItem.setEnabled(true);
    Window_Menu.add(Cascade_MenuItem);
    TileVertically_MenuItem = new JMenuItem("Tile Vertically", 
      KeyEvent.VK_V);
    TileVertically_MenuItem.setActionCommand("TileVertically");
    TileVertically_MenuItem.addActionListener(this);
    TileVertically_MenuItem.setEnabled(false);
    Window_Menu.add(TileVertically_MenuItem);
    TileHorizontally_MenuItem = new JMenuItem("Tile Horizontally", 
      KeyEvent.VK_H);
    TileHorizontally_MenuItem.setActionCommand("TileHorizontally");
    TileHorizontally_MenuItem.addActionListener(this);
    TileHorizontally_MenuItem.setEnabled(false);
    Window_Menu.add(TileHorizontally_MenuItem);
    Window_Menu.addSeparator();
    RestoreAll_MenuItem = new JMenuItem("Restore All", 
      KeyEvent.VK_R);
    RestoreAll_MenuItem.setActionCommand("RestoreAll");
    RestoreAll_MenuItem.addActionListener(this);
    RestoreAll_MenuItem.setEnabled(true);
    Window_Menu.add(RestoreAll_MenuItem);
    MaximizeAll_MenuItem = new JMenuItem("Maximize All", 
      KeyEvent.VK_X);
    MaximizeAll_MenuItem.setActionCommand("MaximizeAll");
    MaximizeAll_MenuItem.addActionListener(this);
    MaximizeAll_MenuItem.setEnabled(true);
    Window_Menu.add(MaximizeAll_MenuItem);
    MinimizeAll_MenuItem = new JMenuItem("Minimize All", 
      KeyEvent.VK_N);
    MinimizeAll_MenuItem.setActionCommand("MinimizeAll");
    MinimizeAll_MenuItem.addActionListener(this);
    MinimizeAll_MenuItem.setEnabled(true);
    Window_Menu.add(MinimizeAll_MenuItem);
    CloseAll_MenuItem = new JMenuItem("Close All", 
      KeyEvent.VK_S);
    CloseAll_MenuItem.setActionCommand("CloseAll");
    CloseAll_MenuItem.addActionListener(this);
    CloseAll_MenuItem.setEnabled(true);
    Window_Menu.add(CloseAll_MenuItem);

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}