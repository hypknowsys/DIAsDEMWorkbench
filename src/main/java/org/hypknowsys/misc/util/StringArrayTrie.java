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
  
public class StringArrayTrie extends StringArrayTrieNode {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */


  protected int Size = 0;  
  protected int MaxDepth = 1; 

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

  public StringArrayTrie() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public boolean previousMatchIsPrefixOfKey() {
    return PreviousMatchIsPrefixOfKey; }

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

  public Object put(String[] pKey, Object value) {

    if (pKey.length > MaxDepth) {
      MaxDepth = pKey.length;
    }
    Object oldValue = super.put(pKey, value, 0);
    if (oldValue == null) {
      Size++;
    }
    
    return oldValue;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object remove(String[] pKey) {
    
    Object oldValue = super.remove(pKey, 0);
    if (oldValue != null) {
      Size--;
    }
    
    return oldValue;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public Object get(String[] pKey) {
    
    if (pKey == null) {
      return null;
    }
    else {
      return super.get(pKey);
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public Object getAndCheckIfKeyIsPrefixOfOtherKey(String[] pKey) {
    
    PreviousMatchIsPrefixOfKey = false;
    if (pKey == null) {
      return null;
    }
    else {
      return super.getAndCheckIfKeyIsPrefixOfOtherKey(pKey);
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KeyValuePair getBestMatch(String[] pKey) {

    if (pKey == null) {
      return null;
    }
    else {
      return super.getBestMatch(pKey);
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public Dictionary contents() {
    
    Hashtable hashtable = new Hashtable();
    super.addElements(hashtable, new String[MaxDepth], 0);
    
    return hashtable;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public Enumeration keys() {
    
    return contents().keys();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public Enumeration elements() {
    
    return contents().elements();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public int size() {
    
    return Size;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isEmpty() {
    
    return (size() == 0);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void dump(PrintStream pOutputStream) {
    
    String[] maxKey = new String[MaxDepth];
    super.dump(pOutputStream, maxKey, 0);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void print(PrintStream pOutputStream) {
    
    pOutputStream.println("{");
    String[] maxKey = new String[MaxDepth];
    super.print(pOutputStream, maxKey, 0);
    pOutputStream.println("}");
    
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

  public static void main(String args[]) {
  
    StringArrayTrie trie = new StringArrayTrie();
    
    String[] compositeNE = {"<<organization>>", "mit", "Sitz", "in", "<<place>>"};
    String[] shortKey = {"Short", "Key"};
    String[] longLongKey = {"Long", "Long", "Key"};
    String[] shortOnly = {"Short", "Only"};
    String[] shor = {"Shor"};
    String[] keyDoesNotExist = {"Key", "Does", "Not", "Exist"};
    trie.put(compositeNE,  "Value associated with 'compositeNE'");
    trie.put(shortKey,  "Value associated with 'ShortKey'");
    trie.put(longLongKey,  "Value associated with 'LongLongKey'");
    trie.put(shortOnly,  "Value associated with 'Short'");
    
    System.out.println("stringTrie.get(\"compositeNE\")=" 
    + trie.get(compositeNE));
    System.out.println("stringTrie.get(\"ShortKey\")=" 
    + trie.get(shortKey));
    System.out.println("stringTrie.get(\"LongLongKey\")=" 
    + trie.get(longLongKey));
    System.out.println("stringTrie.get(\"KeyDoesNotExist\")=" 
    + trie.get(keyDoesNotExist));
    System.out.println("stringTrie.get(\"ShortK\")=" 
    + trie.get(shortOnly));
    System.out.println("stringTrie.getBestMatch(\"ShortK\")=" 
    + trie.getBestMatch(shortOnly));
    System.out.println("stringTriegetBestMatchget(\"Shor\")=" 
    + trie.getBestMatch(shor));
    
  }
  
}