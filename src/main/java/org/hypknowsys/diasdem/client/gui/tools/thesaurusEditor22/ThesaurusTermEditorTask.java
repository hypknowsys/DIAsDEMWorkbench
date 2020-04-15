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

import org.hypknowsys.core.Project;
import org.hypknowsys.diasdem.core.DIAsDEMthesaurusTerm;
import org.hypknowsys.diasdem.server.DiasdemTask;
import org.hypknowsys.misc.util.Tools;
import org.hypknowsys.server.AbstractValidatedTaskParameter;
import org.hypknowsys.server.Server;
import org.hypknowsys.server.TaskParameter;

/**
 * @version 2.1.5, 31 December 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */
  
public class ThesaurusTermEditorTask extends DiasdemTask {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private ThesaurusTermEditorParameter CastParameter = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  private final static String LABEL = 
  "Thesaurus Term Editor 2.2"; 
  private final static String TASK_PARAMETER_CLASS_NAME = 
  "org.hypknowsys.diasdem.client.gui.tools.thesaurusEditor22"
  + ".ThesaurusTermEditorParameter"; 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public ThesaurusTermEditorTask() { 

    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;

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
       
    Parameter = pParameter;
    CastParameter = (ThesaurusTermEditorParameter)pParameter;

    AbstractValidatedTaskParameter result = 
    new AbstractValidatedTaskParameter(Parameter);
    
    DIAsDEMthesaurusTerm DiasdemThesaurusTerm = CastParameter
    .getDiasdemThesaurusTerm();
    if (DiasdemThesaurusTerm.getType().equals(DIAsDEMthesaurusTerm.UNKNOWN)) {
      result.addError(
      "Error: Please specify the 'Type of Term' as\n"
      + "either 'Descriptor' or 'Non-Descriptor'.");
    }
    if (DiasdemThesaurusTerm.isNonDescriptor() && (DiasdemThesaurusTerm
    .getUseDescriptor().equals(DIAsDEMthesaurusTerm.UNKNOWN) || Tools
    .stringIsNullOrEmpty(DiasdemThesaurusTerm.getUseDescriptor()))) {
      result.addError(
      "Error: Please specify a 'Use Descriptor'\n"
      + "term for the 'Non-Descriptor'.");
    }
    if (DiasdemThesaurusTerm.isNonDescriptor() && DiasdemThesaurusTerm
    .getWord().equals(DiasdemThesaurusTerm.getUseDescriptor())) {
      result.addError(
      "Error: Please specify a non-recursive 'Use\n"
      + "Descriptor' term for the 'Non-Descriptor'.");
    }
    
    
    return result;
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  public TaskParameter getDefaultTaskParameter(Server pServer, 
  Project pProject) {
    
    return new ThesaurusTermEditorParameter();
    
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