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

package org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.event.*;
import java.text.*;
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
 * @version 2.1.0.4, 30 November 2003
 * @author Karsten Winkler
 */

public class ComputeTermFrequencyStatisticsTask extends DiasdemScriptableNonBlockingTask {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private ComputeTermFrequencyStatisticsParameter CastParameter = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
    
  private transient StringBuffer LemmaBuffer = null;
  private transient DIAsDEMthesaurus MyThesaurus = null;
  private transient TextFile CsvExportLemmaFile = null;
  private transient TextFile CsvExportFile = null;
  private transient TextFile HtmlTextUnitsFile = null;
  private transient int CsvExportMaxTextLength = 0;
  private transient int NumberOfTerms = 0;
  private transient NumberFormat DoubleFormatRelFreq = null;
  
  private transient Pattern ConditionRegexMatchesTextUnit = null;
  private transient Matcher ConditionMatcherTextUnit = null;
  
  private transient DIAsDEMthesaurus DescriptorThesaurus = null;  
  private transient DIAsDEMthesaurusTerm Term = null;
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient String TmpString = null;
  private transient StringTokenizer TmpStringTokenizer = null;
  private transient String TmpToken = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private final static String LABEL =
  "Compute Term Frequency Statistics";
  private final static String TASK_PARAMETER_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics" +
  ".ComputeTermFrequencyStatisticsParameter";
  private final static String TASK_RESULT_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics" +
  ".ComputeTermFrequencyStatisticsResult";
  private final static String CONTROL_PANEL_CLASS_NAME =
  "org.hypknowsys.diasdem.tasks.understand.computeTermFrequencyStatistics" +
  ".ComputeTermFrequencyStatisticsControlPanel";
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public ComputeTermFrequencyStatisticsTask() {
    
    super();
    
    Label = LABEL;
    TaskParameterClassName = TASK_PARAMETER_CLASS_NAME;
    TaskResultClassName = TASK_RESULT_CLASS_NAME;
    ControlPanelClassName = CONTROL_PANEL_CLASS_NAME;
    
    DoubleFormatRelFreq = NumberFormat.getNumberInstance(Locale.US);
    DoubleFormatRelFreq.setMinimumFractionDigits(10);
    DoubleFormatRelFreq.setMinimumIntegerDigits(1);
    
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
    
    ComputeTermFrequencyStatisticsParameter parameter = (ComputeTermFrequencyStatisticsParameter)pParameter;
    AbstractValidatedTaskParameter result =
    new AbstractValidatedTaskParameter(parameter);
    
    File file = new File( parameter.getCollectionFileName() );
    if ( (! file.exists()) || (! file.isFile())
    || (! file.getAbsolutePath().endsWith(
    DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION))) {
      result.addError(
      "Please enter the name of an existing local\n" +
      DIAsDEMguiPreferences.COLLECTION_FILE_EXTENSION +
      "-file in the field 'Collection File'!");
    }
    if (parameter.getTfStatisticsFileName().length() <= 0
    || (! parameter.getTfStatisticsFileName()
    .endsWith(DIAsDEMguiPreferences.TF_STATISTICS_FILE_EXTENSION))) {
      result.addError(
      "Please enter a valid local " +
      DIAsDEMguiPreferences.TF_STATISTICS_FILE_EXTENSION + "-file name\n" +
      "in the field 'TF Statistics File'!");
    }
    file = new File(parameter.getTfStatisticsFileName());
    if (file.exists()) {
      result.addWarning(
      "Warning: The file specified in the field\n" +
      "'TF Statistics File' currently exists.\n" +
      "Do you really want to replace this file?");
    }
    
    if (parameter.computeConditionalFrequencies()) {

      if (parameter.getConditionTextUnitMatchedByRegex() != null
      && parameter.getConditionTextUnitMatchedByRegex().length() > 0
      &&!Tools.isSyntacticallyCorrectRegex(parameter
      .getConditionTextUnitMatchedByRegex())) {
        result.addError(
        "Error: Please enter a syntactically correct regular\n" +
        "expression in the field 'Text Unit is Matched by Regex:'!");
      }
      if (parameter.getConditionClusterIdOfTextUnit() !=
      ComputeTermFrequencyStatisticsParameter.UNSPECIFIED_INT_CONDITION
      && parameter.getConditionClusterIdOfTextUnit() < 0) {
        result.addError(
        "Please enter a valid integer >= 0 in the\n" +
        "field 'Cluster ID of Text Unit Equals'!");
      }
      if (parameter.getConditionClusterIdOfTextUnit() !=
      ComputeTermFrequencyStatisticsParameter.UNSPECIFIED_INT_CONDITION
      && parameter.getConditionIterationOfTextUnit() < 1) {
        result.addError(
        "Please enter a valid integer > 0 in the\n" +
        "field 'Iteration of Text Unit Equals'!");
      }
            
    }
    
    if (parameter.mapTokensOntoDescriptors()) {
      
      file = new File(parameter.getThesaurusFileName());
      if (!file.exists() || !file.isFile() || !file.getAbsolutePath()
      .endsWith(DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION)) {
        result.addError(
        "Error: Please enter the name of an existing local\n" +
        DIAsDEMguiPreferences.THESAURUS_FILE_EXTENSION +
        "-file in the field 'Thesaurus File'!");
      }
      
    }

    if (parameter.computeConditionalFrequencies()
    || parameter.mapTokensOntoDescriptors()) {
      
      if (parameter.exportStatisticsInHtmlFormat()) {
        result.addWarning(
        "Warning: Exporting the TF statistics involves\n"
        + "many write operations on HTML files, which are\n"
        + "often monitored by virus scanners. Hence, virus\n"
        + "scanners should be temporarily shut down to en-\n"
        + "sure a high performance quality evaluation. Note:\n"
        + "Warnings like this one can be disabled in the\n"
        + "Tools -> Options dialog. Do you want to proceed?");
      }
      
    }

    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskParameter getDefaultTaskParameter(Server pServer,
  Project pProject) {
    
    return new ComputeTermFrequencyStatisticsParameter();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public TaskResult getDefaultTaskResult(Server pServer, Project pProject) {
    
    return new ComputeTermFrequencyStatisticsResult();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public KMenuItem getKMenuItem() {
    
    return new KMenuItem(LABEL, 0,
    org.hypknowsys.diasdem.client.gui.DIAsDEMguiMenuBar.ACTIONS_UNDERSTAND_DOMAIN,
    null, 0, 0, GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.PROJECT_OPENED_NO_TASK_RUNNING,
    GuiClient.GUI_STARTED_NO_PROJECT_OPENED, this.getClassName());
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface Runnable methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void run() {
    
    if (Parameter != null 
    && Parameter instanceof ComputeTermFrequencyStatisticsParameter) {
      CastParameter = (ComputeTermFrequencyStatisticsParameter)Parameter;
    }
    else {
      CastParameter = null;
    }

    String shortErrorMessage = "Term frequency statistics cannot be computed!";
    this.acceptTask(TaskProgress.INDETERMINATE, "Initial Preparations");
    this.validateParameter(Parameter, shortErrorMessage);
    this.openDiasdemCollection(CastParameter.getCollectionFileName());
    this.checkPrerequisitesAndSetDefaultTextUnitsLayer(shortErrorMessage);
    
    if (CastParameter.exportTextsInCsvFormat()) {
      CsvExportFile = new TextFile( new File( Tools.removeFileExtension(
      CastParameter.getTfStatisticsFileName() ) + ".orig.csv") );
      CsvExportFile.open();
      CsvExportFile.setFirstLine("FileName,Text,TextLength");
      CsvExportLemmaFile = new TextFile(new File(Tools.removeFileExtension(
      CastParameter.getTfStatisticsFileName()) + ".proc.csv"));
      CsvExportLemmaFile.open();
      CsvExportLemmaFile.setFirstLine("FileName,Text,TextLength");
    }
    LemmaBuffer = new StringBuffer();
    StringTokenizer tokenizer = null;
    String token = null;
    StringTokenizer lemmaTokenizer = null;
    String lemmaToken = null;    
    
    if (CastParameter.computeConditionalFrequencies()) {
      MyThesaurus = new DefaultDIAsDEMthesaurus(
      "Conditional Term Frequency Statistics, " +
      "Collection: " + CastParameter.getCollectionFileName(), 100000);
      if (!(CastParameter.getConditionTextUnitMatchedByRegex() == null
      || CastParameter.getConditionTextUnitMatchedByRegex().length() == 0)) {
        ConditionRegexMatchesTextUnit = Pattern.compile(
        CastParameter.getConditionTextUnitMatchedByRegex());
      }
    }  
    else {
      MyThesaurus = new DefaultDIAsDEMthesaurus(
      "Term Frequency Statistics, Collection: "
      + CastParameter.getCollectionFileName(), 100000);
    }
    
    if (CastParameter.mapTokensOntoDescriptors()) {    
      DescriptorThesaurus = new DefaultDIAsDEMthesaurus();
      DescriptorThesaurus.load(CastParameter.getThesaurusFileName());
      if (CastParameter.getVectorDimensions() !=
      ComputeTermFrequencyStatisticsParameter.ALL_DESCRIPTORS) {
        ArrayList removeTerms = new ArrayList(DescriptorThesaurus.getSize());
        Term = DescriptorThesaurus.getFirstTerm();
        while (Term != null) {
          if (CastParameter.getVectorDimensions() ==
          ComputeTermFrequencyStatisticsParameter.SPECIFIED_DESCRIPTORS
          && Term.getWord().indexOf(CastParameter
          .getDescriptorsScopeNotesContain()) == 0) {
            removeTerms.add(Term.getWord());
          }
          else if (CastParameter.getVectorDimensions() ==
          ComputeTermFrequencyStatisticsParameter.NOT_SPECIFIED_DESCRIPTORS
          && Term.getWord().indexOf(CastParameter
          .getDescriptorsScopeNotesContain()) >= 0) {
            removeTerms.add(Term.getWord());
          }
          Term = DescriptorThesaurus.getNextTerm();
        }
        Iterator iterator = removeTerms.iterator();
        // remove all text unit descriptors that do not match specified criteria
        while (iterator.hasNext()) {
          DescriptorThesaurus.delete((String)iterator.next());
        }
      }
    }
    else {
      DescriptorThesaurus = null;
    }
    
    if (CastParameter.exportStatisticsInHtmlFormat()
    && (CastParameter.computeConditionalFrequencies() 
    || CastParameter.mapTokensOntoDescriptors())) {
      this.createHtmlTextUnitsFileHeader();
    }
    
    int counterProgress = 0;
    long maxProgress = DiasdemCollection.getNumberOfDocuments();
    
    DiasdemDocument = DiasdemCollection.getFirstDocument();
    while (DiasdemDocument != null) {
      
      if (counterProgress == 1 || (counterProgress % 50) == 0) {
        Progress.update( (int)(counterProgress * 100 / maxProgress),
        "Processing Document " + counterProgress);
        DiasdemServer.setTaskProgress(Progress, TaskThread);
      }
      
      DiasdemDocument.setActiveTextUnitsLayer(DiasdemProject
      .getActiveTextUnitsLayerIndex());
      // backup is not necessary, read-only task

      for (int i = 0; i < DiasdemDocument.getNumberOfProcessedTextUnits(); 
      i++) {
        DiasdemTextUnit = DiasdemDocument.getProcessedTextUnit(i);
        TextUnitContentsAsString = DiasdemTextUnit.getContentsAsString();
        if (!CastParameter.computeConditionalFrequencies() 
        || this.meetsConditions(DiasdemTextUnit)) {
          tokenizer = new StringTokenizer(TextUnitContentsAsString);
          while (tokenizer.hasMoreElements()) {
            token = tokenizer.nextToken();
            NumberOfTerms++;
            if (!CastParameter.excludeNumbersEtc() 
            || (!NamedEntity.isPlaceholder(token) 
            && Tools.stringContainsLetter(token))) {
              if (DescriptorThesaurus == null) {
                // do not map tokens onto text unit descriptors
                MyThesaurus.countOccurrence(token);
              }
              else {
                // if possible, map tokens onto text unit descriptors
                Term = DescriptorThesaurus.getDescriptorTerm(token);
                if (Term != null) {
                  MyThesaurus.countOccurrence(Term.getWord());
                }
                else {
                  MyThesaurus.countOccurrence(token);
                }
              }
            }
          }
          if (CastParameter.exportStatisticsInHtmlFormat()
          && (CastParameter.computeConditionalFrequencies()
          || CastParameter.mapTokensOntoDescriptors())) {
            this.createHtmlTextUnitsFileLine(DiasdemTextUnit,
            DiasdemDocument.getOriginalTextUnit(i));
          }
        }
        if (CastParameter.exportTextsInCsvFormat()) {
          lemmaTokenizer = new StringTokenizer(TextUnitContentsAsString);
          while (lemmaTokenizer.hasMoreElements()) {
            lemmaToken = lemmaTokenizer.nextToken();
            if (!NamedEntity.isPlaceholder(lemmaToken)
            && Tools.stringContainsLetter(lemmaToken)) {
              LemmaBuffer.append(lemmaToken);
              LemmaBuffer.append(" ");
            }
          }
        }
      }
      this.exportTextsInCsvFormat();
      
      DiasdemDocument = DiasdemCollection.getNextDocument();
      counterProgress++;
      
    }  // read all documents
    
    Progress = new AbstractTaskProgress(TaskProgress.INDETERMINATE,
    "Sorting and saving TF statistics ...");
    DiasdemServer.setTaskProgress(Progress, TaskThread);
    
    MyThesaurus.setOrderOccurrencesWordsDesc();
    MyThesaurus.save(CastParameter.getTfStatisticsFileName());
    if (CastParameter.exportStatisticsInCsvFormat()) {
      this.exportStatisticsInCsvFormat();
    }
    
    if (CastParameter.exportTextsInCsvFormat()) {
      CsvExportLemmaFile.close();
      CsvExportFile.close();
    }
    if (CastParameter.exportStatisticsInHtmlFormat()) {
      this.exportStatisticsInHtmlFormat();
      if (CastParameter.computeConditionalFrequencies()
      || CastParameter.mapTokensOntoDescriptors()) {
        this.createHtmlTextUnitsFileFooter();
      }
    }
    
    super.closeDiasdemCollection();
    
    Result.update(TaskResult.FINAL_RESULT,
    "The term frequency statistics of the DIAsDEM collection\n" +
    Tools.shortenFileName(CastParameter.getCollectionFileName(), 50) +
    "\nhave been successfully computed and stored in the file\n" +
    Tools.shortenFileName(CastParameter.getTfStatisticsFileName(), 50)  + "!" +
    (CastParameter.exportStatisticsInCsvFormat() ? "\nThe maximum " +
    "length of exported texts is " + CsvExportMaxTextLength + " bytes.": ""),
    "Term frequency statistics computed");
    this.setTaskResult(100, "All Documents Processed ...", Result,
    TaskResult.FINAL_RESULT);
    
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
  
  private void exportTextsInCsvFormat() {
    
    if (DiasdemDocument != null && CastParameter.exportTextsInCsvFormat()) {
      
      TmpString = DiasdemDocument.getOriginalText().replace('\"', ' ')
      .replace('\n', ' ').trim();
      CsvExportMaxTextLength = Math.max(CsvExportMaxTextLength, 
      TmpString.length());
      CsvExportFile.setNextLine(
      "\"" +
      (String)DiasdemDocument.getMetaData().get("SourceFile") +
      "\",\"" +
      TmpString +
      "\"," +
      TmpString.length());
      
      TmpString = LemmaBuffer.toString().replace('\"', ' ')
      .replace('\n', ' ').trim();
      CsvExportMaxTextLength = Math.max(CsvExportMaxTextLength, 
      TmpString.length());
      CsvExportLemmaFile.setNextLine(
      "\"" +
      (String)DiasdemDocument.getMetaData().get("SourceFile") +
      "\",\"" +
      TmpString + 
      "\"," +
      TmpString.length());
      LemmaBuffer = new StringBuffer();
      
    }

  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void exportStatisticsInHtmlFormat() {
    
    Template DebuggingOutputHeader = new Template(Tools
    .stringFromTextualSystemResource("org/hypknowsys/diasdem/resources/html/"
    + "HtmlFile_HeaderTemplate.html"));
    DebuggingOutputHeader.addValue("${Title}", "Term Frequency Statistics");
    TextFile htmlFile =  new TextFile( new File(Tools.removeFileExtension(
    CastParameter.getTfStatisticsFileName()) + ".html"));
    htmlFile.open();
    htmlFile.setFirstLine(DebuggingOutputHeader.insertValues());
    htmlFile.setNextLine("<p>Created by Tools &gt; Understand Domain " +
    "&gt; Compute Term Frequency Statistics on " + Tools.getSystemDate() +
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
    "<td align=\"left\" valign=\"top\">TF Statistics File</td>" +
    "<td align=\"left\" valign=\"top\">" + CastParameter
    .getTfStatisticsFileName() + "</td></tr>");
    if (CastParameter.mapTokensOntoDescriptors()) {
      htmlFile.setNextLine("<tr>" +
      "<td align=\"left\" valign=\"top\">Thesaurus File</td>" +
      "<td align=\"left\" valign=\"top\">" + CastParameter
      .getThesaurusFileName() + "</td></tr>");
      htmlFile.setNextLine("<tr>" +
      "<td align=\"left\" valign=\"top\">Text Unit Descriptors</td>" +
      "<td align=\"left\" valign=\"top\">" + 
      ComputeTermFrequencyStatisticsParameter.VECTOR_DIMENSIONS_OPTIONS[
      CastParameter.getVectorDimensions()] + " " + CastParameter
      .getDescriptorsScopeNotesContain() + "</td></tr>");
    }
    htmlFile.setNextLine("</table>");
    
    htmlFile.setNextLine("<h3>Results</h3>");
    if (CastParameter.computeConditionalFrequencies()) {
      htmlFile.setNextLine("<p>Conditional term frequency statistics: ");
      if (!(CastParameter.getConditionTextUnitMatchedByRegex() == null
      || CastParameter.getConditionTextUnitMatchedByRegex().length() == 0)) {
        htmlFile.setNextLine("The term &quot;" + CastParameter
        .getConditionTextUnitMatchedByRegex() + "&quot; must occur in the text unit.");
      }
      if (CastParameter.getConditionClusterIdOfTextUnit() !=
      ComputeTermFrequencyStatisticsParameter.UNSPECIFIED_INT_CONDITION) {
        htmlFile.setNextLine("The text unit must have been assigned to a "
        + "cluster whose numeric identifier equals " + CastParameter
        .getConditionClusterIdOfTextUnit() + ".");
      }
      if (!(CastParameter.getConditionClusterLabelOfTextUnit() == null
      || CastParameter.getConditionClusterLabelOfTextUnit().length() == 0)) {
        htmlFile.setNextLine("The text unit must have been assigned to a "
        + "cluster whose semantic label equals &quot;" + CastParameter
        .getConditionClusterLabelOfTextUnit() + "&quot;.");
      }
      if (CastParameter.getConditionIterationOfTextUnit() !=
      ComputeTermFrequencyStatisticsParameter.UNSPECIFIED_INT_CONDITION) {
        htmlFile.setNextLine("The text unit must have been assigned to a "
        + "cluster in clustering iteration " + CastParameter
        .getConditionIterationOfTextUnit() + ".");
      }
      htmlFile.setNextLine("All matching text units are listed in the file "
      + " <a href=\"" + Tools.removeDirectory(Tools.removeFileExtension(
      CastParameter.getTfStatisticsFileName()) + ".textunits.html") +  "\">" 
      + Tools.removeDirectory(Tools.removeFileExtension(CastParameter
      .getTfStatisticsFileName()) + ".textunits.html") + "</a>. "
      + "Alltogether, these processed text units comprise " + NumberOfTerms 
      + " terms and " + MyThesaurus.getSize() 
      + " distinct terms, respectively.</p>");
    } 
    else if (CastParameter.mapTokensOntoDescriptors()) {
      htmlFile.setNextLine("<p>All text units are listed in the file "
      + " <a href=\"" + Tools.removeDirectory(Tools.removeFileExtension(
      CastParameter.getTfStatisticsFileName()) + ".textunits.html") +  "\">" 
      + Tools.removeDirectory(Tools.removeFileExtension(CastParameter
      .getTfStatisticsFileName()) + ".textunits.html") + "</a>. "
      + "Alltogether, these processed text units comprise " + NumberOfTerms 
      + " terms and " + MyThesaurus.getSize() 
      + " distinct terms, respectively.</p>");
    }
    else {
      htmlFile.setNextLine("<p>Alltogether, the collection comprises " + 
      NumberOfTerms + " terms and " + MyThesaurus.getSize() + 
      " distinct terms, respectively.</p>");
    }
    htmlFile.setNextLine("<table border=\"1\"><tr>" +
    "<th align=\"right\" valign=\"top\">Rank</td>" +
    "<th align=\"left\" valign=\"top\">Term</td>" +
    "<th align=\"right\" valign=\"top\">Abs. Term Frequency</td>" +
    "<th align=\"right\" valign=\"top\">Rel. Term Frequency</td>" +
    "<th align=\"right\" valign=\"top\">log<sub>10</sub>(Rank)</td></tr>");
    DIAsDEMthesaurusTerm term = MyThesaurus.getFirstTerm();
    int rank = 1;
    while (term != null) {
      htmlFile.setNextLine("<tr>" +
      "<td align=\"right\" valign=\"top\">" + rank + "</td>" +
      "<td align=\"left\" valign=\"top\">" + term.getWord() + "</td>" +
      "<td align=\"right\" valign=\"top\">" + term.getOccurrences() + "</td>" +
      "<td align=\"right\" valign=\"top\">" + DoubleFormatRelFreq
      .format(((double)term.getOccurrences() / NumberOfTerms)) + "</td>" +
      "<td align=\"right\" valign=\"top\">" + DoubleFormatRelFreq
      .format(Tools.log(10, (double)rank)) + "</td></tr>");
      term = MyThesaurus.getNextTerm();
      rank++;
    }
    htmlFile.setNextLine("</table>");
    htmlFile.setNextLine(Tools.stringFromTextualSystemResource(
    "org/hypknowsys/diasdem/resources/html/HtmlFile_FooterTemplate.html"));
    htmlFile.close();
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void createHtmlTextUnitsFileHeader() {
    
    Template DebuggingOutputHeader = new Template(Tools
    .stringFromTextualSystemResource("org/hypknowsys/diasdem/resources/html/"
    + "HtmlFile_HeaderTemplate.html"));
    DebuggingOutputHeader.addValue("${Title}", "Term Frequency Statistics");
    HtmlTextUnitsFile =  new TextFile( new File(Tools
    .removeFileExtension(CastParameter.getTfStatisticsFileName()) 
    + ".textunits.html"));
    HtmlTextUnitsFile.open();
    HtmlTextUnitsFile.setFirstLine(DebuggingOutputHeader.insertValues());
    HtmlTextUnitsFile.setNextLine("<p>Created by Tools &gt; Understand Domain " +
    "&gt; Compute Term Frequency Statistics on " + Tools.getSystemDate() +
    "</p>");
    HtmlTextUnitsFile.setNextLine("<h3>Text Units Taken into Consideration</h3>");
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void createHtmlTextUnitsFileLine(DIAsDEMtextUnit pProcessedTextUnit,
  DIAsDEMtextUnit pOriginalTextUnit) {

    TmpStringBuffer = new StringBuffer(pProcessedTextUnit
    .getContentsAsString().length() + 10000);
    TmpStringTokenizer = new StringTokenizer(pProcessedTextUnit
    .getContentsAsString());
    while (TmpStringTokenizer.hasMoreElements()) {
      TmpToken = TmpStringTokenizer.nextToken();
      if (!CastParameter.excludeNumbersEtc()
      || (!NamedEntity.isPlaceholder(TmpToken)
      && Tools.stringContainsLetter(TmpToken))) {
        if (DescriptorThesaurus == null) {
          // do not map tokens onto text unit descriptors
          TmpStringBuffer.append(TmpToken);
        }
        else {
          // if possible, map tokens onto text unit descriptors
          Term = DescriptorThesaurus.getDescriptorTerm(TmpToken);
          if (Term != null) {
            if (!Term.getWord().equals(TmpToken)) {
              TmpStringBuffer.append("<strike>");
              TmpStringBuffer.append(TmpToken);
              TmpStringBuffer.append("</strike> ");
            }
            TmpStringBuffer.append("<b>");
            TmpStringBuffer.append(Term.getWord());
            TmpStringBuffer.append("</b>");
          }
          else {
            TmpStringBuffer.append(TmpToken);
          }
        }
      }
      else {
        TmpStringBuffer.append(TmpToken);
      }
      TmpStringBuffer.append(" ");
    }
          
    TmpString = new String(TmpStringBuffer.toString().trim());
    if (ConditionRegexMatchesTextUnit != null) {
      // visualize condition
      ConditionMatcherTextUnit = ConditionRegexMatchesTextUnit.matcher(
      TmpString);
      if (ConditionMatcherTextUnit.find()) {
        ConditionMatcherTextUnit.reset();
        TmpStringBuffer = new StringBuffer(10000);
        while (ConditionMatcherTextUnit.find()) {
          ConditionMatcherTextUnit.appendReplacement(TmpStringBuffer,
          "<font class=\"greenBold\">$0</font>");
        }
        TmpString = ConditionMatcherTextUnit.appendTail(TmpStringBuffer)
        .toString();
      }
    }
    
    HtmlTextUnitsFile.setNextLine("<p>" + TmpString + " <br><font class="
    + "\"silver\">" + pOriginalTextUnit.getContentsAsString() + "</font></p>");
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void createHtmlTextUnitsFileFooter() {
    
    HtmlTextUnitsFile.setNextLine(Tools.stringFromTextualSystemResource(
    "org/hypknowsys/diasdem/resources/html/HtmlFile_FooterTemplate.html"));
    HtmlTextUnitsFile.close();
        
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private void exportStatisticsInCsvFormat() {
    
    TextFile csvFile = null;
    csvFile = new TextFile( new File(Tools.removeFileExtension(
    CastParameter.getTfStatisticsFileName()) + ".csv"));
    csvFile.open();
    csvFile.setFirstLine(
    "Rank,Term,AbsTermFrequency,RelTermFrequency,Log10Rank");
    DIAsDEMthesaurusTerm term = MyThesaurus.getFirstTerm();
    int rank = 1;
    while (term != null) {
      csvFile.setNextLine(rank + ",\"" + term.getWord() + "\"," +
      term.getOccurrences() + "," + DoubleFormatRelFreq.format(
      ((double)term.getOccurrences() / NumberOfTerms)) + "," +
      DoubleFormatRelFreq.format(Tools.log(10, (double)rank)));
      term = MyThesaurus.getNextTerm();
      rank++;
    }
    csvFile.close();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  private boolean meetsConditions(DIAsDEMtextUnit pTextUnit) {
   
    if (pTextUnit == null) {
      return false;
    }
    
    boolean result = false;
    boolean test = false;
    if (ConditionRegexMatchesTextUnit != null) {
      ConditionMatcherTextUnit = ConditionRegexMatchesTextUnit.matcher(
      pTextUnit.getContentsAsString());
      if (ConditionMatcherTextUnit.find()) {
        result = (test ? true & result : true);
      }
      else {
        result = false;
      }
      test = true;
    }
    if (CastParameter.getConditionClusterIdOfTextUnit() != 
    ComputeTermFrequencyStatisticsParameter.UNSPECIFIED_INT_CONDITION) {
      if (pTextUnit.getClusterID() == CastParameter
      .getConditionClusterIdOfTextUnit()) {
        result = (test ? true & result : true);
      }
      else {
        result = false;
      }
      test = true;
    }
    if (!(CastParameter.getConditionClusterLabelOfTextUnit() == null
    || CastParameter.getConditionClusterLabelOfTextUnit().length() == 0)) {
      if (CastParameter.getConditionClusterLabelOfTextUnit()
      .equals(pTextUnit.getClusterLabel())) {
        result = (test ? true & result : true);
      }
      else {
        result = false;
      }
      test = true;
    }
    if (CastParameter.getConditionIterationOfTextUnit() != 
    ComputeTermFrequencyStatisticsParameter.UNSPECIFIED_INT_CONDITION) {
      if (pTextUnit.getIteration() == CastParameter
      .getConditionIterationOfTextUnit()) {
        result = (test ? true & result : true);
      }
      else {
        result = false;
      }
      test = true;
    }
    
    if (!test) {
      result = true;
    }
  
    return result;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static void main(String args[]) {}
  
}