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

import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskControlPanel;
import org.hypknowsys.core.Project;
import org.hypknowsys.misc.swing.KMenuItem;
import org.hypknowsys.misc.util.KProperty;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public interface Task {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final int TASK_UNKNOWN = 0;
  public static final int TASK_ACCEPTED = 1;
  public static final int TASK_REJECTED = 2;
  public static final int TASK_RUNNING = 3;
  public static final int TASK_FINISHED = 4;
  public static final int TASK_CANCELED = 5;
  public static final int TASK_STOPPED = 6;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getClassName();
  
  public String getLabel();
  
  public String getTaskParameterClassName();
  
  public String getTaskResultClassName();
  
  public String getTaskControlPanelClassName();
  
  public KMenuItem getKMenuItem();
  
  public KProperty[] getProjectPropertyData();
  
  public KProperty[] getGuiClientPropertyData();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter);
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject);
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject);
  
  public TaskControlPanel getTaskControlPanel(Server pServer,
  Project pProject, GuiClient pGui, GuiClientPreferences pGuiPreferences);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}