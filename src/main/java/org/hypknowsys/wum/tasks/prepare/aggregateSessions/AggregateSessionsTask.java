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

package org.hypknowsys.wum.tasks.prepare.aggregateSessions;

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

public class AggregateSessionsTask extends WumScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private AggregateSessionsParameter CastParameter = null;
  private AggregateSessionsResult CastResult = null;
  
  private Session CurrentSession = null;
  private Trail CurrentTrail = null;
  private String SessionLine = null;
  
  private int TransactionSize = 100;
  private int UpdateInterval = 50;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient PageOccurrence TmpPageOccurrence = null;
  private transient Trail TmpTrail = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Aggregate Sessions";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.aggregateSessions"
  + ".AggregateSessionsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.aggregateSessions"
  + ".AggregateSessionsResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.aggregateSessions"
  + ".AggregateSessionsControlPanel";
  
  private int MIN_REQUIRED_MINING_BASE_STATUS = MiningBase
  .MINING_BASE_IS_OPEN_AND_SESSIONS_ARE_CREATED;
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("AGGREGATE_SESSIONS:_DEFAULT_FIRST_PAGE_OCCURRENCE", 
    "Aggregate Sessions: Default First Page Occurrence", Tools.int2String(
    AggregateSessionsParameter.EXCLUDE_REFERRER_URL_IN_OBSERVATIONS), 
    KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("AGGREGATE_SESSIONS:_DEFAULT_ADD_REVERSE_TRAIL", 
    "Aggregate Sessions: Default Add Reverse Trail",
    "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AggregateSessionsTask() {
    
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
    
    AggregateSessionsParameter parameter = null;
    if (pParameter instanceof AggregateSessionsParameter) {
      parameter = (AggregateSessionsParameter)pParameter;
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
      "Please sessionize the imported log files.");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new AggregateSessionsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new AggregateSessionsResult();
    
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
    "AGGREGATE_SESSIONS:_SESSIONS_UPDATE_INTERVAL");   
    
    if (Parameter != null && Parameter instanceof AggregateSessionsParameter) {
      CastParameter = (AggregateSessionsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Sessions cannot be aggregated!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
        
    WumProject.getMiningBase().setSessionLog( new TextFile( new File(
      WumProject.getMiningBase().getMiningBaseDirectory() + 
      MiningBase.SESSION_LOG_FILE ) ) );
    WumProject.getMiningBase().getSessionLog().open();   
    WumProject.getMiningBase().setAggregatedLog( new AggregateTree() );
    
    CurrentSession = new Session();
    CurrentTrail = null;

    int counterProgress = 0;
    long maxProgress = WumProject.getMiningBase().getImportedSessions();

    SessionLine = WumProject.getMiningBase().getSessionLog().getFirstLine();
    counterProgress++;          
    while (SessionLine != null) {
    
      if (counterProgress == 1 || (counterProgress % UpdateInterval) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Aggregating Session " + counterProgress);
        WumServer.setTaskProgress(Progress, TaskThread);
      }
      
      CurrentSession = new Session();
      CurrentSession.fromItemLine(SessionLine);
      if (CurrentSession.isValid() ) { 
          
        CurrentTrail = CurrentSession.getTrail();
        // create Trail to aggregate
        if (CastParameter.getFirstPageOccurrence() == CastParameter
        .EXCLUDE_REFERRER_URL_IN_OBSERVATIONS) {  
          int skipPageOccurrence = 1;
          TmpPageOccurrence = null;
          TmpTrail = CurrentTrail;
          CurrentTrail = new Trail();
          TmpPageOccurrence = TmpTrail.getFirstPageOccurrence();
          while (TmpPageOccurrence != null) {
            if (skipPageOccurrence == 0) {
              CurrentTrail.setFirstPageOccurrence(TmpPageOccurrence);
            }
            else if (skipPageOccurrence < 0) {
              CurrentTrail.setNextPageOccurrence(TmpPageOccurrence);
            }
            skipPageOccurrence--;
            TmpPageOccurrence = TmpTrail.getNextPageOccurrence();
          }          
        }

        if ( !CastParameter.addReverseTrail() )
          WumProject.getMiningBase().getAggregatedLog()
          .addTrail(CurrentTrail);
        else
          WumProject.getMiningBase().getAggregatedLog()
          .addReverseTrail(CurrentTrail);
        
      } //if:ValidSession

      SessionLine = WumProject.getMiningBase().getSessionLog().getNextLine();
      counterProgress++;      
    
    }  // while
    
    WumProject.getMiningBase().getSessionLog().close();
    WumProject.getMiningBase().setStatus(MiningBase
    .MINING_BASE_IS_OPEN_AND_SESSIONS_ARE_AGGREGATED);

    String history = Tools.getSystemDate() + Tools.getLineSeparator() +
    "Settings: " + Tools.getLineSeparator();
    if (CastParameter.getFirstPageOccurrence() == CastParameter
    .INCLUDE_REFERRER_URL_IN_OBSERVATIONS) {
      history += "Include Referrer URL in Observations";
    }
    else {
      history += "Exclude Referrer URL in Observations";
    }
    if (CastParameter.addReverseTrail()) {
      history += " and  Reverse Sessions.\n";
    }
    else {
      history += "." + Tools.getLineSeparator();
    }
    history += "Results:" + Tools.getLineSeparator() 
    + WumProject.getMiningBase().getImportedSessions()
    + " trails have been aggregated in the aggregated log."
    + Tools.getLineSeparator() + "The aggregated log consists of " 
    + WumProject.getMiningBase().getAggregatedLog().countObservations()
    + " observations (i.e. branches)" + Tools.getLineSeparator()+ "and " 
    + WumProject.getMiningBase().getAggregatedLog().getRootCountChildren() 
    + " children of the root node (root support = " 
    + WumProject.getMiningBase().getAggregatedLog().getRootSupport() + ").";
    WumProject.getMiningBase().setCreateAggregatedLogHistory(history);
      
    CastResult = new AggregateSessionsResult(TaskResult.FINAL_RESULT,
    WumProject.getMiningBase().getImportedSessions() +
    " sessions trails have been aggregated in the aggregated log.\n" +
    "The aggregated log consists of " +
    WumProject.getMiningBase().getAggregatedLog().countObservations() +
    " observations (i.e., branches)\nand " +
    WumProject.getMiningBase().getAggregatedLog().getRootCountChildren() +
    (WumProject.getMiningBase().getAggregatedLog().getRootCountChildren() 
    == 1 ? " child" : " children") + " of the root node (root support = " +
    WumProject.getMiningBase().getAggregatedLog().getRootSupport() + ").",
    WumProject.getMiningBase().getImportedSessions() +
    " sessions trails have been aggregated in the aggregated log.");
    this.setTaskResult(100, "All Sessions Aggregated ...", CastResult,
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}