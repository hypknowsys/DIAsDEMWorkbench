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

/**
 * A Visitor contains all necessary information about one visitor
 * the current web site: Its ID correspond to the database Visitors. 
 * Attributes: Host, RFC931 and AuthUser refer to the corresponding fields 
 * of one line in an AccessLog. LastToPage is used to determine all fromPages 
 * when importing log file in common log file format. There is no referrer 
 * URL field in these log files, therefore it is assumed that the last 
 * accessed Page (i.g. the LastToPage) is the referrer URL of the current 
 * Page. Accesses counts the total number of accessed
 * Pages within this web site.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class Visitor implements Serializable {

  // ########## attributes ##########
 
  private long ID = 0L;
  private String Host = null;   
  private String RFC931 = null;
  private String AuthUser = null;
  private String LastToPage = null;
  private int Accesses = 0;
  private String UserAgent = null;
  private String Cookie = null;
  
  // ########## constructors ##########
  
/**
 * constructs an empty Visitor
 */   

  public Visitor() {
  
    ID = 0L;   Host = "";   RFC931 = "";   AuthUser = "";
    LastToPage = "";   Accesses = 0; UserAgent = ""; Cookie = "";   
    
  }  
  
/**
 * constructs an Visitor containing the given data
 * @param pHost visitor's host name in a log file
 * @param pRFC931 visitor's RFC931 field in a log file
 * @param pAuthUser visitor's Authuser field in a log file
 * @param pLastToPage visitor's last accessed page in the web site
 * @param pAccesses number of Pages the visitor has accessed in the web site
 */   
    
  public Visitor(String pHost, String pRFC931,
    String pAuthUser, String pLastToPage, int pAccesses) {
  
    ID = 0L;   Host = pHost;   RFC931 = pRFC931;   AuthUser = pAuthUser;
    LastToPage = pLastToPage;   Accesses = pAccesses; UserAgent = "-"; 
    Cookie = "-";
    
  }     
  
/**
 * constructs an Visitor containing the given data
 * @param pHost visitor's host name in a log file
 * @param pRFC931 visitor's RFC931 field in a log file
 * @param pAuthUser visitor's Authuser field in a log file
 * @param pLastToPage visitor's last accessed page in the web site
 * @param pAccesses number of Pages the visitor has accessed in the web site
 */   
    
  public Visitor(String pHost, String pRFC931, String pAuthUser, 
    String pLastToPage, int pAccesses, String pUserAgent, String pCookie) {
  
    ID = 0L;   Host = pHost;   RFC931 = pRFC931;   AuthUser = pAuthUser;
    LastToPage = pLastToPage;   Accesses = pAccesses; UserAgent = pUserAgent; 
    Cookie = pCookie;
    
  }     

  // ########## mutator methods ##########
  
  public void setID(long pID) { ID = pID; }  
  public void setHost(String pHost) { Host = pHost; }
  public void setRFC931(String pRFC931) { RFC931 = pRFC931; }
  public void setAuthUser(String pAuthUser) { AuthUser = pAuthUser; }
  public void setLastToPage(String pLastToPage) { LastToPage = pLastToPage; }
  public void setAccesses(int pAccesses) { Accesses = pAccesses; }
  public void incrementAccesses() { Accesses++; }
  public void decrementAccesses() { Accesses--; }
  public void setUserAgent(String pUserAgent) { UserAgent = pUserAgent; }
  public void setCookie(String pCookie) { Cookie = pCookie; }
  
  // ########## accessor methods ##########
  
  public long getID() { return ID; }
  public String getHost() { return Host; }
  public String getRFC931() { return RFC931; }
  public String getAuthUser() { return AuthUser; }
  public String getLastToPage() { return LastToPage; }
  public int getAccesses() { return Accesses; }
  public String getUserAgent() { return UserAgent; }
  public String getCookie() { return Cookie; }
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    return( "Visitor: ID=" + ID + " " + Host + " " + RFC931 + " " + 
      AuthUser + " Accesses=" + Accesses + " UserAgent=" + UserAgent + 
      " Cookies=" + Cookie );
      
  }  // toString()

  /**
   * @return String that contains all attribute names of Pages, in
   * comma delimited format: "ID", "Url", "Accesses", ...
   */

  public String getExportTxtHeader() {

    return "ID;HOST;RFC931;AUTHUSER;ACCESSES;USER_AGENT;COOKIE";

  }

  /**
   * @return String that contains all attributes of Pages, in
   * comma delimited format: "1000", "www.abc.net", "-", ...
   */

  public String getExportTxtString() {

    return this.getID() + ";\"" + this.getHost() +
      "\";\"" + this.getRFC931() + "\";\"" + this.getAuthUser() + 
      "\";" + this.getAccesses()+ ";\"" + this.getUserAgent() + "\";\"" +
      this.getCookie() + "\"";

  }
  
}  // class Visitor
