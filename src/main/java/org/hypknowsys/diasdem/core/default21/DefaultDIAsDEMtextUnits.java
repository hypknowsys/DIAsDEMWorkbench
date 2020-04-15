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

package org.hypknowsys.diasdem.core.default21;

import java.util.ArrayList;
import java.util.List;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnit;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnits;
import org.jdom.Element;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMtextUnits implements DIAsDEMtextUnits {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected ArrayList DiasdemTextUnits = null;
  
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
  
  public DefaultDIAsDEMtextUnits() {
    
    DiasdemTextUnits = new ArrayList();
    
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
    TmpStringBuffer.append("DefaultDIAsDEMtextUnits: NumberOfTextUnits=");
    TmpStringBuffer.append(this.getNumberOfTextUnits());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object clone() {
    
    DefaultDIAsDEMtextUnits myClone = new DefaultDIAsDEMtextUnits();
    if (DiasdemTextUnits != null) {
      for (int i = 0; i < DiasdemTextUnits.size(); i++) {
        myClone.addTextUnit((DefaultDIAsDEMtextUnit)
        ((DefaultDIAsDEMtextUnit)DiasdemTextUnits.get(i)).clone());
      }
    }
    
    return myClone;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface DIAsDEMtextUnits methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfTextUnits() {
    
    if (DiasdemTextUnits != null) {
      return DiasdemTextUnits.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addTextUnit(DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (pDiasdemTextUnit == null
    || !(pDiasdemTextUnit instanceof DefaultDIAsDEMtextUnit)) {
      return -1;
    }
    else {
      int newTextUnitIndex = this.getNumberOfTextUnits();
      if (DiasdemTextUnits == null) {
        DiasdemTextUnits = new ArrayList();
      }
      DiasdemTextUnits.add(pDiasdemTextUnit);
      return newTextUnitIndex;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceTextUnit(int pDiasdemTextUnitIndex,
  DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (pDiasdemTextUnit == null || DiasdemTextUnits == null
    || pDiasdemTextUnitIndex >= this.getNumberOfTextUnits()
    || pDiasdemTextUnitIndex < 0
    || !(pDiasdemTextUnit instanceof DefaultDIAsDEMtextUnit)) {
      System.err.println("[DefaultDIAsDEMtextUnits] DIAsDEMtextUnit cannot "
      + "be replaced at index " + pDiasdemTextUnitIndex + "!");
    }
    else {
      DiasdemTextUnits.set(pDiasdemTextUnitIndex, pDiasdemTextUnit);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnit getTextUnit(int pTextUnitIndex) {
    
    if (DiasdemTextUnits == null || pTextUnitIndex < 0
    || pTextUnitIndex >= this.getNumberOfTextUnits()) {
      return null;
    }
    else {
      return (DIAsDEMtextUnit)DiasdemTextUnits.get(pTextUnitIndex);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public Element getAsJDomElement(String pJDomElementName,
  String pJDomElementNameOfChildren) {
    
    Element rootJdomElement = null;
    if (pJDomElementName == null) {
      rootJdomElement = new Element("DefaultDIAsDEMtextUnits");
    }
    else {
      rootJdomElement = new Element(pJDomElementName);
    }
    
    /*
    <!ELEMENT DefaultDIAsDEMtextUnits (DefaultDIAsDEMtextUnit+)>
     */
    
    if (DiasdemTextUnits != null && DiasdemTextUnits.size() > 0) {
      for (int i = 0; i < DiasdemTextUnits.size(); i++) {
        rootJdomElement.addContent(
        ((DefaultDIAsDEMtextUnit)DiasdemTextUnits.get(i))
        .getAsJDomElement(pJDomElementNameOfChildren));
      }
    }
    else {
      // DefaultDIAsDEMtextUnits cannot remain empty
      rootJdomElement.addContent((new DefaultDIAsDEMtextUnit())
      .getAsJDomElement(pJDomElementNameOfChildren));
    }
    
    return rootJdomElement;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFromJDomElement(Element pJDomElement) {
    
    this.reset();
    
    // assuming that pJDomElement has been validated against DTD
    DefaultDIAsDEMtextUnit textUnit = null;
    Element textUnitElement = null;
    List children = pJDomElement.getContent();
    for (int i = 0; i < children.size(); i++) {
      if (children.get(i) instanceof Element) {
        textUnitElement = (Element)children.get(i);
        textUnit = new DefaultDIAsDEMtextUnit();
        textUnit.setFromJDomElement(textUnitElement);
        DiasdemTextUnits.add(textUnit);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {
    
    DiasdemTextUnits = new ArrayList();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static DefaultDIAsDEMtextUnits getExample() {
    
    DefaultDIAsDEMtextUnits diasdemTextUnits = new DefaultDIAsDEMtextUnits();
    
    diasdemTextUnits.addTextUnit(DefaultDIAsDEMtextUnit.getExample());
    diasdemTextUnits.addTextUnit(DefaultDIAsDEMtextUnit.getExample());
    diasdemTextUnits.addTextUnit(DefaultDIAsDEMtextUnit.getExample());
    
    return diasdemTextUnits;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String[] pOptions) {
    
    DefaultDIAsDEMtextUnits diasdemTextUnits1 = DefaultDIAsDEMtextUnits
    .getExample();
    System.out.println(diasdemTextUnits1.toString());
    
    DefaultDIAsDEMtextUnits diasdemTextUnits2 = new DefaultDIAsDEMtextUnits();
    diasdemTextUnits2.setFromJDomElement(
    diasdemTextUnits1.getAsJDomElement("TextUnits", "TextUnit"));
    System.out.println(diasdemTextUnits2.toString());
    
    System.out.println("---");
    DefaultDIAsDEMtextUnit textUnit = null;
    for (int i = 0; i < diasdemTextUnits2.getNumberOfTextUnits(); i++) {
      textUnit = (DefaultDIAsDEMtextUnit)diasdemTextUnits2.getTextUnit(i);
      System.out.println(textUnit);
      textUnit.setContentsFromString(i + "; Contents have been modified ...");
      diasdemTextUnits2.replaceTextUnit(i, textUnit);
      System.out.println(diasdemTextUnits2.getTextUnit(i));
    }
    
    System.out.println("---");
    DefaultDIAsDEMtextUnits diasdemTextUnits3 = new DefaultDIAsDEMtextUnits();
    diasdemTextUnits3.setFromJDomElement(
    diasdemTextUnits2.getAsJDomElement("TextUnits", "TextUnit"));
    for (int i = 0; i < diasdemTextUnits3.getNumberOfTextUnits(); i++) {
      System.out.println(diasdemTextUnits3.getTextUnit(i));
    }
    
    System.out.println("---");
    System.out.println(diasdemTextUnits3.clone());
    
  }
  
}