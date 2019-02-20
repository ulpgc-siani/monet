package org.monet.bpi.java;

import org.monet.bpi.FieldFile;
import org.monet.bpi.types.File;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.InputStream;

public class FieldFileImpl extends FieldImpl<File> implements FieldFile {

	@Override
	public File get() {
		String value = this.getIndicatorValue(Indicator.VALUE);
		if (value.isEmpty())
			return null;
		return new File(value);
	}

	@Override
	public void set(File value) {
		this.set(value, null);
	}

	@Override
	public void set(File value, String label) {
		if (!value.isStoredAtDocEngine()) {
			MessageAttach source = value.getSource();
			InputStream sourceStream = null;
			try {
				String documentId = Field.getFilename(this.nodeId, value.getFilename());
				String contentType = source.getContentType();
				sourceStream = source.getInputStream();
				ComponentDocuments.getInstance().uploadDocument(documentId, sourceStream, contentType, MimeTypes.getInstance().isPreviewable(contentType));
				this.setIndicatorValue(Indicator.VALUE, documentId);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				StreamHelper.close(sourceStream);
			}
		} else {
			this.setIndicatorValue(Indicator.VALUE, value.getFilename());
		}
		if (label != null) setFileLabel(label);
	}

	@Override
	public String getFileLabel() {
		String label = this.getIndicatorValue(Indicator.DETAILS);
		if (label == null || label.isEmpty()) super.getLabel();
		return label;
	}

	@Override
	public void setFileLabel(String label) {
		this.setIndicatorValue(Indicator.DETAILS, label);
	}

	@Override
	@Deprecated
	public void setLabel(String label) {
		this.setIndicatorValue(Indicator.DETAILS, label);
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

}