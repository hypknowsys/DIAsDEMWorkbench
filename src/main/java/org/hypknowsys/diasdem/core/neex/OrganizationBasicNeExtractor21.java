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

public class OrganizationBasicNeExtractor21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityExtractorParameter NeTaskParameter = null;
  private String InputLine = null;
  private int NumberOfTokens = 0;  // in InputLine
  private StringBuffer OutputLine = null;
  
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin.
  //             ^
  private ArrayList AllStartIndexes = null;
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin.
  //                 ^
  private ArrayList AllEndIndexes_LastToken = null;
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin.
  //                            ^
  private ArrayList AllEndIndexes_FirstToken = null;
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin.
  //             <<1>>
  private ArrayList AllPlaceholders = null;
  // Das ist die ABC GmbH & Co. KG mit Sitz in Berlin.
  //             ORGANIZATION_END
  private ArrayList AllOrgOrOrgSuffixFlags = null;
  
  // [<<1>> ist Nachname von <<2>>]
  private String[] Tokens_NeAsPlaceholder = null;
  // [<<surname>> ist Nachname von <<forename>>]
  private String[] Tokens_NeAsPossibleType = null;
  // [Winkler ist Nachname von Karsten]
  private String[] Tokens_NeAsToken = null;
  
  private ArrayList PrunedAllStartIndexes = null;
  private ArrayList PrunedAllEndIndexes_LastToken = null;
  private String[] StartPatternPath = null;
  
  private boolean DebuggingMode = false;
  private TextFile DebuggingOutput = null;
  private TextFile DebuggingOrganizations = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient Object LookUpResult = null;
  private transient StringTokenizer MyTokenizer = null;
  private transient String MyToken = null;
  private transient StringBuffer Organization = null;
  private transient StringBuffer OrganizationAbbreviation = null;
  private transient StringBuffer OrganizationAffix = null;
  private transient int NextPlaceholderID = -1;
  private transient String NextPlaceholder = null;
  private transient int StartIndex = -1;
  private transient int EndIndexOrgOrgEndFlag = 0;
  private transient int EndIndexFirstToken = -1;
  private transient int EndIndexLastToken = -1;
  private transient int NewEndIndexLastToken = -1;
  
  private transient Pattern OrganizationIndicatorRegex = null;
  private transient Matcher OrganizationIndicatorMatcher = null;
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public OrganizationBasicNeExtractor21(
  NamedEntityExtractorParameter pNeTaskParameter) {
    
    NeTaskParameter = pNeTaskParameter;
    OrganizationIndicatorRegex = null;
    if (!Tools.stringIsNullOrEmpty(NeTaskParameter
    .getOrganizationIndicatorRegex())) {
      OrganizationIndicatorRegex = Pattern.compile(NeTaskParameter
      .getOrganizationIndicatorRegex());
    }
    DebuggingMode = NeTaskParameter.createDebuggingFiles();
    if (DebuggingMode) {
      Template debuggingOutputHeader = new Template(Tools
      .stringFromTextualSystemResource("org/hypknowsys/diasdem/resources/html/"
      + "HtmlFile_HeaderTemplate.html"));
      debuggingOutputHeader.addValue("${Title}", "NEEX 2.1: Organizations "
      + " Extracted by OrganizationBasicNeExtractor");
      DebuggingOutput = new TextFile(new File(Tools.ensureTrailingSlash(
      NeTaskParameter.getDebuggingFileDirectory())
      + "Neex21_Organizations.html"));
      DebuggingOutput.open();
      DebuggingOutput.setFirstLine(debuggingOutputHeader.insertValues());
      DebuggingOutput.close();
      DebuggingOrganizations = new TextFile(new File(Tools.ensureTrailingSlash(
      NeTaskParameter.getDebuggingFileDirectory())
      + "Neex21_Organizations.txt"));
      DebuggingOrganizations.empty();
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
  
  public String replaceOrganizationBasicNamedEntities(String pInputLine,
  NamedEntityOwner pCurrentNeOwner, BasicNeDictionary21 pBasicNeDictionary) {
    
    InputLine = pInputLine;
    OutputLine = null;
    StartIndex = -1;
    EndIndexFirstToken = -1;
    EndIndexLastToken = -1;
    AllStartIndexes = new ArrayList();
    AllEndIndexes_FirstToken = new ArrayList();
    AllEndIndexes_LastToken = new ArrayList();
    AllPlaceholders = new ArrayList();
    AllOrgOrOrgSuffixFlags = new ArrayList();
    
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
    
    // 1. look for all endings of company candidates by
    // searching for consecutive organization abbreviations
    for (int i = 0; i < NumberOfTokens; i++) {
      // System.out.print(Tokens_NeAsPlaceholder[i]);
      EndIndexOrgOrgEndFlag = 1;
      LookUpResult = pBasicNeDictionary.OrgAndOrgEndTrie
      .getAndCheckForSubsequentBlankSpace(Tokens_NeAsPlaceholder[i]);
      if (LookUpResult != null) {
        if (BasicNeDictionary21.ORGANIZATION_END.compareTo(
        (Integer)LookUpResult) == 0) {
          // end of organization found
          EndIndexOrgOrgEndFlag = 1;
        }
        else {
          // complete organization found
          EndIndexOrgOrgEndFlag = -1;
        }
        if (pBasicNeDictionary.OrgAndOrgEndTrie
        .previousMatchPrecedesBlankSpace()) {
          // go on: token is a valid company abbreviation, but it might
          // be the prefix of a multi-token company abbreviation
          EndIndexLastToken = i;
          OrganizationAbbreviation = new StringBuffer(1000);
          OrganizationAbbreviation.append(Tokens_NeAsPlaceholder[i]);
          for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
          .OrgAndOrgEndTrie.previousMatchPrecedesBlankSpace(); j++) {
            OrganizationAbbreviation.append(" ");
            OrganizationAbbreviation.append(Tokens_NeAsPlaceholder[j]);
            if (pBasicNeDictionary.OrgAndOrgEndTrie
            .getAndCheckForSubsequentBlankSpace(OrganizationAbbreviation
            .toString()) != null) {
              if (BasicNeDictionary21.ORGANIZATION_END.compareTo(
              (Integer)LookUpResult) == 0) {
                // end of organization found
                EndIndexOrgOrgEndFlag = 1;
              }
              else {
                // complete organization found
                EndIndexOrgOrgEndFlag = -1;
              }
              EndIndexLastToken = j;
            }
          }
          AllEndIndexes_FirstToken.add(new Integer(i));
          AllEndIndexes_LastToken.add(new Integer(EndIndexLastToken));
          AllOrgOrOrgSuffixFlags.add(new Integer(EndIndexOrgOrgEndFlag));
          i = EndIndexLastToken;  // continue with next token
        }
        else {
          // stop here: token is a valid, single-token company abbreviation
          EndIndexLastToken = -1;
          AllEndIndexes_FirstToken.add(new Integer(i));
          AllEndIndexes_LastToken.add(new Integer(i));
          AllOrgOrOrgSuffixFlags.add(new Integer(EndIndexOrgOrgEndFlag));
        }
      }
      else if (LookUpResult == null && pBasicNeDictionary.OrgAndOrgEndTrie
      .previousMatchPrecedesBlankSpace()) {
        // go on: token is not a valid company abbreviation, but it might
        // be the prefix of a multi-token company abbreviation
        EndIndexLastToken = -1;
        OrganizationAbbreviation = new StringBuffer(1000);
        OrganizationAbbreviation.append(Tokens_NeAsPlaceholder[i]);
        for (int j = i + 1; j < NumberOfTokens && pBasicNeDictionary
        .OrgAndOrgEndTrie.previousMatchPrecedesBlankSpace(); j++) {
          OrganizationAbbreviation.append(" ");
          OrganizationAbbreviation.append(Tokens_NeAsPlaceholder[j]);
          LookUpResult = pBasicNeDictionary.OrgAndOrgEndTrie
          .getAndCheckForSubsequentBlankSpace(OrganizationAbbreviation
          .toString());
          if (LookUpResult != null) {
            if (BasicNeDictionary21.ORGANIZATION_END.compareTo(
            (Integer)LookUpResult) == 0) {
              // end of organization found
              EndIndexOrgOrgEndFlag = 1;
            }
            else {
              // complete organization found
              EndIndexOrgOrgEndFlag = -1;
            }
            EndIndexLastToken = j;
          }
        }
        if (EndIndexLastToken >= 0) {
          AllEndIndexes_FirstToken.add(new Integer(i));
          AllEndIndexes_LastToken.add(new Integer(EndIndexLastToken));
          AllOrgOrOrgSuffixFlags.add(new Integer(EndIndexOrgOrgEndFlag));
          i = EndIndexLastToken;  // continue with next token
        }
      }
      
      // try to extend complete names of organizations with valid suffixes
      if (EndIndexOrgOrgEndFlag == -1 && EndIndexLastToken >= 0) {
        NewEndIndexLastToken = -1;
        LookUpResult = null;
        OrganizationAbbreviation = new StringBuffer(1000);
        for (int j = EndIndexLastToken + 1; j < NumberOfTokens 
        && (j == (EndIndexLastToken + 1) || pBasicNeDictionary
        .OrgAndOrgEndTrie.previousMatchPrecedesBlankSpace()); j++) {
          OrganizationAbbreviation.append(j > (EndIndexLastToken + 1)
          ? " " : "");
          OrganizationAbbreviation.append(Tokens_NeAsPlaceholder[j]);
          LookUpResult = pBasicNeDictionary.OrgAndOrgEndTrie
          .getAndCheckForSubsequentBlankSpace(OrganizationAbbreviation
          .toString());
          if (LookUpResult != null) {
            NewEndIndexLastToken = j;
          }
        }
        if (NewEndIndexLastToken >= 0) {
          // extend the complete name of organization by updating last token 
          AllEndIndexes_LastToken.set(AllEndIndexes_LastToken.size() - 1, 
          new Integer(NewEndIndexLastToken));
          i = NewEndIndexLastToken;  // continue with next token
        }
      }      
      
    }  // for:all tokens
    
    // 2. find the corresponding start for each company candidate
    StartPatternPath = null;
    if (AllEndIndexes_LastToken.size() > 0) {
      
      for (int i = 0; i < AllEndIndexes_FirstToken.size(); i++) {
        EndIndexFirstToken =
        ((Integer)AllEndIndexes_FirstToken.get(i)).intValue();
        EndIndexOrgOrgEndFlag =
        ((Integer)AllOrgOrOrgSuffixFlags.get(i)).intValue();
        if (EndIndexOrgOrgEndFlag < 0) {
          // complete organization found
          AllStartIndexes.add(new Integer(EndIndexFirstToken));
        }
        else {
          // end of organization found
          StartIndex = -1;
          for (int j = Math.abs(EndIndexFirstToken) - 1; j >= 0; j--) {
            
            for (int k = 0; k < pBasicNeDictionary.MaxOrgIndicatorLength
            && k <= j; k++) {
              StartPatternPath = new String[k + 1];
              for (int l = 0; (l < (k + 1)) && (l <= j); l++) {
                StartPatternPath[l] = Tokens_NeAsPlaceholder[j - l];
                // System.out.print(StartPatternPath[l] + " ");
              }
              // System.out.println(StartPatternPath.length +
              //   " j=" + j + " k=" +k + " +++ ");
              LookUpResult = pBasicNeDictionary.OrgIndicatorTrie
              .get(StartPatternPath);
              if (LookUpResult != null) {
                StartIndex = j + 1;
                AllStartIndexes.add(new Integer(j + 1));
                k = pBasicNeDictionary.MaxOrgIndicatorLength;  // break k
                j = -1; // break j
              }
              else if (k == 0 && this.isValidOrganizationIndicatorToken(
              StartPatternPath[0])) {
                // indicator regex matches first token
                StartIndex = j + 1;
                AllStartIndexes.add(new Integer(j + 1));
                k = pBasicNeDictionary.MaxOrgIndicatorLength;  // break k
                j = -1; // break j
              }
            }  // start pattern can contain more than 1 token
            
          }  // look backward from a single ending
          // start = begin of organization
          if (StartIndex < 0) {
            AllStartIndexes.add(new Integer(0));
          }
          else if (StartIndex > 0
          && (Tokens_NeAsPlaceholder[StartIndex - 1].equals("'")
          || Tokens_NeAsPlaceholder[StartIndex - 1].equals("\""))) {
            // ' and " can be first token of the extracted organization, e.g.
            // die ' Bleitz ' Baggerleistung und Spezialschachtung GmbH
            // whereas <die '> is an organization indicator
            for (int z = StartIndex + 1; z < Math.abs(EndIndexFirstToken);
            z++) {
              if (Tokens_NeAsPlaceholder[z].equals("'")
              || Tokens_NeAsPlaceholder[z].equals("\"")) {
                // replace StartIndex to include ' or " in organization
                AllStartIndexes.set(i, new Integer(StartIndex - 1));
                z = Math.abs(EndIndexFirstToken);  // break z
              }
            }
            // System.out.println(InputLine);
          }
        }
        
      }  // look backward from all endings
      
      // 2b. try to extend each company candidate by searching for affixes
      for (int i = 0; i < AllEndIndexes_LastToken.size(); i++) {
        EndIndexLastToken =
        ((Integer)AllEndIndexes_LastToken.get(i)).intValue();
        NewEndIndexLastToken = -1;
        // start searching at token after current end of organization
        OrganizationAffix = new StringBuffer(1000);
        for (int k = 1; (EndIndexLastToken + k) < NumberOfTokens; k++) {
          if (k > 1) {
            OrganizationAffix.append(" ");
          }
          OrganizationAffix.append(Tokens_NeAsPlaceholder[
          EndIndexLastToken + k].toLowerCase());
          // System.out.println(k + " - " + OrganizationAffix.toString());
          // System.out.println("EndIndex=" + EndIndex + " k=" + k + " +++ "
          // + pBasicNeDictionary.OrgAffixTrie
          // .getAndCheckForSubsequentBlankSpace(PlaceAffix.toString()) + "; "
          // + pBasicNeDictionary.OrgAffixTrie
          // .previousMatchPrecedesBlankSpace());
          LookUpResult = pBasicNeDictionary.OrgAffixTrie
          .getAndCheckForSubsequentBlankSpace(OrganizationAffix.toString());
          if (LookUpResult != null) {
            NewEndIndexLastToken = EndIndexLastToken + k;
          }
          if (!pBasicNeDictionary.OrgAffixTrie
          .previousMatchPrecedesBlankSpace()) {
            k = NumberOfTokens;  // break k
          }
        }  // affix pattern can contain more than 1 token
        // replace previous end of organization
        if (NewEndIndexLastToken >= 0) {
          AllEndIndexes_LastToken.set(i, new Integer(NewEndIndexLastToken));
        }
      }
      
      // 3. register companies with pCurrentNeOwner
      for (int i = 0; i < AllEndIndexes_LastToken.size(); i++) {
        StartIndex = ((Integer)AllStartIndexes.get(i)).intValue();
        EndIndexFirstToken =
        ((Integer)AllEndIndexes_FirstToken.get(i)).intValue();
        EndIndexLastToken =
        ((Integer)AllEndIndexes_LastToken.get(i)).intValue();
        EndIndexOrgOrgEndFlag =
        ((Integer)AllOrgOrOrgSuffixFlags.get(i)).intValue();
        // check if longer company exists that also starts here
        for (int k = i + 1; k < AllEndIndexes_LastToken.size(); k++) {
          // System.out.println("i_F " + AllStartIndexes.get(i));
          // System.out.println("i_L " + AllEndIndexes_LastToken.get(i));
          // System.out.println("k_F " + AllStartIndexes.get(k));
          // System.out.println("k_L " + AllEndIndexes_LastToken.get(k));
          // System.out.println("AllEndIndexes_LastToken.size() = "
          // + AllEndIndexes_LastToken.size());          
          if (((Integer)AllStartIndexes.get(k)).equals(
          (Integer)AllStartIndexes.get(i))
          && ((Integer)AllEndIndexes_LastToken.get(k)).compareTo(
          (Integer)AllEndIndexes_LastToken.get(i)) > 0) {
            // merge two organizations if they share the same start token
            EndIndexLastToken = -1;
            break;
          }
        }
        // avoid extracting empty bogus companies such as GmbH only extract the
        // longest company name for each StartIndex, set corresponding
        // EndIndexLastToken null if it has been used for extracting a company
        // There is a difference between dealing with complete organizations
        // and organizations identified by start and end
        // System.out.println(StartIndex + "-" + Math.abs(EndIndexFirstToken));
        if (((EndIndexOrgOrgEndFlag > 0 && StartIndex < EndIndexFirstToken)
        || EndIndexOrgOrgEndFlag < 0) && EndIndexLastToken >= 0) {
          Organization = new StringBuffer();
          for (int j = StartIndex; j <= EndIndexLastToken; j++) {
            Organization.append(Tokens_NeAsPlaceholder[j]);
            Organization.append(" ");
          }
          NextPlaceholderID = pCurrentNeOwner.getNextNamedEntityIndex();
          NextPlaceholder = NamedEntity.createPlaceholder(NextPlaceholderID);
          AllPlaceholders.add(NextPlaceholder);
          pCurrentNeOwner.addNamedEntity(new NamedEntity(NextPlaceholderID,
          Organization.toString().trim(), NextPlaceholder,
          NamedEntity.ORGANIZATION));
        }
        else {
          AllEndIndexes_LastToken.set(i, null);
        }
      }  // look backward from all endings
      
      PrunedAllStartIndexes = new ArrayList();
      PrunedAllEndIndexes_LastToken = new ArrayList();
      for (int k = 0; k < AllStartIndexes.size(); k++) {
        if (AllEndIndexes_LastToken.get(k) != null) {
          PrunedAllStartIndexes.add(AllStartIndexes.get(k));
          PrunedAllEndIndexes_LastToken.add(AllEndIndexes_LastToken.get(k));
        }
      }
      
      
      // 4. replace companies with their placeholder string
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
            this.appendOrganizationsFile(pCurrentNeOwner.getNamedEntity(
            NamedEntity.getNamedEntityIndex((String)AllPlaceholders.get(
            nextPlaceholderStartIndex))).getToken());
          }
          OutputLine.append((String)AllPlaceholders.get(
          nextPlaceholderStartIndex));
          OutputLine.append(" ");
          i = ((Integer)PrunedAllEndIndexes_LastToken.get(
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
      // System.out.println(AllEndIndexes_FirstToken);
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
  
  private void appendOrganizationsFile(String pOrganization) {
    
    DebuggingOrganizations.open();
    DebuggingOrganizations.setNextLine(pOrganization);
    DebuggingOrganizations.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isValidOrganizationIndicatorToken(String pString) {
    
    if (OrganizationIndicatorRegex == null || pString == null
    || pString.length() == 0) {
      return false;
    }
    else {
      OrganizationIndicatorMatcher = OrganizationIndicatorRegex
      .matcher(pString);
      if (OrganizationIndicatorMatcher.matches()) {
        return true;
      }
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