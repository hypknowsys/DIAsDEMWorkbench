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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskParameterPanel;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterDialog;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.server.DiasdemTaskParameter;
import org.hypknowsys.misc.swing.JTableScrolling;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.swing.KOptionPane;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1.5, 31 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */
  
public class ThesaurusEditorParameterPanel extends DiasdemParameterPanel 
implements TableModelListener, ListSelectionListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private DIAsDEMthesaurus DiasdemThesaurus = null;
  private DIAsDEMthesaurusTerm DiasdemThesaurusTerm = null;
  private String PreviousFindString = null;
  private int PreviousFindResultTableRow = 0;
  private boolean IsEnabled = true;

  private JTable ThesaurusTerms_Table = null;
  private ListSelectionModel ThesaurusTerms_ListSelectionModel = null;
  private int SelectedThesaurusTerm = -1;
  private JScrollPane ThesaurusTerms_ScrollPane = null;
  private KButtonPanel ThesaurusTermsButton_Panel = null;

  private ThesaurusTerms_TablePopup ThesaurusTerms_PopupMenu = null; 
  
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

  public ThesaurusEditorParameterPanel() {
  
    super();

  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public ThesaurusEditorParameterPanel(Server pDiasdemServer, 
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
  
  public void actionPerformed(ActionEvent e) {
  
    ActionCommand = e.getActionCommand();
    ActionSource = e.getSource();

    if ( ActionCommand.equals("New") ) { 
      this.newThesaurusTerm();
    } else if ( ActionCommand.equals("Find") ) { 
      this.findThesaurusTerm();
    } else if ( ActionCommand.equals("Edit") ) { 
      this.editThesaurusTerm();
    } else if ( ActionCommand.equals("Delete") ) { 
      this.deleteThesaurusTerm();
    } 
   
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TableModelListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void tableChanged(TableModelEvent pEvent) {
    
    int row = pEvent.getFirstRow();
    int column = pEvent.getColumn();
    String columnName = DiasdemThesaurus.getColumnName(column);
    Object data = DiasdemThesaurus.getValueAt(row, column);
   
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface ListSelectionListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void valueChanged(ListSelectionEvent pEvent) {
    
    if (pEvent.getValueIsAdjusting()) 
      return;
    ListSelectionModel model = (ListSelectionModel)pEvent.getSource();
    if (model.isSelectionEmpty()) {
      SelectedThesaurusTerm = -1;
      this.setComponentStatus();
    } else {
      SelectedThesaurusTerm = model.getMinSelectionIndex();
      this.setComponentStatus();
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
    
    return "Thesaurus Editor 2.2";
    
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
  
  public TaskParameter getTaskParameter() {
    
    ThesaurusEditorParameter parameter = new ThesaurusEditorParameter(
    DiasdemThesaurus);
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
   
    ThesaurusEditorParameter parameter = null;
    if (pTaskParameter instanceof ThesaurusEditorParameter) {
      parameter = (ThesaurusEditorParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    DiasdemThesaurus = parameter.getDiasdemThesaurus(); 
    IsEnabled = true;
    SaveRequired = false;
    this.createParameterPanel();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (ThesaurusTerms_Table != null && ThesaurusTerms_Table.isEnabled()) {
      return ThesaurusTerms_Table; 
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
    
    if (DiasdemThesaurus != null) {
      ThesaurusTerms_Table = new JTable(DiasdemThesaurus);
      for (int i = 0; i < ThesaurusTerms_Table.getColumnCount(); i++) {
        ThesaurusTerms_Table.getColumnModel().getColumn(i).setPreferredWidth(
        DiasdemThesaurus.getPreferredColumnWidth(i) );
      }
    }
    else {
      ThesaurusTerms_Table = new JTable();
    }
    ThesaurusTerms_Table.setPreferredScrollableViewportSize( this.getSize() );
    ThesaurusTerms_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    ThesaurusTerms_Table.setRowSelectionAllowed(true);
    ThesaurusTerms_Table.setColumnSelectionAllowed(false);
    ThesaurusTerms_ListSelectionModel = ThesaurusTerms_Table
    .getSelectionModel();
    ThesaurusTerms_ListSelectionModel.addListSelectionListener(this);
    ThesaurusTerms_ScrollPane = new JScrollPane(ThesaurusTerms_Table);
    ThesaurusTerms_Table.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
          actionPerformed(new ActionEvent(this,
          ActionEvent.ACTION_PERFORMED, "Edit"));
        }
      }
    });
    ThesaurusTerms_PopupMenu = new ThesaurusTerms_TablePopup(this);
    MouseListener popupListener = new PopupListener();
    ThesaurusTerms_Table.addMouseListener(popupListener);
    ThesaurusTerms_Table.getParent().addMouseListener(popupListener);

    ThesaurusTermsButton_Panel = new KButtonPanel(0, 0, 0, 0, 4,
    KButtonPanel.VERTICAL_TOP);
    ThesaurusTermsButton_Panel.addFirstButton("New",
    KeyEvent.VK_N, false, false, "New", this);
    ThesaurusTermsButton_Panel.addNextButton("Find",
    KeyEvent.VK_F, false, false, "Find", this);
    ThesaurusTermsButton_Panel.addNextButton("Edit",
    KeyEvent.VK_E, false, false, "Edit", this);
    ThesaurusTermsButton_Panel.addLastButton("Delete",
    KeyEvent.VK_D, false, false, "Delete", this);
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.addComponent(ThesaurusTerms_ScrollPane, 0, 0,
    new Insets(0, 0, 0, 0), 1, 1, 1.0);
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addKButtonPanel(ThesaurusTermsButton_Panel, 2, 0,
    GridBagConstraints.NORTH);
    
    if (DiasdemThesaurus != null && DiasdemThesaurus.getSize() > 0) {
      SelectedThesaurusTerm = 0;
      ThesaurusTerms_Table.setRowSelectionInterval(0, 0);
      JTableScrolling.makeRowVisible(ThesaurusTerms_Table, 0);
    }
    else {
      SelectedThesaurusTerm = -1;
    }
    
    this.removeAll();
    this.validate();
    this.addCenter(Parameter_Panel);
    this.validate();
    this.setComponentStatus();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setComponentStatus() {

    boolean enableNew = false;
    boolean enableFind = false;
    boolean enableEdit = false;
    boolean enableDelete = false;
    
    if (DiasdemThesaurus != null && IsEnabled) {
      enableNew = true;    
      if (DiasdemThesaurus.getSize() > 0 && SelectedThesaurusTerm >= 0) {
        enableFind = true;
        enableEdit = true;
        enableDelete = true;        
      }
    }
    
    ThesaurusTermsButton_Panel.setEnabled(0, enableNew);
    ThesaurusTermsButton_Panel.setEnabled(1, enableFind);
    ThesaurusTermsButton_Panel.setEnabled(2, enableEdit);
    ThesaurusTermsButton_Panel.setEnabled(3, enableDelete);
    if (ThesaurusTerms_PopupMenu != null) {
      ThesaurusTerms_PopupMenu.setEnabled(enableNew, enableFind, enableEdit,
      enableDelete);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void newThesaurusTerm() {
    
    if (DiasdemThesaurus != null) {
      String word = JOptionPane.showInputDialog(DiasdemGui.getJFrame(), 
        "Please input the new term:", this.getPreferredTitle(),
        JOptionPane.QUESTION_MESSAGE);
      if (word != null && word.trim().length() > 0) {
        word = word.trim();
        if (DiasdemThesaurus.contains(word) ) {
          JOptionPane.showMessageDialog(DiasdemGui.getJFrame(),
          Tools.insertLineBreaks(40, "The term '" + word + "' already exists "
          + "in the current DIAsDEM thesaurus!"), this.getPreferredTitle(), 
          JOptionPane.WARNING_MESSAGE);
        }
        else {
          DiasdemThesaurusTerm = new DefaultDIAsDEMthesaurusTerm( 
          DiasdemThesaurus.getNextID(), word, 1);
          ThesaurusTermEditorParameter parameter = new
          ThesaurusTermEditorParameter(DiasdemThesaurusTerm);
          TaskParameterPanel panel = parameter.getTaskParameterPanel(
          DiasdemServer, DiasdemProject, DiasdemGui, DiasdemGuiPreferences);
          parameter = (ThesaurusTermEditorParameter)DiasdemParameterDialog
          .showDiasdemParameterDialog(DiasdemServer, DiasdemProject, DiasdemGui,
          DiasdemGuiPreferences, (DiasdemTaskParameter)parameter, panel);
          Tools.requestFocus(Parameter_Panel, ThesaurusTerms_Table);
          if (parameter != null) {
            int newRow = DiasdemThesaurus.addTermAndUpdateKTable(parameter
            .getDiasdemThesaurusTerm());
            if (newRow >= 0 && newRow < DiasdemThesaurus.getSize()) {
              ThesaurusTerms_Table.setRowSelectionInterval(newRow, newRow);
              JTableScrolling.makeRowVisible(ThesaurusTerms_Table, newRow);
            }
            SaveRequired = true;
            this.setComponentStatus();
          }
        }
      }
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void findThesaurusTerm() {
    
    if (DiasdemThesaurus == null && ThesaurusTerms_Table.getModel()
    .getRowCount() <= 0) {
      return;
    }
    
    String findString = KOptionPane.showInputDialog(DiasdemGui.getJFrame(), 
    "Please input the exact thesaurus term\nor a regular expression:",
    this.getPreferredTitle(), JOptionPane.QUESTION_MESSAGE, PreviousFindString);
    if (Tools.stringIsNullOrEmpty(findString)) {
      PreviousFindResultTableRow = 0;
      PreviousFindString = "";
      return;
    }
    
    int findResultTableRow = 0;
    if (findString.equals(PreviousFindString) && PreviousFindResultTableRow
    < (ThesaurusTerms_Table.getModel().getRowCount() - 1)) {
      // continue current search
      findResultTableRow = PreviousFindResultTableRow + 1;
    }
    else {
      // start new search
      findResultTableRow = PreviousFindResultTableRow + 1;
    }
    boolean found = false;
    DiasdemThesaurusTerm = DiasdemThesaurus.getTermAtKTableRow(
    findResultTableRow);
    while (DiasdemThesaurusTerm != null && findResultTableRow
    < ThesaurusTerms_Table.getModel().getRowCount() && !found) {
      if (DiasdemThesaurusTerm.getWord().equals(findString)
      || DiasdemThesaurusTerm.getUseDescriptor().equals(findString)
      || DiasdemThesaurusTerm.getWord().matches(findString)
      || DiasdemThesaurusTerm.getUseDescriptor().matches(findString)
      || DiasdemThesaurusTerm.getScopeNotes().matches(findString)) {
        found = true;
        PreviousFindResultTableRow = findResultTableRow;
        PreviousFindString = findString;
      }
      else {
        findResultTableRow++;
        DiasdemThesaurusTerm = DiasdemThesaurus.getTermAtKTableRow(
        findResultTableRow);
      }
    }
    if (found) {
      ThesaurusTerms_Table.setRowSelectionInterval(PreviousFindResultTableRow,
      PreviousFindResultTableRow);
      JTableScrolling.makeRowVisible(ThesaurusTerms_Table, 
      PreviousFindResultTableRow);
      ThesaurusTerms_ScrollPane.repaint();
    }
    else {
      JOptionPane.showMessageDialog(DiasdemGui.getJFrame(), 
      Tools.insertLineBreaks(40, "The term '" + findString 
      + "' was not found" + (PreviousFindResultTableRow > 0 
      ? " after row " + (PreviousFindResultTableRow + 1) : "") + "!"), 
      this.getPreferredTitle(), JOptionPane.INFORMATION_MESSAGE);  
      PreviousFindResultTableRow = 0;
      PreviousFindString = "";
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void editThesaurusTerm() {
    
    if (SelectedThesaurusTerm >= 0 && SelectedThesaurusTerm 
    < DiasdemThesaurus.getSize()) {
      int selectedThesaurusTerm = SelectedThesaurusTerm;
      DiasdemThesaurusTerm = DiasdemThesaurus.getTermAtKTableRow(
      selectedThesaurusTerm);
      
      ThesaurusTermEditorParameter parameter = new
      ThesaurusTermEditorParameter(DiasdemThesaurusTerm);
      TaskParameterPanel panel = parameter.getTaskParameterPanel(
      DiasdemServer, DiasdemProject, DiasdemGui, DiasdemGuiPreferences);    
      parameter = (ThesaurusTermEditorParameter)DiasdemParameterDialog
      .showDiasdemParameterDialog(DiasdemServer, DiasdemProject, DiasdemGui, 
      DiasdemGuiPreferences, (DiasdemTaskParameter)parameter, panel);
      Tools.requestFocus(Parameter_Panel, ThesaurusTerms_Table);
      
      if (parameter != null) {      
        DiasdemThesaurus.replaceTermAtKTableRow(parameter
        .getDiasdemThesaurusTerm(), selectedThesaurusTerm);
        SaveRequired = true;
        this.setComponentStatus();  
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void deleteThesaurusTerm() {
    
    if (SelectedThesaurusTerm >= 0 && SelectedThesaurusTerm 
    < DiasdemThesaurus.getSize()) {
      boolean commit = true;
      int input = JOptionPane.showConfirmDialog(
      DiasdemGui.getJFrame(), "Warning: Do you really want to delete\n"
      + "the selected DIAsDEM thesaurus term?", this.getPreferredTitle(),
      JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
      if (input == JOptionPane.NO_OPTION
      || input == JOptionPane.CLOSED_OPTION) {
        commit = false;
      }
      if (commit) {
        int selectedThesaurusTerm = SelectedThesaurusTerm;
        DiasdemThesaurus.deleteTermAtKTableRow(selectedThesaurusTerm);
        if (selectedThesaurusTerm < DiasdemThesaurus.getSize()) {
          SelectedThesaurusTerm = selectedThesaurusTerm;
          ThesaurusTerms_Table.setRowSelectionInterval(SelectedThesaurusTerm,
          SelectedThesaurusTerm);
          JTableScrolling.makeRowVisible(ThesaurusTerms_Table, 
          SelectedThesaurusTerm);
        }
        else if (DiasdemThesaurus.getSize() > 0) {
          SelectedThesaurusTerm = selectedThesaurusTerm - 1;
          ThesaurusTerms_Table.setRowSelectionInterval(SelectedThesaurusTerm,
          SelectedThesaurusTerm);
          JTableScrolling.makeRowVisible(ThesaurusTerms_Table, 
          SelectedThesaurusTerm);
        }
        else
          SelectedThesaurusTerm = -1;
        SaveRequired = true;
        this.setComponentStatus();
      }
    }
    
  }   

  /* ########## ########## ########## ########## ########## ######### */
  
  private class PopupListener extends MouseAdapter {
    
    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }
    
    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }
    
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        if (ThesaurusTerms_Table.isRowSelected(ThesaurusTerms_Table
        .rowAtPoint(e.getPoint()))) {
          ThesaurusTerms_PopupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
      }
    }
    
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private class ThesaurusTerms_TablePopup extends JPopupMenu {
    
    KMenuItem NewItem = null;
    KMenuItem FindItem = null;
    KMenuItem EditItem = null;
    KMenuItem DeleteItem = null;
    
    public ThesaurusTerms_TablePopup(ActionListener pListener) {
      super();
      NewItem = new KMenuItem("Add New Term", KeyEvent.VK_N,
      false, "New", pListener);
      add(NewItem);
      FindItem = new KMenuItem("Find Term", KeyEvent.VK_F,
      false, "Find", pListener);
      add(FindItem);
      EditItem = new KMenuItem("Edit Selected Term", KeyEvent.VK_E,
      false, "Edit", pListener);
      add(EditItem);
      DeleteItem = new KMenuItem("Delete Selected Term", KeyEvent.VK_D,
      false, "Delete", pListener);
      add(DeleteItem);
    }
    
    public void setEnabled(boolean pNew, boolean pFind, boolean pEdit, 
    boolean pDelete) {
      NewItem.setEnabled(pNew);
      FindItem.setEnabled(pFind);
      EditItem.setEnabled(pEdit);
      DeleteItem.setEnabled(pDelete);
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