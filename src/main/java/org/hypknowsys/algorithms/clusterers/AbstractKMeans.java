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
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import org.hypknowsys.algorithms.core.AbstractOptionHandler;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.algorithms.core.Option;
import org.hypknowsys.algorithms.core.Utils;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;

/**
 * Based on weka.clusterers.SimpleKMeans, Revision 1.9:
 * Copyright (C) 2000 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * Abstract class for various variations of the k-means clustering algorithm.
 * Training and applications instances should be pre-processed outside this 
 * algorithm instance, but using the exactly the same process workflow for both
 * training and application data. According to the chosen distance measure, 
 * STRING and DATE attributes might be ignored for computing the distance 
 * between two instances. Check the documentation of the respective distance 
 * measure.
 *
 * Valid options of all k-means variants are:<p>
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

public abstract class AbstractKMeans extends Clusterer {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Number of clusters to generate
   */
  protected int NumberOfClusters = DEFAULT_NUMBER_OF_CLUSTERS;
  
 /**
   * Array containing cluster cardinalities: The array size equals
   * this.NumberOfClusters and ClusterCardinality[0] represents the
   * cardinality of the first cluster (ID = 0). Analogously, the 
   * cardinality of the last cluster is stored in 
   * ClusterCardinality[umberOfClusters - 1].
   */
  protected int[] ClusterCardinality = null;
  
  /**
   * Data set containing cluster centroids: The number of instances equals
   * this.NumberOfClusters and ClusterCentroids.instance(0) represents
   * centroid of first cluster (ID = 0). Analogously, the centroid of the
   * last cluster is stored in ClusterCentroids.instance(this.NumberOfClusters
   * - 1).
   */
  protected Instances ClusterCentroids = null;
  
  /**
   * Integer used for initializing the random number generator
   */
  protected int Seed = DEFAULT_SEED;
  
  /**
   * k-Means stopping criterion: Maximum number of reassignment iterations
   */
  protected int MaxNumberOfIterations = DEFAULT_MAX_NUMBER_OF_ITERATIONS;
  
  /**
   * Keeps track of the number of iterations completed before convergence
   */
  protected int Iterations = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Random number generator for selecting initial cluster centroids
   */
  protected transient Random RandomNumberGenerator = null;
  
  /**
   * Temporary string buffer for performance purposes only
   */
  protected transient StringBuffer TmpStringBuffer = null;
  
  /**
   * Temporary double variable for improving performance only
   */
  private transient double TmpDoubleValue = Double.NaN;
  
  /**
   * Temporary boolean variable for improving performance only
   */
  private transient boolean TmpBooleanValue = false;
  
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
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Default setting of attribute this.NumberOfClusters
   */
  protected static final int DEFAULT_NUMBER_OF_CLUSTERS = 2;

  /**
   * Default setting of attribute this.Seed
   */
  protected static final int DEFAULT_SEED = 10;

  /**
   * Default setting of attribute this.MaxNumberOfIterations
   */
  protected static final int DEFAULT_MAX_NUMBER_OF_ITERATIONS = Integer
  .MAX_VALUE;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the random number seed.
   *
   * @return the seed
   */
  
  public int getSeed() {
    
    return  Seed;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of clusters.
   *
   * @return the number of clusters generated for a training dataset.
   */
  public int getNumberOfClusters() {
    
    return NumberOfClusters;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the maximum number of iterations (i.e., a stopping criterion).
   *
   * @return the maximum number of iterations.
   */
  
  public int getMaxNumberOfIterations() {
    
    return MaxNumberOfIterations;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the random number seed, default: 10.
   *
   * @param pNewSeed the seed
   */
  
  public void setSeed(int pNewSeed) {
    
    if (pNewSeed >= 0) {
      pNewSeed = pNewSeed;
    }
    else {
      pNewSeed = DEFAULT_SEED;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Set the number of clusters to generate, default: 2.
   *
   * @param pNewNumberOfClusters the number of clusters to generate
   */
  public void setNumberOfClusters(int pNewNumberOfClusters) {
    
    if (pNewNumberOfClusters > 0) {
      NumberOfClusters = pNewNumberOfClusters;
    }
    else {
      NumberOfClusters = DEFAULT_NUMBER_OF_CLUSTERS;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the maximum number of clustering iterations (i.e., a stopping
   * criterion), default: Interger.MAX_VALUE.
   *
   * @param pMaxNumberOfIterations the maximum number of iterations
   */
  
  public void setMaxNumberOfIterations(int pMaxNumberOfIterations) {
    
    if (pMaxNumberOfIterations > 0) {
      MaxNumberOfIterations = pMaxNumberOfIterations;
    }
    else {
      MaxNumberOfIterations = DEFAULT_MAX_NUMBER_OF_ITERATIONS;
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
    
    TmpStringBuffer = new StringBuffer(10000);
    
    TmpStringBuffer.append(super.toString());
    TmpStringBuffer.append("Number of clusters: " + NumberOfClusters + "\n");
    TmpStringBuffer.append("Number of iterations: " + Iterations + "\n");
    for (int i = 0; i < NumberOfClusters && ClusterCentroids != null; i++) {
      TmpStringBuffer.append("\nCentroid of cluster " + i + "\n");
      for (int j = 0; j < ClusterCentroids.numAttributes(); j++) {
        if (ClusterCentroids.attribute(j).isNominal()) {
          TmpStringBuffer.append("\t"
          + TrainingInstancesMetadata.getNameOfAttribute(j) + " = "
          + ClusterCentroids.attribute(j).value((int)ClusterCentroids
          .instance(i).value(j)) + "\n");
        } 
        else {
          TmpStringBuffer.append("\t"
          + TrainingInstancesMetadata.getNameOfAttribute(j) + " = "
          + Utils.doubleToString(ClusterCentroids.instance(i).value(j), 3)
          + "; Value_Centroid/Mean(Value_Population) = "
          + Utils.doubleToString(ClusterCentroids.instance(i).value(j)
          / TrainingInstancesMetadata.getMeanOfAttribute(j), 3) + "\n");
        }
      }
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface OptionHandler methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an enumeration describing the available options. <p>
   *
   * Valid options of all k-means variants are:<p>
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
    "\tSpecify the number of clusters to generate, default: 2.",
    "N", 1, "-N <num>"));
    newVector.addElement(new Option(
    "\tSpecify the seed of the random number generator, default: 10.", 
    "S", 1, "-S <num>"));
    newVector.addElement(new Option(
    "\tSpecify the maximum number of k-means iterations, "
    + "default: Integer.MAX_VALUE.",
    "maxIterations", 1, "-maxIterations <max. number of iterations>"));
    
    return  newVector.elements();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Parses a given list of options.
   * @param pOptions the list of options as an array of strings
   **/
  
  public void setOptions(String[] pOptions) {
    
    super.setOptions(pOptions);
    
    String optionString = AbstractOptionHandler.getOption('N', pOptions);
    this.setNumberOfClusters(DEFAULT_NUMBER_OF_CLUSTERS);
    if (optionString.length() != 0) {
      this.setNumberOfClusters(Tools.string2Int(optionString));
    }
    
    optionString = AbstractOptionHandler.getOption('S', pOptions);
    this.setSeed(DEFAULT_SEED);
    if (optionString.length() != 0) {
      this.setSeed(Tools.string2Int(optionString));
    }
    
    optionString = AbstractOptionHandler.getOption("maxIterations", pOptions);
    if (optionString.length() != 0) {
      this.setMaxNumberOfIterations(Tools.string2Int(optionString));
    }
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the current settings of SimpleKMeans.
   *
   * @return an array of strings suitable for passing to setOptions()
   */
  
  public String[] getOptions() {
    
    String[] superOptions = super.getOptions();
    
    int numberOfOptions = superOptions.length + 4;
    if (MaxNumberOfIterations < Integer.MAX_VALUE) {
      numberOfOptions += 2;
    }
    String[] options = new String[numberOfOptions];
    int current = 0;

    for (int i = 0; i < superOptions.length; i++) {
      options[current++] = superOptions[i];
    }
    options[current++] = "-N";
    options[current++] = "" + this.getNumberOfClusters();
    options[current++] = "-S";
    options[current++] = "" + this.getSeed();
    if (MaxNumberOfIterations < Integer.MAX_VALUE) {
      options[current++] = "-maxIterations";
      options[current++] = "" + MaxNumberOfIterations;
    }
    
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
  
    MyClusterValidityAssessor = new RelativeClusterValidityAssessor();
    MyClusterValidityAssessor.setTaskProgressListener(MyServer, MyTaskProgress, 
    MyTaskThread);
    MyClusterValidityAssessor.assessClusterValidity(EvaluateInternalQuality, 
    (Clusterer)this);
  
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the name of the representative instance returned by the specific
   * k-means clustering algorithm.
   * @return the name of the representative instance
   */
  
  protected String getNameOfRepresentativeInstance() {
    
    return "Centroid";
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns one instance (i.e., the centroid) that represents the specified 
   * cluster. Prerequisites: The specific clustering algorithm must have been 
   * executed.
   * @param pClusterID the ID of the cluster whose representative instance
   * shall be returned; 0 <= pClusterID < this.getNumberOfClusters()
   * @return the representative instance or null if pClusterID is not valid
   */
  
  protected Instance getRepresentativeInstance(
  int pClusterID) {
    
    if (ClusterCentroids != null && pClusterID >= 0 
    && pClusterID < this.getNumberOfClusters()) {
      return ClusterCentroids.instance(pClusterID);
    }
    else {
      return null;
    }
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing cluster validity indices and details about 
   * all cluster centroids to existing and open HTML report. The text file 
   * remains open.
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
    + "<td align=\"left\" valign=\"top\">Number of Clusters</td>"
    + "<td align=\"left\" valign=\"top\">" + NumberOfClusters
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Random Seed</td>"
    + "<td align=\"left\" valign=\"top\">" + Seed
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Max. Iterations</td>"
    + "<td align=\"left\" valign=\"top\">" + (MaxNumberOfIterations 
    == Integer.MAX_VALUE ? "n/a" : "" + MaxNumberOfIterations)
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
   * Clusters an instance. Prerequisite: pInstance must have been pre-processed
   * analogously to the training instances.
   *
   * @param pInstance the instance to assign a cluster to
   * @return a cluster number
   */
  
  protected int clusterProcessedInstance(Instance pInstance) {
    
    TmpMinDistance = 1.0d;
    TmpBestClusterID = 0;
    for (int i = 0; i < this.getNumberOfClusters(); i++) {
      TmpDistance = MyDistanceMeasure.computeDistance(pInstance,
      ClusterCentroids.instance(i), TrainingInstancesMetadata);
      if (TmpDistance < TmpMinDistance) {
        TmpMinDistance = TmpDistance;
        TmpBestClusterID = i;
      }
    }
    
    return TmpBestClusterID;
    
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