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

package org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles;

import java.util.TreeMap;

import org.hypknowsys.diasdem.server.DiasdemScriptableTaskParameter;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Ingo Kampe
 */

public class SearchTheWebForHtmlFilesParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String ResultFileName = null;
  private String GoogleKey = null;
  private String GoogleSearchString = null;
  private int GoogleSearchMaxResults = 10;
  private String GoogleSearchSites = null;
  private String GoogleSearchFiletypes = null;
  private String GoogleSearchDateStart = null;
  private String GoogleSearchDateEnd = null;
  private String GoogleSearchLanguage = null;  
  
  private SearchParams searchParams = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles"
  + ".SearchTheWebForHtmlFilesTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles"
  + ".SearchTheWebForHtmlFilesParameterPanel";
  
  private final static String RESULT_FILE_NAME =
  "ResultFileName";
  private final static String GOOGLE_KEY =
  "GoogleKey";
  private final static String GOOGLE_SEARCH_STRING =
  "GoogleSearchString";
  private final static String GOOGLE_MAX_RESULTS =
  "GoogleSearchMaxResults";
  private final static String GOOGLE_SEARCH_SITES =
  "GoogleSearchSites";
  private final static String GOOGLE_SEARCH_FILETYPES =
  "GoogleSearchFiletypes";
  private final static String GOOGLE_SEARCH_DATE_START =
  "GoogleSearchDateStart";
  private final static String GOOGLE_SEARCH_DATE_END =
  "GoogleSearchDateEnd";
  private final static String GOOGLE_SEARCH_LANGUAGE =
  "GoogleSearchLanguage";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public SearchTheWebForHtmlFilesParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public SearchTheWebForHtmlFilesParameter(String pResultFileName, 
  SearchParams params) {
    
    this();
    
    ResultFileName = pResultFileName;
    searchParams = params;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getResultFileName() {
    return ResultFileName; 
  }
 public String getGoogleKey() {
    return GoogleKey;
  }
 public String getGoogleSearchString() {
    return GoogleSearchString;
  }
 public int getGoogleSearchMaxResults() {
    return GoogleSearchMaxResults;
  }
 public String getGoogleSearchSites() {
    return GoogleSearchSites;
  }
 public String getGoogleSearchFiletypes() {
    return GoogleSearchFiletypes;
  }
 public String getGoogleSearchDateStart() {
    return GoogleSearchDateStart;
  }
 public String getGoogleSearchDateEnd() {
    return GoogleSearchDateEnd;
  }
 public String getGoogleSearchLanguage() {
    return GoogleSearchLanguage;
  }  
  
  public SearchParams getSearchParams() {
    return searchParams;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setResultFileName(String pResultFileName) {
    ResultFileName = pResultFileName; 
  }
 public void setGoogleKey(String pGoogleKey) {
    GoogleKey = pGoogleKey;
  }
 public void setGoogleSearchString(String pGoogleSearchString) {
    GoogleSearchString = pGoogleSearchString;
  }
 public void setGoogleSearchMaxResults(int pGoogleSearchMaxResults) {
    GoogleSearchMaxResults = pGoogleSearchMaxResults;
  }
 public void setGoogleSearchSites(String pGoogleSearchSites) {
    GoogleSearchSites = pGoogleSearchSites;
  }
 public void setGoogleSearchFiletypes(String pGoogleSearchFiletypes) {
    GoogleSearchFiletypes = pGoogleSearchFiletypes;
  }
 public void setGoogleSearchDateStart(String pGoogleSearchDateStart) {
    GoogleSearchDateStart = pGoogleSearchDateStart;
  }
 public void setGoogleSearchDateEnd(String pGoogleSearchDateEnd) {
    GoogleSearchDateEnd = pGoogleSearchDateEnd;
  }
 public void setGoogleSearchLanguage(String pGoogleSearchLanguage) {
    GoogleSearchLanguage = pGoogleSearchLanguage;
  }
 
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
    ParameterAttributes.put(RESULT_FILE_NAME, ResultFileName);
    ParameterAttributes.put(GOOGLE_KEY, GoogleKey);
    ParameterAttributes.put(GOOGLE_SEARCH_STRING, GoogleSearchString);
    ParameterAttributes.put(GOOGLE_MAX_RESULTS, 
    Tools.int2String(GoogleSearchMaxResults));
    ParameterAttributes.put(GOOGLE_SEARCH_SITES, GoogleSearchSites);
    ParameterAttributes.put(GOOGLE_SEARCH_FILETYPES, GoogleSearchFiletypes);
    ParameterAttributes.put(GOOGLE_SEARCH_DATE_START, GoogleSearchDateStart);
    ParameterAttributes.put(GOOGLE_SEARCH_DATE_END, GoogleSearchDateEnd);
    ParameterAttributes.put(GOOGLE_SEARCH_LANGUAGE, GoogleSearchLanguage);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    ResultFileName = (String)ParameterAttributes.get(RESULT_FILE_NAME);
    GoogleKey = (String)ParameterAttributes.get(GOOGLE_KEY);
    GoogleSearchString = (String)ParameterAttributes.get(GOOGLE_SEARCH_STRING);
    GoogleSearchMaxResults = Tools.string2Int((String)ParameterAttributes
    .get(GOOGLE_MAX_RESULTS));
    GoogleSearchSites = (String)ParameterAttributes.get(GOOGLE_SEARCH_SITES);
    GoogleSearchFiletypes = (String)ParameterAttributes.get(GOOGLE_SEARCH_FILETYPES);
    GoogleSearchDateStart = (String)ParameterAttributes.get(GOOGLE_SEARCH_DATE_START);
    GoogleSearchDateEnd = (String)ParameterAttributes.get(GOOGLE_SEARCH_DATE_END);
    GoogleSearchLanguage = (String)ParameterAttributes.get(GOOGLE_SEARCH_LANGUAGE);
    
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