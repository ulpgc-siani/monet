package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.ContainerDefinition;
import client.services.http.HttpInstance;

public class ContainerDefinitionBuilder extends NodeDefinitionBuilder<client.core.model.definition.entity.ContainerDefinition> {

	@Override
	public client.core.model.definition.entity.ContainerDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		ContainerDefinition definition = new ContainerDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.ContainerDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		ContainerDefinition definition = (ContainerDefinition)object;
		definition.setEnvironment(instance.getBoolean("environment"));
	}
}