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
 * Class implementing the Batch Map algorithm that establishes a Self-Organizing
 * Maps (SOM). We refer to the book by Toivo Kohonen (Self-Organzing Maps, 3rd
 * edition, Springer-Verlag, Berlin, Heidelberg, 2001, pp. 110-111 and 138--140)
 * for an excellent introduction into this algorithm. Training and applications 
 * instances should be pre-processed outside this algorithm instance, but using 
 * the exactly the same process workflow for both training and application data.
 * According to the chosen distance measure,  STRING and DATE attributes might
 * be ignored for computing the distance between two instances. Check the 
 * documentation of the respective distance measure.
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
 * -rows <number of rows> <br>
 * Specify the number of rows in the resulting SOM, default: 5. The number 
 * of clusters to be created is the product of rows and columns.<p>
 *
 * -columns <number of colums> <br>
 * Specify the number of columns in the resulting SOM, default: 5. The number 
 * of clusters to be created is the product of rows and columns.<p>
 *
 * -latticeType <rectangular|hexagonal> <br>
 * Specify the lattice type of the SOM array, default: rectangular. In a
 * rectangalar SOM, the maximum number of neighbors is 5 for each node. Each
 * node can have up to six neighbors in a hexagonal SOM.<p>
 *
 * -neighborhoodRadii <vector of integers> <br>
 * Specifiy the neighborhood radii for the initial iterations using a vector of
 * integers. In the remaining iterations, the neighborhood radius is set to 0. 
 * Vectors (i) must not contain blanks, (i) comprise a comma-separated list of
 * components, (iii) start with '[', and (iv) ends with ']'.  Default: 
 * [5,4,4,3,3,2,2,2,1,1,1,1,0]<p>
 *
 * -S <seed> <br>
 * Specify the seed of the random number generator, default: 10. <p>
 *
 * -maxIterations <max. number of iterations> <br>
 * Specify the maximum number of iterations, default: Integer.MAX_VALUE. <p>
 *
 *
 * @version 0.1, 24 November 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 * @see Clusterer
 * @see OptionHandler
 */

public class BatchSom extends AbstractSom {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
 /**
   * Array containing the neighborhood radii for the initial iterations of the
   * batch SOM algorithm. NeighborhoodRadii[0] represents the neighborhood 
   * radius of the first iteration and NeighborhoodRadii[1] represents the 
   * neighborhood radius of the second iteration. Let n denote the length of
   * NeighborhoodRadii. In iterations greater than n, the neighborhood radius is
   * set to 0. In this case, a simple k-means clustering algorithm is performed.
   */
  protected int[] NeighborhoodRadii = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Temporary double variable for improving performance only
   */
  private double TmpDistance = 0.0d;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Default setting of attribute this.NeighborhoodRadii
   */
  protected static final int[] DEFAULT_NEIGHBORHOOD_RADII = 
  {5, 4, 4, 3, 3, 2, 2, 2, 1, 1, 1, 1, 0};
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the array of neighborhood radii.
   *
   * @return the array of neighborhood radii.
   */
  public int[] getNeighborhoodRadii() {
    
    return NeighborhoodRadii;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the array of neighborhood radii, default: DEFAULT_NEIGHBORHOOD_RADII.
   *
   * @param pNeighborhoodRadii the array of neighborhood radii
   */
  
  public void setNeighborhoodRadii(int[] pNeighborhoodRadii) {
    
    if (pNeighborhoodRadii == null) {
      NeighborhoodRadii = DEFAULT_NEIGHBORHOOD_RADII;
    }
    else {
      NeighborhoodRadii = pNeighborhoodRadii;
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
   * -rows <number of rows> <br>
   * Specify the number of rows in the resulting SOM, default: 5. The number
   * of clusters to be created is the product of rows and columns.<p>
   *
   * -columns <number of colums> <br>
   * Specify the number of columns in the resulting SOM, default: 5. The number
   * of clusters to be created is the product of rows and columns.<p>
   *
   * -latticeType <rectangular|hexagonal> <br>
   * Specify the lattice type of the SOM array, default: rectangular. In a
   * rectangalar SOM, the maximum number of neighbors is 5 for each node. Each
   * node can have up to six neighbors in a hexagonal SOM.<p>
   *
   * -neighborhoodRadii <vector of integers> <br>
   * Specifiy the neighborhood radii for the initial iterations using a vector 
   * of integers. In the remaining iterations, the neighborhood radius is set to
   * 0. Vectors (i) must not contain blanks, (i) comprise a comma-separated list
   * of components, (iii) start with '[', and (iv) ends with ']'.  Default:
   * [5,4,4,3,3,2,2,2,1,1,1,1,0]<p>
   *
   * -S <seed> <br>
   * Specify the seed of the random number generator, default: 10. <p>
   *
   * -maxIterations <max. number of iterations> <br>
   * Specify the maximum number of iterations, default: Integer.MAX_VALUE. <p>
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
    "\tSpecify neighborhood radii for the initial iterations using a "
    + "vector of integers, default: [5,4,4,3,3,2,2,2,1,1,1,1,0].",
    "neighborhoodRadii", 1,
    "-neighborhoodRadii <vector of integers>"));
    
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
    "neighborhoodRadii", pOptions);
    this.setNeighborhoodRadii(DEFAULT_NEIGHBORHOOD_RADII);
    if (optionString.length() != 0) {
      this.setNeighborhoodRadii(Tools.string2IntArray(optionString));
    } 

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the current settings of BatchSOM.
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
    options[current++] = "-neighborhoodRadii";
    options[current++] = Tools.intArray2String(NeighborhoodRadii);
    
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
   * Executes the Batch SOM clustering algorithm.
   */
  
  protected void executeClusteringAlgorithm() {
    
    // randomly select this.NumberOfClusters initial cluster codebook vectors;
    // only one codebook vector may be an empty instance
    
    CodebookVectors = new Instances(TrainingInstances, 
    this.getNumberOfClusters());
    ClusterAssignments = new int [TrainingInstances.numInstances()];
    for (int i = 0; i < ClusterAssignments.length; i++) {
      ClusterAssignments[i] = -1;
    }
    
    RandomNumberGenerator = new Random(Seed);
    boolean [] selected = new boolean[TrainingInstances.numInstances()];
    boolean emptyCodebookVectorExists = false;
    int instIndex;
    for (int i = 0; i < this.getNumberOfClusters(); i++) {
      do {
        instIndex = Math.abs(RandomNumberGenerator.nextInt())
        % TrainingInstances.numInstances();
        if (this.isEmptyInstance(TrainingInstances.instance(instIndex))) {
          if (emptyCodebookVectorExists) {
            selected[instIndex] = true;  // skip empty instance
          }
          else {
            // first and last empty codebook vector
            emptyCodebookVectorExists = true;
          }
        }
      } while (selected[instIndex]);
      CodebookVectors.add(TrainingInstances.instance(instIndex));
      selected[instIndex] = true;
    }
    selected = null;
    
    if (HtmlReportFileName != null) {
      this.appendHtmlReportLogEntry(this.getNumberOfClusters()
      + " initial cluster codebook vectors have been randomly selected.");
    }
    
    // Iteratively reassigns training instances according to k-means algorithm:
    
    boolean converged = false;
    int numberOfReassignments = 0;
    int newClusterAssignment = 0;
    Instances[] newClusterAssignments = null;
    double[][] newClusterMeans = null;
    double[] newClusterCodebookVector = null;
    int[] newClusterCardinality = null;
    int neighborhoodCardinality = 0;
    
    Iterations = 0;
    while (!converged && Iterations < MaxNumberOfIterations) {
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
      
      // for each cluster codebook vector or best matching node, compute the
      // mean of the associated vectors prior to finally updating the cluster 
      // codebook vectors by taking their neighborhood into consideration
      newClusterAssignments = new Instances[NumberOfClusters];
      newClusterCardinality = new int[NumberOfClusters];
      for (int i = 0; i < NumberOfClusters; i++) {
        newClusterAssignments[i] = new Instances(TrainingInstances, 0);
        newClusterCardinality[i] = 0;
      }
      for (int i = 0; i < TrainingInstances.numInstances(); i++) {
        newClusterAssignments[ClusterAssignments[i]].add(
        TrainingInstances.instance(i));
      }
      newClusterMeans = new double[NumberOfClusters]
      [TrainingInstances.numAttributes()];
      for (int i = 0; i < NumberOfClusters; i++) {
        newClusterCardinality[i] = newClusterAssignments[i].numInstances();
        for (int j = 0; j < TrainingInstances.numAttributes(); j++) {
          newClusterMeans[i][j] = newClusterAssignments[i].meanOrMode(j);
        }
      }

      // update cluster codebook vectors according to new cluster assignments
      neighborhoodCardinality = 0;
      CodebookVectors = new Instances(TrainingInstances, NumberOfClusters);
      for (int center = 0; center < NumberOfClusters; center++) {
        newClusterCodebookVector = new double[TrainingInstances
        .numAttributes()];
        neighborhoodCardinality = 0;
        // for map unit center, compute the mean of neighborhood units
        for (int neighbor = 0; neighbor < NumberOfClusters; neighbor++) {
          if (this.isNeighborOfCodebookVector(neighbor, center, Iterations)) {
            neighborhoodCardinality += newClusterCardinality[neighbor];
            for (int component = 0; component < TrainingInstances
            .numAttributes(); component++) {
              newClusterCodebookVector[component] += 
              newClusterCardinality[neighbor]
              * newClusterMeans[neighbor][component];
            }
          }
        }
        for (int component = 0; component < TrainingInstances.numAttributes(); 
        component++) {
          if (neighborhoodCardinality > 0) {
            newClusterCodebookVector[component] = 
            newClusterCodebookVector[component] / neighborhoodCardinality;
          }
          else {
            newClusterCodebookVector[component] = 0.0d;
          }
        }
        CodebookVectors.add(new Instance(1.0, newClusterCodebookVector));
  
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
    
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Neighborhood Radii</td>"
    + "<td align=\"left\" valign=\"top\">" + Tools.intArray2String(
    NeighborhoodRadii) + "</td></tr>");
    HtmlReport.setNextLine("</table>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Given a codebook vector index pTestCodebookVectorIndex, returns true if
   * pTestCodebookVectorIndex is a topological neighbor of 
   * pCenterCodebookVectorIndex and false otherwise.
   * 
   * @param pTestCodebookVectorIndex the codebook vector to be tested
   * @param pCenterCodebookVectorIndex the potential center codebook vector
   * @param pIteration the current iteration of the SOM learning process
   * @return true if pTestCodebookVectorIndex is a topological neighbor of 
   * pCenterCodebookVectorIndex and false otherwise
   */
  
  protected boolean isNeighborOfCodebookVector(int pTestCodebookVectorIndex, 
  int pCenterCodebookVectorIndex, int pIteration) {
  
    // first iteration: pIteration == 1
    if (pIteration <= NeighborhoodRadii.length && pIteration > 0) {
      // use maximum distance due to the notion of neighborhood set
      TmpDistance = Math.max(
      Math.abs(RowLocationsOfCodebookVectors[pTestCodebookVectorIndex]
      - RowLocationsOfCodebookVectors[pCenterCodebookVectorIndex]),
      Math.abs(ColumnLocationsOfCodebookVectors[pTestCodebookVectorIndex]
      - ColumnLocationsOfCodebookVectors[pCenterCodebookVectorIndex]));      
      // extract current neighborhood radius from array
      if (TmpDistance <= (double)NeighborhoodRadii[pIteration - 1]) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (pTestCodebookVectorIndex == pCenterCodebookVectorIndex) {
      // use default neighborhood radius (i.e., 0)
      return true;
    }
    else {
      return false;
    }
        
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
   * [-rows <number of rows>] 
   * [-columns <number of columns>] 
   * [-latticeType <rectangular|hexagonal>] 
   * [-neighborhoodRadii <vector of integers>]
   * [-S <seed>]
   * [-maxIterations <max. number of iterations>]
   */
  
  public static void main(String[] pOptions) {

    try {
      System.out.println(ClusterEvaluation.evaluateClusterer(
      new BatchSom(), pOptions));
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
  }
  
}