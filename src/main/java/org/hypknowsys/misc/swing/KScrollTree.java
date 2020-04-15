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
import javax.swing.tree.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KScrollTree extends KBorderPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private JScrollPane MyScrollPane = null;
  public JTree ScrollTree = null;
  public DefaultTreeCellRenderer ScrollTreeCellRenderer = null;

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

  public KScrollTree(JTree pScrollTree) {

    super();
    
    ScrollTree = pScrollTree;
    this.initScrollPane();

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KScrollTree(JTree pScrollTree, boolean pEditable, String pImageLeaf,
    String pImageOpen, String pImageClosed) {
    
    super();
    
    ScrollTree = pScrollTree;
    ScrollTree.setEditable(pEditable);
    ScrollTree.putClientProperty("JTree.lineStyle", "Angled");
    ScrollTreeCellRenderer = new DefaultTreeCellRenderer();
    ScrollTreeCellRenderer.setLeafIcon( new ImageIcon(pImageLeaf) );
    ScrollTreeCellRenderer.setOpenIcon( new ImageIcon(pImageOpen) );
    ScrollTreeCellRenderer.setClosedIcon( new ImageIcon(pImageClosed) );
    ScrollTreeCellRenderer.setBorder( BorderFactory.createEmptyBorder(4, 0, 0, 0) );
    ScrollTree.setCellRenderer(ScrollTreeCellRenderer);
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

  public void setScrollTree(JTree pScrollTree, boolean pEditable) {
    
    ScrollTree = pScrollTree;
    ScrollTree.setEditable(pEditable);
    ScrollTree.putClientProperty("JTree.lineStyle", "Angled");
    ScrollTree.setCellRenderer(ScrollTreeCellRenderer);
    this.initScrollPane();
    super.revalidate();

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void initScrollPane() {

    MyScrollPane = new JScrollPane(ScrollTree);
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