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

package org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ClusterTextUnitVectorsWekaParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String InputVectorsFileName = null;
  protected String OutputVectorsFileName = null;
  protected String ClusterModelFileName = null;
  protected int ClusteringAlgorithm = WEKA_SIMPLE_KMEANS;
  protected int ClusteringMode = CLUSTERING_PHASE;
  protected String NumberOfClusters = null;
  protected String Acuity = null;
  protected String Cutoff = null;
  protected String MaxIterations = null;
  protected String RandomNumberSeed = null;
  protected String MinStdDeviation = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka.ClusterTextUnitVectorsWekaTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka.ClusterTextUnitVectorsWekaParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String INPUT_VECTORS_FILE_NAME =
  "InputVectorsFileName";
  private final static String OUTPUT_VECTORS_FILE_NAME =
  "OutputVectorsFileName";
  private final static String CLUSTER_MODEL_FILE_NAME =
  "ClusterModelFileName";
  private final static String CLUSTERING_ALGORITHM =
  "ClusteringAlgorithm";
  private final static String CLUSTERING_MODE =
  "ClusteringMode";
  private final static String NUMBER_OF_CLUSTERS =
  "NumberOfClusters";
  private final static String ACUITY =
  "Acuity";
  private final static String CUTOFF =
  "Cutoff";
  private final static String MAX_ITERATIONS =
  "MaxIterations";
  private final static String RANDOM_NUMBER_SEED =
  "RandomNumberSeed";
  private final static String MIN_STD_DEVIATION =
  "MinStdDeviation";
  
  public final static int WEKA_SIMPLE_KMEANS = 0;
  public final static int WEKA_COBWEB = 1;
  public final static int WEKA_EM = 2;
  public final static String[] CLUSTERING_ALGORITHMS = {
    "weka.clusterers.SimpleKMeans",
    "weka.clusterers.Cobweb",
    "weka.clusterers.EM"
  };

  public final static int CLUSTERING_PHASE = 0;
  public final static int APPLICATION_PHASE = 1;
  public final static String[] CLUSTERING_MODES = {
    "Clustering Phase (Create New Clustering Model)",
    "Application Phase (Apply Existing Clustering Model)"
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsWekaParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    InputVectorsFileName = null;
    OutputVectorsFileName = null;
    ClusterModelFileName = null;
    ClusteringAlgorithm = WEKA_SIMPLE_KMEANS;
    ClusteringMode = CLUSTERING_PHASE;
    NumberOfClusters = null;
    Acuity = null;
    Cutoff = null;
    MaxIterations = null;
    RandomNumberSeed = null;
    MinStdDeviation = null;
      
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsWekaParameter(String pCollectionFileName, 
  String pInputVectorsFileName, String pOutputVectorsFileName, 
  String pClusterModelFileName, String pClusteringAlgorithm, 
  String pClusteringMode, String pNumberOfClusters, String pAcuity,
  String pCutoff, String pMaxIterations, String pRandomNumberSeed,
  String pMinStdDeviation) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    InputVectorsFileName = pInputVectorsFileName;
    OutputVectorsFileName = pOutputVectorsFileName;
    ClusterModelFileName = pClusterModelFileName;
    NumberOfClusters = pNumberOfClusters;
    Acuity = pAcuity;
    Cutoff = pCutoff;
    MaxIterations = pMaxIterations;
    RandomNumberSeed = pRandomNumberSeed;
    MinStdDeviation = pMinStdDeviation;

    for (int i = 0; i < CLUSTERING_ALGORITHMS.length; i++)
      if (CLUSTERING_ALGORITHMS[i].equals(pClusteringAlgorithm)) { 
        ClusteringAlgorithm = i;
        break;
      }
    for (int i = 0; i < CLUSTERING_MODES.length; i++)
      if (CLUSTERING_MODES[i].equals(pClusteringMode)) { 
        ClusteringMode = i;
        break;
      }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsWekaParameter(String pCollectionFileName, 
  String pInputVectorsFileName, String pOutputVectorsFileName, 
  String pClusterModelFileName, int pClusteringAlgorithm, 
  int pClusteringMode, String pNumberOfClusters, String pAcuity,
  String pCutoff, String pMaxIterations, String pRandomNumberSeed,
  String pMinStdDeviation) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    InputVectorsFileName = pInputVectorsFileName;
    OutputVectorsFileName = pOutputVectorsFileName;
    ClusterModelFileName = pClusterModelFileName;
    ClusteringAlgorithm = pClusteringAlgorithm;
    ClusteringMode = pClusteringMode;
    NumberOfClusters = pNumberOfClusters;
    Acuity = pAcuity;
    Cutoff = pCutoff;
    MaxIterations = pMaxIterations;
    RandomNumberSeed = pRandomNumberSeed;
    MinStdDeviation = pMinStdDeviation;
    
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
  public String getNumberOfClusters() { 
    return NumberOfClusters; }
  public String getAcuity() { 
    return Acuity; }
  public String getCutoff() { 
    return Cutoff; }
  public String getMaxIterations() { 
    return MaxIterations; }
  public String getRandomNumberSeed() { 
    return RandomNumberSeed; }
  public String getMinStdDeviation() {
    return MinStdDeviation; }
  
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
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(INPUT_VECTORS_FILE_NAME, InputVectorsFileName);
    ParameterAttributes.put(OUTPUT_VECTORS_FILE_NAME, OutputVectorsFileName);
    ParameterAttributes.put(CLUSTER_MODEL_FILE_NAME, ClusterModelFileName);
    ParameterAttributes.put(CLUSTERING_ALGORITHM, 
    Tools.int2String(ClusteringAlgorithm));
    ParameterAttributes.put(CLUSTERING_MODE, 
    Tools.int2String(ClusteringMode));
    ParameterAttributes.put(NUMBER_OF_CLUSTERS, NumberOfClusters);
    ParameterAttributes.put(ACUITY, Acuity);
    ParameterAttributes.put(CUTOFF, Cutoff);
    ParameterAttributes.put(MAX_ITERATIONS, MaxIterations);
    ParameterAttributes.put(RANDOM_NUMBER_SEED, RandomNumberSeed);
    ParameterAttributes.put(MIN_STD_DEVIATION, MinStdDeviation);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
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
    ClusteringAlgorithm = Tools.string2Int(
    (String)ParameterAttributes.get(CLUSTERING_ALGORITHM));
    ClusteringMode = Tools.string2Int(
    (String)ParameterAttributes.get(CLUSTERING_MODE));
    NumberOfClusters = (String)ParameterAttributes.get(NUMBER_OF_CLUSTERS);
    Acuity = (String)ParameterAttributes.get(ACUITY);
    Cutoff = (String)ParameterAttributes.get(CUTOFF);
    MaxIterations = (String)ParameterAttributes.get(MAX_ITERATIONS);
    RandomNumberSeed = (String)ParameterAttributes.get(RANDOM_NUMBER_SEED);
    MinStdDeviation = (String)ParameterAttributes.get(MIN_STD_DEVIATION);
    
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