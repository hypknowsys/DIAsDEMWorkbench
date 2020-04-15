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

package org.hypknowsys.diasdem.core.default21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnit;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnits;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnitsLayer;
import org.hypknowsys.diasdem.core.neex.CompanyNE;
import org.hypknowsys.diasdem.core.neex.CompanyRelocationNE;
import org.hypknowsys.diasdem.core.neex.CompositeNE;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.hypknowsys.diasdem.core.neex.PersonNE;
import org.hypknowsys.diasdem.core.neex.DatePeriodNE;
import org.hypknowsys.diasdem.core.neex.AmountOfMoneyRangeNE;
import org.hypknowsys.diasdem.core.neex.PercentageRangeNE;
import org.hypknowsys.diasdem.core.neex.EquityStakeNE;
import org.hypknowsys.diasdem.core.neex.KeyFigureNE;
import org.hypknowsys.diasdem.core.neex.UnitOfCompanyNE;
import org.hypknowsys.misc.util.Tools;
import org.jdom.Element;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMtextUnitsLayer implements DIAsDEMtextUnitsLayer {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String TextUnitsDescription = null;
  private HashMap MetaData = null;
  private DefaultDIAsDEMtextUnits OriginalTextUnits = null;
  private DefaultDIAsDEMtextUnits ProcessedTextUnits = null;
  private ArrayList RollbackTextUnits = null;
  private ArrayList NamedEntities = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient DIAsDEMtextUnit TmpTextUnit = null;
  
  private transient String TmpName = null;
  private transient String TmpContent = null;
    
  private transient NamedEntity TmpNamedEntity = null;
  private transient String TmpNeID = null;
  private transient String TmpNeType  = null;
  private transient PersonNE TmpPerson = null;
  private transient CompanyNE TmpCompany = null;
  private transient CompanyRelocationNE TmpCompanyRelocation = null;
  private transient DatePeriodNE TmpDatePeriod = null;
  private transient AmountOfMoneyRangeNE TmpAmountOfMoneyRange = null;
  private transient PercentageRangeNE TmpPercentageRange = null;
  private transient EquityStakeNE TmpEquityStake = null;
  private transient KeyFigureNE TmpKeyFigure = null;
  private transient UnitOfCompanyNE TmpUnitOfCompany = null;
  private transient NamedEntity TmpNe = null;
        
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMtextUnitsLayer() {
    
    this.reset();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTextUnitsDescription() {
    return TextUnitsDescription; }
  public HashMap getMetaData() {
    return MetaData; }
  public DIAsDEMtextUnits getOriginalTextUnits() {
    return (DIAsDEMtextUnits)OriginalTextUnits; }
  public DIAsDEMtextUnits getProcessedTextUnits() {
    return (DIAsDEMtextUnits)ProcessedTextUnits; }
  public ArrayList getRollbackTextUnits() {
    return RollbackTextUnits; }
  public ArrayList getNamedEntities() {
    return NamedEntities; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTextUnitsDescription(String pTextUnitsDescription) {
    TextUnitsDescription = pTextUnitsDescription; }
  public void setMetaData(HashMap pMetaData) {
    MetaData = pMetaData; }
  public void setOriginalTextUnits(DIAsDEMtextUnits pOriginalTextUnits) {
    OriginalTextUnits = (DefaultDIAsDEMtextUnits)pOriginalTextUnits; }
  public void setProcessedTextUnits(DIAsDEMtextUnits pProcessedTextUnits) {
    ProcessedTextUnits = (DefaultDIAsDEMtextUnits)pProcessedTextUnits; }
  public void setRollbackTextUnits(ArrayList pRollbackTextUnits) {
    RollbackTextUnits = pRollbackTextUnits; }
  public void setNamedEntities(ArrayList pNamedEntities) {
    NamedEntities = pNamedEntities; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("DefaultDIAsDEMtextUnitsLayer");
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface DIAsDEMtextUnitsLayer methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfOriginalTextUnits() {
    
    if (OriginalTextUnits != null) {
      return OriginalTextUnits.getNumberOfTextUnits();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addOriginalTextUnit(DIAsDEMtextUnit pDiasdemTextUnit,
  boolean pAddProcessedTextUnitAsWell) {
    
    if (OriginalTextUnits != null) {
      if (pAddProcessedTextUnitAsWell) {
        TmpTextUnit = (DIAsDEMtextUnit)pDiasdemTextUnit.clone();
        TmpTextUnit.setBeginIndex(-1);
        TmpTextUnit.setEndIndex(-1);
        ProcessedTextUnits.addTextUnit(TmpTextUnit);
      }
      return OriginalTextUnits.addTextUnit(pDiasdemTextUnit);
    }
    else {
      return -1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceOriginalTextUnit(int pDiasdemTextUnitIndex,
  DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (OriginalTextUnits != null) {
      OriginalTextUnits.replaceTextUnit(pDiasdemTextUnitIndex,
      pDiasdemTextUnit);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnit getOriginalTextUnit(int pTextUnitIndex) {
    
    if (OriginalTextUnits != null) {
      return OriginalTextUnits.getTextUnit(pTextUnitIndex);
    }
    else {
      return null;
    }
    
  }
  
  public void resetOriginalTextUnits() {
    
    OriginalTextUnits = new DefaultDIAsDEMtextUnits();
    ProcessedTextUnits = new DefaultDIAsDEMtextUnits();
    RollbackTextUnits = new ArrayList();
    NamedEntities = new ArrayList();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfProcessedTextUnits() {
    
    if (ProcessedTextUnits != null) {
      return ProcessedTextUnits.getNumberOfTextUnits();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addProcessedTextUnit(DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (ProcessedTextUnits != null) {
      return ProcessedTextUnits.addTextUnit(pDiasdemTextUnit);
    }
    else {
      return -1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceProcessedTextUnit(int pDiasdemTextUnitIndex,
  DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (ProcessedTextUnits != null) {
      ProcessedTextUnits.replaceTextUnit(pDiasdemTextUnitIndex,
      pDiasdemTextUnit);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnit getProcessedTextUnit(int pTextUnitIndex) {
    
    if (ProcessedTextUnits != null) {
      return ProcessedTextUnits.getTextUnit(pTextUnitIndex);
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void resetProcessedTextUnits() {
    
    ProcessedTextUnits = new DefaultDIAsDEMtextUnits();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void backupProcessedTextUnits(int pProcessedTextUnitsRollbackOption) {
    
    if (ProcessedTextUnits == null) {
      return;
    }
    
    switch (pProcessedTextUnitsRollbackOption) {
      case DIAsDEMtextUnitsLayer.LIMITED_ROLLBACK_ENABLED: {
        if (RollbackTextUnits == null) {
          RollbackTextUnits = new ArrayList();          
        }
        if (RollbackTextUnits.size() == 0) {
          RollbackTextUnits.add((DIAsDEMtextUnits)ProcessedTextUnits.clone());
        }
        else {
          RollbackTextUnits.set(RollbackTextUnits.size() - 1,
          (DIAsDEMtextUnits)ProcessedTextUnits.clone());
        }
        break;
      }
      case DIAsDEMtextUnitsLayer.FULL_ROLLBACK_ENABLED: {
        if (RollbackTextUnits == null) {
          RollbackTextUnits = new ArrayList();
        }
        RollbackTextUnits.add((DIAsDEMtextUnits)ProcessedTextUnits.clone());
        break;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void rollbackProcessedTextUnits(int pRollbackTextUnitsID) {
    
    if (RollbackTextUnits != null && pRollbackTextUnitsID >= 0
    && pRollbackTextUnitsID < RollbackTextUnits.size()
    && ProcessedTextUnits != null) {
      ProcessedTextUnits = (DefaultDIAsDEMtextUnits)((DefaultDIAsDEMtextUnits)
      RollbackTextUnits.get(pRollbackTextUnitsID)).clone();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getMaxRollbackTextUnitsID() {
    
    if (RollbackTextUnits != null && RollbackTextUnits.size() > 0) {
      return RollbackTextUnits.size() - 1;
    }
    else {
      return -1;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void updateNamedEntity(int pNamedEntityIndex, int pNewPossibleType) {
    
    if (NamedEntities == null || pNamedEntityIndex < 0
    || pNamedEntityIndex >= NamedEntities.size()) {
      System.err.println("[DefaultDIAsDEMtextUnitsLayer] NamedEntity cannot "
      + "be updated at index " + pNamedEntityIndex + "!");
    }
    else {
      this.getNamedEntity(pNamedEntityIndex).addPossibleType(pNewPossibleType);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNextNamedEntityIndex() {
    
    return this.getNumberOfNamedEntities();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfNamedEntities() {
    
    if (NamedEntities != null) {
      return NamedEntities.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addNamedEntity(NamedEntity pNamedEntity) {
    
    if (pNamedEntity == null) {
      return -1;
    }
    else {
      int newNamedEntityIndex = this.getNumberOfNamedEntities();
      if (NamedEntities == null) {
        NamedEntities = new ArrayList();
      }
      NamedEntities.add(pNamedEntity);
      return newNamedEntityIndex;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceNamedEntity(int pNamedEntityIndex,
  NamedEntity pNamedEntity) {
    
    if (pNamedEntity == null || NamedEntities == null
    || pNamedEntityIndex >= this.getNumberOfNamedEntities()
    || pNamedEntityIndex < 0) {
      System.err.println("[DefaultDIAsDEMtextUnitsLayer] NamedEntity cannot "
      + "be replaced at index " + pNamedEntityIndex + "!");
    }
    else {
      NamedEntities.set(pNamedEntityIndex, pNamedEntity);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public NamedEntity getNamedEntity(int pNamedEntityIndex) {
    
    if (NamedEntities == null || pNamedEntityIndex < 0
    || pNamedEntityIndex >= this.getNumberOfNamedEntities()) {
      return null;
    }
    else {
      return (NamedEntity)NamedEntities.get(pNamedEntityIndex);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void resetNamedEntities() {
    
    NamedEntities = new ArrayList();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public Element getAsJDomElement(int pTextUnitsLayerID) {
    
    Element rootJdomElement = new Element(
    "TextUnitsLayer");
    rootJdomElement.setAttribute("TextUnitsLayerID",
    Tools.int2String(pTextUnitsLayerID));
    rootJdomElement.setAttribute("TextUnitsDescription", TextUnitsDescription);
    
    Iterator iterator = MetaData.keySet().iterator();
    TmpName = null;
    TmpContent = null;
    Element metaDataElement = null;
    while (iterator.hasNext()) {
      TmpName = (String)iterator.next();
      TmpContent = (String)MetaData.get(TmpName);
      metaDataElement = new Element("MetaData");
      metaDataElement.addContent(new Element("Name")
      .setText(TmpName));
      metaDataElement.addContent(new Element("Content")
      .setText(TmpContent));
      rootJdomElement.addContent(metaDataElement);
    }
    
    if (OriginalTextUnits != null && OriginalTextUnits
    .getNumberOfTextUnits() > 0) {
      rootJdomElement.addContent(OriginalTextUnits
      .getAsJDomElement("OriginalTextUnits", "OriginalTextUnit"));
    }
    else {
      // OriginalTextUnits cannot remain empty
      rootJdomElement.addContent((new DefaultDIAsDEMtextUnits())
      .getAsJDomElement("OriginalTextUnits", "OriginalTextUnit"));
    }
    
    if (ProcessedTextUnits != null && ProcessedTextUnits
    .getNumberOfTextUnits() > 0) {
      rootJdomElement.addContent(ProcessedTextUnits
      .getAsJDomElement("ProcessedTextUnits", "ProcessedTextUnit"));
    }
    
    Element rollbackJdomElement = null;
    if (RollbackTextUnits != null && RollbackTextUnits.size() > 0) {
      for (int i = 0; i < RollbackTextUnits.size(); i++) {
        rollbackJdomElement = ((DefaultDIAsDEMtextUnits)RollbackTextUnits
        .get(i)).getAsJDomElement("RollbackTextUnits", "ProcessedTextUnit");
        rollbackJdomElement.setAttribute("RollbackID", Tools.int2String(i));
        rootJdomElement.addContent(rollbackJdomElement);
      }
    }
    
    if (NamedEntities != null && NamedEntities.size() > 0) {
      Element namedEntitiesElement = new Element(
      "NamedEntities");
      Element namedEntityElement = null;
      TmpNamedEntity = null;
      for (int i = 0; i < NamedEntities.size(); i++) {
        TmpNamedEntity = (NamedEntity)NamedEntities.get(i);
        namedEntityElement = new Element("NamedEntity");
        namedEntityElement.setAttribute("NeID", TmpNamedEntity
        .getID() + "");
        namedEntityElement.setAttribute("NeType", TmpNamedEntity
        .getPossibleTypesString());
        if (TmpNamedEntity instanceof CompositeNE) {
          if (TmpNamedEntity instanceof PersonNE) {
            namedEntityElement.setText(((PersonNE)TmpNamedEntity)
            .getAttributeString());
          }
          else if (TmpNamedEntity instanceof CompanyNE) {
            namedEntityElement.setText(((CompanyNE)TmpNamedEntity)
            .getAttributeString());
          }
          else if (TmpNamedEntity instanceof CompanyRelocationNE) {
            namedEntityElement.setText(((CompanyRelocationNE)TmpNamedEntity)
            .getAttributeString());
          }
          else if (TmpNamedEntity instanceof DatePeriodNE) {
            namedEntityElement.setText(((DatePeriodNE)TmpNamedEntity)
            .getAttributeString());
          }
          else if (TmpNamedEntity instanceof AmountOfMoneyRangeNE) {
            namedEntityElement.setText(((AmountOfMoneyRangeNE)TmpNamedEntity)
            .getAttributeString());
          }
          else if (TmpNamedEntity instanceof PercentageRangeNE) {
            namedEntityElement.setText(((PercentageRangeNE)TmpNamedEntity)
            .getAttributeString());
          }
          else if (TmpNamedEntity instanceof EquityStakeNE) {
            namedEntityElement.setText(((EquityStakeNE)TmpNamedEntity)
            .getAttributeString());
          }
          else if (TmpNamedEntity instanceof KeyFigureNE) {
            namedEntityElement.setText(((KeyFigureNE)TmpNamedEntity)
            .getAttributeString());
          }
          else if (TmpNamedEntity instanceof UnitOfCompanyNE) {
            namedEntityElement.setText(((UnitOfCompanyNE)TmpNamedEntity)
            .getAttributeString());
          }
          else {
            // This erroneous case must be avoided!
            namedEntityElement.setText(TmpNamedEntity.getToken());
          }
        }
        else {
          namedEntityElement.setText(TmpNamedEntity.getToken());
        }
        namedEntitiesElement.addContent(namedEntityElement);
      }
      rootJdomElement.addContent(namedEntitiesElement);
    }
    
    return rootJdomElement;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFromJDomElement(Element pJDomElement) {
    
    this.reset();
    
    if (pJDomElement.getAttributeValue("TextUnitsDescription") != null) {
      TextUnitsDescription = pJDomElement
      .getAttributeValue("TextUnitsDescription");
    }
    
    // assuming that pJDomElement has been validated against DTD
    TmpName = null;
    TmpContent = null;
    Element myJdomElement = null;
    List children = pJDomElement.getChildren("MetaData");
    for (int i = 0; i < children.size(); i++) {
      if (children.get(i) instanceof Element) {
        myJdomElement = (Element)children.get(i);
        TmpName = myJdomElement.getChildTextTrim("Name");
        TmpContent = myJdomElement.getChildTextTrim("Content");
        MetaData.put(TmpName, TmpContent);
      }
    }
    
    myJdomElement = pJDomElement.getChild("OriginalTextUnits");
    if (myJdomElement != null) {
      OriginalTextUnits.setFromJDomElement(myJdomElement);
    }
    
    myJdomElement = pJDomElement.getChild("ProcessedTextUnits");
    if (myJdomElement != null) {
      ProcessedTextUnits.setFromJDomElement(myJdomElement);
    }
    
    children = pJDomElement.getChildren("RollbackTextUnits");
    if (children != null) {
      DefaultDIAsDEMtextUnits rollbackTextUnits = null;
      RollbackTextUnits = new ArrayList(children.size());
      for (int i = 0; i < children.size(); i++) {
        if (children.get(i) instanceof Element) {
          myJdomElement = (Element)children.get(i);
          rollbackTextUnits = new DefaultDIAsDEMtextUnits();
          rollbackTextUnits.setFromJDomElement(myJdomElement);
          RollbackTextUnits.add(rollbackTextUnits);
        }
      }
    }
    
    if (pJDomElement.getChild("NamedEntities") != null) {
      children = pJDomElement.getChild("NamedEntities")
      .getChildren("NamedEntity");
      if (children != null) {
        TmpNamedEntity = null;
        TmpNeID = null;
        TmpNeType  = null;
        TmpPerson = null;
        TmpCompany = null;
        TmpCompanyRelocation = null;
        TmpDatePeriod = null;
        TmpAmountOfMoneyRange = null;
        TmpPercentageRange = null;
        TmpEquityStake = null;
        TmpKeyFigure = null;
        TmpUnitOfCompany = null;
        TmpNe = null;
        for (int i = 0; i < children.size(); i++) {
          if (children.get(i) instanceof Element) {
            myJdomElement = (Element)children.get(i);
            TmpNeID = myJdomElement.getAttributeValue("NeID");
            TmpNeType = myJdomElement.getAttributeValue("NeType");
            if (TmpNeType.equals("person")) {
              TmpPerson = new PersonNE(Tools.string2Int(TmpNeID), null,
              NamedEntity.PLACEHOLDER_PREFIX + TmpNeID.trim()
              + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity.PERSON);
              TmpPerson.setAttributeString(myJdomElement.getTextTrim());
              NamedEntities.add(TmpPerson);
            }
            else if (TmpNeType.equals("company")) {
              TmpCompany = new CompanyNE(Tools.string2Int(TmpNeID), null,
              NamedEntity.PLACEHOLDER_PREFIX + TmpNeID.trim()
              + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity.COMPANY);
              TmpCompany.setAttributeString(myJdomElement.getTextTrim());
              NamedEntities.add(TmpCompany);
            }
            else if (TmpNeType.equals("company_relocation")) {
              TmpCompanyRelocation = new CompanyRelocationNE(
              Tools.string2Int(TmpNeID),
              null, NamedEntity.PLACEHOLDER_PREFIX + TmpNeID.trim()
              + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity.COMPANY_RELOCATION);
              TmpCompanyRelocation.setAttributeString(myJdomElement
              .getTextTrim());
              NamedEntities.add(TmpCompanyRelocation);
            }
            else if (TmpNeType.equals("date_period")) {
              TmpDatePeriod = new DatePeriodNE(Tools.string2Int(TmpNeID),
              null, NamedEntity.PLACEHOLDER_PREFIX + TmpNeID.trim()
              + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity.DATE_PERIOD);
              TmpDatePeriod.setAttributeString(myJdomElement.getTextTrim());
              NamedEntities.add(TmpDatePeriod);
            }
            else if (TmpNeType.equals("amount_of_money_range")) {
              TmpAmountOfMoneyRange = new AmountOfMoneyRangeNE(
              Tools.string2Int(TmpNeID), null, NamedEntity.PLACEHOLDER_PREFIX 
              + TmpNeID.trim() + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity
              .AMOUNT_OF_MONEY_RANGE);
              TmpAmountOfMoneyRange.setAttributeString(myJdomElement
              .getTextTrim());
              NamedEntities.add(TmpAmountOfMoneyRange);
            }
            else if (TmpNeType.equals("percentage_range")) {
              TmpPercentageRange = new PercentageRangeNE(
              Tools.string2Int(TmpNeID), null, NamedEntity.PLACEHOLDER_PREFIX 
              + TmpNeID.trim() + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity
              .PERCENTAGE_RANGE);
              TmpPercentageRange.setAttributeString(myJdomElement
              .getTextTrim());
              NamedEntities.add(TmpPercentageRange);
            }
            else if (TmpNeType.equals("equity_stake")) {
              TmpEquityStake = new EquityStakeNE(
              Tools.string2Int(TmpNeID), null, NamedEntity.PLACEHOLDER_PREFIX 
              + TmpNeID.trim() + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity
              .EQUITY_STAKE);
              TmpEquityStake.setAttributeString(myJdomElement.getTextTrim());
              NamedEntities.add(TmpEquityStake);
            }
            else if (TmpNeType.equals("key_figure")) {
              TmpKeyFigure = new KeyFigureNE(
              Tools.string2Int(TmpNeID), null, NamedEntity.PLACEHOLDER_PREFIX 
              + TmpNeID.trim() + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity
              .KEY_FIGURE);
              TmpKeyFigure.setAttributeString(myJdomElement.getTextTrim());
              NamedEntities.add(TmpKeyFigure);
            }
            else if (TmpNeType.equals("unit_of_company")) {
              TmpUnitOfCompany = new UnitOfCompanyNE(
              Tools.string2Int(TmpNeID), null, NamedEntity.PLACEHOLDER_PREFIX 
              + TmpNeID.trim() + NamedEntity.PLACEHOLDER_SUFFIX, NamedEntity
              .UNIT_OF_COMPANY);
              TmpUnitOfCompany.setAttributeString(myJdomElement.getTextTrim());
              NamedEntities.add(TmpUnitOfCompany);
            }
            else {
              TmpNe = new NamedEntity(Tools.string2Int(TmpNeID),
              myJdomElement.getTextTrim(),
              NamedEntity.PLACEHOLDER_PREFIX + TmpNeID.trim()
              + NamedEntity.PLACEHOLDER_SUFFIX, TmpNeType);
              NamedEntities.add(TmpNe);
            }
          }
        }
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {
    
    TextUnitsDescription = "";
    MetaData = new HashMap();
    OriginalTextUnits = new DefaultDIAsDEMtextUnits();
    ProcessedTextUnits = new DefaultDIAsDEMtextUnits();
    RollbackTextUnits = new ArrayList();
    NamedEntities = new ArrayList();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String[] pOptions) {}
  
}