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

import java.util.Random;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.server.TaskProgress;

/**
 * Based on weka.clusterers.SimpleKMeans, Revision 1.9:
 * Copyright (C) 2000 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * Class implementing the simple k-means clustering algorithm.
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
 * @version 0.1, 18 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 * @see Clusterer
 * @see OptionHandler
 */

public class SimpleKMeans extends AbstractKMeans {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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
   * Executes the simple k-means clustering algorithm.
   */
  
  protected void executeClusteringAlgorithm() {
    
    // randomly select this.NumberOfClusters initial cluster centroids; only
    // one centroid may be an empty instance
    
    ClusterCentroids = new Instances(TrainingInstances, NumberOfClusters);
    ClusterAssignments = new int [TrainingInstances.numInstances()];
    for (int i = 0; i < ClusterAssignments.length; i++) {
      ClusterAssignments[i] = -1;
    }
    
    RandomNumberGenerator = new Random(Seed);
    boolean [] selected = new boolean[TrainingInstances.numInstances()];
    boolean emptyCentroidExists = false;
    int instIndex;
    for (int i = 0; i < NumberOfClusters; i++) {
      do {
        instIndex = Math.abs(RandomNumberGenerator.nextInt()) 
        % TrainingInstances.numInstances();
        if (this.isEmptyInstance(TrainingInstances.instance(instIndex))) {
          if (emptyCentroidExists) {
            selected[instIndex] = true;  // skip empty instance
          }
          else {
            emptyCentroidExists = true;  // first and last empty centroid
          }
        }
      } while (selected[instIndex]);
      ClusterCentroids.add(TrainingInstances.instance(instIndex));
      selected[instIndex] = true;
    }
    selected = null;
    
    if (HtmlReportFileName != null) {
      this.appendHtmlReportLogEntry(NumberOfClusters 
      + " initial cluster centroids have been randomly selected.");
    }
    
    // Iteratively reassigns training instances according to k-means algorithm:
    
    boolean converged = false;
    int numberOfReassignments = 0;
    int newClusterAssignment = 0;
    Instances[] newClusterAssignments = null;
    double[] newClusterCentroid = null;
    
    Iterations = 0;
    while (!converged && Iterations <= MaxNumberOfIterations) {
      Iterations++;
      converged = true;
      numberOfReassignments = 0;
      for (int i = 0; i < TrainingInstances.numInstances(); i++) {
        newClusterAssignment = clusterProcessedInstance(
        TrainingInstances.instance(i));
        if (newClusterAssignment != ClusterAssignments[i]) {
          converged = false;
          numberOfReassignments++;
        }
        ClusterAssignments[i] = newClusterAssignment;
      }
      this.updateTaskProgress(TaskProgress.INDETERMINATE, 
      "Cluster Assignment Changes in Iteration " + Iterations + ": " 
      + numberOfReassignments + "/" + TrainingInstances.numInstances());
      if (VerboseMode) {
        System.out.println("Iteration " + Iterations + ": " 
        + numberOfReassignments + " of " + TrainingInstances
        .numInstances() + " cluster assignments have changed.");
      }
      if (HtmlReportFileName != null) {
        this.appendHtmlReportLogEntry("Iteration " + Iterations + ": "
        + numberOfReassignments + " of " + TrainingInstances.numInstances()
        + " cluster assignments have changed.");
      }
      
      // update cluster centroids according to new cluster assignments
      newClusterAssignments = new Instances[NumberOfClusters];
      ClusterCentroids = new Instances(TrainingInstances, NumberOfClusters);
      for (int i = 0; i < NumberOfClusters; i++) {
        newClusterAssignments[i] = new Instances(TrainingInstances, 0);
      }
      for (int i = 0; i < TrainingInstances.numInstances(); i++) {
        newClusterAssignments[ClusterAssignments[i]].add(
        TrainingInstances.instance(i));
      }
      for (int i = 0; i < NumberOfClusters; i++) {
        newClusterCentroid = new double[TrainingInstances.numAttributes()];
        for (int j = 0; j < TrainingInstances.numAttributes(); j++) {
          newClusterCentroid[j] = newClusterAssignments[i].meanOrMode(j);
        }
        ClusterCentroids.add(new Instance(1.0, newClusterCentroid));
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing parameter settings to existing and open 
   * HTML report. The text file remains open.
   */
  
  protected void createHtmlReportParameterSection() {
    
    super.createHtmlReportParameterSection();
    
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
   */
  
  public static void main(String[] pOptions) {
    
    try {
      System.out.println(ClusterEvaluation.evaluateClusterer(
      new SimpleKMeans(), pOptions));
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
  }
  
}