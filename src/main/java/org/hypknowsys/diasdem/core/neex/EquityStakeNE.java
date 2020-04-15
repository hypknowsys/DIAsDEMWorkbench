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

public class EquityStakeNE extends CompositeNE {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CompanyName = null;
  protected String NumberOfShares = null;
  protected String PercentageOfShares = null;
  
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
  
  public EquityStakeNE() {
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public EquityStakeNE(String pCompanyName, String pNumberOfShares,
  String pPercentageOfShares) {
    
    this.CompanyName = pCompanyName;
    this.NumberOfShares = pNumberOfShares;
    this.PercentageOfShares = pPercentageOfShares;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public EquityStakeNE(int pID, String pToken, String pPlaceholder,
  int pPossibleType) {
    
    super(pID, pToken, pPlaceholder, pPossibleType);
    this.CompanyName = null;
    this.NumberOfShares = null;
    this.PercentageOfShares = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public EquityStakeNE(int pID, String pToken, int pType, 
  String pCompanyName, String pNumberOfShares, String pPercentageOfShares) {
  
    super(pID, pToken, null, pType, null);
    this.CompanyName = pCompanyName;
    this.NumberOfShares = pNumberOfShares;
    this.PercentageOfShares = pPercentageOfShares;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCompanyName() {
    return CompanyName; }
  public String getNumberOfShares() {
    return NumberOfShares; }
  public String getPercentageOfShares() {
    return PercentageOfShares; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("EquityStakeNE: \"")
    .append(CompanyName).append(", NoS: ").append(NumberOfShares)
    .append(", PoS: ").append(PercentageOfShares).append("\", ID=")
    .append(ID).append(", PH=").append(this.Placeholder).append(", PT=")
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
    if (this.CompanyName != null && this.CompanyName.equals("")) {
      this.CompanyName = null;
    }
    if (this.NumberOfShares != null && this.NumberOfShares.equals("")) {
      this.NumberOfShares = null;
    }
    if (this.PercentageOfShares != null && this.PercentageOfShares.equals("")) {
      this.PercentageOfShares = null;
    }
    
    StringBuffer currentAttributes = new StringBuffer(1000);
    currentAttributes.append(ID).append("|").append(Token).append("|")
    .append(this.getPossibleTypesString()).append("|").append(this.CompanyName)
    .append("|").append(this.NumberOfShares).append("|")
    .append(this.PercentageOfShares);
    
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
          NamedEntity.EQUITY_STAKE])) {
            MostProbableType = NamedEntity.EQUITY_STAKE;
          }
          else {
            MostProbableType = NamedEntity.UNKNOWN;
          }
          break;
        }
        case 3 : {
          this.CompanyName = currentToken;
          break;
        }
        case 4 : {
          this.NumberOfShares = currentToken;
          break;
        }
        case 5 : {
          this.PercentageOfShares = currentToken;
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