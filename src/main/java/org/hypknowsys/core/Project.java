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

package org.hypknowsys.core;

import java.io.IOException;
import org.hypknowsys.misc.util.KProperty;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface Project {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getProjectName();
  
  public String getProjectNotes();
  
  public String getProjectFileName();
  
  public String getProjectDirectory();
  
  public String getParameterDirectory();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setProjectName(String pProjectName);
  
  public void setProjectNotes(String pProjectMemos);
  
  public void setProjectFileName(String pProjectFileName);
  
  public void setProjectDirectory(String pProjectDirectory);
  
  public void setParameterDirectory(String pParameterDirectory);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String[] getPropertyKeys();
  
  public void save(String pFileName) throws IOException;
  
  public void save() throws IOException;
  
  public void quickSave();
  
  public void load(String pFileName) throws IOException;
  
  public void quickLoad(String pFileName);
  
  public void registerButDontReplaceKProperties(KProperty[] pPropertyDetails);
  
  public String getProperty(String pPropertyKey);
  
  public String getPropertyDescription(String pPropertyKey);
  
  public int getPropertyType(String pPropertyKey);
  
  public boolean isEditableInTable(String pPropertyKey);
  
  public int countProperties();
  
  public String getStringProperty(String pPropertyKey);
  
  public int getIntProperty(String pPropertyKey);
  
  public long getLongProperty(String pPropertyKey);
  
  public double getDoubleProperty(String pPropertyKey);
  
  public boolean getBooleanProperty(String pPropertyKey);
  
  public void setProperty(String pPropertyKey, String pValue);
  
  public void setStringProperty(String pPropertyKey, String pValue);
  
  public void setIntProperty(String pPropertyKey, int pValue);
  
  public void setLongProperty(String pPropertyKey, long pValue);
  
  public void setDoubleProperty(String pPropertyKey, double pValue);
  
  public void setBooleanProperty(String pPropertyKey, boolean pValue);
  
  public boolean isValidProperty(String pPropertyKey, String pValue);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}