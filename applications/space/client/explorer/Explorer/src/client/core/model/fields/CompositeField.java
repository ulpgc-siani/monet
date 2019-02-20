package client.core.model.fields;

import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.model.types.Composite;
import client.core.model.Field;

public interface CompositeField extends Field<CompositeFieldDefinition, Composite> {

	ClassName CLASS_NAME = new ClassName("CompositeField");

	boolean isNullOrEmpty();

	boolean getConditioned();
	void setConditioned(boolean conditioned);
}
