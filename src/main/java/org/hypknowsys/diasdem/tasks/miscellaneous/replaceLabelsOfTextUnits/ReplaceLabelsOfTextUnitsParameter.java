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

package org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits;

import java.util.TreeMap;
import org.hypknowsys.diasdem.server.DiasdemScriptableTaskParameter;
import org.jdom.Element;

/**
 * @version 2.2, 7 May 2006
 * @author Karsten Winkler
 */

public class ReplaceLabelsOfTextUnitsParameter
extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String FindLabel = null;
  protected String ReplaceWith = null;
  protected String LogFileName = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits"
  + ".ReplaceLabelsOfTextUnitsTask";
  private static final String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.replaceLabelsOfTextUnits"
  + ".ReplaceLabelsOfTextUnitsParameterPanel";
  
  private static final String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private static final String FIND_LABEL =
  "FindLabel";
  private static final String REPLACE_WITH =
  "ReplaceWith";
  private static final String LOG_FILE_NAME =
  "LogFileName";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceLabelsOfTextUnitsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    CollectionFileName = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ReplaceLabelsOfTextUnitsParameter(String pCollectionFileName,
  String pFindLabel, String pReplaceWith, String pLogFileName) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    FindLabel = pFindLabel;
    ReplaceWith = pReplaceWith;
    LogFileName = pLogFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getFindLabel() {
    return FindLabel; }
  public String getReplaceWith() {
    return ReplaceWith; }
  public String getLogFileName() {
    return LogFileName; }
  
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
  /* ########## interface ScriptableTaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(FIND_LABEL, FindLabel);
    ParameterAttributes.put(REPLACE_WITH, ReplaceWith);
    ParameterAttributes.put(LOG_FILE_NAME, LogFileName);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes.get(COLLECTION_FILE_NAME);
    FindLabel = (String)ParameterAttributes.get(FIND_LABEL);
    ReplaceWith = (String)ParameterAttributes.get(REPLACE_WITH);
    LogFileName = (String)ParameterAttributes.get(LOG_FILE_NAME);
    
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