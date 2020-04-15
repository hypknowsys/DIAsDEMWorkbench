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

package org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus;

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
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.core.neex.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class EstablishInitialThesaurusTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private EstablishInitialThesaurusParameter CastParameter = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Establish Initial Thesaurus";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus.EstablishInitialThesaurusParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus.EstablishInitialThesaurusResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus.EstablishInitialThesaurusControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public EstablishInitialThesaurusTask() {
    
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
    
    EstablishInitialThesaurusParameter parameter = null;
    if (pParameter instanceof EstablishInitialThesaurusParameter) {
      parameter = (EstablishInitialThesaurusParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    File file = new File(parameter.getCollectionStatisticsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TF_STATISTICS_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TF_STATISTICS_FILE_EXTENSION +
      "-file in the field 'TF Statistics File'!");
    }
    if (parameter.getThesaurusFileName().trim().length() <= 0
    || !parameter.getThesaurusFileName().trim().endsWith(
    DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " + 
      DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION +
      "-file\nname in the field 'Initial Thesaurus File'!");
    }
    file = new File(parameter.getThesaurusFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Initial Thesaurus File' currently exists.\n" +
      "Do you really want to replace this file?");
    }
    if (parameter.getMinTermFrequency() < 0) {
      result.addError(
      "Please enter a non-negative integer\n" +
      "in the field 'Min. Term Frequency'!");
    }
    if (parameter.getMaxTermFrequency() < 0) {
      result.addError(
      "Please enter a non-negative integer\n" +
      "in the field 'Max. Term Frequency'!");
    }
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new EstablishInitialThesaurusParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new EstablishInitialThesaurusResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_UNDERSTAND_DOMAIN,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof EstablishInitialThesaurusParameter) {
      CastParameter = (EstablishInitialThesaurusParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = 
    "Error: Initial thesaurus cannot be established!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    
    DIAsDEMthesaurus wordStatistics = new DefaultDIAsDEMthesaurus();
    wordStatistics.load(CastParameter.getCollectionStatisticsFileName());
    DIAsDEMthesaurus thesaurus = new DefaultDIAsDEMthesaurus(
    "Initial Thesaurus for Collection Statistics"
    + CastParameter.getCollectionStatisticsFileName(), 100000);

    int counterProgress = 1;
    long maxProgress = wordStatistics.getSize();
    
    DIAsDEMthesaurusTerm currentThesaurusTerm = null;
    DIAsDEMthesaurusTerm currentStatisticsTerm = wordStatistics.getFirstTerm();
    while (currentStatisticsTerm != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress), 
          "Processing Term " + counterProgress); 
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      if ( ( currentStatisticsTerm.getOccurrences() >= CastParameter
      .getMinTermFrequency() ) && ( currentStatisticsTerm.getOccurrences() 
      <= CastParameter.getMaxTermFrequency() ) && ( ! NamedEntity.isPlaceholder(
      currentStatisticsTerm.getWord() ) ) ) {
        currentThesaurusTerm = new DefaultDIAsDEMthesaurusTerm(thesaurus
        .getNextID(), 
        currentStatisticsTerm.getWord(), currentStatisticsTerm.getOccurrences(),
        DIAsDEMthesaurusTerm.DESCRIPTOR);
        thesaurus.add(currentThesaurusTerm);
      }
      
      currentStatisticsTerm = wordStatistics.getNextTerm();
      counterProgress++;

    }  // read all collection statistics terms
    
    Progress = new AbstractTaskProgress(TaskProgress.INDETERMINATE,
    "Sorting and saving initial thesaurus ...");
    DiasdemServer.setTaskProgress(Progress, TaskThread);

    thesaurus.setOrderTypeWordsAsc();
    thesaurus.save(CastParameter.getThesaurusFileName());

    Result.update(TaskResult.FINAL_RESULT, 
    "The initial thesaurus has been created and saved in the file\n" +
    Tools.shortenFileName(CastParameter.getThesaurusFileName(), 55)  + "!");
    this.setTaskResult(100, "All Terms Processed ...", Result,
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