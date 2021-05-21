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

package org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.event.*;
import org.hypknowsys.misc.io.*;
import org.hypknowsys.misc.swing.*;
import org.hypknowsys.misc.util.*;
import org.hypknowsys.server.*;
import org.hypknowsys.core.*;
import org.hypknowsys.client.gui.*;
import org.hypknowsys.diasdem.server.*;
import org.hypknowsys.diasdem.core.*; 
import org.hypknowsys.diasdem.core.default21.*;
import org.hypknowsys.diasdem.core.neex.*;
import org.hypknowsys.diasdem.client.gui.*;

/**
 * @version 2.1.0.2, 18 October 2003
 * @author Karsten Winkler
 */

public class FindCollocationsFrequencyPosFilterTask 
extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private FindCollocationsFrequencyPosFilterParameter CastParameter = null;
  private ArrayList ListOfTokens = null;
  private ArrayList ListOfTags = null;
  private ArrayList ListOfLemmas = null;
  private DIAsDEMthesaurus AllCollocations = null;
  private DIAsDEMthesaurus ValidCollocations = null;
  private Pattern PosTagFilterPattern = null;
  private Matcher PosTagFilterMatcher = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient StringTokenizer Tokenizer = null;
  private transient String TmpString = "";
  private transient String TmpString2 = "";
    
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Find Collocations: Freq./POS-Filter";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter"
  + ".FindCollocationsFrequencyPosFilterParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter"
  + ".FindCollocationsFrequencyPosFilterResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.findCollocationsFrequencyPosFilter"
  + ".FindCollocationsFrequencyPosFilterControlPanel";
  
  private final static KProperty[] PROJECT_PROPERTY_DATA = {
    new KProperty("MIN_TOKENS_IN_COLLOCATION", 
    "Minimum Number (0, 1, ...) of Tokens in Collocations",
    "2", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("MAX_TOKENS_IN_COLLOCATION", 
    "Maximum Number (1, 2, ...) of Tokens in Collocations",
    "3", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("MIN_COLLOCATION_FREQUENCY", 
    "Minimum Frequency of Valid Collocations in Collection",
    "25", KProperty.INTEGER, KProperty.EDITABLE),
    new KProperty("COLLOCATION_POS_FILTER_REGEX", 
    "Regular Expression that Matches Language-Specific POS Tags",
    "(JJ NN|NN NN|JJ JJ NN|JJ NN NN|NN JJ NN|NN NN NN|NN IN NN)", 
    KProperty.STRING, KProperty.EDITABLE),
    new KProperty("COLLOCATIONS_FILE_NAME", 
    "Name of Output File for Extracted Collocations",
    "", KProperty.STRING, KProperty.EDITABLE)
  };

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public FindCollocationsFrequencyPosFilterTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    ProjectPropertyData = PROJECT_PROPERTY_DATA;
    
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
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface NonBlockingTask methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public AbstractValidatedTaskParameter validateTaskParameter(
  Project pProject, TaskParameter pParameter) {
    
    FindCollocationsFrequencyPosFilterParameter parameter = null;
    if (pParameter instanceof FindCollocationsFrequencyPosFilterParameter) {
      parameter = (FindCollocationsFrequencyPosFilterParameter)pParameter;
    }
    else {
      return null;
    }
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    File file = new File(parameter.getCollectionFileName());
    if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
    .endsWith(DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION +
      "-file in the field 'Collection File'!");
    }    
    if (parameter.getParserFileName().trim().length() <= 0
    || !parameter.getParserFileName().trim().endsWith(
    GuiClientPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      GuiClientPreferences.TEXT_FILE_EXTENSION +
      "-file name\nin the field 'TreeTagger Input File'!");
    }
    if (parameter.getTaggedFileName().trim().length() <= 0
    || !parameter.getTaggedFileName().trim().endsWith(
    GuiClientPreferences.TEXT_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      GuiClientPreferences.TEXT_FILE_EXTENSION +
      "-file name\nin the field 'TreeTagger Output File'!");
    }
    
    if (parameter.getMinTokensInCollocation() <= 0) {
      result.addError(
      "Error: Please enter a positive, non-zero integer\n" +
      "in the field 'Min. Tokens in Collocation'!");
    }
    if (parameter.getMaxTokensInCollocation() <= 0) {
      result.addError(
      "Error: Please enter a positive, non-zero integer\n" +
      "in the field 'Max. Tokens in Collocation'!");
    }
    if (parameter.getMaxTokensInCollocation() < parameter
    .getMinTokensInCollocation()) {
      result.addError(
      "Error: 'Min. Tokens in Collocation' must be less\n" +
      "than or equal 'Max. Tokens in Collocation'!");
    }
    if (parameter.getMinCollocationFrequency() <= 0) {
      result.addError(
      "Error: Please enter a positive, non-zero integer\n" +
      "in the field 'Min. Collocation Frequency'!");
    }
    try {
      Pattern collocationPosFilterRegex = Pattern.compile(
      parameter.getCollocationPosFilterRegex());
    }
    catch (PatternSyntaxException e) {
      result.addError(
      "Error: The regular expression's syntax entered in\n" +
      "the field 'Collcation POS Tag Filter' is invalid!\n" +
      "Please see System.err for detailed error messages.");
      System.err.println(e.getMessage());
    }
    if (parameter.getCollocationsFileName().trim().length() <= 0
    || !parameter.getCollocationsFileName().trim().endsWith(
    DIAsDEMguiPreferences.TF_STATISTICS_FILE_EXTENSION)) {
      result.addError(
      "Error: Please enter a valid local " +
      DIAsDEMguiPreferences.TF_STATISTICS_FILE_EXTENSION +
      "-file name\nin the field 'Collocations File'!");
    }
    
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new FindCollocationsFrequencyPosFilterParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new FindCollocationsFrequencyPosFilterResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_UNDERSTAND_DOMAIN,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED,
    this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null && Parameter instanceof 
    FindCollocationsFrequencyPosFilterParameter) {
      CastParameter = (FindCollocationsFrequencyPosFilterParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Error: Text units cannot be lemmatized!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
        
    TextFile parserFile = new TextFile( 
      new File(CastParameter.getParserFileName()));
    parserFile.empty();
    parserFile.open();

    int counterProgress = 1;
    long maxProgress = 2 * DiasdemCollection.getNumberOfDocuments();

    AllCollocations = new DefaultDIAsDEMthesaurus();
    PosTagFilterPattern = Pattern.compile(CastParameter
    .getCollocationPosFilterRegex());
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Step 1/3: Exporting Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      
      parserFile.setNextLine("<Document_" + DiasdemDocument
      .getDiasdemDocumentID() + ">");
      
      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits(); 
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        parserFile.setNextLine("<ProcessedTextUnit_" + i + ">");
        parserFile.setNextLine(DiasdemTextUnit.getContentsAsString());
        parserFile.setNextLine("</ProcessedTextUnit_" + i + ">");
      }

      parserFile.setNextLine("</Document_" + DiasdemDocument
      .getDiasdemDocumentID() + ">");
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents

    parserFile.close();

    System.out.println("### Start of POS Tagging using TreeTagger ...");
    Progress.update( TaskProgress.INDETERMINATE, "Step 2/3: POS Tagging ..."); 
    DiasdemServer.setTaskProgress(Progress, TaskThread);

    TextFile taggedFile = new TextFile(
    new File(CastParameter.getTaggedFileName()));
    taggedFile.empty();
    taggedFile.close();
    
    try {
      Process p = Runtime.getRuntime().exec(CastParameter.getTreeTaggerCommand()
      + " " +  CastParameter.getParserFileName());
      FileOutputStream taggedFileStream = new FileOutputStream(CastParameter
      .getTaggedFileName());
      BufferedOutputStream taggedFileBuffer = new BufferedOutputStream(
      taggedFileStream);
      StreamHandler outputStreamHandler = new StreamHandler(p.getInputStream(), 
      null, taggedFileBuffer);
      StreamHandler errorStreamHandler = new StreamHandler(p.getErrorStream(), 
      null, System.out);
      outputStreamHandler.start();
      errorStreamHandler.start();
      int exitValue = p.waitFor();
      while (!outputStreamHandler.isFinished()) {}
      while (!errorStreamHandler.isFinished()) {}
      taggedFileBuffer.flush();
      taggedFileBuffer.close();
      System.out.flush();
      outputStreamHandler = null;
      errorStreamHandler = null;
      System.out.println("### End of POS Tagging using TreeTagger. ExitValue = "
      + exitValue);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.setErrorTaskResult(100, "Error: Text units cannot be lemmatized!",
      "Error: The document collection cannot be lemmatized,\n" +
      "because TreeTagger fails to execute! See stack trace.");
      taggedFile.close();
      this.stop();
    }
    taggedFile.close();
    
    Progress.update( (int)(counterProgress * 100 / maxProgress), 
      "Step 3/3: Updating Document " + counterProgress); 
    DiasdemServer.setTaskProgress(Progress, TaskThread);

    taggedFile = new TextFile(new File(CastParameter.getTaggedFileName()));
    taggedFile.open();
    DiasdemDocument = null;
   
    int currentDocument = 1;
    boolean validDiasdemDocument = true;
    String taggedLine = taggedFile.getFirstLine();
    String token = "";
    String tag = "";
    String lemma = "";
    while (taggedLine != null) {
      
      //System.out.println(taggedLine);
     if (taggedLine.startsWith("<Document_")) {
       
       if (currentDocument == 1 || (currentDocument % 50) == 0) {
         Progress.update( (int)(counterProgress * 100 / maxProgress), 
           "Step 3/3: Processing Document " + currentDocument); 
         DiasdemServer.setTaskProgress(Progress, TaskThread);
       }
       counterProgress++; 
       currentDocument++;
       validDiasdemDocument = true;
       
     } else if (taggedLine.startsWith("</ProcessedTextUnit_") ) {
       
       this.findCollocations();

     } else if (taggedLine.startsWith("<ProcessedTextUnit_") ) {
       
       ListOfTokens = new ArrayList();
       ListOfTags = new ArrayList();
       ListOfLemmas = new ArrayList();

     } else if (taggedLine.startsWith(NamedEntity.PLACEHOLDER_PREFIX)) {
       
       ListOfTokens.add(taggedLine.trim());
       ListOfTags.add("NE");
       ListOfLemmas .add(taggedLine.trim());

     } else if (!taggedLine.startsWith("<") && taggedLine.indexOf("\t") >= 0
     && taggedLine.lastIndexOf("\t") > taggedLine.indexOf("\t")) {
       
       // skip other TML tags etc., check for existrence of tabs
       int indexTag = taggedLine.indexOf("\t") + 1;
       int indexLemma  = taggedLine.lastIndexOf("\t") + 1;
       // System.out.println(taggedLine + " " + indexTag + 
       //   " " + indexLemma );
       if ( indexTag > 0 && indexLemma > 0 && 
       indexLemma <= taggedLine.length() ) { 
         token = taggedLine.substring(0, indexTag - 1);
         tag = taggedLine.substring(indexTag, indexLemma - 1);
         lemma = taggedLine.substring(indexLemma);
         if (lemma.startsWith("<unknown>")) {
           lemma = token; // + "_unknown";
         }
         else if (lemma.startsWith("@card@")) {
           lemma = token; // + "_card"; 
         }
         else if (lemma.indexOf("|") >= 0) {
           lemma = token; // + "_|"; 
         }
         else if (tag.startsWith("NE")) {
           lemma = token; // + "_NE"; 
         }
         ListOfTokens.add(token);
         ListOfTags.add(tag);
         ListOfLemmas .add(lemma);
         // System.out.println("TOKEN=" + token + " TAG=" + 
         //   tag + " LEMMA=" + lemma + " --  ");
         
       }
       
     }      
     taggedLine = taggedFile.getNextLine();
     
   }

   taggedFile.close();    
   
    Progress = new AbstractTaskProgress(TaskProgress.INDETERMINATE,
    "Sorting and saving collocations ...");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    // filter collocation candidates to ensure min. frequency
    ValidCollocations = new DefaultDIAsDEMthesaurus();
    DIAsDEMthesaurusTerm collocation = AllCollocations.getFirstTerm();
    while (collocation != null) {
      if (collocation.getOccurrences() >= CastParameter
      .getMinCollocationFrequency()) {
        ValidCollocations.add(collocation);
        // System.out.println(collocation.getOccurrences() +
        // " : " + collocation.getWord());
      }
      collocation = AllCollocations.getNextTerm();
    }
    
    // save valid collocation as TF statistics file
    ValidCollocations.setOrderOccurrencesWordsDesc();
    ValidCollocations.save(CastParameter.getCollocationsFileName());
    // create HTML and multiterm text file
    if (CastParameter.exportCollocationsInTxtFormat()) {
      this.exportCollocationsInTxtFormat(); 
    }
    if (CastParameter.exportCollocationsInHtmlFormat()) {
      this.exportCollocationsInHtmlFormat(); 
    }
    
    super.closeDiasdemCollection();

    Result.update(TaskResult.FINAL_RESULT, ValidCollocations.getSize() +
    " collocations have been extracted from the collection\n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) + "!\n" +
    "Use 'Tools -> Term Frequency Statistics Viewer' to open\n" +
    Tools.shortenFileName(CastParameter.getCollocationsFileName(), 50) + ".");
    this.setTaskResult(100, "All Documents Processed ...", Result,
    TaskResult.FINAL_RESULT, Task.TASK_FINISHED);
    
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
  
  private void findCollocations() {
    
    // create all n-grams for current text unit
    int minN = CastParameter.getMinTokensInCollocation();
    int maxN = CastParameter.getMaxTokensInCollocation();
    StringBuffer nGramOfTokensBuffer = null;
    String nGramOfTokens = null;
    StringBuffer nGramOfTagsBuffer = null;
    String nGramOfTags = null;
    for (int currentN = minN; currentN <= maxN; currentN++) {
      for (int i = 0; i < ListOfTokens.size() - currentN; i++) {
        nGramOfTokensBuffer = new StringBuffer(1000);
        nGramOfTagsBuffer = new StringBuffer(1000);
        for (int j = 0; j < currentN; j++) {
          if (j > 0) {
            nGramOfTokensBuffer.append(" ");
            nGramOfTagsBuffer.append(" ");
          }
          nGramOfTokensBuffer.append((String)ListOfTokens.get(i + j));
          nGramOfTagsBuffer.append((String)ListOfTags.get(i + j));
        }
        // filter all n-grams according to specified POS tag regex
        PosTagFilterMatcher = PosTagFilterPattern.matcher(
        nGramOfTagsBuffer.toString());
        if (PosTagFilterMatcher.matches()) {
          AllCollocations.countOccurrence(nGramOfTokensBuffer.toString());
          // System.out.println(nGramOfTokensBuffer.toString());
        }
      }
    }  // k++ -> 1-gram, 2-gram, 3-gram
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private void exportCollocationsInTxtFormat() {
    
    TextFile txtFile = null;
    txtFile = new TextFile( new File(Tools.removeFileExtension(
    CastParameter.getCollocationsFileName()) + ".txt"));
    txtFile.open();
    txtFile.setFirstLine(
    "# After editing, this file can be used as 'Multi-Token Words File'");
    txtFile.setNextLine(
    "# in the plug-in 'Tasks -> Prepare Data Set -> Tokenize Text Units'.");
    txtFile.setNextLine("#");
    DIAsDEMthesaurusTerm collocation = ValidCollocations.getFirstTerm();
    while (collocation != null) {
      txtFile.setNextLine("# " + collocation.getOccurrences() + 
      " occurrences:");
      txtFile.setNextLine(collocation.getWord());
      collocation = ValidCollocations.getNextTerm();
    }
    txtFile.close();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void exportCollocationsInHtmlFormat() {
    
    Template DebuggingOutputHeader = new Template(Tools
    .stringFromTextualSystemResource("html/"
    + "HtmlFile_HeaderTemplate.html"));
    DebuggingOutputHeader.addValue("${Title}", "Frequent Collocations");
    TextFile htmlFile =  new TextFile( new File(Tools.removeFileExtension(
    CastParameter.getCollocationsFileName()) + ".html"));
    htmlFile.open();
    htmlFile.setFirstLine(DebuggingOutputHeader.insertValues());
    htmlFile.setNextLine("<p>Created by Tools &gt; Understand Domain " +
    "&gt; Find Collocations: Freq./POS-Filter on " + Tools.getSystemDate() +
    "</p>");
    htmlFile.setNextLine("<h3>Parameter Settings</h3>");
    htmlFile.setNextLine(
    "<table border=\"1\"><tr>" +
    "<th align=\"left\" valign=\"top\">Parameter</th>" +
    "<th align=\"left\" valign=\"top\">Value</th></tr>");
    htmlFile.setNextLine("<tr>" +
    "<td align=\"left\" valign=\"top\">Collection File</td>" +
    "<td align=\"left\" valign=\"top\">" + CastParameter
    .getCollectionFileName() + "</td></tr>");
    htmlFile.setNextLine("<tr>" +
    "<td align=\"left\" valign=\"top\">Min. Tokens in Collocation</td>" +
    "<td align=\"left\" valign=\"top\">" + CastParameter
    .getMinTokensInCollocation() + "</td></tr>");
    htmlFile.setNextLine("<tr>" +
    "<td align=\"left\" valign=\"top\">Max. Tokens in Collocation</td>" +
    "<td align=\"left\" valign=\"top\">" + CastParameter
    .getMaxTokensInCollocation() + "</td></tr>");
    htmlFile.setNextLine("<tr>" +
    "<td align=\"left\" valign=\"top\">Min. Collocation Frequency</td>" +
    "<td align=\"left\" valign=\"top\">" + CastParameter
    .getMinCollocationFrequency() + "</td></tr>");
    htmlFile.setNextLine("<tr>" +
    "<td align=\"left\" valign=\"top\">Collocation POS Tag Filter</td>" +
    "<td align=\"left\" valign=\"top\">" + CastParameter
    .getCollocationPosFilterRegex() + "</td></tr>");
    htmlFile.setNextLine("<tr>" +
    "<td align=\"left\" valign=\"top\">Collocations File</td>" +
    "<td align=\"left\" valign=\"top\">" + CastParameter
    .getCollocationsFileName() + "</td></tr></table>");
    htmlFile.setNextLine("<h3>Results</h3>");
    htmlFile.setNextLine(
    "<table border=\"1\"><tr>" +
    "<th align=\"right\" valign=\"top\">No.</th>" +
    "<th align=\"left\" valign=\"top\">Collocation</th>" +
    "<th align=\"right\" valign=\"top\">Abs. Frequency</th></tr>");
    int no = 1;
    DIAsDEMthesaurusTerm collocation = ValidCollocations.getFirstTerm();
    while (collocation != null) {
      htmlFile.setNextLine("<tr>" +
      "<td align=\"right\" valign=\"top\">" + no + "</td>" + 
      "<td align=\"left\" valign=\"top\">" + collocation.getWord() + "</td>" +
      "<td align=\"right\" valign=\"top\">" + collocation.getOccurrences() + 
      "</td></tr>");
      collocation = ValidCollocations.getNextTerm();
      no++;
    }
    htmlFile.setNextLine("</table>");
    htmlFile.setNextLine(Tools.stringFromTextualSystemResource(
    "html/HtmlFile_FooterTemplate.html"));
    htmlFile.close();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}