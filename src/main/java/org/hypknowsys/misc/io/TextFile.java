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
 * However, read and write access to the same file must not overlap. After
 * adding a new line, reading the file always starts at the first line.
 * Short Example for using the Class <code>TextFile</code>: <p>
 * <pre>
 * // import the Java package containing the class TextFile
 * import org.hypknowsys.misc.io.*;
 * // import the Java package containing the class File
 * import java.io.*;
 *
 * // ...
 *
 * TextFile myTextFile = null;
 * String currentLine = null;
 *
 * // create a TextFile
 * myTextFile = new TextFile(
 *   new File("test.txt") );
 *
 * // open the TextFile for further usage
 * myTextFile.open();
 *
 * // empty the file and set the new first line
 * myTextFile.setFirstLine("There are 10 digits to mention:");
 *
 * // write 10 new lines
 * for (int i = 0; i < 10; i++)
 *   myTextFile.setNextLine( ( new Integer(i) ).toString() );
 *
 * // print contents of file
 * currentLine = myTextFile.getFirstLine();
 * while (currentLine != null) {
 *   System.out.println(currentLine);
 *   currentLine = myTextFile.getNextLine();
 * }
 *
 * // close the TextFile
 * myTextFile.close();
 *
 * // empty the TextFile
 * myTextFile.empty();
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

public class TextFile {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  protected BufferedReader MyFileReader = null;
  protected BufferedWriter MyFileWriter = null;
  protected File FileName = null;
  protected boolean isOpen = false;
  protected int BufferSize = 0;
  protected boolean ReadOnly = false;
  protected int Mode = UNKNOWN;
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
  
  protected static final int UNKNOWN = 0;
  protected static final int READ = 1;
  protected static final int WRITE = 2;
  
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
  
  public TextFile(File pFileName) {
    
    FileName = pFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TextFile(File pFileName, int pBufferSize) {
    
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
    TmpStringBuffer.append("; ReadOnly=");
    TmpStringBuffer.append(ReadOnly);
    
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
    ReadOnly = false;
    Mode = UNKNOWN;
    
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
    
    isOpen = false;
    ReadOnly = false;
    Mode = UNKNOWN;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void openReadOnly() {
    
    isOpen = false;
    ReadOnly = true;
    Mode = UNKNOWN;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void empty() {
    
    // file must be closed to empty it and it is closed afterwards as well
    
    this.close();
    this.openForWriting(false);
    this.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public long getSize() {
    
    long vSize = 0L;
    
    if (FileName != null) {
      vSize = FileName.length();
    }
    
    return vSize;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void openForReading() {
    
    if ( !isOpen ) {
      
      try {
        if ( FileName.exists() ) {
          if ( FileName.canRead() ) {
            if (BufferSize == 0) {
              MyFileReader = new BufferedReader( new InputStreamReader(
              new FileInputStream(FileName), FileEncoding) );
            }
            else {
              MyFileReader = new BufferedReader( new InputStreamReader(
              new FileInputStream(FileName), FileEncoding), BufferSize );
            }
            isOpen = true;
            Mode = READ;
          }
          else {
            MyFileReader = null;
            System.out.println(MESSAGE_ERROR_FILE + FileName.toString() +
            " Permission Denied! ");
            isOpen = false;
          }
        }
        else {
          MyFileReader = null;
          System.out.println(MESSAGE_ERROR_FILE + FileName.toString() +
          " File not found! ");
          isOpen = false;
        }
      }
      catch (FileNotFoundException e) {
        MyFileReader = null;
        System.out.println(MESSAGE_ERROR_FILE + FileName.toString() +
        MESSAGE_NOT_FOUND + " [open]");
      }
      catch (UnsupportedEncodingException e) {
        MyFileReader = null;
        System.out.println("Error: Encoding " + FileEncoding
        + " is not supported! [open]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
      catch (IOException e) {
        MyFileReader = null;
        System.out.println(MESSAGE_GENERAL_IO_ERROR  + " [open]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
      
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  protected void openForWriting(boolean pAppend) {
    
    if (ReadOnly) {
      System.out.println(MESSAGE_ERROR_FILE + FileName.toString() +
      " Permission Denied! ");
      System.out.flush();
      System.exit(-1);
    }
    
    if ( !isOpen ) {
      
      try {
        if (!FileName.exists()) {
          FileName.createNewFile();
        }
        if (FileName.canWrite()) {
          if (BufferSize == 0) {
            MyFileWriter = new BufferedWriter( new OutputStreamWriter(
            new FileOutputStream(FileName, pAppend), FileEncoding) );
          }
          else {
            MyFileWriter = new BufferedWriter( new OutputStreamWriter(
            new FileOutputStream(FileName, pAppend), FileEncoding), BufferSize );
          }
          isOpen = true;
          Mode = WRITE;
        }
        else {
          MyFileWriter = null;
          System.out.println(MESSAGE_ERROR_FILE + FileName.toString() +
          " Permission Denied! ");
          isOpen = false;
        }
      }
      catch (FileNotFoundException e) {
        MyFileWriter = null;
        System.out.println(MESSAGE_ERROR_FILE + FileName.toString() +
        MESSAGE_NOT_FOUND + " [open]");
      }
      catch (UnsupportedEncodingException e) {
        MyFileWriter = null;
        System.out.println("Error: Encoding " + FileEncoding
        + " is not supported! [open]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
      catch (IOException e) {
        MyFileWriter = null;
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
        if (Mode == WRITE) {
          if (MyFileWriter != null) {
            MyFileWriter.flush();
            MyFileWriter.close();
          }
        }
        else if (Mode == READ) {
          if (MyFileReader != null) {
            MyFileReader.close();
          }
        }
        isOpen = false;
        ReadOnly = false;
        Mode = UNKNOWN;
      }
      catch (FileNotFoundException e) {
        MyFileReader = null;
        System.out.println(MESSAGE_ERROR_FILE + FileName.toString() +
        MESSAGE_NOT_FOUND + " [close]");
      }
      catch (IOException e) {
        MyFileReader = null;
        System.out.println(MESSAGE_GENERAL_IO_ERROR + " [close]");
        e.printStackTrace(System.out);
        System.out.flush();
      }
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getFirstLine() {
    
    // Calling Method has to check: String != null?
    
    this.close();
    this.openForReading();
    CurrentLine = null;
    try {
      CurrentLine = MyFileReader.readLine();
    }
    catch (IOException e) {
      MyFileReader = null;
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
    
    if (Mode == WRITE || !isOpen) {
      this.close();
      this.openForReading();
    }
    CurrentLine = null;
    try {
      CurrentLine = MyFileReader.readLine();
    }
    catch (IOException e) {
      MyFileReader = null;
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
  
  public void setFirstLine(String pNewLine) {
    
    this.close();
    this.openForWriting(false);  // append = false
    try {
      MyFileWriter.write(pNewLine);
      MyFileWriter.write(LINE_SEPARATOR);
    }
    catch (IOException e) {
      MyFileWriter = null;
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
    
    if (Mode == READ || !isOpen) {
      this.close();
      this.openForWriting(true);  // append = true
    }
    try {
      MyFileWriter.write(pNewLine);
      MyFileWriter.write(LINE_SEPARATOR);
    }
    catch (IOException e) {
      MyFileWriter = null;
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
    
    TextFile myTextFile = null;
    String currentLine = null;
    
    // create a TextFile
    myTextFile = new TextFile(
    new File("test.txt") );
    
    // open the TextFile for further usage
    myTextFile.open();
    
    // empty the file and set the new first line
    myTextFile.setFirstLine("There are 10 digits to mention:");
    
    // write 10 new lines
    for (int i = 0; i < 10; i++)
      myTextFile.setNextLine( ( new Integer(i) ).toString() );
    
    // print contents of file
    currentLine = myTextFile.getFirstLine();
    while (currentLine != null) {
      System.out.println(currentLine);
      currentLine = myTextFile.getNextLine();
    }
    // outputs null, since there is no next line
    System.out.println(myTextFile.getNextLine());
    
    // add a new line
    myTextFile.setNextLine("This is the new last line!");
    
    // after writing, reading the file start at the first line
    System.out.println(myTextFile.getNextLine());
    
    // close the TextFile
    myTextFile.close();
    
    // empty the TextFile
    myTextFile.empty();
    
    // open the TextFile for further usage
    myTextFile.openReadOnly();
    
    // empty the file and set the new first line
    myTextFile.setFirstLine("You cannot modify a read-only file!");
    
  }
  
}