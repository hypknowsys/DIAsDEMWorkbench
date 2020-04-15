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

/**
  * Visitors is a small database to store instances of Visitor. A Visitor is 
  * stored and retrieved using its VisitorID or its Host as primary keys. 
  * Visitors provides new Visitors with a unique ID. <p>
  *
  * Modified by kwinkler 10/1999: class completely changed
  *
  * @version 0.9.0, 20 May 2004
  * @author Karsten Winkler
  */

public class Visitors implements Serializable {

  // ########## attributes ##########
  

  private long NextID = 0L;
  private TreeMap TableOfVisitors = null;
  private TreeMap IndexOfHosts = null;
  
  private transient Iterator IDIterator = null;
  private transient Iterator HostIterator = null;
  private transient Object TmpObject = null;
  private transient Long CurrentID = null;

  // ########## constructors ##########
  
/**
 * constructs an emtpy database Visitors, first ID is 1
 */
  
  public Visitors() {

    NextID = 1L;
    TableOfVisitors = null;  IndexOfHosts = null;
    IDIterator = null;   HostIterator = null;    
    TmpObject = null;   CurrentID = null;

  }

/**
 * constructs an emtpy database Visitors, first ID is specified
 * @param pNextID ID of the first added Visitor
 */

  public Visitors(long pNextID) {

    NextID = pNextID;
    TableOfVisitors = null; IndexOfHosts = null;
    IDIterator = null;   HostIterator = null; 
    TmpObject = null;   CurrentID = null;

  }

/**
  * Creates relational database, must be called immediately after 
  * constructing instance reason: previously umsed persistence engine 
  */

  public void createDatabase() {

    TableOfVisitors = new TreeMap();  
    IndexOfHosts = new TreeMap();

  }

  // ########## mutator methods ##########

  private void setNextID(long pNextID) { NextID = pNextID; }  
  
  // ########## standard methods ##########

  public String toString() {

    return( "Visitors: NID=" + NextID + " #=" + TableOfVisitors.size() );

  }  // toString()

  /**
   * dumps all contained Visitors ordered by ID into the given PrintStream
   * @param pPrintStream PrintStream to dump 
   * @see java.io.PrintStream
   */

  public void dumpID(PrintStream pPrintStream) {

    pPrintStream.println(this);
    Visitor oVisitor = this.getFirstByID();
    while (oVisitor != null) {
      pPrintStream.println(oVisitor);
      oVisitor = this.getNextByID();
    }

  }  // dumpID()
  
  /**
   * dumps all contained Visitors ordered by URL into the given PrintStream
   * @param pPrintStream PrintStream to dump 
   * @see java.io.PrintStream
   */

  public void dumpHost(PrintStream pPrintStream) {

    pPrintStream.println(this);
    Visitor oVisitor = this.getFirstByHost();
    while (oVisitor != null) {
      pPrintStream.println(oVisitor);
      oVisitor = this.getNextByHost();
    }

  }  // dumpHost()
      
  /**
   * adds the given Visitor to the database, sets a unique VisitorID 
   * in the given Visitor
   * @param pVisitor Visitor to be added to Visitors
   * @return VisitorID of the added Visitor
   */

  public long add(Visitor pVisitor) {

    pVisitor.setID( this.getNextID() );
    CurrentID = new Long( pVisitor.getID() );
    TmpObject = TableOfVisitors.put(CurrentID, pVisitor);
    TmpObject = IndexOfHosts.put(pVisitor.getHost(), CurrentID);
    
    return CurrentID.longValue();

  }  // add()
  
  /**
   * replaces a Visitor that is allready contained in Visitors with the 
   * same AccessLogID 
   * with the given Visitor in the database, 
   * VisitorID ans URL must not be changed between get() and replace()
   * @param pVisitor Visitor to be added to Visitors
   */

  public void replace(Visitor pVisitor) {

    CurrentID = new Long( pVisitor.getID() );
    TmpObject = TableOfVisitors.put(CurrentID, pVisitor);

  }  // replace()
  
  /**
   * @param pID VisitorID of the Visitor to be returned
   * @return Visitor associated with pID or null if pID doesn't exist
   */

  public Visitor get(long pID) {
  
    TmpObject = TableOfVisitors.get( new Long(pID) );    
    if (TmpObject != null)
      return (Visitor)TmpObject;
    else 
      return null;

  }  // get()
  
  /**
   * @param pHost URL of the Visitor to be returned
   * @return Visitor associated with pHost or null if pHost doesn't exist
   */

  public Visitor get(String pHost) {
  
    CurrentID = getID(pHost);    
    if (CurrentID != null)
      return get( CurrentID.longValue() );
    else
      return null;

  }  // get()  
  
  /**
   * @param pHost URL of the Visitor whose ID is to be returned
   * @return ID of the Visitor associated with pHost or null if pHost doesn't 
   * exist
   */

  public Long getID(String pHost) {
  
    CurrentID = (Long)( IndexOfHosts.get(pHost) );    
    if (CurrentID != null)
      return CurrentID;      
    else 
      return null;

  }  // getID() 
  
  /**
   * @return first Visitor of the database ordered by ID or null if the
   * database is empty, must be called to start a new traversal
   */

  public Visitor getFirstByID() {
  
    if (TableOfVisitors == null) return null;
  
    IDIterator = TableOfVisitors.values().iterator();
    if ( IDIterator.hasNext() )      
      return (Visitor)IDIterator.next();
    else
      return null;

  }  // getFirstByID() 
  
  /**
   * @return next Visitor of the database ordered by ID or null if the
   * database is empty, subsequent calls traverse all Visitors
   */

  public Visitor getNextByID() {
  
    if (IDIterator == null) return getFirstByID();
    
    if ( IDIterator.hasNext() ) 
      return (Visitor)IDIterator.next();  
    else
      return null;

  }  // getNextByID() 
  
  /**
   * @return first Visitor of the database ordered by URL or null if the
   * database is empty, must be called to start a new traversal
   */

  public Visitor getFirstByHost() {
  
    if (IndexOfHosts == null) return null;
  
    HostIterator = IndexOfHosts.values().iterator();
    if ( HostIterator.hasNext() )      
      return this.get( ( (Long)HostIterator.next() ).longValue() );
    else
      return null;

  }  // getFirstByHost() 
  
  /**
   * @return next Visitor of the database ordered by Host or null if the
   * database is empty, subsequent calls traverse all Visitors
   */

  public Visitor getNextByHost() {
  
    if (HostIterator == null) return getFirstByHost();
    
    if ( HostIterator.hasNext() )
      return this.get( ( (Long)HostIterator.next() ).longValue() );   
    else
      return null;

  }  // getNextByHost()   
    
  /**
   * deletes the Visitor associated with pID from the database,
   * @param pID VisitorID of the Visitor to be deleted
   */

  public void delete(long pID) {

    TmpObject = TableOfVisitors.remove( new Long(pID) );
    
    if (TmpObject != null) {
      Visitor oVisitor = (Visitor)TmpObject;    
      TmpObject = IndexOfHosts.remove( oVisitor.getHost() );
    }
      
  }  // delete()
  
  /**
   * resets all attributes, database is completely deleted, next ID is 1
   */

  public void reset() {

    NextID = 1L;
    TableOfVisitors.clear();   IndexOfHosts.clear();
    IDIterator = null;   HostIterator = null;
    TmpObject = null;   CurrentID = null;

  }  // reset()
  
  /**
   * @return the number of Visitors contained in the database
   */

  public int countAll() { return TableOfVisitors.size(); } 
  
  /**
   * @return the next unique VisitorID for this database
   */

  private long getNextID() { return NextID++; } 

}  // class Visitors
