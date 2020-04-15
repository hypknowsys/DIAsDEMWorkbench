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

import java.text.NumberFormat;
import java.util.EventListener;
import java.util.Locale;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.swing.KTableModel;


/**
 * Data model representing the results of evaluating the quality
 * of XML tag attributes
 * @version 2.2, 5 February 2005
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class EvaluatedXmlTagAttributes implements KTableModel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected int TrueNumberOfNamedEntities = 0;
  protected int NumberOfComplCorrectNamedEntities = 0;
  protected int NumberOfPartCorrectNamedEntities = 0;
  protected int NumberOfIncorrectNamedEntities = 0;
  protected int NumberOfMissingNamedEntities = 0;
  protected double XmlAttributeAccuracy = 0.0d;
  protected double XmlAttributePrecision = 0.0d;
  protected double XmlAttributeRecall = 0.0d;
  protected String CiXmlAttributeAccuracy = "N/A";
  protected String CiXmlAttributePrecision = "N/A";
  protected String CiXmlAttributeRecall = "N/A";
  
  private NumberFormat MyNumberFormatter = null;

  protected EventListenerList ListenerList = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient double TmpDouble = 0.0d;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected static final int NUMBER_OF_ROWS = 8;
  protected static final int NUMBER_OF_COLUMNS = 3;
  
  protected static final String[] MEASURES_OF_TAGGING_QUALITY = {
    "True Number of Named Entities",
    "Number of Compl. Correct Named Entities",
    "Number of Part. Correct Named Entities",
    "Number of Incorrect Named Entities",
    "Number of Missing Named Entities",
    "XML Attribute Accuracy",
    "XML Attribute Precision",
    "XML Attribute Recall"
  };

  protected static final int TRUE_NUMBER_OF_NAMED_ENTITIES = 0;
  protected static final int NUMBER_OF_COMPL_CORRECT_NAMED_ENTITIES = 1;
  protected static final int NUMBER_OF_PART_CORRECT_NAMED_ENTITIES = 2;
  protected static final int NUMBER_OF_INCORRECT_NAMED_ENTITIES = 3;
  protected static final int NUMBER_OF_MISSING_NAMED_ENTITIES = 4;
  protected static final int XML_ATTRIBUTE_ACCURACY = 5;
  protected static final int XML_ATTRIBUTE_PRECISION = 6;
  protected static final int XML_ATTRIBUTE_RECALL = 7;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public EvaluatedXmlTagAttributes() {

    TrueNumberOfNamedEntities = 0;
    NumberOfComplCorrectNamedEntities = 0;
    NumberOfPartCorrectNamedEntities = 0;
    NumberOfIncorrectNamedEntities = 0;
    NumberOfMissingNamedEntities = 0;
    XmlAttributeAccuracy = 0.0d;
    XmlAttributePrecision = 0.0d;
    XmlAttributeRecall = 0.0d;
    CiXmlAttributeAccuracy = "N/A";
    CiXmlAttributePrecision = "N/A";
    CiXmlAttributeRecall = "N/A";
        
    MyNumberFormatter = NumberFormat.getInstance(Locale.US);
    MyNumberFormatter.setMinimumFractionDigits(4);
    MyNumberFormatter.setMaximumFractionDigits(4);
    
    ListenerList = new EventListenerList();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
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
    }
    return 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getRowCount() {
    
    return NUMBER_OF_ROWS;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getColumnCount() { 
    
    return NUMBER_OF_COLUMNS;
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getColumnName(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return "Measure Of Tagging Quality"; }
      case 1: { return "Value in Evaluated Sample"; }
      case 2: { return "Approx. 95% Conf. Interval"; }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Class getColumnClass(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return (new String("")).getClass(); }
      case 1: { return (new String("")).getClass(); }
      case 2: { return (new String("")).getClass(); }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isCellEditable(int pRowIndex, int pColumnIndex) {
    
      return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object getValueAt(int pRowIndex, int pColumnIndex) {
    
    if (pRowIndex < 0 || pRowIndex >= NUMBER_OF_ROWS) {
      return null;
    }
    
    switch (pColumnIndex) {
      case 0: {
        return MEASURES_OF_TAGGING_QUALITY[pRowIndex];
      }
      case 1: {
        switch (pRowIndex) {
          case TRUE_NUMBER_OF_NAMED_ENTITIES: {
            return String.valueOf(TrueNumberOfNamedEntities);
          }
          case NUMBER_OF_COMPL_CORRECT_NAMED_ENTITIES: {
            return String.valueOf(NumberOfComplCorrectNamedEntities);
          }
          case NUMBER_OF_PART_CORRECT_NAMED_ENTITIES: {
            return String.valueOf(NumberOfPartCorrectNamedEntities);
          }
          case NUMBER_OF_INCORRECT_NAMED_ENTITIES: {
            return String.valueOf(NumberOfIncorrectNamedEntities);
          }
          case NUMBER_OF_MISSING_NAMED_ENTITIES: {
            return String.valueOf(NumberOfMissingNamedEntities);
          }
          case XML_ATTRIBUTE_ACCURACY: {
            return MyNumberFormatter.format(XmlAttributeAccuracy);
          }
          case XML_ATTRIBUTE_PRECISION: {
            return MyNumberFormatter.format(XmlAttributePrecision);
          }
          case XML_ATTRIBUTE_RECALL: {
            return MyNumberFormatter.format(XmlAttributeRecall);
          }
        }
      }
      case 2: {
        switch (pRowIndex) {
          case TRUE_NUMBER_OF_NAMED_ENTITIES: {
            return "N/A";
          }
          case NUMBER_OF_COMPL_CORRECT_NAMED_ENTITIES: {
            return "N/A";
          }
          case NUMBER_OF_PART_CORRECT_NAMED_ENTITIES: {
            return "N/A";
          }
          case NUMBER_OF_INCORRECT_NAMED_ENTITIES: {
            return "N/A";
          }
          case NUMBER_OF_MISSING_NAMED_ENTITIES: {
            return "N/A";
          }
          case XML_ATTRIBUTE_ACCURACY: {
            return CiXmlAttributeAccuracy;
          }
          case XML_ATTRIBUTE_PRECISION: {
            return CiXmlAttributePrecision;
          }
          case XML_ATTRIBUTE_RECALL: {
            return CiXmlAttributeRecall;
          }
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
  
  public void memorizeOneCompleteTextUnitEvaluation(
  int pTrueNumberOfNamedEntities, int pNumberOfComplCorrectNamedEntities,
  int pNumberOfPartCorrectNamedEntities,
  int pNumberOfIncorrectNamedEntities, int pNumberOfMissingNamedEntities,
  boolean pUpdateKTable) {
    
    TrueNumberOfNamedEntities += pTrueNumberOfNamedEntities;
    NumberOfComplCorrectNamedEntities += pNumberOfComplCorrectNamedEntities;
    NumberOfPartCorrectNamedEntities += pNumberOfPartCorrectNamedEntities;
    NumberOfIncorrectNamedEntities += pNumberOfIncorrectNamedEntities;
    NumberOfMissingNamedEntities += pNumberOfMissingNamedEntities;
    
    if (TrueNumberOfNamedEntities > 0) {
      XmlAttributeAccuracy = (NumberOfComplCorrectNamedEntities
      + (0.5d * NumberOfPartCorrectNamedEntities))
      / (double)TrueNumberOfNamedEntities;
    }
    if ((NumberOfComplCorrectNamedEntities + NumberOfPartCorrectNamedEntities
    + NumberOfIncorrectNamedEntities) > 0) {
      XmlAttributePrecision = (NumberOfComplCorrectNamedEntities
      + (0.5d * NumberOfPartCorrectNamedEntities))
      / (double)(NumberOfComplCorrectNamedEntities
      + (0.5d * NumberOfPartCorrectNamedEntities)
      + NumberOfIncorrectNamedEntities);
    }
    if ((NumberOfComplCorrectNamedEntities + NumberOfPartCorrectNamedEntities
    + NumberOfMissingNamedEntities) > 0) {
      XmlAttributeRecall = (NumberOfComplCorrectNamedEntities
      + (0.5d * NumberOfPartCorrectNamedEntities))
      / (double)(NumberOfComplCorrectNamedEntities
      + (0.5d * NumberOfPartCorrectNamedEntities)
      + NumberOfMissingNamedEntities);
    }
    
    if (TrueNumberOfNamedEntities > 30) {
      TmpDouble = 1.96 * Math.sqrt(
      XmlAttributeAccuracy * (1.0d - XmlAttributeAccuracy)
      / (double)TrueNumberOfNamedEntities);
      CiXmlAttributeAccuracy = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      XmlAttributeAccuracy - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      XmlAttributeAccuracy + TmpDouble)) + "]";
      TmpDouble = 1.96 * Math.sqrt(
      XmlAttributePrecision * (1.0d - XmlAttributePrecision)
      / (double)(NumberOfComplCorrectNamedEntities 
      + (0.5d * NumberOfPartCorrectNamedEntities)
      + NumberOfIncorrectNamedEntities));
      CiXmlAttributePrecision = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      XmlAttributePrecision - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      XmlAttributePrecision + TmpDouble)) + "]";
      TmpDouble = 1.96 * Math.sqrt(
      XmlAttributeRecall * (1.0d - XmlAttributeRecall)
      / (double)(NumberOfComplCorrectNamedEntities
      + (0.5d * NumberOfPartCorrectNamedEntities)
      + NumberOfMissingNamedEntities));
      CiXmlAttributeRecall = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      XmlAttributeRecall - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      XmlAttributeRecall + TmpDouble)) + "]";
    }

    if (pUpdateKTable) {
      this.fireTableDataChanged();
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void appendHtmlSection(TextFile pHtmlFile) {
    
    if (pHtmlFile == null) {
      return;
    }
    
    pHtmlFile.setNextLine("<h3>Quality of XML Tag Attributes</h3>");
    pHtmlFile.setNextLine("<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">" + this.getColumnName(0) + "</th>"
    + "<th align=\"left\" valign=\"top\">" + this.getColumnName(1) + "</th>"
    + "<th align=\"left\" valign=\"top\">"  + this.getColumnName(2) + "</th>"
    + "</tr>");
    for (int i = 0; i < NUMBER_OF_ROWS; i++) {
      pHtmlFile.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">" + this.getValueAt(i, 0) + "</td>"
      + "<td align=\"left\" valign=\"top\">" + this.getValueAt(i, 1) + "</td>"
      + "<td align=\"left\" valign=\"top\">" + this.getValueAt(i, 2) + "</td>"
      + "</tr>");
    }
    pHtmlFile.setNextLine("</table>");
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String[] pOptions) {}
  
}