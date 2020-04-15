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

package org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles;

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
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class MergeParameterTextFilesTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private MergeParameterTextFilesParameter CastParameter = null;
  private MergeParameterTextFilesResult CastResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Merge Parameter Text Files";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles"
  + ".MergeParameterTextFilesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles"
  + ".MergeParameterTextFilesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles"
  + ".MergeParameterTextFilesControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME1", 
    "Task: Merge Parameter Text Files; MRU Source Text File 1",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME2", 
    "Task: Merge Parameter Text Files; MRU Source Text File 2",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME3", 
    "Task: Merge Parameter Text Files; MRU Source Text File 3",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MERGE_PARAMETER_FILES:_MRU_SOURCE_FILE_NAME4", 
    "Task: Merge Parameter Text Files; MRU Source Text File 4",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MERGE_PARAMETER_FILES:_MRU_TARGET_FILE_NAME",
    "Task: Merge Parameter Text Files; MRU Target Text File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MERGE_PARAMETER_FILES:_DEFAULT_MERGE_TYPE_INDEX",
    "Task: Merge Parameter Text Files; Default Merge Type Index",
    "0", KProperty.INTEGER, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public MergeParameterTextFilesTask() {
    
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
    
    MergeParameterTextFilesParameter parameter = null;
    if (pParameter instanceof MergeParameterTextFilesParameter) {
      parameter = (MergeParameterTextFilesParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    if (parameter.getSourceParameterFileName1().trim().equals(
    parameter.getTargetParameterFileName().trim())) {
      result.addError(
      "Error: 'Source Parameter File 1' must be\n" +
      "different from 'Target Parameter File'!");
    }
    if (parameter.getSourceParameterFileName2().trim().equals(
    parameter.getTargetParameterFileName().trim())) {
      result.addError(
      "Error: 'Source Parameter File 2' must be\n" +
      "different from 'Target Parameter File'!");
    }
    if (!Tools.stringIsNullOrEmpty(parameter.getSourceParameterFileName3()
    .trim()) && parameter.getSourceParameterFileName3().trim().equals(
    parameter.getTargetParameterFileName().trim())) {
      result.addError(
      "Error: 'Source Parameter File 3' must be\n" +
      "different from 'Target Parameter File'!");
    }
    if (!Tools.stringIsNullOrEmpty(parameter.getSourceParameterFileName4()
    .trim()) && parameter.getSourceParameterFileName4().trim().equals(
    parameter.getTargetParameterFileName().trim())) {
      result.addError(
      "Error: 'Source Parameter File 4' must be\n" +
      "different from 'Target Parameter File'!");
    }
    File file = new File(parameter.getSourceParameterFileName1());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Source Parameter File 1'!");
    }
    file = new File(parameter.getSourceParameterFileName2());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Source Parameter File 2'!");
    }
    if (!Tools.stringIsNullOrEmpty(parameter.getSourceParameterFileName3())) {
      file = new File(parameter.getSourceParameterFileName3());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the field 'Source Parameter File 3'!");
      }
    }
    if (!Tools.stringIsNullOrEmpty(parameter.getSourceParameterFileName4())) {
      file = new File(parameter.getSourceParameterFileName4());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the field 'Source Parameter File 4'!");
      }
    }
    if (parameter.getTargetParameterFileName().trim().length() <= 0
    || !parameter.getTargetParameterFileName().trim().endsWith(
    DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file name\nin the field 'Target Parameter File'!");
    }
    else {
      file = new File(parameter.getTargetParameterFileName());
      if (file.exists() && parameter.getMergeType()
      == MergeParameterTextFilesParameter.CREATE_OR_REPLACE_TARGET) {
        result.addWarning(
        "Warning: The file specified in the field\n" +
        "'Target Parameter File' currently exists.\n" +
        "Do you really want to replace this file?");
      }
    }
     
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new MergeParameterTextFilesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new MergeParameterTextFilesResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_MISCELLANEOUS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof MergeParameterTextFilesParameter) {
      CastParameter = (MergeParameterTextFilesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Parameter text files cannot be merged!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    
    TextFile sourceParameterFile1 = new TextFile(new File(CastParameter
    .getSourceParameterFileName1()));
    sourceParameterFile1.openReadOnly();
    TextFile sourceParameterFile2 = new TextFile(new File(CastParameter
    .getSourceParameterFileName2()));
    sourceParameterFile2.openReadOnly();
    
    String sourceLine = null;
    String targetLine = null;
    int counterSuccess = 0;
    int counterProgress = 0;
    int counterProgressLines = 0;
    long maxProgress = ( new File(CastParameter
    .getSourceParameterFileName1()) ).length() + ( new File(CastParameter
    .getSourceParameterFileName2()) ).length();
    int lengthOfLineFeedChars = Tools.getLengthOfLineFeedChars();

    TextFile sourceParameterFile3 = null;
    if (!Tools.stringIsNullOrEmpty(CastParameter.getSourceParameterFileName3())) {
      sourceParameterFile3 = new TextFile(new File(CastParameter
      .getSourceParameterFileName3()));
      sourceParameterFile3.openReadOnly();
      maxProgress += ( new File(CastParameter
      .getSourceParameterFileName3()) ).length();
    }
    TextFile sourceParameterFile4 = null;
    if (!Tools.stringIsNullOrEmpty(CastParameter.getSourceParameterFileName4())) {
      sourceParameterFile4 = new TextFile(new File(CastParameter
      .getSourceParameterFileName4()));
      sourceParameterFile4.openReadOnly();
      maxProgress += ( new File(CastParameter
      .getSourceParameterFileName4()) ).length();
    }
    
    TextFile targetParameterFile = new TextFile(new File(CastParameter
    .getTargetParameterFileName()));
    if (CastParameter.getMergeType() == MergeParameterTextFilesParameter
    .CREATE_OR_REPLACE_TARGET) {
      targetParameterFile.empty();
    }
    targetParameterFile.open();    

    sourceLine = sourceParameterFile1.getFirstLine();
    while (sourceLine != null) {    
      if (counterProgressLines == 1 || (counterProgressLines % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Writing Target Line " + counterProgressLines);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }     
      targetParameterFile.setNextLine(sourceLine);
      counterProgress += sourceLine.length() + lengthOfLineFeedChars;
      counterProgressLines++;
      sourceLine = sourceParameterFile1.getNextLine();    
    }  // read all lines of file 1
    
    sourceLine = sourceParameterFile2.getFirstLine();
    while (sourceLine != null) {    
      if (counterProgressLines == 1 || (counterProgressLines % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Writing Target Line " + counterProgressLines);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }     
      targetParameterFile.setNextLine(sourceLine);
      counterProgress += sourceLine.length() + lengthOfLineFeedChars;
      counterProgressLines++;
      sourceLine = sourceParameterFile2.getNextLine();    
    }  // read all lines of file 2
    
    if (sourceParameterFile3 != null) {
      sourceLine = sourceParameterFile3.getFirstLine();
      while (sourceLine != null) {
        if (counterProgressLines == 1 || (counterProgressLines % 50) == 0) {
          Progress.update( (int)(counterProgress * 100 / maxProgress),
          "Writing Target Line " + counterProgressLines);
          DiasdemServer.setTaskProgress(Progress, TaskThread);
        }
        targetParameterFile.setNextLine(sourceLine);
        counterProgress += sourceLine.length() + lengthOfLineFeedChars;
        counterProgressLines++;
        sourceLine = sourceParameterFile3.getNextLine();
      }  // read all lines of file 3
    }
    
    if (sourceParameterFile4 != null) {
      sourceLine = sourceParameterFile4.getFirstLine();
      while (sourceLine != null) {
        if (counterProgressLines == 1 || (counterProgressLines % 50) == 0) {
          Progress.update( (int)(counterProgress * 100 / maxProgress),
          "Writing Target Line " + counterProgressLines);
          DiasdemServer.setTaskProgress(Progress, TaskThread);
        }
        targetParameterFile.setNextLine(sourceLine);
        counterProgress += sourceLine.length() + lengthOfLineFeedChars;
        counterProgressLines++;
        sourceLine = sourceParameterFile4.getNextLine();
      }  // read all lines of file 4
    }
    
    sourceParameterFile1.close();
    sourceParameterFile2.close();
    if (sourceParameterFile3 != null) {
      sourceParameterFile3.close();
    }
    if (sourceParameterFile4 != null) {
      sourceParameterFile4.close();
    }
    targetParameterFile.close();

    Result.update(TaskResult.FINAL_RESULT, counterProgressLines + 
    " lines have been copied to the target parameter text file\n" +
    Tools.shortenFileName(CastParameter.getTargetParameterFileName(), 60) + "!");
    this.setTaskResult(100, "Source Text Files Processed ...", Result,
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