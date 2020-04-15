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
  
public class KComboBox extends JComboBox {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected int NumberOfItems = 0;
  protected int NextItemIndex = 0;  

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

  public KComboBox(int pNumberOfItems, boolean pEnabled,
    String pActionCommand, ActionListener pActionListener, boolean pEditable,
    String pToolTipText) {

    super();
   
    NumberOfItems = pNumberOfItems;
    super.setEnabled(pEnabled);
    super.setActionCommand(pActionCommand);
    super.addActionListener(pActionListener);
    super.setEditable(pEditable);
    if (pToolTipText != null) {
      super.setToolTipText(pToolTipText);
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KComboBox(int pNumberOfItems, boolean pEnabled,
    String pActionCommand, ActionListener pActionListener, boolean pEditable) {

    this(pNumberOfItems, pEnabled, pActionCommand, pActionListener, 
    pEditable, null);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KComboBox(int pNumberOfItems, boolean pEnabled,
    String pActionCommand, ActionListener pActionListener) {

    this(pNumberOfItems, pEnabled, pActionCommand, pActionListener, 
    false, null);

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

  public void addItem(String pItemText, boolean pSelectedItem) {

    if (NextItemIndex < NumberOfItems) {
      super.addItem(pItemText);
      if (pSelectedItem)
        super.setSelectedIndex(NextItemIndex);
      NextItemIndex++;
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public String getSelectedString() {

    Object oResult = super.getSelectedItem();
    if (oResult != null)
      return (String)oResult;
    else
      return "";

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void setSelectedString(String pSelectedString) {

    super.setSelectedItem(pSelectedString);

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