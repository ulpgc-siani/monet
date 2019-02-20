package client.core.model;

import client.core.model.definition.entity.CatalogDefinition;

public interface Catalog<Definition extends CatalogDefinition> extends Node<Definition>, Set {

	ClassName CLASS_NAME = new ClassName("Catalog");

	Index getIndex();
	ClassName getClassName();

}
