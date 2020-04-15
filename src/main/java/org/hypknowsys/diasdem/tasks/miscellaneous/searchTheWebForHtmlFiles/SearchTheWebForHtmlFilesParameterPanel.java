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

package org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Locale;

import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Ingo Kampe
 */

public class SearchTheWebForHtmlFilesParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private SearchTheWebForHtmlFilesParameter CastParameter = null;
  private SearchParams searchParams = null;
  
  private KTextField ResultFile_Text = null;
  private KButtonPanel ResultFile_Button = null;
  private KTextField GoogleKey_Text = null;  
  private KTextField GoogleSearchString_Text = null;
  private KTextField GoogleSearchMaxResults_Text = null;
  private KTextField GoogleSearchDateStart_Text = null;
  private KTextField GoogleSearchDateEnd_Text = null;
  private KTextField GoogleSearchSites_Text = null;
  private KTextField GoogleSearchFiletypes_Text = null;
  private KTextField GoogleSearchLanguage_Text = null;

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
  
  public SearchTheWebForHtmlFilesParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public SearchTheWebForHtmlFilesParameterPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences) {
    
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
    
    if (ActionCommand.equals("ResultFileName_Button")) {
      
      CurrentProjectDirectory = this.fileNameButtonClicked(
      ResultFile_Text, CurrentProjectDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_U, null, "Select Download URL File to be Created",
      DIAsDEMguiPreferences.URL_FILE_FILTER, false, true);
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
    
    return "Search the Web for HTML Files";
    
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
    
    SearchTheWebForHtmlFilesParameter parameter =
    new SearchTheWebForHtmlFilesParameter();
    
    parameter.setGoogleKey(
    GoogleKey_Text.getText().trim());
    parameter.setGoogleSearchDateEnd(
    GoogleSearchDateEnd_Text.getText().trim());
    parameter.setGoogleSearchDateStart(
    GoogleSearchDateStart_Text.getText().trim());
    parameter.setGoogleSearchFiletypes(
    GoogleSearchFiletypes_Text.getText().trim());
    parameter.setGoogleSearchLanguage(
    GoogleSearchLanguage_Text.getText().trim());
    parameter.setGoogleSearchMaxResults(
    Tools.string2Int(GoogleSearchMaxResults_Text.getText().trim()));
    parameter.setGoogleSearchSites(
    GoogleSearchSites_Text.getText().trim());
    parameter.setGoogleSearchString(
    GoogleSearchString_Text.getText().trim());
    parameter.setResultFileName(
    ResultFile_Text.getText().trim());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof SearchTheWebForHtmlFilesParameter) {
      CastParameter = (SearchTheWebForHtmlFilesParameter)pTaskParameter;
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
    
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_KEY",
    GoogleKey_Text.getText().trim());
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_DATE_END",
    GoogleSearchDateEnd_Text.getText().trim());
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_DATE_START",
    GoogleSearchDateStart_Text.getText().trim());
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_FILETYPES",
    GoogleSearchFiletypes_Text.getText().trim());
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_LANGUAGE",
    GoogleSearchLanguage_Text.getText().trim());
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_MAX_RESULTS",
    GoogleSearchMaxResults_Text.getText().trim());
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_SITES",
    GoogleSearchSites_Text.getText().trim());
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_STRING",
    GoogleSearchString_Text.getText().trim());
    DiasdemProject.setProperty(
    "SEARCH_THE_WEB_FOR_HTML_FILES:_RESULT_FILE_NAME",
    ResultFile_Text.getText().trim());
    DiasdemProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (ResultFile_Text != null) {
      return ResultFile_Text;
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
      ResultFile_Text = new KTextField(
      CastParameter.getResultFileName(), 30);
      GoogleKey_Text = new KTextField(
      CastParameter.getGoogleKey(), 30);
      GoogleSearchString_Text = new KTextField(
      CastParameter.getGoogleSearchString(), 30);
      GoogleSearchMaxResults_Text = new KTextField(
      CastParameter.getGoogleSearchMaxResults() + "", 30);
      GoogleSearchDateStart_Text = new KTextField(
      CastParameter.getGoogleSearchDateStart(), 30);
      GoogleSearchDateEnd_Text = new KTextField(
      CastParameter.getGoogleSearchDateEnd(), 30);
      GoogleSearchSites_Text = new KTextField(
      CastParameter.getGoogleSearchSites(), 30);
      GoogleSearchFiletypes_Text = new KTextField(
      CastParameter.getGoogleSearchFiletypes(), 30);
      GoogleSearchLanguage_Text = new KTextField(
      CastParameter.getGoogleSearchLanguage(), 30);
    }
    else {
      ResultFile_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_RESULT_FILE_NAME"), 30);
      GoogleKey_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_KEY"), 30);
      GoogleSearchString_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_STRING"), 30);
      GoogleSearchMaxResults_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_MAX_RESULTS"), 30);
      GoogleSearchDateStart_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_DATE_START"), 30);
      GoogleSearchDateEnd_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_DATE_END"), 30);
      GoogleSearchSites_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_SITES"), 30);
      GoogleSearchFiletypes_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_FILETYPES"), 30);
      GoogleSearchLanguage_Text = new KTextField(DiasdemProject.getProperty(
      "SEARCH_THE_WEB_FOR_HTML_FILES:_GOOGLE_SEARCH_LANGUAGE"), 30);
    }

    ResultFile_Text.setCaretAtEnding();
    GoogleKey_Text.setCaretAtEnding();  
    GoogleSearchString_Text.setCaretAtEnding();
    GoogleSearchMaxResults_Text.setCaretAtEnding();
    GoogleSearchDateStart_Text.setCaretAtEnding();
    GoogleSearchDateEnd_Text.setCaretAtEnding();
    GoogleSearchSites_Text.setCaretAtEnding();
    GoogleSearchFiletypes_Text.setCaretAtEnding();    
    GoogleSearchLanguage_Text.setCaretAtEnding();    
    
    ResultFile_Button = new KButtonPanel(0, 0, 0, 0, 1,
    KButtonPanel.HORIZONTAL_RIGHT);
    ResultFile_Button.addSingleButton("...",
    KeyEvent.VK_U, true, true, "ResultFileName_Button", this,
    "Click this button to select the download URL file.");
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    //Parameter_Panel.setVisible(true);
    //Parameter_Panel.setSize(300,300);
    Parameter_Panel.startFocusForwarding(ResultFile_Text);

    Parameter_Panel.addLabel("Download URL File:", 0, 0, KeyEvent.VK_U,
    ResultFile_Button.getDefaultButton(), true,
    "Task output: This download URL file will contain references " +
    "to the most relevant documents.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(ResultFile_Text, 2, 0);
    Parameter_Panel.addBlankColumn(3, 0, 12);
    Parameter_Panel.addKButtonPanel(ResultFile_Button, 4, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());

    Parameter_Panel.addLabel("Google Search Parameter", 0, 2);
    Parameter_Panel.addBlankRow(0, 4, 11);
    
    Parameter_Panel.addLabel("Google Search Key:", 0, 5, KeyEvent.VK_K,
    GoogleKey_Text, true, 
    "Task input: Enter your personal Google API key obtained from Google.");
    Parameter_Panel.addComponent(GoogleKey_Text, 2, 5, 
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 6, 11);
    Parameter_Panel.addLabel("Google Search String:", 0, 7, KeyEvent.VK_S,
    GoogleSearchString_Text, true,
    "Task input: Enter the string to search for such as \"DIAsDEM Workbench\".");
    Parameter_Panel.addComponent(GoogleSearchString_Text, 2, 7,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 8, 11);
    Parameter_Panel.addLabel("Max. Number of Results:", 0, 9, KeyEvent.VK_M,
    GoogleSearchMaxResults_Text, true,
    "Task input: Enter the maximum number of URLs to be retrieved from Google.");
    Parameter_Panel.addComponent(GoogleSearchMaxResults_Text, 2, 9,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 10, 11);
    
    Parameter_Panel.addLabel("Restrict Sites:", 0, 11, KeyEvent.VK_R,
    GoogleSearchSites_Text, true,
    "Optional task input: The Google search can be restricted to this comma-separated list of sites (e.g., hypknowsys.org).");
    Parameter_Panel.addComponent(GoogleSearchSites_Text, 2, 11,
    new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 12, 11);
    Parameter_Panel.addLabel("Restrict File Types:", 0, 13, KeyEvent.VK_F,
    GoogleSearchFiletypes_Text, true,
    "Optional task input: The Google search can be restricted to this comma-separated list of file extensions (e.g., html).");
    Parameter_Panel.addComponent(GoogleSearchFiletypes_Text, 2, 13,new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 14,11);

    Parameter_Panel.addLabel("Restrict Date (Start):", 0, 15, KeyEvent.VK_D,
    GoogleSearchDateStart_Text, true,
    "Optional task input: The Google search can be restricted to start date (format: YYYY-MM-DD).");
    Parameter_Panel.addComponent(GoogleSearchDateStart_Text, 2, 15,new Insets(0, 0, 0, 0), 3, 1);
    Parameter_Panel.addBlankRow(0, 16, 11);
    Parameter_Panel.addLabel("Restrict Date (End):", 0, 17, KeyEvent.VK_E,
    GoogleSearchDateEnd_Text, true,  
    "Optional task input: The Google search can be restricted to end date (format: YYYY-MM-DD).");    
    Parameter_Panel.addComponent(GoogleSearchDateEnd_Text, 2, 17,
    new Insets(0, 0, 0, 0), 3, 1);

    Parameter_Panel.addBlankRow(0, 18, 11);
    Parameter_Panel.addLabel("Restrict Language:", 0, 19, KeyEvent.VK_L,
    GoogleSearchLanguage_Text, true,
    "Optional task input: The Google search can be restricted to this language (e.g., lang_en).");    
    Parameter_Panel.addComponent(GoogleSearchLanguage_Text, 2, 19,
    new Insets(0, 0, 0, 0), 3, 1);
      
    //Parameter_Panel.setMinimumSize(new Dimension(30,30));
    
    /*Parameter_Panel.setPreferredSize(Parameter_Panel.getMinimumSize());    
    KScrollBorderPanel Parameter_ScrollPanel = new KScrollBorderPanel(
    12, 12, 11, 11);
    Parameter_ScrollPanel.addCenter(Parameter_Panel);
    Parameter_ScrollPanel.startFocusForwarding(ResultFile_Text);
    */
        
    this.removeAll();
    this.validate();
    this.addNorth(Parameter_Panel);
    //this.addCenter(Parameter_ScrollPanel);
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