package client.core.model.definition.entity.field;

import client.core.model.Instance;

public interface PictureFieldDefinition extends FileFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("PictureFieldDefinition");

	String getDefault();
	boolean isProfilePhoto();
	SizeDefinition getSize();

	interface SizeDefinition {
		long getWidth();
		long getHeight();
	}
}
