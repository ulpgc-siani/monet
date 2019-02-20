package org.monet.bpi;

import org.monet.bpi.types.File;

public interface NodeDocument extends Node {

	public Schema genericGetSchema();

	public void consolidate();

	public void overwriteContent(File newContent);

	public void setSchema(org.monet.bpi.Schema schema);

	public File getContent();

}
