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

package org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.diasdem.core.neex.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class StructuredDtdAssocRule implements Serializable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private String Antecedent = null;
  private String[] AntecedentArray = null;
  private String Consequent = null;
  private String[] ConsequentArray = null;
  private long AntecedentAbsSupport = 0L;
  private double AntecedentRelSupport = 0.0;
  private long AbsSupport = 0L;
  private double RelSupport = 0.0;
  private double Confidence = 0.0;
  private double Lift = 0.0;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;
  private transient StringTokenizer TmpStringTokenizer = null;
  private transient String TmpString = null;
  private transient Iterator TmpIterator = null;
  private transient TreeSet TmpTreeSet = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public StructuredDtdAssocRule() {

    Antecedent = null;
    AntecedentArray = null;
    Consequent = null;
    ConsequentArray = null;
    AntecedentAbsSupport = 0L;
    AntecedentRelSupport = 0.0;
    AbsSupport = 0L;
    RelSupport = 0.0;
    Confidence = 0.0;
    Lift = 0.0;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public StructuredDtdAssocRule(String pAntecedent, String pConsequent,
    long pAntecedentAbsSupport, double pAntecedentRelSupport,
    long pAbsSupport, double pRelSupport, double pConfidence, double pLift) {

    this.orderAndSetAntecedent(pAntecedent);
    this.orderAndSetConsequent(pConsequent);

    AntecedentAbsSupport = pAntecedentAbsSupport;
    AntecedentRelSupport = pAntecedentRelSupport;
    AbsSupport = pAbsSupport;
    RelSupport = pRelSupport;
    Confidence = pConfidence;
    Lift = pLift;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getAntecedent() { 
    return Antecedent; }
  public String[] getAntecedentArray() { 
    return AntecedentArray; }
  public int getAntecedentCardinality() { 
    return AntecedentArray.length; }
  public String getConsequent() { 
    return Consequent; }
  public String[] getConsequentArray() { 
    return ConsequentArray; }
  public int getConsequentCardinality() { 
    return ConsequentArray.length; }
  public long getAntecedentAbsSupport() { 
    return AntecedentAbsSupport; }
  public double getAntecedentRelSupport() { 
    return AntecedentRelSupport; }
  public long getAbsSupport() { 
    return AbsSupport; }
  public double getRelSupport() { 
    return RelSupport; }
  public double getConfidence() { 
    return Confidence; }
  public double getLift() { 
    return Lift; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setAntecedent(String pAntecedent) 
    { Antecedent = pAntecedent; }
  public void setAntecedentArray(String[] pAntecedentArray) 
    { AntecedentArray = pAntecedentArray; }
  public void setConsequent(String pConsequent) 
    { Consequent = pConsequent; }
  public void setConsequentArray(String[] pConsequentArray) 
    { ConsequentArray = pConsequentArray; }
  public void setAntecedentAbsSupport(long pAntecedentAbsSupport) 
    { AntecedentAbsSupport = pAntecedentAbsSupport; }
  public void setAntecedentRelSupport(double pAntecedentRelSupport) 
    { AntecedentRelSupport = pAntecedentRelSupport; }
  public void setAbsSupport(long pAbsSupport) 
    { AbsSupport = pAbsSupport; }
  public void setRelSupport(double pRelSupport) 
    { RelSupport = pRelSupport; }
  public void setConfidence(double pConfidence) 
    { Confidence = pConfidence; }
  public void setLift(double pLift) { 
    Lift = pLift; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("StructuredDtdAssocRule: ");
    TmpStringBuffer.append(Antecedent);
    TmpStringBuffer.append(" --> ");
    TmpStringBuffer.append(Consequent);
    TmpStringBuffer.append("; AntecedentAbsSupport=");
    TmpStringBuffer.append(AntecedentAbsSupport);
    TmpStringBuffer.append("; AntecedentRelSupport=");
    TmpStringBuffer.append(AntecedentRelSupport);
    TmpStringBuffer.append("; AbsSupport=");
    TmpStringBuffer.append(AbsSupport);
    TmpStringBuffer.append("; RelSupport=");
    TmpStringBuffer.append(RelSupport);
    TmpStringBuffer.append("; Confidence=");
    TmpStringBuffer.append(Confidence);
    TmpStringBuffer.append("; Lift=");
    TmpStringBuffer.append(Lift);
    
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
    TmpStringBuffer.append( Itemizer.stringToItem(Antecedent) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.stringToItem(Consequent) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.longToItem(AntecedentAbsSupport) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.doubleToItem(AntecedentRelSupport) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.longToItem(AbsSupport) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.doubleToItem(RelSupport) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.doubleToItem(Confidence) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.doubleToItem(Lift) );

    return TmpStringBuffer.toString();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String getCsvHeaderLine() { 

    return "Antecedent,Consequent,AntecedentAbsSupport,AntecedentRelSupprt," +
      "AbsSupport,RelSupport,Confidence,Lift";

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCsvValueLine() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append( Itemizer.stringToItem(Antecedent) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.stringToItem(Consequent) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.longToItem(AntecedentAbsSupport) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.doubleToItem(AntecedentRelSupport) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.longToItem(AbsSupport) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.doubleToItem(RelSupport) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.doubleToItem(Confidence) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.doubleToItem(Lift) );

    return TmpStringBuffer.toString();

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) throws NumberFormatException { 

    this.reset();
    String TmpAntecedent = null;
    String TmpConsequent = null;
    Itemizer itemizer = new Itemizer(pItemLine);

    try {
      TmpAntecedent = Itemizer.itemToString( itemizer.getNextItem() );
      TmpConsequent = Itemizer.itemToString( itemizer.getNextItem() );
      AntecedentAbsSupport = Itemizer.itemToLong( itemizer.getNextItem() );
      AntecedentRelSupport = Itemizer.itemToDouble( itemizer.getNextItem() );
      AbsSupport = Itemizer.itemToLong( itemizer.getNextItem() );
      RelSupport = Itemizer.itemToDouble( itemizer.getNextItem() );
      Confidence = Itemizer.itemToDouble( itemizer.getNextItem() );
      Lift = Itemizer.itemToDouble( itemizer.getNextItem() );
      this.orderAndSetAntecedent(TmpAntecedent);
      this.orderAndSetConsequent(TmpConsequent);
    }
    catch (NoSuchElementException e1) { 
      this.reset(); throw new NumberFormatException(); }
    catch (NumberFormatException e2) { 
      this.reset(); throw new NumberFormatException(); }
    catch (StringIndexOutOfBoundsException e2) { 
      this.reset(); throw new NumberFormatException(); }

  }  
 
  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromWumExportItemLine(String pItemLine) 
    throws NumberFormatException { 

    this.reset();
    String TmpAntecedent = null;
    String TmpConsequent = null;
    Itemizer itemizer = new Itemizer(pItemLine);

    // RelSupport of LHS item set as well as lift is not given
    try {
      TmpAntecedent = Itemizer.itemToString( itemizer.getNextItem() );
      TmpConsequent = Itemizer.itemToString( itemizer.getNextItem() );
      AntecedentAbsSupport = Itemizer.itemToLong( itemizer.getNextItem() );
      // AntecedentRelSupport = Itemizer.itemToDouble( itemizer.getNextItem() );
      AbsSupport = Itemizer.itemToLong( itemizer.getNextItem() );
      RelSupport = Itemizer.itemToDouble( itemizer.getNextItem() );
      Confidence = Itemizer.itemToDouble( itemizer.getNextItem() );
      // Lift = Itemizer.itemToDouble( itemizer.getNextItem() );
      // calculate RelSupport of LHS item set 
      AntecedentRelSupport = RelSupport * AntecedentAbsSupport / AbsSupport;
      this.orderAndSetAntecedent(TmpAntecedent);
      this.orderAndSetConsequent(TmpConsequent);
    }
    catch (NoSuchElementException e1) { 
      this.reset(); throw new NumberFormatException(); }
    catch (NumberFormatException e2) { 
      this.reset(); throw new NumberFormatException(); }
    catch (StringIndexOutOfBoundsException e2) { 
      this.reset(); throw new NumberFormatException(); }

  }
 
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {

    Antecedent = null;
    AntecedentArray = null;
    Consequent = null;
    ConsequentArray = null;
    AntecedentAbsSupport = 0L;
    AntecedentRelSupport = 0.0;
    AbsSupport = 0L;
    RelSupport = 0.0;
    Confidence = 0.0;
    Lift = 0.0;

  } 

  /* ########## ########## ########## ########## ########## ######### */

  private void orderAndSetAntecedent(String pAntecedent) {

    // ensure lexicographic ordering antecedent item set
    TmpStringTokenizer = new StringTokenizer(pAntecedent);
    TmpTreeSet = new TreeSet();
    while ( TmpStringTokenizer.hasMoreTokens() )
      TmpTreeSet.add( TmpStringTokenizer.nextToken() );
    AntecedentArray = new String[ TmpTreeSet.size() ];
    int i = 0;
    TmpIterator = TmpTreeSet.iterator();
    while ( TmpIterator.hasNext() ) {
      TmpString = (String)TmpIterator.next();
      if (i == 0)
        Antecedent = TmpString;
      else
        Antecedent += " " + TmpString;
      AntecedentArray[i++] = TmpString;
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  private void orderAndSetConsequent(String pConsequent) {

    // ensure lexicographic ordering consequent item set
    TmpStringTokenizer = new StringTokenizer(pConsequent);
    TmpTreeSet = new TreeSet();
    while ( TmpStringTokenizer.hasMoreTokens() )
      TmpTreeSet.add( TmpStringTokenizer.nextToken() );
    ConsequentArray = new String[ TmpTreeSet.size() ];
    int i = 0;
    TmpIterator = TmpTreeSet.iterator();
    while ( TmpIterator.hasNext() ) {
      TmpString = (String)TmpIterator.next();
      if (i == 0)
        Consequent = TmpString;
      else
        Consequent += " " + TmpString;
      ConsequentArray[i++] = TmpString;
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}