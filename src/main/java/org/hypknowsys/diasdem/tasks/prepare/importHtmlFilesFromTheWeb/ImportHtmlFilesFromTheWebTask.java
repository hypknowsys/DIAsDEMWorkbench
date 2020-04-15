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

package org.hypknowsys.diasdem.tasks.prepare.importHtmlFilesFromTheWeb;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import org.hypknowsys.client.gui.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.client.gui.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Heiko Scharff
 */

public class ImportHtmlFilesFromTheWebTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportHtmlFilesFromTheWebParameter CastParameter = null;

  private ArrayList AllFileNamesInDirectory = null;
  private Pattern HtmlCommentsPattern = null;
  private Pattern HtmlScriptPattern = null;
  private Pattern HtmlParagraphTagPattern = null;
  private Pattern HtmlParagraphTag2Pattern = null;
  private Pattern HtmlTagPattern = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient Matcher TmpMatcher = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Import HTML Files from the Web";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importHtmlFilesFromTheWeb"
  + ".ImportHtmlFilesFromTheWebParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importHtmlFilesFromTheWeb"
  + ".ImportHtmlFilesFromTheWebResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.importHtmlFilesFromTheWeb"
  + ".ImportHtmlFilesFromTheWebControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("IMPORT_HTML_FILES_FROM_THE_WEB:_DEFAULT_URL_FILE", 
    "Import HTML Files from the Web: Default Download URL File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportHtmlFilesFromTheWebTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
    
    HtmlCommentsPattern = Pattern.compile("<!\\-\\-.*\\-\\->",
    Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);
    HtmlScriptPattern = Pattern.compile("<script[^>]*>[^<]*</script>",
    Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);
    HtmlTagPattern = Pattern.compile("<[^>]*>",
    Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);
    HtmlParagraphTagPattern = Pattern.compile("</p>",
    Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);
    HtmlParagraphTag2Pattern = Pattern.compile("\\.\\.</p>",
    Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);
    
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
    
    ImportHtmlFilesFromTheWebParameter parameter = null;
    if (pParameter instanceof ImportHtmlFilesFromTheWebParameter) {
      parameter = (ImportHtmlFilesFromTheWebParameter)pParameter;
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
    
    return new ImportHtmlFilesFromTheWebParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ImportHtmlFilesFromTheWebResult();
    
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
    
    if (Parameter != null && Parameter instanceof ImportHtmlFilesFromTheWebParameter) {
      CastParameter = (ImportHtmlFilesFromTheWebParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, "Error: Text files cannot be imported!");
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    
    File sourceDirectory = null;
    try {
      sourceDirectory = new File(CastParameter.getSourceDirectory());
    }
    catch(Exception e) { 
      e.printStackTrace(); 
      this.stop();
    }
    // Heiko: Download und Bereinigung
    this.downloadAllFiles(CastParameter.getUrlFileName(),
        CastParameter.getFileNameFilter(),
        CastParameter.getSourceDirectory());
    // Ende
    AllFileNamesInDirectory = new ArrayList();
    this.enumerateAllFileNames(sourceDirectory);

    HashMap metaData = null;
    TextBufferedReader sourceFile = null;
    String sourceFileName = null;
    StringBuffer text = null;
    String currentLine = null;
    
    //int maxProgress = AllFileNamesInDirectory.size();
    int collectionFiles = 0;
    
    for (int i = 0; i < AllFileNamesInDirectory.size(); i++) {
      
      /*counterProgress = i;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Text File " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }*/
      
      if ( ( (String)AllFileNamesInDirectory.get(i) )
      .endsWith(CastParameter.getFileNameFilter())) {
        collectionFiles++;
        sourceFileName = (String)AllFileNamesInDirectory.get(i);
        try {
        sourceFile = new TextBufferedReader(new File(sourceFileName), 100000);
        sourceFile.open();
        text = new StringBuffer();
        currentLine = sourceFile.getFirstLine();
        while (currentLine != null) {
          text.append(currentLine);
          text.append(" ");
          currentLine = sourceFile.getNextLine();
        }
        }
        catch (Exception e) {}
        sourceFile.close();
        metaData = new HashMap();
        metaData.put("SourceFile", sourceFileName);
        DiasdemDocument = DiasdemCollection.instantiateDefaultDiasdemDocument();
        DiasdemDocument.setOriginalText(text.toString().trim());
        DiasdemDocument.setMetaData(metaData);
        DiasdemCollection.addDocument(DiasdemDocument);
      }  // if: current file meets criterion
      
    }  // for: process all files in directory
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT,
    collectionFiles + " matching plain text files have been found in\n"
    + Tools.shortenFileName(CastParameter.getSourceDirectory(), 50) + ".\n" 
    + "They have been imported into the DIAsDEM collection\n" + Tools
    .shortenFileName(DiasdemCollection.getCollectionFileName(), 50) + ".\n"
    + "Currently, this collection comprises " + DiasdemCollection
    .getNumberOfDocuments() + " documents.");
    
    this.setTaskResult(100, collectionFiles + " Text Files Imported ...", 
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
      if (!allSourceFiles[i].isDirectory())
      {
        AllFileNamesInDirectory.add(allSourceFiles[i].getAbsolutePath());
      }     
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private static String makeFileName(int fileNo) {
    
    String fileName = "download";
    if (fileNo < 10) fileName += "0";
    if (fileNo < 100) fileName += "0";
    if (fileNo < 1000) fileName += "0";
    fileName = fileName + fileNo;
    
    return fileName;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void cleanHTMLFile(String pInputFileName,
  String pOutputFileName) {
    
    StringBuffer fileBuffer = new StringBuffer(100000);
    String fileContent = "";
    String line = "";
    TextBufferedReader reader = new TextBufferedReader(new File(
    pInputFileName), 100000);
    reader.open();
    while ((line = reader.getNextLine()) != null) {
      fileBuffer.append(line);
      fileBuffer.append(Tools.getLineSeparator());
    }
    reader.close();
    fileContent = fileBuffer.toString();
    
    // detagging
    String newPattern = " "; String oldPattern = null;
    
    // delete comments
    TmpMatcher = HtmlCommentsPattern.matcher(fileContent);
    fileContent = TmpMatcher.replaceAll(newPattern);
    
    // delete scripting
    TmpMatcher = HtmlScriptPattern.matcher(fileContent);
    fileContent = TmpMatcher.replaceAll(newPattern);
    
    // mark end of paragraph with .
    // first replace every </p> with .</p> and then
    // replace possible .. by .
    // reason: . is a special character in regular expressions
    TmpMatcher = HtmlParagraphTagPattern.matcher(fileContent);
    fileContent = TmpMatcher.replaceAll(".</p>");
    TmpMatcher = HtmlParagraphTag2Pattern.matcher(fileContent);
    fileContent = TmpMatcher.replaceAll(" .");
    
    // delete all other tags
    TmpMatcher = HtmlTagPattern.matcher(fileContent);
    fileContent = TmpMatcher.replaceAll(newPattern);
    // end detagging
    
    TextBufferedWriter writer = new TextBufferedWriter(
    new File(pOutputFileName), 100000);
    writer.open();
    writer.setFirstLine(fileContent);
    writer.close();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void downloadAllFiles(String pUrlFile, String pFileNameFilter, 
  String pDownloadDirectory) {
    
    try {
      TextBufferedReader urlFileReader = new TextBufferedReader(
      new File(pUrlFile));
      urlFileReader.open();

      int counterProgress = 0;
      int maxProgress = 0;
      String urlString = urlFileReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (urlString != null) {
        ++maxProgress;
        urlString = urlFileReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }

      // Dateien in der URL-Liste downloaden
      int fileNo = 1;
      urlString = urlFileReader.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (urlString != null) {
        URL url = new URL(urlString);
        InputStreamReader inputStreamReader = new InputStreamReader(
        url.openStream(), "ISO-8859-1"); // "ISO8859_1");
        BufferedReader inputStream = new BufferedReader(
        inputStreamReader, 102400);
        String outputFileName = Tools.ensureTrailingSlash(pDownloadDirectory)
        + makeFileName(fileNo++) + ".html";
        TextBufferedWriter writer = new TextBufferedWriter(
        new File(outputFileName), 100000);
        writer.open();
        StringBuffer buffer = new StringBuffer(100000);
        String line = null;
        while ((line = inputStream.readLine()) != null) {
          buffer.append(line);
          buffer.append(Tools.getLineSeparator());
        }
        writer.setFirstLine(buffer.toString());
        inputStream.close();
        writer.close();
        cleanHTMLFile(outputFileName, Tools.removeFileExtension(
        outputFileName) + pFileNameFilter);
        ++counterProgress;
        //System.out.println("counterProgress: " + counterProgress);
        if (counterProgress == 1 || (counterProgress % 2) == 0) {
          //System.out.println("updateProgress: " + counterProgress * 100 / maxProgress);
          Progress.update( (int)(counterProgress * 100 / maxProgress),
          "Processing URL " + counterProgress);
          DiasdemServer.setTaskProgress(Progress, TaskThread);
        }
        urlString = urlFileReader.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      urlFileReader.close();
    } catch (MalformedURLException mue) {
      //System.out.println("MalformedURLException: " + mue.getMessage());
    } catch (IOException ie) {
      //System.out.println("IOException: " + ie.getMessage());
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