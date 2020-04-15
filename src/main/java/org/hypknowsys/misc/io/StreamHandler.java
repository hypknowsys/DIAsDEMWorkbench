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
 * This class represents a thread, which emties an its input stream and
 * optionally redirects this input stream into the specified output stream.
 * The file encoding is determined by System.getProperty("file.encoding").
 * When executing external program with System.exec(), StreamHandler should 
 * be used to empty System.out. and System.err in parallel. Example:<p>
 *
 * <code>
 * Process p = Runtime.getRuntime().exec("program.exe input.txt");
 * FileOutputStream outputFileStream = new FileOutputStream("output.txt");
 *
 * StreamHandler outputStreamHandler = new StreamHandler(p.getInputStream(), 
 *   null, outputFileStream);
 * StreamHandler errorStreamHandler = new StreamHandler(p.getErrorStream(), 
 *   null, System.out);
 * outputStreamHandler.start();
 * errorStreamHandler.start();
 *
 * int exitValue = p.waitFor();
 * while (!outputStreamHandler.isFinished()) {}
 * while (!errorStreamHandler.isFinished()) {}
 *
 * outputFileStream.flush(); outputFileStream.close();
 * System.out.flush();
 * outputStreamHandler = null;
 * errorStreamHandler = null;
 * </code>
 *
 * @version 0.1, 23 May 2004
 * @author Karsten Winkler, kwinkler{at}hypKNOWsys{d0t}org
 */
  
public class StreamHandler extends Thread {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  protected InputStream MyInputStream = null;
  protected String OutputStreamPrefix = null;
  protected OutputStream MyOutputStream = null;
  protected String FileEncoding = System.getProperty("file.encoding");
  protected String LineSeparator = System.getProperty("line.separator");
  
  protected boolean Finished = false;
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
/**
 * The specified pInputStream will only be emptied.
 */
  
  public StreamHandler(InputStream pInputStream) {
    
    this(pInputStream, null, null);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
/**
 * The specified pInputStream will be emptied and redirected to pOutputStream.
 */
  
  public StreamHandler(InputStream pInputStream, String pOutputStreamPrefix,
  OutputStream pOutputStream) {
    
    MyInputStream = pInputStream;
    OutputStreamPrefix = pOutputStreamPrefix;
    MyOutputStream = pOutputStream;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public boolean isFinished() {
    return Finished; }
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    try {
      OutputStreamWriter myOutputStreamWriter = null;
      BufferedWriter myBufferedWriter = null;
      if (MyOutputStream != null) {
        myOutputStreamWriter = new OutputStreamWriter(
        MyOutputStream, FileEncoding);
        myBufferedWriter = new BufferedWriter(myOutputStreamWriter);
      }
      InputStreamReader myInputStreamReader = new InputStreamReader(
      MyInputStream, FileEncoding);
      BufferedReader myBufferedReader = new BufferedReader(
      myInputStreamReader);
      String line = null;
      while ((line = myBufferedReader.readLine()) != null) {
        if (myBufferedWriter != null) {
          if (OutputStreamPrefix != null) {
            myBufferedWriter.write(OutputStreamPrefix);
          }
          myBufferedWriter.write(line);
          myBufferedWriter.write(LineSeparator);
        }
      }
      if (myBufferedWriter != null) {
        myBufferedWriter.flush();
        myBufferedWriter.close();
      }
      Finished = true;
    } catch (IOException exception) {
      exception.printStackTrace();
      Finished = true;
    }
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

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