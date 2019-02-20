package client.core.model.fields;

import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.model.Field;
import client.core.model.types.Picture;

public interface PictureField extends Field<PictureFieldDefinition, Picture> {

	ClassName CLASS_NAME = new ClassName("PictureField");

	boolean isNullOrEmpty();
}
