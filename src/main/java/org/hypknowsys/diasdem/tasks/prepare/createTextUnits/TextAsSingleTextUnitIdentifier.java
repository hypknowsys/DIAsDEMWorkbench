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

/*
 * This text unit creator identifies German sentences by replacing full stops
 * ('.') in abbrviations with stars ('*') and then splitting the text at the
 * remaining full stops. Prerequisite: Text to be decomposed does not contain
 * stars.
 */

package org.hypknowsys.diasdem.tasks.prepare.createTextUnits;

import java.util.regex.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class TextAsSingleTextUnitIdentifier implements TextUnitCreator {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private String[] AbbreviationsSearch = null;  // "z.B."
  private String[] AbbreviationsReplace = null;  // "z*B*"
  private Pattern[] AbbreviationsRegex = null;  // "z\.B\."
  private Pattern[] RegexSearch = null;  // "(str)\.(\d*)"
  private String[] RegexReplace = null;  // "$1\*$2"
  private boolean KeepStars = false;
  
  private static Pattern FullStopPattern = Pattern.compile("\\.");
  private static Pattern StarPattern = Pattern.compile("\\*");
 
  private String[] TmpTextUnits = null;
  private Matcher TmpMatcher = null;
  
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

  /** 
   * Creates a new instance of BasicTextUnitCreator, all arameters must not 
   * be null, pRegexSearch and pRegexReplace must have the same dimension
   */

  public TextAsSingleTextUnitIdentifier(String[] pAbbreviations, 
  String[] pRegexSearch, String[] pRegexReplace, boolean pKeepStars) {
    
    AbbreviationsSearch = pAbbreviations;
    AbbreviationsReplace = new String[pAbbreviations.length];
    AbbreviationsRegex = new Pattern[pAbbreviations.length];
    KeepStars = pKeepStars;
    
    for (int i = 0; i < AbbreviationsSearch.length; i++) {
      TmpMatcher = FullStopPattern.matcher( AbbreviationsSearch[i] );
      AbbreviationsReplace[i] = "$1" + TmpMatcher.replaceAll("*");
      TmpMatcher.reset();
      AbbreviationsRegex[i] = Pattern.compile("(^|\\ |\\(|\\)|\\,|\\;|\\:|\\-|\\/|\\'|\\\")" 
      + TmpMatcher.replaceAll("\\\\.") );
    }
    
    RegexSearch = new Pattern[pRegexSearch.length];
    RegexReplace = pRegexReplace;
    for (int i = 0; i < RegexSearch.length; i++)
      RegexSearch[i] = Pattern.compile(pRegexSearch[i]);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String[] getAbbreviationsSearch() { 
    return AbbreviationsSearch; }
  public String[] getAbbreviationsReplace() {  
    return AbbreviationsReplace; }
  public Pattern[] getAbbreviationsRegex() { 
    return AbbreviationsRegex; }
  
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
  /* ########## interface TextUnitCreator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnit[] createTextUnits(String pText) {
    
    // replace full stops in abbreviations with stars
    for (int i = 0; i < AbbreviationsRegex.length; i++) {
      // 650% performance increase by initial check
      if (pText.indexOf(AbbreviationsSearch[i]) >= 0) { 
        TmpMatcher = AbbreviationsRegex[i].matcher(pText);
        pText = TmpMatcher.replaceAll(AbbreviationsReplace[i]);
      }
    }
    // replace full stops in regular expression with stars
    for (int i = 0; i < RegexSearch.length; i++) {
      TmpMatcher = RegexSearch[i].matcher(pText);
      pText = TmpMatcher.replaceAll(RegexReplace[i]);
    }
    // don't split text into text units at remaining full stops
    // the entire text corresponds to one text unit
    // TmpTextUnits = FullStopPattern.split(pText);
    DIAsDEMtextUnit[] result = new DefaultDIAsDEMtextUnit[1];
    if (pText != null) {
      result[0]= new DefaultDIAsDEMtextUnit(pText, 0, 0, pText.length());
    }
    else {
      result[0]= new DefaultDIAsDEMtextUnit("null", 0);
    }
    // replace stars with full stops to restore original abbreviations
    // add removed full stop at end of each sentence
    for (int i = 0; i < result.length; i++) {
      if (KeepStars) {
        result[i].setContentsFromString(result[i].getContentsAsString().trim());
      }
      else {
        TmpMatcher = StarPattern.matcher(result[i].getContentsAsString());
        result[i].setContentsFromString(TmpMatcher.replaceAll(".").trim());
      }
    }
    
    return result;
    
  }

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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String[] args) {
    
    String[] abbreviations = new String[4];
    abbreviations[0] = "z.B.";
    abbreviations[1] = "M.E.";
    abbreviations[2] = "GmbH";
    abbreviations[3] = "m. E.";
    String[] regexSearch = new String[3];
    regexSearch[0] = "(str|Str|pl|Pl)\\.(\\s*\\d+)";
    regexSearch[1] = "(\\d+?)\\.(\\s*Okt|\\s*Nov|\\s*Dez)\\.";
    regexSearch[2] = "(\\d+)\\.(\\d+)";
    String[] regexReplace = new String[3];
    regexReplace[0] = "$1\\*$2";
    regexReplace[1] = "$1\\*$2\\*";
    regexReplace[2] = "$1\\*$2";
    String text = "M.E. hat die GmbH 50.000 DM Stammkapital. ";
    text += "Sie wohnt z.B. in der Musterstr. 12. Das ist ein Test. ";
    text += "Heute (m. E.) ist m. E. Donnerstag, der 1. Okt. 2002.";
    
    TextAsSingleTextUnitIdentifier sentenceCreator =
    new TextAsSingleTextUnitIdentifier(abbreviations, regexSearch, 
    regexReplace, true);
    String[] abbrSearch = sentenceCreator.getAbbreviationsSearch();
    String[] abbrReplace = sentenceCreator.getAbbreviationsReplace();
    Pattern[] abbrRegex = sentenceCreator.getAbbreviationsRegex();
    System.out.println("\nText:");
    System.out.println(text);
    System.out.println("\nAbbreviations to search for:");
    for (int i = 0; i < abbrSearch.length; i++)
      System.out.println(abbrSearch[i]);
    System.out.println("\nReplaced abbreviations to search for:");
    for (int i = 0; i < abbrReplace.length; i++)
      System.out.println(abbrReplace[i]);
    System.out.println("\nRegular expressions to search for:");
    for (int i = 0; i < abbrRegex.length; i++)
      System.out.println(abbrRegex[i].pattern());
    DIAsDEMtextUnit[] textUnits = sentenceCreator.createTextUnits(text);
    System.out.println("\nIdentified sentences:");
    for (int i = 0; i < textUnits.length; i++)
      System.out.println(i + ") \"" + textUnits[i].getContents() + "\"");
    
  }

}