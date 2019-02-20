package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.ServiceDefinition;
import client.services.http.HttpInstance;

public class ServiceDefinitionBuilder extends ProcessDefinitionBuilder<client.core.model.definition.entity.ServiceDefinition> {

	@Override
	public client.core.model.definition.entity.ServiceDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		ServiceDefinition definition = new ServiceDefinition();
		initialize(definition, instance);
		return definition;
	}

}