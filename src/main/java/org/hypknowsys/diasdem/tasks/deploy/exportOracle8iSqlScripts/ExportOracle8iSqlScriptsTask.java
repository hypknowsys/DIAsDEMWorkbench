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

package org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts;

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
 * This class is beta squared! Do not use it without proper knowledge ...
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ExportOracle8iSqlScriptsTask 
extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ExportOracle8iSqlScriptsParameter CastParameter = null;

  private DIAsDEMconceptualDtd DerivedDtd = null;

  private File ClobDirectoryFile = null;
  private String ClobDirectoryFileName = null;
  private int ClobDirectoryIndex = 1;
  private int FilesInClobDirectory = 0;
  private int MaxFilesInClobDirectory = 0;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Export Oracle 8i SQL Scripts";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts"
  + ".ExportOracle8iSqlScriptsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts"
  + ".ExportOracle8iSqlScriptsResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts"
  + ".ExportOracle8iSqlScriptsControlPanel";
  
  private static final int START_ID_COLLECTION = 1000000;
  private static final String TABLE_COLLECTION = "DIAsDEM";

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ExportOracle8iSqlScriptsTask() {
    
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
    
    ExportOracle8iSqlScriptsParameter parameter = null;
    if (pParameter instanceof ExportOracle8iSqlScriptsParameter) {
      parameter = (ExportOracle8iSqlScriptsParameter)pParameter;
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
    file = new File(parameter.getDtdFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_EXTENSION +
      "-file in the field 'Conceptual DTD File'!");
    }
    file = new File(parameter.getScriptDirectory());
    if (file.exists() && file.isDirectory() && file.list() != null
    && file.list().length > 0) {
      result.addWarning(
      "Warning: The directory specified in the field\n" +
      "'SQL Script Directory' is not empty. Do you\n" +
      "really want to select this directory?");
    }
    file = new File(parameter.getScriptDirectory());
    if (!file.exists()) {
      try {
        boolean success = file.mkdirs();
      }
      catch (Exception e2) {}
    }
    if (!file.exists() || !file.isDirectory()) {
      result.addError(
      "Please enter the name of an existing local\n" +
      "directory in the field 'SQL Script Directory'!");
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ExportOracle8iSqlScriptsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ExportOracle8iSqlScriptsResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_PUT_RESULTS_INTO_USE,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof ExportOracle8iSqlScriptsParameter) {
      CastParameter = (ExportOracle8iSqlScriptsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: SQL scripts cannot be exported!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    if (DiasdemDocument.getProcessedTextUnit(0).getIteration() < 0
    || DiasdemDocument.getProcessedTextUnit(0).getClusterLabel() == null
    || DiasdemDocument.getProcessedTextUnit(0).getClusterID() < 0) {
      this.setErrorTaskResult(100, shortErrorMessage,
      "Error: The default active text units layer " 
      + DiasdemProject.getActiveTextUnitsLayerIndex() + 
      "\nof the first document does not contain any\n" +
      "tagged processed text units at all!");
      this.stop();
    }
    
    HashSet tags = new HashSet();    
    TreeSet sortedTags = null;    
    try {
      DerivedDtd = new DefaultDIAsDEMconceptualDtd(
      CastParameter.getDtdFileName(), KProperties.LOAD);
    }
    catch (Exception e) {
      super.setErrorTaskResult(100, shortErrorMessage,
      "I/O-error: Conceptual DTD\ncollection cannnot be opened!");
      this.stop();
    }

    TextFile oracleControlFile = null; 
    TextFile oracleDataFile = null;
    TextFile oracleTextFile = null; 
    String oracleTextFileName = null;
    TextFile oracleXmlFile = null;
    String oracleXmlFileName = null;
    TextFile dropTablesFile = null;
    TextFile createTablesFile = null;
    TextFile dropIndexesFile = null;
    TextFile createIndexesFile = null;
    TextFile createSectionsFile = null;
    TextFile insertTablesFile = null;
    int tableCollectionID = START_ID_COLLECTION;
    StringBuffer currentXmlDocument = null;
    String scriptDirectory = CastParameter.getScriptDirectory();
    if ( !scriptDirectory.endsWith(File.separator) ) {
      scriptDirectory = scriptDirectory + File.separator;
      CastParameter.setScriptDirectory(scriptDirectory);
    }

    int counterProgress = 0;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();

    File firstXmlFile = null;
    String[] TextUnitTagged = null;
    String TextUnit = null;
    String featureLine = null;
    NamedEntity[] features = null;

    DiasdemDocument = DiasdemCollection.getFirstDocument();
    dropTablesFile = new TextFile( new File(
    scriptDirectory + "1DropTables.sql") );
    dropTablesFile.empty();
    dropTablesFile.open();
    createTablesFile = new TextFile( new File(
    scriptDirectory + "3CreateTables.sql") );
    createTablesFile.empty();
    createTablesFile.open();
    dropIndexesFile = new TextFile( new File(
    scriptDirectory + "2DropIndexes.sql") );
    dropIndexesFile.empty();
    dropIndexesFile.open();
    insertTablesFile = new TextFile( new File(
    scriptDirectory + "4InsertTables.sql") );
    insertTablesFile.empty();
    insertTablesFile.open();
    oracleControlFile = new TextFile( new File(
    scriptDirectory + "5aControlFile.sql") );
    oracleControlFile.empty();
    oracleControlFile.open();
    oracleDataFile = new TextFile( new File(
    scriptDirectory + "5bDataFile.sql") );
    oracleDataFile.empty();
    oracleDataFile.open();
    createSectionsFile = new TextFile( new File(
    scriptDirectory + "6CreateSections.sql") );
    createSectionsFile.empty();
    createSectionsFile.open();
    createIndexesFile = new TextFile( new File(
    scriptDirectory + "7CreateIndexes.sql") );
    createIndexesFile.empty();
    createIndexesFile.open();

    oracleControlFile.setNextLine(
      "LOAD DATA INFILE '" + CastParameter.getScriptDirectory() 
      + "5bDataFile.sql'");
    oracleControlFile.setNextLine(
      "INTO TABLE " + TABLE_COLLECTION + "DOCS APPEND");
    oracleControlFile.setNextLine(
      "FIELDS TERMINATED BY ',' (");
    oracleControlFile.setNextLine(
      "ID, TEXT_FILE_NAME FILLER CHAR(200), XML_FILE_NAME FILLER CHAR(200),");
    oracleControlFile.setNextLine(
      "DOCUMENT_TEXT LOBFILE(TEXT_FILE_NAME) TERMINATED BY EOF,");
    oracleControlFile.setNextLine(
      "DOCUMENT_XML LOBFILE(XML_FILE_NAME) TERMINATED BY EOF)");

    dropTablesFile.setNextLine("GRANT CTXAPP TO " + 
     CastParameter.getDatabaseUser() + ";");
    dropTablesFile.setNextLine("SET LINESIZE 100;");
    dropTablesFile.setNextLine("SET PAGESIZE 4000;");
    dropTablesFile.setNextLine("SET LONG 4000;");
    dropTablesFile.setNextLine("SET LONGCHUNKSIZE 4000;");
    dropTablesFile.setNextLine("SET DEFINE OFF;");
    dropTablesFile.setNextLine("DROP TABLE " + TABLE_COLLECTION + ";");
    dropTablesFile.setNextLine("DROP TABLE " + TABLE_COLLECTION + "DOCS;");

    dropIndexesFile.setNextLine("DROP INDEX " + TABLE_COLLECTION + 
      "XML_INDEX;");
    dropIndexesFile.setNextLine("DROP INDEX " + TABLE_COLLECTION + 
      "TEXT_INDEX;");
    dropIndexesFile.setNextLine("BEGIN");
    dropIndexesFile.setNextLine(
      "  ctx_ddl.drop_section_group('xml_sections');");
    // Warum html_sections? (auch createIndexFile)
    dropIndexesFile.setNextLine(
      "  ctx_ddl.drop_section_group('html_sections');");
    dropIndexesFile.setNextLine(
      "  ctx_ddl.unset_attribute('my_basic_lexer', 'index_text');");
    dropIndexesFile.setNextLine(
      "  ctx_ddl.unset_attribute('my_basic_lexer', 'index_themes');");
    dropIndexesFile.setNextLine(
      "  ctx_ddl.drop_preference('my_basic_lexer');");
    dropIndexesFile.setNextLine("END;");

    createSectionsFile.setNextLine("BEGIN");
    createSectionsFile.setNextLine(
      "  ctx_ddl.create_section_group('xml_sections', 'XML_SECTION_GROUP');");
    createSectionsFile.setNextLine(
      "  ctx_ddl.create_section_group('html_sections', 'HTML_SECTION_GROUP');");
    String elementName = null;
    int elementNumber = 1;
    String sectionName = null;
    String attributeName = null;
    int attributeNumber = 1;
    TreeSet attributeNames = null;
    Iterator tmpIterator2 = null;  // attributes of each element
    Iterator tmpIterator = DerivedDtd.getElementNames().iterator();
    while ( tmpIterator.hasNext() ) {

      // get elements
      elementName = (String)tmpIterator.next();
      sectionName = "Tag" + elementNumber;
      sectionName = sectionName.substring(0 , Math.min(30,
        sectionName.length() ) );
      createSectionsFile.setNextLine(
        // section name should not include '_' and must be varchar2(30)
        "  ctx_ddl.add_zone_section('xml_sections', '" +
        sectionName + "', '" + elementName + "');");

      // get attributes of each element
      attributeNames = DerivedDtd.getElementAttributesNames(
        elementName, DerivedDtd.getMinAttributeRelSupport() );
      if (attributeNames != null) {
        tmpIterator2 = attributeNames.iterator();
        attributeNumber = 1;
        while ( tmpIterator2.hasNext() ) {
          attributeName = (String)tmpIterator2.next();
          sectionName = "Attribute" + attributeNumber++ + "OfTag" + 
            elementNumber;
          sectionName = sectionName.substring(0 , Math.min(30,
            sectionName.length() ) );
          createSectionsFile.setNextLine(
            // section name should not include '_' and must be varchar2(30)
            "  ctx_ddl.add_attr_section('xml_sections', '" +
            sectionName + "', '" + elementName + "@" + attributeName + "');");
        }
      }
      elementNumber++;

    }
    createSectionsFile.setNextLine(
      "  ctx_ddl.create_preference('my_basic_lexer', 'basic_lexer');");
    createSectionsFile.setNextLine(
      "  ctx_ddl.set_attribute('my_basic_lexer', 'index_text','true');");
    createSectionsFile.setNextLine(
      "  ctx_ddl.set_attribute('my_basic_lexer', 'index_themes','false');");
    createSectionsFile.setNextLine("END;");

    // index name has maximum length of 25
    createIndexesFile.setNextLine(
      "CREATE INDEX " + TABLE_COLLECTION + "TEXT_INDEX ON");
    createIndexesFile.setNextLine(
      "  " + TABLE_COLLECTION +
      "DOCS (DOCUMENT_TEXT) INDEXTYPE IS ctxsys.context");
    createIndexesFile.setNextLine(
      "  PARAMETERS ('lexer my_basic_lexer section group html_sections');");
    createIndexesFile.setNextLine(
      "CREATE INDEX " + TABLE_COLLECTION + "XML_INDEX ON");
    createIndexesFile.setNextLine(
      "  " + TABLE_COLLECTION +
      "DOCS (DOCUMENT_XML) INDEXTYPE IS ctxsys.context");
    createIndexesFile.setNextLine(
      "  PARAMETERS ('lexer my_basic_lexer section group xml_sections');");

    createTablesFile.setNextLine("CREATE TABLE " + TABLE_COLLECTION + " (");
    createTablesFile.setNextLine("  ID                   NUMBER PRIMARY KEY,");
    createTablesFile.setNextLine("  FILE_NAME            VARCHAR2(300)");
    createTablesFile.setNextLine(");");

    createTablesFile.setNextLine("CREATE TABLE " + 
      TABLE_COLLECTION + "DOCS (");
    createTablesFile.setNextLine("  ID                   NUMBER PRIMARY KEY,");
    createTablesFile.setNextLine("  DOCUMENT_TEXT        CLOB,");
    createTablesFile.setNextLine("  DOCUMENT_XML         CLOB");
    createTablesFile.setNextLine(");");

    while (DiasdemDocument != null) {
      
      counterProgress++;
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }

      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());

      currentXmlDocument = new StringBuffer(4000);
      
      oracleTextFile = new TextFile( new File(
      this.getClobDirectory() + tableCollectionID + ".txt.sql") );
      oracleTextFileName = oracleTextFile.getFile().getAbsolutePath();
      oracleTextFile.empty(); oracleTextFile.open();
      oracleXmlFile = new TextFile( new File(
      this.getClobDirectory() + tableCollectionID + ".xml.sql") );
      oracleXmlFileName = oracleXmlFile.getFile().getAbsolutePath();
      oracleXmlFile.empty(); oracleXmlFile.open();
      
      oracleDataFile.setNextLine(tableCollectionID + "," +
      oracleTextFileName + "," + oracleXmlFileName);
      
      currentXmlDocument.append( DerivedDtd.getEncodingTag() );
      currentXmlDocument.append(" ");
      currentXmlDocument.append( DerivedDtd.getDoctypeTag() );
      currentXmlDocument.append(" ");
      currentXmlDocument.append( DerivedDtd.getStartRootElementTag() );
      currentXmlDocument.append(" ");
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      // read-only task does not require backup      
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits();
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
        
        if (DiasdemTextUnit.getClusterLabel() != null
        && (DiasdemTextUnit.getClusterLabel().equals("-")
        || DiasdemTextUnit.getClusterLabel().equals("="))) {
          currentXmlDocument.append(Tools.insertISO88591EntityReferences(
          DiasdemDocument.getOriginalTextUnit(i).getContentsAsString()
          .replace('\n', ' ')));
          currentXmlDocument.append(' ');
        }
        else {
          features = NamedEntity.getContainedNamedEntities(DiasdemDocument
          .getActiveTextUnitsLayer(), DiasdemTextUnit.getContentsAsString());
          featureLine = DerivedDtd.getElementAttributesString(DiasdemTextUnit
          .getClusterLabel(), features, DerivedDtd.getMinAttributeRelSupport());
          
          if (featureLine.length() > 0) {
            currentXmlDocument.append("<" + DiasdemTextUnit.getClusterLabel() +
            featureLine.replace('_', ' ') + ">");  // multi-token terms
          }
          else {
            currentXmlDocument.append("<" + DiasdemTextUnit.getClusterLabel() + ">");
          }
          currentXmlDocument.append(Tools.insertISO88591EntityReferences(
          DiasdemDocument.getOriginalTextUnit(i).getContentsAsString()
          .replace('\n', ' ')));
          currentXmlDocument.append("</" + DiasdemTextUnit.getClusterLabel() + ">");
          currentXmlDocument.append(' ');
        }
      }
      currentXmlDocument.append(DerivedDtd.getEndRootElementTag());

      // insert entire document
      insertTablesFile.setNextLine("INSERT INTO " + TABLE_COLLECTION);
      insertTablesFile.setNextLine("  (ID, FILE_NAME)");
      insertTablesFile.setNextLine("  VALUES (" + tableCollectionID + 
      ", null);" );
      insertTablesFile.setNextLine("PROMPT Insert of tuple " + 
        tableCollectionID + " in table " + TABLE_COLLECTION + " is finished.");

      // create LOB files
      oracleTextFile.setNextLine(DiasdemDocument.getOriginalText());
      oracleTextFile.close();
      oracleXmlFile.setNextLine(currentXmlDocument.toString());
      oracleXmlFile.close();
   
      tableCollectionID++;
      DiasdemDocument = DiasdemCollection.getNextDocument();

    }  // read all documents


    super.closeDiasdemCollection();
    if (oracleControlFile != null) 
      oracleControlFile.close();
    if (oracleDataFile != null) 
      oracleDataFile.close();
    if (oracleTextFile != null) 
      oracleTextFile.close();
    if (oracleXmlFile != null) 
      oracleXmlFile.close();
    if (dropTablesFile != null) 
      dropTablesFile.close();
    if (createTablesFile != null) 
      createTablesFile.close();
    if (dropIndexesFile != null) 
      dropIndexesFile.close();
    if (createSectionsFile != null) 
      createSectionsFile.close();
    if (createIndexesFile != null) 
      createIndexesFile.close();
    if (insertTablesFile != null) 
      insertTablesFile.close();

    Result.update(TaskResult.FINAL_RESULT, 
      "The exported SQL scripts have been copied into the directory\n" + 
      Tools.shortenFileName(CastParameter.getScriptDirectory(), 50) +"!");
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
  
  private String getClobDirectory() {
    
    if (ClobDirectoryFile == null) {
      MaxFilesInClobDirectory = DiasdemProject.getIntProperty(
      "MAX_FILES_PER_DIRECTORY");
      ClobDirectoryIndex = 0;
      FilesInClobDirectory = MaxFilesInClobDirectory;
    }
    if (FilesInClobDirectory == MaxFilesInClobDirectory) {
      ClobDirectoryIndex++;
      FilesInClobDirectory = 0;
      ClobDirectoryFileName = Tools.ensureTrailingSlash(CastParameter
      .getScriptDirectory()) + "clobPart" + ClobDirectoryIndex 
      + File.separator;
      ClobDirectoryFile = new File(ClobDirectoryFileName);
      if (!ClobDirectoryFile.exists()) {
        ClobDirectoryFile.mkdirs();
      }
      DerivedDtd.writeXmlRepresentation(ClobDirectoryFileName);
    }
    FilesInClobDirectory++;
    
    return ClobDirectoryFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}