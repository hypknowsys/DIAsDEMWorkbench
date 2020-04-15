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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KComboBox;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KLabel;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public class DrawDocumentSampleParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DrawDocumentSampleParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField DtdFile_Text = null;
  private KButtonPanel DtdFile_Button = null;
  private KTextField RandomSampleFile_Text = null;
  private KButtonPanel RandomSampleFile_Button = null;
  private KLabel RandomSampleSize_Label = null;
  private KTextField RandomSampleSize_Text = null;
  private KComboBox SamplingMode_Combo = null;
  private boolean SamplingMode_FirstAction = true;
  private KTextField HtmlDirectory_Text = null;
  private KButtonPanel HtmlDirectory_Button = null;
  
  private String PreviousRandomSampleSize = null;
  
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
  
  public DrawDocumentSampleParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DrawDocumentSampleParameterPanel(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences) {
    
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
  
  public void actionPerformed(ActionEvent pActionEvent) {
    
    ActionCommand = pActionEvent.getActionCommand();
    ActionSource = pActionEvent.getSource();
    
    if (ActionCommand.equals("CollectionButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("DtdFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      DtdFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Existing Conceptual DTD File",
      DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("RandomSampleFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      RandomSampleFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY",
      "Select", KeyEvent.VK_S, null, "Select Random Sample File to be Created",
      DIAsDEMguiPreferences.DOCUMENT_SAMPLE_FILE_FILTER, false, true);
      
    }
    else if (ActionCommand.equals("HtmlDirectoryButton")) {
      
      CurrentProjectDirectory = this.directoryButtonClicked(HtmlDirectory_Text,
      CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select", KeyEvent.VK_S,
      null, "Select Existing Directory of HTML Sample Files");
      
    }
    else if (ActionCommand.equals("SamplingModeCombo")) {
      
      if (SamplingMode_FirstAction) {
        SamplingMode_FirstAction = false;
      }
      else {
        this.setComponentStatus();
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
    
    return "Draw Document Sample 2.2";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogMSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    DrawDocumentSampleParameter parameter = new DrawDocumentSampleParameter(
    Collection_Text.getText(),
    DtdFile_Text.getText(),
    Tools.string2Double(RandomSampleSize_Text.getText()),
    RandomSampleFile_Text.getText(),
    HtmlDirectory_Text.getText(),
    SamplingMode_Combo.getSelectedString());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof DrawDocumentSampleParameter) {
      CastParameter = (DrawDocumentSampleParameter)pTaskParameter;
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
    if (DtdFile_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_CONCEPTUAL_DTD_FILE",
      DtdFile_Text.getText());
    }
    DiasdemProject.setProperty("DEFAULT_DOCUMENT_SAMPLE_FILE",
    RandomSampleFile_Text.getText());
    if (RandomSampleSize_Text.getText().length() > 0) {
      DiasdemProject.setProperty("DEFAULT_DOCUMENT_SAMPLE_SIZE",
      RandomSampleSize_Text.getText());
    }
    DiasdemProject.setProperty("DEFAULT_CREATE_HTML_SAMPLE_DIRECTORY",
    HtmlDirectory_Text.getText());
    DiasdemProject.setProperty("DEFAULT_SAMPLING_MODE_INDEX",
    String.valueOf(SamplingMode_Combo.getSelectedIndex()));
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
    
    SamplingMode_Combo = new KComboBox(DrawDocumentSampleParameter
    .SAMPLING_MODES.length, true, "SamplingModeCombo", this);
    for (int i = 0; i < DrawDocumentSampleParameter.SAMPLING_MODES
    .length; i++) {
      SamplingMode_Combo.addItem(DrawDocumentSampleParameter
      .SAMPLING_MODES[i], false);
    }
    
    if (CastParameter != null) {
      Collection_Text = new KTextField(CastParameter
      .getCollectionFileName(), 30);
      DtdFile_Text = new KTextField(CastParameter
      .getDtdFileName(), 30);
      RandomSampleFile_Text = new KTextField(CastParameter
      .getRandomSampleFileName(), 30);
      RandomSampleSize_Text = new KTextField(CastParameter
      .getRandomSampleSize() + "", 30);
      HtmlDirectory_Text = new KTextField(CastParameter
      .getHtmlDirectory(), 30);
      if (CastParameter.getSamplingMode() >= 0
      && CastParameter.getSamplingMode()
      < DrawDocumentSampleParameter.SAMPLING_MODES.length) {
        SamplingMode_Combo.setSelectedIndex(CastParameter.getSamplingMode());
      }
      else {
        SamplingMode_Combo.setSelectedIndex(DrawDocumentSampleParameter
        .CREATE_RANDOM_SAMPLE_FILE);
      }
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      DtdFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CONCEPTUAL_DTD_FILE"), 30);
      RandomSampleFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_DOCUMENT_SAMPLE_FILE"), 30);
      RandomSampleSize_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_DOCUMENT_SAMPLE_SIZE"), 30);
      HtmlDirectory_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CREATE_HTML_SAMPLE_DIRECTORY"), 30);
      if (DiasdemProject.getIntProperty("DEFAULT_SAMPLING_MODE_INDEX")
      >= 0 && DiasdemProject.getIntProperty("DEFAULT_SAMPLING_MODE_INDEX")
      < DrawDocumentSampleParameter.SAMPLING_MODES.length) {
        SamplingMode_Combo.setSelectedIndex(DiasdemProject.getIntProperty(
        "DEFAULT_SAMPLING_MODE_INDEX"));
      }
      else {
        SamplingMode_Combo.setSelectedIndex(DrawDocumentSampleParameter
        .CREATE_RANDOM_SAMPLE_FILE);
      }
    }
    Collection_Text.setCaretAtEnding();
    DtdFile_Text.setCaretAtEnding();
    RandomSampleFile_Text.setCaretAtEnding();
    RandomSampleSize_Text.setCaretAtEnding();
    HtmlDirectory_Text.setCaretAtEnding();
    PreviousRandomSampleSize = RandomSampleSize_Text.getText();
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "CollectionButton", this,
    "Click this button to select the collection file.");
    
    DtdFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    DtdFile_Button.addSingleButton("...",
    KeyEvent.VK_P, true, true, "DtdFileButton", this,
    "Click this button to select the conceptual DTD file.");
    
    RandomSampleFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    RandomSampleFile_Button.addSingleButton("...",
    KeyEvent.VK_F, true, true, "RandomSampleFileButton", this,
    "Click this button to select the random sample file.");
    
    HtmlDirectory_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    HtmlDirectory_Button.addSingleButton("...",
    KeyEvent.VK_D, true, true, "HtmlDirectoryButton", this,
    "Click this button to select the directory of HTML sample files.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(Collection_Text);
    
    Parameter_Panel.addLabel("Collection File:", 0, 0, KeyEvent.VK_C,
    Collection_Button.getDefaultButton(), true,
    "Task input: This collection file contains references "
    + "to all DIAsDEM documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(Collection_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(Collection_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Sampling Mode:", 0, 2, KeyEvent.VK_S,
    SamplingMode_Combo, true,
    "Task input: Select an appropriate sampling mode.");
    Parameter_Panel.addComponent(SamplingMode_Combo, 2, 2,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Random Sample File:", 0, 4, KeyEvent.VK_F,
    RandomSampleFile_Button.getDefaultButton(), true,
    "Task output: This file will contain a random sample of text units.");
    Parameter_Panel.addComponent(RandomSampleFile_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(RandomSampleFile_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    RandomSampleSize_Label = Parameter_Panel.addLabel("Random Sample Size:",
    0, 6, KeyEvent.VK_R, RandomSampleSize_Text, true,
    "Task input: Choose an appropriate parameter value in [0.0; 1.0].");
    Parameter_Panel.addComponent(RandomSampleSize_Text, 2, 6,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Conceptual DTD File:", 0, 8, KeyEvent.VK_P,
    DtdFile_Button.getDefaultButton(), true, "Optional task input: "
    + "This file contains meta-data about the conceptual XML DTD");
    Parameter_Panel.addComponent(DtdFile_Text, 2, 8);
    Parameter_Panel.addKButtonPanel(DtdFile_Button, 4, 8);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("Sample Directory:", 0, 10, KeyEvent.VK_D,
    HtmlDirectory_Button.getDefaultButton(), true,
    "Task input: This directory will contain files of the document sample");
    Parameter_Panel.addComponent(HtmlDirectory_Text, 2, 10,
    new Insets(0, 0, 0, 0), 1, 1);
    Parameter_Panel.addKButtonPanel(HtmlDirectory_Button, 4, 10);
    
    this.removeAll();
    this.validate();
    this.addNorth(Parameter_Panel);
    this.validate();
    this.setComponentStatus();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {
    
    if (SamplingMode_Combo != null && SamplingMode_Combo.getSelectedIndex()
    == DrawDocumentSampleParameter.CREATE_RANDOM_SAMPLE_FILE) {
      if (PreviousRandomSampleSize != null
      && PreviousRandomSampleSize.length() > 0) {
        RandomSampleSize_Text.setText(PreviousRandomSampleSize);
      }
      if (RandomSampleSize_Label != null) {
        RandomSampleSize_Label.setEnabled(true);
      }
      if (RandomSampleSize_Text != null) {
        RandomSampleSize_Text.setEnabled(true);
      }
    }
    else if (SamplingMode_Combo != null && SamplingMode_Combo.getSelectedIndex()
    == DrawDocumentSampleParameter.APPLY_RANDOM_SAMPLE_FILE) {
      if (RandomSampleSize_Label != null) {
        RandomSampleSize_Label.setEnabled(false);
      }
      if (RandomSampleSize_Text != null) {
        if (RandomSampleSize_Text.getText().length() > 0)  {
          PreviousRandomSampleSize = RandomSampleSize_Text.getText();
          RandomSampleSize_Text.setText("");
        }
        RandomSampleSize_Text.setEnabled(false);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}