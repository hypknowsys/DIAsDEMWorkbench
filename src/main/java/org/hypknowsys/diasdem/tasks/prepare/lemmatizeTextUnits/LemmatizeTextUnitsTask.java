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

package org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits;

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
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class LemmatizeTextUnitsTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private LemmatizeTextUnitsParameter CastParameter = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient StringTokenizer Tokenizer = null;
  private transient String TmpString = "";
  private transient String TmpString2 = "";
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Lemmatize Text Units";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits"
  + ".LemmatizeTextUnitsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits"
  + ".LemmatizeTextUnitsResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits"
  + ".LemmatizeTextUnitsControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("LEMMATIZE_TEXT_UNITS:_DEFAULT_APPEND_POS_TAG_TO_TOKENS", 
    "Lemmatize Text Units: Default Setting of Append POS Tag to Each Token",
    "false", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("LEMMATIZE_TEXT_UNITS:_LENGTH_OF_POS_TAG_TO_BE_APPENDED", 
    "Lemmatize Text Units: Maximum Length of POS Tag to be Appended",
    "1", KProperty.INTEGER, KProperty.EDITABLE)    
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public LemmatizeTextUnitsTask() {
    
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
    
    LemmatizeTextUnitsParameter parameter = null;
    if (pParameter instanceof LemmatizeTextUnitsParameter) {
      parameter = (LemmatizeTextUnitsParameter)pParameter;
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
    
    if (parameter.getAlgorithm() == LemmatizeTextUnitsParameter
    .USE_TREETAGGER_TO_DETERMINE_LEMMA_FORM) {
      
      if (parameter.getParserFileName().trim().length() <= 0
      || !parameter.getParserFileName().trim().endsWith(
      GuiClientPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter a valid local " +
        GuiClientPreferences.TEXT_FILE_EXTENSION +
        "-file name\nin the field 'TreeTagger Input File'!");
      }
      if (parameter.getTaggedFileName().trim().length() <= 0
      || !parameter.getTaggedFileName().trim().endsWith(
      GuiClientPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter a valid local " +
        GuiClientPreferences.TEXT_FILE_EXTENSION +
        "-file name\nin the field 'TreeTagger Output File'!");
      }
      if (parameter.createKnownLemmaFormsFile()
      && parameter.getLemmaFormListFileName().trim().length() <= 0 ) {
        result.addError(
        "Error: Please enter a valid local " +
        GuiClientPreferences.TEXT_FILE_EXTENSION +
        "-file name\nin the field 'Lemma Forms File'!");
      }
      
    }
    
    if (parameter.getAlgorithm() == LemmatizeTextUnitsParameter
    .LOOK_UP_LEMMA_FORM_IN_LIST) {
      
      file = new File(parameter.getLemmaFormListFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the field 'Lemma Forms File'!");
      }
      file = new File(parameter.getUnknownLemmaFormsFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the field 'Unknown Lemma Forms'!");
      }
      
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new LemmatizeTextUnitsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new LemmatizeTextUnitsResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_PREPARE_DATA_SET,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof LemmatizeTextUnitsParameter) {
      CastParameter = (LemmatizeTextUnitsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Text units cannot be lemmatized!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
        
    switch (CastParameter.getAlgorithm()) {
      case LemmatizeTextUnitsParameter.USE_TREETAGGER_TO_DETERMINE_LEMMA_FORM: {
        this.useTreeTaggerToDetermineLemmaForm();
        break;
      }
      case LemmatizeTextUnitsParameter.LOOK_UP_LEMMA_FORM_IN_LIST: {
        this.lookUpLemmaFormInList();
        break;
      }
    }
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT, 
    "All text units have been lemmatized in the collection\n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) + "!");
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
  
  private void useTreeTaggerToDetermineLemmaForm() {

    HashMap newLemmaForms = new HashMap();
    String newTerm = null;
    String newLemmaTerm = null;
    
    TextFile parserFile = new TextFile( 
      new File(CastParameter.getParserFileName()));
    parserFile.empty();
    parserFile.open();

    int counterProgress = 1;
    long maxProgress = 2 * DiasdemCollection.getNumberOfDocuments();

    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Step 1/3: Exporting Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      
      parserFile.setNextLine("<Document_" + DiasdemDocument
      .getDiasdemDocumentID() + ">");
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits(); 
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        parserFile.setNextLine("<ProcessedTextUnit_" + i + ">");
        parserFile.setNextLine(DiasdemTextUnit.getContentsAsString());
        parserFile.setNextLine("</ProcessedTextUnit_" + i + ">");
      }

      parserFile.setNextLine("</Document_" + DiasdemDocument
      .getDiasdemDocumentID() + ">");
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents

    parserFile.close();

    System.out.println("### Start of POS Tagging using TreeTagger ...");
    Progress.update( TaskProgress.INDETERMINATE, "Step 2/3: POS Tagging ..."); 
    DiasdemServer.setTaskProgress(Progress, TaskThread);

    TextFile taggedFile = new TextFile(
    new File(CastParameter.getTaggedFileName()));
    taggedFile.empty();
    taggedFile.close();
    
    try {
      Process p = Runtime.getRuntime().exec(CastParameter.getTreeTaggerCommand()
      + " " +  CastParameter.getParserFileName());
      FileOutputStream taggedFileStream = new FileOutputStream(CastParameter
      .getTaggedFileName());
      BufferedOutputStream taggedFileBuffer = new BufferedOutputStream(
      taggedFileStream);
      StreamHandler outputStreamHandler = new StreamHandler(p.getInputStream(), 
      null, taggedFileBuffer);
      StreamHandler errorStreamHandler = new StreamHandler(p.getErrorStream(), 
      null, System.out);
      outputStreamHandler.start();
      errorStreamHandler.start();
      int exitValue = p.waitFor();
      while (!outputStreamHandler.isFinished()) {}
      while (!errorStreamHandler.isFinished()) {}
      taggedFileBuffer.flush();
      taggedFileBuffer.close();
      System.out.flush();
      outputStreamHandler = null;
      errorStreamHandler = null;
      System.out.println("### End of POS Tagging using TreeTagger. ExitValue = "
      + exitValue);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.setErrorTaskResult(100, "Error: Text units cannot be lemmatized!",
      "Error: The document collection cannot be lemmatized,\n" +
      "because TreeTagger fails to execute! See stack trace.");
      taggedFile.close();
      this.stop();
    }
    taggedFile.close();
    
    Progress.update( (int)(counterProgress * 100 / maxProgress), 
      "Step 3/3: Updating Document " + counterProgress); 
    DiasdemServer.setTaskProgress(Progress, TaskThread);

    taggedFile = new TextFile(new File(CastParameter.getTaggedFileName()));
    taggedFile.open();
    DiasdemDocument = null;
   
    boolean validDiasdemDocument = true;
    String taggedLine = taggedFile.getFirstLine();
    StringBuffer resultLine = new StringBuffer(10000);
    String token = "";
    String tag = "";
    int lengthOfPosTagToBeAppended = Math.max(0, DiasdemProject.getIntProperty(
    "LEMMATIZE_TEXT_UNITS:_LENGTH_OF_POS_TAG_TO_BE_APPENDED"));
    String lemma = "";
    int textUnitIndex = -1;
    while (taggedLine != null ) {
      
      //System.out.println(taggedLine);
     if (taggedLine.startsWith("<Document_")) {
       
       if (counterProgress == 1 || (counterProgress % 50) == 0) {
         Progress.update( (int)(counterProgress * 100 / maxProgress), 
           "Step 3/3: Updating Document " + counterProgress / 2); 
         DiasdemServer.setTaskProgress(Progress, TaskThread);
       }
       counterProgress++;
       DiasdemDocument = DiasdemCollection.getDocument(
       taggedLine.substring(10, taggedLine.length() - 1));      
       if (DiasdemDocument == null) {
         this.setErrorTaskResult(100, "Error: Text units cannot be lemmatized!",
         "Error: The TreeTagger output file references the non-existing\n" + 
         "DIAsDEM document " + Tools.shortenFileName(taggedLine.substring(10, 
         taggedLine.length() - 1), 50) + "!");
         this.stop();
       }
       DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
       .getActiveTextUnitsLayerIndex());
       DiasdemDocument.backupProcessedTextUnits(DiasdemProject
       .getProcessedTextUnitsRollbackOption());

       validDiasdemDocument = true;
       
     } else if (taggedLine.startsWith("<ProcessedTextUnit_") ) {
       
       resultLine = new StringBuffer(10000);
       textUnitIndex = Tools.string2Int(taggedLine.substring(19, 
       taggedLine.length() - 1));
       
     } else if (taggedLine.startsWith("</ProcessedTextUnit_") ) {
        
       if (validDiasdemDocument) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(textUnitIndex);
        DiasdemTextUnit.setContentsFromString(resultLine.toString().trim());
        DiasdemDocument.replaceProcessedTextUnit(textUnitIndex, DiasdemTextUnit);
       }
       
     } else if (taggedLine.startsWith("</Document") ) {
       
       if (validDiasdemDocument) {
         DiasdemCollection.replaceDocument(DiasdemDocument
         .getDiasdemDocumentID(), DiasdemDocument);
       }
       DiasdemDocument = null;
       
     } else if (taggedLine.startsWith(NamedEntity.PLACEHOLDER_PREFIX)) {
       
       resultLine.append(taggedLine.trim());
       resultLine.append(" ");
       
     } else if (!taggedLine.startsWith("<") && taggedLine.indexOf("\t") >= 0
     && taggedLine.lastIndexOf("\t") > taggedLine.indexOf("\t")) {
       
       // skip other TML tags etc., check for existrence of tabs
       int indexTag = taggedLine.indexOf("\t") + 1;
       int indexLemma  = taggedLine.lastIndexOf("\t") + 1;
       // System.out.println(taggedLine + " " + indexTag + 
       //   " " + indexLemma );
       if ( indexTag > 0 && indexLemma > 0 && 
       indexLemma <= taggedLine.length() ) { 
         token = taggedLine.substring(0, indexTag - 1);
         tag = taggedLine.substring(indexTag, indexLemma - 1);
         lemma = taggedLine.substring(indexLemma);
         if (lemma.startsWith("<unknown>")) {
           lemma = token; // + "_unknown";
         }
         else if (lemma.startsWith("@card@")) {
           lemma = token; // + "_card"; 
         }
         else if (lemma.startsWith("@ord@")) {
           lemma = token; // + "_ord"; 
         }
         else if (lemma.indexOf("|") >= 0) {
           lemma = token; // + "_|"; 
         }
         else if (tag.startsWith("NE")) {
           lemma = token; // + "_NE"; 
         }
         resultLine.append(lemma);
         if (CastParameter.appendPosTagToEachToken()) {
           if (Tools.stringContainsLetter(token) 
           && !NamedEntity.isPlaceholder(token)) {
             resultLine.append("/p:");
             resultLine.append(tag.substring(0, Math.min(
             lengthOfPosTagToBeAppended, tag.length())));
           }
         }
         resultLine.append(" ");
         // System.out.println("TOKEN=" + token + " TAG=" + 
         //   tag + " LEMMA=" + lemma + " --  ");
         
         if (CastParameter.createKnownLemmaFormsFile()) {
           if (Tools.stringContainsLetter(token)
           && !NamedEntity.isPlaceholder(token)) {
             newLemmaTerm = (String)newLemmaForms.get(token);
             if (newLemmaTerm == null) {
               newLemmaForms.put(token, lemma);
             }
             else if (!lemma.equals(newLemmaTerm)) {
               newLemmaForms.put(token, lemma);
             }
           }
         }
         
       }
       
     }      
     taggedLine = taggedFile.getNextLine();
     
   }

   taggedFile.close();    
   
   if (CastParameter.createKnownLemmaFormsFile()) {
     Progress.update( TaskProgress.INDETERMINATE, 
     "Saving List of Lemma Forms ... "); 
     DiasdemServer.setTaskProgress(Progress, TaskThread);
    
     TextFile textFile = new TextFile(new File(
     CastParameter.getLemmaFormListFileName()));
     textFile.open(); 
     textFile.setFirstLine("# This file contains terms and their lemma forms as determined by the POS");
     textFile.setNextLine("# tagger TreeTagger. DIAsDEM Workbench 2.1 can only process single-token");
     textFile.setNextLine("# terms (e.g., 'ist') which do not contain blank spaces. Terms are not");
     textFile.setNextLine("# looked up case-sensitively. Hence, both terms 'ist' and 'IST' will be");
     textFile.setNextLine("# mapped onto the grammatical root 'sein'. Each line contains exactly one");
     textFile.setNextLine("# term and its lemma form separated by exactly one tab stop. This file was");
     textFile.setNextLine("# created by the DIAsDEM task Actions -> Prepare Data Set -> Lemmatize");
     textFile.setNextLine("# Text Units using the advanced option 'Create New Known Lemma Forms File'.");
     Iterator iterator = newLemmaForms.keySet().iterator();
     while ( iterator.hasNext() ) {
       newTerm = (String)iterator.next();
       newLemmaTerm = (String)newLemmaForms.get(newTerm);
       textFile.setNextLine(newTerm + "\t" + newLemmaTerm);
     }
     textFile.close();
   }

    
  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private void lookUpLemmaFormInList() {
    
    Progress.update( TaskProgress.INDETERMINATE, 
      "Reading List of Lemma Forms ..."); 
    DiasdemServer.setTaskProgress(Progress, TaskThread);

    // read list of known lemma forms
    TreeMap lemmaForms = new TreeMap();
    TextBufferedReader textBufferedReader = new TextBufferedReader( 
      new File(CastParameter.getLemmaFormListFileName()));
    textBufferedReader.open();
    String term = null;
    String lemmaForm = null;
    String line = textBufferedReader
    .getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      if ( line.indexOf("\t") >= 0 
      && (line.indexOf("\t") + 1) < line.length() ) {
        term = line.substring( 0, line.indexOf("\t") ).toLowerCase(); 
        lemmaForm = line.substring( line.indexOf("\t") + 1 ); 
        lemmaForms.put(term, lemmaForm);
      }
      line = textBufferedReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textBufferedReader.close();
    
    TreeMap unknownLemmaForms = new TreeMap();

    int counterProgress = 1;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress), 
          "Processing File " + counterProgress); 
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
        DiasdemTextUnit.setContentsFromString(this.lookUpLemmaForms(
        TextUnitContentsAsString, lemmaForms, unknownLemmaForms, 
        DiasdemDocument.getProcessedTextUnit(i).getContentsAsString()));
        DiasdemDocument.replaceProcessedTextUnit(i, DiasdemTextUnit);
      }

      DiasdemCollection.replaceDocument(DiasdemDocument.getDiasdemDocumentID(),
      DiasdemDocument);
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;

    }  // read all documents

    Progress.update( TaskProgress.INDETERMINATE, 
      "Saving List of Unknown Lemma Forms ... "); 
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    TextFile textFile = new TextFile(new File(
      CastParameter.getUnknownLemmaFormsFileName()));
    textFile.open();
    Iterator iterator = unknownLemmaForms.keySet().iterator();
    while ( iterator.hasNext() ) {
      TmpString = (String)iterator.next();
      if ( ! NamedEntity.isPlaceholder(TmpString) 
      && ! TmpString.matches("[\\d\\,\\.\\;\\-\\+]*") )
        textFile.setNextLine( TmpString + "\t" 
        + (String)unknownLemmaForms.get(TmpString) );
    }
    textFile.close();
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String lookUpLemmaForms(String pInputLine, TreeMap pLemmaFormsList,
  TreeMap pUnknownLemmaForms, String pOriginalContext) {

    TmpStringBuffer = new StringBuffer(pInputLine.length());
    TmpString = "";
    Tokenizer = new StringTokenizer(pInputLine);
    while (Tokenizer.hasMoreElements()) {
      TmpString = Tokenizer.nextToken();
      TmpString2 = (String)pLemmaFormsList.get(TmpString.toLowerCase());
      if (TmpString2 != null) {
        TmpStringBuffer.append(TmpString2);
      }
      else {
        TmpStringBuffer.append(TmpString);
        if (Tools.stringContainsLetter(TmpString)
        && !NamedEntity.isPlaceholder(TmpString)) {
          pUnknownLemmaForms.put(TmpString, pOriginalContext);
        }
      }
      TmpStringBuffer.append(" ");
    }

    return TmpStringBuffer.toString().trim();

  }   

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}