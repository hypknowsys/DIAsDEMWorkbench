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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.StringTrie;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;

/**
 * case-sensitive thesaurus
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class DefaultDIAsDEMthesaurus implements DIAsDEMthesaurus {
  
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
  private ArrayList TableRowIndex = null;
  private boolean MaintainKTableModel = false;
  private DefaultDIAsDEMthesaurusTerm CurrentTerm = null;
  private Enumeration CurrentEnum = null;
  private Iterator CurrentIterator = null;
  
  private String InputFileName = null;
  private TextFile InputFile = null;
  private String OutputFileName = null;
  private TextFile OutputFile = null;
  
  protected EventListenerList ListenerList = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient DIAsDEMthesaurusTerm TmpThesaurusTerm = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final int NOT_ORDERED = 0;
  private static final int OCCURRENCES_WORDS_ASC = 1;
  private static final int OCCURRENCES_WORDS_DESC = 2;
  private static final int WORDS_ASC = 3;
  private static final int WORDS_DESC = 4;
  private static final int TYPE_WORDS_ASC = 5;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMthesaurus() {
    
    Name = "Thesaurus";
    NextID = 1;
    
    UnorderedTerms = new StringTrie();
    UnorderedTermsEnum = null;
    OrderedTerms = null;
    OrderedTermsEnum = null;
    TermsOrderedBy = NOT_ORDERED;
    TableRowIndex = null;
    MaintainKTableModel = false;
    
    InputFileName = null;
    InputFile = null;
    OutputFileName = null;
    OutputFile = null;
    
    ListenerList = new EventListenerList();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMthesaurus(String pName, long pFirstID) {
    
    this();
    
    Name = pName;
    NextID = pFirstID;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getName() {
    
    return Name;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getTermsOrderedBy() {
    
    return TermsOrderedBy;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getSize() {
    
    if (UnorderedTerms != null) {
      return UnorderedTerms.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean maintainKTableModel() {
    
    return MaintainKTableModel;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setMaintainKTableModel(boolean pMaintainKTableModel) {
    
    MaintainKTableModel = pMaintainKTableModel;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface KTableModel and fire methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredColumnWidth(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return 50; }
      case 1: { return 250; }
      case 2: { return 125; }
      case 3: { return 250; }
    }
    return 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getRowCount() {
    
    return this.getSize();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getColumnCount() { return 4; }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getColumnName(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return "No."; }
      case 1: { return "Thesaurus Term"; }
      case 2: { return "Type of Term"; }
      case 3: { return "Details of Term"; }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Class getColumnClass(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return (new String("")).getClass(); }
      case 1: { return (new String("")).getClass(); }
      case 2: { return (new String("")).getClass(); }
      case 3: { return (new String("")).getClass(); }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isCellEditable(int pRowIndex, int pColumnIndex) {
    
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object getValueAt(int pRowIndex, int pColumnIndex) {
    
    if (!MaintainKTableModel || UnorderedTerms == null || pRowIndex < 0
    || pRowIndex >= this.getSize()) {
      return null;
    }
    
    TmpThesaurusTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.get(
    (String)TableRowIndex.get(pRowIndex));
    if (TmpThesaurusTerm == null) {
      return null;
    }
    
    switch (pColumnIndex) {
      case 0: {
        return String.valueOf(pRowIndex + 1);
      }
      case 1: {
        return TmpThesaurusTerm.getWord();
      }
      case 2: {
        if (TmpThesaurusTerm.isDescriptor()) {
          return "Decriptor";
        }
        else if (TmpThesaurusTerm.isNonDescriptor()) {
          return "Non-Descriptor";
        }
        else {
          return "-";
        }
      }
      case 3: {
        if (TmpThesaurusTerm.isDescriptor()) {
          return "SN " + TmpThesaurusTerm.getScopeNotes();
        }
        else {
          return "UD " + TmpThesaurusTerm.getUseDescriptor();
        }
      }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setValueAt(Object pValue, int pRowIndex, int pColumnIndex) {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addTableModelListener(TableModelListener pListener) {
    
    ListenerList.add(TableModelListener.class, pListener);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void removeTableModelListener(TableModelListener pListener) {
    
    ListenerList.remove(TableModelListener.class, pListener);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TableModelListener[] getTableModelListeners() {
    
    return (TableModelListener[])ListenerList.getListeners(
    TableModelListener.class);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fireTableDataChanged() {
    
    fireTableChanged(new TableModelEvent(this));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fireTableStructureChanged() {
    
    fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fireTableRowsInserted(int pFirstRow, int pLastRow) {
    
    fireTableChanged(new TableModelEvent(this, pFirstRow, pLastRow,
    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fireTableRowsUpdated(int pFirstRow, int pLastRow) {
    
    fireTableChanged(new TableModelEvent(this, pFirstRow, pLastRow,
    TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fireTableRowsDeleted(int pFirstRow, int pLastRow) {
    
    fireTableChanged(new TableModelEvent(this, pFirstRow, pLastRow,
    TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fireTableCellUpdated(int pRow, int pColumn) {
    
    fireTableChanged(new TableModelEvent(this, pRow, pRow, pColumn));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fireTableChanged(TableModelEvent pEvent) {
    
    Object[] listeners = ListenerList.getListenerList();
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TableModelListener.class) {
        ((TableModelListener)listeners[i+1]).tableChanged(pEvent);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public EventListener[] getListeners(Class pListenerType) {
    
    return ListenerList.getListeners(pListenerType);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void countOccurrence(String pWord) {
    
    CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.get(pWord);
    if (CurrentTerm == null) {
      CurrentTerm = new DefaultDIAsDEMthesaurusTerm(this.getNextID(),
      pWord, 1);
    }
    else {
      CurrentTerm.incrementOccurrences();
    }
    UnorderedTerms.put(CurrentTerm.getWord(), CurrentTerm);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void countOccurrence(String pWord, int pOccurrence) {
    
    CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.get(pWord);
    if (CurrentTerm == null) {
      CurrentTerm = new DefaultDIAsDEMthesaurusTerm(this.getNextID(),
      pWord, pOccurrence);
    }
    else {
      CurrentTerm.increaseOccurrences(pOccurrence);
    }
    UnorderedTerms.put(CurrentTerm.getWord(), CurrentTerm);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean contains(String pWord) {
    
    CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.get(pWord);
    if (CurrentTerm == null) {
      return false;
    }
    else {
      return true;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void add(DIAsDEMthesaurusTerm pTerm) {
    
    CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.put(
    pTerm.getWord(), pTerm);
    if (TermsOrderedBy != NOT_ORDERED) {
      OrderedTerms.put(this.getPrimaryKey(pTerm), pTerm);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void delete(String pWord) {
    
    CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.remove(pWord);
    if (TermsOrderedBy != NOT_ORDERED && CurrentTerm != null) {
      OrderedTerms.remove(this.getPrimaryKey(CurrentTerm));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMthesaurusTerm get(String pWord) {
    
    CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.get(pWord);
    if (CurrentTerm == null) {
      return null;
    }
    else {
      return CurrentTerm;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMthesaurusTerm getTermAtKTableRow(int pRowIndex) {
    
    if (!MaintainKTableModel || UnorderedTerms == null || pRowIndex < 0
    || pRowIndex >= this.getSize()) {
      return null;
    }
    else {
      return (DIAsDEMthesaurusTerm)UnorderedTerms.get(
      (String)TableRowIndex.get(pRowIndex));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceTermAtKTableRow(DIAsDEMthesaurusTerm pTerm,
  int pRowIndex) {
    
    if (!MaintainKTableModel || UnorderedTerms == null || pRowIndex < 0
    || pRowIndex >= this.getSize()) {
      return;
    }
    else {
      // ensure that the term's primary key has not been modified
      if (pTerm.getWord().equals(((DIAsDEMthesaurusTerm)UnorderedTerms.get(
      (String)TableRowIndex.get(pRowIndex))).getWord())) {
        // the replacement does not change the ordering
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.put(
        pTerm.getWord(), pTerm);
        this.fireTableRowsUpdated(pRowIndex, pRowIndex);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void deleteTermAtKTableRow(int pRowIndex) {
    
    if (!MaintainKTableModel || UnorderedTerms == null || pRowIndex < 0
    || pRowIndex >= this.getSize()) {
      return;
    }
    else {
      CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.remove(
      (String)TableRowIndex.get(pRowIndex));
      TableRowIndex.remove(pRowIndex);
      if (TermsOrderedBy != NOT_ORDERED) {
        OrderedTerms.remove(this.getPrimaryKey(CurrentTerm));
      }
      this.fireTableRowsDeleted(pRowIndex, pRowIndex);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addTermAndUpdateKTable(DIAsDEMthesaurusTerm pTerm) {
    
    if (!MaintainKTableModel || UnorderedTerms == null) {
      return -1;
    }
    else {
      this.add(pTerm);
      int newRowIndex = 0;
      String key = this.getPrimaryKey(pTerm);
      // search insertion point for TableRowIndex
      if (TermsOrderedBy == NOT_ORDERED) {
        CurrentEnum = UnorderedTerms.contents().elements();
        while (CurrentEnum.hasMoreElements()) {
          if (pTerm.getWord().equals(((DIAsDEMthesaurusTerm)CurrentEnum
          .nextElement()).getWord())) {
            break;
          }
          else {
            newRowIndex++;
          }
        }
      }
      else {
        CurrentIterator = OrderedTerms.keySet().iterator();
        while (CurrentIterator.hasNext()) {
          if (key.equals((String)CurrentIterator.next())) {
            break;
          }
          else {
            newRowIndex++;
          }
        }
      }
      TableRowIndex.add(newRowIndex, pTerm.getWord());
      this.fireTableRowsInserted(newRowIndex, newRowIndex);
      return newRowIndex;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public long getID(String pWord) {
    
    CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.get(pWord);
    if (CurrentTerm == null) {
      return 0L;
    }
    else {
      return CurrentTerm.getID();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getOccurrences(String pWord) {
    
    CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTerms.get(pWord);
    if (CurrentTerm == null) {
      return 0;
    }
    else {
      return CurrentTerm.getOccurrences();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderOccurrencesWordsDesc() {
    
    OrderedTerms = new TreeMap(Collections.reverseOrder());
    TermsOrderedBy = OCCURRENCES_WORDS_DESC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while (UnorderedTermsEnum.hasMoreElements()) {
      CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTermsEnum
      .nextElement();
      // case-sensitive; better use Comparator
      OrderedTerms.put(this.getPrimaryKey(CurrentTerm), CurrentTerm);
    }
    this.updateTableRowIndex();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderOccurrencesWordsAsc() {
    
    OrderedTerms = new TreeMap();
    TermsOrderedBy = OCCURRENCES_WORDS_ASC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while (UnorderedTermsEnum.hasMoreElements()) {
      CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTermsEnum
      .nextElement();
      // case-sensitive; better use Comparator
      OrderedTerms.put(this.getPrimaryKey(CurrentTerm), CurrentTerm);
    }
    this.updateTableRowIndex();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderOccurrencesWordsAsc(int pMinOccurrence) {
    
    OrderedTerms = new TreeMap();
    TermsOrderedBy = OCCURRENCES_WORDS_ASC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while (UnorderedTermsEnum.hasMoreElements()) {
      CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTermsEnum
      .nextElement();
      if (CurrentTerm.getOccurrences() >= pMinOccurrence) {
        // case-sensitive; better use Comparator
        OrderedTerms.put(this.getPrimaryKey(CurrentTerm), CurrentTerm);
      }
    }
    this.updateTableRowIndex();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderOccurrencesWordsDesc(int pMinOccurrence) {
    
    OrderedTerms = new TreeMap(Collections.reverseOrder());
    TermsOrderedBy = OCCURRENCES_WORDS_DESC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while (UnorderedTermsEnum.hasMoreElements()) {
      CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTermsEnum
      .nextElement();
      if (CurrentTerm.getOccurrences() >= pMinOccurrence) {
        // case-sensitive; better use Comparator
        OrderedTerms.put(this.getPrimaryKey(CurrentTerm), CurrentTerm);
      }
    }
    this.updateTableRowIndex();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderWordsDesc() {
    
    OrderedTerms = new TreeMap(Collections.reverseOrder());
    TermsOrderedBy = WORDS_DESC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while (UnorderedTermsEnum.hasMoreElements()) {
      CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTermsEnum
      .nextElement();
      // case-sensitive; better use Comparator
      OrderedTerms.put(this.getPrimaryKey(CurrentTerm), CurrentTerm);
    }
    this.updateTableRowIndex();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderWordsAsc() {
    
    OrderedTerms =  new TreeMap();
    TermsOrderedBy = WORDS_ASC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while (UnorderedTermsEnum.hasMoreElements()) {
      CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTermsEnum
      .nextElement();
      // case-sensitive; better use Comparator
      OrderedTerms.put(this.getPrimaryKey(CurrentTerm), CurrentTerm);
    }
    this.updateTableRowIndex();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setOrderTypeWordsAsc() {
    
    OrderedTerms = new TreeMap();
    TermsOrderedBy = TYPE_WORDS_ASC;
    UnorderedTermsEnum = UnorderedTerms.contents().elements();
    while (UnorderedTermsEnum.hasMoreElements()) {
      CurrentTerm = (DefaultDIAsDEMthesaurusTerm)UnorderedTermsEnum
      .nextElement();
      // case-sensitive; better use Comparator
      OrderedTerms.put(this.getPrimaryKey(CurrentTerm), CurrentTerm);
    }
    this.updateTableRowIndex();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMthesaurusTerm getFirstTerm() {
    
    if (TermsOrderedBy == NOT_ORDERED) {
      CurrentEnum = UnorderedTerms.contents().elements();
    }
    else {
      CurrentIterator = OrderedTerms.values().iterator();
    }
    
    if (TermsOrderedBy == NOT_ORDERED) {
      if (CurrentEnum.hasMoreElements()) {
        return (DIAsDEMthesaurusTerm)CurrentEnum.nextElement();
      }
      else {
        return null;
      }
    }
    else {
      if (CurrentIterator.hasNext()) {
        return (DIAsDEMthesaurusTerm)CurrentIterator.next();
      }
      else {
        return null;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMthesaurusTerm getNextTerm() {
    
    if (TermsOrderedBy == NOT_ORDERED) {
      if (CurrentEnum == null) {
        return this.getFirstTerm();
      }
      else {
        if (CurrentEnum.hasMoreElements()) {
          return (DIAsDEMthesaurusTerm)CurrentEnum.nextElement();
        }
        else {
          return null;
        }
      }
    }
    else {
      if (CurrentIterator == null) {
        return this.getFirstTerm();
      }
      else {
        if (CurrentIterator.hasNext()) {
          return (DIAsDEMthesaurusTerm)CurrentIterator.next();
        }
        else {
          return null;
        }
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMthesaurusTerm getDescriptorTerm(String pWord) {
    
    DIAsDEMthesaurusTerm currentTerm = this.get(pWord);
    if (currentTerm == null) {
      return null;
    }
    else if (currentTerm.isDescriptor()) {
      return currentTerm;
    }
    else if (currentTerm.isNonDescriptor() && currentTerm
    .getUseDescriptor() != null) {
      return this.getDescriptorTerm(currentTerm.getUseDescriptor());
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Vector getTermVector() {
    
    Vector oResult = new Vector();
    DIAsDEMthesaurusTerm oCurrentTerm = this.getFirstTerm();
    while (oCurrentTerm != null) {
      oResult.addElement(oCurrentTerm.getWord());
      oCurrentTerm = this.getNextTerm();
    }
    return oResult;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void save(String pOutputFileName) {
    
    OutputFileName = pOutputFileName;
    OutputFile = new TextFile(new File(OutputFileName));
    OutputFile.open();
    OutputFile.setFirstLine("# Terms of Thesaurus " + Name);
    
    if (TermsOrderedBy == NOT_ORDERED) {
      Enumeration oSaveEnum = null;
      oSaveEnum = UnorderedTerms.contents().elements();
      while (oSaveEnum.hasMoreElements()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveEnum.nextElement();
        OutputFile.setNextLine(CurrentTerm.toItemLine());
      }
    }
    else {
      Iterator oSaveIterator = null;
      oSaveIterator = OrderedTerms.values().iterator();
      while (oSaveIterator.hasNext()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveIterator.next();
        OutputFile.setNextLine(CurrentTerm.toItemLine());
      }
    }
    
    OutputFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveAsWordFrequencyFile(String pOutputFileName) {
    
    OutputFileName = pOutputFileName;
    OutputFile = new TextFile(new File(OutputFileName));
    OutputFile.open();
    OutputFile.setFirstLine(CurrentTerm.getWordFrequencyAttributeLine());
    
    if (TermsOrderedBy == NOT_ORDERED) {
      Enumeration oSaveEnum = null;
      oSaveEnum = UnorderedTerms.contents().elements();
      while (oSaveEnum.hasMoreElements()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveEnum.nextElement();
        OutputFile.setNextLine(CurrentTerm.toWordFrequencyLine());
      }
    }
    else {
      Iterator oSaveIterator = null;
      oSaveIterator = OrderedTerms.values().iterator();
      while (oSaveIterator.hasNext()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveIterator.next();
        OutputFile.setNextLine(CurrentTerm.toWordFrequencyLine());
      }
    }
    
    OutputFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveAsCsvFile(String pOutputFileName) {
    
    OutputFileName = pOutputFileName;
    OutputFile = new TextFile(new File(OutputFileName));
    OutputFile.open();
    if (CurrentTerm != null) {
      OutputFile.setFirstLine(CurrentTerm.getCsvAttributeLine());
    }
    else {
      OutputFile.setFirstLine((new DefaultDIAsDEMthesaurusTerm())
      .getCsvAttributeLine());
    }
    
    if (TermsOrderedBy == NOT_ORDERED) {
      Enumeration oSaveEnum = null;
      oSaveEnum = UnorderedTerms.contents().elements();
      while (oSaveEnum.hasMoreElements()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveEnum.nextElement();
        OutputFile.setNextLine(CurrentTerm.toCsvLine());
      }
    }
    else {
      Iterator oSaveIterator = null;
      oSaveIterator = OrderedTerms.values().iterator();
      while (oSaveIterator.hasNext()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveIterator.next();
        OutputFile.setNextLine(CurrentTerm.toCsvLine());
      }
    }
    
    OutputFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveAsHtmlFile(String pOutputFileName) {
    
    TextFile htmlFile = null;
    htmlFile = new TextFile(new File(pOutputFileName));
    htmlFile.open();
    Template header = new Template(Tools.stringFromTextualSystemResource(
    "org/hypknowsys/diasdem/resources/html/HtmlFile_HeaderTemplate.html"));
    header.addValue("${Title}", "DIAsDEM Thesaurus");
    htmlFile.setFirstLine(header.insertValues());
    
    htmlFile.setNextLine("<p>File Name: "
    + Tools.shortenFileName(this.Name, 80) + "</p>");
    htmlFile.setNextLine(
    "<p><a href=\"#T\">List of Terms</a> - "
    + "<a href=\"#D\">List of Descriptors</a></p>"
    + "<a name=\"T\"><h2>List of Terms</h2></a>"
    + "<p><small><a href=\"#TOP\">Top of the Page</a> - "
    + "<a href=\"#D\">List of Descriptors</a> - "
    + "<a href=\"#BOP\">Bottom of the Page</a></small></p>"
    + "<table border=\"1\">"
    + "<tr><th align=\"left\" valign=\"top\" nowrap>Term&nbsp;ID</th>"
    + "<th align=\"left\" valign=\"top\">Term</th>"
    + "<th align=\"left\" valign=\"top\">Term Properties</th></tr>");
    if (TermsOrderedBy == NOT_ORDERED) {
      Enumeration oSaveEnum = null;
      oSaveEnum = UnorderedTerms.contents().elements();
      while (oSaveEnum.hasMoreElements()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveEnum.nextElement();
        htmlFile.setNextLine(CurrentTerm.toHtmlLine());
      }
    }
    else {
      Iterator oSaveIterator = null;
      oSaveIterator = OrderedTerms.values().iterator();
      while (oSaveIterator.hasNext()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveIterator.next();
        htmlFile.setNextLine(CurrentTerm.toHtmlLine());
      }
    }
    htmlFile.setNextLine("</table>");
    
    htmlFile.setNextLine("<a name=\"D\"><h2>List of Descriptors</h2></a>"
    + "<p><small><a href=\"#TOP\">Top of the Page</a> - "
    + "<a href=\"#T\">List of Terms</a> - "
    + "<a href=\"#BOP\">Bottom of the Page</a></small></p>"
    + "<table border=\"1\">"
    + "<tr><th align=\"left\" valign=\"top\" nowrap>Term&nbsp;ID</th>"
    + "<th align=\"left\" valign=\"top\">Descriptor</th>"
    + "<th align=\"left\" valign=\"top\">Used for Non-Descriptors</th></tr>");
    if (TermsOrderedBy == NOT_ORDERED) {
      Enumeration oSaveEnum = null;
      oSaveEnum = UnorderedTerms.contents().elements();
      while (oSaveEnum.hasMoreElements()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveEnum.nextElement();
        if (CurrentTerm.isDescriptor()) {
          String nonDescriptors = "";
          Enumeration enum2 = null;
          enum2 = UnorderedTerms.contents().elements();
          DIAsDEMthesaurusTerm term2 = null;
          while (enum2.hasMoreElements()) {
            term2 = (DIAsDEMthesaurusTerm)enum2.nextElement();
            if (term2.isNonDescriptor()
            && term2.getUseDescriptor().trim().equals(CurrentTerm.getWord())) {
              if (nonDescriptors.equals("")) {
                nonDescriptors += term2.getWord();
              }
              else {
                nonDescriptors += "; " + term2.getWord();
              }
            }
          }
          htmlFile.setNextLine(CurrentTerm.toHtmlLine2(nonDescriptors));
        }
      }
    }
    else {
      Iterator oSaveIterator = null;
      oSaveIterator = OrderedTerms.values().iterator();
      while (oSaveIterator.hasNext()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveIterator.next();
        if (CurrentTerm.isDescriptor()) {
          String nonDescriptors = "";
          Iterator iterator2 = null;
          iterator2 = OrderedTerms.values().iterator();
          DIAsDEMthesaurusTerm term2 = null;
          while (iterator2.hasNext()) {
            term2 = (DIAsDEMthesaurusTerm)iterator2.next();
            if (term2.isNonDescriptor()
            && term2.getUseDescriptor().trim().equals(CurrentTerm.getWord())) {
              if (nonDescriptors.equals("")) {
                nonDescriptors += term2.getWord();
              }
              else {
                nonDescriptors += "; " + term2.getWord();
              }
            }
          }
          htmlFile.setNextLine(CurrentTerm.toHtmlLine2(nonDescriptors));
        }
      }
    }
    htmlFile.setNextLine("</table>");
    
    Template footer = new Template(Tools.stringFromTextualSystemResource(
    "org/hypknowsys/diasdem/resources/html/HtmlFile_FooterTemplate.html"));
    htmlFile.setNextLine(footer.insertValues());
    htmlFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void saveAsTxtFile(String pOutputFileName) {
    
    OutputFileName = pOutputFileName;
    OutputFile = new TextFile(new File(OutputFileName));
    OutputFile.empty();
    OutputFile.open();
    
    if (TermsOrderedBy == NOT_ORDERED) {
      Enumeration oSaveEnum = null;
      oSaveEnum = UnorderedTerms.contents().elements();
      while (oSaveEnum.hasMoreElements()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveEnum.nextElement();
        OutputFile.setNextLine(CurrentTerm.getWord());
      }
    }
    else {
      Iterator oSaveIterator = null;
      oSaveIterator = OrderedTerms.values().iterator();
      while (oSaveIterator.hasNext()) {
        CurrentTerm = (DefaultDIAsDEMthesaurusTerm)oSaveIterator.next();
        OutputFile.setNextLine(CurrentTerm.getWord());
      }
    }
    
    OutputFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void load(String pInputFileName) {
    
    UnorderedTerms = new StringTrie();
    CurrentTerm = new DefaultDIAsDEMthesaurusTerm();
    
    InputFileName = pInputFileName;
    InputFile = new TextFile(new File(InputFileName));
    InputFile.openReadOnly();
    String vCurrentLine = InputFile.getFirstLine();
    
    while (vCurrentLine != null) {
      
      if (!vCurrentLine.startsWith("#")) {
        CurrentTerm = new DefaultDIAsDEMthesaurusTerm();
        CurrentTerm.fromItemLine(vCurrentLine);
        UnorderedTerms.put(CurrentTerm.getWord(), CurrentTerm);
        NextID = Math.max(NextID, CurrentTerm.getID());
      }
      vCurrentLine = InputFile.getNextLine();
    }
    
    InputFile.close();
    
    Name = pInputFileName;
    TermsOrderedBy = NOT_ORDERED;
    this.updateTableRowIndex();
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
  
  private String getPrimaryKey(DIAsDEMthesaurusTerm pTerm) {
    
    switch (TermsOrderedBy) {
      case NOT_ORDERED: {
        return null;
      }
      case OCCURRENCES_WORDS_DESC: {
        return (pTerm.getOccurrences() + 10000000) + pTerm.getWord()
        .toLowerCase() + pTerm.getWord();
      }
      case OCCURRENCES_WORDS_ASC: {
        return (pTerm.getOccurrences() + 10000000) + pTerm.getWord()
        .toLowerCase() + pTerm.getWord();
      }
      case WORDS_DESC: {
        return pTerm.getWord().toLowerCase() + pTerm.getWord();
      }
      case WORDS_ASC: {
        return pTerm.getWord().toLowerCase() + pTerm.getWord();
      }
      case TYPE_WORDS_ASC: {
        return pTerm.getType() + pTerm.getWord().toLowerCase()
        + pTerm.getWord();
      }
    }
    
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void updateTableRowIndex() {
    
    if (!MaintainKTableModel) {
      return;
    }
    
    TableRowIndex = new ArrayList(this.getSize());
    if (TermsOrderedBy == NOT_ORDERED) {
      CurrentEnum = UnorderedTerms.contents().elements();
      while (CurrentEnum.hasMoreElements()) {
        TableRowIndex.add(new String(((DIAsDEMthesaurusTerm)CurrentEnum
        .nextElement()).getWord()));
      }
    }
    else {
      CurrentIterator = OrderedTerms.values().iterator();
      while (CurrentIterator.hasNext()) {
        TableRowIndex.add(new String(((DIAsDEMthesaurusTerm)CurrentIterator
        .next()).getWord()));;
      }
    }
    this.fireTableRowsUpdated(0, Math.max(0, TableRowIndex.size() - 1));
    
  }
  
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
  
  public static void main(String[] pOptions) {
    
    if ((pOptions == null) || ((pOptions[0].endsWith("create"))
    && (pOptions.length != 7))) {
      System.err.println("Usage: java diasdem.objects.Thesaurus -create "
      + "TextFile ThesaurusFile ThesaurusName FirstID [ConvertToLowerCase=]"
      + "true|false [order=]terms|occurrences");
      System.exit(1);
    }
    if ((pOptions == null) || ((pOptions[0].endsWith("clean"))
    && (pOptions.length != 5))) {
      System.err.println("Usage: java diasdem.objects.Thesaurus -clean "
      + "ThesaurusFile StopwordsFile [ConvertToLowerCase=]true|false "
      + "[order=]terms|occurrences");
      System.exit(1);
    }
    if ((pOptions == null) || ((pOptions[0].endsWith("update"))
    && (pOptions.length != 3))) {
      System.err.println("Usage: java diasdem.objects.Thesaurus -update "
      + "TargetThesaurusFile SourceThesaurusFile");
      System.exit(1);
    }
    
    ////////////////////////////////// create
    if (pOptions[0].endsWith("create")) {
      
      DIAsDEMthesaurus thesaurus = new DefaultDIAsDEMthesaurus(pOptions[3],
      (new Long(pOptions[4])).longValue());
      DIAsDEMthesaurusTerm term = null;
      StringTokenizer tokenizer = null;
      String line = null;
      String word = null;
      boolean convertToLowerCase = false;
      if (pOptions[5].trim().startsWith("true")) {
        convertToLowerCase = true;
      }
      boolean orderedByOcc = false;
      if (pOptions[6].trim().startsWith("occ")) {
        orderedByOcc = true;
      }
      
      TextFile inputFile = new TextFile(new File(pOptions[1]));
      inputFile.open();
      line = inputFile.getFirstLine();
      
      while (line != null) {
        
        if (!line.startsWith("#")) {
          System.out.println(line);
          tokenizer = new StringTokenizer(line);
          while (tokenizer.hasMoreElements()) {
            word = tokenizer.nextToken();
            if (convertToLowerCase) {
              thesaurus.countOccurrence(word.toLowerCase());
            }
            else {
              thesaurus.countOccurrence(word);
            }
          }
        }
        
        line = inputFile.getNextLine();
        
      }  // read all lines of input file
      
      if (orderedByOcc) {
        thesaurus.setOrderOccurrencesWordsDesc();
      }
      else {
        thesaurus.setOrderWordsAsc();
      }
      thesaurus.save(pOptions[2]);
      
    }  // if: create
    
    ////////////////////////////////// clean (Vrnamen, Nachnamen)
    if (pOptions[0].endsWith("clean")) {
      
      TextFile dropOuts = new TextFile(
      new File("/users/diasdem/project_hrg/dropouts.txt"));
      dropOuts.open();
      DIAsDEMthesaurus thesaurus = new DefaultDIAsDEMthesaurus();
      DIAsDEMthesaurusTerm term = null;
      StringTokenizer tokenizer = null;
      String line = null;
      String word = null;
      boolean convertToLowerCase = false;
      if (pOptions[3].trim().startsWith("true")) {
        convertToLowerCase = true;
      }
      boolean orderedByOcc = false;
      if (pOptions[4].trim().startsWith("occ")) {
        orderedByOcc = true;
      }
      
      thesaurus.load(pOptions[1]);
      DIAsDEMthesaurus stopwords = new DefaultDIAsDEMthesaurus();
      stopwords.load(pOptions[2]);
      
      term = thesaurus.getFirstTerm();
      while (term != null) {
        
        if (term.getOccurrences() <= 2) {
          thesaurus.delete(term.getWord());
          dropOuts.setNextLine(term.getWord());
        }
        if (stopwords.contains(term.getWord().toLowerCase())) {
          thesaurus.delete(term.getWord());
          dropOuts.setNextLine(term.getWord());
        }
        if (term.getWord().trim().length() < 3) {
          thesaurus.delete(term.getWord());
          dropOuts.setNextLine(term.getWord());
        }
        if (term.getWord().trim().indexOf("-") >= 0) {
          thesaurus.delete(term.getWord());
          dropOuts.setNextLine(term.getWord());
        }
        if (term.getWord().trim().indexOf(" ") >= 0) {
          thesaurus.delete(term.getWord());
          dropOuts.setNextLine(term.getWord());
        }
        
        term = thesaurus.getNextTerm();
        
      }  // read all lines of input file
      
      if (orderedByOcc) {
        thesaurus.setOrderOccurrencesWordsDesc();
      }
      else {
        thesaurus.setOrderWordsAsc();
      }
      thesaurus.save(pOptions[1]);
      dropOuts.close();
      
    } // if: clean
    
    if (pOptions[0].endsWith("update")) {
      
      DIAsDEMthesaurus target = new DefaultDIAsDEMthesaurus();
      target.load(pOptions[1]);
      DIAsDEMthesaurus source = new DefaultDIAsDEMthesaurus();
      source.load(pOptions[2]);
      source.setOrderOccurrencesWordsDesc();
      String decision = "";
      
      DIAsDEMthesaurusTerm term = source.getFirstTerm();
      while (term != null) {
        if (! target.contains(term.getWord())) {
          decision = Tools.readString("Add Term \"" + term.getWord()
          + "\" (" + term.getOccurrences() + ")? [y|n|s] ");
          if (decision.toLowerCase().equals("y")) {
            target.countOccurrence(term.getWord(), term.getOccurrences());
          }
        }
        //else
        //  target.countOccurrence(term.getWord(), term.getOccurrences());
        term = source.getNextTerm();
        if (decision.toLowerCase().equals("s")) {
          term = null;
        }
      }
      
      target.setOrderOccurrencesWordsDesc();
      target.save(pOptions[1]);
      
    }  // if: update
    
  }
  
}