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

package org.hypknowsys.wum.server;

import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class WumTask implements Task {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected String Label = null;
  protected String TaskParameterClassName = null;
  protected String TaskResultClassName = null;
  protected String ControlPanelClassName = null;
  protected KProperty[] ProjectPropertyData = null;
  protected KProperty[] GuiClientPropertyData = null;
  protected int MinRequiredMiningBaseStatus = MiningBase
  .MINING_BASE_IS_NOT_INSTANTIATED;
   
  protected Server WumServer = null;
  protected WUMproject WumProject = null;
  protected TaskParameter Parameter = null;
  protected TaskResult Result = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static String LABEL = null; 
  private final static String TASK_PARAMETER_CLASS_NAME = null; 
  private final static String TASK_RESULT_CLASS_NAME = null; 
  private final static String CONTROL_PANEL_CLASS_NAME = null; 
  private final static KProperty[] PROJECT_PROPERTY_DATA = null;
  private final static KProperty[] GUI_CLIENT_PROPERTY_DATA = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public WumTask() { 

    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
    GuiClientPropertyData = GUI_CLIENT_PROPERTY_DATA;

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
  /* ########## interface Task methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getClassName() {
    return this.getClass().getName(); }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getLabel() {

    if (Label == null) {
      System.err.println(this.getClassName() + ": Implement Task!");
    }
    return Label;

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskParameterClassName() {

    if (TaskParameterClassName == null) {
      System.err.println(this.getClassName() + ": Implement Task!");
    }
    return TaskParameterClassName;

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer, 
  Project pProject) {
    
    return new AbstractTaskParameter();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;    
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskResultClassName() {
    
    if (TaskResultClassName == null) {
      System.err.println(this.getClassName() + ": Implement Task!");
    }
    return TaskResultClassName;

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, 
  Project pProject) {
    
    return new AbstractTaskResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskControlPanelClassName() {
    
    if (ControlPanelClassName == null) {
      System.err.println(this.getClassName() + ": Implement Task!");
    }
    return ControlPanelClassName;

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskControlPanel getTaskControlPanel(Server pServer, 
  Project pProject, GuiClient pGui, GuiClientPreferences pGuiPreferences) {
    
    if (this.getTaskControlPanelClassName() != null) {
       TaskControlPanel currentControlPanel = null;
      try {
        currentControlPanel = (TaskControlPanel)Class
        .forName(this.getTaskControlPanelClassName())
        .getConstructor(null).newInstance(null);
        currentControlPanel.setContext(pServer, pProject, pGui, 
        pGuiPreferences);
        currentControlPanel.initialize();
        return currentControlPanel;
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
  
  public KMenuItem getKMenuItem() {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KProperty[] getProjectPropertyData() {
    return ProjectPropertyData;    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KProperty[] getGuiClientPropertyData() {
    return GuiClientPropertyData;    
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