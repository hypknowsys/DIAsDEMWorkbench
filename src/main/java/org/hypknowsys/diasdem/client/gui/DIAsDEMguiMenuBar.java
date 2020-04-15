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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientMenuBar;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.ScriptableTask;
import org.hypknowsys.diasdem.client.gui.file.editTextFile.EditTextFileTask;
import org.hypknowsys.diasdem.client.gui.file
.editProjectFile.EditProjectFileTask;
import org.hypknowsys.diasdem.client.gui.file
.editParameterFile.EditParameterFileTask;
import org.hypknowsys.diasdem.client.gui.file.viewHtmlFile.ViewHtmlFileTask;
import org.hypknowsys.diasdem.client.gui.file.viewXmlFile.ViewXmlFileTask;
import org.hypknowsys.diasdem.client.gui.help
.aboutDiasdemWorkbench.AboutDiasdemWorkbenchTask;
import org.hypknowsys.diasdem.client.gui.tools
.clusterLabelEditor.ClusterLabelEditorTask;
import org.hypknowsys.diasdem.client.gui.tools.options.OptionsTask;
import org.hypknowsys.diasdem.client.gui.tools
.systemProperties.SystemPropertiesTask;
import org.hypknowsys.diasdem.client.gui.tools
.taggingQualityEvaluator22.TaggingQualityEvaluatorTask;
import org.hypknowsys.diasdem.client.gui.tools
.termFrequencyStatisticsViewer.TermFrequencyStatisticsViewerTask;
import org.hypknowsys.diasdem.client.gui.tools
.thesaurusEditor22.ThesaurusEditorTask;
import org.hypknowsys.diasdem.client.gui.help
.diasdemProjectWebSite.DiasdemProjectWebSiteTask;
import org.hypknowsys.diasdem.tasks.project.closeProject.CloseProjectTask;
import org.hypknowsys.diasdem.tasks.project.newProject.NewProjectTask;
import org.hypknowsys.diasdem.tasks.project.openProject.OpenProjectTask;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class DIAsDEMguiMenuBar implements GuiClientMenuBar, ActionListener {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private JMenuBar menuBarDiasdem = null;
	
  private JMenu mFile = null;
  private   KMenuItem miNewProject = null;
  private   KMenuItem miOpenProject = null;
  private   KMenuItem miCloseProject = null;
  private   JMenuItem miEditTextFile = null;
  private   JMenuItem miEditProjectFile = null;
  private   JMenuItem miEditParameterFile = null;
  private   JMenuItem miViewHtmlFile = null;
  private   JMenuItem miViewXmlFile = null;
  private   KMenuItem miExit = null;
  private JMenu mActions = null;
  // The Actions menu corresponds to the KDD process as proposed by
  // Heikki Mannila: Methods and problems in data mining. In:
  // Proceedings of the International Conference on Database Theory, 
  // Delphi, Greece, January 1997. Springer-Verlag, Berlin, Heidelberg.
  private JMenu mActionsUnderstandDomain = null;
  private JMenu mActionsPrepareDataSet = null;
  private JMenu mActionsDiscoverPatterns = null;
  private JMenu mActionsPostprocessPatterns = null;
  private JMenu mActionsPutResultsIntoUse = null;
  private JMenu mActionsProjectManagement = null;
  private JMenu mActionsMiscellaneous = null;
  private JMenu mSolutions = null;
  private JMenu mBatchScriptProcessing = null;
  private   KMenuItem miRecordBatchScript = null;
  private   KMenuItem miEditBatchScript = null;
  private   KMenuItem miExecuteBatchScript = null;
  private JMenu mSolutionsMiscellaneous = null;
  private JMenu mTools = null;
  private   JMenuItem miCollectionStatisticsViewer = null;
  private   JMenuItem miThesaurusEditor22 = null;
  private   JMenuItem miClusterLabelEditor = null;
  private   JMenuItem miTaggingQualityEvaluator22 = null;
  private   JMenuItem miSystemProperties = null;
  private   JMenuItem miOptions = null;
  private JMenu mToolsMiscellaneous = null;
  private JMenu mHelp = null;
  private   JMenuItem miDiasdemProjectWebSite = null;
  private   JMenuItem miAboutDiasdem = null;

  private Server DiasdemServer = null;
  private GuiClient DiasdemGui = null;
  private GuiClientPreferences DiasdemGuiPreferences = null;
  private int GuiStatus = 0;
  private String[] PlugInClassNames = null;
  
  private ArrayList AllMenuItems = new ArrayList(); 
  private Iterator MenuItemIterator = null;
  
  private DefaultMutableTreeNode RootOfScriptableTasksJTree = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public final static String FILE_DIALOG = 
  "File:Dialog";
  public final static String ACTIONS = 
  "Actions";
  public final static String ACTIONS_UNDERSTAND_DOMAIN = 
  "Actions:UnderstandDomain";
  public final static String ACTIONS_PREPARE_DATA_SET = 
  "Actions:PrepareDataSet";
  public final static String ACTIONS_DISCOVER_PATTERNS = 
  "Actions:DiscoverPatterns";
  public final static String ACTIONS_POSTPROCESS_PATTERNS = 
  "Actions:PostprocessPatterns";
  public final static String ACTIONS_PUT_RESULTS_INTO_USE = 
  "Actions:PutResultsIntoUse";
  public final static String ACTIONS_PROJECT_MANAGEMENT = 
  "Actions:ProjectManagement";
  public final static String ACTIONS_MISCELLANEOUS = 
  "Actions:Miscellaneous";
  public final static String SOLUTIONS = 
  "Solutions";
  public final static String SOLUTIONS_MISCELLANEOUS = 
  "Solutions:Miscellaneous";
  public final static String TOOLS = 
  "Tools";
  public final static String TOOLS_MISCELLANEOUS = 
  "Tools:Miscellaneous";
  public final static String HELP_WWW = 
  "Help:Www";
  public final static String HELP = 
  "Help";

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public DIAsDEMguiMenuBar() {}
  
  /* ########## ########## ########## ########## ########## ######### */

  public DIAsDEMguiMenuBar(Server pDiasdemServer, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences, int pGuiStatus,
  String[] pPlugInClassNames) {
    
    this.setContext(pDiasdemServer, pDiasdemGui, pDiasdemGuiPreferences,
    pGuiStatus, pPlugInClassNames);
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
  
    String actionCommand = pActionEvent.getActionCommand();
    Object actionSource = pActionEvent.getSource();
          
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface GuiClientMenuBar methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContext(Server pDiasdemServer, GuiClient pDiasdemGui, 
  GuiClientPreferences pDiasdemGuiPreferences, int pGuiStatus, 
  String[] pPlugInClassNames) {
    
    DiasdemServer = pDiasdemServer;
    DiasdemGui = pDiasdemGui;
    DiasdemGuiPreferences = pDiasdemGuiPreferences;
    GuiStatus = pGuiStatus; 
    PlugInClassNames = pPlugInClassNames;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void initialize() {
        
    menuBarDiasdem = new JMenuBar();
    DiasdemGui.setGuiMenuBar(this);

    mFile = new JMenu("File"); mFile.setMnemonic(KeyEvent.VK_F);
    
    miNewProject = (new NewProjectTask()).getKMenuItem();
    miNewProject.addActionListener(DiasdemGui);
    miNewProject.setActionCommand(FILE_DIALOG + ":NewProject");
    mFile.add(miNewProject); AllMenuItems.add(miNewProject);

    miOpenProject = (new OpenProjectTask()).getKMenuItem();
    miOpenProject.addActionListener(DiasdemGui);
    miOpenProject.setActionCommand(FILE_DIALOG + ":OpenProject");
    mFile.add(miOpenProject); AllMenuItems.add(miOpenProject);

    miCloseProject = (new CloseProjectTask()).getKMenuItem();
    miCloseProject.addActionListener(DiasdemGui);
    miCloseProject.setActionCommand(FILE_DIALOG + ":CloseProject");
    mFile.add(miCloseProject); AllMenuItems.add(miCloseProject);

    mFile.addSeparator();
    miEditTextFile = (new EditTextFileTask()).getKMenuItem();
    miEditTextFile.addActionListener(DiasdemGui);
    mFile.add(miEditTextFile); AllMenuItems.add(miEditTextFile);
    
    miEditProjectFile = (new EditProjectFileTask()).getKMenuItem();
    miEditProjectFile.addActionListener(DiasdemGui);
    mFile.add(miEditProjectFile); AllMenuItems.add(miEditProjectFile);
    
    miEditParameterFile = (new EditParameterFileTask()).getKMenuItem();
    miEditParameterFile.addActionListener(DiasdemGui);
    mFile.add(miEditParameterFile); AllMenuItems.add(miEditParameterFile);
    
    miViewHtmlFile = (new ViewHtmlFileTask()).getKMenuItem();
    miViewHtmlFile.addActionListener(DiasdemGui);
    mFile.add(miViewHtmlFile); AllMenuItems.add(miViewHtmlFile);
    
    miViewXmlFile = (new ViewXmlFileTask()).getKMenuItem();
    miViewXmlFile.addActionListener(DiasdemGui);
    mFile.add(miViewXmlFile); AllMenuItems.add(miViewXmlFile);
    
    mFile.addSeparator();
    miExit = new KMenuItem("Exit", KeyEvent.VK_X, 
    "File:Exit", DiasdemGui, KeyEvent.VK_X, ActionEvent.CTRL_MASK, 
    GuiClient.MIN_GUI_STATUS, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING, 
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED, null);
    mFile.add(miExit); AllMenuItems.add(miExit);
    
    menuBarDiasdem.add(mFile);
                
    mActions = new JMenu("Actions"); mActions.setMnemonic(KeyEvent.VK_A);
    mActionsUnderstandDomain = new JMenu("Understand Domain"); 
    mActionsUnderstandDomain.setMnemonic(KeyEvent.VK_U);
    mActionsPrepareDataSet = new JMenu("Prepare Data Set"); 
    mActionsPrepareDataSet.setMnemonic(KeyEvent.VK_S);
    mActionsDiscoverPatterns = new JMenu("Discover Patterns"); 
    mActionsDiscoverPatterns.setMnemonic(KeyEvent.VK_D);
    mActionsPostprocessPatterns = new JMenu("Postprocess Patterns"); 
    mActionsPostprocessPatterns.setMnemonic(KeyEvent.VK_P);
    mActionsPutResultsIntoUse = new JMenu("Put Results Into Use"); 
    mActionsPutResultsIntoUse.setMnemonic(KeyEvent.VK_R);
    mActionsProjectManagement = new JMenu("Project Management"); 
    mActionsProjectManagement.setMnemonic(KeyEvent.VK_R);
    mActionsMiscellaneous = new JMenu("Miscellaneous"); 
    mActionsMiscellaneous.setMnemonic(KeyEvent.VK_M);
    mActions.add(mActionsUnderstandDomain);
    mActions.add(mActionsPrepareDataSet);
    mActions.add(mActionsDiscoverPatterns);
    mActions.add(mActionsPostprocessPatterns);
    mActions.add(mActionsPutResultsIntoUse);
    mActions.addSeparator();
    mActions.add(mActionsProjectManagement);
    mActions.add(mActionsMiscellaneous);
    menuBarDiasdem.add(mActions);
    
    mSolutions = new JMenu("Solutions"); mSolutions.setMnemonic(KeyEvent.VK_S);
    mBatchScriptProcessing = new JMenu("Batch Script Processing"); 
    mBatchScriptProcessing.setMnemonic(KeyEvent.VK_B);

    miRecordBatchScript = new KMenuItem("Record Batch Script", KeyEvent.VK_R, 
    SOLUTIONS + ":BatchScriptProcessing:RecordBatchScript", DiasdemGui, 0, 0, 
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED, 
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED, 
    "org.hypknowsys.diasdem.client.gui.solutions.batch.recordBatchScript"
    + ".RecordBatchScriptTask");
    mBatchScriptProcessing.add(miRecordBatchScript); 
    AllMenuItems.add(miRecordBatchScript);  
    
    miEditBatchScript = new KMenuItem("Edit Batch Script", KeyEvent.VK_S, 
    SOLUTIONS + ":BatchScriptProcessing:EditBatchScript", DiasdemGui, 0, 0, 
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED, 
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING, GuiClient.MAX_GUI_STATUS, 
    "org.hypknowsys.diasdem.client.gui.solutions.batch.editBatchScript"
    + ".EditBatchScriptTask");
    mBatchScriptProcessing.add(miEditBatchScript); 
    AllMenuItems.add(miEditBatchScript);
    
    miExecuteBatchScript = new KMenuItem("Execute Batch Script", KeyEvent.VK_E, 
    SOLUTIONS + ":BatchScriptProcessing:ExecuteBatchScript", DiasdemGui, 0, 0, 
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED, 
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED, 
    "org.hypknowsys.diasdem.client.gui.solutions.batch.executeBatchScript"
    + ".ExecuteBatchScriptTask");
    mBatchScriptProcessing.add(miExecuteBatchScript); 
    AllMenuItems.add(miExecuteBatchScript);    
    
    mSolutions.add(mBatchScriptProcessing);
    mSolutions.addSeparator();
    mSolutionsMiscellaneous = new JMenu("Miscellaneous"); 
    mSolutionsMiscellaneous.setMnemonic(KeyEvent.VK_M);
    mSolutions.add(mSolutionsMiscellaneous);
    menuBarDiasdem.add(mSolutions);

    mTools = new JMenu("Tools");  mTools.setMnemonic(KeyEvent.VK_T);
    miCollectionStatisticsViewer = (new TermFrequencyStatisticsViewerTask())
    .getKMenuItem();
    miCollectionStatisticsViewer.addActionListener(DiasdemGui);
    mTools.add(miCollectionStatisticsViewer); 
    AllMenuItems.add(miCollectionStatisticsViewer);    
    
    miThesaurusEditor22 = (new ThesaurusEditorTask()).getKMenuItem();
    miThesaurusEditor22.addActionListener(DiasdemGui);
    mTools.add(miThesaurusEditor22); AllMenuItems.add(miThesaurusEditor22);    

    miClusterLabelEditor = (new ClusterLabelEditorTask()).getKMenuItem();
    miClusterLabelEditor.addActionListener(DiasdemGui);
    mTools.add(miClusterLabelEditor); 
    AllMenuItems.add(miClusterLabelEditor);    
    
    miTaggingQualityEvaluator22 = (new TaggingQualityEvaluatorTask())
    .getKMenuItem();
    miTaggingQualityEvaluator22.addActionListener(DiasdemGui);
    mTools.add(miTaggingQualityEvaluator22); 
    AllMenuItems.add(miTaggingQualityEvaluator22);    
    
    mTools.addSeparator();
    mToolsMiscellaneous = new JMenu("Miscellaneous"); 
    mToolsMiscellaneous.setMnemonic(KeyEvent.VK_M);
    mTools.add(mToolsMiscellaneous);
    
    mTools.addSeparator();    
    miSystemProperties = (new SystemPropertiesTask()).getKMenuItem();
    miSystemProperties.addActionListener(DiasdemGui);
    mTools.add(miSystemProperties); AllMenuItems.add(miSystemProperties);    
    
    miOptions = (new OptionsTask()).getKMenuItem();
    miOptions.addActionListener(DiasdemGui);
    mTools.add(miOptions); AllMenuItems.add(miOptions);     
    menuBarDiasdem.add(mTools);

    menuBarDiasdem.add(DiasdemGui.getKDesktopPane().getWindowMenu());   

    mHelp = new JMenu("Help");  mHelp.setMnemonic(KeyEvent.VK_H);
    miDiasdemProjectWebSite = (new DiasdemProjectWebSiteTask()).getKMenuItem();
    miDiasdemProjectWebSite.addActionListener(DiasdemGui);
    mHelp.add(miDiasdemProjectWebSite); 
    AllMenuItems.add(miDiasdemProjectWebSite);
    
    mHelp.addSeparator();
    miAboutDiasdem = (new AboutDiasdemWorkbenchTask()).getKMenuItem();
    miAboutDiasdem.setText("About " + DiasdemGuiPreferences
    .getProperty("DIASDEM_WORKBENCH_TITLE"));
    miAboutDiasdem.addActionListener(DiasdemGui);
    mHelp.add(miAboutDiasdem); AllMenuItems.add(miAboutDiasdem);
    menuBarDiasdem.add(mHelp);

    this.registerPlugIns(PlugInClassNames);
    
    this.setGuiStatus(GuiClient.GUI_STARTED_NO_PROJECT_OPENED);    
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void finalize() throws PropertyVetoException {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getRecordBatchScriptKMenuItem() {
    return miRecordBatchScript; }

  /* ########## ########## ########## ########## ########## ######### */
  
  public JMenuBar getJMenuBar() {
    return menuBarDiasdem; }

  /* ########## ########## ########## ########## ########## ######### */

  public void setGuiStatus(int pGuiStatus)  {   

    GuiStatus = pGuiStatus;
    MenuItemIterator = AllMenuItems.iterator();
    while (MenuItemIterator.hasNext()) {
      ( (KMenuItem)MenuItemIterator.next() ).setEnabled(GuiStatus);
    }
    this.miClusterLabelEditor.setEnabled(true);
    this.miExit.setEnabled(true);
    
    menuBarDiasdem.validate();

  } 
 
  /* ########## ########## ########## ########## ########## ######### */

  public DefaultMutableTreeNode getRootOfScriptableTasksJTree() {
      
    if (RootOfScriptableTasksJTree != null) {
      return RootOfScriptableTasksJTree;
    }
    
    RootOfScriptableTasksJTree = new DefaultMutableTreeNode(
    "All Scriptable Tasks");
    
    if (mActionsUnderstandDomain != null 
    && mActionsUnderstandDomain.getItemCount() > 0) {
      DefaultMutableTreeNode menu = new DefaultMutableTreeNode(
      mActionsUnderstandDomain.getLabel());
      this.addScriptableTasksToJTreeNode(mActionsUnderstandDomain, menu);
      RootOfScriptableTasksJTree.add(menu);
    }
    
    if (mActionsPrepareDataSet != null 
    && mActionsPrepareDataSet.getItemCount() > 0) {
      DefaultMutableTreeNode menu = new DefaultMutableTreeNode(
      mActionsPrepareDataSet.getLabel());
      this.addScriptableTasksToJTreeNode(mActionsPrepareDataSet, menu);
      RootOfScriptableTasksJTree.add(menu);
    }
    
    if (mActionsDiscoverPatterns != null 
    && mActionsDiscoverPatterns.getItemCount() > 0) {
      DefaultMutableTreeNode menu = new DefaultMutableTreeNode(
      mActionsDiscoverPatterns.getLabel());
      this.addScriptableTasksToJTreeNode(mActionsDiscoverPatterns, menu);
      RootOfScriptableTasksJTree.add(menu);
    }
    
    if (mActionsPostprocessPatterns != null 
    && mActionsPostprocessPatterns.getItemCount() > 0) {
      DefaultMutableTreeNode menu = new DefaultMutableTreeNode(
      mActionsPostprocessPatterns.getLabel());
      this.addScriptableTasksToJTreeNode(mActionsPostprocessPatterns, menu);
      RootOfScriptableTasksJTree.add(menu);
    }
    
    if (mActionsPutResultsIntoUse != null 
    && mActionsPutResultsIntoUse.getItemCount() > 0) {
      DefaultMutableTreeNode menu = new DefaultMutableTreeNode(
      mActionsPutResultsIntoUse.getLabel());
      this.addScriptableTasksToJTreeNode(mActionsPutResultsIntoUse, menu);
      RootOfScriptableTasksJTree.add(menu);
    }
    
    if (mActionsProjectManagement != null 
    && mActionsProjectManagement.getItemCount() > 0) {
      DefaultMutableTreeNode menu = new DefaultMutableTreeNode(
      mActionsProjectManagement.getLabel());
      this.addScriptableTasksToJTreeNode(mActionsProjectManagement, menu);
      RootOfScriptableTasksJTree.add(menu);
    }
    
    if (mActionsMiscellaneous != null 
    && mActionsMiscellaneous.getItemCount() > 0) {
      DefaultMutableTreeNode menu = new DefaultMutableTreeNode(
      mActionsMiscellaneous.getLabel());
      this.addScriptableTasksToJTreeNode(mActionsMiscellaneous, menu);
      RootOfScriptableTasksJTree.add(menu);
    }
    
    return RootOfScriptableTasksJTree;
    
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
  
  private void registerPlugIns(String[] pPlugInClassNames) {
    
    if (pPlugInClassNames == null) {
      return;
    }
    
    for (int i = 0; i < pPlugInClassNames.length; i++) {
      Task task = DiasdemServer.instantiateTask(pPlugInClassNames[i],
      DiasdemServer);
      if (task != null) {
        KMenuItem menuItem = task.getKMenuItem();
        if (menuItem != null) {
          menuItem.addActionListener(DiasdemGui); 
          (this.getMenuForPlugInKMenuItem(menuItem)).add(menuItem);
          AllMenuItems.add(menuItem);
        }
      }
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private JMenu getMenuForPlugInKMenuItem(KMenuItem pMenuItem) {
    
    String actionCommand = pMenuItem.getActionCommand();
    if (actionCommand == null || actionCommand.length() == 0) {
      System.err.println("[DIAsDEMguiMenuBar] Warning: Plug-in "
      + pMenuItem.toString() + " has no action command!");
      System.err.flush();
      return mActionsMiscellaneous;
    }
    else if (actionCommand.startsWith(ACTIONS_UNDERSTAND_DOMAIN)) {     
      return mActionsUnderstandDomain;
    }
    else if (actionCommand.startsWith(ACTIONS_PREPARE_DATA_SET)) {     
      return mActionsPrepareDataSet;
    }
    else if (actionCommand.startsWith(ACTIONS_DISCOVER_PATTERNS)) {     
      return mActionsDiscoverPatterns;
    }
    else if (actionCommand.startsWith(ACTIONS_POSTPROCESS_PATTERNS)) {     
      return mActionsPostprocessPatterns;
    }
    else if (actionCommand.startsWith(ACTIONS_PUT_RESULTS_INTO_USE)) {     
      return mActionsPutResultsIntoUse;
    }
    else if (actionCommand.startsWith(ACTIONS_PROJECT_MANAGEMENT)) {     
      return mActionsProjectManagement;
    }
    else if (actionCommand.startsWith(SOLUTIONS_MISCELLANEOUS)) {     
      return mSolutionsMiscellaneous;
    }
    else if (actionCommand.startsWith(SOLUTIONS)) {     
      return mSolutions;
    }
    else if (actionCommand.startsWith(TOOLS_MISCELLANEOUS)) {     
      return mToolsMiscellaneous;
    }
    
    return mActionsMiscellaneous; 
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  private void addScriptableTasksToJTreeNode(JMenu pMenu, 
  DefaultMutableTreeNode pNode) {
    
    Task task = null;
    JMenuItem item = null;
    for (int i = 0; i < pMenu.getItemCount(); i++) {
      if (pMenu.getItem(i) != null && pMenu.getItem(i) instanceof KMenuItem
      && ((KMenuItem)pMenu.getItem(i)).getTaskClassName() != null) {
        task = DiasdemServer.instantiateTask(((KMenuItem)pMenu.getItem(i))
        .getTaskClassName(), DiasdemServer);
        if (task != null && task instanceof ScriptableTask) {
          pNode.add(new DefaultMutableTreeNode((KMenuItem)pMenu.getItem(i)));
        }
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