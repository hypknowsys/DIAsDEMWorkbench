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

package org.hypknowsys.algorithms.clusterers;

import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;
import org.hypknowsys.algorithms.core.AbstractOptionHandler;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Option;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.TaskProgress;

/**
 * Abstract class for various clustering algorithm that are based on the
 * shared nearest neighbors (SNN) paradigm. Training and applications instances 
 * should be pre-processed outside this algorithm instance, but using the 
 * exactly the same process workflow for both training and application data. 
 * According to the chosen distance measure, STRING and DATE attributes might 
 * be ignored for computing the distance between two instances. Check the 
 * documentation of the respective distance measure.<p>
 *
 * Valid options of all SNN variants are:<p>
 *
 * -distance <euclidean|cosine|extendedJaccard|extendedDice> <br>
 * Specify the distance measure to be employed, default: euclidean. <p>
 *
 * -assessIntValidity <full|true|quick|false> <br>
 * Turn internal cluster validity assessment on or off, Default: false. <p>
 *
 * -verbose <true|false> <br>
 * Turn verbose mode on or off, default: false. <p>
 *
 * -htmlReport <HTML report file name> <br>
 * Specify the HTML report file name, default: skip report creation. <p>
 *
 * -compactify <true|false> <br>
 * Turn compactification of the training data set on or off, default: false. <p>
 *
 * -sizeOfNnList <integer> <br>
 * Specify the size of the neighborhood list for each instance, default: 50. <p>
 *
 * @version 0.1, 10 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 * @see Clusterer
 * @see OptionHandler
 */

public abstract class AbstractSnn extends Clusterer {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Number of generated clusters: The value is set after completing
   * the training phase.
   */
  protected int NumberOfClusters = 0;
  
 /**
   * Array containing cluster cardinalities: The array size equals
   * this.NumberOfClusters and ClusterCardinality[0] represents the
   * cardinality of the first cluster (ID = 0). Analogously, the 
   * cardinality of the last cluster is stored in 
   * ClusterCardinality[umberOfClusters - 1].
   */
  protected int[] ClusterCardinality = null;
  
  /**
   * Data set containing the average instances: The number of instances equals
   * this.NumberOfClusters and AverageInstances.instance(0) represents the 
   * average instance of first cluster (ID = 0). Analogously, the average 
   * instance of the last cluster is stored in AverageInstances.instance(
   * this.NumberOfClusters - 1).
   */
  protected Instances AverageInstances = null;
  
  /**
   * Number of training instances for improving performance only
   */
  protected int NumberOfTrainingInstances = 0;
  
  /**
   * Size of the neighborhood list for each instance, SizeOfNearestNeighborsList
   * must be significantly smaller than number of training instances, default: 
   * 50
   */
  protected int SizeOfNearestNeighborsList 
  = DEFAULT_SIZE_OF_NEAREST_NEIGHBORS_LIST;
  
  /**
   * Array of lists that contain SizeOfNearestNeighborsList nearest neighbors 
   * for each training instance, first dimension: number of training instances,
   * second dimension: SizeOfNearestNeighborsList
   */
  protected int[][] NeighborhoodList = null;
  
  /**
   * List that contains the similarity of the least similar from the list of 
   * SizeOfNearestNeighborsList nearest neighbors for each training instance
   */
  protected float[] MinNearestNeighborSimilarity = null;
  
  /**
   * Flag variable for compactifying the training instances, default: false.
   */
  protected boolean CompactifyTrainingInstances = false;
  
  /**
   * List that contains the SizeOfNearestNeighborsList nearest neighbors 
   * of the instance to be assigned to clusters (application phase only)
   */
  protected int[] ApplNeighborhoodList = null;
  
  /**
   * List that contains the similarities of SizeOfNearestNeighborsList nearest 
   * neighbors of the instance to be assigned to clusters (application phase 
   * only)
   */
  protected float[] ApplSimilarityList = null;
  
  /**
   * List that contains the similarities of the instance to be assigned to 
   * clusters to all training instances (application phase only)
   */
  protected float[] ApplNearestNeighborSimilarities = null;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Temporary similarity matrix of training data set
   */
  protected transient float[] SimilarityMatrix = null;
  
  /**
   * Temporary string buffer for performance purposes only
   */
  protected transient StringBuffer TmpStringBuffer = null;
  
  /**
   * Temporary float variable (similarity in application phase) for improving 
   * performance only
   */
  protected transient float ApplSimilarity = 0.0f;
  
  /**
   * Temporary float variable (number of shared nearest neighbors in application
   * phase) for improving performance only
   */
  protected transient int ApplNumberOfSharedNearestNeighbors = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Initialization value for lists of shared nearest neighbors
   */
  protected static final int EMPTY = -1;
  
  /**
   * Default setting of attribute this.SizeOfNearestNeighborsList
   */
  protected static final int DEFAULT_SIZE_OF_NEAREST_NEIGHBORS_LIST = 50;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the size of the neighborhood list for each training instance.
   *
   * @return the number of clusters generated for a training dataset.
   */
  public int getSizeOfNearestNeighborsList() {
    
    return SizeOfNearestNeighborsList;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of clusters. The value is set after completing
   * the training phase.
   *
   * @return the number of clusters generated for a training dataset.
   */
  public int getNumberOfClusters() {
    
    return NumberOfClusters;
    
  }
  
  /**
   * Returns the flag variable for compactifying the training instances.
   *
   * @return the flag variable for compactifying the training instances.
   */
  public boolean getCompactifyTrainingInstances() {
    
    return CompactifyTrainingInstances;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the size of the neighborhood list for each training instance.
   *
   * @param pSizeOfNearestNeighborsList the size of the neighborhood list
   */
  public void setSizeOfNearestNeighborsList(int pSizeOfNearestNeighborsList) {
    
    SizeOfNearestNeighborsList = pSizeOfNearestNeighborsList;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the flag variable for compactifying the training instances.
   *
   * @param pCompactifyTrainingInstances true if the training set shall be
   * compactified by removing excess duplicates
   */
  public void setCompactifyTrainingInstances(
  boolean pCompactifyTrainingInstances) {
    
    CompactifyTrainingInstances = pCompactifyTrainingInstances;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns a string describing this clusterer.
   *
   * @return a description of the clusterer as a string
   */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(10000);
    
    TmpStringBuffer.append(super.toString());
    TmpStringBuffer.append("Number of generated clusters: " 
    + NumberOfClusters + "\n");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface OptionHandler methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an enumeration describing the available options. <p>
   *
   * Valid options of all SNN variants are:<p>
   *
   * -distance <euclidean|cosine|extendedJaccard|extendedDice> <br>
   * Specify the distance measure to be employed, default: euclidean. <p>
   *
   * -assessIntValidity <full|true|quick|false> <br>
   * Turn internal cluster validity assessment on or off, Default: false. <p>
   *
   * -verbose <true|false> <br>
   * Turn verbose mode on or off, default: false. <p>
   *
   * -htmlReport <HTML report file name> <br>
   * Specify the HTML report file name, default: skip report creation. <p>
   *
   * -compactify <true|false> <br>
   * Turn compactification of the training data set on or off, default: 
   * false. <p>
   *
   * -sizeOfNnList <integer> <br>
   * Specify the size of the neighborhood list for each instance, default: 50. 
   * <p>
   *
   * @return an enumeration of all the available options.
   *
   **/
  
  public Enumeration listOptions() {
    
    Vector newVector = new Vector();
    Enumeration enumeration = super.listOptions();
    while (enumeration.hasMoreElements()) {
      newVector.addElement(enumeration.nextElement());
    }
    
    newVector.addElement(new Option(
    "\tTurn compactification of the training data set on or off, default: "
    + "false.", "compactify", 1, "-compactify <true|false>"));
    newVector.addElement(new Option(
    "\tSpecify the size of the neighborhood list for each instance, default: "
    + "50.", "sizeOfNnList", 1, "-sizeOfNnList <integer>"));
        
    return  newVector.elements();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Parses a given list of options.
   * @param pOptions the list of options as an array of strings
   **/
  
  public void setOptions(String[] pOptions) {
    
    super.setOptions(pOptions);
    
    String optionString = AbstractOptionHandler.getOption(
    "compactify", pOptions);
    this.setCompactifyTrainingInstances(false);
    if (optionString.length() != 0) {
      this.setCompactifyTrainingInstances(Tools.string2Boolean(optionString));
    }
    optionString = AbstractOptionHandler.getOption(
    "sizeOfNnList", pOptions);
    this.setSizeOfNearestNeighborsList(DEFAULT_SIZE_OF_NEAREST_NEIGHBORS_LIST);
    if (optionString.length() != 0) {
      this.setSizeOfNearestNeighborsList(Tools.string2Int(optionString));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the current settings of AbstractSnn.
   *
   * @return an array of strings suitable for passing to setOptions()
   */
  
  public String[] getOptions() {
    
    String[] superOptions = super.getOptions();
    
    int numberOfOptions = superOptions.length + 4;
    String[] options = new String[numberOfOptions];
    int current = 0;

    for (int i = 0; i < superOptions.length; i++) {
      options[current++] = superOptions[i];
    }
    options[current++] = "-compactify";
    options[current++] = "" + this.getCompactifyTrainingInstances();
    options[current++] = "-sizeOfNnList";
    options[current++] = "" + this.getSizeOfNearestNeighborsList();
    
    while (current < options.length) {
      options[current++] = "";
    }
    
    return options;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## abstract class Clusterer methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Classifies a given instance.
   *
   * @param pInstance the instance to be assigned to a cluster
   * @return the number of the assigned cluster as an interger
   * if the class is enumerated, otherwise the predicted value
   */
  
  public int clusterInstance(Instance pInstance) {
    
    return clusterProcessedInstance(pInstance);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of clusters.
   *
   * @return the number of clusters generated for a training dataset.
   */
  
  public int numberOfClusters() {
    
    return NumberOfClusters;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Executes the specific clustering algorithm.
   */
  
  protected abstract void executeClusteringAlgorithm();

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Evaluates cluster validity by computing cluster cardinalities, 
   * intra-cluster distances and squared intra-cluster distances.
   * Prerequisites: The specific clustering algorithm must have been executed
   * and the array ClusterCentroids contains the final cluster centroids.
   */
  
  protected void assessClusterValidity() {
    
    this.computeAverageInstances();
    
    MyClusterValidityAssessor = new RelativeClusterValidityAssessor();
    MyClusterValidityAssessor.setTaskProgressListener(MyServer, MyTaskProgress, 
    MyTaskThread);
    MyClusterValidityAssessor.assessClusterValidity(EvaluateInternalQuality, 
    (Clusterer)this);
  
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the name of the representative instance returned by the specific
   * SNN clustering algorithm.
   * @return the name of the representative instance
   */
  
  protected String getNameOfRepresentativeInstance() {
    
    return "Average Instance";
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns one instance (i.e., the average instance) that represents the 
   * specified cluster. Prerequisites: The specific clustering algorithm must 
   * have been executed.
   * @param pClusterID the ID of the cluster whose representative instance
   * shall be returned; 0 <= pClusterID < this.getNumberOfClusters()
   * @return the representative instance or null if pClusterID is not valid
   */
  
  protected Instance getRepresentativeInstance(
  int pClusterID) {
    
    if (AverageInstances != null && pClusterID >= 0 
    && pClusterID < this.getNumberOfClusters()) {
      return AverageInstances.instance(pClusterID);
    }
    else {
      return null;
    }
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Initializes the data set AverageInstances and computes the average
   * instance of each cluster. Prerequisites: The specific clustering algorithm
   * must have been executed.
   */
  
  protected void computeAverageInstances() {
    
    Instances[] clusterAssignments = new Instances[NumberOfClusters];
    double[] clusterCentroid = null;
    AverageInstances = new Instances(TrainingInstances, NumberOfClusters);
    for (int i = 0; i < NumberOfClusters; i++) {
      clusterAssignments[i] = new Instances(TrainingInstances, 0);
    }
    for (int i = 0; i < TrainingInstances.numInstances(); i++) {
      clusterAssignments[ClusterAssignments[i]].add(
      TrainingInstances.instance(i));
    }
    for (int i = 0; i < NumberOfClusters; i++) {
      clusterCentroid = new double[TrainingInstances.numAttributes()];
      for (int j = 0; j < TrainingInstances.numAttributes(); j++) {
        clusterCentroid[j] = clusterAssignments[i].meanOrMode(j);
      }
      AverageInstances.add(new Instance(1.0, clusterCentroid));
    }
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing cluster validity indices and details about 
   * all cluster representative instances to existing and open HTML report. The 
   * text file remains open.
   */
  
  protected void createHtmlReportClusterValiditySection() {
  
    if (MyClusterValidityAssessor != null) {
      MyClusterValidityAssessor.createHtmlReportClusterValiditySection();
    }
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Creates a new HTML report file and appends the report header. 
   * The text file remains open.
   */
  
  protected void createHtmlReportHeader() {
    
    Template debuggingOutputHeader = new Template(Tools
    .stringFromTextualSystemResource("org/hypknowsys/resources/html/"
    + "HtmlFile_HeaderTemplate.html"));
    debuggingOutputHeader.addValue("${Title}", HtmlReportTitle);
    HtmlReport =  new TextFile(new File(HtmlReportFileName));
    HtmlReport.open();
    HtmlReport.setFirstLine(debuggingOutputHeader.insertValues());
    HtmlReport.setNextLine("<p>Created by " + this.getClass().getName()
    + " on " + Tools.getSystemDate() + "</p>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing parameter settings to existing and open 
   * HTML report. The text file remains open.
   */
  
  protected void createHtmlReportParameterSection() {
    
    super.createHtmlReportParameterSection();
    
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Compactify Training Instances</td>"
    + "<td align=\"left\" valign=\"top\">" + CompactifyTrainingInstances
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Size of Nearest Neighbors List</td>"
    + "<td align=\"left\" valign=\"top\">" + SizeOfNearestNeighborsList
    + "</td></tr>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the report footer to existing and open HTML report. The 
   * text file will is closed.
   */
  
  protected void createHtmlReportFooter() {
    
    HtmlReport.setNextLine(Tools.stringFromTextualSystemResource(
    "org/hypknowsys/resources/html/HtmlFile_FooterTemplate.html"));
    HtmlReport.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the similarity of two instances by looking it up in the
   * array that represents the compressed similarity matrix.
   * @param pInstanceI the first instance
   * @param pInstanceJ the second instance
   * @param pNumberOfInstances the number of instances in the training 
   * data set
   * @return the similarity of instances pInstanceI and pInstanceJ
   */
  
  protected float getSimilarity(int pInstanceI, int pInstanceJ, 
  int pNumberOfInstances) {
    
    // similarity matrix (i\j) and index of each similarity in array:
    //      0   1   2   3   4   5   6   7   8
    //  0   x   0   1   2   3   4   5   6   7
    //  1       x   8   9  10  11  12  13  14
    //  2           x  15  16  17  18  19  20
    //  3               x  21  22  23  24  25
    //  4                   x  26  27  28  29
    //  5                       x  30  31  32
    //  6                           x  33  34
    //  7                               x  35
    //  8                                   x
    //
    // 1st index per row is (i : 2) * ((size - 1) + (size - 1) - i + 1)
    // 0 =  0
    // 1 =  8 = 0 + 8
    // 2 = 15 = 0 + 8 + 7
    // 3 = 21 = 0 + 8 + 7 + 6
    // ...
    // 7 = 35 = 0 + 8 + 7 + 6 + 5 + 4 + 3 + 2
    // 1st index per row is (i : 2) * ((size - 1) + (size - 1) - i + 1)
    // hence, 1st index per row is (i : 2) * (size + size - i - 1) and
    // offset per row is (j - i - 1); thus index of (i, j) is 
    // (i : 2) * (size + size - i - 1) + (j - i - 1):
    // (0, 1) =  0 = (0 : 2) * (9 + 9 - 0 - 1) + (j - 0 - 1)
    // (0, 5) =  4 = (0 : 2) * (9 + 9 - 0 - 1) + (5 - 0 - 1)
    // (1, 2) =  8 = (1 : 2) * (9 + 9 - 1 - 1) + (2 - 1 - 1)
    // (1, 5) = 11 = (1 : 2) * (9 + 9 - 1 - 1) + (5 - 1 - 1)
    // (5, 6) = 30 = (5 : 2) * (9 + 9 - 5 - 1) + (6 - 5 - 1)
    // (5, 7) = 31 = (5 : 2) * (9 + 9 - 5 - 1) + (7 - 5 - 1)
    // (7, 8) = 35 = (7 : 2) * (9 + 9 - 7 - 1) + (8 - 7 - 1)
    
    if (pInstanceI == pInstanceJ) {
      return 1.0f;
    }
    else if (pInstanceI > pInstanceJ) {
      return getSimilarity(pInstanceJ, pInstanceI, pNumberOfInstances);
    }
    else {
      return SimilarityMatrix[(int)((pInstanceI / 2.0d) 
      * (pNumberOfInstances + pNumberOfInstances - pInstanceI - 1))
      + (pInstanceJ - pInstanceI - 1)];
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Compactifies the training data set by deleting excess duplicates (max. 
   * number of duplicates for each instance: SizeOfNearestNeighborsList).
   */
  
  protected void compactifyTrainingInstances() {
    
    if (!CompactifyTrainingInstances) {
      return;
    }
    
    this.outputLogEntry(true, "Number of initial training instances: " 
    + TrainingInstances.numInstances());
    this.updateTaskProgress(TaskProgress.INDETERMINATE,
    "Trying to Compactify the Training Data Set");
    this.outputLogEntry(true, "Trying to compactify the training data set by "
    + "deleting excess duplicates (max. number of duplicates: " 
    + SizeOfNearestNeighborsList + ")");
    
    // If an instance has more the SizeOfNearestNeighborsList duplicates,
    // remove them from the training data set because they do not add value.
    int numberOfDuplicates = 0;
    for (int i = 0; i < TrainingInstances.numInstances(); i++) {
      numberOfDuplicates = 0;
      if (i % 500 == 0) {
        this.updateTaskProgress(TaskProgress.INDETERMINATE, 
        "Compactifying the Training Data Set: " + (int)(100.0d
        * i / (double)TrainingInstances.numInstances()) + "% Completed");
        this.outputLogEntry(false, "Compactifying the training data set: "
        + (int)(100.0d * i / (double)TrainingInstances.numInstances()) 
        + "% Completed");
      }      
      for (int j = i + 1; j < TrainingInstances.numInstances(); j++) {
        if (TrainingInstances.instance(i).equalValues(TrainingInstances
        .instance(j))) {
          numberOfDuplicates++;
          if (numberOfDuplicates > SizeOfNearestNeighborsList) {
            TrainingInstances.delete(j);
          }
        }
      }
    }
    TrainingInstances.compactify();
    
    this.updateTaskProgress(TaskProgress.INDETERMINATE,
    "Performing Garbage Collection to Free Memory");
    this.outputLogEntry(true, "Number of remaining training instances: " 
    + TrainingInstances.numInstances());
    this.outputLogEntry(true, "Performing garbage collection to free memory");
    System.gc();
    
 }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Computes the similarity matrix.
   */
  
  protected void computeSimilarityMatrix() {
    
    int numberOfComputations = NumberOfTrainingInstances
    * (NumberOfTrainingInstances - 1) / 2;
    int numberOfCompletedComputations = 0;
    
    this.updateTaskProgress(TaskProgress.INDETERMINATE, "Computing the "
    + "Similarity Matrix (" + numberOfComputations + " Operations)");
    this.outputLogEntry(true, "Starting to compute the similarity matrix (" 
    + numberOfComputations + " operations)");

    SimilarityMatrix = new float[numberOfComputations];
    for (int i = 0; i < NumberOfTrainingInstances; i++) {
      // output progress information
      if (i % ((double)i / NumberOfTrainingInstances < 0.5d 
      ? 500 : 2000) == 0) {
        this.updateTaskProgress(TaskProgress.INDETERMINATE, 
        "Computing the Similarity Matrix: " + (int)(100.0d
        * numberOfCompletedComputations / (double)numberOfComputations) 
        + "% Completed");
        this.outputLogEntry(false, "Computing the similarity matrix: "
        + (int)(100.0d * numberOfCompletedComputations
        / (double)numberOfComputations) + "% completed");
      }      
      for (int j = i + 1; j < NumberOfTrainingInstances; j++) {
        // compute the similarity of instances i and j
        SimilarityMatrix[numberOfCompletedComputations++] = 1.0f
        - (float)MyDistanceMeasure.computeDistance(TrainingInstances
        .instance(i), TrainingInstances.instance(j), TrainingInstancesMetadata);
      }
    }    
    
 }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Searches for the nearest neighbors of each instance. The results are
   * stored in the 2D array NeighborhoodList. Each list is sorted by increasing
   * index number of the nearest neighbors to allow for a binary search.
   */
  
  protected void searchForNearestNeighbors() {
    
    NeighborhoodList = new int[NumberOfTrainingInstances]
    [SizeOfNearestNeighborsList];
    MinNearestNeighborSimilarity = new float[NumberOfTrainingInstances];
    float[] similarityList = new float[SizeOfNearestNeighborsList];
    float similarity = 0.0f;
    for (int i = 0; i < NumberOfTrainingInstances; i++) {
      // initialize the NN similarity threshold for each training instance
      MinNearestNeighborSimilarity[i] = 1.0f;
      for (int k = 0; k < SizeOfNearestNeighborsList; k++) {
        // initialize the nearest neighborhood list of instance i
        NeighborhoodList[i][k] = EMPTY;
      }
    }    

    this.updateTaskProgress(TaskProgress.INDETERMINATE, "Searching for the " 
    + SizeOfNearestNeighborsList + " Nearest Neighbors of Each Instance");
    this.outputLogEntry(true, "Searching for the " + SizeOfNearestNeighborsList 
    + " nearest neighbors of each instance");

    // determine SizeOfNearestNeighborsList nearest neighbors for each instance
    for (int i = 0; i < NumberOfTrainingInstances; i++) {
      // initialize similarities of potential neighbors to instance i
      for (int j = 0; j < SizeOfNearestNeighborsList; j++) {
        similarityList[j] = 0.0f;
      }      
      // iterate through all neighbors of current instance i
      for (int j = 0; j < NumberOfTrainingInstances; j++) {
        similarity = this.getSimilarity(i, j, NumberOfTrainingInstances);
        // The list of potential neighbors is ordered by decreasing similarity.
        // The list of potential neighbors must not contain the instance itself.
        // If there are more than SizeOfNearestNeighborsList equal instances,
        // the list of nearest neighbors comprises only the first 
        // SizeOfNearestNeighborsList because of the 'greater than' operator. 
        if (similarity > similarityList[similarityList.length - 1] && i != j) {
          // search the insertion point in list of potential neighbors
          for (int k = 0; k < similarityList.length; k++) {
            if (similarity > similarityList[k]) {
              // shift subsequent entries to the right
              for (int l = similarityList.length - 1; l > k; l--) {
                NeighborhoodList[i][l] = NeighborhoodList[i][l - 1];
                similarityList[l] = similarityList[l - 1];
              }
              // insert new entry at the entry point
              NeighborhoodList[i][k] = j;
              similarityList[k] = similarity;
              k = similarityList.length;  // stop iteration
            }
          }
        }
      }
      // set minimum required similarity of nearest neighbors
      for (int j = similarityList.length - 1; j >= 0; j--) {
        if (similarityList[j] > 0.0f) {
          MinNearestNeighborSimilarity[i] = similarityList[j];
          j = -1;  // stop iteration
        }
      }
      // sort list of nearest neighbors for subsequent access
      Arrays.sort(NeighborhoodList[i]);    
    }

 }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Computes the number of shared nearest neighbors for all pairs of
   * instances and re-uses the similarity matrix to store the results.
   */
  
  protected void computeNumberOfSharedNearestNeighbors() {
    
    this.updateTaskProgress(TaskProgress.INDETERMINATE,
    "Computing the Number of Shared Nearest Neighbors");
    this.outputLogEntry(true, "Computing the number of shared nearest "
    + "neighbors");

    // re-use similarity matrix too store the number of shared
    // nearest neighbors for each pair of instances
    int numberOfCompletedComputations = 0;
    int numberOfSharedNearestNeighbors = 0;
    for (int i = 0; i < NumberOfTrainingInstances; i++) {
      for (int j = i + 1; j < NumberOfTrainingInstances; j++) {
        numberOfSharedNearestNeighbors = 0;
        for (int ki = 0, kj = 0; ki < SizeOfNearestNeighborsList
        && kj < SizeOfNearestNeighborsList;) {
          if (NeighborhoodList[i][ki] == NeighborhoodList[j][kj]) {
            numberOfSharedNearestNeighbors++;
            ki++; kj++;
          }
          else if (NeighborhoodList[i][ki] > NeighborhoodList[j][kj]) {
            kj++;
          }
          else {
            ki++;
          }
        }
        SimilarityMatrix[numberOfCompletedComputations++]
        = numberOfSharedNearestNeighbors;
      }
    }

 }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Clusters an instance. Prerequisite: pInstance must have been pre-processed
   * analogously to the training instances.
   *
   * @param pInstance the instance to assign a cluster to
   * @return a cluster number
   */
  
  protected abstract int clusterProcessedInstance(Instance pInstance);
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Searches the nearest neighbors of pInstance in the application phase.
   * The result is ordered by increasing instance ID and stored in the array 
   * ApplNeighborhoodList. This method is typically called by the
   * method this.clusterProcessedInstance().
   *
   * @param pInstance the instance whose nearest neighbors have to be search 
   * for in the application phase
   */
  
  protected void searchForNearestNeighbors(Instance pInstance) {

    // determine nearest neighbors for pInstance in accordance with the 
    // assignment of training instances to clusters
    for (int j = 0; j < SizeOfNearestNeighborsList; j++) {
      ApplNeighborhoodList[j] = EMPTY;
      ApplSimilarityList[j] = 0.0f;
    }
    // iterate through all possible neighbors of pInstance
    for (int j = 0; j < NumberOfTrainingInstances; j++) {
      ApplNearestNeighborSimilarities[j] = 1.0f 
      - (float)MyDistanceMeasure.computeDistance(pInstance, 
      TrainingInstances.instance(j), TrainingInstancesMetadata);
      ApplSimilarity = ApplNearestNeighborSimilarities[j];
      // The list of potential neighbors is ordered by decreasing similarity.
      // The list of potential neighbors must not contain the instance itself.
      // If there are more than SizeOfNearestNeighborsList equal instances,
      // the list of nearest neighbors comprises only the first
      // SizeOfNearestNeighborsList because of the 'greater than' operator.
      if (ApplSimilarity > ApplSimilarityList
      [ApplSimilarityList.length - 1]) {
        // search the insertion point in list of potential neighbors
        for (int k = 0; k < ApplSimilarityList.length; k++) {
          if (ApplSimilarity > ApplSimilarityList[k]) {
            // shift subsequent entries to the right
            for (int l = ApplSimilarityList.length - 1; l > k; l--) {
              ApplNeighborhoodList[l] = ApplNeighborhoodList
              [l - 1];
              ApplSimilarityList[l] = ApplSimilarityList[l - 1];
            }
            // insert new entry at the entry point
            ApplNeighborhoodList[k] = j;
            ApplSimilarityList[k] = ApplSimilarity;
            k = ApplSimilarityList.length;  // stop iteration
          }
        }
      }
    }
    // sort list of nearest neighbors for subsequent access
    Arrays.sort(ApplNeighborhoodList);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
}