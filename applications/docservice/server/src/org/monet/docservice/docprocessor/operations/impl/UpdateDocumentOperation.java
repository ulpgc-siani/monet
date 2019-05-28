package org.monet.docservice.docprocessor.operations.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.pdf.PdfConverter;
import org.monet.docservice.docprocessor.pdf.PreviewGenerator;
import org.monet.docservice.docprocessor.templates.DocumentReplacer;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;

import java.io.*;
import java.util.UUID;

public class UpdateDocumentOperation implements Operation {

	private WorkQueueItem target;
	private Configuration configuration;
	private Provider<Repository> repositoryProvider;
	private DocumentReplacer documentReplacer;
	private PreviewGenerator previewGenerator;
	private PdfConverter pdfConverter;
	private Logger logger;

	@Inject
	public void inject(Configuration configuration) {
		this.configuration = configuration;
	}

	@Inject
	public void inject(Provider<Repository> repositoryProvider) {
		this.repositoryProvider = repositoryProvider;
	}

	@Inject
	public void inject(DocumentReplacer documentReplacer) {
		this.documentReplacer = documentReplacer;
	}

	@Inject
	public void inject(PreviewGenerator previewGenerator) {
		this.previewGenerator = previewGenerator;
	}

	@Inject
	public void inject(PdfConverter pdfConverter) {
		this.pdfConverter = pdfConverter;
	}

	@Inject
	public void inject(Logger logger) {
		this.logger = logger;
	}

	public void setTarget(WorkQueueItem target) {
		this.target = target;
	}

	public void execute() {
		logger.debug("UpdateDocumentOperation.execute()");

		Repository repository = repositoryProvider.get();
		File tempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString());
		File pdfTempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString());
		int state = repository.getDocument(target.getDocumentId()).getState();

		if (! (state == Document.STATE_EDITABLE || state == Document.STATE_OVERWRITTEN))
			throw new ApplicationException(String.format("The document '%s' isn't editable", target.getDocumentId()));

		try {
			FileOutputStream outputStream = null;
			InputStream documentStream = null;
			try {
				outputStream = new FileOutputStream(tempFile);
				documentStream = repository.getDocumentData(target.getDocumentId());
				IOUtils.copy(documentStream, outputStream);
			} finally {
				StreamHelper.close(outputStream);
				StreamHelper.close(documentStream);
			}

			String contentType = repository.getDocumentDataContentType(target.getDocumentId());
			int documentType = DocumentType.valueOf(contentType);
			InputStream xmlData = null;
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			if (target.getExtraDataInputStream() != null) {
				StreamHelper.copy(target.getExtraDataInputStream(), data);
				xmlData = new ByteArrayInputStream(data.toByteArray());
			}

			if (state != Document.STATE_OVERWRITTEN)
				documentReplacer.updateDocument(target.getDocumentId(), tempFile.getAbsolutePath(), xmlData, documentType);

			if (xmlData != null) xmlData.reset();

			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(tempFile);
				String hash = StreamHelper.calculateHashToHexString(inputStream);
				StreamHelper.close(inputStream);
				inputStream = new FileInputStream(tempFile);
				repository.saveDocumentData(target.getDocumentId(), inputStream, xmlData, contentType, hash);
			} finally {
				StreamHelper.close(inputStream);
			}

			if (documentType != DocumentType.XML_DOCUMENT) {
				repository.clearDocumentPreviewData(target.getDocumentId());

				String sPdfTempFile = pdfTempFile.getAbsolutePath();
				if (documentType != DocumentType.PORTABLE_DOCUMENT)
					this.pdfConverter.generatePdf(tempFile.getAbsolutePath(), sPdfTempFile);
				else
					sPdfTempFile = tempFile.getAbsolutePath();

				previewGenerator.generatePreview(sPdfTempFile, target.getDocumentId());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error updating the specified document.");
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error updating the specified document.");
		} finally {
			if (tempFile != null && tempFile.exists()) tempFile.delete();
			if (pdfTempFile != null && pdfTempFile.exists()) pdfTempFile.delete();
		}
	}

}
