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

package org.hypknowsys.diasdem.core.default21;

import java.io.File;
import org.hypknowsys.diasdem.core.DIAsDEMcollection;
import org.hypknowsys.diasdem.core.DIAsDEMdocument;
import org.hypknowsys.diasdem.core.DIAsDEMvolume;
import org.hypknowsys.diasdem.core.DiasdemException;
import org.hypknowsys.misc.io.TextBufferedReader;
import org.hypknowsys.misc.io.TextFile;
import org.hypknowsys.misc.util.KProperties;
import org.hypknowsys.misc.util.KProperty;
import org.hypknowsys.misc.util.Tools;

/**
 * Currently, DiasdemVolumeID = absolute local file name of document
 *
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */

public class DefaultDIAsDEMcollection extends KProperties
implements DIAsDEMcollection {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private DIAsDEMvolume DiasdemVolume = null;
  private String DiasdemVolumeFileName = null;
  private int DiasdemVolumeIndex = 0;
  private int DocumentsPerVolume = 1;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */
  
  private transient StringBuffer TmpStringBuffer = null;
  private transient TextBufferedReader CollectionTextFileReader = null;
  private transient TextFile CollectionTextFile = null;
  private transient File CollectionFile = null;
  private transient DIAsDEMdocument TmpDiasdemDocument = null;
  private transient String TmpVolumeFileName = null;
  private transient String TmpVolumeIndex = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public static final KProperty[] MY_PROPERTY_DATA = {
    new KProperty("COLLECTION_NAME",
    "Collection Name",
    "<DefaultCollectionName>", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("COLLECTION_FILE_NAME",
    "Absolute Path of Collection File",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("COLLECTION_DIRECTORY",
    "Absolute Path of Collection Directory",
    ".", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("COLLECTION_NOTES",
    "Collection Notes",
    "none", KProperty.STRING, KProperty.EDITABLE),
    new KProperty("NUMBER_OF_DOCUMENTS",
    "Number of Documents in the Collection",
    "0", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("NUMBER_OF_TEXT_UNITS",
    "Number of Text Units in the Collection",
    "0", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("NUMBER_OF_UNTAGGED_TEXT_UNITS",
    "Number of Untagged Text Units in the Collection",
    "0", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("NEXT_DIASDEM_VOLUME_ID",
    "Next Valid DIAsDEM Volume ID",
    "100000", KProperty.LONG, KProperty.NOT_EDITABLE),
    new KProperty("DOCUMENTS_FILE_NAME",
    "Absolute Path of File Containing Document IDs",
    "", KProperty.STRING, KProperty.NOT_EDITABLE),
    new KProperty("DOCUMENTS_PER_VOLUME",
    "Number of DIAsDEM documents stored in each DIAsDEM volume",
    "1", KProperty.INTEGER, KProperty.NOT_EDITABLE)
  };
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */
  
  public DefaultDIAsDEMcollection() {
    
    super(MY_PROPERTY_DATA);
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getClassName() {
    return this.getClass().getName(); }
  public String getCollectionName() {
    return this.getStringProperty("COLLECTION_NAME"); }
  public String getCollectionFileName() {
    return this.getStringProperty("COLLECTION_FILE_NAME"); }
  public String getCollectionDirectory() {
    return this.getStringProperty("COLLECTION_DIRECTORY"); }
  public String getCollectionNotes() {
    return this.getStringProperty("COLLECTION_NOTES"); }
  public long getNumberOfDocuments() {
    return this.getLongProperty("NUMBER_OF_DOCUMENTS"); }
  public long getNumberOfTextUnits() {
    return this.getLongProperty("NUMBER_OF_TEXT_UNITS"); }
  public long getNumberOfUntaggedTextUnits() {
    return this.getLongProperty("NUMBER_OF_UNTAGGED_TEXT_UNITS"); }
  public int getDocumentsPerVolume() {
    return this.getIntProperty("DOCUMENTS_PER_VOLUME"); }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setCollectionName(String pCollectionName) {
    this.setProperty("COLLECTION_NAME", pCollectionName); }
  public void setCollectionFileName(String pCollectionFileName) {
    this.setProperty("COLLECTION_FILE_NAME", pCollectionFileName); }
  public void setCollectionDirectory(String pCollectionDirectory) {
    DefaultDIAsDEMvolume.copyDtdFile(pCollectionDirectory);
    this.setProperty("COLLECTION_DIRECTORY", pCollectionDirectory); }
  public void setCollectionNotes(String pCollectionNotes) {
    this.setProperty("COLLECTION_NOTES", pCollectionNotes); }
  public void setNumberOfDocuments(long pNumberOfDocuments) {
    this.setProperty("NUMBER_OF_DOCUMENTS",
    Tools.long2String(pNumberOfDocuments)); }
  public void setNumberOfTextUnits(long pNumberOfTextUnits) {
    this.setProperty("NUMBER_OF_TEXT_UNITS",
    Tools.long2String(pNumberOfTextUnits)); }
  public void setNumberOfUntaggedTextUnits(long pNumberOfTextUnits) {
    this.setProperty("NUMBER_OF_UNTAGGED_TEXT_UNITS",
    Tools.long2String(pNumberOfTextUnits)); }
  public void setDocumentsPerVolume(int pDocumentsPerVolume) {
    DocumentsPerVolume = pDocumentsPerVolume;
    this.setProperty("DOCUMENTS_PER_VOLUME",
    Tools.int2String(pDocumentsPerVolume)); }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public String toString() {
    
    TmpStringBuffer = new StringBuffer(100);
    TmpStringBuffer.append("Document Collection: Name=");
    TmpStringBuffer.append(this.getCollectionName());
    TmpStringBuffer.append("FileName=");
    TmpStringBuffer.append(this.getCollectionFileName());
    
    return TmpStringBuffer.toString();
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface DIAsDEMcollection methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  public void create(String pDiasdemCollectionFileName)
  throws DiasdemException {
    
    try {
      FileName = pDiasdemCollectionFileName;
      this.save(pDiasdemCollectionFileName);
      this.setCollectionFileName(pDiasdemCollectionFileName);
      this.setDocumentsFileName(pDiasdemCollectionFileName + ".files");
      CollectionFile = new File(this.getDocumentsFileName());
      CollectionTextFile = new TextFile(CollectionFile);
      CollectionTextFile.empty();
      CollectionTextFile.open();
      this.setNumberOfDocuments(0L);
      this.setNumberOfTextUnits(0L);
      this.setNumberOfUntaggedTextUnits(0L);
      this.setNextDiasdemVolumeID(100000L);
      DocumentsPerVolume = this.getDocumentsPerVolume();
      DiasdemVolumeFileName = null;
      DiasdemVolumeIndex = 0;
      this.quickSave();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new DiasdemException("DIAsDEM collection "
      + pDiasdemCollectionFileName + "cannot be created! Message: "
      + e.getMessage());
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void open(String pDiasdemCollectionFileName)
  throws DiasdemException {
    
    try {
      if (Tools.isExistingFile(pDiasdemCollectionFileName)) {
        this.load(pDiasdemCollectionFileName);
        FileName = pDiasdemCollectionFileName;
        CollectionFile = new File(this.getDocumentsFileName());
        CollectionTextFile = new TextFile(CollectionFile);
        CollectionTextFile.open();
        DocumentsPerVolume = this.getDocumentsPerVolume();
        DiasdemVolumeFileName = null;
        DiasdemVolumeIndex = 0;
      }
      else {
        throw new DiasdemException("DIAsDEM collection "
        + pDiasdemCollectionFileName + "cannot be opened! Message: "
        + "This file does not exist.");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new DiasdemException("DIAsDEM collection "
      + pDiasdemCollectionFileName + "cannot be opened! Message: "
      + e.getMessage());
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void close() throws DiasdemException {
    
    this.quickSave();
    if (DiasdemVolume != null) {
      this.closeVolume();
    }
    if (CollectionTextFile != null) {
      CollectionTextFile.close();
    }
    if (CollectionTextFileReader != null) {
      CollectionTextFileReader.close();
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String addDocument(DIAsDEMdocument pDiasdemDocument) {
    
    if (pDiasdemDocument == null || CollectionTextFile == null) {
      System.err.println("[DefaultDIAsDEMcollection]"
      + "Document cannot be added, because CollectionTextFile == null.");
      return null;
    }
    
    if (DiasdemVolume == null || DiasdemVolume.getNumberOfDocuments()
    >= DocumentsPerVolume) {
      
      this.closeVolume();
      DiasdemVolumeFileName = Tools.ensureTrailingSlash(
      this.getCollectionDirectory()) + "volume"
      + this.getNextDiasdemVolumeID() + ".xml";
      DiasdemVolume = new DefaultDIAsDEMvolume();
      try {
        DiasdemVolume.create(DiasdemVolumeFileName);
        CollectionTextFile.setNextLine(DiasdemVolumeFileName);
        this.setNextDiasdemVolumeID(this.getNextDiasdemVolumeID() + 1L);
      }
      catch (Exception e) {
        System.err.println("[DefaultDIAsDEMcollection]"
        + "Document cannot be added, because its ID is not valid: "
        + pDiasdemDocument.getDiasdemDocumentID());
        DiasdemVolumeFileName = null;
        DiasdemVolume = null;
      }
      
    }
    
    if (DiasdemVolume != null) {
      pDiasdemDocument.setDiasdemDocumentID(DiasdemVolumeFileName);
      this.setNumberOfDocuments(this.getNumberOfDocuments() + 1L);
      return pDiasdemDocument.getDiasdemDocumentID()
      + ":" + DiasdemVolume.addDocument(pDiasdemDocument);
    }
    else {
      System.err.println("[DefaultDIAsDEMcollection]"
      + "Next document cannot be set, because its ID is not valid: "
      + pDiasdemDocument.getDiasdemDocumentID());
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void replaceDocument(String pDiasdemVolumeID,
  DIAsDEMdocument pDiasdemDocument) {
    
    if (pDiasdemDocument != null && pDiasdemVolumeID != null
    && this.getDocument(pDiasdemVolumeID) != null) {
      // shouls be synchronized; accessing TmpVolumeIndex directly
      DiasdemVolume.replaceDocument(Tools.string2Int(TmpVolumeIndex),
      pDiasdemDocument);
    }
    else {
      System.err.println("[DefaultDIAsDEMcollection]"
      + "Document cannot be set, because its ID is not a valid: "
      + pDiasdemDocument.getDiasdemDocumentID());
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMdocument getFirstDocument() {
    
    if (CollectionTextFileReader != null) {
      CollectionTextFileReader.close();
    }
    CollectionFile = new File(this.getDocumentsFileName());
    CollectionTextFileReader = new TextBufferedReader(CollectionFile);
    CollectionTextFileReader.open();
    
    TmpString = CollectionTextFileReader
    .getFirstLineButIgnoreCommentsAndEmptyLines();
    if (TmpString != null) {
      this.closeVolume();
      DiasdemVolumeFileName = TmpString;
      DiasdemVolumeIndex = 0;
      DiasdemVolume = new DefaultDIAsDEMvolume();
      try {
        DiasdemVolume.open(DiasdemVolumeFileName);
        return DiasdemVolume.getDocument(DiasdemVolumeIndex++);
      }
      catch(Exception e) {
        return null;
      }
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMdocument getNextDocument() {
    
    if (DiasdemVolume != null) {
      TmpDiasdemDocument = DiasdemVolume.getDocument(DiasdemVolumeIndex++);
      if (TmpDiasdemDocument == null) {
        this.openNextDiasdemVolume();
        if (DiasdemVolume != null) {
          DiasdemVolumeIndex = 0;
          return DiasdemVolume.getDocument(DiasdemVolumeIndex++);
        }
        else {
          return null;
        }
      }
      else {
        return TmpDiasdemDocument;
      }
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMdocument getDocument(String pDiasdemVolumeID) {
    
    if (pDiasdemVolumeID != null) {
      
      int separatorIndex = pDiasdemVolumeID.lastIndexOf(':');
      if (separatorIndex <= 0
      || separatorIndex == (pDiasdemVolumeID.length() - 1)) {
        System.err.println("[DefaultDIAsDEMcollection] DiasdemVolumeID "
        + pDiasdemVolumeID + " is not valid!");
        return null;
      }
      TmpVolumeFileName = pDiasdemVolumeID.substring(0, separatorIndex);
      TmpVolumeIndex = pDiasdemVolumeID.substring(separatorIndex + 1,
      pDiasdemVolumeID.length());
      
      if (TmpVolumeFileName.equals(DiasdemVolumeFileName)) {
        return DiasdemVolume.getDocument(Tools.string2Int(TmpVolumeIndex));
      }
      else {
        this.closeVolume();
        DiasdemVolumeFileName = TmpVolumeFileName;
        DiasdemVolume = new DefaultDIAsDEMvolume();
        try {
          DiasdemVolume.open(DiasdemVolumeFileName);
          return DiasdemVolume.getDocument(Tools.string2Int(TmpVolumeIndex));
        }
        catch (Exception e) {
          System.err.println("[DefaultDIAsDEMcollection] DiasdemVolumeID "
          + pDiasdemVolumeID + " is not valid!");
          return null;
        }
      }
      
    }
    else {
      return null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public DIAsDEMdocument instantiateDefaultDiasdemDocument() {
    
    return new DefaultDIAsDEMdocument();
    
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
  
  private long getNextDiasdemVolumeID() {
    return this.getLongProperty("NEXT_DIASDEM_VOLUME_ID"); }
  private String getDocumentsFileName() {
    return this.getStringProperty("DOCUMENTS_FILE_NAME"); }
  
  private void setNextDiasdemVolumeID(long pNextDiasdemVolumeID) {
    this.setLongProperty("NEXT_DIASDEM_VOLUME_ID", pNextDiasdemVolumeID); }
  private void setDocumentsFileName(String pDocumentsFileName) {
    this.setProperty("DOCUMENTS_FILE_NAME", pDocumentsFileName); }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void openNextDiasdemVolume() {
    
    this.closeVolume();
    if (CollectionTextFileReader != null) {
      TmpString = CollectionTextFileReader
      .getNextLineButIgnoreCommentsAndEmptyLines();
    }
    else {
      TmpString = null;
    }
    
    if (TmpString != null) {
      DiasdemVolumeFileName = TmpString;
      DiasdemVolume = new DefaultDIAsDEMvolume();
      try {
        DiasdemVolume.open(DiasdemVolumeFileName);
      }
      catch (Exception e) {
        System.err.println("[DefaultDIAsDEMcollection] DiasdemVolumeID "
        + DiasdemVolumeFileName + " is not valid!");
        DiasdemVolumeFileName = null;
        DiasdemVolume = null;
      }
    }
    else {
      DiasdemVolumeFileName = null;
      DiasdemVolume = null;
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  private void closeVolume() {
    
    if (DiasdemVolume != null) {
      try {
        DiasdemVolume.close();
      }
      catch (Exception e) {
        e.printStackTrace();
        System.err.println("[DefaultDIAsDEMcollection] DiasdemVolumeID "
        + FileName + "cannot be closed! Message: " + e.getMessage());
        DiasdemVolumeFileName = null;
        DiasdemVolume = null;
      }
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