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

package org.hypknowsys.diasdem.tasks.prepare.createTextUnits;

import java.io.*;
import java.util.*;
import java.util.regex.*;
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
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class CreateTextUnitsTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private CreateTextUnitsParameter CastParameter = null;
  
  private TextUnitCreator MyTextUnitCreator = null;
  private DIAsDEMtextUnit[] TmpTextUnits = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient Pattern TmpPattern = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Create Text Units";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createTextUnits"
  + ".CreateTextUnitsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createTextUnits"
  + ".CreateTextUnitsResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.createTextUnits"
  + ".CreateTextUnitsControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public CreateTextUnitsTask() {
    
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
    
    CreateTextUnitsParameter parameter = null;
    if (pParameter instanceof CreateTextUnitsParameter) {
      parameter = (CreateTextUnitsParameter)pParameter;
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
    file = new File(parameter.getAbbreviationsFileName());
    if (!file.exists() || !file.isFile()) {
      result.addError(
      "Error: Please enter the name of an existing\n" +
      "local file in the field 'Abbreviations File'!");
    }
    file = new File(parameter.getRegexFileName());
    if (!file.exists() || !file.isFile()) {
      result.addError(
      "Error: Please enter the name of an existing\n" +
      "local file in the field 'Full Stop Regex File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new CreateTextUnitsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new CreateTextUnitsResult();
    
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
    
    if (Parameter != null && Parameter instanceof CreateTextUnitsParameter) {
      CastParameter = (CreateTextUnitsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, "Error: Text units cannot be created!");
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    
    String algorithmInfo = "";
    int activeTextUnitsLayerIndex = -1;

    if (CastParameter.getAlgorithm() == 
    CreateTextUnitsParameter.HEURISTIC_SENTENCE_IDENTIFIER ||
    CastParameter.getAlgorithm() == 
    CreateTextUnitsParameter.TEXT_AS_A_SINGLE_TEXT_UNIT) {
      
      // read abbreviations: each line contains one abbreviation;
      // comment lines start with '#'
      TextFile abbreviationsFile = new TextFile(
      new File(CastParameter.getAbbreviationsFileName()));
      abbreviationsFile.open();
      String line = abbreviationsFile
      .getFirstLineButIgnoreCommentsAndEmptyLines();
      ArrayList list = new ArrayList();
      while (line != null) {
        list.add(line.trim());
        line = abbreviationsFile.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      // sort abbreviations by decreasing length
      Collections.sort(list, new SortStringsByDecreasingLength());
      // for (int i = 0; i < list.size(); i++) {
      //   System.out.println((String)list.get(i));
      // }
      // System.out.println(list.size());
      String[] abbreviations = new String[list.size()];
      for (int i = 0; i < list.size(); i++) {
        abbreviations[i] = (String)list.get(i);
      }
      abbreviationsFile.close();
      
      // read regex file: each line contains the regex to search for
      // and the replacement string separated by a tab stop;
      // comment lines start with '#'
      TextFile regexFile = new TextFile(new File(CastParameter
      .getRegexFileName()));
      regexFile.open();
      line = regexFile.getFirstLineButIgnoreCommentsAndEmptyLines();
      ArrayList searchList = new ArrayList();
      ArrayList replaceList = new ArrayList();
      String[] contents = null;
      while (line != null) {
        contents = line.split("\t");
        if (contents.length == 2 && contents[1].trim().length() > 0) {
          try {
            TmpPattern = Pattern.compile(contents[0].trim());
            searchList.add(contents[0].trim());
            replaceList.add(contents[1].trim());
          }
          catch (PatternSyntaxException e) {
            System.out.println("[CreateTextUnitsTask] Regex syntax error in "
            + " file " + CastParameter.getRegexFileName() + ": "
            + " Line \"" + line + "\"; error message: " + e.getMessage());
          }
        }
        else {
          System.out.println("[CreateTextUnitsTask] Error in file "
          + CastParameter.getRegexFileName() + ": Line \""
          + line + "\" does not conform to syntax!");
        }
        line = regexFile.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      String[] regexSearch = new String[searchList.size()];
      for (int i = 0; i < searchList.size(); i++)
        regexSearch[i] = (String)searchList.get(i);
      String[] regexReplace = new String[replaceList.size()];
      for (int i = 0; i < replaceList.size(); i++)
        regexReplace[i] = (String)replaceList.get(i);
      regexFile.close();
      
      // create instance
      switch (CastParameter.getAlgorithm()) {
        case CreateTextUnitsParameter.HEURISTIC_SENTENCE_IDENTIFIER: {
          algorithmInfo = "HEURISTIC_SENTENCE_IDENTIFIER";
          MyTextUnitCreator = new HeuristicSentenceIdentifier(abbreviations,
          regexSearch, regexReplace, CastParameter.getKeepAsterisks());
          break;
        }
        case CreateTextUnitsParameter.TEXT_AS_A_SINGLE_TEXT_UNIT: {
          algorithmInfo = "TEXT_AS_A_SINGLE_TEXT_UNIT";
          MyTextUnitCreator = new TextAsSingleTextUnitIdentifier(abbreviations,
          regexSearch, regexReplace, CastParameter.getKeepAsterisks());
          break;
        }
      }
    }
    else {
      MyTextUnitCreator = new DefaultTextUnitCreator();      
    }
    
    int textUnitCounter = 0;
    int counterProgress = 0;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      TmpTextUnits = MyTextUnitCreator.createTextUnits(DiasdemDocument
      .getOriginalText());

      if (CastParameter.getTextUnitsLayer() == CreateTextUnitsParameter
      .CREATE_NEW_TEXT_UNITS_LAYER) {
        activeTextUnitsLayerIndex = DiasdemDocument.createTextUnitsLayer(
        "Algorithm: " + algorithmInfo, true);    
      }
      else {
        DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
        .getActiveTextUnitsLayerIndex());
        activeTextUnitsLayerIndex = DiasdemDocument.resetActiveTextUnitsLayer(
        "Algorithm: " + algorithmInfo);    
      }
      if (TmpTextUnits != null && TmpTextUnits.length > 0) {
        for (int i = 0; i < TmpTextUnits.length; i++) {
          DiasdemTextUnit = (DIAsDEMtextUnit)TmpTextUnits[i].clone();
          DiasdemTextUnit.setBeginIndex(-1);
          DiasdemTextUnit.setEndIndex(-1);
          DiasdemDocument.addProcessedTextUnit(DiasdemTextUnit);
          if (CastParameter.getKeepAsterisks()) {
            TmpTextUnits[i].setContentsFromString(TmpTextUnits[i]
            .getContentsAsString().replace('*', '.'));
          }
          DiasdemDocument.addOriginalTextUnit(TmpTextUnits[i], false);
          textUnitCounter++;
        }
      }
      else {
        DiasdemDocument.addOriginalTextUnit(
        new DefaultDIAsDEMtextUnit("null", 0), true);
        textUnitCounter++;
      }
      
      DiasdemCollection.replaceDocument(DiasdemDocument.getDiasdemDocumentID(),
      DiasdemDocument);
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents
    
    DiasdemCollection.setNumberOfTextUnits(textUnitCounter);
    DiasdemCollection.setNumberOfUntaggedTextUnits(textUnitCounter);
    super.closeDiasdemCollection();
    
    Result.update(TaskResult.FINAL_RESULT,
    textUnitCounter + " text units have been identified in the collection\n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) + "!\n" +
    "They have been associated with text units layer " + 
    activeTextUnitsLayerIndex + ".");
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
  
  class SortStringsByDecreasingLength implements Comparator {
    
    public int compare(Object pObject1, Object pObject2) {
      String string1 = (String)pObject1;
      String string2 = (String)pObject2;
      if (string1.length() < string2.length()) {
        return 1;
      }
      else if (string1.length() > string2.length()) {
        return -1;
      }
      else {
        return 0;
      }
    }
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}