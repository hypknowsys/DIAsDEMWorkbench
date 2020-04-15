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

package org.hypknowsys.diasdem.core.neex.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.hypknowsys.diasdem.core.DIAsDEMtextUnit;
import org.hypknowsys.diasdem.core.default21.DefaultDIAsDEMtextUnit;
import org.hypknowsys.diasdem.core.neex.NamedEntity;
import org.hypknowsys.diasdem.core.neex.NamedEntityOwner;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class TestNamedEntityOwner implements NamedEntityOwner {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ArrayList InputText = null;
  private ArrayList ProcessedText = null;
  private ArrayList NamedEntities = null;
  
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
  
  public TestNamedEntityOwner() {
    
    // This file must contain tokenized text. File might contain
    // comment line starting with # which will not be load.
    
    InputText = new ArrayList();
    ProcessedText = new ArrayList();
    
    String testResource =
    "org/hypknowsys/diasdem/core/neex/util/TestNamedEntityText.txt";
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
      ClassLoader.getSystemResourceAsStream(testResource)));
      String line = reader.readLine();
      while (line != null) {
        if (!line.startsWith("#")) {
          InputText.add(new String(line));
          ProcessedText.add(new String(line));
        }
        line = reader.readLine();
      }
    }
    catch (IOException e) {
      System.err.println("Error: System resource " + testResource
      + " cannot be opened!");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ArrayList getInputText() {
    return InputText; }
  public ArrayList getProcessedText() {
    return ProcessedText; }
  public ArrayList getNamedEntities() {
    return NamedEntities; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("TestNamedEntityOwner: Text=\n\n");
    for (int i = 0; ProcessedText != null && i < ProcessedText.size()
    && InputText != null && i < InputText.size(); i++) {
      TmpStringBuffer.append(Tools.int2String(i));
      TmpStringBuffer.append(":I:");
      TmpStringBuffer.append((String)InputText.get(i));
      TmpStringBuffer.append("\n");
      TmpStringBuffer.append(Tools.int2String(i));
      TmpStringBuffer.append(":P:");
      TmpStringBuffer.append((String)ProcessedText.get(i));
      TmpStringBuffer.append("\n\n");
    }
    TmpStringBuffer.append("TestNamedEntityOwner: NamedEntities=\n\n");
    for (int i = 0; NamedEntities != null && i < NamedEntities.size(); i++) {
      TmpStringBuffer.append(NamedEntities.get(i));
      TmpStringBuffer.append("\n");
    }
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface NamedEntityOwner methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfInputTextUnits() {
    
    if (InputText != null) {
      return InputText.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getNumberOfProcessedTextUnits() {
    
    if (ProcessedText != null) {
      return ProcessedText.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceProcessedTextUnit(int pDiasdemTextUnitIndex,
  DIAsDEMtextUnit pDiasdemTextUnit) {
    
    if (ProcessedText != null && pDiasdemTextUnitIndex >= 0
    && pDiasdemTextUnitIndex < ProcessedText.size()) {
      ProcessedText.set(pDiasdemTextUnitIndex,
      pDiasdemTextUnit.getContentsAsString());
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceProcessedTextUnitFromString(int pDiasdemTextUnitIndex,
  String pDiasdemTextUnit) {
    
    if (ProcessedText != null && pDiasdemTextUnitIndex >= 0
    && pDiasdemTextUnitIndex < ProcessedText.size()) {
      ProcessedText.set(pDiasdemTextUnitIndex, pDiasdemTextUnit);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMtextUnit getProcessedTextUnit(int pTextUnitIndex) {
    
    
    if (ProcessedText != null && pTextUnitIndex >= 0
    && pTextUnitIndex < ProcessedText.size()) {
      return new DefaultDIAsDEMtextUnit((String)ProcessedText
      .get(pTextUnitIndex), 0);
    }
    else {
      return null;
    }
    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getInputTextUnitAsString(int pTextUnitIndex) {
    
    if (InputText != null && pTextUnitIndex >= 0
    && pTextUnitIndex < InputText.size()) {
      return (String)InputText.get(pTextUnitIndex);
    }
    else {
      return null;
    }
    
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getProcessedTextUnitAsString(int pTextUnitIndex) {
    
    if (ProcessedText != null && pTextUnitIndex >= 0
    && pTextUnitIndex < ProcessedText.size()) {
      return (String)ProcessedText.get(pTextUnitIndex);
    }
    else {
      return null;
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
  
  public static void main(String[] pOptions) {}
  
}