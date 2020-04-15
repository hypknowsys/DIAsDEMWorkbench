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

package org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities20;

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

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities20"
  + ".ReplaceNamedEntitiesTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities20"
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
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceNamedEntitiesParameter(String pCollectionFileName,
  String pForenamesFileName, String pSurnamesFileName, 
  String pSurnameSuffixesFileName, String pMiddleInitialsFileName, 
  String pTitlesFileName, String pPlacesFileName, 
  String pOrganizationsStartFileName, String pOrganizationsEndFileName, 
  String pCompositeFeaturesFileName, String pRegularExpressionsFileName) {
    
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
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntityExtractorParameter getNeexParameter() {
    
    return new NamedEntityExtractorParameter(
    ForenamesFileName, SurnamesFileName,
    SurnameSuffixesFileName, MiddleInitialsFileName, TitlesFileName, 
    PlacesFileName, OrganizationsStartFileName, OrganizationsEndFileName, 
    CompositeFeaturesFileName, RegularExpressionsFileName, null, null, null,
    null, null, true, null, false, null, null, null, false, null, null,
    null, null, null, 0, null);
    
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