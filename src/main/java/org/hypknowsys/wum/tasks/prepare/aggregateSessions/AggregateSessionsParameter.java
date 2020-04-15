/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.tasks.prepare.aggregateSessions;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.wum.server.*;

/**
 * @version 0.9, 30 June 2004
 * @author Karsten Winkler
 */

public class AggregateSessionsParameter extends WumScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private int FirstPageOccurrence = 0;
  private boolean AddReverseTrail = false;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.aggregateSessions"
  + ".AggregateSessionsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.wum.tasks.prepare.aggregateSessions"
  + ".AggregateSessionsParameterPanel";
  
  private final static String FIRST_PAGE_OCCURRENCE =
  "FirstPageOccurrence";
  private final static String ADD_REVERSE_TRAIL =
  "AddReverseTrail";
  
  public final static int EXCLUDE_REFERRER_URL_IN_OBSERVATIONS = 0;
  public final static int INCLUDE_REFERRER_URL_IN_OBSERVATIONS = 1;
  public final static String[] FIRST_PAGE_OCCURRENCE_OPTIONS = {
    "Exclude Referrer URL in Observations", 
    "Include Referrer URL in Observations"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AggregateSessionsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getFirstPageOccurrence() { 
    return FirstPageOccurrence; }
  public boolean addReverseTrail() { 
    return AddReverseTrail; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFirstPageOccurrence(int pFirstPageOccurrence) {     
    FirstPageOccurrence = validateFirstPageOccurrence(pFirstPageOccurrence); }
  public void setAddReverseTrail(boolean pAddReverseTrail) { 
    AddReverseTrail = pAddReverseTrail; }
  
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
    ParameterAttributes.put(FIRST_PAGE_OCCURRENCE, 
    Tools.int2String(FirstPageOccurrence));
    ParameterAttributes.put(ADD_REVERSE_TRAIL, 
    Tools.boolean2String(AddReverseTrail));
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    FirstPageOccurrence = Tools.string2Int(
    (String)ParameterAttributes.get(FIRST_PAGE_OCCURRENCE));
    AddReverseTrail = Tools.string2Boolean(
    (String)ParameterAttributes.get(ADD_REVERSE_TRAIL));
    
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
  
  private int validateFirstPageOccurrence(int pFirstPageOccurrence) {
    
    if (pFirstPageOccurrence == EXCLUDE_REFERRER_URL_IN_OBSERVATIONS
    || pFirstPageOccurrence == INCLUDE_REFERRER_URL_IN_OBSERVATIONS) {
      return pFirstPageOccurrence;
    }
    else {
      return EXCLUDE_REFERRER_URL_IN_OBSERVATIONS;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}