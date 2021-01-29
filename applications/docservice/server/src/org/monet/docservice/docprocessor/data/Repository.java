package org.monet.docservice.docprocessor.data;

import org.monet.docservice.core.Key;
import org.monet.docservice.core.log.EventLog;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.model.DocumentMetadata;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Repository {

	String createTemplate(String space, String code, int documentType);

	void saveTemplateData(String templateId, InputStream stream, String hash, String contentType, String signPosition);

	void addTemplatePart(String templateId, String partId, InputStream partData);

	InputStream getTemplatePart(Key documentKey, String partId);

	void createDocument(Key documentKey, Key templateKey, int state, Key documentReferenced);

	void createEmptyDocument(Key documentKey, int state);

	Document getDocument(Key documentKey);

	boolean existsDocument(Key documentKey);

	void removeDocument(Key documentKey);

	String getDocumentDataContentType(Key documentKey);

	String getDocumentDataLocation(Key documentKey);

	String getDocumentHash(Key documentKey);

	boolean existsDocumentPreview(Key documentKey);

	String getDocumentPreviewDataContentType(Key documentKey, int page, int type);

	void readDocumentPreviewData(Key documentKey, int page, OutputStream data, int type);

	void saveDocumentPreviewData(Key documentKey, int page, InputStream data, String contentType, int type, int width, int height, float aspectRatio);

	void clearDocumentPreviewData(Key documentKey);

	DocumentMetadata getDocumentMetadata(Document document);

	void updateDocument(Key documentKey, int state);

	int removeAllNodeFiles(String space, int nodeId);

	List<String> getTemplateSigns(String templateId);

	String getTemplateSignsPosition(String templateId);

	void addSignFields(String templateId, String[] signFields);

	InputStream getDocumentData(Key documentKey);

	void saveDocumentData(Key documentKey, InputStream inputStream, InputStream xmlData, String contentType, String hash);

	void saveDocumentData(Key documentKey, InputStream inputStream, String xmlData, String contentType, String hash);

	void saveDocumentData(Key documentKey, InputStream inputStream, String contentType, String hash);

	void saveDocumentXmlData(Key documentKey, InputStream xmlData);

	InputStream getDocumentXmlData(Key documentKey);

	void insertEventLogBlock(List<EventLog> eventLogs);

	/**
	 * @return: Width = Position[0], Height = Position[1]
	 */
	int[] getImageDimension(Key documentKey);

	void overwriteDocument(Key destinationDocumentId, Key sourceDocumentId);

	public String getVersion();

	public void updateVersion(String databaseVersion);

	void cleanDocumentPreviews();

	void createImagePreview(Key imageKey, int width, int height);
}