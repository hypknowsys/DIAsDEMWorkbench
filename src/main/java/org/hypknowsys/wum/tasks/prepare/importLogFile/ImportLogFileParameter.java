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

package org.hypknowsys.wum.tasks.prepare.importLogFile;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.server.*;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class ImportLogFileParameter extends WumScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private int LogFileFormat = 0;
  private String LogFileFilename = null;
  private boolean TruncateHtmlAnchor = false;
  private boolean TruncateCgiParameter = false;
  private boolean ConvertToLowerCase = false;
  private boolean SkipDataCleaning = false;
  private String IncludeList = null;
  private String ExcludeList = null;
  private String ReplaceList = null;
  private String TaxonomyList = null;  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.importLogFile"
  + ".ImportLogFileTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.importLogFile"
  + ".ImportLogFileParameterPanel";
  
  private final static String LOG_FILE_FORMAT =
  "LogFileFormat";
  private final static String LOG_FILE_FILENAME =
  "LogFileFilename";
  private final static String TRUNCATE_HTML_ANCHOR =
  "TruncateHtmlAnchor";
  private final static String TRUNCATE_CGI_PARAMETER =
  "TruncateCgiParameter";
  private final static String CONVERT_TO_LOWER_CASE =
  "ConvertToLowerCase";
  private final static String SKIP_DATA_CLEANING =
  "SkipDataCleaning";
  private final static String INCLUDE_LIST =
  "IncludeList";
  private final static String EXCLUDE_LIST =
  "ExcludeList";
  private final static String REPLACE_LIST =
  "ReplaceList";
  private final static String TAXONOMY_LIST =
  "TaxonomyList";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportLogFileParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public int getLogFileFormat() {
    return LogFileFormat; }
  public String getLogFileFilename() {
    return LogFileFilename; }
  public boolean getTruncateHtmlAnchor() {
    return TruncateHtmlAnchor; }
  public boolean getTruncateCgiParameter() {
    return TruncateCgiParameter; }
  public boolean getConvertToLowerCase() {
    return ConvertToLowerCase; }
  public boolean getSkipDataCleaning() {
    return SkipDataCleaning; }
  public String getIncludeList() {
    return IncludeList; }
  public String getExcludeList() {
    return ExcludeList; }
  public String getReplaceList() {
    return ReplaceList; }
  public String getTaxonomyList() {
    return TaxonomyList; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setLogFileFormat(int pLogFileFormat) {
    LogFileFormat = pLogFileFormat; }
  public void setLogFileFilename(String pLogFileFilename) {
    LogFileFilename = pLogFileFilename; }
  public void setTruncateHtmlAnchor(boolean pTruncateHtmlAnchor) {
    TruncateHtmlAnchor = pTruncateHtmlAnchor; }
  public void setTruncateCgiParameter(boolean pTruncateCgiParameter) {
    TruncateCgiParameter = pTruncateCgiParameter; }
  public void setConvertToLowerCase(boolean pConvertToLowerCase) {
    ConvertToLowerCase = pConvertToLowerCase; }
  public void setSkipDataCleaning(boolean pSkipDataCleaning) {
    SkipDataCleaning = pSkipDataCleaning; }
  public void setIncludeList(String pIncludeList) {
    IncludeList = pIncludeList; }
  public void setExcludeList(String pExcludeList) {
    ExcludeList = pExcludeList; }
  public void setReplaceList(String pReplaceList) {
    ReplaceList = pReplaceList; }
  public void setTaxonomyList(String pTaxonomyList) {
    TaxonomyList = pTaxonomyList; }
  
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
    ParameterAttributes.put(LOG_FILE_FORMAT, Tools
    .int2String(LogFileFormat));
    ParameterAttributes.put(LOG_FILE_FILENAME, LogFileFilename);
    ParameterAttributes.put(TRUNCATE_HTML_ANCHOR, Tools
    .boolean2String(TruncateHtmlAnchor));
    ParameterAttributes.put(TRUNCATE_CGI_PARAMETER, Tools
    .boolean2String(TruncateCgiParameter));
    ParameterAttributes.put(CONVERT_TO_LOWER_CASE, Tools
    .boolean2String(ConvertToLowerCase));
    ParameterAttributes.put(SKIP_DATA_CLEANING, Tools
    .boolean2String(SkipDataCleaning));
    ParameterAttributes.put(INCLUDE_LIST, IncludeList);
    ParameterAttributes.put(EXCLUDE_LIST, ExcludeList);
    ParameterAttributes.put(REPLACE_LIST, ReplaceList);
    ParameterAttributes.put(TAXONOMY_LIST, TaxonomyList);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    LogFileFormat = Tools.string2Int((String)ParameterAttributes
    .get(LOG_FILE_FORMAT));
    LogFileFilename = (String)ParameterAttributes.get(LOG_FILE_FILENAME);
    TruncateHtmlAnchor = Tools.string2Boolean((String)ParameterAttributes
    .get(TRUNCATE_HTML_ANCHOR));
    TruncateCgiParameter = Tools.string2Boolean((String)ParameterAttributes
    .get(TRUNCATE_CGI_PARAMETER));
    ConvertToLowerCase = Tools.string2Boolean((String)ParameterAttributes
    .get(CONVERT_TO_LOWER_CASE));
    SkipDataCleaning = Tools.string2Boolean((String)ParameterAttributes
    .get(SKIP_DATA_CLEANING));
    IncludeList = (String)ParameterAttributes.get(INCLUDE_LIST);
    ExcludeList = (String)ParameterAttributes.get(EXCLUDE_LIST);
    ReplaceList = (String)ParameterAttributes.get(REPLACE_LIST);
    TaxonomyList = (String)ParameterAttributes.get(TAXONOMY_LIST);
    
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