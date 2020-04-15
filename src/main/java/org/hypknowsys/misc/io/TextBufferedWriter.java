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
 * writing character text files. Text files can be created, emptied,
 * opened and closed. There are methods to sequentially write a text
 * file line by line. Short Example for using the Class
 * <code>TextBufferedWriter</code>: <p>
 * <pre>
 * // import the Java package containing the class TextBufferedWriter
 * import org.hypknowsys.misc.io.*;
 * // import the Java package containing the class File
 * import java.io.*;
 *
 * // ...
 *
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
 * // ...
 *
 * </pre>
 * @see org.hypknowsys.io.AbstractFile
 */

/**
 * @version 2.2, 10 May 2004
 * @author Karsten Winkler
 */

public class TextBufferedWriter {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected BufferedWriter FileBody = null;
  protected File FileName = null;
  protected boolean isOpen = false;
  protected int BufferSize = 0;
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
  
  public TextBufferedWriter(File pFileName) {
    
    FileName = pFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TextBufferedWriter(File pFileName, int pBufferSize) {
    
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
  
  public void empty() {
    
    // file must be closed to empty it and it is closed afterwards as well
    
    this.close();
    this.open(false);
    this.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void open() {
    
    this.open(true);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void open(boolean pAppend) {
    
    if ( !isOpen ) {
      
      try {
        if (!FileName.exists()) {
          FileName.createNewFile();
        }
        if (FileName.canWrite()) {
          if (BufferSize == 0) {
            FileBody = new BufferedWriter( new OutputStreamWriter(
            new FileOutputStream(FileName, pAppend), FileEncoding) );
          }
          else {
            FileBody = new BufferedWriter( new OutputStreamWriter(
            new FileOutputStream(FileName, pAppend), FileEncoding), BufferSize );
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
        if (FileBody != null) {
          FileBody.flush();
          FileBody.close();
        }
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
    
    if (FileName != null) {
      if (isOpen) {
        try {
          if (FileBody != null) {
            FileBody.flush();
          }
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
      vSize = FileName.length();
    }
    
    return vSize;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFirstLine(String pNewLine) {
    
    this.close();
    this.open(false);
    try {
      FileBody.write(pNewLine);
      FileBody.write(LINE_SEPARATOR);
    }
    catch (IOException e) {
      FileBody = null;
      System.out.println(MESSAGE_GENERAL_IO_ERROR + " [setFirstLine]");
      e.printStackTrace(System.out);
      System.out.flush();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setFirstLineAsCommentLine(String pComment) {
    
    this.setFirstLine(CommentLinePrefix + " " + pComment);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setNextLine(String pNewLine) {
    
    try {
      FileBody.write(pNewLine);
      FileBody.write(LINE_SEPARATOR);
    }
    catch (IOException e) {
      FileBody = null;
      System.out.println(MESSAGE_GENERAL_IO_ERROR + " [setNextLine]");
      e.printStackTrace(System.out);
      System.out.flush();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setNextLineAsCommentLine(String pComment) {
    
    this.setFirstLine(CommentLinePrefix + " " + pComment);
    
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
    
  }
  
}