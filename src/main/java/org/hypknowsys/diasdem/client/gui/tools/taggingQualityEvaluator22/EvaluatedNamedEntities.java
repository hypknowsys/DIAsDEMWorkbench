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

package org.hypknowsys.diasdem.client.gui.tools.taggingQualityEvaluator22;

import java.util.EventListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.hypknowsys.misc.swing.KTableModel;


/**
 * case-sensitive thesaurus
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class EvaluatedNamedEntities implements KTableModel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected int NumberOfAttributes = 0;
  protected String[] AttributeNames = null;
  protected int[][] EvaluationResults = null;
  
  protected EventListenerList ListenerList = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final int NUMBER_OF_NES = 0;
  public static final int COMPL_CORRECT = 1;
  public static final int PART_CORRECT = 2;
  public static final int INCORRECT = 3;
  public static final int MISSING = 4;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public EvaluatedNamedEntities() {

    // both arrays must havbe the same size (in dimension 1)
    NumberOfAttributes = 0;
    AttributeNames = null;
    EvaluationResults = null;
    ListenerList = new EventListenerList();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int[][] getEvaluationResults() {
    return EvaluationResults;
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
  /* ########## interface KTableModel and fire methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredColumnWidth(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return 100; }
      case 1: { return 50; }
      case 2: { return 50; }
      case 3: { return 50; }
      case 4: { return 50; }
      case 5: { return 50; }
    }
    return 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getRowCount() {
    
    return NumberOfAttributes;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getColumnCount() { return 6; }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getColumnName(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return "Attribute Name"; }
      case 1: { return "Number of NEs"; }
      case 2: { return "Compl. Correct"; }
      case 3: { return "Part. Correct"; }
      case 4: { return "Incorrect"; }
      case 5: { return "Missing"; }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Class getColumnClass(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return (new String("")).getClass(); }
      case 1: { return (new Integer(1)).getClass(); }
      case 2: { return (new Integer(1)).getClass(); }
      case 3: { return (new Integer(1)).getClass(); }
      case 4: { return (new Integer(1)).getClass(); }
      case 5: { return (new Integer(1)).getClass(); }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isCellEditable(int pRowIndex, int pColumnIndex) {
    
    if (pColumnIndex == 0) {
      return false;
    }
    else {
      return true;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object getValueAt(int pRowIndex, int pColumnIndex) {
    
    if (pRowIndex < 0 || pRowIndex >= NumberOfAttributes 
    || AttributeNames == null || EvaluationResults == null) {
      return null;
    }
    
    switch (pColumnIndex) {
      case 0: {
        return AttributeNames[pRowIndex];
      }
      case 1: {
        return new Integer(EvaluationResults[pRowIndex][NUMBER_OF_NES]);
      }
      case 2: {
        return new Integer(EvaluationResults[pRowIndex][COMPL_CORRECT]);
      }
      case 3: {
        return new Integer(EvaluationResults[pRowIndex][PART_CORRECT]);
      }
      case 4: {
        return new Integer(EvaluationResults[pRowIndex][INCORRECT]);
      }
      case 5: {
        return new Integer(EvaluationResults[pRowIndex][MISSING]);
      }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setValueAt(Object pValue, int pRowIndex, int pColumnIndex) {
  
    if (pRowIndex < 0 || pRowIndex >= NumberOfAttributes || pValue == null
    || AttributeNames == null || EvaluationResults == null) {
      return;
    }
    
    switch (pColumnIndex) {
      case 1: {
        EvaluationResults[pRowIndex][NUMBER_OF_NES] = ((Integer)pValue)
        .intValue();
        break;
      }
      case 2: {
        EvaluationResults[pRowIndex][COMPL_CORRECT] = ((Integer)pValue)
        .intValue();
        break;
      }
      case 3: {
        EvaluationResults[pRowIndex][PART_CORRECT] = ((Integer)pValue)
        .intValue();
        break;
      }
      case 4: {
        EvaluationResults[pRowIndex][INCORRECT] = ((Integer)pValue)
        .intValue();
        break;
      }
      case 5: {
        EvaluationResults[pRowIndex][MISSING] = ((Integer)pValue)
        .intValue();
        break;
      }
    }
  
  }
  
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
  
  public void reset(String[] pAttributeNames, int[][] pEvaluationResults) {

    // both arrays must havbe the same size (in dimension 1)
    
    NumberOfAttributes = 0;
    AttributeNames = null;
    EvaluationResults = null;
    this.fireTableRowsDeleted(0, Math.max(0, NumberOfAttributes - 1));
    if (pAttributeNames != null && pEvaluationResults != null) {
      NumberOfAttributes = pAttributeNames.length;
      AttributeNames = pAttributeNames;
      EvaluationResults = pEvaluationResults;
      this.fireTableRowsInserted(0, Math.max(0, NumberOfAttributes - 1));
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getSumOfNumberOfNes() {
    
    int result = 0;
    if (NumberOfAttributes > 0 && EvaluationResults != null) {
      for (int i = 0; i < EvaluationResults.length; i++) {
        result += EvaluationResults[i][NUMBER_OF_NES];
      }
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getSumOfComplCorrect() {
    
    int result = 0;
    if (NumberOfAttributes > 0 && EvaluationResults != null) {
      for (int i = 0; i < EvaluationResults.length; i++) {
        result += EvaluationResults[i][COMPL_CORRECT];
      }
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getSumOfPartCorrect() {
    
    int result = 0;
    if (NumberOfAttributes > 0 && EvaluationResults != null) {
      for (int i = 0; i < EvaluationResults.length; i++) {
        result += EvaluationResults[i][PART_CORRECT];
      }
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getSumOfIncorrect() {
    
    int result = 0;
    if (NumberOfAttributes > 0 && EvaluationResults != null) {
      for (int i = 0; i < EvaluationResults.length; i++) {
        result += EvaluationResults[i][INCORRECT];
      }
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getSumOfMissing() {
    
    int result = 0;
    if (NumberOfAttributes > 0 && EvaluationResults != null) {
      for (int i = 0; i < EvaluationResults.length; i++) {
        result += EvaluationResults[i][MISSING];
      }
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getEvaluationResult(int pRowID, int pTypeID) {
    
    if (pRowID >= 0 && pRowID < AttributeNames.length && pTypeID >= 0
    && pTypeID < 5 && EvaluationResults != null) {
      return EvaluationResults[pRowID][pTypeID];
    }
    else {
      return 0;
    }
    
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
  
  public static void main(String[] pOptions) {}
  
}