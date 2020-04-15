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

package org.hypknowsys.misc.util;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.core.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KProperty implements Serializable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected String Key = null;
  protected String Description = null;
  protected String DefaultValue = null;
  protected int Type = STRING;
  protected boolean Editable = NOT_EDITABLE;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public final static int STRING = 1;
  public final static int INTEGER = 2;
  public final static int LONG = 3;
  public final static int DOUBLE = 4;
  public final static int BOOLEAN = 5;
  public final static int TIMESTRING = 6;  // "0/00:00:00" = "D/HH:MM:SS"
  
  public final static boolean EDITABLE = true;
  public final static boolean NOT_EDITABLE = false;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KProperty(String pKey, String pDescription, String pDefaultValue,
  int pType, boolean pEditable) {
  
    Key = pKey;
    Description = pDescription;
    DefaultValue = pDefaultValue;
    Editable = pEditable;
    
    if (pType >= STRING && pType <= BOOLEAN) {
      Type = pType;
    }
    else {
      Type = STRING;
    }    
  
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getKey() { return Key; }
  public String getDescription() { return Description; }
  public String getDefaultValue() { return DefaultValue; }
  public int getType() { return Type; }
  public boolean getEditable() { return Editable; }  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setKey(String pKey) {
    Key = pKey; }
  public void setDescription(String pDescription) {
    Description = pDescription; }
  public void setDefaultValue(String pDefaultValue) {
    DefaultValue = pDefaultValue; }
  public void setType(int pType) {
    Type = pType; }
  public void setEditable(boolean pEditable) {
    Editable = pEditable; }  
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    TmpStringBuffer.append(" [Key=");
    TmpStringBuffer.append(Key);
    TmpStringBuffer.append("; Description=");
    TmpStringBuffer.append(Description);
    TmpStringBuffer.append("; DefaultValue=");
    TmpStringBuffer.append(DefaultValue);
    TmpStringBuffer.append("; Type=");
    TmpStringBuffer.append(Type);
    TmpStringBuffer.append("; Editable=");
    TmpStringBuffer.append(Editable);
    TmpStringBuffer.append("]");
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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