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
import org.hypknowsys.algorithms.core.Utils;

/**
 * Inspired by weka.clusterers.SimpleKMeans, Revision 1.9:
 * Copyright (C) 2000 Mark Hall (mhall@cs.waikato.ac.nz)<p>
 *
 * This class defines the [0; 1] normalized extended Dice distance between
 * two instances. A distance of 0 indicates that pFirst and pSecond are very
 * similar, but not necessarily equal instances. Extended Dice distance
 * measure is defined as 1 - extended Dice similarity. During the computation,
 * attributes that are class attributes (i.e., target attributes) or that are
 * not of type weka.attribute.NUMERIC are ignored. Class SparseInstance is
 * supported.
 *
 * @version 0.1, 22 November 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class ExtendedDiceDistanceMeasure extends AbstractDistanceMeasure {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient double TmpExtDiceDistance = 0.0d;
  private transient double TmpScalarProduct = 0.0d;
  private transient double TmpFirstLength = 0.0d;
  private transient double TmpSecondLength = 0.0d;
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
   * DistanceMeasure.OPTION_EXTENDED_DICE_DISTANCE) as a string.
   *
   * @return the distance measure-specific options arguments
   */
  
  public String getOptionArguments() {
    
    return DistanceMeasure.OPTION_EXTENDED_DICE_DISTANCE;
    
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
   * This class defines the [0; 1] normalized extended Dice distance between
   * two instances. A distance of 0 indicates that pFirst and pSecond are very
   * similar, but not necessarily equal instances. Extended Dice distance
   * measure is defined as 1 - extended Dice similarity. During the computation,
   * attributes that are class attributes (i.e., target attributes) or that are
   * not of type weka.attribute.NUMERIC are ignored. Class SparseInstance is
   * supported.
   *
   * @param pFirst the first instance
   * @param pSecond the second instance
   * @param pInstancesMetadata the metadata object that corresponds
   * to the data set of instances pFirst and pSecond
   * @return the extended Dice distance between the two given instances;
   * distance is [0; 1] normalized
   */
  
  public double computeDistance(Instance pFirst, Instance pSecond,
  InstancesMetadata pInstancesMetadata) {
    
    TmpExtDiceDistance = 0.0d;
    TmpScalarProduct = 0.0d;
    TmpFirstLength = 0.0d;
    TmpSecondLength = 0.0d;
    TmpFirstIndex = 0;
    TmpSecondIndex = 0;
    TmpFirstValue = 0.0d;
    TmpSecondValue = 0.0d;
    TmpFirstPosition = 0;
    TmpSecondPosition = 0;
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
      || !pInstancesMetadata.isNumericAttribute(TmpFirstIndex)) {
        TmpFirstPosition++; continue;
      }
      if (pInstancesMetadata.isClassAttribute(TmpSecondIndex)
      || !pInstancesMetadata.isNumericAttribute(TmpSecondIndex)) {
        TmpSecondPosition++; continue;
      }
      // compute product of vector components and increase vector length
      TmpFirstValue = 0.0d;
      TmpSecondValue = 0.0d;
      if (TmpFirstIndex == TmpSecondIndex) {
        TmpFirstValue = pFirst.valueSparse(TmpFirstPosition);
        TmpSecondValue = pSecond.valueSparse(TmpSecondPosition);
        if (Instance.isMissingValue(TmpFirstValue)
        || Instance.isMissingValue(TmpSecondValue)) {
          TmpFirstValue = 0.0d;
          TmpSecondValue = 0.0d;
        }
        else {
          TmpFirstLength += TmpFirstValue * TmpFirstValue;
          TmpSecondLength += TmpSecondValue * TmpSecondValue;
        }
        TmpFirstPosition++;
        TmpSecondPosition++;
      }
      else if (TmpFirstIndex > TmpSecondIndex) {
        TmpSecondValue = pSecond.valueSparse(TmpSecondPosition);
        if (Instance.isMissingValue(TmpSecondValue)) {
          TmpSecondValue = 0.0d;
        }
        else {
          TmpSecondLength += TmpSecondValue * TmpSecondValue;
        }
        TmpSecondPosition++;
      }
      else {
        TmpFirstValue = pFirst.valueSparse(TmpFirstPosition);
        if (Instance.isMissingValue(TmpFirstValue)) {
          TmpFirstValue = 0.0d;
        }
        else {
          TmpFirstLength += TmpFirstValue * TmpFirstValue;
        }
        TmpFirstPosition++;
      }
      TmpScalarProduct += TmpFirstValue * TmpSecondValue;
    }
    
    if (TmpScalarProduct > 0) {
      TmpExtDiceDistance = 1.0d - (2.0d * TmpScalarProduct
      / (TmpFirstLength + TmpSecondLength));
      if (TmpExtDiceDistance > 1.0d) {
        TmpExtDiceDistance = 1.0d;  // round TmpExtDiceDistance to 1.0
      }
      else if (TmpExtDiceDistance < 0.0d) {
        TmpExtDiceDistance = 0.0d;  // round TmpExtDiceDistance to 0.0
      }
    }
    else if (Utils.eq(TmpFirstLength, 0) &&  Utils.eq(TmpSecondLength, 0)) {
      TmpExtDiceDistance = 0.0d;  // both instances are completely empty
    }
    else {
      TmpExtDiceDistance = 1.0d;  // only one instance is completely empty
    }
    
    return TmpExtDiceDistance;
    
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
    DistanceMeasure distanceMeasure = new ExtendedDiceDistanceMeasure();
    
    System.out.println("extended Dice distance((2, 3); (2, 3)) = "
    + distanceMeasure.computeDistance(instance1, instance1,
    instancesMetadata));
    System.out.println("extended Dice distance((2, 3); (4, 6)) = "
    + distanceMeasure.computeDistance(instance1, instance2,
    instancesMetadata));
    System.out.println("extended Dice distance((2, 3); (-2, -3)) = "
    + distanceMeasure.computeDistance(instance1, instance3,
    instancesMetadata));
    System.out.println("extended Dice distance((2, 3); (3, 2)) = "
    + distanceMeasure.computeDistance(instance1, instance4,
    instancesMetadata));
    System.out.println("extended Dice distance((4, 6); (3, 2)) = "
    + distanceMeasure.computeDistance(instance2, instance4,
    instancesMetadata));
    System.out.println("extended Dice distance((3.1, 2.1); (3, 2)) = "
    + distanceMeasure.computeDistance(instance5, instance4,
    instancesMetadata));
    System.out.println("extended Dice distance((0, 2); (4, 6)) = "
    + distanceMeasure.computeDistance(instance6, instance2,
    instancesMetadata));
    
  }
  
}