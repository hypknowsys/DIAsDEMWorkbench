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

import java.io.Serializable;
import java.util.NoSuchElementException;
import org.hypknowsys.diasdem.core.DIAsDEMpreliminaryDtdAttribute;
import org.hypknowsys.misc.io.Itemizer;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMpreliminaryDtdAttribute implements
DIAsDEMpreliminaryDtdAttribute, Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String Name = null;
  private String MostProbableType = null;
  private long AbsoluteSupport = 0;
  private double RelativeSupport = 0.0;
  
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
  
  public DefaultDIAsDEMpreliminaryDtdAttribute() {
    
    Name = null;
    MostProbableType = null;
    AbsoluteSupport = 0L;
    RelativeSupport = 0.0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMpreliminaryDtdAttribute(String pName,
  String pMostProbableType, long pAbsoluteSupport, double pRelativeSupport) {
    
    Name = pName;
    MostProbableType = pMostProbableType;
    AbsoluteSupport = pAbsoluteSupport;
    RelativeSupport = pRelativeSupport;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getName() {
    return Name; }
  public String getMostProbableType() {
    return MostProbableType; }
  public long getAbsoluteSupport() {
    return AbsoluteSupport; }
  public double getRelativeSupport() {
    return RelativeSupport; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setName(String pName) {
    Name = pName; }
  public void setMostProbableType(String pMostProbableType) {
    MostProbableType = pMostProbableType; }
  public void setAbsoluteSupport(long pAbsolueSupport) {
    AbsoluteSupport = pAbsolueSupport; }
  public void incrementAbsoluteSupport() {
    AbsoluteSupport++; }
  public void setRelativeSupport(double pRelativeSupport) {
    RelativeSupport = pRelativeSupport; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("UnstructuredDtdAttribute: Name=");
    TmpStringBuffer.append(Name);
    
    return TmpStringBuffer.toString();
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toItemLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(Itemizer.stringToItem(Name));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.stringToItem(MostProbableType));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.longToItem(AbsoluteSupport));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append(Itemizer.doubleToItem(RelativeSupport));
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String getCsvHeaderLine() {
    
    return "UnstructuredDtdAttribute,MostProbableType,"
    + "AbsoluteSupport,RelativeSupport";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCsvValueLine() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(Itemizer.stringToItem(Name));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.stringToItem(MostProbableType));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.longToItem(AbsoluteSupport));
    TmpStringBuffer.append(",");
    TmpStringBuffer.append(Itemizer.doubleToItem(RelativeSupport));
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) throws NumberFormatException {
    
    Name = null;
    Itemizer itemizer = new Itemizer(pItemLine);
    
    try {
      Name = itemizer.itemToString(itemizer.getNextItem());
      MostProbableType = itemizer.itemToString(itemizer.getNextItem());
      AbsoluteSupport = itemizer.itemToLong(itemizer.getNextItem());
      RelativeSupport = itemizer.itemToDouble(itemizer.getNextItem());
    }
    catch (NoSuchElementException e1) {
      this.reset(); throw new NumberFormatException();
    }
    catch (NumberFormatException e2) {
      this.reset(); throw new NumberFormatException();
    }
    catch (StringIndexOutOfBoundsException e2) {
      this.reset(); throw new NumberFormatException();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {
    
    Name = null;
    MostProbableType = null;
    AbsoluteSupport = 0L;
    RelativeSupport = 0.0;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}