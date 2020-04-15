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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KScrollBorderPanel extends KBorderPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private JScrollPane MyScrollPanenel = null;
  private KBorderPanel MyScrollBorderPanel = null;

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

  public KScrollBorderPanel() {

    this( 0, 0, 0, 0);   

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollBorderPanel(int pInsideTopBorder, int pInsideLeftBorder,
    int pInsideBottomBorder, int pInsideRightBorder) {

    this( 0, 0, 0, 0, pInsideTopBorder, pInsideLeftBorder, pInsideBottomBorder, 
    pInsideRightBorder);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollBorderPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder, 
    int pInsideTopBorder, int pInsideLeftBorder,
    int pInsideBottomBorder, int pInsideRightBorder) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder,
    pOutsideRightBorder);
    
    MyScrollBorderPanel = new KBorderPanel(pInsideTopBorder, pInsideLeftBorder, 
    pInsideBottomBorder, pInsideRightBorder);
    this.initScrollPane();

  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public Component getInitialFocusComponent() { 
    
    if (MyScrollBorderPanel != null)
      return MyScrollBorderPanel.getInitialFocusComponent();
    else
      return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public boolean getForwardFocus() { 
    
    if (MyScrollBorderPanel != null)
      return MyScrollBorderPanel.getForwardFocus();
    else
      return false;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setInitialFocusComponent(Component pInitialFocusComponent) { 
    
    if (MyScrollBorderPanel != null)
      MyScrollBorderPanel.setInitialFocusComponent(pInitialFocusComponent);
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setForwardFocus(boolean pForwardFocus) {
    
    if (MyScrollBorderPanel != null)
      MyScrollBorderPanel.setForwardFocus(pForwardFocus);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void startFocusForwarding(Component pInitialFocusComponent) {
    
    this.setInitialFocusComponent(pInitialFocusComponent);
    this.setForwardFocus(true);
  }
  

  /* ########## ########## ########## ########## ########## ######### */
  
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
  /* ########## interface FocusListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void focusGained(FocusEvent e) {
    
    if (this.getForwardFocus() && MyScrollBorderPanel != null) {
      MyScrollBorderPanel.focusGained(e);
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void requestFocus() {

    if (MyScrollBorderPanel != null)
      MyScrollBorderPanel.requestFocus();

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addMouseListener(MouseListener pMouseListener) {

    if (MyScrollBorderPanel != null)
      MyScrollBorderPanel.addMouseListener(pMouseListener);
    if (MyScrollPanenel != null)
      MyScrollPanenel.addMouseListener(pMouseListener);

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void enforceUpdate() {

    if (MyScrollBorderPanel != null)
      MyScrollBorderPanel.revalidate();
    MyScrollPanenel.revalidate();

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setGaps(int pHorizontalGap, int pVerticalGap) {

    if (MyScrollBorderPanel != null) {
      MyScrollBorderPanel.setGaps(pHorizontalGap, pVerticalGap);
    }
    this.revalidate();

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addNorth(Component pComponent) {

    MyScrollBorderPanel.addNorth(pComponent);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addEast(Component pComponent) {

    MyScrollBorderPanel.addEast(pComponent);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addSouth(Component pComponent) {

    MyScrollBorderPanel.addSouth(pComponent);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addWest(Component pComponent) {

    MyScrollBorderPanel.addWest(pComponent);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addCenter(Component pComponent) {

    MyScrollBorderPanel.addCenter(pComponent);

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void initScrollPane() {

    MyScrollPanenel = new JScrollPane(MyScrollBorderPanel);
    super.addCenter(MyScrollPanenel);

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}