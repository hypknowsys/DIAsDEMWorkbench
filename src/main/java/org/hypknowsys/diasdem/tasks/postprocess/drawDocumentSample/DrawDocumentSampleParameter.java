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

package org.hypknowsys.diasdem.tasks.postprocess.drawDocumentSample;

import java.util.TreeMap;
import org.hypknowsys.diasdem.server.DiasdemScriptableTaskParameter;
import org.hypknowsys.misc.util.Tools;
import org.jdom.Element;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DrawDocumentSampleParameter
extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String DtdFileName = null;
  protected double RandomSampleSize = 0.0;
  protected String RandomSampleFileName = null;
  protected String HtmlDirectory = null;
  protected int SamplingMode = CREATE_RANDOM_SAMPLE_FILE;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.drawDocumentSample"
  + ".DrawDocumentSampleTask";
  private static final String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.postprocess.drawDocumentSample"
  + ".DrawDocumentSampleParameterPanel";
  
  private static final String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private static final String DTD_FILE_NAME =
  "DtdFileName";
  private static final String RANDOM_SAMPLE_SIZE =
  "RandomSampleSize";
  private static final String RANDOM_SAMPLE_FILE_NAME =
  "RandomSampleFileName";
  private static final String HTML_DIRECTORY =
  "HtmlDirectory";
  private static final String SAMPLING_MODE =
  "SamplingMode";
  
  public static final int CREATE_RANDOM_SAMPLE_FILE = 0;
  public static final int APPLY_RANDOM_SAMPLE_FILE = 1;
  public static final String[] SAMPLING_MODES = {
    "Create Random Sample File of DIAsDEM Documents",
    "Apply Random Sample File of DIAsDEM Documents"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DrawDocumentSampleParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
    CollectionFileName = null;
    DtdFileName = null;
    RandomSampleSize = 0.0;
    RandomSampleFileName = null;
    HtmlDirectory = null;
    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DrawDocumentSampleParameter(String pCollectionFileName,
  String pDtdFileName, double pRandomSampleSize, String pRandomSampleFileName,
  String pHtmlDirectory, String pSamplingMode) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    DtdFileName = pDtdFileName;
    RandomSampleSize = pRandomSampleSize;
    RandomSampleFileName = pRandomSampleFileName;
    HtmlDirectory = pHtmlDirectory;
    
    for (int i = 0; i < SAMPLING_MODES.length; i++) {
      if (SAMPLING_MODES[i].equals(pSamplingMode)) {
        SamplingMode = i;
        break;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DrawDocumentSampleParameter(String pCollectionFileName,
  String pDtdFileName, double pRandomSampleSize, String pRandomSampleFileName,
  String pHtmlDirectory, int pSamplingMode) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    DtdFileName = pDtdFileName;
    RandomSampleSize = pRandomSampleSize;
    RandomSampleFileName = pRandomSampleFileName;
    HtmlDirectory = pHtmlDirectory;
    SamplingMode = pSamplingMode;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getDtdFileName() {
    return DtdFileName; }
  public double getRandomSampleSize() {
    return RandomSampleSize; }
  public String getRandomSampleFileName() {
    return RandomSampleFileName; }
  public String getHtmlDirectory() {
    return HtmlDirectory; }
  public int getSamplingMode() {
    return SamplingMode; }
  
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
    ParameterAttributes.put(DTD_FILE_NAME, DtdFileName);
    ParameterAttributes.put(RANDOM_SAMPLE_SIZE,
    Tools.double2String(RandomSampleSize));
    ParameterAttributes.put(RANDOM_SAMPLE_FILE_NAME, RandomSampleFileName);
    ParameterAttributes.put(HTML_DIRECTORY, HtmlDirectory);
    ParameterAttributes.put(SAMPLING_MODE,
    Tools.int2String(SamplingMode));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    DtdFileName = (String)ParameterAttributes
    .get(DTD_FILE_NAME);
    RandomSampleSize = Tools.string2Double(
    (String)ParameterAttributes.get(RANDOM_SAMPLE_SIZE));
    RandomSampleFileName = (String)ParameterAttributes
    .get(RANDOM_SAMPLE_FILE_NAME);
    HtmlDirectory = (String)ParameterAttributes
    .get(HTML_DIRECTORY);
    SamplingMode = Tools.string2Int(
    (String)ParameterAttributes.get(SAMPLING_MODE));
    
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