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

package org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DeriveStructuredDtdParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String UnstructuredDtdFileName = null;
  protected String StructuredDtdFileName = null;
  protected String WumAssocRulesFileName = null;
  protected String WumSequencesFileName = null;
  protected double MinTagRelSupport = 0.0;
  protected double MinAssocRelSupport = 0.0;
  protected double MinAssocConfidence = 0.0;
  protected double MinAssocLift = 0.0;
  protected double MinSeqRelSupport = 0.0;
  protected double MinSeqConfidence = 0.0;
  protected double MinSeqLift = 0.0;


  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd.DeriveStructuredDtdTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd.DeriveStructuredDtdParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String UNSTRUCTURED_DTD_FILE_NAME =
  "UnstructuredDtdFileName";
  private final static String STRUCTURED_DTD_FILE_NAME =
  "StructuredDtdFileName";
  private final static String WUM_ASSOC_RULES_FILE_NAME =
  "WumAssocRulesFileName";
  private final static String WUM_SEQUENCES_FILE_NAME =
  "WumSequencesFileName";
  private final static String MIN_TAG_REL_SUPPORT =
  "MinTagRelSupport";
  private final static String MIN_ASSOC_REL_SUPPORT =
  "MinAssocRelSupport";
  private final static String MIN_ASSOC_CONFIDENCE =
  "MinAssocConfidence";
  private final static String MIN_ASSOC_LIFT =
  "MinAssocLift";
  private final static String MIN_SEQ_REL_SUPPORT =
  "MinSeqRelSupport";
  private final static String MIN_SEQ_CONFIDENCE =
  "MinSeqConfidence";
  private final static String MIN_SEQ_LIFT =
  "MinSeqLift";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DeriveStructuredDtdParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;

    CollectionFileName = null;
    UnstructuredDtdFileName = null;
    StructuredDtdFileName = null;
    WumAssocRulesFileName = null;
    WumSequencesFileName = null;
    MinTagRelSupport = 0.0;
    MinAssocRelSupport = 0.0;
    MinAssocConfidence = 0.0;
    MinAssocLift = 0.0;
    MinSeqRelSupport = 0.0;
    MinSeqConfidence = 0.0;
    MinSeqLift = 0.0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DeriveStructuredDtdParameter(String pCollectionFileName, 
  String pUnstructuredDtdFileName, String pStructuredDtdFileName, 
  String pWumAssocRulesFileName, String pWumSequencesFileName, 
  double pMinTagRelSupport, double pMinAssocRelSupport, 
  double pMinAssocConfidence, double pMinAssocLift, double pMinSeqRelSupport, 
  double pMinSeqConfidence, double pMinSeqLift) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    UnstructuredDtdFileName = pUnstructuredDtdFileName;
    StructuredDtdFileName = pStructuredDtdFileName;
    WumAssocRulesFileName = pWumAssocRulesFileName;
    WumSequencesFileName = pWumSequencesFileName;
    MinTagRelSupport = pMinTagRelSupport;
    MinAssocRelSupport = pMinAssocRelSupport;
    MinAssocConfidence = pMinAssocConfidence;
    MinAssocLift = pMinAssocLift;
    MinSeqRelSupport = pMinSeqRelSupport;
    MinSeqConfidence = pMinSeqConfidence;
    MinSeqLift = pMinSeqLift;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getUnstructuredDtdFileName() {
    return UnstructuredDtdFileName; }
  public String getStructuredDtdFileName() { 
    return StructuredDtdFileName; }
  public String getWumAssocRulesFileName() { 
    return WumAssocRulesFileName; }
  public String getWumSequencesFileName() { 
    return WumSequencesFileName; }
  public double getMinTagRelSupport() { 
    return MinTagRelSupport; }
  public double getMinAssocRelSupport() { 
    return MinAssocRelSupport; }
  public double getMinAssocConfidence() { 
    return MinAssocConfidence; }
  public double getMinAssocLift() { 
    return MinAssocLift; }
  public double getMinSeqRelSupport() { 
    return MinSeqRelSupport; }
  public double getMinSeqConfidence() { 
    return MinSeqConfidence; }
  public double getMinSeqLift() { 
    return MinSeqLift; }

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
  
  public org.jdom.Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(UNSTRUCTURED_DTD_FILE_NAME, UnstructuredDtdFileName);
    ParameterAttributes.put(STRUCTURED_DTD_FILE_NAME, StructuredDtdFileName);
    ParameterAttributes.put(WUM_ASSOC_RULES_FILE_NAME, WumAssocRulesFileName);
    ParameterAttributes.put(WUM_SEQUENCES_FILE_NAME, WumSequencesFileName);
    ParameterAttributes.put(MIN_TAG_REL_SUPPORT, 
    Tools.double2String(MinTagRelSupport));
    ParameterAttributes.put(MIN_ASSOC_REL_SUPPORT, 
    Tools.double2String(MinAssocRelSupport));
    ParameterAttributes.put(MIN_ASSOC_CONFIDENCE, 
    Tools.double2String(MinAssocConfidence));
    ParameterAttributes.put(MIN_ASSOC_LIFT, 
    Tools.double2String(MinAssocLift));
    ParameterAttributes.put(MIN_SEQ_REL_SUPPORT, 
    Tools.double2String(MinSeqRelSupport));
    ParameterAttributes.put(MIN_SEQ_CONFIDENCE, 
    Tools.double2String(MinSeqConfidence));
    ParameterAttributes.put(MIN_SEQ_LIFT, 
    Tools.double2String(MinSeqLift));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    UnstructuredDtdFileName = (String)ParameterAttributes
    .get(UNSTRUCTURED_DTD_FILE_NAME);
    StructuredDtdFileName = (String)ParameterAttributes
    .get(STRUCTURED_DTD_FILE_NAME);
    WumAssocRulesFileName = (String)ParameterAttributes
    .get(WUM_ASSOC_RULES_FILE_NAME);
    WumSequencesFileName = (String)ParameterAttributes
    .get(WUM_SEQUENCES_FILE_NAME);
    MinTagRelSupport = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_TAG_REL_SUPPORT));
    MinAssocRelSupport = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_ASSOC_REL_SUPPORT));
    MinAssocConfidence = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_ASSOC_CONFIDENCE));
    MinAssocLift = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_ASSOC_LIFT));
    MinSeqRelSupport = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_SEQ_REL_SUPPORT));
    MinSeqConfidence = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_SEQ_CONFIDENCE));
    MinSeqLift = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_SEQ_LIFT));
    
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
  
  public static void main(String args[]) {}
  
}