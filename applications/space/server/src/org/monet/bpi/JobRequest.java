package org.monet.bpi;

import org.monet.bpi.types.File;
import org.monet.bpi.types.Location;

public interface JobRequest {

	void setLabel(String label);

	void setDefault(Schema schema);

	void setContent(String content);

	void setLocation(Location location);

	void attachFile(String name, File file);

	void attachDocument(String name, NodeDocument document);

	void attachString(String name, String content);

}
