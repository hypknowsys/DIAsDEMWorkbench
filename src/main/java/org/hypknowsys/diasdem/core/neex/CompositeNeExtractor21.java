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
import org.hypknowsys.misc.util.StringArrayTrie;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class CompositeNeExtractor21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityExtractorParameter NeTaskParameter = null;
  
  private StringArrayTrie CompositeNeTrie = null;
  
  private ArrayList AllCompositeNeStartIndexes = null;
  private ArrayList AllCompositeNeEndIndexes = null;
  private ArrayList AllCompositeNePlaceholders = null;
  
  // [<<1>> ist Surname von <<2>>]
  private String[] Tokens_NeAsPlaceholder = null;
  // [<<surname>> ist Surname von <<forename>>]
  private String[] Tokens_NeAsPossibleType = null;
  // [Winkler ist Surname von Karsten]
  private String[] Tokens_NeAsToken = null;
  
  private String Name = "";
  private String Surname = "";
  private String Forename = "";
  private String Title = "";
  private String MiddleInitial = "";
  private String DateOfBirth = "";
  private String MothersName = "";
  private String PlaceOfResidence = "";
  private String StreetOfResidence = "";
  private String Occupation = "";
  
  // private String Name = "";
  private String OriginPlace = "";
  private String OriginStreet = "";
  private String OriginDistrictCourt = null;
  private String OriginCommercialRegisterID = null;
  private String DestinationPlace = "";
  private String DestinationStreet = "";
  private String DestinationDistrictCourt = null;
  private String DestinationCommercialRegisterID = null;
  
  // private String Name = "";
  private String Place = "";
  private String Street = "";
  private String DistrictCourt = null;
  private String CommercialRegisterID = null;
  
  private String PeriodBeginDate = "";
  private String PeriodEndDate = "";
  
  private String MinimumAmountOfMoney = "";
  private String MaximumAmountOfMoney = "";
  
  private String MinimumPercentage = "";
  private String MaximumPercentage = "";
  
  private String CompanyName = "";
  private String NumberOfShares = "";
  private String PercentageOfShares = "";
  
  private String Value = "";
  
  private String NameOfUnit = null;
  private String PlaceOfUnitHeadquarter = null;
  private String StreetOfUnitHeadquarter = null;
  private String DistrictCourtOfUnit = null;
  private String CommercialRegisterIDOfUnit = null;
  private String NameOfParent = null;
  private String PlaceOfParentHeadquarter = null;
  private String StreetOfParentHeadquarter = null;
  private String DistrictCourtOfParent = null;
  private String CommercialRegisterIDOfParent = null;
  
  private boolean DebuggingMode = false;
  private TextFile DebuggingOutput = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringTokenizer CompositeTokenizer = null;
  private transient StringBuffer CurrentAttributeString = null;
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  
  private transient NamedEntity TmpNewNamedEntity = null;
  private transient String TmpMaxNamedEntityString = null;
  private transient int TmpCurrentNamedEntityAttributes = 0;
  private transient int TmpMaxNamedEntityAttributes = 0;
  private transient int TmpStartIndexCurrentNamedEntity = -1;
  private transient int TmpStartIndexMaxNamedEntity = -1;
  private transient int TmpEndIndexMaxNamedEntity = -1;
  private transient int TmpFeatureType = NamedEntity.UNKNOWN;
  private transient String[] TmpSubPattern = null;
  private transient Object TmpTestResult = null;
  private transient String TmpComposite = null;
  private transient String TmpCompositeToken = null;
  private transient boolean TmpLookForString = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  // company( Name , Place , Street , DistrictCourt , CommercialRegisterID )
  // person( Name , Surname , Forename , Title , MiddleInitial , DoB ,
  //   MothersName , Place , Street , Occupation)
  // company_relocation( Name , OriginPlace , OriginStreet ,
  //   OriginDistrictCourt , OriginCommercialRegisterID , DestinationPlace ,
  //   DestinationStreet , DestinationDistrictCourt ,
  //   DestinationCommercialRegisterID )
  // date_period( PeriodBeginDate , PeriodEndDate )
  // amount_of_money_range( MinimumAmountOfMoney , MaximumAmountOfMoney )
  // percentage_range( MinimumPercentage , MaximumPercentage )
  // equity_stake( CompanyName , NumberOfShares , PercentageOfShares )
  // key_figure( Name , Value )
  // unit_of_company( NameOfUnit , PlaceOfUnitHeadquarter , 
  //   StreetOfUnitHeadquarter , DistrictCourtOfUnit , 
  //   CommercialRegisterIDOfUnit , NameOfParent , PlaceOfParentHeadquarter ,
  //   StreetOfParentHeadquarter , DistrictCourtOfParent ,
  //   CommercialRegisterIDOfParent )
  
  // valid constructor:
  // "person( 1 , 0 , null , null , null , null , null , 3 , null , 5 )"
  private String PERSON_CONSTRUCTOR_ID = "person";
  // valid constructor:
  // "company( 0 , null , null , null , null )"
  private String COMPANY_CONSTRUCTOR_ID = "company";
  // valid constructor:
  // "company( 0 , null , null , null , null , null , null , null , null )"
  private String COMPANY_RELOCATION_CONSTRUCTOR_ID = "company_relocation";
  // valid constructor: "date_period( 1 , 3 )"
  private String DATE_PERIOD_CONSTRUCTOR_ID = "date_period";
  // valid constructor: "amount_of_money_range( 1 , 3 )"
  private String AMOUNT_OF_MONEY_RANGE_CONSTRUCTOR_ID = "amount_of_money_range";
  // valid constructor: "percentage_range( 1 , 3 )"
  private String PERCENTAGE_RANGE_CONSTRUCTOR_ID = "percentage_range";
  // valid constructor: "equity_stake( 0 , 3 , null )"
  private String EQUITY_STAKE_CONSTRUCTOR_ID = "equity_stake";
  // valid constructor: "key_figure( 0 , 1 )"
  private String KEY_FIGURE_CONSTRUCTOR_ID = "key_figure";
  // valid constructor:
  // "unit_of_company( 0 , 1 , 2 , 3 , 4 , null , null , null , null , null )"
  private String UNIT_OF_COMPANY_CONSTRUCTOR_ID = "unit_of_company";
  // isValidCompositeNeConstructor() must be updated after adding a new NE
  
  private static final int SEARCH_FOR_PERSON = 1;
  private static final int SEARCH_FOR_COMPANY = 2;
  private static final int SEARCH_FOR_COMPANY_RELOCATION = 3;
  private static final int SEARCH_FOR_DATE_PERIOD = 4;
  private static final int SEARCH_FOR_AMOUNT_OF_MONEY_RANGE = 5;
  private static final int SEARCH_FOR_PERCENTAGE_RANGE = 6;
  private static final int SEARCH_FOR_EQUITY_STAKE = 7;
  private static final int SEARCH_FOR_KEY_FIGURE = 8;
  private static final int SEARCH_FOR_UNIT_OF_COMPANY = 9;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompositeNeExtractor21(
  NamedEntityExtractorParameter pNeTaskParameter) {
    
    NeTaskParameter = pNeTaskParameter;
    DebuggingMode = NeTaskParameter.createDebuggingFiles();
    if (DebuggingMode) {
      Template debuggingOutputHeader = new Template(Tools
      .stringFromTextualSystemResource("org/hypknowsys/diasdem/resources/html/"
      + "HtmlFile_HeaderTemplate.html"));
      debuggingOutputHeader.addValue("${Title}", "NEEX 2.1: Composite "
      + " NEs Extracted by OrganizationBasicNeExtractor");
      DebuggingOutput = new TextFile(new File(Tools.ensureTrailingSlash(
      NeTaskParameter.getDebuggingFileDirectory())
      + "Neex21_CompositeNEs.html"));
      DebuggingOutput.open();
      DebuggingOutput.setFirstLine(debuggingOutputHeader.insertValues());
      DebuggingOutput.close();
    }
    
    // read composite NE file: each line contains the pattern to search
    // for and the composite NE type string separated by a tab stop;
    // comment lines start with '#'
    CompositeNeTrie = new StringArrayTrie();
    if (NeTaskParameter.getCompositeFeaturesFileName() != null
    && NeTaskParameter.getCompositeFeaturesFileName().length() > 0) {
      TextFile compositeNeFile = new TextFile(new File(NeTaskParameter
      .getCompositeFeaturesFileName()));
      compositeNeFile.openReadOnly();
      String[] contents = null;
      String line = compositeNeFile
      .getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        contents = line.split("\t");
        if (contents.length == 2 && !Tools.stringIsNullOrEmpty(contents[0])
        && this.isValidCompositeNeConstructor(contents[1])) {
          CompositeNeTrie.put(NamedEntityExtractor21.string2ArrayOfTokens(
          new String(contents[0].trim())), new String(contents[1].trim()));
        }
        else {
          System.err.println("[CompositeNeExtractor21] Syntax error in"
          + " file " + NeTaskParameter.getCompositeFeaturesFileName() + ": "
          + " Skipping line \"" + line + "\"");
        }
        line = compositeNeFile.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      // System.out.println("NEEX 2.1: List of composite NE rules loaded");
      compositeNeFile.close();
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
  
  public String replaceCompositeNamedEntities(String pSatz,
  NamedEntityOwner pCurrentNeOwner) {
    
    TmpNewNamedEntity = null;
    
    TmpMaxNamedEntityString = null;
    TmpCurrentNamedEntityAttributes = 0;
    TmpMaxNamedEntityAttributes = 0;
    TmpStartIndexCurrentNamedEntity = -1;
    TmpStartIndexMaxNamedEntity = -1;
    TmpEndIndexMaxNamedEntity = -1;
    
    AllCompositeNeStartIndexes = new ArrayList();
    AllCompositeNeEndIndexes = new ArrayList();
    AllCompositeNePlaceholders = new ArrayList();
    
    StringTokenizer tokenizer = new StringTokenizer(pSatz);
    int tokens = tokenizer.countTokens();
    // [<<1>> ist Surname von <<2>>]
    Tokens_NeAsPlaceholder = new String[tokens];
    // [<<Surname>> ist Surname von <<Forename>>]
    Tokens_NeAsPossibleType = new String[tokens];
    // [Winkler ist Surname von Karsten]
    Tokens_NeAsToken = new String[tokens];
    String token = null;
    int counter = 0;
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      Tokens_NeAsPlaceholder[counter] = token;
      if (NamedEntity.isPlaceholder(token)) {
        Tokens_NeAsPossibleType[counter] = pCurrentNeOwner.getNamedEntity(
        NamedEntity.getNamedEntityIndex(token)).getPossibleTypesPlaceholder();
        Tokens_NeAsToken[counter] = pCurrentNeOwner.getNamedEntity(
        NamedEntity.getNamedEntityIndex(token)).getToken();
      }
      else {
        Tokens_NeAsPossibleType[counter] = token;
        Tokens_NeAsToken[counter] = token;
      }
      // System.out.println(Tokens_NeAsPlaceholder[counter] + " - "
      // + Tokens_NeAsPossibleType[counter] + " - "
      // + Tokens_NeAsToken[counter]);
      counter++;
    }
    
    TmpFeatureType = NamedEntity.UNKNOWN;
    TmpSubPattern = null;
    TmpTestResult = null;
    TmpComposite = null;
    
    boolean continueSearching = true;
    for (int i = 0; i < tokens; i++) {
      
      TmpStartIndexCurrentNamedEntity = i;
      continueSearching = true;
      
      // System.out.println("\n" + pSatz);
      for (int j = 0; j < (tokens - i) && continueSearching; j++) {
        
        TmpSubPattern = new String[j + 1];
        // System.out.println("--");
        for (int k = 0; k < (j + 1); k++) {
          if (NamedEntity.isPlaceholder(Tokens_NeAsPlaceholder[i + k])) {
            TmpSubPattern[k] = Tokens_NeAsPossibleType[i + k];
          }
          else {
            TmpSubPattern[k] = Tokens_NeAsPlaceholder[i + k];
          }
          // System.out.print(i + " " + j + " " + k + ": "
          // + TmpSubPattern[k] + " ## ");
          // System.out.print(Tokens_NeAsPossibleType[i + k] + " ");
        }
        
        TmpTestResult = CompositeNeTrie.getAndCheckIfKeyIsPrefixOfOtherKey(
        TmpSubPattern);
        // System.out.println("CompositeNeTrie.get(TmpSubPattern) = "
        // + (String)TmpTestResult);
        
        // System.out.println(TmpNewNamedEntity);
        if (TmpTestResult == null) {
          
          // Is there a new maximum composite feature?
          if ((TmpMaxNamedEntityString != null)
          && (TmpStartIndexCurrentNamedEntity > TmpEndIndexMaxNamedEntity)) {
            AllCompositeNeStartIndexes.add(
            new Integer(TmpStartIndexMaxNamedEntity));
            AllCompositeNeEndIndexes.add(
            new Integer(TmpEndIndexMaxNamedEntity));
            int nextPlaceholderID = pCurrentNeOwner.getNextNamedEntityIndex();
            String nextPlaceholder = NamedEntity.createPlaceholder(
            nextPlaceholderID);
            if (TmpFeatureType == NamedEntity.PERSON
            || TmpFeatureType == NamedEntity.COMPANY
            || TmpFeatureType == NamedEntity.COMPANY_RELOCATION
            || TmpFeatureType == NamedEntity.DATE_PERIOD
            || TmpFeatureType == NamedEntity.AMOUNT_OF_MONEY_RANGE
            || TmpFeatureType == NamedEntity.PERCENTAGE_RANGE
            || TmpFeatureType == NamedEntity.EQUITY_STAKE
            || TmpFeatureType == NamedEntity.KEY_FIGURE
            || TmpFeatureType == NamedEntity.UNIT_OF_COMPANY) {
              TmpNewNamedEntity.setID(nextPlaceholderID);
              TmpNewNamedEntity.setPlaceholder(nextPlaceholder);
              pCurrentNeOwner.addNamedEntity(TmpNewNamedEntity);
            }
            TmpFeatureType = NamedEntity.UNKNOWN;
            AllCompositeNePlaceholders.add(nextPlaceholder);
            TmpMaxNamedEntityString = null;
            TmpMaxNamedEntityAttributes = 0;
          }
          
          if (!CompositeNeTrie.previousMatchIsPrefixOfKey()) {
            continueSearching = false;
          }
          
        }
        else {
          
          // System.out.println("### " + TmpTestResult);
          // System.out.println((String)TmpTestResult + " -- " + pSatz);
          TmpComposite = (String)TmpTestResult;
          
          if (TmpComposite.startsWith(PERSON_CONSTRUCTOR_ID)) {
            
            Name = "";
            Surname = "";
            Forename = "";
            Title = "";
            MiddleInitial = "";
            DateOfBirth = "";
            MothersName = "";
            PlaceOfResidence = "";
            StreetOfResidence = "";
            Occupation = "";
            this.extractCompositeNE(PERSON_CONSTRUCTOR_ID,
            SEARCH_FOR_PERSON, i, j);
            
          }  // if: person
          
          else if (TmpComposite.startsWith(COMPANY_RELOCATION_CONSTRUCTOR_ID)) {
            
            Name = "";
            OriginPlace = "";
            OriginStreet = "";
            OriginDistrictCourt = null;
            OriginCommercialRegisterID = null;
            DestinationPlace = "";
            DestinationStreet = "";
            DestinationDistrictCourt = null;
            DestinationCommercialRegisterID = null;
            this.extractCompositeNE(COMPANY_RELOCATION_CONSTRUCTOR_ID,
            SEARCH_FOR_COMPANY_RELOCATION, i, j);
            
          }  // if: coposite_relocation
          
          else if (TmpComposite.startsWith(COMPANY_CONSTRUCTOR_ID)) {
            
            Name = "";
            Place = "";
            Street = "";
            DistrictCourt = null;
            CommercialRegisterID = null;
            this.extractCompositeNE(COMPANY_CONSTRUCTOR_ID,
            SEARCH_FOR_COMPANY, i, j);
            
          }  // if: company
          
          else if (TmpComposite.startsWith(DATE_PERIOD_CONSTRUCTOR_ID)) {
            
            PeriodBeginDate = "";
            PeriodEndDate = "";
            this.extractCompositeNE(DATE_PERIOD_CONSTRUCTOR_ID,
            SEARCH_FOR_DATE_PERIOD, i, j);
            
          }  // if: date_period
          
          else if (TmpComposite.startsWith(
          AMOUNT_OF_MONEY_RANGE_CONSTRUCTOR_ID)) {
            
            MinimumAmountOfMoney = "";
            MaximumAmountOfMoney = "";
            this.extractCompositeNE(AMOUNT_OF_MONEY_RANGE_CONSTRUCTOR_ID,
            SEARCH_FOR_AMOUNT_OF_MONEY_RANGE, i, j);
            
          }  // if: amount_of_money_range
          
          else if (TmpComposite.startsWith(PERCENTAGE_RANGE_CONSTRUCTOR_ID)) {
            
            MinimumPercentage = "";
            MaximumPercentage = "";
            this.extractCompositeNE(PERCENTAGE_RANGE_CONSTRUCTOR_ID,
            SEARCH_FOR_PERCENTAGE_RANGE, i, j);
            
          }  // if: percentage_range
          
          else if (TmpComposite.startsWith(EQUITY_STAKE_CONSTRUCTOR_ID)) {
            
            CompanyName = "";
            NumberOfShares = "";
            PercentageOfShares = "";
            this.extractCompositeNE(EQUITY_STAKE_CONSTRUCTOR_ID,
            SEARCH_FOR_EQUITY_STAKE, i, j);
            
          }  // if: equity_stake
          
          else if (TmpComposite.startsWith(KEY_FIGURE_CONSTRUCTOR_ID)) {
            
            Name = "";
            Value = "";
            this.extractCompositeNE(KEY_FIGURE_CONSTRUCTOR_ID,
            SEARCH_FOR_KEY_FIGURE, i, j);
            
          }  // if: key_figure
          
          else if (TmpComposite.startsWith(UNIT_OF_COMPANY_CONSTRUCTOR_ID)) {
            
            NameOfUnit = "";
            PlaceOfUnitHeadquarter = "";
            StreetOfUnitHeadquarter = "";
            DistrictCourtOfUnit = "";
            CommercialRegisterIDOfUnit = "";
            NameOfParent = "";
            PlaceOfParentHeadquarter = "";
            StreetOfParentHeadquarter = "";
            DistrictCourtOfParent = "";
            CommercialRegisterIDOfParent = "";
            this.extractCompositeNE(UNIT_OF_COMPANY_CONSTRUCTOR_ID,
            SEARCH_FOR_UNIT_OF_COMPANY, i, j);
            
          }  // if: unit_of_company
          
        }  // if:testResult
        
      }  // for:j
      
    }  // for:i
    
    // Is there a new maximum composite feature at the ending of
    // the sentence?
    if ((TmpMaxNamedEntityString != null)
    && (TmpStartIndexCurrentNamedEntity >= TmpEndIndexMaxNamedEntity)) {
      AllCompositeNeStartIndexes.add(
      new Integer(TmpStartIndexMaxNamedEntity));
      AllCompositeNeEndIndexes.add(
      new Integer(TmpEndIndexMaxNamedEntity));
      int nextPlaceholderID = pCurrentNeOwner.getNextNamedEntityIndex();
      String nextPlaceholder = NamedEntity.createPlaceholder(
      nextPlaceholderID);
      if (TmpFeatureType == NamedEntity.PERSON
      || TmpFeatureType == NamedEntity.COMPANY
      || TmpFeatureType == NamedEntity.COMPANY_RELOCATION
      || TmpFeatureType == NamedEntity.DATE_PERIOD
      || TmpFeatureType == NamedEntity.AMOUNT_OF_MONEY_RANGE
      || TmpFeatureType == NamedEntity.PERCENTAGE_RANGE
      || TmpFeatureType == NamedEntity.EQUITY_STAKE
      || TmpFeatureType == NamedEntity.KEY_FIGURE
      || TmpFeatureType == NamedEntity.UNIT_OF_COMPANY) {
        TmpNewNamedEntity.setID(nextPlaceholderID);
        TmpNewNamedEntity.setPlaceholder(nextPlaceholder);
        pCurrentNeOwner.addNamedEntity(TmpNewNamedEntity);
      }
      TmpFeatureType = NamedEntity.UNKNOWN;
      AllCompositeNePlaceholders.add(nextPlaceholder);
      TmpMaxNamedEntityString = null;
      TmpMaxNamedEntityAttributes = 0;
    }
    
    // replace new composite features with placeholder
    StringBuffer result = new StringBuffer(pSatz.length());
    int[] placeholdersStartIndex =
    new int[AllCompositeNeStartIndexes.size()];
    for (int i = 0; i < placeholdersStartIndex.length ; i++) {
      placeholdersStartIndex[i] = ((Integer)AllCompositeNeStartIndexes
      .get(i)).intValue();
    }
    // begin: construct the complete token (i.e., text to be replaced by the
    // respective placeholder) for each composite NE; author: Henner Graubitz
    for (int i = 0; i < AllCompositeNePlaceholders.size(); i ++) {
      this.TmpStringBuffer = new StringBuffer();
      for (int j = placeholdersStartIndex[i], k = ((Integer)
      AllCompositeNeEndIndexes.get(i)).intValue(); j <= k; j ++) {
        if (NamedEntity.isPlaceholder(Tokens_NeAsPlaceholder[j])) {
          TmpStringBuffer.append(Tokens_NeAsToken[j]).append(" ");
        }
        else {
          TmpStringBuffer.append(Tokens_NeAsPlaceholder[j]).append(" ");
        }
      }
      pCurrentNeOwner.getNamedEntity(NamedEntity.getNamedEntityIndex(
      AllCompositeNePlaceholders.get(i).toString()))
      .setToken(TmpStringBuffer.toString().trim());
    }
    // end: construct the complete token (i.e., text to be replaced by the
    // respective placeholder) for each composite NE; author: Henner Graubitz
    int nextPlaceholderStartIndex = 0;
    for (int i = 0; i < tokens; i++) {
      if ((nextPlaceholderStartIndex < placeholdersStartIndex.length)
      && (i == placeholdersStartIndex[nextPlaceholderStartIndex])) {
        if (DebuggingMode) {
          this.appendDebuggingFile("<font class=\"redBold\">"
          + "<a title=\""
          + ((CompositeNE)pCurrentNeOwner.getNamedEntity(NamedEntity
          .getNamedEntityIndex((String)AllCompositeNePlaceholders
          .get(nextPlaceholderStartIndex)))).getAttributeString()
          + "\">"
          + ((CompositeNE)pCurrentNeOwner.getNamedEntity(NamedEntity
          .getNamedEntityIndex((String)AllCompositeNePlaceholders
          .get(nextPlaceholderStartIndex)))).getToken()
          + "</a></font> ", false);
        }
        result.append((String)AllCompositeNePlaceholders
        .get(nextPlaceholderStartIndex));
        result.append(" ");
        i = ((Integer)AllCompositeNeEndIndexes.get(
        nextPlaceholderStartIndex)).intValue();
        nextPlaceholderStartIndex++;
      }
      else {
        if (DebuggingMode) {
          if (NamedEntity.isPlaceholder(Tokens_NeAsPlaceholder[i])) {
            this.appendDebuggingFile("<font class=\"greenBold\">"
            + "<a title=\""
            + ((NamedEntity)pCurrentNeOwner.getNamedEntity(NamedEntity
            .getNamedEntityIndex(Tokens_NeAsPlaceholder[i])))
            .getMostProbableTypeString()
            + "\">"
            + Tokens_NeAsToken[i] + "</a></font> ", false);
          }
          else {
            this.appendDebuggingFile(Tokens_NeAsPlaceholder[i] + " ", true);
          }
        }
        result.append(Tokens_NeAsPlaceholder[i]);
        result.append(" ");
      }
    }
    
    if (DebuggingMode) {
      this.appendDebuggingFile("<p>", false);
    }
    
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String[] string2ArrayOfTokens(String pLine) {
    
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
  
  private void extractCompositeNE(String pConstructurID, int pSearchFor,
  int pI, final int pJ) {
    
    // remove trailing constructor name and bracket
    TmpComposite = ((String)TmpTestResult).substring(pConstructurID
    .length() + 1);
    // remove closing bracket
    TmpComposite = TmpComposite.substring(0, TmpComposite.length() - 1).trim();
    // System.out.println("extractCompositeNE: " + TmpComposite);
    int currentAttribut = 0;
    CurrentAttributeString = new StringBuffer(1000);
    CompositeTokenizer = new StringTokenizer(TmpComposite);
    TmpCompositeToken = null;
    TmpLookForString = false;
    
    while (CompositeTokenizer.hasMoreTokens()) {
      TmpCompositeToken = CompositeTokenizer.nextToken();
      if (TmpLookForString) {
        if (CurrentAttributeString.length() != 0) {
          CurrentAttributeString.append(" ");
        }
        if (TmpCompositeToken.endsWith("\"")) {
          if (TmpCompositeToken.length() > 1) {
            CurrentAttributeString.append(TmpCompositeToken.substring(0,
            TmpCompositeToken.length() - 1));
          }
          TmpLookForString = false;
        }
        else {
          CurrentAttributeString.append(TmpCompositeToken);
        }
      }
      else if (TmpCompositeToken.equals(",")) {
        switch (pSearchFor) {
          case SEARCH_FOR_PERSON: {
            switch (currentAttribut) {
              case 0: {
                Name = CurrentAttributeString.toString();
                break;
              }
              case 1: {
                Surname = CurrentAttributeString.toString();
                break;
              }
              case 2: {
                Forename = CurrentAttributeString.toString();
                break;
              }
              case 3: {
                Title = CurrentAttributeString.toString();
                break;
              }
              case 4: {
                MiddleInitial = CurrentAttributeString.toString();
                break;
              }
              case 5: {
                DateOfBirth = CurrentAttributeString.toString();
                break;
              }
              case 6: {
                MothersName = CurrentAttributeString.toString();
                break;
              }
              case 7: {
                PlaceOfResidence = CurrentAttributeString.toString();
                break;
              }
              case 8: {
                StreetOfResidence = CurrentAttributeString.toString();
                break;
              }
            }
            break;
          }
          case SEARCH_FOR_COMPANY_RELOCATION: {
            switch (currentAttribut) {
              case 0: {
                Name = CurrentAttributeString.toString();
                break;
              }
              case 1: {
                OriginPlace = CurrentAttributeString.toString();
                break;
              }
              case 2: {
                OriginStreet = CurrentAttributeString.toString();
                break;
              }
              case 3: {
                OriginDistrictCourt = CurrentAttributeString.toString();
                break;
              }
              case 4: {
                OriginCommercialRegisterID = CurrentAttributeString
                .toString();
                break;
              }
              case 5: {
                DestinationPlace = CurrentAttributeString.toString();
                break;
              }
              case 6: {
                DestinationStreet = CurrentAttributeString.toString();
                break;
              }
              case 7: {
                DestinationDistrictCourt = CurrentAttributeString
                .toString();
                break;
              }
            }
            break;
          }
          case SEARCH_FOR_COMPANY: {
            switch (currentAttribut) {
              case 0: {
                Name = CurrentAttributeString.toString();
                break;
              }
              case 1: {
                Place = CurrentAttributeString.toString();
                break;
              }
              case 2: {
                Street = CurrentAttributeString.toString();
                break;
              }
              case 3: {
                DistrictCourt = CurrentAttributeString.toString();
                break;
              }
            }
            break;
          }
          case SEARCH_FOR_DATE_PERIOD: {
            switch (currentAttribut) {
              case 0: {
                PeriodBeginDate = CurrentAttributeString.toString();
                break;
              }
            }
            break;
          }
          case SEARCH_FOR_AMOUNT_OF_MONEY_RANGE: {
            switch (currentAttribut) {
              case 0: {
                MinimumAmountOfMoney = CurrentAttributeString.toString();
                break;
              }
            }
            break;
          }
          case SEARCH_FOR_PERCENTAGE_RANGE: {
            switch (currentAttribut) {
              case 0: {
                MinimumPercentage = CurrentAttributeString.toString();
                break;
              }
            }
            break;
          }
          case SEARCH_FOR_EQUITY_STAKE: {
            switch (currentAttribut) {
              case 0: {
                CompanyName = CurrentAttributeString.toString();
                break;
              }
              case 1: {
                NumberOfShares = CurrentAttributeString.toString();
                break;
              }
            }
            break;
          }
          case SEARCH_FOR_KEY_FIGURE: {
            switch (currentAttribut) {
              case 0: {
                Name = CurrentAttributeString.toString();
                break;
              }
            }
            break;
          }
          case SEARCH_FOR_UNIT_OF_COMPANY: {
            switch (currentAttribut) {
              case 0: {
                NameOfUnit = CurrentAttributeString.toString();
                break;
              }
              case 1: {
                PlaceOfUnitHeadquarter = CurrentAttributeString.toString();
                break;
              }
              case 2: {
                StreetOfUnitHeadquarter = CurrentAttributeString.toString();
                break;
              }
              case 3: {
                DistrictCourtOfUnit = CurrentAttributeString.toString();
                break;
              }
              case 4: {
                CommercialRegisterIDOfUnit = CurrentAttributeString
                .toString();
                break;
              }
              case 5: {
                NameOfParent = CurrentAttributeString.toString();
                break;
              }
              case 6: {
                PlaceOfParentHeadquarter = CurrentAttributeString.toString();
                break;
              }
              case 7: {
                StreetOfParentHeadquarter = CurrentAttributeString.toString();
                break;
              }
              case 8: {
                DistrictCourtOfParent = CurrentAttributeString
                .toString();
                break;
              }
            }
            break;
          }
        }
        currentAttribut++;
        CurrentAttributeString = new StringBuffer(1000);
      }
      // 1 " und " 2: StringTokenizer
      else if (TmpCompositeToken.startsWith("\"")) {
        TmpLookForString = true;
        if (CurrentAttributeString.length() != 0) {
          CurrentAttributeString.append(" ");
        }
        if (TmpCompositeToken.endsWith("\"")) {
          if (TmpCompositeToken.length() > 1) {
            // " may be a single token
            CurrentAttributeString.append(TmpCompositeToken.substring(
            1, TmpCompositeToken.length() - 1));
            TmpLookForString = false;
          }
        }
        else {
          if (TmpCompositeToken.length() > 1) {
            // " may be a single token
            CurrentAttributeString.append(
            TmpCompositeToken.substring(1));
          }
        }
      }
      else if (TmpCompositeToken.equals("null")) {
        CurrentAttributeString = new StringBuffer(1000);
      }
      else {
        if (CurrentAttributeString.length() != 0) {
          CurrentAttributeString.append(" ");
        }
        TmpCurrentNamedEntityAttributes++;
        int index = (new Integer(TmpCompositeToken)).intValue();
        CurrentAttributeString.append(Tokens_NeAsToken[pI + index]);
      }
    }  // while:createCompositeNamedEntity
    
    // assign the value of the last attribute in respective
    switch (pSearchFor) {
      case SEARCH_FOR_PERSON: {
        Occupation = CurrentAttributeString.toString();
        break;
      }
      case SEARCH_FOR_COMPANY_RELOCATION: {
        DestinationCommercialRegisterID = CurrentAttributeString.toString();
        break;
      }
      case SEARCH_FOR_COMPANY: {
        CommercialRegisterID = CurrentAttributeString.toString();
        break;
      }
      case SEARCH_FOR_DATE_PERIOD: {
        PeriodEndDate = CurrentAttributeString.toString();
        break;
      }
      case SEARCH_FOR_AMOUNT_OF_MONEY_RANGE: {
        MaximumAmountOfMoney = CurrentAttributeString.toString();
        break;
      }
      case SEARCH_FOR_PERCENTAGE_RANGE: {
        MaximumPercentage = CurrentAttributeString.toString();
        break;
      }
      case SEARCH_FOR_EQUITY_STAKE: {
        PercentageOfShares = CurrentAttributeString.toString();
        break;
      }
      case SEARCH_FOR_KEY_FIGURE: {
        Value = CurrentAttributeString.toString();
        break;
      }
      case SEARCH_FOR_UNIT_OF_COMPANY: {
        CommercialRegisterIDOfParent = CurrentAttributeString.toString();
        break;
      }
    }
    
    // look for the most complex composite feature, but create a new named
    // entity if it contains the same number of attributes to implement 
    // a constructor like company( 0 , 6 , 4 , null , null ) for the pattern
    // <<organization>> , <<place>> ( <<street>> , <<place>>
    if (TmpCurrentNamedEntityAttributes >= TmpMaxNamedEntityAttributes) {
      // ID and placeholder will be assigned later
      switch (pSearchFor) {
        case SEARCH_FOR_PERSON: {
          TmpNewNamedEntity = new PersonNE(0, null, NamedEntity.PERSON, null,
          Name, Surname, Forename, DateOfBirth, MiddleInitial, Title,
          Occupation, null, MothersName, PlaceOfResidence,
          StreetOfResidence, null);
          TmpMaxNamedEntityString = Surname + Forename + Title;
          TmpFeatureType = NamedEntity.PERSON;
          break;
        }
        case SEARCH_FOR_COMPANY_RELOCATION: {
          TmpNewNamedEntity = new CompanyRelocationNE(0, null,
          NamedEntity.COMPANY_RELOCATION, Name, OriginDistrictCourt,
          OriginCommercialRegisterID, OriginPlace, OriginStreet,
          DestinationDistrictCourt, DestinationCommercialRegisterID,
          DestinationPlace, DestinationStreet);
          TmpMaxNamedEntityString = Name + OriginPlace + DestinationPlace;
          TmpFeatureType = NamedEntity.COMPANY_RELOCATION;
          break;
        }
        case SEARCH_FOR_COMPANY: {
          TmpNewNamedEntity = new CompanyNE(0, null, NamedEntity.COMPANY, null,
          Name, DistrictCourt, CommercialRegisterID, Place, Street,  null);
          TmpMaxNamedEntityString = Name;
          TmpFeatureType = NamedEntity.COMPANY;
          break;
        }
        case SEARCH_FOR_DATE_PERIOD: {
          TmpNewNamedEntity = new DatePeriodNE(0, null,
          NamedEntity.DATE_PERIOD, PeriodBeginDate, PeriodEndDate);
          TmpMaxNamedEntityString = PeriodBeginDate + "-" + PeriodEndDate;
          TmpFeatureType = NamedEntity.DATE_PERIOD;
          break;
        }
        case SEARCH_FOR_AMOUNT_OF_MONEY_RANGE: {
          TmpNewNamedEntity = new AmountOfMoneyRangeNE(0, null,
          NamedEntity.AMOUNT_OF_MONEY_RANGE, MinimumAmountOfMoney,
          MaximumAmountOfMoney);
          TmpMaxNamedEntityString = MinimumAmountOfMoney + "-"
          + MaximumAmountOfMoney;
          TmpFeatureType = NamedEntity.AMOUNT_OF_MONEY_RANGE;
          break;
        }
        case SEARCH_FOR_PERCENTAGE_RANGE: {
          TmpNewNamedEntity = new PercentageRangeNE(0, null,
          NamedEntity.PERCENTAGE_RANGE, MinimumPercentage,
          MaximumPercentage);
          TmpMaxNamedEntityString = MinimumPercentage + "-"
          + MaximumPercentage;
          TmpFeatureType = NamedEntity.PERCENTAGE_RANGE;
          break;
        }
        case SEARCH_FOR_EQUITY_STAKE: {
          TmpNewNamedEntity = new EquityStakeNE(0, null,
          NamedEntity.EQUITY_STAKE, CompanyName, NumberOfShares,
          PercentageOfShares);
          TmpMaxNamedEntityString = CompanyName + ": "
          + NumberOfShares + ": " + PercentageOfShares;
          TmpFeatureType = NamedEntity.EQUITY_STAKE;
          break;
        }
        case SEARCH_FOR_KEY_FIGURE: {
          TmpNewNamedEntity = new KeyFigureNE(0, null,
          NamedEntity.KEY_FIGURE, Name, Value);
          TmpMaxNamedEntityString = Name + "=" + Value;
          TmpFeatureType = NamedEntity.KEY_FIGURE;
          break;
        }
        case SEARCH_FOR_UNIT_OF_COMPANY: {
          TmpNewNamedEntity = new UnitOfCompanyNE(0, null,
          NamedEntity.UNIT_OF_COMPANY, NameOfUnit, DistrictCourtOfUnit,
          CommercialRegisterIDOfUnit, PlaceOfUnitHeadquarter,
          StreetOfUnitHeadquarter, NameOfParent, DistrictCourtOfParent,
          CommercialRegisterIDOfParent, PlaceOfParentHeadquarter, 
          StreetOfParentHeadquarter);
          TmpMaxNamedEntityString = NameOfUnit + " of " + NameOfParent;
          TmpFeatureType = NamedEntity.UNIT_OF_COMPANY;
          break;
        }
      }
      TmpMaxNamedEntityAttributes = TmpCurrentNamedEntityAttributes;
      TmpStartIndexMaxNamedEntity = pI;
      TmpEndIndexMaxNamedEntity = pI + pJ;
    }
    TmpCurrentNamedEntityAttributes = 0;
    
    // Note: DefaultDIAsDEMtextUnitsLayer must be updated as well when
    // adding new composite NEs!
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isValidCompositeNeConstructor(String pLine) {
    
    if (Tools.stringIsNullOrEmpty(pLine) || !pLine.endsWith(")")) {
      return false;
    }
    if (!(pLine.startsWith(PERSON_CONSTRUCTOR_ID + "(")
    || pLine.startsWith(COMPANY_CONSTRUCTOR_ID + "(")
    || pLine.startsWith(COMPANY_RELOCATION_CONSTRUCTOR_ID + "(")
    || pLine.startsWith(DATE_PERIOD_CONSTRUCTOR_ID + "(")
    || pLine.startsWith(AMOUNT_OF_MONEY_RANGE_CONSTRUCTOR_ID + "(")
    || pLine.startsWith(PERCENTAGE_RANGE_CONSTRUCTOR_ID + "(")
    || pLine.startsWith(EQUITY_STAKE_CONSTRUCTOR_ID + "(")
    || pLine.startsWith(KEY_FIGURE_CONSTRUCTOR_ID + "(")
    || pLine.startsWith(UNIT_OF_COMPANY_CONSTRUCTOR_ID + "("))) {
      return false;
    }
    
    pLine = pLine.substring(pLine.indexOf("(") + 1);
    pLine = pLine.substring(0, pLine.length() - 1).trim();
    CompositeTokenizer = new StringTokenizer(pLine);
    TmpCompositeToken = null;
    TmpLookForString = false;
    CurrentAttributeString = new StringBuffer(1000);
    
    try {
      while (CompositeTokenizer.hasMoreTokens()) {
        TmpCompositeToken = CompositeTokenizer.nextToken();
        if (TmpLookForString) {
          if (TmpCompositeToken.endsWith("\"")) {
            if (TmpCompositeToken.length() > 1) {
              CurrentAttributeString.append(TmpCompositeToken.substring(0,
              TmpCompositeToken.length() - 1));
            }
            TmpLookForString = false;
          }
        }
        else if (TmpCompositeToken.equals(",")) {
          CurrentAttributeString = new StringBuffer(1000);
        }
        else if (TmpCompositeToken.startsWith("\"")) {
          TmpLookForString = true;
          if (TmpCompositeToken.endsWith("\"")) {
            if (TmpCompositeToken.length() > 1) {
              CurrentAttributeString.append(TmpCompositeToken.substring(
              1, TmpCompositeToken.length() - 1));
              TmpLookForString = false;
            }
          }
          else {
            if (TmpCompositeToken.length() > 1) {
              CurrentAttributeString.append(TmpCompositeToken.substring(1));
            }
          }
        }
        else if (TmpCompositeToken.equals("null")) {
          CurrentAttributeString = new StringBuffer(1000);
        }
        else {
          int index = (new Integer(TmpCompositeToken)).intValue();
        }
      }
    }
    catch (Exception e) {
      return false;
    }
    CurrentAttributeString = null;
    
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