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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hypknowsys.diasdem.core.DIAsDEMdocument;
import org.hypknowsys.diasdem.core.DIAsDEMvolume;
import org.hypknowsys.diasdem.core.DiasdemException;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Tools;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMvolume implements DIAsDEMvolume {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected ArrayList DiasdemDocuments = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient String FileName = null;
  private transient Iterator FirstNextIterator = null;
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String DTD_ROOT_ELEMENT = "DefaultDIAsDEMvolume";
  public static String DTD_DOCUMENT_ELEMENT = "DefaultDIAsDEMdocument";
  public static String DTD_FILE_NAME = "DefaultDIAsDEMvolume.dtd";
  public static String DTD_ENCODING = "utf-8";

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMvolume() {
    
    FileName = null;
    DiasdemDocuments = new ArrayList();
    
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
  /* ########## interface DIAsDEMvolume methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfDocuments() {
    
    if (DiasdemDocuments != null) {
      return DiasdemDocuments.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void create(String pDiasdemVolumeFileName)
  throws DiasdemException {
    
    if (pDiasdemVolumeFileName != null
    && Tools.isValidandWriteableFileName(pDiasdemVolumeFileName)) {
      FileName = pDiasdemVolumeFileName;
      DiasdemDocuments = new ArrayList();
      this.writeXml(FileName);
    }
    else {
      throw new DiasdemException("Error: The DefaultDIAsDEMvolume cannot "
      + " be closed, because FileName == null or FileName is not valid!");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void open(String pDiasdemVolumeFileName)
  throws DiasdemException {
    
    if (pDiasdemVolumeFileName != null
    && Tools.isValidandWriteableFileName(pDiasdemVolumeFileName)) {
      FileName = pDiasdemVolumeFileName;
      DiasdemDocuments = new ArrayList();
      this.loadXml(FileName);
    }
    else {
      throw new DiasdemException("Error: The DefaultDIAsDEMvolume cannot "
      + " be closed, because FileName == null or FileName is not valid!");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void close() throws DiasdemException {
    
    if (FileName != null && Tools.isValidandWriteableFileName(FileName)) {
      this.writeXml(FileName);
    }
    else {
      throw new DiasdemException("Error: The DefaultDIAsDEMvolume cannot "
      + " be closed, because FileName == null or FileName is not valid!");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addDocument(DIAsDEMdocument pDiasdemDocument) {
    
    if (pDiasdemDocument == null) {
      return -1;
    }
    else {
      int newDocumentIndex = this.getNumberOfDocuments();
      pDiasdemDocument.setDiasdemDocumentID(pDiasdemDocument
      .getDiasdemDocumentID() + ":" + newDocumentIndex);
      if (DiasdemDocuments == null) {
        DiasdemDocuments = new ArrayList();
      }
      DiasdemDocuments.add(pDiasdemDocument);
      return newDocumentIndex;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceDocument(int pDiasdemDocumentIndex,
  DIAsDEMdocument pDiasdemDocument) {
    
    if (pDiasdemDocument == null || DiasdemDocuments == null
    || pDiasdemDocumentIndex >= this.getNumberOfDocuments()
    || pDiasdemDocumentIndex < 0) {
      System.err.println("[DefaultDIAsDEMvolume] DIAsDEMdocument cannot "
      + "be replaced at index " + pDiasdemDocumentIndex + "!");
    }
    else {
      DiasdemDocuments.set(pDiasdemDocumentIndex, pDiasdemDocument);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMdocument getDocument(int pDiasdemDocumentIndex) {
    
    if (DiasdemDocuments == null || pDiasdemDocumentIndex < 0
    || pDiasdemDocumentIndex >= this.getNumberOfDocuments()) {
      return null;
    }
    else {
      return (DIAsDEMdocument)DiasdemDocuments.get(pDiasdemDocumentIndex);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void loadXml(String pXmlFileName) throws DiasdemException {
    
    if (pXmlFileName == null
    || !Tools.isValidandWriteableFileName(pXmlFileName)) {
      throw new DiasdemException("Error: The DefaultDIAsDEMvolume cannot be "
      + "loaded from XML file " + pXmlFileName + "!");
    }
    
    // assuming that pJDomElement has been validated agianst DTD
    
    Document jDomDocument = null;
    Element rootJdomElement = null;
    List jDomElementList = null;
    Element jDomElement = null;
    File xmlFile = null;
    SAXBuilder jDomSaxBuilder = null;
    try {
      xmlFile = new File(pXmlFileName);
      jDomSaxBuilder = new SAXBuilder();
      jDomSaxBuilder.setValidation(true);
      jDomDocument = jDomSaxBuilder.build(xmlFile);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new DiasdemException("Error: The DefaultDIAsDEMvolume cannot be "
      + "loaded from XML file " + pXmlFileName + "! Message: "
      + e.getMessage());
    }
    
    rootJdomElement = jDomDocument.getRootElement();
    jDomElementList = rootJdomElement.getChildren(DTD_DOCUMENT_ELEMENT);
    if (jDomElementList != null) {
      DiasdemDocuments = new ArrayList();
      DefaultDIAsDEMdocument document = null;
      Iterator iterator = jDomElementList.iterator();
      while (iterator.hasNext()) {
        jDomElement = (Element)iterator.next();
        document = new DefaultDIAsDEMdocument();
        document.setFromJDomElement(jDomElement);
        DiasdemDocuments.add(document);
      }
    }
    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void writeXml(String pXmlFileName) throws DiasdemException {
    
    Element rootJdomElement = new Element(DTD_ROOT_ELEMENT);
    Document jdomDocument = new Document(rootJdomElement,
    new DocType(DTD_ROOT_ELEMENT, null, DTD_FILE_NAME));
    
    rootJdomElement.setAttribute("NumberOfDocuments",
    Tools.int2String(this.getNumberOfDocuments()));
    
    Element documentJdomElement = null;
    DefaultDIAsDEMdocument document = null;
    Iterator iterator = DiasdemDocuments.iterator();
    while (iterator.hasNext()) {
      document = (DefaultDIAsDEMdocument)iterator.next();
      documentJdomElement = document.getAsJDomElement();
      if (documentJdomElement != null) {
        rootJdomElement.addContent(documentJdomElement);
      }
    }
    
    XMLOutputter xmlOutputter = null;
    FileOutputStream fileOutputStream = null;
    Format xmlFormat = Format
    .getPrettyFormat();
    xmlFormat.setEncoding(DTD_ENCODING);
    try {
      xmlOutputter = new XMLOutputter(xmlFormat);
      fileOutputStream = new FileOutputStream(pXmlFileName);
      xmlOutputter.output(jdomDocument, fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new DiasdemException("Error: The DefaultDIAsDEMvolume cannot be "
      + "saved as XML file " + pXmlFileName + "! Message: " + e.getMessage());
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void copyDtdFile(String pDirectory) {
    
    TextFile textFile = null;
    if (pDirectory.endsWith(File.separator)) {
      textFile = new TextFile(new File(pDirectory + DTD_FILE_NAME));
    }
    else {
      textFile = new TextFile(new File(pDirectory + File.separator
      + DTD_FILE_NAME));
    }
    textFile.open();
    textFile.setFirstLine(
    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
    + "\n"
    + "<!-- DIAsDEM Workbench 2.1, 15 August 2003, kwinkler -->\n"
    + "\n"
    + "<!ELEMENT DefaultDIAsDEMvolume (\n"
    + "  DefaultDIAsDEMdocument* )\n"
    + ">\n"
    + "\n"
    + "<!ATTLIST DefaultDIAsDEMvolume\n"
    + "  NumberOfDocuments CDATA #IMPLIED\n"
    + ">\n"
    + "\n"
    + "<!ELEMENT DefaultDIAsDEMdocument ( \n"
    + "  MetaData*, \n"
    + "  OriginalText,\n"
    + "  TextUnitsLayer* )\n"
    + ">\n"
    + "\n"
    + "<!ATTLIST DefaultDIAsDEMdocument\n"
    + "  NumberOfTextUnitsLayers CDATA #IMPLIED\n"
    + ">\n"
    + "\n"
    + "<!ELEMENT MetaData (\n"
    + "  Name, \n"
    + "  Content )\n"
    + ">\n"
    + "<!ELEMENT OriginalText (#PCDATA)>\n"
    + "<!ELEMENT TextUnitsLayer ( \n"
    + "  MetaData*, \n"
    + "  OriginalTextUnits, \n"
    + "  ProcessedTextUnits?,\n"
    + "  RollbackTextUnits*,\n"
    + "  NamedEntities? )\n"
    + ">\n"
    + "\n"
    + "<!ATTLIST TextUnitsLayer\n"
    + "  TextUnitsLayerID CDATA #IMPLIED\n"
    + "  TextUnitsDescription CDATA #IMPLIED\n"
    + ">\n"
    + "\n"
    + "<!ELEMENT Name (#PCDATA)>\n"
    + "<!ELEMENT Content (#PCDATA)>\n"
    + "<!ELEMENT OriginalTextUnits (OriginalTextUnit+)>\n"
    + "<!ELEMENT ProcessedTextUnits (ProcessedTextUnit+)>\n"
    + "<!ELEMENT RollbackTextUnits (ProcessedTextUnit+)>\n"
    + "<!ELEMENT NamedEntities (NamedEntity+)>\n"
    + "\n"
    + "<!ATTLIST RollbackTextUnits\n"
    + "  RollbackID CDATA #IMPLIED\n"
    + ">\n"
    + "\n"
    + "<!ELEMENT OriginalTextUnit (#PCDATA)>\n"
    + "<!ELEMENT ProcessedTextUnit (#PCDATA | NeRef)*>\n"
    + "<!ELEMENT RollbackTextUnit (#PCDATA | NeRef)*>\n"
    + "<!ELEMENT NamedEntity (#PCDATA)>\n"
    + "\n"
    + "<!ATTLIST OriginalTextUnit \n"
    + "  TextUnitID CDATA #IMPLIED\n"
    + "  BeginIndex CDATA #IMPLIED\n"
    + "  EndIndex CDATA #IMPLIED\n"
    + ">\n"
    + "\n"
    + "<!ATTLIST ProcessedTextUnit\n"
    + "  TextUnitID CDATA #IMPLIED\n"
    + "  Iteration CDATA #IMPLIED\n"
    + "  ClusterID CDATA #IMPLIED\n"
    + "  ClusterLabel CDATA #IMPLIED\n"
    + ">\n"
    + "\n"
    + "<!ATTLIST NamedEntity \n"
    + "  NeID CDATA #IMPLIED\n"
    + "  NeType CDATA #IMPLIED\n"
    + ">\n"
    + "\n"
    + "<!ELEMENT NeRef EMPTY>\n"
    + "\n"
    + "<!ATTLIST NeRef\n"
    + "  NeID CDATA #IMPLIED\n"
    + ">");
    textFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}