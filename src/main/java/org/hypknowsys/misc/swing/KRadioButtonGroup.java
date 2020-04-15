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

//  buttonGroupType = new ButtonGroup();
//  radioButtonCommon = new JRadioButton("Common Format", true);
//  radioButtonCommon.setActionCommand("Common");
//  formatPanel.add(radioButtonCommon);
//  radioButtonExtended = new JRadioButton("Extended Format", false);
//  radioButtonExtended.setActionCommand("Extended");
//  formatPanel.add(radioButtonExtended);
//  radioButtonCookie = new JRadioButton("Cookie Format", false);
//  radioButtonCookie.setActionCommand("Cookie");
//  formatPanel.add(radioButtonCookie);
//  radioButtonIIS = new JRadioButton("MS-IIS Format", false);
//  radioButtonIIS.setActionCommand("IIS");
//  formatPanel.add(radioButtonIIS);

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KRadioButtonGroup extends KGridBagPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private JRadioButton[] Buttons = null;
  private ButtonGroup oButtonGroup = null;

  private int NumberOfButtons = 0;
  private int NumberOfColumns = 0;
  private int NextButtonIndex = 0;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static Insets DEFAULT_INSETS = new Insets(0, 0, 0, 0);

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KRadioButtonGroup(int pNumberOfButtons, int pNumberOfColumns) {

    super();

    this.createButtonPanel(pNumberOfButtons, pNumberOfColumns);
    
  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KRadioButtonGroup(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder,
    int pNumberOfButtons, int pNumberOfColumns) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder, 
      pOutsideRightBorder);

    this.createButtonPanel(pNumberOfButtons, pNumberOfColumns);
    
  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KRadioButtonGroup(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder, String pBorderTitle,
    int pInsideTopBorder, int pInsideLeftBorder,
    int pInsideBottomBorder, int pInsideRightBorder,
    int pNumberOfButtons, int pNumberOfColumns) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder, 
      pOutsideRightBorder, pBorderTitle, pInsideTopBorder, 
      pInsideLeftBorder, pInsideBottomBorder, pInsideRightBorder);  

    this.createButtonPanel(pNumberOfButtons, pNumberOfColumns);

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

  public void addFirstButton(String pButtonText, int pButtonMnemonic, 
    boolean pEnabledButton, boolean pSelectedButton,
    String pActionCommand, ActionListener pActionListener) {

    if (NextButtonIndex < NumberOfButtons) {
      Buttons[NextButtonIndex] = new JRadioButton(pButtonText);
      if (pButtonMnemonic != 0)
        Buttons[NextButtonIndex].setMnemonic(pButtonMnemonic);
      Buttons[NextButtonIndex].setEnabled(pEnabledButton);
      Buttons[NextButtonIndex].setSelected(pSelectedButton);
      Buttons[NextButtonIndex].setActionCommand(pActionCommand);
      Buttons[NextButtonIndex].addActionListener(pActionListener);
      NextButtonIndex++;
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addNextButton(String pButtonText, int pButtonMnemonic, 
    boolean pEnabledButton, boolean pSelectedButton, 
    String pActionCommand, ActionListener pActionListener) {

    if (NextButtonIndex < NumberOfButtons) {
      Buttons[NextButtonIndex] = new JRadioButton(pButtonText);
      if (pButtonMnemonic != 0)
        Buttons[NextButtonIndex].setMnemonic(pButtonMnemonic);
      Buttons[NextButtonIndex].setEnabled(pEnabledButton);
      Buttons[NextButtonIndex].setSelected(pSelectedButton);
      Buttons[NextButtonIndex].setActionCommand(pActionCommand);
      Buttons[NextButtonIndex].addActionListener(pActionListener);
      NextButtonIndex++;
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addLastButton(String pButtonText, int pButtonMnemonic, 
    boolean pEnabledButton, boolean pSelectedButton, 
    String pActionCommand, ActionListener pActionListener) {

    if (NextButtonIndex < NumberOfButtons) {
      Buttons[NextButtonIndex] = new JRadioButton(pButtonText);
      if (pButtonMnemonic != 0)
        Buttons[NextButtonIndex].setMnemonic(pButtonMnemonic);
      Buttons[NextButtonIndex].setEnabled(pEnabledButton);
      Buttons[NextButtonIndex].setSelected(pSelectedButton);
      Buttons[NextButtonIndex].setActionCommand(pActionCommand);
      Buttons[NextButtonIndex].addActionListener(pActionListener);
      NextButtonIndex++;
      this.init();
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setEnabled(int pButtonIndex, boolean pEnabledButton) {

    if ( (pButtonIndex >= 0) && (pButtonIndex < NumberOfButtons) ) {
      if (Buttons[pButtonIndex] != null) {
        Buttons[pButtonIndex].setEnabled(pEnabledButton);
      }
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setSelected(int pButtonIndex, boolean pSelectedButton) {

    if ( (pButtonIndex >= 0) && (pButtonIndex < NumberOfButtons) ) {
      if (Buttons[pButtonIndex] != null) {
        Buttons[pButtonIndex].setSelected(pSelectedButton);
      }
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public int getSelectedIndex() {

    int result = -1;
    if (oButtonGroup == null || Buttons == null) {
      return result;
    }
    
    for (int i = 0; i < Buttons.length; i++) {
      if (oButtonGroup.getSelection().equals(Buttons[i].getModel())) {
        result = i;
        break;
      }
    }
    
    return result;

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getSelectedActionCommand() {

    if ( oButtonGroup != null)
      return oButtonGroup.getSelection().getActionCommand();
    else
      return "";

  }  // setSelected()

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void init() {

    int vCurrentColumn = 0;
    int vCurrentRow = 0;
    for (int i = 0; i < NumberOfButtons; i++)
      if (Buttons[i] != null) {
        oButtonGroup.add( Buttons[i] );
        this.addComponent( Buttons[i], vCurrentColumn, vCurrentRow );
        vCurrentColumn++;
        if (vCurrentColumn == NumberOfColumns) {
          vCurrentColumn = 0;
          vCurrentRow++;
        }
      } 
    this.revalidate();

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private void createButtonPanel(int pNumberOfButtons, int pNumberOfColumns) {

    NumberOfButtons = pNumberOfButtons;
    NumberOfColumns = pNumberOfColumns;
    NextButtonIndex = 0;
    Buttons = new JRadioButton[NumberOfButtons];
    for (int i = 0; i < NumberOfButtons; i++) Buttons[i] = null;      
    oButtonGroup = new ButtonGroup();

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}