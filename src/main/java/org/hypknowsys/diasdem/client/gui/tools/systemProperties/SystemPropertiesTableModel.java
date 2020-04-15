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

package org.hypknowsys.diasdem.client.gui.tools.systemProperties;

import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.diasdem.core.*; import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.server.*;

/**
  * Created by kwinkler, 1/2001: interface TableModel implemented
  *
  * @version 6.0, 18 Jan 2001
  * @author Karsten Winkler
  */

public class SystemPropertiesTableModel extends AbstractTableModel {

  // ########## attributes ##########
  
  private int NumberOfRows = 0;
  private String[] SystemProperties = null;
  private String[] Values = null;
  private Class StringClass = null;
  
  // ########## constructors ##########
  
/**
 * constructs a SystemPropertiesTableModel
 */
  
  public SystemPropertiesTableModel() {

    StringClass = ( new String("Class") ).getClass();
    NumberOfRows = 3;  // memory data
    String propertyName = null;
    Properties properties = System.getProperties();
    Enumeration enumeration = properties.propertyNames();
    while ( enumeration.hasMoreElements() ) {
      propertyName = (String)enumeration.nextElement();
      NumberOfRows++;
    }
    SystemProperties = new String[NumberOfRows];
    Values = new String[NumberOfRows];
    Runtime runtime = Runtime.getRuntime();
    SystemProperties[0] = "Total Memory";
    Values[0] = ( runtime.totalMemory() / 1024 ) + " kByte";
    SystemProperties[1] = "Free Memory before GC";
    Values[1] = ( runtime.freeMemory() / 1024 ) + " kByte";
    System.gc();
    SystemProperties[2] = "Free Memory after GC";
    Values[2] = ( runtime.freeMemory() / 1024 ) + " kByte";

    int currentRow = 3;
    enumeration = properties.propertyNames();
    while ( enumeration.hasMoreElements() ) {
      propertyName = (String)enumeration.nextElement();
      SystemProperties[currentRow] = propertyName;
      Values[currentRow++] = properties.getProperty(propertyName);
    }

  }

  public int getPreferredColumnWidth(int columnIndex) {

    switch (columnIndex) {
      case 0: { return 250; }
      case 1: { return 250; }
    }
    return 0;

  }

  // TableModel

  public int getRowCount() { return NumberOfRows; }

  public int getColumnCount() { return 2; }

  public String getColumnName(int columnIndex) {

    switch (columnIndex) {
      case 0: { return "System Property"; }
      case 1: { return "Value"; }
    }
    return null;

  }  // getColumnName()

  public Class getColumnClass(int columnIndex) {

    switch (columnIndex) {
      case 0: { return StringClass; }
      case 1: { return StringClass; }
    }
    return null;

  }  // getColumnClass()

  public boolean isCellEditable(int rowIndex, int columnIndex) {

    return false;

  }  // isCellEditable()

  public Object getValueAt(int rowIndex, int columnIndex) {

    switch (columnIndex) {
      case 0: { 
        return SystemProperties[rowIndex];
      }
      case 1: { 
        return Values[rowIndex]; 
      }
    }
    return null; 

  }  // getValueAt()

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

}  // class SystemPropertiesTableModel
