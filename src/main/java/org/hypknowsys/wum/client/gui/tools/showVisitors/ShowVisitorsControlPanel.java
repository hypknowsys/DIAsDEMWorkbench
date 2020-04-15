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

package org.hypknowsys.wum.client.gui.tools.showVisitors;

import java.lang.reflect.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */
  
public class ShowVisitorsControlPanel extends WumActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private KBorderPanel Output_Panel = null;
  private KGridBagPanel OrderBy_Panel = null;
  private ButtonGroup OrderBy_ButtonGroup = null;
  private JRadioButton OrderById_RadioButton = null;
  private JRadioButton OrderByHost_RadioButton = null;  
  private JTable Visitors_Table = null;
  private JScrollPane Visitors_ScrollPane = null;

  private VisitorsTableModel Visitors_TableModel = null;
  private int OrderBy = VisitorsTableModel.ORDER_BY_UNKNOWN;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String WUM_TASK_HELP_RESOURCE = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public ShowVisitorsControlPanel() {
  
    super();
    
    WumTaskHelpResource = WUM_TASK_HELP_RESOURCE;
    OrderBy = VisitorsTableModel.ORDER_BY_HOST;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public ShowVisitorsControlPanel(Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
    WumTaskHelpResource = WUM_TASK_HELP_RESOURCE;
    OrderBy = VisitorsTableModel.ORDER_BY_HOST;            
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

    if ( ActionCommand.equals("Close") ) {
      this.setControlPanelContainerClosed(true);
    } else
    
    if ( ActionCommand.equals("Details") ) {

      int selectedRow = Visitors_Table.getSelectedRow();
      if (selectedRow == -1) {
        JOptionPane.showInternalMessageDialog(
           ( (WUMgui)ControlPanelContainer.getParentJFrame() ).getKDesktopPane(), 
           "Please select one table row to display all\n" + 
           "attributes of the corresponding visitor!",
           "Details of Web Site Visitor", 
           JOptionPane.WARNING_MESSAGE);
      }
      else {
        Visitor visitor = Visitors_TableModel.getVisitor(selectedRow);
        JOptionPane.showInternalMessageDialog(
           ( (WUMgui)ControlPanelContainer.getParentJFrame() ).getKDesktopPane(), 
           "ID: " + visitor.getID() +"\n" + 
           "Host: " + visitor.getHost() +"\n\n" +
           "RFC931: " + visitor.getRFC931() +"\n" +
           "AuthUser: " + visitor.getAuthUser() +"\n" +
           "UserAgent: " + visitor.getUserAgent() +"\n" +
           "Cookie: " + visitor.getCookie() +"\n" +
           "Accesses: " + visitor.getAccesses(),
           "Details of Web Site Visitor", 
           JOptionPane.INFORMATION_MESSAGE);      
      }

    } else
    
    if ( ActionCommand.equals("OrderById") ) {
    
      OrderBy = VisitorsTableModel.ORDER_BY_ID;
      this.updateVisitorsTable(OrderBy);
      
    } else   
    
    if ( ActionCommand.equals("OrderByHost") ) {
    
      OrderBy = VisitorsTableModel.ORDER_BY_HOST;
      this.updateVisitorsTable(OrderBy);
      
    } else if (ActionCommand.equals("Help")) {
      this.help();
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
  
    PriorWumGuiStatus = DO_NOT_MODIFY_WUM_GUI_STATUS;    
    GuiTimer = new javax.swing.Timer(ONE_SECOND, this);

    this.createButtonPanel();
    this.createOutputPanels();

    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 

    Output_Panel = new KBorderPanel();
    Output_Panel.addNorth(OrderBy_Panel);
    Output_Panel.addCenter(Visitors_ScrollPane);
  
    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));   
    this.setLayout( new BorderLayout() );
    this.add(Output_Panel, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);

    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Show Visitors";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogMSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogLSizeY();
    
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
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

 public void createButtonPanel() {  

    Button_Panel = new KButtonPanel(17, 0, 0, 0, 3, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("Close", KeyEvent.VK_C, true, true, 
    "Close", this);
    Button_Panel.addNextButton("Details", KeyEvent.VK_D, true, false, 
    "Details", this);
    Button_Panel.addLastButton("Help", KeyEvent.VK_H, 
    (WumTaskHelpResource == null ? false : true), false, "Help", this);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    Parameter_Panel = null;
  
  }

  /* ########## ########## ########## ########## ########## ######### */

  public void createOutputPanels() {  

    if (OrderBy == VisitorsTableModel.ORDER_BY_HOST)
      OrderByHost_RadioButton = new JRadioButton("Order by Host", true);
    else
      OrderByHost_RadioButton = new JRadioButton("Order by Host", false);
    OrderByHost_RadioButton.setActionCommand("OrderByHost");  
    OrderByHost_RadioButton.setMnemonic(KeyEvent.VK_H);  
    OrderByHost_RadioButton.addActionListener(this);  
    if (OrderBy == VisitorsTableModel.ORDER_BY_ID)
      OrderById_RadioButton = new JRadioButton("Order by ID", true);
    else
      OrderById_RadioButton = new JRadioButton("Order by ID", false);
    OrderById_RadioButton.setActionCommand("OrderById");
    OrderById_RadioButton.setMnemonic(KeyEvent.VK_I);  
    OrderById_RadioButton.addActionListener(this);
    OrderBy_ButtonGroup = new ButtonGroup();
    OrderBy_ButtonGroup.add(OrderByHost_RadioButton);
    OrderBy_ButtonGroup.add(OrderById_RadioButton);

    OrderBy_Panel = new KGridBagPanel(0, 0, 11, 0);
    OrderBy_Panel.addLabel("Sorting of Visitors:", 0, 0);
    OrderBy_Panel.addBlankColumn(1, 0, 12);
    OrderBy_Panel.addComponent(OrderById_RadioButton, 2, 0);
    OrderBy_Panel.addBlankColumn(3, 0, 12);
    OrderBy_Panel.addComponent(OrderByHost_RadioButton, 4, 0);

    if (OrderBy == VisitorsTableModel.ORDER_BY_HOST)
      Visitors_TableModel = new VisitorsTableModel(((WUMproject)WumProject)
      .getMiningBase(), VisitorsTableModel.ORDER_BY_HOST);
    else
      Visitors_TableModel = new VisitorsTableModel(((WUMproject)WumProject)
      .getMiningBase(), VisitorsTableModel.ORDER_BY_ID);
    Visitors_Table = new JTable(Visitors_TableModel);
    Visitors_Table.setPreferredScrollableViewportSize( this.getSize() );
    Visitors_ScrollPane = new JScrollPane(Visitors_Table);

    for (int i = 0; i < Visitors_TableModel.getColumnCount(); i++) {
      Visitors_Table.getColumnModel().getColumn(i).setPreferredWidth( 
        Visitors_TableModel.getPreferredColumnWidth(i) );
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  private void updateVisitorsTable(int pPatternType) {

    ( (WUMgui)ControlPanelContainer.getParentJFrame() ).setWaitCursor();
    Output_Panel.removeAll();
    Visitors_TableModel.sort(OrderBy);
    Visitors_Table = new JTable(Visitors_TableModel);
    Visitors_Table.setPreferredScrollableViewportSize( this.getSize() );
    Visitors_ScrollPane = new JScrollPane(Visitors_Table);
    for (int i = 0; i < Visitors_TableModel.getColumnCount(); i++) {
      Visitors_Table.getColumnModel().getColumn(i).setPreferredWidth( 
        Visitors_TableModel.getPreferredColumnWidth(i) );
    }
    Output_Panel.addNorth(OrderBy_Panel);
    Output_Panel.addCenter(Visitors_ScrollPane);
    this.revalidate();
    ( (WUMgui)ControlPanelContainer.getParentJFrame() ).setDefaultCursor();

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}