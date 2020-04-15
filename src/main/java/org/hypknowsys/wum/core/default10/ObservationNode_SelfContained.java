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
 * An ObservationNode_SelfContained is one simplified node of the trie 
 * AggregateTree, defined by Myra Spiliopoulou and Lukas Faulstich. Used to 
 * visualize AggregateTree without overhead code. An 
 * ObservationNode_SelfContained is implemented similar to hplb.util.Trie, 
 * Author: Anders Kristensen. An ObservationNode_SelfContained contains its 
 * PageOccurrence_SelfContained, its support value, its level in the 
 * corresponding AggregateTree, the number of children 
 * (= ObservationNode_SelfContaineds of next level in AggregateTree)
 * and a counter CurrentChild that can be used to traverse the 
 * correspondig AggregateTree. Each ObservationNode_SelfContained keeps 
 * a sorted array of its children and their key, here: 
 * PageOccurrence_SelfContaineds. The variable 
 * name and graph drawing coordinates are stored for visualization.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class ObservationNode_SelfContained implements Serializable {

  // ########## attributes ##########
 
  protected PageOccurrence_SelfContained oPageOccurrence_SelfContained = null;
  protected String PageUrl = null;
  protected long Support = 0L;
  protected boolean SupportAdded = false;
  
  protected int Level = 0; 
  protected int CounterChildren = 0;
  protected int CurrentChild = 0;
  // ordered list of PageOccurrence_SelfContaineds as Keys 
  // for ObservationNode_SelfContaineds
  protected PageOccurrence_SelfContained[] ChildrenKeys = null;
  // children of this corresponding to ChildrenKeys
  protected ObservationNode_SelfContained[] Children = null;
  // ChildrenKeys[] an Children[] must have same number of valid entries

  protected String VariableName = null;
  protected int X = 0;  
  protected int Y = 0;
  
  // ########## constructors ##########
  
/**
 * constructs an empty ObservationNode_SelfContained
 */     

  public ObservationNode_SelfContained() {

    oPageOccurrence_SelfContained = new PageOccurrence_SelfContained();   
    PageUrl = ""; Support = 0L; SupportAdded = false;     
    Level = 0;   CounterChildren = 0;   CurrentChild = 0;
    ChildrenKeys = new PageOccurrence_SelfContained[1];
    Children = new ObservationNode_SelfContained[1];    
    VariableName = "";   X = 0;   Y = 0; 
    
  }  
  
/**
 * constructs an ObservationNode_SelfContained that contains the given data
 * ObservationNode_SelfContaineds
 * @param pPageOccurrence_SelfContained PageOccurrence_SelfContained of the new 
 * ObservationNode_SelfContained
 * @param pSupport support value of the new ObservationNode_SelfContained
 * @param pLevel pLevel Level in the corresponding AggregateTree
 */   
    
  public ObservationNode_SelfContained(
    PageOccurrence_SelfContained pPageOccurrence_SelfContained, 
    String pPageUrl, long pSupport, int pLevel) {
  
    oPageOccurrence_SelfContained = pPageOccurrence_SelfContained;   
    PageUrl = pPageUrl; Support = pSupport; SupportAdded = false;   
    Level = pLevel;   CounterChildren = 0;   CurrentChild = 0;
    ChildrenKeys = new PageOccurrence_SelfContained[1];
    Children = new ObservationNode_SelfContained[1];    
    VariableName = "";   X = 0;   Y = 0; 
    
  }
  
  public ObservationNode_SelfContained(
    PageOccurrence_SelfContained pPageOccurrence_SelfContained, 
    String pPageUrl, long pSupport, int pLevel, String pVariableName) {
  
    oPageOccurrence_SelfContained = pPageOccurrence_SelfContained;   
    PageUrl = pPageUrl; Support = pSupport; SupportAdded = false;    
    Level = pLevel;   CounterChildren = 0;   CurrentChild = 0;
    ChildrenKeys = new PageOccurrence_SelfContained[1];
    Children = new ObservationNode_SelfContained[1];    
    VariableName = pVariableName;   X = 0;   Y = 0; 
    
  }
  
/**
 * constructs an ObservationNode_SelfContained that contains the given data
 * ObservationNode_SelfContaineds
 * @param pPageOccurrence_SelfContained PageOccurrence_SelfContained of the new 
 * ObservationNode_SelfContained
 * @param pSupport support value of the new ObservationNode_SelfContained
 * @param pLevel pLevel Level in the corresponding AggregateTree
 * @param pX graph drawing coordinate
 * @param pY graph drawing coordinate
 */   
    
  public ObservationNode_SelfContained(
    PageOccurrence_SelfContained pPageOccurrence_SelfContained, 
    String pPageUrl, long pSupport, int pLevel, int pX, int pY) {
  
    oPageOccurrence_SelfContained = pPageOccurrence_SelfContained;   
    PageUrl = pPageUrl; Support = pSupport; SupportAdded = false;
    Level = pLevel;   CounterChildren = 0;   CurrentChild = 0;
    ChildrenKeys = new PageOccurrence_SelfContained[1];
    Children = new ObservationNode_SelfContained[1];    
    VariableName = "";   X = pX;   Y = pY; 
    
  }
  
  // ########## mutator methods ##########
  
  public void setPageOccurrence_SelfContained(
    PageOccurrence_SelfContained pPageOccurrence_SelfContained) 
    { oPageOccurrence_SelfContained = pPageOccurrence_SelfContained; }
  public void setPageID(long pPageID) 
    { oPageOccurrence_SelfContained.setPageID(pPageID); }
  public void setOccurrence(int pOccurrence) 
    { oPageOccurrence_SelfContained.setOccurrence(pOccurrence); }
  public void setPageUrl(String pPageUrl) { PageUrl = pPageUrl; }
  public void setSupport(long pSupport) { Support = pSupport; }
  public void setSupportAdded(boolean pSupportAdded) 
    { SupportAdded = pSupportAdded; }
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
  
  public PageOccurrence_SelfContained getPageOccurrence_SelfContained() 
    { return oPageOccurrence_SelfContained; } 
  public long getPageID() 
    { return oPageOccurrence_SelfContained.getPageID(); }
  public int getOccurrence() 
    { return oPageOccurrence_SelfContained.getOccurrence(); }
  public String getPageUrl() { return PageUrl; }
  public long getSupport() { return Support; }
  public boolean getSupportAdded() { return SupportAdded; }
  public int getLevel() { return Level; }
  public int countChildren() { return CounterChildren; }
  public int getCurrentChild() { return CurrentChild; }
  public String getVariableName() { return VariableName; }
  public int getX() { return X; }
  public int getY() { return Y; }  
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    return ("[" + oPageOccurrence_SelfContained.getPageID() +  ";" + 
      oPageOccurrence_SelfContained.getOccurrence() + ";" + Support + "]");    
            
  }  // toString()
  
  /**
   * tests whether the ObservationNode_SelfContained contains the given child
   * @param pPageOccurrence_SelfContained PageOccurrence_SelfContained to be 
   * tested
   * @return true if ObservationNode_SelfContained contains 
   * pPageOccurrence_SelfContained
   */  
  
  public boolean containsChild(
    PageOccurrence_SelfContained pPageOccurrence_SelfContained) { 
  
    for (int i = 0; i < ChildrenKeys.length; i++)
      if ( (ChildrenKeys[i] != null) && 
        ( ChildrenKeys[i].equals(pPageOccurrence_SelfContained) ) ) 
        return true;
    
    return false;
  
  }
  
  /**
   * returns the ObservationNode_SelfContained's child with the given pIndex
   * @param pIndex 0 <= pIndex < this.countChildren()
   * @return corresponding child as an ObservationNode_SelfContained or null if 
   * pIndex is out of valid range
   */  
    
  public ObservationNode_SelfContained getChild(int pIndex) { 
  
    if ( (pIndex >= 0) && (pIndex <= CounterChildren) )
      return Children[pIndex]; 
    else 
      return null;
  
  }
  
  // ########## comments will follow ##########

  protected ObservationNode_SelfContained[] addObservationNew(
    ObservationNode_SelfContained[] pObservation, int pLevel) {

    // all Observations that are added to the current AggregateTree
    // must be Observations of the same AggregateTree

    if (pLevel > 0)
      if ( ! pObservation[pLevel-1].getSupportAdded() ) {
        Support += pObservation[pLevel-1].getSupport();
        pObservation[pLevel-1].setSupportAdded(true);   
      }

    // level must be 0 in first call of addObservation()
    // stop recursion here due to end of trail
    if (pLevel == pObservation.length) {
      return pObservation;
    }

// System.out.println( "Visiting now = " + this.toString() );
// System.out.println( "pObservation[pLevel-1] = " + 
// pObservation[Math.max(0,pLevel-1)].toString() );

    ObservationNode_SelfContained oChild = this.getChild( 
      pObservation[pLevel].getPageOccurrence_SelfContained() );
    if (oChild == null) {
      // this part of new Observation doesn't exist in AggregateTree
      // until now
      oChild = new ObservationNode_SelfContained(
        pObservation[pLevel].getPageOccurrence_SelfContained(),
        pObservation[pLevel].getPageUrl(), 0, pLevel + 1, 
        pObservation[pLevel].getVariableName());

// System.out.println( "Adding child = " + pObservation[pLevel]. toString() );

      this.addChild(pObservation[pLevel].getPageOccurrence_SelfContained(), 
        oChild);
    }

    // next recursion with next child
    ObservationNode_SelfContained[] oDummyObservation = 
      oChild.addObservationNew(pObservation, pLevel + 1);

    return pObservation;

  }  // addObservationNew()  
   
  /**
   * returns the ObservationNodeApplet's child with the given 
   * pPageOccurrence_SelfContained
   * @param pPageOccurrence_SelfContained PageOccurrence_SelfContained of 
   * child to look for
   * @return corresponding child as an ObservationNode_SelfContained or 
   * null no child exist that matches pPageOccurrence_SelfContained
   */  
  
  protected final ObservationNode_SelfContained getChild(
    PageOccurrence_SelfContained pPageOccurrence_SelfContained) {
  
    for (int i = 0; i < CounterChildren; i++)
      if ( ChildrenKeys[i].compareTo(pPageOccurrence_SelfContained) == 0 ) 
        return Children[i];
    
    return null;
  
  }  // getChild()
  
  /**
   * adds the given pNewChild to the array of ObservationNode_SelfContained's 
   * children,
   * pPageOccurrence_SelfContained and pNewChild are one entity; if 
   * ObservationNode_SelfContained already
   * contains a child with pPageOccurrence_SelfContained, it will be replaced 
   * by pNewChild
   * @param pPageOccurrence_SelfContained key of pNewChild
   * @param pNewChild new child of ObservationNode_SelfContained
   */  
  
  protected final void addChild(
    PageOccurrence_SelfContained pPageOccurrence_SelfContained, 
    ObservationNode_SelfContained pNewChild) {    
    
    int vIndex = 0;    
    for (int i = 0; i < CounterChildren; i++) {
      vIndex = i;
      if (ChildrenKeys[i].compareTo(pPageOccurrence_SelfContained) >= 0) break;
    }
    
    if ( (vIndex < CounterChildren) && (ChildrenKeys[vIndex].compareTo(
      pPageOccurrence_SelfContained) == 0) ) {
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
      ChildrenKeys[vIndex] = pPageOccurrence_SelfContained;
      Children[vIndex] = pNewChild;
      CounterChildren++;
    }

  }  // addChild()                                  
  
  /*
   * Shift up all elements in ChildrenKeys and Children one index from
   * specified position, presumably to make room for new element.
   * The element at the given position is also shifted up.
   */
   
  protected void shiftUp(int i) {
  
    // sometimes we're copying twice but this is immaterial here
    if ( (i < 0) || (i > CounterChildren) ) return;
    
    this.ensureSpace(CounterChildren + 1);
    System.arraycopy(ChildrenKeys, i, ChildrenKeys, i + 1, CounterChildren - i);
    System.arraycopy(Children, i, Children, i + 1, CounterChildren - i);
    
  }  // shiftUp()
  
  /*
   * doubles the size of ChildrenKeys and Children
   */

  protected void ensureSpace(int n) {
  
    // note that (ChildrenKeys.length == Children.length) ALWAYS
    if (n > ChildrenKeys.length) {
      int newsize = Math.max(2 * ChildrenKeys.length, n);
      PageOccurrence_SelfContained[] newChildrenKeys =
        new PageOccurrence_SelfContained[newsize];
      ObservationNode_SelfContained[] newChildren = 
        new ObservationNode_SelfContained[newsize];
      System.arraycopy(ChildrenKeys, 0, newChildrenKeys, 0, 
        ChildrenKeys.length);
      System.arraycopy(Children, 0, newChildren, 0, Children.length);
      ChildrenKeys = newChildrenKeys;
      Children = newChildren;
    }
    
  }  // ensureSpace()
  
  /**
   * resets recuresively all attributes CurrentsChilds of the corresponding 
   * AggregateTree, must be called before a new traversal is started
   */

  public void initCurrentChild(
    ObservationNode_SelfContained[] path, int index) {

   CurrentChild = 0;
   if ( CounterChildren == 0 ) return;

   for ( int i = 0; i < CounterChildren; i++) {
     if (Children[i] != null) {
       path[index] = Children[i];
       Children[i].initCurrentChild(path, index + 1);
     }
   }
   
  }  // initCurrentChild()  

  /**
   * used to recursively get the first Observation of the corresponding
   * AggregateTree, must be called to start a new traversal
   */

  public ObservationNode_SelfContained[] getFirst(
    ObservationNode_SelfContained[] pTraversalArray, 
    int pLevel) {

    if ( CounterChildren == 0 ) {
      ObservationNode_SelfContained[] oObservation = 
        new ObservationNode_SelfContained[pLevel];
      for (int j = 0; j < pLevel; j++) 
        oObservation[j] = pTraversalArray[j];
      return oObservation;     
    }

   for ( int i = 0; i < CounterChildren; i++) {
   
     if (Children[i] != null) {
        pTraversalArray[pLevel] = Children[i];
        ObservationNode_SelfContained[] oObservation = 
          ( Children[i].getFirst(pTraversalArray, pLevel + 1) );
       if ( (Children[i].countChildren() == 0) ||
         (Children[i].countChildren() < Children[i].getCurrentChild() + 1) ) 
         // set current child for next observation
         CurrentChild = i + 1;  
       return oObservation;
     }
     
   }
   
   return null;
     
  }  // getFirst()
  
  /**
   * used to recursively get the next Observation of the corresponding 
   * AggregateTree, subsequent calls traverse the AggregateTree
   */

  public ObservationNode_SelfContained[] getNext(
    ObservationNode_SelfContained[] pTraversalArray, 
    int pLevel) {

    if ( CounterChildren == 0 ) {
      ObservationNode_SelfContained[] oObservation = 
        new ObservationNode_SelfContained[pLevel];
      for (int j = 0; j < pLevel; j++) 
        oObservation[j] = pTraversalArray[j];
      return oObservation;     
    }
     
   for ( int i = CurrentChild; i < CounterChildren; i++) {
   
     if (Children[i] != null) {
       pTraversalArray[pLevel] = Children[i];
       ObservationNode_SelfContained[] oObservation = 
         ( Children[i].getFirst(pTraversalArray, pLevel + 1) );
       if ( (Children[i].countChildren() == 0) ||
         (Children[i].countChildren() < Children[i].getCurrentChild() + 1) ) 
         // set current child for next observation
         CurrentChild = i + 1;  
       return oObservation;
     }
     
   }     
   
   return null;
     
  }  // getNext() 
  
}  // class ObservationNode_SelfContained
