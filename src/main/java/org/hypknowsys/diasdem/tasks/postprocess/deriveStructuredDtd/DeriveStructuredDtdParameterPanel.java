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

package org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd;

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
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DeriveStructuredDtdParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DeriveStructuredDtdParameter CastParameter = null;
  
  private KGridBagPanel Parameter2_Panel = null;
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField UnstructuredDtdFile_Text = null;
  private KButtonPanel UnstructuredDtdFile_Button = null;
  private KTextField StructuredDtdFile_Text = null;
  private KButtonPanel StructuredDtdFile_Button = null;
  private KTextField WumAssocRulesFile_Text = null;
  private KButtonPanel WumAssocRulesFile_Button = null;
  private KTextField WumSequencesFile_Text = null;
  private KButtonPanel WumSequencesFile_Button = null;
  private KTextField MinTagRelSupport_Text = null;
  private KTextField MinAssocRelSupport_Text = null;
  private KTextField MinAssocConfidence_Text = null;
  private KTextField MinAssocLift_Text = null;
  private KTextField MinSeqRelSupport_Text = null;
  private KTextField MinSeqConfidence_Text = null;
  private KTextField MinSeqLift_Text = null;

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
  
  public DeriveStructuredDtdParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DeriveStructuredDtdParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if  (ActionCommand.equals("CollectionButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      Collection_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Collection File",
      DIAsDEMguiPreferences.COLLECTION_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("UnstructuredDtdFileButton") ) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      UnstructuredDtdFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Unstructured DTD File",
      DIAsDEMguiPreferences.PRELIMINARY_DTD_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("StructuredDtdFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      StructuredDtdFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, 
      "Select Structured DTD File to be Created");
      
    } else if (ActionCommand.equals("WumAssocRulesFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      WumAssocRulesFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, 
      "Select Existing WUM Association Rules File");
      
    } else if (ActionCommand.equals("WumSequencesFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      WumSequencesFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, 
      "Select Existing WUM Sequences File");
      
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
    
    return "Derive Structured DTD";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    DeriveStructuredDtdParameter parameter = new DeriveStructuredDtdParameter(
    Collection_Text.getText(),
    UnstructuredDtdFile_Text.getText(),
    StructuredDtdFile_Text.getText(),
    WumAssocRulesFile_Text.getText(),
    WumSequencesFile_Text.getText(),
    Tools.string2Double(MinTagRelSupport_Text.getText()),
    Tools.string2Double(MinAssocRelSupport_Text.getText()),
    Tools.string2Double(MinAssocConfidence_Text.getText()),
    Tools.string2Double(MinAssocLift_Text.getText()),
    Tools.string2Double(MinSeqRelSupport_Text.getText()),
    Tools.string2Double(MinSeqConfidence_Text.getText()),
    Tools.string2Double(MinSeqLift_Text.getText()) );
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof DeriveStructuredDtdParameter) {
      CastParameter = (DeriveStructuredDtdParameter)pTaskParameter;
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
      UnstructuredDtdFile_Text = new KTextField(CastParameter
      .getUnstructuredDtdFileName(), 30);
      StructuredDtdFile_Text = new KTextField(CastParameter
      .getStructuredDtdFileName(), 30);
      WumAssocRulesFile_Text = new KTextField(CastParameter
      .getWumAssocRulesFileName(), 30);
      WumSequencesFile_Text = new KTextField(CastParameter
      .getWumSequencesFileName(), 30);
      MinTagRelSupport_Text = new KTextField(CastParameter
      .getMinTagRelSupport() + "", 30);
      MinAssocRelSupport_Text = new KTextField(CastParameter
      .getMinAssocRelSupport() + "", 30);
      MinAssocConfidence_Text = new KTextField(CastParameter
      .getMinAssocConfidence() + "", 30);
      MinAssocLift_Text = new KTextField(CastParameter
      .getMinAssocLift() + "", 30);
      MinSeqRelSupport_Text = new KTextField(CastParameter
      .getMinSeqRelSupport() + "", 30);
      MinSeqConfidence_Text = new KTextField(CastParameter
      .getMinSeqConfidence() + "", 30);
      MinSeqLift_Text = new KTextField(CastParameter
      .getMinSeqLift() + "", 30);
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"));
      UnstructuredDtdFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_UNSTRUCTURED_DTD_FILE"), 30);
      StructuredDtdFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_STRUCTURED_DTD_FILE"), 30);
      WumAssocRulesFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_WUM_ASSOC_RULES_FILE"), 30);
      WumSequencesFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_WUM_SEQUENCES_FILE"), 30);
      MinTagRelSupport_Text = new KTextField("0.0");
      MinAssocRelSupport_Text = new KTextField("0.0");
      MinAssocConfidence_Text = new KTextField("0.0");
      MinAssocLift_Text = new KTextField("0.0");
      MinSeqRelSupport_Text = new KTextField("0.0");
      MinSeqConfidence_Text = new KTextField("0.0");
      MinSeqLift_Text = new KTextField("0.0");
    }
    Collection_Text.setCaretAtEnding();    
    UnstructuredDtdFile_Text.setCaretAtEnding();    
    StructuredDtdFile_Text.setCaretAtEnding();    
    WumAssocRulesFile_Text.setCaretAtEnding();    
    WumSequencesFile_Text.setCaretAtEnding();    
    
    Collection_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Collection_Button.addSingleButton("...", 
      0, true, false, "CollectionButton", this,
    "Click this button to select the collection file.");    
    UnstructuredDtdFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    UnstructuredDtdFile_Button.addSingleButton("...", 
      0, true, false, "UnstructuredDtdFileButton", this,
    "Click this button to select the unstructured DTD file.");    
    StructuredDtdFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    StructuredDtdFile_Button.addSingleButton("...", 
      0, true, false, "StructuredDtdFileButton", this,
    "Click this button to select the structured DTD file.");
    WumAssocRulesFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    WumAssocRulesFile_Button.addSingleButton("...", 
      0, true, false, "WumAssocRulesFileButton", this,
    "Click this button to select the WUM assoc rules file.");   
    WumSequencesFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    WumSequencesFile_Button.addSingleButton("...", 
      0, true, false, "WumSequencesFileButton", this,
    "Click this button to select the WUM sequences file.");    

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
    Parameter_Panel.addBlankRow(0, 1, 11);
    Parameter_Panel.addLabel("Unstructured DTD File:", 0, 2, KeyEvent.VK_U,
      UnstructuredDtdFile_Button.getDefaultButton(), true,
    "Task input: This file contains meta-data about the preliminary" +
    "XML DTD");
    Parameter_Panel.addComponent(UnstructuredDtdFile_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(UnstructuredDtdFile_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Structured DTD File:", 0, 4, KeyEvent.VK_S,
      StructuredDtdFile_Button.getDefaultButton(),  true,
    "Task output: This file contains meta-data about the structured" +
    "XML DTD");
    Parameter_Panel.addComponent(StructuredDtdFile_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(StructuredDtdFile_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("WUM Assoc. Rules File:", 0, 6, KeyEvent.VK_A,
      WumAssocRulesFile_Button.getDefaultButton(),  true,
    "Task input: This file contains assoc rules discovered by WUM.");
    Parameter_Panel.addComponent(WumAssocRulesFile_Text, 2, 6);
    Parameter_Panel.addKButtonPanel(WumAssocRulesFile_Button, 4, 6);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("WUM Sequences File:", 0, 8, KeyEvent.VK_W,
      WumSequencesFile_Button.getDefaultButton(),  true,
    "Task input: This file contains sequences discovered by WUM.");
    Parameter_Panel.addComponent(WumSequencesFile_Text, 2, 8);
    Parameter_Panel.addKButtonPanel(WumSequencesFile_Button, 4, 8);

    Parameter2_Panel = new KGridBagPanel(12, 12, 11, 11);
    Parameter2_Panel.startFocusForwarding(MinTagRelSupport_Text);

    Parameter2_Panel.addLabel("Min. Tag Rel. Support:", 0, 0);
    Parameter2_Panel.addBlankColumn(1, 0, 12);
    Parameter2_Panel.addComponent(MinTagRelSupport_Text, 2, 0);
    Parameter2_Panel.addBlankRow(0, 1, 11);
    Parameter2_Panel.addLabel("Min. Assoc. Rule Rel. Support:", 0, 2);
    Parameter2_Panel.addComponent(MinAssocRelSupport_Text, 2, 2);
    Parameter2_Panel.addBlankRow(0, 3, 11);
    Parameter2_Panel.addLabel("Min. Assoc. Rule Confidence:", 0, 4);
    Parameter2_Panel.addComponent(MinAssocConfidence_Text, 2, 4);
    Parameter2_Panel.addBlankRow(0, 5, 11);
    Parameter2_Panel.addLabel("Min. Assoc. Rule Lift:", 0, 6);
    Parameter2_Panel.addComponent(MinAssocLift_Text, 2, 6);
    Parameter2_Panel.addBlankRow(0, 7, 11);
    Parameter2_Panel.addLabel("Min. Sequence Rel. Support:", 0, 8);
    Parameter2_Panel.addComponent(MinSeqRelSupport_Text, 2, 8);
    Parameter2_Panel.addBlankRow(0, 9, 11);
    Parameter2_Panel.addLabel("Min. Sequence Confidence:", 0, 10);
    Parameter2_Panel.addComponent(MinSeqConfidence_Text, 2, 10);
    Parameter2_Panel.addBlankRow(0, 11, 11);
    Parameter2_Panel.addLabel("Min. Sequence Lift:", 0, 12);
    Parameter2_Panel.addComponent(MinSeqLift_Text, 2, 12);
 
    KBorderPanel ParameterNorth_Panel = new KBorderPanel(0, 0, 0, 0);
    ParameterNorth_Panel.startFocusForwarding(Parameter_Panel);
    ParameterNorth_Panel.addNorth(Parameter_Panel);
    
    KBorderPanel Parameter2North_Panel = new KBorderPanel(0, 0, 0, 0);
    Parameter2North_Panel.startFocusForwarding(Parameter2_Panel);
    Parameter2North_Panel.addNorth(Parameter2_Panel);
    
    KTabbedPane Tabbed_Pane = new KTabbedPane();
    Tabbed_Pane.addTab("Files", ParameterNorth_Panel, KeyEvent.VK_F, 
    true, true);
    Tabbed_Pane.addTab("Thresholds", Parameter2North_Panel, KeyEvent.VK_T, 
    true, false);
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