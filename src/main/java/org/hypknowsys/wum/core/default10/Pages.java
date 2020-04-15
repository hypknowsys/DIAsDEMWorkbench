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
  * Pages is a small database to store instances of Page. An Page is stored 
  * and retrieved using its PageID or its URL as primary keys. Pages provides 
  * new Pages with a unique ID. <p>
  *
  * Modified by kwinkler 10/1999: class was completely changed
  *
  * @version 0.9.0, 20 May 2004
  * @author Karsten Winkler
  */

public class Pages implements Serializable {

  // ########## attributes ##########
  
  private long NextID = 0L;
  private TreeMap TableOfPages = null;
  private TreeMap IndexOfUrls = null;

  private transient Iterator IDIterator = null;
  private transient Iterator UrlIterator = null;
  private transient Object TmpObject = null;
  private transient Long CurrentID = null;

  // ########## constructors ##########
  
/**
 * constructs an emtpy database Pages, first ID is 1
 */
  
  public Pages() {

    NextID = 1L;
    TableOfPages = null;  IndexOfUrls = null;
    IDIterator = null;   UrlIterator = null;    
    TmpObject = null;   CurrentID = null;

  }

/**
 * constructs an emtpy database Pages, first ID is specified
 * @param pNextID ID of the first added Page
 */

  public Pages(long pNextID) {

    NextID = pNextID;
    TableOfPages = null; IndexOfUrls = null;
    IDIterator = null;   UrlIterator = null; 
    TmpObject = null;   CurrentID = null;

  }

/**
  * Creates relational database, must be called immediately after 
  * constructing instance reason: previously umsed persistence engine 
  */

  public void createDatabase() {

    TableOfPages = new TreeMap();  
    IndexOfUrls = new TreeMap();

  }

  // ########## mutator methods ##########

  private void setNextID(long pNextID) { NextID = pNextID; }  
  
  // ########## standard methods ##########

  public String toString() {

    return( "Pages: NID=" + NextID + " #=" + TableOfPages.size() );

  }

  /**
   * dumps all contained Pages ordered by ID into the given PrintStream
   * @param pPrintStream PrintStream to dump 
   * @see java.io.PrintStream
   */

  public void dumpID(PrintStream pPrintStream) {

    pPrintStream.println(this);
    Page oPage = this.getFirstByID();
    while (oPage != null) {
      pPrintStream.println(oPage);
      oPage = this.getNextByID();
    }

  }  // dumpID()
  
  /**
   * dumps all contained Pages ordered by URL into the given PrintStream
   * @param pPrintStream PrintStream to dump 
   * @see java.io.PrintStream
   */

  public void dumpUrl(PrintStream pPrintStream) {

    pPrintStream.println(this);
    Page oPage = this.getFirstByUrl();
    while (oPage != null) {
      pPrintStream.println(oPage);
      oPage = this.getNextByUrl();
    }

  }  // dumpUrl()
      
  /**
   * adds the given Page to the database, sets a unique PageID 
   * in the given Page
   * @param pPage Page to be added to Pages
   * @return PageID of the added Page
   */

  public long add(Page pPage) {

    pPage.setID( this.getNextID() );
    CurrentID = new Long( pPage.getID() );
    TmpObject = TableOfPages.put(CurrentID, pPage);
    TmpObject = IndexOfUrls.put(pPage.getUrl(), CurrentID);
    
    return CurrentID.longValue();

  }  // add()
  
  /**
   * replaces a Page that is allready contained in Pages with the same 
   * AccessLogID with the given Page in the database, 
   * PageID ans URL must not be changed between get() and replace()
   * @param pPage Page to be added to Pages
   */

  public void replace(Page pPage) {

    CurrentID = new Long( pPage.getID() );
    TmpObject = TableOfPages.put(CurrentID, pPage);

  }  // replace()
  
  /**
   * @param pID PageID of the Page to be returned
   * @return Page associated with pID or null if pID doesn't exist
   */

  public Page get(long pID) {
  
    TmpObject = TableOfPages.get( new Long(pID) );    
    if (TmpObject != null)
      return (Page)TmpObject;
    else 
      return null;

  }  // get()
  
  /**
   * @param pUrl URL of the Page to be returned
   * @return Page associated with pUrl or null if pUrl doesn't exist
   */

  public Page get(String pUrl) {
  
    CurrentID = getID(pUrl);    
    if (CurrentID != null)
      return get( CurrentID.longValue() );
    else
      return null;

  }  // get()  
  
  /**
   * @param pUrl URL of the Page whose ID is to be returned
   * @return ID of the Page associated with pUrl or null if pUrl doesn't exist
   */

  public Long getID(String pUrl) {
    
    CurrentID = (Long)( IndexOfUrls.get(pUrl) );    
    if (CurrentID != null)
      return CurrentID;      
    else 
      return null;

  }  // getID() 
  
  /**
   * @return first Page of the database ordered by ID or null if the
   * database is empty, must be called to start a new traversal
   */

  public Page getFirstByID() {
  
    if (TableOfPages == null) return null;
  
    IDIterator = TableOfPages.values().iterator();
    if ( IDIterator.hasNext() )      
      return (Page)IDIterator.next();
    else
      return null;

  }  // getFirstByID() 
  
  /**
   * @return next Page of the database ordered by ID or null if the
   * database is empty, subsequent calls traverse all Pages
   */

  public Page getNextByID() {
  
    if (IDIterator == null) return getFirstByID();
    
    if ( IDIterator.hasNext() ) 
      return (Page)IDIterator.next();  
    else
      return null;

  }  // getNextByID() 
  
  /**
   * @return first Page of the database ordered by URL or null if the
   * database is empty, must be called to start a new traversal
   */

  public Page getFirstByUrl() {
  
    if (IndexOfUrls == null) return null;
  
    UrlIterator = IndexOfUrls.values().iterator();
    if ( UrlIterator.hasNext() )      
      return this.get( ( (Long)UrlIterator.next() ).longValue() );
    else
      return null;

  }  // getFirstByUrl() 
  
  /**
   * @return next Page of the database ordered by Url or null if the
   * database is empty, subsequent calls traverse all Pages
   */

  public Page getNextByUrl() {
  
    if (UrlIterator == null) return getFirstByUrl();
    
    if ( UrlIterator.hasNext() )
      return this.get( ( (Long)UrlIterator.next() ).longValue() );    
    else
      return null;

  }  // getNextByUrl()   
    
  /**
   * deletes the Page associated with pID from the database,
   * @param pID PageID of the Page to be deleted
   */

  public void delete(long pID) {

    TmpObject = TableOfPages.remove( new Long(pID) );
    
    if (TmpObject != null) {
      Page oPage = (Page)TmpObject;    
      TmpObject = IndexOfUrls.remove( oPage.getUrl() );
    }
      
  }  // delete()
  
  /**
   * resets all attributes, database is completely deleted, next ID is 1
   */

  public void reset() {

    NextID = 1L;
    TableOfPages.clear();   IndexOfUrls.clear();
    IDIterator = null;   UrlIterator = null;
    TmpObject = null;   CurrentID = null;

  }  // reset()
  
  /**
   * @return the number of Pages contained in the database
   */

  public int countAll() { return TableOfPages.size(); } 
  
  /**
   * @return the next unique PageID for this database
   */

  private long getNextID() { return NextID++; } 

}  // class Pages
