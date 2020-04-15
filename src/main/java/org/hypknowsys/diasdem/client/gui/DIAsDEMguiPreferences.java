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

import java.util.*;
import java.io.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.core.*;

/**
  * @version 2.1, 15 August 2003
  * @author Karsten Winkler
  */

public class DIAsDEMguiPreferences extends KProperties
implements GuiClientPreferences {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public final static KProperty[] MY_PROPERTY_DATA = {
    new KProperty("EXTERNAL_EDITOR",
    "Absolute Path of External Editor",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("FRAME_SIZE_X",
    "Horizontal Size of Main Frame",
    "800", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("FRAME_SIZE_Y",
    "Vertical Size of Main Frame",
    "600", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("FRAME_POSITION_X",
    "Horizontal Position of Main Frame",
    "50", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("FRAME_POSITION_Y",
    "Vertical Position of Main Frame",
    "50", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DIALOG_XXS_SIZE_X",
    "Horizontal Size of Ultra Small Dialogs",
    "300", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XXS_SIZE_Y",
    "Vertical Size of Ultra Small Dialogs",
    "75", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XS_SIZE_X",
    "Horizontal Size of Extra Small Dialogs",
    "400", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XS_SIZE_Y",
    "Vertical Size of Extra Small Dialogs",
    "150", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_S_SIZE_X",
    "Horizontal Size of Small Dialogs",
    "500", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_S_SIZE_Y",
    "Vertical Size of Small Dialogs",
    "200", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_M_SIZE_X",
    "Horizontal Size of Medium Dialogs",
    "600", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_M_SIZE_Y",
    "Vertical Size of Medium Dialogs",
    "300", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_L_SIZE_X",
    "Horizontal Size of Large Dialogs",
    "700", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_L_SIZE_Y",
    "Vertical Size of Large Dialogs",
    "500", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XL_SIZE_X",
    "Horizontal Size of Extra Large Dialogs",
    "800", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XL_SIZE_Y",
    "Vertical Size of Extra Large Dialogs",
    "600", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("GUI_PREFERENCES_DIRECTORY",
    "Directory of GUI Preferences File",
    ".", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("GUI_PREFERENCES_FILE",
    "GUI Preferences Filename",
    "DIAsDEMgui.config", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("EXTERNAL_BROWSER",
    "Absolute Path of External Browser",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("TASK_THREAD_PRIORITY",
    "Task Thread Priority [min=1; norm=5; max=10]",
    "5", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("GUI_DEFAULT_DIRECTORY",
    "Default Directory of GUI File Chooser",
    ".", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("EXTERNAL_XML_VIEWER",
    "Absolute Path of External XML Viewer",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MRU_PROJECT_FILE_NAME",
    "Most Recently Used DIAsDEM Project File Name",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MRU_PROJECT_DIRECTORY",
    "Most Recently Used Project Directory",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MRU_PARAMETER_DIRECTORY",
    "Most Recently Used Parameter Directory",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("SHOW_WARNINGS",
    "Show Warnings (e.g., 'Replace File?')",
    "true", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("INTRO_IMAGE_FILE_NAME",
    "File Name of Splash Screen and Welcome Frame",
    "/org/hypknowsys/diasdem/resources/img/DiasdemIntro.jpg", 
    KProperty.STRING, KProperty.EDITABLE),
    new KProperty("WINDOW_ICON_IMAGE_FILE_NAME",
    "File Name of the Window Icon",
    "/org/hypknowsys/diasdem/resources/img/DiasdemIcon16.gif", 
    KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DIASDEM_VERSION",
    "Version of this DIAsDEM Workbench Release",
    DIAsDEMconstants.DIASDEM_VERSION, KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("JAVA_VERSION",
    "Required Java (TM) Version for this DIAsDEM Workbench Release",
    DIAsDEMconstants.JAVA_VERSION, KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("RELEASE_DATE",
    "Release Date of this DIAsDEM Workbench",
    DIAsDEMconstants.RELEASE_DATE, KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("WRAP_LINES",
    "Wrap Lines in Text Areas and Text Panes",
    "false", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("SHOW_WELCOME_WINDOW",
    "Show Welcome Window",
    "true", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("MRU_BATCH_SCRIPT_FILE_NAME",
    "Most Recently Used DIAsDEM Batch Script File Name",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_TREE_TAGGER_COMMAND", 
    "Default Absolute Path of TreeTagger Command", 
    "tree-tagger-german", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DIASDEM_WORKBENCH_TITLE", 
    "Title of DIAsDEM Workbench Window", 
    "DIAsDEM Workbench 2.2", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("LOOK_AND_FEEL_CLASS_NAME", 
    "Fully Qualified Class Name of Java Look and Feel", 
    "com.jgoodies.plaf.plastic.PlasticLookAndFeel",
    KProperty.STRING, KProperty.NOT_EDITABLE)
  };

  public final static String COLLECTION_FILE_EXTENSION = ".dcf";
  public final static KFileFilter COLLECTION_FILE_FILTER = 
    new KFileFilter(COLLECTION_FILE_EXTENSION, 
    "DIAsDEM Collection Files (*" + COLLECTION_FILE_EXTENSION + ")");

  public final static String TF_STATISTICS_FILE_EXTENSION = ".dtf";
  public final static KFileFilter TF_STATISTICS_FILE_FILTER = 
    new KFileFilter(TF_STATISTICS_FILE_EXTENSION, 
    "DIAsDEM TF Statistics Files (*" + TF_STATISTICS_FILE_EXTENSION + ")");

  public final static String THESAURUS_FILE_EXTENSION = ".dth";
  public final static KFileFilter THESAURUS_FILE_FILTER = 
    new KFileFilter(THESAURUS_FILE_EXTENSION, 
    "DIAsDEM Thesaurus Files (*" + THESAURUS_FILE_EXTENSION + ")");

  public final static String CLUSTER_LABEL_FILE_EXTENSION = ".dcl";
  public final static KFileFilter CLUSTER_LABEL_FILE_FILTER = 
    new KFileFilter(CLUSTER_LABEL_FILE_EXTENSION, 
    "DIAsDEM Cluster Label Files (*" + CLUSTER_LABEL_FILE_EXTENSION + ")");

  public final static String PRELIMINARY_DTD_FILE_EXTENSION = ".dpd";
  public final static KFileFilter PRELIMINARY_DTD_FILE_FILTER = 
    new KFileFilter(PRELIMINARY_DTD_FILE_EXTENSION, 
    "DIAsDEM Preliminary DTD Files (*" + PRELIMINARY_DTD_FILE_EXTENSION + ")");

  public final static String CONCEPTUAL_DTD_FILE_EXTENSION = ".dcd";
  public final static KFileFilter CONCEPTUAL_DTD_FILE_FILTER = 
    new KFileFilter(CONCEPTUAL_DTD_FILE_EXTENSION, 
    "DIAsDEM Conceptual DTD Files (*" + CONCEPTUAL_DTD_FILE_EXTENSION + ")");

  public final static String TEXT_UNIT_SAMPLE_FILE_EXTENSION = ".dts";
  public final static KFileFilter TEXT_UNIT_SAMPLE_FILE_FILTER = 
    new KFileFilter(TEXT_UNIT_SAMPLE_FILE_EXTENSION, 
    "DIAsDEM Text Unit Sample Files (*" + TEXT_UNIT_SAMPLE_FILE_EXTENSION + ")");

  public final static String DOCUMENT_SAMPLE_FILE_EXTENSION = ".dds";
  public final static KFileFilter DOCUMENT_SAMPLE_FILE_FILTER = 
    new KFileFilter(DOCUMENT_SAMPLE_FILE_EXTENSION, 
    "DIAsDEM Document Sample Files (*" + DOCUMENT_SAMPLE_FILE_EXTENSION + ")");

  public final static String EVALUATED_TEXT_UNIT_SAMPLE_FILE_EXTENSION = ".det";
  public final static KFileFilter EVALUATED_TEXT_UNIT_SAMPLE_FILE_FILTER = 
    new KFileFilter(EVALUATED_TEXT_UNIT_SAMPLE_FILE_EXTENSION, 
    "DIAsDEM Evaluated Text Units Files (*" + EVALUATED_TEXT_UNIT_SAMPLE_FILE_EXTENSION + ")");

  public final static String ARFF_FILE_EXTENSION = ".arff";
  public final static KFileFilter ARFF_FILE_FILTER = 
    new KFileFilter(ARFF_FILE_EXTENSION, 
    "Weka Data Mining Files (*" + ARFF_FILE_EXTENSION + ")");

  public final static String WEKA_SIMPLE_KMEANS_FILE_EXTENSION = ".wskm";
  public final static KFileFilter WEKA_SIMPLE_KMEANS_FILE_FILTER = 
    new KFileFilter(WEKA_SIMPLE_KMEANS_FILE_EXTENSION, 
    "Weka Simple K-Means Clusterers (*" + WEKA_SIMPLE_KMEANS_FILE_EXTENSION + ")");

  public final static String WEKA_COBWEB_FILE_EXTENSION = ".wcw";
  public final static KFileFilter WEKA_COBWEB_FILE_FILTER = 
    new KFileFilter(WEKA_COBWEB_FILE_EXTENSION, 
    "Weka Cobweb Clusterers (*" + WEKA_COBWEB_FILE_EXTENSION + ")");

  public final static String WEKA_EM_FILE_EXTENSION = ".wem";
  public final static KFileFilter WEKA_EM_FILE_FILTER = 
    new KFileFilter(WEKA_EM_FILE_EXTENSION, 
    "Weka EM Clusterers (*" + WEKA_EM_FILE_EXTENSION + ")");

  public final static String HYPKNOWSYS_SIMPLE_KMEANS_FILE_EXTENSION = ".hskm";
  public final static KFileFilter HYPKNOWSYS_SIMPLE_KMEANS_FILE_FILTER = 
    new KFileFilter(HYPKNOWSYS_SIMPLE_KMEANS_FILE_EXTENSION, 
    "hypKNOWsys Simple K-Means Clusterers (*" + HYPKNOWSYS_SIMPLE_KMEANS_FILE_EXTENSION + ")");

  public final static String HYPKNOWSYS_BISECTING_KMEANS_FILE_EXTENSION = ".hbkm";
  public final static KFileFilter HYPKNOWSYS_BISECTING_KMEANS_FILE_FILTER = 
    new KFileFilter(HYPKNOWSYS_BISECTING_KMEANS_FILE_EXTENSION, 
    "hypKNOWsys Bisecting K-Means Clusterers (*" + HYPKNOWSYS_BISECTING_KMEANS_FILE_EXTENSION + ")");

  public final static String HYPKNOWSYS_BATCH_SOM_FILE_EXTENSION = ".hbsom";
  public final static KFileFilter HYPKNOWSYS_BATCH_SOM_FILE_FILTER = 
    new KFileFilter(HYPKNOWSYS_BATCH_SOM_FILE_EXTENSION, 
    "hypKNOWsys Batch SOM Clusterers (*" + HYPKNOWSYS_BATCH_SOM_FILE_EXTENSION + ")");

  public final static String HYPKNOWSYS_JARVIS_PATRICK_SNN_FILE_EXTENSION = ".hjps";
  public final static KFileFilter HYPKNOWSYS_JARVIS_PATRICK_SNN_FILE_FILTER = 
    new KFileFilter(HYPKNOWSYS_JARVIS_PATRICK_SNN_FILE_EXTENSION, 
    "hypKNOWsys Jarvis-Patrick SNN Clusterers (*" + HYPKNOWSYS_JARVIS_PATRICK_SNN_FILE_EXTENSION + ")");

  public final static String HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN_FILE_EXTENSION = ".heskt";
  public final static KFileFilter HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN_FILE_FILTER = 
    new KFileFilter(HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN_FILE_EXTENSION, 
    "hypKNOWsys Ertoz-Steinbach-Kumar Topics SNN Clusterers (*" + HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN_FILE_EXTENSION + ")");

  public final static String SAS_FILE_EXTENSION = ".sas";
  public final static KFileFilter SAS_FILE_FILTER = 
    new KFileFilter(SAS_FILE_EXTENSION, 
    "SAS Files (*" + SAS_FILE_EXTENSION + ")");

  public final static String LOG_FILE_EXTENSION = ".log";
  public final static KFileFilter LOG_FILE_FILTER = 
    new KFileFilter(LOG_FILE_EXTENSION, 
    "Log Files (*" + LOG_FILE_EXTENSION + ")");

  public final static String DESCRIPTOR_WEIGHTS_FILE_EXTENSION = ".ddw";
  public final static KFileFilter DESCRIPTOR_WEIGHTS_FILE_FILTER = 
    new KFileFilter(DESCRIPTOR_WEIGHTS_FILE_EXTENSION, 
    "DIAsDEM Descriptor Weights Files (*" + DESCRIPTOR_WEIGHTS_FILE_EXTENSION + ")");

  public final static String COLLECTION_FREQUENCIES_FILE_EXTENSION = ".dcfq";
  public final static KFileFilter COLLECTION_FREQUENCIES_FILE_FILTER = 
    new KFileFilter(COLLECTION_FREQUENCIES_FILE_EXTENSION, 
    "DIAsDEM Collection Frequencies Files (*" + COLLECTION_FREQUENCIES_FILE_EXTENSION + ")");

  public final static String URL_FILE_EXTENSION = ".url";
  public final static KFileFilter URL_FILE_FILTER =
    new KFileFilter(URL_FILE_EXTENSION,
    "DIAsDEM Download URLs Files (*" + URL_FILE_EXTENSION + ")");

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public DIAsDEMguiPreferences() {

    super(MY_PROPERTY_DATA);
    FileName = null;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public DIAsDEMguiPreferences(String pFileName, int pTask) 
  throws IOException {

    super(MY_PROPERTY_DATA);
    
    FileName = pFileName;
    if (pTask == CREATE) {
      this.save(FileName);
    }
    else if (pTask == LOAD) {
      if (Tools.isExistingFile(pFileName)) {
        this.load(FileName);
      }
      else {
        throw new IOException();
      }
    }
    else {
      throw new IOException();
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getFileName() {
    return FileName; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(100);
    TmpStringBuffer.append("DIAsDEM Gui Preferences: FileName=");
    TmpStringBuffer.append(FileName);
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface GuiClientPreferences methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getMruProjectFileName() {
    return this.getStringProperty("MRU_PROJECT_FILE_NAME");
  }

  public void setMruProjectFileName(String pProperty) {
    this.setProperty("MRU_PROJECT_FILE_NAME", pProperty);
  }

  /* ########## ########## ########## ########## ########## ######### */

  public String getMruProjectDirectory() {
    return this.getStringProperty("MRU_PROJECT_DIRECTORY");
  }
  
  public void setMruProjectDirectory(String pProperty) {
    this.setProperty("MRU_PROJECT_DIRECTORY", pProperty);
  }

  /* ########## ########## ########## ########## ########## ######### */

  public String getMruParameterDirectory() {
    return this.getStringProperty("MRU_PARAMETER_DIRECTORY");
  }
  
  public void setMruParameterDirectory(String pProperty) {
    this.setProperty("MRU_PARAMETER_DIRECTORY", pProperty);
  }
 
  /* ########## ########## ########## ########## ########## ######### */

  public boolean getShowWarnings() {
    return this.getBooleanProperty("SHOW_WARNINGS");
  }
  
  public void setShowWarnings(boolean pProperty) {
    this.setProperty("SHOW_WARNINGS", ( new Boolean(pProperty) ).toString() );
  }
 
  /* ########## ########## ########## ########## ########## ######### */

  public int getFrameSizeX() {
    return this.getIntProperty("FRAME_SIZE_X");
  } 
  
  public int getFrameSizeY() {
    return this.getIntProperty("FRAME_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getFramePositionX() {
    return this.getIntProperty("FRAME_POSITION_X");
  } 
  
  public int getFramePositionY() {
    return this.getIntProperty("FRAME_POSITION_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void setFramePositionX(int pFramePositionX) {
    this.setIntProperty("FRAME_POSITION_X", pFramePositionX);
  } 
  
  public void setFramePositionY(int pFramePositionY) {
    this.setIntProperty("FRAME_POSITION_Y", pFramePositionY);
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogXxsSizeX() {
    return this.getIntProperty("DIALOG_XXS_SIZE_X");
  }
  
  public int getDialogXxsSizeY() {
    return this.getIntProperty("DIALOG_XXS_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogXsSizeX() {
    return this.getIntProperty("DIALOG_XS_SIZE_X");
  }
  
  public int getDialogXsSizeY() {
    return this.getIntProperty("DIALOG_XS_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogSSizeX() {
    return this.getIntProperty("DIALOG_S_SIZE_X");
  }
  
  public int getDialogSSizeY() {
    return this.getIntProperty("DIALOG_S_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogMSizeX() {
    return this.getIntProperty("DIALOG_M_SIZE_X");
  }
  
  public int getDialogMSizeY() {
    return this.getIntProperty("DIALOG_M_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogLSizeX() {
    return this.getIntProperty("DIALOG_L_SIZE_X");
  }
  
  public int getDialogLSizeY() {
    return this.getIntProperty("DIALOG_L_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogXlSizeX() {
    return this.getIntProperty("DIALOG_XL_SIZE_X");
  }
  
  public int getDialogXlSizeY() {
    return this.getIntProperty("DIALOG_XL_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public String getIntroImageFileName() {
    return this.getStringProperty("INTRO_IMAGE_FILE_NAME");
  }

  /* ########## ########## ########## ########## ########## ######### */

  public String getWindowIconImageFileName() {
    return this.getStringProperty("WINDOW_ICON_IMAGE_FILE_NAME");
  }

  /* ########## ########## ########## ########## ########## ######### */

  public boolean getShowWelcomeWindow() {
    return this.getBooleanProperty("SHOW_WELCOME_WINDOW");
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getMruBatchScriptFileName() {
    return this.getStringProperty("MRU_BATCH_SCRIPT_FILE_NAME");
  }

  public void setMruBatchScriptFileName(String pProperty) {
    this.setProperty("MRU_BATCH_SCRIPT_FILE_NAME", pProperty);
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getLookAndFeelClassName() {
    return this.getStringProperty("LOOK_AND_FEEL_CLASS_NAME");
  }

  public void setLookAndFeelClassName(String pProperty) {
    this.setProperty("LOOK_AND_FEEL_CLASS_NAME", pProperty);
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}