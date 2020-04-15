/*
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

package org.hypknowsys.algorithms.core;

import java.util.*;
import org.hypknowsys.misc.util.*;

/**
 * Based on weka.core.OptionHandler, Revision 1.6:
 * Copyright (C) 1999 Eibe Frank, Len Trigg<p>
 * Based on weka.core.Utils, Revision 1.33:
 * Copyright (C) 1999 Eibe Frank, Len Trigg, Yong Wang<p>
 *
 * Abstract class implementing weka.core.OptionHandler and
 * providing auxiliary methods for handling options.
 *
 * @version 0.1, 15 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 * @see OptionHandler
 */

public abstract class AbstractOptionHandler {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Temporary string buffer for performance purposes only
   */
  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns a string describing this class.
   *
   * @return a description of this class as a string
   */
  
  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface OptionHandler methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns an enumeration of all the available options.
   *
   * @return an enumeration of all available options.
   */
  
  public abstract Enumeration listOptions();

  /**
   * Sets the OptionHandler's options using the given list. All options
   * will be set (or reset) during this call (i.e. incremental setting
   * of options is not possible).
   *
   * @param options the list of options as an array of strings
   * @exception Exception if an option is not supported
   */
  
  public abstract void setOptions(String[] options) throws Exception;

  /**
   * Gets the current option settings for the OptionHandler.
   *
   * @return the list of current option settings as an array of strings
   */
  
  public abstract String[] getOptions();

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
  
  /**
   * Gets an option indicated by option name 'N' for '-N' from the given 
   * array of strings. Stops searching at the first marker "--". Replaces
   * option name and option in array pOptions with empty strings.
   *
   * @param pOptionName the character indicating the option.
   * @param pOptions the array of strings containing all the options.
   * @return the indicated option or an empty string
   */
  
  public static String getOption(char pOptionName, String [] pOptions) {
    
    return AbstractOptionHandler.getOption(Character.toString(pOptionName), 
    pOptions);
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Gets an option indicated by option name 'OptionName' for '-OptionName' 
   * from the given array of strings. Stops searching at the first marker "--".
   * Replaces option name and option in array pOptions with empty strings.
   *
   * @param pOptionName the string indicating the option.
   * @param pOptions the array of strings containing all the options.
   * @return the indicated option or an empty string
   */
  
  public static String getOption(String pOptionName, String [] pOptions) {
    
    String result = null;
    Double dummy = null;
    
    if (pOptionName == null || pOptions == null) {
      return "";
    }
    for (int i = 0; i < pOptions.length; i++) {
      if ((pOptions[i].length() > 0) && (pOptions[i].charAt(0) == '-')) {
        if (AbstractOptionHandler.isOptionName(pOptions[i])) {
          if (pOptions[i].substring(1).equals(pOptionName)) {
            if (i + 1 == pOptions.length 
            || AbstractOptionHandler.isOptionName(pOptions[i + 1])) {
              System.err.println("No value given for -" + pOptionName 
              + " option. Using default values if possible ...");
            }
            pOptions[i] = "";
            result = new String(pOptions[i + 1]);
            pOptions[i + 1] = "";
            return result;
          }
          if (pOptions[i].charAt(1) == '-') {
            return "";
          }
        }
      }
    }

    return "";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Checks if the given array contains the option name "-String". Stops
   * searching at the first marker "--". If the option name is found,
   * it is replaced with the empty string.
   *
   * @param pOptionName the string indicating 
   * the option name without preceding '-'.
   * @param pOptions the array of strings containing all the options.
   * @return true if the option name was found
   */

  public static boolean optionsContainOptionName(String pOptionName, 
  String [] pOptions) {
    
    if (pOptions == null) {
      return false;
    }
    for (int i = 0; i < pOptions.length; i++) {
      if (AbstractOptionHandler.isOptionName(pOptions[i])) {
          if (pOptions[i].equals("-" + pOptionName)) {
            pOptions[i] = "";
            return true;
          }
          if (pOptions[i].equals("--")) {
            return false;
          }
      }
    }
    return false;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Checks if the given array contains the option name "-Char". Stops
   * searching at the first marker "--". If the option name is found,
   * it is replaced with the empty string.
   *
   * @param pOptionName the character indicating 
   * the option name without preceding '-'.
   * @param pOptions the array of strings containing all the options.
   * @return true if the option name was found
   */

  public static boolean optionsContainOptionName(char pOptionName, 
  String [] pOptions) {
    
    return AbstractOptionHandler.optionsContainOptionName(
    Character.toString(pOptionName), pOptions);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Checks, whether pCandidateString is a valid option name such as
   * '-A' or '-verbose'. Valid option names begin with character '-'. 
   * pCandidateString is not an option name, if it is valid number 
   * (e.g., '-123'), if its length is one character, if it contains 
   * blank spaces or if it is the null value.
   *
   * @param pCandidateString the String to be checked.
   * @return true, if pCandidateString is an option name and false
   * otherwise.
   */
  
  public static boolean isOptionName(String pCandidateString) {
    
    if (pCandidateString == null
    || pCandidateString.length() <= 1
    || !pCandidateString.startsWith("-") 
    || Tools.isDouble(pCandidateString) 
    || pCandidateString.indexOf(' ') >= 0) {
      return false;
    }
    else {
      return true;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}