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
import org.hypknowsys.misc.util.*;

/**
 * A Traversal represents the move from one Page of the web site to another 
 * made by one visitor who is characterized by VisitorID. Both Page are 
 * representaed by their IDs. Each Traversal is part of AccessLog, therefore
 * the corresponding AccessLogID is also part of a Traversal.
 * The calling class may have to check whether a special Trail is valid or not.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class Traversal implements Serializable {
 
  // ########## attributes ##########

  private long VisitorID = 0L;
  private String TimeStamp = null;
  private long FromPageID = 0L;
  private long ToPageID = 0L;
  private long AccessLogID = 0L;  

  private transient boolean ValidTraversal = true;
  private transient Itemizer MyItemizer = null;
  
  // ########## constructors ##########

/**
 * constructs an empty Traversal
 */

  public Traversal() {
  
    VisitorID = 0L;   TimeStamp = "";
    FromPageID = 0L;   ToPageID = 0L;    
    AccessLogID = 0L;   ValidTraversal = true;

    MyItemizer = new Itemizer("Dummy String");

  }  
  
/**
 * constructs a Traversal with the given attributes
 * @param pVisitorID ID of the visitors corresponding to this Traversal
 * @param pTimeStamp FirstTimeStamp of the Traversal according to the AccessLog
 * @param pFromPageID ID of the referrer Page
 * @param pToPageID ID of the accessed Page
 * @param pAccessLogID ID of the corresponding AccessLog
 */
    
  public Traversal(long pVisitorID, String pTimeStamp, 
    long pFromPageID, long pToPageID, long pAccessLogID) {
                   
    VisitorID = pVisitorID;   TimeStamp = pTimeStamp;
    FromPageID = pFromPageID;   ToPageID = pToPageID;    
    AccessLogID = pAccessLogID;   ValidTraversal = true;
    
    MyItemizer = new Itemizer("Dummy String");

  } 
  
  // ########## mutator methods ##########    
  
  public void setVisitorID(long pVisitorID) { VisitorID = pVisitorID; }
  public void setTimeStamp(String pTimeStamp) { TimeStamp = pTimeStamp; }
  public void setFromPageID(long pFromPageID) { FromPageID = pFromPageID; }
  public void setToPageID(long pToPageID) { ToPageID = pToPageID; }
  public void setAccessLogID(long pAccessLogID) { AccessLogID = pAccessLogID; }
  
  // ########## accessor methods ##########
  
  public long getVisitorID() { return VisitorID; }
  public String getTimeStamp() { return TimeStamp; }
  public TimeStamp getTimeStampInstance() {return new TimeStamp(TimeStamp); }
  public long getFromPageID() { return FromPageID; }
  public long getToPageID() { return ToPageID; }
  public long getAccessLogID() { return AccessLogID; }
  public boolean isValid() { return ValidTraversal; }
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    return( "Traversal: VID=" + VisitorID + " " + TimeStamp + " FPID=" + 
      FromPageID + " TPID=" + ToPageID + " ALID=" + AccessLogID );
      
  }  // toString()  
  
  /**
   * @return a String represation for this Traversal's storage in text files
   */
  
  public String toItemLine() { 
  
    return ( new StringBuffer(200) )
      .append( Itemizer.longToItem(VisitorID) )
      .append(Itemizer.ITEM_SEPARATOR)
      .append( Itemizer.stringToItem(TimeStamp) )
      .append(Itemizer.ITEM_SEPARATOR)
      .append( Itemizer.longToItem(FromPageID) )
      .append(Itemizer.ITEM_SEPARATOR)
      .append( Itemizer.longToItem(ToPageID) )
      .append(Itemizer.ITEM_SEPARATOR)
      .append( Itemizer.longToItem(AccessLogID) )
      .toString();
    
  }  // toItemLine()
  
  /**
   * @param  pTraversal sets all attributes of this Traversal according to the
   * String pTraversal that must be a valid representation for a Traversal, 
   * i.g., generated by this.toItemLine()
   */
  
  public void fromItemLine(String pTraversal) {
  
    this.reset();
    MyItemizer.setNewItemString(pTraversal);
  
    try {   
      VisitorID = Itemizer.itemToLong( MyItemizer.getNextItem() );
      TimeStamp = Itemizer.itemToString( MyItemizer.getNextItem() );
      FromPageID = Itemizer.itemToLong( MyItemizer.getNextItem() );
      ToPageID = Itemizer.itemToLong( MyItemizer.getNextItem() );
      AccessLogID = Itemizer.itemToLong( MyItemizer.getNextItem() );
      ValidTraversal = true;
    }
    catch (NoSuchElementException e) 
      { this.handleError(pTraversal); }
    catch (NumberFormatException e) 
      { this.handleError(pTraversal); }
    catch (StringIndexOutOfBoundsException e) 
      { this.handleError(pTraversal); } 
  
  }  // fromItemLine()
  
  /**
   * resets all attributes, marks this Traversal as 'not valid' and writes the
   * given String on System.out
   * @param pTraversal Error-Message to be written on System.out
   */
  
  protected void handleError(String pTraversal) {
  
    System.out.println("Error: Line doesn't confirm to " + 
      "traversal syntax. \nLine = " + pTraversal );
    this.reset();
    ValidTraversal = false;
      
  }  // handleError()
  
  /**
   * resets all attributes, marks this Traversal as 'not valid' and writes the
   * given String on System.out
   */
  
  protected void reset() {
  
    VisitorID = 0L;   TimeStamp = "";
    FromPageID = 0L;   ToPageID = 0L;    
    AccessLogID = 0L;   ValidTraversal = true;
      
    MyItemizer.setNewItemString("");

  }  // reset()
  
}  // class Traversal
