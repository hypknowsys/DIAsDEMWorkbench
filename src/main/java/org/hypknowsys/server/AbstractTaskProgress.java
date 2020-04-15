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

import java.io.Serializable;

/**
 * internal range of progres value is [0; 100]
 *
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class AbstractTaskProgress implements TaskProgress, Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected int Value = 0;
  protected String Note = null;
  protected Object Container = null;
  
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
  
  public AbstractTaskProgress() {
    
    Value = 0;
    Note = "";
    Container = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskProgress(int pValue, String pNote) {
    
    Value = pValue;
    Note = pNote;
    Container = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskProgress(int pValue, String pNote, Object pContainer) {
    
    Value = pValue;
    Note = pNote;
    Container = pContainer;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getValue() { return Value; }
  public String getNote() { return Note; }
  public Object getContainer() { return Container; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setValue(int pValue) { Value = pValue; }
  public void setNote(String pNote) { Note = pNote; }
  public void setContainer(Object pContainer) { Container = pContainer; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface TaskProgress methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void normalizeValue(int pValue, int pMaxValue) {
    
    Value = (int)((double)pValue * 100 / pMaxValue);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void update(int pValue, String pNote) {
    
    Value = pValue;
    Note = pNote;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void update(int pValue, int pMaxValue, String pNote) {
    
    Value = (int)((double)pValue * 100 / pMaxValue);
    Note = pNote;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void appendNote(String pNote) {
    
    Note += pNote;
    
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