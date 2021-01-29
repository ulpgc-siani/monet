package org.monet.docservice.servlet.factory.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class RemoveDocument extends Action{

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
  public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
    String documentId = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_CODE);
    String space = (String) params.get(RequestParams.REQUEST_PARAM_SPACE);
    documentId = normalize(documentId, space);
    logger.debug("removeDocument(%s)", documentId);

    Repository repository = repositoryProvider.get();
    repository.removeDocument(documentId);
    response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
  }

}
