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

package org.hypknowsys.diasdem.tasks.postprocess.tagDocuments;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class TagDocumentsParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String DtdFileName = null;
  protected double RandomSampleSize = 0.0;
  protected String RandomSampleFileName = null;
  protected boolean CreateTagByDocumentFile = false;
  protected boolean CreateWumFiles = false;
  protected boolean CreateGateFiles = false;
  protected String GateDirectory = null;
  protected String XmlDocumentsDirectory = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagDocuments.TagDocumentsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagDocuments.TagDocumentsParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String DTD_FILE_NAME =
  "DtdFileName";
  private final static String RANDOM_SAMPLE_SIZE =
  "RandomSampleSize";
  private final static String RANDOM_SAMPLE_FILE_NAME =
  "RandomSampleFileName";
  private final static String CREATE_TAG_BY_DOCUMENT_FILE =
  "CreateTagByDocumentFile";
  private final static String CREATE_WUM_FILES =
  "CreateWumFiles";
  private final static String CREATE_GATE_FILES =
  "CreateGateFiles";
  private final static String GATE_DIRECTORY =
  "GateDirectory";
  private final static String XML_DOCUMENTS_DIRECTORY =
  "XmlDocumentsDirectory";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagDocumentsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;

    CollectionFileName = null;
    DtdFileName = null;
    RandomSampleSize = 0.0;
    RandomSampleFileName = null;
    CreateTagByDocumentFile = false;
    CreateWumFiles = false;
    CreateGateFiles = false;
    GateDirectory = null;
    XmlDocumentsDirectory = null;

    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagDocumentsParameter(String pCollectionFileName, 
    String pDtdFileName, double pRandomSampleSize, 
    String pRandomSampleFileName, 
    boolean pCreateTagByDocumentFile, boolean pCreateWumFiles, 
    boolean pCreateGateFiles, String pGateDirectory, 
    String pXmlDocumentsDirectory) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    DtdFileName = pDtdFileName;
    RandomSampleSize = pRandomSampleSize;
    RandomSampleFileName = pRandomSampleFileName;
    CreateTagByDocumentFile = pCreateTagByDocumentFile;
    CreateWumFiles = pCreateWumFiles;
    CreateGateFiles = pCreateGateFiles;
    GateDirectory = pGateDirectory;
    XmlDocumentsDirectory = pXmlDocumentsDirectory;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() { 
    return CollectionFileName; }
  public String getDtdFileName() { 
    return DtdFileName; }
  public double getRandomSampleSize() { 
    return RandomSampleSize; }
  public String getRandomSampleFileName() { 
    return RandomSampleFileName; }
  public boolean createTagByDocumentFile() { 
    return CreateTagByDocumentFile; }
  public boolean createWumFiles() { 
    return CreateWumFiles; }
  public boolean createGateFiles() { 
    return CreateGateFiles; }
  public String getGateDirectory() { 
    return GateDirectory; }
  public String getXmlDocumentsDirectory() { 
    return XmlDocumentsDirectory; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCreateTagByDocumentFile(boolean pCreateTagByDocumentFile) {
    CreateTagByDocumentFile = pCreateTagByDocumentFile; }
  public void setCreateWumFiles(boolean pCreateWumFiles) {
    CreateWumFiles = pCreateWumFiles; }
  public void setCreateGateFile(boolean pCreateGateFiles) {
    CreateGateFiles = pCreateGateFiles; }

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
    ParameterAttributes.put(DTD_FILE_NAME, DtdFileName);
    ParameterAttributes.put(RANDOM_SAMPLE_SIZE, 
    Tools.double2String(RandomSampleSize));
    ParameterAttributes.put(RANDOM_SAMPLE_FILE_NAME, RandomSampleFileName);
    ParameterAttributes.put(CREATE_TAG_BY_DOCUMENT_FILE, 
    Tools.boolean2String(CreateTagByDocumentFile));
    ParameterAttributes.put(CREATE_WUM_FILES, 
    Tools.boolean2String(CreateWumFiles));
    ParameterAttributes.put(CREATE_GATE_FILES, 
    Tools.boolean2String(CreateGateFiles));
    ParameterAttributes.put(GATE_DIRECTORY, GateDirectory);
    ParameterAttributes.put(XML_DOCUMENTS_DIRECTORY, XmlDocumentsDirectory);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    DtdFileName = (String)ParameterAttributes
    .get(DTD_FILE_NAME);
    RandomSampleSize = Tools.string2Double(
    (String)ParameterAttributes.get(RANDOM_SAMPLE_SIZE));
    RandomSampleFileName = (String)ParameterAttributes
    .get(RANDOM_SAMPLE_FILE_NAME);
    CreateTagByDocumentFile = Tools.string2Boolean(
    (String)ParameterAttributes.get(CREATE_TAG_BY_DOCUMENT_FILE));
    CreateWumFiles = Tools.string2Boolean(
    (String)ParameterAttributes.get(CREATE_WUM_FILES));
    CreateGateFiles = Tools.string2Boolean(
    (String)ParameterAttributes.get(CREATE_GATE_FILES));
    GateDirectory = (String)ParameterAttributes
    .get(GATE_DIRECTORY);
    XmlDocumentsDirectory = (String)ParameterAttributes
    .get(XML_DOCUMENTS_DIRECTORY);
    
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