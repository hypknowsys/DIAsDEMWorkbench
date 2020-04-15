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
import org.hypknowsys.misc.io.TextBufferedReader;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.StringArrayTrie;
import org.hypknowsys.misc.util.StringTrie;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class BasicNeDictionary21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected NamedEntityExtractorParameter NeTaskParameter = null;
  protected StringTrie NeDictionary = null;
  
  protected StringTrie OrgAndOrgEndTrie = null;
  protected StringTrie OrgAffixTrie = null;
  protected int NumberOfOrgIndicators = 0;
  protected int MaxOrgIndicatorLength = 0;
  protected StringArrayTrie OrgIndicatorTrie = null;
  
  protected StringTrie PlaceAffixTrie = null;
  protected int NumberOfPlaceIndicators = 0;
  protected int MaxPlaceIndicatorLength = 0;
  protected StringArrayTrie PlaceIndicatorTrie = null;
  
  protected int NumberOfPersonIndicators = 0;
  protected int MaxPersonIndicatorLength = 0;
  protected StringArrayTrie PersonIndicatorTrie = null;
  
  protected StringTrie SurnameSuffixes = null;
  
  protected StringTrie StreetExceptionsTrie = null;
  protected String[] StreetSuffixes = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient NamedEntity TmpNamedEntity = null;
  private transient TextBufferedReader TmpTextBufferedReader = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected static final Integer ORGANIZATION_END = new Integer(1);
  protected static final Integer ORGANIZATION = new Integer(2);
  
  protected static final Integer WEAK_PLACE_AFFIX = new Integer(3);
  protected static final Integer STRONG_PLACE_AFFIX = new Integer(4);
  protected static final String WEAK_PLACE_AFFIX_TEXT = "weak_place_affix";
  protected static final String STRONG_PLACE_AFFIX_TEXT = "strong_place_affix";
  
  protected static final Integer WEAK_POS_PERSON_INDICATOR = new Integer(5);
  protected static final Integer STRONG_POS_PERSON_INDICATOR = new Integer(6);
  protected static final Integer STRONG_NEG_PERSON_INDICATOR = new Integer(7);
  protected static final String WEAK_POS_PERSON_INDICATOR_TEXT
  = "weak_pos_person_indicator";
  protected static final String STRONG_POS_PERSON_INDICATOR_TEXT
  = "strong_pos_person_indicator";
  protected static final String STRONG_NEG_PERSON_INDICATOR_TEXT
  = "strong_neg_person_indicator";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public BasicNeDictionary21(NamedEntityExtractorParameter pNeTaskParameter) {
    
    NeTaskParameter = pNeTaskParameter;
    NeDictionary = new StringTrie();
    
    if (NeTaskParameter.getPlacesFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getPlacesFileName())) {
      this.loadNamedEntityFile(NeTaskParameter.getPlacesFileName(),
      NamedEntity.PLACE, "NEEX 2.1: List of places loaded", false);
    }
    if (NeTaskParameter.getForenamesFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getForenamesFileName())) {
      this.loadNamedEntityFile(NeTaskParameter.getForenamesFileName(),
      NamedEntity.FORENAME, "NEEX 2.1: List of forenames loaded", false);
    }
    if (NeTaskParameter.getSurnamesFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getSurnamesFileName())) {
      this.loadNamedEntityFile(NeTaskParameter.getSurnamesFileName(),
      NamedEntity.SURNAME, "NEEX 2.1: List of surnames loaded", false);
    }
    if (NeTaskParameter.getTitlesFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getTitlesFileName())) {
      this.loadNamedEntityFile(NeTaskParameter.getTitlesFileName(),
      NamedEntity.TITLE, "NEEX 2.1: List of titles loaded", false);
    }
    if (NeTaskParameter.getMiddleInitialsFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getMiddleInitialsFileName())) {
      this.loadNamedEntityFile(NeTaskParameter.getMiddleInitialsFileName(),
      NamedEntity.MIDDLE_INITIAL, "NEEX 2.1: List of middle initials loaded",
      false);
    }
    if (NeTaskParameter.getNameAffixesFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getNameAffixesFileName())) {
      this.loadNamedEntityFile(NeTaskParameter.getNameAffixesFileName(),
      NamedEntity.NAME_AFFIX, "NEEX 2.1: List of name affixes loaded", false);
    }
    if (NeTaskParameter.getProfessionsFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getProfessionsFileName())) {
      this.loadNamedEntityFile(NeTaskParameter.getProfessionsFileName(),
      NamedEntity.PROFESSION, "NEEX 2.1: List of professions loaded", false);
    }
    
    this.loadPersonFiles(false);
    this.loadOrganizationFiles(false);
    this.loadPlaceFiles(false);
    
    if (NeTaskParameter.getStreetExceptionsFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getProfessionsFileName())) {
      this.loadStreetExceptionsFile(false);
    }
    if (NeTaskParameter.getStreetSuffixesFileName() != null && Tools
    .isExistingFile(NeTaskParameter.getStreetSuffixesFileName())) {
      this.loadStreetSuffixesFile(false);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean previousMatchPrecedesBlankSpace() {
    return NeDictionary.previousMatchPrecedesBlankSpace(); }
  
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
  
  public String getPossibleTypesString(String pTerm) {
    
    TmpNamedEntity = (NamedEntity)NeDictionary
    .getAndCheckForSubsequentBlankSpace(pTerm);
    if (TmpNamedEntity == null) {
      return null;
    }
    else {
      return TmpNamedEntity.getPossibleTypesString();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int[] getPossibleTypesArray(String pTerm) {
    
    TmpNamedEntity = (NamedEntity)NeDictionary
    .getAndCheckForSubsequentBlankSpace(pTerm);
    if (TmpNamedEntity == null) {
      return null;
    }
    else {
      return TmpNamedEntity.getPossibleTypesArray();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void loadOrganizationFiles(boolean pOutputMessage) {
    
    OrgAndOrgEndTrie = null;
    OrgIndicatorTrie = null;
    OrgAffixTrie = null;
    
    // list of organization suffixes (text file, each line = one suffix)
    OrgAndOrgEndTrie = new StringTrie();
    TextBufferedReader textReader = new TextBufferedReader(
    new File(NeTaskParameter.getOrganizationsEndFileName()));
    String line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      OrgAndOrgEndTrie.put(line.trim(), ORGANIZATION_END);
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    if (pOutputMessage) {
      System.out.println("NEEX 2.1: List of organization suffixes loaded");
    }
    
    if (NeTaskParameter.getOrganizationsFileName() != null
    && NeTaskParameter.getOrganizationsFileName().length() > 0) {
      // list of complete organization (text file, each line = one organization)
      textReader = new TextBufferedReader(
      new File(NeTaskParameter.getOrganizationsFileName()));
      line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        OrgAndOrgEndTrie.put(line.trim(), ORGANIZATION);
        line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      textReader.close();
      if (pOutputMessage) {
        System.out.println("NEEX 2.1: List of organizations loaded");
      }
    }
    
    OrgIndicatorTrie = new StringArrayTrie();
    String[] arrayOfTokens = null;
    NumberOfOrgIndicators = 0;
    textReader = new TextBufferedReader(new File(NeTaskParameter
    .getOrganizationsStartFileName()));
    line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      NumberOfOrgIndicators++;
      arrayOfTokens = NamedEntityExtractor21.string2ArrayOfTokens(line.trim());
      OrgIndicatorTrie.put(arrayOfTokens, line.trim());
      MaxOrgIndicatorLength = Math.max(
      MaxOrgIndicatorLength, arrayOfTokens.length);
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    if (pOutputMessage) {
      System.out.println("NEEX 2.1: List of organization indicators loaded");
    }
    
    OrgAffixTrie = new StringTrie();
    arrayOfTokens = null;
    if (NeTaskParameter.getOrganizationsAffixesFileName() != null
    && NeTaskParameter.getOrganizationsAffixesFileName().length() > 0) {
      textReader = new TextBufferedReader(new File(NeTaskParameter
      .getOrganizationsAffixesFileName()));
      line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        OrgAffixTrie.put(line.toLowerCase(), line.trim());
        line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      textReader.close();
      if (pOutputMessage) {
        System.out.println("NEEX 2.1: List of organization affixes loaded");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void loadPlaceFiles(boolean pOutputMessage) {
    
    TextBufferedReader textReader = null;
    String line = null;
    
    PlaceIndicatorTrie = new StringArrayTrie();
    String[] arrayOfTokens = null;
    NumberOfPlaceIndicators = 0;
    if (NeTaskParameter.getPlaceIndicatorsFileName() != null
    && NeTaskParameter.getPlaceIndicatorsFileName().length() > 0) {
      textReader = new TextBufferedReader(new File(NeTaskParameter
      .getPlaceIndicatorsFileName()));
      line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        NumberOfPlaceIndicators++;
        arrayOfTokens = NamedEntityExtractor21.string2ArrayOfTokens(line
        .toLowerCase().trim());
        PlaceIndicatorTrie.put(arrayOfTokens, line.trim());
        MaxPlaceIndicatorLength = Math.max(
        MaxPlaceIndicatorLength, arrayOfTokens.length);
        line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      textReader.close();
      if (pOutputMessage) {
        System.out.println("NEEX 2.1: List of place indicators loaded");
      }
    }
    
    PlaceAffixTrie = new StringTrie();
    String[] contents = null;
    arrayOfTokens = null;
    if (NeTaskParameter.getPlaceAffixesFileName() != null
    && NeTaskParameter.getPlaceAffixesFileName().length() > 0) {
      textReader = new TextBufferedReader(new File(NeTaskParameter
      .getPlaceAffixesFileName()));
      line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        contents = line.split("\t");
        if (contents.length == 2 && contents[1].trim().length() > 0
        && (WEAK_PLACE_AFFIX_TEXT.equals(contents[0].trim())
        || STRONG_PLACE_AFFIX_TEXT.equals(contents[0].trim()))) {
          if (WEAK_PLACE_AFFIX_TEXT.equals(contents[0].trim())) {
            PlaceAffixTrie.put(contents[1].trim(), WEAK_PLACE_AFFIX);
          }
          else {
            PlaceAffixTrie.put(contents[1].trim(), STRONG_PLACE_AFFIX);
          }
        }
        else {
          System.out.println("[BasicNeDictionary21] Error in file "
          + NeTaskParameter.getPlaceAffixesFileName() + ": Line \""
          + line + "\" does not conform to syntax!");
        }
        line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      textReader.close();
      if (pOutputMessage) {
        System.out.println("NEEX 2.1: List of place affixes loaded");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void loadStreetExceptionsFile(boolean pOutputMessage) {
    
    TextBufferedReader textReader = null;
    String line = null;
    
    StreetExceptionsTrie = new StringTrie();
    if (NeTaskParameter.getStreetExceptionsFileName() != null
    && NeTaskParameter.getStreetExceptionsFileName().length() > 0) {
      textReader = new TextBufferedReader(new File(NeTaskParameter
      .getStreetExceptionsFileName()));
      line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        StreetExceptionsTrie.put(line.trim(), line.trim());
        line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      textReader.close();
      if (pOutputMessage) {
        System.out.println("NEEX 2.1: List of street exceptions loaded");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void loadStreetSuffixesFile(boolean pOutputMessage) {
    
    TextBufferedReader textReader = null;
    String line = null;
    
    ArrayList streetSuffixesList = new ArrayList(100);
    if (NeTaskParameter.getStreetSuffixesFileName() != null
    && NeTaskParameter.getStreetSuffixesFileName().length() > 0) {
      textReader = new TextBufferedReader(new File(NeTaskParameter
      .getStreetSuffixesFileName()));
      line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        streetSuffixesList.add(line.trim().toLowerCase());
        line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      textReader.close();
      StreetSuffixes = new String[streetSuffixesList.size()];
      for (int i = 0; i < streetSuffixesList.size(); i++) {
        StreetSuffixes[i] = (String)streetSuffixesList.get(i);
      }
      if (pOutputMessage) {
        System.out.println("NEEX 2.1: List of street suffixes loaded");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void loadNamedEntityFile(String pFileName, int pNamedEntityType,
  String pSystemOutComment, boolean pOutputMessage) {
    
    TmpTextBufferedReader = new TextBufferedReader(new File(pFileName),
    1000000);
    TmpTextBufferedReader.open();
    TmpString = TmpTextBufferedReader
    .getFirstLineButIgnoreCommentsAndEmptyLines();
    while (TmpString != null) {
      TmpNamedEntity = (NamedEntity)NeDictionary.get(TmpString.trim());
      if (TmpNamedEntity == null) {
        TmpNamedEntity = new NamedEntity();
      }
      TmpNamedEntity.addPossibleType(pNamedEntityType);
      NeDictionary.put(TmpString, TmpNamedEntity.clone());
      TmpString = TmpTextBufferedReader
      .getNextLineButIgnoreCommentsAndEmptyLines();
    }
    TmpTextBufferedReader.close();
    if (pOutputMessage) {
      System.out.println(pSystemOutComment);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void loadPersonFiles(boolean pOutputMessage) {
    
    // read list of surname suffixes (text file, each line = one suffix)
    SurnameSuffixes = new StringTrie();
    StringBuffer reverseSuffix = null;
    TextFile surnameSuffixesFile = new TextFile(
    new File(NeTaskParameter.SurnameSuffixesFileName));
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
    if (pOutputMessage) {
      System.out.println("NEEX 2.1: List of surname suffixes loaded");
    }
    // SurnameSuffixes.dump(System.out);
    
    PersonIndicatorTrie = new StringArrayTrie();
    String[] contents = null;
    String[] arrayOfTokens = null;
    NumberOfPlaceIndicators = 0;
    if (NeTaskParameter.getPersonNameIndicatorsFileName() != null
    && NeTaskParameter.getPersonNameIndicatorsFileName().length() > 0) {
      TextBufferedReader textReader = new TextBufferedReader(
      new File(NeTaskParameter.getPersonNameIndicatorsFileName()));
      String line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        contents = line.split("\t");
        if (contents.length == 2 && contents[1].trim().length() > 0
        && (WEAK_POS_PERSON_INDICATOR_TEXT.equals(contents[0].trim())
        || STRONG_POS_PERSON_INDICATOR_TEXT.equals(contents[0].trim())
        || STRONG_NEG_PERSON_INDICATOR_TEXT.equals(contents[0].trim()))) {
          NumberOfPersonIndicators++;
          arrayOfTokens = NamedEntityExtractor21.string2ArrayOfTokens(
          contents[1].toLowerCase().trim());
          if (WEAK_POS_PERSON_INDICATOR_TEXT.equals(contents[0].trim())) {
            PersonIndicatorTrie.put(arrayOfTokens, WEAK_POS_PERSON_INDICATOR);
          }
          else if (STRONG_POS_PERSON_INDICATOR_TEXT.equals(contents[0]
          .trim())) {
            PersonIndicatorTrie.put(arrayOfTokens, STRONG_POS_PERSON_INDICATOR);
          }
          else {
            PersonIndicatorTrie.put(arrayOfTokens, STRONG_NEG_PERSON_INDICATOR);
          }
          MaxPersonIndicatorLength = Math.max(
          MaxPersonIndicatorLength, arrayOfTokens.length);
        }
        else {
          System.out.println("[BasicNeDictionary21] Error in file "
          + NeTaskParameter.getPersonNameIndicatorsFileName() + ": Line \""
          + line + "\" does not conform to syntax!");
        }
        line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      textReader.close();
      if (pOutputMessage) {
        System.out.println("NEEX 2.1: List of person name indicators loaded");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {
    
    String parameterDirectory = "/home/kwinkler/diasdem/DIAsDEM.workbench21/"
    + "data/parameters/replaceNamedEntities2/default/";
    NamedEntityExtractorParameter neParameter =
    new NamedEntityExtractorParameter(
    parameterDirectory + "ForenamesDE.txt",
    parameterDirectory + "SurnamesDE.txt", null,
    parameterDirectory + "MiddleInitialsDE.txt",
    parameterDirectory + "TitlesDE.txt",
    parameterDirectory + "PlacesDE.txt", null, null, null, null,
    parameterDirectory + "NameAffixesDE.txt",
    parameterDirectory + "PlaceAffixesDE.txt", null, null, null, false, null,
    false, null, null, null, false, null, null, null, null, null, 0, null);
    
    BasicNeDictionary21 neDictionary = new BasicNeDictionary21(
    neParameter);
    
    System.out.println("Rothenburg: " + neDictionary
    .getPossibleTypesString("Rothenburg"));
    System.out.println("ob: " + neDictionary
    .getPossibleTypesString("ob"));
    System.out.println("Berlin: " + neDictionary
    .getPossibleTypesString("Berlin"));
    System.out.println("Meier: " + neDictionary
    .getPossibleTypesString("Meier"));
    System.out.println("Hagen: " + neDictionary
    .getPossibleTypesString("Hagen"));
    System.out.println(", jun.: " + neDictionary
    .getPossibleTypesString(", jun."));
    System.out.println("Dresden: " + neDictionary
    .getPossibleTypesString("Dresden"));
    System.out.println("Spiliopoulou: " + neDictionary
    .getPossibleTypesString("Spiliopoulou"));
    System.out.println("null: " + neDictionary
    .getPossibleTypesString(null));
    System.out.println("");
    System.out.print("Berlin: ");
    int[] array = neDictionary.getPossibleTypesArray("Berlin");
    for (int i = 0; array != null && i < array.length; i++) {
      System.out.print(array[i] + " ");
    }
    System.out.println("");
    System.out.print("Meier: ");
    array = neDictionary.getPossibleTypesArray("Meier");
    for (int i = 0; array != null && i < array.length; i++) {
      System.out.print(array[i] + " ");
    }
    System.out.println("");
    System.out.print("Hagen: ");
    array = neDictionary.getPossibleTypesArray("Hagen");
    for (int i = 0; array != null && i < array.length; i++) {
      System.out.print(array[i] + " ");
    }
    System.out.println("");
    System.out.print("Spiliopoulou: ");
    array = neDictionary.getPossibleTypesArray("Spiliopoulou");
    for (int i = 0; array != null && i < array.length; i++) {
      System.out.print(array[i] + " ");
    }
    System.out.println("");
    System.out.print("null: ");
    array = neDictionary.getPossibleTypesArray(null);
    for (int i = 0; array != null && i < array.length; i++) {
      System.out.print(array[i] + " ");
    }
    
  }
  
}