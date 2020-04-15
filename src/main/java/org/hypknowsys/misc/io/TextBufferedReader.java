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
 * This class provides the user with basic methods for sequentially
 * reading character text files. Text files can be opened and closed. 
 * There are methods to sequentially read a text file line by line. 
 * Short Example for using the Class <code>TextBufferedReader</code>: <p>
 * <pre>
 * // import the Java package containing the class TextBufferedReader
 * import org.hypknowsys.misc.io.*;
 * // import the Java package containing the class File
 * import java.io.*;
 *
 * // ...
 *
 * TextBufferedReader myTextBufferedReader = null;
 * TextBufferedWriter myTextBufferedWriter = null;
 * String currentLine = null;
 *
 * // create a TextBufferedWriter
 * myTextBufferedWriter = new TextBufferedWriter(
 *   new File("test.txt") );
 *
 * // open the TextBufferedWriter for further usage
 * myTextBufferedWriter.open();
 *
 * // empty the file and set the new first line
 * myTextBufferedWriter.setFirstLine("There are 10 digits to mention:");
 *
 * // write 10 new lines
 * for (int i = 0; i < 10; i++)
 *   myTextBufferedWriter.setNextLine( ( new Integer(i) ).toString() );
 *
 * // close the TextBufferedWriter
 * myTextBufferedWriter.close();
 *
 * // create a TextBufferedReader
 * myTextBufferedReader = new TextBufferedReader(
 *   new File("test.txt") );
 *
 * // open the TextBufferedReader for further usage
 * myTextBufferedReader.open();
 *
 * // print contents of file
 * currentLine = myTextBufferedReader.getFirstLine();
 * while (currentLine != null) {
 *   System.out.println(currentLine);
 *   currentLine = myTextBufferedReader.getNextLine();
 * }
 *
 * // close the TextBufferedReader
 * myTextBufferedReader.close();
 *
 * // ...
 *
 * </pre>
 * @see org.hypknowsys.io.AbstractFile
 */

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class TextBufferedReader {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected BufferedReader FileBody = null;
  protected File FileName = null;
  protected boolean isOpen = false;
  protected int BufferSize = 0;
  protected String CurrentLine = null;
  protected String CommentLinePrefix = COMMENT_LINE_PREFIX;
  protected String FileEncoding = FILE_ENCODING;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  public static final String COMMENT_LINE_PREFIX = "#";

  protected static final String MESSAGE_GENERAL_IO_ERROR = 
  "Error: General I/O-Error! ";
  protected static final String MESSAGE_ERROR_FILE = 
  "Error: File ";
  protected static final String MESSAGE_NOT_FOUND = 
  " not found! ";
  protected static final String LINE_SEPARATOR = 
  System.getProperty("line.separator");
  protected static final String FILE_ENCODING =
  System.getProperty("file.encoding");

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public TextBufferedReader(File pFileName) {
    
    FileName = pFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TextBufferedReader(File pFileName, int pBufferSize) {
    
    FileName = pFileName;
    if (pBufferSize > 0) {
      BufferSize = pBufferSize;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String getCommentLinePrefix() {
    return CommentLinePrefix; }
  public String getFileEncoding() {
    return FileEncoding; }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCommentLinePrefix(String pCommentLinePrefix) {
    this.CommentLinePrefix = pCommentLinePrefix; }
  public void setFileEncoding(String pFileEncoding) {
    this.FileEncoding = pFileEncoding; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    TmpStringBuffer.append(": ");
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
  
  public void open() {

    if ( !isOpen ) {

    try {
      if ( FileName.exists() ) {
        if ( FileName.canRead() ) {
          if (BufferSize == 0) {
            FileBody = new BufferedReader( new InputStreamReader(
            new FileInputStream(FileName), FileEncoding) );
          }
          else {
            FileBody = new BufferedReader( new InputStreamReader(
            new FileInputStream(FileName), FileEncoding), BufferSize );
          }
          isOpen = true;
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
           " File not found! ");
         isOpen = false;
      }
    }
    catch (FileNotFoundException e) {
      FileBody = null;
      System.out.println(MESSAGE_ERROR_FILE + FileName.toString() + 
        MESSAGE_NOT_FOUND + " [open]");
    }
    catch (UnsupportedEncodingException e) {
      FileBody = null;
      System.out.println("Error: Encoding " + FileEncoding 
      + " is not supported! [open]");
      e.printStackTrace(System.out);
      System.out.flush();
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
  
  public long getSize() {

    long vSize = 0L;

    if (FileName != null)
        vSize = FileName.length();
 
    return vSize;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getFirstLine() {

    // Calling Method has to check: String != null?

    this.close();
    this.open();
    CurrentLine = null;
    try {
      CurrentLine = FileBody.readLine();
    }
    catch (IOException e) {
      FileBody = null;
      System.out.println(MESSAGE_GENERAL_IO_ERROR + " [close]");
      e.printStackTrace(System.out);
      System.out.flush();
    }

    return CurrentLine;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getFirstLineButIgnoreComments() {

    CurrentLine = this.getFirstLine();   
    while (CurrentLine != null && CurrentLine.startsWith(CommentLinePrefix)) {
      CurrentLine = this.getNextLine();
    }

    return CurrentLine;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getFirstLineButIgnoreCommentsAndEmptyLines() {

    CurrentLine = this.getFirstLine();   
    while (CurrentLine != null && (CurrentLine.startsWith(CommentLinePrefix)
    || CurrentLine.length() == 0)) {
      CurrentLine = this.getNextLine();
    }

    return CurrentLine;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getNextLine() {

    // Calling Method has to check: String != null?

    CurrentLine = null;
    try {
      CurrentLine = FileBody.readLine();
    }
    catch (IOException e) {
      FileBody = null;
      System.out.println(MESSAGE_GENERAL_IO_ERROR + " [close]");
      e.printStackTrace(System.out);
      System.out.flush();
    }

    return CurrentLine;

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getNextLineButIgnoreComments() {

    CurrentLine = this.getNextLine();   
    while (CurrentLine != null && CurrentLine.startsWith(CommentLinePrefix)) {
      CurrentLine = this.getNextLine();
    }

    return CurrentLine;

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getNextLineButIgnoreCommentsAndEmptyLines() {

    CurrentLine = this.getNextLine();   
    while (CurrentLine != null && (CurrentLine.startsWith(CommentLinePrefix)
    || CurrentLine.length() == 0)) {
      CurrentLine = this.getNextLine();
    }

    return CurrentLine;

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
  
  public static void main(String args[]) {
    
    TextBufferedReader myTextBufferedReader = null;
    TextBufferedWriter myTextBufferedWriter = null;
    String currentLine = null;
    
    // create a TextBufferedWriter
    myTextBufferedWriter = new TextBufferedWriter(
    new File("test.txt") );
    
    // open the TextBufferedWriter for further usage
    myTextBufferedWriter.open();
    
    // empty the file and set the new first line
    myTextBufferedWriter.setFirstLine("There are 10 digits to mention:");
    
    // write 10 new lines
    for (int i = 0; i < 10; i++)
      myTextBufferedWriter.setNextLine( ( new Integer(i) ).toString() );
    
    // close the TextBufferedWriter
    myTextBufferedWriter.close();
    
    // create a TextBufferedReader
    myTextBufferedReader = new TextBufferedReader(
    new File("test.txt") );
    
    // open the TextBufferedReader for further usage
    myTextBufferedReader.open();
    
    // print contents of file
    currentLine = myTextBufferedReader.getFirstLine();
    while (currentLine != null) {
      System.out.println(currentLine);
      currentLine = myTextBufferedReader.getNextLine();
    }
    
    // close the TextBufferedReader
    myTextBufferedReader.close();
 
  }
  
}