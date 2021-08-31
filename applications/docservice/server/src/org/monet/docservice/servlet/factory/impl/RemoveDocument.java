package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Map;

import org.monet.docservice.core.Key;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;
import org.monet.http.Response;

import java.util.Map;

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
  public void execute(Map<String, Object> params, Response response) throws Exception {
    Key documentKey = documentKey(params);
    logger.debug("removeDocument(%s)", documentKey);

    Repository repository = repositoryProvider.get();
    repository.removeDocument(documentKey);
    response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
  }

}
