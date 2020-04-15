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

package org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class VectorizeTextUnitsParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected int Iteration = 1;
  protected String ThesaurusFileName = null;
  protected String VectorFileName = null;
  protected int FileType = CSV_FILE;
  protected int DescriptorFrequency = RAW_DESCRIPTOR_FREQUENCY;
  protected int VectorDimensions = ALL_DESCRIPTORS;
  protected String DescriptorsScopeNotesContain = null;
  protected boolean CreateFileForTermAssociationDiscovery = false;
  protected boolean CreateMetaDataFile = false;
  protected int WeightsMode = CREATE_DESCRIPTOR_WEIGHTS_FILE_EQUAL;
  protected String WeightsFileName = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits"
  + ".VectorizeTextUnitsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits"
  + ".VectorizeTextUnitsParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String ITERATION =
  "Iteration";
  private final static String THESAURUS_FILE_NAME =
  "ThesaurusFileName";
  private final static String VECTOR_FILE_NAME =
  "VectorFileName";
  private final static String FILE_TYPE =
  "FileType";
  private final static String TERM_FREQUENCY =
  "DescriptorFrequency";
  private final static String VECTOR_DIMENSIONS =
  "VectorDimensions";
  private final static String DESCRIPTORS_SCOPE_EQUALTES_CONTAIN =
  "DescriptorsScopeNotesContain";
  private final static String CREATE_FILE_FOR_DESCRIPTOR_ASSOCIATION_DISCOVERY =
  "CreateFileForTermAssociationDiscovery";
  private final static String CREATE_META_DATA_FILE =
  "CreateMetaDataFile";
  private final static String WEIGHTS_MODE =
  "WeightsMode";
  private final static String WEIGHTS_FILE_NAME =
  "WeightsFileName";
  
  public final static String[] VECTOR_DIMENSIONS_OPTIONS = {
    "All Descriptors in Thesaurus", 
    "Descriptors whose Scope Notes Contain String", 
    "Descriptors whose Scope Notes Don't Contain String" 
  };
  public final static int ALL_DESCRIPTORS = 0;
  public final static int SPECIFIED_DESCRIPTORS = 1;
  public final static int NOT_SPECIFIED_DESCRIPTORS = 2;

  public final static int CSV_FILE = 0;
  public final static int TXT_FILE = 1;
  public final static int ARFF_FILE = 2;
  public final static int SPARSE_ARFF_FILE = 3;
  public final static String[] VECTOR_FILE_FORMAT_OPTIONS = {
    "CSV: Comma Separated Values", 
    "TXT: Fixed Width Values",
    "ARFF: Weka Data Mining Project",
    "Sparse ARFF: Weka Data Mining Project"
  };

  public final static int RAW_DESCRIPTOR_FREQUENCY = 0;
  public final static int BOOLEAN_DESCRIPTOR_FREQUENCY = 1;
  public final static String[] DESCRIPTOR_FREQUENCY_OPTIONS = {
    "Raw Descriptor Frequency", 
    "Boolean Descriptor Frequency" 
  };

  public final static int CREATE_DESCRIPTOR_WEIGHTS_FILE_EQUAL = 0;
  public final static int CREATE_DESCRIPTOR_WEIGHTS_FILE_IDF = 1;
  public final static int APPLY_DESCRIPTOR_WEIGHTS_FILE = 2;
  public final static String[] WEIGHTS_MODES = {
    "Create Descriptor Weights File: Equals Weights",
    "Create Descriptor Weights File: IDF Weights",
    "Apply Existing Descriptor Weights File."
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public VectorizeTextUnitsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    Iteration = 1;
    ThesaurusFileName = null;
    VectorFileName = null;
    FileType = CSV_FILE;
    DescriptorFrequency = RAW_DESCRIPTOR_FREQUENCY;
    VectorDimensions = ALL_DESCRIPTORS;
    DescriptorsScopeNotesContain = null;
    CreateFileForTermAssociationDiscovery = false;
    CreateMetaDataFile = false;
    WeightsMode = CREATE_DESCRIPTOR_WEIGHTS_FILE_EQUAL;
    WeightsFileName = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public VectorizeTextUnitsParameter(String pCollectionFileName,
    int pIteration, String pThesaurusFileName, 
    String pVectorFileName, String pFileType,
    String pDescriptorFrequency, 
    String pVectorDimensions, String pDescriptorsScopeNotesContain,
    boolean pCreateFileForTermAssociationDiscovery,
    boolean pCreateMetaDataFile, String pWeightsMode,
    String pWeightsFileName) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    Iteration = pIteration;
    ThesaurusFileName = pThesaurusFileName;
    VectorFileName = pVectorFileName;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    CreateFileForTermAssociationDiscovery = pCreateFileForTermAssociationDiscovery;
    CreateMetaDataFile = pCreateMetaDataFile;
    WeightsFileName = pWeightsFileName;

    if (pVectorDimensions.equals(VECTOR_DIMENSIONS_OPTIONS[ALL_DESCRIPTORS])) 
      VectorDimensions = ALL_DESCRIPTORS;
    else if (pVectorDimensions.equals(VECTOR_DIMENSIONS_OPTIONS[SPECIFIED_DESCRIPTORS])) 
      VectorDimensions = SPECIFIED_DESCRIPTORS;
    else
      VectorDimensions = NOT_SPECIFIED_DESCRIPTORS;
    
    if (pFileType.equals(VECTOR_FILE_FORMAT_OPTIONS[CSV_FILE])) 
      FileType = CSV_FILE;
    else if (pFileType.equals(VECTOR_FILE_FORMAT_OPTIONS[TXT_FILE]))
      FileType = TXT_FILE;
    else if (pFileType.equals(VECTOR_FILE_FORMAT_OPTIONS[ARFF_FILE]))
      FileType = ARFF_FILE;
    else if (pFileType.equals(VECTOR_FILE_FORMAT_OPTIONS[SPARSE_ARFF_FILE]))
      FileType = SPARSE_ARFF_FILE;
    else 
      FileType = ARFF_FILE;

    if (pDescriptorFrequency.equals(DESCRIPTOR_FREQUENCY_OPTIONS[RAW_DESCRIPTOR_FREQUENCY])) 
      DescriptorFrequency = RAW_DESCRIPTOR_FREQUENCY;
    else 
      DescriptorFrequency = BOOLEAN_DESCRIPTOR_FREQUENCY;

    if (pWeightsMode.equals(WEIGHTS_MODES[CREATE_DESCRIPTOR_WEIGHTS_FILE_EQUAL])) 
      WeightsMode = CREATE_DESCRIPTOR_WEIGHTS_FILE_EQUAL;
    else if (pWeightsMode.equals(WEIGHTS_MODES[CREATE_DESCRIPTOR_WEIGHTS_FILE_IDF]))
      WeightsMode = CREATE_DESCRIPTOR_WEIGHTS_FILE_IDF;
    else
      WeightsMode = APPLY_DESCRIPTOR_WEIGHTS_FILE;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public VectorizeTextUnitsParameter(String pCollectionFileName,
    int pIteration, String pThesaurusFileName, 
    String pVectorFileName, int pFileType,
    int pDescriptorFrequency, 
    int pVectorDimensions, String pDescriptorsScopeNotesContain,
    boolean pCreateFileForTermAssociationDiscovery,
    boolean pCreateMetaDataFile, int pWeightsMode,
    String pWeightsFileName) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    Iteration = pIteration;
    ThesaurusFileName = pThesaurusFileName;
    VectorFileName = pVectorFileName;
    FileType = pFileType;
    DescriptorFrequency = pDescriptorFrequency;
    VectorDimensions = pVectorDimensions;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    CreateFileForTermAssociationDiscovery = pCreateFileForTermAssociationDiscovery;
    CreateMetaDataFile = pCreateMetaDataFile;
    WeightsMode = pWeightsMode;
    WeightsFileName = pWeightsFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() { 
    return CollectionFileName; }
  public int getIteration() { 
    return Iteration; }
  public String getThesaurusFileName() { 
    return ThesaurusFileName; }
  public String getVectorFileName() { 
    return VectorFileName; }
  public int getFileType() { 
    return FileType; }
  public int getDescriptorFrequency() { 
    return DescriptorFrequency; }
  public int getVectorDimensions() { 
    return VectorDimensions; }
  public String getDescriptorsScopeNotesContain() { 
    return DescriptorsScopeNotesContain; }
  public boolean getCreateFileForTermAssociationDiscovery() { 
    return CreateFileForTermAssociationDiscovery; }
  public boolean createFileForTermAssociationDiscovery() { 
    return CreateFileForTermAssociationDiscovery; }
  public boolean getCreateMetaDataFile() { 
    return CreateMetaDataFile; }
  public boolean createMetaDataFile() { 
    return CreateMetaDataFile; }
  public int getWeightsMode() { 
    return WeightsMode; }
  public String getWeightsFileName() { 
    return WeightsFileName; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCreateFileForTermAssociationDiscovery(
  boolean pCreateFileForTermAssociationDiscovery) { 
    CreateFileForTermAssociationDiscovery =
    pCreateFileForTermAssociationDiscovery; } 
  public void setCreateMetaDataFile(boolean pCreateMetaDataFile) { 
    CreateMetaDataFile = pCreateMetaDataFile; }
   
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
    ParameterAttributes.put(COLLECTION_FILE_NAME, 
    CollectionFileName);
    ParameterAttributes.put(ITERATION, 
    Tools.int2String(Iteration));
    ParameterAttributes.put(THESAURUS_FILE_NAME, 
    ThesaurusFileName);
    ParameterAttributes.put(VECTOR_FILE_NAME, 
    VectorFileName);
    ParameterAttributes.put(FILE_TYPE, 
    Tools.int2String(FileType));
    ParameterAttributes.put(TERM_FREQUENCY, 
    Tools.int2String(DescriptorFrequency));
    ParameterAttributes.put(VECTOR_DIMENSIONS, 
    Tools.int2String(VectorDimensions));
    ParameterAttributes.put(DESCRIPTORS_SCOPE_EQUALTES_CONTAIN, 
    DescriptorsScopeNotesContain);
    ParameterAttributes.put(CREATE_FILE_FOR_DESCRIPTOR_ASSOCIATION_DISCOVERY, 
    Tools.boolean2String(CreateFileForTermAssociationDiscovery));
    ParameterAttributes.put(CREATE_META_DATA_FILE, 
    Tools.boolean2String(CreateMetaDataFile));
    ParameterAttributes.put(WEIGHTS_MODE, 
    Tools.int2String(WeightsMode));
    ParameterAttributes.put(WEIGHTS_FILE_NAME, 
    WeightsFileName);
    
  return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes.get(COLLECTION_FILE_NAME);
    Iteration = Tools.string2Int(
    (String)ParameterAttributes.get(ITERATION));
    ThesaurusFileName = (String)ParameterAttributes.get(THESAURUS_FILE_NAME);
    VectorFileName = (String)ParameterAttributes.get(VECTOR_FILE_NAME);
    FileType = Tools.string2Int(
    (String)ParameterAttributes.get(FILE_TYPE));
    DescriptorFrequency = Tools.string2Int(
    (String)ParameterAttributes.get(TERM_FREQUENCY));
    VectorDimensions = Tools.string2Int(
    (String)ParameterAttributes.get(VECTOR_DIMENSIONS));
    DescriptorsScopeNotesContain = (String)ParameterAttributes
    .get(DESCRIPTORS_SCOPE_EQUALTES_CONTAIN);
    CreateFileForTermAssociationDiscovery = Tools.string2Boolean(
    (String)ParameterAttributes.get(CREATE_FILE_FOR_DESCRIPTOR_ASSOCIATION_DISCOVERY));
    CreateMetaDataFile = Tools.string2Boolean(
    (String)ParameterAttributes.get(CREATE_META_DATA_FILE));
    WeightsMode = Tools.string2Int(
    (String)ParameterAttributes.get(WEIGHTS_MODE));
    WeightsFileName = (String)ParameterAttributes
    .get(WEIGHTS_FILE_NAME);
    
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