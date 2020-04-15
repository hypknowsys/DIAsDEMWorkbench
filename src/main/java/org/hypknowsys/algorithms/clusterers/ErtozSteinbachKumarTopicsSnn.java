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
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import org.hypknowsys.algorithms.core.AbstractOptionHandler;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Option;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.TaskProgress;

/**
 * Class implementing the Ertoz-Steinbach-Kumar SNN clustering algorithm for 
 * finding topics in document collections. Source: Levent Ertoz, Michael 
 * Steinbach, and Vipin Kumar. Finding topics in collections of documents: A 
 * shared nearest neighbor approach. In Weili Wu, Hui Xiong, and Shashi Shakhar,
 * editors, Clustering and Information Retrieval, volume 11 of Network Theory
 * and Applications, pages 83-103. Kluwer Academic Publishers, Boston, 
 * Dordrecht, 2004. URL: http://www-users.cs.umn.edu/~kumar/papers/papers.html
 * <p>
 * For reasons of efficiency, the training data set should not comprise more 
 * than sizeOfNnList duplicates of each instance. If it does, excess duplicate 
 * instances are removed prior to clustering the data set. Training and 
 * applications instances should be pre-processed outside this algorithm 
 * instance, but using the exactly the same process workflow for both training 
 * and application data. According to the chosen distance measure, STRING and 
 * DATE attributes might be ignored for computing the distance between two 
 * instances. Check the documentation of the respective distance measure.<p>
 *
 * Valid options of the Ertoz-Steinbach-Kumar SNN clustering algorithm are:<p>
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
 * -strongLinkT <integer> <br>
 * Specify the strong link threshold, default: 20. <p>
 *
 * -labelingT <integer> <br>
 * Specify the labeling threshold, default: 25. <p>
 *
 * -mergeT <integer> <br>
 * Specify the merge threshold, default: 40. <p>
 *
 * -noiseT <integer> <br>
 * Specify the noise threshold, default: 5. <p>
 *
 * -topicT <integer> <br>
 * Specify the topic threshold, default: 40. <p>
 *
 * @version 0.1, 10 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 * @see Clusterer
 * @see OptionHandler
 */

public class ErtozSteinbachKumarTopicsSnn extends AbstractSnn {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * List that contains the connectivity, or SNN density, for each training 
   * instance
   */
  protected int[] Connectivity = null;
  
  /**
   * List that indicates for each instance whether is a representative 
   * point for a topic
   */
  protected boolean[] IsTopicPoint = null;
  
  /**
   * List that indicates for each instance whether is a noise point
   */
  protected boolean[] IsNoisePoint = null;
  
  /**
   * Strong link threshold, default: 20
   */
  protected int StrongLinkThreshold = DEFAULT_STRONG_LINK_THRESHOLD;
  
  /**
   * Labeling threshold, default: 25
   */
  protected int LabelingThreshold = DEFAULT_LABELING_THRESHOLD;
  
  /**
   * Merge threshold, default: 40
   */
  protected int MergeThreshold = DEFAULT_MERGE_THRESHOLD;
  
  /**
   * Noise threshold, default: 25
   */
  protected int NoiseThreshold = DEFAULT_NOISE_THRESHOLD;
 
  /**
   * Topic threshold, default: 25
   */
  protected int TopicThreshold = DEFAULT_TOPIC_THRESHOLD;
 
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Temporary double variable for improving performance only; used in 
   * assigning instances to clusters in the application phase
   */
  private transient int ApplFirstPointMergeStep = 0;

  /**
   * Temporary double variable for improving performance only; used in 
   * assigning instances to clusters in the application phase
   */
  private transient int ApplFirstPointLabelingStep = 0;

  /**
   * Temporary double variable for improving performance only; used in 
   * assigning instances to clusters in the application phase
   */
  private transient int ApplConnectivity = 0;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Default setting of attribute this.StrongLinkThreshold
   */
  protected static final int DEFAULT_STRONG_LINK_THRESHOLD = 20;

  /**
   * Default setting of attribute this.LabelingThreshold
   */
  protected static final int DEFAULT_LABELING_THRESHOLD = 25;

  /**
   * Default setting of attribute this.MergeThreshold
   */
  protected static final int DEFAULT_MERGE_THRESHOLD = 40;

  /**
   * Default setting of attribute this.NoiseThreshold
   */
  protected static final int DEFAULT_NOISE_THRESHOLD = 5;

  /**
   * Default setting of attribute this.TopicThreshold
   */
  protected static final int DEFAULT_TOPIC_THRESHOLD = 40;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the strong link threshold.
   *
   * @return the strong link threshold
   */
  public int getStrongLinkThreshold() {
    
    return StrongLinkThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the labeling threshold.
   *
   * @return the labeling threshold
   */
  public int getLabelingThreshold() {
    
    return LabelingThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the merge threshold.
   *
   * @return the merge threshold
   */
  public int getMergeThreshold() {
    
    return MergeThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the noise threshold.
   *
   * @return the noise threshold
   */
  public int getNoiseThreshold() {
    
    return NoiseThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the topic threshold.
   *
   * @return the topic threshold
   */
  public int getTopicThreshold() {
    
    return TopicThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the strong link threshold.
   *
   * @param pStrongLinkThreshold the new strong link threshold
   */
  public void setStrongLinkThreshold(int pStrongLinkThreshold) {
    
    StrongLinkThreshold = pStrongLinkThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the labeling threshold.
   *
   * @param pLabelingThreshold the new labeling threshold
   */
  public void setLabelingThreshold(int pLabelingThreshold) {
    
    LabelingThreshold = pLabelingThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the merge threshold.
   *
   * @param pMergeThreshold the new merge threshold
   */
  public void setMergeThreshold(int pMergeThreshold) {
    
    MergeThreshold = pMergeThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the noise threshold.
   *
   * @param pNoiseThreshold the new noise threshold
   */
  public void setNoiseThreshold(int pNoiseThreshold) {
    
    NoiseThreshold = pNoiseThreshold;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the topic threshold.
   *
   * @param pTopicThreshold the new topic threshold
   */
  public void setTopicThreshold(int pTopicThreshold) {
    
    TopicThreshold = pTopicThreshold;
    
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
   * Valid options of the Ertoz-Steinbach-Kumar SNN clustering algorithm are:<p>
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
   * -strongLinkT <integer> <br>
   * Specify the strong link threshold, default: 20. <p>
   *
   * -labelingT <integer> <br>
   * Specify the labeling threshold, default: 25. <p>
   *
   * -mergeT <integer> <br>
   * Specify the merge threshold, default: 40. <p>
   *
   * -noiseT <integer> <br>
   * Specify the noise threshold, default: 5. <p>
   *
   * -topicT <integer> <br>
   * Specify the topic threshold, default: 40. <p>
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
    "\tSpecify the strong link threshold, default: 20.", 
    "strongLinkT", 1, "-strongLinkT <integer>"));
    newVector.addElement(new Option(
    "\tSpecify the labeling threshold, default: 25.", 
    "labelingT", 1, "-labelingT <integer>"));
    newVector.addElement(new Option(
    "\tSpecify the merge threshold, default: 40.", 
    "mergeT", 1, "-mergeT <integer>"));
    newVector.addElement(new Option(
    "\tSpecify the noise threshold, default: 5.", 
    "noiseT", 1, "-noiseT <integer>"));
    newVector.addElement(new Option(
    "\tSpecify the topic threshold, default: 40.", 
    "topicT", 1, "-topicT <integer>"));
        
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
    "strongLinkT", pOptions);
    this.setStrongLinkThreshold(DEFAULT_STRONG_LINK_THRESHOLD);
    if (optionString.length() != 0) {
      this.setStrongLinkThreshold(Tools.string2Int(optionString));
    }    
    optionString = AbstractOptionHandler.getOption(
    "labelingT", pOptions);
    this.setLabelingThreshold(DEFAULT_LABELING_THRESHOLD);
    if (optionString.length() != 0) {
      this.setLabelingThreshold(Tools.string2Int(optionString));
    }    
    optionString = AbstractOptionHandler.getOption(
    "mergeT", pOptions);
    this.setMergeThreshold(DEFAULT_MERGE_THRESHOLD);
    if (optionString.length() != 0) {
      this.setMergeThreshold(Tools.string2Int(optionString));
    }    
    optionString = AbstractOptionHandler.getOption(
    "noiseT", pOptions);
    this.setNoiseThreshold(DEFAULT_NOISE_THRESHOLD);
    if (optionString.length() != 0) {
      this.setNoiseThreshold(Tools.string2Int(optionString));
    }    
    optionString = AbstractOptionHandler.getOption(
    "topicT", pOptions);
    this.setTopicThreshold(DEFAULT_TOPIC_THRESHOLD);
    if (optionString.length() != 0) {
      this.setTopicThreshold(Tools.string2Int(optionString));
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
    
    int numberOfOptions = superOptions.length + 10;
    String[] options = new String[numberOfOptions];
    int current = 0;

    for (int i = 0; i < superOptions.length; i++) {
      options[current++] = superOptions[i];
    }
    options[current++] = "-strongLinkT";
    options[current++] = "" + this.getStrongLinkThreshold();
    options[current++] = "-labelingT";
    options[current++] = "" + this.getLabelingThreshold();
    options[current++] = "-mergeT";
    options[current++] = "" + this.getMergeThreshold();
    options[current++] = "-noiseT";
    options[current++] = "" + this.getNoiseThreshold();
    options[current++] = "-topicT";
    options[current++] = "" + this.getTopicThreshold();
    
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
   * Executes the Ertoz-Steinbach-Kumar SNN clustering algorithm.
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
    // SimilarityMatrix and four other arrays: NeighborhoodList, 
    // SimilarityList, MinNearestNeighborSimilarity, Connectivity,
    // IsTopicPoint, and IsNoisePoint
    long necessaryMemory = (4 * numberOfComputations 
    + 4 * 6 * NumberOfTrainingInstances) / 1024 / 1024;
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
    "Computing the Connectivity of each Instance");
    this.outputLogEntry(true, "Computing the connectivity of each instance");

    Connectivity = new int[NumberOfTrainingInstances];
    IsTopicPoint = new boolean[NumberOfTrainingInstances];
    IsNoisePoint = new boolean[NumberOfTrainingInstances];
    int numberOfTopicPoints = 0;
    int numberOfNoisePoints = 0;
    int minConnectivity = Integer.MAX_VALUE;
    int maxConnectivity = 0;
    long sumConnectivity = 0;
    // determine the connectivity, or SNN density, of each instance
    for (int i = 0; i < NumberOfTrainingInstances; i++) {
      // initialize connectivity of instance i and indicator lists
      Connectivity[i] = 0;
      IsTopicPoint[i] = false;
      IsNoisePoint[i] = false;
      // iterate through nearest neighbors of current instance i
      for (int j = 0; j < NeighborhoodList[i].length; j++) {
        if (NeighborhoodList[i][j] != EMPTY
        && this.getSimilarity(i, NeighborhoodList[i][j], 
        NumberOfTrainingInstances) > StrongLinkThreshold) {
          Connectivity[i]++;
        }
      }
      if (Connectivity[i] > TopicThreshold) {
        IsTopicPoint[i] = true;
        numberOfTopicPoints++;
      }
      else if (Connectivity[i] < NoiseThreshold) {
        IsNoisePoint[i] = true;
        numberOfNoisePoints++;
      }
      sumConnectivity += Connectivity[i];
      if (Connectivity[i] < minConnectivity) {
        minConnectivity = Connectivity[i];
      }
      else if (Connectivity[i] > maxConnectivity) {
        maxConnectivity = Connectivity[i];
      }
    }    
    this.outputLogEntry(true, "Min. connectivity: " + minConnectivity);
    this.outputLogEntry(true, "Avg. connectivity: " + (sumConnectivity 
    / NumberOfTrainingInstances));
    this.outputLogEntry(true, "Max. connectivity: " + maxConnectivity);
    this.outputLogEntry(true, "Number of topic points: " + numberOfTopicPoints);
    this.outputLogEntry(true, "Number of noise points: " + numberOfNoisePoints);
    
    this.updateTaskProgress(TaskProgress.INDETERMINATE,
    "Merging Highly Connected Instances to Form Clusters");
    this.outputLogEntry(true, "Merging highly connected instances to form "
    + "clusters");

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
        // The instances i and j are assigned to the same cluster if they
        // share more than MergeThreshold nearest neighbors and either of
        // them (or both) is or b a topic point.
        if ((SimilarityMatrix[numberOfCompletedComputations] > MergeThreshold)
        && (IsTopicPoint[i] || IsTopicPoint[j]) 
        && !IsNoisePoint[i] && !IsNoisePoint[j]) {
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
    
    this.updateTaskProgress(TaskProgress.INDETERMINATE,
    "Assigning the Remaining, Non-Noise Instances to Clusters");
    this.outputLogEntry(true, "Assigning the remaining, non-noise instances to "
    + "clusters");
    
    for (int i = 0; i < NumberOfTrainingInstances; i++) {
      if (ClusterAssignments[i] > 0) {
        for (int j = 0; j < NumberOfTrainingInstances; j++) {
          if (ClusterAssignments[j] == 0 && !IsNoisePoint[j] && i != j
          && (int)this.getSimilarity(i, j, NumberOfTrainingInstances)
          > LabelingThreshold) {
            ClusterAssignments[j] = ClusterAssignments[i];
          }
        }
      }
    }

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
    
    HtmlReport.setNextLine("<tr>" 
    + "<td align=\"left\" valign=\"top\">Strong Link Threshold "
    + "[Link Strength]</td>"
    + "<td align=\"left\" valign=\"top\">" + StrongLinkThreshold
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Labeling Threshold "
    + "[Link Strength]</td>"
    + "<td align=\"left\" valign=\"top\">" + LabelingThreshold
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Merge Threshold "
    + "[Link Strength]</td>"
    + "<td align=\"left\" valign=\"top\">" + MergeThreshold
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Noise Threshold "
    + "[Number of Strong Links]</td>"
    + "<td align=\"left\" valign=\"top\">" + NoiseThreshold
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Topic Threshold "
    + "[Number of Strong Links]</td>"
    + "<td align=\"left\" valign=\"top\">" + TopicThreshold
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
    
    ApplFirstPointMergeStep = -1;
    ApplFirstPointLabelingStep = -1;
    ApplConnectivity = 0;

    this.searchForNearestNeighbors(pInstance);
    
    // step 1: Compute the connectivity of pInstance by considering only
    // the nearest neighbors of pInstance and simultaneously perform the
    // merge step among the nearest neighbors.
    for (int j = 0; j < ApplNeighborhoodList.length
    && ApplNeighborhoodList[j] != EMPTY; j++) {
      ApplNumberOfSharedNearestNeighbors = 0;
      for (int ki = 0, kj = 0; ki < SizeOfNearestNeighborsList
      && kj < SizeOfNearestNeighborsList;) {
        if (ApplNeighborhoodList[ki] == NeighborhoodList
        [ApplNeighborhoodList[j]][kj]) {
          ApplNumberOfSharedNearestNeighbors++;
          ki++; kj++;
        }
        else if (ApplNeighborhoodList[ki] > NeighborhoodList
        [ApplNeighborhoodList[j]][kj]) {
          kj++;
        }
        else {
          ki++;
        }
      }
      if (ApplNumberOfSharedNearestNeighbors > StrongLinkThreshold) {
        ApplConnectivity++;
      }
      if (ApplFirstPointMergeStep == -1 
      && IsTopicPoint[ApplNeighborhoodList[j]]
      && ApplNumberOfSharedNearestNeighbors > MergeThreshold) {
        // merge step
        ApplFirstPointMergeStep = ApplNeighborhoodList[j];
      }
    }
    if (ApplConnectivity < NoiseThreshold) {
      // default cluster for outlier instances
      return 0;
    }
    else if (ApplFirstPointMergeStep >= 0) {
      return ClusterAssignments[ApplFirstPointMergeStep];
    }

    // step 2: Assign pInstance to clusters according to the algorithm
    // that comprises an initial merge step and a subsequent labeling step.
    for (int j = 0; j < NumberOfTrainingInstances; j++) {
      // count the number of shared neared neighbors of pInstance
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
      if ((IsTopicPoint[j] || ApplConnectivity > TopicThreshold)
      && ApplNumberOfSharedNearestNeighbors > MergeThreshold) {
        // merge step
        return ClusterAssignments[j];
      }
      else if (ApplFirstPointLabelingStep == -1 && !IsNoisePoint[j]
      && ApplNumberOfSharedNearestNeighbors > LabelingThreshold) {
        // labeling step
        ApplFirstPointLabelingStep = j;
      }
    }
  
    if (ApplFirstPointLabelingStep >= 0) {
      return ClusterAssignments[ApplFirstPointLabelingStep];
    }
    else {    
      // default cluster for outlier instances
      return 0;
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
   * @param pOptions should contain appropriate arguments: <p>
   * -t training file 
   * [-distance <euclidean|cosine|extendedJaccard|extendedDice>]
   * [-assessIntValidity <full|true|quick|false>]
   * [-verbose <true|false>]
   * [-htmlReport <HTML report file name>]
   * [-compactify <true|false>]
   * [-sizeOfNnList <integer>]
   * [-strongLinkT <integer>]
   * [-labelingT <integer>]
   * [-mergeT <integer>]
   * [-noiseT <integer>]
   * [-topicT <integer>]
   */
  
  public static void main(String[] pOptions) {
    
    try {
      System.out.println(ClusterEvaluation.evaluateClusterer(
      new ErtozSteinbachKumarTopicsSnn(), pOptions));
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
  }
  
}