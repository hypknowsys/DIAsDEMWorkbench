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
import org.hypknowsys.core.ScriptTask;
import org.hypknowsys.core.ScriptableTaskParameter;
import org.hypknowsys.core.ScriptableTaskResult;
import org.hypknowsys.diasdem.core.DIAsDEMscriptTask;
import org.hypknowsys.diasdem.core.DiasdemException;
import org.hypknowsys.misc.util.Tools;
import org.jdom.Element;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMscriptTask implements DIAsDEMscriptTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String Label = null;
  protected String ClassName = null;
  protected ScriptableTaskParameter Parameter = null;
  protected ScriptableTaskResult Result = null;
  protected String Notes = null;
  protected String Log = null;
  protected int Status = ScriptTask.TASK_NOT_EXECUTED;
  protected String StartTimeStamp = null;
  protected String EndTimeStamp = null;
  protected boolean Execute = true;
  protected transient String ScriptLabel = null;
  protected transient int TaskNumber = -1;
  
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
  
  public DefaultDIAsDEMscriptTask() {
    
    Label = null;
    ClassName = null;
    Parameter = null;
    Result = null;
    Notes = null;
    Log = null;
    Execute = true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMscriptTask(String pLabel, String pClassName,
  ScriptableTaskParameter pParameter, String pNotes) {
    
    Label = pLabel;
    ClassName = pClassName;
    Parameter = pParameter;
    Result = null;
    Notes = pNotes;
    Log = null;
    Execute = true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getLabel() {
    return Label; }
  public String getClassName() {
    return ClassName; }
  public ScriptableTaskParameter getParameter() {
    return Parameter; }
  public ScriptableTaskResult getResult() {
    return Result; }
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
  public String getTransientScriptLabel() {
    return ScriptLabel; }
  public int getTransientTaskNumber() {
    return TaskNumber; }
  public boolean execute() {
    return Execute; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setLabel(String pLabel) {
    Label = pLabel; }
  public void setClassName(String pClassName) {
    ClassName = pClassName; }
  public void setParameter(ScriptableTaskParameter pParameter) {
    Parameter = pParameter; }
  public void setResult(ScriptableTaskResult pResult) {
    Result = pResult; }
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
  public void setTransientScriptLabel(String pTransientScriptLabel) {
    ScriptLabel = pTransientScriptLabel; }
  public void setTransientTaskNumber(int pTransientTaskNumber) {
    TaskNumber = pTransientTaskNumber; }
  public void setExecute(boolean pExecute) {
    Execute = pExecute; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(100);
    TmpStringBuffer.append("DIAsDEM Script Task, Label:");
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
  /* ########## interface ScriptTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addLog(String pLog) {
    
    if (Log != null && Log.length() > 0) {
      Log += "\n" + pLog;
    }
    else {
      Log = pLog;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFromJDomElement(Element pJDomElement) throws DiasdemException {
    
    // assuming that pJDomElement has been validated agianst DTD
    
    Label = null;
    ClassName = null;
    Parameter = null;
    Result = null;
    Notes = null;
    Log = null;
    Status = ScriptTask.TASK_NOT_EXECUTED;
    StartTimeStamp = null;
    EndTimeStamp = null;
    Execute = true;
    
    ClassName = pJDomElement.getAttributeValue("ClassName");
    Execute = Tools.string2Boolean(pJDomElement.getAttributeValue("Execute"));
    
    Element jDomElement = pJDomElement.getChild("Label");
    Label = jDomElement.getTextTrim();
    
    jDomElement = pJDomElement.getChild("Parameter");
    String parameterClassName = jDomElement.getAttributeValue("ClassName");
    try {
      Parameter = (ScriptableTaskParameter)Class.forName(parameterClassName)
      .getConstructor(null).newInstance(null);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw new DiasdemException("Error: Scriptable task parameter "
      + parameterClassName + " cannot be instantiated!");
    }
    Parameter.setParameterAttributesFromJDomElement(jDomElement);
    
    jDomElement = pJDomElement.getChild("Result");
    if (jDomElement != null) {
      String resultClassName = jDomElement.getAttributeValue("ClassName");
      try {
        Result = (ScriptableTaskResult)Class.forName(resultClassName)
        .getConstructor(null).newInstance(null);
      }
      catch(Exception e) {
        e.printStackTrace();
        throw new DiasdemException("Error: Scriptable task result "
        + resultClassName + " cannot be instantiated!");
      }
      Result.setResultAttributesFromJDomElement(jDomElement);
    }
    
    jDomElement = pJDomElement.getChild("Notes");
    if (jDomElement != null) {
      Notes = jDomElement.getTextTrim();
    }
    
    jDomElement = pJDomElement.getChild("Log");
    if (jDomElement != null) {
      Log = jDomElement.getTextTrim();
    }
    
    jDomElement = pJDomElement.getChild("Status");
    if (jDomElement != null) {
      Status = Tools.string2Int(jDomElement.getTextTrim());
    }
    
    jDomElement = pJDomElement.getChild("StartTimeStamp");
    if (jDomElement != null) {
      StartTimeStamp = jDomElement.getTextTrim();
    }
    
    jDomElement = pJDomElement.getChild("EndTimeStamp");
    if (jDomElement != null) {
      EndTimeStamp = jDomElement.getTextTrim();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Element getAsJDomElement() {
    
    Element scriptTaskElement = new Element("DIAsDEMscriptTask");
    if (ClassName != null) {
      scriptTaskElement.setAttribute("ClassName", ClassName);
    }
    scriptTaskElement.setAttribute("Execute", Tools.boolean2String(Execute));
    
    Element labelElement = new Element("Label");
    if (Label != null) {
      labelElement.addContent(Label);
      scriptTaskElement.addContent(labelElement);
    }
    
    if (Parameter != null && Parameter.getClassName() != null
    && Parameter.getParameterAttributesAsJDomElement() != null) {
      Element parameterElement = new Element("Parameter");
      parameterElement.setAttribute("ClassName", Parameter.getClassName());
      parameterElement.addContent(Parameter
      .getParameterAttributesAsJDomElement());
      scriptTaskElement.addContent(parameterElement);
    }
    
    if (Result != null && Result.getClassName() != null
    && Result.getResultAttributesAsJDomElement() != null) {
      Element resultElement = new Element("Result");
      resultElement.setAttribute("ClassName", Result.getClassName());
      resultElement.addContent(Result.getResultAttributesAsJDomElement());
      scriptTaskElement.addContent(resultElement);
    }
    if (Notes != null) {
      Element notesElement = new Element("Notes");
      notesElement.addContent(Notes);
      scriptTaskElement.addContent(notesElement);
    }
    if (Log != null) {
      Element logElement = new Element("Log");
      logElement.addContent(Log);
      scriptTaskElement.addContent(logElement);
    }
    if (Status == ScriptTask.TASK_NOT_EXECUTED
    || Status == ScriptTask.TASK_EXECUTED_WITHOUT_ERRORS
    || Status == ScriptTask.TASK_EXECUTED_WITH_ERRORS) {
      Element statusElement = new Element("Status");
      statusElement.addContent(Tools.int2String(Status));
      scriptTaskElement.addContent(statusElement);
    }
    if (StartTimeStamp != null) {
      Element startElement = new Element("StartTimeStamp");
      startElement.addContent(StartTimeStamp);
      scriptTaskElement.addContent(startElement);
    }
    if (EndTimeStamp != null) {
      Element endElement = new Element("EndTimeStamp");
      endElement.addContent(EndTimeStamp);
      scriptTaskElement.addContent(endElement);
    }
    
    return scriptTaskElement;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getStatusString() {
    
    String status = "";
    switch (Status) {
      case ScriptTask.TASK_NOT_EXECUTED: {
        status = "TASK_NOT_EXECUTED";
        break;
      }
      case ScriptTask.TASK_EXECUTED_WITHOUT_ERRORS: {
        status = "TASK_EXECUTED_WITHOUT_ERRORS";
        break;
      }
      case ScriptTask.TASK_EXECUTED_WITH_ERRORS: {
        status = "TASK_EXECUTED_WITH_ERRORS";
        break;
      }
    }
    return status;
    
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
  
  public static void main(String pOptions[]) {}
  
}