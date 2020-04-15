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

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KFileFilter extends FileFilter {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected String[] FileExtensions = null;
  protected String Description = null;

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

  public KFileFilter(String pFileExtension, String pDescription) {

    super();
    
    FileExtensions = new String[1];
    FileExtensions[0] = pFileExtension.toLowerCase();
    Description = pDescription;

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public KFileFilter(String[] pFileExtensions, String pDescription) {

    super();
    
    FileExtensions = pFileExtensions;
    for (int i = 0; i < FileExtensions.length; i++)
      FileExtensions[i] = FileExtensions[i].toLowerCase();
    Description = pDescription;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getDescription() {
    return Description; }

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

  public boolean accept(File pFile) {

    if ( pFile.isDirectory() )
      return true;
  
    String filename = pFile.getAbsolutePath().toLowerCase();
    for (int i = 0; i < FileExtensions.length; i++)
      if ( filename.endsWith( FileExtensions[i] ) )
        return true;

    return false;

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public String getDefaultFileExtension() {
    
    if (FileExtensions != null && FileExtensions.length >= 1) {
      return FileExtensions[0]; 
    }
    else {
      return "";
    }
  
  }

  /* ########## ########## ########## ########## ########## ######### */

  public File ensureDefaultFileExtension(File pFile) {
    
    return KFileFilter.ensureFileExtension(pFile, 
    this.getDefaultFileExtension()); 
    
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
  
  public static File ensureFileExtension(File pFile, String pFileExtension) {

    if ( pFile.getAbsolutePath().toLowerCase().endsWith(
      pFileExtension.toLowerCase() ) ) 
      return pFile;
    else 
      return new File( pFile.getAbsolutePath() + pFileExtension );

  }  

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}