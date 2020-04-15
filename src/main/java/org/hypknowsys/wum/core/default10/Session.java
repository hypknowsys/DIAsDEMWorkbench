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
 * A session represents a sequence of HTTP-requests made by one visitor who 
 * is characterized by VisitorID. The sequence of requests is 
 * called Trail. FirstTimeStamp is the timestamp of the trail's first requested
 * URL.The calling class may have to check whether a special Session is valid
 * or not. Pages can subsequently be added to a Session: The corresponding 
 * PageOccurrences will automatically be determined
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class Session implements Serializable {

  // ########## attributes ##########
 
  private long VisitorID = 0L;
  private String FirstTimeStamp = null;
  private Trail oTrail = null;  
  private transient boolean ValidSession = true;
  private transient Hashtable PageOccurrenceCounter = null;
  private transient String EndOfSessionTimeStamp = null;  

  // ########## constructors ##########
  
/**
 * constructs an empty Session
 */
  
  public Session() {
  
    VisitorID = 0L;   FirstTimeStamp = "";   oTrail = new Trail();    
    ValidSession = true;   PageOccurrenceCounter = null;   
    EndOfSessionTimeStamp = null;
    
  }  
  
/**
 * constructs an Session with the given attributes
 * @param pVisitorID ID of the visitors corresponding to this Session
 * @param pFirstTimeStamp FirstTimeStamp of the Session
 */
    
  public Session(long pVisitorID, String pFirstTimeStamp) {
                 
    VisitorID = pVisitorID;   FirstTimeStamp = pFirstTimeStamp;   
    oTrail = new Trail();  
    ValidSession = true;   PageOccurrenceCounter = null;   
    EndOfSessionTimeStamp =null;
    
  }     
  
  // ########## mutator methods ##########
  
  public void setVisitorID(long pVisitorID) 
    { VisitorID = pVisitorID; }
  public void setFirstTimeStamp(String pFirstTimeStamp) 
    { FirstTimeStamp = pFirstTimeStamp; }
  public void setTrail(Trail pTrail) 
    { oTrail = pTrail; }
  public void setEndOfSessionTimeStamp(String pEndOfSessionTimeStamp) 
    { EndOfSessionTimeStamp = pEndOfSessionTimeStamp; }  
    
  // ########## accessor methods ##########  
  
  public long getVisitorID() 
    { return VisitorID; }
  public String getFirstTimeStamp() 
    { return FirstTimeStamp; }
  public Trail getTrail() 
    { return oTrail; }
  public String getEndOfSessionTimeStamp() 
    { return EndOfSessionTimeStamp; }
  public boolean isValid() 
    { return ValidSession; }
  public int getTrailSize() 
    { return oTrail.getSize(); } 
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    String vResult = "Session: ID=" + VisitorID + " FTS=" + 
      FirstTimeStamp + " " + oTrail.toString();
    
    return vResult;
    
  }  // toString()
  
  
  /**
   * resets the Hashtable that keeps track of the occurrence number
   * for each Page and adds a first PageOccurrence [givenPageID;1] to its Trail
   * @param pPageID ID of the corresponding Page
   */
  
  public void setFirstPage(long pPageID) {
  
    PageOccurrenceCounter = new Hashtable();    
    Object oObject = PageOccurrenceCounter.put( new Long(pPageID), 
      new Integer(1) );
    oTrail.setFirstPageOccurrence( new PageOccurrence(pPageID, 1) );
  
  }  // setFirstPage()
  
  /**
   * by using a Hashtable that keeps track of the occurrence number
   * for each Page the PageOccurrence will be determined; PageOccurrences 
   * will be added to its Trail
   * @param pPageID ID of the corresponding Page
   */
  
  public void setNextPage(long pPageID) {

    Integer vOldOccurrence = (Integer)PageOccurrenceCounter.get( 
      new Long(pPageID) );    
    int vNewOccurrence = 1;
    if (vOldOccurrence != null) 
      vNewOccurrence = vOldOccurrence.intValue() + 1; 
    
    Object oObject = PageOccurrenceCounter.put( new Long(pPageID), 
      new Integer(vNewOccurrence) );
    oTrail.setNextPageOccurrence( 
      new PageOccurrence(pPageID, vNewOccurrence) );
    
  }  // setNextPage()
  
  /**
   * @return a String represation for this Session's storage in text files
   */
  
  public String toItemLine() { 
  
    String vResult = Itemizer.longToItem(VisitorID) + Itemizer.ITEM_SEPARATOR +
      Itemizer.stringToItem(FirstTimeStamp) + Itemizer.ITEM_SEPARATOR + 
      oTrail.toItemLine();
            
    return vResult;
    
  }  // toItemLine()
  
  /**
   * @param  pSession sets all attributes of this Sessions according to the
   * String pSession that must be a valid representation for a Session, i.g.
   * generated by this.toItemLine()
   */
  
  public void fromItemLine(String pSession) {
  
    int vTrailSize = 0; 
    String vBuffer = ""; 
    PageOccurrence oPageOccurrence = new PageOccurrence();
    Itemizer oItemizer = new Itemizer(pSession);
    
    try {   
      VisitorID = Itemizer.itemToLong( oItemizer.getNextItem() );
      FirstTimeStamp = Itemizer.itemToString( oItemizer.getNextItem() );
      oTrail = new Trail();
      oTrail.fromItemLine( oItemizer.getEndOfLine() );
    }
    catch (NoSuchElementException e) { this.handleError(pSession); }
    catch (NumberFormatException e) { this.handleError(pSession); }
    catch (StringIndexOutOfBoundsException e) { 
      this.handleError(pSession); 
    }   
  
  }  // fromItemLine()
  
  /**
   * resets all attributes, marks this Session as 'not valid' and writes the
   * given String on System.out
   * @param pSession Error-Message to be written on System.out
   */
  
  protected void handleError(String pSession) {
  
    System.out.println("Error: Line doesn't confirm to " + 
      "session syntax. \nLine = " + pSession);
      
    VisitorID = 0L;
    FirstTimeStamp = "";
    oTrail = null;    
    ValidSession = false;
      
  }  // handleError()
  
}  // class Session

