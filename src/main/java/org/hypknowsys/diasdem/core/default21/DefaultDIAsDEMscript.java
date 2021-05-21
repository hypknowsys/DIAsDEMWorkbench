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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.hypknowsys.core.Script;
import org.hypknowsys.core.ScriptTask;
import org.hypknowsys.diasdem.core.DIAsDEMscript;
import org.hypknowsys.diasdem.core.DIAsDEMscriptTask;
import org.hypknowsys.diasdem.core.DiasdemException;
import org.hypknowsys.misc.util.Tools;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMscript implements DIAsDEMscript {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String Label = null;
  private ArrayList ScriptTasks = null;
  protected String Notes = null;
  protected String Log = null;
  protected int Status = Script.SCRIPT_NOT_EXECUTED;
  protected String StartTimeStamp = null;
  protected String EndTimeStamp = null;
  protected transient String FileName = null;
  
  private String XmlFileName = null;
  private transient int CurrentScriptTaskIndex = 0;
  private transient DIAsDEMscriptTask CurrentScriptTask = null;
  
  protected EventListenerList ListenerList = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient File TmpFile = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMscript() {
    
    Label = null;
    ScriptTasks = new ArrayList();
    XmlFileName = null;
    CurrentScriptTaskIndex = 0;
    CurrentScriptTask = null;
    ListenerList = new EventListenerList();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMscript(String pXmlFileName) throws DiasdemException {
    
    this();
    this.setFromXmlDocument(pXmlFileName);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getLabel() {
    return Label; }
  public String getNotes() {
    return Notes; }
  public String getLog() {
    return Log; }
  public int getStatus() {
    return Status; }
  public String getStartTimeStamp() {
    return StartTimeStamp; }
  public String getEndTimeStamp() {
    return EndTimeStamp; }
  public String getTransientFileName() {
    return FileName; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setLabel(String pLabel) {
    Label = pLabel; }
  public void setNotes(String pNotes) {
    Notes = pNotes; }
  public void setLog(String pLog) {
    Log = pLog; }
  public void setStatus(int pStatus) {
    Status = pStatus; }
  public void setStartTimeStamp(String pStartTimeStamp) {
    StartTimeStamp = pStartTimeStamp; }
  public void setEndTimeStamp(String pEndTimeStamp) {
    EndTimeStamp = pEndTimeStamp; }
  public void setTransientFileName(String pTransientFileName) {
    FileName = pTransientFileName; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(100);
    TmpStringBuffer.append("DIAsDEM Script: Label=");
    TmpStringBuffer.append(Label);
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object clone() {
    
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException e) {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface KTableModel and fire methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getPreferredColumnWidth(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return 100; }
      case 1: { return 500; }
      case 2: { return 100; }
      case 3: { return 100; }
    }
    return 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getRowCount() {
    
    if (ScriptTasks != null) {
      return ScriptTasks.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getColumnCount() { return 4; }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getColumnName(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return "Number"; }
      case 1: { return "Task Label"; }
      case 2: { return "Status"; }
      case 3: { return "Execute"; }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Class getColumnClass(int pColumnIndex) {
    
    switch (pColumnIndex) {
      case 0: { return (new String("")).getClass(); }
      case 1: { return (new String("")).getClass(); }
      case 2: { return (new String("")).getClass(); }
      case 3: { return (new Boolean(true)).getClass(); }
    }
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isCellEditable(int pRowIndex, int pColumnIndex) {
    
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object getValueAt(int pRowIndex, int pColumnIndex) {
    
    if (ScriptTasks == null || pRowIndex < 0
    || pRowIndex >= ScriptTasks.size()) {
      return null;
    }
    
    switch (pColumnIndex) {
      case 0: {
        return String.valueOf(pRowIndex + 1);
      }
      case 1: {
        return ((DIAsDEMscriptTask)ScriptTasks.get(pRowIndex)).getLabel();
      }
      case 2: {
        return ((DIAsDEMscriptTask)ScriptTasks.get(pRowIndex)).getStatus()
        + "";
      }
      case 3: {
        return new Boolean(
        ((DIAsDEMscriptTask)ScriptTasks.get(pRowIndex)).execute());
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
  
  public void addLog(String pLog) {
    
    if (Log != null && Log.length() > 0 && !pLog.startsWith("\n")) {
      Log += "\n" + pLog;
    }
    else {
      Log = pLog;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int countScriptTasks() {
    
    if (ScriptTasks != null) {
      return ScriptTasks.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addScriptTask(ScriptTask pScriptTask) {
    
    if (pScriptTask instanceof DIAsDEMscriptTask) {
      ScriptTasks.add((DIAsDEMscriptTask)pScriptTask);
      this.fireTableRowsInserted(ScriptTasks.size() - 1,
      ScriptTasks.size() - 1);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void appendScriptTask(ScriptTask pScriptTask, int pTaskIndex) {
    
    this.insertScriptTask(pScriptTask, pTaskIndex + 1);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void insertScriptTask(ScriptTask pScriptTask, int pTaskIndex) {
    
    if (pScriptTask instanceof DIAsDEMscriptTask) {
      if (pTaskIndex >= 0 && pTaskIndex < ScriptTasks.size()) {
        ScriptTasks.add(pTaskIndex, pScriptTask);
        this.fireTableRowsInserted(pTaskIndex, pTaskIndex);
      }
      else {
        this.addScriptTask(pScriptTask);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void deleteScriptTask(int pTaskIndex) {
    
    if (pTaskIndex >= 0 && pTaskIndex < ScriptTasks.size()) {
      Object dummy = ScriptTasks.remove(pTaskIndex);
      this.fireTableRowsDeleted(pTaskIndex, pTaskIndex);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ScriptTask getScriptTask(int pTaskIndex) {
    
    if (pTaskIndex >= 0 && pTaskIndex < ScriptTasks.size()) {
      return (ScriptTask)ScriptTasks.get(pTaskIndex);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceScriptTask(ScriptTask pScriptTask, int pTaskIndex) {
    
    if (pScriptTask instanceof DIAsDEMscriptTask) {
      if (pTaskIndex >= 0 && pTaskIndex < ScriptTasks.size()) {
        ScriptTasks.set(pTaskIndex, pScriptTask);
        this.fireTableRowsUpdated(pTaskIndex, pTaskIndex);
      }
      else {
        this.addScriptTask(pScriptTask);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ScriptTask getFirstScriptTask() {
    
    CurrentScriptTaskIndex = 0;
    return this.getNextScriptTask();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ScriptTask getNextScriptTask() {
    
    if (CurrentScriptTaskIndex < this.countScriptTasks()) {
      return (DIAsDEMscriptTask)ScriptTasks.get(CurrentScriptTaskIndex++);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFromXmlDocument(String pXmlFileName)
  throws DiasdemException {
    
    // assuming that pJDomElement has been validated against DTD
    Document jDomDocument = null;
    Element rootJdomElement = null;
    List jDomElementList = null;
    Element jDomElement = null;
    try {
      File xmlFile = new File(pXmlFileName);
      SAXBuilder jDomSaxBuilder = new SAXBuilder();
      jDomSaxBuilder.setValidation(true);
      jDomDocument = jDomSaxBuilder.build(xmlFile);
      XmlFileName = xmlFile.getAbsolutePath();
      rootJdomElement = jDomDocument.getRootElement();
      Label = rootJdomElement.getChildTextTrim("Label");
      jDomElementList = rootJdomElement.getChildren("DIAsDEMscriptTask");
      if (jDomElementList != null) {
        ScriptTasks = new ArrayList();
        Iterator iterator = jDomElementList.iterator();
        while (iterator.hasNext()) {
          jDomElement = (Element)iterator.next();
          CurrentScriptTask = new DefaultDIAsDEMscriptTask();
          CurrentScriptTask.setFromJDomElement(jDomElement);
          ScriptTasks.add(CurrentScriptTask);
        }
      }
    }
    catch (Exception e) {
      System.err.println("*** Exception in file " + pXmlFileName.toString());
      e.printStackTrace();
      throw new DiasdemException("Error: The script file "
      + Tools.shortenFileName(pXmlFileName, 50)
      + " cannot be opened! Message: " + e.getMessage());
    }
    
    jDomElement = rootJdomElement.getChild("Notes");
    if (jDomElement != null) {
      Notes = jDomElement.getTextTrim();
    }
    
    jDomElement = rootJdomElement.getChild("Log");
    if (jDomElement != null) {
      Log = jDomElement.getTextTrim();
    }
    
    jDomElement = rootJdomElement.getChild("Status");
    if (jDomElement != null) {
      Status = Tools.string2Int(jDomElement.getTextTrim());
    }
    
    jDomElement = rootJdomElement.getChild("StartTimeStamp");
    if (jDomElement != null) {
      StartTimeStamp = jDomElement.getTextTrim();
    }
    
    jDomElement = rootJdomElement.getChild("EndTimeStamp");
    if (jDomElement != null) {
      EndTimeStamp = jDomElement.getTextTrim();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeXmlDocument(String pXmlFileName) {
    
    XmlFileName = pXmlFileName;
    Element rootJdomElement = new Element("DIAsDEMscript");
    DocType dtd = new DocType("DIAsDEMscript");
    dtd.setInternalSubset(this.getInternalDtdSubset());
    Document jdomDocument = new Document(rootJdomElement,
    dtd);
    
    Element labelElement = new Element("Label");
    labelElement.addContent(Label);
    rootJdomElement.addContent(labelElement);
    
    CurrentScriptTask = (DIAsDEMscriptTask)this.getFirstScriptTask();
    while (CurrentScriptTask != null) {
      if (CurrentScriptTask.getAsJDomElement() != null) {
        rootJdomElement.addContent(CurrentScriptTask.getAsJDomElement());
      }
      CurrentScriptTask = (DIAsDEMscriptTask)this.getNextScriptTask();
    }
    
    if (Notes != null) {
      Element notesElement = new Element("Notes");
      notesElement.addContent(Notes);
      rootJdomElement.addContent(notesElement);
    }
    if (Log != null) {
      Element logElement = new Element("Log");
      logElement.addContent(Log);
      rootJdomElement.addContent(logElement);
    }
    if (Status == Script.SCRIPT_NOT_EXECUTED
    || Status == Script.SCRIPT_EXECUTED_WITHOUT_ERRORS
    || Status == Script.SCRIPT_EXECUTED_WITH_ERRORS) {
      Element statusElement = new Element("Status");
      statusElement.addContent(Tools.int2String(Status));
      rootJdomElement.addContent(statusElement);
    }
    if (StartTimeStamp != null) {
      Element startElement = new Element("StartTimeStamp");
      startElement.addContent(StartTimeStamp);
      rootJdomElement.addContent(startElement);
    }
    if (EndTimeStamp != null) {
      Element endElement = new Element("EndTimeStamp");
      endElement.addContent(EndTimeStamp);
      rootJdomElement.addContent(endElement);
    }
    
    Format xmlFormat = Format.getPrettyFormat();
    xmlFormat.setEncoding("UTF-8");
    try {
      XMLOutputter xmlOutputter = new
      XMLOutputter(xmlFormat);
      FileOutputStream fileOutputStream = new FileOutputStream(pXmlFileName);
      xmlOutputter.output(jdomDocument, fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();
    }
    catch (IOException e) {
      System.err.println("Error: DIAsDEM script cannot be saved as "
      + XmlFileName + "!");
    }
    
  }
  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void resetScriptTask(int pTaskIndex) {
    
    ScriptTask task = this.getScriptTask(pTaskIndex);
    if (task != null) {
      task.setResult(null);
      task.setStartTimeStamp(null);
      task.setEndTimeStamp(null);
      task.setLog(null);
      task.setStatus(ScriptTask.TASK_NOT_EXECUTED);
      this.replaceScriptTask(task, pTaskIndex);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void resetAllScriptTasks() {
    
    for (int i = 0; i < this.countScriptTasks(); i++) {
      this.resetScriptTask(i);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void resetScript() {
    
    StartTimeStamp = null;
    EndTimeStamp = null;
    Log = null;
    Status = Script.SCRIPT_NOT_EXECUTED;
    this.resetAllScriptTasks();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getInternalDtdSubset() {
    
    return
    "<!ELEMENT DIAsDEMscript (Label, DIAsDEMscriptTask*, Notes?, Log?,"
    + "  Status?, StartTimeStamp?, EndTimeStamp?)>"
    + ""
    + "<!ELEMENT DIAsDEMscriptTask (Label, Parameter, Result?, Notes?, Log?,"
    + "  Status?, StartTimeStamp?, EndTimeStamp?)>"
    + ""
    + "<!ELEMENT Label (#PCDATA)>"
    + "<!ELEMENT ClassName (#PCDATA)>"
    + "<!ELEMENT Parameter (ParameterAttributes)>"
    + "<!ELEMENT Result (ResultAttributes)>"
    + "<!ELEMENT Notes (#PCDATA)>"
    + "<!ELEMENT Log (#PCDATA)>"
    + "<!ELEMENT Status (#PCDATA)>"
    + "<!ELEMENT StartTimeStamp (#PCDATA)>"
    + "<!ELEMENT EndTimeStamp (#PCDATA)>"
    + ""
    + "<!ELEMENT ParameterAttributes (ParameterAttribute*)>"
    + "<!ELEMENT ResultAttributes (ResultAttribute*)>"
    + ""
    + "<!ELEMENT ParameterAttribute (AttributeName, AttributeValue)>"
    + "<!ELEMENT ResultAttribute (AttributeName, AttributeValue)>"
    + ""
    + "<!ELEMENT AttributeName (#PCDATA)>"
    + "<!ELEMENT AttributeValue (#PCDATA)>"
    + ""
    + "<!ATTLIST DIAsDEMscriptTask "
    + "  ClassName CDATA #IMPLIED"
    + "  Execute CDATA #IMPLIED"
    + ">"
    + "<!ATTLIST Parameter "
    + "  ClassName CDATA #IMPLIED"
    + ">"
    + "<!ATTLIST Result "
    + "  ClassName CDATA #IMPLIED"
    + ">";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getStatusString() {
    
    String status = "";
    switch (Status) {
      case Script.SCRIPT_NOT_EXECUTED: {
        status = "SCRIPT_NOT_EXECUTED";
        break;
      }
      case Script.SCRIPT_EXECUTED_WITHOUT_ERRORS: {
        status = "SCRIPT_EXECUTED_WITHOUT_ERRORS";
        break;
      }
      case Script.SCRIPT_EXECUTED_WITH_ERRORS: {
        status = "SCRIPT_EXECUTED_WITH_ERRORS";
        break;
      }
    }
    return status;
    
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