/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.tasks.prepare.aggregateSessions;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class AggregateSessionsParameterPanel extends WumParameterPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private AggregateSessionsParameter CastParameter = null;
  
  private KComboBox FirstPageOccurrence_Combo = null;
  private KCheckBox AddReserveTrail_CheckBox = null;
  
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
  
  public AggregateSessionsParameterPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AggregateSessionsParameterPanel(Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
    
    super();
    
    this.setContext(pWumServer, pWumProject, pWumGui,
    pWumGuiPreferences);
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
    
    return "Aggregate Sessions";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogSSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getTaskParameter() {
    
    int firstPageOccurrence = AggregateSessionsParameter
    .EXCLUDE_REFERRER_URL_IN_OBSERVATIONS;
    if (FirstPageOccurrence_Combo.getSelectedString().equals(
    AggregateSessionsParameter.FIRST_PAGE_OCCURRENCE_OPTIONS[
    AggregateSessionsParameter.INCLUDE_REFERRER_URL_IN_OBSERVATIONS])) {
      firstPageOccurrence = AggregateSessionsParameter
      .INCLUDE_REFERRER_URL_IN_OBSERVATIONS;
    }
    AggregateSessionsParameter parameter = new AggregateSessionsParameter();
    parameter.setFirstPageOccurrence(firstPageOccurrence);
    if (AddReserveTrail_CheckBox.isSelected()) {
      parameter.setAddReverseTrail(true);
    }
    else {
      parameter.setAddReverseTrail(false);
    }
     
    return parameter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTaskParameter(TaskParameter pTaskParameter) {
    
    if (pTaskParameter instanceof AggregateSessionsParameter) {
      CastParameter = (AggregateSessionsParameter)pTaskParameter;
    }
    else {
      return;
    }
    if (Parameter_Panel == null) {
      this.initialize();
    }
    
    IsEnabled = true;
    this.createParameterPanel();
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveCurrentParameterSettingsAsDefaults() {
    
    WumProject.setProperty("AGGREGATE_SESSIONS:_DEFAULT_FIRST_PAGE_OCCURRENCE",
    String.valueOf(FirstPageOccurrence_Combo.getSelectedIndex()));
    if (AddReserveTrail_CheckBox.isSelected()) {    
      WumProject.setBooleanProperty(
      "AGGREGATE_SESSIONS:_DEFAULT_ADD_REVERSE_TRAIL", true);
    }
    else {
      WumProject.setBooleanProperty(
      "AGGREGATE_SESSIONS:_DEFAULT_ADD_REVERSE_TRAIL", false);
    }
    WumProject.quickSave();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (FirstPageOccurrence_Combo != null) {
      return FirstPageOccurrence_Combo;
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
    
    FirstPageOccurrence_Combo = new KComboBox(AggregateSessionsParameter
    .FIRST_PAGE_OCCURRENCE_OPTIONS.length, true, "FirstPageOccurrence_Combo", 
    this);
    for (int i = 0; i < AggregateSessionsParameter.FIRST_PAGE_OCCURRENCE_OPTIONS
    .length; i++) {
      FirstPageOccurrence_Combo.addItem(AggregateSessionsParameter
      .FIRST_PAGE_OCCURRENCE_OPTIONS[i], false);
    }
    
    AddReserveTrail_CheckBox = new KCheckBox(
    "Reverse Sessions Trail before Aggregation", false, true, 
    "AddReserveTrail", this, KeyEvent.VK_R, 
    "Check this box, if all session should be reversed prior to aggregation.");
    
    if (CastParameter != null) {
      if (CastParameter.getFirstPageOccurrence() >= 0 
      && CastParameter.getFirstPageOccurrence()
      < AggregateSessionsParameter.FIRST_PAGE_OCCURRENCE_OPTIONS.length) {
        FirstPageOccurrence_Combo.setSelectedIndex(CastParameter
        .getFirstPageOccurrence());
      }
      else {
        FirstPageOccurrence_Combo.setSelectedIndex(AggregateSessionsParameter
        .EXCLUDE_REFERRER_URL_IN_OBSERVATIONS);
      }
      if (CastParameter.addReverseTrail()) {
        AddReserveTrail_CheckBox.setSelected(true);
      }
      else {
        AddReserveTrail_CheckBox.setSelected(false);
      }
    }
    else {
      if (WumProject.getIntProperty(
      "AGGREGATE_SESSIONS:_DEFAULT_FIRST_PAGE_OCCURRENCE")
      >= 0 && WumProject.getIntProperty(
      "AGGREGATE_SESSIONS:_DEFAULT_FIRST_PAGE_OCCURRENCE")
      < AggregateSessionsParameter.FIRST_PAGE_OCCURRENCE_OPTIONS.length) {
        FirstPageOccurrence_Combo.setSelectedIndex(WumProject
        .getIntProperty("AGGREGATE_SESSIONS:_DEFAULT_FIRST_PAGE_OCCURRENCE"));
      }
      else {
        FirstPageOccurrence_Combo.setSelectedIndex(AggregateSessionsParameter
        .EXCLUDE_REFERRER_URL_IN_OBSERVATIONS);
      }
      if (WumProject.getBooleanProperty(
      "AGGREGATE_SESSIONS:_DEFAULT_ADD_REVERSE_TRAIL")) {
        AddReserveTrail_CheckBox.setSelected(true);
      }
      else {
        AddReserveTrail_CheckBox.setSelected(false);
      }
    }
    
    Parameter_Panel = new KGridBagPanel(0, 0, 0, 0);
    Parameter_Panel.startFocusForwarding(FirstPageOccurrence_Combo);

    Parameter_Panel.addLabel("First Page Occurrence:", 0, 0, KeyEvent.VK_F,
    FirstPageOccurrence_Combo, true, "Task input: Exluding the first page" +
    "occurrence from aggregation typically reduces the aggregated log size.");
    Parameter_Panel.addBlankColumn(1, 0, 12);
    Parameter_Panel.addComponent(FirstPageOccurrence_Combo, 2, 0);
    Parameter_Panel.addBlankRow(0, 1, 11, this.getPreferredSizeX());
    Parameter_Panel.addLabel("Advanced Options:", 0, 10);
    Parameter_Panel.addComponent(AddReserveTrail_CheckBox, 
      2, 10, new Insets(0, 0, 0, 0));
    
    this.removeAll();
    this.validate();
    this.addNorth(Parameter_Panel);
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