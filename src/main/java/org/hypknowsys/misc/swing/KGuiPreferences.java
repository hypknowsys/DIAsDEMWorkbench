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

package org.hypknowsys.misc.swing;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
import java.io.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.util.*;

public interface KGuiPreferences {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public final static String TEXT_FILE_EXTENSION = ".txt";
  public final static KFileFilter TEXT_FILE_FILTER = 
    new KFileFilter(TEXT_FILE_EXTENSION, 
    "Text Files (*" + TEXT_FILE_EXTENSION + ")");

  public final static String CSV_FILE_EXTENSION = ".csv";
  public final static KFileFilter CSV_FILE_FILTER = 
    new KFileFilter(CSV_FILE_EXTENSION, 
    "Comma Separated Values Files (*" + CSV_FILE_EXTENSION + ")");

  public final static String XML_FILE_EXTENSION = ".xml";
  public final static KFileFilter XML_FILE_FILTER = 
    new KFileFilter(XML_FILE_EXTENSION, 
    "XML Files (*" + XML_FILE_EXTENSION + ")");

  public final static String HTML_FILE_EXTENSION = ".html";
  public final static KFileFilter HTML_FILE_FILTER = 
    new KFileFilter(HTML_FILE_EXTENSION, 
    "HTML Files (*" + HTML_FILE_EXTENSION + ")");

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getFileName();

  public String[] getPropertyKeys();
  
  public int getFramePositionX();
  
  public int getFramePositionY();
  
  public int getFrameSizeX();
  
  public int getFrameSizeY();
  
  public int getDialogXxsSizeX();
  
  public int getDialogXxsSizeY();
  
  public int getDialogXsSizeX();
  
  public int getDialogXsSizeY();
  
  public int getDialogSSizeX();
  
  public int getDialogSSizeY();
  
  public int getDialogMSizeX();
  
  public int getDialogMSizeY();
  
  public int getDialogLSizeX();
  
  public int getDialogLSizeY();
  
  public int getDialogXlSizeX();
  
  public int getDialogXlSizeY();
  
  public String getLookAndFeelClassName();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setFramePositionX(int pFramePositionX);
  
  public void setFramePositionY(int pFramePositionY);
  
  public void setLookAndFeelClassName(String pLookAndFeelClassName);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString();

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

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