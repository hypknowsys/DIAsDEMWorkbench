/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.client.gui.tools.options;

import javax.swing.table.*;
import javax.swing.event.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.core.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.server.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;
import org.hypknowsys.wum.server.*;
import org.hypknowsys.wum.client.gui.*;

/**
  * Created by kwinkler, 1/2001: interface TableModel implemented
  *
  * @version 1.0, 17 Jan 2002
  * @author Karsten Winkler
  */

public class GuiPreferencesTableModel extends AbstractTableModel {

  // ########## attributes ##########
  
  private Server WumServer = null;
  private Project WumProject = null;
  private GuiClient WumGui = null;
  private GuiClientPreferences WumGuiPreferences = null;

  private int NumberOfRows = 0;
  private String[] RowKeyMappings = null;
  private String[] GuiProperties = null;
  private String[] Values = null;
  private Class StringClass = null;
  
  // ########## constructors ##########
  
/**
 * constructs a GuiPreferencesTableModel
 */
  
  public GuiPreferencesTableModel() {
  
    throw new NumberFormatException();

  }

  public GuiPreferencesTableModel(Server pWumServer, Project pWumProject,
  GuiClient pWumGui, GuiClientPreferences pWumGuiPreferences) {

    if (pWumGuiPreferences == null) {
      throw new NumberFormatException();
    }
    WumServer = pWumServer;
    WumProject = pWumProject;
    WumGui = pWumGui;
    WumGuiPreferences = pWumGuiPreferences;

    StringClass = ( new String("Class") ).getClass();
    NumberOfRows = 0;
    String[] allKeys = WumGuiPreferences.getPropertyKeys();
    for (int i = 0; allKeys != null && i < allKeys.length; i++) {
      // true: property is editable in Swing table
      if ( WumGuiPreferences.isEditableInTable(allKeys[i]) ) {
        NumberOfRows++;
      }
    }
    RowKeyMappings = new String[NumberOfRows];
    GuiProperties = new String[NumberOfRows];
    Values = new String[NumberOfRows];
    int currentRow = 0;
    for (int i = 0; allKeys != null && i < allKeys.length; i++) {
      // true: property is editable in Swing table
      if ( WumGuiPreferences.isEditableInTable(allKeys[i]) ) {
        RowKeyMappings[currentRow] = allKeys[i];
        GuiProperties[currentRow] = WumGuiPreferences
        .getPropertyDescription(allKeys[i]);
        Values[currentRow++] = WumGuiPreferences.getProperty(allKeys[i]);
      }
    }

  }

  public int getPreferredColumnWidth(int columnIndex) {

    switch (columnIndex) {
      case 0: { return 400; }
      case 1: { return 100; }
    }
    return 0;

  }

  public GuiClientPreferences getGuiPreferences() {

    return WumGuiPreferences;

  }

  public String getGuiProperty(int rowIndex) {

    if ( (rowIndex >= 0) && (rowIndex < NumberOfRows) )
      return GuiProperties[rowIndex];
    else
      return null; 

  }

  public int getGuiPropertyType(int rowIndex) {

    if (RowKeyMappings != null && rowIndex >= 0 && rowIndex < NumberOfRows)
      return WumGuiPreferences.getPropertyType(RowKeyMappings[rowIndex]);
    else
      return KProperty.STRING; 

  }

  public String getValue(int rowIndex) {

    if ( (rowIndex >= 0) && (rowIndex < NumberOfRows) )
      return Values[rowIndex];
    else
      return null; 

  }

  public boolean isValidValue(int rowIndex) {

    if ( (rowIndex >= 0) && (rowIndex < NumberOfRows) )
      return WumGuiPreferences.isValidProperty(
        RowKeyMappings[rowIndex], Values[rowIndex] );
    else
      return false; 

  }

  public GuiClientPreferences commit() {

    for (int i = 0; i < NumberOfRows; i++)
      if ( this.isValidValue(i) )
        WumGuiPreferences.setProperty( RowKeyMappings[i], Values[i]);

    return WumGuiPreferences;

  }

  // TableModel

  public int getRowCount() { return NumberOfRows; }

  public int getColumnCount() { return 2; }

  public String getColumnName(int columnIndex) {

    switch (columnIndex) {
      case 0: { return "GUI Property"; }
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

    if (columnIndex == 1)
      return true;
    else
      return false;

  }  // isCellEditable()

  public Object getValueAt(int rowIndex, int columnIndex) {

    switch (columnIndex) {
      case 0: { 
        return GuiProperties[rowIndex];
      }
      case 1: { 
        return Values[rowIndex]; 
      }
    }
    return null; 

  }  // getValueAt()

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    switch (columnIndex) {
      case 0: { 
        break; }
      case 1: { 
        Values[rowIndex] = (String)aValue; 
        break; }
    }

  }

}