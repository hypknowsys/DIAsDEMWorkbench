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

package org.hypknowsys.diasdem.client.gui.tools.thesaurusEditor22;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurus;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KFileFilter;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;

/**
 * @version 2.1.5, 31 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */
  
public class ThesaurusEditorControlPanel extends DiasdemActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private DIAsDEMthesaurus DiasdemThesaurus = null;
  private DIAsDEMthesaurusTerm DiasdemThesaurusTerm = null;
  private String ThesaurusFileName = null;
  private File ThesaurusDirectory = null;
      
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

  public ThesaurusEditorControlPanel(Server pDiasdemServer, 
  Project pDiasdemProject, GuiClient pDiasdemGui, 
  GuiClientPreferences pDiasdemGuiPreferences) {
  
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
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

    if ( ActionCommand.equals("Create") ) { 
      this.createThesaurus();
    } else if ( ActionCommand.equals("Open") ) {
      this.openThesaurus();
    } else if ( ActionCommand.equals("Info") ) {
      this.infoThesaurus();
    } else if ( ActionCommand.equals("Save") ) {
      this.save();
    } else if ( ActionCommand.equals("SaveAs") ) {
      this.saveAs();
    } else if ( ActionCommand.equals("Exit") ) {
      this.setControlPanelContainerClosed(true);
    } else if (ActionCommand.equals("KInternalFrame:EscapePressed")) {      
      if (CloseIfEscapeIsPressed) {
        this.setControlPanelContainerClosed(true);
      }
    }        
  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskControlPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {

    super.initialize();
    
    PriorDiasdemGuiStatus = DO_NOT_MODIFY_DIASDEM_GUI_STATUS;    

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Thesaurus Editor 2.2";
    
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
  
  public void finalize() throws PropertyVetoException {

    boolean commit = true;
    if (DiasdemThesaurus != null && DiasdemThesaurus.getSize() > 0
    && Parameter_Panel != null && Parameter_Panel.getSaveRequired()) {
      int input = JOptionPane.showConfirmDialog(
      DiasdemGui.getJFrame(), Tools.insertLineBreaks(40,
      "Warning: The current DIAsDEM thesaurus has not been saved yet. "
      + "Do you really want to close Thesaurus Editor 2.2?"),
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION, 
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION 
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
    }
    if (commit) {
      super.finalize();
    }
    else {
      throw new PropertyVetoException("Close operation canceled!", 
      new PropertyChangeEvent(this, "close", null, null) ); 
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createButtonPanel() {  

    Button_Panel = this.getButtonPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    Parameter_Panel = new ThesaurusEditorParameterPanel(DiasdemServer, 
    DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
    Parameter_Panel.setEnabled(false);
  
  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createThesaurus() {
    
    boolean commit = true;
    if (DiasdemThesaurus != null && DiasdemThesaurus.getSize() > 0
    && Parameter_Panel != null && Parameter_Panel.getSaveRequired()) {
      int input = JOptionPane.showConfirmDialog(
      DiasdemGui.getJFrame(), 
      "Warning: The current DIAsDEM thesaurus\n"
      + "has not been saved yet. Do you really want\n"
      + "to create a new DIAsDEM thesaurus?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION, 
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
    }
    if (commit) {
      Button_Panel.setEnabled(0, true);  // Create
      Button_Panel.setEnabled(1, true);  // Open
      Button_Panel.setEnabled(2, true);  // Info
      Button_Panel.setEnabled(3, true);  // Save
      Button_Panel.setEnabled(4, true);  // SaveAs
      DiasdemThesaurus = new DefaultDIAsDEMthesaurus("DIAsDEM Thesaurus", 1);
      ThesaurusFileName = null;
      DiasdemThesaurus.setMaintainKTableModel(true);
      DiasdemThesaurus.setOrderTypeWordsAsc();
      Parameter_Panel.setSaveRequired(false);
      this.setControlPanelContainerTitle(this.getPreferredTitle());
      Parameter_Panel.setTaskParameter(new ThesaurusEditorParameter(
      DiasdemThesaurus));
      Tools.requestFocus(Parameter_Panel.getAsComponent(),
      Parameter_Panel.getInitialFocusComponent());
    }
  
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private void openThesaurus() {
    
    boolean commit = true;
    if (DiasdemThesaurus != null && DiasdemThesaurus.getSize() > 0
    && Parameter_Panel != null && Parameter_Panel.getSaveRequired()) {
      int input = JOptionPane.showConfirmDialog(
      DiasdemGui.getJFrame(), 
      "Warning: The current DIAsDEM thesaurus has\n"
      + "not been saved yet. Do you really want to\n"
      + "open a new DIAsDEM thesaurus?",
      this.getPreferredTitle(), JOptionPane.YES_NO_OPTION,
      JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
    }
    if (commit) {
      File file = null;
      if (ThesaurusFileName != null) {
        file = new File(ThesaurusFileName);
      }
      if (file != null && file.exists() && file.isFile()) {
        GuiFileChooser = new JFileChooser(file);
        GuiFileChooser.setSelectedFile(file);
      }
      else if (ThesaurusDirectory != null) {
        GuiFileChooser = new JFileChooser(ThesaurusDirectory);
      }
      else if (DiasdemProject != null && DiasdemProject.getStringProperty(
      "DEFAULT_THESAURUS_FILE").length() > 0) {
        GuiFileChooser = new JFileChooser(DiasdemProject
        .getStringProperty("DEFAULT_THESAURUS_FILE"));
        file = new File(DiasdemProject.getStringProperty(
        "DEFAULT_THESAURUS_FILE"));
        GuiFileChooser.setSelectedFile(file);
      }
      else if (DiasdemProject != null && DiasdemProject.getStringProperty(
      "PROJECT_DIRECTORY").length() > 0) {
        GuiFileChooser = new JFileChooser(DiasdemProject
        .getStringProperty("PROJECT_DIRECTORY"));
      }
      else {
        GuiFileChooser = new JFileChooser();
      }
      GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      GuiFileChooser.setDialogTitle("Select Existing DIAsDEM Thesaurus");
      GuiFileChooser.setFileFilter(DIAsDEMguiPreferences.THESAURUS_FILE_FILTER);
      GuiFileChooser.setAcceptAllFileFilterUsed(false);
      int result = GuiFileChooser.showOpenDialog(this);
      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = KFileFilter.ensureFileExtension(
        GuiFileChooser.getSelectedFile(),
        DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION);
        DiasdemThesaurus = new DefaultDIAsDEMthesaurus();
        DiasdemThesaurus.setMaintainKTableModel(true);
        DiasdemThesaurus.load(selectedFile.getAbsolutePath());
        DiasdemThesaurus.setOrderTypeWordsAsc();
        ThesaurusDirectory = GuiFileChooser.getCurrentDirectory();
        this.setControlPanelContainerTitle(this.getPreferredTitle() + " ["
        + Tools.shortenFileName(selectedFile.getAbsolutePath(), 40)  + "]");
        Button_Panel.setEnabled(0, true);  // Create
        Button_Panel.setEnabled(1, true);  // Open
        Button_Panel.setEnabled(2, true);  // Info
        Button_Panel.setEnabled(3, true);  // Save
        Button_Panel.setEnabled(4, true);  // SaveAs
        ThesaurusFileName = selectedFile.getAbsolutePath();
        Parameter_Panel.setSaveRequired(false);
        Parameter_Panel.setTaskParameter(new ThesaurusEditorParameter(
        DiasdemThesaurus));
        Tools.requestFocus(Parameter_Panel.getAsComponent(),
        Parameter_Panel.getInitialFocusComponent());
      }
    }
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private void infoThesaurus() {
    
    DiasdemThesaurus = ((ThesaurusEditorParameter)Parameter_Panel
    .getTaskParameter()).getDiasdemThesaurus();
    
    int terms = 0;
    int descriptors = 0;
    int nonDescriptors = 0;
    if (DiasdemThesaurus != null) {
      DiasdemThesaurusTerm = DiasdemThesaurus.getFirstTerm();
      while (DiasdemThesaurusTerm != null) {
        terms++;
        if (DiasdemThesaurusTerm.isDescriptor()) {
          descriptors++;
        }
        else if (DiasdemThesaurusTerm.isNonDescriptor()) {
          nonDescriptors++;
        }
        
        DiasdemThesaurusTerm = DiasdemThesaurus.getNextTerm();
      }
      JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
      "Number of Terms: " + terms +
      "\nNumber of Descriptors: " + descriptors +
      "\nNumber of Non-Descriptors: " + nonDescriptors,
      "Thesaurus Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void save() {
    
    DiasdemThesaurus = ((ThesaurusEditorParameter)Parameter_Panel
    .getTaskParameter()).getDiasdemThesaurus();
    
    if (ThesaurusFileName != null) {
      File file = new File(ThesaurusFileName);
      if (file.exists() && file.isFile()) {
        boolean commit = true;
        DIAsDEMthesaurusTerm term = DiasdemThesaurus.getFirstTerm();
        while (term != null) {
          if (DiasdemThesaurus.getDescriptorTerm(term.getWord()) == null) {
            commit = false;
            JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
            Tools.insertLineBreaks(60, "The thesaurus cannot be saved "
            + "because the non-descriptor term '" + term.getWord()
            + "' is associated with a use descriptor term that does not "
            + "correspond to a valid descriptor term!"), 
            this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);
          }
          term = DiasdemThesaurus.getNextTerm();
        }
        if (commit && DiasdemThesaurus != null) {
          DiasdemThesaurus.save(file.getAbsolutePath());
          DiasdemThesaurus.saveAsCsvFile(Tools.removeFileExtension(
          file.getAbsolutePath()) + ".csv");
          DiasdemThesaurus.saveAsHtmlFile(Tools.removeFileExtension(
          file.getAbsolutePath()) + ".html");
        }
        Parameter_Panel.setSaveRequired(false);
      }
      else {
        this.saveAs();
      }
    }
    else {
      this.saveAs();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void saveAs() {
    
    DiasdemThesaurus = ((ThesaurusEditorParameter)Parameter_Panel
    .getTaskParameter()).getDiasdemThesaurus();

    boolean commit = true;
    DIAsDEMthesaurusTerm term = DiasdemThesaurus.getFirstTerm();
    while (term != null) {
      if (DiasdemThesaurus.getDescriptorTerm(term.getWord()) == null) {
        commit = false;
        JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
        Tools.insertLineBreaks(60, "The thesaurus cannot be saved "
        + "because the non-descriptor term '" + term.getWord()
        + "' is associated with a use descriptor term that does not "
        + "correspond to a valid descriptor term!"), 
        this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);
      }
      term = DiasdemThesaurus.getNextTerm();
    }
    if (!commit) {
      return;
    }

    File file = null;
    if (ThesaurusFileName != null) {
      file = new File(ThesaurusFileName);
    }
    if (file != null && file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else if (ThesaurusDirectory != null) {
      GuiFileChooser = new JFileChooser(ThesaurusDirectory);
    }
    else if (DiasdemProject != null && DiasdemProject.getStringProperty(
    "DEFAULT_THESAURUS_FILE").length() > 0) {
      GuiFileChooser = new JFileChooser(DiasdemProject
      .getStringProperty("DEFAULT_THESAURUS_FILE"));
    }
    else if (DiasdemProject != null && DiasdemProject.getStringProperty(
    "PROJECT_DIRECTORY").length() > 0) {
      GuiFileChooser = new JFileChooser(DiasdemProject
      .getStringProperty("PROJECT_DIRECTORY"));
    }
    else {
      GuiFileChooser = new JFileChooser();
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select DIAsDEM Thesaurus to be Created");
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences.THESAURUS_FILE_FILTER);
    GuiFileChooser.setAcceptAllFileFilterUsed(false);
    int result = GuiFileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      file = KFileFilter.ensureFileExtension(
      GuiFileChooser.getSelectedFile(),
      DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION);
      ThesaurusDirectory = GuiFileChooser.getCurrentDirectory();
      if (Tools.isExistingFile(file.getAbsolutePath())) {
        commit = false;
        int input = JOptionPane.showConfirmDialog(
        DiasdemGui.getJFrame(), 
        "Warning: The specified DIAsDEM thesaurus currently\n" +
        "exists. Do you really want to replace this file?",
        this.getPreferredTitle(), JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
        if (input == JOptionPane.YES_OPTION) {
          commit = true;
        }
      }  
      
      if (commit && DiasdemThesaurus != null) {
        DiasdemThesaurus.save(file.getAbsolutePath());
        DiasdemThesaurus.saveAsCsvFile( Tools.removeFileExtension(
        file.getAbsolutePath() ) + ".csv" );
        DiasdemThesaurus.saveAsHtmlFile( Tools.removeFileExtension(
        file.getAbsolutePath() ) + ".html" );
      }
      ThesaurusFileName = file.getAbsolutePath();
      Parameter_Panel.setTaskParameter(new ThesaurusEditorParameter(
      DiasdemThesaurus));
      Tools.requestFocus(Parameter_Panel.getInitialFocusComponent());
      Parameter_Panel.setSaveRequired(false);
      this.setControlPanelContainerTitle(this.getPreferredTitle() + " ["
      + Tools.shortenFileName(file.getAbsolutePath(), 40)  + "]");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private KButtonPanel getButtonPanel() {  

    KButtonPanel button_Panel = new KButtonPanel(17, 0, 0, 0, 7, 
      KButtonPanel.HORIZONTAL_RIGHT);
    button_Panel.addFirstButton("Create", 
      KeyEvent.VK_C, true, false, "Create", this);
    button_Panel.addNextButton("Open", 
      KeyEvent.VK_O, true, false, "Open", this);
    button_Panel.addNextButton("Info", 
      KeyEvent.VK_I, false, false, "Info", this);
    button_Panel.addNextButton("Save", 
      KeyEvent.VK_S, false, false, "Save", this);
    button_Panel.addNextButton("Save As", 
      KeyEvent.VK_A, false, false, "SaveAs", this);
    button_Panel.addNextButton("Exit", 
      KeyEvent.VK_X, true, false, "Exit", this);
    button_Panel.addLastButton("Help", 
      KeyEvent.VK_H, false, false, "Help", this);
    
    return button_Panel;
    
  } 
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}