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

package org.hypknowsys.wum.server;

import java.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.util.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class WumScriptableTaskResult extends WumTaskResult implements ScriptableTaskResult {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected transient TreeMap ResultAttributes = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  protected final static String STATUS = "Status";
  protected final static String DESCRIPTION = "Description";
  protected final static String LOG_MESSAGE = "LogMessage";

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public WumScriptableTaskResult() { 

    super();
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public WumScriptableTaskResult(int pStatus, String pDescription, String pLogMessage) { 

    super(pStatus, pDescription, pLogMessage);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

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
  /* ########## interface ScriptableTaskResult methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getClassName() {    
    return this.getClass().getName(); }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public org.jdom.Element getResultAttributesAsJDomElement() {

    this.setDefaultResultAttributesTreeMap();
    
    return this.getResultAttributesAsJDomElement(ResultAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setResultAttributesFromJDomElement(
  org.jdom.Element pResultAttributes) {
    
    ResultAttributes = getResultAttributesAsTreeMap(pResultAttributes);
    this.setDefaultResultAttributesFromTreeMap();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected org.jdom.Element getResultAttributesAsJDomElement(
  TreeMap pResultAttributes) {
    
    org.jdom.Element resultAttributesElement = new org.jdom.Element(
    "ResultAttributes");
    if (pResultAttributes == null || pResultAttributes.size() ==0)
      return resultAttributesElement;
    
    org.jdom.Element resultAttributeElement = null;
    org.jdom.Element resultAttributeNameElement = null;
    org.jdom.Element resultAttributeValueElement = null;
    String attributeName = null;
    String attributeValue = null;
    Iterator iterator = pResultAttributes.keySet().iterator();
    while (iterator.hasNext()) {
      attributeName = (String)iterator.next();
      attributeValue = (String)pResultAttributes.get(attributeName);
      resultAttributeElement = new org.jdom.Element("ResultAttribute");
      resultAttributeNameElement = new org.jdom.Element("AttributeName");
      resultAttributeNameElement.addContent(attributeName);
      resultAttributeValueElement = new org.jdom.Element("AttributeValue");
      resultAttributeValueElement.addContent(attributeValue);
      resultAttributeElement.addContent(resultAttributeNameElement);
      resultAttributeElement.addContent(resultAttributeValueElement);
      resultAttributesElement.addContent(resultAttributeElement);
    }
    
    return resultAttributesElement;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setDefaultResultAttributesTreeMap() {

    ResultAttributes = new TreeMap();    
    ResultAttributes.put(STATUS, Tools.int2String(Status));
    ResultAttributes.put(DESCRIPTION, Description);
    ResultAttributes.put(LOG_MESSAGE, LogMessage);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected TreeMap getResultAttributesAsTreeMap(
  org.jdom.Element pResultAttributes) {
    
    ResultAttributes = new TreeMap();
    
    org.jdom.Element jDomElement = null;
    List jDomElementList = pResultAttributes.getChild("ResultAttributes")
    .getChildren("ResultAttribute");
    if (jDomElementList != null) {
      Iterator iterator = jDomElementList.iterator();
      while ( iterator.hasNext() ) {
        jDomElement = (org.jdom.Element)iterator.next();
        ResultAttributes.put(jDomElement.getChildTextTrim("AttributeName"),
        jDomElement.getChildTextTrim("AttributeValue"));
      }
    }
    
    return ResultAttributes;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected void setDefaultResultAttributesFromTreeMap() {

    Status = Tools.string2Int( (String)ResultAttributes.get(STATUS) );
    Description = (String)ResultAttributes.get(DESCRIPTION);
    LogMessage = (String)ResultAttributes.get(LOG_MESSAGE);
    
  }

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