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

package org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities21;

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
import gnu.regexp.*;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class ReplaceNamedEntitiesTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ReplaceNamedEntitiesParameter CastParameter = null;
  
  private NamedEntityExtractor21 CurrentExtractor = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient String CompanyNameTokenized = null;
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Replace Named Entities 2.1";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities21"
  + ".ReplaceNamedEntitiesParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities21"
  + ".ReplaceNamedEntitiesResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities21"
  + ".ReplaceNamedEntitiesControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("NEEX21_DEFAULT_FORENAMES_FILE",
    "NEEX 2.1: Default Forenames File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_SURNAMES_FILE", 
    "NEEX 2.1: Default Surnames File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_SURNAME_SUFFIXES_FILE", 
    "NEEX 2.1: Default Surname Suffixes File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_MIDDLE_INITIALS_FILE", 
    "NEEX 2.1: Default Middle Initials File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_TITLES_FILE", 
    "NEEX 2.1: Default Titles File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_PLACES_FILE", 
    "NEEX 2.1: Default Places File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_ORGANIZATIONS_START_FILE",
    "NEEX 2.1: Default Organizations Start File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_ORGANIZATIONS_END_FILE",
    "NEEX 2.1: Default Organizations End File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_ORGANIZATIONS_AFFIXES_FILE",
    "NEEX 2.1: Default Organizations Affixes File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_COMPOSITE_NE_FILE", 
    "NEEX 2.1: Default Composite NE File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_REGEX_NE_FILE", 
    "NEEX 2.1: Default Regex NE File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_NAME_AFFIXES_FILE", 
    "NEEX 2.1: Default Name Affixes File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_PLACE_AFFIXES_FILE", 
    "NEEX 2.1: Default Place Affixes File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_ORGANIZATIONS_FILE",
    "NEEX 2.1: Default Organizations File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_ORGANIZATIONS_META_DATA",
    "NEEX 2.1: Default Organizations Meta Data Attribute in DIAsDEM Documents",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_CREATE_CANONICAL_FORMS",
    "NEEX 2.1: Default Create Canonical Forms of Persons and Companies",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_PLACE_INDICATORS_FILE", 
    "NEEX 2.1: Default Place Indicators File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_CREATE_DEBUGGING_FILES",
    "NEEX 2.1: Default Create Debugging HTML Files",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_DEBUGGING_FILE_DIRECTORY", 
    "NEEX 2.1: Default Directory of Debugging HTML Files",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_PERSON_NAME_INDICATORS_FILE_NAME", 
    "NEEX 2.1: Default Person Name Indicators File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_PROFESSIONS_FILE_NAME", 
    "NEEX 2.1: Default Professions File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_EXTRACT_STREET_BASIC_NE", 
    "NEEX 2.1: Default Extract Street Basic NE",
    "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_STREET_EXCEPTIONS_FILE_NAME", 
    "NEEX 2.1: Default Street Exceptions File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_STREET_SUFFIXES_FILE_NAME", 
    "NEEX 2.1: Default Street Suffixes File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),    
    new KProperty("NEEX21_DEFAULT_PREFIX_TOKEN_REGEX", 
    "NEEX 2.1: Default Regex for Street Prefix Tokens",
    "([A-Z][A-Za-z\\-\\.]*)",
    KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_AFFFIX_TOKEN_REGEX", 
    "NEEX 2.1: Default Regex for Street Prefix Tokens",
    "([0-9\\-]{1,4}[a-zA-Z]?|[a-zA-Z\\-\\/]?|[Nn][Rr][\\.]?)", 
    KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_STREET_EXCLUSION_REGEX", 
    "NEEX 2.1: Default Regex for Excluding Street Candidates",
    "(.*-$|^Die.*|^Der.*|^Das.*|[\\p{Alpha}]*ring$|.*\\/$)", 
    KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_MIN_TOKEN_IN_STREET", 
    "NEEX 2.1: Default Minimum Number of Tokens in Street Basic NE",
    "2", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NEEX21_DEFAULT_ORGANIZATION_INDICATOR_REGEX", 
    "NEEX 2.1: Default Regex for Tokens to Serve as Organization Indicators",
    "[0-9\\.]{2,}", KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceNamedEntitiesTask() {
    
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
    
    ReplaceNamedEntitiesParameter parameter = null;
    if (pParameter instanceof ReplaceNamedEntitiesParameter) {
      parameter = (ReplaceNamedEntitiesParameter)pParameter;
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
    
    file = new File(parameter.getForenamesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Forenames File'!");
    }
    file = new File(parameter.getSurnamesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Surnames File'!");
    }
    file = new File(parameter.getSurnameSuffixesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Surname Suffixes File'!");
    }
    file = new File(parameter.getMiddleInitialsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Middle Initials File'!");
    }
    file = new File(parameter.getTitlesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Titles File'!");
    }
    file = new File(parameter.getPlacesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Places File'!");
    }
    file = new File(parameter.getOrganizationsStartFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Organization Indicators File'!");
    }
    if (!Tools.stringIsNullOrEmpty(parameter.getOrganizationIndicatorRegex())) {
      if (!Tools.isSyntacticallyCorrectRegex(parameter
      .getOrganizationIndicatorRegex())) {
        result.addError(
        "Error: Please enter a syntactically correct regular\n" +
        "expression in the field 'Organization Indicator Regex'!");
      }
    }
    file = new File(parameter.getOrganizationsEndFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Organization Suffixes File'!");
    }
    file = new File(parameter.getCompositeFeaturesFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Composite NE File'!");
    }
    file = new File(parameter.getRegularExpressionsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
      "-file in the field 'Regex NE File'!");
    }
    if (parameter.getOrganizationsAffixesFileName() != null
    && parameter.getOrganizationsAffixesFileName().length() > 0) {
      file = new File(parameter.getOrganizationsAffixesFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Organization Affixes File'!");
      }
    }
    if (parameter.getOrganizationsFileName() != null
    && parameter.getOrganizationsFileName().length() > 0) {
      file = new File(parameter.getOrganizationsFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Organizations File'!");
      }
    }
    if (parameter.getPlaceIndicatorsFileName() != null
    && parameter.getPlaceIndicatorsFileName().length() > 0) {
      file = new File(parameter.getPlaceIndicatorsFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Place Indicators File'!");
      }
    }
    if (parameter.getPlaceAffixesFileName() != null
    && parameter.getPlaceAffixesFileName().length() > 0) {
      file = new File(parameter.getPlaceAffixesFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Place Affixes File'!");
      }
    }
    if (parameter.getNameAffixesFileName() != null
    && parameter.getNameAffixesFileName().length() > 0) {
      file = new File(parameter.getNameAffixesFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Name Affixes File'!");
      }
    }
    if (parameter.getProfessionsFileName() != null
    && parameter.getProfessionsFileName().length() > 0) {
      file = new File(parameter.getProfessionsFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Professions File'!");
      }
    }
    if (parameter.getPersonNameIndicatorsFileName() != null
    && parameter.getPersonNameIndicatorsFileName().length() > 0) {
      file = new File(parameter.getPersonNameIndicatorsFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Person Name Indicators File'!");
      }
    }
    if (parameter.getDebuggingFileDirectory() != null
    && parameter.getDebuggingFileDirectory().length() > 0) {
      file = new File(parameter.getDebuggingFileDirectory());
      if (!file.exists() || !file.isDirectory()) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        "directory in the optional field 'Debugging Files Directory'!");
      }
      else {
        result.addWarning(
        "Warning: Creating NEEX debugging files involves\n"
        + "many write operations on HTML files, which are\n"
        + "often monitored by virus scanners. Hence, virus\n"
        + "scanners should be temporarily shut down to en-\n"
        + "sure a high performance named entity extraction.\n"
        + "Note: Warnings like this one can be disabled in the\n"
        + "Tools -> Options dialog. Do you want to proceed?");
      }
    }
    
    if (parameter.extractStreetBasicNe()) {
      file = new File(parameter.getStreetExceptionsFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Street Exceptions File'!");
      }
      file = new File(parameter.getStreetSuffixesFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.TEXT_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter nothing or the name of an existing local\n" +
        DIAsDEMguiPreferences.TEXT_FILE_EXTENSION +
        "-file in the optional field 'Street Suffixes File'!");
      }
      if (!Tools.isSyntacticallyCorrectRegex(parameter
      .getStreetPrefixTokenRegex())) {
        result.addError(
        "Error: Please enter a syntactically correct regular\n" +
        "expression in the field 'Street Prefix Token Regex'!");
      }
      if (!Tools.isSyntacticallyCorrectRegex(parameter
      .getStreetAffixTokenRegex())) {
        result.addError(
        "Error: Please enter a syntactically correct regular\n" +
        "expression in the field 'Street Affix Token Regex'!");
      }
      if (!Tools.isSyntacticallyCorrectRegex(parameter
      .getStreetExclusionRegex())) {
        result.addError(
        "Error: Please enter a syntactically correct regular\n" +
        "expression in the field 'Street Exclusion Regex'!");
      }
      if (parameter.getMinTokenInStreet() <= 0) {
        result.addError(
        "Error: Please enter a valid integer > 0\n" +
        "in the field 'Min. Tokens in Street'!");
      }
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ReplaceNamedEntitiesParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ReplaceNamedEntitiesResult();
    
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
    
    if (Parameter != null && Parameter instanceof ReplaceNamedEntitiesParameter) {
      CastParameter = (ReplaceNamedEntitiesParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    String shortErrorMessage = "Error: Named entities cannot be replaced!";
    this.acceptTask(TaskProgress.INDETERMINATE, 
    "Loading Lists of Known Named Entities");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);        
    
    CurrentExtractor = new NamedEntityExtractor21(CastParameter
    .getNeexParameter());
    
    int counterProgress = 1;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      DiasdemDocument.backupProcessedTextUnits(DiasdemProject
      .getProcessedTextUnitsRollbackOption());
      
      // delete previous and register new company name with CurrentExtractor
      if (CastParameter.getOrganizationsMetaDataAttribute() != null
      && CastParameter.getOrganizationsMetaDataAttribute().length() > 0) {
        if (CompanyNameTokenized != null) {
          CurrentExtractor.deleteTempOrganizationName(CompanyNameTokenized);
          // StringTrie.remove does not truely delete the entire
          // search path within the trie. Hence, search times increase
          // rapidly due to temporary storage of company names.
          if ((counterProgress % 2500) == 0) {
            CurrentExtractor.reloadOrganizationFiles();
          }
        }
        CompanyNameTokenized = (String)DiasdemDocument.getMetaData()
        .get(CastParameter.getOrganizationsMetaDataAttribute().trim());
        if (CompanyNameTokenized != null) {
          CurrentExtractor.addTempOrganizationName(CompanyNameTokenized);
        }
      }
      
      DiasdemDocument.getActiveTextUnitsLayer().resetNamedEntities();
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits(); 
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
        if (TextUnitContentsAsString.indexOf(NamedEntity
        .PLACEHOLDER_PREFIX) >= 0) {
          this.setErrorTaskResult(100, shortErrorMessage,
          "Error: The default active text units " +
          "layer " + DiasdemProject.getActiveTextUnitsLayerIndex() +
          "\ncomprises processed text units that contain\n" +
          "placeholders of previously extracted named\n" +
          "entities. Rollback this text units layer to a\n" +
          "savepoint whose processed text units do\n" +
          "not contain named entity placeholders!");
          this.stop();
        }
        DiasdemTextUnit.setContentsFromString(CurrentExtractor
        .extractNamedEntities(TextUnitContentsAsString, DiasdemDocument
        .getActiveTextUnitsLayer()));
        DiasdemDocument.replaceProcessedTextUnit(i, DiasdemTextUnit);
      }      
      if (CastParameter.determineCanonicalForms()) {
        // not implemented yet ;-)
      }
      
      DiasdemCollection.replaceDocument(DiasdemDocument.getDiasdemDocumentID(),
      DiasdemDocument);
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT,
    "All identified named entities have been replaced\n" +
    "in DIAsDEM documents contained in the collection\n" +
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}