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

import java.io.*;
import java.util.*;

/**
 * The MintQueryOptimizer creates a Sublist (kwinkler: of matching Pages) 
 * for to each Variable of the Query. The Sublist contains the all valid 
 * Attributes (ACCESSES and ID) due to the Query-restrictions. The Sublist is 
 * a sorted and particular Sequence of the Heap created while opening a 
 * mining base. The applied Sort-Algorithm is HeapSort.
 *
 * code completely removed by kwinkler 11/2001
 *
 * @version 0.7.0, 17 Nov 2001
 * @author Torsten Veit, Karsten Winkler
 */

public class MintQueryOptimizer implements Serializable {
  
  // ########## attributes ##########

  // ########## constructors ##########

  public MintQueryOptimizer() {}

  /**
   * optimizeQuery() analyses the given Query and returns a updated MintQuery 
   * according to new algorithm, code removed by kwinkler 11/2001
   */

  public MintQuery optimizeQuery(MintQuery pQuery) {

    return pQuery;  // do not use optimizer 

  }  

}