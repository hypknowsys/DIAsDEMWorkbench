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

public class ProfessionBasicNeExtractor21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityExtractorParameter NeTaskParameter = null;
  private String InputLine = null;
  private int NumberOfTokens = 0;  // in InputLine
  private StringBuffer OutputLine = null;
  
  // Das ist Herr Dr. Mustermann , Steuerberater , von ACB Technologies .
  //                               ^
  private ArrayList AllStartIndexes = null;
  // Das ist Herr Dr. Mustermann , Steuerberater , von ACB Technologies .
  //                               ^
  private ArrayList AllEndIndexes = null;
  // Das ist Herr Dr. Mustermann , Steuerberater , von ACB Technologies .
  //                               <<1>>........
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
  private transient int StartIndex = -1;
  private transient int EndIndex = -1;
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ProfessionBasicNeExtractor21(
  NamedEntityExtractorParameter pNeTaskParameter) {
    
    NeTaskParameter = pNeTaskParameter;
    DebuggingMode = NeTaskParameter.createDebuggingFiles();
    if (DebuggingMode) {
      Template debuggingOutputHeader = new Template(Tools
      .stringFromTextualSystemResource("org/hypknowsys/diasdem/resources/html/"
      + "HtmlFile_HeaderTemplate.html"));
      debuggingOutputHeader.addValue("${Title}", "NEEX 2.1: Professions "
      + " Extracted by OrganizationBasicNeExtractor");
      DebuggingOutput = new TextFile(new File(Tools.ensureTrailingSlash(
      NeTaskParameter.getDebuggingFileDirectory())
      + "Neex21_Professions.html"));
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
  
  public String replaceProfessionBasicNamedEntities(String pInputLine,
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
    
    // 1. look for all professions
    for (int i = 0; i < NumberOfTokens; i++) {
      NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
      .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[i]);
      if (NeLookUpResult != null && NeLookUpResult.isPossibleType(
      NamedEntity.PROFESSION)) {
        if (pBasicNeDictionary.NeDictionary.previousMatchPrecedesBlankSpace()) {
          // go on: token is a valid profession, but it might
          // be the prefix of a multi-token profession
          EndIndex = i;
          PersonAffix = new StringBuffer(1000);
          PersonAffix.append(Tokens_NeAsPlaceholder[i]);
          for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
          .NeDictionary.previousMatchPrecedesBlankSpace(); j++) {
            PersonAffix.append(" ");
            PersonAffix.append(Tokens_NeAsPlaceholder[j]);
            NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
            .getAndCheckForSubsequentBlankSpace(PersonAffix.toString());
            if (NeLookUpResult != null && NeLookUpResult.isPossibleType(
            NamedEntity.PROFESSION)) {
              EndIndex = j;
            }
          }
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(EndIndex));
          i = EndIndex;  // continue with next token
        }
        else {
          // stop here: token is a valid, single-token profession
          EndIndex = -1;
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(i));
        }
      }
      else if (pBasicNeDictionary.NeDictionary
      .previousMatchPrecedesBlankSpace()) {
        // go on: token is not a valid place, but it might
        // be the prefix of a multi-token profession
        EndIndex = -1;
        PersonAffix = new StringBuffer(1000);
        PersonAffix.append(Tokens_NeAsPlaceholder[i]);
        for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
        .NeDictionary.previousMatchPrecedesBlankSpace(); j++) {
          PersonAffix.append(" ");
          PersonAffix.append(Tokens_NeAsPlaceholder[j]);
          NeLookUpResult = (NamedEntity)pBasicNeDictionary.NeDictionary
          .getAndCheckForSubsequentBlankSpace(PersonAffix.toString());
          if (NeLookUpResult != null && NeLookUpResult.isPossibleType(
          NamedEntity.PROFESSION)) {
            EndIndex = j;
          }
        }
        if (EndIndex >= 0) {
          AllStartIndexes.add(new Integer(i));
          AllEndIndexes.add(new Integer(EndIndex));
          i = EndIndex;  // continue with next token
        }
      }
    }  // for:all tokens
    
    //System.out.println("###00###" + pInputLine);
    //System.out.print("AllStartIndexes.size() = "
    //+ AllStartIndexes.size() + "; Contents: ");
    //for (int i = 0; i < AllStartIndexes.size(); i++) {
    //  System.out.print((Integer)AllStartIndexes.get(i) + "; ");
    //}
    //System.out.print("\n");
    //System.out.print("AllEndIndexes.size() = "
    //+ AllEndIndexes.size() + "; Contents: ");
    //for (int i = 0; i < AllEndIndexes.size(); i++) {
    //  System.out.print((Integer)AllEndIndexes.get(i) + "; ");
    //}
    //System.out.print("\n");
    
    if (AllEndIndexes.size() > 0) {
      
      PrunedAllStartIndexes = new ArrayList();
      PrunedAllEndIndexes = new ArrayList();
      for (int k = 0; k < AllStartIndexes.size(); k++) {
        if (AllEndIndexes.get(k) != null) {
          PrunedAllStartIndexes.add(AllStartIndexes.get(k));
          PrunedAllEndIndexes.add(AllEndIndexes.get(k));
          StartIndex = ((Integer)AllStartIndexes.get(k)).intValue();
          EndIndex = ((Integer)AllEndIndexes.get(k)).intValue();
          // register new profession with NamedEntityOwner
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
          NamedEntity.PROFESSION));
        }
      }
      
      // 5. replace professions with their placeholder string
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
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}