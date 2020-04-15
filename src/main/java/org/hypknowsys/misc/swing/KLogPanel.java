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

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KLogPanel extends KBorderPanel implements ActionListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private JTextPane LogTextArea = null;
  private Document LogTextDocument = null;
  private SimpleAttributeSet LogMessageStyle = null;
  private SimpleAttributeSet InfoMessageStyle = null;
  private SimpleAttributeSet WarningMessageStyle = null;
  private JScrollPane MyScrollPane = null;
  private JPopupMenu MyPopupMenu = null; 
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient int TmpInt = 0;  

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private static final int MAX_TEXT_LENGTH = 30000;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KLogPanel() {

    super();
    
    this.createLogPanel();
    
  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KLogPanel(int pOutsideTopBorder, int pOutsideLeftBorder, 
  int pOutsideBottomBorder, int pOutsideRightBorder) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder, 
      pOutsideRightBorder);

    this.createLogPanel();
    
  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KLogPanel(int pOutsideTopBorder, int pOutsideLeftBorder, 
  int pOutsideBottomBorder, int pOutsideRightBorder, 
  String pBorderTitle, int pInsideTopBorder, int pInsideLeftBorder, 
  int pInsideBottomBorder, int pInsideRightBorder) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder, 
      pOutsideRightBorder, pBorderTitle, pInsideTopBorder, 
      pInsideLeftBorder, pInsideBottomBorder, pInsideRightBorder);  

    this.createLogPanel();
    
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
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String logMessage(String pLogMessage) {
    
    return this.logMessage(pLogMessage, LogMessageStyle);
       
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String logInfoMessage(String pLogMessage) {
   
    return this.logMessage(pLogMessage, InfoMessageStyle);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String logWarningMessage(String pLogMessage) {
   
    return this.logMessage(pLogMessage, WarningMessageStyle);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String getLogMessage(String pLogMessage) {
   
    return (new SimpleDateFormat("HH:mm:ss")).format(new Date())
    + " " + pLogMessage;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createLogPanel() {
    
    super.setPreferredSize( new Dimension(1024, 100) );
    LogTextArea = new JTextPane();
    LogTextDocument = LogTextArea.getStyledDocument();
    LogTextArea.setEditable(false);
    MyScrollPane = new JScrollPane(LogTextArea);
    super.addCenter(MyScrollPane);
    
    LogMessageStyle = new SimpleAttributeSet();
    InfoMessageStyle = new SimpleAttributeSet();
    WarningMessageStyle = new SimpleAttributeSet();
    StyleConstants.setForeground(WarningMessageStyle, Color.RED);
 
    this.createContextMenu();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private String logMessage(String pLogMessage, SimpleAttributeSet pStyle) {
   
    TmpString = this.getTimestamp() + ' ' + pLogMessage;
    if (LogTextDocument != null) {
      if (LogTextDocument.getLength() < MAX_TEXT_LENGTH) {
        if (LogTextDocument.getLength() > 0) {
          TmpString = "\n" + TmpString;
        }
        TmpInt = LogTextDocument.getLength();
      }
      else {
        try {
          LogTextDocument.remove(0, LogTextDocument.getLength());
          TmpInt = 0;       
        }
        catch (BadLocationException e) {
          TmpInt = LogTextDocument.getLength();
        }
      }
      try {
        LogTextDocument.insertString(TmpInt, TmpString, pStyle);
      }
      catch (BadLocationException e) {}
      LogTextArea.setCaretPosition(LogTextDocument.getLength());
    }
    
    return TmpString;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private String getTimestamp() {

    return (new SimpleDateFormat("HH:mm:ss")).format(new Date());
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createContextMenu() {
    
    MyPopupMenu = new JPopupMenu();
    JMenuItem menuItem1 = new JMenuItem("Clear Output", KeyEvent.VK_O);
    menuItem1.setActionCommand("ClearOutput");
    menuItem1.addActionListener(this);
    MyPopupMenu.add(menuItem1);
    MyPopupMenu.addSeparator();
    JMenuItem menuItem2 = new JMenuItem("Available Memory", KeyEvent.VK_M);
    menuItem2.setActionCommand("AvailableMemory");
    menuItem2.addActionListener(this);
    MyPopupMenu.add(menuItem2);
    JMenuItem menuItem3 = new JMenuItem("Run Garbage Collection", KeyEvent.VK_G);
    menuItem3.setActionCommand("GarbageCollection");
    menuItem3.addActionListener(this);
    MyPopupMenu.add(menuItem3);

    MouseListener popupListener = new PopupListener();
    LogTextArea.addMouseListener(popupListener);   
    LogTextArea.getParent().addMouseListener(popupListener);
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  public void actionPerformed(ActionEvent e) {
       
    String command = e.getActionCommand();
    Object source = e.getSource();

    if ( command.equals("ClearOutput") ) {
      LogTextArea.setText("");
    }
    if ( command.equals("AvailableMemory") ) {
      Runtime myRuntime = Runtime.getRuntime();
      long currentlyMaxMemory = myRuntime.totalMemory() / 1024 / 1024;
      long currentlyFreeMemory = myRuntime.freeMemory() / 1024 / 1024;
      long potentiallyMaxMemory = myRuntime.maxMemory() / 1024 / 1024;
      long potentiallyFreeMemory = (potentiallyMaxMemory - currentlyMaxMemory)
      + currentlyFreeMemory;
      this.logInfoMessage("Currently maximum memory: " + currentlyMaxMemory
      + " MB");
      this.logInfoMessage("Currently free memory: " + currentlyFreeMemory
      + " MB");
      this.logInfoMessage("Potentially maximum memory: " + potentiallyMaxMemory
      + " MB");
      this.logInfoMessage("Potentially free memory: " + potentiallyFreeMemory
      + " MB");
    }
    if ( command.equals("GarbageCollection") ) {
      this.logInfoMessage("Starting garbage colletion");
      System.gc();
      this.logInfoMessage("Garbage collection finished");
      Runtime myRuntime = Runtime.getRuntime();
      long currentlyFreeMemory = myRuntime.freeMemory() / 1024 / 1024;
      this.logInfoMessage("Currently free memory: " + currentlyFreeMemory
      + " MB");
    }

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
   private class PopupListener extends MouseAdapter {
  
     public void mousePressed(MouseEvent e) {
       maybeShowPopup(e);
     }

     public void mouseReleased(MouseEvent e) {
       maybeShowPopup(e);
     }

     private void maybeShowPopup(MouseEvent e) {
       if (e.isPopupTrigger()) { 
         MyPopupMenu.show(e.getComponent(), e.getX(), e.getY());
       }       
     }
     
   } 
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}