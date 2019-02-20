package org.monet.bpi;

import org.monet.bpi.types.File;

public interface FieldFile extends Field<File> {
	void set(File value, String label);
	String getFileLabel();
	void setFileLabel(String label);
	@Deprecated void setLabel(String label);
}