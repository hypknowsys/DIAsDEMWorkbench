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

package org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class CreateDocumentCollectionTask 
extends DiasdemScriptableBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private CreateDocumentCollectionParameter CastParameter = null;
  private CreateDocumentCollectionResult CastResult = null;
  
  private DIAsDEMcollection MyCollection = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Create Document Collection";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection.CreateDocumentCollectionParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection.CreateDocumentCollectionResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection.CreateDocumentCollectionControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateDocumentCollectionTask() {
    
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
  /* ########## interface NonBlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    
    CreateDocumentCollectionParameter parameter = null;
    if (pParameter instanceof CreateDocumentCollectionParameter) {
      parameter = (CreateDocumentCollectionParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    if (parameter.getCollectionName().trim().length() <= 0) {
      result.addError(
      "Error: Please enter a collection name\n" +
      "in the field 'Collection Name'!");
    }
    if (parameter.getCollectionFileName().trim().length() <= 0
    || !parameter.getCollectionFileName().trim().endsWith(
    DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION +
      "-file\nname in the field 'Collection File'!");
    }
    File file = new File(parameter.getCollectionFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Collection File' currently exists.\n" +
      "Do you really want to replace this file?");
    }    
    file = new File(parameter.getCollectionDirectory());
    if (file.exists() && file.isDirectory() && file.list() != null
    && file.list().length > 0) {
      result.addWarning(
      "Warning: The directory specified in the field\n" +
      "'Collection Directory' is not empty. Do you\n" +
      "really want to select this directory?");
    }
    file = new File(parameter.getCollectionDirectory());
    if (!file.exists()) {
      try {
        boolean success = file.mkdirs();
      }
      catch (Exception e2) {}
    }
    if (!file.exists() || !file.isDirectory()) {
      result.addError(
      "Please enter the name of an existing local\n" +
      "directory in the field 'Collection Directory'!");
    }
    if (parameter.getDocumentsPerVolume() < 1) {
      result.addError(
      "Error: Please enter an integer >= 1\n" +
      "in the field 'Documents Per Volume'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult execute(Server pServer, Project pProject,
  TaskParameter pParameter) {
    
    DiasdemServer = pServer;
    if (pProject instanceof DIAsDEMproject) {
      DiasdemProject = (DIAsDEMproject)pProject;
    }
    else {
      DiasdemProject = null;
    }
    Parameter = pParameter;

    if (Parameter != null && Parameter instanceof
    CreateDocumentCollectionParameter) {
      CastParameter = (CreateDocumentCollectionParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
        
    this.acceptTask();
    if (!this.isValidParameter(Parameter, 
    "Error: New DIAsDEM document collection cannot be created!")) {
      return Result; 
    }
    
    try {
      DefaultDIAsDEMcollection collection = new DefaultDIAsDEMcollection();
      collection.create(CastParameter.getCollectionFileName());
      collection.setCollectionName(CastParameter.getCollectionName());
      collection.setCollectionFileName(CastParameter.getCollectionFileName());
      collection.setCollectionDirectory(CastParameter.getCollectionDirectory());
      collection.setCollectionNotes(CastParameter.getCollectionNotes()); 
      collection.setDocumentsPerVolume(CastParameter.getDocumentsPerVolume());
      collection.close();    
      DiasdemProject.setDefaultDiasdemCollectionClassName(
      collection.getClassName());
      CastResult = new CreateDocumentCollectionResult(TaskResult.FINAL_RESULT,
      "The DIAsDEM collection " + CastParameter.getCollectionName() +
      " has been created.\nFile: " + 
      Tools.shortenFileName(CastParameter.getCollectionFileName(), 50), 
      "DIAsDEM collection " + CastParameter.getCollectionName() + " created");
    }
    catch (Exception e) {
      e.printStackTrace();
      CastResult = new CreateDocumentCollectionResult(TaskResult.NO_RESULT,
      "Error: The DIAsDEM collection " + CastParameter.getCollectionName() +
      "\nhas not been created due to an internal error.", "DIAsDEM collection " 
      + CastParameter.getCollectionName() + " cannot be created");
    }
    
    return CastResult;
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new CreateDocumentCollectionParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new CreateDocumentCollectionResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_PREPARE_DATA_SET,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
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