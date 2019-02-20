package client.core.model;

import client.core.model.definition.entity.ProcessDefinition;

import java.util.Map;

public interface Process<Definition extends ProcessDefinition> extends Task<Definition> {

	ClassName CLASS_NAME = new ClassName("Process");

	Map<String, Node> getShortcuts();
	Node getShortcut(String name);

}
