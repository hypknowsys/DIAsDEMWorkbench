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

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
// String TimeStamp = "YYYY/MM/DD/HH:MM:SS" 
// String Time = "DD/HH:MM:SS"
//
// This Class only provides correct algorithms for 
// TimeStamps >= "1582/10/15/00:00:00". The TimeStamp 
// must be a valid date, because it will not be checked.  

public class TimeStamp {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private int Day = 0;
  private int Month = 0;
  private int Year = 0;
  private int DayOfWeek = 0;
  private int Hours = 0;
  private int Minutes = 0;
  private int Seconds = 0;
  private double GregorianDay = 0.0;
  private double JulianDay = 0.0;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public final static int UNKNOWN = 0;

  public final static int MON = 1;
  public final static int TUE = 2;
  public final static int WED = 3;
  public final static int THU = 4;
  public final static int FRI = 5;
  public final static int SAT = 6;
  public final static int SUN = 7;

  public final static String[] DAYS_OF_WEEK = { "unknown", "Monday", 
    "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
  public final static String[] ABBR_DAYS_OF_WEEK = { "unknown", "Mon", 
    "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

  public final static int JAN = 1;
  public final static int FEB = 2;
  public final static int MAR = 3;
  public final static int APR = 4;
  public final static int MAY = 5;
  public final static int JUN = 6;
  public final static int JUL = 7;
  public final static int AUG = 8;
  public final static int SEP = 9;
  public final static int OCT = 10;
  public final static int NOV = 11;
  public final static int DEC = 12;

  public final static String[] MONTHS = { "unknown", "January", "February", 
    "March", "April", "May", "June", "July", "August", "September",
    "October", "November", "December" };
  public final static String[] ABBR_MONTHS = { "unknown", "Jan", "Feb", 
    "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

  private final static String[] LOGFILE_MONTHS = {"Jan01", "Feb02", "Mar03", 
    "Apr04", "May05", "Jun06", "Jul07", "Aug08", "Sep09", "Oct10", "Nov11", 
    "Dec12"};

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public TimeStamp(String pTimeStamp) {

    Day = Integer.parseInt( pTimeStamp.substring(8, 10) );
    Month = Integer.parseInt( pTimeStamp.substring(5, 7) );
    Year = Integer.parseInt( pTimeStamp.substring(0, 4) );
    Hours = Integer.parseInt( pTimeStamp.substring(11, 13) );
    Minutes = Integer.parseInt( pTimeStamp.substring(14, 16) );
    Seconds = Integer.parseInt( pTimeStamp.substring(17, 19) );
    GregorianDay = TimeStamp.TimeStamp2GregorianDay(pTimeStamp);
    JulianDay = TimeStamp.JulianDay(GregorianDay);
    DayOfWeek = TimeStamp.DayOfWeek(JulianDay);

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public int getDay() { 
    return Day; }
  public int getMonth() { 
    return Month; }
  public int getYear() { 
    return Year; }
  public int getDayOfWeek() { 
    return DayOfWeek; }
  public int getHours() { 
    return Hours; }
  public int getMinutes() { 
    return Minutes; }
  public int getSeconds() { 
    return Seconds; }
  public double getGregorianDay() { 
    return GregorianDay; }
  public double getJulianDay() { 
    return JulianDay; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    return "D=" + Day +
      ", M=" + Month + 
      ", Y=" + Year +
      ", DOW=" + DAYS_OF_WEEK[DayOfWeek] + 
      ", H=" + Hours + 
      ", M=" + Minutes +
      ", S=" + Seconds +
      ", GD=" + GregorianDay +
      ", JD=" + JulianDay;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public static String LogFileTimeStamp2TimeStamp(String pLogFileTimeStamp) {
  
    // input: 
    // time zone is currently ignored
  
    int month = 0;    
    StringBuffer timeStamp = new StringBuffer(20); 
                       
    timeStamp.append( pLogFileTimeStamp.substring(7,11) ).append("/");  // year
    while (month < 12) {
      if ( pLogFileTimeStamp.substring(3,6).equals(
        LOGFILE_MONTHS[month].substring(0,3)) ) {
        // month
        timeStamp.append( LOGFILE_MONTHS[month].substring(3) ).append("/");  
        month = 11; // stop now
      }
      month++;
    } 
    timeStamp.append( pLogFileTimeStamp.substring(0,2) ).append("/");  // day
    timeStamp.append( pLogFileTimeStamp.substring(12) );  // time
      
    return timeStamp.toString();  
    
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String DateTime2TimeStamp(String pDate, String pTime) {
  
    // not y2k compliant
    // input: pDate = "DD.MM.YY", pTime = "HH:MM:SS"
    // time zone is currently ignored

    String year = pDate.substring(6);
    StringBuffer timeStamp = new StringBuffer(20); 
                       
    if ( ( year.startsWith("9") ) || ( year.startsWith("8") ) )              
      timeStamp.append("19");
    else
      timeStamp.append("20");
    timeStamp.append(year).append("/");
    timeStamp.append( pDate.substring(3, 5) ).append("/");
    timeStamp.append( pDate.substring(0, 2) ).append("/");
    timeStamp.append(pTime);

    return timeStamp.toString();  
    
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String TimeStampDifference(String pFirstTimeStamp, 
    String pSecondTimeStamp) {
                                           
    // TimeStamp = "YYYY/MM/DD/HH:MM:SS", TimeStamp must be valid Date 
    // time zone is currently ignored: both must be the same;
    // result = "DD/HH:MM:SS"
  
    int days = 0, hours = 0, minutes = 0, seconds = 0;
    double difference = 0.0;
    StringBuffer timeStampDifference = new StringBuffer();
    
    difference = Math.abs( 
      JulianDay( TimeStamp2GregorianDay(pFirstTimeStamp) ) -
      JulianDay( TimeStamp2GregorianDay(pSecondTimeStamp) ) );
    
    // This does not work properly:
    // 1998/12/31/00:00:00 - 1998/12/30/00:00:00 = 0/23:59:60
    // days = trunc(difference); difference -= days;
    // difference *= 24.0; hours = trunc(difference); 
    // difference -= hours;
    // difference *= 60.0 ; minutes = trunc(difference); 
    // difference -= minutes;
    // difference *= 60.0; seconds = trunc(difference + 5E-3); 
    // difference -= seconds;
    
    days = trunc(difference + 5E-6); difference -= days;
    difference *= 24.0; hours = trunc(difference + 5E-5); 
    difference -= hours;
    difference *= 60.0 ; minutes = trunc(difference + 5E-4); 
    difference -= minutes;
    difference *= 60.0; seconds = trunc(difference + 5E-3); 
    difference -= seconds;
    
    timeStampDifference.append(days).append("/");
    if (hours < 10) timeStampDifference.append("0");
    timeStampDifference.append(hours).append(":");
    if (minutes < 10) timeStampDifference.append("0");
    timeStampDifference.append(minutes).append(":");
    if (seconds < 10) timeStampDifference.append("0");
    timeStampDifference.append(seconds);
                         
    return timeStampDifference.toString();                     
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String TimeStampPlusTime(String pTimeStamp, String pTime) {
                                           
    // TimeStamp = 'YYYY/MM/DD/HH:MM:SS', TimeStamp must be valid Date 
    // time zone is currently ignored: both must be the same;
    // Time = 'DD/HH:MM:SS'; Time is added to TimeStamp
    // result: new valid TimeStamp
  
    double vNewJulianDay = 0.0;
    
    vNewJulianDay = 
      JulianDay( TimeStamp2GregorianDay(pTimeStamp) ) + Time2Days(pTime);
                         
    return GregorianDay2TimeStamp( JulianDay2GregorianDay(vNewJulianDay) );
  
  }  // TimeStampPlusTime()  
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static double TimeStamp2GregorianDay(String pTimeStamp) {
  
    // TimeStamp = 'YYYY/MM/DD/HH:MM:SS'
    // Result = JJJJMMDD.TTTT with TTTT = time as a fraction of day
  
    int myInt = 0;
    double gregorianDay = 0.0;
    
    myInt = Integer.parseInt( pTimeStamp.substring(0, 4) );
    gregorianDay = myInt * 10000E0;  // year
    
    myInt = Integer.parseInt( pTimeStamp.substring(5, 7) );
    gregorianDay += myInt * 100E0;  // month
    
    myInt = Integer.parseInt( pTimeStamp.substring(8, 10) );
    gregorianDay += myInt;  // day
    
    myInt = Integer.parseInt( pTimeStamp.substring(11, 13) );
    gregorianDay += myInt / 24E0;  // hours
    
    myInt = Integer.parseInt( pTimeStamp.substring(14, 16) );
    gregorianDay += myInt / 1440E0;  // minutes
    
    myInt = Integer.parseInt( pTimeStamp.substring(17, 19) );
    gregorianDay += myInt / 86400E0;  // seconds
    
    return gregorianDay;
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static double TimeStamp2GregorianDay(TimeStamp pTimeStamp) {
  
    // TimeStamp = instance of class TimeStamp
    // Result = JJJJMMDD.TTTT with TTTT = time as a fraction of day
  
    double gregorianDay = 0.0;
    
    gregorianDay = pTimeStamp.getYear() * 10000E0;  // year
    gregorianDay += pTimeStamp.getMonth() * 100E0;  // month
    gregorianDay += pTimeStamp.getDay();  // day
    
    gregorianDay += pTimeStamp.getHours() / 24E0;  // hours
    gregorianDay += pTimeStamp.getMinutes() / 1440E0;  // minutes
    gregorianDay += pTimeStamp.getSeconds() / 86400E0;  // seconds
    
    return gregorianDay;
  
  }   
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static double Time2Days(String pTime) {
  
    // Time = "DD/HH:MM:SS"
    // Result = DD.TTTT with TTTT = time as a fraction of day
    // pTime must be a String representing a valid Time
  
    int myInt = 0;
    double days = 0.0;
    int slashPosition = pTime.indexOf("/");
    
    myInt = Integer.parseInt( pTime.substring(0, slashPosition) );
    days = myInt;  // days
    
    myInt = Integer.parseInt( pTime.substring(slashPosition + 1,
      slashPosition + 3) );
    days += myInt / 24E0;  // hours
    
    myInt = Integer.parseInt( pTime.substring(slashPosition + 4,
      slashPosition + 6) );
    days += myInt / 1440E0;  // minutes
    
    myInt = Integer.parseInt( pTime.substring(slashPosition + 7) );
    days += myInt / 86400E0;  // seconds
    
    return days;
  
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static boolean isValidTime(String pTime) {
  
    // Time = "DD/HH:MM:SS"
    // Method only performs syntax test!
  
    boolean result = true;
    int myInt = 0;
    int slashPosition = pTime.indexOf("/");
    if (slashPosition < 0) return false;
    
    try {
      // days
      myInt = Integer.parseInt( pTime.substring(0, slashPosition) );

      // hours
      myInt = Integer.parseInt( pTime.substring(slashPosition + 1,
        slashPosition + 3) );
      if ( (myInt < 0) || (myInt > 23) )
        return false;

      // minutes
      myInt = Integer.parseInt( pTime.substring(slashPosition + 4,
        slashPosition + 6) );
      if ( (myInt < 0) || (myInt > 59) )
        return false;

      // seconds
      myInt = Integer.parseInt( pTime.substring(slashPosition + 7) );
      if ( (myInt < 0) || (myInt > 59) )
        return false;
    }
    catch (StringIndexOutOfBoundsException e1) { 
      result = false; 
    }
    catch (NumberFormatException e2) { 
      result = false;
    }
    
    return result;
  
  }   
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static String GregorianDay2TimeStamp(double pGregorianDay) {
  
    int year = 0, month = 0, day = 0, hours = 0, minutes = 0, seconds = 0;
    double dayTime = 0.0, time = 0.0;
    StringBuffer timeStamp = new StringBuffer(20);
  
    year = trunc(pGregorianDay / 10000E0 );
    month = trunc( frac( pGregorianDay / 10000E0 ) * 100E0 );
    dayTime = frac( pGregorianDay / 100E0 ) * 100E0;
//     day = trunc(dayTime);
    day = trunc(dayTime + 5E-6);
//     time = frac(dayTime);
    time = frac(dayTime + 5E-6);
    
    // This does not work properly:
    // 1998/05/01/00:10:00  + 0/00:20:00 = 1998/05/01/00:29:60
    // 1998/10/10/00:10:00  + 0/00:01:00 = 1998/10/10/00:10:60
    // time *= 24E0; hours = trunc(time); time -= (double)hours;
    // time *= 60E0 ; minutes = trunc(time); time -= (double)minutes;
    // time *= 60E0; seconds = trunc(time + 5E-3); time -= (double)seconds;

    time *= 24E0; hours = trunc(time + 5E-5); time -= (double)hours;
    time *= 60E0 ; minutes = trunc(time + 5E-4); time -= (double)minutes;
    time *= 60E0; seconds = trunc(time + 5E-3); time -= (double)seconds;

    timeStamp.append(year).append("/");
    
    if (month < 10) timeStamp.append("0");
    timeStamp.append(month).append("/");
    
    if (day < 10) timeStamp.append("0");
    timeStamp.append(day).append("/");
    
    if (hours < 10) timeStamp.append("0");
    timeStamp.append(hours).append(":");
    
    if (minutes < 10) timeStamp.append("0");
    timeStamp.append(minutes).append(":");
    
    if (seconds < 10) timeStamp.append("0");
    timeStamp.append(seconds);
    
    return timeStamp.toString();
  
  }     
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private static double JulianDay(double pGregorianDay) {
  
    /* Source: Meeus, Jean: Atronomische Algotithmen, pp. 73-75 
       Input: YYYYMMDD.TTTT with TTTT = time as a fraction of day; 
       pGregorianDay represents one day of the Gregorian Calendar
       as a double,  20000101.5 = 2000/01/01/12:00:00 */
     
    int A = 0, B = 0, year = 0, month = 0;  
    double dayTime = 0.0, julianDay = 0;
  
    year = trunc( pGregorianDay / 10000E0 );
    month = 
      Math.abs( trunc( frac( pGregorianDay / 10000E0 ) * 100E0 ) );
    dayTime = 
      Math.abs( frac( frac( pGregorianDay / 10000E0 ) * 100E0 ) * 100E0 );
  
    if (month < 3) { year -= 1; month += 12; }
  
    A = trunc(year / 100E0);
    if (pGregorianDay > 158210041E-1) 
      B = 2 - A + trunc(A / 4E0);
    else
      B = 0;
  
    julianDay = trunc( 36525E-2 * (year + 4716E0) ) +
      trunc( 306001E-4 * (month + 1) ) + dayTime + B - 15245E-1;
  
    return julianDay;
  
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public static double JulianDay2GregorianDay(double pJulianDay) {
  
    /* Source: Meeus, Jean: Atronomische Algotithmen, pp. 76-80  */

    int A = 0, alpha = 0, B = 0, C = 0, D = 0, E = 0, Z = 0;
    int year = 0, month = 0;
    double F = 0.0, dayTime = 0.0, gregorianDay = 0.0;
    
    Z = trunc( pJulianDay + 5E-1 );
    F = frac( pJulianDay + 5E-1 );
    
    if ( Z < 2299161)
      A = Z;
    else {
      alpha = trunc( (Z - 186721625E-2) / 3652425E-2 );
      A = Z + 1 + alpha - trunc( alpha / 4E0);
    }
    
    B = A + 1524;
    C = trunc( (B - 1221E-1) / 36525E-2 );
    D = trunc( 36525E-2 * C );
    E = trunc( ( (B - D) / 306001E-4 ) );

    dayTime = B - D - trunc( 306001E-4 * E ) + F;
    
    if (E < 14)
      month = E - 1;
    else
      month = E - 13;
      
    if (month > 2)
      year = C - 4716;
    else
      year = C - 4715;

    gregorianDay = (year * 10000E0) + (month * 100E0) + dayTime;

    return gregorianDay;
  
  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public static int DayOfWeek(double pJulianDay) {

    int dayOfWeek = 0;
    if ( frac(pJulianDay) < 0.5 )
      dayOfWeek = ( (int)(pJulianDay) + 1 ) % 7;
    else
      dayOfWeek = ( (int)(pJulianDay) + 2 ) % 7;
 
    if (dayOfWeek == 0)
      return 7;
    else
      return dayOfWeek;

  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private static int trunc(double p) {
  
    return (int)p;
  
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private static double frac(double p) {
  
    return (p - trunc(p));
  
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {
  
    System.out.println( "JD(19571004.81) = " + JulianDay(1957100481E-2) );
    System.out.println( "JD(2000/01/01/12:00:00) = " + 
      JulianDay( TimeStamp2GregorianDay("2000/01/01/12:00:00") ) );
    
    System.out.println( "TS(2436116.31) = " + 
      JulianDay2GregorianDay(243611631E-2) );
    System.out.println( "TS(2451545.00) = "  + 
      GregorianDay2TimeStamp (JulianDay2GregorianDay(245154500E-2) ) );
      
    System.out.println( "1998/12/31/00:00:00 - 1998/12/30/00:00:00 = " + 
      TimeStamp.TimeStampDifference("1998/12/31/00:00:00", 
      "1998/12/30/00:00:00") );
    System.out.println( "1998/12/31/00:00:00 - 1998/12/30/12:00:00 = " + 
      TimeStamp.TimeStampDifference("1998/12/31/00:00:00", 
      "1998/12/30/12:00:00") );
    System.out.println( "1998/12/31/00:00:01 - 1998/12/30/23:59:59 = " + 
      TimeStamp.TimeStampDifference("1998/12/31/00:00:01", 
      "1998/12/30/23:59:59") );
    System.out.println( "1998/12/31/00:00:00 - 1998/12/31/00:00:00 = " + 
      TimeStamp.TimeStampDifference("1998/12/31/00:00:00", 
      "1998/12/31/00:00:00") );      
    System.out.println( "1998/10/10/12:00:00 - 1998/10/10/11:59:59 = " + 
      TimeStamp.TimeStampDifference("1998/10/10/12:00:00", 
      "1998/10/10/11:59:59") );
    System.out.println( "1998/12/31/00:00:00 - 1997/12/31/00:00:00 = " + 
      TimeStamp.TimeStampDifference("1998/12/31/00:00:00", 
      "1997/12/31/00:00:00") );      
    System.out.println( "2001/12/31/00:10:00 - 2001/12/31/00:00:10 = " + 
      TimeStamp.TimeStampDifference("2001/12/31/00:10:00", 
      "2001/12/31/00:00:10") );      
    System.out.println( "2001/12/31/23:59:59 - 2001/12/31/00:00:00 = " + 
      TimeStamp.TimeStampDifference("2001/12/31/23:59:59", 
      "2001/12/31/00:00:00") );      
    System.out.println( "2001/12/31/23:30:00 - 2001/12/31/00:30:00 = " + 
      TimeStamp.TimeStampDifference("2001/12/31/23:30:00", 
      "2001/12/31/00:30:00") );      
  
    String go = Tools.readString("Hit ENTER to continue! ");

    System.out.println(           "1998/12/31/23:59:59  + 0/00:01:01 = " + 
      TimeStamp.TimeStampPlusTime("1998/12/31/23:59:59", "0/00:01:01") );
    System.out.println(           "1998/10/10/12:00:00  + 0/00:01:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/12:00:00", "0/00:01:00") );
    System.out.println(           "1998/10/10/12:00:00  + 5/01:01:59 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/12:00:00", "5/01:01:59") );
    System.out.println(           "1998/10/10/12:00:00  + 0/00:00:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/12:00:00", "0/00:00:00") );
    
    System.out.println(           "2001/12/31/23:59:59  + 0/00:01:01 = " + 
      TimeStamp.TimeStampPlusTime("2001/12/31/23:59:59", "0/00:01:01") );
    System.out.println(           "2001/10/10/12:00:00  + 0/00:01:00 = " + 
      TimeStamp.TimeStampPlusTime("2001/10/10/12:00:00", "0/00:01:00") );
    System.out.println(           "2001/10/10/12:00:00  + 5/01:01:59 = " + 
      TimeStamp.TimeStampPlusTime("2001/10/10/12:00:00", "5/01:01:59") );
    System.out.println(           "1998/05/01/00:10:00  + 0/00:20:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/05/01/00:10:00", "0/00:20:00") );
    System.out.println(           "1998/10/10/00:10:00  + 0/00:01:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/00:10:00", "0/00:01:00") );
    System.out.println(           "1998/10/10/12:00:00  + 0/00:01:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/12:00:00", "0/00:01:00") );
    System.out.println(           "1998/10/10/00:00:00  + 0/00:01:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/00:00:00", "0/00:01:00") );
    System.out.println(           "1998/10/10/00:00:00  + 0/00:10:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/00:00:00", "0/00:10:00") );
    System.out.println(           "1998/10/10/00:00:00  + 0/00:20:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/00:00:00", "0/00:20:00") );
    System.out.println(           "1998/10/10/00:30:00  + 0/00:30:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/00:30:00", "0/00:30:00") );
    System.out.println(           "1998/10/10/00:30:30  + 0/00:00:30 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/00:30:30", "0/00:00:30") );
    System.out.println(           "1998/10/10/23:30:00  + 0/00:30:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/23:30:00", "0/00:30:00") );
    System.out.println(           "1998/10/10/00:59:59  + 0/00:00:02 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/00:59:59", "0/00:00:02") );
    System.out.println(           "1998/10/10/00:00:59  + 0/00:00:02 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/00:00:59", "0/00:00:02") );
    System.out.println(           "1998/10/10/23:30:00  + 1/01:30:00 = " + 
      TimeStamp.TimeStampPlusTime("1998/10/10/23:30:00", "1/01:30:00") );
   
    go = Tools.readString("Hit ENTER to continue! ");

    System.out.println(           "GDay(1998/10/10/00:00:00) = " + 
      TimeStamp.TimeStamp2GregorianDay("1998/10/10/00:00:00") );
    System.out.println(        "TS(GDay(1998/10/10/00:00:01)) = " + 
      TimeStamp.GregorianDay2TimeStamp( 
      TimeStamp.TimeStamp2GregorianDay("1998/10/10/00:00:01") ) );
    System.out.println(        "TS(GDay(1998/12/12/23:59:59)) = " + 
      TimeStamp.GregorianDay2TimeStamp( 
      TimeStamp.TimeStamp2GregorianDay("1998/12/12/23:59:59") ) );
    System.out.println(        "TS(GDay(1998/12/12/10:10:10)) = " + 
      TimeStamp.GregorianDay2TimeStamp( 
      TimeStamp.TimeStamp2GregorianDay("1998/12/12/10:10:10") ) );

    go = Tools.readString("Hit ENTER to continue! ");

    System.out.println("");
    TimeStamp timeStamp = new TimeStamp("2000/01/31/12:11:10");
    System.out.println("2000/01/31/12:11:10 = " + timeStamp);
    timeStamp = new TimeStamp("1954/06/30/00:00:00");
    System.out.println("1954/06/30/00:00:00 = " + timeStamp);
    timeStamp = new TimeStamp("1954/06/30/23:59:59");
    System.out.println("1954/06/30/23:59:59 = " + timeStamp);
    timeStamp = new TimeStamp("2000/11/21/17:30:10");
    System.out.println("2000/11/21/17:30:10 = " + timeStamp);
    timeStamp = new TimeStamp("2000/11/21/00:00:00");
    System.out.println("2000/11/21/00:00:00 = " + timeStamp);
    timeStamp = new TimeStamp("2000/11/21/23:59:59");
    System.out.println("2000/11/21/23:59:59 = " + timeStamp);
    timeStamp = new TimeStamp("2000/11/22/00:00:00");
    System.out.println("2000/11/22/00:00:00 = " + timeStamp);
    
  }
  
}