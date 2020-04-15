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

package org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.text.*;
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

public class DeriveStructuredDtdTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DeriveStructuredDtdParameter CastParameter = null;

  private DIAsDEMpreliminaryDtd MyUnstructuredDtd = null;
  private StringTokenizer Tokenizer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient StringTokenizer TmpStringTokenizer = null;
  private transient String TmpString = null;
  private transient Iterator TmpIterator = null;
  private transient DIAsDEMpreliminaryDtdElement TmpElement = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Derive Structured DTD";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd.DeriveStructuredDtdParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd.DeriveStructuredDtdResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd.DeriveStructuredDtdControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DEFAULT_STRUCTURED_DTD_FILE", 
    "Default structured DTD file",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_WUM_ASSOC_RULES_FILE", 
    "Default file containing assoc rules discovered by WUM",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_WUM_SEQUENCES_FILE", 
    "Default file containing sequences discovered by WUM",
    "", KProperty.STRING, KProperty.EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DeriveStructuredDtdTask() {
    
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
    
    DeriveStructuredDtdParameter parameter = null;
    if (pParameter instanceof DeriveStructuredDtdParameter) {
      parameter = (DeriveStructuredDtdParameter)pParameter;
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
    file = new File(parameter.getUnstructuredDtdFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.PRELIMINARY_DTD_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.PRELIMINARY_DTD_FILE_EXTENSION +
      "-file in the field 'Unstructured DTD File'!");
    }
    if (parameter.getStructuredDtdFileName().trim().length() <= 0) {
      result.addError(
      "Error: Please enter a valid local file name\n" +
      "in the field 'Structured DTD File'!");
    }
    file = new File(parameter.getWumAssocRulesFileName());
    if (!file.exists() || !file.isFile()) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      "file in the field 'WUM Assoc. Rules File'!");
    }
    file = new File(parameter.getWumSequencesFileName());
    if (!file.exists() || !file.isFile()) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      "file in the field 'WUM Sequences File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new DeriveStructuredDtdParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new DeriveStructuredDtdResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_POSTPROCESS_PATTERNS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof DeriveStructuredDtdParameter) {
      CastParameter = (DeriveStructuredDtdParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, 
    "Error: Structured DTD cannot be derived!");        
    
    // test: read all association rules and create instances
    StructuredDtdAssocRule assocRule = null;
    TextFile assocFile = new TextFile(
      new File(CastParameter.getWumAssocRulesFileName()));
    assocFile.open();
    StructuredDtdSequence sequence = null;
    TextFile sequencesFile = new TextFile(
      new File(CastParameter.getWumSequencesFileName()));
    sequencesFile.open();

    // code should be moved StruturedDtd

    NumberFormat formatDouble = NumberFormat.getInstance();
    formatDouble.setGroupingUsed(false);
    formatDouble.setMinimumFractionDigits(3);
    formatDouble.setMaximumFractionDigits(3);

    TreeMap vertices = new TreeMap();
    long nextVertexID = 1L;
    long sourceVertexID = 0L;
    StructuredDtdVertex sourceVertex = null;
    long targetVertexID = 0L;
    StructuredDtdVertex targetVertex = null;
    TreeMap edges = new TreeMap();
    long nextEdgeID = 1L;
    StructuredDtdEdge edge = null;
    Iterator elementIterator = MyUnstructuredDtd.getElementIterator();
    while ( elementIterator.hasNext() ) {
      TmpElement = (DIAsDEMpreliminaryDtdElement)elementIterator.next();
      if (TmpElement.getRelSupport() >= CastParameter.getMinTagRelSupport())
        vertices.put(TmpElement.getName(), new StructuredDtdVertex(
          nextVertexID++, "T: " + TmpElement.getName() + ";" + 
          formatDouble.format( TmpElement.getRelSupport() ), 
          null ) );  // add LabelArray
    }

    int counterProgress = 0;
    int maxProgress = (int)( (
      new File(CastParameter.getWumAssocRulesFileName() ) ).length() ) / 
      (int)( assocFile.getFirstLine().length() ) + (int)( (
      new File(CastParameter.getWumSequencesFileName() ) ).length() ) / 
      (int)( assocFile.getFirstLine().length() );

    String currentLine = assocFile.getFirstLine();
    while (currentLine != null) {

      counterProgress++;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress), 
          "Processing WUM Association Rules " + counterProgress); 
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }

      if ( !currentLine.startsWith("#") ) {
        assocRule = new StructuredDtdAssocRule();
        try {
          assocRule.fromWumExportItemLine(currentLine);
          if (assocRule.getConsequentCardinality() == 1
            && assocRule.getAntecedentCardinality() < 3) {  // ###### debug
            assocRule.setLift( assocRule.getConfidence() /
              MyUnstructuredDtd.getElement( 
              assocRule.getConsequent() ).getRelSupport() );
            // System.out.println(assocRule);
            // extend graph
            if ( assocRule.getRelSupport() >= CastParameter.getMinAssocRelSupport() 
              && assocRule.getConfidence() >= CastParameter.getMinAssocConfidence()
              && assocRule.getLift() >= CastParameter.getMinAssocLift() ) {
              targetVertex = (StructuredDtdVertex)vertices.get( 
                assocRule.getConsequent() );
              if (targetVertex != null) {
                // System.out.println(targetVertex + " #target# " + assocRule);
                targetVertexID = targetVertex.getID();
              }
              else {
                targetVertexID = nextVertexID++;
                vertices.put(assocRule.getConsequent(), new StructuredDtdVertex(
                  targetVertexID, assocRule.getConsequent() + ";" + 
                  formatDouble.format( assocRule.getRelSupport() )
                  , null ) );  // LabelArray
              }
              sourceVertex = (StructuredDtdVertex)vertices.get( 
                assocRule.getAntecedent() );
              if (sourceVertex != null) {
                // System.out.println(sourceVertex + " #source# " + assocRule);
                sourceVertexID = sourceVertex.getID();
              }
              else {
                sourceVertexID = nextVertexID++;
                vertices.put(assocRule.getAntecedent(), new StructuredDtdVertex(
                  sourceVertexID, assocRule.getAntecedent() + ";" + 
                  formatDouble.format( assocRule.getAntecedentRelSupport() )
                  , null ) );  // LabelArray
              }
              edge = (StructuredDtdEdge)vertices.get(
                assocRule.getAntecedent() + " -> " + 
                assocRule.getConsequent() );
              if (edge != null) {
                // System.out.println(edge + " #edge# " + assocRule);
              }
              else {
                edges.put(assocRule.getAntecedent() + " -> " +
                  assocRule.getConsequent(), new StructuredDtdEdge(nextEdgeID++,
                  sourceVertexID, targetVertexID, assocRule.getAntecedent() + 
                  " -> " + assocRule.getConsequent() + ";" +
                  formatDouble.format( assocRule.getConfidence() ) +";" +
                  formatDouble.format( assocRule.getLift() ) + ";NULL;NULL",
                  null ) );  // LabelArray
              }
            } 
          }
        }
        catch (NumberFormatException e) {
          System.out.println("CANNOT CREATE ASSOC RULE: " + currentLine);
        }
      }
      currentLine = assocFile.getNextLine();
    } // while: read assoc rules

    DIAsDEMpreliminaryDtdElement element = null;
    currentLine = sequencesFile.getFirstLine();
    while (currentLine != null) {

      counterProgress++;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress), 
          "Processing WUM Sequences " + counterProgress); 
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }

      if ( !currentLine.startsWith("#") ) {
        sequence = new StructuredDtdSequence();
        try {
          System.out.println("SequenceLine: " + currentLine);
          sequence.fromWumExportItemLine(currentLine);
          System.out.println("Sequence: " + sequence);
          if (sequence.getConsequentCardinality() == 1
            && sequence.getConsequent().indexOf(";") < 0
            && sequence.getConsequent().indexOf("UntaggedTextUnit") < 0
            && sequence.getAntecedent().indexOf("UntaggedTextUnit") < 0
            && sequence.getAntecedentCardinality() < 3) {  // ###### debug
            sequence.setLift( sequence.getConfidence() /
              MyUnstructuredDtd.getElement( 
              sequence.getConsequent() ).getRelSupport() );
            // System.out.println(sequence);
            // extend graph
            if ( sequence.getRelSupport() >= CastParameter.getMinSeqRelSupport() 
              && sequence.getConfidence() >= CastParameter.getMinSeqConfidence()
              && sequence.getLift() >= CastParameter.getMinSeqLift() ) {
              targetVertex = (StructuredDtdVertex)vertices.get( 
                sequence.getConsequent() );
              if (targetVertex != null) {
                System.out.println(targetVertex + " #target# " + sequence);
                targetVertexID = targetVertex.getID();
              }
              else {
                targetVertexID = nextVertexID++;
                vertices.put(sequence.getConsequent(), new StructuredDtdVertex(
                  targetVertexID, sequence.getConsequent() + ";" + 
                  formatDouble.format( sequence.getRelSupport() )
                  , null ) );  // LabelArray
              }
              sourceVertex = (StructuredDtdVertex)vertices.get( 
                sequence.getAntecedent() );
              if (sourceVertex != null) {
                System.out.println(sourceVertex + " #source# " + sequence);
                sourceVertexID = sourceVertex.getID();
              }
              else {
                sourceVertexID = nextVertexID++;
                vertices.put(sequence.getAntecedent(), new StructuredDtdVertex(
                  sourceVertexID, sequence.getAntecedent() + ";" + 
                  formatDouble.format( sequence.getAntecedentRelSupport() )
                  , null ) );  // LabelArray
              }
              edge = (StructuredDtdEdge)vertices.get(
                sequence.getAntecedent() + " o> " + 
                sequence.getConsequent() );
              if (edge != null) {
                System.out.println(edge + " #edge# " + sequence);
              }
              else {
                edges.put(sequence.getAntecedent() + " o> " +
                  sequence.getConsequent(), new StructuredDtdEdge(nextEdgeID++,
                  sourceVertexID, targetVertexID, sequence.getAntecedent() + 
                  " o> " + sequence.getConsequent() + ";NULL;NULL;" +
                  formatDouble.format( sequence.getConfidence() ) +";" +
                  formatDouble.format( sequence.getLift() ),
                  null ) );  // LabelArray
              }
            } 
          }

        }
        catch (NumberFormatException e) {
          System.out.println("CANNOT CREATE SEQUENCE: " + currentLine);
        }
      }
      currentLine = sequencesFile.getNextLine();
    } // while: read sequences

    // write GML text file; code should be move to StructuredDtd
    TextFile structuredDtdGmlFile = new TextFile( new File(
    CastParameter.getStructuredDtdFileName() + ".gml" ) );
    structuredDtdGmlFile.open();
    structuredDtdGmlFile.setFirstLine("graph [");
    structuredDtdGmlFile.setNextLine("  directed 1");
    structuredDtdGmlFile.setNextLine("  id 1"); 
    structuredDtdGmlFile.setNextLine("  label \"DIAsDEM Structured DTD\""); 
    TmpIterator = vertices.values().iterator();
    while ( TmpIterator.hasNext() )
      ( (StructuredDtdVertex)TmpIterator.next() ).writeGmlRepresentation(
        structuredDtdGmlFile);
    TmpIterator = edges.values().iterator();
    while ( TmpIterator.hasNext() )
      ( (StructuredDtdEdge)TmpIterator.next() ).writeGmlRepresentation(
        structuredDtdGmlFile);
    structuredDtdGmlFile.setNextLine("]");

    // write XGMML text file; code should be move to StructuredDtd

// <?xml version="1.0" encoding="UTF-8"?>
// <!DOCTYPE graph PUBLIC "-//John Punin//DTD graph description//EN"
//                        "http://www.cs.rpi.edu/~puninj/XGMML/xgmml.dtd">
// <graph Graphic="1" directed="1">
// </graph>

    TextFile structuredDtdXgmmlFile = new TextFile( new File(
    CastParameter.getStructuredDtdFileName() + ".xgmml" ) );
    structuredDtdXgmmlFile.open();
    structuredDtdXgmmlFile.setFirstLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <!DOCTYPE graph PUBLIC \"-//John Punin//DTD graph description//EN\" \"http://www.cs.rpi.edu/~puninj/XGMML/xgmml.dtd\"> <graph Graphic=\"1\" directed=\"1\">");
    TmpIterator = vertices.values().iterator();
    while ( TmpIterator.hasNext() )
      ( (StructuredDtdVertex)TmpIterator.next() ).writeXgmmlRepresentation(
        structuredDtdXgmmlFile);
    TmpIterator = edges.values().iterator();
    while ( TmpIterator.hasNext() )
      ( (StructuredDtdEdge)TmpIterator.next() ).writeXgmmlRepresentation(
        structuredDtdXgmmlFile);
    structuredDtdXgmmlFile.setNextLine("</graph>");

    structuredDtdXgmmlFile.close();
    structuredDtdGmlFile.close();
    assocFile.close();
    sequencesFile.close();

    Result.update(TaskResult.FINAL_RESULT, 
      "The structured DIAsDEM DTD has been derived!");
    this.setTaskResult(100, "All XML Files Processed ...", Result,
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