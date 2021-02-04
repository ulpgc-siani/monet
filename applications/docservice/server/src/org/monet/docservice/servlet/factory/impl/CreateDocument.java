package org.monet.docservice.servlet.factory.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.ws.api.message.AttachmentEx;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.AttachmentExtractor;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.operations.OperationsFactory;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.misc.IOUtils;

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
  public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
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
