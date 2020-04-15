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

package org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics;

import java.util.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.diasdem.server.*;

/**
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ComputeTermFrequencyStatisticsParameter extends DiasdemScriptableTaskParameter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected String CollectionFileName = null;
  protected String TfStatisticsFileName = null;
  protected boolean ExportTextsInCsvFormat = false;
  protected boolean ExportStatisticsInCsvFormat = false;
  protected boolean ExportStatisticsInHtmlFormat = false;
  protected boolean ExcludeNumbersEtc = false;
  protected boolean ComputeConditionalFrequencies = false;
  protected String ConditionTextUnitMatchedByRegex = null;
  protected int ConditionClusterIdOfTextUnit = UNSPECIFIED_INT_CONDITION;
  protected String ConditionClusterLabelOfTextUnit = UNSPECIFIED_STRING_CONDITION;
  protected int ConditionIterationOfTextUnit = UNSPECIFIED_INT_CONDITION;
  protected boolean MapTokensOntoDescriptors = false;
  protected String ThesaurusFileName = null;
  protected int VectorDimensions = ALL_DESCRIPTORS;
  protected String DescriptorsScopeNotesContain = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String TASK_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics" +
  ".ComputeTermFrequencyStatisticsTask";
  private final static String PARAMETER_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics" +
  ".ComputeTermFrequencyStatisticsParameterPanel";
  
  private final static String COLLECTION_FILE_NAME =
  "CollectionFileName";
  private final static String TF_STATISTICS_FILE_NAME =
  "TfStatisticsFileName";
  private final static String EXPORT_TEXTS_IN_CSV_FORMAT =
  "ExportTextsInCsvFormat";
  private final static String EXPORT_STATISTICS_IN_CSV_FORMAT =
  "ExportStatisticsInCsvFormat";
  private final static String EXPORT_STATISTICS_IN_HTML_FORMAT =
  "ExportStatisticsInHtmlFormat";
  private final static String EXCLUDE_NUMBERS_ETC =
  "ExcludeNumbersEtc";
  private final static String COMPUTE_CONDITIONAL_FREQUENCIES =
  "ComputeConditionalFrequencies";
  private final static String CONDITION_TEXT_UNIT_MATCHED_BY_REGEX =
  "ConditionTermInTextUnit";
  private final static String CONDITION_CLUSTER_ID_OF_TEXT_UNIT =
  "ConditionClusterIdOfTextUnit";
  private final static String CONDITION_CLUSTER_LABEL_OF_TEXT_UNIT =
  "ConditionClusterLabelOfTextUnit";
  private final static String CONDITION_ITERATION_OF_TEXT_UNIT =
  "ConditionIterationOfTextUnit";
  private final static String MAP_TOKENS_ONTO_DESCRIPTORS =
  "MapTokensOntoDescriptors";
  private final static String THESAURUS_FILE_NAME =
  "ThesaurusFileName";
  private final static String VECTOR_DIMENSIONS =
  "VectorDimensions";
  private final static String DESCRIPTOR_SCOPE_NOTES_CONTAIN =
  "DescriptorsScopeNotesContain";

  public final static int UNSPECIFIED_INT_CONDITION = Integer.MIN_VALUE;
  public final static String UNSPECIFIED_STRING_CONDITION = "";
  
  public final static int ALL_DESCRIPTORS = 0;
  public final static int SPECIFIED_DESCRIPTORS = 1;
  public final static int NOT_SPECIFIED_DESCRIPTORS = 2;
  public final static String[] VECTOR_DIMENSIONS_OPTIONS = {
    "All Descriptors in Thesaurus", 
    "Descriptors whose Scope Notes Contain String", 
    "Descriptors whose Scope Notes Don't Contain String" 
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ComputeTermFrequencyStatisticsParameter() {
    
    super();
    
    TaskClassName = TASK_CLASS_NAME;
    ParameterPanelClassName = PARAMETER_PANEL_CLASS_NAME;
    CollectionFileName = null;
    TfStatisticsFileName = null;
    ExportTextsInCsvFormat = false;
    ExportStatisticsInCsvFormat = false;
    ExportStatisticsInHtmlFormat = false;
    ExcludeNumbersEtc = false;
    ComputeConditionalFrequencies = false;
    ConditionTextUnitMatchedByRegex = null;
    ConditionClusterIdOfTextUnit = UNSPECIFIED_INT_CONDITION;
    ConditionClusterLabelOfTextUnit = UNSPECIFIED_STRING_CONDITION;
    ConditionIterationOfTextUnit = UNSPECIFIED_INT_CONDITION;
    MapTokensOntoDescriptors = false;
    ThesaurusFileName = null;
    VectorDimensions = ALL_DESCRIPTORS;
    DescriptorsScopeNotesContain = null;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ComputeTermFrequencyStatisticsParameter(String pCollectionFileName, 
  String pTfStatisticsFileName, boolean pExportTextsInCsvFormat, 
  boolean pExportStatisticsInCsvFormat, boolean pExportStatisticsInHtmlFormat,
  boolean pExcludeNumbersEtc, boolean pComputeConditionalFrequencies,
  String pConditionTextUnitMatchedByRegex, int pConditionClusterIdOfTextUnit,
  String pConditionClusterLabelOfTextUnit, int pConditionIterationOfTextUnit,
  boolean pMapTokensOntoDescriptors, String pThesaurusFileName,
  String pVectorDimensions, String pDescriptorsScopeNotesContain) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    TfStatisticsFileName = pTfStatisticsFileName;
    ExportTextsInCsvFormat = pExportTextsInCsvFormat;
    ExportStatisticsInCsvFormat = pExportStatisticsInCsvFormat;
    ExportStatisticsInHtmlFormat = pExportStatisticsInHtmlFormat;
    ExcludeNumbersEtc = pExcludeNumbersEtc;
    ComputeConditionalFrequencies = pComputeConditionalFrequencies;
    ConditionTextUnitMatchedByRegex = pConditionTextUnitMatchedByRegex;
    ConditionClusterIdOfTextUnit = pConditionClusterIdOfTextUnit;
    ConditionClusterLabelOfTextUnit = pConditionClusterLabelOfTextUnit;
    ConditionIterationOfTextUnit = pConditionIterationOfTextUnit;
    MapTokensOntoDescriptors = pMapTokensOntoDescriptors;
    ThesaurusFileName = pThesaurusFileName;
    VectorDimensions = ALL_DESCRIPTORS;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    
    if (pVectorDimensions.equals(VECTOR_DIMENSIONS_OPTIONS[ALL_DESCRIPTORS])) 
      VectorDimensions = ALL_DESCRIPTORS;
    else if (pVectorDimensions.equals(VECTOR_DIMENSIONS_OPTIONS[SPECIFIED_DESCRIPTORS])) 
      VectorDimensions = SPECIFIED_DESCRIPTORS;
    else
      VectorDimensions = NOT_SPECIFIED_DESCRIPTORS;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public ComputeTermFrequencyStatisticsParameter(String pCollectionFileName, 
  String pTfStatisticsFileName, boolean pExportTextsInCsvFormat, 
  boolean pExportStatisticsInCsvFormat, boolean pExportStatisticsInHtmlFormat,
  boolean pExcludeNumbersEtc, boolean pComputeConditionalFrequencies,
  String pConditionTextUnitMatchedByRegex, int pConditionClusterIdOfTextUnit,
  String pConditionClusterLabelOfTextUnit, int pConditionIterationOfTextUnit,
  boolean pMapTokensOntoDescriptors, String pThesaurusFileName,
  int pVectorDimensions, String pDescriptorsScopeNotesContain) {
    
    this();
    
    CollectionFileName = pCollectionFileName;
    TfStatisticsFileName = pTfStatisticsFileName;
    ExportTextsInCsvFormat = pExportTextsInCsvFormat;
    ExportStatisticsInCsvFormat = pExportStatisticsInCsvFormat;
    ExportStatisticsInHtmlFormat = pExportStatisticsInHtmlFormat;
    ExcludeNumbersEtc = pExcludeNumbersEtc;
    ComputeConditionalFrequencies = pComputeConditionalFrequencies;
    ConditionTextUnitMatchedByRegex = pConditionTextUnitMatchedByRegex;
    ConditionClusterIdOfTextUnit = pConditionClusterIdOfTextUnit;
    ConditionClusterLabelOfTextUnit = pConditionClusterLabelOfTextUnit;
    ConditionIterationOfTextUnit = pConditionIterationOfTextUnit;
    MapTokensOntoDescriptors = pMapTokensOntoDescriptors;
    ThesaurusFileName = pThesaurusFileName;
    VectorDimensions = pVectorDimensions;
    DescriptorsScopeNotesContain = pDescriptorsScopeNotesContain;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCollectionFileName() {
    return CollectionFileName; }
  public String getTfStatisticsFileName() {
    return TfStatisticsFileName; }
  public boolean getExportTextsInCsvFormat() {
    return ExportTextsInCsvFormat; }
  public boolean exportTextsInCsvFormat() {
    return ExportTextsInCsvFormat; }
  public boolean getExportStatisticsInCsvFormat() {
    return ExportStatisticsInCsvFormat; }
  public boolean exportStatisticsInCsvFormat() {
    return ExportStatisticsInCsvFormat; }
  public boolean getExportStatisticsInHtmlFormat() {
    return ExportStatisticsInHtmlFormat; }
  public boolean exportStatisticsInHtmlFormat() {
    return ExportStatisticsInHtmlFormat; }
  public boolean getExcludeNumbersEtc() {
    return ExcludeNumbersEtc; }
  public boolean excludeNumbersEtc() {
    return ExcludeNumbersEtc; }
  public boolean getComputeConditionalFrequencies() {
    return ComputeConditionalFrequencies; }
  public boolean computeConditionalFrequencies() {
    return ComputeConditionalFrequencies; }
  public String getConditionTextUnitMatchedByRegex() {
    return ConditionTextUnitMatchedByRegex; }
  public int getConditionClusterIdOfTextUnit() {
    return ConditionClusterIdOfTextUnit; }
  public String getConditionClusterLabelOfTextUnit() {
    return ConditionClusterLabelOfTextUnit; }
  public int getConditionIterationOfTextUnit() {
    return ConditionIterationOfTextUnit; }
  public boolean mapTokensOntoDescriptors() {
    return MapTokensOntoDescriptors; }
  public String getThesaurusFileName() { 
    return ThesaurusFileName; }
  public int getVectorDimensions() { 
    return VectorDimensions; }
  public String getDescriptorsScopeNotesContain() { 
    return DescriptorsScopeNotesContain; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setExportTextsInCsvFormat(boolean pExportTextsInCsvFormat) {
    ExportTextsInCsvFormat = pExportTextsInCsvFormat; }
  public void setExportStatisticsInCsvFormat(
  boolean pExportStatisticsInCsvFormat) {
    ExportStatisticsInCsvFormat = pExportStatisticsInCsvFormat; }
  public void setExportStatisticsInHtmlFormat(
  boolean pExportStatisticsInHtmlFormat) {
    ExportStatisticsInHtmlFormat = pExportStatisticsInHtmlFormat; }
  public void setExcludeNumbersEtc(boolean pExcludeNumbersEtc) {
    ExcludeNumbersEtc = pExcludeNumbersEtc; }
  public void setComputeConditionalFrequencies(
  boolean pComputeConditionalFrequencies) {
    ComputeConditionalFrequencies = pComputeConditionalFrequencies; }
  public void setMapTokensOntoDescriptors(
  boolean pMapTokensOntoDescriptors) {
    MapTokensOntoDescriptors = pMapTokensOntoDescriptors; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface ScriptableTaskParameter methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public org.jdom.Element getParameterAttributesAsJDomElement() {
    
    ParameterAttributes = new TreeMap();
    ParameterAttributes.put(COLLECTION_FILE_NAME, CollectionFileName);
    ParameterAttributes.put(TF_STATISTICS_FILE_NAME, TfStatisticsFileName);
    ParameterAttributes.put(EXPORT_TEXTS_IN_CSV_FORMAT,
    Tools.boolean2String(ExportTextsInCsvFormat));
    ParameterAttributes.put(EXPORT_STATISTICS_IN_CSV_FORMAT,
    Tools.boolean2String(ExportStatisticsInCsvFormat));
    ParameterAttributes.put(EXPORT_STATISTICS_IN_HTML_FORMAT,
    Tools.boolean2String(ExportStatisticsInHtmlFormat));
    ParameterAttributes.put(EXCLUDE_NUMBERS_ETC,
    Tools.boolean2String(ExcludeNumbersEtc));
    ParameterAttributes.put(COMPUTE_CONDITIONAL_FREQUENCIES,
    Tools.boolean2String(ComputeConditionalFrequencies));
    ParameterAttributes.put(CONDITION_TEXT_UNIT_MATCHED_BY_REGEX,
    ConditionTextUnitMatchedByRegex);
    ParameterAttributes.put(CONDITION_CLUSTER_ID_OF_TEXT_UNIT,
    Tools.int2String(ConditionClusterIdOfTextUnit));
    ParameterAttributes.put(CONDITION_CLUSTER_LABEL_OF_TEXT_UNIT,
    ConditionClusterLabelOfTextUnit);
    ParameterAttributes.put(CONDITION_ITERATION_OF_TEXT_UNIT,
    Tools.int2String(ConditionIterationOfTextUnit));
    ParameterAttributes.put(MAP_TOKENS_ONTO_DESCRIPTORS,
    Tools.boolean2String(MapTokensOntoDescriptors));
    ParameterAttributes.put(THESAURUS_FILE_NAME, 
    ThesaurusFileName);
    ParameterAttributes.put(VECTOR_DIMENSIONS, 
    Tools.int2String(VectorDimensions));
    ParameterAttributes.put(DESCRIPTOR_SCOPE_NOTES_CONTAIN, 
    DescriptorsScopeNotesContain);
    
    return super.getParameterAttributesAsJDomElement(ParameterAttributes);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setParameterAttributesFromJDomElement(
  org.jdom.Element pParameterAttributes) {
    
    ParameterAttributes = super.getParameterAttributesAsTreeMap(
    pParameterAttributes);
    
    CollectionFileName = (String)ParameterAttributes
    .get(COLLECTION_FILE_NAME);
    TfStatisticsFileName = (String)ParameterAttributes
    .get(TF_STATISTICS_FILE_NAME);
    ExportTextsInCsvFormat = Tools.string2Boolean(
    (String)ParameterAttributes.get(EXPORT_TEXTS_IN_CSV_FORMAT));
    ExportStatisticsInCsvFormat = Tools.string2Boolean(
    (String)ParameterAttributes.get(EXPORT_STATISTICS_IN_CSV_FORMAT));
    ExportStatisticsInHtmlFormat = Tools.string2Boolean(
    (String)ParameterAttributes.get(EXPORT_STATISTICS_IN_HTML_FORMAT));
    ExcludeNumbersEtc = Tools.string2Boolean(
    (String)ParameterAttributes.get(EXCLUDE_NUMBERS_ETC));
    ComputeConditionalFrequencies = Tools.string2Boolean(
    (String)ParameterAttributes.get(COMPUTE_CONDITIONAL_FREQUENCIES));
    ConditionTextUnitMatchedByRegex = (String)ParameterAttributes
    .get(CONDITION_TEXT_UNIT_MATCHED_BY_REGEX);
    ConditionClusterIdOfTextUnit = Tools.string2Int(
    (String)ParameterAttributes.get(CONDITION_CLUSTER_ID_OF_TEXT_UNIT));
    ConditionClusterLabelOfTextUnit = (String)ParameterAttributes
    .get(CONDITION_CLUSTER_LABEL_OF_TEXT_UNIT);
    ConditionIterationOfTextUnit = Tools.string2Int(
    (String)ParameterAttributes.get(CONDITION_ITERATION_OF_TEXT_UNIT));
    MapTokensOntoDescriptors = Tools.string2Boolean(
    (String)ParameterAttributes.get(MAP_TOKENS_ONTO_DESCRIPTORS));
    ThesaurusFileName = (String)ParameterAttributes
    .get(THESAURUS_FILE_NAME);
    ThesaurusFileName = (String)ParameterAttributes
    .get(THESAURUS_FILE_NAME);
    VectorDimensions = Tools.string2Int(
    (String)ParameterAttributes.get(VECTOR_DIMENSIONS));
    DescriptorsScopeNotesContain = (String)ParameterAttributes
    .get(DESCRIPTOR_SCOPE_NOTES_CONTAIN);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
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