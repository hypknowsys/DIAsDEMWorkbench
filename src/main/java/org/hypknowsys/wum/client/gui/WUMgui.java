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

package org.hypknowsys.wum.client.gui;

import java.lang.reflect.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*; 
import org.hypknowsys.wum.client.gui.*;

/**
 * @version 0.9, 15 August 2003
 * @author Karsten Winkler
 */

public class WUMgui extends KFrame implements GuiClient {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected GuiClientPreferences GuiPreferences = null;

  protected Server WumServer = null; 
  protected Project WumProject = null;
  protected GuiClientMenuBar GuiMenuBar = null; 
  protected String[] PlugInClassNames = null;
  
  protected KDesktopPane MyDesktopPane = null;
  protected KLogPanel LogPanel = null;  
  protected int GuiStatus = 0;
    
  public final static String PLUG_IN_FILE_NAME = "WUM.plugins";
  public final static String GUI_PREFERENES_FILE_NAME = "WUM.config";
  public final static String[] PLUG_IN_CLASS_NAMES = {
    "org.hypknowsys.wum.tasks.project.newProject.NewProjectTask",
    "org.hypknowsys.wum.tasks.project.openProject.OpenProjectTask",
    "org.hypknowsys.wum.tasks.project.closeProject.CloseProjectTask",
    "org.hypknowsys.wum.tasks.understand.generateSummaryReport.GenerateSummaryReportTask",
    "org.hypknowsys.wum.tasks.prepare.importLogFile.ImportLogFileTask",
    "org.hypknowsys.wum.tasks.prepare.sessionizeLogFiles.SessionizeLogFilesTask",
    "org.hypknowsys.wum.tasks.prepare.aggregateSessions.AggregateSessionsTask",
    "org.hypknowsys.wum.resources.templates.taskTemplate.TemplateTask",
    // "org.hypknowsys.wum.resources.templates.blockingTaskTemplate.TemplateTask",
    "org.hypknowsys.wum.resources.templates.nonBlockingTaskTemplate.TemplateTask",
    "org.hypknowsys.wum.client.gui.tools.xmlDocumentViewer.XmlDocumentViewerTask",
    "org.hypknowsys.wum.client.gui.tools.htmlDocumentViewer.HtmlDocumentViewerTask"
  };
  
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

  public WUMgui() {

    super();

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public GuiClientMenuBar getGuiMenuBar() { 
    return GuiMenuBar; }
  public KDesktopPane getDesktopPane() { 
    return MyDesktopPane; }
  public Project getProject() {   
    return WumProject; }
  public int getGuiStatus()  {     
    return GuiStatus; }   
  public JFrame getJFrame()  {   
    return (JFrame)this; }
  public KDesktopPane getKDesktopPane() { 
    return MyDesktopPane; }  
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setProject(Project pWumProject) {    
    WumProject = pWumProject; this.registerPluginProperties(); }

  /* ########## ########## ########## ########## ########## ######### */

  public void setGuiMenuBar(GuiClientMenuBar pGuiMenuBar) { 
    
    if (pGuiMenuBar instanceof WUMguiMenuBar) {
      GuiMenuBar = (WUMguiMenuBar)pGuiMenuBar;
      this.setJMenuBar(GuiMenuBar.getJMenuBar());
    }
    else {
      System.err.println("[WUMgui] Error in setGuiMenuBar() ..."); 
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  /** 
   * enables resp. disables nenuitems according status code
   * which is determined by program progress
   * @param   pStatus   code representing programm progress:
   * 
   *  GuiClient.PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING suspend any menu events in main frame: task is currently running
   *  0  no web site at all exists,
   */
   
  public int setGuiStatus(int pStatus)  {   
  
    int previousStatus = GuiStatus;
    GuiStatus = pStatus;
    GuiMenuBar.setGuiStatus(pStatus);
    this.getContentPane().validate();
    
    return previousStatus;
     
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
  /* ########## interface GuiClient methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContext(GuiClientPreferences pGuiPreferences,
  String[] pPlugInClassNames) {
  
    GuiPreferences = pGuiPreferences;
    PlugInClassNames = pPlugInClassNames;
    super.setContext(GuiPreferences.getProperty("WUM_WORKBENCH_TITLE"), 
    GuiPreferences);
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setOpened(boolean pOpened) {
  
    if (pOpened) {
      this.initialize();
      this.setVisible(true);
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setClosed(boolean pClosed) {
  
    if (pClosed) {
      try {
        this.finalize();
        this.setVisible(false);
        System.out.println("\n\nThank you for using " + GuiPreferences
        .getProperty("WUM_WORKBENCH_TITLE") + "!\n");
        System.out.flush();
        System.exit(0);
      }
      catch (PropertyVetoException e) {
        System.out.println("Warning: " + GuiPreferences
        .getProperty("WUM_WORKBENCH_TITLE") + " cannot be closed!");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void initialize() {
  
    if (GuiPreferences == null) {
      GuiPreferences = new WUMguiPreferences();
    }
    GuiPreferences.setProperty("GUI_DEFAULT_DIRECTORY",
    Tools.extractDirectory(GuiPreferences.getFileName()));

    WumServer = new WUMserver();
    WumServer.setTaskThreadPriority(GuiPreferences.getIntProperty(
    "TASK_THREAD_PRIORITY"));
    WumProject = new DefaultWUMproject();
    this.registerPluginProperties();
    
  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void finalize() throws PropertyVetoException {
    
    if (this.getState() != Frame.ICONIFIED) {
      Point location = this.getLocation();
      GuiPreferences.setProperty("FRAME_POSITION_X",
      ( new Integer( location.x ) ).toString() );
      GuiPreferences.setProperty("FRAME_POSITION_Y",
      ( new Integer( location.y ) ).toString() );
      Dimension dimension = this.getSize();
      GuiPreferences.setProperty("FRAME_SIZE_X",
      ( new Integer( dimension.width ) ).toString() );
      GuiPreferences.setProperty("FRAME_SIZE_Y",
      ( new Integer( dimension.height ) ).toString() );
      GuiPreferences.quickSave();
    }
    WumServer.terminateSession();
    super.finalize();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setVisible(boolean pVisible) {
  
    if (pVisible) {
      MyDesktopPane = new KDesktopPane((JFrame)this);
      this.setContentPane(MyDesktopPane);      

      GuiMenuBar = new WUMguiMenuBar(WumServer, (GuiClient)this,
      GuiPreferences, GuiClient.GUI_STARTED_NO_PROJECT_OPENED, PlugInClassNames);
      
      this.setTitle(GuiPreferences.getProperty("WUM_WORKBENCH_TITLE"));
      this.setGuiStatus(GuiClient.GUI_STARTED_NO_PROJECT_OPENED);
      this.validate();
      super.setVisible(true);
      
      LogPanel = new KLogPanel();
      KInternalFrame logFrame = new KInternalFrame("Log Window",
      true, false, true, true, 450, 225, LogPanel);
      MyDesktopPane.add(logFrame);
      
      if (GuiPreferences.getShowWelcomeWindow()) {
        WUMguiWelcome logoFrame = new WUMguiWelcome(this,
        GuiPreferences);
        MyDesktopPane.add(logoFrame);
      }
      
      LogPanel.logInfoMessage("Welcome to " + GuiPreferences
        .getProperty("WUM_WORKBENCH_TITLE") + "!"); 
    }
    else {
      super.setVisible(pVisible);
    }
   
  }

  public void addKInternalFrame(KInternalFrame pNewInternalFrame) {
    
    MyDesktopPane.add(pNewInternalFrame); 
    
  } 
 
  /* ########## ########## ########## ########## ########## ######### */  

  public void setWaitCursor() {

    this.setCursor( new Cursor(Cursor.WAIT_CURSOR) );

  }

  /* ########## ########## ########## ########## ########## ######### */  

  public void setDefaultCursor() {

    this.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );

  } 

  /* ########## ########## ########## ########## ########## ######### */  

  public String logInfoMessage(String pInfoMessage) {

    return LogPanel.logInfoMessage(pInfoMessage);

  }

  /* ########## ########## ########## ########## ########## ######### */  

  public String logWarningMessage(String pWarningMessage) {

    return LogPanel.logWarningMessage(pWarningMessage);

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public KInternalFrame launchTaskControlPanelAsInternalFrame(
  TaskControlPanel pTaskControlPanel) {
    
    if (pTaskControlPanel instanceof WumControlPanel) {
      WumInternalFrame frame = new WumInternalFrame(
      true, true, true, true, WumServer, WumProject, this,
      GuiPreferences, pTaskControlPanel, this.getJFrame(),
      (JDesktopPane)this.getKDesktopPane());
      if (frame.controlPanelContainerIsVisible()) {
        MyDesktopPane.add(frame, frame.getPreferredKDesktopLayout());
      }
      return frame;
    }
    else if (LogPanel != null) {
      LogPanel.logWarningMessage("Task panel \"" + pTaskControlPanel
      .getPreferredTitle() + "\" cannot be launched!");
      return null;
    }
    else {
      return null;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setLookAndFeel(String pLookAndFeelClassName, boolean pUpdateUi) {
    
    // setup jGoodies Looks options
    if (pLookAndFeelClassName.startsWith("com.jgoodies.plaf.")) {
      UIManager.put(com.jgoodies.plaf.Options.USE_SYSTEM_FONTS_APP_KEY, 
      new Boolean(true));
      UIManager.put(com.jgoodies.plaf.Options.DEFAULT_ICON_SIZE_KEY, 
      new Dimension(18, 18));
      com.jgoodies.plaf.Options.setGlobalFontSizeHints(
      com.jgoodies.plaf.FontSizeHints.MIXED);
    }
    try {
      UIManager.setLookAndFeel(pLookAndFeelClassName);
    } catch (Exception e) {
      System.err.println("Error: Java look and feel class " + 
      pLookAndFeelClassName + " is not in the class path.");
    }
    if (pUpdateUi) {
      SwingUtilities.updateComponentTreeUI((Component)this); 
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface ActionListener methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void actionPerformed(ActionEvent e) {
  
    String actionCommand = e.getActionCommand();
    Object actionSource = e.getSource();
    
    if (actionCommand.equals("File:Exit")) {
      this.setClosed(true);
    } 
    else if (actionSource instanceof KMenuItem
    && ((KMenuItem)actionSource).getTaskClassName() != null) {

      Task task = null;
      try {
        task = (Task)Class
        .forName(((KMenuItem)actionSource).getTaskClassName())
        .getConstructor(null).newInstance(null);
      }
      catch(Exception e2) { 
        if (LogPanel != null) {
          LogPanel.logWarningMessage("Task panel " + ((KMenuItem)actionSource)
          .getTaskClassName() + " cannot be launched!");
        }
        e2.printStackTrace(); 
      }
      
      if (task != null) {
        if (actionCommand.startsWith(WUMguiMenuBar.FILE_DIALOG)) {
          WumDialog dialog = new WumDialog(this.getJFrame());
          dialog.setContext(this.getJFrame(), task.getLabel(), true,
          WumServer, WumProject, this, GuiPreferences, task
          .getTaskControlPanel(WumServer, WumProject, this,
          GuiPreferences));
          dialog.initialize();
          dialog.setVisible(true);
        }
        else if (actionCommand.startsWith(WUMguiMenuBar.ACTIONS)
        || actionCommand.startsWith(WUMguiMenuBar.SOLUTIONS)
        || actionCommand.startsWith(WUMguiMenuBar.TOOLS)
        || actionCommand.startsWith(WUMguiMenuBar.HELP)) {
          WumInternalFrame frame = new WumInternalFrame(
          true, true, true, true, WumServer, WumProject, this,
          GuiPreferences, task.getTaskControlPanel(WumServer,
          WumProject, this, GuiPreferences), this.getJFrame(),
          (JDesktopPane)this.getKDesktopPane());
          if (frame.controlPanelContainerIsVisible()) {
            MyDesktopPane.add(frame, frame.getPreferredKDesktopLayout());
          }
        }
        else if (LogPanel != null) {
          LogPanel.logWarningMessage("Task panel " + ((KMenuItem)actionSource)
          .getTaskClassName() + " cannot be launched!");
        }
      }
    }  
    this.requestFocus();
      
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void registerPluginProperties() {
  
    for (int i = 0; i < PlugInClassNames.length; i++) {
      Task task = WumServer.instantiateTask(PlugInClassNames[i],
      WumServer);
      if (task != null && WumProject != null) {
        WumProject.registerButDontReplaceKProperties(task
        .getProjectPropertyData());
      }
      if (task != null && GuiPreferences != null) {
        GuiPreferences.registerButDontReplaceKProperties(task
        .getGuiClientPropertyData());
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void launchTaskControlPanelAsInternalFrame(String pTaskClassName) {
    
    Task task = null;
    try {
      task = (Task)Class.forName(pTaskClassName)
      .getConstructor(null).newInstance(null);
    }
    catch(Exception e2) {
      if (LogPanel != null) {
        LogPanel.logWarningMessage("Task panel " + pTaskClassName
        + " cannot be launched!");
      }
      e2.printStackTrace();
    }
    
    if (task != null) {
      this.launchTaskControlPanelAsInternalFrame(task.getTaskControlPanel(
      WumServer, WumProject, this, GuiPreferences));
    }
    else if (LogPanel != null) {
      LogPanel.logWarningMessage("Task panel " + pTaskClassName
      + " cannot be launched!");
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {
  
    // setting the locale only works out before creating a SWING component
    Locale.setDefault(Locale.US);
    
    String errorString = 
    "\nWUM Workbench requires its valid home directory ${WUM_HOME} as the\n"
    + "1st parameter. Example: java org.hypknowsys.wum.client.gui.WUMgui /opt/wum/";
    String warningString = 
    "\nWUM Workbench cannot locate a valid plug-in file "
    + WUMgui.PLUG_IN_FILE_NAME + "\nin its home directory. "
    + "The default plug-ins will be used instead.";
    File directory = null;
    String guiPreferencesFile = null;
    GuiClientPreferences guiPreferences = null;
    String plugInFile = null;
    String[] plugInClassNames = null;
    WUMgui wumGui = null;
    
    if (args != null && args.length > 0) {
      directory = new File(args[0].trim());
      if (directory.exists() && directory.isDirectory()) {
        if ( !args[0].endsWith(File.separator) ) {
          guiPreferencesFile = args[0].trim() + File.separator
          + WUMgui.GUI_PREFERENES_FILE_NAME;
          plugInFile = args[0].trim() + File.separator
          + WUMgui.PLUG_IN_FILE_NAME;
        }
        else {
          guiPreferencesFile = args[0].trim() 
          + WUMgui.GUI_PREFERENES_FILE_NAME;
          plugInFile = args[0].trim() 
          + WUMgui.PLUG_IN_FILE_NAME;
        }
      }
    }
    
    if (plugInFile != null) {
      File file = new File(plugInFile);
      if (file.exists()) {
        TextFile plugInTextFile = new TextFile(file); 
        plugInTextFile.open();
        int counter = 0;
        String line = null;
        line = plugInTextFile.getFirstLineButIgnoreCommentsAndEmptyLines();
        while (line != null) {
          counter++;
          line = plugInTextFile.getNextLineButIgnoreCommentsAndEmptyLines();
        }
        plugInClassNames = new String[counter];
        counter = 0;
        line = plugInTextFile.getFirstLineButIgnoreCommentsAndEmptyLines();
        while (line != null) {
          plugInClassNames[counter++] = line.trim();
          line = plugInTextFile.getNextLineButIgnoreCommentsAndEmptyLines();
        }
        plugInTextFile.close();
      }
      else {
        System.err.println(warningString);
      }
    }

    if (guiPreferencesFile != null) {
      try {
        TextFile plugInTextFile = new TextFile(new File(plugInFile));
        if (Tools.isExistingFile(guiPreferencesFile)) {
          guiPreferences = new WUMguiPreferences(guiPreferencesFile, 
          KProperties.LOAD);
        }
        else {
          guiPreferences = new WUMguiPreferences(guiPreferencesFile, 
          KProperties.CREATE);
        }
        wumGui = new WUMgui();
        if (plugInClassNames == null) {
          wumGui.setContext(guiPreferences, 
          WUMgui.PLUG_IN_CLASS_NAMES);
        }
        else {
          wumGui.setContext(guiPreferences, plugInClassNames);
        }
        wumGui.setLookAndFeel(guiPreferences.getLookAndFeelClassName(), 
        false);
        wumGui.setOpened(true);
      }
      catch (IOException e) {
        System.err.println(errorString);
      }
    }
    else {
      System.err.println(errorString);
    }
  
  }

}