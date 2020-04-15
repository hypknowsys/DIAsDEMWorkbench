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

package org.hypknowsys.diasdem.core.neex.util;

import java.io.IOException;
import org.hypknowsys.misc.io.CsvFile;
import org.hypknowsys.misc.util.Tools;

/**
 * This class constructs a combination of placeholders for you to make
 * datamining on a file and identify for examples names (cf.
 * <a href ="http://diasdem.hhl.de"> Diasdem Project</a>)
 * <p>
 * For example:
 * <b>First Step </b> you have to create a csv.file for example called
 * <b>pattern.txt</b> with the following contents (the basic pattern):
 *
 * <pre>
 * content
 * String
 * "&lt;&lt;surname&gt;&gt;"
 * "&lt;&lt;name&gt;&gt;"
 * "&lt;&lt;town&gt;&gt;"
 * </pre>
 *
 * <b>Second step </b> you have to create a csv.file for example called
 * <b>sample.txt</B> containing the basic combinations you are searching for :
 *
 * <pre>
 * Content;content2
 * String;String
 * "&lt;&lt;name&gt;&gt; , &lt;&lt;surname&gt;&gt; , born , 
 *    &lt;&lt;birthday&gt;&gt;";"person1"
 * "&lt;&lt;surname&gt;&gt; , &lt;&lt;town&gt;&gt; , lives there";"person2"
 * </pre>
 *
 * When you start the program with "java PatternCreator" first you have type in
 * the file you want to load :  here our example <b>pattern.txt</b>
 * <p>
 * After pressing return you have to type in the name for the next datafile
 * (our example <b>sample.txt</b>
 *
 * Last but not least you have type in a file  name for the output after
 * computing : let us suggest <b>output</b>
 * <p>
 *
 * After computing you receive some messages and the combinations
 * necessaery for the diasdem datamining process stored in the file
 * we called <b>output.csv</b>
 *
 * <pre>
 * attribute0;attribute1
 * String;String
 * "&lt;&lt;name&gt;&gt; , &lt;&lt;surname&gt;&gt; , born , 
 *     &lt;&lt;birthday&gt;&gt;";"person1"
 * "&lt;&lt;name&gt;&gt; , &lt;&lt;surname name&gt;&gt; , born , 
 *     &lt;&lt;birthday&gt;&gt;";"person1"
 * "&lt;&lt;name&gt;&gt; , &lt;&lt;surname town&gt;&gt; , born , 
 *     &lt;&lt;birthday&gt;&gt;";"person1"
 * "&lt;&lt;name&gt;&gt; , &lt;&lt;surname name town&gt;&gt; , born , 
 *     &lt;&lt;birthday&gt;&gt;";"person1"
 * "&lt;&lt;surname name&gt;&gt; , &lt;&lt;surname&gt;&gt; , born , 
 *     &lt;&lt;birthday&gt;&gt;";"person1"
 * "&lt;&lt;name town&gt;&gt; , &lt;&lt;surname&gt;&gt; , born , 
 *     &lt;&lt;birthday&gt;&gt;";"person1"
 * "&lt;&lt;surname name town&gt;&gt; , &lt;&lt;surname&gt;&gt; , born , 
 *     &lt;&lt;birthday&gt;&gt;";"person1"
 * "&lt;&lt;surname&gt;&gt; , &lt;&lt;town&gt;&gt; , lives there";
 *     "person2"
 * "&lt;&lt;surname&gt;&gt; , &lt;&lt;surname town&gt;&gt; , lives there";
 *     "person2"
 * "&lt;&lt;surname&gt;&gt; , &lt;&lt;name town&gt;&gt; , lives there";
 *     "person2"
 * "&lt;&lt;surname&gt;&gt; , &lt;&lt;surname name town&gt;&gt; , lives there";
 *     "person2"
 * "&lt;&lt;surname name&gt;&gt; , &lt;&lt;town&gt;&gt; , lives there";
 *     "person2"
 * "&lt;&lt;surname town&gt;&gt; , &lt;&lt;town&gt;&gt; , lives there";
 *     "person2"
 *"&lt;&lt;surname name town&gt;&gt; , &lt;&lt;town&gt;&gt; , lives there";
 *     "person2"
 * </pre>
 *
 * Ok so enjoy DIAsDEM !!! :-)
 *
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class PatternCreator {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private CsvFile MyCsvFile = null;
  private Combinator MyCombinator = null;
  
  // "<<place>>"
  // "<<forename>>"
  // "<<surname>>"
  private Object BasicNEs[][];
  private int NumberOfBasicNEs = 0;
  private String BasicNeFileName = null;
  
  // "<<forename>> <<surname>> ";
  // "person( 1 , 0 , null , null , null , null , null )"
  private Object InitialCompositeNEs[][];
  private int NumberOfInitialCompositeNEs = 0;
  private String InitialCompositeNeFileName = null;
  
  // "<<forename>> <<surname>> ";
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename>> <<place surname>>";
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename>> <<forename surname>>";
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename>> <<place forename surname>>";
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<place forename>> <<surname>>";
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename surname>> <<surname>>";
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<place forename surname>> <<surname>>";
  // "person( 1 , 0 , null , null , null , null , null )"
  private Object ExtendedCompositeNEs[][];
  private String ExtendedCompositeNeFileName = null;
  
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
  
  public PatternCreator()  {
    
    BasicNeFileName =
    Tools.readString("Enter existing BasicNE file name: ");
    // "/home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/heins/"
    // + "BasicNE.csv";
    InitialCompositeNeFileName =
    Tools.readString("Enter existing DgapSourceCompositeNE file name: ");
    // "/home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/heins/"
    // + "DgapSourceCompositeNE.csv";
    ExtendedCompositeNeFileName =
    Tools.readString("Enter DgapExtendedCompositeNE file name to be created:");
    // "/home/kwinkler/diasdem/DIAsDEM.workbench2/data/samples/de/heins/"
    // + "DgapExtendedCompositeNE.csv";
    
    try {
      MyCsvFile = new CsvFile(BasicNeFileName, false);
      BasicNEs = MyCsvFile.getValues();
      NumberOfBasicNEs = MyCsvFile.countTuples();
    }
    catch (IOException e) {
      System.out.println("Error: Cannot open BasicNeFileName ...");
    }
    
    try {
      MyCsvFile = new CsvFile(InitialCompositeNeFileName, false);
      InitialCompositeNEs = MyCsvFile.getValues();
      NumberOfInitialCompositeNEs = MyCsvFile.countTuples();
    }
    catch (IOException e) {
      System.out.println("Error: Cannot open InitialCompositeNeFileName ...");
    }
    
    this.extendInitialCompositeNEs();
    ExtendedCompositeNEs = MyCombinator.ExtendedCompositeNEs;
    
    try {
      if (!ExtendedCompositeNeFileName.endsWith(".csv")) {
        ExtendedCompositeNeFileName += ".csv";
      }
      MyCsvFile.save(ExtendedCompositeNEs, ExtendedCompositeNeFileName);
      // System.out.println(
      //   "I successfully created the extended composite NE file "
      //   + ExtendedCompositeNeFileName);
    }
    catch (IOException e) {
      System.out.println("Error: Cannot write ExtendedCompositeNeFileName ...");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public PatternCreator(String pBasicNeFileName,
  String pInitialCompositeNeFileName, String pExtendedCompositeNeFileName)  {
    
    // same method as Constructor PatterCreator() + entendCompositeNeFile()
    
    BasicNeFileName = pBasicNeFileName;
    InitialCompositeNeFileName = pInitialCompositeNeFileName;
    ExtendedCompositeNeFileName = pExtendedCompositeNeFileName;
    
    try {
      MyCsvFile = new CsvFile(BasicNeFileName, false);
      BasicNEs = MyCsvFile.getValues();
      NumberOfBasicNEs = MyCsvFile.countTuples();
    }
    catch (IOException e) {
      System.out.println("Error: Cannot open BasicNeFileName ...");
    }
    
    try {
      MyCsvFile = new CsvFile(InitialCompositeNeFileName, false);
      InitialCompositeNEs = MyCsvFile.getValues();
      NumberOfInitialCompositeNEs = MyCsvFile.countTuples();
    }
    catch (IOException e) {
      System.out.println("Error: Cannot open InitialCompositeNeFileName ...");
    }
    
  }
  
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
  
  public void extendCompositeNeFile()  {
    
    // same method as Constructor PatterCreator() + entendCompositeNeFile()
    
    this.extendInitialCompositeNEs();
    ExtendedCompositeNEs = MyCombinator.ExtendedCompositeNEs;
    
    try {
      if (!ExtendedCompositeNeFileName.endsWith(".csv")) {
        ExtendedCompositeNeFileName += ".csv";
      }
      MyCsvFile.save(ExtendedCompositeNEs, ExtendedCompositeNeFileName);
      // System.out.println(
      //   "I successfully created the extended composite NE file "
      //   + ExtendedCompositeNeFileName);
    }
    catch (IOException e) {
      System.out.println("Error: Cannot write ExtendedCompositeNeFileName ...");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method calls the class Constructor and transfer the all necessary
   * values for computing the combinations
   */
  
  private void extendInitialCompositeNEs() {
    
    MyCombinator = new Combinator();
    
    String initialCompositeNE = null;
    String compositeNeConstructor = null;
    for (int i = 0; i < NumberOfInitialCompositeNEs ; i ++) {
      initialCompositeNE = InitialCompositeNEs[0][i].toString();
      compositeNeConstructor = InitialCompositeNEs[1][i].toString();
      MyCombinator.setPatternArray(BasicNEs, NumberOfBasicNEs,
      initialCompositeNE, compositeNeConstructor);
      MyCombinator.reset();
    }
    MyCombinator.copyArray();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String [] pOptions) {
    
    PatternCreator myPatternCreator = new PatternCreator();
    
  }
  
}