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

package org.hypknowsys.diasdem.tasks.postprocess.deriveStructuredDtd;

import java.io.*;
import java.util.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.diasdem.core.neex.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class StructuredDtdEdge implements Serializable {

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private long ID = 0L;
  private long SourceVertexID = 0L;
  private long TargetVertexID = 0L;
  private String Label = null;
  private String[] LabelArray = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;
  private transient StringTokenizer TmpStringTokenizer = null;
  private transient String TmpString = null;
  private transient Iterator TmpIterator = null;
  private transient ArrayList TmpArrayList = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public StructuredDtdEdge() {

    ID = 0L;
    SourceVertexID = 0L;
    TargetVertexID = 0L;
    Label = null;
    LabelArray = null;

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public StructuredDtdEdge(long pID, long pSourceVertexID, long pTargetVertexID,
    String pLabel, String[] pLabelArray) {

    ID = pID;
    SourceVertexID = pSourceVertexID;
    TargetVertexID = pTargetVertexID;
    Label = pLabel;
    LabelArray = pLabelArray;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  public long getID() { 
    return ID; }
  public long getSourceVertexID() { 
    return SourceVertexID; }
  public long getTargetVertexID() { 
    return TargetVertexID; }
  public String getLabel() { 
    return Label; }
  public String[] getLabelArray() { 
    return LabelArray; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setID(long pID) { 
    ID = pID; }
  public void setSourceVertexID(long pSourceVertexID) { 
    SourceVertexID = pSourceVertexID; }
  public void setTargetVertexID(long pTargetVertexID) {
    TargetVertexID = pTargetVertexID; }
  public void setLabel(String pLabel) {
    Label = pLabel; }
  public void setLabelArray(String[] pLabelArray) {
    LabelArray = pLabelArray; }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append("StructuredDtdEdge: ID=");
    TmpStringBuffer.append(ID);
    TmpStringBuffer.append("; SourceVertexID=");
    TmpStringBuffer.append(SourceVertexID);
    TmpStringBuffer.append("; TargetVertexID=");
    TmpStringBuffer.append(TargetVertexID);
    TmpStringBuffer.append("; Label=");
    TmpStringBuffer.append(Label);
    TmpStringBuffer.append("; LabelArray=");
    TmpStringBuffer.append(LabelArray);
    
    return TmpStringBuffer.toString();
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toItemLine() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append( Itemizer.longToItem(ID) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.longToItem(SourceVertexID) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.longToItem(TargetVertexID) );
    TmpStringBuffer.append(Itemizer.ITEM_SEPARATOR);
    TmpStringBuffer.append( Itemizer.stringToItem(Label) );

    return TmpStringBuffer.toString();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public static String getCsvHeaderLine() { 

    return "ID,SourceVertexID,TargetVertexID,Label";

  } 

  /* ########## ########## ########## ########## ########## ######### */
  
  public String getCsvValueLine() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append( Itemizer.longToItem(ID) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.longToItem(SourceVertexID) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.longToItem(TargetVertexID) );
    TmpStringBuffer.append(",");
    TmpStringBuffer.append( Itemizer.stringToItem(Label) );

    return TmpStringBuffer.toString();

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void fromItemLine(String pItemLine) throws NumberFormatException { 

    this.reset();
    Itemizer itemizer = new Itemizer(pItemLine);

    try {
      ID = Itemizer.itemToLong( itemizer.getNextItem() );
      SourceVertexID = Itemizer.itemToLong( itemizer.getNextItem() );
      TargetVertexID = Itemizer.itemToLong( itemizer.getNextItem() );
      Label = Itemizer.itemToString( itemizer.getNextItem() );
    }
    catch (NoSuchElementException e1) { 
      this.reset(); throw new NumberFormatException(); }
    catch (NumberFormatException e2) { 
      this.reset(); throw new NumberFormatException(); }
    catch (StringIndexOutOfBoundsException e2) { 
      this.reset(); throw new NumberFormatException(); }

  } 
 
  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeGmlRepresentation(TextFile pTextFile) { 

    pTextFile.setNextLine("edge [");
    pTextFile.setNextLine("  source " + SourceVertexID);
    pTextFile.setNextLine("  target " + TargetVertexID);
    pTextFile.setNextLine("  label \"" + ID + "\"");
    pTextFile.setNextLine("  diasdemLabel \"" + Label + "\"");
    pTextFile.setNextLine("]");

  }  

  /* ########## ########## ########## ########## ########## ######### */
  
  public void writeXgmmlRepresentation(TextFile pTextFile) { 

//     <edge source="1" target="2" label="1-2">
//         <graphics font="Lucida Sans" visible="true" fill="ff0000ff" outline="ff0000ff">
//             <Line>
//                 <point x="100.0" y="100.0"/>
//                 <point x="100.0" y="100.0"/>
//             </Line>
//             <att name="className" type="string" value="salvo.jesus.graph.visual.VisualEdge"/>
//             <att name="painter" type="string" value="salvo.jesus.graph.visual.drawing.VisualEdgePainterImpl"/>
//         </graphics>
//     </edge>

    pTextFile.setNextLine("<edge source=\"" + SourceVertexID + "\" target=\"" +
      TargetVertexID + "\" label=\"" + Label + "\">");
    pTextFile.setNextLine("<graphics font=\"Lucida Sans\" visible=\"true\" fill=\"ff0000ff\" outline=\"ff0000ff\">  <Line>  <point x=\"100.0\" y=\"100.0\"/> <point x=\"100.0\" y=\"100.0\"/>  </Line> <att name=\"className\" type=\"string\" value=\"salvo.jesus.graph.visual.VisualEdge\"/>  <att name=\"painter\" type=\"string\" value=\"salvo.jesus.graph.visual.drawing.VisualEdgePainterImpl\"/> </graphics> </edge>");

  }  

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private void reset() {

    ID = 0L;
    SourceVertexID = 0L;
    TargetVertexID = 0L;
    Label = null;
    LabelArray = null;

  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}