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

package org.hypknowsys.misc.io;

import java.io.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class AbstractFile {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected RandomAccessFile FileBody = null;
  protected File FileName = null;
  protected boolean isOpen = false;
  protected int Mode = UNKNOWN;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  protected static final String MESSAGE_GENERAL_IO_ERROR = 
    "Error: General I/O-Error! ";
  protected static final String MESSAGE_ERROR_FILE = "Error: File ";
  protected static final String MESSAGE_NOT_FOUND = " not found! ";
  protected static final String LINE_SEPARATOR = 
    System.getProperty("line.separator");

  protected static final int UNKNOWN = 0;
  protected static final int READ = 1;
  protected static final int READ_WRITE = 2;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public AbstractFile(File pFileName) {

    FileName = pFileName;
    isOpen = false;

  } 

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
    TmpStringBuffer.append("AbstractFile: FileName=");
    TmpStringBuffer.append(FileName.getAbsolutePath());
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void finalize() {

    this.close();
    isOpen = false;

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public File getFile() {

    return FileName;

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public boolean exists() {

    if (FileName != null) {
      return FileName.exists();
    }
    else {
      return false;
    }
    
  }
        
  /* ########## ########## ########## ########## ########## ######### */

  public void open() {

    if ( !isOpen ) {

    try {
      if ( FileName.exists() ) {
        if ( FileName.canRead() && FileName.canWrite() ) {
          FileBody = new RandomAccessFile(FileName, "rw");
          FileBody.seek(0);
          isOpen = true;
          Mode = READ_WRITE;
        }
        else {
         FileBody = null;
         System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
           " Permission Denied! ");
         isOpen = false;
        }
      }
      else {
        // System.out.println("Creating File " +
        //  FileName.toString() + " ...");
        FileBody = new RandomAccessFile(FileName, "rw");
        FileBody.seek(0);
        isOpen = true;
        Mode = READ_WRITE;
      }
    }
    catch (FileNotFoundException e) {
      FileBody = null;
      System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
        MESSAGE_NOT_FOUND + " [open]");
    }
    catch (IOException e) {
      FileBody = null;
      System.out.println(MESSAGE_GENERAL_IO_ERROR  + " [open]");
      e.printStackTrace(System.out);
      System.out.flush();
    }

    }

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void openReadOnly() {

    if ( !isOpen ) {

    try {
      if ( FileName.exists() ) {
        if ( FileName.canRead() ) {
          FileBody = new RandomAccessFile(FileName, "r");
          FileBody.seek(0);
          isOpen = true;
          Mode = READ;
        }
        else {
         FileBody = null;
         System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
           " Permission Denied! ");
         isOpen = false;
        }
      }
      else {
        FileBody = null;
        System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
          " Permission Denied! ");
        isOpen = false;        
      }
    }
    catch (FileNotFoundException e) {
      FileBody = null;
      System.out.println(MESSAGE_ERROR_FILE +
        FileName.toString() + MESSAGE_NOT_FOUND + " [open]");
    }
    catch (IOException e) {
      FileBody = null;
      System.out.println(MESSAGE_GENERAL_IO_ERROR + " [open]");
      e.printStackTrace(System.out);
      System.out.flush();
    }

    }

  }  

  /* ########## ########## ########## ########## ########## ######### */

  public void close() {

    if (isOpen) {
      try {
        if (FileBody != null) FileBody.close();
        isOpen = false;
      }
      catch (FileNotFoundException e) {
        FileBody = null;
        System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
          MESSAGE_NOT_FOUND + " [close]");
      }
      catch (IOException e) {
        FileBody = null;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [close]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void empty() {

    // file must be closed to empty it, is closed afterwards as well

    try {
      FileOutputStream oFileOutputStream = new FileOutputStream(FileName);
      oFileOutputStream.close();
    }
    catch (FileNotFoundException e) {
      FileBody = null;
      System.out.println(MESSAGE_ERROR_FILE +
        FileName.toString() + MESSAGE_NOT_FOUND + " [empty]");
    }
    catch (IOException e) {
      FileBody = null;
      System.out.println(MESSAGE_GENERAL_IO_ERROR + " [empty]");
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public long getSize() {

    long vSize = 0L;

    if (FileBody != null) {
      try {
        vSize = FileBody.length();
      }
      catch (FileNotFoundException e) {
        FileBody = null;
        vSize = 0L;
        System.out.println(MESSAGE_ERROR_FILE +
          FileName.toString() + MESSAGE_NOT_FOUND + " [getSize]");
      }
      catch (IOException e) {
        FileBody = null;
        vSize = 0L;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [getSize]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
    }

  return vSize;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public long getFilePointer() {

    long vFilePointer = 0L;

    if (FileBody != null) {
      try {
        vFilePointer = FileBody.getFilePointer();
      }
      catch (FileNotFoundException e) {
        FileBody = null;
        vFilePointer = 0L;
        System.out.println(MESSAGE_ERROR_FILE +
          FileName.toString() + MESSAGE_NOT_FOUND + " [getFilePointer]");
      }
      catch (IOException e) {
        FileBody = null;
        vFilePointer = 0L;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [getFilePointer]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
    }

  return vFilePointer;

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void setFilePointer(long pFilePointer) {

    if (FileBody != null) {
      try {
        if ( ( pFilePointer > FileBody.length() ) ||
             (  pFilePointer < 0 ) )
          FileBody.seek( FileBody.length() );
        else
          FileBody.seek(pFilePointer);
      }
      catch (FileNotFoundException e) {
        FileBody = null;
        System.out.println(MESSAGE_ERROR_FILE +
          FileName.toString() + MESSAGE_NOT_FOUND + " [setFilePointer]");
      }
      catch (IOException e) {
        FileBody = null;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [setFilePointer]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public String readLine() {  // reads at current position

    String vLine = null;

    if (FileBody != null) {
      try {
        vLine = FileBody.readLine();
      }
      catch (FileNotFoundException e) {
        FileBody = null;
        System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
          MESSAGE_NOT_FOUND + " [readLine]");
      }
      catch (IOException e) {
        FileBody = null;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [readLine]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
    }

    return vLine;

  }

  /* ########## ########## ########## ########## ########## ######### */

  public void writeLine(String vLine) {  // writes at current position

    if (FileBody != null) {
      try {
        FileBody.writeBytes( vLine + LINE_SEPARATOR );
      }
      catch (FileNotFoundException e) {
        FileBody = null;
        System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
          MESSAGE_NOT_FOUND + " [writeLine]");
      }
      catch (IOException e) {
        FileBody = null;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [writeLine]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
    }

  }

  /* ########## ########## ########## ########## ########## ######### */

  public String readStringBlock(long pBlockSize) {  

    // reads at current position
    byte[] vBlock = new byte[ (int)pBlockSize ];
    int vResult = 0;
    String vStringBlock = null;

    if (FileBody != null) {
      try {
        vResult = FileBody.read(vBlock);
      }
      catch (FileNotFoundException e) {
        FileBody = null;
        System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
          MESSAGE_NOT_FOUND + " [readStringBlock]");
      }
      catch (IOException e) {
        FileBody = null;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [readStringBlock]");
      }
    }
    if (vResult == pBlockSize)
      vStringBlock = new String( vBlock, 0 );

    return vStringBlock;

  } 

  /* ########## ########## ########## ########## ########## ######### */

  public void writeStringBlock(String vStringBlock) {  
    
    // writes at current position
    if (FileBody != null) {
      try {
        FileBody.writeBytes(vStringBlock);
      }
      catch (FileNotFoundException e) {
        FileBody = null;
        System.out.println(MESSAGE_ERROR_FILE +
          FileName.toString() + MESSAGE_NOT_FOUND + " [writeStringBlock]");
      }
      catch (IOException e) {
        FileBody = null;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [writeStringBlock]");
      }
    }

  } 

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}