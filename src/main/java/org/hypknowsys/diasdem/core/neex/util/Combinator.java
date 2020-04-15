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

import java.util.StringTokenizer;
import java.util.Vector;
import org.hypknowsys.diasdem.core.neex.NamedEntity;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz
 */

public class Combinator {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  // "<<place>>"
  // "<<forename>>"
  // "<<surname>>"
  private Object BasicNEs[][] = null;
  private int NumberOfBasicNEs = 0;
  private Object IdentifierOfBasicNEs[] = null;
  
  // "<<forename>> <<surname>> "
  // "person( 1 , 0 , null , null , null , null , null )"
  private Object ParsedInitialCompositeNE[][];
  private String InitialCompositeNE = "";
  private String CompositeNeConstructor = "";
  private String NamedEntityPrefix = NamedEntity.PLACEHOLDER_PREFIX;
  private String NamedEntitySuffix = NamedEntity.PLACEHOLDER_SUFFIX;
  
  // "<<forename>> <<surname>> "
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename>> <<place surname>>"
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename>> <<forename surname>>"
  // ...
  private Vector FlatExtendedCompositeNEs = new Vector();
  // "<<forename>> <<surname>> "
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename>> <<place surname>>"
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename>> <<forename surname>>"
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename>> <<place forename surname>>"
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<place forename>> <<surname>>"
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<forename surname>> <<surname>>"
  // "person( 1 , 0 , null , null , null , null , null )"
  // "<<place forename surname>> <<surname>>"
  // "person( 1 , 0 , null , null , null , null , null )"
  public Object ExtendedCompositeNEs[][] = null;
  
  // 1 "<<place>>"
  // 2 "<<forename>>"
  // 3 "<<surname>>"
  // AllCombinationsOfBasicNEs: 1, 2, 3,
  private Vector AllCombinationsOfBasicNEs = new Vector();
  
  private int IndexAllCombinationsOfBasicNEs = 0;
  private Vector[] SpecificCombinationsOfBasicNEs;
  private InitialCompositeNeParser MyInitialCompositeNeParser =
  new InitialCompositeNeParser();
  private String BasicNEsInInitialCompositeNE;
  
  private Vector MCombination;
  private Vector NumberResult = new Vector();
  private NumberCombinator GetPatterns = new NumberCombinator();
  
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
  
  public Combinator() {}
  
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
  
  public void reset() {
    
    IdentifierOfBasicNEs = null;
    AllCombinationsOfBasicNEs.removeAllElements();
    
    IndexAllCombinationsOfBasicNEs = 0;
    BasicNEs = null;
    for (int i = 0; i < NumberOfBasicNEs; i ++) {
      SpecificCombinationsOfBasicNEs[i].removeAllElements();
    }
    BasicNEsInInitialCompositeNE = "";
    if (MCombination != null) {
      MCombination.removeAllElements();
    }
    if (NumberResult != null) {
      NumberResult.removeAllElements();
    }
    ExtendedCompositeNEs = null;
    InitialCompositeNE = "";
    ParsedInitialCompositeNE = null;
    CompositeNeConstructor = "";
    MyInitialCompositeNeParser.reset();
    if (GetPatterns != null) {
      GetPatterns.reset();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setPatternArray(Object pBasicNEs[][], int pNumberOfBasicNEs,
  String pInitialCompositeNE, String pCompositeNeConstructor) {
    
    BasicNEs = pBasicNEs;
    NumberOfBasicNEs = pNumberOfBasicNEs ;
    InitialCompositeNE = pInitialCompositeNE;
    CompositeNeConstructor = pCompositeNeConstructor;
    this.initializeIdentifierOfBasicNEs();
    this.setFirstBasicNeCombinations();
    this.setNextBasicNeCombinations();
    
    // != this.IndexAllCombinationsOfBasicNEs
    int indexAllCombinationsOfBasicNEs = 0;
    while (indexAllCombinationsOfBasicNEs
    < (AllCombinationsOfBasicNEs.size() - NumberOfBasicNEs)) {
      // System.out.println("");
      this.setRestCombination(indexAllCombinationsOfBasicNEs);
      indexAllCombinationsOfBasicNEs ++;
    }
    
    // debug
    // SpecificCombinationsOfBasicNEs() kombiniert : 12 13 123
    SpecificCombinationsOfBasicNEs = new Vector [NumberOfBasicNEs];
    for (int i = 0; i < NumberOfBasicNEs; i++) {
      SpecificCombinationsOfBasicNEs [i] = new Vector();
    }
    this.createSpecificCombinationsOfBasicNEs();
    // this.showSpecificCombinationsOfBasicNEs();
    
    BasicNEsInInitialCompositeNE = MyInitialCompositeNeParser
    .getBasicNEsInInitialCompositeNE(BasicNEs, InitialCompositeNE);
    // System.out.println("BasicNEsInInitialCompositeNE = #"
    //   + BasicNEsInInitialCompositeNE + "#");
    
    
    if (BasicNEsInInitialCompositeNE == null) {
      FlatExtendedCompositeNEs.addElement(InitialCompositeNE);
      FlatExtendedCompositeNEs.addElement(CompositeNeConstructor);
    }
    else {
      // BasicNEsInInitialCompositeNE: String das z.B. lautet 121 und
      // bedeutet es ist das 1., 2., 1. Basic Pattern enthalten
      // ParsedInitialCompositeNE : beinhaltet die einzelnen InitialCompositeNE
      // der Zeile, ist ein Objekt, mit i und in [0] steht der Wert drin (Sitz),
      // in [2] true oder false und in [3] steht z.B. 2 f�r 2. BasicPattern
      ParsedInitialCompositeNE = MyInitialCompositeNeParser
      .getParsedInitialCompositeNE();
      //this.showParsedInitialCompositeNE();
      
      if (BasicNEsInInitialCompositeNE.length() == 1) {
        NumberResult.removeAllElements();
        NumberResult = SpecificCombinationsOfBasicNEs[
        (new Integer(BasicNEsInInitialCompositeNE)).intValue() - 1];
      }
      else {
        //mcombination = getPatterns.getCombination(
        //  (BasicNEsInInitialCompositeNE.length()-1),
        //  (SpecificCombinationsOfBasicNEs[0].size() - 1));
        //System.out.println("BasicNEsInInitialCompositeNE.length()
        //  = " + BasicNEsInInitialCompositeNE.length());
        //System.out.println("SpecificCombinationsOfBasicNEs[0].size()
        //  = " + SpecificCombinationsOfBasicNEs[0].size());
        MCombination = GetPatterns.getCombination(
        BasicNEsInInitialCompositeNE.length(),
        SpecificCombinationsOfBasicNEs[0].size());
        // this.showVector(mcombination);
        combineMainVector();
      }
      
      combinePatterns();
      // System.out.println("");
      // this.showVector(numberResult);
      this.copyArray();
    }
    
    // debug
    // System.out.println("Current initial composite NE: "+InitialCompositeNE);
    // System.out.println("Number of extended composite NEs: "
    //   + FlatExtendedCompositeNEs.size() / 2);
    
  }
  
  
  public String getBasicNEsInInitialCompositeNE() {
    
    return BasicNEsInInitialCompositeNE;
    
  }
  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   *
   * this method shows the content of an array
   *
   */
  
  public void showVector(Vector pCurrentVector) {
    
    for (int i = 0; i < pCurrentVector.size(); i++) {
      System.out.println("Vector[" + i + "]" + pCurrentVector.elementAt(i));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void showParsedInitialCompositeNE() {
    
    for (int i = 0; i < ParsedInitialCompositeNE.length; i ++) {
      for (int j = 0 ; j < ParsedInitialCompositeNE[i].length; j ++) {
        System.out.println("ParsedInitialCompositeNE[" + i + "][" + j + "] = "
        + ParsedInitialCompositeNE[i][j]);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void showSpecificCombinationsOfBasicNEs() {
    
    for (int i = 0; i < SpecificCombinationsOfBasicNEs.length; i ++) {
      System.out.println("SpecificCombinationsOfBasicNEs[" + i + "] = "
      + SpecificCombinationsOfBasicNEs[i]);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   *
   * method writes results into an array (for the *.csv file)
   *
   */
  
  public void copyArray() {
    
    ExtendedCompositeNEs = new Object [2][FlatExtendedCompositeNEs.size()/2];
    int counter = 0;
    int i = 0;
    while (i < FlatExtendedCompositeNEs.size()) {
      ExtendedCompositeNEs[0][counter] = FlatExtendedCompositeNEs
      .elementAt(i).toString();
      i++;
      ExtendedCompositeNEs[1][counter] = FlatExtendedCompositeNEs
      .elementAt(i).toString();
      i++;
      counter ++;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method converts some important numbers to int or to double
   */
  
  private void initializeIdentifierOfBasicNEs() {
    
    IdentifierOfBasicNEs = new Object[NumberOfBasicNEs];
    for (int i = 0; i < NumberOfBasicNEs; i++) {
      IdentifierOfBasicNEs[i] = new Integer(i + 1);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setFirstBasicNeCombinations() {
    
    // add plains IDs of of BasicNEs to vector without combining them
    for (int i = 0; i < NumberOfBasicNEs; i++) {
      AllCombinationsOfBasicNEs.addElement((Integer)IdentifierOfBasicNEs[i]);
    }
    IndexAllCombinationsOfBasicNEs = NumberOfBasicNEs;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void setNextBasicNeCombinations() {
    
    int constant1 = 0;
    int constant2 = 0;
    while (constant2 < NumberOfBasicNEs) {
      for (int i = constant2; i < (NumberOfBasicNEs - 1); i++) {
        constant1 = i + 1;
        AllCombinationsOfBasicNEs.addElement(new StringBuffer()
        .append(IdentifierOfBasicNEs[constant2]).append("")
        .append(IdentifierOfBasicNEs[constant1]).toString());
      }
      constant2 ++;
    }
    // System.out.println("setNextBasicNeCombinations ");
    // this.showVector(AllCombinationsOfBasicNEs);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method compute the rest of the combinations (basics are the first and the
   * second combinations
   */
  
  private void setRestCombination(int position) {
    
    int getVectorAt = (position + NumberOfBasicNEs);
    String tmpContent = AllCombinationsOfBasicNEs.elementAt(getVectorAt)
    .toString();
    int lastValue = tmpContent.charAt(tmpContent.length() - 1);
    for (int i = 0; i < NumberOfBasicNEs; i++) {
      int tmpLastValue = lastValue;
      String tmpIdentifierOfBasicNEs = IdentifierOfBasicNEs[i].toString();
      int tmpIdentifierOfBasicNEsLastValue = tmpIdentifierOfBasicNEs.charAt(0);
      if (tmpLastValue < tmpIdentifierOfBasicNEsLastValue) {
        String newCreatedPattern = new StringBuffer().append(tmpContent)
        .append("").append(tmpIdentifierOfBasicNEs).toString();
        AllCombinationsOfBasicNEs.addElement(newCreatedPattern);
      }
    }
    // System.out.println("setRestCombination " + position);
    // this.showVector(AllCombinationsOfBasicNEs);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method create the suited pattern
   */
  
  private void createSpecificCombinationsOfBasicNEs() {
    
    for (int h = 0; h < IndexAllCombinationsOfBasicNEs; h++) {
      int arrayCounter = 0;
      SpecificCombinationsOfBasicNEs[h].addElement(IdentifierOfBasicNEs[h]);
      for (int i = IndexAllCombinationsOfBasicNEs;
      i < AllCombinationsOfBasicNEs.size(); i++) {
        String currentCombination = AllCombinationsOfBasicNEs.elementAt(i)
        .toString();
        for (int j = 0; j < currentCombination.length(); j ++){
          int tmpCurrentLetter = currentCombination.charAt(j);
          int tmpCurrentBasic =
          AllCombinationsOfBasicNEs.elementAt(h).toString().charAt(0);
          if (tmpCurrentBasic == tmpCurrentLetter) {
            SpecificCombinationsOfBasicNEs[h].addElement(currentCombination);
            arrayCounter ++;
          }
        }
      }
    }
    
  }
  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method gets a String and returns a int
   */
  
  private int stringCompiler(String pI) {
    
    Integer tmp = new Integer(pI);
    int tmp1 = tmp.intValue();
    return tmp1;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method combine the mcombination vector with the suited pattern
   */
  
  private void combineMainVector() {
    
    String currentEndString = "";
    for (int i = 0; i < MCombination.size(); i ++) {
      String tmpCombination = MCombination.elementAt(i).toString();
      for (int j = 0; j < tmpCombination.length();j ++) {
        Character tmpChar = new Character(tmpCombination.charAt(j));
        String currentTmpChar = tmpChar.toString();
        int tmpInt = stringCompiler(currentTmpChar);
        Character tmpCharFromBasicNEsInInitialCompositeNE =
        new Character(BasicNEsInInitialCompositeNE.charAt(j));
        String currentTmpCharFromBasicNEsInInitialCompositeNE =
        tmpCharFromBasicNEsInInitialCompositeNE.toString();
        int tmpIntFromBasicNEsInInitialCompositeNE =
        stringCompiler(currentTmpCharFromBasicNEsInInitialCompositeNE);
        currentEndString = new StringBuffer().append(currentEndString)
        .append(SpecificCombinationsOfBasicNEs[
        tmpIntFromBasicNEsInInitialCompositeNE - 1]
        .elementAt(tmpInt-1)).append(" ").toString();
        
      }
      NumberResult.addElement(currentEndString);
      
      currentEndString = "";
    }
    
    // there is only one basic NE contained in initial composite NE
    //if (numberResult.size() < 2) {
    // numberResult.removeAllElements();
    // numberResult = SpecificCombinationsOfBasicNEs[
    //   (new Integer(BasicNEsInInitialCompositeNE) ).intValue() - 1];
    //}
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method takes the number results and combine the numbers with the patterns
   * given in the pattern.txt file
   */
  
  private void combinePatterns() {
    
    for (int i = 0; i < NumberResult.size(); i ++) {
      StringTokenizer getNumberToken =
      new StringTokenizer(NumberResult.elementAt(i).toString(), " ");
      String currentEndString = "";
      for (int j = 0; j < ParsedInitialCompositeNE.length; j ++) {
        if (ParsedInitialCompositeNE[j][1].equals("false")) {
          currentEndString = new StringBuffer().append(currentEndString)
          .append(ParsedInitialCompositeNE[j][0]).append(" ").toString();
        }
        if (ParsedInitialCompositeNE[j][1].equals("true")) {
          currentEndString = new StringBuffer().append(currentEndString)
          .append(NamedEntityPrefix).toString();
          String currentInitialCompositeNE = getNumberToken.nextToken();
          for (int h = 0; h < currentInitialCompositeNE.length(); h ++) {
            Character tmpChar = new Character(currentInitialCompositeNE
            .charAt(h));
            String currentTmpChar = tmpChar.toString();
            int tmpInt = stringCompiler(currentTmpChar);
            String tmpBasicNEs = BasicNEs[0][tmpInt-1].toString();
            tmpBasicNEs =
            tmpBasicNEs.substring(2, (tmpBasicNEs.length()-2));
            currentEndString = new StringBuffer().append(currentEndString)
            .append(tmpBasicNEs).append(" ").toString();
          }
          currentEndString = new StringBuffer()
          .append(currentEndString.substring(0,
          (currentEndString.length() - 1))).append(NamedEntitySuffix)
          .append(" ").toString();
        }
      }
      currentEndString = currentEndString.substring
      (0, (currentEndString.length() - 1));
      FlatExtendedCompositeNEs.addElement(currentEndString);
      FlatExtendedCompositeNEs.addElement(CompositeNeConstructor);
      currentEndString ="";
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