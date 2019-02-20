package org.monet.docservice.servlet.factory.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class GetDocumentXmlData extends ActionStringResult{
  private Logger logger;
  private Provider<Repository> repositoryProvider;
  
  @Inject
  public void injectLogger(Logger logger){
    this.logger = logger;
  }
  
  @Inject
  public void injectRepository(Provider<Repository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public String onExecute(Map<String, Object> params, HttpServletResponse response)
      throws Exception {
    String documentId = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_CODE);
    logger.debug("getDocumentXmlData(%s)", documentId);

    String xmlData = null;
    try {
      Repository repository = repositoryProvider.get();
      InputStream xmlDataStream = repository.getDocumentXmlData(documentId);
      xmlData = xmlDataStream != null ? StreamHelper.toString(xmlDataStream) : null;
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException("Error getting the document xml data.");
    }
   
    return xmlData;
  }

}
