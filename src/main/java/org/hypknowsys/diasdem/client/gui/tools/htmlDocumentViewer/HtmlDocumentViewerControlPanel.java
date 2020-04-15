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

package org.hypknowsys.diasdem.client.gui.tools.htmlDocumentViewer;

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
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.2, 15 May 2004
 * @author Karsten Winkler
 */
  
public class HtmlDocumentViewerControlPanel extends DiasdemActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String FileName = null;
  private JScrollPane scrollPane = null;
  private JEditorPane pane = null;
  private File CurrentDirectory = null;
  private boolean IsHelpDisplay = false;

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

  public HtmlDocumentViewerControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public HtmlDocumentViewerControlPanel(Server pDiasdemServer, Project pDiasdemProject, 
  GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences, String pFileName) {
  
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
    ControlPanelContainerIsVisible = false;
    this.initialize();
    if (pFileName != null) {
      File file = new File(pFileName);
      if (file.exists() && file.isFile()) {
        this.displayHtmlDocument(file);
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getFileName() {
    return FileName; }
  public boolean isHelpDisplay() {
    return IsHelpDisplay;
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setFileName(String pFileName) {
    FileName = pFileName; }
  public void setIsHelpDisplay(boolean pIsHelpDisplay) {
    IsHelpDisplay = pIsHelpDisplay;
  }

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

    if ( ActionCommand.equals("Open") ) {
      this.open();      
    } else if ( ActionCommand.equals("Reload") ) {
      this.reload();
    } else if ( ActionCommand.equals("Close") ) {
      DiasdemGuiPreferences.quickSave();
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
    
    PriorDiasdemGuiStatus = DO_NOT_MODIFY_DIASDEM_GUI_STATUS;    
    GuiTimer = new javax.swing.Timer(ONE_SECOND, this);

    this.createButtonPanel();
    
    pane = new JEditorPane();
    pane.setEditable(false);
    pane.addHyperlinkListener(new HyperlinkListener() {
      public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          try {
            pane.setPage(event.getURL());
          }
          catch (IOException e) {
            System.out.println(e.getMessage());
          }
        }
      }
    });

    scrollPane = new JScrollPane(pane);
    scrollPane.setPreferredSize(new Dimension(1024, 768));
    scrollPane.getViewport().setBackground(pane.getBackground());
    if (FileName != null) {
      File file = new File(FileName);
      if (file.exists() && file.isFile()) {
        this.displayHtmlDocument(file);
      }
    }

    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 

    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));   
    this.setLayout( new BorderLayout() );
    this.add(scrollPane, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setControlPanelContainer(TaskControlPanelContainer 
  pControlPanelContainer) {    
    
    ControlPanelContainer = pControlPanelContainer;
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    
    return "HTML Document Viewer";
    
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
    
    if (IsHelpDisplay) {
      return DiasdemGuiPreferences.getDialogMSizeX();
    }
    else {
     return DiasdemGuiPreferences.getDialogLSizeX();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createButtonPanel() {  

    if (IsHelpDisplay) {
      Button_Panel = new KButtonPanel(17, 0, 0, 0, 1,
      KButtonPanel.HORIZONTAL_RIGHT);
      Button_Panel.addSingleButton("Close",
      KeyEvent.VK_C, true, false, "Close", this);
    }
    else {
      Button_Panel = new KButtonPanel(17, 0, 0, 0, 4,
      KButtonPanel.HORIZONTAL_RIGHT);
      Button_Panel.addFirstButton("Open",
      KeyEvent.VK_O, true, false, "Open", this);
      Button_Panel.addNextButton("Reload",
      KeyEvent.VK_R, false, false, "Reload", this);
      Button_Panel.addNextButton("Close",
      KeyEvent.VK_C, true, false, "Close", this);
      Button_Panel.addLastButton("Help",
      KeyEvent.VK_H, false, false, "Help", this);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void createParameterPanel() {  

    Parameter_Panel = null;
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */

  private void open() {
    
    if (FileName != null) {
      File file = new File(FileName);
      GuiFileChooser = new JFileChooser(file);
      GuiFileChooser.setSelectedFile(file);
    } 
    else if (CurrentDirectory != null) {
      GuiFileChooser = new JFileChooser(CurrentDirectory);
    }
    else {
      GuiFileChooser = new JFileChooser(DiasdemProject.getProperty(
      "PROJECT_DIRECTORY"));
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setMultiSelectionEnabled(false);
    GuiFileChooser.setAcceptAllFileFilterUsed(true);
    GuiFileChooser.setDialogTitle("View HTML File");
    GuiFileChooser.setFileFilter(GuiClientPreferences.HTML_FILE_FILTER);
    int result = GuiFileChooser.showOpenDialog(ControlPanelContainer
    .getParentJFrame());
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = GuiFileChooser.getSelectedFile();
      if (selectedFile.exists() && selectedFile.isFile()) {
        FileName = selectedFile.getAbsolutePath();
        this.reload();
        ControlPanelContainer.setTitle(this.getPreferredTitle()
        + " [" + Tools.shortenFileName(FileName, 50) + "]");
        CurrentDirectory = selectedFile.getParentFile();
        Button_Panel.setEnabled(1, true);  // Reload
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  private void reload() {
    
    if (FileName != null) {
      File selectedFile = new File(FileName);
      if (selectedFile.exists() && selectedFile.isFile()) {
        this.displayHtmlDocument(selectedFile);
        scrollPane.setForeground(Color.WHITE);
        this.remove(scrollPane);
        this.validate();
        this.add(scrollPane, BorderLayout.CENTER);
        this.validate();
        Tools.requestFocus(scrollPane, pane);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void displayHtmlDocument(File pFile) {

    if (pane != null && pFile.exists()) {
      try {
        // http://java.sun.com/j2se/1.5.0/docs/api/javax/swing/JEditorPane.html
        Document document = pane.getDocument();
        document.putProperty(Document.StreamDescriptionProperty, null);
        pane.setPage("file:" + pFile.getAbsoluteFile());
      }
      catch (IOException e) {
        System.out.println(e.getMessage());
      }
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