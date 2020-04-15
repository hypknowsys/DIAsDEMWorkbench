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
  
public class KScrollTextArea extends KBorderPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private JScrollPane MyScrollPane = null;
  private KTextArea MyTextArea = null;

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

  public KScrollTextArea() {

    super();
    
    MyTextArea = new KTextArea();
    this.initScrollPane();

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText) {

    this(pText, true, true);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText, boolean pEnabled) {

    this(pText, pEnabled, true);
    
  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText, boolean pEnabled, boolean pEditable) {

    super();
    
    MyTextArea = new KTextArea(pText, pEnabled, pEditable);
    this.initScrollPane();
    this.setTextAreaEnabled(pEnabled);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText, int pRows, int pColumns) {

    this(pText, pRows, pColumns, true, true);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText, int pRows, int pColumns, 
  boolean pEnabled) {

    this(pText, pRows, pColumns, pEnabled, true);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText, int pRows, int pColumns, 
  boolean pEnabled, boolean pEditable) {

    super();
    
    MyTextArea = new KTextArea(pText, pRows, pColumns, pEnabled, pEditable);
    this.initScrollPane();
    this.setTextAreaEnabled(pEnabled);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText, int pRows, int pColumns,
  Dimension pMinimumSize) {

    super();
    
    MyTextArea = new KTextArea(pText, pRows, pColumns);
    this.initScrollPane();
    this.setMinimumSize(pMinimumSize);    

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText, int pRows, int pColumns,
  Dimension pMinimumSize, boolean pEnabled) {

    super();
    
    MyTextArea = new KTextArea(pText, pRows, pColumns);
    this.initScrollPane();
    this.setMinimumSize(pMinimumSize);    
    this.setTextAreaEnabled(pEnabled);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTextArea(String pText, Font pFont) {

    super();
    
    MyTextArea = new KTextArea(pText, pFont);
    this.initScrollPane();

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

  public void requestFocus() {

    if (MyTextArea != null)
      MyTextArea.requestFocus();

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setText(String pText) {

    if (MyTextArea != null)
      MyTextArea.setText(pText);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void appendText(String pText) {

    if (MyTextArea != null)
      MyTextArea.append(pText);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getText() {

    if (MyTextArea != null)
      return MyTextArea.getText();
    else
      return "";

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getSelectedText() {

    if (MyTextArea != null)
      return MyTextArea.getSelectedText();
    else
      return null;

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTextAreaEnabled(boolean pEnabled) {

    if (MyTextArea != null) {
      MyScrollPane.setEnabled(pEnabled);
      MyTextArea.setEnabled(pEnabled);
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTextAreaEditable(boolean pEditable) {

    if (MyTextArea != null) {
      MyTextArea.setEditable(pEditable);
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTextAreaLineWrap(boolean pLineWrap) {

    if (MyTextArea != null)
      MyTextArea.setLineWrap(pLineWrap);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addMouseListener(MouseListener pMouseListener) {

    if (MyTextArea != null)
      MyTextArea.addMouseListener(pMouseListener);
    if (MyScrollPane != null)
      MyScrollPane.addMouseListener(pMouseListener);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTextAreaWrapStyleWord(boolean pWrapStyleWord) {

    if (MyTextArea != null)
      MyTextArea.setWrapStyleWord(pWrapStyleWord);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCaretAtBeginning() {

    if (MyTextArea != null) {
      this.requestFocus();
      MyTextArea.setCaretPosition(0);
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCaretAtEnding() {

    if (MyTextArea != null) {
      this.requestFocus();
      MyTextArea.setCaretPosition( this.getText().length() );
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFont(Font pFont) {

    if (MyTextArea != null)
      MyTextArea.setFont(pFont);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void enforceUpdate() {

    if (MyTextArea != null)
      MyTextArea.revalidate();
    MyScrollPane.revalidate();

  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void initScrollPane() {

    MyScrollPane = new JScrollPane(MyTextArea);
    super.addCenter(MyScrollPane);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}