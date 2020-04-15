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

import java.util.Vector;
import org.hypknowsys.misc.swing.KTableModel;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface DIAsDEMthesaurus extends KTableModel {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getName();
  public int getTermsOrderedBy();
  public int getSize();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setMaintainKTableModel(boolean pMaintainKTableModel);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void countOccurrence(String pWord);
  
  public void countOccurrence(String pWord, int pOccurrence);
  
  public boolean contains(String pWord);
  
  public void add(DIAsDEMthesaurusTerm pTerm);
  
  public void delete(String pWord);
  
  public DIAsDEMthesaurusTerm get(String pWord);
  
  public DIAsDEMthesaurusTerm getTermAtKTableRow(int pRowIndex);
  
  public void replaceTermAtKTableRow(DIAsDEMthesaurusTerm pTerm,
  int pRowIndex);
  
  public void deleteTermAtKTableRow(int pRowIndex);
  
  public int addTermAndUpdateKTable(DIAsDEMthesaurusTerm pTerm);
  
  public long getID(String pWord);
  
  public int getOccurrences(String pWord);
  
  public void setOrderOccurrencesWordsDesc();
  
  public void setOrderOccurrencesWordsAsc();
  
  public void setOrderOccurrencesWordsAsc(int pMinOccurrence);
  
  public void setOrderOccurrencesWordsDesc(int pMinOccurrence);
  
  public void setOrderWordsDesc();
  
  public void setOrderWordsAsc();
  
  public void setOrderTypeWordsAsc();
  
  public DIAsDEMthesaurusTerm getFirstTerm();
  
  public DIAsDEMthesaurusTerm getNextTerm();
  
  public DIAsDEMthesaurusTerm getDescriptorTerm(String pWord);
  
  public Vector getTermVector();
  
  public void save(String pOutputFileName);
  
  public void saveAsWordFrequencyFile(String pOutputFileName);
  
  public void saveAsCsvFile(String pOutputFileName);
  
  public void saveAsHtmlFile(String pOutputFileName);
  
  public void saveAsTxtFile(String pOutputFileName);
  
  public void load(String pInputFileName);
  
  public long getNextID();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}