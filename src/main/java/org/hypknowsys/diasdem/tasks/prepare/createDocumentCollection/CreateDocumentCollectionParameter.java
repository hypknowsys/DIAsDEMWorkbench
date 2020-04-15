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

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class CreateDocumentCollectionParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionName = null;
  protected String CollectionFileName = null;
  protected String CollectionDirectory = null;
  protected String CollectionNotes = null;
  protected int DocumentsPerVolume = 1;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection.CreateDocumentCollectionTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection.CreateDocumentCollectionParameterPanel";
  
  private final static String COLLECTION_NAME =
  "CollectionName";
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String COLLECTION_DIRECTORY =
  "CollectionDirectory";
  private final static String COLLECTION_NOTES =
  "CollectionNotes";
  private final static String DOCUMENTS_PER_VOLUME =
  "DocumentsPerVolume";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateDocumentCollectionParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionName = null;
    CollectionFileName = null;
    CollectionDirectory = null;
    CollectionNotes = null;
    DocumentsPerVolume = 1;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateDocumentCollectionParameter(String pCollectionName,
  String pCollectionFileName, String pCollectionDirectory,
  String pCollectionNotes, int pDocumentsPerVolume) {
    
    this();
    
    CollectionName = pCollectionName;
    CollectionFileName = pCollectionFileName;
    CollectionDirectory = pCollectionDirectory;
    CollectionNotes = pCollectionNotes;
    DocumentsPerVolume = Math.max(1, pDocumentsPerVolume);

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionName() {
    return CollectionName; }
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getCollectionDirectory() {
    return CollectionDirectory; }
  public String getCollectionNotes() {
    return CollectionNotes; }
  public int getDocumentsPerVolume() {
    return DocumentsPerVolume; }
  
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
  /* ########## interface ScriptableTaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public org.jdom.Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(COLLECTION_NAME, CollectionName);
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(COLLECTION_DIRECTORY, CollectionDirectory);
    ParameterAttributes.put(COLLECTION_NOTES, CollectionNotes);
    ParameterAttributes.put(DOCUMENTS_PER_VOLUME, 
    Tools.int2String(DocumentsPerVolume));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionName = (String)ParameterAttributes
    .get(COLLECTION_NAME);
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    CollectionDirectory = (String)ParameterAttributes
    .get(COLLECTION_DIRECTORY);
    CollectionNotes = (String)ParameterAttributes
    .get(COLLECTION_NOTES);
    DocumentsPerVolume = Tools.string2Int(
    (String)ParameterAttributes.get(DOCUMENTS_PER_VOLUME));
    
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