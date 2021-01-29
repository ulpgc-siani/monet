package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.operations.OperationsFactory;
import org.monet.docservice.docprocessor.pdf.PdfXmlExtractor;
import org.monet.docservice.docprocessor.worker.WorkQueue;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

public class UploadDocument extends Action {

	private Logger logger;
	private Provider<Repository> repositoryProvider;
	private PdfXmlExtractor pdfXmlExtractor;
	private OperationsFactory operationsFactory;
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
	public void injectPdfXmlExtractor(PdfXmlExtractor pdfXmlExtractor) {
		this.pdfXmlExtractor = pdfXmlExtractor;
	}

	@Inject
	public void injectOperationsFactory(OperationsFactory operationsFactory) {
		this.operationsFactory = operationsFactory;
	}

	@Inject
	public void injectWorkQueue(WorkQueue workQueue) {
		this.workQueue = workQueue;
	}

	@Override
	public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
		Key documentKey = documentKey(params);
		InputStream documentData = (InputStream) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_DATA);
		String contentType = (String) params.get(RequestParams.REQUEST_PARAM_CONTENT_TYPE);
		boolean generatePreview = Boolean.valueOf((String)params.get(RequestParams.REQUEST_PARAM_GENERATE_PREVIEW));
		File tempFile = null;
		String xmlData = null;

		logger.debug("uploadDocument(%s)", documentData);

		try {
			Repository repository = repositoryProvider.get();
			tempFile = File.createTempFile("docService", ".tpl");
			FileOutputStream tempFileStream = new FileOutputStream(tempFile);

			StreamHelper.copy(documentData, tempFileStream);

			FileInputStream sourcePdfFileStream = new FileInputStream(tempFile);

			if (DocumentType.valueOf(contentType) == DocumentType.PORTABLE_DOCUMENT) {
				xmlData = this.pdfXmlExtractor.extractXmlData(sourcePdfFileStream);
				StreamHelper.close(sourcePdfFileStream);
				sourcePdfFileStream = new FileInputStream(tempFile);
			}

			String hash = StreamHelper.calculateHashToHexString(sourcePdfFileStream);
			StreamHelper.close(sourcePdfFileStream);
			sourcePdfFileStream = new FileInputStream(tempFile);

			if (!repository.existsDocument(documentKey))
				repository.createEmptyDocument(documentKey, Document.STATE_CONSOLIDATED);
			else {
				try {
					InputStream inputStream = repository.getDocumentXmlData(documentKey);
					if (inputStream != null)
						xmlData = StreamHelper.toString(inputStream);
				}
				catch (ApplicationException exception) {
				}
			}

			if (DocumentType.valueOf(contentType) == DocumentType.PORTABLE_DOCUMENT) {
				Operation operation = this.operationsFactory.create(Operation.OPERATION_EXTRACT_ATTACHMENT);
				WorkQueueItem target = new WorkQueueItem(-1);
				target.setDocumentKey(documentKey);
				target.setDataFile(tempFile);
				target.setXmlData(xmlData);
				operation.setTarget(target);
				operation.execute();
				xmlData = target.getXmlData();
			}

			repository.saveDocumentData(documentKey, sourcePdfFileStream, xmlData, contentType, hash);
			repository.clearDocumentPreviewData(documentKey);

			if (generatePreview) {
				WorkQueueItem item = new WorkQueueItem(-1);
				item.setDocumentKey(documentKey);
				item.setOperation(Operation.OPERATION_GENERATE_DOCUMENT_PREVIEW);
				this.workQueue.queueNewWorkItem(item);
			}

			if (response != null) response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error uploading document.");
		} finally {
			if (tempFile != null && tempFile.exists())
				tempFile.delete();
		}
	}

}
