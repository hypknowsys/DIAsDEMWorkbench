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

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.hypknowsys.misc.io.TextFile;

/**
 * @version 2.1, 30 Jul 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class RegexBasicNeExtractor21 {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private NamedEntityExtractorParameter NeTaskParameter = null;
  private int NumberOfRegularExpressions = 0;
  
  private int[] NamedEntityTypes = null;
  private Pattern[] CompiledRegularExpressions = null;
  private Matcher RegexMatcher = null;
  
  private String NePlaceholder = null;
  private int NeID = 0;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient Pattern TmpPattern = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public RegexBasicNeExtractor21(
  NamedEntityExtractorParameter pNeTaskParameter) {
    
    NeTaskParameter = pNeTaskParameter;
    
    // read regex file: each line contains the regex to search for
    // and the basic NE type string separated by a tab stop;
    // comment lines start with '#'
    TextFile regexFile = new TextFile(new File(NeTaskParameter
    .getRegularExpressionsFilename()));
    regexFile.openReadOnly();
    String[] contents = null;
    ArrayList regexList = new ArrayList();
    ArrayList neTypesList = new ArrayList();
    NumberOfRegularExpressions = 0;
    String line = regexFile.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      contents = line.split("\t");
      if (contents.length == 2 && NamedEntity.getNumericTypeOfNE(
      contents[1].trim()) != NamedEntity.UNKNOWN) {
        try {
          TmpPattern = Pattern.compile(contents[0].trim());
          NumberOfRegularExpressions++;
          regexList.add(contents[0].trim());
          neTypesList.add(contents[1].trim());
        }
        catch (PatternSyntaxException e) {
          System.out.println("[RegexBasicNeExtractor21] Regex syntax error in "
          + " file " + NeTaskParameter.getRegularExpressionsFilename() + ": "
          + " Line \"" + line + "\"; error message: " + e.getMessage());
        }
      }
      else {
        System.out.println("[RegexBasicNeExtractor21] Error in file "
        + NeTaskParameter.getRegularExpressionsFilename() + ": Line \""
        + line + "\" does not conform to syntax!");
      }
      line = regexFile.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    regexFile.close();
    
    CompiledRegularExpressions = new Pattern[NumberOfRegularExpressions];
    NamedEntityTypes = new int[NumberOfRegularExpressions];
    for (int i = 0; i < NumberOfRegularExpressions; i++) {
      NamedEntityTypes[i] = NamedEntity.getNumericTypeOfNE(
      (String)neTypesList.get(i));
      CompiledRegularExpressions[i] = Pattern.compile(
      (String)regexList.get(i));
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
    
    for (int regexIndex = 0; pInputLine != null
    && regexIndex < NumberOfRegularExpressions; regexIndex++) {
      
      RegexMatcher = CompiledRegularExpressions[regexIndex]
      .matcher(pInputLine);
      TmpStringBuffer = new StringBuffer();
      while(RegexMatcher.find()) {
        NeID = pCurrentNeOwner.getNextNamedEntityIndex();
        NePlaceholder = NamedEntity.createPlaceholder(NeID);
        pCurrentNeOwner.addNamedEntity(new NamedEntity(NeID, pInputLine
        .substring(RegexMatcher.start(), RegexMatcher.end()),
        NePlaceholder, NamedEntityTypes[regexIndex]));
        RegexMatcher.appendReplacement(TmpStringBuffer, " "
        + NePlaceholder + " ");
        // System.out.println(pInputLine.substring(RegexMatcher.start(),
        // RegexMatcher.end()));
      }
      RegexMatcher.appendTail(TmpStringBuffer);
      pInputLine = TmpStringBuffer.toString();
      
    }
    
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