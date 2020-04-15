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

import java.util.Vector;

/**
 * MUST BE RE-ENGINEERED!
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz
 */

public class NumberCombinator {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private int Length = 0; // muss er uebergeben bekommen
  private int NumberOfNumbers = 0; // muss er uebergeben bekommen
  private Object Combination[] = null;
  private int WholeLength = 0;
  private Vector VectorIncludeConstant;
  
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
  
  public NumberCombinator() {}
  
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
    
    Length = 0;
    NumberOfNumbers = 0;
    WholeLength = 0;
    Combination = null;
    if (VectorIncludeConstant != null) {
      VectorIncludeConstant.removeAllElements();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * you send this method a length and the amount of contents
   * @return vectorIncludeConstant  <p>
   * <pre>
   * For example:
   * you get three patterns :
   * (1) name
   * (2) place
   * (3) street
   *
   * you type in a combination 12 to hold "name" fix and afterwards
   * "place" fix
   *
   * After computing you will receive the following number combination
   * stored in a vector:
   *
   * 12
   * 13
   * 14
   * 21
   * 31
   * 41
   * </pre>
   */
  
  public Vector getCombination(int plength, int pnumberOfNumbers)  {
    
    Length = plength - 1; // problem to be solved!
    NumberOfNumbers = pnumberOfNumbers;
    
    WholeLength = power(NumberOfNumbers, Length);
    // System.out.println("wholeLength = " + wholeLength);
    initializeArray();
    makeCombinationKwinkler();
    firstConstant();
    restConstantKwinkler();
    backwardsCombination();
    return VectorIncludeConstant;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String stringCompiler(int pInteger) {
    
    Integer currentInteger = new Integer(pInteger);
    String currentString = currentInteger.toString();
    return currentString;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method to compute the power
   */
  
  private int power(int power1, int power2) {
    
    double doublePower1 = new Integer(power1).doubleValue();
    double doublePower2 = new Integer(power2).doubleValue();
    double powerResult = java.lang.Math.pow(doublePower1, doublePower2);
    int currentPower  = new Double(powerResult).intValue();
    return currentPower;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method to initialize the array
   */
  
  private void initializeArray(){
    
    Combination = new Object [WholeLength];
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method to compute the Combination without "1"
   */
  
  private void makeCombination(){
    
    for (int i = 0; i < WholeLength; i++){
      Combination[i] = "";
    }
    int controler = 0;
    int stringToInsert = 2;
    int position = 0;
    int counter1 = NumberOfNumbers;
    int counter2 = WholeLength/NumberOfNumbers ;
    int value = 2;
    while (controler != Length) {
      for (int i = 0; i < counter1; i ++){
        for (int j = 0; j < counter2; j++) {
          
          Combination[position] = new StringBuffer()
          .append(Combination[position])
          .append(stringCompiler(stringToInsert)).toString();
          
          position++;
          
        }
        stringToInsert++;
        if (stringToInsert > (NumberOfNumbers+1)) {
          stringToInsert = 2;
        }
      }
      counter1 = counter1 * NumberOfNumbers;
      counter2 = counter2 / NumberOfNumbers;
      stringToInsert = 2;
      position = 0;
      controler ++;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void makeCombinationKwinkler(){
    
    for (int i = 0; i < WholeLength; i++){
      Combination[i] = "";
    }
    int controler = 0;
    int stringToInsert = 1;
    int position = 0;
    int counter1 = NumberOfNumbers;
    int counter2 = WholeLength/NumberOfNumbers ;
    int value = 2;
    while (controler != Length) {
      for (int i = 0; i < counter1; i ++){
        for (int j = 0; j < counter2; j++) {
          
          Combination[position] = new StringBuffer()
          .append(Combination[position])
          .append(stringCompiler(stringToInsert)).toString();
          
          position++;
          
        }
        stringToInsert++;
        if (stringToInsert > (NumberOfNumbers)) {
          stringToInsert = 1;
        }
      }
      counter1 = counter1 * NumberOfNumbers;
      counter2 = counter2 / NumberOfNumbers;
      stringToInsert = 1;
      position = 0;
      controler ++;
    }
    // for (int i = 0; i < combination.length; i++)
    //   System.out.println("combination[" + i + "]" + combination[i]);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method to insert an "1" at the begin of the founded strings
   *
   */
  
  private void firstConstant() {
    
    VectorIncludeConstant = new Vector();
    for (int i = 0; i < Combination.length; i++) {
      VectorIncludeConstant.addElement(new StringBuffer().append("1")
      .append(Combination[i]).toString());
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method insert an "1" at the rest founded strings
   *
   */
  
  private void restConstant() {
    
    for (int i = 1; i < Length; i++) {
      for (int j = 0; j < Combination.length; j++) {
        String tmpCombination = (String)Combination[j];
        String tmp = new StringBuffer().append(tmpCombination.substring(0,i))
        .append("1").append(tmpCombination.substring(i,Length)).toString();
        VectorIncludeConstant.addElement(tmp);
        
        
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void restConstantKwinkler() {
    
    for (int i = 1; i < Length; i++) {
      for (int j = 0; j < Combination.length; j++) {
        String tmpCombination = (String)Combination[j];
        String tmp = new StringBuffer().append(tmpCombination.substring(0,i))
        .append("1").append(tmpCombination.substring(i,Length)).toString();
        VectorIncludeConstant.addElement(tmp);
      }
    }
    // for (int i = 0; i < vectorIncludeConstant.size(); i++)
    //   System.out.println("vectorIncludeConstant[" + i + "]"
    //   + vectorIncludeConstant.elementAt(i));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method to compute backwards the combinations
   *
   */
  
  private void backwardsCombination() {
    
    boolean isDuplicated = false;
    String newCombination = "";
    int tmpCurrentVectorSize = VectorIncludeConstant.size();
    for (int i = 0; i < tmpCurrentVectorSize; i ++) {
      String tmpCombination = (String)VectorIncludeConstant.elementAt(i);
      for (int j = (tmpCombination.length() - 1); j > -1; j --) {
        newCombination = new StringBuffer().append(newCombination)
        .append(tmpCombination.charAt(j)).toString();
      }
      isDuplicated = duplicatedCombination(newCombination);
      if (isDuplicated != true) {
        VectorIncludeConstant.addElement(newCombination);
      }
      newCombination = "";
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method search for duplicate combinations in the vector
   * "vectorIncludeConstant"
   */
  
  private boolean duplicatedCombination(String pcurrentCombination) {
    
    boolean duplicated = false;
    for (int i = 0; i < VectorIncludeConstant.size(); i ++) {
      if (pcurrentCombination.equals(VectorIncludeConstant.elementAt(i))) {
        duplicated = true;
      }
    }
    return duplicated;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}