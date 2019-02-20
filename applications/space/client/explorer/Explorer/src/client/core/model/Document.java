package client.core.model;

import client.core.model.definition.entity.DocumentDefinition;

public interface Document<Definition extends DocumentDefinition> extends Node<Definition> {

	ClassName CLASS_NAME = new ClassName("Document");

	ClassName getClassName();

}
