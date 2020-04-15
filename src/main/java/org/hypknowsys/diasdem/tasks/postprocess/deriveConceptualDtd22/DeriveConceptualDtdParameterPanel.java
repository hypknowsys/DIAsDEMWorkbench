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

package org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22;

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
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public class DeriveConceptualDtdParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DeriveConceptualDtdParameter CastParameter = null;
  
  private KTextField Collection_Text = null;
  private KButtonPanel Collection_Button = null;
  private KTextField DtdFile_Text = null;
  private KButtonPanel DtdFile_Button = null;
  private KTextField DtdRootElement_Text = null;
  private KTextField MinAttributeRelSupport_Text = null;
  private KTextField DtdDocumentationFile_Text = null;
  private KButtonPanel DtdDocumentationFile_Button = null;

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
  
  public DeriveConceptualDtdParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DeriveConceptualDtdParameterPanel(Server pDiasdemServer,
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
      
    } else if (ActionCommand.equals("DtdFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      DtdFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", "Select",
      KeyEvent.VK_S, null, "Select Conceptual DTD File to be Created",
      DIAsDEMguiPreferences.CONCEPTUAL_DTD_FILE_FILTER, false, true);
      
    } else if (ActionCommand.equals("DtdDocumentationFileButton")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      DtdDocumentationFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, 
      "Select DTD Documentation File to be Created",
      DIAsDEMguiPreferences.HTML_FILE_FILTER, false, true);
      
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
    
    return "Derive Conceptual DTD 2.2";
    
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
    
    DeriveConceptualDtdParameter parameter = new DeriveConceptualDtdParameter(
    Collection_Text.getText(),
    DtdFile_Text.getText(),
    DtdRootElement_Text.getText(),
    Tools.string2Double(MinAttributeRelSupport_Text.getText()),
    DtdDocumentationFile_Text.getText());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof DeriveConceptualDtdParameter) {
      CastParameter = (DeriveConceptualDtdParameter)pTaskParameter;
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
    DiasdemProject.setProperty("DEFAULT_CONCEPTUAL_DTD_FILE",
    DtdFile_Text.getText());
    DiasdemProject.setProperty("DEFAULT_DTD_ROOT_ELEMENT",
    DtdRootElement_Text.getText());
    DiasdemProject.setProperty("DEFAULT_MIN_ATTRIBUTE_SUPPORT",
    MinAttributeRelSupport_Text.getText());
    DiasdemProject.setProperty("DEFAULT_DTD_DOCUMENTATION_FILE",
    DtdDocumentationFile_Text.getText());
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
      DtdFile_Text = new KTextField(CastParameter
      .getDtdFileName(), 30);
      DtdRootElement_Text = new KTextField(CastParameter
      .getDtdRootElement(), 30);
      DtdDocumentationFile_Text = new KTextField(CastParameter
      .getDtdDocumentationFileName(), 30);
    }
    else {
      Collection_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_COLLECTION_FILE"), 30);
      DtdFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_CONCEPTUAL_DTD_FILE"), 30);
      DtdRootElement_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_DTD_ROOT_ELEMENT"), 30);
      MinAttributeRelSupport_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_MIN_ATTRIBUTE_SUPPORT"), 30);
      DtdDocumentationFile_Text = new KTextField(DiasdemProject.getProperty(
      "DEFAULT_DTD_DOCUMENTATION_FILE"), 30);
    }
    Collection_Text.setCaretAtEnding();    
    DtdFile_Text.setCaretAtEnding();  
    DtdRootElement_Text.setCaretAtEnding();  
    MinAttributeRelSupport_Text.setCaretAtEnding();  
    DtdDocumentationFile_Text.setCaretAtEnding();  
    
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

    DtdDocumentationFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
    KButtonPanel.HORIZONTAL_RIGHT);
    DtdDocumentationFile_Button.addSingleButton("...", 
    KeyEvent.VK_D, true, true, "DtdDocumentationFileButton", this,
    "Click this button to select the DTD documentation file.");    

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
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Conceptual DTD File:", 0, 4, KeyEvent.VK_P,
    DtdFile_Button.getDefaultButton(), true, "Task output: This file will "
    + "contain meta-data about the conceptual XML DTD");
    Parameter_Panel.addComponent(DtdFile_Text, 2, 4);
    Parameter_Panel.addKButtonPanel(DtdFile_Button, 4, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.addLabel("DTD Root Element:", 0, 6, KeyEvent.VK_R,
    DtdRootElement_Text, true, "Task input: Enter an appropriate semantic "
    + "XML root tag for each XML document");
    Parameter_Panel.addComponent(DtdRootElement_Text, 2, 6, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 7, 11);
    Parameter_Panel.addLabel("Min. Attribute Support:", 0, 8, KeyEvent.VK_A,
    MinAttributeRelSupport_Text, true, "Task input: Choose an appropriate "
    + "parameter value between 0.0 and 1.0!");
    Parameter_Panel.addComponent(MinAttributeRelSupport_Text, 2, 8, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 9, 11);
    Parameter_Panel.addLabel("DTD Documentation File:", 0, 10, KeyEvent.VK_D,
    DtdFile_Button.getDefaultButton(), true, "Task output: This file will "
    + "be an initial HTML documentation of the conceptual XML DTD");
    Parameter_Panel.addComponent(DtdDocumentationFile_Text, 2, 10);
    Parameter_Panel.addKButtonPanel(DtdDocumentationFile_Button, 4, 10);

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
  
  public static void main(String pOptions[]) {}
  
}