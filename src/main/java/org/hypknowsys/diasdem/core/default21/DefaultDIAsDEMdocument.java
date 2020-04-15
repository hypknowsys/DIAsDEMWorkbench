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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.hypknowsys.diasdem.core.DIAsDEMdocument;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnit;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnitsLayer;
import org.hypknowsys.misc.util.Tools;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMdocument implements DIAsDEMdocument {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private HashMap MetaData = null;
  private String OriginalText = null;
  private ArrayList TextUnitsLayers = null;
  private int ActiveTextUnitsLayerIndex = -1;
  private DIAsDEMtextUnitsLayer ActiveTextUnitsLayer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient boolean HasBeenDumped = false;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static int FIRST_PLACEHOLDER_ID = 1000;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMdocument() {
    
    MetaData = new HashMap();
    OriginalText = "";
    TextUnitsLayers = new ArrayList();
    ActiveTextUnitsLayerIndex = -1;
    ActiveTextUnitsLayer = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public HashMap getMetaData() {
    return MetaData; }
  public String getOriginalText() {
    return OriginalText; }
  public String getDiasdemDocumentID() {
    return (String)MetaData.get("DiasdemDocumentID"); }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setMetaData(HashMap pMetaData) {
    if (pMetaData != null) {
      MetaData = pMetaData;
    }
  }
  public void setOriginalText(String pOriginalText) {
    OriginalText = pOriginalText; }
  public void setDiasdemDocumentID(String pDiasdemDocumentID) {
    MetaData.put("DiasdemDocumentID", pDiasdemDocumentID); }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("DefaultDIAsDEMdocument: NumberOfTextUnitLayers=");
    TmpStringBuffer.append(this.getNumberOfTextUnitsLayers());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface DIAsDEMdocument methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String dumpAsXmlFile(String pDirectory, boolean pDumpOnlyOnce) {
    
    if (Tools.isValidandWriteableDirectoryName(pDirectory)) {
      String fileName = Tools.ensureTrailingSlash(pDirectory)
      + Tools.removeDirectory(this.getDiasdemDocumentID().replace(':', '_'))
      + ".xml";
      if (!HasBeenDumped || !pDumpOnlyOnce) {
        this.dumpAsXmlFile(fileName);
        HasBeenDumped = true;
        return fileName;
      }
      else {
        return fileName;
      }
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void dumpAsXmlFile(String pFileName) {
    
    if (Tools.isValidandWriteableFileName(pFileName)) {
      Document jdomDocument = new Document(this
      .getAsJDomElement());
      XMLOutputter xmlOutputter = null;
      FileOutputStream fileOutputStream = null;
      Format xmlFormat = Format
      .getPrettyFormat();
      xmlFormat.setEncoding(DefaultDIAsDEMvolume.DTD_ENCODING);
      try {
        xmlOutputter = new XMLOutputter(xmlFormat);
        fileOutputStream = new FileOutputStream(pFileName);
        xmlOutputter.output(jdomDocument, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        HasBeenDumped = true;
      }
      catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error: The DefaultDIAsDEMdocument cannot be "
        + "dumped as XML file " + pFileName + "! Message: "
        + e.getMessage());
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfTextUnitsLayers() {
    
    if (TextUnitsLayers != null) {
      return TextUnitsLayers.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getActiveTextUnitsLayerIndex() {
    
    return ActiveTextUnitsLayerIndex;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setActiveTextUnitsLayerIndex(int pActiveTextUnitsLayerIndex) {
    
    if (TextUnitsLayers != null && pActiveTextUnitsLayerIndex >= 0
    && pActiveTextUnitsLayerIndex < this.getNumberOfTextUnitsLayers()) {
      ActiveTextUnitsLayerIndex = pActiveTextUnitsLayerIndex;
    }
    else {
      ActiveTextUnitsLayerIndex = -1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnitsLayer getActiveTextUnitsLayer() {
    
    if (TextUnitsLayers != null && ActiveTextUnitsLayerIndex >= 0
    && ActiveTextUnitsLayerIndex < this.getNumberOfTextUnitsLayers()) {
      return (DIAsDEMtextUnitsLayer)TextUnitsLayers
      .get(ActiveTextUnitsLayerIndex);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setActiveTextUnitsLayer(int pActiveTextUnitsLayerIndex) {
    
    this.setActiveTextUnitsLayerIndex(pActiveTextUnitsLayerIndex);
    if (ActiveTextUnitsLayerIndex >= 0) {
      ActiveTextUnitsLayer = (DIAsDEMtextUnitsLayer)TextUnitsLayers
      .get(ActiveTextUnitsLayerIndex);
    }
    else {
      ActiveTextUnitsLayer = null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int createTextUnitsLayer(String pTextUnitsDescription,
  boolean pIsActiveLayer) {
    
    DefaultDIAsDEMtextUnitsLayer newLayer = new DefaultDIAsDEMtextUnitsLayer();
    newLayer.setTextUnitsDescription(pTextUnitsDescription);
    int newTextUnitsLayerIndex = this.getNumberOfTextUnitsLayers();
    if (TextUnitsLayers == null) {
      TextUnitsLayers = new ArrayList();
    }
    TextUnitsLayers.add(newLayer);
    if (pIsActiveLayer) {
      this.setActiveTextUnitsLayer(newTextUnitsLayerIndex);
    }
    
    return newTextUnitsLayerIndex;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int resetActiveTextUnitsLayer(String pTextUnitsDescription) {
    
    if (ActiveTextUnitsLayerIndex >= 0 && ActiveTextUnitsLayer != null) {
      ActiveTextUnitsLayer = new DefaultDIAsDEMtextUnitsLayer();
      ActiveTextUnitsLayer.setTextUnitsDescription(pTextUnitsDescription);
      if (TextUnitsLayers == null) {
        TextUnitsLayers = new ArrayList();
      }
      TextUnitsLayers.set(ActiveTextUnitsLayerIndex, ActiveTextUnitsLayer);
      return ActiveTextUnitsLayerIndex;
    }
    else {
      return this.createTextUnitsLayer(pTextUnitsDescription, true);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addTextUnitsLayer(DIAsDEMtextUnitsLayer pTextUnitsLayer) {
    
    if (pTextUnitsLayer == null
    || !(pTextUnitsLayer instanceof DefaultDIAsDEMtextUnit)) {
      return -1;
    }
    else {
      int newTextUnitsLayerIndex = this.getNumberOfTextUnitsLayers();
      if (TextUnitsLayers == null) {
        TextUnitsLayers = new ArrayList();
      }
      TextUnitsLayers.add(pTextUnitsLayer);
      return newTextUnitsLayerIndex;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceTextUnitsLayer(int pTextUnitsLayerIndex,
  DIAsDEMtextUnitsLayer pTextUnitsLayer) {
    
    if (pTextUnitsLayer == null || TextUnitsLayers == null
    || pTextUnitsLayerIndex >= this.getNumberOfTextUnitsLayers()
    || pTextUnitsLayerIndex < 0
    || !(pTextUnitsLayer instanceof DefaultDIAsDEMtextUnit)) {
      System.err.println("[DefaultDIAsDEMdocument] DIAsDEMtextUnitsLayer cannot"
      + " be replaced at index " + pTextUnitsLayerIndex + "!");
    }
    else {
      TextUnitsLayers.set(pTextUnitsLayerIndex, pTextUnitsLayer);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnitsLayer getTextUnitsLayer(
  int pTextUnitsLayerIndex) {
    
    if (TextUnitsLayers == null || pTextUnitsLayerIndex < 0
    || pTextUnitsLayerIndex >= this.getNumberOfTextUnitsLayers()) {
      return null;
    }
    else {
      return (DIAsDEMtextUnitsLayer)TextUnitsLayers.get(pTextUnitsLayerIndex);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void deleteTextUnitsLayer(int pTextUnitsLayerIndex) {
    
    if (TextUnitsLayers == null || pTextUnitsLayerIndex < 0
    || pTextUnitsLayerIndex >= this.getNumberOfTextUnitsLayers()) {
      TextUnitsLayers.remove(pTextUnitsLayerIndex);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfOriginalTextUnits() {
    
    if (ActiveTextUnitsLayer != null) {
      return ActiveTextUnitsLayer.getNumberOfOriginalTextUnits();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addOriginalTextUnit(DIAsDEMtextUnit pDiasdemTextUnit,
  boolean pAddProcessedTextUnitAsWell) {
    
    if (ActiveTextUnitsLayer != null) {
      return ActiveTextUnitsLayer.addOriginalTextUnit(pDiasdemTextUnit,
      pAddProcessedTextUnitAsWell);
    }
    else {
      return -1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceOriginalTextUnit(int pDiasdemTextUnitIndex,
  DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (ActiveTextUnitsLayer != null) {
      ActiveTextUnitsLayer.replaceOriginalTextUnit(pDiasdemTextUnitIndex,
      pDiasdemTextUnit);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnit getOriginalTextUnit(int pTextUnitIndex) {
    
    if (ActiveTextUnitsLayer != null) {
      return ActiveTextUnitsLayer.getOriginalTextUnit(pTextUnitIndex);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void resetOriginalTextUnits() {
    
    if (ActiveTextUnitsLayer != null) {
      ActiveTextUnitsLayer.resetOriginalTextUnits();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfProcessedTextUnits() {
    
    if (ActiveTextUnitsLayer != null) {
      return ActiveTextUnitsLayer.getNumberOfProcessedTextUnits();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addProcessedTextUnit(DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (ActiveTextUnitsLayer != null) {
      return ActiveTextUnitsLayer.addProcessedTextUnit(pDiasdemTextUnit);
    }
    else {
      return -1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceProcessedTextUnit(int pDiasdemTextUnitIndex,
  DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (ActiveTextUnitsLayer != null) {
      ActiveTextUnitsLayer.replaceProcessedTextUnit(pDiasdemTextUnitIndex,
      pDiasdemTextUnit);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnit getProcessedTextUnit(int pTextUnitIndex) {
    
    if (ActiveTextUnitsLayer != null) {
      return ActiveTextUnitsLayer.getProcessedTextUnit(pTextUnitIndex);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void resetProcessedTextUnits() {
    
    if (ActiveTextUnitsLayer != null) {
      ActiveTextUnitsLayer.resetProcessedTextUnits();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void backupProcessedTextUnits(int pProcessedTextUnitsRollbackOption) {
    
    if (ActiveTextUnitsLayer != null) {
      ActiveTextUnitsLayer.backupProcessedTextUnits(
      pProcessedTextUnitsRollbackOption);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void rollbackProcessedTextUnits(int pRollbackTextUnitsID) {
    
    if (ActiveTextUnitsLayer != null) {
      ActiveTextUnitsLayer.rollbackProcessedTextUnits(
      pRollbackTextUnitsID);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getMaxRollbackTextUnitsID() {
    
    if (ActiveTextUnitsLayer != null) {
      return ActiveTextUnitsLayer.getMaxRollbackTextUnitsID();
    }
    else {
      return -1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public Element getAsJDomElement() {
    
    Element rootJdomElement = new Element(
    "DefaultDIAsDEMdocument");
    
    Iterator iterator = MetaData.keySet().iterator();
    String name = null;
    String content = null;
    Element metaDataElement = null;
    while (iterator.hasNext()) {
      name = (String)iterator.next();
      content = (String)MetaData.get(name);
      metaDataElement = new Element("MetaData");
      metaDataElement.addContent(new Element("Name")
      .setText(name));
      metaDataElement.addContent(new Element("Content")
      .setText(content));
      rootJdomElement.addContent(metaDataElement);
    }
    
    Element myJdomElement = null;
    myJdomElement = new Element("OriginalText");
    myJdomElement.addContent(OriginalText);
    rootJdomElement.addContent(myJdomElement);
    
    rootJdomElement.setAttribute("NumberOfTextUnitsLayers",
    Tools.int2String(this.getNumberOfTextUnitsLayers()));
    if (TextUnitsLayers != null && TextUnitsLayers.size() > 0) {
      for (int i = 0; i < TextUnitsLayers.size(); i++) {
        myJdomElement = ((DefaultDIAsDEMtextUnitsLayer)TextUnitsLayers
        .get(i)).getAsJDomElement(i);
        rootJdomElement.addContent(myJdomElement);
      }
    }
    
    return rootJdomElement;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFromJDomElement(Element pJDomElement) {
    
    this.reset();
    
    // assuming that pJDomElement has been validated against DTD
    String name = null;
    String content = null;
    Element myJdomElement = null;
    List children = pJDomElement.getChildren("MetaData");
    for (int i = 0; i < children.size(); i++) {
      if (children.get(i) instanceof Element) {
        myJdomElement = (Element)children.get(i);
        name = myJdomElement.getChildTextTrim("Name");
        content = myJdomElement.getChildTextTrim("Content");
        MetaData.put(name, content);
      }
    }
    
    myJdomElement = pJDomElement.getChild("OriginalText");
    if (myJdomElement != null) {
      OriginalText = pJDomElement.getChildTextTrim("OriginalText");
    }
    
    children = pJDomElement.getChildren("TextUnitsLayer");
    if (children != null) {
      DefaultDIAsDEMtextUnitsLayer textUnitsLayer = null;
      for (int i = 0; i < children.size(); i++) {
        if (children.get(i) instanceof Element) {
          myJdomElement = (Element)children.get(i);
          textUnitsLayer = new DefaultDIAsDEMtextUnitsLayer();
          textUnitsLayer.setFromJDomElement(myJdomElement);
          TextUnitsLayers.add(textUnitsLayer);
        }
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {
    
    MetaData = new HashMap();
    OriginalText = "";
    TextUnitsLayers = new ArrayList();
    ActiveTextUnitsLayerIndex = -1;
    ActiveTextUnitsLayer = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String[] pOptions) {}
  
}