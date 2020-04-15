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

package org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities21;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.core.neex.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ReplaceNamedEntitiesParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String ForenamesFileName = null;
  protected String SurnamesFileName = null;
  protected String SurnameSuffixesFileName = null;
  protected String MiddleInitialsFileName = null;
  protected String TitlesFileName = null;
  protected String PlacesFileName = null;
  protected String OrganizationsStartFileName = null;
  protected String OrganizationsEndFileName = null;
  protected String CompositeFeaturesFileName = null;
  protected String RegularExpressionsFileName = null;
  protected String NameAffixesFileName = null;
  protected String PlaceAffixesFileName = null;
  protected String OrganizationsAffixesFileName = null;
  protected String OrganizationsFileName = null;
  protected String OrganizationsMetaDataAttribute = null;
  protected boolean DetermineCanonicalForms = false;
  protected String PlaceIndicatorsFileName = null;
  protected boolean CreateDebuggingFiles = false;
  protected String DebuggingFileDirectory = null;
  protected String PersonNameIndicatorsFileName = null;
  protected String ProfessionsFileName = null;
  protected boolean ExtractStreetBasicNe = false;
  protected String StreetExceptionsFileName = null;
  protected String StreetSuffixesFileName = null;
  protected String StreetPrefixTokenRegex = null;   
  protected String StreetAffixTokenRegex = null;
  protected String StreetExclusionRegex = null;
  protected int MinTokenInStreet = 0;
  protected String OrganizationIndicatorRegex = null;
      
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities21"
  + ".ReplaceNamedEntitiesTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities21"
  + ".ReplaceNamedEntitiesParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String FORENAMES_FILE_NAME =
  "ForenamesFileName";
  private final static String SURNAMES_FILE_NAME =
  "SurnamesFileName";
  private final static String SURNAME_SUFFIXES_FILE_NAME =
  "SurnameSuffixesFileName";
  private final static String MIDDLE_INITIALS_FILE_NAME =
  "MiddleInitialsFileName";
  private final static String TITLES_FILE_NAME =
  "TitlesFileName";
  private final static String PLACES_FILE_NAME =
  "PlacesFileName";
  private final static String ORGANIZATIONS_START_FILE_NAME =
  "OrganizationsStartFileName";
  private final static String ORGANIZATIONS_END_FILE_NAME =
  "OrganizationsEndFileName";
  private final static String COMPOSITE_FEATURES_FILE_NAME =
  "CompositeFeaturesFileName";
  private final static String DIASDEM_DOCUMENT_TAG_SOURCE =
  "DiasdemDocumentTagSource";
  private final static String DIASDEM_DOCUMENT_TAG_TARGET =
  "DiasdemDocumentTagTarget";
  private final static String REGULAR_EXPRESSIONS_FILE_NAME =
  "RegularExpressionsFileName";
  private final static String NAME_AFFIXES_FILE_NAME =
  "NameAffixesFileName";
  private final static String PLACE_AFFIXES_FILE_NAME =
  "PlaceAffixesFileName";
  private final static String ORGANIZATIONS_AFFIXES_FILE_NAME =
  "OrganizationsAffixesFileName";
  private final static String ORGANIZATIONS_FILE_NAME =
  "OrganizationsFileName";
  private final static String ORGANIZATIONS_META_DATA_ATTRIBUTE =
  "OrganizationsMetaDataAttribute";
  private final static String DETERMINE_CANONICAL_FORMS =
  "DetermineCanonicalForms";
  private final static String PLACE_INDICATORS_FILE_NAME =
  "PlaceIndicatorsFileName";
  private final static String CREATE_DEBUGGING_FILES =
  "CreateDebuggingFiles";
  private final static String DEBUGGING_FILE_DIRECTORY =
  "DebuggingFileDirectory";
  private final static String PERSON_NAME_INDICATORS_FILE_NAME =
  "PersonNameIndicatorsFileName";
  private final static String PROFESSIONS_FILE_NAME =
  "ProfessionsFileName";
  private final static String EXTRACT_STREET_BASIC_NE =
  "ExtractStreetBasicNe";
  private final static String STREET_EXCEPTIONS_FILE_NAME =
  "StreetExceptionsFileName";
  private final static String STREET_SUFFIXES_FILE_NAME =
  "StreetSuffixesFileName";
  private final static String STREET_PREFIX_TOKEN_REGEX =
  "StreetPrefixTokenRegex";
  private final static String STREET_AFFIX_TOKEN_REGEX =
  "StreetAffixTokenRegex";
  private final static String STREET_EXCLUSION_REGEX =
  "StreetExclusionRegex";
  private final static String MIN_TOKEN_IN_STREET =
  "MinTokenInStreet";
  private final static String ORGANIZATION_INDICATOR_REGEX =
  "OrganizationIndicatorRegex";
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceNamedEntitiesParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    ForenamesFileName = null;
    SurnamesFileName = null;
    SurnameSuffixesFileName = null;
    MiddleInitialsFileName = null;
    TitlesFileName = null;
    PlacesFileName = null;
    OrganizationsStartFileName = null;
    OrganizationsEndFileName = null;
    CompositeFeaturesFileName = null;
    RegularExpressionsFileName = null;
    NameAffixesFileName = null;
    PlaceAffixesFileName = null;
    OrganizationsAffixesFileName = null;
    OrganizationsFileName = null;
    OrganizationsMetaDataAttribute = null;
    DetermineCanonicalForms = false;
    PlaceIndicatorsFileName = null;
    CreateDebuggingFiles = false;
    DebuggingFileDirectory = null;
    PersonNameIndicatorsFileName = null;
    ProfessionsFileName = null;
    ExtractStreetBasicNe = false;
    StreetExceptionsFileName = null;
    StreetSuffixesFileName = null;   
    StreetPrefixTokenRegex = null;      
    StreetAffixTokenRegex = null;      
    StreetExclusionRegex = null;      
    MinTokenInStreet = 0;    
    OrganizationIndicatorRegex = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceNamedEntitiesParameter(String pCollectionFileName,
  String pForenamesFileName, String pSurnamesFileName, 
  String pSurnameSuffixesFileName, String pMiddleInitialsFileName, 
  String pTitlesFileName, String pPlacesFileName, 
  String pOrganizationsStartFileName, String pOrganizationsEndFileName, 
  String pCompositeFeaturesFileName, String pRegularExpressionsFileName,
  String pNameAffixesFileName, String pPlaceAffixesFileName,
  String pOrganizationsAffixesFileName, String pOrganizationsFileName,
  String pOrganizationsMetaDataAttribute, boolean pDetermineCanonicalForms,
  String pPlaceIndicatorsFileName, boolean pCreateDebuggingFiles,
  String pDebuggingFileDirectory, String pPersonNameIndicatorsFileName,
  String pProfessionsFileName, boolean pExtractStreetBasicNe,
  String pStreetExceptionsFileName, String pStreetSuffixesFileName, 
  String pStreetPrefixTokenRegex, String pStreetAffixTokenRegex, 
  String pStreetExclusionRegex, int pMinTokenInStreet,
  String pOrganizationIndicatorRegex) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ForenamesFileName = pForenamesFileName;
    SurnamesFileName = pSurnamesFileName;
    SurnameSuffixesFileName = pSurnameSuffixesFileName;
    MiddleInitialsFileName = pMiddleInitialsFileName;
    TitlesFileName = pTitlesFileName;
    PlacesFileName = pPlacesFileName;
    OrganizationsStartFileName = pOrganizationsStartFileName;
    OrganizationsEndFileName = pOrganizationsEndFileName;
    CompositeFeaturesFileName = pCompositeFeaturesFileName;
    RegularExpressionsFileName = pRegularExpressionsFileName; 
    NameAffixesFileName = pNameAffixesFileName;
    PlaceAffixesFileName = pPlaceAffixesFileName;
    OrganizationsAffixesFileName = pOrganizationsAffixesFileName;
    OrganizationsFileName = pOrganizationsFileName;
    OrganizationsMetaDataAttribute = pOrganizationsMetaDataAttribute;
    DetermineCanonicalForms = pDetermineCanonicalForms;
    PlaceIndicatorsFileName = pPlaceIndicatorsFileName;
    CreateDebuggingFiles = pCreateDebuggingFiles;
    DebuggingFileDirectory = pDebuggingFileDirectory;
    PersonNameIndicatorsFileName = pPersonNameIndicatorsFileName;
    ProfessionsFileName = pProfessionsFileName;
    StreetExceptionsFileName = pStreetExceptionsFileName;
    StreetSuffixesFileName = pStreetSuffixesFileName;       
    ExtractStreetBasicNe = pExtractStreetBasicNe;
    StreetExceptionsFileName = pStreetExceptionsFileName;
    StreetSuffixesFileName = pStreetSuffixesFileName;   
    StreetPrefixTokenRegex = pStreetPrefixTokenRegex;      
    StreetAffixTokenRegex = pStreetAffixTokenRegex;      
    StreetExclusionRegex = pStreetExclusionRegex;      
    MinTokenInStreet = pMinTokenInStreet;    
    OrganizationIndicatorRegex = pOrganizationIndicatorRegex;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getForenamesFileName() {
    return ForenamesFileName; }
  public String getSurnamesFileName() {
    return SurnamesFileName; }
  public String getSurnameSuffixesFileName() {
    return SurnameSuffixesFileName; }
  public String getMiddleInitialsFileName() {
    return MiddleInitialsFileName; }
  public String getTitlesFileName() {
    return TitlesFileName; }
  public String getPlacesFileName() {
    return PlacesFileName; }
  public String getOrganizationsStartFileName() {
    return OrganizationsStartFileName; }
  public String getOrganizationsEndFileName() {
    return OrganizationsEndFileName; }
  public String getCompositeFeaturesFileName() {
    return CompositeFeaturesFileName; }
  public String getRegularExpressionsFileName() {
    return RegularExpressionsFileName; }
  public String getNameAffixesFileName() {
    return NameAffixesFileName; }
  public String getPlaceAffixesFileName() {
    return PlaceAffixesFileName; }
  public String getOrganizationsAffixesFileName() {
    return OrganizationsAffixesFileName; }
  public String getOrganizationsFileName() {
    return OrganizationsFileName; }
  public String getOrganizationsMetaDataAttribute() {
    return OrganizationsMetaDataAttribute; }
  public boolean getDetermineCanonicalForms() {
    return DetermineCanonicalForms; }
  public boolean determineCanonicalForms() {
    return DetermineCanonicalForms; }
  public String getPlaceIndicatorsFileName() {
    return PlaceIndicatorsFileName; }
  public boolean getCreateDebuggingFiles() {
    return CreateDebuggingFiles; }
  public boolean createDebuggingFiles() {
    return CreateDebuggingFiles; }
  public String getDebuggingFileDirectory() {
    return DebuggingFileDirectory; }
  public String getPersonNameIndicatorsFileName() {
    return PersonNameIndicatorsFileName; }
  public String getProfessionsFileName() {
    return ProfessionsFileName; }
  public boolean extractStreetBasicNe() {
    return ExtractStreetBasicNe; }
  public String getStreetExceptionsFileName() {
    return StreetExceptionsFileName; }
  public String getStreetSuffixesFileName() {
    return StreetSuffixesFileName; }          
  public String getStreetPrefixTokenRegex() {
    return StreetPrefixTokenRegex; }          
  public String getStreetAffixTokenRegex() {
    return StreetAffixTokenRegex; }          
  public String getStreetExclusionRegex() {
    return StreetExclusionRegex; }          
  public int getMinTokenInStreet() {
    return MinTokenInStreet; }  
  public String getOrganizationIndicatorRegex() {
    return OrganizationIndicatorRegex; }            
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setDetermineCanonicalForms(boolean pDetermineCanonicalForms) {
    DetermineCanonicalForms = pDetermineCanonicalForms; }
  public void setCreateDebuggingFiles(boolean pCreateDebuggingFiles) {
    CreateDebuggingFiles = pCreateDebuggingFiles; }
  public void setExtractStreetBasicNe(boolean pExtractStreetBasicNe) {
    ExtractStreetBasicNe = pExtractStreetBasicNe; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface ScriptableTaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public org.jdom.Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(COLLECTION_FILE_NAME, 
    CollectionFileName);
    ParameterAttributes.put(FORENAMES_FILE_NAME, 
    ForenamesFileName);
    ParameterAttributes.put(SURNAMES_FILE_NAME, 
    SurnamesFileName);
    ParameterAttributes.put(SURNAME_SUFFIXES_FILE_NAME, 
    SurnameSuffixesFileName);
    ParameterAttributes.put(MIDDLE_INITIALS_FILE_NAME, 
    MiddleInitialsFileName);
    ParameterAttributes.put(TITLES_FILE_NAME, 
    TitlesFileName);
    ParameterAttributes.put(PLACES_FILE_NAME, 
    PlacesFileName);
    ParameterAttributes.put(ORGANIZATIONS_START_FILE_NAME, 
    OrganizationsStartFileName);
    ParameterAttributes.put(ORGANIZATIONS_END_FILE_NAME, 
    OrganizationsEndFileName);
    ParameterAttributes.put(COMPOSITE_FEATURES_FILE_NAME, 
    CompositeFeaturesFileName);
    ParameterAttributes.put(REGULAR_EXPRESSIONS_FILE_NAME, 
    RegularExpressionsFileName);
    ParameterAttributes.put(NAME_AFFIXES_FILE_NAME, 
    NameAffixesFileName);
    ParameterAttributes.put(PLACE_AFFIXES_FILE_NAME, 
    PlaceAffixesFileName);
    ParameterAttributes.put(ORGANIZATIONS_AFFIXES_FILE_NAME, 
    OrganizationsAffixesFileName);
    ParameterAttributes.put(ORGANIZATIONS_FILE_NAME, 
    OrganizationsFileName);
    ParameterAttributes.put(ORGANIZATIONS_META_DATA_ATTRIBUTE, 
    OrganizationsMetaDataAttribute);
    ParameterAttributes.put(DETERMINE_CANONICAL_FORMS, 
    Tools.boolean2String(DetermineCanonicalForms));
    ParameterAttributes.put(PLACE_INDICATORS_FILE_NAME, 
    PlaceIndicatorsFileName);
    ParameterAttributes.put(CREATE_DEBUGGING_FILES, 
    Tools.boolean2String(CreateDebuggingFiles));
    ParameterAttributes.put(DEBUGGING_FILE_DIRECTORY, 
    DebuggingFileDirectory);
    ParameterAttributes.put(PERSON_NAME_INDICATORS_FILE_NAME, 
    PersonNameIndicatorsFileName);
    ParameterAttributes.put(PROFESSIONS_FILE_NAME, 
    ProfessionsFileName);
    ParameterAttributes.put(EXTRACT_STREET_BASIC_NE, 
    Tools.boolean2String(ExtractStreetBasicNe));
    ParameterAttributes.put(STREET_EXCEPTIONS_FILE_NAME, 
    StreetExceptionsFileName);
    ParameterAttributes.put(STREET_SUFFIXES_FILE_NAME, 
    StreetSuffixesFileName);    
    ParameterAttributes.put(STREET_PREFIX_TOKEN_REGEX, 
    StreetPrefixTokenRegex);    
    ParameterAttributes.put(STREET_AFFIX_TOKEN_REGEX, 
    StreetAffixTokenRegex);    
    ParameterAttributes.put(STREET_EXCLUSION_REGEX, 
    StreetExclusionRegex);    
    ParameterAttributes.put(MIN_TOKEN_IN_STREET, 
    Tools.int2String(MinTokenInStreet));
    ParameterAttributes.put(ORGANIZATION_INDICATOR_REGEX, 
    OrganizationIndicatorRegex);    

    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    ForenamesFileName = (String)ParameterAttributes
    .get(FORENAMES_FILE_NAME);
    SurnamesFileName = (String)ParameterAttributes
    .get(SURNAMES_FILE_NAME);
    SurnameSuffixesFileName = (String)ParameterAttributes
    .get(SURNAME_SUFFIXES_FILE_NAME);
    MiddleInitialsFileName = (String)ParameterAttributes
    .get(MIDDLE_INITIALS_FILE_NAME);
    TitlesFileName = (String)ParameterAttributes
    .get(TITLES_FILE_NAME);
    PlacesFileName = (String)ParameterAttributes
    .get(PLACES_FILE_NAME);
    OrganizationsStartFileName = (String)ParameterAttributes
    .get(ORGANIZATIONS_START_FILE_NAME);
    OrganizationsEndFileName = (String)ParameterAttributes
    .get(ORGANIZATIONS_END_FILE_NAME);
    CompositeFeaturesFileName = (String)ParameterAttributes
    .get(COMPOSITE_FEATURES_FILE_NAME);
    RegularExpressionsFileName = (String)ParameterAttributes
    .get(REGULAR_EXPRESSIONS_FILE_NAME);
    NameAffixesFileName = (String)ParameterAttributes
    .get(NAME_AFFIXES_FILE_NAME);
    PlaceAffixesFileName = (String)ParameterAttributes
    .get(PLACE_AFFIXES_FILE_NAME);
    OrganizationsAffixesFileName = (String)ParameterAttributes
    .get(ORGANIZATIONS_AFFIXES_FILE_NAME);
    OrganizationsFileName = (String)ParameterAttributes
    .get(ORGANIZATIONS_FILE_NAME);
    OrganizationsMetaDataAttribute = (String)ParameterAttributes
    .get(ORGANIZATIONS_META_DATA_ATTRIBUTE);
    DetermineCanonicalForms = Tools.string2Boolean((String)ParameterAttributes
    .get(DETERMINE_CANONICAL_FORMS));
    PlaceIndicatorsFileName = (String)ParameterAttributes
    .get(PLACE_INDICATORS_FILE_NAME);
    CreateDebuggingFiles = Tools.string2Boolean((String)ParameterAttributes
    .get(CREATE_DEBUGGING_FILES));
    DebuggingFileDirectory = (String)ParameterAttributes
    .get(DEBUGGING_FILE_DIRECTORY);
    PersonNameIndicatorsFileName = (String)ParameterAttributes
    .get(PERSON_NAME_INDICATORS_FILE_NAME);
    ProfessionsFileName = (String)ParameterAttributes
    .get(PROFESSIONS_FILE_NAME);
    ExtractStreetBasicNe = Tools.string2Boolean((String)ParameterAttributes
    .get(EXTRACT_STREET_BASIC_NE));
    StreetExceptionsFileName = (String)ParameterAttributes
    .get(STREET_EXCEPTIONS_FILE_NAME);
    StreetSuffixesFileName = (String)ParameterAttributes
    .get(STREET_SUFFIXES_FILE_NAME);    
    StreetPrefixTokenRegex = (String)ParameterAttributes
    .get(STREET_PREFIX_TOKEN_REGEX);
    StreetAffixTokenRegex = (String)ParameterAttributes
    .get(STREET_AFFIX_TOKEN_REGEX);
    StreetExclusionRegex = (String)ParameterAttributes
    .get(STREET_EXCLUSION_REGEX);
    MinTokenInStreet = Tools.string2Int((String)ParameterAttributes
    .get(MIN_TOKEN_IN_STREET));
    OrganizationIndicatorRegex = (String)ParameterAttributes
    .get(ORGANIZATION_INDICATOR_REGEX);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntityExtractorParameter getNeexParameter() {
    
    return new NamedEntityExtractorParameter(
    ForenamesFileName, SurnamesFileName,
    SurnameSuffixesFileName, MiddleInitialsFileName, TitlesFileName, 
    PlacesFileName, OrganizationsStartFileName, OrganizationsEndFileName, 
    CompositeFeaturesFileName, RegularExpressionsFileName,
    NameAffixesFileName, PlaceAffixesFileName, OrganizationsAffixesFileName,
    OrganizationsFileName, OrganizationsMetaDataAttribute,
    DetermineCanonicalForms, PlaceIndicatorsFileName, CreateDebuggingFiles, 
    DebuggingFileDirectory, PersonNameIndicatorsFileName, ProfessionsFileName,
    ExtractStreetBasicNe, StreetExceptionsFileName, StreetSuffixesFileName, 
    StreetPrefixTokenRegex, StreetAffixTokenRegex, StreetExclusionRegex, 
    MinTokenInStreet, OrganizationIndicatorRegex);
    
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
  
  public static void main(String args[]) {}
  
}