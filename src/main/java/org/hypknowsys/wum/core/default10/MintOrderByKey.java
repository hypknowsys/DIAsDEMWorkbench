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

import java.util.*;

/**
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MintOrderByKey implements Comparable, Comparator {

  // ########## attributes ##########

  private Comparable[] SortKeys = null;
  private int[] Ordering = null;
  private Object Item = null;
  
  public static final int UNKNOWN = 0;
  public static final int ASC = 1;
  public static final int DESC = -1;

  public MintOrderByKey() {
  
    SortKeys = null;
    Ordering = null;
    Item = null;

  }

  public MintOrderByKey(Comparable[] pSortKeys, Object pItem) {
  
    SortKeys = pSortKeys;
    Ordering = new int[SortKeys.length];
    for (int i = 0; i < SortKeys.length; i++)
      Ordering[i] = ASC;
    Item = pItem;

  }

  public MintOrderByKey(Comparable[] pSortKeys, int[] pOrdering, 
    Object pItem) {
  
    SortKeys = pSortKeys;
    Ordering = pOrdering;
    Item = pItem;

  }

  public MintOrderByKey(Comparable[] pSortKeys, int[] pOrdering) {
  
    // simply store key values for use in java.util.Map
    SortKeys = pSortKeys;
    Ordering = pOrdering;
    Item = null;

  }

  public Comparable[] getSortKeys() { return SortKeys; }
  public int[] getOrdering() { return Ordering; }
  public Object getItem() { return Item; }

  public String toString() {

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < SortKeys.length; i++) {
      result.append( SortKeys[i].toString() );
      result.append( " - " );
    }
    result.append( " - " );
    result.append( Item.toString() );

    return result.toString();

  }

  public boolean equals(Object pObject) {

    // pObject must be an instance of MintOrderByKey 
    // compare SortKeys, Ordering Item is omitted

    MintOrderByKey compare = null;
   
    if (pObject == null)
      return false;

    try {
      compare = (MintOrderByKey)pObject;
    }
    catch (ClassCastException e) {
      return false;
    }     

    if (SortKeys.length != compare.getSortKeys().length )
      return false;

    for (int i = 0; i < SortKeys.length; i++)
      if ( ! SortKeys[i].equals( compare.getSortKeys()[i] ) )
        return false;

    return true;

  }

  public int compareTo(Object pObject) {

    // pObject must be an instance of MintOrderByKey 
    // compare SortKeys, Ordering and Item are omitted


    if (pObject == null)
      throw new ClassCastException();

    MintOrderByKey compare = (MintOrderByKey)pObject;

    int result = 0;
    for (int i = 0; i < Math.min(SortKeys.length, 
      compare.getSortKeys().length); i++) {
      result = SortKeys[i].compareTo( compare.getSortKeys()[i] );
      if (result != 0)
        // ASC vs. DESC 
        return result * Ordering[i]; 
    }

    if (SortKeys.length < compare.getSortKeys().length)
      // ASC vs. DESC
      return -1 * Ordering[ SortKeys.length ];
    else
     if (SortKeys.length > compare.getSortKeys().length)
       // ASC vs. DESC
       return 1 * Ordering[ compare.getSortKeys().length ];
     else
       return 0;

  }

  public int compare(Object pMyself, Object pObject) {

    // pObject must be an instance of MintOrderByKey 
    // compare SortKeys, Ordering and Item are omitted;
    // both objects must be ordered using the same Ordering


    if ( (pObject == null) || (pMyself == null) )
      throw new ClassCastException();

    MintOrderByKey myself = (MintOrderByKey)pMyself;
    MintOrderByKey compare = (MintOrderByKey)pObject;

    int result = 0;
    for (int i = 0; i < Math.min(myself.getSortKeys().length, 
      compare.getSortKeys().length); i++) {
      result = myself.getSortKeys()[i].compareTo( compare.getSortKeys()[i] );
      if (result != 0)
        // ASC vs. DESC 
        return result * myself.getOrdering()[i]; 
    }

    if (myself.getSortKeys().length < compare.getSortKeys().length)
      // ASC vs. DESC
      return -1 * Ordering[ myself.getSortKeys().length ];
    else
     if (myself.getSortKeys().length > compare.getSortKeys().length)
       // ASC vs. DESC
       return 1 * Ordering[ myself.getSortKeys().length ];
     else
       return 0;

  }

}  // MintOrderByKey




