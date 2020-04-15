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

public class PersonNE extends CompositeNE {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String Name = null;
  protected String Surname = null;
  protected String MiddleInitial = null;
  protected String Forename = null;
  protected String Title = null;
  protected String Occupation = null;
  protected String DateOfBirth = null;
  protected String PlaceOfBirth = null;
  protected String Birthname = null;
  protected String PlaceOfResidence = null;
  protected String StreetOfResidence = null;
  protected String KnownAttributes = null;
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
  
  public PersonNE() {
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public PersonNE(String pSurname, String pForename, String pDateOfBirth) {
    
    this(null, pSurname, pForename, pDateOfBirth);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public PersonNE(String pName, String pSurname, String pForename,
  String pDateOfBirth) {
    
    // null settings required by this.isSimilar()
    if ((pName != null) && (! pName.equals(""))) {
      this.Name = pName;
    }
    else {
      this.Name = null;
    }
    if ((pSurname != null) && (! pSurname.equals(""))) {
      this.Surname = pSurname;
    }
    else {
      this.Surname = null;
    }
    if ((pForename != null) && (! pForename.equals(""))) {
      this.Forename = pForename;
    }
    else {
      this.Forename = null;
    }
    if ((pDateOfBirth != null) && (! pDateOfBirth.equals(""))) {
      this.DateOfBirth = pDateOfBirth;
    }
    else {
      this.DateOfBirth = null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public PersonNE(int pID, String pToken, String pPlaceholder,
  int pPossibleType) {
    
    super(pID, pToken, pPlaceholder, pPossibleType);
    this.Name = null;
    this.Surname = null;
    this.Forename = null;
    this.DateOfBirth = null;
    this.MiddleInitial = null;
    this.Title = null;
    this.Occupation = null;
    this.PlaceOfBirth = null;
    this.Birthname = null;
    this.PlaceOfResidence = null;
    this.KnownAttributes = null;
    this.StreetOfResidence = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public PersonNE(int pID, String pToken, String pPlaceholder,
  int pPossibleType, String pCanonical) {
    
    super(pID, pToken, pPlaceholder, pPossibleType, pCanonical);
    this.Name = null;
    this.Surname = null;
    this.Forename = null;
    this.DateOfBirth = null;
    this.MiddleInitial = null;
    this.Title = null;
    this.Occupation = null;
    this.PlaceOfBirth = null;
    this.Birthname = null;
    this.PlaceOfResidence = null;
    this.KnownAttributes = null;
    this.StreetOfResidence = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public PersonNE(int pID, String pToken, int pType, String pCanonical,
  String pName, String pSurname, String pForename, String pDateOfBirth,
  String pMiddleInitial, String pTitle , String pOccupation,
  String pPlaceOfBirth, String pBirthname, String pPlaceOfResidence,
  String pStreetOfResidence, String pKnownAttributes) {
    
    super(pID, pToken, null, pType, pCanonical);
    // null settings required by this.isSimilar()
    if ((pName != null) && (! pName.equals(""))) {
      this.Name = pName;
    }
    else {
      this.Name = null;
    }
    if ((pSurname != null) && (! pSurname.equals(""))) {
      this.Surname = pSurname;
    }
    else {
      this.Surname = null;
    }
    if ((pForename != null) && (! pForename.equals(""))) {
      this.Forename = pForename;
    }
    else {
      this.Forename = null;
    }
    if ((pDateOfBirth != null) && (! pDateOfBirth.equals(""))) {
      this.DateOfBirth = pDateOfBirth;
    }
    else {
      this.DateOfBirth = null;
    }
    if ((pMiddleInitial != null) && (! pMiddleInitial.equals(""))) {
      this.MiddleInitial = pMiddleInitial;
    }
    else {
      this.MiddleInitial = null;
    }
    if ((pTitle != null) && (! pTitle.equals(""))) {
      this.Title = pTitle;
    }
    else {
      this.Title = null;
    }
    if ((pOccupation != null) && (! pOccupation.equals(""))) {
      this.Occupation = pOccupation;
    }
    else {
      this.Occupation = null;
    }
    if ((pPlaceOfBirth != null) && (! pPlaceOfBirth.equals(""))) {
      this.PlaceOfBirth = pPlaceOfBirth;
    }
    else {
      this.PlaceOfBirth = null;
    }
    if ((pBirthname != null) && (! pBirthname.equals(""))) {
      this.Birthname = pBirthname;
    }
    else {
      this.Birthname = null;
    }
    if ((pPlaceOfResidence != null) && (! pPlaceOfResidence.equals(""))) {
      this.PlaceOfResidence = pPlaceOfResidence;
    }
    else {
      this.PlaceOfResidence = null;
    }
    if ((pStreetOfResidence != null) && (! pStreetOfResidence.equals(""))) {
      this.StreetOfResidence = pStreetOfResidence;
    }
    else {
      this.StreetOfResidence = null;
    }
    if ((pKnownAttributes != null) && (! pKnownAttributes.equals(""))) {
      this.KnownAttributes = pKnownAttributes;
    }
    else {
      this.KnownAttributes = null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public PersonNE(int pID, String pSurname, String pForename,
  String pDateOfBirth,
  String pMiddleInitial, String pTitle , String pOccupation,
  String pPlaceOfBirth, String pBirthname, String pPlaceOfResidence,
  String pStreetOfResidence, String pKnownAttributes) {
    
    super(pID, null, null, NamedEntity.PERSON, null);
    // null settings required by this.isSimilar()
    if ((pSurname != null) && (! pSurname.equals(""))) {
      this.Surname = pSurname;
    }
    else {
      this.Surname = null;
    }
    if ((pForename != null) && (! pForename.equals(""))) {
      this.Forename = pForename;
    }
    else {
      this.Forename = null;
    }
    if ((pDateOfBirth != null) && (! pDateOfBirth.equals(""))) {
      this.DateOfBirth = pDateOfBirth;
    }
    else {
      this.DateOfBirth = null;
    }
    this.MiddleInitial = pMiddleInitial;
    this.Title = pTitle;
    this.Occupation = pOccupation;
    this.PlaceOfBirth = pPlaceOfBirth;
    this.Birthname = pBirthname;
    this.PlaceOfResidence = pPlaceOfResidence;
    this.StreetOfResidence = pStreetOfResidence;
    this.KnownAttributes = pKnownAttributes;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public PersonNE(String pCanonical, String pSurname, String pForename,
  String pDateOfBirth,
  String pMiddleInitial, String pTitle , String pOccupation,
  String pPlaceOfBirth, String pBirthname, String pPlaceOfResidence,
  String pStreetOfResidence, String pKnownAttributes) {
    
    super(0, null, null, NamedEntity.PERSON, pCanonical);
    // null settings required by this.isSimilar()
    if ((pSurname != null) && (! pSurname.equals(""))) {
      this.Surname = pSurname;
    }
    else {
      this.Surname = null;
    }
    if ((pForename != null) && (! pForename.equals(""))) {
      this.Forename = pForename;
    }
    else {
      this.Forename = null;
    }
    if ((pDateOfBirth != null) && (! pDateOfBirth.equals(""))) {
      this.DateOfBirth = pDateOfBirth;
    }
    else {
      this.DateOfBirth = null;
    }
    this.MiddleInitial = pMiddleInitial;
    this.Title = pTitle;
    this.Occupation = pOccupation;
    this.PlaceOfBirth = pPlaceOfBirth;
    this.Birthname = pBirthname;
    this.PlaceOfResidence = pPlaceOfResidence;
    this.KnownAttributes = pKnownAttributes;
    this.CanonicalForm = true;
    this.StreetOfResidence = pStreetOfResidence;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getName() {
    return Name; }
  public String getSurname() {
    return Surname; }
  public String getMiddleInitial() {
    return MiddleInitial; }
  public String getForename() {
    return Forename; }
  public String getTitle() {
    return Title; }
  public String getOccupation() {
    return Occupation; }
  public String getDateOfBirth() {
    return DateOfBirth; }
  public String getPlaceOfBirth() {
    return PlaceOfBirth; }
  public String getBirthname() {
    return Birthname; }
  public String getPlaceOfResidence() {
    return PlaceOfResidence; }
  public String getStreetOfResidence() {
    return StreetOfResidence; }
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
    TmpStringBuffer.append("PersonNE: \"").append((Name != null
    ? Name : Forename + " " + Surname)).append("\", ID=")
    .append(ID).append(", PH=").append(this.Placeholder).append(", PT=")
    .append(getPossibleTypesPlaceholder()).append(", CF=")
    .append(this.isCanonicalForm() ? "true" : "false");
    ;
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getContent() {
    
    StringBuffer result = new StringBuffer(50);
    result.append(ID).append("").append(Canonical);
    return result.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getAttributeString() {
    
    if ((this.Token != null) && this.Token.equals("")) {
      this.Token = null;
    }
    if ((this.Canonical != null) && this.Canonical.equals("")) {
      this.Canonical = null;
    }
    if ((this.Surname != null) && this.Surname.equals("")) {
      this.Surname = null;
    }
    if ((this.Forename != null) && this.Forename.equals("")) {
      this.Forename = null;
    }
    if ((this.DateOfBirth != null) && this.DateOfBirth.equals("")) {
      this.DateOfBirth = null;
    }
    if ((this.MiddleInitial != null) && this.MiddleInitial.equals("")) {
      this.MiddleInitial = null;
    }
    if ((this.Title != null) && this.Title.equals("")) {
      this.Title = null;
    }
    if ((this.Occupation != null) && this.Occupation.equals("")) {
      this.Occupation = null;
    }
    if ((this.PlaceOfBirth != null) && this.PlaceOfBirth.equals("")) {
      this.PlaceOfBirth = null;
    }
    if ((this.Birthname != null) && this.Birthname.equals("")) {
      this.Birthname = null;
    }
    if ((this.PlaceOfResidence != null) && this.PlaceOfResidence.equals("")) {
      this.PlaceOfResidence = null;
    }
    if ((this.StreetOfResidence != null) && this.StreetOfResidence.equals("")) {
      this.StreetOfResidence = null;
    }
    if ((this.KnownAttributes != null) && this.KnownAttributes.equals("")) {
      this.KnownAttributes = null;
    }
    
    StringBuffer currentAttributes = new StringBuffer(1000);
    currentAttributes.append(ID).append("|")
    .append(Token).append("|")
    .append(this.getPossibleTypesString()).append("|")
    .append(Canonical).append("|")
    .append(this.Name).append("|")
    .append(this.Surname).append("|")
    .append(this.Forename).append("|")
    .append(this.DateOfBirth).append("|")
    .append(this.MiddleInitial).append("|")
    .append(this.Title).append("|")
    .append(this.Occupation).append("|")
    .append(this.PlaceOfBirth).append("|")
    .append(this.Birthname).append("|")
    .append(this.PlaceOfResidence).append("|")
    .append(this.StreetOfResidence).append("|")
    .append(this.KnownAttributes);
    
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
          NamedEntity.TYPE_DESCRIPTIONS[NamedEntity.PERSON])) {
            MostProbableType = NamedEntity.PERSON;
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
          this.Surname = currentToken;
          break;
        }
        case 6 : {
          this.Forename = currentToken;
          break;
        }
        case 7 : {
          this.DateOfBirth = currentToken;
          break;
        }
        case 8 : {
          this.MiddleInitial = currentToken;
          break;
        }
        case 9 : {
          this.Title = currentToken;
          break;
        }
        case 10 : {
          this.Occupation = currentToken;
          break;
        }
        case 11 : {
          this.PlaceOfBirth = currentToken;
        }
        case 12 : {
          this.Birthname = currentToken;
        }
        case 13 : {
          this.PlaceOfResidence = currentToken;
        }
        case 14 : {
          this.StreetOfResidence = currentToken;
        }
        case 15 : {
          this.KnownAttributes = currentToken;
        }
      }
      counter ++;
    }
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isSimilar(PersonNE pPersonNE) {
    
    // only NEEX 2.0
    
    boolean result = false;
    
    if (this.Surname != null && this.Surname.equals(pPersonNE.Surname)) {
      
      if ((this.DateOfBirth != null
      && this.DateOfBirth.equals(pPersonNE.DateOfBirth))
      || pPersonNE.DateOfBirth == null || this.DateOfBirth == null) {
        
        if (this.Forename == null || pPersonNE.Forename == null) {
          result = true;
        }
        else if (this.Forename != null && pPersonNE.Forename != null
        && this.Forename.equals(pPersonNE.Forename)) {
          result = true;
        }
        else if (this.Forename != null && pPersonNE.Forename != null
        && this.Forename.substring(0, 1).equals(pPersonNE.Forename.substring(
        0, 1))) {
          result = true;
        }
      }
    }
    
    return result;
    
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
  
  public static PersonNE createCanonicalForm(PersonNE[] pSimilarPersonNEs) {
    
    // only NEEX 2.0
    
    PersonNE onePerson = null;
    PersonNE[] similarPersonNEs = pSimilarPersonNEs;
    PersonNE buildPerson = null;
    
    String currentSurname = null;
    String currentForename = null;
    String currentDateOfBirth = null;
    String currentMiddleInitial = null;
    String currentTitle = null;
    String currentOccupation = null;
    String currentPlaceOfBirth = null;
    String currentBirthname = null;
    String currentPlaceOfResidence = null;
    String currentStreetOfResidence = null;
    String currentKnownAttributes = null;
    
    StringBuffer currentCanonical = new StringBuffer(1000);
    
    for (int i = 0; i < similarPersonNEs.length; i++) {
      
      onePerson = (PersonNE) similarPersonNEs[i];
      
      if (i < (similarPersonNEs.length - 1)) {
        currentCanonical.append(onePerson.ID).append(",");
      }
      else {
        currentCanonical.append(onePerson.ID);
      }
      
      if (onePerson.Surname != null
      && onePerson.Surname.length() > 0) {
        currentSurname = onePerson.Surname;
      }
      
      if (onePerson.Forename != null
      && onePerson.Forename.length() > 0) {
        if (onePerson.Forename.length() > 2) {
          currentForename = onePerson.Forename;
        }
        else  {
          currentForename = onePerson.Forename;
        }
      }
      
      if (onePerson.DateOfBirth != null
      && onePerson.DateOfBirth.length() > 0) {
        currentDateOfBirth = onePerson.DateOfBirth;
      }
      if (onePerson.MiddleInitial != null
      && onePerson.MiddleInitial.length() > 0) {
        currentMiddleInitial = onePerson.MiddleInitial;
      }
      if (onePerson.Title != null
      && onePerson.Title.length() > 0) {
        currentTitle = onePerson.Title;
      }
      if (onePerson.Occupation != null
      && onePerson.Occupation.length() > 0) {
        currentOccupation = onePerson.Occupation;
      }
      if (onePerson.PlaceOfBirth != null
      && onePerson.PlaceOfBirth.length() > 0) {
        currentPlaceOfBirth = onePerson.PlaceOfBirth;
      }
      if (onePerson.Birthname != null
      && onePerson.Birthname.length() > 0) {
        currentBirthname = onePerson.Birthname;
      }
      if (onePerson.PlaceOfResidence != null
      && onePerson.PlaceOfResidence.length() > 0) {
        currentPlaceOfResidence = onePerson.PlaceOfResidence;
      }
      if (onePerson.StreetOfResidence != null
      && onePerson.StreetOfResidence.length() > 0) {
        currentStreetOfResidence = onePerson.StreetOfResidence;
      }
      if (onePerson.KnownAttributes != null
      && onePerson.KnownAttributes.length() > 0) {
        currentKnownAttributes = onePerson.KnownAttributes;
      }
      
    }
    
    buildPerson = new PersonNE(currentCanonical.toString(),
    currentSurname, currentForename,
    currentDateOfBirth, currentMiddleInitial, currentTitle,
    currentOccupation, currentPlaceOfBirth, currentBirthname,
    currentPlaceOfResidence, currentStreetOfResidence,
    currentKnownAttributes);
    
    return buildPerson;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static ArrayList groupSimilarPersonNEs  (ArrayList pPersonVector) {
    
    PersonNE onePerson = null;
    ArrayList similarPersons = new ArrayList();
    ArrayList returnResult = new ArrayList();
    
    for (int i = 0; i < pPersonVector.size(); i ++) {
      
      PersonNE firstPerson = (PersonNE) pPersonVector.get(i);
      
      PersonNE newPerson = new PersonNE(
      (int) firstPerson.ID,
      (String) firstPerson.Surname,
      (String) firstPerson.Forename,
      (String) firstPerson.DateOfBirth,
      (String) firstPerson.MiddleInitial,
      (String) firstPerson.Title,
      (String) firstPerson.Occupation,
      (String) firstPerson.PlaceOfBirth,
      (String) firstPerson.Birthname,
      (String) firstPerson.PlaceOfResidence,
      (String) firstPerson.StreetOfResidence,
      (String) firstPerson.KnownAttributes);
      
      similarPersons.add(newPerson);
      
      if (i < (pPersonVector.size() - 1)) {
        for (int j = (i+1); j < pPersonVector.size(); j ++) {
          PersonNE controlPerson = (PersonNE) pPersonVector.get(j);
          
          onePerson = new PersonNE(
          (int) controlPerson.ID,
          (String) controlPerson.Surname,
          (String) controlPerson.Forename,
          (String) controlPerson.DateOfBirth,
          (String) controlPerson.MiddleInitial,
          (String) controlPerson.Title,
          (String) controlPerson.Occupation,
          (String) controlPerson.PlaceOfBirth,
          (String) controlPerson.Birthname,
          (String) controlPerson.PlaceOfResidence,
          (String) firstPerson.StreetOfResidence,
          (String) controlPerson.KnownAttributes);
          
          boolean result = newPerson.isSimilar(onePerson);
          
          if (result == true) {
            
            similarPersons.add(onePerson);
            pPersonVector.remove(j);
            j--;
          }
          
        }
      }
      
      PersonNE[] currentResult = new PersonNE[similarPersons.size()];
      for (int k = 0; k < similarPersons.size(); k ++) {
        currentResult[k] = (PersonNE) similarPersons.get(k);
      }
      similarPersons.clear();
      PersonNE resultNE = createCanonicalForm(currentResult);
      
      returnResult.add(resultNE);
      
    }
    
    return returnResult;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}