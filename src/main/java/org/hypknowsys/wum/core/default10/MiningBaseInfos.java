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
 * MiningBaseInfos is a small repository to store instances of MiningBaseInfo 
 * in a HashMap. An instance of the class MiningBaseInfo is stored and 
 * retrieved using its ID as a primary key. MiningBaseInfos provides new 
 * instances of the class MiningBaseInfo with a unique ID.
 * 
 * @version 0.9.0, 20 May 2004
 * @author Karsten Winkler
 */

public class MiningBaseInfos implements Serializable {

  // ########## attributes ##########

  private long NextID = 0L;
  private HashMap TableOfMiningBaseInfos = null;

  // ########## constructors ##########

/**
 * constructs an emtpy repository MiningBaseInfos, first ID is 1
 */

  public MiningBaseInfos() {

    NextID = 1L;
    TableOfMiningBaseInfos = new HashMap();

  }

/**
 * constructs an emtpy repository MiningBaseInfos, first ID is specified
 * @param pNextID ID of the first added MiningBaseInfo
 */

  public MiningBaseInfos(long pNextID) {

    NextID = pNextID;
    TableOfMiningBaseInfos = new HashMap();

  }

  // ########## mutator methods ##########

  public void setNextID(long pNextID) { NextID = pNextID; }
    
  // ########## standard methods ##########

  public String toString() {

    String result = "MiningBaseInfos: NID=" + NextID + " #=" + 
      TableOfMiningBaseInfos.size() + " HM=" + TableOfMiningBaseInfos + "\n";
    MiningBaseInfo[] allMiningBaseInfos = this.getAllMiningBaseInfos();
    if (allMiningBaseInfos == null)
      return result;
    for (int i = 0; i < allMiningBaseInfos.length; i++)
      result += allMiningBaseInfos[i].toString();

    return result;

  }  // toString()
  
  /**
   * adds the given MiningBaseInfo to the repository, sets a unique 
   * AccessLogID in the given AccessLog
   * @param pMiningBaseInfo MiningBaseInfo to be added to repository
   * @return new ID of the MiningBaseInfo to be inserted
   */

  public long add(MiningBaseInfo pMiningBaseInfo) {
    
    pMiningBaseInfo.setID( this.getNextID() );
    Object object = TableOfMiningBaseInfos.put( 
      new Long( pMiningBaseInfo.getID() ), pMiningBaseInfo );

    return new Long( pMiningBaseInfo.getID() ).longValue();

  }  // add()
  
  /**
   * @param pID ID of the MiningBaseInfo to be returned
   * @return MiningBaseInfo associated with pID or null if pID doesn't exist
   */

  public MiningBaseInfo get(long pID) {
  
    Object object = TableOfMiningBaseInfos.get( new Long(pID) );
    
    if (object != null)
      return (MiningBaseInfo)object;
    else 
      return null;

  }  // get()
  
  /**
   * deletes the MiningBaseInfo associated with pID from the repository,
   * @param pID ID of the MiningBaseInfo to be deleted
   */

  public void delete(long pID) {

    Object object = TableOfMiningBaseInfos.remove( new Long(pID) );
      
  }  // delete()
  
  /**
   * resets all attributes, next ID is 1
   */

  public void reset() {

    NextID = 1L;
    TableOfMiningBaseInfos = new HashMap();

  }  // reset()
  
  /**
   * @return IDs of all MiningBaseInfos contained in the repository as an 
   * array of long or null if the repository is empty
   */
  
  public long[] getAllIDs() {
  
    if ( TableOfMiningBaseInfos.isEmpty() ) return null;
      
    long[] result = new long[ TableOfMiningBaseInfos.size() ];
    int counter = 0;
    
    Iterator iterator = TableOfMiningBaseInfos.keySet().iterator(); 
    while ( iterator.hasNext() )
      result[counter++] = ( (Long)iterator.next() ).longValue();
      
    return result;
    
  }  // getAllIDs()
  
  /**
   * @return all MiningBaseInfos contained in the repository as an array of 
   * MiningBaseInfo or null if the repository is empty
   */
  
  public MiningBaseInfo[] getAllMiningBaseInfos() {
  
    if ( TableOfMiningBaseInfos.isEmpty() ) 
      return null;

    MiningBaseInfo[] result = 
      new MiningBaseInfo[ TableOfMiningBaseInfos.size() ];
    int counter = 0;
    
    Iterator iterator = TableOfMiningBaseInfos.keySet().iterator(); 
    while ( iterator.hasNext() )
      result[counter++] = (MiningBaseInfo)iterator.next();

    return result;
    
  }  // getAllMiningBaseInfos()  
  
  /**
   * @return the number of MiningBaseInfos contained in the repository
   */

  public int countAll() { return TableOfMiningBaseInfos.size(); } 
  
  /**
   * @return the next unique ID for this repository
   */

  private long getNextID() { return NextID++; } 
  
}  // class MiningBaseInfos

