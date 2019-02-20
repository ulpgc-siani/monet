package org.monet.bpi.java;

import org.monet.bpi.FieldPicture;
import org.monet.bpi.types.Picture;
import org.monet.metamodel.PictureFieldProperty;
import org.monet.metamodel.PictureFieldProperty.SizeProperty;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.InputStream;

public class FieldPictureImpl extends FieldImpl<Picture> implements FieldPicture {

	@Override
	public Picture get() {
		String value = this.getIndicatorValue(Indicator.VALUE);
		if (value.isEmpty())
			return null;
		return new Picture(value);
	}

	@Override
	public void set(Picture value) {
		this.set(value, null);
	}

	@Override
	public void set(Picture value, String label) {
		if (value == null)
			this.setIndicatorValue(Indicator.VALUE, Strings.EMPTY);
		else if (!value.isStoredAtDocEngine()) {
			MessageAttach source = value.getSource();
			InputStream sourceStream = null;
			try {
				String documentId = Field.getFilename(this.nodeId, value.getFilename());
				String contentType = source.getContentType();
				sourceStream = source.getInputStream();
				SizeProperty size = ((PictureFieldProperty) this.fieldDefinition).getSize();
				long width = -1;
				long heigth = -1;
				if (size != null) {
					width = size.getWidth();
					heigth = size.getHeight();
				}
				ComponentDocuments.getInstance().uploadImage(documentId, sourceStream, contentType, (int) width, (int) heigth);
				this.setIndicatorValue(Indicator.VALUE, documentId);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				StreamHelper.close(sourceStream);
			}
		} else
			this.setIndicatorValue(Indicator.VALUE, value.getFilename());
		if (label != null) setPictureLabel(label);
	}

	@Override
	public String getPictureLabel() {
		String label = this.getIndicatorValue(Indicator.DETAILS);
		if (label == null || label.isEmpty()) super.getLabel();
		return label;
	}

	@Override
	public void setPictureLabel(String label) {
		this.setIndicatorValue(Indicator.DETAILS, label);
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof Picture)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set(new Picture(""));
	}

}