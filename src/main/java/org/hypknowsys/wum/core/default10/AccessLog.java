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
 * An AccessLog represents all necessary information about an
 * HTTP-server access log file imported into a WUM mining base. Each
 * AccessLog has a unique ID and a Location in the local file
 * system. Type refers to access log file type: Currently common, 
 * extented, cookie and IIS log file formats are supported. Other attributes:
 * Timestamp of both the first and the last logged request 
 * (FirstTimeStamp, LastTimeStamp), log file size in bytes (Size)
 * number of lines contained in the access log file (LinesTotal),
 * number of lines not imported into a WUM WebSite due to 
 * DataCleaning or syntax errors (LinesSkipped) and number of lines
 * successfully imported into a WUM WebSite (LinesImported).
 * 
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class AccessLog implements Serializable {

  // ########## attributes and constants ##########
 
  private long ID = 0L;
  private String Location = null;
  private int Type = 0;
  private String FirstTimeStamp = null;
  private String LastTimeStamp = null;
  private long Size = 0L;
  private long LinesTotal = 0L;
  private long LinesSkipped = 0L;
  private long LinesImported = 0L;
  
  public final static int UNKNOWN_LOG_FILE_FORMAT = 0;
  public final static int COMMON_LOG_FILE_FORMAT = 1;
  public final static int EXTENDED_LOG_FILE_FORMAT = 2;  
  public final static int COOKIE_LOG_FILE_FORMAT = 3;  
  public final static int WUMPREP_LOG_FILE_FORMAT = 4;
  
  // ########## constructors ###########
  
/**
 * constructs an empty AccessLog
 */  
  
  public AccessLog() {
  
    ID = 0L;   Location = "";   Type = UNKNOWN_LOG_FILE_FORMAT;            
    FirstTimeStamp = "";   LastTimeStamp = "";    
    Size = 0L;   
    LinesTotal = 0L;   LinesSkipped = 0L;   LinesImported = 0L;        
    
  }  
  
/**
 * constructs an empty AccessLog that contains the file's location in
 * local file system and the type of the imported access log file
 */  
    
  public AccessLog(String pLocation, int pType) {
    
    ID = 0L;   Location = pLocation;   Type = pType;            
    FirstTimeStamp = "";   LastTimeStamp = "";
    Size = 0L;   LinesTotal = 0L;   LinesSkipped = 0L;   LinesImported = 0L; 
    
  } 
  
  // ########## mutator methods ##########
  
  public void setID(long pID) 
    { ID = pID; }
  public void setLocation(String pLocation) 
    { Location = pLocation; }
  public void setType(int pType) 
    { Type = pType; }
  public void setFirstTimeStamp(String pFirstTimeStamp) 
    { FirstTimeStamp = pFirstTimeStamp; }
  public void setLastTimeStamp(String pLastTimeStamp) 
    {  LastTimeStamp = pLastTimeStamp; }
  public void setSize(long pSize) 
    { Size = pSize; }
  public void setLinesTotal(long pLinesTotal) 
    { LinesTotal = pLinesTotal; }
  public void setLinesSkipped(long pLinesSkipped) 
    { LinesSkipped = pLinesSkipped; }
  public void setLinesImported(long pLinesImported) 
    { LinesImported = pLinesImported; }
    
  // ########## accessor methods ##########
  
  public long getID() 
    { return ID; }
  public String getLocation() 
    { return Location; }
  public int getType() 
    { return Type; }
  public String getFirstTimeStamp() 
    { return FirstTimeStamp; }
  public String getLastTimeStamp() 
    { return LastTimeStamp; }
  public long getSize() 
    { return Size; }
  public long getLinesTotal() 
    { return LinesTotal; }
  public long getLinesSkipped() 
    { return LinesSkipped; }
  public long getLinesImported() 
    { return LinesImported; }
  
  // ########## standard methods ##########
  
  public String toString() { 
  
    return( "AccessLog: ID=" + ID + " " + Location + " " + Type + " FTS=" + 
      FirstTimeStamp + " LTS=" + LastTimeStamp + " S=" + Size + " LT=" + 
      LinesTotal + " LS=" + LinesSkipped + " LI=" + LinesImported );
      
  }  // toString()
  
}  // class AccessLog
