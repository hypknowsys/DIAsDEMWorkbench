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

import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import javax.swing.JFrame;
import org.hypknowsys.client.Client;
import org.hypknowsys.misc.swing.KDesktopPane;
import org.hypknowsys.misc.swing.KInternalFrame;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface GuiClient extends Client, ActionListener {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final int MIN_GUI_STATUS = 0;
  public static final int GUI_STARTED_NO_PROJECT_OPENED = 10;
  public static final int PROJECT_OPENED_NO_TASK_RUNNING = 20;
  public static final int PROJECT_OPENED_NON_BLOCKING_TASK_RUNNING = 30;
  public static final int PROJECT_OPENED_BLOCKING_TASK_RUNNING = 40;
  public static final int MAX_GUI_STATUS = 100;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTitle();
  
  public int getGuiStatus();
  
  public GuiClientMenuBar getGuiMenuBar();
  
  public JFrame getJFrame();
  
  public KDesktopPane getKDesktopPane();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setTitle(String pTitle);
  
  public int setGuiStatus(int pStatus);
  
  public void setGuiMenuBar(GuiClientMenuBar pGuiClientMenuBar);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  // Opening a GUI client: (i) setContext() and (ii) setOpened(true)
  // which subsequently calls (iii) initialize() and (iv) setVisible(true)
  // Note: initialize() only deals with business rules and data
  // Note: setVisible(true) only deals with GUI staff
  // Note: setOpened(false) is not defined
  
  // Closing a GUI client: (i) setClosed(true) which subsequently
  // calls (ii) finalize() and (iii) setVisible(false)
  // Note: finalize() only deals with business rules and data
  // Note: setVisible(false) only deals with GUI staff
  // Note: setClosed(false) is not defined
  
  public void setContext(GuiClientPreferences pGuiClientPreferences,
  String[] pPlugInClassNames);
  
  public void setOpened(boolean pOpened);
  
  public void setClosed(boolean pClosed);
  
  public void initialize();
  
  public void finalize() throws PropertyVetoException;
  
  public void setVisible(boolean pVisible);
  
  public KInternalFrame launchTaskControlPanelAsInternalFrame(
  TaskControlPanel pTaskControlPanel);
  
  public void setWaitCursor();
  
  public void setDefaultCursor();
  
  public void setLookAndFeel(String pLookAndFeelClassName,
  boolean pUpdateUi);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}