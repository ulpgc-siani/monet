package client.core.model.fields;

import client.core.model.definition.entity.field.MemoFieldDefinition;
import client.core.model.Field;

public interface MemoField extends Field<MemoFieldDefinition, String> {

	ClassName CLASS_NAME = new ClassName("MemoField");

	boolean isNullOrEmpty();
}
