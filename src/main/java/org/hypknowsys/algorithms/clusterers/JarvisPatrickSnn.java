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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import org.hypknowsys.algorithms.core.AbstractOptionHandler;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Option;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.TaskProgress;


/**
 * Class implementing the Jarvis-Patrick clustering algorithm. Source:
 * R. A. Jarvis and E. A. Patrick. Clustering Using a Similarity Measure 
 * Based on Shared Nearest Neighbors. IEEE Transactions on Computers, 
 * C-22(11):1025-1034, November 1973.<p> 
 *
 * The Jarvis-Patrick clustering algorithm uses a nearest neighbor approach to 
 * clustering instances. This algorihtm requires a distance measure between 
 * instances and two integers that represent the size of the neighborhood list 
 * (i.e., sizeOfNnList) and the minimum number of shared neighbors (i.e., 
 * numberOfSnn). In the first step, the sizeOfNnList nearest neighbors are 
 * determined for each instance in the data set. Subsequently, two instances 
 * are assigned to the same cluster (i) if they are contained in each other's 
 * nearest neighbor list and (ii) if they share at least numberOfSnn nearest 
 * neighbors.<p>
 *
 * For reasons of efficiency, the training data set should not comprise more 
 * than sizeOfNnList duplicates of each instance. If it does, excess duplicate 
 * instances are removed prior to clustering the data set. Training and 
 * applications instances should be pre-processed outside this algorithm 
 * instance, but using the exactly the same process workflow for both training 
 * and application data. According to the chosen distance measure, STRING and 
 * DATE attributes might be ignored for computing the distance between two 
 * instances. Check the documentation of the respective distance measure.<p>
 *
 * Valid options of the Jarvis-Patrick clustering algorithm are:<p>
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
 * -numberOfSnn <integer> <br>
 * Specify the minimum number of shared nearest neighbors for two instances
 * to be assigned to the same cluster, default: 20. <p>
 *
 * @version 0.1, 10 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 * @see Clusterer
 * @see AbstractSnn
 * @see OptionHandler
 * @see Algorithm
 */

public class JarvisPatrickSnn extends AbstractSnn {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Minimum number of shared nearest neighbors for two instances to be assigned
   * to the same cluster, NumberOfSharedNearestNeighbors must be significantly
   * smaller than SizeOfNearestNeighborsList, default: 20
   */
  protected int NumberOfSharedNearestNeighbors 
  = DEFAULT_NUMBER_OF_SHARED_NEAREST_NEIGHBORS;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Default setting of attribute this.NumberOfSharedNearestNeighbors
   */
  protected static final int DEFAULT_NUMBER_OF_SHARED_NEAREST_NEIGHBORS = 20;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the minimum number of common nearest neighbors for two instances
   * to be assigned to the same cluster.
   *
   * @return the minimum number of common nearest neighbors.
   */
  public int getNumberOfSharedNearestNeighbors() {
    
    return NumberOfSharedNearestNeighbors;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the minimum number of common nearest neighbors for two instances
   * to be assigned to the same cluster.
   *
   * @param pNumberOfSharedNearestNeighbors the number of common nearest 
   * neighbors
   */
  public void setNumberOfSharedNearestNeighbors(
  int pNumberOfSharedNearestNeighbors) {
    
    NumberOfSharedNearestNeighbors = pNumberOfSharedNearestNeighbors;
    
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
   * Valid options of the Jarvis-Patrick clustering algorithm are:<p>
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
   * -numberOfSnn <integer> <br>
   * Specify the minimum number of shared nearest neighbors for two instances
   * to be assigned to the same cluster, default: 20. <p>
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
    "\tSpecify the minimum number of shared nearest neighbors for two instances"
    + " to be assigned to the same cluster, default: 20.", 
    "numberOfSnn", 1, "-numberOfSnn <integer>"));
        
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
    "numberOfSnn", pOptions);
    this.setNumberOfSharedNearestNeighbors(
    DEFAULT_NUMBER_OF_SHARED_NEAREST_NEIGHBORS);
    if (optionString.length() != 0) {
      this.setNumberOfSharedNearestNeighbors(Tools.string2Int(optionString));
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
    
    int numberOfOptions = superOptions.length + 2;
    String[] options = new String[numberOfOptions];
    int current = 0;

    for (int i = 0; i < superOptions.length; i++) {
      options[current++] = superOptions[i];
    }
    options[current++] = "-numberOfSnn";
    options[current++] = "" + this.getNumberOfSharedNearestNeighbors();
    
    while (current < options.length) {
      options[current++] = "";
    }
    
    return options;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Algorithm methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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
   * Executes the JarvisPatrick SNN clustering algorithm.
   */
  
  protected void executeClusteringAlgorithm() {
    
    NumberOfClusters = 0;
    this.compactifyTrainingInstances();    
    NumberOfTrainingInstances = TrainingInstances.numInstances();
    int numberOfComputations = NumberOfTrainingInstances 
    * (NumberOfTrainingInstances - 1) / 2;
    
    Runtime myRuntime = Runtime.getRuntime();    
    long maxMemory = myRuntime.maxMemory() / 1024 / 1024;
    long freeMemory = myRuntime.freeMemory() / 1024 / 1024;
    long totalMemory = myRuntime.totalMemory() / 1024 / 1024;
    long availableMemory = (maxMemory - totalMemory) + freeMemory;
    // size of float array is approx. 4 * (Length) + 14 +/- 2: 
    // SimilarityMatrix and three other arrays: NeighborhoodList, 
    // SimilarityList, and MinNearestNeighborSimilarity
    long necessaryMemory = (4 * numberOfComputations 
    + 4 * 3 * NumberOfTrainingInstances) / 1024 / 1024;
    if (availableMemory < necessaryMemory + 10) {
      this.updateTaskProgress(TaskProgress.INDETERMINATE, 
      "Error: Insufficient Memory for Similarity Matrix");
      this.outputLogEntry(true, "Error: The size of the similarity matrix"
      + " (" + (necessaryMemory + 10) + " MB) exceeds the available memory (" 
      + availableMemory + " MB). Reduce the size of the input data set!");
      return;
    }

    this.computeSimilarityMatrix();    
    
    this.searchForNearestNeighbors();
    
    this.computeNumberOfSharedNearestNeighbors();
    
    this.updateTaskProgress(TaskProgress.INDETERMINATE,
    "Assigning Instances to Clusters based on SNN");
    this.outputLogEntry(true, "Assigning instances to clusters based on SNN");

    // assign all clusters to cluster 0 that serves as the noise cluster
    int nextClusterID = 0;
    ClusterAssignments = new int[NumberOfTrainingInstances];
    for (int i = 0; i < ClusterAssignments.length; i++) {
      ClusterAssignments[i] = nextClusterID;
    }
    nextClusterID++;
    // determine the cluster assignment of each instance accoring to SNN
    int numberOfCompletedComputations = 0;
    int find = 0, replace = 0;
    for (int i = 0; i < NumberOfTrainingInstances; i++) {
      for (int j = i + 1; j < NumberOfTrainingInstances; j++) {
        // two clustering criteria: j must be a nearest neighbor of i and vice 
        // versa as well as i and j must have a minimum number of shared nearest
        // neighbors
        if (SimilarityMatrix[numberOfCompletedComputations] 
        >= NumberOfSharedNearestNeighbors 
        && Arrays.binarySearch(NeighborhoodList[i], j) >= 0
        && Arrays.binarySearch(NeighborhoodList[j], i) >= 0) {
          if (ClusterAssignments[i] == 0 && ClusterAssignments[j] == 0) {
            ClusterAssignments[i] = nextClusterID;
            ClusterAssignments[j] = nextClusterID;
            nextClusterID++;
          }
          else if (ClusterAssignments[i] == 0) {
            ClusterAssignments[i] = ClusterAssignments[j];
          }
          else if (ClusterAssignments[j] == 0) {
            ClusterAssignments[j] = ClusterAssignments[i];
          }
          else if (ClusterAssignments[j] != ClusterAssignments[i]) {
            // Since instances i and j have are now assigned to the same
            // cluster, the assignments of all instances assigned to the 
            // same cluster as instance j are replaced with the cluster 
            // assignment of instance i.
            find = ClusterAssignments[j];
            replace = ClusterAssignments[i];
            for (int k = 0; k < ClusterAssignments.length; k++) {
              if (ClusterAssignments[k] == find) {
                ClusterAssignments[k] = replace;
              }
            }
          }
        }
        numberOfCompletedComputations++;
      }
    }
    // Due to the replacement of cluster IDs, the array ClusterAssignments may 
    // not contain consecutive cluster ID between 0 and NumberOfClusters. Hence,
    // we have to update the cluster assignments.
    TreeSet set = new TreeSet();
    for (int i = 0; i < ClusterAssignments.length; i++) {
      set.add(new Integer(ClusterAssignments[i]));
    }
    Iterator iterator = set.iterator();
    find = 0; replace = -1;
    while (iterator.hasNext()) {
      find = ((Integer)iterator.next()).intValue();
      replace++;
      for (int i = 0; i < ClusterAssignments.length; i++) {
        if (ClusterAssignments[i] == find) { 
          ClusterAssignments[i] = replace;
        }
      }
    }   
    NumberOfClusters = set.size();
    this.outputLogEntry(true, "Number of discovered clusters: " 
    + NumberOfClusters);
    
    // free memory and prepare cluster assignment
    SimilarityMatrix = null; System.gc();
    ApplNeighborhoodList = new int[SizeOfNearestNeighborsList];
    ApplSimilarityList = new float[SizeOfNearestNeighborsList];
    ApplNearestNeighborSimilarities = new float[NumberOfTrainingInstances];

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing parameter settings to existing and open 
   * HTML report. The text file remains open.
   */
  
  protected void createHtmlReportParameterSection() {
    
    super.createHtmlReportParameterSection();
    
    HtmlReport.setNextLine("<tr><td align=\"left\" valign=\"top\">"
    + "Number of Shared Nearest Neighbors</td>" 
    + "<td align=\"left\" valign=\"top\">" + NumberOfSharedNearestNeighbors
    + "</td></tr>");
    HtmlReport.setNextLine("</table>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Clusters an instance. Prerequisite: pInstance must have been pre-processed
   * analogously to the training instances.
   *
   * @param pInstance the instance to assign a cluster to
   * @return a cluster number
   */
  
  protected int clusterProcessedInstance(Instance pInstance) {

    this.searchForNearestNeighbors(pInstance);
    
    // determine cluster assignment of pInstance by counting the number
    // of nearest neighbors it shares with each training instance
    for (int j = 0; j < NumberOfTrainingInstances; j++) {
      // j must be a nearest neighbor of pInstance and vice versa
      if (ApplNearestNeighborSimilarities[j] 
      >= MinNearestNeighborSimilarity[j] 
      && Arrays.binarySearch(ApplNeighborhoodList, j) >= 0) {
        // count the number of shared neared neighbors
        ApplNumberOfSharedNearestNeighbors = 0;
        for (int ki = 0, kj = 0; ki < SizeOfNearestNeighborsList
        && kj < SizeOfNearestNeighborsList;) {
          if (ApplNeighborhoodList[ki] == NeighborhoodList[j][kj]) {
            ApplNumberOfSharedNearestNeighbors++;
            ki++; kj++;
          }
          else if (ApplNeighborhoodList[ki] > NeighborhoodList[j][kj]) {
            kj++;
          }
          else {
            ki++;
          }
        }
        if (ApplNumberOfSharedNearestNeighbors 
        >= NumberOfSharedNearestNeighbors) {
          return ClusterAssignments[j];
        }
      }
    }
    
    // default cluster for outlier instances
    return 0;
    
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
   * @param pOptions should contain appropriate arguments: <p>
   * -t training file 
   * [-distance <euclidean|cosine|extendedJaccard|extendedDice>]
   * [-assessIntValidity <full|true|quick|false>]
   * [-verbose <true|false>]
   * [-htmlReport <HTML report file name>]
   * [-compactify <true|false>]
   * [-sizeOfNnList <integer>]
   * [-numberOfSnn <integer>]
   */
  
  public static void main(String[] pOptions) {
    
    try {
      System.out.println(ClusterEvaluation.evaluateClusterer(
      new JarvisPatrickSnn(), pOptions));
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
  }
  
}