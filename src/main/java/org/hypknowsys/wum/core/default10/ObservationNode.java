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
 * An ObservationNode is one node of the trie AggregateTree, defined
 * by Myra Spiliopoulou and Lukas Faulstich. 
 * An ObservationNode is implemented similar to hplb.util.TrieNode, Author: 
 * Anders Kristensen. An ObservationNode contains its PageOccurrence, its
 * support value, its level in the corresponding AggregateTree, the
 * number of children (= ObservationNodes of next level in AggregateTree)
 * and a counter CurrentChild that can be used to traverse the 
 * correspondig AggregateTree. Each ObservationNode keeps a sorted array 
 * of its children and their keys, here: PageOccurrences. The VariableName
 * and graph drawing coordinates are stored for visualization.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class ObservationNode implements Serializable, Cloneable {

  // ########## attributes ##########
 
  protected PageOccurrence oPageOccurrence = null;
  protected long Support = 0L;
  
  protected int Level = 0; 
  protected int CounterChildren = 0;
  protected int CurrentChild = 0;
  // ordered list of PageOccurrences as Keys for ObservationNodes
  protected PageOccurrence[] ChildrenKeys = null;
  // children of this corresponding to ChildrenKeys
  protected ObservationNode[] Children = null;
  // ChildrenKeys[] an Children[] must have same number of valid entries

  protected String VariableName = null;
  protected int X = 0;  
  protected int Y = 0;
  
  // ########## constructors ##########
  
/**
 * constructs an empty ObservationNode
 */     

  public ObservationNode() {

    oPageOccurrence = new PageOccurrence();   Support = 0L;     
    Level = 0;   CounterChildren = 0;   CurrentChild = 0;
    ChildrenKeys = new PageOccurrence[1];
    Children = new ObservationNode[1];    
    VariableName = "";   X = 0;   Y = 0; 
    
  }  
  
/**
 * constructs an ObservationNode that contains the given data
 * ObservationNodes
 * @param pPageOccurrence PageOccurrence of the new ObservationNode
 * @param pSupport support value of the new ObservationNode
 * @param pLevel pLevel Level in the corresponding AggregateTree
 */   
    
  public ObservationNode(PageOccurrence pPageOccurrence, 
    long pSupport, int pLevel) {
  
    oPageOccurrence = pPageOccurrence;   Support = pSupport;     
    Level = pLevel;   CounterChildren = 0;   CurrentChild = 0;
    ChildrenKeys = new PageOccurrence[1];
    Children = new ObservationNode[1];    
    VariableName = "";   X = 0;   Y = 0; 
    
  }
  
/**
 * constructs an ObservationNode that contains the given data
 * ObservationNodes
 * @param pPageOccurrence PageOccurrence of the new ObservationNode
 * @param pSupport support value of the new ObservationNode
 * @param pLevel pLevel Level in the corresponding AggregateTree
 * @param pX graph drawing coordinate
 * @param pY graph drawing coordinate
 */   
    
  public ObservationNode(PageOccurrence pPageOccurrence, 
    long pSupport, int pLevel, int pX, int pY) {
  
    oPageOccurrence = pPageOccurrence;   Support = pSupport;     
    Level = pLevel;   CounterChildren = 0;   CurrentChild = 0;
    ChildrenKeys = new PageOccurrence[1];
    Children = new ObservationNode[1];    
    VariableName = "";   X = pX;   Y = pY; 
    
  }
  
  // ########## mutator methods ##########
  
  public void setPageOccurrence(PageOccurrence pPageOccurrence) 
    { oPageOccurrence = pPageOccurrence; }
  public void setPageID(long pPageID) 
    { oPageOccurrence.setPageID(pPageID); }
  public void setOccurrence(int pOccurrence) 
    { oPageOccurrence.setOccurrence(pOccurrence); }
  public void setSupport(long pSupport) { Support = pSupport; }
  public void incrementSupport() { Support++; }
  public void increaseSupport(long pIncrease) { Support += pIncrease; }
  public void incrementCurrentChild() { CurrentChild++; }
  public void setCurrentChild(int pCurrentChild) 
    { CurrentChild = pCurrentChild; }
  public void setLevel(int pLevel) { Level = pLevel; }    
  public void setVariableName(String pVariableName) 
    { VariableName = pVariableName; }
  public void setX(int pX) { X = pX; }
  public void setY(int pY) { Y = pY; }
  
  // ########## accessor methods ##########
  
  public PageOccurrence getPageOccurrence() { return oPageOccurrence; } 
  public long getPageID() { return oPageOccurrence.getPageID(); }
  public int getOccurrence() { return oPageOccurrence.getOccurrence(); }
  public long getSupport() { return Support; }
  public int getLevel() { return Level; }
  public int countChildren() { return CounterChildren; }
  public int getCurrentChild() { return CurrentChild; }
  public String getVariableName() { return VariableName; }
  public int getX() { return X; }
  public int getY() { return Y; }  
  // added by kwinkler 7/2000
  public ObservationNode[] getChildren() { return Children; }
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    return ("[" + oPageOccurrence.getPageID() +  ";" + 
      oPageOccurrence.getOccurrence() + ";" + Support + "]");    
            
  }  // toString()

  public Object clone() { 

      // does not work properly! kwinkler 12/99
  
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException e) {
      return null;
    }
    
  }  // clone() 
  
  /**
   * tests whether the ObservationNode contains the given child
   * @param pPageOccurrence PageOccurrence to be tested
   * @return true if ObservationNode contains pPageOccurrence
   */  
  
  public boolean containsChild(PageOccurrence pPageOccurrence) { 
  
    for (int i = 0; i < ChildrenKeys.length; i++)
      if ( (ChildrenKeys[i] != null) && 
        ( ChildrenKeys[i].equals(pPageOccurrence) ) ) 
        return true;
    
    return false;
  
  }
  
  /**
   * returns the ObservationNode's child with the given pIndex
   * @param pIndex 0 <= pIndex < this.countChildren()
   * @return corresponding child as an ObservationNode or null if pIndex is 
   * out of valid range
   */  
    
  public ObservationNode getChild(int pIndex) { 
  
    if ( (pIndex >= 0) && (pIndex <= CounterChildren) )
      return Children[pIndex]; 
    else 
      return null;
  
  }
   
  /**
   * adds recursively the given trail in the corresponding AggregateTree
   * @param pTrail Trail to add recusively
   * @param pLevel current level of the AggregateTree 
   * @return the number of new Observations added to AggregateTree
   */  
    
  protected int addTrail(Trail pTrail, int pLevel) {
  
    int vNewObservations = 0;  
    PageOccurrence oCurrentPageOccurrence = null;
    ObservationNode oChild = null;
  
    // level must be 0 in first call of addTrail()
    // stop recursion here due to end of trail
    if ( pLevel == pTrail.getSize() ) {
      this.incrementSupport();
      return vNewObservations;
    }    
    
    if (pLevel == 0)
      oCurrentPageOccurrence = pTrail.getFirstPageOccurrence();
    else
      oCurrentPageOccurrence = pTrail.getNextPageOccurrence();
    oChild = this.getChild(oCurrentPageOccurrence);
      
    if (oChild == null) {    
      oChild = new ObservationNode(oCurrentPageOccurrence, 0, pLevel + 1);
      vNewObservations = this.addChildAndCountNewObservations(
        oCurrentPageOccurrence, oChild);
    }
    
    this.incrementSupport();
    
    // next recursion with next child
    vNewObservations += oChild.addTrail(pTrail, pLevel + 1); 
    
    return vNewObservations;
  
  }  // addTrail()  

  /**
   * reverses and adds recursively the given trail in the corresponding 
   * AggregateTree
   * @param pTrail Trail to be reversed and added recursively
   * @param pLevel current level of the AggregateTree 
   * @return the number of new Observations added to AggregateTree
   */  
    
  protected int addReverseTrail(Trail pTrail, int pLevel) {
  
    int vNewObservations = 0;  
    PageOccurrence oCurrentPageOccurrence = null;
    ObservationNode oChild = null;
  
    // level must be 0 in first call of addTrail()
    // stop recursion here due to end of trail
    if ( pLevel == pTrail.getSize() ) {
      this.incrementSupport();
      return vNewObservations;
    }    
    
    if (pLevel == 0)
      oCurrentPageOccurrence = pTrail.getLastPageOccurrence();
    else
      oCurrentPageOccurrence = pTrail.getPreviousPageOccurrence();
    oChild = this.getChild(oCurrentPageOccurrence);
      
    if (oChild == null) {    
      oChild = new ObservationNode(oCurrentPageOccurrence, 0, pLevel + 1);
      vNewObservations = this.addChildAndCountNewObservations(
        oCurrentPageOccurrence, oChild);
    }
    
    this.incrementSupport();
    
    // next recursion with next child
    vNewObservations += oChild.addReverseTrail(pTrail, pLevel + 1); 
    
    return vNewObservations;
  
  }  // addReverseTrail()  

  /**
   * looks up the ObservationNode that is rooted at the last
   * PageOccurrence of pTrail; KDD Cup 2000: killer pages and brands viewed
   * @param pTrail start of unknown trail
   * @return null, if there is no complete matching in the AggregateTree
   */
   
  protected synchronized ObservationNode getSubtree(Trail pTrail, int pLevel) {

    int vNewObservations = 0;  
    PageOccurrence oCurrentPageOccurrence = null;
    ObservationNode oChild = null;
  
    // level must be 0 in first call of addTrail()
    // stop recursion here due to end of trail
    if ( pLevel == pTrail.getSize() ) {
      return this;
    }    
    
    if (pLevel == 0)
      oCurrentPageOccurrence = pTrail.getFirstPageOccurrence();
    else
      oCurrentPageOccurrence = pTrail.getNextPageOccurrence();
    oChild = this.getChild(oCurrentPageOccurrence);
      
    if (oChild == null) {    
      return null;  // appropriate strategy should be implemented later
    }
    
    // next recursion with next child
    return oChild.getSubtree(pTrail, pLevel + 1);         
    
  }  // getSubtree() 

  /**
   * returns the ObservationNode's child with the given pPageOccurrence
   * @param pPageOccurrence PageOccurrence of child to look for
   * @return corresponding child as an ObservationNode or null no child
   * exist that matches pPageOccurrence
   */  
  
  public final ObservationNode getChild(PageOccurrence pPageOccurrence) {
  
    for (int i = 0; i < CounterChildren; i++)
      if ( ChildrenKeys[i].compareTo(pPageOccurrence) == 0 ) 
        return Children[i];
    
    return null;
  
  }  // getChild()
  
  /**
   * adds the given pNewChild to the array of ObservationNode's children,
   * pPageOccurrence and pNewChild are one entity; if ObservationNode already
   * contains a child with pPageOccurrence, it will be replaced by pNewChild
   * @param pPageOccurrence key of pNewChild
   * @param pNewChild new child of ObservationNode
   */  
  
  protected final void addChild(PageOccurrence pPageOccurrence, 
    ObservationNode pNewChild) {    
    
    int vIndex = 0;    
    for (int i = 0; i < CounterChildren; i++) {
      vIndex = i;
      if (ChildrenKeys[i].compareTo(pPageOccurrence) >= 0) break;
    }
    
    if ( (vIndex < CounterChildren) && 
      (ChildrenKeys[vIndex].compareTo(pPageOccurrence) == 0) ) {
      // replace existing child; CounterChildren unchanged
      Children[vIndex] = pNewChild;     
    }   
    else {
      if (vIndex < CounterChildren) {
        // new Child inserted in between existing Children
        this.shiftUp(vIndex);    
      }
      else {
        // new child goes at end of list
        this.ensureSpace(CounterChildren + 1);
      }
      ChildrenKeys[vIndex] = pPageOccurrence;
      Children[vIndex] = pNewChild;
      CounterChildren++;
    }

  }  // addChild()                                  
  
  /**
   * adds the given pNewChild to the array of ObservationNode's children,
   * pPageOccurrence and pNewChild are one entity; if ObservationNode already
   * contains a child with pPageOccurrence, it will be replaced by pNewChild;
   * @param pPageOccurrence key of pNewChild
   * @param pNewChild new child of ObservationNode
   * @return the number of added observation in the corresponding AggregateTree
   */  
  
  protected final int addChildAndCountNewObservations(
    PageOccurrence pPageOccurrence, ObservationNode pNewChild) {    
    
  int vNewObservations = 0;
  int vIndex = 0;  
  for (int i = 0; i < CounterChildren; i++) {
    vIndex = i;
    if (ChildrenKeys[i].compareTo(pPageOccurrence) >= 0) break;
  }
  
  if ( (vIndex < CounterChildren) && 
    (ChildrenKeys[vIndex].compareTo(pPageOccurrence) == 0) ) {
    // replace existing child; CounterChildren unchanged
    Children[vIndex] = pNewChild;     
  }   
  else {
    if ( (CounterChildren >= 1) || (Level == 0) )
      vNewObservations++;      
    if (vIndex < CounterChildren) {
      // new Child inserted in between existing Children
      this.shiftUp(vIndex);    
    }
    else {
      // new child goes at end of list
      this.ensureSpace(CounterChildren + 1);
    }
    ChildrenKeys[vIndex] = pPageOccurrence;
    Children[vIndex] = pNewChild;
    CounterChildren++;
  }
  
  return vNewObservations;

  }  // addChildAndCountNewObservations() 
  
  /*
   * Shift up all elements in ChildrenKeys and Children one index from
   * specified position, presumably to make room for new element.
   * The element at the given position is also shifted up.
   */
   
  protected void shiftUp(int i) {
  
    // sometimes we're copying twice but this is immaterial here
    if ( (i < 0) || (i > CounterChildren) ) return;
    
    this.ensureSpace(CounterChildren + 1);
    System.arraycopy(ChildrenKeys, i, ChildrenKeys, i + 1, 
      CounterChildren - i);
    System.arraycopy(Children, i, Children, i + 1, CounterChildren - i);
    
  }  // shiftUp()
  
  /*
   * doubles the size of ChildrenKeys and Children
   */

  protected void ensureSpace(int n) {
  
    // note that (ChildrenKeys.length == Children.length) ALWAYS
    if (n > ChildrenKeys.length) {
      int newsize = Math.max(2 * ChildrenKeys.length, n);
      PageOccurrence[] newChildrenKeys = new PageOccurrence[newsize];
      ObservationNode[] newChildren = new ObservationNode[newsize];
      System.arraycopy(ChildrenKeys, 0, newChildrenKeys, 
        0, ChildrenKeys.length);
      System.arraycopy(Children, 0, newChildren, 0, Children.length);
      ChildrenKeys = newChildrenKeys;
      Children = newChildren;
    }
    
  }  // ensureSpace()
  
  /**
   * dumps recuresively the corresponding AggregateTree to the 
   * given PrintStream, used for debugging
   */

  public void print(PrintStream out, ObservationNode[] path, int index) {

   if ( CounterChildren == 0 ) {
     for (int j = 0; j < index; j++) 
       out.println("  [" + path[j].toString() + "] ");
     out.println();
   }

   for (int i = 0; i < CounterChildren; i++) {
     if (Children[i] != null) {
       path[index] = Children[i];
       Children[i].print(out, path, index + 1);
     }
   }
     
  }  // print()
  
  /**
   * resets recuresively all attributes CurrentsChilds of the corresponding 
   * AggregateTree, must be called before a new traversal is started
   */

  public void initCurrentChild(ObservationNode[] path, int index) {

   CurrentChild = 0;
   X = 0;  // actually: SupportAdded = false; MintQueryProcessor
   if ( CounterChildren == 0 ) return;

   for ( int i = 0; i < CounterChildren; i++) {
     if (Children[i] != null) {
       path[index] = Children[i];
       Children[i].initCurrentChild(path, index + 1);
     }
   }
   
  }  // initCurrentChild()  

  /**
   * added by kwinkler 12/99, WUM.v44
   * sets this.Support = 0 and calls itself on all children
   * used to build navigation patterns corresponding to GSM
   */

  public void initSupport(ObservationNode[] path, int index) {

   Support = 0;
   if ( CounterChildren == 0 ) return;

   for ( int i = 0; i < CounterChildren; i++) {
     if (Children[i] != null) {
       path[index] = Children[i];
       Children[i].initSupport(path, index + 1);
     }
   }
   
  }  // initCurrentChild()  


  /**
   * used to recursively get the first Observation of the corresponding
   * AggregateTree, must be called to start a new traversal
   */

  public Observation getFirst(ObservationNode[] pTraversalArray, int pLevel,
    Observation pObservation) {

   if ( CounterChildren == 0 ) return pObservation;

   for ( int i = 0; i < CounterChildren; i++) {
   
     if (Children[i] != null) {
       pTraversalArray[pLevel] = Children[i];
       if (pLevel == 0) 
         pObservation.setFirstObservationNode( Children[i] );
       else
         pObservation.setNextObservationNode( Children[i] );
       Observation oObservation = 
         ( Children[i].getFirst( pTraversalArray, pLevel + 1, pObservation) );
       if ( (Children[i].countChildren() == 0) ||
         (Children[i].countChildren() < Children[i].getCurrentChild() + 1) ) 
         // set current child for next observation
         CurrentChild = i + 1;  
       return pObservation;
     }
     
   }
   
   return null;
     
  }  // getFirst()
  
  /**
   * used to recursively get the next Observation of the corresponding 
   * AggregateTree, subsequent calls traverse the AggregateTree
   */

  public Observation getNext(ObservationNode[] pTraversalArray, int pLevel,
    Observation pObservation) {

   if ( CounterChildren == 0 ) return pObservation;
     
   for ( int i = CurrentChild; i < CounterChildren; i++) {
   
     if (Children[i] != null) {
       pTraversalArray[pLevel] = Children[i];
       if (pLevel == 0)
         pObservation.setFirstObservationNode( Children[i] );
       else
         pObservation.setNextObservationNode( Children[i] );
       Observation oObservation = ( Children[i].getNext(
         pTraversalArray, pLevel + 1, pObservation) );
       if ( (Children[i].countChildren() == 0) ||
         (Children[i].countChildren() < Children[i].getCurrentChild() + 1) ) 
         // set current child for next observation
         CurrentChild = i + 1;  
       return pObservation;
     }
     
   }     
   
   return null;
     
  }  // getNext() 
  
}  // class ObservationNode
