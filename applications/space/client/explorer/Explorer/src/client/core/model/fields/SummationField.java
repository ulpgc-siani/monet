package client.core.model.fields;

import client.core.model.definition.entity.field.SummationFieldDefinition;
import client.core.model.Field;

public interface SummationField extends Field<SummationFieldDefinition, String> {

	ClassName CLASS_NAME = new ClassName("SummationField");

	boolean isNullOrEmpty();
}
