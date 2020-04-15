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

package org.hypknowsys.misc.net;

import java.io.*;
import java.net.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class Http {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

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

  public Http() {}

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

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

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String get(String pHost, int pPort, String pUri, int pRetries, long pRetryAfterMillis) {

    // example: Http.get("www.demo.de",80,"/index.html",3,1000) 
    // return HTML page as a String or null
 
    if ( (pRetries < 0) || (pRetryAfterMillis < 0) ) return null;

    Socket oServer = null;
    DataInputStream oInStream = null; 
    DataOutputStream oOutStream = null;
    String vResult = null;
    int vCounter = 0, vMaxCounter = pRetries;

    while (vCounter < vMaxCounter) {
      try {
        oServer = new Socket(pHost, pPort);
        oServer.setSoTimeout(0);
        vCounter = vMaxCounter;
      }
      catch (UnknownHostException e1) { 
        waitMilliSeconds(pRetryAfterMillis);
        vCounter++;
      }
      catch (IOException e2) { 
        waitMilliSeconds(pRetryAfterMillis);
        vCounter++;
      }
    } 
    if (vCounter >= vMaxCounter) return null;
 
    try {
      oInStream = new DataInputStream( oServer.getInputStream() );
      oOutStream = new DataOutputStream( oServer.getOutputStream() );
      oOutStream.writeBytes("GET " + pUri + " HTTP/1.0\n\n"); 
      oOutStream.flush();

      vResult = oInStream.readLine();
      while (vResult != null)
        vResult = oInStream.readLine();

      oInStream.close();
      oOutStream.close();
    }
    catch (IOException e2) { return null; }

    return vResult;

  } 

  /* ########## ########## ########## ########## ########## ######### */

  private static void waitMilliSeconds(long pMillis) {

    // does pMillis milliseconds 'nothing'

    long vStartMillis = System.currentTimeMillis();
    long vDifference = System.currentTimeMillis() - vStartMillis;

    while (vDifference < pMillis) {
      vDifference = System.currentTimeMillis() - vStartMillis;
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}