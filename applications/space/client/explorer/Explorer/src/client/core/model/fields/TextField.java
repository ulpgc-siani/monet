package client.core.model.fields;

import client.core.model.Field;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.definition.entity.field.TextFieldDefinition.EditionDefinition.Mode;

import java.util.Map;

public interface TextField extends Field<TextFieldDefinition, String> {

	ClassName CLASS_NAME = new ClassName("TextField");

	boolean isNullOrEmpty();
    Map<String, String> getMetaData();
	String[] getMetaDataValues();
	String getMeta(String indicator);
	Mode getMode();
	boolean metaDataIsValid(String value);
}
