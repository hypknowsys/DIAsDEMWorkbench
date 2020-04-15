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
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Henner Graubitz, Karsten Winkler
 */

public class NamedEntity implements Cloneable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected int ID = 0;
  protected String Token = null;
  protected String Placeholder = null;
  protected int MostProbableType = UNKNOWN;
  protected int NumberOfPossibleTypes = 0;
  protected boolean[] PossibleTypes = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient StringTokenizer TmpStringTokenizer = null;
  private transient String TmpString = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final int NUMBER_OF_TYPES = 41;
  // basic named entities
  public static final int UNKNOWN = 0;
  public static final int NUMBER = 1;
  public static final int DATE = 2;
  public static final int TIME = 3;
  public static final int PLACE = 4;
  public static final int ORGANIZATION = 5;
  public static final int ORGANIZATION_ABBREVIATION = 6;
  public static final int AMOUNT_OF_MONEY = 7;
  public static final int PARAGRAPH = 8;
  public static final int FORENAME = 9;
  public static final int SURNAME = 10;
  public static final int TITLE = 11;
  public static final int MIDDLE_INITIAL = 12;
  public static final int EMAIL = 13;
  public static final int URL = 14;
  public static final int ORGANIZATION_ID = 15;
  public static final int DOCUMENT_ID = 16;
  public static final int COURT = 17;
  public static final int PLACE_AFFIX = 18;
  public static final int NAME_AFFIX = 19;
  public static final int POSTAL_CODE = 20;
  public static final int PERSON_NAME = 21;
  public static final int ISIN = 22;
  public static final int WKN = 23;
  public static final int PROFESSION = 24;
  public static final int STREET = 25;
  public static final int REFERENCE_NUMBER = 26;
  public static final int PERCENTAGE = 27;
  public static final int NEWSPAPER = 28;
  public static final int STOCK_EXCHANGE = 29;
  public static final int NUMBER_OF_SHARES = 30;
  public static final int AMOUNT_OF_MONEY_PER_SHARE = 31;
  // composite named entities
  public static final int PERSON = 32;
  public static final int COMPANY = 33;
  public static final int COMPANY_RELOCATION = 34;
  public static final int DATE_PERIOD = 35;
  public static final int AMOUNT_OF_MONEY_RANGE = 36;
  public static final int PERCENTAGE_RANGE = 37;
  public static final int EQUITY_STAKE = 38;
  public static final int KEY_FIGURE = 39;
  public static final int UNIT_OF_COMPANY = 40;
  
  // size of these arrays must correspond to NUMBER_OF_TYPES
  public static final String[] TYPE_DESCRIPTIONS = {
    "unknown", "number", "date", "time", "place", "organization",
    "organization_abbreviation", "amount_of_money", "paragraph",
    "forename", "surname", "title", "middle_initial",
    "email", "url", "organization_id", "document_id", "court",
    "place_affix", "name_affix", "postal_code", "person_name",
    "isin", "wkn", "profession", "street", "reference_number",
    "percentage", "newspaper", "stock_exchange", "number_of_shares",
    "amount_of_money_per_share",
    "person", "company", "company_relocation", "date_period",
    "amount_of_money_range", "percentage_range", "equity_stake",
    "key_figure", "unit_of_company"
  };
  public static final String[] MOST_PROBABLE_TYPE_STRINGS = {
    "UnknownNE", "Number", "Date", "Time", "Place", "Organization",
    "OrganizationAbbreviation", "AmountOfMoney", "Paragraph",
    "Forename", "Surname", "Title", "MiddleInitial",
    "EMail", "URL", "OrganizationID", "DocumentID", "Court",
    "PlaceAffix", "NameAffix", "PostalCode", "PersonName",
    "ISIN", "WKN", "Profession", "Street", "ReferenceNumber",
    "Percentage", "Newspaper", "StockExchange", "NumberOfShares",
    "AmountOfMoneyPerShare",
    "Person", "Company", "CompanyRelocation", "DatePeriod",
    "AmountOfMoneyRange", "PercentageRange", "EquityStake",
    "KeyFigure", "UnitOfCompany"
  };
  public static final boolean[] INITIAL_POSSIBLE_TYPES = {
    false, false, false, false, false, false,
    false, false, false,
    false, false, false, false,
    false, false, false, false, false,
    false, false, false, false,
    false, false, false, false, false,
    false, false, false, false,
    false,
    false, false, false, false,
    false, false, false,
    false, false
  };
  
  // each placeholder consists of PLACEHOLDER_PREFIX + this.ID +
  // PLACEHOLDER_SUFFIX; example: "<<123>>", this.ID = 123
  public static final String PLACEHOLDER_PREFIX = "<<";
  public static final String PLACEHOLDER_SUFFIX = ">>";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntity() {
    
    ID = 0;
    Token = null;
    Placeholder = null;
    MostProbableType = UNKNOWN;
    NumberOfPossibleTypes = 0;
    PossibleTypes = new boolean[INITIAL_POSSIBLE_TYPES.length];
    System.arraycopy(INITIAL_POSSIBLE_TYPES, 0, PossibleTypes,
    0, INITIAL_POSSIBLE_TYPES.length);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntity(int pID, String pToken) {
    
    this();
    ID = pID;
    Token = pToken;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntity(int pID, String pToken, String pPlaceholder,
  int pPossibleType) {
    
    this(pID, pToken);
    Placeholder = pPlaceholder;
    this.addPossibleType(pPossibleType);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntity(int pID, String pToken, String pPlaceholder,
  int[] pPossibleTypes) {
    
    this(pID, pToken);
    Placeholder = pPlaceholder;
    this.addPossibleTypes(pPossibleTypes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntity(int pID, String pToken, String pPlaceholder,
  String pPossibleTypesString) {
    
    
    this(pID, pToken);
    Placeholder = pPlaceholder;
    TmpString = null;
    TmpStringTokenizer = new StringTokenizer(pPossibleTypesString);
    while (TmpStringTokenizer.hasMoreTokens()) {
      TmpString = TmpStringTokenizer.nextToken();
      for (int i = 0; i < NUMBER_OF_TYPES; i++) {
        if (TmpString.equals(TYPE_DESCRIPTIONS[i])) {
          this.addPossibleType(i);
        }
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getID() {
    return ID; }
  public String getToken() {
    return Token; }
  public String getPlaceholder() {
    return Placeholder; }
  public int getMostProbableType() {
    return MostProbableType; }
  public int getNumberOfPossibleTypes() {
    return NumberOfPossibleTypes; }
  public boolean[] getPossibleTypes() {
    return PossibleTypes; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setID(int pID) {
    ID = pID; }
  public void setToken(String pToken) {
    Token = pToken; }
  public void appendToken(String pAppendToken) {
    Token += pAppendToken; }
  public void setPlaceholder(String pPlaceholder) {
    Placeholder = pPlaceholder; }
  public void setMostProbableType(int pMostProbableType) {
    MostProbableType = pMostProbableType; }
  public void setNumberOfPossibleTypes(int pNumberOfPossibleTypes) {
    NumberOfPossibleTypes = pNumberOfPossibleTypes; }
  public void setPossibleTypes(boolean[] pPossibleTypes) {
    PossibleTypes = pPossibleTypes; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    StringBuffer result = new StringBuffer(1000);
    result.append("NamedEntity: \"").append(Token).append("\", ID=")
    .append(ID).append(", PH=").append(this.Placeholder).append(", PT=")
    .append(this.getPossibleTypesPlaceholder());
    
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object clone() {
    
    boolean[] possibleTypesClone = new boolean[PossibleTypes.length];
    System.arraycopy(PossibleTypes, 0, possibleTypesClone, 0,
    possibleTypesClone.length);
    NamedEntity myClone = new NamedEntity(ID, Token);
    myClone.setPlaceholder(Placeholder);
    myClone.setMostProbableType(MostProbableType);
    myClone.setNumberOfPossibleTypes(NumberOfPossibleTypes);
    myClone.setPossibleTypes(possibleTypesClone);
    
    return myClone;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPossibleTypesPlaceholder() {
    
    StringBuffer result = new StringBuffer(1000);
    result.append(PLACEHOLDER_PREFIX);
    result.append(this.getPossibleTypesString());
    result.append(PLACEHOLDER_SUFFIX);
    
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addPossibleTypes(int[] pPossibleTypes) {
    
    if (pPossibleTypes != null) {
      for (int i = 0; i < pPossibleTypes.length; i++) {
        this.addPossibleType(pPossibleTypes[i]);
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addPossibleType(int pPossibleType) {
    
    if (pPossibleType >= 0 && pPossibleType < NUMBER_OF_TYPES) {
      if (!PossibleTypes[pPossibleType]) {
        NumberOfPossibleTypes++;
      }
      PossibleTypes[pPossibleType] = true;
      if (pPossibleType == PERSON || pPossibleType == COMPANY
      || pPossibleType == DATE || pPossibleType == AMOUNT_OF_MONEY
      || pPossibleType == PARAGRAPH || pPossibleType == NUMBER
      || pPossibleType == EMAIL || pPossibleType == URL
      || pPossibleType == ORGANIZATION_ID || pPossibleType == PLACE
      || pPossibleType == DOCUMENT_ID || pPossibleType == COURT
      || pPossibleType == PLACE_AFFIX || pPossibleType == NAME_AFFIX
      || pPossibleType == POSTAL_CODE || pPossibleType == PERSON_NAME
      || pPossibleType == ISIN || pPossibleType == WKN
      || pPossibleType == PROFESSION || pPossibleType == STREET
      || pPossibleType == COMPANY_RELOCATION
      || pPossibleType == REFERENCE_NUMBER
      || pPossibleType == PERCENTAGE || pPossibleType == NEWSPAPER
      || pPossibleType == STOCK_EXCHANGE || pPossibleType == NUMBER_OF_SHARES
      || pPossibleType == AMOUNT_OF_MONEY_PER_SHARE 
      || pPossibleType == DATE_PERIOD || pPossibleType == AMOUNT_OF_MONEY_RANGE
      || pPossibleType == PERCENTAGE_RANGE || pPossibleType == EQUITY_STAKE 
      || pPossibleType == KEY_FIGURE || pPossibleType == UNIT_OF_COMPANY) {
        MostProbableType = pPossibleType;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void deletePossibleType(int pPossibleType) {
    
    if (pPossibleType >= 0 && pPossibleType < NUMBER_OF_TYPES) {
      if (PossibleTypes[pPossibleType]) {
        NumberOfPossibleTypes--;
      }
      PossibleTypes[pPossibleType] = false;
      if (pPossibleType == MostProbableType) {
        MostProbableType = UNKNOWN;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isPossibleType(int pPossibleType) {
    
    if (pPossibleType >= 0 && pPossibleType < NUMBER_OF_TYPES) {
      return PossibleTypes[pPossibleType];
    }
    else {
      return false;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int[] getPossibleTypesArray() {
    
    int[] result = new int[NumberOfPossibleTypes];
    int typeCounter = 0;
    for (int i = 0; i < PossibleTypes.length; i++) {
      if (PossibleTypes[i]) {
        result[typeCounter++] = i;
      }
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPossibleTypesString() {
    
    StringBuffer result = new StringBuffer(1000);
    int typeCounter = 0;
    for (int i = 0; i < PossibleTypes.length; i++) {
      if (PossibleTypes[i]) {
        if (typeCounter++ > 0) {
          result.append(" ");
        }
        result.append(TYPE_DESCRIPTIONS[i]);
      }
    }
    
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getMostProbableDescription() {
    
    TmpStringBuffer = new StringBuffer(1000);
    
    switch (MostProbableType) {
      case PERSON: {
        if (this instanceof PersonNE) {
          PersonNE person = (PersonNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(person.ID);
          if (!Tools.stringIsNullOrEmpty(person.Name)) {
            TmpStringBuffer.append("; Name: ");
            TmpStringBuffer.append(person.Name);
          }
          if (!Tools.stringIsNullOrEmpty(person.Surname)) {
            TmpStringBuffer.append("; Surname: ");
            TmpStringBuffer.append(person.Surname);
          }
          if (!Tools.stringIsNullOrEmpty(person.Forename)) {
            TmpStringBuffer.append("; Forename: ");
            TmpStringBuffer.append(person.Forename);
          }
          if (!Tools.stringIsNullOrEmpty(person.Title)) {
            TmpStringBuffer.append("; Title: ");
            TmpStringBuffer.append(person.Title);
          }
          if (!Tools.stringIsNullOrEmpty(person.MiddleInitial)) {
            TmpStringBuffer.append("; MiddleInitial: ");
            TmpStringBuffer.append(person.MiddleInitial);
          }
          if (!Tools.stringIsNullOrEmpty(person.DateOfBirth)) {
            TmpStringBuffer.append("; DateOfBirth: ");
            TmpStringBuffer.append(person.DateOfBirth);
          }
          if (!Tools.stringIsNullOrEmpty(person.Birthname)) {
            TmpStringBuffer.append("; Birthname: ");
            TmpStringBuffer.append(person.Birthname);
          }
          if (!Tools.stringIsNullOrEmpty(person.PlaceOfResidence)) {
            TmpStringBuffer.append("; Place: ");
            TmpStringBuffer.append(person.PlaceOfResidence);
          }
          if (!Tools.stringIsNullOrEmpty(person.StreetOfResidence)) {
            TmpStringBuffer.append("; Street: ");
            TmpStringBuffer.append(person.StreetOfResidence);
          }
          if (!Tools.stringIsNullOrEmpty(person.Occupation)) {
            TmpStringBuffer.append("; Profession: ");
            TmpStringBuffer.append(person.Occupation);
          }
        }
        break;
      }
      case COMPANY: {
        if (this instanceof CompanyNE) {
          CompanyNE company = (CompanyNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(company.ID);
          if (!Tools.stringIsNullOrEmpty(company.Name)) {
            TmpStringBuffer.append("; Name: ");
            TmpStringBuffer.append(company.Name);
          }
          if (!Tools.stringIsNullOrEmpty(company.PlaceOfHeadquarter)) {
            TmpStringBuffer.append("; Place: ");
            TmpStringBuffer.append(company.PlaceOfHeadquarter);
          }
          if (!Tools.stringIsNullOrEmpty(company.StreetOfHeadquarter)) {
            TmpStringBuffer.append("; Street: ");
            TmpStringBuffer.append(company.StreetOfHeadquarter);
          }
          if (!Tools.stringIsNullOrEmpty(company.DistrictCourt)) {
            TmpStringBuffer.append("; DistrictCourt: ");
            TmpStringBuffer.append(company.DistrictCourt);
          }
          if (!Tools.stringIsNullOrEmpty(company.CommercialRegisterID)) {
            TmpStringBuffer.append("; CommercialRegisterID: ");
            TmpStringBuffer.append(company.CommercialRegisterID);
          }
        }
        break;
      }
      case COMPANY_RELOCATION: {
        if (this instanceof CompanyRelocationNE) {
          CompanyRelocationNE companyRelocation  = (CompanyRelocationNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(companyRelocation.ID);
          if (!Tools.stringIsNullOrEmpty(companyRelocation.Name)) {
            TmpStringBuffer.append("; Name: ");
            TmpStringBuffer.append(companyRelocation.Name);
          }
          if (!Tools.stringIsNullOrEmpty(companyRelocation
          .OriginDistrictCourt)) {
            TmpStringBuffer.append("; OriginDistrictCourt: ");
            TmpStringBuffer.append(companyRelocation.OriginDistrictCourt);
          }
          if (!Tools.stringIsNullOrEmpty(companyRelocation
          .OriginCommercialRegisterID)) {
            TmpStringBuffer.append("; OriginCommercialRegisterID: ");
            TmpStringBuffer.append(companyRelocation
            .OriginCommercialRegisterID);
          }
          if (!Tools.stringIsNullOrEmpty(companyRelocation
          .OriginPlaceOfHeadquarter)) {
            TmpStringBuffer.append("; OriginPlace: ");
            TmpStringBuffer.append(companyRelocation.OriginPlaceOfHeadquarter);
          }
          if (!Tools.stringIsNullOrEmpty(companyRelocation
          .OriginStreetOfHeadquarter)) {
            TmpStringBuffer.append("; OriginStreet: ");
            TmpStringBuffer.append(companyRelocation.OriginStreetOfHeadquarter);
          }
          if (!Tools.stringIsNullOrEmpty(companyRelocation
          .DestinationDistrictCourt)) {
            TmpStringBuffer.append("; DestinationDistrictCourt: ");
            TmpStringBuffer.append(companyRelocation.DestinationDistrictCourt);
          }
          if (!Tools.stringIsNullOrEmpty(companyRelocation
          .DestinationCommercialRegisterID)) {
            TmpStringBuffer.append("; DestinationCommercialRegisterID: ");
            TmpStringBuffer.append(companyRelocation
            .DestinationCommercialRegisterID);
          }
          if (!Tools.stringIsNullOrEmpty(companyRelocation
          .DestinationPlaceOfHeadquarter)) {
            TmpStringBuffer.append("; DestinationPlace: ");
            TmpStringBuffer.append(companyRelocation
            .DestinationPlaceOfHeadquarter);
          }
          if (!Tools.stringIsNullOrEmpty(companyRelocation
          .DestinationStreetOfHeadquarter)) {
            TmpStringBuffer.append("; DestinationStreet: ");
            TmpStringBuffer.append(companyRelocation
            .DestinationStreetOfHeadquarter);
          }
        }
        break;
      }
      case DATE_PERIOD: {
        if (this instanceof DatePeriodNE) {
          DatePeriodNE datePeriod = (DatePeriodNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(datePeriod.ID);
          if (!Tools.stringIsNullOrEmpty(datePeriod.PeriodBeginDate)) {
            TmpStringBuffer.append("; Begin: ");
            TmpStringBuffer.append(datePeriod.PeriodBeginDate);
          }
          if (!Tools.stringIsNullOrEmpty(datePeriod.PeriodEndDate)) {
            TmpStringBuffer.append("; End: ");
            TmpStringBuffer.append(datePeriod.PeriodEndDate);
          }
        }
        break;
      }
      case AMOUNT_OF_MONEY_RANGE: {
        if (this instanceof AmountOfMoneyRangeNE) {
          AmountOfMoneyRangeNE amountOfMoneyRange = (AmountOfMoneyRangeNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(amountOfMoneyRange.ID);
          if (!Tools.stringIsNullOrEmpty(amountOfMoneyRange
          .MinimumAmountOfMoney)) {
            TmpStringBuffer.append("; Min: ");
            TmpStringBuffer.append(amountOfMoneyRange.MinimumAmountOfMoney);
          }
          if (!Tools.stringIsNullOrEmpty(amountOfMoneyRange
          .MaximumAmountOfMoney)) {
            TmpStringBuffer.append("; Max: ");
            TmpStringBuffer.append(amountOfMoneyRange.MaximumAmountOfMoney);
          }
        }
        break;
      }
      case PERCENTAGE_RANGE: {
        if (this instanceof PercentageRangeNE) {
          PercentageRangeNE percentageRange = (PercentageRangeNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(percentageRange.ID);
          if (!Tools.stringIsNullOrEmpty(percentageRange
          .MinimumPercentage)) {
            TmpStringBuffer.append("; Min: ");
            TmpStringBuffer.append(percentageRange.MinimumPercentage);
          }
          if (!Tools.stringIsNullOrEmpty(percentageRange
          .MaximumPercentage)) {
            TmpStringBuffer.append("; Max: ");
            TmpStringBuffer.append(percentageRange.MaximumPercentage);
          }
        }
        break;
      }
      case EQUITY_STAKE: {
        if (this instanceof EquityStakeNE) {
          EquityStakeNE equityStake = (EquityStakeNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(equityStake.ID);
          if (!Tools.stringIsNullOrEmpty(equityStake.CompanyName)) {
            TmpStringBuffer.append("; CompanyName: ");
            TmpStringBuffer.append(equityStake.CompanyName);
          }
          if (!Tools.stringIsNullOrEmpty(equityStake.NumberOfShares)) {
            TmpStringBuffer.append("; NumberOfShares: ");
            TmpStringBuffer.append(equityStake.NumberOfShares);
          }
          if (!Tools.stringIsNullOrEmpty(equityStake.PercentageOfShares)) {
            TmpStringBuffer.append("; PercentageOfShares: ");
            TmpStringBuffer.append(equityStake.PercentageOfShares);
          }
        }
        break;
      }
      case KEY_FIGURE: {
        if (this instanceof KeyFigureNE) {
          KeyFigureNE keyFigure = (KeyFigureNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(keyFigure.ID);
          if (!Tools.stringIsNullOrEmpty(keyFigure.Name)) {
            TmpStringBuffer.append("; Name: ");
            TmpStringBuffer.append(keyFigure.Name);
          }
          if (!Tools.stringIsNullOrEmpty(keyFigure.Value)) {
            TmpStringBuffer.append("; Value: ");
            TmpStringBuffer.append(keyFigure.Value);
          }
        }
        break;
      }
      case UNIT_OF_COMPANY: {
        if (this instanceof UnitOfCompanyNE) {
          UnitOfCompanyNE unitOfCompany  = (UnitOfCompanyNE)this;
          TmpStringBuffer.append("ID: ");
          TmpStringBuffer.append(unitOfCompany.ID);
          if (!Tools.stringIsNullOrEmpty(unitOfCompany.NameOfUnit)) {
            TmpStringBuffer.append("; NameOfUnit: ");
            TmpStringBuffer.append(unitOfCompany.NameOfUnit);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany
          .DistrictCourtOfUnit)) {
            TmpStringBuffer.append("; DistrictCourtOfUnit: ");
            TmpStringBuffer.append(unitOfCompany.DistrictCourtOfUnit);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany
          .CommercialRegisterIDOfUnit)) {
            TmpStringBuffer.append("; CommercialRegisterIdOfUnit: ");
            TmpStringBuffer.append(unitOfCompany
            .CommercialRegisterIDOfUnit);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany
          .PlaceOfUnitHeadquarter)) {
            TmpStringBuffer.append("; PlaceOfUnitHeadquarter: ");
            TmpStringBuffer.append(unitOfCompany.PlaceOfUnitHeadquarter);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany
          .StreetOfUnitHeadquarter)) {
            TmpStringBuffer.append("; StreetOfUnitHeadquarter: ");
            TmpStringBuffer.append(unitOfCompany.StreetOfUnitHeadquarter);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany.NameOfParent)) {
            TmpStringBuffer.append("; NameOfParent: ");
            TmpStringBuffer.append(unitOfCompany.NameOfParent);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany
          .DistrictCourtOfParent)) {
            TmpStringBuffer.append("; DistrictCourtOfParent: ");
            TmpStringBuffer.append(unitOfCompany.DistrictCourtOfParent);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany
          .CommercialRegisterIDOfParent)) {
            TmpStringBuffer.append("; CommercialRegisterIdOfParent: ");
            TmpStringBuffer.append(unitOfCompany
            .CommercialRegisterIDOfParent);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany
          .PlaceOfParentHeadquarter)) {
            TmpStringBuffer.append("; PlaceOfParentHeadquarter: ");
            TmpStringBuffer.append(unitOfCompany
            .PlaceOfParentHeadquarter);
          }
          if (!Tools.stringIsNullOrEmpty(unitOfCompany
          .StreetOfParentHeadquarter)) {
            TmpStringBuffer.append("; StreetOfParentHeadquarter: ");
            TmpStringBuffer.append(unitOfCompany
            .StreetOfParentHeadquarter);
          }
        }
        break;
      }
      default: {
        TmpStringBuffer.append(Token);
        break;
      }
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getMostProbableTypeString() {
    
    return MOST_PROBABLE_TYPE_STRINGS[MostProbableType];
    
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
  
  public static boolean isPlaceholder(String pPlaceholder) {
    
    if (pPlaceholder.startsWith(NamedEntity.PLACEHOLDER_PREFIX)
    && pPlaceholder.endsWith(NamedEntity.PLACEHOLDER_SUFFIX)
    && pPlaceholder.lastIndexOf(NamedEntity.PLACEHOLDER_PREFIX) == 0) {
      return true;
    }
    else {
      return false;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String createPlaceholder(int pPlaceholderID) {
    
    StringBuffer myStringBuffer = new StringBuffer(1000);
    myStringBuffer.append(NamedEntity.PLACEHOLDER_PREFIX)
    .append(pPlaceholderID).append(NamedEntity.PLACEHOLDER_SUFFIX);
    
    return myStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String getPlaceholderID(String pPlaceholder) {
    
    if (NamedEntity.isPlaceholder(pPlaceholder)) {
      return pPlaceholder.substring(NamedEntity.PLACEHOLDER_PREFIX.length(),
      pPlaceholder.length() - NamedEntity.PLACEHOLDER_SUFFIX.length());
    }
    else {
      return "";
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static int getNamedEntityIndex(String pPlaceholder) {
    
    if (NamedEntity.isPlaceholder(pPlaceholder)) {
      return Tools.string2Int(
      pPlaceholder.substring(NamedEntity.PLACEHOLDER_PREFIX.length(),
      pPlaceholder.length() - NamedEntity.PLACEHOLDER_SUFFIX.length()));
    }
    else {
      return -1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String convertContainedPlaceholders(
  NamedEntityOwner pNamedEntityOwner, String pTokenizedLine) {
    
    StringTokenizer tokenizer = new StringTokenizer(pTokenizedLine);
    String token = null;
    ArrayList placeholders = new ArrayList();
    ArrayList features = new ArrayList();
    StringBuffer result = new StringBuffer(pTokenizedLine.length());
    
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      if (NamedEntity.isPlaceholder(token)) {
        placeholders.add(token);
      }
    }
    
    for (int i = 0; i < placeholders.size(); i++) {
      features.add(pNamedEntityOwner.getNamedEntity(NamedEntity
      .getNamedEntityIndex((String)placeholders.get(i))));
      token = ((NamedEntity)features.get(i)).getMostProbableDescription();
      if (i > 0 && result.length() > 0 && token != null) {
        result.append(", ");
      }
      if (token != null) {
        result.append(token);
      }
    }
    
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static NamedEntity[] getContainedNamedEntities(
  NamedEntityOwner pNamedEntityOwner, String pTokenizedLine) {
    
    StringTokenizer tokenizer = new StringTokenizer(pTokenizedLine);
    String token = null;
    ArrayList placeholders = new ArrayList();
    ArrayList features = new ArrayList();
    NamedEntity[] result = null;
    
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      if (NamedEntity.isPlaceholder(token)) {
        placeholders.add(token);
      }
    }
    
    for (int i = 0; i < placeholders.size(); i++) {
      features.add(pNamedEntityOwner.getNamedEntity(NamedEntity
      .getNamedEntityIndex((String)placeholders.get(i))));
    }
    
    result = new NamedEntity[features.size()];
    for (int i = 0; i <  features.size(); i++) {
      result[i] = (NamedEntity)features.get(i);
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static int getNumericTypeOfNE(String pStringTypeOfNE) {
    
    for (int i = 0; i < TYPE_DESCRIPTIONS.length; i++) {
      if (pStringTypeOfNE.equals(NamedEntity.TYPE_DESCRIPTIONS[i])) {
        return i;
      }
    }
    
    return NamedEntity.UNKNOWN;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}