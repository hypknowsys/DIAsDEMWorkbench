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

// How to use Weka for clustering:
// java -classpath weka.jar weka.filters.unsupervised.attribute.Remove -i /home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/testCR/vector1.arff -o /home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/testCR/vector1plain.arff -R 1,2,3
// java -classpath weka.jar weka.clusterers.SimpleKMeans -t /home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/testCR/vector1plain.arff -d /home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/testCR/vector1.kmeans -N 100
// java -classpath weka.jar weka.clusterers.SimpleKMeans -t /home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/testCR/vector1plain.arff -l /home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/testCR/vector1.kmeans -p 0 > /home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/testCR/vector1.kmeans.txt
// thereafter: diasdem.misc.tools.Weka2Csv

package org.hypknowsys.diasdem.tasks.discover.clusterTextUnitVectorsWeka;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.util.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class WekaOutput2Csv  {
  
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

  public WekaOutput2Csv() {}

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
  
  public static int convertWekaOutput2Csv(String pWekaArffInputFileName,
  String pWekaArffOutputFileName, String pWekaArffOutputFile) {
    
    int maxClusterID = 0;
    int clusterID = 0;
    
    TextFile WekaArffInputFile = new TextFile( new File(pWekaArffInputFileName) );
    WekaArffInputFile.open();
    TextFile WekaArffOutputFile = new TextFile( new File(pWekaArffOutputFileName) );
    WekaArffOutputFile.open();
    TextFile CsvOutputFile = new TextFile( new File(pWekaArffOutputFile) );
    CsvOutputFile.empty();
    CsvOutputFile.open();
    
    String WekaArffInputLine = WekaArffInputFile.getFirstLine();
    String[] WekaArffInputLineSplit = null;
    String WekaArffOutputLine = WekaArffOutputFile.getFirstLine();
    String[] WekaArffOutputLineSplit = null;
    String CsvOutputLine = "";
    String FileName = "";
    String TextUnitNumber = "";
    String ClusterID = "";
    while (WekaArffInputLine != null) {
      if ( !WekaArffInputLine.startsWith("@") ) {
        WekaArffInputLineSplit = WekaArffInputLine.split(",");
        //       for (int i = 0; i < WekaArffInputLineSplit.length; i++)
        //         System.out.println(i + ": " + WekaArffInputLineSplit[i]);
        WekaArffOutputLineSplit = WekaArffOutputLine.split(" ");
        //       for (int i = 0; i < WekaArffOutputLineSplit.length; i++)
        //         System.out.println(i + ": " + WekaArffOutputLineSplit[i]);
        if (WekaArffInputLineSplit[0].startsWith("{")) {
          // sparse ARFF format
          FileName = WekaArffInputLineSplit[1].substring(3);
          TextUnitNumber = WekaArffInputLineSplit[2].substring(3);
          if (TextUnitNumber.endsWith("}")) {
            TextUnitNumber = TextUnitNumber.substring(0, 
            TextUnitNumber.length() - 1);
          }
        }
        else {
          // ordinary ARFF format
          FileName = WekaArffInputLineSplit[1];
          TextUnitNumber = WekaArffInputLineSplit[2];
        }
        ClusterID = WekaArffOutputLineSplit[1];
        CsvOutputFile.setNextLine(FileName + "," + TextUnitNumber + "," +
        ClusterID);
        clusterID = Tools.string2Int(ClusterID);
        if (clusterID > maxClusterID) {
          maxClusterID = clusterID;
        }
        WekaArffOutputLine = WekaArffOutputFile.getNextLine();
      }
      else {
      }
      WekaArffInputLine = WekaArffInputFile.getNextLine();
    }
    
    WekaArffInputFile.close();
    WekaArffOutputFile.close();
    CsvOutputFile.close();
    
    return maxClusterID;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String[] args) {
    
    String baseDirectory =
    "/home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/testCR/";
    int maxClusterID = WekaOutput2Csv.convertWekaOutput2Csv(
    baseDirectory + "vector1.arff", 
    baseDirectory + "vector1.kmeans.txt", 
    baseDirectory + "vector1.kmeans.csv");
    
  }
  
}
