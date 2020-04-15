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

package org.hypknowsys.server;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskResultPanel;
import org.hypknowsys.core.Project;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class AbstractTaskResult implements TaskResult, Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String ResultPanelClassName = null;
  
  protected int Status = TaskResult.RESULT_UNKNOWN;
  protected String Description = null;
  protected Object Container = null;
  protected String LogMessage = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String RESULT_PANEL_CLASS_NAME = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskResult() {
    
    ResultPanelClassName = RESULT_PANEL_CLASS_NAME;
    Status = 0;
    Description = "";
    Container = null;
    LogMessage = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskResult(int pStatus, String pDescription) {
    
    this();
    Status = pStatus;
    Description = pDescription;
    Container = null;
    LogMessage = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskResult(int pStatus, String pDescription,
  String pLogMessage) {
    
    this();
    Status = pStatus;
    Description = pDescription;
    Container = null;
    LogMessage = pLogMessage;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskResult(int pStatus, String pDescription,
  Object pContainer) {
    
    this();
    Status = pStatus;
    Description = pDescription;
    Container = pContainer;
    LogMessage = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskResult(int pStatus, String pDescription,
  Object pContainer, String pLogMessage) {
    
    this();
    Status = pStatus;
    Description = pDescription;
    Container = pContainer;
    LogMessage = pLogMessage;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getStatus() { return Status; }
  public String getDescription() { return Description; }
  public Object getContainer() { return Container; }
  public String getLogMessage() { return LogMessage; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setStatus(int pStatus) {
    Status = pStatus; }
  public void setDescription(String pDescription) {
    Description = pDescription; }
  public void setContainer(Object pContainer) {
    Container = pContainer; }
  public void setLogMessage(String pLogMessage) {
    LogMessage = pLogMessage; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskResult methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResultPanel getTaskResultPanel(Server pServer,
  Project pProject, GuiClient pGui, GuiClientPreferences pGuiPreferences) {
    
    if (this.getTaskResultPanelClassName() != null) {
      TaskResultPanel currentResultPanel = null;
      try {
        currentResultPanel = (TaskResultPanel)Class
        .forName(this.getTaskResultPanelClassName())
        .getConstructor(null).newInstance(null);
        currentResultPanel.setContext(pServer, pProject, pGui,
        pGuiPreferences);
        currentResultPanel.initialize();
        currentResultPanel.setTaskResult(this);
        return currentResultPanel;
      }
      catch(ClassNotFoundException e1) { e1.printStackTrace(); }
      catch(NoSuchMethodException e2) { e2.printStackTrace(); }
      catch(InstantiationException e3) { e3.printStackTrace(); }
      catch(IllegalAccessException e4) { e4.printStackTrace(); }
      catch(InvocationTargetException e5) { e5.printStackTrace(); }
    }
    
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskResultPanelClassName() {
    
    if (ResultPanelClassName == null) {
      System.err.println(this.getClass().getName()
      + ": Implement TaskParameter!");
    }
    return ResultPanelClassName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getStatusString() {
    
    String status = "";
    switch (Status) {
      case TaskResult.RESULT_UNKNOWN: {
        status = "RESULT_UNKNOWN"; break;
      }
      case TaskResult.NO_RESULT: {
        status = "NO_RESULT"; break;
      }
      case TaskResult.INTERMEDIATE_RESULT: {
        status = "INTERMEDIATE_RESULT"; break;
      }
      case TaskResult.FINAL_RESULT: {
        status = "FINAL_RESULT"; break;
      }
    }
    return status;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void update(int pStatus) {
    
    Status = pStatus;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void update(int pStatus, String pDescription) {
    
    Status = pStatus;
    Description = pDescription;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void update(int pStatus, String pDescription, String pLogMessage) {
    
    Status = pStatus;
    Description = pDescription;
    LogMessage = pLogMessage;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void update(int pStatus, String pDescription, Object pContainer) {
    
    Status = pStatus;
    Description = pDescription;
    Container = pContainer;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void update(int pStatus, String pDescription, Object pContainer,
  String pLogMessage) {
    
    Status = pStatus;
    Description = pDescription;
    Container = pContainer;
    LogMessage = pLogMessage;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void appendDescription(String pDescription) {
    
    Description += pDescription;
    
  }
  
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
  
  public static void main(String pOptions[]) {}
  
}