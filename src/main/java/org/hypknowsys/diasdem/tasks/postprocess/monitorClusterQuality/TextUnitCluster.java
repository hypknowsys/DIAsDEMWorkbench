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
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class TextUnitCluster implements Serializable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private int ID = 0;
  private int NumberOfDescriptors = 0;
  private int NumberOfFrequentDescriptors = 0;
  private int ClusterCardinality = 0;
  private double DistinctDescriptorsRatio = 0.0d;
  private double FrequentDescriptorsRatio = 0.0d;
  private TreeSet FrequentDescriptors = null;
  private String DefaultClusterLabels = null;

  private DIAsDEMthesaurus ClusterDescriptorCounter = null;
  private double[] ClusterSummary = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public TextUnitCluster(int pID, int pNumberOfDescriptors) {

    this.ID = pID;
    this.NumberOfDescriptors = pNumberOfDescriptors;
    this.NumberOfFrequentDescriptors = 0;
    this.ClusterCardinality = 0;

    this.ClusterDescriptorCounter = new DefaultDIAsDEMthesaurus();
    this.ClusterSummary = new double[this.NumberOfDescriptors];
    for (int i = 0; i < this.ClusterSummary.length; i++)
      this.ClusterSummary[i] = 0.0d;
    
    FrequentDescriptors = new TreeSet();
    DefaultClusterLabels = "-";
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public int getID() { 
    return ID; }
  public int getClusterCardinality() {
    return ClusterCardinality; }
  public int getNumberOfDescriptors() {
    return NumberOfDescriptors; }
  public int getNumberOfFrequentDescriptors() {
    return NumberOfFrequentDescriptors; }
  public double getDistinctDescriptorsRatio() {
    return DistinctDescriptorsRatio; }
  public double getFrequentDescriptorsRatio() {
    return FrequentDescriptorsRatio; }
  public java.lang.String getDefaultClusterLabels() {
    return DefaultClusterLabels; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setClusterCardinality(int ClusterCardinality) {
    this.ClusterCardinality = ClusterCardinality; }  
  public void setNumberOfDescriptors(int NumberOfDescriptors) {
    this.NumberOfDescriptors = NumberOfDescriptors; }

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
    TmpStringBuffer.append( Itemizer.intToItem(ID) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);

    return TmpStringBuffer.toString();

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) { 

    ID = 0;
    Itemizer itemizer = new Itemizer(pItemLine);

    try {
      ID = itemizer.itemToInt( itemizer.getNextItem() );
    }
    catch (NoSuchElementException e1) { this.reset(); }
    catch (NumberFormatException e2) { this.reset(); }
    catch (StringIndexOutOfBoundsException e2) { this.reset(); }

  } 
  
  /* ########## ########## ########## ########## ########## ######### */

  public void incrementClusterCardinality() {
    
    this.ClusterCardinality++;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  public void countDescriptorOccurrence(String pDescriptor) {
    
    this.ClusterDescriptorCounter.countOccurrence(pDescriptor);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void computeDescriptorFrequencies(TextFile pClusterFile,
    TextFile pClusterSummaryFile, int pNumberOfClusters,
    Hashtable pMappingHashtable) {
    
      pClusterFile.open();
      pClusterFile.setNextLine("<hr>");
      ClusterDescriptorCounter.setOrderWordsAsc();
      DIAsDEMthesaurusTerm currentTerm = ClusterDescriptorCounter.getFirstTerm();
      pClusterFile.setNextLine("<a name=\"FREQ\">");
      pClusterFile.setNextLine("<h2>Descriptor Frequencies, Cluster Size = " + 
        this.ClusterCardinality + "</h2>");
      pClusterFile.setNextLine("<table align=\"center\" width=\"100%\"><tr><td valign=\"top\" align=\"left\" width=\"50%\"><b>Descriptor</b></td><td valign=\"top\" align=\"left\" width=\"25%\"><b>Absolute Frequency:</b></td> <td valign=\"top\" align=\"left\" width=\"25%\"><b>Relative Frequency</b></td></tr>");
      pClusterSummaryFile.setNextLine("<hr><h2>Cluster ID = " + this.ID + ", Descriptor Frequencies, Cluster Size = " + this.ClusterCardinality + "</h2>");
      pClusterSummaryFile.setNextLine("<table align=\"center\" width=\"100%\"><tr><td valign=\"top\" align=\"left\" width=\"50%\"><b>Descriptor</b></td><td valign=\"top\" align=\"left\" width=\"25%\"><b>Absolute Frequency:</b></td> <td valign=\"top\" align=\"left\" width=\"25%\"><b>Relative Frequency</b></td></tr>");
      this.NumberOfFrequentDescriptors = 0;
      while (currentTerm != null) {
        double relFrequency =
          (currentTerm.getOccurrences() / (double)this.ClusterCardinality );
        ClusterSummary[ ( (Integer)pMappingHashtable.get(
          currentTerm.getWord() ) ).intValue() ] = relFrequency;
        if (relFrequency > 0.8) {
           this.NumberOfFrequentDescriptors++;
           FrequentDescriptors.add( currentTerm.getWord() );
        }
        if (relFrequency > 0.95) {
          pClusterFile.setNextLine(
            "<td valign=\"top\" align=\"left\" bgcolor=\"red\">" +
            Tools.insertISO88591EntityReferences( currentTerm.getWord() ) +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"red\">" +
            currentTerm.getOccurrences() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"red\">" +
            relFrequency + " </td> </tr>");
          pClusterSummaryFile.setNextLine(
            "<td valign=\"top\" align=\"left\" bgcolor=\"red\">" +
            currentTerm.getWord() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"red\">" +
            currentTerm.getOccurrences() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"red\">" +
            relFrequency + " </td> </tr>");
        }
        else if ( (relFrequency <= 0.95) && (relFrequency > 0.8) ) {
          pClusterFile.setNextLine(
            "<td valign=\"top\" align=\"left\" bgcolor=\"orange\">" +
            Tools.insertISO88591EntityReferences( currentTerm.getWord() ) +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"orange\">" +
            currentTerm.getOccurrences() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"orange\">" +
            relFrequency + " </td> </tr>");
          pClusterSummaryFile.setNextLine(
            "<td valign=\"top\" align=\"left\" bgcolor=\"orange\">" +
            currentTerm.getWord() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"orange\">" +
            currentTerm.getOccurrences() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"orange\">" +
            relFrequency + " </td> </tr>");
        }
        else if ( (relFrequency <= 0.8) && (relFrequency > 0.6) ) {
          pClusterFile.setNextLine(
            "<td valign=\"top\" align=\"left\" bgcolor=\"yellow\">" +
            Tools.insertISO88591EntityReferences( currentTerm.getWord() ) +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"yellow\">" +
            currentTerm.getOccurrences() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"yellow\">" +
            relFrequency + " </td> </tr>");
          pClusterSummaryFile.setNextLine(
            "<td valign=\"top\" align=\"left\" bgcolor=\"yellow\">" +
            currentTerm.getWord() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"yellow\">" +
            currentTerm.getOccurrences() +
            "</td> <td valign=\"top\" align=\"left\" bgcolor=\"yellow\">" +
            relFrequency + " </td> </tr>");
        }
        else {
          pClusterFile.setNextLine(
            "<td valign=\"top\" align=\"left\">" + 
            Tools.insertISO88591EntityReferences( currentTerm.getWord() ) +
            "</td> <td valign=\"top\" align=\"left\">" +
            currentTerm.getOccurrences() +
            "</td> <td valign=\"top\" align=\"left\">" +  relFrequency +
            " </td> </tr>");          
          pClusterSummaryFile.setNextLine(
            "<td valign=\"top\" align=\"left\">" + currentTerm.getWord() +
            "</td> <td valign=\"top\" align=\"left\">" +
            currentTerm.getOccurrences() +
            "</td> <td valign=\"top\" align=\"left\">" +
            relFrequency + " </td> </tr>");
        }
        currentTerm = ClusterDescriptorCounter.getNextTerm();
      }
      pClusterFile.setNextLine("</table> <hr>");
      pClusterSummaryFile.setNextLine("</table> <hr>");
      pClusterFile.setNextLine(
        "<a href=\"#TOP\">Top of the Page</a> " +
        "<a href=\"#FREQ\">Descriptor Frequencies</a> &nbsp; | &nbsp; " +
        ( this.ID > 0 ? "<a href=\"cluster" + (this.ID-1) + ".html" + "\">Previous Cluster</a> " : "") +
        "<a href=\"index.html\">Cluster Index</a> " +
        ( this.ID < (pNumberOfClusters - 1) ? "<a href=\"cluster" + (this.ID+1) + ".html" + "\">Next Cluster</a> " : "") +
        "<p>  <a name=\"BOP\"></a> </body></html><p>");
      pClusterFile.close();    
      
      DefaultClusterLabels = "";
      Iterator iterator = FrequentDescriptors.iterator();
      boolean isFirstLabel = true;
      while ( iterator.hasNext() ) {
        if (isFirstLabel)
          isFirstLabel = false;
        else
          DefaultClusterLabels += "_";
        DefaultClusterLabels += (String)( iterator.next() );
      }
      DefaultClusterLabels = Tools.createAsciiAttributeName(
      DefaultClusterLabels, 200, "DEFAULT_", ""); 
    
  } 
  
  /* ########## ########## ########## ########## ########## ######### */

  public String extentDescriptorSummaryLine(int pDescriptorID) {
    
    if (ClusterSummary[pDescriptorID] >= 0.9)
      return "<td bgcolor=\"red\"> <a href=\"cluster" + this.ID + 
        ".html#BOP" + "\" target=\"_new\">" + "#" + "</a>" + " </td>";
    else if (ClusterSummary[pDescriptorID] < 0.9 &&
    ClusterSummary[pDescriptorID] >= 0.75)
      return "<td bgcolor=\"orange\"> <a href=\"cluster" + this.ID + 
          ".html#BOP" + "\" target=\"_new\">" + "+" + "</a>" + " </td>";
    else if (ClusterSummary[pDescriptorID] < 0.75 &&
    ClusterSummary[pDescriptorID] >= 0.5)
      return "<td bgcolor=\"yellow\"> <a href=\"cluster" + this.ID + 
          ".html#BOP" + "\" target=\"_new\">" + "=" + "</a>" + " </td>";
    else
      return "<td> <a href=\"cluster" + this.ID + 
        ".html#BOP" + "\" target=\"_new\">" + "-" + "</a>" + " </td>";
    
  } 
      
  /* ########## ########## ########## ########## ########## ######### */

  public boolean isAcceptable(int pMinClusterCardinality, 
    double pMaxDistinctDescriptorsRatio, 
    double pMinFrequentDescriptorsRatio) {
    
    if (this.ClusterDescriptorCounter.getSize() == 0)
      this.FrequentDescriptorsRatio = 0.0d;
    else
      this.FrequentDescriptorsRatio = this.NumberOfFrequentDescriptors / 
        (double)this.ClusterDescriptorCounter.getSize();
    
    if (this.NumberOfDescriptors == 0)
      this.DistinctDescriptorsRatio = 0.0d;
    else
      this.DistinctDescriptorsRatio = this.ClusterDescriptorCounter.getSize() / 
        (double)this.NumberOfDescriptors;
    
    if (this.ClusterCardinality >= pMinClusterCardinality &&
    this.DistinctDescriptorsRatio <= pMaxDistinctDescriptorsRatio &&
    this.FrequentDescriptorsRatio >= pMinFrequentDescriptorsRatio)    
      return true;
    else {
      DefaultClusterLabels = "-";
      return false;
    }
    
  }
 
  /* ########## ########## ########## ########## ########## ######### */

  public DIAsDEMthesaurusTerm getClusterDescriptionTerm() {
    
    DIAsDEMthesaurusTerm term = new DefaultDIAsDEMthesaurusTerm ( (long)(this.ID), 
    String.valueOf(this.ID), 1);
    term.setScopeNotes(this.DefaultClusterLabels);
    
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

  public static void main(String args[]) {}
  
}