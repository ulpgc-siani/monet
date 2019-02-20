package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldFile;
import org.monet.bpi.types.File;
import org.monet.v2.model.constants.Strings;

import java.io.InputStream;

public class FieldFileImpl extends FieldImpl<File> implements FieldFile {

	@Override
	public File get() {
		String value = this.getIndicatorValue(Indicator.VALUE);

		if (value.isEmpty())
			return null;

		File file = new File(value);
		file.setContent(this.api.getNodeFile(this.nodeId, value));

		return file;
	}

	@Override
	public void set(File value) {

		if (value == null) {
			this.setIndicatorValue(Indicator.VALUE, Strings.EMPTY);
			this.setIndicatorValue(Indicator.DETAILS, Strings.EMPTY);
			return;
		}

		InputStream content = value.getContent();
		if (content == null) {
			this.setIndicatorValue(Indicator.VALUE, Strings.EMPTY);
			this.setIndicatorValue(Indicator.DETAILS, Strings.EMPTY);
			return;
		}

		String filename = value.getFilename();

		if (!this.api.saveNodeFile(this.nodeId, filename, content))
			throw new RuntimeException("Could not save file " + filename + " of node " + this.nodeId);

		this.setIndicatorValue(Indicator.VALUE, filename);
		this.setIndicatorValue(Indicator.DETAILS, filename);
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof File)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set(new File(""));
	}

	private String getFilename(String filename) {
		return String.format("%s/%s", this.nodeId, filename);
	}

}