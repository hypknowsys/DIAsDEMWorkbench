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

package org.hypknowsys.algorithms.core;

import java.io.*;
import java.util.*;
import java.text.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.util.*;

/**
 * Inspired by weka.core.Instances: Revision 1.36
 * Copyright (C) 1999 Eibe Frank<p>
 *
 * Class for accessing the instances contained in an ARFF file one
 * after the other. In particular, this task allows the application
 * of clusterers and classification models to large data sets by
 * overcoming the main memory limitation. Note that this class cannot
 * be used to create a clusterer or to train a classification model.
 * This class provides read-only access to ARFF files.<p>
 *
 * Short Example for using the Class <code>SequentialInstanceReader</code>: <p>
 * <pre>
 * // import the Java package containing the class SequentialInstanceReader
 * import org.hypknowsys.algorithms.core.*;
 * // import the Java package containing the class File
 * import java.io.*;
 *
 * // ...
 *
 * SequentialInstanceReader mySequentialInstanceReader = null;
 * Instance currentInstance = null;
 *
 * // create a SequentialInstanceReader with 10 MB file buffer
 * mySequentialInstanceReader = new SequentialInstanceReader(
 *   new File("test.arff"), 10240000 );
 *
 * // open the SequentialInstanceReader for reading instances
 * mySequentialInstanceReader.open();
 *
 * // print all instances in the file
 * currentInstance = mySequentialInstanceReader.getFirstInstance();
 * while (currentInstance != null) {
 *   System.out.println( currentInstance.toString() );
 *   currentInstance = mySequentialInstanceReader.getNextInstance();
 * }
 *
 * // close the SequentialInstanceReader
 * mySequentialInstanceReader.close();
 *
 * // ...
 *
 * </pre>
 *
 * @version 0.1, 11 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class SequentialInstanceReader implements Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Text buffered reader is used to sequentially access the instances
   * contained in an ARFF file.
   */
  protected TextBufferedReader MyTextFileReader = null;
  
  /**
   * String reader is used to convert the current line into a reader
   * that is required by the Weka code.
   */
  protected StringReader MyStringReader = null;
  
  /** The file object of the ARFF file. */
  protected File FileName = null;
  
  /** The buffer size for reading the ARFF file. */
  protected  int BufferSize  = 1024000;
  
  /** The currently read line of the ARFF file. */
  protected transient String CurrentLine = null;
  
  /**
   * The currently read instance of the ARFF file or null if the
   * header is currently read.
   */
  protected transient Instance CurrentInstance = null;
  
  /** The preceding line of the currently read line of the ARFF file. */
  protected transient String PrecedingLine = null;
  
  /** The approximate number of instances in the ARFF file. */
  protected int ApproxNumberOfInstances = -1;
  
  /** The prototype instance of the ARFF file. */
  protected Instance PrototypeInstance = null;
  
  /** The prototype instances of the ARFF file. */
  protected Instances PrototypeInstances = null;
  
  /* ########## rest is eqivalent to weka.core.Instances ########## */
  
  /** The tokenizer used to tokenize the ARFF file. */
  protected StreamTokenizer tokenizer = null;;
  
  /** The dataset's name. */
  protected String m_RelationName;
  
  /** The attribute information. */
  protected FastVector m_Attributes;
  
  /** The instances. */
  protected FastVector m_Instances;
  
  /** The class attribute's index */
  protected int m_ClassIndex;
  
  /** Buffer of values for sparse instance */
  protected double[] m_ValueBuffer;
  
  /** Buffer of indices for sparse instance */
  protected int[] m_IndicesBuffer;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Temporary string buffer for performance purposes only
   */
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /** The filename extension that should be used for arff files */
  public static String FILE_EXTENSION = ".arff";
  
  /** The keyword used to denote the start of an arff header */
  protected static String ARFF_RELATION = "@relation";
  
  /** The keyword used to denote the start of the arff data section */
  protected static String ARFF_DATA = "@data";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Opens the ARFF file and reads its header. Lets the class index be
   * undefined (negative).
   *
   * @param pFileName the existing file to open
   * @param pBufferSize the buffer size of the text buffered reader
   * @exception IllegalArgumentException if the header is not read
   * successfully.
   */
  public SequentialInstanceReader(File pFileName, int pBufferSize)
  throws IOException {
    
    if (pFileName == null || !pFileName.exists() || pFileName.isDirectory()) {
      throw new IllegalArgumentException(
      "The ARFF file must be an existing file!");
    }
    if (pBufferSize < 0) {
      throw new IllegalArgumentException(
      "The buffer size has to be positive!");
    }
    FileName = pFileName;
    BufferSize = pBufferSize;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the approximate number of instances in the dataset. This value
   * is only set after opening the ARFF file.
   *
   * @return the approximate number of instances in the dataset as an integer
   */
  public final int getApproxNumberOfInstances() {

    return ApproxNumberOfInstances;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the prototype instance of the dataset. This value
   * is only set after opening the ARFF file.
   *
   * @return the prototype instance of the dataset
   */
  public final Instance getPrototypeInstance() {

    return PrototypeInstance;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the prototype instances of the dataset, which is a data set
   * comprising only the prototype instance. This value is only set after 
   * opening the ARFF file.
   *
   * @return the prototype instance of the dataset
   */
  public final Instances getPrototypeInstances() {

    return PrototypeInstances;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the header as a string in ARFF format. Strings
   * are quoted if they contain whitespace characters, or if they
   * are a question mark.
   *
   * @return the dataset in ARFF format as a string
   */
  public final String toString() {
    
    TmpStringBuffer = new StringBuffer();
    
    TmpStringBuffer.append(ARFF_RELATION).append(" ")
    .append(Utils.quote(m_RelationName)).append("\n\n");
    for (int i = 0; i < numAttributes(); i++) {
      TmpStringBuffer.append(attribute(i)).append("\n");
    }
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void finalize() {
    
    this.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void open() {
    
    MyTextFileReader = new TextBufferedReader(FileName, BufferSize);
    MyTextFileReader.setCommentLinePrefix("%");
    MyTextFileReader.open();
    
    // read header and convert it into a string to re-use Weka code
    StringBuffer headerBuffer= new StringBuffer(10000);
    PrecedingLine = "";
    CurrentLine = MyTextFileReader
    .getFirstLineButIgnoreCommentsAndEmptyLines();
    while (CurrentLine != null && !PrecedingLine.equalsIgnoreCase(ARFF_DATA)) {
      headerBuffer.append(CurrentLine);
      headerBuffer.append("\n");
      PrecedingLine = new String(CurrentLine.trim());
      CurrentLine = MyTextFileReader
      .getNextLineButIgnoreCommentsAndEmptyLines();
    }
    MyStringReader = new StringReader(headerBuffer.toString());
    tokenizer = new StreamTokenizer(MyStringReader);
    this.initTokenizer(tokenizer);
    try {
      this.readHeader(tokenizer);
    }
    catch (IOException e) {
      MyTextFileReader.close();
      System.out.println("Error: Invalid ARFF header! [open]");
      e.printStackTrace(System.out);
      System.out.flush();
    }
    
    if (ApproxNumberOfInstances == -1) {
      // set the prototype instance
      if (CurrentLine != null) {
        MyStringReader = new StringReader(CurrentLine + "\n");
        tokenizer = new StreamTokenizer(MyStringReader);
        this.initTokenizer(tokenizer);
        try {
          PrototypeInstance = this.getInstance(tokenizer, false);
          PrototypeInstances = new Instances(m_RelationName, m_Attributes, 1);
          PrototypeInstances.add(PrototypeInstance);
        }
        catch (IOException e) {
          MyTextFileReader.close();
          System.out.println("Error: Invalid ARFF instance! [open]");
          System.out.println(CurrentLine);
          e.printStackTrace(System.out);
          System.out.flush();
        }
      }
      // estimate the number of instances in the first call of this.open()
      ApproxNumberOfInstances = 1;
      while (CurrentLine != null) {
        ApproxNumberOfInstances++;
        CurrentLine = MyTextFileReader
        .getNextLineButIgnoreCommentsAndEmptyLines();
      }
      // line pointer is now at the end of this ARFF file
    }
    
    m_ClassIndex = -1;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void close() {
    
    if (MyTextFileReader != null) {
      MyTextFileReader.close();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Instance getFirstInstance() {
    
    // Calling Method has to check: Instance != null?
    
    CurrentLine = null;
    this.close();
    this.open();
    
    if (CurrentLine != null) {
      MyStringReader = new StringReader(CurrentLine + "\n");
      tokenizer = new StreamTokenizer(MyStringReader);
      this.initTokenizer(tokenizer);
      try {
        return this.getInstance(tokenizer, false);
      }
      catch (IOException e) {
        MyTextFileReader.close();
        System.out.println("Error: Invalid ARFF instance! [getFirstInstance]");
        System.out.println(CurrentLine);
        e.printStackTrace(System.out);
        System.out.flush();
      }
    }
    
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Instance getNextInstance() {
    
    // Calling Method has to check: Instance != null?
    
    if (MyTextFileReader != null) {
      if (CurrentLine != null) {
        PrecedingLine = new String(CurrentLine);
      }
      CurrentLine = MyTextFileReader.getNextLine();
      if (CurrentLine != null) {
        MyStringReader = new StringReader(CurrentLine + "\n");
        tokenizer = new StreamTokenizer(MyStringReader);
        this.initTokenizer(tokenizer);
        try {
          return this.getInstance(tokenizer, false);
        }
        catch (IOException e) {
          MyTextFileReader.close();
          System.out.println("Error: Invalid ARFF instance! [getFirstInstance]");
          System.out.println(CurrentLine);
          e.printStackTrace(System.out);
          System.out.flush();
        }
      }
    }
    
    return null;
    
  }
  
  /* ########## rest is eqivalent to weka.core.Instances ########## */
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an attribute.
   *
   * @param index the attribute's index
   * @return the attribute at the given position
   */
  public final Attribute attribute(int index) {
    
    return (Attribute) m_Attributes.elementAt(index);
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an attribute given its name. If there is more than
   * one attribute with the same name, it returns the first one.
   * Returns null if the attribute can't be found.
   *
   * @param name the attribute's name
   * @return the attribute with the given name, null if the
   * attribute can't be found
   */
  public final Attribute attribute(String name) {
    
    for (int i = 0; i < numAttributes(); i++) {
      if (attribute(i).name().equals(name)) {
        return attribute(i);
      }
    }
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Checks for string attributes in the dataset
   *
   * @return true if string attributes are present, false otherwise
   */
  public boolean checkForStringAttributes() {
    
    int i = 0;
    
    while (i < m_Attributes.size()) {
      if (attribute(i++).isString()) {
        return true;
      }
    }
    return false;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Checks if the given instance is compatible
   * with this dataset. Only looks at the size of
   * the instance and the ranges of the values for 
   * nominal and string attributes.
   *
   * @return true if the instance is compatible with the dataset 
   */
  public final boolean checkInstance(Instance instance) {

    if (instance.numAttributes() != numAttributes()) {
      return false;
    }
    for (int i = 0; i < numAttributes(); i++) {
      if (instance.isMissing(i)) {
	continue;
      } else if (attribute(i).isNominal() ||
		 attribute(i).isString()) {
	if (!(Utils.eq(instance.value(i),
		       (double)(int)instance.value(i)))) {
	  return false;
	} else if (Utils.sm(instance.value(i), 0) ||
		   Utils.gr(instance.value(i),
			    attribute(i).numValues())) {
	  return false;
	}
      }
    }
    return true;
  }
	
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the class attribute.
   *
   * @return the class attribute
   * @exception UnassignedClassException if the class is not set
   */
  public final Attribute classAttribute() {

    if (m_ClassIndex < 0) {
      throw new UnassignedClassException("Class index is negative (not set)!");
    }
    return attribute(m_ClassIndex);
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the class attribute's index. Returns negative number
   * if it's undefined.
   *
   * @return the class index as an integer
   */
  public final int classIndex() {
    
    return m_ClassIndex;
  }
 
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an enumeration of all the attributes.
   *
   * @return enumeration of all the attributes.
   */
  public Enumeration enumerateAttributes() {

    return m_Attributes.elements(m_ClassIndex);
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Checks if two headers are equivalent.
   *
   * @param dataset another dataset
   * @return true if the header of the given dataset is equivalent 
   * to this header
   */
  public final boolean equalHeaders(Instances dataset){

    // Check class and all attributes
    if (m_ClassIndex != dataset.m_ClassIndex) {
      return false;
    }
    if (m_Attributes.size() != dataset.m_Attributes.size()) {
      return false;
    }
    for (int i = 0; i < m_Attributes.size(); i++) {
      if (!(attribute(i).equals(dataset.attribute(i)))) {
	return false;
      }
    }
    return true;
  }
 
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of attributes.
   *
   * @return the number of attributes as an integer
   */
  public int numAttributes() {
    
    return m_Attributes.size();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of class labels.
   *
   * @return the number of class labels as an integer if the class 
   * attribute is nominal, 1 otherwise.
   * @exception UnassignedClassException if the class is not set
   */
  public final int numClasses() {
    
    if (m_ClassIndex < 0) {
      throw new UnassignedClassException("Class index is negative (not set)!");
    }
    if (!classAttribute().isNominal()) {
      return 1;
    } else {
      return classAttribute().numValues();
    }
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the relation's name.
   *
   * @return the relation's name as a string
   */
  public final String relationName() {

    return m_RelationName;
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /** 
   * Sets the class attribute.
   *
   * @param att attribute to be the class
   */
  public final void setClass(Attribute att) {

    m_ClassIndex = att.index();
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /** 
   * Sets the class index of the set.
   * If the class index is negative there is assumed to be no class.
   * (ie. it is undefined)
   *
   * @param classIndex the new class index
   * @exception IllegalArgumentException if the class index is too big or < 0
   */
  public final void setClassIndex(int classIndex) {

    if (classIndex >= numAttributes()) {
      throw new IllegalArgumentException("Invalid class index: " + classIndex);
    }
    m_ClassIndex = classIndex;
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Sets the relation's name.
   *
   * @param newName the new relation name.
   */
  public final void setRelationName(String newName) {
    
    m_RelationName = newName;
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Reads a single instance using the tokenizer and appends it
   * to the dataset. Automatically expands the dataset if it
   * is not large enough to hold the instance.
   *
   * @param tokenizer the tokenizer to be used
   * @param flag if method should test for carriage return after
   * each instance
   * @return new instance or null if end of file has been reached
   * @exception IOException if the information is not read
   * successfully
   */
  protected Instance getInstance(StreamTokenizer tokenizer,
  boolean flag) throws IOException {
    
    // Check if any attributes have been declared.
    if (m_Attributes.size() == 0) {
      errms(tokenizer,"no header information available");
    }
    
    // Check if end of file reached.
    getFirstToken(tokenizer);
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      return null;
    }
    
    // Parse instance
    if (tokenizer.ttype == '{') {
      return getInstanceSparse(tokenizer, flag);
    } else {
      return getInstanceFull(tokenizer, flag);
    }
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Reads a single instance using the tokenizer and appends it
   * to the dataset. Automatically expands the dataset if it
   * is not large enough to hold the instance.
   *
   * @param tokenizer the tokenizer to be used
   * @param flag if method should test for carriage return after
   * each instance
   * @return new new instance or null if end of file has been reached
   * @exception IOException if the information is not read
   * successfully
   */
  protected Instance getInstanceSparse(StreamTokenizer tokenizer,
  boolean flag) throws IOException {
    
    int valIndex, numValues = 0, maxIndex = -1;
    
    // Get values
    do {
      
      // Get index
      getIndex(tokenizer);
      if (tokenizer.ttype == '}') {
        break;
      }
      
      // Is index valid?
      try{
        m_IndicesBuffer[numValues] = Integer.valueOf(tokenizer.sval).intValue();
      } catch (NumberFormatException e) {
        errms(tokenizer,"index number expected");
      }
      if (m_IndicesBuffer[numValues] <= maxIndex) {
        errms(tokenizer,"indices have to be ordered");
      }
      if ((m_IndicesBuffer[numValues] < 0) ||
      (m_IndicesBuffer[numValues] >= numAttributes())) {
        errms(tokenizer,"index out of bounds");
      }
      maxIndex = m_IndicesBuffer[numValues];
      
      // Get value;
      getNextToken(tokenizer);
      
      // Check if value is missing.
      if  (tokenizer.ttype == '?') {
        m_ValueBuffer[numValues] = Instance.missingValue();
      } else {
        
        // Check if token is valid.
        if (tokenizer.ttype != StreamTokenizer.TT_WORD) {
          errms(tokenizer,"not a valid value");
        }
        switch (attribute(m_IndicesBuffer[numValues]).type()) {
          case Attribute.NOMINAL:
            // Check if value appears in header.
            valIndex =
            attribute(m_IndicesBuffer[numValues]).indexOfValue(tokenizer.sval);
            if (valIndex == -1) {
              errms(tokenizer,"nominal value not declared in header");
            }
            m_ValueBuffer[numValues] = (double)valIndex;
            break;
          case Attribute.NUMERIC:
            // Check if value is really a number.
            try{
              m_ValueBuffer[numValues] = Double.valueOf(tokenizer.sval).
              doubleValue();
            } catch (NumberFormatException e) {
              errms(tokenizer,"number expected");
            }
            break;
          case Attribute.STRING:
            m_ValueBuffer[numValues] =
            attribute(m_IndicesBuffer[numValues]).addStringValue(tokenizer.sval);
            break;
          case Attribute.DATE:
            try {
              m_ValueBuffer[numValues] =
              attribute(m_IndicesBuffer[numValues]).parseDate(tokenizer.sval);
            } catch (ParseException e) {
              errms(tokenizer,"unparseable date: " + tokenizer.sval);
            }
            break;
          default:
            errms(tokenizer,"unknown attribute type in column " + m_IndicesBuffer[numValues]);
        }
      }
      numValues++;
    } while (true);
    if (flag) {
      getLastToken(tokenizer,true);
    }
    
    // Return current instance
    double[] tempValues = new double[numValues];
    int[] tempIndices = new int[numValues];
    System.arraycopy(m_ValueBuffer, 0, tempValues, 0, numValues);
    System.arraycopy(m_IndicesBuffer, 0, tempIndices, 0, numValues);
    
    return new SparseInstance(1, tempValues, tempIndices, numAttributes());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Reads a single instance using the tokenizer and appends it
   * to the dataset. Automatically expands the dataset if it
   * is not large enough to hold the instance.
   *
   * @param tokenizer the tokenizer to be used
   * @param flag if method should test for carriage return after
   * each instance
   * @return new instance or null if end of file has been reached
   * @exception IOException if the information is not read
   * successfully
   */
  protected Instance getInstanceFull(StreamTokenizer tokenizer,
  boolean flag) throws IOException {
    
    double[] instance = new double[numAttributes()];
    int index;
    
    // Get values for all attributes.
    for (int i = 0; i < numAttributes(); i++){
      
      // Get next token
      if (i > 0) {
        getNextToken(tokenizer);
      }
      
      // Check if value is missing.
      if  (tokenizer.ttype == '?') {
        instance[i] = Instance.missingValue();
      } else {
        
        // Check if token is valid.
        if (tokenizer.ttype != StreamTokenizer.TT_WORD) {
          errms(tokenizer,"not a valid value");
        }
        switch (attribute(i).type()) {
          case Attribute.NOMINAL:
            // Check if value appears in header.
            index = attribute(i).indexOfValue(tokenizer.sval);
            if (index == -1) {
              errms(tokenizer,"nominal value not declared in header");
            }
            instance[i] = (double)index;
            break;
          case Attribute.NUMERIC:
            // Check if value is really a number.
            try{
              instance[i] = Double.valueOf(tokenizer.sval).
              doubleValue();
            } catch (NumberFormatException e) {
              errms(tokenizer,"number expected");
            }
            break;
          case Attribute.STRING:
            instance[i] = attribute(i).addStringValue(tokenizer.sval);
            break;
          case Attribute.DATE:
            try {
              instance[i] = attribute(i).parseDate(tokenizer.sval);
            } catch (ParseException e) {
              errms(tokenizer,"unparseable date: " + tokenizer.sval);
            }
            break;
          default:
            errms(tokenizer,"unknown attribute type in column " + i);
        }
      }
    }
    if (flag) {
      getLastToken(tokenizer,true);
    }
    
    // Return current instance
    return new Instance(1, instance);
    
  }
  
  /* ########## rest is eqivalent to weka.core.Instances ########## */
  
  /**
   * Gets token and checks if its end of line.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if it doesn't find an end of line
   */
  private void getLastToken(StreamTokenizer tokenizer, boolean endOfFileOk)
  throws IOException {
    
    if ((tokenizer.nextToken() != StreamTokenizer.TT_EOL) &&
    ((tokenizer.ttype != StreamTokenizer.TT_EOF) || !endOfFileOk)) {
      errms(tokenizer,"end of line expected");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets next token, checking for a premature and of line.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if it finds a premature end of line
   */
  private void getNextToken(StreamTokenizer tokenizer)
  throws IOException {
    
    if (tokenizer.nextToken() == StreamTokenizer.TT_EOL) {
      errms(tokenizer,"premature end of line");
    }
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      errms(tokenizer,"premature end of file");
    } else if ((tokenizer.ttype == '\'') ||
    (tokenizer.ttype == '"')) {
      tokenizer.ttype = StreamTokenizer.TT_WORD;
    } else if ((tokenizer.ttype == StreamTokenizer.TT_WORD) &&
    (tokenizer.sval.equals("?"))){
      tokenizer.ttype = '?';
    }
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Initializes the StreamTokenizer used for reading the ARFF file.
   *
   * @param tokenizer the stream tokenizer
   */
  private void initTokenizer(StreamTokenizer pTokenizer){
    
    pTokenizer.resetSyntax();
    pTokenizer.whitespaceChars(0, ' ');
    pTokenizer.wordChars(' '+1,'\u00FF');
    pTokenizer.whitespaceChars(',',',');
    pTokenizer.commentChar('%');
    pTokenizer.quoteChar('"');
    pTokenizer.quoteChar('\'');
    pTokenizer.ordinaryChar('{');
    pTokenizer.ordinaryChar('}');
    pTokenizer.eolIsSignificant(true);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Reads and stores header of an ARFF file.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if the information is not read
   * successfully
   */
  protected void readHeader(StreamTokenizer tokenizer)
  throws IOException {
    
    String attributeName;
    FastVector attributeValues;
    int i;
    
    // Get name of relation.
    getFirstToken(tokenizer);
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      errms(tokenizer,"premature end of file");
    }
    if (ARFF_RELATION.equalsIgnoreCase(tokenizer.sval)) {
      getNextToken(tokenizer);
      m_RelationName = tokenizer.sval;
      getLastToken(tokenizer,false);
    } else {
      errms(tokenizer,"keyword " + ARFF_RELATION + " expected");
    }
    
    // Create vectors to hold information temporarily.
    m_Attributes = new FastVector();
    
    // Get attribute declarations.
    getFirstToken(tokenizer);
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      errms(tokenizer,"premature end of file");
    }
    
    while (Attribute.ARFF_ATTRIBUTE.equalsIgnoreCase(tokenizer.sval)) {
      
      // Get attribute name.
      getNextToken(tokenizer);
      attributeName = tokenizer.sval;
      getNextToken(tokenizer);
      
      // Check if attribute is nominal.
      if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
        
        // Attribute is real, integer, or string.
        if (tokenizer.sval.equalsIgnoreCase(Attribute.ARFF_ATTRIBUTE_REAL) ||
        tokenizer.sval.equalsIgnoreCase(Attribute.ARFF_ATTRIBUTE_INTEGER) ||
        tokenizer.sval.equalsIgnoreCase(Attribute.ARFF_ATTRIBUTE_NUMERIC)) {
          m_Attributes.addElement(new Attribute(attributeName, numAttributes()));
          readTillEOL(tokenizer);
        } else if (tokenizer.sval.equalsIgnoreCase(Attribute.ARFF_ATTRIBUTE_STRING)) {
          m_Attributes.
          addElement(new Attribute(attributeName, (FastVector)null,
          numAttributes()));
          readTillEOL(tokenizer);
        } else if (tokenizer.sval.equalsIgnoreCase(Attribute.ARFF_ATTRIBUTE_DATE)) {
          String format = null;
          if (tokenizer.nextToken() != StreamTokenizer.TT_EOL) {
            if ((tokenizer.ttype != StreamTokenizer.TT_WORD) &&
            (tokenizer.ttype != '\'') &&
            (tokenizer.ttype != '\"')) {
              errms(tokenizer,"not a valid date format");
            }
            format = tokenizer.sval;
            readTillEOL(tokenizer);
          } else {
            tokenizer.pushBack();
          }
          m_Attributes.addElement(new Attribute(attributeName, format,
          numAttributes()));
          
        } else {
          errms(tokenizer,"no valid attribute type or invalid "+
          "enumeration");
        }
      } else {
        
        // Attribute is nominal.
        attributeValues = new FastVector();
        tokenizer.pushBack();
        
        // Get values for nominal attribute.
        if (tokenizer.nextToken() != '{') {
          errms(tokenizer,"{ expected at beginning of enumeration");
        }
        while (tokenizer.nextToken() != '}') {
          if (tokenizer.ttype == StreamTokenizer.TT_EOL) {
            errms(tokenizer,"} expected at end of enumeration");
          } else {
            attributeValues.addElement(tokenizer.sval);
          }
        }
        if (attributeValues.size() == 0) {
          errms(tokenizer,"no nominal values found");
        }
        m_Attributes.
        addElement(new Attribute(attributeName, attributeValues,
        numAttributes()));
      }
      getLastToken(tokenizer,false);
      getFirstToken(tokenizer);
      if (tokenizer.ttype == StreamTokenizer.TT_EOF)
        errms(tokenizer,"premature end of file");
    }
    
    // Check if data part follows. We can't easily check for EOL.
    if (!ARFF_DATA.equalsIgnoreCase(tokenizer.sval)) {
      errms(tokenizer,"keyword " + ARFF_DATA + " expected");
    }
    
    // Check if any attributes have been declared.
    if (m_Attributes.size() == 0) {
      errms(tokenizer,"no attributes declared");
    }
    
    // Allocate buffers in case sparse instances have to be read
    m_ValueBuffer = new double[numAttributes()];
    m_IndicesBuffer = new int[numAttributes()];
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets next token, skipping empty lines.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if reading the next token fails
   */
  private void getFirstToken(StreamTokenizer tokenizer)
  throws IOException {
    
    while (tokenizer.nextToken() == StreamTokenizer.TT_EOL){};
    if ((tokenizer.ttype == '\'') ||
    (tokenizer.ttype == '"')) {
      tokenizer.ttype = StreamTokenizer.TT_WORD;
    } else if ((tokenizer.ttype == StreamTokenizer.TT_WORD) &&
    (tokenizer.sval.equals("?"))){
      tokenizer.ttype = '?';
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Gets index, checking for a premature and of line.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if it finds a premature end of line
   */
  private void getIndex(StreamTokenizer tokenizer) throws IOException {
    
    if (tokenizer.nextToken() == StreamTokenizer.TT_EOL) {
      errms(tokenizer,"premature end of line");
    }
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      errms(tokenizer,"premature end of file");
    }
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Throws error message with line number and last token read.
   *
   * @param theMsg the error message to be thrown
   * @param tokenizer the stream tokenizer
   * @throws IOExcpetion containing the error message
   */
  private void errms(StreamTokenizer tokenizer, String theMsg)
  throws IOException {
    
    throw new IOException(theMsg + ", read " + tokenizer.toString());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Reads and skips all tokens before next end of line token.
   *
   * @param tokenizer the stream tokenizer
   */
  private void readTillEOL(StreamTokenizer tokenizer)
  throws IOException {
    
    while (tokenizer.nextToken() != StreamTokenizer.TT_EOL) {};
    tokenizer.pushBack();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {
    
    SequentialInstanceReader testInstances = null;
    try {
      System.out.println("org/hypknowsys/algorithms/datasets/iris.arff");
      Tools.copyTextualSystemResource(
      "org/hypknowsys/algorithms/datasets/iris.arff", "temp.arff");
      File file = new File("temp.arff");
      testInstances = new SequentialInstanceReader(file, 5000000);
      testInstances.open();
      Instance instance = testInstances.getFirstInstance();
      while (instance != null) {
        System.out.println(instance.toString());
        instance = testInstances.getNextInstance();
      }
      testInstances.close();
      file.delete();
    }
    catch (IOException e) {
      System.exit(-1);
    }
    
    try {
      System.out.println("\n\n\norg/hypknowsys/algorithms/datasets/labor.arff");
      Tools.copyTextualSystemResource(
      "org/hypknowsys/algorithms/datasets/labor.arff", "temp.arff");
      File file = new File("temp.arff");
      testInstances = new SequentialInstanceReader(file, 5000000);
      testInstances.open();
      Instance instance = testInstances.getFirstInstance();
      while (instance != null) {
        System.out.println(instance.toString());
        instance = testInstances.getNextInstance();
      }
      testInstances.close();
      file.delete();
    }
    catch (IOException e) {
      System.exit(-1);
    }
    
  }
  
}