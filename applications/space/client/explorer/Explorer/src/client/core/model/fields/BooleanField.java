package client.core.model.fields;

import client.core.model.definition.entity.field.BooleanFieldDefinition;
import client.core.model.Field;

public interface BooleanField extends Field<BooleanFieldDefinition, Boolean> {

	ClassName CLASS_NAME = new ClassName("BooleanField");

	void setValue(Boolean value);
	boolean isNullOrEmpty();
}
