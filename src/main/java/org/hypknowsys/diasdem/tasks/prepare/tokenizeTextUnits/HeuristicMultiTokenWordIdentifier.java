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

package org.hypknowsys.diasdem.tasks.prepare.tokenizeTextUnits;

import java.util.regex.*;
import org.hypknowsys.misc.util.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class HeuristicMultiTokenWordIdentifier 
implements MultiTokenWordIdentifier {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private String[] MultiTokenWordsSearch = null;  // "unter anderem" "GmbH & Co* KG"
  private String[] MultiTokenWordsReplace = null;  // "unter_anderem" "GmbH_&_Co*_KG"
  private Pattern[] MultiTokenWordsRegex = null;  // "unter\ anderem" "GmbH\ &\ Co\*\ KG"

  private String Text = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;
  private transient Matcher TmpMatcher = null;  
  private transient Matcher TmpMatcher2 = null;  
  private transient String TmpString = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public HeuristicMultiTokenWordIdentifier(String[] pMultiTokenWords) {

    MultiTokenWordsSearch = pMultiTokenWords;
    MultiTokenWordsReplace = new String[pMultiTokenWords.length];
    MultiTokenWordsRegex = new Pattern[pMultiTokenWords.length];
    String search = null;
    String replace = null;
    String regex = null;
    
    for (int i = 0; i < MultiTokenWordsSearch.length; i++) {
      search = new String(MultiTokenWordsSearch[i]);
      replace = new String(MultiTokenWordsSearch[i]);
      replace = replace.replace(' ', '_');
      regex = new String(MultiTokenWordsSearch[i]);
      regex = Tools.escapeRegexSpecialChars(regex);
      MultiTokenWordsReplace[i] = new String(replace);
      MultiTokenWordsRegex[i] = Pattern.compile(regex);
//      System.out.println("Search:  " + MultiTokenWordsSearch[i]);
//      System.out.println("Replace: " + MultiTokenWordsReplace[i]);
//      System.out.println("Regex:   " + MultiTokenWordsRegex[i].pattern());
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface MultiTokenWordIdentifier methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String identifyMultiTokenWords(String pText) {
    
    // replace blank spaces in multi token words with underscores
    for (int i = 0; i < MultiTokenWordsRegex.length; i++) {
      // ?% performance increase by initial check
      if (pText.indexOf(MultiTokenWordsSearch[i]) >= 0) { 
        TmpMatcher = MultiTokenWordsRegex[i].matcher(pText);
        pText = TmpMatcher.replaceAll(MultiTokenWordsReplace[i]);
      }
    }

    return pText;
    
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

  public static void main(String args[]) {}
  
}