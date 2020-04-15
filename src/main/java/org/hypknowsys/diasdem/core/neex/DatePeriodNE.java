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

package org.hypknowsys.diasdem.core.neex;

import java.util.StringTokenizer;

/**
 * @version 2.1.7, 31 March 2005
 * @author Karsten Winkler
 */

public class DatePeriodNE extends CompositeNE {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String PeriodBeginDate = null;
  protected String PeriodEndDate = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DatePeriodNE() {
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DatePeriodNE(String pPeriodBeginDate, String pPeriodEndDate) {
    
    this.PeriodBeginDate = pPeriodBeginDate;
    this.PeriodEndDate = pPeriodEndDate;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DatePeriodNE(int pID, String pToken, String pPlaceholder, 
  int pPossibleType) {
    
    super(pID, pToken, pPlaceholder, pPossibleType);
    this.PeriodBeginDate = null;
    this.PeriodEndDate = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DatePeriodNE(int pID, String pToken, int pType, 
  String pPeriodBeginDate, String pPeriodEndDate) {
  
    super(pID, pToken, null, pType, null);
    this.PeriodBeginDate = pPeriodBeginDate;
    this.PeriodEndDate = pPeriodEndDate;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPeriodBeginDate() {
    return PeriodBeginDate; }
  public String getPeriodEndDate() {
    return PeriodEndDate; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("DatePeriodNE: \"").append(PeriodBeginDate)
    .append("-").append(PeriodEndDate).append("\", ID=").append(ID)
    .append(", PH=").append(this.Placeholder).append(", PT=")
    .append(getPossibleTypesPlaceholder());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getAttributeString() {
    
    if (this.Token != null && this.Token.equals("")) {
      this.Token = null;
    }
    if (this.PeriodBeginDate != null && this.PeriodBeginDate.equals("")) {
      this.PeriodBeginDate = null;
    }
    if (this.PeriodEndDate != null && this.PeriodEndDate.equals("")) {
      this.PeriodEndDate = null;
    }
    
    StringBuffer currentAttributes = new StringBuffer(1000);
    currentAttributes.append(ID).append("|")
    .append(Token).append("|")
    .append(this.getPossibleTypesString()).append("|")
    .append(this.PeriodBeginDate).append("|").append(this.PeriodEndDate);
    
    return currentAttributes.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setAttributeString
  (String pfeature) {
    
    int counter = 0;
    StringTokenizer tokenedFeature = new StringTokenizer(pfeature, "|");
    String currentToken = null;
    
    while (tokenedFeature.hasMoreTokens()) {
      
      currentToken = tokenedFeature.nextToken();
      if (currentToken.equals("null")) {
        currentToken = null;
      }
      
      switch (counter) {
        
        case 0 : {
          Integer currentID = new Integer(currentToken);
          ID =  currentID.intValue();
          break;
        }
        case 1 : {
          Token = currentToken;
          break;
        }
        case 2 : {
          if (currentToken.equals(NamedEntity.TYPE_DESCRIPTIONS[
          NamedEntity.DATE_PERIOD])) {
            MostProbableType = NamedEntity.DATE_PERIOD;
          }
          else {
            MostProbableType = NamedEntity.UNKNOWN;
          }
          break;
        }
        case 3 : {
          this.PeriodBeginDate = currentToken;
          break;
        }
        case 4 : {
          this.PeriodEndDate = currentToken;
          break;
        }
        
      }
      counter ++;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}