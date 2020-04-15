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

package org.hypknowsys.diasdem.client.gui.tools.thesaurusEditor;

import java.util.regex.*;
import javax.swing.*;
import javax.swing.text.*;
import java.lang.reflect.*;
import java.beans.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class ThesaurusEditorControlPanel extends DiasdemActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private File CurrentProjectDirectory = null;
  private File CurrentParameterDirectory = null;
  private File CurrentThesaurusFile = null;

  private KButtonPanel TermEditButtons_Panel = null;
  private KTextField TermText = null;
  private KComboBox TermType = null;
  private KComboBox TermHierarchyLevel = null;
  private KTextField TermUseDescriptor = null;
  private KScrollTextArea TermSynonyms = null;
  private KScrollTextArea TermScopeNotes = null;

  private KBorderPanel Term_Panel = null;
  private KBorderPanel Thesaurus_Panel = null;
  private KButtonPanel ThesaurusEditButtons_Panel = null;
  private KScrollTextArea Thesaurus_ScrollTextArea = null;

  private DIAsDEMthesaurus CurrentThesaurus = null;
  private DIAsDEMthesaurusTerm CurrentTerm = null;
  private boolean ThesaurusModified = false;
  
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

  public ThesaurusEditorControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public ThesaurusEditorControlPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences, String pFileName) {
  
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
  
  public void actionPerformed(ActionEvent e) {
  
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();

    if (ActionCommand.equals("ThesaurusExit") ) {
      this.setControlPanelContainerClosed(true);
    } 
    else if (ActionCommand.equals("ThesaurusNew") ) {
      this.newThesaurus();
    } 
    else if (ActionCommand.equals("ThesaurusOpen") ) {
      this.openThesaurus();
    } 
    else if (ActionCommand.equals("ThesaurusSave") ) {
      this.saveThesaurus();
    } 
    else if (ActionCommand.equals("ThesaurusSaveAs") ) {
      this.saveAsThesaurus();
    } 
    else if (ActionCommand.equals("ThesaurusEditNew") ) {
      this.newThesaurusEdit();
    } 
    else if (ActionCommand.equals("ThesaurusEditEdit") ) {
      this.editThesaurusEdit();
    } 
    else if (ActionCommand.equals("ThesaurusEditDelete") ) {
      this.deleteThesaurusEdit();
    } 
    else if (ActionCommand.equals("ThesaurusInfo") ) {
      this.infoThesaurus();
    } 
    else if (ActionCommand.equals("TermEditOk") ) {
      this.okTermEdit();
    } 
    else if (ActionCommand.equals("TermEditCancel") ) {
      this.cancelTermEdit();
    } 
    else if (ActionCommand.equals("TermType") ) {
      if (TermType.getSelectedString().equals("Descriptor") ) {
        // TermHierarchyLevel.setEnabled(true);
        TermHierarchyLevel.setEnabled(false);
        TermUseDescriptor.setEnabled(false); 
      } 
      else if (TermType.getSelectedString().equals("Non-Descriptor") ) {
        TermHierarchyLevel.setEnabled(false);
        TermUseDescriptor.setEnabled(true); 
       }
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

    this.createButtonPanel();
    this.createParameterPanel();

    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 

    JPanel Edit_Panel = new JPanel();
    Edit_Panel.setLayout( new GridLayout(1, 2, 12, 0) );
    Edit_Panel.add(Thesaurus_Panel);
    Edit_Panel.add(Term_Panel);

    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));   
    this.setLayout( new BorderLayout() );
    this.add(Edit_Panel, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);

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
        "Do you want to save the current thesaurus?");
      if (confirm == JOptionPane.YES_OPTION) {
        this.saveThesaurus();
      }
      else if (confirm == JOptionPane.CANCEL_OPTION) {
        throw new PropertyVetoException("Close operation canceled!",
        new PropertyChangeEvent(this, "close", null, null) );
      }
    }
    super.finalize();
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    
    return "Thesaurus Editor";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getInitialFocusComponent() {
    
    if (Button_Panel != null)
      return Button_Panel.getButton(1);
    else
      return null;
    
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

    Button_Panel = new KButtonPanel(17, 0, 0, 0, 7, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("Create", 
      KeyEvent.VK_R, true, false, "ThesaurusNew", this);
    Button_Panel.addNextButton("Open", 
      KeyEvent.VK_P, true, false, "ThesaurusOpen", this);
    Button_Panel.addNextButton("Info", 
      KeyEvent.VK_I, false, false, "ThesaurusInfo", this);
    Button_Panel.addNextButton("Save", 
      KeyEvent.VK_S, false, false, "ThesaurusSave", this);
    Button_Panel.addNextButton("Save As", 
      KeyEvent.VK_A, false, false, "ThesaurusSaveAs", this);
    Button_Panel.addNextButton("Exit", 
      KeyEvent.VK_X, true, false, "ThesaurusExit", this);
    Button_Panel.addLastButton("Help", 
      KeyEvent.VK_H, false, false, "ThesaurusHelp", this);

    TermEditButtons_Panel = new KButtonPanel(17, 0, 0, 0, 2, 
      KButtonPanel.HORIZONTAL_LEFT);
    TermEditButtons_Panel.addFirstButton("OK", 
      KeyEvent.VK_O, true, false, "TermEditOk", this);
    TermEditButtons_Panel.addLastButton("Cancel", 
      KeyEvent.VK_C, true, false, "TermEditCancel", this);
    TermEditButtons_Panel.setAllEnabled(false);

  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    TermText = new KTextField("");
    TermType = new KComboBox(3, true, "TermType", this);
    TermType.addItem("unknown", false);
    TermType.addItem("Descriptor", false);
    TermType.addItem("Non-Descriptor", false);
    
    TermHierarchyLevel = new KComboBox(4, true, "TermHierarchyLevel", this);
    TermHierarchyLevel.addItem("unknown", false);
    TermHierarchyLevel.addItem("Top Level Term", false);
    TermHierarchyLevel.addItem("First Level Term", false);
    TermHierarchyLevel.addItem("Second Level Term", false);
    TermHierarchyLevel.setEnabled(false);
    
    TermUseDescriptor = new KTextField();
    TermSynonyms = new KScrollTextArea();
    TermSynonyms.setEnabled(false);
    
    TermScopeNotes = new KScrollTextArea();
    this.setTermEditPane(null);

    KGridBagPanel TermEdit_Panel = new KGridBagPanel();
    TermEdit_Panel.addLabel("Term:", 0, 0);
    TermEdit_Panel.addBlankColumn(1, 0, 12);
    TermEdit_Panel.addComponent( TermText, 2, 0);
    TermEdit_Panel.addBlankRow(0, 1, 11);
    TermEdit_Panel.addLabel("Type of Term:", 0, 2);
    TermEdit_Panel.addComponent( TermType, 2, 2);
    TermEdit_Panel.addBlankRow(0, 3, 11);
    TermEdit_Panel.addLabel("Term Level:", 0, 4);
    TermEdit_Panel.addComponent( TermHierarchyLevel, 2, 4);
    TermEdit_Panel.addBlankRow(0, 5, 11);
    TermEdit_Panel.addLabel("Use Descriptor:", 0, 6);
    TermEdit_Panel.addComponent( TermUseDescriptor, 2, 6);
    TermEdit_Panel.addBlankRow(0, 7, 11);
    TermEdit_Panel.setLabelAnchor( GridBagConstraints.NORTHWEST );
    TermEdit_Panel.addLabel("Synonyms:", 0, 8);
    TermEdit_Panel.addComponent( TermSynonyms, 2, 8, 
      new Insets(0, 0, 0, 0), 1, 1, 0.5 );
    TermEdit_Panel.addBlankRow(0, 9, 11);
    TermEdit_Panel.addLabel("Scope Notes:", 0, 10);
    TermEdit_Panel.addComponent( TermScopeNotes, 2, 10, 
      new Insets(0, 0, 0, 0), 1, 1, 0.5 );
    TermEdit_Panel.setLabelAnchor( GridBagConstraints.WEST );

    ThesaurusEditButtons_Panel = new KButtonPanel(17, 0, 0, 0, 3, 
      KButtonPanel.HORIZONTAL_LEFT);
    ThesaurusEditButtons_Panel.addFirstButton("New", 
      KeyEvent.VK_N, true, false, "ThesaurusEditNew", this);
    ThesaurusEditButtons_Panel.addNextButton("Edit", 
      KeyEvent.VK_T, true, false, "ThesaurusEditEdit", this);
    ThesaurusEditButtons_Panel.addLastButton("Delete", 
      KeyEvent.VK_D, true, false, "ThesaurusEditDelete", this);
    ThesaurusEditButtons_Panel.setAllEnabled(false);

    Thesaurus_ScrollTextArea = new KScrollTextArea();  

    Thesaurus_Panel = new KBorderPanel(0, 0, 0, 0, 
    "Case-Sensitive Thesaurus:", 12, 12, 11, 11);
    Thesaurus_Panel.addCenter(Thesaurus_ScrollTextArea);
    Thesaurus_Panel.addSouth(ThesaurusEditButtons_Panel);

    Term_Panel = new KBorderPanel(0, 0, 0, 0, 
    "Selected Thesaurus Term:", 12, 12, 11, 11);
    Term_Panel.addCenter(TermEdit_Panel);
    Term_Panel.addSouth(TermEditButtons_Panel);

    Parameter_Panel = null;
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */

  private void newThesaurus() {

    if (ThesaurusModified) {
      int confirm = JOptionPane.showConfirmDialog(this,
        "Do you want to save the current thesaurus?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        this.saveThesaurus();
      }
    }

    File file = null;
    if (CurrentParameterDirectory != null) {
      GuiFileChooser = new JFileChooser(CurrentParameterDirectory);
    }
    else {
      GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
      "PARAMETER_DIRECTORY") );
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Thesaurus File to be Created");
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences.THESAURUS_FILE_FILTER);
    int vResult = GuiFileChooser.showOpenDialog(this);
    if (vResult == JFileChooser.APPROVE_OPTION) {
      CurrentThesaurusFile = ( (KFileFilter)GuiFileChooser.getFileFilter() )
      .ensureDefaultFileExtension(GuiFileChooser.getSelectedFile());
      boolean commit = true;
      if (CurrentThesaurusFile.exists()) {
        int confirm = JOptionPane.showConfirmDialog(this,
        "Do you really want to replace the existing thesaurus\n" + Tools
        .shortenFileName(CurrentThesaurusFile.getAbsolutePath(), 50) + "?",
        this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
          commit = false;
        }
      }
      if (commit) {
        CurrentParameterDirectory = GuiFileChooser.getCurrentDirectory();
        CurrentThesaurus = new DefaultDIAsDEMthesaurus();
        CurrentThesaurus.save( CurrentThesaurusFile.getAbsolutePath() );
        this.updateThesaurusView();
        ThesaurusEditButtons_Panel.setAllEnabled(true);
        Button_Panel.setEnabled(2, true);  // Info
        Button_Panel.setEnabled(3, true);  // Save
        Button_Panel.setEnabled(4, true);  // SaveAs
        ThesaurusModified = false;
        ControlPanelContainer.setTitle("Thesaurus Editor [" + Tools
        .shortenFileName(CurrentThesaurusFile.getAbsolutePath(), 50)  + "]");
      }
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  private void openThesaurus() {

    if (ThesaurusModified) {
      int confirm = JOptionPane.showConfirmDialog(this,
      "Do you want to save the current thesaurus?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        this.saveThesaurus();
      }
    }

    File file = new File(DiasdemProject.getProperty(
    "DEFAULT_THESAURUS_FILE"));
    if ( file.exists() && file.isFile() ) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      if (CurrentParameterDirectory != null) {
        GuiFileChooser = new JFileChooser(CurrentParameterDirectory); 
      }
      else {
        GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
        "PARAMETER_DIRECTORY") ); 
      }
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Existing Thesaurus File");
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences.THESAURUS_FILE_FILTER);
    int vResult = GuiFileChooser.showOpenDialog(this);
    if (vResult == JFileChooser.APPROVE_OPTION) {
      CurrentThesaurusFile = ( (KFileFilter)GuiFileChooser.getFileFilter() )
      .ensureDefaultFileExtension(GuiFileChooser.getSelectedFile());
      CurrentParameterDirectory = GuiFileChooser.getCurrentDirectory();
      CurrentThesaurus = new DefaultDIAsDEMthesaurus();
      CurrentThesaurus.load( CurrentThesaurusFile.getAbsolutePath() );
      this.updateThesaurusView();
      ThesaurusEditButtons_Panel.setAllEnabled(true);
      Button_Panel.setEnabled(2, true);  // Info
      Button_Panel.setEnabled(3, true);  // Save
      Button_Panel.setEnabled(4, true);  // SaveAs
      ThesaurusModified = false;
      ControlPanelContainer.setTitle(this.getPreferredTitle() + " [" + Tools
      .shortenFileName(CurrentThesaurusFile.getAbsolutePath(), 50)  + "]");
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void saveThesaurus() {
    
    boolean commit = true;
    DIAsDEMthesaurusTerm term = CurrentThesaurus.getFirstTerm();
    while (term != null) {
      if (CurrentThesaurus.getDescriptorTerm(term.getWord()) == null) {
        commit = false;
        JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
        "The thesaurus cannot be saved due to the following error:\n" +
        "The non-descriptor term " + term.getWord() + " does not\n" +
        "have a use descriptor term that truely is a descriptor!",
        ControlPanelContainer.getTitle(), JOptionPane.WARNING_MESSAGE);
      }
      term = CurrentThesaurus.getNextTerm();
    }

    if (commit && CurrentThesaurus != null && CurrentThesaurusFile != null) {
      CurrentThesaurus.save( CurrentThesaurusFile.getAbsolutePath() );
      CurrentThesaurus.saveAsCsvFile( Tools.removeFileExtension(
      CurrentThesaurusFile.getAbsolutePath() ) + ".csv" );
      CurrentThesaurus.saveAsHtmlFile( Tools.removeFileExtension(
      CurrentThesaurusFile.getAbsolutePath() ) + ".html" );
      ThesaurusModified = false;
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void saveAsThesaurus() {

    if (CurrentThesaurus != null) {
      if (CurrentProjectDirectory != null) {
        GuiFileChooser = new JFileChooser(CurrentProjectDirectory);
      }
      else {
        GuiFileChooser = new JFileChooser( DiasdemProject.getProperty(
        "PROJECT_DIRECTORY") );
      }
      GuiFileChooser.setFileFilter(DIAsDEMguiPreferences.THESAURUS_FILE_FILTER);
      GuiFileChooser.setDialogTitle("Save Thesaurus File As");
      int vResult = GuiFileChooser.showSaveDialog(this);
      if (vResult == JFileChooser.APPROVE_OPTION) {
        CurrentThesaurusFile = KFileFilter.ensureFileExtension(
        GuiFileChooser.getSelectedFile(),
        DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION);
        int confirm = 0;
        if (CurrentThesaurusFile.exists()) {
          confirm = JOptionPane.showConfirmDialog(this,
          "Do you really want to replace the existing thesaurus?",
          this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
        }
        else {
          confirm = JOptionPane.YES_OPTION;
        }
        if (confirm == JOptionPane.YES_OPTION) {
          CurrentParameterDirectory = GuiFileChooser.getCurrentDirectory();
          this.saveThesaurus();
          ThesaurusModified = false;
          ControlPanelContainer.setTitle(this.getPreferredTitle() + " ["
          + Tools.shortenFileName(CurrentThesaurusFile.getName(), 50)  + "]");
        }
      }
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void updateThesaurusView() {

    if (CurrentThesaurus != null) {
      CurrentThesaurus.setOrderTypeWordsAsc();
      CurrentTerm = CurrentThesaurus.getFirstTerm();
      Thesaurus_ScrollTextArea.setText("");
      while (CurrentTerm != null) {
        if ( CurrentTerm.isDescriptor() ) 
          Thesaurus_ScrollTextArea.appendText( CurrentTerm.getWord() + 
            " (" + CurrentTerm.getType() + "; " + 
            CurrentTerm.getScopeNotes() + ")\n" );
        else
          Thesaurus_ScrollTextArea.appendText( CurrentTerm.getWord() + 
            " (" + CurrentTerm.getType() + "; " + 
            CurrentTerm.getUseDescriptor() + ")\n" );
        CurrentTerm = CurrentThesaurus.getNextTerm();
      }
      Thesaurus_ScrollTextArea.setCaretAtBeginning();
      Thesaurus_Panel.revalidate();
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void infoThesaurus() {

    int terms = 0;
    int descriptors = 0, nonDescriptors = 0;

    if (CurrentThesaurus != null) {
   
      CurrentTerm = CurrentThesaurus.getFirstTerm();
      while (CurrentTerm != null) {
        terms++;
        if ( CurrentTerm.isDescriptor() ) {
          descriptors++;
        }
        else if ( CurrentTerm.isNonDescriptor() ) {
          nonDescriptors++;
        }
         
        CurrentTerm = CurrentThesaurus.getNextTerm();
      }
 
    JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
      "Number of Terms: " + terms + 
      "\nNumber of Descriptors: " + descriptors +
      "\nNumber of Non-Descriptors: " + nonDescriptors, 
      "Thesaurus Info", JOptionPane.INFORMATION_MESSAGE);

    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void newThesaurusEdit() {

    if (CurrentThesaurus != null) {

      String word = JOptionPane.showInputDialog(this, 
        "Please input the new term:");
      if (word != null) {
        if ( CurrentThesaurus.contains(word) ) {
          JOptionPane.showMessageDialog(this, "The term \"" + word + 
          "\" already exists\nin the current thesaurus!");  
        }
        else {
          this.cancelTermEdit();
          CurrentTerm = new DefaultDIAsDEMthesaurusTerm( 
          CurrentThesaurus.getNextID(), word, 1);
          this.setTermEditPane(CurrentTerm);
          TermEditButtons_Panel.setAllEnabled(true);
          ThesaurusEditButtons_Panel.setAllEnabled(false);
        }
      }

    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void editThesaurusEdit() {

    if (CurrentThesaurus != null) {

      String word = Thesaurus_ScrollTextArea.getSelectedText();
      if (word == null)
        word = JOptionPane.showInputDialog(this, 
          "Please input the term to be edited:");

      if (word != null) {
        this.cancelTermEdit();
        CurrentTerm = CurrentThesaurus.get(word);
        if (CurrentTerm != null) {
          this.setTermEditPane(CurrentTerm);
          TermEditButtons_Panel.setAllEnabled(true);
          ThesaurusEditButtons_Panel.setAllEnabled(false);
        }
        else
          JOptionPane.showMessageDialog(this, "The term \"" + word + 
            "\" does not exist\nin the current thesaurus!");
      }

    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void deleteThesaurusEdit() {

    if (CurrentThesaurus != null) {

      String word = Thesaurus_ScrollTextArea.getSelectedText();
      if (word == null) {
        word = JOptionPane.showInputDialog(this, 
        "Please input the term to be deleted:");
      }

      if (word != null) {
        if ( CurrentThesaurus.contains(word) ) {
          int confirm = JOptionPane.showConfirmDialog(this,
          "Do you really want to delete the term \"" + word + "\"?",
          this.getPreferredTitle(), JOptionPane.YES_NO_OPTION);
          if (confirm == JOptionPane.YES_OPTION) {
            CurrentThesaurus.delete(word);
            this.updateThesaurusView();
          }
        }
        else {
          JOptionPane.showMessageDialog(this, "The term \"" + word + 
          "\" does not exist\nin the current thesaurus!");     
        }
      }

    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void okTermEdit() {

    if ( (CurrentThesaurus != null) && (CurrentTerm != null) ) {

      if ( TermType.getSelectedString().trim().equals("unknown") )
        CurrentTerm.setType(DIAsDEMthesaurusTerm.UNKNOWN);
      else 
        if (  TermType.getSelectedString().trim().equals("Descriptor") )
          CurrentTerm.setType(DIAsDEMthesaurusTerm.DESCRIPTOR);
        else
          CurrentTerm.setType(DIAsDEMthesaurusTerm.NONDESCRIPTOR);

      if ( TermHierarchyLevel.getSelectedString().trim().equals("unknown") )
        CurrentTerm.setHierarchyLevel(DIAsDEMthesaurusTerm.UNKNOWN);
      else 
        if ( TermHierarchyLevel.getSelectedString().trim()
          .equals("Top Level Term") )
          CurrentTerm.setHierarchyLevel(DIAsDEMthesaurusTerm.TOP_TERM);
        else
          if ( TermHierarchyLevel.getSelectedString()
            .equals("First Level Term") )
            CurrentTerm.setHierarchyLevel(DIAsDEMthesaurusTerm.LEVEL1_TERM);
          else
            CurrentTerm.setHierarchyLevel(DIAsDEMthesaurusTerm.LEVEL2_TERM);

      if ( TermUseDescriptor.getText().trim().length() > 0)
        CurrentTerm.setUseDescriptor( TermUseDescriptor.getText().trim() );
      else
        CurrentTerm.setUseDescriptor(DIAsDEMthesaurusTerm.UNKNOWN);

      if ( TermSynonyms.getText().trim().length() > 0)
        CurrentTerm.setSynonyms( TermSynonyms.getText().trim() );
      else
        CurrentTerm.setSynonyms(DIAsDEMthesaurusTerm.UNKNOWN);

      if ( TermScopeNotes.getText().trim().length() > 0)
        CurrentTerm.setScopeNotes( TermScopeNotes.getText().trim() );
      else
        CurrentTerm.setScopeNotes(DIAsDEMthesaurusTerm.UNKNOWN);      

      CurrentThesaurus.add(CurrentTerm);
      this.setTermEditPane(null);
      ThesaurusEditButtons_Panel.setAllEnabled(true);
      TermEditButtons_Panel.setAllEnabled(false);      
      this.updateThesaurusView();
      ThesaurusModified = true;

    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void cancelTermEdit() {

      CurrentTerm = new DefaultDIAsDEMthesaurusTerm();
      this.setTermEditPane(null);
      ThesaurusEditButtons_Panel.setAllEnabled(true);
      TermEditButtons_Panel.setAllEnabled(false);      

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void setTermEditPane(DIAsDEMthesaurusTerm pTerm) {

    if (pTerm == null) {
      TermText.setText("");   
      TermText.setEnabled(false);
      TermType.setSelectedIndex(0);  
      TermType.setEnabled(false);
      TermHierarchyLevel.setSelectedIndex(0);   
      TermHierarchyLevel.setEnabled(false);
      TermUseDescriptor.setText("");   
      TermUseDescriptor.setEnabled(false);
      TermSynonyms.setText("");   
      TermSynonyms.setTextAreaEnabled(false);
      TermScopeNotes.setText("");   
      TermScopeNotes.setTextAreaEnabled(false);
    }
    else {
      TermText.setText( pTerm.getWord() );
      if ( pTerm.getType().equals(DIAsDEMthesaurusTerm.UNKNOWN) )
        TermType.setSelectedIndex(0);
      else 
        if ( pTerm.getType().equals(DIAsDEMthesaurusTerm.DESCRIPTOR) )
          TermType.setSelectedIndex(1);
        else
          TermType.setSelectedIndex(2);
      TermType.setEnabled(true);
      if ( pTerm.getHierarchyLevel().equals(DIAsDEMthesaurusTerm.UNKNOWN) )
        TermHierarchyLevel.setSelectedIndex(0);
      else 
        if ( pTerm.getHierarchyLevel().equals(DIAsDEMthesaurusTerm.TOP_TERM) )
          TermType.setSelectedIndex(1);
        else
          if ( pTerm.getHierarchyLevel().equals(DIAsDEMthesaurusTerm.LEVEL1_TERM) )
            TermType.setSelectedIndex(2);
          else
            TermType.setSelectedIndex(3);
      // TermHierarchyLevel.setEnabled(true);
      TermHierarchyLevel.setEnabled(false);
      TermUseDescriptor.setText( pTerm.getUseDescriptor() );
      if ( pTerm.getType().equals(DIAsDEMthesaurusTerm.NONDESCRIPTOR) ) {
        TermUseDescriptor.setEnabled(true);
      }
      TermSynonyms.setText( pTerm.getSynonyms() );
      // TermSynonyms.setTextAreaEnabled(true); 
      TermSynonyms.setTextAreaEnabled(false); 
      TermScopeNotes.setText( pTerm.getScopeNotes() );
      TermScopeNotes.setTextAreaEnabled(true);
      Tools.requestFocus(TermType);
    }
 
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}