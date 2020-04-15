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

package org.hypknowsys.diasdem.core;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface DIAsDEMthesaurusTerm {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final String UNKNOWN = "-";
  
  public static final String DESCRIPTOR = "D";
  public static final String NONDESCRIPTOR = "N";
  
  public static final String TOP_TERM = "0";
  public static final String LEVEL1_TERM = "1";
  public static final String LEVEL2_TERM = "2";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public long getID();
  public String getWord();
  public int getOccurrences();
  public String getType();
  public String getHierarchyLevel();
  public String getScopeNotes();
  public String getSynonyms();
  public String getBroaderTerm();
  public String getNarrowerTerms();
  public String getUseDescriptor();
  public boolean isDescriptor();
  public boolean isNonDescriptor();
  public double getTermWeight();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setID(long pID);
  public void setWord(String pWord);
  public void setOccurrences(int pOccurrences);
  public void incrementOccurrences();
  public void increaseOccurrences(int pIncrease);
  public void setType(String pType);
  public void setHierarchyLevel(String pHierarchyLevel);
  public void setScopeNotes(String pScopeNotes);
  public void setSynonyms(String pSynonyms);
  public void setBroaderTerm(String pBroaderTerm);
  public void setNarrowerTerms(String pNarrowerTerms);
  public void setUseDescriptor(String pUseDescriptor);
  public void setTermWeight(double pTermWeight);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}