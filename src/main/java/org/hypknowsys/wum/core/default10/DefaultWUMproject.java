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

package org.hypknowsys.wum.core.default10;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class DefaultWUMproject extends KProperties implements WUMproject {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  MiningBase MyMiningBase = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public final static KProperty[] MY_PROPERTY_DATA = {
    new KProperty("PROJECT_NAME", "Project Name",
    "<DefaultProjectName>", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("PROJECT_NOTES", "Project Notes",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("PROJECT_FILE_NAME", 
    "Absolute File Name of Project File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("PROJECT_DIRECTORY", 
    "Absolute File Name of Project Directory",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("PARAMETER_DIRECTORY", 
    "Absolute File Name of Parameter Directory",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MRU_BATCH_SCRIPT_FILE_NAME", 
    "Most Recently Used WUM Batch Script File Name", 
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("IMPORT_LOG_FILE:_LINES_UPDATE_INTERVAL",
    "Import Log File: GUI Update Interval (Lines)", 
    "50", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("SESSIONIZE_LOG_FILES:_SESSIONS_UPDATE_INTERVAL", 
    "Sessionize Log Files: GUI Update Interval [Sessions]",
    "50", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("SESSIONIZE_LOG_FILES:_SESSIONS_START_CLEANING_SESSIONS", 
    "Sessionize Log Files: Start Cleaning Sessions at Session",
    "2500", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("SESSIONIZE_LOG_FILES:_SESSIONS_START_CLEANING_TIME", 
    "Sessionize Log Files: Start Cleaning Time",
    "0/00:00:00", KProperty.TIMESTRING, KProperty.EDITABLE),
    new KProperty("SESSIONIZE_LOG_FILES:_INTERVAL_CLEANING_TIME", 
    "Sessionize Log Files: Interval Cleaning Time",
    "1/00:00:00", KProperty.TIMESTRING, KProperty.EDITABLE),
    new KProperty("SESSIONIZE_LOG_FILES:_INFINITE_SESSION_TIME", 
    "Sessionize Log Files: Infinite Sessions Time",
    "10/00:00:00", KProperty.TIMESTRING, KProperty.EDITABLE),
    new KProperty("AGGREGATE_SESSIONS:_SESSIONS_UPDATE_INTERVAL",
    "Aggregate Sessions: GUI Update Interval (Sessions)", 
    "50", KProperty.INTEGER, KProperty.EDITABLE)
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public DefaultWUMproject() {

    super(MY_PROPERTY_DATA);
    FileName = null;
    
    MyMiningBase = new MiningBase("Name", "Server", 
    Tools.ensureTrailingSlash(this.getParameterDirectory()), 
    Tools.ensureTrailingSlash(this.getProjectDirectory()), "Remarks");

  }

  /* ########## ########## ########## ########## ########## ######### */

  public DefaultWUMproject(String pFileName, int pTask) throws IOException {

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
    
    MyMiningBase = new MiningBase("Name", "Server", 
    Tools.ensureTrailingSlash(this.getParameterDirectory()), 
    Tools.ensureTrailingSlash(this.getProjectDirectory()), "Remarks");

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getProjectName() { 
    return this.getStringProperty("PROJECT_NAME"); } 
  public String getProjectNotes() { 
    return this.getStringProperty("PROJECT_NOTES"); } 
  public String getProjectFileName() { 
    return this.getStringProperty("PROJECT_FILE_NAME"); }
  public String getProjectDirectory() { 
    return this.getStringProperty("PROJECT_DIRECTORY"); } 
  public String getParameterDirectory() { 
    return this.getStringProperty("PARAMETER_DIRECTORY"); } 

  public MiningBase getMiningBase() { 
    return this.MyMiningBase; }   
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setProjectName(String pProjectName) { 
    this.setProperty("PROJECT_NAME", pProjectName); 
    this.quickSave();
  }
  public void setProjectNotes(String pProjectNotes) { 
    this.setProperty("PROJECT_NOTES", pProjectNotes);  
    this.quickSave();
  }
  public void setProjectFileName(String pProjectFileName) { 
    this.setProperty("PROJECT_FILE_NAME", pProjectFileName); 
    FileName = pProjectFileName;
    this.quickSave();
  }
  public void setProjectDirectory(String pProjectDirectory) { 
    this.setProperty("PROJECT_DIRECTORY", pProjectDirectory);
    this.quickSave();
  }
  public void setParameterDirectory(String pParameterDirectory) { 
    this.setProperty("PARAMETER_DIRECTORY", pParameterDirectory);
    this.quickSave();
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(100);
    TmpStringBuffer.append("WUM Project: Name=");
    TmpStringBuffer.append( this.getProjectName() );
    TmpStringBuffer.append("; FileName=");
    TmpStringBuffer.append( this.getProjectFileName() );
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface WUMproject methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */

  public void quickSave() {

    if (FileName != null && FileName.length() > 0) {
      super.quickSave();
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public int getMiningBaseStatus() { 
    
    if (this.MyMiningBase != null) {
      return this.MyMiningBase.getStatus();
    }
    else {
      return MiningBase.MINING_BASE_IS_NOT_INSTANTIATED;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void setMiningBaseStatus(int pMiningBaseStatus) { 
    
    if (this.MyMiningBase != null) {
      this.MyMiningBase.setStatus(pMiningBaseStatus);
    }
    
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

  public static void main(String args[]) {}
  
}