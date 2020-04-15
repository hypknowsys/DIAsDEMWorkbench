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

package org.hypknowsys.diasdem.client.gui.tools.regexTestEnvironment;

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
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class RegexTestEnvironmentControlPanel extends DiasdemActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String FileName = null;
  private JScrollPane scrollPane = null;
  private JTextPane pane = null;
  private KTextField Regex_Text = null;
  private KGridBagPanel Regex_Panel = null;
  private MutableAttributeSet MatchAttributes = null;
  private MutableAttributeSet NormalAttributes = null;
  private Pattern MyPattern = null;
  private Matcher MyMatcher = null;
  private File CurrentDirectory = null;
  private boolean Wrap = false;

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

  public RegexTestEnvironmentControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public RegexTestEnvironmentControlPanel(Server pDiasdemServer, Project pDiasdemProject, GuiClient pDiasdemGui, GuiClientPreferences pDiasdemGuiPreferences, String pFileName) {
  
    super(pDiasdemServer, pDiasdemProject, pDiasdemGui, pDiasdemGuiPreferences);
    
    ControlPanelContainerIsVisible = false;
    this.initialize();
    if (pFileName != null) {
      File file = new File(pFileName);
      if (file.exists() && file.isFile()) {
        this.displayText(file);
      }
    }
    
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

    if ( ActionCommand.equals("Open") ) {
      this.open();      
    } else if ( ActionCommand.equals("Reload") ) {
      this.reload();
    } else if ( ActionCommand.equals("Match") ) {
      this.match();
    } else if ( ActionCommand.equals("Wrap") ) {
      this.wrap();
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
    
    this.initAttributeSets();

    PriorDiasdemGuiStatus = DO_NOT_MODIFY_DIASDEM_GUI_STATUS;    
    GuiTimer = new javax.swing.Timer(ONE_SECOND, this);

    Wrap = DiasdemGuiPreferences.getBooleanProperty("WRAP_LINES");
    this.createButtonPanel();

    pane = new JTextPane() {
      public boolean getScrollableTracksViewportWidth() {
        if (!Wrap && this.getText().length() > 0)
          return false;
        else
          return super.getScrollableTracksViewportWidth();
      }
    };
    pane.setText("The Quick Brown Fox Jumped Over The Lazy Dog.");
    scrollPane = new JScrollPane(pane);
    scrollPane.setPreferredSize(new Dimension(1024, 768));
    scrollPane.getViewport().setBackground(pane.getBackground());
    
    Regex_Panel = new KGridBagPanel(0, 0, 0, 0);
    Regex_Panel.startFocusForwarding(pane);

    Regex_Text = new KTextField("([Ff]ox|Dog|Cat)");
    Regex_Panel.addLabel("java.util.regex.Pattern:", 0, 0, KeyEvent.VK_P,
    Regex_Text, true, "Please enter the java.util.regex.Pattern regular "
    + "expression to be matched against the text.");
    Regex_Panel.addBlankColumn(1, 0, 12);
    Regex_Panel.addComponent(Regex_Text, 2, 0);
    Regex_Panel.addBlankRow(0, 1, 11);
    Regex_Panel.addComponent(scrollPane, 0, 2, new Insets(0, 0, 0, 0),
    3, 1, 1.0);     
    
    if (FileName != null) {
      File file = new File(FileName);
      if (file.exists() && file.isFile()) {
        this.displayText(file);
      }
    }

    PreferredSizeX = this.getPreferredSizeX();
    PreferredSizeY = this.getPreferredSizeY();
    this.setPreferredSize(new Dimension(PreferredSizeX, PreferredSizeY)); 

    this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));   
    this.setLayout( new BorderLayout() );
    this.add(Regex_Panel, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setControlPanelContainer(TaskControlPanelContainer 
  pControlPanelContainer) {    
    
    ControlPanelContainer = pControlPanelContainer;
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public String getPreferredTitle() {
    
    return "Regex Test Environment";
    
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
    
    return DiasdemGuiPreferences.getDialogLSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return DiasdemGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createButtonPanel() {  

    Button_Panel = new KButtonPanel(17, 0, 0, 0, 6, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("Open", 
      KeyEvent.VK_O, true, false, "Open", this);
    Button_Panel.addNextButton("Reload", 
      KeyEvent.VK_R, false, false, "Reload", this);
    Button_Panel.addNextButton("Match", 
      KeyEvent.VK_M, true, false, "Match", this);
    if (Wrap) {
      Button_Panel.addNextButton("!Wrap",
      KeyEvent.VK_W, true, false, "Wrap", this);
    }
    else {
      Button_Panel.addNextButton("Wrap",
      KeyEvent.VK_W, true, false, "Wrap", this);
    }
    Button_Panel.addNextButton("Close", 
      KeyEvent.VK_C, true, false, "Close", this);
    Button_Panel.addLastButton("Help", 
      KeyEvent.VK_H, false, false, "Help", this);
    
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
    GuiFileChooser.setDialogTitle("Open Regex Test File");
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
        this.displayText(selectedFile);
        scrollPane.setForeground(Color.WHITE);
        String previousRegex = Regex_Text.getText();
        pane.validate();
        scrollPane.validate();
        this.removeAll();
        this.validate();
        KGridBagPanel Regex_Panel = new KGridBagPanel(0, 0, 0, 0);
        Regex_Panel.startFocusForwarding(pane);
        Regex_Text = new KTextField(previousRegex);
        Regex_Panel.addLabel("java.util.regex.Pattern:", 0, 0, KeyEvent.VK_P,
        Regex_Text, true, "Please enter the java.util.regex.Pattern regular "
        + "expression to be matched against the text.");
        Regex_Panel.addBlankColumn(1, 0, 12);
        Regex_Panel.addComponent(Regex_Text, 2, 0);
        Regex_Panel.addBlankRow(0, 1, 11);
        Regex_Panel.addComponent(scrollPane, 0, 2, new Insets(0, 0, 0, 0),
        3, 1, 1.0);
        this.add(Regex_Panel, BorderLayout.CENTER);
        this.add(Button_Panel, BorderLayout.SOUTH);
        this.validate();
        pane.setCaretPosition(0);
        Tools.requestFocus(scrollPane, pane);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  private void wrap() {
    
    if (Wrap) {
      Button_Panel.setLabel(3, "Wrap");
      DiasdemGuiPreferences.setBooleanProperty("WRAP_LINES", false);
    }
    else {
      Button_Panel.setLabel(3, "!Wrap");
      DiasdemGuiPreferences.setBooleanProperty("WRAP_LINES", true);
    }
    Button_Panel.validate();
    Wrap = !Wrap;
    pane.validate();
    scrollPane.validate();

    String previousRegex = Regex_Text.getText();
    this.removeAll();
    this.validate();
    KGridBagPanel Regex_Panel = new KGridBagPanel(0, 0, 0, 0);
    Regex_Panel.startFocusForwarding(pane);
    Regex_Text = new KTextField(previousRegex);
    Regex_Panel.addLabel("java.util.regex.Pattern:", 0, 0, KeyEvent.VK_P,
    Regex_Text, true, "Please enter the java.util.regex.Pattern regular "
    + "expression to be matched against the text.");
    Regex_Panel.addBlankColumn(1, 0, 12);
    Regex_Panel.addComponent(Regex_Text, 2, 0);
    Regex_Panel.addBlankRow(0, 1, 11);
    Regex_Panel.addComponent(scrollPane, 0, 2, new Insets(0, 0, 0, 0),
    3, 1, 1.0);     
    this.add(Regex_Panel, BorderLayout.CENTER);
    this.add(Button_Panel, BorderLayout.SOUTH);
    this.validate();
    Tools.requestFocus(scrollPane, pane);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void initAttributeSets() {
    
    NormalAttributes = new SimpleAttributeSet();
    MatchAttributes = new SimpleAttributeSet();
    StyleConstants.setBold(MatchAttributes, true);
    StyleConstants.setForeground(MatchAttributes, Color.yellow);
    StyleConstants.setBackground(MatchAttributes, Color.blue);
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void match() {

    if (Regex_Text.getText().trim().length() == 0) {
      JOptionPane.showMessageDialog(ControlPanelContainer.getParentJFrame(), 
      "Please enter a regular expression!", 
      this.getPreferredTitle(), JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (pane.getText().trim().length() == 0) {
      JOptionPane.showMessageDialog(ControlPanelContainer.getParentJFrame(), 
      "Please enter a text or open an existing file!", 
      this.getPreferredTitle(), JOptionPane.ERROR_MESSAGE);
      return;
    }

    pane.getStyledDocument().setCharacterAttributes(0, pane.getText()
    .length(), NormalAttributes, true);
    try {
      MyPattern = Pattern.compile(Regex_Text.getText().trim());
      String text = pane.getText();
      MyMatcher = MyPattern.matcher(text);
      while (MyMatcher.find()) {
        pane.getStyledDocument().setCharacterAttributes(MyMatcher.start(),
        MyMatcher.group().length(), MatchAttributes, true);
      }
    }
    catch (PatternSyntaxException e) {
      JOptionPane.showMessageDialog(ControlPanelContainer.getParentJFrame(), 
      "Regex Syntax Error: See System.out for Details.", 
      this.getPreferredTitle(), JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void displayText(File pFile) {

    TextBufferedReader file = new TextBufferedReader(pFile, 1000000);
    file.open();
    StringBuffer buffer = new StringBuffer((int)pFile.length());
    String line = file.getFirstLine();
    while (line != null) {
      buffer.append(line);
      buffer.append("\n");
      line = file.getNextLine();
    }
    file.close();
    pane.setText(buffer.toString());
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}