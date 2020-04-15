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

package org.hypknowsys.diasdem.client.gui.tools.assessmentOfTaggingQuality;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskControlPanelContainer;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.diasdem.core.DIAsDEMpreliminaryDtd;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMpreliminaryDtd;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KBorderPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KFileFilter;
import org.hypknowsys.misc.swing.KScrollTextArea;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;


/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class AssessmentOfTaggingQualityControlPanel
extends DiasdemActionsControlPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private File CurrentProjectDirectory = null;
  private File InputFile = null;
  private File OutputFile = null;
  private File RestFile = null;
  private File LogFile = null;
  private DIAsDEMpreliminaryDtd MyPriliminaryDtd = null;
  
  private KBorderPanel Results_Panel = null;
  private KBorderPanel TextUnit_Panel = null;
  private KButtonPanel Button_Panel = null;
  private KBorderPanel Dtd_Panel = null;
  private KBorderPanel Left_Panel = null;
  private KScrollTextArea TextUnit_ScrollTextArea = null;
  private KScrollTextArea Dtd_ScrollTextArea = null;
  private KTextField Results_TextField = null;
  
  private TextFile InputTextFile = null;
  private TextFile OutputTextFile = null;
  private TextFile RestTextFile = null;
  private TextFile LogTextFile = null;
  
  private String CurrentTextUnit = null;
  private String TaggedUntaggedTextUnit = null;
  private String XmlDocumentFileName = null;
  private int NumberOfTextUnitsToBeEvaluated = 0;
  private int NumberOfCurrentTextUnit = 0;
  private int TruePositives = 0;
  private int TrueNegatives = 0;
  private int FalsePositives = 0;
  private int FalseNegatives = 0;
  
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
  
  public AssessmentOfTaggingQualityControlPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AssessmentOfTaggingQualityControlPanel(Server pDiasdemServer,
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
    
    if (ActionCommand.equals("Stop")) {
      this.setControlPanelContainerClosed(true);
    }
    else if (ActionCommand.equals("True")) {
      if (TaggedUntaggedTextUnit.startsWith("<")) {
        OutputTextFile.setNextLine("1,0,0,0,\"TP\",\""
        + CurrentTextUnit + "\"");
        TruePositives++;
      }
      else {
        OutputTextFile.setNextLine("0,1,0,0,\"TN\",\""
        + CurrentTextUnit + "\"");
        TrueNegatives++;
      }
      CurrentTextUnit = InputTextFile.getNextLine();
      this.enableButtons();
    }
    else if (ActionCommand.equals("FalsePos")) {
      OutputTextFile.setNextLine("0,0,1,0,\"FP\",\""
      + CurrentTextUnit + "\"");
      FalsePositives++;
      CurrentTextUnit = InputTextFile.getNextLine();
      this.enableButtons();
    }
    else if (ActionCommand.equals("FalseNeg")) {
      OutputTextFile.setNextLine("0,0,0,1,\"FN\",\""
      + CurrentTextUnit + "\"");
      FalseNegatives++;
      CurrentTextUnit = InputTextFile.getNextLine();
      this.enableButtons();
    }
    else if (ActionCommand.equals("Log")) {
      String message = JOptionPane.showInputDialog(DiasdemGui.getJFrame(),
      "Please enter your comment concerning the current text unit:",
      ControlPanelContainer.getTitle(), JOptionPane.QUESTION_MESSAGE);
      if (message != null) {
        LogTextFile.open();
        LogTextFile.setNextLine("Document ID:  " + XmlDocumentFileName);
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
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskControlPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
    
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
    
    JPanel edit_Panel = new JPanel();
    edit_Panel.setLayout(new GridLayout(1, 2, 12, 0));
    edit_Panel.add(Left_Panel);
    edit_Panel.add(Dtd_Panel);
    
    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
    this.setLayout(new BorderLayout());
    this.add(edit_Panel, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setControlPanelContainer(TaskControlPanelContainer
  pControlPanelContainer) {
    
    ControlPanelContainer = pControlPanelContainer;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void finalize() throws PropertyVetoException {
    
    int confirm = JOptionPane.showConfirmDialog(this,
    "Do you want to stop this assessment session?\n"
    + "Note, all remaining text units will be saved\n"
    + "for assessment in a future session.",
    this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
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
      super.finalize();
    }
    else {
      throw new PropertyVetoException("Close operation canceled!",
      new PropertyChangeEvent(this, "close", null, null));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Assessment of Tagging Quality";
    
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
    
    Results_TextField = new KTextField("Results: 0 TP, 0 TN, 0 FP, 0 FN");
    Results_TextField.setEditable(false);
    Results_Panel = new KBorderPanel(4, 2, 11, 2);
    Results_Panel.addCenter(Results_TextField);
    
    TextUnit_ScrollTextArea = new KScrollTextArea();
    TextUnit_ScrollTextArea.setTextAreaLineWrap(true);
    TextUnit_ScrollTextArea.setCaretAtBeginning();
    TextUnit_Panel = new KBorderPanel(0, 0, 0, 0,
    "Text Unit to be Assessed:", 12, 12, 11, 11);
    TextUnit_Panel.addCenter(TextUnit_ScrollTextArea);
    
    Left_Panel = new KBorderPanel(0, 0, 0, 0);
    Left_Panel.addNorth(Results_Panel);
    Left_Panel.addCenter(TextUnit_Panel);
    
    Dtd_ScrollTextArea = new KScrollTextArea();
    Dtd_ScrollTextArea.setTextAreaEditable(false);
    Dtd_ScrollTextArea.setCaretAtBeginning();
    Dtd_Panel = new KBorderPanel(0, 0, 0, 0,
    "Elements of Preliminary DTD:", 12, 12, 11, 11);
    Dtd_Panel.addCenter(Dtd_ScrollTextArea);
    
    Parameter_Panel = null;
    
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
    "Select New or Existing File of Evaluated Text Units");
    GuiFileChooser.setFileFilter(
    DIAsDEMguiPreferences.EVALUATED_TEXT_UNIT_SAMPLE_FILE_FILTER);
    result = GuiFileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      OutputFile = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      DIAsDEMguiPreferences.EVALUATED_TEXT_UNIT_SAMPLE_FILE_EXTENSION);
      CurrentProjectDirectory = GuiFileChooser.getCurrentDirectory();
      OutputTextFile = new TextFile(OutputFile);
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
      DiasdemProject.setProperty(
      "DEFAULT_REMAINING_TEXT_UNIT_SAMPLE_FILE", RestFile.getAbsolutePath());
      DiasdemProject.quickSave();
    }
    else {
      return;
    }
    
    file = new File(DiasdemProject.getProperty(
    "DEFAULT_PRELIMINARY_DTD_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Existing Preliminary DTD File");
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences
    .PRELIMINARY_DTD_FILE_FILTER);
    result = GuiFileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File tagFile = GuiFileChooser.getSelectedFile();
      CurrentProjectDirectory = GuiFileChooser.getCurrentDirectory();
      try {
        MyPriliminaryDtd = new DefaultDIAsDEMpreliminaryDtd(tagFile
        .getAbsolutePath(), DefaultDIAsDEMpreliminaryDtd.LOAD);
      }
      catch (IOException e) { return; }
      TreeSet elementNames = MyPriliminaryDtd.getElementNames();
      Iterator iterator = elementNames.iterator();
      Dtd_ScrollTextArea.setText("");
      while (iterator.hasNext()) {
        Dtd_ScrollTextArea.appendText((String)iterator.next()  + "\n");
      }
      Dtd_ScrollTextArea.setCaretAtBeginning();
      Dtd_Panel.revalidate();
      DiasdemProject.setProperty(
      "DEFAULT_PRELIMINARY_DTD_FILE", tagFile.getAbsolutePath());
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
    
    OutputTextFile.open();
    if (OutputTextFile.getSize() == 0) {
      OutputTextFile.setNextLine("# TP,TN,FP,FN,Type,TextUnit");
    }
    else {
      String line = OutputTextFile.getFirstLineButIgnoreCommentsAndEmptyLines();
      while (line != null) {
        String currentResult =
        line.substring(0, Math.max(0, line.indexOf(",\"")));
        StringTokenizer tokenizer = new StringTokenizer(currentResult, ",");
        int i = 1;
        while (tokenizer.hasMoreTokens()) {
          if (i == 1) {
            TruePositives += Tools.string2Int(tokenizer.nextToken());
          }
          if (i == 2) {
            TrueNegatives += Tools.string2Int(tokenizer.nextToken());
          }
          if (i == 3) {
            FalsePositives += Tools.string2Int(tokenizer.nextToken());
          }
          if (i == 4) {
            FalseNegatives += Tools.string2Int(tokenizer.nextToken());
          }
          i++;
        }
        line = OutputTextFile.getNextLineButIgnoreCommentsAndEmptyLines();
      }
      OutputTextFile.close();
      OutputTextFile.open();
    }
    RestTextFile.empty();
    RestTextFile.open();
    
    CurrentTextUnit = InputTextFile.getFirstLine();
    NumberOfCurrentTextUnit = 0;
    this.enableButtons();
    Button_Panel.setEnabled(0, false);  // Start
    Button_Panel.setEnabled(4, true);  // Log
    
    ControlPanelContainer.setTitle(this.getPreferredTitle() + " [" + Tools
    .shortenFileName(InputFile.getAbsolutePath(), 50)  + "]");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void enableButtons() {
    
    if (CurrentTextUnit == null || CurrentTextUnit.length() == 0) {
      OutputTextFile.close();
      this.setControlPanelContainerClosed(true);
    }
    NumberOfCurrentTextUnit++;
    String fileName = CurrentTextUnit.substring(0,
    Math.max(0, CurrentTextUnit.indexOf(" ")));
    String textUnit = CurrentTextUnit.substring(
    Math.max(0, CurrentTextUnit.indexOf(" ") + 1));
    
    TextUnit_ScrollTextArea.setText(NumberOfCurrentTextUnit + "/"
    + NumberOfTextUnitsToBeEvaluated + " - " + fileName + "\n\n"
    + textUnit.substring(0, Math.max(0, textUnit.indexOf(">") + 1))
    + "\n\n" + textUnit.substring(Math.max(0, textUnit.indexOf(">") + 1),
    Math.max(0, textUnit.indexOf("</"))) + "\n\n" + textUnit.substring(
    Math.max(0, textUnit.indexOf("</"))));
    TextUnit_ScrollTextArea.setCaretAtBeginning();
    TextUnit_Panel.revalidate();
    
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
    XmlDocumentFileName = fileName;
    Results_TextField.setText("Results: " + TruePositives + " TP, "
    + TrueNegatives + " TN, " + FalsePositives + " FP, "
    + FalseNegatives + " FP");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}