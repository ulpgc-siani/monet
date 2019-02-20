package client.core.model.fields;

import client.core.model.definition.entity.field.NodeFieldDefinition;
import client.core.model.Field;
import client.core.model.Node;

public interface NodeField extends Field<NodeFieldDefinition, Node> {

	ClassName CLASS_NAME = new ClassName("NodeField");

	boolean isNullOrEmpty();
}
