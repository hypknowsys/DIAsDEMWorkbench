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
  
public class StringTrie extends StringTrieNode {
  
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

  public StringTrie() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean previousMatchPrecedesBlankSpace() {
    return PreviousMatchPrecedesBlankSpace; }

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

  public Object put(String pKey, Object value) {
    
    byte[] key = Tools.getStringAsArrayOfByte(pKey);
    if (key.length > MaxDepth) {
      MaxDepth = key.length;
    }
    Object oldValue = super.put(key, value, 0);
    if (oldValue == null) {
      Size++;
    }
    
    return oldValue;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public Object put(byte[] pKey, Object value) {
    
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

  public Object remove(String pKey) {
    
    Object oldValue = super.remove(Tools.getStringAsArrayOfByte(pKey), 0);
    if (oldValue != null) {
      Size--;
    }
    
    return oldValue;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public Object remove(byte[] pKey) {
    
    Object oldValue = super.remove(pKey, 0);
    if (oldValue != null) {
      Size--;
    }
    
    return oldValue;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public Object get(String pKey) {
    
    if (pKey == null) {
      return null;
    }
    else {
      return super.get(Tools.getStringAsArrayOfByte(pKey));
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public Object getAndCheckForSubsequentBlankSpace(String pKey) {
    
    PreviousMatchPrecedesBlankSpace = false;
    if (pKey == null) {
      return null;
    }
    else {
      return super.getAndCheckForSubsequentBlankSpace(
      Tools.getStringAsArrayOfByte(pKey));
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public Object get(byte[] pKey) {
    
    if (pKey == null) {
      return null;
    }
    else {
      return super.get(pKey);
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public KeyValuePair getBestMatch(String pKey) {
    
    PreviousMatchPrecedesBlankSpace = false;
    if (pKey == null) {
      return null;
    }
    else {
      return super.getBestMatch(Tools.getStringAsArrayOfByte(pKey));
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public KeyValuePair getBestMatchAndCheckForSubsequentBlankSpace(String pKey) {
    
    PreviousMatchPrecedesBlankSpace = false;
    if (pKey == null) {
      return null;
    }
    else {
      return super.getBestMatchAndCheckForSubsequentBlankSpace(
      Tools.getStringAsArrayOfByte(pKey));
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public KeyValuePair getBestMatch(byte[] pKey) {
    
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
    super.addElements(hashtable, new byte[MaxDepth], 0);
    
    return hashtable;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public Enumeration keys() {
    
    return this.contents().keys();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public Enumeration elements() {
    
    return this.contents().elements();
    
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
    
    byte[] maxKey = new byte[MaxDepth];
    super.dump(pOutputStream, maxKey, 0);
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public void print(PrintStream pOutputStream) {
    
    pOutputStream.println("{");
    byte[] maxKey = new byte[MaxDepth];
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
  
    StringTrie stringTrie = new StringTrie();
    
    stringTrie.put("ShortKey",  "Value associated with 'ShortKey'");
    stringTrie.put("LongLongKey",  "Value associated with 'LongLongKey'");
    stringTrie.put("Short",  "Value associated with 'Short'");
    
    // stringTrie.dump(System.out);
    
    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());
    System.out.println("stringTrie.get(\"Geschäftsführer\")=" 
    + stringTrie.get("Geschäftsführer"));
    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());

    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());
    System.out.println("stringTrie.get(\"ShortKey\")=" 
    + stringTrie.get("ShortKey"));
    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());

    System.out.println("stringTrie.get(\"LongLongKey\")=" 
    + stringTrie.get("LongLongKey"));
    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());

    System.out.println("stringTrie.get(\"KeyDoesNotExist\")=" 
    + stringTrie.get("KeyDoesNotExist"));
    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());

    System.out.println("stringTrie.get(\"ShortK\")=" 
    + stringTrie.get("ShortK"));
    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());

    System.out.println("stringTrie.getBestMatch(\"ShortK\")=" 
    + stringTrie.getBestMatch("ShortK"));
    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());
    
    System.out.println("stringTriegetBestMatchget(\"Shor\")=" 
    + stringTrie.getBestMatch("Shor"));
    System.out.println("PreviousGetHasBeenPartialMatch="
    + stringTrie.previousMatchPrecedesBlankSpace());
    
  }
  
}