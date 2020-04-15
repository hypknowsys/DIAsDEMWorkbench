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
import org.hypknowsys.misc.io.Itemizer;
import org.hypknowsys.misc.util.TimeStamp;

/**
 * An AccessLogLine contains data representing one HTTP-request that might be 
 * logged in the server's access log file. The amount of data depends on the 
 * log file format: WUM currently supports common, extended, cookie and IIS 
 * log file formats. Methods using instances of AccessLogLine may have to 
 * check whether a certain instance of AccessLogLine is valid or not. Each 
 * AccessLogLine contains an AccessLogID that points to the AccessLog from 
 * which the line is imported.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class AccessLogLine implements Serializable {

  // ########## attributes ##########
 
  private String Host = null;
  private String RFC931 = null;
  private String Authuser = null;
  private String TimeStamp = null;
  private String Request = null;
  private String Status = null;
  private String TransferVolume = null;
  private String Referrer = null;
  private String UserAgent = null;
  private String Cookie = null;
  
  private String Method = null;
  private String ToPage = null;
  private String Protocol = null; 
  private String FromPage = null;
  private long AccessLogID = 0L;
  
  private transient boolean ValidAccessLogLine = true;
      
/**
 * constructs an empty valid AccessLog
 */  

  public AccessLogLine() {
  
    Host = "";   RFC931 = "";   Authuser = "";   TimeStamp = "";   
    Request = "";   Status = "";   TransferVolume = "";   
    Referrer = "";   UserAgent = "-";   Cookie = "-";  
    Method = "";   ToPage = "-";   Protocol = "";   FromPage = "-";    
    AccessLogID = 0L;   ValidAccessLogLine = true;
    
  }  
  
  // ########## mutator methods ##########   
  
  public void setHost(String pHost) 
    { Host = pHost; }
  public void setRFC931(String pRFC931) 
    { RFC931 = pRFC931; }
  public void setAuthuser(String pAuthuser) 
    { Authuser = pAuthuser; }
  public void setTimeStamp(String pTimeStamp) 
    { TimeStamp = pTimeStamp; }
  public void setRequest(String pRequest)  
    { Request = pRequest; }
  public void setStatus(String pStatus) 
    { Status = pStatus; }
  public void setTransferVolume(String pTransferVolume) 
    { TransferVolume = pTransferVolume; }
  public void setReferrer(String pReferrer) 
    { Referrer = pReferrer; }
  public void setUserAgent(String pUserAgent) 
    { UserAgent = pUserAgent; }
  public void setCookie(String pCookie) 
    { Cookie = pCookie; }  
  public void setMethod(String pMethod) 
    { Method = pMethod; }
  public void setToPage(String pToPage) 
    { ToPage = pToPage; }
  public void setProtocol(String pProtocol) 
    { Protocol = pProtocol; }
  public void setFromPage(String pFromPage) 
    { FromPage = pFromPage; }
  public void setAccessLogID(long pAccessLogID) 
    { AccessLogID = pAccessLogID; }
  public void setValid(boolean pValidAccessLogLine) 
   { ValidAccessLogLine = pValidAccessLogLine; }
    
  // ########## accessor methods ##########
  
  public String getHost() { return Host; }
  public String getRFC931() { return RFC931; }
  public String getAuthuser() { return Authuser; }
  public String getTimeStamp() { return TimeStamp; }
  public String getRequest() { return Request; }
  public String getStatus() { return Status; }
  public String getTransferVolume() { return TransferVolume; }
  public String getReferrer() { return Referrer; }
  public String getUserAgent() { return UserAgent; }
  public String getCookie() { return Cookie; }  
  public String getMethod() { return Method; }
  public String getToPage() { return ToPage; }
  public String getProtocol() { return Protocol; }
  public String getFromPage() { return FromPage; }
  public long getAccessLogID() { return AccessLogID; }
  public boolean isValid() { return ValidAccessLogLine; }
    
  // ########## standard methods ##########
  
  public String toString() { 
  
    return( "AccessLogLine: " + Host + " " + RFC931 + " " + Authuser + 
      " " + TimeStamp + " " + FromPage + " " + ToPage + " " + Status + 
      " " + AccessLogID );
      
  }  // toString()
  
  /**
   * calls private import method according to pLogFileFormat
   * @param pLogFileFormat log file format specified using constants of 
   * AccessLog
   * @param pAccessLogLine original line from an access log file 
   * @param pAccessLogID ID of AccessLog that contains pAccessLogLine
   * @see wum.objects.AccessLog
   */
  
  public void fromAccessLogLine(int pLogFileFormat, String pAccessLogLine, 
    long pAccessLogID) {

    this.reset();    
    switch (pLogFileFormat) {
      case AccessLog.COMMON_LOG_FILE_FORMAT: {
        this.fromCommonExtendedLogLine(pLogFileFormat, pAccessLogLine, 
          pAccessLogID); break;
      }
      case AccessLog.EXTENDED_LOG_FILE_FORMAT: {
        this.fromCommonExtendedLogLine(pLogFileFormat, pAccessLogLine, 
          pAccessLogID); break;
      }
      case AccessLog.COOKIE_LOG_FILE_FORMAT: {
        this.fromCommonExtendedLogLine(pLogFileFormat, pAccessLogLine, 
          pAccessLogID); break;
      }
      case AccessLog.WUMPREP_LOG_FILE_FORMAT: {
        this.fromCommonExtendedLogLine(pLogFileFormat, pAccessLogLine, 
          pAccessLogID); break;
      }
    }

  }  // fromAccessLogLine()
  
  /**
   * sets the attributes an an AccessLogLine by decomposing the given line from
   * the HTTP-server's access log file, if an syntax error is detected the 
   * AccessLogLine will be labeled 'not valid' and all attributes will be 
   * reset, calling class have to test on validity before using AccessLogLine,
   * the line's timestamp is converted into an sortable format 
   * (YYYY/MM/DD/HH:MM:SS) without any use of the time zone information 
   * supplied in the log line @param pLogFileFormat log file format specified 
   * using constants of AccessLog
   * @param pAccessLogLine original line from an access log file 
   * @param pAccessLogID ID of AccessLog that contains pAccessLogLine
   * @see wum.objects.AccessLog
   * @see kwinkler.util.TimeStamp
   */
    
  private void fromCommonExtendedLogLine(int pLogFileFormat, 
    String pAccessLogLine, long pAccessLogID) {
    
    String dummy = null;    
    Itemizer itemizer = new Itemizer(pAccessLogLine);
    try {   
      Host = itemizer.getNextItem(); 
      RFC931 = itemizer.getNextItem(); 
      Authuser = itemizer.getNextItem(); 
      TimeStamp = ( itemizer.getNextItem() ).substring(1);
      dummy = itemizer.getNextItem(); 
      Request = Itemizer.itemToString( itemizer.getNextItem() );
      Status = itemizer.getNextItem();
      TransferVolume = itemizer.getNextItem();
      if ( (pLogFileFormat == AccessLog.EXTENDED_LOG_FILE_FORMAT) ||
        (pLogFileFormat == AccessLog.COOKIE_LOG_FILE_FORMAT) ) {
        Referrer = Itemizer.itemToString( itemizer.getNextItem() );
        FromPage = Referrer;
        UserAgent = Itemizer.itemToString( itemizer.getNextItem() );
        if (pLogFileFormat == AccessLog.COOKIE_LOG_FILE_FORMAT)
          Cookie = Itemizer.itemToString( itemizer.getNextItem() );
      }      
      TimeStamp = org.hypknowsys.misc.util.TimeStamp
      .LogFileTimeStamp2TimeStamp(TimeStamp);      
    }
    catch (NoSuchElementException e) { this.handleError(pAccessLogLine); }
    catch (NumberFormatException e) { this.handleError(pAccessLogLine); }
    catch (StringIndexOutOfBoundsException e) { 
      this.handleError(pAccessLogLine); } 
    
    itemizer = new Itemizer(Request);
    try {  
      Method = itemizer.getNextItem();
      if ( itemizer.hasMoreItems() )
        ToPage = itemizer.getNextItem();
      else
        ToPage = "-";
      if ( itemizer.hasMoreItems() )
        Protocol = itemizer.getNextItem();
      else
        Protocol = "-";
    }
    catch (NoSuchElementException e) { this.handleError(pAccessLogLine); }
    catch (NumberFormatException e) { this.handleError(pAccessLogLine); }
    catch (StringIndexOutOfBoundsException e) { 
      this.handleError(pAccessLogLine); } 

    if (UserAgent.length() == 0) UserAgent = "-";
    if (Cookie.length() == 0) Cookie = "-";

    AccessLogID = pAccessLogID;   
  
  }  // fromCommonExtendedLogLine()
  
  /**
   * removed from import dialog due to lack of standardized IIS format
   *
   * sets the attributes an an AccessLogLine by decomposing the given line from
   * the HTTP-server's access log file, if an syntax error is detected the 
   * AccessLogLine will be labeled 'not valid' and all attributes will be 
   * reset, calling class have to test on validity before using AccessLogLine,
   *  the line's timestamp is converted into an sortable format 
   * (YYYY/MM/DD/HH:MM:SS) without any use of the time zone information 
   * supplied in the log line @param pLogFileFormat log file format specified 
   * using constants of AccessLog
   * @param pAccessLogLine original line from an access log file 
   * @param pAccessLogID ID of AccessLog that contains pAccessLogLine
   * @see wum.objects.AccessLog
   * @see kwinkler.util.TimeStamp
   */
    
  private void fromIISLogLine(int pLogFileFormat, String pAccessLogLine, 
    long pAccessLogID) {
    
    String dummy = null;  
    String dateBuffer = null;
    String timeBuffer = null;
    Itemizer itemizer = new Itemizer(pAccessLogLine);
    try {   
      Host = this.removeComma( itemizer.getNextItem() ); 
      RFC931 = "-"; 
      Authuser = this.removeComma( itemizer.getNextItem() );
      dateBuffer = this.removeComma( itemizer.getNextItem() );
      timeBuffer = this.removeComma( itemizer.getNextItem() );
      dummy = itemizer.getNextItem();  // service
      dummy = itemizer.getNextItem();  // computer name
      dummy = itemizer.getNextItem();  // server IP
      dummy = itemizer.getNextItem();  // elapsed time
      dummy = itemizer.getNextItem();  // bytes received
      TransferVolume = this.removeComma( itemizer.getNextItem() );
      Status = this.removeComma( itemizer.getNextItem() );
      dummy = itemizer.getNextItem();  // NT status code
      Method = this.removeComma( itemizer.getNextItem() );
      ToPage = this.removeComma( itemizer.getNextItem() );
      Protocol = "-";
      TimeStamp = org.hypknowsys.misc.util.TimeStamp.DateTime2TimeStamp(
        dateBuffer, timeBuffer);                 
    }
    catch (NoSuchElementException e) { this.handleError(pAccessLogLine); }
    catch (NumberFormatException e) { this.handleError(pAccessLogLine); }
    catch (StringIndexOutOfBoundsException e) { 
      this.handleError(pAccessLogLine); } 
    
    AccessLogID = pAccessLogID;   
  
  }  // fromIISLogLine()  
  
  /**
   * resets all attributes and labels the AccessLogLine as 'not valid'
   * @param pAccessLogLine access log line that doen't confirm to syntax
   */
  
  private void handleError(String pAccessLogLine) {
  
    System.out.println("Error: Access log line doesn't conform to " + 
      "AccessLogLine syntax. \nLine = " + pAccessLogLine );
    this.reset();      
    ValidAccessLogLine = false;

  }  // handleError()
  
  /**
   * resets all attributes and labels the AccessLogLine as 'not valid'
   */
  
  private void reset() {
  
    Host = "";   RFC931 = "";   Authuser = "";   TimeStamp = "";   
    Request = "";   Status = "";   TransferVolume = "";   
    Referrer = "";   UserAgent = "";   Cookie = "";  
    Method = "";   ToPage = "-";   Protocol = "";   FromPage = "-";    
    AccessLogID = 0L;   ValidAccessLogLine = true;
      
  }  // reset()
  
  /**
   * if a comma exists at the end of pString it will be removed
   */
  
  private String removeComma(String pString) {
  
    if ( pString.endsWith(",") )      
      return pString.substring( 0, Math.max(0, pString.length() - 1) );
    else
      return pString;
      
  }  // handleError()
 
  
}  // class AccessLogLine
