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

import org.hypknowsys.algorithms.core.Attribute;
import org.hypknowsys.algorithms.core.FastVector;
import org.hypknowsys.algorithms.core.Instance;
import org.hypknowsys.algorithms.core.Instances;
import org.hypknowsys.algorithms.core.InstancesMetadata;

/**
 * Inspired by weka.clusterers.SimpleKMeans, Revision 1.9:
 * Copyright (C) 2000 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * This class defines [0; 1] normalized Euclidean distance between two
 * instances. A distance of 0 indicates that pFirst and pSecond are very
 * similar, but not necessarily equal instances. During the computation,
 * attributes that are class attributes (i.e., target attributes) ignored
 * as well as all attributes hat are not of type weka.attribute.NUMERIC or
 * type weka.attribute.NONIMAL. Class SparseInstance is supported.
 *
 * @version 0.1, 6 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class EuclideanDistanceMeasure extends AbstractDistanceMeasure {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient double TmpEuclideanDistance = 0.0d;
  private transient double TmpDifference = 0.0d;
  // index: 0 .. Instance.numAttributes() for both sparse and ordinary instances
  private transient int TmpFirstIndex = 0;
  private transient int TmpSecondIndex = 0;
  // position: 0 .. Instance.numValues() << Instance.numAttributes()
  // for both sparse instances
  // position: 0 .. Instance.numValues() = Instance.numAttributes()
  // for both normal instances
  private transient int TmpFirstPosition = 0;
  private transient int TmpSecondPosition = 0;
  private transient double TmpFirstValue = 0.0d;
  private transient double TmpSecondValue = 0.0d;
  
  private transient int TmpNumberOfDistanceCounts = 0;
  
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
   * Returns the distance measure-specific option arguments (i.e.,
   * DistanceMeasure.OPTION_EUCLIDEAN_DISTANCE) as a string.
   *
   * @return the distance measure-specific options arguments
   */
  
  public String getOptionArguments() {
    
    return DistanceMeasure.OPTION_EUCLIDEAN_DISTANCE;
    
  };
  
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
  
  /**
   * Computes the [0; 1] normalized Euclidean distance between two instances. A
   * distance of 0 indicates that pFirst and pSecond are very similar, but
   * not necessarily equal instances. During the computation, attributes that
   * are class attributes (i.e., target attributes) ignored as well as all
   * attributes hat are not of type weka.attribute.NUMERIC or
   * type weka.attribute.NONIMAL. Class SparseInstance is supported.
   *
   * @param pFirst the first instance
   * @param pSecond the second instance
   * @param pInstancesMetadata the metadata object that corresponds
   * to the data set of instances pFirst and pSecond
   * @return the Euclidean distance between the two given instances;
   * distance is [0; 1] normalized
   */
  
  public double computeDistance(Instance pFirst, Instance pSecond,
  InstancesMetadata pInstancesMetadata) {
    
    TmpEuclideanDistance = 0.0d;
    TmpFirstIndex = 0;
    TmpSecondIndex = 0;
    TmpFirstValue = 0.0d;
    TmpSecondValue = 0.0d;
    TmpFirstPosition = 0;
    TmpSecondPosition = 0;
    TmpNumberOfDistanceCounts = 0;
    while (TmpFirstPosition < pFirst.numValues()
    || TmpSecondPosition < pSecond.numValues()) {
      // translate attribute position into attribute index
      if (TmpFirstPosition >= pFirst.numValues()) {
        TmpFirstIndex = pInstancesMetadata.getNumberOfAttributes();
      }
      else {
        TmpFirstIndex = pFirst.index(TmpFirstPosition);
      }
      if (TmpSecondPosition >= pSecond.numValues()) {
        TmpSecondIndex = pInstancesMetadata.getNumberOfAttributes();
      }
      else {
        TmpSecondIndex = pSecond.index(TmpSecondPosition);
      }
      // ignore certain attributes
      if (pInstancesMetadata.isClassAttribute(TmpFirstIndex)
      || pInstancesMetadata.isDateAttribute(TmpFirstIndex)
      || pInstancesMetadata.isStringAttribute(TmpFirstIndex)) {
        TmpFirstPosition++; continue;
      }
      if (pInstancesMetadata.isClassAttribute(TmpSecondIndex)
      || pInstancesMetadata.isDateAttribute(TmpSecondIndex)
      || pInstancesMetadata.isStringAttribute(TmpSecondIndex)) {
        TmpSecondPosition++; continue;
      }
      // compute difference of vector components
      TmpFirstValue = 0.0d;
      TmpSecondValue = 0.0d;
      TmpDifference = 0.0d;
      if (TmpFirstIndex == TmpSecondIndex) {
        TmpFirstValue = pFirst.valueSparse(TmpFirstPosition);
        TmpSecondValue = pSecond.valueSparse(TmpSecondPosition);
        TmpDifference = this.difference(pInstancesMetadata, TmpFirstIndex,
        TmpFirstValue, TmpSecondValue);
        TmpFirstPosition++;
        TmpSecondPosition++;
      }
      else if (TmpFirstIndex > TmpSecondIndex) {
        TmpSecondValue = pSecond.valueSparse(TmpSecondPosition);
        TmpDifference = this.difference(pInstancesMetadata, TmpSecondIndex,
        0, TmpSecondValue);
        TmpSecondPosition++;
      }
      else {
        TmpFirstValue = pFirst.valueSparse(TmpFirstPosition);
        TmpDifference = this.difference(pInstancesMetadata, TmpFirstIndex,
        TmpFirstValue, 0);
        TmpFirstPosition++;
      }
      TmpEuclideanDistance += TmpDifference * TmpDifference;
      TmpNumberOfDistanceCounts++;
    }
    
    if (TmpNumberOfDistanceCounts > 0) {
      TmpEuclideanDistance = Math.sqrt(TmpEuclideanDistance
      / TmpNumberOfDistanceCounts);
    }
    else {
      TmpEuclideanDistance = 0.0d;  // both instances are completely empty
    }
    
    return TmpEuclideanDistance;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Computes the difference between two given attribute
   * values from the same data set.
   *
   * @param pInstancesMetadata the metadata object that corresponds
   * to the data set of pAttributeValue's instance
   * @param pIndex the index of both attributes
   * @param pAttributeValue1 the attribute value of instance 1
   * @param pAttributeValue2 the attribute value of instance 2
   */
  
  private double difference(InstancesMetadata pInstancesMetadata,
  int pIndex, double pAttributeValue1, double pAttributeValue2) {
    
    switch (pInstancesMetadata.getTypeOfAttribute(pIndex)) {
      
      case Attribute.NOMINAL: {
        
        if (Instance.isMissingValue(pAttributeValue1)
        || Instance.isMissingValue(pAttributeValue2)
        || ((int)pAttributeValue1 != (int)pAttributeValue2)) {
          return 1;
        }
        else {
          return 0;
        }
        
      }
      case Attribute.NUMERIC: {
        
        if (Instance.isMissingValue(pAttributeValue1)
        || Instance.isMissingValue(pAttributeValue2)) {
          if (Instance.isMissingValue(pAttributeValue1)
          && Instance.isMissingValue(pAttributeValue2)) {
            return 1.0d;
          }
          else {
            double difference;
            if (Instance.isMissingValue(pAttributeValue2)) {
              difference = pInstancesMetadata
              .normalize(pIndex, pAttributeValue1);
            }
            else {
              difference = pInstancesMetadata
              .normalize(pIndex, pAttributeValue2);
            }
            if (difference < 0.5d) {
              difference = 1.0d - difference;
            }
            return difference;
          }
        }
        else {
          return pInstancesMetadata.normalize(pIndex, pAttributeValue1)
          - pInstancesMetadata.normalize(pIndex, pAttributeValue2);
        }
        
      }
      default: {
        return 0.0d;
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {
    
    Attribute x = new Attribute("x");
    Attribute y = new Attribute("y");
    
    FastVector attributes = new FastVector(2);
    attributes.addElement(x);
    attributes.addElement(y);
    
    Instances instances = new Instances("test", attributes, 0);
    
    Instance instance1 = new Instance(2);
    instance1.setValue(x, 2); instance1.setValue(y, 3);
    instance1.setDataset(instances); instances.add(instance1);
    
    Instance instance2 = new Instance(2);
    instance2.setValue(x, 4); instance2.setValue(y, 6);
    instance2.setDataset(instances); instances.add(instance2);
    
    Instance instance3 = new Instance(2);
    instance3.setValue(x, -2); instance3.setValue(y, -3);
    instance3.setDataset(instances); instances.add(instance3);
    
    Instance instance4 = new Instance(2);
    instance4.setValue(x, 3); instance4.setValue(y, 2);
    instance4.setDataset(instances); instances.add(instance4);
    
    Instance instance5 = new Instance(2);
    instance5.setValue(x, 3.1); instance5.setValue(y, 2.1);
    instance5.setDataset(instances); instances.add(instance5);
    
    Instance instance6 = new Instance(2);
    instance6.setValue(x, 0); instance6.setValue(y, 2);
    instance6.setDataset(instances); instances.add(instance6);
    
    InstancesMetadata instancesMetadata = new InstancesMetadata(instances);
    System.out.println(instancesMetadata);
    DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
    
    System.out.println("Euclidean distance((2, 3); (2, 3)) = "
    + distanceMeasure.computeDistance(instance1, instance1,
    instancesMetadata));
    System.out.println("Euclidean distance((2, 3); (4, 6)) = "
    + distanceMeasure.computeDistance(instance1, instance2,
    instancesMetadata));
    System.out.println("Euclidean distance((2, 3); (-2, -3)) = "
    + distanceMeasure.computeDistance(instance1, instance3,
    instancesMetadata));
    System.out.println("Euclidean distance((2, 3); (3, 2)) = "
    + distanceMeasure.computeDistance(instance1, instance4,
    instancesMetadata));
    System.out.println("Euclidean distance((4, 6); (3, 2)) = "
    + distanceMeasure.computeDistance(instance2, instance4,
    instancesMetadata));
    System.out.println("Euclidean distance((3.1, 2.1); (3, 2)) = "
    + distanceMeasure.computeDistance(instance5, instance4,
    instancesMetadata));
    System.out.println("Euclidean distance((0, 2); (4, 6)) = "
    + distanceMeasure.computeDistance(instance6, instance2,
    instancesMetadata));
    
  }
  
}