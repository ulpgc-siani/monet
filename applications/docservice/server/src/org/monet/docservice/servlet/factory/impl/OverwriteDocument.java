package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;
import org.monet.http.Response;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class OverwriteDocument extends Action {

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
  public void execute(Map<String, Object> params, Response response) throws Exception {
    Key sourceDocumentId = new Key(space(params), (String) params.get(RequestParams.REQUEST_PARAM_SOURCE_DOCUMENT_ID));
    Key destinationDocumentId = new Key(space(params), (String) params.get(RequestParams.REQUEST_PARAM_DESTINATION_DOCUMENT_ID));
    String xmlData = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_XML_DATA);
    logger.debug("overwriteDocument(%s, %s)", destinationDocumentId, sourceDocumentId);

    try {
      Repository repository = repositoryProvider.get();
      repository.overwriteDocument(destinationDocumentId, sourceDocumentId);

      if (xmlData != null)
        repository.saveDocumentXmlData(destinationDocumentId, new ByteArrayInputStream(xmlData.getBytes()));

      if(response != null) response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException("Error overwriting document.");
    }
  }

}