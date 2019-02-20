package client.core.model;

import client.core.model.definition.entity.FieldDefinition;

public interface Field<Definition extends FieldDefinition, Value> extends Entity<Definition> {

	ClassName CLASS_NAME = new ClassName("Field");

	ClassName getClassName();
	String getCode();
	void setCode(String code);
	Value getValue();
	String getValueAsString();
	void setValue(Value value);
	boolean isMultiple();
	boolean isNullOrEmpty();
	FieldDefinition.FieldType getFieldType();
    String getPath();
}
