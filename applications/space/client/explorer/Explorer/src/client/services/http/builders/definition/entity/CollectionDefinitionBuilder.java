package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.CollectionDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.RefBuilder;

public class CollectionDefinitionBuilder extends NodeDefinitionBuilder<client.core.model.definition.entity.CollectionDefinition> {

	@Override
	public client.core.model.definition.entity.CollectionDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		CollectionDefinition definition = new CollectionDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.CollectionDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		CollectionDefinition definition = (CollectionDefinition) object;
		definition.setAddDefinition(getAddDefinition(instance.getObject("add")));
	}

	private client.core.model.definition.entity.CollectionDefinition.AddDefinition getAddDefinition(HttpInstance instance) {
		if (instance == null)
			return null;

		CollectionDefinition.AddDefinition addDefinition = new CollectionDefinition.AddDefinition();
		addDefinition.setNode(new RefBuilder().buildList(instance.getList("node")));

		return addDefinition;
	}

}