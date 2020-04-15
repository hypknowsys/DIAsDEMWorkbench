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

import java.io.Serializable;
import org.hypknowsys.algorithms.core.Attribute;
import org.hypknowsys.algorithms.core.FastVector;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.algorithms.core.InstancesMetadata;


/**
 * Inspired by weka.clusterers.SimpleKMeans, Revision 1.9:
 * Copyright (C) 2000 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * This abstract class implements the interface DistanceMeasure defining
 * a [0; 1] normalized distance measure for two Weka instances.
 *
 * @version 0.1, 6 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public abstract class AbstractDistanceMeasure implements Serializable,
DistanceMeasure {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Temporary string buffer for performance purposes only
   */
  protected transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
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
  
  /**
   * Returns a string representation of this instance.
   *
   * @return a string representation of this instance
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
  
  public abstract double computeDistance(Instance pFirst, Instance pSecond,
  InstancesMetadata pInstancesMetadata);
  
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
  
  /**
   * Main method for testing this class.
   *
   * @param pOptions no parameters are required
   */
  
  public static void main(String pOptions[]) {
    
    // create two attributes and data set
    Attribute x = new Attribute("x");
    Attribute y = new Attribute("y");
    FastVector attributes = new FastVector(2);
    attributes.addElement(x); attributes.addElement(y);
    Instances instances = new Instances("DistanceMeasures", attributes, 0);
    
    // create instances and associate them with the data set
    Instance instance1 = new Instance(2);
    instance1.setValue(x, 2); instance1.setValue(y, 3);
    instance1.setDataset(instances); instances.add(instance1);
    Instance instance2 = new Instance(2);
    instance2.setValue(x, 4); instance2.setValue(y, 6);
    instance2.setDataset(instances); instances.add(instance2);
    Instance instance3 = new Instance(2);
    instance3.setValue(x, -2); instance3.setValue(y, -3);
    instance3.setDataset(instances); instances.add(instance3);
    
    // create metadata container for data set
    InstancesMetadata instancesMetadata = new InstancesMetadata(instances);
    
    // create two different distance measures
    DistanceMeasure euclideanDistanceMeasure = new EuclideanDistanceMeasure();
    DistanceMeasure cosineDistanceMeasure = new CosineDistanceMeasure();
    
    // compute the distances between instance1 and instance1
    System.out.println(euclideanDistanceMeasure.computeDistance(
    instance1, instance1, instancesMetadata));
    System.out.println(cosineDistanceMeasure.computeDistance(
    instance1, instance1, instancesMetadata));
    
    // compute the distances between instance1 and instance2
    System.out.println(euclideanDistanceMeasure.computeDistance(
    instance1, instance2, instancesMetadata));
    System.out.println(cosineDistanceMeasure.computeDistance(
    instance1, instance2, instancesMetadata));
    
    // compute the distances between instance1 and instance3
    System.out.println(euclideanDistanceMeasure.computeDistance(
    instance1, instance3, instancesMetadata));
    System.out.println(cosineDistanceMeasure.computeDistance(
    instance1, instance3, instancesMetadata));
    
  }
  
}