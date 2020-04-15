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

import java.util.*;
import java.io.*;
import org.hypknowsys.misc.io.*;

/**
 * A Trail represents a sequence of PageOccurrences made by any visitor.
 * PageOccurrences can subsequently be added to a Trail. Trails are used by
 * Seesions and by MintPatternDescriptors. Trails are called patterns in
 * generating and evaluating MINT queries. The calling class may have to 
 * check whether a special Trail is valid or not. <p>
 *
 * Modified by kwinkler: methods getLastPageOccurrence() and 
 * getPreviousPageOccurrence added <p>
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class Trail implements Serializable {

  // ########## attributes ##########
 
  private Vector oTrail = null; 
  private transient Iterator TrailIterator = null;  
  private transient ListIterator TrailListIterator = null;  
  private boolean ValidTrail = true;

  // ########## constructors ##########
  
/**
 * constructs an empty Trail
 */  

  public Trail() {
  
    oTrail = null;   TrailIterator = null;   
    TrailListIterator = null; ValidTrail = true;
    
  }  
  
  // ########## mutator methods ##########
  
  public void setTrail(Vector pTrail) 
    { oTrail = pTrail; }
    
  // ########## accessor methods ##########  
  
  public Vector getTrail() 
    { return oTrail; }
  public boolean isValid() 
    { return ValidTrail; }
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    String result = "Trail: TS=" + this.getSize();    
    for (int i = 0; i < this.getSize(); i++) 
      result +=  " " + ( (PageOccurrence)oTrail.elementAt(i) ).toString();
            
    return result;
    
  }  // toString()
    
  public boolean equals(Object pTrail) {
  
    if ( ! (pTrail instanceof Trail) )
      return false;
    if ( this.getSize() != ( (Trail)pTrail ).getSize() )
      return false;
    else {
      PageOccurrence MyPageOccurrence = this.getFirstPageOccurrence();
      PageOccurrence OtherPageOccurrence = 
        ( (Trail)pTrail ).getFirstPageOccurrence();
      while (MyPageOccurrence != null) {
        if ( ! MyPageOccurrence.equals(OtherPageOccurrence) ) return false;
        MyPageOccurrence = this.getNextPageOccurrence();
        OtherPageOccurrence = ( (Trail)pTrail ).getNextPageOccurrence();
      }
    }
  
    return true;
  
  }  // equals()      
  
  /**
   * @return the number of PageOccurrences contained in this Trail
   */
  
  public int getSize() { 
  
    if (oTrail == null) 
      return 0;
    else
      return oTrail.size(); 
      
  }  // getSize()
  
  
  /**
   * return Trail as an array of PageOccurrences, the order of 
   * PageOccurrences is 
   * preserved: array[0] contains the first PageOccurrence in Trail
   */  
 
  public PageOccurrence[] getArray() {
  
    PageOccurrence[] aTrail = new PageOccurrence[ oTrail.size() ];
    for (int i = 0; i < aTrail.length; i++) 
      aTrail[i] = (PageOccurrence)oTrail.elementAt(i);
      
    return aTrail;
    
  }  // getArray()
  
  /**
   * return Trail as an array of PageOccurrences.toString(), the order of 
   * PageOccurrences is preserved: array[0] contains the first PageOccurrence
   *  in Trail
   */  
 
  public String[] getStringArray() {
  
    String[] aTrail = new String[ oTrail.size() ];
    for (int i = 0; i < aTrail.length; i++) 
      aTrail[i] = ( (PageOccurrence)oTrail.elementAt(i) ).toString();
      
    return aTrail;
    
  }  // getStringArray()  
  
  /**
   * @return first PageOccurrence of the Trail or null if the
   * Trail is empty, must be called to start a new traversal
   */

  public PageOccurrence getFirstPageOccurrence() {
  
    if (oTrail == null) return null;
  
    TrailListIterator = null;
    TrailIterator = oTrail.iterator();
    if ( TrailIterator.hasNext() )      
      return (PageOccurrence)TrailIterator.next();     
    else
      return null;

  }  // getFirstPageOccurrence() 
  
  /**
   * @return next PageOccurrence of the Trail or null if the
   * Trail is empty, subsequent calls traverse the Trail
   */

  public PageOccurrence getNextPageOccurrence() {
  
    if (TrailIterator == null) return getFirstPageOccurrence();

    if ( TrailIterator.hasNext() ) 
      return (PageOccurrence)TrailIterator.next();     
    else
      return null;

  }  // getNextPageOccurrence()
  
  /**
   * @return last PageOccurrence of the Trail or null if the
   * Trail is empty, must be called to start a new backward traversal
   */

  public PageOccurrence getLastPageOccurrence() {
  
    if (oTrail == null) return null;
  
    TrailIterator = null;
    TrailListIterator = oTrail.listIterator( oTrail.size() );
    if ( TrailListIterator.hasPrevious() )
      return (PageOccurrence)TrailListIterator.previous();     
    else
      return null;

  }  // getLastPageOccurrence() 
  
  /**
   * @return previous PageOccurrence of the Trail or null if the
   * Trail is empty, subsequent calls traverse the Trail backwards
   */

  public PageOccurrence getPreviousPageOccurrence() {
  
    if (TrailListIterator == null) return getLastPageOccurrence();

    if ( TrailListIterator.hasPrevious() )
      return (PageOccurrence)TrailListIterator.previous();
    else
      return null;

  }  // getPreviousPageOccurrence()
  
  /**
   * returns the Trail's PageOccurrence given by pIndex
   * @param pIndex 0 <= pIndex < this.getSize()
   * @return corresponding PageOccurrence or null if pIndex is out of 
   * valid range
   */

  public PageOccurrence getPageOccurrence(int pIndex) {
  
    if ( (pIndex >= 0) && ( pIndex <= this.getSize() ) )
      return (PageOccurrence)oTrail.elementAt(pIndex);
    else
      return null;  

  }  // getPageOccurrence()  
  
  /**
   * @return a String represation for this Trail's storage in text files
   */
  
  public String toItemLine() { 
  
    String vResult = Itemizer.intToItem( this.getSize() );
    
    for (int i = 0; i < this.getSize(); i++) 
      vResult +=  Itemizer.ITEM_SEPARATOR + Itemizer.stringToItem( 
        ( (PageOccurrence)oTrail.elementAt(i) ).toString() );
            
    return vResult;
    
  }  // toItemLine()

  /**
   * @param  pTrail sets all attributes of this Trail according to the
   * String pTrail that must be a valid representation for a Trail, i.g.
   * generated by this.toItemLine()
   */
  
  public void fromItemLine(String pTrail) {
  
    int vTrailSize = 0; 
    String vBuffer = ""; 
    PageOccurrence oPageOccurrence = new PageOccurrence();
    Itemizer oItemizer = new Itemizer(pTrail);
    
    try {   
      vTrailSize = Itemizer.itemToInt( oItemizer.getNextItem() );      
      oTrail = new Vector();
      for (int i = 0; i < vTrailSize; i++) {
        vBuffer = Itemizer.itemToString( oItemizer.getNextItem() );
        oPageOccurrence = new PageOccurrence();
        oPageOccurrence.fromString(vBuffer);
        oTrail.add(oPageOccurrence);
      }
    }
    catch (NoSuchElementException e) { this.handleError(pTrail); }
    catch (NumberFormatException e) { this.handleError(pTrail); }
    catch (StringIndexOutOfBoundsException e) { this.handleError(pTrail); }  
  
  }  // fromItemLine()
  
  /**
   * resets all attributes, marks this Trail as 'not valid' and writes the
   * given String on System.out
   * @param pTrail Error-Message to be written on System.out
   */
  
  protected void handleError(String pTrail) {
  
    System.out.println("Error: Line doesn't confirm to " + 
      "Trail syntax. \nLine = " + pTrail );
      
    oTrail = null;
    TrailIterator = null;    
    ValidTrail = false;
      
  }  // handleError()
  
  /**
   * resets the Trail and adds the first PageOccurrence
   * @param pPageOccurrence PageOccurrence to add
   */  
  
  public void setFirstPageOccurrence(PageOccurrence pPageOccurrence) {
  
    oTrail = new Vector();
    oTrail.add(pPageOccurrence);
  
  }  // setFirstPageOccurrence()
  
  /**
   * adds the given PageOccurrence at the Trail's tail
   * @param pPageOccurrence PageOccurrence to add
   */  
  
  public void setNextPageOccurrence(PageOccurrence pPageOccurrence) {
  
    oTrail.add(pPageOccurrence);
  
  }  // setNextPageOccurrence()  

  public static void main(String[] args) {

    Trail trail = new Trail();
    trail.fromItemLine("4 \"[100;1]\" \"[102;1]\" \"[100;2]\" \"[103;1]\"");

    PageOccurrence po = trail.getFirstPageOccurrence();
    while (po != null) {
      System.out.println( "> " + po.toString() );
      po = trail.getNextPageOccurrence();
    }

    po = trail.getLastPageOccurrence();
    while (po != null) {
      System.out.println( "< " + po.toString() );
      po = trail.getPreviousPageOccurrence();
    }

  }
  
}  // class Trail
