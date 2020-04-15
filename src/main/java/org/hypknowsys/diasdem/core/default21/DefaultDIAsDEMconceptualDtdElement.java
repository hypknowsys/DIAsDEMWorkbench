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

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.TreeSet;
import org.hypknowsys.diasdem.core.DIAsDEMconceptualDtdAttribute;
import org.hypknowsys.diasdem.core.DIAsDEMconceptualDtdElement;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.hypknowsys.misc.io.Itemizer;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMconceptualDtdElement implements
DIAsDEMconceptualDtdElement, Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String Name = null;
  private long AbsoluteSupport = 0;
  private double RelativeSupport = 0.0;
  private String HtmlDescription = null;
  private String HtmlExamples = null;
  private int MaxNumberOfHtmlExamples = 5;
  private int NumberOfHtmlExamples = 0;
  
  private TreeMap Attributes = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpName = null;
  private transient DefaultDIAsDEMconceptualDtdAttribute 
  TmpAttribute = null;
  private transient Iterator TmpIterator = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMconceptualDtdElement() {
    
    Name = null;
    AbsoluteSupport = 0L;
    RelativeSupport = 0.0;
    Attributes = new TreeMap();
    HtmlDescription = null;
    HtmlExamples = null;
   
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMconceptualDtdElement(String pName,
  long pAbsoluteSupport, double pRelativeSupport) {
    
    Name = pName;
    AbsoluteSupport = pAbsoluteSupport;
    RelativeSupport = pRelativeSupport;
    Attributes = new TreeMap();
    HtmlDescription = "";
    HtmlExamples = "";
    MaxNumberOfHtmlExamples = 5;
    NumberOfHtmlExamples = 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getName() {
    return Name; }
  public long getAbsoluteSupport() {
    return AbsoluteSupport; }
  public double getRelativeSupport() {
    return RelativeSupport; }
  public long getAbsSupport() {
    return AbsoluteSupport; }
  public double getRelSupport() {
    return RelativeSupport; }
  public String getHtmlDescription() {
    return HtmlDescription; }
  public String getHtmlExamples() {
    return HtmlExamples; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setName(String pName) {
    if (pName != null) {
      Name = pName;
    }
  }
  public void setAbsoluteSupport(long pAbsolueSupport) {
    AbsoluteSupport = pAbsolueSupport; }
  public void incrementAbsoluteSupport() {
    AbsoluteSupport++; }
  public void setRelativeSupport(double pRelativeSupport) {
    RelativeSupport = pRelativeSupport; }
  public void setHtmlDescription(String pHtmlDescription) {
    if (pHtmlDescription != null) {
      HtmlDescription = pHtmlDescription;
    }
  }
  public void setHtmlExamples(String pHtmlExamples) {
    if (pHtmlExamples != null) {
      HtmlExamples = pHtmlExamples;
    }
  }
  public void setMaxNumberOfHtmlExamples(int pMaxNumberOfHtmlExamples) {
    MaxNumberOfHtmlExamples = pMaxNumberOfHtmlExamples;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("ConceptualDtdElement: Name=");
    TmpStringBuffer.append(Name);
    TmpStringBuffer.append("; AbsSupport=");
    TmpStringBuffer.append(AbsoluteSupport);
    TmpStringBuffer.append("; RelSupport=");
    TmpStringBuffer.append(RelativeSupport);
    
    return TmpStringBuffer.toString();
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addOrUpdateAttribute(NamedEntity pNamedEntity) {
    
    TmpName = pNamedEntity.getMostProbableTypeString();
    
    TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)Attributes
    .get(TmpName);
    if (TmpAttribute != null) {
      TmpAttribute.incrementAbsoluteSupport();
      Attributes.put(TmpName, TmpAttribute);
    }
    else {
      TmpAttribute = new DefaultDIAsDEMconceptualDtdAttribute(TmpName,
      pNamedEntity.getMostProbableTypeString(), 1L, 0.0);
      Attributes.put(TmpName, TmpAttribute);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addOrUpdateAttribute(
  DefaultDIAsDEMconceptualDtdAttribute pAttribute) {
    
    Attributes.put(pAttribute.getName(), pAttribute);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public double getRelativeSupportOfAttribute(NamedEntity pNamedEntity) {
    
    TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)Attributes
    .get(TmpName);
    if (TmpAttribute != null) {
      return TmpAttribute.getRelativeSupport();
    }
    else {
      return 0.0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMconceptualDtdAttribute getAttribute(String pName) {
    
    return (DefaultDIAsDEMconceptualDtdAttribute)Attributes.get(pName);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void computeRelativeSupportOfAttributes() {
    
    TmpIterator = Attributes.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)TmpIterator.next();
      TmpAttribute.setRelativeSupport(TmpAttribute.getAbsoluteSupport()
      / (double)AbsoluteSupport);
      Attributes.put(TmpAttribute.getName(), TmpAttribute);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getXmlRepresentation() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("<!ELEMENT ");
    TmpStringBuffer.append(Name);
    TmpStringBuffer.append(" (#PCDATA)> ");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeAttributesToXmlRepresentation(
  double pMinimumRelativeSupport, TextFile pTextFile) {
    
    TmpIterator = Attributes.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)TmpIterator.next();
      if (TmpAttribute.getRelativeSupport() >= pMinimumRelativeSupport) {
        pTextFile.setNextLine("<!ATTLIST " + Name + " "
        + TmpAttribute.getName() + " CDATA #IMPLIED> ");
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TreeSet getAttributeNames(double pMinimumRelativeSupport) {
    
    TreeSet result = new TreeSet();
    TmpIterator = Attributes.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)TmpIterator.next();
      if (TmpAttribute.getRelativeSupport() >= pMinimumRelativeSupport) {
        result.add(TmpAttribute.getName());
      }
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getAttributesXmlRepresentation(
  double pMinimumRelativeSupport) {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpIterator = Attributes.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)TmpIterator.next();
      if (TmpAttribute.getRelativeSupport() >= pMinimumRelativeSupport) {
        TmpStringBuffer.append("<!ATTLIST ");
        TmpStringBuffer.append(Name);
        TmpStringBuffer.append(" ");
        TmpStringBuffer.append(TmpAttribute.getName());
        TmpStringBuffer.append(" CDATA #IMPLIED> ");
      }
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeAttributesToItemFile(double pMinimumRelativeSupport,
  TextFile pTextFile) {
    
    TmpIterator = Attributes.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)TmpIterator.next();
      if (TmpAttribute.getRelativeSupport() >= pMinimumRelativeSupport) {
        pTextFile.setNextLine(Itemizer.stringToItem(Name)
        + Itemizer.ITEM_SEPARATOR + TmpAttribute.toItemLine());
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeAttributesToDtdDocumentationFile(
  double pMinimumRelativeSupport, TextFile pTextFile) {

    // count attribute
    TmpIterator = Attributes.values().iterator();
    int numberOfAttributes = 0;
    while (TmpIterator.hasNext()) {
      TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)TmpIterator.next();
      if (TmpAttribute.getRelativeSupport() >= pMinimumRelativeSupport) {
        numberOfAttributes++;
      }
    }
    if (numberOfAttributes > 0) {
      pTextFile.setNextLine(
      "<table border=\"1\"><tr>"
      + "<th align=\"left\" valign=\"top\">Name</th>"
      + "<th align=\"left\" valign=\"top\">Description</th></tr>");
      TmpIterator = Attributes.values().iterator();
      while (TmpIterator.hasNext()) {
        TmpAttribute = (DefaultDIAsDEMconceptualDtdAttribute)TmpIterator.next();
        if (TmpAttribute.getRelativeSupport() >= pMinimumRelativeSupport) {
          pTextFile.setNextLine("<tr>"
          + "<td align=\"left\" valign=\"top\">" + TmpAttribute.getName()
          + "</td>");
          pTextFile.setNextLine("<td align=\"left\" valign=\"top\">");
          pTextFile.setNextLine((Tools.stringIsNullOrEmpty(
          TmpAttribute.getHtmlDescription()) ? "&nbsp;" : Tools
          .insertHtmlEntityReferences(TmpAttribute.getHtmlDescription())));
          pTextFile.setNextLine("</td></tr>");
        }
      }
      pTextFile.setNextLine("</table>");
    }
    else {
      pTextFile.setNextLine("This DTD element has no attributes.");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toItemLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(Itemizer.stringToItem(Name));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.longToItem(AbsoluteSupport));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.doubleToItem(RelativeSupport));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem(
    Tools.removeQuotesAndNewLines(HtmlDescription)));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem(
    Tools.removeQuotesAndNewLines(HtmlExamples)));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.intToItem(MaxNumberOfHtmlExamples));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.intToItem(NumberOfHtmlExamples));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String getCsvHeaderLine() {
    
    return "ConceptualDtdElement,AbsoluteSupport,RelativeSupport"
    + ",HtmlDescription,HtmlExamples,MaxNumberOfHtmlExamples"
    + ",NumberOfHtmlExamples";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCsvValueLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(Itemizer.stringToItem(Name));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.longToItem(AbsoluteSupport));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.doubleToItem(RelativeSupport));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.stringToItem(
    Tools.removeQuotesAndNewLines(HtmlDescription)));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.stringToItem(
    Tools.removeQuotesAndNewLines(HtmlExamples)));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.longToItem(MaxNumberOfHtmlExamples));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.longToItem(NumberOfHtmlExamples));
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) throws NumberFormatException {
    
    Name = null;
    Itemizer itemizer = new Itemizer(pItemLine);
    
    try {
      Name = Itemizer.itemToString(itemizer.getNextItem());
      AbsoluteSupport = Itemizer.itemToLong(itemizer.getNextItem());
      RelativeSupport = Itemizer.itemToDouble(itemizer.getNextItem());
      HtmlDescription = itemizer.itemToString(itemizer.getNextItem());
      HtmlExamples = itemizer.itemToString(itemizer.getNextItem());
      MaxNumberOfHtmlExamples = Itemizer.itemToInt(itemizer.getNextItem());
      NumberOfHtmlExamples = Itemizer.itemToInt(itemizer.getNextItem());
    }
    catch (NoSuchElementException e1) {
      this.reset(); throw new NumberFormatException();
    }
    catch (NumberFormatException e2) {
      this.reset(); throw new NumberFormatException();
    }
    catch (StringIndexOutOfBoundsException e2) {
      this.reset(); throw new NumberFormatException();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
public void addHtmlExample(String pHtmlExample) {
    
    if (NumberOfHtmlExamples < MaxNumberOfHtmlExamples) {
      HtmlExamples += "<pre>" + Tools.insertHtmlEntityReferences(Tools
      .insertLineBreaks(100, pHtmlExample)) + "</pre>";
      NumberOfHtmlExamples++;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {
    
    Name = null;
    AbsoluteSupport = 0L;
    RelativeSupport = 0.0;
    Attributes = new TreeMap();
    HtmlDescription = "";
    HtmlExamples = "";
    MaxNumberOfHtmlExamples = 5;
    NumberOfHtmlExamples = 0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}