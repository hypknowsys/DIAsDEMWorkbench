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

package org.hypknowsys.diasdem.tasks.miscellaneous.extendCompositeNeRules20;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class ExtendCompositeNeRulesParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String BasicNeFileName = null;
  protected String InitialCompositeNeFileName = null;
  protected String ExtendedCompositeNeFileName = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.extendCompositeNeRules20"
  + ".ExtendCompositeNeRulesTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.extendCompositeNeRules20"
  + ".ExtendCompositeNeRulesParameterPanel";
  
  private final static String BASIC_NE_FILE_NAME =
  "BasicNeFileName";
  private final static String INITIAL_COMPOSITE_NE_FILE_NAME =
  "InitialCompositeNeFileName";
  private final static String EXTENDED_COMPOSITE_NE_FILE_NAME =
  "ExtendedCompositeNeFileName";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ExtendCompositeNeRulesParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;

    BasicNeFileName = null;
    InitialCompositeNeFileName = null;
    ExtendedCompositeNeFileName = null;

    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ExtendCompositeNeRulesParameter(String pBasicNeFileName, 
  String pInitialCompositeNeFileName, String pExtendedCompositeNeFileName) {
    
    this();
    
    BasicNeFileName = pBasicNeFileName;
    InitialCompositeNeFileName = pInitialCompositeNeFileName;
    ExtendedCompositeNeFileName = pExtendedCompositeNeFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getBasicNeFileName() { 
    return BasicNeFileName; }
  public String getInitialCompositeNeFileName() {
    return InitialCompositeNeFileName; }
  public String getExtendedCompositeNeFileName() { 
    return ExtendedCompositeNeFileName; }
  
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
    ParameterAttributes.put(BASIC_NE_FILE_NAME, BasicNeFileName);
    ParameterAttributes.put(INITIAL_COMPOSITE_NE_FILE_NAME, 
    InitialCompositeNeFileName);
    ParameterAttributes.put(EXTENDED_COMPOSITE_NE_FILE_NAME, 
    ExtendedCompositeNeFileName);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    BasicNeFileName = (String)ParameterAttributes
    .get(BASIC_NE_FILE_NAME);
    InitialCompositeNeFileName = (String)ParameterAttributes
    .get(INITIAL_COMPOSITE_NE_FILE_NAME);
    ExtendedCompositeNeFileName = (String)ParameterAttributes
    .get(EXTENDED_COMPOSITE_NE_FILE_NAME);

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