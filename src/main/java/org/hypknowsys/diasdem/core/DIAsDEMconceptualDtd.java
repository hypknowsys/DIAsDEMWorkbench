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

package org.hypknowsys.diasdem.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.jdom.Element;

/**
 * @version 2.2, 28 February 2005
 * @author Karsten Winkler
 */

public interface DIAsDEMconceptualDtd extends Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getConceptualDtdID();
  public String getConceptualDtdRemarks();
  public String getTrainingCollectionFileName();
  public String getElementsFileName();
  public String getAttributesFileName();
  public String getXmlFileName();
  public String getDtdDocumentationFileName();
  public long getNumberOfDocuments();
  public long getNumberOfTextUnits();
  public long getNumberOfTaggedTextUnits();
  public long getNumberOfUntaggedTextUnits() ;
  public String getRootElement();
  public double getMinAttributeRelSupport();
  public TreeMap getElements();
  
  public String getEncodingTag();
  public String getDoctypeTag();
  public String getStartRootElementTag();
  public String getEndRootElementTag();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setConceptualDtdID(String pConceptualDtdID);
  public void setConceptualDtdRemarks(String pConceptualDtdRemarks);
  public void setTrainingCollectionFileName(String pCollectionFileName);
  public void setElementsFileName(String pElementsFileName);
  public void setAttributesFileName(String pAttributesFileName);
  public void setXmlFileName(String pXmlFileName);
  public void setDtdDocumentationFileName(String pDtdDocumentationFileName);
  public void setNumberOfDocuments(long pNumberOfDocuments);
  public void setNumberOfTextUnits(long pNumberOfTextUnits);
  public void setNumberOfTaggedTextUnits(long pNumberOfTaggedTextUnits);
  public void setNumberOfUntaggedTextUnits(long pNumberOfUntaggedTextUnits);
  public void setRootElement(String pRootElement);
  public void setMinAttributeRelSupport(double pMinAttributeRelSupport);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addOrUpdateElement(String pName);
  
  public void addOrUpdateElementAttributes(String pElementName,
  NamedEntity[] pNamedEntities);
  
  public DIAsDEMconceptualDtdElement getElement(String pName);
  
  public DIAsDEMconceptualDtdElement[] getAllElements();
  
  public String getElementAttributesString(String pElementName,
  NamedEntity[] pNamedEntities, double pMinimumRelativeSupport);
  
  public NamedEntity[] getValidElementAttributes(String pElementName,
  NamedEntity[] pNamedEntities);
  
  public void computeRelativeSupportOfElements(long pNumberOfTextUnits);
  
  public void save() throws IOException;
  
  public void writeXmlRepresentation(String pDirectory);
  
  public void writeDtdDocumentation(String pFileName);
  
  public TreeSet getElementNames();
  
  public Iterator getElementIterator();
  
  public TreeSet getElementAttributesNames(String pElementName,
  double pMinimumRelativeSupport);
  
  public String[] getElementAttributesNames(String pElementName);
  
  public Element getDocumentAsJDomElement(DIAsDEMproject pDiasdemProject,
  DIAsDEMdocument pDiasdemDocument);
  
  public void dumpDocumentAsXmlFile(DIAsDEMproject pDiasdemProject,
  DIAsDEMdocument pDiasdemDocument, String pFileName, boolean pCopyDtdFile);
  
  public void addHtmlExample(DIAsDEMproject pDiasdemProject,
  DIAsDEMdocument pDiasdemDocument, int pTextUnitID);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}