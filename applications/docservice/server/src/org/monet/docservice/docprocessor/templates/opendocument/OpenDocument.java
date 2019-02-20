package org.monet.docservice.docprocessor.templates.opendocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.monet.docservice.core.agent.AgentFilesystem;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.library.LibraryZip;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.templates.DocumentProcessor;
import org.monet.docservice.docprocessor.templates.common.Model;

import com.google.inject.Inject;

public class OpenDocument implements DocumentProcessor {

  private Model model = null;
  private AgentFilesystem oAgentFilesystem;
  private LibraryZip oLibraryZip;
  private Configuration configuration;
  private Logger logger;
  private String documentId;
  
  @Inject
  public void injectAgentFilesystem(AgentFilesystem oAgentFilesystem) {
    this.oAgentFilesystem = oAgentFilesystem;
  }
  
  @Inject
  public void injectLibraryZip(LibraryZip oLibraryZip) {
    this.oLibraryZip = oLibraryZip;
  }
  
  @Inject
  public void injectConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Inject
  public void injectLogger(Logger logger) {
    this.logger = logger;
  }
  
  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public void setModel(Model model) {
    logger.debug("setModel(%s)", model);
    
    this.model = model;
  }
  
  public void process(String document) {
    logger.debug("process(%s)", document);
    
    String tempDir = this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString();
    File workingXmlFile = null;
    
    try {
      //Descomprimir el Docx     
      this.oLibraryZip.decompress(document, tempDir);
    
      //Copiar document.xml fuera
      String documentXmlPath = tempDir + File.separator + "content.xml";
      workingXmlFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString() + ".xml");
      String workingXmlPath = workingXmlFile.getAbsolutePath();
      oAgentFilesystem.copyFile(documentXmlPath , workingXmlPath);
      
      //Procesar el Xml sobreescribiendo el original
      FileInputStream sourceDocumentStream = new FileInputStream(workingXmlFile);
      FileOutputStream processedDocumentStream = new FileOutputStream(documentXmlPath);
      RootProcessor rootProc = new RootProcessor(sourceDocumentStream, processedDocumentStream);
      rootProc.setModel(this.model);
      rootProc.setDocumentId(this.documentId);
      rootProc.start();
      sourceDocumentStream.close();
      processedDocumentStream.close();
      
      //Comprimir el odt y sustituir el original
      ArrayList<String> aFiles = getAllFilesPath(new File(tempDir));
      ArrayList<String> aRelativeFiles = new ArrayList<String>();
      for(String file : aFiles)
        aRelativeFiles.add(file.substring(tempDir.length()+1));
      this.oLibraryZip.compress(tempDir, aRelativeFiles, document);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException(String.format("Error processing document '%s'.", document));
    }
    finally {
      try
      {
        oAgentFilesystem.removeDir(tempDir);
        if(workingXmlFile != null)
          oAgentFilesystem.removeFile(workingXmlFile.getAbsolutePath());
      }
      catch(Exception e) { 
        logger.error(e.getMessage(), e);
      }
    }
  }
  
  private ArrayList<String> getAllFilesPath(File tempDir) {
    logger.debug("getAllFilesPath(%s)", tempDir);
    
    ArrayList<String> aFiles = new ArrayList<String>();
    File [] childs = tempDir.listFiles();
    File currentChild = null;
    for(int i=0;i<childs.length;i++) {
      currentChild = childs[i];
      if(currentChild.isDirectory())
        aFiles.addAll(getAllFilesPath(currentChild));
      else
        aFiles.add(currentChild.getPath());
    }
    
    return aFiles;
  }

}
