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

package org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public class DeriveConceptualDtdParameter
extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String DtdFileName = null;
  protected String DtdRootElement = null;
  protected double MinAttributeRelSupport = 0.0;
  protected double RandomSampleSize = 0.0;
  protected String RandomSampleFileName = null;
  protected boolean CreateTagByDocumentFile = false;
  protected boolean CreateWumFiles = false;
  protected boolean CreateGateFiles = false;
  protected boolean CreateHtmlFile = false;
  protected String GateDirectory = null;
  protected String HtmlDirectory = null;
  protected String XmlDocumentsDirectory = null;
  protected String DtdDocumentationFileName = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22"
  + ".DeriveConceptualDtdTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.deriveConceptualDtd22"
  + ".DeriveConceptualDtdParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String DTD_FILE_NAME =
  "DtdFileName";
  private final static String DTD_ROOT_ELEMENT =
  "DtdRootElement";
  private final static String MIN_ATTRIBUTE_REL_SUPPORT =
  "MinAttributeRelSupport";
  private final static String DTD_DOCUMENTATION_FILE_NAME =
  "DtdDocumentationFileName";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DeriveConceptualDtdParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;

    CollectionFileName = null;
    DtdFileName = null;
    DtdRootElement = null;
    MinAttributeRelSupport = 0.0;
    DtdDocumentationFileName = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DeriveConceptualDtdParameter(String pCollectionFileName, 
  String pDtdFileName, String pDtdRootElement, double pMinAttributeRelSupport,
  String pDtdDocumentationFileName) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    DtdFileName = pDtdFileName;
    DtdRootElement = pDtdRootElement;
    MinAttributeRelSupport = pMinAttributeRelSupport;
    DtdDocumentationFileName = pDtdDocumentationFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() { 
    return CollectionFileName; }
  public String getDtdFileName() { 
    return DtdFileName; }
  public String getDtdRootElement() { 
    return DtdRootElement; }
  public double getMinAttributeRelSupport() { 
    return MinAttributeRelSupport; }
 public String getDtdDocumentationFileName() { 
    return DtdDocumentationFileName; }
  
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
    ParameterAttributes.put(DTD_FILE_NAME, DtdFileName);
    ParameterAttributes.put(DTD_ROOT_ELEMENT, DtdRootElement);
    ParameterAttributes.put(MIN_ATTRIBUTE_REL_SUPPORT, 
    Tools.double2String(MinAttributeRelSupport));
    ParameterAttributes.put(DTD_DOCUMENTATION_FILE_NAME, 
    DtdDocumentationFileName);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    DtdFileName = (String)ParameterAttributes
    .get(DTD_FILE_NAME);
    DtdRootElement = (String)ParameterAttributes
    .get(DTD_ROOT_ELEMENT);
    MinAttributeRelSupport = Tools.string2Double(
    (String)ParameterAttributes.get(MIN_ATTRIBUTE_REL_SUPPORT));
    DtdDocumentationFileName = (String)ParameterAttributes
    .get(DTD_DOCUMENTATION_FILE_NAME);
    
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