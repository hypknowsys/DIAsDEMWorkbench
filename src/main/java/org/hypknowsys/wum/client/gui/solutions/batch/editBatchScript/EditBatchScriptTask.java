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

package org.hypknowsys.wum.client.gui.solutions.batch.editBatchScript;

import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class EditBatchScriptTask extends WumTask {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private EditBatchScriptParameter CastParameter = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static String LABEL = 
  "Edit Batch Script"; 
  private final static String TASK_PARAMETER_CLASS_NAME = 
  "org.hypknowsys.wum.client.gui.solutions.batch.editBatchScript"
  + ".EditBatchScriptParameter"; 
  private final static String CONTROL_PANEL_CLASS_NAME =  
  "org.hypknowsys.wum.client.gui.solutions.batch.editBatchScript"
  + ".EditBatchScriptControlPanel"; 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public EditBatchScriptTask() { 

    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
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
  /* ########## interface Task methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
       
    Parameter = pParameter;
    CastParameter = (EditBatchScriptParameter)pParameter;
    Script script = CastParameter.getWumScript();
    
    AbstractValidatedTaskParameter result = 
    new AbstractValidatedTaskParameter(Parameter);
    
    if (script == null || script.countScriptTasks() == 0) {
      result.addError(
      "Error: The current batch script does\n"
      + "not contain any task at all! Empty\n"
      + "batch scripts cannot be saved.");
      return result;
    }

    if (script.getLabel() == null || script.getLabel().length() == 0) {
      result.addWarning(
      "Warning: The script has an empty label.\n" +
      "Do you really want to continue?");      
    }
        
    ScriptTask task = null;
    for (int i = 0; i < script.countScriptTasks(); i++) {
      task = script.getScriptTask(i);
      if (task.getClassName() == null || task.getClassName().length() == 0) {
        result.addError(
        "Error: Task " + (i+1) + " of the current batch script does not\n"
        + "contain its respective class name. Please input a\n"
        + "valid Java class name such as wum.tasks.MyTask\n"
        + "in the field 'Class Name'. This class must implement\n"
        + "certain Java interfaces as described in the tutorial.");
      }
      if (task.getParameter() == null) {
        result.addError(
          "Error: Task " + (i+1) + " of the current batch\n"
        + "script does not have parameter settings.");
      }
      
    }
  
    return result;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer, 
  Project pProject) {
    
    return new EditBatchScriptParameter();
    
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