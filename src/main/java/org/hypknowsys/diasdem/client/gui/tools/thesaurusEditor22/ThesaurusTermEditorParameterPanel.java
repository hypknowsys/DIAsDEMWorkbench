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
import java.awt.event.KeyEvent;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.misc.swing.KComboBox;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KScrollTextArea;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;


/**
 * @version 2.1.5, 31 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */
  
public class ThesaurusTermEditorParameterPanel extends DiasdemParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DIAsDEMthesaurusTerm DiasdemThesaurusTerm = null;
  
  private KTextField TermText = null;
  private KComboBox TermType = null;
  private KTextField TermUseDescriptor = null;
  private KScrollTextArea TermScopeNotes = null;
  
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
  
  public ThesaurusTermEditorParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ThesaurusTermEditorParameterPanel(Server pDiasdemServer, 
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
    
    if (ActionCommand.equals("TermType") ) {
      if (TermType.getSelectedString().equals("Descriptor")) {
        TermUseDescriptor.setText("-");
        TermUseDescriptor.setEnabled(false);
      }
      else if (TermType.getSelectedString().equals("Non-Descriptor") ) {
        if (DiasdemThesaurusTerm != null) {
          TermUseDescriptor.setText(DiasdemThesaurusTerm.getUseDescriptor());
        }
        else {
          TermUseDescriptor.setText("-");
        }
        TermUseDescriptor.setEnabled(true);
      }
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
    
    return "Thesaurus Term Editor 2.2";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogXsSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    if (DiasdemThesaurusTerm != null) {
      if (TermType.getSelectedString().trim().equals("unknown")) {
        DiasdemThesaurusTerm.setType(DIAsDEMthesaurusTerm.UNKNOWN);
      }
      else if (TermType.getSelectedString().trim().equals("Descriptor")) {
        DiasdemThesaurusTerm.setType(DIAsDEMthesaurusTerm.DESCRIPTOR);
      }
      else {
        DiasdemThesaurusTerm.setType(DIAsDEMthesaurusTerm.NONDESCRIPTOR);
      }
      if (TermUseDescriptor.getText().trim().length() > 0) {
        DiasdemThesaurusTerm.setUseDescriptor(TermUseDescriptor.getText()
        .trim());
      }
      else {
        DiasdemThesaurusTerm.setUseDescriptor(DIAsDEMthesaurusTerm.UNKNOWN);
      }
      if (TermScopeNotes.getText().trim().length() > 0) {
        DiasdemThesaurusTerm.setScopeNotes(TermScopeNotes.getText().trim());
      }
      else {
        DiasdemThesaurusTerm.setScopeNotes(DIAsDEMthesaurusTerm.UNKNOWN);
      }
    }
    ThesaurusTermEditorParameter parameter = new ThesaurusTermEditorParameter(
    DiasdemThesaurusTerm);
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    ThesaurusTermEditorParameter parameter = null;
    if (pTaskParameter instanceof ThesaurusTermEditorParameter) {
      parameter = (ThesaurusTermEditorParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    DiasdemThesaurusTerm = parameter.getDiasdemThesaurusTerm();
    this.createParameterPanel();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (TermType != null) {
      return TermType;
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
    
    TermText = new KTextField("-");
    TermText.setEnabled(false);
    TermType = new KComboBox(3, true, "TermType", this);
    TermType.addItem("unknown", true);
    TermType.addItem("Descriptor", false);
    TermType.addItem("Non-Descriptor", false);
    TermUseDescriptor = new KTextField("-");
    TermScopeNotes = new KScrollTextArea("-");
    TermScopeNotes.setTextAreaLineWrap(true);

    if (DiasdemThesaurusTerm != null) {
      TermText.setText(DiasdemThesaurusTerm.getWord());
      TermUseDescriptor.setText(DiasdemThesaurusTerm.getUseDescriptor());
      if (DiasdemThesaurusTerm.isDescriptor()) {
        TermType.setSelectedString("Descriptor");
        TermUseDescriptor.setEnabled(false);
      }
      else {
        TermType.setSelectedString("Non-Descriptor");
        TermUseDescriptor.setEnabled(true);
      }
      TermScopeNotes.setText(DiasdemThesaurusTerm.getScopeNotes());
    }
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(TermType);
    
    Parameter_Panel.addLabel("Term:", 0, 0);
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(TermText, 2, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Type of Term:", 0, 2, KeyEvent.VK_T, TermType);
    Parameter_Panel.addComponent(TermType, 2, 2);
    Parameter_Panel.addBlankRow(0, 3, 11);
    Parameter_Panel.addLabel("Use Descriptor:", 0, 4, KeyEvent.VK_U, 
    TermUseDescriptor);   
    Parameter_Panel.addComponent(TermUseDescriptor, 2, 4);
    Parameter_Panel.addBlankRow(0, 5, 11);
    Parameter_Panel.setLabelAnchor(GridBagConstraints.NORTHWEST);
    Parameter_Panel.addLabel("Scope Notes:", 0, 6, KeyEvent.VK_S, 
    TermScopeNotes);
    Parameter_Panel.addComponent(TermScopeNotes, 2, 6, new Insets(0, 0, 0, 0),
    1, 1, 1.0);
    
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
    
    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}