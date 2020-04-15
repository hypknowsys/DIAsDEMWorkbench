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
  
public class KTextArea extends JTextArea {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

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

  public KTextArea() {

    this("", true, true);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KTextArea(String pText) {

    this(pText, true, true);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KTextArea(String pText, boolean pEnabled) {

    this(pText, pEnabled, true);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KTextArea(String pText, boolean pEnabled, boolean pEditable) {

    super((pText == null ? "" : pText));
    this.setMargin(DEFAULT_MARGIN);
    this.setEnabled(pEnabled);
    this.setEditable(pEditable);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KTextArea(String pText, int pRows, int pColumns) {

    this(pText, pRows, pColumns, true, true);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KTextArea(String pText, int pRows, int pColumns, boolean pEnabled) {

    this(pText, pRows, pColumns, pEnabled, true);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public KTextArea(String pText, int pRows, int pColumns, boolean pEnabled,
  boolean pEditable) {

    super((pText == null ? "" : pText), pRows, pColumns);
    this.setMargin(DEFAULT_MARGIN);
    this.setEnabled(pEnabled);
    this.setEditable(pEditable);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KTextArea(String pText, Font pFont) {

    super((pText == null ? "" : pText));
    this.setMargin(DEFAULT_MARGIN);
    this.setFont(pFont);

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

  public void setTextAndEnabled(String pText, boolean pEnabled) {

    this.setEnabled(pEnabled);
    super.setText((pText == null ? "" : pText));

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addMouseListener(MouseListener pMouseListener) {

   super.addMouseListener(pMouseListener);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCaretAtBeginning() {

    this.requestFocus();
    this.setCaretPosition(0);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCaretAtEnding() {

    this.requestFocus();
    this.setCaretPosition( this.getText().length() );
    
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