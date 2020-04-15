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

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class NamedEntityExtractorParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String ForenamesFileName = null;
  protected String SurnamesFileName = null;
  protected String SurnameSuffixesFileName = null;
  protected String MiddleInitialsFileName = null;
  protected String TitlesFileName = null;
  protected String PlacesFileName = null;
  protected String OrganizationsStartFileName = null;
  protected String OrganizationsEndFileName = null;
  protected String CompositeFeaturesFileName = null;
  protected String RegularExpressionsFilename = null;
  protected String NameAffixesFileName = null;  // NEEX 2.1 only
  protected String PlaceAffixesFileName = null;  // NEEX 2.1 only
  protected String OrganizationsAffixesFileName = null;  // NEEX 2.1 only
  protected String OrganizationsFileName = null;  // NEEX 2.1 only
  protected String OrganizationsMetaDataAttribute = null;  // NEEX 2.1 only
  protected boolean DetermineCanonicalForms = false;  // NEEX 2.1 only
  protected String PlaceIndicatorsFileName = null;  // NEEX 2.1 only
  protected boolean CreateDebuggingFiles = false;  // NEEX 2.1 only
  protected String DebuggingFileDirectory = null;  // NEEX 2.1 only
  protected String PersonNameIndicatorsFileName = null;  // NEEX 2.1 only
  protected String ProfessionsFileName = null;  // NEEX 2.1 only
  protected boolean ExtractStreetBasicNe = false;  // NEEX 2.1 only
  protected String StreetExceptionsFileName = null;  // NEEX 2.1 only
  protected String StreetSuffixesFileName = null;  // NEEX 2.1 only
  protected String StreetPrefixTokenRegex = null;  // NEEX 2.1 only
  protected String StreetAffixTokenRegex = null;  // NEEX 2.1 only
  protected String StreetExclusionRegex = null;  // NEEX 2.1 only
  protected int MinTokenInStreet = 0;  // NEEX 2.1 only
  protected String OrganizationIndicatorRegex = null;  // NEEX 2.1 only
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntityExtractorParameter(
  String pForenamesFileName, String pSurnamesFileName,
  String pSurnameSuffixesFileName, String pMiddleInitialsFileName,
  String pTitlesFileName, String pPlacesFileName,
  String pOrganizationsStartFileName, String pOrganizationsEndFileName,
  String pCompositeFeaturesFileName, String pRegularExpressionsFilename,
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
    
    ForenamesFileName = pForenamesFileName;
    SurnamesFileName = pSurnamesFileName;
    SurnameSuffixesFileName = pSurnameSuffixesFileName;
    MiddleInitialsFileName = pMiddleInitialsFileName;
    TitlesFileName = pTitlesFileName;
    PlacesFileName = pPlacesFileName;
    OrganizationsStartFileName = pOrganizationsStartFileName;
    OrganizationsEndFileName = pOrganizationsEndFileName;
    CompositeFeaturesFileName = pCompositeFeaturesFileName;
    RegularExpressionsFilename = pRegularExpressionsFilename;
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
  public String getRegularExpressionsFilename() {
    return RegularExpressionsFilename; }
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
  
  public static void main(String pOptions[]) {}
  
}