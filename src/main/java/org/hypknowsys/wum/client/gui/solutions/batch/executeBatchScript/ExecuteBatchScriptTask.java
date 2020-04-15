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

package org.hypknowsys.wum.client.gui.solutions.batch.executeBatchScript;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class ExecuteBatchScriptTask extends WumBlockingTask {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private ExecuteBatchScriptParameter CastParameter = null;
  private ExecuteBatchScriptResult CastResult = null;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static String LABEL = 
  "Execute Batch Script"; 
  private final static String TASK_PARAMETER_CLASS_NAME = 
  "org.hypknowsys.wum.client.gui.solutions.batch.executeBatchScript"
  + ".ExecuteBatchScriptParameter"; 
  private final static String TASK_RESULT_CLASS_NAME = 
  "org.hypknowsys.wum.client.gui.solutions.batch.executeBatchScript."
  + "ExecuteBatchScriptResult"; 
  private final static String CONTROL_PANEL_CLASS_NAME = 
  "org.hypknowsys.wum.client.gui.solutions.batch.executeBatchScript."
  + "ExecuteBatchScriptControlPanel"; 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public ExecuteBatchScriptTask() { 

    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;

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
  /* ########## interface BlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
       
    Parameter = pParameter;
    CastParameter = (ExecuteBatchScriptParameter)pParameter;

    AbstractValidatedTaskParameter result = 
    new AbstractValidatedTaskParameter(Parameter);
    
    if (!Tools.isExistingFile(CastParameter.getBatchScriptFileName(),
    WUMproject.SCRIPT_FILE_EXTENSION)) {
      result.addError(
      "Error: The parameter 'Script File Name' is incorrect.\n" +
      "It must contain the valid local file name of an existing\n" +
      "WUM script file whose file extension is '" + 
      WUMproject.SCRIPT_FILE_EXTENSION + "'.");
    }

    return result;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult execute(Server pServer, Project pProject, 
  TaskParameter pParameter) {
    
    WumServer = pServer;
    if (pProject instanceof WUMproject) {
      WumProject = (WUMproject)pProject;
    }
    else {
      WumProject = null;
    }
    Parameter = pParameter;
    CastParameter = (ExecuteBatchScriptParameter)pParameter;
    
    this.acceptTask();
    
    CastResult = new ExecuteBatchScriptResult(TaskResult.NO_RESULT, 
    "Error: The task 'Execute Batch Script' cannot\n" + 
    "be executed as a standalone task.", "Batch script cannot be executed");     
    
    return CastResult;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer, 
  Project pProject) {
    
    return new ExecuteBatchScriptParameter();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ExecuteBatchScriptResult();
    
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