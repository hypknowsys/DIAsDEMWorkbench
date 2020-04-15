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
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class PlaceBasicNeExtractor21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityExtractorParameter NeTaskParameter = null;
  private String InputLine = null;
  private int NumberOfTokens = 0;  // in InputLine
  private StringBuffer OutputLine = null;
  
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin / Mitte .
  //                                           ^
  private ArrayList AllStartIndexes = null;
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin / Mitte .
  //                                                    ^
  private ArrayList AllEndIndexes = null;
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin / Mitte .
  //                                                    ^
  private ArrayList AllEndIndexes_LastStrongAffix = null;
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin / Mitte .
  //                                           <<1>>
  private ArrayList AllPlaceholders = null;
  
  // [<<1>> ist Nachname von <<2>>]
  private String[] Tokens_NeAsPlaceholder = null;
  // [<<surname>> ist Nachname von <<forename>>]
  private String[] Tokens_NeAsPossibleType = null;
  // [Winkler ist Nachname von Karsten]
  private String[] Tokens_NeAsToken = null;
  
  private ArrayList PrunedAllStartIndexes = null;
  private ArrayList PrunedAllEndIndexes = null;
  private String[] StartPatternPath = null;
  
  private boolean DebuggingMode = false;
  private TextFile DebuggingOutput = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient NamedEntity NeLookUpResult = null;
  private transient Object LookUpResult = null;
  private transient StringTokenizer MyTokenizer = null;
  private transient String MyToken = null;
  private transient StringBuffer Place = null;
  private transient StringBuffer PlaceAffix = null;
  private transient int NextPlaceholderID = -1;
  private transient String NextPlaceholder = null;
  private transient int StartIndex = -1;
  private transient int EndIndex = -1;
  private transient int EndIndex_LastStrongAffix = -1;
  private transient int NewEndIndex = -1;
  private transient int PrelimNewEndIndex = -1;
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public PlaceBasicNeExtractor21(
  NamedEntityExtractorParameter pNeTaskParameter) {
    
    NeTaskParameter = pNeTaskParameter;
    DebuggingMode = NeTaskParameter.createDebuggingFiles();
    if (DebuggingMode) {
      Template debuggingOutputHeader = new Template(Tools
      .stringFromTextualSystemResource("org/hypknowsys/diasdem/resources/html/"
      + "HtmlFile_HeaderTemplate.html"));
      debuggingOutputHeader.addValue("${Title}", "NEEX 2.1: Places "
      + " Extracted by PlaceBasicNeExtractor");
      DebuggingOutput = new TextFile(new File(Tools.ensureTrailingSlash(
      NeTaskParameter.getDebuggingFileDirectory()) + "Neex21_Places.html"));
      DebuggingOutput.open();
      DebuggingOutput.setFirstLine(debuggingOutputHeader.insertValues());
      DebuggingOutput.close();
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
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String replacePlaceBasicNamedEntities(String pInputLine,
  NamedEntityOwner pCurrentNeOwner, BasicNeDictionary21 pBasicNeDictionary) {
    
    InputLine = pInputLine;
    OutputLine = null;
    StartIndex = -1;
    EndIndex = -1;
    AllStartIndexes = new ArrayList();
    AllEndIndexes = new ArrayList();
    AllEndIndexes_LastStrongAffix = new ArrayList();
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
    
    // 1. look for all place candidates by searching in list of known places
    // such as Berlin, Frankfurt am Main, Baden Baden, Beispiel - Dorf
    for (int i = 0; i < NumberOfTokens; i++) {
      NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
      .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[i]);
      if (NeLookUpResult != null && NeLookUpResult.isPossibleType(
      NamedEntity.PLACE)) {
        if (pBasicNeDictionary.NeDictionary.previousMatchPrecedesBlankSpace()) {
          // go on: token is a valid place, but it might
          // be the prefix of a multi-token place
          EndIndex = i;
          PlaceAffix = new StringBuffer(1000);
          PlaceAffix.append(Tokens_NeAsPlaceholder[i]);
          for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
          .NeDictionary.previousMatchPrecedesBlankSpace(); j++) {
            PlaceAffix.append(" ");
            PlaceAffix.append(Tokens_NeAsPlaceholder[j]);
            NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
            .getAndCheckForSubsequentBlankSpace(PlaceAffix.toString());
            if (NeLookUpResult != null && NeLookUpResult.isPossibleType(
            NamedEntity.PLACE)) {
              EndIndex = j;
            }
          }
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(EndIndex));
          AllEndIndexes_LastStrongAffix.add(new Integer(EndIndex));
          i = EndIndex;  // continue with next token
        }
        else {
          // stop here: token is a valid, single-token place
          EndIndex = -1;
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(i));
          AllEndIndexes_LastStrongAffix.add(new Integer(i));
        }
      }
      else if (pBasicNeDictionary.NeDictionary
      .previousMatchPrecedesBlankSpace()) {
        // go on: token is not a valid place, but it might
        // be the prefix of a multi-token place
        EndIndex = -1;
        PlaceAffix = new StringBuffer(1000);
        PlaceAffix.append(Tokens_NeAsPlaceholder[i]);
        for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
        .NeDictionary.previousMatchPrecedesBlankSpace(); j++) {
          PlaceAffix.append(" ");
          PlaceAffix.append(Tokens_NeAsPlaceholder[j]);
          NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
          .getAndCheckForSubsequentBlankSpace(PlaceAffix.toString());
          if (NeLookUpResult != null && NeLookUpResult.isPossibleType(
          NamedEntity.PLACE)) {
            EndIndex = j;
          }
        }
        if (EndIndex >= 0) {
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(EndIndex));
          AllEndIndexes_LastStrongAffix.add(new Integer(EndIndex));
          i = EndIndex;  // continue with next token
        }
      }
    }  // for:all tokens
    
    StartPatternPath = null;
    if (AllEndIndexes.size() > 0) {
      
      // 2. exclude one tokens place which follow a forename or a title
      boolean isPlace = true;
      for (int i = 0; i < AllEndIndexes.size(); i++) {
        isPlace = true;
        StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
        EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
        // place is preceded by forename or title
        if (StartIndex > 0) {
          NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
          .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
          StartIndex - 1]);
          if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
          NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
          NamedEntity.TITLE))) {
            isPlace = false;
          }
          else if (StartIndex > 1 && (Tokens_NeAsPlaceholder[StartIndex - 1]
          .equals("-") || (NeLookUpResult != null && NeLookUpResult
          .isPossibleType(NamedEntity.MIDDLE_INITIAL)))) {
            NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
            .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
            StartIndex - 2]);
            if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
            NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
            NamedEntity.TITLE))) {
              isPlace = false;
            }
          }
        }
        // one token place could be forename or surname
        if (isPlace && (EndIndex - StartIndex) == 0)  {
          NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
          .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
          StartIndex]);
          if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
          NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
          NamedEntity.SURNAME))) {
            if (StartIndex > 0) {
              NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
              .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
              StartIndex - 1]);
              if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
              NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
              NamedEntity.SURNAME) || NeLookUpResult.isPossibleType(
              NamedEntity.TITLE))) {
                isPlace = false;
              }
              else if (StartIndex > 1 && Tokens_NeAsPlaceholder[StartIndex - 1]
              .equals("-")) {
                NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
                .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
                StartIndex - 2]);
                if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
                NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
                NamedEntity.SURNAME) || NeLookUpResult.isPossibleType(
                NamedEntity.TITLE)) && !NeLookUpResult.isPossibleType(
                NamedEntity.PLACE)) {
                  isPlace = false;
                }
              }
              else if (StartIndex > 1 && NeLookUpResult != null
              && NeLookUpResult.isPossibleType(NamedEntity.MIDDLE_INITIAL)) {
                NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
                .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
                StartIndex - 2]);
                if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
                NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
                NamedEntity.SURNAME) || NeLookUpResult.isPossibleType(
                NamedEntity.TITLE))) {
                  isPlace = false;
                }
              }
            }
            if (isPlace && (EndIndex + 1) < NumberOfTokens) {
              NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
              .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
              EndIndex + 1]);
              if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
              NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
              NamedEntity.SURNAME) || NeLookUpResult.isPossibleType(
              NamedEntity.TITLE))) {
                isPlace = false;
              }
              else if (isPlace && (EndIndex + 2) < NumberOfTokens
              && Tokens_NeAsPlaceholder[EndIndex + 1].equals("-")) {
                NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
                .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
                EndIndex + 2]);
                if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
                NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
                NamedEntity.SURNAME) || NeLookUpResult.isPossibleType(
                NamedEntity.TITLE)) && !NeLookUpResult.isPossibleType(
                NamedEntity.PLACE)) {
                  // Karl - Heinz
                  // && pBasicNeDictionary.PlaceAffixTrie
                  // .get(Tokens_NeAsPlaceholder[EndIndex + 2]) == null) {
                  isPlace = false;
                }
              }
              else if (isPlace && (EndIndex + 2) < NumberOfTokens
              && NeLookUpResult != null && NeLookUpResult.isPossibleType(
              NamedEntity.MIDDLE_INITIAL)) {
                NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
                .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[
                EndIndex + 2]);
                if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
                NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
                NamedEntity.SURNAME) || NeLookUpResult.isPossibleType(
                NamedEntity.TITLE))) {
                  isPlace = false;
                }
              }
            }
          }
        }
        if (!isPlace) {
          AllEndIndexes.set(i, new Integer(-1));
          AllEndIndexes_LastStrongAffix.set(i, new Integer(-1));
        }
      }  // look backward from all endings
      
      
      // 3. try to extend each place by searching for place affixes
      for (int i = 0; i < AllEndIndexes.size(); i++) {
        EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
        NewEndIndex = EndIndex;
        PrelimNewEndIndex = EndIndex;
        // start searching at token after current end of place
        // note that place affixes can be onstructed by various
        // weak and strong place affixes
        if (EndIndex >= 0) {
          do {
            NewEndIndex = PrelimNewEndIndex;
            PlaceAffix = new StringBuffer(1000);
            for (int k = 1; (NewEndIndex + k) < NumberOfTokens; k++) {
              if (k > 1) {
                PlaceAffix.append(" ");
              }
              PlaceAffix.append(Tokens_NeAsPlaceholder[NewEndIndex + k]);
              // System.out.println(k + " - " + PlaceAffix.toString());
              // System.out.println("EndIndex=" + EndIndex + " k=" + k
              // + " +++ " + pBasicNeDictionary.PlaceAffixTrie
              // .getAndCheckForSubsequentBlankSpace(PlaceAffix.toString())
              // + "; "  + pBasicNeDictionary.PlaceAffixTrie
              // .previousMatchPrecedesBlankSpace());
              LookUpResult = pBasicNeDictionary.PlaceAffixTrie
              .getAndCheckForSubsequentBlankSpace(PlaceAffix.toString());
              if (LookUpResult != null) {
                PrelimNewEndIndex = NewEndIndex + k;
                if (BasicNeDictionary21.STRONG_PLACE_AFFIX.equals(
                (Integer)LookUpResult)) {
                  AllEndIndexes_LastStrongAffix.set(i,
                  new Integer(PrelimNewEndIndex));
                }
              }
              if (!pBasicNeDictionary.PlaceAffixTrie
              .previousMatchPrecedesBlankSpace()) {
                k = NumberOfTokens;  // break k
              }
            }  // affix pattern can contain more than 1 token
            // affix pattern can contain more than 1 affix
          } while (PrelimNewEndIndex > NewEndIndex);
          // replace previous end of organization
          if (NewEndIndex > EndIndex) {
            AllEndIndexes.set(i, new Integer(NewEndIndex));
          }
        }
      }
      
      // System.out.println("###1###" + pInputLine);
      // System.out.print("AllStartIndexes.size() = "
      // + AllStartIndexes.size() + "; Contents: ");
      // for (int i = 0; i < AllStartIndexes.size(); i++) {
      //   System.out.print((Integer)AllStartIndexes.get(i) + "; ");
      // }
      // System.out.print("\n");
      // System.out.print("AllEndIndexes_LastStrongAffix.size() = "
      // + AllEndIndexes_LastStrongAffix.size() + "; Contents: ");
      // for (int i = 0; i < AllEndIndexes_LastStrongAffix.size(); i++) {
      //   System.out.print((Integer)AllEndIndexes_LastStrongAffix.get(i)
      //   + "; ");
      // }
      // System.out.print("\n");
      // System.out.print("AllEndIndexes.size() = "
      // + AllEndIndexes.size() + "; Contents: ");
      // for (int i = 0; i < AllEndIndexes.size(); i++) {
      //   System.out.print((Integer)AllEndIndexes.get(i) + "; ");
      // }
      // System.out.print("\n");
      
      // 4. check for places whose place affix is extended by another place
      for (int i = 0; i < AllEndIndexes.size(); i++) {
        StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
        EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
        EndIndex_LastStrongAffix = ((Integer)AllEndIndexes_LastStrongAffix
        .get(i)).intValue();
        if (EndIndex >= 0) {
          for (int k = i + 1; k < AllEndIndexes.size(); k++) {
            if (((Integer)AllStartIndexes.get(k)).compareTo(
            new Integer(EndIndex + 1)) <= 0
            && ((Integer)AllEndIndexes.get(k)).compareTo(
            new Integer(EndIndex)) >= 0) {
              AllEndIndexes.set(i, (Integer)AllEndIndexes.get(k));
              AllEndIndexes_LastStrongAffix.set(i,
              (Integer)AllEndIndexes_LastStrongAffix.get(k));
              EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
              EndIndex_LastStrongAffix = (
              (Integer)AllEndIndexes_LastStrongAffix.get(i)).intValue();
              AllEndIndexes.set(k, new Integer(-1));
              AllEndIndexes_LastStrongAffix.set(k, new Integer(-1));
              break;
            }
          }
          if (EndIndex_LastStrongAffix < EndIndex) {
            AllEndIndexes.set(i, (Integer)AllEndIndexes_LastStrongAffix.get(i));
            EndIndex = EndIndex_LastStrongAffix;
          }
          if (EndIndex < 0 || StartIndex > EndIndex) {
            AllEndIndexes.set(i, null);
            AllEndIndexes_LastStrongAffix.set(i, null);
          }
        }
        else {
          AllEndIndexes.set(i, null);
          AllEndIndexes_LastStrongAffix.set(i, null);
        }
      }  // look foreward from all endings
      
      // 5. delete places that are not preceded by a place indicator
      if (pBasicNeDictionary.NumberOfPlaceIndicators > 0) {
        // System.out.println("### " + pInputLine);
        for (int i = 0; i < AllEndIndexes.size(); i++) {
          StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
          if (AllEndIndexes.get(i) != null && StartIndex > 0) {
            
            EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
            isPlace = (StartIndex == 0 ? true : false);
            // valid place if preceded by another named entity
            if (NamedEntity.isPlaceholder(Tokens_NeAsPlaceholder
            [StartIndex - 1])) {
              isPlace = true;
            }
            else {
              // preceding place indicator?
              for (int j = StartIndex - 1; j >= 0; j--) {
                for (int k = 0; k < pBasicNeDictionary.MaxPlaceIndicatorLength
                && k <= j; k++) {
                  StartPatternPath = new String[k + 1];
                  for (int l = 0; (l < (k + 1)) && (l <= j); l++) {
                    StartPatternPath[l] = Tokens_NeAsPlaceholder[j - l]
                    .toLowerCase();
                    // System.out.print(StartPatternPath[l] + " ");
                  }
                  LookUpResult = pBasicNeDictionary.PlaceIndicatorTrie
                  .getAndCheckIfKeyIsPrefixOfOtherKey(StartPatternPath);
                  // System.out.println(StartPatternPath.length +
                  // " j=" + j + " k=" + k + " +++ " + LookUpResult);
                  if (LookUpResult != null) {
                    isPlace = true;
                    k = pBasicNeDictionary.MaxPlaceIndicatorLength;  // break k
                    j = -1; // break j
                  }
                  else if (!pBasicNeDictionary.PlaceIndicatorTrie
                  .previousMatchIsPrefixOfKey()) {
                    k = pBasicNeDictionary.MaxPlaceIndicatorLength;  // break k
                    j = -1; // break j
                  }
                }  // start pattern can contain more than 1 token
              }  // look backward from a single beginning
            }
            if (!isPlace && this.isGermanPostalCode(Tokens_NeAsPlaceholder
            [StartIndex - 1])) {
              isPlace = true;
              AllStartIndexes.set(i, new Integer(StartIndex - 1));
            }
            if (!isPlace) {
              AllEndIndexes.set(i, null);
              AllEndIndexes_LastStrongAffix.set(i, null);
            }
            else {
              // look for German postal code like "( 12345 ) Berlin"
              if ((StartIndex - 3) >= 0
              && Tokens_NeAsPlaceholder[StartIndex - 3].equals("(")
              && Tokens_NeAsPlaceholder[StartIndex - 1].equals(")")
              && this.isGermanPostalCode(Tokens_NeAsPlaceholder[StartIndex - 2])
              ) {
                AllStartIndexes.set(i, new Integer(StartIndex - 3));
              }
              // look for German postal code like "Berlin ( 12345 ;"
              if ((StartIndex + 3) < Tokens_NeAsPlaceholder.length
              && Tokens_NeAsPlaceholder[StartIndex + 1].equals("(")
              && (Tokens_NeAsPlaceholder[StartIndex + 3].equals(";")
              || Tokens_NeAsPlaceholder[StartIndex + 3].equals(","))
              && this.isGermanPostalCode(Tokens_NeAsPlaceholder[StartIndex + 2])
              ) {
                AllEndIndexes.set(i, new Integer(StartIndex + 3));
              }
            }
            // System.out.println("isPlace=" + isPlace);
          }
        }  // look backward from all beginnings
      }
      
      // System.out.println("###2###" + pInputLine);
      // System.out.print("AllStartIndexes.size() = "
      // + AllStartIndexes.size() + "; Contents: ");
      // for (int i = 0; i < AllStartIndexes.size(); i++) {
      //   System.out.print((Integer)AllStartIndexes.get(i) + "; ");
      // }
      // System.out.print("\n");
      // System.out.print("AllEndIndexes.size() = "
      // + AllEndIndexes.size() + "; Contents: ");
      // for (int i = 0; i < AllEndIndexes.size(); i++) {
      //   System.out.print((Integer)AllEndIndexes.get(i) + "; ");
      // }
      // System.out.print("\n");
      
      PrunedAllStartIndexes = new ArrayList();
      PrunedAllEndIndexes = new ArrayList();
      for (int k = 0; k < AllStartIndexes.size(); k++) {
        if (AllEndIndexes.get(k) != null) {
          PrunedAllStartIndexes.add(AllStartIndexes.get(k));
          PrunedAllEndIndexes.add(AllEndIndexes.get(k));
          StartIndex = ((Integer)AllStartIndexes.get(k)).intValue();
          EndIndex = ((Integer)AllEndIndexes.get(k)).intValue();
          // register new place with NamedEntityOwner
          Place = new StringBuffer();
          for (int j = StartIndex; j <= EndIndex; j++) {
            Place.append(Tokens_NeAsPlaceholder[j]);
            Place.append(" ");
          }
          NextPlaceholderID = pCurrentNeOwner.getNextNamedEntityIndex();
          NextPlaceholder = NamedEntity.createPlaceholder(NextPlaceholderID);
          AllPlaceholders.add(NextPlaceholder);
          pCurrentNeOwner.addNamedEntity(new NamedEntity(NextPlaceholderID,
          Place.toString().trim(), NextPlaceholder, NamedEntity.PLACE));
        }
      }
      
      // System.out.println("###" + pInputLine);
      // System.out.print("PrunedAllStartIndexes.size() = "
      // + PrunedAllStartIndexes.size() + "; Contents: ");
      // for (int i = 0; i < PrunedAllStartIndexes.size(); i++) {
      //   System.out.print((Integer)PrunedAllStartIndexes.get(i) + "; ");
      // }
      // System.out.print("\n");
      // System.out.print("PrunedAllEndIndexes.size() = "
      // + PrunedAllEndIndexes.size() + "; Contents: ");
      // for (int i = 0; i < PrunedAllEndIndexes.size(); i++) {
      //   System.out.print((Integer)PrunedAllEndIndexes.get(i) + "; ");
      // }
      //System.out.print("\n");
      // System.out.print("AllPlaceholders.size() = "
      // + AllPlaceholders.size() + "; Contents: ");
      // for (int i = 0; i < AllPlaceholders.size(); i++) {
      //   System.out.print((String)AllPlaceholders.get(i) + "; ");
      // }
      // System.out.print("\n");
      
      // 6. replace places with their placeholder string
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
    
    // there is no organization in InputLine
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
  
  private boolean isGermanPostalCode(String pString) {
    
    if (pString == null || pString.length() < 4 || pString.length() > 5) {
      return false;
    }
    else {
      for (int i = 0; i < pString.length(); i++) {
        if (!Character.isDigit(pString.charAt(i))) {
          return false;
        }
      }
    }
    
    return true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}