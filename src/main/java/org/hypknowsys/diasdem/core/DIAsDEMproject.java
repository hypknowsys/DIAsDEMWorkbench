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

import org.hypknowsys.core.Project;
import org.hypknowsys.misc.swing.KFileFilter;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface DIAsDEMproject extends Project {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final String PROJECT_FILE_EXTENSION = ".dpr";
  public static final KFileFilter PROJECT_FILE_FILTER =
  new KFileFilter(PROJECT_FILE_EXTENSION,
  "DIAsDEM Project Files (*" + PROJECT_FILE_EXTENSION + ")");
  
  public static final String SCRIPT_FILE_EXTENSION = ".dsc";
  public static final KFileFilter SCRIPT_FILE_FILTER =
  new KFileFilter(SCRIPT_FILE_EXTENSION,
  "DIAsDEM Script Files (*" + SCRIPT_FILE_EXTENSION + ")");
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getDefaultDiasdemCollectionClassName();
  public int getProcessedTextUnitsRollbackOption();
  public int getActiveTextUnitsLayerIndex();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setDefaultDiasdemCollectionClassName(
  String pDefaultDiasdemCollectionClassName);
  public void setProcessedTextUnitsRollbackOption(
  int pProcessedTextUnitsRollbackOption);
  public void setActiveTextUnitsLayerIndex(
  int pActiveTextUnitsLayerIndex);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMcollection instantiateDefaultDiasdemCollection()
  throws DiasdemException;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}