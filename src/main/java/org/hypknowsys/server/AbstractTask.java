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

public abstract class AbstractTask implements Task {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected Server TaskServer = null;
  protected Project TaskProject = null;
  protected AbstractTaskParameter Parameter = null;
  protected AbstractTaskResult Result = null;
  
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
  
  public AbstractTask() {}
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTask(Server pTaskServer) {
    
    TaskServer = pTaskServer;
    
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
  /* ########## interface Task methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getClassName() {
    return this.getClass().getName();
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getLabel() {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskParameterClassName() {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskResultClassName() {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskControlPanelClassName() {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskControlPanel getTaskControlPanel(Server pServer,
  Project pProject, GuiClient pGui, GuiClientPreferences pGuiPreferences) {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KProperty[] getProjectPropertyData() {
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KProperty[] getGuiClientPropertyData(){
    System.err.println(this.getClassName() + ": Implement Task!");
    return null;
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