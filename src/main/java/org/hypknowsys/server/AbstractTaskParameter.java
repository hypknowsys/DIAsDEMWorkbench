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
import java.lang.reflect.InvocationTargetException;
import org.hypknowsys.client.gui.GuiClient;
import org.hypknowsys.client.gui.GuiClientPreferences;
import org.hypknowsys.client.gui.TaskParameterPanel;
import org.hypknowsys.core.Project;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class AbstractTaskParameter implements TaskParameter, Serializable {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String TaskClassName = null;
  protected String ParameterPanelClassName = null;
  
  protected Object Container = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static final String TASK_CLASS_NAME = null;
  private static final String PARAMETER_PANEL_CLASS_NAME = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskParameter() {
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    Container = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractTaskParameter(Object pContainer) {
    
    this();
    Container = pContainer;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public Object getContainer() { return Container; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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
  /* ########## interface TaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameterPanel getTaskParameterPanel(Server pServer,
  Project pProject, GuiClient pGui, GuiClientPreferences pGuiPreferences) {
    
    if (this.getTaskParameterPanelClassName() != null) {
      TaskParameterPanel currentParameterPanel = null;
      try {
        currentParameterPanel = (TaskParameterPanel)Class
        .forName(this.getTaskParameterPanelClassName())
        .getConstructor(null).newInstance(null);
        currentParameterPanel.setContext(pServer, pProject, pGui,
        pGuiPreferences);
        currentParameterPanel.initialize();
        currentParameterPanel.setTaskParameter(this);
        return currentParameterPanel;
      }
      catch(ClassNotFoundException e1) {
        e1.printStackTrace();
      }
      catch(NoSuchMethodException e2) {
        e2.printStackTrace();
      }
      catch(InstantiationException e3) {
        e3.printStackTrace();
      }
      catch(IllegalAccessException e4) {
        e4.printStackTrace();
      }
      catch(InvocationTargetException e5) {
        e5.printStackTrace();
      }
    }
    
    return null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskParameterPanelClassName() {
    
    if (ParameterPanelClassName == null) {
      System.err.println(this.getClass().getName()
      + ": Implement TaskParameter!");
    }
    return ParameterPanelClassName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTaskClassName() {
    
    if (TaskClassName == null) {
      System.err.println(this.getClass().getName()
      + ": Implement TaskParameter!");
    }
    return TaskClassName;
    
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