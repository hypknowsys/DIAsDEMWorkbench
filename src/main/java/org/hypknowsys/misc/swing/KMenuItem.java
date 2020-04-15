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
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KMenuItem extends JMenuItem {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private int CurrentStatus = 0;
  private int MinStatusEnabled = Integer.MIN_VALUE;
  private int MaxStatusEnabled = Integer.MAX_VALUE;
  private String TaskClassName = null;
  
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

  public KMenuItem(String pText, int pMnemonic, 
  String pActionCommand, ActionListener pActionListener,
  int pAcceleratorKeyEvent, int pAcceleratorActionEvent,
  int pMinStatusEnabled, int pMaxStatusEnabled, int pCurrentStatus,
  String pTaskClassName) {

    super(pText, pMnemonic);
    
    this.setMnemonic(pMnemonic);
    this.setActionCommand(pActionCommand);
    if (pActionListener != null) {
      this.addActionListener(pActionListener);
    }
    if (pAcceleratorKeyEvent != 0 && pAcceleratorActionEvent != 0) {
      this.setAccelerator(KeyStroke.getKeyStroke(pAcceleratorKeyEvent, 
      pAcceleratorActionEvent));
    }
    MinStatusEnabled = pMinStatusEnabled;
    MaxStatusEnabled = pMaxStatusEnabled;
    CurrentStatus = pCurrentStatus;
    this.setEnabled(CurrentStatus);
    TaskClassName = pTaskClassName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public KMenuItem(String pText, int pMnemonic, boolean pEnabled, 
  String pActionCommand, ActionListener pActionListener,
  int pAcceleratorKeyEvent, int pAcceleratorActionEvent,
  String pTaskClassName) {

    super(pText, pMnemonic);
    
    this.setMnemonic(pMnemonic);
    this.setEnabled(pEnabled);
    this.setActionCommand(pActionCommand);
    if (pActionListener != null) {
      this.addActionListener(pActionListener);
    }
    if (pAcceleratorKeyEvent != 0 && pAcceleratorActionEvent != 0) {
      this.setAccelerator(KeyStroke.getKeyStroke(pAcceleratorKeyEvent, 
      pAcceleratorActionEvent));
    }
    MinStatusEnabled = Integer.MIN_VALUE;
    MaxStatusEnabled = Integer.MAX_VALUE;
    CurrentStatus = 0;
    TaskClassName = pTaskClassName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public KMenuItem(String pText, int pMnemonic, boolean pEnabled, 
  String pActionCommand, ActionListener pActionListener) {

    super(pText, pMnemonic);
    
    this.setMnemonic(pMnemonic);
    this.setEnabled(pEnabled);
    this.setActionCommand(pActionCommand);
    if (pActionListener != null) {
      this.addActionListener(pActionListener);
    }
    MinStatusEnabled = Integer.MIN_VALUE;
    MaxStatusEnabled = Integer.MAX_VALUE;
    CurrentStatus = 0;
    TaskClassName = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getTaskClassName() {    
    return TaskClassName; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    return this.getLabel();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setEnabled(int pCurrentStatus) {

    if (pCurrentStatus >= MinStatusEnabled 
    && pCurrentStatus <= MaxStatusEnabled)
      this.setEnabled(true);
    else
      this.setEnabled(false);
    this.validate();
    
  }

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