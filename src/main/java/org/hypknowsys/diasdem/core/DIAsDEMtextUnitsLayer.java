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

import java.util.ArrayList;
import java.util.HashMap;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.hypknowsys.diasdem.core.neex.NamedEntityOwner;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface DIAsDEMtextUnitsLayer extends NamedEntityOwner {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final int ROLLBACK_DISABLED = 0;
  public static final int LIMITED_ROLLBACK_ENABLED = 1;
  public static final int FULL_ROLLBACK_ENABLED = 2;
  public static final String[] ROLLBACK_OPTIONS = {
    "Rollback for ProcessedTextUnits disabled",
    "Limited Rollback for ProcessedTextUnits enabled",
    "Full Rollback for ProcessedTextUnits enabled"
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTextUnitsDescription();
  public HashMap getMetaData();
  public DIAsDEMtextUnits getOriginalTextUnits();
  public DIAsDEMtextUnits getProcessedTextUnits();
  public ArrayList getRollbackTextUnits();
  public ArrayList getNamedEntities();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTextUnitsDescription(String pTextUnitsDescription);
  public void setMetaData(HashMap pMetaData);
  public void setOriginalTextUnits(DIAsDEMtextUnits pOriginalTextUnits);
  public void setProcessedTextUnits(DIAsDEMtextUnits pProcessedTextUnits);
  public void setRollbackTextUnits(ArrayList pRollbackTextUnits);
  public void setNamedEntities(ArrayList pNamedEntities);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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
  
  public int getNumberOfNamedEntities();
  
  public int addNamedEntity(NamedEntity pNamedEntity);
  
  public void replaceNamedEntity(int pNamedEntityIndex,
  NamedEntity pNamedEntity);
  
  public NamedEntity getNamedEntity(int pNamedEntityIndex);
  
  public void resetNamedEntities();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}