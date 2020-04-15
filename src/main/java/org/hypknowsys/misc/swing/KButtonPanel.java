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
  
public class KButtonPanel extends KGridBagPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected JButton[] Buttons = null;
  protected KGridBagPanel ButtonGridBagPanel = null;

  protected int NumberOfButtons = 0;
  protected int Alignment = UNKNOWN;
  protected int HorizontalGap = UNKNOWN;
  protected int VerticalGap = UNKNOWN;
  protected int DefaultButtonIndex = UNKNOWN;
  protected int NextButtonIndex = 0;
  
  protected int TotalBorderWidth = 0;
  protected int TotalBorderHeight = 0;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public final static int UNKNOWN = -1;

  public final static int HORIZONTAL_LEFT = 1;
  public final static int HORIZONTAL_CENTER = 2;
  public final static int HORIZONTAL_RIGHT = 3;
  public final static int VERTICAL_TOP = 4;
  public final static int VERTICAL_MIDDLE = 5;
  public final static int VERTICAL_BOTTOM = 6;

  public final static int DEFAULT_HORIZONTAL_GAP = 5;
  public final static int DEFAULT_VERTICAL_GAP = 5;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KButtonPanel(int pNumberOfButtons, int pAlignment) {

    super();

    this.createButtonPanel(pNumberOfButtons, pAlignment);
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public KButtonPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder,
    int pNumberOfButtons, int pAlignment) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder, 
      pOutsideRightBorder);
    TotalBorderWidth = pOutsideLeftBorder + pOutsideRightBorder;
    TotalBorderHeight = pOutsideTopBorder + pOutsideBottomBorder;

    this.createButtonPanel(pNumberOfButtons, pAlignment);
    
  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KButtonPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder, String pBorderTitle,
    int pInsideTopBorder, int pInsideLeftBorder,
    int pInsideBottomBorder, int pInsideRightBorder,
    int pNumberOfButtons, int pAlignment) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder, 
      pOutsideRightBorder, pBorderTitle, pInsideTopBorder, 
      pInsideLeftBorder, pInsideBottomBorder, pInsideRightBorder);  

    TotalBorderWidth = pOutsideLeftBorder + pOutsideRightBorder +
    pInsideLeftBorder + pInsideRightBorder;
    TotalBorderHeight = pOutsideTopBorder + pOutsideBottomBorder +
    pInsideTopBorder + pInsideBottomBorder;

    this.createButtonPanel(pNumberOfButtons, pAlignment);

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
    boolean pEnabledButton, boolean pDefaultButton, String pActionCommand, 
    ActionListener pActionListener) {

    NextButtonIndex = 0;
    if (NextButtonIndex < NumberOfButtons) {
      Buttons[NextButtonIndex] = new JButton(pButtonText);
      if (pButtonMnemonic != 0)
        Buttons[NextButtonIndex].setMnemonic(pButtonMnemonic);
      Buttons[NextButtonIndex].setEnabled(pEnabledButton);
      Buttons[NextButtonIndex].setDefaultCapable(pDefaultButton);
      if (pDefaultButton)
        DefaultButtonIndex = NextButtonIndex;
      Buttons[NextButtonIndex].setActionCommand(pActionCommand);
      Buttons[NextButtonIndex].addActionListener(pActionListener);
      NextButtonIndex++;
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void addNextButton(String pButtonText, int pButtonMnemonic, 
    boolean pEnabledButton, boolean pDefaultButton, String pActionCommand, 
    ActionListener pActionListener) {

    if (NextButtonIndex < NumberOfButtons) {
      Buttons[NextButtonIndex] = new JButton(pButtonText);
      if (pButtonMnemonic != 0)
        Buttons[NextButtonIndex].setMnemonic(pButtonMnemonic);
      Buttons[NextButtonIndex].setEnabled(pEnabledButton);
      Buttons[NextButtonIndex].setDefaultCapable(pDefaultButton);
      if (pDefaultButton)
        DefaultButtonIndex = NextButtonIndex;
      Buttons[NextButtonIndex].setActionCommand(pActionCommand);
      Buttons[NextButtonIndex].addActionListener(pActionListener);
      NextButtonIndex++;
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addLastButton(String pButtonText, int pButtonMnemonic, 
    boolean pEnabledButton, boolean pDefaultButton, String pActionCommand, 
    ActionListener pActionListener) {

    if (NextButtonIndex < NumberOfButtons) {
      Buttons[NextButtonIndex] = new JButton(pButtonText);
      if (pButtonMnemonic != 0)
        Buttons[NextButtonIndex].setMnemonic(pButtonMnemonic);
      Buttons[NextButtonIndex].setEnabled(pEnabledButton);
      Buttons[NextButtonIndex].setDefaultCapable(pDefaultButton);
      if (pDefaultButton)
        DefaultButtonIndex = NextButtonIndex;
      Buttons[NextButtonIndex].setActionCommand(pActionCommand);
      Buttons[NextButtonIndex].addActionListener(pActionListener);
      NextButtonIndex++;
      this.init();
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addSingleButton(String pButtonText, int pButtonMnemonic, 
    boolean pEnabledButton, boolean pDefaultButton, String pActionCommand, 
    ActionListener pActionListener) {

   this.addSingleButton(pButtonText, pButtonMnemonic, pEnabledButton, 
   pDefaultButton, pActionCommand, pActionListener, null);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addSingleButton(String pButtonText, int pButtonMnemonic, 
    boolean pEnabledButton, boolean pDefaultButton, String pActionCommand, 
    ActionListener pActionListener, String pToolTipText) {

    if (NextButtonIndex < NumberOfButtons) {
      Buttons[NextButtonIndex] = new JButton(pButtonText);
      if (pButtonMnemonic != 0)
        Buttons[NextButtonIndex].setMnemonic(pButtonMnemonic);
      Buttons[NextButtonIndex].setEnabled(pEnabledButton);
      Buttons[NextButtonIndex].setDefaultCapable(pDefaultButton);
      if (pDefaultButton)
        DefaultButtonIndex = NextButtonIndex;
      Buttons[NextButtonIndex].setActionCommand(pActionCommand);
      Buttons[NextButtonIndex].addActionListener(pActionListener);
      if (pToolTipText != null)
        Buttons[NextButtonIndex].setToolTipText(pToolTipText);
      NextButtonIndex++;
      this.init();
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void setEnabled(int pButtonIndex, boolean pEnabledButton) {

    if ( (pButtonIndex >= 0) && (pButtonIndex < NumberOfButtons) )
      if (Buttons[pButtonIndex] != null)
        Buttons[pButtonIndex].setEnabled(pEnabledButton);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void setLabel(int pButtonIndex, String pLabel) {

    if ( (pButtonIndex >= 0) && (pButtonIndex < NumberOfButtons) )
      if (Buttons[pButtonIndex] != null)
        Buttons[pButtonIndex].setLabel(pLabel);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void setAllEnabled(boolean pEnabledButton) {

    for (int i = 0; i < NumberOfButtons; i++)
      if (Buttons[i] != null)
        Buttons[i].setEnabled(pEnabledButton);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public JButton getButton(int pButtonIndex) {

    if ( (pButtonIndex >= 0) &&  (pButtonIndex < Buttons.length) )
      return Buttons[pButtonIndex];
    else
      return null;

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public JButton getDefaultButton() {

    if ( (DefaultButtonIndex != UNKNOWN) && 
      (Buttons[DefaultButtonIndex] != null) )
      return Buttons[DefaultButtonIndex];
    else
      return null;

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void defaultButtonRequestFocus() {

    if ( (DefaultButtonIndex != UNKNOWN) && 
      (Buttons[DefaultButtonIndex] != null) )
      Buttons[DefaultButtonIndex].requestFocus();

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void buttonRequestFocus(int pButtonIndex) {

    if ( (pButtonIndex >= 0) &&  (pButtonIndex < Buttons.length) )
      Buttons[pButtonIndex].requestFocus();

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void init() {

    int vMaxWidth = 0;
    int vMaxHeight = 0;
    for (int i = 0; i < NumberOfButtons; i++)
      if (Buttons[i] != null) {
        vMaxWidth = Math.max( vMaxWidth, 
          (int)Buttons[i].getPreferredSize().width );
        vMaxHeight = Math.max( vMaxHeight, 
          (int)Buttons[i].getPreferredSize().height );
      }
    for (int i = 0; i < NumberOfButtons; i++)
    if (Buttons[i] != null) {
      Buttons[i].setPreferredSize( new Dimension(vMaxWidth, vMaxHeight) );
      Buttons[i].setMinimumSize( new Dimension(vMaxWidth, vMaxHeight) );
      if (i == 0) {
        if (Alignment <= HORIZONTAL_RIGHT) {
          ButtonGridBagPanel.addButton( Buttons[i], i, 0, 
          new Insets(0, 0, 0, 0) );
        }
        else {
          ButtonGridBagPanel.addButton( Buttons[i], 0, i, 
          new Insets(0, 0, VerticalGap, 0) );
        }
      }
      else if (i == (NumberOfButtons - 1)) {
        if (Alignment <= HORIZONTAL_RIGHT) {
          ButtonGridBagPanel.addButton( Buttons[i], i, 0, 
          new Insets(0, HorizontalGap, 0, 0) );
        }
        else {
          ButtonGridBagPanel.addButton( Buttons[i], 0, i, 
          new Insets(0, 0, 0, 0) );
        }        
      }
      else {
        if (Alignment <= HORIZONTAL_RIGHT) {
          ButtonGridBagPanel.addButton( Buttons[i], i, 0,  
          new Insets(0, HorizontalGap, 0, 0) );
        }
        else {
          ButtonGridBagPanel.addButton( Buttons[i], 0, i,  
          new Insets(0, 0, VerticalGap, 0) );
        }
      }
    }
    switch (Alignment) {
      case HORIZONTAL_LEFT: {
        this.add(ButtonGridBagPanel);
        break;        
      } 
      case HORIZONTAL_CENTER: {
        this.add(ButtonGridBagPanel);
        break;        
      } 
      case HORIZONTAL_RIGHT: {
        this.add(ButtonGridBagPanel);
        break;        
      } 
      case VERTICAL_TOP: {
        this.add(ButtonGridBagPanel, BorderLayout.NORTH);
        break;        
      } 
      case VERTICAL_MIDDLE: {
        this.add(ButtonGridBagPanel, BorderLayout.CENTER);
        break;        
      } 
      case VERTICAL_BOTTOM: {
        this.add(ButtonGridBagPanel, BorderLayout.SOUTH);
        break;        
      } 
    }
    this.revalidate();

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private void createButtonPanel(int pNumberOfButtons, int pAlignment) {

    NumberOfButtons = pNumberOfButtons;
    Alignment = pAlignment;
    HorizontalGap = DEFAULT_HORIZONTAL_GAP;
    VerticalGap = DEFAULT_VERTICAL_GAP;
    NextButtonIndex = 0;
    Buttons = new JButton[NumberOfButtons];
    for (int i = 0; i < NumberOfButtons; i++) Buttons[i] = null;      

    switch (Alignment) {
      case HORIZONTAL_LEFT: {
        this.setLayout( new FlowLayout(FlowLayout.LEFT, 0, 0) );
        break;        
      } 
      case HORIZONTAL_CENTER: {
        this.setLayout( new FlowLayout(FlowLayout.CENTER, 0, 0) );
        break;        
      } 
      case HORIZONTAL_RIGHT: {
        this.setLayout( new FlowLayout(FlowLayout.RIGHT, 0, 0) );
        break;        
      } 
      case VERTICAL_TOP: {
        this.setLayout( new BorderLayout(0, 0) );
        break;        
      } 
      case VERTICAL_MIDDLE: {
        this.setLayout( new BorderLayout(0, 0) );
        break;        
      } 
      case VERTICAL_BOTTOM: {
        this.setLayout( new BorderLayout(0, 0) );
        break;        
      } 
    }
    ButtonGridBagPanel = new KGridBagPanel(0, 0, 0, 0);    

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}