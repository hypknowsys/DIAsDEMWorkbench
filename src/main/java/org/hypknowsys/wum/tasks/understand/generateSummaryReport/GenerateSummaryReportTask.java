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

import java.io.*;
import java.util.*;
import java.net.*;
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

public class GenerateSummaryReportTask extends WumScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private GenerateSummaryReportParameter CastParameter = null;
  private GenerateSummaryReportResult CastResult = null;
  
  private TreeMap TopPages = null;
  private TreeMap FlopPages = null;
  private TreeMap TopVisitors = null;
  private Thesaurus TopEntryPages = null;
  private Thesaurus TopExitPages = null;
  private Thesaurus TopReferrerPages = null;
  private Thesaurus TopDirectories = null;
  private Thesaurus SingleAccessPages = null;
  private Thesaurus TopUserAgents = null;
  private Thesaurus TopReferrerSites = null;
  private Thesaurus TopNations = null;
  private Thesaurus TopOrganizations = null;
  
  private TextFile ReportFile = null;
  private int ReportRange = 0;
  
  private Iterator ReportIterator = null;
  private String CurrentLine = null;
  private Session CurrentSession = null;
  private Trail CurrentTrail = null;
  
  private int[] TopPagesOrdering =
  {MintOrderByKey.DESC, MintOrderByKey.ASC};
  private int[] FlopPagesOrdering =
  {MintOrderByKey.ASC, MintOrderByKey.ASC};
  private int[] TopVisitorsOrdering =
  {MintOrderByKey.DESC, MintOrderByKey.ASC};
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Generate Summary Report";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.wum.tasks.understand.generateSummaryReport"
  + ".GenerateSummaryReportParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.wum.tasks.understand.generateSummaryReport"
  + ".GenerateSummaryReportResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.understand.generateSummaryReport"
  + ".GenerateSummaryReportControlPanel";
  
  private int MIN_REQUIRED_MINING_BASE_STATUS = MiningBase
  .MINING_BASE_IS_OPEN_AND_SESSIONS_ARE_AGGREGATED;
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_REPORT_FILENAME",
    "Generate Summary Report: Default Report Filename",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_REQUESTED_PAGES",
    "Generate Summary Report: Default Most Requested Pages",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_REQUESTED_DIRECTORIES",
    "Generate Summary Report: Default Most Requested Directories",
    "10", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_LEAST_REQUESTED_PAGES",
    "Generate Summary Report: Default Least Requested Pages",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_LEAST_REQUESTED_DIRECTORIES",
    "Generate Summary Report: Default Least Requested Directories",
    "10", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_ENTRY_PAGES",
    "Generate Summary Report: Default Top Entry Pages",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_EXIT_PAGES",
    "Generate Summary Report: Default Top Exit Pages",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_SINGLE_ACCESS_PAGES",
    "Generate Summary Report: Default Single Access Pages",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_ACTIVE_TOP_LEVEL_DOMAINS",
    "Generate Summary Report: Default Most Active Top-Level Domains",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_ACTIVE_2ND_LEVEL_DOMAINS",
    "Generate Summary Report: Default Most Active 2nd-Level Domains",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_VISITORS",
    "Generate Summary Report: Default Top Visitors",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_REFERRER_SITES",
    "Generate Summary Report: Default Top Referrer Sites",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_REFERRER_PAGES",
    "Generate Summary Report: Default Top Referrer Pages",
    "20", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty(
    "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_USED_BROWSERS",
    "Generate Summary Report: Default Most Used Browsers",
    "10", KProperty.INTEGER, KProperty.NOT_EDITABLE),
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public GenerateSummaryReportTask() {
    
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
    
    GenerateSummaryReportParameter parameter = null;
    if (pParameter instanceof GenerateSummaryReportParameter) {
      parameter = (GenerateSummaryReportParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    if (!(pProject instanceof WUMproject && ( (WUMproject)pProject )
    .getMiningBaseStatus() >= MinRequiredMiningBaseStatus)) {
      result.addError(
      "Error: The project's mining base does not have the\n" +
      "minimum status code required by this task! Prior to\n" +
      "report generation, at least one log file must have\n" +
      "been imported, log files have have been sessionized,\n" +
      "and sessions must have been aggregated.");
    }
    
    if (parameter.getReportFilename().trim().length() <= 0
    || !parameter.getReportFilename().trim().endsWith(
    WUMguiPreferences.HTML_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      WUMguiPreferences.HTML_FILE_EXTENSION +
      "-file name\nin the field 'Report File Name'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new GenerateSummaryReportParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new GenerateSummaryReportResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.wum.client.gui.WUMguiMenuBar.ACTIONS_UNDERSTAND_DOMAIN,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof GenerateSummaryReportParameter) {
      CastParameter = (GenerateSummaryReportParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    String shortErrorMessage = "Error: Summary report cannot be generated!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    
    TopPages = new TreeMap( new MintOrderByKey() );
    FlopPages = new TreeMap( new MintOrderByKey() );
    TopVisitors = new TreeMap( new MintOrderByKey() );
    TopEntryPages = new Thesaurus();
    TopExitPages = new Thesaurus();
    TopReferrerPages = new Thesaurus();
    TopDirectories = new Thesaurus();
    TopUserAgents = new Thesaurus();
    SingleAccessPages = new Thesaurus();
    TopReferrerSites = new Thesaurus();
    TopNations = new Thesaurus();
    TopOrganizations = new Thesaurus();
    
    WumServer.setTaskStatus(Task.TASK_ACCEPTED);
    Progress = new AbstractTaskProgress(0, "Current Web Site Page 0");
    WumServer.setTaskProgress(Progress, TaskThread);
    Result = new AbstractTaskResult(TaskResult.NO_RESULT, "");
    WumServer.setTaskResult(Result);
    
    int countWebSitePages = WumProject.getMiningBase().getWebSitePages().countAll();
    int countReferrerPages = WumProject.getMiningBase().getReferrerPages().countAll();
    int countWebSiteVisitors =
    WumProject.getMiningBase().getWebSiteVisitors().countAll();
    int countSessions = WumProject.getMiningBase().getImportedSessions();
    int sumAccesses = 0;
    int sumSessionLength = 0;
    int countSingleAccessPages = 0;
    
    int vMaximumProgress = countWebSitePages + countWebSiteVisitors +
    countSessions;
    int vAccumulatedProgress = 1;
    int vCurrentProgress = 1;
    
    Page currentPage = WumProject.getMiningBase().getWebSitePages().getFirstByID();
    while (currentPage != null) {
      if ( (vCurrentProgress == 1) || ((vCurrentProgress % 50) == 0) ) {
        Progress.update(
        (int)(vAccumulatedProgress * 100 / vMaximumProgress),
        "Current Web Site Page " + vCurrentProgress);
        WumServer.setTaskProgress(Progress, TaskThread);
      }
      Comparable[] SortKeys = new Comparable[2];
      SortKeys[0] = new Integer( currentPage.getAccesses() );
      SortKeys[1] = currentPage.getUrl();
      MintOrderByKey SortKey =
      new MintOrderByKey(SortKeys, TopPagesOrdering);
      TopPages.put(SortKey, currentPage);
      MintOrderByKey SortKey2 =
      new MintOrderByKey(SortKeys, FlopPagesOrdering);
      FlopPages.put(SortKey2, currentPage);
      TopDirectories.countOccurrence(
      this.getDirectory( currentPage.getUrl() ),
      currentPage.getAccesses() );
      sumAccesses += currentPage.getAccesses();
      vAccumulatedProgress++;
      vCurrentProgress++;
      currentPage = WumProject.getMiningBase().getWebSitePages().getNextByID();
    }
    
    vCurrentProgress = 1;
    Visitor currentVisitor =
    WumProject.getMiningBase().getWebSiteVisitors().getFirstByID();
    while (currentVisitor != null) {
      if ( (vCurrentProgress == 1) || ((vCurrentProgress % 50) == 0) ) {
        Progress.update(
        (int)(vAccumulatedProgress * 100 / vMaximumProgress),
        "Current Web Site Visitor " + vCurrentProgress);
        WumServer.setTaskProgress(Progress, TaskThread);
      }
      Comparable[] SortKeys = new Comparable[2];
      SortKeys[0] = new Integer( currentVisitor.getAccesses() );
      SortKeys[1] = currentVisitor.getHost();
      MintOrderByKey SortKey =
      new MintOrderByKey(SortKeys, TopVisitorsOrdering);
      TopVisitors.put(SortKey, currentVisitor);
      vAccumulatedProgress++;
      vCurrentProgress++;
      currentVisitor = WumProject.getMiningBase().getWebSiteVisitors().getNextByID();
    }
    
    int lastDotIndex = 0;
    int lastButFirstDotIndex = 0;
    vCurrentProgress = 1;
    WumProject.getMiningBase().setSessionLog( new TextFile( new File(
    WumProject.getMiningBase().getMiningBaseDirectory() +
    MiningBase.SESSION_LOG_FILE ) ) );
    WumProject.getMiningBase().getSessionLog().open();
    String vSessionLine = WumProject.getMiningBase().getSessionLog().getFirstLine();
    while (vSessionLine != null) {
      if ( (vCurrentProgress == 1) || ((vCurrentProgress % 50) == 0) ) {
        Progress.update(
        (int)(vAccumulatedProgress * 100 / vMaximumProgress),
        "Current Session Log Line " + vCurrentProgress);
        WumServer.setTaskProgress(Progress, TaskThread);
      }
      CurrentSession = new Session();
      CurrentSession.fromItemLine(vSessionLine);
      if (CurrentSession.isValid() ) {
        CurrentTrail = CurrentSession.getTrail();
        // without referrer URL
        sumSessionLength += CurrentTrail.getSize() - 1;
        TopReferrerPages.countOccurrence( WumProject.getMiningBase().getPage(
        CurrentTrail.getFirstPageOccurrence().getPageID() ).getUrl() );
        TopReferrerSites.countOccurrence( this.getSite(
        WumProject.getMiningBase().getPage(
        CurrentTrail.getFirstPageOccurrence().getPageID() ).getUrl() ) );
        TopEntryPages.countOccurrence( WumProject.getMiningBase().getPage(
        CurrentTrail.getNextPageOccurrence().getPageID() ).getUrl() );
        int vExitPageID = CurrentTrail.getSize() - 1;
        if (vExitPageID >= 0)
          TopExitPages.countOccurrence(WumProject.getMiningBase().getPage(
          CurrentTrail.getPageOccurrence(vExitPageID).getPageID() )
          .getUrl() );
        if (vExitPageID == 1) {
          SingleAccessPages.countOccurrence(WumProject.getMiningBase().getPage(
          CurrentTrail.getPageOccurrence(vExitPageID).getPageID() )
          .getUrl() );
          countSingleAccessPages++;
        }
        currentVisitor = WumProject.getMiningBase().getWebSiteVisitors().get(
        CurrentSession.getVisitorID() );
        TopUserAgents.countOccurrence( currentVisitor.getUserAgent() );
        if ( this.isDnsName( currentVisitor.getHost() ) ) {
          lastDotIndex = currentVisitor.getHost().lastIndexOf(".");
          lastButFirstDotIndex =
          currentVisitor.getHost().lastIndexOf(".", lastDotIndex - 1);
          TopNations.countOccurrence( currentVisitor.getHost().substring(
          lastDotIndex + 1 ) );
          TopOrganizations.countOccurrence(currentVisitor.getHost().substring(
          lastButFirstDotIndex + 1) );
        }
        else {
          TopNations.countOccurrence("unknown");
          TopOrganizations.countOccurrence("unknown");
        }
      }
      vAccumulatedProgress++;
      vCurrentProgress++;
      vSessionLine = WumProject.getMiningBase().getSessionLog().getNextLine();
    }  // while
    WumProject.getMiningBase().getSessionLog().close();
    
    
    ReportFile = new TextFile(new File(CastParameter.getReportFilename()));
    ReportFile.open();
    Template outputHeader = new Template(Tools
    .stringFromTextualSystemResource("org/hypknowsys/wum/resources/html/"
    + "HtmlFile_HeaderTemplate.html"));
    outputHeader.addValue("${Title}", "Comprehensive Summary Report");
    ReportFile.setFirstLine(outputHeader.insertValues());
    ReportFile.setNextLine("<p>Created by Tasks &gt; Understand Domain " +
    "&gt; Generate Summary Report on " + Tools.getSystemDate() +
    "</p>");
    ReportFile.setNextLine("<h3>Table of Contents</h3>");
    ReportFile.setNextLine( 
    "Mining Base: " + WumProject.getMiningBase().getName() + " (Web Server: " +
    this.createHyperlink( WumProject.getMiningBase().getServer(),
    WumProject.getMiningBase().getServer() )+ ") <p>" + "Report Range: " +
    WumProject.getMiningBase().getImportedAccessLogs().getFirstTimeStamp() + " - " +
    WumProject.getMiningBase().getImportedAccessLogs().getLastTimeStamp() + " (" +
    this.setReportRange(
    WumProject.getMiningBase().getImportedAccessLogs().getFirstTimeStamp(),
    WumProject.getMiningBase().getImportedAccessLogs().getLastTimeStamp() ) +
    " Days)<br>" + "Mining Base Directory: " +
    WumProject.getMiningBase().getMiningBaseDirectory() + "<br>" +
    "Log File Directory: " + WumProject.getMiningBase().getLogFileDirectory());    
    ReportFile.setNextLine("<p><a href=\"#Report1\">General Statistics</a> ");
    if (CastParameter.getNumberOfMostRequestedPages() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report2\">Most Requested Pages</a>");
    }
    if (CastParameter.getNumberOfMostRequestedDirectories() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report3\">Most Requested Directories</a>");
    }
    if (CastParameter.getNumberOfLeastRequestedPages() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report4\">Least Requested Pages</a>");
    }
    if (CastParameter.getNumberOfLeastRequestedDirectories() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report5\">Least Requested Directories</a>");
    }
    if (CastParameter.getNumberOfTopVisitors() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report6\">Top Visitors</a>");
    }
    if (CastParameter.getNumberOfMostActiveTopLevelDomains() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report7\">Most Active Top-Level Domains</a>");
    }
    if (CastParameter.getNumberOfMostActiveSecondLevelDomains() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report8\">Most Active Second-Level Domains</a>");
    }
    if (CastParameter.getNumberOfTopEntryPages() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report9\">Top Entry Pages</a>");
    }
    if (CastParameter.getNumberOfTopExitPages() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report10\">Top Exit Pages</a>");
    }
    if (CastParameter.getNumberOfTopReferrerPages() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report11\">Top Referrer Pages</a>");
    }
    if (CastParameter.getNumberOfTopReferrerSites() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report12\">Top Referrer Sites</a>");
    }
    if (CastParameter.getNumberOfMostUsedBrowsers() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report13\">Most Used Browsers</a>");
    }
    if (CastParameter.getNumberOfSingleAccessPages() > 0) {
      ReportFile.setNextLine("- <a href=\"#Report14\">Single Access Pages</a>");
    }
    ReportFile.setNextLine("</p>");
    
    ReportFile.setNextLine( this.htmlHeading("Report1", "General Statistics") );
    ReportFile.setNextLine( this.htmlTableHeading("No.", "Attribute",
    "Value", "&nbsp;") );
    ReportFile.setNextLine( this.htmlTableData(
    "1.","Page Accesses (Page Views)", sumAccesses + "", "&nbsp;") );
    ReportFile.setNextLine( this.htmlTableData(
    "2.","Average Page Accesses per Day", (ReportRange == 0 ? "N/A" :
      new String( this.getRatio(sumAccesses, ReportRange) ) ) + "",
      "&nbsp;") );
      ReportFile.setNextLine( this.htmlTableData(
      "3.", "Visitor Sessions ", countSessions + "", "&nbsp;") );
      ReportFile.setNextLine( this.htmlTableData(
      "4.","Average Visitor Sessions per Day", (ReportRange == 0 ? "N/A" :
        new String( this.getRatio(countSessions, ReportRange) ) ) + "", "&nbsp;") );
        ReportFile.setNextLine( this.htmlTableData(
        "5.", "Average Visitor Session Length ",
        this.getRatio(sumSessionLength, countSessions) + "", "&nbsp;") );
        ReportFile.setNextLine( this.htmlTableData(
        "6.", "Unique Visitors ", countWebSiteVisitors + "", "&nbsp;") );
        ReportFile.setNextLine( this.htmlTableData(
        "7.", "Average Sessions per Visitor ",
        this.getRatio(countSessions, countWebSiteVisitors) + "", "&nbsp;") );
        ReportFile.setNextLine( this.htmlTableData(
        "8.", "Web Site Pages ", countWebSitePages + "", "&nbsp;") );
        ReportFile.setNextLine( this.htmlTableData(
        "9.", "Referrer Pages ", countReferrerPages + "", "&nbsp;") );
        ReportFile.setNextLine( this.htmlTableData(
        "10.", "Observations in Aggregated Log ",
        WumProject.getMiningBase().getAggregatedLog().countObservations() + "",
        "&nbsp;") );
        ReportFile.setNextLine( this.htmlTableData(
        "11.", "Support of Root Node in Aggregated Log ",
        WumProject.getMiningBase().getAggregatedLog().getRootSupport() + "",
        "&nbsp;") );
        ReportFile.setNextLine( this.htmlTableData(
        "12.", "Children of Root Node in Aggregated Log ",
        WumProject.getMiningBase().getAggregatedLog().getRootCountChildren() + "",
        "&nbsp;") );
        ReportFile.setNextLine( this.htmlEnding() );
        
        int counter = 0;
        int sumSection = 0;
        
        if ( CastParameter.getNumberOfMostRequestedPages() > 0) {
          ReportFile.setNextLine( this.htmlHeading("Report2",
          "Most Requested Pages") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "URL",
          "Accesses", "Percentage") );
          ReportIterator = TopPages.values().iterator();
          while ( ( ReportIterator.hasNext() ) &&
          ( counter < CastParameter.getNumberOfMostRequestedPages() )  ) {
            currentPage = (Page)ReportIterator.next();
            sumSection += currentPage.getAccesses();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", this.createHyperlink(
            WumProject.getMiningBase().getServer() +
            currentPage.getUrl(), currentPage.getUrl() ),
            currentPage.getAccesses() + "",
            this.getPercentage(currentPage.getAccesses(), sumAccesses)) );
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Pages above", sumSection + "",
          this.getPercentage(sumSection, sumAccesses)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", sumAccesses + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        ThesaurusTerm currentTerm = null;
        
        if ( CastParameter.getNumberOfMostRequestedDirectories() > 0 ) {
          ReportFile.setNextLine( this.htmlHeading("Report3",
          "Most Requested Directories") );
          // ReportFile.setNextLine(
          //  this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "Directory",
          "Accesses", "Percentage") );
          counter = 0;
          sumSection = 0;
          TopDirectories.setOrderOccurrencesWordsDesc();
          currentTerm = TopDirectories.getFirstTerm();
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfMostRequestedDirectories() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", this.createHyperlink(
            WumProject.getMiningBase().getServer() +
            currentTerm.getWord(), currentTerm.getWord() ),
            currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), sumAccesses)) );
            currentTerm = TopDirectories.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Directories above", sumSection + "",
          this.getPercentage(sumSection, sumAccesses)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", sumAccesses + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfLeastRequestedPages() > 0 ) {
          ReportFile.setNextLine(
          this.htmlHeading("Report4", "Least Requested Pages") );
          // ReportFile.setNextLine(
          //  this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine(
          this.htmlTableHeading("No.", "URL", "Accesses", "Percentage") );
          ReportIterator = FlopPages.values().iterator();
          counter = 0;
          sumSection = 0;
          while ( ( ReportIterator.hasNext() ) &&
          ( counter < CastParameter.getNumberOfLeastRequestedPages() )  ) {
            currentPage = (Page)ReportIterator.next();
            sumSection += currentPage.getAccesses();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", this.createHyperlink(
            WumProject.getMiningBase().getServer() +
            currentPage.getUrl(), currentPage.getUrl() ),
            currentPage.getAccesses() + "",
            this.getPercentage(currentPage.getAccesses(), sumAccesses)) );
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Pages above", sumSection + "",
          this.getPercentage(sumSection, sumAccesses)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", sumAccesses + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfLeastRequestedDirectories() > 0 ) {
          ReportFile.setNextLine(
          this.htmlHeading("Report5", "Least Requested Directories") );
          // ReportFile.setNextLine(
          //  this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine(
          this.htmlTableHeading("No.", "Directory", "Accesses", "Percentage") );
          counter = 0;
          sumSection = 0;
          TopDirectories.setOrderOccurrencesWordsAsc();
          currentTerm = TopDirectories.getFirstTerm();
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfLeastRequestedDirectories() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", this.createHyperlink(
            WumProject.getMiningBase().getServer() +
            currentTerm.getWord(), currentTerm.getWord() ),
            currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), sumAccesses)) );
            currentTerm = TopDirectories.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Directories above", sumSection + "",
          this.getPercentage(sumSection, sumAccesses)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", sumAccesses + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfTopVisitors() > 0 ) {
          ReportFile.setNextLine( this.htmlHeading("Report6", "Top Visitors") );
          // ReportFile.setNextLine(
          //  this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.
          htmlTableHeading("No.", "Visitor", "Accesses", "Percentage") );
          ReportIterator = TopVisitors.values().iterator();
          counter = 0;
          sumSection = 0;
          while ( ( ReportIterator.hasNext() ) &&
          ( counter < CastParameter.getNumberOfTopVisitors() )  ) {
            currentVisitor = (Visitor)ReportIterator.next();
            sumSection += currentVisitor.getAccesses();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", currentVisitor.getHost() + " " +
            currentVisitor.getAuthUser() + " " + currentVisitor.getRFC931(),
            currentVisitor.getAccesses() + "",
            this.getPercentage(currentVisitor.getAccesses(), sumAccesses)) );
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Visitors above", sumSection + "",
          this.getPercentage(sumSection, sumAccesses)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", sumAccesses + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfMostActiveTopLevelDomains() > 0 ) {
          ReportFile.setNextLine(
          this.htmlHeading("Report7", "Most Active Top-Level Domains") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "Top-Level Domain",
          "Sessions", "Percentage") );
          TopNations.setOrderOccurrencesWordsDesc();
          currentTerm = TopNations.getFirstTerm();
          counter = 0;
          sumSection = 0;
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfMostActiveTopLevelDomains() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", currentTerm.getWord(),
            currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), countSessions)) );
            currentTerm = TopNations.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Top-Level Domains above", sumSection + "",
          this.getPercentage(sumSection, countSessions)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", countSessions + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfMostActiveSecondLevelDomains() > 0 ) {
          ReportFile.setNextLine(
          this.htmlHeading("Report8", "Most Second-Level Domains") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.",
          "Second-Level Domain", "Sessions", "Percentage") );
          TopOrganizations.setOrderOccurrencesWordsDesc();
          currentTerm = TopOrganizations.getFirstTerm();
          counter = 0;
          sumSection = 0;
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfMostActiveSecondLevelDomains() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", currentTerm.getWord(),
            currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), countSessions)) );
            currentTerm = TopOrganizations.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Second-Level Domains above",
          sumSection + "",
          this.getPercentage(sumSection, countSessions)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", countSessions + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfTopEntryPages() > 0 ) {
          ReportFile.setNextLine( this.htmlHeading("Report9", "Top Entry Pages") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "URL",
          "Sessions", "Percentage") );
          TopEntryPages.setOrderOccurrencesWordsDesc();
          currentTerm = TopEntryPages.getFirstTerm();
          counter = 0;
          sumSection = 0;
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfTopEntryPages() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData( (counter+1) + ".",
            this.createHyperlink( WumProject.getMiningBase().getServer() +
            currentTerm.getWord(), currentTerm.getWord() ),
            currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), countSessions)) );
            currentTerm = TopEntryPages.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Pages above", sumSection + "",
          this.getPercentage(sumSection, countSessions)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", countSessions + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfTopExitPages() > 0 ) {
          ReportFile.setNextLine( this.htmlHeading("Report10", "Top Exit Pages") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "URL", "Sessions",
          "Percentage") );
          TopExitPages.setOrderOccurrencesWordsDesc();
          currentTerm = TopExitPages.getFirstTerm();
          counter = 0;
          sumSection = 0;
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfTopExitPages() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData( (counter+1) + ".",
            this.createHyperlink( WumProject.getMiningBase().getServer() +
            currentTerm.getWord(), currentTerm.getWord() ),
            currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), countSessions)) );
            currentTerm = TopExitPages.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Pages above", sumSection + "",
          this.getPercentage(sumSection, countSessions)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", countSessions + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfTopReferrerPages() > 0 ) {
          ReportFile.setNextLine(
          this.htmlHeading("Report11", "Top Referrer Pages") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "URL", "Sessions",
          "Percentage") );
          TopReferrerPages.setOrderOccurrencesWordsDesc();
          currentTerm = TopReferrerPages.getFirstTerm();
          counter = 0;
          sumSection = 0;
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfTopReferrerPages() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", this.createHyperlink( currentTerm.getWord(),
            currentTerm.getWord() ), currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), countSessions)) );
            currentTerm = TopReferrerPages.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Pages above", sumSection + "",
          this.getPercentage(sumSection, countSessions)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", countSessions + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfTopReferrerSites() > 0 ) {
          ReportFile.setNextLine(
          this.htmlHeading("Report12", "Top Referrer Sites") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "URL",
          "Sessions", "Percentage") );
          TopReferrerSites.setOrderOccurrencesWordsDesc();
          currentTerm = TopReferrerSites.getFirstTerm();
          counter = 0;
          sumSection = 0;
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfTopReferrerSites() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", this.createHyperlink( currentTerm.getWord(),
            currentTerm.getWord() ), currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), countSessions)) );
            currentTerm = TopReferrerSites.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Sites above", sumSection + "",
          this.getPercentage(sumSection, countSessions)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", countSessions + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfMostUsedBrowsers() > 0 ) {
          ReportFile.setNextLine(
          this.htmlHeading("Report13", "Most Used Browsers") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "Browser",
          "Sessions", "Percentage") );
          TopUserAgents.setOrderOccurrencesWordsDesc();
          currentTerm = TopUserAgents.getFirstTerm();
          counter = 0;
          sumSection = 0;
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfMostUsedBrowsers() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData(
            (counter+1) + ".", currentTerm.getWord(),
            currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(), countSessions)) );
            currentTerm = TopUserAgents.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Subtotal for the Browsers above", sumSection + "",
          this.getPercentage(sumSection, countSessions)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Imported Log Files", countSessions + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        if ( CastParameter.getNumberOfSingleAccessPages() > 0 ) {
          ReportFile.setNextLine(
          this.htmlHeading("Report14", "Single Access Pages") );
          // ReportFile.setNextLine(
          //   this.htmlExplanation("Explanation must be written!") );
          ReportFile.setNextLine( this.htmlTableHeading("No.", "URL",
          "Accesses", "Percentage") );
          SingleAccessPages.setOrderOccurrencesWordsDesc();
          currentTerm = SingleAccessPages.getFirstTerm();
          counter = 0;
          sumSection = 0;
          while ( ( currentTerm != null ) &&
          ( counter < CastParameter.getNumberOfSingleAccessPages() )  ) {
            sumSection += currentTerm.getOccurrences();
            ReportFile.setNextLine( this.htmlTableData( (counter+1) + ".",
            this.createHyperlink( WumProject.getMiningBase().getServer() +
            currentTerm.getWord(), currentTerm.getWord() ),
            currentTerm.getOccurrences() + "",
            this.getPercentage(currentTerm.getOccurrences(),
            countSingleAccessPages)) );
            currentTerm = SingleAccessPages.getNextTerm();
            counter++;
          }
          ReportFile.setNextLine( this.htmlTableHeading( "&nbsp;",
          "Subtotal for the Single Access Pages above", sumSection + "",
          this.getPercentage(sumSection, countSingleAccessPages)) );
          ReportFile.setNextLine( this.htmlTableHeading(
          "&nbsp;", "Total for all Single Access Pages",
          countSingleAccessPages + "",
          "100.00 %") );
          ReportFile.setNextLine( this.htmlEnding() );
        }
        
        
        Template outputFooter = new Template(Tools
        .stringFromTextualSystemResource("org/hypknowsys/wum/resources/html/"
        + "HtmlFile_FooterTemplate.html"));
        ReportFile.setNextLine(outputFooter.insertValues());
        ReportFile.close();
        
        //    int counterProgress = 0;
        //    long maxProgress = 100;
        //
        //    while (counterProgress < 100) {
        //
        //      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        //        Progress.update( (int)(counterProgress * 100 / maxProgress),
        //        "Processing Item " + counterProgress);
        //        WumServer.setTaskProgress(Progress, TaskThread);
        //      }
        //
        //      counterProgress++;
        //
        //    }  // do something
        //
        CastResult = new GenerateSummaryReportResult(TaskResult.FINAL_RESULT,
        "The report was successfully generated and saved as\n" +
        CastParameter.getReportFilename() + ".",
        "The report was successfully generated.");
        this.setTaskResult(100, "All Items Processed ...", CastResult,
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
  
  private String getDirectory(String pUrl) {
    
    int lastSlashPosition = pUrl.lastIndexOf("/");
    if (lastSlashPosition >= 0)
      return pUrl.substring(0, lastSlashPosition) + "/";
    else
      return "/";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getPercentage(int Part, int Basis) {
    
    int value1 = (int)( ( (double)Part / Basis) * 10000.0);
    int value2 = value1 / 100;
    int value3 = value1 % 100;
    String result = ( new Integer(value2) ).toString() + ".";
    if (value3 < 10)
      if (value3 == 0)
        result += "00";
      else
        result += "0" + ( new Integer(value3) ).toString();
    else
      result += ( new Integer(value3) ).toString();
    
    return result + " %";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getRatio(int Part, int Basis) {
    
    int value1 = (int)( ( (double)Part / Basis) * 100.0);
    int value2 = value1 / 100;
    int value3 = value1 % 100;
    String result = ( new Integer(value2) ).toString() + ".";
    if (value3 < 10)
      if (value3 == 0)
        result += "00";
      else
        result += "0" + ( new Integer(value3) ).toString();
    else
      result += ( new Integer(value3) ).toString();
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String setReportRange(String pFirstTimeStamp,
  String pLastTimeStamp) {
    
    String vReportRange = TimeStamp.TimeStampDifference(
    pFirstTimeStamp, pLastTimeStamp );
    if (vReportRange != null) {
      vReportRange = vReportRange.substring(
      0, vReportRange.indexOf("/") );
      try {
        ReportRange = ( new Integer(vReportRange) ).intValue();
      }
      catch (NumberFormatException e) {
        ReportRange = 0;
      }
      return vReportRange;
    }
    else {
      ReportRange = 0;
      return "-";
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getSite(String pUrl) {
    
    URL oUrl = null;
    String vSite = null;
    try {
      oUrl = new URL(pUrl);
      vSite = oUrl.getProtocol() + "://" + oUrl.getHost();
    }
    catch (MalformedURLException e) {
      vSite = "unknown";
    }
    
    return vSite;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String createHyperlink(String pUrl, String pContent) {
    
    URL oUrl = null;
    String vHyperlink = null;
    try {
      oUrl = new URL(pUrl);
      vHyperlink = "<a href=\"" + pUrl + "\" target=\"_new\">" +
      pContent + "</a>";
    }
    catch (MalformedURLException e) {
      vHyperlink = pUrl;
    }
    
    return vHyperlink;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isDnsName(String pHost) {
    
    int pLastDotIndex = pHost.lastIndexOf(".");
    if ( (pLastDotIndex < 0) || (pLastDotIndex == (pHost.length() - 1) ) )
      return false;
    String pTopLevelDomain = pHost.substring(pLastDotIndex + 1);
    if ( pTopLevelDomain.toLowerCase().equals(
    pTopLevelDomain.toUpperCase() ) )
      return false;
    else
      return true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getLineSeparator() {
    
    return System.getProperty("line.separator");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String htmlHeading(String pName, String pHeading) {
    
    String result = "<h3><a name=\"" + pName + "\">" + pHeading + "</a>" +
    "</h3>" + this.getLineSeparator() +
    "<p><small><a href=\"#TOP\">Top of the Page</a></small></p>" +
    this.getLineSeparator() + "<table border=\"1\" width=\"100%\">";
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String htmlEnding() {
    
    String result = "</table>" + this.getLineSeparator();
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String htmlTableHeading(String pItem1, String pItem2,
  String pItem3, String pItem4) {
    
    String result = "  <tr>" +
    this.getLineSeparator() + "    <th nowrap width=\"5%\" " +
    "valign=\"top\" align=\"right\" class=\"TableHeading\">" +
    this.getLineSeparator() + "      " + pItem1 +
    this.getLineSeparator() + "    </th>" +
    this.getLineSeparator() + "    <th nowrap width=\"65%\" " +
    "valign=\"top\" align=\"left\" class=\"TableHeading\">" +
    this.getLineSeparator() + "      " + pItem2 +
    this.getLineSeparator() + "    </th>" +
    this.getLineSeparator() + "    <th nowrap width=\"15%\" " +
    "valign=\"top\" align=\"right\" class=\"TableHeading\">" +
    this.getLineSeparator() + "      " + pItem3 +
    this.getLineSeparator() + "    </th>" +
    this.getLineSeparator() + "    <th nowrap width=\"15%\" " +
    "valign=\"top\" align=\"right\" class=\"TableHeading\">" +
    this.getLineSeparator() + "      " + pItem4 +
    this.getLineSeparator() + "    </th>" +
    this.getLineSeparator() + "  </tr>";
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String htmlTableData(String pItem1, String pItem2, String pItem3,
  String pItem4) {
    
    String result = "  <tr>" +
    this.getLineSeparator() + "    <td nowrap width=\"5%\" " +
    "valign=\"top\" align=\"right\">" +
    this.getLineSeparator() + "      " + pItem1 +
    this.getLineSeparator() + "    </td>" +
    this.getLineSeparator() + "    <td nowrap width=\"65%\" " +
    "valign=\"top\" align=\"left\">" +
    this.getLineSeparator() + "      " + pItem2 +
    this.getLineSeparator() + "    </td>" +
    this.getLineSeparator() + "    <td nowrap width=\"15%\" " +
    "valign=\"top\" align=\"right\">" +
    this.getLineSeparator() + "      " + pItem3 +
    this.getLineSeparator() + "    </td>" +
    this.getLineSeparator() + "    <td nowrap width=\"15%\" " +
    "valign=\"top\" align=\"right\">" +
    this.getLineSeparator() + "      " + pItem4 +
    this.getLineSeparator() + "    </td>" +
    this.getLineSeparator() + "  </tr>";
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}