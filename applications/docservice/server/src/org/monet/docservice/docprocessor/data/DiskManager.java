package org.monet.docservice.docprocessor.data;

import java.io.InputStream;

public interface DiskManager {
	InputStream readDocument(String documentId, String location);
	String addDocument(String documentId, InputStream data);
	void saveDocument(String documentId, String location, InputStream data);
	void overrideDocument(String fromDocumentId, String fromLocation, String toDocumentId, String toLocation);
	void deleteDocument(String documentId, String location);
}
