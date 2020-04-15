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
 * An Observation is one brach of the AggregateTree. It's implemented
 * as a sequence of ObservationNodes. 
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class Observation implements Serializable {

  // ########## attributes ##########
 
  private Vector oObservation = null; 
  private Iterator ObservationIterator = null;
  
  // ########## constructors ##########

/**
 * constructs an empty Observation
 */   

  public Observation() {
  
    oObservation = null;   ObservationIterator = null;
    
  }  
  
/**
 * constructs an Observation containing pObservationNodes empty
 * ObservationNodes
 * @param pObservationNodes number of ObservationNodes that new 
 * Observation contains
 */   

  public Observation(int pObservationNodes) { 
  
    oObservation = new Vector();   ObservationIterator = null;
    
    for (int i = 0; i < pObservationNodes; i++)
      oObservation.add( new ObservationNode() );     
    
  }    
  
  // ########## mutator methods ##########
  
  public void setObservation(Vector pObservation) 
    { oObservation = pObservation; }
    
  // ########## accessor methods ##########  
  
  public Vector getObservation() 
    { return oObservation; }
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    String result = "Observation: Size=" + this.getSize();
    
    for (int i = 0; i < this.getSize(); i++) 
      result +=  " " + 
        ( (ObservationNode)oObservation.elementAt(i) ).toString();
            
    return result;
    
  }  // toString()
  
  
  
  /**
   * @return the number of ObservationNodes contained in this Observation
   */
  
  public int getSize() { 
  
    if (oObservation == null) 
      return 0;
    else
      return oObservation.size(); 
      
  }  // getSize()
  
  /**
   * returns the ObservationNode with the given pIndex
   * @param pIndex 0 <= pIndex < this.getSize()
   * @return corresponding ObservationNode or null if pIndex is 
   * out of valid range
   */  
  
  public ObservationNode getObservationNodeAt(int pIndex) { 
  
    if ( (pIndex >= 0) && ( pIndex < oObservation.size() ) ) 
      return (ObservationNode)oObservation.elementAt(pIndex);
    else
      return null; 
      
  }  // getObservationAt()  

  /**
   * sets the ObservationNode given by pIndex
   * @param pIndex 0 <= pIndex < this.getSize()
   * @param pObservationNode pObservationNode to set
   */  
  
  public void setObservationNodeAt(int pIndex, 
    ObservationNode pObservationNode) { 
  
    if ( (pIndex >= 0) && ( pIndex < oObservation.size() ) ) 
      oObservation.setElementAt(pObservationNode, pIndex);
      
  }  // setObservationAt()    
  
  /**
   * return Observation as an array of ObservationNodes, the order of
   * ObservationNodes is preserved: array[0] contains the first node in 
   * Observation
   */  
 
  public ObservationNode[] getArray() {
  
    ObservationNode[] aObservation = 
      new ObservationNode[ oObservation.size() ];
    for (int i = 0; i < aObservation.length; i++)
      aObservation[i] = (ObservationNode)oObservation.elementAt(i);
      
    return aObservation;
    
  }  // getArray()
  
  /**
   * @return first ObservationNode of the Observation or null if the
   * Observation is empty, must be called to start a new traversal
   */

  public ObservationNode getFirstObservationNode() {
  
    if (oObservation == null) return null;
  
    ObservationIterator = oObservation.iterator();
    if ( ObservationIterator.hasNext() )      
      return (ObservationNode)ObservationIterator.next();     
    else
      return null;

  }  // getFirstObservationNode() 
  
  /**
   * @return next ObservationNode of the Observation or null if the
   * Observation is empty, subsequent calls traverse the Observation
   */

  public ObservationNode getNextObservationNode() {
  
    if (ObservationIterator == null) return getFirstObservationNode();
    
    if ( ObservationIterator.hasNext() ) 
      return (ObservationNode)ObservationIterator.next();     
    else
      return null;

  }  // getNextObservationNode()
  
  /**
   * resets the Observation and adds the first ObservationNode
   * @param pObservationNode ObservationNode to add
   */  
  
  public void setFirstObservationNode(ObservationNode pObservationNode) {
  
    oObservation = new Vector();
    oObservation.add(pObservationNode);
  
  }  // setFirstObservationNode()
  
  /**
   * adds the given ObservationNode at the Observation's tail
   * @param pObservationNode ObservationNode to add
   */  
  
  public void setNextObservationNode(ObservationNode pObservationNode) {
  
    oObservation.add(pObservationNode);
  
  }  // setNextObservationNode()  
  
}  // class Observation
