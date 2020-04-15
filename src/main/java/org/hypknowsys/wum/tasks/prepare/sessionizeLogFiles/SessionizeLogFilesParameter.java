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

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.server.*;

/**
 * MaxSessionDuration: 1st criterium to determine sessions; maximum length of 
 * one session, meassured from its first to its last timestamp; "2/03:04:05" 
 * means 2 days, 3 hours, 4 minutes, 5 seconds <p>
 *
 * MaxPageViewTime: 2nd criterium to determine sessions; maximum elapsed time
 * between 2 subsequent page accesses <p>
 *
 * SessionCriterion: indicates which of the 2 criteria is passed, must either 
 * be Session.TOTAL, Session.LOCAL, or Session.WUMPREP
 *
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class SessionizeLogFilesParameter extends WumScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private int SessionCriterion = MAXIMUM_SESSION_DURATION;
  private String SessionThreshold = "0/00:30:00";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.sessionizeLogFiles"
  + ".SessionizeLogFilesTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.sessionizeLogFiles"
  + ".SessionizeLogFilesParameterPanel";
  
  private final static String SESSION_CRITERION =
  "SessionCriterion";
  private final static String SESSION_THRESHOLD =
  "SessionThreshold";
  
  // criterion used to determine ends of sessions
  public final static int MAXIMUM_SESSION_DURATION = 0;
  public final static int MAXIMUM_PAGE_VIEW_TIME = 1;
  public final static int WUMPREP_SESSION_ID = 2;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public SessionizeLogFilesParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getSessionCriterion() { 
    return SessionCriterion; }
  public String getSessionThreshold() { 
    return SessionThreshold; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setSessionCriterion(int pSessionCriterion) { 
    SessionCriterion = pSessionCriterion; }
  public void setSessionThreshold(String pSessionThreshold) { 
    SessionThreshold = pSessionThreshold; }
  
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
    ParameterAttributes.put(SESSION_CRITERION, 
    Tools.int2String(SessionCriterion));
    ParameterAttributes.put(SESSION_THRESHOLD, SessionThreshold);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    SessionCriterion = Tools.string2Int((String)ParameterAttributes
    .get(SESSION_CRITERION));
    SessionThreshold = (String)ParameterAttributes.get(SESSION_THRESHOLD);
    
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
  
  public static boolean isValidSessionCriterion(int pSessionCriterion) { 
   
    if (pSessionCriterion == MAXIMUM_SESSION_DURATION
    || pSessionCriterion == MAXIMUM_PAGE_VIEW_TIME
    || pSessionCriterion == WUMPREP_SESSION_ID) {
      return true; 
    }
    else {
      return false;
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}