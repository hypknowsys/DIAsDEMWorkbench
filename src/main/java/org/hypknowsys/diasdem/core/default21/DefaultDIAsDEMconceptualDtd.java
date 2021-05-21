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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import org.hypknowsys.diasdem.core.DIAsDEMdocument;
import org.hypknowsys.diasdem.core.DIAsDEMconceptualDtd;
import org.hypknowsys.diasdem.core.DIAsDEMconceptualDtdAttribute;
import org.hypknowsys.diasdem.core.DIAsDEMconceptualDtdElement;
import org.hypknowsys.diasdem.core.DIAsDEMproject;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnit;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.hypknowsys.misc.io.Itemizer;
import org.hypknowsys.misc.io.TextBufferedReader;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.KProperties;
import org.hypknowsys.misc.util.KProperty;
import org.hypknowsys.misc.util.Template;
import org.hypknowsys.misc.util.Tools;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMconceptualDtd extends KProperties
implements DIAsDEMconceptualDtd {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private TreeMap Elements = null;
  private TreeSet AttributeNames = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient TextBufferedReader TmpTextFileReader = null;
  private transient TextFile TmpTextFile = null;
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient DefaultDIAsDEMconceptualDtdElement TmpElement = null;
  private transient DefaultDIAsDEMconceptualDtdAttribute TmpAttribute = null;
  private transient Iterator TmpIterator = null;
  private transient TreeSet TmpTreeSet = null;
  private transient TreeMap TmpTreeMap = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final KProperty[] MY_PROPERTY_DATA = {
    new KProperty("CONCEPTUAL_DTD_ID",
    "Conceptual DTD ID",
    "null", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("CONCEPTUAL_DTD_REMARKS",
    "Conceptual DTD Remarks",
    "none", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("TRAINING_COLLECTION_FILE_NAME",
    "Absolute Name of Training Collection File",
    "null", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("ELEMENTS_FILE_NAME",
    "Absolute Name of File Containing Elements",
    "null", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("ATTRIBUTES_FILE_NAME",
    "Absolute Name of File Containing Attributes",
    "null", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("XML_DTD_FILE_NAME",
    "Absolute Name of XML DTD File",
    "null", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DTD_DOCUMENTATION_FILE_NAME",
    "Absolute Name of DTD Documentation File",
    "null", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("CONCEPTUAL_DTD_FILE_NAME",
    "Absolute Name of this Conceptual DTD File",
    "null", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("NUMBER_OF_DOCUMENTS",
    "Number of Documents Described by DTD",
    "0", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("NUMBER_OF_TEXT_UNITS",
    "Number of Text Units Described by DTD",
    "0", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("NUMBER_OF_TAGGED_TEXT_UNITS",
    "Number of Tagged Text Units Described by DTD",
    "0", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("NUMBER_OF_UNTAGGED_TEXT_UNITS",
    "Number of Untagged Text Units Described by DTD",
    "0", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("ROOT_ELEMENT",
    "Root Element of XML DTD",
    "RootElementOfXml", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("MIN_ATTRIBUTE_REL_SUPPORT",
    "Minimum Relative Support of Attributes",
    "0.1", KProperty.DOUBLE, KProperty.NOT_EDITABLE)
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMconceptualDtd(String pFileName, int pTask)
  throws IOException {
    
    super(MY_PROPERTY_DATA);
    
    FileName = pFileName;
    if (pTask == CREATE) {
      this.save(FileName);
    }
    else if (pTask == LOAD) {
      if (Tools.isExistingFile(pFileName)) {
        this.load(FileName);
      }
      else {
        throw new IOException();
      }
    }
    else {
      throw new IOException();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getConceptualDtdID() {
    return this.getStringProperty("CONCEPTUAL_DTD_ID"); }
  public String getConceptualDtdRemarks() {
    return this.getStringProperty("CONCEPTUAL_DTD_REMARKS"); }
  public String getTrainingCollectionFileName() {
    return this.getStringProperty("TRAINING_COLLECTION_FILE_NAME"); }
  public String getElementsFileName() {
    return this.getStringProperty("ELEMENTS_FILE_NAME"); }
  public String getAttributesFileName() {
    return this.getStringProperty("ATTRIBUTES_FILE_NAME"); }
  public String getXmlFileName() {
    return this.getStringProperty("XML_DTD_FILE_NAME"); }
  public String getDtdDocumentationFileName() {
    return this.getStringProperty("DTD_DOCUMENTATION_FILE_NAME"); }
  public long getNumberOfDocuments() {
    return this.getLongProperty("NUMBER_OF_DOCUMENTS"); }
  public long getNumberOfTextUnits() {
    return this.getLongProperty("NUMBER_OF_TEXT_UNITS"); }
  public long getNumberOfTaggedTextUnits() {
    return this.getLongProperty("NUMBER_OF_TAGGED_TEXT_UNITS"); }
  public long getNumberOfUntaggedTextUnits() {
    return this.getLongProperty("NUMBER_OF_UNTAGGED_TEXT_UNITS"); }
  public String getRootElement() {
    return this.getStringProperty("ROOT_ELEMENT"); }
  public double getMinAttributeRelSupport() {
    return this.getDoubleProperty("MIN_ATTRIBUTE_REL_SUPPORT"); }
  public TreeMap getElements() {
    return this.Elements; }
  
  public String getEncodingTag() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; }
  public String getDoctypeTag() {
    return "<!DOCTYPE " + this.getRootElement() + " SYSTEM '"
    + this.getRootElement() + ".dtd'>"; }
  public String getStartRootElementTag() {
    return "<" + this.getRootElement() + ">"; }
  public String getEndRootElementTag() {
    return "</" + this.getRootElement() + ">"; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setConceptualDtdID(String pConceptualDtdID) {
    this.setProperty("CONCEPTUAL_DTD_ID", pConceptualDtdID);
    //this.quickSave();
  }
  public void setConceptualDtdRemarks(String pConceptualDtdRemarks) {
    this.setProperty("CONCEPTUAL_DTD_REMARKS", pConceptualDtdRemarks);
    //this.quickSave();
  }
  public void setTrainingCollectionFileName(String pCollectionFileName) {
    this.setProperty("TRAINING_COLLECTION_FILE_NAME", pCollectionFileName);
    //this.quickSave();
  }
  public void setElementsFileName(String pElementsFileName) {
    this.setProperty("ELEMENTS_FILE_NAME", pElementsFileName);
    //this.quickSave();
  }
  public void setAttributesFileName(String pAttributesFileName) {
    this.setProperty("ATTRIBUTES_FILE_NAME", pAttributesFileName);
    //this.quickSave();
  }
  public void setXmlFileName(String pXmlFileName) {
    this.setProperty("XML_DTD_FILE_NAME", pXmlFileName);
    //this.quickSave();
  }
  public void setDtdDocumentationFileName(String pDtdDocumentationFileName) {
    this.setProperty("DTD_DOCUMENTATION_FILE_NAME", pDtdDocumentationFileName);
    //this.quickSave();
  }
  public void setNumberOfDocuments(long pNumberOfDocuments) {
    this.setLongProperty("NUMBER_OF_DOCUMENTS", pNumberOfDocuments);
    //this.quickSave();
  }
  public void setNumberOfTextUnits(long pNumberOfTextUnits) {
    this.setLongProperty("NUMBER_OF_TEXT_UNITS",pNumberOfTextUnits);
    //this.quickSave();
  }
  public void setNumberOfTaggedTextUnits(long pNumberOfTaggedTextUnits) {
    this.setLongProperty("NUMBER_OF_TAGGED_TEXT_UNITS",
    pNumberOfTaggedTextUnits);
    //this.quickSave();
  }
  public void setNumberOfUntaggedTextUnits(long pNumberOfUntaggedTextUnits) {
    this.setLongProperty("NUMBER_OF_UNTAGGED_TEXT_UNITS",
    pNumberOfUntaggedTextUnits);
    //this.quickSave();
  }
  public void setRootElement(String pRootElement) {
    this.setProperty("ROOT_ELEMENT", pRootElement);
    //this.quickSave();
  }
  public void setMinAttributeRelSupport(double pMinAttributeRelSupport) {
    this.setDoubleProperty("MIN_ATTRIBUTE_REL_SUPPORT",
    pMinAttributeRelSupport);
    //this.quickSave();
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("DefaultDIAsDEMconceptualDtd: RootElement=");
    TmpStringBuffer.append(this.getRootElement());
    
    if (Elements != null) {
      TmpIterator = Elements.values().iterator();
      while (TmpIterator.hasNext()) {
        TmpStringBuffer.append("\n");
        TmpStringBuffer.append((DefaultDIAsDEMconceptualDtdElement)TmpIterator
        .next());
      }
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void save(String pFileName) throws IOException {
    
    this.setProperty("CONCEPTUAL_DTD_FILE_NAME", pFileName);
    this.createDtdFiles();
    super.save(pFileName);
    
  }
  /* ########## ########## ########## ########## ########## ######### */
  
  public void save() throws IOException {
    
    this.save(FileName);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void quickSave() {
    
    if (FileName != null) {
      try {
        // this method does not call createDtdFiles()
        super.save(FileName);
      }
      catch (IOException e) {
        System.err.println("[ConceptualDtd] Warning: Property file "
        + FileName + " cannot be saved!");
        System.err.flush();
        FileName = null;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void load(String pFileName) throws IOException {
    
    super.load(pFileName);
    Elements = new TreeMap();
    AttributeNames = new TreeSet();
    
    String myDirectory = Tools.ensureTrailingSlash(Tools.extractDirectory(
    this.FileName));
    String myFileName = Tools.removeDirectory(this.FileName);
    
    // retrieve DTD elements
    TmpTextFileReader = new TextBufferedReader(new File(myDirectory
    + this.getElementsFileName()));
    TmpTextFileReader.open();
    TmpString = TmpTextFileReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (TmpString != null) {
      TmpElement = new DefaultDIAsDEMconceptualDtdElement();
      try {
        TmpElement.fromItemLine(TmpString);
        Elements.put(TmpElement.getName(), TmpElement);
      }
      catch (NumberFormatException e) {
        e.printStackTrace();
      }
      TmpString = TmpTextFileReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    TmpTextFileReader.close();
    
    // retrieve attributes of DTD elements
    Itemizer itemizer = null;
    String elementName = null;
    String attributeItemLine = null;
    TmpTextFileReader = new TextBufferedReader(new File(myDirectory
    + this.getAttributesFileName()));
    TmpTextFileReader.open();
    TmpString = TmpTextFileReader.getFirstLineButIgnoreCommentsAndEmptyLines();
    while (TmpString != null) {
      elementName = null;
      attributeItemLine = null;
      itemizer = new Itemizer(TmpString);
      try {
        elementName = Itemizer.itemToString(itemizer.getNextItem());
        attributeItemLine = itemizer.getEndOfLine();
        TmpAttribute = new DefaultDIAsDEMconceptualDtdAttribute();
        TmpAttribute.fromItemLine(attributeItemLine);
        TmpElement = (DefaultDIAsDEMconceptualDtdElement)Elements
        .get(elementName);
        if (TmpElement != null) {
          TmpElement.addOrUpdateAttribute(TmpAttribute);
          Elements.put(TmpElement.getName(), TmpElement);
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      TmpString = TmpTextFileReader.getNextLineButIgnoreCommentsAndEmptyLines();
    }
    TmpTextFileReader.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMconceptualDtdElement getElement(String pName) {
    
    return (DIAsDEMconceptualDtdElement)Elements.get(pName);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addOrUpdateElement(String pName) {
    
    TmpElement = (DefaultDIAsDEMconceptualDtdElement)Elements.get(pName);
    if (TmpElement != null) {
      TmpElement.incrementAbsoluteSupport();
      Elements.put(pName, TmpElement);
    }
    else {
      TmpElement = new DefaultDIAsDEMconceptualDtdElement(pName, 1L, 0.0);
      Elements.put(pName, TmpElement);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addOrUpdateElementAttributes(String pElementName,
  NamedEntity[] pNamedEntities) {
    
    if (pNamedEntities == null) {
      return;
    }
    
    TmpTreeSet = new TreeSet();
    boolean newNamedEntityType = false;
    
    for (int i = 0; i < pNamedEntities.length; i++) {
      // add or update only naed entities whose type is set
      if (pNamedEntities[i].getMostProbableTypeString() != null) {
        // register each named entity type only once
        newNamedEntityType = TmpTreeSet.add(
        pNamedEntities[i].getMostProbableTypeString());
        if (newNamedEntityType) {
          // memorize all names of attributes within the DTD
          AttributeNames.add(pNamedEntities[i].getMostProbableTypeString());
          // add or update named entity within the element
          TmpElement = (DefaultDIAsDEMconceptualDtdElement)Elements
          .get(pElementName);
          if (TmpElement != null) {
            TmpElement.addOrUpdateAttribute(pNamedEntities[i]);
            Elements.put(pElementName, TmpElement);
          }
          else {
            TmpElement = new DefaultDIAsDEMconceptualDtdElement(
            pElementName, 1L, 0.0);
            TmpElement.addOrUpdateAttribute(pNamedEntities[i]);
            Elements.put(pElementName, TmpElement);
          }
        }  // newNamedEntityType
      }
    }  //
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TreeSet getElementNames() {
    
    return new TreeSet(Elements.keySet());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Iterator getElementIterator() {
    
    return Elements.values().iterator();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMconceptualDtdElement[] getAllElements() {
    
    DIAsDEMconceptualDtdElement[] result = new DIAsDEMconceptualDtdElement[
    Elements.size()];
    Iterator iterator = this.getElementIterator();
    for (int i = 0; iterator.hasNext(); i++) {
      result[i] = (DIAsDEMconceptualDtdElement)iterator.next();
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getElementAttributesString(String pElementName,
  NamedEntity[] pNamedEntities, double pMinimumRelativeSupport) {
    
    if (pNamedEntities == null) {
      return "";
    }
    
    TmpTreeMap = new TreeMap();
    TmpStringBuffer = null;
    
    for (int i = 0; i < pNamedEntities.length; i++) {
      if (pNamedEntities[i].getMostProbableTypeString() != null) {
        if (TmpTreeMap.containsKey(pNamedEntities[i].
        getMostProbableTypeString())) {
          TmpStringBuffer = (StringBuffer)TmpTreeMap.get(pNamedEntities[i].
          getMostProbableTypeString());
          TmpStringBuffer.append(" [AND] ");
          TmpStringBuffer.append(
          pNamedEntities[i].getMostProbableDescription());
          TmpTreeMap.put(pNamedEntities[i].getMostProbableTypeString(),
          TmpStringBuffer);
        }
        else {
          if (this.isValidAttribute(pElementName, pNamedEntities[i]
          .getMostProbableTypeString(), pMinimumRelativeSupport)) {
            TmpTreeMap.put(pNamedEntities[i].getMostProbableTypeString(), new
            StringBuffer(pNamedEntities[i].getMostProbableDescription()));
          }
        }
      }
    }
    
    TmpStringBuffer = new StringBuffer();
    TmpIterator = TmpTreeMap.keySet().iterator();
    while (TmpIterator.hasNext()) {
      TmpString = (String)TmpIterator.next();
      TmpStringBuffer.append(" ");
      TmpStringBuffer.append(TmpString);
      TmpStringBuffer.append("=\"");
      TmpStringBuffer.append(Tools.insertUTF8EntityReferences(
      ((StringBuffer)TmpTreeMap.get(TmpString)).toString()));
      TmpStringBuffer.append("\"");
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntity[] getValidElementAttributes(String pElementName,
  NamedEntity[] pNamedEntities) {
    
    if (pNamedEntities == null) {
      return new NamedEntity[0];
    }
    
    TmpTreeMap = new TreeMap();
    ArrayList validNamedEntities = new ArrayList();
    for (int i = 0; i < pNamedEntities.length; i++) {
      if (pNamedEntities[i].getMostProbableTypeString() != null) {
        if (TmpTreeMap.containsKey(pNamedEntities[i]
        .getMostProbableTypeString())) {
          validNamedEntities.add(pNamedEntities[i]);
        }
        else {
          if (this.isValidAttribute(pElementName, pNamedEntities[i]
          .getMostProbableTypeString(), this.getMinAttributeRelSupport())) {
            TmpTreeMap.put(pNamedEntities[i].getMostProbableTypeString(), new
            StringBuffer(pNamedEntities[i].getMostProbableDescription()));
            validNamedEntities.add(pNamedEntities[i]);
          }
        }
      }
    }
    
    NamedEntity[] result = new NamedEntity[validNamedEntities.size()];
    for (int i = 0; i < validNamedEntities.size(); i++) {
      result[i] = (NamedEntity)validNamedEntities.get(i);
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Element appendValidAttributes(String pElementName,
  Element pElement, NamedEntity[] pNamedEntities) {
    
    if (pElement == null) {
      return null;
    }
    
    TmpTreeMap = new TreeMap();
    TmpStringBuffer = null;
    
    for (int i = 0; i < pNamedEntities.length; i++) {
      // add or update only naed entities whose type is set
      if (pNamedEntities[i].getMostProbableTypeString() != null) {
        if (TmpTreeMap.containsKey(pNamedEntities[i].
        getMostProbableTypeString())) {
          TmpStringBuffer = (StringBuffer)TmpTreeMap.get(pNamedEntities[i].
          getMostProbableTypeString());
          TmpStringBuffer.append(" [AND] ");
          TmpStringBuffer.append(
          pNamedEntities[i].getMostProbableDescription());
          TmpTreeMap.put(pNamedEntities[i].getMostProbableTypeString(),
          TmpStringBuffer);
        }
        else {
          if (this.isValidAttribute(pElementName, pNamedEntities[i]
          .getMostProbableTypeString(), this.getMinAttributeRelSupport())) {
            TmpTreeMap.put(pNamedEntities[i].getMostProbableTypeString(), new
            StringBuffer(pNamedEntities[i].getMostProbableDescription()));
          }
        }
      }
    }
    
    TmpStringBuffer = new StringBuffer();
    TmpIterator = TmpTreeMap.keySet().iterator();
    while (TmpIterator.hasNext()) {
      TmpString = (String)TmpIterator.next();
      if (TmpString != null) {
        pElement.setAttribute(TmpString, ((StringBuffer)TmpTreeMap
        .get(TmpString)).toString().replace('_', ' '));
      }
    }
    
    return pElement;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TreeSet getElementAttributesNames(String pElementName,
  double pMinimumRelativeSupport) {
    
    TmpElement = (DefaultDIAsDEMconceptualDtdElement)Elements
    .get(pElementName);
    if (TmpElement != null) {
      return TmpElement.getAttributeNames(pMinimumRelativeSupport);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String[] getElementAttributesNames(String pElementName) {
    
    TmpElement = (DefaultDIAsDEMconceptualDtdElement)Elements.get(pElementName);
    if (TmpElement != null) {
      TreeSet attributeSet = TmpElement.getAttributeNames(this
      .getMinAttributeRelSupport());
      if (attributeSet.size() > 0) {
        String[] result = new String[attributeSet.size()];
        Iterator iterator = attributeSet.iterator();
        int i = 0;
        while (iterator.hasNext()) {
          result[i++] = (String)iterator.next();
        }
        return result;
      } 
      else {
        return null;
      }
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void computeRelativeSupportOfElements(long pNumberOfTextUnits) {
    
    TmpIterator = Elements.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpElement = (DefaultDIAsDEMconceptualDtdElement)TmpIterator.next();
      TmpElement.setRelativeSupport(TmpElement.getAbsoluteSupport()
      / (double)pNumberOfTextUnits);
      TmpElement.computeRelativeSupportOfAttributes();
      Elements.put(TmpElement.getName(), TmpElement);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeXmlRepresentation(String pDirectory) {
    
    if (pDirectory.endsWith(File.separator)) {
      this.writeXmlRepresentationToFile(pDirectory + this.getRootElement()
      + ".dtd");
    }
    else {
      this.writeXmlRepresentationToFile(pDirectory  + File.separator
      + this.getRootElement() + ".dtd");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeDtdDocumentation(String pFileName) {
    
    if (pFileName == null || !Tools.isValidandWriteableFileName(pFileName)) {
      return;
    }
    TextFile doc = new TextFile(new File(pFileName));
    doc.open();
    
    Template htmlHeader = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_HeaderTemplate.html"));
    htmlHeader.addValue("${Title}", "Documentation of Conceptual XML DTD");
    doc.setFirstLine(htmlHeader.insertValues());
    doc.setNextLine(
    "<p>Created by Tasks &gt; Derive Conceptual DTD 2.2"
    + "&gt; Monitor Cluster Quality 2.2 on " + Tools.getSystemDate() +"</p>");
    
    doc.setNextLine(
    "<a name=\"SUMMARY\"><h3>DTD Summary</h3>");
    doc.setNextLine("<p><small>"
    + "<a href=\"#ELEMENTS\">Summary of DTD Elements</a>"
    + "</small></p>");
    doc.setNextLine("<h4>Root Element:</h4>");
    doc.setNextLine("<p>");
    doc.setNextLine(this.getRootElement());
    doc.setNextLine("</p>");
    doc.setNextLine("<h4>Description:</h4>");
    doc.setNextLine("<p>");
    doc.setNextLine(
    (Tools.stringIsNullOrEmpty(this.getConceptualDtdRemarks()) ? "&nbsp;" 
    : Tools.insertHtmlEntityReferences(this.getConceptualDtdRemarks())));
    doc.setNextLine("</p>");
    doc.setNextLine("<h4>Properties:</h4>");
    doc.setNextLine(
    "<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">Property</th>"
    + "<th align=\"left\" valign=\"top\">Value</th></tr>");
    doc.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Conceptual DTD File</td>"
    + "<td align=\"left\" valign=\"top\">" + Tools.shortenFileName(
    this.getStringProperty("CONCEPTUAL_DTD_FILE_NAME"), 80) + "</td></tr>");
    doc.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">XML DTD File</td>"
    + "<td align=\"left\" valign=\"top\"><a href=\"" + Tools.extractDirectory(
    this.getStringProperty("CONCEPTUAL_DTD_FILE_NAME")) + this
    .getStringProperty("XML_DTD_FILE_NAME") + "\">" + Tools.shortenFileName(
    Tools.extractDirectory(this.getStringProperty("CONCEPTUAL_DTD_FILE_NAME"))
    + this.getStringProperty("XML_DTD_FILE_NAME"), 80) + "</a></td></tr>");
    doc.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Training Collection File</td>"
    + "<td align=\"left\" valign=\"top\">" + Tools.shortenFileName(
    this.getTrainingCollectionFileName(), 80) + "</td></tr>");
    doc.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Number of Documents</td>"
    + "<td align=\"left\" valign=\"top\">" 
    + this.getNumberOfDocuments() + "</td></tr>");
    doc.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Number of Text Units</td>"
    + "<td align=\"left\" valign=\"top\">" 
    + this.getNumberOfTextUnits() + "</td></tr>");
    doc.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Number of Tagged Text Units</td>"
    + "<td align=\"left\" valign=\"top\">" 
    + this.getNumberOfTaggedTextUnits() + "</td></tr>");
    doc.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Number of Untagged Text Units</td>"
    + "<td align=\"left\" valign=\"top\">" 
    + this.getNumberOfUntaggedTextUnits() + "</td></tr>");
    doc.setNextLine("<tr>"
    + "<td align=\"left\" valign=\"top\">Min. Attribute Support</td>"
    + "<td align=\"left\" valign=\"top\">" 
    + this.getMinAttributeRelSupport() + "</td></tr></table>");
    
    doc.setNextLine(
    "<a name=\"ELEMENTS\"><h3>Summary of DTD Elements</h3>");
    doc.setNextLine("<p><small>"
    + "<a href=\"#SUMMARY\">DTD Summary</a> - "
    + "<a href=\"#ELEMENT1\">DTD Element 1</a>"
    + "</small></p>");
    doc.setNextLine(
    "<table border=\"1\"><tr>"
    + "<th align=\"left\" valign=\"top\">No.</th>"
    + "<th align=\"left\" valign=\"top\">DTD Element Name</th></tr>");
    TmpIterator = Elements.keySet().iterator();
    int i = 1;
    while (TmpIterator.hasNext()) {
      doc.setNextLine("<tr>"
      + "<td align=\"left\" valign=\"top\">" + i + "</td>"
      + "<td align=\"left\" valign=\"top\"><a href=\"#ELEMENT" + i + "\">" 
      + Tools.insertHtmlLineBreaks(95, TmpIterator.next().toString()) 
      + "</td></tr>");
      i++;
    }
    doc.setNextLine("</table>");
    
    TmpIterator = Elements.keySet().iterator();
    i = 1;
    int numberOfElements = Elements.keySet().size();
    while (TmpIterator.hasNext()) {
      TmpElement = (DefaultDIAsDEMconceptualDtdElement)Elements.get(
      TmpIterator.next());
      doc.setNextLine(
      "<a name=\"ELEMENT" + i + "\"><h3>Summary of DTD Element " + i + "</h3>");
      doc.setNextLine("<p><small>"
      + "<a href=\"#SUMMARY\">DTD Summary</a> - "
      + "<a href=\"#ELEMENTS\">Summary of DTD Elements</a>"
      + (i > 1 ? " - <a href=\"#ELEMENT" + (i - 1) 
      + "\">DTD Element " + (i - 1) + "</a>" : "")
      + (i < numberOfElements ? " - <a href=\"#ELEMENT" + (i + 1) 
      + "\">DTD Element " + (i + 1) + "</a>" : "")
      + "</small></p>");
      doc.setNextLine("<h4>Name:</h4>");
      doc.setNextLine("<p>");
      doc.setNextLine(Tools.insertHtmlLineBreaks(100, TmpElement.getName()));
      doc.setNextLine("</p>");
      doc.setNextLine("<h4>Description:</h4>");
      doc.setNextLine((Tools.stringIsNullOrEmpty(TmpElement
      .getHtmlDescription()) ? "" : "<p>" + Tools
      .insertHtmlEntityReferences(TmpElement.getHtmlDescription()) + "</p>"));
      doc.setNextLine("<h4>Optional Attributes:</h4>");
      TmpElement.writeAttributesToDtdDocumentationFile(
      this.getMinAttributeRelSupport(), doc);
      doc.setNextLine("<h4>Examples:</h4>");
      doc.setNextLine((Tools.stringIsNullOrEmpty(TmpElement
      .getHtmlExamples()) ? "" : TmpElement.getHtmlExamples()));
      i++;
    }
    
    Template htmlFooter = new Template(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_FooterTemplate.html"));
    doc.setNextLine(htmlFooter.insertValues());
    doc.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Element getDocumentAsJDomElement(DIAsDEMproject pDiasdemProject,
  DIAsDEMdocument pDiasdemDocument) {
    
    NamedEntity[] namedEntities = null;
    DIAsDEMtextUnit diasdemTextUnit = null;
    String textUnitContentsAsString = null;
    
    Element rootElement = new Element(
    this.getRootElement());
    Element taggedDocumentElement = new Element(
    "TaggedDocument");
    Element textUnitElement = null;
    
    if (pDiasdemDocument.getMetaData() != null
    && pDiasdemDocument.getMetaData().size() > 0) {
      Iterator iterator = pDiasdemDocument.getMetaData().keySet().iterator();
      String name = null;
      String content = null;
      Element metaDataElement = null;
      while (iterator.hasNext()) {
        name = (String)iterator.next();
        content = (String)pDiasdemDocument.getMetaData().get(name);
        metaDataElement = new Element("MetaData");
        metaDataElement.addContent(new Element("Name")
        .setText(name));
        metaDataElement.addContent(new Element("Content")
        .setText(content));
        rootElement.addContent(metaDataElement);
      }
    }
    
    pDiasdemDocument.setActiveTextUnitsLayer(pDiasdemProject
    .getActiveTextUnitsLayerIndex());
    // read-only task does not require backup
    
    for (int i = 0; i < pDiasdemDocument.getNumberOfProcessedTextUnits();
    i++) {
      diasdemTextUnit = pDiasdemDocument.getProcessedTextUnit(i);
      textUnitContentsAsString = diasdemTextUnit.getContentsAsString();
      
      if (diasdemTextUnit.getClusterLabel() != null
      && (diasdemTextUnit.getClusterLabel().equals("-")
      || diasdemTextUnit.getClusterLabel().equals("="))) {
        taggedDocumentElement.addContent(pDiasdemDocument.getOriginalTextUnit(i)
        .getContentsAsString().replace('\n', ' ') + ' ');
      }
      else {
        textUnitElement = new Element(diasdemTextUnit.getClusterLabel());
        namedEntities = NamedEntity.getContainedNamedEntities(pDiasdemDocument
        .getActiveTextUnitsLayer(), diasdemTextUnit.getContentsAsString());
        textUnitElement = this.appendValidAttributes(diasdemTextUnit
        .getClusterLabel(), textUnitElement, namedEntities);
        textUnitElement.addContent(pDiasdemDocument.getOriginalTextUnit(i)
        .getContentsAsString().replace('\n', ' '));
        taggedDocumentElement.addContent(textUnitElement);
      }
    }
    rootElement.addContent(taggedDocumentElement);
    
    return rootElement;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void dumpDocumentAsXmlFile(DIAsDEMproject pDiasdemProject,
  DIAsDEMdocument pDiasdemDocument, String pFileName, boolean pCopyDtdFile) {
    
    if (Tools.isValidandWriteableFileName(pFileName)) {
      Document jdomDocument = new Document(this
      .getDocumentAsJDomElement(pDiasdemProject, pDiasdemDocument),
      new DocType(this.getRootElement(), null,
      this.getRootElement() + ".dtd"));
      XMLOutputter xmlOutputter = null;
      FileOutputStream fileOutputStream = null;
      Format xmlFormat = Format.getPrettyFormat();
      xmlFormat.setEncoding(DefaultDIAsDEMvolume.DTD_ENCODING);
      try {
        xmlOutputter = new XMLOutputter(xmlFormat);
        fileOutputStream = new FileOutputStream(pFileName);
        xmlOutputter.output(jdomDocument, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        if (pCopyDtdFile) {
          this.writeXmlRepresentation(Tools.extractDirectory(pFileName));
        }
      }
      catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error: The DefaultDIAsDEMdocument cannot be "
        + "dumped as semantic XML file " + pFileName + "! Message: "
        + e.getMessage());
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addHtmlExample(DIAsDEMproject pDiasdemProject,
  DIAsDEMdocument pDiasdemDocument, int pTextUnitID) {
    
    if (pDiasdemDocument == null || pTextUnitID < 0 
    || pTextUnitID >= pDiasdemDocument.getNumberOfProcessedTextUnits()) {
      return;
    }
    
    Element textUnitElement = null;
    pDiasdemDocument.setActiveTextUnitsLayer(pDiasdemProject
    .getActiveTextUnitsLayerIndex());
    // read-only task does not require backup
    
    NamedEntity[] namedEntities = null;
    DIAsDEMtextUnit diasdemTextUnit = pDiasdemDocument
    .getProcessedTextUnit(pTextUnitID);
    String elementName = diasdemTextUnit.getClusterLabel();
    String textUnitContentsAsString = diasdemTextUnit.getContentsAsString();
    
    if (elementName != null 
    && !(elementName.equals("-") || elementName.equals("="))) {
      textUnitElement = new Element(diasdemTextUnit.getClusterLabel());
      namedEntities = NamedEntity.getContainedNamedEntities(pDiasdemDocument
      .getActiveTextUnitsLayer(), diasdemTextUnit.getContentsAsString());
      textUnitElement = this.appendValidAttributes(diasdemTextUnit
      .getClusterLabel(), textUnitElement, namedEntities);
      textUnitElement.addContent(pDiasdemDocument
      .getOriginalTextUnit(pTextUnitID).getContentsAsString()
      .replace('\n', ' '));
      //     
      TmpElement = (DefaultDIAsDEMconceptualDtdElement)Elements.get(
      elementName);
      if (TmpElement != null) {
        XMLOutputter xmlOutputter = null;
        Format xmlFormat = Format.getPrettyFormat();
        xmlFormat.setEncoding(DefaultDIAsDEMvolume.DTD_ENCODING);
        xmlFormat.setOmitDeclaration(true);
        xmlFormat.setOmitEncoding(true);
        try {
          xmlOutputter = new XMLOutputter(xmlFormat);
          TmpElement.addHtmlExample(xmlOutputter.outputString(textUnitElement));
        }
        catch (Exception e) {
          e.printStackTrace();
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
    
    Elements = new TreeMap();
    AttributeNames = new TreeSet();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createDtdFiles() {
    
    if (Elements == null) {
      Elements = new TreeMap();
    }
    if (AttributeNames == null) {
      AttributeNames = new TreeSet();
    }
    
    String myDirectory = Tools.ensureTrailingSlash(Tools.extractDirectory(
    this.FileName));
    String myFileName = Tools.removeDirectory(this.FileName);
    this.setElementsFileName(myFileName + ".elements");
    TextFile elementsFile = new TextFile(new File(myDirectory
    + this.getElementsFileName()));
    elementsFile.open();
    
    this.setAttributesFileName(myFileName + ".attributes");
    TextFile attributesFile = new TextFile(new File(myDirectory
    + this.getAttributesFileName()));
    attributesFile.open();
    
    this.setXmlFileName(myFileName + ".xml");
    this.writeXmlRepresentationToFile(myDirectory + this.getXmlFileName());
    
    elementsFile.setFirstLine("# "
    + DefaultDIAsDEMconceptualDtdElement.getCsvHeaderLine());
    attributesFile.setFirstLine("# ConceptualDtdElement,"
    + DefaultDIAsDEMconceptualDtdAttribute.getCsvHeaderLine());
    
    TmpIterator = Elements.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpElement = (DefaultDIAsDEMconceptualDtdElement)TmpIterator.next();
      elementsFile.setNextLine(TmpElement.toItemLine());
    }
    
    TmpIterator = Elements.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpElement = (DefaultDIAsDEMconceptualDtdElement)TmpIterator.next();
      TmpElement.writeAttributesToItemFile(this.getMinAttributeRelSupport(),
      attributesFile);
    }
    
    elementsFile.close();
    attributesFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void writeXmlRepresentationToFile(String pFileName) {
    
    if (pFileName == null || !Tools.isValidandWriteableFileName(pFileName)) {
      return;
    }
    TextFile dtd = new TextFile(new File(pFileName));
    
    dtd.open();
    dtd.setFirstLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    dtd.setNextLine("");
    dtd.setNextLine("<!ELEMENT " + this.getRootElement()
    + " (MetaData*, TaggedDocument)> ");
    dtd.setNextLine("");
    dtd.setNextLine("<!ELEMENT MetaData (Name, Content)>");
    dtd.setNextLine("<!ELEMENT Name (#PCDATA)>");
    dtd.setNextLine("<!ELEMENT Content (#PCDATA)>");
    dtd.setNextLine("");
    dtd.setNextLine("<!ELEMENT TaggedDocument ( #PCDATA");
    TmpIterator = Elements.keySet().iterator();
    while (TmpIterator.hasNext()) {
      dtd.setNextLine(" | " + (String)TmpIterator.next());
    }
    dtd.setNextLine(")* > ");
    dtd.setNextLine("");
    
    TmpIterator = Elements.values().iterator();
    while (TmpIterator.hasNext()) {
      TmpElement = (DefaultDIAsDEMconceptualDtdElement)TmpIterator.next();
      dtd.setNextLine(TmpElement.getXmlRepresentation());
    }
    dtd.setNextLine("");
    
    TmpIterator = Elements.values().iterator();
    while (TmpIterator.hasNext()) {
      ((DefaultDIAsDEMconceptualDtdElement)TmpIterator.next())
      .writeAttributesToXmlRepresentation(
      this.getMinAttributeRelSupport(), dtd);
    }
    
    dtd.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean isValidAttribute(String pElementName, String pAttributeName,
  double pMinAttributeRelSupport) {
    
    DefaultDIAsDEMconceptualDtdElement element =
    (DefaultDIAsDEMconceptualDtdElement)Elements.get(pElementName);
    if (element == null) {
      return false;
    }
    DIAsDEMconceptualDtdAttribute attribute =  element
    .getAttribute(pAttributeName);
    if (attribute == null) {
      return false;
    }
    else if (attribute.getRelativeSupport() >= pMinAttributeRelSupport) {
      return true;
    }
    else {
      return false;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String[] pOptions) {
    
    DefaultDIAsDEMconceptualDtd myDtd = null;
    try {
      myDtd = new DefaultDIAsDEMconceptualDtd(
      "./ConceptualDtdText.meta", DefaultDIAsDEMconceptualDtd.CREATE);
    }
    catch (IOException e) { e.printStackTrace(); }
    System.out.println(
    "[DefaultDIAsDEMconceptualDtd] main() myDtd.getElements()="
    + myDtd.getElements());
    myDtd.setRootElement("TestDtdTagged");
    myDtd.setMinAttributeRelSupport(0.1);
    myDtd.addOrUpdateElement("D");
    myDtd.addOrUpdateElement("C");
    myDtd.addOrUpdateElement("B");
    myDtd.addOrUpdateElement("A");
    myDtd.addOrUpdateElement("A");
    myDtd.addOrUpdateElement("B");
    myDtd.addOrUpdateElement("A");
    myDtd.computeRelativeSupportOfElements(7L);
    System.out.println(myDtd.toString());
    myDtd.writeXmlRepresentation("/home/kwinkler/diasdem/DIAsDEM.workbench");
    
  }
  
}