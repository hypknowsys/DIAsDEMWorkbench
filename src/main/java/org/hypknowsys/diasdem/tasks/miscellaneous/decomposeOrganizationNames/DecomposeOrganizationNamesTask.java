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

package org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames;

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
import org.hypknowsys.diasdem.tasks.prepare.tokenizeTextUnits.*;
import org.hypknowsys.diasdem.tasks.prepare.createTextUnits.*;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class DecomposeOrganizationNamesTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DecomposeOrganizationNamesParameter CastParameter = null;

  private StringTrie SuffixesTrie = null;
  private StringTrie BlacklistTrie = null;
  
  private TextFile TokenizedOrganizationsFile = null;
  private TextFile ShortenedOrganizationsFile = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient StringTokenizer TmpTokenizer = null;
  private transient String TmpToken = null;
  private transient String TmpTokenToLowerCase = null;
  private transient int TmpBlankIndex = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Decompose Organization Names";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames"
  + ".TDecomposeOrganizationNamesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames"
  + ".DecomposeOrganizationNamesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames"
  + ".DecomposeOrganizationNamesControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DECOMPOSE_ORG_NAMES:_MRU_TOKENIZED_ORG_FILE_NAME", 
    "Task: Decompose Organization Names; MRU Tokenized Organizations File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DECOMPOSE_ORG_NAMES:_MRU_BLACKLIST_FILE_NAME",
    "Task: Decompose Organization Names; MRU Blacklist of Organizations File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DECOMPOSE_ORG_NAMES:_MRU_SHORTENED_ORG_FILE_NAME",
    "Task: Decompose Organization Names; MRU Shortened Organizations File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DECOMPOSE_ORG_NAMES:_MRU_EXTRACT_TOKENS",
    "Task: Decompose Organization Names; Default Extract Tokens of Company Names",
    "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DecomposeOrganizationNamesTask() {
    
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
    
    DecomposeOrganizationNamesParameter parameter = null;
    if (pParameter instanceof DecomposeOrganizationNamesParameter) {
      parameter = (DecomposeOrganizationNamesParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    if (parameter.getTokenizedOrganizationsFileName().trim().equals(
    parameter.getShortenedOrganizationsFileName().trim())) {
      result.addError(
      "Error: Input 'Tokenized Organizations File' must be\n" +
      "different from output 'Shortened Organizations File'!");
    }
    File file = new File(parameter.getTokenizedOrganizationsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Tokenized Organizations File'!");
    }
    if (parameter.getShortenedOrganizationsFileName().trim().length() <= 0
    || !parameter.getShortenedOrganizationsFileName().trim().endsWith(
    DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file name\nin the field 'Shortened Organizations File'!");
    }
    file = new File(parameter.getShortenedOrganizationsFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Shortened Organizations File' exists.\n" +
      "Do you really want to replace this file?");
    }
    file = new File(parameter.getBlacklistOfOrganizationsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Blacklist of Organizations File'!");
    }
    file = new File(parameter.getOrganizationSuffixesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Organization Suffixes File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new DecomposeOrganizationNamesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new DecomposeOrganizationNamesResult();
    
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
    
    if (Parameter != null && Parameter instanceof DecomposeOrganizationNamesParameter) {
      CastParameter = (DecomposeOrganizationNamesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Parameter text file cannot be tokenized!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Loading Parameter Files");
    this.validateParameter(Parameter, shortErrorMessage);
    
    this.loadParameterFiles();
    
    TokenizedOrganizationsFile = new TextFile(new File(CastParameter
    .getTokenizedOrganizationsFileName()));
    TokenizedOrganizationsFile.openReadOnly();
    
    ShortenedOrganizationsFile = new TextFile(new File(CastParameter
    .getShortenedOrganizationsFileName()));
    ShortenedOrganizationsFile.empty();
    ShortenedOrganizationsFile.open();    
    
    String sourceLine = null;
    String targetLine = null;
    int counterSuccess = 0;
    int counterProgress = 0;
    int counterProgressLines = 0;
    long maxProgress = ( new File(CastParameter
    .getTokenizedOrganizationsFileName()) ).length();
    int lengthOfLineFeedChars = Tools.getLengthOfLineFeedChars();
    
    sourceLine = TokenizedOrganizationsFile.getFirstLine();
    while (sourceLine != null) {
      
      if (counterProgressLines == 1 || (counterProgressLines % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Source Line " + counterProgressLines);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      if (!sourceLine.startsWith(TokenizedOrganizationsFile.getCommentLinePrefix())) {
        counterSuccess++;
        this.processTokenizedOrganization(sourceLine);
      }
      else {
        ShortenedOrganizationsFile.setNextLine(sourceLine);
      }

      counterProgress += sourceLine.length() + lengthOfLineFeedChars;
      counterProgressLines++;
      sourceLine = TokenizedOrganizationsFile.getNextLine();
      
    }  // read all lines
    
    TokenizedOrganizationsFile.close();
    ShortenedOrganizationsFile.close();

    Result.update(TaskResult.FINAL_RESULT,
    counterSuccess + " of " + counterProgressLines + 
    " lines in the source parameter text file have been\n" + 
    "processed. Shortened organization names have been saved as \n" +
    Tools.shortenFileName(CastParameter.getShortenedOrganizationsFileName(), 60)
    + "!");
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
  
  private void loadParameterFiles() {
    
    // list of case-insensitive organization suffixes 
    // (text file, each line = one suffix)
    SuffixesTrie = new StringTrie();
    TextBufferedReader textReader = new TextBufferedReader(
    new File(CastParameter.getOrganizationSuffixesFileName()) );
    String line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      SuffixesTrie.put(line.toLowerCase().trim(), line.toLowerCase().trim());
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    
    // list of case-insensitive potential organization names that in fact are
    // no organizations (text file, each line = one blacklist entry)
    BlacklistTrie = new StringTrie();
    textReader = new TextBufferedReader(
    new File(CastParameter.getBlacklistOfOrganizationsFileName()) );
    line = textReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      BlacklistTrie.put(line.toLowerCase().trim(), line.toLowerCase().trim());
      line = textReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    textReader.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void processTokenizedOrganization(String pOrganization) {
    
    // identify start and end of organization suffix
    int SuffixStartIndex = -1;
    int SuffixEndIndex = -1;
    TmpToken = null;
    TmpTokenizer = new StringTokenizer(pOrganization);
    int NumberOfTokens = TmpTokenizer.countTokens();
    String[] TokensOriginal = new String[NumberOfTokens];
    String[] TokensLowerCase = new String[NumberOfTokens];
    Object LookUpResult = null;
    
    int counter = 0;
    while (TmpTokenizer.hasMoreTokens()) {
      TmpToken = TmpTokenizer.nextToken();
      TokensOriginal[counter] = TmpToken;
      TokensLowerCase[counter] = TmpToken.toLowerCase();
      counter++;
    }
    
    for (int i = 0; i < NumberOfTokens; i++) {
      LookUpResult = SuffixesTrie.getAndCheckForSubsequentBlankSpace(
      TokensLowerCase[i]);
      if (LookUpResult != null) {
        SuffixStartIndex = i;
        SuffixEndIndex = i;
        if (SuffixesTrie.previousMatchPrecedesBlankSpace()) {
          // go on: token is a valid company abbreviation, but it might
          // be the prefix of a multi-token company abbreviation         
          TmpStringBuffer = new StringBuffer(1000);
          TmpStringBuffer.append(TokensLowerCase[i]);
          for (int j = i + 1; j < NumberOfTokens && SuffixesTrie
          .previousMatchPrecedesBlankSpace(); j++) {
            TmpStringBuffer.append(" ");
            TmpStringBuffer.append(TokensLowerCase[j]);
            if (SuffixesTrie.getAndCheckForSubsequentBlankSpace(
            TmpStringBuffer.toString()) != null) {
              SuffixEndIndex = j;
            }
          }
        }
        i = NumberOfTokens;  // break i
      }
      else if (LookUpResult == null && SuffixesTrie
      .previousMatchPrecedesBlankSpace()) {
        // go on: token is not a valid company abbreviation, but it might
        // be the prefix of a multi-token company abbreviation
        SuffixStartIndex = i;
        SuffixEndIndex = -1;
        TmpStringBuffer = new StringBuffer(1000);
        TmpStringBuffer.append(TokensLowerCase[i]);
        for (int j = i + 1; j < NumberOfTokens && SuffixesTrie
        .previousMatchPrecedesBlankSpace(); j++) {
          TmpStringBuffer.append(" ");
          TmpStringBuffer.append(TokensLowerCase[j]);
          if (SuffixesTrie.getAndCheckForSubsequentBlankSpace(
          TmpStringBuffer.toString()) != null) {
            SuffixEndIndex = j;
          }
        }
        if (SuffixEndIndex < 0) {
          SuffixStartIndex = -1;
          SuffixEndIndex = -1;
        }
        else {
          i = NumberOfTokens;  // break i
        }
      }
    }  // for:all tokens
   
    if (SuffixEndIndex >= 0 && SuffixEndIndex >= 0) {
      // organization suffix found; create string of shortened organization name
      TmpStringBuffer = new StringBuffer(10000);
      for (int i = 0; i < SuffixStartIndex; i++) {
        if (i > 0) { TmpStringBuffer.append(" "); }
        TmpStringBuffer.append(TokensOriginal[i]);
      }  
      // check whether shortened organization name is contained in blacklist
      TmpToken = TmpStringBuffer.toString();
      TmpTokenToLowerCase = TmpToken.toLowerCase();
      if (BlacklistTrie.get(TmpTokenToLowerCase) == null
      && Tools.stringContainsLetter(TmpToken) 
      && !Tools.stringIsNullOrEmpty(TmpToken)
      && Character.isUpperCase(TmpToken.charAt(0))
      && !this.isAbbreviationOfForenamesToken(TmpToken)
      && !TmpTokenToLowerCase.endsWith(" and")
      && !TmpTokenToLowerCase.endsWith(" &")
      && !TmpTokenToLowerCase.endsWith(" '")
      && !TmpTokenToLowerCase.endsWith(" of")
      && !TmpTokenToLowerCase.endsWith(" -")
      && !TmpTokenToLowerCase.endsWith(" ,")) {
        ShortenedOrganizationsFile.setNextLine(TmpStringBuffer.toString());
      }
      else {
        ShortenedOrganizationsFile.setNextLine("# " + TmpStringBuffer.toString());
      }
      
      // check first token of company name separately: ABC
      if (CastParameter.extractTokensOfDecomposedCompanyNames() 
      && TmpStringBuffer.indexOf(" ") > 0) {
        TmpBlankIndex = TmpStringBuffer.indexOf(" ", 0);
        TmpToken = TmpStringBuffer.substring(0, TmpBlankIndex);
        TmpTokenToLowerCase = TmpToken.toLowerCase();
        if (BlacklistTrie.get(TmpTokenToLowerCase) == null
        && Tools.stringContainsLetter(TmpToken) 
        && !Tools.stringIsNullOrEmpty(TmpToken)
        && Character.isUpperCase(TmpToken.charAt(0))
        && !this.isAbbreviationOfForenamesToken(TmpToken)
        && !TmpTokenToLowerCase.endsWith(" and")
        && !TmpTokenToLowerCase.endsWith(" &")
        && !TmpTokenToLowerCase.endsWith(" '")
        && !TmpTokenToLowerCase.endsWith(" of")
        && !TmpTokenToLowerCase.endsWith(" -")
        && !TmpTokenToLowerCase.endsWith(" ,")) {
          ShortenedOrganizationsFile.setNextLine(TmpToken);
        }
        else {
          ShortenedOrganizationsFile.setNextLine("# " + TmpToken);
        }
        
        // check tokens 1 and 2 of company name separately: ABC Industries
        if (((TmpBlankIndex + 1) < TmpStringBuffer.toString().length()) &&
        TmpStringBuffer.indexOf(" ", TmpBlankIndex + 1) >= 0) {
          TmpBlankIndex = TmpStringBuffer.indexOf(" ", TmpBlankIndex + 1);
          TmpToken = TmpStringBuffer.substring(0, TmpBlankIndex);
          TmpTokenToLowerCase = TmpToken.toLowerCase();
          if (BlacklistTrie.get(TmpTokenToLowerCase) == null
          && Tools.stringContainsLetter(TmpToken)
          && !Tools.stringIsNullOrEmpty(TmpToken)
          && Character.isUpperCase(TmpToken.charAt(0))
          && !this.isAbbreviationOfForenamesToken(TmpToken)
          && !TmpTokenToLowerCase.endsWith(" and")
          && !TmpTokenToLowerCase.endsWith(" &")
          && !TmpTokenToLowerCase.endsWith(" '")
          && !TmpTokenToLowerCase.endsWith(" of")
          && !TmpTokenToLowerCase.endsWith(" -")
          && !TmpTokenToLowerCase.endsWith(" ,")) {
            ShortenedOrganizationsFile.setNextLine(TmpToken);
          }
          else {
            ShortenedOrganizationsFile.setNextLine("# " + TmpToken);
          }
        }
        
        // check tokens 1, 2, and 3 of company name separately: ABC Ind. YXZ
        if (((TmpBlankIndex + 1) < TmpStringBuffer.toString().length()) &&
        TmpStringBuffer.indexOf(" ", TmpBlankIndex + 1) >= 0) {
          TmpBlankIndex = TmpStringBuffer.indexOf(" ", TmpBlankIndex + 1);
          TmpToken = TmpStringBuffer.substring(0, TmpBlankIndex);
          TmpTokenToLowerCase = TmpToken.toLowerCase();
          if (BlacklistTrie.get(TmpTokenToLowerCase) == null
          && Tools.stringContainsLetter(TmpToken)
          && !Tools.stringIsNullOrEmpty(TmpToken)
          && Character.isUpperCase(TmpToken.charAt(0))
          && !this.isAbbreviationOfForenamesToken(TmpToken)
          && !TmpTokenToLowerCase.endsWith(" and")
          && !TmpTokenToLowerCase.endsWith(" &")
          && !TmpTokenToLowerCase.endsWith(" '")
          && !TmpTokenToLowerCase.endsWith(" of")
          && !TmpTokenToLowerCase.endsWith(" -")
          && !TmpTokenToLowerCase.endsWith(" ,")) {
            ShortenedOrganizationsFile.setNextLine(TmpToken);
          }
          else {
            ShortenedOrganizationsFile.setNextLine("# " + TmpToken);
          }
        }
      }
    }
    else {   
      ShortenedOrganizationsFile.setNextLine(pOrganization);
    }

  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isAbbreviationOfForenamesToken(String pString) {
    
    if (pString == null || pString.length() == 0) {
      return false;
    }
    else if (pString.length() == 2 && Character.isUpperCase(pString.charAt(0))
    && pString.charAt(1) == '.') {
      return true;
    }
    else if (pString.length() == 4 && Character.isUpperCase(pString.charAt(0))
    && pString.charAt(1) == '.' && Character.isUpperCase(pString.charAt(2))
    && pString.charAt(3) == '.') {
      return true;
    }
    
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}