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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KOchButtonPanel {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private KButtonPanel OchButtonPanel = null;

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

  public KOchButtonPanel(ActionListener pActionListenner) {

    OchButtonPanel = new KButtonPanel(17, 0, 0, 0, 5, KButtonPanel.HORIZONTAL_RIGHT);
    OchButtonPanel.addFirstButton("OK", KeyEvent.VK_P, true, false, "OK", pActionListenner);
    OchButtonPanel.addNextButton("Cancel", 0, false, false, "Cancel", pActionListenner);
    OchButtonPanel.addLastButton("Help", KeyEvent.VK_H, false, false, "Help", pActionListenner);

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
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setOkEnabled(boolean pEnabledButton) {

    OchButtonPanel.setEnabled(0, pEnabledButton);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCancelEnabled(boolean pEnabledButton) {

    OchButtonPanel.setEnabled(1, pEnabledButton);

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setHelpEnabled(boolean pEnabledButton) {

    OchButtonPanel.setEnabled(2, pEnabledButton);

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setAllEnabled(boolean pEnabledButton) {

    for (int i = 0; i < 3; i++)
      OchButtonPanel.setEnabled(i, pEnabledButton);

  } 

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