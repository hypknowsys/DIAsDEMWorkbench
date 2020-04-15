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

package org.hypknowsys.wum.client.gui.tools.showPages;

import javax.swing.table.*;
import javax.swing.event.*;
import org.hypknowsys.wum.core.*; 
import org.hypknowsys.wum.core.default10.*;

/**
  * Created by kwinkler, 8/2000: interface TableModel implemented
  *
  * @version 0.7.0, 17 Nov 2001
  * @author Karsten Winkler
  */

public class PagesTableModel extends AbstractTableModel {

  // ########## attributes ##########

  private MiningBase MyMiningBase = null;
  private long[] RowIDs = null;

  public final static int ORDER_BY_UNKNOWN = 0;
  public final static int ORDER_BY_ID = 1;
  public final static int ORDER_BY_URL = 2;
  
  // ########## constructors ##########
  
/**
 * constructs a VisitorsTableModel
 */
  
  public PagesTableModel() {
  
    throw new NumberFormatException();

  }

  public PagesTableModel(MiningBase pMyMiningBase, int pOrder) {

    MyMiningBase = pMyMiningBase;
    Page page = null;

    RowIDs = new long[ MyMiningBase.getWebSitePages().countAll() ];
    if (pOrder == ORDER_BY_URL)
      page = MyMiningBase.getWebSitePages().getFirstByUrl();
    else
      page = MyMiningBase.getWebSitePages().getFirstByID();
    int counter = 0;
    while (page != null) {
      RowIDs[counter++] = page.getID(); 
      if (pOrder == ORDER_BY_URL)
        page = MyMiningBase.getWebSitePages().getNextByUrl();
      else
        page = MyMiningBase.getWebSitePages().getNextByID();
    }
  
  }

  public void sort(int pOrder) {

    Page page = null;
    RowIDs = new long[ MyMiningBase.getWebSitePages().countAll() ];
    if (pOrder == ORDER_BY_URL)
      page = MyMiningBase.getWebSitePages().getFirstByUrl();
    else
      page = MyMiningBase.getWebSitePages().getFirstByID();
    int counter = 0;
    while (page != null) {
      RowIDs[counter++] = page.getID(); 
      if (pOrder == ORDER_BY_URL)
        page = MyMiningBase.getWebSitePages().getNextByUrl();
      else
        page = MyMiningBase.getWebSitePages().getNextByID();
    }

  }

  public int getPreferredColumnWidth(int columnIndex) {

    switch (columnIndex) {
      case 0: { return 50; }
      case 1: { return 300; }
      case 2: { return 50; }
    }
    return 0;

  }

  public Page getPage(int rowIndex) {

    if ( (rowIndex >= 0) && (rowIndex < RowIDs.length) )
      return MyMiningBase.getWebSitePages().get( RowIDs[rowIndex] );
    else
      return null;

  }

  // TableModel

  public int getRowCount() { 
    
    return MyMiningBase.getWebSitePages().countAll(); 
  
  }

  public int getColumnCount() { return 3; }

  public String getColumnName(int columnIndex) {

    switch (columnIndex) {
      case 0: { return "ID"; }
      case 1: { return "URL"; }
      case 2: { return "Accesses"; }
    }
    return null;

  } 
  
  public Class getColumnClass(int columnIndex) {

    switch (columnIndex) {
      case 0: { return ( new String("Class") ).getClass(); }
      case 1: { return ( new String("Class") ).getClass(); }
      case 2: { return ( new Integer(1) ).getClass(); }
    }
    return null;

  }  

  public boolean isCellEditable(int rowIndex, int columnIndex) {

    return false;

  }  

  public Object getValueAt(int rowIndex, int columnIndex) {

    switch (columnIndex) {
      case 0: { 
        return new Long( MyMiningBase.getWebSitePages().get(
          RowIDs[rowIndex] ).getID() ).toString(); }
      case 1: { 
        return MyMiningBase.getWebSitePages().get(
        RowIDs[rowIndex] ).getUrl(); }
      case 2: { 
        return new Integer( MyMiningBase.getWebSitePages().get(
          RowIDs[rowIndex] ).getAccesses() ); }
    }
    return null; 

  }  // getValueAt()

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

}