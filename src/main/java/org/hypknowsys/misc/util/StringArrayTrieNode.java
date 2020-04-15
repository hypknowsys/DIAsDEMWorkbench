/*
 * Copyright (C) 2000-2005, Henner Graubitz, Myra Spiliopoulou, Karsten 
 * Winkler. All rights reserved.
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

package org.hypknowsys.misc.util;  // for convenience only

import java.util.*;
import java.io.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class StringArrayTrieNode {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  // ordered list of strings associated
  protected String[] OrderedListOfStrings;        
  // children corresponding to OrderedListOfStrings
  protected StringArrayTrieNode[] OrderedListOfChildren;       
  // number of valid entries in OrderedListOfStrings and OrderedListOfChildren
  protected int SizeOfOrderedLists = 0;    
  // value of this node
  protected Object Value = null; 

  protected static boolean PreviousMatchIsPrefixOfKey = false;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public StringArrayTrieNode() {
    
    // These two arrays must always be of the same size.
    // OrderedListOfStrings = new String[1];
    // OrderedListOfChildren = new StringArrayTrieNode[1];
    OrderedListOfStrings = new String[0];
    OrderedListOfChildren = new StringArrayTrieNode[0];
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  protected Object put(String[] pKey, Object pValue, int pIndex) {
    
    if (pIndex == pKey.length) {
      Object oldValue = Value;
      Value = pValue;
      return oldValue;
    }

    StringArrayTrieNode trieNode = getChild(pKey[pIndex]);
    if (trieNode == null) {
      putChild(pKey[pIndex], trieNode = new StringArrayTrieNode());
    }
    
    return trieNode.put(pKey, pValue, pIndex + 1);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected Object remove(String[] pKey, int pIndex) {
    
    if (pIndex == pKey.length) {
      Object oldValue = Value;
      Value = null;
      return oldValue;
    }
    StringArrayTrieNode trieNode = getChild(pKey[pIndex]);
    if (trieNode != null) {
      return trieNode.remove(pKey, pIndex + 1);
    }
    else {
      return null;
    }
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public Object get(String[] pKey) {
    
    StringArrayTrieNode current = this;
    int n = pKey.length;
    int pIndex = 0;

    while (current != null) {
      if (pIndex == n) {
        return current.Value;
      }
      current = current.getChild(pKey[pIndex]);
      pIndex++;
    }
    
    return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public Object getAndCheckIfKeyIsPrefixOfOtherKey(String[] pKey) {
    
    StringArrayTrieNode current = this;
    int n = pKey.length;
    int pIndex = 0;

    while (current != null) {
      if (pIndex == n) {
        if (current.OrderedListOfChildren.length > 0
        && current.OrderedListOfChildren[0] != null) {
          PreviousMatchIsPrefixOfKey = true;
        }
        else {
          PreviousMatchIsPrefixOfKey = false;
        }
        return current.Value;
      }
      current = current.getChild(pKey[pIndex]);
      pIndex++;
    }
    
    return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KeyValuePair getBestMatch(String[] pKey) {
    
    StringArrayTrieNode bestNode = null;  // best match so far - provisional result
    int bestIndex = -1;  // pValue of pIndex when 'best' match made
    StringArrayTrieNode currentNode = this;
    int n = pKey.length;
    int pIndex = 0;

    while (currentNode != null) {
      if (currentNode.Value != null) {
        bestNode = currentNode;
        bestIndex = pIndex;
      }
      if (pIndex == n) break;
      currentNode = currentNode.getChild(pKey[pIndex]);
      pIndex++;
    }

    if (bestNode != null) {
      String[] result = new String[bestIndex];
      for (int i = 0; i < result.length; i++) {
        result[i] = pKey[i];
      }
      return new KeyValuePair(result, bestNode.Value);
    }
    
    return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected final StringArrayTrieNode getChild(String pCharacter) {
    
//    // original code to search for the child
//    for (int i = 0; i < SizeOfOrderedLists; i++) {
//      if (OrderedListOfStrings[i].compareTo(pCharacter) == 0) {
//        return OrderedListOfChildren[i];
//      }
//    }
//    return null;
   
    if (SizeOfOrderedLists == 0 || pCharacter == null) {
      return null;
    }
    
    int childIndex = Arrays.binarySearch(OrderedListOfStrings, pCharacter);
    if (childIndex >= 0) {
      return OrderedListOfChildren[childIndex];
    }
    else {
      return null;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected final StringArrayTrieNode putChild(String pCharacter, 
  StringArrayTrieNode pTrieNode) {
    
    StringArrayTrieNode oldValue = null;
    int i;

    for (i = 0; i < SizeOfOrderedLists; i++) {
      if (OrderedListOfStrings[i].compareTo(pCharacter) >= 0) break;
    }

    if (i < SizeOfOrderedLists && OrderedListOfStrings[i]
    .compareTo(pCharacter) == 0) {
      // replace existing child; SizeOfOrderedLists unchanged
      oldValue = OrderedListOfChildren[i];
      OrderedListOfChildren[i] = pTrieNode;

    } 
    else {
      if (i < SizeOfOrderedLists) {
        // new element inserted in between existing elements
        shiftUp(i);
      } else {
        // element goes at end of the list
        // ensureSpace(SizeOfOrderedLists + 1);
        incrementSizeOfArrays();
      }
      OrderedListOfStrings[i] = pCharacter;
      OrderedListOfChildren[i] = pTrieNode;
      SizeOfOrderedLists++;
    }

    return oldValue;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  // Shift up all elements in OrderedListOfStrings and OrderedListOfChildren 
  // one index from specified position, presumably to make room for new 
  // element. The element at the given position is also shifted up.

  protected void shiftUp(int pIndex) {
    
    if (pIndex < 0 || pIndex > SizeOfOrderedLists) {
      return;
    }
    // ensureSpace(SizeOfOrderedLists + 1);
    incrementSizeOfArrays();
    System.arraycopy(OrderedListOfStrings, pIndex, OrderedListOfStrings, 
    pIndex + 1, SizeOfOrderedLists - pIndex);
    System.arraycopy(OrderedListOfChildren, pIndex, OrderedListOfChildren, 
    pIndex + 1, SizeOfOrderedLists - pIndex);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
//  protected void ensureSpace(int pSize) {
//    
//    if (pSize > OrderedListOfStrings.length) {
//      int newsize = Math.max(2 * OrderedListOfStrings.length, pSize);
//      String[] newchars = new String[newsize];
//      StringArrayTrieNode[] newchildren = new StringArrayTrieNode[newsize];
//      System.arraycopy(OrderedListOfStrings, 0, newchars, 0, 
//      OrderedListOfStrings.length);
//      System.arraycopy(OrderedListOfChildren, 0, newchildren, 0, 
//      OrderedListOfChildren.length);
//      OrderedListOfStrings = newchars;
//      OrderedListOfChildren = newchildren;
//    }
//    
//  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void incrementSizeOfArrays() {
    
    // array must only contain defined values in order to employ
    // Arrays.binarySearch().
    int newsize = OrderedListOfStrings.length + 1;
    String[] newchars = new String[newsize];
    StringArrayTrieNode[] newchildren = new StringArrayTrieNode[newsize];
    System.arraycopy(OrderedListOfStrings, 0, newchars, 0,
    OrderedListOfStrings.length);
    System.arraycopy(OrderedListOfChildren, 0, newchildren, 0,
    OrderedListOfChildren.length);
    OrderedListOfStrings = newchars;
    OrderedListOfChildren = newchildren;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void addElements(Dictionary pDictionary, String[] pKey, 
  int pIndex) {
    
    String string;
    StringArrayTrieNode trieNode;

    if (Value != null) {
      TmpStringBuffer = new StringBuffer();
      for (int i = 0; i < pIndex; i++) {
        TmpStringBuffer.append(pKey[i]);      
      }
      pDictionary.put(TmpStringBuffer.toString(), Value);
    }

    for (int i = 0; i < SizeOfOrderedLists; i++) {
      string = OrderedListOfStrings[i];
      trieNode = OrderedListOfChildren[i];
      if (trieNode != null) {
        pKey[pIndex] = string;
        trieNode.addElements(pDictionary, pKey, pIndex + 1);
      }
    }
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void dumpNode(PrintStream pOutputStream, String aggregateString) {
    
    pOutputStream.println(this + " {");
    pOutputStream.println("  Key   = " + aggregateString);
    pOutputStream.println("  SizeOfOrderedLists = " + SizeOfOrderedLists 
    + '(' + OrderedListOfStrings.length +')');
    pOutputStream.println("  Value = " + Value);
    pOutputStream.println("  [");
    for (int i = 0; i < SizeOfOrderedLists; i++) {
      pOutputStream.println("    " + OrderedListOfStrings[i] + ": " 
      + OrderedListOfChildren[i]);
    }
    pOutputStream.println("  ]");
    pOutputStream.println("}");
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void dump(PrintStream pOutputStream, String[] pKey, int pIndex) {
    
    TmpStringBuffer = new StringBuffer();
    for (int i = 0; i < pIndex; i++) {
      TmpStringBuffer.append(pKey[i]);
      TmpStringBuffer.append(" ");   
    }
    dumpNode(pOutputStream, TmpStringBuffer.toString());

    for (int i = 0; i < SizeOfOrderedLists; i++) {
      if (OrderedListOfChildren[i] != null) {
        pKey[pIndex] = OrderedListOfStrings[i];
        OrderedListOfChildren[i].dump(pOutputStream, pKey, pIndex + 1);
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void print(PrintStream pOutputStream, String[] pKey, int pIndex) {
    
    if (Value != null) {
      TmpStringBuffer = new StringBuffer();
      for (int i = 0; i < pIndex; i++) {
        TmpStringBuffer.append(pKey[i]);
        TmpStringBuffer.append(" ");
      }
      pOutputStream.println("  " + TmpStringBuffer.toString() + "=" 
      + Value + ", ");
    }

    for (int i = 0; i < SizeOfOrderedLists; i++) {
      if (OrderedListOfChildren[i] != null) {
        pKey[pIndex] = OrderedListOfStrings[i];
        OrderedListOfChildren[i].print(pOutputStream, pKey, pIndex + 1);
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}