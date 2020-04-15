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

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.text.*;

import com.google.soap.search.*;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.server.DiasdemScriptableNonBlockingTask;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Ingo Kampe
 */

public class SearchTheWebForHtmlFilesTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private SearchTheWebForHtmlFilesParameter CastParameter = null;
  private SearchTheWebForHtmlFilesResult CastResult = null;
  private GoogleSearchResult searchResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static int DATE_FORMAT = DateFormat.SHORT;
  private final static Locale DATE_LOCALE = Locale.GERMAN;
  
  private final static String LABEL =
  "Search the Web for HTML Files";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles"
  + ".SearchTheWebForHtmlFilesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles"
  + ".SearchTheWebForHtmlFilesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles"
  + ".SearchTheWebForHtmlFilesControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_RESULT_FILE_NAME", 
    "Search the Web for HTML Files: Default Download URL File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_KEY", 
    "Search the Web for HTML Files: Google Search Key",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_STRING", 
    "Search the Web for HTML Files: Google Search String",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_MAX_RESULTS", 
    "Search the Web for HTML Files: Max. Number of URLs to be Retrieved",
    "100", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_SITES", 
    "Search the Web for HTML Files: Restrict Search to these Web Sites",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_FILETYPES", 
    "Search the Web for HTML Files: Restrict Search to these File Types",
    "html,htm", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_DATE_START", 
    "Search the Web for HTML Files: Start Date that Restricts Search (YYYY-MM-DD)",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_DATE_END", 
    "Search the Web for HTML Files: End Date that Restricts Search (YYYY-MM-DD)",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_LANGUAGE", 
    "Search the Web for HTML Files: Restrict Search to this Language",
    "lang_en", KProperty.STRING, KProperty.NOT_EDITABLE),
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public SearchTheWebForHtmlFilesTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
    
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
  /* ########## interface NonBlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {

    SearchTheWebForHtmlFilesParameter parameter = null;
    if (pParameter instanceof SearchTheWebForHtmlFilesParameter) {
      parameter = (SearchTheWebForHtmlFilesParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);

    /** validate GUI entry filename **/
    if (parameter.getResultFileName().trim().length() <= 0
    || !parameter.getResultFileName().trim().endsWith(
    DIAsDEMguiPreferences.URL_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local download URL\n"
      + "file name (" + DIAsDEMguiPreferences.URL_FILE_EXTENSION 
      + ") in the field 'Download URL File'!");
    }
    if (parameter.getGoogleKey().trim().length() <= 0) {
      result.addError(
      "Error: Please enter a valid personal Google\n"
      + "API key in the field 'Google Search Key'!");
    }
    if (parameter.getGoogleSearchString().trim().length() <= 0) {
      result.addError(
      "Error: Please enter words to search for\n"
      + "in the field 'Google Search String'!");
    } 
    if (parameter.getGoogleSearchMaxResults() <= 0) {
      result.addError(
      "Error: Please enter the maximum number (> 0) of URLs to\n"
      + "be retrieved in the field 'Max. Number of Results'!");
    }
    
    if (!Tools.stringIsNullOrEmpty(parameter.getGoogleSearchDateStart())) {
      try {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date)formatter.parse(parameter.getGoogleSearchDateStart());
      } catch (java.text.ParseException e) {
        result.addError(
        "Error: Please enter either nothing or a valid date (format:\n"
        + "YYYY-MM-DD) in the field 'Restrict Date (Start)'!");
      }
      
    }  
    if (!Tools.stringIsNullOrEmpty(parameter.getGoogleSearchDateEnd())) {
      try {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date)formatter.parse(parameter.getGoogleSearchDateEnd());
      } catch (java.text.ParseException e) {
        result.addError(
        "Error: Please enter either nothing or a valid date (format:\n"
        + "YYYY-MM-DD) in the field 'Restrict Date (End)'!");
      }
      
    }
     
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new SearchTheWebForHtmlFilesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new SearchTheWebForHtmlFilesResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_MISCELLANEOUS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof SearchTheWebForHtmlFilesParameter) {
      CastParameter = (SearchTheWebForHtmlFilesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: The Web cannot be searched for HTML files!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations ...");
    this.validateParameter(Parameter, shortErrorMessage);
        
    String message = ""; // holds the final status information
    GoogleSearch googleSearch = null; // search object to fill with params
    
    SearchParams params = new SearchParams();
    try {
      // Creat Search Parameter instance
      params.setGoogleKey(CastParameter.getGoogleKey());
      params.setGoogleSearchString(CastParameter.getGoogleSearchString());
      params.setGoogleSearchMaxResults(CastParameter.getGoogleSearchMaxResults());
      Date start = null;
      Date end = null;
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      try {
        start = (Date)formatter.parse(CastParameter.getGoogleSearchDateStart());
        end = (Date)formatter.parse(CastParameter.getGoogleSearchDateEnd());
      } catch (java.text.ParseException ex) {
        // System.out.println("Date Parsing Error" + ex.getMessage());
      }
      params.setGoogleSearchDateStart(start);
      params.setGoogleSearchDateEnd(end);      
      params.setGoogleSearchFiletypes(CastParameter.getGoogleSearchFiletypes()
      .split(","));
      params.setGoogleSearchSites(CastParameter.getGoogleSearchSites()
      .split(","));
      
      // Search Params creates needed search object with all params inside
      googleSearch = params.getGoogleSearch();
    } catch (VerifyError incomplete) {
      // search pattern are incomplete for a valid search
      // this should be prevented by validateTaskParameter method
      CastResult = new SearchTheWebForHtmlFilesResult(TaskResult.FINAL_RESULT,
      "The task 'Search the Web for HTML Files'\nfailed due to incomplete search parameters!",  
      "The task 'Search the Web for HTML Files' failed due to incomplete search parameters!");      
    } catch (Exception ex) {
      // unknown error during search
      ex.printStackTrace();
      CastResult = new SearchTheWebForHtmlFilesResult(TaskResult.FINAL_RESULT,
      "The task 'Search the Web for HTML Files'\nfailed due to an internal error!",  
      "The task 'Search the Web for HTML Files' failed due to an internal error!");      
    }
    
    int startResult = 0;
    int downloadedResults = 0;
    boolean stop = false;
    try {
      if (googleSearch != null) {
        int maxNumberOfResults = params.getGoogleSearchMaxResults();
        params.setGoogleSearchMaxResults(10);
        while (downloadedResults < maxNumberOfResults && !stop) {          
          Progress.update(TaskProgress.INDETERMINATE,
          "Number of Retrieved Result URLs: " + downloadedResults);
          DiasdemServer.setTaskProgress(Progress, TaskThread);
          
          googleSearch = params.getGoogleSearch();
          googleSearch.setStartResult(startResult);
          searchResult = googleSearch.doSearch();
          
          this.writeResultToFile(searchResult,CastParameter, 
          (startResult == 0 ? false : true));
          // System.out.println(searchResult.getEndIndex() + "/"
          // + searchResult.getEstimatedTotalResultsCount());
          downloadedResults += searchResult.getResultElements().length;
          startResult = searchResult.getEndIndex();
          if (searchResult.getEndIndex() >= searchResult
          .getEstimatedTotalResultsCount()) {
            stop = true;
          }
        }
      }
    } catch (GoogleSearchFault f) {
      // System.out.println("The call to the Google Web APIs failed:");
      // System.out.println(f.toString());
      message = f.getMessage();
      try {
      message = message.substring(message.indexOf("msg"),
      message.indexOf(";",message.indexOf("msg")));
      } catch (Exception ignore) {};
      if (message.length() >= 0) {
        System.out.println(message);
      }
    }
   
    if ((searchResult != null) && (searchResult.getEstimatedTotalResultsCount() > 0)) {
      CastResult = new SearchTheWebForHtmlFilesResult(TaskResult.FINAL_RESULT,
      "The Web was successfully searched for HTML files.\n"
      + "Estimated number of relevant URLs: " 
      + searchResult.getEstimatedTotalResultsCount() + "\n"
      + "Number of retrieved, most relevant URLs: " + downloadedResults, 
      "The Web was successfully searched for HTML files.\n"
      + "Estimated number of releant URLs: " 
      + searchResult.getEstimatedTotalResultsCount() + "\n"
      + "Number of retrieved, most relevant URLs: " + downloadedResults);
    } else if (searchResult == null) {
      if (CastResult == null) {
        CastResult = new SearchTheWebForHtmlFilesResult(TaskResult.FINAL_RESULT,
        "The task 'Search the Web for HTML Files' failed:\n" + message, 
        "The task 'Search the Web for HTML Files' failed:\n" + message);
      }
    } else {
      CastResult = new SearchTheWebForHtmlFilesResult(TaskResult.FINAL_RESULT,
      "The Web was successfully searched for HTML files.\n"
      + "Estimated number of relevant URLs: 0\n"
      + "Number of retrieved, most relevant URLs: 0", 
      "The Web was successfully searched for HTML files.\n"
      + "Estimated number of releant URLs: 0\n"
      + "Number of retrieved, most relevant URLs: 0");
    }
    this.setTaskResult(100, "Task Finished ...", CastResult,
    TaskResult.FINAL_RESULT, Task.TASK_FINISHED);
    
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

  /**
   * method takes a search result object and writes the content of it to
   * a filepath taken form the parameter object
   * @param result the filled search result
   * @param params current parameter object
   */
  private void writeResultToFile(GoogleSearchResult result, 
  SearchTheWebForHtmlFilesParameter params, boolean pAppend) {
    // test paramter
    if ((result == null) || (params == null)) return;
    
    FileWriter file = null;
    // open file
    try {
      file = new FileWriter(params.getResultFileName(), pAppend);
    } catch (Exception ex) {
      System.out.println("result file open failed");
      ex.printStackTrace();
      return;
    }                                       
    // write result to file
    try {
    file.write("# Google Search Result (" + result.getSearchTime() + " sec.)," 
    + " Start Index: " + result.getStartIndex() + "," 
    + " End Index: " + result.getEndIndex()  + "\n");
    file.write("# Search Date: " + (new Date()).toString() + "\n");
    file.write("# Search String: " + result.getSearchQuery() + "\n");
    GoogleSearchResultElement resultElements[] = result.getResultElements();
    for (int i = 0; i < resultElements.length; i++) {
      GoogleSearchResultElement resultElement = resultElements[i];
      // System.out.println("Result " + i + ": " + resultElement.getURL());
      file.write(resultElement.getURL() + "\n");
    }    
    // close file
    file.flush();
    file.close();
    } catch (IOException ioEx) {
       System.out.println("Failure during file write: " + ioEx.getMessage());
    }
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}