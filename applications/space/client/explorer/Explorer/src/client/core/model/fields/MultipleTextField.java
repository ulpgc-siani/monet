package client.core.model.fields;

import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.MultipleField;

public interface MultipleTextField extends MultipleField<TextField, TextFieldDefinition, String> {

	ClassName CLASS_NAME = new ClassName("MultipleTextField");

	boolean metaDataIsValid(String value);
}
