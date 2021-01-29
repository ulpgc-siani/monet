package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.operations.OperationsFactory;
import org.monet.docservice.docprocessor.worker.WorkQueue;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

public class UpdateDocument extends Action {

  private Logger logger;
  private WorkQueue workQueue;
  private OperationsFactory operationsFactory;

  @Inject
  public void injectLogger(Logger logger){
    this.logger = logger;
  }

  @Inject
  public void injectWorkQueue(WorkQueue workQueue){
    this.workQueue = workQueue;
  }

  @Inject
  public void injectOperationsFactory(OperationsFactory operationsFactory){
    this.operationsFactory = operationsFactory;
  }

  @Override
  public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
    Key documentKey = documentKey(params);
    InputStream documentData  = (InputStream) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_DATA);
    boolean async = Boolean.valueOf((String) params.get(RequestParams.REQUEST_PARAM_ASYNCRONOUS));
    String space = (String) params.get(RequestParams.REQUEST_PARAM_SPACE);
    logger.debug("updateDocument(%s, %s, %s)", documentKey, documentData, async);

    WorkQueueItem item = new WorkQueueItem(-1);
    item.setDocumentKey(documentKey);
    item.setOperation(Operation.OPERATION_UPDATE_DOCUMENT);
    try {
      item.setExtraDataInputStream((documentData != null)?documentData:null);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException("Error reading update model data. Can't get an input stream.");
    }
    if(async)
      this.workQueue.queueNewWorkItem(item);
    else {
      Operation operation = this.operationsFactory.create(Operation.OPERATION_UPDATE_DOCUMENT);
      operation.setTarget(item);
      operation.execute();
    }

    response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
  }

}
