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

import java.io.*;
import java.util.*;
import java.awt.event.*;
import weka.core.*;
import weka.filters.*;
import weka.filters.unsupervised.attribute.*;
import weka.clusterers.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ClusterTextUnitVectorsWekaTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ClusterTextUnitVectorsWekaParameter CastParameter = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Cluster Text Unit Vectors (Weka)";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka.ClusterTextUnitVectorsWekaParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka.ClusterTextUnitVectorsWekaResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka.ClusterTextUnitVectorsWekaControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ClusterTextUnitVectorsWekaTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    
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
    
    ClusterTextUnitVectorsWekaParameter parameter = null;
    if (pParameter instanceof ClusterTextUnitVectorsWekaParameter) {
      parameter = (ClusterTextUnitVectorsWekaParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    File file = new File(parameter.getCollectionFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION +
      "-file in the field 'Collection File'!");
    }
    file = new File(parameter.getInputVectorsFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.ARFF_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.ARFF_FILE_EXTENSION +
      "-file in the field 'Text Unit Vectors File'!");
    }
    if (parameter.getOutputVectorsFileName().trim().length() <= 0
    || !parameter.getOutputVectorsFileName().trim().endsWith(
    GuiClientPreferences.CSV_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " + 
      GuiClientPreferences.CSV_FILE_EXTENSION +
      "-file name\nin the field 'Clustering Results File'!");
    }
    file = new File(parameter.getOutputVectorsFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'Clustering Results File' currently exists.\n" +
      "Do you really want to replace this file?");
    }

    if (parameter.getClusteringMode() == 
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE) {
      
      if (parameter.getClusterModelFileName().trim().length() <= 0
      || !parameter.getClusterModelFileName().trim().endsWith(
      this.getRequiredFileExtension(parameter))) {
        result.addError(
        "Error: Please enter a valid local " +
        this.getRequiredFileExtension(parameter) +
        "-file name\nin the field 'Text Unit Clusterer File'!");
      }
      file = new File(parameter.getClusterModelFileName());
      if (file.exists()) {
        result.addWarning(
        "Warning: The file specified in the field\n" +
        "'Text Unit Clusterer File' currently exists.\n" +
        "Do you really want to replace this file?");
      }
      
    }
    else {
      
      file = new File(parameter.getClusterModelFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(this.getRequiredFileExtension(parameter))) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        this.getRequiredFileExtension(parameter) +
        "-file in the field 'Text Unit Clusterer File'!");
      }
      
    }
    
    if (!Tools.isInt(parameter.getNumberOfClusters().trim())
    && parameter.getClusteringMode() ==
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE
    && parameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_SIMPLE_KMEANS) {
      result.addError(
      "Error: Please enter a valid integer\n" +
      "in the field 'Number of Clusters'!");
    }
    if (!Tools.isDouble(parameter.getAcuity().trim())
    && parameter.getClusteringMode() ==
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE
    && parameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_COBWEB) {
      result.addError(
      "Error: Please enter a valid\n" +
      "double in the field 'Acuity'!");
    }
    if (!Tools.isDouble(parameter.getCutoff().trim())
    && parameter.getClusteringMode() ==
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE
    && parameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_COBWEB) {
      result.addError(
      "Error: Please enter a valid\n" +
      "double in the field 'Cutoff'!");
    }
    if (!Tools.isInt(parameter.getMaxIterations().trim())
    && parameter.getClusteringMode() ==
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE
    && parameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_EM) {
      result.addError(
      "Error: Please enter a valid integer\n" +
      "in the field 'Max. Iterations'!");
    }
    if (!Tools.isInt(parameter.getRandomNumberSeed().trim())
    && parameter.getClusteringMode() ==
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE
    && parameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_EM) {
      result.addError(
      "Error: Please enter a valid integer\n" +
      "in the field 'Random Number Seed'!");
    }
    if (!Tools.isDouble(parameter.getMinStdDeviation().trim())
    && parameter.getClusteringMode() ==
    ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE
    && parameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_EM) {
      result.addError(
      "Error: Please enter a valid double\n" +
      "in the field 'Min. Std. Deviation'!");
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ClusterTextUnitVectorsWekaParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ClusterTextUnitVectorsWekaResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_DISCOVER_PATTERNS,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof ClusterTextUnitVectorsWekaParameter) {
      CastParameter = (ClusterTextUnitVectorsWekaParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    this.acceptTask(TaskProgress.INDETERMINATE, 
    "Phase 1: Read Text Unit Vectors");
    
    int counterProgress = 0;
    long maxProgress = 4;  // attribute removal, learning, application

    counterProgress++;
    Progress.update(TaskProgress.INDETERMINATE, 
    "Phase 1: Read Text Unit Vectors"); 
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    FileReader inputFileReader = null;
    Instances inputVectors = null;
    Instances filteredVectors = null;
    RemoveType removeStringFilter = null;
    Clusterer clusterer = null;
    String[] clustererOptions = null;
    ObjectInputStream modelInputStream = null;
    ObjectOutputStream modelOutputStream = null;
    int maxClusterID = 0;
    
    try {
      inputFileReader = new FileReader(CastParameter.getInputVectorsFileName());
      inputVectors = new Instances(inputFileReader);
 
      counterProgress++;
      Progress.update(TaskProgress.INDETERMINATE, 
        "Phase 2: Remove String Attributes"); 
      DiasdemServer.setTaskProgress(Progress, TaskThread);

      removeStringFilter = new RemoveType();
      String[] filterOptions = new String[2]; 
      filterOptions[0] = "-T";
      filterOptions[1] = "string";
      removeStringFilter.setOptions(filterOptions);
      removeStringFilter.inputFormat(inputVectors);
      filteredVectors = Filter.useFilter(inputVectors, removeStringFilter);
      //System.out.println("Test - " + Parameter.getInputVectorsFileName());
      //System.out.println(filteredVectors.toString()); System.out.flush();
      inputVectors = null;
      
      counterProgress++;
      Progress.update( TaskProgress.INDETERMINATE, 
        "Phase 3: Create or Read Clusterer"); 
      DiasdemServer.setTaskProgress(Progress, TaskThread);

      if (CastParameter.getClusteringMode() == ClusterTextUnitVectorsWekaParameter
      .CLUSTERING_PHASE) {
        switch (CastParameter.getClusteringAlgorithm()) {
          case ClusterTextUnitVectorsWekaParameter.WEKA_SIMPLE_KMEANS: {
            clusterer = new SimpleKMeans();
            clustererOptions = new String[2]; 
            clustererOptions[0] = "-N";
            clustererOptions[1] = CastParameter.getNumberOfClusters();
            ( (SimpleKMeans)clusterer ).setOptions(clustererOptions);
            break; 
          }
          case ClusterTextUnitVectorsWekaParameter.WEKA_COBWEB: {
            clusterer = new Cobweb();
            clustererOptions = new String[4]; 
            clustererOptions[0] = "-A";
            clustererOptions[1] = CastParameter.getAcuity();
            clustererOptions[2] = "-C";
            clustererOptions[3] = CastParameter.getCutoff();
            ( (Cobweb)clusterer ).setOptions(clustererOptions);
            break; 
          }
          case ClusterTextUnitVectorsWekaParameter.WEKA_EM: {
            clusterer = new EM();
            if (CastParameter.getNumberOfClusters().length() == 0)
              clustererOptions = new String[6];
            else {
              clustererOptions = new String[8];
              clustererOptions[6] = "-N";
              clustererOptions[7] = CastParameter.getNumberOfClusters();
            }              
            clustererOptions[0] = "-I";
            clustererOptions[1] = CastParameter.getMaxIterations();
            clustererOptions[2] = "-S";
            clustererOptions[3] = CastParameter.getRandomNumberSeed();
            clustererOptions[4] = "-M";
            clustererOptions[5] = CastParameter.getMinStdDeviation(); 
            ( (EM)clusterer ).setOptions(clustererOptions);
            break; 
          }
        }            
        try {
          clusterer.buildClusterer(filteredVectors);
        }
        catch (Exception e) {
          Progress.update(100, "Stop Clustering due to Error ..."); 
          DiasdemServer.setTaskProgress(Progress, TaskThread);
          DiasdemServer.setTaskResult( new AbstractTaskResult(
            TaskResult.NO_RESULT, Tools.wrapString("Error: " 
            + e.getMessage(), 60 ) ) );
          DiasdemServer.setTaskStatus(Task.TASK_FINISHED);
          e.printStackTrace(System.err);
          return;       
        }
        modelOutputStream = new ObjectOutputStream( new FileOutputStream(
        CastParameter.getClusterModelFileName() ));
        modelOutputStream.writeObject(clusterer);
        modelOutputStream.flush(); 
        modelOutputStream.close();
      }
      else {
        modelInputStream = new ObjectInputStream( new FileInputStream(
        CastParameter.getClusterModelFileName()));
        switch (CastParameter.getClusteringAlgorithm()) {
          case ClusterTextUnitVectorsWekaParameter.WEKA_SIMPLE_KMEANS: {
             clusterer = (SimpleKMeans)modelInputStream.readObject();
             break; 
          }
          case ClusterTextUnitVectorsWekaParameter.WEKA_COBWEB: {
             clusterer = (Cobweb)modelInputStream.readObject();
            break; 
          }
          case ClusterTextUnitVectorsWekaParameter.WEKA_EM: {
             clusterer = (EM)modelInputStream.readObject();
            break; 
          }
        }        
       modelInputStream.close();
      }
      
      counterProgress++;
      Progress.update( TaskProgress.INDETERMINATE, 
        "Phase 4: Create Result File"); 
      DiasdemServer.setTaskProgress(Progress, TaskThread);

      TextFile outputFile = new TextFile( 
      new File(CastParameter.getOutputVectorsFileName() + ".temp") );
      outputFile.empty();
      outputFile.open();
      for (int i = 0; i < filteredVectors.numInstances(); i++)
        outputFile.setNextLine(i + " " + clusterer.clusterInstance(
        filteredVectors.instance(i)));
      outputFile.close();
      
      maxClusterID = WekaOutput2Csv.convertWekaOutput2Csv(
      CastParameter.getInputVectorsFileName(),
      CastParameter.getOutputVectorsFileName()
      + ".temp", CastParameter.getOutputVectorsFileName());
      DiasdemProject.setIntProperty("DEFAULT_MAX_CLUSTER_ID", maxClusterID);
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
 
    int numberOfClusters = 0;
    try {
      numberOfClusters = clusterer.numberOfClusters();
    }
    catch (Exception e) {}

    Result.update(TaskResult.FINAL_RESULT, 
      "All untagged text unit vectors of DIAsDEM collection\n"
      + Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) 
      + "\nhave been segmented into " + numberOfClusters 
      + " clusters. Result file:\n" 
      + Tools.shortenFileName(CastParameter.getOutputVectorsFileName(), 50)
      + (CastParameter.getClusteringMode() == 
      ClusterTextUnitVectorsWekaParameter.CLUSTERING_PHASE ? 
      "\nThe maximum cluster ID is " + maxClusterID + " in this iteration." : 
      ""));
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
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String getRequiredFileExtension(
  ClusterTextUnitVectorsWekaParameter pParameter) {
    
    if (pParameter != null && pParameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_SIMPLE_KMEANS) {
      return DIAsDEMguiPreferences.WEKA_SIMPLE_KMEANS_FILE_EXTENSION;
    }
    else if (pParameter != null && pParameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_COBWEB) {
      return DIAsDEMguiPreferences.WEKA_COBWEB_FILE_EXTENSION;
    }
    else if (pParameter != null && pParameter.getClusteringAlgorithm() ==
    ClusterTextUnitVectorsWekaParameter.WEKA_EM) {
      return DIAsDEMguiPreferences.WEKA_EM_FILE_EXTENSION;
    }
    
    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}