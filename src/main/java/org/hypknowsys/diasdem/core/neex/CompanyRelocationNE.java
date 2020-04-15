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
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class CompanyRelocationNE extends CompositeNE {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String Name = null;
  protected String OriginPlaceOfHeadquarter = null;
  protected String OriginStreetOfHeadquarter = null;
  protected String OriginDistrictCourt = null;
  protected String OriginCommercialRegisterID = null;
  protected String DestinationPlaceOfHeadquarter = null;
  protected String DestinationStreetOfHeadquarter = null;
  protected String DestinationDistrictCourt = null;
  protected String DestinationCommercialRegisterID = null;
  
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
  
  public CompanyRelocationNE() {
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyRelocationNE(String pName, String pOriginDistrictCourt,
  String pOriginCommercialRegisterID, String pOriginPlaceOfHeadquarter,
  String pOriginStreetOfHeadquarter,
  String pDestinationDistrictCourt, String pDestinationCommercialRegisterID,
  String pDestinationPlaceOfHeadquarter,
  String pDestinationStreetOfHeadquarter) {
    
    this.Name = pName;
    this.OriginDistrictCourt = pOriginDistrictCourt;
    this.OriginCommercialRegisterID = pOriginCommercialRegisterID;
    this.OriginPlaceOfHeadquarter = pOriginPlaceOfHeadquarter;
    this.OriginStreetOfHeadquarter = pOriginStreetOfHeadquarter;
    this.DestinationDistrictCourt = pDestinationDistrictCourt;
    this.DestinationCommercialRegisterID = pDestinationCommercialRegisterID;
    this.DestinationPlaceOfHeadquarter = pDestinationPlaceOfHeadquarter;
    this.DestinationStreetOfHeadquarter = pDestinationStreetOfHeadquarter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyRelocationNE(int pID, String pToken, String pPlaceholder,
  int pPossibleType) {
    
    super(pID, pToken, pPlaceholder, pPossibleType);
    this.Name = null;
    this.OriginDistrictCourt = null;
    this.OriginCommercialRegisterID = null;
    this.OriginPlaceOfHeadquarter = null;
    this.DestinationDistrictCourt = null;
    this.DestinationCommercialRegisterID = null;
    this.DestinationPlaceOfHeadquarter = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyRelocationNE(int pID, String pToken, int pType,
  String pName, String pOriginDistrictCourt,
  String pOriginCommercialRegisterID, String pOriginPlaceOfHeadquarter,
  String pOriginStreetOfHeadquarter,
  String pDestinationDistrictCourt, String pDestinationCommercialRegisterID,
  String pDestinationPlaceOfHeadquarter,
  String pDestinationStreetOfHeadquarter) {
    
    super(pID, pToken, null, pType, null);
    this.Name = pName;
    this.OriginDistrictCourt = pOriginDistrictCourt;
    this.OriginCommercialRegisterID = pOriginCommercialRegisterID;
    this.OriginPlaceOfHeadquarter = pOriginPlaceOfHeadquarter;
    this.OriginStreetOfHeadquarter = pOriginStreetOfHeadquarter;
    this.DestinationDistrictCourt = pDestinationDistrictCourt;
    this.DestinationCommercialRegisterID = pDestinationCommercialRegisterID;
    this.DestinationPlaceOfHeadquarter = pDestinationPlaceOfHeadquarter;
    this.DestinationStreetOfHeadquarter = pDestinationStreetOfHeadquarter;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getName() {
    return Name; }
  public String getOriginPlaceOfHeadquarter() {
    return OriginPlaceOfHeadquarter; }
  public String getOriginStreetOfHeadquarter() {
    return OriginStreetOfHeadquarter; }
  public String getOriginDistrictCourt() {
    return OriginDistrictCourt; }
  public String getOriginCommercialRegisterID() {
    return OriginCommercialRegisterID; }
  public String getDestinationPlaceOfHeadquarter() {
    return DestinationPlaceOfHeadquarter; }
  public String getDestinationStreetOfHeadquarter() {
    return DestinationStreetOfHeadquarter; }
  public String getDestinationDistrictCourt() {
    return DestinationDistrictCourt; }
  public String getDestinationCommercialRegisterID() {
    return DestinationCommercialRegisterID; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("CompanyRelocationNE: \"").append(Name).append(" ")
    .append(OriginPlaceOfHeadquarter).append(" -> ")
    .append(DestinationPlaceOfHeadquarter)
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
    if (this.Name != null && this.Name.equals("")) {
      this.Name = null;
    }
    if (this.OriginDistrictCourt != null
    && this.OriginDistrictCourt.equals("")) {
      this.OriginDistrictCourt = null;
    }
    if (this.OriginCommercialRegisterID != null
    &&  this.OriginCommercialRegisterID.equals("")) {
      this.OriginCommercialRegisterID = null;
    }
    if (this.OriginPlaceOfHeadquarter != null
    && this.OriginPlaceOfHeadquarter.equals("")) {
      this.OriginPlaceOfHeadquarter = null;
    }
    if (this.OriginStreetOfHeadquarter != null
    && this.OriginStreetOfHeadquarter.equals("")) {
      this.OriginStreetOfHeadquarter = null;
    }
    if (this.DestinationDistrictCourt != null
    && this.DestinationDistrictCourt.equals("")) {
      this.DestinationDistrictCourt = null;
    }
    if (this.DestinationCommercialRegisterID != null
    &&  this.DestinationCommercialRegisterID.equals("")) {
      this.DestinationCommercialRegisterID = null;
    }
    if (this.DestinationPlaceOfHeadquarter != null
    && this.DestinationPlaceOfHeadquarter.equals("")) {
      this.DestinationPlaceOfHeadquarter = null;
    }
    if (this.DestinationStreetOfHeadquarter != null
    && this.DestinationStreetOfHeadquarter.equals("")) {
      this.DestinationStreetOfHeadquarter = null;
    }
    
    StringBuffer currentAttributes = new StringBuffer(1000);
    currentAttributes.append(ID).append("|")
    .append(Token).append("|")
    .append(this.getPossibleTypesString()).append("|")
    .append(this.Name).append("|")
    .append(this.OriginDistrictCourt).append("|")
    .append(this.OriginCommercialRegisterID).append("|")
    .append(this.OriginPlaceOfHeadquarter).append("|")
    .append(this.OriginStreetOfHeadquarter).append("|")
    .append(this.DestinationDistrictCourt).append("|")
    .append(this.DestinationCommercialRegisterID).append("|")
    .append(this.DestinationPlaceOfHeadquarter).append("|")
    .append(this.DestinationStreetOfHeadquarter);
    
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
          NamedEntity.COMPANY_RELOCATION])) {
            MostProbableType = NamedEntity.COMPANY_RELOCATION;
          }
          else {
            MostProbableType = NamedEntity.UNKNOWN;
          }
          break;
        }
        case 3 : {
          this.Name = currentToken;
          break;
        }
        case 4 : {
          this.OriginDistrictCourt = currentToken;
          break;
        }
        case 5 : {
          this.OriginCommercialRegisterID = currentToken;
          break;
        }
        case 6 : {
          this.OriginPlaceOfHeadquarter = currentToken;
          break;
        }
        case 7 : {
          this.OriginStreetOfHeadquarter = currentToken;
          break;
        }
        case 8 : {
          this.DestinationDistrictCourt = currentToken;
          break;
        }
        case 9 : {
          this.DestinationCommercialRegisterID = currentToken;
          break;
        }
        case 10 : {
          this.DestinationPlaceOfHeadquarter = currentToken;
          break;
        }
        case 11 : {
          this.DestinationStreetOfHeadquarter = currentToken;
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