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

package org.hypknowsys.wum.client.gui.tools.xmlDocumentViewer;

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
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class XmlDocumentViewerControlPanel extends WumActionsControlPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String FileName = null;
  private JScrollPane scrollPane = null;
  private JTextPane pane = null;
  private MutableAttributeSet tagAttributes = null;
  private MutableAttributeSet elementAttributes = null; 
  private MutableAttributeSet attributeValueAttributes = null; 
  private MutableAttributeSet characterAttributes = null;
  private MutableAttributeSet cdataAttributes = null;
  private Pattern partPattern = null;
  private Pattern namePattern = null;
  private Pattern attributePattern = null;
  private Matcher partMatcher = null;
  private Matcher nameMatcher = null;
  private Matcher attributeMatcher = null;
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

  public XmlDocumentViewerControlPanel() {
  
    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public XmlDocumentViewerControlPanel(Server pWumServer, 
  Project pWumProject, GuiClient pWumGui, 
  GuiClientPreferences pWumGuiPreferences,
  String pFileName) {
  
    super(pWumServer, pWumProject, pWumGui, pWumGuiPreferences);
    
    ControlPanelContainerIsVisible = false;
    this.initialize();
    if (pFileName != null) {
      File file = new File(pFileName);
      if (file.exists() && file.isFile()) {
        this.displayXmlDocument(file);
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
    } else if ( ActionCommand.equals("Wrap") ) {
      this.wrap();
    } else if ( ActionCommand.equals("Close") ) {
      WumGuiPreferences.quickSave();
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
    this.initPatterns();

    PriorWumGuiStatus = DO_NOT_MODIFY_WUM_GUI_STATUS;    
    GuiTimer = new javax.swing.Timer(ONE_SECOND, this);

    Wrap = WumGuiPreferences.getBooleanProperty("WRAP_LINES");
    this.createButtonPanel();

    pane = new JTextPane() {
      public boolean getScrollableTracksViewportWidth() {
        if (!Wrap && this.getText().length() > 0)
          return false;
        else
          return super.getScrollableTracksViewportWidth();
      }
    };
    scrollPane = new JScrollPane(pane);
    scrollPane.setPreferredSize(new Dimension(1024, 768));
    scrollPane.getViewport().setBackground(pane.getBackground());
    if (FileName != null) {
      File file = new File(FileName);
      if (file.exists() && file.isFile()) {
        this.displayXmlDocument(file);
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
    
    return "XML Document Viewer";
    
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
    
    return WumGuiPreferences.getDialogLSizeX();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public int getPreferredSizeY() {
    
    return WumGuiPreferences.getDialogLSizeY();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void createButtonPanel() {  

    Button_Panel = new KButtonPanel(17, 0, 0, 0, 5, 
      KButtonPanel.HORIZONTAL_RIGHT);
    Button_Panel.addFirstButton("Open", 
      KeyEvent.VK_O, true, false, "Open", this);
    Button_Panel.addNextButton("Reload", 
      KeyEvent.VK_R, false, false, "Reload", this);
    if (Wrap) {
      Button_Panel.addNextButton("!Wrap",
      KeyEvent.VK_W, false, false, "Wrap", this);
    }
    else {
      Button_Panel.addNextButton("Wrap",
      KeyEvent.VK_W, false, false, "Wrap", this);
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
      GuiFileChooser = new JFileChooser(WumProject.getProperty(
      "PROJECT_DIRECTORY"));
    }
    GuiFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    GuiFileChooser.setMultiSelectionEnabled(false);
    GuiFileChooser.setAcceptAllFileFilterUsed(true);
    GuiFileChooser.setDialogTitle("View XML File");
    GuiFileChooser.setFileFilter(GuiClientPreferences.XML_FILE_FILTER);
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
        Button_Panel.setEnabled(2, true);  // Wrap/!Wrap
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  private void reload() {
    
    if (FileName != null) {
      File selectedFile = new File(FileName);
      if (selectedFile.exists() && selectedFile.isFile()) {
        this.displayXmlDocument(selectedFile);
        scrollPane.setForeground(Color.WHITE);
        this.remove(scrollPane);
        this.validate();
        this.add(scrollPane, BorderLayout.CENTER);
        this.validate();
        pane.setCaretPosition(0);
        Tools.requestFocus(scrollPane, pane);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  private void wrap() {
    
    if (Wrap) {
      Button_Panel.setLabel(2, "Wrap");
      WumGuiPreferences.setBooleanProperty("WRAP_LINES", false);
    }
    else {
      Button_Panel.setLabel(2, "!Wrap");
      WumGuiPreferences.setBooleanProperty("WRAP_LINES", true);
    }
    Button_Panel.validate();
    Wrap = !Wrap;
    pane.validate();
    scrollPane.validate();
    this.remove(scrollPane);
    this.validate();
    this.add(scrollPane, BorderLayout.CENTER);
    this.validate();
    Tools.requestFocus(scrollPane, pane);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void initAttributeSets() {
    
    tagAttributes = new SimpleAttributeSet();
    StyleConstants.setForeground(tagAttributes, Color.blue);

    attributeValueAttributes = new SimpleAttributeSet();

    cdataAttributes = new SimpleAttributeSet();
    StyleConstants.setFontFamily(cdataAttributes, "Courier");

    characterAttributes = new SimpleAttributeSet();
    StyleConstants.setBold(characterAttributes, true);

    elementAttributes = new SimpleAttributeSet();
    StyleConstants.setForeground(elementAttributes, new Color(153,0,0));
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void initPatterns() {

    // REX/Javascript 1.0 
    // Robert D. Cameron "REX: XML Shallow Parsing with Regular Expressions",
    // Technical Report TR 1998-17, School of Computing Science, Simon Fraser 
    // University, November, 1998.
    // Copyright (c) 1998, Robert D. Cameron. 
    // The following code may be freely used and distributed provided that
    // this copyright and citation notice remains intact and that modifications
    // or additions are clearly identified.
    
    // modified to fit java.util.regex.

    String TextSE = "[^<]+";
    String UntilHyphen = "[^-]*-";
    String Until2Hyphens = UntilHyphen + "([^-]" + UntilHyphen + ")*-";
    String CommentCE = Until2Hyphens + ">?";
    String UntilRSBs = "[^]]*]([^]]+])*]+";
    String CDATA_CE = UntilRSBs + "([^]>]" + UntilRSBs + ")*>";
    String S = "[ \\n\\t\\r]+";
    String NameStrt = "[A-Za-z_:]|[^\\x00-\\x7F]";
    String NameChar = "[A-Za-z0-9_:.-]|[^\\x00-\\x7F]";
    String Name = "(" + NameStrt + ")(" + NameChar + ")*";
    String QuoteSE = "\"[^\"]" + "*" + "\"" + "|'[^']*'";
    String DT_IdentSE = S + Name + "(" + S + "(" + Name + "|" + QuoteSE + "))*";
    String MarkupDeclCE = "([^]\"'><]+|" + QuoteSE + ")*>";
    String S1 = "[\\n\\r\\t ]";
    String UntilQMs = "[^?]*\\?+";
    String PI_Tail = "\\?>|" + S1 + UntilQMs + "([^>?]" + UntilQMs + ")*>";
    String DT_ItemSE = "<(!(--" + Until2Hyphens + ">|[^-]" + MarkupDeclCE + ")|\\?" + Name + "(" +
      PI_Tail + "))|%" + Name + ";|" + S;
    String DocTypeCE = DT_IdentSE + "(" + S + ")?(\\[(" + DT_ItemSE + ")*](" + S + ")?)?>?";
    String DeclCE = "--(" + CommentCE + ")?|\\[CDATA\\[(" + CDATA_CE + ")?|DOCTYPE(" + DocTypeCE +
      ")?";
    String PI_CE = Name + "(" + PI_Tail + ")?";
    String EndTagCE = Name + "(" + S + ")?>?";
    String AttValSE = "\"[^<\"]" + "*" + "\"" + "|'[^<']*'";
    String ElemTagCE = Name + "(" + S + Name + "(" + S + ")?=(" + S + ")?(" + AttValSE + "))*(" + S
      + ")?/?>?";
    String MarkupSPE = "<(!(" + DeclCE + ")?|\\?(" + PI_CE + ")?|/(" + EndTagCE + ")?|(" +
      ElemTagCE + ")?)";
    String XML_SPE = TextSE + "|" + MarkupSPE;
    
    partPattern = Pattern.compile(XML_SPE);
    namePattern = Pattern.compile(Name);
    attributePattern = Pattern.compile(AttValSE);
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void displayXmlDocument(File pFile) {

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

    String text = pane.getText();
    nameMatcher = namePattern.matcher(text);
    partMatcher = partPattern.matcher(text);
    attributeMatcher = attributePattern.matcher(text);
    while (partMatcher.find()) {
      formatPart(partMatcher);
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void formatPart(Matcher partMatcher) {
    
    String part = partMatcher.group();
    if (!part.startsWith("<")) {
      applyAttributeSet(partMatcher, characterAttributes);
    }
    else {
      applyAttributeSet(partMatcher, tagAttributes);
      if (!part.startsWith("<?") && !part.startsWith("<!")) {
        formatElement(partMatcher);
      }
      if (part.startsWith("<![CDATA[") && part.endsWith("]]>")) {
        applyAttributeSet(partMatcher, cdataAttributes, 9, 12);
      }
    }
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private void formatElement(Matcher partMatcher) {
    
    nameMatcher.find(partMatcher.start());
    do {
      applyAttributeSet(nameMatcher, elementAttributes);
      if (! nameMatcher.find()) break;
    }
    while(nameMatcher.end() < partMatcher.end());
    
    if (!attributeMatcher.find(partMatcher.start())) return;
    do {
      applyAttributeSet(attributeMatcher, attributeValueAttributes, 1, 2);
      if (! attributeMatcher.find()) break;
    }
    while(attributeMatcher.end() < partMatcher.end());
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void applyAttributeSet(Matcher m, AttributeSet attributeSet) {
    
    applyAttributeSet(m, attributeSet, 0, 0);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void applyAttributeSet(Matcher m, AttributeSet attributeSet, 
  int additionalOffset, int lengthReduction) {
    
    int offset = m.start();
    int length = m.group().length();
    pane.getStyledDocument().
    setCharacterAttributes(offset+additionalOffset,
    length - lengthReduction, attributeSet, true);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}