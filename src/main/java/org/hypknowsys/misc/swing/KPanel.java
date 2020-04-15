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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KPanel extends JPanel implements ActionListener, FocusListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected JFrame Parent_Frame = null;
  protected KProgressMonitor GuiProgressMonitor = null;
  protected KInternalProgressMonitor GuiInternalProgressMonitor = null;
  protected JFileChooser GuiFileChooser = null;
  protected javax.swing.Timer GuiTimer = null;
  
  protected Component InitialFocusComponent = null;
  protected Component MostRecentFocusComponent = null;
  protected boolean InitialFocusSet = false;
  protected boolean ForwardFocus = false;

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

  public KPanel() {

    super();
    this.setBorder( BorderFactory.createEmptyBorder(0, 0, 0, 0) );
    this.addFocusListener(this);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder) {

    super();
    this.setBorder( BorderFactory.createEmptyBorder(pOutsideTopBorder, 
      pOutsideLeftBorder, pOutsideBottomBorder, pOutsideRightBorder) );
    this.addFocusListener(this);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder, String pBorderTitle,
    int pInsideTopBorder, int pInsideLeftBorder,
    int pInsideBottomBorder, int pInsideRightBorder) {

    super();
    this.setBorder( BorderFactory.createCompoundBorder(
      BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
      pBorderTitle), 
      BorderFactory.createEmptyBorder(pInsideTopBorder, 
      pInsideLeftBorder, pInsideBottomBorder, pInsideRightBorder) ) );  
    this.setBorder( BorderFactory.createCompoundBorder(
      BorderFactory.createEmptyBorder(pOutsideTopBorder, 
      pOutsideLeftBorder, pOutsideBottomBorder, pOutsideRightBorder),  
      this.getBorder() ) );
    this.addFocusListener(this);

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
  public boolean getForwardFocus() { 
    return ForwardFocus; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setPreferredSizeX(int pPreferredSizeX) {
    PreferredSizeX = pPreferredSizeX; }
  public void setPreferredSizeY(int pPreferredSizeY) {
    PreferredSizeY = pPreferredSizeY; }
  public void setInitialFocusComponent(Component pInitialFocusComponent) { 
    InitialFocusComponent = pInitialFocusComponent; }
  public void setForwardFocus(boolean pForwardFocus) {
    ForwardFocus = pForwardFocus; }
  public void startFocusForwarding(Component pInitialFocusComponent) {
    this.setInitialFocusComponent(pInitialFocusComponent);
    this.setForwardFocus(true);
  }
  public void stopFocusForwarding() {
    this.setForwardFocus(false);
  }

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
    }
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface FocusListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void focusGained(FocusEvent e) {
    
    if (ForwardFocus) {
      //System.out.println("KPanel:focusGained(), Component:" + e.getComponent());
      if (InitialFocusSet == false && InitialFocusComponent != null) {
        MostRecentFocusComponent = InitialFocusComponent;
        InitialFocusSet = true;
        MostRecentFocusComponent.requestFocus();
      }
      else if (MostRecentFocusComponent != null) {
        MostRecentFocusComponent.requestFocus();
      }
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void focusLost(FocusEvent e) {
  
    if (ForwardFocus) {
      //System.out.println("KPanel:focusLost(), Component:" + e.getComponent());
    }  
  
  }
  
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