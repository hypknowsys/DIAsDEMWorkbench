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

package org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ExportOracle8iSqlScriptsParameter
extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String DtdFileName = null;
  protected int DatabaseType = ORACLE_8I;
  protected String DatabaseUser = null;
  protected String DatabasePassword = null;
  protected String ScriptDirectory = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts"
  + ".ExportOracle8iSqlScriptsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.deploy.exportOracle8iSqlScripts"
  + ".ExportOracle8iSqlScriptsParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String DTD_FILE_NAME =
  "DtdFileName";
  private final static String DATABASE_TYPE =
  "DatabaseType";
  private final static String DATABASE_USER =
  "DatabaseUser";
  private final static String DATABASE_PASSWORD =
  "DatabasePassword";
  private final static String SCRIPT_DIRECTORY =
  "ScriptDirectory";
  
  public final static int ORACLE_8I = 0;
  public final static String[] DATABASE_TYPES = {
    "Oracle 8i (8.1.6) incl. interMedia"};

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
    public ExportOracle8iSqlScriptsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;

    CollectionFileName = null;
    DtdFileName = null;
    DatabaseType = ORACLE_8I;
    DatabaseUser = null;
    DatabasePassword = null;
    ScriptDirectory = null;
    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
    public ExportOracle8iSqlScriptsParameter(String pCollectionFileName,
    String pDtdFileName, String pDatabaseType, String pDatabaseUser,
    String pDatabasePassword, String pScriptDirectory) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    DtdFileName = pDtdFileName;
    DatabaseUser = pDatabaseUser;
    DatabasePassword = pDatabasePassword;
    ScriptDirectory = pScriptDirectory;

    DatabaseType = ORACLE_8I;
    for (int i = 0; i < DATABASE_TYPES.length; i++)
      if  (pDatabaseType.equals(DATABASE_TYPES[i])) {
        DatabaseType = i;
        break;
      }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
    public ExportOracle8iSqlScriptsParameter(String pCollectionFileName,
    String pDtdFileName, int pDatabaseType, String pDatabaseUser,
    String pDatabasePassword, String pScriptDirectory) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    DtdFileName = pDtdFileName;
    DatabaseType = pDatabaseType;
    DatabaseUser = pDatabaseUser;
    DatabasePassword = pDatabasePassword;
    ScriptDirectory = pScriptDirectory;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() { 
    return CollectionFileName; }
  public String getDtdFileName() { 
    return DtdFileName; }
  public int getDatabaseType() { 
    return DatabaseType; }
  public String getDatabaseUser() { 
    return DatabaseUser; }
  public String getDatabasePassword() { 
    return DatabasePassword; }
  public String getScriptDirectory() { 
    return ScriptDirectory; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setScriptDirectory(String pScriptDirectory) { 
    ScriptDirectory = pScriptDirectory; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface ScriptableTaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public org.jdom.Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(DTD_FILE_NAME, DtdFileName);
    ParameterAttributes.put(DATABASE_TYPE, 
    Tools.int2String(DatabaseType));
    ParameterAttributes.put(DATABASE_USER, DatabaseUser);
    ParameterAttributes.put(DATABASE_PASSWORD, DatabasePassword);
    ParameterAttributes.put(SCRIPT_DIRECTORY, ScriptDirectory);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes.get(COLLECTION_FILE_NAME);
    DtdFileName = (String)ParameterAttributes.get(DTD_FILE_NAME);
    DatabaseType = Tools.string2Int(
    (String)ParameterAttributes.get(DATABASE_TYPE));
    DatabaseUser = (String)ParameterAttributes.get(DATABASE_USER);
    DatabasePassword = (String)ParameterAttributes.get(DATABASE_PASSWORD);
    ScriptDirectory = (String)ParameterAttributes.get(SCRIPT_DIRECTORY);
    
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