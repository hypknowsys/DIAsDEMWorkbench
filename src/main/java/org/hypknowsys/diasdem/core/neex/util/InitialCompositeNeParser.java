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

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz
 */

public class InitialCompositeNeParser {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private Object BasicNEs[][] = null;
  private String InitialCompositeNE = "";
  private Object ParsedInitialCompositeNE[][] = null;
  private String BasicNEsInInitialCompositeNE = "";
  
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
  
  public InitialCompositeNeParser() {}
  
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
    
    ParsedInitialCompositeNE = null;
    BasicNEs = null;
    InitialCompositeNE = "";
    BasicNEsInInitialCompositeNE = "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getBasicNEsInInitialCompositeNE(Object[][] pBasicNEs,
  String pInitialCompositeNE) {
    
    BasicNEs = pBasicNEs;
    InitialCompositeNE = pInitialCompositeNE;
    this.parse(InitialCompositeNE);
    
    return BasicNEsInInitialCompositeNE;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method to get the array called ParsedInitialCompositeNE
   *
   */
  
  public Object[][] getParsedInitialCompositeNE() {
    
    return ParsedInitialCompositeNE;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method returns the BasicNEsInInitialCompositeNE
   *
   */
  
  public String getBasicNEsInInitialCompositeNE() {
    
    return BasicNEsInInitialCompositeNE;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * method gets a String, searches for tokens and write the tokens into
   * an array called ParsedInitialCompositeNE[?][0]
   */
  
  private void parse(String pInitialCompositeNE) {
    
    StringTokenizer getTokens = new StringTokenizer(pInitialCompositeNE, " ");
    ParsedInitialCompositeNE = new Object [getTokens.countTokens()][3];
    int counter = 0;
    while (getTokens.hasMoreElements()) {
      ParsedInitialCompositeNE[counter][0] = getTokens.nextToken();
      counter++;
    }
    for (int i = 0; i < ParsedInitialCompositeNE.length; i ++) {
      for (int j = 0; j < BasicNEs[0].length; j ++) {
        if (BasicNEs[0][j].equals(ParsedInitialCompositeNE[i][0])) {
          ParsedInitialCompositeNE[i][1] = "true";
          ParsedInitialCompositeNE[i][2] = intConverter(j+1);
          j = BasicNEs[0].length;
        }
        else {
          ParsedInitialCompositeNE[i][1] = "false";
        }
      }
    }
    
    int controler = 0;
    for (int i = 0; i < ParsedInitialCompositeNE.length; i ++) {
      if (ParsedInitialCompositeNE[i][1].equals("true")) {
        BasicNEsInInitialCompositeNE = new StringBuffer()
        .append(BasicNEsInInitialCompositeNE)
        .append(ParsedInitialCompositeNE[i][2]
        .toString()).toString();
        controler ++;
      }
    }
    if (controler == 0) {
      BasicNEsInInitialCompositeNE = null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String intConverter(int pInt) {
    
    Integer tmpString = new Integer(pInt);
    String returnString = tmpString.toString();
    return returnString;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void showParsedInitialCompositeNE() {
    
    for (int i = 0; i < ParsedInitialCompositeNE.length; i ++) {
      for (int j = 0; j < ParsedInitialCompositeNE[i].length; j ++) {
        System.out.println("ParsedInitialCompositeNE[" + i + "][" + j
        + " = " + ParsedInitialCompositeNE[i][j]);
      }
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