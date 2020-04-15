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

package org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus;

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

public class EstablishInitialThesaurusParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private EstablishInitialThesaurusParameter CastParameter = null;
  
  private KTextField CollectionStatistics_Text = null;
  private KButtonPanel CollectionStatistics_Button = null;
  private KTextField TfStatistics_Text = null;
  private KButtonPanel TfStatistics_Button = null;
  private KTextField MinTermFrequency_Text = null;
  private KTextField MaxTermFrequency_Text = null;
   
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
  
  public EstablishInitialThesaurusParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public EstablishInitialThesaurusParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if (ActionCommand.equals("CollectionStatisticsButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      CollectionStatistics_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing TF Statistics File",
      DIAsDEMguiPreferences.TF_STATISTICS_FILE_FILTER, false, true);
     
    } else if (ActionCommand.equals("ThesaurusNameButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      TfStatistics_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Initial Thesaurus File to be Created",
      DIAsDEMguiPreferences.THESAURUS_FILE_FILTER, false, true);
     
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
    
    return "Establish Initial Thesaurus";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    EstablishInitialThesaurusParameter parameter = new EstablishInitialThesaurusParameter(
    CollectionStatistics_Text.getText(),
    TfStatistics_Text.getText(),
    Tools.string2Int(MinTermFrequency_Text.getText().trim()),
    Tools.string2Int(MaxTermFrequency_Text.getText().trim()) );
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof EstablishInitialThesaurusParameter) {
      CastParameter = (EstablishInitialThesaurusParameter)pTaskParameter;
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
    
    DiasdemProject.setProperty("DEFAULT_WORD_STATISTICS_FILE",
    CollectionStatistics_Text.getText());
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (CollectionStatistics_Text != null) {
      return CollectionStatistics_Text;
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
      CollectionStatistics_Text = new KTextField(CastParameter
      .getCollectionStatisticsFileName(), 30);
      TfStatistics_Text = new KTextField(CastParameter
      .getThesaurusFileName(), 30);
      MinTermFrequency_Text = new KTextField(CastParameter
      .getMinTermFrequency() + "", 30);
      MaxTermFrequency_Text = new KTextField(CastParameter
      .getMaxTermFrequency() + "", 30);
    }
    else {
      CollectionStatistics_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_WORD_STATISTICS_FILE"), 30 );
      TfStatistics_Text = new KTextField("", 30);
      MinTermFrequency_Text = new KTextField("10", 30);
      MaxTermFrequency_Text = new KTextField("100000", 30);
    }
    CollectionStatistics_Text.setCaretAtEnding();
    
    CollectionStatistics_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    CollectionStatistics_Button.addSingleButton("...", 
      KeyEvent.VK_S, true, true, "CollectionStatisticsButton", this,
    "Click this button to select the TF statistics file.");
    
    TfStatistics_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    TfStatistics_Button.addSingleButton("...", 
      KeyEvent.VK_F, true, true, "ThesaurusNameButton", this,
    "Click this button to select the thesaurus file.");  

    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(CollectionStatistics_Text);

    Parameter_Panel.addLabel("TF Statistics File:", 0, 0, KeyEvent.VK_S,
      CollectionStatistics_Button.getDefaultButton(), true,
    "Task input: This file contains collection-specific " +
    "term frequency statistics.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(CollectionStatistics_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(CollectionStatistics_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Initial Thesaurus File:", 0, 2, KeyEvent.VK_F,
      TfStatistics_Button.getDefaultButton(), true,
    "Task output: This file will contain the initial, DIAsDEM-specific " +
    "thesaurus.");
    Parameter_Panel.addComponent(TfStatistics_Text, 2, 2);
    Parameter_Panel.addKButtonPanel(TfStatistics_Button, 4, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Min. Term Frequency:", 0, 4, KeyEvent.VK_I,
      MinTermFrequency_Text, true,
    "Task input: Enter an appropriate parameter value.");
    Parameter_Panel.addComponent(MinTermFrequency_Text, 2, 4,
      new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("Max. Term Frequency:", 0, 6, KeyEvent.VK_A,
      MaxTermFrequency_Text, true,
    "Task input: Enter an appropriate parameter value.");
    Parameter_Panel.addComponent(MaxTermFrequency_Text, 2, 6,
      new Insets(0, 0, 0, 0), 3, 1);
    
    this.removeAll();
    this.validate();
    this.addNorth(Parameter_Panel);
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