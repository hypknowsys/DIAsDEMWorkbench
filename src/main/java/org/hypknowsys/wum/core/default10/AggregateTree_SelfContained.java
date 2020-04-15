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
 * AggregateTree_SelfContained is a trie structure containing Observations, 
 * defined by Myra Spiliopoulou and Lukas Faulstich. Used to visualize 
 * AggregateTree_SelfContained without overhead code. Its nodes are 
 * ObservationNode_SelfContaineds. An Aggregate Tree is implemented similar 
 * to hplb.util.Trie, Author: Anders Kristensen. An AggregateTree_SelfContained
 * keeps information about its root support (equal to the number of Trails 
 * added to the AggregateTree_SelfContained), about the number of Observations
 * it contains and about the size of the longest Observation (MaxLevel). 
 * A new AggregateTree_SelfContained can either be  constructed by 
 * subsequently adding Trails or by using a push/pop-mechanism on a traversal
 * stack of ObservationNode_SelfContaineds. 
 * 
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class AggregateTree_SelfContained extends ObservationNode_SelfContained
  implements Serializable {

  // ########## attributes ##########
 
  private int CounterObservations = 0;
  private int MaxLevel = 0;  
 
  // ########## constructors ##########
  
/**
 * constructs an empty AggregateTree_SelfContained, its traversal 
 * stack contains the root
 */  

  public AggregateTree_SelfContained() {

    super();   
    
  }  
  
  // ########## mutator methods ##########
  
  public void setCounterObservations(int pCounterObservations) 
    { CounterObservations = pCounterObservations; }
  public void setRootSupport(long pRootSupport) 
    { Support = pRootSupport; }
  public void setMaxLevel(int pMaxLevel) 
    { MaxLevel = pMaxLevel; }
  
  // ########## accessor methods ##########
  
  public int getMaxLevel() 
    { return MaxLevel; }
  public ObservationNode_SelfContained getRoot() 
    { return (ObservationNode_SelfContained)this; }
  public long getRootSupport() 
    { return Support; }
  public long getRootCountChildren() 
    { return CounterChildren; }
  public int countObservations() 
    { return CounterObservations; }
  public boolean isEmpty() 
    { return (this.countObservations() == 0); }
  
  // ########## comments will follow ##########

  public synchronized ObservationNode_SelfContained[] addObservationNew(
    ObservationNode_SelfContained[] pObservation) {

    // RootSupport, MaxLevel, CounterObservations must be set by 
    // mutator methods
    if (pObservation.length > MaxLevel) MaxLevel = pObservation.length;
    return super.addObservationNew(pObservation, 0);

  }  // addObservationPart()
    
  /**
   * resets CurrentChild of all contained ObservationNode_SelfContaineds 
   * to zero, is called by in getFirst() to ensure a complete traversal of 
   * the AggregateTree_SelfContained
   */

  public void initCurrentChild() {

    ObservationNode_SelfContained[] traversalArray = 
      new ObservationNode_SelfContained[MaxLevel];
    super.initCurrentChild(traversalArray, 0);
   
  }  // initCurrentChild()

  /**
   * @return first Observation of the AggregateTree_SelfContained or null 
   * if the AggregateTree_SelfContained is empty, must be called to start 
   * a new 
   * traversal
   */

  public synchronized ObservationNode_SelfContained[] getFirst() {
  
    this.initCurrentChild();    
    ObservationNode_SelfContained[] traversalArray = 
      new ObservationNode_SelfContained[MaxLevel];
    
    return ( super.getFirst(traversalArray, 0) );

  }  // getFirst()

  
  /**
   * @return next Observation of the AggregateTree_SelfContained or null if the
   * AggregateTree_SelfContained is empty, subsequent calls traverse the 
   * AggregateTree_SelfContained
   */

  public synchronized ObservationNode_SelfContained[] getNext() {
  
    ObservationNode_SelfContained[] traversalArray = 
      new ObservationNode_SelfContained[MaxLevel];
    
    return ( super.getNext(traversalArray, 0) );

  }  // getNext()

  // ########## comments will follow ########## 

  
}  // class AggregateTree_SelfContained
