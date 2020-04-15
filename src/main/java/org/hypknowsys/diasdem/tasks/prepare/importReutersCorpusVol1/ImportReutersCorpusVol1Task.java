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

package org.hypknowsys.diasdem.tasks.prepare.importReutersCorpusVol1;

import java.io.*;
import java.util.*;
import java.util.zip.*;
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
 * @version 2.1.0.5, 1 January 2004
 * @author Karsten Winkler
 */

public class ImportReutersCorpusVol1Task extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportReutersCorpusVol1Parameter CastParameter = null;
  private ImportReutersCorpusVol1Result CastResult = null;
  
  private ArrayList AllFileNamesInDirectory = null;
  private ReutersCorpusVol1NewsItem NewsItem = null;
  private HashMap MetaData = null;
  private StringBuffer Text = null;
  private int NumberOfImportedFiles = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient ZipFile TmpZipFile = null;
  private transient ZipEntry TmpZipEntry = null;
  private transient Enumeration TmpEntryEnumeration = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Import Reuters Corpus Vol. 1";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importReutersCorpusVol1"
  + ".ImportReutersCorpusVol1Parameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importReutersCorpusVol1"
  + ".ImportReutersCorpusVol1Result";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importReutersCorpusVol1"
  + ".ImportReutersCorpusVol1ControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DEFAULT_REUTERS_CORPUS_VOL1_FILE_DIRECTORY", 
    "Default Directory of Source Reuters Corpus Vol. 1 Files", 
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_REUTERS_CORPUS_VOL1_COLLECTION_TOPIC", 
    "Default Collection Topic of Source Reuters Corpus Vol. 1 Files", 
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_REUTERS_CORPUS_VOL1_COLLECTION_REGION", 
    "Default Collection Region of Source Reuters Corpus Vol. 1 Files", 
    "", KProperty.STRING, KProperty.EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportReutersCorpusVol1Task() {
    
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
    
    ImportReutersCorpusVol1Parameter parameter = null;
    if (pParameter instanceof ImportReutersCorpusVol1Parameter) {
      parameter = (ImportReutersCorpusVol1Parameter)pParameter;
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
    file = new File(parameter.getSourceDirectory());
    if (!file.exists() || !file.isDirectory()) {
      result.addError(
      "Error: Please enter the name of\n" +
      "an existing local directory in the\n" +
      "field 'Text File Directory'!");
    }
    if (parameter.getFileNameFilter().trim().length() <= 0) {
      result.addError(
      "Error: Please enter a valid file\n" +
      "name extension such as '.txt' in\n" +
      "the field 'File Name Extension'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ImportReutersCorpusVol1Parameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ImportReutersCorpusVol1Result();
    
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
    
    if (Parameter != null && Parameter instanceof ImportReutersCorpusVol1Parameter) {
      CastParameter = (ImportReutersCorpusVol1Parameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    this.acceptTask(TaskProgress.INDETERMINATE, "Processing Reuters Corpus Vol. 1");
    this.validateParameter(Parameter, "Error: Reuters Corpus Vol. 1 cannot be imported!");
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    
    File sourceDirectory = null;
    try {
      sourceDirectory = new File(CastParameter.getSourceDirectory());
    }
    catch(Exception e) { 
      e.printStackTrace(); 
      this.stop();
    }
    AllFileNamesInDirectory = new ArrayList();
    this.enumerateAllFileNames(sourceDirectory);

    String sourceFileName = null;
    String targetFileName = null;
    StringBuffer text = null;
    String currentLine = null;
    
    int counterProgress = 0;
    int maxProgress = AllFileNamesInDirectory.size() + 1;
    int refreshProgress = 0;
    if (maxProgress < 500) {
      refreshProgress = 1;
    }
    else {
      refreshProgress = 50;
    }
    
    for (int i = 0; i < AllFileNamesInDirectory.size(); i++) {
      
      counterProgress++;
      if (counterProgress == 1 || (counterProgress % refreshProgress) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Reuters Corpus File " + counterProgress + "/"
        + maxProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      sourceFileName = (String)AllFileNamesInDirectory.get(i);
      if (sourceFileName.endsWith(CastParameter.getFileNameFilter())) {
        
        // file matches extension criterion and is not in a zip archive
        try {
          NewsItem = new ReutersCorpusVol1NewsItem(sourceFileName);
          this.importNewsItem();
        }
        catch(Exception e) {}
        
      } else if (sourceFileName.toLowerCase().endsWith(".zip")) {
        
        // check contents of zip archives
        try {
          TmpZipFile = new ZipFile(sourceFileName);
          TmpEntryEnumeration = TmpZipFile.entries();
          while (TmpEntryEnumeration.hasMoreElements()) {
            TmpZipEntry = (ZipEntry)TmpEntryEnumeration.nextElement();
            if ( ( TmpZipEntry.getName() )
            .endsWith(CastParameter.getFileNameFilter())) {
              NewsItem = new ReutersCorpusVol1NewsItem(
              TmpZipFile.getInputStream(TmpZipEntry));
              this.importNewsItem();
            }
          }
          TmpZipFile.close();
        }
        catch(Exception e) {}
        
      }  // if: current file meets criterion
      
    }  // for: process all files in directory
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT,
    NumberOfImportedFiles + " news have been found in matching files in\n"
    + Tools.shortenFileName(CastParameter.getSourceDirectory(), 50) + ".\n" 
    + "They have been imported into the DIAsDEM collection\n" + Tools
    .shortenFileName(DiasdemCollection.getCollectionFileName(), 50) + ".\n"
    + "Currently, this collection comprises " + DiasdemCollection
    .getNumberOfDocuments() + " documents.");
    
    this.setTaskResult(100, NumberOfImportedFiles + " Text Files Imported ...", 
    Result, TaskResult.FINAL_RESULT, Task.TASK_FINISHED);
    
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
  
  private void enumerateAllFileNames(File pInitialDirectory) {
  
    File[] allSourceFiles = pInitialDirectory.listFiles();
    for (int i = 0; i < allSourceFiles.length; i++) {
      if (allSourceFiles[i].isDirectory() 
      && CastParameter.includeSubdirectories()) {
        this.enumerateAllFileNames(allSourceFiles[i]);
      }
      else {
        AllFileNamesInDirectory.add(allSourceFiles[i].getAbsolutePath());
      }     
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void importNewsItem() {
    
    DiasdemDocument = DiasdemCollection
    .instantiateDefaultDiasdemDocument();
    MetaData = new HashMap();
    MetaData.put("ItemID", NewsItem.getItemID());
    MetaData.put("Date", NewsItem.getDate());
    MetaData.put("TopicCodes", NewsItem.getTopicCodes());
    MetaData.put("RegionCodes", NewsItem.getRegionCodes());
    Text = new StringBuffer(NewsItem.getTitle().trim());
    if (!NewsItem.getTitle().trim().endsWith(".")) {
      Text.append(" .");
    }
    Text.append(NewsItem.getText());
    
    // save document
    if (DiasdemDocument != null && Text != null) {
      DiasdemDocument.setOriginalText(Text.toString().trim());
      DiasdemDocument.setMetaData(MetaData);
      if ((Tools.stringIsNullOrEmpty(CastParameter.getTopicOfCollection())
      || NewsItem.getTopicCodes().indexOf(CastParameter
      .getTopicOfCollection()) >= 0)
      && (Tools.stringIsNullOrEmpty(CastParameter.getRegionOfCollection())
      || NewsItem.getRegionCodes().indexOf(CastParameter
      .getRegionOfCollection()) >= 0)) {
        DiasdemCollection.addDocument(DiasdemDocument);
        NumberOfImportedFiles++;
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