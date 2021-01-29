package org.monet.docservice.servlet.factory.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.monet.docservice.core.Key;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class CreateDocument extends Action {

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
    Key documentKey = documentKey(params);
    Key templateKey = templateKey(params);
    Key documentReferenced = Key.from(space(params), (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_REFERENCED));
    logger.debug("createDocument(%s, %s)", templateKey, documentKey);

    Repository repository = repositoryProvider.get();      
    repository.createDocument(documentKey, templateKey, Document.STATE_EDITABLE, documentReferenced);
    
    response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
  }

}
