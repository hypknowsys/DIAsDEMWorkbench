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

import java.util.HashMap;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface DIAsDEMdocument {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public HashMap getMetaData() ;
  public String getOriginalText();
  public String getDiasdemDocumentID();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setMetaData(HashMap pMetaData) ;
  public void setOriginalText(String pOriginalText);
  public void setDiasdemDocumentID(String pDiasdemDocumentID);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void dumpAsXmlFile(String pFileName);
  
  public String dumpAsXmlFile(String pDirectory, boolean pDumpOnlyOnce);
  
  public int getNumberOfTextUnitsLayers();
  
  public int getActiveTextUnitsLayerIndex();
  
  public void setActiveTextUnitsLayerIndex(int pActiveTextUnitsLayerIndex);
  
  public DIAsDEMtextUnitsLayer getActiveTextUnitsLayer();
  
  public void setActiveTextUnitsLayer(int pActiveTextUnitsLayerIndex);
  
  public int createTextUnitsLayer(String pTextUnitsDescription,
  boolean pIsActiveLayer);
  
  public int resetActiveTextUnitsLayer(String pTextUnitsDescription);
  
  public int addTextUnitsLayer(DIAsDEMtextUnitsLayer pTextUnitsLayer);
  
  public void replaceTextUnitsLayer(int pTextUnitsLayerIndex,
  DIAsDEMtextUnitsLayer pTextUnitsLayer);
  
  public DIAsDEMtextUnitsLayer getTextUnitsLayer(
  int pTextUnitsLayerIndex);
  
  public void deleteTextUnitsLayer(int pTextUnitsLayerIndex);
  
  public int getNumberOfOriginalTextUnits();
  
  public int addOriginalTextUnit(DIAsDEMtextUnit pDiasdemTextUnit,
  boolean pAddProcessedTextUnitAsWell);
  
  public void replaceOriginalTextUnit(int pDiasdemTextUnitIndex,
  DIAsDEMtextUnit pDiasdemTextUnit);
  
  public DIAsDEMtextUnit getOriginalTextUnit(int pTextUnitIndex);
  
  public void resetOriginalTextUnits();
  
  public int getNumberOfProcessedTextUnits();
  
  public int addProcessedTextUnit(DIAsDEMtextUnit pDiasdemTextUnit);
  
  public void replaceProcessedTextUnit(int pDiasdemTextUnitIndex,
  DIAsDEMtextUnit pDiasdemTextUnit);
  
  public DIAsDEMtextUnit getProcessedTextUnit(int pTextUnitIndex);
  
  public void resetProcessedTextUnits();
  
  public void backupProcessedTextUnits(int pProcessedTextUnitsRollbackOption);
  
  public void rollbackProcessedTextUnits(int pRollbackTextUnitsID);
  
  public int getMaxRollbackTextUnitsID();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}