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

package org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class MergeParameterTextFilesParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String SourceParameterFileName1 = null;
  protected String SourceParameterFileName2 = null;
  protected String SourceParameterFileName3 = null;
  protected String SourceParameterFileName4 = null;
  protected String TargetParameterFileName = null;
  protected int MergeType = CREATE_OR_REPLACE_TARGET;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles"
  + ".MergeParameterTextFilesTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.mergeParameterTextFiles"
  + ".MergeParameterTextFilesParameterPanel";
  
  private final static String SOURCE_PARAMETER_FILE_NAME1 =
  "SourceParameterFileName1";
  private final static String SOURCE_PARAMETER_FILE_NAME2 =
  "SourceParameterFileName2";
  private final static String SOURCE_PARAMETER_FILE_NAME3 =
  "SourceParameterFileName3";
  private final static String SOURCE_PARAMETER_FILE_NAME4 =
  "SourceParameterFileName4";
  private final static String TARGET_PARAMETER_FILE_NAME =
  "TargetParameterFileName";
  private final static String MERGE_TYPE =
  "MergeType";
  
  public final static int CREATE_OR_REPLACE_TARGET = 0;
  public final static int APPEND_SOURCES_TO_TARGET = 1;
  public final static String[] MERGE_TYPES = {
    "Create or Replace Target Parameter File",
    "Append Source Files to Target Parameter File"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public MergeParameterTextFilesParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    SourceParameterFileName1 = null;
    SourceParameterFileName2 = null;
    SourceParameterFileName3 = null;
    SourceParameterFileName4 = null;
    TargetParameterFileName = null;
    MergeType = CREATE_OR_REPLACE_TARGET;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public MergeParameterTextFilesParameter(String pSourceParameterFileName1,
  String pSourceParameterFileName2, String pSourceParameterFileName3,
  String pSourceParameterFileName4, String pTargetParameterFileName, 
  String pMergeType) {
    
    this();
    
    SourceParameterFileName1 = pSourceParameterFileName1;
    SourceParameterFileName2 = pSourceParameterFileName2;
    SourceParameterFileName3 = pSourceParameterFileName3;
    SourceParameterFileName4 = pSourceParameterFileName4;
    TargetParameterFileName = pTargetParameterFileName;
    MergeType = CREATE_OR_REPLACE_TARGET;
    
    for (int i = 0; i < MERGE_TYPES.length; i++) {
      if (MERGE_TYPES[i].equals(pMergeType)) {
        MergeType = i;
        break;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public MergeParameterTextFilesParameter(String pSourceParameterFileName1,
  String pSourceParameterFileName2, String pSourceParameterFileName3,
  String pSourceParameterFileName4, String pTargetParameterFileName, 
  int pMergeType) {
    
    this();
    
    SourceParameterFileName1 = pSourceParameterFileName1;
    SourceParameterFileName2 = pSourceParameterFileName2;
    SourceParameterFileName3 = pSourceParameterFileName3;
    SourceParameterFileName4 = pSourceParameterFileName4;
    TargetParameterFileName = pTargetParameterFileName;
    MergeType = pMergeType;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getSourceParameterFileName1() {
    return SourceParameterFileName1; }
  public String getSourceParameterFileName2() {
    return SourceParameterFileName2; }
  public String getSourceParameterFileName3() {
    return SourceParameterFileName3; }
  public String getSourceParameterFileName4() {
    return SourceParameterFileName4; }
  public String getTargetParameterFileName() {
    return TargetParameterFileName; }
  public int getMergeType() {
    return MergeType; }
  
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
  /* ########## interface ScriptableTaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public org.jdom.Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(SOURCE_PARAMETER_FILE_NAME1, 
    SourceParameterFileName1);
    ParameterAttributes.put(SOURCE_PARAMETER_FILE_NAME2, 
    SourceParameterFileName2);
    ParameterAttributes.put(SOURCE_PARAMETER_FILE_NAME3, 
    SourceParameterFileName3);
    ParameterAttributes.put(SOURCE_PARAMETER_FILE_NAME4, 
    SourceParameterFileName4);
    ParameterAttributes.put(TARGET_PARAMETER_FILE_NAME, 
    TargetParameterFileName);
    ParameterAttributes.put(MERGE_TYPE, Tools.int2String(
    MergeType));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    SourceParameterFileName1 = (String)ParameterAttributes
    .get(SOURCE_PARAMETER_FILE_NAME1);
    SourceParameterFileName2 = (String)ParameterAttributes
    .get(SOURCE_PARAMETER_FILE_NAME2);
    SourceParameterFileName3 = (String)ParameterAttributes
    .get(SOURCE_PARAMETER_FILE_NAME3);
    SourceParameterFileName4 = (String)ParameterAttributes
    .get(SOURCE_PARAMETER_FILE_NAME4);
    TargetParameterFileName = (String)ParameterAttributes
    .get(TARGET_PARAMETER_FILE_NAME);
    MergeType = Tools.string2Int((String)ParameterAttributes
    .get(MERGE_TYPE));
    
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