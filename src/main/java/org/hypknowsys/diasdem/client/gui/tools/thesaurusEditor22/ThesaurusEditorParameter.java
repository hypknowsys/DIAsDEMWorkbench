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

package org.hypknowsys.diasdem.client.gui.tools.thesaurusEditor22;

import org.hypknowsys.diasdem.core.DIAsDEMthesaurus;
import org.hypknowsys.diasdem.server.DiasdemTaskParameter;

/**
 * @version 2.1.5, 31 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */
  
public class ThesaurusEditorParameter extends DiasdemTaskParameter {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected DIAsDEMthesaurus DiasdemThesaurus = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static String TASK_CLASS_NAME = 
  "org.hypknowsys.diasdem.client.gui.tools.thesaurusEditor22"
  + ".ThesaurusEditorTask"; 
  private final static String PARAMETER_PANEL_CLASS_NAME = 
  "org.hypknowsys.diasdem.client.gui.tools.thesaurusEditor22"
  + ".ThesaurusEditorParameterPanel"; 
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public ThesaurusEditorParameter() {

    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    DiasdemThesaurus = null;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public ThesaurusEditorParameter(DIAsDEMthesaurus pDiasdemThesaurus) {

    this();
    
    DiasdemThesaurus = pDiasdemThesaurus;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public DIAsDEMthesaurus getDiasdemThesaurus() { 
    return DiasdemThesaurus; } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setDiasdemThesaurus(DIAsDEMthesaurus pDiasdemThesaurus) {
    DiasdemThesaurus = pDiasdemThesaurus; } 

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