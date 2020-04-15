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
import javax.swing.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KGridBagPanel extends KPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private GridBagLayout PanelLayout = null;

  private int LabelAlignment = JLabel.LEFT;
  private boolean OpaqueBlankRowsAndColumns = true;

  private GridBagConstraints LabelConstraints = null;
  private GridBagConstraints ButtonConstraints = null;
  private GridBagConstraints ComponentConstraints = null;

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

  public KGridBagPanel() {

    super();

    this.createGridBagPanel();

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KGridBagPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
  int pOutsideBottomBorder, int pOutsideRightBorder) {
    
    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder,
    pOutsideRightBorder);
    
    this.createGridBagPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KGridBagPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
  int pOutsideBottomBorder, int pOutsideRightBorder, String pBorderTitle,
  int pInsideTopBorder, int pInsideLeftBorder,
  int pInsideBottomBorder, int pInsideRightBorder) {
    
    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder,
    pOutsideRightBorder, pBorderTitle, pInsideTopBorder,
    pInsideLeftBorder, pInsideBottomBorder, pInsideRightBorder);
    
    this.createGridBagPanel();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  // must be JLabel.LEFT, CENTER or RIGHT
  public void setLabelAlignment(int pLabelAlignment) { 
    LabelAlignment = pLabelAlignment; }

  // must be GridBagConstraints.NORTH, WEST, ... 
  public void setLabelAnchor(int pLabelAnchor) { 
    LabelConstraints.anchor = pLabelAnchor; }
  
  public void setOpaqueBlankRowsAndColumns(boolean pOpaqueBlankRowsAndColumns) {
    OpaqueBlankRowsAndColumns = pOpaqueBlankRowsAndColumns; }

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

  public KLabel addLabel(String pText, int pGridX, int pGridY, Insets pInsets,
    int pLabelMnemonic, Component pLabelFor, boolean pLabelEnabled,
    String pToolTipText) {

    KLabel currentLabel = new KLabel(pText, LabelAlignment, pLabelEnabled,
    pLabelMnemonic, pLabelFor, pToolTipText);
    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)LabelConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    if (pInsets != null) {
      CurrentConstraints.insets = pInsets;
    }
    PanelLayout.setConstraints(currentLabel, CurrentConstraints);
    this.add(currentLabel);
    
    return currentLabel;

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KLabel addLabel(String pText, int pGridX, int pGridY, 
    int pLabelMnemonic, Component pLabelFor, boolean pLabelEnabled,
    String pToolTipText) {

    return this.addLabel(pText, pGridX, pGridY, null,
    pLabelMnemonic, pLabelFor, pLabelEnabled, pToolTipText);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addLabel(KLabel pKLabel, int pGridX, int pGridY, 
  Insets pInsets) {

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)LabelConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    if (pInsets != null) {
      CurrentConstraints.insets = pInsets;
    }
    PanelLayout.setConstraints(pKLabel, CurrentConstraints);
    this.add(pKLabel);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KLabel addLabel(String pText, int pGridX, int pGridY, Insets pInsets,
    int pLabelMnemonic, Component pLabelFor, boolean pLabelEnabled) {

    return this.addLabel(pText, pGridX, pGridY, pInsets,
    pLabelMnemonic, pLabelFor, pLabelEnabled, null);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KLabel addLabel(String pText, int pGridX, int pGridY, Insets pInsets,
    int pLabelMnemonic, Component pLabelFor) {

    return this.addLabel(pText, pGridX, pGridY, pInsets, pLabelMnemonic, 
    pLabelFor, true);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KLabel addLabel(String pText, int pGridX, int pGridY,
    int pLabelMnemonic, Component pLabelFor) {

    return this.addLabel(pText, pGridX, pGridY, new Insets(0, 0, 0, 0), 
    pLabelMnemonic, pLabelFor, true);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KLabel addLabel(String pText, int pGridX, int pGridY, Insets pInsets) {

    return this.addLabel(pText, pGridX, pGridY, pInsets, '0', null);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KLabel addLabel(String pText, int pGridX, int pGridY) {

    return this.addLabel(pText, pGridX, pGridY, new Insets(0, 0, 0, 0), 
    '0', null);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addLabel(KLabel pKLabel, int pGridX, int pGridY) {

    this.addLabel(pKLabel, pGridX, pGridY, new Insets(0, 0, 0, 0));

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addButton(JButton pButton, int pGridX, int pGridY, 
    Insets pInsets) {

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)ButtonConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    CurrentConstraints.insets = pInsets;
    PanelLayout.setConstraints(pButton, CurrentConstraints);
    this.add(pButton);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addButton(JButton pButton, int pGridX, int pGridY) {

    this.addButton( pButton, pGridX, pGridY, new Insets(0, 0, 0, 0) );

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addKButtonPanel(KButtonPanel pKButtonPanel, int pGridX, 
    int pGridY) {

    this.addKButtonPanel(pKButtonPanel, pGridX, pGridY,
    GridBagConstraints.CENTER);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addKButtonPanel(KButtonPanel pKButtonPanel, int pGridX, 
    int pGridY, int pAnchor) {

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)ButtonConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    CurrentConstraints.anchor = pAnchor;
    CurrentConstraints.insets = new Insets(0, 0, 0, 0);
    PanelLayout.setConstraints(pKButtonPanel, CurrentConstraints);
    this.add(pKButtonPanel);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addComponent(JComponent pComponent, int pGridX, int pGridY, 
    Insets pInsets, int pGridWidth, int pGridHeight, double pWeightY) {

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)ComponentConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    CurrentConstraints.insets = pInsets;
    CurrentConstraints.gridwidth = pGridWidth;
    CurrentConstraints.gridheight = pGridHeight;
    CurrentConstraints.weighty = pWeightY;
    if (pWeightY > 0.0)
      CurrentConstraints.fill = GridBagConstraints.BOTH;
    PanelLayout.setConstraints(pComponent, CurrentConstraints);
    this.add(pComponent);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addComponent(JComponent pComponent, int pGridX, int pGridY, 
    int pGridWidth, int pGridHeight, double pWeightX, double pWeightY) {

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)ComponentConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    CurrentConstraints.gridwidth = pGridWidth;
    CurrentConstraints.gridheight = pGridHeight;
    CurrentConstraints.weightx = pWeightX;
    CurrentConstraints.weighty = pWeightY;
    if (pWeightY > 0.0)
      CurrentConstraints.fill = GridBagConstraints.BOTH;
    PanelLayout.setConstraints(pComponent, CurrentConstraints);
    this.add(pComponent);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addComponent(JComponent pComponent, int pGridX, int pGridY, 
    Insets pInsets, int pGridWidth, int pGridHeight) {

    this.addComponent( pComponent, pGridX, pGridY, new Insets(0, 0, 0, 0), 
      pGridWidth, pGridHeight, 0.0 );

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addComponent(JComponent pComponent, int pGridX, int pGridY, 
    Insets pInsets) {

    this.addComponent( pComponent, pGridX, pGridY, new Insets(0, 0, 0, 0), 
      1, 1, 0.0 );

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addComponent(JComponent pComponent, int pGridX, int pGridY) {

    this.addComponent( pComponent, pGridX, pGridY, new Insets(0, 0, 0, 0), 
      1, 1, 0.0 );

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addBlankColumn(int pGridX, int pGridY, int pGridWidth, 
    int pGridHeight, double pWeightX, double pWeightY, int pColumnWidth ) {

    JPanel BlankColumn = new JPanel();
    BlankColumn.setOpaque(OpaqueBlankRowsAndColumns);
    BlankColumn.setMinimumSize( new Dimension(pColumnWidth, 1) ); 
    BlankColumn.setPreferredSize( new Dimension(pColumnWidth, 1) );

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)ComponentConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    CurrentConstraints.gridwidth = pGridWidth;
    CurrentConstraints.gridheight = pGridHeight;
    CurrentConstraints.weightx = pWeightX;
    CurrentConstraints.weighty = pWeightY;
    PanelLayout.setConstraints(BlankColumn, CurrentConstraints);
    this.add(BlankColumn);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addBlankColumn(int pGridX, int pGridY, int pGridWidth, 
    int pGridHeight, double pWeightX, double pWeightY) {

    JPanel BlankColumn = new JPanel();
    BlankColumn.setOpaque(OpaqueBlankRowsAndColumns);

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)ComponentConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    CurrentConstraints.gridwidth = pGridWidth;
    CurrentConstraints.gridheight = pGridHeight;
    CurrentConstraints.weightx = pWeightX;
    CurrentConstraints.weighty = pWeightY;
    PanelLayout.setConstraints(BlankColumn, CurrentConstraints);
    this.add(BlankColumn);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addBlankColumn(int pGridX, int pGridY, int pColumnWidth ) {

    this.addBlankColumn(pGridX, pGridY, 1, GridBagConstraints.REMAINDER, 
      0.0, 0.0, pColumnWidth);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addBlankRow(int pGridX, int pGridY, int pGridWidth, 
    int pGridHeight, double pWeightX, double pWeightY) {

    JPanel BlankRow = new JPanel();
    BlankRow.setOpaque(OpaqueBlankRowsAndColumns);

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)ComponentConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    CurrentConstraints.gridwidth = pGridWidth;
    CurrentConstraints.gridheight = pGridHeight;
    CurrentConstraints.weightx = pWeightX;
    CurrentConstraints.weighty = pWeightY;
    PanelLayout.setConstraints(BlankRow, CurrentConstraints);
    this.add(BlankRow);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addBlankRow(int pGridX, int pGridY, int pGridWidth, 
    int pGridHeight, double pWeightX, double pWeightY, int pRowHeight,
    int pPreferredRowLength) {

    JPanel BlankRow = new JPanel();
    BlankRow.setOpaque(OpaqueBlankRowsAndColumns);
    BlankRow.setMinimumSize( new Dimension(pPreferredRowLength, pRowHeight) ); 
    BlankRow.setPreferredSize( new Dimension(pPreferredRowLength, pRowHeight) );

    GridBagConstraints CurrentConstraints = 
      (GridBagConstraints)ComponentConstraints.clone();
    CurrentConstraints.gridx = pGridX;
    CurrentConstraints.gridy = pGridY;
    CurrentConstraints.gridwidth = pGridWidth;
    CurrentConstraints.gridheight = pGridHeight;
    CurrentConstraints.weightx = pWeightX;
    CurrentConstraints.weighty = pWeightY;
    PanelLayout.setConstraints(BlankRow, CurrentConstraints);
    this.add(BlankRow);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addBlankRow(int pGridX, int pGridY, int pGridWidth, 
    int pGridHeight, double pWeightX, double pWeightY, int pRowHeight) {

    this.addBlankRow(pGridX, pGridY, GridBagConstraints.REMAINDER, 1, 
      pWeightX, pWeightY, pRowHeight, 1);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addBlankRow(int pGridX, int pGridY, int pRowHeight,
    int pPreferredRowLength ) {

    this.addBlankRow(pGridX, pGridY, GridBagConstraints.REMAINDER, 1, 
      0.0, 0.0, pRowHeight, pPreferredRowLength);

  } 
  
  /* ########## ########## ########## ########## ########## ######### */

  public void addBlankRow(int pGridX, int pGridY, int pRowHeight ) {

    this.addBlankRow(pGridX, pGridY, GridBagConstraints.REMAINDER, 1, 
      0.0, 0.0, pRowHeight, 1);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createGridBagPanel() {

    PanelLayout = new GridBagLayout();
    this.setLayout(PanelLayout);
    LabelConstraints = new GridBagConstraints();
    ButtonConstraints = new GridBagConstraints();
    ComponentConstraints = new GridBagConstraints();

    LabelConstraints.gridx = 0;
    LabelConstraints.gridy = 0;
    LabelConstraints.gridwidth = 1;
    LabelConstraints.gridheight = 1;
    LabelConstraints.fill = GridBagConstraints.NONE;
    LabelConstraints.ipadx = 0;
    LabelConstraints.ipady = 0;
    LabelConstraints.insets = new Insets(0, 0, 0, 0);
    LabelConstraints.anchor = GridBagConstraints.WEST;
    LabelConstraints.weightx = 0.0;
    LabelConstraints.weighty = 0.0;
  
    ButtonConstraints.gridx = 0;
    ButtonConstraints.gridy = 0;
    ButtonConstraints.gridwidth = 1;
    ButtonConstraints.gridheight = 1;
    ButtonConstraints.fill = GridBagConstraints.NONE;
    ButtonConstraints.ipadx = 0;
    ButtonConstraints.ipady = 0;
    ButtonConstraints.insets = new Insets(0, 0, 0, 0);
    ButtonConstraints.anchor = GridBagConstraints.CENTER;
    ButtonConstraints.weightx = 0.0;
    ButtonConstraints.weighty = 0.0;
  
    ComponentConstraints.gridx = 0;
    ComponentConstraints.gridy = 0;
    ComponentConstraints.gridwidth = 1;
    ComponentConstraints.gridheight = 1;
    ComponentConstraints.fill = GridBagConstraints.HORIZONTAL;
    ComponentConstraints.ipadx = 0;
    ComponentConstraints.ipady = 0;
    ComponentConstraints.insets = new Insets(0, 0, 0, 0);
    ComponentConstraints.anchor = GridBagConstraints.NORTHWEST;
    ComponentConstraints.weightx = 1.0;
    ComponentConstraints.weighty = 0.0;

  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}
