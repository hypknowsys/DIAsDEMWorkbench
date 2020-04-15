/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.core.default10;

import java.io.*;
import java.util.*;

/**
 * 
 */

public class MintPatternOutput implements Serializable {

  // ########## attributes ##########
  
  private int PatternID = 0;
  private int PatternType = UNKNOWN;
  // number of variables, length of corresponding trail
  private int Length = 0;  

  private String[] Variables = null;
  private long[] PageIDs = null;
  private String[] PageUrls = null;
  private String[] PageTitles = null;
  private int[] OccurrenceValues = null;
  private long[] SupportValues = null;
  private double[] RelativeSupportValues = null;
  private int[] AccessValues = null;
  private double[] ConfidenceValues = null; 
  private double[] RootConfidenceValues = null; 

  private long CurrentConfidenceBase = 0l;
  private long CurrentRootConfidenceBase = 0l;

  public static final int UNKNOWN = 0;
  public static final int COMPLETE_PATTERN = 1;
  public static final int PARTIAL_PATTERN = 2;

  // ########## constructors ##########
  
  /**
   * pMintPatternDescriptor must contain valid result
   */  

  public MintPatternOutput(MintQuery pMintQuery, int pPatternID,
    int pPatternType, MintPatternDescriptor pMintPatternDescriptor,
    MiningBase pMiningBase) {
  
    PatternID = pPatternID;
    switch (pPatternType) {
      case COMPLETE_PATTERN: { PatternType = COMPLETE_PATTERN; break; }
      case PARTIAL_PATTERN: { PatternType = PARTIAL_PATTERN; break; }
      default: { PatternType = UNKNOWN; }
    }
    Length = pMintPatternDescriptor.getLength();
 
    Vector variables = pMintQuery.getAllVariableNames();
    Variables = new String[Length];
    PageIDs = new long[Length];
    PageUrls = new String[Length];
    PageTitles = new String[Length];
    OccurrenceValues = new int[Length];
    SupportValues = new long[Length];
    RelativeSupportValues = new double[Length];
    AccessValues = new int[Length];
    ConfidenceValues = new double[Length]; 
    RootConfidenceValues = new double[Length]; 

    for (int i = 0; i < Length; i++) {
      Variables[i] = (String)variables.elementAt(i);
      PageIDs[i] = pMintPatternDescriptor.getPattern().
        getPageOccurrence(i).getPageID();
      PageUrls[i] = pMiningBase.getPage( pMintPatternDescriptor.getPattern().
        getPageOccurrence(i).getPageID() ).getUrl();
      PageTitles[i] = "-";
      OccurrenceValues[i] = pMintPatternDescriptor.getPattern().
        getPageOccurrence(i).getOccurrence();
      SupportValues[i] = pMintPatternDescriptor.getLayer(i).getSupport();
      RelativeSupportValues[i] = 
        (double)pMintPatternDescriptor.getLayer(i).getSupport() / 
        pMiningBase.getAggregatedLogRootSupport();
      AccessValues[i] = pMiningBase.getPage( pMintPatternDescriptor.
        getPattern().getPageOccurrence(i).getPageID() ).getAccesses();
      if (i == 0) {
        ConfidenceValues[i] = 1.0d;
        CurrentConfidenceBase = SupportValues[i];
        if ( pMintQuery.getTemplate().startsWithRoot() ) {
          CurrentRootConfidenceBase = pMiningBase.getAggregatedLogRootSupport();
          RootConfidenceValues[i] =
            SupportValues[i] / (double)CurrentRootConfidenceBase;
        }
        else {
          CurrentRootConfidenceBase = SupportValues[i];
          RootConfidenceValues[i] = 1.0d;
        }
      }
      else
        CurrentConfidenceBase = SupportValues[i-1]; // kwinkler, 15 Mar 2002
        ConfidenceValues[i] = 
          SupportValues[i] / (double)CurrentConfidenceBase;
        RootConfidenceValues[i] = 
          SupportValues[i] / (double)CurrentRootConfidenceBase;
    }
   
  }  
  
  // ########## mutator methods ##########
  
  public void setPatternID(int pPatternID) 
    { PatternID = pPatternID; }

  // ########## accessor methods ##########
  
  public int getPatternID() 
    { return PatternID; }
  public int getPatternType() 
    { return PatternType; }
  public int getLength() 
    { return Length; }
  public int countVariables() 
    { return Length; }
  public String getVariable(int pIndex) 
    { return Variables[pIndex]; }
  public long getPageID(int pIndex) 
    { return PageIDs[pIndex]; }
  public String getPageUrl(int pIndex) 
    { return PageUrls[pIndex]; }
  public String getPageTitle(int pIndex) 
    { return PageTitles[pIndex]; }
  public int getOccurrence(int pIndex) 
    { return OccurrenceValues[pIndex]; }
  public long getSupport(int pIndex) 
    { return SupportValues[pIndex]; }
  public double getRelativeSupport(int pIndex) 
    { return RelativeSupportValues[pIndex]; }
  public int getAccesses(int pIndex) 
    { return AccessValues[pIndex]; }
  public double getConfidence(int pIndex) 
    { return ConfidenceValues[pIndex]; }
  public double getRootConfidence(int pIndex) 
    { return RootConfidenceValues[pIndex]; }

  // ########## standard methods ##########
  
  public String toString() {
        
    String result = "PatternID=" + PatternID + ", PatternLength=" + Length +
      ", " + Variables[0] + ", " + PageUrls[0] + ", " + SupportValues[0];
     
    return result;
  
  }  // toString()  
  
}