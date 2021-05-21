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

package org.hypknowsys.diasdem.client.gui.tools.taggingQualityEvaluator22;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskControlPanelContainer;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.diasdem.core.DIAsDEMconceptualDtd;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMconceptualDtd;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KBorderPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KCheckBox;
import org.hypknowsys.misc.swing.KComboBox;
import org.hypknowsys.misc.swing.KFileFilter;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KScrollTextArea;
import org.hypknowsys.misc.swing.KTabbedPane;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.misc.io.CsvItemizer;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;

/**
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public class TaggingQualityEvaluatorControlPanel
extends DiasdemActionsControlPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private File CurrentProjectDirectory = null;
  private File InputFile = null;
  private File OutputFile = null;
  private File RestFile = null;
  private File LogFile = null;
  private DIAsDEMconceptualDtd MyConceptualDtd = null;
  
  private KGridBagPanel Results_Panel = null;
  private KButtonPanel Button_Panel = null;
  private KBorderPanel Dtd_Panel = null;
  private KScrollTextArea Dtd_ScrollTextArea = null;
  private NumberFormat MyNumberFormatter = null;
  
  private TextFile InputTextFile = null;
  private TextFile OutputTextFile = null;
  private TextFile NeOutputTextFile = null;
  private TextFile HtmlOutputTextFile = null;
  private TextFile HtmlResultTextFile = null;
  private TextFile RestTextFile = null;
  private TextFile LogTextFile = null;
  
  private String CurrentTextUnit = null;
  private String TaggedUntaggedTextUnit = null;
  private String DiasdemDocumentID = null;
  private String XmlStartTag = null;
  private String TaggedContent = null;
  private String XmlEndTag = null;
  private String ElementName = null;
  private String[] AttributeNames = null;
  private int[][] AttributeEvaluationResults = null;
  
  private int NumberOfTextUnitsToBeEvaluated = 0;
  private int NumberOfEvaluatedTextUnits_Offset = 0;
  private int NumberOfEvaluatedTextUnits = 0;
  private int NumberOfCurrentTextUnit = 0;
  private int NumberOfExtractedNEs = 0;
  
  // main tabbed pane
  private KTabbedPane Tabbed_Pane = null;
  
  // text unit panel
  private KGridBagPanel TextUnit_Panel = null;
  private KTextField Metadata_Text = null;
  private JScrollPane TaggedTextUnit_ScrollPane = null;
  private KComboBox CorrectElement_Combo = null;
  private KCheckBox XmlTagTooGeneral_CheckBox = null;
  private KCheckBox XmlTagTooSpecific_CheckBox = null;
  private boolean WaitForUserInput = false;
  private JTextPane TaggedTextUnit_Pane = null;
  private JTable EvaluatedNamedEntities_Table = null;
  private JScrollPane EvaluatedNamedEntities_ScrollPane = null;
  private EvaluatedNamedEntities MyEvaluatedNamedEntities = null;
  private EvaluatedXmlTagNames PresentResultsOfXmlTagNames = null;
  private EvaluatedXmlTagAttributes PresentResultsOfXmlTagAttributes = null;
  
  // present results pane
  private JTable PresentResultsOfXmlTagNames_Table = null;
  private JScrollPane PresentResultsOfXmlTagNames_ScrollPane = null;
  private JTable PresentResultsOfXmlTagAttributes_Table = null;
  private JScrollPane PresentResultsOfXmlTagAttributes_ScrollPane = null;

  // XML syntax highlighting
  private MutableAttributeSet TagAttributes = null;
  private MutableAttributeSet ElementAttributes = null; 
  private MutableAttributeSet AttributeValueAttributes = null; 
  private MutableAttributeSet CharacterAttributes = null;
  private MutableAttributeSet CdataAttributes = null;
  private Pattern PartPattern = null;
  private Pattern NamePattern = null;
  private Pattern AttributePattern = null;
  private Matcher PartMatcher = null;
  private Matcher NameMatcher = null;
  private Matcher AttributeMatcher = null;
  
  
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
  
  public TaggingQualityEvaluatorControlPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaggingQualityEvaluatorControlPanel(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences, String pFileName) {
    
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
    ControlPanelContainerIsVisible = false;
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
    
    String tags = "";
    String nes = "";
    String diasdemXmlTag = "";
    String htmlAttributes = "";
    int diasdemTagIsMoreGeneral = 0;
    int diasdemTagIsMoreSpecific = 0;
    if (ActionCommand.equals("FalseNeg") && CorrectElement_Combo
    .getSelectedString().equals("[No Semantic XML Tag]")) {
      JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
      "Please select the correct XML tag!",
      ControlPanelContainer.getTitle(), JOptionPane.ERROR_MESSAGE);
      return;
    }
    else if (ActionCommand.equals("FalsePos") && CorrectElement_Combo
    .getSelectedString().equals(ElementName)) {
      JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
      "Please select the correct XML tag!", 
      ControlPanelContainer.getTitle(), JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if (ActionCommand.equals("True") || ActionCommand.startsWith("False")) {
      if (!Tools.stringIsNullOrEmpty(ElementName)) {
        tags = CsvItemizer.stringToItem(ElementName) + ","
        + CsvItemizer.stringToItem(CorrectElement_Combo.getSelectedString());
        diasdemXmlTag = ElementName;
      }
      else {
        tags = CsvItemizer.stringToItem("[No Semantic XML Tag]") + "," 
        + CsvItemizer.stringToItem(CorrectElement_Combo.getSelectedString());
        diasdemXmlTag = "[No Semantic XML Tag]";
      }
      if (XmlTagTooGeneral_CheckBox.isSelected()) {
        diasdemTagIsMoreGeneral = 1;
      }
      if (XmlTagTooSpecific_CheckBox.isSelected()) {
        diasdemTagIsMoreSpecific = 1;
      }
      tags += "," + diasdemTagIsMoreGeneral + "," + diasdemTagIsMoreSpecific;
      tags += "," + MyEvaluatedNamedEntities.getSumOfNumberOfNes();
      tags += "," + MyEvaluatedNamedEntities.getSumOfComplCorrect();
      tags += "," + MyEvaluatedNamedEntities.getSumOfPartCorrect();
      tags += "," + MyEvaluatedNamedEntities.getSumOfIncorrect();
      tags += "," + MyEvaluatedNamedEntities.getSumOfMissing();
      // update table model
      PresentResultsOfXmlTagAttributes.memorizeOneCompleteTextUnitEvaluation(
      MyEvaluatedNamedEntities.getSumOfNumberOfNes(),
      MyEvaluatedNamedEntities.getSumOfComplCorrect(),
      MyEvaluatedNamedEntities.getSumOfPartCorrect(),
      MyEvaluatedNamedEntities.getSumOfIncorrect(),
      MyEvaluatedNamedEntities.getSumOfMissing(), true);
      for (int i = 0; AttributeNames != null && i < AttributeNames.length; 
      i++) {
        NeOutputTextFile.setNextLine("\"" +  diasdemXmlTag + "\",\""
        + AttributeNames[i] + "\"," + MyEvaluatedNamedEntities
        .getEvaluationResult(i, MyEvaluatedNamedEntities.NUMBER_OF_NES)
        + "," + MyEvaluatedNamedEntities.getEvaluationResult(i, 
        MyEvaluatedNamedEntities.COMPL_CORRECT) + "," + MyEvaluatedNamedEntities
        .getEvaluationResult(i, MyEvaluatedNamedEntities.PART_CORRECT)
        + "," + MyEvaluatedNamedEntities.getEvaluationResult(i, 
        MyEvaluatedNamedEntities.INCORRECT) + "," + MyEvaluatedNamedEntities
        .getEvaluationResult(i, MyEvaluatedNamedEntities.MISSING) + ",");
        htmlAttributes += AttributeNames[i] + " (N=" + MyEvaluatedNamedEntities
        .getEvaluationResult(i, MyEvaluatedNamedEntities.NUMBER_OF_NES)
        + ", CC=" + MyEvaluatedNamedEntities.getEvaluationResult(i, 
        MyEvaluatedNamedEntities.COMPL_CORRECT) + ", PC=" 
        + MyEvaluatedNamedEntities.getEvaluationResult(i, 
        MyEvaluatedNamedEntities.PART_CORRECT) + ", I="
        + MyEvaluatedNamedEntities.getEvaluationResult(i, 
        MyEvaluatedNamedEntities.INCORRECT) + ", M=" + MyEvaluatedNamedEntities
        .getEvaluationResult(i, MyEvaluatedNamedEntities.MISSING) + ")"
        + ((i + 1) < AttributeNames.length ? ", " : "");
      }
      htmlAttributes += "</p>";
      HtmlOutputTextFile.setNextLine("<a name=\"" + NumberOfCurrentTextUnit 
      + "\"><h4>Sample Text Unit " 
      + (NumberOfCurrentTextUnit + NumberOfEvaluatedTextUnits_Offset) + "/" 
      + (NumberOfTextUnitsToBeEvaluated + NumberOfEvaluatedTextUnits_Offset)
      + "</h4></a>");
      HtmlOutputTextFile.setNextLine("<pre>");
      HtmlOutputTextFile.setNextLine(Tools.replaceNewlineWithHtmlBrTag(
      Tools.insertHtmlEntityReferences(Tools.insertLineBreaks(100, XmlStartTag
      + TaggedContent + XmlEndTag))));
      HtmlOutputTextFile.setNextLine("</pre>");   
    }

    
    if (ActionCommand.equals("Stop")) {
      this.setControlPanelContainerClosed(true);
    }
    else if (ActionCommand.equals("True")) {
      NumberOfEvaluatedTextUnits++;
      if (TaggedUntaggedTextUnit.startsWith("<")) {
        OutputTextFile.setNextLine("1,0,0,0,\"TP\"," + tags + ",\"" + Tools
        .removeQuotesAndNewLines(Tools.replaceDoubleQuotesWithSingleQuotes(
        CurrentTextUnit)) + "\"");
        PresentResultsOfXmlTagNames.memorizeOneTextUnitEvaluation(
        EvaluatedXmlTagNames.TRUE_POSITIVE, true);
        HtmlOutputTextFile.setNextLine(
        "<p>DIAsDEM XML Tag Name: True Positive</p>");   
        HtmlOutputTextFile.setNextLine("<p>DIAsDEM XML Tag Attributes: " 
        + htmlAttributes + "</p");
      }
      else {
        OutputTextFile.setNextLine("0,1,0,0,\"TN\"," + tags + ",\"" + Tools
        .removeQuotesAndNewLines(Tools.replaceDoubleQuotesWithSingleQuotes(
        CurrentTextUnit)) + "\"");
        PresentResultsOfXmlTagNames.memorizeOneTextUnitEvaluation(
        EvaluatedXmlTagNames.TRUE_NEGATIVE, true);
        HtmlOutputTextFile.setNextLine(
        "<p>DIAsDEM XML Tag Name: True Negative</p>");   
      }
      CurrentTextUnit = InputTextFile.getNextLine();
      this.enableButtons();
    }
    else if (ActionCommand.equals("FalsePos")) {
      NumberOfEvaluatedTextUnits++;
      OutputTextFile.setNextLine("0,0,1,0,\"FP\"," + tags + ",\"" + Tools
      .removeQuotesAndNewLines(Tools.replaceDoubleQuotesWithSingleQuotes(
      CurrentTextUnit)) + "\"");
      PresentResultsOfXmlTagNames.memorizeOneTextUnitEvaluation(
      EvaluatedXmlTagNames.FALSE_POSITIVE, true);
      CurrentTextUnit = InputTextFile.getNextLine();
      HtmlOutputTextFile.setNextLine(
      "<p>DIAsDEM XML Tag Name: False Positive</p>");
      HtmlOutputTextFile.setNextLine(
      "<p>Correct XML Tag Name: <code>" + Tools.replaceNewlineWithHtmlBrTag(
      Tools.insertHtmlEntityReferences(Tools.insertLineBreaks(100,
      CorrectElement_Combo.getSelectedString()))) + "</code></p>");
      if (XmlTagTooGeneral_CheckBox.isSelected()) {
        HtmlOutputTextFile.setNextLine("<p>Note: DIAsDEM XML Tag Name is "
        + "More General Than Correct XML Tag Name</p>");
      }
      if (XmlTagTooSpecific_CheckBox.isSelected()) {
        HtmlOutputTextFile.setNextLine("<p>Note: DIAsDEM XML Tag Name is "
        + "More Specific Than Correct XML Tag Name</p>");
      }
      HtmlOutputTextFile.setNextLine("<p>DIAsDEM XML Tag Attributes: " 
      + htmlAttributes + "</p");
      this.enableButtons();
    }
    else if (ActionCommand.equals("FalseNeg")) {
      NumberOfEvaluatedTextUnits++;
      OutputTextFile.setNextLine("0,0,0,1,\"FN\"," + tags + ",\"" + Tools
      .removeQuotesAndNewLines(Tools.replaceDoubleQuotesWithSingleQuotes(
      CurrentTextUnit)) + "\"");
      PresentResultsOfXmlTagNames.memorizeOneTextUnitEvaluation(
      EvaluatedXmlTagNames.FALSE_NEGATIVE, true);
      CurrentTextUnit = InputTextFile.getNextLine();
      HtmlOutputTextFile.setNextLine(
      "<p>DIAsDEM XML Tag Name: False Negative</p>");
      HtmlOutputTextFile.setNextLine(
      "<p>Correct XML Tag Name: <code>" + Tools.replaceNewlineWithHtmlBrTag(
      Tools.insertHtmlEntityReferences(Tools.insertLineBreaks(100,
      CorrectElement_Combo.getSelectedString()))) + "</code></p>");
      if (XmlTagTooGeneral_CheckBox.isSelected()) {
        HtmlOutputTextFile.setNextLine("<p>Note: DIAsDEM XML Tag Name is "
        + "More General Than Correct XML Tag Name</p>");
      }
      if (XmlTagTooSpecific_CheckBox.isSelected()) {
        HtmlOutputTextFile.setNextLine("<p>Note: DIAsDEM XML Tag Name is "
        + "More Specific Than Correct XML Tag Name</p>");
      }
      this.enableButtons();
    }
    else if (ActionCommand.equals("Log")) {
      String message = JOptionPane.showInputDialog(DiasdemGui.getJFrame(),
      "Please enter your comment concerning the current text unit:",
      ControlPanelContainer.getTitle(), JOptionPane.QUESTION_MESSAGE);
      if (message != null) {
        LogTextFile.open();
        LogTextFile.setNextLine("Document ID:  " + DiasdemDocumentID);
        LogTextFile.setNextLine("Text Unit:    " + TaggedUntaggedTextUnit);
        LogTextFile.setNextLine("Comment:      " + message);
        LogTextFile.setNextLine("- - - - - - - - - -");
        LogTextFile.close();
      }
    }
    else if (ActionCommand.equals("Start")) {
      this.start();
    }
    else if (ActionCommand.equals("KInternalFrame:EscapePressed")) {
      if (CloseIfEscapeIsPressed) {
        this.setControlPanelContainerClosed(true);
      }
    }
    else if (WaitForUserInput && ActionCommand.equals("CorrectElement_Combo")) {
      if (CorrectElement_Combo.getSelectedString().equals(ElementName) 
      || (ElementName.equals("") && CorrectElement_Combo.getSelectedString()
      .equals("[No Semantic XML Tag]"))) {
        Button_Panel.setEnabled(1, true);  // True
        XmlTagTooGeneral_CheckBox.setSelected(false);
        XmlTagTooSpecific_CheckBox.setSelected(false);
        XmlTagTooGeneral_CheckBox.setEnabled(false);
        XmlTagTooSpecific_CheckBox.setEnabled(false);
      }
      else {
        Button_Panel.setEnabled(1, false);  // True
        XmlTagTooGeneral_CheckBox.setEnabled(true);
        XmlTagTooSpecific_CheckBox.setEnabled(true);
      }
    }
    else if (WaitForUserInput && ActionCommand.equals("XmlTagTooGeneral")) {
      if (XmlTagTooGeneral_CheckBox.isSelected()) {
        XmlTagTooSpecific_CheckBox.setSelected(false);
        Button_Panel.setEnabled(1, false);  // True
      }
      else {
        Button_Panel.setEnabled(1, true);  // True
      }
    }
    else if (WaitForUserInput && ActionCommand.equals("XmlTagTooSpecific")) {
      if (XmlTagTooSpecific_CheckBox.isSelected()) {
        XmlTagTooGeneral_CheckBox.setSelected(false);
        Button_Panel.setEnabled(1, false);  // True
      }
      else {
        Button_Panel.setEnabled(1, true);  // True
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskControlPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
    
    this.initAttributeSets();
    this.initPatterns();
    
    MyNumberFormatter = NumberFormat.getInstance(Locale.US);
    MyNumberFormatter.setMinimumFractionDigits(2);
    MyNumberFormatter.setMaximumFractionDigits(2);
    
    PriorDiasdemGuiStatus = DO_NOT_MODIFY_DIASDEM_GUI_STATUS;
    if (Tools.isValidandWriteableDirectoryName(
    DiasdemGuiPreferences.getMruProjectDirectory())) {
      CurrentProjectDirectory = new File(
      DiasdemGuiPreferences.getMruProjectDirectory());
    }
    if (DiasdemProject != null && Tools.isValidandWriteableDirectoryName(
    DiasdemProject.getProjectDirectory())) {
      CurrentProjectDirectory = new File(
      DiasdemProject.getProjectDirectory());
    }
    
    this.createButtonPanel();
    this.createParameterPanel();
    
    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY));
    
    Tabbed_Pane = new KTabbedPane();
    Tabbed_Pane.addTab("1. Current Text Unit", TextUnit_Panel, 
    KeyEvent.VK_1, true, true);
    Tabbed_Pane.addTab("2. Present Results", Results_Panel, 
    KeyEvent.VK_2, true, false);
    Tabbed_Pane.addTab("3. XML DTD Elements", Dtd_Panel, 
    KeyEvent.VK_3, true, false);
    
    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
    this.setLayout(new BorderLayout());
    this.add(Tabbed_Pane, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setControlPanelContainer(TaskControlPanelContainer
  pControlPanelContainer) {
    
    ControlPanelContainer = pControlPanelContainer;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void finalize() throws PropertyVetoException {
    
    int confirm = JOptionPane.NO_OPTION;
    if (NumberOfCurrentTextUnit == NumberOfTextUnitsToBeEvaluated) {
      // preceding text unit was the last one in the sample
      JOptionPane.showMessageDialog(this,
      "All text units in the random\nsample have been evaluated!", 
      this.getPreferredTitle(), JOptionPane.INFORMATION_MESSAGE);
      confirm = JOptionPane.YES_OPTION;
    }
    else {
      confirm = JOptionPane.showConfirmDialog(this,
      "Do you want to stop this assessment session?\n"
      + "Note, all remaining text units will be saved\n"
      + "for quality evaluation in a future session.",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
    }
    if (confirm == JOptionPane.YES_OPTION) {
      while (CurrentTextUnit != null) {
        RestTextFile.setNextLine(CurrentTextUnit);
        CurrentTextUnit = InputTextFile.getNextLine();
      }
      if (RestTextFile != null) {
        RestTextFile.close();
      }
      if (InputTextFile != null) {
        InputTextFile.close();
      }
      if (OutputTextFile != null) {
        OutputTextFile.close();
      }
      if (NeOutputTextFile != null) {
        NeOutputTextFile.close();
      }
      if (HtmlOutputTextFile != null) {
        if (NumberOfCurrentTextUnit == NumberOfTextUnitsToBeEvaluated) {
          // preceding text unit was the last one in the sample 
          PresentResultsOfXmlTagNames.appendHtmlSection(HtmlOutputTextFile);
          PresentResultsOfXmlTagAttributes
          .appendHtmlSection(HtmlOutputTextFile);
          HtmlOutputTextFile.setNextLine("<h3>Abbreviations</h3>");
          HtmlOutputTextFile.setNextLine("<p>"
          + "N: true number of relevant named entities in marked-up text unit; "
          + "CC: number of extracted, completely correct named entities; "
          + "PC: number of extracted, partially correct named entities; "
          + "I: number of extracted, but incorrect named entities; "
          + "M: number of missing named entities</p>");
          Template footer = new Template(Tools.stringFromTextualSystemResource(
          "html/HtmlFile_FooterTemplate.html")
          );
          HtmlOutputTextFile.setNextLine(footer.insertValues());
        }
        HtmlOutputTextFile.close();
      }
      if (HtmlResultTextFile != null) {
        HtmlResultTextFile.setNextLine("<tr>"
        + "<td align=\"left\" valign=\"top\">"
        + "Evaluated Text Units</td>"
        + "<td align=\"left\" valign=\"top\">"
        + (NumberOfEvaluatedTextUnits + NumberOfEvaluatedTextUnits_Offset)
        + "</td></tr></table>");
        PresentResultsOfXmlTagNames.appendHtmlSection(HtmlResultTextFile);
        PresentResultsOfXmlTagAttributes.appendHtmlSection(HtmlResultTextFile);
        Template footer = new Template(Tools.stringFromTextualSystemResource(
        "html/HtmlFile_FooterTemplate.html"));
        HtmlResultTextFile.setNextLine(footer.insertValues());
        HtmlResultTextFile.close();
      }
      super.finalize();
    }
    else {
      throw new PropertyVetoException("Close operation canceled!",
      new PropertyChangeEvent(this, "close", null, null));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Tagging Quality Evaluator 2.2";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Button_Panel != null) {
      return Button_Panel.getButton(0);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogLSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createButtonPanel() {
    
    Button_Panel = new KButtonPanel(17, 0, 0, 0, 6,
    KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("Start", KeyEvent.VK_S,
    true, false, "Start", this);
    Button_Panel.addNextButton("True", KeyEvent.VK_T,
    false, false, "True", this);
    Button_Panel.addNextButton("False Pos.", KeyEvent.VK_P,
    false, false, "FalsePos", this);
    Button_Panel.addNextButton("False Neg.", KeyEvent.VK_N,
    false, false, "FalseNeg", this);
    Button_Panel.addNextButton("Log", KeyEvent.VK_L,
    false, false, "Log", this);
    Button_Panel.addLastButton("Stop", 0,
    true, false, "Stop", this);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    if (Dtd_ScrollTextArea == null) {
      Dtd_ScrollTextArea = new KScrollTextArea();
      Dtd_ScrollTextArea.setTextAreaEditable(false);
      Dtd_ScrollTextArea.setCaretAtBeginning();
    }
    Dtd_Panel = new KBorderPanel(12, 12, 11, 11);
    Dtd_Panel.addCenter(Dtd_ScrollTextArea);
    
    Parameter_Panel = null;
    
    Metadata_Text = new KTextField("", 30, true, false);
    
    TaggedTextUnit_Pane = new JTextPane() {
      public boolean getScrollableTracksViewportWidth() {
        boolean wrap = true;
        if (!wrap && this.getText().length() > 0) {
          return false;
        }
        else {
          return super.getScrollableTracksViewportWidth();
        }
      }
    };
    TaggedTextUnit_ScrollPane = new JScrollPane(TaggedTextUnit_Pane);
    TaggedTextUnit_ScrollPane.setPreferredSize(new Dimension(1024, 768));
    TaggedTextUnit_ScrollPane.getViewport().setBackground(TaggedTextUnit_Pane
    .getBackground());
    
    if (MyConceptualDtd != null) {
      TreeSet elementNames = MyConceptualDtd.getElementNames();
      CorrectElement_Combo = new KComboBox(1 + elementNames.size(), true,
      "CorrectElement_Combo", this, false);
      CorrectElement_Combo.addItem("[No Semantic XML Tag]", false);
      Iterator iterator = elementNames.iterator();
      while (iterator.hasNext()) {
        CorrectElement_Combo.addItem((String)iterator.next(), false);
      }
    }
    else {
      CorrectElement_Combo = new KComboBox(0, false,
      "CorrectElement_Combo", this, false);
    }
    KTextField dummyKTextField = new KTextField("", 30, true, false);
    CorrectElement_Combo.setPreferredSize(new Dimension(50,
    (int)dummyKTextField.getPreferredSize().getHeight()));
    CorrectElement_Combo.setFont(dummyKTextField.getFont());
    
    XmlTagTooGeneral_CheckBox = new KCheckBox(
    "More General Than Correct XML Tag", false, (MyConceptualDtd != null ? true
    : false), "XmlTagTooGeneral", this, KeyEvent.VK_G, "Check this box, if the "
    +"XML tag assgned by DIAsDEM is more general than the correct XML tag.");
    XmlTagTooSpecific_CheckBox = new KCheckBox(
    "More Specific Than Correct XML Tag", false, (MyConceptualDtd != null ? true
    : false), "XmlTagTooSpecific", this, KeyEvent.VK_M, "Check this box, if the"
    + " XML tag assgned by DIAsDEM is more specific than the correct XML tag.");

    MyEvaluatedNamedEntities = new EvaluatedNamedEntities();
    if (MyEvaluatedNamedEntities != null) {
      EvaluatedNamedEntities_Table = new JTable(MyEvaluatedNamedEntities);
      for (int i = 0; i < EvaluatedNamedEntities_Table.getColumnCount(); i++) {
        EvaluatedNamedEntities_Table.getColumnModel().getColumn(i)
        .setPreferredWidth(MyEvaluatedNamedEntities.getPreferredColumnWidth(i));
      }
    }
    else {
      EvaluatedNamedEntities_Table = new JTable();
    }
    EvaluatedNamedEntities_Table.setPreferredScrollableViewportSize(
    this.getSize());
    EvaluatedNamedEntities_Table.setSelectionMode(ListSelectionModel
    .SINGLE_SELECTION);
    EvaluatedNamedEntities_Table.setRowSelectionAllowed(false);
    EvaluatedNamedEntities_Table.setColumnSelectionAllowed(false);
    EvaluatedNamedEntities_ScrollPane = new JScrollPane(
    EvaluatedNamedEntities_Table);
    
    TextUnit_Panel = new KGridBagPanel(12, 12, 11, 11);
    TextUnit_Panel.startFocusForwarding(TaggedTextUnit_ScrollPane);
    
    TextUnit_Panel.addComponent(Metadata_Text, 0, 0,
    new Insets(0, 0, 0, 0), 5, 1);
    TextUnit_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    TextUnit_Panel.addComponent(TaggedTextUnit_ScrollPane, 0, 2,
    new Insets(0, 0, 0, 0), 5, 1, 0.7d);
    
    TextUnit_Panel.addBlankRow(0, 3, 11);
    TextUnit_Panel.addLabel("Correct XML Tag:", 0, 4, 
    KeyEvent.VK_C, CorrectElement_Combo);
    TextUnit_Panel.addBlankColumn(1, 0, 12);
    TextUnit_Panel.addComponent(CorrectElement_Combo, 2, 4,
    new Insets(0, 0, 0, 0), 3, 1);

    TextUnit_Panel.addBlankRow(0, 5, 11);
    TextUnit_Panel.addLabel("DIAsDEM XML Tag:", 0, 6);
    TextUnit_Panel.addComponent(XmlTagTooGeneral_CheckBox, 2, 6,
    new Insets(0, 0, 0, 0), 1, 1);
    TextUnit_Panel.addBlankColumn(3, 0, 12);
    TextUnit_Panel.addComponent(XmlTagTooSpecific_CheckBox, 4, 6,
    new Insets(0, 0, 0, 0), 1, 1);

    TextUnit_Panel.addBlankRow(0, 7, 11);
    TextUnit_Panel.addComponent(EvaluatedNamedEntities_ScrollPane, 0, 8,
    new Insets(0, 0, 0, 0), 5, 1, 0.3d);
    
    // present results panel
    
    if (PresentResultsOfXmlTagNames != null) {
      PresentResultsOfXmlTagNames_Table = new JTable(
      PresentResultsOfXmlTagNames);
      for (int i = 0; i < PresentResultsOfXmlTagNames_Table.getColumnCount();
      i++) {
        PresentResultsOfXmlTagNames_Table.getColumnModel().getColumn(i)
        .setPreferredWidth(PresentResultsOfXmlTagNames
        .getPreferredColumnWidth(i));
      }
    }
    else {
      PresentResultsOfXmlTagNames = new EvaluatedXmlTagNames();
      PresentResultsOfXmlTagNames_Table = new JTable();
    }
    PresentResultsOfXmlTagNames_Table.setPreferredScrollableViewportSize(
    this.getSize());
    PresentResultsOfXmlTagNames_Table.setSelectionMode(ListSelectionModel
    .SINGLE_SELECTION);
    PresentResultsOfXmlTagNames_Table.setRowSelectionAllowed(false);
    PresentResultsOfXmlTagNames_Table.setColumnSelectionAllowed(false);
    PresentResultsOfXmlTagNames_ScrollPane = new JScrollPane(
    PresentResultsOfXmlTagNames_Table);
    
    if (PresentResultsOfXmlTagAttributes != null) {
      PresentResultsOfXmlTagAttributes_Table = new JTable(
      PresentResultsOfXmlTagAttributes);
      for (int i = 0; i < PresentResultsOfXmlTagAttributes_Table
      .getColumnCount(); i++) {
        PresentResultsOfXmlTagAttributes_Table.getColumnModel().getColumn(i)
        .setPreferredWidth(PresentResultsOfXmlTagAttributes
        .getPreferredColumnWidth(i));
      }
    }
    else {
      PresentResultsOfXmlTagAttributes = new EvaluatedXmlTagAttributes();
      PresentResultsOfXmlTagAttributes_Table = new JTable();
    }
    PresentResultsOfXmlTagAttributes_Table.setPreferredScrollableViewportSize(
    this.getSize());
    PresentResultsOfXmlTagAttributes_Table.setSelectionMode(ListSelectionModel
    .SINGLE_SELECTION);
    PresentResultsOfXmlTagAttributes_Table.setRowSelectionAllowed(false);
    PresentResultsOfXmlTagAttributes_Table.setColumnSelectionAllowed(false);
    PresentResultsOfXmlTagAttributes_ScrollPane = new JScrollPane(
    PresentResultsOfXmlTagAttributes_Table);
    
    Results_Panel = new KGridBagPanel(12, 12, 11, 11);
    Results_Panel.startFocusForwarding(TaggedTextUnit_ScrollPane);
    
    Results_Panel.addLabel("Quality of XML Tag Names:", 0, 0);
    Results_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Results_Panel.addComponent(PresentResultsOfXmlTagNames_ScrollPane, 0, 2,
    new Insets(0, 0, 0, 0), 1, 1, 0.5d);
    
    Results_Panel.addBlankRow(0, 3, 11);
    Results_Panel.addLabel("Quality of XML Tag Attributes:", 0, 4);

    Results_Panel.addBlankRow(0, 5, 11);
    Results_Panel.addComponent(PresentResultsOfXmlTagAttributes_ScrollPane,
    0, 6, new Insets(0, 0, 0, 0), 1, 1, 0.5d);

}
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void start() {
    
    Button_Panel.setEnabled(4, false);  // Log
    boolean cancel = false;
    
    File file = new File(DiasdemProject.getProperty(
    "DEFAULT_TEXT_UNIT_SAMPLE_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Existing Text Unit Sample File");
    GuiFileChooser.setFileFilter(
    DIAsDEMguiPreferences.TEXT_UNIT_SAMPLE_FILE_FILTER);
    int result = GuiFileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      InputFile = GuiFileChooser.getSelectedFile();
      CurrentProjectDirectory = GuiFileChooser.getCurrentDirectory();
      InputTextFile = new TextFile(InputFile);
      DiasdemProject.setProperty("DEFAULT_TEXT_UNIT_SAMPLE_FILE",
      InputFile.getAbsolutePath());
      DiasdemProject.quickSave();
    }
    else {
      return;
    }
    
    file = new File(DiasdemProject.getProperty(
    "DEFAULT_EVALUATED_TEXT_UNIT_SAMPLE_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle(
    "Select New or Existing Evaluated Text Units File");
    GuiFileChooser.setFileFilter(
    DIAsDEMguiPreferences.EVALUATED_TEXT_UNIT_SAMPLE_FILE_FILTER);
    result = GuiFileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      OutputFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      DIAsDEMguiPreferences.EVALUATED_TEXT_UNIT_SAMPLE_FILE_EXTENSION);
      CurrentProjectDirectory = GuiFileChooser.getCurrentDirectory();
      OutputTextFile = new TextFile(OutputFile);
      NeOutputTextFile = new TextFile(new File(OutputFile.getAbsolutePath() 
      + ".neex"));
      HtmlOutputTextFile = new TextFile(new File(Tools.removeFileExtension(
      OutputFile.getAbsolutePath()) + ".protocol.html"));
      HtmlResultTextFile = new TextFile(new File(Tools.removeFileExtension(
      OutputFile.getAbsolutePath()) + ".results.html"));
      DiasdemProject.setProperty(
      "DEFAULT_EVALUATED_TEXT_UNIT_SAMPLE_FILE", OutputFile.getAbsolutePath());
      DiasdemProject.quickSave();
    }
    else {
      return;
    }
    
    file = new File(DiasdemProject.getProperty(
    "DEFAULT_REMAINING_TEXT_UNIT_SAMPLE_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle(
    "Select Text Unit Sample File to be Created for Next Evaluation");
    GuiFileChooser.setFileFilter(
    DIAsDEMguiPreferences.TEXT_UNIT_SAMPLE_FILE_FILTER);
    result = GuiFileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      RestFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      DIAsDEMguiPreferences.TEXT_UNIT_SAMPLE_FILE_EXTENSION);
      CurrentProjectDirectory = GuiFileChooser.getCurrentDirectory();
      RestTextFile = new TextFile(RestFile);
      if (RestTextFile.exists()) {
        int confirm = JOptionPane.showConfirmDialog(this,
        "The chosen sample file for the next evaluation session\nexists. "
        + "Do you really want to replace this file?",
        this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.NO_OPTION) {
          return;
        }
      }
      DiasdemProject.setProperty(
      "DEFAULT_REMAINING_TEXT_UNIT_SAMPLE_FILE", RestFile.getAbsolutePath());
      DiasdemProject.quickSave();
    }
    else {
      return;
    }
    
    file = new File(DiasdemProject.getProperty(
    "DEFAULT_CONCEPTUAL_DTD_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Existing Conceptual DTD File");
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences
    .CONCEPTUAL_DTD_FILE_FILTER);
    result = GuiFileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File tagFile = GuiFileChooser.getSelectedFile();
      CurrentProjectDirectory = GuiFileChooser.getCurrentDirectory();
      try {
        MyConceptualDtd = new DefaultDIAsDEMconceptualDtd(tagFile
        .getAbsolutePath(), DefaultDIAsDEMconceptualDtd.LOAD);
      }
      catch (IOException e) { return; }
      TreeSet elementNames = MyConceptualDtd.getElementNames();
      Iterator iterator = elementNames.iterator();
      Dtd_ScrollTextArea.setText("");
      while (iterator.hasNext()) {
        Dtd_ScrollTextArea.appendText((String)iterator.next()  + "\n");
      }
      Dtd_ScrollTextArea.setCaretAtBeginning();
      Dtd_Panel.revalidate();
      DiasdemProject.setProperty(
      "DEFAULT_CONCEPTUAL_DTD_FILE", tagFile.getAbsolutePath());
      DiasdemProject.quickSave();
    }
    else {
      return;
    }
    
    file = new File(DiasdemProject.getProperty(
    "DEFAULT_TEXT_UNIT_EVALUATION_LOG_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle(
    "Select New or Existing Log File for Personal Notes");
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences.TEXT_FILE_FILTER);
    result = GuiFileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      LogFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      DIAsDEMguiPreferences.TEXT_FILE_EXTENSION);
      CurrentProjectDirectory = GuiFileChooser.getCurrentDirectory();
      LogTextFile = new TextFile(LogFile);
      DiasdemProject.setProperty(
      "DEFAULT_TEXT_UNIT_EVALUATION_LOG_FILE", LogFile.getAbsolutePath());
      DiasdemProject.quickSave();
    }
    else {
      return;
    }
    
    InputTextFile.openReadOnly();
    CurrentTextUnit = InputTextFile.getFirstLine();
    while (CurrentTextUnit != null) {
      NumberOfTextUnitsToBeEvaluated++;
      CurrentTextUnit = InputTextFile.getNextLine();
    }
    
    NumberOfEvaluatedTextUnits = 0;
    NumberOfEvaluatedTextUnits_Offset = 0;
    OutputTextFile.open();
    if (OutputTextFile.getSize() == 0) {
      OutputTextFile.setNextLine("# TP,TN,FP,FN,Type,"
      + "DiasdemXmlTag,CorrectXmlTag,"
      + "DiasdemXmlTagIsMoreGeneral,DiasdemXmlTagIsMoreSpecific,"
      + "NumberOfExistingAttributeNEs,NumberOfComplCorrectAttributeNEs,"
      + "NumberOfPartCorrectAttributeNEs,NumberOfIncorrectAttributeNEs,"
      + "NumberOfMissingAttributeNEs,TextUnit");
    }
    else {
      PresentResultsOfXmlTagNames = new EvaluatedXmlTagNames();
      CsvItemizer itemizer = null;
      int truePositive = -1;
      int trueNegative = -1;
      int falsePositive = -1;
      int falseNegative = -1;
      String type = null;
      String diasdemXmlTag = null;
      String correctXmlTag = null;
      int diasdemXmlTagIsMoreGeneral = 0;
      int diasdemXmlTagIsMoreSpecific = 0;
      int trueNumberOfNamedEntities = 0;
      int numberOfComplCorrectNamedEntities = 0;
      int numberOfPartCorrectNamedEntities = 0;
      int numberOfIncorrectNamedEntities = 0;
      int numberOfMissingNamedEntities = 0;
      String line = OutputTextFile.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        NumberOfEvaluatedTextUnits_Offset++;
        itemizer = new CsvItemizer(line); 
        try {
          truePositive = CsvItemizer.itemToInt(itemizer.getNextItem());
          trueNegative = CsvItemizer.itemToInt(itemizer.getNextItem());
          falsePositive = CsvItemizer.itemToInt(itemizer.getNextItem());
          falseNegative = CsvItemizer.itemToInt(itemizer.getNextItem());
          type = CsvItemizer.itemToString(itemizer.getNextItem());
          diasdemXmlTag = CsvItemizer.itemToString(itemizer.getNextItem());
          correctXmlTag = CsvItemizer.itemToString(itemizer.getNextItem());
          diasdemXmlTagIsMoreGeneral = CsvItemizer.itemToInt(itemizer
          .getNextItem());
          diasdemXmlTagIsMoreSpecific = CsvItemizer.itemToInt(itemizer
          .getNextItem());
          trueNumberOfNamedEntities = CsvItemizer.itemToInt(itemizer
          .getNextItem());
          numberOfComplCorrectNamedEntities = CsvItemizer.itemToInt(itemizer
          .getNextItem());
          numberOfPartCorrectNamedEntities = CsvItemizer.itemToInt(itemizer
          .getNextItem());
          numberOfIncorrectNamedEntities = CsvItemizer.itemToInt(itemizer
          .getNextItem());
          numberOfMissingNamedEntities = CsvItemizer.itemToInt(itemizer
          .getNextItem());
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        if (truePositive == 1) {
          PresentResultsOfXmlTagNames.memorizeOneTextUnitEvaluation(
          EvaluatedXmlTagNames.TRUE_POSITIVE, false);
        }
        if (trueNegative == 1) {
          PresentResultsOfXmlTagNames.memorizeOneTextUnitEvaluation(
          EvaluatedXmlTagNames.TRUE_NEGATIVE, false);
        }
        if (falsePositive == 1) {
          PresentResultsOfXmlTagNames.memorizeOneTextUnitEvaluation(
          EvaluatedXmlTagNames.FALSE_POSITIVE, false);
        }
        if (falseNegative == 1) {
          PresentResultsOfXmlTagNames.memorizeOneTextUnitEvaluation(
          EvaluatedXmlTagNames.FALSE_NEGATIVE, false);
        }
        PresentResultsOfXmlTagAttributes.memorizeOneCompleteTextUnitEvaluation(
        trueNumberOfNamedEntities, numberOfComplCorrectNamedEntities,
        numberOfPartCorrectNamedEntities, numberOfIncorrectNamedEntities,
        numberOfMissingNamedEntities, false);
        line = OutputTextFile.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      OutputTextFile.close();
      OutputTextFile.open();
      PresentResultsOfXmlTagNames.fireTableDataChanged();
    }
    NeOutputTextFile.open();
    if (NeOutputTextFile.getSize() == 0) {
      NeOutputTextFile.setNextLine("# DiasdemXmlTag,Attribute,"
      + "NumberOfExistingAttributeNEs,NumberOfComplCorrectAttributeNEs,"
      + "NumberOfPartCorrectAttributeNEs,NumberOfIncorrectAttributeNEs,"
      + "NumberOfMissingAttributeNEs");
    }
    HtmlOutputTextFile.open();
    if (HtmlOutputTextFile.getSize() == 0) {
      Template header = new Template(Tools.stringFromTextualSystemResource(
      "html/HtmlFile_HeaderTemplate.html"));
      header.addValue("${Title}", "Protocol of Tagging Quality Evaluation");
      HtmlOutputTextFile.setNextLine(header.insertValues());
      HtmlOutputTextFile.setNextLine("<p>Created by Tools &gt; Tagging "
      + "Quality Evaluator 2.2 on " + Tools.getSystemDate() + "</p>");
      HtmlOutputTextFile.setNextLine(
      "<h3>Overview of Random Text Unit Sample</h3>");
      HtmlOutputTextFile.setNextLine("<table border=\"1\"><tr>"
      + "<th align=\"left\" valign=\"top\">Property</th>"
      + "<th align=\"left\" valign=\"top\">Value</th></tr>");
      HtmlOutputTextFile.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">DIAsDEM Project Name</td>"
      + "<td align=\"left\" valign=\"top\">" 
      + DiasdemProject.getProjectName() + "</td></tr>");
      HtmlOutputTextFile.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">DIAsDEM Project File</td>"
      + "<td align=\"left\" valign=\"top\">" 
      + Tools.shortenFileName(DiasdemProject.getProjectFileName(), 60)
      + "</td></tr>");
      HtmlOutputTextFile.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">Text Unit Sample File</td>"
      + "<td align=\"left\" valign=\"top\">" 
      + Tools.shortenFileName(InputFile.getAbsolutePath(), 60) + "</td></tr>");
      HtmlOutputTextFile.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">Evaluated Text Units File</td>"
      + "<td align=\"left\" valign=\"top\">" 
      + Tools.shortenFileName(OutputFile.getAbsolutePath(), 60) + "</td></tr>");
      HtmlOutputTextFile.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">Drawn Text Units</td>"
      + "<td align=\"left\" valign=\"top\">" 
      + (NumberOfTextUnitsToBeEvaluated + NumberOfEvaluatedTextUnits_Offset)
      + "</td></tr></table>");
    }
    HtmlResultTextFile.empty();
    HtmlResultTextFile.open();
    Template header = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_HeaderTemplate.html"));
    header.addValue("${Title}", "Results of Tagging Quality Evaluation");
    HtmlResultTextFile.setNextLine(header.insertValues());
    HtmlResultTextFile.setNextLine("<p>Created by Tools &gt; Tagging "
    + "Quality Evaluator 2.2 on " + Tools.getSystemDate() + "</p>");
    HtmlResultTextFile.setNextLine(
    "<h3>Overview of Random Text Unit Sample</h3>");
    HtmlResultTextFile.setNextLine("<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Property</th>"
    + "<th align=\"left\" valign=\"top\">Value</th></tr>");
    HtmlResultTextFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">DIAsDEM Project Name</td>"
    + "<td align=\"left\" valign=\"top\">"
    + DiasdemProject.getProjectName() + "</td></tr>");
    HtmlResultTextFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">DIAsDEM Project File</td>"
    + "<td align=\"left\" valign=\"top\">"
    + Tools.shortenFileName(DiasdemProject.getProjectFileName(), 60)
    + "</td></tr>");
    HtmlResultTextFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Text Unit Sample File</td>"
    + "<td align=\"left\" valign=\"top\">"
    + Tools.shortenFileName(InputFile.getAbsolutePath(), 60) + "</td></tr>");
    HtmlResultTextFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Evaluated Text Units File</td>"
    + "<td align=\"left\" valign=\"top\">"
    + Tools.shortenFileName(OutputFile.getAbsolutePath(), 60) + "</td></tr>");
    HtmlResultTextFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Drawn Text Units</td>"
    + "<td align=\"left\" valign=\"top\">"
    + (NumberOfTextUnitsToBeEvaluated + NumberOfEvaluatedTextUnits_Offset)
    + "</td></tr>");
    RestTextFile.empty();
    RestTextFile.open();
    
    ControlPanelContainer.setTitle(this.getPreferredTitle() + " [" + Tools
    .shortenFileName(InputFile.getAbsolutePath(), 50)  + "]");

    // update CorrectElement_Combo
    this.removeAll();
    this.validate();
    this.initialize();
    this.validate();
    
    CurrentTextUnit = InputTextFile.getFirstLine();
    NumberOfCurrentTextUnit = 0;
    this.enableButtons();
    Button_Panel.setEnabled(0, false);  // Start
    Button_Panel.setEnabled(4, true);  // Log
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void enableButtons() {
    
    WaitForUserInput = false;
    if (CurrentTextUnit == null || CurrentTextUnit.length() == 0) {
      // preceding text unit was the last one in the sample 
      this.setControlPanelContainerClosed(true);
      return;
    }
    NumberOfCurrentTextUnit++;
    String fileName = CurrentTextUnit.substring(0,
    Math.max(0, CurrentTextUnit.indexOf(" ")));
    String textUnit = CurrentTextUnit.substring(
    Math.max(0, CurrentTextUnit.indexOf(" ") + 1));  // text unit ID
    
    XmlStartTag = textUnit.substring(0, Math.max(0, textUnit.indexOf(">") + 1));
    TaggedContent = textUnit.substring(Math.max(0, textUnit.indexOf(">") + 1), 
    Math.max(0, textUnit.indexOf("</")));
    XmlEndTag = textUnit.substring(Math.max(0, textUnit.indexOf("</")));
    this.displayTextUnit(XmlStartTag + TaggedContent + XmlEndTag);
    
    NumberOfExtractedNEs = 0;
    if (Tools.stringIsNullOrEmpty(XmlStartTag)) {
      ElementName = "";
      NumberOfExtractedNEs = 0;
    }
    else {
      // extract element name from start tag
      if (XmlStartTag.indexOf(" ") >= 0) {
        ElementName = XmlStartTag.substring(1, textUnit.indexOf(" ", 0)).trim();
        // determine the number of attributes: number of occurrences of =" plus
        // number of occurrences of [AND]
        NumberOfExtractedNEs = this.getNumberOfExtractedNEs(XmlStartTag);
      }
      else {
        ElementName = XmlStartTag.substring(1, textUnit.indexOf(">", 0)).trim();
        // tag does not contain attributes
        NumberOfExtractedNEs = 0;
      }
    }
    AttributeNames = MyConceptualDtd.getElementAttributesNames(ElementName);
    if (AttributeNames != null) {
      AttributeEvaluationResults = new int[AttributeNames.length][5];
      for (int i = 0; i < AttributeNames.length; i++) {
        for (int j = 0; j < 5; j++) {
          AttributeEvaluationResults[i][j] = 0;
        }
        AttributeEvaluationResults[i][EvaluatedNamedEntities.NUMBER_OF_NES]
        = this.getNumberOfExtractedNEs(AttributeNames[i], XmlStartTag);
        AttributeEvaluationResults[i][EvaluatedNamedEntities.COMPL_CORRECT]
        = this.getNumberOfExtractedNEs(AttributeNames[i], XmlStartTag);
      }
    }
    else {
      AttributeEvaluationResults = null;
    }
    MyEvaluatedNamedEntities.reset(AttributeNames, AttributeEvaluationResults);

    Metadata_Text.setText(NumberOfCurrentTextUnit + "/" 
    + NumberOfTextUnitsToBeEvaluated + "; DIAsDEM Document ID: " + fileName);
    Metadata_Text.setCaretAtEnding();
    
    if (Tools.stringIsNullOrEmpty(ElementName)) {
      CorrectElement_Combo.setSelectedString("[No Semantic XML Tag]");
    }
    else {
      CorrectElement_Combo.setSelectedString(ElementName);
    }
    
    XmlTagTooGeneral_CheckBox.setSelected(false);
    XmlTagTooSpecific_CheckBox.setSelected(false);
    XmlTagTooGeneral_CheckBox.setEnabled(false);
    XmlTagTooSpecific_CheckBox.setEnabled(false);
    
    Button_Panel.setEnabled(1, true);  // True
    if ((CurrentTextUnit != null) && (textUnit.startsWith("<"))) {
      Button_Panel.setEnabled(2, true);  // FP
      Button_Panel.setEnabled(3, false);  // FN
    }
    else {
      Button_Panel.setEnabled(2, false);  // FP
      Button_Panel.setEnabled(3, true);  // FN
    }
    
    TaggedUntaggedTextUnit = textUnit;
    DiasdemDocumentID = fileName;
    this.updateResults();
    
    WaitForUserInput = true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void updateResults() {

        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void initAttributeSets() {
    
    TagAttributes = new SimpleAttributeSet();
    StyleConstants.setForeground(TagAttributes, Color.blue);

    AttributeValueAttributes = new SimpleAttributeSet();

    CdataAttributes = new SimpleAttributeSet();
    StyleConstants.setFontFamily(CdataAttributes, "Courier");

    CharacterAttributes = new SimpleAttributeSet();
    StyleConstants.setBold(CharacterAttributes, true);

    ElementAttributes = new SimpleAttributeSet();
    StyleConstants.setForeground(ElementAttributes, new Color(153,0,0));
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void initPatterns() {

    // REX/Javascript 1.0 
    // Robert D. Cameron "REX: XML Shallow Parsing with Regular Expressions",
    // Technical Report TR 1998-17, School of Computing Science, Simon Fraser 
    // University, November, 1998.
    // Copyright (c) 1998, Robert D. Cameron. 
    // The following code may be freely used and distributed provided that
    // this copyright and citation notice remains intact and that modifications
    // or additions are clearly identified.
    
    // modified to fit java.util.regex.

    String textSE = "[^<]+";
    String untilHyphen = "[^-]*-";
    String until2Hyphens = untilHyphen + "([^-]" + untilHyphen + ")*-";
    String commentCE = until2Hyphens + ">?";
    String untilRSBs = "[^]]*]([^]]+])*]+";
    String cDATA_CE = untilRSBs + "([^]>]" + untilRSBs + ")*>";
    String s = "[ \\n\\t\\r]+";
    String nameStrt = "[A-Za-z_:]|[^\\x00-\\x7F]";
    String nameChar = "[A-Za-z0-9_:.-]|[^\\x00-\\x7F]";
    String name = "(" + nameStrt + ")(" + nameChar + ")*";
    String quoteSE = "\"[^\"]" + "*" + "\"" + "|'[^']*'";
    String dt_IdentSE = s + name + "(" + s + "(" + name + "|" + quoteSE + "))*";
    String markupDeclCE = "([^]\"'><]+|" + quoteSE + ")*>";
    String s1 = "[\\n\\r\\t ]";
    String untilQMs = "[^?]*\\?+";
    String pi_Tail = "\\?>|" + s1 + untilQMs + "([^>?]" + untilQMs + ")*>";
    String dt_ItemSE = "<(!(--" + until2Hyphens + ">|[^-]" 
    + markupDeclCE + ")|\\?" + name + "(" + pi_Tail + "))|%" + name + ";|" + s;
    String docTypeCE = dt_IdentSE + "(" + s + ")?(\\[(" + dt_ItemSE 
    + ")*](" + s + ")?)?>?";
    String declCE = "--(" + commentCE + ")?|\\[CDATA\\[(" + cDATA_CE 
    + ")?|DOCTYPE(" + docTypeCE + ")?";
    String pi_CE = name + "(" + pi_Tail + ")?";
    String endTagCE = name + "(" + s + ")?>?";
    String attValSE = "\"[^<\"]" + "*" + "\"" + "|'[^<']*'";
    String elemTagCE = name + "(" + s + name + "(" + s + ")?=(" + s + ")?(" 
    + attValSE + "))*(" + s + ")?/?>?";
    String markupSPE = "<(!(" + declCE + ")?|\\?(" + pi_CE + ")?|/(" + endTagCE
    + ")?|(" + elemTagCE + ")?)";
    String xml_SPE = textSE + "|" + markupSPE;
    
    PartPattern = Pattern.compile(xml_SPE);
    NamePattern = Pattern.compile(name);
    AttributePattern = Pattern.compile(attValSE);
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void displayTextUnit(String pTextUnit) {

    TaggedTextUnit_Pane.setText(pTextUnit);

    String text = TaggedTextUnit_Pane.getText();
    NameMatcher = NamePattern.matcher(text);
    PartMatcher = PartPattern.matcher(text);
    AttributeMatcher = AttributePattern.matcher(text);
    while (PartMatcher.find()) {
      formatPart(PartMatcher);
    }
    TaggedTextUnit_Pane.setCaretPosition(0);
    TaggedTextUnit_Pane.revalidate();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void formatPart(Matcher partMatcher) {
    
    String part = partMatcher.group();
    if (!part.startsWith("<")) {
      applyAttributeSet(partMatcher, CharacterAttributes);
    }
    else {
      applyAttributeSet(partMatcher, TagAttributes);
      if (!part.startsWith("<?") && !part.startsWith("<!")) {
        formatElement(partMatcher);
      }
      if (part.startsWith("<![CDATA[") && part.endsWith("]]>")) {
        applyAttributeSet(partMatcher, CdataAttributes, 9, 12);
      }
    }
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private void formatElement(Matcher partMatcher) {
    
    NameMatcher.find(partMatcher.start());
    do {
      applyAttributeSet(NameMatcher, ElementAttributes);
      if (! NameMatcher.find()) {
        break;
      }
    }
    while(NameMatcher.end() < partMatcher.end());
    
    if (!AttributeMatcher.find(partMatcher.start())) {
      return;
    }
    do {
      applyAttributeSet(AttributeMatcher, AttributeValueAttributes, 1, 2);
      if (! AttributeMatcher.find()) {
        break;
      }
    }
    while(AttributeMatcher.end() < partMatcher.end());
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void applyAttributeSet(Matcher pM, AttributeSet pAttributeSet) {
    
    applyAttributeSet(pM, pAttributeSet, 0, 0);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void applyAttributeSet(Matcher pM, AttributeSet pAttributeSet, 
  int pAdditionalOffset, int pLengthReduction) {
    
    int offset = pM.start();
    int length = pM.group().length();
    TaggedTextUnit_Pane.getStyledDocument().
    setCharacterAttributes(offset + pAdditionalOffset,
    length - pLengthReduction, pAttributeSet, true);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private int getNumberOfExtractedNEs(String pXmlStartTag) {
    
    int result = 0;
    String[] split = pXmlStartTag.split("=\"");
    if (split != null) {
      result += split.length - 1;
    }
    split = pXmlStartTag.split("\\[AND\\]");
    if (split != null) {
      result += split.length - 1;
    }   
    
    return result;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private int getNumberOfExtractedNEs(String pAttributName,
  String pXmlStartTag) {
    
    int result = 0;    
    int start = pXmlStartTag.indexOf(pAttributName + "=\"");
    int end = -1;
    String attribute = null;
    
    if (start < 0) {
      return result;
    }   
    start += pAttributName.length() + 2;
    end = pXmlStartTag.indexOf("\"", start);
    if (start < 0) {
      return result;
    }   
    result = 1;
    attribute = pXmlStartTag.substring(start + 1, end);
    String[] split = attribute.split("\\[AND\\]");
    if (split != null) {
      result += split.length - 1;
    }   
    
    return result;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}