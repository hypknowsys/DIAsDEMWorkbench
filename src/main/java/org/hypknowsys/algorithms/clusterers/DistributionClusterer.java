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

package org.hypknowsys.algorithms.clusterers;

import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Utils;

/**
 * Based on weka.clusterers.DistributionClusterer, Revision 1.7:
 * Copyright (C) 1999 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * Abstract clustering model that produces (for each test instance)
 * an estimate of the membership in each cluster
 * (ie. a probability distribution).
 *
 * @version 0.1, 6 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public abstract class DistributionClusterer extends Clusterer {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Temporary string buffer for performance purposes only
   */
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns a string describing this clusterer.
   *
   * @return a description of the clusterer as a string
   */
  
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
  
  /**
   * Computes the density for a given instance.
   *
   * @param pInstance the instance to compute the density for
   * @return the density.
   * @exception Exception if the density could not be computed
   * successfully
   */
  
  public abstract double densityForInstance(Instance pInstance)
  throws Exception;
  
  /* ########## ########## ########## ########## ########## ######### */
  /**
   * Predicts the cluster memberships for a given instance.
   *
   * @param pInstance the instance to be assigned a cluster.
   * @return an array containing the estimated membership
   * probabilities of the test instance in each cluster (this
   * should sum to at most 1)
   * @exception Exception if distribution could not be
   * computed successfully
   */
  
  public abstract double[] distributionForInstance(Instance pInstance)
  throws Exception;
  
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Assigns an instance to a Cluster.
   *
   * @param pInstance the instance to be classified
   * @return the predicted most likely cluster for the instance.
   */
  
  public int clusterInstance(Instance pInstance) {
    
    double [] dist = null;
    try {
      dist = distributionForInstance(pInstance);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    if (dist == null) {
      System.err.println("Null distribution predicted");
      System.exit(-1);
    }    
    if (Utils.sum(dist) <= 0) {
      System.err.println("Unable to cluster instance");
      System.exit(-1);
    }
    
    return Utils.maxIndex(dist);
    
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
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
}