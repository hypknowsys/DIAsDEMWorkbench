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

package org.hypknowsys.diasdem.client.gui.help.aboutDiasdemWorkbench;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.Timer;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskControlPanelContainer;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DiasdemActionsControlPanel;
import org.hypknowsys.misc.swing.KButtonPanel;
import org.hypknowsys.misc.swing.KGridBagPanel;
import org.hypknowsys.misc.swing.KPanel;
import org.hypknowsys.misc.swing.KScrollTextArea;
import org.hypknowsys.misc.swing.KTabbedPane;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class AboutDiasdemWorkbenchControlPanel
extends DiasdemActionsControlPanel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private KTabbedPane Tabbed_Pane = null;
  
  private KGridBagPanel GuiPreferences_Panel = null;
  
  private KScrollTextArea Version_Text = null;
  private KScrollTextArea Contact_Text = null;
  private KScrollTextArea Credits_Text = null;
  private KScrollTextArea License_Text = null;
  
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
  
  public AboutDiasdemWorkbenchControlPanel() {
    
    super();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AboutDiasdemWorkbenchControlPanel(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences, String pFileName) {
    
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
  
  public void actionPerformed(ActionEvent pActionEvent) {
    
    ActionCommand = pActionEvent.getActionCommand();
    ActionSource = pActionEvent.getSource();
    
    if (ActionCommand.equals("Close")) {
      this.setControlPanelContainerClosed(true);
    }
    else if (ActionCommand.equals("Cancel")) {
      this.setControlPanelContainerClosed(true);
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
    
    super.initialize();
    
    PriorDiasdemGuiStatus = DO_NOT_MODIFY_DIASDEM_GUI_STATUS;
    GuiTimer = new Timer(ONE_SECOND, this);
    
    this.createButtonPanel();
    this.createInputPanels();
    
    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY));
    
    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
    this.setLayout(new BorderLayout());
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
    
    return "About " + DiasdemGuiPreferences
    .getProperty("DIASDEM_WORKBENCH_TITLE");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Component getInitialFocusComponent() {
    
    if (Tabbed_Pane != null) {
      return Tabbed_Pane;
    }
    else {
      return null;
    }
    
  }
  
  public JButton getDefaultButton() {
    
    if (Button_Panel != null) {
      return Button_Panel.getDefaultButton();
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeX() {
    
    return DiasdemGuiPreferences.getDialogSSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogMSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createButtonPanel() {
    
    Button_Panel = new KButtonPanel(17, 0, 0, 0, 2,
    KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("Close", KeyEvent.VK_C, true, true,
    "Close", this);
    Button_Panel.addLastButton("Help", KeyEvent.VK_H, false, false,
    "Help", this);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createParameterPanel() {
    
    Parameter_Panel = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void createInputPanels() {
    
    Version_Text = new KScrollTextArea();
    Version_Text.setTextAreaLineWrap(true);
    Version_Text.setTextAreaWrapStyleWord(true);
    Version_Text.setText(
    DiasdemGuiPreferences.getProperty("DIASDEM_WORKBENCH_TITLE")
    + "\n\nVersion " + DiasdemGuiPreferences.getProperty("DIASDEM_VERSION")
    + " for Java " + DiasdemGuiPreferences.getProperty("JAVA_VERSION")
    + "\nReleased " + DiasdemGuiPreferences.getProperty("RELEASE_DATE"));
    // + "\n\nCurrent GUI Status: " + DiasdemGui.getGuiStatus());
    Version_Text.setCaretAtBeginning();
    Version_Text.setTextAreaEditable(false);
    KPanel version_Panel = new KPanel(12, 12, 11, 11);
    version_Panel.setLayout(new BorderLayout());
    version_Panel.add(Version_Text, BorderLayout.CENTER);
    version_Panel.startFocusForwarding(Version_Text);
    
    String contact = Tools.stringFromTextualSystemResource(
    "org/hypknowsys/diasdem/resources/txt/CONTACT.txt");
    if (contact.length() == 0) {
      contact = "The file org/hypknowsys/diasdem/resources/txt/CONTACT.txt\n"
      + "could not be retrieved from CLASSPATH.";
    }
    Contact_Text = new KScrollTextArea();
    Contact_Text.setTextAreaLineWrap(true);
    Contact_Text.setTextAreaWrapStyleWord(true);
    Contact_Text.setText(contact);
    Contact_Text.setCaretAtBeginning();
    Contact_Text.setTextAreaEditable(false);
    KPanel contact_Panel = new KPanel(12, 12, 11, 11);
    contact_Panel.setLayout(new BorderLayout());
    contact_Panel.add(Contact_Text, BorderLayout.CENTER);
    contact_Panel.startFocusForwarding(Contact_Text);
    
    String credits = Tools.stringFromTextualSystemResource(
    "org/hypknowsys/diasdem/resources/txt/CREDITS.txt");
    if (credits.length() == 0) {
      contact = "The file org/hypknowsys/diasdem/resources/txt/CREDITS.txt\n"
      + "could not be retrieved from CLASSPATH.";
    }
    Credits_Text = new KScrollTextArea();
    Credits_Text.setTextAreaLineWrap(true);
    Credits_Text.setTextAreaWrapStyleWord(true);
    Credits_Text.setText(credits);
    Credits_Text.setCaretAtBeginning();
    Credits_Text.setTextAreaEditable(false);
    KPanel credits_Panel = new KPanel(12, 12, 11, 11);
    credits_Panel.setLayout(new BorderLayout());
    credits_Panel.add(Credits_Text, BorderLayout.CENTER);
    credits_Panel.startFocusForwarding(Credits_Text);
    
    String license = Tools.stringFromTextualSystemResource(
    "org/hypknowsys/diasdem/resources/txt/LICENSE.txt");
    if (license.length() == 0) {
      contact = "The file org/hypknowsys/diasdem/resources/txt/LICENSE.txt\n"
      + "could not be retrieved from CLASSPATH.";
    }
    License_Text = new KScrollTextArea();
    License_Text.setTextAreaLineWrap(true);
    License_Text.setTextAreaWrapStyleWord(true);
    License_Text.setText(license);
    License_Text.setCaretAtBeginning();
    License_Text.setTextAreaEditable(false);
    KPanel license_Panel = new KPanel(12, 12, 11, 11);
    license_Panel.setLayout(new BorderLayout());
    license_Panel.add(License_Text, BorderLayout.CENTER);
    license_Panel.startFocusForwarding(License_Text);
    
    Tabbed_Pane = new KTabbedPane();
    Tabbed_Pane.addTab("Version", version_Panel, KeyEvent.VK_V, true, true);
    Tabbed_Pane.addTab("Contact", contact_Panel, KeyEvent.VK_O, true, false);
    Tabbed_Pane.addTab("Credits", credits_Panel, KeyEvent.VK_R, true, false);
    Tabbed_Pane.addTab("License", license_Panel, KeyEvent.VK_L, true, false);
    
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
  
  public static void main(String pOptions[]) {}
  
}