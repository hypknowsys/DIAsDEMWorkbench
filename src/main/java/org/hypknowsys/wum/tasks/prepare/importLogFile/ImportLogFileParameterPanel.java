/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.tasks.prepare.importLogFile;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class ImportLogFileParameterPanel extends WumParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ImportLogFileParameter CastParameter = null;
  
  private KScrollTextArea Exclude_Text = null;
  private KScrollTextArea Include_Text = null;
  private KScrollTextArea Replace_Text = null;
  private KScrollTextArea Taxonomy_Text = null;

  private KTextField LogFilename_Text = null;
  private KButtonPanel LogFilename_Button = null;
  private KRadioButtonGroup Formats_Group = null;

  private KGridBagPanel Advanced_Panel = null;
  private JCheckBox Anchor_CheckBox = null;
  private JCheckBox Parameter_CheckBox = null;
  private JCheckBox LowerCase_CheckBox = null;
  private JCheckBox SkipDataCleaning_CheckBox = null;
  
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
  
  public ImportLogFileParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ImportLogFileParameterPanel(Server pWumServer, Project pWumProject, 
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
    
    super();
    
    this.setContext(pWumServer, pWumProject, pWumGui,
    pWumGuiPreferences);
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
    
    if (ActionCommand.equals("LogFilename_Button")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      LogFilename_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Existing Log File",
      WUMguiPreferences.TEXT_FILE_FILTER, true, false);
      
    } else if (ActionCommand.equals("WUMprep")) {
    
      SkipDataCleaning_CheckBox.setSelected(true);
      Anchor_CheckBox.setSelected(false);
      Parameter_CheckBox.setSelected(false);
      LowerCase_CheckBox.setSelected(false);
      
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
    
    return "Import Log File";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogLSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    ImportLogFileParameter parameter = new ImportLogFileParameter();
    if ( Formats_Group.getSelectedActionCommand().equals("Common") )
      parameter.setLogFileFormat(AccessLog.COMMON_LOG_FILE_FORMAT);
    else
      if ( Formats_Group.getSelectedActionCommand().equals("Extended") )
        parameter.setLogFileFormat(AccessLog.EXTENDED_LOG_FILE_FORMAT);
      else
        if ( Formats_Group.getSelectedActionCommand().equals("Cookie") )
          parameter.setLogFileFormat(AccessLog.COOKIE_LOG_FILE_FORMAT);
        else
          parameter.setLogFileFormat(AccessLog.WUMPREP_LOG_FILE_FORMAT);
    if ( Anchor_CheckBox.isSelected() )
      parameter.setTruncateHtmlAnchor(true);
    if ( Parameter_CheckBox.isSelected() )
      parameter.setTruncateCgiParameter(true);
    if ( LowerCase_CheckBox.isSelected() )
      parameter.setConvertToLowerCase(true);
    if ( SkipDataCleaning_CheckBox.isSelected() )
      parameter.setSkipDataCleaning(true);
    parameter.setLogFileFilename( LogFilename_Text.getText().trim() );
    parameter.setIncludeList( Include_Text.getText() );
    parameter.setExcludeList( Exclude_Text.getText() );
    parameter.setReplaceList( Replace_Text.getText() );
    parameter.setTaxonomyList( Taxonomy_Text.getText() );
        
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof ImportLogFileParameter) {
      CastParameter = (ImportLogFileParameter)pTaskParameter;
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
    
    WumProject.setProperty("IMPORT_LOG_FILE:_DEFAULT_EXCLUDE_LIST",
    Exclude_Text.getText());
    WumProject.setProperty("IMPORT_LOG_FILE:_DEFAULT_INCLUDE_LIST",
    Include_Text.getText());
    WumProject.setProperty("IMPORT_LOG_FILE:_DEFAULT_REPLACE_LIST",
    Replace_Text.getText());
    WumProject.setProperty("IMPORT_LOG_FILE:_DEFAULT_TAXONOMY_LIST",
    Taxonomy_Text.getText());
    WumProject.setProperty("IMPORT_LOG_FILE:_DEFAULT_LOG_FILE_DIRECTORY",
    LogFilename_Text.getText());
    int logFileFormat = AccessLog.COMMON_LOG_FILE_FORMAT;
    if ( Formats_Group.getSelectedActionCommand().equals("Common") )
      logFileFormat = AccessLog.COMMON_LOG_FILE_FORMAT;
    else
      if ( Formats_Group.getSelectedActionCommand().equals("Extended") )
        logFileFormat = AccessLog.EXTENDED_LOG_FILE_FORMAT;
      else
        if ( Formats_Group.getSelectedActionCommand().equals("Cookie") )
          logFileFormat = AccessLog.COOKIE_LOG_FILE_FORMAT;
        else
          logFileFormat = AccessLog.WUMPREP_LOG_FILE_FORMAT;
    WumProject.setIntProperty("IMPORT_LOG_FILE:_DEFAULT_LOG_FILE_FORMAT",
    logFileFormat);
    WumProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (LogFilename_Text != null) {
      return LogFilename_Text;
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
    
    Formats_Group = new KRadioButtonGroup(4, 4);
    Formats_Group.addFirstButton("Common",
      KeyEvent.VK_C, true, true, "Common", this);
    Formats_Group.addNextButton("Extended",
      KeyEvent.VK_E, true, false, "Extended", this);
    Formats_Group.addNextButton("Cookie",
      KeyEvent.VK_K, true, false, "Cookie", this);
    Formats_Group.addLastButton("WUMprep",
      KeyEvent.VK_W, true, false, "WUMprep", this);

    Anchor_CheckBox = new JCheckBox(
      "1. Truncate HTML Anchors (#)", true);
    Anchor_CheckBox.setActionCommand("Anchor");
    Anchor_CheckBox.setMnemonic(KeyEvent.VK_H);
    Parameter_CheckBox = new JCheckBox(
      "2. Truncate CGI Parameters (?)", true);
    Parameter_CheckBox.setActionCommand("Parameter");
    Parameter_CheckBox.setMnemonic(KeyEvent.VK_P);
    LowerCase_CheckBox = new JCheckBox(
      "3. Conversion to Lower Case", false);
    LowerCase_CheckBox.setActionCommand("LowerCase");
    LowerCase_CheckBox.setMnemonic(KeyEvent.VK_L);
    SkipDataCleaning_CheckBox = new JCheckBox(
      "Skip Data Cleaning Phase", false);
    SkipDataCleaning_CheckBox.setActionCommand("SkipCleaning");
    SkipDataCleaning_CheckBox.setMnemonic(KeyEvent.VK_S);
    
    if (CastParameter != null) {
      Exclude_Text = new KScrollTextArea(CastParameter.getExcludeList());
      Include_Text = new KScrollTextArea(CastParameter.getIncludeList());
      Replace_Text = new KScrollTextArea(CastParameter.getReplaceList());
      Taxonomy_Text = new KScrollTextArea(CastParameter.getTaxonomyList());
      LogFilename_Text = new KTextField(CastParameter
      .getLogFileFilename(), 30);
      switch (CastParameter.getLogFileFormat()) {
        case AccessLog.COMMON_LOG_FILE_FORMAT: {
          Formats_Group.setSelected(0, true); break;
        }
        case AccessLog.EXTENDED_LOG_FILE_FORMAT: {
          Formats_Group.setSelected(1, true); break;
        }
        case AccessLog.COOKIE_LOG_FILE_FORMAT: {
          Formats_Group.setSelected(2, true); break;
        }
        case AccessLog.WUMPREP_LOG_FILE_FORMAT: {
          Formats_Group.setSelected(3, true); break;
        }
      }
      Anchor_CheckBox.setSelected(CastParameter.getTruncateHtmlAnchor());
      Parameter_CheckBox.setSelected(CastParameter.getTruncateCgiParameter());
      LowerCase_CheckBox.setSelected(CastParameter.getConvertToLowerCase());
      SkipDataCleaning_CheckBox.setSelected(CastParameter.getSkipDataCleaning());
    }
    else {
      Exclude_Text = new KScrollTextArea(WumProject.getProperty(
      "IMPORT_LOG_FILE:_DEFAULT_EXCLUDE_LIST"));
      Include_Text = new KScrollTextArea(WumProject.getProperty(
      "IMPORT_LOG_FILE:_DEFAULT_INCLUDE_LIST"));
      Replace_Text = new KScrollTextArea(WumProject.getProperty(
      "IMPORT_LOG_FILE:_DEFAULT_REPLACE_LIST"));
      Taxonomy_Text = new KScrollTextArea(WumProject.getProperty(
      "IMPORT_LOG_FILE:_DEFAULT_TAXONOMY_LIST"));
      LogFilename_Text = new KTextField(WumProject.getProperty(
      "IMPORT_LOG_FILE:_DEFAULT_LOG_FILE_DIRECTORY"), 30);
      switch (WumProject.getIntProperty(
      "IMPORT_LOG_FILE:_DEFAULT_LOG_FILE_FORMAT")) {
        case AccessLog.COMMON_LOG_FILE_FORMAT: {
          Formats_Group.setSelected(0, true); break;
        }
        case AccessLog.EXTENDED_LOG_FILE_FORMAT: {
          Formats_Group.setSelected(1, true); break;
        }
        case AccessLog.COOKIE_LOG_FILE_FORMAT: {
          Formats_Group.setSelected(2, true); break;
        }
        case AccessLog.WUMPREP_LOG_FILE_FORMAT: {
          Formats_Group.setSelected(3, true); break;
        }
      }
    }
    LogFilename_Text.setCaretAtEnding();    
    
    LogFilename_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    LogFilename_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "LogFilename_Button", this,
    "Click this button to select the log file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(LogFilename_Text);

    Advanced_Panel = new KGridBagPanel();
    Advanced_Panel.addComponent(Anchor_CheckBox, 0, 0);
    Advanced_Panel.addBlankColumn(1, 0, 12);
    Advanced_Panel.addComponent(Parameter_CheckBox, 2, 0);
    Advanced_Panel.addBlankRow(0, 1, 11);
    Advanced_Panel.addComponent(LowerCase_CheckBox, 0, 2);
    Advanced_Panel.addComponent(SkipDataCleaning_CheckBox, 2, 2);

    Parameter_Panel = new KGridBagPanel(12, 12, 11, 11);
    Parameter_Panel.addLabel("File Name:", 0, 0, KeyEvent.VK_F,
    LogFilename_Text, true, "Task input: Select the log file to be imported.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(LogFilename_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(LogFilename_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Format:", 0, 2);
    Parameter_Panel.addComponent(Formats_Group, 2, 2, new Insets(0, 0, 0, 0), 
      3, 1, 0.0 );
    Parameter_Panel.setLabelAnchor( GridBagConstraints.NORTHWEST );
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Advanced:", 0, 4);
    Parameter_Panel.addComponent(Advanced_Panel, 2, 4, new Insets(0, 0, 0, 0), 
      3, 1, 0.0 );

    KBorderPanel exclude_Panel = new KBorderPanel(12, 12, 11, 11);
    Exclude_Text.setFont( new Font("monospaced", Font.PLAIN, 12) );
    JLabel label1 = new JLabel("Host, Request, Status Code must not " +
      "contain any Substring from this List", JLabel.LEFT);
    label1.setBorder( BorderFactory.createEmptyBorder(0, 0, 11, 0) );
    exclude_Panel.addNorth(label1);
    exclude_Panel.addCenter(Exclude_Text);

    KBorderPanel include_Panel = new KBorderPanel(12, 12, 11, 11);
    Include_Text.setFont( new Font("monospaced", Font.PLAIN, 12) );
    JLabel label2 = new JLabel("Host, Request, Status Code must contain " + 
      "at least one Substring from this List", JLabel.LEFT);
    label2.setBorder( BorderFactory.createEmptyBorder(0, 0, 11, 0) );
    include_Panel.addNorth(label2);
    include_Panel.addCenter(Include_Text);

    KBorderPanel replace_Panel = new KBorderPanel(12, 12, 11, 11);
    Replace_Text.setFont( new Font("monospaced", Font.PLAIN, 12) );
    JLabel label3 = new JLabel("In all Requests: 1st Substring of each Line" +
      " is Replaced by 2nd Substring", JLabel.LEFT);
    label3.setBorder( BorderFactory.createEmptyBorder(0, 0, 11, 0) );
    replace_Panel.addNorth(label3);
    replace_Panel.addCenter(Replace_Text);
    
    KBorderPanel taxonomy_Panel = new KBorderPanel(12, 12, 11, 11);
    Taxonomy_Text.setFont( new Font("monospaced", Font.PLAIN, 12) );
    JLabel label4 = new JLabel("If the URLs contains 1st Substring, it'll " +
      "be replaced by 2nd String (Taxonomy Item)", JLabel.LEFT);
    label4.setBorder( BorderFactory.createEmptyBorder(0, 0, 11, 0) );
    taxonomy_Panel.addNorth(label4);
    taxonomy_Panel.addCenter(Taxonomy_Text);

    JTabbedPane textAreasPanel = new JTabbedPane();
    textAreasPanel.addTab("Log File", Parameter_Panel);
    textAreasPanel.addTab("4. Include List", include_Panel);
    textAreasPanel.addTab("5. Exclude List", exclude_Panel);
    textAreasPanel.addTab("6. Replace List", replace_Panel);
    textAreasPanel.addTab("7. Concept List", taxonomy_Panel);

    this.removeAll();
    this.validate();
    this.addCenter(textAreasPanel);
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