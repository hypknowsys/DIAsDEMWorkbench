/*
 * Copyright (C) 1998-2005, Steffan Baron, Henner Graubitz, Carsten Pohle,
 * Myra Spiliopoulou, Karsten Winkler. All rights reserved.
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

package org.hypknowsys.wum.core.default10;

/**
 * If there's an syntax error in the QueryString, a MintSyntaxErrorException
 * will be thrown. Mainly used by MintQueryParser.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MintSyntaxErrorException extends RuntimeException {

  // ########## attributes and constants ##########

  private final static String ERROR = "MINT Syntax Error:\n";

  // ########## constructors ##########
  
  /**
   * constructs an empty MintSyntaxErrorException, no datailed Message
   */  

  public MintSyntaxErrorException() { super(); }
	
  /**
   * constructs a MintSyntaxErrorException with a datailed Message
   * @param pMessage message describing the syntax error
   */  

  public MintSyntaxErrorException(String pMessage) { super(ERROR + pMessage); }
	
}  // class MintSyntaxErrorException
