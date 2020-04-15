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

package org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ImportReuters21578FilesParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String SourceDirectory = null;
  protected String FileNameFilter = null;
  protected boolean IncludeSubdirectories = false;
  protected String TopicOfCollection = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files"
  + ".ImportReuters21578FilesTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files"
  + ".ImportReuters21578FilesParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String SOURCE_DIRECTORY =
  "SourceDirectory";
  private final static String FILE_NAME_FILTER =
  "FileNameFilter";
  private final static String INCLUDE_SUBDIRECTORIES =
  "IncludeSubdirectories";
  private final static String TOPIC_OF_COLLECTION =
  "TopicOfCollection";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportReuters21578FilesParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    SourceDirectory = null;
    FileNameFilter = null;
    IncludeSubdirectories = false;
    TopicOfCollection = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportReuters21578FilesParameter(String pCollectionFileName, 
  String pSourceDirectory, String pFileNameFilter, 
  boolean pIncludeSubdirectories, String pTopicOfCollection) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    SourceDirectory = pSourceDirectory;
    FileNameFilter = pFileNameFilter;
    IncludeSubdirectories = pIncludeSubdirectories;
    TopicOfCollection = pTopicOfCollection;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() { 
    return CollectionFileName; }
  public String getSourceDirectory() {
    return SourceDirectory; }
  public String getFileNameFilter() {
    return FileNameFilter; }
  public boolean getIncludeSubdirectories() {
    return IncludeSubdirectories; }
  public boolean includeSubdirectories() {
    return IncludeSubdirectories; }
  public String getTopicOfCollection() {
    return TopicOfCollection; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setIncludeSubdirectories(boolean pIncludeSubdirectories) {
    IncludeSubdirectories = pIncludeSubdirectories; }

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
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(SOURCE_DIRECTORY, SourceDirectory);
    ParameterAttributes.put(FILE_NAME_FILTER, FileNameFilter);
    ParameterAttributes.put(INCLUDE_SUBDIRECTORIES, 
    Tools.boolean2String(IncludeSubdirectories));
    ParameterAttributes.put(TOPIC_OF_COLLECTION, TopicOfCollection);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes.get(COLLECTION_FILE_NAME);
    SourceDirectory = (String)ParameterAttributes.get(SOURCE_DIRECTORY);
    FileNameFilter = (String)ParameterAttributes.get(FILE_NAME_FILTER);
    IncludeSubdirectories = Tools.string2Boolean(
    (String)ParameterAttributes.get(INCLUDE_SUBDIRECTORIES));
    TopicOfCollection = (String)ParameterAttributes.get(TOPIC_OF_COLLECTION);
    
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