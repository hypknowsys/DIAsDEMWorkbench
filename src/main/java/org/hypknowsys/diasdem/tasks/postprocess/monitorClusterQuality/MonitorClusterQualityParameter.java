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

package org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class MonitorClusterQualityParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String ThesaurusFileName = null;
  protected String ClusterResultFileName = null;
  protected int ClusterResultFileFormat = CSV_FILE;
  protected String ClusterDirectory = null;
  protected String ClusterLabelFileName = null;
  protected int NumberOfClusters = 0;
  protected int Iteration = 1;
  protected int MinClusterCardinality = 0;
  protected double MaxDistinctDescriptorsRatio = 0.0;
  protected double MinFrequentDescriptorsRatio = 0.0;
  protected int VectorDimensions = ALL_DESCRIPTORS;
  protected String DescriptorsScopeNotesContain = null;
  protected boolean IgnoreFirstResultFileLine = false;
  protected boolean IgnoreEmptyClusters = false;
  protected boolean LaunchHtmlBrowser = false;
  protected boolean LaunchClusterLabelEditor = false;
  protected boolean DumpDocumentsForVisualization = false;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality.MonitorClusterQualityTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality.MonitorClusterQualityParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String THESAURUS_FILE_NAME =
  "ThesaurusFileName";
  private final static String CLUSTER_RESULT_FILE_NAME =
  "ClusterResultFileName";
  private final static String CLUSTER_RESULT_FILE_FORMAT =
  "ClusterResultFileFormat";
  private final static String CLUSTER_DIRECTORY =
  "ClusterDirectory";
  private final static String CLUSTER_LABEL_FILE_NAME =
  "ClusterLabelFileName";
  private final static String NUMBER_OF_CLUSTERS =
  "NumberOfClusters";
  private final static String ITERATION =
  "Iteration";
  private final static String MIN_CLUSTER_CARDINALITY =
  "MinClusterCardinality";
  private final static String MAX_DISTINCT_DESCRIPTORS_RATIO =
  "MaxDistinctDescriptorsRatio";
  private final static String MIN_FREQUENT_DESCRIPTORS_RATIO =
  "MinFrequentDescriptorsRatio";
  private final static String VECTOR_DIMENSIONS =
  "VectorDimensions";
  private final static String DESCRIPTOR_SCOPE_NOTES_CONTAIN =
  "DescriptorsScopeNotesContain";
  private final static String IGNORE_FIRST_RESULT_FILE_LINE =
  "IgnoreFirstResultFileLine";
  private final static String IGNORE_EMPTY_CLUSTERS =
  "IgnoreEmptyClusters";
  private final static String LAUNCH_HTML_BROWSER =
  "LaunchHtmlBrowser";
  private final static String LAUNCH_CLUSTER_LABEL_EDITOR =
  "LaunchClusterLabelEditor";
  private final static String DUMP_DOCUMENTS_FOR_VISUALIZATION =
  "DumpDocumentsForVisualization";
  
  public final static int CSV_FILE = 0;
  public final static int TXT_FILE = 1;
  public final static String[] RESULT_FILE_FORMAT = {
    "CSV: Comma Separated Values", 
    "TXT: Fixed Width Values"
  };

  public final static int ALL_DESCRIPTORS = 0;
  public final static int SPECIFIED_DESCRIPTORS = 1;
  public final static int NOT_SPECIFIED_DESCRIPTORS = 2;
  public final static String[] VECTOR_DIMENSIONS_OPTIONS = {
    "All Descriptors in Thesaurus", 
    "Descriptors whose Scope Notes Contain String", 
    "Descriptors whose Scope Notes Don't Contain String" 
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;

    CollectionFileName = null;
    ThesaurusFileName = null;
    ClusterResultFileName = null;
    ClusterResultFileFormat = CSV_FILE;
    ClusterDirectory = null;
    ClusterLabelFileName = null;
    NumberOfClusters = 0;
    Iteration = 1;
    MinClusterCardinality = 0;
    MaxDistinctDescriptorsRatio = 0.0;
    MinFrequentDescriptorsRatio = 0.0;
    VectorDimensions = ALL_DESCRIPTORS;
    DescriptorsScopeNotesContain = null;
    IgnoreFirstResultFileLine = false;
    IgnoreEmptyClusters = false;
    LaunchHtmlBrowser = false;
    LaunchClusterLabelEditor = false;
    DumpDocumentsForVisualization = false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityParameter(String pCollectionFileName, 
    String pThesaurusFileName, String pClusterResultFileName, 
    String pClusterResultFileFormat, String pClusterDirectory, 
    int pNumberOfClusters, int pIteration, int pMinClusterCardinality, 
    double pMaxDistinctDescriptorsRatio, double pMinFrequentDescriptorsRatio, 
    String pVectorDimensions, String pDescriptorsScopeNotesContain, 
    String pClusterLabelFileName, boolean pIgnoreFirstResultFileLine, 
    boolean pIgnoreEmptyClusters, boolean pLaunchHtmlBrowser, 
    boolean pLaunchClusterLabelEditor, boolean pDumpDocumentsForVisualization) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ThesaurusFileName = pThesaurusFileName;
    ClusterResultFileName = pClusterResultFileName;
    ClusterDirectory = pClusterDirectory;
    NumberOfClusters = pNumberOfClusters;
    Iteration = pIteration;
    MinClusterCardinality = pMinClusterCardinality;
    MaxDistinctDescriptorsRatio = pMaxDistinctDescriptorsRatio;
    MinFrequentDescriptorsRatio = pMinFrequentDescriptorsRatio;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    ClusterLabelFileName = pClusterLabelFileName;
    IgnoreFirstResultFileLine = pIgnoreFirstResultFileLine;
    IgnoreEmptyClusters = pIgnoreEmptyClusters;
    LaunchHtmlBrowser = pLaunchHtmlBrowser;
    LaunchClusterLabelEditor = pLaunchClusterLabelEditor;
    DumpDocumentsForVisualization = pDumpDocumentsForVisualization;

    if (pVectorDimensions.equals(VECTOR_DIMENSIONS_OPTIONS[ALL_DESCRIPTORS])) 
      VectorDimensions = ALL_DESCRIPTORS;
    else if (pVectorDimensions.equals(VECTOR_DIMENSIONS_OPTIONS[SPECIFIED_DESCRIPTORS])) 
      VectorDimensions = SPECIFIED_DESCRIPTORS;
    else
      VectorDimensions = NOT_SPECIFIED_DESCRIPTORS;

    if (pClusterResultFileFormat.equals(RESULT_FILE_FORMAT[CSV_FILE])) 
      ClusterResultFileFormat = CSV_FILE;
    else if (pClusterResultFileFormat.equals(RESULT_FILE_FORMAT[TXT_FILE]))
      ClusterResultFileFormat = TXT_FILE;
    else 
      ClusterResultFileFormat = CSV_FILE;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityParameter(String pCollectionFileName, 
    String pThesaurusFileName, String pClusterResultFileName, 
    int pClusterResultFileFormat, String pClusterDirectory, 
    int pNumberOfClusters, int pIteration, int pMinClusterCardinality, 
    double pMaxDistinctDescriptorsRatio, double pMinFrequentDescriptorsRatio, 
    int pVectorDimensions, String pDescriptorsScopeNotesContain, 
    String pClusterLabelFileName, boolean pIgnoreFirstResultFileLine, 
    boolean pIgnoreEmptyClusters, boolean pLaunchHtmlBrowser, 
    boolean pLaunchClusterLabelEditor, boolean pDumpDocumentsForVisualization) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ThesaurusFileName = pThesaurusFileName;
    ClusterResultFileName = pClusterResultFileName;
    ClusterResultFileFormat = pClusterResultFileFormat;
    ClusterDirectory = pClusterDirectory;
    NumberOfClusters = pNumberOfClusters;
    Iteration = pIteration;
    MinClusterCardinality = pMinClusterCardinality;
    MaxDistinctDescriptorsRatio = pMaxDistinctDescriptorsRatio;
    MinFrequentDescriptorsRatio = pMinFrequentDescriptorsRatio;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    VectorDimensions = pVectorDimensions;
    ClusterLabelFileName = pClusterLabelFileName;
    IgnoreFirstResultFileLine = pIgnoreFirstResultFileLine;
    IgnoreEmptyClusters = pIgnoreEmptyClusters;
    LaunchHtmlBrowser = pLaunchHtmlBrowser;
    LaunchClusterLabelEditor = pLaunchClusterLabelEditor;
    DumpDocumentsForVisualization = pDumpDocumentsForVisualization;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getThesaurusFileName() { 
    return ThesaurusFileName; }
  public String getClusterResultFileName() { 
    return ClusterResultFileName; }
  public int getClusterResultFileFormat() { 
    return ClusterResultFileFormat; }
  public String getClusterDirectory() { 
    return ClusterDirectory; }
  public String getClusterLabelFileName() { 
    return ClusterLabelFileName; }
  public int getNumberOfClusters() { 
    return NumberOfClusters; }
  public int getIteration() { 
    return Iteration; }
  public int getMinClusterCardinality() { 
    return MinClusterCardinality; }
  public double getMaxDistinctDescriptorsRatio() { 
    return MaxDistinctDescriptorsRatio; }
  public double getMinFrequentDescriptorsRatio() { 
    return MinFrequentDescriptorsRatio; }
  public int getVectorDimensions() { 
    return VectorDimensions; }
  public String getDescriptorsScopeNotesContain() { 
    return DescriptorsScopeNotesContain; }
  public boolean ignoreFirstResultFileLine() {
    return IgnoreFirstResultFileLine; }
  public boolean ignoreEmptyClusters() {
    return IgnoreEmptyClusters; }
  public boolean launchHtmlBrowser() {
    return LaunchHtmlBrowser; }
  public boolean launchClusterLabelEditor() {
    return LaunchClusterLabelEditor; }
  public boolean dumpDocumentsForVisualization() {
    return DumpDocumentsForVisualization; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void incrementNumberOfClusters() { 
    this.NumberOfClusters++; }
  public void setNumberOfClusters(int pNumberOfClusters) {
    NumberOfClusters = pNumberOfClusters; }
  public void setIgnoreFirstResultFileLine(boolean pIgnoreFirstResultFileLine) {
    IgnoreFirstResultFileLine = pIgnoreFirstResultFileLine; }
  public void setIgnoreEmptyClusters(boolean pIgnoreEmptyClusters) {
    IgnoreEmptyClusters = pIgnoreEmptyClusters; }
  public void setLaunchHtmlBrowser(boolean pLaunchHtmlBrowser) {
    LaunchHtmlBrowser = pLaunchHtmlBrowser; }
  public void setLaunchClusterLabelEditor(boolean pLaunchClusterLabelEditor) {
    LaunchClusterLabelEditor = pLaunchClusterLabelEditor; }
  public void setDumpDocumentsForVisualization(boolean pDumpDocumentsForVisualization) {
    DumpDocumentsForVisualization = pDumpDocumentsForVisualization; }
  
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
    ParameterAttributes.put(COLLECTION_FILE_NAME, 
    CollectionFileName);
    ParameterAttributes.put(THESAURUS_FILE_NAME, 
    ThesaurusFileName);
    ParameterAttributes.put(CLUSTER_RESULT_FILE_NAME, 
    ClusterResultFileName);
    ParameterAttributes.put(CLUSTER_RESULT_FILE_FORMAT, 
    Tools.int2String(ClusterResultFileFormat));
    ParameterAttributes.put(CLUSTER_DIRECTORY, 
    ClusterDirectory);
    ParameterAttributes.put(CLUSTER_LABEL_FILE_NAME, 
    ClusterLabelFileName);
    ParameterAttributes.put(NUMBER_OF_CLUSTERS, 
    Tools.int2String(NumberOfClusters));
    ParameterAttributes.put(ITERATION, 
    Tools.int2String(Iteration));
    ParameterAttributes.put(MIN_CLUSTER_CARDINALITY, 
    Tools.int2String(MinClusterCardinality));
    ParameterAttributes.put(MAX_DISTINCT_DESCRIPTORS_RATIO, 
    Tools.double2String(MaxDistinctDescriptorsRatio));
    ParameterAttributes.put(MIN_FREQUENT_DESCRIPTORS_RATIO, 
    Tools.double2String(MinFrequentDescriptorsRatio));
    ParameterAttributes.put(VECTOR_DIMENSIONS, 
    Tools.int2String(VectorDimensions));
    ParameterAttributes.put(DESCRIPTOR_SCOPE_NOTES_CONTAIN, 
    DescriptorsScopeNotesContain);
    ParameterAttributes.put(IGNORE_FIRST_RESULT_FILE_LINE, 
    Tools.boolean2String(IgnoreFirstResultFileLine));
    ParameterAttributes.put(IGNORE_EMPTY_CLUSTERS, 
    Tools.boolean2String(IgnoreEmptyClusters));
    ParameterAttributes.put(LAUNCH_HTML_BROWSER, 
    Tools.boolean2String(LaunchHtmlBrowser));
    ParameterAttributes.put(LAUNCH_CLUSTER_LABEL_EDITOR, 
    Tools.boolean2String(LaunchClusterLabelEditor));
    ParameterAttributes.put(DUMP_DOCUMENTS_FOR_VISUALIZATION, 
    Tools.boolean2String(DumpDocumentsForVisualization));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    ThesaurusFileName = (String)ParameterAttributes
    .get(THESAURUS_FILE_NAME);
    ClusterResultFileName = (String)ParameterAttributes
    .get(CLUSTER_RESULT_FILE_NAME);
    ClusterResultFileFormat = Tools.string2Int(
    (String)ParameterAttributes.get(CLUSTER_RESULT_FILE_FORMAT));
    ClusterDirectory = (String)ParameterAttributes
    .get(CLUSTER_DIRECTORY);
    ClusterLabelFileName = (String)ParameterAttributes
    .get(CLUSTER_LABEL_FILE_NAME);
    NumberOfClusters = Tools.string2Int(
    (String)ParameterAttributes.get(NUMBER_OF_CLUSTERS));
    Iteration = Tools.string2Int(
    (String)ParameterAttributes.get(ITERATION));
    MinClusterCardinality = Tools.string2Int(
    (String)ParameterAttributes.get(MIN_CLUSTER_CARDINALITY));
    MaxDistinctDescriptorsRatio = Tools.string2Double(
    (String)ParameterAttributes.get(MAX_DISTINCT_DESCRIPTORS_RATIO));
    MinFrequentDescriptorsRatio = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_FREQUENT_DESCRIPTORS_RATIO));
    VectorDimensions = Tools.string2Int(
    (String)ParameterAttributes.get(VECTOR_DIMENSIONS));
    DescriptorsScopeNotesContain = (String)ParameterAttributes
    .get(DESCRIPTOR_SCOPE_NOTES_CONTAIN);
    IgnoreFirstResultFileLine = Tools.string2Boolean(
    (String)ParameterAttributes.get(IGNORE_FIRST_RESULT_FILE_LINE));
    IgnoreEmptyClusters = Tools.string2Boolean(
    (String)ParameterAttributes.get(IGNORE_EMPTY_CLUSTERS));
    LaunchHtmlBrowser = Tools.string2Boolean(
    (String)ParameterAttributes.get(LAUNCH_HTML_BROWSER));
    LaunchClusterLabelEditor = Tools.string2Boolean(
    (String)ParameterAttributes.get(LAUNCH_CLUSTER_LABEL_EDITOR));
    DumpDocumentsForVisualization = Tools.string2Boolean(
    (String)ParameterAttributes.get(DUMP_DOCUMENTS_FOR_VISUALIZATION));
    
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