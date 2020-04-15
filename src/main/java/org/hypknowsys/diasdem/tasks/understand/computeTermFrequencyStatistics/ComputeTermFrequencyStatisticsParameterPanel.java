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

package org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics;

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
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ComputeTermFrequencyStatisticsParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ComputeTermFrequencyStatisticsParameter CastParameter = null;
  
  private KTabbedPane Tabbed_Pane = null;
  private KBorderPanel ConditionNorth_Panel = null;
  private KGridBagPanel Condition_Panel = null;
  private KBorderPanel ThesaurusNorth_Panel = null;
  private KGridBagPanel Thesaurus_Panel = null;

  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField TfStatistics_Text = null;
  private KButtonPanel TfStatistics_Button = null;
  private KCheckBox ExportTextsInCsv_CheckBox = null;
  private KCheckBox ExportStatisticsInCsv_CheckBox = null;
  private KCheckBox ExportStatisticsInHtml_CheckBox = null;
  private KCheckBox ExcludeNumbersEtc_CheckBox = null;
  private KCheckBox ComputeConditionalFrequencies_CheckBox = null;
  private KTextField ConditionTextUnitMatchedByRegex_Text = null;
  private KTextField ConditionClusterIdOfTextUnit_Text = null;
  private KTextField ConditionClusterLabelOfTextUnit_Text = null;
  private KTextField ConditionIterationTextUnit_Text = null;
  private KCheckBox MapTokensOntoDescriptors_CheckBox = null;
  private KTextField ThesaurusName_Text = null;
  private KButtonPanel ThesaurusName_Button = null;
  private KComboBox VectorDimensions_Combo = null;
  private KTextField ScopeNotesContain_Text = null;
  
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
  
  public ComputeTermFrequencyStatisticsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ComputeTermFrequencyStatisticsParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if ( ActionCommand.equals("CollectionFileName_Button") ) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(Collection_Text, 
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S, 
      null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);

    } else if ( ActionCommand.equals("WordStatisticsFileName_Button") ) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      TfStatistics_Text, CurrentProjectDirectory, 
      "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S, 
      null, "Select TF Statistics File to be Created",
      DIAsDEMguiPreferences.TF_STATISTICS_FILE_FILTER, false, true);
      
    } else if ( ActionCommand.equals("ComputeConditionalFrequencies") ) {
      
      if (ComputeConditionalFrequencies_CheckBox != null) {
        if (ComputeConditionalFrequencies_CheckBox.isSelected()) {
          ConditionTextUnitMatchedByRegex_Text.setEnabled(true);
          ConditionTextUnitMatchedByRegex_Text.setText("");
          ConditionClusterIdOfTextUnit_Text.setEnabled(true);
          ConditionClusterIdOfTextUnit_Text.setText("");
          ConditionClusterLabelOfTextUnit_Text.setEnabled(true);
          ConditionClusterLabelOfTextUnit_Text.setText("");
          ConditionIterationTextUnit_Text.setEnabled(true);
          ConditionIterationTextUnit_Text.setText("");
          ConditionTextUnitMatchedByRegex_Text.requestFocus();
          Tabbed_Pane.setEnabledAt(1, true);
        }
        else {
          ConditionTextUnitMatchedByRegex_Text.setEnabled(false);
          ConditionTextUnitMatchedByRegex_Text.setText("");
          ConditionClusterIdOfTextUnit_Text.setEnabled(false);
          ConditionClusterIdOfTextUnit_Text.setText("");
          ConditionClusterLabelOfTextUnit_Text.setEnabled(false);
          ConditionClusterLabelOfTextUnit_Text.setText("");
          ConditionIterationTextUnit_Text.setEnabled(false);
          ConditionIterationTextUnit_Text.setText("");
          Tabbed_Pane.setEnabledAt(1, false);
        }
        Tools.requestFocus(ComputeConditionalFrequencies_CheckBox);
      }
      
    } else if ( ActionCommand.equals("MapTokensOntoDescriptors") ) {
      
      if (MapTokensOntoDescriptors_CheckBox != null) {
        if (MapTokensOntoDescriptors_CheckBox.isSelected()) {
          if (CastParameter != null) {
            MapTokensOntoDescriptors_CheckBox.setSelected(true);
            ThesaurusName_Text.setEnabled(true);
            ThesaurusName_Text.setText(CastParameter.getThesaurusFileName());
            ThesaurusName_Button.setAllEnabled(true);
            VectorDimensions_Combo.setEnabled(true);
            if (CastParameter.getVectorDimensions() >= 0
            && CastParameter.getVectorDimensions()
            < ComputeTermFrequencyStatisticsParameter.VECTOR_DIMENSIONS_OPTIONS.length) {
              VectorDimensions_Combo.setSelectedIndex(CastParameter
              .getVectorDimensions());
            }
            else {
              VectorDimensions_Combo.setSelectedIndex(
              ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS);
            }
            if (CastParameter.getVectorDimensions()
            == ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS ||
            CastParameter.getVectorDimensions()
            == ComputeTermFrequencyStatisticsParameter.NOT_SPECIFIED_DESCRIPTORS) {
              ScopeNotesContain_Text.setEnabled(true);
              ScopeNotesContain_Text.setText(CastParameter.
              getDescriptorsScopeNotesContain());
            }
            else {
              ScopeNotesContain_Text.setEnabled(false);
              ScopeNotesContain_Text.setText("");
            }
          }
          else {
            MapTokensOntoDescriptors_CheckBox.setSelected(true);
            ThesaurusName_Text.setEnabled(true);
            ThesaurusName_Text.setText(DiasdemProject.getProperty(
            "DEFAULT_THESAURUS_FILE"));
            ThesaurusName_Button.setAllEnabled(true);
            VectorDimensions_Combo.setEnabled(true);
            if (DiasdemProject.getIntProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX")
            >= 0 && DiasdemProject.getIntProperty("DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX")
            < ComputeTermFrequencyStatisticsParameter.VECTOR_DIMENSIONS_OPTIONS.length) {
              VectorDimensions_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
              "DEFAULT_TEXT_UNIT_DESCRIPTORS_INDEX"));
            }
            else {
              VectorDimensions_Combo.setSelectedIndex(
              ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS);
            }
            if (VectorDimensions_Combo.getSelectedIndex()
            == ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS ||
            VectorDimensions_Combo.getSelectedIndex()
            == ComputeTermFrequencyStatisticsParameter.NOT_SPECIFIED_DESCRIPTORS) {
              ScopeNotesContain_Text.setEnabled(true);
              ScopeNotesContain_Text = new KTextField(DiasdemProject.getProperty(
              "DEFAULT_TEXT_UNIT_DESCRIPTORS_CONTAIN"), 30);
            }
            else {
              ScopeNotesContain_Text.setEnabled(false);
              ScopeNotesContain_Text.setText("");
            }
          }
          ThesaurusName_Text.setCaretAtEnding();
          ScopeNotesContain_Text.setCaretAtEnding();
          ThesaurusName_Text.requestFocus();
          Tabbed_Pane.setEnabledAt(2, true);
        }
        else {
          ThesaurusName_Text.setText("");
          ThesaurusName_Button.setAllEnabled(false);
          VectorDimensions_Combo.setEnabled(false);
          VectorDimensions_Combo.setSelectedIndex(
          ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS);
          ScopeNotesContain_Text.setEnabled(false);
          ScopeNotesContain_Text.setText("");
          Tabbed_Pane.setEnabledAt(2, false);
        }
        Tools.requestFocus(MapTokensOntoDescriptors_CheckBox);
      }
      
    } else if (ActionCommand.equals("ThesaurusNameButton")) {
      
      CurrentParameterDirectory = this.fileNameButtonClicked(
      ThesaurusName_Text, CurrentParameterDirectory, "PARAMETER_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Thesaurus File",
      DIAsDEMguiPreferences.THESAURUS_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("VectorDimensionsCombo")) {
      
      if ( VectorDimensions_Combo.getSelectedString().equals(
      ComputeTermFrequencyStatisticsParameter.VECTOR_DIMENSIONS_OPTIONS[
      ComputeTermFrequencyStatisticsParameter.ALL_DESCRIPTORS] ) ) {
        if (ScopeNotesContain_Text != null) {
          ScopeNotesContain_Text.setText("");
          ScopeNotesContain_Text.setEnabled(false);
        }
      }
      else {
        if (ScopeNotesContain_Text != null) {
          ScopeNotesContain_Text.setEnabled(true);
          ScopeNotesContain_Text.setText(DiasdemProject.getProperty(
          "DEFAULT_TEXT_UNIT_DESCRIPTORS_CONTAIN"));
          ScopeNotesContain_Text.setCaretAtEnding();
        }
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
    
    return "Compute Term Frequency Statistics";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY() 
    + DiasdemGuiPreferences.getDialogXxsSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    int conditionClusterIdOfTextUnit = Tools.string2Int(
    ConditionClusterIdOfTextUnit_Text.getText());
    if (ConditionClusterIdOfTextUnit_Text.getText().length() == 0) {
      conditionClusterIdOfTextUnit = ComputeTermFrequencyStatisticsParameter
      . UNSPECIFIED_INT_CONDITION;
    }
    int conditionIterationTextUnit_Text = Tools.string2Int(
    ConditionIterationTextUnit_Text.getText());
    if (ConditionIterationTextUnit_Text.getText().length() == 0) {
      conditionIterationTextUnit_Text = ComputeTermFrequencyStatisticsParameter
      . UNSPECIFIED_INT_CONDITION;
    }
    ComputeTermFrequencyStatisticsParameter parameter = 
    new ComputeTermFrequencyStatisticsParameter(Collection_Text.getText(), 
    TfStatistics_Text.getText(), false, false, false, false, false,
    ConditionTextUnitMatchedByRegex_Text.getText(), conditionClusterIdOfTextUnit, 
    ConditionClusterLabelOfTextUnit_Text.getText(), 
    conditionIterationTextUnit_Text, false,
    ThesaurusName_Text.getText(), VectorDimensions_Combo.getSelectedIndex(),
    ScopeNotesContain_Text.getText());
    
    if (ExportTextsInCsv_CheckBox.isSelected()) {
      parameter.setExportTextsInCsvFormat(true);
    }
    if (ExportStatisticsInCsv_CheckBox.isSelected()) {
      parameter.setExportStatisticsInCsvFormat(true);
    }
    if (ExportStatisticsInHtml_CheckBox.isSelected()) {
      parameter.setExportStatisticsInHtmlFormat(true);
    }
    if (ExcludeNumbersEtc_CheckBox.isSelected()) {
      parameter.setExcludeNumbersEtc(true);
    }
    if (ComputeConditionalFrequencies_CheckBox.isSelected()) {
      parameter.setComputeConditionalFrequencies(true);
    }
    if (MapTokensOntoDescriptors_CheckBox.isSelected()) {
      parameter.setMapTokensOntoDescriptors(true);
    }
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ComputeTermFrequencyStatisticsParameter) {
      CastParameter = (ComputeTermFrequencyStatisticsParameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_WORD_STATISTICS_FILE",
    TfStatistics_Text.getText());
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
    
    VectorDimensions_Combo = new KComboBox(ComputeTermFrequencyStatisticsParameter
    .VECTOR_DIMENSIONS_OPTIONS.length, true, "VectorDimensionsCombo", this);
    for (int i = 0; i < ComputeTermFrequencyStatisticsParameter.VECTOR_DIMENSIONS_OPTIONS
    .length; i++) {
      VectorDimensions_Combo.addItem(ComputeTermFrequencyStatisticsParameter
      .VECTOR_DIMENSIONS_OPTIONS[i], false);
    }

    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "CollectionFileName_Button", this,
    "Click this button to select the collection file.");
    
    TfStatistics_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    TfStatistics_Button.addSingleButton("...",
    KeyEvent.VK_S, true, true, "WordStatisticsFileName_Button", this,
    "Click this button to select the TF statistics file.");
    
    ExportTextsInCsv_CheckBox = new KCheckBox(
    "Export Original Texts in CSV Format", false, true,
    "ExportTextsInCsv", this, KeyEvent.VK_F, 
    "If this box is checked, all texts will be exported as a CSV file.");
    ExportStatisticsInCsv_CheckBox = new KCheckBox(
    "Export Term Frequency Statistics in CSV Format", false, true,
    "ExportStatisticsInCsv", this, KeyEvent.VK_E, 
    "If this box is checked, TF statistics will be exported as a CSV file.");
    ExportStatisticsInHtml_CheckBox = new KCheckBox(
    "Export Term Frequency Statistics in HTML Format", false, true,
    "ExportStatisticsInHtml", this, KeyEvent.VK_H, 
    "If this box is checked, TF statistics will be exported as an HTML file."); 
    ExcludeNumbersEtc_CheckBox = new KCheckBox(
    "Exclude Numbers, Dates, and NE Placeholders", true, true,
    "ExcludeNumbersEtc", this, KeyEvent.VK_N, 
    "If this box is checked, the TF statistics will include words only."); 
    ComputeConditionalFrequencies_CheckBox = new KCheckBox(
    "Compute Conditional Term Frequency Statistics", true, true,
    "ComputeConditionalFrequencies", this, KeyEvent.VK_T, 
    "If this box is checked, conditional TF statistics will be computed."); 
    MapTokensOntoDescriptors_CheckBox = new KCheckBox(
    "If Possible, Map Tokens onto Text Unit Descriptors", true, true,
    "MapTokensOntoDescriptors", this, KeyEvent.VK_M, 
    "If this box is checked, tokens will be mapped onto descriptors."); 
    
    ConditionTextUnitMatchedByRegex_Text = new KTextField(30);
    ConditionClusterIdOfTextUnit_Text = new KTextField(30);
    ConditionClusterLabelOfTextUnit_Text = new KTextField(30);
    ConditionIterationTextUnit_Text = new KTextField(30);
    ThesaurusName_Text = new KTextField(30);
    ScopeNotesContain_Text = new KTextField(30);

    ThesaurusName_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    ThesaurusName_Button.addSingleButton("...", 
      KeyEvent.VK_T, true, true, "ThesaurusNameButton", this,
    "Click this button to select the thesaurus file.");

    MapTokensOntoDescriptors_CheckBox.setSelected(false);
    ThesaurusName_Text.setText("");
    ThesaurusName_Button.setAllEnabled(false);
    VectorDimensions_Combo.setEnabled(false);
    VectorDimensions_Combo.setSelectedIndex(
    ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS);
    ScopeNotesContain_Text.setEnabled(false);
    ScopeNotesContain_Text.setText("");
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      TfStatistics_Text = new KTextField(CastParameter
      .getTfStatisticsFileName(), 30);
      if (CastParameter.getExportTextsInCsvFormat()) {
        ExportTextsInCsv_CheckBox.setSelected(true);
      }
      else {
        ExportTextsInCsv_CheckBox.setSelected(false);
      }
      if (CastParameter.getExportStatisticsInCsvFormat()) {
        ExportStatisticsInCsv_CheckBox.setSelected(true);
      }
      else {
        ExportStatisticsInCsv_CheckBox.setSelected(false);
      }
      if (CastParameter.getExportStatisticsInHtmlFormat()) {
        ExportStatisticsInHtml_CheckBox.setSelected(true);
      }
      else {
        ExportStatisticsInHtml_CheckBox.setSelected(false);
      }
      if (CastParameter.getExcludeNumbersEtc()) {
        ExcludeNumbersEtc_CheckBox.setSelected(true);
      }
      else {
        ExcludeNumbersEtc_CheckBox.setSelected(false);
      }
      if (CastParameter.getComputeConditionalFrequencies()) {
        ComputeConditionalFrequencies_CheckBox.setSelected(true);
        ConditionTextUnitMatchedByRegex_Text.setEnabled(true);
        ConditionTextUnitMatchedByRegex_Text.setText(CastParameter
        .getConditionTextUnitMatchedByRegex());
        ConditionClusterIdOfTextUnit_Text.setEnabled(true);
        ConditionClusterIdOfTextUnit_Text.setText(CastParameter
        .getConditionClusterIdOfTextUnit() ==
        ComputeTermFrequencyStatisticsParameter.UNSPECIFIED_INT_CONDITION ? ""
        : Tools.int2String(CastParameter.getConditionClusterIdOfTextUnit()));
        ConditionClusterLabelOfTextUnit_Text.setEnabled(true);
        ConditionClusterLabelOfTextUnit_Text.setText(CastParameter
        .getConditionClusterLabelOfTextUnit());
        ConditionIterationTextUnit_Text.setEnabled(true);
        ConditionIterationTextUnit_Text.setText(CastParameter
        .getConditionIterationOfTextUnit() ==
        ComputeTermFrequencyStatisticsParameter.UNSPECIFIED_INT_CONDITION ? ""
        : Tools.int2String(CastParameter.getConditionIterationOfTextUnit()));
      }
      else {
        ComputeConditionalFrequencies_CheckBox.setSelected(false);
        ConditionTextUnitMatchedByRegex_Text.setEnabled(false);
        ConditionTextUnitMatchedByRegex_Text.setText("");
        ConditionClusterIdOfTextUnit_Text.setEnabled(false);
        ConditionClusterIdOfTextUnit_Text.setText("");
        ConditionClusterLabelOfTextUnit_Text.setEnabled(false);
        ConditionClusterLabelOfTextUnit_Text.setText("");
        ConditionIterationTextUnit_Text.setEnabled(false);
        ConditionIterationTextUnit_Text.setText("");
      }
      if (CastParameter.mapTokensOntoDescriptors()) {
        MapTokensOntoDescriptors_CheckBox.setSelected(true);
        ThesaurusName_Text.setEnabled(true);
        ThesaurusName_Text.setText(CastParameter.getThesaurusFileName());
        ThesaurusName_Button.setAllEnabled(true);
        VectorDimensions_Combo.setEnabled(true);
        if (CastParameter.getVectorDimensions() >= 0
        && CastParameter.getVectorDimensions()
        < ComputeTermFrequencyStatisticsParameter.VECTOR_DIMENSIONS_OPTIONS.length) {
          VectorDimensions_Combo.setSelectedIndex(CastParameter
          .getVectorDimensions());
        }
        else {
          VectorDimensions_Combo.setSelectedIndex(
          ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS);
        }
        if (CastParameter.getVectorDimensions()
        == ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS ||
        CastParameter.getVectorDimensions()
        == ComputeTermFrequencyStatisticsParameter.NOT_SPECIFIED_DESCRIPTORS) {
          ScopeNotesContain_Text.setEnabled(true);
          ScopeNotesContain_Text.setText(CastParameter.
          getDescriptorsScopeNotesContain());
        }
        else {
          ScopeNotesContain_Text.setEnabled(false);
          ScopeNotesContain_Text.setText("");
        }
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      TfStatistics_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_WORD_STATISTICS_FILE"), 30);
      ComputeConditionalFrequencies_CheckBox.setSelected(false);
      ConditionTextUnitMatchedByRegex_Text.setEnabled(false);
      ConditionTextUnitMatchedByRegex_Text.setText("");
      ConditionClusterIdOfTextUnit_Text.setEnabled(false);
      ConditionClusterIdOfTextUnit_Text.setText("");
      ConditionClusterLabelOfTextUnit_Text.setEnabled(false);
      ConditionClusterLabelOfTextUnit_Text.setText("");
      ConditionIterationTextUnit_Text.setEnabled(false);
      ConditionIterationTextUnit_Text.setText("");
    }    
    Collection_Text.setCaretAtEnding();
    TfStatistics_Text.setCaretAtEnding();
    ConditionTextUnitMatchedByRegex_Text.setCaretAtEnding();
    ConditionClusterIdOfTextUnit_Text.setCaretAtEnding();
    ConditionClusterLabelOfTextUnit_Text.setCaretAtEnding();
    ConditionIterationTextUnit_Text.setCaretAtEnding();
    ThesaurusName_Text.setCaretAtEnding();
    ScopeNotesContain_Text.setCaretAtEnding();
        
    Parameter_Panel = new KGridBagPanel(12, 12, 11, 11);
    Parameter_Panel.startFocusForwarding(Collection_Text);
    
    Parameter_Panel.addLabel("Collection File:", 0, 0, KeyEvent.VK_C,
    Collection_Button.getDefaultButton(), true,
    "Task input: This collection file contains references " +
    "to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("TF Statistics File:", 0, 2, KeyEvent.VK_S,
    TfStatistics_Button.getDefaultButton(), true,
    "Task output: This file will contain collection-specific "
    + "term frequency statistics.");
    Parameter_Panel.addComponent(TfStatistics_Text, 2, 2);
    Parameter_Panel.addBlankColumn(3, 2, 12);
    Parameter_Panel.addKButtonPanel(TfStatistics_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Advanced Options:", 0, 4);
    Parameter_Panel.addComponent(ExportTextsInCsv_CheckBox, 2, 4,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 5);
    Parameter_Panel.addComponent(ExportStatisticsInCsv_CheckBox, 2, 5,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 6);
    Parameter_Panel.addComponent(ExportStatisticsInHtml_CheckBox, 2, 6,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 7);
    Parameter_Panel.addComponent(ExcludeNumbersEtc_CheckBox, 2, 7,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 8);
    Parameter_Panel.addComponent(ComputeConditionalFrequencies_CheckBox, 2, 8,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addLabel("", 0, 9);
    Parameter_Panel.addComponent(MapTokensOntoDescriptors_CheckBox, 2, 9,
    new Insets(0, 0, 0, 0), 3, 1);
    
    Condition_Panel = new KGridBagPanel(12, 12, 11, 11);
    Condition_Panel.startFocusForwarding(ConditionTextUnitMatchedByRegex_Text);
    
    Condition_Panel.addLabel("1. Text Unit is Matched by Regex:", 0, 0, 
    KeyEvent.VK_1, ConditionTextUnitMatchedByRegex_Text, true,
    "Optional task input: Specify the regular expression that must match "
    + "valid processed text units.");
    Condition_Panel.addBlankColumn(1, 0, 12);
    Condition_Panel.addComponent(ConditionTextUnitMatchedByRegex_Text, 2, 0,
    new Insets(0, 0, 0, 0), 1, 1);
    Condition_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Condition_Panel.addLabel("2. Cluster ID of Text Unit Equals:", 0, 2, 
    KeyEvent.VK_2, ConditionClusterIdOfTextUnit_Text, true,
    "Optional task input: Specify the cluster ID of text units to be "
    + "considered.");
    Condition_Panel.addComponent(ConditionClusterIdOfTextUnit_Text, 2, 2,
    new Insets(0, 0, 0, 0), 1, 1);
    Condition_Panel.addBlankRow(0, 3, 11);
    Condition_Panel.addLabel("3. Cluster Label of Text Unit Equals:", 0, 4, 
    KeyEvent.VK_3, ConditionClusterLabelOfTextUnit_Text, true,
    "Optional task input: Specify the cluster label of text units to be "
    + "considered.");
    Condition_Panel.addComponent(ConditionClusterLabelOfTextUnit_Text, 2, 4,
    new Insets(0, 0, 0, 0), 1, 1);
    Condition_Panel.addBlankRow(0, 5, 11);
    Condition_Panel.addLabel("4. Iteration of Text Unit Equals:", 0, 6, 
    KeyEvent.VK_4, ConditionIterationTextUnit_Text, true,
    "Optional task input: Specify the clustering iteration of text units to be "
    + "considered.");
    Condition_Panel.addComponent(ConditionIterationTextUnit_Text, 2, 6,
    new Insets(0, 0, 0, 0), 1, 1);

    Thesaurus_Panel = new KGridBagPanel(12, 12, 11, 11);
    Thesaurus_Panel.startFocusForwarding(ThesaurusName_Text);
    
    Thesaurus_Panel.addLabel("Thesaurus File:", 0, 0, KeyEvent.VK_T,
      ThesaurusName_Button.getDefaultButton(), true,
    "Task input: This DIAsDEM-specific thesaurus file contains " +
    "text unit descriptors.");
    Thesaurus_Panel.addBlankColumn(1, 0, 12);
    Thesaurus_Panel.addComponent(ThesaurusName_Text, 2, 0);
    Thesaurus_Panel.addBlankColumn(3, 0, 12);
    Thesaurus_Panel.addKButtonPanel(ThesaurusName_Button, 4, 0);
    Thesaurus_Panel.addBlankRow(0, 1, 11);
    Thesaurus_Panel.addLabel("Text Unit Descriptors:", 0, 2, KeyEvent.VK_U,
    VectorDimensions_Combo, true, "Task input: Only matching thesaurus "
    + "descriptors will correspond to vector dimensions.");
    Thesaurus_Panel.addComponent(VectorDimensions_Combo, 
      2, 2, new Insets(0, 0, 0, 0), 3, 1);
    Thesaurus_Panel.addBlankRow(0, 3, 11);
    Thesaurus_Panel.addLabel("", 0, 4);
    Thesaurus_Panel.addComponent(ScopeNotesContain_Text, 2, 4,
      new Insets(0, 0, 0, 0), 3, 1);
    
    KBorderPanel ParameterNorth_Panel = new KBorderPanel(0, 0, 0, 0);
    ParameterNorth_Panel.startFocusForwarding(Parameter_Panel);
    ParameterNorth_Panel.addNorth(Parameter_Panel);
    
    ConditionNorth_Panel = new KBorderPanel(0, 0, 0, 0);
    ConditionNorth_Panel.startFocusForwarding(Condition_Panel);
    ConditionNorth_Panel.addNorth(Condition_Panel);
    
    ThesaurusNorth_Panel = new KBorderPanel(0, 0, 0, 0);
    ThesaurusNorth_Panel.startFocusForwarding(Thesaurus_Panel);
    ThesaurusNorth_Panel.addNorth(Thesaurus_Panel);
    
    Tabbed_Pane = new KTabbedPane();
    Tabbed_Pane.addTab("Settings", ParameterNorth_Panel, KeyEvent.VK_I, 
    true, true);
    Tabbed_Pane.addTab("Conditions", ConditionNorth_Panel, KeyEvent.VK_D, 
    (ComputeConditionalFrequencies_CheckBox.isSelected() ? true : false), 
    false);
    Tabbed_Pane.addTab("Thesaurus", ThesaurusNorth_Panel, KeyEvent.VK_R, 
    (MapTokensOntoDescriptors_CheckBox.isSelected() ? true : false), 
    false);
    Tabbed_Pane.startFocusForwardingToSelectedTab();
    
    this.removeAll();
    this.validate();
    this.addCenter(Tabbed_Pane);
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