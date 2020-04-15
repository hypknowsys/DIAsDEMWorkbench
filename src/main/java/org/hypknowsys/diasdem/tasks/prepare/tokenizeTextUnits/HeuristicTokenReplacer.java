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

package org.hypknowsys.diasdem.tasks.prepare.tokenizeTextUnits;

import java.util.*;
import org.hypknowsys.misc.util.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class HeuristicTokenReplacer implements TokenReplacer {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  // key "' ll" and value "will"
  private StringTrie SearchTokensTrie = null;  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient String[] Tokens = null; 
  private transient int NumberOfTokens = 0;   
  private transient String LookUpResult = null; 
  private transient String SuccessfulLookUpResult = null; 
  private transient StringBuffer ResultStringBuffer = null;
  private transient int EndIndex = -1;
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient StringTokenizer TmpStringTokenizer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public HeuristicTokenReplacer(String[] pSearchTokensAsString, 
  String[] pReplaceTokensAsString) {

    if (pSearchTokensAsString.length != pReplaceTokensAsString.length) {
      System.err.println("HeuristicTokenReplacer.constructor: "
      + "Arrays pSearchTokensAsString and pReplaceTokensAsString "
      + "must have same length.");
      return;
    } 
    SearchTokensTrie = new StringTrie();
    for (int i = 0; i < pSearchTokensAsString.length; i++) {
      if (!Tools.stringIsNullOrEmpty(pSearchTokensAsString[i])) {
        SearchTokensTrie.put(new String(pSearchTokensAsString[i]), 
        new String(pReplaceTokensAsString[i]));
      }
    }
    
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
  /* ########## interface TextNormalizer methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String replaceTokens(String pText) {
    
    // search for tokens to be replaced and replace them
    TmpStringTokenizer = new StringTokenizer(pText);
    Tokens = new String[TmpStringTokenizer.countTokens()];
    NumberOfTokens = 0;
    while (TmpStringTokenizer.hasMoreTokens()) {
      Tokens[NumberOfTokens++] = TmpStringTokenizer.nextToken();
    }
    
    ResultStringBuffer = new StringBuffer(pText.length() + 10000);
    for (int i = 0; i < NumberOfTokens; i++) {
       LookUpResult = (String)SearchTokensTrie
      .getAndCheckForSubsequentBlankSpace(Tokens[i]);
      if (LookUpResult != null) {
        if (SearchTokensTrie.previousMatchPrecedesBlankSpace()) {
          // go on: token is a search string, but it might
          // be the prefix of a multi-token search string
          EndIndex = i;
          SuccessfulLookUpResult = new String(LookUpResult);
          TmpStringBuffer = new StringBuffer(1000);
          TmpStringBuffer.append(Tokens[i]);
          for (int j = i + 1; j < NumberOfTokens && SearchTokensTrie
          .previousMatchPrecedesBlankSpace(); j++) {
            TmpStringBuffer.append(" ");
            TmpStringBuffer.append(Tokens[j]);
            LookUpResult = (String)SearchTokensTrie
            .getAndCheckForSubsequentBlankSpace(TmpStringBuffer.toString());
            if (LookUpResult != null) {
              EndIndex = j;
              SuccessfulLookUpResult = new String(LookUpResult);
            }
          }
          ResultStringBuffer.append(SuccessfulLookUpResult);
          i = EndIndex;  // continue with next token 
        }
        else {
          // stop here: token is a valid, single-token search string
          ResultStringBuffer.append(LookUpResult);
        }
      }
      else if (SearchTokensTrie.previousMatchPrecedesBlankSpace()) {
        // go on: token is not a valid search string, but it might
        // be the prefix of a multi-token search string
        EndIndex = -1;
        SuccessfulLookUpResult = "";
        TmpStringBuffer = new StringBuffer(1000);
        TmpStringBuffer.append(Tokens[i]);
        for (int j = i + 1; j < NumberOfTokens && SearchTokensTrie
          .previousMatchPrecedesBlankSpace(); j++) {
          TmpStringBuffer.append(" ");
          TmpStringBuffer.append(Tokens[j]);
          LookUpResult = (String)SearchTokensTrie
          .getAndCheckForSubsequentBlankSpace(TmpStringBuffer.toString());
          if (LookUpResult != null) {
            EndIndex = j;
            SuccessfulLookUpResult = new String(LookUpResult);
          }
        }
        if (EndIndex >= 0) {
          ResultStringBuffer.append(SuccessfulLookUpResult);
          i = EndIndex;  // continue with next token 
        }
        else {
          // token is not a complete search string 
          ResultStringBuffer.append(Tokens[i]);
        }
      }
      else {
        // token is neither a complete search string nor the prefix of
        // a search string
        ResultStringBuffer.append(Tokens[i]);
      }
      ResultStringBuffer.append(" ");
    }  // for:all tokens
    
    return ResultStringBuffer.toString().trim();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

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
  
    String[] searchFor = new String[4];
    searchFor[0] = "' ll"; searchFor[1] = "wont"; searchFor[2] = "replaced";
    searchFor[3] = "";
    String[] replace = new String[4];
    replace[0] = "will"; replace[1] = "will not"; replace[2] = "re-placed";
    replace[3] = "";
    
    String line = "We ' ll be replaced , you wont win !";
    
    HeuristicTokenReplacer myHeuristicTokenReplacer = 
    new HeuristicTokenReplacer(searchFor, replace);
    
    System.out.println(">" + myHeuristicTokenReplacer.replaceTokens(line) 
    + "<");
  
  }
  
}