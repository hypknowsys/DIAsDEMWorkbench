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

package org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ConvertParameterTextFileParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String SourceParameterFileName = null;
  protected String TargetParameterFileName = null;
  protected int ConversionType = CONVERT_PARAMETER_FILE_LINES_TO_LOWER_CASE;
  protected String RegularExpression = null;
  protected String ReplacementString = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile"
  + ".ConvertParameterTextFileTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.miscellaneous.convertParameterTextFile"
  + ".ConvertParameterTextFileParameterPanel";
  
  private final static String SOURCE_PARAMETER_FILE_NAME =
  "SourceParameterFileName";
  private final static String TARGET_PARAMETER_FILE_NAME =
  "TargetParameterFileName";
  private final static String CONVERSION_TYPE =
  "ConversionType";
  private final static String REGULAR_EXPRESSION =
  "RegularExpression";
  private final static String REPLACEMENT_STRING =
  "ReplacementString";
  
  public final static int CONVERT_PARAMETER_FILE_LINES_TO_LOWER_CASE = 0;
  public final static int CONVERT_PARAMETER_FILE_LINES_TO_UPPER_CASE = 1;
  public final static int APPLY_REGULAR_EXPRESSION_TO_PARAMETER_FILE_LINES = 2;
  public final static String[] CONVERSION_TYPES = {
    "Convert Lines of Parameter File to Lower Case",
    "Convert TLines of Parameter File to Upper Case",
    "Apply Regular Expression to Lines of Parameter File"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertParameterTextFileParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    SourceParameterFileName = null;
    TargetParameterFileName = null;
    ConversionType = CONVERT_PARAMETER_FILE_LINES_TO_LOWER_CASE;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertParameterTextFileParameter(String pSourceParameterFileName, 
  String pTargetParameterFileName, String pConversionType, 
  String pRegularExpression, String pReplacementString) {
    
    this();
    
    SourceParameterFileName = pSourceParameterFileName;
    TargetParameterFileName = pTargetParameterFileName;
    ConversionType = CONVERT_PARAMETER_FILE_LINES_TO_LOWER_CASE;
    RegularExpression = pRegularExpression;
    ReplacementString = pReplacementString;
    
    for (int i = 0; i < CONVERSION_TYPES.length; i++) {
      if (CONVERSION_TYPES[i].equals(pConversionType)) {
        ConversionType = i;
        break;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ConvertParameterTextFileParameter(String pSourceParameterFileName, 
  String pTargetParameterFileName, int pConversionType, 
  String pRegularExpression, String pReplacementString) {
    
    this();
    
    SourceParameterFileName = pSourceParameterFileName;
    TargetParameterFileName = pTargetParameterFileName;
    RegularExpression = pRegularExpression;
    ReplacementString = pReplacementString;
    if (pConversionType >= 0 && pConversionType < CONVERSION_TYPES.length) {
      ConversionType = pConversionType;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getSourceParameterFileName() {
    return SourceParameterFileName; }
  public String getTargetParameterFileName() {
    return TargetParameterFileName; }
  public int getConversionType() {
    return ConversionType; }
  public String getRegularExpression() {
    return RegularExpression; }
  public String getReplacementString() {
    return ReplacementString; }
  
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
    ParameterAttributes.put(SOURCE_PARAMETER_FILE_NAME, SourceParameterFileName);
    ParameterAttributes.put(TARGET_PARAMETER_FILE_NAME, TargetParameterFileName);
     ParameterAttributes.put(CONVERSION_TYPE, Tools.int2String(
    ConversionType));
    ParameterAttributes.put(REGULAR_EXPRESSION, RegularExpression);
    ParameterAttributes.put(REPLACEMENT_STRING, ReplacementString);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    SourceParameterFileName = (String)ParameterAttributes
    .get(SOURCE_PARAMETER_FILE_NAME);
    TargetParameterFileName = (String)ParameterAttributes
    .get(TARGET_PARAMETER_FILE_NAME);
    ConversionType = Tools.string2Int((String)ParameterAttributes
    .get(CONVERSION_TYPE));
    RegularExpression = (String)ParameterAttributes.get(REGULAR_EXPRESSION);
    ReplacementString = (String)ParameterAttributes.get(REPLACEMENT_STRING);
    
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