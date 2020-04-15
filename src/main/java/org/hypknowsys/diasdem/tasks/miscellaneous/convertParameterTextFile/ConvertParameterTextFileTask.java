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

package org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile;

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

public class ConvertParameterTextFileTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ConvertParameterTextFileParameter CastParameter = null;
  private ConvertParameterTextFileResult CastResult = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient Pattern RegexPattern = null;
  private transient Matcher RegexMatcher = null;
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Convert Parameter Text File";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile"
  + ".ConvertParameterTextFileParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile"
  + ".ConvertParameterTextFileResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile"
  + ".ConvertParameterTextFileControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("CONVERT_PARAMETER_FILE:_MRU_SOURCE_FILE_NAME", 
    "Task: Convert Parameter Text File; MRU Source Text File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("CONVERT_PARAMETER_FILE:_MRU_TARGET_FILE_NAME",
    "Task: Convert Parameter Text File; MRU Target Text File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertParameterTextFileTask() {
    
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
    
    ConvertParameterTextFileParameter parameter = null;
    if (pParameter instanceof ConvertParameterTextFileParameter) {
      parameter = (ConvertParameterTextFileParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    if (parameter.getSourceParameterFileName().trim().equals(
    parameter.getTargetParameterFileName().trim())) {
      result.addError(
      "Error: 'Source Parameter File' must be\n" +
      "different from 'Target Parameter File'!");
    }
    File file = new File(parameter.getSourceParameterFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Source Parameter File'!");
    }
    if (parameter.getTargetParameterFileName().trim().length() <= 0
    || !parameter.getTargetParameterFileName().trim().endsWith(
    DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file name\nin the field 'Target Parameter File'!");
    }
    file = new File(parameter.getTargetParameterFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Target Parameter File' currently exists.\n" +
      "Do you really want to replace this file?");
    }
    
    if (parameter.getRegularExpression() != null && parameter
    .getRegularExpression().length() > 0 && !Tools.isSyntacticallyCorrectRegex(
    parameter.getRegularExpression())) {
      result.addError(
      "Error: Please enter a syntactically correct regular\n" +
      "expression in the field 'Regular Expression'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ConvertParameterTextFileParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ConvertParameterTextFileResult();
    
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
    
    if (Parameter != null && Parameter instanceof ConvertParameterTextFileParameter) {
      CastParameter = (ConvertParameterTextFileParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Parameter text file cannot be converted!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    
    if (CastParameter.getConversionType() == ConvertParameterTextFileParameter
    .APPLY_REGULAR_EXPRESSION_TO_PARAMETER_FILE_LINES) {
      RegexPattern = Pattern.compile(CastParameter.getRegularExpression());
    }
        
    TextFile sourceParameterFile = new TextFile(new File(CastParameter
    .getSourceParameterFileName()));
    sourceParameterFile.openReadOnly();
    
    TextFile targetParameterFile = new TextFile(new File(CastParameter
    .getTargetParameterFileName()));
    targetParameterFile.empty();
    targetParameterFile.open();    
    
    String sourceLine = null;
    String targetLine = null;
    int counterSuccess = 0;
    int counterProgress = 0;
    int counterProgressLines = 0;
    long maxProgress = ( new File(CastParameter
    .getSourceParameterFileName()) ).length();
    int lengthOfLineFeedChars = Tools.getLengthOfLineFeedChars();
    
    sourceLine = sourceParameterFile.getFirstLine();
    while (sourceLine != null) {
      
      if (counterProgressLines == 1 || (counterProgressLines % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Source Line " + counterProgressLines);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      if (!sourceLine.startsWith(sourceParameterFile.getCommentLinePrefix())) {
        counterSuccess++;
        
        switch (CastParameter.getConversionType()) {
          case ConvertParameterTextFileParameter.CONVERT_PARAMETER_FILE_LINES_TO_LOWER_CASE: {
            targetLine = sourceLine.toLowerCase();
            break;
          }
          case ConvertParameterTextFileParameter.CONVERT_PARAMETER_FILE_LINES_TO_UPPER_CASE: {
            targetLine = sourceLine.toUpperCase();
            break;
          }         
          case ConvertParameterTextFileParameter.APPLY_REGULAR_EXPRESSION_TO_PARAMETER_FILE_LINES: {
            RegexMatcher = RegexPattern.matcher(sourceLine);
            TmpStringBuffer = new StringBuffer(sourceLine.length() + 10000);
            while (RegexMatcher.find()) {
              RegexMatcher.appendReplacement(TmpStringBuffer,
              CastParameter.getReplacementString());
            }
            targetLine = RegexMatcher.appendTail(TmpStringBuffer).toString();
            break;
          }         
          default: {
            targetLine = sourceLine;
          }
        }
        targetParameterFile.setNextLine(targetLine);
        
      }
      else {
        targetParameterFile.setNextLine(sourceLine);
      }

      counterProgress += sourceLine.length() + lengthOfLineFeedChars;
      counterProgressLines++;
      sourceLine = sourceParameterFile.getNextLine();
      
    }  // read all lines
    
    sourceParameterFile.close();
    targetParameterFile.close();

    Result.update(TaskResult.FINAL_RESULT,
    counterSuccess + " of " + counterProgressLines + 
    " lines in the source parameter text file have been\n" + 
    "converted. Results have been saved as target parameter text file\n" +
    Tools.shortenFileName(CastParameter.getTargetParameterFileName(), 60) + "!");
    this.setTaskResult(100, "Source Text File Processed ...", Result,
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