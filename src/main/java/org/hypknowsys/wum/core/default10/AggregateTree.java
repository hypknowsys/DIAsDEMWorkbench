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
 * AggregateTree is a trie structure containing Observations, defined
 * by Myra Spiliopoulou and Lukas Faulstich. Its nodes are ObservationNodes.
 * An Aggregate Tree is implemented similar to hplb.util.Trie, Author: 
 * Anders Kristensen. An AggregateTree keeps information about its root
 * support (equal to the number of Trails added to the AggregateTree),
 * about the number of Observations it contains and about the size of
 * the longest Observation (MaxLevel). A new AggregateTree can either be 
 * constructed by subsequently adding Trails or by using a push/pop-
 * mechanism on a traversal stack of ObservationNodes. 
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class AggregateTree extends ObservationNode 
  implements Serializable, Cloneable {

  // ########## attributes ##########
 
  private int CounterObservations = 0;
  private int MaxLevel = 0;  
  private transient Stack TraversalStack = null;
  private transient Stack CurrentChildStack = null;
  private transient int CurrentLevel = 0;
  private transient Object CurrentObject = null;
 
  // ########## constructors ##########
  
/**
 * constructs an empty AggregateTree, its traversal stack contains the root
 */  

  public AggregateTree() {

    super();   
    
    TraversalStack = new Stack();   
    pushObservationNode( (ObservationNode)this );
    CurrentChildStack = new Stack();   pushStackCurrentChild(0);   
    CurrentLevel = 0;   CurrentObject = null;   CounterObservations = 0;
    
  }  
  
  // ########## accessor methods ##########
  
  public int getMaxLevel() 
    { return MaxLevel; }
  public ObservationNode getRoot() 
    { return (ObservationNode)this; }
  public long getRootSupport() 
    { return Support; }
  public long getRootCountChildren() 
    { return CounterChildren; }
  public int getTraversalStackSize() 
    { return TraversalStack.size(); }
  public int countObservations() 
    { return CounterObservations; }
  public boolean isEmpty() 
    { return (this.countObservations() == 0); }

  // ########## standard methods ##########

  public String toString() { 
  
    return "AggTree: " + this.hashCode() + ",  " + super.toString();
            
  }  // toString()

  public Object clone() { 
   
    // AggregateTree oClone = new AggregateTree();
    // ObservationNode[] aTraversalArray = new ObservationNode[MaxLevel];
    // super.clone(aTraversalArray, 0);

    return super.clone();

  }  // clone() 
    
  /**
   * adds a Trail to the AggregateTree by recomputing the support value
   * of the aquivalent Observation or by (partly) creating a new Observation
   * in the AggregateTree
   * @param pTrail Trail to be added to the AggregateTree
   */
   
  public synchronized void addTrail(Trail pTrail) {
  
    if (pTrail.getSize() > MaxLevel) MaxLevel = pTrail.getSize();      
      CounterObservations += super.addTrail(pTrail, 0);
    
  }  // addTrail() 

  /**
   * reverses the Trail and adds it to the AggregateTree by recomputing 
   * the support value
   * of the aquivalent Observation or by (partly) creating a new Observation
   * in the AggregateTree
   * @param pTrail Trail to be reversed and added to the AggregateTree
   */
   
  public synchronized void addReverseTrail(Trail pTrail) {
  
    if (pTrail.getSize() > MaxLevel) MaxLevel = pTrail.getSize();      
      CounterObservations += super.addReverseTrail(pTrail, 0);
    
  }  // addReverseTrail() 

  /**
   * looks up the ObservationNode that is rooted at the last
   * PageOccurrence of pTrail; KDD Cup 2000: killer pages and brands viewed
   * @param pTrail start of unknown trail
   * @return null, if there is no complete matching in the AggregateTree
   */
   
  public synchronized ObservationNode getSubtree(Trail pTrail) {
        
    return super.getSubtree(pTrail, 0);
    
  }  // getSubtree() 

  /**
   * resets CurrentChild of all contained ObservationNodes to zero, is called 
   * by in getFirst() to ensure a complete traversal of the AggregateTree
   */

  public void initCurrentChild() {

   ObservationNode[] traversalArray = new ObservationNode[MaxLevel];
   super.initCurrentChild(traversalArray, 0);
   
  }  // initCurrentChild()    

  /**
   * sets Support of all contained ObservationNodes to zero, is used
   * to build navigation patterns corresponding to GSM
   */

  public void initSupport() {

   ObservationNode[] traversalArray = new ObservationNode[MaxLevel];
   super.initSupport(traversalArray, 0);
   
  }  // initSupport()    

  
  /**
   * @return first Observation of the AggregateTree or null if the
   * AggregateTree is empty, must be called to start a new traversal
   */

  public synchronized Observation getFirst() {
  
    this.initCurrentChild();    
    ObservationNode[] traversalArray = new ObservationNode[MaxLevel];
    Observation observation = new Observation();    
    
    return ( super.getFirst(traversalArray, 0, observation) );
    
  }  // getFirst()  
  
  /**
   * @return next Observation of the AggregateTree or null if the
   * AggregateTree is empty, subsequent calls traverse the AggregateTree
   */

  public synchronized Observation getNext() {
  
    ObservationNode[] traversalArray = new ObservationNode[MaxLevel];
    Observation observation = new Observation();    
    
    return ( super.getNext(traversalArray, 0, observation) );
    
  }  // getNext() 
  
  /**
   * used to create a new AggregateTree from an existing one, the calling class
   * must completely manage a push/pop-mechanism on the AggregateTree's 
   * traversal  stack: if the current ObservationNode on top of the traversal
   * stack contains a child equal to the given PageOccurrence, this child's
   * support value will be increased otherwise a new child is added to the 
   * current ObservationNode on top, pushes a new ObservationNode on top of 
   * traversal stack with CurrentChild = 0
   * @param pPageOccurrence PageOccurrence to push
   * @param pSupport support value to set/add in the ObservationNode
   * @param pVariableName "" or the variable name according to a MINT query if
   * PageOccurrence represent an instance of a query variable, used for 
   * visualization
   */

  public void pushNewObservationNode(PageOccurrence pPageOccurrence,
    long pSupport, String pVariableName ) {

    ObservationNode currentObservationNode = this.getObservationNode();  
    
    if ( currentObservationNode.containsChild(pPageOccurrence) ) {  
    
      // replace observation node
      ( currentObservationNode.getChild(pPageOccurrence) ).
        increaseSupport(pSupport);
      if (CurrentLevel++ == 1) this.increaseSupport(pSupport);      
      this.pushObservationNode( currentObservationNode.getChild(
        pPageOccurrence) );
      this.pushStackCurrentChild( 
        ( currentObservationNode.getChild(pPageOccurrence) ).countChildren() );
    
    }
    else {  
      
      // add new observation node
      ObservationNode newObservationNode = new ObservationNode(
        pPageOccurrence, pSupport, CurrentLevel++);
      newObservationNode.setVariableName(pVariableName);          
      
      if (CurrentLevel > MaxLevel) MaxLevel = CurrentLevel; 
      if (CurrentLevel == 1) this.increaseSupport(pSupport);           
      if ( (this.getStackCurrentChild() > 0 ) || (CurrentLevel == 1) )
        CounterObservations++;
        
      currentObservationNode.addChild(pPageOccurrence, newObservationNode);  
      this.incrementStackCurrentChild();
      this.pushObservationNode(newObservationNode);
      this.pushStackCurrentChild(0);     
      
    }
    
  }  // pushNewObservationNode()

  /**
   * pops the current ObservationNode from the top of the AggregateTree's
   * traversal stack
   */
  
  public void popCurrentObservationNode() {
  
    CurrentLevel--;
    popObservationNode();
    popStackCurrentChild();  
    
  }  // popCurrentObservationNode()
  
  /**
   * pops all ObservationNodes except the root plus the given number of 
   * ObservationNodes from the top of the AggregateTree's traversal stack
   * example: popAllObservationNodesButRootPlus(1) leaves the root and the
   * next ObservationNode in the traversal stack
   */
  
  public void popAllObservationNodesButRootPlus(int pKeepObservationNodes) {
  
    for (int i = CurrentLevel; i > pKeepObservationNodes; i--) {
      CurrentLevel--;
      popObservationNode();
      popStackCurrentChild();  
    }
    
  }  // popAllObservationNodesButRoot()
  
  /**
   * creates a String representation of the AggregateTree, used for debugging
   */

  public synchronized String treeToString() {
  
    String result = "[" + oPageOccurrence.getPageID() + ";" + 
      oPageOccurrence.getOccurrence() + ";" + Support + "]" + 
      System.getProperty("line.separator");

    ObservationNode observationNode = null;
    Observation observation = this.getFirst();
    while (observation != null) {
      result += observation.toString() + System.getProperty("line.separator");
      observation = this.getNext();
    }
        
    return result;
    
  }  // treeToString()
  
  // ########## comments will follow ##########

  /**
   * dumps the AggregateTree to the given PrintStream, used for debugging
   */
   
  public synchronized void print(PrintStream out) {
  
    out.println("{");
    ObservationNode[] traversalArray = new ObservationNode[MaxLevel];
    super.print(out, traversalArray, 0);
    out.println("}");
    
  }  // print()  
  
  // ########## traversal methods ##########    
  
  // Hint: There are 2 stacks used to build an AggregateTree. A stack of 
  // ObservationNodes contains the AggregateTree's node and a stack of 
  // Integers contains the CurrentChild of the ObservationNodes. Both stacks 
  // must always have the same size: The top Integer on the CurrentChildStacks
  //  represents the CurrentChildID of the top ObservationNode on the other 
  // stack.
  
  private int getStackCurrentChild() {
  
    if ( CurrentChildStack.isEmpty() )
      return -1;
    else
      return ( (Integer)CurrentChildStack.peek() ).intValue();
  
  }  // getStackCurrentChild()

  private int incrementStackCurrentChild() {
  
    if ( CurrentChildStack.isEmpty() )
      return -1;
    else {      
      int vNewCurrentChild = 
        ( (Integer)CurrentChildStack.pop() ).intValue() + 1;
      CurrentChildStack.push( new Integer(vNewCurrentChild) );
      return vNewCurrentChild;
    }
  
  }  // incrementStackCurrentChild    
  
  private void pushStackCurrentChild(int pCurrentChild) {
  
    CurrentChildStack.push( new Integer(pCurrentChild) );
    
  }  // pushStackCurrentChild  

  private void popStackCurrentChild() {
  
    if ( ! CurrentChildStack.isEmpty() ) 
      CurrentObject = CurrentChildStack.pop();
    
  }  // popStackCurrentChild    
  
  // ########## comments will follow ##########    
  
  private ObservationNode getObservationNode() {

    if ( TraversalStack.isEmpty() ) 
      return null;
    else
      return ( (ObservationNode)TraversalStack.peek() );
  
  }  // getObservationNode()

  private void pushObservationNode(ObservationNode pObservationNode) {
  
    TraversalStack.push(pObservationNode);
    
  }  // pushObservationNode()
  
  private void popObservationNode() {
  
    if ( ! TraversalStack.isEmpty() ) 
      CurrentObject = TraversalStack.pop();
    
  }  // popObservationNode()  
  
}  // class AggregateTree
