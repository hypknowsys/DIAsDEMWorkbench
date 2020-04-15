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

import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.Options;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Locale;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientMenuBar;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskControlPanel;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMproject;
import org.hypknowsys.diasdem.server.DIAsDEMserver;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KDesktopPane;
import org.hypknowsys.misc.swing.KFrame;
import org.hypknowsys.misc.swing.KInternalFrame;
import org.hypknowsys.misc.swing.KLogPanel;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.KProperties;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;

/**
 * @version 2.1.0.3, 29 October 2003
 * @author Karsten Winkler
 */

public class DIAsDEMgui extends KFrame implements GuiClient {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected GuiClientPreferences GuiPreferences = null;

  protected Server DiasdemServer = null; 
  protected Project DiasdemProject = null;
  protected GuiClientMenuBar GuiMenuBar = null; 
  protected String[] PlugInClassNames = null;
  
  protected KDesktopPane MyDesktopPane = null;
  protected KLogPanel LogPanel = null;  
  protected int GuiStatus = 0;
    
  public final static String PLUG_IN_FILE_NAME = "DIAsDEM.plugins";
  public final static String GUI_PREFERENES_FILE_NAME = "DIAsDEM.config";
  public final static String[] PLUG_IN_CLASS_NAMES = {
    "org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics.ComputeTermFrequencyStatisticsTask",
    "org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter.FindCollocationsFrequencyPosFilterTask",
    "org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus.EstablishInitialThesaurusTask",
    "org.hypknowsys.diasdem.tasks.prepare.createDocumentCollection.CreateDocumentCollectionTask",
    "org.hypknowsys.diasdem.tasks.prepare.importPlainTextFiles.ImportPlainTextFilesTask",
    "org.hypknowsys.diasdem.tasks.prepare.importReuters21578Files.ImportReuters21578FilesTask",
    "org.hypknowsys.diasdem.tasks.prepare.importReutersCorpusVol1.ImportReutersCorpusVol1Task",
    "org.hypknowsys.diasdem.tasks.prepare.importHtmlFilesFromTheWeb.ImportHtmlFilesFromTheWebTask",
    "org.hypknowsys.diasdem.tasks.prepare.createTextUnits.CreateTextUnitsTask",
    "org.hypknowsys.diasdem.tasks.prepare.tokenizeTextUnits.TokenizeTextUnitsTask",
    "org.hypknowsys.diasdem.tasks.prepare.replaceNamedEntities21.ReplaceNamedEntitiesTask",
    "org.hypknowsys.diasdem.tasks.prepare.removeStopwords.RemoveStopwordsTask",
    "org.hypknowsys.diasdem.tasks.prepare.lemmatizeTextUnits.LemmatizeTextUnitsTask",
    "org.hypknowsys.diasdem.tasks.prepare.convertTextUnits.ConvertTextUnitsTask",
    "org.hypknowsys.diasdem.tasks.prepare.disambiguateWordSenses.DisambiguateWordSensesTask",
    "org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits22.VectorizeTextUnitsTask",
    "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys.ClusterTextUnitVectorsHypknowsysTask",
    "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka.ClusterTextUnitVectorsWekaTask",
    "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22.MonitorClusterQualityTask",
    "org.hypknowsys.diasdem.tasks.postprocess.tagTextUnits.TagTextUnitsTask",
    "org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22.DeriveConceptualDtdTask",
    "org.hypknowsys.diasdem.tasks.postprocess.tagDocuments22.TagDocumentsTask",
    "org.hypknowsys.diasdem.tasks.postprocess.drawDocumentSample22.DrawDocumentSampleTask",
    "org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts.ExportOracle8iSqlScriptsTask",
    "org.hypknowsys.diasdem.tasks.project.newProject.NewProjectTask",
    "org.hypknowsys.diasdem.tasks.project.openProject.OpenProjectTask",
    "org.hypknowsys.diasdem.tasks.project.closeProject.CloseProjectTask",
    "org.hypknowsys.diasdem.tasks.miscellaneous.rollbackProcessedTextUnits.RollbackProcessedTextUnitsTask",
    "org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits.ReplaceLabelsOfTextUnitsTask",
    "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeDocumentMetaData.TokenizeDocumentMetaDataTask",
    "org.hypknowsys.diasdem.tasks.miscellaneous.tokenizeParameterTextFile.TokenizeParameterTextFileTask",
    "org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile.ConvertParameterTextFileTask",
    "org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles.MergeParameterTextFilesTask",
    "org.hypknowsys.diasdem.tasks.miscellaneous.decomposeOrganizationNames.DecomposeOrganizationNamesTask",
    "org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles.SearchTheWebForHtmlFilesTask",
    "org.hypknowsys.diasdem.resources.templates.taskTemplate.TemplateTask",
    "org.hypknowsys.diasdem.resources.templates.blockingTaskTemplate.TemplateTask",
    "org.hypknowsys.diasdem.resources.templates.nonBlockingTaskTemplate.TemplateTask",
    "org.hypknowsys.diasdem.client.gui.solutions.wekaKnowledgeExplorer.WekaKnowledgeExplorerTask",
    "org.hypknowsys.diasdem.client.gui.tools.xmlDocumentViewer.XmlDocumentViewerTask",
    "org.hypknowsys.diasdem.client.gui.tools.htmlDocumentViewer.HtmlDocumentViewerTask",
    "org.hypknowsys.diasdem.client.gui.tools.regexTestEnvironment.RegexTestEnvironmentTask"
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

  public DIAsDEMgui() {

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
    return DiasdemProject; }
  public int getGuiStatus()  {     
    return GuiStatus; }   
  public JFrame getJFrame()  {   
    return (JFrame)this; }
  public KDesktopPane getKDesktopPane() { 
    return MyDesktopPane; }  
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setProject(Project pDiasdemProject) {    
    DiasdemProject = pDiasdemProject; this.registerPluginProperties(); }

  /* ########## ########## ########## ########## ########## ######### */

  public void setGuiMenuBar(GuiClientMenuBar pGuiMenuBar) { 
    
    if (pGuiMenuBar instanceof DIAsDEMguiMenuBar) {
      GuiMenuBar = (DIAsDEMguiMenuBar)pGuiMenuBar;
      this.setJMenuBar(GuiMenuBar.getJMenuBar());
    }
    else {
      System.err.println("[DIAsDEMgui] Error in setGuiMenuBar() ..."); 
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
    super.setContext(GuiPreferences.getProperty("DIASDEM_WORKBENCH_TITLE"), 
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
        .getProperty("DIASDEM_WORKBENCH_TITLE") + "!\n");
        System.out.flush();
        System.exit(0);
      }
      catch (PropertyVetoException e) {
        System.out.println("Warning: " + GuiPreferences
        .getProperty("DIASDEM_WORKBENCH_TITLE") + " cannot be closed!");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void initialize() {
  
    if (GuiPreferences == null) {
      GuiPreferences = new DIAsDEMguiPreferences();
    }
    GuiPreferences.setProperty("GUI_DEFAULT_DIRECTORY",
    Tools.extractDirectory(GuiPreferences.getFileName()));

    URL url = getClass().getResource(
    GuiPreferences.getWindowIconImageFileName());
    if (url != null) {
      ImageIcon GuiImageIcon = new ImageIcon(url);
      if (GuiImageIcon != null) {
        super.setIconImage(GuiImageIcon.getImage());
      }
    }
    
    DiasdemServer = new DIAsDEMserver();
    DiasdemServer.setTaskThreadPriority(GuiPreferences.getIntProperty(
    "TASK_THREAD_PRIORITY"));
    DiasdemProject = new DefaultDIAsDEMproject();
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
    DiasdemServer.terminateSession();
    super.finalize();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */  

  public void setVisible(boolean pVisible) {
  
    if (pVisible) {
      MyDesktopPane = new KDesktopPane((JFrame)this);
      this.setContentPane(MyDesktopPane);      

      GuiMenuBar = new DIAsDEMguiMenuBar(DiasdemServer, (GuiClient)this,
      GuiPreferences, GuiClient.GUI_STARTED_NO_PROJECT_OPENED, PlugInClassNames);
      
      this.setTitle(GuiPreferences.getProperty("DIASDEM_WORKBENCH_TITLE"));
      this.setGuiStatus(GuiClient.GUI_STARTED_NO_PROJECT_OPENED);
      this.validate();
      super.setVisible(true);
      
      LogPanel = new KLogPanel();
      KInternalFrame logFrame = new KInternalFrame("Log Window",
      true, false, true, true, 450, 225, LogPanel);
      MyDesktopPane.add(logFrame);
      
      if (GuiPreferences.getShowWelcomeWindow()) {
        DIAsDEMguiWelcome logoFrame = new DIAsDEMguiWelcome(this,
        GuiPreferences);
        MyDesktopPane.add(logoFrame);
      }
      
      LogPanel.logInfoMessage("Welcome to " + GuiPreferences
        .getProperty("DIASDEM_WORKBENCH_TITLE") + "!"); 
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
    
    if (pTaskControlPanel instanceof DiasdemControlPanel) {
      DiasdemInternalFrame frame = new DiasdemInternalFrame(
      true, true, true, true, DiasdemServer, DiasdemProject, this,
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
      UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, 
      new Boolean(true));
      UIManager.put(Options.DEFAULT_ICON_SIZE_KEY, 
      new Dimension(18, 18));
      Options.setGlobalFontSizeHints(
      FontSizeHints.MIXED);
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
        if (actionCommand.startsWith(DIAsDEMguiMenuBar.FILE_DIALOG)
        || actionCommand.startsWith(DIAsDEMguiMenuBar.HELP_WWW)) {
          DiasdemDialog dialog = new DiasdemDialog(this.getJFrame());
          dialog.setContext(this.getJFrame(), task.getLabel(), true,
          DiasdemServer, DiasdemProject, this, GuiPreferences, task
          .getTaskControlPanel(DiasdemServer, DiasdemProject, this,
          GuiPreferences));
          dialog.initialize();
          dialog.setVisible(true);
        }
        else if (actionCommand.startsWith(DIAsDEMguiMenuBar.ACTIONS)
        || actionCommand.startsWith(DIAsDEMguiMenuBar.SOLUTIONS)
        || actionCommand.startsWith(DIAsDEMguiMenuBar.TOOLS)
        || actionCommand.startsWith(DIAsDEMguiMenuBar.HELP)) {
          DiasdemInternalFrame frame = new DiasdemInternalFrame(
          true, true, true, true, DiasdemServer, DiasdemProject, this,
          GuiPreferences, task.getTaskControlPanel(DiasdemServer,
          DiasdemProject, this, GuiPreferences), this.getJFrame(),
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
      Task task = DiasdemServer.instantiateTask(PlugInClassNames[i],
      DiasdemServer);
      if (task != null && DiasdemProject != null) {
        DiasdemProject.registerButDontReplaceKProperties(task
        .getProjectPropertyData());
      }
      if (task != null && GuiPreferences != null) {
        GuiPreferences.registerButDontReplaceKProperties(task
        .getGuiClientPropertyData());
      }
    }
    if (DiasdemProject != null) {
      DiasdemProject.quickSave();
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
      DiasdemServer, DiasdemProject, this, GuiPreferences));
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
    "\nDIAsDEM Workbench requires its valid home directory ${DIAsDEM_HOME} as the\n"
    + "1st parameter. Example: java org.hypknowsys.diasdem.client.gui.DIAsDEMgui /opt/diasdem/";
    String warningString = 
    "\nDIAsDEM Workbench cannot locate a valid plug-in file "
    + DIAsDEMgui.PLUG_IN_FILE_NAME + "\nin its home directory. "
    + "The default plug-ins will be used instead.";
    File directory = null;
    String guiPreferencesFile = null;
    GuiClientPreferences guiPreferences = null;
    String plugInFile = null;
    String[] plugInClassNames = null;
    DIAsDEMgui diasdemGui = null;
    
    if (args != null && args.length > 0) {
      directory = new File(args[0].trim());
      if (directory.exists() && directory.isDirectory()) {
        if ( !args[0].endsWith(File.separator) ) {
          guiPreferencesFile = args[0].trim() + File.separator
          + DIAsDEMgui.GUI_PREFERENES_FILE_NAME;
          plugInFile = args[0].trim() + File.separator
          + DIAsDEMgui.PLUG_IN_FILE_NAME;
        }
        else {
          guiPreferencesFile = args[0].trim() 
          + DIAsDEMgui.GUI_PREFERENES_FILE_NAME;
          plugInFile = args[0].trim() 
          + DIAsDEMgui.PLUG_IN_FILE_NAME;
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
        if (plugInFile != null) {
          Tools.copyTextualSystemResource(
          "org/hypknowsys/diasdem/resources/txt/DIAsDEM.plugins", plugInFile);
        }
      }
    }

    if (guiPreferencesFile != null) {
      try {
        TextFile plugInTextFile = new TextFile(new File(plugInFile));
        if (Tools.isExistingFile(guiPreferencesFile)) {
          guiPreferences = new DIAsDEMguiPreferences(guiPreferencesFile, 
          KProperties.LOAD);
        }
        else {
          guiPreferences = new DIAsDEMguiPreferences(guiPreferencesFile, 
          KProperties.CREATE);
        }
        diasdemGui = new DIAsDEMgui();
        if (plugInClassNames == null) {
          diasdemGui.setContext(guiPreferences, 
          DIAsDEMgui.PLUG_IN_CLASS_NAMES);
        }
        else {
          diasdemGui.setContext(guiPreferences, plugInClassNames);
        }
        diasdemGui.setLookAndFeel(guiPreferences.getLookAndFeelClassName(), 
        false);
        diasdemGui.setOpened(true);
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