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

public class PersonNameBasicNeExtractor21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityExtractorParameter NeTaskParameter = null;
  private String InputLine = null;
  private int NumberOfTokens = 0;  // in InputLine
  private StringBuffer OutputLine = null;
  
  // Das ist Herr Dr. Hans M. von Mustermann jr. von ACB Technologies .
  //             ^
  private ArrayList AllStartIndexes = null;
  // Das ist Herr Dr. Hans M. von Mustermann jr. von ACB Technologies .
  //                                          ^
  private ArrayList AllEndIndexes = null;
  // Das ist Herr Dr. Hans M. von Mustermann jr. von ACB Technologies .
  //                                         ^
  private ArrayList AllEndIndexes_LastStrongAffix = null;
  // Das ist Herr Dr. Hans M. von Mustermann jr. von ACB Technologies .
  //              TF++SA
  private ArrayList AllTypeSummaries = null;
  // Das ist Herr Dr. Hans M. von Mustermann jr. von ACB Technologies .
  //               <<1>>.........................
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
  private String[] AffixPatternPath = null;
  
  private boolean DebuggingMode = false;
  private TextFile DebuggingOutput = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient NamedEntity NeLookUpResult = null;
  private transient Object LookUpResult = null;
  private transient StringTokenizer MyTokenizer = null;
  private transient String MyToken = null;
  private transient StringBuffer PersonName = null;
  private transient StringBuffer PersonAffix = null;
  private transient int NextPlaceholderID = -1;
  private transient String NextPlaceholder = null;
  private transient StringBuffer TypeSummary = null;
  private transient String PrelimTypeSummary = null;
  private transient String NextTypeSummary = null;
  private transient String TypeSummaryString = null;
  private transient int StartIndex = -1;
  private transient int NextStartIndex = -1;
  private transient int EndIndex = -1;
  private transient int NextEndIndex = -1;
  private transient int EndIndex_LastStrongAffix = -1;
  private transient int NextEndIndex_LastStrongAffix = -1;
  private transient int NewEndIndex = -1;
  private transient int PrevEndIndex = -1;
  private transient int PrelimNewEndIndex = -1;
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public PersonNameBasicNeExtractor21(
  NamedEntityExtractorParameter pNeTaskParameter) {
    
    NeTaskParameter = pNeTaskParameter;
    DebuggingMode = NeTaskParameter.createDebuggingFiles();
    if (DebuggingMode) {
      Template debuggingOutputHeader = new Template(Tools
      .stringFromTextualSystemResource("org/hypknowsys/diasdem/resources/html/"
      + "HtmlFile_HeaderTemplate.html"));
      debuggingOutputHeader.addValue("${Title}", "NEEX 2.1: Person Names "
      + " Extracted by PersonNameBasicNeExtractor");
      DebuggingOutput = new TextFile(new File(Tools.ensureTrailingSlash(
      NeTaskParameter.getDebuggingFileDirectory())
      + "Neex21_PersonNames.html"));
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
  
  public String replacePersonNameBasicNamedEntities(String pInputLine,
  NamedEntityOwner pCurrentNeOwner, BasicNeDictionary21 pBasicNeDictionary) {
    
    InputLine = pInputLine;
    OutputLine = null;
    StartIndex = -1;
    EndIndex = -1;
    AllStartIndexes = new ArrayList();
    AllEndIndexes = new ArrayList();
    AllEndIndexes_LastStrongAffix = new ArrayList();
    AllPlaceholders = new ArrayList();
    AllTypeSummaries = new ArrayList();
    
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
      PrelimTypeSummary = null;
      NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
      .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[i]);
      if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
      NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
      NamedEntity.SURNAME) || NeLookUpResult.isPossibleType(
      NamedEntity.TITLE))) {
        if (NeLookUpResult.isPossibleType(NamedEntity.TITLE)
        && !NeLookUpResult.isPossibleType(NamedEntity.SURNAME)
        && !NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
          PrelimTypeSummary = "T";
        }
        else if (NeLookUpResult.isPossibleType(NamedEntity.FORENAME)
        && !NeLookUpResult.isPossibleType(NamedEntity.SURNAME)) {
          PrelimTypeSummary = "F";
        }
        else if (NeLookUpResult.isPossibleType(NamedEntity.SURNAME)
        && !NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
          PrelimTypeSummary = "S";
        }
        else if (NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
          PrelimTypeSummary = "f";
        }
        else {
          PrelimTypeSummary = "?";
        }
        if (pBasicNeDictionary.NeDictionary.previousMatchPrecedesBlankSpace()) {
          // go on: token is a valid place, but it might
          // be the prefix of a multi-token place
          EndIndex = i;
          PersonAffix = new StringBuffer(1000);
          PersonAffix.append(Tokens_NeAsPlaceholder[i]);
          for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
          .NeDictionary.previousMatchPrecedesBlankSpace(); j++) {
            PersonAffix.append(" ");
            PersonAffix.append(Tokens_NeAsPlaceholder[j]);
            NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
            .getAndCheckForSubsequentBlankSpace(PersonAffix.toString());
            if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
            NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
            NamedEntity.SURNAME) || NeLookUpResult.isPossibleType(
            NamedEntity.TITLE))) {
              EndIndex = j;
              if (NeLookUpResult.isPossibleType(NamedEntity.TITLE)
              && !NeLookUpResult.isPossibleType(NamedEntity.SURNAME)
              && !NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
                PrelimTypeSummary = "T";
              }
              else if (NeLookUpResult.isPossibleType(NamedEntity.FORENAME)
              && !NeLookUpResult.isPossibleType(NamedEntity.SURNAME)) {
                PrelimTypeSummary = "F";
              }
              else if (NeLookUpResult.isPossibleType(NamedEntity.SURNAME)
              && !NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
                PrelimTypeSummary = "S";
              }
              else if (NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
                PrelimTypeSummary = "f";
              }
              else {
                PrelimTypeSummary = "?";
              }
            }
          }
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(EndIndex));
          AllEndIndexes_LastStrongAffix.add(new Integer(EndIndex));
          AllTypeSummaries.add(new StringBuffer(PrelimTypeSummary));
          i = EndIndex;  // continue with next token
        }
        else {
          // stop here: token is a valid, single-token person name
          EndIndex = -1;
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(i));
          AllEndIndexes_LastStrongAffix.add(new Integer(i));
          AllTypeSummaries.add(new StringBuffer(PrelimTypeSummary));
        }
      }
      else if (pBasicNeDictionary.NeDictionary
      .previousMatchPrecedesBlankSpace()) {
        // go on: token is not a valid place, but it might
        // be the prefix of a multi-token person name
        EndIndex = -1;
        PersonAffix = new StringBuffer(1000);
        PersonAffix.append(Tokens_NeAsPlaceholder[i]);
        for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
        .NeDictionary.previousMatchPrecedesBlankSpace(); j++) {
          PersonAffix.append(" ");
          PersonAffix.append(Tokens_NeAsPlaceholder[j]);
          NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
          .getAndCheckForSubsequentBlankSpace(PersonAffix.toString());
          if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
          NamedEntity.FORENAME) || NeLookUpResult.isPossibleType(
          NamedEntity.SURNAME)  || NeLookUpResult.isPossibleType(
          NamedEntity.TITLE))) {
            EndIndex = j;
            if (NeLookUpResult.isPossibleType(NamedEntity.TITLE)
            && !NeLookUpResult.isPossibleType(NamedEntity.SURNAME)
            && !NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
              PrelimTypeSummary = "T";
            }
            else if (NeLookUpResult.isPossibleType(NamedEntity.FORENAME)
            && !NeLookUpResult.isPossibleType(NamedEntity.SURNAME)) {
              PrelimTypeSummary = "F";
            }
            else if (NeLookUpResult.isPossibleType(NamedEntity.SURNAME)
            && !NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
              PrelimTypeSummary = "S";
            }
            else if (NeLookUpResult.isPossibleType(NamedEntity.FORENAME)) {
              PrelimTypeSummary = "f";
            }
            else {
              PrelimTypeSummary = "?";
            }
          }
        }
        if (EndIndex >= 0) {
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(EndIndex));
          AllEndIndexes_LastStrongAffix.add(new Integer(EndIndex));
          AllTypeSummaries.add(new StringBuffer(PrelimTypeSummary));
          i = EndIndex;  // continue with next token
        }
      }
    }  // for:all tokens
    
    // System.out.println("###00###" + pInputLine);
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
    // System.out.print("AllTypeSummaries.size() = "
    // + AllTypeSummaries.size() + "; Contents: ");
    // for (int i = 0; i < AllTypeSummaries.size(); i++) {
    //   System.out.print(((StringBuffer)AllTypeSummaries.get(i)).toString()
    //   + "; ");
    // }
    // System.out.print("\n");
    
    if (AllEndIndexes.size() > 0) {
      
      // 3. try to extend each place by searching for name affixes (, jun.)
      // and middle initials (von, van de, de la, Graf zu) or -
      // System.out.println("### " + pInputLine);
      boolean enforceLoop = false;
      for (int i = 0; i < AllEndIndexes.size(); i++) {
        EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
        NewEndIndex = EndIndex;
        PrelimNewEndIndex = EndIndex;
        if (i + 1 >= AllStartIndexes.size()) {
          NextStartIndex = NumberOfTokens;
        }
        else {
          NextStartIndex = ((Integer)AllStartIndexes.get(i + 1)).intValue();
        }
        StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
        if (i - 1 < 0) {
          PrevEndIndex = -1;
        }
        else {
          PrevEndIndex = ((Integer)AllEndIndexes.get(i - 1)).intValue();
        }
        TypeSummary = (StringBuffer)AllTypeSummaries.get(i);
        // start searching at token after current end of person (title,
        // forename or surname)
        if (EndIndex >= 0) {
          do {
            NewEndIndex = PrelimNewEndIndex;
            for (int k = 1; (NewEndIndex + k) < NumberOfTokens
            && (NewEndIndex + k) < NextStartIndex; k++) {
              PersonAffix = new StringBuffer(1000);
              for (int l = 0; l < k; l++) {
                if (l > 0) {
                  PersonAffix.append(" ");
                }
                PersonAffix.append(Tokens_NeAsPlaceholder[NewEndIndex + 1 + l]);
                // System.out.println(l + " - " + PersonAffix.toString());
              }
              // System.out.println(" EndIndex="
              // + EndIndex + " k=" + k + " +++ " + pBasicNeDictionary
              // .NeDictionary.getAndCheckForSubsequentBlankSpace(PersonAffix
              // .toString() + "; " + pBasicNeDictionary.NeDictionary
              // .previousMatchPrecedesBlankSpace()));
              NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
              .getAndCheckForSubsequentBlankSpace(PersonAffix.toString());
              if (NeLookUpResult != null && (NeLookUpResult.isPossibleType(
              NamedEntity.NAME_AFFIX) || NeLookUpResult.isPossibleType(
              NamedEntity.MIDDLE_INITIAL))) {
                PrelimNewEndIndex = NewEndIndex + k;
                if (NeLookUpResult.isPossibleType(NamedEntity.NAME_AFFIX)) {
                  AllEndIndexes_LastStrongAffix.set(i,
                  new Integer(PrelimNewEndIndex));
                }
                TypeSummary.append("+");
              }
              else if (k == 1 && PersonAffix.toString().equals("-")) {
                PrelimNewEndIndex = NewEndIndex + k;
                TypeSummary.append("-");
                enforceLoop = true;
              }
              else if (k == 1 && (TypeSummary.indexOf("F") >= 0
              || TypeSummary.indexOf("f") >= 0 || TypeSummary.toString()
              .equals("T")) && this.isProbablyASurname(
              PersonAffix.toString(), pBasicNeDictionary)) {
                PrelimNewEndIndex = NewEndIndex + k;
                AllEndIndexes_LastStrongAffix.set(i,
                new Integer(PrelimNewEndIndex));
                TypeSummary.append("s");
                enforceLoop = true;
              }
              if (enforceLoop && !pBasicNeDictionary.NeDictionary
              .previousMatchPrecedesBlankSpace()) {
                k = NumberOfTokens;  // break k
              }
              else {
                enforceLoop = false;
              }
            }  // affix pattern can contain more than 1 token
            // affix pattern can contain more than 1 affix
          } while (PrelimNewEndIndex > NewEndIndex);
          // replace previous end of organization
          if (NewEndIndex > EndIndex) {
            AllEndIndexes.set(i, new Integer(NewEndIndex));
            AllTypeSummaries.set(i, new StringBuffer(TypeSummary.toString()));
          }
          // try to extend surnames by looking backwards, for example
          // Der Gesch�ftsf�hrer von Mustermann Martin hat ... (be cautious!)
          if (StartIndex > 0 && (PrevEndIndex < (StartIndex - 2)
          || PrevEndIndex == -1) && TypeSummary.toString().startsWith("S")) {
            PersonAffix = new StringBuffer(Tokens_NeAsPlaceholder[
            StartIndex - 1]);
            NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
            .get(PersonAffix.toString());
            if (NeLookUpResult != null && NeLookUpResult.isPossibleType(
            NamedEntity.MIDDLE_INITIAL)) {
              AllStartIndexes.set(i, new Integer(StartIndex - 1));
            }
          }
        }
      }
      
      // System.out.println("###0###" + pInputLine);
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
      // System.out.print("AllTypeSummaries.size() = "
      // + AllTypeSummaries.size() + "; Contents: ");
      // for (int i = 0; i < AllTypeSummaries.size(); i++) {
      //   System.out.print(((StringBuffer)AllTypeSummaries.get(i)).toString()
      //   + "; ");
      // }
      // System.out.print("\n");
      
      // 4. merge subsequent person names into a single person name
      // by forward parsing
      boolean mergeSuccessful = false;
      for (int i = 0; i < AllEndIndexes.size(); i++) {
        StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
        EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
        EndIndex_LastStrongAffix = ((Integer)AllEndIndexes_LastStrongAffix
        .get(i)).intValue();
        TypeSummary = (StringBuffer)AllTypeSummaries.get(i);
        if (EndIndex >= 0) {
          mergeSuccessful = true;
          for (int k = i + 1; k < AllEndIndexes.size() && mergeSuccessful;
          k++) {
            NextStartIndex = ((Integer)AllStartIndexes.get(k)).intValue();
            NextEndIndex = ((Integer)AllEndIndexes.get(k)).intValue();
            NextTypeSummary = ((StringBuffer)AllTypeSummaries.get(k))
            .toString();
            if ((NextStartIndex <= (EndIndex + 1)
            && NextEndIndex >= EndIndex)
            ||
            (NextStartIndex == (EndIndex + 2)
            && Tokens_NeAsPlaceholder[EndIndex + 1].equals(",")
            && (TypeSummary.toString().toLowerCase().indexOf("f") < 0
            || Tokens_NeAsPlaceholder[Math.max(StartIndex - 1, 0)].equals(";")
            || Tokens_NeAsPlaceholder[Math.max(StartIndex - 1, 0)].equals(":"))
            && NextTypeSummary.indexOf("S") < 0
            && NextTypeSummary.indexOf("T") < 0)) {
              AllEndIndexes.set(i,
              AllEndIndexes.get(k));
              AllEndIndexes_LastStrongAffix.set(i,
              AllEndIndexes_LastStrongAffix.get(k));
              TypeSummary.append(NextTypeSummary);
              AllTypeSummaries.set(i, new StringBuffer(TypeSummary.toString()));
              EndIndex = NextEndIndex;
              EndIndex_LastStrongAffix = (
              (Integer)AllEndIndexes_LastStrongAffix.get(i)).intValue();
              AllEndIndexes.set(k, new Integer(-1));
              AllEndIndexes_LastStrongAffix.set(k, new Integer(-1));
              AllTypeSummaries.set(k, new StringBuffer(""));
            }
            else {
              mergeSuccessful = false;
            }
          }
          if (EndIndex_LastStrongAffix < EndIndex) {
            EndIndex = EndIndex_LastStrongAffix;
            AllEndIndexes.set(i, new Integer(EndIndex_LastStrongAffix));
          }
          if (EndIndex < 0) {
            AllEndIndexes.set(i, new Integer(EndIndex));
          }
        }
      }  // look foreward from all endings
      
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
      // System.out.print("AllTypeSummaries.size() = "
      // + AllTypeSummaries.size() + "; Contents: ");
      // for (int i = 0; i < AllTypeSummaries.size(); i++) {
      //   System.out.print(((StringBuffer)AllTypeSummaries.get(i)).toString()
      //   + "; ");
      // }
      // System.out.print("\n");
      
      // 5. delete single token person names that are not preceded by a
      // person name indicator; delete all person names that follow or
      // precede a strong negative person name indicator or that precede
      // a number (i.e., house numbers)
      boolean isPerson = false;
      if (pBasicNeDictionary.NumberOfPersonIndicators > 0) {
        // System.out.println("### " + pInputLine);
        for (int i = 0; i < AllEndIndexes.size(); i++) {
          StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
          EndIndex = ((Integer)AllEndIndexes.get(i)).intValue();
          EndIndex_LastStrongAffix = ((Integer)AllEndIndexes_LastStrongAffix
          .get(i)).intValue();
          TypeSummary = (StringBuffer)AllTypeSummaries.get(i);
          TypeSummaryString = TypeSummary.toString();
          isPerson = true;
          if (TypeSummaryString.equals("T")) {
            isPerson = false;
          }
          else if (EndIndex > 0 && StartIndex > 0
          && (TypeSummaryString.length() == 1
          || (TypeSummaryString.toLowerCase().indexOf("f") < 0
          && TypeSummaryString.indexOf("T") < 0))) {
            isPerson = false;
            // preceding person name indicator?
            for (int j = StartIndex - 1; j >= 0; j--) {
              for (int k = 0; k < pBasicNeDictionary.MaxPersonIndicatorLength
              && k <= j; k++) {
                StartPatternPath = new String[k + 1];
                for (int l = 0; (l < (k + 1)) && (l <= j); l++) {
                  StartPatternPath[l] = Tokens_NeAsPlaceholder[j - l]
                  .toLowerCase();
                  // System.out.print(StartPatternPath[l] + " ");
                }
                LookUpResult = pBasicNeDictionary.PersonIndicatorTrie
                .getAndCheckIfKeyIsPrefixOfOtherKey(StartPatternPath);
                // System.out.println(StartPatternPath.length +
                // " j=" + j + " k=" + k + " +++ " + LookUpResult);
                if (LookUpResult != null && BasicNeDictionary21
                .STRONG_POS_PERSON_INDICATOR.equals((Integer)LookUpResult)) {
                  isPerson = true;
                  k = pBasicNeDictionary.MaxPersonIndicatorLength;  // break k
                  j = -1; // break j
                }
                else if (!pBasicNeDictionary.PersonIndicatorTrie
                .previousMatchIsPrefixOfKey()) {
                  k = pBasicNeDictionary.MaxPersonIndicatorLength;  // break k
                  j = -1; // break j
                }
              }  // start pattern can contain more than 1 token
            }  // look backward from a single beginning
            // System.out.println("isPerson=" + isPerson);
          }
          // check all candidates for negative person indicators
          if (EndIndex >= 0 && isPerson && StartIndex > 0) {
            StartPatternPath = new String[1];
            StartPatternPath[0] = Tokens_NeAsPlaceholder[StartIndex - 1]
            .toLowerCase();
            // System.out.print(StartPatternPath[l] + " ");
            LookUpResult = pBasicNeDictionary.PersonIndicatorTrie
            .get(StartPatternPath);
            // System.out.println(StartPatternPath.length +
            // " j=" + j + " k=" + k + " +++ " + LookUpResult);
            if (LookUpResult != null && BasicNeDictionary21
            .STRONG_NEG_PERSON_INDICATOR.equals((Integer)LookUpResult)) {
              isPerson = false;
            }
          }
          if (EndIndex >= 0 && isPerson && (EndIndex + 1) < NumberOfTokens) {
            StartPatternPath = new String[1];
            StartPatternPath[0] = Tokens_NeAsPlaceholder[EndIndex + 1]
            .toLowerCase();
            // System.out.print(StartPatternPath[l] + " ");
            LookUpResult = pBasicNeDictionary.PersonIndicatorTrie
            .get(StartPatternPath);
            // System.out.println(StartPatternPath.length +
            // " j=" + j + " k=" + k + " +++ " + LookUpResult);
            if (LookUpResult != null && BasicNeDictionary21
            .STRONG_NEG_PERSON_INDICATOR.equals((Integer)LookUpResult)) {
              isPerson = false;
            }
            else if (this.isProbablyAHouseNumber(Tokens_NeAsPlaceholder[
            EndIndex + 1])) {
              isPerson = false;
            }
          }
          if (!isPerson || EndIndex < 0
          || (StartIndex == 0 && (EndIndex - StartIndex) == 0)) {
            AllEndIndexes.set(i, null);
            AllEndIndexes_LastStrongAffix.set(i, null);
          }
          else {
            if (isPerson) {
              // look for an abbreviated forename that precedes the name
              // like "A. Beth Caesar"
              if ((StartIndex - 1) >= 0 && this.isProbablyAnAbbreviatedForename(
              Tokens_NeAsPlaceholder[StartIndex - 1])) {
                AllStartIndexes.set(i, new Integer(StartIndex - 1));
              }
            }
          }
        }  // look backward from all beginnings
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
      
      PrunedAllStartIndexes = new ArrayList();
      PrunedAllEndIndexes = new ArrayList();
      for (int k = 0; k < AllStartIndexes.size(); k++) {
        if (AllEndIndexes.get(k) != null) {
          PrunedAllStartIndexes.add(AllStartIndexes.get(k));
          PrunedAllEndIndexes.add(AllEndIndexes.get(k));
          StartIndex = ((Integer)AllStartIndexes.get(k)).intValue();
          EndIndex = ((Integer)AllEndIndexes.get(k)).intValue();
          // register new place with NamedEntityOwner
          PersonName = new StringBuffer();
          for (int j = StartIndex; j <= EndIndex; j++) {
            PersonName.append(Tokens_NeAsPlaceholder[j]);
            PersonName.append(" ");
          }
          NextPlaceholderID = pCurrentNeOwner.getNextNamedEntityIndex();
          NextPlaceholder = NamedEntity.createPlaceholder(NextPlaceholderID);
          AllPlaceholders.add(NextPlaceholder);
          pCurrentNeOwner.addNamedEntity(new NamedEntity(NextPlaceholderID,
          PersonName.toString().trim(), NextPlaceholder,
          NamedEntity.PERSON_NAME));
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
      
      // 5. replace places with their placeholder string
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
  
  private boolean isProbablyASurname(String pString,
  BasicNeDictionary21 pBasicNeDictionary) {
    
    if (pString == null || pString.length() < 5) {
      return false;
    }
    else {
      if (Character.isUpperCase(pString.charAt(0))) {
        if (pBasicNeDictionary.SurnameSuffixes.getBestMatch(
        (new StringBuffer(pString)).reverse().toString()) != null) {
          return true;
        }
        else {
          return false;
        }
      }
      else {
        return false;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isProbablyAnAbbreviatedForename(String pString) {
    
    if (pString != null && pString.length() == 2 && pString.charAt(1) == '.'
    && Character.isUpperCase(pString.charAt(0))) {
      return true;
    }
    else {
      return false; 
    }  
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isProbablyAHouseNumber(String pString) {
    
    if (pString == null || pString.length() == 0 || pString.length() > 3) {
      return false;
    }
    else if (pString.equals("-")) {
      return true;
    }
    else {
      for (int i = 0; i < pString.length(); i++) {
        if (!Character.isDigit(pString.charAt(i))) {
          return false;
        }
      }
      return true;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}