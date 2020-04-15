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

import java.util.List;
import java.util.StringTokenizer;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnit;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.hypknowsys.misc.util.Tools;
import org.jdom.Element;
import org.jdom.Text;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMtextUnit implements DIAsDEMtextUnit {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected StringBuffer Contents = null;
  protected int TextUnitID = -1;
  protected int BeginIndex = -1;
  protected int EndIndex = -1;
  protected int Iteration = -1;
  protected int ClusterID = -1;
  protected String ClusterLabel = null;
  
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
  
  public DefaultDIAsDEMtextUnit() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMtextUnit(String pContents, int pTextUnitID) {
    
    this();
    
    Contents = new StringBuffer(2 * pContents.length());
    Contents.append(pContents);
    TextUnitID = pTextUnitID;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMtextUnit(String pContents, int pTextUnitID,
  int pBeginIndex, int pEndIndex) {
    
    this(pContents, pTextUnitID);
    
    BeginIndex = pBeginIndex;
    EndIndex = pEndIndex;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public StringBuffer getContents() {
    return Contents; }
  public int getTextUnitID() {
    return TextUnitID; }
  public int getBeginIndex() {
    return BeginIndex; }
  public int getEndIndex() {
    return EndIndex; }
  public int getIteration() {
    return Iteration; }
  public int getClusterID() {
    return ClusterID; }
  public String getClusterLabel() {
    return ClusterLabel; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContents(StringBuffer pContents) {
    Contents = pContents; }
  public void setTextUnitID(int pTextUnitID) {
    TextUnitID = pTextUnitID; }
  public void setBeginIndex(int pBeginIndex) {
    BeginIndex = pBeginIndex; }
  public void setEndIndex(int pEndIndex) {
    EndIndex = pEndIndex; }
  public void setIteration(int pIteration) {
    Iteration = pIteration; }
  public void setClusterID(int pClusterID) {
    ClusterID = pClusterID; }
  public void setClusterLabel(String pClusterLabel) {
    ClusterLabel = pClusterLabel; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("DefaultDIAsDEMtextUnit: TextUnitID=");
    TmpStringBuffer.append(TextUnitID);
    TmpStringBuffer.append("; BeginIndex=");
    TmpStringBuffer.append(BeginIndex);
    TmpStringBuffer.append("; EndIndex=");
    TmpStringBuffer.append(EndIndex);
    TmpStringBuffer.append("; Iteration=");
    TmpStringBuffer.append(Iteration);
    TmpStringBuffer.append("; ClusterID=");
    TmpStringBuffer.append(ClusterID);
    TmpStringBuffer.append("; ClusterLabel=");
    TmpStringBuffer.append(ClusterLabel);
    TmpStringBuffer.append("; Contents=\"");
    TmpStringBuffer.append(Contents);
    TmpStringBuffer.append("\"");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object clone() {
    
    DefaultDIAsDEMtextUnit myClone = new DefaultDIAsDEMtextUnit(
    Contents.toString(), TextUnitID, BeginIndex, EndIndex);
    myClone.setIteration(Iteration);
    myClone.setClusterID(ClusterID);
    myClone.setClusterLabel(ClusterLabel);
    
    return myClone;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface DIAsDEMtextUnit methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getContentsAsString() {
    
    return Contents.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setContentsFromString(String pContents) {
    
    Contents = new StringBuffer(2 * pContents.length());
    Contents.append(pContents);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public Element getAsJDomElement(String pJDomElementName) {
    
    Element rootJdomElement = null;
    if (pJDomElementName == null) {
      rootJdomElement = new Element("DefaultDIAsDEMtextUnit");
    }
    else {
      rootJdomElement = new Element(pJDomElementName);
    }
    
    /*
    <!ATTLIST DefaultDIAsDEMtextUnit
      TextUnitID CDATA #IMPLIED
      BeginIndex CDATA #IMPLIED
      EndIndex CDATA #IMPLIED
      Iteration CDATA #IMPLIED
      ClusterID CDATA #IMPLIED
      ClusterLabel CDATA #IMPLIED
    >
     */
    
    if (TextUnitID >= 0) {
      rootJdomElement.setAttribute("TextUnitID",
      Tools.int2String(TextUnitID));
    }
    if (BeginIndex >= 0) {
      rootJdomElement.setAttribute("BeginIndex",
      Tools.int2String(BeginIndex));
    }
    if (EndIndex >= 0) {
      rootJdomElement.setAttribute("EndIndex",
      Tools.int2String(EndIndex));
    }
    if (Iteration >= 0) {
      rootJdomElement.setAttribute("Iteration",
      Tools.int2String(Iteration));
    }
    if (ClusterID >= 0) {
      rootJdomElement.setAttribute("ClusterID",
      Tools.int2String(ClusterID));
    }
    if (ClusterLabel != null && ClusterLabel.length() > 0) {
      rootJdomElement.setAttribute("ClusterLabel", ClusterLabel);
    }
    
    /*
    <!ELEMENT DefaultDIAsDEMtextUnit(#PCDATA | NeRef)*>
    <!ELEMENT NeRef EMPTY>
    <!ATTLIST NeRef
      NeID CDATA #IMPLIED
    >
     */
    
    StringBuffer nonNeRefTokens = null;
    StringTokenizer searchNeRefsTokenizer = null;
    String token = null;
    
    if (Contents != null && Contents.length() > 0) {
      nonNeRefTokens = new StringBuffer(2 * Contents.length());
      searchNeRefsTokenizer = new StringTokenizer(Contents.toString());
      while (searchNeRefsTokenizer.hasMoreElements()) {
        token = searchNeRefsTokenizer.nextToken();
        if (!NamedEntity.isPlaceholder(token)) {
          nonNeRefTokens.append(token);
          nonNeRefTokens.append(" ");
        }
        else {
          if (nonNeRefTokens.length() > 0) {
            rootJdomElement.addContent(nonNeRefTokens.toString().trim());
            nonNeRefTokens = new StringBuffer(2 * Contents.length());
          }
          rootJdomElement.addContent(new Element("NeRef")
          .setAttribute("NeID",NamedEntity.getPlaceholderID(token)));
        }
      }
      if (nonNeRefTokens.length() > 0) {
        rootJdomElement.addContent(nonNeRefTokens.toString().trim());
      }
    }
    else {
      // DefaultDIAsDEMtextUnit cannot remain empty
      rootJdomElement.addContent("");
    }
    
    return rootJdomElement;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFromJDomElement(Element pJDomElement) {
    
    this.reset();
    
    // assuming that pJDomElement has been validated against DTD
    if (pJDomElement.getAttributeValue("TextUnitID") != null) {
      TextUnitID = Tools.string2Int(pJDomElement
      .getAttributeValue("TextUnitID"));
    }
    if (pJDomElement.getAttributeValue("BeginIndex") != null) {
      BeginIndex = Tools.string2Int(pJDomElement
      .getAttributeValue("BeginIndex"));
    }
    if (pJDomElement.getAttributeValue("EndIndex") != null) {
      EndIndex = Tools.string2Int(pJDomElement
      .getAttributeValue("EndIndex"));
    }
    if (pJDomElement.getAttributeValue("Iteration") != null) {
      Iteration = Tools.string2Int(pJDomElement
      .getAttributeValue("Iteration"));
    }
    if (pJDomElement.getAttributeValue("ClusterID") != null) {
      ClusterID = Tools.string2Int(pJDomElement
      .getAttributeValue("ClusterID"));
    }
    if (pJDomElement.getAttributeValue("ClusterLabel") != null) {
      ClusterLabel = pJDomElement.getAttributeValue("ClusterLabel");
    }
    
    Element neRefJdomElement = null;
    List children = pJDomElement.getContent();
    Contents = new StringBuffer(2 * pJDomElement.getTextTrim().length());
    for (int i = 0; i < children.size(); i++) {
      if (children.get(i) instanceof Text) {
        if (i > 0) {
          Contents.append(" ");
        }
        Contents.append(((Text)children.get(i)).getTextTrim());
      }
      else if (children.get(i) instanceof Element) {
        neRefJdomElement = (Element)children.get(i);
        if (neRefJdomElement.getName().equals("NeRef")) {
          if (i > 0) {
            Contents.append(" ");
          }
          Contents.append(NamedEntity.PLACEHOLDER_PREFIX);
          Contents.append(neRefJdomElement.getAttributeValue("NeID"));
          Contents.append(NamedEntity.PLACEHOLDER_SUFFIX);
        }
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
    
    Contents = null;
    TextUnitID = -1;
    BeginIndex = -1;
    EndIndex = -1;
    Iteration = -1;
    ClusterID = -1;
    ClusterLabel = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static DefaultDIAsDEMtextUnit getExample() {
    
    DefaultDIAsDEMtextUnit diasdemTextUnit = new DefaultDIAsDEMtextUnit();
    
    diasdemTextUnit.setTextUnitID(1000);
    diasdemTextUnit.setContentsFromString("This is a default DIAsDEM text "
    + "unit. Current time: " + Tools.getSystemDate() + "; NE: "
    + NamedEntity.createPlaceholder(789) + " ...");
    diasdemTextUnit.setBeginIndex(0);
    diasdemTextUnit.setEndIndex(1000);
    diasdemTextUnit.setIteration(1);
    diasdemTextUnit.setClusterID(123);
    diasdemTextUnit.setClusterLabel("-");
    
    return diasdemTextUnit;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String[] pOptions) {
    
    DefaultDIAsDEMtextUnit diasdemTextUnit1 = DefaultDIAsDEMtextUnit
    .getExample();
    System.out.println(diasdemTextUnit1.toString());
    
    DefaultDIAsDEMtextUnit diasdemTextUnit2 = new DefaultDIAsDEMtextUnit();
    diasdemTextUnit2.setFromJDomElement(
    diasdemTextUnit1.getAsJDomElement("TextUnit"));
    System.out.println(diasdemTextUnit2.toString());
    
  }
  
}