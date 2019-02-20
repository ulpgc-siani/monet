package org.monet.docservice.docprocessor.templates.openxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.monet.docservice.core.agent.AgentFilesystem;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.library.LibraryZip;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.templates.DocumentProcessor;
import org.monet.docservice.docprocessor.templates.common.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.inject.Inject;

public class OpenXmlDocument implements DocumentProcessor {

  private Model model = null;
  private AgentFilesystem oAgentFilesystem;
  private LibraryZip oLibraryZip;
  private Configuration configuration;
  private Logger logger;
  private Repository repository;
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
  
  @Inject
  public void injectRepository(Repository repository) {
    this.repository = repository;
  }

  public void setModel(Model model) {
    logger.debug("setModel(%s)", model);
    
    this.model = model;
  }
  
  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public void process(String document) {
    logger.debug("process(%s)", document);
    
    String tempDir = this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString();
    
    try {
      //Descomprimir el Docx
      this.oLibraryZip.decompress(document, tempDir);
    
      //Procesar document.xml, headers y footers
      List<String> docxFiles = getWordDocuments(tempDir);
      for (int i = 0; i < docxFiles.size(); i++) {
          replace(tempDir, docxFiles.get(i));   
      }
      
      //Comprimir el Docx y sustituir el original
      ArrayList<String> aFiles = getAllFilesPath(new File(tempDir));
      ArrayList<String> aRelativeFiles = new ArrayList<String>();
      for(String file : aFiles)
        aRelativeFiles.add(file.substring(tempDir.length()+1));
      this.oLibraryZip.compress(tempDir, aRelativeFiles, document);

    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage(), e);
      throw new ApplicationException(String.format("Error processing document '%s'", document));
    }
    finally {
      try {
        oAgentFilesystem.removeDir(tempDir);
      }
      catch(Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
  }
  
  private List<String> getWordDocuments(String tempDir) {
    List<String> retFiles = new ArrayList<String>(); 
    try {
    File file = new File(tempDir + File.separator + "[Content_Types].xml");
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(file);
    doc.getDocumentElement().normalize();

    NodeList nodes = doc.getElementsByTagName("Override");
    for (int j = 0; j < nodes.getLength(); j++) {
      Element e = (Element)(nodes.item(j));
      String contentType = e.getAttribute("ContentType");
      if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"))
        retFiles.add(e.getAttribute("PartName"));
      else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml"))
        retFiles.add(e.getAttribute("PartName"));
      else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml"))
        retFiles.add(e.getAttribute("PartName"));
    }
  } catch (Exception e) {
     logger.error(e.getMessage(), e);
       throw new ApplicationException(String.format("Error processing document [Content_Types].xml"));
  }
  
  return retFiles;
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

  public void replace(String tempDir, String sourceDocument){
    File workingXmlFile = null;
    FileInputStream sourceDocumentStream = null;
    FileOutputStream processedDocumentStream = null;
    try {
      //Copiar document.xml fuera
      String documentXmlPath = tempDir + sourceDocument;
      workingXmlFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString() + ".xml");
  
      String workingXmlPath = workingXmlFile.getAbsolutePath();
      oAgentFilesystem.copyFile(documentXmlPath , workingXmlPath);
      
      //Procesar el Xml sobreescribiendo el original
      sourceDocumentStream = new FileInputStream(workingXmlFile);
      processedDocumentStream = new FileOutputStream(documentXmlPath);
      RootProcessor rootProc = new RootProcessor(sourceDocumentStream, processedDocumentStream);
      rootProc.setModel(this.model);
      rootProc.setRepository(this.repository);
      rootProc.setDocumentId(this.documentId);
      rootProc.start();
      
      ImageProcessor imgProc = new ImageProcessor(tempDir, sourceDocument);
      imgProc.replaceImages(rootProc.getOldIdImages(),rootProc.getNewIdImages());
    } catch (Exception e) {
       logger.error(e.getMessage(), e);
         throw new ApplicationException(String.format("Error processing document '%s'", sourceDocument));
    } finally {
      StreamHelper.close(sourceDocumentStream);
      StreamHelper.close(processedDocumentStream);
      try {
        if(workingXmlFile != null)
          oAgentFilesystem.removeFile(workingXmlFile.getAbsolutePath());
      } catch(Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
  }
}
