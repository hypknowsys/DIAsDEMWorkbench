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

import java.util.*;
import java.io.*;
import org.hypknowsys.misc.util.*;

/**
 * A MintQuery contains all necessary information about the MINT query to be 
 * executed: Its variable, its template and all predicates. The calling class
 * may have to check whether a MintQuery is valid or not. The attributes are
 * set by MintQueryParser. There are 3 Tries being used during the query's
 * execution: RejectedPatterns, SuccessfulPatterns and SuccessfulSubPatterns. 
 * They contain patterns (Trails) and the corresponding MintPatternDescriptors 
 * in order to speed up the query's execution. After the queriy's execution, 
 * this.generateSuccessfulPatternDescriptors() must be called to generate the 
 * Array of successful MintPatternDescriptors. A MintQuery contains the 
 * following attributes: ResultString, a ResultTree (AggregateTree) and a 
 * ResultPattern (NavigationPattern). These attributes must be set by the 
 * calling class. <p>
 *
 * Modified by tveit 5/1999: countVariables() <p>
 *
 * Modified by kwinkler 08/2000: PatternOutput of successful patterns and
 * successful subpattern generated when adding pattern to corresponding trie;
 * methods addSuccessfulPattern and addSuccessfulSubPattern modified: signature
 * now also includes the current WumServer; attributes 
 * SuccessfulPattternsOutput and SuccessfulSubPatternsOutput added to store
 * all output information in one place
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MintQuery implements Serializable {

  // ########## attributes ##########

  private String QueryString = null; 
  private boolean ValidQuery = true;
  private int ResultType = RESULT_UNKNOWN;
  
  private MintVariable[] Variables = null;
  private MintTemplate Template = null;
  private MintPredicate[] Predicates = null;
  
  private MintPatternDescriptor PatternDescriptor = null;
  private Vector PatternDescriptors = null;
  private transient Iterator PatternDescriptorsIterator = null;
  private AggregateTree ResultTree = null;
  private NavigationPattern  ResultPattern = null;
  private String ResultString = null;

  // added by kwinkler 1/2000 to show navigation pattern of loaded queries
  private boolean LoadQuery = false;
  private NavigationPattern_SelfContained[] AllNavigationPatterns = null;

  // added by kwinkler 7/2000 to support report facility
  private String ReportQuestion = null;
  private String ReportAnswer = null;
  private String ReportRemarks = null;  
  private String ExecutionTime = null;

  private StringArrayTrie RejectedPatterns = null;
  private StringArrayTrie SuccessfulPatterns = null;
  private StringArrayTrie SuccessfulSubPatterns = null;
  // added by kwinkler 8/2000
  private MintPatternOutput PatternOutput = null;
  private Vector SuccessfulPatternsOutput = null;
  private Vector SuccessfulSubPatternsOutput = null;
  
  public final static int RESULT_UNKNOWN = -100;
  public final static int SYNTAX_ERROR = -1;
  public final static int INTERMEDIATE_RESULT = 0;
  public final static int FINAL_RESULT = 1;

  // ########## constructors ##########
  
/**
 * constructs an empty MintQuery
 */  

  public MintQuery() {

    QueryString = "";   ValidQuery = true;                                  
    Variables = null;   Template = null;   Predicates = null;      
    PatternDescriptor = null;   PatternDescriptors = null;
    PatternDescriptorsIterator = null;
    ResultTree = null;   ResultPattern = null;   ResultString = "";
    RejectedPatterns = new StringArrayTrie();
    SuccessfulPatterns = new StringArrayTrie();
    SuccessfulSubPatterns = new StringArrayTrie();  

    SuccessfulPatternsOutput = new Vector();
    SuccessfulSubPatternsOutput = new Vector();
    ExecutionTime = "";

    LoadQuery = false;
    AllNavigationPatterns = null;

    ReportQuestion = ""; ReportAnswer = ""; 
    ReportRemarks = "";   ExecutionTime = "";

  }  
  
/**
 * constructs an MintQuery with the given QueryString
 */  

  public MintQuery(String pQueryString) {

    QueryString = pQueryString;   ValidQuery = true;    
    Variables = null;   Template = null;   Predicates = null;      
    PatternDescriptor = null;   PatternDescriptors = null;
    PatternDescriptorsIterator = null;
    ResultTree = null;   ResultPattern = null;   ResultString = "";
    RejectedPatterns = new StringArrayTrie();
    SuccessfulPatterns = new StringArrayTrie();
    SuccessfulSubPatterns = new StringArrayTrie();  

    SuccessfulPatternsOutput = new Vector();
    SuccessfulSubPatternsOutput = new Vector(); 
    
    LoadQuery = false;
    AllNavigationPatterns = null;
          
    ReportQuestion = ""; ReportAnswer = ""; ReportRemarks = "";
    ExecutionTime = "";
    
  }    
  
  // ########## mutator methods ##########

  public void setResultType(int pResultType) 
    { ResultType = pResultType; }  
  public void setVariables(MintVariable[] pVariables) 
    { Variables = pVariables; }
  public void setTemplate(MintTemplate pTemplate) 
    { Template = pTemplate; }
  public void setPredicates(MintPredicate[] pPredicates) 
    { Predicates = pPredicates; }
  public void setResultString(String pResultString) 
    { ResultString = pResultString; }
  public void setResultTree(AggregateTree pAggregateTree) 
    { ResultTree = pAggregateTree; }
  public void setPatternDescriptor(MintPatternDescriptor pPatternDescriptor) 
    { PatternDescriptor = pPatternDescriptor; }  
  public void setIsValid() 
    { ValidQuery = true; }
  public void setIsNotValid() 
    { ValidQuery = false; }
  public void clearRejectedPatterns() 
    { RejectedPatterns = new StringArrayTrie(); }
  public void clearSuccessfulPatterns() 
    { SuccessfulPatterns = new StringArrayTrie(); }
  public void clearSuccessfulSubPatterns() 
    { SuccessfulSubPatterns = new StringArrayTrie(); }

  public void setLoadQuery(boolean pLoadQuery) 
    { LoadQuery = pLoadQuery; }
  public void setAllNavigationPatterns
    (NavigationPattern_SelfContained[] pAllNavigationPatterns)
    { AllNavigationPatterns = pAllNavigationPatterns; }   
  public void resetAllNavigationPatterns() { AllNavigationPatterns = null; }   

  public void setReportQuestion(String pReportQuestion) 
    { ReportQuestion = pReportQuestion; }
  public void setReportAnswer(String pReportAnswer) 
    { ReportAnswer = pReportAnswer; }
  public void setReportRemarks(String pReportRemarks) 
    { ReportRemarks = pReportRemarks; }
  public void setExecutionTime(String pExecutionTime) 
    { ExecutionTime = pExecutionTime; }
  
  
  // ########## accessor methods ##########
  
  public int getResultType() 
    { return ResultType; }
  public MintTemplate getTemplate() 
    { return Template; }
  public String getQueryString() 
    { return QueryString; }
  public String getResultString() 
    { return ResultString; }
  public AggregateTree getResultTree() 
    { return ResultTree; }
  public MintPatternDescriptor getPatternDescriptor() 
    { return PatternDescriptor; }
  public boolean isValid()
    { return ValidQuery; }

  public String getReportQuestion() 
    { return ReportQuestion; }
  public String getReportAnswer() 
    { return ReportAnswer; }
  public String getReportRemarks() 
    { return ReportRemarks; }
  public String getExecutionTime() 
    { return ExecutionTime; }

  public Vector getSuccessfulPatternsOutput() { 
    return SuccessfulPatternsOutput; }
  public Vector getSuccessfulSubPatternsOutput() { 
    return SuccessfulSubPatternsOutput; }
  public int countSuccessfulPatterns() { 
    return SuccessfulPatternsOutput.size(); }
  public int getSuccessfulSubPatterns() { 
    return SuccessfulSubPatternsOutput.size(); }

  //########### countVariables by tveit
  public int countVariables() { return Variables.length; }

  // ########## standard methods ##########
  
  public String toString() {
    
    String vResult = "MintQuery: \n\n" + QueryString + "\n";
    
    if (ValidQuery) {
      for (int i = 0; i < Variables.length; i++) 
        vResult += "\n" + Variables[i].toString();
      vResult += "\n\n" + Template.toString();
      vResult += "\n";
      if (Predicates != null) 
        for (int i = 0; i < Predicates.length; i++) 
          vResult += "\n" + Predicates[i].toString();
    }
    else
      vResult += "\nThis Query is not valid.";
      
    return vResult;
    
  }  // toString()  
  
  public boolean isLoadQuery() { 

    if (LoadQuery == true) 
      return true; 
    else 
      return false;

  }  // isLoadQuery()

  public NavigationPattern_SelfContained getAllNavigationPatterns(int pIndex) {

    if ( (pIndex >= 0) && (pIndex < AllNavigationPatterns.length) ) 
      return AllNavigationPatterns[pIndex]; 
    else 
      return null; 
  }
  
  /**
    * modified by kwinkler 11/99
    * allocates all content predicates to the appropiate variables, all
    * statistic predicates to the template and all wildcard predicates to the
    * corresponding search expression in the template
    */  
  
  public void allocatePredicates() {
  
    // content predicates
    for (int i = 0; i < Variables.length; i++)
      for (int j = 0; j < Predicates.length; j++)
        if ( ( Variables[i].getName().equals( Predicates[j].getVariable() ) ) &&
          ( Predicates[j].getType() == MintPredicate.CONTENT_PREDICATE ) &&
          ( Predicates[j].getPredicateClass() == 
            MintPredicate.VARIABLE_PREDICATE) )
          Variables[i].addContentPredicate( Predicates[j] );
    
    // statistics predicates and wildcard predicates
    for (int j = 0; j < Predicates.length; j++) {
      if ( ( Predicates[j].getType() == MintPredicate.STATISTICS_PREDICATE ) &&
         (Predicates[j].getPredicateClass() == 
          MintPredicate.VARIABLE_PREDICATE) )
        Template.addStatisticsPredicate( Predicates[j] );
      else 
        if ( Predicates[j].getPredicateClass() == 
          MintPredicate.WILDCARD_PREDICATE )
          Template.addWildcardPredicate( Predicates[j] );
    }
      
  }  // allocatePredicates()

  /**
   * returns the MintVariable specified by its position in the array Variables
   * @param pIndex 0 <= pIndex < Variables.length
   * @return corresponding MintVariable or null if pIndex is out of valid range
   */  
  
  public MintVariable getVariable(int pIndex) {
  
    if ( (pIndex >= 0) && (pIndex <= Variables.length) )
      return Variables[pIndex];
    else
      return null;
  
  }
  
  // added by kwinkler 8/2000

  public Vector getAllVariableNames() {
  
    Vector result = new Vector();
    String[] variableNames = Template.getVariableNames();
    for (int i = 0; i < variableNames.length; i++)
      result.add( variableNames[i] );
  
    return result;  

  }
  
  // added by kwinkler 8/2000
  // return index of gicen variable or -1 if it does not exist

  public int getVariableIndex(String pVariableName) {
  
    int variableIndex = -1;
    for (int i = 0; i < Variables.length; i++)
      if ( Variables[i].getName().equals(pVariableName) ) {
        variableIndex = i;
        break;
      }
  
    return variableIndex;  

  }
  
  /**
   * returns the MintVariable specified by its name
   * @param pVariableName name of the variable to return
   * @return corresponding MintVariable or null if pVariableName doesn't exist
   */  
  
  public MintVariable getVariable(String pVariableName) {
  
    for (int i = 0; i < Variables.length; i++)
      if ( Variables[i].getName().equals(pVariableName) ) return Variables[i];
  
    return null;
  
  }  // getVariable()  

  /**
   * returns the MintPredicate specified by its position in the array
   * Predicates
   * @param pIndex 0 <= pIndex < Predicates.length
   * @return corresponding MintPredicate or null if pIndex is out of valid 
   * range
   */  
  
  public MintPredicate getPredicate(int pIndex) {
  
    if ( (pIndex >= 0) && (pIndex <= Predicates.length) )
      return Predicates[pIndex];
    else
      return null;
  
  } 

  /**
   * generates the Array PatternDescriptors by copying all successful 
   * MintPatternDescriptors from the Trie SuccessfulPatterns, must be
   * called after the execution of this MintQuery in order to access
   * the result pattern descriptors
   * 
   * modified by kwinkler 8/2000: creates output vector of
   * successful patterns and subpattern as well
   */
  
  public void generateSuccessfulPatternDescriptors() {
  
    int counter = 1;
    MintPatternDescriptor descriptor = null;

    // make sure that ID in PatternOutput and PatternDescriptor are equivalent
    PatternDescriptors = new Vector();
    SuccessfulPatternsOutput = new Vector();
    Enumeration oSuccessfulPattern = this.successfulPatternsEnumerate();
    while ( oSuccessfulPattern.hasMoreElements() ) {
      descriptor = (MintPatternDescriptor)oSuccessfulPattern.nextElement();
      descriptor.getPatternOutput().setPatternID(counter++);
      SuccessfulPatternsOutput.add( descriptor.getPatternOutput() );
      PatternDescriptors.add(descriptor);
    }
    SuccessfulSubPatternsOutput = new Vector();
    Enumeration oSuccessfulSubPattern = this.successfulSubPatternsEnumerate();
    while ( oSuccessfulSubPattern.hasMoreElements() ) {
      descriptor = (MintPatternDescriptor)oSuccessfulSubPattern.nextElement();
      descriptor.getPatternOutput().setPatternID(counter++);
      SuccessfulSubPatternsOutput.add( descriptor.getPatternOutput() );
    }
    
  }  // generateSuccessfulPatternDescriptors()
  
  /**
   * @return first successful MintPatternDescriptor of this query or null 
   * if the Array PatternDescriptors is empty, must be called to start a 
   * new traversal through all successful patterns, call 
   * this.generateSuccessfulPatternDescriptors() before
   */
  
  public MintPatternDescriptor getFirstSuccessfulPatternDescriptor() {
  
    if (PatternDescriptors == null) return null;
    
    PatternDescriptorsIterator = PatternDescriptors.iterator();
    if ( PatternDescriptorsIterator.hasNext() )      
      return (MintPatternDescriptor)PatternDescriptorsIterator.next();     
    else
      return null;
    
  }  // getFirstSuccessfulPatternDescriptor()

  /**
   * @return next successful MintPatternDescriptor of this query or null 
   * if the Array PatternDescriptors is empty, subsequent calls traverse 
   * through all successful patterns
   */
  
  public MintPatternDescriptor getNextSuccessfulPatternDescriptor() {
  
    if (PatternDescriptors == null) 
      return null;
    if (PatternDescriptorsIterator == null) 
      return getFirstSuccessfulPatternDescriptor();

    if ( PatternDescriptorsIterator.hasNext() ) 
      return (MintPatternDescriptor)PatternDescriptorsIterator.next();     
    else
      return null;
    
  }  // getNextSuccessfulPatternDescriptor()
  
  /**
   * @param pIndex 0 <= pIndex < this.countSuccessfulPatternDescriptors()
   * @return MintPatternDescriptor pIndex of this query or null 
   * if the Array PatternDescriptors is empty or pIndex is out of valid range
   */
  
  public MintPatternDescriptor getSuccessfulPatternDescriptor(int pIndex) {
  
    if ( (pIndex >= 0) && 
      ( pIndex <= this.countSuccessfulPatternDescriptors() ) )
      return (MintPatternDescriptor)PatternDescriptors.elementAt(pIndex);
    else
      return null;  
    
  }  // getSuccessfulPatternDescriptor()
  
  /**
   * @return the number of successful MintPatternDescriptors contained in Array
   * PatternDescriptors 
   */
  
  public int countSuccessfulPatternDescriptors() {
  
    if (PatternDescriptors != null)
      return PatternDescriptors.size();
    else
      return 0;
      
  }  // countSuccessfulPatternDescriptors()
  
  /**
   * @return the number of rejected MintPatterns contained in Trie
   * RejectedPatterns
   */
  
  public int countRejectedPatterns() {
  
    if (RejectedPatterns != null)
      return RejectedPatterns.size();
    else
      return 0;
      
  }  // countSuccessfulPatterns()

  /**
   * @return the number of successful SubMintPatterns contained in Trie
   * SuccessfulSubPatterns
   */
  
  public int countSuccessfulSubPatterns() {
  
    if (SuccessfulSubPatterns != null)
      return SuccessfulSubPatterns.size();
    else
      return 0;
      
  }  // countSuccessfulSubPatterns()
  
    
  /**
   * @param pPattern pattern (Trail) describing the pattern of the 
   * rejected MintPatternDescriptor to return
   * @return successful MintPatternDescriptor exactly containing pPattern or
   * null if no successful MintPatternDescriptor matches pPattern
   */
  
  // ########## comments will follow ##########  
  
  public MintPatternDescriptor rejectedPatternsGet(Trail pPattern) {
  
    Object oObject = RejectedPatterns.get( pPattern.getStringArray() );
    if (oObject != null)
      return (MintPatternDescriptor)oObject;
    else
      return null;
  
  }  // RejectedPatternsGet()  
  
  /**
   * @param pPattern pattern (Trail) describing the pattern of the 
   * rejected pMintPatternDescriptor to add to the Trie
   * @param pPatternDescriptor MintPatternDescriptor to add
   */
  
  public void rejectedPatternsAdd(Trail pPattern, 
    MintPatternDescriptor pPatternDescriptor) {
  
    Object oObject = RejectedPatterns.put( pPattern.getStringArray(), 
      pPatternDescriptor );
  
  }  // rejectedPatternsAdd()  
  
  /**
   * dumps the Trie RejectedPatterns into the given PrintStream
   * @param pPrintStream PrintStream to dump
   */
  
  public void rejectedPatternsPrint(PrintStream pPrintStream) {
  
    pPrintStream.println("Rejected Patterns:"); 
    RejectedPatterns.print(pPrintStream);
  
  }  // rejectedPatternsPrint()    
  
  /**
   * @param pPattern pattern (Trail) describing the pattern of the 
   * successful MintPatternDescriptor to return
   * @return successful MintPatternDescriptor exactly containing pPattern or

   * null if no successful MintPatternDescriptor matches pPattern
   */
  
  public MintPatternDescriptor successfulPatternsGet(Trail pPattern) {
  
    Object oObject = SuccessfulPatterns.get( pPattern.getStringArray() );
    if (oObject != null)
      return (MintPatternDescriptor)oObject;
    else
      return null;
  
  }  // successfulPatternsGet()  
  
  /**
   * @param pPattern pattern (Trail) describing the pattern of the 
   * successful pMintPatternDescriptor to add to the Trie
   * @param pPatternDescriptor MintPatternDescriptor to add
   */
  
  public void successfulPatternsAdd(Trail pPattern,
    MintPatternDescriptor pPatternDescriptor, MiningBase pMiningBase) {

    // added by kwinkler, 08/2000
    PatternOutput = new MintPatternOutput( this, MintPatternOutput.UNKNOWN, 
      MintPatternOutput.COMPLETE_PATTERN, pPatternDescriptor, pMiningBase);
    pPatternDescriptor.setPatternOutput(PatternOutput);
  
    Object oObject = SuccessfulPatterns.put( pPattern.getStringArray(), 
      pPatternDescriptor );
  
  }  // successfulPatternsAdd()  
  
  /**
   * dumps the Trie SuccessfulPatterns into the given PrintStream
   * @param pPrintStream PrintStream to dump
   */
  
  public void successfulPatternsPrint(PrintStream pPrintStream) {
  
    pPrintStream.println("Successful Patterns:"); 
    SuccessfulPatterns.print(pPrintStream);
  
  }  // successfulPatternsPrint()    
  
  /**
   * @return Enumeration of all contained successful MintPatterndescriptors
   */
  
  public Enumeration successfulPatternsEnumerate() {
  
    return SuccessfulPatterns.elements();
  
  }  // successfulPatternsEnumerate()    
  
  /**
   * @param pPattern pattern (Trail) describing the pattern of the 
   * successful Sub-MintPatternDescriptor to return, it doesn't contain
   * all variable of the corresponding MintQuery
   * @return successful Sub-MintPatternDescriptor exactly containing pPattern 
   * or null if no successful Sub-MintPatternDescriptor matches pPattern
   */
  
  public MintPatternDescriptor successfulSubPatternsGet(Trail pPattern) {
  
    Object oObject = SuccessfulSubPatterns.get( pPattern.getStringArray() );
    if (oObject != null)
      return (MintPatternDescriptor)oObject;
    else
      return null;
  
  }  // successfulSubPatternsGet()  
  
  /**
   * @param pPattern pattern (Trail) describing the pattern of the 
   * successful Sub-pMintPatternDescriptor to add to the Trie, it doesn't 
   * contain all variable of the corresponding MintQuery
   * @param pPatternDescriptor Sub-MintPatternDescriptor to add
   */
  
  public void successfulSubPatternsAdd(Trail pPattern,
    MintPatternDescriptor pPatternDescriptor, MiningBase pMiningBase) {

    // added by kwinkler, 08/2000
    PatternOutput = new MintPatternOutput(this, MintPatternOutput.UNKNOWN,
      MintPatternOutput.PARTIAL_PATTERN, pPatternDescriptor, pMiningBase);  
    pPatternDescriptor.setPatternOutput(PatternOutput);
   
    Object oObject = SuccessfulSubPatterns.put( pPattern.getStringArray(), 
      pPatternDescriptor );
  
  }  // successfulSubPatternsAdd()  
  
  /**
   * dumps the Trie SuccessfulSubPatterns into the given PrintStream
   * @param pPrintStream PrintStream to dump
   */
  
  public void successfulSubPatternsPrint(PrintStream pPrintStream) {
  
    pPrintStream.println("SuccessfulSub Patterns:"); 
    SuccessfulSubPatterns.print(pPrintStream);
  
  }  // successfulSubPatternsPrint()    
  
  /**
   * @return Enumeration of all contained successful Sub-MintPatterndescriptors
   */
  
  public Enumeration successfulSubPatternsEnumerate() {
  
    return SuccessfulSubPatterns.elements();
  
  }  // successfulSubPatternsEnumerate()    
    
}  // class MintQuery
