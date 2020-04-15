/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.core.default10;

import java.io.*;

/**
 * A PageOccurrence_SelfContained contains a PageID and the occurrence of this 
 * Page in the corresponding Trail or Observation. It is defined by Myra 
 * Spiliopoulou and Lukas Faulstich. Basically this class is introduced to 
 * modell repeated visits of the same Page in one branch of the AggregateTree.
 * A PageOccurrence_SelfContained contains graph drawing coordibates as well.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class PageOccurrence_SelfContained implements Serializable {

  // ########## attributes ##########

  private long PageID = 0L;
  private int Occurrence = 0;
  private int X = 0;
  private int Y = 0;

  // ########## constructors ##########

/**
 * constructs an empty PageOccurrence_SelfContained
 */

  public PageOccurrence_SelfContained() {

    PageID = 0L;   Occurrence = 0;
    X = 0;   Y = 0;

  }

/**
 * constructs an PageOccurrence_SelfContained containing the given data
 * @param pPageID PageID of the corresponding Page in database Pages
 * @param pOccurrence occurrence number ot the corresponding page in its Trail
 * or Observation
 */

  public PageOccurrence_SelfContained(long pPageID, int pOccurrence) {

    PageID = pPageID;  Occurrence = pOccurrence;
    X = 0;   Y = 0;

  }

  // ########## mutator methods ##########

  public void setPageID(long pPageID) { PageID = pPageID; }
  public void setOccurrence(int pOccurrence) { Occurrence = pOccurrence; }
  public void setX(int pX) { X = pX; }
  public void setY(int pY) { Y = pY; }

  // ########## accessor methods ##########

  public long getPageID() { return PageID; }
  public int getOccurrence() { return Occurrence; }
  public int getX() { return X; }
  public int getY() { return Y; }

  // ########## standard methods ##########

  public boolean equals(Object pPageOccurrence_SelfContained) {

  if ( ! (pPageOccurrence_SelfContained instanceof 
    PageOccurrence_SelfContained) )
    return false;
  if ( ( PageID == ( (PageOccurrence_SelfContained)
    pPageOccurrence_SelfContained ).getPageID() ) &&
    ( Occurrence == ( (PageOccurrence_SelfContained)
    pPageOccurrence_SelfContained ).getOccurrence() ) )
    return true;
  else
    return false;

  }  // equals()

  public String toString() {

    return ("[" + PageID + ";" + Occurrence + "]");

  }  // toString()

  /**
   * compare the given PageOccurrence_SelfContained with this: 
   * this < pPageOccurrence_SelfContained: vResult < 0;
   * this == pPageOccurrence_SelfContained: vResult = 0; 
   * this > pPageOccurrence_SelfContained: vResult > 0;
   * sorting: [10;1] < [20;1] < [20;2] < [30;1]
   * @param pPageOccurrence_SelfContained PageOccurrence_SelfContained to 
   * compare with this
   * @return result of comparison
   */

  public int compareTo(
    PageOccurrence_SelfContained pPageOccurrence_SelfContained) {

    int vResult = 0;

    if (pPageOccurrence_SelfContained.getPageID() > PageID)
      vResult = -1;
    else
      if (pPageOccurrence_SelfContained.getPageID() < PageID)
        vResult = 1;
      else  // pPageOccurrence_SelfContained.getPageID() == PageID
        if ( pPageOccurrence_SelfContained.getOccurrence() > Occurrence )
          vResult = -1;
        else
          if ( pPageOccurrence_SelfContained.getOccurrence() < Occurrence )
            vResult = 1;
          else
            vResult = 0;

    return vResult;

  }  // compareTo()

}  // class PageOccurrence_SelfContained

