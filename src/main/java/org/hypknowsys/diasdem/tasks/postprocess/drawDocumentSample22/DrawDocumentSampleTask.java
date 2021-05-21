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

package org.hypknowsys.diasdem.tasks.postprocess.drawDocumentSample22;

import java.io.File;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.core.DIAsDEMconceptualDtd;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMconceptualDtd;
import org.hypknowsys.diasdem.server.DiasdemScriptableNonBlockingTask;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.KProperties;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.AbstractTaskProgress;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;
import org.hypknowsys.server.TaskParameter;
import org.hypknowsys.server.TaskProgress;
import org.hypknowsys.server.TaskResult;

/**
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public class DrawDocumentSampleTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DrawDocumentSampleParameter CastParameter = null;
  
  private DIAsDEMconceptualDtd DerivedDtd = null;
  
  private File SampleDirectoryFile = null;
  private String SampleDirectoryFileName = null;
  private int SampleDirectoryIndex = 1;
  private int FilesInSampleDirectory = 0;
  private int MaxFilesInSampleDirectory = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String LABEL =
  "Draw Document Sample 2.2";
  private static final String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.drawDocumentSample22"
  + ".DrawDocumentSampleParameter";
  private static final String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.drawDocumentSample22"
  + ".DrawDocumentSampleResult";
  private static final String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.drawDocumentSample22"
  + ".DrawDocumentSampleControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DrawDocumentSampleTask() {
    
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
    
    DrawDocumentSampleParameter parameter = null;
    if (pParameter instanceof DrawDocumentSampleParameter) {
      parameter = (DrawDocumentSampleParameter)pParameter;
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
      "Error: Please enter the name of an existing local\n"
      + DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION
      + "-file in the field 'Collection File'!");
    }
    if (parameter.getDtdFileName() != null
    && parameter.getDtdFileName().length() > 9) {
      file = new File(parameter.getDtdFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n"
        + DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_EXTENSION
        + "-file in the field 'Conceptual DTD File'! This\n"
        + "field might remain empty, if you want to draw a\n"
        + "sample of DIAsDEM documents without semantic tags.");
      }
    }
    
    if (parameter.getSamplingMode() == DrawDocumentSampleParameter
    .CREATE_RANDOM_SAMPLE_FILE) {
      if (parameter.getRandomSampleFileName().trim().length() <= 0
      || !parameter.getRandomSampleFileName().trim().endsWith(
      DIAsDEMguiPreferences.DOCUMENT_SAMPLE_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter a valid local "
        + DIAsDEMguiPreferences.DOCUMENT_SAMPLE_FILE_EXTENSION
        + "-file name\nin the field 'Random Sample File'!");
      }
      file = new File(parameter.getRandomSampleFileName());
      if (file.exists()) {
        result.addWarning(
        "Warning: The file specified in the field\n"
        + "'Random Sample File' currently exists.\n"
        + "Do you really want to replace this file?");
      }
      if (parameter.getRandomSampleSize() < 0.0
      || parameter.getRandomSampleSize() > 1.0) {
        result.addError(
        "Error: 'Random Sample Size' must\n"
        + "be a valid double in [0.0; 1.0]!");
      }
    }
    else {
      file = new File(parameter.getRandomSampleFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.DOCUMENT_SAMPLE_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n"
        + DIAsDEMguiPreferences.DOCUMENT_SAMPLE_FILE_EXTENSION
        + "-file in the field 'Random Sample File'!");
      }
    }
    
    file = new File(parameter.getHtmlDirectory());
    if (file.exists() && file.isDirectory() && file.list() != null
    && file.list().length > 0) {
      result.addWarning(
      "Warning: The directory specified in the field\n"
      + "'Sample Directory' is not empty. Do you\n"
      + "really want to select this directory?");
    }
    file = new File(parameter.getHtmlDirectory());
    if (!file.exists()) {
      try {
        boolean success = file.mkdirs();
      }
      catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    if (!file.exists() || !file.isDirectory()) {
      result.addError(
      "Please enter the name of an existing local\n"
      + "directory in the field 'Sample Directory'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new DrawDocumentSampleParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new DrawDocumentSampleResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    DIAsDEMguiMenuBar.ACTIONS_POSTPROCESS_PATTERNS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof DrawDocumentSampleParameter) {
      CastParameter = (DrawDocumentSampleParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    String shortErrorMessage = "Error: Document sample cannot be drawn!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    try {
      DerivedDtd = new DefaultDIAsDEMconceptualDtd(
      CastParameter.getDtdFileName(), KProperties.LOAD);
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
    }
    catch (Exception e) {
      DerivedDtd = null;
    }
    
    if (CastParameter.getSamplingMode() == DrawDocumentSampleParameter
    .CREATE_RANDOM_SAMPLE_FILE) {
      this.createRandomFile();
    }
    this.applyRandomFile();
    super.closeDiasdemCollection();
    
    Result.update(TaskResult.FINAL_RESULT,
    "The random sample of DIAsDEM documents has been created in\n"
    + Tools.shortenFileName(CastParameter.getHtmlDirectory(), 55) +"!");
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
  
  private String getSampleDirectory() {
    
    if (SampleDirectoryFile == null) {
      MaxFilesInSampleDirectory = DiasdemProject.getIntProperty(
      "MAX_FILES_PER_DIRECTORY");
      SampleDirectoryIndex = 0;
      FilesInSampleDirectory = MaxFilesInSampleDirectory;
    }
    if (FilesInSampleDirectory == MaxFilesInSampleDirectory) {
      SampleDirectoryIndex++;
      FilesInSampleDirectory = 0;
      SampleDirectoryFileName = Tools.ensureTrailingSlash(CastParameter
      .getHtmlDirectory()) + "part" + SampleDirectoryIndex
      + File.separator;
      SampleDirectoryFile = new File(SampleDirectoryFileName);
      if (!SampleDirectoryFile.exists()) {
        SampleDirectoryFile.mkdirs();
      }
      if (DerivedDtd != null) {
        DerivedDtd.writeXmlRepresentation(SampleDirectoryFileName);
      }
    }
    FilesInSampleDirectory++;
    
    return SampleDirectoryFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createRandomFile() {
    
    Progress = new AbstractTaskProgress(TaskProgress.INDETERMINATE,
    "Creating Random Sample of Documents");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    TextFile sampleTextUnitsFile = new TextFile(new File(CastParameter
    .getRandomSampleFileName()));
    sampleTextUnitsFile.empty();
    sampleTextUnitsFile.open();
    
    TextFile randomFile = new TextFile(new File(Tools.ensureTrailingSlash(
    CastParameter.getHtmlDirectory()) + "randomSample.tmp"));
    randomFile.empty();
    randomFile.open();
    
    double sampleSize = CastParameter.getRandomSampleSize() * DiasdemCollection
    .getNumberOfDocuments();
    Random randomizer = new Random();
    TreeSet sortedRandoms = new TreeSet();
    for (int i = 0; sortedRandoms.size() < (int)sampleSize; i++) {
      sortedRandoms.add(new Integer(randomizer.nextInt((int)DiasdemCollection
      .getNumberOfDocuments() + 1) + 1));
    }
    Iterator iterator = sortedRandoms.iterator();
    while (iterator.hasNext()) {
      randomFile.setNextLine(((Integer)iterator.next()).toString());
    }
    
    sampleTextUnitsFile.setNextLine("# <table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Parameter</th>"
    + "<th align=\"left\" valign=\"top\">Value</th></tr>");
    sampleTextUnitsFile.setNextLine("# <tr>"
    + "<td align=\"left\" valign=\"top\">DIAsDEM Collection</td>"
    + "<td align=\"left\" valign=\"top\">" + Tools.shortenFileName(
    DiasdemCollection.getCollectionFileName(), 60)
    + "</td></tr>");
    sampleTextUnitsFile.setNextLine("# <tr>"
    + "<td align=\"left\" valign=\"top\">Number of Documents</td>"
    + "<td align=\"left\" valign=\"top\">" + DiasdemCollection
    .getNumberOfDocuments()
    + "</td></tr>");
    sampleTextUnitsFile.setNextLine("# <tr>"
    + "<td align=\"left\" valign=\"top\"> Random Sample Size</td>"
    + "<td align=\"left\" valign=\"top\">" + CastParameter.getRandomSampleSize()
    + " * " + DiasdemCollection.getNumberOfDocuments()
    + " = " + (int)sampleSize
    + "</td></tr></table>");
    
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    int currentDocCounter = 1;
    String nextSampleLineString = randomFile
    .getFirstLineButIgnoreCommentsAndEmptyLines();
    int nextSampleLine = 0;
    if (nextSampleLineString != null) {
      nextSampleLine = Integer.parseInt(nextSampleLineString);
    }
    while (DiasdemDocument != null) {
      if (currentDocCounter == 1 || (currentDocCounter % 50) == 0) {
        Progress.update((int)(currentDocCounter * 100 / maxProgress),
        "Processing Document " + currentDocCounter);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      if (currentDocCounter == nextSampleLine) {
        sampleTextUnitsFile.setNextLine(DiasdemDocument.getDiasdemDocumentID());
        nextSampleLineString = randomFile
        .getNextLineButIgnoreCommentsAndEmptyLines();
        if (nextSampleLineString != null) {
          nextSampleLine = Integer.parseInt(nextSampleLineString);
        }
      }
      DiasdemDocument = DiasdemCollection.getNextDocument();
      currentDocCounter++;
    }
    
    randomFile.close();
    sampleTextUnitsFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void applyRandomFile() {
    
    Progress = new AbstractTaskProgress(TaskProgress.INDETERMINATE,
    "Exporting Random Sample of Documents");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    TextFile sampleTextUnitsFile = new TextFile(new File(
    CastParameter.getRandomSampleFileName()));
    sampleTextUnitsFile.open();
    
    TextFile sampleDocsHtmlFile = new TextFile(new File(Tools
    .ensureTrailingSlash(CastParameter.getHtmlDirectory()) + Tools
    .removeFileExtension(Tools.removeDirectory(CastParameter
    .getRandomSampleFileName())) + ".html"));
    sampleDocsHtmlFile.empty();
    sampleDocsHtmlFile.open();
    
    int currentDocCounter = 0;
    String diasdemDocFile = null;
    String taggedDocFile = null;
    Template header = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_HeaderTemplate.html"));
    header.addValue("${Title}", "Random Sample of DIAsDEM Documents");
    sampleDocsHtmlFile.setFirstLine(header.insertValues());
    sampleDocsHtmlFile.setNextLine("<p>Created by " + this.getClass().getName()
    + " on " + Tools.getSystemDate() + "</p>");
    
    sampleDocsHtmlFile.setNextLine("<h3>Parameter Settings</h3>");
    String nextSampleLineString = sampleTextUnitsFile.getFirstLine();
    while (nextSampleLineString.startsWith("# ")) {
      sampleDocsHtmlFile.setNextLine(nextSampleLineString.substring(1).trim());
      nextSampleLineString = sampleTextUnitsFile.getNextLine();
    }
    
    sampleDocsHtmlFile.setNextLine("<h3>Results</h3>");
    while (nextSampleLineString != null) {
      currentDocCounter++;
      DiasdemDocument = DiasdemCollection.getDocument(nextSampleLineString
      .trim());
      if (DiasdemDocument != null) {
        diasdemDocFile = this.getSampleDirectory() + currentDocCounter
        + "diasdemDoc.xml";
        DiasdemDocument.dumpAsXmlFile(diasdemDocFile);
        if (DerivedDtd != null) {
          taggedDocFile = this.getSampleDirectory() + currentDocCounter
          + "taggedDoc.xml";
          DerivedDtd.dumpDocumentAsXmlFile(DiasdemProject, DiasdemDocument,
          taggedDocFile, false);
        }
        sampleDocsHtmlFile.setNextLine("<p><a href=\""
        + Tools.getRelativeFileName(CastParameter.getHtmlDirectory(),
        diasdemDocFile) + "\" target=\"_new\" title=\"DIAsDEM Document ID: "
        + DiasdemDocument.getDiasdemDocumentID() + "\">"
        + "Intermediate DIAsDEM Document " + currentDocCounter
        + "</a>" + (DerivedDtd != null ? "&nbsp; - &nbsp;<a href=\""
        + Tools.getRelativeFileName(CastParameter.getHtmlDirectory(),
        taggedDocFile) + "\" target=\"_new\" title=\"DIAsDEM Document ID: "
        + DiasdemDocument.getDiasdemDocumentID() + "\">"
        + "Semantically Tagged XML Document " + currentDocCounter
        + "</a> " : "") + "</p>");
      }
      nextSampleLineString = sampleTextUnitsFile
      .getNextLineButIgnoreCommentsAndEmptyLines();
    }
    Template footer = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_FooterTemplate.html"));
    sampleDocsHtmlFile.setNextLine(footer.insertValues());
    
    sampleTextUnitsFile.close();
    sampleDocsHtmlFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}