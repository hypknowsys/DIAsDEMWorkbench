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

package org.hypknowsys.misc.io;
 
import java.io.*;
import java.util.*;
 
/**
 * This class provides a basic environment to handle comma separated value
 * files. Consider the following comma separated value file test.csv: <p>
 * <pre>
 *
 * Name; Customer; Age; Revenue
 * String; boolean; int; double
 * "Miller"; true; 12; 120.50
 * "Smith \"the Magic\""; false; 99; 1000
 *
 * </pre>
 * The first line contains the names of the attributes of each tuple. The
 * second line contains the corresponding Java primitive types. The following
 * lines
 * contain one tuple per line. String values are enclosed in double quotation
 * marks. Note: Double quotation marks in Strings must be escaped using a
 * backslash. <p> <pre>
 *
 * // create an instance of CsvFile to handle the file
 * CsvFile myCsvFile = null;
 * // create an array of java.lang.Object to host the tuples
 * Object[][] values = null;
 * // create an array containing the attributes
 * String [] attributes = null;
 * // create an array containing the Class types
 * Class[] types = null;
 *
 * // create variables to be filled
 * String name = null;
 * boolean customer = false;
 * int age = 0;
 * double revenue = 0.0;
 *
 * <b>Possibility 1 </b> : open a file and write all contents into one array
 *
 * // syntax error raise an IOExceptions, so they must be catched
 * try {
 *
 *   // open the specified file
 *   // with the boolean type "false" you decided to write all values
 *   // into one array
 *
 *   myCsvFile = new CsvFile("/directory/CsvFileExample.csv", false);
 *
 *   // assign the CsvFile value tuple array to local array values
 *   values = myCsvFile.getValues();
 *
 *   // retrieve the first tuple
 *   name = (String)values[0][0];
 *   customer = ( (Boolean)values[1][0] ).booleanValue();
 *   age = ( (Integer)values[2][0] ).intValue();
 *   revenue = ( (Double)values[3][0] ).doubleValue();
 *
 *   // change name and age and save file  as a new CsvFile
 *   values[0][0] = "Watson";
 *   values[2][0] = new Integer(12);
 *
 *   myCsvFile.save(values, "/directory/newFile.csv");
 *   // this method allows you to save the array without mentioning
 *   // the attribute names - the default attribute names are
 *   // attribute1;attribute2; ...
 *
 *
 *   // if you want to mention the attribute names in the *.csv file
 *   // use the following method :
 *
 *   myCsvFile.save(values, attributes, "/directory/newFile.csv");
 *
 * }
 * catch (IOException e) { // do something }
 *
 * <b>Possibility 2 </b> : open a file and read the values step by step
 *
 * // syntax error raise an IOExceptions, so they must be catched
 
 * // create an instance of CsvFile to handle the file
 * CsvFile myCsvFile = null;
 * // create an array of java.lang.Object to host the tuples
 * Object[] values = null;
 * // create an array containing the the Class types
 * Class[] types = null;
 *
 * try {
 *
 *   // open the specified file
 *   // with the boolean type "true" you decided to write the values
 *   // of one tuple into one array
 *
 *   myCsvFile = new CsvFile("/directory/CsvFileExample.csv", true);
 *
 *   // afterwards you have to construct an array
 *
 *   values = new  Object [myCsvFile.countAttributes()];
 *
 *    // now load the tuples step by step
 *    values = myCsvFile.getNextTuple();
 *
 *    // after you finished reading do not forget to close file reading
 *
 *   myCsvFile.getCloseTuple();
 *
 * }
 * catch (IOException e) { // do something }
 *
 * // after changing some values you can save the new arrays step by step
 * // you can also decide to use your attribute names or to use default
 * // attribute names like attribute1; attribute2; ...
 *
 * ... without mentioning attribute names
 *
 * myCsvFile.setFirstTuple(types, "/directory/newFile.csv");
 *
 * ... with mentioning attribute names
 *
 * myCsvFile.setFirstTuple(attributes, types, "/directory/newFile.csv");
 *
 * // afterwards changing some values save the values (one dimension array)
 *
 * myCsvFile.setNextTuple(value1);
 * myCsvFile.setNextTuple(value2);
 *
 * // also you have to close the FileWriter
 *
 * myCsvFile.closeTuple();
 *
 *
 * </pre>
 *
 */


/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz
 */
  
public class CsvFile {
 
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private BufferedReader BufferedContent;
  private BufferedReader BufferedLine;
  private String FirstLine;
  private String SecondLine;
  private int AllLines = 0;  // NumberOfLines
  private Object Values[][];
  private Class AttributeTypes[];
  private String AttributeNames[];
  private int NumberOfTokens;
  private FileWriter CurrentFileWriter;
  private FileWriter TupleFileWriter;
  private int PNumberToken = 0;
  private int PNumberLine = 0;
  private String CommentLinePrefix = COMMENT_LINE_PREFIX;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient String TmpString = null;
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public static final String COMMENT_LINE_PREFIX = "#";

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * a Constructor for the instance for sequential reading
   *
   */
  
  public CsvFile(String pFileName, boolean sequential)
  throws java.io.IOException {
    
    if (sequential == true) {
      this.getFirstTuple(pFileName);
    }
    else {
      this.readFirstLine(pFileName);
    }
    CommentLinePrefix = COMMENT_LINE_PREFIX;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * a Constructor for the instance
   *
   */
  
  public CsvFile(File pFile, boolean sequential) throws java.io.IOException {
    
    if (sequential == true) {
      this.getFirstTuple( pFile.getAbsolutePath() );
    }
    else {
      this.readFirstLine( pFile.getAbsolutePath() );
    }
    CommentLinePrefix = COMMENT_LINE_PREFIX;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getCommentLinePrefix() { 
    return CommentLinePrefix; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setCommentLinePrefix(String pCommentLinePrefix) {
    this.CommentLinePrefix = pCommentLinePrefix; }

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
  
  /**
   * @return number of all lines from the datafile
   *
   */
  
  public int countAllLines() {
    
    return AllLines;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
    
  /**
   * @return number of attributes of each tuple
   *
   */
  
  public int countAttributes() {
    
    return NumberOfTokens;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
    
  /**
   * @return all attribute names given in the first line
   *
   */
  
  public String[] getAttributeNames() {
    
    return AttributeNames;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
    
  /**
   * @return attribute name at the specified index position
   *
   */
  
  public String getAttributeName(int index) {
    
    return AttributeNames[index];
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
    
  /**
   * @return all attribute types given in the second line
   *
   */
  
  public Class[] getAttributeTypes() {
    
    return AttributeTypes;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
    
  /**
   * @return attribute type at the specified index position
   *
   */
  
  public Class getAttributeType(int index) {
    
    return AttributeTypes[index];
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * @return all Values contained in the file wrapped in an array;
   * size: Oject[ this.getNumberOfTokens() ][ this.countValueLines() ]
   */
  
  public Object[][] getValues() {
    
    return this.Values;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * @return number of lines containing a tuple
   */
  
  public int countTuples() {
    
    int currentValues = (AllLines-2);
    
    return currentValues;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * @return number of Values (i.e., tuple lines multiplied by attributes)
   *
   */
  
  public int countValues() {
    
    int currentValues = ((AllLines-2) * NumberOfTokens);
    
    return currentValues;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method is for sequetial reading
   * @return a simple tuple in an Object []
   */
  
  public Object[] getNextTuple() {
    
    PNumberLine = 0;
    String remainLine;
    Object[] tmpValues = new Object[NumberOfTokens];
    
    if ((remainLine = getReadedLine()) != null) {
      setValues(remainLine);
    }
    else if ((remainLine = getReadedLine()) == null) {
      for (int i = 0; i < NumberOfTokens; i ++) {
        Values[i][0] = null;
      }
    }
    for (int i = 0; i < NumberOfTokens; i ++) {
      tmpValues[i] = Values[i][0];
    }
    return tmpValues;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * saves the attribute names and the classes in the *.csv datafile with
   * mentioning the attribute names
   */
  
  public void setFirstTuple(String[] names, Class[] pClasses,
  String currentName)  throws java.io.IOException {
    
    String tmpArray;
    String currentNewArray = "";
    TupleFileWriter = new FileWriter(currentName);
    
    for (int i = 0;i < names.length;i++) {
      currentNewArray = new StringBuffer().append(currentNewArray)
      .append(names[i]).toString();
      if (i < names.length-1) {
        currentNewArray = new StringBuffer().
        append(currentNewArray).append(";").toString();
      }
    }
    currentNewArray = new StringBuffer().append(currentNewArray).append
    (System.getProperty("line.separator")).toString();
    
    for (int i = 0;i < pClasses.length;i++) {
      
      String tmpClasses = pClasses[i].toString();
      currentNewArray = new StringBuffer().append(currentNewArray)
      .append(saveRightClass(tmpClasses)).toString();
      if (i < pClasses.length-1) {
        currentNewArray = new StringBuffer().append(currentNewArray).
        append(";").toString();
      }
    }
    currentNewArray = new StringBuffer().append(currentNewArray).
    append(System.getProperty("line.separator")).toString();
    TupleFileWriter.write(currentNewArray.toString());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * saves the attribute names and the classes in the *.csv datafile without
   * mentioning the attribute names
   *
   */
  
  public void setFirstTuple(Class[] pClasses, String currentName)
  throws java.io.IOException {
    
    String tmpArray;
    String currentNewArray = "";
    TupleFileWriter = new FileWriter(currentName);
    
    for (int i = 0;i < pClasses.length;i++) {
      currentNewArray = new StringBuffer().append(currentNewArray).
      append("attribute").append(i).toString();
      if (i < pClasses.length-1) {
        currentNewArray = new StringBuffer().append(currentNewArray).
        append(";").toString();
      }
    }
    currentNewArray = new StringBuffer().append(currentNewArray)
    .append(System.getProperty("line.separator")).toString();
    
    for (int i = 0;i < pClasses.length;i++) {
      
      String tmpClasses = pClasses[i].toString();
      currentNewArray = new StringBuffer().append(currentNewArray)
      .append(saveRightClass(tmpClasses)).toString();
      if (i < pClasses.length-1) {
        currentNewArray = new StringBuffer().append(currentNewArray).
        append(";").toString();
      }
    }
    currentNewArray = new StringBuffer().append(currentNewArray)
    .append(System.getProperty("line.separator")).toString();
    TupleFileWriter.write(currentNewArray.toString());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * saves a simple tuple (see description at the beginning)
   *
   */
  
  public void setNextTuple(Object[] pTuple)
  throws java.io.IOException {
    
    String currentNewArray = "";
    
    for (int i = 0; i < pTuple.length ; i++) {
      currentNewArray = new StringBuffer().append(currentNewArray)
      .append(saveRightString(pTuple[i])).toString();
      if (i < pTuple.length-1) {
        currentNewArray = new StringBuffer().append(currentNewArray)
        .append(";").toString();
      }
    }
    currentNewArray = new StringBuffer().append(currentNewArray)
    .append(System.getProperty("line.separator")).toString();
    TupleFileWriter.write(currentNewArray.toString());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * saves a comment in the *.csv datafile (commented in the *.csv file
   * like # my new comment)
   */
  
  public void setComment(String pComment)
  throws java.io.IOException {
    
    TupleFileWriter.write(CommentLinePrefix + " " + pComment);
    TupleFileWriter.write(System.getProperty("line.separator"));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   *
   * method closes the tuple reading
   *
   */
  
  public void getCloseTuple() throws java.io.IOException {
    
    BufferedContent.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * saves the content of the given tuple array as a new file <br>
   * transfer the array[][] and the name under which name it has to be saved
   */
  
  public void save(Object[][] pAttributeValues, String pFileName)
  throws java.io.IOException {
    
    String[] AttributeNames = new String[pAttributeValues.length];
    for (int i = 0; i < AttributeNames.length;i++)
      AttributeNames[i] = "Attribute" + i;
    this.save(pAttributeValues, AttributeNames, pFileName);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * saves the content of the given tuple array as a new file <br>
   * transfer the array[][], the attribute names[] and the name under which
   * name it has to be saved
   */
  
  public void save(Object[][] pAttributeValues, String[] pAttributeNames,
  String pFileName) throws java.io.IOException {
    
    String rightClass = null;
    StringBuffer lineBuffer = new StringBuffer(10000);
    String lineSeparator = System.getProperty("line.separator");
    CurrentFileWriter = new FileWriter(pFileName);
    
    for (int i = 0; i < pAttributeNames.length; i++) {
      lineBuffer.append(pAttributeNames[i]);
      if (i < pAttributeValues.length-1)
        lineBuffer.append(";");
    }
    lineBuffer.append(lineSeparator);
    
    for (int i = 0;i < pAttributeValues.length;i++) {
      rightClass = "";
      if (pAttributeValues[i][0] == null)
        rightClass = "Object";
      else
        rightClass = saveRightClass(
        pAttributeValues[i][0].getClass().toString() );
      lineBuffer.append(rightClass);
      if (i < pAttributeValues.length-1)
        lineBuffer.append(";");
    }
    lineBuffer.append(lineSeparator);
    
    for (int i = 0;i < pAttributeValues[0].length;i++) {
      for (int j = 0;j < pAttributeValues.length;j++) {
        lineBuffer.append(saveRightString(pAttributeValues[j][i]));
        if (j < pAttributeValues.length-1)
          lineBuffer.append(";");
      }
      lineBuffer.append(lineSeparator);
      CurrentFileWriter.write(lineBuffer.toString());
      lineBuffer = new StringBuffer(10000);
    }
    
    CurrentFileWriter.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * close the tuple writer
   *
   */
  
  public void closeTuple() throws java.io.IOException {
    
    TupleFileWriter.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * ATTENTION : you have to open the datafile twice because of initializing
   * the array for the Values
   * @return number of all lines in the datafile
   */
  
  private int getNumberOfLines(String pfileNameForLine)
  throws java.io.IOException {
    
    String getLineNumber;
    BufferedLine = new BufferedReader(new FileReader(pfileNameForLine));
    
    while (getNumberReadedLine() != null) {
      AllLines++;
    }
    BufferedLine.close();
    
    return AllLines;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * returns the next readed line (seperated because allowing a benchmark)
   * remark : this method is only for method private int getNumberOfLines
   *
   */
  
  private String getNumberReadedLine() {
    
    boolean done = false;
    String currentLine = "";
    
    while (!done) {
      try {
        currentLine = BufferedLine.readLine();
      }
      catch (IOException e) {
        System.out.println("Fehler beim Lesen der Datei");
      }
      if (currentLine == null) {
        done = true;
        return currentLine;
      }
      Character tmpChar = new Character(currentLine.charAt(0));
      String currentTmpChar = tmpChar.toString();
      if (currentTmpChar.equals(CommentLinePrefix)) {
        done = false;
      }
      else {
        done = true;
      }
    }
    return currentLine;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * this method sets the right type for each content
   *
   */
  
  private Object getRightType(int pcurrentnumberToken, String simplyTokens) {
    
    Object currentObject = null;
    
    if (simplyTokens.equals("null")) {
    }
    else if (simplyTokens.equals("Null")) {
    }
    else if (AttributeTypes[pcurrentnumberToken].toString().equals
    ("class java.lang.String")) {
      currentObject = (new String(simplyTokens));
    }
    else if (AttributeTypes[pcurrentnumberToken].toString().equals
    ("class java.lang.Integer")) {
      currentObject = (new Integer(simplyTokens));
    }
    else if (AttributeTypes[pcurrentnumberToken].toString().equals
    ("class java.lang.Boolean")) {
      currentObject = (new Boolean(simplyTokens));
    }
    else if (AttributeTypes[pcurrentnumberToken].toString().equals
    ("class java.lang.Double")) {
      currentObject = (new Double(simplyTokens));
    }
    else if (AttributeTypes[pcurrentnumberToken].toString().equals
    ("class java.lang.Long")) {
      currentObject = (new Long(simplyTokens));
    }
    else if (AttributeTypes[pcurrentnumberToken].toString().equals
    ("class java.lang.Float")) {
      currentObject = (new Float(simplyTokens));
    }
    else if (AttributeTypes[pcurrentnumberToken].toString().equals
    ("class java.lang.Object")) {
      currentObject = simplyTokens;
    }
    
    
    return currentObject;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * sets (load) the content of the remaining lines
   *
   */
  
  private void setContent()
  throws java.io.IOException {
    
    String remainLine;
    
    while ((remainLine = getReadedLine()) != null) {
      setValues(remainLine);
    }
    BufferedContent.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * gets a line and sets the values
   *
   */
  
  
  private void setValues(String remainLine) {
    
    int controler = 0;
    String simplyTokens = "";
    
    for (int i = 0;i < remainLine.length() ;i++) {
      
      switch ((char)remainLine.charAt(i)) {
        case (char)'\"': {
          if (controler == 1 ) {
            
            Values [PNumberToken][PNumberLine]
            = getRightType(PNumberToken, simplyTokens);
            PNumberToken = PNumberToken + 1;
            simplyTokens = "";
            controler = 0;
            break;
          }
          else controler = 1;
          break;
        }
        case (char)';': {
          if ( controler == 0) {
            if (simplyTokens != "") {
              
              Values [PNumberToken][PNumberLine]
              = getRightType(PNumberToken, simplyTokens);
              PNumberToken = PNumberToken + 1;
              simplyTokens = "";
              break;
            }
          }
          else  simplyTokens = new StringBuffer().append(
          simplyTokens).append(remainLine.charAt(i)).toString();
          break;
        }
        case (char)'\\': {
          if ((char)remainLine.charAt(i+1) == 34) {
            simplyTokens = new StringBuffer().append(simplyTokens).
            append(remainLine.charAt(i+1)).toString();
            i = i + 1;
          }
          else simplyTokens = new StringBuffer().append(simplyTokens).
          append(remainLine.charAt(i)).toString();
          break;
        }
        default: {
          simplyTokens = new StringBuffer().append(simplyTokens).
          append(remainLine.charAt(i)).toString();
          break;
        }
        
      }
    }
    if (simplyTokens != "" ) {
      
      Values [PNumberToken][PNumberLine]
      = getRightType(PNumberToken, simplyTokens);
      PNumberToken = PNumberToken + 1;
      simplyTokens = "";
    }
    PNumberLine = PNumberLine + 1;
    PNumberToken = 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * returns the array names mentioned in the first line
   *
   */
  
  private void setAttributeNames(String getFirstLine, int getAllLines) {
    
    int i = 0;
    
    StringTokenizer getTokensFirstLine =
    new StringTokenizer(getFirstLine, ";");
    while(getTokensFirstLine.hasMoreElements()) {
      AttributeNames[i] = getTokensFirstLine.nextToken();
      i++;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * returns the types of the array mentioned
   * in the second line
   *
   */
  
  private void setAttributeTypes(String getSecondLine, int getAllLines) {
    
    int i = 0;
    
    StringTokenizer getTokensSecondLine =
    new StringTokenizer(getSecondLine, ";");
    while(getTokensSecondLine.hasMoreElements()) {
      String currentToken = getTokensSecondLine.nextToken();
      if (currentToken.equals("String")) {
        AttributeTypes[i] = (new String(currentToken)).getClass();
      }
      else if (currentToken.equals("Integer")) {
        AttributeTypes[i] = (new Integer(0)).getClass();
      }
      else if (currentToken.equals("int")) {
        AttributeTypes[i] =  (new Integer(0)).getClass();
      }
      else if (currentToken.equals("Boolean")) {
        AttributeTypes[i] =  (new Boolean(null)).getClass();
      }
      else if (currentToken.equals("boolean")) {
        AttributeTypes[i] =  (new Boolean(null)).getClass();
      }
      else if (currentToken.equals("Double")) {
        AttributeTypes[i] =  (new Double(0.00)).getClass();
      }
      else if (currentToken.equals("double")) {
        AttributeTypes[i] =  (new Double(0.00)).getClass();
      }
      else if (currentToken.equals("Long")) {
        AttributeTypes[i] =  (new Long(0)).getClass();
      }
      else if (currentToken.equals("long")) {
        AttributeTypes[i] =  (new Long(0)).getClass();
      }
      else if (currentToken.equals("float")) {
        AttributeTypes[i] =  (new Float(0)).getClass();
      }
      else if (currentToken.equals("Float")) {
        AttributeTypes[i] =  (new Float(0)).getClass();
      }
      
      else if (currentToken.equals("Object")) {
        AttributeTypes[i] = (new String(currentToken)).getClass();
        AttributeTypes[i] =  AttributeTypes[i].getSuperclass();
      }
      i++;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * sets the number of tokens mentioned in the first line
   * called numberOfTokens
   *
   */
  
  private void setNumberOfTokens(String stringToBeTokenized) {
    
    StringTokenizer getTheNumberOfTokens =
    new StringTokenizer(stringToBeTokenized, ";");
    NumberOfTokens = getTheNumberOfTokens.countTokens();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * gets an array and returns a String with the right conventions
   *
   */
  
  private String saveRightString(Object pAttributeValue) {
    
    if (pAttributeValue == null)
      return "null";
    
    TmpStringBuffer = new StringBuffer(1000);  // result String
    TmpString = ""; // string represenation of pAttributeValue
    boolean isString = false;
    
    if (pAttributeValue instanceof java.lang.String) {
      TmpStringBuffer.append("\"");
      isString = true;
    }
    else
      isString = false;
    
    TmpString = pAttributeValue.toString();
    for (int k = 0;k < TmpString.length();k++) {
      switch ((char) TmpString.charAt(k)) {
        case (char) '"' : {
          TmpStringBuffer.append("\\");
        }
        default : {
          TmpStringBuffer.append(TmpString.charAt(k));
        }
      }
      
    }
    if (isString)
      TmpStringBuffer.append("\"");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * gets an array and returns the right class
   *
   */
  
  private String saveRightClass(String pAttributeValue) {
    
    String returnClass = "";
    
    if (pAttributeValue.equals("class java.lang.String")) {
      returnClass = "String";
    }
    else if (pAttributeValue.equals("class java.lang.Integer")) {
      returnClass = "int";
    }
    else if (pAttributeValue.equals("class java.lang.Boolean")) {
      returnClass = "boolean";
    }
    else if (pAttributeValue.equals("class java.lang.Double")) {
      returnClass = "double";
    }
    else if (pAttributeValue.equals("class java.lang.Long")) {
      returnClass = "long";
    }
    else if (pAttributeValue.equals("class java.lang.Float")) {
      returnClass = "float";
    }
    
    return returnClass;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * 1. Step returns the first line of a data file called firstLine
   * 2. Step returns the second line of a datafile called nextLine
   * 3. Step returns the number of all lines of a datafile called AllLines
   * 4. Step returns the number of the splitted tokens called numberOfTokens
   * 5. Step initialize the Object Names with the number of the tokens
   */
  
  private void readFirstLine(String pfileName)
  throws java.io.IOException {
    
    BufferedContent = new BufferedReader( new FileReader(pfileName));
    FirstLine = getReadedLine();
    readSecondLine();
    getNumberOfLines(pfileName);
    setNumberOfTokens(FirstLine);
    initializeContent(NumberOfTokens, AllLines);
    setAttributeNames(FirstLine, AllLines);
    setAttributeTypes(SecondLine, AllLines);
    setContent();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method is for sequetial reading
   * 1. Step returns the first line of a data file called firstLine
   * 2. Step returns the second line of a datafile called nextLine
   * 3. Step returns the number of all lines of a datafile called AllLines
   * 4. Step returns the number of the splitted tokens called numberOfTokens
   * 5. Step initialize the Object Names with the number of the tokens
   */
  
  private void getFirstTuple(String pfileName)
  throws java.io.IOException {
    
    BufferedContent = new BufferedReader( new FileReader(pfileName));
    FirstLine = getReadedLine();
    readSecondLine();
    getNumberOfLines(pfileName);
    setNumberOfTokens(FirstLine);
    initializeContent(NumberOfTokens, AllLines);
    setAttributeNames(FirstLine, AllLines);
    setAttributeTypes(SecondLine, AllLines);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * returns the second line of a data file
   *
   */
  
  private String readSecondLine() throws java.io.IOException {
    
    SecondLine = getReadedLine();
    
    return SecondLine;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * returns the next readed line (seperated because allowing a benchmark
   *
   */
  
  private String getReadedLine() {
    
    boolean done = false;
    String currentLine = "";
    
    while (!done) {
      try {
        currentLine = BufferedContent.readLine();
      }
      catch (IOException e) {
        System.out.println("Fehler beim Lesen der Datei");
      }
      
      if (currentLine == null) {
        done = true;
        return currentLine;
      }
      
      Character tmpChar = new Character(currentLine.charAt(0));
      String currentTmpChar = tmpChar.toString();
      if (currentTmpChar.equals("#")) {
        done = false;
      }
      else {
        done = true;
      }
    }
    
    return currentLine;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * initializes the Object and the Class with the number of
   * the tokens
   *
   */
  
  private void initializeContent(int numberToken, int numberLines) {
    
    AttributeNames = new String[numberToken];
    AttributeTypes = new Class[numberToken];
    
    Values = new Object[numberToken][numberLines-2];
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main (String [] args) {
 
    CsvFile myCsvFile = null;
    String[] AttributeNames = null;
    Object Values[][];
 
      try {
          myCsvFile = new CsvFile("./CsvFileExample.csv", false);
          AttributeNames = myCsvFile.getAttributeNames();
            Values = myCsvFile.getValues();
            for (int i = 0; i < Values[i].length; i++) {
              for (int j = 0; j < Values.length; j++) {
                System.out.print(AttributeNames[j] + " = ");
                System.out.println(Values[j][i]);
              }
            }
          int intExample = ( (Integer)Values[2][0] ).intValue();
          Values[2][0] = new Integer(456);
      }
      catch (IOException e) {
        System.out.println("Error opening file");
      }
 
  }
   
}