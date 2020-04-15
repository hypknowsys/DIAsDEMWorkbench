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

package org.hypknowsys.misc.util;

import java.util.*;
import java.io.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler; 
 */
  
public class StringTrieNode {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  // ordered list of chars associated
  protected byte[] OrderedListOfChars;        
  // children corresponding to OrderedListOfChars
  protected StringTrieNode[] OrderedListOfChildren;       
  // number of valid entries in OrderedListOfChars and OrderedListOfChildren
  protected int SizeOfOrderedLists = 0;    
  // value of this node
  protected Object Value = null; 
  
  protected static boolean PreviousMatchPrecedesBlankSpace = false;

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

  public StringTrieNode() {
    
    // These two arrays must always be of the same size.
    // OrderedListOfChars = new byte[1];
    // OrderedListOfChildren = new StringTrieNode[1];
    OrderedListOfChars = new byte[0];
    OrderedListOfChildren = new StringTrieNode[0];
    
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

  protected Object put(byte[] pKey, Object pValue, int pIndex) {
    
    if (pIndex == pKey.length) {
      Object oldValue = Value;
      Value = pValue;
      return oldValue;
    }

    StringTrieNode stringTrieNode = this.getChild(pKey[pIndex]);
    if (stringTrieNode == null) {
      putChild(pKey[pIndex], stringTrieNode = new StringTrieNode());
    }
    
    return stringTrieNode.put(pKey, pValue, pIndex + 1);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected Object remove(byte[] pKey, int pIndex) {
    
    if (pIndex == pKey.length) {
      Object oldValue = Value;
      Value = null;
      return oldValue;
    }
    StringTrieNode stringTrieNode = getChild(pKey[pIndex]);
    if (stringTrieNode != null) {
      return stringTrieNode.remove(pKey, pIndex + 1);
    }
    else {
      return null;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected Object get(byte[] pKey) {
    
    StringTrieNode current = this;
    int n = pKey.length;
    int index = 0;

    while (current != null) {
      if (index == n) {
        return current.Value;
      }
      current = current.getChild(pKey[index]);
      index++;
    }
    return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected Object getAndCheckForSubsequentBlankSpace(byte[] pKey) {
    
    StringTrieNode current = this;
    int n = pKey.length;
    int index = 0;

    while (current != null) {
      if (index == n) {
        if (current.getChild((byte)' ') != null) {
          PreviousMatchPrecedesBlankSpace = true;
        }
        else {
          PreviousMatchPrecedesBlankSpace = false;
        }
        return current.Value;
      }
      current = current.getChild(pKey[index]);
      index++;
    }
    return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected KeyValuePair getBestMatch(byte[] pKey) {
    
    StringTrieNode bestNode = null;  // best match so far - provisional result
    int bestIndex = -1;  // value of index when 'best' match made
    StringTrieNode currentNode = this;
    int n = pKey.length;
    int index = 0;

    while (currentNode != null) {
      if (currentNode.Value != null) {
        bestNode = currentNode;
        bestIndex = index;
      }
      if (index == n) {
        break;
      }
      currentNode = currentNode.getChild(pKey[index]);
      index++;
    }

    if (bestNode != null) {
      return new KeyValuePair(new String(pKey, 0, 0, bestIndex), 
      bestNode.Value);
    }
    
    return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected KeyValuePair getBestMatchAndCheckForSubsequentBlankSpace(
  byte[] pKey) {
    
    StringTrieNode bestNode = null;  // best match so far - provisional result
    int bestIndex = -1;  // value of index when 'best' match made
    StringTrieNode currentNode = this;
    int n = pKey.length;
    int index = 0;

    while (currentNode != null) {
      if (currentNode.Value != null) {
        bestNode = currentNode;
        bestIndex = index;
      }
      if (index == n) {
        if (currentNode.getChild((byte)' ') != null) {
          PreviousMatchPrecedesBlankSpace = true;
        }
        else {
          PreviousMatchPrecedesBlankSpace = false;
        }
        break;
      }
      currentNode = currentNode.getChild(pKey[index]);
      index++;
    }

    if (bestNode != null) {
      return new KeyValuePair(new String(pKey, 0, 0, bestIndex), 
      bestNode.Value);
    }
    
    return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected final StringTrieNode getChild(byte pCharacter) {
    
//    // original code to search for the child
//    for (int i = 0; i < SizeOfOrderedLists; i++) {
//      if (OrderedListOfChars[i] == pCharacter) {
//        return OrderedListOfChildren[i];
//      }
//    }
//    return null;

    if (SizeOfOrderedLists == 0) {
      return null;
    }
    
    int childIndex = Arrays.binarySearch(OrderedListOfChars, pCharacter);
    if (childIndex >= 0) {
      return OrderedListOfChildren[childIndex];
    }
    else {
      return null;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected final StringTrieNode putChild(byte pCharacter, 
  StringTrieNode pStringTrieNode) {
    
    StringTrieNode oldValue = null;
    int i;

    for (i = 0; i < SizeOfOrderedLists; i++) {
      if (OrderedListOfChars[i] >= pCharacter) break;
    }

    if (i < SizeOfOrderedLists && OrderedListOfChars[i] == pCharacter) {
      // replace existing child; SizeOfOrderedLists unchanged
      oldValue = OrderedListOfChildren[i];
      OrderedListOfChildren[i] = pStringTrieNode;
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
      OrderedListOfChars[i] = pCharacter;
      OrderedListOfChildren[i] = pStringTrieNode;
      SizeOfOrderedLists++;
    }

    return oldValue;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  // Shift up all elements in OrderedListOfChars and OrderedListOfChildren 
  // one index from specified position, presumably to make room for new 
  // element. The element at the given position is also shifted up.

  protected void shiftUp(int pIndex) {
    
    if (pIndex < 0 || pIndex > SizeOfOrderedLists) {
      return;
    }
    // this.ensureSpace(SizeOfOrderedLists + 1);
    incrementSizeOfArrays();
    System.arraycopy(OrderedListOfChars, pIndex, OrderedListOfChars, 
    pIndex + 1, SizeOfOrderedLists - pIndex);
    System.arraycopy(OrderedListOfChildren, pIndex, OrderedListOfChildren, 
    pIndex + 1, SizeOfOrderedLists - pIndex);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
//  protected void ensureSpace(int pSize) {
//    
//    if (pSize > OrderedListOfChars.length) {
//      int newsize = Math.max(2 * OrderedListOfChars.length, pSize);
//      byte[] newchars = new byte[newsize];
//      StringTrieNode[] newchildren = new StringTrieNode[newsize];
//      System.arraycopy(OrderedListOfChars, 0, newchars, 0, 
//      OrderedListOfChars.length);
//      System.arraycopy(OrderedListOfChildren, 0, newchildren, 0,
//      OrderedListOfChildren.length);
//      OrderedListOfChars = newchars;
//      OrderedListOfChildren = newchildren;
//    }
//    
//  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void incrementSizeOfArrays() {
    
    // array must only contain defined values in order to employ
    // Arrays.binarySearch().
    int newsize = OrderedListOfChars.length + 1;
    byte[] newchars = new byte[newsize];
    StringTrieNode[] newchildren = new StringTrieNode[newsize];
    System.arraycopy(OrderedListOfChars, 0, newchars, 0,
    OrderedListOfChars.length);
    System.arraycopy(OrderedListOfChildren, 0, newchildren, 0,
    OrderedListOfChildren.length);
    OrderedListOfChars = newchars;
    OrderedListOfChildren = newchildren;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void addElements(Dictionary pDictionary, byte[] pKey, 
  int pKeyIndex) {
    
    byte character;
    StringTrieNode stringTrieNode;

    if (Value != null) {
      pDictionary.put(new String(pKey, 0, 0, pKeyIndex), Value);
    }

    for (int i = 0; i < SizeOfOrderedLists; i++) {
      character = OrderedListOfChars[i];
      stringTrieNode = OrderedListOfChildren[i];
      if (stringTrieNode != null) {
        pKey[pKeyIndex] = character;
        stringTrieNode.addElements(pDictionary, pKey, pKeyIndex + 1);
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void dumpNode(PrintStream pOutputStream, String pKeyUpToHere) {
    
    pOutputStream.println(this + " {");
    pOutputStream.println("  Key = " + pKeyUpToHere);
    pOutputStream.println("  SizeOfOrderedLists = " + SizeOfOrderedLists 
    + '(' + OrderedListOfChars.length +')');
    pOutputStream.println("  Value = " + Value);
    pOutputStream.println("  [");
    for (int i = 0; i < SizeOfOrderedLists; i++) {
      pOutputStream.println("    " + (char) OrderedListOfChars[i] + ": " 
      + OrderedListOfChildren[i]);
    }
    pOutputStream.println("  ]");
    pOutputStream.println("}");
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void dump(PrintStream pOutputStream, byte[] pKey, int pKeyIndex) {
    
    dumpNode(pOutputStream, new String(pKey, 0, 0, pKeyIndex));

    for (int i = 0; i < SizeOfOrderedLists; i++) {
      if (OrderedListOfChildren[i] != null) {
        pKey[pKeyIndex] = OrderedListOfChars[i];
        OrderedListOfChildren[i].dump(pOutputStream, pKey, pKeyIndex + 1);
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void print(PrintStream pOutputStream, byte[] pKey, int pKeyIndex) {
    
    if (Value != null) {
      pOutputStream.println("  " + new String(pKey, 0, 0, pKeyIndex) + "=" 
      + Value + ", ");
    }

    for (int i = 0; i < SizeOfOrderedLists; i++) {
      if (OrderedListOfChildren[i] != null) {
        pKey[pKeyIndex] = OrderedListOfChars[i];
        OrderedListOfChildren[i].print(pOutputStream, pKey, pKeyIndex + 1);
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