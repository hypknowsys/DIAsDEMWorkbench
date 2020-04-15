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

package org.hypknowsys.core;

import java.io.Serializable;
import org.hypknowsys.misc.swing.KTableModel;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface Script extends Cloneable, Serializable, KTableModel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final int SCRIPT_NOT_EXECUTED = 1;
  public static final int SCRIPT_EXECUTED_WITH_ERRORS = 2;
  public static final int SCRIPT_EXECUTED_WITHOUT_ERRORS = 3;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getLabel();
  
  public String getNotes();
  
  public String getLog();
  
  public int getStatus();
  
  public String getStatusString();
  
  public String getStartTimeStamp();
  
  public String getEndTimeStamp();
  
  public String getTransientFileName();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setLabel(String pLabel);
  
  public void setNotes(String pNotes);
  
  public void setLog(String pLog);
  
  public void setStatus(int pStatus);
  
  public void setStartTimeStamp(String pStartTimeStamp);
  
  public void setEndTimeStamp(String pEndTimeStamp);
  
  public void setTransientFileName(String pTransientFileName);
  
  public String getInternalDtdSubset();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString();
  
  public Object clone();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addLog(String pLog);
  
  public int countScriptTasks();
  
  public void addScriptTask(ScriptTask pScriptTask);
  
  public ScriptTask getFirstScriptTask();
  
  public ScriptTask getNextScriptTask();
  
  public void setFromXmlDocument(String pXmlFileName)
  throws HypknowsysException;
  
  public void writeXmlDocument(String pXmlFileName);
  
  public void appendScriptTask(ScriptTask pScriptTask, int pTaskIndex);
  
  public void insertScriptTask(ScriptTask pScriptTask, int pTaskIndex);
  
  public void deleteScriptTask(int pTaskIndex);
  
  public ScriptTask getScriptTask(int pTaskIndex);
  
  public void replaceScriptTask(ScriptTask pScriptTask, int pTaskIndex);
  
  public void resetScriptTask(int pTaskIndex);
  
  public void resetAllScriptTasks();
  
  public void resetScript();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}