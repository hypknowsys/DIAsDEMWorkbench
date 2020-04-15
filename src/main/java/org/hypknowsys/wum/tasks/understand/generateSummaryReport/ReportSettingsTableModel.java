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

package org.hypknowsys.wum.tasks.understand.generateSummaryReport;

import javax.swing.table.*;
import javax.swing.event.*;
import org.hypknowsys.misc.util.*;

/**
  * Created by kwinkler, 8/2000: interface TableModel implemented
  *
  * @see wum.objects.Page
  * @version 6.0, 14 Nov 2000
  * @author Karsten Winkler
  */

public class ReportSettingsTableModel extends AbstractTableModel {

  // ########## attributes ##########
  
  private GenerateSummaryReportParameter Parameter = null;

  public final static int NUMBER_OF_SETTINGS = 13;

  public final static int MOST_REQUESTED_PAGES = 0;
  public final static int MOST_REQUESTED_DIRECTORIES = 1;
  public final static int LEAST_REQUESTED_PAGES = 2;
  public final static int LEAST_REQUESTED_DIRECTORIES = 3;
  public final static int TOP_ENTRY_PAGES = 4;
  public final static int TOP_EXIT_PAGES = 5;
  public final static int SINGLE_ACCESS_PAGES = 6;
  public final static int MOST_ACTIVE_TOP_LEVEL_DOMAINS = 7;
  public final static int MOST_ACTIVE_2ND_LEVEL_DOMAINS = 8;
  public final static int TOP_VISITORS = 9;
  public final static int TOP_REFERRER_SITES = 10;
  public final static int TOP_REFERRER_PAGES = 11;
  public final static int MOST_USED_BROWSERS = 12;

  public final static String[] NAMES_OF_SETTINGS = {
    "Most Requested Pages",    
    "Most Requested Directories",    
    "Least Requested Pages",    
    "Least Requested Directories",    
    "Top Entry Pages",    
    "Top Exit Pages",    
    "Single Access Pages",    
    "Most Active Top-Level Domains",    
    "Most Active 2nd-Level Domains",    
    "Top Visitors",    
    "Top Referrer Sites",    
    "Top Referrer Pages",    
    "Most Used Browsers",    
  };
  
  // ########## constructors ##########
  
/**
 * constructs a ReportSettingsTableModel
 */
  
  public ReportSettingsTableModel(GenerateSummaryReportParameter pParameter) {

    Parameter = pParameter;
  
  }

  public int getPreferredColumnWidth(int columnIndex) {

    switch (columnIndex) {
      case 0: { return 300; }
      case 1: { return 50; }
    }
    return 0;

  }

  public int getRowCount() { return NUMBER_OF_SETTINGS; }

  public int getColumnCount() { return 2; }

  public String getColumnName(int columnIndex) {

    switch (columnIndex) {
      case 0: { return "Report Section"; }
      case 1: { return "Size"; }
    }
    return null;

  }  // getColumnName()

  public Class getColumnClass(int columnIndex) {

    switch (columnIndex) {
      case 0: { return ( new String("Class") ).getClass(); }
      case 1: { return ( new String("Class") ).getClass(); }
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
        return NAMES_OF_SETTINGS[rowIndex]; }
      case 1: { 
        switch (rowIndex) {
          case MOST_REQUESTED_PAGES: { 
            return Tools.int2String(
            Parameter.getNumberOfMostRequestedPages());
          }
          case MOST_REQUESTED_DIRECTORIES: { 
            return  Tools.int2String(
            Parameter.getNumberOfMostRequestedDirectories());
          }
          case LEAST_REQUESTED_PAGES: { 
            return  Tools.int2String(
            Parameter.getNumberOfLeastRequestedPages());
          }
          case LEAST_REQUESTED_DIRECTORIES: { 
            return  Tools.int2String(
            Parameter.getNumberOfLeastRequestedDirectories());
          }
          case TOP_ENTRY_PAGES: { 
            return  Tools.int2String(
            Parameter.getNumberOfTopEntryPages());
          }
          case TOP_EXIT_PAGES: { 
            return  Tools.int2String(
            Parameter.getNumberOfTopExitPages());
          }
          case SINGLE_ACCESS_PAGES: { 
            return  Tools.int2String(
            Parameter.getNumberOfSingleAccessPages());
          }
          case MOST_ACTIVE_TOP_LEVEL_DOMAINS: { 
            return  Tools.int2String(
            Parameter.getNumberOfMostActiveTopLevelDomains());
          }
          case MOST_ACTIVE_2ND_LEVEL_DOMAINS: { 
            return  Tools.int2String(
            Parameter.getNumberOfMostActiveSecondLevelDomains());
          }
          case TOP_VISITORS: { 
            return  Tools.int2String(
            Parameter.getNumberOfTopVisitors());
          }
          case TOP_REFERRER_SITES: { 
            return  Tools.int2String(
            Parameter.getNumberOfTopReferrerSites());
          }
          case TOP_REFERRER_PAGES: { 
            return  Tools.int2String(
            Parameter.getNumberOfTopReferrerPages());
          }
          case MOST_USED_BROWSERS: { 
            return  Tools.int2String(
            Parameter.getNumberOfMostUsedBrowsers());
          }
        }
      }
    }
    return null; 

  }  // getValueAt()

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    switch (columnIndex) {
      case 0: { 
        break; }
      case 1: { 
        switch (rowIndex) {
          case MOST_REQUESTED_PAGES: { 
            Parameter.setNumberOfMostRequestedPages(
            Tools.string2Int((String)aValue));
            break;
          }
          case MOST_REQUESTED_DIRECTORIES: { 
            Parameter.setNumberOfMostRequestedDirectories(
            Tools.string2Int((String)aValue));
            break;
          }
          case LEAST_REQUESTED_PAGES: { 
            Parameter.setNumberOfLeastRequestedPages(
            Tools.string2Int((String)aValue));
            break;
          }
          case LEAST_REQUESTED_DIRECTORIES: { 
            Parameter.setNumberOfLeastRequestedDirectories(
            Tools.string2Int((String)aValue));
            break;
          }
          case TOP_ENTRY_PAGES: { 
            Parameter.setNumberOfTopEntryPages(
            Tools.string2Int((String)aValue));
            break;
          }
          case TOP_EXIT_PAGES: { 
            Parameter.setNumberOfTopExitPages(
            Tools.string2Int((String)aValue));
            break;
          }
          case SINGLE_ACCESS_PAGES: { 
            Parameter.setNumberOfSingleAccessPages(
            Tools.string2Int((String)aValue));
            break;
          }
          case MOST_ACTIVE_TOP_LEVEL_DOMAINS: { 
            Parameter.setNumberOfMostActiveTopLevelDomains(
            Tools.string2Int((String)aValue));
            break;
          }
          case MOST_ACTIVE_2ND_LEVEL_DOMAINS: { 
            Parameter.setNumberOfMostActiveSecondLevelDomains(
            Tools.string2Int((String)aValue));
            break;
          }
          case TOP_VISITORS: { 
            Parameter.setNumberOfTopVisitors(
            Tools.string2Int((String)aValue));
            break;
          }
          case TOP_REFERRER_SITES: { 
            Parameter.setNumberOfTopReferrerSites(
            Tools.string2Int((String)aValue));
            break;
          }
          case TOP_REFERRER_PAGES: { 
            Parameter.setNumberOfTopReferrerPages(
            Tools.string2Int((String)aValue));
            break;
          }
          case MOST_USED_BROWSERS: { 
            Parameter.setNumberOfMostUsedBrowsers(
            Tools.string2Int((String)aValue));
            break;
          }
        }
      }
    }

  }

}