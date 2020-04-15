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

package org.hypknowsys.diasdem.client.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class DiasdemTaskResultPanel extends DiasdemResultPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private TaskResult Result = null;
  private boolean IsEnabled = true;

  private KTextField Status_Text = null;
  private KTextField LogMessage_Text = null;
  private KScrollTextArea Description_Text = null;
 
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

  public DiasdemTaskResultPanel() {
  
    super();
   
  } 

  /* ########## ########## ########## ########## ########## ######### */

  public DiasdemTaskResultPanel(Server pDiasdemServer, 
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
  /* ########## interface TaskResultPanel methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
    
    super.initialize();
  
    PreferredSizeX = DiasdemGuiPreferences.getDialogSSizeX();
    PreferredSizeY = DiasdemGuiPreferences.getDialogMSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 
    
    this.createResultPanel();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    return "Edit Task Result";
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public TaskResult getTaskParameter() {
    
    return Result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setTaskResult(TaskResult pTaskResult) {
   
    Result = pTaskResult;
    
    this.createResultPanel();
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public Component getInitialFocusComponent() {
    
    if (Status_Text != null)
      return Status_Text; 
    else
      return null;
    
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createResultPanel() { 
        
    if (Result != null) {
      LogMessage_Text = new KTextField(Result.getLogMessage(), 30, true, false);
      LogMessage_Text.setCaretAtEnding();
      Status_Text = new KTextField(Result.getStatusString(), 30, 
      true, false);
      Status_Text.setCaretAtEnding();
      Description_Text = new KScrollTextArea(Result.getDescription(), 5, 30, 
      true, false);
    }
    else {
      LogMessage_Text = new KTextField("", 30, false);
      Status_Text = new KTextField("", 30, false);
      Description_Text = new KScrollTextArea("", 30, 10, false, false);
    }
    Result_Panel = new KGridBagPanel(0, 0, 0, 0);
    Result_Panel.startFocusForwarding(LogMessage_Text);
    
    Result_Panel.addLabel("Result Status:", 0, 0);
    Result_Panel.addBlankColumn(1, 0, 12);
    Result_Panel.addComponent(Status_Text, 2, 0);
    Result_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Result_Panel.addLabel("Log Message:", 0, 2);
    Result_Panel.addComponent(LogMessage_Text, 2, 2);
    Result_Panel.addBlankRow(0, 3, 11);
    Result_Panel.setLabelAnchor(GridBagConstraints.NORTHWEST); 
    Result_Panel.addLabel("Description:", 0, 4);
    Result_Panel.setLabelAnchor(GridBagConstraints.WEST); 
    Result_Panel.addComponent(Description_Text, 2, 4, new Insets(0, 0, 0, 0), 
      1, 1, 1.0);

    this.removeAll();
    this.validate();
    this.addCenter(Result_Panel);
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

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}