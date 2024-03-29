package org.monet.docservice.servlet.factory.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;

import com.google.inject.Inject;
import com.google.inject.Provider;

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
  public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
    int nodeId = Integer.valueOf((String) params.get(RequestParams.REQUEST_PARAM_NODE_CODE));
    logger.debug("removeAllNodeFiles(%s)", nodeId);

    Repository repository = repositoryProvider.get();
    response.getWriter().write(String.valueOf(repository.removeAllNodeFiles(nodeId)));
  }

}
