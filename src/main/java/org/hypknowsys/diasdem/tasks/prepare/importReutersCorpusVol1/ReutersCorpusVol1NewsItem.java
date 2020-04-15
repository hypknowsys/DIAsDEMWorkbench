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

package org.hypknowsys.diasdem.tasks.prepare.importReutersCorpusVol1;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.jdom.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;

/**
 * Note: This class does not implement the entire NewsML document type 
 * definiiton. It rather focuses on its most common elements that occur
 * in the Reuters Corpus Vol. 1: ItemID, Date, Title, Headline (without
 * further paragraph elements), Byline (without further paragraph elements),
 * Byline (without further paragraph elements), Dateline (without further 
 * paragraph elements), Text (support of paragraph elements), Copyright 
 * (without further paragraph elements) as well as region and topic codes.
 * 
 * @version 2.1.0.5, 1 January 2004
 * @author Karsten Winkler
 */
  
public class ReutersCorpusVol1NewsItem {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String ItemID = null;
  protected String Date = null;
  protected String Title = null;
  protected String Headline = null;
  protected String Byline = null;
  protected String Dateline = null;
  protected String Text = null;
  protected String Copyright = null;
  protected String TopicCodes = null;
  protected String RegionCodes = null;

  private String XmlFileName = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient File TmpFile = null;
  private transient org.jdom.Element TmpTextElement = null;
  private transient org.jdom.Element TmpTextChildElement = null;
  private transient org.jdom.Element TmpMetadataElement = null;
  private transient org.jdom.Element TmpCodesElement = null;
  private transient org.jdom.Element TmpCodeElement = null;
  private transient List TmpList = null;
  private transient List TmpCodesList = null;
  private transient StringBuffer TmpTopicsBuffer = null;
  private transient StringBuffer TmpRegionsBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public ReutersCorpusVol1NewsItem() {
    
    this.reset();

  }

  /* ########## ########## ########## ########## ########## ######### */

  public ReutersCorpusVol1NewsItem(String pXmlFileName) 
  throws DiasdemException {

    this();
    try {
      this.setFromXmlDocument(new FileInputStream(pXmlFileName), pXmlFileName);
    }
    catch (Exception e) {
      System.err.println("*** Exception in file " + pXmlFileName);
      e.printStackTrace();
      throw new DiasdemException("Error: The news item in file " 
      + Tools.shortenFileName(pXmlFileName, 50 ) 
      + " cannot be opened! Message: " + e.getMessage());
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public ReutersCorpusVol1NewsItem(InputStream pXmlInputStream) 
  throws DiasdemException {

    this();
    this.setFromXmlDocument(pXmlInputStream, "InputStream");

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getItemID() { 
    return ItemID; }
  public String getDate() { 
    return Date; }
  public String getTitle() { 
    return Title; }
  public String getHeadline() { 
    return Headline; }
  public String getByline() { 
    return Byline; }
  public String getDateline() { 
    return Dateline; }
  public String getText() { 
    return Text; }
  public String getCopyright() { 
    return Copyright; }
  public String getTopicCodes() { 
    return TopicCodes; }
  public String getRegionCodes() { 
    return RegionCodes; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(100);
    TmpStringBuffer.append("Reuters Corpus Vol. 1 News Item: ItemID=");
    TmpStringBuffer.append(ItemID);
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setFromXmlDocument(InputStream pXmlInputStream,
  String pXmlFilename) throws DiasdemException {
    
    // DTD: subset of NewsML 1999-11-10
    org.jdom.Document jDomDocument = null;
    org.jdom.Element rootJdomElement = null;
    List jDomElementList = null;
    org.jdom.Element jDomElement = null;
    try {      
      org.jdom.input.SAXBuilder jDomSaxBuilder = new org.jdom.input.SAXBuilder();
      jDomSaxBuilder.setValidation(false);
      jDomDocument = jDomSaxBuilder.build(pXmlInputStream);
      rootJdomElement = jDomDocument.getRootElement();
      TmpString = rootJdomElement.getAttributeValue("itemid");
      if (!Tools.stringIsNullOrEmpty(TmpString)) {
        ItemID = new String(TmpString);
      }
      else {
        ItemID = "";
      }
      TmpString = rootJdomElement.getAttributeValue("date");
      if (!Tools.stringIsNullOrEmpty(TmpString)) {
        Date = new String(TmpString);
      }
      else {
        Date = "";
      }
      TmpString = rootJdomElement.getChildTextTrim("title");
      if (!Tools.stringIsNullOrEmpty(TmpString)) {
        Title = new String(TmpString);
      }
      else {
        Title = "";
      }
      TmpString = rootJdomElement.getChildTextTrim("headline");
      if (!Tools.stringIsNullOrEmpty(TmpString)) {
        Headline = new String(TmpString);
      }
      else {
        Headline = "";
      }
      TmpString = rootJdomElement.getChildTextTrim("byline");
      if (!Tools.stringIsNullOrEmpty(TmpString)) {
        Byline = new String(TmpString);
      }
      else {
        Byline = "";
      }
      TmpString = rootJdomElement.getChildTextTrim("dateline");
      if (!Tools.stringIsNullOrEmpty(TmpString)) {
        Dateline = new String(TmpString);
      }
      else {
        Dateline = "";
      }
      
      // <text>...</text>
      TmpTextElement = rootJdomElement.getChild("text");
      TmpList = TmpTextElement.getContent();
      TmpStringBuffer = new StringBuffer(2 * TmpTextElement.getTextTrim()
      .length());
      for (int i = 0; i < TmpList.size(); i++) {
        if (TmpList.get(i) instanceof org.jdom.Text) {
          if (i > 0) {
            TmpStringBuffer.append(" ");
          }
          TmpStringBuffer.append(((org.jdom.Text)TmpList.get(i))
          .getTextTrim());
        }
        else if (TmpList.get(i) instanceof org.jdom.Element) {
          TmpTextChildElement = (org.jdom.Element)TmpList.get(i);
          if (TmpTextChildElement.getName().equals("p")
          || TmpTextChildElement.getName().equals("link")) {
            if (i > 0) {
              TmpStringBuffer.append(" ");
            }
            TmpString = ((org.jdom.Element)TmpList.get(i)).getTextTrim();
            TmpStringBuffer.append(TmpString);
            if (!Tools.stringIsNullOrEmpty(TmpString) && TmpString.charAt(
            TmpString.length() - 1) != '.' && !TmpString.endsWith(".\"")) {
              TmpStringBuffer.append(" .");
            }
          }
        }
      }
      
      Text = new String(TmpStringBuffer.toString());
      TmpString = rootJdomElement.getChildTextTrim("copyright");
      if (!Tools.stringIsNullOrEmpty(TmpString)) {
        Copyright = new String(TmpString);
      }
      else {
        Copyright = "";
      }
      
      // <metadata> topic codes </metadata>
      TmpTopicsBuffer = new StringBuffer(10000);
      TmpRegionsBuffer = new StringBuffer(10000);
      TmpMetadataElement = rootJdomElement.getChild("metadata");
      if (TmpMetadataElement != null) {
        TmpList = TmpMetadataElement.getChildren("codes");
        for (int i = 0; i < TmpList.size(); i++) {
          if (TmpList.get(i) instanceof org.jdom.Element
          && ((org.jdom.Element)TmpList.get(i)).getAttributeValue(
          "class").equals("bip:topics:1.0")) {
            TmpCodesList = ((org.jdom.Element)TmpList.get(i))
            .getChildren("code");
            for (int j = 0; j < TmpCodesList.size(); j++) {
              TmpCodeElement = (org.jdom.Element)TmpCodesList.get(j);
              if (!Tools.stringIsNullOrEmpty(TmpCodeElement.getAttributeValue(
              "code"))) {
                if (j > 0) { TmpTopicsBuffer.append(","); }
                TmpTopicsBuffer.append(TmpCodeElement
                .getAttributeValue("code"));
              }
            }
          }
          else if (TmpList.get(i) instanceof org.jdom.Element
          && ((org.jdom.Element)TmpList.get(i)).getAttributeValue(
          "class").equals("bip:countries:1.0")) {
            TmpCodesList = ((org.jdom.Element)TmpList.get(i))
            .getChildren("code");
            for (int j = 0; j < TmpCodesList.size(); j++) {
              TmpCodeElement = (org.jdom.Element)TmpCodesList.get(j);
              if (!Tools.stringIsNullOrEmpty(TmpCodeElement.getAttributeValue(
              "code"))) {
                if (j > 0) { TmpRegionsBuffer.append(","); }
                TmpRegionsBuffer.append(TmpCodeElement
                .getAttributeValue("code"));
              }
            }
          }
        }
      }
      TopicCodes = new String(TmpTopicsBuffer.toString().trim());
      RegionCodes = new String(TmpRegionsBuffer.toString().trim());
    
    }
    catch (Exception e) {
      System.err.println("*** Exception in file " + pXmlFilename);
      e.printStackTrace();
      throw new DiasdemException("Error: The news item in file " 
      + Tools.shortenFileName(pXmlFilename, 50 ) + " cannot be opened! Message: "
      + e.getMessage());
    }

  }
    
  /* ########## ########## ########## ########## ########## ######### */

  public final void reset() {
    
    ItemID = null;
    Date = null;
    Title = null;
    Headline = null;
    Byline = null;
    Dateline = null;
    Text = null;
    Copyright = null;
    TopicCodes = null;
    RegionCodes = null;
    
  }
  
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

  public static void main(String[] args) {}

}