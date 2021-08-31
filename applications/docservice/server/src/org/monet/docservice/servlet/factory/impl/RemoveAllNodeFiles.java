package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;
import org.monet.http.Response;

import java.util.Map;

public class RemoveAllNodeFiles extends Action {

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
    int nodeId = Integer.parseInt((String) params.get(RequestParams.REQUEST_PARAM_NODE_CODE));
    //TODO revisar caso
    logger.debug("removeAllNodeFiles(%s)", nodeId);

    Repository repository = repositoryProvider.get();
    response.getWriter().write(String.valueOf(repository.removeAllNodeFiles(space(params), nodeId)));
  }

}
