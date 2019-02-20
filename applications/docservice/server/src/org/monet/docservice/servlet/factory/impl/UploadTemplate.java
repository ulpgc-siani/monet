package org.monet.docservice.servlet.factory.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.library.LibraryUtils;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.docservice.docprocessor.templates.PartsExtractor;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class UploadTemplate extends Action {

  private Logger logger;
  private Provider<Repository> repositoryProvider;
  private LibraryUtils libraryUtils;
  private PartsExtractor partsExtractor;
  
  @Inject
  public void injectLogger(Logger logger){
    this.logger = logger;
  }
  
  @Inject
  public void injectRepository(Provider<Repository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }
  
  @Inject
  public void injectLibraryUtils(LibraryUtils libraryUtils) {
    this.libraryUtils = libraryUtils;
  }
  
  @Inject
  public void injectPartsExtractor(PartsExtractor partsExtractor) {
    this.partsExtractor = partsExtractor;
  }

  @Override
  public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
    String templateCode = (String) params.get(RequestParams.REQUEST_PARAM_TEMPLATE_CODE);
    String mimeType = (String) params.get(RequestParams.REQUEST_PARAM_MIME_TYPE);
    InputStream templateData = (InputStream) params.get(RequestParams.REQUEST_PARAM_TEMPLATE_DATA);
    
    logger.debug("uploadTemplate(%s, %s, %s)", templateCode, mimeType, templateData);

    File tempFile = null;
    int documentType = DocumentType.valueOf(mimeType);

    try {
      Repository repository = repositoryProvider.get();
      String templateId = repository.createTemplate(templateCode, documentType);

      tempFile = File.createTempFile("docService", ".tpl");

      //Copy to temporal file
      FileOutputStream outputStream = new FileOutputStream(tempFile);
      
      this.libraryUtils.copyStream(templateData, outputStream);
      templateData.close();
      outputStream.close();

      FileInputStream tempFileStream = new FileInputStream(tempFile);
      String hash = StreamHelper.calculateHashToHexString(tempFileStream);
      StreamHelper.close(tempFileStream);
      
      partsExtractor.processTemplate(tempFile.getAbsolutePath(), templateId, documentType);

      repository.saveTemplateData(templateId, new FileInputStream(tempFile), hash, mimeType, null);
      
      response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException("Error uploading the template.");
    } finally {
      if(tempFile != null && tempFile.exists())
        tempFile.delete();
    }
  }

}
