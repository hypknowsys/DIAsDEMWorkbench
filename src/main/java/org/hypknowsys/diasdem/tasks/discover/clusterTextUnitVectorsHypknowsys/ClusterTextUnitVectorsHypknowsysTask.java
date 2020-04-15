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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;
import org.hypknowsys.algorithms.clusterers.AbstractKMeans;
import org.hypknowsys.algorithms.clusterers.BatchSom;
import org.hypknowsys.algorithms.clusterers.BisectingKMeans;
import org.hypknowsys.algorithms.clusterers.Clusterer;
import org.hypknowsys.algorithms.clusterers.ErtozSteinbachKumarTopicsSnn;
import org.hypknowsys.algorithms.clusterers.JarvisPatrickSnn;
import org.hypknowsys.algorithms.clusterers.SimpleKMeans;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.algorithms.core.SequentialInstanceReader;
import org.hypknowsys.algorithms.core.Utils;
import org.hypknowsys.algorithms.filters.Filter;
import org.hypknowsys.algorithms.filters.unsupervised.attribute.RemoveType;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar;
import org.hypknowsys.diasdem.client.gui.DIAsDEMguiPreferences;
import org.hypknowsys.diasdem.server.DiasdemScriptableNonBlockingTask;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.KProperty;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.Task;
import org.hypknowsys.server.TaskParameter;
import org.hypknowsys.server.TaskProgress;
import org.hypknowsys.server.TaskResult;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ClusterTextUnitVectorsHypknowsysTask
extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ClusterTextUnitVectorsHypknowsysParameter CastParameter = null;
  private TextFile HtmlReport = null;
  private int MaxClusterID = 0;
  private Clusterer MyClusterer = null;
  private Instances FilteredVectors = null;
  private Instances SampledVectors = null;
  private RemoveType RemoveStringFilter = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String LABEL =
  "Cluster Text Unit Vectors (hypKNOWsys)";
  private static final String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys"
  + ".ClusterTextUnitVectorsHypknowsysParameter";
  private static final String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys"
  + ".ClusterTextUnitVectorsHypknowsysResult";
  private static final String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys"
  + ".ClusterTextUnitVectorsHypknowsysControlPanel";
  
  private static final KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("DEFAULT_HYPKNOWSYS_CLUSTERER_FILE_NAME",
    "hypKNOWsys Clustering: Default Cluster File Name",
    "", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_DISTANCE_MEASURE_INDEX",
    "hypKNOWsys Clustering: Index of Default Distance Measure",
    "0", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_CLUSTER_VALIDITY_ASSESSMENT",
    "hypKNOWsys Clustering: Default Setting of Cluster Validity Assessment",
    "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_VERBOSE_MODE",
    "hypKNOWsys Clustering: Default Setting of Verbose Mode",
    "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_HTML_REPORT_FILE_NAME",
    "hypKNOWsys Clustering: Default HTML Report File Name",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_LAUNCH_HTML_BROWSER",
    "hypKNOWsys Clustering: Launch HTML Browser to View Report",
    "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_NUMBER_OF_CLUSTERS",
    "hypKNOWsys Clustering: Default Number of Clusters",
    "100", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_RANDOM_NUMBER_SEED",
    "hypKNOWsys Clustering: Default Random Number Seed",
    "12345", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_MAX_KMEANS_ITERATIONS",
    "hypKNOWsys Clustering: Default Max. K-Means Iterations",
    "20", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_MIN_CLUSTER_CARDINALITY",
    "hypKNOWsys Clustering: Default Min. Cluster Cardinality",
    "5", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_MAX_RETRIES_PER_BISECTING_PASS",
    "hypKNOWsys Clustering: Default Max. Retries per Bisecting Pass",
    "20", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_EVALUATE_INTERNAL_QUALITY",
    "hypKNOWsys Clustering: Default Option for Internal"
    + "Cluster Quality Evaluation ('true' or 'quick')",
    "quick", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_NUMBER_OF_ROWS",
    "hypKNOWsys Clustering: Default Option for Number of SOM Rows"
    + "(integer > 0)", "5", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_NUMBER_OF_COLUMNS",
    "hypKNOWsys Clustering: Default Option for Number of SOM Columns"
    + "(integer > 0)", "5", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_LATTICE_TYPE",
    "hypKNOWsys Clustering: Default Option for Lattice Type of SOM"
    + "array ('rectangular' or 'hexagonal')",
    "rectangular", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_NEIGHBORHOOD_RADII",
    "hypKNOWsys Clustering: Default Option for Neighborhood Radii"
    + "as Vector of Inteers (e.g., '[3,2,2,2,1,1,1,1,0]')",
    "[3,2,2,2,1,1,1,1,0]", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_DRAW_RANDOM_SAMPLE",
    "hypKNOWsys Clustering: Default Setting of Draw Random Sample",
    "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_RANDOM_SAMPLE_SIZE",
    "hypKNOWsys Clustering: Default Setting of Random Sample Size"
    + " (e.g., 1000 or 0.25)", "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_SIZE_OF_NEAREST_NEIGHBORS_LIST",
    "hypKNOWsys Clustering: Default Setting for Size of Nearest Neighbors"
    + " List (e.g., 50)", "50", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_COMPACTIFY_TRAINING_INSTANCES",
    "hypKNOWsys SNN Clustering: Compactify Training Instances",
    "false", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_NUMBER_OF_SHARED_NEAREST_NEIGHBORS",
    "hypKNOWsys Clustering: Default Setting for Number of Shared Nearest "
    + "Neighbors (e.g., 20)", "20", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_SEQUENTIAL_ACCESS",
    "hypKNOWsys Clustering: Default Setting for Sequential Access in "
    + "Application Phase", "false", KProperty.BOOLEAN, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_INPUT_IS_TEXT_UNIT_VECTORS_FILE",
    "hypKNOWsys Clustering: Input File is a DIAsDEM Text Unit Vectors File",
    "true", KProperty.BOOLEAN, KProperty.EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_STRONG_LINK_THRESHOLD",
    "hypKNOWsys Clustering: Default Setting for Strong Link Threshold "
    + "(e.g., 20)", "20", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_LABELING_THRESHOLD",
    "hypKNOWsys Clustering: Default Setting for Labeling Threshold "
    + "(e.g., 25)", "25", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_MERGE_THRESHOLD",
    "hypKNOWsys Clustering: Default Setting for Merge Threshold "
    + "(e.g., 40)", "40", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_NOISE_THRESHOLD",
    "hypKNOWsys Clustering: Default Setting for Noise Threshold "
    + "(e.g., 5)", "5", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DEFAULT_HYPKNOWSYS_TOPIC_THRESHOLD",
    "hypKNOWsys Clustering: Default Setting for Topic Threshold "
    + "(e.g., 40)", "40", KProperty.STRING, KProperty.NOT_EDITABLE)
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsHypknowsysTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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
  /* ########## interface NonBlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    
    ClusterTextUnitVectorsHypknowsysParameter parameter = null;
    if (pParameter instanceof ClusterTextUnitVectorsHypknowsysParameter) {
      parameter = (ClusterTextUnitVectorsHypknowsysParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    File file = new File(parameter.getCollectionFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION)) {
      result.addWarning(
      Tools.insertLineBreaks(65,
      "Warning: Please enter the name of an existing local "
      + DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION + "-file in the "
      + "field 'Collection File'! If no such file is specified, the "
      + "clustering algorithm is executed independent of any DIAsDEM "
      + "collection. This approach may be appropriate when utilizing a "
      + "computer whose file system provides access to a text unit vectors "
      + "file without containing the respective DIAsDEM collection. Do "
      + "you want to continue without inputting a collection file?"));
    }
    file = new File(parameter.getInputVectorsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.ARFF_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n"
      + DIAsDEMguiPreferences.ARFF_FILE_EXTENSION
      + "-file in the field 'Text Unit Vectors File'!");
    }
    if (parameter.getOutputVectorsFileName().trim().length() <= 0
    || !parameter.getOutputVectorsFileName().trim().endsWith(
    GuiClientPreferences.CSV_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local "
      + GuiClientPreferences.CSV_FILE_EXTENSION
      + "-file name\nin the field 'Clustering Results File'!");
    }
    file = new File(parameter.getOutputVectorsFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n"
      + "'Clustering Results File' currently exists.\n"
      + "Do you really want to replace this file?");
    }
    
    if (parameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      
      if (parameter.getClusterModelFileName().trim().length() <= 0
      || !parameter.getClusterModelFileName().trim().endsWith(
      this.getRequiredFileExtension(parameter))) {
        result.addError(
        "Error: Please enter a valid local "
        + this.getRequiredFileExtension(parameter)
        + "-file name\nin the field 'Text Unit Clusterer File'!");
      }
      file = new File(parameter.getClusterModelFileName());
      if (file.exists()) {
        result.addWarning(
        "Warning: The file specified in the field\n"
        + "'Text Unit Clusterer File' currently exists.\n"
        + "Do you really want to replace this file?");
      }
      if (parameter.getDistanceMeasure() == CastParameter
      .HYPKNOWSYS_NOT_APPLICABLE) {
        result.addError(
        "Error: Please choose a valid distance "
        + "measure\nin the field 'Distance Measure'!");
      }
      if (parameter.drawRandomSample() == true) {
        String sampleSize = parameter.getRandomSampleSize();
        if (!(Tools.isInt(sampleSize) || Tools.isDouble(sampleSize))
        || (Tools.isInt(sampleSize) && Tools.string2Int(sampleSize) < 2)
        || (Tools.isDouble(sampleSize) && sampleSize.indexOf(".") > 0
        && (Tools.string2Double(sampleSize) > 1.0d
        || Tools.string2Double(sampleSize) <= 0.0d))) {
          result.addError(
          "Error: Please input a valid random sample size\n"
          + "in the field 'Random Sample Size'! The input can\n"
          + "either be an absolute sample size (e.g., 1000)\n"
          + "a relative sample size (e.g., 0.25).");
        }
      }
      
    }
    else {
      
      file = new File(parameter.getClusterModelFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(this.getRequiredFileExtension(parameter))) {
        result.addError(
        "Error: Please enter the name of an existing local\n"
        + this.getRequiredFileExtension(parameter)
        + "-file in the field 'Text Unit Clusterer File'!");
      }
      
    }
    
    if ((parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_SIMPLE_KMEANS
    || parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BISECTING_KMEANS
    || parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BATCH_SOM)
    && parameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      
      if (!Tools.isInt(parameter.getRandomNumberSeed().trim())) {
        result.addError(
        "Error: Please enter a valid integer\n"
        + "in the field 'Random Number Seed'!");
      }
      
      if (!Tools.isInt(parameter.getMaxIterations().trim())) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Max. k-Means Iterations'!");
      }
      else if (Tools.string2Int(parameter.getMaxIterations().trim()) < 1) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Max. k-Means Iterations'!");
      }
      
    }
    
    if ((parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_SIMPLE_KMEANS
    || parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BISECTING_KMEANS)
    && parameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      
      if (parameter.getNumberOfClusters().indexOf("/") >= 0) {
        // check looping syntax: startValue/incrementValue/endValue
        String[] loopParameters = parameter.getNumberOfClusters().split("/");
        if (loopParameters != null && loopParameters.length == 3) {
          int start = Tools.string2Int(loopParameters[0]);
          int increment = Tools.string2Int(loopParameters[1]);
          int end = Tools.string2Int(loopParameters[2]);
          if (start < 1 && start > end && increment < 1) {
            result.addError(
            "Error: Please enter a syntactically correct loop specification\n"
            + "such as '100/10/200' in the field 'Number of Clusters'!");
          }
        }
        else {
          result.addError(
          "Error: Please enter a syntactically correct loop specification\n"
          + "such as '100/10/200' in the field 'Number of Clusters'!");
        }
      }
      else if (!Tools.isInt(parameter.getNumberOfClusters().trim())) {
        result.addError(
        "Error: Please enter an integer >= 1\n"
        + "in the field 'Number of Clusters'!");
      }
      else if (Tools.string2Int(parameter.getNumberOfClusters().trim()) < 1) {
        result.addError(
        "Error: Please enter an integer >= 1\n"
        + "in the field 'Number of Clusters'!");
      }
      
    }
    
    if ((parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BISECTING_KMEANS)
    && parameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      
      if (!Tools.isInt(parameter.getMinClusterCardinality().trim())) {
        result.addError(
        "Error: Please enter an integer >= 0\n"
        + "in the field 'Min. Cluster Cardinality'!");
      }
      else if (Tools.string2Int(parameter.getMinClusterCardinality().trim())
      < 0) {
        result.addError(
        "Error: Please enter an integer >= 0\n"
        + "in the field 'Min. Cluster Cardinality'!");
      }
      
      if (!Tools.isInt(parameter.getMaxRetriesPerBisectingPass().trim())) {
        result.addError(
        "Error: Please enter an integer > 0 in the\n"
        + "field 'Max. Retries per Bisecting Pass'!");
      }
      else if (Tools.string2Int(parameter.getMaxRetriesPerBisectingPass()
      .trim()) < 0) {
        result.addError(
        "Error: Please enter an integer > 0 in the\n"
        + "field 'Max. Retries per Bisecting Pass'!");
      }
      
    }
    
    if ((parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BATCH_SOM)
    && parameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      
      if (!Tools.isInt(parameter.getNumberOfRows().trim())) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Number of SOM Rows'!");
      }
      else if (Tools.string2Int(parameter.getNumberOfRows().trim()) <= 0) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Number of SOM Rows'!");
      }
      
      if (!Tools.isInt(parameter.getNumberOfColumns().trim())) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Number of SOM Columns'!");
      }
      else if (Tools.string2Int(parameter.getNumberOfColumns().trim()) <= 0) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Number of SOM Columns'!");
      }
      
      if (!(parameter.getLatticeType().trim().equals("rectangular")
      || parameter.getLatticeType().trim().equals("hexagonal"))) {
        result.addError(
        "Error: Please enter a valid lattice type (i.e., 'rectangular'\n"
        + "or 'hexagonal') in the field 'Lattice Type of SOM Array'!");
      }
      
      if (!Tools.isIntArray(parameter.getNeighborhoodRadii().trim())) {
        result.addError(
        "Error: Please enter a valid array of integers (e.g.,\n"
        + "'[3,2,2,2,1,1,1,1,0]') in the field 'Neighborhood Radii'!");
      }
      
    }
    
    if ((parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_JARVIS_PATRICK_SNN
    || parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter
    .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN)
    && parameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      
      if (!Tools.isInt(parameter.getSizeOfNearestNeighborsList().trim())) {
        result.addError(
        "Error: Please enter an integer > 0 in the\n"
        + "field 'Size of Nearest Neighbors List'!");
      }
      else if (Tools.string2Int(parameter.getSizeOfNearestNeighborsList()
      .trim()) <= 0) {
        result.addError(
        "Error: Please enter an integer > 0 in the\n"
        + "field 'Size of Nearest Neighbors List'!");
      }
      
    }
    
    if ((parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_JARVIS_PATRICK_SNN)
    && parameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      
      if (!Tools.isInt(parameter.getNumberOfSharedNearestNeighbors().trim())) {
        result.addError(
        "Error: Please enter an integer > 0 in the\n"
        + "field 'Number of Shared Nearest Neighbors'!");
      }
      else if (Tools.string2Int(parameter.getNumberOfSharedNearestNeighbors()
      .trim()) <= 0) {
        result.addError(
        "Error: Please enter an integer > 0 in the\n"
        + "field 'Number of Shared Nearest Neighbors'!");
        if (Tools.string2Int(parameter.getNumberOfSharedNearestNeighbors()
        .trim()) >= Tools.string2Int(parameter.getSizeOfNearestNeighborsList()
        .trim())) {
          result.addError(
          "Error: The number of shared nearest\n"
          + "neighbors must be smaller than the\n"
          + "size of the nearest neighbors list!");
        }
      }
      
    }
    
    if ((parameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter
    .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN)
    && parameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      
      int numberOfSnn = Tools.string2Int(parameter
      .getSizeOfNearestNeighborsList().trim());
      int strongLinkThreshold = Tools.string2Int(parameter
      .getStrongLinkThreshold().trim());
      if (strongLinkThreshold <= 0) {
        result.addError(
        "Error: Please enter an integer > 0 in\n"
        + "the field 'Strong Link Threshold'!");
      }
      else if (strongLinkThreshold > numberOfSnn) {
        result.addError(
        "Error: The strong link threshold must be smaller than\n"
        + "or equal to the size of the nearest neighbors list!");
      }
      int labelingThreshold = Tools.string2Int(parameter
      .getLabelingThreshold().trim());
      if (labelingThreshold <= 0) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Labeling Threshold'!");
      }
      else if (labelingThreshold > numberOfSnn) {
        result.addError(
        "Error: The labeling threshold must be smaller than\n"
        + "or equal to the size of the nearest neighbors list!");
      }
      else if (labelingThreshold < strongLinkThreshold) {
        result.addError(
        "Error: The labeling threshold must be greater\n"
        + "than or equal to the strong link threshold!");
      }
      int mergeThreshold = Tools.string2Int(parameter
      .getMergeThreshold().trim());
      if (mergeThreshold <= 0) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Merge Threshold'!");
      }
      else if (mergeThreshold > numberOfSnn) {
        result.addError(
        "Error: The merge threshold must be smaller than or\n"
        + "equal to the size of the nearest neighbors list!");
      }
      else if (mergeThreshold < labelingThreshold) {
        result.addError(
        "Error: The merge threshold must be greater\n"
        + "than or equal to the labeling threshold!");
      }
      int noiseThreshold = Tools.string2Int(parameter
      .getNoiseThreshold().trim());
      if (noiseThreshold < 0) {
        result.addError(
        "Error: Please enter an integer >= 0\n"
        + "in the field 'Noise Threshold'!");
      }
      else if (noiseThreshold > numberOfSnn) {
        result.addError(
        "Error: The noise threshold must be smaller than or\n"
        + "equal to the size of the nearest neighbors list!");
      }
      int topicThreshold = Tools.string2Int(parameter
      .getTopicThreshold().trim());
      if (topicThreshold <= 0) {
        result.addError(
        "Error: Please enter an integer > 0\n"
        + "in the field 'Topic Threshold'!");
      }
      else if (topicThreshold > numberOfSnn) {
        result.addError(
        "Error: The topic threshold must be smaller than or\n"
        + "equal to the size of the nearest neighbors list!");
      }
      else if (topicThreshold < noiseThreshold) {
        result.addError(
        "Error: The topic threshold must be greater\n"
        + "than or equal to the noise threshold!");
      }
      
    }
    
    if (parameter.getHtmlReportFileName().trim().length() > 0) {
      
      file = new File(parameter.getHtmlReportFileName());
      if (file.exists()) {
        result.addWarning(
        "Warning: The file specified in the field\n"
        + "'HTML Report File' currently exists.\n"
        + "Do you really want to replace this file?");
      }
      else if (!Tools.isValidandWriteableFileName(parameter
      .getHtmlReportFileName())) {
        result.addError(
        "Error: Please enter a valid local HTML"
        + "-file name\nin the field 'HTML Report File'!");
      }
      
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ClusterTextUnitVectorsHypknowsysParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ClusterTextUnitVectorsHypknowsysResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    DIAsDEMguiMenuBar.ACTIONS_DISCOVER_PATTERNS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null
    && Parameter instanceof ClusterTextUnitVectorsHypknowsysParameter) {
      CastParameter = (ClusterTextUnitVectorsHypknowsysParameter)Parameter;
    }
    else {
      CastParameter = null;
    }
    
    String shortErrorMessage = "Error: Clustering algorithm cannot be "
    + "executed!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Opening Text Unit Vectors "
    + "File");
    this.validateParameter(Parameter, shortErrorMessage);
    
    FileReader inputFileReader = null;
    Instances inputVectors = null;
    String[] clustererOptions = null;
    ObjectInputStream modelInputStream = null;
    ObjectOutputStream modelOutputStream = null;
    
    // variables related to possible loop over number of clusters
    String loopNumberOfClustersAffix = "";
    int loopNumberOfClustersStart = 2;
    int loopNumberOfClustersIncrement = 1;
    int loopNumberOfClustersEnd = 2;
    if (CastParameter.getNumberOfClusters().indexOf("/") >= 0) {
      loopNumberOfClustersAffix = "_LoopNumberOfClusters";
      String[] loopParameters = CastParameter.getNumberOfClusters().split("/");
      loopNumberOfClustersStart = Tools.string2Int(loopParameters[0]);
      loopNumberOfClustersIncrement = Tools.string2Int(loopParameters[1]);
      loopNumberOfClustersEnd = Tools.string2Int(loopParameters[2]);
      this.createHtmlReportHeader();
    }
    else {
      loopNumberOfClustersStart = Tools.string2Int(CastParameter
      .getNumberOfClusters());
      loopNumberOfClustersIncrement = 1;
      loopNumberOfClustersEnd = loopNumberOfClustersStart;
    }
    
    
    try {
      RemoveStringFilter = new RemoveType();
      String[] filterOptions = new String[2];
      filterOptions[0] = "-T";
      filterOptions[1] = "string";
      RemoveStringFilter.setOptions(filterOptions);
    }
    catch (Exception e) {
      this.setErrorTaskResult(100, shortErrorMessage,
      "A detailed error description is not available.");
      e.printStackTrace();
      this.stop();
    }
    
    if (!CastParameter.sequentialAccess() || (CastParameter.sequentialAccess()
    && !CastParameter.drawRandomSample())) {
      // process all text unit vectors in one batch: apply filter now
      try {
        Progress.update(TaskProgress.INDETERMINATE,
        "Removing String Attributes before Clustering");
        DiasdemServer.setTaskProgress(Progress, TaskThread);
        inputFileReader = new FileReader(CastParameter
        .getInputVectorsFileName());
        inputVectors = new Instances(inputFileReader);
        RemoveStringFilter.inputFormat(inputVectors);
        FilteredVectors = Filter.useFilter(inputVectors, RemoveStringFilter);
        inputVectors = null;
        System.gc();
      }
      catch (Exception e) {
        this.setErrorTaskResult(100, shortErrorMessage,
        "A detailed error description is not available.");
        e.printStackTrace();
        this.stop();
      }
    }
    // if required, draw random sample of training vectors
    if (CastParameter.drawRandomSample()) {
      this.drawRandomSample(CastParameter.getRandomSampleSize());
    }
    
    if (CastParameter.getClusteringMode()
    == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE) {
      // loop over number of clusters
      for (int loopNumberOfClusters = loopNumberOfClustersStart;
      loopNumberOfClusters <= loopNumberOfClustersEnd;
      loopNumberOfClusters += loopNumberOfClustersIncrement) {
        if (loopNumberOfClustersStart != loopNumberOfClustersEnd
        && CastParameter.getVerboseMode()) {
          System.out.println("Loop over parameter settings: NumberOfCluster = "
          + loopNumberOfClusters + " in loop " + CastParameter
          .getNumberOfClusters());
        }
        Progress.update(TaskProgress.INDETERMINATE,
        "Executing Clustering Algorithm in Clustering Mode");
        DiasdemServer.setTaskProgress(Progress, TaskThread);
        switch (CastParameter.getClusteringAlgorithm()) {
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_SIMPLE_KMEANS: {
            int options = 12, current = 0;
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              options += 2;
            }
            clustererOptions = new String[options];
            clustererOptions[current++] = "-N";
            clustererOptions[current++] = Tools.int2String(
            loopNumberOfClusters);
            clustererOptions[current++] = "-S";
            clustererOptions[current++] = CastParameter.getRandomNumberSeed();
            clustererOptions[current++] = "-distance";
            clustererOptions[current++] = CastParameter
            .HYPKNOWSYS_DISTANCE_MEASURES_OPTIONS[CastParameter
            .getDistanceMeasure()];
            clustererOptions[current++] = "-maxIterations";
            clustererOptions[current++] = CastParameter
            .getMaxIterations();
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              clustererOptions[current++] = "-htmlReport";
              if (loopNumberOfClustersStart == loopNumberOfClustersEnd) {
                clustererOptions[current++] = CastParameter
                .getHtmlReportFileName();
              }
              else {
                clustererOptions[current++] = this.insertLoopAffixIntoFileName(
                CastParameter.getHtmlReportFileName(), loopNumberOfClustersAffix
                + Tools.int2String(loopNumberOfClusters));
              }
            }
            clustererOptions[current++] = "-verbose";
            clustererOptions[current++] = Tools.boolean2String(CastParameter
            .getVerboseMode());
            clustererOptions[current++] = "-assessIntValidity";
            if (CastParameter.getClusterValidityAssessment()) {
              clustererOptions[current++] = DiasdemProject.getProperty(
              "DEFAULT_HYPKNOWSYS_EVALUATE_INTERNAL_QUALITY");
            }
            else {
              clustererOptions[current++] = "false";
            }
            MyClusterer = new SimpleKMeans();
            ((SimpleKMeans)MyClusterer)
            .setOptions(clustererOptions);
            ((SimpleKMeans)MyClusterer)
            .setTaskProgressListener(DiasdemServer, Progress, TaskThread);
            break;
          }
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_BISECTING_KMEANS: {
            int options = 16, current = 0;
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              options += 2;
            }
            clustererOptions = new String[options];
            clustererOptions[current++] = "-N";
            clustererOptions[current++] = Tools.int2String(
            loopNumberOfClusters);
            clustererOptions[current++] = "-S";
            clustererOptions[current++] = CastParameter.getRandomNumberSeed();
            clustererOptions[current++] = "-distance";
            clustererOptions[current++] = CastParameter
            .HYPKNOWSYS_DISTANCE_MEASURES_OPTIONS[CastParameter
            .getDistanceMeasure()];
            clustererOptions[current++] = "-maxIterations";
            clustererOptions[current++] = CastParameter
            .getMaxIterations();
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              clustererOptions[current++] = "-htmlReport";
              if (loopNumberOfClustersStart == loopNumberOfClustersEnd) {
                clustererOptions[current++] = CastParameter
                .getHtmlReportFileName();
              }
              else {
                clustererOptions[current++] = this.insertLoopAffixIntoFileName(
                CastParameter.getHtmlReportFileName(), loopNumberOfClustersAffix
                + Tools.int2String(loopNumberOfClusters));
              }
            }
            clustererOptions[current++] = "-verbose";
            clustererOptions[current++] = Tools.boolean2String(CastParameter
            .getVerboseMode());
            clustererOptions[current++] = "-assessIntValidity";
            if (CastParameter.getClusterValidityAssessment()) {
              clustererOptions[current++] = DiasdemProject.getProperty(
              "DEFAULT_HYPKNOWSYS_EVALUATE_INTERNAL_QUALITY");
            }
            else {
              clustererOptions[current++] = "false";
            }
            clustererOptions[current++] = "-minClusterCardinality";
            clustererOptions[current++] = CastParameter
            .getMinClusterCardinality();
            clustererOptions[current++] = "-maxRetriesPerBisectingPass";
            clustererOptions[current++] = CastParameter
            .getMaxRetriesPerBisectingPass();
            MyClusterer = new BisectingKMeans();
            ((BisectingKMeans)MyClusterer)
            .setOptions(clustererOptions);
            ((BisectingKMeans)MyClusterer)
            .setTaskProgressListener(DiasdemServer, Progress, TaskThread);
            break;
          }
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_BATCH_SOM: {
            int options = 18, current = 0;
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              options += 2;
            }
            clustererOptions = new String[options];
            clustererOptions[current++] = "-rows";
            clustererOptions[current++] = CastParameter.getNumberOfRows();
            clustererOptions[current++] = "-columns";
            clustererOptions[current++] = CastParameter.getNumberOfColumns();
            clustererOptions[current++] = "-latticeType";
            clustererOptions[current++] = CastParameter.getLatticeType();
            clustererOptions[current++] = "-neighborhoodRadii";
            clustererOptions[current++] = CastParameter.getNeighborhoodRadii();
            clustererOptions[current++] = "-S";
            clustererOptions[current++] = CastParameter.getRandomNumberSeed();
            clustererOptions[current++] = "-distance";
            clustererOptions[current++] = CastParameter
            .HYPKNOWSYS_DISTANCE_MEASURES_OPTIONS[CastParameter
            .getDistanceMeasure()];
            clustererOptions[current++] = "-maxIterations";
            clustererOptions[current++] = CastParameter
            .getMaxIterations();
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              clustererOptions[current++] = "-htmlReport";
              if (loopNumberOfClustersStart == loopNumberOfClustersEnd) {
                clustererOptions[current++] = CastParameter
                .getHtmlReportFileName();
              }
              else {
                clustererOptions[current++] = this.insertLoopAffixIntoFileName(
                CastParameter.getHtmlReportFileName(), loopNumberOfClustersAffix
                + Tools.int2String(loopNumberOfClusters));
              }
            }
            clustererOptions[current++] = "-verbose";
            clustererOptions[current++] = Tools.boolean2String(CastParameter
            .getVerboseMode());
            clustererOptions[current++] = "-assessIntValidity";
            if (CastParameter.getClusterValidityAssessment()) {
              clustererOptions[current++] = DiasdemProject.getProperty(
              "DEFAULT_HYPKNOWSYS_EVALUATE_INTERNAL_QUALITY");
            }
            else {
              clustererOptions[current++] = "false";
            }
            MyClusterer = new BatchSom();
            ((BatchSom)MyClusterer)
            .setOptions(clustererOptions);
            ((BatchSom)MyClusterer)
            .setTaskProgressListener(DiasdemServer, Progress, TaskThread);
            break;
          }
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_JARVIS_PATRICK_SNN: {
            int options = 14, current = 0;
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              options += 2;
            }
            clustererOptions = new String[options];
            clustererOptions[current++] = "-S";
            clustererOptions[current++] = CastParameter.getRandomNumberSeed();
            clustererOptions[current++] = "-sizeOfNnList";
            clustererOptions[current++] = CastParameter
            .getSizeOfNearestNeighborsList();
            clustererOptions[current++] = "-compactify";
            clustererOptions[current++] = Tools.boolean2String(CastParameter
            .getCompactifyTrainingInstances());
            clustererOptions[current++] = "-numberOfSnn";
            clustererOptions[current++] = CastParameter
            .getNumberOfSharedNearestNeighbors();
            clustererOptions[current++] = "-distance";
            clustererOptions[current++] = CastParameter
            .HYPKNOWSYS_DISTANCE_MEASURES_OPTIONS[CastParameter
            .getDistanceMeasure()];
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              clustererOptions[current++] = "-htmlReport";
              clustererOptions[current++] = CastParameter
              .getHtmlReportFileName();
            }
            clustererOptions[current++] = "-verbose";
            clustererOptions[current++] = Tools.boolean2String(CastParameter
            .getVerboseMode());
            clustererOptions[current++] = "-assessIntValidity";
            if (CastParameter.getClusterValidityAssessment()) {
              clustererOptions[current++] = DiasdemProject.getProperty(
              "DEFAULT_HYPKNOWSYS_EVALUATE_INTERNAL_QUALITY");
            }
            else {
              clustererOptions[current++] = "false";
            }
            MyClusterer = new JarvisPatrickSnn();
            ((JarvisPatrickSnn)MyClusterer)
            .setOptions(clustererOptions);
            ((JarvisPatrickSnn)MyClusterer)
            .setTaskProgressListener(DiasdemServer, Progress, TaskThread);
            break;
          }
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN: {
            int options = 22, current = 0;
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              options += 2;
            }
            clustererOptions = new String[options];
            clustererOptions[current++] = "-S";
            clustererOptions[current++] = CastParameter.getRandomNumberSeed();
            clustererOptions[current++] = "-sizeOfNnList";
            clustererOptions[current++] = CastParameter
            .getSizeOfNearestNeighborsList();
            clustererOptions[current++] = "-sizeOfNnList";
            clustererOptions[current++] = CastParameter
            .getSizeOfNearestNeighborsList();
            clustererOptions[current++] = "-compactify";
            clustererOptions[current++] = Tools.boolean2String(CastParameter
            .getCompactifyTrainingInstances());
            clustererOptions[current++] = "-labelingT";
            clustererOptions[current++] = CastParameter.getLabelingThreshold();
            clustererOptions[current++] = "-mergeT";
            clustererOptions[current++] = CastParameter.getMergeThreshold();
            clustererOptions[current++] = "-noiseT";
            clustererOptions[current++] = CastParameter.getNoiseThreshold();
            clustererOptions[current++] = "-topicT";
            clustererOptions[current++] = CastParameter.getTopicThreshold();
            clustererOptions[current++] = "-distance";
            clustererOptions[current++] = CastParameter
            .HYPKNOWSYS_DISTANCE_MEASURES_OPTIONS[CastParameter
            .getDistanceMeasure()];
            if (CastParameter.getHtmlReportFileName().trim().length() > 0) {
              clustererOptions[current++] = "-htmlReport";
              clustererOptions[current++] = CastParameter
              .getHtmlReportFileName();
            }
            clustererOptions[current++] = "-verbose";
            clustererOptions[current++] = Tools.boolean2String(CastParameter
            .getVerboseMode());
            clustererOptions[current++] = "-assessIntValidity";
            if (CastParameter.getClusterValidityAssessment()) {
              clustererOptions[current++] = DiasdemProject.getProperty(
              "DEFAULT_HYPKNOWSYS_EVALUATE_INTERNAL_QUALITY");
            }
            else {
              clustererOptions[current++] = "false";
            }
            MyClusterer = new ErtozSteinbachKumarTopicsSnn();
            ((ErtozSteinbachKumarTopicsSnn)
            MyClusterer).setOptions(clustererOptions);
            ((ErtozSteinbachKumarTopicsSnn)
            MyClusterer).setTaskProgressListener(DiasdemServer, Progress,
            TaskThread);
            break;
          }
        }
        if (SampledVectors == null) {
          MyClusterer.buildClusterer(FilteredVectors);
          // re-create the filtered input data set because some clustering
          // algorithms (e.g., Jarvis-Patrick) compactify the training data set
          try {
            Progress.update(TaskProgress.INDETERMINATE,
            "Removing String Attributes before Applying Clusterer");
            DiasdemServer.setTaskProgress(Progress, TaskThread);
            inputFileReader = new FileReader(CastParameter
            .getInputVectorsFileName());
            inputVectors = new Instances(inputFileReader);
            RemoveStringFilter.inputFormat(inputVectors);
            FilteredVectors = Filter.useFilter(inputVectors,
            RemoveStringFilter);
            inputVectors = null;
            System.gc();
          }
          catch (Exception e) {
            this.setErrorTaskResult(100, shortErrorMessage,
            "A detailed error description is not available.");
            e.printStackTrace();
            this.stop();
          }
        }
        else {
          MyClusterer.buildClusterer(SampledVectors);
        }
        if (MyClusterer.numberOfClusters() == 0) {
          this.setErrorTaskResult(100, shortErrorMessage,
          "Error in Creating a New Clustering Model: See HTML Report");
          this.stop();
        }
        if (loopNumberOfClustersStart == loopNumberOfClustersEnd) {
          try {
            modelOutputStream = new ObjectOutputStream(new FileOutputStream(
            CastParameter.getClusterModelFileName()));
            this.createOutputFile(CastParameter.getOutputVectorsFileName());
          }
          catch (Exception e) {
            this.setErrorTaskResult(100, shortErrorMessage,
            "Error in writing the text unit clusterer file.\n"
            + "A detailed error description is not available.");
            e.printStackTrace();
            this.stop();
          }
        }
        else {
          this.createHtmlReportOverviewLine(loopNumberOfClusters,
          this.insertLoopAffixIntoFileName(CastParameter
          .getHtmlReportFileName(),
          loopNumberOfClustersAffix + Tools.int2String(loopNumberOfClusters)),
          (MyClusterer instanceof AbstractKMeans ? ((AbstractKMeans)MyClusterer)
          .getClusterValidityAssessor().getMeanIntraCentroidDistance() : 0.0d),
          (MyClusterer instanceof AbstractKMeans ? ((AbstractKMeans)MyClusterer)
          .getClusterValidityAssessor().getMeanInterCentroidDistance() : 0.0d));
          try {
            modelOutputStream = new ObjectOutputStream(new FileOutputStream(
            this.insertLoopAffixIntoFileName(CastParameter
            .getClusterModelFileName(), loopNumberOfClustersAffix + Tools
            .int2String(loopNumberOfClusters))));
            this.createOutputFile(this.insertLoopAffixIntoFileName(CastParameter
            .getOutputVectorsFileName(), loopNumberOfClustersAffix + Tools
            .int2String(loopNumberOfClusters)));
          }
          catch (Exception e) {
            this.setErrorTaskResult(100, shortErrorMessage,
            "Error in writing the clustering results file.\n"
            + "A detailed error description is not available.");
            e.printStackTrace();
            this.stop();
          }
        }
        try {
          modelOutputStream.writeObject(MyClusterer);
          modelOutputStream.flush();
          modelOutputStream.close();
        }
        catch (Exception e) {
          this.setErrorTaskResult(100, shortErrorMessage,
          "Error in writing the text unit clusterer file.\n"
          + "A detailed error description is not available.");
          e.printStackTrace();
          this.stop();
        }
      }
    }  // loopNumberOfClusters
    else {
      Progress.update(TaskProgress.INDETERMINATE,
      "Executing Clustering Algorithm in Application Mode");
      DiasdemServer.setTaskProgress(Progress, TaskThread);
      try {
        modelInputStream = new ObjectInputStream(new FileInputStream(
        CastParameter.getClusterModelFileName()));
        switch (CastParameter.getClusteringAlgorithm()) {
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_SIMPLE_KMEANS: {
            MyClusterer = (SimpleKMeans)modelInputStream.readObject();
            break;
          }
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_BISECTING_KMEANS: {
            MyClusterer = (BisectingKMeans)modelInputStream.readObject();
            break;
          }
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_BATCH_SOM: {
            MyClusterer = (BatchSom)modelInputStream.readObject();
            break;
          }
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_JARVIS_PATRICK_SNN: {
            MyClusterer = (JarvisPatrickSnn)modelInputStream.readObject();
            break;
          }
          case ClusterTextUnitVectorsHypknowsysParameter
          .HYPKNOWSYS_ERTOZ_STEINBACH_KUMAR_TOPICS_SNN: {
            MyClusterer = (ErtozSteinbachKumarTopicsSnn)modelInputStream
            .readObject();
            break;
          }
        }
        modelInputStream.close();
      }
      catch (Exception e) {
        this.setErrorTaskResult(100, shortErrorMessage,
        "A detailed error description is not available.");
        e.printStackTrace();
        this.stop();
      }
      this.createOutputFile(CastParameter.getOutputVectorsFileName());
    }
    
    if (CastParameter.getNumberOfClusters().indexOf("/") >= 0) {
      this.createHtmlReportFooter();
    }
    
    int numberOfClusters = MyClusterer.numberOfClusters();
    if (!Tools.stringIsNullOrEmpty(CastParameter.getCollectionFileName())) {
      Result.update(TaskResult.FINAL_RESULT, Tools.insertLineBreaks(56,
      "All untagged text unit vectors in the DIAsDEM collection "
      + Tools.shortenFileName(CastParameter.getCollectionFileName(), 50)
      + " have been assigned to one of " + numberOfClusters
      + " clusters." + (CastParameter.getClusteringMode()
      == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE
      ? " The maximum cluster ID is " + MaxClusterID + " in this iteration."
      : "") + " Clustering results file: " + Tools.shortenFileName(
      CastParameter.getOutputVectorsFileName(), 50)));
    }
    else {
      Result.update(TaskResult.FINAL_RESULT, Tools.insertLineBreaks(56,
      "All vectors contained in the text unit vectors file "
      + Tools.shortenFileName(CastParameter.getInputVectorsFileName(), 50)
      + " have been assigned to one of " + numberOfClusters
      + " clusters." + (CastParameter.getClusteringMode()
      == ClusterTextUnitVectorsHypknowsysParameter.CLUSTERING_PHASE
      ? " The maximum cluster ID is " + MaxClusterID + " in this iteration."
      : "") + " Clustering results file: " + Tools.shortenFileName(
      CastParameter.getOutputVectorsFileName(), 50)));
    }
    this.setTaskResult(100, "All Text Units Clustered ...", Result,
    TaskResult.FINAL_RESULT, Task.TASK_FINISHED);
    
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
  
  private String insertLoopAffixIntoFileName(String pFileName,
  String pLoopAffix) {
    
    String result = pFileName;
    if (!Tools.stringIsNullOrEmpty(pLoopAffix)) {
      result = Tools.removeFileExtension(pFileName)
      + pLoopAffix + Tools.getFileExtension(pFileName);
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getRequiredFileExtension(
  ClusterTextUnitVectorsHypknowsysParameter pParameter) {
    
    if (pParameter != null && pParameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_SIMPLE_KMEANS) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_SIMPLE_KMEANS_FILE_EXTENSION;
    }
    if (pParameter != null && pParameter.getClusteringAlgorithm()
    == ClusterTextUnitVectorsHypknowsysParameter.HYPKNOWSYS_BISECTING_KMEANS) {
      return DIAsDEMguiPreferences.HYPKNOWSYS_BISECTING_KMEANS_FILE_EXTENSION;
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createHtmlReportHeader() {
    
    Template debuggingOutputHeader = new Template(Tools
    .stringFromTextualSystemResource("org/hypknowsys/resources/html/"
    + "HtmlFile_HeaderTemplate.html"));
    debuggingOutputHeader.addValue("${Title}",
    "Clustering Report: Summary of Loop over Parameter Settings");
    HtmlReport =  new TextFile(new File(CastParameter.getHtmlReportFileName()));
    HtmlReport.open();
    HtmlReport.setFirstLine(debuggingOutputHeader.insertValues());
    HtmlReport.setNextLine("<p>Created by " + this.getClass().getName()
    + " on " + Tools.getSystemDate() + "</p>");
    
    HtmlReport.setNextLine("<h3>Parameter Settings</h3>");
    HtmlReport.setNextLine(
    "<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Parameter</th>"
    + "<th align=\"left\" valign=\"top\">Value</th></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Clustering Algorithm</td>"
    + "<td align=\"left\" valign=\"top\">" + CastParameter
    .CLUSTERING_ALGORITHMS[CastParameter.getClusteringAlgorithm()]
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Distance Measure</td>"
    + "<td align=\"left\" valign=\"top\">" + CastParameter
    .DISTANCE_MEASURES[CastParameter.getDistanceMeasure()]
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Loop over Number of Clusters</td>"
    + "<td align=\"left\" valign=\"top\">" + CastParameter.getNumberOfClusters()
    + "</td></tr>");
    HtmlReport.setNextLine("</table>");
    
    HtmlReport.setNextLine("<h3>Results</h3>");
    HtmlReport.setNextLine(
    "<table border=\"1\"><tr>"
    + "<th align=\"right\" valign=\"top\">Number of<br>Clusters</th>"
    + "<th align=\"left\" valign=\"top\">Report</th>"
    + "<th align=\"right\" valign=\"top\">A = Mean(Intra-<br>"
    + "Centroid Distance)</th>"
    + "<th align=\"right\" valign=\"top\">B = Mean(Inter-<br>"
    + "Centroid Distance)</th>"
    + "<th align=\"right\" valign=\"top\">(1 - B) + A</th>"
    + "</tr>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createHtmlReportOverviewLine(int pNumberOfClusters,
  String pReportFileName, double pMeanIntraCentroidDistance,
  double pMeanInterCentroidDistance) {
    
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"right\" valign=\"top\">"
    + Tools.int2String(pNumberOfClusters) + "</td>"
    + "<td align=\"left\" valign=\"top\"><a href=\""
    + Tools.removeDirectory(pReportFileName) + "\" target=\"_new\">"
    + Tools.removeDirectory(pReportFileName) + "</a>"
    + "</td>"
    + "<td align=\"right\" valign=\"top\">"
    + (CastParameter.getClusterValidityAssessment()
    ? Utils.doubleToString(pMeanIntraCentroidDistance, 3) : "&nbsp;")
    + "</td>"
    + "<td align=\"right\" valign=\"top\">"
    + (CastParameter.getClusterValidityAssessment()
    ? Utils.doubleToString(pMeanInterCentroidDistance, 3) : "&nbsp;")
    + "</td>"
    + "<td align=\"right\" valign=\"top\">"
    + (CastParameter.getClusterValidityAssessment()
    ? Utils.doubleToString((1.0d - pMeanInterCentroidDistance)
    + pMeanIntraCentroidDistance, 3) : "&nbsp;")
    + "</td>"
    + "</tr>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createHtmlReportFooter() {
    
    HtmlReport.setNextLine("</table>");
    
    HtmlReport.setNextLine(Tools.stringFromTextualSystemResource(
    "org/hypknowsys/resources/html/HtmlFile_FooterTemplate.html"));
    HtmlReport.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void createOutputFile(String pOutputFilename) {
    
    Progress.update(TaskProgress.INDETERMINATE,
    "Creating the Result File with Cluster Assignments");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    TextFile outputFile = new TextFile(new File(pOutputFilename + ".temp"));
    outputFile.empty();
    outputFile.open();
    if (CastParameter.sequentialAccess()) {
      // sequentially access instances
      SequentialInstanceReader instanceReader = null;
      try {
        instanceReader = new SequentialInstanceReader(new File(CastParameter
        .getInputVectorsFileName()), DiasdemProject.getIntProperty(
        "DEFAULT_FILE_BUFFER_SIZE"));
        instanceReader.open();
        RemoveStringFilter.inputFormat(instanceReader.getPrototypeInstances());
      }
      catch (Exception e) {
        this.setErrorTaskResult(100, "Error: Clustering algorithm cannot be "
        + "executed!", "A detailed error description is not available.");
        this.stop();
      }
      int instanceID = 0;
      int numberOfInstances = instanceReader.getApproxNumberOfInstances();
      Instance instance = instanceReader.getFirstInstance();
      while (instance != null) {
        if (instanceID % 1000 == 0) {
          Progress.update(TaskProgress.INDETERMINATE,
          "Creating the Result File: " + ((int)(100.0d * instanceID
          / numberOfInstances)) + "% Completed");
          DiasdemServer.setTaskProgress(Progress, TaskThread);
        }
        TmpStringBuffer = new StringBuffer(100);
        TmpStringBuffer.append(instanceID);
        TmpStringBuffer.append(' ');
        RemoveStringFilter.input(instance);
        TmpStringBuffer.append(MyClusterer.clusterInstance(
        RemoveStringFilter.output()));
        outputFile.setNextLine(TmpStringBuffer.toString());
        instanceID++;
        instance = instanceReader.getNextInstance();
      }
    }
    else {
      // complete instances are in the main memory
      for (int i = 0; i < FilteredVectors.numInstances(); i++) {
        if (i % 1000 == 0) {
          Progress.update(TaskProgress.INDETERMINATE,
          "Creating the Result File: " + ((int)(100.0d * i / FilteredVectors
          .numInstances())) + "% Completed");
          DiasdemServer.setTaskProgress(Progress, TaskThread);
        }
        TmpStringBuffer = new StringBuffer(100);
        TmpStringBuffer.append(i);
        TmpStringBuffer.append(' ');
        TmpStringBuffer.append(MyClusterer.clusterInstance(FilteredVectors
        .instance(i)));
        outputFile.setNextLine(TmpStringBuffer.toString());
      }
    }
    outputFile.close();
    
    if (DiasdemProject.getBooleanProperty(
    "DEFAULT_HYPKNOWSYS_INPUT_IS_TEXT_UNIT_VECTORS_FILE")) {
      MaxClusterID = HypknowsysOutput2Csv.convertHypknowsysOutput2Csv(
      CastParameter.getInputVectorsFileName(), pOutputFilename + ".temp",
      pOutputFilename);
    }
    else {
      System.out.println("Warning: This task does not create a DIAsDEM"
      + " clustering results file, but an augmented ARFF file.");
      MaxClusterID = HypknowsysOutput2Csv.createArffOutputFile(
      CastParameter.getInputVectorsFileName(), pOutputFilename + ".temp",
      pOutputFilename + ".arff");
    }
    
    DiasdemProject.setIntProperty("DEFAULT_MAX_CLUSTER_ID", MaxClusterID);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void drawRandomSample(String pSampleSize) {
    
    Progress.update(TaskProgress.INDETERMINATE,
    "Drawing Random Sample of Text Unit Vectors");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    SampledVectors = null;
    SequentialInstanceReader instanceReader = null;
    int numberOfInstances = 0;
    if (CastParameter.sequentialAccess()) {
      try {
        instanceReader = new SequentialInstanceReader(new File(CastParameter
        .getInputVectorsFileName()), DiasdemProject.getIntProperty(
        "DEFAULT_FILE_BUFFER_SIZE"));
        instanceReader.open();
        RemoveStringFilter.inputFormat(instanceReader.getPrototypeInstances());
        numberOfInstances = instanceReader.getApproxNumberOfInstances();
      }
      catch (Exception e) {
        this.setErrorTaskResult(100, "Error: Clustering algorithm cannot be "
        + "executed!", "A detailed error description is not available.");
        this.stop();
      }
    }
    else  {
      numberOfInstances = FilteredVectors.numInstances();
    }
    
    double sampleSize = 0.0d;
    if (Tools.isInt(pSampleSize) && Tools.string2Int(pSampleSize) > 1) {
      // abosulte sample size is given (e.g., 10000)
      sampleSize = Math.min((double)Tools.string2Int(pSampleSize),
      numberOfInstances);
    }
    else if (Tools.isDouble(pSampleSize) && Tools.string2Double(pSampleSize)
    <= 1.0d && Tools.string2Double(pSampleSize) > 0.0d) {
      // relative sample size is given (e.g., 0.25)
      sampleSize = Tools.string2Double(pSampleSize) * numberOfInstances;
    }
    else {
      return;
    }
    if (CastParameter.sequentialAccess()) {
      SampledVectors = new Instances(RemoveStringFilter.getOutputFormat(),
      (int)sampleSize);
    }
    else  {
      SampledVectors = new Instances(FilteredVectors, (int)sampleSize);
    }
    
    Random randomizer = new Random(Tools.string2Long(CastParameter
    .getRandomNumberSeed()));
    TreeSet sortedRandoms = new TreeSet();
    for (int i = 0; sortedRandoms.size() < (int)sampleSize; i++) {
      sortedRandoms.add(new Integer(randomizer.nextInt(numberOfInstances)));
    }
    Iterator iterator = sortedRandoms.iterator();
    if (CastParameter.sequentialAccess()) {
      // sequentially access instances
      int nextInstanceID = 0;
      int instanceID = 0;
      Instance instance = instanceReader.getFirstInstance();
      if (iterator.hasNext()) {
        nextInstanceID = ((Integer)iterator.next()).intValue();
      }
      while (instance != null) {
        if (instanceID == nextInstanceID) {
          RemoveStringFilter.input(instance);
          SampledVectors.add(RemoveStringFilter.output());
          if (iterator.hasNext()) {
            nextInstanceID = ((Integer)iterator.next()).intValue();
          }
          else {
            // there are no further instances in the sample
            nextInstanceID = -1;
          }
        }
        instanceID++;
        instance = instanceReader.getNextInstance();
      }
    }
    else {
      // complete instances are in the main memory
      while (iterator.hasNext()) {
        SampledVectors.add(FilteredVectors.instance(
        ((Integer)iterator.next()).intValue()));
      }
    }
    randomizer = null;
    sortedRandoms = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}