/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.tasks.understand.generateSummaryReport;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.server.*;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class GenerateSummaryReportParameter extends WumScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String ReportFilename = "";
  protected int NumberOfMostRequestedPages = 0;
  protected int NumberOfMostRequestedDirectories = 0;
  protected int NumberOfLeastRequestedPages = 0;
  protected int NumberOfLeastRequestedDirectories = 0;
  protected int NumberOfTopVisitors = 0;
  protected int NumberOfMostActiveTopLevelDomains = 0;
  protected int NumberOfMostActiveSecondLevelDomains = 0;
  protected int NumberOfTopEntryPages = 0;
  protected int NumberOfTopExitPages = 0;
  protected int NumberOfTopReferrerPages = 0;
  protected int NumberOfTopReferrerSites = 0;
  protected int NumberOfMostUsedBrowsers = 0;
  protected int NumberOfSingleAccessPages = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.wum.tasks.understand.generateSummaryReport"
  + ".GenerateSummaryReportTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.understand.generateSummaryReport"
  + ".GenerateSummaryReportPanel";
  
  private final static String REPORT_FILENAME =
  "ReportFilename";
  private final static String NUMBER_OF_MOST_REQUESTED_PAGES =
  "NumberOfMostRequestedPages";
  private final static String NUMBER_OF_MOST_REQUESTED_DIRECTORIES =
  "NumberOfMostRequestedDirectories";
  private final static String NUMBER_OF_LEAST_REQUESTED_PAGES =
  "NumberOfLeastRequestedPages";
  private final static String NUMBER_OF_LEAST_REQUESTED_DIRECTORIES =
  "NumberOfLeastRequestedDirectories";
  private final static String NUMBER_OF_TOP_VISITORS =
  "NumberOfTopVisitors";
  private final static String NUMBER_OF_MOST_ACTIVE_TOP_LEVEL_DIRECTORIES =
  "NumberOfMostActiveTopLevelDomains";
  private final static String NUMBER_OF_MOST_ACTIVE_SECOND_LEVEL_DIRECTORIES =
  "NumberOfMostActiveSecondLevelDomains";
  private final static String NUMBER_OF_TOP_ENTRY_PAGES =
  "NumberOfTopEntryPages";
  private final static String NUMBER_OF_TOP_EXIT_PAGES =
  "NumberOfTopExitPages";
  private final static String NUMBER_OF_TOP_REFERRER_PAGES =
  "NumberOfTopReferrerPages";
  private final static String NUMBER_OF_TOP_REFERRER_SITES =
  "NumberOfTopReferrerSites";
  private final static String NUMBER_OF_MOST_USED_BROWSERS =
  "NumberOfMostUsedBrowsers";
  private final static String NUMBER_OF_SINGLE_ACCESS_PAGES =
  "NumberOfSingleAccessPages";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public GenerateSummaryReportParameter() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getReportFilename() { 
    return ReportFilename; }
  public int getNumberOfMostRequestedPages() { 
    return NumberOfMostRequestedPages; }
  public int getNumberOfMostRequestedDirectories() { 
    return NumberOfMostRequestedDirectories; }
  public int getNumberOfLeastRequestedPages() { 
    return NumberOfLeastRequestedPages; }
  public int getNumberOfLeastRequestedDirectories() { 
    return NumberOfLeastRequestedDirectories; }
  public int getNumberOfTopVisitors() { 
    return NumberOfTopVisitors; }
  public int getNumberOfMostActiveTopLevelDomains() { 
    return NumberOfMostActiveTopLevelDomains; }
  public int getNumberOfMostActiveSecondLevelDomains() { 
    return NumberOfMostActiveSecondLevelDomains; }
  public int getNumberOfTopEntryPages() { 
    return NumberOfTopEntryPages; }
  public int getNumberOfTopExitPages() { 
    return NumberOfTopExitPages; }
  public int getNumberOfTopReferrerPages() { 
    return NumberOfTopReferrerPages; }
  public int getNumberOfTopReferrerSites() { 
    return NumberOfTopReferrerSites; }
  public int getNumberOfMostUsedBrowsers() { 
    return NumberOfMostUsedBrowsers; }
  public int getNumberOfSingleAccessPages() { 
    return NumberOfSingleAccessPages; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setReportFilename(String pReportFilename) { 
    ReportFilename = pReportFilename; }
  public void setNumberOfMostRequestedPages(int pNumberOfMostRequestedPages) { 
    NumberOfMostRequestedPages = pNumberOfMostRequestedPages; }
  public void setNumberOfMostRequestedDirectories(int pNumberOfMostRequestedDirectories) { 
    NumberOfMostRequestedDirectories = pNumberOfMostRequestedDirectories; }
  public void setNumberOfLeastRequestedPages(int pNumberOfLeastRequestedPages) { 
    NumberOfLeastRequestedPages = pNumberOfLeastRequestedPages; }
  public void setNumberOfLeastRequestedDirectories(int pNumberOfLeastRequestedDirectories) { 
    NumberOfLeastRequestedDirectories = pNumberOfLeastRequestedDirectories; }
  public void setNumberOfTopVisitors(int pNumberOfTopVisitors) { 
    NumberOfTopVisitors = pNumberOfTopVisitors; }
  public void setNumberOfMostActiveTopLevelDomains(int pNumberOfMostActiveTopLevelDomains) { 
    NumberOfMostActiveTopLevelDomains = pNumberOfMostActiveTopLevelDomains; }
  public void setNumberOfMostActiveSecondLevelDomains(int pNumberOfMostActiveSecondLevelDomains) { 
    NumberOfMostActiveSecondLevelDomains = pNumberOfMostActiveSecondLevelDomains; }
  public void setNumberOfTopEntryPages(int pNumberOfTopEntryPages) { 
    NumberOfTopEntryPages = pNumberOfTopEntryPages; }
  public void setNumberOfTopExitPages(int pNumberOfTopExitPages) { 
    NumberOfTopExitPages = pNumberOfTopExitPages; }
  public void setNumberOfTopReferrerPages(int pNumberOfTopReferrerPages) { 
    NumberOfTopReferrerPages = pNumberOfTopReferrerPages; }
  public void setNumberOfTopReferrerSites(int pNumberOfTopReferrerSites) { 
    NumberOfTopReferrerSites = pNumberOfTopReferrerSites; }
  public void setNumberOfMostUsedBrowsers(int pNumberOfMostUsedBrowsers) { 
    NumberOfMostUsedBrowsers = pNumberOfMostUsedBrowsers; }
  public void setNumberOfSingleAccessPages(int pNumberOfSingleAccessPages) { 
    NumberOfSingleAccessPages = pNumberOfSingleAccessPages; }
  
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
    ParameterAttributes.put(REPORT_FILENAME, ReportFilename);
    ParameterAttributes.put(NUMBER_OF_MOST_REQUESTED_PAGES, 
    Tools.int2String(NumberOfMostRequestedPages));
    ParameterAttributes.put(NUMBER_OF_MOST_REQUESTED_DIRECTORIES, 
    Tools.int2String(NumberOfMostRequestedDirectories));
    ParameterAttributes.put(NUMBER_OF_LEAST_REQUESTED_PAGES, 
    Tools.int2String(NumberOfLeastRequestedPages));
    ParameterAttributes.put(NUMBER_OF_LEAST_REQUESTED_DIRECTORIES, 
    Tools.int2String(NumberOfLeastRequestedDirectories));
    ParameterAttributes.put(NUMBER_OF_TOP_VISITORS, 
    Tools.int2String(NumberOfTopVisitors));
    ParameterAttributes.put(NUMBER_OF_MOST_ACTIVE_TOP_LEVEL_DIRECTORIES, 
    Tools.int2String(NumberOfMostActiveTopLevelDomains));
    ParameterAttributes.put(NUMBER_OF_MOST_ACTIVE_SECOND_LEVEL_DIRECTORIES, 
    Tools.int2String(NumberOfMostActiveSecondLevelDomains));
    ParameterAttributes.put(NUMBER_OF_TOP_ENTRY_PAGES, 
    Tools.int2String(NumberOfTopEntryPages));
    ParameterAttributes.put(NUMBER_OF_TOP_EXIT_PAGES, 
    Tools.int2String(NumberOfTopExitPages));
    ParameterAttributes.put(NUMBER_OF_TOP_REFERRER_PAGES, 
    Tools.int2String(NumberOfTopReferrerPages));
    ParameterAttributes.put(NUMBER_OF_TOP_REFERRER_SITES, 
    Tools.int2String(NumberOfTopReferrerSites));
    ParameterAttributes.put(NUMBER_OF_MOST_USED_BROWSERS, 
    Tools.int2String(NumberOfMostUsedBrowsers));
    ParameterAttributes.put(NUMBER_OF_SINGLE_ACCESS_PAGES, 
    Tools.int2String(NumberOfSingleAccessPages));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    ReportFilename = (String)ParameterAttributes.get(REPORT_FILENAME);
    NumberOfMostRequestedPages = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_MOST_REQUESTED_PAGES));
    NumberOfMostRequestedDirectories = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_MOST_REQUESTED_DIRECTORIES));
    NumberOfLeastRequestedPages = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_LEAST_REQUESTED_PAGES));
    NumberOfLeastRequestedDirectories = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_LEAST_REQUESTED_DIRECTORIES));
    NumberOfTopVisitors = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_TOP_VISITORS));
    NumberOfMostActiveTopLevelDomains = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_MOST_ACTIVE_TOP_LEVEL_DIRECTORIES));
    NumberOfMostActiveSecondLevelDomains = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_MOST_ACTIVE_SECOND_LEVEL_DIRECTORIES));
    NumberOfTopEntryPages = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_TOP_ENTRY_PAGES));
    NumberOfTopExitPages = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_TOP_EXIT_PAGES));
    NumberOfTopReferrerPages = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_TOP_REFERRER_PAGES));
    NumberOfTopReferrerSites = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_TOP_REFERRER_SITES));
    NumberOfMostUsedBrowsers = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_MOST_USED_BROWSERS));
    NumberOfSingleAccessPages = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_SINGLE_ACCESS_PAGES));
    
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