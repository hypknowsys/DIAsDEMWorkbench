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
 * @author Henner Graubitz, Karsten Winkler
 */

public class UnitOfCompanyNE extends CompositeNE {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String NameOfUnit = null;
  protected String PlaceOfUnitHeadquarter = null;
  protected String StreetOfUnitHeadquarter = null;
  protected String DistrictCourtOfUnit = null;
  protected String CommercialRegisterIDOfUnit = null;
  protected String NameOfParent = null;
  protected String PlaceOfParentHeadquarter = null;
  protected String StreetOfParentHeadquarter = null;
  protected String DistrictCourtOfParent = null;
  protected String CommercialRegisterIDOfParent = null;
  
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
  
  public UnitOfCompanyNE() {
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public UnitOfCompanyNE(String pNameOfUnit, String pDistrictCourtOfUnit,
  String pCommercialRegisterIDOfUnit, String pPlaceOfUnitHeadquarter,
  String pStreetOfUnitHeadquarter, String pNameOfParent,
  String pDistrictCourtOfParent, String pCommercialRegisterIDOfParent,
  String pPlaceOfParentHeadquarter, String pStreetOfParentHeadquarter) {
    
    this.NameOfUnit = pNameOfUnit;
    this.DistrictCourtOfUnit = pDistrictCourtOfUnit;
    this.CommercialRegisterIDOfUnit = pCommercialRegisterIDOfUnit;
    this.PlaceOfUnitHeadquarter = pPlaceOfUnitHeadquarter;
    this.StreetOfUnitHeadquarter = pStreetOfUnitHeadquarter;
    this.NameOfParent = pNameOfParent;
    this.DistrictCourtOfParent = pDistrictCourtOfParent;
    this.CommercialRegisterIDOfParent = pCommercialRegisterIDOfParent;
    this.PlaceOfParentHeadquarter = pPlaceOfParentHeadquarter;
    this.StreetOfParentHeadquarter = pStreetOfParentHeadquarter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public UnitOfCompanyNE(int pID, String pToken, String pPlaceholder,
  int pPossibleType) {
    
    super(pID, pToken, pPlaceholder, pPossibleType);
    this.NameOfUnit = null;
    this.DistrictCourtOfUnit = null;
    this.CommercialRegisterIDOfUnit = null;
    this.PlaceOfUnitHeadquarter = null;
    this.DistrictCourtOfParent = null;
    this.CommercialRegisterIDOfParent = null;
    this.PlaceOfParentHeadquarter = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public UnitOfCompanyNE(int pID, String pToken, int pType,
  String pNameOfUnit, String pDistrictCourtOfUnit,
  String pCommercialRegisterIDOfUnit, String pPlaceOfUnitHeadquarter,
  String pStreetOfUnitHeadquarter, String pNameOfParent,
  String pDistrictCourtOfParent, String pCommercialRegisterIDOfParent,
  String pPlaceOfParentHeadquarter, String pStreetOfParentHeadquarter) {
    
    super(pID, pToken, null, pType, null);
    this.NameOfUnit = pNameOfUnit;
    this.DistrictCourtOfUnit = pDistrictCourtOfUnit;
    this.CommercialRegisterIDOfUnit = pCommercialRegisterIDOfUnit;
    this.PlaceOfUnitHeadquarter = pPlaceOfUnitHeadquarter;
    this.StreetOfUnitHeadquarter = pStreetOfUnitHeadquarter;
    this.NameOfParent = pNameOfParent;
    this.DistrictCourtOfParent = pDistrictCourtOfParent;
    this.CommercialRegisterIDOfParent = pCommercialRegisterIDOfParent;
    this.PlaceOfParentHeadquarter = pPlaceOfParentHeadquarter;
    this.StreetOfParentHeadquarter = pStreetOfParentHeadquarter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getNameOfUnit() {
    return NameOfUnit; }
  public String getPlaceOfUnitHeadquarter() {
    return PlaceOfUnitHeadquarter; }
  public String getStreetOfUnitHeadquarter() {
    return StreetOfUnitHeadquarter; }
  public String getDistrictCourtOfUnit() {
    return DistrictCourtOfUnit; }
  public String getCommercialRegisterIDOfUnit() {
    return CommercialRegisterIDOfUnit; }
  public String getNameOfParent() {
    return NameOfParent; }
  public String getPlaceOfParentHeadquarter() {
    return PlaceOfParentHeadquarter; }
  public String getStreetOfParentHeadquarter() {
    return StreetOfParentHeadquarter; }
  public String getDistrictCourtOfParent() {
    return DistrictCourtOfParent; }
  public String getCommercialRegisterIDOfParent() {
    return CommercialRegisterIDOfParent; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("CompanyRelocationNE: \"").append(NameOfUnit)
    .append(" of ") .append(NameOfParent)
    .append("\", ID=").append(ID).append(", PH=").append(this.Placeholder)
    .append(", PT=").append(getPossibleTypesPlaceholder());
    
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
    if (this.NameOfUnit != null && this.NameOfUnit.equals("")) {
      this.NameOfUnit = null;
    }
    if (this.DistrictCourtOfUnit != null
    && this.DistrictCourtOfUnit.equals("")) {
      this.DistrictCourtOfUnit = null;
    }
    if (this.CommercialRegisterIDOfUnit != null
    &&  this.CommercialRegisterIDOfUnit.equals("")) {
      this.CommercialRegisterIDOfUnit = null;
    }
    if (this.PlaceOfUnitHeadquarter != null
    && this.PlaceOfUnitHeadquarter.equals("")) {
      this.PlaceOfUnitHeadquarter = null;
    }
    if (this.StreetOfUnitHeadquarter != null
    && this.StreetOfUnitHeadquarter.equals("")) {
      this.StreetOfUnitHeadquarter = null;
    }
    if (this.DistrictCourtOfParent != null
    && this.DistrictCourtOfParent.equals("")) {
      this.DistrictCourtOfParent = null;
    }
    if (this.CommercialRegisterIDOfParent != null
    &&  this.CommercialRegisterIDOfParent.equals("")) {
      this.CommercialRegisterIDOfParent = null;
    }
    if (this.NameOfParent != null && this.NameOfParent.equals("")) {
      this.NameOfParent = null;
    }
    if (this.PlaceOfParentHeadquarter != null
    && this.PlaceOfParentHeadquarter.equals("")) {
      this.PlaceOfParentHeadquarter = null;
    }
    if (this.StreetOfParentHeadquarter != null
    && this.StreetOfParentHeadquarter.equals("")) {
      this.StreetOfParentHeadquarter = null;
    }
    
    StringBuffer currentAttributes = new StringBuffer(1000);
    currentAttributes.append(ID).append("|")
    .append(Token).append("|")
    .append(this.getPossibleTypesString()).append("|")
    .append(this.NameOfUnit).append("|")
    .append(this.DistrictCourtOfUnit).append("|")
    .append(this.CommercialRegisterIDOfUnit).append("|")
    .append(this.PlaceOfUnitHeadquarter).append("|")
    .append(this.StreetOfUnitHeadquarter).append("|")
    .append(this.NameOfParent).append("|")
    .append(this.DistrictCourtOfParent).append("|")
    .append(this.CommercialRegisterIDOfParent).append("|")
    .append(this.PlaceOfParentHeadquarter).append("|")
    .append(this.StreetOfParentHeadquarter);
    
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
          NamedEntity.UNIT_OF_COMPANY])) {
            MostProbableType = NamedEntity.UNIT_OF_COMPANY;
          }
          else {
            MostProbableType = NamedEntity.UNKNOWN;
          }
          break;
        }
        case 3 : {
          this.NameOfUnit = currentToken;
          break;
        }
        case 4 : {
          this.DistrictCourtOfUnit = currentToken;
          break;
        }
        case 5 : {
          this.CommercialRegisterIDOfUnit = currentToken;
          break;
        }
        case 6 : {
          this.PlaceOfUnitHeadquarter = currentToken;
          break;
        }
        case 7 : {
          this.StreetOfUnitHeadquarter = currentToken;
          break;
        }
        case 8 : {
          this.NameOfParent = currentToken;
          break;
        }
        case 9 : {
          this.DistrictCourtOfParent = currentToken;
          break;
        }
        case 10 : {
          this.CommercialRegisterIDOfParent = currentToken;
          break;
        }
        case 11 : {
          this.PlaceOfParentHeadquarter = currentToken;
          break;
        }
        case 12 : {
          this.StreetOfParentHeadquarter = currentToken;
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