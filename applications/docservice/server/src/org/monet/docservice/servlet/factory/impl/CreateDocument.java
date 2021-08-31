package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.operations.OperationsFactory;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;
import org.monet.http.Response;

import java.util.Map;

public class CreateDocument extends Action {

  private Logger logger;
  private Provider<Repository> repositoryProvider;
  private OperationsFactory operationsFactory;

  @Inject
  public void injectLogger(Logger logger){
    this.logger = logger;
  }
  
  @Inject
  public void injectRepository(Provider<Repository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Inject
  public void injectOperationsFactory(OperationsFactory operationsFactory) {
    this.operationsFactory = operationsFactory;
  }

  @Override
  public void execute(Map<String, Object> params, Response response) throws Exception {
    Key documentKey = documentKey(params);
    Key templateKey = templateKey(params);
    String documentReferencedId = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_REFERENCED);
    Key documentReferenced = documentReferencedId != null ? Key.from(documentReferencedId) : null;
    logger.debug("createDocument(%s, %s)", templateKey, documentKey);

    Repository repository = repositoryProvider.get();      
    repository.createDocument(documentKey, templateKey, Document.STATE_EDITABLE, documentReferenced);

    response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
  }

}
