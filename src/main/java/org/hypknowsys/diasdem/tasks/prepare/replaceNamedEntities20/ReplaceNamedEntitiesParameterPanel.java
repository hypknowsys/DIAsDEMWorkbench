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

package org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities20;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
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
      "Select", KeyEvent.VK_S, null, "Select Existing Organizations Start File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("OrganizationsEndButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      OrganizationsEnd_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Organizations End File",
      DIAsDEMguiPreferences.TEXT_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("CompositeFeaturesButton") ) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      CompositeNE_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Composite NE File",
      DIAsDEMguiPreferences.CSV_FILE_FILTER, false, true);
            
    } else if (ActionCommand.equals("RegularExpressionsButton")) {

      CurrentParameterDirectory = this.fileNameButtonClicked(
      RegexNE_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Regex NE File",
      DIAsDEMguiPreferences.CSV_FILE_FILTER, false, true);
            
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
    
    return "Replace Named Entities 2.0";
    
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
    RegexNE_Text.getText());
    
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
    DiasdemProject.setProperty("NEEX20_DEFAULT_FORENAMES_FILE",
    Forenames_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_SURNAMES_FILE",
    Surnames_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_SURNAME_SUFFIXES_FILE",
    SurnameSuffixes_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_MIDDLE_INITIALS_FILE",
    MiddleInitials_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_TITLES_FILE",
    Titles_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_PLACES_FILE",
    Places_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_ORGANIZATIONS_START_FILE",
    OrganizationsStart_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_ORGANIZATIONS_END_FILE",
    OrganizationsEnd_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_COMPOSITE_NE_FILE",
    CompositeNE_Text.getText());
    DiasdemProject.setProperty("NEEX20_DEFAULT_REGEX_NE_FILE",
    RegexNE_Text.getText());
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
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      Forenames_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_FORENAMES_FILE"), 30);
      Surnames_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_SURNAMES_FILE"), 30);
      SurnameSuffixes_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_SURNAME_SUFFIXES_FILE"), 30);
      MiddleInitials_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_MIDDLE_INITIALS_FILE"), 30);
      Titles_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_TITLES_FILE"), 30);
      Places_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_PLACES_FILE"), 30);
      OrganizationsStart_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_ORGANIZATIONS_START_FILE"), 30);
      OrganizationsEnd_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_ORGANIZATIONS_END_FILE"), 30);
      CompositeNE_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_COMPOSITE_NE_FILE"), 30);
      RegexNE_Text = new KTextField(DiasdemProject.getProperty(
      "NEEX20_DEFAULT_REGEX_NE_FILE"), 30);
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
    "Click this button to select the organizations start file.");
    
    OrganizationsEnd_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    OrganizationsEnd_Button.addSingleButton("...", 
      KeyEvent.VK_E, true, true, "OrganizationsEndButton", this,
    "Click this button to select the organizations end file.");
    
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
    Parameter_Panel.addLabel("Forenames File:", 0, 2, KeyEvent.VK_F,
      Forenames_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific forenames.");
    Parameter_Panel.addComponent(Forenames_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(Forenames_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Surnames File:", 0, 4, KeyEvent.VK_S,
      Surnames_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific surnames.");
    Parameter_Panel.addComponent(Surnames_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(Surnames_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Surname Suffixes File:", 0, 6, KeyEvent.VK_U,
      SurnameSuffixes_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific surname suffixes.");
    Parameter_Panel.addComponent(SurnameSuffixes_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(SurnameSuffixes_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Middle Initials File:", 0, 8, KeyEvent.VK_M,
      MiddleInitials_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific middle initials.");
    Parameter_Panel.addComponent(MiddleInitials_Text, 2, 8);
    Parameter_Panel.addKButtonPanel(MiddleInitials_Button, 4, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Titles File:", 0, 10, KeyEvent.VK_T,
      Titles_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific titles.");
    Parameter_Panel.addComponent(Titles_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(Titles_Button, 4, 10);
    Parameter_Panel.addBlankRow(0, 11, 11);
    Parameter_Panel.addLabel("Places File:", 0, 12, KeyEvent.VK_P,
      Places_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific places.");
    Parameter_Panel.addComponent(Places_Text, 2, 12);
    Parameter_Panel.addKButtonPanel(Places_Button, 4, 12);
    Parameter_Panel.addBlankRow(0, 13, 11);
    Parameter_Panel.addLabel("Organizations Start File:", 0, 14, KeyEvent.VK_G,
      OrganizationsStart_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific terms preceding " +
    "organizations.");
    Parameter_Panel.addComponent(OrganizationsStart_Text, 2, 14);
    Parameter_Panel.addKButtonPanel(OrganizationsStart_Button, 4, 14);
    Parameter_Panel.addBlankRow(0, 15, 11);
    Parameter_Panel.addLabel("Organizations End File:", 0, 16, KeyEvent.VK_E,
      OrganizationsEnd_Button.getDefaultButton(), true,
    "Task input: This file contains language-specific abbreviations of " +
    "organizations.");
    Parameter_Panel.addComponent(OrganizationsEnd_Text, 2, 16);
    Parameter_Panel.addKButtonPanel(OrganizationsEnd_Button, 4, 16);
    Parameter_Panel.addBlankRow(0, 17, 11);
    Parameter_Panel.addLabel("Composite NE File:", 0, 18, KeyEvent.VK_N,
      CompositeNE_Button.getDefaultButton(), true,
    "Task input: This file contains NEEX rules for extracting " +
    "composite NEs.");
    Parameter_Panel.addComponent(CompositeNE_Text, 2, 18);
    Parameter_Panel.addKButtonPanel(CompositeNE_Button, 4, 18);
    Parameter_Panel.addBlankRow(0, 19, 11);
    Parameter_Panel.addLabel("Regex NE File:", 0, 20, KeyEvent.VK_R,
      RegexNE_Button.getDefaultButton(), true,
    "Task input: This file contains regular expressions for extracting " +
    "basic NEs.");
    Parameter_Panel.addComponent(RegexNE_Text, 2, 20);
    Parameter_Panel.addKButtonPanel(RegexNE_Button, 4, 20);

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
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}