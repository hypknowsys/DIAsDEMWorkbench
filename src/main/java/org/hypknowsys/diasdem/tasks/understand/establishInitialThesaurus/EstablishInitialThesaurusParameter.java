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

package org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class EstablishInitialThesaurusParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionStatisticsFileName = null;
  protected String ThesaurusFileName = null;
  protected int MinTermFrequency = 0;
  protected int MaxTermFrequency = 0;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus.EstablishInitialThesaurusTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.establishInitialThesaurus.EstablishInitialThesaurusParameterPanel";
  
  private final static String COLLECTION_STATISTICS_FILE_NAME =
  "CollectionStatisticsFileName";
  private final static String THESAURUS_FILE_NAME =
  "ThesaurusFileName";
  private final static String MIN_TERM_FREQUENCY =
  "MinTermFrequency";
  private final static String MAX_TERM_FREQUENCY =
  "MaxTermFrequency";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public EstablishInitialThesaurusParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;

    CollectionStatisticsFileName = null;
    ThesaurusFileName = null;
    MinTermFrequency = 0;
    MaxTermFrequency = 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public EstablishInitialThesaurusParameter(String pCollectionStatisticsFileName, 
  String pThesaurusFileName, int pMinTermFrequency, int pMaxTermFrequency) {
    
    this();
    
    CollectionStatisticsFileName = pCollectionStatisticsFileName;
    ThesaurusFileName = pThesaurusFileName;
    MinTermFrequency = pMinTermFrequency;
    MaxTermFrequency = pMaxTermFrequency;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionStatisticsFileName() {
    return CollectionStatisticsFileName; }
  public String getThesaurusFileName() { 
    return ThesaurusFileName; }
  public int getMinTermFrequency() {
    return MinTermFrequency; }
  public int getMaxTermFrequency() { 
    return MaxTermFrequency; }

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
    ParameterAttributes.put(COLLECTION_STATISTICS_FILE_NAME, 
    CollectionStatisticsFileName);
    ParameterAttributes.put(THESAURUS_FILE_NAME, 
    ThesaurusFileName);
    ParameterAttributes.put(MIN_TERM_FREQUENCY, 
    Tools.int2String(MinTermFrequency));
    ParameterAttributes.put(MAX_TERM_FREQUENCY, 
    Tools.int2String(MaxTermFrequency));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionStatisticsFileName = (String)ParameterAttributes
    .get(COLLECTION_STATISTICS_FILE_NAME);
    ThesaurusFileName = (String)ParameterAttributes
    .get(THESAURUS_FILE_NAME);
    MinTermFrequency = Tools.string2Int(
    (String)ParameterAttributes.get(MIN_TERM_FREQUENCY));
    MaxTermFrequency = Tools.string2Int(
    (String)ParameterAttributes.get(MAX_TERM_FREQUENCY));
    
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