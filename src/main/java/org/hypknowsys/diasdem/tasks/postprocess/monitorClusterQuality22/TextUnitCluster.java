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

package org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality22;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurusTerm;
import org.hypknowsys.misc.io.Itemizer;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Karsten Winkler
 */

public class TextUnitCluster implements Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private int ID = 0;
  private int NumberOfDescriptorsInControlledVocabulary = 0;
  private int NumberOfDominantDescriptors = 0;
  private int NumberOfNonRareDescriptors = 0;
  private int AbsClusterSize = 0;
  private double RelClusterSize = 0;
  private double DescriptorCoverage = 0.0d;
  private double DescriptorDominance = 0.0d;
  private double QualityIndex = 0.0d;
  private TreeMap DominantDescriptors = null;
  private String DefaultClusterLabels = null;
  private boolean IsAscceptable = false;
  
  private double Red_MinRelClusterSize = 0.0d;
  private double Orange_MinRelClusterSize = 0.0d;
  private double Yellow_MinRelClusterSize = 0.0d;

  private DIAsDEMthesaurus ClusterDescriptorCounter = null;
  private DIAsDEMthesaurus ClusterNonDescriptorCounter = null;
  private double[] ClusterSummary = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TextUnitCluster(int pID, 
  int pNumberOfDescriptorsInControlledVocabulary) {
    
    this.ID = pID;
    this.NumberOfDescriptorsInControlledVocabulary 
    = pNumberOfDescriptorsInControlledVocabulary;
    this.NumberOfDominantDescriptors = 0;
    this.AbsClusterSize = 0;
    this.RelClusterSize = 0.0d;
    this.DescriptorCoverage = 0.0d;
    this.DescriptorDominance = 0.0d;
    this.QualityIndex = 0.0d;
    
    this.ClusterDescriptorCounter = new DefaultDIAsDEMthesaurus();
    this.ClusterNonDescriptorCounter = new DefaultDIAsDEMthesaurus();
    this.ClusterSummary = new double[
    this.NumberOfDescriptorsInControlledVocabulary];
    for (int i = 0; i < this.ClusterSummary.length; i++) {
      this.ClusterSummary[i] = 0.0d;
    }
    
    DominantDescriptors = new TreeMap();
    DefaultClusterLabels = "-";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getID() {
    return ID; }
  public int getAbsClusterSize() {
    return AbsClusterSize; }
  public double getRelClusterSize() {
    return RelClusterSize; }
  public int getNumberOfDescriptors() {
    return NumberOfDescriptorsInControlledVocabulary; }
  public int getNumberOfDominantDescriptors() {
    return NumberOfDominantDescriptors; }
  public int getNumberOfNonRareDescriptors() {
    return NumberOfNonRareDescriptors; }
  public double getDescriptorCoverage() {
    return DescriptorCoverage; }
  public double getDescriptorDominance() {
    return DescriptorDominance; }
  public double getQualityIndex() {
    return QualityIndex; }
  public java.lang.String getDefaultClusterLabels() {
    return DefaultClusterLabels; }
  public boolean isAcceptable() {
    return IsAscceptable; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setAbsClusterSize(int pAbsClusterSize) {
    this.AbsClusterSize = pAbsClusterSize; }
  public void setRelClusterSize(double pRelClusterSize) {
    this.RelClusterSize = pRelClusterSize; }
  public void setNumberOfDescriptors(int pNumberOfDescriptors) {
    this.NumberOfDescriptorsInControlledVocabulary 
    = NumberOfDescriptorsInControlledVocabulary; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("TextUnitCluster, ID=");
    TmpStringBuffer.append(ID);
    
    return TmpStringBuffer.toString();
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toItemLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(Itemizer.intToItem(ID));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) {
    
    ID = 0;
    Itemizer itemizer = new Itemizer(pItemLine);
    
    try {
      ID = itemizer.itemToInt(itemizer.getNextItem());
    }
    catch (NoSuchElementException e1) { this.reset(); }
    catch (NumberFormatException e2) { this.reset(); }
    catch (StringIndexOutOfBoundsException e2) { this.reset(); }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void incrementClusterCardinality() {
    
    this.AbsClusterSize++;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void countDescriptorOccurrence(String pDescriptor) {
    
    this.ClusterDescriptorCounter.countOccurrence(pDescriptor);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void countNonDescriptorOccurrence(String pNonDescriptor) {
    
    this.ClusterNonDescriptorCounter.countOccurrence(pNonDescriptor);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void computeDescriptorFrequencies(TextFile pClusterFile,
  TextFile pClusterSummaryFile, int pNumberOfClusters, int pNumberOfTextUnits,
  Hashtable pMappingHashtable, double pDominantDescriptorThreshold,
  double pRareDescriptorThreshold, double pFrequentNonDescriptorThreshold,
  DIAsDEMthesaurus pDescriptorIterationFrequencies,
  DIAsDEMthesaurus pNonDescriptorIterationFrequencies,
  int pNumberOfTextUnitsInClustering) {
    
    Red_MinRelClusterSize = 1.0d - (1.0d / 3.0d 
    * (1.0d - pDominantDescriptorThreshold));
    Orange_MinRelClusterSize = pDominantDescriptorThreshold;
    Yellow_MinRelClusterSize = pDominantDescriptorThreshold 
    - (1.0d / 3.0d * (1.0d - pDominantDescriptorThreshold));
      
    NumberFormat nf = NumberFormat.getInstance(Locale.US);
    nf.setMinimumFractionDigits(6);
    nf.setMaximumFractionDigits(6);
    NumberFormat nf2 = NumberFormat.getInstance(Locale.US);
    nf2.setMinimumFractionDigits(2);
    nf2.setMaximumFractionDigits(2);
    
    pClusterFile.open();
    ClusterDescriptorCounter.setOrderOccurrencesWordsDesc();
    DIAsDEMthesaurusTerm currentTerm = ClusterDescriptorCounter
    .getFirstTerm();
    pClusterFile.setNextLine("<a name=\"DESC_BY_SUPP\">");
    pClusterFile.setNextLine("<h3>Descriptors by Support</h3>");
    pClusterFile.setNextLine("<p><small>"
    + "<a href=\"#CONTENTS\">Cluster Contents</a> - "
    + "<a href=\"#DESC_BY_TERM\">Descriptors by Term</a> - "
    + "<a href=\"#NONDESC_BY_SUPP\">Non-Descriptors by Support</a> - "
    + "<a href=\"#QUALITY\">Cluster Quality Assessment</a>"
    + "</small></p>");
    String topLeft = "valign=\"top\" align=\"left\" ";
    String topCenter = "valign=\"top\" align=\"center\" ";
    String topRight = "valign=\"top\" align=\"right\" ";
    TmpString = "<table border=\"1\" align=\"center\" width=\"100%\"><tr>"
    + "<td " + topLeft + "width=\"28%\" rowspan=\"2\"><b>&nbsp;</b></td>"
    + "<td " + topCenter + "colspan=\"3\"><b>Relative Support</b></td>"
    + "<td " + topCenter + "colspan=\"3\"><b>Absolute Support</b></td></tr>";
    TmpString += "<tr>"
    + "<td " + topRight + "width=\"12%\"><b>Cluster</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Clustering</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Ratio</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Cluster</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Clustering</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Ratio</b></td></tr>";
    pClusterFile.setNextLine(TmpString);
    pClusterSummaryFile.setNextLine("<h3>Cluster " + this.ID + "</h3>"
    + "<p>Cluster Size = " + this.AbsClusterSize + " ("
    + nf2.format(this.AbsClusterSize / (double)pNumberOfTextUnits * 100)
    + "%)</p>");
    pClusterSummaryFile.setNextLine("<p><b>Descriptors by Support</b></p>");
    pClusterSummaryFile.setNextLine(TmpString);
    this.NumberOfDominantDescriptors = 0;
    DominantDescriptors = new TreeMap();
    while (currentTerm != null) {
      int absSupportCluster = currentTerm.getOccurrences();
      double relSupportCluster = absSupportCluster
      / (double)this.AbsClusterSize;
      int absSupportIteration = pDescriptorIterationFrequencies.get(
      currentTerm.getWord()).getOccurrences();
      double relSupportIteration = absSupportIteration
      / (double)pNumberOfTextUnits;
      
      ClusterSummary[((Integer)pMappingHashtable.get(
      currentTerm.getWord())).intValue()] = relSupportCluster;
      if (relSupportCluster >= pDominantDescriptorThreshold) {
        this.NumberOfDominantDescriptors++;
        DominantDescriptors.put(new Integer(this.NumberOfDominantDescriptors),
        currentTerm.getWord());
      }
      String bgcolor = "";
      String color = "black";
      if (relSupportCluster >= Red_MinRelClusterSize) {
        bgcolor = "bgcolor=\"red\"";
      }
      else if (relSupportCluster >= Orange_MinRelClusterSize) {
        bgcolor = "bgcolor=\"orange\"";
      }
      else if (relSupportCluster >= Yellow_MinRelClusterSize) {
        bgcolor = "bgcolor=\"yellow\"";
      }
      else if (relSupportCluster <= pRareDescriptorThreshold) {
        color = "silver";
      }
      
      TmpString =
      "<td valign=\"top\" align=\"left\" " + bgcolor + "><font color=\""
      + color + "\">"
      + Tools.insertISO88591EntityReferences(currentTerm.getWord())
      + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportCluster) + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportIteration) + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportCluster / relSupportIteration)
      + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + absSupportCluster + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + absSupportIteration + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(absSupportCluster / (double)absSupportIteration)
      + " </font> </td>"
      + "</tr>";
      
      pClusterFile.setNextLine(TmpString);
      pClusterSummaryFile.setNextLine(TmpString);
      currentTerm = ClusterDescriptorCounter.getNextTerm();
    }
    pClusterFile.setNextLine("</table>");
    pClusterSummaryFile.setNextLine("</table>");
    
    ClusterDescriptorCounter.setOrderWordsAsc();
    currentTerm = ClusterDescriptorCounter.getFirstTerm();
    pClusterFile.setNextLine("<a name=\"DESC_BY_TERM\">");
    pClusterFile.setNextLine("<h3>Descriptors by Term</h3>");
    pClusterFile.setNextLine("<p><small>"
    + "<a href=\"#CONTENTS\">Cluster Contents</a> - "
    + "<a href=\"#DESC_BY_SUPP\">Descriptors by Support</a> - "
    + "<a href=\"#NONDESC_BY_SUPP\">Non-Descriptors by Support</a> - "
    + "<a href=\"#QUALITY\">Cluster Quality Assessment</a>"
    + "</small></p>");
    TmpString = "<table border=\"1\" align=\"center\" width=\"100%\"><tr>"
    + "<td " + topLeft + "width=\"28%\" rowspan=\"2\"><b>&nbsp;</b></td>"
    + "<td " + topCenter + "colspan=\"3\"><b>Relative Support</b></td>"
    + "<td " + topCenter + "colspan=\"3\"><b>Absolute Support</b></td></tr>";
    TmpString += "<tr>"
    + "<td " + topRight + "width=\"12%\"><b>Cluster</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Clustering</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Ratio</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Cluster</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Clustering</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Ratio</b></td></tr>";
    pClusterFile.setNextLine(TmpString);
    while (currentTerm != null) {
      int absSupportCluster = currentTerm.getOccurrences();
      double relSupportCluster = absSupportCluster
      / (double)this.AbsClusterSize;
      int absSupportIteration = pDescriptorIterationFrequencies.get(
      currentTerm.getWord()).getOccurrences();
      double relSupportIteration = absSupportIteration
      / (double)pNumberOfTextUnits;
      
      ClusterSummary[((Integer)pMappingHashtable.get(
      currentTerm.getWord())).intValue()] = relSupportCluster;
      String bgcolor = "";
      String color = "black";
      if (relSupportCluster >= Red_MinRelClusterSize) {
        bgcolor = "bgcolor=\"red\"";
      }
      else if (relSupportCluster >= Orange_MinRelClusterSize) {
        bgcolor = "bgcolor=\"orange\"";
      }
      else if (relSupportCluster >= Yellow_MinRelClusterSize) {
        bgcolor = "bgcolor=\"yellow\"";
      }
      else if (relSupportCluster <= pRareDescriptorThreshold) {
        color = "silver";
      }
      TmpString =
      "<td valign=\"top\" align=\"left\" " + bgcolor + "><font color=\""
      + color + "\">"
      + Tools.insertISO88591EntityReferences(currentTerm.getWord())
      + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportCluster) + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportIteration) + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportCluster / relSupportIteration)
      + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + absSupportCluster + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + absSupportIteration + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(absSupportCluster / (double)absSupportIteration)
      + " </font> </td>"
      + "</tr>";
      
      pClusterFile.setNextLine(TmpString);
      currentTerm = ClusterDescriptorCounter.getNextTerm();
    }
    pClusterFile.setNextLine("</table>");
    pClusterSummaryFile.setNextLine("</table>");
    
    ClusterNonDescriptorCounter.setOrderOccurrencesWordsDesc();
    currentTerm = ClusterNonDescriptorCounter.getFirstTerm();
    pClusterFile.setNextLine("<a name=\"NONDESC_BY_SUPP\">");
    pClusterFile.setNextLine("<h3>Non-Descriptors by Support</h3>");
    pClusterFile.setNextLine("<p><small>"
    + "<a href=\"#CONTENTS\">Cluster Contents</a> - "
    + "<a href=\"#DESC_BY_SUPP\">Descriptors by Support</a> - "
    + "<a href=\"#DESC_BY_TERM\">Descriptors by Term</a> - "
    + "<a href=\"#QUALITY\">Cluster Quality Assessment</a>"
    + "</small></p>");
    TmpString = "<table border=\"1\" align=\"center\" width=\"100%\"><tr>"
    + "<td " + topLeft + "width=\"28%\" rowspan=\"2\"><b>&nbsp;</b></td>"
    + "<td " + topCenter + "colspan=\"3\"><b>Relative Support</b></td>"
    + "<td " + topCenter + "colspan=\"3\"><b>Absolute Support</b></td></tr>";
    TmpString += "<tr>"
    + "<td " + topRight + "width=\"12%\"><b>Cluster</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Clustering</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Ratio</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Cluster</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Clustering</b></td>"
    + "<td " + topRight + "width=\"12%\"><b>Ratio</b></td></tr>";
    pClusterFile.setNextLine(TmpString);
    pClusterSummaryFile.setNextLine("<b>Non-Descriptors by Support</b>");
    pClusterSummaryFile.setNextLine(TmpString);
    while (currentTerm != null && (currentTerm.getOccurrences()
    / (double)this.AbsClusterSize) > pFrequentNonDescriptorThreshold) {
      int absSupportCluster = currentTerm.getOccurrences();
      double relSupportCluster = absSupportCluster
      / (double)this.AbsClusterSize;
      int absSupportIteration = pNonDescriptorIterationFrequencies.get(
      currentTerm.getWord()).getOccurrences();
      double relSupportIteration = absSupportIteration
      / (double)pNumberOfTextUnits;
      
      String bgcolor = "";
      String color = "black";
      if (relSupportCluster >= Red_MinRelClusterSize) {
        bgcolor = "bgcolor=\"red\"";
      }
      else if (relSupportCluster >= Orange_MinRelClusterSize) {
        bgcolor = "bgcolor=\"orange\"";
      }
      else if (relSupportCluster >= Yellow_MinRelClusterSize) {
        bgcolor = "bgcolor=\"yellow\"";
      }
      else if (relSupportCluster <= pRareDescriptorThreshold) {
        color = "silver";
      }
      TmpString =
      "<td valign=\"top\" align=\"left\" " + bgcolor + "><font color=\""
      + color + "\">"
      + Tools.insertISO88591EntityReferences(currentTerm.getWord())
      + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportCluster) + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportIteration) + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(relSupportCluster / relSupportIteration)
      + " </font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + absSupportCluster + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + absSupportIteration + "</font> </td>"
      + "<td valign=\"top\" align=\"right\" " + bgcolor + "><font color=\""
      + color + "\">"
      + nf.format(absSupportCluster / (double)absSupportIteration)
      + " </font> </td>"
      + "</tr>";
      
      pClusterFile.setNextLine(TmpString);
      pClusterSummaryFile.setNextLine(TmpString);
      currentTerm = ClusterNonDescriptorCounter.getNextTerm();
    }
    pClusterFile.setNextLine("</table>");
    pClusterSummaryFile.setNextLine("</table>");

    pClusterFile.close();
    
    DefaultClusterLabels = "";
    Iterator iterator = DominantDescriptors.keySet().iterator();
    boolean isFirstLabel = true;
    while (iterator.hasNext()) {
      if (isFirstLabel) {
        isFirstLabel = false;
      }
      else {
        DefaultClusterLabels += "_";
      }
      DefaultClusterLabels += (String)DominantDescriptors.get(
      (Integer)(iterator.next()));
    }
    DefaultClusterLabels = Tools.createAsciiAttributeName(
    DefaultClusterLabels, 200, "DEFAULT_", "");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String extentDescriptorSummaryLine(int pDescriptorID) {
    
    if (ClusterSummary[pDescriptorID] >= Red_MinRelClusterSize) {
      return "<td bgcolor=\"red\"> <a href=\"cluster" + this.ID
      + ".html" + "\" target=\"_new\">" + "#" + "</a>" + " </td>";
    }
    else if (ClusterSummary[pDescriptorID] >= Orange_MinRelClusterSize) {
      return "<td bgcolor=\"orange\"> <a href=\"cluster" + this.ID
      + ".html" + "\" target=\"_new\">" + "+" + "</a>" + " </td>";
    }
    else if (ClusterSummary[pDescriptorID] >= Yellow_MinRelClusterSize) {
      return "<td bgcolor=\"yellow\"> <a href=\"cluster" + this.ID
      + ".html" + "\" target=\"_new\">" + "=" + "</a>" + " </td>";
    }
    else {
      return "<td> <a href=\"cluster" + this.ID
      + ".html" + "\" target=\"_new\">" + "-" + "</a>" + " </td>";
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void computeQualityIndex(int pMinClusterSize, 
  double pMaxDescriptorCoverage, double pMinDescriptorDominance,
  double pRareDescriptorThreshold, int pNumberOfTextUnitsInClustering,
  TextFile pClusterFile) {
    
    
    RelClusterSize = AbsClusterSize / (double)pNumberOfTextUnitsInClustering;
    
    NumberOfNonRareDescriptors = ClusterDescriptorCounter.getSize();
    DIAsDEMthesaurusTerm descriptor = ClusterDescriptorCounter.getFirstTerm();
    while (descriptor != null) {
      if (descriptor.getOccurrences()/(double)this.AbsClusterSize
      <= pRareDescriptorThreshold) {
        NumberOfNonRareDescriptors--;
      }
      descriptor = ClusterDescriptorCounter.getNextTerm();
    }
    
    if (NumberOfNonRareDescriptors == 0) {
      this.DescriptorDominance = 0.0d;
    }
    else {
      this.DescriptorDominance = this.NumberOfDominantDescriptors
      / (double)NumberOfNonRareDescriptors;
    }
    
    if (this.NumberOfDescriptorsInControlledVocabulary == 0) {
      this.DescriptorCoverage = 0.0d;
    }
    else {
      this.DescriptorCoverage = NumberOfNonRareDescriptors
      / (double)this.NumberOfDescriptorsInControlledVocabulary;
    }
    
    double oneThird = 1.0d / 3.0d;
    if (Double.compare(this.DescriptorCoverage, 0.0d) > 0) {
      this.QualityIndex = (oneThird * (1.0d - DescriptorCoverage))
      + (oneThird * DescriptorDominance) + (oneThird * RelClusterSize);
    }
    else {
      this.QualityIndex = oneThird * RelClusterSize;
    }
    
    if (this.AbsClusterSize >= pMinClusterSize
    && this.DescriptorCoverage <= pMaxDescriptorCoverage
    && this.DescriptorDominance >= pMinDescriptorDominance) {
      IsAscceptable = true;
    }
    else {
      DefaultClusterLabels = "-";
      IsAscceptable = false;
    }
    
    NumberFormat nf6 = NumberFormat.getInstance(Locale.US);
    nf6.setMinimumFractionDigits(6);
    nf6.setMaximumFractionDigits(6);
    NumberFormat nf3 = NumberFormat.getInstance(Locale.US);
    nf3.setMinimumFractionDigits(3);
    nf3.setMaximumFractionDigits(3);

    pClusterFile.open();
    pClusterFile.setNextLine(
    "<a name=\"QUALITY\"><h3>Cluster Quality Assessment</h3>");
    pClusterFile.setNextLine("<p><small>"
    + "<a href=\"#CONTENTS\">Cluster Contents</a> - "
    + "<a href=\"#DESC_BY_SUPP\">Descriptors by Support</a> - "
    + "<a href=\"#DESC_BY_TERM\">Descriptors by Term</a> - "
    + "<a href=\"#NONDESC_BY_SUPP\">Non-Descriptors by Support</a>"
    + "</small></p>");
    pClusterFile.setNextLine(
    "<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Cluster Quality Criterion</th>"
    + "<th align=\"right\" valign=\"top\">Value</th>"
    + "<th align=\"right\" valign=\"top\">Range</th>"
    + "<th align=\"right\" valign=\"top\">Objective</th></tr>");
    pClusterFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Cluster Quality Index</td>"
    + "<td align=\"right\" valign=\"top\">" + nf3.format(this.QualityIndex)
    + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
    + "<td align=\"right\" valign=\"top\">Maximize!</td>"
    + "</td></tr>");
    pClusterFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Descriptor Coverage</td>"
    + "<td align=\"right\" valign=\"top\">" + nf3.format(
    this.DescriptorCoverage)
    + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
    + "<td align=\"right\" valign=\"top\">Minimize!</td>"
    + "</tr>");
    pClusterFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Descriptor Dominance</td>"
    + "<td align=\"right\" valign=\"top\">" + nf3.format(this
    .DescriptorDominance)
    + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
    + "<td align=\"right\" valign=\"top\">Maximize!</td>"
    + "</tr>");
    pClusterFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Absolute Cluster Size</td>"
    + "<td align=\"right\" valign=\"top\">" + this.AbsClusterSize
    + "<td align=\"right\" valign=\"top\">[0; " 
    + pNumberOfTextUnitsInClustering + "]</td>"
    + "<td align=\"right\" valign=\"top\">Maximize!</td>"
    + "</tr>");
    pClusterFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Relative Cluster Size</td>"
    + "<td align=\"right\" valign=\"top\">" + (this.RelClusterSize
    < 0.001 ? nf6.format(this.RelClusterSize) : nf3.format(this.RelClusterSize))
    + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
    + "<td align=\"right\" valign=\"top\">Maximize!</td>"
    + "</tr></table>");

    Template htmlFooter = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_FooterTemplate.html"));
    pClusterFile.setNextLine(htmlFooter.insertValues());
    pClusterFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMthesaurusTerm getClusterDescriptionTerm(
  boolean pIsQualitativelyAccecptable) {
    
    DIAsDEMthesaurusTerm term =
    new DefaultDIAsDEMthesaurusTerm((long)(this.ID),
    String.valueOf(this.ID), 1);
    term.setScopeNotes(this.DefaultClusterLabels);
    if (pIsQualitativelyAccecptable) {
      term.setSynonyms("a/?");
    }
    else {
      term.setSynonyms("u/?");
    }
    term.setBroaderTerm(Integer.toString(AbsClusterSize));
    
    return term;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {
    
    ID = 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}