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

import java.io.*;
import java.util.*;
import java.awt.event.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class ImportLogFileTask extends WumScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportLogFileParameter CastParameter = null;
  private ImportLogFileResult CastResult = null;
  
  private TextBufferedReader AccessLogFile = null;   
  private boolean CheckedAccessLog = false;
  private boolean ValidAccessLog = false;
 
  private AccessLog CurrentAccessLog = null;  
  private AccessLogLine CurrentAccessLogLine = null;
  private Traversal CurrentTraversal = null;
  private Page CurrentPage = null;
  private Visitor CurrentVisitor = null;
  private DataCleaning DataCleaner = null;
  private TaxonomyHierarchy Taxonomy = null;

  private int UpdateInterval = 50;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Import Log File";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.importLogFile"
  + ".ImportLogFileParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.importLogFile"
  + ".ImportLogFileResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.importLogFile"
  + ".ImportLogFileControlPanel";
  
  private int MIN_REQUIRED_MINING_BASE_STATUS = MiningBase
  .MINING_BASE_IS_OPEN_BUT_NO_LOG_FILE_IS_IMPORTED;
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("IMPORT_LOG_FILE:_DEFAULT_LOG_FILE_DIRECTORY", 
    "Import Log File: Default Log File Directory",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("IMPORT_LOG_FILE:_DEFAULT_INCLUDE_LIST", 
    "Import Log File: Default Include List",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("IMPORT_LOG_FILE:_DEFAULT_EXCLUDE_LIST", 
    "Import Log File: Default Exlude List",
    ".gif\n.jpg\n.jpeg\n.css\n.js", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("IMPORT_LOG_FILE:_DEFAULT_REPLACE_LIST", 
    "Import Log File: Default Replace List",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("IMPORT_LOG_FILE:_DEFAULT_TAXONOMY_LIST", 
    "Import Log File: Default Taxonomy List",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("IMPORT_LOG_FILE:_DEFAULT_LOG_FILE_FORMAT", 
    "Import Log File: Default Log File Format",
    Tools.int2String(AccessLog.COMMON_LOG_FILE_FORMAT), 
    KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportLogFileTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
    
    MinRequiredMiningBaseStatus = MIN_REQUIRED_MINING_BASE_STATUS;
    
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
    
    ImportLogFileParameter parameter = null;
    if (pParameter instanceof ImportLogFileParameter) {
      parameter = (ImportLogFileParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    if (!(pProject instanceof WUMproject && ( (WUMproject)pProject )
    .getMiningBaseStatus() >= MinRequiredMiningBaseStatus)) {
      result.addError(
      "Error: The project's mining base does not have\n" +
      "the minimum status code required by this task!");
    }
    
    File file = new File(parameter.getLogFileFilename());
    if (!file.exists() || !file.isFile()) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      "log file in the field 'Log File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ImportLogFileParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ImportLogFileResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.wum.client.gui.WUMguiMenuBar.ACTIONS_PREPARE_DATA_SET,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    UpdateInterval = WumProject.getIntProperty(
    "IMPORT_LOG_FILE:_LINES_UPDATE_INTERVAL");

    if (Parameter != null && Parameter instanceof ImportLogFileParameter) {
      CastParameter = (ImportLogFileParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Log file cannot be imported!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
        
    DataCleaner = new DataCleaning(WumProject.getMiningBase()); 
    DataCleaner.setExcludeList(CastParameter.getExcludeList());
    DataCleaner.setIncludeList(CastParameter.getIncludeList());
    DataCleaner.setReplaceList(CastParameter.getReplaceList());
    
    Taxonomy = new TaxonomyHierarchy(WumProject.getMiningBase());
    Taxonomy.setTaxonomyList(CastParameter.getTaxonomyList());

    long counterTotal = 0L; 
    long counterSkippedDataCleaning = 0L;
    long counterSkippedSyntax = 0L; 
    long counterImported = 0L; 
    long currentID = 0L;
    String accessLogLine = null;
    ValidAccessLog = false; 
    CheckedAccessLog = false;
    
    CurrentAccessLog = new AccessLog(CastParameter.getLogFileFilename(), 
      CastParameter.getLogFileFormat() );
    AccessLogFile = new TextBufferedReader( 
      new File(CurrentAccessLog.getLocation() ) );
    AccessLogFile.open();
    CurrentAccessLog.setSize( AccessLogFile.getSize() );
    currentID = WumProject.getMiningBase().getImportedAccessLogs()
      .add(CurrentAccessLog);
    CurrentAccessLog = WumProject.getMiningBase().getImportedAccessLogs()
      .get(currentID);
        
    // delete the TraversalLog before importing the 1st log file
    if (WumProject.getMiningBase().getImportedAccessLogs().countAll() == 1) {
      File currentTraversalLogFile = new File(
        WumProject.getMiningBase().getMiningBaseDirectory() + 
        WumProject.getMiningBase().TRAVERSAL_LOG_FILE );
      currentTraversalLogFile.delete(); 
      currentTraversalLogFile = null;
      WumProject.getMiningBase().setImportedTraversals(0);
    }
    WumProject.getMiningBase().setTraversalLog( new TextFile( 
      new File(WumProject.getMiningBase().getMiningBaseDirectory() + 
        MiningBase.TRAVERSAL_LOG_FILE ) ) );
    WumProject.getMiningBase().getTraversalLog().open();
    
    long totalSize = AccessLogFile.getSize();
    long importedSize = 0L;
    long lineSeparatorSize = System.getProperty("line.separator").length();

    Progress.update( (int)(10 / totalSize), "Processing Log File Line 1"); 
    WumServer.setTaskProgress(Progress, TaskThread);
    CurrentAccessLogLine = new AccessLogLine(); 

    accessLogLine = AccessLogFile.getFirstLine();  
    counterTotal++;
    while ( (accessLogLine != null) && ( (ValidAccessLog) || 
      (!ValidAccessLog && !CheckedAccessLog) ) ) {
      
      importedSize += (int)( accessLogLine.length() ) + lineSeparatorSize;
      if ( (counterTotal % UpdateInterval) == 0 ) {
        Progress.update( (int)(importedSize * 100 / totalSize), 
         "Processing Log File Line " + counterTotal); 
        WumServer.setTaskProgress(Progress, TaskThread);
      }

      CurrentAccessLogLine.fromAccessLogLine( CurrentAccessLog.getType(), 
        accessLogLine, CurrentAccessLog.getID() );

      if ( CurrentAccessLogLine.isValid() ) {
      
        if (!CheckedAccessLog) this.checkAccessLog();

        if (ValidAccessLog) {        
          if ( counterImported == 0L)
            CurrentAccessLog.setFirstTimeStamp( 
              CurrentAccessLogLine.getTimeStamp() );
          else
            CurrentAccessLog.setLastTimeStamp( 
              CurrentAccessLogLine.getTimeStamp() );

          if ( CastParameter.getTruncateHtmlAnchor() ) {
            if ( CurrentAccessLogLine.getToPage().indexOf("#") > 0 )
              CurrentAccessLogLine.setToPage( 
                CurrentAccessLogLine.getToPage().substring( 
                0, CurrentAccessLogLine.getToPage().indexOf("#") ) );
            if ( CurrentAccessLogLine.getFromPage().indexOf("#") > 0 )
              CurrentAccessLogLine.setFromPage( 
                CurrentAccessLogLine.getFromPage().substring( 
                0, CurrentAccessLogLine.getFromPage().indexOf("#") ) );
          }
          if ( CastParameter.getTruncateCgiParameter() ) {
            if ( CurrentAccessLogLine.getToPage().indexOf("?") > 0 )
              CurrentAccessLogLine.setToPage( 
                CurrentAccessLogLine.getToPage().substring( 0, 
                CurrentAccessLogLine.getToPage().indexOf("?") ) );
            if ( CurrentAccessLogLine.getFromPage().indexOf("?") > 0 )
              CurrentAccessLogLine.setFromPage( 
                CurrentAccessLogLine.getFromPage().substring( 
                0, CurrentAccessLogLine.getFromPage().indexOf("?") ) );
          }
          if ( CastParameter.getConvertToLowerCase() ) {
            // convert URLs to lower case
            CurrentAccessLogLine.setToPage( 
              CurrentAccessLogLine.getToPage().toLowerCase() );
            CurrentAccessLogLine.setFromPage( 
              CurrentAccessLogLine.getFromPage().toLowerCase() );
            CurrentAccessLogLine.setHost( 
              CurrentAccessLogLine.getHost().toLowerCase() );
          }

          if ( ! CastParameter.getSkipDataCleaning() )  // perform data cleaning
            CurrentAccessLogLine = 
              DataCleaner.checkAccessLogLine(CurrentAccessLogLine);
          if ( CurrentAccessLogLine.isValid() ) {
            importAccessLogLine();   
            counterImported++;
          }
          else
            counterSkippedDataCleaning++;
        }  // if (ValidAccessLog)
        
      }  // if ( CurrentAccessLogLine.isValid() )
      else
        counterSkippedSyntax++;
        
      accessLogLine = AccessLogFile.getNextLine();  
      counterTotal++;
      
    }  // while
    
  if (ValidAccessLog) {
  
    CurrentAccessLog.setLinesTotal(counterTotal - 1);
    CurrentAccessLog.setLinesSkipped(counterSkippedDataCleaning + 
      counterSkippedSyntax);
    CurrentAccessLog.setLinesImported(counterImported);   
    WumProject.getMiningBase().getImportedAccessLogs()
    .replace(CurrentAccessLog);
    
    WumProject.getMiningBase().setImportedSessions(0);
    WumProject.getMiningBase().setStatus(MiningBase
    .MINING_BASE_IS_OPEN_AND_LOG_FILE_IS_IMPORTED);
    
    String history = Tools.getSystemDate() + 
      Tools.getLineSeparator() + "Log File:" +
      Tools.getLineSeparator() + CastParameter.getLogFileFilename() + 
      Tools.getLineSeparator() + "Settings:" + 
      Tools.getLineSeparator();
    if (CastParameter.getLogFileFormat() 
    ==  AccessLog.COMMON_LOG_FILE_FORMAT) 
      history += "Common Log File";
    else
      if (CastParameter.getLogFileFormat() 
      ==  AccessLog.EXTENDED_LOG_FILE_FORMAT)
        history += "Extended Log File";
      else
        if (CastParameter.getLogFileFormat() ==  
          AccessLog.COOKIE_LOG_FILE_FORMAT )
          history += "Cookie Log File";
        else
          history += "WUMprep Log File";
    if ( CastParameter.getTruncateHtmlAnchor() ) 
      history += ", Truncate HTML Anchors";
    if ( CastParameter.getTruncateCgiParameter() ) 
      history += ", Truncate CGI Parameters";
    if ( CastParameter.getConvertToLowerCase() ) 
      history += ", Convert to Lower Case";
    history += Tools.getLineSeparator() + "Include List:" +
      Tools.getLineSeparator();
    if (CastParameter.getIncludeList().trim().length() == 0)
       history += "(empty)";
    else     
       history += CastParameter.getIncludeList().trim();
    history += Tools.getLineSeparator() + "Exclude List:" +
      Tools.getLineSeparator();
    if (CastParameter.getExcludeList().trim().length() == 0)
       history += "(empty)";
    else     
       history += CastParameter.getExcludeList().trim();
    history += Tools.getLineSeparator() + "Replace List:" +
      Tools.getLineSeparator();
    if (CastParameter.getReplaceList().trim().length() == 0)
       history += "(empty)";
    else     
       history += CastParameter.getReplaceList().trim();
    history += Tools.getLineSeparator() + "Taxonomy List:" +
      Tools.getLineSeparator();
    if (CastParameter.getTaxonomyList().trim().length() == 0)
       history += "(empty)";
    else     
       history += CastParameter.getTaxonomyList().trim();
    history += Tools.getLineSeparator() + "Results:" +
      Tools.getLineSeparator() + counterImported + 
      " log file lines were successfully imported." + 
      Tools.getLineSeparator() + counterSkippedDataCleaning + 
      " log file lines were skipped due to user's choice." +
      Tools.getLineSeparator() + counterSkippedSyntax + 
      " log file lines were skipped due to syntax errors.";
    if (WumProject.getMiningBase().countImportedAccessLogs() == 1)
      WumProject.getMiningBase().setImportLogFilesHistory(history);
    else {
      history += Tools.getLineSeparator() + Tools.getLineSeparator();
      WumProject.getMiningBase().appendImportLogFilesHistory(history); 
    }
    WumProject.getMiningBase().setCreateVisitorsSessionsHistory("");
    WumProject.getMiningBase().setCreateAggregatedLogHistory("");

    CastResult = new ImportLogFileResult(TaskResult.FINAL_RESULT,
    counterImported + 
    " log file lines were successfully imported.\n" +
    counterSkippedDataCleaning + 
    " log file lines were skipped due to user's choice.\n" +
    counterSkippedSyntax + 
    " log file lines were skipped due to syntax errors.", 
    counterImported + " log file lines were successfully imported.");
    this.setTaskResult(100, "Log File Imported ...", CastResult,
    TaskResult.FINAL_RESULT, Task.TASK_FINISHED);

  }
  else {  // log file not importable

    WumProject.getMiningBase().getImportedAccessLogs().delete(currentID);
 
    this.setErrorTaskResult(100, "Error: Log file cannot be imported!",
    "The specified access log lile cannot be imported.\n" +
    "Its first time stamp might be smaller than the\n" +
    "last time stamp of all imported log files or the\n" +
    "file might not conform to the specified format.");

  }

  AccessLogFile.close();
  WumProject.getMiningBase().getTraversalLog().close();
  WumProject.getMiningBase().createBackupFiles();  
        
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
   * creates a new traversal from CurrentAccessLogLine and stores
   * visitor, fromPage and toPage in the corresponding databases;
   * assumption in common and IIS log files: fromPage is previously accessed
   * toPage (=LastToPage); counts the total number of accesses of
   * each visitor, the number of referrals by each referrer pages and
   * the number of accesses of each web site page; CurrentAccessLogLine
   * must contain a valid object
   */
  
  private void importAccessLogLine() {
    
    long visitorID = 0L;
    long fromPageID = 0L;
    long toPageID = 0L;
  
    // use taxonomy items as URL if there are any
    CurrentAccessLogLine.setToPage( Taxonomy.getTaxonomyItem1( 
      CurrentAccessLogLine.getToPage() ) );
    CurrentPage = WumProject.getMiningBase().getWebSitePages().get( 
      CurrentAccessLogLine.getToPage() );
    if (CurrentPage == null) {  // ToPage doesn't exist yet
      CurrentPage = new Page( CurrentAccessLogLine.getToPage(), 1, 0 );
      toPageID = WumProject.getMiningBase().getWebSitePages().add(CurrentPage);    
    }
    else {  // ToPage already exists
      toPageID = CurrentPage.getID();
      CurrentPage.incrementAccesses();
      WumProject.getMiningBase().getWebSitePages().replace(CurrentPage);
    }
    
    if ( (CurrentAccessLog.getType() == AccessLog.WUMPREP_LOG_FILE_FORMAT) 
      && (CurrentAccessLogLine.getHost().indexOf("|") > 0) )  // session id
      CurrentVisitor = WumProject.getMiningBase().getWebSiteVisitors().get( 
        CurrentAccessLogLine.getHost().substring( 
        0, CurrentAccessLogLine.getHost().indexOf("|") ) );
    else
      CurrentVisitor = WumProject.getMiningBase().getWebSiteVisitors().get( 
        CurrentAccessLogLine.getHost() );
    if (CurrentVisitor == null) {  // Visitor doesn't exist yet
      if ( (CurrentAccessLog.getType() == AccessLog.WUMPREP_LOG_FILE_FORMAT )
        && (CurrentAccessLogLine.getHost().indexOf("|") > 0) )
        CurrentVisitor = new Visitor( 
          CurrentAccessLogLine.getHost().substring( 
            0, CurrentAccessLogLine.getHost().indexOf("|") ), 
          CurrentAccessLogLine.getRFC931(), 
          CurrentAccessLogLine.getAuthuser(), 
          CurrentAccessLogLine.getToPage(), 1, 
          CurrentAccessLogLine.getUserAgent(), 
          CurrentAccessLogLine.getCookie() );
      else
        CurrentVisitor = new Visitor( 
          CurrentAccessLogLine.getHost(), 
          CurrentAccessLogLine.getRFC931(), 
          CurrentAccessLogLine.getAuthuser(), 
          CurrentAccessLogLine.getToPage(), 1, 
          CurrentAccessLogLine.getUserAgent(), 
          CurrentAccessLogLine.getCookie() );
      visitorID = WumProject.getMiningBase().getWebSiteVisitors().add(CurrentVisitor);
    }
    else {  // Visitor already exists
      visitorID = CurrentVisitor.getID();
      CurrentVisitor.incrementAccesses();
      if ( CurrentAccessLog.getType() == AccessLog.COMMON_LOG_FILE_FORMAT ) {
        // replace empty FromPage by last ToPage in this case
        CurrentAccessLogLine.setFromPage( CurrentVisitor.getLastToPage() );
        CurrentVisitor.setLastToPage( CurrentAccessLogLine.getToPage() );
      }
      WumProject.getMiningBase().getWebSiteVisitors().replace(CurrentVisitor);
    }  
    
    CurrentPage = WumProject.getMiningBase().getReferrerPages().get( 
      CurrentAccessLogLine.getFromPage() );
    if (CurrentPage == null) {  // FromPage doesn't exist yet
      CurrentPage = new Page( CurrentAccessLogLine.getFromPage(), 0, 1 );
      fromPageID = WumProject.getMiningBase().getReferrerPages().add(CurrentPage);
    }
    else {    // FromPage already exists
      fromPageID = CurrentPage.getID();
      CurrentPage.incrementReferrals();
      WumProject.getMiningBase().getReferrerPages().replace(CurrentPage);
    }
    
    CurrentTraversal = new Traversal( 
      visitorID, CurrentAccessLogLine.getTimeStamp(),
      fromPageID, toPageID, CurrentAccessLog.getID() ); 
    
    // write current Traversal into TraversalLog
    WumProject.getMiningBase().getTraversalLog().setNextLine( 
      CurrentTraversal.toItemLine() );
    WumProject.getMiningBase().incrementImportedTraversals();
    
  }  // importAccessLogLine
  
  /**
   * checks whether the current AccessLog can be imported in the TraversalLog
   * by comparing ImportedAccessLogs.getLastTimeStamp() with the AccessLog's
   * first timestamp
   */
  
  private void checkAccessLog() {
  
    // works on CurrentAccessLogLine, vCheckAccessLog and ValidAccessLog

    CheckedAccessLog = true;

    if ( WumProject.getMiningBase().getImportedAccessLogs().getLastTimeStamp()
      .equals("") )
      ValidAccessLog = true;
    else
      if ( CurrentAccessLogLine.getTimeStamp().compareTo( 
        WumProject.getMiningBase().getImportedAccessLogs().getLastTimeStamp() ) < 0)  
        ValidAccessLog = false;
      else
        ValidAccessLog = true;

  }  // checkAccessLog()
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}