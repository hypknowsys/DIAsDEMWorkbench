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

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.hypknowsys.diasdem.core.neex.util.TestNamedEntityOwner;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class NamedEntityExtractor21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityOwner MyNamedEntityOwner = null;
  private NamedEntityExtractorParameter NeTaskParameter = null;
  private BasicNeDictionary21 NeDictionary = null;
  private RegexBasicNeExtractor21 MyRegexBasicNeExtractor = null;
  private OrganizationBasicNeExtractor21 MyOrganizationBasicNeExtractor = null;
  private PlaceBasicNeExtractor21 MyPlaceBasicNeExtractor = null;
  private StreetBasicNeExtractor21 MyStreetBasicNeExtractor = null;
  private PersonNameBasicNeExtractor21 MyPersonNameBasicNeExtractor = null;
  private ProfessionBasicNeExtractor21 MyProfessionBasicNeExtractor = null;
  private CompositeNeExtractor21 MyCompositeNeExtractor = null;
  
  private boolean ExtractProfessions = false;
  private boolean ExtractCompositeNes = false;
  private boolean ExtractStreetNes = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient int PreviouslyExtractedNEs1 = 0;
  private transient int PreviouslyExtractedNEs2 = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntityExtractor21(NamedEntityExtractorParameter pTaskParameter) {
    
    NeTaskParameter = pTaskParameter;
    NeDictionary = new BasicNeDictionary21(
    NeTaskParameter);
    MyRegexBasicNeExtractor = new RegexBasicNeExtractor21(
    NeTaskParameter);
    MyOrganizationBasicNeExtractor = new OrganizationBasicNeExtractor21(
    NeTaskParameter);
    MyPlaceBasicNeExtractor = new PlaceBasicNeExtractor21(
    NeTaskParameter);
    MyPersonNameBasicNeExtractor = new PersonNameBasicNeExtractor21(
    NeTaskParameter);
    MyProfessionBasicNeExtractor = new ProfessionBasicNeExtractor21(
    NeTaskParameter);
    MyCompositeNeExtractor = new CompositeNeExtractor21(
    NeTaskParameter);
    
    if (NeTaskParameter.getProfessionsFileName() != null
    && NeTaskParameter.getProfessionsFileName().length() > 0) {
      ExtractProfessions = true;
    }
    else {
      ExtractProfessions = false;
    }
    
    if (NeTaskParameter.getCompositeFeaturesFileName() != null
    && NeTaskParameter.getCompositeFeaturesFileName().length() > 0) {
      ExtractCompositeNes = true;
    }
    else {
      ExtractCompositeNes = false;
    }
    
    if (NeTaskParameter.extractStreetBasicNe()) {
      MyStreetBasicNeExtractor = new StreetBasicNeExtractor21(
      NeTaskParameter);
      ExtractStreetNes = true;
    }
    else {
      MyStreetBasicNeExtractor = null;
      ExtractStreetNes = false;
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
  
  public String extractNamedEntities(String pInputLine,
  NamedEntityOwner pMyNamedEntityOwner) {
    
    MyNamedEntityOwner = pMyNamedEntityOwner;
    
    PreviouslyExtractedNEs1 = MyNamedEntityOwner.getNumberOfNamedEntities();
    String result = MyRegexBasicNeExtractor
    .replaceRegexBasicNamedEntities(pInputLine, MyNamedEntityOwner);
    result = MyOrganizationBasicNeExtractor
    .replaceOrganizationBasicNamedEntities(result, MyNamedEntityOwner,
    NeDictionary);
    result = MyPlaceBasicNeExtractor
    .replacePlaceBasicNamedEntities(result, MyNamedEntityOwner,
    NeDictionary);
    PreviouslyExtractedNEs2 = MyNamedEntityOwner.getNumberOfNamedEntities();
    result = MyPersonNameBasicNeExtractor
    .replacePersonNameBasicNamedEntities(result, MyNamedEntityOwner,
    NeDictionary);
    if (ExtractProfessions && PreviouslyExtractedNEs2 < MyNamedEntityOwner
    .getNumberOfNamedEntities()) {
      result = MyProfessionBasicNeExtractor
      .replaceProfessionBasicNamedEntities(result, MyNamedEntityOwner,
      NeDictionary);
    }
    if (ExtractStreetNes) {
      result = MyStreetBasicNeExtractor
      .replaceStreetBasicNamedEntities(result, MyNamedEntityOwner,
      NeDictionary);
    }
    if (ExtractCompositeNes && (MyNamedEntityOwner.getNumberOfNamedEntities()
    - PreviouslyExtractedNEs1) > 0) {
      result = MyCompositeNeExtractor.replaceCompositeNamedEntities(result,
      MyNamedEntityOwner);
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addTempOrganizationName(String pTokenizedOrganizationName) {
    
    NeDictionary.OrgAndOrgEndTrie.put(pTokenizedOrganizationName,
    BasicNeDictionary21.ORGANIZATION);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void deleteTempOrganizationName(String pTokenizedOrganizationName) {
    
    // StringTrie should have a real delete method, because *.remove
    // only sets value == null. It does not remove the search path.
    NeDictionary.OrgAndOrgEndTrie.remove(pTokenizedOrganizationName);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void reloadOrganizationFiles() {
    
    // StringTrie should have a real delete method, because *.remove
    // only sets value == null. It does not remove the search path.
    NeDictionary.loadOrganizationFiles(true);
    
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {
    
    String parameterDirectory = "[ENTER DEBUGGING DIRECORY]";
    NamedEntityExtractorParameter neParameter =
    new NamedEntityExtractorParameter(
    parameterDirectory + "ForenamesDE.txt",
    parameterDirectory + "SurnamesDE.txt",
    parameterDirectory + "SurnameSuffixesDE_HeinsUndPartner21.txt",
    parameterDirectory + "MiddleInitialsDE_HeinsUndPartner21.txt",
    parameterDirectory + "TitlesDE_HeinsUndPartner21.txt",
    parameterDirectory + "PlacesDE.txt",
    parameterDirectory + "OrganizationsStartDE_HeinsUndPartner21.txt",
    parameterDirectory + "OrganizationsEndDE_HeinsUndPartner21.txt",
    parameterDirectory + "CompositeNE_HeinsUndPartner21.txt",
    parameterDirectory + "RegexNE_HeinsUndPartner21.txt",
    parameterDirectory + "NameAffixesDE_HeinsUndPartner21.txt",
    parameterDirectory + "PlaceAffixesDE_HeinsUndPartner21.txt",
    parameterDirectory + "OrganizationsAffixesDE_HeinsUndPartner21.txt",
    parameterDirectory + "OrganizationsDE_HeinsUndPartner21.tokenized.txt",
    "TestMetaDataAttribute", false,
    parameterDirectory + "PlaceIndicatorsDE_HeinsUndPartner21.txt",
    false, null,
    parameterDirectory + "PersonNameIndicatorsDE_HeinsUndPartner21.txt",
    parameterDirectory + "ProfessionsDE_HeinsUndPartner21.txt", true,
    parameterDirectory + "StreetExceptionsDE.txt",
    parameterDirectory + "StreetSuffixesDE_HeinsUndPartner21.txt",
    "([A-ZÖÄÜ][A-ZÖÄÜa-zöäüß\\-\\.]*)",
    "([0-9\\-]{1,4}[a-zA-Z]?|[a-zA-Z\\-\\/]?|[Nn][Rr][\\.]?)",
    "(.*-$|^Die.*|^Der.*|^Das.*|[\\p{Alpha}]*ring$|.*\\/$)", 2, 
    "([0-9\\.]{2,}|^<<.*)");
    
    NamedEntityExtractor21 neExtractor = new NamedEntityExtractor21(
    neParameter);
    neExtractor.addTempOrganizationName("DiE Test ABC GmbH");
    TestNamedEntityOwner neOwner = new TestNamedEntityOwner();
    
    for (int i = 0; i < neOwner.getNumberOfProcessedTextUnits(); i++) {
      neOwner.replaceProcessedTextUnitFromString(i,
      neExtractor.extractNamedEntities(neOwner
      .getInputTextUnitAsString(i), neOwner));
    }
    System.out.println(neOwner.toString());
    
  }
  
}