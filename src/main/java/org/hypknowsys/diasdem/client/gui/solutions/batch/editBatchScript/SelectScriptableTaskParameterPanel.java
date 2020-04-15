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

package org.hypknowsys.diasdem.client.gui.solutions.batch.editBatchScript;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemParameterPanel;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.swing.KScrollBorderPanel;
import org.hypknowsys.misc.swing.KTextField;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class SelectScriptableTaskParameterPanel extends DiasdemParameterPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private SelectScriptableTaskParameter CastParameter = null;

  private KScrollBorderPanel ParameterContainer_Panel = null;
  private KTextField SelectedClassName_Text = null;
  private JTree ScriptableTasks_Tree = null;

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

  public SelectScriptableTaskParameterPanel() {
  
    super();

  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public SelectScriptableTaskParameterPanel(Server pDiasdemServer, 
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
    
    return "Select Scriptable Task";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogMSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    SelectScriptableTaskParameter parameter = 
    new SelectScriptableTaskParameter();
    parameter.setSelectedClassName(SelectedClassName_Text.getText());
    
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
   
    CastParameter = null;
    if (pTaskParameter instanceof SelectScriptableTaskParameter) {
      CastParameter = (SelectScriptableTaskParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    this.createParameterPanel();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {}
    
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (ScriptableTasks_Tree != null) {
      return ScriptableTasks_Tree; 
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setEnabled(boolean pEnabled) {
    
    this.setComponentStatus();
    super.setEnabled(pEnabled);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createParameterPanel() { 
        
    if (CastParameter != null) {
      SelectedClassName_Text = new KTextField(
      CastParameter.getSelectedClassName(), 30, true, false);
      SelectedClassName_Text.setCaretAtEnding();
      ScriptableTasks_Tree = new JTree(CastParameter
      .getRootOfScriptableTasks());
    }
    else {
      SelectedClassName_Text = new KTextField("", 30, false);
      ScriptableTasks_Tree = new JTree();
    }
    ScriptableTasks_Tree.getSelectionModel().setSelectionMode
    (TreeSelectionModel.SINGLE_TREE_SELECTION);
    ScriptableTasks_Tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        ScriptableTasks_Tree.getLastSelectedPathComponent();  
        if (node == null) return;
        Object nodeInfo = node.getUserObject();
        if (node.isLeaf() && nodeInfo instanceof KMenuItem
        && ((KMenuItem)nodeInfo).getTaskClassName() != null) {
          SelectedClassName_Text.setText(
          ((KMenuItem)nodeInfo).getTaskClassName());
        }
        else {
          SelectedClassName_Text.setText("");
        }
      }
    });
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(ScriptableTasks_Tree);
    ParameterContainer_Panel = new KScrollBorderPanel(0, 0, 0, 0,
    0, 0, 0, 0);
    ParameterContainer_Panel.startFocusForwarding(ScriptableTasks_Tree);
    ParameterContainer_Panel.addCenter(ScriptableTasks_Tree);
    
    Parameter_Panel.setLabelAnchor(GridBagConstraints.NORTHWEST); 
    Parameter_Panel.addLabel("Scriptable Tasks:", 0, 0, KeyEvent.VK_S, 
    ParameterContainer_Panel);
    Parameter_Panel.setLabelAnchor(GridBagConstraints.WEST); 
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(ParameterContainer_Panel, 2, 0, 
    new Insets(0, 0, 0, 0), 1, 1, 1.0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Selected Class Name:", 0, 2);
    Parameter_Panel.addComponent(SelectedClassName_Text, 2, 2, 
    new Insets(0, 0, 0, 0), 3, 1);

    this.removeAll();
    this.validate();
    this.addCenter(Parameter_Panel);
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