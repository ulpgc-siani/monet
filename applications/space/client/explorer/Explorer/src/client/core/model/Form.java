package client.core.model;

import client.core.model.definition.entity.FormDefinition;

public interface Form extends Node<FormDefinition> {

	ClassName CLASS_NAME = new ClassName("Form");

	ClassName getClassName();
	List<Field> get();
	Field get(String key);
}
