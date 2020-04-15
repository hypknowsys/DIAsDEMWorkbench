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
 * Each MiningBaseInfo contains meta information about one mining base: Its 
 * unique ID is used as a primary key in the WUM repository WebSIteInfos. 
 * Attribues:  Name, Server, LogFileDirectory (local directory of log files), 
 * MiningBaseDirectory (local directory of corresponding WUM files). 
 * FirstTimeStamp and LastTimeStamp refer to all imported log files. <p>
 *
 * Modified by kwinkler 7/2000: attribute Remarks added and methods
 * get/setLogFileDirectory and get/setMiningBaseDirectory added
 *
 * @version 0.6.0, 14 Nov 2000
 * @author Karsten Winkler
 */

public class MiningBaseInfo implements Serializable {

  // ########## attributes ##########

  private long ID = 0L;
  private String Name = null;
  private String Server = null;
  private String LogFileDirectory = null;
  private String MiningBaseDirectory = null;
  private String FirstTimeStamp = null;
  private String LastTimeStamp = null;
  private String Remarks = null;

  // ########## constructors ##########

/**
 * constructs an empty MiningBaseInfo
 */  

  public MiningBaseInfo() {

    ID = 0L;   Name = "";   Server = "";
    LogFileDirectory = "";   MiningBaseDirectory = "";
    FirstTimeStamp = "";   LastTimeStamp = "";
    Remarks = "-";

  }

/**
 * constructs a MiningBaseInfo with the given attributes
 * @param pName web site's Name
 * @param pServer web site's server URL
 * @param pLogFileDirectory local directory that contains the 
 * server's log files
 * @param pMiningBaseDirectory local directory that contains the 
 * corresponding WUM files
 * @param pRemarks remarks concerning the mining base
 */  

  public MiningBaseInfo(String pName, String pServer, String pLogFileDirectory,
    String pMiningBaseDirectory, String pRemarks) {

    ID = 0L;   Name = pName;   Server = pServer;
    LogFileDirectory = pLogFileDirectory;   
    MiningBaseDirectory = pMiningBaseDirectory;
    FirstTimeStamp = "";   LastTimeStamp = ""; Remarks = pRemarks;

  }

/**
 * constructs a MiningBaseInfo with the given attributes
 * @param pName web site's Name
 * @param pServer web site's server URL
 * @param pLogFileDirectory local directory that contains the server's 
 * log files
 * @param pMiningBaseDirectory local directory that contains the 
 * corresponding WUM files
 */  

  public MiningBaseInfo(String pName, String pServer, String pLogFileDirectory,
    String pMiningBaseDirectory) {

    ID = 0L;   Name = pName;   Server = pServer;
    LogFileDirectory = pLogFileDirectory;   
    MiningBaseDirectory = pMiningBaseDirectory;
    FirstTimeStamp = "";   LastTimeStamp = ""; Remarks = "-";

  }

  // ########## mutator methods ##########

  public void setID(long pID) 
    { ID = pID; }
  public void setName(String pName) 
    { Name = pName; }
  public void setServer(String pServer) 
    { Server = pServer; }
  public void setLogFileDirectory(String pLogFileDirectory) { 
    LogFileDirectory = pLogFileDirectory; }
  public void setMiningBaseDirectory(String pMiningBaseDirectory) { 
    MiningBaseDirectory = pMiningBaseDirectory; }
  public void setFirstTimeStamp(String pFirstTimeStamp) { 
   FirstTimeStamp = pFirstTimeStamp; }
  public void setLastTimeStamp(String pLastTimeStamp) { 
   LastTimeStamp = pLastTimeStamp; }
  public void setRemarks(String pRemarks) { 
   Remarks = pRemarks; }

   // ########## accessor methods ##########

  public long getID() 
    { return ID; }
  public String getName() 
    { return Name; }
  public String getServer() 
    { return Server; }
  public String getLogFileDirectory() 
    { return LogFileDirectory; }
  public String getMiningBaseDirectory() 
    { return MiningBaseDirectory; }
  public String getFirstTimeStamp() 
    { return FirstTimeStamp; }
  public String getLastTimeStamp() 
    { return LastTimeStamp; }
  public String getRemarks() 
    { return Remarks; }
    
  // ########## standard methods ##########
  
  public String toString() { 
  
    return( "MiningBaseInfo: ID=" + ID + ", " + Name + ", " + Server + ", " + 
      LogFileDirectory + ", " + MiningBaseDirectory );
      
  }  // toString()
     
}  // class MiningBaseInfo
