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

package org.hypknowsys.client.gui;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface TaskControlPanelContainer extends GuiClientContainer {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTitle();
  
  public JButton getDefaultButton();
  
  public JFrame getParentJFrame();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTitle(String pTitle);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  // Opening a GUI container: (i) setContext() and (ii) setOpened(true)
  // which subsequently calls (iii) initialize() and (iv) setVisible(true)
  // Note: initialize() only deals with business rules and data
  // Note: setVisible(true) only deals with GUI staff
  // Note: setOpened(false) is not defined
  
  // Closing a GUI container: (i) setClosed(true) which subsequently
  // calls (ii) finalize() and (iii) setVisible(false)
  // Note: finalize() only deals with business rules and data
  // Note: setVisible(false) only deals with GUI staff
  // Note: setClosed(false) is not defined
  
  public void setOpened(boolean pOpened);
  
  public void setClosed(boolean pClosed);
  
  public void setVisible(boolean pVisible);
  
  public void requestFocus();
  
  public boolean controlPanelContainerIsVisible();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}