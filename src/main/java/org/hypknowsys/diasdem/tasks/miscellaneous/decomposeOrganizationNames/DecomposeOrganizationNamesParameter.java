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

package org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class DecomposeOrganizationNamesParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String TokenizedOrganizationsFileName = null;
  protected String OrganizationSuffixesFileName = null;
  protected String BlacklistOfOrganizationsFileName = null;
  protected String ShortenedOrganizationsFileName = null;
  protected boolean ExtractTokensOfDecomposedCompanyNames = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames"
  + ".DecomposeOrganizationNamesTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames"
  + ".DecomposeOrganizationNamesParameterPanel";
  
  private final static String TOKENIZED_ORGANIZATIONS_FILE_NAME =
  "TokenizedOrganizationsFileName";
  private final static String ORGANIZATION_SUFFIXES_FILE_NAME =
  "OrganizationSuffixesFileName";
  private final static String BLACKLIST_OF_ORGANIZATIONS_FILE_NAME =
  "BlacklistOfOrganizationsFileName";
  private final static String SHORTENED_ORGANIZATIONS_FILE_NAME =
  "ShortenedOrganizationsFileName";
  private final static String EXTRACT_TOKENS_OF_DECOMPOSED_COMPANY_NAMES =
  "ExtractTokensOfDecomposedCompanyNames";

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DecomposeOrganizationNamesParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    TokenizedOrganizationsFileName = null;
    OrganizationSuffixesFileName = null;
    BlacklistOfOrganizationsFileName = null;
    ShortenedOrganizationsFileName = null;
    ExtractTokensOfDecomposedCompanyNames = false;

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DecomposeOrganizationNamesParameter(
  String pTokenizedOrganizationsFileName, String pOrganizationSuffixesFileName,
  String pBlacklistOfOrganizationsFileName, String pShortenedOrganizationsFileName,
  boolean pExtractTokensOfDecomposedCompanyNames) {
    
    this();
    
    TokenizedOrganizationsFileName = pTokenizedOrganizationsFileName;
    OrganizationSuffixesFileName = pOrganizationSuffixesFileName;
    BlacklistOfOrganizationsFileName = pBlacklistOfOrganizationsFileName;
    ShortenedOrganizationsFileName = pShortenedOrganizationsFileName;
    ExtractTokensOfDecomposedCompanyNames = 
    pExtractTokensOfDecomposedCompanyNames;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTokenizedOrganizationsFileName() {
    return TokenizedOrganizationsFileName; }
  public String getOrganizationSuffixesFileName() {
    return OrganizationSuffixesFileName; }
  public String getBlacklistOfOrganizationsFileName() {
    return BlacklistOfOrganizationsFileName; }
  public String getShortenedOrganizationsFileName() {
    return ShortenedOrganizationsFileName; }
  public boolean extractTokensOfDecomposedCompanyNames() {
    return ExtractTokensOfDecomposedCompanyNames; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setExtractTokensOfDecomposedCompanyNames(
  boolean pExtractTokensOfDecomposedCompanyNames) {
    ExtractTokensOfDecomposedCompanyNames = 
    pExtractTokensOfDecomposedCompanyNames; }
  
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
    ParameterAttributes.put(TOKENIZED_ORGANIZATIONS_FILE_NAME, 
    TokenizedOrganizationsFileName);
    ParameterAttributes.put(ORGANIZATION_SUFFIXES_FILE_NAME, 
    OrganizationSuffixesFileName);
    ParameterAttributes.put(BLACKLIST_OF_ORGANIZATIONS_FILE_NAME, 
    BlacklistOfOrganizationsFileName);
    ParameterAttributes.put(SHORTENED_ORGANIZATIONS_FILE_NAME, 
    ShortenedOrganizationsFileName);
    ParameterAttributes.put(EXTRACT_TOKENS_OF_DECOMPOSED_COMPANY_NAMES, 
    Tools.boolean2String(ExtractTokensOfDecomposedCompanyNames));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    TokenizedOrganizationsFileName = (String)ParameterAttributes
    .get(TOKENIZED_ORGANIZATIONS_FILE_NAME);
    OrganizationSuffixesFileName = (String)ParameterAttributes
    .get(ORGANIZATION_SUFFIXES_FILE_NAME);
    BlacklistOfOrganizationsFileName = (String)ParameterAttributes
    .get(BLACKLIST_OF_ORGANIZATIONS_FILE_NAME);
    ShortenedOrganizationsFileName = (String)ParameterAttributes
    .get(SHORTENED_ORGANIZATIONS_FILE_NAME);
    ExtractTokensOfDecomposedCompanyNames = Tools.string2Boolean((String)
    ParameterAttributes.get(EXTRACT_TOKENS_OF_DECOMPOSED_COMPANY_NAMES));
    
  }
  
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
  
  public static void main(String args[]) {}
  
}