package org.monet.docservice.docprocessor.data;

import org.monet.docservice.core.log.EventLog;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.model.DocumentMetadata;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Repository {

	String createTemplate(String code, int documentType);

	void saveTemplateData(String templateId, InputStream stream, String hash, String contentType, String signPosition);

	void addTemplatePart(String templateId, String partId, InputStream partData);

	InputStream getTemplatePart(String documentId, String partId);

	void createDocument(String documentId, String templateCode, int state);

	void createEmptyDocument(String documentId, int state);

	Document getDocument(String documentId);

	boolean existsDocument(String documentId);

	void removeDocument(String documentId);

	String getDocumentDataContentType(String documentId);

	String getDocumentDataLocation(String documentId);

	String getDocumentHash(String documentId);

	boolean existsDocumentPreview(String documentId);

	String getDocumentPreviewDataContentType(String documentId, int page, int type);

	void readDocumentPreviewData(String documentId, int page, OutputStream data, int type);

	void saveDocumentPreviewData(String documentId, int page, InputStream data, String contentType, int type, int width, int height, float aspectRatio);

	void clearDocumentPreviewData(String documentId);

	DocumentMetadata getDocumentMetadata(Document document);

	void updateDocument(String documentId, int state);

	int removeAllNodeFiles(int nodeId);

	List<String> getTemplateSigns(String id);

	String getTemplateSignsPosition(String id);

	void addSignFields(String templateId, String[] signFields);

	InputStream getDocumentData(String documentId);

	void saveDocumentData(String documentId, InputStream inputStream, InputStream xmlData, String contentType, String hash);

	void saveDocumentData(String documentId, InputStream inputStream, String xmlData, String contentType, String hash);

	void saveDocumentData(String documentId, InputStream inputStream, String contentType, String hash);

	void saveDocumentXmlData(String documentId, InputStream xmlData);

	InputStream getDocumentXmlData(String documentId);

	void insertEventLogBlock(List<EventLog> eventLogs);

	/**
	 * @return: Width = Position[0], Height = Position[1]
	 */
	int[] getImageDimension(String documentId);

	void overwriteDocument(String destinationDocumentId, String sourceDocumentId);

	public String getVersion();

	public void updateVersion(String databaseVersion);

	void cleanDocumentPreviews();

	void createImagePreview(String imageId, int width, int height);
}