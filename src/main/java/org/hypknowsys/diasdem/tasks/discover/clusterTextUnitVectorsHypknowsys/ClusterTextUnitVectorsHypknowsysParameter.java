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

package org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys;

import java.util.TreeMap;
import org.hypknowsys.diasdem.server.DiasdemScriptableTaskParameter;
import org.hypknowsys.misc.util.Tools;
import org.jdom.Element;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ClusterTextUnitVectorsHypknowsysParameter
extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String InputVectorsFileName = null;
  protected String OutputVectorsFileName = null;
  protected String ClusterModelFileName = null;
  protected int ClusteringAlgorithm = HYPKNOWSYS_SIMPLE_KMEANS;
  protected int ClusteringMode = CLUSTERING_PHASE;
  protected int DistanceMeasure = HYPKNOWSYS_EUCLIDEAN_DISTANCE;
  protected boolean ClusterValidityAssessment = false;
  protected boolean VerboseMode = false;
  protected String HtmlReportFileName = null;
  protected String NumberOfClusters = null;
  protected boolean LaunchHtmlBrowser = false;
  protected String RandomNumberSeed = null;
  protected String MaxIterations = null;
  protected String MinClusterCardinality = null;
  protected String MaxRetriesPerBisectingPass = null;
  protected String NumberOfRows = null;
  protected String NumberOfColumns = null;
  protected String LatticeType = null;
  protected String NeighborhoodRadii = null;
  protected boolean DrawRandomSample = false;
  protected boolean SequentialAccess = false;
  protected String RandomSampleSize = null;
  protected String SizeOfNearestNeighborsList = null;
  protected boolean CompactifyTrainingInstances = false;
  protected String NumberOfSharedNearestNeighbors = null;
  protected String StrongLinkThreshold = null;
  protected String LabelingThreshold = null;
  protected String MergeThreshold = null;
  protected String NoiseThreshold = null;
  protected String TopicThreshold = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys"
  + ".ClusterTextUnitVectorsHypknowsysTask";
  private static final String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys"
  + ".ClusterTextUnitVectorsHypknowsysParameterPanel";
  
  private static final String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private static final String INPUT_VECTORS_FILE_NAME =
  "InputVectorsFileName";
  private static final String OUTPUT_VECTORS_FILE_NAME =
  "OutputVectorsFileName";
  private static final String CLUSTER_MODEL_FILE_NAME =
  "ClusterModelFileName";
  private static final String CLUSTERING_ALGORITHM =
  "ClusteringAlgorithm";
  private static final String CLUSTERING_MODE =
  "ClusteringMode";
  private static final String DISTANCE_MEASURE =
  "DistanceMeasure";
  private static final String CLUSTER_VALIDITY_ASSESSMENT =
  "ClusterValidityAssessment";
  private static final String VERBOSE_MODE =
  "VerboseMode";
  private static final String HTML_REPORT_FILE_NAME =
  "HtmlReportFileName";
  private static final String NUMBER_OF_CLUSTERS =
  "NumberOfClusters";
  private static final String LAUNCH_HTML_BROWSER =
  "LaunchHtmlBrowser";
  private static final String RANDOM_NUMBER_SEED =
  "RandomNumberSeed";
  private static final String MAX_ITERATIONS =
  "MaxIterations";
  private static final String MIN_CLUSTER_CARDINALITY =
  "MinClusterCardinality";
  private static final String MAX_RETRIES_PER_BISECTING_PASS =
  "MaxRetriesPerBisectingPass";
  private static final String NUMBER_OF_ROWS =
  "NumberOfRows";
  private static final String NUMBER_OF_COLUMNS =
  "NumberOfColumns";
  private static final String LATTICE_TYPE =
  "LatticeType";
  private static final String NEIGHBORHOOD_RADII =
  "NeighborhoodRadii";
  private static final String DRAW_RANDOM_SAMPLE =
  "DrawRandomSample";
  private static final String SEQUENTIAL_ACCESS =
  "SequentialAccess";
  private static final String RANDOM_SAMPLE_SIZE =
  "RandomSampleSize";
  private static final String SIZE_OF_NEAREST_NEIGHBORS_LIST =
  "SizeOfNearestNeighborsList";
  private static final String COMPACTIFY_TRAINING_INSTANCES =
  "CompactifyTrainingInstances";
  private static final String NUMBER_OF_SHARED_NEAREST_NEIGHBORS =
  "NumberOfSharedNearestNeighbors";
  private static final String STRONG_LINK_THRESHOLD =
  "StrongLinkThreshold";
  private static final String LABELING_THRESHOLD =
  "LabelingThreshold";
  private static final String MERGE_THRESHOLD =
  "MergeThreshold";
  private static final String NOISE_THRESHOLD =
  "NoiseThreshold";
  private static final String TOPIC_THRESHOLD =
  "TopicThreshold";
  
  public static final int HYPKNOWSYS_SIMPLE_KMEANS = 0;
  public static final int HYPKNOWSYS_BISECTING_KMEANS = 1;
  public static final int HYPKNOWSYS_BATCH_SOM = 2;
  public static final int HYPKNOWSYS_JARVIS_PATRICK_SNN = 3;
  public static final int HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN = 4;
  public static final String[] CLUSTERING_ALGORITHMS = {
    "hypKNOWsys Clusterers: SimpleKMeans",
    "hypKNOWsys Clusterers: BisectingKMeans",
    "hypKNOWsys Clusterers: BatchSom",
    "hypKNOWsys Clusterers: JarvisPatrickSnn",
    "hypKNOWsys Clusterers: ErtozSteinbachKumarTopicsSnn"
  };
  
  public static final int HYPKNOWSYS_EUCLIDEAN_DISTANCE = 0;
  public static final int HYPKNOWSYS_COSINE_DISTANCE = 1;
  public static final int HYPKNOWSYS_EXTENDED_JACCARD_DISTANCE = 2;
  public static final int HYPKNOWSYS_EXTENDED_DICE_DISTANCE = 3;
  public static final int HYPKNOWSYS_NOT_APPLICABLE = 4;
  public static final String[] DISTANCE_MEASURES = {
    "hypKNOWsys Proximity: EuclideanDistance",
    "hypKNOWsys Proximity: CosineDistance",
    "hypKNOWsys Proximity: ExtendedJaccardDistance",
    "hypKNOWsys Proximity: ExtendedDiceDistance",
    "Not Applicable in Application Phase"
  };
  public static final String[] HYPKNOWSYS_DISTANCE_MEASURES_OPTIONS = {
    "euclidean",
    "cosine",
    "extendedJaccard",
    "extendedDice",
    "notApplicable"
  };
  
  public static final int CLUSTERING_PHASE = 0;
  public static final int APPLICATION_PHASE = 1;
  public static final String[] CLUSTERING_MODES = {
    "Clustering Phase (Create New Clustering Model)",
    "Application Phase (Apply Existing Clustering Model)"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsHypknowsysParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    InputVectorsFileName = null;
    OutputVectorsFileName = null;
    ClusterModelFileName = null;
    ClusteringAlgorithm = HYPKNOWSYS_SIMPLE_KMEANS;
    ClusteringMode = CLUSTERING_PHASE;
    DistanceMeasure = HYPKNOWSYS_EUCLIDEAN_DISTANCE;
    ClusterValidityAssessment = false;
    VerboseMode = false;
    HtmlReportFileName = null;
    NumberOfClusters = null;
    LaunchHtmlBrowser = false;
    RandomNumberSeed = null;
    MaxIterations = null;
    MinClusterCardinality = null;
    MaxRetriesPerBisectingPass = null;
    NumberOfRows = null;
    NumberOfColumns = null;
    LatticeType = null;
    NeighborhoodRadii = null;
    DrawRandomSample = false;
    SequentialAccess = false;
    RandomSampleSize = null;
    SizeOfNearestNeighborsList = null;
    CompactifyTrainingInstances = false;
    NumberOfSharedNearestNeighbors = null;
    StrongLinkThreshold = null;
    LabelingThreshold = null;
    MergeThreshold = null;
    NoiseThreshold = null;
    TopicThreshold = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsHypknowsysParameter(String pCollectionFileName,
  String pInputVectorsFileName, String pOutputVectorsFileName,
  String pClusterModelFileName, String pClusteringAlgorithm,
  String pClusteringMode, String pDistanceMeasure,
  boolean pClusterValidityAssessment, boolean pVerboseMode,
  String pHtmlReportFileName, String pNumberOfClusters,
  boolean pLaunchHtmlBrowser, String pRandomNumberSeed,
  String pMaxKMeansIterations, String pMinClusterCardinality,
  String pMaxRetriesPerBisectingPass, String pNumberOfRows,
  String pNumberOfColumns, String pLatticeType, String pNeighborhoodRadii,
  boolean pDrawRandomSample, boolean pSequentialAccess,
  String pRandomSampleSize, String pSizeOfNearestNeighborsList,
  String pNumberOfSharedNearestNeighbors, String pStrongLinkThreshold,
  String pLabelingThreshold, String pMergeThreshold,
  String pNoiseThreshold, String pTopicThreshold) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    InputVectorsFileName = pInputVectorsFileName;
    OutputVectorsFileName = pOutputVectorsFileName;
    ClusterModelFileName = pClusterModelFileName;
    ClusterValidityAssessment = pClusterValidityAssessment;
    VerboseMode = pVerboseMode;
    HtmlReportFileName = pHtmlReportFileName;
    NumberOfClusters = pNumberOfClusters;
    LaunchHtmlBrowser = pLaunchHtmlBrowser;
    RandomNumberSeed = pRandomNumberSeed;
    MaxIterations = pMaxKMeansIterations;
    MinClusterCardinality = pMinClusterCardinality;
    MaxRetriesPerBisectingPass = pMaxRetriesPerBisectingPass;
    LatticeType = pLatticeType;
    NeighborhoodRadii = pNeighborhoodRadii;
    NumberOfRows = pNumberOfRows;
    NumberOfColumns = pNumberOfColumns;
    DrawRandomSample = pDrawRandomSample;
    RandomSampleSize = pRandomSampleSize;
    SequentialAccess = pSequentialAccess;
    SizeOfNearestNeighborsList = pSizeOfNearestNeighborsList;
    NumberOfSharedNearestNeighbors = pNumberOfSharedNearestNeighbors;
    StrongLinkThreshold = pStrongLinkThreshold;
    LabelingThreshold = pLabelingThreshold;
    MergeThreshold = pMergeThreshold;
    NoiseThreshold = pNoiseThreshold;
    TopicThreshold = pTopicThreshold;
    
    for (int i = 0; i < CLUSTERING_ALGORITHMS.length; i++) {
      if (CLUSTERING_ALGORITHMS[i].equals(pClusteringAlgorithm)) {
        ClusteringAlgorithm = i;
        break;
      }
    }
    for (int i = 0; i < CLUSTERING_MODES.length; i++) {
      if (CLUSTERING_MODES[i].equals(pClusteringMode)) {
        ClusteringMode = i;
        break;
      }
    }
    for (int i = 0; i < DISTANCE_MEASURES.length; i++) {
      if (DISTANCE_MEASURES[i].equals(pDistanceMeasure)) {
        DistanceMeasure = i;
        break;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsHypknowsysParameter(String pCollectionFileName,
  String pInputVectorsFileName, String pOutputVectorsFileName,
  String pClusterModelFileName, int pClusteringAlgorithm, int pClusteringMode,
  int pDistanceMeasure, boolean pClusterValidityAssessment,
  boolean pVerboseMode, String pHtmlReportFileName, String pNumberOfClusters,
  boolean pLaunchHtmlBrowser, String pRandomNumberSeed,
  String pMaxKMeansIterations, String pMinClusterCardinality,
  String pMaxRetriesPerBisectingPass, String pNumberOfRows,
  String pNumberOfColumns, String pLatticeType, String pNeighborhoodRadii,
  boolean pDrawRandomSample, boolean pSequentialAccess,
  String pRandomSampleSize, String pSizeOfNearestNeighborsList,
  String pNumberOfSharedNearestNeighbors, String pStrongLinkThreshold,
  String pLabelingThreshold, String pMergeThreshold,
  String pNoiseThreshold, String pTopicThreshold) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    InputVectorsFileName = pInputVectorsFileName;
    OutputVectorsFileName = pOutputVectorsFileName;
    ClusterModelFileName = pClusterModelFileName;
    ClusteringAlgorithm = pClusteringAlgorithm;
    ClusteringMode = pClusteringMode;
    DistanceMeasure = pDistanceMeasure;
    ClusterValidityAssessment = pClusterValidityAssessment;
    VerboseMode = pVerboseMode;
    HtmlReportFileName = pHtmlReportFileName;
    NumberOfClusters = pNumberOfClusters;
    LaunchHtmlBrowser = pLaunchHtmlBrowser;
    RandomNumberSeed = pRandomNumberSeed;
    MaxIterations = pMaxKMeansIterations;
    MinClusterCardinality = pMinClusterCardinality;
    MaxRetriesPerBisectingPass = pMaxRetriesPerBisectingPass;
    LatticeType = pLatticeType;
    NeighborhoodRadii = pNeighborhoodRadii;
    NumberOfRows = pNumberOfRows;
    NumberOfColumns = pNumberOfColumns;
    DrawRandomSample = pDrawRandomSample;
    SequentialAccess = pSequentialAccess;
    RandomSampleSize = pRandomSampleSize;
    SizeOfNearestNeighborsList = pSizeOfNearestNeighborsList;
    NumberOfSharedNearestNeighbors = pNumberOfSharedNearestNeighbors;
    StrongLinkThreshold = pStrongLinkThreshold;
    LabelingThreshold = pLabelingThreshold;
    MergeThreshold = pMergeThreshold;
    NoiseThreshold = pNoiseThreshold;
    TopicThreshold = pTopicThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getInputVectorsFileName() {
    return InputVectorsFileName; }
  public String getOutputVectorsFileName() {
    return OutputVectorsFileName; }
  public String getClusterModelFileName() {
    return ClusterModelFileName; }
  public int getClusteringAlgorithm() {
    return ClusteringAlgorithm; }
  public int getClusteringMode() {
    return ClusteringMode; }
  public int getDistanceMeasure() {
    return DistanceMeasure; }
  public boolean getClusterValidityAssessment() {
    return ClusterValidityAssessment; }
  public boolean getVerboseMode() {
    return VerboseMode; }
  public String getHtmlReportFileName() {
    return HtmlReportFileName; }
  public String getNumberOfClusters() {
    return NumberOfClusters; }
  public boolean launchHtmlBrowser() {
    return LaunchHtmlBrowser; }
  public String getRandomNumberSeed() {
    return RandomNumberSeed; }
  public String getMaxIterations() {
    return MaxIterations; }
  public String getMinClusterCardinality() {
    return MinClusterCardinality; }
  public String getMaxRetriesPerBisectingPass() {
    return MaxRetriesPerBisectingPass; }
  public String getNumberOfRows() {
    return NumberOfRows; }
  public String getNumberOfColumns() {
    return NumberOfColumns; }
  public String getLatticeType() {
    return LatticeType; }
  public String getNeighborhoodRadii() {
    return NeighborhoodRadii; }
  public boolean drawRandomSample() {
    return DrawRandomSample; }
  public boolean sequentialAccess() {
    return SequentialAccess; }
  public String getRandomSampleSize() {
    return RandomSampleSize; }
  public String getSizeOfNearestNeighborsList() {
    return SizeOfNearestNeighborsList; }
  public boolean getCompactifyTrainingInstances() {
    return CompactifyTrainingInstances; }
  public String getNumberOfSharedNearestNeighbors() {
    return NumberOfSharedNearestNeighbors; }
  public String getStrongLinkThreshold() {
    return StrongLinkThreshold; }
  public String getLabelingThreshold() {
    return LabelingThreshold; }
  public String getMergeThreshold() {
    return MergeThreshold; }
  public String getNoiseThreshold() {
    return NoiseThreshold; }
  public String getTopicThreshold() {
    return TopicThreshold; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setClusterValidityAssessment(boolean pClusterValidityAssessment) {
    ClusterValidityAssessment = pClusterValidityAssessment; }
  public void setVerboseMode(boolean pVerboseMode) {
    VerboseMode = pVerboseMode; }
  public void setLaunchHtmlBrowser(boolean pLaunchHtmlBrowser) {
    LaunchHtmlBrowser = pLaunchHtmlBrowser; }
  public void setDrawRandomSample(boolean pDrawRandomSample) {
    DrawRandomSample = pDrawRandomSample; }
  public void setSequentialAccess(boolean pSequentialAccess) {
    SequentialAccess = pSequentialAccess; }
  public void setCompactifyTrainingInstances(
  boolean pCompactifyTrainingInstances) {
    CompactifyTrainingInstances = pCompactifyTrainingInstances; }
  
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
    ParameterAttributes.put(INPUT_VECTORS_FILE_NAME, InputVectorsFileName);
    ParameterAttributes.put(OUTPUT_VECTORS_FILE_NAME, OutputVectorsFileName);
    ParameterAttributes.put(CLUSTER_MODEL_FILE_NAME, ClusterModelFileName);
    ParameterAttributes.put(CLUSTERING_ALGORITHM,
    Tools.int2String(ClusteringAlgorithm));
    ParameterAttributes.put(CLUSTERING_MODE,
    Tools.int2String(ClusteringMode));
    ParameterAttributes.put(DISTANCE_MEASURE,
    Tools.int2String(DistanceMeasure));
    ParameterAttributes.put(CLUSTER_VALIDITY_ASSESSMENT,
    Tools.boolean2String(ClusterValidityAssessment));
    ParameterAttributes.put(VERBOSE_MODE,
    Tools.boolean2String(VerboseMode));
    ParameterAttributes.put(HTML_REPORT_FILE_NAME, HtmlReportFileName);
    ParameterAttributes.put(NUMBER_OF_CLUSTERS, NumberOfClusters);
    ParameterAttributes.put(LAUNCH_HTML_BROWSER,
    Tools.boolean2String(LaunchHtmlBrowser));
    ParameterAttributes.put(RANDOM_NUMBER_SEED, RandomNumberSeed);
    ParameterAttributes.put(MAX_ITERATIONS, MaxIterations);
    ParameterAttributes.put(MIN_CLUSTER_CARDINALITY, MinClusterCardinality);
    ParameterAttributes.put(MAX_RETRIES_PER_BISECTING_PASS,
    MaxRetriesPerBisectingPass);
    ParameterAttributes.put(NUMBER_OF_ROWS, NumberOfRows);
    ParameterAttributes.put(NUMBER_OF_COLUMNS, NumberOfColumns);
    ParameterAttributes.put(LATTICE_TYPE, LatticeType);
    ParameterAttributes.put(NEIGHBORHOOD_RADII, NeighborhoodRadii);
    ParameterAttributes.put(DRAW_RANDOM_SAMPLE,
    Tools.boolean2String(DrawRandomSample));
    ParameterAttributes.put(SEQUENTIAL_ACCESS,
    Tools.boolean2String(SequentialAccess));
    ParameterAttributes.put(RANDOM_SAMPLE_SIZE, RandomSampleSize);
    ParameterAttributes.put(SIZE_OF_NEAREST_NEIGHBORS_LIST,
    SizeOfNearestNeighborsList);
    ParameterAttributes.put(COMPACTIFY_TRAINING_INSTANCES,
    Tools.boolean2String(CompactifyTrainingInstances));
    ParameterAttributes.put(NUMBER_OF_SHARED_NEAREST_NEIGHBORS,
    NumberOfSharedNearestNeighbors);
    ParameterAttributes.put(STRONG_LINK_THRESHOLD, StrongLinkThreshold);
    ParameterAttributes.put(LABELING_THRESHOLD, LabelingThreshold);
    ParameterAttributes.put(MERGE_THRESHOLD, MergeThreshold);
    ParameterAttributes.put(NOISE_THRESHOLD, NoiseThreshold);
    ParameterAttributes.put(TOPIC_THRESHOLD, TopicThreshold);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    InputVectorsFileName = (String)ParameterAttributes
    .get(INPUT_VECTORS_FILE_NAME);
    OutputVectorsFileName = (String)ParameterAttributes
    .get(OUTPUT_VECTORS_FILE_NAME);
    ClusterModelFileName = (String)ParameterAttributes
    .get(CLUSTER_MODEL_FILE_NAME);
    ClusteringAlgorithm = Tools.string2Int((String)ParameterAttributes
    .get(CLUSTERING_ALGORITHM));
    ClusteringMode = Tools.string2Int(
    (String)ParameterAttributes.get(CLUSTERING_MODE));
    DistanceMeasure = Tools.string2Int(
    (String)ParameterAttributes.get(DISTANCE_MEASURE));
    ClusterValidityAssessment = Tools.string2Boolean(
    (String)ParameterAttributes.get(CLUSTER_VALIDITY_ASSESSMENT));
    VerboseMode = Tools.string2Boolean(
    (String)ParameterAttributes.get(VERBOSE_MODE));
    HtmlReportFileName = (String)ParameterAttributes
    .get(HTML_REPORT_FILE_NAME);
    NumberOfClusters = (String)ParameterAttributes
    .get(NUMBER_OF_CLUSTERS);
    LaunchHtmlBrowser = Tools.string2Boolean((String)ParameterAttributes
    .get(LAUNCH_HTML_BROWSER));
    RandomNumberSeed = (String)ParameterAttributes
    .get(RANDOM_NUMBER_SEED);
    MaxIterations = (String)ParameterAttributes
    .get(MAX_ITERATIONS);
    MinClusterCardinality = (String)ParameterAttributes
    .get(MIN_CLUSTER_CARDINALITY);
    MaxRetriesPerBisectingPass = (String)ParameterAttributes
    .get(MAX_RETRIES_PER_BISECTING_PASS);
    NumberOfRows = (String)ParameterAttributes
    .get(NUMBER_OF_ROWS);
    NumberOfColumns = (String)ParameterAttributes
    .get(NUMBER_OF_COLUMNS);
    LatticeType = (String)ParameterAttributes
    .get(LATTICE_TYPE);
    NeighborhoodRadii = (String)ParameterAttributes
    .get(NEIGHBORHOOD_RADII);
    DrawRandomSample = Tools.string2Boolean((String)ParameterAttributes
    .get(DRAW_RANDOM_SAMPLE));
    SequentialAccess = Tools.string2Boolean((String)ParameterAttributes
    .get(SEQUENTIAL_ACCESS));
    RandomSampleSize = (String)ParameterAttributes
    .get(RANDOM_SAMPLE_SIZE);
    SizeOfNearestNeighborsList = (String)ParameterAttributes
    .get(SIZE_OF_NEAREST_NEIGHBORS_LIST);
    CompactifyTrainingInstances = Tools.string2Boolean(
    (String)ParameterAttributes.get(COMPACTIFY_TRAINING_INSTANCES));
    NumberOfSharedNearestNeighbors = (String)ParameterAttributes
    .get(NUMBER_OF_SHARED_NEAREST_NEIGHBORS);
    StrongLinkThreshold = (String)ParameterAttributes
    .get(STRONG_LINK_THRESHOLD);
    LabelingThreshold = (String)ParameterAttributes
    .get(LABELING_THRESHOLD);
    MergeThreshold = (String)ParameterAttributes
    .get(MERGE_THRESHOLD);
    NoiseThreshold = (String)ParameterAttributes
    .get(NOISE_THRESHOLD);
    TopicThreshold = (String)ParameterAttributes
    .get(TOPIC_THRESHOLD);
    
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