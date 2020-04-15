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

package org.hypknowsys.wum.tasks.project.newProject;

import java.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class NewProjectParameter extends WumScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String ProjectName = null;
  protected String ProjectNotes = null;
  protected String ProjectFileName = null;
  protected String ProjectDirectory = null;
  protected String ParameterDirectory = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.wum.tasks.project.newProject.NewProjectTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.project.newProject.NewProjectParameterPanel";
  
  private final static String PROJECT_NAME = "ProjectName";
  private final static String PROJECT_NOTES = "ProjectNotes";
  private final static String PROJECT_FILE_NAME = "ProjectFileName";
  private final static String PROJECT_DIRECTORY = "ProjectDirectory";
  private final static String PARAMETER_DIRECTORY = "ParameterDirectory";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public NewProjectParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    ProjectName = null;
    ProjectNotes = null;
    ProjectFileName = null;
    ProjectDirectory = null;
    ParameterDirectory = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NewProjectParameter(String pProjectName,
  String pProjectNotes, String pProjectFileName,
  String pProjectDirectory, String pParameterDirectory) {
    
    this();
    
    ProjectName = pProjectName;
    ProjectNotes = pProjectNotes;
    ProjectFileName = pProjectFileName;
    ProjectDirectory = pProjectDirectory;
    ParameterDirectory = pParameterDirectory;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getProjectName() {
    return ProjectName; }
  public String getProjectNotes() {
    return ProjectNotes; }
  public String getProjectFileName() {
    return ProjectFileName; }
  public String getProjectDirectory() {
    return ProjectDirectory; }
  public String getParameterDirectory() {
    return ParameterDirectory; }
  
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
  /* ########## interface ScriptableTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public org.jdom.Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(PROJECT_NAME, ProjectName);
    ParameterAttributes.put(PROJECT_NOTES, ProjectNotes);
    ParameterAttributes.put(PROJECT_FILE_NAME, ProjectFileName);
    ParameterAttributes.put(PROJECT_DIRECTORY, ProjectDirectory);
    ParameterAttributes.put(PARAMETER_DIRECTORY, ParameterDirectory);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    ProjectName = (String)ParameterAttributes.get(PROJECT_NAME);
    ProjectNotes = (String)ParameterAttributes.get(PROJECT_NOTES);
    ProjectFileName = (String)ParameterAttributes.get(PROJECT_FILE_NAME);
    ProjectDirectory = (String)ParameterAttributes.get(PROJECT_DIRECTORY);
    ParameterDirectory = (String)ParameterAttributes.get(PARAMETER_DIRECTORY);
    
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