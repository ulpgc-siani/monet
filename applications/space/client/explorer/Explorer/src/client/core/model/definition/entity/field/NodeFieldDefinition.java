package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface NodeFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("NodeFieldDefinition");

	ContainDefinition getContain();
	AddDefinition getAdd();

	interface ContainDefinition {
		String getNode();
	}

	interface AddDefinition {
		List<String> getNodes();
	}

}
