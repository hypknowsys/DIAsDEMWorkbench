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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public abstract class WumScriptableTaskParameter extends WumTaskParameter implements ScriptableTaskParameter {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected transient TreeMap ParameterAttributes = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public WumScriptableTaskParameter() {
  
  super();
  
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
  
  public abstract org.jdom.Element getParameterAttributesAsJDomElement();

  /* ########## ########## ########## ########## ########## ######### */
  
  public abstract void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected org.jdom.Element getParameterAttributesAsJDomElement(
  TreeMap pParameterAttributes) {
    
    org.jdom.Element parameterAttributesElement = new org.jdom.Element(
    "ParameterAttributes");
    if (pParameterAttributes == null || pParameterAttributes.size() ==0)
      return parameterAttributesElement;
    
    org.jdom.Element parameterAttributeElement = null;
    org.jdom.Element parameterAttributeNameElement = null;
    org.jdom.Element parameterAttributeValueElement = null;
    String attributeName = null;
    String attributeValue = null;
    Iterator iterator = pParameterAttributes.keySet().iterator();
    while (iterator.hasNext()) {
      attributeName = (String)iterator.next();
      attributeValue = (String)pParameterAttributes.get(attributeName);
      parameterAttributeElement = new org.jdom.Element("ParameterAttribute");
      parameterAttributeNameElement = new org.jdom.Element("AttributeName");
      parameterAttributeNameElement.addContent(attributeName);
      parameterAttributeValueElement = new org.jdom.Element("AttributeValue");
      parameterAttributeValueElement.addContent(attributeValue);
      parameterAttributeElement.addContent(parameterAttributeNameElement);
      parameterAttributeElement.addContent(parameterAttributeValueElement);
      parameterAttributesElement.addContent(parameterAttributeElement);
    }
    
    return parameterAttributesElement;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  protected TreeMap getParameterAttributesAsTreeMap(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = new TreeMap();
    
    org.jdom.Element jDomElement = pParameterAttributes.getChild(
    "ParameterAttributes");
    if (jDomElement == null) {
      return ParameterAttributes;
    }
  
    List jDomElementList = jDomElement.getChildren("ParameterAttribute");
    if (jDomElementList != null) {
      Iterator iterator = jDomElementList.iterator();
      while ( iterator.hasNext() ) {
        jDomElement = (org.jdom.Element)iterator.next();
        ParameterAttributes.put(jDomElement.getChildTextTrim("AttributeName"),
        jDomElement.getChildTextTrim("AttributeValue"));
      }
    }
    
    return ParameterAttributes;
    
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