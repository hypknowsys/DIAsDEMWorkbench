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
import org.jdom.Element;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface ScriptTask extends Cloneable, Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final int TASK_NOT_EXECUTED = 1;
  public static final int TASK_EXECUTED_WITH_ERRORS = 2;
  public static final int TASK_EXECUTED_WITHOUT_ERRORS = 3;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getLabel();
  
  public String getClassName();
  
  public ScriptableTaskParameter getParameter();
  
  public ScriptableTaskResult getResult();
  
  public String getNotes();
  
  public String getLog();
  
  public int getStatus();
  
  public String getStatusString();
  
  public String getStartTimeStamp();
  
  public String getEndTimeStamp();
  
  public String getTransientScriptLabel();
  
  public int getTransientTaskNumber();
  
  public boolean execute();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setLabel(String pLabel);
  
  public void setClassName(String pClassName);
  
  public void setParameter(ScriptableTaskParameter pParameter);
  
  public void setResult(ScriptableTaskResult pResult);
  
  public void setNotes(String pNotes);
  
  public void setLog(String pLog);
  
  public void setStatus(int pStatus);
  
  public void setStartTimeStamp(String pStartTimeStamp);
  
  public void setEndTimeStamp(String pEndTimeStamp);
  
  public void setTransientScriptLabel(String pTransientScriptLabel);
  
  public void setTransientTaskNumber(int pTransientTaskNumber);
  
  public void setExecute(boolean pExecute);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Cloneable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object clone();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addLog(String pLog);
  
  public Element getAsJDomElement();
  
  public void setFromJDomElement(Element pJDomElement)
  throws HypknowsysException;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}