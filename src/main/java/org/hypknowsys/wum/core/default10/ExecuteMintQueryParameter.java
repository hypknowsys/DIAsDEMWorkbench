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

package org.hypknowsys.wum.core.default10;

/**
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class ExecuteMintQueryParameter {

  // ########## attributes  ##########

  private String QueryString = null;
  private int AggregatedLogID = 0;

  // ########## constructors ##########
  
  public ExecuteMintQueryParameter() {

    QueryString = null;
    AggregatedLogID = 0;

  }

  public ExecuteMintQueryParameter(
    String pQueryString, int pAggregatedLogID) {

    QueryString = pQueryString; 
    AggregatedLogID = pAggregatedLogID; 

  }

  // ########## accessor methods ##########
  
  public String getQueryString() { 
    return QueryString; }
  public int getAggregatedLogID() { 
    return AggregatedLogID; }

  // ########## mutator methods ##########
  
  public void setQueryString(String pQueryString) { 
    QueryString = pQueryString; }
  public void setAggregatedLogID(int pAggregatedLogID) { 
    AggregatedLogID = pAggregatedLogID; }

}