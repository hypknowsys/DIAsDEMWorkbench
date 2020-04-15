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

package org.hypknowsys.diasdem.tasks.postprocess.monitorClusterQuality;

import java.io.*;
import java.util.*;
import java.text.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.diasdem.tasks.prepare.vectorizeTextUnits.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.core.neex.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class TextUnitClusterSet implements Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private long ID = 0L;
  
  private int NumberOfClusters = 0;
  private TextUnitCluster[] TextUnitClusters = null;
  private String ClusterDirectory = null;
  protected int VectorDimensions = 0;
  protected String DescriptorsScopeNotesContain = null;
  
  protected int MinClusterCardinality = 0;
  protected double MaxDistinctDescriptorsRatio = 0.0;
  protected double MinFrequentDescriptorsRatio = 0.0;
  
  private DIAsDEMthesaurus DescriptorThesaurus = null;  // contains entire thesaurus
  private DIAsDEMthesaurusTerm term = null;
  private DIAsDEMthesaurus descriptors = null;  // only contains descriptors
  private int descriptorCounter = 0;
  private Hashtable mappingHashtable = null;
  private String[] mappingArray = null;
  private String[] htmlMappingArray = null;
  private DIAsDEMthesaurus clusterDescriptions = null;  // for semantic cluster labels
  private String ClusterLabelFileName = null;
  private TextFile[] aClusterFiles = null;
  private TextFile clusterSummaryFile = null;
  private TextFile descriptorSummaryFile = null;
  private boolean IgnoreEmptyClusters = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static double LOW_BOUNDARY = 0.6;
  private final static double MEDIUM_BOUNDARY = 0.8;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public TextUnitClusterSet(String pThesaurusFileName, int pNumberOfClusters,
    String pClusterDirectory, 
    int pMinClusterCardinality, double pMaxDistinctDescriptorsRatio,
    double pMinFrequentDescriptorsRatio,
    int pVectorDimensions, String pDescriptorsScopeNotesContain,
    String pClusterLabelFileName, boolean pIgnoreEmptyClusters) {
    
    this.ID = 0L;
    this.NumberOfClusters = pNumberOfClusters + 2;  // NULL cluster and ID shift
    this.ClusterDirectory = pClusterDirectory;
    this.MinClusterCardinality = pMinClusterCardinality;
    this.MaxDistinctDescriptorsRatio = pMaxDistinctDescriptorsRatio;
    this.MinFrequentDescriptorsRatio = pMinFrequentDescriptorsRatio;
    VectorDimensions = pVectorDimensions;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    ClusterLabelFileName = pClusterLabelFileName;
    IgnoreEmptyClusters = pIgnoreEmptyClusters;
    
    DescriptorThesaurus = new DefaultDIAsDEMthesaurus();
    DescriptorThesaurus.load(pThesaurusFileName);
    descriptors = new DefaultDIAsDEMthesaurus();
    descriptorCounter = 0;
    mappingHashtable = new Hashtable();
    mappingArray = null;
    clusterDescriptions = new DefaultDIAsDEMthesaurus(
    "ClusterID = Term; ClusterTag = ScopeNotes", 1);
    
    this.fillThesauri();
    this.TextUnitClusters = new TextUnitCluster[this.NumberOfClusters];
    for (int i = 0; i < TextUnitClusters.length; i++)
      TextUnitClusters[i] = new TextUnitCluster(i, descriptorCounter);
    this.createEmptyFiles();
    
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
    TmpStringBuffer.append( Itemizer.longToItem(ID) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    
    return TmpStringBuffer.toString();
    
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) {
    
    ID = 0L;
    Itemizer itemizer = new Itemizer(pItemLine);
    
    try {
      ID = itemizer.itemToLong( itemizer.getNextItem() );
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
    DIAsDEMthesaurus oCurrentSentence = new DefaultDIAsDEMthesaurus();
    DIAsDEMthesaurusTerm term = null;
    String word = null;
    
    tokenizer = new StringTokenizer(pTokenizedLine);
    while ( tokenizer.hasMoreElements() ) {
      word = tokenizer.nextToken();
      if (NamedEntity.isPlaceholder(word)) {
        word = (pCurrentDiasdemDocument.getActiveTextUnitsLayer()
        .getNamedEntity(NamedEntity.getNamedEntityIndex(word)))
        .getPossibleTypesPlaceholder();
      }
      term = pDescriptors.getDescriptorTerm(word);
      if (term != null) {
        oCurrentSentence.countOccurrence(term.getWord());
      }
    }
    
    for (int i = 0; i < oCurrentVector.length; i++) oCurrentVector[i] = "0";
    term = oCurrentSentence.getFirstTerm();
    while (term != null) {
      oCurrentVector[ ( (Integer)pMappingHashtable.get( term.getWord() ) )
      .intValue() ] = term.getOccurrences() + "";
      term = oCurrentSentence.getNextTerm();
    }
    
    String result = "";
    int vOutputCounter = 0;
    for (int i = 0; i < oCurrentVector.length; i++)
      if ( ! oCurrentVector[i].trim().equals("0") ) {
        if (vOutputCounter > 0) result += "; ";
        result += pHtmlMappingHeader[i].trim() + "=" + oCurrentVector[i].trim();
        vOutputCounter++;
        TextUnitClusters[pClusterID].countDescriptorOccurrence(
          pMappingHeader[i].trim() );
      }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void addTextUnit(int NewClusterID, String lineZerlegt,
  String lineGrundformen, String CurrentXmlFileName,
  int NewSentenceID, DIAsDEMdocument pCurrentDiasdemDocument,
  String pDumpDirectory, String pHtmlBaseDirectory, boolean pDumpDocuments) {
    
    aClusterFiles[NewClusterID].open();
    TmpStringBuffer = new StringBuffer(5000);
    TmpStringBuffer.append(lineZerlegt);
    TmpStringBuffer.append("<br><font color=\"red\">Descriptors: [");
    TmpStringBuffer.append(this.getAndCountDescriptors(
    lineGrundformen, DescriptorThesaurus, mappingHashtable, mappingArray, 
    descriptorCounter, NewClusterID, pCurrentDiasdemDocument,
    htmlMappingArray));
    TmpStringBuffer.append("]</font>");
    TmpStringBuffer.append("<br>Document: ");
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
    TmpStringBuffer.append(NewSentenceID);
    TmpStringBuffer.append("<p>");
    aClusterFiles[NewClusterID].setNextLine(TmpStringBuffer.toString());
    aClusterFiles[NewClusterID].close();   
    this.TextUnitClusters[NewClusterID].incrementClusterCardinality();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void assessClusterQuality() {
  
    DIAsDEMthesaurusTerm currentTerm = null;
    descriptorSummaryFile.setNextLine(
    "<table cellpadding=\"0\" cellspacing=\"0\"><tr><td>Descriptor</td>");
    for (int i = 0; i < this.NumberOfClusters; i++)
      TextUnitClusters[i].computeDescriptorFrequencies(aClusterFiles[i],
        clusterSummaryFile, this.NumberOfClusters, mappingHashtable);
    
    // complete cluster summary; rows = descriptors;
    // columns = relative frequencies
    descriptorSummaryFile.setNextLine("<tr>");
    String summaryLine = null;
    for (int i = 0; i < descriptorCounter; i++) {
      currentTerm = DescriptorThesaurus.get( mappingArray[i] );
      // only valid descriptors
      if ( (currentTerm != null) && ( this.isDescriptor(
      currentTerm.getWord(), DescriptorThesaurus) ) ) {
        summaryLine = "<tr> <td>" + mappingArray[i] + "</td>";
        for (int j = 0; j < this.NumberOfClusters; j++)
          summaryLine += TextUnitClusters[j].extentDescriptorSummaryLine(i);
        summaryLine += "</tr>";
        descriptorSummaryFile.setNextLine(summaryLine);
      }
    }
    descriptorSummaryFile.setNextLine("</table> <hr> ");
    descriptorSummaryFile.setNextLine("<a href=\"#TOP" +
    "\">Top of the Page</a> <a href=\"index.html\">Cluster Index</a> ");
    descriptorSummaryFile.setNextLine("<a name=\"BOP\"> </body></html>");
    descriptorSummaryFile.close();
    clusterSummaryFile.setNextLine("<a href=\"#TOP" +
    "\">Top of the Page</a> <a href=\"index.html\">Cluster Index</a> ");
    clusterSummaryFile.setNextLine("<a name=\"BOP\"> </body></html>");
    clusterSummaryFile.close();
    
    NumberFormat nf = NumberFormat.getInstance(Locale.US);
    nf.setMinimumFractionDigits(2);
    nf.setMaximumFractionDigits(2);
    TextFile indexHtmlFile = new TextFile( new File(
      this.ClusterDirectory + File.separator + "index.html" ) );
    indexHtmlFile.open();
    indexHtmlFile.setFirstLine("<html><head><title>DIAsDEM Cluster Quality Monitor</title></head><body bgcolor=\"white\"><a name=\"TOP\"><h1>DIAsDEM Cluster Quality Monitor</h1>");
  indexHtmlFile.setNextLine("<p> <a href=\"#ACCEPTABLE" + "\">Acceptable Clusters</a> <a href=\"#UNACCEPTABLE" + "\">Unacceptable Clusters</a> <a href=\"#BOP" + "\">Bottom of the Page</a> &nbsp; | &nbsp; <a href=\"ClusterSummary.html\" target=\"_new\">Cluster Summary</a> <a href=\"descriptorSummary.html\" target=\"_new\">Descriptor Summary</a> <p> <hr>");
    indexHtmlFile.setNextLine("<a name=\"ACCEPTABLE\"><h2>Qualitatively Acceptable Clusters</h2>");
    indexHtmlFile.setNextLine("<p> <a href=\"#TOP" + "\">Top of the Page</a> <a href=\"#ACCEPTABLE" + "\">Acceptable Clusters</a> <a href=\"#UNACCEPTABLE" + "\">Unacceptable Clusters</a> <a href=\"#BOP" + "\">Bottom of the Page</a> </p>");
    indexHtmlFile.setNextLine("<p>Min. Cluster Cardinality = " +
      this.MinClusterCardinality + "<br>");
    indexHtmlFile.setNextLine("Max. Distinct Descriptors Ratio = " +
      this.MaxDistinctDescriptorsRatio + "<br>");
    indexHtmlFile.setNextLine("Min. Frequent Descriptors Ratio = " +
      this.MinFrequentDescriptorsRatio + "</p>");
    indexHtmlFile.setNextLine("<table align=\"center\" width=\"100%\">");
    for (int i = 0; i < TextUnitClusters.length; i++) {
      if ( TextUnitClusters[i].isAcceptable(
      this.MinClusterCardinality, this.MaxDistinctDescriptorsRatio,
      this.MinFrequentDescriptorsRatio) && ( TextUnitClusters[i]
      .getClusterCardinality() > 0 || ! IgnoreEmptyClusters ) ) {
        indexHtmlFile.setNextLine("<tr><td><a href=\"cluster" +
          i + ".html" + "\" target=\"_new\">" + "Cluster_" + i + "</a></td>");
        indexHtmlFile.setNextLine("<td>Cardinality = " +
          TextUnitClusters[i].getClusterCardinality() + "</td>");
        indexHtmlFile.setNextLine("<td>Distinct Descriptors Ratio = " +
          nf.format( TextUnitClusters[i].getDistinctDescriptorsRatio() ) + 
          "</td>");
        indexHtmlFile.setNextLine("<td>Frequent Descriptors Ratio = " +
          nf.format( TextUnitClusters[i].getFrequentDescriptorsRatio() ) + 
          "</td></tr>");
      }
      if ( TextUnitClusters[i].isAcceptable( this.MinClusterCardinality, 
      this.MaxDistinctDescriptorsRatio, this.MinFrequentDescriptorsRatio) )
        clusterDescriptions.add(
        TextUnitClusters[i].getClusterDescriptionTerm() );
    }
    indexHtmlFile.setNextLine("</table>");
    indexHtmlFile.setNextLine("<p> <hr> <a name=\"UNACCEPTABLE\"><h2>Qualitatively Unacceptable Clusters</h2>");
    indexHtmlFile.setNextLine("<p> <a href=\"#TOP" + "\">Top of the Page</a> <a href=\"#ACCEPTABLE" + "\">Acceptable Clusters</a> <a href=\"#UNACCEPTABLE" + "\">Unacceptable Clusters</a> <a href=\"#BOP" + "\">Bottom of the Page</a> </p>");
    indexHtmlFile.setNextLine("<table align=\"center\" width=\"100%\">");
    for (int i = 0; i < TextUnitClusters.length; i++) {
      if ( ! TextUnitClusters[i].isAcceptable(
      this.MinClusterCardinality, this.MaxDistinctDescriptorsRatio,
      this.MinFrequentDescriptorsRatio)  && ( TextUnitClusters[i]
      .getClusterCardinality() > 0 || ! IgnoreEmptyClusters ) ) {
        indexHtmlFile.setNextLine("<tr><td><a href=\"cluster" +
          i + ".html" + "\" target=\"_new\">" + "Cluster_" + i + "</a></td>");
        indexHtmlFile.setNextLine("<td>Cardinality = " +
          TextUnitClusters[i].getClusterCardinality() + "</td>");
        indexHtmlFile.setNextLine("<td>Distinct Descriptors Ratio = " +
          nf.format( TextUnitClusters[i].getDistinctDescriptorsRatio() ) + 
          "</td>");
        indexHtmlFile.setNextLine("<td>Frequent Descriptors Ratio = " +
          nf.format( TextUnitClusters[i].getFrequentDescriptorsRatio() ) + 
          "</td></tr>");
      }
      if ( ! TextUnitClusters[i].isAcceptable( this.MinClusterCardinality, 
      this.MaxDistinctDescriptorsRatio, this.MinFrequentDescriptorsRatio) )      
        clusterDescriptions.add( 
          TextUnitClusters[i].getClusterDescriptionTerm() );
    }
    indexHtmlFile.setNextLine("</table>");
    indexHtmlFile.setNextLine("<p> <hr> <p> <a href=\"#TOP" + "\">Top of the Page</a> <a href=\"#ACCEPTABLE" + "\">Acceptable Clusters</a> <a href=\"#UNACCEPTABLE" + "\">Unacceptable Clusters</a> &nbsp; | &nbsp; <a href=\"ClusterSummary.html\" target=\"_new\">Cluster Summary</a> <a href=\"descriptorSummary.html\" target=\"_new\">Descriptor Summary</a> </p>");
    indexHtmlFile.setNextLine("<a name=\"BOP\"> </body></html>");
    indexHtmlFile.close();   
    
    clusterDescriptions.save(ClusterLabelFileName);
    
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
    term = DescriptorThesaurus.getFirstTerm();
    descriptorCounter = 0;
    while (term != null) {
      if (term.isDescriptor() && this.isDescriptor(term
      .getWord(), DescriptorThesaurus)) {
        // this descriptor is valid in this iteration
        descriptorCounter++;
        descriptors.countOccurrence(term.getWord());
        mappingHashtable.put(term.getWord(),
        new Integer(descriptorCounter - 1));
      }
      else if (term.isDescriptor()) {
        // this descriptor is not valid in this iteration
        DescriptorThesaurus.delete(term.getWord());
      }
      term = DescriptorThesaurus.getNextTerm();
    }
    term = descriptors.getFirstTerm();
    mappingArray = new String[descriptorCounter];
    while (term != null) {
      mappingArray[ ( (Integer)mappingHashtable.get( term.getWord() ) )
      .intValue() ] = term.getWord() + "";
      term = descriptors.getNextTerm();
    }
    term = descriptors.getFirstTerm();
    htmlMappingArray = new String[descriptorCounter];
    while (term != null) {
      htmlMappingArray[ ( (Integer)mappingHashtable.get( term.getWord() ) )
      .intValue() ] = Tools.insertISO88591EntityReferences(term.getWord()) + "";
      term = descriptors.getNextTerm();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createEmptyFiles() {
    
    // create cluster content files and thesaurus containing cluster IDs
    // (+1) in order to visualize sentences not associated with any cluster
    // at all
    descriptorSummaryFile = new TextFile( new File(
      this.ClusterDirectory + File.separator + "descriptorSummary.html" ) );
    descriptorSummaryFile.open();
    descriptorSummaryFile.setFirstLine("<html><head><title>Descriptor Summary</title></head><body bgcolor=\"white\"><a name=\"TOP\"><h1>Descriptor Summary</h1>");
    descriptorSummaryFile.setNextLine("<a href=\"#BOP" + "\">Bottom of the Page</a> <a href=\"index.html\">Cluster Index</a> <hr>");
    clusterSummaryFile = new TextFile( new File(
    this.ClusterDirectory + File.separator + "ClusterSummary.html" ) );
    clusterSummaryFile.open();
    clusterSummaryFile.setFirstLine("<html><head><title>Cluster Summary</title></head><body bgcolor=\"white\"><a name=\"TOP\"><h1>Cluster Summary</h1>");
    clusterSummaryFile.setNextLine("<a href=\"#BOP" + "\">Bottom of the Page</a> <a href=\"index.html\">Cluster Index</a> <hr>");
    aClusterFiles = new TextFile[this.NumberOfClusters];
    for (int i = 0; i < this.NumberOfClusters; i++) {
      aClusterFiles[i] = new TextFile( new File( this.ClusterDirectory +
      File.separator + "cluster" + i + ".html" ) );
      aClusterFiles[i].open();
      aClusterFiles[i].setFirstLine("<html><head><title>Cluster ID = " + i +
      "</title></head><body bgcolor=\"white\"><a name=\"TOP\"><h1>Cluster ID = " + i +
      ( i == 0 ? " (NULL Cluster)" : "") + "</h1>");
      aClusterFiles[i].setNextLine(
        "<a href=\"#BOP\">Bottom of the Page</a> " +
        "<a href=\"#FREQ\">Descriptor Frequencies</a> &nbsp; | &nbsp; " +
        ( i > 0 ? "<a href=\"cluster" + (i-1) + ".html" + "\">Previous Cluster</a> " : "") +
        "<a href=\"index.html\">Cluster Index</a> " +
        ( i < (this.NumberOfClusters - 1) ? "<a href=\"cluster" + (i+1) + ".html" + "\">Next Cluster</a> " : "") +
        "<p>");
      aClusterFiles[i].setNextLine("<hr><h2>Cluster Contents</h2>");
      aClusterFiles[i].close();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  private boolean isDescriptor(String pDescriptor, DIAsDEMthesaurus pThesaurus) {
   
    if ( (pThesaurus.get(pDescriptor) != null) &&
    (VectorDimensions == VectorizeTextUnitsParameter
    .ALL_DESCRIPTORS ) 
    || ((pThesaurus.get(pDescriptor).getScopeNotes()
    .indexOf(DescriptorsScopeNotesContain) >= 0) &&
    VectorDimensions == VectorizeTextUnitsParameter
    .SPECIFIED_DESCRIPTORS) 
    || ( (pThesaurus.get(pDescriptor).getScopeNotes()
    .indexOf(DescriptorsScopeNotesContain) < 0) &&
    VectorDimensions == VectorizeTextUnitsParameter
    .NOT_SPECIFIED_DESCRIPTORS) )
      return true;
    else
      return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}