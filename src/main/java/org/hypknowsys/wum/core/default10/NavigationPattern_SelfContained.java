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
 * A NavigationPattern is used to represent MINT query results, it's defined
 * by Myra Spiliopoulou and Lukas Faulstich. Basically, it contains all 
 * 2-sub-MintPatternDescriptors that are adjacent in the original
 * MintPatternDescriptor and the corresponding sub-AggregateTrees. 
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class NavigationPattern_SelfContained implements Serializable {

  // ########## attributes ##########
 
  private AggregateTree_SelfContained[] SubTrees = null;
  
  // ########## constructors ##########
  
/**
 * constructs an empty NavigationPattern
 */   

  public NavigationPattern_SelfContained() {
  
    SubTrees = null;
    
  }  
  
/**
 * constructs an NavigationPattern with the given number of sub-patterns
 * and sub-trees
 * @param pCountSubPatternDescriptors number of sub-MintPatternDescriptors
 * @param pCountSubTrees number of sub-AggregateTrees
 */   

  public NavigationPattern_SelfContained(int pCountSubTrees) {
  
    SubTrees = new AggregateTree_SelfContained[pCountSubTrees];

  }
  
  // ########## accessor methods ##########
  
  public int countSubTrees() { return SubTrees.length; }
    
  // ########## standard methods ##########
  
  public String toString() {
  
   return super.toString();
    
  }  // toString()
    
  /**
   * returns the the sub-AggregateTree with the given pIndex
   * @param pIndex 0 <= pIndex < this.countSubTrees()
   * @return corresponding Aggregate or null if pIndex is 
   * out of valid range
   */  
  
  public AggregateTree_SelfContained getSubTree(int pIndex) { 
  
    if ( (pIndex >= 0) && (pIndex < SubTrees.length) )
      return SubTrees[pIndex];
    else
      return null;
      
  }  // getSubTree()
  
  /**
   * sets the sub-MintPatternDescriptor given by pIndex
   * @param pIndex 0 <= pIndex < this.countSubPatternDescriptors()
   * @param pSubPatternDescriptor MintPatternDescriptor to add
   */  
  
  public void setSubTree(int pIndex, AggregateTree_SelfContained pSubTree) { 
  
    if ( (pIndex >= 0) && (pIndex < SubTrees.length) ) 
      SubTrees[pIndex] = pSubTree;
      
  }  // setSubTree()  
  
}  // class NavigationPattern_SelfContained
