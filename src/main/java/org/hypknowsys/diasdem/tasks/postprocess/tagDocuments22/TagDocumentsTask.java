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

package org.hypknowsys.diasdem.tasks.postprocess.tagDocuments22;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.core.neex.*;
import org.hypknowsys.diasdem.core.gate.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public class TagDocumentsTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TagDocumentsParameter CastParameter = null;

  private DIAsDEMconceptualDtd DerivedDtd = null;

  private File XmlDirectoryFile = null;
  private String XmlDirectoryFileName = null;
  private int XmlDirectoryIndex = 1;
  private int FilesInXmlDirectory = 0;
  private int MaxFilesInXmlDirectory = 0;

  private File GateDirectoryFile = null;
  private String GateDirectoryFileName = null;
  private int GateDirectoryIndex = 1;
  private int FilesInGateDirectory = 0;
  private int MaxFilesInGateDirectory = 0;

  private GateDocument MyGateDocument = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Tag Documents 2.2";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagDocuments22"
  + ".TagDocumentsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagDocuments22"
  + ".TagDocumentsResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagDocuments22"
  + ".TagDocumentsControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagDocumentsTask() {
    
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
    
    TagDocumentsParameter parameter = null;
    if (pParameter instanceof TagDocumentsParameter) {
      parameter = (TagDocumentsParameter)pParameter;
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
    file = new File(parameter.getDtdFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_EXTENSION +
      "-file in the field 'Conceptual DTD File'!");
    }
    
    file = new File(parameter.getXmlDocumentsDirectory());
    if (file.exists() && file.isDirectory() && file.list() != null
    && file.list().length > 0) {
      result.addWarning(
      "Warning: The directory specified in the field\n" +
      "'XML Document Directory' is not empty. Do\n" + 
      "you really want to select this directory?");
    }
    file = new File(parameter.getXmlDocumentsDirectory());
    if (!file.exists()) {
      try {
        boolean success = file.mkdirs();
      }
      catch (Exception e2) {}
    }
    if (!file.exists() || !file.isDirectory()) {
      result.addError(
      "Please enter the name of an existing local\n" +
      "directory in the field 'XML Document Directory'!");
    }

    if (parameter.getRandomSampleFileName().trim().length() <= 0
    || !parameter.getRandomSampleFileName().trim().endsWith(
    DIAsDEMguiPreferences.TEXT_UNIT_SAMPLE_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " + 
      DIAsDEMguiPreferences.TEXT_UNIT_SAMPLE_FILE_EXTENSION +
      "-file name\nin the field 'Random Sample File'!");
    }
    file = new File(parameter.getRandomSampleFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Random Sample File' currently exists.\n" +
      "Do you really want to replace this file?");
    }
    if (parameter.getRandomSampleSize() < 0.0 
    || parameter.getRandomSampleSize() > 1.0) {
      result.addError(
      "Error: 'Random Sample Size' must\n"+
      "be a valid double in [0.0; 1.0]!");
    }

    if (parameter.createGateFiles()) {
      file = new File(parameter.getGateDirectory());
      if (file.exists() && file.isDirectory() && file.list() != null
      && file.list().length > 0) {
        result.addWarning(
        "Warning: The directory specified in the field\n" +
        "'GATE Files Directory' is not empty. Do you\n" +
        "really want to select this directory?");
      }
      file = new File(parameter.getGateDirectory());
      if (!file.exists()) {
        try {
          boolean success = file.mkdirs();
        }
        catch (Exception e2) {}
      }
      if (!file.exists() || !file.isDirectory()) {
        result.addError(
        "Please enter the name of an existing local\n" +
        "directory in the field 'GATE Files Directory'!");
      }
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new TagDocumentsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new TagDocumentsResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar
    .ACTIONS_POSTPROCESS_PATTERNS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof TagDocumentsParameter) {
      CastParameter = (TagDocumentsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Documents cannot be tagged!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    if (DiasdemDocument.getProcessedTextUnit(0).getIteration() < 0
    || DiasdemDocument.getProcessedTextUnit(0).getClusterLabel() == null
    || DiasdemDocument.getProcessedTextUnit(0).getClusterID() < 0) {
      this.setErrorTaskResult(100, shortErrorMessage,
      "Error: The default active text units layer " 
      + DiasdemProject.getActiveTextUnitsLayerIndex()
      + "\nof the first document does not contain any\n"
      + "tagged processed text units at all!");
      this.stop();
    }
    try {
      DerivedDtd = new DefaultDIAsDEMconceptualDtd(
      CastParameter.getDtdFileName(), KProperties.LOAD);
    }
    catch (Exception e) {
      super.setErrorTaskResult(100, shortErrorMessage,
      "I/O-error: Conceptual DTD\ncollection cannnot be opened!");
      this.stop();
    }
    
    File firstXmlFile = null;
    String featureLine = null;
    NamedEntity[] namedEntities = null;
    
    TextFile wumSequenceFile = null;
    TextFile wumAssociationFile = null;
    TextFile allTextUnitsFile = null;
    TextFile randomFile = null;
    TextFile sampleTextUnitsFile = null;
    
    int counterProgress = 0;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    if (DiasdemDocument != null && CastParameter.createWumFiles()) {
      firstXmlFile = new File(DiasdemDocument.getDiasdemDocumentID());
      wumSequenceFile = new TextFile( new File(Tools.ensureTrailingSlash(
      CastParameter.getXmlDocumentsDirectory()) + "TagSequencesWum.log"));
      wumSequenceFile.empty();
      wumSequenceFile.open();
      wumAssociationFile = new TextFile( new File(Tools.ensureTrailingSlash(
      CastParameter.getXmlDocumentsDirectory()) + "TagAssociationsWum.log"));
      wumAssociationFile.empty();
      wumAssociationFile.open();
    }
    
    allTextUnitsFile = new TextFile( new File(Tools.ensureTrailingSlash(
    CastParameter.getXmlDocumentsDirectory()) + "_AllTextUnitsTagged.txt") );
    allTextUnitsFile.empty();
    allTextUnitsFile.open();
    
    if (DiasdemDocument != null) {
      firstXmlFile = new File(DiasdemDocument.getDiasdemDocumentID());
    }
    
    TextFile tagByDocFile = null;
    StringBuffer tagByDocLine = null;
    TreeSet documentElements = null;
    TreeSet dtdElements = null;
    Iterator iterator2 = null;
    TreeSet metaDataKeys = null;
    if (CastParameter.createTagByDocumentFile()) {
      if (DiasdemDocument.getMetaData() != null
      && DiasdemDocument.getMetaData().size() > 0) {
        metaDataKeys = new TreeSet(DiasdemDocument.getMetaData().keySet());
      }
      else {
        metaDataKeys = new TreeSet();
      }
      tagByDocFile = new TextFile( new File(Tools.ensureTrailingSlash(
      CastParameter.getXmlDocumentsDirectory()) + "TagByDocumentMatrix.csv") );
      tagByDocFile.open();
      tagByDocLine = null;
      iterator2 = metaDataKeys.iterator();
      while (iterator2.hasNext()) {
        if (tagByDocLine == null) {
          tagByDocLine = new StringBuffer(5000);
        }
        else {
          tagByDocLine.append(",");
        }
        tagByDocLine.append((String)iterator2.next());
      }
      dtdElements = new TreeSet(DerivedDtd.getElements().keySet());
      iterator2 = dtdElements.iterator();
      while (iterator2.hasNext()) {
        if (tagByDocLine == null) {
          tagByDocLine = new StringBuffer(5000);
        }
        else {
          tagByDocLine.append(",");
        }
        tagByDocLine.append((String)iterator2.next());
      }
      tagByDocFile.setFirstLine(tagByDocLine.toString());
      documentElements = new TreeSet();
    }
    
    while (DiasdemDocument != null) {

      counterProgress++;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Document " + (counterProgress) );
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      if (CastParameter.createGateFiles()) {
        MyGateDocument = new GateDocument(this.getGateDirectory()
        + "gateDocument" + counterProgress + ".xml");
      }
      
      DerivedDtd.dumpDocumentAsXmlFile(DiasdemProject, DiasdemDocument,
      this.getXmlDirectory() + "document" + counterProgress + ".xml", false);
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      // read-only task does not require backup      
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits();
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
        
        if (DiasdemTextUnit.getClusterLabel() != null
        && (DiasdemTextUnit.getClusterLabel().equals("-")
        || DiasdemTextUnit.getClusterLabel().equals("="))) {
          // allTextUnitsFile for evaluation
          allTextUnitsFile.setNextLine(DiasdemDocument.getDiasdemDocumentID()
          + " " + DiasdemDocument.getOriginalTextUnit(i).getContentsAsString()
          .replace('\n', ' ') );
          if (CastParameter.createWumFiles()) {
            // wumImportFile for sequence and association analysis
            wumSequenceFile.setNextLine(DiasdemDocument.getDiasdemDocumentID()
            + " - - " + "[01/Jan/2003:00:00:00 +0200] \"GET "
            + "UntaggedTextUnit HTTP/1.0\" 200 1");
          }
        }
        else {
          namedEntities = NamedEntity.getContainedNamedEntities(DiasdemDocument
          .getActiveTextUnitsLayer(), DiasdemTextUnit.getContentsAsString());
          featureLine = DerivedDtd.getElementAttributesString(DiasdemTextUnit
          .getClusterLabel(), namedEntities, DerivedDtd
          .getMinAttributeRelSupport());       
          if (CastParameter.createTagByDocumentFile()) {
            // remember XML tags
            documentElements.add(DiasdemTextUnit.getClusterLabel());
          }         
          if (featureLine.length() > 0) {
            // allTextUnitsFile for evaluation
            allTextUnitsFile.setNextLine(DiasdemDocument.getDiasdemDocumentID()
            + " <" + DiasdemTextUnit.getClusterLabel() + featureLine + ">"
            + DiasdemDocument.getOriginalTextUnit(i).getContentsAsString()
            .replace('\n', ' ')
            + "</" + DiasdemTextUnit.getClusterLabel() + ">");
          }
          else {
            // allTextUnitsFile for evaluation
            allTextUnitsFile.setNextLine(DiasdemDocument.getDiasdemDocumentID()
            + " <" + DiasdemTextUnit.getClusterLabel() + ">"
            + DiasdemDocument.getOriginalTextUnit(i).getContentsAsString()
            .replace('\n', ' ')
            + "</" + DiasdemTextUnit.getClusterLabel() + ">");
          }
        }
        // create GATE document
        if (CastParameter.createGateFiles()) {
          int structEndNode = MyGateDocument.addStructuralAnnotationStartNode(
          "Text Unit");
          int semanEndNode = MyGateDocument.addSemanticAnnotationStartNode(
          DiasdemTextUnit.getClusterLabel());
          MyGateDocument.addPlainText(DiasdemDocument.getOriginalTextUnit(i)
          .getContentsAsString());
          MyGateDocument.addStructuralAnnotationEndNode(structEndNode);
          MyGateDocument.addSemanticAnnotationEndNode(semanEndNode);
          MyGateDocument.addBlankSpace();
        }       
        if (CastParameter.createWumFiles()) {
          // wumImportFile for sequence and association analysis
          wumSequenceFile.setNextLine(DiasdemDocument.getDiasdemDocumentID()
          + " - - " + "[01/Jan/2003:00:00:00 +0200] \"GET "
          + DiasdemTextUnit.getClusterLabel() + " HTTP/1.0\" 200 1");
          wumAssociationFile.setNextLine(DiasdemDocument.getDiasdemDocumentID()
          + " - - " + "[01/Jan/2003:00:00:00 +0200] \"GET "
          + DiasdemTextUnit.getClusterLabel() + " HTTP/1.0\" 200 1");
        }
      }
      if (CastParameter.createGateFiles()) {
        MyGateDocument.writeXml();
      }
            
      if (DiasdemDocument != null && CastParameter.createTagByDocumentFile()) {
        tagByDocLine = null;
        iterator2 = metaDataKeys.iterator();
        while (iterator2.hasNext()) {
          if (tagByDocLine == null) {
            tagByDocLine = new StringBuffer(5000);
          }
          else {
            tagByDocLine.append(",");
          }
          tagByDocLine.append("\"");
          TmpString = (String)iterator2.next();
          if (DiasdemDocument.getMetaData() != null
          && DiasdemDocument.getMetaData().get(TmpString) != null) {
            tagByDocLine.append((String)DiasdemDocument.getMetaData()
            .get(TmpString));
          }
          else {
            tagByDocLine.append("null");
          }
          tagByDocLine.append("\"");
        }
        String tmpElement = null;
        iterator2 = dtdElements.iterator();
        while (iterator2.hasNext()) {
          if (tagByDocLine == null) {
            tagByDocLine = new StringBuffer(5000);
          }
          else {
            tagByDocLine.append(",");
          }
          tmpElement = (String)iterator2.next();
          if (documentElements.contains(tmpElement)) {
            tagByDocLine.append("1");
          }
          else {
            tagByDocLine.append("0");
          }
        }
        tagByDocFile.setNextLine(tagByDocLine.toString());
        documentElements = new TreeSet();
      }
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      
    }  // read all documents
    
    Progress = new AbstractTaskProgress(TaskProgress.INDETERMINATE,
    "Drawing Random Text Unit Sample ...");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    sampleTextUnitsFile = new TextFile( new File(
    CastParameter.getRandomSampleFileName()));
    sampleTextUnitsFile.empty();
    sampleTextUnitsFile.open();
    
    randomFile = new TextFile( new File(Tools.ensureTrailingSlash(
    DiasdemProject.getProjectDirectory()) + "_RandomSampleTextUnits.txt") );
    randomFile.empty();
    randomFile.open();
    
    randomFile.setNextLine("# ");
    randomFile.setNextLine("# Number of text units = " 
    + DiasdemCollection.getNumberOfTextUnits());
    Random randomizer = new Random();
    double sampleSize = CastParameter.getRandomSampleSize() 
    * DiasdemCollection.getNumberOfTextUnits();
    randomFile.setNextLine("# " + CastParameter.getRandomSampleSize() +
    " * text units = " + (int)sampleSize);
    randomFile.setNextLine("# ");
    TreeSet sortedRandoms = new TreeSet();
    for (int i = 0; sortedRandoms.size() < (int)sampleSize; i++) {
      sortedRandoms.add(new Integer(randomizer.nextInt(
      (int)DiasdemCollection.getNumberOfTextUnits() + 1) + 1));
    }
    Iterator iterator = sortedRandoms.iterator();
    while (iterator.hasNext()) {
      randomFile.setNextLine(((Integer)iterator.next()).toString());
    }
    
    // create file that contain random text units for manual evaluation
    String currentTextUnit = allTextUnitsFile.getFirstLine();
    int currentLineCounter = 1;
    String nextSampleLineString = randomFile
    .getFirstLineButIgnoreCommentsAndEmptyLines();
    int nextSampleLine = 0;
    if (nextSampleLineString != null) {
      nextSampleLine = Integer.parseInt(nextSampleLineString);
    }
    while (currentTextUnit != null) {
      if (currentLineCounter == nextSampleLine) {
        sampleTextUnitsFile.setNextLine(currentTextUnit);
        nextSampleLineString = randomFile
        .getNextLineButIgnoreCommentsAndEmptyLines();
        if (nextSampleLineString != null) {
          nextSampleLine = Integer.parseInt(nextSampleLineString);
        }
      }
      currentTextUnit = allTextUnitsFile.getNextLine();
      currentLineCounter++;
    }
    
    if (CastParameter.createTagByDocumentFile()) {
      tagByDocFile.close();
    }
    if (CastParameter.createWumFiles() && wumSequenceFile != null) {
      wumSequenceFile.close();
    }
    if (CastParameter.createWumFiles() && wumAssociationFile != null) {
      wumAssociationFile.close();
    }
    if (allTextUnitsFile != null) {
      allTextUnitsFile.close();
      File file = allTextUnitsFile.getFile();
      file.delete();
    }
    if (randomFile != null) {
      randomFile.close();
      File file = randomFile.getFile();
      file.delete();
    }
    if (sampleTextUnitsFile != null) {
      sampleTextUnitsFile.close();
    }
    super.closeDiasdemCollection();
    
    Result.update(TaskResult.FINAL_RESULT,
    "All DIAsDEM documents have been tagged in collection \n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) +"!");
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
  
  private String getXmlDirectory() {
    
    if (XmlDirectoryFile == null) {
      MaxFilesInXmlDirectory = DiasdemProject.getIntProperty(
      "MAX_FILES_PER_DIRECTORY");
      XmlDirectoryIndex = 0;
      FilesInXmlDirectory = MaxFilesInXmlDirectory;
    }
    if (FilesInXmlDirectory == MaxFilesInXmlDirectory) {
      XmlDirectoryIndex++;
      FilesInXmlDirectory = 0;
      XmlDirectoryFileName = Tools.ensureTrailingSlash(CastParameter
      .getXmlDocumentsDirectory()) + "part" + XmlDirectoryIndex 
      + File.separator;
      XmlDirectoryFile = new File(XmlDirectoryFileName);
      if (!XmlDirectoryFile.exists()) {
        XmlDirectoryFile.mkdirs();
      }
      DerivedDtd.writeXmlRepresentation(XmlDirectoryFileName);
    }
    FilesInXmlDirectory++;
    
    return XmlDirectoryFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getGateDirectory() {
    
    if (GateDirectoryFile == null) {
      MaxFilesInGateDirectory = DiasdemProject.getIntProperty(
      "MAX_FILES_PER_DIRECTORY");
      GateDirectoryIndex = 0;
      FilesInGateDirectory = MaxFilesInGateDirectory;
    }
    if (FilesInGateDirectory == MaxFilesInGateDirectory) {
      GateDirectoryIndex++;
      FilesInGateDirectory = 0;
      GateDirectoryFileName = Tools.ensureTrailingSlash(CastParameter
      .getGateDirectory()) + "part" + GateDirectoryIndex 
      + File.separator;
      GateDirectoryFile = new File(GateDirectoryFileName);
      if (!GateDirectoryFile.exists()) {
        GateDirectoryFile.mkdirs();
      }
    }
    FilesInGateDirectory++;
    
    return GateDirectoryFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}