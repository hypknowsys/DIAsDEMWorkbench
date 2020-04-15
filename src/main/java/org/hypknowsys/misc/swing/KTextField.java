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
  
public class KTextField extends JTextField implements FocusListener,
MouseListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private boolean MouseEntered = false;
  private boolean MouseClicked = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private static final Insets DEFAULT_MARGIN = new Insets(3, 3, 3, 3);

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KTextField() {

    this("", true, true);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KTextField(String pText) {

    this(pText, true, true);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KTextField(String pText, boolean pEnabled) {

    this(pText, pEnabled, true);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KTextField(String pText, boolean pEnabled, boolean pEditable) {

    super((pText == null ? "" : pText));
    this.setMargin(DEFAULT_MARGIN);
    this.setEnabled(pEnabled);
    this.addFocusListener(this);
    this.addMouseListener(this);
    this.setEditable(pEditable);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KTextField(int pColumns) {

    this("", pColumns, true, true);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KTextField(String pText, int pColumns) {

    
    this(pText, pColumns, true, true);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KTextField(String pText, int pColumns, boolean pEnabled) {

    this(pText, pColumns, pEnabled, true);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KTextField(String pText, int pColumns, boolean pEnabled,
  boolean pEditable) {

    super((pText == null ? "" : pText), pColumns);
    this.setMargin(DEFAULT_MARGIN);
    this.setEnabled(pEnabled);
    this.addFocusListener(this);
    this.addMouseListener(this);
    this.setEditable(pEditable);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KTextField(String pText, Font pFont) {

    super((pText == null ? "" : pText));
    this.setMargin(DEFAULT_MARGIN);
    this.setFont(pFont);
    this.addFocusListener(this);
    this.addMouseListener(this);

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
  /* ########## interface FocusListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void focusGained(FocusEvent e) {
    
    if (!MouseEntered && !MouseClicked) {
      super.setCaretPosition( super.getText().length() );
      super.selectAll();
    }
    MouseClicked = false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void focusLost(FocusEvent e) {
    
    if (!MouseEntered && !MouseClicked)
      super.setCaretPosition( super.getText().length() );
    MouseClicked = false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface MouseListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void mouseClicked(MouseEvent e) {
    
    MouseClicked = true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void mouseEntered(MouseEvent e) {
    
    MouseEntered = true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void mouseExited(MouseEvent e) { 
    
    MouseEntered = false;    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void mousePressed(MouseEvent e) {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void mouseReleased(MouseEvent e) {}
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setTextAndEnabled(String pText, boolean pEnabled) {

    this.setEnabled(pEnabled);
    super.setText((pText == null ? "" : pText));

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void requestFocus() {

    super.requestFocus();

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setCaretAtBeginning() {

    super.requestFocus();
    super.setCaretPosition(0);

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setCaretAtEnding() {

    super.requestFocus();
    super.setCaretPosition( super.getText().length() );

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