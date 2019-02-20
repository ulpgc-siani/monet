package client.core.model.fields;

import client.core.model.definition.entity.field.SerialFieldDefinition;
import client.core.model.Field;

public interface SerialField extends Field<SerialFieldDefinition, String> {

	ClassName CLASS_NAME = new ClassName("SerialField");

	boolean isNullOrEmpty();
}
