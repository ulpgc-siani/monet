package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Map;

import org.monet.docservice.core.Key;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;
import org.monet.http.Response;

import java.util.Map;

public class GetDocumentHash extends ActionStringResult {

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
    Key documentKey = documentKey(params);
    logger.debug("getDocumentHash(%s)", documentKey);

    Repository repository = repositoryProvider.get();
    return repository.getDocumentHash(documentKey);

  }

}
