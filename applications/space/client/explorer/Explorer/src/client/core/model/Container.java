package client.core.model;

import client.core.model.definition.entity.ContainerDefinition;

public interface Container<Definition extends ContainerDefinition> extends Node<Definition> {

	ClassName CLASS_NAME = new ClassName("Container");

	ClassName getClassName();

	int getChildrenCount();
	List<Node> getChildren();
	<T extends Node> T getChild(String key);
	void addChild(Node node);

}
