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

package org.hypknowsys.diasdem.client.gui.solutions.batch.editBatchScript;

import java.lang.reflect.InvocationTargetException;
import org.hypknowsys.core.Project;
import org.hypknowsys.core.ScriptTask;
import org.hypknowsys.diasdem.server.DiasdemTask;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class EditBatchScriptTaskTask extends DiasdemTask {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private EditBatchScriptTaskParameter CastParameter = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static String LABEL = 
  "Edit Task of Batch Script"; 
  private final static String TASK_PARAMETER_CLASS_NAME = 
  "org.hypknowsys.diasdem.client.gui.solutions.batch.editBatchScript" +
  ".EditBatchScriptTaskParameter"; 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public EditBatchScriptTaskTask() { 

    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;

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
    CastParameter = (EditBatchScriptTaskParameter)pParameter;
    
    AbstractValidatedTaskParameter result = 
    new AbstractValidatedTaskParameter(Parameter);
    
    ScriptTask scriptTask = CastParameter.getDiasdemScriptTask();
    TaskParameter scriptTaskParameter = null;
    if (scriptTask != null && scriptTask.getParameter() != null) {
      scriptTaskParameter = scriptTask.getParameter();
      result = this.validateTaskParameter(DiasdemProject,
      scriptTaskParameter.getTaskClassName(), scriptTaskParameter);
      // looking for errors does ot make sense when editing a batch script
      result.convertErrorsIntoWarnings("\n\nMake sure that preceding script"
      + " tasks fulfill\nthis requirement. Do you want to continue?");
    }

    if (scriptTask.getClassName() == null 
    || scriptTask.getClassName().length() == 0) {
      result.addError(
      "Error: The task does not contain its respective\n"
      + "class name. Please input a valid Java class name\n"
      + "such as diasdem.tasks.MyTask in the field 'Class\n"
      + "Name'. This class must implement certain Java\n"
      + "interfaces as described in the tutorial.");
    }
    if (scriptTask.getParameter() == null) {
      result.addError(
      "Error: The task does not\n"
      + "have parameter settings.");
    }

    return result;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public TaskParameter getDefaultTaskParameter(Server pServer, 
  Project pProject) {
    
    return new EditBatchScriptTaskParameter();
    
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
  
  private AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, String pTaskClassName, TaskParameter pTaskParameter) {
  
    // pTaskClassName = 
    // "org.hypknowsys.diasdem.tasks.file.newProject.newProjectTask"
    // exactly the same method as in -- single task -- DIAsDEMserver to
    // avoid recursive calls of DIAsDEMserver.validateTaskParameter()

    AbstractValidatedTaskParameter validatedTaskParameter = null;
    Task taskForParameterValidation = null;
    try {
      taskForParameterValidation = (Task)Class.forName(pTaskClassName)
      .getConstructor(null).newInstance(null);
      validatedTaskParameter = ((Task)taskForParameterValidation)
      .validateTaskParameter(pProject, pTaskParameter);  
    }
    catch(ClassNotFoundException e1) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: ClassNotFoundException, parameter cannot be validated!\n" 
      + pTaskClassName);
      e1.printStackTrace(); 
    }
    catch(NoSuchMethodException e2) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: NoSuchMethodException, parameter cannot be validated!\n" 
      + pTaskClassName);      
      e2.printStackTrace();
    }
    catch(InstantiationException e3) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: InstantiationException, parameter cannot be validated!\n" 
      + pTaskClassName);      
      e3.printStackTrace(); 
    }
    catch(IllegalAccessException e4) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: IllegalAccessException, parameter cannot be validated!\n" 
      + pTaskClassName);      
      e4.printStackTrace(); 
    }
    catch(InvocationTargetException e5) { 
      validatedTaskParameter = new AbstractValidatedTaskParameter(
      pTaskParameter);
      validatedTaskParameter.addError(
      "Error: InvocationTargetException, parameter cannot be validated!\n" 
      + pTaskClassName);      
      e5.printStackTrace(); 
    }

    return validatedTaskParameter;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}