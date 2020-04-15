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
 * A PageOccurrence contains a PageID and the occurrence of this Page in the 
 * corresponding Trail or Observation. It is defined by Myra Spiliopoulou and 
 * Lukas Faulstich. Basically this class is introduced to modell repeated 
 * visits of the same Page in one branch of the AggregateTree. A 
 * PageOccurrence contains graph drawing coordibates as well.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class PageOccurrence implements Serializable {

  // ########## attributes ##########
 
  private long PageID = 0L;
  private int Occurrence = 0;  
  private int X = 0;
  private int Y = 0;

  // ########## constructors ##########
  
/**
 * constructs an empty PageOccurrence
 */     
 
  public PageOccurrence() {
  
    PageID = 0L;   Occurrence = 0;            
    X = 0;   Y = 0;   
    
  } 
  
/**
 * constructs an PageOccurrence containing the given data
 * @param pPageID PageID of the corresponding Page in database Pages
 * @param pOccurrence occurrence number ot the corresponding page in its Trail
 * or Observation
 */   
    
  public PageOccurrence(long pPageID, int pOccurrence) {
  
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
  
  public boolean equals(Object pPageOccurrence) {
  
  if ( ! (pPageOccurrence instanceof PageOccurrence) )
    return false;
  if ( ( PageID == ( (PageOccurrence)pPageOccurrence ).getPageID() ) &&
    ( Occurrence == ( (PageOccurrence)pPageOccurrence ).getOccurrence() ) )
    return true;
  else
    return false;
  
  }  // equals()    
    
  public String toString() {
   
    return ("[" + PageID + ";" + Occurrence + "]");
    
  }  // toString()
  
  /**
   * compare the given PageOccurrence with this: this < pPageOccurrence: 
   * result < 0; this == pPageOccurrence: result = 0; 
   * this > pPageOccurrence: result > 0;
   * sorting: [10;1] < [20;1] < [20;2] < [30;1]
   * @param pPageOccurrence PageOccurrence to compare with this
   * @return result of comparison
   */
  
  public int compareTo(PageOccurrence pPageOccurrence) {
  
    int result = 0;
    
    if (pPageOccurrence.getPageID() > PageID)
      result = -1;
    else 
      if (pPageOccurrence.getPageID() < PageID) 
        result = 1;
      else  // pPageOccurrence.getPageID() == PageID
        if ( pPageOccurrence.getOccurrence() > Occurrence )
          result = -1;
        else
          if ( pPageOccurrence.getOccurrence() < Occurrence )
            result = 1;
          else
            result = 0;
  
    return result;
  
  }  // compareTo()    
    
  /**
   * set the attributes of this PageOccurrence according to the values
   * given by the string; the given string must conform with the syntax
   * of PageOccurrence used by this.toString(), otherwise PageId = 0L and
   * Occurrence = 0
   * @param pPageOccurrence string containing PageOccurrence, e.g. "[30;1]"
   */

  public void fromString(String pPageOccurrence) {
    
    int length = pPageOccurrence.length(), 
      vSemicolon = pPageOccurrence.indexOf(";");
    String pageID = pPageOccurrence.substring(1, vSemicolon), 
      vOccurrence = pPageOccurrence.substring(vSemicolon + 1, length - 1);
    
    try {
      PageID = (new Long(pageID)).longValue();
    }
    catch (NumberFormatException e) { PageID = 0L; }
     
    try {
      Occurrence = (new Integer(vOccurrence)).intValue();
    }
    catch (NumberFormatException e) { Occurrence = 0; }
  
  }   // fromString()
  
}  // class PageOccurrence

