package org.monet.docservice.docprocessor.operations.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.IOUtils;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.library.LibraryUtils;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.MimeTypes;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.docservice.docprocessor.model.SignPositions;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.pdf.PdfConverter;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.*;

public class ConsolidateDocumentOperation implements Operation {
	private WorkQueueItem target;
	private Configuration configuration;
	private Provider<Repository> repositoryProvider;
	private PdfConverter pdfConverter;
	private Logger logger;
	private LibraryUtils libraryUtils;
	private Map<String, String> extraDataParameters = null;
	private MimeTypes mimeTypes;

	public static final String EXTRA_PARAMETERS_DELIMITER = "#####";
	public static final String EXTRA_PARAMETERS_PATTERN = "%s" + EXTRA_PARAMETERS_DELIMITER + "%s";

	@Inject
	public void inject(Provider<Repository> repositoryProvider) {
		this.repositoryProvider = repositoryProvider;
	}

	@Inject
	public void inject(PdfConverter pdfConverter) {
		this.pdfConverter = pdfConverter;
	}

	@Inject
	public void inject(Configuration configuration) {
		this.configuration = configuration;
	}

	@Inject
	public void inject(Logger logger) {
		this.logger = logger;
	}

	@Inject
	public void inject(LibraryUtils libraryUtils) {
		this.libraryUtils = libraryUtils;
	}

	@Inject
	public void inject(MimeTypes mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

	public void setTarget(WorkQueueItem target) {
		this.target = target;
	}

	public void execute() {
		logger.debug("execute()");

		Repository repository = repositoryProvider.get();
		File tempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString());
		File pdfTempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString());
		File outputTempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString());

		Document document = repository.getDocument(this.target.getDocumentId());
		int state = document.getState();

		if (! (state == Document.STATE_EDITABLE || state == Document.STATE_OVERWRITTEN))
			throw new ApplicationException(String.format("The document '%s' isn't editable", this.target.getDocumentId()));

		try {
			String contentType = repository.getDocumentDataContentType(this.target.getDocumentId());
			int documentType = DocumentType.valueOf(contentType);

			if (documentType != DocumentType.XML_DOCUMENT) {

				FileOutputStream outputStream = null;
				InputStream documentData = null;

				try {
					documentData = repository.getDocumentData(this.target.getDocumentId());
					outputStream = new FileOutputStream(tempFile);
					IOUtils.copy(documentData, outputStream);
				} finally {
					StreamHelper.close(documentData);
					StreamHelper.close(outputStream);
				}

				List<String> signsFields = repository.getTemplateSigns(document.getTemplate().getId());
				Map<String, Integer> signsCount = getSignsCount();
				String signsCountPattern = getSignsCountPattern();
				String signsPosition = repository.getTemplateSignsPosition(document.getTemplate().getId());

				if (documentType != DocumentType.PORTABLE_DOCUMENT && documentType != DocumentType.XML_DOCUMENT)
					pdfConverter.generatePdf(tempFile.getAbsolutePath(), pdfTempFile.getAbsolutePath());
				else
					pdfTempFile = tempFile;

				InputStream xmlData = null;
				try {
					xmlData = repository.getDocumentXmlData(document.getId());
					this.addFields(pdfTempFile, outputTempFile, xmlData, signsPosition, signsFields, signsCount, signsCountPattern);
				} finally {
					StreamHelper.close(xmlData);
				}

				FileInputStream inputStream = null;

				try {
					inputStream = new FileInputStream(outputTempFile);
					String hash = StreamHelper.calculateHashToHexString(inputStream);
					StreamHelper.close(inputStream);
					inputStream = new FileInputStream(outputTempFile);
					repository.saveDocumentData(this.target.getDocumentId(), inputStream, "application/pdf", hash);
				} finally {
					StreamHelper.close(inputStream);
				}
			}

			repository.updateDocument(this.target.getDocumentId(), Document.STATE_CONSOLIDATED);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error updating the specified document.");
		} finally {
			if (tempFile != null && tempFile.exists())
				tempFile.delete();
			if (pdfTempFile != null && pdfTempFile.exists())
				pdfTempFile.delete();
		}
	}

	private Map<String, String> getExtraDataParameters() throws IOException {

		if (extraDataParameters != null)
			return extraDataParameters;

		InputStream extraDataInputStream = this.target.getExtraDataInputStream();
		String extraData = extraDataInputStream != null ? StreamHelper.toString(extraDataInputStream) : null;
		extraDataParameters = new HashMap<>();
		extraDataParameters.put("signsCount", extraData != null ? extraData.substring(0, extraData.indexOf(EXTRA_PARAMETERS_DELIMITER)) : "");
		extraDataParameters.put("signsCountPattern", extraData != null ? extraData.substring(extraData.indexOf(EXTRA_PARAMETERS_DELIMITER) + 5) : "");

		return extraDataParameters;
	}

	private Map<String, Integer> getSignsCount() throws IOException {
		String signsCount = getExtraDataParameters().get("signsCount");

		if (signsCount.isEmpty())
			return new HashMap<>();

		String[] signsCountArray = signsCount.split(",");
		Map<String, Integer> result = new HashMap<>();

		for (int i = 0; i < signsCountArray.length; i++) {
			String[] signCount = signsCountArray[i].split("#");
			result.put(signCount[0], Integer.valueOf(signCount[1]));
		}

		return result;
	}

	private String getSignsCountPattern() throws IOException {
		return getExtraDataParameters().get("signsCountPattern");
	}

	private void addAttachment(PdfStamper stamper, InputStream xmlData) {

		Repository repository = repositoryProvider.get();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		InputStream attachFile = null;

		try {
			docBuilder = docFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.parse(xmlData);

			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "//*[@is-attachment=\"true\"]/text()";

			NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); i++) {
				String attachId = nodeList.item(i).getNodeValue();
				String contentType = repository.getDocumentDataContentType(attachId);
				String attachName = attachId.contains(".") ? attachId : attachId + "." + mimeTypes.getExtension(contentType);
				attachFile = repository.getDocumentData(attachId);
				byte[] acttachAsByteArray = this.libraryUtils.copyStreamToByteArray(attachFile);
				PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(stamper.getWriter(), null, attachName, acttachAsByteArray);
				PdfAnnotation pdfAnnot = PdfAnnotation.createFileAttachment(stamper.getWriter(), new Rectangle(0, 0, 10f, 10f), contentType, fs);
				pdfAnnot.setFlags(PdfAnnotation.FLAGS_INVISIBLE);
				pdfAnnot.setName(attachName);
				stamper.addAnnotation(pdfAnnot, 1);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error adding attachment.");
		}
		finally {
			StreamHelper.close(attachFile);
		}

	}

	private void addFields(File documentInput, File documentOutput, InputStream xmlDataStream, String signsPosition, List<String> signsFieldsNames, Map<String, Integer> signsCount, String signsCountPattern) {
		FileInputStream documentInputStream = null;
		FileOutputStream outputStream = null;

		try {
			documentInputStream = new FileInputStream(documentInput);
			outputStream = new FileOutputStream(documentOutput);

			PdfReader pdfReader = new PdfReader(documentInputStream);
			PdfStamper stamper = new PdfStamper(pdfReader, outputStream);
			PdfWriter pdfWriter = stamper.getWriter();

			BaseFont bf = null;
			if (this.configuration.getBoolean(Configuration.GENERATE_PDF_A)) {
				pdfWriter.setPDFXConformance(PdfWriter.PDFA1A);

				ArrayList<Object[]> documentFonts = BaseFont.getDocumentFonts(pdfReader);
				if (documentFonts.size() > 0 && documentFonts.get(0).length > 1) {
					Object[] fontObjectArray = documentFonts.get(0);
					bf = BaseFont.createFont((PRIndirectReference) fontObjectArray[1]);
				}
			}

			if (xmlDataStream != null) {
				addAttachment(stamper, xmlDataStream);
				xmlDataStream.reset();

				TextField xmlDataField = new TextField(pdfWriter, new Rectangle(0, 0, 10, 10), "xmlData");
				xmlDataField.setText(convertStreamToString(xmlDataStream));
				xmlDataField.setVisibility(BaseField.HIDDEN);
				if (bf != null)
					xmlDataField.setFont(bf);
				stamper.addAnnotation(xmlDataField.getTextField(), 1);
			}

			if (signsFieldsNames.size() > 0) {
				SignPositions signPosition = SignPositions.valueOf(signsPosition.toUpperCase());

				int page = 1;
				switch (signPosition) {
					case TOP:
						page = 1;
						break;
					case BOTTOM:
						page = pdfReader.getNumberOfPages();
						break;
					case NEW_PAGE_AT_TOP:
						page = 1;
						stamper.insertPage(page, pdfReader.getPageSize(1));
						break;
					case NEW_PAGE_AT_BOTTOM:
						page = pdfReader.getNumberOfPages() + 1;
						stamper.insertPage(page, pdfReader.getPageSize(1));
						break;
				}

				int offsetX = this.configuration.getSignConfig(Configuration.OFFSET_X);
				int offsetY = this.configuration.getSignConfig(Configuration.OFFSET_Y);
				int height = this.configuration.getSignConfig(Configuration.SIGN_HEIGHT);
				for (int i = 0; i < signsFieldsNames.size(); i++) {
					String signFieldName = signsFieldsNames.get(i);

					for (int j = 0; j < signsCount.get(signFieldName); j++) {
						String signId = String.format(signsCountPattern, signFieldName, j);

						PdfFormField sig = PdfFormField.createSignature(pdfWriter);
						sig.setPage(page);
						sig.setFlags(PdfAnnotation.FLAGS_PRINT);
						sig.setFieldName(signId);
						sig.setWidget(new Rectangle(offsetX, offsetY, offsetX + 300, offsetY + height), null);
						stamper.addAnnotation(sig, page);
						offsetY -= height + 10;
					}
				}

			}

			stamper.close();
			pdfReader.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error updating the specified document.");
		} finally {
			StreamHelper.close(documentInputStream);
			StreamHelper.close(outputStream);
		}
	}

	private String convertStreamToString(InputStream xmlDataStream) {
		InputStreamReader xmlDataReader = null;
		StringWriter xmlDataWriter = new StringWriter();

		if (xmlDataStream == null)
			return "<schema></schema>";

		char[] buffer = new char[1024];
		try {
			xmlDataReader = new InputStreamReader(xmlDataStream, "UTF-8");
			int n;
			while ((n = xmlDataReader.read(buffer)) != -1) {
				xmlDataWriter.write(buffer, 0, n);
			}
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} finally {
			StreamHelper.close(xmlDataStream);
		}
		return xmlDataWriter.toString();
	}

}
