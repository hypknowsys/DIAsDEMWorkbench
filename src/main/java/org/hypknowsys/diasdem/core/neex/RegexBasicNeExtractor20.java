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

package org.hypknowsys.diasdem.core.neex;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;
import gnu.regexp.REMatchEnumeration;
import java.io.IOException;
import org.hypknowsys.misc.io.CsvFile;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz
 */

public class RegexBasicNeExtractor20 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String ExtractedEntity = null;
  private NamedEntityExtractorParameter CurrentParameter = null;
  private CsvFile RegularExpressionsFilename = null;
  private int NumberOfRegularExpressions = 0;
  private Object RegularExpressions[][] = null;
  
  private RE Regex1 = null;
  private REMatchEnumeration RegexMatchEnumeration = null;
  private REMatch RegexMatch = null;
  private RE Regex3 =  null;
  private String CurrentNePlaceholder = null;
  private int NamedEntityType = NamedEntity.UNKNOWN;
  private int CurrentNamedEntityID = 0;
  
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
  
  public RegexBasicNeExtractor20(
  NamedEntityExtractorParameter pCurrentParameter) {
    
    CurrentParameter = pCurrentParameter;
    
    try {
      RegularExpressionsFilename = new CsvFile(CurrentParameter
      .getRegularExpressionsFilename(), false);
      RegularExpressions = RegularExpressionsFilename.getValues();
      NumberOfRegularExpressions = RegularExpressionsFilename.countTuples();
    }
    catch (IOException e) {
      System.err.println("[RegexPatternExtractor] Error: File "
      + CurrentParameter.getRegularExpressionsFilename()
      + " cannot be opened!");
      e.printStackTrace();
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
  
  public String replaceRegexBasicNamedEntities(String pInputLine,
  NamedEntityOwner pCurrentNeOwner) {
    
    Regex1 = null;
    RegexMatchEnumeration = null;
    RegexMatch  =  null;
    Regex3  =  null;
    NamedEntityType = NamedEntity.UNKNOWN;
    CurrentNamedEntityID = 0;
    CurrentNePlaceholder = null;
    
    for (int regexIndex = 0; pInputLine != null
    && regexIndex < NumberOfRegularExpressions; regexIndex++) {
      
      try {
        Regex1 = new RE(RegularExpressions[0][regexIndex]);
        RegexMatchEnumeration = Regex1.getMatchEnumeration(pInputLine);
        while(RegexMatchEnumeration.hasMoreElements()){
          RegexMatch = (REMatch) RegexMatchEnumeration.nextElement();
          Regex3 = new RE(RegularExpressions[0][regexIndex]);
          NamedEntityType = NamedEntity.getNumericTypeOfNE(
          (String)RegularExpressions[1][regexIndex]);
          
          CurrentNamedEntityID = pCurrentNeOwner.getNextNamedEntityIndex();
          CurrentNePlaceholder = NamedEntity.createPlaceholder(
          CurrentNamedEntityID);
          pCurrentNeOwner.addNamedEntity(new NamedEntity(CurrentNamedEntityID,
          RegexMatch.toString(), CurrentNePlaceholder, NamedEntityType));
          
          ExtractedEntity = Regex3.substitute(pInputLine, " "
          + CurrentNePlaceholder + " ");
          pInputLine = ExtractedEntity;
          // System.out.println(RegexMatch);
        }
        
      }
      catch (REException e) {
        e.printStackTrace();
      }
      
    }
    // returns text with regegex named entities replaced by placeholders
    return pInputLine;
    
    
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
  
  public static void main(String pOptions[]) {}
  
}