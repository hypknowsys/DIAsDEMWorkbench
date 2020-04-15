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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ReplaceNamedEntitiesParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ReplaceNamedEntitiesParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField Forenames_Text = null;
  private KButtonPanel Forenames_Button = null;
  private KTextField Surnames_Text = null;
  private KButtonPanel Surnames_Button = null;
  private KTextField SurnameSuffixes_Text = null;
  private KButtonPanel SurnameSuffixes_Button = null;
  private KTextField MiddleInitials_Text = null;
  private KButtonPanel MiddleInitials_Button = null;
  private KTextField Titles_Text = null;
  private KButtonPanel Titles_Button = null;
  private KTextField Places_Text = null;
  private KButtonPanel Places_Button = null;
  private KTextField OrganizationsStart_Text = null;
  private KButtonPanel OrganizationsStart_Button = null;
  private KTextField OrganizationsEnd_Text = null;
  private KButtonPanel OrganizationsEnd_Button = null;
  private KTextField CompositeNE_Text = null;
  private KButtonPanel CompositeNE_Button = null;
  private KTextField RegexNE_Text = null;
  private KButtonPanel RegexNE_Button = null;
  private KTextField NameAffixes_Text = null;
  private KButtonPanel NameAffixes_Button = null;
  private KTextField PlaceAffixes_Text = null;
  private KButtonPanel PlaceAffixes_Button = null;
  private KTextField OrganizationsAffixes_Text = null;
  private KButtonPanel OrganizationsAffixes_Button = null;
  private KTextField Organizations_Text = null;
  private KButtonPanel Organizations_Button = null;
  private KTextField OrganizationsMetaData_Text = null;
  private KCheckBox CanonicalForms_CheckBox = null;
  private KTextField PlaceIndicators_Text = null;
  private KButtonPanel PlaceIndicators_Button = null;
  private KTextField DebuggingFilesDir_Text = null;
  private KButtonPanel DebuggingFilesDir_Button = null;
  private KTextField PersonIndicators_Text = null;
  private KButtonPanel PersonIndicators_Button = null;
  private KTextField Professions_Text = null;
  private KCheckBox ExtractStreetBasicNe_CheckBox = null;
  private KButtonPanel Professions_Button = null;
  private KTextField StreetExceptions_Text = null;
  private KButtonPanel StreetExceptions_Button = null;  
  private KTextField StreetSuffixes_Text = null;
  private KButtonPanel StreetSuffixes_Button = null;
  private KTextField StreetPrefixTokenRegex_Text = null;
  private KTextField StreetAffixTokenRegex_Text = null;
  private KTextField StreetExclusionRegex_Text = null;
  private KTextField MinTokenInStreet_Text = null;
  private KTextField OrganizationIndicatorRegex_Text = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceNamedEntitiesParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceNamedEntitiesParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
    super();
    
    this.setContext(pDiasdemServer, pDiasdemProject, pDiasdemGui,
    pDiasdemGuiPreferences);
    this.initialize();
    
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
  /* ########## interface ActionListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void actionPerformed(ActionEvent e) {
    
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();
    
    if (ActionCommand.equals("CollectionButton") ) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("ForenamesButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      Forenames_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Forenames File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("SurnamesButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      Surnames_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Surnames File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("SurnameSuffixesButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      SurnameSuffixes_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Surname Suffixes File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("MiddleInitialsButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      MiddleInitials_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Middle Initials File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("TitlesButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      Titles_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Titles File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("PlacesButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      Places_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Places File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("OrganizationsStartButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      OrganizationsStart_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Organization Indicators File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("OrganizationsEndButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      OrganizationsEnd_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Organization Suffixes File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("CompositeFeaturesButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      CompositeNE_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Composite NE File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("RegularExpressionsButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      RegexNE_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Regex NE File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("NameAffixesButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      NameAffixes_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Name Affixes File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("PlaceAffixesButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      PlaceAffixes_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Place Affixes File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("OrganizationsAffixesButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      OrganizationsAffixes_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Organization Affixes File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("OrganizationsButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      Organizations_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Organizations File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("PlaceIndicatorsButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      PlaceIndicators_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Place Indicators File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("DebuggingFilesDirButton")) {
      
      this.directoryButtonClicked(DebuggingFilesDir_Text, 
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing Directory for Debugging Files");
      
    } else if (ActionCommand.equals("PersonIndicatorsButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      PersonIndicators_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Person Name Indicators File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("ProfessionsButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      Professions_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Professions File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("StreetExceptionsButton")) {
    
      CurrentParameterDirectory = this.fileNameButtonClicked(
      StreetExceptions_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Street Exceptions File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);

    } else if (ActionCommand.equals("StreetSuffixesButton")) {
    
      CurrentParameterDirectory = this.fileNameButtonClicked(
      StreetSuffixes_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Street Suffixes File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
                
    } else if (ActionCommand.equals("ExtractStreetBasicNe_CheckBox")) {
      
      if (ExtractStreetBasicNe_CheckBox.isSelected()) {
        this.enableStreetFields();
      }
      else {
        this.disableStreetFields();
      }
      
    }
         
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskParameterPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
    
    super.initialize();
    
    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY));
    
    this.createParameterPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Replace Named Entities 2.1";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogMSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    ReplaceNamedEntitiesParameter parameter = new ReplaceNamedEntitiesParameter(
    Collection_Text.getText(),
    Forenames_Text.getText(), 
    Surnames_Text.getText(),
    SurnameSuffixes_Text.getText(), 
    MiddleInitials_Text.getText(),
    Titles_Text.getText(), 
    Places_Text.getText(),
    OrganizationsStart_Text.getText(), 
    OrganizationsEnd_Text.getText(),
    CompositeNE_Text.getText(),
    RegexNE_Text.getText(),
    NameAffixes_Text.getText(), 
    PlaceAffixes_Text.getText(),
    OrganizationsAffixes_Text.getText(),
    Organizations_Text.getText(),
    OrganizationsMetaData_Text.getText(), false,
    PlaceIndicators_Text.getText(), false,
    DebuggingFilesDir_Text.getText(),
    PersonIndicators_Text.getText(),
    Professions_Text.getText(), false,
    StreetExceptions_Text.getText(),
    StreetSuffixes_Text.getText(),
    StreetPrefixTokenRegex_Text.getText(),
    StreetAffixTokenRegex_Text.getText(),
    StreetExclusionRegex_Text.getText(),
    Tools.string2Int(MinTokenInStreet_Text.getText()),
    OrganizationIndicatorRegex_Text.getText());
    
    if (CanonicalForms_CheckBox.isSelected()) {
      parameter.setDetermineCanonicalForms(true);
    }
    else {
      parameter.setDetermineCanonicalForms(false);
    }
    if (DebuggingFilesDir_Text.getText().length() > 0) {
      parameter.setCreateDebuggingFiles(true);
    }
    else {
      parameter.setCreateDebuggingFiles(false);
    }
    if (ExtractStreetBasicNe_CheckBox.isSelected()) {
      parameter.setExtractStreetBasicNe(true);
    }
    else {
      parameter.setExtractStreetBasicNe(false);
    }
    
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ReplaceNamedEntitiesParameter) {
      CastParameter = (ReplaceNamedEntitiesParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    IsEnabled = true;
    this.createParameterPanel();
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {
    
    DiasdemProject.setProperty("DEFAULT_COLLECTION_FILE",
    Collection_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_FORENAMES_FILE",
    Forenames_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_SURNAMES_FILE",
    Surnames_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_SURNAME_SUFFIXES_FILE",
    SurnameSuffixes_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_MIDDLE_INITIALS_FILE",
    MiddleInitials_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_TITLES_FILE",
    Titles_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_PLACES_FILE",
    Places_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_ORGANIZATIONS_START_FILE",
    OrganizationsStart_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_ORGANIZATIONS_END_FILE",
    OrganizationsEnd_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_COMPOSITE_NE_FILE",
    CompositeNE_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_REGEX_NE_FILE",
    RegexNE_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_NAME_AFFIXES_FILE",
    NameAffixes_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_PLACE_AFFIXES_FILE",
    PlaceAffixes_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_ORGANIZATIONS_AFFIXES_FILE",
    OrganizationsAffixes_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_ORGANIZATIONS_FILE",
    Organizations_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_ORGANIZATIONS_META_DATA",
    OrganizationsMetaData_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_PLACE_INDICATORS_FILE",
    PlaceIndicators_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_DEBUGGING_FILE_DIRECTORY",
    DebuggingFilesDir_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_PERSON_NAME_INDICATORS_FILE_NAME",
    PersonIndicators_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_PROFESSIONS_FILE_NAME",
    Professions_Text.getText());
    DiasdemProject.setProperty("NEEX21_DEFAULT_ORGANIZATION_INDICATOR_REGEX",
    OrganizationIndicatorRegex_Text.getText());

    if (CanonicalForms_CheckBox.isSelected()) {
      DiasdemProject.setBooleanProperty(
      "NEEX21_DEFAULT_CREATE_CANONICAL_FORMS", true);
    }
    else {
      DiasdemProject.setBooleanProperty(
      "NEEX21_DEFAULT_CREATE_CANONICAL_FORMS", false);
    }
    if (DebuggingFilesDir_Text.getText().length() > 0) {
      DiasdemProject.setBooleanProperty(
      "NEEX21_DEFAULT_CREATE_DEBUGGING_FILES", true);
    }
    else {
      DiasdemProject.setBooleanProperty(
      "NEEX21_DEFAULT_CREATE_DEBUGGING_FILES", false);
    }
    
    if (ExtractStreetBasicNe_CheckBox.isSelected()) {
      DiasdemProject.setBooleanProperty(
      "NEEX21_DEFAULT_EXTRACT_STREET_BASIC_NE", true);
      DiasdemProject.setProperty("NEEX21_DEFAULT_STREET_EXCEPTIONS_FILE_NAME",
      StreetExceptions_Text.getText());
      DiasdemProject.setProperty("NEEX21_DEFAULT_STREET_SUFFIXES_FILE_NAME",
      StreetSuffixes_Text.getText());
      DiasdemProject.setProperty("NEEX21_DEFAULT_PREFIX_TOKEN_REGEX",
      StreetPrefixTokenRegex_Text.getText());
      DiasdemProject.setProperty("NEEX21_DEFAULT_AFFFIX_TOKEN_REGEX",
      StreetAffixTokenRegex_Text.getText());
      DiasdemProject.setProperty("NEEX21_DEFAULT_STREET_EXCLUSION_REGEX",
      StreetExclusionRegex_Text.getText());
      DiasdemProject.setProperty("NEEX21_DEFAULT_MIN_TOKEN_IN_STREET",
      MinTokenInStreet_Text.getText());
    }
    else {
      DiasdemProject.setBooleanProperty(
      "NEEX21_DEFAULT_EXTRACT_STREET_BASIC_NE", false);
    }
    
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Collection_Text != null) {
      return Collection_Text;
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setEnabled(boolean pEnabled) {
    
    IsEnabled = pEnabled;
    this.setComponentStatus();
    super.setEnabled(IsEnabled);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createParameterPanel() {
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
      KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");
    
    Forenames_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Forenames_Button.addSingleButton("...", 
      KeyEvent.VK_F, true, true, "ForenamesButton", this,
    "Click this button to select the forenames file.");
    
    Surnames_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Surnames_Button.addSingleButton("...", 
      KeyEvent.VK_S, true, true, "SurnamesButton", this,
    "Click this button to select the surnames file.");
    
    SurnameSuffixes_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    SurnameSuffixes_Button.addSingleButton("...", 
      KeyEvent.VK_U, true, true, "SurnameSuffixesButton", this,
    "Click this button to select the surname suffixes file.");
    
    MiddleInitials_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    MiddleInitials_Button.addSingleButton("...", 
      KeyEvent.VK_M, true, true, "MiddleInitialsButton", this,
    "Click this button to select the middle initials file.");
    
    Titles_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Titles_Button.addSingleButton("...", 
      KeyEvent.VK_T, true, true, "TitlesButton", this,
    "Click this button to select the titles file.");
    
    Places_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Places_Button.addSingleButton("...", 
      KeyEvent.VK_P, true, true, "PlacesButton", this,
    "Click this button to select the places file.");
    
    OrganizationsStart_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    OrganizationsStart_Button.addSingleButton("...", 
      KeyEvent.VK_G, true, true, "OrganizationsStartButton", this,
    "Click this button to select the organization indicators file.");
    
    OrganizationsEnd_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    OrganizationsEnd_Button.addSingleButton("...", 
      KeyEvent.VK_E, true, true, "OrganizationsEndButton", this,
    "Click this button to select the organization suffixes file.");
    
    CompositeNE_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    CompositeNE_Button.addSingleButton("...", 
      KeyEvent.VK_N, true, true, "CompositeFeaturesButton", this,
    "Click this button to select the composite NE file.");
    
    RegexNE_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    RegexNE_Button.addSingleButton("...", 
      KeyEvent.VK_R, true, true, "RegularExpressionsButton", this,
    "Click this button to select the regex NE file.");   

    NameAffixes_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    NameAffixes_Button.addSingleButton("...", 
      KeyEvent.VK_I, true, true, "NameAffixesButton", this,
    "Click this button to select the name affixes file.");   

    PlaceAffixes_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    PlaceAffixes_Button.addSingleButton("...", 
      KeyEvent.VK_A, true, true, "PlaceAffixesButton", this,
    "Click this button to select the place affixes file.");   

    OrganizationsAffixes_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    OrganizationsAffixes_Button.addSingleButton("...", 
      KeyEvent.VK_X, true, true, "OrganizationsAffixesButton", this,
    "Click this button to select the organization affixes file.");   

    Organizations_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Organizations_Button.addSingleButton("...", 
      KeyEvent.VK_Z, true, true, "OrganizationsButton", this,
    "Click this button to select the organizations file.");   

    PlaceIndicators_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    PlaceIndicators_Button.addSingleButton("...", 
      0, true, true, "PlaceIndicatorsButton", this,
    "Click this button to select the place indicators file.");   

    PersonIndicators_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    PersonIndicators_Button.addSingleButton("...", 
      0, true, true, "PersonIndicatorsButton", this,
    "Click this button to select the person name indicators file.");   

    Professions_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Professions_Button.addSingleButton("...", 
      KeyEvent.VK_L, true, true, "ProfessionsButton", this,
    "Click this button to select the professions file.");   

    StreetExceptions_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    StreetExceptions_Button.addSingleButton("...", 
      0, true, true, "StreetExceptionsButton", this,
    "Click this button to select the street exceptions file.");   
    
    StreetSuffixes_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
      StreetSuffixes_Button.addSingleButton("...", 
      0, true, true, "StreetSuffixesButton", this,
    "Click this button to select the street suffixes file.");           

    DebuggingFilesDir_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    DebuggingFilesDir_Button.addSingleButton("...", 
      KeyEvent.VK_Y, true, true, "DebuggingFilesDirButton", this,
    "Click this button to select the directory for debugging HTML files.");   

    CanonicalForms_CheckBox = new KCheckBox(
    "Determine Canonical Forms of Named Entities",
    false, false, "CanonicalForms_CheckBox", this, 0,
    "If this box is checked, heuristics are used to determine canonical forms"
    + " of persons and companies within documents.");

    ExtractStreetBasicNe_CheckBox = new KCheckBox(
    "Extract Basic Named Entities of Type 'Street'",
    false, true, "ExtractStreetBasicNe_CheckBox", this, 0,
    "If this box is checked, basic named entities of type 'street' will "
    + "be extracted.");

    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      Forenames_Text = new KTextField(CastParameter
      .getForenamesFileName(), 30);
      Surnames_Text = new KTextField(CastParameter
      .getSurnamesFileName(), 30);
      SurnameSuffixes_Text = new KTextField(CastParameter
      .getSurnameSuffixesFileName(), 30);
      MiddleInitials_Text = new KTextField(CastParameter
      .getMiddleInitialsFileName(), 30);
      Titles_Text = new KTextField(CastParameter
      .getTitlesFileName(), 30);
      Places_Text = new KTextField(CastParameter
      .getPlacesFileName(), 30);
      OrganizationsStart_Text = new KTextField(CastParameter
      .getOrganizationsStartFileName(), 30);
      OrganizationsEnd_Text = new KTextField(CastParameter
      .getOrganizationsEndFileName(), 30);
      CompositeNE_Text = new KTextField(CastParameter
      .getCompositeFeaturesFileName(), 30);
      RegexNE_Text = new KTextField(CastParameter
      .getRegularExpressionsFileName(), 30);
      NameAffixes_Text = new KTextField(CastParameter
      .getNameAffixesFileName(), 30);
      PlaceAffixes_Text = new KTextField(CastParameter
      .getPlaceAffixesFileName(), 30);
      OrganizationsAffixes_Text = new KTextField(CastParameter
      .getOrganizationsAffixesFileName(), 30);
      Organizations_Text = new KTextField(CastParameter
      .getOrganizationsFileName(), 30);
      OrganizationsMetaData_Text = new KTextField(CastParameter
      .getOrganizationsMetaDataAttribute(), 30);
      if (CastParameter.determineCanonicalForms()) {
        CanonicalForms_CheckBox.setSelected(true);
      }
      else {
        CanonicalForms_CheckBox.setSelected(false);
      }
      PlaceIndicators_Text = new KTextField(CastParameter
      .getPlaceIndicatorsFileName(), 30);
      DebuggingFilesDir_Text = new KTextField(CastParameter
      .getDebuggingFileDirectory(), 30);
      PersonIndicators_Text = new KTextField(CastParameter
      .getPersonNameIndicatorsFileName(), 30);
      Professions_Text = new KTextField(CastParameter
      .getProfessionsFileName(), 30);
      StreetExceptions_Text = new KTextField("", 30);
      StreetSuffixes_Text = new KTextField("", 30);
      StreetPrefixTokenRegex_Text = new KTextField("", 30);
      StreetAffixTokenRegex_Text = new KTextField("", 30);
      StreetExclusionRegex_Text = new KTextField("", 30);
      MinTokenInStreet_Text = new KTextField("", 30);
      if (CastParameter.extractStreetBasicNe()) {
        ExtractStreetBasicNe_CheckBox.setSelected(true);
        this.enableStreetFields();
      }
      else {
        ExtractStreetBasicNe_CheckBox.setSelected(false);
        this.disableStreetFields();
      }
      OrganizationIndicatorRegex_Text = new KTextField(CastParameter
      .getOrganizationIndicatorRegex(), 30);
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      Forenames_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_FORENAMES_FILE"), 30);
      Surnames_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_SURNAMES_FILE"), 30);
      SurnameSuffixes_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_SURNAME_SUFFIXES_FILE"), 30);
      MiddleInitials_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_MIDDLE_INITIALS_FILE"), 30);
      Titles_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_TITLES_FILE"), 30);
      Places_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_PLACES_FILE"), 30);
      OrganizationsStart_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_ORGANIZATIONS_START_FILE"), 30);
      OrganizationsEnd_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_ORGANIZATIONS_END_FILE"), 30);
      CompositeNE_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_COMPOSITE_NE_FILE"), 30);
      RegexNE_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_REGEX_NE_FILE"), 30);
      NameAffixes_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_NAME_AFFIXES_FILE"), 30);
      PlaceAffixes_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_PLACE_AFFIXES_FILE"), 30);
      OrganizationsAffixes_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_ORGANIZATIONS_AFFIXES_FILE"), 30);
      Organizations_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_ORGANIZATIONS_FILE"), 30);
      OrganizationsMetaData_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_ORGANIZATIONS_META_DATA"), 30);
      if (DiasdemProject.getBooleanProperty(
      "NEEX21_DEFAULT_CREATE_CANONICAL_FORMS")) {
        CanonicalForms_CheckBox.setSelected(true);
      }
      else {
        CanonicalForms_CheckBox.setSelected(false);
      }      
      PlaceIndicators_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_PLACE_INDICATORS_FILE"), 30);
      DebuggingFilesDir_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_DEBUGGING_FILE_DIRECTORY"), 30);
      PersonIndicators_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_PERSON_NAME_INDICATORS_FILE_NAME"), 30);
      Professions_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_PROFESSIONS_FILE_NAME"), 30);
      StreetExceptions_Text = new KTextField("", 30);
      StreetSuffixes_Text = new KTextField("", 30);
      StreetPrefixTokenRegex_Text = new KTextField("", 30);
      StreetAffixTokenRegex_Text = new KTextField("", 30);
      StreetExclusionRegex_Text = new KTextField("", 30);
      MinTokenInStreet_Text = new KTextField("", 30);
      if (DiasdemProject.getBooleanProperty(
      "NEEX21_DEFAULT_EXTRACT_STREET_BASIC_NE")) {
        ExtractStreetBasicNe_CheckBox.setSelected(true);
        this.enableStreetFields();
      }
      else {
        ExtractStreetBasicNe_CheckBox.setSelected(false);
        this.disableStreetFields();
      }
      OrganizationIndicatorRegex_Text = new KTextField(DiasdemProject
      .getProperty("NEEX21_DEFAULT_ORGANIZATION_INDICATOR_REGEX"), 30);
    }
    Collection_Text.setCaretAtEnding();
    Forenames_Text.setCaretAtEnding();
    Surnames_Text.setCaretAtEnding();
    SurnameSuffixes_Text.setCaretAtEnding();
    MiddleInitials_Text.setCaretAtEnding();
    Titles_Text.setCaretAtEnding();
    Places_Text.setCaretAtEnding();
    OrganizationsStart_Text.setCaretAtEnding();
    OrganizationsEnd_Text.setCaretAtEnding();
    CompositeNE_Text.setCaretAtEnding();
    RegexNE_Text.setCaretAtEnding();
    NameAffixes_Text.setCaretAtEnding();
    PlaceAffixes_Text.setCaretAtEnding();
    OrganizationsAffixes_Text.setCaretAtEnding();
    Organizations_Text.setCaretAtEnding();
    OrganizationsMetaData_Text.setCaretAtEnding();
    PlaceIndicators_Text.setCaretAtEnding();
    DebuggingFilesDir_Text.setCaretAtEnding();
    PersonIndicators_Text.setCaretAtEnding();
    Professions_Text.setCaretAtEnding();
    OrganizationIndicatorRegex_Text.setCaretAtEnding();
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Collection_Text);

    Parameter_Panel.addLabel("Collection File:", 0, 0, KeyEvent.VK_C,
      Collection_Button.getDefaultButton(), true,
    "Task input: This collection file contains references " +
    "to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11);
    Parameter_Panel.addLabel("Regex NE File:", 0, 2, KeyEvent.VK_R,
      RegexNE_Button.getDefaultButton(), true,
    "Task input: This file contains regular expressions for extracting " +
    "basic NEs.");
    Parameter_Panel.addComponent(RegexNE_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(RegexNE_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Organization Indicators File:", 0, 4, KeyEvent.VK_G,
      OrganizationsStart_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific terms preceding " +
    "organizations.");
    Parameter_Panel.addComponent(OrganizationsStart_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(OrganizationsStart_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Organization Indicator Regex:", 0, 6, 0, null, 
    true, "Optional task input: Additional organization indicators are tokens "
    + "that match this regular expression.");
    Parameter_Panel.addComponent(OrganizationIndicatorRegex_Text, 2, 6, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Organization Suffixes File:", 0, 8, KeyEvent.VK_E,
      OrganizationsEnd_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific abbreviations of " +
    "organizations.");
    Parameter_Panel.addComponent(OrganizationsEnd_Text, 2, 8);
    Parameter_Panel.addKButtonPanel(OrganizationsEnd_Button, 4, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Organization Affixes File:", 0, 10, 
    KeyEvent.VK_X, OrganizationsAffixes_Button.getDefaultButton(), true,
    "Optional task input: This file contains organization affixes.");
    Parameter_Panel.addComponent(OrganizationsAffixes_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(OrganizationsAffixes_Button, 4, 10);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("Organizations File:", 0, 12, KeyEvent.VK_Z,
      Organizations_Button.getDefaultButton(), true,
    "Optional task input: This file contains tokenized organizations.");
    Parameter_Panel.addComponent(Organizations_Text, 2, 12);
    Parameter_Panel.addKButtonPanel(Organizations_Button, 4, 12);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("Organizations as Meta Data:", 0, 14, KeyEvent.VK_D,
      OrganizationsMetaData_Text, true,
    "Optional task input: This DIAsDEM document meta data attribute contains a "
    + "tokenized organization.");
    Parameter_Panel.addComponent(OrganizationsMetaData_Text, 2, 14,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 15, 11);
    Parameter_Panel.addLabel("Place Indicators File:", 0, 16, 0,
      PlaceIndicators_Button.getDefaultButton(), true,
    "Optional task input: This file contains place indicators such as 'in'.");
    Parameter_Panel.addComponent(PlaceIndicators_Text, 2, 16);
    Parameter_Panel.addKButtonPanel(PlaceIndicators_Button, 4, 16);
    Parameter_Panel.addBlankRow(0, 17, 11);
    Parameter_Panel.addLabel("Places File:", 0, 18, KeyEvent.VK_P,
      Places_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific places.");
    Parameter_Panel.addComponent(Places_Text, 2, 18);
    Parameter_Panel.addKButtonPanel(Places_Button, 4, 18);
    Parameter_Panel.addBlankRow(0, 19, 11);
    Parameter_Panel.addLabel("Place Affixes File:", 0, 20, KeyEvent.VK_A,
      RegexNE_Button.getDefaultButton(), true,
    "Optional task input: This file contains place affixes such as 'am Main'.");
    Parameter_Panel.addComponent(PlaceAffixes_Text, 2, 20);
    Parameter_Panel.addKButtonPanel(PlaceAffixes_Button, 4, 20);
    Parameter_Panel.addBlankRow(0, 21, 11);
    Parameter_Panel.addLabel("Person Name Indicators File:", 0, 22, 0,
      PersonIndicators_Button.getDefaultButton(), true,
    "Optional task input: This file contains person indicators such as 'von'.");
    Parameter_Panel.addComponent(PersonIndicators_Text, 2, 22);
    Parameter_Panel.addKButtonPanel(PersonIndicators_Button, 4, 22);
    Parameter_Panel.addBlankRow(0, 23, 11);
    Parameter_Panel.addLabel("Titles File:", 0, 24, KeyEvent.VK_T,
      Titles_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific academic titles.");
    Parameter_Panel.addComponent(Titles_Text, 2, 24);
    Parameter_Panel.addKButtonPanel(Titles_Button, 4, 24);
    Parameter_Panel.addBlankRow(0, 25, 11);
    Parameter_Panel.addLabel("Forenames File:", 0, 26, KeyEvent.VK_F,
      Forenames_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific forenames.");
    Parameter_Panel.addComponent(Forenames_Text, 2, 26);
    Parameter_Panel.addKButtonPanel(Forenames_Button, 4, 26);
    Parameter_Panel.addBlankRow(0, 27, 11);
    Parameter_Panel.addLabel("Middle Initials File:", 0, 28, KeyEvent.VK_M,
      MiddleInitials_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific middle initials.");
    Parameter_Panel.addComponent(MiddleInitials_Text, 2, 28);
    Parameter_Panel.addKButtonPanel(MiddleInitials_Button, 4, 28);
    Parameter_Panel.addBlankRow(0, 29, 11);
    Parameter_Panel.addLabel("Surnames File:", 0, 30, KeyEvent.VK_S,
      Surnames_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific surnames.");
    Parameter_Panel.addComponent(Surnames_Text, 2, 30);
    Parameter_Panel.addKButtonPanel(Surnames_Button, 4, 30);
    Parameter_Panel.addBlankRow(0, 31, 11);
    Parameter_Panel.addLabel("Surname Suffixes File:", 0, 32, KeyEvent.VK_U,
      SurnameSuffixes_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific surname suffixes.");
    Parameter_Panel.addComponent(SurnameSuffixes_Text, 2, 32);
    Parameter_Panel.addKButtonPanel(SurnameSuffixes_Button, 4, 32);
    Parameter_Panel.addBlankRow(0, 33, 11);
    Parameter_Panel.addLabel("Name Affixes File:", 0, 34, KeyEvent.VK_I,
      RegexNE_Button.getDefaultButton(), true,
    "Optional task input: This file contains name affixes.");
    Parameter_Panel.addComponent(NameAffixes_Text, 2, 34);
    Parameter_Panel.addKButtonPanel(NameAffixes_Button, 4, 34);
    Parameter_Panel.addBlankRow(0, 35, 11);
    Parameter_Panel.addLabel("Professions File:", 0, 36, KeyEvent.VK_L,
      Professions_Button.getDefaultButton(), true,
    "Optional task input: This file contains professions such as 'Manager'.");
    Parameter_Panel.addComponent(Professions_Text, 2, 36);
    Parameter_Panel.addKButtonPanel(Professions_Button, 4, 36);
    Parameter_Panel.addBlankRow(0, 37, 11);
    
    Parameter_Panel.addLabel("Street Exceptions File:", 0, 38, 0,
    null, true,
    "Optional task input: This file contains street exceptions such as 'Kietz'.");
    Parameter_Panel.addComponent(StreetExceptions_Text, 2, 38);
    Parameter_Panel.addKButtonPanel(StreetExceptions_Button, 4, 38);
    Parameter_Panel.addBlankRow(0, 39, 11);
    Parameter_Panel.addLabel("Street Suffixes File:", 0, 40, 0,
    null, true,
    "Optional task input: This file contains street suffixes such as 'str.'.");
    Parameter_Panel.addComponent(StreetSuffixes_Text, 2, 40);
    Parameter_Panel.addKButtonPanel(StreetSuffixes_Button, 4, 40);
    Parameter_Panel.addBlankRow(0, 41, 11);
    Parameter_Panel.addLabel("Street Prefix Token Regex:", 0, 42, 0,
    null, true, "Optional task input: Regular expression matching tokens "
    + "that precede street indicators.");
    Parameter_Panel.addComponent(StreetPrefixTokenRegex_Text, 2, 42, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 43, 11);
    Parameter_Panel.addLabel("Street Affix Token Regex:", 0, 44, 0,
    null, true, "Optional task input: Regular expression matching tokens "
    + "that follow street indicators.");
    Parameter_Panel.addComponent(StreetAffixTokenRegex_Text, 2, 44, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 45, 11);
    Parameter_Panel.addLabel("Street Exclusion Regex:", 0, 46, 0,
    null, true,
    "Optional task input: Street candidates are matching this regular "
    + "expression are excluded.");
    Parameter_Panel.addComponent(StreetExclusionRegex_Text, 2, 46, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 47, 11);
    Parameter_Panel.addLabel("Min. Tokens in Street:", 0, 48, 0,
    null, true, "Optional task input: Minimum number of tokens in valid "
    + "named entity of type 'street'.");
    Parameter_Panel.addComponent(MinTokenInStreet_Text, 2, 48, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 49, 11);    
    
    Parameter_Panel.addLabel("Composite NE File:", 0, 50, KeyEvent.VK_N,
      CompositeNE_Button.getDefaultButton(), true,
    "Task input: This file contains NEEX2 rules for extracting " +
    "composite NEs.");
    Parameter_Panel.addComponent(CompositeNE_Text, 2, 50);
    Parameter_Panel.addKButtonPanel(CompositeNE_Button, 4, 50);
    Parameter_Panel.addBlankRow(0, 51, 11);
    Parameter_Panel.addLabel("Debugging Files Directory:", 0, 52, KeyEvent.VK_Y,
      DebuggingFilesDir_Button.getDefaultButton(), true,
    "Optional task output: Debugging HTML files visualizing extracted NEs will "
    + "be created in this directory.");
    Parameter_Panel.addComponent(DebuggingFilesDir_Text, 2, 52);
    Parameter_Panel.addKButtonPanel(DebuggingFilesDir_Button, 4, 52);
    Parameter_Panel.addBlankRow(0, 53, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 54);
    Parameter_Panel.addComponent(ExtractStreetBasicNe_CheckBox, 2, 54, 
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 55);
    Parameter_Panel.addComponent(CanonicalForms_CheckBox, 2, 55, 
      new Insets(0, 0, 0, 0), 3, 1);

    Parameter_Panel.setPreferredSize(Parameter_Panel.getMinimumSize());    
    KScrollBorderPanel Parameter_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    Parameter_ScrollPanel.addNorth(Parameter_Panel);
    Parameter_ScrollPanel.startFocusForwarding(Collection_Text);
    
    this.removeAll();
    this.validate();
    this.addCenter(Parameter_ScrollPanel);
    this.validate();
    this.setComponentStatus();    
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void enableStreetFields() {
     
    if (CastParameter != null) {
      if (StreetExceptions_Text != null) {
        StreetExceptions_Text.setText(CastParameter
        .getStreetExceptionsFileName());
      }
      if (StreetSuffixes_Text != null) {
        StreetSuffixes_Text.setText(CastParameter
        .getStreetSuffixesFileName());
      }
      if (StreetPrefixTokenRegex_Text != null) {
        StreetPrefixTokenRegex_Text.setText(CastParameter
        .getStreetPrefixTokenRegex());
      }
      if (StreetAffixTokenRegex_Text != null) {
        StreetAffixTokenRegex_Text.setText(CastParameter
        .getStreetAffixTokenRegex());
      }
      if (StreetExclusionRegex_Text != null) {
        StreetExclusionRegex_Text.setText(CastParameter
        .getStreetExclusionRegex());
      }
      if (MinTokenInStreet_Text != null) {
        MinTokenInStreet_Text.setText(Tools.int2String(CastParameter
        .getMinTokenInStreet()));
      }
    }
    else {
      if (StreetExceptions_Text != null) {
        StreetExceptions_Text.setText(DiasdemProject.getProperty(
        "NEEX21_DEFAULT_STREET_EXCEPTIONS_FILE_NAME"));
      }
      if (StreetSuffixes_Text != null) {
        StreetSuffixes_Text.setText(DiasdemProject.getProperty(
        "NEEX21_DEFAULT_STREET_SUFFIXES_FILE_NAME"));
      }
      if (StreetPrefixTokenRegex_Text != null) {
        StreetPrefixTokenRegex_Text.setText(DiasdemProject.getProperty(
        "NEEX21_DEFAULT_PREFIX_TOKEN_REGEX"));
      }
      if (StreetAffixTokenRegex_Text != null) {
        StreetAffixTokenRegex_Text.setText(DiasdemProject.getProperty(
        "NEEX21_DEFAULT_AFFFIX_TOKEN_REGEX"));
      }
      if (StreetExclusionRegex_Text != null) {
        StreetExclusionRegex_Text.setText(DiasdemProject.getProperty(
        "NEEX21_DEFAULT_STREET_EXCLUSION_REGEX"));
      }
      if (MinTokenInStreet_Text != null) {
        MinTokenInStreet_Text.setText(DiasdemProject.getProperty(
      "NEEX21_DEFAULT_MIN_TOKEN_IN_STREET"));
      }
    }
    if (StreetExceptions_Text != null) {
      StreetExceptions_Text.setCaretAtEnding();
      StreetExceptions_Text.setEnabled(true);
    }
    if (StreetExceptions_Button != null) {
      StreetExceptions_Button.setAllEnabled(true);
    }
    if (StreetSuffixes_Text != null) {
      StreetSuffixes_Text.setCaretAtEnding();
      StreetSuffixes_Text.setEnabled(true);
    }
    if (StreetSuffixes_Button != null) {
      StreetSuffixes_Button.setAllEnabled(true);
    }
    if (StreetPrefixTokenRegex_Text != null) {
      StreetPrefixTokenRegex_Text.setCaretAtEnding();
      StreetPrefixTokenRegex_Text.setEnabled(true);
    }
    if (StreetAffixTokenRegex_Text != null) {
      StreetAffixTokenRegex_Text.setCaretAtEnding();
      StreetAffixTokenRegex_Text.setEnabled(true);
    }
    if (StreetExclusionRegex_Text != null) {
      StreetExclusionRegex_Text.setCaretAtEnding();
      StreetExclusionRegex_Text.setEnabled(true);
    }
    if (MinTokenInStreet_Text != null) {
      MinTokenInStreet_Text.setCaretAtEnding();
      MinTokenInStreet_Text.setEnabled(true);
    }
    
  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private void disableStreetFields() {
    
    if (StreetExceptions_Text != null) {
      StreetExceptions_Text.setText("");
      StreetExceptions_Text.setEnabled(false);
    }
    if (StreetExceptions_Button != null) {
      StreetExceptions_Button.setAllEnabled(false);
    }
    if (StreetSuffixes_Text != null) {
      StreetSuffixes_Text.setText("");
      StreetSuffixes_Text.setEnabled(false);
    }
    if (StreetSuffixes_Button != null) {
      StreetSuffixes_Button.setAllEnabled(false);
    }
    if (StreetPrefixTokenRegex_Text != null) {
      StreetPrefixTokenRegex_Text.setText("");
      StreetPrefixTokenRegex_Text.setEnabled(false);
    }
    if (StreetAffixTokenRegex_Text != null) {
      StreetAffixTokenRegex_Text.setText("");
      StreetAffixTokenRegex_Text.setEnabled(false);
    }
    if (StreetExclusionRegex_Text != null) {
      StreetExclusionRegex_Text.setText("");
      StreetExclusionRegex_Text.setEnabled(false);
    }
    if (MinTokenInStreet_Text != null) {
      MinTokenInStreet_Text.setText("");
      MinTokenInStreet_Text.setEnabled(false);
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