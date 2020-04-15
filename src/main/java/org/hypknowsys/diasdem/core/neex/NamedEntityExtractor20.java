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

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;
import gnu.regexp.REMatchEnumeration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurus;
import org.hypknowsys.misc.io.CsvFile;
import org.hypknowsys.misc.io.TextBufferedReader;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.StringArrayTrie;
import org.hypknowsys.misc.util.StringTrie;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class NamedEntityExtractor20 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityExtractorParameter MyTaskParameter = null;
  
  private DIAsDEMthesaurus Forenames = null;
  private DIAsDEMthesaurus Surnames = null;
  private DIAsDEMthesaurus Titles = null;
  private DIAsDEMthesaurus Places = null;
  private DIAsDEMthesaurus Organizations = null;
  private StringTrie SurnameSuffixes = null;
  private StringTrie MiddleInitials = null;
  
  private DIAsDEMthesaurusTerm CurrentTerm = null;
  private StringTokenizer Tokenizer = null;
  private String Token = null;
  private StringBuffer ReverseToken = null;
  private Object SurnameSuffix = null;
  private Object MiddleInitial = null;
  
  private int NumberOfCompositePatterns = 0;  // composite features
  private String[] Patterns = null;
  private String[] MyCompositeNamedEntity = null;
  private StringArrayTrie MyPatternTrie = null;
  
  private int NumberOfOrgStartPatterns = 0;  // start tokens of organizations
  private int MaxStartOrgPatternLength = 0;
  private String[] StartPatterns = null;
  private StringArrayTrie StartPatternTrie = null;
  
  private NamedEntityOwner CurrentNamedEntityOwner = null;
  
  CsvFile CompositeNeCsvFile = null;
  Object[] CompositeNeValues = null;
  
  private PersonNE CurrentPerson = null;
  private CompanyNE CurrentCompany = null;
  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  // "";"company( Name , Place , DistrictCourt , CommercialRegisterID )"
  // "";"person( Surname , Forename , Title , MiddleInitial , DoB ,
  //   MothersName , Place )"
  // "";"company_relocation( Name , OriginPlace , OriginStreet ,
  //   OriginDistrictCourt , OriginCommercialRegisterID , DestinationPlace ,
  //   DestinationStreet , DestinationDistrictCourt ,
  //   DestinationCommercialRegisterID )"
  
  // valid consturctor:
  // "person( 1 , 0 , null , null , 3 , null , 5 )"
  private String PERSON_CONSTRUCTOR_ID = "person";
  // valid constructor:
  // "company( 0 , null , null , null )"
  private String COMPANY_CONSTRUCTOR_ID = "company";
  // valid constructor:
  // "company( 0 , null , null , null , null , null , null )"
  private String COMPANY_RELOCATION_CONSTRUCTOR_ID = "company_relocation";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntityExtractor20(NamedEntityExtractorParameter pTaskParameter) {
    
    MyTaskParameter = pTaskParameter;
    
    this.fillPatternArrays();
    MyPatternTrie = new StringArrayTrie();
    for (int i = 0; i < NumberOfCompositePatterns; i++) {
      MyPatternTrie.put(this.string2ArrayOfTokens(Patterns[i]),
      MyCompositeNamedEntity[i]);
    }
    // patternTrie.dump(System.out); System.exit(0);
    
    StartPatternTrie = new StringArrayTrie();
    for (int i = 0; i < NumberOfOrgStartPatterns; i++) {
      StartPatternTrie.put(this.string2ArrayOfTokens(
      StartPatterns[i].toLowerCase()), StartPatterns[i]);
    }
    System.out.println("NEEX 2.0: List of composite NE rules loaded");
    
    // read list of forenames (text file, each line = one forname)
    Forenames = new DefaultDIAsDEMthesaurus();
    TextBufferedReader textReader = new TextBufferedReader(
    new File(MyTaskParameter.ForenamesFileName));
    String line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      Forenames.countOccurrence(line.trim());
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    System.out.println("NEEX 2.0: List of forenames loaded");
    
    // read list of surnames (text file, each line = one surname)
    Surnames = new DefaultDIAsDEMthesaurus();
    textReader = new TextBufferedReader(
    new File(MyTaskParameter.SurnamesFileName));
    line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      Surnames.countOccurrence(line.trim());
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    System.out.println("NEEX 2.0: List of surnames loaded");
    
    // read list of titles (text file, each line = one title)
    Titles = new DefaultDIAsDEMthesaurus();
    textReader = new TextBufferedReader(
    new File(MyTaskParameter.TitlesFileName));
    line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      Titles.countOccurrence(line.trim());
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    System.out.println("NEEX 2.0: List of titles loaded");
    
    // read list of places (text file, each line = one place)
    Places = new DefaultDIAsDEMthesaurus();
    textReader = new TextBufferedReader(
    new File(MyTaskParameter.PlacesFileName));
    line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      Places.countOccurrence(line.trim());
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    System.out.println("NEEX 2.0: List of places loaded");
    
    // read list of organization suffixes (text file, each line = one suffix)
    Organizations = new DefaultDIAsDEMthesaurus();
    textReader = new TextBufferedReader(
    new File(MyTaskParameter.OrganizationsEndFileName));
    line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      Organizations.countOccurrence(line.trim());
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    System.out.println("NEEX 2.0: List of organization suffixes loaded");
    
    // read list of surname suffixes (text file, each line = one suffix)
    SurnameSuffixes = new StringTrie();
    StringBuffer reverseSuffix = null;
    TextFile surnameSuffixesFile = new TextFile(
    new File(MyTaskParameter.SurnameSuffixesFileName));
    surnameSuffixesFile.open();
    String surnameSuffix = surnameSuffixesFile.getFirstLine();
    while (surnameSuffix != null) {
      reverseSuffix = new StringBuffer(surnameSuffix);
      reverseSuffix.reverse();
      SurnameSuffixes.put(reverseSuffix.toString().trim(),
      surnameSuffix.trim());
      surnameSuffix = surnameSuffixesFile.getNextLine();
    }
    surnameSuffixesFile.close();
    System.out.println("NEEX 2.0: List of surname suffixes loaded");
    // SurnameSuffixes.dump(System.out);
    
    // read list of middle initials (text file, each line = one initial)
    MiddleInitials = new StringTrie();
    TextFile middleInitialsFile = new TextFile(
    new File(MyTaskParameter.MiddleInitialsFileName));
    middleInitialsFile.open();
    String middleInitial = middleInitialsFile.getFirstLine();
    while (middleInitial != null) {
      MiddleInitials.put(middleInitial.trim(), middleInitial.trim());
      middleInitial = middleInitialsFile.getNextLine();
    }
    middleInitialsFile.close();
    System.out.println("NEEX 2.0: List of middle initials loaded");
    //MiddleInitials.dump(System.out);
    
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
  
  /*
   Strategy for extracting features:
   1) search elementary features (forenames, dates, surnames), sentence level
   2) construct composed features from elementary features (names),
      sentence level
   3) search features with different IDs that belong to the same entity,
      document level
   */
  
  public String extractNamedEntities(String pSourceString,
  NamedEntityOwner pCurrentNamedEntityOwner) {
    
    CurrentNamedEntityOwner = pCurrentNamedEntityOwner;
    
    String result = null;
    result = this.replaceNamedEntitysFromLine(pSourceString,
    pCurrentNamedEntityOwner);
    result = this.replaceCompositeNamedEntities(result,
    pCurrentNamedEntityOwner);
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String replaceNamedEntitysFromLine(String pInputLine,
  NamedEntityOwner pcurrentDiasdemDocument) {
    
    String satz = null;
    satz = this.patternMatching(pInputLine, pcurrentDiasdemDocument);
    satz = this.replaceOrganizations(satz, pcurrentDiasdemDocument);
    
    StringBuffer resultWithoutNamedEntitys = new StringBuffer(satz.length());
    int nextPlaceholderID = 0;
    String nextPlaceholder = null;
    boolean previousTokenIsForename = false;
    boolean currentTokenIsForename = false;
    Tokenizer = new StringTokenizer(satz);
    Token = null;
    
    boolean tokenIsPlaceholder = false;
    int tokenNamedEntityIndex = -1;
    
    while (Tokenizer.hasMoreTokens()) {
      
      currentTokenIsForename = false;
      Token = (String)Tokenizer.nextToken();
      if (NamedEntity.isPlaceholder(Token)) {
        tokenIsPlaceholder = true;
        tokenNamedEntityIndex = NamedEntity.getNamedEntityIndex(Token);
      }
      else {
        tokenIsPlaceholder = false;
        tokenNamedEntityIndex = -1;
      }
      
      // order of test is relevant due to bug in NamedEntity.java:
      // getPossibleTypeString() doesn't return uniformly ordered String
      //  of types
      if (tokenIsPlaceholder && (Places.contains(
      pcurrentDiasdemDocument.getNamedEntity(tokenNamedEntityIndex)
      .getToken()))) {
        pcurrentDiasdemDocument.updateNamedEntity(tokenNamedEntityIndex,
        NamedEntity.PLACE);
      }
      else
        if (Places.contains(Token)) {
          nextPlaceholderID = pcurrentDiasdemDocument
          .getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID, Token,
          nextPlaceholder, NamedEntity.PLACE));
          Token = nextPlaceholder;
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
      
      if (tokenIsPlaceholder && (Forenames.contains(
      pcurrentDiasdemDocument.getNamedEntity(tokenNamedEntityIndex)
      .getToken()))) {
        pcurrentDiasdemDocument.updateNamedEntity(tokenNamedEntityIndex,
        NamedEntity.FORENAME);
        currentTokenIsForename = true;
      }
      else
        if (Forenames.contains(Token)) {
          nextPlaceholderID = pcurrentDiasdemDocument
          .getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID, Token,
          nextPlaceholder, NamedEntity.FORENAME));
          Token = nextPlaceholder;
          currentTokenIsForename = true;
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
      
      if (tokenIsPlaceholder && (Surnames.contains(
      pcurrentDiasdemDocument.getNamedEntity(tokenNamedEntityIndex)
      .getToken()))) {
        pcurrentDiasdemDocument.updateNamedEntity(tokenNamedEntityIndex,
        NamedEntity.SURNAME);
      }
      else
        if (Surnames.contains(Token)) {
          nextPlaceholderID = pcurrentDiasdemDocument.getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID, Token,
          nextPlaceholder, NamedEntity.SURNAME));
          Token = nextPlaceholder;
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
      
      if (tokenIsPlaceholder && (Titles.contains(
      pcurrentDiasdemDocument.getNamedEntity(tokenNamedEntityIndex)
      .getToken()))) {
        pcurrentDiasdemDocument.updateNamedEntity(tokenNamedEntityIndex,
        NamedEntity.TITLE);
      }
      else
        if (Titles.contains(Token)) {
          nextPlaceholderID = pcurrentDiasdemDocument.getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID, Token,
          nextPlaceholder, NamedEntity.TITLE));
          Token = nextPlaceholder;
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
      
      
      // Doppelnamen?
      boolean vorname = false;
      boolean nachname = false;
      NamedEntity feature = null;
      if ((!tokenIsPlaceholder) && (Token.indexOf("-") > 0)
      && (Token.indexOf("-") < (Token.length() - 1))) {
        if (Surnames.contains(Token.substring(0, Token.indexOf("-")))) {
          nachname = true;
        }
        else {  // surname due tu suffix
          ReverseToken = new StringBuffer(
          Token.substring(0, Token.indexOf("-")));
          ReverseToken.reverse();
          SurnameSuffix = SurnameSuffixes.getBestMatch(
          ReverseToken.toString());
          if ((SurnameSuffix != null) && (previousTokenIsForename)) {
            nachname = true;
          }
        }
        if (Surnames.contains(Token.substring(Token.indexOf("-") + 1,
        Token.length()))) {
          nachname = true;
        }
        else {  // surname due to suffix
          ReverseToken = new StringBuffer(
          Token.substring(Token.indexOf("-") + 1,
          Token.length()));
          ReverseToken.reverse();
          SurnameSuffix = SurnameSuffixes.getBestMatch(
          ReverseToken.toString());
          if ((SurnameSuffix != null) && (previousTokenIsForename)) {
            nachname = true;
          }
        }
        if (Forenames.contains(Token.substring(0, Token.indexOf("-")))) {
          vorname = true;
        }
        if (Forenames.contains(Token.substring(Token.indexOf("-") + 1,
        Token.length()))) {
          vorname = true;
        }
        if (vorname && nachname) {
          nextPlaceholderID = pcurrentDiasdemDocument.getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          feature = new NamedEntity(nextPlaceholderID, Token, nextPlaceholder,
          NamedEntity.FORENAME);
          feature.addPossibleType(NamedEntity.SURNAME);
          pcurrentDiasdemDocument.addNamedEntity(feature);
          Token = nextPlaceholder;
          currentTokenIsForename = true;
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
        else if (vorname) {
          nextPlaceholderID = pcurrentDiasdemDocument
          .getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID, Token,
          nextPlaceholder, NamedEntity.FORENAME));
          Token = nextPlaceholder;
          currentTokenIsForename = true;
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
        else if (nachname) {
          nextPlaceholderID = pcurrentDiasdemDocument.getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID, Token,
          nextPlaceholder, NamedEntity.SURNAME));
          Token = nextPlaceholder;
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
      }
      
      // search for middle initials that follow a forename, must be done before
      // searching for unknown surname by surname sufiffixes
      
      if ((!tokenIsPlaceholder) && (previousTokenIsForename)) {
        MiddleInitial = MiddleInitials.get(Token);
        if (MiddleInitial != null) {
          // System.out.println(" ### Middle Initial ### " + Token);
          nextPlaceholderID = pcurrentDiasdemDocument.getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID, Token,
          nextPlaceholder, NamedEntity.MIDDLE_INITIAL));
          Token = nextPlaceholder;
          currentTokenIsForename = true;  // just pretending to be a forename
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
      }
      
      // surname if previous token is forename and token starts with capital
      // letter and Token ends with surname suffix
      
      if ((!tokenIsPlaceholder) && (previousTokenIsForename)
      && (Token.substring(0, 1).equals(Token.substring(0, 1).toUpperCase()))) {
        ReverseToken = new StringBuffer(Token);
        ReverseToken.reverse();
        SurnameSuffix = SurnameSuffixes.getBestMatch(
        ReverseToken.toString());
        if (SurnameSuffix != null) {
          // System.out.println(" ### Surname Suffix ### " + Token);
          nextPlaceholderID = pcurrentDiasdemDocument.getNextNamedEntityIndex();
          nextPlaceholder = NamedEntity.createPlaceholder(nextPlaceholderID);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID, Token,
          nextPlaceholder, NamedEntity.SURNAME));
          Token = nextPlaceholder;
          tokenIsPlaceholder = true;
          tokenNamedEntityIndex = nextPlaceholderID;
        }
      }
      
      // surname if previous token is forename and token starts with capital
      // letter and Token
      // ends with surname suffix, token currently is a placeholder for a place
      
      if (tokenIsPlaceholder && (pcurrentDiasdemDocument.getNamedEntity(
      tokenNamedEntityIndex).getPossibleTypesString().equals("place"))
      && (previousTokenIsForename) && (Token.substring(0, 1).equals(
      Token.substring(0, 1).toUpperCase()))) {
        ReverseToken = new StringBuffer(Token);
        ReverseToken.reverse();
        SurnameSuffix = SurnameSuffixes.getBestMatch(
        ReverseToken.toString());
        if (SurnameSuffix != null) {
          // System.out.println(
          //   " ### Surname Suffix (place+surname) ### " + Token);
          pcurrentDiasdemDocument.updateNamedEntity(tokenNamedEntityIndex,
          NamedEntity.SURNAME);
        }
      }
      
      previousTokenIsForename = currentTokenIsForename;
      
      resultWithoutNamedEntitys.append(Token);
      resultWithoutNamedEntitys.append(" ");
    }
    
    // System.out.println(resultWithoutNamedEntitys.toString());
    
    return resultWithoutNamedEntitys.toString().trim();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String patternMatching(String pSatz,
  NamedEntityOwner pcurrentDiasdemDocument) {
    
    String satz = pSatz;
    RegexBasicNeExtractor20 patternExtractor =
    new RegexBasicNeExtractor20(MyTaskParameter);
    
    satz = patternExtractor.replaceRegexBasicNamedEntities(
    satz, pcurrentDiasdemDocument);
    
    return satz;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String replaceCompositeNamedEntities(String pSatz,
  NamedEntityOwner pcurrentDiasdemDocument) {
    
    NamedEntity newNamedEntity = null;
    
    // System.out.println(pSatz);
    String satz = pSatz;
    String maxNamedEntityString = null;
    NamedEntity currentNamedEntity = null;
    NamedEntity maxNamedEntity = null;
    int currentNamedEntityAttributes = 0;
    int maxNamedEntityAttributes = 0;
    int startIndexCurrentNamedEntity = -1;
    int endIndexCurrentNamedEntity = -1;
    int startIndexMaxNamedEntity = -1;
    int endIndexMaxNamedEntity = -1;
    
    ArrayList compositeNamedEntitysStartIndex = new ArrayList();
    ArrayList compositeNamedEntitysEndIndex = new ArrayList();
    ArrayList compositeNamedEntitysPlaceholder = new ArrayList();
    
    StringTokenizer tokenizer = new StringTokenizer(satz);
    int tokens = tokenizer.countTokens();
    // [<<1>> ist Nachname von <<2>>]
    String[] placeholderSentences = new String[tokens];
    // [<<nachname>> ist Nachname von <<vorname>>]
    String[] possibleTypesSentences = new String[tokens];
    // [Winkler ist Nachname von Karsten]
    String[] noPlaceholderSentences = new String[tokens];
    String token = null;
    int counter = 0;
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      placeholderSentences[counter] = token;
      if (NamedEntity.isPlaceholder(token)) {
        possibleTypesSentences[counter] =
        pcurrentDiasdemDocument.getNamedEntity(
        NamedEntity.getNamedEntityIndex(token))
        .getPossibleTypesPlaceholder();
        noPlaceholderSentences[counter] =
        pcurrentDiasdemDocument.getNamedEntity(
        NamedEntity.getNamedEntityIndex(token))
        .getToken();
      }
      else {
        possibleTypesSentences[counter] = token;
        noPlaceholderSentences[counter] = token;
      }
      
      // System.out.println (placeholderSentences[counter]+ " - " +
      // possibleTypesSentences[counter] + " - " +
      // noPlaceholderSentences[counter]);
      counter++;
    }
    
    int featureType = NamedEntity.UNKNOWN;
    String[] patternPath = null;
    // System.out.println("########################" + satz);
    String[] subPattern = null;
    Object testResult = null;
    
    for (int i = 0; i < tokens; i++) {
      
      startIndexCurrentNamedEntity = i;
      
      // System.out.println("\n" + satz);
      for (int j = 0; j < (tokens - i); j++) {
        
        endIndexCurrentNamedEntity = j + i;
        subPattern = new String[j + 1];
        // System.out.println("--");
        for (int k = 0; k < (j + 1); k++) {
          if (NamedEntity.isPlaceholder(placeholderSentences[i + k])) {
            subPattern[k] = possibleTypesSentences[i + k];
          }
          else {
            subPattern[k] = placeholderSentences[i + k];
          }
          // System.out.print(i + " " + j + " " + k + ": " +
          //   subPattern[k] + " ## ");
          // System.out.print(possibleTypesSentences[i + k] + " ");
        }
        
        testResult = MyPatternTrie.get(subPattern);
        if (testResult == null) {
          // System.out.println((String)testResult);
          
          // Is there a new maximum composite feature?
          if ((maxNamedEntityString != null)
          && (startIndexCurrentNamedEntity > endIndexMaxNamedEntity)) {
            // System.out.println(MaxNamedEntityString);
            compositeNamedEntitysStartIndex.add(
            new Integer(startIndexMaxNamedEntity));
            compositeNamedEntitysEndIndex.add(
            new Integer(endIndexMaxNamedEntity));
            int nextPlaceholderID = pcurrentDiasdemDocument
            .getNextNamedEntityIndex();
            String nextPlaceholder = NamedEntity.createPlaceholder(
            nextPlaceholderID);
            if (featureType == NamedEntity.PERSON) {
              newNamedEntity.setID(nextPlaceholderID);
              newNamedEntity.setPlaceholder(nextPlaceholder);
              pcurrentDiasdemDocument.addNamedEntity(newNamedEntity);
            }
            if (featureType == NamedEntity.COMPANY) {
              newNamedEntity.setID(nextPlaceholderID);
              newNamedEntity.setPlaceholder(nextPlaceholder);
              pcurrentDiasdemDocument.addNamedEntity(newNamedEntity);
            }
            if (featureType == NamedEntity.COMPANY_RELOCATION) {
              newNamedEntity.setID(nextPlaceholderID);
              newNamedEntity.setPlaceholder(nextPlaceholder);
              pcurrentDiasdemDocument.addNamedEntity(newNamedEntity);
            }
            featureType = NamedEntity.UNKNOWN;
            compositeNamedEntitysPlaceholder.add(nextPlaceholder);
            maxNamedEntityString = null;
            maxNamedEntityAttributes = 0;
          }
          
        }
        else {
          
          // System.out.println("### " + testResult);
          // System.out.println((String)testResult + " -- " + satz);
          String composite = (String)testResult;
          
          if (composite.startsWith(PERSON_CONSTRUCTOR_ID)) {
            
            // remove trailing constructor name and bracket
            composite = ((String)testResult).substring(
            PERSON_CONSTRUCTOR_ID.length() + 1);
            // remove closing bracket
            composite = composite.substring(0, composite.length()-1);
            // System.out.println(composite);
            String nachname = "";
            String vorname = "";
            String titel = "";
            String namenszusatz = "";
            String geburtsdatum = "";
            String geburtsname = "";
            String wohnort = "";
            int currentAttribut = 0;
            String currentAttributString = "";
            StringTokenizer compositeTokenizer =
            new StringTokenizer(composite);
            String compositeToken = null;
            boolean lookForString = false;
            
            while (compositeTokenizer.hasMoreTokens()) {
              compositeToken = compositeTokenizer.nextToken();
              if (lookForString) {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                if (compositeToken.endsWith("\"")) {
                  if (compositeToken.length() > 1) {
                    currentAttributString += compositeToken.substring(0,
                    compositeToken.length() - 1);
                  }
                  lookForString = false;
                }
                else {
                  currentAttributString += compositeToken;
                }
              }
              else if (compositeToken.equals(",")) {
                switch (currentAttribut) {
                  case 0: {
                    nachname = currentAttributString; break;
                  }
                  case 1: {
                    vorname = currentAttributString; break;
                  }
                  case 2: {
                    titel = currentAttributString; break;
                  }
                  case 3: {
                    namenszusatz = currentAttributString; break;
                  }
                  case 4: {
                    geburtsdatum = currentAttributString; break;
                  }
                  case 5: {
                    geburtsname = currentAttributString; break;
                  }
                }
                currentAttribut++;
                currentAttributString = "";
              }
              // 1 " und " 2: StringTokenizer
              else if (compositeToken.startsWith("\"")) {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                if (compositeToken.endsWith("\"")) {
                  if (compositeToken.length() > 1) {
                    // " may be a single token
                    currentAttributString += compositeToken.substring(
                    1, compositeToken.length() - 1);
                  }
                  else {
                    if (compositeToken.length() > 1) {
                      // " may be a single token
                      currentAttributString += compositeToken.substring(1);
                    }
                    lookForString = true;
                  }
                }
              }
              else if (compositeToken.equals("null")) {
                currentAttributString = "";
              }
              else {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                currentNamedEntityAttributes++;
                int index = (new Integer(compositeToken)).intValue();
                currentAttributString += noPlaceholderSentences[i + index];
              }
            }  // while:createCompositeNamedEntity
            wohnort = currentAttributString;  // last attribute
            
            // look for the most complex composite feature
            if (currentNamedEntityAttributes > maxNamedEntityAttributes) {
              // ID and placeholder will be assigned later
              newNamedEntity = new PersonNE(0, null, NamedEntity.PERSON, null,
              null, nachname, vorname, geburtsdatum, namenszusatz, titel,
              null, null, geburtsname, wohnort, null, null);
              // ???
              maxNamedEntityString = nachname + vorname + titel;
              // MaxNamedEntityString = NamedEntity.createPerson(
              //   nachname, vorname, titel,
              //   namenszusatz, geburtsdatum, geburtsname, wohnort);
              featureType = NamedEntity.PERSON;
              maxNamedEntityAttributes = currentNamedEntityAttributes;
              startIndexMaxNamedEntity = i;
              endIndexMaxNamedEntity = i + j;
            }
            currentNamedEntityAttributes = 0;
            
          }  // if: person
          
          else if (composite.startsWith(COMPANY_RELOCATION_CONSTRUCTOR_ID)) {
            
            // remove trailing constructor name and bracket
            composite = ((String)testResult).substring(
            COMPANY_RELOCATION_CONSTRUCTOR_ID.length() + 1);
            // remove closing bracket
            composite = composite.substring(0, composite.length()-1);
            // System.out.println(composite);
            String name = "";
            String originPlace = "";
            String originDistrictCourt = null;
            String originCommercialRegisterID = null;
            String destinationPlace = "";
            String destinationDistrictCourt = null;
            String destinationCommercialRegisterID = null;
            int currentAttribut = 0;
            String currentAttributString = "";
            StringTokenizer compositeTokenizer =
            new StringTokenizer(composite);
            String compositeToken = null;
            boolean lookForString = false;
            
            while (compositeTokenizer.hasMoreTokens()) {
              compositeToken = compositeTokenizer.nextToken();
              if (lookForString) {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                if (compositeToken.endsWith("\"")) {
                  if (compositeToken.length() > 1) {
                    currentAttributString += compositeToken.substring(
                    0, compositeToken.length() - 1);
                  }
                  lookForString = false;
                }
                else {
                  currentAttributString += compositeToken;
                }
              }
              else if (compositeToken.equals(",")) {
                switch (currentAttribut) {
                  case 0: {
                    name = currentAttributString; break;
                  }
                  case 1: {
                    originPlace = currentAttributString; break;
                  }
                  case 2: {
                    originDistrictCourt = currentAttributString; break;
                  }
                  case 3: {
                    originCommercialRegisterID = currentAttributString; break;
                  }
                  case 4: {
                    destinationPlace = currentAttributString; break;
                  }
                  case 5: {
                    destinationDistrictCourt = currentAttributString; break;
                  }
                }
                currentAttribut++;
                currentAttributString = "";
              }
              // 1 " und " 2: StringTokenizer
              else if (compositeToken.startsWith("\"")) {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                if (compositeToken.endsWith("\"")) {
                  if (compositeToken.length() > 1) {
                    // " may be a single token
                    currentAttributString += compositeToken.substring(
                    1, compositeToken.length() - 1);
                  }
                  else {
                    if (compositeToken.length() > 1) {
                      // " may be a single token
                      currentAttributString += compositeToken.substring(1);
                    }
                    lookForString = true;
                  }
                }
              }
              else if (compositeToken.equals("null")) {
                currentAttributString = "";
              }
              else {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                currentNamedEntityAttributes++;
                int index = (new Integer(compositeToken)).intValue();
                currentAttributString += noPlaceholderSentences[i + index];
              }
            }  // while:createCompositeNamedEntity
            // last attribute
            destinationCommercialRegisterID = currentAttributString;
            
            // look for the most complex composite feature
            if (currentNamedEntityAttributes > maxNamedEntityAttributes) {
              newNamedEntity = new CompanyRelocationNE(0, null,
              NamedEntity.COMPANY_RELOCATION, name, originDistrictCourt,
              originCommercialRegisterID, originPlace, null,
              destinationDistrictCourt, destinationCommercialRegisterID,
              destinationPlace, null);
              maxNamedEntityString = name + originPlace + destinationPlace;
              featureType = NamedEntity.COMPANY_RELOCATION;
              maxNamedEntityAttributes = currentNamedEntityAttributes;
              startIndexMaxNamedEntity = i;
              endIndexMaxNamedEntity = i + j;
            }
            currentNamedEntityAttributes = 0;
            
          }  // if: coposite_relocation
          
          // r
          else if (composite.startsWith(COMPANY_CONSTRUCTOR_ID)) {
            
            // remove trailing constructor name and bracket
            composite = ((String)testResult).substring(
            COMPANY_CONSTRUCTOR_ID.length() + 1);
            // remove closing bracket
            composite = composite.substring(0, composite.length()-1);
            // System.out.println(composite);
            String name = "";
            String sitz = "";
            String districtCourt = null;
            String commercialRegisterID = null;
            int currentAttribut = 0;
            String currentAttributString = "";
            StringTokenizer compositeTokenizer =
            new StringTokenizer(composite);
            String compositeToken = null;
            boolean lookForString = false;
            
            while (compositeTokenizer.hasMoreTokens()) {
              compositeToken = compositeTokenizer.nextToken();
              if (lookForString) {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                if (compositeToken.endsWith("\"")) {
                  if (compositeToken.length() > 1) {
                    currentAttributString += compositeToken.substring(
                    0, compositeToken.length() - 1);
                  }
                  lookForString = false;
                }
                else {
                  currentAttributString += compositeToken;
                }
              }
              else if (compositeToken.equals(",")) {
                switch (currentAttribut) {
                  case 0: {
                    name = currentAttributString; break;
                  }
                  case 1: {
                    sitz = currentAttributString; break;
                  }
                  case 2: {
                    districtCourt = currentAttributString; break;
                  }
                }
                currentAttribut++;
                currentAttributString = "";
              }
              // 1 " und " 2: StringTokenizer
              else if (compositeToken.startsWith("\"")) {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                if (compositeToken.endsWith("\"")) {
                  if (compositeToken.length() > 1) {
                    // " may be a single token
                    currentAttributString += compositeToken.substring(
                    1, compositeToken.length() - 1);
                  }
                  else {
                    if (compositeToken.length() > 1) {
                      // " may be a single token
                      currentAttributString += compositeToken.substring(1);
                    }
                    lookForString = true;
                  }
                }
              }
              else if (compositeToken.equals("null")) {
                currentAttributString = "";
              }
              else {
                if (currentAttributString.length() != 0) {
                  currentAttributString += " ";
                }
                currentNamedEntityAttributes++;
                int index = (new Integer(compositeToken)).intValue();
                currentAttributString += noPlaceholderSentences[i + index];
              }
            }  // while:createCompositeNamedEntity
            commercialRegisterID = currentAttributString;  // last attribute
            
            // look for the most complex composite feature
            if (currentNamedEntityAttributes > maxNamedEntityAttributes) {
              newNamedEntity = new CompanyNE(0, null, NamedEntity.COMPANY, null,
              name, districtCourt, commercialRegisterID, sitz, null, null);
              maxNamedEntityString = name;
              // MaxNamedEntityString = NamedEntity.createCompany(name, sitz);
              featureType = NamedEntity.COMPANY;
              maxNamedEntityAttributes = currentNamedEntityAttributes;
              startIndexMaxNamedEntity = i;
              endIndexMaxNamedEntity = i + j;
            }
            currentNamedEntityAttributes = 0;
            
          }  // if: unternehmen
          
        }  // if:testResult
        
      }  // for:j
      
    }  // for:i
    
    // Is there a new maximum composite feature at the ending of
    // the sentence?
    if ((maxNamedEntityString != null)
    && (startIndexCurrentNamedEntity > endIndexMaxNamedEntity)) {
      // System.out.println(MaxNamedEntityString);
      compositeNamedEntitysStartIndex.add(
      new Integer(startIndexMaxNamedEntity));
      compositeNamedEntitysEndIndex.add(
      new Integer(endIndexMaxNamedEntity));
      int nextPlaceholderID = pcurrentDiasdemDocument.getNextNamedEntityIndex();
      String nextPlaceholder = NamedEntity.createPlaceholder(
      nextPlaceholderID);
      if (featureType == NamedEntity.PERSON) {
        pcurrentDiasdemDocument.addNamedEntity(
        new NamedEntity(nextPlaceholderID,
        maxNamedEntityString, nextPlaceholder, NamedEntity.PERSON));
      }
      if (featureType == NamedEntity.COMPANY) {
        pcurrentDiasdemDocument.addNamedEntity(
        new NamedEntity(nextPlaceholderID,
        maxNamedEntityString, nextPlaceholder, NamedEntity.COMPANY));
      }
      if (featureType == NamedEntity.COMPANY_RELOCATION) {
        pcurrentDiasdemDocument.addNamedEntity(new NamedEntity(
        nextPlaceholderID, maxNamedEntityString, nextPlaceholder,
        NamedEntity.COMPANY_RELOCATION));
      }
      featureType = NamedEntity.UNKNOWN;
      compositeNamedEntitysPlaceholder.add(nextPlaceholder);
    }
    
    // replace new composite features with placeholder
    StringBuffer result = new StringBuffer(satz.length());
    int[] placeholdersStartIndex =
    new int[compositeNamedEntitysStartIndex.size()];
    for (int i = 0; i < placeholdersStartIndex.length; i++) {
      placeholdersStartIndex[i] = ((Integer)compositeNamedEntitysStartIndex
      .get(i)).intValue();
    }
    int nextPlaceholderStartIndex = 0;
    for (int i = 0; i < tokens; i++) {
      if ((nextPlaceholderStartIndex < placeholdersStartIndex.length)
      && (i == placeholdersStartIndex[nextPlaceholderStartIndex])) {
        result.append((String)compositeNamedEntitysPlaceholder
        .get(nextPlaceholderStartIndex));
        result.append(" ");
        i = ((Integer)compositeNamedEntitysEndIndex.get(
        nextPlaceholderStartIndex)).intValue();
        nextPlaceholderStartIndex++;
      }
      else {
        result.append(placeholderSentences[i]);
        result.append(" ");
      }
    }
    
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String replaceOrganizations(String pSatz,
  NamedEntityOwner pcurrentDiasdemDocument) {
    
    // System.out.println("&&& " + pSatz); System.out.flush();;
    
    String satz = pSatz;
    StringBuffer result = null;
    int startIndexCurrentOrganization = -1;
    int endIndexCurrentOrganization = -1;
    int startIndexPreviousOrganization = -1;
    int endIndexPreviousOrganization = -1;
    
    ArrayList organizationsStartIndex = new ArrayList();
    ArrayList organizationsEndIndex = new ArrayList();
    ArrayList organizationsPlaceholder = new ArrayList();
    
    
    StringTokenizer tokenizer = new StringTokenizer(satz);
    int tokens = tokenizer.countTokens();
    // [<<1>> ist Nachname von <<2>>]
    String[] placeholderSentences = new String[tokens];
    // [<<nachname>> ist Nachname von <<vorname>>]
    String[] possibleTypesSentences = new String[tokens];
    // [Winkler ist Nachname von Karsten]
    String[] noPlaceholderSentences = new String[tokens];
    
    String token = null;
    int counter = 0;
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      placeholderSentences[counter] = token;
      // System.out.println("+++ token = " + token + " getNE=" +
      // pcurrentDiasdemDocument.getNamedEntity(token)); System.out.flush();
      if (NamedEntity.isPlaceholder(token)) {
        possibleTypesSentences[counter] =
        pcurrentDiasdemDocument.getNamedEntity(
        NamedEntity.getNamedEntityIndex(token))
        .getPossibleTypesPlaceholder();
        noPlaceholderSentences[counter] =
        pcurrentDiasdemDocument.getNamedEntity(
        NamedEntity.getNamedEntityIndex(token))
        .getToken();
      }
      else {
        possibleTypesSentences[counter] = token;
        noPlaceholderSentences[counter] = token;
      }
      
      // System.out.println (placeholderSentences[counter]+ " - " +
      //   possibleTypesSentences[counter] + " - " +
      //   noPlaceholderSentences[counter]);
      counter++;
    }
    
    ArrayList prunedOrganizationsStartIndex = null;
    ArrayList prunedOrganizationsEndIndex = null;
    String[] startPatternPath = null;
    // 1. look for all endings of company candidates by
    // searching for consecutive organization abbreviations
    
    for (int i = 0; i < tokens; i++) {
      // System.out.print(placeholderSentences[i]);
      if (Organizations.contains(placeholderSentences[i])) {
        // System.out.println("org.-abbr. = " + placeholderSentences[i]);
        if (endIndexPreviousOrganization >= 0) {
          if (endIndexPreviousOrganization == (i - 1)) {
            endIndexPreviousOrganization = i;
          }
          else {
            organizationsEndIndex.add(
            new Integer(endIndexPreviousOrganization));
            endIndexPreviousOrganization = i;
          }
        }
        else {
          endIndexPreviousOrganization = i;
        }
      }
      else {
        if (endIndexPreviousOrganization >= 0) {
          organizationsEndIndex.add(
          new Integer(endIndexPreviousOrganization));
          endIndexPreviousOrganization = -1;
        }
      }
    }  // for:all tokens
    if (endIndexPreviousOrganization >= 0) {
      organizationsEndIndex.add(
      new Integer(endIndexPreviousOrganization));
    }
    
    // 2. find the corresponding start for each company candidate
    Object testResult = null;
    if (organizationsEndIndex.size() > 0) {
      
      for (int i = 0; i < organizationsEndIndex.size(); i++) {
        endIndexCurrentOrganization =
        ((Integer)organizationsEndIndex.get(i)).intValue();
        startIndexCurrentOrganization = -1;
        for (int j = endIndexCurrentOrganization; j >= 0; j--) {
          
          // buggy? for (int k = 0; k < MaxStartOrgPatternLength; k++) {
          for (int k = 0; (k < MaxStartOrgPatternLength) && (k <= j); k++) {
            startPatternPath = new String[k + 1];
            for (int l = 0; (l < (k + 1)) && (l <= j); l++) {
              startPatternPath[l] =
              placeholderSentences[j - l].toLowerCase();
              // System.out.print(startPatternPath[l] + " ");
            }
            // System.out.println(startPatternPath.length +
            //   " j=" + j + " k=" +k + " +++ ");
            testResult = StartPatternTrie.get(startPatternPath);
            if (testResult != null) {
              startIndexCurrentOrganization = j + 1;
              organizationsStartIndex.add(new Integer(j + 1));
              k = MaxStartOrgPatternLength;  // break k
              j = -1; // break j
            }
          }  // start pattern can contain more than 1 token
          
          
        }  // look backward from a single ending
        // start = begin of organization
        if (startIndexCurrentOrganization < 0) {
          organizationsStartIndex.add(new Integer(0));
        }
        
      }  // look backward from all endings
      
      
      // 3. register companies with pcurrentDiasdemDocument
      StringBuffer organization = null;
      
      for (int i = 0; i < organizationsEndIndex.size(); i++) {
        startIndexCurrentOrganization =
        ((Integer)organizationsStartIndex.get(i)).intValue();
        endIndexCurrentOrganization =
        ((Integer)organizationsEndIndex.get(i)).intValue();
        // check if longer company exists that also starts here
        for (int k = i + 1; k < organizationsEndIndex.size(); k++) {
          // System.out.println("k " + (Integer)organizationsEndIndex.get(k)
          // + "; organizationsEndIndex.size() = "
          // + organizationsEndIndex.size());
          // System.out.println("i " + (Integer)organizationsEndIndex.get(i));
          if (((Integer)organizationsStartIndex.get(k)).equals(
          (Integer)organizationsStartIndex.get(i))
          && ((Integer)organizationsEndIndex.get(k)).compareTo(
          (Integer)organizationsEndIndex.get(i)) > 0) {
            endIndexCurrentOrganization = -1;
            break;
          }
        }
        // avoid extracting empty bogus companies such as GmbH
        // only extract the longest company name for each
        // startIndexCurrentOrganization, set corresponding
        // endIndexCurrentOrganization null if it has been
        // used for extracting a company name
        // System.out.println(startIndexCurrentOrganization
        // +"-"+endIndexCurrentOrganization);
        if ((startIndexCurrentOrganization < endIndexCurrentOrganization)
        && (endIndexCurrentOrganization >= 0)) {
          organization = new StringBuffer();
          for (int j = startIndexCurrentOrganization;
          j <= endIndexCurrentOrganization; j++) {
            organization.append(placeholderSentences[j]);
            organization.append(" ");
          }
          int nextPlaceholderID = pcurrentDiasdemDocument
          .getNextNamedEntityIndex();
          String nextPlaceholder =
          NamedEntity.createPlaceholder(nextPlaceholderID);
          organizationsPlaceholder.add(nextPlaceholder);
          pcurrentDiasdemDocument.addNamedEntity(
          new NamedEntity(nextPlaceholderID,
          organization.toString().trim(),
          nextPlaceholder, NamedEntity.ORGANIZATION));
        }
        else {
          organizationsEndIndex.set(i, null);
        }
      }  // look backward from all endings
      
      prunedOrganizationsStartIndex = new ArrayList();
      prunedOrganizationsEndIndex = new ArrayList();
      for (int k = 0; k < organizationsStartIndex.size(); k++) {
        if (organizationsEndIndex.get(k) != null) {
          prunedOrganizationsStartIndex.add(
          organizationsStartIndex.get(k));
          prunedOrganizationsEndIndex.add(
          organizationsEndIndex.get(k));
        }
      }
      
      // 4. replace companies with their placeholder string
      result = new StringBuffer(satz.length());
      int[] placeholdersStartIndex =
      new int[prunedOrganizationsStartIndex.size()];
      for (int i = 0; i < placeholdersStartIndex.length ; i++) {
        placeholdersStartIndex[i] = (
        (Integer)prunedOrganizationsStartIndex.get(i)).intValue();
      }
      int nextPlaceholderStartIndex = 0;
      for (int i = 0; i < tokens; i++) {
        if ((nextPlaceholderStartIndex < placeholdersStartIndex.length)
        && (i == placeholdersStartIndex[nextPlaceholderStartIndex])) {
          result.append((String)organizationsPlaceholder.get(
          nextPlaceholderStartIndex));
          result.append(" ");
          i = ((Integer)prunedOrganizationsEndIndex.get(
          nextPlaceholderStartIndex)).intValue();
          nextPlaceholderStartIndex++;
        }
        else {
          result.append(placeholderSentences[i]);
          result.append(" ");
        }
      }
      
      // System.out.println("\n---");
      // System.out.println(satz);
      // System.out.println(organizationsStartIndex);
      // System.out.println(organizationsEndIndex);
      // System.out.println(result.toString());
      
    }
    
    // 5. look for additional patterns to enhance company information
    
    // there is no organization in satz
    if (result == null) {
      result = new StringBuffer(satz);
    }
    
    // System.out.println(result.toString());
    
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String[] string2ArrayOfTokens(String pLine) {
    
    // creates array of String from pattern String
    
    String[] result = null;
    ArrayList resultVector = new ArrayList();
    StringTokenizer tokenizer =  new StringTokenizer(pLine);
    String token = null;
    StringBuffer tokenBuffer = null;
    boolean lookForPlaceholder = false;
    
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      if (lookForPlaceholder) {
        if (token.trim().endsWith(NamedEntity.PLACEHOLDER_SUFFIX)) {
          tokenBuffer.append(token);
          resultVector.add(tokenBuffer.toString());
          lookForPlaceholder = false;
        }  // if:endOfCurrentPlaceholder=true
        else {
          tokenBuffer.append(token);
          tokenBuffer.append(" ");
        }  // if:endOfCurrentPlaceholder=false
      }  // if:lookForPlaceholder=true
      else {
        if (token.trim().startsWith(NamedEntity.PLACEHOLDER_PREFIX)) {
          if (token.trim().endsWith(NamedEntity.PLACEHOLDER_SUFFIX)) {
            resultVector.add(token);
          }
          else {
            tokenBuffer = new StringBuffer();
            tokenBuffer.append(token);
            tokenBuffer.append(" ");
            lookForPlaceholder = true;
          }
        }  // if:startOfNewPlaceholder=true
        else {
          resultVector.add(token);
        }  // if:startOfNewPlaceholder=false
      }  // if:lookForPlaceholder=false
    } // while:allTokens
    
    result = new String[ resultVector.size() ];
    for (int i = 0; i < result.length; i++) {
      result[i] = (String)(resultVector.get(i));
    }
    
    return result;
    
  }  // string2ArrayOfTokens()
  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void fillPatternArrays() {
    
    int i = 0;
    
    try {
      CompositeNeCsvFile = new CsvFile
      (MyTaskParameter.CompositeFeaturesFileName, true);
      CompositeNeValues = new  Object [CompositeNeCsvFile.countAttributes()];
      Patterns = new String[CompositeNeCsvFile.countAllLines() - 2];
      MyCompositeNamedEntity = new String[CompositeNeCsvFile
      .countAllLines() - 2];
      NumberOfCompositePatterns = Patterns.length;
      
      CompositeNeValues = CompositeNeCsvFile.getNextTuple();
      while (CompositeNeValues[0] != null) {
        Patterns[i] = (String) CompositeNeValues[0];
        MyCompositeNamedEntity[i] = (String) CompositeNeValues[1];
        i++;
        CompositeNeValues = CompositeNeCsvFile.getNextTuple();
      }
      CompositeNeCsvFile.getCloseTuple();
      
    }
    catch (IOException e) {
      System.out.println("Fehler beim Lesen der Datei");
    }
    
    
    i = 0;
    
    try {
      boolean done = false;
      CompositeNeCsvFile = new CsvFile
      (MyTaskParameter.OrganizationsStartFileName, true);
      CompositeNeValues = new  Object [CompositeNeCsvFile.countAttributes()];
      StartPatterns = new String[CompositeNeCsvFile.countAllLines() - 2];
      NumberOfOrgStartPatterns = StartPatterns.length;
      
      // int counter = 1;
      StringTokenizer tokenizer = null;
      while (!done) {
        CompositeNeValues = CompositeNeCsvFile.getNextTuple();
        // System.out.println(counter++ + ": " + CompositeNeValues);
        if (CompositeNeValues[0] != null) {
          StartPatterns[i] = (String) CompositeNeValues[0];
          tokenizer = new StringTokenizer(StartPatterns[i]);
          MaxStartOrgPatternLength = Math.max(
          MaxStartOrgPatternLength, tokenizer.countTokens());
          i ++;
        }
        else {
          done = true;
        }
      }
      CompositeNeCsvFile.getCloseTuple();
      
    }
    catch (IOException e) {
      System.out.println("Fehler beim Lesen der Datei");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createCanonicalNEs(NamedEntityOwner pCurrentNamedEntityOwner) {
    
    ArrayList storedVectorOfPersons = new ArrayList();
    ArrayList storedVectorOfCompanies = new ArrayList();
    
    NamedEntity namedEntity = null;
    for (int i = 0; i < pCurrentNamedEntityOwner.getNumberOfNamedEntities();
    i++) {
      namedEntity = pCurrentNamedEntityOwner.getNamedEntity(i);
      if (namedEntity instanceof PersonNE) {
        storedVectorOfPersons.add(namedEntity);
      }
      else if (namedEntity instanceof CompanyNE) {
        storedVectorOfCompanies.add(namedEntity);
      }
    }
    
    ArrayList vPerson = CurrentPerson.groupSimilarPersonNEs(
    storedVectorOfPersons);
    ArrayList vCompany = CurrentCompany.groupSimilarCompanyNEs(
    storedVectorOfCompanies);
    
    storedVectorOfPersons.clear();
    storedVectorOfCompanies.clear();
    
    ArrayList [] canonicalNumbers = new ArrayList[
    vPerson.size()+vCompany.size()];
    int canonicalNumberLength = 0;
    
    // Henner: if (vPerson.size() > 1) {
    if (vPerson.size() >= 1) {
      for (int i = 0; i < vPerson.size(); i ++) {
        
        ((PersonNE)vPerson.get(i))
        .setID(pCurrentNamedEntityOwner.getNextNamedEntityIndex());
        ((PersonNE)vPerson.get(i)).setPlaceholder
        (NamedEntity.PLACEHOLDER_PREFIX +  ((PersonNE)vPerson
        .get(i)).getID()+NamedEntity.PLACEHOLDER_SUFFIX);
        pCurrentNamedEntityOwner.addNamedEntity(
        (PersonNE)vPerson.get(i));
        
        // debug // hier kommt der StringTokenizer drum und danach
        // ab in den Vektor um getFirstTextUnitNamedEntities zu bearbeiten
        
        canonicalNumbers[canonicalNumberLength] = new ArrayList();
        String numberSummary = ((PersonNE)vPerson.get(i)).getCanonical();
        StringTokenizer numberTokens = new StringTokenizer(numberSummary, ",");
        
        while (numberTokens.hasMoreTokens()) {
          canonicalNumbers[canonicalNumberLength].add
          (numberTokens.nextToken());
        }
        canonicalNumberLength++;
        
      }
    }
    
    if (vCompany.size() >= 1) {
      for (int i = 0; i < vCompany.size(); i ++) {
        ((CompanyNE)vCompany.get(i))
        .setID(pCurrentNamedEntityOwner.getNextNamedEntityIndex());
        
        ((CompanyNE)vCompany.get(i)).setPlaceholder
        (NamedEntity.PLACEHOLDER_PREFIX +  ((CompanyNE)vCompany
        .get(i)).getID()+NamedEntity.PLACEHOLDER_SUFFIX);
        pCurrentNamedEntityOwner.addNamedEntity((CompanyNE)vCompany.get(i));
        
        canonicalNumbers[canonicalNumberLength] = new ArrayList();
        String numberSummary = ((CompanyNE)vCompany.get(i)).getCanonical();
        StringTokenizer numberTokens = new StringTokenizer(numberSummary, ",");
        
        while (numberTokens.hasMoreTokens()) {
          canonicalNumbers[canonicalNumberLength].add
          (numberTokens.nextToken());
        }
        canonicalNumberLength++;
        
      }
    }
    
    String replacedString = "";
    String searched = null;
    String toreplace = null;
    RE replaceCanonicals, finalMatch = null;
    REMatchEnumeration match = null;
    REMatch oneMatch = null;
    
    String satz = null;
    for (int k = 0; k < pCurrentNamedEntityOwner
    .getNumberOfProcessedTextUnits(); k++) {
      
      satz = pCurrentNamedEntityOwner.getProcessedTextUnit(k)
      .getContentsAsString();
      for (int i = 0; i < (vPerson.size() + vCompany.size()); i ++) {
        toreplace = new StringBuffer()
        .append(NamedEntity.PLACEHOLDER_PREFIX)
        .append(canonicalNumbers[i].get(0).toString())
        .append(NamedEntity.PLACEHOLDER_SUFFIX)
        .toString();
        for (int j = 1; j < canonicalNumbers[i].size(); j ++) {
          try {
            searched = new StringBuffer()
            .append(NamedEntity.PLACEHOLDER_PREFIX)
            .append(canonicalNumbers[i].get(j).toString())
            .append(NamedEntity.PLACEHOLDER_SUFFIX)
            .toString();
            replaceCanonicals = new RE(searched);
            match = replaceCanonicals.getMatchEnumeration(satz);
            while (match.hasMoreElements()) {
              oneMatch = (REMatch) match.nextElement();
              finalMatch = new RE(searched);
              canonicalNumbers[i].remove(j);
              j --;
              replacedString = finalMatch.substitute(satz, toreplace);
              satz = replacedString;
            }
          }
          catch (REException rException) {
            rException.printStackTrace();
          }
        }  // end of for j
      } // end of i
      
      pCurrentNamedEntityOwner.getProcessedTextUnit(k)
      .setContentsFromString(satz);
    } // end of k
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}