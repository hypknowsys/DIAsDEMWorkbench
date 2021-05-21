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

package org.hypknowsys.diasdem.core.default21;

import java.util.NoSuchElementException;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.misc.io.Itemizer;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMthesaurusTerm implements DIAsDEMthesaurusTerm {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private long ID = 0L;
  private String Word = null;
  private int Occurrences = 0;
  private String Type = UNKNOWN;
  private String HierarchyLevel = UNKNOWN;
  
  private String ScopeNotes = null;
  // private String Definition = null;
  
  private String Synonyms = null;
  // reference from a descriptor to exactly one parent descriptor
  private String BroaderTerm = null;
  // reference from one descriptor to children descriptor
  private String NarrowerTerms = null;
  // reference from a non-descriptor to exactly one descriptor
  private String UseDescriptor = null;
  
  private double TermWeight = 0.0d;
  
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
  
  public DefaultDIAsDEMthesaurusTerm() {
    
    this(0L, "", 0, "");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMthesaurusTerm(long pID, String pWord,
  int pOccurrences) {
    
    this(pID, pWord, pOccurrences, "");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMthesaurusTerm(long pID, String pWord,
  double pTermWeight) {
    
    this(pID, pWord, 0, "");
    TermWeight = pTermWeight;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMthesaurusTerm(long pID, String pWord, int pOccurrences,
  String pType) {
    
    ID = pID;
    Word = pWord;
    Occurrences = pOccurrences;
    Type = pType;
    HierarchyLevel = UNKNOWN;
    ScopeNotes = UNKNOWN;
    Synonyms = UNKNOWN;
    BroaderTerm = UNKNOWN;
    NarrowerTerms = UNKNOWN;
    UseDescriptor = UNKNOWN;
    TermWeight = 0.0d;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMthesaurusTerm(String pItemLine) {
    
    this.fromItemLine(pItemLine);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public long getID() {
    return ID; }
  public String getWord() {
    return Word; }
  public int getOccurrences() {
    return Occurrences; }
  public String getType() {
    return Type; }
  public String getHierarchyLevel() {
    return HierarchyLevel; }
  public String getScopeNotes() {
    return ScopeNotes; }
  public String getSynonyms() {
    return Synonyms; }
  public String getBroaderTerm() {
    return BroaderTerm; }
  public String getNarrowerTerms() {
    return NarrowerTerms; }
  public String getUseDescriptor() {
    return UseDescriptor; }
  public boolean isDescriptor() {
    if (Type.equals("D")) {
      return true;
    }
    else {
      return false;
    }
  }
  public boolean isNonDescriptor() {
    if (Type.equals("N")) {
      return true;
    }
    else {
      return false;
    }
  }
  public double getTermWeight() {
    return TermWeight; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setID(long pID) {
    ID = pID; }
  public void setWord(String pWord) {
    Word = pWord; }
  public void setOccurrences(int pOccurrences) {
    Occurrences = pOccurrences; }
  public void incrementOccurrences() {
    Occurrences++; }
  public void increaseOccurrences(int pIncrease) {
    Occurrences += pIncrease; }
  public void setType(String pType) {
    Type = pType; }
  public void setHierarchyLevel(String pHierarchyLevel) {
    HierarchyLevel = pHierarchyLevel; }
  public void setScopeNotes(String pScopeNotes) {
    ScopeNotes = pScopeNotes; }
  public void setSynonyms(String pSynonyms) {
    Synonyms = pSynonyms; }
  public void setBroaderTerm(String pBroaderTerm) {
    BroaderTerm = pBroaderTerm; }
  public void setNarrowerTerms(String pNarrowerTerms) {
    NarrowerTerms = pNarrowerTerms; }
  public void setUseDescriptor(String pUseDescriptor) {
    UseDescriptor = pUseDescriptor; }
  public void setTermWeight(double pTermWeight) {
    TermWeight = pTermWeight; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    return "Term: " + Word + ", ID=" + ID + ", Occurrences=" + Occurrences
    + ", TY=" + Type + ", HL=" + HierarchyLevel + ", TW=" + TermWeight;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toItemLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(Itemizer.longToItem(ID));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem(Word));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.intToItem(Occurrences));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem("TY=" + Type));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem("HL=" + HierarchyLevel));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem("SY=" + Synonyms));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem("BT=" + BroaderTerm));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem("NT=" + NarrowerTerms));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem("UD=" + UseDescriptor));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem("SN=" + ScopeNotes));
    if (TermWeight < 0.0d || TermWeight > 0.0d) {
      TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
      TmpStringBuffer.append(Itemizer.doubleToItem(TermWeight));
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) {
    
    ID = 0L; Word = null; Occurrences = 0;
    Itemizer itemizer = new Itemizer(pItemLine);
    
    try {
      ID = itemizer.itemToLong(itemizer.getNextItem());
      Word = itemizer.itemToString(itemizer.getNextItem());
      Occurrences = itemizer.itemToInt(itemizer.getNextItem());
      Type = (
      itemizer.itemToString(itemizer.getNextItem())).substring(3);
      HierarchyLevel =
      itemizer.itemToString(itemizer.getNextItem()).substring(3);
      Synonyms =
      itemizer.itemToString(itemizer.getNextItem()).substring(3);
      BroaderTerm = itemizer.itemToString(
      itemizer.getNextItem()).substring(3);
      NarrowerTerms = itemizer.itemToString(
      itemizer.getNextItem()).substring(3);
      UseDescriptor = itemizer.itemToString(
      itemizer.getNextItem()).substring(3);
    }
    catch (NoSuchElementException e1) { this.reset(); }
    catch (NumberFormatException e2) { this.reset(); }
    catch (StringIndexOutOfBoundsException e2) { this.reset(); }
    try {
      ScopeNotes =
      itemizer.itemToString(itemizer.getNextItem()).substring(3);
    }
    catch (NoSuchElementException e1) { ScopeNotes = UNKNOWN; }
    catch (NumberFormatException e2) { ScopeNotes = UNKNOWN; }
    catch (StringIndexOutOfBoundsException e2) { ScopeNotes = UNKNOWN; }
    try {
      // kwinkler, 20 Jun 2003: TermWeight is optional
      TermWeight = itemizer.itemToDouble(itemizer.getNextItem());
    }
    catch (NoSuchElementException e1) { TermWeight = 0.0d; }
    catch (NumberFormatException e2) { TermWeight = 0.0d; }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toWordFrequencyLine() {
    
    return "\"" + Word + "\"," + Occurrences;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getWordFrequencyAttributeLine() {
    
    return "Term,TermFrequency";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toCsvLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(ID);
    TmpStringBuffer.append(",\"");
    TmpStringBuffer.append(Tools.removeQuotesAndNewLines(Word));
    TmpStringBuffer.append("\",");
    TmpStringBuffer.append(Occurrences);
    TmpStringBuffer.append(",\"");
    TmpStringBuffer.append(Tools.removeQuotesAndNewLines(Type));
    TmpStringBuffer.append("\",\"");
    TmpStringBuffer.append(Tools.removeQuotesAndNewLines(HierarchyLevel));
    TmpStringBuffer.append("\",\"");
    TmpStringBuffer.append(Tools.removeQuotesAndNewLines(Synonyms));
    TmpStringBuffer.append("\",\"");
    TmpStringBuffer.append(Tools.removeQuotesAndNewLines(BroaderTerm));
    TmpStringBuffer.append("\",\"");
    TmpStringBuffer.append(Tools.removeQuotesAndNewLines(NarrowerTerms));
    TmpStringBuffer.append("\",\"");
    TmpStringBuffer.append(Tools.removeQuotesAndNewLines(UseDescriptor));
    TmpStringBuffer.append("\",\"");
    TmpStringBuffer.append(Tools.removeQuotesAndNewLines(ScopeNotes));
    TmpStringBuffer.append("\",");
    TmpStringBuffer.append(TermWeight);
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toHtmlLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("<tr> <td align=\"left\" valign=\"top\">");
    TmpStringBuffer.append(ID);
    TmpStringBuffer.append("</td> <td align=\"left\" valign=\"top\">");
    if (Word == null || Word.length() == 0) {
      TmpStringBuffer.append("&nbsp;");
    }
    else {
      TmpStringBuffer.append(Tools.insertUTF8EntityReferences(Tools
      .removeQuotesAndNewLines(Word)));
    }
    TmpStringBuffer.append("</td> <td align=\"left\" valign=\"top\">");
    TmpStringBuffer.append("TermFrequeny = " + Occurrences + "; ");
    TmpStringBuffer.append("Type = "
    + Tools.removeQuotesAndNewLines(Type) + "; ");
    boolean appendNbsp = true;
    if (!HierarchyLevel.trim().equals("-") && HierarchyLevel.length() > 0) {
      TmpStringBuffer.append("<br>");
      TmpStringBuffer.append("HierarchyLevel = "
      + Tools.insertUTF8EntityReferences(Tools.removeQuotesAndNewLines(
      HierarchyLevel)) + "; ");
      appendNbsp = false;
    }
    if (!Synonyms.trim().equals("-") && Synonyms.length() > 0) {
      TmpStringBuffer.append("<br>");
      TmpStringBuffer.append("Synomyms = \""
      + Tools.insertUTF8EntityReferences(Tools.removeQuotesAndNewLines(
      Synonyms)) + "\"; ");
      appendNbsp = false;
    }
    if (!BroaderTerm.trim().equals("-") && BroaderTerm.length() > 0) {
      TmpStringBuffer.append("<br>");
      TmpStringBuffer.append("BroaderTerms = \""
      + Tools.insertUTF8EntityReferences(Tools.removeQuotesAndNewLines(
      BroaderTerm)) + "\"; ");
      appendNbsp = false;
    }
    if (!NarrowerTerms.trim().equals("-") && NarrowerTerms.length() > 0) {
      TmpStringBuffer.append("<br>");
      TmpStringBuffer.append("NarrowerTerms = \""
      + Tools.insertUTF8EntityReferences(Tools.removeQuotesAndNewLines(
      NarrowerTerms)) + "\"; ");
      appendNbsp = false;
    }
    if (!UseDescriptor.trim().equals("-") && UseDescriptor.length() > 0) {
      TmpStringBuffer.append("<br>");
      TmpStringBuffer.append("UseDescriptor = \""
      + Tools.insertUTF8EntityReferences(Tools.removeQuotesAndNewLines(
      UseDescriptor)) + "\"; ");
      appendNbsp = false;
    }
    if (!ScopeNotes.trim().equals("-") && ScopeNotes.length() > 0) {
      TmpStringBuffer.append("<br>");
      TmpStringBuffer.append("ScopeNotes = \""
      + Tools.insertUTF8EntityReferences(Tools.removeQuotesAndNewLines(
      ScopeNotes)) + "\"; ");
      appendNbsp = false;
    }
    if (appendNbsp) {
      TmpStringBuffer.append("&nbsp;");
    }
    TmpStringBuffer.append("</td> </tr>");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toHtmlLine2(String pContents) {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("<tr> <td align=\"left\" valign=\"top\">");
    TmpStringBuffer.append(ID);
    TmpStringBuffer.append("</td> <td align=\"left\" valign=\"top\">");
    if (Word == null || Word.length() == 0) {
      TmpStringBuffer.append("&nbsp;");
    }
    else {
      TmpStringBuffer.append(Tools.insertUTF8EntityReferences(Tools
      .removeQuotesAndNewLines(Word)));
    }
    TmpStringBuffer.append("</td> <td align=\"left\" valign=\"top\">");
    if (pContents == null || pContents.length() == 0) {
      TmpStringBuffer.append("&nbsp;");
    }
    else {
      TmpStringBuffer.append(Tools.insertUTF8EntityReferences(Tools
      .removeQuotesAndNewLines(pContents)));
    }
    TmpStringBuffer.append("</td> </tr>");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCsvAttributeLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("ID");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("Term");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("TermFrequency");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("Type");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("HierarchyLevel");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("Synonyms");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("BroaderTerm");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("NarrowerTerms");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("UseDescriptor");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("ScopeNotes");
    TmpStringBuffer.append(",");
    TmpStringBuffer.append("TermWeight");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {
    
    ID = 0L;
    Word = "";
    Occurrences = 0;
    Type = UNKNOWN;
    HierarchyLevel = UNKNOWN;
    ScopeNotes = UNKNOWN;
    Synonyms = UNKNOWN;
    BroaderTerm = UNKNOWN;
    NarrowerTerms = UNKNOWN;
    UseDescriptor = UNKNOWN;
    TermWeight = 0.0d;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}