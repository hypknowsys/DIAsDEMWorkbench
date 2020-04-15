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

package org.hypknowsys.wum.tasks.prepare.sessionizeLogFiles;

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

public class SessionizeLogFilesTask extends WumScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private SessionizeLogFilesParameter CastParameter = null;
  private SessionizeLogFilesResult CastResult = null;
  
  private String TraversalLine = null;
  private Traversal CurrentTraversal = null;
  private Session CurrentSession = null;  
  
  private HashMap PendingSessions = null;
  private Iterator PendingSessionsIterator = null; 
  private String LimitTimeStamp = null;
  private Object CurrentObject = null;
  
  private int UpdateInterval = 50;
  private String StartCleaningTime = "0/00:00:00";
  private String IntervalCleaningTime = "1/00:00:00";
  private int StartCleaningSessions = 2500;  
  private String InfiniteSessionTime = "10/:00:00";  // WUMPrep  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Sessionize Log Files";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.sessionizeLogFiles"
  + ".SessionizeLogFilesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.sessionizeLogFiles"
  + ".SessionizeLogFilesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.sessionizeLogFiles"
  + ".SessionizeLogFilesControlPanel";
  
  private int MIN_REQUIRED_MINING_BASE_STATUS = MiningBase
  .MINING_BASE_IS_OPEN_AND_LOG_FILE_IS_IMPORTED;
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_CRITERION", 
    "Sessionize Log Files: Default Session Criterion",
    "0", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("SESSIONIZE_LOG_FILES:_DEFAULT_SESSION_THRESHOLD", 
    "Sessionize Log Files: Default Session Threshold",
    "0/00:30:00", KProperty.TIMESTRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public SessionizeLogFilesTask() {
    
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
    
    SessionizeLogFilesParameter parameter = null;
    if (pParameter instanceof SessionizeLogFilesParameter) {
      parameter = (SessionizeLogFilesParameter)pParameter;
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
      "the minimum status code required by this task!\n" +
      "Please import at least one log file.");
    }
        
    if (parameter.getSessionCriterion() == parameter.MAXIMUM_SESSION_DURATION
    || parameter.getSessionCriterion() == parameter.MAXIMUM_PAGE_VIEW_TIME) {
      if (!TimeStamp.isValidTime(parameter.getSessionThreshold())) {
      result.addError(
      "Error: Enter a valid D/HH:MM:SS time string\n" +
      "in the field 'Threshold'. For example, enter\n" +
      "0/00:30:00 for a threshold of 30 minutes.");
      }
    }
        
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new SessionizeLogFilesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new SessionizeLogFilesResult();
    
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
    "SESSIONIZE_LOG_FILES:_SESSIONS_UPDATE_INTERVAL");   
    StartCleaningTime = WumProject.getTimeStringProperty(
    "SESSIONIZE_LOG_FILES:_SESSIONS_START_CLEANING_TIME");   
    IntervalCleaningTime = WumProject.getTimeStringProperty(
    "SESSIONIZE_LOG_FILES:_INTERVAL_CLEANING_TIME");   
    StartCleaningSessions = WumProject.getIntProperty(
    "SESSIONIZE_LOG_FILES:_SESSIONS_START_CLEANING_SESSIONS");   
    InfiniteSessionTime = WumProject.getTimeStringProperty(
    "SESSIONIZE_LOG_FILES:_INFINITE_SESSION_TIME"); 
    
    if (Parameter != null && Parameter instanceof SessionizeLogFilesParameter) {
      CastParameter = (SessionizeLogFilesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Log files cannot be sessionized!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
        
    int counterProgress = 0;
    long maxProgress = WumProject.getMiningBase().getImportedTraversals();
    
    PendingSessions = new HashMap();
    
    File CurrentSessionLogFile = 
      new File(WumProject.getMiningBase().getMiningBaseDirectory() + 
      WumProject.getMiningBase().SESSION_LOG_FILE );
    CurrentSessionLogFile.delete(); CurrentSessionLogFile = null;
    WumProject.getMiningBase().setImportedSessions(0);
    WumProject.getMiningBase().setTraversalLog( 
      new TextFile( new File(WumProject.getMiningBase().getMiningBaseDirectory() + 
        WumProject.getMiningBase().TRAVERSAL_LOG_FILE ) ) );
    WumProject.getMiningBase().getTraversalLog().open();
    WumProject.getMiningBase().setSessionLog( 
      new TextFile( new File(WumProject.getMiningBase().getMiningBaseDirectory() + 
        WumProject.getMiningBase().SESSION_LOG_FILE ) ) );
    WumProject.getMiningBase().getSessionLog().open();    
  
    Progress.update( (int)(10 / maxProgress), "Current Log File Line 1"); 
    WumServer.setTaskProgress(Progress, TaskThread);
    CurrentTraversal = new Traversal();

//     System.out.println("\n ###   Threshold MPVT: " + 
//       Parameter.getMaxPageViewTime() );
//     System.out.println(" ###   Threshold MSeD: " + 
//       Parameter.getMaxSessionDuration() );

    TraversalLine = WumProject.getMiningBase().getTraversalLog().getFirstLine();
    counterProgress++;  
    while (TraversalLine != null) {

      if ( (counterProgress % UpdateInterval) == 0 ) {
        Progress.update( (int)(counterProgress * 100 / maxProgress), 
         "Current Log File Line " +  maxProgress); 
        WumServer.setTaskProgress(Progress, TaskThread);
      }      
      
      CurrentTraversal.fromItemLine(TraversalLine);
      
      if (CurrentTraversal.isValid() ) { 
        
        // set first time to save pending sesions 
        // that are finished to SessionLog
        if (WumProject.getMiningBase().getImportedSessions() == 0)
          LimitTimeStamp = TimeStamp.TimeStampPlusTime( 
            CurrentTraversal.getTimeStamp(), StartCleaningTime);
            
        // save pending sesions that are finished to SessionLog
        if ( ( PendingSessions.size() > StartCleaningSessions ) ||
          ( LimitTimeStamp.compareTo( CurrentTraversal.getTimeStamp() ) < 0 ) )
          this.cleanPendingSessions();            
            
        CurrentSession = (Session)PendingSessions.get( 
          new Long( CurrentTraversal.getVisitorID() ) );      

    
        if (CurrentSession == null) {
          // corresponding Session of Visitor/SessionID already exists
          this.newSession();  
// 	  System.out.println(CurrentTraversal.getTimeStamp() + 
// 	    " ### new Visitor ###     " + 
// 	    CurrentSession.getEndOfSessionTimeStamp() );
        }
        else 
          if (CurrentSession.getEndOfSessionTimeStamp().compareTo(
            CurrentTraversal.getTimeStamp() ) < 0) {
            this.newSession();  // previous Session is finished
// 	    System.out.println(CurrentTraversal.getTimeStamp() + 
// 	      " ### new (threshold) ### " + 
// 	      CurrentSession.getEndOfSessionTimeStamp() );

          }
          else {
            this.continueSession();  // continue current Session
// 	    System.out.println(CurrentTraversal.getTimeStamp() + 
// 	      " ### continue ###        " + 
// 	      CurrentSession.getEndOfSessionTimeStamp() );

          }
          
      }  // valid
      
      TraversalLine = WumProject.getMiningBase().getTraversalLog().getNextLine();
      counterProgress++;  
      
    }  // while
    
    PendingSessionsIterator = PendingSessions.values().iterator();
    while ( PendingSessionsIterator.hasNext() )
      WumProject.getMiningBase().getSessionLog().setNextLine( 
        ( (Session)PendingSessionsIterator.next() ).toItemLine() );
    
    WumProject.getMiningBase().getTraversalLog().close();
    WumProject.getMiningBase().getSessionLog().close();
    
    WumProject.getMiningBase().setStatus(MiningBase
    .MINING_BASE_IS_OPEN_AND_SESSIONS_ARE_CREATED);

    String history = Tools.getSystemDate() + Tools.getLineSeparator() + 
      "Session Criterion: " + Tools.getLineSeparator();
    if (CastParameter.getSessionCriterion() 
    == CastParameter.MAXIMUM_SESSION_DURATION) {
      history += "Maximum Session Duration = " 
      + CastParameter.getSessionThreshold();
    }
    else if (CastParameter.getSessionCriterion()
    == CastParameter.MAXIMUM_PAGE_VIEW_TIME) {
      history += "Maximum Page View Time = "
      + CastParameter.getSessionThreshold();
    }
    else if (CastParameter.getSessionCriterion()
    == CastParameter.WUMPREP_SESSION_ID) {
      history += "Use WUMprep Session ID";
    }
    history += Tools.getLineSeparator() + "Results: " +
    Tools.getLineSeparator() + WumProject.getMiningBase().getImportedSessions()
    + " sessions were successfully created.";
    WumProject.getMiningBase().setCreateVisitorsSessionsHistory(history);
    WumProject.getMiningBase().setCreateAggregatedLogHistory("");

    CastResult = new SessionizeLogFilesResult(TaskResult.FINAL_RESULT,
    WumProject.getMiningBase().getImportedSessions() +
    " sessions were successfully created.", 
    WumProject.getMiningBase().getImportedSessions() +
    " sessions were successfully created.");
    this.setTaskResult(100, "All Log Files Processed ...", CastResult,
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
   * saves prevoius Session of the corresponding Visitor and
   * creates a new Session in PendingSessions
   */
  
  private void newSession() {
    
    //  writes prevoius CurrentSession to file SessionsLog
    if (CurrentSession != null)
      WumProject.getMiningBase().getSessionLog().setNextLine(
      CurrentSession.toItemLine() );
    
    // creates new Session starting with current Traversal
    CurrentSession = new Session(
    CurrentTraversal.getVisitorID(), CurrentTraversal.getTimeStamp() );
    CurrentSession.setFirstPage( CurrentTraversal.getFromPageID() );
    CurrentSession.setNextPage( CurrentTraversal.getToPageID() );
    // determine the timestamp when this Sessions is finished
    if (CastParameter.getSessionCriterion() 
    == CastParameter.MAXIMUM_SESSION_DURATION) {
      CurrentSession.setEndOfSessionTimeStamp(
      TimeStamp.TimeStampPlusTime(CurrentTraversal.getTimeStamp(),
      CastParameter.getSessionThreshold()));
      // System.out.println(CurrentTraversal.getTimeStamp() + "  " +
      // Parameter.getMaxSessionDuration() + " ### new EofSeTS ### " +
      // CurrentSession.getEndOfSessionTimeStamp() );
    }
    else if (CastParameter.getSessionCriterion() 
    == CastParameter.MAXIMUM_PAGE_VIEW_TIME) {
      CurrentSession.setEndOfSessionTimeStamp(
      TimeStamp.TimeStampPlusTime(CurrentTraversal.getTimeStamp(),
      CastParameter.getSessionThreshold()));
      // System.out.println(" ### new EofSeTS ### " +
      // CurrentSession.getEndOfSessionTimeStamp() );
    }
    else if (CastParameter.getSessionCriterion()
    == CastParameter.WUMPREP_SESSION_ID) {
      CurrentSession.setEndOfSessionTimeStamp(
      TimeStamp.TimeStampPlusTime(CurrentTraversal.getTimeStamp(),
      InfiniteSessionTime ) );
      // System.out.println(" ### new inEoSTS ### " +
      // CurrentSession.getEndOfSessionTimeStamp() );
    }
    CurrentObject = PendingSessions.put(
    new Long( CurrentSession.getVisitorID() ), CurrentSession );
    WumProject.getMiningBase().incrementImportedSessions();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * continues pending Session of the corresponding Visitor
   */
  
  private void continueSession() {
    
    // adds toPage of current Traversal to pending CurrentSession
    CurrentSession.setNextPage(CurrentTraversal.getToPageID() );
    // determine the timestamp when this Sessions is finished
    if (CastParameter.getSessionCriterion() 
    == CastParameter.MAXIMUM_PAGE_VIEW_TIME)
      CurrentSession.setEndOfSessionTimeStamp(
      TimeStamp.TimeStampPlusTime(
      CurrentTraversal.getTimeStamp(), CastParameter.getSessionThreshold()));
    CurrentObject = PendingSessions.put(
    new Long( CurrentSession.getVisitorID() ), CurrentSession );
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * determines all finished Sessions in PendingSessions,removes them from
   * PendingSessions and saves them to the file SessionLog
   */
  
  private void cleanPendingSessions() {
    
    Session tmpSession = null;
    
    // create new HashSet to avoid java.util.ConcurrentModificationException
    // when removing entries from the original HashMap containing all sessions
    HashSet tmpPendingSessions = new HashSet(
    PendingSessions.values().size() );
    tmpPendingSessions.addAll( PendingSessions.values() );
    PendingSessionsIterator = tmpPendingSessions.iterator();
    while ( PendingSessionsIterator.hasNext() ) {
      tmpSession = (Session)PendingSessionsIterator.next();
      if ( tmpSession.getEndOfSessionTimeStamp().compareTo(
      CurrentTraversal.getTimeStamp() ) < 0 ) {
        WumProject.getMiningBase().getSessionLog().setNextLine(
        tmpSession.toItemLine() );
        CurrentObject = PendingSessions.remove(
        new Long( tmpSession.getVisitorID() ) );
      }
    }
    
    // set next time to save pending sesions that are finished to SessionLog
    LimitTimeStamp = TimeStamp.TimeStampPlusTime(
    CurrentTraversal.getTimeStamp(), IntervalCleaningTime);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}