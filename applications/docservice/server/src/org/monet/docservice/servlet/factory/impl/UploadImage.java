package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;
import org.monet.http.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

public class UploadImage extends Action {

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

		String documentId = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_CODE);
		InputStream documentData  = (InputStream) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_DATA);
		String contentType = (String) params.get(RequestParams.REQUEST_PARAM_CONTENT_TYPE);
		int width =  Integer.valueOf((String) params.get(RequestParams.REQUEST_PARAM_WIDTH));
		int height =  Integer.valueOf((String) params.get(RequestParams.REQUEST_PARAM_HEIGHT));

		logger.debug("uploadImage(%s)", documentData);

		File tempFile = null;
		String xmlData = null;

		try {
			Repository repository = repositoryProvider.get();
			if (!repository.existsDocument(documentId))
				repository.createEmptyDocument(documentId, Document.STATE_CONSOLIDATED);

			tempFile = File.createTempFile("docService", ".tpl");

			FileOutputStream tempFileStream = new FileOutputStream(tempFile);

			StreamHelper.copy(documentData, tempFileStream);

			FileInputStream imageFileStream = new FileInputStream(tempFile);
			try {
				String hash = StreamHelper.calculateHashToHexString(imageFileStream);
				StreamHelper.close(imageFileStream);
				imageFileStream = new FileInputStream(tempFile);
				repository.saveDocumentData(documentId, imageFileStream, xmlData, contentType, hash);
			} finally {
				StreamHelper.close(imageFileStream);
			}

			repository.clearDocumentPreviewData(documentId);
			repository.createImagePreview(documentId, width, height);

			if(response != null)response.getWriter().write(MessageResponse.OPERATION_SUCCESFULLY);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error uploading image.");
		} finally {
			if(tempFile != null && tempFile.exists())
				tempFile.delete();
		}
	}

}
