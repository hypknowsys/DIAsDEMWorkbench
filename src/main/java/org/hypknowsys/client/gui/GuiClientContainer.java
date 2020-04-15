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

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import org.hypknowsys.core.Project;
import org.hypknowsys.server.Server;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface GuiClientContainer extends ActionListener {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getPreferredTitle();
  
  public int getPreferredSizeX();
  
  public int getPreferredSizeY();
  
  public Component getInitialFocusComponent();
  
  public Component getAsComponent();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  // Creating a GUI container: (i) setContext() and (ii) initialize()
  // Note: initialize() only deals with business rules, data and
  // lays out all components of its GUI container
  
  // Closing a GUI container: (i) setClosed(true) which subsequently
  // calls (ii) finalize() and (iii) setVisible(false)
  // Note: finalize() only deals with business rules and data
  
  public void setContext(Server pDiasdemServer,
  Project pDiasdemProject, GuiClient pDiasdemGui,
  GuiClientPreferences pDiasdemGuiPreferences);
  
  public void initialize();
  
  public void finalize() throws PropertyVetoException;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}