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

package org.hypknowsys.diasdem.tasks.miscellaneous.searchTheWebForHtmlFiles;

import org.hypknowsys.misc.util.TimeStamp;
import com.google.soap.search.GoogleSearch;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Data Object for all supported Google Search parameter.
 * @version 2.1.2.0, 13 May 2004
 * @author Ingo Kampe
 */

public class SearchParams {
  
  private GoogleSearch gSearch = null;
  private String pGoogleKey = null;
  private String pGoogleSearchString = null;
  private int pGoogleSearchStartResult = 0;
  private int pGoogleSearchMaxResults = 10;
  private Date pGoogleSearchDateStart = null;
  private Date pGoogleSearchDateEnd = null;
  private String[] pGoogleSearchSites = null;
  private String[] pGoogleSearchFiletypes = null;
  private String pGoogleSearchLanguage = null;
  private String pGoogleSearchRestrict = null;
  private String pGoogleProxyHost = null;
  private String pGoogleProxyUser = null;
  private String pgoogleProxyPass = null;
  
  private transient StringBuffer TmpStringBuffer = null;
  
  SearchParams() {
  }
  
  /**
   * This method contructs the Google Search Object.
   * @return GoogleSearch search Object to perform the Search
   */ 
  GoogleSearch getGoogleSearch() {
    boolean incomplete = false;
    
    if (gSearch == null) {
      gSearch = new GoogleSearch();
    }
    // set google search key (you get one with a google
    // account at http://www.google.de/accounts)
    if (pGoogleKey != null) {
      gSearch.setKey(pGoogleKey);
    } else {
      incomplete = true;
    }
    
    // set special language restrictions
    if (pGoogleSearchLanguage != null) {
      gSearch.setLanguageRestricts(pGoogleSearchLanguage);
    }
    
    // set all params encoded in the google search string
    StringBuffer query = new StringBuffer("");
    if ((pGoogleSearchString != null) && (pGoogleSearchString.length() >0)) {
      query.append(pGoogleSearchString);

      if ((pGoogleSearchSites != null) && (pGoogleSearchSites.length >0)) {
        for (int i = 0; i < pGoogleSearchSites.length; i++) {
          if (pGoogleSearchSites[i].length() >0) {
            query.append(" site:" + pGoogleSearchSites[i]);
          }
        }
      }
      if ((pGoogleSearchFiletypes != null) && (pGoogleSearchFiletypes.length >0)) {
        for (int i = 0; i < pGoogleSearchFiletypes.length; i++) {
          if (i>0) {
            if (pGoogleSearchFiletypes[i].length() > 0) {
              query.append(" OR filetype:" + pGoogleSearchFiletypes[i]);
            }
          } else {
            if (pGoogleSearchFiletypes[i].length() > 0) {
              query.append(" filetype:" + pGoogleSearchFiletypes[i]);
            }
          }
        }

      }
      if (pGoogleSearchDateStart != null) {
        if (pGoogleSearchDateEnd != null) {
          query.append(" daterange:" + toJulianDate(pGoogleSearchDateStart)
                               + "-" + toJulianDate(pGoogleSearchDateEnd));
        } else {
          query.append(" daterange:" + toJulianDate(pGoogleSearchDateStart)
                               + "-" + toJulianDate(new Date()));          
        }
      }      
    } else {
      incomplete = true;
    }

    //System.out.println("Query: " + query);
    gSearch.setQueryString(query.toString());

    // restrict settings for topic or specific countries
    if ((pGoogleSearchRestrict != null) && (pGoogleSearchRestrict.length() > 0)) {
      gSearch.setRestrict(pGoogleSearchRestrict);
    }
    
    // all proxy settings
    if ((pGoogleProxyHost != null) && (pGoogleProxyHost.length() > 0)) {
      gSearch.setProxyHost(pGoogleProxyHost);
      if ((pGoogleProxyUser != null) && (pGoogleProxyUser.length() > 0)) {
        gSearch.setProxyUserName(pGoogleProxyUser);
      }
      if ((pgoogleProxyPass != null) && (pgoogleProxyPass.length() > 0)) {
        gSearch.setProxyPassword(pgoogleProxyPass);
      }
    }
    
    gSearch.setStartResult(pGoogleSearchStartResult);
    
    gSearch.setMaxResults(pGoogleSearchMaxResults);
    
    /* not editable default values */
    // don't remove eventually doubled sites
    gSearch.setFilter(false);
    // prevent adult stuff
    gSearch.setSafeSearch(true);
    
    if (incomplete) {
      throw new VerifyError("Minimum Search Params are not completed");  
    }

    return gSearch;
  }

  // bean like set/get access methods for all parameters

  void setGoogleKey(String value) {
    pGoogleKey = value;
  }  
  String getGoogleKey() {
    return pGoogleKey;
  }

  void setGoogleSearchString(String value) {
    pGoogleSearchString = value;
  }  
  String getGoogleSearchString() {
    return pGoogleSearchString;
  }

  void setGoogleSearchDateStart(Date value) {
    pGoogleSearchDateStart = value;
  }  
  Date getGoogleSearchDateStart() {
    return pGoogleSearchDateStart;
  }
  
  void setGoogleSearchDateEnd(Date value) {
    pGoogleSearchDateEnd = value;
  }  
  Date getGoogleSearchDateEnd() {
    return pGoogleSearchDateEnd;
  }
  
  void setGoogleSearchSites(String[] value) {
    pGoogleSearchSites = value;
  }
  void appendGoogleSearchSite(String value) {
    if (pGoogleSearchSites != null) {
      String[] old = pGoogleSearchSites;
      pGoogleSearchSites = new String[old.length+1];
      for (int i = 0; i < old.length; i++) {
        pGoogleSearchSites[i] = old[i];
      }
      pGoogleSearchSites[old.length] = value;
    } else {
      pGoogleSearchSites = new String[1];
      pGoogleSearchSites[0] = value;
    }
  }
  String[] getGoogleSearchSites() {
    return pGoogleSearchSites;
  }
  String getGoogleSearchSitesString() {
    StringBuffer tmpStr = new StringBuffer("");
    for (int i = 0; i < pGoogleSearchSites.length; i++) {
      if (i == 0) {
        tmpStr.append(pGoogleSearchSites[i]);
      } else {
        tmpStr.append("," + pGoogleSearchSites[i]);
      }
    }
    return tmpStr.toString();
  }

  void setGoogleSearchFiletypes(String[] value) {
    pGoogleSearchFiletypes = value;
  }  
  void appendGoogleSearchFiletype(String value) {
    if (pGoogleSearchFiletypes != null) {
      String[] old = pGoogleSearchFiletypes;
      pGoogleSearchFiletypes = new String[old.length+1];
      for (int i = 0; i < old.length; i++) {
        pGoogleSearchFiletypes[i] = old[i];
      }
      pGoogleSearchFiletypes[old.length] = value;
    } else {
      pGoogleSearchFiletypes = new String[1];
      pGoogleSearchFiletypes[0] = value;
    }
  }
  String[] getGoogleSearchFiletypes() {
    return pGoogleSearchFiletypes;
  }
  String getGoogleSearchFiletypesString() {
    StringBuffer tmpStr = new StringBuffer("");
    for (int i = 0; i < pGoogleSearchFiletypes.length; i++) {
      if (i == 0) {
        tmpStr.append(pGoogleSearchFiletypes[i]);
      } else {
        tmpStr.append("," + pGoogleSearchFiletypes[i]);
      }
    }
    return tmpStr.toString();
  }

  void setGoogleSearchLanguage(String value) {
    pGoogleSearchLanguage = value;
  }  
  String getGoogleSearchLanguage() {
    return pGoogleSearchLanguage;
  }
  
  void setGoogleSearchRestrict(String value) {
    pGoogleSearchRestrict = value;
  }  
  String getGoogleSearchRestrict() {
    return pGoogleSearchRestrict;
  }
  
  void setGoogleProxyHost(String value) {
    pGoogleProxyHost = value;
  }  
  String getGoogleProxyHost() {
    return pGoogleProxyHost;
  }
  
  void setGoogleProxyUser(String value) {
    pGoogleProxyUser = value;
  }  
  String getGoogleProxyUser() {
    return pGoogleProxyUser;
  }
  
  void setGoogleProxyPass(String value) {
    pgoogleProxyPass = value;
  }  
  String getGoogleProxyPass() {
    return pgoogleProxyPass;
  }
  
  void setGoogleSearchStartResults(int pStartResult) {
    pGoogleSearchStartResult = pStartResult;
  }
  int getGoogleSearchStartResults() {
    return pGoogleSearchStartResult;
  }
  
  void setGoogleSearchMaxResults(int max) {
    pGoogleSearchMaxResults = max;
  }
  int getGoogleSearchMaxResults() {
    return pGoogleSearchMaxResults;
  }
  
  public String toString() {
    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("Google Search Param Object\n");
    TmpStringBuffer.append("GoogleSearchString: " + pGoogleSearchString + "\n");
    TmpStringBuffer.append("GoogleKey: " + pGoogleKey + "\n");

    for (int i = 0; i < pGoogleSearchSites.length; i++) {
       TmpStringBuffer.append("GoogleSearchSite"+i+": " + pGoogleSearchSites[i] + "\n");
    }
    for (int i = 0; i < pGoogleSearchFiletypes.length; i++) {
       TmpStringBuffer.append("GoogleSearchFiletype"+i+": " + pGoogleSearchFiletypes[i] + "\n");
    }
    TmpStringBuffer.append("GoogleSearchMaxResults: " + pGoogleSearchMaxResults);
    TmpStringBuffer.append("GoogleSearchLanguage: " + pGoogleSearchLanguage + "\n");
    TmpStringBuffer.append("GoogleSearchRestrict: " + pGoogleSearchRestrict + "\n");
    TmpStringBuffer.append("GoogleSearchDateStart: " + pGoogleSearchDateStart + "\n");
    TmpStringBuffer.append("GoogleSearchDateEnd: " + pGoogleSearchDateEnd + "\n");
    
    TmpStringBuffer.append("GoogleProxyHost: " + pGoogleProxyHost + "\n");
    TmpStringBuffer.append("GoogleProxyUser: " + pGoogleProxyUser + "\n");
    TmpStringBuffer.append("GoogleProxyPass: " + pgoogleProxyPass + "\n");

    return TmpStringBuffer.toString();    
  }
  
  // util methods
  private String toJulianDate(Date date) {
    // place the julian algorithm here or create a common
    // util package for DIAsDEM
    
    // use the existing org.hypknowsys.misc.util.TimeStamp at the moment
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");    
    TimeStamp timestamp = new TimeStamp(dateFormat.format(date) + "/00:00:00"); 
    Long julianDay = new Long(Math.round(timestamp.getJulianDay())); 

    return julianDay.toString();
  }
  
  // test methods
  void setTestValues() {
    // the key does not working in release
    setGoogleKey("+uo8m/lQFHLco7h+w2y9Qd/Gu+JYImt2");
    setGoogleSearchString("");
    appendGoogleSearchSite("");
    appendGoogleSearchFiletype("htm");
    appendGoogleSearchFiletype("html");
    setGoogleSearchLanguage("lang_de");
  }
}
