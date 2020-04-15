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

package org.hypknowsys.wum.client.gui.tools.showPages;

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
  
public class ShowPagesControlPanel extends WumActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private KBorderPanel Output_Panel = null;
  private KGridBagPanel OrderBy_Panel = null;
  private ButtonGroup OrderBy_ButtonGroup = null;
  private JRadioButton OrderById_RadioButton = null;
  private JRadioButton OrderByUrl_RadioButton = null;  
  private JTable Pages_Table = null;
  private JScrollPane Pages_ScrollPane = null;

  private PagesTableModel Pages_TableModel = null;
  private int OrderBy = PagesTableModel.ORDER_BY_UNKNOWN;
  
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

  public ShowPagesControlPanel() {
  
    super();
    
    WumTaskHelpResource = WUM_TASK_HELP_RESOURCE;
    OrderBy = PagesTableModel.ORDER_BY_ID;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public ShowPagesControlPanel(Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {
  
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
    WumTaskHelpResource = WUM_TASK_HELP_RESOURCE;
    OrderBy = PagesTableModel.ORDER_BY_ID;            
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

      int selectedRow = Pages_Table.getSelectedRow();
      if (selectedRow == -1) {
        JOptionPane.showInternalMessageDialog(
           ( (WUMgui)ControlPanelContainer.getParentJFrame() ).getKDesktopPane(), 
           "Please select one table row to display all\n" + 
           "attributes of the corresponding page!",
           "Details of Web Site Page", 
           JOptionPane.WARNING_MESSAGE);
      }
      else {
        Page page = Pages_TableModel.getPage(selectedRow);
        JOptionPane.showInternalMessageDialog(
           ( (WUMgui)ControlPanelContainer.getParentJFrame() ).getKDesktopPane(), 
            "ID: " + page.getID() +"\n" + 
           "URL: " + page.getUrl() +"\n\n" +
           "Title: " + page.getTitle() +"\n" +
           "Author: " + page.getAuthor() +"\n" +
           "LastUpdate: " + page.getLastUpdate() +"\n" +
           "Remarks: " + page.getRemarks() +"\n" +
           "Concepts: " + page.getConcepts() +"\n" +
           "Accesses: " + page.getAccesses() +"\n" +
           "Referrals: " + page.getReferrals() +"\n" +
           "MaxOccurrence: " + page.getMaxOccurrence(),
           "Details of Web Site Visitor", 
           JOptionPane.INFORMATION_MESSAGE);      
      }

    } else
    
    if ( ActionCommand.equals("OrderById") ) {
    
      OrderBy = PagesTableModel.ORDER_BY_ID;
      this.updatePagesTable(OrderBy);
      
    } else   
    
    if ( ActionCommand.equals("OrderByUrl") ) {
    
      OrderBy = PagesTableModel.ORDER_BY_URL;
      this.updatePagesTable(OrderBy);
      
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
    Output_Panel.addCenter(Pages_ScrollPane);
  
    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));   
    this.setLayout( new BorderLayout() );
    this.add(Output_Panel, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);

    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle() {
    
    return "Show Pages";
    
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

    if (OrderBy == PagesTableModel.ORDER_BY_URL)
      OrderByUrl_RadioButton = new JRadioButton("Order by URL", true);
    else
      OrderByUrl_RadioButton = new JRadioButton("Order by URL", false);
    OrderByUrl_RadioButton.setActionCommand("OrderByUrl");  
    OrderByUrl_RadioButton.setMnemonic(KeyEvent.VK_U);  
    OrderByUrl_RadioButton.addActionListener(this);  
    if (OrderBy == PagesTableModel.ORDER_BY_ID)
      OrderById_RadioButton = new JRadioButton("Order by ID", true);
    else
      OrderById_RadioButton = new JRadioButton("Order by ID", false);
    OrderById_RadioButton.setActionCommand("OrderById");
    OrderById_RadioButton.setMnemonic(KeyEvent.VK_I);  
    OrderById_RadioButton.addActionListener(this);
    OrderBy_ButtonGroup = new ButtonGroup();
    OrderBy_ButtonGroup.add(OrderByUrl_RadioButton);
    OrderBy_ButtonGroup.add(OrderById_RadioButton);

    OrderBy_Panel = new KGridBagPanel(0, 0, 11, 0);
    OrderBy_Panel.addLabel("Sorting of Pages:", 0, 0);
    OrderBy_Panel.addBlankColumn(1, 0, 12);
    OrderBy_Panel.addComponent(OrderById_RadioButton, 2, 0);
    OrderBy_Panel.addBlankColumn(3, 0, 12);
    OrderBy_Panel.addComponent(OrderByUrl_RadioButton, 4, 0);

    if (OrderBy == PagesTableModel.ORDER_BY_URL)
      Pages_TableModel = new PagesTableModel(((WUMproject)WumProject)
      .getMiningBase(), PagesTableModel.ORDER_BY_URL);
    else
      Pages_TableModel = new PagesTableModel(((WUMproject)WumProject)
      .getMiningBase(), PagesTableModel.ORDER_BY_ID);
    Pages_Table = new JTable(Pages_TableModel);
    Pages_Table.setPreferredScrollableViewportSize( this.getSize() );
    Pages_ScrollPane = new JScrollPane(Pages_Table);

    for (int i = 0; i < Pages_TableModel.getColumnCount(); i++) {
      Pages_Table.getColumnModel().getColumn(i).setPreferredWidth( 
        Pages_TableModel.getPreferredColumnWidth(i) );
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  private void updatePagesTable(int pPatternType) {

    ( (WUMgui)ControlPanelContainer.getParentJFrame() ).setWaitCursor();
    Output_Panel.removeAll();
    Pages_TableModel.sort(OrderBy);
    Pages_Table = new JTable(Pages_TableModel);
    Pages_Table.setPreferredScrollableViewportSize( this.getSize() );
    Pages_ScrollPane = new JScrollPane(Pages_Table);
    for (int i = 0; i < Pages_TableModel.getColumnCount(); i++) {
      Pages_Table.getColumnModel().getColumn(i).setPreferredWidth( 
        Pages_TableModel.getPreferredColumnWidth(i) );
    }
    Output_Panel.addNorth(OrderBy_Panel);
    Output_Panel.addCenter(Pages_ScrollPane);
    this.revalidate();
    ( (WUMgui)ControlPanelContainer.getParentJFrame() ).setDefaultCursor();

  }  // updatePagesTable

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