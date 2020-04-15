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

package org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsHypknowsys;

import java.io.File;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class HypknowsysOutput2Csv  {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
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
  
  public HypknowsysOutput2Csv() {}
  
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
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static int convertHypknowsysOutput2Csv(
  String pHypknowsysArffInputFileName, String pHypknowsysArffOutputFileName,
  String pHypknowsysArffOutputFile) {
    
    int maxClusterID = 0;
    int clusterID = 0;
    
    TextFile hypknowsysArffInputFile = new TextFile(
    new File(pHypknowsysArffInputFileName));
    hypknowsysArffInputFile.open();
    TextFile hypknowsysArffOutputFile = new TextFile(
    new File(pHypknowsysArffOutputFileName));
    hypknowsysArffOutputFile.open();
    TextFile csvOutputFile = new TextFile(
    new File(pHypknowsysArffOutputFile));
    csvOutputFile.empty();
    csvOutputFile.open();
    
    String hypknowsysArffInputLine = hypknowsysArffInputFile.getFirstLine();
    String[] hypknowsysArffInputLineSplit = null;
    String hypknowsysArffOutputLine = hypknowsysArffOutputFile.getFirstLine();
    String[] hypknowsysArffOutputLineSplit = null;
    String csvOutputLine = "";
    String fileName = "";
    String textUnitNumber = "";
    String myClusterID = "";
    while (hypknowsysArffInputLine != null) {
      if (!hypknowsysArffInputLine.startsWith("@")) {
        hypknowsysArffInputLineSplit = hypknowsysArffInputLine.split(",");
        hypknowsysArffOutputLineSplit = hypknowsysArffOutputLine.split(" ");
        if (hypknowsysArffInputLineSplit[0].startsWith("{")) {
          // sparse ARFF format
          fileName = hypknowsysArffInputLineSplit[1].substring(3);
          textUnitNumber = hypknowsysArffInputLineSplit[2].substring(3);
          if (textUnitNumber.endsWith("}")) {
            textUnitNumber = textUnitNumber.substring(0,
            textUnitNumber.length() - 1);
          }
        }
        else {
          // ordinary ARFF format
          fileName = hypknowsysArffInputLineSplit[1];
          textUnitNumber = hypknowsysArffInputLineSplit[2];
        }
        myClusterID = hypknowsysArffOutputLineSplit[1];
        csvOutputFile.setNextLine(fileName + "," + textUnitNumber + ","
        + myClusterID);
        clusterID = Tools.string2Int(myClusterID);
        if (clusterID > maxClusterID) {
          maxClusterID = clusterID;
        }
        hypknowsysArffOutputLine = hypknowsysArffOutputFile.getNextLine();
      }
      hypknowsysArffInputLine = hypknowsysArffInputFile.getNextLine();
    }
    
    hypknowsysArffInputFile.close();
    hypknowsysArffOutputFile.close();
    csvOutputFile.close();
    
    return maxClusterID;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static int createArffOutputFile(
  String pHypknowsysArffInputFileName, String pHypknowsysArffOutputFileName,
  String pHypknowsysArffOutputFile) {
    
    int maxClusterID = 0;
    int clusterID = 0;
    
    TextFile hypknowsysArffInputFile = new TextFile(
    new File(pHypknowsysArffInputFileName));
    hypknowsysArffInputFile.open();
    TextFile hypknowsysArffOutputFile = new TextFile(
    new File(pHypknowsysArffOutputFileName));
    hypknowsysArffOutputFile.open();
    TextFile arffOutputFile = new TextFile(
    new File(pHypknowsysArffOutputFile));
    arffOutputFile.empty();
    arffOutputFile.open();
    
    String hypknowsysArffInputLine = hypknowsysArffInputFile.getFirstLine();
    String hypknowsysArffOutputLine = hypknowsysArffOutputFile.getFirstLine();
    String[] hypknowsysArffOutputLineSplit = null;
    String myClusterID = "";
    int attributeIndexOfClusterID = -1;  // -1 due to @relation and @data
    while (hypknowsysArffInputLine != null) {
      if (hypknowsysArffInputLine.startsWith("@data")
      || hypknowsysArffInputLine.startsWith("@DATA")) {
        arffOutputFile.setNextLine("@attribute clusterID integer");
        arffOutputFile.setNextLine("@data");
      }
      else if (!hypknowsysArffInputLine.startsWith("@")) {
        attributeIndexOfClusterID++;
        hypknowsysArffOutputLineSplit = hypknowsysArffOutputLine.split(" ");
        myClusterID = hypknowsysArffOutputLineSplit[1];
        clusterID = Tools.string2Int(myClusterID);
        if (clusterID > maxClusterID) {
          maxClusterID = clusterID;
        }
        if (hypknowsysArffInputLine.startsWith("{")
        && hypknowsysArffInputLine.endsWith("}")) {
          // sparse ARFF format
          arffOutputFile.setNextLine(hypknowsysArffInputLine
          .substring(0, hypknowsysArffInputLine.length() - 1)
          + ", " + attributeIndexOfClusterID + " " + myClusterID + "}");
        }
        else {
          // ordinary ARFF format
          arffOutputFile.setNextLine(hypknowsysArffInputLine + ","
          + myClusterID);
        }
        hypknowsysArffOutputLine = hypknowsysArffOutputFile.getNextLine();
      }
      else {
        // keep relation name and all other attributes
        arffOutputFile.setNextLine(hypknowsysArffInputLine);
      }
      hypknowsysArffInputLine = hypknowsysArffInputFile.getNextLine();
    }
    
    hypknowsysArffInputFile.close();
    hypknowsysArffOutputFile.close();
    arffOutputFile.close();
    
    return maxClusterID;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String[] pOptions) {}
  
}