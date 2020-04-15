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
 * of XML tags
 * @version 2.2, 5 February 2005
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class EvaluatedXmlTagNames implements KTableModel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected int NumberOfTextUnits = 0;
  protected int NumberOfTruePositives = 0;
  protected int NumberOfFalsePositives = 0;
  protected int NumberOfTrueNegatives = 0;
  protected int NumberOfFalseNegatives = 0;
  protected double ProportionOfTruePositives = 0.0d;
  protected double ProportionOfFalsePositives = 0.0d;
  protected double ProportionOfTrueNegatives = 0.0d;
  protected double ProportionOfFalseNegatives = 0.0d;
  protected double XmlTagNameAccuracy = 0.0d;
  protected double XmlTagNamePrecision = 0.0d;
  protected double XmlTagNameRecall = 0.0d;
  protected String CiProportionOfTruePositives = "N/A";
  protected String CiProportionOfFalsePositives = "N/A";
  protected String CiProportionOfTrueNegatives = "N/A";
  protected String CiProportionOfFalseNegatives = "N/A";
  protected String CiXmlTagNameAccuracy = "N/A";
  protected String CiXmlTagNamePrecision = "N/A";
  protected String CiXmlTagNameRecall = "N/A";
  
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
  
  protected static final int NUMBER_OF_ROWS = 12;
  protected static final int NUMBER_OF_COLUMNS = 3;
  
  protected static final String[] MEASURES_OF_TAGGING_QUALITY = {
    "Number of Text Units",
    "Number of True Positives",
    "Number of False Positives",
    "Number of True Negatives",
    "Number of False Negatives",
    "Proportion of True Positives",
    "Proportion of False Positives",
    "Proportion of True Negatives",
    "Proportion of False Negatives",
    "XML Tag Name Accuracy",
    "XML Tag Name Precision",
    "XML Tag Name Recall"
  };

  protected static final int NUMBER_OF_TEXT_UNITS = 0;
  protected static final int NUMBER_OF_TRUE_POSITIVES = 1;
  protected static final int NUMBER_OF_FALSE_POSITIVES = 2;
  protected static final int NUMBER_OF_TRUE_NEGATIVES = 3;
  protected static final int NUMBER_OF_FALSE_NEGATIVES = 4;
  protected static final int PROPORTION_OF_TRUE_POSITIVES = 5;
  protected static final int PROPORTION_OF_FALSE_POSITIVES = 6;
  protected static final int PROPORTION_OF_TRUE_NEGATIVES = 7;
  protected static final int PROPORTION_OF_FALSE_NEGATIVES = 8;
  protected static final int XML_TAG_NAME_ACCURACY = 9;
  protected static final int XML_TAG_NAME_PRECISION = 10;
  protected static final int XML_TAG_NAME_RECALL = 11;
  
  public static final int TRUE_POSITIVE = 0;
  public static final int FALSE_POSITIVE = 1;
  public static final int TRUE_NEGATIVE = 2;
  public static final int FALSE_NEGATIVE = 3;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public EvaluatedXmlTagNames() {

    NumberOfTextUnits = 0;
    NumberOfTruePositives = 0;
    NumberOfFalsePositives = 0;
    NumberOfTrueNegatives = 0;
    NumberOfFalseNegatives = 0;
    XmlTagNameAccuracy = 0.0d;
    XmlTagNamePrecision = 0.0d;
    XmlTagNameRecall = 0.0d;
    CiProportionOfTruePositives = "N/A";
    CiProportionOfFalsePositives = "N/A";
    CiProportionOfTrueNegatives = "N/A";
    CiProportionOfFalseNegatives = "N/A";
    CiXmlTagNameAccuracy = "N/A";
    CiXmlTagNamePrecision = "N/A";
    CiXmlTagNameRecall = "N/A";
        
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
          case NUMBER_OF_TEXT_UNITS: {
            return String.valueOf(NumberOfTextUnits);
          }
          case NUMBER_OF_TRUE_POSITIVES: {
            return String.valueOf(NumberOfTruePositives);
          }
          case NUMBER_OF_FALSE_POSITIVES: {
            return String.valueOf(NumberOfFalsePositives);
          }
          case NUMBER_OF_TRUE_NEGATIVES: {
            return String.valueOf(NumberOfTrueNegatives);
          }
          case NUMBER_OF_FALSE_NEGATIVES: {
            return String.valueOf(NumberOfFalseNegatives);
          }
          case PROPORTION_OF_TRUE_POSITIVES: {
            return MyNumberFormatter.format(ProportionOfTruePositives);
          }
          case PROPORTION_OF_FALSE_POSITIVES: {
            return MyNumberFormatter.format(ProportionOfFalsePositives);
          }
          case PROPORTION_OF_TRUE_NEGATIVES: {
            return MyNumberFormatter.format(ProportionOfTrueNegatives);
          }
          case PROPORTION_OF_FALSE_NEGATIVES: {
            return MyNumberFormatter.format(ProportionOfFalseNegatives);
          }
          case XML_TAG_NAME_ACCURACY: {
            return MyNumberFormatter.format(XmlTagNameAccuracy);
          }
          case XML_TAG_NAME_PRECISION: {
            return MyNumberFormatter.format(XmlTagNamePrecision);
          }
          case XML_TAG_NAME_RECALL: {
            return MyNumberFormatter.format(XmlTagNameRecall);
          }
        }
      }
      case 2: {
        switch (pRowIndex) {
          case NUMBER_OF_TEXT_UNITS: {
            return "N/A";
          }
          case NUMBER_OF_TRUE_POSITIVES: {
            return "N/A";
          }
          case NUMBER_OF_FALSE_POSITIVES: {
            return "N/A";
          }
          case NUMBER_OF_TRUE_NEGATIVES: {
            return "N/A";
          }
          case NUMBER_OF_FALSE_NEGATIVES: {
            return "N/A";
          }
          case PROPORTION_OF_TRUE_POSITIVES: {
            return CiProportionOfTruePositives;
          }
          case PROPORTION_OF_FALSE_POSITIVES: {
            return CiProportionOfFalsePositives;
          }
          case PROPORTION_OF_TRUE_NEGATIVES: {
            return CiProportionOfTrueNegatives;
          }
          case PROPORTION_OF_FALSE_NEGATIVES: {
            return CiProportionOfFalseNegatives;
          }
          case XML_TAG_NAME_ACCURACY: {
            return CiXmlTagNameAccuracy;
          }
          case XML_TAG_NAME_PRECISION: {
            return CiXmlTagNamePrecision;
          }
          case XML_TAG_NAME_RECALL: {
            return CiXmlTagNameRecall;
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
  
  public void memorizeOneTextUnitEvaluation(int pEvaluation, 
  boolean pUpdateKTable) {
    
    switch (pEvaluation) {
      case TRUE_POSITIVE: { 
        NumberOfTextUnits++;
        NumberOfTruePositives++;
        break;
      }
      case FALSE_POSITIVE: {
        NumberOfTextUnits++;
        NumberOfFalsePositives++;
        break;
      }
      case TRUE_NEGATIVE: {
        NumberOfTextUnits++;
        NumberOfTrueNegatives++;
        break;
      }
      case FALSE_NEGATIVE: {
        NumberOfTextUnits++;
        NumberOfFalseNegatives++;
        break;
      }
    }
    ProportionOfTruePositives = NumberOfTruePositives
    / (double)NumberOfTextUnits;
    ProportionOfFalsePositives = NumberOfFalsePositives
    / (double)NumberOfTextUnits;
    ProportionOfTrueNegatives = NumberOfTrueNegatives
    / (double)NumberOfTextUnits;
    ProportionOfFalseNegatives = NumberOfFalseNegatives
    / (double)NumberOfTextUnits;
    XmlTagNameAccuracy = (NumberOfTruePositives + NumberOfTrueNegatives)
    / (double)NumberOfTextUnits;
    XmlTagNamePrecision = NumberOfTruePositives
    / (double)(NumberOfTruePositives + NumberOfFalsePositives);
    XmlTagNameRecall = NumberOfTruePositives
    / (double)(NumberOfTruePositives + NumberOfFalseNegatives);
    if (NumberOfTextUnits > 30) {
      TmpDouble = 1.96 * Math.sqrt(
      ProportionOfTruePositives * (1.0d - ProportionOfTruePositives)
      / (double)NumberOfTextUnits);
      CiProportionOfTruePositives = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      ProportionOfTruePositives - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      ProportionOfTruePositives + TmpDouble)) + "]";
      TmpDouble = 1.96 * Math.sqrt(
      ProportionOfFalsePositives * (1.0d - ProportionOfFalsePositives)
      / (double)NumberOfTextUnits);
      CiProportionOfFalsePositives = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      ProportionOfFalsePositives - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      ProportionOfFalsePositives + TmpDouble)) + "]";
      TmpDouble = 1.96 * Math.sqrt(
      ProportionOfTrueNegatives * (1.0d - ProportionOfTrueNegatives)
      / (double)NumberOfTextUnits);
      CiProportionOfTrueNegatives = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      ProportionOfTrueNegatives - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      ProportionOfTrueNegatives + TmpDouble)) + "]";
      TmpDouble = 1.96 * Math.sqrt(
      ProportionOfFalseNegatives * (1.0d - ProportionOfFalseNegatives)
      / (double)NumberOfTextUnits);
      CiProportionOfFalseNegatives = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      ProportionOfFalseNegatives - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      ProportionOfFalseNegatives + TmpDouble)) + "]";
      TmpDouble = 1.96 * Math.sqrt(
      XmlTagNameAccuracy * (1.0d - XmlTagNameAccuracy)
      / (double)NumberOfTextUnits);
      CiXmlTagNameAccuracy = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      XmlTagNameAccuracy - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      XmlTagNameAccuracy + TmpDouble)) + "]";
      TmpDouble = 1.96 * Math.sqrt(
      XmlTagNamePrecision * (1.0d - XmlTagNamePrecision)
      / (double)(NumberOfTruePositives + NumberOfFalsePositives));
      CiXmlTagNamePrecision = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      XmlTagNamePrecision - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      XmlTagNamePrecision + TmpDouble)) + "]";
      TmpDouble = 1.96 * Math.sqrt(
      XmlTagNameRecall * (1.0d - XmlTagNameRecall)
      / (double)(NumberOfTruePositives + NumberOfFalseNegatives));
      CiXmlTagNameRecall = "["
      + MyNumberFormatter.format(Math.max(0.0d, 
      XmlTagNameRecall - TmpDouble)) + "; "
      + MyNumberFormatter.format(Math.min(1.0d,
      XmlTagNameRecall + TmpDouble)) + "]";
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
    
    pHtmlFile.setNextLine("<h3>Quality of XML Tag Names</h3>");
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