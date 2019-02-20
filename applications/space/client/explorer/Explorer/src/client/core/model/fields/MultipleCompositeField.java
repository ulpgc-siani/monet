package client.core.model.fields;

import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.model.types.Composite;
import client.core.model.MultipleField;

public interface MultipleCompositeField extends MultipleField<CompositeField, CompositeFieldDefinition, Composite> {
	ClassName CLASS_NAME = new ClassName("MultipleCompositeField");

	boolean getConditioned();
	void setConditioned(boolean conditioned);
}
