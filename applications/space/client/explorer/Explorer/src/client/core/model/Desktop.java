package client.core.model;

import client.core.model.definition.entity.DesktopDefinition;

public interface Desktop<Definition extends DesktopDefinition> extends Node<Definition> {

	ClassName CLASS_NAME = new ClassName("Desktop");

	ClassName getClassName();

	int getSingletonsCount();
	Node getSingleton(String key);

}
