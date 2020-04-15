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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface DIAsDEMcollection {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getClassName();
  public String getCollectionName();
  public String getCollectionFileName();
  public String getCollectionNotes();
  public long getNumberOfDocuments();
  public long getNumberOfTextUnits();
  public long getNumberOfUntaggedTextUnits();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCollectionName(String pCollectionName);
  public void setCollectionFileName(String pCollectionFileName);
  public void setCollectionNotes(String pCollectionNotes);
  public void setNumberOfDocuments(long pNumberOfDocuments);
  public void setNumberOfTextUnits(long pNumberOfTextUnits);
  public void setNumberOfUntaggedTextUnits(long pNumberOfTextUnits);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void create(String pDiasdemCollectionFileName)
  throws DiasdemException;
  
  public void open(String pDiasdemCollectionFileName)
  throws DiasdemException;
  
  public void close() throws DiasdemException;
  
  public String addDocument(DIAsDEMdocument pDiasdemDocument);
  
  public void replaceDocument(String pDiasdemDocumentID,
  DIAsDEMdocument pDiasdemDocument);
  
  public DIAsDEMdocument getFirstDocument();
  
  public DIAsDEMdocument getNextDocument();
  
  public DIAsDEMdocument getDocument(String pDiasdemDocumentID);
  
  public DIAsDEMdocument instantiateDefaultDiasdemDocument();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}