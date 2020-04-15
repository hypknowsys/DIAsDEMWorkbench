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

package org.hypknowsys.wum.client.gui.tools.options;

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
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 2.1, 28 October 2003
 * @author Karsten Winkler
 */
  
public class OptionsControlPanel extends WumActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private KTabbedPane Tabbed_Pane = null;

  private KGridBagPanel GuiPreferences_Panel = null;
  private JTable GuiPreferences_Table = null;
  private GuiPreferencesTableModel GuiPreferences_TableModel = null;
  private JScrollPane GuiPreferences_ScrollPane = null;

  private KGridBagPanel ProjectProperties_Panel = null;
  private JTable ProjectProperties_Table = null;
  private ProjectTableModel ProjectProperties_TableModel = null;
  private JScrollPane ProjectProperties_ScrollPane = null;

  private KGridBagPanel Applications_Panel = null;
  private File CurrentDirectory = null;
  private KTextField EditorFile_Text = null;
  private KTextField BrowserFile_Text = null;
  private KTextField XmlViewerFile_Text = null;
  private KButtonPanel EditorFile_Button = null;
  private KButtonPanel BrowserFile_Button = null;
  private KButtonPanel XmlViewerFile_Button = null;

  private KGridBagPanel LookAndFeel_Panel = null;
  private KComboBox PlafDescriptors_Combo = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String[] PlafClassNames= {
    "com.jgoodies.plaf.plastic.PlasticLookAndFeel",
    "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel",
    "com.jgoodies.plaf.plastic.PlasticXPLookAndFeel",
    "com.jgoodies.plaf.windows.ExtWindowsLookAndFeel",
    "com.jgoodies.plaf.motif.ExtMotifLookAndFeel",
    "javax.swing.plaf.metal.MetalLookAndFeel",
    "javax.swing.plaf.windows.WindowsLookAndFeel",
    "javax.swing.plaf.motif.MotifLookAndFeel"};

  private final static String[] PlafDescriptors= {
    "JGoodies Plastic",
    "JGoodies Plastic 3D",
    "JGoodies Plastic XP",
    "JGoodies Extended Windows",
    "JGoodies Extended Motif",
    "Java Default: Metal",
    "Java Default: Windows",
    "Java Default: Motif"};

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public OptionsControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public OptionsControlPanel(Server pWumServer, Project pWumProject, GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences, String pFileName) {
  
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
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

    if ( ActionCommand.equals("OK") ) {
      this.ok();      
    } else if (ActionCommand.equals("BrowserFile_Button")) {   
      CurrentDirectory = this.fileNameButtonClicked(
      BrowserFile_Text, CurrentDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Your Preferred Web Browser");
    } else if (ActionCommand.equals("EditorFile_Button")) {      
      CurrentDirectory = this.fileNameButtonClicked(
      EditorFile_Text, CurrentDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Your Preferred File Editor");
    } else if (ActionCommand.equals("XmlViewerFile_Button")) {
      CurrentDirectory = this.fileNameButtonClicked(
      XmlViewerFile_Text, CurrentDirectory, "PROJECT_DIRECTORY", 
      "Select", KeyEvent.VK_S, null, "Select Your Preferred XML Viewer");    
    } else if ( ActionCommand.equals("Cancel") ) {
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
    
    PriorWumGuiStatus = DO_NOT_MODIFY_WUM_GUI_STATUS;    
    GuiTimer = new javax.swing.Timer(ONE_SECOND, this);

    this.createButtonPanel();
    this.createInputPanels();

    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 

    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));   
    this.setLayout( new BorderLayout() );
    this.add(Tabbed_Pane, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setControlPanelContainer(TaskControlPanelContainer 
  pControlPanelContainer) {    
    
    ControlPanelContainer = pControlPanelContainer;
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    
    return "Options";
    
  }  
  
  /* ########## ########## ########## ########## ########## ######### */  

  public Component getInitialFocusComponent() {
    
    if (Tabbed_Pane != null)
      return Tabbed_Pane;
    else
      return null;
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeX() {
    
    return WumGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createButtonPanel() {  

    Button_Panel = new KButtonPanel(17, 0, 0, 0, 3,
    KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("Apply", KeyEvent.VK_A, true, true,
    "OK", this);
    Button_Panel.addNextButton("Cancel", 0, true, false, 
    "Cancel", this);
    Button_Panel.addLastButton("Help", KeyEvent.VK_H, false, false,
    "Help", this);

  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    Parameter_Panel = null;
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void createInputPanels() {  

    // GUI Properties
    GuiPreferences_TableModel = new GuiPreferencesTableModel(
      WumServer, WumProject, WumGui, WumGuiPreferences);
    GuiPreferences_Table = new JTable(GuiPreferences_TableModel);
    GuiPreferences_Table.setPreferredScrollableViewportSize( this.getSize() );
    GuiPreferences_ScrollPane = new JScrollPane(GuiPreferences_Table);

    for (int i = 0; i < GuiPreferences_TableModel.getColumnCount(); i++) {
      GuiPreferences_Table.getColumnModel().getColumn(i).setPreferredWidth( 
      GuiPreferences_TableModel.getPreferredColumnWidth(i) );
    }

    GuiPreferences_Panel = new KGridBagPanel();
    GuiPreferences_Panel.addComponent(GuiPreferences_ScrollPane, 0, 0, 
      new Insets(0, 0, 0, 0), 1, 1, 1.0);
    
    EditorFile_Text = new KTextField(
      WumGuiPreferences.getProperty("EXTERNAL_EDITOR") );
    BrowserFile_Text = new KTextField(
      WumGuiPreferences.getProperty("EXTERNAL_BROWSER") );
    XmlViewerFile_Text = new KTextField(
      WumGuiPreferences.getProperty("EXTERNAL_XML_VIEWER") );

    EditorFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    EditorFile_Button.addSingleButton("...", KeyEvent.VK_F, true, true, 
      "EditorFile_Button", this);    
    BrowserFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    BrowserFile_Button.addSingleButton("...", KeyEvent.VK_W, true, true, 
      "BrowserFile_Button", this);    
    XmlViewerFile_Button = new KButtonPanel(0, 0, 0, 0, 1, 
      KButtonPanel.HORIZONTAL_RIGHT);
    XmlViewerFile_Button.addSingleButton("...", KeyEvent.VK_X, true, true, 
      "XmlViewerFile_Button", this);    

    Applications_Panel = new KGridBagPanel();
    Applications_Panel.startFocusForwarding(EditorFile_Text);
    Applications_Panel.addLabel("File Editor:", 0, 0, KeyEvent.VK_F,
      EditorFile_Button);
    Applications_Panel.addBlankColumn(1, 0, 12);
    Applications_Panel.addComponent(EditorFile_Text, 2, 0);
    Applications_Panel.addBlankColumn(3, 0, 12);
    Applications_Panel.addKButtonPanel(EditorFile_Button, 4, 0);
    Applications_Panel.addBlankRow(0, 1, 11);
    Applications_Panel.addLabel("Web Browser:", 0, 2, KeyEvent.VK_W,
      BrowserFile_Button);
    Applications_Panel.addComponent(BrowserFile_Text, 2, 2);
    Applications_Panel.addKButtonPanel(BrowserFile_Button, 4, 2);
    Applications_Panel.addBlankRow(0, 3, 11);
    Applications_Panel.addLabel("XML Viewer:", 0, 4, KeyEvent.VK_X,
      XmlViewerFile_Button);
    Applications_Panel.addComponent(XmlViewerFile_Text, 2, 4);
    Applications_Panel.addKButtonPanel(XmlViewerFile_Button, 4, 4);

    // Look and Feel
    PlafDescriptors_Combo = new KComboBox(PlafDescriptors.length, 
    true, "PlafDescriptors_Combo", this, false);
    for (int i = 0; i < PlafDescriptors.length; i++) {
      if (PlafClassNames[i].equals(WumGuiPreferences
      .getLookAndFeelClassName().trim())) {
        PlafDescriptors_Combo.addItem(PlafDescriptors[i], true);
      }
      else {
        PlafDescriptors_Combo.addItem(PlafDescriptors[i], false);
      }
    }
    LookAndFeel_Panel = new KGridBagPanel();
    LookAndFeel_Panel.startFocusForwarding(PlafDescriptors_Combo);
    LookAndFeel_Panel.addLabel("Look & Feel Name:", 0, 0, KeyEvent.VK_N,
      PlafDescriptors_Combo);
    LookAndFeel_Panel.addBlankColumn(1, 0, 12);
    LookAndFeel_Panel.addComponent(PlafDescriptors_Combo, 2, 0);

    // Project Properties
    ProjectProperties_TableModel = new ProjectTableModel(
      WumServer, WumProject, WumGui, WumGuiPreferences);
    ProjectProperties_Table = new JTable(ProjectProperties_TableModel);
    ProjectProperties_Table.setPreferredScrollableViewportSize( this.getSize() );
    ProjectProperties_ScrollPane = new JScrollPane(ProjectProperties_Table);

    for (int i = 0; i < ProjectProperties_TableModel.getColumnCount(); i++) {
      ProjectProperties_Table.getColumnModel().getColumn(i)
      .setPreferredWidth(ProjectProperties_TableModel
      .getPreferredColumnWidth(i));
    }

    ProjectProperties_Panel = new KGridBagPanel();
    ProjectProperties_Panel.addComponent(ProjectProperties_ScrollPane, 0, 0, 
      new Insets(0, 0, 0, 0), 1, 1, 1.0);
    
    KPanel main_Panel = new KPanel(12, 12, 11, 11);
    main_Panel.setLayout( new BorderLayout() );
    main_Panel.add(Applications_Panel, BorderLayout.NORTH);
    main_Panel.startFocusForwarding(EditorFile_Text);

    KPanel mainPlaf_Panel = new KPanel(12, 12, 11, 11);
    mainPlaf_Panel.setLayout( new BorderLayout() );
    mainPlaf_Panel.add(LookAndFeel_Panel, BorderLayout.NORTH);
    mainPlaf_Panel.startFocusForwarding(PlafDescriptors_Combo);

    Tabbed_Pane = new KTabbedPane();
    Tabbed_Pane.addTab("GUI Properties", GuiPreferences_Panel, 
    KeyEvent.VK_G, true, true);
    Tabbed_Pane.addTab("GUI Look & Feel", mainPlaf_Panel, 
    KeyEvent.VK_L, true, false);
    Tabbed_Pane.addTab("External Programs", main_Panel, 
    KeyEvent.VK_E, true, false);
    if (WumProject.getProjectFileName() != null
    && WumProject.getProjectFileName().length() > 0) {      
      Tabbed_Pane.addTab("Project Properties", ProjectProperties_Panel,
      KeyEvent.VK_P, true, false);
    }
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */

  private void ok() {
    
      boolean commit = true;
      for (int i = 0; i < GuiPreferences_TableModel.getRowCount(); i++) {
        if ( ! GuiPreferences_TableModel.isValidValue(i) ) {
          JOptionPane.showMessageDialog(WumGui.getJFrame(), "Input Error:\n\n" +
            "'" + GuiPreferences_TableModel.getGuiProperty(i) +
            "' must be a valid " + 
            GuiPreferences_TableModel.getGuiPropertyType(i) + "!\n", 
            this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);
          commit = false;
        }
      }
      if (WumProject.getProjectFileName() != null
      && WumProject.getProjectFileName().length() > 0) {
        for (int i = 0; i < ProjectProperties_TableModel.getRowCount(); i++) {
          if ( ! ProjectProperties_TableModel.isValidValue(i) ) {
            JOptionPane.showMessageDialog(WumGui.getJFrame(), "Input Error:\n\n" +
            "'" + ProjectProperties_TableModel.getProjectProperty(i) +
            "' must be a valid " +
            ProjectProperties_TableModel.getProjectPropertyType(i) + "!\n",
            this.getPreferredTitle(), JOptionPane.WARNING_MESSAGE);
            commit = false;
          }
        }
      }

      if (commit) { 
        // set look & feel class name if necessary
        int selectedPlaf = PlafDescriptors_Combo.getSelectedIndex();
        if (!PlafClassNames[selectedPlaf].equals(WumGuiPreferences
        .getLookAndFeelClassName().trim())) {
          WumGuiPreferences.setLookAndFeelClassName(
          PlafClassNames[selectedPlaf]);
          JOptionPane.showMessageDialog(ControlPanelContainer.getParentJFrame(),
          "Please restart the GUI to activate\nthe new Java look and feel!", 
          this.getPreferredTitle(), JOptionPane.INFORMATION_MESSAGE);
        }
        
        WumGuiPreferences = GuiPreferences_TableModel.commit();
        WumGuiPreferences.setProperty("EXTERNAL_EDITOR", 
          EditorFile_Text.getText().trim() );
        WumGuiPreferences.setProperty("EXTERNAL_BROWSER", 
          BrowserFile_Text.getText().trim() );
        WumGuiPreferences.setProperty("EXTERNAL_XML_VIEWER", 
          XmlViewerFile_Text.getText().trim() );

        // check TASK_THREAD_PRIORITY
        int currentTaskThreadPriority = WumGuiPreferences.getIntProperty(
        "TASK_THREAD_PRIORITY");
        if (currentTaskThreadPriority < Thread.MIN_PRIORITY ||
          currentTaskThreadPriority > Thread.MAX_PRIORITY)
          WumGuiPreferences.setProperty("TASK_THREAD_PRIORITY",
          ( new Integer( Thread.NORM_PRIORITY ) ).toString() );
        WumServer.setTaskThreadPriority(WumGuiPreferences
        .getIntProperty("TASK_THREAD_PRIORITY") );
    
        WumGuiPreferences.quickSave();

        if (WumProject.getProjectFileName() != null
        && WumProject.getProjectFileName().length() > 0) {
          WumProject = ProjectProperties_TableModel.commit();
          WumProject.quickSave();
        }
        
        this.setControlPanelContainerClosed(true);
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