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

package org.hypknowsys.diasdem.tasks.prepare.disambiguateWordSenses;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.core.neex.*;

/**
 * This algorithm is inspired by the section 'Diasambiguation based on sense 
 * definitions' on pp. 242-244 in Christopher D. Manning and Hinrich Schuetze: 
 * Foundations of Statistical Natural Language Processing. MIT Press, 2001.
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */
  
public class WordSenseDisambiguatorBasedOnContextWords implements 
WordSenseDisambiguator {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private String WordSensesFileName = null;
  private TextFile DebuggingHtmlFile = null;
  
  private int NumberOfWordSenses = 0;
  private HashMap WordsWithSenseDefinitions = null;
  private String[] SenseTags = null;
  private int[] SenseContexts = null;
  private StringTrie[] SenseIndicatorTerms = null;
  private int[] SenseIndicatorTermCounter = null;
  private String[] SenseIndicators = null;
  private StringBuffer ResultStringBuffer = null;
  private StringBuffer HtmlStringBuffer = null;
  private String[] Tokens = null;
  private String[] Tokens_OriginalNePlaceholders = null;
  private String Token = null;
  private String SenseTag = null;
  private int[] SenseIndex = null;
  private int DisambiguatedSenseIndex = -1;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;
  private transient StringTokenizer TmpStringTokenizer = null;
  private transient String TmpString = null;
  private transient ArrayList TmpArrayList = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public WordSenseDisambiguatorBasedOnContextWords(
  String pWordSensesFileName, TextFile pDebuggingHtmlFile) {
    
    // pDebuggingHtmlFile must either be null or an opened TextFile
    
    WordSensesFileName = pWordSensesFileName;
    DebuggingHtmlFile = pDebuggingHtmlFile;
    
    HashMap wordsWithSenseDefinitions = new HashMap();
    
    // load list of word senses
    ArrayList senseTags = new ArrayList();
    ArrayList senseContexts = new ArrayList();
    ArrayList senseIndicatorTerms = new ArrayList();
    String[] wordSenseLine = null;
    TextFile wordSensesFile = new TextFile(new File(WordSensesFileName));
    wordSensesFile.openReadOnly();
    NumberOfWordSenses = 0;
    String line = wordSensesFile.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (line != null) {
      wordSenseLine = line.split("\t");
      if (wordSenseLine != null && wordSenseLine.length == 4
      && !Tools.stringIsNullOrEmpty(wordSenseLine[0])
      && !Tools.stringIsNullOrEmpty(wordSenseLine[1])
      && !Tools.stringIsNullOrEmpty(wordSenseLine[2])
      && Tools.isInt(wordSenseLine[2])
      && !Tools.stringIsNullOrEmpty(wordSenseLine[3])) {
        TmpArrayList = (ArrayList)wordsWithSenseDefinitions
        .get(wordSenseLine[0]);
        if (TmpArrayList == null) {
          wordsWithSenseDefinitions.put(new String(wordSenseLine[0]), 
          new ArrayList());
          TmpArrayList = (ArrayList)wordsWithSenseDefinitions
          .get(wordSenseLine[0]);
          TmpArrayList.add(new Integer(NumberOfWordSenses));
        }
        else {
          TmpArrayList.add(new Integer(NumberOfWordSenses));
        }
        senseTags.add(new String(wordSenseLine[1].trim()));
        senseContexts.add(new String(wordSenseLine[2].trim()));
        senseIndicatorTerms.add(new String(wordSenseLine[3].trim()));
        NumberOfWordSenses++;
      }
      else {
        System.err.println("WordSenseDisambiguatorBasedOnContextTerms: Line \""
        + line + "\" does not conform to syntax and will thus be ignored."); 
      }
      line = wordSensesFile.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    
    // create arrays for word sense disambiguation
    WordsWithSenseDefinitions = new HashMap();
    Iterator wordsWithSenseDefinitionsIterator = 
    wordsWithSenseDefinitions.keySet().iterator();
    while (wordsWithSenseDefinitionsIterator.hasNext()) {
      TmpString = (String)wordsWithSenseDefinitionsIterator.next();
      TmpArrayList = (ArrayList)wordsWithSenseDefinitions.get(TmpString);
      WordsWithSenseDefinitions.put(new String(TmpString), 
      new int[TmpArrayList.size()]);
      int[] senseIndex = (int[])WordsWithSenseDefinitions.get(TmpString);
      for (int i = 0; i < senseIndex.length; i++) {
        senseIndex[i] = ((Integer)TmpArrayList.get(i)).intValue();
      }
    }
    SenseTags = new String[NumberOfWordSenses];
    for (int i = 0; i < SenseTags.length; i++) {
      SenseTags[i] = (String)senseTags.get(i); 
    }
    SenseContexts = new int[NumberOfWordSenses];
    for (int i = 0; i < SenseContexts.length; i++) {
      SenseContexts[i] = Tools.string2Int((String)senseContexts.get(i)); 
    }
    SenseIndicatorTerms = new StringTrie[NumberOfWordSenses];
    for (int i = 0; i < SenseIndicatorTerms.length; i++) {
      SenseIndicatorTerms[i] = new StringTrie(); 
      TmpString = (String)senseIndicatorTerms.get(i);
      wordSenseLine = TmpString.split(",");
      for (int j = 0; j < wordSenseLine.length; j++) {
        SenseIndicatorTerms[i].put(wordSenseLine[j].trim(), 
        wordSenseLine[j].trim());
      }
    }
    SenseIndicatorTermCounter = new int[NumberOfWordSenses];
    for (int i = 0; i < SenseIndicatorTermCounter.length; i++) {
      SenseIndicatorTermCounter[i] = 0; 
    }
    SenseIndicators = new String[NumberOfWordSenses];
    for (int i = 0; i < SenseIndicatorTermCounter.length; i++) {
      SenseIndicators[i] = ""; 
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
  /* ########## interface WordSenseDisambiguator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String disambiguateWordSenses(String pProcessedText,
  String pOriginalText, DIAsDEMdocument pDiasdemDocument) {
    
    ResultStringBuffer = new StringBuffer(pProcessedText.length() + 10000);
    HtmlStringBuffer = new StringBuffer(pProcessedText.length() + 10000);
    boolean senseLookedUp = false;
    
    TmpStringTokenizer = new StringTokenizer(pProcessedText, " ");
    Tokens = new String[TmpStringTokenizer.countTokens()];
    Tokens_OriginalNePlaceholders = new String[Tokens.length];
    for (int i = 0; TmpStringTokenizer.hasMoreTokens(); i++) {
      Tokens[i] = TmpStringTokenizer.nextToken();
      Tokens_OriginalNePlaceholders[i] = new String(Tokens[i]);
      // create NE type placeholder <<date>> from NE placeholders <<123>>
      if (NamedEntity.isPlaceholder(Tokens[i])) {
        Tokens[i] = (pDiasdemDocument.getActiveTextUnitsLayer()
        .getNamedEntity(NamedEntity.getNamedEntityIndex(Tokens[i])))
        .getPossibleTypesPlaceholder();
      }
    }
    
    for (int i = 0; i < Tokens.length; i++) {
      SenseIndex = (int[])WordsWithSenseDefinitions.get(Tokens[i]);
      if (SenseIndex != null) {
        senseLookedUp = true;
        ResultStringBuffer.append(Tokens_OriginalNePlaceholders[i]);
        DisambiguatedSenseIndex = this.getDisambiguatedSenseIndex(
        Tokens, i, SenseIndex);
        if (DisambiguatedSenseIndex < 0) {
          SenseTag = "";
        }
        else {
          SenseTag = SenseTags[DisambiguatedSenseIndex];
        }
        ResultStringBuffer.append(SenseTag);
        if (DebuggingHtmlFile != null) {
          if (SenseTag.length() > 0) {
            HtmlStringBuffer.append("<font class=\"greenBold\">");
          }
          else {
            HtmlStringBuffer.append("<font class=\"redBold\">");
          }
          HtmlStringBuffer.append(Tools.insertISO88591EntityReferences(
          Tokens[i]));
          HtmlStringBuffer.append(SenseTag);
          HtmlStringBuffer.append("</font>");
          if (DisambiguatedSenseIndex >= 0) {
            HtmlStringBuffer.append("<font class=\"silver\">");
            HtmlStringBuffer.append("<sub>");
            HtmlStringBuffer.append(Tools.insertISO88591EntityReferences(
            SenseIndicators[DisambiguatedSenseIndex]));
            HtmlStringBuffer.append("</sub>");
            HtmlStringBuffer.append("</font>");
          }
        }
      }
      else {
        ResultStringBuffer.append(Tokens_OriginalNePlaceholders[i]);
        if (DebuggingHtmlFile != null) {
          HtmlStringBuffer.append(Tools.insertISO88591EntityReferences(
          Tokens[i]));
        }
      }      
      ResultStringBuffer.append(" ");
      if (DebuggingHtmlFile != null) {
        HtmlStringBuffer.append(" ");
      }
    }
    
    if (DebuggingHtmlFile != null && senseLookedUp) {
      this.createHtmlTextUnitsFileLine(HtmlStringBuffer.toString().trim(),
      pOriginalText); 
    }
    
    return ResultStringBuffer.toString().trim();
    
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
  
  private void createHtmlTextUnitsFileLine(String pProcessedText, 
  String pOriginalText) {

    DebuggingHtmlFile.setNextLine("<p>" + pProcessedText + " <br><font class="
    + "\"silver\">" + pOriginalText + "</font></p>");
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private int getDisambiguatedSenseIndex(String[] pTokens, int pTokenIndex,
  int[] pSenseIndex) {
    
    // reset counters of current sense candidates
    for (int i = 0; i < pSenseIndex.length; i++) {
      SenseIndicatorTermCounter[pSenseIndex[i]] = 0;
      SenseIndicators[pSenseIndex[i]] = "";
      if (SenseContexts[pSenseIndex[i]] == 0) {
        // context: entire text unit 
        for (int j = 0; j < pTokens.length; j++) {
          if (SenseIndicatorTerms[pSenseIndex[i]].get(pTokens[j]) != null) {
            SenseIndicatorTermCounter[pSenseIndex[i]]++;
            SenseIndicators[pSenseIndex[i]] += (SenseIndicators[pSenseIndex[i]]
            .length() > 0 ? ";" : "") + pTokens[j];
          }
        }
      }
      else {
        // context: number of token preceding and following pTokenIndex
        for (int j = Math.max(0, pTokenIndex - SenseContexts[pSenseIndex[i]]); 
        j < Math.min(pTokens.length, pTokenIndex + 1 
        + SenseContexts[pSenseIndex[i]]); j++) {
          if (SenseIndicatorTerms[pSenseIndex[i]].get(pTokens[j]) != null) {
            SenseIndicatorTermCounter[pSenseIndex[i]]++;
            SenseIndicators[pSenseIndex[i]] += (SenseIndicators[pSenseIndex[i]]
            .length() > 0 ? ";" : "") + pTokens[j];
          }
        }
      }
    }
    
    // determine sense with max. occurrences of indicator terms
    int senseIndex = -1;
    int maxOccurrence = 0;
    for (int i = 0; i < pSenseIndex.length; i++) {
      if (SenseIndicatorTermCounter[pSenseIndex[i]] > maxOccurrence) {
        maxOccurrence = SenseIndicatorTermCounter[pSenseIndex[i]];
        senseIndex = pSenseIndex[i];
      }
    }
    
    if (senseIndex >= 0) {
      return senseIndex;
    }
    else {
      return -1; 
    }
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}