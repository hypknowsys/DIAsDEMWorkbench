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
 * @author Karsten Winkler
 */
  
public class KStringTokenizer {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private int CurrentPosition;
  private int NewPosition;
  private int MaxPosition;
  private String StringToBeTokenized;
  private String Delimiters;
  private boolean ReturnDelimiters;
  private boolean DelimitersChanged;
  private int CurrentBeginIndex = -1;  // inclusive
  private int CurrentEndIndex = -1;  // exclusive
  
  private char MaxDelimiterChar;
  
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

  public KStringTokenizer(String pStringToBeTokenized, String pDelimiters, 
  boolean pReturnDelimiters) {
    
    CurrentPosition = 0;
    NewPosition = -1;
    DelimitersChanged = false;
    StringToBeTokenized = pStringToBeTokenized;
    MaxPosition = StringToBeTokenized.length();
    Delimiters = pDelimiters;
    ReturnDelimiters = ReturnDelimiters;
    this.setMaxDelimChar();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public KStringTokenizer(String pStringToBeTokenized, String pDelimiters) {
    
    this(pStringToBeTokenized, pDelimiters, false);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public KStringTokenizer(String pStringToBeTokenized) {
    
    this(pStringToBeTokenized, " \t\n\r\f", false);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  // must be called after calling nextToken()
  public int getCurrentBeginIndex() { 
    return CurrentBeginIndex; }

  // must be called after calling nextToken()
  public int getCurrentEndIndex() { 
    return CurrentEndIndex; }

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

  public boolean hasMoreTokens() {
    
    NewPosition = skipDelimiters(CurrentPosition);
    
    return (NewPosition < MaxPosition);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String nextToken() {
    
    CurrentPosition = (NewPosition >= 0 && !DelimitersChanged) ?
    NewPosition : skipDelimiters(CurrentPosition);
    
    DelimitersChanged = false;
    NewPosition = -1;
    
    if (CurrentPosition >= MaxPosition) {
      throw new NoSuchElementException();
    }
    int start = CurrentPosition;
    CurrentPosition = scanToken(CurrentPosition);
    CurrentBeginIndex = start;
    CurrentEndIndex = CurrentPosition;
    
    return StringToBeTokenized.substring(start, CurrentPosition);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String nextToken(String delim) {
    
    Delimiters = delim;    
    DelimitersChanged = true;    
    setMaxDelimChar();
    
    return nextToken();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int countTokens() {
    
    int count = 0;
    int currpos = CurrentPosition;
    while (currpos < MaxPosition) {
      currpos = skipDelimiters(currpos);
      if (currpos >= MaxPosition) {
        break;
      }
      currpos = scanToken(currpos);
      count++;
    }
    
    return count;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private int skipDelimiters(int startPos) {
    
    if (Delimiters == null)
      throw new NullPointerException();
    
    int position = startPos;
    while (!ReturnDelimiters && position < MaxPosition) {
      char c = StringToBeTokenized.charAt(position);
      if ((c > MaxDelimiterChar) || (Delimiters.indexOf(c) < 0)) {
        break;
      }
      position++;
    }
    return position;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setMaxDelimChar() {
    
    if (Delimiters == null) {
      MaxDelimiterChar = 0;
      return;
    }
    
    char m = 0;
    for (int i = 0; i < Delimiters.length(); i++) {
      char c = Delimiters.charAt(i);
      if (m < c) {
        m = c;
      }
    }
    MaxDelimiterChar = m;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private int scanToken(int startPos) {
    
    int position = startPos;
    while (position < MaxPosition) {
      char c = StringToBeTokenized.charAt(position);
      if ((c <= MaxDelimiterChar) && (Delimiters.indexOf(c) >= 0)) {
        break;
      }
      position++;
    }
    if (ReturnDelimiters && (startPos == position)) {
      char c = StringToBeTokenized.charAt(position);
      if ((c <= MaxDelimiterChar) && (Delimiters.indexOf(c) >= 0)) {
        position++;
      }
    }
    
    return position;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String[] args) {

    String test = "0 23 567 8";
    KStringTokenizer tokenizer = new KStringTokenizer(test);   
    String token = null;
    int beginIndex = -1;
    int endIndex = -1;
    
    System.out.println(test);
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      System.out.println(">" + token + "<, begin index: " 
      + tokenizer.getCurrentBeginIndex() + ", end index: "
      + tokenizer.getCurrentEndIndex() + ", substring(" +
      tokenizer.getCurrentBeginIndex() + "," +
      tokenizer.getCurrentEndIndex() + ") = " +
      test.substring(tokenizer.getCurrentBeginIndex(),
      tokenizer.getCurrentEndIndex() ) );
    }

  }

}