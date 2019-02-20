package client.core.model.fields;

import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.Field;

public interface NumberField extends Field<NumberFieldDefinition, client.core.model.types.Number> {

	ClassName CLASS_NAME = new ClassName("NumberField");

	void setFormattedValue(String value);
	boolean isNullOrEmpty();
}
