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

package org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22;

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
import org.hypknowsys.diasdem.core.*;
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.core.neex.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public class DeriveConceptualDtdTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DeriveConceptualDtdParameter CastParameter = null;

  private DIAsDEMconceptualDtd DerivedDtd = null;
  private StringTokenizer Tokenizer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Derive Conceptual DTD 2.2";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22"
  + ".DeriveConceptualDtdParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22"
  + ".DeriveConceptualDtdResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22"
  + ".DeriveConceptualDtdControlPanel";
  
  private static final KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DEFAULT_CONCEPTUAL_DTD_FILE",
    "Derive Conceptual DTD 2.2: Default Conceptual DTD File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_DTD_DOCUMENTATION_FILE",
    "Derive Conceptual DTD 2.2: Default DTD Documentation File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE)
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DeriveConceptualDtdTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
  
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
    
    DeriveConceptualDtdParameter parameter = null;
    if (pParameter instanceof DeriveConceptualDtdParameter) {
      parameter = (DeriveConceptualDtdParameter)pParameter;
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
    if (parameter.getDtdFileName().trim().length() <= 0
    || !parameter.getDtdFileName().trim().endsWith(
    DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " + 
      DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_EXTENSION +
      "-file name\nin the field 'Conceptual DTD File'!");
    }    
    file = new File(parameter.getDtdFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Conceptual DTD File' currently exists.\n" +
      "Do you really want to replace this file?");
    }
    if (parameter.getDtdRootElement().trim().length() <= 0) {
      result.addError(
      "Error: Please enter a valid DTD root element such\n" +
      "as 'MyRoot' in the field 'DTD Root Element'!");
    }    
    if (parameter.getMinAttributeRelSupport() < 0.0 
    || parameter.getMinAttributeRelSupport() > 1.0) {
      result.addError(
      "Error: 'Min. Attribute Support' must\n"+
      "be a valid double in [0.0; 1.0]!");
    }
    if (parameter.getDtdDocumentationFileName().trim().length() <= 0
    || !parameter.getDtdDocumentationFileName().trim().endsWith(
    DIAsDEMguiPreferences.HTML_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " + 
      DIAsDEMguiPreferences.HTML_FILE_EXTENSION +
      "-file name\nin the field 'DTD Documentation File'!");
    }    
    file = new File(parameter.getDtdDocumentationFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'DTD Documentation File' currently exists.\n" +
      "Do you really want to replace this file?");
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new DeriveConceptualDtdParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new DeriveConceptualDtdResult();
    
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
    
    if (Parameter != null
    && Parameter instanceof DeriveConceptualDtdParameter) {
      CastParameter = (DeriveConceptualDtdParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Conceptual DTD cannot be derived!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    if (DiasdemDocument.getProcessedTextUnit(0).getIteration() < 0
    || DiasdemDocument.getProcessedTextUnit(0).getClusterLabel() == null
    || DiasdemDocument.getProcessedTextUnit(0).getClusterID() < 0) {
      this.setErrorTaskResult(100, shortErrorMessage,
      "Error: The default active text units layer " 
      + DiasdemProject.getActiveTextUnitsLayerIndex() + 
      "\nof the first document does not contain any\n" +
      "tagged processed text units at all!");
      this.stop();
    }
    
    HashSet tags = new HashSet();
    String namedEntityLine = null;
    NamedEntity[] namedEntities = null;
    
    int counterProgress = 0;
    long maxProgress = DiasdemCollection.getNumberOfDocuments() * 2;
    
    long NumberOfDocuments = 0L;
    long NumberOfTextUnits = 0L;
    long NumberOfTaggedTextUnits = 0L;
    long NumberOfUntaggedTextUnits = 0L;
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();    
    try {
      DerivedDtd = new DefaultDIAsDEMconceptualDtd(CastParameter
      .getDtdFileName(), DefaultDIAsDEMconceptualDtd.CREATE);
      DerivedDtd.setRootElement(CastParameter.getDtdRootElement());
      DerivedDtd.setMinAttributeRelSupport(CastParameter
      .getMinAttributeRelSupport());
      DerivedDtd.save();
    }
    catch (IOException e) {
      super.setErrorTaskResult(100, shortErrorMessage,
      "Error: Conceptual DTD file cannot be created!");
      this.stop();
    }
    
    while (DiasdemDocument != null) {
      
      counterProgress++;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      // read-only task does not require backup
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits();
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
        
        NumberOfTextUnits++;
        if (DiasdemTextUnit.getClusterLabel() != null
        && (DiasdemTextUnit.getClusterLabel().equals("-")
        || DiasdemTextUnit.getClusterLabel().equals("="))) {
          NumberOfUntaggedTextUnits++;
        }
        else {
          NumberOfTaggedTextUnits++;
          namedEntityLine = NamedEntity.convertContainedPlaceholders(
          DiasdemDocument.getActiveTextUnitsLayer(), DiasdemTextUnit
          .getContentsAsString());
          tags.add(DiasdemTextUnit.getClusterLabel());
          // register tag
          DerivedDtd.addOrUpdateElement(DiasdemTextUnit.getClusterLabel());
          namedEntities = NamedEntity.getContainedNamedEntities(
          DiasdemDocument.getActiveTextUnitsLayer(), DiasdemTextUnit
          .getContentsAsString());
          // register tag's attributes
          DerivedDtd.addOrUpdateElementAttributes(
          DiasdemTextUnit.getClusterLabel(), namedEntities);
        }
        
      }
      NumberOfDocuments++;      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      
    }  // read all documents
    
    DerivedDtd.setTrainingCollectionFileName(CastParameter
    .getCollectionFileName());
    DerivedDtd.setConceptualDtdRemarks("Created " + Tools
    .getSystemDate());
    DerivedDtd.setNumberOfDocuments(NumberOfDocuments);
    DerivedDtd.setNumberOfTextUnits(NumberOfTextUnits);
    DerivedDtd.setNumberOfTaggedTextUnits(NumberOfTaggedTextUnits);
    DerivedDtd.setNumberOfUntaggedTextUnits(NumberOfUntaggedTextUnits);
    DerivedDtd.computeRelativeSupportOfElements(
    DerivedDtd.getNumberOfDocuments());
    DerivedDtd.writeXmlRepresentation(
    DiasdemProject.getProperty("DEFAULT_COLLECTION_DIRECTORY"));
    
    // create example entries in DTD documentation
    int counterExamples = 0;
    DiasdemDocument = DiasdemCollection.getFirstDocument();    
    while (DiasdemDocument != null) {
      
      counterProgress++;
      counterExamples++;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Searching Examples in Document " + counterExamples);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      // read-only task does not require backup
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits();
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        DerivedDtd.addHtmlExample(DiasdemProject, DiasdemDocument, i);
        NumberOfTextUnits++;
      }
      NumberOfDocuments++;      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      
    }  // read all documents
    
    DerivedDtd.writeDtdDocumentation(CastParameter
    .getDtdDocumentationFileName());
    try {
      DerivedDtd.save();
    }
    catch (IOException e) {
      super.setErrorTaskResult(100, shortErrorMessage,
      "Error: Conceptual DTD file cannot be saved!");
      this.stop();
    }
    super.closeDiasdemCollection();
    
    Result.update(TaskResult.FINAL_RESULT,
    "The conceptual DTD has been derived for the DIAsDEM collection\n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 60) +"!");
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}