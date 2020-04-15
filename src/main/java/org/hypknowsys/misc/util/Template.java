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

import java.util.*;
import java.io.*;

/**
 * Template: "Name: ${name}, Age: ${age} years."<br>
 * Note, template placeholders must not contain blank spaces or curly 
 * brackets. Capitalization of template placeholders is relevant.<br>
 * Values: Hashtable(("name", "Smith"), ("age", "101"))<br>
 * Hashtable must not contain other types than String. 
 *
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class Template {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private HashMap Values = null;
  private String TemplateString = null;

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

  public Template(String pTemplate) {
    
    Values = new HashMap();
    TemplateString = pTemplate;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getTemplateString() {
    return TemplateString; }

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
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addValue(String pPlaceholder, String pValue) {
    
    Values.put(pPlaceholder, pValue);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addFirstValue(String pPlaceholder, String pValue) {
    
    this.reset();
    this.addValue(pPlaceholder, pValue);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void addNextValue(String pPlaceholder, String pValue) {
    
    this.addValue(pPlaceholder, pValue);
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void reset() {
    
    Values = new HashMap();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public String insertValues() {
    
    return Template.insertValues(TemplateString, Values);
    
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
  
  public static String insertValues(String pTemplate, HashMap pValues) {
    
    PushbackReader templateReader = new PushbackReader(
    new StringReader(pTemplate));
    StringWriter resultWriter = new StringWriter();
    StringBuffer templateValue = new StringBuffer();
    String value = null;
    
    int nextChar = 0;
    int valueChar = 0;
    
    try {
      int vCurrentChar = templateReader.read();
      while (vCurrentChar != -1) {
        
        if (vCurrentChar == '$') {  // beginning of template
          nextChar = templateReader.read();
          if (nextChar == '{') {
            templateValue = new StringBuffer(pTemplate.length() * 5);
            templateValue.append("${");
            valueChar = templateReader.read();
            while (valueChar != -1) {
              if (valueChar != '}') {
                templateValue.append( (char)valueChar );
                valueChar = templateReader.read();
              }
              else {
                templateValue.append("}");
                value = (String)pValues.get(templateValue.toString().trim());
                if (value != null) {
                  for (int i = 0; i < value.length(); i++) {
                    resultWriter.write(value.charAt(i));
                  }
                }
                valueChar = -1;  // stop here
              }
            }  // while (valueChar != -1)
          }
          else {
            templateReader.unread(nextChar);
          }
        }
        else {  // no beginning of template
          resultWriter.write(vCurrentChar);
        }
        vCurrentChar = templateReader.read();
      }
    }
    catch (Exception e) {
      System.out.println("IOException");
    }
    
    return resultWriter.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {
    
    HashMap testValues = new HashMap();
    testValues.put("${name}", "Smith");
    testValues.put("${age}", "101");
    System.out.println( Template.insertValues(
    "Name: ${ name }, Age: ${ age }", testValues) );
    System.out.println( Template.insertValues(
    "Name: ${name}, Age: ${age}", testValues) );
    System.out.println( Template.insertValues(
    "Name: ${InvalidPlaceholder}, Age: ${Age}", testValues) );
    System.out.println( Template.insertValues(
    "Syntax Error; Name: ${Name, Age: ${Age}", testValues) );
    System.out.println( Template.insertValues(
    "Syntax Error; Name: name}, Age: ${age}", testValues) );
    
    System.out.println("---");
    
    Template template = new Template("Name: ${name}, Age: ${age}");
    template.addValue("${name}", "Smith");
    template.addValue("${age}", "123");
    System.out.println(template.insertValues());
    template.addValue("${name}", "Smith");
    template.addValue("${age}", "12");
    System.out.println(template.insertValues());
    template.reset();
    template.addValue("${name}", "Smith");
    System.out.println(template.insertValues());

  }
  
}