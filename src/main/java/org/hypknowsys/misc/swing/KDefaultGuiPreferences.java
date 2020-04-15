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

package org.hypknowsys.misc.swing;

import java.util.*;
import java.io.*;
import org.hypknowsys.misc.util.*;

/**
  * @version 2.1, 15 August 2003
  * @author Karsten Winkler
  */

public class KDefaultGuiPreferences extends KProperties 
implements KGuiPreferences {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public final static KProperty[] MY_PROPERTY_DATA = {
    new KProperty("FRAME_SIZE_X",
    "Horizontal Size of Main Frame",
    "800", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("FRAME_POSITION_X",
    "Horizontal Position of Main Frame",
    "50", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("FRAME_POSITION_Y",
    "Vertical Position of Main Frame",
    "50", KProperty.INTEGER, KProperty.NOT_EDITABLE),
    new KProperty("FRAME_SIZE_Y",
    "Vertical Size of Main Frame",
    "600", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XXS_SIZE_X",
    "Horizontal Size of Ultra Small Dialogs",
    "300", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XXS_SIZE_Y",
    "Vertical Size of Ultra Small Dialogs",
    "75", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XS_SIZE_X",
    "Horizontal Size of Extra Small Dialogs",
    "400", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XS_SIZE_Y",
    "Vertical Size of Extra Small Dialogs",
    "150", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_S_SIZE_X",
    "Horizontal Size of Small Dialogs",
    "500", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_S_SIZE_Y",
    "Vertical Size of Small Dialogs",
    "200", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_M_SIZE_X",
    "Horizontal Size of Medium Dialogs",
    "600", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_M_SIZE_Y",
    "Vertical Size of Medium Dialogs",
    "300", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_L_SIZE_X",
    "Horizontal Size of Large Dialogs",
    "700", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_L_SIZE_Y",
    "Vertical Size of Large Dialogs",
    "500", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XL_SIZE_X",
    "Horizontal Size of Extra Large Dialogs",
    "800", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("DIALOG_XL_SIZE_Y",
    "Vertical Size of Extra Large Dialogs",
    "600", KProperty.INTEGER, KProperty.EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KDefaultGuiPreferences() {

    super(MY_PROPERTY_DATA);
    FileName = null;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public KDefaultGuiPreferences(String pFileName, int pTask)
  throws IOException {

    super(MY_PROPERTY_DATA);
    
    FileName = pFileName;
    if (pTask == CREATE) {
      this.save(FileName);
    }
    else if (pTask == LOAD) {
      if (Tools.isExistingFile(pFileName)) {
        this.load(FileName);
      }
      else {
        throw new IOException();
      }
    }
    else {
      throw new IOException();
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getFileName() {
    return FileName; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(100);
    TmpStringBuffer.append("Default Gui Preferences: FileName=");
    TmpStringBuffer.append(FileName);
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface GuiPreferences methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getFrameSizeX() {
    return this.getIntProperty("FRAME_SIZE_X");
  } 
  
  public int getFrameSizeY() {
    return this.getIntProperty("FRAME_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getFramePositionX() {
    return this.getIntProperty("FRAME_POSITION_X");
  } 
  
  public int getFramePositionY() {
    return this.getIntProperty("FRAME_POSITION_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public void setFramePositionX(int pFramePositionX) {
    this.setIntProperty("FRAME_POSITION_X", pFramePositionX);
  } 
  
  public void setFramePositionY(int pFramePositionY) {
    this.setIntProperty("FRAME_POSITION_Y", pFramePositionY);
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogXxsSizeX() {
    return this.getIntProperty("DIALOG_XXS_SIZE_X");
  }
  
  public int getDialogXxsSizeY() {
    return this.getIntProperty("DIALOG_XXS_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogXsSizeX() {
    return this.getIntProperty("DIALOG_XS_SIZE_X");
  }
  
  public int getDialogXsSizeY() {
    return this.getIntProperty("DIALOG_XS_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogSSizeX() {
    return this.getIntProperty("DIALOG_S_SIZE_X");
  }
  
  public int getDialogSSizeY() {
    return this.getIntProperty("DIALOG_S_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogMSizeX() {
    return this.getIntProperty("DIALOG_M_SIZE_X");
  }
  
  public int getDialogMSizeY() {
    return this.getIntProperty("DIALOG_M_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogLSizeX() {
    return this.getIntProperty("DIALOG_L_SIZE_X");
  }
  
  public int getDialogLSizeY() {
    return this.getIntProperty("DIALOG_L_SIZE_Y");
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public int getDialogXlSizeX() {
    return this.getIntProperty("DIALOG_XL_SIZE_X");
  }
  
  public int getDialogXlSizeY() {
    return this.getIntProperty("DIALOG_XL_SIZE_Y");
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getLookAndFeelClassName() {
    return this.getStringProperty("LOOK_AND_FEEL_CLASS_NAME");
  }

  public void setLookAndFeelClassName(String pProperty) {
    this.setProperty("LOOK_AND_FEEL_CLASS_NAME", pProperty);
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