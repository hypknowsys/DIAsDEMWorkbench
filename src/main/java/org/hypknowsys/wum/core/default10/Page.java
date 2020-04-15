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

/**
 * A Page contains all necessary information about one (HTML) page of
 * the current web site: Its ID correspond to the database Pages. Attributes:
 * URL, Accesses (of this page in the TraversalLog) and Referrals (to other
 * pages by this page). Title, Author, LastUpdate and Type are not used yet.
 * There are graph drawing coordinates associated with this page.
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class Page implements Serializable {

  // ########## attributes ##########
 
  private long ID = 0L;
  private String Url = null;
  private String Title = null;
  private String Author = null;
  private String LastUpdate = null;
  private String Remarks = null;
  private String Concepts = null;
  private int Accesses = 0;
  private int Referrals = 0;  
  private int MaxOccurrence = 0;
  private transient int X = 0;  
  private transient int Y = 0;

  // ########## constructors ##########
  
/**
 * constructs an empty Page; MaxOccurrence = 1
 */   
 
  public Page() {  
  
    ID = 0L;   Url = "";   
    Title = "";   Author = "";   LastUpdate = "";    
    Remarks = "";   Concepts = "";
    Accesses = 0;   Referrals = 0;  MaxOccurrence = 1;  
    X = 0;   Y = 0;
    
  }
  
/**
 * constructs an Page containing the given data; MaxOccurrence = 1
 * @param pUrl the page's URL
 * @param pAccesses the page's number of accesses in TraversalLog
 * @param pReferrals the page's number of referrals to other pages in 
 * TraversalLog
 */   
    
  public Page(String pUrl, int pAccesses, int pReferrals) {

    ID = 0L;  Url = pUrl; 
    Title = "";   Author = "";   LastUpdate = ""; 
    Remarks = "";   Concepts = "";
    Accesses = pAccesses;   Referrals = pReferrals;   
    MaxOccurrence = 1;
    X = 0;   Y = 0;
    
  } 
      
  // ########## mutator methods ########## 
  
  public void setID(long pID) 
    { ID = pID; }
  public void setUrl(String pUrl) 
    { Url = pUrl; }
  public void setTitle(String pTitle) 
    { Title = pTitle; }
  public void setAuthor(String pAuthor) 
    { Author = pAuthor; }
  public void setLastUpdate(String pLastUpdate) 
    { LastUpdate = pLastUpdate; }
  public void setRemarks(String pRemarks) 
    { Remarks = pRemarks; }
  public void setConcepts(String pConcepts) 
    { Concepts = pConcepts; }
  public void setAccesses(int pAccesses) 
    { Accesses = pAccesses; }
  public void incrementAccesses() 
    { Accesses++; }
  public void decrementAccesses() 
    { Accesses--; }
  public void increaseAccesses(int pIncrease) 
    { Accesses += pIncrease; }  
  public void setReferrals(int pReferrals) 
    { Referrals = pReferrals; }
  public void incrementReferrals() 
    { Referrals++; }
  public void decrementReferrals() 
    { Referrals--; }
  public void setMaxOccurrence(int pMaxOccurrence) 
    { MaxOccurrence = pMaxOccurrence; }
  public void setX(int pX) 
    { X = pX; }
  public void setY(int pY) 
    { Y = pY; }
  
  // ########## accessor methods ##########

  public long getID() { return ID; }
  public String getUrl() { return Url; }
  public String getTitle() { return Title; }
  public String getAuthor() { return Author; }
  public String getLastUpdate() { return LastUpdate; }
  public String getRemarks() { return Remarks; }
  public String getConcepts() { return Concepts; }
  public int getAccesses() { return Accesses; }
  public int getReferrals() { return Referrals; }  
  public int getMaxOccurrence() { return MaxOccurrence; }  
  public int getX() { return X; }
  public int getY() { return Y; }
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    return( "Page: ID=" + ID + " " + Url + " Accesses=" + Accesses );
    
  }  // toString()

  /**
   * @return String that contains all attribute names of Pages, in
   * comma delimited format: "ID", "Url", "Accesses", ...
   */

  public String getExportTxtHeader() {

    return "ID;URL;ACCESSES;REFERRALS;TITLE;AUTHOR;LAST_UPDATE;" +
      "REMARKS;CONCEPTS;MAX_OCCURRENCE";

  } 

  /**
   * @return String that contains all attributes of Pages, in
   * comma delimited format: "1000", "h.html", "33", ...
   */

  public String getExportTxtString() {

    return this.getID() + ";\"" + this.getUrl() +
      "\";" + this.getAccesses() + ";" + this.getReferrals() + 
      ";\"" + this.getTitle() + "\";\"" + this.getAuthor() + 
      "\";\"" + this.getLastUpdate() + "\";\"" + this.getRemarks() + 
      "\";\"" + this.getConcepts() + "\";" + this.getMaxOccurrence();

  } // 
  
}  // class Page
