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

package org.hypknowsys.diasdem.tasks.postprocess.tagTextUnits;

import java.util.TreeMap;
import org.hypknowsys.diasdem.server.DiasdemScriptableTaskParameter;
import org.hypknowsys.misc.util.Tools;
import org.jdom.Element;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class TagTextUnitsParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String ClusterLabelFileName = null;
  protected String ClusterResultFileName = null;
  protected int ClusterResultFileFormat = CSV_FILE;
  protected int Iteration = 1;
  protected boolean IgnoreFirstResultFileLine = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagTextUnits"
  + ".TagTextUnitsTask";
  private static final String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.tagTextUnits"
  + ".TagTextUnitsParameterPanel";
  
  private static final String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private static final String CLUSTER_LABEL_FILE_NAME =
  "ClusterLabelFileName";
  private static final String CLUSTER_RESULT_FILE_NAME =
  "ClusterResultFileName";
  private static final String CLUSTER_RESULT_FILE_FORMAT =
  "ClusterResultFileFormat";
  private static final String ITERATION =
  "Iteration";
  private static final String IGNORE_FIRST_RESULT_FILE_LINE =
  "IgnoreFirstResultFileLine";
  
  public static final int CSV_FILE = 0;
  public static final int TXT_FILE = 1;
  public static final String[] RESULT_FILE_FORMAT = {
    "CSV: Comma Separated Values",
    "TXT: Fixed Width Values"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagTextUnitsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    ClusterLabelFileName = null;
    ClusterResultFileName = null;
    ClusterResultFileFormat = CSV_FILE;
    Iteration = 1;
    IgnoreFirstResultFileLine = false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagTextUnitsParameter(String pCollectionFileName,
  String pClusterLabelFileName, String pClusterResultFileName,
  String pClusterResultFileFormat, int pIteration,
  boolean pIgnoreFirstResultFileLine) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ClusterLabelFileName = pClusterLabelFileName;
    ClusterResultFileName = pClusterResultFileName;
    IgnoreFirstResultFileLine = pIgnoreFirstResultFileLine;
    Iteration = pIteration;
    
    if (pClusterResultFileFormat.equals(RESULT_FILE_FORMAT[CSV_FILE])) {
      ClusterResultFileFormat = CSV_FILE;
    }
    else if (pClusterResultFileFormat.equals(RESULT_FILE_FORMAT[TXT_FILE])) {
      ClusterResultFileFormat = TXT_FILE;
    }
    else {
      ClusterResultFileFormat = CSV_FILE;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TagTextUnitsParameter(String pCollectionFileName,
  String pClusterLabelFileName, String pClusterResultFileName,
  int pClusterResultFileFormat, int pIteration,
  boolean pIgnoreFirstResultFileLine) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ClusterLabelFileName = pClusterLabelFileName;
    ClusterResultFileFormat = pClusterResultFileFormat;
    ClusterResultFileName = pClusterResultFileName;
    Iteration = pIteration;
    IgnoreFirstResultFileLine = pIgnoreFirstResultFileLine;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getClusterLabelFileName() {
    return ClusterLabelFileName; }
  public String getClusterResultFileName() {
    return ClusterResultFileName; }
  public int getClusterResultFileFormat() {
    return ClusterResultFileFormat; }
  public int getIteration() {
    return Iteration; }
  public boolean ignoreFirstResultFileLine() {
    return IgnoreFirstResultFileLine; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setIgnoreFirstResultFileLine(boolean pIgnoreFirstResultFileLine) {
    IgnoreFirstResultFileLine = pIgnoreFirstResultFileLine; }
  
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
  
  public Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(CLUSTER_LABEL_FILE_NAME, ClusterLabelFileName);
    ParameterAttributes.put(CLUSTER_RESULT_FILE_NAME, ClusterResultFileName);
    ParameterAttributes.put(CLUSTER_RESULT_FILE_FORMAT,
    Tools.int2String(ClusterResultFileFormat));
    ParameterAttributes.put(ITERATION,
    Tools.int2String(Iteration));
    ParameterAttributes.put(IGNORE_FIRST_RESULT_FILE_LINE,
    Tools.boolean2String(IgnoreFirstResultFileLine));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    ClusterLabelFileName = (String)ParameterAttributes
    .get(CLUSTER_LABEL_FILE_NAME);
    ClusterResultFileName = (String)ParameterAttributes
    .get(CLUSTER_RESULT_FILE_NAME);
    ClusterResultFileFormat = Tools.string2Int(
    (String)ParameterAttributes.get(CLUSTER_RESULT_FILE_FORMAT));
    Iteration = Tools.string2Int(
    (String)ParameterAttributes.get(ITERATION));
    IgnoreFirstResultFileLine = Tools.string2Boolean(
    (String)ParameterAttributes.get(IGNORE_FIRST_RESULT_FILE_LINE));
    
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
  
  public static void main(String pOptions[]) {}
  
}