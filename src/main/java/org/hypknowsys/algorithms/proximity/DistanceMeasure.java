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

package org.hypknowsys.algorithms.proximity;

import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.InstancesMetadata;

/**
 * Inspired by weka.clusterers.SimpleKMeans, Revision 1.9:
 * Copyright (C) 2000 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * This interface defines a [0; 1] normalized distance measure for
 * two Weka instances.
 *
 * @version 0.1, 6 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public interface DistanceMeasure {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final String OPTION_EUCLIDEAN_DISTANCE = "euclidean";
  public static final String OPTION_COSINE_DISTANCE = "cosine";
  public static final String OPTION_EXTENDED_JACCARD_DISTANCE
  = "extendedJaccard";
  public static final String OPTION_EXTENDED_DICE_DISTANCE
  = "extendedDice";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the distance measure-specific option arguments (e.g.,
   * "euclidean" or "cosine") as a string .
   *
   * @return the distance measure-specific options arguments
   */
  
  public abstract String getOptionArguments();
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Computes the [0; 1] normalized distance between two instances. A
   * distance of 0 indicates that pFirst and pSecond are very similar, but
   * not necessarily equal instances.
   *
   * @param pFirst the first instance
   * @param pSecond the second instance
   * @param pInstancesMetadata the metadata object the corresponds
   * to the data set of instances pFirst and pSecond
   * @return the distance between the two given instances;
   * distance is [0; 1] normalized
   */
  
  public double computeDistance(Instance pFirst, Instance pSecond,
  InstancesMetadata pInstancesMetadata);
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
}