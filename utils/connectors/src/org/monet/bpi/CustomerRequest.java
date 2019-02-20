package org.monet.bpi;

import org.monet.bpi.types.File;

import java.util.Iterator;

public interface CustomerRequest {

	String getCode();

	String getContent();

	File getFile(String name);

	NodeDocument getDocument(String name, Class<? extends NodeDocument> documentType);

	Schema getSchema(String name, Class<? extends Schema> schemaType);

	String getString(String name);

	Iterator<String> getAttachmentsKeys();

}
