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

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class CompanyNE extends CompositeNE {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String Name = null;
  protected String DistrictCourt = null;
  protected String CommercialRegisterID = null;
  protected String PlaceOfHeadquarter = null;
  protected String StreetOfHeadquarter = null;
  protected String Abbreviation = null;
  public Vector NumbersCanonical = new Vector();
  
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
  
  public CompanyNE() {
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyNE(String pName, String pDistrictCourt,
  String pCommercialRegisterID, String pPlaceOfHeadquarter,
  String pStreetOfHeadquarter, String pAbbreviation) {
    
    this.Name = pName;
    this.DistrictCourt = pDistrictCourt;
    this.CommercialRegisterID = pCommercialRegisterID;
    this.PlaceOfHeadquarter = pPlaceOfHeadquarter;
    this.StreetOfHeadquarter = pStreetOfHeadquarter;
    this.Abbreviation = pAbbreviation;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyNE(int pID, String pToken, String pPlaceholder,
  int pPossibleType) {
    
    super(pID, pToken, pPlaceholder, pPossibleType);
    this.Name = null;
    this.DistrictCourt = null;
    this.CommercialRegisterID = null;
    this.PlaceOfHeadquarter = null;
    this.StreetOfHeadquarter = null;
    this.Abbreviation = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyNE(int pID, String pToken, String pPlaceholder,
  int pPossibleType, String pCanonical) {
    
    super(pID, pToken, pPlaceholder, pPossibleType, pCanonical);
    this.Name = null;
    this.DistrictCourt = null;
    this.CommercialRegisterID = null;
    this.PlaceOfHeadquarter = null;
    this.StreetOfHeadquarter = null;
    this.Abbreviation = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyNE(int pID, String pToken, int pType,
  String pCanonical, String pName, String pDistrictCourt,
  String pCommercialRegisterID, String pPlaceOfHeadquarter,
  String pStreetOfHeadquarter, String pAbbreviation) {
    
    super(pID, pToken, null, pType, pCanonical);
    this.Name = pName;
    this.DistrictCourt = pDistrictCourt;
    this.CommercialRegisterID = pCommercialRegisterID;
    this.PlaceOfHeadquarter = pPlaceOfHeadquarter;
    this.StreetOfHeadquarter = pStreetOfHeadquarter;
    this.Abbreviation = pAbbreviation;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyNE(int pID, String pName, String pDistrictCourt,
  String pCommercialRegisterID, String pPlaceOfHeadquarter,
  String pStreetOfHeadquarter, String pAbbreviation) {
    
    super(pID, null, null, NamedEntity.COMPANY, null);
    this.Name = pName;
    this.DistrictCourt = pDistrictCourt;
    this.CommercialRegisterID = pCommercialRegisterID;
    this.PlaceOfHeadquarter = pPlaceOfHeadquarter;
    this.StreetOfHeadquarter = pStreetOfHeadquarter;
    this.Abbreviation = pAbbreviation;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public CompanyNE(String pCanonical, String pName,
  String pDistrictCourt, String pCommercialRegisterID,
  String pPlaceOfHeadquarter, String pStreetOfHeadquarter,
  String pAbbreviation) {
    
    super(0, null, null, NamedEntity.COMPANY, pCanonical);
    this.Name = pName;
    this.DistrictCourt = pDistrictCourt;
    this.CommercialRegisterID = pCommercialRegisterID;
    this.PlaceOfHeadquarter = pPlaceOfHeadquarter;
    this.StreetOfHeadquarter = pStreetOfHeadquarter;
    this.Abbreviation = pAbbreviation;
    this.CanonicalForm = true;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getName() {
    return Name; }
  public String getDistrictCourt() {
    return DistrictCourt; }
  public String getCommercialRegisterID() {
    return CommercialRegisterID; }
  public String getPlaceOfHeadquarter() {
    return PlaceOfHeadquarter; }
  public String getStreetOfHeadquarter() {
    return StreetOfHeadquarter; }
  public String getAbbreviation() {
    return Abbreviation; }
  
  public String getCanonical() {
    
    if (this.Canonical != null) {
      return this.ID + "," + this.Canonical;
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("CompanyNE: \"").append(Name).append("\"")
    .append((PlaceOfHeadquarter != null && PlaceOfHeadquarter.length() > 0 
    ? ", HQ_P:" : "")).append((PlaceOfHeadquarter != null 
    && PlaceOfHeadquarter.length() > 0 ? PlaceOfHeadquarter : ""))
    .append((StreetOfHeadquarter != null && StreetOfHeadquarter.length() > 0 
    ? ", HQ_S:" : "")).append((StreetOfHeadquarter != null 
    && StreetOfHeadquarter.length() > 0 ? StreetOfHeadquarter : ""))
    .append(", ID=").append(ID).append(", PH=").append(this.Placeholder)
    .append(", PT=").append(this.getPossibleTypesPlaceholder())
    .append(", CF=").append(this.isCanonicalForm() ? "true" : "false");
    
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
    if (this.Canonical != null && this.Canonical.equals("")) {
      this.Canonical = null;
    }
    if (this.Name != null && this.Name.equals("")) {
      this.Name = null;
    }
    if (this.DistrictCourt != null && this.DistrictCourt.equals("")) {
      this.DistrictCourt = null;
    }
    if (this.CommercialRegisterID != null
    && this.CommercialRegisterID.equals("")) {
      this.CommercialRegisterID = null;
    }
    if (this.PlaceOfHeadquarter != null
    && this.PlaceOfHeadquarter.equals("")) {
      this.PlaceOfHeadquarter = null;
    }
    if (this.StreetOfHeadquarter != null
    && this.StreetOfHeadquarter.equals("")) {
      this.StreetOfHeadquarter = null;
    }
    if (this.Abbreviation != null && this.Abbreviation.equals("")) {
      this.Abbreviation = null;
    }
    
    StringBuffer currentAttributes = new StringBuffer(1000);
    currentAttributes.append(ID).append("|")
    .append(Token).append("|")
    .append(this.getPossibleTypesString()).append("|")
    .append(Canonical).append("|")
    .append(this.Name).append("|")
    .append(this.DistrictCourt).append("|")
    .append(this.CommercialRegisterID).append("|")
    .append(this.PlaceOfHeadquarter).append("|")
    .append(this.StreetOfHeadquarter).append("|")
    .append(this.Abbreviation);
    
    return currentAttributes.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setAttributeString(String pfeature) {
    
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
          if (currentToken.equals(
          NamedEntity.TYPE_DESCRIPTIONS[NamedEntity.COMPANY])) {
            MostProbableType = NamedEntity.COMPANY;
          }
          else {
            MostProbableType = NamedEntity.UNKNOWN;
          }
          break;
        }
        case 3 : {
          Canonical = currentToken;
          break;
        }
        case 4 : {
          this.Name = currentToken;
          break;
        }
        case 5 : {
          this.DistrictCourt = currentToken;
          break;
        }
        case 6 : {
          this.CommercialRegisterID = currentToken;
          break;
        }
        case 7 : {
          this.PlaceOfHeadquarter = currentToken;
          break;
        }
        case 8 : {
          this.StreetOfHeadquarter = currentToken;
          break;
        }
        case 9 : {
          this.Abbreviation = currentToken;
          break;
        }
        
      }
      counter ++;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isSimilar(CompanyNE pCompanyNE) {
    
    boolean result = false;
    
    if (this.Name != null && (this.Name.equals(pCompanyNE.Name)
    || this.Name.toLowerCase().equals(pCompanyNE.Name.toLowerCase()))) {
      
      if (((this.CommercialRegisterID != null) && (
      this.CommercialRegisterID.equals(pCompanyNE.CommercialRegisterID)))
      || pCompanyNE.CommercialRegisterID == null) {
        if (this.DistrictCourt == null
        || pCompanyNE.DistrictCourt == null) {
          result = true;
        }
        else if ((this.DistrictCourt != null) && (
        this.DistrictCourt.equals(pCompanyNE.DistrictCourt))) {
          result = true;
        }
      }
      
      if (this.PlaceOfHeadquarter == null
      || pCompanyNE.PlaceOfHeadquarter == null) {
        result = true;
      }
      else if (this.PlaceOfHeadquarter != null && (
      this.PlaceOfHeadquarter.equals(pCompanyNE.PlaceOfHeadquarter)
      || this.PlaceOfHeadquarter.toLowerCase().equals(
      pCompanyNE.PlaceOfHeadquarter.toLowerCase()))) {
        result = true;
      }
      
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static CompanyNE createCanonicalForm
  (CompanyNE[] pSimilarCompanyNEs) {
    
    CompanyNE oneCompany = null;
    CompanyNE[] similarCompanyNEs = pSimilarCompanyNEs;
    CompanyNE buildCompany = null;
    
    String currentName = null;
    String currentDistrictCourt = null;
    String currentCommercialRegisterID = null;
    String currentPlaceOfHeadquarter = null;
    String currentStreetOfHeadquarter = null;
    String currentAbbreviation = null;
    
    StringBuffer currentCanonical = new StringBuffer(1000);
    
    for (int i = 0; i < similarCompanyNEs.length; i++) {
      
      oneCompany = (CompanyNE)similarCompanyNEs[i];
      
      if (i < (similarCompanyNEs.length - 1)) {
        currentCanonical.append(oneCompany.ID).append(",");
      }
      else {
        currentCanonical.append(oneCompany.ID);
      }
      
      if (oneCompany.Name != null
      && oneCompany.Name.length() > 0) {
        currentName = oneCompany.Name;
      }
      if (oneCompany.DistrictCourt != null
      && oneCompany.DistrictCourt.length() > 0) {
        currentDistrictCourt = oneCompany.DistrictCourt;
      }
      if (oneCompany.CommercialRegisterID != null
      && oneCompany.CommercialRegisterID.length() > 0) {
        currentCommercialRegisterID = oneCompany.CommercialRegisterID;
      }
      if (oneCompany.PlaceOfHeadquarter != null
      && oneCompany.PlaceOfHeadquarter.length() > 0) {
        currentPlaceOfHeadquarter = oneCompany.PlaceOfHeadquarter;
      }
      if (oneCompany.StreetOfHeadquarter != null
      && oneCompany.StreetOfHeadquarter.length() > 0) {
        currentStreetOfHeadquarter = oneCompany.StreetOfHeadquarter;
      }
      if (oneCompany.Abbreviation != null
      && oneCompany.Abbreviation.length() > 0) {
        currentAbbreviation = oneCompany.Abbreviation;
      }
      
    }
    
    buildCompany = new CompanyNE(currentCanonical.toString(),
    currentName, currentDistrictCourt, currentCommercialRegisterID,
    currentPlaceOfHeadquarter, currentStreetOfHeadquarter, currentAbbreviation);
    
    return buildCompany;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static ArrayList groupSimilarCompanyNEs  (ArrayList pCompanyVector) {
    
    CompanyNE oneCompany = null;
    ArrayList similarCompanies = new ArrayList();
    ArrayList returnResult = new ArrayList();
    
    for (int i = 0; i < pCompanyVector.size(); i ++) {
      
      CompanyNE firstCompany = (CompanyNE) pCompanyVector.get(i);
      
      CompanyNE newCompany = new CompanyNE(
      (int) firstCompany.ID,
      (String) firstCompany.Name,
      (String) firstCompany.DistrictCourt,
      (String) firstCompany.CommercialRegisterID,
      (String) firstCompany.PlaceOfHeadquarter,
      (String) firstCompany.StreetOfHeadquarter,
      (String) firstCompany.Abbreviation);
      
      similarCompanies.add(newCompany);
      
      if (i < (pCompanyVector.size() - 1)) {
        for (int j = (i+1); j < pCompanyVector.size(); j ++) {
          CompanyNE controlCompany = (CompanyNE) pCompanyVector.get(j);
          
          oneCompany = new CompanyNE(
          (int) controlCompany.ID,
          (String) controlCompany.Name,
          (String) controlCompany.DistrictCourt,
          (String) controlCompany.CommercialRegisterID,
          (String) controlCompany.PlaceOfHeadquarter,
          (String) firstCompany.StreetOfHeadquarter,
          (String) controlCompany.Abbreviation);
          
          boolean result = newCompany.isSimilar(oneCompany);
          
          if (result == true) {
            similarCompanies.add(oneCompany);
            pCompanyVector.remove(j);
            j--;
          }
          
        }
      }
      
      CompanyNE[] currentResult = new CompanyNE[similarCompanies.size()];
      for (int k = 0; k < similarCompanies.size(); k ++) {
        currentResult[k] = (CompanyNE) similarCompanies.get(k);
      }
      similarCompanies.clear();
      CompanyNE resultNE = createCanonicalForm(currentResult);
      
      returnResult.add(resultNE);
      
    }
    
    return returnResult;
    
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