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

/**
 * Inspired by weka.core.Attribute: Revision 1.26
 * Copyright (C) 1999 Eibe Frank<p>
 * Inspired by weka.core.AttributeStats: Revision 1.6
 * Copyright (C) 1999 Len Trigg<p>
 * Inspired by weka.experiment.Stats: Revision 1.9
 * Copyright (C) 1999 Len Trigg<p>
 *
 * Class for storing metadata about an instance of weka.core.Attributes. 
 * Once AttributeMetadata has been created, it can't be changed at all.
 *
 * @version 0.1, 6 November 2003
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */

public class AttributeMetadata implements Serializable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  /** The attribute's index, default value: 0 */
  protected int Index = 0;

  /** The attribute's name, default value: "defaultAttributeName" */
  protected String Name = "defaultAttributeName";

  /** The attribute's type, default value: weka.core.Attribute.NUMERIC */
  protected int Type = weka.core.Attribute.NUMERIC;

  /** The number of instances in the associated data set, this.NumberOfValues
   * == this.NumberOfMissingValues + this.NumberOfNonMissingValues, equals 0 
   if there are no instances */
  protected int NumberOfValues = 0;

  /** The number of missing values of this attribute in the associated 
   * data set, equals 0 if there are no instances */
  protected int NumberOfMissingValues = 0;

  /** The number of non-missing values of this attribute in the associated 
   * data set, equals 0 if there are no instances */
  protected int NumberOfNonMissingValues = 0;

  /* ########## specific metadata for numeric attributes */

  /** The sum of all non-missing values, equals 0 if there are no instances 
   * or attribute type other than weka.core.Attribute.NUMERIC */
  protected double Sum = 0;

  /** The sum of all squared, non-missing values, equals 0 if there are no 
   * instances or attribute type other than weka.core.Attribute.NUMERIC */
  protected double SumOfSquared = 0;

  /** The sample variance of all non-missing values, equals Double.NaN if 
   * there are no instances, attribute type other than 
   * weka.core.Attribute.NUMERIC or method this.updateMetadata() has not 
   * been invoked with pIsLastInstance == true */
  protected double Variance = Double.NaN;

  /** The sample standard deviation of all non-missing values, equals 
   * Double.NaN if there are no instances, attribute type other than 
   * weka.core.Attribute.NUMERIC or method this.updateMetadata() has not 
   * been invoked with pIsLastInstance == true */
  protected double StdDev = Double.NaN;

  /** The mean of all non-missing values, defined as this.Sum / this.NumberOfNonMissingValues,
   * equals Double.NaN if there are no instances, attribute type other than 
   * weka.core.Attribute.NUMERIC or method this.updateMetadata() has not been 
   * invoked with pIsLastInstance == true */
  protected double Mean = Double.NaN;

  /** The minimum value of all non-missing values, equals Double.NaN if there 
   * are no instances or attribute type other than weka.core.Attribute.NUMERIC */
  protected double Minimum = Double.NaN;

  /** The maximum value of all non-missing values, equals Double.NaN if there
   * are no instances or attribute type other than weka.core.Attribute.NUMERIC */
  protected double Maximum = Double.NaN;
    
  /** The attributes range of all non-missing values, defined as this.Maximum - 
   * this.Minimum, equals Double.NaN if there are no instances, attribute type 
   * other than weka.core.Attribute.NUMERIC or method his.updateMetadata() has 
   * not been invoked with pIsLastInstance == true */
  protected double Range = Double.NaN;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Temporary string buffer for performance purposes only
   */
  private transient StringBuffer TmpStringBuffer = null;
  
  /**
   * Temporary double value for performance purposes only
   */
  private transient double TmpValue = Double.NaN;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Constructor for an instance of AttributeMetadata, wheras
   * metadata of pAttribute is retrieved and stored. pAttribute
   * must not be null. Note, subsequent modifications of pAttribute
   * will not update metadata stored in this instance. Note, this 
   * class encapsulates redundandly stored metadata to provide an 
   * easy access to this information from a single class.
   *
   * @param pAttribute the attribute whose metadata should be stored
   */
  
  public AttributeMetadata(Attribute pAttribute) {
    
    // validate parameter value
    if (pAttribute == null) {
      System.err.println("org.hypknowsys.algorithms.core.AttributeMetadata: "
      + "Parameter pAttribute of constructor must not be null; exiting!");
      System.exit(-1);
    }
    
    this.Index = pAttribute.index();
    this.Name = pAttribute.name();
    this.Type = pAttribute.type();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  /**
   * Returns the attribute's index in the corresponding Instances.
   *
   * @return the attribute's index.
   */
  
  public int getIndex() {
    
    return Index;
  
  }  

  /**
   * Returns the attribute's name.
   *
   * @return the attribute's name.
   */
  
  public String getName() {
    
    return Name;
  
  }  

  /**
   * Returns the attribute's type as an integer that equals
   * weka.core.Attribute.NUMERIC, weka.core.Attribute.NOMINAL, 
   * weka.core.Attribute.STRING, or weka.core.Attribute.DATE.
   *
   * @return the attribute's type as an integer.
   */
  
  public int getType() {
    
    return Type;
  
  }  

  /**
   * Returns the attribute's number of values.
   *
   * @return the attribute's number of values.
   */
  
  public int getNumberOfValues() {
    
    return NumberOfValues;
  
  }  

  /**
   * Returns the attribute's number of missing values.
   *
   * @return the attribute's number of missing values.
   */
  
  public int getNumberOfMissingValues() {
    
    return NumberOfMissingValues;
  
  }  

  /**
   * Returns the attribute's number of non-missing values.
   *
   * @return the attribute's number of non-missing values.
   */
  
  public int getNumberOfNonMissingValues() {
    
    return NumberOfNonMissingValues;
  
  }  

  /**
   * Returns the sum of all non-missing values, equals 0 if there are no 
   * instances or attribute type other than weka.core.Attribute.NUMERIC
   *
   * @return the attribute's sum of all non-missing values.
   */
  
  public double getSum() {
    
    return Sum;
  
  }  

  /**
   * Returns the sum of all squared, non-missing values, equals 0 if there are 
   * no instances or attribute type other than weka.core.Attribute.NUMERIC
   *
   * @return the attribute's sum of all squared, non-missing values.
   */
  
  public double getSumOfSquared() {
    
    return SumOfSquared;
  
  }  

  /**
   * Returns the sample variance of all non-missing values, equals 
   * Double.NaN if there are no instances, attribute type other than 
   * weka.core.Attribute.NUMERIC or method this.updateMetadata() has not 
   * been invoked with pIsLastInstance == true
   *
   * @return the attribute's sample variance.
   */
  
  public double getVariance() {
    
    return Variance;
  
  }  

  /**
   * Returns the sample standard deviation of all non-missing values, equals 
   * Double.NaN if there are no instances, attribute type other than 
   * weka.core.Attribute.NUMERIC or method this.updateMetadata() has not 
   * been invoked with pIsLastInstance == true
   *
   * @return the attribute's sample standard variance.
   */
  
  public double getStdDev() {
    
    return StdDev;
  
  }  

  /**
   * Returns minimum value of all non-missing values, equals Double.NaN if 
   * there are no instances or attribute type other than 
   * weka.core.Attribute.NUMERIC
   *
   * @return the attribute's minimum value.
   */
  
  public double getMinimum() {
    
    return Minimum;
  
  }  

  /**
   * Returns maximum value of all non-missing values, equals Double.NaN if there
   * are no instances or attribute type other than weka.core.Attribute.NUMERIC
   *
   * @return the attribute's maximum value.
   */
  
  public double getMaximum() {
    
    return Maximum;
  
  }  

  /**
   * Returns attributes range of all non-missing values, defined as this.Maximum 
   * - this.Minimum, equals Double.NaN if there are no instances, attribute type 
   * other than weka.core.Attribute.NUMERIC or method his.updateMetadata() has 
   * not been invoked with pIsLastInstance == true
   *
   * @return the attribute's range.
   */
  
  public double getRange() {
    
    return Range;
  
  }  

  /**
   * Returns the mean of all non-missing values, defined as this.Sum / 
   * this.NumberOfNonMissingValues, equals Double.NaN if there are no instances,
   * attribute type other than weka.core.Attribute.NUMERIC or method 
   * this.updateMetadata() has not been invoked with pIsLastInstance == true
   *
   * @return the attribute's sample standard variance.
   */
  
  public double getMean() {
    
    return Mean;
  
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
    TmpStringBuffer.append(": Name=");
    TmpStringBuffer.append(Name);
    TmpStringBuffer.append("; Index=");
    TmpStringBuffer.append(Index);
    TmpStringBuffer.append("; Type=");
    TmpStringBuffer.append(Type);
    TmpStringBuffer.append("; MissVal=");
    TmpStringBuffer.append(Utils.doubleToString(NumberOfMissingValues
    / (double)NumberOfValues, 3));
    if (Type == Attribute.NUMERIC) {
      TmpStringBuffer.append("; Min=");
      TmpStringBuffer.append(Utils.doubleToString(Minimum, 3));
      TmpStringBuffer.append("; Max=");
      TmpStringBuffer.append(Utils.doubleToString(Maximum, 3));
      TmpStringBuffer.append("; Range=");
      TmpStringBuffer.append(Utils.doubleToString(Range, 3));
      TmpStringBuffer.append("; Mean=");
      TmpStringBuffer.append(Utils.doubleToString(Mean, 3));
      TmpStringBuffer.append("; StdDev=");
      TmpStringBuffer.append(Utils.doubleToString(StdDev, 3));
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
   * Updates the metadata based on the value of pInstance specified by
   * component index this.Index as set by constructor. If pIsLastInstance 
   * is true, descriptive summary statistics are computed based the values 
   * of all preceding instances. Class SparseInstance is supported.
   *
   * @param pInstance the current instance, whose attribute value is to
   * be processed
   * @param pIsLastInstance triggers the computation of descriptive
   * statistics
   */
  
  public void updateMetadata(Instance pInstance, boolean pIsLastInstance) {
    
    // validate parameter values
    if (pInstance == null) {
      System.err.println("org.hypknowsys.algorithms.core.AttributeMetadata: "
      + "Parameter pInstance of updateMetadata() must not be null; returning!");
      return;
    }
    
    TmpValue = pInstance.value(Index);
    NumberOfValues++;
    if (Instance.isMissingValue(TmpValue)) {
      NumberOfMissingValues++;
    }
    else {
      NumberOfNonMissingValues++;
      switch (Type) {
        case weka.core.Attribute.NUMERIC: {
          if (Double.isNaN(Minimum)) {
            Minimum = TmpValue;
          }
          else if (TmpValue < Minimum) {
            Minimum = TmpValue;
          }
          if (Double.isNaN(Maximum)) {
            Maximum = TmpValue;
          }
          else if (TmpValue > Maximum) {
            Maximum = TmpValue;
          }
          Sum += TmpValue;
          SumOfSquared += TmpValue * TmpValue;
        }
      }
    }
            
    if (pIsLastInstance) {
      Range = Double.NaN;
      Mean = Double.NaN;
      StdDev = Double.NaN;
      switch (Type) {
        case weka.core.Attribute.NUMERIC: {
          if (NumberOfNonMissingValues > 0) {
            Range = Maximum - Minimum;
            Mean = Sum / NumberOfNonMissingValues;
            Variance = Double.POSITIVE_INFINITY;
            StdDev = Double.POSITIVE_INFINITY;
            if (NumberOfNonMissingValues > 1) {
              Variance = SumOfSquared - (Sum * Sum) / NumberOfNonMissingValues;
              Variance /= (NumberOfNonMissingValues - 1);
              if (Variance < 0) {
                Variance = 0.0d;  // round StdDev to zero
              }
              StdDev = Math.sqrt(Variance);
            }
          }
        }
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

  public static void main(String args[]) {}
  
}