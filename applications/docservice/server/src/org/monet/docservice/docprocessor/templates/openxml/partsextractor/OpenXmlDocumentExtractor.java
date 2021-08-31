package org.monet.docservice.docprocessor.templates.openxml.partsextractor;

import java.io.File;

import java.io.FileInputStream;
import java.util.UUID;

import org.monet.docservice.core.Key;
import org.monet.docservice.core.agent.AgentFilesystem;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.library.LibraryZip;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.Repository;

import com.google.inject.Inject;

public class OpenXmlDocumentExtractor {

  private AgentFilesystem oAgentFilesystem;
  private LibraryZip oLibraryZip;
  private Configuration configuration;
  private Logger logger;
  private Repository repository;
  
  @Inject
  public void setAgentFilesystem(AgentFilesystem oAgentFilesystem) {
    this.oAgentFilesystem = oAgentFilesystem;
  }
  
  @Inject
  public void setLibraryZip(LibraryZip oLibraryZip) {
    this.oLibraryZip = oLibraryZip;
  }
  
  @Inject
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
  
  @Inject
  public void setLogger(Logger logger) {
    this.logger = logger;
  }
  
  @Inject
  public void setRepository(Repository repository) {
    this.repository = repository;
  }

  public void process(String document, String templateId) {
    logger.debug("process(%s)", document);
    
    String tempDir = this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString();
    File workingXmlFile = null;
    
    try {
      //Descomprimir el Docx
      this.oLibraryZip.decompress(document, tempDir);
    
      //Copiar document.xml fuera
      String documentXmlPath = tempDir + File.separator + "word" + File.separator + "document.xml";
      workingXmlFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString() + ".xml");
      String workingXmlPath = workingXmlFile.getAbsolutePath();
      oAgentFilesystem.copyFile(documentXmlPath , workingXmlPath);
      
      //Procesar el Xml sobreescribiendo el original
      FileInputStream sourceDocumentStream = new FileInputStream(workingXmlFile);
      RootProcessor rootProc = new RootProcessor(sourceDocumentStream, null);
      rootProc.setTemplateId(templateId);
      rootProc.setRepository(this.repository);
      rootProc.Start();
      sourceDocumentStream.close();
      
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException(String.format("Error extracting parts from template '%s'", document));
    }
    finally {
      try {
        oAgentFilesystem.removeDir(tempDir);
        if(workingXmlFile != null)
          oAgentFilesystem.removeFile(workingXmlFile.getAbsolutePath());
      }
      catch(Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

}
