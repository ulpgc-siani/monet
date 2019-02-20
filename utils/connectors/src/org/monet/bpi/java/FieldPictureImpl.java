package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldPicture;
import org.monet.bpi.types.Picture;
import org.monet.v2.model.constants.Strings;

public class FieldPictureImpl extends FieldImpl<Picture> implements FieldPicture {

	@Override
	public Picture get() {
		String value = this.getIndicatorValue(Indicator.VALUE);

		if (value.isEmpty())
			return null;

		Picture picture = new Picture(value);
		picture.setContent(this.api.getNodeFile(this.nodeId, value));

		return picture;
	}

	@Override
	public void set(Picture value) {

		if (value == null) {
			this.setIndicatorValue(Indicator.VALUE, Strings.EMPTY);
			return;
		}

		String filename = value.getFilename();

		if (!this.api.saveNodePicture(this.nodeId, filename, value.getContent()))
			throw new RuntimeException("Could not save picture " + filename + " of node " + this.nodeId);

		this.setIndicatorValue(Indicator.VALUE, filename);
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

	private String getFilename(String filename) {
		return String.format("%s/%s", this.nodeId, filename);
	}

}