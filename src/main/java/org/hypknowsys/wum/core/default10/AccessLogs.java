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
 * AccessLogs is a small database to store instances of AccessLog in a
 * HashMap. An AccessLog is stored and retrieved using its AccessLogID
 * as a primary key. AccessLogs provides new AccessLogs with a unique ID.
 * The first and the last timestamp of all contained AccessLog is stored to
 * check the import of new access log files into the WUM mining base.
 *
 * @version 0.9.0, 20 May 2004
 * @author Karsten Winkler
 */

public class AccessLogs implements Serializable {

  // ########## attributes ##########

  private String FirstTimeStamp = null;
  private String LastTimeStamp = null;
  private long NextID = 0L;
  private HashMap TableOfAccessLogs = null;  
  private transient Object CurrentObject = null;
  private transient Long CurrentID = null;

  // ########## constructors ##########
  
/**
 * constructs an emtpy database AccessLogs, first ID is 1
 */

  public AccessLogs() {

    FirstTimeStamp = "";   LastTimeStamp = "";   NextID = 1L;
    TableOfAccessLogs = new HashMap();    
    CurrentObject = null;   CurrentID = null;       

  }

/**
 * constructs an emtpy database AccessLogs, first ID is specified
 * @param pNextID ID of the first added AccessLog
 */

  public AccessLogs(long pNextID) {

    FirstTimeStamp = "";   LastTimeStamp = "";   NextID = pNextID;
    TableOfAccessLogs = new HashMap();    
    CurrentObject = null;   CurrentID = null;   

  }

  // ########## mutator methods ##########

  public void setFirstTimeStamp(String pFirstTimeStamp) 
    { FirstTimeStamp = pFirstTimeStamp; }
  public void setLastTimeStamp(String pLastTimeStamp) 
    {  LastTimeStamp = pLastTimeStamp; }

  // ########## accessor methods ##########

  public String getFirstTimeStamp() { return FirstTimeStamp; }
  public String getLastTimeStamp() { return LastTimeStamp; }

  // ########## standard methods ##########

  public String toString() {

    return( "AccessLogs: FTS=" + FirstTimeStamp + " LTS=" + LastTimeStamp + 
      " NID=" + NextID + " #=" + TableOfAccessLogs.size() );

  }  // toString()
  
  /**
   * dumps all contained AccessLogs into the given PrintStream
   * @param pPrintStream PrintStream to dump 
   * @see java.io.PrintStream
   */

  public void dump(PrintStream pPrintStream) {

    pPrintStream.println( this.toString() );
    AccessLog[] accessLogs = this.getAllAccessLogs();
    if ( accessLogs != null)
      for (int i = 0; i < accessLogs.length; i++) 
        pPrintStream.println(accessLogs[i]);

  }  // dump()  
  
  /**
   * adds the given AccessLog to the database, recomputes FirstTimeStamp
   * and LastTimeStamp, sets a unique AccessLogID in the given AccessLog
   * @param pAccessLog AccessLog to be added to AccessLogs
   * @return AccessLogID of the added AccessLog
   */

  public long add(AccessLog pAccessLog) {

    if ( TableOfAccessLogs.isEmpty() ) {
      FirstTimeStamp = pAccessLog.getFirstTimeStamp();
      LastTimeStamp = pAccessLog.getLastTimeStamp();
    }
    else {
    
      if ( FirstTimeStamp.equals("") ) 
        FirstTimeStamp = pAccessLog.getFirstTimeStamp();
      else
        if ( ! ( pAccessLog.getFirstTimeStamp().equals("") ) &&
          ( pAccessLog.getFirstTimeStamp().compareTo(FirstTimeStamp) < 0) )
          FirstTimeStamp = pAccessLog.getFirstTimeStamp();
          
    if ( LastTimeStamp.equals("") ) 
      LastTimeStamp = pAccessLog.getLastTimeStamp();  
    else
      if ( ! ( pAccessLog.getLastTimeStamp().equals("") ) &&
        ( pAccessLog.getLastTimeStamp().compareTo(LastTimeStamp) > 0) )
        LastTimeStamp = pAccessLog.getLastTimeStamp();  
        
    }

    pAccessLog.setID( this.getNextID() );
    CurrentObject = TableOfAccessLogs.put( 
      new Long( pAccessLog.getID() ), pAccessLog );
      
    return pAccessLog.getID();

  }  // add()
  
  /**
   * replaces the contained AccessLog with the same AccessLogID with the given
   * AccessLog in the database, recomputes FirstTimeStamp and LastTimeStamp, 
   * AccessLogID must not be changed between get() and replace()
   * @param pAccessLog AccessLog to be added to AccessLogs
   */

  public void replace(AccessLog pAccessLog) {

    if ( FirstTimeStamp.equals("") ) 
      FirstTimeStamp = pAccessLog.getFirstTimeStamp();  
    else
      if ( ! ( pAccessLog.getFirstTimeStamp().equals("") ) &&
        ( pAccessLog.getFirstTimeStamp().compareTo(FirstTimeStamp) < 0) )
        FirstTimeStamp = pAccessLog.getFirstTimeStamp();
        
    if ( LastTimeStamp.equals("") ) 
      LastTimeStamp = pAccessLog.getLastTimeStamp();  
    else
      if ( ! ( pAccessLog.getLastTimeStamp().equals("") ) &&
        ( pAccessLog.getLastTimeStamp().compareTo(LastTimeStamp) > 0) )
        LastTimeStamp = pAccessLog.getLastTimeStamp();  

    CurrentID = new Long( pAccessLog.getID() );
    CurrentObject = TableOfAccessLogs.put(CurrentID, pAccessLog);

  }  // replace()
  
  
  /**
   * @param pID AccessLogID of the AccessLog to be returned
   * @return AccessLog associated with pID or null if pID doesn't exist
   */

  public AccessLog get(long pID) {
  
    CurrentObject = TableOfAccessLogs.get( new Long(pID) );    
    if (CurrentObject != null)
      return (AccessLog)CurrentObject;
    else 
      return null;

  }  // get()
  
  /**
   * deletes the AccessLog associated with pID from the database,
   * recomputes FirstTimeStamp and LastTimeStamp
   * @param pID AccessLogID of the AccessLog to be deleted
   */

  public void delete(long pID) {

    CurrentObject = TableOfAccessLogs.remove( new Long(pID) );      
    if (CurrentObject != null) {
      if ( ( (AccessLog)CurrentObject ).
        getFirstTimeStamp().equals(FirstTimeStamp) )
        this.determineFirstTimeStamp();
      if ( ( (AccessLog)CurrentObject ).
        getLastTimeStamp().equals(LastTimeStamp) )
        this.determineLastTimeStamp();
    }      

  }  // delete()
  
  /**
   * resets all attributes, next ID is 1
   */

  public void reset() {

    FirstTimeStamp = "";   LastTimeStamp = "";   NextID = 1L;
    TableOfAccessLogs = new HashMap();    
    CurrentObject = null;   CurrentID = null;   

  }  // reset()
  
  /**
   * @return AccessLogIDs of all AccessLogs contained in the database as an 
   * array of long or null if the database is empty
   */
  
  public long[] getAllIDs() {
  
    if ( TableOfAccessLogs.isEmpty() ) return null;
      
    long[] result = new long[ TableOfAccessLogs.size() ];
    int counter = 0;    
    Iterator iterator = TableOfAccessLogs.keySet().iterator(); 
    while ( iterator.hasNext() )
      result[counter++] = ( (Integer)iterator.next() ).longValue();
      
    return result;
    
  }  // getAllIDs()
  
  /**
   * @return all AccessLogs contained in the database as an array of AccessLog 
   * or null if the database is empty
   */
  
  public AccessLog[] getAllAccessLogs() {
  
    if ( TableOfAccessLogs.isEmpty() ) return null;
      
    AccessLog[] result = new AccessLog[ TableOfAccessLogs.size() ];
    int counter = 0;    
    Iterator iterator = TableOfAccessLogs.values().iterator(); 
    while ( iterator.hasNext() )
      result[counter++] = (AccessLog)iterator.next();
      
    return result;
    
  }  // getAllAccessLogs()  
  
  /**
   * @return the number of AccessLogs contained in the database
   */

  public int countAll() { return TableOfAccessLogs.size(); } 
    
  /**
   * @return the next unique AccessLogID for this database
   */

  private long getNextID() { return NextID++; } 
  
  /**
   * determines the first timestamp of all AccessLogs contained in the 
   * database, resets FirstTimeStamp
   */

  private void determineFirstTimeStamp() {

    if ( TableOfAccessLogs.isEmpty() ) {
      FirstTimeStamp = "";
      return;
    }
    else
      FirstTimeStamp = "9999/99/99/99:99:99";
      
    AccessLog accessLog = null;    
    Iterator iterator = TableOfAccessLogs.values().iterator();
    while ( iterator.hasNext() ) {
      accessLog = (AccessLog)iterator.next();
      if ( accessLog.getFirstTimeStamp().compareTo(FirstTimeStamp) < 0 )
        FirstTimeStamp = accessLog.getFirstTimeStamp();
    }
    
  }  // determineFirstTimeStamp()
  
  /**
   * determines the last timestamp of all AccessLogs contained in the 
   * database, resets FirstTimeStamp
   */

  private void determineLastTimeStamp() {

    if ( TableOfAccessLogs.isEmpty() ) {
      LastTimeStamp = "";
      return;
    }
    else
      LastTimeStamp = "0000/00/00/00:00:00";
      
    AccessLog accessLog = null;    
    Iterator iterator = TableOfAccessLogs.values().iterator();
    while ( iterator.hasNext() ) {
      accessLog = (AccessLog)iterator.next();
      if ( accessLog.getLastTimeStamp().compareTo(LastTimeStamp) > 0 )
        LastTimeStamp = accessLog.getLastTimeStamp();
    }

  }  // determineLastTimeStamp()
    
}  // class AccessLogs
