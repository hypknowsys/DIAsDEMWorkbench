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

package org.hypknowsys.misc.util;

import java.io.*;
import java.net.*;
import java.util.*;
import org.hypknowsys.misc.io.*;

/**
 * case-sensitive thesaurus
 * @version 2.2, 15 June 2004
 * @author Karsten Winkler
 */
  
public class Thesaurus {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private String Name = null;
  private long NextID = 1;

  private StringTrie UnorderedTerms = null;
  private Enumeration UnorderedTermsEnum = null;
  private TreeMap OrderedTerms = null;
  private Enumeration OrderedTermsEnum = null;
  private int TermsOrderedBy = NOT_ORDERED;
  private ThesaurusTerm CurrentTerm = null;
  private Enumeration CurrentEnum = null;
  private Iterator CurrentIterator = null;

  private String InputFileName = null;
  private TextFile InputFile = null;
  private String OutputFileName = null;
  private TextFile OutputFile = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static int NOT_ORDERED = 0;
  private final static int OCCURRENCES_WORDS_ASC = 1;
  private final static int OCCURRENCES_WORDS_DESC = 2;
  private final static int WORDS_ASC = 3;
  private final static int WORDS_DESC = 4;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public Thesaurus() {

    Name = "Thesaurus";  
    NextID = 1;

    UnorderedTerms = new StringTrie(); 
    UnorderedTermsEnum = null;
    OrderedTerms = null; 
    OrderedTermsEnum = null;
    TermsOrderedBy = NOT_ORDERED;

    InputFileName = null;
    InputFile = null;
    OutputFileName = null;
    OutputFile = null;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public Thesaurus(String pName, long pFirstID) {

    Name = pName;  
    NextID = pFirstID;

    UnorderedTerms = new StringTrie(); 
    UnorderedTermsEnum = null;
    OrderedTerms = null; 
    OrderedTermsEnum = null;
    TermsOrderedBy = NOT_ORDERED;

    InputFileName = null;
    InputFile = null;
    OutputFileName = null;
    OutputFile = null;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getName() { 
    return Name; }
  public int getTermsOrderedBy() { 
    return TermsOrderedBy; }
  public int getSize() { 
    if (UnorderedTerms != null) 
      return UnorderedTerms.size(); 
    else 
      return 0; 
  }
    
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

  public void countOccurrence(String pWord) {

     CurrentTerm = (ThesaurusTerm)UnorderedTerms.get(pWord);
     if (CurrentTerm == null) {
       CurrentTerm = new ThesaurusTerm(this.getNextID(), 
       pWord, 1);
     }
     else {
       CurrentTerm.incrementOccurrences();
     }
     UnorderedTerms.put(CurrentTerm.getWord(), CurrentTerm);

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void countOccurrence(String pWord, int pOccurrence) {

     CurrentTerm = (ThesaurusTerm)UnorderedTerms.get(pWord);
     if (CurrentTerm == null) {
       CurrentTerm = new ThesaurusTerm(this.getNextID(), 
       pWord, pOccurrence);
     }
     else {
       CurrentTerm.increaseOccurrences(pOccurrence);
     }
     UnorderedTerms.put(CurrentTerm.getWord(), CurrentTerm);

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean contains(String pWord) {

     CurrentTerm = (ThesaurusTerm)UnorderedTerms.get(pWord);
     if (CurrentTerm == null)
       return false;
     else
       return true;

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void add(ThesaurusTerm pTerm) {

     CurrentTerm = (ThesaurusTerm)UnorderedTerms.put(
     pTerm.getWord(), pTerm);
     TermsOrderedBy = NOT_ORDERED;

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void delete(String pWord) {

     CurrentTerm = (ThesaurusTerm)UnorderedTerms.remove(pWord);
     TermsOrderedBy = NOT_ORDERED;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public ThesaurusTerm get(String pWord) {

     CurrentTerm = (ThesaurusTerm)UnorderedTerms.get(pWord);
     if (CurrentTerm == null)
       return null;
     else
       return CurrentTerm;

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public long getID(String pWord) {

     CurrentTerm = (ThesaurusTerm)UnorderedTerms.get(pWord);
     if (CurrentTerm == null)
       return 0L;
     else
       return CurrentTerm.getID();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public int getOccurrences(String pWord) {

     CurrentTerm = (ThesaurusTerm)UnorderedTerms.get(pWord);
     if (CurrentTerm == null)
       return 0;
     else
       return CurrentTerm.getOccurrences();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderOccurrencesWordsDesc() {

    OrderedTerms = new TreeMap( Collections.reverseOrder() );
    TermsOrderedBy = OCCURRENCES_WORDS_DESC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while ( UnorderedTermsEnum.hasMoreElements() ) {
      CurrentTerm = (ThesaurusTerm)UnorderedTermsEnum
      .nextElement();
      OrderedTerms.put( (CurrentTerm.getOccurrences() + 10000000) + 
      CurrentTerm.getWord().toLowerCase() + CurrentTerm.getWord(), 
      CurrentTerm);  // case-sensitive; better use Comparator
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderOccurrencesWordsAsc() {

    OrderedTerms = new TreeMap();
    TermsOrderedBy = OCCURRENCES_WORDS_ASC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while ( UnorderedTermsEnum.hasMoreElements() ) {
      CurrentTerm = (ThesaurusTerm)UnorderedTermsEnum
      .nextElement();
      OrderedTerms.put( (CurrentTerm.getOccurrences() + 10000000) + 
      CurrentTerm.getWord().toLowerCase() + CurrentTerm.getWord(), 
      CurrentTerm);  // case-sensitive; better use Comparator
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderOccurrencesWordsAsc(int pMinOccurrence) {

    OrderedTerms = new TreeMap();
    TermsOrderedBy = OCCURRENCES_WORDS_ASC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while ( UnorderedTermsEnum.hasMoreElements() ) {
      CurrentTerm = (ThesaurusTerm)UnorderedTermsEnum
      .nextElement();
      if (CurrentTerm.getOccurrences() >= pMinOccurrence) {
        OrderedTerms.put( (CurrentTerm.getOccurrences() + 10000000) + 
        CurrentTerm.getWord().toLowerCase() + CurrentTerm.getWord(), 
        CurrentTerm);  // case-sensitive; better use Comparator
      }
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderOccurrencesWordsDesc(int pMinOccurrence) {

    OrderedTerms = new TreeMap( Collections.reverseOrder() );
    TermsOrderedBy = OCCURRENCES_WORDS_DESC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while ( UnorderedTermsEnum.hasMoreElements() ) {
      CurrentTerm = (ThesaurusTerm)UnorderedTermsEnum
      .nextElement();
      if (CurrentTerm.getOccurrences() >= pMinOccurrence) {
        OrderedTerms.put( (CurrentTerm.getOccurrences() + 10000000) + 
        CurrentTerm.getWord().toLowerCase() + CurrentTerm.getWord(), 
        CurrentTerm);  // case-sensitive; better use Comparator
      }
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderWordsDesc() {

    OrderedTerms = new TreeMap( Collections.reverseOrder() );
    TermsOrderedBy = WORDS_DESC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while ( UnorderedTermsEnum.hasMoreElements() ) {
      CurrentTerm = (ThesaurusTerm)UnorderedTermsEnum
      .nextElement();
      OrderedTerms.put(CurrentTerm.getWord().toLowerCase() 
      + CurrentTerm.getWord(), CurrentTerm);  
      // case-sensitive; better use Comparator
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderWordsAsc() {

    OrderedTerms =  new TreeMap();
    TermsOrderedBy = WORDS_ASC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while ( UnorderedTermsEnum.hasMoreElements() ) {
      CurrentTerm = (ThesaurusTerm)UnorderedTermsEnum
      .nextElement();
      OrderedTerms.put(CurrentTerm.getWord().toLowerCase()
      + CurrentTerm.getWord(), CurrentTerm);  
      // case-sensitive; better use Comparator
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderTypeWordsAsc() {

    OrderedTerms = new TreeMap();
    TermsOrderedBy = OCCURRENCES_WORDS_ASC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while ( UnorderedTermsEnum.hasMoreElements() ) {
      CurrentTerm = (ThesaurusTerm)UnorderedTermsEnum
      .nextElement();
      OrderedTerms.put( CurrentTerm.getType() +
      CurrentTerm.getWord().toLowerCase() + CurrentTerm.getWord(), 
      CurrentTerm);  // case-sensitive; better use Comparator
    }

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public ThesaurusTerm getFirstTerm() {

    if (TermsOrderedBy == NOT_ORDERED)
      CurrentEnum = UnorderedTerms.contents().elements();
    else
      CurrentIterator = OrderedTerms.values().iterator();
     
    if (TermsOrderedBy == NOT_ORDERED)
      if ( CurrentEnum.hasMoreElements() )
        return (ThesaurusTerm)CurrentEnum.nextElement();
      else
        return null;
    else
      if ( CurrentIterator.hasNext() )
        return (ThesaurusTerm)CurrentIterator.next();
      else
        return null;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public ThesaurusTerm getNextTerm() {
    
    if (TermsOrderedBy == NOT_ORDERED) {
      if (CurrentEnum == null)
        return this.getFirstTerm();
      else {
        if ( CurrentEnum.hasMoreElements() )
          return (ThesaurusTerm)CurrentEnum.nextElement();
        else
          return null;
      }
    }
    else {
      if (CurrentIterator == null)
        return this.getFirstTerm();
      else {
        if ( CurrentIterator.hasNext() )
          return (ThesaurusTerm)CurrentIterator.next();
        else
          return null;
      }
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public ThesaurusTerm getDescriptorTerm(String pWord) {

    ThesaurusTerm CurrentTerm = this.get(pWord);
    if (CurrentTerm == null) {
      return null;
    }
    else if (CurrentTerm.isDescriptor()) {
      return CurrentTerm;
    }
    else if (CurrentTerm.isNonDescriptor() && CurrentTerm
    .getUseDescriptor() != null) {
      return this.getDescriptorTerm(CurrentTerm.getUseDescriptor());
    }
    else {
      return null;
    }
      
  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public Vector getTermVector() {

    Vector oResult = new Vector();
    ThesaurusTerm oCurrentTerm = this.getFirstTerm();
    while (oCurrentTerm != null) {
      oResult.addElement( oCurrentTerm.getWord() );
      oCurrentTerm = this.getNextTerm();
    }
    return oResult;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void save(String pOutputFileName) {

    OutputFileName = pOutputFileName;
    OutputFile = new TextFile( new File(OutputFileName) );
    OutputFile.open();
    OutputFile.setFirstLine("# Terms of Thesaurus " + Name);

    if (TermsOrderedBy == NOT_ORDERED) {
      Enumeration oSaveEnum = null;
      oSaveEnum = UnorderedTerms.contents().elements();
      while ( oSaveEnum.hasMoreElements() ) {
        CurrentTerm = (ThesaurusTerm)oSaveEnum.nextElement();
        OutputFile.setNextLine( CurrentTerm.toItemLine() );
      }
    }
    else {
      Iterator oSaveIterator = null;
      oSaveIterator = OrderedTerms.values().iterator();
      while ( oSaveIterator.hasNext() ) {
        CurrentTerm = (ThesaurusTerm)oSaveIterator.next();
        OutputFile.setNextLine( CurrentTerm.toItemLine() );
      }
    }
          
    OutputFile.close();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveAsWordFrequencyFile(String pOutputFileName) {

    OutputFileName = pOutputFileName;
    OutputFile = new TextFile( new File(OutputFileName) );
    OutputFile.open();
    OutputFile.setFirstLine( CurrentTerm.getWordFrequencyAttributeLine() );

    if (TermsOrderedBy == NOT_ORDERED) {
      Enumeration oSaveEnum = null;
      oSaveEnum = UnorderedTerms.contents().elements();
      while ( oSaveEnum.hasMoreElements() ) {
        CurrentTerm = (ThesaurusTerm)oSaveEnum.nextElement();
        OutputFile.setNextLine( CurrentTerm.toWordFrequencyLine() );
      }
    }
    else {
      Iterator oSaveIterator = null;
      oSaveIterator = OrderedTerms.values().iterator();
      while ( oSaveIterator.hasNext() ) {
        CurrentTerm = (ThesaurusTerm)oSaveIterator.next();
        OutputFile.setNextLine( CurrentTerm.toWordFrequencyLine() );
      }
    }
          
    OutputFile.close();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void load(String pInputFileName) {

    UnorderedTerms = new StringTrie();
    CurrentTerm = new ThesaurusTerm();

    InputFileName = pInputFileName;
    InputFile = new TextFile( new File(InputFileName) );
    InputFile.openReadOnly();
    String vCurrentLine = InputFile.getFirstLine();
     
    while (vCurrentLine != null) {

      if ( ! vCurrentLine.startsWith("#") ) { 
        CurrentTerm = new ThesaurusTerm();
        CurrentTerm.fromItemLine(vCurrentLine);
        UnorderedTerms.put( CurrentTerm.getWord(), CurrentTerm ); 
        NextID = Math.max( NextID, CurrentTerm.getID() );
      }
      vCurrentLine = InputFile.getNextLine();
    }
          
    InputFile.close();

    Name = pInputFileName;
    TermsOrderedBy = NOT_ORDERED;
    UnorderedTermsEnum = null;
    OrderedTerms = null;
    OrderedTermsEnum = null;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public long getNextID() {

    return NextID++;

  }


  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  class SortStringsByDecreasingLength implements Comparator {
    
    public int compare(Object pObject1, Object pObject2) {
      String string1 = (String)pObject1;
      String string2 = (String)pObject2;
      if (string1.length() < string2.length()) {
        return 1;
      }
      else if (string1.length() > string2.length()) {
        return -1;
      }
      else {
        return 0;
      }
    }
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String[] args) {}

}