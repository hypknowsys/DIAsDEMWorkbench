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

package org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits22;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.hypknowsys.diasdem.server.DiasdemScriptableNonBlockingTask;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.KProperty;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.*;

/**
 * @version 2.2, 20 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class VectorizeTextUnitsTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private VectorizeTextUnitsParameter CastParameter = null;

  private long MaxProgress = 0;
  private int CounterProgress = 0;
  private TextFile AssociationFile = null;
  private TextFile MetaDataFile = null;
  private String[] DescriptorWords = null;
  private int GlobalSentenceID = 0;
  private int GlobalDocumentID = 1;
  private int SentenceCounter = 0;
  private long NumberOfUntaggedTextUnits = 0;

  private int TextUnitCounter = 0;
  private int NumberOfDescriptors = 0;
  private int[] DescriptorDocumentFrequencies = null;
  private String[] Descriptors = null;
  
  private DIAsDEMthesaurus thesaurus = null;
  private DIAsDEMthesaurus descriptors = null;
  private HashMap mappingTable = null;
  private DIAsDEMthesaurus currentSentence = null;
  private DIAsDEMthesaurus descriptorWeights = null;
  
  private DIAsDEMthesaurusTerm currentTerm = null;
  private DIAsDEMthesaurusTerm correspondingDescriptor = null;
  private StringTokenizer tokenizer = null;
  private String clusterName = null;
  private String currentWord = null;
  private String[] currentVector = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient DIAsDEMthesaurusTerm TmpTerm = null;
  
  private transient StringBuffer TmpVectorBuffer = null;
  private transient StringBuffer TmpAssocBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Vectorize Text Units 2.2";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits22"
  + ".VectorizeTextUnitsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits22"
  + ".VectorizeTextUnitsResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits22"
  + ".VectorizeTextUnitsControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("VECTORIZE_TEXT_UNITS_22:_LENGTH_NORMALIZATION_INDEX", 
    "Vectorize Text Units 2.2: Default Setting: Length Normalization",
    "0", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("VECTORIZE_TEXT_UNITS_22:_CREATE_ASSOC_RULES_FILE", 
    "Vectorize Text Units 2.2: Default Setting: Create File for Mining "
    + "Descriptor Association Rules",
    "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("VECTORIZE_TEXT_UNITS_22:_CREATE_METADATA_FILE", 
    "Vectorize Text Units 2.2: Default Setting: Create Metadata File for Text "
    + "Unit Vectors File", "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public VectorizeTextUnitsTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
   
    GlobalSentenceID = 10000;
    
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
    
    VectorizeTextUnitsParameter parameter = null;
    if (pParameter instanceof VectorizeTextUnitsParameter) {
      parameter = (VectorizeTextUnitsParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    File file = new File(parameter.getCollectionFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION +
      "-file in the field 'Collection File'!");
    }
    file = new File(parameter.getThesaurusFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION +
      "-file in the field 'Thesaurus File'!");
    }
    if (parameter.getVectorFileName().trim().length() <= 0
    || !parameter.getVectorFileName().trim().endsWith(
    this.getRequiredFileExtension(parameter))) {
      result.addError(
      "Error: Please enter a valid local " +
      this.getRequiredFileExtension(parameter) +
      "-file name\nin the field 'Text Unit Vectors File'!");
    }
    file = new File(parameter.getVectorFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Text Unit Vectors File' currently exists.\n" +
      "Do you really want to replace this file?");
    }
    
    if (parameter.getWeightsMode() !=
    VectorizeTextUnitsParameter.APPLY_DESCRIPTOR_WEIGHTS_FILE) {
      if (parameter.getWeightsFileName().trim().length() <= 0
      || !parameter.getWeightsFileName().trim().endsWith(
      DIAsDEMguiPreferences.COLLECTION_FREQUENCIES_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter a valid local " +
        DIAsDEMguiPreferences.COLLECTION_FREQUENCIES_FILE_EXTENSION +
        "-file name\nin the field 'Collection Frequencies File'!");
      }
      file = new File(parameter.getWeightsFileName());
      if (file.exists()) {
        result.addWarning(
        "Warning: The file specified in the field\n" +
        "'Collection Frequencies File' currently exists.\n" +
        "Do you really want to replace this file?");
      }
    }
    else {
      file = new File(parameter.getWeightsFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.COLLECTION_FREQUENCIES_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        DIAsDEMguiPreferences.COLLECTION_FREQUENCIES_FILE_EXTENSION +
        "-file in the field 'Collection Frequencies File'!");
      }
    }
    
    if (parameter.getIteration() < 1) {
      result.addError(
      "Please enter a non-negative integer\n" +
      "in the field 'KDD Process Iteration'!");
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new VectorizeTextUnitsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new VectorizeTextUnitsResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    DIAsDEMguiMenuBar.ACTIONS_PREPARE_DATA_SET,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof VectorizeTextUnitsParameter) {
      CastParameter = (VectorizeTextUnitsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Text units cannot be vectorized!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    thesaurus = new DefaultDIAsDEMthesaurus();
    thesaurus.load(CastParameter.getThesaurusFileName());
    currentSentence = new DefaultDIAsDEMthesaurus();
    descriptors = new DefaultDIAsDEMthesaurus();
    mappingTable = new HashMap();    

    // create or load descriptor weights
    if (CastParameter.getWeightsMode() == VectorizeTextUnitsParameter
    .APPLY_DESCRIPTOR_WEIGHTS_FILE) {
      descriptorWeights = new DefaultDIAsDEMthesaurus();
      descriptorWeights.load(CastParameter.getWeightsFileName());
    }
    else {
      descriptorWeights = new DefaultDIAsDEMthesaurus("Descriptor Weights "
      + "of Iteration " + CastParameter.getIteration() + " in DIAsDEM Project "
      + DiasdemProject.getProjectName(), 0);
    }
    
    // retrieve all descriptors
    currentTerm = thesaurus.getFirstTerm();
    NumberOfDescriptors = 0;
    while (currentTerm != null) {
      if (currentTerm.isDescriptor() && this.isDescriptor(currentTerm
      .getWord(), thesaurus, CastParameter)) {
        // this descriptor is valid in this iteration
        NumberOfDescriptors++;
        descriptors.countOccurrence(currentTerm.getWord());
        mappingTable.put(currentTerm.getWord(),
        new Integer(NumberOfDescriptors + 2));
        TmpTerm = descriptorWeights.get(currentTerm.getWord());
        if (TmpTerm == null) {
          // add descriptor with default (i.e. equal) weight of 1.0
          descriptorWeights.add(new DefaultDIAsDEMthesaurusTerm(
          descriptorWeights.getNextID(), currentTerm.getWord(), 1.0d));
        }
      }
      else if (currentTerm.isDescriptor()) {
        // this descriptor is not valid in this iteration
        thesaurus.delete(currentTerm.getWord());
      }
      currentTerm = thesaurus.getNextTerm();
    }

    // retrieve all attribute vectors
    currentVector = new String[NumberOfDescriptors + 3];
    DescriptorWords = new String[NumberOfDescriptors + 3];
    currentVector[0] = "DocumentType"; 
    currentVector[1] = "Document"; 
    currentVector[2] = "TextUnit"; 
    DescriptorWords[0] = "DocumentType"; 
    DescriptorWords[1] = "Document"; 
    DescriptorWords[2] = "TextUnit"; 
    currentTerm = descriptors.getFirstTerm();
    while (currentTerm != null) {
      currentVector[ ( (Integer)mappingTable.get( currentTerm.getWord() ) )
        .intValue() ] = currentTerm.getWord() + "";
      DescriptorWords[ ( (Integer)mappingTable.get( currentTerm.getWord() ) )
        .intValue() ] = currentTerm.getWord() + "";
      currentTerm = descriptors.getNextTerm();
    }  
   
    DescriptorDocumentFrequencies = new int[NumberOfDescriptors + 3];
    for (int i = 0; i < DescriptorDocumentFrequencies.length; i++) {
      DescriptorDocumentFrequencies[i] = -1;
    }
    
    CounterProgress = 1;
    if (CastParameter.getWeightsMode() == VectorizeTextUnitsParameter
    .CREATE_DESCRIPTOR_WEIGHTS_FILE_IDF) {
      MaxProgress = DiasdemCollection.getNumberOfDocuments() * 2;
      this.computeInverseDescriptorDocumentFrequencies();
    }
    else {
      MaxProgress = DiasdemCollection.getNumberOfDocuments();
    }
      
    this.createVectors();

    // save descriptor weights if necessary
    if (CastParameter.getWeightsMode() != VectorizeTextUnitsParameter
    .APPLY_DESCRIPTOR_WEIGHTS_FILE) {
      descriptorWeights.save(CastParameter.getWeightsFileName());
    }
        
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT, NumberOfUntaggedTextUnits
    + " untagged text units in the DIAsDEM collection\n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) + 
    "\nhave been vectorized and exported into the file\n" +
    Tools.shortenFileName(CastParameter.getVectorFileName(), 50) + "!");
    this.setTaskResult(100, "All Documents Processed ...", Result,
    TaskResult.FINAL_RESULT, Task.TASK_FINISHED);
    
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
  
  private void createVectors() {

    Progress = new AbstractTaskProgress(TaskProgress.INDETERMINATE,
    "Loading Descriptors ...");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    TextFile vectorFile = new TextFile( 
    new File(CastParameter.getVectorFileName()));
    vectorFile.empty();
    vectorFile.open();
    SentenceCounter = 0;

    String structLine = null;
    if (CastParameter.getFileType() == VectorizeTextUnitsParameter.ARFF_FILE ||
    CastParameter.getFileType() == VectorizeTextUnitsParameter.SPARSE_ARFF_FILE) {
      vectorFile.setNextLine("@relation 'DIAsDEM'");
      vectorFile.setNextLine("@attribute DocumentType string");
      vectorFile.setNextLine("@attribute Document string");
      vectorFile.setNextLine("@attribute TextUnit string");
      for (int i = 3; i < currentVector.length; i++) {
        structLine = "@attribute ";
        structLine += Tools.createAsciiAttributeName(currentVector[i], 
        20, "D" + (i - 2) + "_", null);
        structLine += " real";
        vectorFile.setNextLine(structLine);
      }         
      vectorFile.setNextLine("@data");
    }
    if (CastParameter.getFileType() == VectorizeTextUnitsParameter.TXT_FILE) {
      String allDescriptors = "";
      for (int i = 3; i < currentVector.length; i++) {
        allDescriptors += Tools.fixStringSize(Tools.createAsciiAttributeName(
        currentVector[i], 20, "D" + (i - 2) + "_", null), 20, "left");
      }
      vectorFile.setFirstLine(
      Tools.fixStringSize("DocumentType", 20, "left" ) 
      + Tools.fixStringSize("Document", 25, "left" ) 
      + Tools.fixStringSize("TextUnit" + "", 10, "left" ) 
      + allDescriptors );
    }
    if (CastParameter.getFileType() == VectorizeTextUnitsParameter.CSV_FILE) {
      structLine = "DocumentType,Document,TextUnit";
      for (int i = 3; i < currentVector.length; i++)
        structLine += "," + Tools.createAsciiAttributeName(currentVector[i], 
        20, "D" + (i - 2) + "_", null);
      vectorFile.setNextLine(structLine);
    }

    if (CastParameter.createMetaDataFile()) {
      MetaDataFile = new TextFile( new File(CastParameter.getVectorFileName()
      + ".meta" ) );
      MetaDataFile.empty(); 
      MetaDataFile.open();
      if (CastParameter.getFileType() == VectorizeTextUnitsParameter.TXT_FILE) {
        MetaDataFile.setNextLine("1-20\tDocumentType");
        MetaDataFile.setNextLine("21-45\tDocument");
        MetaDataFile.setNextLine("46-55\tTextUnit");
      }
      else {
        MetaDataFile.setNextLine("DocumentType");
        MetaDataFile.setNextLine("Document");
        MetaDataFile.setNextLine("TextUnit");
      }
      String allDescriptors = "";
      int index = 56;
      for (int i = 3; i < currentVector.length; i++) {
        if (CastParameter.getFileType() == VectorizeTextUnitsParameter.TXT_FILE) {
          MetaDataFile.setNextLine(index + "-" + (index + 19) + "\t"
          + Tools.createAsciiAttributeName(currentVector[i], 20, "D" + (i - 2) 
          + "_", null) + " = " + currentVector[i] 
          + (DescriptorDocumentFrequencies[i] == -1 ? "; Descriptor Frequency = ?" : 
          "; Descriptor Frequency = " + DescriptorDocumentFrequencies[i]) 
          + "; Descriptor Weight = " 
          + this.getTermWeight(currentVector[i], mappingTable) );
        }
        else {
          MetaDataFile.setNextLine(Tools.createAsciiAttributeName(currentVector[i], 
          20, "D" + (i - 2) + "_", null) + " = " + currentVector[i] 
          + (DescriptorDocumentFrequencies[i] == -1 ? "; Descriptor Frequency = ?" : 
          "; Descriptor Frequency = " + DescriptorDocumentFrequencies[i])
           + "; Descriptor Weight = " 
          + this.getTermWeight(currentVector[i], mappingTable) );
       }
        index += 20;
      }
      MetaDataFile.close();
    }

    if (CastParameter.createFileForTermAssociationDiscovery()) {
      AssociationFile = new TextFile( new File(CastParameter
      .getVectorFileName() + ".assoc") );
      AssociationFile.empty();
      AssociationFile.open();
      if (CastParameter.getFileType() == VectorizeTextUnitsParameter.TXT_FILE) {
        AssociationFile.setFirstLine(
        Tools.fixStringSize("Document", 25, "left" ) 
        + Tools.fixStringSize("TextUnit", 10, "left" ) 
        + Tools.fixStringSize("DocumentID" + "", 10, "left" ) 
        + Tools.fixStringSize("TextUnitID" + "", 10, "left" ) 
        + Tools.fixStringSize("TextUnitDescriptor" + "", 20, "left" ) );
 
      }
      if (CastParameter.getFileType() == VectorizeTextUnitsParameter.CSV_FILE)
        AssociationFile.setFirstLine("Document,TextUnit,"
        + "DocumentID,TextUnitID,TextUnitDescriptor");
      if (CastParameter.getFileType() == VectorizeTextUnitsParameter.ARFF_FILE) {
        AssociationFile.setFirstLine("@relation 'DIAsDEM_TermAssociations'");
        AssociationFile.setNextLine("@attribute Document string");
        AssociationFile.setNextLine("@attribute TextUnit string");
        AssociationFile.setNextLine("@attribute DocumentID integer");
        AssociationFile.setNextLine("@attribute TextUnitID integer");
        for (int i = 3; i < currentVector.length; i++)
          AssociationFile.setNextLine("@attribute " + Tools
          .createAsciiAttributeName(currentVector[i], 20, "D" 
          + (i - 2) + "_", null) + " {0, 1}");
        AssociationFile.setNextLine("@data");
      }
    }
    
    long numberOfTextUnits = 0;
    NumberOfUntaggedTextUnits = 0;
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (CounterProgress == 1 || (CounterProgress % 50) == 0) {
        Progress.update( (int)(CounterProgress * 100 / MaxProgress),
        "Vectorizing Document " + GlobalDocumentID);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      DiasdemDocument.backupProcessedTextUnits(DiasdemProject
      .getProcessedTextUnitsRollbackOption());
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits(); 
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
        
        if (CastParameter.getIteration() == 1) {
          // remove data concerning previous clustering iterations
          DiasdemTextUnit.setClusterLabel(null);
          DiasdemTextUnit.setClusterID(-1);
          DiasdemTextUnit.setIteration(-1);
          clusterName = "";
        }
        else {
          clusterName = DiasdemTextUnit.getClusterLabel();
        }       
        
        SentenceCounter = 0;
        currentSentence = new DefaultDIAsDEMthesaurus();
        // + EintragTyp, EintragID, SatzID
        currentVector = new String[NumberOfDescriptors + 3];
        currentVector[0] = "null";
        currentVector[1] = DiasdemDocument.getDiasdemDocumentID();
        SentenceCounter++;
        currentVector[2] = Tools.int2String(i);
        tokenizer = new StringTokenizer(TextUnitContentsAsString);
        while (tokenizer.hasMoreElements()) {
          currentWord = tokenizer.nextToken();
          // create NE type placeholder <<date>> from NE placeholders <<123>>
          if (NamedEntity.isPlaceholder(currentWord)) {
            currentWord = (DiasdemDocument.getActiveTextUnitsLayer()
            .getNamedEntity(NamedEntity.getNamedEntityIndex(currentWord)))
            .getPossibleTypesPlaceholder();
          }
          correspondingDescriptor = thesaurus.getDescriptorTerm(currentWord);
          if (correspondingDescriptor != null) {
            currentSentence.countOccurrence(correspondingDescriptor.getWord());
          }
        }
        
        for (int j = 3; j < currentVector.length; j++) {
          currentVector[j] = "0";
        }
        currentTerm = currentSentence.getFirstTerm();
        while (currentTerm != null) {
          currentVector[ ( (Integer)mappingTable.get(
          currentTerm.getWord() ) ).intValue() ] =
          this.computeTermWeight(currentTerm, mappingTable);
          currentTerm = currentSentence.getNextTerm();
        }
        
        // write current text unit vector according to specified format
        numberOfTextUnits++;
        if (CastParameter.getIteration() == 1 || (CastParameter
        .getIteration() > 1 && clusterName.trim().equals("-"))) {
          NumberOfUntaggedTextUnits++;
          if (CastParameter.getLengthNormalization() ==
          CastParameter.COSINE_LENGTH_NORMALIZATION) {
            currentVector = this.cosineNormalization(currentVector);
          }
          switch (CastParameter.getFileType()) {
            case VectorizeTextUnitsParameter.TXT_FILE: {
              vectorFile.setNextLine(this.writeFixedVector(currentVector));
              break;
            }
            case VectorizeTextUnitsParameter.ARFF_FILE: {
              vectorFile.setNextLine(this.writeArffVector(currentVector));
              break;
            }
            case VectorizeTextUnitsParameter.SPARSE_ARFF_FILE: {
              vectorFile.setNextLine(this.writeSparseArffVector(currentVector));
              break;
            }
            case VectorizeTextUnitsParameter.CSV_FILE: {
              vectorFile.setNextLine(this.writeCsvVector(currentVector));
              break;
            }
          }
        }

        DiasdemDocument.replaceProcessedTextUnit(i, DiasdemTextUnit);
      }
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      CounterProgress++;
      GlobalDocumentID++;

    }  // read all documents
    
    DiasdemCollection.setNumberOfTextUnits(numberOfTextUnits);
    DiasdemCollection.setNumberOfUntaggedTextUnits(NumberOfUntaggedTextUnits);

    vectorFile.close();
    if (CastParameter.createFileForTermAssociationDiscovery()) {
      AssociationFile.close();
    }

    // System.out.println(mappingTable);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private String writeFixedVector(String[] pVector) {

    int length = 10;
    TmpVectorBuffer = new StringBuffer(10000);

    TmpVectorBuffer.append(Tools.fixStringSize(pVector[0].trim(), 20, "left" ));
    TmpVectorBuffer.append(Tools.fixStringSize(pVector[1].trim(), 25, "left" ));
    TmpVectorBuffer.append(Tools.fixStringSize(pVector[2].trim(), 10, "left" ));
    GlobalSentenceID++;
    for (int i = 3; i < pVector.length; i++) {
      TmpVectorBuffer.append(Tools.fixStringSize(pVector[i].trim(), 20, 
      "left" ));
      // write data for association rule mining
      if (CastParameter.createFileForTermAssociationDiscovery()
      && ! pVector[i].trim().startsWith("0") 
      && ! pVector[1].trim().equals("Document")) {
        AssociationFile.setNextLine(
        Tools.fixStringSize(pVector[1].trim(), 25, "left" ) 
        + Tools.fixStringSize(pVector[2].trim(), 10, "left" ) 
        + Tools.fixStringSize(GlobalDocumentID + "", 10, "left" ) 
        + Tools.fixStringSize(GlobalSentenceID + "", 10, "left" ) 
        + Tools.fixStringSize( DescriptorWords[i], 20, "left" ) );
      }
    }

    return TmpVectorBuffer.toString();

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private String writeCsvVector(String[] pVector) {

    TmpVectorBuffer = new StringBuffer(10000);

    TmpVectorBuffer.append("\"");
    TmpVectorBuffer.append(pVector[0].trim());
    TmpVectorBuffer.append("\",\"");
    TmpVectorBuffer.append(pVector[1].trim());
    TmpVectorBuffer.append("\",");
    TmpVectorBuffer.append(pVector[2].trim());
    GlobalSentenceID++;
    for (int i = 3; i < pVector.length; i++) {
      TmpVectorBuffer.append(",");
      TmpVectorBuffer.append(pVector[i].trim());
      // write data for association rule mining
      if (CastParameter.createFileForTermAssociationDiscovery()
      && ! pVector[i].trim().equals("0") 
      && ! pVector[1].trim().equals("Document")) {
        AssociationFile.setNextLine("\"" + pVector[1].trim() 
        + "\",\"" + pVector[2].trim() + "\"," + GlobalDocumentID + ","
        + GlobalSentenceID + ",\"" + DescriptorWords[i] + "\"");
      }
    }

    return TmpVectorBuffer.toString();

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private String writeArffVector(String[] pVector) {

    TmpVectorBuffer = new StringBuffer(10000);
    TmpAssocBuffer = new StringBuffer(10000);

    TmpVectorBuffer.append(pVector[0].trim());
    TmpVectorBuffer.append(",");
    TmpVectorBuffer.append(pVector[1].trim());
    TmpVectorBuffer.append(",");
    TmpVectorBuffer.append(pVector[2].trim());
    GlobalSentenceID++;
    for (int i = 3; i < pVector.length; i++) {
      TmpVectorBuffer.append(",");
      TmpVectorBuffer.append(pVector[i].trim());
      // write data for association rule mining
      if (i == 3) {
        TmpAssocBuffer.append(pVector[i].equals("0") ? "?" : "1");
      }
      else {
        TmpAssocBuffer.append(",");
        TmpAssocBuffer.append(pVector[i].equals("0") ? "?" : "1");
      }
    }
    if (CastParameter.createFileForTermAssociationDiscovery()) {
      AssociationFile.setNextLine( "\"" + pVector[1].trim() 
      + "\",\"" + pVector[2].trim() + "\"," + GlobalDocumentID + ","
      + GlobalSentenceID + "," + TmpAssocBuffer.toString());
    }
        
    return TmpVectorBuffer.toString();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  private String writeSparseArffVector(String[] pVector) {

    TmpVectorBuffer = new StringBuffer(10000);
    TmpAssocBuffer = new StringBuffer(10000);

    TmpVectorBuffer.append("{0 ");
    TmpVectorBuffer.append(pVector[0].trim());
    TmpVectorBuffer.append(", 1 ");
    TmpVectorBuffer.append(pVector[1].trim());
    TmpVectorBuffer.append(", 2 ");
    TmpVectorBuffer.append(pVector[2].trim());
    GlobalSentenceID++;
    for (int i = 3; i < pVector.length; i++) {
      if (!pVector[i].equals("0")) {
        TmpVectorBuffer.append(", ");
        TmpVectorBuffer.append(i);
        TmpVectorBuffer.append(" ");
        TmpVectorBuffer.append(pVector[i].trim());
      }
      // write data for association rule mining
      if (i == 3) {
        TmpAssocBuffer.append(pVector[i].equals("0") ? "?" : "1");
      }
      else {
        TmpAssocBuffer.append(",");
        TmpAssocBuffer.append(pVector[i].equals("0") ? "?" : "1");
      }
    }
    TmpVectorBuffer.append("}");
    if (CastParameter.createFileForTermAssociationDiscovery()) {
      AssociationFile.setNextLine( "\"" + pVector[1].trim() 
      + "\",\"" + pVector[2].trim() + "\"," + GlobalDocumentID + ","
      + GlobalSentenceID + "," + TmpAssocBuffer.toString());
    }
        
    return TmpVectorBuffer.toString();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  private void computeInverseDescriptorDocumentFrequencies() {

    Progress.update( TaskProgress.INDETERMINATE, 
      "Computing Collection Frequencies ..."); 
    DiasdemServer.setTaskProgress(Progress, TaskThread);

    DescriptorDocumentFrequencies = new int[NumberOfDescriptors + 3];
    for (int i = 0; i < DescriptorDocumentFrequencies.length; i++) {
      DescriptorDocumentFrequencies[i] = 0;
    }

    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (CounterProgress == 1 || (CounterProgress % 50) == 0) {
        Progress.update( (int)(CounterProgress * 100 / MaxProgress), 
          "Computing Collection Frequencies: Text Unit " + TextUnitCounter); 
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }

      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      // read-only task: no backup necessary
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits(); 
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
        
        if (CastParameter.getIteration() == 1) {
          clusterName = "";
        }
        else {
          clusterName = DiasdemTextUnit.getClusterLabel();
        }       
                
        SentenceCounter = 0;
        if (CastParameter.getIteration() == 1 || (CastParameter
        .getIteration() > 1  && clusterName.trim().equals("-"))) {

          currentSentence = new DefaultDIAsDEMthesaurus();
          SentenceCounter++;
          TextUnitCounter++;
          tokenizer = new StringTokenizer(TextUnitContentsAsString);
          while ( tokenizer.hasMoreElements() ) {
            currentWord = tokenizer.nextToken();
            // create NE type placeholder <<date>> from NE placeholders <<123>>
            if (NamedEntity.isPlaceholder(currentWord)) {
              currentWord = (DiasdemDocument.getActiveTextUnitsLayer()
              .getNamedEntity(NamedEntity.getNamedEntityIndex(currentWord)))
              .getPossibleTypesPlaceholder();
            }
            correspondingDescriptor = thesaurus.getDescriptorTerm(currentWord);
            if (correspondingDescriptor != null) {
              currentSentence.countOccurrence(correspondingDescriptor.getWord());
            }
          }
          
          currentTerm = currentSentence.getFirstTerm();
          while (currentTerm != null) {
            DescriptorDocumentFrequencies[ ( (Integer)mappingTable.get(
            currentTerm.getWord() ) ).intValue() ] +=
            currentTerm.getOccurrences();
            currentTerm = currentSentence.getNextTerm();
          }
          
        }  // compute term weighting only if first || subsequent && !="-"
        
      }
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      CounterProgress++;

    }  // read all documents
    
    // fill descriptor weigths
    TmpString = null;
    TmpTerm = null;
    double idfWeight = 0.0d;
    Iterator iterator = mappingTable.keySet().iterator();
    while (iterator.hasNext()) {
      TmpString = (String)iterator.next();
      TmpTerm = descriptorWeights.get(TmpString);
      if (TmpTerm != null) {
        if (DescriptorDocumentFrequencies[( (Integer)mappingTable
        .get(TmpString) ).intValue()] > 0) {
          TmpTerm.setTermWeight(1.0d * Math.log(
          (double)TextUnitCounter / DescriptorDocumentFrequencies[
          ( (Integer)mappingTable.get(TmpString) ).intValue() ]));
        }
        else {
          // avoid 'Infinity' IDF weight, if DDF == 0
          TmpTerm.setTermWeight(0.0d);
        }
        descriptorWeights.delete(TmpString);
        descriptorWeights.add(TmpTerm);
      }
      else {
        // TmpTerm == null
        if (DescriptorDocumentFrequencies[( (Integer)mappingTable
        .get(TmpString) ).intValue()] > 0) {
          descriptorWeights.add(new DefaultDIAsDEMthesaurusTerm(
          descriptorWeights.getNextID(), TmpString, 1.0d * Math.log(
          (double)TextUnitCounter / DescriptorDocumentFrequencies[
          ( (Integer)mappingTable.get(TmpString) ).intValue() ])));
        }
        else {
          // avoid 'Infinity' IDF weight, if DDF == 0
          descriptorWeights.add(new DefaultDIAsDEMthesaurusTerm(
          descriptorWeights.getNextID(), TmpString, 0.0d));
        }
      }
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private String getTermWeight(String pTerm, HashMap pMappingTable) {
   
    return this.computeTermWeight(new DefaultDIAsDEMthesaurusTerm(123, pTerm, 
    1), pMappingTable);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private String computeTermWeight(DIAsDEMthesaurusTerm pTerm, 
  HashMap pMappingTable) {

    // step 1: compute term frequency
    int termFrequency = 0;
    if (CastParameter.getDescriptorFrequency() == 
      VectorizeTextUnitsParameter.RAW_DESCRIPTOR_FREQUENCY) {
      termFrequency = pTerm.getOccurrences();
    }
    else if (CastParameter.getDescriptorFrequency() == 
      VectorizeTextUnitsParameter.BOOLEAN_DESCRIPTOR_FREQUENCY) {
      termFrequency = (pTerm.getOccurrences() > 0 ? 1 : 0);
    }

    // step 2: multiply term frequency and term weight
    double weight = 1.0d;
    TmpTerm = descriptorWeights.get(pTerm.getWord());
    if (TmpTerm != null) {
      weight = TmpTerm.getTermWeight();
    }

    return Double.toString((double)termFrequency * weight);

  }  
  
  /* ########## ########## ########## ########## ########## ######### */

  private String[] cosineNormalization(String[] pVector) {
    
    // pVector[0]..[2] contains metadata, remaining components are term weights
    double termWeight = 0.0d, sumOfSquaredTermWeights = 0.0;
    double[] termWeights = new double[pVector.length];
    for (int i = 3; i < pVector.length; i++) {
      termWeight = Double.parseDouble(pVector[i]);
      sumOfSquaredTermWeights += (termWeight * termWeight);
      termWeights[i] = termWeight;
    }
    // cosine normalization: each term weight is divided by a factor 
    // representing Euclidean vector length
    double normalizationComponent = 1.0d / Math.sqrt(sumOfSquaredTermWeights);
    for (int i = 3; i < pVector.length; i++) {
      // do not destroy the sparsity of the vector by double arithmetic that
      if (!pVector[i].equals("0")) {
        pVector[i] = Double.toString(termWeights[i] * normalizationComponent);
      }
    }
    
    return pVector;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isDescriptor(String pDescriptor, DIAsDEMthesaurus pThesaurus,
  VectorizeTextUnitsParameter pParameter) {
   
    if ( (pThesaurus.get(pDescriptor) != null) &&
    (pParameter.getVectorDimensions() == VectorizeTextUnitsParameter
    .ALL_DESCRIPTORS ) 
    || ( (pThesaurus.get(pDescriptor).getScopeNotes()
    .indexOf(CastParameter.getDescriptorsScopeNotesContain()) >= 0) &&
    pParameter.getVectorDimensions() == VectorizeTextUnitsParameter
    .SPECIFIED_DESCRIPTORS ) 
    || ( (pThesaurus.get(pDescriptor).getScopeNotes()
    .indexOf(CastParameter.getDescriptorsScopeNotesContain()) < 0) &&
    pParameter.getVectorDimensions() == VectorizeTextUnitsParameter
    .NOT_SPECIFIED_DESCRIPTORS) )
      return true;
    else
      return false;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  private String getRequiredFileExtension(
  VectorizeTextUnitsParameter pParameter) {
    
    if (pParameter != null && pParameter.getFileType() ==
    VectorizeTextUnitsParameter.CSV_FILE) {
      return DIAsDEMguiPreferences.CSV_FILE_EXTENSION;
    }
    else if (pParameter != null && pParameter.getFileType() ==
    VectorizeTextUnitsParameter.ARFF_FILE) {
      return DIAsDEMguiPreferences.ARFF_FILE_EXTENSION;
    }
    else if (pParameter != null && pParameter.getFileType() ==
    VectorizeTextUnitsParameter.TXT_FILE) {
      return DIAsDEMguiPreferences.TEXT_FILE_EXTENSION;
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}