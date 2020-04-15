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
import java.util.*;

/**
 * A MintVariable corresponds to one variable name in the MINT query's 
 * from-clause. A MintVariable contains its Name and an Array of content 
 * predidicates allocated to this MintVariable. <p>
 *
 * Modified by tveit 5/1999: various modifications, removed by kwinkler
 * 11/2001 <p>
 *
 * @version 0.7.0, 17 Nov 2001
 * @author Karsten Winkler
 */

public class MintVariable implements Serializable {

  // ########## attributes ##########

  private String Name = null;
  private Vector ContentPredicates = null;
  private transient Iterator ContentPredicatesIterator = null;

  // ######## attributes by tveit #########
  private PageOccurrence[] PgOccurrence = null;

  // ######## attributes by kwinkler 11/99 #########
  private boolean vMatch = false;
  private Page oPage = null;

    // ########## constructors ##########
  
/**
 * constructs an empty MintVariable
 */   

  public MintVariable() {

    Name = "";
    ContentPredicates = new Vector();   
    ContentPredicatesIterator = null;
    
  }  
  
/**
 * constructs an MintVariable with the given name
 * @param pName the variable's name
 */  

  public MintVariable(String pName) {

    Name = pName;
    ContentPredicates = new Vector();   
    ContentPredicatesIterator = null;
    
  }
  
  // ########## mutator methods ##########

  public void setName(String pName) { Name = pName; }

  // ########## accessor methods ##########
  
  public String getName() { return Name; }

  // ########## standard methods ##########
  
  public String toString() {
  
    String vResult = "MintVariable: " + Name + " ";
    
    ContentPredicatesIterator = ContentPredicates.iterator();
    while ( ContentPredicatesIterator.hasNext() )
      vResult += "\n   " + 
        ( (MintPredicate)ContentPredicatesIterator.next() ).toString();
    ContentPredicatesIterator = null;
      
    return vResult;
  
  }  // toString()
  
  /**
   * adds the given MintPredicate to the MintTemplate's Array ContentPredicates
   * @param pContentPredicate ContentPredicate to add
   */
  
  public void addContentPredicate(MintPredicate pContentPredicate) {
  
    ContentPredicates.add(pContentPredicate);
  
  }  

  /**
   * @return the number of content predicates contained in Array
   * ContentPredicates
   */
  
  public int countContentPredicates() { 
  
    if (ContentPredicates == null) 
      return 0;
    else
      return ContentPredicates.size(); 
      
  }  // countContentPredicates()


  // ######## tveit #########

  public MintPredicate getContentPredicate(int pPredicateID) {

    if ( (pPredicateID >= 0) && 
      ( pPredicateID < this.countContentPredicates() ) ) 
      return (MintPredicate)ContentPredicates.elementAt(pPredicateID);
    else 
      return null;

  }


  /**
   * tests whether this MintVariable (better: its content predicates) 
   * is matched by the given PageOccurrence;
   * @param pWUM WUM context for the WUM query
   * @param pPageOccurrence PageOccurrence that is to be tested for match 
   * with this MintVariable
   * @return true if pPageOccurrence match this MintVariable or false if there
   * is no match 
   */
  
  public boolean isMatchedByPageOccurrence(MiningBase pMiningBase, 
    PageOccurrence pPageOccurrence) {
   
    if ( (ContentPredicates == null) || (ContentPredicates.size() == 0) ) 
      return true;  // no predicate = all nodes match

    vMatch = false;    
    oPage = pMiningBase.getPage( pPageOccurrence.getPageID() );
    if (oPage == null) return false;
   
    // predicates = all predicates must be matched by node
    for (int i = 0; i < ContentPredicates.size(); i++) {
      vMatch = ( (MintPredicate)ContentPredicates.elementAt(i) ).
        isMatchedByPage(oPage, pPageOccurrence.getOccurrence() );
      if ( !vMatch ) return false;              
      }
    
    return vMatch;
  
  }  // isMatchedByObservationNode()
  
}  // class MintVariable
