package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.monet.docservice.core.Key;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;
import org.monet.http.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class GetDocumentXmlData extends ActionStringResult {
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
  public String onExecute(Map<String, Object> params, Response response) throws Exception {
    String documentCode = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_CODE);
    Key documentKey = Key.containsSpace(documentCode) ? Key.from(documentCode) : documentKey(params);
    logger.debug("getDocumentXmlData(%s)", documentKey);

    String xmlData;
    try {
      Repository repository = repositoryProvider.get();
      InputStream xmlDataStream = repository.getDocumentXmlData(documentKey);
      xmlData = xmlDataStream != null ? StreamHelper.toString(xmlDataStream) : null;
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException("Error getting the document xml data.");
    }
   
    return xmlData;
  }

}
