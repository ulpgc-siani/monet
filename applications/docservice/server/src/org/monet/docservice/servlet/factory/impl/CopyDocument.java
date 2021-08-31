package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.worker.WorkQueue;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;
import org.monet.http.Response;

import java.io.InputStream;
import java.util.Map;

public class CopyDocument extends Action {

	private Logger logger;
	private Provider<Repository> repositoryProvider;
	private WorkQueue workQueue;

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	@Inject
	public void injectRepository(Provider<Repository> repositoryProvider) {
		this.repositoryProvider = repositoryProvider;
	}

	@Inject
	public void injectWorkQueue(WorkQueue workQueue) {
		this.workQueue = workQueue;
	}

	@Override
	public void execute(Map<String, Object> params, Response response)
		throws Exception {

		Key documentKey = documentKey(params);
		Key newDocumentKey = new Key(documentKey.getSpace(), (String) params.get(RequestParams.REQUEST_PARAM_COPIED_DOCUMENT_CODE));
		boolean generatePreview = Boolean.valueOf((String)params.get(RequestParams.REQUEST_PARAM_GENERATE_PREVIEW));

		logger.debug("copyDocument(%s,%s)", documentKey, newDocumentKey);

		Repository repository = repositoryProvider.get();
		InputStream inputDocument = repository.getDocumentData(documentKey);
		InputStream xmlData = repository.getDocumentXmlData(documentKey);
		String contentType = repository.getDocumentDataContentType(documentKey);
		String hash = repository.getDocumentHash(documentKey);

		if (!repository.existsDocument(newDocumentKey))
			repository.createEmptyDocument(newDocumentKey, Document.STATE_CONSOLIDATED);

		repository.saveDocumentData(newDocumentKey, inputDocument, xmlData, contentType, hash);
		repository.clearDocumentPreviewData(documentKey);

		StreamHelper.close(inputDocument);

		if (generatePreview) {
			WorkQueueItem item = new WorkQueueItem(-1);
			item.setDocumentKey(documentKey);
			item.setOperation(Operation.OPERATION_GENERATE_DOCUMENT_PREVIEW);
			this.workQueue.queueNewWorkItem(item);
		}

		response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
	}

}
