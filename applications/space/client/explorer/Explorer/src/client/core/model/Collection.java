package client.core.model;

import client.core.model.definition.entity.CollectionDefinition;

public interface Collection<Definition extends CollectionDefinition> extends Node<Definition>, Set {

	ClassName CLASS_NAME = new ClassName("Collection");

	ClassName getClassName();
	
}
