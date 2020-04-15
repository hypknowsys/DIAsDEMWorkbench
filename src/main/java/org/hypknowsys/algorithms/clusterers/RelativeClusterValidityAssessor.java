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
import java.util.Iterator;
import java.util.TreeMap;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.InstancesMetadata;
import org.hypknowsys.algorithms.core.Utils;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.TaskProgress;

/**
 * Based on weka.clusterers.Clusterer, Revision 1.8:
 * Copyright (C) 1999 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * This class instantitiates a relative clustering quality evaluator.
 * According to the chosen distance measure, STRING and DATE attributes might be
 * ignored for computing the distance between two instances. Check the
 * documentation of the respective distance measure.
 *
 * @version 0.1, 28 June 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class RelativeClusterValidityAssessor extends ClusterValidityAssessor {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Array containing the average silhouette width of each cluster: The array 
   * size equals MyClusterer.numberOfClusters() and AvgSilhouetteWidths[0] 
   * represents the average silhouette width of the first cluster (ID = 0). 
   * Analogously, the average silhouette width of the last cluster is stored in
   * AvgSilhouetteWidths[MyClusterer.numberOfClusters() - 1].
   *
   * Reference: Leonard Kaufman and Peter J. Rousseeuw. Finding Groups in Data:
   * An Introduction to Cluster Analysis, pp. 83-88. John Wiley & Sons, New 
   * York, Chichester, 1990.
   */
  protected double[] AvgSilhouetteWidths = null;
  
  /**
   * Holds the average silhouette width of the entire clustering.
   */
  protected double AvgSilhouetteWidth = Double.NaN;
  
  /**
   * Array containing the Davies-Bouldin index of each cluster: The array 
   * size equals MyClusterer.numberOfClusters() and DaviesBouldinIndexes[0] 
   * represents the Davies-Bouldin index of the first cluster (ID = 0). 
   * Analogously, the Davies-Bouldin index of the last cluster is stored in
   * DaviesBouldinIndexes[MyClusterer.numberOfClusters() - 1].
   *
   * Reference: Anil K. Jain and Richard C. Dubes. Algorithms for Clustering 
   * Data, pp. 185-187. Prentice Hall, Englewood Cliffs, 1988.
   */
  protected double[] DaviesBouldinIndexes = null;
  
  /**
   * Holds the Davies-Bouldin index of the entire clustering.
   */
  protected double DaviesBouldinIndex = Double.NaN;
  
  /**
   * Holds the original Dunn index of the entire clustering.
   *
   * Reference: Benno Stein, Sven Meyer zu Eissen, and Frank Wiszbrock. On 
   * cluster validity and the information need of users. In Proceedings of the
   * Third IASTED International Conference on Artificial Intelligence and 
   * Applications, pages 216-221, Benalmadena, Spain, September 2003. 
   * ACTA Press.
   */
  protected double OriginalDunnIndex = Double.NaN;
  
  /**
   * Holds the minimum distance between any two clusters; numerator of the
   * original Dunn index.
   */
  protected double OriginalDunnIndexMinDistance = Double.NaN;
  
  /**
   * Holds the maximum diameter of all clusters; denominator of the
   * original Dunn index.
   */
  protected double OriginalDunnIndexMaxDiameter = Double.NaN;
  
  /**
   * Holds the Dunn index of the entire clustering as proposed by Bezdek et al.
   *
   * Reference: Benno Stein, Sven Meyer zu Eissen, and Frank Wiszbrock. On 
   * cluster validity and the information need of users. In Proceedings of the
   * Third IASTED International Conference on Artificial Intelligence and 
   * Applications, pages 216-221, Benalmadena, Spain, September 2003. 
   * ACTA Press.
   */
  protected double BezdekDunnIndex = Double.NaN;
  
  /**
   * Holds the minimum distance between any two clusters; numerator of the
   * Dunn index as proposed by Bezdek et al.
   */
  protected double BezdekDunnIndexMinDistance = Double.NaN;
  
  /**
   * Holds the maximum diameter of all clusters; denominator of the
   * Dunn index as proposed by Bezdek et al.
   */
  protected double BezdekDunnIndexMaxDiameter = Double.NaN;
  
  /**
   * Holds the S_Dbw index as proposed by Vazirgiannis et al.
   *
   * Reference: Michalis Vazirgiannis, Maria Halkidi, and Dimitrios Gunopulos. 
   * Uncertainty Handling and Quality Assessment in Data Mining, pp. 113-115.
   * Springer-Verlag, London, Berlin, 2003.
   */
  protected double SDbwIndex = Double.NaN;
  
  /**
   * Holds the average scattering for clusters component of the S_Dbw index as
   * proposed by Vazirgiannis et al.
   */
  protected double SDbwIndexAverageScattering = Double.NaN;
  
  /**
   * Holds the inter-cluster density component of the S_Dbw index as proposed 
   * by Vazirgiannis et al.
   */
  protected double SDbwIndexInterClusterDensity = Double.NaN;
  
  /**
   * Holds the average standard deviation of clusters required by the S_Dbw
   * index as proposed by Vazirgiannis et al.
   */
  protected double SDbwIndexAvgStdDevOfClusters = Double.NaN;
  
  /**
   * Holds the densities of the prototype instances (e.g., centroids) required
   * by the S_Dbw index as proposed by Vazirgiannis et al., whereas the data is
   * stored analogously to the array ClusterCardinality.
   */
  protected int[] SDbwIndexDensityOfPrototypes = null;
  
  /**
   * Holds the Overall Cluster Quality (0.5) index as proposed by He et al.
   *
   * Reference: Ji He, Ah-Hwee Tan, Chew-Lim Tan, and Sam-Yuan Sung. On 
   * quantitative evaluation of clustering systems. In Weili Wu, Hui Xiong, and
   * Shashi Shakhar, editors, Clustering and Information Retrieval, pp. 105-133.
   * Kluwer Academic Publishers, Boston, Dordrecht, 2004.
   */
  protected double OverallClusterQualityIndex05 = Double.NaN;
  
  /**
   * Holds the cluster compactness component of the Overall Cluster Quality
   * index as proposed by He et al.
   */
  protected double OcqIndexClusterCompactness = Double.NaN;
  
  /**
   * Holds the cluster separation component of the Overall Cluster Quality
   * index as proposed by He et al.
   */
  protected double OcqIndexClusterSeparation = Double.NaN;
  
  /**
   * Holds the generelazed variance of each clusters required by the Overall
   * Cluster Quality index as proposed by He et al., whereas the data is stored
   * analogously to the array ClusterCardinality.
   */
  protected double[] OcqIndexVarianceOfClusters = null;
  
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
  
  /**
   * Returns the average silhouette width of the entire clustering.
   *
   * @return the average silhouette width of the entire clustering
   */
  
  public double getAvgSilhouetteWidth() {
    
    return AvgSilhouetteWidth;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the Davies-Bouldin index of the entire clustering.
   *
   * @return the Davies-Bouldin index of the entire clustering
   */
  
  public double getDaviesBouldinIndex() {
    
    return DaviesBouldinIndex;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the original Dunn index of the entire clustering.
   *
   * @return the original Dunn index of the entire clustering
   */
  
  public double getOriginalDunnIndex() {
    
    return OriginalDunnIndex;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the Dunn index of the entire clustering as proposed by Bezdek et 
   * al.
   *
   * @return the Dunn index of the entire clustering as proposed by Bezdek et 
   * al.
   */
  
  public double getBezdekDunnIndex() {
    
    return BezdekDunnIndex;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the SDbwIndex index of the entire clustering as proposed by 
   * Vazirgiannis et al.
   *
   * @return the SDbwIndex index of the entire clustering as proposed by 
   * Vazirgiannis et al.
   */
  
  public double getSDbwIndex() {
    
    return SDbwIndex;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the Overall Cluster Quality (0.5) index as proposed by He et al.
   *
   * @return the Overall Cluster Quality (0.5) index as proposed by He et al.
   */
  
  public double getOverallClusterQualityIndex05() {
    
    return OverallClusterQualityIndex05;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Evaluates cluster quality by computing cluster cardinalities,
   * intra-cluster distances and squared intra-cluster distances.
   * Prerequisites: The specific clustering algorithm must have been executed.
   */
  
  protected void evaluateClusterValidity() {
    
    if (MyClusterer == null) {
      // guarantee that there exists an associated cluster
      throw new NullPointerException("Error: RelativeClusterValidityAssessor"
      + ".evaluateClusterValidity().MyClusterer == null!");
    }
    else {
      this.updateTaskProgress(TaskProgress.INDETERMINATE,
      "Assessing the Cluster Validity");
      this.outputLogEntry(false, "Assessing the cluster validity ...");
    }
    
    this.init();
    int numberOfClusters = MyClusterer.numberOfClusters();
    int numberOfTrainingInstances = MyClusterer.TrainingInstances
    .numInstances();
    // variables that temporarily hold the respective distance values
    // for the currently processed instance
    double intraClusterDistance = 0.0d;
    double intraCentroidDistance = 0.0d;
    double interClusterDistance = 0.0d;
    double interCentroidDistance = 0.0d;
    // array temporarily stores inter-centroid distances required to
    // compute the silhouette width's b value for each instance;
    double[] silhouetteWidthBs = new double[numberOfClusters];
    double silhouetteWidthMinB = 0.0d;
    double silhouetteWidthA = 0.0d;
    double silhouetteWidth = 0.0d;
    // temporary variables for computing the original Dunn index and the
    // Dunn index as proposed by Bezdek et al.
    double originalDunnIndexMinDistance = 0.0;
    double originalDunnIndexMaxDiameter = 0.0;
    double[][] bezdekDunnIndexDistances = new double[numberOfClusters]
    [numberOfClusters];
    double bezdekDunnIndexDiameter = 0.0;
    // temporary variable for the S_Dbw index
    int density = 0;
    
    for (int i = 0; i < numberOfClusters; i++) {
      for (int j = i + 1; j < numberOfClusters; j++) {
        // compute inter-centroid distance and related measures
        interCentroidDistance = MyClusterer.MyDistanceMeasure.computeDistance(
        MyClusterer.getRepresentativeInstance(i), 
        MyClusterer.getRepresentativeInstance(j), 
        MyClusterer.TrainingInstancesMetadata);
        SumInterCentroidDistance += interCentroidDistance;
        NumberOfInterCentroidDistances++;
        SumOfSquaredInterCentroidDistance += 
        (interCentroidDistance * interCentroidDistance);
      }
    }
    
    // approximate maximum progress for evaluation of internal quality
    long currentEvalProgress = 0;
    long maxEvalProgress = 2 *  numberOfTrainingInstances;
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      maxEvalProgress = numberOfTrainingInstances
      + ((long)MyClusterer.TrainingInstances.numInstances()
      * ((long)MyClusterer.TrainingInstances.numInstances() - 1L));
    }
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      maxEvalProgress += ((long)MyClusterer.numberOfClusters()
      * ((long)MyClusterer.numberOfClusters() - 1L)
      * (long)MyClusterer.TrainingInstances.numInstances());
    }
    
    // assign training instances to clusters as in the application phase
    // and compute the cardinality of each cluster
    for (int i = 0; i < numberOfTrainingInstances; i++) {
      currentEvalProgress++;
      if (i % 1000 == 0) {
        this.updateTaskProgress(TaskProgress.INDETERMINATE,
        "Assessment of Cluster Validity: " + (int)(100.0d
        * currentEvalProgress / (double)maxEvalProgress) + "% Completed");
        this.outputLogEntry(false, 
        "Assessment of cluster validity: " + (int)(100.0d
        * currentEvalProgress / (double)maxEvalProgress) + "% completed");
      }
      ClusterAssignments[i] = MyClusterer.clusterProcessedInstance(
      MyClusterer.TrainingInstances.instance(i));
      ClusterCardinality[ClusterAssignments[i]]++;
      TrainingInstancesPerCluster[ClusterAssignments[i]].add(
      MyClusterer.TrainingInstances.instance(i));
    }
    // compute the medata for each cluster
    for (int i = 0; i < numberOfClusters; i++) {
      TrainingInstancesMetadataPerCluster[i] = new InstancesMetadata(
      TrainingInstancesPerCluster[i]);
    }
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      // compute the first component of the S_Dbw index
      this.computeSDbwIndexAverageScattering();
    }
    
    for (int i = 0; i < numberOfTrainingInstances; i++) {
      currentEvalProgress++;
      if (i % 500 == 0) {
        this.updateTaskProgress(TaskProgress.INDETERMINATE,
        "Assessment of Cluster Validity: " + (int)(100.0d
        * currentEvalProgress / (double)maxEvalProgress) + "% Completed");
        this.outputLogEntry(false, 
        "Assessment of cluster validity: " + (int)(100.0d
        * currentEvalProgress / (double)maxEvalProgress) + "% completed");
      }
      // compute intra-centroid distance and related measures
      intraCentroidDistance = MyClusterer.MyDistanceMeasure.computeDistance(
      MyClusterer.TrainingInstances.instance(i),
      MyClusterer.getRepresentativeInstance(ClusterAssignments[i]),
      MyClusterer.TrainingInstancesMetadata);
      SumIntraCentroidDistances[ClusterAssignments[i]] 
      += intraCentroidDistance;
      SumOfSquaredIntraCentroidDistances[ClusterAssignments[i]] 
      += (intraCentroidDistance * intraCentroidDistance);
      // compute generalized variance of clusters for Overall Cluster
      // Quality index (0.5) with respect to instance i
      OcqIndexVarianceOfClusters[ClusterAssignments[i]] +=
      (intraCentroidDistance * intraCentroidDistance);
      if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
      || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
        
        // initialize variable related to silhouette width of instance i
        silhouetteWidthA = 0.0d;
        for (int l = 0; l < numberOfClusters; l++) {
          silhouetteWidthBs[l] = 0.0d;
        }
        // initialize variable related to Dunn indexes of instance i
        originalDunnIndexMinDistance = Double.POSITIVE_INFINITY;
        originalDunnIndexMaxDiameter = 0.0;
        // compute densities of prototype instances for S_Dbs index
        // with respect to instance i
        if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
          for (int j = 0; j < numberOfClusters; j++) {
            if (!Utils.gr(MyClusterer.MyDistanceMeasure.computeDistance(
            MyClusterer.TrainingInstances.instance(i),
            MyClusterer.getRepresentativeInstance(j),
            MyClusterer.TrainingInstancesMetadata),
            SDbwIndexAvgStdDevOfClusters)) {
              SDbwIndexDensityOfPrototypes[j]++;
            }
          }
        }
        
        for (int j = i + 1; j < numberOfTrainingInstances; j++) {
          currentEvalProgress++;
          if (ClusterAssignments[i] != ClusterAssignments[j]) {
            // compute inter-cluster distance and related measures
            interClusterDistance = MyClusterer.MyDistanceMeasure
            .computeDistance(MyClusterer.TrainingInstances.instance(i),
            MyClusterer.TrainingInstances.instance(j),
            MyClusterer.TrainingInstancesMetadata);
            SumInterClusterDistance += interClusterDistance;
            NumberOfInterClusterDistances++;
            SumOfSquaredInterClusterDistance += 
            (interClusterDistance * interClusterDistance);
            // compute measures for the silhouette width
            silhouetteWidthBs[ClusterAssignments[j]] 
            += interClusterDistance;
            // compute measures for Dunn indices
            if (Utils.sm(interClusterDistance, originalDunnIndexMinDistance)) {
              originalDunnIndexMinDistance = interClusterDistance;
            }
            bezdekDunnIndexDistances[ClusterAssignments[i]]
            [ClusterAssignments[j]] += interClusterDistance;
          }
          else {
            // ClusterAssignments[i] == ClusterAssignments[j]
            // compute intra-cluster distance and related measures
            intraClusterDistance = MyClusterer.MyDistanceMeasure
            .computeDistance(MyClusterer.TrainingInstances.instance(i),
            MyClusterer.TrainingInstances.instance(j),
            MyClusterer.TrainingInstancesMetadata);
            SumIntraClusterDistances[ClusterAssignments[i]] +=
            intraClusterDistance;
            NumberOfIntraClusterDistances[ClusterAssignments[i]]++;
            SumOfSquaredIntraClusterDistances[ClusterAssignments[i]] +=
            (intraClusterDistance * intraClusterDistance);
            // compute measures for the silhouette width
            silhouetteWidthA += intraClusterDistance;
            // compute measures for Dunn indices
            if (Utils.gr(intraClusterDistance, originalDunnIndexMaxDiameter)) {
              originalDunnIndexMaxDiameter = intraClusterDistance;
            }
          }
        }  // for (int j = i + 1; j < numberOfTrainingInstances; j++)
        
        // we minimize memory requirements (i.e., similarity matrix) by 
        // computing similarities twice
        for (int j = 0; j < i; j++) {
          currentEvalProgress++;
          if (ClusterAssignments[i] != ClusterAssignments[j]) {
            // compute inter-cluster distance and related measures
            interClusterDistance = MyClusterer.MyDistanceMeasure
            .computeDistance(MyClusterer.TrainingInstances.instance(i),
            MyClusterer.TrainingInstances.instance(j),
            MyClusterer.TrainingInstancesMetadata);
            // compute measures for the silhouette width
            silhouetteWidthBs[ClusterAssignments[j]] 
            += interClusterDistance;
            // compute measures for Dunn indices
            if (Utils.sm(interClusterDistance, originalDunnIndexMinDistance)) {
              originalDunnIndexMinDistance = interClusterDistance;
            }
            bezdekDunnIndexDistances[ClusterAssignments[i]]
            [ClusterAssignments[j]] += interClusterDistance;
          }
          else {
            // ClusterAssignments[i] == ClusterAssignments[j]
            // compute intra-cluster distance and related measures
            intraClusterDistance = MyClusterer.MyDistanceMeasure
            .computeDistance(MyClusterer.TrainingInstances.instance(i),
            MyClusterer.TrainingInstances.instance(j),
            MyClusterer.TrainingInstancesMetadata);
            // compute measures for the silhouette width
            silhouetteWidthA += intraClusterDistance;
            // compute measures for Dunn indices
            if (Utils.gr(intraClusterDistance, originalDunnIndexMaxDiameter)) {
              originalDunnIndexMaxDiameter = intraClusterDistance;
            }
          }
        }  // for (int j = 0; j < i; j++)
        
        // compute silhouette width for instance i
        if (ClusterCardinality[ClusterAssignments[i]] > 1) {
          silhouetteWidthA = silhouetteWidthA
          / (ClusterCardinality[ClusterAssignments[i]] - 1);
          silhouetteWidthMinB = Double.MAX_VALUE;
          for (int l = 0; l < numberOfClusters; l++) {
            silhouetteWidthBs[l] = silhouetteWidthBs[l] / ClusterCardinality[l];
            if (silhouetteWidthBs[l] < silhouetteWidthMinB
            && ClusterAssignments[i] != l) {
              silhouetteWidthMinB = silhouetteWidthBs[l];
            }
          }
          if (Utils.eq(silhouetteWidthMinB, silhouetteWidthA)) {
            // if silhouetteCoefficientA and silhouetteCoefficientMinB
            // both equaled zero, silhouetteCoefficient would be Double.NaN
            silhouetteWidth = 0.0d; 
          }
          else {
            silhouetteWidth = (silhouetteWidthMinB - silhouetteWidthA)
            / Math.max(silhouetteWidthA, silhouetteWidthMinB);
          }
        }
        else {
          silhouetteWidth = 0.0d;
        }
        AvgSilhouetteWidths[ClusterAssignments[i]] += silhouetteWidth;
        AvgSilhouetteWidth += silhouetteWidth;
        
        // compute measures for Dunn indices
        if (Utils.sm(originalDunnIndexMinDistance, 
        OriginalDunnIndexMinDistance)) {
          OriginalDunnIndexMinDistance = originalDunnIndexMinDistance;
        }
        if (Utils.gr(originalDunnIndexMaxDiameter, 
        OriginalDunnIndexMaxDiameter)) {
          OriginalDunnIndexMaxDiameter = originalDunnIndexMaxDiameter;
        }        
        
      }  // if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
         // || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL)
    }  //  for (int i = 0; i < numberOfTrainingInstances; i++)
    
    // compute silhouette width for the entire clustering
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      AvgSilhouetteWidth = AvgSilhouetteWidth / numberOfTrainingInstances;
      for (int i = 0; i < numberOfClusters; i++) {
        AvgSilhouetteWidths[i] = AvgSilhouetteWidths[i] / ClusterCardinality[i];
      }
    }
    
    for (int i = 0; i < numberOfClusters; i++) {
      // compute the number of empty and non-empty clusters
      if (ClusterCardinality[i] == 0) {
        NumberOfEmptyClusters++;
      }
      else {
        NumberOfNonEmptyClusters++;
      }
      // compute relative cluster cardinality
      RelativeClusterCardinality[i] = ClusterCardinality[i]
      / (double)numberOfTrainingInstances;
      // compute intra-cluster distance related measures
      StdDevIntraClusterDistances[i] = Double.POSITIVE_INFINITY;
      MeanIntraClusterDistances[i] = Double.NaN;
      if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
      || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
        if (NumberOfIntraClusterDistances[i] > 0) {
          MeanIntraClusterDistances[i] = SumIntraClusterDistances[i]
          / (double)NumberOfIntraClusterDistances[i];
        }
        if (NumberOfIntraClusterDistances[i] > 1) {
          StdDevIntraClusterDistances[i] = SumOfSquaredIntraClusterDistances[i]
          - ((SumIntraClusterDistances[i] * SumIntraClusterDistances[i])
          / (double)NumberOfIntraClusterDistances[i]);
          StdDevIntraClusterDistances[i] /= (NumberOfIntraClusterDistances[i]
          - 1);
          if (StdDevIntraClusterDistances[i] < 0) {
            StdDevIntraClusterDistances[i] = 0.0d;  // round StdDev to zero
          }
          StdDevIntraClusterDistances[i] = Math.sqrt(
          StdDevIntraClusterDistances[i]);
        }
        // compute statistics for intra-cluster distances over all cluster
        SumIntraClusterDistance += SumIntraClusterDistances[i];
        NumberOfIntraClusterDistance += NumberOfIntraClusterDistances[i];
        SumOfSquaredIntraClusterDistance +=
        SumOfSquaredIntraClusterDistances[i];
      }
      // compute intra-centroid distance related measures
      StdDevIntraCentroidDistances[i] = Double.POSITIVE_INFINITY;
      MeanIntraCentroidDistances[i] = Double.NaN;
      if (ClusterCardinality[i] > 0) {
        MeanIntraCentroidDistances[i] = SumIntraCentroidDistances[i]
        / (double)ClusterCardinality[i];
      }
      if (ClusterCardinality[i] > 1) {
        StdDevIntraCentroidDistances[i] = SumOfSquaredIntraCentroidDistances[i]
        - ((SumIntraCentroidDistances[i] * SumIntraCentroidDistances[i])
        / (double)ClusterCardinality[i]);
        StdDevIntraCentroidDistances[i] /= (ClusterCardinality[i] - 1);
        if (StdDevIntraCentroidDistances[i] < 0) {
          StdDevIntraCentroidDistances[i] = 0.0d;  // round StdDev to zero
        }
        StdDevIntraCentroidDistances[i] = Math.sqrt(
        StdDevIntraCentroidDistances[i]);
      }
      // compute statistics for intra-centroid distances over all cluster
      SumIntraCentroidDistance += SumIntraCentroidDistances[i];
      SumClusterCardinality += ClusterCardinality[i];
      SumOfSquaredIntraCentroidDistance +=
      SumOfSquaredIntraCentroidDistances[i];
      // compute measures for Dunn indices
      if ((EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
      || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL)
      && ClusterCardinality[i] > 0) {
        bezdekDunnIndexDiameter = 2.0d * MeanIntraCentroidDistances[i];
        if (Utils.gr(bezdekDunnIndexDiameter, BezdekDunnIndexMaxDiameter)) {
          BezdekDunnIndexMaxDiameter = bezdekDunnIndexDiameter;
        }
        for (int j = i + 1; j < numberOfClusters; j++) {
          bezdekDunnIndexDistances[i][j] = bezdekDunnIndexDistances[i][j]
          / (ClusterCardinality[i] * ClusterCardinality[j]);
          if (Utils.sm(bezdekDunnIndexDistances[i][j],
          BezdekDunnIndexMinDistance) && ClusterCardinality[j] > 0) {
            BezdekDunnIndexMinDistance = bezdekDunnIndexDistances[i][j];
          }
        }
      }
    }
    
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      // compute inter-cluster distance related measures
      StdDevInterClusterDistance = Double.POSITIVE_INFINITY;
      MeanInterClusterDistance = Double.NaN;
      if (NumberOfInterClusterDistances > 0) {
        MeanInterClusterDistance = SumInterClusterDistance
        / (double)NumberOfInterClusterDistances;
      }
      if (NumberOfInterClusterDistances > 1) {
        StdDevInterClusterDistance = SumOfSquaredInterClusterDistance
        - ((SumInterClusterDistance * SumInterClusterDistance)
        / (double)NumberOfInterClusterDistances);
        StdDevInterClusterDistance /= (NumberOfInterClusterDistances - 1);
        if (StdDevInterClusterDistance < 0) {
          StdDevInterClusterDistance = 0.0d;  // round StdDev to zero
        }
        StdDevInterClusterDistance = Math.sqrt(StdDevInterClusterDistance);
      }
    }
    // compute inter-centroid distance related measures
    StdDevInterCentroidDistance = Double.POSITIVE_INFINITY;
    MeanInterCentroidDistance = Double.NaN;
    if (NumberOfInterCentroidDistances > 0) {
      MeanInterCentroidDistance = SumInterCentroidDistance
      / (double)NumberOfInterCentroidDistances;
    }
    if (NumberOfInterCentroidDistances > 1) {
      StdDevInterCentroidDistance = SumOfSquaredInterCentroidDistance
      - ((SumInterCentroidDistance * SumInterCentroidDistance)
      / (double)NumberOfInterCentroidDistances);
      StdDevInterCentroidDistance /= (NumberOfInterCentroidDistances - 1);
      if (StdDevInterCentroidDistance < 0) {
        StdDevInterCentroidDistance = 0.0d;  // round StdDev to zero
      }
      StdDevInterCentroidDistance = Math.sqrt(StdDevInterCentroidDistance);
    }
    // compute intra-cluster distance related measures for all clusters
    StdDevIntraClusterDistance = Double.POSITIVE_INFINITY;
    MeanIntraClusterDistance = Double.NaN;
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      if (NumberOfIntraClusterDistance > 0) {
        MeanIntraClusterDistance = SumIntraClusterDistance
        / (double)NumberOfIntraClusterDistance;
      }
      if (NumberOfIntraClusterDistance > 1) {
        StdDevIntraClusterDistance = SumOfSquaredIntraClusterDistance
        - ((SumIntraClusterDistance * SumIntraClusterDistance)
        / (double)NumberOfIntraClusterDistance);
        StdDevIntraClusterDistance /= (NumberOfIntraClusterDistance - 1);
        if (StdDevIntraClusterDistance < 0) {
          StdDevIntraClusterDistance = 0.0d;  // round StdDev to zero
        }
        StdDevIntraClusterDistance = Math.sqrt(StdDevIntraClusterDistance);
      }
    }
    // compute intra-centroid distance related measures for all cluster
    StdDevIntraCentroidDistance = Double.POSITIVE_INFINITY;
    MeanIntraCentroidDistance = Double.NaN;
    if (SumClusterCardinality > 0) {
      MeanIntraCentroidDistance = SumIntraCentroidDistance
      / (double)SumClusterCardinality;
    }
    if (SumClusterCardinality > 1) {
      StdDevIntraCentroidDistance = SumOfSquaredIntraCentroidDistance
      - ((SumIntraCentroidDistance * SumIntraCentroidDistance)
      / (double)SumClusterCardinality);
      StdDevIntraCentroidDistance /= (SumClusterCardinality - 1);
      if (StdDevIntraCentroidDistance < 0) {
        StdDevIntraCentroidDistance = 0.0d;  // round StdDev to zero
      }
      StdDevIntraCentroidDistance = Math.sqrt(StdDevIntraCentroidDistance);
    }
    
    this.computeDaviesBouldinIndex();

    // compute Dunn indices and Overall Cluster Quality index
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      this.computeOverallClusterQualityIndex05();
      OriginalDunnIndex = OriginalDunnIndexMinDistance
      / OriginalDunnIndexMaxDiameter;
      BezdekDunnIndex = BezdekDunnIndexMinDistance
      / BezdekDunnIndexMaxDiameter;
    }
    else {
      OriginalDunnIndex = Double.NaN;
      BezdekDunnIndex = Double.NaN;
    }
    
    // compute Dunn indices and S_Dbw index
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL
    && numberOfClusters > 1) {
      currentEvalProgress = this.computeSDbwIndexInterClusterDensity(
      currentEvalProgress, maxEvalProgress);
    }
    else {
      SDbwIndex = Double.NaN;
      SDbwIndexInterClusterDensity = Double.NaN;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing internal cluster quality measures and
   * details about all cluster centroids to existing and open HTML report.
   * The text file remains open.
   */
  
  protected void createHtmlReportClusterValiditySection() {
    
    this.updateTaskProgress(TaskProgress.INDETERMINATE,
    "Creating HTML Report Section on Quality Assessment");
    if (MyClusterer.VerboseMode) {
      System.out.println(
      "Creating HTML report section on quality assessment ...");
    }
    MyClusterer.HtmlReport.setNextLine("<a name=\"CLUSTER_VALIDITY\">"
    + "<h3>Cluster Validity Assessment</h3></a>");
    MyClusterer.HtmlReport.setNextLine(
    "<p><small><a href=\"#TOP\">Top of the Page</a> - "
    + "<a href=\"#PARAMETERS\">Parameter Settings</a> - "
    + "<a href=\"#LOG\">Execution Log</a> - " + (MyClusterer.numberOfClusters()
    > 0 ? "<a href=\"#CLUSTER0\">First Cluster</a> - " : "")
    + "<a href=\"#BOP\">Bottom of the Page</a></small></p>");
    
    MyClusterer.HtmlReport.setNextLine("<p>The term &quot;Prototype&quot; "
    + "corresponds to a &quot;" + MyClusterer.getNameOfRepresentativeInstance()
    + "&quot; in the clustering algorithm employed. Warning: Evaluate only "
    + "cluster validity indices that are appropriate for the executed "
    + "clustering algorithm.</p>");
    
    int nextID = this.createOrOpenClusterValidityIndicesFile();
    StringBuffer arffLine = new StringBuffer(10000);
    arffLine.append(nextID);
    arffLine.append(",\"" + MyClusterer.getClass().toString() + "\"");
    arffLine.append(",\"" + Tools.removeQuotesAndNewLines(MyClusterer
    .getOptionsAsString().trim()) + "\"");
    arffLine.append("," + MyClusterer.TrainingInstances.numInstances());
    
    MyClusterer.HtmlReport.setNextLine("<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Index ID</th>"
    + "<th align=\"left\" valign=\"top\">Cluster Validity Index</th>"
    + "<th align=\"right\" valign=\"top\">Value</th>"
    + "<th align=\"right\" valign=\"top\">Range</th>"
    + "<th align=\"right\" valign=\"top\">Objective</th>"
    + "</tr>");
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">A.i</td>"
    + "<td align=\"left\" valign=\"top\">Number of Clusters</td>"
    + "<td align=\"right\" valign=\"top\">" 
    + MyClusterer.numberOfClusters() + "</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "</tr>");
    arffLine.append("," + MyClusterer.numberOfClusters());
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">A.ii</td>"
    + "<td align=\"left\" valign=\"top\">Number of Non-Empty Clusters</td>"
    + "<td align=\"right\" valign=\"top\">" + NumberOfNonEmptyClusters + "</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "</tr>");
    arffLine.append("," + NumberOfNonEmptyClusters);
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">A.iii</td>"
    + "<td align=\"left\" valign=\"top\">Number of Empty Clusters</td>"
    + "<td align=\"right\" valign=\"top\">" + NumberOfEmptyClusters + "</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "</tr>");
    arffLine.append("," + NumberOfEmptyClusters);
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\"><a href=\"#B\">B</a></td>"
    + "<td align=\"left\" valign=\"top\">Davies-Bouldin Index</td>"
    + "<td align=\"right\" valign=\"top\">"
    + this.formatDouble(this.DaviesBouldinIndex) + "</td>"
    + "<td align=\"right\" valign=\"top\">[0; oo]</td>"
    + "<td align=\"right\" valign=\"top\">Minimize!</td>"
    + "</tr>");
    arffLine.append("," + this.DaviesBouldinIndex);
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#C\">C</a></td>"
      + "<td align=\"left\" valign=\"top\">Avg(Silhouette Width)</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(AvgSilhouetteWidth) + "</td>"
      + "<td align=\"right\" valign=\"top\">[-1; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Maximize!</td>"
      + "</tr>");      
      arffLine.append("," + this.AvgSilhouetteWidth);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#D\">D.i</a></td>"
      + "<td align=\"left\" valign=\"top\">Original Dunn Index ("
      + this.formatDouble(OriginalDunnIndexMinDistance) + " / "
      + this.formatDouble(OriginalDunnIndexMaxDiameter) + ")</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(OriginalDunnIndex) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; oo]</td>"
      + "<td align=\"right\" valign=\"top\">Maximize!</td>"
      + "</tr>");      
      arffLine.append("," + this.OriginalDunnIndex);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#D\">D.ii</a></td>"
      + "<td align=\"left\" valign=\"top\">Bezdek's Dunn Index ("
      + this.formatDouble(BezdekDunnIndexMinDistance) + " / "
      + this.formatDouble(BezdekDunnIndexMaxDiameter) + ")</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(BezdekDunnIndex) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; oo]</td>"
      + "<td align=\"right\" valign=\"top\">Maximize!</td>"
      + "</tr>");      
      arffLine.append("," + this.BezdekDunnIndex);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">E.i</td>"
      + "<td align=\"left\" valign=\"top\">"
      + "Avg(Instance-Based Within-Cluster Distance)</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.MeanIntraClusterDistance) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");      
      arffLine.append("," + this.MeanIntraClusterDistance);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">E.ii</td>"
      + "<td align=\"left\" valign=\"top\">"
      + "StdDev(Instance-Based Within-Cluster Distance)</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.StdDevIntraClusterDistance) + "</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "</tr>");      
    }
    else {
      arffLine.append(",?,?,?,?");
    }
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">F.i</td>"
    + "<td align=\"left\" valign=\"top\">"
    + "Avg(Prototype-Based Within-Cluster Distance)</td>"
    + "<td align=\"right\" valign=\"top\">"
    + this.formatDouble(this.MeanIntraCentroidDistance) + "</td>"
    + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
    + "<td align=\"right\" valign=\"top\">Minimize!</td>"
    + "</tr>");
    arffLine.append("," + this.MeanIntraCentroidDistance);
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">F.ii</td>"
    + "<td align=\"left\" valign=\"top\">"
    + "StdDev(Prototype-Based Within-Cluster Distance)</td>"
    + "<td align=\"right\" valign=\"top\">"
    + this.formatDouble(this.StdDevIntraCentroidDistance) + "</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "</tr>");
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">G.i</td>"
      + "<td align=\"left\" valign=\"top\">"
      + "Avg(Instance-Based Between-Clusters Distance)</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.MeanInterClusterDistance) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Maximize!</td>"
      + "</tr>");
      arffLine.append("," + this.MeanInterClusterDistance);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">G.ii</td>"
      + "<td align=\"left\" valign=\"top\">"
      + "StdDev(Instance-Based Between-Clusters Distance)</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.StdDevInterClusterDistance) + "</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "</tr>");      
    }
    else {
      arffLine.append(",?");
    }
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">H.i</td>"
    + "<td align=\"left\" valign=\"top\">"
    + "Avg(Prototype-Based Between-Clusters Distance)</td>"
    + "<td align=\"right\" valign=\"top\">"
    + this.formatDouble(this.MeanInterCentroidDistance) + "</td>"
    + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
    + "<td align=\"right\" valign=\"top\">Maximize!</td>"
    + "</tr>");
    arffLine.append("," + this.MeanInterCentroidDistance);
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">H.ii</td>"
    + "<td align=\"left\" valign=\"top\">"
    + "StdDev(Prototype-Based Between-Clusters Distance)</td>"
    + "<td align=\"right\" valign=\"top\">"
    + this.formatDouble(this.StdDevInterCentroidDistance) + "</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
    + "</tr>");
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">I.i</td>"
      + "<td align=\"left\" valign=\"top\">"
      + "[1 - Avg(Instance-Based Between-Clusters Distance)] +<br>"
      + "Avg(Instance-Based Within-Cluster Distance)</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(1.0d - this.MeanInterClusterDistance
      + this.MeanIntraClusterDistance) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; 2]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      arffLine.append("," + (1.0d - this.MeanInterClusterDistance
      + this.MeanIntraClusterDistance));
    }
    else {
      arffLine.append(",?");
    }
    MyClusterer.HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">I.ii</td>"
    + "<td align=\"left\" valign=\"top\">"
    + "[1 - Avg(Prototype-Based Between-Clusters Distance)] +<br>"
    + "Avg(Prototype-Based Within-Cluster Distance)</td>"
    + "<td align=\"right\" valign=\"top\">"
    + this.formatDouble(1.0d - this.MeanInterCentroidDistance
    + this.MeanIntraCentroidDistance) + "</td>"
    + "<td align=\"right\" valign=\"top\">[0; 2]</td>"
    + "<td align=\"right\" valign=\"top\">Minimize!</td>"
    + "</tr>");
    arffLine.append("," + (1.0d - this.MeanInterCentroidDistance
    + this.MeanIntraCentroidDistance));
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
    || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#J\">J.i</a></td>"
      + "<td align=\"left\" valign=\"top\">Overall Cluster Quality Index "
      + "(0.5 * " + this.formatDouble(OcqIndexClusterCompactness) + " + 0.5 * "
      + this.formatDouble(OcqIndexClusterSeparation) + ")</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.OverallClusterQualityIndex05) + "</td>"
      + "<td align=\"right\" valign=\"top\">(0.183; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      arffLine.append("," + this.OverallClusterQualityIndex05);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#J\">J.ii</a></td>"
      + "<td align=\"left\" valign=\"top\">OCQ Index: Cluster Compactness</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.OcqIndexClusterCompactness) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      arffLine.append("," + this.OcqIndexClusterCompactness);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#J\">J.iii</a></td>"
      + "<td align=\"left\" valign=\"top\">OCQ Index: Cluster Separation</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.OcqIndexClusterSeparation) + "</td>"
      + "<td align=\"right\" valign=\"top\">(0.367; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      arffLine.append("," + this.OcqIndexClusterSeparation);
    }
    else {
      arffLine.append(",?,?,?");
    }
    if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#K\">K.i</a></td>"
      + "<td align=\"left\" valign=\"top\">SDbw Index ("
      + this.formatDouble(SDbwIndexAverageScattering) + " + "
      + this.formatDouble(SDbwIndexInterClusterDensity) + ")</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(SDbwIndex) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; 2]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      arffLine.append("," + this.SDbwIndex);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#K\">K.ii</a></td>"
      + "<td align=\"left\" valign=\"top\">"
      + "SDbw Index: Average Scattering for Clusters</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(SDbwIndexAverageScattering) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      arffLine.append("," + this.SDbwIndexAverageScattering);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#K\">K.iii</a></td>"
      + "<td align=\"left\" valign=\"top\">"
      + "SDbw Index: Average Inter-Cluster Density</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(SDbwIndexInterClusterDensity) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      arffLine.append("," + this.SDbwIndexInterClusterDensity);
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#K\">K.iv</a></td>"
      + "<td align=\"left\" valign=\"top\">"
      + "SDbw Index: Average Standard Deviation of Clusters</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(SDbwIndexAvgStdDevOfClusters) + "</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "</tr>");
      arffLine.append("," + this.SDbwIndexAvgStdDevOfClusters);
    }
    else {
      arffLine.append(",?,?,?,?");
    }
    MyClusterer.HtmlReport.setNextLine("</table>");
    
    this.appendClusterValidityIndicesToFile(arffLine.toString());
    this.closeClusterValidityIndicesFile();
    
    MyClusterer.HtmlReport.setNextLine(
    "<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Cluster ID</th>"
    + "<th align=\"right\" valign=\"top\">Abs. Cluster<br>Cardinality</th>"
    + "<th align=\"right\" valign=\"top\">Rel. Cluster<br>Cardinality</th>"
    + "<th align=\"right\" valign=\"top\">Davies-<br>Bouldin<br>Index of<br>"
    + "Cluster</th>"
    + "<th align=\"right\" valign=\"top\">Avg(Silhou-<br>ette Width)</th>"
    + "<th align=\"right\" valign=\"top\">Avg(Instance-<br>Based Within-<br>"
    + "Cluster Distance)</th>"
    + "<th align=\"right\" valign=\"top\">Avg(Prototype-<br>Based Within-<br>"
    + "Cluster Distance)</th></tr>");
    for (int i = 0; i < MyClusterer.numberOfClusters()
    && MyClusterer.getRepresentativeInstance(i) != null; i++) {
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td  align=\"left\" valign=\"top\"><a href=\"#CLUSTER" + i
      + "\">Cluster&nbsp;" + i + "</a></td>"
      + "<td align=\"right\" valign=\"top\">" + ClusterCardinality[i] + "</td>"
      + "<td align=\"right\" valign=\"top\">" 
      + this.formatDouble(RelativeClusterCardinality[i]) + "</td>"
      + "<td align=\"right\" valign=\"top\">" 
      + this.formatDouble(DaviesBouldinIndexes[i]) + "</td>"
      + "<td align=\"right\" valign=\"top\">"
      + ((EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
      || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL)
      ? this.formatDouble(AvgSilhouetteWidths[i]) : "&nbsp;")
      + "</td>"
      + "<td align=\"right\" valign=\"top\">"
      + ((EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
      || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL)
      ? this.formatDouble(MeanIntraClusterDistances[i]) : "&nbsp;")
      + "</td>"
      + "<td align=\"right\" valign=\"top\">" 
      + this.formatDouble(MeanIntraCentroidDistances[i]) + "</td>"
      + "</tr>");
    }
    MyClusterer.HtmlReport.setNextLine("</table>");
    
    MyClusterer.HtmlReport.setNextLine("<p><small>"
    + "<a name=\"B\">Davies-Bouldin Index:</a> See for example Anil K. Jain "
    + "and Richard C. Dubes. Algorithms for Clustering Data, pp. 185-187. "
    + "Prentice Hall, Englewood Cliffs, 1988.</small></p>");
    MyClusterer.HtmlReport.setNextLine("<p><small>"
    + "<a name=\"C\">Avg(Silhouette Width):</a> See for example Leonard "
    + "Kaufman and Peter J. Rousseeuw. Finding Groups in Data: An Introduction "
    + "to Cluster Analysis, pp. 83-88. John Wiley & Sons, New York, "
    + "Chichester, 1990.</small></p>");
    MyClusterer.HtmlReport.setNextLine("<p><small>"
    + "<a name=\"D\">Original Dunn Index and Bezdek's Dunn Index:</a> See for "
    + "example Benno Stein, Sven Meyer zu Eissen, and Frank Wiszbrock. On "
    + "cluster validity and the information need of users. In Proceedings of "
    + "the Third IASTED International Conference on Artificial Intelligence "
    + "and Applications, pp. 216-221, Benalmadena, Spain, September 2003. "
    + "ACTA Press.</small></p>");
    MyClusterer.HtmlReport.setNextLine("<p><small>"
    + "<a name=\"J\">Overall Cluster Quality Index:</a> See "
    + "Ji He, Ah-Hwee Tan, Chew-Lim Tan, and Sam-Yuan Sung. On quantitative "
    + "evaluation of clustering systems. In Weili Wu, Hui Xiong, and Shashi "
    + "Shakhar, editors, Clustering and Information Retrieval, pp. 105-133. "
    + "Kluwer Academic Publishers, Boston, Dordrecht, 2004.</small></p>");
    MyClusterer.HtmlReport.setNextLine("<p><small>"
    + "<a name=\"K\">SDbw Index:</a> See for example Michalis Vazirgiannis, "
    + "Maria Halkidi, and Dimitrios Gunopulos. Uncertainty Handling and "
    + "Quality Assessment in Data Mining, pp. 113-115. Springer-Verlag, "
    + "London, Berlin, 2003.</small></p>");
    
    TreeMap sortedNumericAttributes = null;
    double lift = 0.0d;
    for (int i = 0; i < MyClusterer.numberOfClusters()
    && MyClusterer.getRepresentativeInstance(i) != null; i++) {
      MyClusterer.HtmlReport.setNextLine("<a name=\"CLUSTER" + i
      + "\"><h3>Summary of Cluster " + i + "</h3></a>");
      MyClusterer.HtmlReport.setNextLine(
      "<p><small><a href=\"#TOP\">Top of the Page</a> - "
      + "<a href=\"#CLUSTER_VALIDITY\">Cluster Validity Asessment</a> - "
      + (i > 0 ? "<a href=\"#CLUSTER" + (i - 1) + "\">Previous Cluster</a> - "
      : "") + (i < (MyClusterer.numberOfClusters() - 1) ? "<a href=\"#CLUSTER"
      + (i + 1) + "\">Next Cluster</a> - " : "")
      + "<a href=\"#BOP\">Bottom of the Page</a></small></p>");
      
      MyClusterer.HtmlReport.setNextLine("<p>The term &quot;Prototype&quot; "
      + " corresponds to a &quot;" + MyClusterer
      .getNameOfRepresentativeInstance() + "&quot; in the clustering algorithm "
      + "employed. Warning: Evaluate only cluster validity indices that are "
      + "appropriate for the executed clustering algorithm.</p>");
      
      MyClusterer.HtmlReport.setNextLine("<table border=\"1\"><tr>"
      + "<th align=\"left\" valign=\"top\">Index ID</th>"
      + "<th align=\"left\" valign=\"top\">Cluster Validity Index</th>"
      + "<th align=\"right\" valign=\"top\">Value</th>"
      + "<th align=\"right\" valign=\"top\">Range</th>"
      + "<th align=\"right\" valign=\"top\">Objective</th>"
      + "</tr>");
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">A.i</td>"
      + "<td align=\"left\" valign=\"top\">Abs. Cluster Cardinality</td>"
      + "<td align=\"right\" valign=\"top\">" + ClusterCardinality[i] + "</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "</tr>");
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">A.ii</td>"
      + "<td align=\"left\" valign=\"top\">Rel. Cluster Cardinality</td>"
      + "<td align=\"right\" valign=\"top\">" 
      + this.formatDouble(RelativeClusterCardinality[i]) + "</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "</tr>");
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">B</td>"
      + "<td align=\"left\" valign=\"top\">Davies-Bouldin Index for "
      + "Cluster " + i + "</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.DaviesBouldinIndexes[i]) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; oo]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      ClusterDigests[i] += "; Cardinality: " + ClusterCardinality[i];

      if (EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_TRUE
      || EvaluateInternalQuality == ASSESS_CLUSTER_VALIDITY_FULL) {
        MyClusterer.HtmlReport.setNextLine("<tr>"
        + "<td align=\"left\" valign=\"top\">C</td>"
        + "<td align=\"left\" valign=\"top\">Avg(Silhouette Width)</td>"
        + "<td align=\"right\" valign=\"top\">"
        + this.formatDouble(AvgSilhouetteWidths[i]) + "</td>"
        + "<td align=\"right\" valign=\"top\">[-1; 1]</td>"
        + "<td align=\"right\" valign=\"top\">Maximize!</td>"
        + "</tr>");
        MyClusterer.HtmlReport.setNextLine("<tr>"
        + "<td align=\"left\" valign=\"top\">D.i</td>"
        + "<td align=\"left\" valign=\"top\">"
        + "Avg(Instance-Based Within-Cluster Distance)</td>"
        + "<td align=\"right\" valign=\"top\">"
        + this.formatDouble(this.MeanIntraClusterDistances[i]) + "</td>"
        + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
        + "<td align=\"right\" valign=\"top\">Minimize!</td>"
        + "</tr>");
        MyClusterer.HtmlReport.setNextLine("<tr>"
        + "<td align=\"left\" valign=\"top\">D.ii</td>"
        + "<td align=\"left\" valign=\"top\">"
        + "StdDev(Instance-Based Within-Cluster Distance)</td>"
        + "<td align=\"right\" valign=\"top\">"
        + this.formatDouble(this.StdDevIntraClusterDistances[i]) + "</td>"
        + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
        + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
        + "</tr>");
      }
      if (ClusterCardinality[i] > 0) {
        ClusterDigests[i] += "; Avg(Intra-Centroid Distance): "
        + this.formatDouble(this.MeanIntraCentroidDistances[i]);
        ClusterDigests[i] += "; StdDev(Intra-Centroid Distance): "
        + this.formatDouble(this.StdDevIntraCentroidDistances[i]);
      }
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">E.i</td>"
      + "<td align=\"left\" valign=\"top\">"
      + "Avg(Prototype-Based Within-Cluster Distance)</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.MeanIntraCentroidDistances[i]) + "</td>"
      + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
      + "<td align=\"right\" valign=\"top\">Minimize!</td>"
      + "</tr>");
      MyClusterer.HtmlReport.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">E.ii</td>"
      + "<td align=\"left\" valign=\"top\">"
      + "StdDev(Prototype-Based Within-Cluster Distance)</td>"
      + "<td align=\"right\" valign=\"top\">"
      + this.formatDouble(this.StdDevIntraCentroidDistances[i]) + "</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "<td align=\"right\" valign=\"top\">&nbsp;</td>"
      + "</tr>");
      MyClusterer.HtmlReport.setNextLine("</table>");
      // cluster digests comprise only the five most dicriminating attributes
      sortedNumericAttributes = new TreeMap();
      lift = 0.0d;
      MyClusterer.HtmlReport.setNextLine("<table border=\"1\"><tr>"
      + "<th align=\"left\" valign=\"top\">Attribute<br>Name</th>"
      + "<th align=\"left\" valign=\"top\">Attribute<br>Type</th>"
      + "<th align=\"right\" valign=\"top\">Value of <br>Prototype</th>"
      + "<th align=\"right\" valign=\"top\">Avg(Value of<br>Population)</th>"
      + "<th align=\"right\" valign=\"top\">Lift Value</th>" + "</tr>");
      for (int j = 0; j < MyClusterer.TrainingInstances.numAttributes(); j++) {
        if (MyClusterer.TrainingInstances.attribute(j).isNominal()) {
          MyClusterer.HtmlReport.setNextLine("<tr>"
          + "<td align=\"left\" valign=\"top\">" + MyClusterer
          .TrainingInstancesMetadata.getNameOfAttribute(j) + "</td>"
          + "<td align=\"left\" valign=\"top\">" + MyClusterer
          .TrainingInstancesMetadata.getTypeOfAttributeAsString(j) + "</td>"
          + "<td align=\"right\" valign=\"top\">" + MyClusterer
          .TrainingInstances.attribute(j).value((int)MyClusterer
          .getRepresentativeInstance(i).value(j)) + "</td>"
          + "<td align=\"right\" valign=\"top\">" + MyClusterer
          .TrainingInstances.attribute(j).value((int)MyClusterer
          .TrainingInstances.meanOrMode(j)) + "</td>"
          + "<td align=\"right\" valign=\"top\">N/A</td>" + "</tr>");
        }
        else {
          lift = MyClusterer.getRepresentativeInstance(i).value(j)
          / MyClusterer.TrainingInstancesMetadata.getMeanOfAttribute(j);
          if (ClusterCardinality[i] > 0 && !Utils.eq(lift, 0d)
          && !Double.isNaN(lift)) {
            sortedNumericAttributes.put(new Double((lift >= 1 ? (lift * -1d)
            : (-1.0d / lift))), MyClusterer.TrainingInstancesMetadata
            .getNameOfAttribute(j) + (Utils.gr(lift, 1d) ? " (+" + this
            .formatDouble((lift * 100d), 0, false) + "%)" : " (-" + this
            .formatDouble(((1.0d - lift) * 100d), 0, false) + "%)"));
          }
          MyClusterer.HtmlReport.setNextLine("<tr>"
          + "<td align=\"left\" valign=\"top\">" + MyClusterer
          .TrainingInstancesMetadata.getNameOfAttribute(j) + "</td>"
          + "<td align=\"left\" valign=\"top\">" + MyClusterer
          .TrainingInstancesMetadata.getTypeOfAttributeAsString(j) + "</td>"
          + "<td align=\"right\" valign=\"top\">" 
          + this.formatDouble(MyClusterer.getRepresentativeInstance(i)
          .value(j)) + "</td>"
          + "<td align=\"right\" valign=\"top\">" 
          + this.formatDouble(MyClusterer.TrainingInstancesMetadata
          .getMeanOfAttribute(j)) + "</td>"
          + "<td align=\"right\" valign=\"top\">" + (Double.isNaN(lift) 
          || Utils.eq(0d, lift) || Utils.eq(1d, lift) ? "&nbsp;" : (Utils
          .gr(lift, 1d) ? "+" + this.formatDouble((lift * 100d), 0, false) + "%"
          : "-" + this.formatDouble(((1.0d - lift) * 100d), 0, false) + "%")) 
          + "</td></tr>");
        }
      }
      if (ClusterCardinality[i] > 0 && sortedNumericAttributes.size() > 0) {
        ClusterDigests[i] += "; Discriminative Features:";
        int counter = 0;
        Iterator iterator = sortedNumericAttributes.keySet().iterator();
        while (iterator.hasNext() && counter++ < 5) {
          ClusterDigests[i] += (counter == 1 ? " " : ", ")
          + (String)sortedNumericAttributes.get((Double)iterator.next());
        }
      }
      MyClusterer.HtmlReport.setNextLine("</table>");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Initializes all attributes that are required to assess the cluster 
   * validity.
   */
  
  protected void init() {
    
    super.init();
    
    AvgSilhouetteWidths = new double[MyClusterer.numberOfClusters()];
    DaviesBouldinIndexes = new double[MyClusterer.numberOfClusters()];
    SDbwIndexDensityOfPrototypes = new int[MyClusterer.numberOfClusters()];
    OcqIndexVarianceOfClusters = new double[MyClusterer.numberOfClusters()];
        
    for (int i = 0; i < MyClusterer.numberOfClusters(); i++) {
      AvgSilhouetteWidths[i] = 0.0d;
      DaviesBouldinIndexes[i] = 0.0d;
      SDbwIndexDensityOfPrototypes[i] = 0;
      OcqIndexVarianceOfClusters[i] = 0.0d;
    }
    
    AvgSilhouetteWidth = 0.0d;
    DaviesBouldinIndex = 0.0d;
    OriginalDunnIndex = 0.0d;
    OriginalDunnIndexMinDistance = Double.POSITIVE_INFINITY;
    OriginalDunnIndexMaxDiameter = 0.0d;
    BezdekDunnIndex = 0.0d;
    BezdekDunnIndexMinDistance = Double.POSITIVE_INFINITY;
    BezdekDunnIndexMaxDiameter = 0.0d;
    SDbwIndex = 0.0d;
    SDbwIndexAverageScattering = 0.0d;
    SDbwIndexInterClusterDensity = 0.0d;
    SDbwIndexAvgStdDevOfClusters = 0.0d;
    OverallClusterQualityIndex05 = 0.0d;
    OcqIndexClusterCompactness = 0.0d;
    OcqIndexClusterSeparation = 0.0d;
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Computes the Davies-Bouldin index of each cluster and for the entire
   * clustering. The notation is according to Jain and Dubes (1988, pp. 
   * 185-187).
   */
  private void computeDaviesBouldinIndex() {
    
    DaviesBouldinIndex = 0.0d;
    int numberOfClusters = MyClusterer.numberOfClusters();
    double daviesBouldinEj = 0.0d;
    double daviesBouldinEk = 0.0d;
    double daviesBouldinMjk = 0.0d;
    double daviesBouldinRjk = 0.0d;
    double daviesBouldinMaxRk = 0.0d;
    
    for (int k = 0; k < numberOfClusters; k++) {
      if (ClusterCardinality[k] > 0) {
        // cluster k is not empty
        daviesBouldinEk = MeanIntraCentroidDistances[k];
        daviesBouldinMaxRk = 0.0d;
        for (int j = 0; j < numberOfClusters; j++) {
          daviesBouldinEj = MeanIntraCentroidDistances[j];
          daviesBouldinMjk = MyClusterer.MyDistanceMeasure.computeDistance(
          MyClusterer.getRepresentativeInstance(j),
          MyClusterer.getRepresentativeInstance(k),
          MyClusterer.TrainingInstancesMetadata);
          daviesBouldinRjk = (daviesBouldinEj + daviesBouldinEk)
          / daviesBouldinMjk;
          if (k != j && Utils.gr(daviesBouldinRjk, daviesBouldinMaxRk)) {
            daviesBouldinMaxRk = daviesBouldinRjk;
          }
        }
        DaviesBouldinIndexes[k] = daviesBouldinMaxRk;
        DaviesBouldinIndex = DaviesBouldinIndex + DaviesBouldinIndexes[k];
      }
      else {
        // cluster k is empty
        DaviesBouldinIndexes[k] = Double.NaN;
      }
    }
    
    if (numberOfClusters > 1) {
      DaviesBouldinIndex = DaviesBouldinIndex / NumberOfNonEmptyClusters;
    }
    else {
      DaviesBouldinIndex = Double.NaN;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Computes the average scattering for clusters for the S_Dbw index.
   * The notation is according to vazirgiannis et al. (2003, pp. 113-115).
   */
  private void computeSDbwIndexAverageScattering() {
    
    double variance = 0.0d;
    double[] l2NormStdDevOfClusters = new double[MyClusterer
    .numberOfClusters()];
    double l2NormStdDevOfClustering = 0.0d;
    SDbwIndexAvgStdDevOfClusters = 0.0d;
    
    for (int i = 0; i < MyClusterer.TrainingInstancesMetadata
    .getNumberOfAttributes(); i++) {
      variance = MyClusterer.TrainingInstancesMetadata
      .getVarianceOfAttribute(i);
      if (!(Double.isNaN(variance) || Double.isInfinite(variance))) {
        l2NormStdDevOfClustering += (variance * variance);
      }
    }
    l2NormStdDevOfClustering = Math.sqrt(l2NormStdDevOfClustering);
    
    for (int i = 0; i < MyClusterer.numberOfClusters(); i++) {
      l2NormStdDevOfClusters[i] = 0.0d; 
      for (int j = 0; j < TrainingInstancesMetadataPerCluster[i]
      .getNumberOfAttributes(); j++) {
        variance = TrainingInstancesMetadataPerCluster[i]
        .getVarianceOfAttribute(j);
        if (!(Double.isNaN(variance) || Double.isInfinite(variance))) {
          l2NormStdDevOfClusters[i] += (variance * variance);
        }
      }
      l2NormStdDevOfClusters[i] = Math.sqrt(l2NormStdDevOfClusters[i]);
      SDbwIndexAvgStdDevOfClusters += l2NormStdDevOfClusters[i];
    }
    
    SDbwIndexAvgStdDevOfClusters = Math.sqrt(SDbwIndexAvgStdDevOfClusters)
    / MyClusterer.numberOfClusters();
    SDbwIndexAverageScattering = SDbwIndexAvgStdDevOfClusters 
    / l2NormStdDevOfClustering;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Computes the inter-cluster density for the S_Dbw index. The notation is
   * according to vazirgiannis et al. (2003, pp. 113-115).
   * @param pCurrentProgress the current value of the progress indicator
   * @param pMaxProgress the maximum value of the progress indicator
   * @return the new value of the progress indicator
   */
  private long computeSDbwIndexInterClusterDensity(long pCurrentProgress,
  long pMaxProgress) {
    
    // for (int j = 0; j < MyClusterer.numberOfClusters(); j++) {
    //   System.out.println("SDbwIndexDensityOfPrototypes[" + j + "]"
    //   + SDbwIndexDensityOfPrototypes[j]);
    // }
    
    SDbwIndexInterClusterDensity = 0.0d;
    int numberOfClusters = MyClusterer.numberOfClusters();
    int numberOfTrainingInstances = MyClusterer.TrainingInstances
    .numInstances();
    double[] instanceValuesUij = new double[MyClusterer.TrainingInstances
    .numAttributes()];
    int densityUij = 0;
    
    long currentProgress = pCurrentProgress;    
    for (int i = 0; i < numberOfClusters; i++) {
      this.updateTaskProgress(TaskProgress.INDETERMINATE,
      "Assessment of Cluster Validity: " + (int)(100.0d
      * currentProgress / (double)pMaxProgress) + "% Completed"
      + " (Cluster " + i + "/" + numberOfClusters + ")");
      this.outputLogEntry(false,
      "Assessment of cluster validity: " + (int)(100.0d
      * currentProgress / (double)pMaxProgress) + "% completed"
      + " (cluster " + i + "/" + numberOfClusters + ")");
      for (int j = 0; j < numberOfClusters; j++) {
        if (i != j) {
          // compute u_ij that is the middle point of the line segment
          // defined by the prototype instances of clusters i and j
          for (int k = 0; k < instanceValuesUij.length; k++) {
            if (Double.isNaN(MyClusterer.getRepresentativeInstance(i)
            .value(k)) || Double.isNaN(MyClusterer.getRepresentativeInstance(j)
            .value(k))) {
              instanceValuesUij[k] = Double.NaN;
            }
            else {
              instanceValuesUij[k] = 0.5d
              * (MyClusterer.getRepresentativeInstance(i).value(k)
              + MyClusterer.getRepresentativeInstance(j).value(k));
            }
          }
          densityUij = 0;
          for (int k = 0; k < numberOfTrainingInstances; k++) {
            currentProgress++;
            if (!Utils.gr(MyClusterer.MyDistanceMeasure.computeDistance(
            MyClusterer.TrainingInstances.instance(k), 
            new Instance(1.0, instanceValuesUij),
            MyClusterer.TrainingInstancesMetadata),
            SDbwIndexAvgStdDevOfClusters)) {
              densityUij++;
            }
          }
          SDbwIndexInterClusterDensity += (double)densityUij / Math.max(
          SDbwIndexDensityOfPrototypes[i], SDbwIndexDensityOfPrototypes[j]);
        }
      }
    }

    SDbwIndexInterClusterDensity = SDbwIndexInterClusterDensity
    / (numberOfClusters * (numberOfClusters - 1));

    SDbwIndex = SDbwIndexAverageScattering + SDbwIndexInterClusterDensity;
    
    return currentProgress;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Computes the Overall Cluster Quality (0.5) index. The notation is 
   * according to He et al. (2004).
   */
  private void computeOverallClusterQualityIndex05() {
    
    int numberOfClusters = MyClusterer.numberOfClusters();
    int numberOfTrainingInstances = MyClusterer.TrainingInstances
    .numInstances();
    double[] avgTrainingInstanceValues = new double[MyClusterer
    .TrainingInstances.numAttributes()];
    for (int i = 0; i < avgTrainingInstanceValues.length; i++) {
      avgTrainingInstanceValues[i] = MyClusterer.TrainingInstancesMetadata
      .getMeanOfAttribute(i);
    }
    
    double distance = 0.0d;
    double ocqIndexVarianceOfTrainingInstances = 0.0d;
    Instance avgTrainingInstance = new Instance(1.0, avgTrainingInstanceValues);
    for (int i = 0; i < numberOfTrainingInstances; i++) {
      distance = MyClusterer.MyDistanceMeasure.computeDistance(
      MyClusterer.TrainingInstances.instance(i), avgTrainingInstance,
      MyClusterer.TrainingInstancesMetadata);
      ocqIndexVarianceOfTrainingInstances += (distance * distance);
    }
    if (numberOfTrainingInstances > 0) {
      ocqIndexVarianceOfTrainingInstances = Math.sqrt(
      ocqIndexVarianceOfTrainingInstances / numberOfTrainingInstances);
    }
    
    for (int i = 0; i < numberOfClusters; i++) {
      if (ClusterCardinality[i] > 0 && OcqIndexVarianceOfClusters[i] > 0.0d) {
        OcqIndexVarianceOfClusters[i] = Math.sqrt(OcqIndexVarianceOfClusters[i]
        / ClusterCardinality[i]);
      }
      else {
        OcqIndexVarianceOfClusters[i] = 0.0d;
      }
    }
    
    OcqIndexClusterCompactness = 0.0d;
    for (int i = 0; i < numberOfClusters; i++) {
      OcqIndexClusterCompactness += (OcqIndexVarianceOfClusters[i]
      / ocqIndexVarianceOfTrainingInstances);
    }
    
    OcqIndexClusterSeparation = 0.0d;
    for (int i = 0; i < numberOfClusters; i++) {
      for (int j = 0; j < numberOfClusters; j++) {
        if (i != j) {
          distance = MyClusterer.MyDistanceMeasure.computeDistance(
          MyClusterer.getRepresentativeInstance(i),
          MyClusterer.getRepresentativeInstance(j),
          MyClusterer.TrainingInstancesMetadata);
          distance = distance * distance;
          // The Gaussian constant can be set to 1.0 because the distances
          // are normalized between 0 and 1. The same approach was taken by
          // He et al.
          OcqIndexClusterSeparation += Math.exp(-1.0d * distance);
        }
      }
    }

    if (numberOfClusters > 1) {
      OcqIndexClusterCompactness = OcqIndexClusterCompactness 
      / numberOfClusters;
      OcqIndexClusterSeparation = OcqIndexClusterSeparation
      / (numberOfClusters * (numberOfClusters - 1));
      OverallClusterQualityIndex05 = (0.5d * OcqIndexClusterCompactness)
      + (0.5d * OcqIndexClusterSeparation);
    }
    else {
      OcqIndexClusterCompactness = Double.NaN;
      OcqIndexClusterSeparation = Double.NaN;
      OverallClusterQualityIndex05 = Double.NaN;
    }
    
    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Opens an existing or creates a new cluster validity indices ARFF file.
   * The text file ClusterValidityIndicesArffFile remains open.
   * @return the next ID
   */
  
  protected int createOrOpenClusterValidityIndicesFile() {
    
    if (Tools.stringIsNullOrEmpty(MyClusterer.HtmlReportFileName)) {
      return -1;
    }
    
    int nextID = 1;
    String fileName = Tools.removeFileExtension(MyClusterer.HtmlReportFileName);
    if (fileName.indexOf('_') >= 0) {
      fileName = fileName.substring(0, fileName.indexOf('_')) 
      + ".validity.arff";
    }
    else {
      fileName += ".validity.arff";
    }    
    File file = new File(fileName);
    if (file.exists()) {
      ClusterValidityIndicesArffFile = new TextFile(file);
      ClusterValidityIndicesArffFile.open();
      String line = ClusterValidityIndicesArffFile.getFirstLine();
      while (line != null) {
        if (line.toLowerCase().trim().equals("@data")) {
          nextID = 0;
        }
        nextID++;
        line = ClusterValidityIndicesArffFile.getNextLine();
      }
      ClusterValidityIndicesArffFile.close();
      ClusterValidityIndicesArffFile.open();
    }
    else {
      ClusterValidityIndicesArffFile = new TextFile(file);
      ClusterValidityIndicesArffFile.open();
      ClusterValidityIndicesArffFile.setFirstLine(
      "@relation 'hypKNOWsys Algorithms: Cluster Validity Indices'");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute ID integer");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute Clusterer string");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute Parameter string");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute NumberOfTrainingInstances integer");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute NumberOfClusters integer");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute NumberOfNonEmptyClusters integer");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute NumberOfEmptyClusters integer");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute DaviesBouldinIndex real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute AvgSilhouetteWidth real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute OriginalDunnIndex real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute BezdekDunnIndex real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute AvgInstanceBasedWithinClusterDistance real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute AvgPrototypeBasedWithinClusterDistance real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute AvgInstanceBasedBetweenClustersDistance real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute AvgPrototypeBasedBetweenClustersDistance real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute CombinationAvgInstanceBasedDistances real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute CombinationAvgPrototypeBasedDistances real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute OverallClusterQualityIndex05 real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute OcqIndexClusterCompactness real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute OcqIndexClusterSeparation real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute SDbwIndex real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute SDbwIndexAvgScatteringForClusters real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute SDbwIndexAvgInterClusterDensity real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@attribute SDbwIndexAvgStandardDeviationOfClusters real");
      ClusterValidityIndicesArffFile.setNextLine(
      "@data");
    }
    
    return nextID;
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * If ClusterValidityIndicesArffFile != null, the list of cluster validity
   * indices (i.e., pArffLine) is appended to the cluster validity indices file.
   * The ordering of indices must correspond to the attribute listed generated
   * by this. createOrOpenClusterValidityIndicesFile().
   * @param pArffLine the ARFF line to be appended to the file
   */
  
  protected void appendClusterValidityIndicesToFile(String pArffLine) {
    
    if (ClusterValidityIndicesArffFile != null) {
      ClusterValidityIndicesArffFile.setNextLine(pArffLine);
    }   
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * If ClusterValidityIndicesArffFile != null, the cluster validity indices 
   * ARFF file is closed.
   */
  
  protected void closeClusterValidityIndicesFile() {
    
    if (ClusterValidityIndicesArffFile != null) {
      ClusterValidityIndicesArffFile.close();
    }   
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
}