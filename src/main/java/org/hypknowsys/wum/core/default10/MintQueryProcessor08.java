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
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.util.*;

/**
 * The MintQueryProcessor processes MINT queries and generates aggregate trees 
 * and navigation patterns that represent the corresponding results. It 
 * maintains three different traversal environments on the Aggregated Log: The
 * first one is used by the methods generating a PatternDescriptor, the second
 * one is used by the methods statistically evaluating one PatternDescriptor
 * and the third traversal environment is used by the methods that generate
 * AggregateTree / NavigationPattern representing the result for visualization.
 * Each traversal environment can be accessed by setting the (first) index in 
 * all attributes to the corresponding constant this.GENERATOR, this.EVALUATOR
 * or this.OUTPUT). The MintQueryProcessor maintains an instance of MintParser
 * in order to check query strings and receive instances of the class 
 * MintQuery. <p>
 *
 * Modified by kwinkler 2/1999: Feature added, now either the persistent 
 * CurrentMiningBase.AggregatedLog or the transient 
 * CurrentMiningBase.TransientAggregatedLog can be queried, constructor
 * changed, constants PERSISTENT and TRANSIENT added to decide what 
 * Aggregated Log to query, attribute AggregatedLogToQuery added <p>
 *
 * Modified by tveit 5/1999: various changes, 
 * setNextPatternDescriptorOptimizer(), removed by kwinkler 11/2001
 *
 * @see wum.kernel.queries.MintQueryParser
 * @see wum.kernel.queries.MintQueryOptimizer
 * @see wum.objects.MintQuery
 * @version 7.0, 17 Nov 2001
 * @author Karsten Winkler
 */

public class MintQueryProcessor08 { // implements Task, Runnable {

//
//    // ########## attributes ##########
//
//    private Thread TaskThread = null;
//    private WUMserver WumServer = null;
//    private ExecuteMintQueryParameter Parameter = null;
//    private AbstractTaskResult Result = null;
//    private AbstractTaskProgress Progress = null;
//
//    private MintQueryParser Parser = null;
//    private MintQueryOptimizer Optimizer = null;
//
//    private MintQuery Query = null;
//    private int AggregatedLogToQuery = 
//      CreateAggregatedLogParameter.PERSISTENT_AGGREGATED_LOG;
//    private MintPatternDescriptor PDOptimizer = null;
//    private int Count;
//
//    // [GENERATOR, EVALUATOR, OUTPUT]
//    // number of variables contained in query
//    private int[] Variables = null;           
//    // index of current variable in Variables
//    private int[] CurrentVariable = null;     
//
//    // [GENERATOR, EVALUATOR, OUTPUT] [CurrentVariable]
//    private SearchExpression[][] SearchExpressions = null;
//    private Stack[][] TraversalStacks = null;
//    private Stack[][] CurrentChildStacks = null;
//    private Stack[] CurrentPattern = null; 
//
//    private MintPatternDescriptor[] PatternDescriptor = null;
//    private Trail[] Pattern = null;
//    private ObservationNode[] AggTreeNode = null;
//    private Object TmpObject = null;
//
//    // attributes for generating output 
//    private AggregateTree AggTree = null;
//    private NavigationPattern NavPattern = null;
//    private int CurrentLevel = 0;
//    private int PreviousLevel = 0;
//    private int MinimumLevel = 0;
//    private int[] FoundVariableAtLevel = null;  
//    private ListIterator StackIterator = null;
//    private String CurrentVariableName = null;
//
//    private TextFile PatternFile = null;
//    private TextFile ResultFile = null;
//    private TimeLogger TaskTimeLogger = null;
//
//    private final static int GENERATOR = 0;
//    private final static int EVALUATOR = 1;  
//    private final static int OUTPUT = 2;  
//
//    // ########## constructors ##########
//
//    /**
//     * constructs an MintQueryProcessor and associates the given Dialog with it,
//     * @param pParentDialog Dialog of MintAdHocProcessor_GUI
//     * pAggregatedLogToQuery dinstinguishes between PERSISTENT and TRANSIENT
//     * Aggregated Log to be queried
//     */
//
//    public MintQueryProcessor(WUMserver pWumServer) {
//
//      WumServer = pWumServer;
//      Query = null;
//
//      // construct 3 traversal environments: generating, evaluating and 
//      // outputting patterns
//      Variables = new int[3];
//      CurrentVariable = new int[3];
//      SearchExpressions = new SearchExpression[3][];
//      TraversalStacks = new Stack[3][];
//      CurrentChildStacks = new Stack[3][];    
//      CurrentPattern = new Stack[3];
//
//      PatternDescriptor = new MintPatternDescriptor[3];
//      Pattern = new Trail[3];
//      AggTreeNode = new ObservationNode[3];
//
//    } 
//
//    /**
//     * starts the execution of the MINT-query represented by the given string
//     * @param pQueryString string containing the MINT-query 
//     * "select t from node ..."
//     */
//
//      public void start(TaskParameter pParameter) {
//
//      Parameter = (ExecuteMintQueryParameter)pParameter;
//
//      if ( (Parameter.getAggregatedLogID() == 
//        CreateAggregatedLogParameter.PERSISTENT_AGGREGATED_LOG) 
//        || (Parameter.getAggregatedLogID() == 
//        CreateAggregatedLogParameter.TRANSIENT_AGGREGATED_LOG) )
//        AggregatedLogToQuery = Parameter.getAggregatedLogID();
//      else
//        AggregatedLogToQuery = 
//          CreateAggregatedLogParameter.PERSISTENT_AGGREGATED_LOG;
//
//      WumServer.setTaskStatus(Task.TASK_RUNNING);
//      Result = new AbstractTaskResult(TaskResult.NO_RESULT, "");
//      WumServer.setTaskResult(Result);
//
//      TaskThread = null;
//      TaskThread = new Thread(this);
//      TaskThread.start();    
//
//    }  // start()
//
//    /**
//     * stops the thread executing the MINT-query
//     */
//
//    public void cancel() {
//
//      if (TaskTimeLogger != null) TaskTimeLogger.close();
//      if ( (TaskThread != null) && TaskThread.isAlive() ) TaskThread.stop();
//      TaskThread = null;
//
//    }  // cancelExecuteMintQuery()  
//
//    /**
//     * code of the thread executing the MINT-query, works with corresponding GUI
//     * MUST CONTAIN THE SAME CODE AS executeMinteQuery()
//     */
//
//    public void run() {
//
//      if (SessionMiningBase != null) 
//        try {
//          SessionMiningBase.join();    
//        }
//        catch (ObjectStoreException e) {}   
//
//      WumServer.setTaskStatus(Task.TASK_ACCEPTED);
//      Progress = new AbstractTaskProgress(0, "");
//      WumServer.setTaskProgress(Progress);
//      Result = new AbstractTaskResult(TaskResult.NO_RESULT, "");
//      WumServer.setTaskResult(Result);
//
//  /*  
//      PatternFile = new TextFile( 
//        new File(oWUM.MiningBase.getMiningBaseDirectory() + "_Patterns.txt") );
//      PatternFile.open();
//      PatternFile.setFirstLine( Query.getQueryString() );
//      ResultFile = new TextFile( new File(
//        oWUM.MiningBase.getMiningBaseDirectory() + "_Results.txt") );
//      ResultFile.open();
//      ResultFile.setFirstLine( Query.getQueryString() );
//  */           
//
//      // first parse MINT-query: syntax check and creation of an instance 
//      // of MintQuery
//      Parser = new MintQueryParser();
//      try {
//        Query = Parser.parseQueryString( Parameter.getQueryString() );
//      }
//      catch (MintSyntaxErrorException e) {
//        // syntax error occured: marks Query as 'not valid'
//        Query = new MintQuery();
//        Query.setResultString( e.getMessage() );
//        Query.setIsNotValid();
//      }
//
//      if ( Query.isValid() ) {
//
//        // get sorted sequence of access values from created heap by tveit
//
//        Optimizer = new MintQueryOptimizer(WumServer);
//        Query = Optimizer.optimizeQuery(Query);
//
//        // kwinkler: optimizer removed but code below could be recyled in 
//        // new and properly working optimizer to be implemented
//
//  //       // execution of Optimizer-Algorithm
//  //       if (Optimizer.canExecuteOptimizer()) { 
//
//  //         WumServer.appendTaskProgressNote(
//  //           "\n\nUsing optimized algorithm and logging in file " +
//  //           CurrentMiningBase.getMiningBaseDirectory() + WUM_MINING_LOG_FILE 
//  //           + "... \n");
//  //         TaskTimeLogger = new TimeLogger( new File( 
//  //           CurrentMiningBase.getMiningBaseDirectory() + WUM_MINING_LOG_FILE ), 
//  //           "Test of WUM: A Web Utilization Miner\n" +
//  //           "Optimizer Algorithm used.\n" );     
//  //         WumServer.appendTaskProgressNote( TaskTimeLogger.startString( 
//  //           Query.getQueryString() ) + "\n\n" );
//  //         WumServer.setTaskResult(Result);
//
//  // 	Count=0;
//  // 	this.setFirstPatternDescriptor();
//  // 	while ( Query.getPatternDescriptor() != null ) {  
//  // 	  this.evaluateCurrentPatternDescriptor(true);  // GUI exists
//  // 	  this.setNextPatternDescriptorOptimizer(Count);        
//  // 	  Count++;
//  // 	}
//
//  //       WumServer.appendTaskProgressNote( "\n" + TaskTimeLogger.stopString("") + 
//  //         "\n" ); 
//  //       WumServer.setTaskResult(Result);
//  //       Query.setExecutionTime( TaskTimeLogger.getPreviousStopTime() );      
//  //       TaskTimeLogger.close();
//  //       this.updateMintAdHocProcessor(MintQuery.FINAL_RESULT);
//  //       // System.out.println("\nSize of Trie RejectedPatterns = " + 
//  //       //   Query.countRejectedPatterns());      
//  //       // System.out.println("\nSize of Trie SuccessfulSubPatterns = " + 
//  //       //   Query.countSuccessfulSubPatterns());      
//  //       // System.out.flush();      
//
//
//  //       }  // execution of Optimizer-Algorithm
//
//  //       else {  // execution without Optimizer-Algorithm
//
//        WumServer.appendTaskProgressNote(
//          "\n\nUsing standard algorithm and logging in file " +
//          WumServer.getMiningBaseDirectory() + WUM_MINING_LOG_FILE + 
//          "... \n");
//        TaskTimeLogger = new TimeLogger( new File( 
//          WumServer.getMiningBaseDirectory() + WUM_MINING_LOG_FILE ), 
//          "Test of WUM: A Web Utilization Miner\n" );     
//        WumServer.appendTaskProgressNote( TaskTimeLogger.startString( 
//          Query.getQueryString() ) + "\n\n" );
//        WumServer.setTaskResult(Result);      
//
//        this.setFirstPatternDescriptor();
//        while ( Query.getPatternDescriptor() != null ) {  
//          this.evaluateCurrentPatternDescriptor(true);  // GUI exists
//          this.setNextPatternDescriptor();        
//          // System.out.print(".");
//        }
//
//        WumServer.appendTaskProgressNote( "\n" + 
//          TaskTimeLogger.stopString( "" ) + "\n" );
//        WumServer.setTaskResult(Result);
//        Query.setExecutionTime( TaskTimeLogger.getPreviousStopTime() );        
//        TaskTimeLogger.close();
//        this.updateMintAdHocProcessor(MintQuery.FINAL_RESULT);
//        // System.out.println("\nSize of Trie RejectedPatterns = " + 
//        //   Query.countRejectedPatterns());      
//        // System.out.println("\nSize of Trie SuccessfulSubPatterns = " + 
//        //   Query.countSuccessfulSubPatterns());      
//        // System.out.flush();      
//
//
//  //       }  // execution without Optimizer-Algorithm
//
//      }   
//
//      else {
//        // no execution of invalid MINT-query
//        // debugging: oParentDialog.setResults(Query, MintQuery.SYNTAX_ERROR);
//        Query.setResultType(MintQuery.SYNTAX_ERROR);
//        Result.update(TaskResult.FINAL_RESULT, "MintQuery", Query);
//        WumServer.setTaskResult(Result);
//        WumServer.setTaskStatus(Task.TASK_FINISHED);
//      }
//
//  /* debugging:
//      try {
//        FileOutputStream oTriesFile = new FileOutputStream( new File(
//          oWUM.MiningBase.getMiningBaseDirectory() + "_Tries.txt") );
//        PrintStream oTriesPrint = new PrintStream(oTriesFile);
//        Query.successfulPatternsPrint(oTriesPrint);
//        Query.successfulSubPatternsPrint(oTriesPrint);
//        Query.rejectedPatternsPrint(oTriesPrint);
//        oTriesFile.close();MintQueryProcessor:
//      }
//      catch (IOException e) {}    
//      PatternFile.close();
//      ResultFile.close();
//  */    
//
//    }  // run()
//
//    /**
//     * updates the MintAdHocProcessor with all currently found results
//     * @param pResultType type of given result: this.SYNTAX_ERROR or
//     * this.INTERMEDIATE_RESULT or this.FINAL_RESULT
//     */
//
//    private void updateMintAdHocProcessor(int pResultType) {
//
//      Query.generateSuccessfulPatternDescriptors(); String vResultString = "";
//
//      for (int i = 0; i < Query.countSuccessfulPatternDescriptors(); i++)
//        vResultString += "\nPatternID=" + (i+1) + ": " + 
//          Query.getSuccessfulPatternDescriptor(i).toStringUrl(WumServer);
//
//      // vResultString += "\n";
//      // for (int i = 0; i < Query.countSuccessfulPatternDescriptors(); i++)
//      //   vResultString += "\nPatternID=" + (i+1) + ": " +
//      //     Query.getSuccessfulPatternDescriptor(i).toString();
//
//      vResultString += "\n\n" + Query.countSuccessfulSubPatterns() +
//        " Successful Sub-Patterns:\n";
//      Enumeration oSuccessfulSubPatterns = 
//        Query.successfulSubPatternsEnumerate();
//      while ( oSuccessfulSubPatterns.hasMoreElements() )
//        vResultString += "\n" + ( (MintPatternDescriptor)
//        oSuccessfulSubPatterns.nextElement() ).toStringUrl(WumServer);
//
//      vResultString += "\n\nNotation:\n[variable.id;variable.url;" + 
//        "variable.occurrence;variable.support]";
//
//      Query.setResultType(pResultType);
//      if (pResultType == MintQuery.FINAL_RESULT) {
//        Query.setResultString(Query.countSuccessfulPatternDescriptors() +
//          " Successful Patterns (Final Result):\n" + vResultString);
//        Result.update(TaskResult.FINAL_RESULT, "MINT query", Query);
//        WumServer.setTaskResult(Result);
//        WumServer.setTaskStatus(Task.TASK_FINISHED);
//      }
//      else {
//        Query.setResultString(Query.countSuccessfulPatternDescriptors() +
//          " Successful Patterns (Intermediate Result):\n" + vResultString);
//        Result.update(TaskResult.INTERMEDIATE_RESULT, "MINT query", Query);
//        WumServer.setTaskResult(Result);
//      }
//
//    }  // updateMintAdHocProcessor()
//
//    /**
//     * starts the process of generating all possible pattern descriptors by 
//     * preparing the corresponding traversal environment and finally calling 
//     * setNextPatternDescriptor(); idea of algorithm: the aggregated tree is 
//     * independently traversed from the evaluating  procedure that tests only 
//     * one special instance of MintPatternDescriptor given by the
//     * generating procedure; uses token-based traversal algorithm: each variable
//     * is represented by one token that traverses the subtree of the 
//     * AggregatedLog below its predecessor variable; possible patterns are 
//     * contained in CurrentPattern; CurrentVariable can be seen
//     * as a token's id 
//     */
//
//    private void setFirstPatternDescriptor() {
//
//      Pattern[GENERATOR] = new Trail();
//      AggTreeNode[GENERATOR] = new ObservationNode();
//      CurrentPattern[GENERATOR] = new Stack();  
//
//      Variables[GENERATOR] = Query.getTemplate().countVariableNames();
//      CurrentVariable[GENERATOR] = 0;
//
//      // cloning must be performed to create completely new instances 
//      // of SearchExpressions
//      // that can be manipulated independently of the original SearchExpressions
//      SearchExpressions[GENERATOR] = 
//        new SearchExpression[ Variables[GENERATOR] ];
//
//      for (int i = 0; i < SearchExpressions[GENERATOR].length; i++)
//        SearchExpressions[GENERATOR][i] = (SearchExpression)
//          ( ( Query.getTemplate().getSearchExpressions() )[i] ).clone();  
//      TraversalStacks[GENERATOR] = new Stack[ Variables[GENERATOR] ];
//      for (int i = 0; i < TraversalStacks[GENERATOR].length; i++)
//        TraversalStacks[GENERATOR][i] = new Stack();
//      CurrentChildStacks[GENERATOR] = new Stack[ Variables[GENERATOR] ];
//      for (int i = 0; i < CurrentChildStacks[GENERATOR].length; i++)
//        CurrentChildStacks[GENERATOR][i] = new Stack();
//
//      SearchExpressions[GENERATOR][ CurrentVariable[GENERATOR] ].resetCounter();
//      TraversalStacks[GENERATOR][ CurrentVariable[GENERATOR] ] = new Stack();
//
//      // root depends on whether persistent or transient aggregated Log 
//      // is to be queried
//      if (AggregatedLogToQuery == 
//        CreateAggregatedLogParameter.TRANSIENT_AGGREGATED_LOG) {
//        pushObservationNode( GENERATOR, CurrentVariable[GENERATOR], 
//          CurrentMiningBase.getTransientAggregatedLog().getRoot() );
//      }
//      else {
//        pushObservationNode( GENERATOR, CurrentVariable[GENERATOR], 
//          super.CurrentMiningBase.getAggregatedLog().getRoot() );
//      }    
//      CurrentChildStacks[GENERATOR][ CurrentVariable[GENERATOR] ] = new Stack();
//      pushCurrentChild(GENERATOR, CurrentVariable[GENERATOR], 0);
//
//      this.setNextPatternDescriptor();
//
//    }  // setFirstPatternDescriptor()
//
//    // ########################## tveit #############################
//    // kwinkler: optimizer removed but code might be recycled
//
//  //   private void setNextPatternDescriptorOptimizer(int pIndx) {
//  //     int i =  pIndx;
//  //     if ( i < Optimizer.getLengthOfTrail()) {
//  //       PDOptimizer = new MintPatternDescriptor(Query.countVariables());
//  //       PDOptimizer.setPattern(Optimizer.getTrail(i));
//  //       Query.setPatternDescriptor(PDOptimizer);
//  //     }
//  //     else Query.setPatternDescriptor(null);
//  //   }
//
//
//    /**
//     * continues the process of generating all possible pattern descriptors by
//     * finding the next possible pattern descriptor and storinf it in 
//     * Query.PatternDescriptor, Query.PatternDescriptor = null if there is no 
//     * possible pattern descriptor left, resursive method
//     */
//
//    private void setNextPatternDescriptor() {
//
//      AggTreeNode[GENERATOR] = this.getNextValidObservationNode( 
//        CurrentVariable[GENERATOR] );
//
//      if (AggTreeNode[GENERATOR] != null) {
//
//        if ( CurrentPattern[GENERATOR].size() == 
//          (CurrentVariable[GENERATOR] + 1) )
//
//          if ( ! CurrentPattern[GENERATOR].isEmpty() ) 
//            TmpObject = CurrentPattern[GENERATOR].pop();  
//
//        CurrentPattern[GENERATOR].push( 
//          AggTreeNode[GENERATOR].getPageOccurrence() );      
//
//        Pattern[GENERATOR] = this.currentPatternToTrail(GENERATOR);
//        PatternDescriptor[GENERATOR] = 
//          new MintPatternDescriptor( Pattern[GENERATOR].getSize() ); 
//        PatternDescriptor[GENERATOR].setPattern( Pattern[GENERATOR] );
//        Query.setPatternDescriptor(PatternDescriptor[GENERATOR]);      
//
//        if ( CurrentVariable[GENERATOR] < (Variables[GENERATOR] - 1) ) {
//          CurrentVariable[GENERATOR]++;
//          SearchExpressions[GENERATOR][ 
//            CurrentVariable[GENERATOR] ].resetCounter();
//          TraversalStacks[GENERATOR][ CurrentVariable[GENERATOR] ] = 
//            new Stack();
//          pushObservationNode(GENERATOR, CurrentVariable[GENERATOR], 
//            AggTreeNode[GENERATOR]);
//          CurrentChildStacks[GENERATOR][ CurrentVariable[GENERATOR] ] = 
//            new Stack();
//          pushCurrentChild(GENERATOR, CurrentVariable[GENERATOR], 0);
//        }
//
//      }    
//      else {
//
//        if (CurrentVariable[GENERATOR] > 0) {
//          CurrentVariable[GENERATOR]--;
//          if ( ! CurrentPattern[GENERATOR].isEmpty() ) 
//            TmpObject = CurrentPattern[GENERATOR].pop();        
//          setNextPatternDescriptor();
//        }
//        else    
//          Query.setPatternDescriptor(null);
//
//      }    
//
//    }  // setNextPatternDescriptor()
//
//    /**
//      * modified by kwinkler 11/99 to include predicates on wildcards
//      * returns the next valid (according to SearchExpressions) ObservationNode
//      * in the given traversal environment; only for [GENERATOR]; recursive 
//      * method; stops if TraversalStack.empty()
//      * @param pCurrentVariable specifying the traversal environment
//      */
//
//    private ObservationNode getNextValidObservationNode(int pCurrentVariable) {
//
//      int currentChild = getCurrentChild(GENERATOR, pCurrentVariable);
//      if (currentChild == -1) return null;
//      ObservationNode currentNode = getObservationNode(GENERATOR, 
//        pCurrentVariable);
//      if (currentNode == null) return null;
//
//      ObservationNode resultNode = null;
//      if ( currentChild < currentNode.countChildren() ) 
//        resultNode = currentNode.getChild(currentChild);
//
//      if (resultNode != null) {
//
//        // modified by kwinkler 11/99
//        int validSearchExpression = SearchExpressions[GENERATOR]
//          [ CurrentVariable[GENERATOR] ].incrementCounter(WumServer, 
//            resultNode.getPageOccurrence() );
//
//        if (validSearchExpression == SearchExpression.VALID) {    
//          incrementCurrentChild(GENERATOR, pCurrentVariable);
//          pushObservationNode(GENERATOR, pCurrentVariable, resultNode);
//          pushCurrentChild(GENERATOR, pCurrentVariable, 0);
//          return resultNode;
//        } else
//        if (validSearchExpression == SearchExpression.SMALLER_MIN) {  
//          incrementCurrentChild(GENERATOR, pCurrentVariable);
//          pushObservationNode(GENERATOR, pCurrentVariable, resultNode);
//          pushCurrentChild(GENERATOR, pCurrentVariable, 0);
//          return getNextValidObservationNode(pCurrentVariable);
//        } else
//        if (validSearchExpression == SearchExpression.GREATER_MAX) {       
//          SearchExpressions[GENERATOR][ 
//            CurrentVariable[GENERATOR] ].decreaseCounter(2);
//          popObservationNode(GENERATOR, pCurrentVariable);
//          popCurrentChild(GENERATOR, pCurrentVariable);
//          return getNextValidObservationNode(pCurrentVariable);
//        } else
//        // added by kwinkler 11/99
//        if (validSearchExpression == SearchExpression.LAST_VALID) {      
//          incrementCurrentChild(GENERATOR, pCurrentVariable);
//          SearchExpressions[GENERATOR][ 
//            CurrentVariable[GENERATOR] ].decreaseCounter(1);
//          return resultNode;
//        }
//
//      }
//      else {
//        SearchExpressions[GENERATOR][ 
//          CurrentVariable[GENERATOR] ].decrementCounter();
//        popObservationNode(GENERATOR, pCurrentVariable);
//        popCurrentChild(GENERATOR, pCurrentVariable);
//        return getNextValidObservationNode(pCurrentVariable);
//      }
//
//      return null;
//
//    }  // getNextValidObservationNode()
//
//    /**
//     * starts the process of evaluating the pattern descriptor 
//     * Query.getPatternDescriptor() in the Aggregated Log; uses corresponding 
//     * traversal environment and finally calls setNextPatternDescriptor();
//     * idea of algorithm: the current pattern descriptor will only be evaluated
//     * if this didn't happen before; Query therefore contains 3 different tries
//     * of tested patterns; if the pattern descriptor doesn't match all content 
//     * predicates no traversal on the AggregatedLog is started
//     */  
//    private void evaluateCurrentPatternDescriptor(boolean pIsGUI) {
//
//      if ( Query.getPatternDescriptor() == null) return;
//
//      Trail oPattern = Query.getPatternDescriptor().getPattern();
//      if (oPattern == null) return;
//
//      // check whether this pattern descriptor was tested before
//      if ( Query.successfulPatternsGet(oPattern) != null ) {
//        // PatternFile.setNextLine( "(SuccCon) " + oPattern.toString() );     
//        // System.out.print(";");      
//        WumServer.appendTaskProgressNote(";");
//        return;
//      } else
//      if ( Query.successfulSubPatternsGet(oPattern) != null ) {
//        // PatternFile.setNextLine( "(PendCon) " + oPattern.toString() );     
//        // System.out.print(":");      
//        WumServer.appendTaskProgressNote(":");
//        return;
//      } else
//      if ( Query.rejectedPatternsGet(oPattern) != null ) {
//        // PatternFile.setNextLine( "(RejeCon) " + oPattern.toString() );     
//        // System.out.print(".");      
//        WumServer.appendTaskProgressNote(".");
//        return;
//      }    
//
//      boolean match = false;
//      match = this.matchesContentPredicates();
//
//      if (match) {
//
//        // all content predicates match, check now for statistics predicates
//        match = this.matchesStatisticsPredicates();
//
//        // add current pattern descriptor to the appropriate list in its query
//        if (match) {      
//          if (oPattern.getSize() == Variables[GENERATOR]) {
//            Query.successfulPatternsAdd( oPattern, 
//              Query.getPatternDescriptor(), WumServer );
//            // PatternFile.setNextLine( "(SuccAdd) " + oPattern.toString() );
//            // ResultFile.setNextLine( "(SuccPat) " + 
//            //    Query.getPatternDescriptor() ); 
//            // System.out.print("#"); 
//            WumServer.appendTaskProgressNote("#", TaskThread);
//            // System.out.print("(" + oWUM.getFreeMemory() + ")"); 
//            this.updateMintAdHocProcessor(MintQuery.INTERMEDIATE_RESULT);
//          }
//          else {
//            Query.successfulSubPatternsAdd( oPattern, 
//              Query.getPatternDescriptor(), WumServer );
//            // changed by kwinkler 9/99
//            // if ( Query.countRejectedPatterns() > 30000 ) {  
//            // do not use Query.clearRejectedPatterns(); to be speedy ...
//            // oWUM.startGarbageCollection();
//            // 
//            // kwinkler 10/99:  }
//            // System.out.print("+");           
//            WumServer.appendTaskProgressNote("+", TaskThread);
//            // System.out.print("(" + oWUM.getFreeMemory() + ")");               
//            // PatternFile.setNextLine( "(PendAdd) " + oPattern.toString() );
//          }
//        }
//        else {
//          //old Query.rejectedPatternsAdd( oPattern, 
//          //  Query.getPatternDescriptor() );
//          //replacement aimed at reducing memory in use, is only tested != null
//          Query.rejectedPatternsAdd( oPattern, new MintPatternDescriptor(1) );
//          // PatternFile.setNextLine( "(RejeAdd) " + oPattern.toString() );
//          // System.out.print("="); // statistics predicates failed       
//          WumServer.appendTaskProgressNote("=", TaskThread);
//        }       
//
//      }
//      else {
//        //old Query.rejectedPatternsAdd( oPattern, 
//        //  Query.getPatternDescriptor() );
//        //replacement aimed at reducing the memory in use, is only tested != null
//        Query.rejectedPatternsAdd( oPattern, new MintPatternDescriptor(1) );
//        // System.out.print("-"); // content predicates failed
//        WumServer.appendTaskProgressNote("-", TaskThread);
//        // PatternFile.setNextLine( "(RejeAdd) " + oPattern.toString() );
//      }    
//
//    }  // evaluateCurrentPatternDescriptor()
//
//    /**
//     * tests whether the current Query.PatternDescriptor matches all content 
//     * predicates; doesn't change Query.PatternDescriptor
//     * @return true if it does, otherwise not
//     */
//
//    private boolean matchesContentPredicates() {
//
//      if ( Query.getPatternDescriptor() == null) return false;
//      boolean match = false;
//
//      String[] aVariableNames = Query.getTemplate().getVariableNames();
//      Trail oPattern = Query.getPatternDescriptor().getPattern();
//      PageOccurrence oPageOccurrence = null;
//
//      for (int i = 0; i < oPattern.getSize(); i++) {
//        if (i == 0)
//          oPageOccurrence = oPattern.getFirstPageOccurrence();
//        else
//          oPageOccurrence = oPattern.getNextPageOccurrence();
//        match = Query.getVariable( aVariableNames[i] ).
//          isMatchedByPageOccurrence(WumServer, oPageOccurrence );
//        if ( ! match) return false;    
//      }    
//
//      return match;
//
//    }  // matchesContentPredicates()
//
//    /**
//     * tests whether the current Query.PatternDescriptor matches all statistics 
//     * predicates; changes Query.PatternDescriptor by calculating support and 
//     * setting references; starts a traversal on Aggregated Log to determine 
//     * support of current pattern descriptor; if current pattern descriptor 
//     * starts with a successfully tested subpattern, only the
//     * rest will be evaluated by using the the references in the last 
//     * MintPatternLayer; 
//     * if current pattern descriptor starts with a rejected subpattern
//     * not traversal will be performed and false will be returned
//     * @return true if it does, otherwise not
//     */
//
//    private boolean matchesStatisticsPredicates() {
//
//      if ( Query.getPatternDescriptor() == null) return false;
//      boolean match = false;
//
//      int startLevel= this.lookForContainedSuccessfulSubPatterns();
//
//      if (startLevel >= 0) {  
//
//        if (startLevel == 0) {  // new pattern, start with Root, level == 0
//          // root depends on whether persistent or transient aggregated 
//          // Log is to be queried
//          if (AggregatedLogToQuery == 
//            CreateAggregatedLogParameter.TRANSIENT_AGGREGATED_LOG) {
//            this.startLookingForPattern(startLevel, 
//              CurrentMiningBase.getTransientAggregatedLog().getRoot() );
//          }
//          else {
//            this.startLookingForPattern(startLevel, 
//              CurrentMiningBase.getAggregatedLog().getRoot() );        
//          }
//          while ( AggTreeNode[EVALUATOR] != null)
//            this.continueLookingForPattern(startLevel);  
//        }
//        else {  
//          // pattern starts with successfull subpattern, 
//          //start with references, level > 0, 
//          for (int j = 0; j < PatternDescriptor[EVALUATOR].
//            getLayer(startLevel-1).getSize(); j++) {
//            this.startLookingForPattern(startLevel, 
//              PatternDescriptor[EVALUATOR].getLayer(startLevel-1).
//                getObservationNode(j) );
//            while ( AggTreeNode[EVALUATOR] != null)
//              this.continueLookingForPattern(startLevel);  
//          }        
//        }
//
//        // tests whether the result pattern descriptor matches statistics 
//        // predicates
//        match = Query.getTemplate().isMatchedByPatternDescriptor( 
//          WumServer, PatternDescriptor[EVALUATOR] ); 
//        Query.setPatternDescriptor( PatternDescriptor[EVALUATOR] );   
//
//      }
//      else  // pattern starts with rejected subpattern
//        match = false;
//
//      return match;          
//
//    }  // matchesStatisticsPredicates()  
//
//    /**
//     * tests whether the current Query.PatternDescriptor contains any already 
//     * tested subpatterns 
//     * @return -1 if it contains a rejected subpattern; 0 if it doesn't 
//     * contain tested pattern; >= 1 if it contains successfully teste pattern 
//     * (index of layer to continue evaluation)
//     */
//
//    private int lookForContainedSuccessfulSubPatterns() {
//
//      PatternDescriptor[EVALUATOR] = Query.getPatternDescriptor();
//      Pattern[EVALUATOR] = Query.getPatternDescriptor().getPattern();    
//      Trail oNewPattern = null;
//
//      // look for the largest tested subpattern
//      for (int i = Pattern[EVALUATOR].getSize() - 2 ; i >= 0; i--) {
//
//        oNewPattern = PatternDescriptor[EVALUATOR].getSubPattern(0, i);
//
//        if ( Query.rejectedPatternsGet(oNewPattern) != null ) {      
//          Query.rejectedPatternsAdd( Pattern[EVALUATOR], 
//            PatternDescriptor[EVALUATOR] );
//          return -1;  
//          // don't consider this descriptor: subpattern allready rejected
//        } 
//        else      
//          if ( Query.successfulSubPatternsGet(oNewPattern) != null ) {      
//            MintPatternDescriptor oSubPatternDescriptor = 
//              Query.successfulSubPatternsGet(oNewPattern);
//            for (int j = 0; j <= i; j++)        
//              PatternDescriptor[EVALUATOR].setLayer( j, 
//                oSubPatternDescriptor.getLayer(j) );
//            return (i + 1);  
//            // determine new descriptor by copying 
//            // subpattern, level
//          }       
//      }
//
//      return 0;
//
//    }  // lookForContainedSuccessfulSubPatterns()
//
//    /**
//     * starts the process of evaluating the Pattern contained in 
//     * PatternDescriptor[EVALUATOR] and Pattern[EVALUATOR]; uses corresponding 
//     * traversal environment and finally calls setNextPatternDescriptor();
//     * idea of algorithm: evaluates only subtrees below pStartObservationNode 
//     * by looking for valid valid patterns beginning at pStartLevel
//     * @param pStartLevel pStartLevel == 0 == root: look for complete pattern; 
//     * pStartLevel > 0: look for pattern beginning at variable pStartIndex
//     * @param pStartObservationNode ObservationNode to start traversal 
//     */
//
//    private void startLookingForPattern(int pStartLevel, 
//      ObservationNode pStartObservationNode) {
//
//      AggTreeNode[EVALUATOR] = new ObservationNode();
//      CurrentPattern[EVALUATOR] = new Stack();  
//
//      Variables[EVALUATOR] = Pattern[EVALUATOR].getSize() - pStartLevel;
//      CurrentVariable[EVALUATOR] = 0;
//
//      // clone only interesting SearchExpression 
//      SearchExpressions[EVALUATOR] = 
//        new SearchExpression[ Variables[EVALUATOR] ];
//      for (int i = 0; i < SearchExpressions[EVALUATOR].length; i++) {
//        SearchExpressions[EVALUATOR][i] = (SearchExpression)
//          Query.getTemplate().getSearchExpression(i+pStartLevel).clone();        
//        SearchExpressions[EVALUATOR][i].setLookForPageOccurrence(
//          Pattern[EVALUATOR].getPageOccurrence(i+pStartLevel) );
//      }
//      TraversalStacks[EVALUATOR] = new Stack[ Variables[EVALUATOR] ];
//      for (int i = 0; i < TraversalStacks[EVALUATOR].length; i++)
//        TraversalStacks[EVALUATOR][i] = new Stack();
//      CurrentChildStacks[EVALUATOR] = new Stack[ Variables[EVALUATOR] ];
//      for (int i = 0; i < CurrentChildStacks[EVALUATOR].length; i++)
//        CurrentChildStacks[EVALUATOR][i] = new Stack();
//
//      SearchExpressions[EVALUATOR][ CurrentVariable[EVALUATOR] ].resetCounter();
//      TraversalStacks[EVALUATOR][ CurrentVariable[EVALUATOR] ] = new Stack();
//      pushObservationNode( EVALUATOR, CurrentVariable[EVALUATOR], 
//        pStartObservationNode );
//      CurrentChildStacks[EVALUATOR][ CurrentVariable[EVALUATOR] ] = new Stack();
//      pushCurrentChild(EVALUATOR, CurrentVariable[EVALUATOR], 0);
//
//      this.continueLookingForPattern(pStartLevel);
//
//    }  // startLookingForPattern()
//
//    /**
//     * continues the process of evaluating the Pattern contained in 
//     * PatternDescriptor[EVALUATOR] 
//     * and Pattern[EVALUATOR];
//     * resursive method
//     * @param pStartLevel pStartLevel == 0 == root: look for complete pattern; 
//     * pStartLevel > 0: look for pattern beginning at variable pStartIndex
//     */
//
//    private void continueLookingForPattern(int pStartLevel) {
//
//      AggTreeNode[EVALUATOR] = this.getNextLookForObservationNode( 
//        CurrentVariable[EVALUATOR] );
//
//      if (AggTreeNode[EVALUATOR] != null) {
//
//        if ( CurrentPattern[EVALUATOR].size() == 
//          (CurrentVariable[EVALUATOR] + 1) )
//          if ( ! CurrentPattern[EVALUATOR].isEmpty() ) 
//            TmpObject = CurrentPattern[EVALUATOR].pop();  
//
//        CurrentPattern[EVALUATOR].push( 
//          AggTreeNode[EVALUATOR].getPageOccurrence() ); 
//
//        PatternDescriptor[EVALUATOR].appendObservationNode(
//          AggTreeNode[EVALUATOR],
//          CurrentVariable[EVALUATOR] + pStartLevel );
//
//        if ( CurrentVariable[EVALUATOR] < (Variables[EVALUATOR] - 1) ) {
//          CurrentVariable[EVALUATOR]++;
//          SearchExpressions[EVALUATOR][ 
//            CurrentVariable[EVALUATOR] ].resetCounter();
//          TraversalStacks[EVALUATOR][ CurrentVariable[EVALUATOR] ] = 
//            new Stack();
//          pushObservationNode(EVALUATOR, CurrentVariable[EVALUATOR], 
//            AggTreeNode[EVALUATOR]);
//          CurrentChildStacks[EVALUATOR][ CurrentVariable[EVALUATOR] ] = 
//            new Stack();
//          pushCurrentChild(EVALUATOR, CurrentVariable[EVALUATOR], 0);
//        }      
//
//      }    
//      else {
//
//        if (CurrentVariable[EVALUATOR] > 0) {
//          CurrentVariable[EVALUATOR]--;
//          if ( ! CurrentPattern[EVALUATOR].isEmpty() ) 
//            TmpObject = CurrentPattern[EVALUATOR].pop();        
//          this.continueLookingForPattern(pStartLevel);
//        }
//        else
//          AggTreeNode[EVALUATOR] = null;        
//      }     
//
//    }  // continueLookingForPattern()
//
//    /**
//      * modified by kwinkler 11/99 to include predicates on wildcards
//      * returns the next valid (according to SearchExpressions) ObservationNode
//      * in the given traversal environment; only for [EVALUATOR]; recursive 
//      * method; stops if TraversalStack.empty()
//      * @param pCurrentVariable specifying the traversal environment
//      */
//
//    private ObservationNode getNextLookForObservationNode(int pCurrentVariable) {
//
//      int currentChild = getCurrentChild(EVALUATOR, pCurrentVariable);
//      if (currentChild == -1) return null;
//      ObservationNode currentNode = getObservationNode(EVALUATOR, 
//        pCurrentVariable);
//      if (currentNode == null) return null;
//
//      ObservationNode resultNode = null;
//      if ( currentChild < currentNode.countChildren() ) 
//        resultNode = currentNode.getChild(currentChild);
//
//      if (resultNode != null) {
//
//        // modified by kwinkler 11/99    
//        int validSearchExpression = SearchExpressions[EVALUATOR]
//          [ CurrentVariable[EVALUATOR] ].incrementCounter(WumServer, 
//            resultNode.getPageOccurrence() );
//
//        if (validSearchExpression == SearchExpression.VALID) {    
//
//          if ( SearchExpressions[EVALUATOR][ CurrentVariable[EVALUATOR] ].
//            getLookForPageOccurrence().equals( 
//            resultNode.getPageOccurrence() ) ) {
//            SearchExpressions[EVALUATOR][ CurrentVariable[EVALUATOR] ].
//              decrementCounter();
//            incrementCurrentChild(EVALUATOR, pCurrentVariable);
//            return resultNode;
//          }
//          else {
//            incrementCurrentChild(EVALUATOR, pCurrentVariable);
//            pushObservationNode(EVALUATOR, pCurrentVariable, resultNode);
//            pushCurrentChild(EVALUATOR, pCurrentVariable, 0);
//            return getNextLookForObservationNode(pCurrentVariable);
//          }
//
//        } else
//        if (validSearchExpression == SearchExpression.SMALLER_MIN) {  
//          incrementCurrentChild(EVALUATOR, pCurrentVariable);
//          pushObservationNode(EVALUATOR, pCurrentVariable, resultNode);
//          pushCurrentChild(EVALUATOR, pCurrentVariable, 0);
//          return getNextLookForObservationNode(pCurrentVariable);
//        } else
//        if (validSearchExpression == SearchExpression.GREATER_MAX) {
//          SearchExpressions[EVALUATOR][ CurrentVariable[EVALUATOR] ].
//            decreaseCounter(2);
//          popObservationNode(EVALUATOR, pCurrentVariable);
//          popCurrentChild(EVALUATOR, pCurrentVariable);
//          return getNextLookForObservationNode(pCurrentVariable);
//        } else
//        // added by kwinkler 11/99
//        if (validSearchExpression == SearchExpression.LAST_VALID) { 
//
//          if ( SearchExpressions[EVALUATOR][ CurrentVariable[EVALUATOR] ].
//            getLookForPageOccurrence().equals( 
//            resultNode.getPageOccurrence() ) ) {
//            SearchExpressions[EVALUATOR][ CurrentVariable[EVALUATOR] ].
//              decrementCounter();
//            incrementCurrentChild(EVALUATOR, pCurrentVariable);
//            return resultNode;
//          }
//          else {    
//            incrementCurrentChild(EVALUATOR, pCurrentVariable);
//            SearchExpressions[EVALUATOR][ CurrentVariable[EVALUATOR] ].
//              decreaseCounter(1);
//            return getNextLookForObservationNode(pCurrentVariable);
//          }
//
//        }
//      }
//      else {
//        SearchExpressions[EVALUATOR][ CurrentVariable[EVALUATOR] ].
//          decrementCounter();    
//        popObservationNode(EVALUATOR, pCurrentVariable);
//        popCurrentChild(EVALUATOR, pCurrentVariable);
//        return getNextLookForObservationNode(pCurrentVariable);
//      }
//
//      return null;
//
//    }  // getNextLookForObservationNode()  
//
//    /**
//     * @param pGeneratorEvaluator indicator of the traversal environment
//     * @return the trail corresponding to the stack CurrentPattern 
//     */
//
//    private Trail currentPatternToTrail(int pGeneratorEvaluator) {
//
//      Trail vResult = new Trail();    
//      ListIterator oIterator = 
//        CurrentPattern[pGeneratorEvaluator].listIterator();
//
//      if ( oIterator.hasNext() )
//        vResult.setFirstPageOccurrence( (PageOccurrence)oIterator.next() );
//      else
//        return null;
//
//      while ( oIterator.hasNext() )
//        vResult.setNextPageOccurrence( (PageOccurrence)oIterator.next() );
//
//      return vResult;
//
//    }  // currentPatternToTrail()
//
//    /**
//     * creates an AggregateTree that contains only the pattern of the given 
//     * pattern descriptor in pQuery; used to visualize the URL, PageOccurrence, 
//     * support of all variables' bindings
//     * @param pQuery MintQuery containing the pattern descriptors and its context
//     * @param pSuccessfulPatternDescriptorIndex index of the pattern descriptor 
//     * to visualize in pQuery's list of successful pattern descriptors
//     * @return AggregateTree containing one observation that is the sequence of 
//     * ObservationNodes representing the pattern descriptor
//     */
//
//    private AggregateTree generateGSequenceTree(MintQuery pQuery, 
//      int pSuccessfulPatternDescriptorIndex) {
//
//      if (SessionMiningBase != null) 
//        try {
//          SessionMiningBase.join();    
//        }
//        catch (ObjectStoreException e) {}   
//
//      AggTree = new AggregateTree();
//      PatternDescriptor[OUTPUT] = pQuery.getSuccessfulPatternDescriptor(
//        pSuccessfulPatternDescriptorIndex);
//      Pattern[OUTPUT] = PatternDescriptor[OUTPUT].getPattern();
//
//      for (int i = 0; i < PatternDescriptor[OUTPUT].getLength(); i++) {
//        AggTree.pushNewObservationNode(
//          PatternDescriptor[OUTPUT].getPageOccurrence(i),
//          PatternDescriptor[OUTPUT].getSupport(i), 
//          pQuery.getTemplate().getVariableName(i) );   
//      }
//
//      return AggTree;
//
//    }  // generateTemplateTree()  
//
//    /**
//     * creates an AggregateTree that contains all observations matching the 
//     * given pattern descriptor in pQuery; used to visualize the pattern 
//     * descriptor; recomputes support of nodes that are
//     * not bindings of the query's variables
//     * @param pQuery MintQuery containing the pattern descriptors and its context
//     * @param pSuccessfulPatternDescriptorIndex index of the pattern descriptor 
//     * to visualize in pQuery's list of successful pattern descriptors
//     * @return AggregateTree containing all valid observations
//     */
//
//    private AggregateTree generateAggregateTree (MintQuery pQuery, 
//      int pSuccessfulPatternDescriptorIndex) {
//
//      if (SessionMiningBase != null) 
//        try {
//          SessionMiningBase.join();    
//        }
//        catch (ObjectStoreException e) {}   
//
//        // debugging:System.out.println("MintQueryProcessor: " +
//        //   "GenAggTree using " +  pQuery.getSuccessfulPatternDescriptor(
//        // pQuery.getSuccessfulPatternDescriptor(
//        //   pSuccessfulPatternDescriptorIndex));
//
//      AggTree = new AggregateTree();
//
//      // root depends on whether persistent or transient aggregated 
//      // Log is to be queried
//      return this.generateAggregateTree(pQuery, 
//        pQuery.getSuccessfulPatternDescriptor(pSuccessfulPatternDescriptorIndex),
//        ( AggregatedLogToQuery == 
//          CreateAggregatedLogParameter.TRANSIENT_AGGREGATED_LOG ? 
//          CurrentMiningBase.getTransientAggregatedLog().getRoot() : 
//          CurrentMiningBase.getAggregatedLog().getRoot() ), null );
//
//    }  // generateAggregateTree()  
//
//    /**
//     * creates an NavigationPattern that contains all subpatterns contained 
//     * in thegiven pattern descriptor; used to visualize the pattern descriptor;
//     * recomputes support of nodes in subpatterns; uses corresponding references
//     * in MintPatternLayer to determine valid observations of seutrees
//     * @param pQuery MintQuery containing the pattern descriptors and its context
//     * @param pSuccessfulPatternDescriptorIndex index of the pattern descriptor 
//     * to visualize in pQuery's list of successful pattern descriptors
//     * @return AggregateTree containing all valid observations
//     */
//
//    private NavigationPattern generateNavigationPattern (MintQuery pQuery, 
//      int pSuccessfulPatternDescriptorIndex) {
//
//      if (SessionMiningBase != null) 
//        try {
//          SessionMiningBase.join();    
//        }
//        catch (ObjectStoreException e) {}   
//
//      PatternDescriptor[OUTPUT] = 
//        pQuery.getSuccessfulPatternDescriptor( 
//          pSuccessfulPatternDescriptorIndex );
//      Pattern[OUTPUT] = PatternDescriptor[OUTPUT].getPattern();
//
//      int vStartSubTreeIndex = 0;    
//      // determine the number of subpatterns
//      if ( pQuery.getTemplate().getSearchExpression(0).getType() == 
//        SearchExpression.TYPE_WUM_GIVEN ) {
//        // template begins with variable; 
//        // WUM added * before first variable for correct search
//        NavPattern = new NavigationPattern(
//          PatternDescriptor[OUTPUT].getLength(), 
//          PatternDescriptor[OUTPUT].getLength() );      
//        vStartSubTreeIndex = 1;
//      }
//      else {
//        // template begins with root symbol; first placeholder 
//        // after root symbol is valid
//        NavPattern = new NavigationPattern(
//          PatternDescriptor[OUTPUT].getLength(), 
//          PatternDescriptor[OUTPUT].getLength() + 1 );      
//        vStartSubTreeIndex = 0;
//      }
//
//      MintPatternDescriptor oPatternDescriptor = new MintPatternDescriptor(1);
//      Trail oPattern = new Trail();
//      // set all subpatterns in new navigation pattern
//      for ( int i = 0; i < 
//        NavPattern.countSubPatternDescriptors(); i++ ) {
//        oPattern.setFirstPageOccurrence( 
//          PatternDescriptor[OUTPUT].getPageOccurrence(i) );
//        oPatternDescriptor.setPattern(oPattern);
//        oPatternDescriptor.setLayer( 0, PatternDescriptor[OUTPUT].getLayer(i) ); 
//        NavPattern.setSubPatternDescriptor(i, oPatternDescriptor);
//        oPatternDescriptor = new MintPatternDescriptor(1);
//        oPattern = new Trail();
//      }    
//
//      // set the SearchExpression for the first subtree
//      SearchExpressions[OUTPUT] = new SearchExpression[1];
//      SearchExpressions[OUTPUT][0] = (SearchExpression)
//        pQuery.getTemplate().getSearchExpression(0).clone();
//      SearchExpressions[OUTPUT][0].setLookForPageOccurrence(
//        NavPattern.getSubPatternDescriptor(0).getPattern().
//        getFirstPageOccurrence() );    
//
//      if (vStartSubTreeIndex == 0) {
//        // template begins with root symbol: generate sutree from root to 
//        // first variable
//        AggTree = new AggregateTree();
//        // root depends on whether persistent or transient aggregated Log 
//        // is to be queried
//        // generate subtree starting at root
//        AggTree = this.generateAggregateTree(  
//          pQuery, NavPattern.getSubPatternDescriptor(0),
//          ( AggregatedLogToQuery == 
//            CreateAggregatedLogParameter.TRANSIENT_AGGREGATED_LOG ? 
//            CurrentMiningBase.getTransientAggregatedLog().getRoot() : 
//            CurrentMiningBase.getAggregatedLog().getRoot() ), 
//            SearchExpressions[OUTPUT]);   
//        NavPattern.setSubTree(0, AggTree);  
//      }
//
//      // generate subtrees for all pairs of variables
//      for (int i = 1; i < NavPattern.countSubPatternDescriptors(); 
//        i++ ) {
//
//        AggTree = new AggregateTree();
//        // push root of next subtree (variable's binding)
//        AggTree.pushNewObservationNode(  
//          NavPattern.getSubPatternDescriptor(i-1).getPattern().
//            getPageOccurrence(0),
//          NavPattern.getSubPatternDescriptor(i-1).getSupport(0), 
//          SearchExpressions[OUTPUT][0].getLookForVariableName() ); 
//
//        // set the SearchExpression for the (relatively) next subtree
//        SearchExpressions[OUTPUT] = new SearchExpression[1];
//        SearchExpressions[OUTPUT][0] = (SearchExpression)
//          pQuery.getTemplate().getSearchExpression(i).clone();  
//        SearchExpressions[OUTPUT][0].setLookForPageOccurrence(
//          NavPattern.getSubPatternDescriptor(i).getPattern().
//          getFirstPageOccurrence() );
//
//        // generate next subtree using all references in MintPatternLayer
//        for (int j = 0; j < NavPattern.getSubPatternDescriptor(i-1).
//          getLayer(0).getSize(); j++) {
//          AggTree = this.generateAggregateTree(
//            pQuery, NavPattern.getSubPatternDescriptor(i),
//            NavPattern.getSubPatternDescriptor(i-1).getLayer(0).
//            getObservationNode(j), SearchExpressions[OUTPUT]);     
//          AggTree.popAllObservationNodesButRootPlus(1);
//        }
//
//      if (vStartSubTreeIndex == 0)
//        NavPattern.setSubTree(i, AggTree);
//      else
//        NavPattern.setSubTree(i - 1, AggTree);
//
//      }
//
//      // generate suptree for the last variable's binding
//      AggTree = new AggregateTree();
//      AggTree.pushNewObservationNode( 
//        NavPattern.getSubPatternDescriptor( 
//          NavPattern.countSubPatternDescriptors() - 1 ).getPattern().
//          getPageOccurrence(0), NavPattern.getSubPatternDescriptor(
//          NavPattern.countSubPatternDescriptors() - 1 ).getSupport(0),
//        SearchExpressions[OUTPUT][0].getLookForVariableName() ); 
//      NavPattern.setSubTree(NavPattern.countSubTrees() - 1, 
//        AggTree);
//
//      // ################### new: enhance navigationPattern according to GSM
//
//      this.gsmNavigationPattern(NavPattern, pQuery, 
//        pSuccessfulPatternDescriptorIndex);
//
//      // ################### new
//
//      return NavPattern;
//
//    }  // generateNavigationPattern()  
//
//    /**
//     * generates the AggregateTree AggTree which must be cleared before ; 
//     * uses the given PatternDescriptor to look for observations in the subtree 
//     * of pStartObservationNode, SearchExpressions are given by 
//     * pSearchExpressions;
//     * uses the push/pop-mechanism of AggregateTree to generate the new instance;
//     * uses corresponding traversal environment and calls 
//     * continueGeneratingAggregateTree()
//     * @param pQuery context of the AggregateTree to generate
//     * @param pSuccessfulPatternDescriptor contains current pattern 
//     * @param pStartObservationNode ObservationNode in Aggregated Log containing 
//     * subtree to search
//     * @param pSearchExpressions SearchExpression describing valid Observation 
//     */
//
//    private AggregateTree generateAggregateTree(MintQuery pQuery, 
//      MintPatternDescriptor pSuccessfulPatternDescriptor, 
//      ObservationNode pStartObservationNode, 
//      SearchExpression[] pSearchExpressions) {
//
//      if (SessionMiningBase != null) 
//        try {
//          SessionMiningBase.join();    
//        }
//        catch (ObjectStoreException e) {}   
//      Query = pQuery;
//
//      PatternDescriptor[OUTPUT] = pSuccessfulPatternDescriptor;
//      Pattern[OUTPUT] = PatternDescriptor[OUTPUT].getPattern();
//
//      AggTreeNode[OUTPUT] = new ObservationNode();
//      CurrentPattern[OUTPUT] = new Stack();  
//      int LevelAdded = -1;
//
//      Variables[OUTPUT] = Pattern[OUTPUT].getSize();
//      CurrentVariable[OUTPUT] = 0;
//
//      if (pSearchExpressions == null) {  // not set yet
//        SearchExpressions[OUTPUT] = new SearchExpression[ Variables[OUTPUT] ];
//        for (int i = 0; i < SearchExpressions[OUTPUT].length; i++) {
//          SearchExpressions[OUTPUT][i] = (SearchExpression)
//            ( ( Query.getTemplate().getSearchExpressions() )[i] ).clone();  
//          if (i == 0)
//            SearchExpressions[OUTPUT][i].setLookForPageOccurrence(
//              Pattern[OUTPUT].getFirstPageOccurrence() );
//          else
//            SearchExpressions[OUTPUT][i].setLookForPageOccurrence(
//              Pattern[OUTPUT].getNextPageOccurrence() );
//        }
//      }
//      else 
//        SearchExpressions[OUTPUT] = pSearchExpressions;
//
//      FoundVariableAtLevel = new int[ Variables[OUTPUT] ];
//      for (int i = 0; i < FoundVariableAtLevel.length; i++)
//        FoundVariableAtLevel[i] = -1;  // must be != 0 because 0 is a valid level
//      TraversalStacks[OUTPUT] = new Stack[1];
//      TraversalStacks[OUTPUT][0] = new Stack();
//      CurrentChildStacks[OUTPUT] = new Stack[1];
//      CurrentChildStacks[OUTPUT][0] = new Stack();
//
//      SearchExpressions[OUTPUT][ CurrentVariable[OUTPUT] ].resetCounter();
//      TraversalStacks[OUTPUT][0] = new Stack();
//      pushObservationNode( OUTPUT, 0, pStartObservationNode );
//      CurrentChildStacks[OUTPUT][0] = new Stack();
//      pushCurrentChild(OUTPUT, 0, 0);
//
//      int ObservationsAdded = 0;    
//      CurrentLevel = 0; 
//      PreviousLevel = 0;
//      MinimumLevel = 0;
//      int vFirstVariableLevel = -1;
//      int vLastVariableLevel = -1;
//
//      // uses complex algorithm to create tree starting at pStartObservationNode;
//      // the currently interesting parts of traversal stack must be separated; 
//      // adding one ObservationNode more than once is not allowed, therefore 
//      // backtracking must be detected
//
//      AggTreeNode[OUTPUT] = this.continueGeneratingAggregateTree();
//      // kwinkler 9/2000: ForwardIterator replaced by ListIterator
//      // ForwardIterator used a different method of advancing in a collection
//      ObservationNode nextNode = null;
//      while ( AggTreeNode[OUTPUT] != null ) {
//
//        StackIterator = TraversalStacks[OUTPUT][0].listIterator();
//        if ( StackIterator.hasNext() ) 
//          nextNode = (ObservationNode)StackIterator.next();
//
//        // no previous backtracking
//        if (MinimumLevel == PreviousLevel) {
//
//          // iterate through traversal stack to find new nodes
//          int j = 0;      
//          if ( (vFirstVariableLevel == -1) && 
//            (SearchExpressions[OUTPUT][0].getType() == 
//            SearchExpression.TYPE_WUM_GIVEN) ) {
//            for ( ; j < TraversalStacks[OUTPUT][0].size(); j++) {
//              if ( StackIterator.hasNext() ) 
//                nextNode = (ObservationNode)StackIterator.next();
//              vFirstVariableLevel = j + 1;
//              if ( nextNode.getPageOccurrence().equals( 
//                SearchExpressions[OUTPUT][0].getLookForPageOccurrence() ) ) {
//                j++;
//                break;
//              }
//            }
//          }          
//          for ( ; (j <= PreviousLevel) && 
//            (j < TraversalStacks[OUTPUT][0].size()); j++) 
//            if ( StackIterator.hasNext() ) 
//              nextNode = (ObservationNode)StackIterator.next();         
//
//          // add new nodes to AggTree
//          for (  ; j < TraversalStacks[OUTPUT][0].size(); j++) {     
//            if ( j == ( TraversalStacks[OUTPUT][0].size() - 1 ) )
//              AggTree.pushNewObservationNode(  // add variable name
//                nextNode.getPageOccurrence(),
//                nextNode.getSupport(), CurrentVariableName );  
//            else
//              AggTree.pushNewObservationNode(
//                nextNode.getPageOccurrence(), nextNode.getSupport(), "" ); 
//            if ( StackIterator.hasNext() ) 
//              nextNode = (ObservationNode)StackIterator.next();          
//        }
//        // end of: no previous backtracking
//
//        }  else 
//
//        // previous backtracking
//        if (MinimumLevel < PreviousLevel) {
//
//          // pop old nodes in AggregateTree
//          if ( SearchExpressions[OUTPUT][0].getType() == 
//            SearchExpression.TYPE_WUM_GIVEN ) {
//            if (MinimumLevel < vFirstVariableLevel) {
//              vFirstVariableLevel = -1;
//              for (int i = AggTree.getTraversalStackSize(); i > 1; i--) {
//                AggTree.popCurrentObservationNode(); 
//              }
//            }
//            else {
//              for (int i = Math.max(0, (MinimumLevel - vFirstVariableLevel) ); 
//                i < Math.max(0, (PreviousLevel - vFirstVariableLevel) ) ; i++) {
//                AggTree.popCurrentObservationNode(); 
//              }
//            }          
//          }
//          else
//            for (int i = MinimumLevel; i < PreviousLevel ; i++) 
//              AggTree.popCurrentObservationNode();   
//
//          // iterate through traversal stack to find new nodes
//          int j = 0;        
//          if ( (vFirstVariableLevel == -1) && 
//            (SearchExpressions[OUTPUT][0].getType() == 
//            SearchExpression.TYPE_WUM_GIVEN) ) {
//            for ( ; j < TraversalStacks[OUTPUT][0].size(); j++) {
//              if ( StackIterator.hasNext() ) 
//                nextNode = (ObservationNode)StackIterator.next();
//              vFirstVariableLevel = j + 1;
//              if ( nextNode.getPageOccurrence().equals(
//                SearchExpressions[OUTPUT][0].getLookForPageOccurrence() ) ) {
//                j++;
//                break;
//              }
//            }
//          }
//
//          for ( ; (j <= MinimumLevel) && 
//            (j < TraversalStacks[OUTPUT][0].size()); j++)       
//            if ( StackIterator.hasNext() ) 
//              nextNode = (ObservationNode)StackIterator.next();
//
//          // add new nodes to AggTree
//          for ( ; j < TraversalStacks[OUTPUT][0].size(); j++) {             
//            if ( j == ( TraversalStacks[OUTPUT][0].size() - 1 ) )
//              AggTree.pushNewObservationNode(  // add variable name
//                nextNode.getPageOccurrence(), nextNode.getSupport(), 
//                CurrentVariableName );  
//            else {
//              AggTree.pushNewObservationNode(
//                nextNode.getPageOccurrence(), nextNode.getSupport(), "" );
//            }    
//            if ( StackIterator.hasNext() ) 
//              nextNode = (ObservationNode)StackIterator.next();
//          } 
//
//        } 
//
//        ObservationsAdded++; 
//        PreviousLevel = CurrentLevel;
//        MinimumLevel = CurrentLevel;
//        CurrentVariableName = "";
//        AggTreeNode[OUTPUT] = this.continueGeneratingAggregateTree();
//
//      }  // while
//
//      return AggTree;
//
//    }  // generateAggregateTree()
//
//    /**
//     * returns the next valid (according to SearchExpressions) ObservationNode
//     * in the given traversal environment; only for [OUTPUT]; recursive method;
//     * stops if TraversalStack.empty()
//     */
//
//    private ObservationNode continueGeneratingAggregateTree() {
//
//      int currentChild = getCurrentChild(OUTPUT, 0);
//      if (currentChild == -1) return null;
//      ObservationNode currentNode = getObservationNode(OUTPUT, 0);
//      if (currentNode == null) return null;
//
//      ObservationNode resultNode = null;
//      if ( currentChild < currentNode.countChildren() ) 
//        resultNode = currentNode.getChild(currentChild);
//
//      if (resultNode != null) {
//
//        // modified by kwinkler 11/99
//        int validSearchExpression = SearchExpressions[OUTPUT]
//          [ CurrentVariable[OUTPUT] ].incrementCounter( 
//          WumServer, resultNode.getPageOccurrence() );
//
//        if (validSearchExpression == SearchExpression.VALID) {    
//
//          if ( SearchExpressions[OUTPUT][ CurrentVariable[OUTPUT] ].
//            getLookForPageOccurrence().equals( 
//            resultNode.getPageOccurrence() ) ) {          
//            CurrentLevel++;
//            FoundVariableAtLevel[ CurrentVariable[OUTPUT] ] = CurrentLevel; 
//            CurrentVariableName = SearchExpressions[OUTPUT]
//              [ CurrentVariable[OUTPUT] ].getLookForVariableName();
//            if ( (CurrentVariable[OUTPUT] + 1) < Variables[OUTPUT]) {
//              // more variables            
//              CurrentVariable[OUTPUT]++;
//              SearchExpressions[OUTPUT][ CurrentVariable[OUTPUT] ]
//                .resetCounter();
//              incrementCurrentChild(OUTPUT, 0);
//              pushObservationNode(OUTPUT, 0, resultNode);
//              pushCurrentChild(OUTPUT, 0, 0);
//            }
//            else {
//              // last variable
//              incrementCurrentChild(OUTPUT, 0);
//              pushObservationNode(OUTPUT, 0, resultNode);
//              pushCurrentChild(OUTPUT, 0, resultNode.countChildren() );
//            }
//            return resultNode;
//
//          }
//          else {
//            CurrentLevel++;
//            incrementCurrentChild(OUTPUT, 0);
//            pushObservationNode(OUTPUT, 0, resultNode);
//            pushCurrentChild(OUTPUT, 0, 0);
//            return continueGeneratingAggregateTree();
//          }
//
//        } else
//        if (validSearchExpression == SearchExpression.SMALLER_MIN) { 
//          CurrentLevel++;
//          incrementCurrentChild(OUTPUT, 0);
//          pushObservationNode(OUTPUT, 0, resultNode);
//          pushCurrentChild(OUTPUT, 0, 0);
//          return continueGeneratingAggregateTree();
//        } else
//        if (validSearchExpression == SearchExpression.GREATER_MAX) {
//          if ( FoundVariableAtLevel[ 
//            Math.max(CurrentVariable[OUTPUT] - 1, 0) ] == CurrentLevel ) 
//            CurrentVariable[OUTPUT] = Math.max( CurrentVariable[OUTPUT] - 1, 0);
//          CurrentLevel--;
//          SearchExpressions[OUTPUT][ CurrentVariable[OUTPUT] ]
//            .decreaseCounter(2);
//          popObservationNode(OUTPUT, 0); 
//          popCurrentChild(OUTPUT, 0);
//          MinimumLevel = Math.min(MinimumLevel, CurrentLevel);
//          return continueGeneratingAggregateTree();
//        } else
//        // added by kwinkler 11/99
//        if (validSearchExpression == SearchExpression.LAST_VALID) { 
//
//          if ( SearchExpressions[OUTPUT][ CurrentVariable[OUTPUT] ].
//            getLookForPageOccurrence().equals( 
//            resultNode.getPageOccurrence() ) ) {          
//            CurrentLevel++;
//            FoundVariableAtLevel[ CurrentVariable[OUTPUT] ] = CurrentLevel; 
//            CurrentVariableName = SearchExpressions[OUTPUT]
//              [ CurrentVariable[OUTPUT] ].getLookForVariableName();
//            if ( (CurrentVariable[OUTPUT] + 1) < Variables[OUTPUT]) {
//              // more variables            
//              CurrentVariable[OUTPUT]++;
//              SearchExpressions[OUTPUT][ CurrentVariable[OUTPUT] ]
//                .resetCounter();
//              incrementCurrentChild(OUTPUT, 0);
//              pushObservationNode(OUTPUT, 0, resultNode);
//              pushCurrentChild(OUTPUT, 0, 0);
//            }
//            else {
//              // last variable
//              incrementCurrentChild(OUTPUT, 0);
//              pushObservationNode(OUTPUT, 0, resultNode);
//              pushCurrentChild(OUTPUT, 0, resultNode.countChildren() );
//            }
//            return resultNode;
//
//          }
//          else {
//            // CurrentLevel++;
//            SearchExpressions[OUTPUT][ CurrentVariable[OUTPUT] ].
//              decreaseCounter(1);
//            incrementCurrentChild(OUTPUT, 0);
//            return continueGeneratingAggregateTree();
//          }
//
//        } 
//      }
//      else {
//        if ( FoundVariableAtLevel[ 
//          Math.max(CurrentVariable[OUTPUT] - 1, 0) ] == CurrentLevel ) 
//          CurrentVariable[OUTPUT] = Math.max( CurrentVariable[OUTPUT] - 1, 0);
//        CurrentLevel--;
//        SearchExpressions[OUTPUT][ CurrentVariable[OUTPUT] ].decrementCounter(); 
//        popObservationNode(OUTPUT, 0);
//        popCurrentChild(OUTPUT, 0);
//        MinimumLevel = Math.min(MinimumLevel, CurrentLevel);
//        return continueGeneratingAggregateTree();
//      }
//
//      return null;
//
//    }  // getNextLookForObservationNode()  
//
//    // ########## traversal methods ##########    
//
//    // Hint: There are 2 stacks used in each traversal environment. A stack of 
//    // ObservationNodes contains the AggregateTree's node and a stack of 
//    // Integers contains the CurrentChild of the ObservationNodes. Both stacks 
//    // must always have the same size: The top Integer on the CurrentChildStacks
//    // represents the CurrentChildID of the top ObservationNode on the other 
//    // stack. pCurrentVariable is always the indicator of the traversal 
//    // environment
//
//    private int getCurrentChild(int pGeneratorEvaluator, int pCurrentVariable) {
//
//      if ( CurrentChildStacks[pGeneratorEvaluator][pCurrentVariable].isEmpty() )
//        return -1;
//      else
//        return ( (Integer)CurrentChildStacks[pGeneratorEvaluator]
//          [pCurrentVariable].peek() ).intValue();
//
//    }  // getCurrentChild()
//
//    private int incrementCurrentChild(int pGeneratorEvaluator, 
//      int pCurrentVariable) {
//
//      if ( CurrentChildStacks[pGeneratorEvaluator][pCurrentVariable].isEmpty() )
//        return -1;
//      else {      
//        int vNewCurrentChild = ( (Integer)CurrentChildStacks[pGeneratorEvaluator]
//          [pCurrentVariable].pop() ).intValue() + 1;
//        CurrentChildStacks[pGeneratorEvaluator][pCurrentVariable].push( 
//          new Integer(vNewCurrentChild) );
//        return vNewCurrentChild;
//      }
//
//    }  // incrementCurrentChild    
//
//    private void setCurrentChild(int pGeneratorEvaluator, int pCurrentVariable, 
//      int pNewCurrentChild) {
//
//      if ( ! CurrentChildStacks[pGeneratorEvaluator][pCurrentVariable]
//        .isEmpty() )
//        CurrentChildStacks[pGeneratorEvaluator][pCurrentVariable].push( 
//          new Integer(pNewCurrentChild) );
//
//    }  // setCurrentChild    
//
//    // ########## comments will follow ##########    
//
//    private void pushCurrentChild(int pGeneratorEvaluator, int pCurrentVariable, 
//      int pCurrentChild) {
//
//      CurrentChildStacks[pGeneratorEvaluator][pCurrentVariable].push( 
//        new Integer(pCurrentChild) );
//
//    }  // pushCurrentChild  
//
//    private void popCurrentChild(int pGeneratorEvaluator, int pCurrentVariable) {
//
//      if ( ! CurrentChildStacks[pGeneratorEvaluator][pCurrentVariable]
//        .isEmpty() )
//        TmpObject = 
//          CurrentChildStacks[pGeneratorEvaluator][pCurrentVariable].pop();
//
//    }  // popCurrentChild    
//
//    private ObservationNode getObservationNode(int pGeneratorEvaluator, 
//      int pCurrentVariable) {
//
//      if ( TraversalStacks[pGeneratorEvaluator][pCurrentVariable].isEmpty() ) 
//        return null;
//      else
//        return ( (ObservationNode)TraversalStacks[pGeneratorEvaluator]
//          [pCurrentVariable].peek() );
//
//    }  // getObservationNode()
//
//    private void pushObservationNode(int pGeneratorEvaluator, 
//      int pCurrentVariable, ObservationNode pObservationNode) {
//
//      TraversalStacks[pGeneratorEvaluator][pCurrentVariable].
//        push(pObservationNode);
//
//    }  // pushObservationNode()
//
//    private void popObservationNode(int pGeneratorEvaluator, 
//      int pCurrentVariable) {
//
//      if ( ! TraversalStacks[pGeneratorEvaluator][pCurrentVariable].isEmpty() ) 
//        TmpObject = TraversalStacks[pGeneratorEvaluator][pCurrentVariable].pop();
//
//    }  // popObservationNode()    
//
//    /**
//     * method to enhance navigation pattern according to GSM: sub paths of an 
//     * aggregate tree that don't reach the binding of a variable are now also 
//     * included, works only with subtrees of navigation patterns,
//     * @param pNavigationPattern NavigationPattern whose AggregateTrees must be 
//     * enhanced
//     * @param pQuery MintQuery containing the pattern descriptors and its context
//     * @param pSuccessfulPatternDescriptorIndex index of the pattern descriptor 
//     * to visualize in pQuery's list of successful pattern descriptors
//     * @return NavigationPattern that consists of correctly enhanced 
//     * AggregateTrees according to GSM
//     */
//
//    private NavigationPattern gsmNavigationPattern(
//      NavigationPattern pNavigationPattern, MintQuery pQuery,
//      int pSuccessfulPatternDescriptorIndex) {
//
//      PatternDescriptor[OUTPUT] = pQuery.getSuccessfulPatternDescriptor( 
//        pSuccessfulPatternDescriptorIndex );
//      Pattern[OUTPUT] = PatternDescriptor[OUTPUT].getPattern();
//
//      // System.out.println("count SubPattDesc = " +  
//      //   pNavigationPattern.countSubPatternDescriptors());
//      // System.out.println("count SubTrees = " +  
//      //   pNavigationPattern.countSubTrees());
//
//      Trail oPattern = null; 
//      MintPatternDescriptor oPatternDescriptor = null; 
//      AggregateTree oNewTree = null;
//
//      // (pNavigationPattern.countSubTrees() - 1) = 
//      // skip last sub tree that only contains the last variable
//      for (int vSubPatternCounter = 0; 
//        vSubPatternCounter < (pNavigationPattern.countSubTrees() - 1); 
//        vSubPatternCounter++) {
//
//        oNewTree = (AggregateTree)pNavigationPattern.
//          getSubTree(vSubPatternCounter); oNewTree.initSupport();
//
//        oPattern = new Trail(); 
//        oPatternDescriptor = new MintPatternDescriptor(1);
//        // oPattern.setFirstPageOccurrence( 
//        // PatternDescriptor[OUTPUT].getPageOccurrence(0) );
//        // oPatternDescriptor.setLayer(0, 
//        //   PatternDescriptor[OUTPUT].getLayer(0) );
//
//        if ( pQuery.getTemplate().getSearchExpression(0).getType() == 
//          SearchExpression.TYPE_WUM_GIVEN ) {
//          // template starts with variable
//          oPattern.setFirstPageOccurrence( PatternDescriptor[OUTPUT].
//            getPageOccurrence(vSubPatternCounter) );
//          oPatternDescriptor.setPattern(oPattern);
//          oPatternDescriptor.setLayer(0, PatternDescriptor[OUTPUT].
//            getLayer(vSubPatternCounter) );
//        }
//        else {
//          // template starts with wildcard or root
//            if (vSubPatternCounter == 0) {
//              // start 1st subtree with root in AggregateLog
//              oPattern.setFirstPageOccurrence( PatternDescriptor[OUTPUT].
//                getPageOccurrence(vSubPatternCounter) );
//              oPatternDescriptor.setPattern(oPattern);
//              oPatternDescriptor.setLayer(0, PatternDescriptor[OUTPUT].
//                getLayer(vSubPatternCounter) );
//            }
//            else {
//              // start next subtrees with 1st variable in AggregateLog, 
//              // decrement 'real' vSubPatternCounter due to # in template
//              oPattern.setFirstPageOccurrence( PatternDescriptor[OUTPUT].
//                getPageOccurrence(vSubPatternCounter-1) );
//              oPatternDescriptor.setPattern(oPattern);
//              oPatternDescriptor.setLayer(0, PatternDescriptor[OUTPUT].
//                getLayer(vSubPatternCounter-1) ); 
//            }
//        }             
//
//        // System.out.println( "\n" + vSubPatternCounter + " oPattern: " + 
//        //   oPattern );
//        // System.out.println( vSubPatternCounter + " oPatternDescriptor: " + 
//        //   oPatternDescriptor );
//        // System.out.println( vSubPatternCounter + 
//        //   " pNavigationPattern.countSubTrees(): " + 
//        //   pNavigationPattern.countSubTrees() );
//
//          if ( pQuery.getTemplate().getSearchExpression(0).getType() == 
//            SearchExpression.TYPE_WUM_GIVEN ) {
//            // template starts with variable
//            // start all subtrees with 1st variable in AggregateLog
//            for (int i = 0; i < oPatternDescriptor.getLayer(0).getSize(); i++) {
//              this.gsmUpdatePatternSupport(  oPatternDescriptor.getLayer(0).
//                getObservationNode(i), oNewTree.getRoot().getChild(0) ); 
//            }
//            // pNavigationPattern.countSubTrees() = 
//            //   pNavigationPattern.countSubPatternDescriptors() + 1
//            // therefore stop current loop here
//            if ( vSubPatternCounter == (pNavigationPattern.countSubTrees() - 2) )
//              break;  
//          }
//          else {
//            // template starts with wildcard or root
//            if (vSubPatternCounter == 0)
//                // start 1st subtree with root in AggregateLog 
//              this.gsmUpdatePatternSupport( AggregatedLogToQuery == 
//                CreateAggregatedLogParameter.TRANSIENT_AGGREGATED_LOG ? 
//                CurrentMiningBase.getTransientAggregatedLog().getRoot() : 
//                CurrentMiningBase.getAggregatedLog().getRoot(), 
//                  oNewTree.getRoot() );
//            else
//              // start next subtrees with 1st variable in AggregateLog
//              for (int i = 0; i < oPatternDescriptor.getLayer(0).getSize(); 
//                i++) {
//                this.gsmUpdatePatternSupport(  
//                oPatternDescriptor.getLayer(0).getObservationNode(i), 
//                oNewTree.getRoot().getChild(0) ); 
//              }             
//
//          } 
//
//      }  // all subtrees
//
//
//      return pNavigationPattern;
//
//    }  // gsmNavigationPattern()  
//
//    /**
//     * updates PatternTree with support values from subtree of ParentTree 
//     * starting at ParentStartNode according to same structure of PatternTree; 
//     * start: pParentStartNode.getPageOccurrence == 
//     * pPatternStartNode.getPageOccurrence(); method traverses recursively both 
//     * tree; before starting the enhancement of each pattern aggregate tree all 
//     * support values in it must be reset to zero 
//     * @param pParentStartNode start node resp. current node in the original 
//     * aggregated log (either persistent ortransient)
//     * @param pPatternStartNode start node resp. current node in aggregated log 
//     * that contains the pattern and that has to be enhanced according to the 
//     * support of itself in in the original aggregated log
//     */
//
//    private void gsmUpdatePatternSupport(ObservationNode pParentStartNode, 
//      ObservationNode pPatternStartNode) {
//
//      if ( pParentStartNode.getPageOccurrence().equals( 
//        pPatternStartNode.getPageOccurrence() ) ) 
//        pPatternStartNode.increaseSupport( pParentStartNode.getSupport() );
//      else
//        return;
//
//      ObservationNode oCurrentPatternChild = null, oCurrentParentChild = null;
//
//      // traverse all children below pPatternStartNode in pattern a
//      // ggregated tree to be enhanced
//      for (int vCurrentPatternChildID = 0; 
//        vCurrentPatternChildID < pPatternStartNode.countChildren(); 
//        vCurrentPatternChildID++) {
//
//        oCurrentPatternChild = pPatternStartNode
//          .getChild(vCurrentPatternChildID);
//        if (oCurrentPatternChild == null) 
//          return;
//        else {
//          // System.out.println("### TraversePattern: " + oCurrentPatternChild);
//          // look for equal pattern in the original aggregate tree
//          oCurrentParentChild = pParentStartNode.getChild( 
//            oCurrentPatternChild.getPageOccurrence() );
//          if ( (oCurrentParentChild != null) && 
//            (oCurrentParentChild.getPageOccurrence().equals( 
//            oCurrentPatternChild.getPageOccurrence() ) ) ) {
//            // System.out.println("### EqualParentNode: " + oCurrentParentChild);
//            this.gsmUpdatePatternSupport(oCurrentParentChild, 
//              oCurrentPatternChild);
//          }
//        }
//
//      }
//
//    }  // gsmUpdatePatternSupport()
//
//    // ########## comments will follow ##########
//
//    public static AggregateTree_SelfContained convertAggregateTree(
//      AggregateTree pAggregateTree, WUMserver pWumServer) {
//
//      // creates transient and self-contained tree to be dealt with
//
//      // must later be removed; use X as SupportAdded ...
//      pAggregateTree.initCurrentChild();   
//
//      AggregateTree sourceTree = pAggregateTree;
//      AggregateTree_SelfContained targetTree = 
//        new AggregateTree_SelfContained();
//      targetTree.setMaxLevel( sourceTree.getMaxLevel() );
//      targetTree.setCounterObservations( sourceTree.countObservations() );
//      targetTree.setRootSupport( sourceTree.getRootSupport() );
//
//      Observation sourceObservation = null;
//      ObservationNode sourceNode = null;
//      ObservationNode_SelfContained[] targetObservation = null;
//      ObservationNode_SelfContained targetNode = null;
//
//      sourceObservation = sourceTree.getFirst();
//      while (sourceObservation != null) {
//
//        targetObservation = 
//          new ObservationNode_SelfContained[ sourceObservation.getSize() ];
//        for (int i = 0; i < targetObservation.length; i++) {
//          targetNode = new ObservationNode_SelfContained(
//            new PageOccurrence_SelfContained( 
//              sourceObservation.getObservationNodeAt(i).getPageID(),
//              sourceObservation.getObservationNodeAt(i).getOccurrence()
//            ),
//            ( pWumServer.getPage( 
//            sourceObservation.getObservationNodeAt(i).getPageID() ) ).getUrl(),
//            sourceObservation.getObservationNodeAt(i).getSupport(),
//            sourceObservation.getObservationNodeAt(i).getLevel(),
//            sourceObservation.getObservationNodeAt(i).getVariableName()
//          );
//          // copy support added flag from one tree to the other to ensure 
//          // correct support measure it's assumed that at the beginning all 
//          // ResultTrees contain X=0 values, because each
//          // ResultTree is generated on the fly and doesn't contain original 
//          // ObservationNode from persistent AggregatedLog, each ResultTree is 
//          // a transient AggregateTree
//          if ( sourceObservation.getObservationNodeAt(i).getX() == 1 )
//            // X=1 == SupportAdded=true (SupportAdded is not contained in 
//            // ObservationNode anymore!)
//            targetNode.setSupportAdded(true); 
//            targetObservation[i] = targetNode;
//        }
//
//        ObservationNode_SelfContained[] targetObservationAfterAdding = 
//          targetTree.addObservationNew(targetObservation);  
//        // copy support added flag from one tree to the other to keep 
//        // track of last changes to X
//        for (int i = 0; i < targetObservationAfterAdding.length; i++) {
//          if ( targetObservationAfterAdding[i].getSupportAdded() )    
//            ( sourceObservation.getObservationNodeAt(i) ).setX(1);
//        }        
//        sourceObservation = sourceTree.getNext();
//
//      }
//
//      return targetTree;
//
//    }  // convertAggregateTree()  
//
//    // ########## comments will follow ##########
//
//    public static NavigationPattern_SelfContained convertNavigationPattern(
//      NavigationPattern pNavigationPattern, WUMserver pWumServer) {
//
//      // creates transient and self-contained pattern to be dealt with
//
//      NavigationPattern_SelfContained targetPattern = 
//        new NavigationPattern_SelfContained( 
//        pNavigationPattern.countSubTrees() );
//      for (int i = 0; i < targetPattern.countSubTrees(); i++)
//        targetPattern.setSubTree(i, MintQueryProcessor.convertAggregateTree( 
//          pNavigationPattern.getSubTree(i), pWumServer ) );
//
//      return targetPattern;
//
//    }  // convertNavigationPattern()
//
//    // ########## comments will follow ##########
//
//    public AggregateTree_SelfContained generateGSequenceTree_SelfContained(
//      GenerateGSequenceTreeParameter pParameter, WUMserver pWumServer) {
//
//      return MintQueryProcessor.convertAggregateTree( 
//        this.generateGSequenceTree( pParameter.getQuery(), 
//          pParameter.getSelectedPattern() ), pWumServer ); 
//
//    }  // generateGSequenceTree_SelfContained 
//
//    // ########## comments will follow ##########
//
//    public AggregateTree_SelfContained generateAggregateTree_SelfContained(
//      GenerateAggregateTreeParameter pParameter, WUMserver pWumServer) {
//
//      return MintQueryProcessor.convertAggregateTree( 
//        this.generateAggregateTree( pParameter.getQuery(), 
//          pParameter.getSelectedPattern() ), pWumServer ); 
//
//    }  // generateAggregateTree_SelfContained 
//
//    // ########## comments will follow ##########
//
//    public NavigationPattern_SelfContained 
//      generateNavigationPattern_SelfContained(
//      GenerateNavigationPatternParameter pParameter, WUMserver pWumServer) {
//
//      return MintQueryProcessor.convertNavigationPattern( 
//        this.generateNavigationPattern( pParameter.getQuery(), 
//          pParameter.getSelectedPattern() ), pWumServer ); 
//
//    }  // generateNavigationPattern_SelfContained 

}  // class MintQueryProcessor
