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

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import org.hypknowsys.algorithms.core.AbstractOptionHandler;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.algorithms.core.Option;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.TaskProgress;

/**
 * Based on weka.clusterers.SimpleKMeans, Revision 1.9:
 * Copyright (C) 2000 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * This class implements the bisecting k-means clustering algorithm as described
 * in Steinbach, Karypis, and Kumar: A Comparison of Document Clustering
 * Techniques. In: Workshop on Text Mining at the Sixth ACM SIGKDD International
 * Conference on Knowledge Discovery and Data Mining, Boston, MA, USA,
 * pp. 109--110, August 2000.<p>
 *
 * Training and applications instances should be pre-processed outside this
 * algorithm instance, but using the exactly the same process workflow for both
 * training and application data. According to the chosen distance measure,
 * STRING and DATE attributes might be ignored for computing the distance
 * between two instances. Check the documentation of the respective distance
 * measure.
 *
 * Valid options are:<p>
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
 * -N <number of clusters> <br>
 * Specify the number of clusters to generate, default: 2. <p>
 *
 * -S <seed> <br>
 * Specify the seed of the random number generator, default: 10. <p>
 *
 * -maxIterations <max. number of iterations> <br>
 * Specify the maximum number of k-means iterations, default: Integer.MAX_VALUE.
 * <p>
 *
 * -minClusterCardinality <min. cluster cardinality> <br>
 * Specify the desired minimum cluster cardinality, default: 5. <p>
 *
 * -maxRetriesPerBisectingPass <max. number of retries> <br>
 * Specify the maximum number of retries per bisecting pass, default: 20. <p>
 *
 * @version 0.1, 18 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 * @see Clusterer
 * @see OptionHandler
 */

public class BisectingKMeans extends AbstractKMeans {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * ID of cluster containing only missing or zero values or -1, if
   * this cluster does not exist.
   */
  protected int EmptyInstancesCluster = -1;
  
  /**
   * Minimum number of instances that should be assigned to each cluster
   * in each bisecting pass. In each bisecting pass, the algorithm retries
   * a this.MaxRetriesInitialBisectingSelection times to ensure this minimum
   * cluster cardinality for both resulting clusters.
   */
  protected int MinClusterCardinality = DEFAULT_MIN_CLUSTER_CARDINALITY;
  
  /**
   * Maximum number of retries in each bisecting pass to select new
   * initial cluster centroids.
   */
  protected int MaxRetriesPerBisectingPass
  = DEFAULT_MAX_RETRIES_PER_BISECTING_PASS;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Temporary variable indicating for each cluster, whether a further
   * split within an bisecting pass is allowed. Splitting a cluster is
   * not allowed, if the preceding split resulted in at least one empty
   * cluster.
   */
  protected transient boolean[] ClusterSplitAllowed = null;
  
  /**
   * Temporary variable holding number of instances assigned to the cluster
   * selected for splitting in the current bisecting pass
   */
  protected transient int CardinalityOfClusterToSplit = 0;
  
  /**
   * Temporary double variable for improving performance only
   */
  private transient double TmpDoubleValue = Double.NaN;
  
  /**
   * Temporary boolean variable for improving performance only
   */
  private transient boolean TmpBooleanValue = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Default setting of attribute this.MinClusterCardinality
   */
  protected static final int DEFAULT_MIN_CLUSTER_CARDINALITY = 5;
  
  /**
   * Default setting of attribute this.MaxRetriesPerBisectingPass
   */
  protected static final int DEFAULT_MAX_RETRIES_PER_BISECTING_PASS = 20;
  
  /**
   * Temporary double variable (distance) for improving performance only
   */
  private transient double TmpDistance = Double.NaN;
  
  /**
   * Temporary double variable (min. distance) for improving performance only
   */
  private transient double TmpMinDistance = Double.NaN;
  
  /**
   * Temporary double variable (best cluster ID) for improving performance only
   */
  private transient int TmpBestClusterID = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the desired minimum cluster cardinality. Clusters with
   * cardinality less than or equal to this.MinClusterCardinality will
   * not be further split in the bisecting pass. Additionally, this
   * algorithm tries up to this.MaxRetriesPerBisectingPass times to
   * find a new clustering by selecting two new initial centroids,
   * if the bisecting pass results in a cluster whose cardinality is
   * less than or equal to this.MinClusterCardinality. If the algorithm
   * fails, cluster with cardinality less that this.MinClusterCardinality
   * are accepted to avoid a deadlook.
   *
   * @return the desired minimum cluster cardinality.
   */
  public int getMinClusterCardinality() {
    
    return MinClusterCardinality;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the maximum number of retries per bisecting pass. This
   * algorithm tries up to this.MaxRetriesPerBisectingPass times to
   * find a new clustering by selecting two new initial centroids,
   * if the bisecting pass results in a cluster whose cardinality is
   * less than or equal to this.MinClusterCardinality. If the algorithm
   * fails, cluster with cardinality less that this.MinClusterCardinality
   * are accepted to avoid a deadlook.
   *
   * @return the maximum number of retries per bisecting pass.
   */
  public int getMaxRetriesPerBisectingPass() {
    
    return MaxRetriesPerBisectingPass;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the desired minimum cluster cardinality, default: 5. Clusters with
   * cardinality less than or equal to this.MinClusterCardinality will
   * not be further split in the bisecting pass. Additionally, this
   * algorithm tries up to this.MaxRetriesPerBisectingPass times to
   * find a new clustering by selecting two new initial centroids,
   * if the bisecting pass results in a cluster whose cardinality is
   * less than or equal to this.MinClusterCardinality. If the algorithm
   * fails, cluster with cardinality less that this.MinClusterCardinality
   * are accepted to avoid a deadlook.
   *
   * @param pMinClusterCardinality the desired minimum cluster cardinality.
   */
  
  public void setMinClusterCardinality(int pMinClusterCardinality) {
    
    if (pMinClusterCardinality > 0) {
      MinClusterCardinality = pMinClusterCardinality;
    }
    else {
      MinClusterCardinality = DEFAULT_MIN_CLUSTER_CARDINALITY;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the maximum number of retries per bisecting pass, default: 20.
   *
   * @param pMaxRetriesPerBisectingPass the maximum number of retries per
   * bisecting pass.
   */
  
  public void setMaxRetriesPerBisectingPass(int pMaxRetriesPerBisectingPass) {
    
    if (pMaxRetriesPerBisectingPass > 0) {
      MaxRetriesPerBisectingPass = pMaxRetriesPerBisectingPass;
    }
    else {
      MaxRetriesPerBisectingPass = DEFAULT_MAX_RETRIES_PER_BISECTING_PASS;
    }
    
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
    
    return super.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface OptionHandler methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an enumeration describing the available options. <p>
   *
   * Valid options are:<p>
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
   * -N <number of clusters> <br>
   * Specify the number of clusters to generate, default: 2. <p>
   *
   * -S <seed> <br>
   * Specify the seed of the random number generator, default: 10. <p>
   *
   * -maxIterations <max. number of iterations> <br>
   * Specify the maximum number of k-means iterations, default: 
   * Integer.MAX_VALUE. <p>
   *
   * -minClusterCardinality <min. cluster cardinality> <br>
   * Specify the desired minimum cluster cardinality, default: 5. <p>
   *
   * -maxRetriesPerBisectingPass <max. number of retries> <br>
   * Specify the maximum number of retries per bisecting pass, default: 20. <p>
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
    "\tSpecify the desired minimum cluster cardinality, default: 5.",
    "minClusterCardinality", 1,
    "-minClusterCardinality <min. cluster cardinality>"));
    newVector.addElement(new Option(
    "\tSpecify the maximum number of retries per bisecting pass, default: 20.",
    "maxRetriesPerBisectingPass", 1,
    "-maxRetriesPerBisectingPass <max. number of retries>"));
    
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
    "minClusterCardinality", pOptions);
    this.setMinClusterCardinality(DEFAULT_MIN_CLUSTER_CARDINALITY);
    if (optionString.length() != 0) {
      this.setMinClusterCardinality(Tools.string2Int(optionString));
    }
    
    optionString = AbstractOptionHandler.getOption(
    "maxRetriesPerBisectingPass", pOptions);
    this.setMaxRetriesPerBisectingPass(DEFAULT_MAX_RETRIES_PER_BISECTING_PASS);
    if (optionString.length() != 0) {
      this.setMaxRetriesPerBisectingPass(Tools.string2Int(optionString));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the current settings of BisectingKMeans.
   *
   * @return an array of strings suitable for passing to setOptions()
   */
  
  public String[] getOptions() {
    
    String[] superOptions = super.getOptions();
    
    int numberOfOptions = superOptions.length + 4;
    if (MinClusterCardinality != DEFAULT_MIN_CLUSTER_CARDINALITY) {
      numberOfOptions += 2;
    }
    if (MaxRetriesPerBisectingPass != DEFAULT_MAX_RETRIES_PER_BISECTING_PASS) {
      numberOfOptions += 2;
    }
    String[] options = new String[numberOfOptions];
    int current = 0;
    
    for (int i = 0; i < superOptions.length; i++) {
      options[current++] = superOptions[i];
    }
    if (MinClusterCardinality != DEFAULT_MIN_CLUSTER_CARDINALITY) {
      options[current++] = "-minClusterCardinality";
      options[current++] = "" + this.getMinClusterCardinality();
    }
    if (MaxRetriesPerBisectingPass != DEFAULT_MAX_RETRIES_PER_BISECTING_PASS) {
      options[current++] = "-maxRetriesPerBisectingPass";
      options[current++] = "" + this.getMaxRetriesPerBisectingPass();
    }
    
    while (current < options.length) {
      options[current++] = "";
    }
    
    return options;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## abstract class Clusterer methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Creates an initial set of cluster centroids. At the beginning, two cases
   * are distinguished: If the data set does not comprise instances that only
   * contain missing or zero values, all instances are assigned to the first
   * cluster (ID = 0). Otherwise, all instances containing only missing or zero
   * values are assigned to the first cluster (ID = 0), wheras the remaining
   * instances are assigned to the second cluster (ID = 1).
   */
  
  /**
   * Executes the bisecting k-means clustering algorithm.<p>
   *
   * Concerning the initial set of cluster centroids, two cases are
   * distinguished: If the data set does not comprise instances that only
   * contain missing or zero values, all instances are assigned to the first
   * cluster (ID = 0). Otherwise, all instances containing only missing or zero
   * values are assigned to the first cluster (ID = 0), wheras the remaining
   * instances are assigned to the second cluster (ID = 1).
   *
   */
  
  protected void executeClusteringAlgorithm() {
    
    ClusterCentroids = new Instances(TrainingInstances, NumberOfClusters);
    ClusterAssignments = new int [TrainingInstances.numInstances()];
    int emptyInstances = 0, nonEmptyInstances = 0;
    for (int i = 0; i < ClusterAssignments.length; i++) {
      if (this.isEmptyInstance(TrainingInstances.instance(i))) {
        ClusterAssignments[i] = 0; emptyInstances++;
      }
      else {
        ClusterAssignments[i] = 1; nonEmptyInstances++;
      }
    }
    
    // compute cluster centroid(s) that represents the initial cluster(s)
    // by distinguishing two cases
    double[] newClusterCentroid = new double[TrainingInstances.numAttributes()];
    ClusterCardinality = new int [NumberOfClusters];
    for (int i = 1; i < ClusterCardinality.length; i++) {
      ClusterCardinality[i] = 0;
    }
    if (emptyInstances == 0) {
      // ClusterAssignments must be corrected, because there is only one
      // initial cluster centroid representing the entire training data set
      EmptyInstancesCluster = -1;
      for (int i = 0; i < ClusterAssignments.length; i++) {
        ClusterAssignments[i] = 0;
      }
      for (int i = 0; i < TrainingInstances.numAttributes(); i++) {
        newClusterCentroid[i] = TrainingInstances.meanOrMode(i);
      }
      ClusterCentroids.add(new Instance(1.0, newClusterCentroid));
      ClusterCardinality[0] = TrainingInstances.numInstances();
    }
    else {
      // There are two initial cluster centroids. The first cluster centroid
      // contains only empty instances:
      EmptyInstancesCluster = 0;
      for (int i = 0; i < TrainingInstances.numAttributes(); i++) {
        newClusterCentroid[i] = 0.0d;
      }
      ClusterCentroids.add(new Instance(1.0, newClusterCentroid));
      ClusterCardinality[0] = emptyInstances;
      // instantiate new array to avoid 'call by reference' problems
      newClusterCentroid = new double[TrainingInstances.numAttributes()];
      for (int i = 0; i < TrainingInstances.numAttributes(); i++) {
        newClusterCentroid[i] = TrainingInstances.meanOrMode(i);
      }
      ClusterCentroids.add(new Instance(1.0, newClusterCentroid));
      ClusterCardinality[1] = nonEmptyInstances;
    }
    
    // All clusters can be further split at the beginning:
    ClusterSplitAllowed = new boolean[NumberOfClusters];
    for (int i = 0; i < ClusterSplitAllowed.length; i++) {
      ClusterSplitAllowed[i] = true;
    }
    
    // Iteratively reassigns training instances according to bisecting
    // k-means algorithm:
    
    // variables for bisecting k-means
    int clusterToSplit = 0;
    
    // variabels for simple k-means in bisecting step
    RandomNumberGenerator = new Random(Seed);
    boolean converged = false;
    int numberOfReassignments = 0;
    int newClusterAssignment = 0;
    Instances[] newClusterAssignments = null;
    Instance firstBisectingCentroid = null;
    Instance secondBisectingCentroid = null;
    
    clusterToSplit = this.pickClusterToSplit();
    while (ClusterCentroids.numInstances() < NumberOfClusters
    && clusterToSplit >= 0) {
      
      this.outputLogEntry(true, "Bisecting pass " + ClusterCentroids
      .numInstances() + ": Splitting cluster " + clusterToSplit 
      + " comprising " + CardinalityOfClusterToSplit + " instances.");
      
      // Split cluster with ID = clusterToSplit in two new clusters
      // using simple k-means:
      
      // Bisecting step: Iteratively assigned all instances allocated to cluster
      // with ID = clusterToSplit to the two new centroids:
      int retriesPerBisectingPass = 0;
      boolean selectInitialInstances = true;
      Iterations = 0;
      converged = false;
      while (!converged && Iterations <= MaxNumberOfIterations
      && retriesPerBisectingPass < MaxRetriesPerBisectingPass) {
        
        if (selectInitialInstances) {
          // Bisecting step: Select initial k = 2 clustering from instances
          // assigned to cluster with ID = clusterToSplit:
          int selected = -1;
          int instIndex = -1;
          do {
            instIndex = Math.abs(RandomNumberGenerator.nextInt())
            % TrainingInstances.numInstances();
          } while (!(ClusterAssignments[instIndex] == clusterToSplit
          || ClusterAssignments[instIndex] < 0));
          firstBisectingCentroid = TrainingInstances.instance(instIndex);
          selected = instIndex;
          // System.out.println("firstBisectingCentroid = " + instIndex
          // + " in cluster " + ClusterAssignments[instIndex]);
          do {
            instIndex = Math.abs(RandomNumberGenerator.nextInt())
            % TrainingInstances.numInstances();
          } while (!(ClusterAssignments[instIndex] == clusterToSplit
          || ClusterAssignments[instIndex] < 0 || selected == instIndex));
          secondBisectingCentroid = TrainingInstances.instance(instIndex);
          // System.out.println("secondBisectingCentroid = " + instIndex
          // + " in cluster " + ClusterAssignments[instIndex]);
          // System.out.println("ClusterCentroids.numInstances() = "
          // + ClusterCentroids.numInstances());
          selectInitialInstances = false;
          retriesPerBisectingPass++;
        }
        
        Iterations++;
        converged = true;
        numberOfReassignments = 0;
        for (int i = 0; i < TrainingInstances.numInstances(); i++) {
          // Consider instances assigned to Cluster with ID = clusterToSplit
          // (i.e., in first k-means iteration) or instances assigned to one
          // of the bisecting clusters with ID = -1 and ID = -2 (i.e., in
          // iterations 2, 3, ...):
          if (ClusterAssignments[i] == clusterToSplit
          || ClusterAssignments[i] < 0) {
            newClusterAssignment = (-1) - bisectingAssignProcessedInstance(
            TrainingInstances.instance(i), firstBisectingCentroid,
            secondBisectingCentroid);
            if (newClusterAssignment != ClusterAssignments[i]) {
              converged = false;
              numberOfReassignments++;
            }
            ClusterAssignments[i] = newClusterAssignment;
          }
        }
        this.updateTaskProgress(TaskProgress.INDETERMINATE,
        "Bisecting Pass " + ClusterCentroids.numInstances() + ": "
        + "Iteration " + Iterations + " of 2-Means Split "
        + retriesPerBisectingPass);
        this.outputLogEntry(false, "Bisecting pass " + ClusterCentroids
        .numInstances() + ": " + "Iteration " + Iterations 
        + " of 2-means split " + retriesPerBisectingPass);
        
        // Bisecting step: In each iteration, update the two cluster centroids
        // according to new cluster assignments:
        newClusterAssignments = new Instances[2];
        newClusterAssignments[0] = new Instances(TrainingInstances, 0);
        newClusterAssignments[1] = new Instances(TrainingInstances, 0);
        for (int i = 0; i < TrainingInstances.numInstances(); i++) {
          if (ClusterAssignments[i] == -1) {
            newClusterAssignments[0].add(TrainingInstances.instance(i));
          }
          else if (ClusterAssignments[i] == -2) {
            newClusterAssignments[1].add(TrainingInstances.instance(i));
          }
        }
        if (newClusterAssignments[0].numInstances() == 0
        || newClusterAssignments[1].numInstances() == 0) {
          // retry with same assignments, because one cluster is empty
          selectInitialInstances = true;
          Iterations = 0;
          converged = false;          
          // System.out.println("Retry clustering same assignments using two "
          // + "different initial centroids");
        }
        else {
          // create new assignment, because both cluster are non-empty
          newClusterCentroid = new double[TrainingInstances.numAttributes()];
          for (int j = 0; j < TrainingInstances.numAttributes(); j++) {
            newClusterCentroid[j] = newClusterAssignments[0].meanOrMode(j);
          }
          firstBisectingCentroid = new Instance(1.0, newClusterCentroid);
          // instantiate new array to avoid 'call by reference' problems
          newClusterCentroid = new double[TrainingInstances.numAttributes()];
          for (int j = 0; j < TrainingInstances.numAttributes(); j++) {
            newClusterCentroid[j] = newClusterAssignments[1].meanOrMode(j);
          }
          secondBisectingCentroid = new Instance(1.0, newClusterCentroid);
          // System.out.println("newClusterAssignments[0].numInstances() = "
          // + newClusterAssignments[0].numInstances());
          // System.out.println("newClusterAssignments[1].numInstances() = "
          // + newClusterAssignments[1].numInstances());
        }
        
      }  // while (!converged && Iterations <= MaxNumberOfIterations)
      
      for (int i = 0; i < ClusterCardinality.length; i++) {
        ClusterCardinality[i] = 0;
      }
      // The bi-secting step depends on the success of current bi-section.
      if (newClusterAssignments[0].numInstances() == 0
      || newClusterAssignments[1].numInstances() == 0) {
        // Bi-secting step: The previous bi-secting step was unsuccessful
        // because one new centroid is empty and the other is an exact copy 
        // of clusterToSplit. Hence, prevent clusterToSplit from being
        // selected for splitting once again.
        ClusterSplitAllowed[clusterToSplit] = false;
        for (int i = 0; i < ClusterAssignments.length; i++) {
          if (ClusterAssignments[i] == -2 || ClusterAssignments[i] == -1) {
            ClusterAssignments[i] = clusterToSplit;
          }
          ClusterCardinality[ClusterAssignments[i]]++;
        }
      }
      else {
        // Bisecting step: Delete clusterToSplit from ClusterCentroids and add
        // firstBisectingCentroid and secondBisectingCentroid.
        ClusterCentroids.delete(clusterToSplit);
        ClusterCentroids.add(firstBisectingCentroid);
        ClusterCentroids.add(secondBisectingCentroid);
        // Bisecting step: Update ClusterAssignments and ClusterCardinality due
        // to deletion of cluster with ID = clusterToSplit from ClusterCentroids
        for (int i = 0; i < ClusterAssignments.length; i++) {
          if (ClusterAssignments[i] > clusterToSplit) {
            ClusterAssignments[i]--;
          }
          else if (ClusterAssignments[i] == -2) {
            ClusterAssignments[i] = ClusterCentroids.numInstances() - 1;
          }
          else if (ClusterAssignments[i] == -1) {
            ClusterAssignments[i] = ClusterCentroids.numInstances() - 2;
          }
          ClusterCardinality[ClusterAssignments[i]]++;
        }
        // Bisecting step: Update ClusterSplitAllowed due to deletion of cluster
        // with ID = clusterToSplit from ClusterCentroids:
        for (int i = clusterToSplit; i < (ClusterSplitAllowed.length - 1); 
        i++) {
          ClusterSplitAllowed[i] = ClusterSplitAllowed[i + 1];
        }
        // Remember that current split was unsuccessful:
        if (selectInitialInstances) {
          ClusterSplitAllowed[ClusterCentroids.numInstances() - 1] = false;
          ClusterSplitAllowed[ClusterCentroids.numInstances() - 2] = false;
        }
        else {
          ClusterSplitAllowed[ClusterCentroids.numInstances() - 1] = true;
          ClusterSplitAllowed[ClusterCentroids.numInstances() - 2] = true;
        }
      }
      
      clusterToSplit = this.pickClusterToSplit();
      
      // for (int j = 0; j < ClusterCardinality.length; j++) {
      //   System.out.println("BisectingPass=" + ClusterCentroids.numInstances()
      //   + "; clusterToSplit=" + clusterToSplit
      //   + "; ClusterCardinality[" + j + "]=" + ClusterCardinality[j]
      //   + "; ClusterSplitAllowed[" + j + "]=" + ClusterSplitAllowed[j]);
      // }
    
    }  // while (ClusterCentroids.numInstances() < NumberOfClusters)
    
    // correct the number of clusters to cater for null clusters
    for (int i = NumberOfClusters - 1; i >= 0; i--) {
      if (ClusterCentroids.instance(i) == null) {
        NumberOfClusters--;
      }
      else {
        break;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Clusters an instance in the bisecting step by considering only
   * centroids of the two specified clusters.
   *
   * @param pInstance the instance to assign a cluster to
   * @param pFirstBisectingCentroid the centroid of the first cluster
   * @param pSecondBisectingCentroid the centroid of the seconds cluster
   * considered
   * @return 0, if pInstance is assigned to pFirstBisectingCentroid, or
   * 1, if pInstance is assigned to pSecondBisectingCentroid.
   */
  
  protected int bisectingAssignProcessedInstance(Instance pInstance,
  Instance pFirstBisectingCentroid, Instance pSecondBisectingCentroid) {
    
    if (MyDistanceMeasure.computeDistance(pInstance,
    pFirstBisectingCentroid, TrainingInstancesMetadata)
    < MyDistanceMeasure.computeDistance(pInstance,
    pSecondBisectingCentroid, TrainingInstancesMetadata)) {
      return 0;
    }
    else {
      return 1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Picks a cluster to be further split in the bisecting step. An existing
   * cluster cluster containing only empty instances is not selected. Clusters
   * with cardinality less than or equal to this.MinClusterCardinality are
   * no selected for further splitting.
   *
   * @return the cluster ID of the cluster to be split or -1, if no cluster
   * satisfies splitting criterion.
   */
  
  protected int pickClusterToSplit() {
    
    CardinalityOfClusterToSplit = 0;
    int clusterToSplit = -1;
    for (int i = 0; i < ClusterCardinality.length
    && i < ClusterCentroids.numInstances(); i++) {
      if (ClusterCardinality[i] > MinClusterCardinality
      && ClusterCardinality[i] > CardinalityOfClusterToSplit
      && i != EmptyInstancesCluster && ClusterSplitAllowed[i]) {
        CardinalityOfClusterToSplit = ClusterCardinality[i];
        clusterToSplit = i;
      }
    }
    
    return clusterToSplit;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing parameter settings to existing and open
   * HTML report. The text file remains open.
   */
  
  protected void createHtmlReportParameterSection() {
    
    super.createHtmlReportParameterSection();
    
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Min. Cluster Cardinality</td>"
    + "<td align=\"left\" valign=\"top\">" + MinClusterCardinality
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Max. Retries per Bisecting Pass</td>"
    + "<td align=\"left\" valign=\"top\">" + MaxRetriesPerBisectingPass
    + "</td></tr>");
    HtmlReport.setNextLine("</table>");
    
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
  
  /**
   * Main method for testing this class.
   *
   * @param pOptions should contain the following arguments: <p>
   * -t training file 
   * [-distance <euclidean|cosine|extendedJaccard|extendedDice>]
   * [-assessIntValidity <full|true|quick|false>]
   * [-verbose <true|false>]
   * [-htmlReport <HTML report file name>]
   * [-N <number of clusters>]
   * [-S <seed>]
   * [-maxIterations <max. number of iterations>]
   * [-minClusterCardinality <min. cluster cardinality>]
   * [-maxRetriesPerBisectingPass <max. number of retries>]
   */
  
  public static void main(String[] pOptions) {
    
    try {
      System.out.println(ClusterEvaluation.evaluateClusterer(
      new BisectingKMeans(), pOptions));
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
  }
  
}