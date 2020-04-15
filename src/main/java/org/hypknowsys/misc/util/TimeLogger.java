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

package org.hypknowsys.misc.util;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class TimeLogger {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private TextFile OutputTextFile = null;
  private long StartTime = 0L;
  private long StopTime = 0L;
  private String StopString = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public TimeLogger() {
  
    OutputTextFile = null;
    StartTime = 0L;
    StopTime = 0L;
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public TimeLogger(File pOutputFile, String pFirstLine) {
  
    OutputTextFile = new TextFile(pOutputFile);
    OutputTextFile.open();
    OutputTextFile.setNextLine("");
    OutputTextFile.setNextLine("# # # # # # # # # # # # # ");
    OutputTextFile.setNextLine("");
    OutputTextFile.setNextLine(pFirstLine);
    OutputTextFile.setNextLine("");
    StartTime = 0L;
    StopTime = 0L;
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getStopString() {
    return StopString; }

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

  public void log(String pLogInformation) {
  
    System.out.println("\n" + pLogInformation);    
    OutputTextFile.setNextLine(pLogInformation);
  
  } 
  
  /* ########## ########## ########## ########## ########## ######### */

  public void startQuietly() {
  
    StopTime = 0L;
    StartTime = ( new Date() ).getTime();
  
  }  
  
  /* ########## ########## ########## ########## ########## ######### */

  public void start(String pStartInformation) {
  
    System.out.println("\nSTART " + pStartInformation);    
    OutputTextFile.setNextLine("START " + pStartInformation);
    StartTime = ( new Date() ).getTime();
  
  }  
  
  /* ########## ########## ########## ########## ########## ######### */

  public String startString(String pStartInformation) {
  
    OutputTextFile.setNextLine("START " + pStartInformation);
    StartTime = ( new Date() ).getTime();
    return "\nSTART " + pStartInformation;    
  
  }  

  /* ########## ########## ########## ########## ########## ######### */

  public String stopQuietly() {
  
    StopTime = ( new Date() ).getTime();
    StopString = DeltaMillisToTimeString(StopTime - StartTime);
    
    return StopString;    
  
  }  
  
  /* ########## ########## ########## ########## ########## ######### */

  public void stop(String pStopInformation) {
  
    StopTime = ( new Date() ).getTime();
    System.out.println("\nSTOP " + 
      DeltaMillisToTimeString(StopTime - StartTime) +
      " " + pStopInformation);    
    OutputTextFile.setNextLine("STOP " + 
      DeltaMillisToTimeString(StopTime - StartTime) +
      " " + pStopInformation);   
  
  }  
  
  /* ########## ########## ########## ########## ########## ######### */

  public String stopString(String pStopInformation) {
  
    StopTime = ( new Date() ).getTime();
    StopString = DeltaMillisToTimeString(StopTime - StartTime);
    OutputTextFile.setNextLine("STOP " + StopString +
      " " + pStopInformation);   
    return "\nSTOP " + StopString + " " + pStopInformation;    
 
  }  
  
  /* ########## ########## ########## ########## ########## ######### */

  public void close() {
  
    OutputTextFile.close();
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private String DeltaMillisToTimeString(long pDeltaMillis) {
  
    long h = 0L, min = 0L, sec = 0L, msec = 0L;
    String minString = null, secString = null, msecString = null;
    
    h = pDeltaMillis / 3600000;
    pDeltaMillis %= 3600000;
    min = pDeltaMillis / 60000;
    pDeltaMillis %= 60000;
    sec = pDeltaMillis / 1000;
    msec = pDeltaMillis % 1000;

    if (min < 10l)
      minString = "0" + min;
    else
      minString = (new Long(min) ).toString();
    if (sec < 10l)
      secString = "0" + sec;
    else
      secString = (new Long(sec) ).toString();
    if (msec < 10l)
      msecString = "00" + msec;
    else if (msec < 100l)
      msecString = "0" + msec;
    else
      msecString = (new Long(msec) ).toString();

    return h + ":" + minString + ":" + secString + "." + msecString;
    
  }  

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {
  
    System.out.println("Start: ...");
    TimeLogger logger = new TimeLogger();
    logger.startQuietly();
    for (int i = 0; i < 10000000; i++) { String string = ""; }
    logger.stopQuietly();
    System.out.println("Start: " + logger.getStopString());
  
  }
  
}