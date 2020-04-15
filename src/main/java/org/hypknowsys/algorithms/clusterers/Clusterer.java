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
import java.io.Serializable;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.hypknowsys.algorithms.core.AbstractOptionHandler;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.algorithms.core.InstancesMetadata;
import org.hypknowsys.algorithms.core.Option;
import org.hypknowsys.algorithms.core.OptionHandler;
import org.hypknowsys.algorithms.core.SerializedObject;
import org.hypknowsys.algorithms.core.Utils;
import org.hypknowsys.algorithms.proximity.CosineDistanceMeasure;
import org.hypknowsys.algorithms.proximity.DistanceMeasure;
import org.hypknowsys.algorithms.proximity.EuclideanDistanceMeasure;
import org.hypknowsys.algorithms.proximity.ExtendedDiceDistanceMeasure;
import org.hypknowsys.algorithms.proximity.ExtendedJaccardDistanceMeasure;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.Algorithm;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskProgress;

/**
 * Based on weka.clusterers.Clusterer, Revision 1.8:
 * Copyright (C) 1999 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * Abstract class for various clustering algorithm. Training and applications
 * instances should be pre-processed outside this algorithm instance, but using
 * the exactly the same process workflow for both training and application data.
 * According to the chosen distance measure, STRING and DATE attributes might be
 * ignored for computing the distance between two instances. Check the
 * documentation of the respective distance measure.
 *
 * Valid options of all clustering algorithms are:<p>
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
 * @version 0.1, 6 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public abstract class Clusterer implements Serializable, OptionHandler,
Algorithm {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Training instances: Is it really necessary to serialize the entire
   * training data set? Yes, at least for shared nearest neighbor clusterers!
   */
  protected Instances TrainingInstances = null;
  
  /**
   * Distance measure to be used for training the clusterer and applying
   * the clusterer to new instances
   */
  protected DistanceMeasure MyDistanceMeasure = new EuclideanDistanceMeasure();
  
  /**
   * Metadata object for the training data set
   */
  protected InstancesMetadata TrainingInstancesMetadata = null;
  
  /**
   * Variable holding cluster assignments of training data set
   */
  protected int[] ClusterAssignments = null;
  
  /**
   * Assessment of cluster validity is performed by this object; default
   * setting: null
   */
  protected ClusterValidityAssessor MyClusterValidityAssessor = null;
  
  /**
   * Verbose mode outputs basic information about the clustering progress
   */
  protected boolean VerboseMode = DEFAULT_VERBOSE_MODE;
  
  /**
   * File name of HTML report to be created or null, if report creation
   * should be skipped
   */
  protected String HtmlReportFileName = DEFAULT_HTML_REPORT_FILE_NAME;
  
  /**
   * Title of HTML report to be created.
   */
  protected String HtmlReportTitle = DEFAULT_HTML_REPORT_TITLE;
  
  /**
   * Should internal cluster quality be evaluated? Can assume one of the
   * following values: Clusterer.ASSESS_CLUSTER_VALIDITY:_TRUE,
   * Clusterer.ASSESS_CLUSTER_VALIDITY:_QUICK, or
   * Clusterer.ASSESS_CLUSTER_VALIDITY:_FALSE,
   */
  protected int EvaluateInternalQuality = DEFAULT_ASSESS_CLUSTER_VALIDITY;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * HTML report to be created or null, if report creation should be skipped
   */
  protected transient TextFile HtmlReport = null;
  
  /**
   * hypKNOWsys server that observes the progress of this algorithm
   */
  protected transient Server MyServer = null;
  
  /**
   * hypKNOWsys task progress instance for communicating the progress of
   * this algorithm
   */
  protected transient TaskProgress MyTaskProgress = null;
  
  /**
   * Thread that is associated with this algorithm
   */
  protected transient Thread MyTaskThread = null;
  
  /**
   * Temporary string buffer for performance purposes only
   */
  private transient StringBuffer TmpStringBuffer = null;
  
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
   * Default setting of attribute this.VerboseMode
   */
  protected static final boolean DEFAULT_VERBOSE_MODE = false;
  
  /**
   * Default setting of attribute this.HtmlReportFileName
   */
  protected static final String DEFAULT_HTML_REPORT_FILE_NAME = null;
  
  /**
   * Default setting of attribute this.EvaluateInternalQuality
   */
  protected static final int DEFAULT_ASSESS_CLUSTER_VALIDITY =
  ClusterValidityAssessor.ASSESS_CLUSTER_VALIDITY_FALSE;
  
  /**
   * Default setting of attribute this.HtmlReportTitle
   */
  private static final String DEFAULT_HTML_REPORT_TITLE = "Clustering Report";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the cluster quality assessor.
   *
   * @return the cluster quality assessor.
   */
  
  public ClusterValidityAssessor getClusterValidityAssessor() {
    
    return MyClusterValidityAssessor;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the setting of verbose mode.
   *
   * @return the setting of verbose mode.
   */
  
  public boolean verboseMode() {
    
    return VerboseMode;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the file name of the HTML report to be created or null, if
   * report creation should be skipped.
   *
   * @return the file name of the HTML report or null.
   */
  
  public String getHtmlReportFileName() {
    
    return HtmlReportFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the setting of the evaluation of internal cluster quality mode.
   *
   * @return the setting of the evaluation of internal cluster quality mode.
   */
  
  public int evaluateInternalQuality() {
    
    return EvaluateInternalQuality;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the distance measure to be employed: "euclidean" (default),
   * "cosine", "extendedJaccard", or "extendedDice".
   *
   * @param pDistanceMeasure the distance measure as an option argument
   */
  
  public void setDistanceMeasure(String pDistanceMeasure) {
    
    if (pDistanceMeasure != null) {
      if (DistanceMeasure.OPTION_COSINE_DISTANCE.equals(pDistanceMeasure)) {
        MyDistanceMeasure = new CosineDistanceMeasure();
      }
      else if (DistanceMeasure.OPTION_EXTENDED_JACCARD_DISTANCE
      .equals(pDistanceMeasure)) {
        MyDistanceMeasure = new ExtendedJaccardDistanceMeasure();
      }
      else if (DistanceMeasure.OPTION_EXTENDED_DICE_DISTANCE
      .equals(pDistanceMeasure)) {
        MyDistanceMeasure = new ExtendedDiceDistanceMeasure();
      }
      else {
        MyDistanceMeasure = new EuclideanDistanceMeasure();
      }
    }
    else {
      MyDistanceMeasure = new EuclideanDistanceMeasure();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the cluster validity assessor.
   *
   * @param pClusterValidityAssessor the cluster validity assessor to be used
   */
  
  public void setClusterValidityAssessor(
  ClusterValidityAssessor pClusterValidityAssessor) {
    
    if (pClusterValidityAssessor != null) {
      MyClusterValidityAssessor = pClusterValidityAssessor;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the verbose mode, default: false.
   *
   * @param pVerboseMode the verbose mode
   */
  
  public void setVerboseMode(boolean pVerboseMode) {
    
    VerboseMode = pVerboseMode;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the file name of the HTML report to be created or null, if
   * report creation should be skipped. Default: skip report creation.
   *
   * @param pHtmlReportFileName the file name of the HTML report to be
   * created or null
   */
  
  public void setHtmlReportFileName(String pHtmlReportFileName) {
    
    HtmlReportFileName = pHtmlReportFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the evaluation of internal cluster quality mode, default:
   * Clusterer.ASSESS_CLUSTER_VALIDITY_FALSE.
   *
   * @param pEvaluateInternalQuality the evaluation of internal cluster
   * quality mode
   */
  
  public void setEvaluateInternalQuality(int pEvaluateInternalQuality) {
    
    if (pEvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_FULL
    || pEvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_TRUE
    || pEvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_QUICK
    || pEvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_FALSE) {
      EvaluateInternalQuality = pEvaluateInternalQuality;
    }
    else {
      EvaluateInternalQuality = ClusterValidityAssessor
      .ASSESS_CLUSTER_VALIDITY_FALSE;
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
    
    TmpStringBuffer = new StringBuffer(1000);
    
    TmpStringBuffer.append("\n\n" + this.getClass().toString() + "\n\n");
    TmpStringBuffer.append("Distance measure: ");
    TmpStringBuffer.append(MyDistanceMeasure.getOptionArguments() + "\n");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Algorithm methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the hypKNOWsys server, which listens to the progress of this
   * algorithm, and provides am implementation of TaskProgress as well as
   * the associated thread.
   *
   * @param pServer the hypKNOWsys server of the associated task
   * @param pTaskProgress the current progress of the associated task
   * @param pMyTaskThread the current threat of the associated task
   */
  
  public void setTaskProgressListener(Server pServer,
  TaskProgress pTaskProgress, Thread pMyTaskThread) {
    
    MyServer = pServer;
    MyTaskProgress = pTaskProgress;
    MyTaskThread = pMyTaskThread;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface OptionHandler methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an enumeration describing the available options. <p>
   *
   * Valid options of all clustering algorithms are:<p>
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
   * @return an enumeration of all the available options.
   *
   **/
  
  public Enumeration listOptions() {
    
    Vector newVector = new Vector();
    
    newVector.addElement(new Option(
    "\tSpecify the distance measure to be employed, default: euclidean.",
    "distance", 1, 
    "-distance <euclidean|cosine|extendedJaccard|extendedDice>"));
    newVector.addElement(new Option(
    "\tTurn internal cluster validity assessment on or off, Default: false.",
    "assessIntValidity", 1, 
    "-assessIntValidity <full|true|quick|false>"));
    newVector.addElement(new Option(
    "\tTurn verbose mode on or off, default: false.",
    "verbose", 1, "-verbose <true|false>"));
    newVector.addElement(new Option(
    "\tSpecify the HTML report file name, default: skip report creation.",
    "htmlReport", 1, "-htmlReport <HTML report file name>"));
    
    return  newVector.elements();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Parses a given string of options.
   * @param pOptionString the list of options as an array of strings
   **/
  
  public void setOptionsFromString(String pOptionString) {
    
    if (pOptionString == null || pOptionString.length() == 0) {
      return;
    }
    
    StringTokenizer tokenizer = new StringTokenizer(pOptionString);
    String[] options = new String[tokenizer.countTokens()];
    int option = 0;
    while (tokenizer.hasMoreTokens()) {
      options[option++] = tokenizer.nextToken();
    }
    
    this.setOptions(options);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Parses a given list of options.
   * @param pOptions the list of options as an array of strings
   **/
  
  public void setOptions(String[] pOptions) {
    
    String optionString = AbstractOptionHandler.getOption("distance", pOptions);
    if (optionString.length() != 0) {
      this.setDistanceMeasure(optionString);
    }
    
    optionString = AbstractOptionHandler.getOption("assessIntValidity",
    pOptions);
    if (optionString.length() != 0) {
      if (optionString.toLowerCase().equals("full")) {
        this.setEvaluateInternalQuality(ClusterValidityAssessor
        .ASSESS_CLUSTER_VALIDITY_FULL);
      }
      else if (optionString.toLowerCase().equals("true")) {
        this.setEvaluateInternalQuality(ClusterValidityAssessor
        .ASSESS_CLUSTER_VALIDITY_TRUE);
      }
      else if (optionString.toLowerCase().equals("quick")) {
        this.setEvaluateInternalQuality(ClusterValidityAssessor
        .ASSESS_CLUSTER_VALIDITY_QUICK);
      }
      else if (optionString.toLowerCase().equals("false")) {
        this.setEvaluateInternalQuality(ClusterValidityAssessor
        .ASSESS_CLUSTER_VALIDITY_FALSE);
      }
      else {
        this.setEvaluateInternalQuality(DEFAULT_ASSESS_CLUSTER_VALIDITY);
      }
    }
    
    optionString = AbstractOptionHandler.getOption("verbose", pOptions);
    if (optionString.length() != 0) {
      this.setVerboseMode(Tools.string2Boolean(optionString));
    }
    
    optionString = AbstractOptionHandler.getOption("htmlReport", pOptions);
    if (optionString.length() != 0) {
      if (Tools.isValidandWriteableFileName(optionString)) {
        this.setHtmlReportFileName(optionString);
      }
      else {
        this.setHtmlReportFileName(null);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the current settings as a string.
   *
   * @return a string suitable for passing to setOptionsFromString()
   **/
  
  public String getOptionsAsString() {
    
    String[] options = this.getOptions();
    
    TmpStringBuffer = new StringBuffer(10000);
    for (int i = 0; i < options.length; i++) {
      if (i > 0) {
        TmpStringBuffer.append(" ");
      }
      TmpStringBuffer.append(options[i]);
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the current settings as an array of strings.
   *
   * @return an array of strings suitable for passing to setOptions()
   */
  
  public String[] getOptions() {
    
    int numberOfOptions = 4;
    if (VerboseMode) {
      numberOfOptions += 2;
    }
    if (HtmlReportFileName != null) {
      numberOfOptions += 2;
    }
    String[] options = new String[numberOfOptions];
    int current = 0;
    
    options[current++] = "-distance";
    options[current++] = MyDistanceMeasure.getOptionArguments();
    options[current++] = "-assessIntValidity";
    if (EvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_TRUE) {
      options[current++] = "true";
    }
    else if (EvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_QUICK) {
      options[current++] = "quick";
    }
    else {
      options[current++] = "false";
    }
    if (VerboseMode) {
      options[current++] = "-verbose";
      options[current++] = Tools.boolean2String(VerboseMode);
    }
    if (HtmlReportFileName != null) {
      options[current++] = "-htmlReport";
      options[current++] = HtmlReportFileName;
    }
    
    while (current < options.length) {
      options[current++] = "";
    }
    
    return options;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Generates a clusterer and initializes all attributes of the clusterer
   * that are not being set via options.
   *
   * @param pTrainingInstances data set of instances serving as training data
   */
  
  public void buildClusterer(Instances pTrainingInstances) {
    
    if (pTrainingInstances.checkForStringAttributes()) {
      System.err.println("Warning: Can't handle string attributes! "
      + "All string attributes will be ignored.");
    }
    
    if (HtmlReportFileName != null) {
      this.createHtmlReportHeader();
      this.createHtmlReportParameterSection();
      this.createHtmlReportLogSection();
      this.appendHtmlReportLogEntry("Start of clustering");
    }
    
    TrainingInstances = pTrainingInstances;
    TrainingInstancesMetadata = new InstancesMetadata(TrainingInstances);
    
    this.executeClusteringAlgorithm();
    
    if (EvaluateInternalQuality != ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_FALSE) {
      this.appendHtmlReportLogEntry(
      "Start of cluster quality assessment");
      this.assessClusterValidity();
      this.appendHtmlReportLogEntry(
      "End of cluster quality assessment");
      if (HtmlReportFileName != null) {
        this.appendHtmlReportLogEntry("End of clustering");
        this.createHtmlReportClusterValiditySection();
        this.createHtmlReportVisualizationSection();
      }
    }
    else if (HtmlReportFileName != null) {
      this.appendHtmlReportLogEntry("End of clustering");
    }
    
    if (HtmlReportFileName != null) {
      this.createHtmlReportFooter();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Classifies a given instance.
   *
   * @param pInstance the instance to be assigned to a cluster
   * @return the number of the assigned cluster as an interger
   * if the class is enumerated, otherwise the predicted valueabst
   */
  
  public abstract int clusterInstance(Instance pInstance);
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of clusters.
   *
   * @return the number of clusters generated for a training dataset.
   */
  
  public abstract int numberOfClusters();
  
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
    
    String evaluateInternalQualityString = null;
    if (EvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_FULL) {
      evaluateInternalQualityString = "full";
    }
    else if (EvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_TRUE) {
      evaluateInternalQualityString = "true";
    }
    else if (EvaluateInternalQuality == ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_QUICK) {
      evaluateInternalQualityString = "quick";
    }
    else {
      evaluateInternalQualityString = "false";
    }
    
    HtmlReport.setNextLine(
    "<a name=\"PARAMETERS\"><h3>Parameter Settings</h3></a>");
    HtmlReport.setNextLine(
    "<p><small><a href=\"#LOG\">Execution Log</a> - " + (EvaluateInternalQuality
    != ClusterValidityAssessor.ASSESS_CLUSTER_VALIDITY_FALSE
    ? "<a href=\"#CLUSTER_VALIDITY\">Cluster Validity Assessment</a> - " : "")
    + "<a href=\"#BOP\">Bottom of the Page</a></small></p>");
    HtmlReport.setNextLine("<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Parameter</th>"
    + "<th align=\"left\" valign=\"top\">Value</th></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Distance Measure</td>"
    + "<td align=\"left\" valign=\"top\">" + MyDistanceMeasure
    .getOptionArguments() + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Internal Cluster Validity Assessment"
    + "</td><td align=\"left\" valign=\"top\">" + evaluateInternalQualityString
    + "</td></tr>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the title of execution log to existing and open HTML report.
   * The text file remains open.
   */
  
  protected void createHtmlReportLogSection() {
    
    HtmlReport.setNextLine("<a name=\"LOG\"><h3>Execution Log</h3></a>");
    HtmlReport.setNextLine("<p><small><a href=\"#TOP\">Top of the Page</a> - "
    + "<a href=\"#PARAMETERS\">Parameter Settings</a> - "
    + (EvaluateInternalQuality != ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_FALSE
    ? "<a href=\"#CLUSTER_VALIDITY\">Cluster Validity Assessment</a> - " : "")
    + "<a href=\"#BOP\">Bottom of the Page</a></small></p>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends a visualization section to existing and open HTML report if
   * subclass overrides this empty method. The text file remains open.
   */
  
  protected void createHtmlReportVisualizationSection() {
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the execution log entry to existing and open HTML report.
   * The text file remains open.
   * @param pLogEntry the log entry to appended to the HTML report file
   */
  
  protected void appendHtmlReportLogEntry(String pLogEntry) {
    
    HtmlReport.setNextLine("<p>" + Tools.getSystemDate() + " - "
    + pLogEntry + "</p>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing cluster validity indices and details about
   * all cluster centroids to existing and open HTML report. The text file
   * remains open.
   */
  
  protected abstract void createHtmlReportClusterValiditySection();
  
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
  
  protected abstract void assessClusterValidity();
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the name of the representative instance returned by a specific
   * clustering algorithm. For example, "Centroid" is returned by the k-means
   * algorithm.
   * @return the name of the representative instance
   */
  
  protected abstract String getNameOfRepresentativeInstance();
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns one instance (e.g., centroid is returned by the k-means algorithm)
   * that represents the specified cluster. Prerequisites: The specific
   * clustering algorithm must have been executed.
   * @param pClusterID the ID of the cluster whose representative instance
   * shall be returned
   * @return the representative instance or null if pClusterID is not valid
   */
  
  protected abstract Instance getRepresentativeInstance(
  int pClusterID);
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Notifies MyServer about the specified progress of this algorithm,
   * if MyServer != null and MyTaskProgress != null and MyTaskThread != null.
   * The progress value pValue is either in the interval [0; 100] or equals
   * org.hypknowsys.server.TaskProgress.INDETERMINATE.
   *
   * @param pValue the current progress of this algorithm
   * @param pNote the note describing the current state of this algorithm
   */
  
  protected void updateTaskProgress(int pValue, String pNote) {
    
    if (MyServer != null && MyTaskProgress != null && MyTaskThread != null) {
      MyTaskProgress.update(pValue, pNote);
      MyServer.setTaskProgress(MyTaskProgress, MyTaskThread);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Outputs a log entry to System.out and/or the HTML log file according to
   * the settings of this.VerboseMode and this.HtmlReportFileName.
   *
   * @param pIsHtmlOutput if true, the log entry will be output to the HTML
   * report as well
   * @param pLogEntry the log entry to be output
   */
  
  protected void outputLogEntry(boolean pIsHtmlOutput, String pLogEntry) {
    
    if (VerboseMode) {
      System.out.println(pLogEntry);
    }
    if (pIsHtmlOutput && HtmlReportFileName != null) {
      this.appendHtmlReportLogEntry(pLogEntry);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Notifies MyServer about the specified progress of this algorithm,
   * if MyServer != null and MyTaskProgress != null and MyTaskThread != null.
   * The progress value pValue is either in the interval [0; 100] or equals
   * org.hypknowsys.server.TaskProgress.INDETERMINATE.
   *
   * @param pValue the current progress of this algorithm
   * @param pNewMaxValue the new maximum progress value of this task
   * @param pNote the note describing the current state of this algorithm
   */
  
  protected void updateTaskProgress(int pValue, int pNewMaxValue,
  String pNote) {
    
    if (MyServer != null && MyTaskProgress != null && MyTaskThread != null) {
      MyTaskProgress.update(pValue, pNewMaxValue, pNote);
      MyServer.setTaskProgress(MyTaskProgress, MyTaskThread);
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
   * Checks, whether pInstance only contains missing and zero values.
   *
   * @param pInstance the instance to be checked
   * @return true, if pIstance only contains missing and zero values,
   * and false otherwise
   */
  
  protected final boolean isEmptyInstance(Instance pInstance) {
    
    TmpBooleanValue = true;
    TmpDoubleValue = Double.NaN;
    for (int i = 0; i < pInstance.numValues(); i++) {
      TmpDoubleValue = pInstance.valueSparse(i);
      if (!Instance.isMissingValue(TmpDoubleValue)
      && !Utils.eq(0.0d, TmpDoubleValue)) {
        TmpBooleanValue = false;
        break;
      }
    }
    
    return TmpBooleanValue;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Creates a new instance of a clusterer given it's class name and
   * (optional) arguments to pass to it's setOptions method. If the
   * clusterer implements OptionHandler and the options parameter is
   * non-null, the clusterer will have it's options set.
   *
   * @param pClustererName the fully qualified class name of the clusterer
   * @param pOptions an array of options suitable for passing to setOptions. May
   * be null.
   * @return the newly created search object, ready for use.
   * @exception Exception if the clusterer class name is invalid, or the
   * options supplied are not acceptable to the clusterer.
   */
  
  public static Clusterer forName(String pClustererName,
  String [] pOptions) throws Exception {
    
    return (Clusterer)Utils.forName(Clusterer.class, pClustererName, pOptions);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Creates copies of the current clusterer. Note that this method
   * now uses Serialization to perform a deep copy, so the Clusterer
   * object must be fully Serializable. Any currently built model will
   * now be copied as well.
   *
   * @param pModel an example clusterer to copy
   * @param pNumberOfClusterers the number of clusterer copies to create.
   * @return an array of clusterers.
   * @exception Exception if an error occurs
   */
  
  public static Clusterer [] makeCopies(Clusterer pModel,
  int pNumberOfClusterers) throws Exception {
    
    if (pModel == null) {
      throw new Exception("No model clusterer set");
    }
    Clusterer [] clusterers = new Clusterer [pNumberOfClusterers];
    SerializedObject so = new SerializedObject(pModel);
    for (int i = 0; i < clusterers.length; i++) {
      clusterers[i] = (Clusterer) so.getObject();
    }
    return clusterers;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
}