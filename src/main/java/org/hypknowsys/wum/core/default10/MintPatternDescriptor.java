/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.core.default10;

import java.io.*;

/**
 * MintPatternDescriptors are used by the MintQueryProcesser to generate and
 * evalute bindings of query's variables. They are also used to generate 
 * output trees and patterns. A MintPatternDescriptor contains a Pattern that 
 * is an instance of Trail: Each PageOccurrence in this Pattern corresponds to
 * and therefore is exactly one binding for one variable in the MINT query. 
 * Its first (second, last) PageOccurrence corresponds to the first (second, 
 * last) variable in the MINT query's MintTemplate expression. The 
 * MintPatternDescriptor's and therefore the Pattern's Length are equal to the 
 * number of variables in the template expression. For each PageOccurrence in 
 * the Pattern exists a corresponding MintPatternLayer
 * containing its support value and pointers to ObservationNodes in the
 * AggregateTree that match the MintTemplate. <p>
 * 
 * Modified by kwinkler 11/1999: method toStringUrl() added <p>
 *
 * Modified by kwinkler 08/2000: attribute PatternOutput added
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */


  public class MintPatternDescriptor implements Serializable {


  // ########## attributes ##########
  
  private Trail Pattern = null;
  private int Length = 0;
  private MintPatternLayer[] Layers = null;

  private MintPatternOutput PatternOutput = null;
  
  // ########## constructors ##########
  
  /**
   * constructs an empty MintPatternDescriptor of the given length
   * @param pLength length of the MintPatternDescriptor and therefore the 
   * number of variables in the corresponding MintTemplate expression
   */  

  public MintPatternDescriptor(int pLength) {
  
    Length = pLength;
    Layers = new MintPatternLayer[ Length ];
    for (int i = 0; i < Layers.length; i++) Layers[i] = new MintPatternLayer();
  
    PatternOutput = null;
  
  }  
  
  // ########## mutator methods ##########
  
  public void setPattern(Trail pPattern) 
    { Pattern = pPattern; }
  public void setLayer(int pIndex, MintPatternLayer pLayer) 
    { Layers[pIndex] = pLayer; }    

  public void setPatternOutput(MintPatternOutput pPatternOutput) 
    { PatternOutput = pPatternOutput; }

  // ########## accessor methods ##########
  
  public Trail getPattern() { return Pattern; }
  public int getLength() { return Length; }

  public MintPatternOutput getPatternOutput() { return PatternOutput; }
  
  // ########## standard methods ##########
  
  public String toString() {
        
    String result =  "";
    for (int i = 0; i < Layers.length; i++) {
      if (i > 0) result += ", ";
      result += "[" + Pattern.getPageOccurrence(i).getPageID() + ";";
      result += Pattern.getPageOccurrence(i).getOccurrence() + ";";
      result += Layers[i].getSupport() + "]";  
      // "]={" + Layers[i].toString() + "}";       
    }
    
    return result;
  
  }  // toString()  
  
  /**
   * returns the Pattern's PageOccurrence at the given position, example:
   * getPageOccurrence(0) returns the binding of the first variable in the
   * corresponding MintTemplate expression
   * @param pIndex 0 <= pIndex < this.getLength()
   * @return corresponding PageOccurrence or null if pIndex is out of 
   * valid range
   */
    
  public PageOccurrence getPageOccurrence(int pIndex) { 
  
    if ( (pIndex >= 0) && ( pIndex < Pattern.getSize() ) )
      return Pattern.getPageOccurrence(pIndex);
    else
      return null;
    
  }
 
  /**
   * returns the the support value of the given MintPatternLayer, example:
   * getSupport(0) returns the support value of the first variable's binding 
   * in the corresponding MintTemplate expression
   * @param pIndex 0 <= pIndex < this.getLength()
   * @return corresponding support or 0L if pIndex is out of valid range
   */
  
  public long getSupport(int pIndex) {
  
    if ( (pIndex >= 0) && ( pIndex < Layers.length ) )
      return Layers[pIndex].getSupport();
    else
      return 0L;
   
  }
  
  /**
   * returns the given MintPatternLayer, example: getLayer(0) returns the 
   * MintPatternLayer of the first variable's binding in the corresponding 
   * MintTemplate expression
   * @param pIndex 0 <= pIndex < this.getLength()
   * @return corresponding MintPatternLayer or null if pIndex is out of
   * valid range
   */  
  
  public MintPatternLayer getLayer(int pIndex){
  
    if ( (pIndex >= 0) && ( pIndex < Layers.length ) )
      return Layers[pIndex];
    else
      return null;
   
  }
  
  /**
   * returns the subpattern of Pattern that begins with 
   * this.getPageOccurrence(pStartIndex) and ends with 
   * this.getPageOccurrence(pEndeIndex), example:
   * getSubPattern(0, 2) returns a pattern (Trail) that starts of the first 
   * variable's binding and ends with the third variable's binding in the 
   * corresponding MintTemplate expression
   * @param pStartIndex 0 <= pStartIndex <= pEndIndex()
   * @param pEndIndex pStartIndex <= pEndIndex < this.getLength()
   * @return corresponding pattern (Trail) or null if indexes are out of 
   * valid range
   */  
   
  public Trail getSubPattern(int pStartIndex, int pEndIndex) {
  
    if ( (pStartIndex >= 0)  && (pStartIndex <= pEndIndex) && 
      (pEndIndex < Pattern.getSize()) ) {
      Trail result = new Trail();
      for (int i = pStartIndex; i <= pEndIndex; i++) {
        if (i == pStartIndex)
          result.setFirstPageOccurrence( Pattern.getPageOccurrence(i) );
        else
          result.setNextPageOccurrence( Pattern.getPageOccurrence(i) );
      }
      return result;
    }
    else
      return null;      
  
  }  // getSubPattern()
    
  /**
   * appends the given ObservationNode to the specified MintPatternLayer,
   * simply calls MintPatternLayer.append(.)
   * @param pObservationNode ObservationNode to append
   * @param pLayer 0 <= pLayer < this.getLength(), no validity check is execute
   * @see wum.objects.MintPatternLayer
   */
  
  public void appendObservationNode(ObservationNode pObservationNode, 
    int pLayer) {
    
    Layers[pLayer].appendObservationNode(pObservationNode);
  
  }  // addObservationNodeToLayer()

  /**
   * @return Trail of PatternDescriptor with PageUrls instead of PageIds
   */

  public String toStringUrl(MiningBase pMiningBase) {
        
    String result =  "";
    for (int i = 0; i < Layers.length; i++) {
      if (i > 0) result += ", ";
      result += "[" + Pattern.getPageOccurrence(i).getPageID() + ";" + 
        ( pMiningBase.getPage( 
        Pattern.getPageOccurrence(i).getPageID() ) ).getUrl() + ";";
      result += Pattern.getPageOccurrence(i).getOccurrence() + ";";
      result += Layers[i].getSupport() + "]";       
    }
    
    return result;
  
  }  // toStringURL()  


}  // class MintPatternDescriptor
