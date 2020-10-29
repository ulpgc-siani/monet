package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.servlet.RequestParams;
import org.monet.http.Response;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

public class GetDocument extends Action {

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
  public void execute(Map<String, Object> params, Response response)
      throws Exception {
    String documentId = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_CODE);
    logger.debug("getDocument(%s)", documentId);

    ByteArrayOutputStream outputStream = null;
    InputStream documentStream = null;
    try {
      Repository repository = repositoryProvider.get();
      documentStream = repository.getDocumentData(documentId);
      IOUtils.copy(documentStream, response.getOutputStream());
    } finally {
      StreamHelper.close(documentStream);
      StreamHelper.close(outputStream);
    }
  }

}
