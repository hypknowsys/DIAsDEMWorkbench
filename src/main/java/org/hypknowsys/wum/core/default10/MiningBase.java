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

package org.hypknowsys.wum.core.default10;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;

/**
 * A MiningBase is one main class in WUM. It contains all classes that 
 * belong to one special mining project. Attributes: 
 * MiningBaseSiteInfo containing meta-information about this web site, 
 * ImportedAccessLogs, TraversalLog and SessionLog (text files), 
 * AggregatedLog, ReferrerPages and WebSitePages, WebSiteVisitors. There 
 * are also two counters (ImportedTraversals, ImportedSessions) and a status
 * field memorizing the execution progress within this MiningBase. The public
 * constants contain information about file names and ID ranges.<p>
 *
 * 2/99: public transient TransientAggregateTree TmpAggregatedLog added to 
 * use another Aggregated Log that is not persistently stored in ObjectStore 
 * PSE <p>
 *
 * Modified by tveit 5/99: attributes added, CurrentHeap, removed by kwinkler
 * 11/2001 <p>
 *
 * Modified by kwinkler 7/2000; parameter pRemarks added in constructor
 * and filenames changed from 'Database.*' to 'MiningBase.*' <p>
 *
 * Modified by kwinkler 8/2000: attributes ImportLogfile, 
 * CreateSessionsHistory and CreateAggregatedLogHistory added <p>
 *
 * @version 0.9.0, 20 May 2004
 * @author Karsten Winkler
 */

public class MiningBase implements Serializable {

  // ########## attributes and constants ##########

  private MiningBaseInfo MiningBaseInfo = null;
  
  private AccessLogs ImportedAccessLogs = null;
  private transient TextFile TraversalLog = null;
  private transient TextFile SessionLog = null;
  private AggregateTree AggregatedLog = null;
  
  private Pages ReferrerPages = null;
  private Pages WebSitePages = null;
  private Visitors WebSiteVisitors = null;

  private int ImportedTraversals = 0;
  private int ImportedSessions = 0;    
  private int Status = 0;

  private String ImportLogFilesHistory = null;
  private String CreateSessionsHistory = null;
  private String CreateAggregatedLogHistory = null;
  
  public final static String TRAVERSAL_LOG_FILE = "WUM.TraversalLog.txt";
  public final static String SESSION_LOG_FILE = "WUM.SessionLog.txt";

  public final static String BACKUP_WEB_SITE_PAGES = 
    "WUM.WebSitePages.bak";
  public final static String BACKUP_REFERRER_PAGES = 
    "WUM.ReferrerPages.bak";
  public final static String BACKUP_WEB_SITE_VISITORS = 
    "WUM.WebSiteVisitors.bak";
  public final static String BACKUP_ACCESS_LOGS = 
    "WUM.AccessLogs.bak";

  public final static int FIRST_ID_ACCESS_LOGS = 100;
  public final static int FIRST_ID_WEB_PAGE_VISITORS = 1000000;
  public final static int FIRST_ID_WEB_SITE_PAGES = 3000000;
  public final static int LAST_ID_WEB_SITE_PAGES = 4999999;
  public final static int FIRST_ID_REFERRER_PAGES = 5000000;
  public final static int LAST_ID_REFERRER_PAGES = 6999999;

  public final static int MINING_BASE_IS_NOT_INSTANTIATED = 0;
  public final static int MINING_BASE_IS_CLOSED = 10;
  public final static int MINING_BASE_IS_OPEN_BUT_NO_LOG_FILE_IS_IMPORTED = 20;
  public final static int MINING_BASE_IS_OPEN_AND_LOG_FILE_IS_IMPORTED = 30;
  public final static int MINING_BASE_IS_OPEN_AND_SESSIONS_ARE_CREATED = 50;
  public final static int MINING_BASE_IS_OPEN_AND_SESSIONS_ARE_AGGREGATED = 60;
  
  // ########## constructors ##########

/**
 * constructs an empty MiningBase
 */  

  public MiningBase() {

    MiningBaseInfo = new MiningBaseInfo();

    ImportedAccessLogs = new AccessLogs(FIRST_ID_ACCESS_LOGS);
    TraversalLog = null;   SessionLog = null;   
    AggregatedLog = null;
  
    ReferrerPages = new Pages(FIRST_ID_REFERRER_PAGES);
    WebSitePages = new Pages(FIRST_ID_WEB_SITE_PAGES);
    WebSiteVisitors = new Visitors(FIRST_ID_WEB_PAGE_VISITORS);
    this.createRelationalDatabases();

    ImportedTraversals = 0;   ImportedSessions = 0;
    Status = MINING_BASE_IS_OPEN_BUT_NO_LOG_FILE_IS_IMPORTED; 

  }

/**
 * constructs a MiningBase with the given attributes
 * @param pName web site's Name
 * @param pServer web site's server URL
 * @param pLogFileDirectory local directory that contains the server's 
 * log files
 * @param pMiningBaseDirectory local directory that contains the corresponding 
 * WUM files
 */  

  public MiningBase(String pName, String pServer, String pLogFileDirectory, 
    String pMiningBaseDirectory, String pRemarks) {

    MiningBaseInfo = new MiningBaseInfo(pName, pServer, pLogFileDirectory, 
      pMiningBaseDirectory, pRemarks);
   
    ImportedAccessLogs = new AccessLogs(FIRST_ID_ACCESS_LOGS);
    TraversalLog = null;   SessionLog = null;   
    AggregatedLog = null;
    ReferrerPages = new Pages(FIRST_ID_REFERRER_PAGES);
    WebSitePages = new Pages(FIRST_ID_WEB_SITE_PAGES);
    WebSiteVisitors = new Visitors(FIRST_ID_WEB_PAGE_VISITORS);
    this.createRelationalDatabases();
    
    ImportedTraversals = 0;   ImportedSessions = 0;   
    Status = MINING_BASE_IS_OPEN_BUT_NO_LOG_FILE_IS_IMPORTED; 

  }

  // ########## mutator methods ##########

  public void setID(long pID) 
    { MiningBaseInfo.setID(pID); }
  public void setName(String pName) 
    { MiningBaseInfo.setName(pName); }
  public void setServer(String pServer) 
    { MiningBaseInfo.setServer(pServer); }
  public void setLogFileDirectory(String pLogFileDirectory)
    { MiningBaseInfo.setLogFileDirectory(pLogFileDirectory); }
  public void setMiningBaseDirectory(String pMiningBaseDirectory)
    { MiningBaseInfo.setMiningBaseDirectory(pMiningBaseDirectory); }
  public void setFirstTimeStamp(String pFirstTimeStamp)
    { MiningBaseInfo.setFirstTimeStamp(pFirstTimeStamp); }
  public void setLastTimeStamp(String pLastTimeStamp)
    { MiningBaseInfo.setLastTimeStamp(pLastTimeStamp); }
  public void setImportedTraversals(int pImportedTraversals) 
    { ImportedTraversals = pImportedTraversals; }
  public void incrementImportedTraversals() 
    { ImportedTraversals++; }
  public void decrementImportedTraversals() 
    { ImportedTraversals--; }
  public void setImportedSessions(int pImportedSessions) { 
    ImportedSessions = pImportedSessions; }
  public void incrementImportedSessions() 
    { ImportedSessions++; }
  public void decrementImportedSessions() 
    { ImportedSessions--; }
  public void setStatus(int pStatus) 
    { Status = pStatus; }
  public void setImportLogFilesHistory(String pImportLogFilesHistory)
    { ImportLogFilesHistory = pImportLogFilesHistory; }
  public void appendImportLogFilesHistory(String pImportLogFilesHistory)
    { ImportLogFilesHistory += pImportLogFilesHistory; }
  public void setCreateVisitorsSessionsHistory(String pCreateSessionsHistory)
    { CreateSessionsHistory = pCreateSessionsHistory; }
  public void setCreateAggregatedLogHistory(String pCreateAggregatedLogHistory)
    { CreateAggregatedLogHistory = pCreateAggregatedLogHistory; }

  public void setTraversalLog(TextFile pTraversalLog)
    { TraversalLog = pTraversalLog; }
  public void setSessionLog(TextFile pSessionLog) 
    { SessionLog = pSessionLog; }
  public void setAggregatedLog(AggregateTree pAggregatedLog)
    { AggregatedLog = pAggregatedLog; }
  
  public void setReferrerPages(Pages pReferrerPages) 
    { ReferrerPages = pReferrerPages; }
  public void setWebSitePages(Pages pWebSitePages)
    { WebSitePages = pWebSitePages; }
  public void setWebSiteVisitors(Visitors pWebSiteVisitors)
    { WebSiteVisitors = pWebSiteVisitors; }

  // ########## accessor methods ##########

  public String getName() { 
    return MiningBaseInfo.getName(); }
  public long getID() 
    { return MiningBaseInfo.getID(); }
  public String getServer() 
    { return MiningBaseInfo.getServer(); }
  public String getLogFileDirectory() 
    { return MiningBaseInfo.getLogFileDirectory(); }
  public String getMiningBaseDirectory() 
    { return MiningBaseInfo.getMiningBaseDirectory(); }
  public String getFirstTimeStamp() 
    { return MiningBaseInfo.getFirstTimeStamp(); }
  public String getLastTimeStamp() 
    { return MiningBaseInfo.getLastTimeStamp(); }
  public int getImportedTraversals() 
    { return ImportedTraversals; }
  public int getImportedSessions() 
    { return ImportedSessions; }
  public int getStatus() 
    { return Status; }
  public AccessLogs getImportedAccessLogs() 
    { return ImportedAccessLogs; }
  public int countImportedAccessLogs() 
    { return ImportedAccessLogs.countAll(); }
  public long getAggregatedLogRootSupport() 
    { return AggregatedLog.getRootSupport(); }    
  public String getImportLogFilesHistory()
    { return ImportLogFilesHistory; }
  public String getCreateVisitorsSessionsHistory()
    { return CreateSessionsHistory; }
  public String getCreateAggregatedLogHistory()
    { return CreateAggregatedLogHistory; }

  public TextFile getTraversalLog()
    { return TraversalLog; }
  public TextFile getSessionLog() 
    { return SessionLog; }
  public AggregateTree getAggregatedLog()
    { return AggregatedLog; }
  
  public Pages getReferrerPages() 
    { return ReferrerPages; }
  public Pages getWebSitePages()
    { return WebSitePages; }
  public Visitors getWebSiteVisitors()
    { return WebSiteVisitors; }

  /**
   * Returns the Page with the given pID from the database WebSitePages or
   * ReferrerPages.
   * @param pID ID of the Page to look for
   * @return  Page with the given pID or null if it doesn't exist
   */

  public Page getPage(long pID) {
  
    if ( (pID >= FIRST_ID_REFERRER_PAGES) && (pID <= LAST_ID_REFERRER_PAGES) )
      return ReferrerPages.get(pID);  
    if ( (pID >= FIRST_ID_WEB_SITE_PAGES) && (pID <= LAST_ID_WEB_SITE_PAGES) )
      return WebSitePages.get(pID);
   
    return null;
  
  }  // getPage()
  
  /**
   * Returns the Visitor with the given pID from the database WebSiteVisiotrs
   * @param pID ID of the Visitor to look for
   * @return  Visitor with the given pID or null if it doesn't exist
   */

  public Visitor getVisitor(long pID) {
  
    return WebSiteVisitors.get(pID); 
  
  }  // getVisitor()
  
  /**
   * Replaces the the given Page in the database WebSitePages resp. 
   * ReferrerPages according to the Page's ID.
   * @param pPage Page to be replaced
   */

  public void replacePage(Page pPage) {
  
    long id = pPage.getID();
    if ( (id >= FIRST_ID_REFERRER_PAGES) && (id <= LAST_ID_REFERRER_PAGES) )
      ReferrerPages.replace(pPage);  
    else 
      if ( (id >= FIRST_ID_WEB_SITE_PAGES) && (id <= LAST_ID_WEB_SITE_PAGES) )
        WebSitePages.replace(pPage);
  
  }  // replacePage()
  
  /**
   * dumps the contents of WebSitePages, ReferrerPages, WebSiteVisitor ans
   * AccessLog into separate text files, file names start with "_"
   */
    
  public void createBackupFiles() {
  
    try {  
    
      FileOutputStream fileOutStream = new FileOutputStream( 
        this.getMiningBaseDirectory() + BACKUP_WEB_SITE_PAGES );
      PrintStream printOutStream = new PrintStream(fileOutStream);
      this.WebSitePages.dumpUrl(printOutStream);
      fileOutStream.close();
      
      fileOutStream = new FileOutputStream( 
        this.getMiningBaseDirectory() + BACKUP_REFERRER_PAGES );
      printOutStream = new PrintStream( fileOutStream );
      this.ReferrerPages.dumpUrl(printOutStream);
      fileOutStream.close();
      
      fileOutStream = new FileOutputStream( 
        this.getMiningBaseDirectory() + BACKUP_WEB_SITE_VISITORS );
      printOutStream = new PrintStream( fileOutStream );
      this.WebSiteVisitors.dumpID(printOutStream);
      fileOutStream.close();

      fileOutStream = new FileOutputStream( 
        this.getMiningBaseDirectory() + BACKUP_ACCESS_LOGS );
      printOutStream = new PrintStream( fileOutStream );
      this.ImportedAccessLogs.dump(printOutStream);
      fileOutStream.close();
      
    }
    catch (IOException e) {}
    
  }  // createBackupFiles()  

  // ########### kwinkler 10/99
  // must be called when a new web site is created in order to generate 
  // the OSTreeMaps

  public void createRelationalDatabases() {

    ReferrerPages.createDatabase();
    WebSitePages.createDatabase();
    WebSiteVisitors.createDatabase();

  }
  
  public String getMiningBaseHistory() {

    StringBuffer result = new StringBuffer();
    String lineSeparator = System.getProperty("line.separator");

    if ( (this.getCreateAggregatedLogHistory() != null) &&
      (this.getCreateAggregatedLogHistory().trim().length() > 0) ) {
      result.append("AGGREGATED LOG:");
      result.append(lineSeparator);
      result.append(lineSeparator);
      result.append( this.getCreateAggregatedLogHistory() );
      result.append(lineSeparator);
      result.append(lineSeparator);
    }
    if ( (this.getCreateVisitorsSessionsHistory() != null) &&
      (this.getCreateVisitorsSessionsHistory().trim().length() > 0) ) {
      result.append("VISITORS' SESSIONS:");
      result.append(lineSeparator);
      result.append(lineSeparator);
      result.append( this.getCreateVisitorsSessionsHistory() );
      result.append(lineSeparator);
      result.append(lineSeparator);
    }
    result.append("IMPORTED LOG FILES:");
    result.append(lineSeparator);
    result.append(lineSeparator);
    result.append( this.getImportLogFilesHistory() );
    
    return result.toString();

  }  //  String getMiningBaseHistory()

  public String getTraversalLogFilename() {

    return this.getMiningBaseDirectory() + TRAVERSAL_LOG_FILE;

  }  // getTraversalLogFilename()

  public String getSessionLogFilename() {

    return this.getMiningBaseDirectory() + SESSION_LOG_FILE;

  }  // getSessionLogFilename()

}  // class MiningBase
