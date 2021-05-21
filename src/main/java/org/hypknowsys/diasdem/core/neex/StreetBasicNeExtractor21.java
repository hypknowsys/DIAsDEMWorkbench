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

package org.hypknowsys.diasdem.core.neex;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class StreetBasicNeExtractor21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityExtractorParameter NeTaskParameter = null;
  private String InputLine = null;
  private int NumberOfTokens = 0;  // in InputLine
  private StringBuffer OutputLine = null;
  
  // Das ist die ABC GmbH & Co. KG ( Musterstra�e 1 , 12345 Berlin ) .
  //                                 ^
  private ArrayList AllStartIndexes = null;
  // Das ist die ABC GmbH & Co. KG ( Musterstra�e 1 , 12345 Berlin ) .
  //                                              ^
  private ArrayList AllEndIndexes = null;
  // Das ist die ABC GmbH & Co. KG ( Musterstra�e 1 , 12345 Berlin ) .
  //                                 <<1>>
  private ArrayList AllPlaceholders = null;
  
  // [<<1>> ist Nachname von <<2>>]
  private String[] Tokens_NeAsPlaceholder = null;
  // [<<surname>> ist Nachname von <<forename>>]
  private String[] Tokens_NeAsPossibleType = null;
  // [Winkler ist Nachname von Karsten]
  private String[] Tokens_NeAsToken = null;
  
  private ArrayList PrunedAllStartIndexes = null;
  private ArrayList PrunedAllEndIndexes = null;
  
  private boolean DebuggingMode = false;
  private TextFile DebuggingOutput = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer Street = null;
  private transient StringBuffer StreetCandidate = null;
  private transient String StreetExceptionLookUpResult = null;
  private transient boolean IsValidStreetAffix = false;
  private transient boolean LookForStreetSuffix = false;
  private transient boolean IsValidStreetPrefix = false;
  private transient boolean Merged = false;
  private transient boolean Dropped = false;
  
  private transient StringTokenizer MyTokenizer = null;
  private transient String MyToken = null;
  private transient int NextPlaceholderID = -1;
  private transient String NextPlaceholder = null;
  private transient int StartIndex = -1;
  private transient int EndIndex = -1;
  private transient int NewStartIndex = -1;
  private transient int NewEndIndex = -1;
  
  private transient Pattern StreetAffixRegex = null;
  private transient Matcher StreetAffixMatcher = null;
  
  private transient Pattern StreetPrefixRegex = null;
  private transient Matcher StreetPrefixMatcher = null;
  
  private transient Pattern StreetExclusionRegex = null;
  private transient Matcher StreetExclusionMatcher = null;
  
  private transient int MinTokenInStreet = 0;
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public StreetBasicNeExtractor21(
  NamedEntityExtractorParameter pNeTaskParameter) {
    
    NeTaskParameter = pNeTaskParameter;
    DebuggingMode = NeTaskParameter.createDebuggingFiles();
    if (DebuggingMode) {
      Template debuggingOutputHeader = new Template(Tools
      .stringFromTextualSystemResource("html/"
      + "HtmlFile_HeaderTemplate.html"));
      debuggingOutputHeader.addValue("${Title}", "NEEX 2.1: Streets "
      + " Extracted by StreetBasicNeExtractor");
      DebuggingOutput = new TextFile(new File(Tools.ensureTrailingSlash(
      NeTaskParameter.getDebuggingFileDirectory()) + "Neex21_Streets.html"));
      DebuggingOutput.open();
      DebuggingOutput.setFirstLine(debuggingOutputHeader.insertValues());
      DebuggingOutput.close();
    }
    
    StreetAffixRegex = Pattern.compile(NeTaskParameter
    .getStreetAffixTokenRegex());
    StreetPrefixRegex = Pattern.compile(NeTaskParameter
    .getStreetPrefixTokenRegex());
    StreetExclusionRegex = Pattern.compile(NeTaskParameter
    .getStreetExclusionRegex());
    MinTokenInStreet = NeTaskParameter.getMinTokenInStreet();
    
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
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String replaceStreetBasicNamedEntities(String pInputLine,
  NamedEntityOwner pCurrentNeOwner, BasicNeDictionary21 pBasicNeDictionary) {
    
    InputLine = pInputLine;
    OutputLine = null;
    StartIndex = -1;
    EndIndex = -1;
    AllStartIndexes = new ArrayList();
    AllEndIndexes = new ArrayList();
    AllPlaceholders = new ArrayList();
    
    MyTokenizer = new StringTokenizer(InputLine);
    NumberOfTokens = MyTokenizer.countTokens();
    Tokens_NeAsPlaceholder = new String[NumberOfTokens];
    // these two arrays are not used in the current implementation
    // Tokens_NeAsPossibleType = new String[NumberOfTokens];
    // Tokens_NeAsToken = new String[NumberOfTokens];
    
    MyToken = null;
    int counter = 0;
    while (MyTokenizer.hasMoreTokens()) {
      MyToken = MyTokenizer.nextToken();
      Tokens_NeAsPlaceholder[counter] = MyToken;
      // if (NamedEntity.isPlaceholder(MyToken)) {
      //   Tokens_NeAsPossibleType[counter] = pCurrentNeOwner.getNamedEntity(
      //   NamedEntity.getNamedEntityIndex(MyToken))
      //   .getPossibleTypesPlaceholder();
      //   Tokens_NeAsToken[counter] = pCurrentNeOwner.getNamedEntity(
      //   NamedEntity.getNamedEntityIndex(MyToken)).getToken();
      // }
      // else {
      //   Tokens_NeAsPossibleType[counter] = MyToken;
      //   Tokens_NeAsToken[counter] = MyToken;
      // }
      counter++;
    }
    
    // 1. look for all street candidates by searching in list of known street
    // exceptions and in the array of known street suffixes
    for (int i = 0; i < NumberOfTokens; i++) {
      
      LookForStreetSuffix = true;
      StreetExceptionLookUpResult = (String)pBasicNeDictionary
      .StreetExceptionsTrie.getAndCheckForSubsequentBlankSpace(
      Tokens_NeAsPlaceholder[i]);
      if (StreetExceptionLookUpResult != null) {
        if (pBasicNeDictionary.StreetExceptionsTrie
        .previousMatchPrecedesBlankSpace()) {
          // go on: token is a valid street exception, but it might
          // be the prefix of a multi-token street exception
          EndIndex = i;
          StreetCandidate = new StringBuffer(1000);
          StreetCandidate.append(Tokens_NeAsPlaceholder[i]);
          for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
          .StreetExceptionsTrie.previousMatchPrecedesBlankSpace(); j++) {
            StreetCandidate.append(" ");
            StreetCandidate.append(Tokens_NeAsPlaceholder[j]);
            StreetExceptionLookUpResult = (String)pBasicNeDictionary
            .StreetExceptionsTrie.getAndCheckForSubsequentBlankSpace(
            StreetCandidate.toString());
            if (StreetExceptionLookUpResult != null) {
              EndIndex = j;
            }
          }
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(EndIndex));
          i = EndIndex;  // continue with next token
        }
        else {
          // stop here: token is a valid, single-token street exception
          EndIndex = -1;
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(i));
        }
        LookForStreetSuffix = false;
      }
      else if (pBasicNeDictionary.StreetExceptionsTrie
      .previousMatchPrecedesBlankSpace()) {
        // go on: token is not a valid street exception, but it might
        // be the prefix of a multi-token street exception
        EndIndex = -1;
        StreetCandidate = new StringBuffer(1000);
        StreetCandidate.append(Tokens_NeAsPlaceholder[i]);
        for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
        .StreetExceptionsTrie.previousMatchPrecedesBlankSpace(); j++) {
          StreetCandidate.append(" ");
          StreetCandidate.append(Tokens_NeAsPlaceholder[j]);
          StreetExceptionLookUpResult = (String)pBasicNeDictionary
          .StreetExceptionsTrie.getAndCheckForSubsequentBlankSpace(
          StreetCandidate.toString());
          if (StreetExceptionLookUpResult != null) {
            EndIndex = j;
          }
        }
        if (EndIndex >= i)  {
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(EndIndex));
          i = EndIndex;  // continue with next token
          LookForStreetSuffix = false;
        }
      }
      
      if (LookForStreetSuffix) {
        // go on: token might end with a street suffix: 'stra�e' etc.
        if (this.endsWithStreetSuffix(Tokens_NeAsPlaceholder[i],
        pBasicNeDictionary) && this.isCapitalizedToken(
        Tokens_NeAsPlaceholder[i])) {
          // In einer Stra�e wird gebaut .
          // In einer Musterstra�e wird gebaut .
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(i));
        }
      }
      
    }  // for:all tokens
    
    if (AllEndIndexes.size() > 0) {
      
      // 2. try to extend each street candidate by searching for tokens that
      // are valid street affixes ('1' or '1 - 2' or '12 a' or '34 b')
      for (int i = 0; i < AllEndIndexes.size(); i++) {
        EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
        NewEndIndex = EndIndex;
        // start searching at token after current end of street vandidate
        if (EndIndex >= 0) {
          for (int k = 1; (EndIndex + k) < NumberOfTokens; k++) {
            IsValidStreetAffix = this.isValidStreetAffixToken(
            Tokens_NeAsPlaceholder[EndIndex + k]);
            if (IsValidStreetAffix) {
              NewEndIndex = EndIndex + k;
            }
            else {
              k = NumberOfTokens;  // break k
            }
          }  // street affixes can contain more than 1 token
          // replace previous end of organization
          if (NewEndIndex > EndIndex) {
            AllEndIndexes.set(i, new Integer(NewEndIndex));
          }
        }
      }
      
      // 3. try to extend each street candidate by searching for tokens that
      // are valid street prefixes ('Neue' or 'Gr.')
      for (int i = 0; i < AllStartIndexes.size(); i++) {
        StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
        NewStartIndex = Integer.MAX_VALUE;
        // start searching at token before current start of street
        if (StartIndex > 0) {
          NewStartIndex = StartIndex;
          for (int k = 1; (StartIndex - k) >= 0; k++) {
            IsValidStreetPrefix = this.isValidStreetPrefixToken(
            Tokens_NeAsPlaceholder[StartIndex - k]);
            if (IsValidStreetPrefix) {
              NewStartIndex = StartIndex - k;
            }
            else {
              k = StartIndex + 1;  // break k
            }
          }  // street prefixes can contain more than 1 token
          // replace previous end of organization
          if (NewStartIndex < StartIndex) {
            AllStartIndexes.set(i, new Integer(NewStartIndex));
          }
        }
      }
      
      // 4. merge consecutive street candidates
      Merged = true;
      while (Merged) {
        Merged = false;
        for (int i = 0; i < AllStartIndexes.size() - 1; i++) {
          EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
          StartIndex = ((Integer)AllStartIndexes.get(i + 1)).intValue();
          if ((EndIndex + 1) == StartIndex) {
            EndIndex = ((Integer)AllEndIndexes.get(i + 1)).intValue();
            AllEndIndexes.set(i, new Integer(EndIndex));
            AllStartIndexes.remove(i + 1);
            AllEndIndexes.remove(i + 1);
            Merged = true;
            i = AllStartIndexes.size();  // break
          }
        }
      }
      
      // 5. drop street candidates whose number of tokens does not match
      // threshold
      Dropped = true;
      while (Dropped) {
        Dropped = false;
        for (int i = 0; i < AllStartIndexes.size(); i++) {
          StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
          EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
          if ((1 + EndIndex - StartIndex) < MinTokenInStreet) {
            AllStartIndexes.remove(i);
            AllEndIndexes.remove(i);
            Dropped = true;
            i = AllStartIndexes.size() + 1;  // break
          }
        }
      }
      
      PrunedAllStartIndexes = new ArrayList();
      PrunedAllEndIndexes = new ArrayList();
      for (int k = 0; k < AllStartIndexes.size(); k++) {
        if (AllEndIndexes.get(k) != null) {
          StartIndex = ((Integer)AllStartIndexes.get(k)).intValue();
          EndIndex = ((Integer)AllEndIndexes.get(k)).intValue();
          Street = new StringBuffer();
          for (int j = StartIndex; j <= EndIndex; j++) {
            if (j > StartIndex) {
              Street.append(" ");
            }
            Street.append(Tokens_NeAsPlaceholder[j]);
          }
          // 7. exclude street candidate that match exclusion regex
          // register remaining streets with NamedEntityOwner
          if (!this.isStreetToBeExcluded(Street.toString())) {
            PrunedAllStartIndexes.add(AllStartIndexes.get(k));
            PrunedAllEndIndexes.add(AllEndIndexes.get(k));
            NextPlaceholderID = pCurrentNeOwner.getNextNamedEntityIndex();
            NextPlaceholder = NamedEntity.createPlaceholder(NextPlaceholderID);
            AllPlaceholders.add(NextPlaceholder);
            pCurrentNeOwner.addNamedEntity(new NamedEntity(NextPlaceholderID,
            Street.toString(), NextPlaceholder, NamedEntity.STREET));
          }
        }
      }
      AllStartIndexes = null;
      AllEndIndexes = null;
      
      // System.out.println("###1###" + pInputLine);
      // System.out.print("AllStartIndexes.size() = "
      // + AllStartIndexes.size() + "; Contents: ");
      // for (int j = 0; j < AllStartIndexes.size(); j++) {
      //   System.out.print((Integer)AllStartIndexes.get(j) + "; ");
      // }
      // System.out.print("\n");
      // System.out.print("AllEndIndexes.size() = "
      // + AllEndIndexes.size() + "; Contents: ");
      // for (int j = 0; j < AllEndIndexes.size(); j++) {
      //   System.out.print((Integer)AllEndIndexes.get(j) + "; ");
      // }
      // System.out.print("\n");
      
      // 8. replace streets with their placeholder string
      OutputLine = new StringBuffer(InputLine.length());
      int[] placeholdersStartIndex = new int[PrunedAllStartIndexes.size()];
      for (int i = 0; i < placeholdersStartIndex.length ; i++) {
        placeholdersStartIndex[i] = ((Integer)PrunedAllStartIndexes
        .get(i)).intValue();
      }
      int nextPlaceholderStartIndex = 0;
      for (int i = 0; i < NumberOfTokens; i++) {
        if (nextPlaceholderStartIndex < placeholdersStartIndex.length
        && i == placeholdersStartIndex[nextPlaceholderStartIndex]) {
          if (DebuggingMode) {
            this.appendDebuggingFile("<font class=\"redBold\">"
            + pCurrentNeOwner.getNamedEntity(NamedEntity.getNamedEntityIndex(
            (String)AllPlaceholders.get(nextPlaceholderStartIndex)))
            .getToken() + "</font> ", false);
          }
          OutputLine.append((String)AllPlaceholders.get(
          nextPlaceholderStartIndex));
          OutputLine.append(" ");
          i = ((Integer)PrunedAllEndIndexes.get(
          nextPlaceholderStartIndex)).intValue();
          nextPlaceholderStartIndex++;
        }
        else {
          if (DebuggingMode) {
            this.appendDebuggingFile(Tokens_NeAsPlaceholder[i] + " ", true);
          }
          OutputLine.append(Tokens_NeAsPlaceholder[i]);
          OutputLine.append(" ");
        }
      }
      // System.out.println("\n---");
      // System.out.println(InputLine);
      // System.out.println(AllStartIndexes);
      // System.out.println(AllEndIndexes_LastToken);
      // System.out.println(OutputLine.toString());
      
    }  // if (AllEndIndexes_FirstToken.size() > 0)
    
    // there is no street in InputLine
    if (OutputLine == null) {
      if (DebuggingMode) {
        this.appendDebuggingFile(Tools.insertHtmlEntityReferences(InputLine)
        + "<p>", false);
      }
      OutputLine = new StringBuffer(InputLine);
    }
    else {
      if (DebuggingMode) {
        this.appendDebuggingFile("<p>", false);
      }
    }
    return OutputLine.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void appendDebuggingFile(String pString, boolean pReplaceEntities) {
    
    DebuggingOutput.open();
    if (pReplaceEntities) {
      DebuggingOutput.setNextLine(Tools.insertHtmlEntityReferences(pString));
    }
    else {
      DebuggingOutput.setNextLine(pString);
    }
    DebuggingOutput.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean endsWithStreetSuffix(String pString,
  BasicNeDictionary21 pBasicNeDictionary) {
    
    if (pString == null || pString.length() == 0) {
      return false;
    }
    else {
      TmpString = pString.toLowerCase();
      for (int i = 0; i < pBasicNeDictionary.StreetSuffixes.length; i++) {
        if (TmpString.endsWith(pBasicNeDictionary.StreetSuffixes[i])) {
          return true;
        }
      }
    }
    
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isValidStreetAffixToken(String pString) {
    
    if (pString == null || pString.length() == 0) {
      return false;
    }
    else {
      StreetAffixMatcher = StreetAffixRegex.matcher(pString);
      if (StreetAffixMatcher.matches()) {
        return true;
      }
    }
    
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isValidStreetPrefixToken(String pString) {
    
    if (pString == null || pString.length() == 0) {
      return false;
    }
    else {
      StreetPrefixMatcher = StreetPrefixRegex.matcher(pString);
      if (StreetPrefixMatcher.matches()) {
        return true;
      }
    }
    
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isStreetToBeExcluded(String pString) {
    
    if (pString == null || pString.length() == 0) {
      return false;
    }
    else {
      StreetExclusionMatcher = StreetExclusionRegex.matcher(pString);
      if (StreetExclusionMatcher.matches()) {
        return true;
      }
    }
    
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isCapitalizedToken(String pString) {
    
    if (pString == null || pString.length() == 0) {
      return false;
    }
    else if (Character.isUpperCase(pString.charAt(0))) {
      return true;
    }
    
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}