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
  
public class KBorderPanel extends KPanel {

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

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KBorderPanel() {

    super();

    this.setLayout( new BorderLayout() );
    this.setGaps(0, 0);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KBorderPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder, 
      pOutsideRightBorder);

    this.setLayout( new BorderLayout() );
    this.setGaps(0, 0);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public KBorderPanel(int pOutsideTopBorder, int pOutsideLeftBorder,
    int pOutsideBottomBorder, int pOutsideRightBorder, String pBorderTitle,
    int pInsideTopBorder, int pInsideLeftBorder,
    int pInsideBottomBorder, int pInsideRightBorder) {

    super(pOutsideTopBorder, pOutsideLeftBorder, pOutsideBottomBorder, 
      pOutsideRightBorder, pBorderTitle, pInsideTopBorder, 
      pInsideLeftBorder, pInsideBottomBorder, pInsideRightBorder);  

    this.setLayout( new BorderLayout() );
    this.setGaps(0, 0);

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

  public void setGaps(int pHorizontalGap, int pVerticalGap) {

    ( (BorderLayout)this.getLayout() ).setHgap(pHorizontalGap);
    ( (BorderLayout)this.getLayout() ).setVgap(pVerticalGap);
    this.revalidate();

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addNorth(Component pComponent) {

    this.add(pComponent, BorderLayout.NORTH);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addEast(Component pComponent) {

    this.add(pComponent, BorderLayout.EAST);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addSouth(Component pComponent) {

    this.add(pComponent, BorderLayout.SOUTH);

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void addWest(Component pComponent) {

    this.add(pComponent, BorderLayout.WEST);

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void addCenter(Component pComponent) {

    this.add(pComponent, BorderLayout.CENTER);

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