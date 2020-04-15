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

package org.hypknowsys.diasdem.client.gui.tools.clusterLabelEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskControlPanelContainer;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.diasdem.core.DIAsDEMpreliminaryDtd;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMpreliminaryDtd;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurus;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KBorderPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KComboBox;
import org.hypknowsys.misc.swing.KFileFilter;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;


/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ClusterLabelEditorControlPanel extends DiasdemActionsControlPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private File CurrentProjectDirectory = null;
  private File CurrentParameterDirectory = null;
  private File CurrentClusterLabelFile = null;
  
  private String[] ClusterIDs = null;
  private KTextField[] ClusterNames = null;
  private KComboBox[] ClusterNames_Combo = null;
  private KComboBox[] QualityDecision_Combo = null;
  private KBorderPanel Thesaurus_Panel = null;
  private KGridBagPanel ClusterLabel_Panel = null;
  private JScrollPane Thesaurus_ScrollPane = null;
  private KBorderPanel Main_Panel = null;
  
  private DIAsDEMthesaurus CurrentThesaurus = null;
  private DIAsDEMthesaurusTerm CurrentTerm = null;
  private boolean ThesaurusModified = false;
  private boolean ThesaurusIsLoaded = false;
  private int MinClusterID = 0;  // to ensure max. 512 cluster labels in editor
  private int MaxClusterID = 0;  // to ensure max. 512 cluster labels in editor
  
  private TreeSet ProposedTagsSet = null;
  private String[] ProposedTagsArray = null;
  
  // metadata creation
  private int MetadataNumberOfTextUnits = 0;
  private int MetadataNumberOfClusters = 0;
  private int MetadataNumberOfTextUnitsAA = 0;
  private int MetadataNumberOfClustersAA = 0;
  private int MetadataNumberOfTextUnitsAU = 0;
  private int MetadataNumberOfClustersAU = 0;
  private int MetadataNumberOfTextUnitsAQ = 0;
  private int MetadataNumberOfClustersAQ = 0;
  private int MetadataNumberOfTextUnitsUA = 0;
  private int MetadataNumberOfClustersUA = 0;
  private int MetadataNumberOfTextUnitsUU = 0;
  private int MetadataNumberOfClustersUU = 0;
  private int MetadataNumberOfTextUnitsUQ = 0;
  private int MetadataNumberOfClustersUQ = 0;
  private int MetadataNumberOfTextUnitsQQ = 0;
  private int MetadataNumberOfClustersQQ = 0;
  private int MetadataNumberOfTextUnitsQA = 0;
  private int MetadataNumberOfClustersQA = 0;
  private int MetadataNumberOfTextUnitsQU = 0;
  private int MetadataNumberOfClustersQU = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient int TmpInt = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterLabelEditorControlPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterLabelEditorControlPanel(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences, String pFileName) {
    
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
    ControlPanelContainerIsVisible = false;
    this.initialize();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public File getClusterLabelFile() {
    return CurrentClusterLabelFile; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setClusterLabelFile(File pClusterLabelFile) {
    CurrentClusterLabelFile = pClusterLabelFile; }
  
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
    
    if (ActionSource != null && ActionSource instanceof KTextField) {
      ThesaurusModified = true;
    } 
    else if (ThesaurusIsLoaded && ActionSource != null
    && ActionSource instanceof KComboBox && ActionCommand != null 
    && Tools.isInt(ActionCommand)) {
      TmpInt = Tools.string2Int(ActionCommand);
      if (QualityDecision_Combo[TmpInt] != null 
      && QualityDecision_Combo[TmpInt].getItemCount() == 3 
      && QualityDecision_Combo[TmpInt].getSelectedString().equals("a/u")) {
        if (ProposedTagsArray == null) {
          if (ClusterNames[TmpInt].getText().startsWith("DEFAULT")) {
            ClusterNames[TmpInt].setText("");
          }
        }
        else {
          if (ClusterNames_Combo[TmpInt].getSelectedString()
          .startsWith("DEFAULT")) {
            ClusterNames_Combo[TmpInt].setSelectedString("");
          }
        }
        ThesaurusModified = true;
      }
    }
    else if (ActionCommand.equals("ThesaurusExit")) {
      this.setControlPanelContainerClosed(true);
    }
    else if (ActionCommand.equals("ThesaurusOpen")) {
      this.openThesaurus();
    }
    else if (ActionCommand.equals("ThesaurusSave")) {
      this.saveThesaurus();
    }
    else if (ActionCommand.equals("ThesaurusSaveAs")) {
      this.saveAsThesaurus();
    }
    else if (ActionCommand.equals("OpenProposedTags")) {
      this.openProposedTags();
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
    if (Tools.isValidandWriteableDirectoryName(
    DiasdemGuiPreferences.getMruParameterDirectory())) {
      CurrentParameterDirectory = new File(
      DiasdemGuiPreferences.getMruParameterDirectory());
    }
    if (DiasdemProject != null && Tools.isValidandWriteableDirectoryName(
    DiasdemProject.getProjectDirectory())) {
      CurrentProjectDirectory = new File(
      DiasdemProject.getProjectDirectory());
    }
    if (DiasdemProject != null && Tools.isValidandWriteableDirectoryName(
    DiasdemProject.getParameterDirectory())) {
      CurrentParameterDirectory = new File(
      DiasdemProject.getParameterDirectory());
    }
    
    ProposedTagsSet = new TreeSet();
    this.createButtonPanel();
    this.createParameterPanel();
    
    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY));
    
    Thesaurus_ScrollPane = new JScrollPane(ClusterLabel_Panel);
    Thesaurus_Panel = new KBorderPanel(0, 0, 0, 0);
    Thesaurus_Panel.addCenter(Thesaurus_ScrollPane);
    
    Main_Panel = new KBorderPanel(12, 12, 11, 11);
    Main_Panel.addCenter(Thesaurus_Panel);
    Main_Panel.addSouth(Button_Panel);
    
    this.removeAll();
    this.validate();
    this.setLayout(new BorderLayout());
    this.add(Main_Panel, BorderLayout.CENTER);
    this.validate();
    
    if (CurrentClusterLabelFile != null) {
      CurrentThesaurus = new DefaultDIAsDEMthesaurus();
      CurrentThesaurus.load(CurrentClusterLabelFile.getAbsolutePath());
      boolean commit = true;
      if (CurrentThesaurus.getSize() > 512) {
        String minMaxChoice = JOptionPane.showInputDialog(this,
        "The specified cluster label file contains more than\n"
        + "512 labels. Therefore, this file cannot be edited\n"
        + "completely in Cluster Label Editor. Please input a\n"
        + "range of cluster IDs of clusters to be edited now!\n"
        + "The maximum cluster ID is probably " 
        + (CurrentThesaurus.getSize() - 1) + ". Example:\n"
        + "Enter '0-511' to edit the first 512 clusters labels.",
        this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);
        commit = this.setMinMaxClusterIDs(minMaxChoice);
      }
      else {
        MinClusterID = 0;
        MaxClusterID = CurrentThesaurus.getSize();
      }
      if (commit) {
        Button_Panel.setEnabled(1, true);  // Save
        Button_Panel.setEnabled(2, true);  // SaveAs
        Button_Panel.setEnabled(3, true);  // Tags
        ThesaurusModified = false;
        this.updateView();
        CurrentProjectDirectory = CurrentClusterLabelFile.getParentFile();
        if (ControlPanelContainer != null) {
          ControlPanelContainer.setTitle(this.getPreferredTitle() + " [" 
          + Tools.shortenFileName(CurrentClusterLabelFile.getAbsolutePath(), 
          50)  + "]");
        }
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setControlPanelContainer(TaskControlPanelContainer
  pControlPanelContainer) {
    
    ControlPanelContainer = pControlPanelContainer;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void finalize() throws PropertyVetoException {
    
    if (ThesaurusModified) {
      int confirm = JOptionPane.showConfirmDialog(this,
      "Do you want to save the\ncurrent cluster label file?",
      this.getPreferredTitle(), JOptionPane.YES_NO_CANCEL_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        this.saveThesaurus();
      }
      else if (confirm == JOptionPane.CANCEL_OPTION) {
        throw new PropertyVetoException("Close operation canceled!",
        new PropertyChangeEvent(this, "close", null, null));
      }
    }
    super.finalize();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Cluster Label Editor";
    
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
    Button_Panel.addFirstButton("Open",
    KeyEvent.VK_O, true, false, "ThesaurusOpen", this);
    Button_Panel.addNextButton("Save",
    KeyEvent.VK_S, false, false, "ThesaurusSave", this);
    Button_Panel.addNextButton("Save As",
    KeyEvent.VK_A, false, false, "ThesaurusSaveAs", this);
    Button_Panel.addNextButton("Tags",
    KeyEvent.VK_T, false, false, "OpenProposedTags", this);
    Button_Panel.addNextButton("Exit",
    KeyEvent.VK_X, true, false, "ThesaurusExit", this);
    Button_Panel.addLastButton("Help",
    KeyEvent.VK_H, false, false, "ThesaurusHelp", this);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    ClusterLabel_Panel = new KGridBagPanel(12, 12, 11, 11);
    ClusterLabel_Panel.addLabel("Cluster", 0, 0);
    ClusterLabel_Panel.addBlankColumn(1, 0, 12);
    ClusterLabel_Panel.addLabel("Quality", 2, 0);
    ClusterLabel_Panel.addBlankColumn(3, 0, 12);
    ClusterLabel_Panel.addLabel("Semantic Cluster Label", 4, 0);
    ClusterLabel_Panel.addBlankRow(0, 1, 3, 1, 1.0, 1.0, 11);
    
    Thesaurus_ScrollPane = new JScrollPane(ClusterLabel_Panel);
    Thesaurus_Panel = new KBorderPanel(0, 0, 0, 0);
    Thesaurus_Panel.addCenter(Thesaurus_ScrollPane);
    
    Parameter_Panel = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void openThesaurus() {
    
    if (ThesaurusModified) {
      int confirm = JOptionPane.showConfirmDialog(this,
      "Do you want to save the\ncurrent cluster label file?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        this.saveThesaurus();
      }
    }
    
    File file = new File(DiasdemProject.getProperty(
    "DEFAULT_CLUSTER_LABEL_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      if (CurrentProjectDirectory != null) {
        GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
      }
      else {
        GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
        "DEFAULT_CLUSTER_LABEL_FILE"));
      }
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences
    .CLUSTER_LABEL_FILE_FILTER);
    GuiFileChooser.setDialogTitle("Select Existing Cluster Label File");
    int vResult = GuiFileChooser.showOpenDialog(this);
    if (vResult == JFileChooser.APPROVE_OPTION) {
      CurrentClusterLabelFile = GuiFileChooser.getSelectedFile();
      CurrentProjectDirectory = GuiFileChooser.getCurrentDirectory();
      CurrentThesaurus = new DefaultDIAsDEMthesaurus();
      CurrentThesaurus.load(CurrentClusterLabelFile.getAbsolutePath());
      boolean commit = true;
      if (CurrentThesaurus.getSize() > 512) {
        String minMaxChoice = JOptionPane.showInputDialog(this,
        "The specified cluster label file contains more than\n"
        + "512 labels. Therefore, this file cannot be edited\n"
        + "completely in Cluster Label Editor. Please input a\n"
        + "range of cluster IDs of clusters to be edited now!\n"
        + "The maximum cluster ID is probably " 
        + (CurrentThesaurus.getSize() - 1) + ". Example:\n"
        + "Enter '0-511' to edit the first 512 clusters labels.",
        this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);
        commit = this.setMinMaxClusterIDs(minMaxChoice);
      }
      else {
        MinClusterID = 0;
        MaxClusterID = CurrentThesaurus.getSize();
      }
      if (commit) {
        Button_Panel.setEnabled(1, true);  // Save
        Button_Panel.setEnabled(2, true);  // SaveAs
        Button_Panel.setEnabled(3, true);  // Tags
        ThesaurusModified = false;
        this.updateView();
        String clusterIDs = "";
        if (MaxClusterID > 0) {
          clusterIDs = "; Clusters: " + MinClusterID + "-" + MaxClusterID;
        }
        ControlPanelContainer.setTitle(this.getPreferredTitle() + " [" 
        + Tools.shortenFileName(CurrentClusterLabelFile.getAbsolutePath(),
        50) + clusterIDs + "]");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void saveThesaurus() {
    
    if (CurrentThesaurus != null && CurrentClusterLabelFile != null) {
      this.resetMetadata();
      boolean containsDefaultLabels = false;
      for (int i = 0; i < CurrentThesaurus.getSize(); i++) {
        CurrentTerm = CurrentThesaurus.get("" + i);
        if (i >= MinClusterID && i <= MaxClusterID) {
          CurrentTerm.setSynonyms(QualityDecision_Combo[i].getSelectedString());
          if (ClusterNames != null) {
            if (ClusterNames[i].getText().trim().length() > 0) {
              CurrentTerm.setScopeNotes(Tools.createAsciiAttributeName(
              ClusterNames[i].getText().trim(), 1000, "", "", "-="));
              if (ClusterNames[i].getText().startsWith("DEFAULT_")) {
                containsDefaultLabels = true;
              }
            }
            else {
              CurrentTerm.setScopeNotes("-");
            }
          }
          else {
            if (ClusterNames_Combo[i].getSelectedString().trim().length() > 0) {
              CurrentTerm.setScopeNotes(Tools.createAsciiAttributeName(
              ClusterNames_Combo[i].getSelectedString().trim(), 1000, "", "",
              "-"));
              if (ClusterNames_Combo[i].getSelectedString()
              .startsWith("DEFAULT_")) {
                containsDefaultLabels = true;
              }
            }
            else {
              CurrentTerm.setScopeNotes("-");
            }
          }
          if (ClusterNames != null) {
            ClusterNames[i].setText(CurrentTerm.getScopeNotes());
          }
          else {
            ClusterNames_Combo[i].removeItemAt(0);
            ClusterNames_Combo[i].insertItemAt(CurrentTerm.getScopeNotes(), 0);
            ClusterNames_Combo[i].setSelectedIndex(0);
          }
        }
        this.updateMetadata(CurrentTerm.getSynonyms(),  // SY contains decision
        CurrentTerm.getBroaderTerm());  // BT contains number of text units
      }
      
      boolean commit = true;
      if (containsDefaultLabels && DiasdemGuiPreferences.getShowWarnings()) {
        int confirm = JOptionPane.showConfirmDialog(this,
        "This cluster label file contains default cluster\n"
        + "labels. Do you really want to save this file?",
        this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.NO_OPTION) {
          commit = false;
        }
      }
      if (commit) {
        CurrentThesaurus.save(CurrentClusterLabelFile.getAbsolutePath());
        this.writeMetadataFile();
        ThesaurusModified = false;
        this.validate();
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void saveAsThesaurus() {
    
    if (CurrentThesaurus != null) {
      if (CurrentProjectDirectory != null) {
        GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
      }
      else {
        GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
        "PROJECT_DIRECTORY"));
      }
      GuiFileChooser.setFileFilter(DIAsDEMguiPreferences
      .CLUSTER_LABEL_FILE_FILTER);
      GuiFileChooser.setDialogTitle("Save Cluster Label File As");
      int vResult = GuiFileChooser.showSaveDialog(this);
      if (vResult == JFileChooser.APPROVE_OPTION) {
        CurrentClusterLabelFile = KFileFilter.ensureFileExtension(
        GuiFileChooser.getSelectedFile(),
        DIAsDEMguiPreferences.CLUSTER_LABEL_FILE_EXTENSION);
        CurrentParameterDirectory = GuiFileChooser.getCurrentDirectory();
        this.saveThesaurus();
        ThesaurusModified = false;
        String clusterIDs = "";
        if (MaxClusterID > 0) {
          clusterIDs = "; Clusters: " + MinClusterID + "-" + MaxClusterID;
        }
        ControlPanelContainer.setTitle(this.getPreferredTitle() + " [" 
        + Tools.shortenFileName(CurrentClusterLabelFile.getAbsolutePath(),
        50) + clusterIDs + "]");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void updateView() {
    
    ThesaurusIsLoaded = false;
    KTextField dummyKTextField = new KTextField("Dummy", 20);
    
    ClusterLabel_Panel = new KGridBagPanel(12, 12, 11, 11);
    ClusterLabel_Panel.addLabel("Cluster", 0, 0);
    ClusterLabel_Panel.addBlankColumn(1, 0, 12);
    ClusterLabel_Panel.addLabel("Quality", 2, 0);
    ClusterLabel_Panel.addBlankColumn(3, 0, 12);
    ClusterLabel_Panel.addLabel("Semantic Cluster Label", 4, 0);
    ClusterLabel_Panel.addBlankRow(0, 1, 11);
    ClusterIDs = new String[CurrentThesaurus.getSize()];
    QualityDecision_Combo = new KComboBox[CurrentThesaurus.getSize()];
    if (ProposedTagsArray == null) {
      ClusterNames = new KTextField[ CurrentThesaurus.getSize() ];
      ClusterNames_Combo = null;
    }
    else {
      ClusterNames = null;
      ClusterNames_Combo = new KComboBox[CurrentThesaurus.getSize()];
    }
    // to avoid 0,1,100,101,102,2,3,4,...
    TreeMap orderedIDs = new TreeMap();
    CurrentTerm = CurrentThesaurus.getFirstTerm();
    while (CurrentTerm != null) {
      orderedIDs.put(Tools.lpad(CurrentTerm.getWord(),
      CurrentThesaurus.getSize()), CurrentTerm);
      CurrentTerm = CurrentThesaurus.getNextTerm();
    }
    Iterator orderedIDsIterator = orderedIDs.keySet().iterator();
    int i = 0;  // iterate through entire cluster label thesaurus
    int rowCounter = 0;  // iterate through partial cluster label thesaurus
    while (orderedIDsIterator.hasNext()) {
      CurrentTerm = CurrentThesaurus.get(
      ((String)orderedIDsIterator.next()).trim());
      ClusterIDs[i] = CurrentTerm.getWord();
      if (i >= MinClusterID && i <= MaxClusterID) {
        QualityDecision_Combo[i] = new KComboBox(3, true, Integer.toString(i), 
        this, false);
        QualityDecision_Combo[i].setPreferredSize(new Dimension(50,
        (int)dummyKTextField.getPreferredSize().getHeight()));
        QualityDecision_Combo[i].setFont(dummyKTextField.getFont());
        QualityDecision_Combo[i].setToolTipText("Cluster Quality Decision by "
        + "DIAsDEM/Expert: a=acceptable, u=unacceptable, ?=unknown");
        if (CurrentTerm.getSynonyms().startsWith("a/")) {
          QualityDecision_Combo[i].addItem("a/?", true);
          if (CurrentTerm.getSynonyms().equals("a/a")) {
            QualityDecision_Combo[i].addItem("a/a", true);
          }
          else {
            QualityDecision_Combo[i].addItem("a/a", false);
          }
          if (CurrentTerm.getSynonyms().equals("a/u")) {
            QualityDecision_Combo[i].addItem("a/u", true);
          }
          else {
            QualityDecision_Combo[i].addItem("a/u", false);
          }
        }
        else if (CurrentTerm.getSynonyms().startsWith("u/")) {
          QualityDecision_Combo[i].addItem("u/?", true);
          if (CurrentTerm.getSynonyms().equals("u/a")) {
            QualityDecision_Combo[i].addItem("u/a", true);
          }
          else {
            QualityDecision_Combo[i].addItem("u/a", false);
          }
          if (CurrentTerm.getSynonyms().equals("u/u")) {
            QualityDecision_Combo[i].addItem("u/u", true);
          }
          else {
            QualityDecision_Combo[i].addItem("u/u", false);
          }
        }
        else {
          QualityDecision_Combo[i].addItem("?/?", true);
          QualityDecision_Combo[i].addItem("?/a", false);
          QualityDecision_Combo[i].addItem("?/u", false);
        }
      }
      if (ProposedTagsArray == null) {
        if (i >= MinClusterID && i <= MaxClusterID) {
          ClusterNames[i] = new KTextField(CurrentTerm.getScopeNotes(), 20);
          ClusterNames[i].addActionListener(this);
          ClusterLabel_Panel.addLabel(ClusterIDs[i], 0, (rowCounter + 2));
          ClusterLabel_Panel.addComponent(QualityDecision_Combo[i], 
          2, (rowCounter + 2), 1, 1, 0.0, 0.0);
          ClusterLabel_Panel.addComponent(ClusterNames[i], 4, (rowCounter + 2),
          1, 1, 1.0, 0.0);
          rowCounter++;
        }
      }
      else {
        if (i >= MinClusterID && i <= MaxClusterID) {
          ClusterNames_Combo[i] = new KComboBox(1 + ProposedTagsArray.length,
          true, null, null, true);
          ClusterNames_Combo[i].addItem(CurrentTerm.getScopeNotes(), true);
          ClusterNames_Combo[i].setPreferredSize(
          dummyKTextField.getPreferredSize());
          ClusterNames_Combo[i].setFont(dummyKTextField.getFont());
          for (int j = 0; j < ProposedTagsArray.length; j++) {
            ClusterNames_Combo[i].addItem(ProposedTagsArray[j], false);
          }
          ClusterLabel_Panel.addLabel(ClusterIDs[i], 0, (rowCounter + 2));
          ClusterLabel_Panel.addComponent(QualityDecision_Combo[i], 
          2, (rowCounter + 2), 1, 1, 0.0, 0.0);
          ClusterLabel_Panel.addComponent(ClusterNames_Combo[i], 4, 
          (rowCounter + 2), 1, 1, 1.0, 1.0);
          rowCounter++;
        }
      }
      //ClusterLabel_Panel.addBlankRow(0, (i+2), 11);
      CurrentTerm = CurrentThesaurus.getNextTerm();
      i++;
    }
    
    Thesaurus_ScrollPane = new JScrollPane(ClusterLabel_Panel);
    Thesaurus_Panel = new KBorderPanel(0, 0, 0, 0);
    Thesaurus_Panel.addCenter(Thesaurus_ScrollPane);
    
    Main_Panel = new KBorderPanel(12, 12, 11, 11);
    Main_Panel.addCenter(Thesaurus_Panel);
    Main_Panel.addSouth(Button_Panel);
    
    this.removeAll();
    this.validate();
    this.setLayout(new BorderLayout());
    this.add(Main_Panel, BorderLayout.CENTER);
    this.validate();
    ThesaurusIsLoaded = true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void openProposedTags() {
    
    if (CurrentParameterDirectory != null) {
      GuiFileChooser = new JFileChooser(CurrentParameterDirectory);
    }
    else {
      GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
      "PARAMETER_DIRECTORY"));
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Existing Tag Proposal File");
    String[] filterExtensions = new String[2];
    filterExtensions[0] = DIAsDEMguiPreferences.PRELIMINARY_DTD_FILE_EXTENSION;
    filterExtensions[1] = DIAsDEMguiPreferences.TEXT_FILE_EXTENSION;
    GuiFileChooser.setFileFilter(new KFileFilter(filterExtensions,
    "DIAsDEM Tag Proposal Files (*" + DIAsDEMguiPreferences
    .PRELIMINARY_DTD_FILE_EXTENSION + ", *" + DIAsDEMguiPreferences
    .TEXT_FILE_EXTENSION + ")"));
    int result = GuiFileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = GuiFileChooser.getSelectedFile();
      CurrentParameterDirectory = GuiFileChooser.getCurrentDirectory();
      TreeMap elements = null;
      if (selectedFile.getAbsolutePath().endsWith(DIAsDEMguiPreferences
      .PRELIMINARY_DTD_FILE_EXTENSION)) {
        DIAsDEMpreliminaryDtd dtd = null;
        try {
          dtd = new DefaultDIAsDEMpreliminaryDtd(selectedFile.getAbsolutePath(),
          DefaultDIAsDEMpreliminaryDtd.LOAD);
        }
        catch (IOException e) {
          System.out.println("Error: Cannot open tag proposal file!");
        }
        elements = dtd.getElements();
      }
      else if (selectedFile.getAbsolutePath().endsWith(DIAsDEMguiPreferences
      .TEXT_FILE_EXTENSION)) {
        TextFile textFile = new TextFile(selectedFile);
        textFile.openReadOnly();
        elements = new TreeMap();
        String line = textFile.getFirstLineButIgnoreCommentsAndEmptyLines();
        while (line != null) {
          elements.put(line.trim(), line);
          line = textFile.getNextLineButIgnoreCommentsAndEmptyLines();
        }
        textFile.close();
      }
      Iterator iterator = elements.keySet().iterator();
      while (iterator.hasNext()) {
        ProposedTagsSet.add((String)iterator.next());
      }
      ProposedTagsArray = new String[ProposedTagsSet.size()];
      iterator = ProposedTagsSet.iterator();
      for (int i = 0; i < ProposedTagsArray.length; i++) {
        ProposedTagsArray[i] = (String)iterator.next();
      }
      this.updateView();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean setMinMaxClusterIDs(String pMinMaxClusterIDsString) {
    
    boolean commit = false;
    
    if (Tools.stringIsNullOrEmpty(pMinMaxClusterIDsString)) {
      return commit;
    }
    
    String[] split = pMinMaxClusterIDsString.split("\\-");
    if (split != null && split.length == 2 && Tools.isInt(split[0].trim())
    && Tools.string2Int(split[0].trim()) >= 0 && Tools.isInt(split[1].trim())
    && Tools.string2Int(split[1].trim()) >= 0 && CurrentThesaurus != null
    && Tools.string2Int(split[1].trim()) < CurrentThesaurus.getSize()
    && (Tools.string2Int(split[1].trim()) - Tools.string2Int(split[0].trim())
    + 1) <= 512) {
      MinClusterID = Tools.string2Int(split[0].trim());
      MaxClusterID = Tools.string2Int(split[1].trim());
      commit = true;
    }
    else {
      JOptionPane.showMessageDialog(this,
      "The specified cluster label file contains more than\n"
      + "512 labels and the input cluster ID range does not.\n"
      + "match the required syntax (e.g., '0-511'). Therefore,\n"
      + "this file cannot be edited in Cluster Label Editor.\n"
      + "Please use an external text file editor!",
      this.getPreferredTitle(), JOptionPane.ERROR_MESSAGE);
    }
    
    return commit;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void resetMetadata() {
    
    MetadataNumberOfTextUnits = 0;
    MetadataNumberOfClusters = 0;
    MetadataNumberOfTextUnitsAA = 0;
    MetadataNumberOfClustersAA = 0;
    MetadataNumberOfTextUnitsAU = 0;
    MetadataNumberOfClustersAU = 0;
    MetadataNumberOfTextUnitsAQ = 0;
    MetadataNumberOfClustersAQ = 0;
    MetadataNumberOfTextUnitsUA = 0;
    MetadataNumberOfClustersUA = 0;
    MetadataNumberOfTextUnitsUU = 0;
    MetadataNumberOfClustersUU = 0;
    MetadataNumberOfTextUnitsUQ = 0;
    MetadataNumberOfClustersUQ = 0;
    MetadataNumberOfTextUnitsQQ = 0;
    MetadataNumberOfClustersQQ = 0;
    MetadataNumberOfTextUnitsQA = 0;
    MetadataNumberOfClustersQA = 0;
    MetadataNumberOfTextUnitsQU = 0;
    MetadataNumberOfClustersQU = 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void updateMetadata(String pDecision, String pNumberOfTextUnits) {
    
    int numberOfTextUnits = Tools.string2Int(pNumberOfTextUnits);
    MetadataNumberOfTextUnits += numberOfTextUnits;
    MetadataNumberOfClusters++;
    
    if (pDecision.equals("a/a")) {
      MetadataNumberOfTextUnitsAA += numberOfTextUnits;
      MetadataNumberOfClustersAA++;
    }
    else if (pDecision.equals("a/u")) {
      MetadataNumberOfTextUnitsAU += numberOfTextUnits;
      MetadataNumberOfClustersAU++;
    }
    else if (pDecision.equals("a/?")) {
      MetadataNumberOfTextUnitsAQ += numberOfTextUnits;
      MetadataNumberOfClustersAQ++;
    }
    else if (pDecision.equals("u/a")) {
      MetadataNumberOfTextUnitsUA += numberOfTextUnits;
      MetadataNumberOfClustersUA++;
    }
    else if (pDecision.equals("u/u")) {
      MetadataNumberOfTextUnitsUU += numberOfTextUnits;
      MetadataNumberOfClustersUU++;
    }
    else if (pDecision.equals("u/?")) {
      MetadataNumberOfTextUnitsUQ += numberOfTextUnits;
      MetadataNumberOfClustersUQ++;
    }
    else if (pDecision.equals("?/?")) {
      MetadataNumberOfTextUnitsQQ += numberOfTextUnits;
      MetadataNumberOfClustersQQ++;
    }
    else if (pDecision.equals("?/a")) {
      MetadataNumberOfTextUnitsQA += numberOfTextUnits;
      MetadataNumberOfClustersQA++;
    }
    else if (pDecision.equals("?/u")) {
      MetadataNumberOfTextUnitsQU += numberOfTextUnits;
      MetadataNumberOfClustersQU++;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void writeMetadataFile() {

    if (CurrentClusterLabelFile == null || !CurrentClusterLabelFile.exists()
    || !CurrentClusterLabelFile.isFile() || MetadataNumberOfClusters == 0
    || MetadataNumberOfTextUnits == 0) {
      return;
    }
    
    NumberFormat nf2 = NumberFormat.getInstance(Locale.US);
    nf2.setMinimumFractionDigits(2);
    nf2.setMaximumFractionDigits(2);
     
    TextFile metadataFile = new TextFile(new File(CurrentClusterLabelFile
    .getAbsolutePath() + ".meta"));
    metadataFile.empty();
    metadataFile.open();
    metadataFile.setNextLine("Total Number of Clusters: " 
    + MetadataNumberOfClusters);
    metadataFile.setNextLine("Total Number of Text Units: "
    + MetadataNumberOfTextUnits);
    metadataFile.setNextLine("");
    metadataFile.setNextLine("---");
    metadataFile.setNextLine("");
    metadataFile.setNextLine("Number of a/a-Clusters: "
    + MetadataNumberOfClustersAA + " (" + nf2.format(100d 
    * MetadataNumberOfClustersAA / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("Number of a/u-Clusters: "
    + MetadataNumberOfClustersAU + " (" + nf2.format(100d
    * MetadataNumberOfClustersAU / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("Number of u/u-Clusters: "
    + MetadataNumberOfClustersUU + " (" + nf2.format(100d
    * MetadataNumberOfClustersUU / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("Number of u/a-Clusters: "
    + MetadataNumberOfClustersUA + " (" + nf2.format(100d
    * MetadataNumberOfClustersUA / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("");
    metadataFile.setNextLine("Number of Text Units Assigned to a/a-Clusters: "
    + MetadataNumberOfTextUnitsAA + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsAA / MetadataNumberOfTextUnits) + "%)");
    metadataFile.setNextLine("Number of Text Units Assigned to a/u-Clusters: "
    + MetadataNumberOfTextUnitsAU + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsAU / MetadataNumberOfTextUnits) + "%)");
    metadataFile.setNextLine("Number of Text Units Assigned to u/u-Clusters: "
    + MetadataNumberOfTextUnitsUU + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsUU / MetadataNumberOfTextUnits) + "%)");
    metadataFile.setNextLine("Number of Text Units Assigned to u/a-Clusters: "
    + MetadataNumberOfTextUnitsUA + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsUA / MetadataNumberOfTextUnits) + "%)");
    metadataFile.setNextLine("");
    metadataFile.setNextLine("---");
    metadataFile.setNextLine("");
    metadataFile.setNextLine("Number of a/?-Clusters: "
    + MetadataNumberOfClustersAQ + " (" + nf2.format(100d
    * MetadataNumberOfClustersAQ / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("Number of u/?-Clusters: "
    + MetadataNumberOfClustersUQ + " (" + nf2.format(100d
    * MetadataNumberOfClustersUQ / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("Number of ?/?-Clusters: "
    + MetadataNumberOfClustersQQ + " (" + nf2.format(100d
    * MetadataNumberOfClustersQQ / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("Number of ?/a-Clusters: "
    + MetadataNumberOfClustersQA + " (" + nf2.format(100d
    * MetadataNumberOfClustersQA / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("Number of ?/u-Clusters: "
    + MetadataNumberOfClustersQU + " (" + nf2.format(100d
    * MetadataNumberOfClustersQU / MetadataNumberOfClusters) + "%)");
    metadataFile.setNextLine("");
    metadataFile.setNextLine("Number of Text Units Assigned to a/?-Clusters: "
    + MetadataNumberOfTextUnitsAQ + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsAQ / MetadataNumberOfTextUnits) + "%)");
    metadataFile.setNextLine("Number of Text Units Assigned to u/?-Clusters: "
    + MetadataNumberOfTextUnitsUQ + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsUQ / MetadataNumberOfTextUnits) + "%)");
    metadataFile.setNextLine("Number of Text Units Assigned to ?/?-Clusters: "
    + MetadataNumberOfTextUnitsQQ + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsQQ / MetadataNumberOfTextUnits) + "%)");
    metadataFile.setNextLine("Number of Text Units Assigned to ?/a-Clusters: "
    + MetadataNumberOfTextUnitsQA + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsQA / MetadataNumberOfTextUnits) + "%)");
    metadataFile.setNextLine("Number of Text Units Assigned to ?/u-Clusters: "
    + MetadataNumberOfTextUnitsQU + " (" + nf2.format(100d
    * MetadataNumberOfTextUnitsQU / MetadataNumberOfTextUnits) + "%)");
    
    
    metadataFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}