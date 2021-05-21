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

import java.io.File;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.hypknowsys.diasdem.core.DIAsDEMdocument;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.hypknowsys.misc.io.Itemizer;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1.2.0, 13 May 2004
 * @author Karsten Winkler
 */

public class TextUnitClusterSet implements Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private long ID = 0L;
  
  private int NumberOfClusters = 0;
  private int NumberOfTextUnits = 0;
  private TextUnitCluster[] TextUnitClusters = null;
  private String ClusterDirectory = null;
  protected int VectorDimensions = 0;
  protected String DescriptorsScopeNotesContain = null;
  protected int Iteration = 1;
  protected String ProjectName = null;
  protected String ProjectFileName = null;
  protected String CollectionFileName = null;
  
  protected int MinClusterSize = 0;
  protected double MaxDescriptorCoverage = 0.0;
  protected double MinDescriptorDominance = 0.0;
  protected double DominantDescriptorThreshold = 0.0;
  protected double RareDescriptorThreshold = 0.0;
  protected double FrequentNonDescriptorThreshold = 0.0;
  protected int MaxNumberOfOutputTextUnits = 0;
  protected boolean IgnoreTextUnitsInOutlierCluster = false;
  protected int OutlierClusterID = -1;
  protected double QualityIndex = 0.0d;
  
  // contains entire thesaurus:
  private DIAsDEMthesaurus DescriptorThesaurus = null;
  private DIAsDEMthesaurusTerm MyTerm = null;
  // only contains descriptors:
  private DIAsDEMthesaurus MyDescriptors = null;
  private DIAsDEMthesaurus DescriptorIterationFrequencies = null;
  private DIAsDEMthesaurus NonDescriptorIterationFrequencies = null;
  private int NumberOfDescriptorsInControlledVocabulary = 0;
  private Hashtable MyMappingHashtable = null;
  private String[] MyMappingArray = null;
  private String[] MyHtmlMappingArray = null;
  // for semantic cluster labels:
  private DIAsDEMthesaurus MyClusterDescriptions = null;
  private String ClusterLabelFileName = null;
  private TextFile[] MyClusterFiles = null;
  private int[] MyNumberOfOutputTextUnits = null;
  private TextFile MyClusterSummaryFile = null;
  private TextFile MyDescriptorSummaryFile = null;
  private boolean IgnoreEmptyClusters = false;
  private boolean RankClustersByQuality = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  private transient TextFile ClusterQualityIndicesArffFile = null;
  private transient String ClusterResultFileName = null;
  private transient int NextIdOfClusterQualityIndices = 0;
  
   /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final double LOW_BOUNDARY = 0.6;
  private static final double MEDIUM_BOUNDARY = 0.8;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TextUnitClusterSet(String pProjectName, String pProjectFileName,
  String pCollectionFileName, int pIteration, 
  String pThesaurusFileName, int pNumberOfClusters,
  String pClusterDirectory, int pMinClusterSize, 
  double pMaxDescriptorCoverage, double pMinDescriptorDominance,
  int pVectorDimensions, String pDescriptorsScopeNotesContain,
  String pClusterLabelFileName, boolean pIgnoreEmptyClusters,
  boolean pRankClustersByQuality, double pDominantDescriptorThreshold,
  double pRareDescriptorThreshold, double pFrequentNonDescriptorThreshold,
  int pMaxNumberOfOutputTextUnits, String pClusterResultFileName,
  boolean pIgnoreTextUnitsInOutlierCluster, int pOutlierClusterID) {
    
    this.ID = 0L;
    this.Iteration = pIteration;
    this.ProjectName = pProjectName;
    this.ProjectFileName = pProjectFileName;
    this.CollectionFileName = pCollectionFileName;
    // include cluster 0 as well as garbage cluster
    this.NumberOfClusters = pNumberOfClusters + 2;
    this.NumberOfTextUnits = 0;
    this.ClusterDirectory = pClusterDirectory;
    this.MinClusterSize = pMinClusterSize;
    this.MaxDescriptorCoverage = pMaxDescriptorCoverage;
    this.MinDescriptorDominance = pMinDescriptorDominance;
    this.DominantDescriptorThreshold = pDominantDescriptorThreshold;
    this.RareDescriptorThreshold = pRareDescriptorThreshold;
    this.FrequentNonDescriptorThreshold = pFrequentNonDescriptorThreshold;
    this.MaxNumberOfOutputTextUnits = pMaxNumberOfOutputTextUnits;
    this.ClusterResultFileName = pClusterResultFileName;
    this.IgnoreTextUnitsInOutlierCluster = pIgnoreTextUnitsInOutlierCluster;
    this.OutlierClusterID = pOutlierClusterID;
    VectorDimensions = pVectorDimensions;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    ClusterLabelFileName = pClusterLabelFileName;
    IgnoreEmptyClusters = pIgnoreEmptyClusters;
    RankClustersByQuality = pRankClustersByQuality;
    
    DescriptorThesaurus = new DefaultDIAsDEMthesaurus();
    DescriptorThesaurus.load(pThesaurusFileName);
    MyDescriptors = new DefaultDIAsDEMthesaurus();
    NumberOfDescriptorsInControlledVocabulary = 0;
    MyMappingHashtable = new Hashtable();
    MyMappingArray = null;
    MyClusterDescriptions = new DefaultDIAsDEMthesaurus(
    "ClusterID = Term; ClusterTag = ScopeNotes; Decision = Synonyms"
    + "; NumberOfTextUnitsInCluster = BroaderTerm", 1);
    
    this.fillThesauri();
    this.TextUnitClusters = new TextUnitCluster[this.NumberOfClusters];
    for (int i = 0; i < TextUnitClusters.length; i++) {
      TextUnitClusters[i] = new TextUnitCluster(i, 
      NumberOfDescriptorsInControlledVocabulary);
    }
    this.createEmptyFiles();
    
    MyNumberOfOutputTextUnits = new int[this.NumberOfClusters];
    for (int i = 0; i < MyNumberOfOutputTextUnits.length; i++) {
      MyNumberOfOutputTextUnits[i] = 0;
    }
    
    DescriptorIterationFrequencies = new DefaultDIAsDEMthesaurus();
    NonDescriptorIterationFrequencies = new DefaultDIAsDEMthesaurus();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public long getID() {
    return ID; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setID(long pID) {
    this.ID = pID; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(", ID=");
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
    TmpStringBuffer.append(Itemizer.longToItem(ID));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) {
    
    ID = 0L;
    Itemizer itemizer = new Itemizer(pItemLine);
    
    try {
      ID = itemizer.itemToLong(itemizer.getNextItem());
    }
    catch (NoSuchElementException e1) { this.reset(); }
    catch (NumberFormatException e2) { this.reset(); }
    catch (StringIndexOutOfBoundsException e2) { this.reset(); }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getAndCountDescriptors(String pTokenizedLine,
  DIAsDEMthesaurus pDescriptors, Hashtable pMappingHashtable,
  String[] pMappingHeader, int pDescriptorCounter, int pClusterID,
  DIAsDEMdocument pCurrentDiasdemDocument, String[] pHtmlMappingHeader) {
    
    String[] oCurrentVector = new String[pDescriptorCounter];
    StringTokenizer tokenizer = null;
    DIAsDEMthesaurus oCurrentSentenceDescriptors =
    new DefaultDIAsDEMthesaurus();
    DIAsDEMthesaurus oCurrentSentenceNonDescriptors =
    new DefaultDIAsDEMthesaurus();
    DIAsDEMthesaurus oIterationDescriptors =
    new DefaultDIAsDEMthesaurus();
    DIAsDEMthesaurus oIterationNonDescriptors =
    new DefaultDIAsDEMthesaurus();
    DIAsDEMthesaurusTerm term = null;
    String word = null;
    
    tokenizer = new StringTokenizer(pTokenizedLine);
    while (tokenizer.hasMoreElements()) {
      word = tokenizer.nextToken();
      if (NamedEntity.isPlaceholder(word)) {
        word = (pCurrentDiasdemDocument.getActiveTextUnitsLayer()
        .getNamedEntity(NamedEntity.getNamedEntityIndex(word)))
        .getPossibleTypesPlaceholder();
      }
      term = pDescriptors.getDescriptorTerm(word);
      if (term != null) {
        oCurrentSentenceDescriptors.countOccurrence(term.getWord());
        oIterationDescriptors.countOccurrence(term.getWord());
      }
      else {
        if (Tools.stringContainsLetter(word)) {
          oCurrentSentenceNonDescriptors.countOccurrence(word);
          oIterationNonDescriptors.countOccurrence(word);
        }
      }
    }
    
    // descriptors in clusters
    for (int i = 0; i < oCurrentVector.length; i++) {
      oCurrentVector[i] = "0";
    }
    term = oCurrentSentenceDescriptors.getFirstTerm();
    while (term != null) {
      oCurrentVector[((Integer)pMappingHashtable.get(term.getWord()))
      .intValue()] = term.getOccurrences() + "";
      term = oCurrentSentenceDescriptors.getNextTerm();
    }
    
    String result = "";
    int vOutputCounter = 0;
    for (int i = 0; i < oCurrentVector.length; i++) {
      if (!oCurrentVector[i].trim().equals("0")) {
        if (vOutputCounter > 0) {
          result += ", ";
        }
        result += pHtmlMappingHeader[i].trim();
        if (!oCurrentVector[i].trim().equals("1")) {
          result += " (" + oCurrentVector[i].trim() + ")";
        }
        vOutputCounter++;
        // binary frequency counter
        TextUnitClusters[pClusterID].countDescriptorOccurrence(
        pMappingHeader[i].trim());
      }
    }
    
    // non-descriptors in clusters
    term = oCurrentSentenceNonDescriptors.getFirstTerm();
    while (term != null) {
      // binary frequency counter
      TextUnitClusters[pClusterID].countNonDescriptorOccurrence(term.getWord());
      term = oCurrentSentenceNonDescriptors.getNextTerm();
    }
    
    // update descriptor frequencies in iteration
    term = oIterationDescriptors.getFirstTerm();
    while (term != null) {
      // binary frequency counter
      DescriptorIterationFrequencies.countOccurrence(term.getWord());
      term = oIterationDescriptors.getNextTerm();
    }
    
    // update non-descriptors frequencies in iteration
    term = oIterationNonDescriptors.getFirstTerm();
    while (term != null) {
      // binary frequency counter
      NonDescriptorIterationFrequencies.countOccurrence(term.getWord());
      term = oIterationNonDescriptors.getNextTerm();
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addTextUnit(int pNewClusterID, String pLineZerlegt,
  String pLineGrundformen, String pCurrentXmlFileName,
  int pNewSentenceID, DIAsDEMdocument pCurrentDiasdemDocument,
  String pDumpDirectory, String pHtmlBaseDirectory, boolean pDumpDocuments,
  int pMaxNumberOfOutputTextUnits) {
    
    if (pNewClusterID < 0 || pNewClusterID >= this.NumberOfClusters) {
      pNewClusterID = this.NumberOfClusters - 1;  // garbage cluster
    }
    this.NumberOfTextUnits++;
    TmpStringBuffer = new StringBuffer(5000);
    if (this.MyNumberOfOutputTextUnits[pNewClusterID]
    < pMaxNumberOfOutputTextUnits) {
      this.MyNumberOfOutputTextUnits[pNewClusterID]++;
      // count descriptors and output text unit
      TmpStringBuffer.append("<p>");
      TmpStringBuffer.append(pLineZerlegt);
      TmpStringBuffer.append("<br><font color=\"silver\">Document: ");
      if (pDumpDocuments) {
        TmpStringBuffer.append("<a href=\"");
        TmpStringBuffer.append(Tools.getRelativeFileName(pHtmlBaseDirectory,
        pCurrentDiasdemDocument.dumpAsXmlFile(pDumpDirectory, true)));
        TmpStringBuffer.append("\">");
      }
      TmpStringBuffer.append(pCurrentDiasdemDocument.getDiasdemDocumentID());
      if (pDumpDocuments) {
        TmpStringBuffer.append("</a> ");
      }
      TmpStringBuffer.append(" - Text Unit: ");
      TmpStringBuffer.append(pNewSentenceID);
      TmpStringBuffer.append(" - Descriptors: ");
      TmpStringBuffer.append(this.getAndCountDescriptors(
      pLineGrundformen, DescriptorThesaurus, MyMappingHashtable, MyMappingArray,
      NumberOfDescriptorsInControlledVocabulary, pNewClusterID, 
      pCurrentDiasdemDocument, MyHtmlMappingArray));
      TmpStringBuffer.append("</font></p>");
      MyClusterFiles[pNewClusterID].open();
      MyClusterFiles[pNewClusterID].setNextLine(TmpStringBuffer.toString());
      MyClusterFiles[pNewClusterID].close();
    }
    else {
      // count descriptors, but do not output text unit
      TmpStringBuffer.append(this.getAndCountDescriptors(
      pLineGrundformen, DescriptorThesaurus, MyMappingHashtable, MyMappingArray,
      NumberOfDescriptorsInControlledVocabulary, pNewClusterID,
      pCurrentDiasdemDocument, MyHtmlMappingArray));
    }
    this.TextUnitClusters[pNewClusterID].incrementClusterCardinality();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void assessClusterQuality() {
    
    DIAsDEMthesaurusTerm currentTerm = null;
    MyDescriptorSummaryFile.setNextLine(
    "<table border=\"1\" align=\"center\" width=\"100%\"><tr>"
    + "<th align=\"left\">Descriptor</th>");
    for (int i = 0; i < this.NumberOfClusters; i++) {
      MyDescriptorSummaryFile.setNextLine("<th align=\"center\" width=\"15\">"
      + i + "</th>");
      TextUnitClusters[i].computeDescriptorFrequencies(MyClusterFiles[i],
      MyClusterSummaryFile, this.NumberOfClusters, this.NumberOfTextUnits,
      MyMappingHashtable, this.DominantDescriptorThreshold, 
      this.RareDescriptorThreshold, this.FrequentNonDescriptorThreshold, 
      DescriptorIterationFrequencies, NonDescriptorIterationFrequencies,
      this.NumberOfTextUnits);
    }
    
    Template htmlFooter = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_FooterTemplate.html"));
    
    // complete cluster summary; rows = descriptors;
    // columns = relative frequencies
    MyDescriptorSummaryFile.setNextLine("<tr>");
    String summaryLine = null;
    for (int i = 0; i < NumberOfDescriptorsInControlledVocabulary; i++) {
      currentTerm = DescriptorThesaurus.get(MyMappingArray[i]);
      // only valid descriptors
      if ((currentTerm != null) && (this.isDescriptor(
      currentTerm.getWord(), DescriptorThesaurus))) {
        summaryLine = "<tr> <td>" + MyMappingArray[i] + "</td>";
        for (int j = 0; j < this.NumberOfClusters; j++) {
          summaryLine += TextUnitClusters[j].extentDescriptorSummaryLine(i);
        }
        summaryLine += "</tr>";
        MyDescriptorSummaryFile.setNextLine(summaryLine);
      }
    }
    MyDescriptorSummaryFile.setNextLine("</table>");
    MyDescriptorSummaryFile.setNextLine(htmlFooter.insertValues());
    MyDescriptorSummaryFile.close();
    
    MyClusterSummaryFile.setNextLine(htmlFooter.insertValues());
    MyClusterSummaryFile.close();
    
    NumberFormat nf = NumberFormat.getInstance(Locale.US);
    nf.setMinimumFractionDigits(2);
    nf.setMaximumFractionDigits(2);
    NumberFormat nf3 = NumberFormat.getInstance(Locale.US);
    nf3.setMinimumFractionDigits(3);
    nf3.setMaximumFractionDigits(3);
    NumberFormat nf6 = NumberFormat.getInstance(Locale.US);
    nf6.setMinimumFractionDigits(6);
    nf6.setMaximumFractionDigits(6);
    Template htmlHeader = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_HeaderTemplate.html"));
    htmlHeader.addValue("${Title}", "DIAsDEM Cluster Quality Monitor");
    
    TextFile indexHtmlFile = new TextFile(new File(
    this.ClusterDirectory + File.separator + "index.html"));
    indexHtmlFile.open();
    indexHtmlFile.setFirstLine(htmlHeader.insertValues());
    indexHtmlFile.setNextLine(
    "<p>Created by Tasks &gt; Postprocess Patterns "
    + "&gt; Monitor Cluster Quality 2.2 on " + Tools.getSystemDate() +"</p>");
    indexHtmlFile.setNextLine(
    "<p> Related Pages: <a href=\"clusterSummary.html\" target=\"_new\">"
    + "Cluster Summary</a> - <a href=\"descriptorSummary.html\" "
    + "target=\"_new\">Descriptor Summary</a> </p>");
    indexHtmlFile.setNextLine(
    "<a name=\"SETTINGS\"><h3>Parameter Settings</h3>");
    indexHtmlFile.setNextLine("<p><small>"
    + "<a href=\"#ACCEPTABLE\">Acceptable Clusters</a> - "
    + "<a href=\"#UNACCEPTABLE\">Unacceptable Clusters</a> - "
    + "<a href=\"#GARBAGE\">Garbage Cluster</a> - "
    + "<a href=\"#QUALITY\">Clustering Quality Assessment</a>"
    + "</small></p>");
    indexHtmlFile.setNextLine(
    "<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Parameter</th>"
    + "<th align=\"left\" valign=\"top\">Value</th></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">DIAsDEM Project Name</td>"
    + "<td align=\"left\" valign=\"top\">" + this.ProjectName + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">DIAsDEM Project File</td>"
    + "<td align=\"left\" valign=\"top\">" + Tools.shortenFileName(
    this.ProjectFileName, 60) + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">DIAsDEM Collection</td>"
    + "<td align=\"left\" valign=\"top\">" + Tools.shortenFileName(
    this.CollectionFileName, 60) + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">KDT Process Iteration</td>"
    + "<td align=\"left\" valign=\"top\">" + this.Iteration
    + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Dominant Descriptor Threshold</td>"
    + "<td align=\"left\" valign=\"top\">" + this.DominantDescriptorThreshold
    + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Rare Descriptor Threshold</td>"
    + "<td align=\"left\" valign=\"top\">" + this.RareDescriptorThreshold
    + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Maximum Descriptor Coverage</td>"
    + "<td align=\"left\" valign=\"top\">" + this.MaxDescriptorCoverage
    + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Minimum Descriptor Dominance</td>"
    + "<td align=\"left\" valign=\"top\">" + this.MinDescriptorDominance
    + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Minimum Cluster Size</td>"
    + "<td align=\"left\" valign=\"top\">" + this.MinClusterSize
    + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Frequent Non-Descriptor Threshold</td>"
    + "<td align=\"left\" valign=\"top\">" + this.FrequentNonDescriptorThreshold
    + "</td></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Max. Number of Output Text Units</td>"
    + "<td align=\"left\" valign=\"top\">" + this.MaxNumberOfOutputTextUnits
    + "</td></tr></table>");
    
    // count acceptable and unacceptable clusters and vectors therein
    int numberOfAcceptableClusters = 0;
    int numberOfUnacceptableClusters = 0;
    int numberOfVectorsInAcceptableClusters = 0;
    int numberOfVectorsInUnacceptableClusters = 0;
    int numberOfEmptyClusters = 0;
    QualityIndex = 0.0d;
    // garbage cluster is not counted since it was not created by clusterer
    for (int i = 0; i < (TextUnitClusters.length - 1); i++) {
      TextUnitClusters[i].computeQualityIndex(this.MinClusterSize,
      this.MaxDescriptorCoverage, this.MinDescriptorDominance,
      this.RareDescriptorThreshold, this.NumberOfTextUnits, MyClusterFiles[i]);
      QualityIndex += (TextUnitClusters[i].getAbsClusterSize()
      * TextUnitClusters[i].getQualityIndex());
      if (TextUnitClusters[i].isAcceptable()) {
        numberOfAcceptableClusters++;
        numberOfVectorsInAcceptableClusters += TextUnitClusters[i]
        .getAbsClusterSize();
      }
      else {
        numberOfUnacceptableClusters++;
        numberOfVectorsInUnacceptableClusters += TextUnitClusters[i]
        .getAbsClusterSize();
        if (TextUnitClusters[i].getAbsClusterSize() == 0) {
          numberOfEmptyClusters++;
        }
      }
    }
    QualityIndex /= (double)this.NumberOfTextUnits;
    
    this.appendClusterQualityIndicesToFile(NextIdOfClusterQualityIndices
    + ",\"MinClusterSize=" + this.MinClusterSize + " "
    + "MaxDescriptorCoverage=" + this.MaxDescriptorCoverage + " "
    + "MinDescriptorDominance=" + this.MinDescriptorDominance + " "
    + "DominantDescriptorThreshold=" + this.DominantDescriptorThreshold + "\","
    + "RareDescriptorThreshold=" + this.RareDescriptorThreshold + "\","
    + this.NumberOfClusters + "," + numberOfAcceptableClusters + ","
    + (numberOfAcceptableClusters / (double)this.NumberOfClusters) + ","
    + numberOfUnacceptableClusters + "," + (numberOfUnacceptableClusters 
    / (double)this.NumberOfClusters) + "," + this.NumberOfTextUnits + ","
    + numberOfVectorsInAcceptableClusters + "," 
    + (numberOfVectorsInAcceptableClusters / (double)this.NumberOfTextUnits) 
    + "," + numberOfVectorsInUnacceptableClusters + "," 
    + (numberOfVectorsInUnacceptableClusters / (double)this.NumberOfTextUnits)
    + "," + QualityIndex);

    if (!RankClustersByQuality) {
      indexHtmlFile.setNextLine(
      "<a name=\"ACCEPTABLE\"><h3>Qualitatively Acceptable Clusters</h3>");
      indexHtmlFile.setNextLine("<p><small>"
      + "<a href=\"#SETTINGS\">Parameter Settings</a> - "
      + "<a href=\"#UNACCEPTABLE\">Unacceptable Clusters</a> - "
      + "<a href=\"#GARBAGE\">Garbage Cluster</a> - "
      + "<a href=\"#QUALITY\">Clustering Quality Assessment</a>"
      + "</small></p>");
      indexHtmlFile.setNextLine("<p>" + numberOfAcceptableClusters + " ("
      + nf.format(numberOfAcceptableClusters * 100.0d
      / (numberOfAcceptableClusters + numberOfUnacceptableClusters))
      + "%) of " + (numberOfAcceptableClusters + numberOfUnacceptableClusters)
      + " text unit vector clusters are qualitatively acceptable. "
      + numberOfVectorsInAcceptableClusters + " (" + nf.format(
      numberOfVectorsInAcceptableClusters * 100.0d
      / (numberOfVectorsInAcceptableClusters
      + numberOfVectorsInUnacceptableClusters))
      + "%) of " + (numberOfVectorsInAcceptableClusters
      + numberOfVectorsInUnacceptableClusters) + " text unit vectors are "
      + "assigned to qualitatively acceptable clusters.</p>");
      indexHtmlFile.setNextLine(
      "<table border=\"1\">"
      + "<th width=\"10%\" nowrap align=\"left\" valign=\"top\">"
      + "Cluster&nbsp;ID</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Cluster<br>Quality Index</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Descriptor<br>Coverage</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Descriptor<br>Dominance</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Absolute<br>Cluster Size</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Relative<br>Cluster Size</th></tr>");
      for (int i = 0; i < (TextUnitClusters.length - 1); i++) {
        if (TextUnitClusters[i].isAcceptable()
        && (TextUnitClusters[i].getAbsClusterSize() > 0
        || ! IgnoreEmptyClusters)) {
          indexHtmlFile.setNextLine("<tr><td align=\"left\"><a href=\"cluster"
          + i + ".html#DESC_BY_SUPP" + "\" target=\"_new\">Cluster&nbsp;" + i
          + "</a></td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + nf3.format(TextUnitClusters[i].getQualityIndex())
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getAbsClusterSize() > 0
          ? "<font color=\"silver\">"
          + " (" + TextUnitClusters[i].getNumberOfNonRareDescriptors()
          + "/" + TextUnitClusters[i].getNumberOfDescriptors()
          + " =) " + "</font>"
          + nf3.format(TextUnitClusters[i].getDescriptorCoverage()) : "&nbsp")
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getAbsClusterSize() > 0
          ? "<font color=\"silver\">"
          + "(" + TextUnitClusters[i].getNumberOfDominantDescriptors()
          + "/" + TextUnitClusters[i].getNumberOfNonRareDescriptors()
          + " =) " + "</font>"
          + nf3.format(TextUnitClusters[i].getDescriptorDominance()) : "&nbsp")
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + TextUnitClusters[i].getAbsClusterSize()
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getRelClusterSize() < 0.001 
          ? nf6.format(TextUnitClusters[i].getRelClusterSize()) 
          : nf3.format(TextUnitClusters[i].getRelClusterSize()))
          + "</td></tr>");
        }
        if (TextUnitClusters[i].isAcceptable()) {
          MyClusterDescriptions.add(TextUnitClusters[i]
          .getClusterDescriptionTerm(true));
        }
      }
      indexHtmlFile.setNextLine("</table>");
      
      indexHtmlFile.setNextLine(
      "<a name=\"UNACCEPTABLE\"><h3>Qualitatively Unacceptable Clusters</h3>");
      indexHtmlFile.setNextLine("<p><small>"
      + "<a href=\"#SETTINGS\">Parameter Settings</a> - "
      + "<a href=\"#ACCEPTABLE\">Acceptable Clusters</a> - "
      + "<a href=\"#GARBAGE\">Garbage Cluster</a> - "
      + "<a href=\"#QUALITY\">Clustering Quality Assessment</a>"
      + "</small></p>");
      indexHtmlFile.setNextLine("<p>" + numberOfUnacceptableClusters + " ("
      + nf.format(numberOfUnacceptableClusters * 100.0d
      /  (numberOfAcceptableClusters + numberOfUnacceptableClusters))
      + "%) of " + (numberOfAcceptableClusters + numberOfUnacceptableClusters)
      + " text unit vector clusters are qualitatively unacceptable. "
      + numberOfEmptyClusters + " qualitatively unacceptable clusters "
      + (numberOfEmptyClusters == 1 ? "is" : "are") + " empty. "
      + numberOfVectorsInUnacceptableClusters + " (" + nf.format(
      numberOfVectorsInUnacceptableClusters * 100.0d
      / (numberOfVectorsInAcceptableClusters
      + numberOfVectorsInUnacceptableClusters))
      + "%) of " + (numberOfVectorsInAcceptableClusters
      + numberOfVectorsInUnacceptableClusters) + " text unit vectors are "
      + "assigned to qualitatively unacceptable clusters.</p>");
      indexHtmlFile.setNextLine(
      "<table border=\"1\">"
      + "<th width=\"10%\" nowrap align=\"left\" valign=\"top\">"
      + "Cluster&nbsp;ID</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Cluster<br>Quality Index</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Descriptor<br>Coverage</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Descriptor<br>Dominance</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Absolute<br>Cluster Size</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Relative<br>Cluster Size</th></tr>");
      for (int i = 0; i < (TextUnitClusters.length - 1); i++) {
        if (!TextUnitClusters[i].isAcceptable()
        && (TextUnitClusters[i].getAbsClusterSize() > 0
        || ! IgnoreEmptyClusters)) {
          indexHtmlFile.setNextLine("<tr><td align=\"left\"><a href=\"cluster"
          + i + ".html#DESC_BY_SUPP" + "\" target=\"_new\">Cluster&nbsp;" + i
          + "</a></td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + nf3.format(TextUnitClusters[i].getQualityIndex())
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getAbsClusterSize() > 0
          ? "<font color=\"silver\">"
          + " (" + TextUnitClusters[i].getNumberOfNonRareDescriptors()
          + "/" + TextUnitClusters[i].getNumberOfDescriptors()
          + " =) " + "</font>"
          + nf3.format(TextUnitClusters[i].getDescriptorCoverage()) : "&nbsp")
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getAbsClusterSize() > 0
          ? "<font color=\"silver\">"
          + "(" + TextUnitClusters[i].getNumberOfDominantDescriptors()
          + "/" + TextUnitClusters[i].getNumberOfNonRareDescriptors()
          + " =) " + "</font>"
          + nf3.format(TextUnitClusters[i].getDescriptorDominance()) : "&nbsp")
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + TextUnitClusters[i].getAbsClusterSize()
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getRelClusterSize() < 0.001 
          ? nf6.format(TextUnitClusters[i].getRelClusterSize()) 
          : nf3.format(TextUnitClusters[i].getRelClusterSize()))
          + "</td></tr>");
        }
        if (!TextUnitClusters[i].isAcceptable()) {
          MyClusterDescriptions.add(TextUnitClusters[i]
          .getClusterDescriptionTerm(false));
        }
      }
      indexHtmlFile.setNextLine("</table>");
    } 
    else {  // rank clusters by decreasing quality
      indexHtmlFile.setNextLine(
      "<a name=\"ACCEPTABLE\"><h3>Qualitatively Acceptable Clusters "
      + "by Quality Index</h3>");
      indexHtmlFile.setNextLine("<p><small>"
      + "<a href=\"#SETTINGS\">Parameter Settings</a> - "
      + "<a href=\"#UNACCEPTABLE\">Unacceptable Clusters</a> - "
      + "<a href=\"#GARBAGE\">Garbage Cluster</a> - "
      + "<a href=\"#QUALITY\">Clustering Quality Assessment</a>"
      + "</small></p>");
      indexHtmlFile.setNextLine("<p>" + numberOfAcceptableClusters + " ("
      + nf.format(numberOfAcceptableClusters * 100.0d
      / (numberOfAcceptableClusters + numberOfUnacceptableClusters))
      + "%) of " + (numberOfAcceptableClusters + numberOfUnacceptableClusters)
      + " text unit vector clusters are qualitatively acceptable. "
      + numberOfVectorsInAcceptableClusters + " (" + nf.format(
      numberOfVectorsInAcceptableClusters * 100.0d
      / (numberOfVectorsInAcceptableClusters
      + numberOfVectorsInUnacceptableClusters))
      + "%) of " + (numberOfVectorsInAcceptableClusters
      + numberOfVectorsInUnacceptableClusters) + " text unit vectors are "
      + "assigned to qualitatively acceptable clusters.</p>");
      indexHtmlFile.setNextLine(
      "<table border=\"1\">"
      + "<th width=\"10%\" nowrap align=\"left\" valign=\"top\">"
      + "Cluster&nbsp;ID</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Cluster<br>Quality Index</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Descriptor<br>Coverage</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Descriptor<br>Dominance</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Absolute<br>Cluster Size</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Relative<br>Cluster Size</th></tr>");
      TreeMap rankedClusters = new TreeMap();
      double epsilon = 0.0d;
      for (int i = 0; i < (TextUnitClusters.length - 1); i++) {
        if (TextUnitClusters[i].isAcceptable()) {
          // check whether map contains another cluster of the same quality
          epsilon = 0.0d;
          while (rankedClusters.get(new Double(1.0d - TextUnitClusters[i]
          .getQualityIndex() - epsilon)) != null) {
            epsilon += 0.00001d;
          }
          rankedClusters.put(new Double(1.0d - TextUnitClusters[i]
          .getQualityIndex() - epsilon), new Integer(i));
        }
      }
      for (Iterator iterator = rankedClusters.keySet().iterator();
      iterator.hasNext(); ) {
        int i = ((Integer)rankedClusters.get(iterator.next())).intValue();
        if (TextUnitClusters[i].getAbsClusterSize() > 0
        || ! IgnoreEmptyClusters) {
          indexHtmlFile.setNextLine("<tr><td align=\"left\"><a href=\"cluster"
          + i + ".html#DESC_BY_SUPP" + "\" target=\"_new\">Cluster&nbsp;" + i
          + "</a></td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + nf3.format(TextUnitClusters[i].getQualityIndex())
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getAbsClusterSize() > 0
          ? "<font color=\"silver\">"
          + " (" + TextUnitClusters[i].getNumberOfNonRareDescriptors()
          + "/" + TextUnitClusters[i].getNumberOfDescriptors()
          + " =) " + "</font>"
          + nf3.format(TextUnitClusters[i].getDescriptorCoverage()) : "&nbsp")
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getAbsClusterSize() > 0
          ? "<font color=\"silver\">"
          + "(" + TextUnitClusters[i].getNumberOfDominantDescriptors()
          + "/" + TextUnitClusters[i].getNumberOfNonRareDescriptors()
          + " =) " + "</font>"
          + nf3.format(TextUnitClusters[i].getDescriptorDominance()) : "&nbsp")
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + TextUnitClusters[i].getAbsClusterSize()
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getRelClusterSize() < 0.001
          ? nf6.format(TextUnitClusters[i].getRelClusterSize())
          : nf3.format(TextUnitClusters[i].getRelClusterSize()))
          + "</td></tr>");
        }
        if (TextUnitClusters[i].isAcceptable()) {
          MyClusterDescriptions.add(TextUnitClusters[i]
          .getClusterDescriptionTerm(true));
        }
      }
      indexHtmlFile.setNextLine("</table>");
      
      indexHtmlFile.setNextLine(
      "<a name=\"UNACCEPTABLE\"><h3>Qualitatively Unacceptable Clusters "
      + "by Quality Index</h3>");
      indexHtmlFile.setNextLine("<p><small>"
      + "<a href=\"#SETTINGS\">Parameter Settings</a> - "
      + "<a href=\"#ACCEPTABLE\">Acceptable Clusters</a> - "
      + "<a href=\"#GARBAGE\">Garbage Cluster</a> - "
      + "<a href=\"#QUALITY\">Clustering Quality Assessment</a>"
      + "</small></p>");
      indexHtmlFile.setNextLine("<p>" + numberOfUnacceptableClusters + " ("
      + nf.format(numberOfUnacceptableClusters * 100.0d
      /  (numberOfAcceptableClusters + numberOfUnacceptableClusters))
      + "%) of " + (numberOfAcceptableClusters + numberOfUnacceptableClusters)
      + " text unit vector clusters are qualitatively unacceptable. "
      + numberOfEmptyClusters + " qualitatively unacceptable clusters "
      + (numberOfEmptyClusters == 1 ? "is" : "are") + " empty. "
      + numberOfVectorsInUnacceptableClusters + " (" + nf.format(
      numberOfVectorsInUnacceptableClusters * 100.0d
      / (numberOfVectorsInAcceptableClusters
      + numberOfVectorsInUnacceptableClusters))
      + "%) of " + (numberOfVectorsInAcceptableClusters
      + numberOfVectorsInUnacceptableClusters) + " text unit vectors are "
      + "assigned to qualitatively unacceptable clusters.</p>");
      indexHtmlFile.setNextLine(
      "<table border=\"1\">"
      + "<th width=\"10%\" nowrap align=\"left\" valign=\"top\">"
      + "Cluster&nbsp;ID</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Cluster<br>Quality Index</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Descriptor<br>Coverage</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Descriptor<br>Dominance</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Absolute<br>Cluster Size</th>"
      + "<th width=\"18%\" align=\"right\" valign=\"top\">"
      + "Relative<br>Cluster Size</th></tr>");
      rankedClusters = new TreeMap();
      epsilon = 0.0d;
      for (int i = 0; i < (TextUnitClusters.length - 1); i++) {
        if (!TextUnitClusters[i].isAcceptable()) {
          // check whether map contains another cluster of the same quality
          epsilon = 0.0d;
          while (rankedClusters.get(new Double(1.0d - TextUnitClusters[i]
          .getQualityIndex() - epsilon)) != null) {
            epsilon += 0.00001d;
          }
          rankedClusters.put(new Double(1.0d - TextUnitClusters[i]
          .getQualityIndex() - epsilon), new Integer(i));
        }
      }
      for (Iterator iterator = rankedClusters.keySet().iterator();
      iterator.hasNext(); ) {
        int i = ((Integer)rankedClusters.get(iterator.next())).intValue();
        if (TextUnitClusters[i].getAbsClusterSize() > 0
        || ! IgnoreEmptyClusters) {
          indexHtmlFile.setNextLine("<tr><td align=\"left\"><a href=\"cluster"
          + i + ".html#DESC_BY_SUPP" + "\" target=\"_new\">Cluster&nbsp;" + i
          + "</a></td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + nf3.format(TextUnitClusters[i].getQualityIndex())
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getAbsClusterSize() > 0
          ? "<font color=\"silver\">"
          + " (" + TextUnitClusters[i].getNumberOfNonRareDescriptors()
          + "/" + TextUnitClusters[i].getNumberOfDescriptors()
          + " =) " + "</font>"
          + nf3.format(TextUnitClusters[i].getDescriptorCoverage()) : "&nbsp")
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getAbsClusterSize() > 0
          ? "<font color=\"silver\">"
          + "(" + TextUnitClusters[i].getNumberOfDominantDescriptors()
          + "/" + TextUnitClusters[i].getNumberOfNonRareDescriptors()
          + " =) " + "</font>"
          + nf3.format(TextUnitClusters[i].getDescriptorDominance()) : "&nbsp")
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + TextUnitClusters[i].getAbsClusterSize()
          + "</td>");
          indexHtmlFile.setNextLine("<td align=\"right\">"
          + (TextUnitClusters[i].getRelClusterSize() < 0.001
          ? nf6.format(TextUnitClusters[i].getRelClusterSize())
          : nf3.format(TextUnitClusters[i].getRelClusterSize()))
          + "</td></tr>");
        }
        if (!TextUnitClusters[i].isAcceptable()) {
          MyClusterDescriptions.add(TextUnitClusters[i]
          .getClusterDescriptionTerm(false));
        }
      }
      indexHtmlFile.setNextLine("</table>");
    }
    
    indexHtmlFile.setNextLine("<a name=\"GARBAGE\"><h3>Garbage Cluster</h3>");
    indexHtmlFile.setNextLine("<p><small>"
    + "<a href=\"#SETTINGS\">Parameter Settings</a> - "
    + "<a href=\"#ACCEPTABLE\">Acceptable Clusters</a> - "
    + "<a href=\"#UNACCEPTABLE\">Unacceptable Clusters</a> - "
    + "<a href=\"#QUALITY\">Clustering Quality Assessment</a>"
    + "</small></p>");
    indexHtmlFile.setNextLine("<p>The additional garbage cluster only contains "
    + "text unit vectors that are assigned a negative cluster ID or a cluster "
    + "ID that is greater than the maximum cluster ID output by the clustering "
    + "algorithm. Prior to executing the task 'Tag Text Units', this garbage "
    + "cluster must be empty.</p>");
    indexHtmlFile.setNextLine(
    "<table border=\"1\">"
    + "<th width=\"10%\" nowrap align=\"left\" valign=\"top\">"
    + "Cluster&nbsp;ID</th>"
    + "<th width=\"18%\" align=\"right\" valign=\"top\">"
    + "Cluster<br>Quality Index</th>"
    + "<th width=\"18%\" align=\"right\" valign=\"top\">"
    + "Descriptor<br>Coverage</th>"
    + "<th width=\"18%\" align=\"right\" valign=\"top\">"
    + "Descriptor<br>Dominance</th>"
    + "<th width=\"18%\" align=\"right\" valign=\"top\">"
    + "Absolute<br>Cluster Size</th>"
    + "<th width=\"18%\" align=\"right\" valign=\"top\">"
    + "Relative<br>Cluster Size</th></tr>");
    indexHtmlFile.setNextLine("<tr><td align=\"left\"><a href=\"cluster"
    + (this.NumberOfClusters - 1) + ".html#DESC_BY_SUPP" 
    + "\" target=\"_new\">Cluster&nbsp;" + (this.NumberOfClusters - 1)
    + "</a></td>");
    indexHtmlFile.setNextLine("<td align=\"right\">" + nf3.format(
    TextUnitClusters[this.NumberOfClusters - 1].getQualityIndex()) + "</td>");
    indexHtmlFile.setNextLine("<td align=\"right\">" + (TextUnitClusters[
    this.NumberOfClusters - 1].getAbsClusterSize() > 0 
    ? "<font color=\"silver\">" + " (" + TextUnitClusters[this.NumberOfClusters
    - 1].getNumberOfNonRareDescriptors() + "/" + TextUnitClusters[this
    .NumberOfClusters - 1].getNumberOfDescriptors() + " =) " + "</font>"
    + nf3.format(TextUnitClusters[this.NumberOfClusters - 1]
    .getDescriptorCoverage()) : "&nbsp") + "</td>");
    indexHtmlFile.setNextLine("<td align=\"right\">"
    + (TextUnitClusters[this.NumberOfClusters - 1].getAbsClusterSize() > 0
    ? "<font color=\"silver\">" + "(" + TextUnitClusters[this.NumberOfClusters
    - 1].getNumberOfDominantDescriptors() + "/" + TextUnitClusters[this
    .NumberOfClusters - 1].getNumberOfNonRareDescriptors() + " =) " + "</font>"
    + nf3.format(TextUnitClusters[this.NumberOfClusters - 1]
    .getDescriptorDominance()) : "&nbsp") + "</td>");
    indexHtmlFile.setNextLine("<td align=\"right\">" + TextUnitClusters[
    this.NumberOfClusters - 1].getAbsClusterSize() + "</td>");
    indexHtmlFile.setNextLine("<td align=\"right\">"
    + (TextUnitClusters[this.NumberOfClusters - 1].getRelClusterSize() < 0.001
    ? nf6.format(TextUnitClusters[this.NumberOfClusters - 1]
    .getRelClusterSize()) : nf3.format(TextUnitClusters[this.NumberOfClusters 
    - 1].getRelClusterSize())) + "</td></tr>");
    indexHtmlFile.setNextLine("</table>");
        
    indexHtmlFile.setNextLine(
    "<a name=\"QUALITY\"><h3>Clustering Quality Assessment</h3>");
    indexHtmlFile.setNextLine("<p><small>"
    + "<a href=\"#SETTINGS\">Parameter Settings</a> - "
    + "<a href=\"#ACCEPTABLE\">Acceptable Clusters</a> - "
    + "<a href=\"#UNACCEPTABLE\">Unacceptable Clusters</a> - "
    + "<a href=\"#GARBAGE\">Garbage Cluster</a>"
    + "</small></p>");
    indexHtmlFile.setNextLine(
    "<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Clustering Quality Criterion</th>"
    + "<th align=\"right\" valign=\"top\">Value</th>"
    + "<th align=\"right\" valign=\"top\">Range</th>"
    + "<th align=\"right\" valign=\"top\">Objective</th></tr>");
    indexHtmlFile.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Clustering Quality Index</td>"
    + "<td align=\"right\" valign=\"top\">" + nf3.format(this.QualityIndex)
    + "<td align=\"right\" valign=\"top\">[0; 1]</td>"
    + "<td align=\"right\" valign=\"top\">Maximize!</td>"
    + "</tr></table>");

    indexHtmlFile.setNextLine(htmlFooter.insertValues());
    indexHtmlFile.close();
    
    this.closeClusterQualityIndicesFile();
    
    MyClusterDescriptions.save(ClusterLabelFileName);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {
    
    ID = 0L;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void fillThesauri() {
    
    // create descriptor thesaurus
    MyTerm = DescriptorThesaurus.getFirstTerm();
    NumberOfDescriptorsInControlledVocabulary = 0;
    while (MyTerm != null) {
      if (MyTerm.isDescriptor() && this.isDescriptor(MyTerm
      .getWord(), DescriptorThesaurus)) {
        // this descriptor is valid in this iteration
        NumberOfDescriptorsInControlledVocabulary++;
        MyDescriptors.countOccurrence(MyTerm.getWord());
        MyMappingHashtable.put(MyTerm.getWord(),
        new Integer(NumberOfDescriptorsInControlledVocabulary - 1));
      }
      else if (MyTerm.isDescriptor()) {
        // this descriptor is not valid in this iteration
        DescriptorThesaurus.delete(MyTerm.getWord());
      }
      MyTerm = DescriptorThesaurus.getNextTerm();
    }
    MyTerm = MyDescriptors.getFirstTerm();
    MyMappingArray = new String[NumberOfDescriptorsInControlledVocabulary];
    while (MyTerm != null) {
      MyMappingArray[((Integer)MyMappingHashtable.get(MyTerm.getWord()))
      .intValue() ] = MyTerm.getWord() + "";
      MyTerm = MyDescriptors.getNextTerm();
    }
    MyTerm = MyDescriptors.getFirstTerm();
    MyHtmlMappingArray = new String[NumberOfDescriptorsInControlledVocabulary];
    while (MyTerm != null) {
      MyHtmlMappingArray[((Integer)MyMappingHashtable.get(MyTerm.getWord()))
      .intValue()] = Tools.insertISO88591EntityReferences(MyTerm.getWord())
      + "";
      MyTerm = MyDescriptors.getNextTerm();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createEmptyFiles() {
    
    // create cluster content files and thesaurus containing cluster IDs
    // (+1) in order to visualize sentences not associated with any cluster
    // at all
    
    Template htmlHeader = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_HeaderTemplate.html"));
    htmlHeader.addValue("${Title}", "Disambiguate Word Senses");
    
    MyDescriptorSummaryFile = new TextFile(new File(
    this.ClusterDirectory + File.separator + "descriptorSummary.html"));
    MyDescriptorSummaryFile.open();
    htmlHeader.addValue("${Title}", "Descriptor Summary");
    MyDescriptorSummaryFile.setFirstLine(htmlHeader.insertValues());
    MyDescriptorSummaryFile.setNextLine(
    "<p>Created by Tasks &gt; Postprocess Patterns "
    + "&gt; Monitor Cluster Quality 2.2 on " + Tools.getSystemDate() +"</p>");
    MyDescriptorSummaryFile.setNextLine(
    "<p>Related Page: <a href=\"index.html\">Cluster Index</a></p>");
    MyClusterSummaryFile = new TextFile(new File(
    this.ClusterDirectory + File.separator + "clusterSummary.html"));
    MyClusterSummaryFile.open();
    htmlHeader.reset();
    htmlHeader.addValue("${Title}", "Cluster Summary");
    MyClusterSummaryFile.setFirstLine(htmlHeader.insertValues());
    MyClusterSummaryFile.setNextLine(
    "<p>Created by Tasks &gt; Postprocess Patterns "
    + "&gt; Monitor Cluster Quality 2.2 on " + Tools.getSystemDate() +"</p>");
    MyClusterSummaryFile.setNextLine("<p><a href=\"#BOP"
    + "\">Bottom of the Page</a> <a href=\"index.html\">Cluster Index</a></p>");
    MyClusterFiles = new TextFile[this.NumberOfClusters];
    for (int i = 0; i < this.NumberOfClusters; i++) {
      MyClusterFiles[i] = new TextFile(new File(this.ClusterDirectory
      + File.separator + "cluster" + i + ".html"));
      MyClusterFiles[i].open();
      htmlHeader.reset();
      htmlHeader.addValue("${Title}", "Cluster " + i
      + (i == (this.NumberOfClusters - 1) ? " (Garbage Cluster)" : ""));
      MyClusterFiles[i].setFirstLine(htmlHeader.insertValues());
      MyClusterFiles[i].setNextLine(
      "<p>Created by Tasks &gt; Postprocess Patterns "
      + "&gt; Monitor Cluster Quality 2.2 on " + Tools.getSystemDate() +"</p>");
      MyClusterFiles[i].setNextLine(
      "<p>Related Pages: " + (i > 0 ? "<a href=\"cluster" + (i-1) + ".html"
      + "\">Previous Cluster</a> - " : "")
      + "<a href=\"index.html\">Cluster Index</a> "
      + (i < (this.NumberOfClusters - 1) ? " - <a href=\"cluster" + (i+1)
      + ".html" + "\">Next Cluster</a> " : "") + "</p>");
      MyClusterFiles[i].setNextLine("<a name=\"CONTENTS\">");
      MyClusterFiles[i].setNextLine("<h3>Cluster Contents</h3>");
      MyClusterFiles[i].setNextLine("<p><small>"
      + "<a href=\"#DESC_BY_SUPP\">Descriptors by Support</a> - "
      + "<a href=\"#DESC_BY_TERM\">Descriptors by Term</a> - "
      + "<a href=\"#NONDESC_BY_SUPP\">Non-Descriptors by Support</a> - "
      + "<a href=\"#QUALITY\">Cluster Quality Assessment</a>"
      + "</small></p>");
      MyClusterFiles[i].close();
    }
    
    NextIdOfClusterQualityIndices = 
    this.createOrOpenClusterQualityIndicesFile();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isDescriptor(String pDescriptor,
  DIAsDEMthesaurus pThesaurus) {
    
    if ((pThesaurus.get(pDescriptor) != null)
    && (VectorDimensions == MonitorClusterQualityParameter.ALL_DESCRIPTORS)
    || ((pThesaurus.get(pDescriptor).getScopeNotes()
    .indexOf(DescriptorsScopeNotesContain) >= 0)
    && VectorDimensions == MonitorClusterQualityParameter.SPECIFIED_DESCRIPTORS)
    || ((pThesaurus.get(pDescriptor).getScopeNotes()
    .indexOf(DescriptorsScopeNotesContain) < 0)
    && VectorDimensions == MonitorClusterQualityParameter
    .NOT_SPECIFIED_DESCRIPTORS)) {
      return true;
    }
    else {
      return false;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  protected int createOrOpenClusterQualityIndicesFile() {
    
    if (Tools.stringIsNullOrEmpty(ClusterResultFileName)) {
      return -1;
    }
    
    int nextID = 1;
    String fileName = Tools.removeFileExtension(ClusterResultFileName);
    if (fileName.indexOf('_') >= 0) {
      fileName = fileName.substring(0, fileName.indexOf('_')) 
      + ".quality.arff";
    }
    else {
      fileName += ".quality.arff";
    }    
    File file = new File(fileName);
    if (file.exists()) {
      ClusterQualityIndicesArffFile = new TextFile(file);
      ClusterQualityIndicesArffFile.open();
      String line = ClusterQualityIndicesArffFile.getFirstLine();
      while (line != null) {
        if (line.toLowerCase().trim().equals("@data")) {
          nextID = 0;
        }
        nextID++;
        line = ClusterQualityIndicesArffFile.getNextLine();
      }
      ClusterQualityIndicesArffFile.close();
      ClusterQualityIndicesArffFile.open();
    }
    else {
      ClusterQualityIndicesArffFile = new TextFile(file);
      ClusterQualityIndicesArffFile.open();
      ClusterQualityIndicesArffFile.setFirstLine(
      "@relation 'DIAsDEM Cluster Quality Monitor: Cluster Quality Indices'");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute ID integer");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute Parameter string");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute NumberOfClusters integer");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute NumberOfAcceptableClusters integer");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute ProportionAcceptableClusters real");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute NumberOfUnacceptableClusters integer");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute ProportionUnacceptableClusters real");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute NumberOfTextUnits integer");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute NumberOfTextUnitsInAcceptableClusters integer");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute TextUnitsInAcceptableClustersProportion real");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute NumberOfTextUnitsInUnacceptableClusters integer");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute TextUnitsInUnacceptableClustersProportion real");
      ClusterQualityIndicesArffFile.setNextLine(
      "@attribute ClusteringQualityIndex real");
      ClusterQualityIndicesArffFile.setNextLine(
      "@data");
    }
    
    return nextID;
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */

  protected void appendClusterQualityIndicesToFile(String pArffLine) {
    
    if (ClusterQualityIndicesArffFile != null) {
      ClusterQualityIndicesArffFile.setNextLine(pArffLine);
      NextIdOfClusterQualityIndices++;
    }   
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */

  protected void closeClusterQualityIndicesFile() {
    
    if (ClusterQualityIndicesArffFile != null) {
      ClusterQualityIndicesArffFile.close();
    }   
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}