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
import java.util.*;

/**
 * A MintPatternLayer simply contains the support value and pointers to 
 * ObservationNodes in the AggregateTree that match the MintTemplate. 
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MintPatternLayer implements Serializable {

  // ########## attributes ##########
  
  private long Support = 0;
  private Vector ObservationNodes = null;

  
  // ########## constructors ##########
  
/**
 * constructs an emtpy MintPatternLayer
 */  

  public MintPatternLayer() { ObservationNodes = new Vector(); }  

  // ########## mutator methods ##########
  
  public void setSupport(long pSupport) 
    { Support = pSupport; }
  public void increaseSupport(long pSupportIncrease) 
    { Support += pSupportIncrease; }
  
  // ########## accessor methods ##########
  
  public long getSupport() { return Support; }    
  public int getSize() { return ObservationNodes.size(); }
  
  // ########## standard methods ##########
  
  public String toString() {
        
    String vResult = "MintPatternLayer: ";
    for (int i = 0; i < ObservationNodes.size(); i++)
      vResult += " " + (ObservationNode)ObservationNodes.elementAt(i);
    
    return vResult;
  
  }  // toString()
  
  /**
   * returns the MintPatternLayer's ObservationNode given by pIndex
   * @param pIndex 0 <= pIndex < this.getSize()
   * @return corresponding ObservationNode or null if pIndex is out of 
   * valid range
   */
    
  public ObservationNode getObservationNode(int pIndex) {
  
    if ( (pIndex >= 0) && ( pIndex < this.getSize() ) )
      return (ObservationNode)ObservationNodes.elementAt(pIndex);
    else
      return null;
  
  }  // getObservationNode()
  
  /**
   * appends the given ObservationNode to the MintPatternLayer and increases 
   * the layer's support value by the ObservationNode's support value
   * @param pObservationNode ObservationNode to be added
   */
  
  public void appendObservationNode(ObservationNode pObservationNode) {
  
    ObservationNodes.add(pObservationNode);
    this.increaseSupport( pObservationNode.getSupport() );
  
  }  // appendObservationNode()
  
}  // class MintPatternLayer
