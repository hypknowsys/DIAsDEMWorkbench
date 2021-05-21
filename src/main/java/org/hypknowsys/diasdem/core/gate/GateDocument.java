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

package org.hypknowsys.diasdem.core.gate;

import java.io.File;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.Tools;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class GateDocument {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private StringBuffer TextWithNodes = null;
  private StringBuffer NeAnnotationSet = null;
  private StringBuffer StructuralAnnotationSet = null;
  private StringBuffer SemanticAnnotationSet = null;
  
  private int NextNodeID = 0;
  private transient int StartNodeID = 0;
  private transient int EndNodeID = 0;
  
  private String FileName = null;
  
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
  
  public GateDocument() {
    
    this(null);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public GateDocument(String pFileName) {
    
    TextWithNodes = new StringBuffer(5000);
    NeAnnotationSet = new StringBuffer(5000);
    StructuralAnnotationSet = new StringBuffer(5000);
    SemanticAnnotationSet = new StringBuffer(5000);
    
    FileName = pFileName;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getFileName() {
    return FileName; }
  
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
  
  public void addText(String pText) {
    
    this.TextWithNodes.append(Tools.insertUTF8EntityReferences(pText));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addPlainText(String pText) {
    
    this.TextWithNodes.append(Tools.insertUTF8EntityReferences(pText));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addBlankSpace() {
    
    this.TextWithNodes.append(' ');
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addStructurallyAnnotatedText(String pText,
  String pStructuralAnnotationType) {
    
    this.StartNodeID = this.getNextNodeID();
    this.EndNodeID = this.getNextNodeID();
    
    this.TextWithNodes.append(this.createNextNodeTag(this.StartNodeID));
    this.TextWithNodes.append(Tools.insertUTF8EntityReferences(pText));
    this.TextWithNodes.append(this.createNextNodeTag(this.EndNodeID));
    
    this.createStruturalAnnotation(pStructuralAnnotationType, this.StartNodeID,
    this.EndNodeID);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addStructuralAnnotationStartNode(String pStruturalAnnotationType) {
    
    this.StartNodeID = this.getNextNodeID();
    this.EndNodeID = this.getNextNodeID();
    
    this.TextWithNodes.append(this.createNextNodeTag(this.StartNodeID));
    this.createStruturalAnnotation(pStruturalAnnotationType, this.StartNodeID,
    this.EndNodeID);
    
    return this.EndNodeID;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addStructuralAnnotationEndNode(int pEndNode) {
    
    this.TextWithNodes.append(this.createNextNodeTag(pEndNode));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addSemanticallyAnnotatedText(String pText,
  String pSemanticAnnotationType) {
    
    this.StartNodeID = this.getNextNodeID();
    this.EndNodeID = this.getNextNodeID();
    
    this.TextWithNodes.append(this.createNextNodeTag(this.StartNodeID));
    this.TextWithNodes.append(Tools.insertUTF8EntityReferences(pText));
    this.TextWithNodes.append(this.createNextNodeTag(this.EndNodeID));
    
    this.createSemanticAnnotation(pSemanticAnnotationType, this.StartNodeID,
    this.EndNodeID);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int addSemanticAnnotationStartNode(String pSemanticAnnotationType) {
    
    this.StartNodeID = this.getNextNodeID();
    this.EndNodeID = this.getNextNodeID();
    
    this.TextWithNodes.append(this.createNextNodeTag(this.StartNodeID));
    this.createSemanticAnnotation(pSemanticAnnotationType, this.StartNodeID,
    this.EndNodeID);
    
    return this.EndNodeID;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void addSemanticAnnotationEndNode(int pEndNode) {
    
    this.TextWithNodes.append(this.createNextNodeTag(pEndNode));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeXml() {
    
    if (FileName != null) {
      this.writeXml(FileName);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeXml(String pFileName) {
    
    TextFile textFile = new TextFile(new File(pFileName));
    textFile.setFirstLine("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    textFile.setNextLine("<GateDocument>");
    textFile.setNextLine("<TextWithNodes>");
    textFile.setNextLine(this.TextWithNodes.toString());
    textFile.setNextLine("</TextWithNodes>");
    this.writeXmlAnnotationSets(textFile);
    textFile.setNextLine("</GateDocument>");
    textFile.close();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private int getNextNodeID() { return NextNodeID++; }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private String createNextNodeTag(int pNodeID) {
    
    return "<Node id=\"" + pNodeID +"\"/>";
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createStruturalAnnotation(String pStructuralAnnotationType,
  int pStartNodeID, int pEndNodeID) {
    
    StructuralAnnotationSet.append("<Annotation Type=\"");
    StructuralAnnotationSet.append(pStructuralAnnotationType);
    StructuralAnnotationSet.append("\" StartNode=\"");
    StructuralAnnotationSet.append(pStartNodeID);
    StructuralAnnotationSet.append("\" EndNode=\"");
    StructuralAnnotationSet.append(pEndNodeID);
    StructuralAnnotationSet.append("\"> </Annotation>"
    + Tools.getLineSeparator());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createSemanticAnnotation(String pSemanticAnnotationType,
  int pStartNodeID, int pEndNodeID) {
    
    SemanticAnnotationSet.append("<Annotation Type=\"");
    SemanticAnnotationSet.append(pSemanticAnnotationType);
    SemanticAnnotationSet.append("\" StartNode=\"");
    SemanticAnnotationSet.append(pStartNodeID);
    SemanticAnnotationSet.append("\" EndNode=\"");
    SemanticAnnotationSet.append(pEndNodeID);
    SemanticAnnotationSet.append("\"> </Annotation>"
    + Tools.getLineSeparator());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void writeXmlAnnotationSets(TextFile pTextFile) {
    
    if (NeAnnotationSet.length() > 0) {
      pTextFile.setNextLine("<AnnotationSet Name=\"DIAsDEM named entity\" >");
      pTextFile.setNextLine(this.NeAnnotationSet.toString());
      pTextFile.setNextLine("</AnnotationSet>");
    }
    if (StructuralAnnotationSet.length() > 0) {
      pTextFile.setNextLine("<AnnotationSet Name=\"DIAsDEM structural\" >");
      pTextFile.setNextLine(this.StructuralAnnotationSet.toString());
      pTextFile.setNextLine("</AnnotationSet>");
    }
    if (SemanticAnnotationSet.length() > 0) {
      pTextFile.setNextLine("<AnnotationSet Name=\"DIAsDEM semantic\" >");
      pTextFile.setNextLine(this.SemanticAnnotationSet.toString());
      pTextFile.setNextLine("</AnnotationSet>");
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String pOptions[]) {}
  
}