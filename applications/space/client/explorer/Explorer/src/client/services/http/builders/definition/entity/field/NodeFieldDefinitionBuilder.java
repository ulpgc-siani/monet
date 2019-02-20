package client.services.http.builders.definition.entity.field;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.entity.field.NodeFieldDefinition;
import client.services.http.HttpInstance;

public class NodeFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.NodeFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.NodeFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		NodeFieldDefinition definition = new NodeFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.NodeFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		NodeFieldDefinition definition = (NodeFieldDefinition)object;
		definition.setContain(getContain(instance.getObject("contain")));
		definition.setAdd(getAdd(instance.getObject("add")));
	}

	private client.core.model.definition.entity.field.NodeFieldDefinition.ContainDefinition getContain(HttpInstance instance) {
		if (instance == null)
			return null;

		NodeFieldDefinition.ContainDefinition contain = new NodeFieldDefinition.ContainDefinition();
		contain.setNode(instance.getString("node"));

		return contain;
	}

	private client.core.model.definition.entity.field.NodeFieldDefinition.AddDefinition getAdd(HttpInstance instance) {
		if (instance == null)
			return null;

		NodeFieldDefinition.AddDefinition add = new NodeFieldDefinition.AddDefinition();
		List<String> nodes = new MonetList<>();

		for (int i = 0; i < instance.getArray("nodes").length(); i++)
			nodes.add((String) getArrayObject(instance, "nodes", i));

		add.setNodes(nodes);

		return add;
	}

}
