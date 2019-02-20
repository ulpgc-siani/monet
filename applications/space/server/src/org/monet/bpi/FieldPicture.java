package org.monet.bpi;

import org.monet.bpi.types.Picture;

public interface FieldPicture extends Field<Picture> {
	void set(Picture value, String label);
	String getPictureLabel();
	void setPictureLabel(String label);
}