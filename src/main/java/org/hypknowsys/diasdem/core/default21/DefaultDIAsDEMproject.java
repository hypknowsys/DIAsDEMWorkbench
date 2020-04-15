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

package org.hypknowsys.diasdem.core.default21;

import java.io.IOException;
import org.hypknowsys.diasdem.core.DIAsDEMcollection;
import org.hypknowsys.diasdem.core.DIAsDEMproject;
import org.hypknowsys.diasdem.core.DiasdemException;
import org.hypknowsys.misc.util.KProperties;
import org.hypknowsys.misc.util.KProperty;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMproject extends KProperties
implements DIAsDEMproject {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final KProperty[] MY_PROPERTY_DATA = {
    new KProperty("PROJECT_NAME", "Project Name",
    "<DefaultProjectName>", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("PROJECT_NOTES", "Project Notes",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("PROJECT_FILE_NAME",
    "Absolute File Name of Project File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("PROJECT_DIRECTORY",
    "Absolute File Name of Project Directory",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("PARAMETER_DIRECTORY",
    "Absolute File Name of Parameter Directory",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_TEXT_FILE_DIRECTORY",
    "Default Directory of Source Text Files",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_COLLECTION_DIRECTORY",
    "Default Collection Directory",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_FILE_BUFFER_SIZE",
    "Default FileBuffer Size in Bytes (e.g., 1024000 = 1 MB)",
    "1024000", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("NEEX20_DEFAULT_FORENAMES_FILE",
    "NEEX: Default Forenames File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_SURNAMES_FILE",
    "NEEX: Default Surnames File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_SURNAME_SUFFIXES_FILE",
    "NEEX: Default Surname Suffixes File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_MIDDLE_INITIALS_FILE",
    "NEEX: Default Middle Initials File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_TITLES_FILE",
    "NEEX: Default Titles File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_PLACES_FILE",
    "NEEX: Default Places File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_ORGANIZATIONS_START_FILE",
    "NEEX: Default Organizations Start File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_ORGANIZATIONS_END_FILE",
    "NEEX: Default Organizations End File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_COMPOSITE_NE_FILE",
    "NEEX: Default Composite NE File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_REGEX_NE_FILE",
    "NEEX: Default Regex NE File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_COLLECTION_FILE",
    "Default Collection File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("TREE_TAGGER_COMMAND",
    "TreeTagger: Command",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_ABBREVIATIONS_FILE",
    "Default Abbreviations File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_FULL_STOP_REGEX_FILE",
    "Default Full Stop Regex File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_TOKENIZE_REGEX_FILE",
    "Default Tokenize Regex File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_NORMALIZE_REGEX_FILE",
    "Default Normalize Regex File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_MULTI_TOKEN_WORDS_FILE",
    "Default Multi Token Words File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_TOKEN_REPLACEMENT_FILE",
    "Default Token Replacement File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("NEEX20_DEFAULT_INITIAL_COMPOSITE_NE_FILE",
    "NEEX: Default Initial Composite NE File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX20_DEFAULT_BASIC_NE_FILE",
    "NEEX: Default Composite NE File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_STOPWORD_FILE",
    "Default Stopword File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_TREETAGGER_INPUT_FILE",
    "Default TreeTagger Input File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_TREETAGGER_OUTPUT_FILE",
    "Default TreeTagger Output File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_KNOWN_LEMMA_FORMS_FILE",
    "Default Known Lemma Forms File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_UNKNOWN_LEMMA_FORMS_FILE",
    "Default Unknown Lemma Forms File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_WORD_STATISTICS_FILE",
    "Default Word Statistics File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_TEXT_UNIT_VECTORS_FILE",
    "Default Text Unit Vectors File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_THESAURUS_FILE",
    "Default Thesaurus File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_VECTOR_FILE_FORMAT_INDEX",
    "Default Vector File Format Index",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_CLUSTER_RESULT_FILE",
    "Default Cluster Result File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_ITERATION",
    "Default Iteration (1, 2, ...)",
    "1", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX",
    "Default Text Unit Descriptors Index",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_CONTAIN",
    "Default Text Unit Descriptors Contain",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_DESCRIPTOR_FREQUENCY_INDEX",
    "Default Term Frequeny Index",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_WEKA_SIMPLE_KMEANS_FILE",
    "Default Weka Simple K-Means File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_NUMBER_OF_CLUSTERS",
    "Default Number of Clusters (Weka)",
    "50", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DEFAULT_CLUSTERING_ALGORITHM_INDEX",
    "Default Clustering Algorithm Index",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_CLUSTERING_MODE_INDEX",
    "Default Clustering Mode Index",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_ACUITY",
    "Default Acuity (Weka: 1.0)",
    "0.5", KProperty.DOUBLE, KProperty.EDITABLE),
    new KProperty("DEFAULT_CUTOFF",
    "Default Cutoff (Weka)",
    "0.002", KProperty.DOUBLE, KProperty.EDITABLE),
    new KProperty("DEFAULT_MAX_ITERATIONS",
    "Default Maximum Number of Iterations (Weka)",
    "100", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DEFAULT_RANDOM_NUMBER_SEED",
    "Default Random Number Seed (Weka)",
    "123456", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DEFAULT_MIN_STD_DEVIATION",
    "Default Min. Standard Devition (Weka)",
    "0.5", KProperty.DOUBLE, KProperty.EDITABLE),
    new KProperty("DEFAULT_RESULT_FILE_FORMAT_INDEX",
    "Default Result File Format Index",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_CLUSTER_DIRECTORY",
    "Default Cluster Visualization Directory",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_CLUSTER_LABEL_FILE",
    "Default Semantic Cluster Labels File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_IGNORE_FIRST_LINE_IN_CLUSTER_RESULT_FILE",
    "Default Setting: Ignore First Line in Cluster Result File",
    "false", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("DEFAULT_MAX_CLUSTER_ID",
    "Default Maximum Cluster ID",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_MIN_CARDINALITY",
    "Default Min. Cardinality (Cluster Quality)",
    "50", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_MAX_DISTINCT_RATIO",
    "Default Max. Distinct Ratio (Cluster Quality)",
    "0.75", KProperty.DOUBLE, KProperty.EDITABLE),
    new KProperty("DEFAULT_MIN_FREQUENT_RATIO",
    "Default Min. Frequent Ratio (Cluster Quality)",
    "0.25", KProperty.DOUBLE, KProperty.EDITABLE),
    new KProperty("DEFAULT_PRELIMINARY_DTD_FILE",
    "Default DIAsDEM Preliminary DTD File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_DTD_ROOT_ELEMENT",
    "Default DTD Root Element",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_MIN_ATTRIBUTE_SUPPORT",
    "Default Min. Attribute Support (DTD Derivation)",
    "0.1", KProperty.DOUBLE, KProperty.EDITABLE),
    new KProperty("DEFAULT_TEXT_UNIT_SAMPLE_SIZE",
    "Default Size of Random Text Unit Sample",
    "0.05", KProperty.DOUBLE, KProperty.EDITABLE),
    new KProperty("DEFAULT_TEXT_UNIT_SAMPLE_FILE",
    "Default Text Unit Sample File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_REMAINING_TEXT_UNIT_SAMPLE_FILE",
    "Default Remaining Text Unit Sample File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_EVALUATED_TEXT_UNIT_SAMPLE_FILE",
    "Default Evaluated Text Unit Sample File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_DATABASE_TYPE_INDEX",
    "Default Database Type Index",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_DATABASE_USER",
    "Default Database User Name",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_DATABASE_PASSWORD",
    "Default Database Password",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_SQL_SCRIPT_DIRECTORY",
    "Default SQL Script Directory",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_LEMMATIZATION_ALGORITHM_INDEX",
    "Default Lemmatization Algorithm Index",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_EXPORT_GATE_DOCUMENTS",
    "Export GATE Documents with Semantic und Structural Annotations",
    "false", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("DEFAULT_EXPORT_GATE_DIRECTORY",
    "Default Directory of Exported GATE Documents",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_CREATE_HTML_SAMPLE_DIRECTORY",
    "Default Directory of Random XML Document Sample HTML Files",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_XML_DOCUMENTS_DIRECTORY",
    "Default Directory of Semantically Tagged XML Documents",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_TEXT_UNIT_EVALUATION_LOG_FILE",
    "Default Log File for Text Unit Evaluation",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("MRU_BATCH_SCRIPT_FILE_NAME",
    "Most Recently Used DIAsDEM Batch Script File Name",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_DIASDEM_COLLECTION_CLASS_NAME",
    "Class Name of Default DIAsDEMcollection Implementation",
    "org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMcollection",
    KProperty.STRING, KProperty.EDITABLE),
    new KProperty("PROCESSED_TEXT_UNITS_ROLLBACK_OPTION",
    "Rollback Option (0, 1, 2) for ProcessedTextUnits in DIAsDEM Documents",
    "1", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("ACTIVE_TEXT_UNITS_LAYER_INDEX",
    "Index of Default Active Text Units Layer in DIAsDEM Documents",
    "0", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("MAX_FILES_PER_DIRECTORY",
    "Maximum Files per Directory",
    "1000", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DEFAULT_DOCUMENT_SAMPLE_FILE",
    "Default Document Sample File",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_DOCUMENT_SAMPLE_SIZE",
    "Default Size of Random Document Sample",
    "0.05", KProperty.DOUBLE, KProperty.EDITABLE),
    new KProperty("DEFAULT_SAMPLING_MODE_INDEX",
    "Default Sampling Mode Index: Create or Apply Sample File?",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_DESCRIPTOR_WEIGHTS_INDEX",
    "Default Descriptor Weights Index: Create or Apply Weights File?",
    "", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_DESCRIPTOR_WEIGHTS_FILE",
    "Default Descriptor Weights File",
    "", KProperty.STRING, KProperty.EDITABLE)
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMproject() {
    
    super(MY_PROPERTY_DATA);
    FileName = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMproject(String pFileName, int pTask) throws IOException {
    
    super(MY_PROPERTY_DATA);
    
    FileName = pFileName;
    if (pTask == CREATE) {
      this.save(FileName);
    }
    else if (pTask == LOAD) {
      if (Tools.isExistingFile(pFileName)) {
        this.load(FileName);
      }
      else {
        throw new IOException();
      }
    }
    else {
      throw new IOException();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getProjectName() {
    return this.getStringProperty("PROJECT_NAME"); }
  public String getProjectNotes() {
    return this.getStringProperty("PROJECT_NOTES"); }
  public String getProjectFileName() {
    return this.getStringProperty("PROJECT_FILE_NAME"); }
  public String getProjectDirectory() {
    return this.getStringProperty("PROJECT_DIRECTORY"); }
  public String getParameterDirectory() {
    return this.getStringProperty("PARAMETER_DIRECTORY"); }
  public String getDefaultDiasdemCollectionClassName() {
    return this.getStringProperty("DEFAULT_DIASDEM_COLLECTION_CLASS_NAME"); }
  public int getProcessedTextUnitsRollbackOption() {
    return this.getIntProperty("PROCESSED_TEXT_UNITS_ROLLBACK_OPTION"); }
  public int getActiveTextUnitsLayerIndex() {
    return this.getIntProperty("ACTIVE_TEXT_UNITS_LAYER_INDEX"); }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setProjectName(String pProjectName) {
    this.setProperty("PROJECT_NAME", pProjectName);
    this.quickSave();
  }
  public void setProjectNotes(String pProjectNotes) {
    this.setProperty("PROJECT_NOTES", pProjectNotes);
    this.quickSave();
  }
  public void setProjectFileName(String pProjectFileName) {
    this.setProperty("PROJECT_FILE_NAME", pProjectFileName);
    FileName = pProjectFileName;
    this.quickSave();
  }
  public void setProjectDirectory(String pProjectDirectory) {
    this.setProperty("PROJECT_DIRECTORY", pProjectDirectory);
    this.quickSave();
  }
  public void setParameterDirectory(String pParameterDirectory) {
    this.setProperty("PARAMETER_DIRECTORY", pParameterDirectory);
    this.quickSave();
  }
  public void setDefaultDiasdemCollectionClassName(
  String pDefaultDiasdemCollectionClassName) {
    this.setProperty("DEFAULT_DIASDEM_COLLECTION_CLASS_NAME",
    pDefaultDiasdemCollectionClassName);
    this.quickSave();
  }
  public void setProcessedTextUnitsRollbackOption(
  int pProcessedTextUnitsRollbackOption) {
    this.setIntProperty("PROCESSED_TEXT_UNITS_ROLLBACK_OPTION",
    pProcessedTextUnitsRollbackOption);
    this.quickSave();
  }
  public void setActiveTextUnitsLayerIndex(
  int pActiveTextUnitsLayerIndex) {
    this.setIntProperty("ACTIVE_TEXT_UNITS_LAYER_INDEX",
    pActiveTextUnitsLayerIndex);
    this.quickSave();
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(100);
    TmpStringBuffer.append("DIAsDEM Project: Name=");
    TmpStringBuffer.append(this.getProjectName());
    TmpStringBuffer.append("; FileName=");
    TmpStringBuffer.append(this.getProjectFileName());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface DIAsDEMproject methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMcollection instantiateDefaultDiasdemCollection()
  throws DiasdemException {
    
    try {
      return (DIAsDEMcollection)Class.forName(
      this.getDefaultDiasdemCollectionClassName())
      .getConstructor(null).newInstance(null);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw new DiasdemException("Error: The default DIAsDEMcollection "
      + "implementation " + this.getDefaultDiasdemCollectionClassName()
      + " cannot be instantiated! Message: " + e.getMessage());
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void quickSave() {
    
    if (FileName != null && FileName.length() > 0) {
      super.quickSave();
    }
    
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
  
  public static void main(String pOptions[]) {}
  
}