package org.monet.docservice.docprocessor.operations.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.Resources;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.docservice.docprocessor.model.PreviewType;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.pdf.PdfConverter;
import org.monet.docservice.docprocessor.pdf.PreviewGenerator;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class GenerateDocumentPreviewOperation implements Operation {

	private WorkQueueItem target;
	private Configuration configuration;
	private Provider<Repository> repositoryProvider;
	private PreviewGenerator previewGenerator;
	private PdfConverter pdfConverter;
	private Logger logger;

	@Inject
	public void injectConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Inject
	public void injectRepository(Provider<Repository> repositoryProvider) {
		this.repositoryProvider = repositoryProvider;
	}

	@Inject
	public void injectPreviewGenerator(PreviewGenerator previewGenerator) {
		this.previewGenerator = previewGenerator;
	}

	@Inject
	public void injectPdfConverter(PdfConverter pdfConverter) {
		this.pdfConverter = pdfConverter;
	}

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	public void setTarget(WorkQueueItem target) {
		this.target = target;
	}

	public void execute() {
		logger.debug("GenerateDocumentPreviewOperation.execute()");

		Repository repository = repositoryProvider.get();
		File tempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString());
		File pdfTempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString());

		try {
			FileOutputStream outputStream = null;
			InputStream documentStream = null;
			try {
				outputStream = new FileOutputStream(tempFile);
				documentStream = repository.getDocumentData(target.getDocumentKey());
				IOUtils.copy(documentStream, outputStream);
			} finally {
				StreamHelper.close(documentStream);
				StreamHelper.close(outputStream);
			}

			String contentType = repository.getDocumentDataContentType(target.getDocumentKey());
			int documentType = DocumentType.valueOf(contentType);

			repository.clearDocumentPreviewData(target.getDocumentKey());

			String pdfTempFilePath = pdfTempFile.getAbsolutePath();
			boolean generatePreview = false;

			if (documentType != DocumentType.PORTABLE_DOCUMENT && documentType != DocumentType.XML_DOCUMENT)
				try {
					this.pdfConverter.generatePdf(tempFile.getAbsolutePath(), pdfTempFilePath);
					generatePreview = true;
				}
				catch (Exception exception) {
					logger.error(exception.getMessage(), exception);
				}
			else {
				pdfTempFilePath = tempFile.getAbsolutePath();
				generatePreview = documentType != DocumentType.XML_DOCUMENT && !repository.existsDocumentPreview(target.getDocumentKey());
			}

			if (generatePreview)
				previewGenerator.generatePreview(pdfTempFilePath, target.getDocumentKey());
			else
				generateDefaultPreview(repository);

		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error generating preview of the specified document.");
		} finally {
			if (tempFile != null && tempFile.exists()) tempFile.delete();
			if (pdfTempFile != null && pdfTempFile.exists()) pdfTempFile.delete();
		}
	}

	private void generateDefaultPreview(Repository repository) throws IOException {
		InputStream previewData = Resources.getAsStream("/images/default.png");
		InputStream previewDataForImage = Resources.getAsStream("/images/default.png");
		InputStream previewThumbnailData = Resources.getAsStream("/images/defaultThumb.png");
		InputStream previewThumbnailDataForImage = Resources.getAsStream("/images/defaultThumb.png");
		BufferedImage previewImage = ImageIO.read(previewDataForImage);
		BufferedImage previewThumbnailImage = ImageIO.read(previewThumbnailDataForImage);

		repository.saveDocumentPreviewData(target.getDocumentKey(),
			1,
			previewData,
			"image/png",
			PreviewType.PAGE,
			previewImage.getWidth(),
			previewImage.getHeight(),
			previewImage.getWidth()/(float)previewImage.getHeight());
		StreamHelper.close(previewData);
		StreamHelper.close(previewDataForImage);

		repository.saveDocumentPreviewData(target.getDocumentKey(),
			1,
			previewThumbnailData,
			"image/png",
			PreviewType.THUMBNAIL,
			previewThumbnailImage.getWidth(),
			previewThumbnailImage.getHeight(),
			previewThumbnailImage.getWidth()/(float)previewThumbnailImage.getHeight());
		StreamHelper.close(previewThumbnailData);
		StreamHelper.close(previewThumbnailDataForImage);
	}

}
