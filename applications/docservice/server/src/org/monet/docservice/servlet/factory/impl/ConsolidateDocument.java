package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.operations.OperationsFactory;
import org.monet.docservice.docprocessor.worker.WorkQueue;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ConsolidateDocument extends Action {

	private Logger logger;
	private WorkQueue workQueue;
	private OperationsFactory operationsFactory;

	@Inject
	public void inject(Logger logger) {
		this.logger = logger;
	}

	@Inject
	public void inject(WorkQueue workQueue) {
		this.workQueue = workQueue;
	}

	@Inject
	public void inject(OperationsFactory operationsFactory) {
		this.operationsFactory = operationsFactory;
	}

	@Override
	public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
		String documentId = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_CODE);
		boolean async = Boolean.valueOf((String) params.get(RequestParams.REQUEST_PARAM_ASYNCRONOUS));
		logger.debug("consolidateDocument(%s, %s)", documentId, async);

		if (async) {
			WorkQueueItem item = new WorkQueueItem(-1);
			item.setDocumentId(documentId);
			item.setOperation(Operation.OPERATION_CONSOLIDATE_DOCUMENT);
			this.workQueue.queueNewWorkItem(item);
		} else {
			Operation operation = this.operationsFactory.create(Operation.OPERATION_CONSOLIDATE_DOCUMENT);
			WorkQueueItem target = new WorkQueueItem(-1);
			target.setDocumentId(documentId);
			operation.setTarget(target);
			operation.execute();
		}
		response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
	}

}
