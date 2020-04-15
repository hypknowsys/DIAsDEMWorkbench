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

package org.hypknowsys.server;

import java.util.ArrayList;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class AbstractValidatedTaskParameter implements ValidatedTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ArrayList ErrorMessages = null;
  private ArrayList WarningMessages = null;
  private TaskParameter ValidatedTaskParameter = null;
  
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
  
  public AbstractValidatedTaskParameter(TaskParameter pValidatedTaskParameter) {
    
    ValidatedTaskParameter = pValidatedTaskParameter;
    ErrorMessages = new ArrayList();
    WarningMessages = new ArrayList();
    
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
  /* ########## interface ValidatedTaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getValidatedTaskParameter() {
    return ValidatedTaskParameter; }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isValid() {
    
    if (ErrorMessages != null && ErrorMessages.size() == 0
    && WarningMessages != null && WarningMessages.size() == 0) {
      return true;
    }
    else {
      return false;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int numberOfErrors() {
    
    if (ErrorMessages != null) {
      return ErrorMessages.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int numberOfWarnings() {
    
    if (WarningMessages != null) {
      return WarningMessages.size();
    }
    else {
      return 0;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getError(int pErrorIndex) {
    
    if (ErrorMessages != null && pErrorIndex >= 0
    && pErrorIndex < ErrorMessages.size()) {
      return (String)ErrorMessages.get(pErrorIndex);
    }
    else {
      return "";
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getWarning(int pWarningIndex) {
    
    if (WarningMessages != null && pWarningIndex >= 0
    && pWarningIndex < WarningMessages.size()) {
      return (String)WarningMessages.get(pWarningIndex);
    }
    else {
      return "";
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addError(String pErrorMessage) {
    
    if (ErrorMessages != null) {
      ErrorMessages.add(pErrorMessage);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addWarning(String pWarningMessage) {
    
    if (WarningMessages != null) {
      WarningMessages.add(pWarningMessage);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void convertErrorsIntoWarnings(String pAdditionalNote) {
    
    for (int i = 0; i < this.numberOfErrors(); i++) {
      this.addWarning(this.getError(i) + pAdditionalNote);
    }
    ErrorMessages = new ArrayList();
    
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
  
  public static void main(String pOptions[]) {}
  
}