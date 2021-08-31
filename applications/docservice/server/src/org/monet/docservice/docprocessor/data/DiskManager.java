package org.monet.docservice.docprocessor.data;

import org.monet.docservice.core.Key;

import java.io.InputStream;

public interface DiskManager {
	InputStream readDocument(Key documentKey, String location);
	String addDocument(Key documentKey, InputStream data);
	void saveDocument(Key documentKey, String location, InputStream data);
	void overrideDocument(Key fromDocumentId, String fromLocation, Key toDocumentId, String toLocation);
	void deleteDocument(Key documentKey, String location);
}
