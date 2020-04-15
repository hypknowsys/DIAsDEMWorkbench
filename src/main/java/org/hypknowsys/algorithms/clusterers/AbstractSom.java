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
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import org.hypknowsys.algorithms.core.AbstractOptionHandler;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.algorithms.core.Option;
import org.hypknowsys.algorithms.core.Utils;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;

/**
 * Based on weka.clusterers.SimpleKMeans, Revision 1.9:
 * Copyright (C) 2000 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * Abstract class for various algorithms that establish Self-Organizing Maps 
 * (SOMs). We refer to the book by Toivo Kohonen (Self-Organzing Maps, 3rd
 * edition, Springer-Verlag, Berlin, Heidelberg, 2001) for an excellent 
 * introduction into Self-Organizing Maps. Training and applications instances 
 * should be pre-processed outside this algorithm instance, but using the 
 * exactly the same process workflow for both training and application data. 
 * According to the chosen distance measure, STRING and DATE attributes might
 * be ignored for computing the distance between two instances. Check the 
 * documentation of the respective distance measure.
 *
 * Valid options of all SOM variants are:<p>
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
 * -rows <number of rows> <br>
 * Specify the number of rows in the resulting SOM, default: 5. The number 
 * of clusters to be created is the product of rows and columns.<p>
 *
 * -columns <number of colums> <br>
 * Specify the number of columns in the resulting SOM, default: 5. The number 
 * of clusters to be created is the product of rows and columns.<p>
 *
 * -latticeType <rectangular|hexagonal> <br>
 * Specify the lattice type of the SOM array, default: rectangular. In a
 * rectangalar SOM, the maximum number of neighbors is 5 for each node. Each
 * node can have up to six neighbors in a hexagonal SOM.<p>
 *
 * -S <seed> <br>
 * Specify the seed of the random number generator, default: 10. <p>
 *
 * -maxIterations <max. number of iterations> <br>
 * Specify the maximum number of iterations, default: Integer.MAX_VALUE. <p>
 *
 *
 * @version 0.1, 24 November 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 * @see Clusterer
 * @see OptionHandler
 */

public abstract class AbstractSom extends Clusterer {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Number of rows in the SOM
   */
  protected int NumberOfRows = DEFAULT_NUMBER_OF_ROWS;
  
  /**
   * Number of columns in the SOM
   */
  protected int NumberOfColumns = DEFAULT_NUMBER_OF_COLUMNS;
  
  /**
   * Lattice type of the SOM array (i.e., rectangular or hexagonal)
   */
  protected int LatticeType = DEFAULT_LATTICE_TYPE;
  
  /**
   * Number of clusters to generate := NumberOfRows * NumberOfColumns
   */
  protected int NumberOfClusters = 0;
  
 /**
   * Array containing cluster cardinalities: The array size equals
   * this.NumberOfClusters and ClusterCardinality[0] represents the
   * cardinality of the first cluster (ID = 0). Analogously, the 
   * cardinality of the last cluster is stored in 
   * ClusterCardinality[umberOfClusters - 1].
   */
  protected int[] ClusterCardinality = null;
  
  /**
   * For each cluster codebook vector (i.e., reference vector or SOM node), this
   * array contains the row ID according to the specified number of rows and 
   * columns as well as the lattice type of the SOM array. The row ID is in the 
   * interval [0; NumberOfRows - 1]. The array size equals 
   * this.getNumberOfClusters().RowsOfCodebookVectors[0] contains the row ID of
   * the first codebook vector (ID = 0). Analogously, 
   * RowsOfCodebookVectors[this.NumberOfClusters - 1] contains the row ID of the
   * last cluster codebook vector.
   */
  protected int[] RowsOfCodebookVectors = null;
  
  /**
   * For each cluster codebook vector (i.e., reference vector or SOM node), this
   * array contains the column ID according to the specified number of rows and
   * columns as well as the lattice type of the SOM array. The column ID is in 
   * the interval [0; NumberOfColumns - 1]. The array size equals 
   * this.getNumberOfClusters(). ColumnsOfCodebookVectors[0] contains the 
   * column ID of the first codebook vector (ID = 0). Analogously, 
   * ColumnsOfCodebookVectors[this.NumberOfClusters - 1] contains the
   * column ID of the last cluster codebook vector.
   */
  protected int[] ColumnsOfCodebookVectors = null;
  
  /**
   * For each cluster codebook vector (i.e., reference vector or SOM node), this
   * array contains the row location that corresponds to the 1st component of 
   * the node's location vector in the SOM. The row location is in the interval 
   * [0; NumberOfRows - 1] for both rectangular and hexagonal SOMs. The array 
   * size equals this.getNumberOfClusters(). RowLocationsOfCodebookVectors[0] 
   * contains the row location of the first codebook vector (ID = 0). 
   * Analogously, RowLocationsOfCodebookVectors[this.NumberOfClusters - 1] 
   * contains the row location of the last cluster codebook vector.
   */
  protected double[] RowLocationsOfCodebookVectors = null;
  
  /**
   * For each cluster codebook vector (i.e., reference vector or SOM node), this
   * array contains the column location that corresponds to the 2nd component of
   * the node's location vector in the SOM. The column location is in the 
   * interval [0; NumberOfRows - 1] for rectangular and in the interval [0; 
   * NumberOfRows - 1 + 0.5] for hexagonal SOMs. The array size equals 
   * this.getNumberOfClusters(). ColumnLocationsOfCodebookVectors[0] 
   * contains the column location of the first codebook vector (ID = 0). 
   * Analogously, ColumnLocationsOfCodebookVectors[this.NumberOfClusters - 1] 
   * contains the column location of the last cluster codebook vector.
   */
  protected double[] ColumnLocationsOfCodebookVectors = null;
  
  /**
   * Data set containing cluster codebook vectors, which are referred to as 
   * reference vectors by Kohonen: The number of instances equals
   * this.getNumberOfClusters() and CodebookVectors.instance(0) represents the
   * codebook vector of the first cluster (ID = 0). Analogously, the codebook 
   * vector of the last cluster is stored in 
   * CodebookVectors.instance(this.NumberOfClusters - 1).
   */
  protected Instances CodebookVectors = null;
  
  /**
   * Integer used for initializing the random number generator
   */
  protected int Seed = DEFAULT_SEED;
  
  /**
   * stopping criterion: Maximum number of reassignment iterations
   */
  protected int MaxNumberOfIterations = DEFAULT_MAX_NUMBER_OF_ITERATIONS;
  
  /**
   * Keeps track of the number of iterations completed before convergence
   */
  protected int Iterations = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Random number generator for selecting initial cluster codebook vectors
   */
  protected transient Random RandomNumberGenerator = null;
  
  /**
   * Temporary string buffer for performance purposes only
   */
  protected transient StringBuffer TmpStringBuffer = null;
  
  /**
   * Temporary double variable for improving performance only
   */
  private transient double TmpDoubleValue = Double.NaN;
  
  /**
   * Temporary boolean variable for improving performance only
   */
  private transient boolean TmpBooleanValue = false;
  
  /**
   * Temporary double variable (distance) for improving performance only
   */
  private transient double TmpDistance = Double.NaN;
  
  /**
   * Temporary double variable (min. distance) for improving performance only
   */
  private transient double TmpMinDistance = Double.NaN;
  
  /**
   * Temporary double variable (best cluster ID) for improving performance only
   */
  private transient int TmpBestClusterID = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Constant represents the type of a rectangular SOM array
   */
  protected static final int RECTANGULAR_LATTICE = 1;
  
  /**
   * Constant represents the type of a hexagonal SOM array
   */
  protected static final int HEXAGONAL_LATTICE = 2;
  
  /**
   * Default setting of attribute this.NumberOfRows
   */
  protected static final int DEFAULT_NUMBER_OF_ROWS = 5;

  /**
   * Default setting of attribute this.NumberOfColumns
   */
  protected static final int DEFAULT_NUMBER_OF_COLUMNS = 5;

  /**
   * Default setting of attribute this.LatticeType
   */
  protected static final int DEFAULT_LATTICE_TYPE = RECTANGULAR_LATTICE;
  
  /**
   * Default setting of attribute this.Seed
   */
  protected static final int DEFAULT_SEED = 10;
  
  /**
   * Default setting of attribute this.MaxNumberOfIterations
   */
  protected static final int DEFAULT_MAX_NUMBER_OF_ITERATIONS = Integer
  .MAX_VALUE;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the random number seed.
   *
   * @return the seed
   */
  
  public int getSeed() {
    
    return  Seed;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of clusters that is defined as the product of 
   * rows and columns in the SOM.
   *
   * @return the number of clusters generated for a training dataset.
   */
  public int getNumberOfClusters() {
    
    return NumberOfClusters;
    
  }
  
  /**
   * Returns the number of rows in the SOM.
   *
   * @return the number of rows in the SOM generated for a training dataset.
   */
  public int getNumberOfRows() {
    
    return NumberOfRows;
    
  }
  
  /**
   * Returns the number of columns in the SOM.
   *
   * @return the number of columns in the SOM generated for a training dataset.
   */
  public int getNumberOfColumns() {
    
    return NumberOfColumns;
    
  }
  
  /**
   * Returns the lattice type of the SOM array as an integer constant that
   * can take the values AbstractSom.RECTANGULAR_LATTICE or 
   * AbstractSom.HEXAGONAL_LATTICE.
   *
   * @return the lattice type of the SOM array.
   */
  public int getLatticeType() {
    
    return LatticeType;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the maximum number of iterations (i.e., a stopping criterion).
   *
   * @return the maximum number of iterations.
   */
  
  public int getMaxNumberOfIterations() {
    
    return MaxNumberOfIterations;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the random number seed, default: 10.
   *
   * @param pNewSeed the seed
   */
  
  public void setSeed(int pNewSeed) {
    
    if (pNewSeed >= 0) {
      pNewSeed = pNewSeed;
    }
    else {
      pNewSeed = DEFAULT_SEED;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the number of rows in the SOM, default: 5, the number of 
   * columns in the SOM, default: 5, and the lattice type of the SOM 
   * array (i.e., AbstractSom.RECTANGULAR_LATTICE or 
   * AbstractSom.HEXAGONAL_LATTICE), default: rectangular lattice.
   *
   * @param pNewNumberOfRows the number of rows in the SOM
   * @param pNewNumberOfColumns the number of rows in the SOM
   * @param pNewLatticeType the lattice type of the SOM
   */
  public void setSomProperties(int pNewNumberOfRows,
  int pNewNumberOfColumns, int pNewLatticeType) {
    
    if (pNewNumberOfRows > 0) {
      NumberOfRows = pNewNumberOfRows;
    }
    else {
      NumberOfRows = DEFAULT_NUMBER_OF_ROWS;
    }
    if (pNewNumberOfColumns > 0) {
      NumberOfColumns = pNewNumberOfColumns;
    }
    else {
      NumberOfColumns = DEFAULT_NUMBER_OF_COLUMNS;
    }
    LatticeType = DEFAULT_LATTICE_TYPE;
    if (pNewLatticeType == HEXAGONAL_LATTICE) {
      LatticeType = HEXAGONAL_LATTICE;
    }
    else {
      LatticeType = RECTANGULAR_LATTICE;
    }
    NumberOfClusters = NumberOfRows * NumberOfColumns;
    
    RowsOfCodebookVectors = new int[this.getNumberOfClusters()];
    ColumnsOfCodebookVectors = new int[this.getNumberOfClusters()];
    RowLocationsOfCodebookVectors = new double[this.getNumberOfClusters()];
    ColumnLocationsOfCodebookVectors = new double[this.getNumberOfClusters()];
    int codebookVectorIndex = 0;
    for (int row = 0; row < NumberOfRows; row++) {
      for (int column = 0; column < NumberOfColumns; column++) {
        RowsOfCodebookVectors[codebookVectorIndex] = row;
        ColumnsOfCodebookVectors[codebookVectorIndex] = column;
        RowLocationsOfCodebookVectors[codebookVectorIndex] = (double)row;
        ColumnLocationsOfCodebookVectors[codebookVectorIndex] = (double)column;
        if (LatticeType == HEXAGONAL_LATTICE && (row % 2) == 1) {
          ColumnLocationsOfCodebookVectors[codebookVectorIndex] += 0.5d;
        }
        codebookVectorIndex++;
      }
    }
    
    // Example of rectangular SOM with 3 rows and 5 columns,
    // whose codebook vector 5 is in row 1 and column 0:
    //
    //  0  1  2  3  4     0:(0,0)  1:(0,1)  2:(0,2)  3:(0,3)  4:(0,4)
    //  5  6  7  8  9     5:(1,0)  6:(1,1)  7:(1,2)  8:(1,3)  9:(1,4)
    // 10 11 12 13 14    10:(2,0) 11:(2,1) 12:(2,2) 13:(2,3) 14:(2,4)
    //
    // Example of hexagonal SOM with 5 rows and 5 columns,
    // whose codebook vector 13 is in row 2 and column 3.
    //
    //   0   1   2   3   4       0:(0,0)  1:(0,1)  2:(0,2)  3:(0,3)  4:(0,4)
    //     5   6   7   8   9     5:(1,0)  6:(1,1)  7:(1,2)  8:(1,3)  9:(1,4)
    //  10  11  12  13  14      10:(2,0) 11:(2,1) 12:(2,2) 13:(2,3) 14:(2,4)
    //    15  16  17  18  19    15:(3,0) 16:(3,1) 17:(3,2) 18:(3,3) 19:(3,4)
    //  20  21  22  23  24      20:(4,0) 21:(4,1) 22:(4,2) 23:(4,3) 24:(4,4)
    // 
    //   0:(0,0)    1:(0,1)    2:(0,2)    3:(0,3)    4:(0,4)
    //   5:(1,0.5)  6:(1,1.5)  7:(1,2.5)  8:(1,3.5)  9:(1,4.5)
    //  10:(2,0)   11:(2,1)   12:(2,2)   13:(2,3)   14:(2,4)
    //  15:(3,0.5) 16:(3,1.5) 17:(3,2.5) 18:(3,3.5) 19:(3,4.5)
    //  20:(4,0)   21:(4,1)   22:(4,2)   23:(4,3)   24:(4,4)
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the maximum number of clustering iterations (i.e., a stopping
   * criterion), default: Interger.MAX_VALUE.
   *
   * @param pMaxNumberOfIterations the maximum number of iterations
   */
  
  public void setMaxNumberOfIterations(int pMaxNumberOfIterations) {
    
    if (pMaxNumberOfIterations > 0) {
      MaxNumberOfIterations = pMaxNumberOfIterations;
    }
    else {
      MaxNumberOfIterations = DEFAULT_MAX_NUMBER_OF_ITERATIONS;
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
    
    TmpStringBuffer = new StringBuffer(10000);
    
    TmpStringBuffer.append(super.toString());
    TmpStringBuffer.append("Number of clusters: " + this.getNumberOfClusters()
    + "\n");
    TmpStringBuffer.append("Number of SOM rows: " + NumberOfRows + "\n");
    TmpStringBuffer.append("Number of SOM columns: " + NumberOfColumns + "\n");
    TmpStringBuffer.append("Number of iterations: " + Iterations + "\n");
    for (int i = 0; i < this.getNumberOfClusters() 
    && CodebookVectors != null; i++) {
      TmpStringBuffer.append("\nCodebook Vector of cluster " + i + "\n");
      for (int j = 0; j < CodebookVectors.numAttributes(); j++) {
        if (CodebookVectors.attribute(j).isNominal()) {
          TmpStringBuffer.append("\t"
          + TrainingInstancesMetadata.getNameOfAttribute(j) + " = "
          + CodebookVectors.attribute(j).value((int)CodebookVectors
          .instance(i).value(j)) + "\n");
        } 
        else {
          TmpStringBuffer.append("\t"
          + TrainingInstancesMetadata.getNameOfAttribute(j) + " = "
          + Utils.doubleToString(CodebookVectors.instance(i).value(j), 3)
          + "; Value_Codebook_Vector/Mean(Value_Population) = "
          + Utils.doubleToString(CodebookVectors.instance(i).value(j)
          / TrainingInstancesMetadata.getMeanOfAttribute(j), 3) + "\n");
        }
      }
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface OptionHandler methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an enumeration describing the available options. <p>
   *
   * Valid options of all SOM variants are:<p>
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
   * -rows <number of rows> <br>
   * Specify the number of rows in the resulting SOM, default: 5. The number
   * of clusters to be created is the product of rows and columns.<p>
   *
   * -columns <number of colums> <br>
   * Specify the number of columns in the resulting SOM, default: 5. The number
   * of clusters to be created is the product of rows and columns.<p>
   *
   * -latticeType <rectangular|hexagonal> <br>
   * Specify the lattice type of the SOM array, default: rectangular. In a
   * rectangalar SOM, the maximum number of neighbors is 5 for each node. Each
   * node can have up to six neighbors in a hexagonal SOM.<p>
   *
   * -S <seed> <br>
   * Specify the seed of the random number generator, default: 10. <p>
   *
   * -maxIterations <max. number of iterations> <br>
   * Specify the maximum number of iterations, default: Integer.MAX_VALUE. <p>
   *
   * @return an enumeration of all the available options.
   *
   **/
  
  public Enumeration listOptions() {
    
    Vector newVector = new Vector();
    Enumeration enumeration = super.listOptions();
    while (enumeration.hasMoreElements()) {
      newVector.addElement(enumeration.nextElement());
    }
    
    newVector.addElement(new Option(
    "\tSpecify the number of rows in the resulting SOM, default: 5.",
    "rows", 1, "-rows <number of rows>"));
    newVector.addElement(new Option(
    "\tSpecify the number of columns in the resulting SOM, default: 5.",
    "columns", 1, "-columns <number of columns>"));
    newVector.addElement(new Option(
    "\tSpecify the lattice type of the SOM array, default: rectangular.",
    "latticeType", 1, "-latticeType <rectangular|hexagonal>"));
    newVector.addElement(new Option(
    "\tSpecify the seed of the random number generator, default: 10.", 
    "S", 1, "-S <num>"));
    newVector.addElement(new Option(
    "\tSpecify the maximum number of iterations, "
    + "default: Integer.MAX_VALUE.",
    "maxIterations", 1, "-maxIterations <max. number of iterations>"));
    
    return  newVector.elements();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Parses a given list of options.
   * @param pOptions the list of options as an array of strings
   **/
  
  public void setOptions(String[] pOptions) {
    
    super.setOptions(pOptions);
    
    String optionString = AbstractOptionHandler.getOption("rows", pOptions);
    int numberOfRows = DEFAULT_NUMBER_OF_ROWS;
    if (optionString.length() != 0) {
      numberOfRows = Tools.string2Int(optionString);
    }
    int numberOfColumns = DEFAULT_NUMBER_OF_COLUMNS;
    optionString = AbstractOptionHandler.getOption("columns", pOptions);
    if (optionString.length() != 0) {
      numberOfColumns = Tools.string2Int(optionString);
    }
    int latticeType = DEFAULT_LATTICE_TYPE;
    optionString = AbstractOptionHandler.getOption("latticeType", pOptions);
    if (optionString.length() != 0) {
      if (optionString.toLowerCase().equals("hexagonal")) {
        latticeType = HEXAGONAL_LATTICE;
      }
      else if (optionString.toLowerCase().equals("rectangular")) {
        latticeType = RECTANGULAR_LATTICE;
      }
    }
    this.setSomProperties(numberOfRows, numberOfColumns, latticeType);
    
    optionString = AbstractOptionHandler.getOption('S', pOptions);
    this.setSeed(DEFAULT_SEED);
    if (optionString.length() != 0) {
      this.setSeed(Tools.string2Int(optionString));
    }
    
    optionString = AbstractOptionHandler.getOption("maxIterations", pOptions);
    if (optionString.length() != 0) {
      this.setMaxNumberOfIterations(Tools.string2Int(optionString));
    }
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets the current settings of SimpleKMeans.
   *
   * @return an array of strings suitable for passing to setOptions()
   */
  
  public String[] getOptions() {
    
    String[] superOptions = super.getOptions();
    
    int numberOfOptions = superOptions.length + 8;
    if (MaxNumberOfIterations < Integer.MAX_VALUE) {
      numberOfOptions += 2;
    }
    String[] options = new String[numberOfOptions];
    int current = 0;

    for (int i = 0; i < superOptions.length; i++) {
      options[current++] = superOptions[i];
    }
    options[current++] = "-rows";
    options[current++] = "" + this.getNumberOfRows();
    options[current++] = "-columns";
    options[current++] = "" + this.getNumberOfColumns();
    options[current++] = "-latticeType";
    options[current++] = "" + this.getLatticeType();
    options[current++] = "-S";
    options[current++] = "" + this.getSeed();
    if (MaxNumberOfIterations < Integer.MAX_VALUE) {
      options[current++] = "-maxIterations";
      options[current++] = "" + MaxNumberOfIterations;
    }
    
    while (current < options.length) {
      options[current++] = "";
    }
    
    return options;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## abstract class Clusterer methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Classifies a given instance.
   *
   * @param pInstance the instance to be assigned to a cluster
   * @return the number of the assigned cluster as an interger
   * if the class is enumerated, otherwise the predicted value
   */
  
  public int clusterInstance(Instance pInstance) {
    
    return clusterProcessedInstance(pInstance);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of clusters that is defined as the product of 
   * rows and columns in the SOM.
   *
   * @return the number of clusters generated for a training dataset.
   */
  
  public int numberOfClusters() {
    
    return this.getNumberOfClusters();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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
   * and the array CodebookVectors contains the final cluster codebook vectors.
   */
  
  protected void assessClusterValidity() {
  
    MyClusterValidityAssessor = new RelativeClusterValidityAssessor();
    MyClusterValidityAssessor.setTaskProgressListener(MyServer, MyTaskProgress, 
    MyTaskThread);
    MyClusterValidityAssessor.assessClusterValidity(EvaluateInternalQuality, 
    (Clusterer)this);
  
  }
        
 /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the name of the representative instance returned by the specific
   * SOM clustering algorithm.
   * @return the name of the representative instance
   */
  
  protected String getNameOfRepresentativeInstance() {
    
    return "Codebook Vector";
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns one instance (i.e., the codebook vector) that represents the 
   * specified cluster. Prerequisites: The specific clustering algorithm must 
   * have been executed.
   * @param pClusterID the ID of the cluster whose representative instance
   * shall be returned; 0 <= pClusterID < this.getNumberOfClusters()
   * @return the representative instance or null if pClusterID is not valid
   */
  
  protected Instance getRepresentativeInstance(
  int pClusterID) {
    
    if (CodebookVectors != null && pClusterID >= 0 
    && pClusterID < this.getNumberOfClusters()) {
      return CodebookVectors.instance(pClusterID);
    }
    else {
      return null;
    }
    
  }
        
 /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends the section containing cluster validity indices and details about 
   * all cluster codebook vectors to existing and open HTML report. The text 
   * file remains open.
   */
  
  protected void createHtmlReportClusterValiditySection() {
  
    if (MyClusterValidityAssessor != null) {
      MyClusterValidityAssessor.createHtmlReportClusterValiditySection();
    }
  
  }
  
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
    
    super.createHtmlReportParameterSection();
    
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Number of Clusters</td>"
    + "<td align=\"left\" valign=\"top\">" + this.getNumberOfClusters()
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Number of SOM Rows</td>"
    + "<td align=\"left\" valign=\"top\">" + NumberOfRows
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Number of SOM Columns</td>"
    + "<td align=\"left\" valign=\"top\">" + NumberOfColumns
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Type of SOM Lattice</td>"
    + "<td align=\"left\" valign=\"top\">" + (LatticeType 
    == HEXAGONAL_LATTICE ? "hexagonal" : "rectangular")
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Random Seed</td>"
    + "<td align=\"left\" valign=\"top\">" + Seed
    + "</td></tr>");
    HtmlReport.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Max. Iterations</td>"
    + "<td align=\"left\" valign=\"top\">" + (MaxNumberOfIterations 
    == Integer.MAX_VALUE ? "n/a" : "" + MaxNumberOfIterations)
    + "</td></tr>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Appends a visualization section that depicts the SOM to existing and open 
   * HTML report. The text file remains open.
   */
  
  protected void createHtmlReportVisualizationSection() {
  
    String[] bgcolors = new String[NumberOfClusters];
    for (int i = 0; i < bgcolors.length; i++) {
      bgcolors[i] = "#FFFFFF";
    }
    
    if (EvaluateInternalQuality != ClusterValidityAssessor
    .ASSESS_CLUSTER_VALIDITY_FALSE && MyClusterValidityAssessor != null) {
      int maxCardinality = 0;
      for (int i = 0; i < MyClusterValidityAssessor.ClusterCardinality.length;
      i++) {
        if (MyClusterValidityAssessor.ClusterCardinality[i] > maxCardinality) {
          maxCardinality = MyClusterValidityAssessor.ClusterCardinality[i];
        }
      }
      double relativeClustercardinality = 0.0d;
      for (int i = 0; i < bgcolors.length; i++) {
        relativeClustercardinality = MyClusterValidityAssessor
        .ClusterCardinality[i] / (double)maxCardinality;
        if (relativeClustercardinality > 0.95) {
          bgcolors[i] = "#00CC00";
        }
        else if (relativeClustercardinality > 0.85d) {
          bgcolors[i] = "#00FF00";
        }
        else if (relativeClustercardinality > 0.75d) {
          bgcolors[i] = "#33FF00";
        }
        else if (relativeClustercardinality > 0.65d) {
          bgcolors[i] = "#66FF00";
        }
        else if (relativeClustercardinality > 0.55d) {
          bgcolors[i] = "#99FF00";
        }
        else if (relativeClustercardinality > 0.45d) {
          bgcolors[i] = "#CCFF00";
        }
        else if (relativeClustercardinality > 0.35d) {
          bgcolors[i] = "#FFFF00";
        }
        else if (relativeClustercardinality > 0.25d) {
          bgcolors[i] = "#FFFF33";
        }
        else if (relativeClustercardinality > 0.15d) {
          bgcolors[i] = "#FFFF66";
        }
        else if (relativeClustercardinality > 0.05d) {
          bgcolors[i] = "#FFFF99";
        }
        else if (relativeClustercardinality > 0.0d) {
          bgcolors[i] = "#FFFFCC";
        }
        else {
          bgcolors[i] = "#FFFFFF";
        }
      }
    }
    
    HtmlReport.setNextLine("<h3>Result Map of Text Unit Vectors</h3>");
    HtmlReport.setNextLine(
    "<p><small><a href=\"#TOP\">Top of the Page</a> - "
    + "<a href=\"#PARAMETERS\">Parameter Settings</a> - "
    + "<a href=\"#LOG\">Execution Log</a> - " + (EvaluateInternalQuality 
    != ClusterValidityAssessor.ASSESS_CLUSTER_VALIDITY_FALSE
    ? "<a href=\"#CLUSTER_VALIDITY\">Cluster Validity Asessment</a> - " : "")
    + "<a href=\"#BOP\">Bottom of the Page</a></small></p>");
    HtmlReport.setNextLine("<p>Position your mouse pointer above the cluster "
    + "ID of a SOM unit to obtain further information about the respective "
    + "text unit cluster. The color of SOM units indicates the cluster "
    + "cardinality. Dark green (and light yellow, resp.) SOM units exhibit the "
    + "highest (and lowest, resp.) relative cluster cardinality, whereas white "
    + "SOM units are empty.</p>");
    HtmlReport.setNextLine("<table border=\"1\">");
    
    if (LatticeType == HEXAGONAL_LATTICE) {
      for (int column = 0; column < (2 * NumberOfColumns + 1); column++) {
        HtmlReport.setNextLine("<td bgcolor=\"#FFFFFF\" width=\"12\" "
        + "height=\"1\"></td>");
      }
    }   
  
    int codebookVectorIndex = 0;
    for (int row = 0; row < NumberOfRows; row++) {
      HtmlReport.setNextLine("<tr>");  
      if (LatticeType == HEXAGONAL_LATTICE && (row % 2) == 1) {
        HtmlReport.setNextLine("<td bgcolor=\"#FFFFFF\" width=\"12\""
        + "height=\"24\" align=\"center\">&nbsp;</td>");
      }
      for (int column = 0; column < NumberOfColumns; column++) {
        HtmlReport.setNextLine("<td bgcolor=\"" + bgcolors[codebookVectorIndex] 
        + "\" width=\"24\"" + "height=\"24\" align=\"center\"" 
        + (LatticeType == HEXAGONAL_LATTICE ? " colspan=\"2\"" : "") 
        + "><a href=\"#CLUSTER" + codebookVectorIndex + "\" title=\"" 
        + MyClusterValidityAssessor.ClusterDigests[codebookVectorIndex] 
        + "; SOM Unit: (" + row + ", " + column + ")\">" + codebookVectorIndex 
        + "</a></td>");
        codebookVectorIndex++;
      }
      if (LatticeType == HEXAGONAL_LATTICE && (row % 2) == 0) {
        HtmlReport.setNextLine("<td bgcolor=\"#FFFFFF\" width=\"12\""
        + "height=\"24\" align=\"center\">&nbsp;</td>");
      }
      HtmlReport.setNextLine("</tr>");  
    }

    if (LatticeType == HEXAGONAL_LATTICE) {
      for (int column = 0; column < (2 * NumberOfColumns + 1); column++) {
        HtmlReport.setNextLine("<td bgcolor=\"#FFFFFF\" width=\"12\" "
        + "height=\"1\"></td>");
      }
    }   
    HtmlReport.setNextLine("</table>");
  
  }
  
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
  
  /**
   * Clusters an instance. Prerequisite: pInstance must have been pre-processed
   * analogously to the training instances.
   *
   * @param pInstance the instance to assign a cluster to
   * @return a cluster number
   */
  
  protected int clusterProcessedInstance(Instance pInstance) {
    
    TmpMinDistance = Double.MAX_VALUE;
    TmpBestClusterID = 0;
    for (int i = 0; i < this.getNumberOfClusters(); i++) {
      TmpDistance = MyDistanceMeasure.computeDistance(pInstance,
      CodebookVectors.instance(i), TrainingInstancesMetadata);
      if (TmpDistance < TmpMinDistance) {
        TmpMinDistance = TmpDistance;
        TmpBestClusterID = i;
      }
    }
    
    return TmpBestClusterID;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

}