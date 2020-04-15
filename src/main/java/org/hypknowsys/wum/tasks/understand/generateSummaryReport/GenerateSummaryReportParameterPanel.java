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

package org.hypknowsys.wum.tasks.understand.generateSummaryReport;

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

public class GenerateSummaryReportParameterPanel extends WumParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private GenerateSummaryReportParameter CastParameter = null;
  
  private KTextField ReportFilename_Text = null;
  private KButtonPanel ReportFilename_Button = null;

  private JTable Settings_Table = null;
  private JScrollPane Settings_ScrollPane = null;
  private ReportSettingsTableModel Settings_TableModel = null;

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
  
  public GenerateSummaryReportParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public GenerateSummaryReportParameterPanel(Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
    
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
    
    if (ActionCommand.equals("TextFileName_Button")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ReportFilename_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Report File to be Generated",
      WUMguiPreferences.HTML_FILE_FILTER, false, true);
      
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
    
    return "Generate Summary Report";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    if (CastParameter == null) {
      CastParameter = new GenerateSummaryReportParameter();
    }
    CastParameter.setReportFilename(ReportFilename_Text.getText().trim());
    
    return CastParameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof GenerateSummaryReportParameter) {
      CastParameter = (GenerateSummaryReportParameter)pTaskParameter;
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
    
    WumProject.setProperty("GENERATE_SUMMARY_REPORT:_DEFAULT_REPORT_FILENAME",
    ReportFilename_Text.getText());
    if (CastParameter != null) {
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_REQUESTED_PAGES",
      CastParameter.getNumberOfMostRequestedPages());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_REQUESTED_DIRECTORIES",
      CastParameter.getNumberOfMostRequestedDirectories());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_LEAST_REQUESTED_PAGES",
      CastParameter.getNumberOfLeastRequestedPages());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_LEAST_REQUESTED_DIRECTORIES",
      CastParameter.getNumberOfLeastRequestedDirectories());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_ENTRY_PAGES",
      CastParameter.getNumberOfTopEntryPages());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_EXIT_PAGES",
      CastParameter.getNumberOfTopExitPages());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_SINGLE_ACCESS_PAGES",
      CastParameter.getNumberOfSingleAccessPages());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_ACTIVE_TOP_LEVEL_DOMAINS",
      CastParameter.getNumberOfMostActiveTopLevelDomains());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_ACTIVE_2ND_LEVEL_DOMAINS",
      CastParameter.getNumberOfMostActiveSecondLevelDomains());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_VISITORS",
      CastParameter.getNumberOfTopVisitors());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_REFERRER_SITES",
      CastParameter.getNumberOfTopReferrerSites());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_REFERRER_PAGES",
      CastParameter.getNumberOfTopReferrerPages());
      WumProject.setIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_USED_BROWSERS",
      CastParameter.getNumberOfMostUsedBrowsers());
    }    
    WumProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (ReportFilename_Text != null) {
      return ReportFilename_Text;
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
    
    if (CastParameter == null) {
      CastParameter = new GenerateSummaryReportParameter();
      CastParameter.setReportFilename(WumProject.getProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_REPORT_FILENAME"));
      CastParameter.setNumberOfMostRequestedPages(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_REQUESTED_PAGES"));
      CastParameter.setNumberOfMostRequestedDirectories(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_REQUESTED_DIRECTORIES"));
      CastParameter.setNumberOfLeastRequestedPages(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_LEAST_REQUESTED_PAGES"));
      CastParameter.setNumberOfLeastRequestedDirectories(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_LEAST_REQUESTED_DIRECTORIES"));
      CastParameter.setNumberOfTopEntryPages(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_ENTRY_PAGES"));
      CastParameter.setNumberOfTopExitPages(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_EXIT_PAGES"));
      CastParameter.setNumberOfSingleAccessPages(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_SINGLE_ACCESS_PAGES"));
      CastParameter.setNumberOfMostActiveTopLevelDomains(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_ACTIVE_TOP_LEVEL_DOMAINS"));
      CastParameter.setNumberOfMostActiveSecondLevelDomains(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_ACTIVE_2ND_LEVEL_DOMAINS"));
      CastParameter.setNumberOfTopVisitors(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_VISITORS"));
      CastParameter.setNumberOfTopReferrerSites(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_REFERRER_SITES"));
      CastParameter.setNumberOfTopReferrerPages(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_TOP_REFERRER_PAGES"));
      CastParameter.setNumberOfMostUsedBrowsers(WumProject.getIntProperty(
      "GENERATE_SUMMARY_REPORT:_DEFAULT_MOST_USED_BROWSERS"));
    }    
    
    Settings_TableModel = new ReportSettingsTableModel(CastParameter);
    Settings_Table = new JTable(Settings_TableModel);
    Settings_Table.setPreferredScrollableViewportSize(new Dimension(100, 150));
    Settings_ScrollPane = new JScrollPane(Settings_Table);

    for (int i = 0; i < Settings_TableModel.getColumnCount(); i++) {
      Settings_Table.getColumnModel().getColumn(i).setPreferredWidth( 
        Settings_TableModel.getPreferredColumnWidth(i) );
    }

    ReportFilename_Text = new KTextField(CastParameter.getReportFilename(), 30);
    ReportFilename_Text.setCaretAtEnding();    
    
    ReportFilename_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ReportFilename_Button.addSingleButton("...",
    KeyEvent.VK_C, true, true, "TextFileName_Button", this,
    "Click this button to select the HTML report file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(ReportFilename_Text);

    Parameter_Panel.addLabel("Report File Name:", 0, 0, KeyEvent.VK_F,
    ReportFilename_Button.getDefaultButton(), true,
    "Task input: Specify the file name of the HTML report to be generated.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(ReportFilename_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(ReportFilename_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.setLabelAnchor(GridBagConstraints.NORTHWEST);
    Parameter_Panel.addLabel("Report Settings:", 0, 2, KeyEvent.VK_S, 
    Settings_Table, true, "Task input: Specify the report settings.");
    Parameter_Panel.addComponent(Settings_ScrollPane, 2, 2, 
      new Insets(0, 0, 0, 0), 3, 1, 1.0);
    
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