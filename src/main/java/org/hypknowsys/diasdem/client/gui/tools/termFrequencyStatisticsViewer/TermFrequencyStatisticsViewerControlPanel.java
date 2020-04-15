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

package org.hypknowsys.diasdem.client.gui.tools.termFrequencyStatisticsViewer;

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
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class TermFrequencyStatisticsViewerControlPanel extends DiasdemActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private File CurrentThesaurusDirectory = null;
  private File CurrentStatisticsDirectory = null;
  private File CurrentThesaurusFile = null;
  private File CurrentStatisticsFile = null;

  private KBorderPanel Statistics_Panel = null;
  private KBorderPanel Thesaurus_Panel = null;
  private KButtonPanel ThesaurusButtons_Panel = null;
  private KButtonPanel SortByButtons_Panel = null;
  private KScrollTextArea Statistics_ScrollTextArea = null;
  private KScrollTextArea Thesaurus_ScrollTextArea = null;

  private DIAsDEMthesaurus CurrentStatistics = null;
  private DIAsDEMthesaurus CurrentThesaurus = null;
  private DIAsDEMthesaurusTerm CurrentTerm = null;
  private int MinOccurrence = 0;

  private final static int VIEW_THESAURUS = 1;
  private final static int INCLUDED_IN_THESAURUS = 2;
  private final static int EXCLUDED_IN_THESAURUS = 3;
  
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

  public TermFrequencyStatisticsViewerControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public TermFrequencyStatisticsViewerControlPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences, String pFileName) {
  
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

    if (ActionCommand.equals("StatisticsExit")) {
      this.setControlPanelContainerClosed(true);
    } 
    else if (ActionCommand.equals("StatisticsOpen")) {
      this.openStatistics();
    } 
    else if (ActionCommand.equals("StatisticsFreq")) {
      this.sortStatistics("Freq");
    } 
    else if (ActionCommand.equals("StatisticsTerm")) {
      this.sortStatistics("Term");
    } 
    else if (ActionCommand.equals("ThesaurusOpen")) {
      this.openThesaurus();
    } 
    else if (ActionCommand.equals("ThesaurusView")) {
      this.updateThesaurusView(VIEW_THESAURUS);
    } 
    else if (ActionCommand.equals("ThesaurusIncluded")) {
      this.updateThesaurusView(INCLUDED_IN_THESAURUS);
    } 
    else if (ActionCommand.equals("ThesaurusExcluded")) {
      this.updateThesaurusView(EXCLUDED_IN_THESAURUS);
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
      CurrentStatisticsDirectory = new File(
      DiasdemGuiPreferences.getMruProjectDirectory());
      CurrentThesaurusDirectory = new File(
      DiasdemGuiPreferences.getMruProjectDirectory());
    }
    if (DiasdemProject != null && Tools.isValidandWriteableDirectoryName(
    DiasdemProject.getProjectDirectory())) {
      CurrentStatisticsDirectory = new File(
      DiasdemProject.getProjectDirectory());
      CurrentThesaurusDirectory = new File(
      DiasdemGuiPreferences.getMruProjectDirectory());
    }

    this.createButtonPanel();
    this.createParameterPanel();

    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 

    JPanel Edit_Panel = new JPanel();
    Edit_Panel.setLayout( new GridLayout(1, 2, 12, 0) );
    Edit_Panel.add(Statistics_Panel);
    Edit_Panel.add(Thesaurus_Panel);

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

  public String getPreferredTitle() {
    
    return "Term Frequency Statistics Viewer";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getInitialFocusComponent() {
    
    if (Button_Panel != null)
      return Button_Panel.getButton(0);
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

    Button_Panel = new KButtonPanel(17, 0, 0, 0, 4, 
     KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("Open Statistics", 
      KeyEvent.VK_S, true, false, "StatisticsOpen", this);
    Button_Panel.addNextButton("Open Thesaurus", 
      KeyEvent.VK_T, false, false, "ThesaurusOpen", this);
    Button_Panel.addNextButton("Exit", 
      KeyEvent.VK_X, true, false, "StatisticsExit", this);
    Button_Panel.addLastButton("Help", 
      KeyEvent.VK_H, false, false, "StatisticsHelp", this);

    ThesaurusButtons_Panel = new KButtonPanel(17, 0, 0, 0, 3, 
      KButtonPanel.HORIZONTAL_LEFT);
    ThesaurusButtons_Panel.addFirstButton("View", 
      KeyEvent.VK_V, false, false, "ThesaurusView", this);
    ThesaurusButtons_Panel.addNextButton("Incl.", 
      KeyEvent.VK_I, false, false, "ThesaurusIncluded", this);
    ThesaurusButtons_Panel.addLastButton("Excl.", 
      KeyEvent.VK_E, false, false, "ThesaurusExcluded", this);

    SortByButtons_Panel = new KButtonPanel(17, 0, 0, 0, 2, 
      KButtonPanel.HORIZONTAL_LEFT);
    SortByButtons_Panel.addFirstButton("Sort by Freq.", 
      KeyEvent.VK_F, false, false, "StatisticsFreq", this);
    SortByButtons_Panel.addLastButton("Sort by Term", 
      KeyEvent.VK_B, false, false, "StatisticsTerm", this);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    Statistics_ScrollTextArea = new KScrollTextArea();  
    Statistics_Panel = new KBorderPanel(0, 0, 0, 0, 
      "Term Frequency Statistics:", 12, 12, 11, 11);
    Statistics_Panel.addCenter(Statistics_ScrollTextArea);
    Statistics_Panel.addSouth(SortByButtons_Panel);

    Thesaurus_ScrollTextArea = new KScrollTextArea();  
    Thesaurus_Panel = new KBorderPanel(0, 0, 0, 0, 
      "Case-Sensitive Thesaurus:", 12, 12, 11, 11);
    Thesaurus_Panel.addCenter(Thesaurus_ScrollTextArea);
    Thesaurus_Panel.addSouth(ThesaurusButtons_Panel);

    Parameter_Panel = null;
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */

  private void openStatistics() {

    File file = new File(DiasdemProject.getProperty(
    "DEFAULT_WORD_STATISTICS_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      if (CurrentStatisticsDirectory != null) 
        GuiFileChooser = new JFileChooser(CurrentStatisticsDirectory); 
      else
        GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
        "PROJECT_DIRECTORY") ); 
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Existing TF Statistics File");
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences.TF_STATISTICS_FILE_FILTER);
    int result = GuiFileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) { 
      CurrentStatisticsFile = GuiFileChooser.getSelectedFile();
      CurrentStatisticsDirectory = GuiFileChooser.getCurrentDirectory();
      CurrentStatistics = new DefaultDIAsDEMthesaurus();
      CurrentStatistics.load( CurrentStatisticsFile.getAbsolutePath() );
      CurrentStatistics.setOrderOccurrencesWordsDesc(MinOccurrence);
      this.updateStatisticsView();
      Button_Panel.setEnabled(1, true);
      ControlPanelContainer.setTitle(this.getPreferredTitle() + " [" + Tools
      .shortenFileName(CurrentStatisticsFile.getAbsolutePath(), 50)  + "]");
      SortByButtons_Panel.setAllEnabled(true);
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  private void sortStatistics(String pSortBy) {

    if (CurrentStatistics != null) {
      if ( pSortBy.equals("Freq") ) 
        CurrentStatistics.setOrderOccurrencesWordsDesc();
      else
        CurrentStatistics.setOrderWordsAsc();
      this.updateStatisticsView();
      Button_Panel.setEnabled(1, true);
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private void openThesaurus() {

    File file = new File(DiasdemProject.getProperty(
    "DEFAULT_THESAURUS_FILE"));
    if (file.exists() && file.isFile()) {
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    }
    else {
      if (CurrentThesaurusDirectory != null) 
        GuiFileChooser = new JFileChooser(CurrentThesaurusDirectory); 
      else
        GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
        "PARAMETER_DIRECTORY") ); 
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setDialogTitle("Select Existing Thesaurus File");
    GuiFileChooser.setFileFilter(DIAsDEMguiPreferences.THESAURUS_FILE_FILTER);
    int result = GuiFileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) { 
      CurrentThesaurusFile = GuiFileChooser.getSelectedFile();
      CurrentThesaurusDirectory = GuiFileChooser.getCurrentDirectory();
      CurrentThesaurus = new DefaultDIAsDEMthesaurus();
      CurrentThesaurus.load( CurrentThesaurusFile.getAbsolutePath() );
      this.updateThesaurusView(VIEW_THESAURUS);
      ThesaurusButtons_Panel.setAllEnabled(true);
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private void updateStatisticsView() {

    if (CurrentStatistics != null) {
      String word = JOptionPane.showInputDialog(this, 
        "Please input minimum term frequency:");
      if ( Tools.isInt(word) )
        MinOccurrence = Tools.string2Int(word);
      else 
        MinOccurrence = 10;
      CurrentTerm = CurrentStatistics.getFirstTerm();
      Statistics_ScrollTextArea.setText("Collection Statistics File " + 
            CurrentStatisticsFile.getName() + ":\n\n");
      Statistics_Panel.revalidate();   
      while (CurrentTerm != null) {
        if ( CurrentTerm.getOccurrences() >= MinOccurrence) {
          Statistics_ScrollTextArea.appendText( CurrentTerm.getOccurrences() + 
          "\t" + CurrentTerm.getWord() + "\n" );
        }
        CurrentTerm = CurrentStatistics.getNextTerm();
      }
      Statistics_ScrollTextArea.setCaretAtBeginning();
      Statistics_Panel.revalidate();   
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  private void updateThesaurusView(int pChoice) {

    DIAsDEMthesaurusTerm thesaurusTerm = null;
    if ( (CurrentStatistics != null) && (CurrentThesaurus != null) ) {
      switch (pChoice) {
        case VIEW_THESAURUS: {
          CurrentThesaurus.setOrderTypeWordsAsc();
          Thesaurus_ScrollTextArea.setText("Complete Thesaurus " + 
            CurrentThesaurusFile.getName() + ":\n\n");
          CurrentTerm = CurrentThesaurus.getFirstTerm();
          break;
        }
        case INCLUDED_IN_THESAURUS: {
          Thesaurus_ScrollTextArea.setText(
            "Terms of Collection Statistics File that " +
            "are Contained\nin Thesaurus " + CurrentThesaurusFile.getName() + 
            ":\n\n");
          CurrentTerm = CurrentStatistics.getFirstTerm();
          break;
        }
        case EXCLUDED_IN_THESAURUS: {
          Thesaurus_ScrollTextArea.setText(
            "Terms of Collection Statistics File that " +
            "are not Contained\nin Thesaurus " + 
            CurrentThesaurusFile.getName() + ":\n\n");
          CurrentTerm = CurrentStatistics.getFirstTerm();
          break;
        }
      }
      while (CurrentTerm != null) {
        switch (pChoice) {
          case VIEW_THESAURUS: {
            if ( CurrentTerm.isDescriptor() ) 
              Thesaurus_ScrollTextArea.appendText( CurrentTerm.getWord() + 
                " (" + CurrentTerm.getType() + "; " + 
                CurrentTerm.getScopeNotes() + ")\n" );
            else
              Thesaurus_ScrollTextArea.appendText( CurrentTerm.getWord() + 
                " (" + CurrentTerm.getType() + "; " + 
                CurrentTerm.getUseDescriptor() + ")\n" );
            CurrentTerm = CurrentThesaurus.getNextTerm();
            break;
          }
          case INCLUDED_IN_THESAURUS: {
            if ( CurrentThesaurus.contains( CurrentTerm.getWord() ) ) {
              thesaurusTerm = CurrentThesaurus.get( CurrentTerm.getWord() );
              if ( thesaurusTerm.isDescriptor() ) 
                Thesaurus_ScrollTextArea.appendText( 
                  CurrentTerm.getOccurrences() + "\t" +
                  thesaurusTerm.getWord() + " (" + thesaurusTerm.getType() +
                  "; " + thesaurusTerm.getScopeNotes() + ")\n" );
              else
                Thesaurus_ScrollTextArea.appendText( 
                  CurrentTerm.getOccurrences() + "\t" +
                  thesaurusTerm.getWord() + " (" + thesaurusTerm.getType() + 
                  "; " +  thesaurusTerm.getUseDescriptor() + ")\n" );
            }
            CurrentTerm = CurrentStatistics.getNextTerm();
            break;
          }
          case EXCLUDED_IN_THESAURUS: {
            if ( ! CurrentThesaurus.contains( CurrentTerm.getWord() ) ) {
              Thesaurus_ScrollTextArea.appendText( 
                CurrentTerm.getOccurrences() + "\t" +
                CurrentTerm.getWord() + " (" + CurrentTerm.getType() + ")\n" );
              }
            CurrentTerm = CurrentStatistics.getNextTerm();
            break;
          }
        }
      }
      Thesaurus_ScrollTextArea.setCaretAtBeginning();
      Thesaurus_Panel.revalidate();   
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