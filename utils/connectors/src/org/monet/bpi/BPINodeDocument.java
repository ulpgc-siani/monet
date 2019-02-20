package org.monet.bpi;


import org.monet.bpi.types.File;

public interface BPINodeDocument<Owner extends BPIBaseNode<?>,
	Schema extends BPISchema>
	extends BPINode<Owner, Schema>, BPIBaseNodeDocument<Schema> {

	public void consolidate();

	public File getContent();

	public void overwriteContent(File newContent);
}
