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

import java.util.*;
import java.io.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.client.gui.*;
import org.hypknowsys.wum.core.*;

/**
  * @version 0.9, 15 August 2003
  * @author Karsten Winkler
  */

public class WUMguiPreferences extends KProperties
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
    "WUMgui.config", KProperty.STRING, KProperty.NOT_EDITABLE),
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
    "Most Recently Used WUM Project File Name",
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
    "/org/hypknowsys/wum/resources/img/WumIntro.gif", 
    KProperty.STRING, KProperty.EDITABLE),
    new KProperty("WUM_VERSION",
    "Version of this WUM Workbench Release",
    WUMconstants.WUM_VERSION, KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("JAVA_VERSION",
    "Required Java (TM) Version for this WUM Workbench Release",
    WUMconstants.JAVA_VERSION, KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("RELEASE_DATE",
    "Release Date of this WUM Workbench",
    WUMconstants.RELEASE_DATE, KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("WRAP_LINES",
    "Wrap Lines in Text Areas and Text Panes",
    "false", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("SHOW_WELCOME_WINDOW",
    "Show Welcome Window",
    "true", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("MRU_BATCH_SCRIPT_FILE_NAME",
    "Most Recently Used WUM Batch Script File Name",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("WUM_WORKBENCH_TITLE", 
    "Title of WUM Workbench Window", 
    "WUM Workbench 1.0", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("LOOK_AND_FEEL_CLASS_NAME", 
    "Fully Qualified Class Name of Java Look and Feel", 
    "com.jgoodies.plaf.plastic.PlasticLookAndFeel",
    KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("EXECUTE_BATCH_SCRIPT:_UNCLOSED_PROJECT_REMAINS_OPEN", 
    "Execute Batch Script: Unless Explicitly Closed, Batch Script Project Remains Open", 
    "true", KProperty.BOOLEAN, KProperty.NOT_EDITABLE)
  };

  public final static String ARFF_FILE_EXTENSION = ".arff";
  public final static KFileFilter ARFF_FILE_FILTER = 
    new KFileFilter(ARFF_FILE_EXTENSION, 
    "Weka Data Mining Files (*" + ARFF_FILE_EXTENSION + ")");

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public WUMguiPreferences() {

    super(MY_PROPERTY_DATA);
    FileName = null;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public WUMguiPreferences(String pFileName, int pTask) throws IOException {

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
    TmpStringBuffer.append("WUM Gui Preferences: FileName=");
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
    return "";
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