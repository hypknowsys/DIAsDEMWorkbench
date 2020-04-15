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

package org.hypknowsys.algorithms.core;

import java.io.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.algorithms.core.*;

/**
 * Inspired by weka.core.Instances: Revision 1.36
 * Copyright (C) 1999 Eibe Frank<p>
 * Inspired by weka.core.Attribute: Revision 1.26
 * Copyright (C) 1999 Eibe Frank<p>
 * Inspired by weka.core.AttributeStats: Revision 1.6
 * Copyright (C) 1999 Len Trigg<p>
 * Inspired by weka.experiment.Stats: Revision 1.9
 * Copyright (C) 1999 Len Trigg<p>
 *
 * Class for computing and storing metadata about an instance of 
 * weka.core.Instances and its attributes. Once InstancesMetadata 
 * has been created, it can't be changed at all. Note, this class
 * encapsulates redundandly stored metadata to provide an easy 
 * access to this information from a single class.
 *
 * @version 0.1, 6 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class InstancesMetadata implements Serializable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /** The list contains attribute metadata objects, whereas 
   * AttributeMetadataList[0] corresponds to Instances.attribute(0) and 
   * AttributeMetadataList.length corresponds to the number of 
   * attributes in the data set */
  protected AttributeMetadata[] AttributeMetadataList = 
  new AttributeMetadata[0];

  /** The class attribute's index, default value -1, negative integer
   * represents undefined class index */
  protected int ClassIndex = -1;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Temporary string buffer for performance purposes only
   */
  private transient StringBuffer TmpStringBuffer = null;

  /**
   * Temporary minimum value for performance purposes only
   */
  private transient double TmpMinimumValue = 0.0d;

  /**
   * Temporary maximum value for performance purposes only
   */
  private transient double TmpMaximumValue = 0.0d;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Constructor for an instance of InstancesMetadata, wheras metadata of 
   * all data set attributes  is retrieved and stored. pInstances must not 
   * be null. Note, subsequent modifications of pInstances will not update 
   * metadata stored in this instance. Note, this class encapsulates 
   * redundandly stored metadata to provide an easy access to this 
   * information from a single class.
   *
   * @param pInstances the data set whose metadata should be stored
   */
  
  public InstancesMetadata(Instances pInstances) {
    
    // validate parameter value
    if (pInstances == null) {
      System.err.println("org.hypknowsys.algorithms.core.InstancesMetadata: "
      + "Parameter pInstances of constructor must not be null; exiting!");
      System.exit(-1);
    }
    
    this.AttributeMetadataList = new AttributeMetadata[pInstances
    .numAttributes()];
    this.ClassIndex = pInstances.classIndex();
    
    for (int i = 0; i < pInstances.numAttributes(); i++) {
      AttributeMetadataList[i] = new AttributeMetadata(pInstances
      .attribute(i));
    }
    for (int i = 0; i < pInstances.numInstances(); i++) {
      for (int j = 0; j < pInstances.numAttributes(); j++) {
        if ((i + 1) < pInstances.numInstances()) {
          AttributeMetadataList[j].updateMetadata(pInstances
          .instance(i), false);
        }
        else {
          AttributeMetadataList[j].updateMetadata(pInstances
          .instance(i), true);
        }
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the number of attributes.
   *
   * @return the number of attributes as an integer
   */
  
  public final int getNumberOfAttributes() {

    if (AttributeMetadataList != null) {
      return AttributeMetadataList.length;
    }
    else {
      return 0;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns the class attribute's index. Returns negative number
   * if it's undefined.
   *
   * @return the class index as an integer
   */
  
  public final int getClassIndex() {
    
    return ClassIndex;
    
  }
 
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns a string describing this class.
   *
   * @return a description of this class as a string
   */
  
  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    TmpStringBuffer.append(": NumberOfAttributes=");
    TmpStringBuffer.append(AttributeMetadataList.length);
    for (int i = 0; i < AttributeMetadataList.length; i++) {
      TmpStringBuffer.append("\n");
      TmpStringBuffer.append(AttributeMetadataList[i].toString());
    }
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns the name of attribute specified by pIndex.
   *
   * @param pIndex the index of the specified attribute.
   * @return the name of the specified attribute.
   */
  
  public final String getNameOfAttribute(int pIndex) {

    if (AttributeMetadataList != null && pIndex >= 0
    && pIndex < AttributeMetadataList.length) {
      return AttributeMetadataList[pIndex].getName();
    }
    else {
      return "UnlabeledAttribute" + pIndex;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns the type of attribute specified by pIndex as an integer that 
   * equals weka.core.Attribute.NUMERIC, weka.core.Attribute.NOMINAL, 
   * weka.core.Attribute.STRING, or weka.core.Attribute.DATE.
   *
   * @param pIndex the index of the specified attribute.
   * @return the type of the specified attribute as an integer.
   */
  
  public final int getTypeOfAttribute(int pIndex) {

    if (pIndex >= 0 && pIndex < AttributeMetadataList.length) {
      return AttributeMetadataList[pIndex].getType();
    }
    else {
      return Attribute.NUMERIC;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns the type of attribute specified by pIndex as a string: 
   * "NUMERIC" for weka.core.Attribute.NUMERIC, "NOMINAL" for 
   * weka.core.Attribute.NOMINAL, "STRING" for weka.core.Attribute.STRING, 
   * or "DATE" for weka.core.Attribute.DATE.
   *
   * @param pIndex the index of the specified attribute.
   * @return the type of the specified attribute as a string.
   */
  
  public final String getTypeOfAttributeAsString(int pIndex) {

    if (pIndex >= 0 && pIndex < AttributeMetadataList.length) {
      switch (AttributeMetadataList[pIndex].getType()) {
        case Attribute.NUMERIC: return "NUMERIC";
        case Attribute.NOMINAL: return "NOMINAL";
        case Attribute.STRING: return "STRING";
        case Attribute.DATE: return "DATE";
      }
    }

    return "NUMERIC";
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns minimum value of all non-missing values of the specified attribute,
   * equals Double.NaN if there are no instances or attribute type other than 
   * weka.core.Attribute.NUMERIC
   *
   * @param pIndex the index of the specified attribute.
   * @return the minimum value of the specified attribute.
   */
  
  public final double getMinimumOfAttribute(int pIndex) {

    if (pIndex >= 0 && pIndex < AttributeMetadataList.length) {
      return AttributeMetadataList[pIndex].getMinimum();
    }
    else {
      return Double.NaN;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns maximum value of all non-missing values of the specified attribute,
   * equals Double.NaN if there are no instances or attribute type other than 
   * weka.core.Attribute.NUMERIC
   *
   * @param pIndex the index of the specified attribute.
   * @return the maximum value of the specified attribute.
   */
  
  public final double getMaximumOfAttribute(int pIndex) {

    if (pIndex >= 0 && pIndex < AttributeMetadataList.length) {
      return AttributeMetadataList[pIndex].getMaximum();
    }
    else {
      return Double.NaN;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns mean value of all non-missing values of the specified attribute,
   * equals Double.NaN if there are no instances or attribute type other than 
   * weka.core.Attribute.NUMERIC
   *
   * @param pIndex the index of the specified attribute.
   * @return the mean value of the specified attribute.
   */
  
  public final double getMeanOfAttribute(int pIndex) {

    if (pIndex >= 0 && pIndex < AttributeMetadataList.length) {
      return AttributeMetadataList[pIndex].getMean();
    }
    else {
      return Double.NaN;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns variance value of all non-missing values of the specified 
   * attribute, equals Double.NaN if there are no instances or attribute type
   * other than weka.core.Attribute.NUMERIC
   *
   * @param pIndex the index of the specified attribute.
   * @return the variance of the specified attribute.
   */
  
  public final double getVarianceOfAttribute(int pIndex) {

    if (pIndex >= 0 && pIndex < AttributeMetadataList.length) {
      return AttributeMetadataList[pIndex].getVariance();
    }
    else {
      return Double.NaN;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns the standard deviation of all non-missing values of the specified 
   * attribute, equals Double.NaN if there are no instances or attribute type
   * other than weka.core.Attribute.NUMERIC
   *
   * @param pIndex the index of the specified attribute.
   * @return the standard deviation of the specified attribute.
   */
  
  public final double getStdDevOfAttribute(int pIndex) {

    if (pIndex >= 0 && pIndex < AttributeMetadataList.length) {
      return AttributeMetadataList[pIndex].getStdDev();
    }
    else {
      return Double.NaN;
    }
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns true, if the specified attribute is the class attribute
   * representing the target variable, or false otherwise.
   *
   * @param pIndex the index of the specified attribute.
   * @return true, if the specified attribute is the class attribute, 
   * or false otherwise.
   */
  
  public final boolean isClassAttribute(int pIndex) {
    
    if (pIndex == this.getClassIndex() && pIndex >= 0) {
      return true;
    }
    else {
      return false;
    }
    
  }
 
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns true, if the specified attribute is a numeric attribute,
   * or false otherwise.
   *
   * @param pIndex the index of the specified attribute.
   * @return true, if the specified attribute is  numeric attribute,
   * or false otherwise.
   */
  
  public final boolean isNumericAttribute(int pIndex) {
    
    if (this.getTypeOfAttribute(pIndex) == Attribute.NUMERIC) {
      return true;
    }
    else {
      return false;
    }
    
  }
 
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns true, if the specified attribute is a nominal attribute,
   * or false otherwise.
   *
   * @param pIndex the index of the specified attribute.
   * @return true, if the specified attribute is nominal attribute,
   * or false otherwise.
   */
  
  public final boolean isNominalAttribute(int pIndex) {
    
    if (this.getTypeOfAttribute(pIndex) == Attribute.NOMINAL) {
      return true;
    }
    else {
      return false;
    }
    
  }
 
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns true, if the specified attribute is a string attribute,
   * or false otherwise.
   *
   * @param pIndex the index of the specified attribute.
   * @return true, if the specified attribute is string attribute,
   * or false otherwise.
   */
  
  public final boolean isStringAttribute(int pIndex) {
    
    if (this.getTypeOfAttribute(pIndex) == Attribute.STRING) {
      return true;
    }
    else {
      return false;
    }
    
  }
 
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * Returns true, if the specified attribute is a date attribute,
   * or false otherwise.
   *
   * @param pIndex the index of the specified attribute.
   * @return true, if the specified attribute is date attribute,
   * or false otherwise.
   */
  
  public final boolean isDateAttribute(int pIndex) {
    
    if (this.getTypeOfAttribute(pIndex) == Attribute.DATE) {
      return true;
    }
    else {
      return false;
    }
    
  }
 
  /* ########## ########## ########## ########## ########## ######### */
  
  /**
   * [0; 1] normalizes a given value of a numeric attribute by subtracting 
   * its minimum value and dividing the result by its range. Returns 0.0,
   * if pValue < this.getMinimumOfAttribute(pIndex) and return 1.0, if pValue 
   * > this.getMaximumOfAttribute(pIndex). Returns Double.NaN, if the
   * attribute is not numeric or if this.getMinimumOfAttribute(pIndex) =
   * this.getMaximumOfAttribute(pIndex).
   *
   * @param pIndex the attribute's index.
   * @return the [0; 1] normalized attribute value.
   */
  
  public final double normalize(int pIndex, double pValue) {

    TmpMinimumValue = this.getMinimumOfAttribute(pIndex);
    TmpMaximumValue = this.getMaximumOfAttribute(pIndex);
    
    if (Double.isNaN(TmpMinimumValue)) {
      return Double.NaN;
    } else {
      if (Utils.eq(TmpMaximumValue, TmpMinimumValue)) {
        return 0.0d;
      }
      else if (pValue < TmpMinimumValue) {
         return 0.0d;
      }
      else if (pValue > TmpMaximumValue) {
         return 1.0d;
      }
      else {
        return (pValue - TmpMinimumValue) 
        / (TmpMaximumValue - TmpMinimumValue);
      }
    }
    
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

  public static void main(String args[]) {
  
    Instances testInstances = null;
    InstancesMetadata testInstancesMetadata = null;
    try {
      testInstances = new Instances(new StringReader(
      Tools.stringFromTextualSystemResource(
      "org/hypknowsys/algorithms/datasets/iris.arff")));
      System.out.println("org/hypknowsys/algorithms/datasets/iris.arff");
      testInstancesMetadata = new InstancesMetadata(testInstances);
      System.out.println(testInstancesMetadata.toString());
    }
    catch (IOException e) {
      System.exit(-1);
    }
      
    try {
      testInstances = new Instances(new StringReader(
      Tools.stringFromTextualSystemResource(
      "org/hypknowsys/algorithms/datasets/labor.arff")));
      System.out.println("org/hypknowsys/algorithms/datasets/labor.arff");
      testInstancesMetadata = new InstancesMetadata(testInstances);
      System.out.println(testInstancesMetadata.toString());
    }
    catch (IOException e) {
      System.exit(-1);
    }
      
  }
  
}