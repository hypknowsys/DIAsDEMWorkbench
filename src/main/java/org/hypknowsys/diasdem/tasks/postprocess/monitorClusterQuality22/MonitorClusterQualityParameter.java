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

package org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22;

import java.util.TreeMap;
import org.hypknowsys.diasdem.server.DiasdemScriptableTaskParameter;
import org.hypknowsys.misc.util.Tools;
import org.jdom.Element;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Karsten Winkler
 */

public class MonitorClusterQualityParameter
extends DiasdemScriptableTaskParameter {
  
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
  protected int MinClusterSize = 0;
  protected double MaxDescriptorCoverage = 0.0;
  protected double MinDescriptorDominance = 0.0;
  protected int VectorDimensions = ALL_DESCRIPTORS;
  protected String DescriptorsScopeNotesContain = null;
  protected boolean IgnoreFirstResultFileLine = false;
  protected boolean IgnoreEmptyClusters = false;
  protected boolean RankClustersByQuality = false;
  protected boolean LaunchHtmlBrowser = false;
  protected boolean LaunchClusterLabelEditor = false;
  protected boolean DumpDocumentsForVisualization = false;
  protected double DominantDescriptorThreshold = 0.0;
  protected double RareDescriptorThreshold = 0.0;
  protected double FrequentNonDescriptorThreshold = 0.0;
  protected int MaxNumberOfOutputTextUnits = 0;
  protected boolean IgnoreTextUnitsInOutlierCluster = false;
  protected int OutlierClusterID = -1;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22"
  + ".MonitorClusterQualityTask";
  private static final String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22"
  + ".MonitorClusterQualityParameterPanel";
  
  private static final String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private static final String THESAURUS_FILE_NAME =
  "ThesaurusFileName";
  private static final String CLUSTER_RESULT_FILE_NAME =
  "ClusterResultFileName";
  private static final String CLUSTER_RESULT_FILE_FORMAT =
  "ClusterResultFileFormat";
  private static final String CLUSTER_DIRECTORY =
  "ClusterDirectory";
  private static final String CLUSTER_LABEL_FILE_NAME =
  "ClusterLabelFileName";
  private static final String NUMBER_OF_CLUSTERS =
  "NumberOfClusters";
  private static final String ITERATION =
  "Iteration";
  private static final String MIN_CLUSTER_SIZE =
  "MinClusterSize";
  private static final String MAX_DESCRIPTOR_COVERAGE =
  "MaxDescriptorCoverage";
  private static final String MIN_FREQUENT_DESCRIPTORS_RATIO =
  "MinDescriptorDominance";
  private static final String VECTOR_DIMENSIONS =
  "VectorDimensions";
  private static final String DESCRIPTOR_SCOPE_NOTES_CONTAIN =
  "DescriptorsScopeNotesContain";
  private static final String IGNORE_FIRST_RESULT_FILE_LINE =
  "IgnoreFirstResultFileLine";
  private static final String IGNORE_EMPTY_CLUSTERS =
  "IgnoreEmptyClusters";
  private static final String RANK_CLUSTERS_BY_QUALITY =
  "RankClustersByQuality";
  private static final String LAUNCH_HTML_BROWSER =
  "LaunchHtmlBrowser";
  private static final String LAUNCH_CLUSTER_LABEL_EDITOR =
  "LaunchClusterLabelEditor";
  private static final String DUMP_DOCUMENTS_FOR_VISUALIZATION =
  "DumpDocumentsForVisualization";
  private static final String DOMINANT_DESCRIPTOR_THRESHOLD =
  "DominantDescriptorThreshold";
  private static final String RARE_DESCRIPTOR_THRESHOLD =
  "RareDescriptorThreshold";
  private static final String FREQUENT_NONDESCRIPTOR_THRESHOLD =
  "FrequentNonDescriptorThreshold";
  private static final String MAX_NUMBER_OF_OUTPUT_TEXT_UNITS =
  "MaxNumberOfOutputTextUnits";
  private static final String IGNORE_TEXT_UNITS_IN_OUTLIER_CLUSTER =
  "IgnoreTextUnitsInOutlierCluster";
  private static final String OUTLIER_CLUSTER_ID =
  "OutlierClusterID";
  
  public static final int CSV_FILE = 0;
  public static final int TXT_FILE = 1;
  public static final String[] RESULT_FILE_FORMAT = {
    "CSV: Comma Separated Values",
    "TXT: Fixed Width Values"
  };
  
  public static final int ALL_DESCRIPTORS = 0;
  public static final int SPECIFIED_DESCRIPTORS = 1;
  public static final int NOT_SPECIFIED_DESCRIPTORS = 2;
  public static final String[] VECTOR_DIMENSIONS_OPTIONS = {
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
    MinClusterSize = 0;
    MaxDescriptorCoverage = 0.0;
    MinDescriptorDominance = 0.0;
    VectorDimensions = ALL_DESCRIPTORS;
    DescriptorsScopeNotesContain = null;
    IgnoreFirstResultFileLine = false;
    IgnoreEmptyClusters = false;
    RankClustersByQuality = false;
    LaunchHtmlBrowser = false;
    LaunchClusterLabelEditor = false;
    DumpDocumentsForVisualization = false;
    DominantDescriptorThreshold = 0.0;
    RareDescriptorThreshold = 0.0;
    FrequentNonDescriptorThreshold = 0.0;
    MaxNumberOfOutputTextUnits = 0;
    IgnoreTextUnitsInOutlierCluster = false;
    OutlierClusterID = -1;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public MonitorClusterQualityParameter(String pCollectionFileName,
  String pThesaurusFileName, String pClusterResultFileName,
  String pClusterResultFileFormat, String pClusterDirectory,
  int pNumberOfClusters, int pIteration, int pMinClusterSize,
  double pMaxDescriptorCoverage, double pMinDescriptorDominance,
  String pVectorDimensions, String pDescriptorsScopeNotesContain,
  String pClusterLabelFileName, boolean pIgnoreFirstResultFileLine,
  boolean pIgnoreEmptyClusters, boolean pRankClustersByQuality,
  boolean pLaunchHtmlBrowser, boolean pLaunchClusterLabelEditor, 
  boolean pDumpDocumentsForVisualization, double pDominantDescriptorThreshold,
  double pRareDescriptorThreshold, double pFrequentNonDescriptorThreshold,
  int pMaxNumberOfOutputTextUnits, boolean pIgnoreTextUnitsInOutlierCluster,
  int pOutlierClusterID) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ThesaurusFileName = pThesaurusFileName;
    ClusterResultFileName = pClusterResultFileName;
    ClusterDirectory = pClusterDirectory;
    NumberOfClusters = pNumberOfClusters;
    Iteration = pIteration;
    MinClusterSize = pMinClusterSize;
    MaxDescriptorCoverage = pMaxDescriptorCoverage;
    MinDescriptorDominance = pMinDescriptorDominance;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    ClusterLabelFileName = pClusterLabelFileName;
    IgnoreFirstResultFileLine = pIgnoreFirstResultFileLine;
    IgnoreEmptyClusters = pIgnoreEmptyClusters;
    RankClustersByQuality = pRankClustersByQuality;
    LaunchHtmlBrowser = pLaunchHtmlBrowser;
    LaunchClusterLabelEditor = pLaunchClusterLabelEditor;
    DumpDocumentsForVisualization = pDumpDocumentsForVisualization;
    DominantDescriptorThreshold = pDominantDescriptorThreshold;
    RareDescriptorThreshold = pRareDescriptorThreshold;
    FrequentNonDescriptorThreshold = pFrequentNonDescriptorThreshold;
    MaxNumberOfOutputTextUnits = pMaxNumberOfOutputTextUnits;
    IgnoreTextUnitsInOutlierCluster = pIgnoreTextUnitsInOutlierCluster;
    OutlierClusterID = pOutlierClusterID;
    
    if (pVectorDimensions.equals(VECTOR_DIMENSIONS_OPTIONS[ALL_DESCRIPTORS])) {
      VectorDimensions = ALL_DESCRIPTORS;
    }
    else if (pVectorDimensions.equals(VECTOR_DIMENSIONS_OPTIONS[
    SPECIFIED_DESCRIPTORS])) {
      VectorDimensions = SPECIFIED_DESCRIPTORS;
    }
    else {
      VectorDimensions = NOT_SPECIFIED_DESCRIPTORS;
    }
    
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
  
  public MonitorClusterQualityParameter(String pCollectionFileName,
  String pThesaurusFileName, String pClusterResultFileName,
  int pClusterResultFileFormat, String pClusterDirectory,
  int pNumberOfClusters, int pIteration, int pMinClusterSize,
  double pMaxDescriptorCoverage, double pMinDescriptorDominance,
  int pVectorDimensions, String pDescriptorsScopeNotesContain,
  String pClusterLabelFileName, boolean pIgnoreFirstResultFileLine,
  boolean pIgnoreEmptyClusters, boolean pRankClustersByQuality,
  boolean pLaunchHtmlBrowser, boolean pLaunchClusterLabelEditor,
  boolean pDumpDocumentsForVisualization, double pDominantDescriptorThreshold,
  double pRareDescriptorThreshold, double pFrequentNonDescriptorThreshold,
  int pMaxNumberOfOutputTextUnits, boolean pIgnoreTextUnitsInOutlierCluster,
  int pOutlierClusterID) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    ThesaurusFileName = pThesaurusFileName;
    ClusterResultFileName = pClusterResultFileName;
    ClusterResultFileFormat = pClusterResultFileFormat;
    ClusterDirectory = pClusterDirectory;
    NumberOfClusters = pNumberOfClusters;
    Iteration = pIteration;
    MinClusterSize = pMinClusterSize;
    MaxDescriptorCoverage = pMaxDescriptorCoverage;
    MinDescriptorDominance = pMinDescriptorDominance;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    VectorDimensions = pVectorDimensions;
    ClusterLabelFileName = pClusterLabelFileName;
    IgnoreFirstResultFileLine = pIgnoreFirstResultFileLine;
    RankClustersByQuality = pRankClustersByQuality;
    IgnoreEmptyClusters = pIgnoreEmptyClusters;
    LaunchHtmlBrowser = pLaunchHtmlBrowser;
    LaunchClusterLabelEditor = pLaunchClusterLabelEditor;
    DumpDocumentsForVisualization = pDumpDocumentsForVisualization;
    DominantDescriptorThreshold = pDominantDescriptorThreshold;
    RareDescriptorThreshold = pRareDescriptorThreshold;
    FrequentNonDescriptorThreshold = pFrequentNonDescriptorThreshold;
    MaxNumberOfOutputTextUnits = pMaxNumberOfOutputTextUnits;
    IgnoreTextUnitsInOutlierCluster = pIgnoreTextUnitsInOutlierCluster;
    OutlierClusterID = pOutlierClusterID;
    
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
  public int getMinClusterSize() {
    return MinClusterSize; }
  public double getMaxDescriptorCoverage() {
    return MaxDescriptorCoverage; }
  public double getMinDescriptorDominance() {
    return MinDescriptorDominance; }
  public int getVectorDimensions() {
    return VectorDimensions; }
  public String getDescriptorsScopeNotesContain() {
    return DescriptorsScopeNotesContain; }
  public boolean ignoreFirstResultFileLine() {
    return IgnoreFirstResultFileLine; }
  public boolean ignoreEmptyClusters() {
    return IgnoreEmptyClusters; }
  public boolean rankClustersByQuality() {
    return RankClustersByQuality; }
  public boolean launchHtmlBrowser() {
    return LaunchHtmlBrowser; }
  public boolean launchClusterLabelEditor() {
    return LaunchClusterLabelEditor; }
  public boolean dumpDocumentsForVisualization() {
    return DumpDocumentsForVisualization; }
  public double getDominantDescriptorThreshold() {
    return DominantDescriptorThreshold; }
  public double getRareDescriptorThreshold() {
    return RareDescriptorThreshold; }
  public double getFrequentNonDescriptorThreshold() {
    return FrequentNonDescriptorThreshold; }
  public int getMaxNumberOfOutputTextUnits() {
    return MaxNumberOfOutputTextUnits; }
  public boolean ignoreTextUnitsInOutlierCluster() {
    return IgnoreTextUnitsInOutlierCluster; }
 public int getOutlierClusterID() {
    return OutlierClusterID; }
  
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
  public void setRankClustersByQuality(boolean pRankClustersByQuality) {
    RankClustersByQuality = pRankClustersByQuality; }
  public void setLaunchHtmlBrowser(boolean pLaunchHtmlBrowser) {
    LaunchHtmlBrowser = pLaunchHtmlBrowser; }
  public void setLaunchClusterLabelEditor(boolean pLaunchClusterLabelEditor) {
    LaunchClusterLabelEditor = pLaunchClusterLabelEditor; }
  public void setDumpDocumentsForVisualization(
  boolean pDumpDocumentsForVisualization) {
    DumpDocumentsForVisualization = pDumpDocumentsForVisualization; }
  public void setIgnoreTextUnitsInOutlierCluster(
  boolean pIgnoreTextUnitsInOutlierCluster) {
    IgnoreTextUnitsInOutlierCluster = pIgnoreTextUnitsInOutlierCluster; }
  
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
    ParameterAttributes.put(MIN_CLUSTER_SIZE,
    Tools.int2String(MinClusterSize));
    ParameterAttributes.put(MAX_DESCRIPTOR_COVERAGE,
    Tools.double2String(MaxDescriptorCoverage));
    ParameterAttributes.put(MIN_FREQUENT_DESCRIPTORS_RATIO,
    Tools.double2String(MinDescriptorDominance));
    ParameterAttributes.put(VECTOR_DIMENSIONS,
    Tools.int2String(VectorDimensions));
    ParameterAttributes.put(DESCRIPTOR_SCOPE_NOTES_CONTAIN,
    DescriptorsScopeNotesContain);
    ParameterAttributes.put(IGNORE_FIRST_RESULT_FILE_LINE,
    Tools.boolean2String(IgnoreFirstResultFileLine));
    ParameterAttributes.put(IGNORE_EMPTY_CLUSTERS,
    Tools.boolean2String(IgnoreEmptyClusters));
    ParameterAttributes.put(RANK_CLUSTERS_BY_QUALITY,
    Tools.boolean2String(RankClustersByQuality));
    ParameterAttributes.put(LAUNCH_HTML_BROWSER,
    Tools.boolean2String(LaunchHtmlBrowser));
    ParameterAttributes.put(LAUNCH_CLUSTER_LABEL_EDITOR,
    Tools.boolean2String(LaunchClusterLabelEditor));
    ParameterAttributes.put(DUMP_DOCUMENTS_FOR_VISUALIZATION,
    Tools.boolean2String(DumpDocumentsForVisualization));
    ParameterAttributes.put(DOMINANT_DESCRIPTOR_THRESHOLD,
    Tools.double2String(DominantDescriptorThreshold));
    ParameterAttributes.put(RARE_DESCRIPTOR_THRESHOLD,
    Tools.double2String(RareDescriptorThreshold));
    ParameterAttributes.put(FREQUENT_NONDESCRIPTOR_THRESHOLD,
    Tools.double2String(FrequentNonDescriptorThreshold));
    ParameterAttributes.put(MAX_NUMBER_OF_OUTPUT_TEXT_UNITS,
    Tools.int2String(MaxNumberOfOutputTextUnits));
    ParameterAttributes.put(IGNORE_TEXT_UNITS_IN_OUTLIER_CLUSTER,
    Tools.boolean2String(IgnoreTextUnitsInOutlierCluster));
    ParameterAttributes.put(OUTLIER_CLUSTER_ID,
    Tools.int2String(OutlierClusterID));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  Element pParameterAttributes) {
    
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
    MinClusterSize = Tools.string2Int(
    (String)ParameterAttributes.get(MIN_CLUSTER_SIZE));
    MaxDescriptorCoverage = Tools.string2Double(
    (String)ParameterAttributes.get(MAX_DESCRIPTOR_COVERAGE));
    MinDescriptorDominance = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_FREQUENT_DESCRIPTORS_RATIO));
    VectorDimensions = Tools.string2Int(
    (String)ParameterAttributes.get(VECTOR_DIMENSIONS));
    DescriptorsScopeNotesContain = (String)ParameterAttributes
    .get(DESCRIPTOR_SCOPE_NOTES_CONTAIN);
    IgnoreFirstResultFileLine = Tools.string2Boolean(
    (String)ParameterAttributes.get(IGNORE_FIRST_RESULT_FILE_LINE));
    IgnoreEmptyClusters = Tools.string2Boolean(
    (String)ParameterAttributes.get(IGNORE_EMPTY_CLUSTERS));
    RankClustersByQuality = Tools.string2Boolean(
    (String)ParameterAttributes.get(RANK_CLUSTERS_BY_QUALITY));
    LaunchHtmlBrowser = Tools.string2Boolean(
    (String)ParameterAttributes.get(LAUNCH_HTML_BROWSER));
    LaunchClusterLabelEditor = Tools.string2Boolean(
    (String)ParameterAttributes.get(LAUNCH_CLUSTER_LABEL_EDITOR));
    DumpDocumentsForVisualization = Tools.string2Boolean(
    (String)ParameterAttributes.get(DUMP_DOCUMENTS_FOR_VISUALIZATION));
    DominantDescriptorThreshold = Tools.string2Double(
    (String)ParameterAttributes.get(DOMINANT_DESCRIPTOR_THRESHOLD));
    RareDescriptorThreshold = Tools.string2Double(
    (String)ParameterAttributes.get(RARE_DESCRIPTOR_THRESHOLD));
    FrequentNonDescriptorThreshold = Tools.string2Double(
    (String)ParameterAttributes.get(FREQUENT_NONDESCRIPTOR_THRESHOLD));
    MaxNumberOfOutputTextUnits = Tools.string2Int(
    (String)ParameterAttributes.get(MAX_NUMBER_OF_OUTPUT_TEXT_UNITS));
    IgnoreTextUnitsInOutlierCluster = Tools.string2Boolean(
    (String)ParameterAttributes.get(IGNORE_TEXT_UNITS_IN_OUTLIER_CLUSTER));
    OutlierClusterID = Tools.string2Int(
    (String)ParameterAttributes.get(OUTLIER_CLUSTER_ID));
    
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
  
  public static void main(String pOtions[]) {}
  
}